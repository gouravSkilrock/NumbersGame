package com.skilrock.lms.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.BOMasterApprovalBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawTicketDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.dao.WinningMgmtDao;
import com.skilrock.lms.daoImpl.WinningTransactionManagerSLE.DirectPlrPwtBean;
import com.skilrock.lms.daoImpl.WinningTransactionManagerSLE.PwtInvBean;
import com.skilrock.lms.daoImpl.WinningTransactionManagerSLE.TransactionBean;
import com.skilrock.lms.dge.beans.PlayerBean;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.web.drawGames.common.Util;

public final class WinningMgmtDaoImplSLE implements WinningMgmtDao {
	private static Logger logger = LoggerFactory.getLogger(WinningMgmtDaoImplSLE.class);

	private volatile static WinningMgmtDaoImplSLE winMgmtDao = null;

	private WinningMgmtDaoImplSLE(){}

	public static WinningMgmtDaoImplSLE getInstance() {
		if (winMgmtDao == null) {
			synchronized (WinningMgmtDaoImplSLE.class) {
				if (winMgmtDao == null) {
					winMgmtDao = new WinningMgmtDaoImplSLE();
				}
			}
		}

		return winMgmtDao;
	}

	@Override
	public TPPwtResponseBean boNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside boNormalPay --");
		Connection connection = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			double govtCommRate = 0.00;
			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {

				double govtCommPwt = 0.00;
				if(drawBean.getWinningAmt() >= Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"))) {
					govtCommPwt = CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
					govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
				}

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(drawBean.getTransactionId())).setClaimAt("BO").setStatus("CLAIM_AT_BO").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean,true, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(1).setTransType("SLE_PWT_AUTO").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertBOTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setPwtInvId(pwtInvId).setTransId(transId).setTransTime(transactionTime).setPlayerId(1).setTaxAmount(govtCommPwt).setNetAmount(drawBean.getWinningAmt()-govtCommPwt).setWinningAmount(drawBean.getWinningAmt()).setPaymentType("CASH");
				WinningTransactionManagerSLE.insertBODirectPlrPwt(dirPwtBean, connection);

