package com.skilrock.lms.coreEngine.ola;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.web.ola.CashCardPinGeneratorHelper;
import com.skilrock.lms.web.ola.OlaRummyRefundPinHelper;

public class OLARummyHelper{
	private static final long serialVersionUID = 1L;
	HashSet<Long> hPin = new HashSet<Long>();
	List<Long> listSerial = new ArrayList<Long>();
	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt2 = null;
	static Log  logger = LogFactory.getLog(OLARummyHelper.class);
	
	public FlexiCardPurchaseBean initRummyDeposit(double amount,UserInfoBean userBean, int walletId, String depositAnyWhere,
			FlexiCardPurchaseBean flexiCardPurchaseBean, Date expiryDate, String userPhone, String desKey,
			String propKey) {
	
			if(amount>0){
				Connection con = DBConnect.getConnection();
					try {
							con.setAutoCommit(false);
							flexiCardPurchaseBean = rummyDeposit(con, amount, userBean, walletId,
									depositAnyWhere, flexiCardPurchaseBean,expiryDate, userPhone, desKey, propKey);
					if(flexiCardPurchaseBean.isSuccess()){
								con.commit();
								String depositInfoStatus=sendDepositInfoToRummy(flexiCardPurchaseBean.getPlayerName(),flexiCardPurchaseBean.getSerialNumber(),flexiCardPurchaseBean.getPinNbr(),amount,userPhone);
									
								if(!depositInfoStatus.equalsIgnoreCase("success")){
									OlaRummyRefundPinHelper helper = new OlaRummyRefundPinHelper();
									String cancelReason="CANCEL_SERVER";
									String returnType =helper.refundPin(walletId,flexiCardPurchaseBean.getPinNbr(),flexiCardPurchaseBean.getSerialNumber(),flexiCardPurchaseBean.getPlayerName(),amount,desKey,propKey,con,cancelReason);
									flexiCardPurchaseBean.setSuccess(false);
									if(returnType.equalsIgnoreCase("success")){
										flexiCardPurchaseBean.setReturnType("Error In Deposit at KhelPlay Rummy : Amount Refunded Successfully");
									}
									else{
										flexiCardPurchaseBean.setReturnType(returnType);
									}
								}else{
									
									StringBuilder sb = new StringBuilder(flexiCardPurchaseBean.getSerialNumber().toString());
									String srNbr =sb.substring(0,4)+" "+sb.substring(4,8)+" "+sb.substring(8,12);// 12digit serial number
									String msg ="Dear Customer, Your Deposit Request of Amt:"+amount+" has been initiated with PlrName:"+flexiCardPurchaseBean.getPlayerName()+" and RefCode:"+srNbr+",please visit the cashier page at khelplayrummy.com to confirm deposit";
									SendSMS smsSend = new SendSMS(msg,userPhone);
									smsSend.setDaemon(true);
									smsSend.start();
									System.out.println(" SMS Sent");
								}
															
							
							}
						}
				catch(Exception e){
					e.printStackTrace();
				}
				finally {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();

					}
				}

			}
			else {
				flexiCardPurchaseBean.setSuccess(false);
				flexiCardPurchaseBean.setReturnType("Amount Should Be greater than zero");
			}

