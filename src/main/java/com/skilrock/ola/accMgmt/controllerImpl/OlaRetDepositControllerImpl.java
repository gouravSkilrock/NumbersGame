package com.skilrock.ola.accMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.accMgmt.daoImpl.OlaRetDepositDaoImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;

public class OlaRetDepositControllerImpl  {
	
	static Log logger = LogFactory.getLog(OlaRetDepositControllerImpl.class);
	
	public 	OLADepositResponseBean  olaRetPlrDeposit(OLADepositRequestBean reqBean, UserInfoBean userBean) throws LMSException, GenericException {		
	
		Connection con = DBConnect.getConnection();

		double retailerComm = 0.0;
		double agentComm = 0.0;
		double retNetAmt = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		int isUpdate;
		OLADepositResponseBean resBean = null;
		double olaDepositLimit = 0.0;
		boolean isValid=false;
		StringBuilder userName=new StringBuilder();
		try {				
			int playerId=OlaCommonMethodControllerImpl.fetchPlayerIdFromRefCode(reqBean.getRefCode(), reqBean.getWalletId(), con,userName);
			reqBean.setPlrId(playerId);
		
			int agentOrgId = userBean.getParentOrgId();
			int retOrgId = userBean.getUserOrgId();
			retailerComm = OlaCommonMethodControllerImpl.fetchOLACommOfOrganization(reqBean.getWalletId(), retOrgId, "DEPOSIT", "RETAILER", con);
			agentComm = OlaCommonMethodControllerImpl.fetchOLACommOfOrganization(reqBean.getWalletId(), agentOrgId, "DEPOSIT", "AGENT", con);
			retNetAmt = (reqBean.getDepositAmt() - ((reqBean.getDepositAmt() * retailerComm) / 100));
			agentNetAmt = (reqBean.getDepositAmt() - ((reqBean.getDepositAmt() * agentComm) / 100));
			
			OrgPwtLimitBean orgPwtLimit = OlaCommonMethodControllerImpl.fetchPwtLimitsOfOrgnization(retOrgId, con);
	
			olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
			logger.info("olaDepositLimit"+olaDepositLimit+"ola deposite money"+reqBean.getDepositAmt());		
			if (reqBean.getDepositAmt() > olaDepositLimit) {
				throw new LMSException(LMSErrors.EXCEED_DEPOSIT_AMOUNT_REQUEST_ERROR_CODE);
			}				
			// check with retailer and agent balance to deposit
			boolean isRetSalBalanceAval = OlaCommonMethodControllerImpl.checkOrgBalance(retOrgId, retNetAmt, "RETAILER", con);
			boolean isAgtSalBalanceAval = OlaCommonMethodControllerImpl.checkOrgBalance( agentOrgId, agentNetAmt, "AGENT", con);
			logger.info("RetailerSaleBalenceAvl : "+isRetSalBalanceAval+" and AgentSaleBalanceAvl : "+isAgtSalBalanceAval);
			// insert in LMS transaction master
			if (isRetSalBalanceAval && isAgtSalBalanceAval) {
					con.setAutoCommit(false);
					boolean isPlayerBind = OlaCommonMethodControllerImpl.affiliatePlrBinding(userName.toString(),"DEPOSIT",reqBean.getDepositAnyWhere(),reqBean.getPlrId(), userBean,reqBean.getWalletId(),con); 
					logger.info("isBinding :"+isPlayerBind);
						
					if (isPlayerBind) {
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
								pstmt1.setInt(4, reqBean.getWalletId());
								Timestamp txnTime = Util.getCurrentTimeStamp();
								pstmt1.setTimestamp(5, txnTime);
								pstmt1.setString(6, "OLA_DEPOSIT");
								isUpdate = pstmt1.executeUpdate();
								System.out.println("insert into retailer transaction master"+isUpdate);	
								// insert in deposit master													
								String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
								PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
								pstmtUpdate.setLong(1, transactionId);
								pstmtUpdate.setInt(2, reqBean.getWalletId());
								pstmtUpdate.setInt(3, reqBean.getPlrId());
								pstmtUpdate.setInt(4, userBean.getUserOrgId());
								pstmtUpdate.setDouble(5, reqBean.getDepositAmt());
								pstmtUpdate.setDouble(6, retailerComm);
								pstmtUpdate.setDouble(7, retNetAmt);
								pstmtUpdate.setDouble(8, agentComm);
								pstmtUpdate.setDouble(9, agentNetAmt);
								pstmtUpdate.setLong(10, agentRefTransactionId);
								pstmtUpdate.setString(11, "CLAIM_BAL");									
								pstmtUpdate.setString(12, reqBean.getDeviceType());
								pstmtUpdate.setLong(13, imsTransactionId);
								pstmtUpdate.setString(14,"PENDING");
								pstmtUpdate.executeUpdate();
																	
								/*// update st_lms_organization_master for claimable balance for retailer
								CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
									commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,userBean.getUserOrgId(), "CREDIT", con);
								// update st_lms_organization_master for claimable balance  for agent 
									commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,userBean.getParentOrgId(), "CREDIT", con);					
									
							*/		isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNetAmt, "CLAIM_BAL", "CREDIT", userBean
											.getUserOrgId(), userBean
											.getParentOrgId(), "RETAILER", 0, con);
									if(!isValid){
										throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
									}
									isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "CREDIT", userBean
											.getParentOrgId(),0, "AGENT", 0, con);
									if(!isValid){
										throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
									}
									con.commit();
									System.out.println("in ola helper amount is deposit Successfully");
									reqBean.setTransactionId(transactionId);
									if("PLAYER_LOTTERY".equals(reqBean.getWalletDevName())){
										resBean = PlayerLotteryIntegration.playerDeposit(reqBean);
									}else if("TabletGaming".equals(reqBean.getWalletDevName()) || "KhelPlayRummy".equals(reqBean.getWalletDevName()) || "GroupRummy".equals(reqBean.getWalletDevName()) ||"ALA_WALLET".equals(reqBean.getWalletDevName())){
										Map<String, String> depositRespMap = null;
										depositRespMap =OlaHelper.sendDepositInfoToKpRummy(reqBean.getWalletId(), userName.toString(), reqBean.getDepositAmt(), transactionId);
										logger.info("Khelplay deposit response"+depositRespMap.toString());
										resBean=new OLADepositResponseBean();
										if(depositRespMap != null && depositRespMap.get("respMsg") != null && depositRespMap.get("requestId")!=null){
											resBean.setSuccess(true);
											resBean.setRefTxnId(depositRespMap.get("requestId"));
										}else{
											resBean.setSuccess(false);
										}
									}
									  boolean isSuccess=resBean.isSuccess();
									  
									  if(isSuccess){														    	   // save refTransId 
										imsTransactionId= Long.parseLong(resBean.getRefTxnId());
										pstmt1 = con.prepareStatement("update st_ola_ret_deposit set ims_ref_transaction_id=? , status = ? where transaction_id=?");
										pstmt1.setLong(1,imsTransactionId);
										pstmt1.setString(2,"DONE");
										pstmt1.setLong(3, transactionId);
										pstmt1.executeUpdate();
										// Updating st_lms_ret_offline_master After Getting Success...
										Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(),"OLA_DEP",con);
										con.commit();									
										resBean.setTxnId(transactionId);
										resBean.setTxnDate(txnTime);
										resBean.setSuccess(true);
									 }else{
										    boolean isRefund = doRefund(transactionId, userBean, con);
										    if(isRefund){
											    pstmt1 = con.prepareStatement("update st_ola_ret_deposit set status = ? where transaction_id=?");
												pstmt1.setString(1,"FAILED");
												pstmt1.setLong(2, transactionId);
												pstmt1.executeUpdate();
												con.commit();
												if(resBean.getReponseCode() == 10030 || resBean.getReponseCode() == 10032){
													throw new LMSException(resBean.getReponseCode());
												}else{
													throw new LMSException(LMSErrors.PLAYER_LOTTERY_ERROR_CODE);
												}
										    }
									}
									// transactionId
							}else {									
								throw new LMSException(LMSErrors.DEPOSIT_MONEY_ERROR_CODE);
							}
						}		
			} else {
				throw new LMSException(LMSErrors.BALANCE_VERIFICATION_ERROR_CODE);
			}
		}catch (LMSException e) {
			if(e.getErrorCode() == 10001){
				e.setErrorMessage(Double.toString(olaDepositLimit));
		}
		throw e;
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}finally{
			DBConnect.closeCon(con);
		}
		return resBean;
	}
	
	
	
	public static boolean doRefund(long depositTransactionId, UserInfoBean userBean, Connection con) throws LMSException{
		 boolean isRefund;
		try {
				isRefund = OlaRetDepositDaoImpl.depositeRefund(depositTransactionId, userBean, con);
				if(isRefund){
					isRefund = true;
				}else{
					throw new LMSException(LMSErrors.DEPOSIT_REFUND_ERROR_CODE);
				}
		} catch (SQLException e) {
			 throw new LMSException(LMSErrors.DEPOSIT_REFUND_ERROR_CODE);
		}
		return isRefund;
	}



}