				WinningTransactionManagerSLE.updateBOPwtInv(requestBean.getGameId(), requestBean.getGameTypeId(), drawBean.getDrawId(), requestBean.getTicketNumber(), transId, connection);

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(transId));
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setStatus("PAID");
			responseBean.setGovtTaxPwt(govtCommRate);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	@Override
	public TPPwtResponseBean boHighPrize(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside boHighPrize --");
		Connection connection = null;
		PreparedStatement appReqStmt = null;
		//Statement stmt = null;
		//String query = null;
		ResultSet rs = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();
			PlayerBean playerBean = requestBean.getPlayerBean();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			//stmt = connection.createStatement();
			appReqStmt = connection.prepareStatement("INSERT INTO st_sle_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, game_type_id, draw_id, panel_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, approved_by_user_id, approved_by_type, approval_date, remarks, payment_done_by_user_id, payment_done_by_type,engine_trans_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

			int playerId = getPlayerIdOrRegister(playerBean, connection);

			String recIdForApp = GenerateRecieptNo.generateRequestIdSLE("SLEREQUEST", connection);
			logger.info("recIdForApp - "+recIdForApp);

			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {
				long taskId = 0L;

				double govtCommPwt = 0.00;
				if(drawBean.getWinningAmt() >= Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"))) {
					double govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
					govtCommPwt = CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
				}
				
				appReqStmt.setString(1, recIdForApp);
				appReqStmt.setInt(2, playerId);
				appReqStmt.setString(3, "PLAYER");
				appReqStmt.setString(4, requestBean.getTicketNumber());
				appReqStmt.setInt(5, requestBean.getGameId());
				appReqStmt.setInt(6, requestBean.getGameTypeId());
				appReqStmt.setInt(7, drawBean.getDrawId());
				appReqStmt.setInt(8, 1);
				appReqStmt.setDouble(9, drawBean.getWinningAmt());
				appReqStmt.setDouble(10, govtCommPwt);
				appReqStmt.setDouble(11, drawBean.getWinningAmt() - govtCommPwt);
				appReqStmt.setString(12, "PND_PAY");
				appReqStmt.setInt(13, userBean.getUserId());
				appReqStmt.setString(14, userBean.getUserType());
				appReqStmt.setString(15, transactionTime);
				appReqStmt.setInt(16, userBean.getUserId());
				appReqStmt.setString(17, userBean.getUserType());
				appReqStmt.setString(18, transactionTime);
				appReqStmt.setString(19, (requestBean.getRemarks()==null) ? null : requestBean.getRemarks().replaceAll("\\+", " "));
				appReqStmt.setInt(20, userBean.getUserId());
				appReqStmt.setString(21, userBean.getUserType());
				appReqStmt.setLong(22, drawBean.getTransactionId());
				logger.info("Insert In st_sle_approval_req_master - "+appReqStmt);
				appReqStmt.executeUpdate();
				rs = appReqStmt.getGeneratedKeys();
				if (rs.next()) {
					taskId = rs.getInt(1);
				} else {
					throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
				}

				/*PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setClaimAt("CLAIM_AT_BO").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(playerId).setTransType("SLE_PWT_PLR").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertBOTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setPwtInvId(pwtInvId).setTaskId(taskId).setTransId(transId).setTransTime(transactionTime).setPlayerId(playerId).setWinningAmount(drawBean.getWinningAmt()).setPaymentType("CASH");
				WinningTransactionManagerSLE.insertBODirectPlrPwt(dirPwtBean, connection);

				query = "UPDATE st_sle_approval_req_master SET transaction_id="+transId+" WHERE task_id="+taskId+";";
				logger.info("UPDATE st_sle_approval_req_master - "+query);
				stmt.executeUpdate(query);*/

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(taskId));
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setRequestId(recIdForApp);
			responseBean.setStatus("PAID");
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	@Override
	public TPPwtResponseBean agentNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside agentNormalPay --");
		Connection connection = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			stmt = connection.createStatement();

			boolean isFraud = false;//ResponsibleGaming.respGaming(userBean, "SLE_PWT", String.valueOf(requestBean.getTotalAmount()), connection);
			if (isFraud) {
				throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			}

			double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("SLE_HIGH_PRIZE_AMT"));
			double masApproveLimit = Double.parseDouble(Utility.getPropertyValue("SLE_MAS_APPROVE_LIMIT"));
			double govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
			double agentComm = Util.getSLEPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameTypeId());
			query = "SELECT (available_credit-claimable_bal) AS balance FROM st_lms_organization_master WHERE organization_id="+userBean.getUserOrgId()+";";
			logger.info("Agent Balance Query - "+query);

			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {
				if(masApproveLimit <= drawBean.getWinningAmt()) {
					drawTransMap.put(drawBean.getDrawId(), SLE.Status.MAS_APPROVAL_INIT);
					continue;
				} else if(highPrizeAmt <= drawBean.getWinningAmt() && drawBean.getWinningAmt() < masApproveLimit) {
					drawTransMap.put(drawBean.getDrawId(), SLE.Status.HIGH_PRIZE);
					continue;
				}

				double agentNet = CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt()) + CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt() * agentComm * .01);
				double govtCommPwt=CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
				
				if(drawBean.getWinningAmt()<Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"))){
					govtCommPwt=0;
					govtCommRate=0;
				}

				rs = stmt.executeQuery(query);
				if (rs.next()) {
					if (!(rs.getDouble("balance") >= agentNet)) {
						throw new LMSException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
					}
				} else {
					throw new LMSException(SLEErrors.INVALID_USER_NAME_CODE, SLEErrors.INVALID_USER_NAME_MESSAGE);
				}

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(drawBean.getTransactionId())).setClaimAt("AGENT").setStatus("CLAIM_AT_AGENT").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean,true, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(1).setTransType("SLE_PWT_AUTO").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertAgentTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setPwtInvId(pwtInvId).setTransId(transId).setTransTime(transactionTime).setPlayerId(1).setWinningAmount(drawBean.getWinningAmt()).setPaymentType("CASH").setPwtClaimStatus("CLAIM_BAL").setAgentClaimComm(agentComm*0.01*drawBean.getWinningAmt()).setNetAmount(agentNet);
				WinningTransactionManagerSLE.insertAgentDirectPlrPwt(dirPwtBean,govtCommPwt, connection);

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(transId));

				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet-govtCommPwt, "CLAIM_BAL", "DEBIT", userBean.getParentOrgId(), userBean.getUserOrgId(), "AGENT", userBean.getParentOrgId(), connection);
				if(!isValid) {
					throw new LMSException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setStatus("PAID");
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	@Override
	public TPPwtResponseBean agentHighPrize(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside agentHighPrize --");
		Connection connection = null;
		PreparedStatement appReqStmt = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();
			PlayerBean playerBean = requestBean.getPlayerBean();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			double agentComm = Util.getSLEPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameTypeId());
				
			stmt = connection.createStatement();
			appReqStmt = connection.prepareStatement("INSERT INTO st_sle_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, game_type_id, draw_id, panel_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, approved_by_user_id, approved_by_type, approval_date, remarks, payment_done_by_user_id, payment_done_by_type,engine_trans_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

			int playerId = getPlayerIdOrRegister(playerBean, connection);
			double govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
			
			String recIdForApp = GenerateRecieptNo.generateRequestIdSLE("SLEREQUEST", connection);
			logger.info("recIdForApp - "+recIdForApp);

			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {
				long taskId = 0L;
				double agentNet = CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt()) + CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt() * agentComm * .01);
				double govtCommPwt=CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
				appReqStmt.setString(1, recIdForApp);
				appReqStmt.setInt(2, playerId);
				appReqStmt.setString(3, "PLAYER");
				appReqStmt.setString(4, requestBean.getTicketNumber());
				appReqStmt.setInt(5, requestBean.getGameId());
				appReqStmt.setInt(6, requestBean.getGameTypeId());
				appReqStmt.setInt(7, drawBean.getDrawId());
				appReqStmt.setInt(8, 1);
				appReqStmt.setDouble(9, drawBean.getWinningAmt());
				appReqStmt.setDouble(10, govtCommPwt);
				appReqStmt.setDouble(11, drawBean.getWinningAmt()-govtCommPwt);
				appReqStmt.setString(12, "PAID");
				appReqStmt.setInt(13, userBean.getUserId());
				appReqStmt.setString(14, userBean.getUserType());
				appReqStmt.setString(15, transactionTime);
				appReqStmt.setInt(16, userBean.getUserId());
				appReqStmt.setString(17, userBean.getUserType());
				appReqStmt.setString(18, transactionTime);
				appReqStmt.setString(19, requestBean.getRemarks());
				appReqStmt.setInt(20, userBean.getUserId());
				appReqStmt.setString(21, userBean.getUserType());
				appReqStmt.setLong(17, drawBean.getTransactionId());
				logger.info("Insert In st_sle_approval_req_master - "+appReqStmt);
				appReqStmt.executeUpdate();
				rs = appReqStmt.getGeneratedKeys();
				if (rs.next()) {
					taskId = rs.getInt(1);
				} else {
					throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
				}

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(drawBean.getTransactionId())).setClaimAt("AGENT").setStatus("CLAIM_AT_AGENT").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean,true, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(playerId).setTransType("SLE_PWT_PLR").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertAgentTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setPwtInvId(pwtInvId).setTaskId(taskId).setTransId(transId).setTransTime(transactionTime).setPlayerId(playerId).setWinningAmount(drawBean.getWinningAmt()).setPaymentType("CASH").setPwtClaimStatus("CLAIM_BAL").setAgentClaimComm(agentComm*0.01*drawBean.getWinningAmt()).setNetAmount(agentNet);
				WinningTransactionManagerSLE.insertAgentDirectPlrPwt(dirPwtBean,govtCommPwt, connection);

				query = "UPDATE st_sle_approval_req_master SET transaction_id="+transId+" WHERE task_id="+taskId+";";
				logger.info("UPDATE st_sle_approval_req_master - "+query);
				stmt.executeUpdate(query);

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(transId));
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setStatus("PAID");
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	@Override
	public TPPwtResponseBean retailerNormalPay(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside retailerNormalPay --");
		Connection connection = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			boolean isFraud = ResponsibleGaming.respGaming(userBean, "SLE_PWT", String.valueOf(requestBean.getTotalAmount()), connection);
			if (isFraud) {
				throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			}

			double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("SLE_HIGH_PRIZE_AMT"));
			double masApproveLimit = Double.parseDouble(Utility.getPropertyValue("SLE_MAS_APPROVE_LIMIT"));

			double retailerComm = Util.getSLEPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameTypeId());
			double agentComm = Util.getSLEPwtCommVariance(userBean.getParentOrgId(), requestBean.getGameTypeId());
			double govtCommRate = 0.00;
			
			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {
				if(masApproveLimit <= drawBean.getWinningAmt()) {
					drawTransMap.put(drawBean.getDrawId(), SLE.Status.MAS_APPROVAL_INIT);
					continue;
				} else if(highPrizeAmt <= drawBean.getWinningAmt() && drawBean.getWinningAmt() < masApproveLimit) {
					drawTransMap.put(drawBean.getDrawId(), SLE.Status.HIGH_PRIZE);
					continue;
				}

				double retailerNet = CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt()) + CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt() * retailerComm * .01);
				double agentNet = CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt()) + CommonMethods.fmtToTwoDecimal(drawBean.getWinningAmt() * agentComm * .01);
				double govtCommPwt = 0.00;
				if(drawBean.getWinningAmt() >= Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"))) {
					govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
					govtCommPwt = CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
				}
				
				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(drawBean.getTransactionId())).setClaimAt("RETAILER").setStatus("CLAIM_AT_RETAILER").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean,true, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyId(requestBean.getGameTypeId()).setTransType("SLE_PWT").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertRetailerTransaction(transBean, connection);

				DirectPlrPwtBean pwtBean = new DirectPlrPwtBean().setTransId(transId).setPwtInvId(pwtInvId).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setUserBean(userBean).setWinningAmount(drawBean.getWinningAmt()).setRetailerClaimComm(retailerComm*0.01*drawBean.getWinningAmt()).setRetailerNetAmt(retailerNet).setAgentClaimComm(agentComm*0.01*drawBean.getWinningAmt()).setAgentNetAmt(agentNet).setTransTime(transactionTime).setPwtClaimStatus("CLAIM_BAL");
				WinningTransactionManagerSLE.insertRetailerPwt(pwtBean,govtCommPwt, connection);

				WinningTransactionManagerSLE.updateRetailerPwtInv(requestBean.getGameId(), requestBean.getGameTypeId(), drawBean.getDrawId(), requestBean.getTicketNumber(), transId, connection);

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(transId));

				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retailerNet-govtCommPwt, "CLAIM_BAL", "DEBIT", userBean.getUserOrgId(), userBean.getParentOrgId(), "RETAILER", 0, connection);
				if(!isValid) {
					throw new LMSException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE, SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}

				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet-govtCommPwt, "CLAIM_BAL", "DEBIT", userBean.getParentOrgId(),0, "AGENT", 0, connection);
				if(!isValid) {
					throw new LMSException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			}

			//	Update Heart Beat After PWT Completion
			Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(), "SLE_PWT", connection);

			connection.commit();

			new AjaxRequestHelper().getAvlblCreditAmt(userBean, connection);
			double balance = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setBalance(balance);
			responseBean.setStatus("PAID");
			responseBean.setGovtTaxPwt(govtCommRate);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	@Override
	public TPPwtResponseBean masApprovalInit(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside boMasApprovalInit --");
		Connection connection = null;
		PreparedStatement appReqStmt = null;
		ResultSet rs = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();

			PlayerBean playerBean = requestBean.getPlayerBean();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			int playerId = getPlayerIdOrRegister(playerBean, connection);

			String recIdForApp = GenerateRecieptNo.generateRequestIdSLE("SLEREQUEST", connection);
			logger.info("recIdForApp - "+recIdForApp);

			appReqStmt = connection.prepareStatement("INSERT INTO st_sle_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, game_type_id, draw_id, panel_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, remarks,engine_trans_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {
				int taskId = 0;

				double govtCommPwt = 0.00;
				if(drawBean.getWinningAmt() >= Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"))) {
					double govtCommRate = Util.getSLEGameMap().get(requestBean.getGameTypeId()).getGovtCommPwt();
					govtCommPwt = CommonMethods.fmtToTwoDecimal(govtCommRate*0.01*drawBean.getWinningAmt());
				}

				appReqStmt.setString(1, recIdForApp);
				appReqStmt.setInt(2, playerId);
				appReqStmt.setString(3, "PLAYER");
				appReqStmt.setString(4, requestBean.getTicketNumber());
				appReqStmt.setInt(5, requestBean.getGameId());
				appReqStmt.setInt(6, requestBean.getGameTypeId());
				appReqStmt.setInt(7, drawBean.getDrawId());
				appReqStmt.setInt(8, 1);
				appReqStmt.setDouble(9, drawBean.getWinningAmt());
				appReqStmt.setDouble(10, govtCommPwt);
				appReqStmt.setDouble(11, drawBean.getWinningAmt() - govtCommPwt);
				appReqStmt.setString(12, "PND_MAS");
				appReqStmt.setInt(13, userBean.getUserId());
				appReqStmt.setString(14, userBean.getUserType());
				appReqStmt.setString(15, transactionTime);
				appReqStmt.setString(16, (requestBean.getRemarks()==null) ? null : requestBean.getRemarks().replaceAll("\\+", " "));
				appReqStmt.setLong(17, drawBean.getTransactionId());
				logger.info("Insert In st_sle_approval_req_master - "+appReqStmt);
				appReqStmt.executeUpdate();
				rs = appReqStmt.getGeneratedKeys();
				if (rs.next()) {
					taskId = rs.getInt(1);
				} else {
					throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
				}

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(taskId));
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setRequestId(recIdForApp);
			responseBean.setStatus("PENDING");
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
		return responseBean;
	}

	@Override
	public TPPwtResponseBean masApprovalDone(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside boMasApprovalDone --");
		Connection connection = null;
		PreparedStatement appReqStmt = null;

		TPPwtResponseBean responseBean = null;
		Map<Integer, String> drawTransMap = new HashMap<Integer, String>();
		try {
			String transactionTime = Util.getCurrentTimeString();
			
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			int playerId = requestBean.getPlayerBean().getPlayerId();

			appReqStmt = connection.prepareStatement("UPDATE st_sle_approval_req_master SET request_status=?, approved_by_user_id=?, approved_by_type=?, approval_date=?, remarks=?, payment_done_by_user_id=?, payment_done_by_type=?, transaction_id=? WHERE task_id=?;");

			for (DrawTicketDataBean drawBean : requestBean.getDrawDataList()) {

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setWinningAmount(drawBean.getWinningAmt()).setTicketNumber(requestBean.getTicketNumber()).setClaimAt("BO").setStatus("CLAIM_AT_BO").setDirPly(true);
				int pwtInvId = WinningTransactionManagerSLE.insertPwtInventory(invBean,false, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(playerId).setTransType("SLE_PWT_PLR").setTransTime(transactionTime);
				long transId = WinningTransactionManagerSLE.insertBOTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setGameTypeId(requestBean.getGameTypeId()).setDrawId(drawBean.getDrawId()).setPwtInvId(pwtInvId).setTaskId(drawBean.getTaskId()).setTransId(transId).setTransTime(transactionTime).setPlayerId(playerId).setTaxAmount(drawBean.getTaxAmt()).setNetAmount(drawBean.getNetAmt()).setWinningAmount(drawBean.getWinningAmt()).setPaymentType("CASH");
				WinningTransactionManagerSLE.insertBODirectPlrPwt(dirPwtBean, connection);

				appReqStmt.setString(1, "PAID");
				appReqStmt.setInt(2, userBean.getUserId());
				appReqStmt.setString(3, userBean.getUserType());
				appReqStmt.setString(4, transactionTime);
				appReqStmt.setString(5, requestBean.getRemarks());
				appReqStmt.setInt(6, userBean.getUserId());
				appReqStmt.setString(7, userBean.getUserType());
				appReqStmt.setLong(8, transId);
				appReqStmt.setLong(9, drawBean.getTaskId());
				logger.info("UPDATE st_sle_approval_req_master - "+appReqStmt);
				appReqStmt.executeUpdate();

				WinningTransactionManagerSLE.updateBOPwtInv(requestBean.getGameId(), requestBean.getGameTypeId(), drawBean.getDrawId(), requestBean.getTicketNumber(), transId, connection);

				drawTransMap.put(drawBean.getDrawId(), String.valueOf(transId));
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setGameTypeId(requestBean.getGameTypeId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setDrawTransMap(drawTransMap);
			responseBean.setDoneByUserId(userBean.getUserId());
			responseBean.setStatus("PAID");
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	private static int getPlayerIdOrRegister(PlayerBean playerBean, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;

		int playerId = 0;
		try {
			String firstName = (playerBean.getFirstName()==null) ? null : playerBean.getFirstName().replaceAll("\\+", " ");
			String lastName = (playerBean.getLastName()==null) ? null : playerBean.getLastName().replaceAll("\\+", " ");
			String idType = (playerBean.getIdType()==null) ? null : playerBean.getIdType().replaceAll("\\+", " ");
			String idNumber = (playerBean.getIdNumber()==null) ? null : playerBean.getIdNumber().replaceAll("\\+", " ");

			stmt = connection.createStatement();
			String query = "SELECT player_id FROM st_lms_player_master WHERE first_name='"+firstName+"' AND last_name='"+lastName+"' AND photo_id_type='"+idType+"' AND photo_id_nbr='"+idNumber+"';";
			logger.info("Get Player Basic Info - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				playerId = rs.getInt("player_id");
			} else {
				String emailId = (playerBean.getEmailId()==null) ? null : playerBean.getEmailId().replaceAll("\\+", " ");
				String phoneNo = (playerBean.getPhoneNumber()==null) ? null : playerBean.getPhoneNumber().replaceAll("\\+", " ");
				String address1 = (playerBean.getAddress1()==null) ? null : playerBean.getAddress1().replaceAll("\\+", " ");
				String address2 = (playerBean.getAddress2()==null) ? null : playerBean.getAddress2().replaceAll("\\+", " ");
				String city = (playerBean.getCity()==null) ? null : playerBean.getCity().replaceAll("\\+", " ");
				String state = (playerBean.getState()==null) ? null : playerBean.getState().replaceAll("\\+", " ");
				String country = (playerBean.getCountry()==null) ? null : playerBean.getCountry().replaceAll("\\+", " ");

				query = "INSERT INTO st_lms_player_master (first_name, last_name, email_id, phone_nbr, addr_line1, addr_line2, city, state_code, country_code, pin_code, photo_id_type, photo_id_nbr) VALUES ("+stmtParamSetter(firstName, lastName, emailId, phoneNo, address1, address2, city, state, country, "0", idType, idNumber)+");";
				logger.info("Insert In st_lms_player_master - "+query);
				stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					playerId = rs.getInt(1);
				} else {
					throw new LMSException(LMSErrors.PLAYER_REGISTRATION_ERROR_CODE, LMSErrors.PLAYER_REGISTRATION_ERROR_MESSAGE);
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		logger.info("Player ID - "+playerId);
		return playerId;
	}

	private static final String stmtParamSetter(String... params) {
		StringBuilder stmtBuilder = new StringBuilder();

		for(String param : params) {
			stmtBuilder.append("'"+param+"',");
		}
		stmtBuilder.deleteCharAt(stmtBuilder.length()-1);

		return stmtBuilder.toString();
	}

	public List<BOMasterApprovalBean> getMasOrPendingRequests(BOMasterApprovalBean requestBean, String requestType, Connection connection) throws LMSException {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		List<BOMasterApprovalBean> masterApprovalList = new ArrayList<BOMasterApprovalBean>();
		BOMasterApprovalBean approvalBean = null;
		try {
			String appender = "";
			if(requestBean.getRequestId() != null && requestBean.getRequestId().length()>0)
				appender += " AND request_id='"+requestBean.getRequestId()+"'";
			if (requestBean.getFirstName() != null && requestBean.getFirstName().length()>0)
				appender += " AND first_name='"+requestBean.getFirstName()+"'";
			if (requestBean.getLastName() != null && requestBean.getLastName().length()>0)
				appender += " AND last_name='"+requestBean.getLastName()+"'";

			query = "SELECT task_id,tax_amt,net_amt, game_id, game_type_id, draw_id, ticket_nbr, pwt_amt, request_id, (SELECT NAME FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE um.user_id=11001) request_by, request_status, party_type, player_id, first_name, last_name, city, remarks FROM st_sle_approval_req_master arm INNER JOIN st_lms_player_master pm ON arm.party_id=pm.player_id WHERE request_status='"+requestType+"'"+appender+";";
			stmt = connection.createStatement();
			logger.info("getMasterApprovalRequests - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				approvalBean = new BOMasterApprovalBean();
				approvalBean.setTaskId(rs.getInt("task_id"));
				approvalBean.setGameId(rs.getInt("game_id"));
				approvalBean.setGameTypeId(rs.getInt("game_type_id"));
				approvalBean.setDrawId(rs.getInt("draw_id"));
				approvalBean.setTicketNumber(rs.getString("ticket_nbr"));
				approvalBean.setWinningAmount(rs.getDouble("pwt_amt"));
				approvalBean.setTaxAmount(rs.getDouble("tax_amt"));
				approvalBean.setNetAmount(rs.getDouble("net_amt"));
				approvalBean.setRequestId(rs.getString("request_id"));
				approvalBean.setRequestFor(rs.getString("party_type"));
				approvalBean.setRequestedBy(rs.getString("request_by"));
				approvalBean.setRequestStatus(rs.getString("request_status"));
				approvalBean.setPlayerId(rs.getInt("player_id"));
				approvalBean.setFirstName(rs.getString("first_name"));
				approvalBean.setLastName(rs.getString("last_name"));
				approvalBean.setCity(rs.getString("city"));
				approvalBean.setRemarks(rs.getString("remarks"));
				masterApprovalList.add(approvalBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return masterApprovalList;
	}

	public boolean processMasterApproval(int taskId, String processType, int userId, String userType, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		boolean status = false;
		try {
			pstmt = connection.prepareStatement("UPDATE st_sle_approval_req_master SET request_status=?, approved_by_user_id=?, approved_by_type=?, approval_date=? WHERE task_id=?;");
			pstmt.setString(1, processType);
			pstmt.setInt(2, userId);
			pstmt.setString(3, userType);
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setInt(5, taskId);
			logger.info("processMasterApproval - "+pstmt);
			pstmt.executeUpdate();
			status = true;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return status;
	}
}