		return flexiCardPurchaseBean;

	}
	
	

	public synchronized FlexiCardPurchaseBean rummyDeposit(Connection con,double amount,
			 UserInfoBean userBean, int walletId,
			String depositAnyWhere,
			FlexiCardPurchaseBean flexiCardPurchaseBean,
			 Date expiryDate, String plrPhoneNumber,String desKey,String propKey) {
		try {
			String isBinding = OLAUtility.affiliatePlrBinding(depositAnyWhere,
			 flexiCardPurchaseBean.getPlayerName(), userBean,walletId, con); 
			logger.info("isBinding :"+isBinding);
			if (isBinding.equalsIgnoreCase("OK")) {
			
				
				
			flexiCardPurchaseBean = depositMoneyinLMSForRummy(flexiCardPurchaseBean.getPlayerName(), amount, userBean,
													walletId, flexiCardPurchaseBean,con);
			logger.info("Deposit In LMS For Rummy :"+flexiCardPurchaseBean.getReturnType());
			if (!flexiCardPurchaseBean.getReturnType().equalsIgnoreCase("true")) {
				System.out.println(flexiCardPurchaseBean.getReturnType());
				return flexiCardPurchaseBean;
			} else {
				flexiCardPurchaseBean = generateFlexiPinAndSerialNbr(flexiCardPurchaseBean, con, walletId);// Method to generate Pin and Serial													// number
				// update st_ola_pin_record
				pstmt2 = con.prepareStatement("update st_ola_pin_generation  set last_generated_serial_nbr=? where wallet_id  =? and pin_type=? ");
				pstmt2.setString(1,flexiCardPurchaseBean.getSerialNumber().toString().substring(7));// Serial of Last generated Serial Number
				pstmt2.setInt(2,walletId);
 				pstmt2.setString(3,"FLEXI");
				pstmt2.executeUpdate();
				int isUpdate = cashCardDeposit(flexiCardPurchaseBean,expiryDate, plrPhoneNumber, con,flexiCardPurchaseBean.getPlayerName(),
     										flexiCardPurchaseBean.getTransactionId(), walletId,desKey,propKey);
				if (isUpdate != 1) {
					flexiCardPurchaseBean.setSuccess(false);
					flexiCardPurchaseBean
							.setReturnType("Some Error During Pin Purchase");
					logger.info("Some Error During Cash Card Purchase");
					return flexiCardPurchaseBean;
				}
			}
		flexiCardPurchaseBean.setSuccess(true);	
		return flexiCardPurchaseBean;
			
		}
			else {
				flexiCardPurchaseBean.setSuccess(false);
				flexiCardPurchaseBean.setReturnType(isBinding);
				return flexiCardPurchaseBean;
						
			}
									
		} catch (Exception e) {
			flexiCardPurchaseBean.setReturnType("Some Error");
			e.printStackTrace();

		} 
		return flexiCardPurchaseBean;
	}
	
	
	public int cashCardDeposit(FlexiCardPurchaseBean flexiCardPurchaseBean,Date expiryDate,
					String plrPhoneNumber,Connection con,String partyId,Long transactionId,int walletId,String desKey,String propKey) throws Exception {
		CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
		String query = "insert into st_ola_pin_rep_rm_"+walletId+"(serial_number,pin_number,amount,expiry_date,player_id,player_phone_nbr,lms_transaction_id,verification_status) values(?,?,?,?,?,?,?,?) ";
		
		String   pin_nbr=helper.encryptPin(((Long) flexiCardPurchaseBean.getPinNbr()).toString(),desKey,propKey);
		logger.info("Cash Card Deposit:: Number:"+pin_nbr+"Amount:"+flexiCardPurchaseBean.getAmount()+"expiryDate"+expiryDate+"Party Id:"+partyId);
		try {			
			PreparedStatement pstmtUpdate1 = con.prepareStatement(query);
			pstmtUpdate1.setLong(1, flexiCardPurchaseBean.getSerialNumber());
			pstmtUpdate1.setString(2,pin_nbr);
			pstmtUpdate1.setDouble(3,flexiCardPurchaseBean.getAmount());
			pstmtUpdate1.setDate(4,expiryDate);
			pstmtUpdate1.setString(5,partyId);
			pstmtUpdate1.setString(6,plrPhoneNumber);
			pstmtUpdate1.setLong(7,transactionId);
			pstmtUpdate1.setString(8,"PENDING");// default verificarion_status PENDING
			int isUpdate = pstmtUpdate1.executeUpdate();			
			System.out.println(isUpdate+" :Data Inserted Into st_ola_rummy_deposit");
			return isUpdate;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		} 
	}

	
	
	private FlexiCardPurchaseBean generateFlexiPinAndSerialNbr(
			FlexiCardPurchaseBean flexiCardPurchaseBean,Connection con,int walletId) {
		
		try {
				CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
				//Get the lastGeneratedSerial count and Day Count
				String lastGeneratedSerialDayCount[] = helper.getLastGeneratedPin(walletId,con,"FLEXI");
				logger.info("Last Generated Number :"+lastGeneratedSerialDayCount[0]+"Last Day Count :"+lastGeneratedSerialDayCount[1]);
			if (lastGeneratedSerialDayCount[0] == null
					|| lastGeneratedSerialDayCount == null) {
				flexiCardPurchaseBean.setReturnType("Wallet Does Not exist");
				return flexiCardPurchaseBean;
			}
		String lastGeneratedSerial=lastGeneratedSerialDayCount[0];
		String lastGeneratedDayCount=lastGeneratedSerialDayCount[1];
		listSerial = helper.randomSerial("FLEXI", listSerial, 1,walletId,lastGeneratedSerial,lastGeneratedDayCount);
		hPin = helper.randomPin(hPin, 1);// generate 1  pin 
		List<Long> listPin = new ArrayList<Long>(hPin);
		flexiCardPurchaseBean.setPinNbr(listPin.get(0));
		flexiCardPurchaseBean.setSerialNumber(listSerial.get(0));
		logger.info("New Generated Serial :"+listSerial.get(0));
		
		return flexiCardPurchaseBean;	
		
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		return flexiCardPurchaseBean;		
		
		
	}

	public Map<String, String> verifyOrgName(String userName) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			//here call the API for check User Name Availability
			  InputStream iStream = OLAUtility.checkUserNameAvailabilityAtKhelPlayApi(userName);
			  BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
			  StringBuilder sb =new StringBuilder();
			  String line =null;
			while ((line = reader.readLine()) != null) {
			      sb.append(line);
			    }
			
			String msg = sb.toString();
			System.out.println("Verification Message"+msg);
			String success_flag = msg.split(",")[0].split(":")[1];
			// String success_msg = msg.split(",")[1].split(":")[1].split("}")[0];
			
			if(success_flag.equalsIgnoreCase("false")){
				System.out.println("User Name Not Exists !!");
				errorMap.put("userError", "User Name Not Exists !!");
				
			}
			else if(success_flag.equalsIgnoreCase("true")){
				System.out.println("User Name Availiable !!");
					errorMap.put("userError","Avail");
				}
			else {
				System.out.println("Error!!");
				errorMap.put("userError", "Some Error");
				
			}
			return errorMap;
		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}	
	
	// Added By Neeraj
	public 	FlexiCardPurchaseBean  depositMoneyinLMSForRummy(
			String userName, double depositAmt,
			UserInfoBean userBean, int walletId,FlexiCardPurchaseBean flexiCardPurchaseBean,Connection con) throws LMSException {
		
		
		//Connection con = DBConnect.getConnection();

		double retailerComm = 0.0;
		double agentComm = 0.0;
		double retNetAmt = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		int isUpdate;
		
		try {
			
			int agentOrgId = userBean.getParentOrgId();
			int retOrgId = userBean.getUserOrgId();

			retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "DEPOSIT", "RETAILER", con);
			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, agentOrgId, "DEPOSIT", "AGENT", con);

			// check with organizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(retOrgId, con);
			if (orgPwtLimit == null) { // send mail to back office
				System.out.println("OLA Limits Are Not defined Properly!!");
				throw new LMSException("OLA Limits Are Not defined Properly!!");
			}
			double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
			System.out.println("olaDepositLimit" + olaDepositLimit);
			System.out.println("ola deposite money" + depositAmt);

			if (depositAmt > olaDepositLimit) {
				System.out
						.println("Deposit amount is greater then deposit limit");
				flexiCardPurchaseBean
						.setReturnType("Deposit amount is greater then deposit limit");
				return 	flexiCardPurchaseBean ;
				// return "Deposit amount is greater then deposit limit";
			}
			// check with retailer and agent balance to deposit
			OlaHelper olahelper = new OlaHelper();
			int isCheck = olahelper.checkOrgBalance(depositAmt, retOrgId, agentOrgId,
					con, retailerComm, agentComm);
			System.out.println("ischeck" + isCheck);

			if (isCheck == -1) {
				// Agent has insufficient
				flexiCardPurchaseBean.setReturnType("Agent has insufficient");
				return 	flexiCardPurchaseBean ;
				// return "Agent has insufficient";

			} else if (isCheck == -2) {
				// Error LMS
				flexiCardPurchaseBean .setReturnType("Error LMS");
				return 		flexiCardPurchaseBean ;
				// return "Error LMS";
			} else if (isCheck == 0) {
				// Retailer has insufficient
				flexiCardPurchaseBean 
						.setReturnType("Retailer has insufficient Balance ");
				return 		flexiCardPurchaseBean;
				// return "Retailer has insufficient";
			}
			// insert in LMS transaction master
			if (isCheck == 2) {

				//String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
				//PreparedStatement pstmt = con				.prepareStatement(transMasQuery);
				String insertInLMS = QueryManager.insertInLMSTransactionMaster();
				PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
				pstmt1.setString(1, "RETAILER");
				long transactionId = 0;
				pstmt1.executeUpdate();
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					transactionId = rs1.getLong(1);					
					// insert into retailer transaction master
					pstmt1 = con
							.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
					pstmt1.setLong(1, transactionId);
					pstmt1.setInt(2, userBean.getUserId());
					pstmt1.setInt(3, userBean.getUserOrgId());
					pstmt1.setInt(4, walletId);
					java.util.Date date = new java.util.Date();
					pstmt1.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
					pstmt1.setString(6, "OLA_DEPOSIT");
					isUpdate = pstmt1.executeUpdate();

					// insert in deposit master
					retNetAmt = (depositAmt - ((depositAmt * retailerComm) / 100));
					agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));					
					String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement pstmtUpdate = con
							.prepareStatement(insertQry);
					pstmtUpdate.setLong(1, transactionId);
					pstmtUpdate.setInt(2, walletId);
					pstmtUpdate.setString(3, userName);
					pstmtUpdate.setInt(4, userBean.getUserOrgId());
					pstmtUpdate.setDouble(5, depositAmt);
					pstmtUpdate.setDouble(6, retailerComm);
					pstmtUpdate.setDouble(7, retNetAmt);
					pstmtUpdate.setDouble(8, agentComm);
					pstmtUpdate.setDouble(9, agentNetAmt);
					pstmtUpdate.setLong(10, agentRefTransactionId);
					pstmtUpdate.setString(11, "CLAIM_BAL");
					pstmtUpdate.setString(12, "WEB");
					pstmtUpdate.setLong(13, imsTransactionId);
					pstmtUpdate.executeUpdate();

					// update st_lms_organization_master for claimable balance
					// for
					// retailer
					CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
					commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
							userBean.getUserOrgId(), "CREDIT", con);

					// update st_lms_organization_master for claimable balance
					// for
					// agent
					commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
							userBean.getParentOrgId(), "CREDIT", con);					
					//con.commit();
					flexiCardPurchaseBean.setTransactionId(transactionId);	// transactionId
				}

				else {
					System.out
							.println("Trabsaction Id is not Generated in LMS transaction master");
					flexiCardPurchaseBean
							.setReturnType("error in Deposit the money");
					return 	flexiCardPurchaseBean;
					// return "error in Deposit the money";
				}
			} else {
				System.out.println("Error During balance verification");
				flexiCardPurchaseBean 
						.setReturnType("Error During balance verification");
				return 		flexiCardPurchaseBean;
				// return "Error During balance verification";
			}

			// con.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		}
		
		flexiCardPurchaseBean .setReturnType("true");
		return 		flexiCardPurchaseBean;
		// return "true";
	}

