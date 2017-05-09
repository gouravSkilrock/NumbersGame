 package com.skilrock.lms.controller.pwtMgmtDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.RetPWTProcessHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.BoardTicketDataBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

/**
 * @author Nikhil K. Bansal
 * @category Direct Player Pwt MerchantWise DBupdation
 */

 public class pwtMgmtDaoImpl{
		 
	private static final Logger logger = LoggerFactory.getLogger(pwtMgmtDaoImpl.class);
		
	private pwtMgmtDaoImpl(){}
	
	private static pwtMgmtDaoImpl classInstance;

	public static synchronized pwtMgmtDaoImpl getInstance() {
		if(classInstance == null)
			classInstance = new pwtMgmtDaoImpl();
		return classInstance;
	}
	
	public List<Long> payDirectPwtProcessAtLMS(String verCode,PwtVerifyTicketBean pwtBean,UserInfoBean userInfoBean,Connection con) throws LMSException{
		PreparedStatement pstmt =null;
		String playerType = "anonymous";
		String reqStatus =null;
		double netPwtAmt = 0.0;
	
		List<Long> transIdList = new ArrayList<Long>();
		try{
			String recIdForApp = GenerateRecieptNo.generateRequestIdDraw("DGREQUEST");
			pwtBean.setRecieptNumber(recIdForApp);
			if (pwtBean.getVerifyTicketDrawDataBeanList() != null && pwtBean.getVerifyTicketDrawDataBeanList().size() > 0) {
				for (PwtVerifyTicketDrawDataBean drawIdBean : pwtBean.getVerifyTicketDrawDataBeanList()){
					if( drawIdBean.getBoardTicketBeanList().size()>0) {
					    for (BoardTicketDataBean panelIdBean : drawIdBean.getBoardTicketBeanList()){
					    	if(panelIdBean.getWinningAmt()>0.0){
						    	reqStatus = "PAID";
								String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							
								pstmt = con.prepareStatement(insertAppQuery);
								pstmt.setString(1, playerType.toUpperCase());
								pstmt.setString(2, recIdForApp);
								pstmt.setInt(3,pwtBean.getUserId());
								pstmt.setObject(4,pwtBean.getTicketNumber());
								pstmt.setInt(5, drawIdBean.getDrawId());
								pstmt.setInt(6, panelIdBean.getBoardId());
								pstmt.setInt(7, pwtBean.getGameId());
								pstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()));
								pstmt.setDouble(9, 0.0);
								pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()));
								pstmt.setString(11, reqStatus);
								pstmt.setString(12, userInfoBean.getUserType());
								pstmt.setInt(13, userInfoBean.getUserId());
								pstmt.setInt(14, userInfoBean.getUserOrgId());
								pstmt.setInt(15, userInfoBean.getUserOrgId());
								pstmt.setString(16, userInfoBean.getUserType());
								pstmt.setString(17,"BO");
								pstmt.setInt(18, userInfoBean.getUserId());
								pstmt.setInt(19, userInfoBean.getUserOrgId());
								pstmt.setString(20, "BO");
								pstmt.setInt(21, userInfoBean.getUserOrgId());
								Calendar cal = Calendar.getInstance();
								Timestamp currentDate = null;
								currentDate = new Timestamp(cal.getTime().getTime());	
								pstmt.setTimestamp(22,currentDate);
								pstmt.setTimestamp(23,currentDate);
								pstmt.setString(24, "Paid as Anonymous Player");
								pstmt.executeUpdate();
								logger.info("insertion into pwt temp request  table = "+ pstmt);
								ResultSet rs = pstmt.getGeneratedKeys();
								int reqId = 0;
								if (rs.next()) {
									reqId = rs.getInt(1);
								} else {
									throw new LMSException("NO Data Inserted in st_pwt_approval_request_master table");
								}
	
								// insert in draw pwt inv table
								if (reqStatus!=null) {
									reqStatus = "CLAIM_PLR_BO";
								}
								String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr,merchant_code) values (?, ?, ?, ?, ?,?,?)";
								PreparedStatement insIntoDGPwtInvPstmt = con.prepareStatement(insIntoDGPwtInvQuery);
								insIntoDGPwtInvPstmt.setInt(1,pwtBean.getGameId());
								insIntoDGPwtInvPstmt.setString(2,pwtBean.getTicketNumber());
								insIntoDGPwtInvPstmt.setInt(3,drawIdBean.getDrawId());
								insIntoDGPwtInvPstmt.setInt(4,panelIdBean.getBoardId());
								insIntoDGPwtInvPstmt.setDouble(5,CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()));
								insIntoDGPwtInvPstmt.setString(6,reqStatus);
								insIntoDGPwtInvPstmt.setString(7, "Y");
								insIntoDGPwtInvPstmt.setString(8, pwtBean.getMerchantCode());
								insIntoDGPwtInvPstmt.executeUpdate();
								
								netPwtAmt = netPwtAmt+ CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt());
								
								if ( reqStatus!=null) {
									logger.info("is  payin as anonymous::::True reqStatus "+reqStatus);
									// hard coded for anonymous player
									long transId = boDirectPlrPwtPayment(pwtBean.getTicketNumber(),drawIdBean.getDrawId(),pwtBean.getUserId(),CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()),
											0.0,reqId,null,null,null,null,"CASH",userInfoBean.getUserOrgId(),userInfoBean.getUserId(),Util.getGameNumberFromGameId(pwtBean.getGameId()),pwtBean.getGameId(),con, panelIdBean.getBoardId(),
											"PANEL_WISE");
									if (transId > 0) {
										String updateAppTable = "update st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where task_id = ?";
										PreparedStatement pstmt1 = con.prepareStatement(updateAppTable);
										pstmt1.setString(1, "BO");
										pstmt1.setInt(2, userInfoBean.getUserOrgId());
										pstmt1.setLong(3, transId);
										pstmt1.setInt(4, reqId);
										logger.debug("update  st_dg_approval_req_master Query::::"+ pstmt);
										pstmt1.executeUpdate();
										transIdList.add(transId);
										
									}else{
										logger.debug("Error In Transaction At LMS End");
										throw new LMSException("Error At LMS End");
									}
								}
					    	}
						}
					}
				}
			}
		} catch (SQLException s) {
			s.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException el) {
			el.printStackTrace();
			throw el;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return transIdList;
	}
	
	public long boDirectPlrPwtPayment(String ticketNbr, int drawId,
			int playerId, double pwtAmt, double tax, int taskId,
			String chequeNbr, String draweeBank, String issuingParty,
			java.sql.Date chqDate, String paymentType, int userOrgId,
			int userId, int gameNbr, int gameId, Connection connection,
			Object panelId, String schemeType) throws LMSException {
		int isUpdate;
		try {
		/*	connection.commit();
			connection.setAutoCommit(false);*/
			// insert data into main transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection.prepareStatement(transMasQuery);
			pstmt.setString(1, "BO");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				long transId = rs.getLong(1);
				// insert in st_bo_transaction master
				pstmt = connection.prepareStatement(QueryManager.insertInBOTransactionMaster());
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);	
				pstmt.setString(4, "PLAYER");
				pstmt.setInt(5, playerId);
				pstmt.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				pstmt.setString(7, "DG_PWT_PLR");
				pstmt.executeUpdate();
				logger.debug("insert into BO transaction master = " + pstmt);

				String directPlrPayment = "insert into st_dg_bo_direct_plr_pwt (bo_user_id, "
						+ "bo_org_id, draw_id, transaction_id, transaction_date, game_id, player_id,"
						+ " pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank,"
						+ " issuing_party_name, task_id,panel_id ) values (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
				pstmt = connection.prepareStatement(directPlrPayment);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, drawId);
				pstmt.setLong(4, transId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				pstmt.setInt(6, gameId);
				pstmt.setInt(7, playerId);
				pstmt.setDouble(8, pwtAmt);
				pstmt.setDouble(9, tax);
				pstmt.setDouble(10, pwtAmt - tax);
				pstmt.setString(11, paymentType);

				if ("cash".equalsIgnoreCase(paymentType)
						|| "TPT".equalsIgnoreCase(paymentType)) {
					pstmt.setObject(12, null);
					pstmt.setObject(13, null);
					pstmt.setObject(14, null);
					pstmt.setObject(15, null);
				} else if ("cheque".equalsIgnoreCase(paymentType)) {
					pstmt.setString(12, chequeNbr);
					pstmt.setDate(13, chqDate);
					pstmt.setString(14, draweeBank);
					pstmt.setString(15, issuingParty);
				}
			
				pstmt.setInt(16, taskId);
				pstmt.setObject(17, panelId);
				pstmt.executeUpdate();
				logger.debug("insert into st_dg_bo_direct_plr_pwt = " + pstmt);

				// update ticket details into st_dg_pwt_inv_? table
				String insIntoDGPwtInvQuery = null;
				if ("DRAW_WISE".equalsIgnoreCase(schemeType.trim())) {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
							+ " where ticket_nbr = ? and draw_id = ?";
				} else {
					insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
							+ " where ticket_nbr = ? and draw_id = ? and panel_id="
							+ panelId;
				}
				PreparedStatement insIntoDGPwtInvPstmt = connection
						.prepareStatement(insIntoDGPwtInvQuery);
				insIntoDGPwtInvPstmt.setInt(1, gameId);
				insIntoDGPwtInvPstmt.setString(2, "CLAIM_PLR_BO");
				insIntoDGPwtInvPstmt.setLong(3, transId);
				insIntoDGPwtInvPstmt.setString(4, ticketNbr);
				insIntoDGPwtInvPstmt.setInt(5, drawId);
				logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);
				isUpdate=insIntoDGPwtInvPstmt.executeUpdate();
				if(isUpdate==0){
					throw new LMSException(LMSErrors.FAILURE_AT_TIMEOF_TRANSACTION_ERROR_CODE,LMSErrors.FAILURE_AT_TIMEOF_TRANSACTION_ERROR_MESSAGE);
				}
				return transId;

			} else {
				throw new LMSException(LMSErrors.FAILURE_AT_TIMEOF_TRANSACTION_ERROR_CODE,LMSErrors.FAILURE_AT_TIMEOF_TRANSACTION_ERROR_MESSAGE);
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} catch (LMSException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

	}
	
	
	public boolean updatePlayerPwtMerchantTransaction(PwtVerifyTicketBean pwtBean,String merchantTransId,Connection con) throws LMSException{
		boolean isSuccess=false;
		StringBuilder updateQary=new StringBuilder();
		PreparedStatement pStmt = null;
		int updateRow=0;
		try {
			updateQary.append("UPDATE st_dg_pwt_inv_").append(pwtBean.getGameId()).append(" SET merchant_trans_id= ").append(merchantTransId).append(" WHERE ticket_nbr=").append(pwtBean.getTicketNumber());
			pStmt = con.prepareStatement(updateQary.toString());
			logger.info("update Merchant Trans Id at lms end query"+updateQary.toString());
			updateRow=pStmt.executeUpdate();
			if(updateRow>0){
				isSuccess=true;
			}
			else{
				throw new LMSException(LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_CODE,LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return isSuccess;
	}
	
	
	public List<Long> payTpPwtProcessAtLMS(String verCode,PwtVerifyTicketBean pwtBean,UserInfoBean userInfoBean,Connection con) throws LMSException{
		PreparedStatement pstmt =null;
		String playerType = "anonymous";
		String reqStatus =null;
		double netPwtAmt = 0.0;
	
		List<Long> transIdList = new ArrayList<Long>();
		try{
			String recIdForApp = GenerateRecieptNo.generateRequestIdDraw("DGREQUEST");
			pwtBean.setRecieptNumber(recIdForApp);
			if (pwtBean.getVerifyTicketDrawDataBeanList() != null && pwtBean.getVerifyTicketDrawDataBeanList().size() > 0) {
				for (PwtVerifyTicketDrawDataBean drawIdBean : pwtBean.getVerifyTicketDrawDataBeanList()){
					if( drawIdBean.getBoardTicketBeanList()!=null && drawIdBean.getBoardTicketBeanList().size()>0) {
					    for (BoardTicketDataBean panelIdBean : drawIdBean.getBoardTicketBeanList()){
					    	if(panelIdBean.getWinningAmt()>0.0){
					    		/*	reqStatus = "PAID";
								
								String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr,merchant_code) values (?, ?, ?, ?, ?,?,?)";
								PreparedStatement insIntoDGPwtInvPstmt = con.prepareStatement(insIntoDGPwtInvQuery);
								insIntoDGPwtInvPstmt.setInt(1,pwtBean.getGameId());
								insIntoDGPwtInvPstmt.setString(2,pwtBean.getTicketNumber());
								insIntoDGPwtInvPstmt.setInt(3,drawIdBean.getDrawId());
								insIntoDGPwtInvPstmt.setInt(4,panelIdBean.getBoardId());
								insIntoDGPwtInvPstmt.setDouble(5,CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()));
								insIntoDGPwtInvPstmt.setString(6,reqStatus);
								insIntoDGPwtInvPstmt.setString(7, "Y");
								insIntoDGPwtInvPstmt.setString(8, pwtBean.getMerchantCode());
								insIntoDGPwtInvPstmt.executeUpdate();
								
								netPwtAmt = netPwtAmt+ CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt());
								
								
					    	}*/
					    	CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
					    	OrgPwtLimitBean orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),con);
							boolean isAutoScrap = "YES".equalsIgnoreCase(orgPwtLimit.getIsPwtAutoScrap())&& pwtBean.getTotalWinAmt() <= orgPwtLimit.getScrapLimit() ? true: false;
					    	RetPWTProcessHelper retHelper = new RetPWTProcessHelper();
					    	long transId = retHelper.retPwtPayment(userInfoBean.getUserId(),userInfoBean.getUserOrgId(),userInfoBean.getParentOrgId(), pwtBean.getGameId(),
									isAutoScrap,CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()), drawIdBean.getDrawId(), panelIdBean.getBoardId(),pwtBean.getTicketNumber(),con, false,false);
					    	transIdList.add(transId);
					    	}
						}
					}
				}
			}
		} catch (SQLException s) {
			s.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException el) {
			el.printStackTrace();
			throw el;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return transIdList;
	}

	public void updateMerchantCode(PwtVerifyTicketBean pwtBean, Connection con) throws LMSException{
		boolean isSuccess = false;
		StringBuilder updateQuery=new StringBuilder();
		PreparedStatement pStmt = null;
		int updateRow=0;
		try {
			updateQuery.append("UPDATE st_dg_pwt_inv_").append(pwtBean.getGameId()).append(" SET merchant_code= '").append(pwtBean.getMerchantCode()).append("' WHERE ticket_nbr=").append(pwtBean.getTicketNumber());
			pStmt = con.prepareStatement(updateQuery.toString());
			logger.info("update Merchant Trans Id at lms end query"+updateQuery.toString());
			updateRow=pStmt.executeUpdate();
			if(updateRow>0){
				isSuccess=true;
			}
			else{
				throw new LMSException(LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_CODE,LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

 }