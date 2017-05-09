package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.web.drawGames.common.Util;

public class OLAPlrLotteryHelper {
static Log logger = LogFactory.getLog(OLAPlrLotteryHelper.class);
//Added By Neeraj For Player Mgmt
public 	String  plrLotteryDeposit(String depositAnyWhere,
		String plrName, double depositAmt,
		UserInfoBean userBean, int walletId,String userPhone) throws LMSException {
	
	
	Connection con = DBConnect.getConnection();

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
			logger.info("OLA Limits Are Not defined Properly!!");
			throw new LMSException("OLA Limits Are Not defined Properly!!");
		}
		double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
		logger.info("olaDepositLimit"+olaDepositLimit+"ola deposite money"+depositAmt);
		

		if (depositAmt > olaDepositLimit) {
			logger.info("Deposit amount is greater then deposit limit");
					
			return 	"Deposit amount is greater then deposit limit" ;
			// return "Deposit amount is greater then deposit limit";
		}
		// check with retailer and agent balance to deposit
		OlaHelper olahelper = new OlaHelper();
		int isCheck = olahelper.checkOrgBalance(depositAmt, retOrgId, agentOrgId,
				con, retailerComm, agentComm);
		logger.info("ischeck" + isCheck);
		

		if (isCheck == -1) {
			// Agent has insufficient
			logger.info("Agent has insufficient");
			
			return 	"Agent has insufficient" ;
			// return "Agent has insufficient";

		} else if (isCheck == -2) {
			// Error LMS
			logger.info("Error LMS");
			return 		"Error LMS" ;
			// return "Error LMS";
		} else if (isCheck == 0) {
			// Retailer has insufficient
			logger.info("Retailer has insufficient Balance");
			return 		"Retailer has insufficient Balance ";
			// return "Retailer has insufficient";
		}
		// insert in LMS transaction master
		if (isCheck == 2) {
								con.setAutoCommit(false);
								String isBinding = OLAUtility.affiliatePlrBinding(depositAnyWhere,
																plrName, userBean,walletId,con); 
								logger.info("isBinding :"+isBinding);
						
						if (isBinding.equalsIgnoreCase("OK")) {
													//String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
													String insertInLMS = QueryManager.insertInLMSTransactionMaster();
													PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
													pstmt1.setString(1, "RETAILER");
													long transactionId = 0;
													pstmt1.executeUpdate();
													ResultSet rs1 = pstmt1.getGeneratedKeys();
														if (rs1.next()) {
																transactionId = rs1.getLong(1);					
																// insert into retailer transaction master
																pstmt1 = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
																pstmt1.setLong(1, transactionId);
																pstmt1.setInt(2, userBean.getUserId());
																pstmt1.setInt(3, userBean.getUserOrgId());
																pstmt1.setInt(4, walletId);
																java.util.Date date = new java.util.Date();
																pstmt1.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
																pstmt1.setString(6, "OLA_DEPOSIT");
																isUpdate = pstmt1.executeUpdate();
																System.out
																		.println("insert into retailer transaction master"+isUpdate);	
																// insert in deposit master
																retNetAmt = (depositAmt - ((depositAmt * retailerComm) / 100));
																agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));					
																String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
																PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
																pstmtUpdate.setLong(1, transactionId);
																pstmtUpdate.setInt(2, walletId);
																pstmtUpdate.setString(3, plrName);
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
																	
																// update st_lms_organization_master for claimable balance for retailer
																CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
																commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
																								userBean.getUserOrgId(), "CREDIT", con);

																// update st_lms_organization_master for claimable balance  for agent 
																commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
																		userBean.getParentOrgId(), "CREDIT", con);					
																con.commit();
																 System.out.println("in ola helper amount is deposit Successfully");
														// Call Player Mgmt Api 
																String method = "playerDepositAction";
																JSONObject params = new JSONObject();
														        params.put("refTransactionId",transactionId);
														        params.put("depositMode", "OLA");
														        params.put("playerName", plrName);
														        params.put("depositAmount",depositAmt);
														        JSONObject responseObj =Utility.sendCallApi(method, params, "3");
														        if(responseObj==null){
														        														        	
														        	String refund = doRefund(depositAmt, retNetAmt, agentNetAmt,
														       			 retailerComm, agentComm, plrName, con, walletId, userBean,
														    			 transactionId, imsTransactionId);
														        	 return refund;
														        }
														        boolean isSuccess=false;
														        try{
														        	isSuccess= responseObj.getBoolean("isSuccess");
																   }catch(Exception e){
														        	e.printStackTrace();
														        	String refund = doRefund(depositAmt, retNetAmt, agentNetAmt,
															       			 retailerComm, agentComm, plrName, con, walletId, userBean,
															    			 transactionId, imsTransactionId);
															        	 return refund;
														        }
																   logger.info("call API Deposit Done");
														  
														       if(isSuccess){
														    	   // save refTransId 
														    	   imsTransactionId= responseObj.getLong("transactioId");
														    	   System.out.println("Deposit Successful in Player Mmgt");
														    	   pstmt1 = con.prepareStatement("update st_ola_ret_deposit set ims_ref_transaction_id=? where transaction_id=?");
														    	   pstmt1.setLong(1,imsTransactionId);
														    	   pstmt1.setLong(2, transactionId);
														    	   pstmt1.executeUpdate();
														    	   
														    	   // Updating st_lms_ret_offline_master After Getting Success...
														    	   Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(),"OLA_DEP",con);
														    	   con.commit();
														    	   return "true";	
														    	 											    	
														    	   
														       }else{
														    	  	  //refund;
														    		String refund = doRefund(depositAmt, retNetAmt, agentNetAmt,
															       			 retailerComm, agentComm, plrName, con, walletId, userBean,
															    			 transactionId, imsTransactionId);
															        	 return refund;
														    	   
														       }
														// transactionId
														}else {
																	
															 logger.info("Trabsaction Id is not Generated in LMS transaction master");
															
																	
																	return 	"error in Deposit the money";
																	// return "error in Deposit the money";
																	}
												}else {	
															
													return isBinding ;
												}	
			
			
		} else {
			logger.info("Error During balance verification");
						
			return 		"Error During balance verification";
			// return "Error During balance verification";
		}

		// con.commit();
	}

	catch (Exception e) {
		e.printStackTrace();
		throw new LMSException("Error during deposit");
	}finally{
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	// return "true";
}