public Map<String, String> verifyPlrName(String userName) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
		
			// Call Player Mgmt Api  
			String method = "playerVerificationAction";
			JSONObject params = new JSONObject();
	        params.put("userName",userName);
	        JSONObject responseObj =Utility.sendCallApi(method, params, "5");
			if(responseObj==null){
				errorMap.put("inValid","Error In Connection With Player Lottery");
			}
			else{
				boolean isSuccess = responseObj.getBoolean("isSuccess");
				if(isSuccess){
					//Player Not Exist at Player Lottery
					errorMap.put("inValid","User Name is Invalid !!");
				}else{
					//Player Exist at Player Lottery
					errorMap.put("valid","User Name is Valid !! ");
					
				}
			}
			 
			return errorMap;
		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}		

public String sendDepositInfoToRummy(String playerName, long srNbr,
		long pinNbr, double amount, String userPhone) {
	
	DocumentBuilderFactory  docFactory = DocumentBuilderFactory.newInstance();
	try {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		// Create Root Element 
		Element root = doc.createElement("olaPlayerDepositRequest");
		doc.appendChild(root);
		// add child nodes 
		Element plrUserName =doc.createElement("plrUserName");
		plrUserName.appendChild(doc.createTextNode(playerName));
		root.appendChild(plrUserName);
		Element serialNo =doc.createElement("serialNo");
		serialNo.appendChild(doc.createTextNode(srNbr+""));
		root.appendChild(serialNo);
		Element pinNo =doc.createElement("pinNo");
		pinNo.appendChild(doc.createTextNode(pinNbr+""));
		root.appendChild(pinNo);
		Element amt =doc.createElement("amount");
		amt.appendChild(doc.createTextNode(amount+""));
		root.appendChild(amt);
		Element mobileNo =doc.createElement("mobileNo");
		mobileNo.appendChild(doc.createTextNode(userPhone));
		root.appendChild(mobileNo);
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer transformer=transFact.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter stringWriter = new StringWriter();
		transformer.transform(source,new StreamResult(stringWriter));
		// Uncomment the below code to bypass SSL
		/*SSLContext ssl_ctx = SSLContext.getInstance("TLS");
         TrustManager[ ] trust_mgr = get_trust_mgr();
         ssl_ctx.init(null,                // key manager
                      trust_mgr,           // trust manager
                      new SecureRandom()); // random number generator
         HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
         HostnameVerifier allHostsValid = new HostnameVerifier() {   
             public boolean verify(String hostname, SSLSession session) {   
                 return true;   
             }   
         };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);    */
        URL url = new URL(LMSFilterDispatcher.rummyCashierWebLink);  
	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
        conn.setRequestMethod("POST");  
        OutputStreamWriter wr = new OutputStreamWriter(conn
				.getOutputStream());
        String param1 = "olaData="+stringWriter.toString()+"";
        wr.write(param1);
        wr.flush();
        wr.close();
      
    	InputStream iStream =conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
		StringBuilder sb =new StringBuilder();
		String line =null;
				while ((line = reader.readLine()) != null) {
						sb.append(line);
						}
				
		  String msg = sb.toString();
		  System.out.println(msg);
		  stringWriter.close();
		  return msg;
		
	} catch (Exception e) {
			e.printStackTrace();
	}
	return "ERROR";
}
private TrustManager[ ] get_trust_mgr() {
    TrustManager[ ] certs = new TrustManager[ ] {
       new X509TrustManager() {
          public X509Certificate[ ] getAcceptedIssuers() { return null; }
     
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
        }
     };
     return certs;
 }

}
