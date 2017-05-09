package com.skilrock.lms.embedded.loginMgmt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */

public class ConnectionTest extends ActionSupport implements
ServletRequestAware, ServletResponseAware, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String terminalId;
	private String OperatorName;
	private int pingTime;
	private long startTimeMTN;
	private long startTimeZain;
	private long startTimeGlobacom;
	private long startTimeEtisalat;
	private long startTimeYU;
	private long startTimeORANGE;
	private long startTimeSAFARICOM;
	private long startTimeECONET;
	private long startTimeVODACOM;
	private long startTimeAIRTEL;
	private long startTimeTIGO_TZ;
	private long startTimeZANTEL;
	private int pingTimeMTN;
	private int pingTimeZain;
	private int pingTimeGlobacom;
	private int pingTimeEtisalat;
	private int pingTimeYU;
	private int pingTimeORANGE;
	private int pingTimeSAFARICOM;
	private int pingTimeECONET;
	private int pingTimeVODACOM;
	private int pingTimeAIRTEL;
	private int pingTimeTIGO_TZ;
	private int pingTimeZANTEL;	
	static Thread tMTN = null;
	static Thread tZain = null;
	static Thread tGlobacom = null;
	static Thread tEtisalat = null;
	static Thread tYU = null;
	static Thread tORANGE = null;
	static Thread tSAFARICOM = null;
	static Thread tECONET = null;
	static Thread tVODACOM = null;
	static Thread tAIRTEL = null;
	static Thread tTIGO_TZ = null;
	static Thread tZANTEL = null;
	private static int countMTN;
	private static int countZain; 
	private static int countGlobacom;
	private static int countEtisalat;
	private static int countYU;
	private static int countORANGE;
	private static int countSAFARICOM;
	private static int countECONET;
	private static int countVODACOM;
	private static int countAIRTEL;
	private static int countTIGO_TZ;
	private static int countZANTEL;
	private String IP;
	private int Port;
	private String Project_Name;
	private String date;
	private String time;
	private String CID;
	private String LAC;
	private String LRespTime;
	private String Signal_Level;
	private String reqCounter;
	private String respCounter;
	private String SIM_1;
	private String SIM_2;
	private String requestLength;
	private String responseLength;
	private String GPRSEnableTime;


	public String getGPRSEnableTime() {
		return GPRSEnableTime;
	}

	public void setGPRSEnableTime(String gPRSEnableTime) {
		GPRSEnableTime = gPRSEnableTime;
	}

	public String getRequestLength() {
		return requestLength;
	}

	public void setRequestLength(String requestLength) {
		this.requestLength = requestLength;
	}

	public String getResponseLength() {
		return responseLength;
	}

	public void setResponseLength(String responseLength) {
		this.responseLength = responseLength;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLRespTime() {
		return LRespTime;
	}

	public void setLRespTime(String lRespTime) {
		LRespTime = lRespTime;
	}

	public String getReqCounter() {
		return reqCounter;
	}

	public void setReqCounter(String reqCounter) {
		this.reqCounter = reqCounter;
	}

	public String getRespCounter() {
		return respCounter;
	}

	public void setRespCounter(String respCounter) {
		this.respCounter = respCounter;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public String getProject_Name() {
		return Project_Name;
	}

	public void setProject_Name(String projectName) {
		Project_Name = projectName;
	}

	public String getCID() {
		return CID;
	}

	public void setCID(String cID) {
		CID = cID;
	}

	public String getLAC() {
		return LAC;
	}

	public void setLAC(String lAC) {
		LAC = lAC;
	}

	public String getSignal_Level() {
		return Signal_Level;
	}

	public void setSignal_Level(String signalLevel) {
		Signal_Level = signalLevel;
	}

	public String getSIM_1() {
		return SIM_1;
	}

	public void setSIM_1(String sIM_1) {
		SIM_1 = sIM_1;
	}

	public String getSIM_2() {
		return SIM_2;
	}

	public void setSIM_2(String sIM_2) {
		SIM_2 = sIM_2;
	}

	public int getPingTimeMTN() {
		return pingTimeMTN;
	}

	public void setPingTimeMTN(int pingTimeMTN) {
		this.pingTimeMTN = pingTimeMTN;
	}

	public int getPingTimeZain() {
		return pingTimeZain;
	}

	public void setPingTimeZain(int pingTimeZain) {
		this.pingTimeZain = pingTimeZain;
	}

	public int getPingTimeGlobacom() {
		return pingTimeGlobacom;
	}

	public void setPingTimeGlobacom(int pingTimeGlobacom) {
		this.pingTimeGlobacom = pingTimeGlobacom;
	}

	public int getPingTimeEtisalat() {
		return pingTimeEtisalat;
	}

	public void setPingTimeEtisalat(int pingTimeEtisalat) {
		this.pingTimeEtisalat = pingTimeEtisalat;
	}

	public int getPingTimeYU() {
		return pingTimeYU;
	}

	public void setPingTimeYU(int pingTimeYU) {
		this.pingTimeYU = pingTimeYU;
	}

	public int getPingTimeORANGE() {
		return pingTimeORANGE;
	}

	public void setPingTimeORANGE(int pingTimeORANGE) {
		this.pingTimeORANGE = pingTimeORANGE;
	}

	public int getPingTimeSAFARICOM() {
		return pingTimeSAFARICOM;
	}

	public void setPingTimeSAFARICOM(int pingTimeSAFARICOM) {
		this.pingTimeSAFARICOM = pingTimeSAFARICOM;
	}

	public int getPingTimeECONET() {
		return pingTimeECONET;
	}

	public void setPingTimeECONET(int pingTimeECONET) {
		this.pingTimeECONET = pingTimeECONET;
	}

	public int getPingTimeVODACOM() {
		return pingTimeVODACOM;
	}

	public void setPingTimeVODACOM(int pingTimeVODACOM) {
		this.pingTimeVODACOM = pingTimeVODACOM;
	}

	public int getPingTimeAIRTEL() {
		return pingTimeAIRTEL;
	}

	public void setPingTimeAIRTEL(int pingTimeAIRTEL) {
		this.pingTimeAIRTEL = pingTimeAIRTEL;
	}

	public int getPingTimeTIGO_TZ() {
		return pingTimeTIGO_TZ;
	}

	public void setPingTimeTIGO_TZ(int pingTimeTIGOTZ) {
		pingTimeTIGO_TZ = pingTimeTIGOTZ;
	}

	public int getPingTimeZANTEL() {
		return pingTimeZANTEL;
	}

	public void setPingTimeZANTEL(int pingTimeZANTEL) {
		this.pingTimeZANTEL = pingTimeZANTEL;
	}

	public String getOperatorName() {
		return OperatorName;
	}

	public void setOperatorName(String operatorName) {
		OperatorName = operatorName;
	}

	public int getPingTime() {
		return pingTime;
	}

	public void setPingTime(int pingTime) {
		this.pingTime = pingTime;
	}

	public long getStartTimeMTN() {
		return startTimeMTN;
	}

	public void setStartTimeMTN(long startTimeMTN) {
		this.startTimeMTN = startTimeMTN;
	}

	public long getStartTimeZain() {
		return startTimeZain;
	}

	public void setStartTimeZain(long startTimeZain) {
		this.startTimeZain = startTimeZain;
	}

	public long getStartTimeGlobacom() {
		return startTimeGlobacom;
	}

	public void setStartTimeGlobacom(long startTimeGlobacom) {
		this.startTimeGlobacom = startTimeGlobacom;
	}

	public long getStartTimeEtisalat() {
		return startTimeEtisalat;
	}

	public void setStartTimeEtisalat(long startTimeEtisalat) {
		this.startTimeEtisalat = startTimeEtisalat;
	}

	public long getStartTimeYU() {
		return startTimeYU;
	}

	public void setStartTimeYU(long startTimeYU) {
		this.startTimeYU = startTimeYU;
	}

	public long getStartTimeORANGE() {
		return startTimeORANGE;
	}

	public void setStartTimeORANGE(long startTimeORANGE) {
		this.startTimeORANGE = startTimeORANGE;
	}

	public long getStartTimeSAFARICOM() {
		return startTimeSAFARICOM;
	}

	public void setStartTimeSAFARICOM(long startTimeSAFARICOM) {
		this.startTimeSAFARICOM = startTimeSAFARICOM;
	}

	public long getStartTimeECONET() {
		return startTimeECONET;
	}

	public void setStartTimeECONET(long startTimeECONET) {
		this.startTimeECONET = startTimeECONET;
	}

	public long getStartTimeVODACOM() {
		return startTimeVODACOM;
	}

	public void setStartTimeVODACOM(long startTimeVODACOM) {
		this.startTimeVODACOM = startTimeVODACOM;
	}

	public long getStartTimeAIRTEL() {
		return startTimeAIRTEL;
	}

	public void setStartTimeAIRTEL(long startTimeAIRTEL) {
		this.startTimeAIRTEL = startTimeAIRTEL;
	}

	public long getStartTimeTIGO_TZ() {
		return startTimeTIGO_TZ;
	}

	public void setStartTimeTIGO_TZ(long startTimeTIGOTZ) {
		startTimeTIGO_TZ = startTimeTIGOTZ;
	}

	public long getStartTimeZANTEL() {
		return startTimeZANTEL;
	}

	public void setStartTimeZANTEL(long startTimeZANTEL) {
		this.startTimeZANTEL = startTimeZANTEL;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public long getMinsDiff(long startTime, long endTime){
		return (endTime - startTime)/60000;
	}

	public void ping() throws IOException, SQLException {
		if("Terminal-930G".equalsIgnoreCase(request.getHeader("User-Agent"))){
			if("MTN".equalsIgnoreCase(OperatorName)){
				if(tMTN == null || Thread.State.TERMINATED == tMTN.getState())
					tMTN = new Thread(this, "ThreadMTN");
			}
			else if("Zain".equalsIgnoreCase(OperatorName)){
				if(tZain == null || Thread.State.TERMINATED == tZain.getState())
					tZain = new Thread(this, "ThreadZain");
			}
			else if("Globacom".equalsIgnoreCase(OperatorName)){
				if(tGlobacom == null || Thread.State.TERMINATED == tGlobacom.getState())
					tGlobacom = new Thread(this, "ThreadGlobacom");
			}
			else if("Etisalat".equalsIgnoreCase(OperatorName)){
				if(tEtisalat == null || Thread.State.TERMINATED == tEtisalat.getState())
					tEtisalat = new Thread(this, "ThreadEtisalat");
			}
			else if("YU".equalsIgnoreCase(OperatorName)){
				if(tYU == null || Thread.State.TERMINATED == tYU.getState())
					tYU = new Thread(this, "ThreadYU");
			}
			else if("ORANGE".equalsIgnoreCase(OperatorName)){
				if(tORANGE == null || Thread.State.TERMINATED == tORANGE.getState())
					tORANGE = new Thread(this, "ThreadORANGE");
			}
			else if("SAFARICOM".equalsIgnoreCase(OperatorName)){
				if(tSAFARICOM == null || Thread.State.TERMINATED == tSAFARICOM.getState())
					tSAFARICOM = new Thread(this, "ThreadSAFARICOM");
			}
			else if("ECONET".equalsIgnoreCase(OperatorName)){
				if(tECONET == null || Thread.State.TERMINATED == tECONET.getState())
					tECONET = new Thread(this, "ThreadECONET");
			}
			else if("VODACOM".equalsIgnoreCase(OperatorName)){
				if(tVODACOM == null || Thread.State.TERMINATED == tVODACOM.getState())
					tVODACOM = new Thread(this, "ThreadVODACOM");
			}
			else if("AIRTEL".equalsIgnoreCase(OperatorName)){
				if(tAIRTEL == null || Thread.State.TERMINATED == tAIRTEL.getState())
					tAIRTEL = new Thread(this, "ThreadAIRTEL");
			}
			else if("TIGO TZ".equalsIgnoreCase(OperatorName)){
				if(tTIGO_TZ == null || Thread.State.TERMINATED == tTIGO_TZ.getState())
					tTIGO_TZ = new Thread(this, "ThreadTIGO_TZ");
			}
			else if("ZANTEL".equalsIgnoreCase(OperatorName)){
				if(tZANTEL == null || Thread.State.TERMINATED == tZANTEL.getState())
					tZANTEL = new Thread(this, "ThreadZANTEL");	
			}
			boolean flagDB = true;
			boolean flagDG = true;
			long currentTime = new Date().getTime();
			if("MTN".equalsIgnoreCase(OperatorName)){
				if(!tMTN.isAlive())
					tMTN.start();
				setStartTimeMTN(currentTime);
				setPingTimeMTN(pingTime);
			}
			else if("Zain".equalsIgnoreCase(OperatorName)){
				if(!tZain.isAlive())
					tZain.start();
				setStartTimeZain(currentTime);
				setPingTimeZain(pingTime);
			}
			else if("Globacom".equalsIgnoreCase(OperatorName)){
				if(!tGlobacom.isAlive())
					tGlobacom.start();
				setStartTimeGlobacom(currentTime);
				setPingTimeGlobacom(pingTime);
			}
			else if("Etisalat".equalsIgnoreCase(OperatorName)){
				if(!tEtisalat.isAlive())
					tEtisalat.start();
				setStartTimeEtisalat(currentTime);
				setPingTimeEtisalat(pingTime);
			}
			else if("YU".equalsIgnoreCase(OperatorName)){
				if(!tYU.isAlive())
					tYU.start();
				setStartTimeYU(currentTime);
				setPingTimeYU(pingTime);
			}
			else if("ORANGE".equalsIgnoreCase(OperatorName)){
				if(!tORANGE.isAlive())
					tORANGE.start();
				setStartTimeORANGE(currentTime);
				setPingTimeORANGE(pingTime);
			}
			else if("SAFARICOM".equalsIgnoreCase(OperatorName)){
				if(!tSAFARICOM.isAlive())
					tSAFARICOM.start();
				setStartTimeSAFARICOM(currentTime);
				setPingTimeSAFARICOM(pingTime);
			}
			else if("ECONET".equalsIgnoreCase(OperatorName)){
				if(!tECONET.isAlive())
					tECONET.start();
				setStartTimeECONET(currentTime);
				setPingTimeECONET(pingTime);
			}
			else if("VODACOM".equalsIgnoreCase(OperatorName)){
				if(!tVODACOM.isAlive())
					tVODACOM.start();
				setStartTimeVODACOM(currentTime);
				setPingTimeVODACOM(pingTime);
			}
			else if("AIRTEL".equalsIgnoreCase(OperatorName)){
				if(!tAIRTEL.isAlive())
					tAIRTEL.start();
				setStartTimeAIRTEL(currentTime);
				setPingTimeAIRTEL(pingTime);
			}
			else if("TIGO TZ".equalsIgnoreCase(OperatorName)){
				if(!tTIGO_TZ.isAlive())
					tTIGO_TZ.start();
				setStartTimeTIGO_TZ(currentTime);
				setPingTimeTIGO_TZ(pingTime);
			}
			else if("ZANTEL".equalsIgnoreCase(OperatorName)){
				if(!tZANTEL.isAlive())
					tZANTEL.start();
				setStartTimeZANTEL(currentTime);
				setPingTimeZANTEL(pingTime);
			}
			Connection con = DBConnect.getConnection();
			Statement st = con.createStatement();
			int i = st.executeUpdate("insert into st_ping_test (test) values ('" + OperatorName + "')");
			if(i<=0)
				flagDB = false;
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAWGAME);
			sReq.setServiceMethod(ServiceMethodName.PING);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			String str = (String) sRes.getResponseData();
			if(!"connected".equalsIgnoreCase(str))
				flagDG = false;
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
			fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			if(flagDB && flagDG) {
				response.getOutputStream().write("Ping successful.".getBytes());
			}
			else if (!flagDB){
				response.getOutputStream().write(("ErrorCode:01|GMT:" + fmt.format(timestamp)).getBytes());
				System.out.println("Ping unsuccessful. DB not functioning properly");
			}
			else if (!flagDG){
				response.getOutputStream().write(("ErrorCode:02|GMT:" + fmt.format(timestamp)).getBytes());
				System.out.println("Ping unsuccessful. DGE not functioning properly");
			}
			else if (!flagDG && !flagDB){
				response.getOutputStream().write(("ErrorCode:03|GMT:" + fmt.format(timestamp)).getBytes());
				System.out.println("Ping unsuccessful. DGE & DB both not functioning properly");
			}
		}
	}
 
	public void test() throws IOException {
		System.out.println("Inside Connection Test....");
		new TestHelper().storeData(IP,Port,Project_Name,date,time,CID,LAC,LRespTime,Signal_Level,reqCounter,respCounter,SIM_1,SIM_2,requestLength,responseLength,GPRSEnableTime);
		response.getOutputStream().write("Your data has been inserted succesfully and sending 512 bytes Your data has been inserted succesfully and sending 512 bytes Your data has been inserted succesfully and sending 512 bytesYour data has been inserted succesfully and sending 512 bytesYour data has been inserted succesfully and sending 512 bytesYour data has been inserted succesfully and sending 512 bytes Your data has been inserted succesfully and sending 512 bytesYour data has been inserted succesfully and sending 512 bytesYour data has been inserted succesfully and sending 512 bytes".getBytes());
	}

	public void stopThread() {
		if("MTN".equalsIgnoreCase(OperatorName)){
			if(tMTN.isAlive())
				tMTN.interrupt();
		}
		else if("Zain".equalsIgnoreCase(OperatorName)){
			if(tZain.isAlive())
				tZain.interrupt();
		}
		else if("Globacom".equalsIgnoreCase(OperatorName)){
			if(tGlobacom.isAlive())
				tGlobacom.interrupt();
		}
		else if("Etisalat".equalsIgnoreCase(OperatorName)){
			if(tEtisalat.isAlive())
				tEtisalat.interrupt();
		}
		else if("YU".equalsIgnoreCase(OperatorName)){
			if(tYU.isAlive())
				tYU.interrupt();
		}
		else if("ORANGE".equalsIgnoreCase(OperatorName)){
			if(tORANGE.isAlive())
				tORANGE.interrupt();
		}
		else if("SAFARICOM".equalsIgnoreCase(OperatorName)){
			if(tSAFARICOM.isAlive())
				tSAFARICOM.interrupt();
		}
		else if("ECONET".equalsIgnoreCase(OperatorName)){
			if(tECONET.isAlive())
				tECONET.interrupt();;
		}
		else if("VODACOM".equalsIgnoreCase(OperatorName)){
			if(tVODACOM.isAlive())
				tVODACOM.interrupt();
		}
		else if("AIRTEL".equalsIgnoreCase(OperatorName)){
			if(tAIRTEL.isAlive())
				tAIRTEL.interrupt();
		}
		else if("TIGO TZ".equalsIgnoreCase(OperatorName)){
			if(tTIGO_TZ.isAlive())
				tTIGO_TZ.interrupt();
		}
		else if("ZANTEL".equalsIgnoreCase(OperatorName)){
			if(tZANTEL.isAlive())
				tZANTEL.interrupt();
		}
	}
	
	public void run() {
		countMTN = 0;
		countZain = 0;
		countGlobacom = 0;
		countEtisalat = 0;
		countYU = 0;
		countORANGE = 0;
		countSAFARICOM = 0;
		countECONET = 0;
		countVODACOM = 0;
		countAIRTEL = 0;
		countTIGO_TZ = 0;
		countZANTEL = 0;
		String threadName = Thread.currentThread().getName();
		PropertyLoader.loadProperties("config/LMS.properties");
		String emailAdd = PropertyLoader.getProperty("email");
		try{
			while(!Thread.currentThread().isInterrupted()) {
				boolean flag = false;
				long currentTime = new Date().getTime();
				String emailMsgTxt = null;
				if("ThreadMTN".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeMTN, currentTime) >= pingTimeMTN){
						emailMsgTxt = "No ping at server for " + pingTimeMTN + " minutes from operator MTN";
						flag = true;
						countMTN++;
					}
				}
				else if("ThreadZain".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeZain, currentTime) >= pingTimeZain){
						emailMsgTxt = "No ping at server for " + pingTimeZain + " minutes from operator Zain";
						flag = true;
						countZain++;
					}			
				}
				else if("ThreadGlobacom".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeGlobacom, currentTime) >= pingTimeGlobacom){
						emailMsgTxt = "No ping at server for " + pingTimeGlobacom + " minutes from operator Globacom";
						flag = true;
						countGlobacom++;
					}			
				}
				else if("ThreadEtisalat".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeEtisalat, currentTime) >= pingTimeEtisalat){
						emailMsgTxt = "No ping at server for " + pingTimeEtisalat + " minutes from operator Etisalat";
						flag = true;
						countEtisalat++;
					}			
				}
				else if("ThreadYU".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeYU, currentTime) >= pingTimeYU){
						emailMsgTxt = "No ping at server for " + pingTimeYU + " minutes from operator YU";
						flag = true;
						countYU++;
					}			
				}
				else if("ThreadORANGE".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeORANGE, currentTime) >= pingTimeORANGE){
						emailMsgTxt = "No ping at server for " + pingTimeORANGE + " minutes from operator Orange";
						flag = true;
						countORANGE++;
					}			
				}
				else if("ThreadSAFARICOM".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeSAFARICOM, currentTime) >= pingTimeSAFARICOM){
						emailMsgTxt = "No ping at server for " + pingTimeSAFARICOM + " minutes from operator Safaricom";
						flag = true;
						countSAFARICOM++;
					}			
				}
				else if("ThreadECONET".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeECONET, currentTime) >= pingTimeECONET){
						emailMsgTxt = "No ping at server for " + pingTimeECONET + " minutes from operator Econet";
						flag = true;
						countECONET++;
					}			
				}
				else if("ThreadVODACOM".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeVODACOM, currentTime) >= pingTimeVODACOM){
						emailMsgTxt = "No ping at server for " + pingTimeVODACOM + " minutes from operator Vodacom";
						flag = true;
						countVODACOM++;
					}			
				}
				else if("ThreadAIRTEL".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeAIRTEL, currentTime) >= pingTimeAIRTEL){
						emailMsgTxt = "No ping at server for " + pingTimeAIRTEL + " minutes from operator Airtel";
						flag = true;
						countAIRTEL++;
					}			
				}
				else if("ThreadTIGO_TZ".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeTIGO_TZ, currentTime) >= pingTimeTIGO_TZ){
						emailMsgTxt = "No ping at server for " + pingTimeTIGO_TZ + " minutes from operator Tigo TZ";
						flag = true;
						countTIGO_TZ++;
					}			
				}
				else if("ThreadZANTEL".equalsIgnoreCase(threadName)) {
					if(getMinsDiff(startTimeZANTEL, currentTime) >= pingTimeZANTEL){
						emailMsgTxt = "No ping at server for " + pingTimeZANTEL + " minutes from operator Zantel";
						flag = true;
						countZANTEL++;
					}			
				}
				if (flag){
					MailSend mailSend = new MailSend(emailAdd, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();
					flag = false;
				}
				Thread.sleep(60000);
				if(countMTN >= 3)
					tMTN.interrupt();
				else if(countZain >= 3)
					tZain.interrupt();
				else if(countGlobacom >= 3)
					tGlobacom.interrupt();
				else if(countEtisalat >= 3)
					tEtisalat.interrupt();
				else if(countYU >= 3)
					tYU.interrupt();
				else if(countORANGE >= 3)
					tORANGE.interrupt();
				else if(countSAFARICOM >= 3)
					tSAFARICOM.interrupt();
				else if(countECONET >= 3)
					tECONET.interrupt();
				else if(countVODACOM >= 3)
					tVODACOM.interrupt();
				else if(countAIRTEL >= 3)
					tAIRTEL.interrupt();
				else if(countTIGO_TZ >= 3)
					tTIGO_TZ.interrupt();
				else if(countZANTEL >= 3)
					tZANTEL.interrupt();
			}	
		}
		catch(InterruptedException e){
			System.out.println(Thread.currentThread().getName() + " exiting.");
		}
	}
}