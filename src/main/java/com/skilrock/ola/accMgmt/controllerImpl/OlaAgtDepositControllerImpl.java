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
import com.skilrock.ola.accMgmt.daoImpl.OlaAgtDepositDaoImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;

public class OlaAgtDepositControllerImpl  {
	
	static Log logger = LogFactory.getLog(OlaAgtDepositControllerImpl.class);
	
	public 	OLADepositResponseBean  olaAgtPlrDeposit(OLADepositRequestBean reqBean, UserInfoBean userBean) throws LMSException, GenericException {		
	
		Connection con = DBConnect.getConnection();

		
		double agentComm = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		int isUpdate;
		OLADepositResponseBean resBean = null;
		double olaDepositLimit = 0.0;
		boolean isValid=false;
		StringBuilder userName=new StringBuilder();
		try {
				int playerId=OlaCommonMethodControllerImpl.fetchPlayerIdFromRefCode(reqBean.getRefCode(), reqBean.getWalletId(), con,userName);
				reqBean.setPlrId(playerId);
			
				int agentOrgId = userBean.getUserOrgId();
				agentComm = OlaCommonMethodControllerImpl.fetchOLACommOfOrganization(reqBean.getWalletId(), agentOrgId, "DEPOSIT", "AGENT", con);
				agentNetAmt = (reqBean.getDepositAmt() - ((reqBean.getDepositAmt() * agentComm) / 100));
				
				OrgPwtLimitBean orgPwtLimit = OlaCommonMethodControllerImpl.fetchPwtLimitsOfOrgnization(agentOrgId, con);

				olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
				logger.info("olaDepositLimit"+olaDepositLimit+"ola deposite money"+reqBean.getDepositAmt());		
				if (reqBean.getDepositAmt() > olaDepositLimit) {
					throw new LMSException(LMSErrors.EXCEED_DEPOSIT_AMOUNT_REQUEST_ERROR_CODE);
				}				
				// check with retailer and agent balance to deposit
				boolean isAgtSalBalanceAval = OlaCommonMethodControllerImpl.checkOrgBalance( agentOrgId, agentNetAmt, "AGENT", con);
				logger.info(" AgentSaleBalanceAvl : "+isAgtSalBalanceAval);
				// insert in LMS transaction master
				if (isAgtSalBalanceAval) {
						con.setAutoCommit(false);
						boolean isPlayerBind = OlaCommonMethodControllerImpl.affiliatePlrBinding(userName.toString(),"DEPOSIT",reqBean.getDepositAnyWhere(),reqBean.getPlrId(), userBean,reqBean.getWalletId(),con); 
						logger.info("isBinding :"+isPlayerBind);
							
						if (isPlayerBind) {
							//String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
							String insertInLMS = QueryManager.insertInLMSTransactionMaster();
							PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
								pstmt1.setString(1, "AGENT");
								long transactionId = 0;
								pstmt1.executeUpdate();
								ResultSet rs1 = pstmt1.getGeneratedKeys();
								if (rs1.next()) {
									transactionId = rs1.getLong(1);					
									// insert into retailer transaction master
									pstmt1 = con.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
									pstmt1.setLong(1, transactionId);
									pstmt1.setInt(2, userBean.getUserId());
									pstmt1.setInt(3, userBean.getUserOrgId());
									pstmt1.setString(4, "PLAYER");	
									pstmt1.setInt(5, playerId);
									pstmt1.setString(6, "OLA_DEPOSIT_PLR");
									Timestamp txnTime = Util.getCurrentTimeStamp();
									pstmt1.setTimestamp(7, txnTime);
									
									isUpdate = pstmt1.executeUpdate();
									logger.info("insert into agent transaction master"+isUpdate);	
									// insert in deposit master		
									
									String insertQry = "insert into st_ola_agt_direct_plr_deposit(transaction_id,agent_user_id,agent_org_id,wallet_id,plr_id,deposit_amt,net_amt,deposit_claim_status,agt_claim_comm,status)values(?,?,?,?,?,?,?,?,?,?)";
									PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
									pstmtUpdate.setLong(1, transactionId);
									pstmtUpdate.setInt(2, userBean.getUserId());
									pstmtUpdate.setInt(3, userBean.getUserOrgId());
									
									pstmtUpdate.setInt(4, reqBean.getWalletId());
									pstmtUpdate.setInt(5,playerId);
									pstmtUpdate.setDouble(6, reqBean.getDepositAmt());
									pstmtUpdate.setDouble(7, agentNetAmt);
									pstmtUpdate.setString(8, "CLAIM_BAL");
									pstmtUpdate.setDouble(9, agentComm);
									pstmtUpdate.setString(10, "PENDING");
									pstmtUpdate.executeUpdate();
									
									
										isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "CREDIT", userBean
												.getUserOrgId(),0, "AGENT", 0, con);
										if(!isValid){
											throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
										}
										con.commit();
										System.out.println("in ola helper amount is deposit Successfully");
										reqBean.setTransactionId(transactionId);
										if("PLAYER_LOTTERY".equals(reqBean.getWalletDevName())){
											resBean = PlayerLotteryIntegration.playerDeposit(reqBean);
										}else if("TabletGaming".equals(reqBean.getWalletDevName()) || "KhelPlayRummy".equals(reqBean.getWalletDevName()) || "GroupRummy".equals(reqBean.getWalletDevName())){
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
											pstmt1 = con.prepareStatement("update st_ola_agt_direct_plr_deposit set ims_ref_transaction_id=? , status = ? where transaction_id=?");
											pstmt1.setLong(1,imsTransactionId);
											pstmt1.setString(2,"DONE");
											pstmt1.setLong(3, transactionId);
											pstmt1.executeUpdate();															    	   
											con.commit();										
											resBean.setTxnId(transactionId);
											resBean.setTxnDate(txnTime);
											resBean.setSuccess(true);
										 }else{
											    boolean isRefund = doRefund(transactionId, userBean, con);
											    if(isRefund){
												    pstmt1 = con.prepareStatement("update st_ola_agt_direct_plr_deposit set status = ? where transaction_id=?");
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
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		return resBean;
	}
	
	
	
	public static boolean doRefund(long depositTransactionId, UserInfoBean userBean, Connection con) throws LMSException{
		 boolean isRefund;
		try {
				isRefund = OlaAgtDepositDaoImpl.depositeRefund(depositTransactionId, userBean, con);
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
