package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
/**
 * Check withdrawal request and withdrawal money From LMS For Rummy 
 * @author Neeraj Jain
 *
 */
public class OlaRummyWithdrawalHelper {
	private static Log logger = LogFactory.getLog(OlaRummyWithdrawalHelper.class);
/**
 * This method withdrawal money from LMS for Rummy
 * @param userName
 * @param WithdrawlAmt
 * @param devWalletName
 * @param userBean
 * @param walletId
 * @param withdrawlAnyWhere
 * @param authenticationCode  verify withdrawal request
 * @return true/ERROR Message
 * @throws LMSException
 */
	public String olaWithdrawalMoneyFromLMSForRummy(String userName,double WithdrawlAmt,
			String devWalletName,UserInfoBean userBean,int walletId,String withdrawlAnyWhere,
			String authenticationCode)	throws LMSException{
		Connection con = DBConnect.getConnection();

		double retailerComm = 0;
		double agentComm = 0;
		double retNetAmt = 0;
		double agentNetAmt = 0;
		long imsTransactionId = 0;
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
			logger.info("olaWithdrawlLimit" + olaWithdrawlLimit);
			if (WithdrawlAmt > olaWithdrawlLimit) {
				System.out
						.println("withdrawl amount is greater then withdrawl limit");
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
				boolean isIMSSuccess = false;
				OlaPTResponseBean respBean = new OlaPTResponseBean();
				respBean = checkWithdrawalRequest(con,respBean,authenticationCode,userName,WithdrawlAmt);// validate withdrawal request
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
						pstmtUpdate.setLong(4, imsTransactionId);
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
				/*		CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
								userBean.getUserOrgId(), "DEBIT", con);

						// update st_lms_organization_master for claimable
						// balance
						// for
						// agent
						commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
								userBean.getParentOrgId(), "DEBIT", con);
*/
						
						boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNetAmt, "CLAIM_BAL", "DEBIT", userBean.getUserOrgId(),0, userBean.getUserType(), 0, con);
						if (!isValid)
							throw new LMSException();
						isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "DEBIT",userBean.getParentOrgId(),0, userBean.getUserType(), 0, con);
						if (!isValid)
							throw new LMSException();
						logger.info("withdrawl amount successfully");
						PreparedStatement updateImsTransId = con
								.prepareStatement("update st_ola_ret_withdrawl set ims_ref_transaction_id=? where transaction_id=?");
						updateImsTransId.setLong(1, respBean
								.getImsWithdrawalTransactionId());
						updateImsTransId.setLong(2, transactionId);
						updateImsTransId.executeUpdate();

						PreparedStatement updateTemp = con
								.prepareStatement("update st_ola_withdrawal_request set status=?,ref_lms_transaction_id=? where task_id=?");
						updateTemp.setString(1, "DONE");
						updateTemp.setLong(2,transactionId);
						updateTemp.setLong(3, respBean
								.getImsWithdrawalTransactionId());
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
		 
			// con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during withdrawl");

		} finally {
			
				DBConnect.closeCon(con);
		
		}
		
		
	}
	/**
	 * 
	 * @param con
	 * @param respBean
	 * @param authenticationCode
	 * @param userName
	 * @param WithdrawlAmt
	 * @return
	 */
public OlaPTResponseBean checkWithdrawalRequest(Connection con,OlaPTResponseBean respBean,String authenticationCode,String userName,double WithdrawlAmt){
	try {
		PreparedStatement ps = con.prepareStatement("select task_id from st_ola_withdrawal_request where plr_id=? and amount=? and ref_code =? and status='PENDING'");
		ps.setString(1,userName);
		ps.setDouble(2,WithdrawlAmt);
		ps.setString(3,authenticationCode);
		ResultSet rs = ps.executeQuery();
		int recordCount = 0;
		respBean.setWithdrawalStatus("Withdrawal Request Declined");
		while(rs.next()){
			if(recordCount>0){
				respBean.setWithdrawalStatus("Withdrawal Request Not Approved Contact To Back Office");
				
			}
			else {
				respBean.setImsWithdrawalTransactionId(rs.getInt("task_id"));
				respBean.setWithdrawalStatus("APPROVED");
				
				}
			recordCount++;
		}
	}catch(Exception e){
		e.printStackTrace();
		respBean.setWithdrawalStatus("Some Error In Withdrawal Request Approval");
	}
	return respBean;
}
}