private static String doRefund(double depositAmt,double retNetAmt,double agentNetAmt,double retailerComm,double agentComm,
		String plrName,Connection con,int walletId,UserInfoBean userBean,long transactionId,long imsTransactionId){
	 OlaHelper helper = new OlaHelper();
	 boolean isRefund;
	try {
		isRefund = helper.depositeRefund(depositAmt, retNetAmt, agentNetAmt,
				 retailerComm, agentComm, plrName, con, walletId, userBean,
				 transactionId, imsTransactionId);
		 if(isRefund){
			 con.commit();
			 logger.info("Error In Player Mgmt  Deposit. Amount Refunded Successfully");
			  return "Error In Player Lottery Deposit. Amount Refunded Successfully";
		 }else{
			 logger.info("Error In LMS Deposit Refund");
			
			 return 	"Error In LMS Deposit Refund";	
		 }
	} catch (SQLException e) {
		
		e.printStackTrace();
		return 	"Error In LMS Deposit Refund";	
	}
	
}

public String plrLotteryWithdrawal(String userName,double WithdrawlAmt,
		String devWalletName,UserInfoBean userBean,int walletId,String withdrawlAnyWhere,
		String authenticationCode){
	Connection con = DBConnect.getConnection();

	double retailerComm = 0;
	double agentComm = 0;
	double retNetAmt = 0;
	double agentNetAmt = 0;
	long tempTransactionId = 0;
	long agentRefTransactionId = 0;
	try {
		con.setAutoCommit(false);
		int retOrgId = userBean.getUserOrgId();
		
		retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
				walletId, retOrgId, "WITHDRAWAL", "RETAILER", con);
		agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
				walletId, userBean.getParentOrgId(), "WITHDRAWAL", "AGENT", con);
		// check with organizarizations limit
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean orgPwtLimit = commonFunction
				.fetchPwtLimitsOfOrgnization(retOrgId, con);
		if (orgPwtLimit == null) { // send mail to backoffice
			throw new LMSException("PWT Limits Are Not defined Properly!!");
		}
		double olaWithdrawlLimit = orgPwtLimit.getOlaWithdrawlLimit();
		System.out.println("olaWithdrawlLimit" + olaWithdrawlLimit);
		if (WithdrawlAmt > olaWithdrawlLimit) {
			 logger.info("withdrawl amount is greater then withdrawl limit");
				
			return "WITHDRAWL_LIMIT";
		}

		String affiliateId = null;
		PreparedStatement affPstmt = con
				.prepareStatement("select ref_user_id from st_ola_org_affiliate_mapping where organization_id="
						+ userBean.getUserOrgId() + "");
		ResultSet resultSet = affPstmt.executeQuery();
		if (resultSet.next()) {
			affiliateId = resultSet.getString("ref_user_id");
		}
		boolean isMappingOk = OLAUtility.affiliatePlrBindingForWithdrawl(
				withdrawlAnyWhere, userName, affiliateId, con,walletId);
		// isMappingOk=true;
		if (!isMappingOk) {
			logger.info("Player is not Mapped");

			return "Player is not mapped";
		}
		//retNetAmt = (WithdrawlAmt - ((WithdrawlAmt * retailerComm) / 100));
		//agentNetAmt = (WithdrawlAmt - ((WithdrawlAmt * agentComm) / 100));
		retNetAmt = (WithdrawlAmt + ((WithdrawlAmt * retailerComm) / 100));
		agentNetAmt = (WithdrawlAmt + ((WithdrawlAmt * agentComm) / 100));
		// insert withdrawal details in st_ola_ret_withdrawal_temp
		PreparedStatement insertTemp = con
				.prepareStatement("insert into st_ola_ret_withdrawl_temp(wallet_id, retailer_org_id, ims_ref_transaction_id, withdrawl_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, deposit_channel, status, retailer_ref_transaction_id, party_id)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insertTemp.setInt(1, walletId);
		insertTemp.setInt(2, userBean.getUserOrgId());
		insertTemp.setInt(3, 0);
		insertTemp.setDouble(4, WithdrawlAmt);
		insertTemp.setDouble(5, retNetAmt);
		insertTemp.setDouble(6, agentNetAmt);
		insertTemp.setDouble(7, retailerComm);
		insertTemp.setDouble(8, agentComm);
		insertTemp.setString(9, "WEB");
		insertTemp.setString(10, "PENDING");
		insertTemp.setInt(11, 0);
		insertTemp.setString(12, userName);
		insertTemp.executeUpdate();
		ResultSet resultSet2 = insertTemp.getGeneratedKeys();
		if (resultSet2.next()) {
			tempTransactionId = resultSet2.getLong(1);
			con.commit(); // here commit the data before sending the request
		
		boolean isIMSSuccess = false;
		OlaPTResponseBean respBean = new OlaPTResponseBean();
		respBean = checkWithdrawalRequest(con,respBean,authenticationCode,userName,WithdrawlAmt,tempTransactionId);// validate withdrawal request
		if(respBean.getWithdrawalStatus().equalsIgnoreCase("APPROVED")){
			isIMSSuccess =true;
		}
		else{
			logger.info("Get Following error in checkWithdrawalRequest method :"+respBean.getWithdrawalStatus());
			return respBean.getWithdrawalStatus();
		}
		if (isIMSSuccess) {
			
			/*	String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
			PreparedStatement pstmt1 = con
					.prepareStatement(insertInLMS);*/
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
				pstmt1.setTimestamp(5, new java.sql.Timestamp(
						new Date().getTime()));
				pstmt1.setString(6, "OLA_WITHDRAWL");
				pstmt1.executeUpdate();
				// insert in withdrawl master

				String insertQry = "insert into st_ola_ret_withdrawl(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, withdrawl_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, deposit_channel, claim_status, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmtUpdate = con
						.prepareStatement(insertQry);
				pstmtUpdate.setLong(1, transactionId);
				pstmtUpdate.setInt(2, walletId);
				pstmtUpdate.setInt(3, userBean.getUserOrgId());
				pstmtUpdate.setLong(4, respBean
						.getImsWithdrawalTransactionId());
				pstmtUpdate.setDouble(5, WithdrawlAmt);
				pstmtUpdate.setDouble(6, retNetAmt);
				pstmtUpdate.setDouble(7, agentNetAmt);
				pstmtUpdate.setDouble(8, retailerComm);
				pstmtUpdate.setDouble(9, agentComm);
				pstmtUpdate.setString(10, "WEB");
				pstmtUpdate.setString(11, "CLAIM_BAL");
				pstmtUpdate.setLong(12, agentRefTransactionId);
				pstmtUpdate.setString(13, userName);
				pstmtUpdate.executeUpdate();
				// update st_lms_organization_master for claimable
				// balance
				// for
				// retailer
				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
						userBean.getUserOrgId(), "DEBIT", con);

				// update st_lms_organization_master for claimable
				// balance
				// for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
						userBean.getParentOrgId(), "DEBIT", con);
				logger.info("withdrawl amount successfully");
				
			// Update temp
				PreparedStatement updateTemp = con
						.prepareStatement("update st_ola_ret_withdrawl_temp set status=?,retailer_ref_transaction_id=? where transaction_id=?");
				updateTemp.setString(1, "DONE");
				updateTemp.setLong(2,transactionId); 
				updateTemp.setLong(3,tempTransactionId);
				
				// Updating st_lms_ret_offline_master After Getting Success...
				Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(),"OLA_WITH",con);
				updateTemp.executeUpdate();
				con.commit();
				return "true";
			} else {
				logger.info("Trabsaction Id is not Generated in RMS transaction master");
				
				return "error in withdrawl the money";
			}
		} else {
			logger.info("Some Error in RMS withdrawal");	
			return "Error in RMS";
			
		}
	}else {
		logger.info("Trabsaction Id is not Generated in st_ola_ret_withdrawal_temp");
			
			return "error in withdrawl the money";
	}

		
	}catch (Exception e) {
		e.printStackTrace();
		return "Error during withdrawal";

	} finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
