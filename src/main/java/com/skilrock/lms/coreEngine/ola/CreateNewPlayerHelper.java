package com.skilrock.lms.coreEngine.ola;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OlaGetPendingWithdrawalDetailsBean;
import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.ola.common.OLAClient;
import com.skilrock.lms.coreEngine.ola.common.OLAConstants;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.web.ola.CashCardPinGeneratorHelper;
import com.skilrock.lms.web.ola.OlaRummyRefundPinHelper;
public class CreateNewPlayerHelper {
	
	Log logger =LogFactory.getLog(CreateNewPlayerHelper.class);
	public String savePlayerDetails(int walletId,String walletName,UserInfoBean userBean,String depositAnyWhere,OlaPlayerDetailsBean playerBean,double depositAmount,String rootPath) throws LMSException{
		try{
			boolean isPendingData = false;
			String countryCode = null;
		Connection con = DBConnect.getConnection();
		con.setAutoCommit(false);
		Statement stmt = con.createStatement();
			String getCountryCode = QueryManager.getST3CountryCode()
			+ " where name='" + playerBean.getCountry() + "' ";
			ResultSet rs = stmt.executeQuery(getCountryCode);
			while(rs.next())
			{
				countryCode = rs.getString("country_code");
			}
			String responseData = OLAUtility.newPlayerRegistration(playerBean,countryCode);
			if(responseData.equalsIgnoreCase("OK"))
			{
				OlaHelper helper = new OlaHelper();
				OlaGetPendingWithdrawalDetailsBean bean = null;
				bean = helper.depositMoney(playerBean.getUsername(), depositAmount, walletName, userBean, walletId, depositAnyWhere,bean,isPendingData,rootPath);
				if(bean.getReturnType().equalsIgnoreCase("true"))
				{
				String insQry = "insert into st_ola_player_master(username,date_of_birth, password, email, phone, address, city, state, country, status, registration_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement statement = con.prepareStatement(insQry);
				statement.setString(1, playerBean.getUsername());
				statement.setString(2, playerBean.getDateOfBirth());
				statement.setString(3, playerBean.getPassword());
				statement.setString(4, playerBean.getEmail());
				statement.setString(5, playerBean.getPhone());
				statement.setString(6, playerBean.getAddress());
				statement.setString(7, playerBean.getCity());
				statement.setString(8, playerBean.getState());
				statement.setString(9, playerBean.getCountry());
				statement.setString(10, "ACTIVE");
				statement.setTimestamp(11, new java.sql.Timestamp(new Date().getTime()));
				statement.executeUpdate();
				con.commit();
				return bean.getReturnType();
				}
				else
				{
				return bean.getReturnType()+" During Deposit the money But Player is Registered Successfully... ";
				}
			}
			else
			{
				return responseData;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "true";
		
	}
	
	public Map<String, String> verifyOrgName(String userName) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			//here call the API for check User Name Availability
			  InputStream iStream = OLAUtility.parseCheckUserNameAvailabilityApi(userName);
			  BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
			  StringBuilder sb =new StringBuilder();
			  String line =null;
			while ((line = reader.readLine()) != null) {
			      sb.append(line);
			    }
			
			String msg = sb.toString();
			System.out.println("Verification Message"+msg);
			String success_flag = msg.split(",")[0].split(":")[1];
			String success_msg = msg.split(",")[1].split(":")[1].split("}")[0];
			if(success_flag.equalsIgnoreCase("false")&& success_msg.equalsIgnoreCase("true")){
				System.out.println("User Name Already Exists !!");
				errorMap.put("userError", "User Name Already exists !!");
				
			}
			else if(success_flag.equalsIgnoreCase("true")&& success_msg.equalsIgnoreCase("false")){
				System.out.println("User Name Availiable !!");
					errorMap.put("userError","Avail");
				}
			else {
				System.out.println("User Name Invalid!!");
				errorMap.put("userError", "User Name Invalid");
				
			}
			return errorMap;
		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}
	
	public 	FlexiCardPurchaseBean  registerPlayer(int walletId,String walletName,UserInfoBean userBean,String depositAnyWhere,OlaPlayerDetailsBean playerBean,double depositAmount,String rootPath,int  validMonths,String desKey,String propKey) throws LMSException{
		FlexiCardPurchaseBean flexiCardPurchaseBean = new 	FlexiCardPurchaseBean();
		boolean isPendingData = false;
		//if deposit amount >0 then  proceed 
		if(depositAmount<=0){
			flexiCardPurchaseBean.setReturnType("Deposit Amount Should Be Greater Than Zero");
			flexiCardPurchaseBean.setSuccess(false);
			return flexiCardPurchaseBean;
		}	
		Connection con = DBConnect.getConnection();
		try{
			con.setAutoCommit(false);
			flexiCardPurchaseBean.setPlayerName(playerBean.getUsername());
			//Make  deposit in LMS and generate pin  
			OLARummyHelper olaRummy = new OLARummyHelper();
			Calendar cal = Calendar.getInstance();
			java.sql.Date purchaseDate = new java.sql.Date(cal.getTime().getTime());
			cal.add(Calendar.MONTH,validMonths);//  Expiry date 
			java.sql.Date expiryDate = new java.sql.Date(cal.getTime().getTime());
			flexiCardPurchaseBean.setPurchaseDate(purchaseDate.toString());
			flexiCardPurchaseBean.setAmount(depositAmount);
			flexiCardPurchaseBean.setDenomiationType("FLEXI");
			flexiCardPurchaseBean  = olaRummy.rummyDeposit(con,depositAmount,userBean, walletId,depositAnyWhere,flexiCardPurchaseBean,expiryDate,
															playerBean.getPhone(),desKey,propKey);
							
			if(flexiCardPurchaseBean.getReturnType().equalsIgnoreCase("true"))
			{
				playerBean = OLAUtility.newRummyPlayerRegistration(playerBean);
				
					
				//Save the Player in the database
				if(playerBean.isSuccess())
				{		
					flexiCardPurchaseBean.setPartyId(playerBean.getAccountId());
					String insQry = "insert into st_ola_player_master(username,wallet_id,account_id,fname,lname,gender,date_of_birth, password, email, phone, address, city, state, country, status, registration_date) values (?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
					PreparedStatement statement = con.prepareStatement(insQry);
					statement.setString(1, playerBean.getUsername());
					statement.setInt(2, playerBean.getWalletId());
					statement.setString(3, playerBean.getAccountId());
					statement.setString(4, playerBean.getFirstName());
					statement.setString(5, playerBean.getLastName());
					statement.setString(6, playerBean.getGender());
					statement.setString(7, playerBean.getDateOfBirth());
					CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
					String   password= helper.encryptPin(playerBean.getPassword(),desKey,propKey);// encrypt password
					statement.setString(8,password);
					statement.setString(9, playerBean.getEmail());
					statement.setString(10, playerBean.getPhone());
					statement.setString(11, playerBean.getAddress());
					statement.setString(12, playerBean.getCity());
					statement.setString(13, playerBean.getState());
					statement.setString(14, playerBean.getCountry());
					statement.setString(15, "ACTIVE");
					statement.setTimestamp(16, new java.sql.Timestamp(new Date().getTime()));
					int isUpdate =statement.executeUpdate();
					if(isUpdate!=1){
						flexiCardPurchaseBean.setReturnType("Error In Saving Player Data");
						flexiCardPurchaseBean.setSuccess(false);
						return flexiCardPurchaseBean;
					}
					// Bind Player 
				//	CommonFunctionsHelper.bindPlrNAffiliate(con, playerBean
					//		.getUsername(), userBean.getUserName(), walletId);
						
					con.commit();
					//send Deposit Info to Rummy 
					OLARummyHelper infoHelper = new OLARummyHelper();
					String depositInfoStatus= infoHelper.sendDepositInfoToRummy(flexiCardPurchaseBean.getPlayerName(),flexiCardPurchaseBean.getSerialNumber(),flexiCardPurchaseBean.getPinNbr(),depositAmount,playerBean.getPhone());
					if(!depositInfoStatus.equalsIgnoreCase("success")){
						flexiCardPurchaseBean.setSuccess(false);
						OlaRummyRefundPinHelper refundHelper = new OlaRummyRefundPinHelper();
						String cancelReason="CANCEL_SERVER";
						String returnType =refundHelper.refundPin(walletId,flexiCardPurchaseBean.getPinNbr(),flexiCardPurchaseBean.getSerialNumber(),flexiCardPurchaseBean.getPlayerName(),depositAmount,desKey,propKey,con,cancelReason);
						if(returnType.equalsIgnoreCase("success")){
							flexiCardPurchaseBean.setReturnType(" Player Registered Successfully,Error In Deposit at KhelPlay Rummy : Amount Refunded");
							return flexiCardPurchaseBean;
						}
						else{
							flexiCardPurchaseBean.setReturnType(" Player Registered Successfully, Error In Deposit at KhelPlay Rummy :"+returnType);
							return flexiCardPurchaseBean;
						}
					}else{
						
						flexiCardPurchaseBean.setSuccess(true);
						
						//Send Msg
						StringBuilder sb = new StringBuilder(flexiCardPurchaseBean.getSerialNumber().toString());
						String srNbr =sb.substring(0,4)+" "+sb.substring(4,8)+" "+sb.substring(8,12);
						//		sb = new StringBuilder(flexiCardPurchaseBean.getPinNbr()+"");
						//String pinNbr =flexiCardPurchaseBean.getPinNbr()+"";		
						String msg ="Welcome to Khelplay Rummy :Your account has been created successfully, and Your Deposit Request of Amt:"+depositAmount+" has been initiated with PlrName:"+flexiCardPurchaseBean.getPlayerName()+" and RefCode:"+srNbr+",please visit the cashier page at khelplayrummy.com to confirm deposit";
						SendSMS smsSend = new SendSMS(msg,playerBean.getPhone());
						smsSend.setDaemon(true);
						smsSend.start();
						System.out.println(" Sending Message..... ");
						//send Mail
						String emailMsgTxt = "<html><table><tr><td>Dear  "
								+ flexiCardPurchaseBean.getPlayerName()+" </td></tr><tr><td> Welcome to Khelplay Rummy !!"+
								 "</td></tr><tr><td>Your account has been created successfully, and your  details are</td></tr>" +
								 "<tr><td>User Name :  "+flexiCardPurchaseBean.getPlayerName()+"</td></tr><tr><td>Password :  "+playerBean.getPassword()+"</td></tr><tr><td>Deposit Amount :  "+depositAmount+"</td></tr><tr><td>Deposit RefCode:"+srNbr+"</td></tr></tr><tr><td> please visit the cashier page at khelplayrummy.com to confirm deposit </td></tr></table></html>";
						MailSend mailSend = new MailSend(playerBean.getEmail(),
								emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						System.out.println(" Sending Mail..... ");
						return flexiCardPurchaseBean;
					}
					
					
				}
				else
				{	
					flexiCardPurchaseBean.setReturnType(playerBean.getMsg());
					flexiCardPurchaseBean.setSuccess(false);
					return flexiCardPurchaseBean;
				}
				
			}
			
			else
			{
				flexiCardPurchaseBean.setSuccess(false);// Error In Rummy  Deposit 
				System.out.println(flexiCardPurchaseBean.getReturnType());
				return flexiCardPurchaseBean;
			}
			
			
		} catch (SQLException e) {
			flexiCardPurchaseBean.setSuccess(false);
			flexiCardPurchaseBean.setReturnType("ERROR IN RMS");
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		
		
		return flexiCardPurchaseBean;
		//return "true";
		
	}	

	 public Map<String, String> verifyEmail(String email) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			//here call the API for check User Name Availability
			  InputStream iStream = OLAUtility.parseCheckEmailAvailabilityApi(email);
			  BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
			  StringBuilder sb =new StringBuilder();
			  String line =null;
			while ((line = reader.readLine()) != null) {
			      sb.append(line);
			    }
			
			String msg = sb.toString();
			System.out.println(" Email Verification Message"+msg);
			String success_flag = msg.split(",")[0].split(":")[1];
			String success_msg = msg.split(",")[1].split(":")[1].split("}")[0];
			if(success_flag.equalsIgnoreCase("false")&& success_msg.equalsIgnoreCase("true")){
				System.out.println("Email Already exits !!");
				errorMap.put("EmailError", "Email Already exits !!");
				
			}
			else if(success_flag.equalsIgnoreCase("true")&& success_msg.equalsIgnoreCase("false")){
				System.out.println("Email Availiable !!");
					errorMap.put("EmailError","Avail");
				}
			else {
					errorMap.put("EmailError","Error");
				}
			return errorMap;
		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}
	public 	FlexiCardPurchaseBean  registerPlayerForPMS(int walletId,String walletName,UserInfoBean userBean,String depositAnyWhere,OlaPlayerDetailsBean playerBean,double depositAmount,String rootPath) throws LMSException{
		FlexiCardPurchaseBean flexiCardPurchaseBean = new 	FlexiCardPurchaseBean();
		boolean isPendingData = false;
		//if deposit amount >0 then  proceed 
		if(depositAmount<=0){
			flexiCardPurchaseBean.setReturnType("Deposit Amount Should Be Greater Than Zero");
			flexiCardPurchaseBean.setSuccess(false);
			return flexiCardPurchaseBean;
		}	
		flexiCardPurchaseBean.setAmount(depositAmount);
		Connection con = DBConnect.getConnection();
		try{
			con.setAutoCommit(false);
			// call PMS API 
			playerBean= OLAUtility.newPMSPlayerRegistration(playerBean);
			if(playerBean.isSuccess()){
				// save Player Details
				flexiCardPurchaseBean.setPlayerName(playerBean.getUsername());
				String insQry = "insert into st_ola_player_master(username,wallet_id,account_id,fname,lname,gender,date_of_birth,password,email,phone, address, city, state, country, status, registration_date) values (?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
				PreparedStatement statement = con.prepareStatement(insQry);
				statement.setString(1, playerBean.getUsername());
				statement.setInt(2, playerBean.getWalletId());
				statement.setString(3, playerBean.getAccountId());
				statement.setString(4, playerBean.getFirstName());
				statement.setString(5, playerBean.getLastName());
				statement.setString(6, playerBean.getGender());
				statement.setString(7, playerBean.getDateOfBirth());
				statement.setString(8,playerBean.getPassword());
				statement.setString(9, playerBean.getEmail());
				statement.setString(10, playerBean.getPhone());
				statement.setString(11, playerBean.getAddress());
				statement.setString(12, playerBean.getCity());
				statement.setString(13, playerBean.getState());
				statement.setString(14, playerBean.getCountry());
				statement.setString(15, "ACTIVE");
				statement.setTimestamp(16, new java.sql.Timestamp(new Date().getTime()));
				int isUpdate =statement.executeUpdate();
				if(isUpdate!=1){
					flexiCardPurchaseBean.setReturnType("Error In Saving Player Data");
					flexiCardPurchaseBean.setSuccess(false);
					return flexiCardPurchaseBean;
				}
				con.commit();
				OLAPlrLotteryHelper plrLottery = new OLAPlrLotteryHelper();
				String  returnType =plrLottery.plrLotteryDeposit(depositAnyWhere,playerBean.getUsername(),depositAmount,
												userBean, walletId,playerBean.getPhone());
				//String returnType="true";
				if(returnType.equalsIgnoreCase("true")){
					flexiCardPurchaseBean.setSuccess(true);
					
					return flexiCardPurchaseBean ;
					
				}else{
					flexiCardPurchaseBean.setSuccess(false);
					flexiCardPurchaseBean.setReturnType("Player Registered Successfully. </br>Deposit Error :"+returnType);
					return flexiCardPurchaseBean ;
				}
			
			
			
			}else{
				flexiCardPurchaseBean.setSuccess(false);
				flexiCardPurchaseBean.setReturnType(playerBean.getMsg());
				return flexiCardPurchaseBean ;
			}
			
			
			
		} catch (Exception e) {
			flexiCardPurchaseBean.setSuccess(false);
			flexiCardPurchaseBean.setReturnType("ERROR IN RMS");
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		
		
		return flexiCardPurchaseBean;
		//return "true";
		
	}
	public Map<String, String> verifyPlrName(String userName) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
		
			// Call Player Mgmt Api  
			String method = "playerVerificationAction";
			JSONObject params = new JSONObject();
	        params.put("userName",userName);
	        JSONObject responseObj =Utility.sendCallApi(method, params, "6");
			if(responseObj==null){
				errorMap.put("inValid","Error In Connection With Player Lottery");
			}
			else{
				boolean isSuccess = responseObj.getBoolean("isSuccess");
				if(isSuccess){
					//Player Not Exist at Player Lottery
					errorMap.put("valid","User Name is Valid !! ");
				}else{
					//Player Exist at Player Lottery
					errorMap.put("inValid","User Name is Invalid !!");
					
				}
			}
			 
			return errorMap;
		} catch (Exception se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}

	@Deprecated
	public FlexiCardPurchaseBean registerPlayerForKpRummy(int walletId,String walletName, UserInfoBean userBean, String depositAnyWhere,
												OlaPlayerDetailsBean playerBean, double depositAmount) {
		FlexiCardPurchaseBean flexiCardPurchaseBean = new 	FlexiCardPurchaseBean();
		
		//if deposit amount >0 then  proceed 
		if(depositAmount<=0){
			flexiCardPurchaseBean.setReturnType("Deposit Amount Should Be Greater Than Zero");
			flexiCardPurchaseBean.setSuccess(false);
			return flexiCardPurchaseBean;
		}	
		Connection con =null;
		PreparedStatement pstmt=null;
		try{
			
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			flexiCardPurchaseBean.setPlayerName(playerBean.getUsername());
			//Register Player 
			
		//	playerBean = OLAUtility.newKpRummyPlayerRegistration(playerBean,walletId);
			//Save the Player in the database
		if(playerBean.isSuccess())
				{		
					flexiCardPurchaseBean.setPartyId(playerBean.getAccountId());
					String insQry = "insert into st_ola_player_master(username,wallet_id,account_id,fname,lname,gender,date_of_birth, password, email, phone, address, city, state, country, status, registration_date) values (?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
					pstmt = con.prepareStatement(insQry);
					pstmt.setString(1, playerBean.getUsername());
					pstmt.setInt(2, playerBean.getWalletId());
					pstmt.setString(3, playerBean.getAccountId());
					pstmt.setString(4, playerBean.getFirstName());
					pstmt.setString(5, playerBean.getLastName());
					pstmt.setString(6, playerBean.getGender());
					pstmt.setString(7, playerBean.getDateOfBirth());
					pstmt.setString(8,playerBean.getPassword());
					pstmt.setString(9, playerBean.getEmail());
					pstmt.setString(10, playerBean.getPhone());
					pstmt.setString(11, playerBean.getAddress());
					pstmt.setString(12, playerBean.getCity());
					pstmt.setString(13, playerBean.getState());
					pstmt.setString(14, playerBean.getCountry());
					pstmt.setString(15, "ACTIVE");
					pstmt.setTimestamp(16, new java.sql.Timestamp(new Date().getTime()));
					int isUpdate =pstmt.executeUpdate();
					if(isUpdate!=1){
						flexiCardPurchaseBean.setReturnType("Error In Saving Player Data");
						flexiCardPurchaseBean.setSuccess(false);
						return flexiCardPurchaseBean;
					}
					
					con.commit();
					//send Deposit Info to KP Rummy 
					String depositResp =OlaHelper.depositMoneyForKpRummy(depositAnyWhere, playerBean.getUsername(), depositAmount,
							userBean, walletName, walletId, playerBean.getPhone());


					if(depositResp.equalsIgnoreCase("true")){
						flexiCardPurchaseBean.setAmount(depositAmount);
						flexiCardPurchaseBean.setSuccess(true);
						flexiCardPurchaseBean.setSerialNumber(0);
						
						//Send Msg
							
						String msg ="Welcome to Khelplay Rummy :Your account has been created successfully, and Your Deposit Request of Amt:"+depositAmount+" has been initiated with PlrName:"+flexiCardPurchaseBean.getPlayerName()+" ,please visit the cashier page at khelplayrummy.com ";
						SendSMS smsSend = new SendSMS(msg,playerBean.getPhone());
						smsSend.setDaemon(true);
						smsSend.start();
						logger.info(" Sending Message..... ");
						//send Mail
						String emailMsgTxt = "<html><table><tr><td>Dear  "
								+ flexiCardPurchaseBean.getPlayerName()+" </td></tr><tr><td> Welcome to Khelplay Rummy !!"+
								 "</td></tr><tr><td>Your account has been created successfully, and your  details are</td></tr>" +
								 "<tr><td>User Name :  "+flexiCardPurchaseBean.getPlayerName()+"</td></tr><tr><td>Deposit Amount :  "+depositAmount+"</td></tr></tr><tr><td> please visit the cashier page at khelplayrummy.com  </td></tr></table></html>";
						MailSend mailSend = new MailSend(playerBean.getEmail(),
								emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						logger.info(" Sending Mail..... ");
						return flexiCardPurchaseBean;
						
					
					}else{
				
						flexiCardPurchaseBean.setReturnType(" Player Registered Successfully,</br>Deposit Error :"+depositResp);
						return flexiCardPurchaseBean;
					}
				}else{
						flexiCardPurchaseBean.setSuccess(false);
						logger.info("ERROR IN Player Registration At OLA");
						flexiCardPurchaseBean.setReturnType("ERROR IN Player Registration: "+playerBean.getMsg());
						return flexiCardPurchaseBean;
				}
	
		} catch (Exception e) {
			flexiCardPurchaseBean.setSuccess(false);
			logger.info("ERROR IN Player Registration At OLA");
			flexiCardPurchaseBean.setReturnType("ERROR IN Player Registration");
			e.printStackTrace();
		}finally {
			try {
				if(con!=null){
					con.close();
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		
		
		return flexiCardPurchaseBean;
	}		
	 public Map<String, String> verifyEmailForKpRummy(String email,int walletId) throws LMSException {
			Map<String, String> errorMap = new TreeMap<String, String>();
			try {
				//here call the API for check User Name Availability
				Map<String,String> verifyEmailReqMap = new HashMap<String, String>();
				verifyEmailReqMap.put("requestType","EMAIL_AVAILABILITY");
				verifyEmailReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpIp());
				verifyEmailReqMap.put("availabilityValue",email);
				InputStream verifyEmailResponse =  OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(verifyEmailReqMap),walletId,OLAConstants.depReq);
				Map<String,String> verifyEmailRespMap = null;
				verifyEmailRespMap=OLAUtility.prepareDataFromXml(verifyEmailResponse);
				logger.info(verifyEmailRespMap);
				if(verifyEmailRespMap==null){
					errorMap.put("EmailError","Error");
					
					
				}else if(verifyEmailRespMap.get("errorCode")!=null || verifyEmailRespMap.get("errorMsg")!=null){
					
					errorMap.put("EmailError", "Email Already exits !!");
				
					
				}else if(verifyEmailRespMap.get("respMsg")!=null){
						errorMap.put("EmailError","Avail");
				
				}
				
				return errorMap;
			} catch (Exception se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}	
	 
	 public Map<String, String> verifyPhoneForKpRummy(String phone,int walletId) throws LMSException {
			Map<String, String> errorMap = new TreeMap<String, String>();
			try {
				//here call the API for check User Name Availability
				Map<String,String> verifyPhoneReqMap = new HashMap<String, String>();
				verifyPhoneReqMap.put("requestType","MOBILE_AVAILABILITY");
				verifyPhoneReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpIp());
				verifyPhoneReqMap.put("availabilityValue",phone);
				InputStream verifyPhoneResponse =  OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(verifyPhoneReqMap),walletId,OLAConstants.depReq);
				Map<String,String> verifyPhoneRespMap = null;
				verifyPhoneRespMap=OLAUtility.prepareDataFromXml(verifyPhoneResponse);
				logger.info(verifyPhoneRespMap);
				if(verifyPhoneRespMap==null){
					errorMap.put("PhoneError","Error");
					
					
				}else if(verifyPhoneRespMap.get("errorCode")!=null||verifyPhoneRespMap.get("errorMsg")!=null){
					
					errorMap.put("PhoneError", "Phone Number Already exits !!");
				
					
				}else if(verifyPhoneRespMap.get("respMsg")!=null){
						errorMap.put("PhoneError","Avail");
				
				}
				
				return errorMap;
			} catch (Exception se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}	 
	
}