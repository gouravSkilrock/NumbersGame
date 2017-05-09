package com.skilrock.ola.accMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.accMgmt.common.AccountMgmtCommonFunction;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalResponseBean;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;

public class OlaBoWithdrawlControllerImpl {
	
static Log logger = LogFactory.getLog(OlaBoWithdrawlControllerImpl.class);
	
	public 	OLAWithdrawalResponseBean  olaBoPlrWithdrawal(OLAWithdrawalRequestBean reqBean, UserInfoBean userBean) throws LMSException, GenericException {
		
		Connection con = DBConnect.getConnection();

		
		long tempTransactionId = 0;
		OLAWithdrawalResponseBean resBean = null;
		OlaPTResponseBean respBean = null;
		StringBuilder userName=new StringBuilder();
		try {
				int playerId=OlaCommonMethodControllerImpl.fetchPlayerIdFromRefCode(reqBean.getRefCode(), reqBean.getWalletId(), con,userName);
				reqBean.setPlayerId(playerId);				
				con.setAutoCommit(false);
	
				boolean isPlayerBind = OlaCommonMethodControllerImpl.affiliatePlrBinding(userName.toString(),"WITHDRAWAL",reqBean.getWithdrawlAnyWhere(),reqBean.getPlayerId(), userBean,reqBean.getWalletId(),con); 
				logger.info("isBinding :"+isPlayerBind);
				// insert withdrawal details in st_ola_withdrawal_temp
				tempTransactionId=AccountMgmtCommonFunction.withdrawlRequestIntiated(reqBean, userBean, con);
				con.commit(); // here commit the data before sending the request
				con.close();
				
					reqBean.setTxnId(tempTransactionId);
					if("PLAYER_LOTTERY".equals(reqBean.getDevWalletName())){
						respBean = PlayerLotteryIntegration.checkWithdrawalRequest(reqBean);// validate withdrawal request
					}else if("TabletGaming".equals(reqBean.getDevWalletName()) || "GroupRummy".equals(reqBean.getDevWalletName()) || "KhelPlayRummy".equals(reqBean.getDevWalletName())){
						 Map<String, String> withResMap=OlaHelper.verifyWithdrawalAtKpRummy(reqBean.getWalletId(), userName.toString(), reqBean.getWithdrawlAmt(), tempTransactionId, reqBean.getAuthenticationCode());
						 logger.info("Khelplay withdrawal response"+withResMap.toString());
						 respBean=new OlaPTResponseBean();
							if(withResMap != null && withResMap.get("respMsg") != null && withResMap.get("withTxnId")!=null){
								respBean.setSuccess(true);
								respBean.setImsWithdrawalTransactionId(Long.parseLong(withResMap.get("withTxnId")));
							}else{
								respBean.setSuccess(false);
							}
					}
					boolean isIMSSuccess = respBean.isSuccess();
					
					if(isIMSSuccess){
						con = DBConnect.getConnection();
						PreparedStatement updateTemp = con.prepareStatement("update st_ola_withdrawl_temp set status=? where task_id=?");
						updateTemp.setString(1, "PROCESSED");
						updateTemp.setLong(2,tempTransactionId);
						updateTemp.executeUpdate();						
						con.setAutoCommit(false);
						updateTemp.clearParameters();
						String insertInLMS = QueryManager.insertInLMSTransactionMaster();
						PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
						pstmt1.setString(1, "BO");
						long transactionId = 0;
						pstmt1.executeUpdate();
						ResultSet rs1 = pstmt1.getGeneratedKeys();	
						if (rs1.next()) {
							transactionId = rs1.getLong(1);
							// insert into retailer transaction master
							PreparedStatement pstmt = con
									.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
							pstmt.setLong(1, transactionId);
							pstmt.setInt(2, userBean.getUserId());
							pstmt.setInt(3, userBean.getUserOrgId());
							pstmt.setString(4, "PLAYER");
							pstmt.setInt(5, playerId);
							pstmt.setString(6, "OLA_WITHDRAWL_PLR");
							Timestamp tmp = Util.getCurrentTimeStamp();
							pstmt.setTimestamp(7, tmp);
							logger.info("Query "+pstmt);
							int updated =pstmt.executeUpdate();
							logger.info("insertd in st_lms_bo_transaction_master "+updated);
						
							
							String insertQry = "insert into st_ola_bo_direct_plr_withdrawl(transaction_id,wallet_id,plr_id,bo_user_id,bo_org_id,ims_ref_transaction_id,withdrawl_amt)values(?,?,?,?,?,?,?);";
							PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
							pstmtUpdate.setLong(1, transactionId);
							pstmtUpdate.setInt(2, reqBean.getWalletId());
							pstmtUpdate.setInt(3, reqBean.getPlayerId());
							pstmtUpdate.setInt(4, userBean.getUserId());
							pstmtUpdate.setInt(5, userBean.getUserOrgId());
							pstmtUpdate.setLong(6, respBean.getImsWithdrawalTransactionId());
							pstmtUpdate.setDouble(7, reqBean.getWithdrawlAmt());
							
							pstmtUpdate.executeUpdate();
							
							
							// Update temp
							updateTemp = con.prepareStatement("update st_ola_withdrawl_temp set status=?,ref_transaction_id=?,ims_ref_transaction_id=? where task_id=?");
							updateTemp.setString(1, "DONE");
							updateTemp.setLong(2,transactionId);
							updateTemp.setLong(3,respBean.getImsWithdrawalTransactionId());
							updateTemp.setLong(4,tempTransactionId);
							updateTemp.executeUpdate();
							
							con.commit();
							resBean = new OLAWithdrawalResponseBean();
							resBean.setTxnId(transactionId);
							resBean.setTxnDate(tmp);
							resBean.setSuccess(true);
						} else {							
							//con = DBConnect.getConnection();
							AccountMgmtCommonFunction.updateWithdrawlTmpStatus("FAILED", respBean.getImsWithdrawalTransactionId(), tempTransactionId, con);
							con.commit();
							throw new LMSException(LMSErrors.MONEY_WITHDRAWL_ERROR_CODE);
						}
					} else {
						con = DBConnect.getConnection();
						if(respBean == null){
							AccountMgmtCommonFunction.updateWithdrawlTmpStatus("DENIED", 0, tempTransactionId, con);
						}else{
							AccountMgmtCommonFunction.updateWithdrawlTmpStatus("DENIED", respBean.getImsWithdrawalTransactionId(), tempTransactionId, con);
						}
						throw new LMSException(LMSErrors.RMS_WITHDRAWL_ERROR_CODE);
					}
				
		}catch(LMSException le){
			le.printStackTrace();
			if(le.getErrorCode() == 10015 || le.getErrorCode() == 10028 || le.getErrorCode() == 10031){
			try{
				con = DBConnect.getConnection();
				if(respBean == null){
					AccountMgmtCommonFunction.updateWithdrawlTmpStatus("DENIED", 0, tempTransactionId, con);
				}else{
					AccountMgmtCommonFunction.updateWithdrawlTmpStatus("DENIED", respBean.getImsWithdrawalTransactionId(), tempTransactionId, con);
				}
			}catch(SQLException se){
				throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
			}
		}
			if(le.getErrorCode() == 10014){
				try{
					con = DBConnect.getConnection();
					AccountMgmtCommonFunction.updateWithdrawlTmpStatus("FAILED", 0, tempTransactionId, con);
				}catch(SQLException se){
					se.printStackTrace();
					throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
				}
			}
			throw le;
		}catch (Exception e) {
			throw new LMSException(LMSErrors.WITHDRAWL_ERROR_CODE);
		} finally {
			DBConnect.closeCon(con);
		}
		return resBean;		
	}
}