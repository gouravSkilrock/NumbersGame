package com.skilrock.lms.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import javax.crypto.BadPaddingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.LSDaoImpl;

public class LSControllerImpl {
	private static Log logger = LogFactory.getLog(LSControllerImpl.class);

	private static LSControllerImpl instance = null;
	private LSControllerImpl() {
	}

	public static LSControllerImpl getInstance() {
		if (instance == null) {
			instance = new LSControllerImpl();
		}
		return instance;
	}

	private FileOutputStream fout = null;
	private final String SEPARATOR = System.getProperty("file.separator");
	private final String PRIVATE_KEY_PATH = LMSUtility.sc.getRealPath("/com/skilrock/lms/") + SEPARATOR + "private.key";

	private class Constants {
		private static final String CLASSES_PATH = "/WEB-INF/classes";
		private static final String IP = "http://192.168.124.207:80";
		private static final String NAME_SPACE = "/LicensingServer/com/skilrock/ls/common/"; 
		private static final String FILE_NAME = "checksum.txt";
		private static final String WAR_NAME = "LMS";

		private class Action {
			private static final String CHECK_SUM_ACTION = "chkSum.action";
			private static final String INVALID_KEY_ACTION = "invalidKey.action";
			private static final String MONITOR_PARAM = "monitorParam.action";
		}

		private class Params {
			private static final String DATA = "data:";
			private static final String CLIENT_TIME = "|clientTime:";
		}

		private class MailingMessages {
			private static final String SUBSCRIPTION_EXPIRED = "Dear Team, <br/><br/>Your Subscription Period is Expired.<br/> Please take appropriate action.";
			private static final String UNAUTHORIZED_ACCESS = "Dear Team, <br/><br/>Unauthorized User Access is Found.<br/> Please take appropriate action.";
			private static final String BAD_PADDING_ERROR = "Dear Team, <br/><br/>Some Unwanted Error Occured.<br/> Please take appropriate action.";
			private static final String CHECKSUM_VALIDATION_FAILED = "Dear Team, <br/><br/>Checksum Validation is Failed.<br/> Please take appropriate action.";
			private static final String SYSTEM_ID_NOT_MATCH = "Dear Team, <br/><br/>System ID calculated on LMS at {0} is - {1}<br/> Please take appropriate action.";
			private static final String IP_MISSMATCH = "Dear Team, <br/><br/>IP Not Match. calculated IP on LMS is {0}<br/> Please take appropriate action.";
			private static final String ILLEGAL_COUNTER_EXCEED = "Dear Team, <br/><br/>Illegal Expiry Counter Exceeds the Specified Limit.<br/> Please take appropriate action.";
		}
	}