public static OlaPTResponseBean checkWithdrawalRequest(Connection con,OlaPTResponseBean respBean,String authenticationCode,String userName,double WithdrawlAmt,long tempTransactionId){
	try {
		//call Plr Mgmt API set IMS Transaction id 
		String method ="PlayerWithdrawlVerification";
		JSONObject params = new JSONObject();
		params.put("verificationCode",authenticationCode);
		params.put("withdrawlAmount",WithdrawlAmt);
		params.put("transactionId",tempTransactionId);
		params.put("userName",userName);
		JSONObject responseObj =Utility.sendCallApi(method, params, "7");
		if(responseObj==null){
			respBean.setWithdrawalStatus("Error In Connection With Player Lottery");
			return respBean;
		}
		else{
			boolean isSuccess = responseObj.getBoolean("isSuccess");
			if(isSuccess){
				respBean.setImsWithdrawalTransactionId(Long.parseLong(responseObj.getString("refTransactionId")));
				respBean.setWithdrawalStatus("Approved");
				return respBean;
			}else{
				respBean.setWithdrawalStatus("Withdrawal Denied From Player Lottery");
				return respBean;
				
			}
		}
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
	return respBean;
}
public synchronized static 	String  plrLotteryAgtDeposit(String depositAnyWhere,
		String plrName, double depositAmt,
		UserInfoBean userBean, int walletId,String userPhone) throws LMSException {
	
	
	Connection con =null;

	double agentComm = 0.0;
	double agentNetAmt = 0.0;
	long imsTransactionId = 0;
	int isUpdate;
	
	try {
			con = DBConnect.getConnection();
		
			int agentOrgId =  userBean.getUserOrgId();
		// int retOrgId = userBean.getUserOrgId();

		// check with organizations limit
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean orgPwtLimit = commonFunction
				.fetchPwtLimitsOfOrgnization(agentOrgId, con);
		if (orgPwtLimit == null) { // send mail to back office
			logger.info("OLA Limits Are Not defined Properly!!");
			throw new LMSException("OLA Limits Are Not defined Properly!!");
		}
		double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
		logger.info("olaDepositLimit"+olaDepositLimit+"ola deposite money"+depositAmt);
		

		if (depositAmt > olaDepositLimit) {
			logger.info("Deposit amount is greater then deposit limit");
					
			return 	"Deposit amount is greater then deposit limit" ;
			// return "Deposit amount is greater then deposit limit";
		}
		// check with  agent credit limit  to deposit
		String erroMsg = CommonMethods.chkCreditLimitAgt(agentOrgId, depositAmt, con);
		logger.info(" erroMsg: " + erroMsg);
	 
		// insert in LMS transaction master
		if (erroMsg.equalsIgnoreCase("TRUE")) {
								con.setAutoCommit(false);
								String isBinding = OLAUtility.affiliatePlrBinding(depositAnyWhere,
																plrName, userBean,walletId,con); 
								logger.info("isBinding :"+isBinding);
								
				if (isBinding.equalsIgnoreCase("OK")) {
					
													agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
																			walletId, agentOrgId, "DEPOSIT", "AGENT", con);
													int plrId = OLAUtility.getPlrId(con, plrName);
													if(plrId==0){
														
														return 	"Error In Getting Player ID";
													}
													//String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
													String insertInLMS = QueryManager.insertInLMSTransactionMaster();
													PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
													pstmt1.setString(1, "AGENT");
													long transactionId = 0;
													pstmt1.executeUpdate();
													ResultSet rs1 = pstmt1.getGeneratedKeys();
														if (rs1.next()) {
																transactionId = rs1.getLong(1);					
																// insert into agent transaction master
																pstmt1 = con.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
																pstmt1.setLong(1, transactionId);
																pstmt1.setInt(2, userBean.getUserId());
																pstmt1.setInt(3, userBean.getUserOrgId());
																pstmt1.setString(4, "PLAYER");	
																pstmt1.setInt(5, plrId);
																pstmt1.setString(6, "OLA_DEPOSIT_PLR");
																java.util.Date date = new java.util.Date();
																pstmt1.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
																isUpdate = pstmt1.executeUpdate();
																logger.info("inserted into agent transaction master"+isUpdate);	
															
																agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));					
																String insertQry = "insert into st_ola_agt_direct_plr_deposit(agent_user_id,agent_org_id,transaction_id,wallet_id,plr_id,plr_alias, deposit_amt,  net_amt, deposit_claim_status,agt_claim_comm) values(?,?,?,?,?,?,?,?,?,?)";
																PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
																pstmtUpdate.setInt(1, userBean.getUserId());
																pstmtUpdate.setInt(2, userBean.getUserOrgId());
																pstmtUpdate.setLong(3, transactionId);
																pstmtUpdate.setInt(4, walletId);
																pstmtUpdate.setInt(5,plrId);
																pstmtUpdate.setString(6,plrName);
																pstmtUpdate.setDouble(7, depositAmt);
																pstmtUpdate.setDouble(8, agentNetAmt);
																pstmtUpdate.setString(9, "CLAIM_BAL");
																pstmtUpdate.setDouble(10, agentComm);
																pstmtUpdate.executeUpdate();
																	
																
																/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
																commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
																								userBean.getUserOrgId(), "CREDIT", con);*/
																
																// update st_lms_organization_master for claimable balance  for agent 
															boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "CREDIT", userBean.getUserOrgId(), userBean.getParentOrgId(), userBean.getUserType(), 0, con);
															if(!isValid){
																logger.info("Error In Organization Balance Updation");
																															
																return 	"Error in Deposit the money";
																
															}
															
																
																con.commit();
																logger.info("in ola helper amount is deposit Successfully");
																
																 // Call Player Mgmt Api 
																String method = "playerDepositAction";
																JSONObject params = new JSONObject();
														        params.put("refTransactionId",transactionId);
														        params.put("depositMode", "OLA");
														        params.put("playerName", plrName);
														        params.put("depositAmount",depositAmt);
														        JSONObject responseObj =Utility.sendCallApi(method, params, "4");
														  
														        boolean isSuccess=false;
														        try{
														        	isSuccess= responseObj.getBoolean("isSuccess");
																   }catch(Exception e){
														        	e.printStackTrace();
														        	String refund = doAgtRefund(depositAmt,agentNetAmt,
														        						agentComm,plrId, plrName, con, walletId, userBean,
															    			 transactionId);
															        	 return refund;
														        }
															   logger.info("call API Deposit Done");
														  
														       if(isSuccess){
														    	   // save refTransId 
														    	   imsTransactionId= responseObj.getLong("transactioId");
														    	   System.out.println("Deposit Successful in Player Mmgt");
														    	   pstmt1 = con.prepareStatement("update st_ola_agt_direct_plr_deposit set ims_ref_transaction_id=? where transaction_id=?");
														    	   pstmt1.setLong(1,imsTransactionId);
														    	   pstmt1.setLong(2, transactionId);
														    	   pstmt1.executeUpdate();
														    	   
														    	   // Updating st_lms_ret_offline_master After Getting Success...
														    	   Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(),"OLA_DEP",con);
														    	   con.commit();
														    	   return "true";	
														    	 											    	
														    	   
														       }else{
														    	  	  //refund;
														    		String refund = doAgtRefund(depositAmt,agentNetAmt,agentComm,plrId, plrName, con, walletId, 
														    						userBean,transactionId);								
														    			return refund;														    	   
														       }
														// transactionId
														}else {
																	
															 logger.info("Trabsaction Id is not Generated in LMS transaction master");
															
																	
																	return 	"error in Deposit the money";
																	// return "error in Deposit the money";
																	}
												}else {	
															
													return isBinding ;
												}	
			
			
		} else {
			
						
			return 		erroMsg;
			
		}

	
	}

	catch (Exception e) {
		e.printStackTrace();
		throw new LMSException("Error during deposit");
	}finally{
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	// return "true";
}
private static String doAgtRefund(double depositAmt,double agentNetAmt,double agentComm,int plrId,
		String plrName,Connection con,int walletId,UserInfoBean userBean,long reftransactionId){
	 OlaHelper helper = new OlaHelper();
	 boolean isRefund;
	try {
		isRefund = helper.agtDepositeRefund(depositAmt,agentNetAmt,agentComm,plrId,plrName, con, walletId, userBean,
				reftransactionId);
		 if(isRefund){
			 con.commit();
			 logger.info("Error In Player Mgmt  Deposit. Amount Refunded Successfully");
			  return "Error In Player Lottery Deposit. Amount Refunded Successfully";
		 }else{
			 logger.info("Error In LMS Deposit Refund");
			
			 return 	"Error In LMS Deposit Refund";	
		 }
	}catch (SQLException e) {
		
		e.printStackTrace();
		return 	"Error In LMS Deposit Refund";	
	}catch (Exception e) {
		
		e.printStackTrace();
		return 	"Error In LMS Deposit Refund";	
	}
	
	
}
public static synchronized String plrLotteryAgtWithdrawal(String plrName,double WithdrawlAmt,
		String devWalletName,UserInfoBean userBean,int walletId
		,String withdrawlAnyWhere,String authenticationCode) {
		Connection con = null;
		ResultSet resultSet2=null;
		PreparedStatement pstmt=null;
		double agentComm = 0;
		double agentNetAmt = 0;
		long tempTransactionId = 0;
		int  updated=0;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			int agtOrgId = userBean.getUserOrgId();
			int agtUserId = userBean.getUserOrgId();

			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId,agtOrgId, "WITHDRAWAL", "AGENT",
					con);
			// check with organizarizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(agtOrgId, con);
			if (orgPwtLimit == null) { // send mail to backoffice
				throw new LMSException("PWT Limits Are Not defined Properly!!");
			}
			double olaWithdrawlLimit = orgPwtLimit.getOlaWithdrawlLimit();
			logger.info("olaWithdrawlLimit" + olaWithdrawlLimit);
			if (WithdrawlAmt > olaWithdrawlLimit) {
				logger.info("withdrawl amount is greater then withdrawl limit");

				return "WITHDRAWL_LIMIT";
			}

			boolean isMappingOk = OLAUtility.affiliatePlrBindingForWithdrawl(
					withdrawlAnyWhere, plrName, userBean.getUserName(), con,
					walletId);
			// isMappingOk=true;
			if (!isMappingOk) {
				logger.info("Player is not Mapped");

				return "Player is not mapped";
			}
			// get Plr Id
			int plrId = OLAUtility.getPlrId(con, plrName);
			if (plrId == 0) {

				return "Error In Getting Player ID";
			}
			agentNetAmt = (WithdrawlAmt + ((WithdrawlAmt * agentComm) / 100));
			// insert withdrawal details in st_ola_agt_direct_plr_withdrawl_temp
			pstmt = con.prepareStatement("insert into st_ola_agt_direct_plr_withdrawl_temp(wallet_id, agt_org_id,agt_user_id,plr_alias, withdrawl_amt,agent_net_amt, agent_comm, channel, status)values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, walletId);
			pstmt.setInt(2,agtOrgId);
			pstmt.setInt(3, agtUserId);
			pstmt.setString(4, plrName);
			pstmt.setDouble(5, WithdrawlAmt);
			pstmt.setDouble(6, agentNetAmt);
			pstmt.setDouble(7, agentComm);
			pstmt.setString(8, "WEB");
			pstmt.setString(9, "PENDING");
			logger.info("Query "+pstmt);
			updated =pstmt.executeUpdate();
			logger.info("insertd in st_ola_agt_direct_plr_withdrawl_temp "+updated);
			resultSet2 = pstmt.getGeneratedKeys();

		

			if (resultSet2.next()) {
				tempTransactionId = resultSet2.getLong(1);
				con.commit(); // here commit the data before sending the request

				OlaPTResponseBean respBean = new OlaPTResponseBean();
				respBean = checkWithdrawalRequest(con, respBean,
						authenticationCode, plrName, WithdrawlAmt,
						tempTransactionId);// validate withdrawal request
				if (!respBean.getWithdrawalStatus()
						.equalsIgnoreCase("APPROVED")) {
					logger
							.info("Get Following error in checkWithdrawalRequest method :"
									+ respBean.getWithdrawalStatus());
					return respBean.getWithdrawalStatus();
				}

		
				pstmt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
				pstmt.setString(1, "AGENT");
				pstmt.executeUpdate();
				ResultSet rs1 = pstmt.getGeneratedKeys();
				long transactionId = 0;
				if (rs1.next()) {
					transactionId = rs1.getLong(1);
					// insert into retailer transaction master
					pstmt = con
							.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
					pstmt.setLong(1, transactionId);
					pstmt.setInt(2,agtUserId);
					pstmt.setInt(3, agtOrgId);
					pstmt.setString(4, "PLAYER");
					pstmt.setInt(5, plrId);
					pstmt.setString(6, "OLA_WITHDRAWL_PLR");
					pstmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("insertd in st_lms_agent_transaction_master "+updated);
					// insert in withdrawl master

					String insertQry = "insert into st_ola_agt_direct_plr_withdrawl(wallet_id,plr_id,plr_alias,agt_user_id,agt_org_id,transaction_id,ims_ref_transaction_id, withdrawl_amt, agent_net_amt, agent_comm,claim_status) values(?,?,?,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(insertQry);
					pstmt.setInt(1, walletId);
					pstmt.setInt(2, plrId);
					pstmt.setString(3, plrName);
					pstmt.setInt(4, agtUserId);
					pstmt.setInt(5,agtOrgId);
					pstmt.setLong(6,transactionId);
					pstmt.setLong(7, respBean.getImsWithdrawalTransactionId());
					pstmt.setDouble(8, WithdrawlAmt);
					pstmt.setDouble(9, agentNetAmt);
					pstmt.setDouble(10, agentComm);
					pstmt.setString(11, "CLAIM_BAL");
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("insertd in st_ola_agt_direct_plr_withdrawl "+updated);

					boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt,"CLAIM_BAL", "DEBIT", agtOrgId, userBean
											.getParentOrgId(), userBean.getUserType(), 0, con);
					logger.info("withdrawl amount successfully");
					if (!isValid) {
						logger.info("Error In Org Balance Updation");
						return "error in withdrawl the money";

					}
					// Update temp
					pstmt = con.prepareStatement("update st_ola_agt_direct_plr_withdrawl_temp set status=?,agt_ref_transaction_id=?,ims_ref_transaction_id=? where transaction_id=?");
					pstmt.setString(1, "DONE");
					pstmt.setLong(2, transactionId);
					pstmt.setLong(3, respBean.getImsWithdrawalTransactionId());
					pstmt.setLong(4, tempTransactionId);
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("update st_ola_agt_direct_plr_withdrawl_temp "+updated);
					con.commit();
					return "true";
				} else {
					logger
							.info("Trabsaction Id is not Generated in RMS transaction master");

					return "error in withdrawl the money";
				}

			} else {
				logger
						.info("Trabsaction Id is not Generated in st_ola_ret_withdrawal_temp");

				return "error in withdrawl the money";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "Error during withdrawal";

		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (resultSet2 != null) {
					resultSet2.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

public static synchronized	String  plrLotteryBoDeposit(String depositAnyWhere,String plrName, double depositAmt,
											UserInfoBean userBean, int walletId,String userPhone) throws LMSException {

		Connection con = null;

		long imsTransactionId = 0;
		int isUpdate;

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			String isBinding = OLAUtility.affiliatePlrBinding(depositAnyWhere,
					plrName, userBean, walletId, con);
			logger.info("isBinding :" + isBinding);

			if (isBinding.equalsIgnoreCase("OK")) {

				int plrId = OLAUtility.getPlrId(con, plrName);
				if (plrId == 0) {

					return "Error In Getting Player ID";
				}
				// String insertInLMS =
				// "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
				String insertInLMS = QueryManager
						.insertInLMSTransactionMaster();
				PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
				pstmt1.setString(1, "BO");
				long transactionId = 0;
				pstmt1.executeUpdate();
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					transactionId = rs1.getLong(1);
					// insert into agent transaction master
					pstmt1 = con
							.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
					pstmt1.setLong(1, transactionId);
					pstmt1.setInt(2, userBean.getUserId());
					pstmt1.setInt(3, userBean.getUserOrgId());
					pstmt1.setString(4, "PLAYER");
					pstmt1.setInt(5, plrId);
					pstmt1.setString(6, "OLA_DEPOSIT_PLR");
					java.util.Date date = new java.util.Date();
					pstmt1.setTimestamp(7, new java.sql.Timestamp(date
							.getTime()));
					isUpdate = pstmt1.executeUpdate();
					logger.info("inserted into bo transaction master"+ isUpdate);

					String insertQry = "insert into st_ola_bo_direct_plr_deposit(bo_user_id,bo_org_id,transaction_id,wallet_id,plr_id,plr_alias, deposit_amt) values(?,?,?,?,?,?,?)";
					PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
					pstmtUpdate.setInt(1, userBean.getUserId());
					pstmtUpdate.setInt(2, userBean.getUserOrgId());
					pstmtUpdate.setLong(3, transactionId);
					pstmtUpdate.setInt(4, walletId);
					pstmtUpdate.setInt(5, plrId);
					pstmtUpdate.setString(6, plrName);
					pstmtUpdate.setDouble(7, depositAmt);
					pstmtUpdate.executeUpdate();
					con.commit();
					logger.info("in ola helper amount is deposit Successfully");

					// Call Player Mgmt Api
					String method = "playerDepositAction";
					JSONObject params = new JSONObject();
					params.put("refTransactionId", transactionId);
					params.put("depositMode", "OLA");
					params.put("playerName", plrName);
					params.put("depositAmount", depositAmt);
					JSONObject responseObj =Utility.sendCallApi(method,params, "5");

					boolean isSuccess = false;
					try {
						isSuccess = responseObj.getBoolean("isSuccess");
					} catch (Exception e) {
						e.printStackTrace();
						String refund = doBoRefund(depositAmt, plrId, plrName, con, walletId,
								userBean, transactionId);
						return refund;
					}
					logger.info("call API Deposit Done");

					if (isSuccess) {
						// save refTransId
						imsTransactionId = responseObj.getLong("transactioId");
						System.out.println("Deposit Successful in Player Mmgt");
						pstmt1 = con
								.prepareStatement("update st_ola_bo_direct_plr_deposit set ims_ref_transaction_id=? where transaction_id=?");
						pstmt1.setLong(1, imsTransactionId);
						pstmt1.setLong(2, transactionId);
						pstmt1.executeUpdate();

						// Updating st_lms_ret_offline_master After Getting
						// Success...
						Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(),
								"OLA_DEP", con);
						con.commit();
						return "true";

					} else {
						// refund;
						String refund = doBoRefund(depositAmt, plrId, plrName, con, walletId,
											userBean, transactionId);
						return refund;
					}
					// transactionId
				} else {

					logger
							.info("Trabsaction Id is not Generated in LMS transaction master");

					return "error in Deposit the money";
					// return "error in Deposit the money";
				}
			} else {

				return isBinding;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// return "true";
}
private static String doBoRefund(double depositAmt,int plrId,
		String plrName,Connection con,int walletId,UserInfoBean userBean,long reftransactionId){
	 OlaHelper helper = new OlaHelper();
	 boolean isRefund;
	try {
		isRefund = helper.boDepositeRefund(depositAmt,plrId,plrName, con, walletId, userBean,
				reftransactionId);
		 if(isRefund){
			 con.commit();
			 logger.info("Error In Player Mgmt  Deposit. Amount Refunded Successfully");
			  return "Error In Player Lottery Deposit. Amount Refunded Successfully";
		 }else{
			 logger.info("Error In LMS Deposit Refund");
			
			 return 	"Error In LMS Deposit Refund";	
		 }
	} catch (SQLException e) {
		
		e.printStackTrace();
		return 	"Error In LMS Deposit Refund";	
	}
	
}
public String plrLotteryBoWithdrawal(String plrName,double WithdrawlAmt,
		String devWalletName,UserInfoBean userBean,int walletId
		,String withdrawlAnyWhere,String authenticationCode) {
		Connection con = null;
		ResultSet resultSet2=null;
		PreparedStatement pstmt=null;
		long tempTransactionId = 0;
		int  updated=0;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			int boOrgId = userBean.getUserOrgId();
			int boUserId = userBean.getUserId();


			boolean isMappingOk = OLAUtility.affiliatePlrBindingForWithdrawl(
					withdrawlAnyWhere, plrName, userBean.getUserName(), con,
					walletId);
			// isMappingOk=true;
			if (!isMappingOk) {
				logger.info("Player is not Mapped");

				return "Player is not mapped";
			}
			// get Plr Id
			int plrId = OLAUtility.getPlrId(con, plrName);
			if (plrId == 0) {

				return "Error In Getting Player ID";
			}
			
			// insert withdrawal details in st_ola_agt_direct_plr_withdrawl_temp
			pstmt = con.prepareStatement("insert into st_ola_bo_direct_plr_withdrawl_temp(wallet_id, user_id,plr_alias,withdrawl_amt, channel, status)values(?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, walletId);
			pstmt.setInt(2,boUserId);
			pstmt.setString(3, plrName);
			pstmt.setDouble(4, WithdrawlAmt);
			pstmt.setString(5, "WEB");
			pstmt.setString(6, "PENDING");
			logger.info("Query "+pstmt);
			updated =pstmt.executeUpdate();
			logger.info("insertd in st_ola_bo_direct_plr_withdrawl_temp "+updated);
			resultSet2 = pstmt.getGeneratedKeys();

		

			if (resultSet2.next()) {
				tempTransactionId = resultSet2.getLong(1);
				con.commit(); // here commit the data before sending the request

				OlaPTResponseBean respBean = new OlaPTResponseBean();
				respBean = checkWithdrawalRequest(con, respBean,
						authenticationCode, plrName, WithdrawlAmt,
						tempTransactionId);// validate withdrawal request
				if (!respBean.getWithdrawalStatus()
						.equalsIgnoreCase("APPROVED")) {
					logger
							.info("Get Following error in checkWithdrawalRequest method :"
									+ respBean.getWithdrawalStatus());
					return respBean.getWithdrawalStatus();
				}

		
				pstmt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
				pstmt.setString(1, "BO");
				pstmt.executeUpdate();
				ResultSet rs1 = pstmt.getGeneratedKeys();
				long transactionId = 0;
				if (rs1.next()) {
					transactionId = rs1.getLong(1);
					// insert into retailer transaction master
					pstmt = con
							.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
					pstmt.setLong(1, transactionId);
					pstmt.setInt(2,boUserId);
					pstmt.setInt(3,boOrgId);
					pstmt.setString(4, "PLAYER");
					pstmt.setInt(5, plrId);
					pstmt.setString(6, "OLA_WITHDRAWL_PLR");
					pstmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("insertd in st_lms_bo_transaction_master "+updated);
					
					// insert in withdrawl master

					String insertQry = "insert into st_ola_bo_direct_plr_withdrawl(wallet_id,plr_id,plr_alias,bo_user_id,bo_org_id,transaction_id,ims_ref_transaction_id, withdrawl_amt,channel,claim_status) values(?,?,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(insertQry);
					pstmt.setInt(1, walletId);
					pstmt.setInt(2, plrId);
					pstmt.setString(3, plrName);
					pstmt.setInt(4,boUserId);
					pstmt.setInt(5,boOrgId);
					pstmt.setLong(6,transactionId);
					pstmt.setLong(7, respBean.getImsWithdrawalTransactionId());
					pstmt.setDouble(8, WithdrawlAmt);
					pstmt.setString(9, "WEB");
					pstmt.setString(10, "CLAIM_BAL");
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("insertd in st_ola_agt_direct_plr_withdrawl "+updated);
			
					// Update temp
					pstmt = con.prepareStatement("update st_ola_bo_direct_plr_withdrawl_temp set status=?,bo_ref_transaction_id=?,ims_ref_transaction_id=? where transaction_id=?");
					pstmt.setString(1, "DONE");
					pstmt.setLong(2, transactionId);
					pstmt.setLong(3, respBean.getImsWithdrawalTransactionId());
					pstmt.setLong(4, tempTransactionId);
					logger.info("Query "+pstmt);
					updated =pstmt.executeUpdate();
					logger.info("update st_ola_bo_direct_plr_withdrawl_temp "+updated);
					con.commit();
					return "true";
				} else {
					logger
							.info("Trabsaction Id is not Generated in RMS transaction master");

					return "error in withdrawl the money";
				}

			} else {
				logger
						.info("Trabsaction Id is not Generated in st_ola_ret_withdrawal_temp");

				return "error in withdrawl the money";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "Error during withdrawal";

		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (resultSet2 != null) {
					resultSet2.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}



}
