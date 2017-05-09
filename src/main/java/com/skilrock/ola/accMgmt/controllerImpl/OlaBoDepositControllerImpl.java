package com.skilrock.ola.accMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.accMgmt.daoImpl.OlaBoDepositDaoImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;

public class OlaBoDepositControllerImpl  {
	
	static Log logger = LogFactory.getLog(OlaBoDepositControllerImpl.class);
	
	public 	OLADepositResponseBean  olaBoPlrDeposit(OLADepositRequestBean reqBean, UserInfoBean userBean) throws LMSException, GenericException {		
	
		Connection con = DBConnect.getConnection();
		long imsTransactionId = 0;
		int isUpdate;
		OLADepositResponseBean resBean = null;
		StringBuilder userName=new StringBuilder();
		try {
				int playerId=OlaCommonMethodControllerImpl.fetchPlayerIdFromRefCode(reqBean.getRefCode(), reqBean.getWalletId(), con,userName);
				reqBean.setPlrId(playerId);
			
				// insert in LMS transaction master
				
						con.setAutoCommit(false);
						boolean isPlayerBind = OlaCommonMethodControllerImpl.affiliatePlrBinding(userName.toString(),"DEPOSIT",reqBean.getDepositAnyWhere(),reqBean.getPlrId(), userBean,reqBean.getWalletId(),con); 
						logger.info("isBinding :"+isPlayerBind);
							
						if (isPlayerBind) {
							//String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
							String insertInLMS = QueryManager.insertInLMSTransactionMaster();
							PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
								pstmt1.setString(1, "BO");
								long transactionId = 0;
								pstmt1.executeUpdate();
								ResultSet rs1 = pstmt1.getGeneratedKeys();
								if (rs1.next()) {
									transactionId = rs1.getLong(1);					
									// insert into retailer transaction master
									pstmt1 = con.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
									pstmt1.setLong(1, transactionId);
									pstmt1.setInt(2, userBean.getUserId());
									pstmt1.setInt(3, userBean.getUserOrgId());
									pstmt1.setString(4, "PLAYER");
									pstmt1.setInt(5, playerId);
									pstmt1.setString(6, "OLA_DEPOSIT_PLR");
									Timestamp txnTime = Util.getCurrentTimeStamp();
									pstmt1.setTimestamp(7, txnTime);
									isUpdate = pstmt1.executeUpdate();
									
									logger.info("insert into bo transaction master"+isUpdate);	
									
									
									String insertQry = "insert into st_ola_bo_direct_plr_deposit(transaction_id,bo_user_id,bo_org_id,wallet_id,plr_id, deposit_amt,ims_ref_transaction_id,status) values(?,?,?,?,?,?,?,?)";
									PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
									pstmtUpdate.setLong(1, transactionId);
									pstmtUpdate.setInt(2, userBean.getUserId());
									pstmtUpdate.setInt(3, userBean.getUserOrgId());
									pstmtUpdate.setInt(4, reqBean.getWalletId());
									pstmtUpdate.setInt(5, playerId);
									pstmtUpdate.setDouble(6,reqBean.getDepositAmt() );
									pstmtUpdate.setInt(7, 0);
									pstmtUpdate.setString(8, "PENDING");
									pstmtUpdate.executeUpdate();
									con.commit();
									logger.info("in ola helper amount is deposit Successfully");

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
											pstmt1 = con.prepareStatement("update st_ola_bo_direct_plr_deposit set ims_ref_transaction_id=? , status = ? where transaction_id=?");
											pstmt1.setLong(1,imsTransactionId);
											pstmt1.setString(2,"DONE");
											pstmt1.setLong(3, transactionId);
											pstmt1.executeUpdate();															    	   
											// Updating st_lms_ret_offline_master After Getting Success...
											
											con.commit();										
											resBean.setTxnId(transactionId);
											resBean.setTxnDate(txnTime);
											resBean.setSuccess(true);
										 }else{
											    boolean isRefund = doRefund(transactionId, userBean, con);
											    if(isRefund){
												    pstmt1 = con.prepareStatement("update st_ola_bo_direct_plr_deposit set status = ? where transaction_id=?");
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
				
		}catch (LMSException e) {
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
				isRefund = OlaBoDepositDaoImpl.depositeRefund(depositTransactionId, userBean, con);
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