	public void clientValidation() {
		boolean isLSEnable = "YES".equals(Utility.getPropertyValue("IS_LS_ENABLE")) ? true : false;
		logger.info("Is LS Enable - "+isLSEnable);

		if (isLSEnable) {
			try {
				boolean flag = LSDaoImpl.getInstance().validateGracePeriod(Constants.WAR_NAME);
				if(!flag)
					stopServer(Constants.MailingMessages.SUBSCRIPTION_EXPIRED);

				//	1. Check Sum Validation
				checksumValidation();

				//	2. Encoded Key Validation
				String key = LSDaoImpl.getInstance().fetchKey(Constants.WAR_NAME);
				decodeKey(key);

				//	3. Send Data to LS
				sendParamToLS();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void checksumValidation() {
		try {
			String finalCheckSumEncoded = getFinalChecksum();

			String result = callServer(Constants.Action.CHECK_SUM_ACTION, Constants.Params.DATA + finalCheckSumEncoded);
			if (result == null) {
				logger.info("No Response from LS");
			} else if("CONNECTION_ERROR".equals(result)) {
				logger.info("Connection Break from LS");
				boolean flag = LSDaoImpl.getInstance().validateGracePeriod(Constants.WAR_NAME);
				if(!flag)
					stopServer(Constants.MailingMessages.SUBSCRIPTION_EXPIRED);
			} else if("UNAUTHORIZED_ACCESS".equals(result)) {
				logger.info("UnAuthorized Access");
				stopServer(Constants.MailingMessages.UNAUTHORIZED_ACCESS);
			} else if("DATA_MATCH".equals(result)) {
				logger.info("Data Match from LS");
			} else if("EXIT_SYSTEM".equals(result)) {
				logger.info("CheckSum Validation Fails - EXIT_SYSTEM from LS");
				stopServer(Constants.MailingMessages.CHECKSUM_VALIDATION_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decodeKey(String key) throws Exception {
		if (key == null) {
			long clientTime = Calendar.getInstance().getTimeInMillis();
			key = callServer(Constants.Action.INVALID_KEY_ACTION, Constants.Params.DATA + key + Constants.Params.CLIENT_TIME + clientTime);
			if (key == null) {
				logger.info("No Response from LS");
			} else if("CONNECTION_ERROR".equals(key)) {
				logger.info("Connection Break from LS");
				boolean flag = LSDaoImpl.getInstance().validateGracePeriod(Constants.WAR_NAME);
				if(!flag)
					stopServer(Constants.MailingMessages.SUBSCRIPTION_EXPIRED);
			} else if("UNAUTHORIZED_ACCESS".equals(key)) {
				logger.info("UnAuthorized Access");
				stopServer(Constants.MailingMessages.UNAUTHORIZED_ACCESS);
			} else {
				String[] dataArr = key.split("Nxt");
				String encodeKey = dataArr[0];
				key = encodeKey;
				LSDaoImpl.getInstance().updateKey(encodeKey, Constants.WAR_NAME);

				if (dataArr.length > 1) {
					String expiryCounter = dataArr[1];
					illegalCounterAction(expiryCounter);
				}
			}
		}

		try {
			String encodeText = new String(ClientMain.hexToChar(key.toCharArray()));
			logger.info("Encoded Text - " + encodeText);

			PrivateKey privateKey = ClientMain.readPrivateKeyFromFile(PRIVATE_KEY_PATH);
			logger.info("Private Key - " + privateKey);

			BASE64Decoder decoder = new BASE64Decoder();
			byte decoded[] = decoder.decodeBuffer(encodeText);
			logger.info("Decoded - " + new String(decoded));
			String decodeText = new String(ClientMain.rsaDecrypt(privateKey, decoded));
			logger.info("(TEMP) Decoded Text - " + decodeText);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			String[] decodedKeyArr = decodeText.split("\\|");
			//int merchantId = Integer.parseInt(decodedKeyArr[0]);
			String ipAddress = decodedKeyArr[1];
			String systemId = decodedKeyArr[2];
			Date fromDate = dateFormat.parse(decodedKeyArr[3]);
			Date toDate = dateFormat.parse(decodedKeyArr[4]);
			int expiryPeriod = Integer.parseInt(decodedKeyArr[5]);

			// Motherboard Serial No. Verification.
			String localSystemId = calculateSystemId();
			if(localSystemId == null || localSystemId.length()==0)
				localSystemId = "null";
			logger.info("System Id - "+systemId+" | Local System Id - "+localSystemId);
			if (!systemId.equals(localSystemId)) {
				logger.info("System ID Does Not Match");

				stopServer(Constants.MailingMessages.SYSTEM_ID_NOT_MATCH, calculateIP(), localSystemId);
			}

			// IP Validation
			String calculateIP = calculateIP();
			logger.info("IP Address - "+ipAddress+" | Calculate IP - "+calculateIP);
			if (!ipAddress.equals(calculateIP)) {
				logger.info("IP Address Does Not Match");

				stopServer(Constants.MailingMessages.IP_MISSMATCH, calculateIP());
			}

			// Date Expiry Validation.
			if (!validateKey(fromDate, toDate, expiryPeriod)) {
				String isGracePeriod = Utility.getPropertyFromDB("IS_GRACE_PERIOD");
				if("NO".equals(isGracePeriod))
					LSDaoImpl.getInstance().setGracePeriod(true, toDate, Constants.WAR_NAME);

				boolean flag = LSDaoImpl.getInstance().validateGracePeriod(Constants.WAR_NAME);
				if(!flag)
					stopServer(Constants.MailingMessages.SUBSCRIPTION_EXPIRED);
			} else
				LSDaoImpl.getInstance().setGracePeriod(false, null, Constants.WAR_NAME);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			stopServer(Constants.MailingMessages.BAD_PADDING_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendParamToLS() throws Exception {
		Map<String, String> paramMap = LSDaoImpl.getInstance().getLMSParamMap();

		String result = callServer(Constants.Action.MONITOR_PARAM, Constants.Params.DATA + paramMap);
		if (result == null) {
			logger.info("No Response from LS");
		} else if("CONNECTION_ERROR".equals(result)) {
			logger.info("Connection Break from LS");
			boolean flag = LSDaoImpl.getInstance().validateGracePeriod(Constants.WAR_NAME);
			if(!flag)
				stopServer(Constants.MailingMessages.SUBSCRIPTION_EXPIRED);
		} else if("UNAUTHORIZED_ACCESS".equals(result)) {
			logger.info("UnAuthorized Access");
			stopServer(Constants.MailingMessages.UNAUTHORIZED_ACCESS);
		} else {
			logger.info("Monitoring Result - " + result);
		}
	}

	public String getFinalChecksum() {
		File source = new File(LMSUtility.sc.getRealPath(Constants.CLASSES_PATH));
		String filePath = source.getAbsolutePath() + SEPARATOR + Constants.FILE_NAME;
		String finalCheckSumEncoded = null;
		try {
			fout = new FileOutputStream(filePath);
			checkFiles(source);
			long totalCheckSum = getCheckSum(filePath);
			fout.close();
			finalCheckSumEncoded = MD5Encoder.encode(((Long) totalCheckSum).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalCheckSumEncoded;
	}

	private void checkFiles(File source) throws Exception {
		try {
			if (source.isDirectory()) {
				String files[] = source.list();
				for (String file : files)
					checkFiles(new File(source, file));
			} else {
				String newSource = source.getAbsolutePath().replace('\\', '/');
				if (newSource.contains(".class"))
					fout.write(String.valueOf(getCheckSum(newSource)).getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	private long getCheckSum(String source) throws IOException {
		FileInputStream fis = new FileInputStream(source);
		CheckedInputStream cis = new CheckedInputStream(fis, new CRC32());
		BufferedInputStream bis = new BufferedInputStream(cis);
		while (bis.read() != -1) {
		}
		long checkSum = cis.getChecksum().getValue();
		bis.close();
		cis.close();
		fis.close();

		return checkSum;
	}

	private boolean validateKey(Date fromDate, Date toDate, int expiryPeriod) {
		Date currentDate = new Date();
		try {
			if (expiryPeriod > 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(toDate);
				calendar.add(Calendar.DAY_OF_MONTH, expiryPeriod);
				toDate = new Date(calendar.getTimeInMillis());
			}

			if (currentDate.getTime() >= fromDate.getTime() && currentDate.getTime() <= toDate.getTime())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private String callServer(String actionName, String urlData) throws Exception {
		URL url = null;
		URLConnection connection = null;
		String response = null;
		try {
			String dataArr[] = urlData.split("\\|");
			StringBuilder urlStr = new StringBuilder();
			for (String data : dataArr) {
				String encodeData[] = data.split(":");
				urlStr.append(URLEncoder.encode(encodeData[0], "UTF-8") + "=" + URLEncoder.encode(encodeData[1], "UTF-8") + "&");
			}
			urlStr.deleteCharAt(urlStr.length() - 1);

			// Send data to Licensing Server
			url = new URL(Constants.IP + Constants.NAME_SPACE + actionName);
			connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();

			// Get the response from Licensing Server
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			response = rd.readLine();
			rd.close();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "CONNECTION_ERROR";
		}

		return response;
	}

	private String calculateIP() throws UnknownHostException {
		String result = "";
		String osName = System.getProperty("os.name");
		if (osName != null) {
			if (osName.contains("Window")) {
				result = InetAddress.getLocalHost().getHostAddress();
			} else if (osName.contains("Linux")) {
				result = calculateIPLinux();
			}
		}
		logger.info("Calculated IP - " + result);
		return result;
	}

	private static String calculateIPLinux() {
		String result = null;
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "sh", "-c", "ifconfig eth0 | grep 'inet addr:'| cut -d: -f2 | awk '{ print $1}'" });
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((result = input.readLine()) != null)
				input.close();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String calculateSystemId() {
		String result = "";
		String osName = System.getProperty("os.name");
		if (osName != null) {
			if (osName.contains("Window")) {
				result = calculateSystemIdWindows();
			} else if (osName.contains("Linux")) {
				result = calculateSystemIdLinux();
			}
		}
		logger.info("System ID - " + result);
		return result;
	}

	private String calculateSystemIdWindows() {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String motherboardInfoVbs = "strComputer = \".\" \n"
					+ "Set objWMIService = GetObject(\"winmgmts:\\\\\" & strComputer & \"\\root\\CIMV2\") \n"
					+ "Set colItems = objWMIService.ExecQuery(\"SELECT * FROM Win32_BaseBoard\",,48)\n"
					+ "For Each objItem in colItems \n"
					+ "Wscript.Echo objItem.SerialNumber \n" + "Next \n";
			fw.write(motherboardInfoVbs);
			fw.close();

			Process process = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private String calculateSystemIdLinux() {
		String result = null;
		try {
			Process process = Runtime.getRuntime().exec("dmidecode -t 1");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = null;
			Map<String, String> infoMap = new HashMap<String, String>();
			while ((line = input.readLine()) != null) {
				String[] lineArr = line.split("\\:");
				if (lineArr.length != 1) {
					infoMap.put(lineArr[0].trim(), lineArr[1].trim());
				}
			}
			String serialNo = infoMap.get("Serial Number");
			if ("System Serial Number".equalsIgnoreCase(serialNo)) {
				Process process2 = Runtime.getRuntime().exec("sudo -S dmidecode -t 2");
				BufferedReader input2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
				BufferedReader err2 = new BufferedReader(new InputStreamReader(process2.getErrorStream()));

				Map<String, String> infoMap2 = new HashMap<String, String>();
				String line2;
				while ((line2 = input2.readLine()) != null) {
					String[] lineArr2 = line2.split("\\:");
					if (lineArr2.length != 1) {
						infoMap2.put(lineArr2[0].trim(), lineArr2[1].trim());
					}
				}
				String serialNo2 = infoMap2.get("Serial Number");
				result = serialNo2;

				String error2;
				String errResult2 = "";
				while ((error2 = err2.readLine()) != null) {
					errResult2 += error2;
				}
			} else {
				result = serialNo;
			}

			String error;
			String errResult = "";
			while ((error = err.readLine()) != null) {
				errResult += error;
			}
			input.close();
			err.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void illegalCounterAction(String expiryCounter) {
		logger.info("Illegal Expiry Counter - " + expiryCounter);
		if (expiryCounter != null) {
			try {
				if (Integer.parseInt(expiryCounter) > 20) {
					stopServer(Constants.MailingMessages.ILLEGAL_COUNTER_EXCEED);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void stopServer(String messagePattern, Object... params) {
		String mailingMessage = MessageFormat.format(messagePattern, params);
		logger.info("Email Message - "+mailingMessage);

		String[] emailIDs = Utility.getPropertyFromDB("LS_MAILING_USERS").split(",");
		if(emailIDs.length>0) {
			for(String emailID : emailIDs) {
				MailSend mailSend = new MailSend(emailID.trim(), mailingMessage);
				mailSend.setDaemon(true);
				mailSend.start();
			}
		}

		logger.info("Exit from System IN 5 sec.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}
}