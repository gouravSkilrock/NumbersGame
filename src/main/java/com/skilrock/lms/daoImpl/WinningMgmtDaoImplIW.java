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
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.instantWin.common.IWErrors;
import com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.dao.WinningMgmtDao;
import com.skilrock.lms.daoImpl.WinningTransactionManagerIW.DirectPlrPwtBean;
import com.skilrock.lms.daoImpl.WinningTransactionManagerIW.PwtInvBean;
import com.skilrock.lms.daoImpl.WinningTransactionManagerIW.TransactionBean;
import com.skilrock.lms.dge.beans.PlayerBean;
import com.skilrock.lms.instantWin.common.IW;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public final class WinningMgmtDaoImplIW implements WinningMgmtDao {
	private static Logger logger = LoggerFactory.getLogger(WinningMgmtDaoImplIW.class);

	private volatile static WinningMgmtDaoImplIW winMgmtDao = null;

	private WinningMgmtDaoImplIW() {
	}

	public static WinningMgmtDaoImplIW getInstance() {
		if (winMgmtDao == null) {
			synchronized (WinningMgmtDaoImplIW.class) {
				if (winMgmtDao == null) {
					winMgmtDao = new WinningMgmtDaoImplIW();
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
		long taskId = 0;
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(requestBean.getTxnIdIw())).setClaimAt("BO").setStatus("CLAIM_AT_BO").setDirPly(true);
			int pwtInvId = WinningTransactionManagerIW.insertPwtInventory(invBean, connection);

			TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(requestBean.getPlayerId()).setTransType("IW_PWT_AUTO").setTransTime(transactionTime);
			long transId = WinningTransactionManagerIW.insertBOTransaction(transBean, connection);
			
			if (requestBean.getPlayerId() == -1 || requestBean.getPlayerId() == 0) {
				// Do Nothing
			} else
				taskId = WinningTransactionManagerIW.insertPWTApproval(userBean, requestBean, serviceCode, interfaceType, transId, transactionTime, connection);

			DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setPwtInvId(pwtInvId).setTransId(transId).setTransTime(transactionTime).setPlayerId(requestBean.getPlayerId()).setTaxAmount(0.00).setNetAmount(requestBean.getTotalAmount()).setWinningAmount(requestBean.getTotalAmount()).setPaymentType("CASH").setTaskId(taskId);
			WinningTransactionManagerIW.insertBODirectPlrPwt(dirPwtBean, connection);

			WinningTransactionManagerIW.updateBOPwtInv(requestBean.getGameId(), requestBean.getTicketNumber(), transId, connection);

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setResponseCode(0);
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setTxnId(String.valueOf(transId));
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
	public TPPwtResponseBean boHighPrize(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws LMSException {
		logger.info("-- Inside boHighPrize --");
		Connection connection = null;
		PreparedStatement appReqStmt = null;
		ResultSet rs = null;

		TPPwtResponseBean responseBean = null;
		try {
			String transactionTime = Util.getCurrentTimeString();
			PlayerBean playerBean = requestBean.getPlayerBean();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			appReqStmt = connection.prepareStatement("INSERT INTO st_iw_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, approved_by_user_id, approved_by_type, approval_date, remarks, payment_done_by_user_id, payment_done_by_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

			int playerId = getPlayerIdOrRegister(playerBean, connection);

			String recIdForApp = GenerateRecieptNo.generateRequestIdIW("IWREQUEST", connection);
			logger.info("recIdForApp - " + recIdForApp);

			long taskId = 0L;

			appReqStmt.setString(1, recIdForApp);
			appReqStmt.setInt(2, playerId);
			appReqStmt.setString(3, "PLAYER");
			appReqStmt.setString(4, requestBean.getTicketNumber());
			appReqStmt.setInt(5, requestBean.getGameId());
			appReqStmt.setDouble(6, requestBean.getTotalAmount());
			appReqStmt.setDouble(7, requestBean.getTaxAmt());
			appReqStmt.setDouble(8, requestBean.getNetAmt());
			appReqStmt.setString(9, "PND_PAY");
			appReqStmt.setInt(10, userBean.getUserId());
			appReqStmt.setString(11, userBean.getUserType());
			appReqStmt.setString(12, transactionTime);
			appReqStmt.setInt(13, userBean.getUserId());
			appReqStmt.setString(14, userBean.getUserType());
			appReqStmt.setString(15, transactionTime);
			appReqStmt.setString(16, (requestBean.getRemarks() == null) ? null : requestBean.getRemarks().replaceAll("\\+", " "));
			appReqStmt.setInt(17, userBean.getUserId());
			appReqStmt.setString(18, userBean.getUserType());
			logger.info("Insert In st_iw_approval_req_master - " + appReqStmt);
			appReqStmt.executeUpdate();
			rs = appReqStmt.getGeneratedKeys();
			if (rs.next()) {
				taskId = rs.getInt(1);
			} else {
				throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
			}
			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setResponseCode(0);
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setTxnId(String.valueOf(taskId));
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

		TPPwtResponseBean responseBean = new TPPwtResponseBean();
		long transId = 0L;
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			// boolean isFraud = ResponsibleGaming.respGaming(userBean, "IW_PWT", String.valueOf(requestBean.getTotalAmount()), connection);
			// if (isFraud) {
			// throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			// }

			double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("IW_HIGH_PRIZE_AMT"));
			double masApproveLimit = Double.parseDouble(Utility.getPropertyValue("IW_MAS_APPROVE_LIMIT"));

			double agentComm = com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper.fetchIWCommOfOrganization(requestBean.getGameId(), userBean.getUserOrgId(), "PWT", "AGENT", connection);

//			double agentComm = Util.getIWPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameId());

			if (masApproveLimit <= requestBean.getTotalAmount()) {
				responseBean.setStatus(IW.Status.MAS_APPROVAL_INIT);
			} else if (highPrizeAmt <= requestBean.getTotalAmount() && requestBean.getTotalAmount() < masApproveLimit) {
				responseBean.setStatus(IW.Status.HIGH_PRIZE);
			} else {
				responseBean.setStatus(IW.Status.NORMAL_PAY);
				double agentNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) + CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01);

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(requestBean.getTxnIdIw()).setClaimAt("AGENT").setStatus("CLAIM_AT_AGENT").setDirPly(true);
				int pwtInvId = WinningTransactionManagerIW.insertPwtInventory(invBean, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(requestBean.getGameId()).setTransType("IW_PWT_AUTO").setTransTime(transactionTime);
				transId = WinningTransactionManagerIW.insertAgentTransaction(transBean, connection);

				DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setPwtInvId(pwtInvId).setTransId(transId).setTransTime(transactionTime).setPlayerId(1).setWinningAmount(requestBean.getTotalAmount()).setPaymentType("CASH").setPwtClaimStatus("DONE_CLM").setAgentClaimComm(CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01)).setAgentNetAmt(agentNet);
				WinningTransactionManagerIW.insertAgentDirectPlrPwt(dirPwtBean, connection);
				
				WinningTransactionManagerIW.updateAgentPwtInv(requestBean.getGameId(), requestBean.getTicketNumber(), transId, connection);

				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet, "TRANSACTION", "IW_PWT", userBean.getUserOrgId(), 0, "AGENT", 0, connection);
				if (!isValid) {
					throw new LMSException(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			}

			connection.commit();

			if (IW.Status.NORMAL_PAY.equalsIgnoreCase(responseBean.getStatus())) {
				responseBean.setGameId(requestBean.getGameId());
				responseBean.setTicketNumber(requestBean.getTicketNumber());
				responseBean.setTxnId(String.valueOf(transId));
				responseBean.setDoneByUserId(userBean.getUserId());
				responseBean.setStatus("PAID");
			} else {
				responseBean.setGameId(requestBean.getGameId());
				responseBean.setTicketNumber(requestBean.getTicketNumber());
				responseBean.setStatus("Check PWT Status");
			}
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

			stmt = connection.createStatement();
			appReqStmt = connection.prepareStatement("INSERT INTO st_iw_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, approved_by_user_id, approved_by_type, approval_date, remarks, payment_done_by_user_id, payment_done_by_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

			int playerId = getPlayerIdOrRegister(playerBean, connection);

			String recIdForApp = GenerateRecieptNo.generateRequestIdIW("IWREQUEST", connection);
			logger.info("recIdForApp - " + recIdForApp);

			long taskId = 0L;

			appReqStmt.setString(1, recIdForApp);
			appReqStmt.setInt(2, playerId);
			appReqStmt.setString(3, "PLAYER");
			appReqStmt.setString(4, requestBean.getTicketNumber());
			appReqStmt.setInt(5, requestBean.getGameId());
			appReqStmt.setDouble(6, requestBean.getTotalAmount());
			appReqStmt.setDouble(7, 0.00);
			appReqStmt.setDouble(8, requestBean.getTotalAmount());
			appReqStmt.setString(9, "PAID");
			appReqStmt.setInt(10, userBean.getUserId());
			appReqStmt.setString(11, userBean.getUserType());
			appReqStmt.setString(12, transactionTime);
			appReqStmt.setInt(13, userBean.getUserId());
			appReqStmt.setString(14, userBean.getUserType());
			appReqStmt.setString(15, transactionTime);
			appReqStmt.setString(16, requestBean.getRemarks());
			appReqStmt.setInt(17, userBean.getUserId());
			appReqStmt.setString(18, userBean.getUserType());
			logger.info("Insert In st_iw_approval_req_master - " + appReqStmt);
			appReqStmt.executeUpdate();
			rs = appReqStmt.getGeneratedKeys();
			if (rs.next()) {
				taskId = rs.getInt(1);
			} else {
				throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
			}

			PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(requestBean.getTxnIdIw()).setClaimAt("AGENT").setStatus("CLAIM_AT_AGENT").setDirPly(true);
			int pwtInvId = WinningTransactionManagerIW.insertPwtInventory(invBean, connection);

			TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(playerId).setTransType("IW_PWT_PLR").setTransTime(transactionTime);
			long transId = WinningTransactionManagerIW.insertAgentTransaction( transBean, connection);

			DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setPwtInvId(pwtInvId).setTaskId(taskId).setTransId(transId).setTransTime(transactionTime).setPlayerId(playerId).setWinningAmount(requestBean.getTotalAmount()).setPaymentType("CASH").setPwtClaimStatus("CLAIM_BAL").setAgentClaimComm(0.00);
			WinningTransactionManagerIW.insertAgentDirectPlrPwt(dirPwtBean, connection);

			query = "UPDATE st_iw_approval_req_master SET transaction_id=" + transId + " WHERE task_id=" + taskId + ";";
			logger.info("UPDATE st_iw_approval_req_master - " + query);

			double agentComm = Util.getIWPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameId());
			double agentNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) - CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01);

			boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet, "TRANSACTION", "IW_PWT", userBean.getUserOrgId(), 0, "AGENT", 0, connection);
			if (!isValid) {
				throw new LMSException(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
			}

			stmt.executeUpdate(query);

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setTxnId(String.valueOf(transId));
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

		TPPwtResponseBean responseBean = new TPPwtResponseBean();
		long transId = 0L;
		double balance = 0L;
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			boolean isFraud = ResponsibleGaming.respGaming(userBean, "IW_PWT", String.valueOf(requestBean.getTotalAmount()), connection);
			if (isFraud) {
				throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			}

			double retailerComm = CommonFunctionsHelper.fetchIWCommOfOrganization(requestBean.getGameId(), userBean.getUserOrgId(), "PWT", "RETAILER", connection);
			double agentComm = CommonFunctionsHelper.fetchIWCommOfOrganization(requestBean.getGameId(), userBean.getParentOrgId(), "PWT", "AGENT", connection);

			double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("IW_HIGH_PRIZE_AMT"));
			double masApproveLimit = Double.parseDouble(Utility.getPropertyValue("IW_MAS_APPROVE_LIMIT"));

//			double retailerComm = Util.getIWPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameId());
//			double agentComm = Util.getIWPwtCommVariance(userBean.getParentOrgId(), requestBean.getGameId());

			if (masApproveLimit <= requestBean.getTotalAmount()) {
				responseBean.setStatus(IW.Status.MAS_APPROVAL_INIT);
			} else if (highPrizeAmt <= requestBean.getTotalAmount() && requestBean.getTotalAmount() < masApproveLimit) {
				responseBean.setStatus(IW.Status.HIGH_PRIZE);
			} else {
				responseBean.setStatus(IW.Status.NORMAL_PAY);
				double retailerNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) + CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * retailerComm * .01);
				double agentNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) + CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01);

				PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(requestBean.getTxnIdIw())).setClaimAt("RETAILER").setStatus("CLAIM_AT_RETAILER").setDirPly(false);
				int pwtInvId = WinningTransactionManagerIW.insertPwtInventory(invBean, connection);

				TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyId(requestBean.getGameId()).setTransType("IW_PWT").setTransTime(transactionTime);
				transId = WinningTransactionManagerIW.insertRetailerTransaction(transBean, connection);

				DirectPlrPwtBean pwtBean = new DirectPlrPwtBean().setTransId(transId).setPwtInvId(pwtInvId).setGameId(requestBean.getGameId()).setUserBean(userBean).setWinningAmount(requestBean.getTotalAmount()).setRetailerClaimComm(CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * retailerComm * .01)).setRetailerNetAmt(retailerNet).setAgentClaimComm(CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01)).setAgentNetAmt(agentNet).setTransTime(transactionTime).setPwtClaimStatus("DONE_CLM");
				WinningTransactionManagerIW.insertRetailerPwt(pwtBean, connection);

				WinningTransactionManagerIW.updateRetailerPwtInv(requestBean.getGameId(), requestBean.getTicketNumber(), transId, connection);

				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retailerNet, "TRANSACTION", "IW_PWT", userBean.getUserOrgId(), userBean.getParentOrgId(), "RETAILER", 0, connection);
				if (!isValid) {
					throw new LMSException(IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}

				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet, "TRANSACTION", "IW_PWT", userBean.getParentOrgId(), 0, "AGENT", 0, connection);
				if (!isValid) {
					throw new LMSException(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
				// Update Heart Beat After PWT Completion
				Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(), "IW_PWT", connection);

				connection.commit();
				
				new AjaxRequestHelper().getAvlblCreditAmt(userBean, connection);
				balance = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
			}

			if(IW.Status.NORMAL_PAY.equalsIgnoreCase(responseBean.getStatus())) {
				responseBean.setGameId(requestBean.getGameId());
				responseBean.setTicketNumber(requestBean.getTicketNumber());
				responseBean.setTxnId(String.valueOf(transId));
				responseBean.setDoneByUserId(userBean.getUserId());
				responseBean.setBalance(balance);
				responseBean.setStatus("PAID");
				responseBean.setAdvMessageMap(CommonMethodsControllerImpl.getInstance().getIWAdvMessages(userBean.getUserOrgId(), requestBean.getGameId(), "PWT"));
			} else {
				responseBean.setTicketNumber(requestBean.getTicketNumber());
				responseBean.setGameId(requestBean.getGameId());
				responseBean.setStatus("Check PWT Status");
			}
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
		try {
			String transactionTime = Util.getCurrentTimeString();
			PlayerBean playerBean = requestBean.getPlayerBean();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			int playerId = getPlayerIdOrRegister(playerBean, connection);

			String recIdForApp = GenerateRecieptNo.generateRequestIdIW("IWREQUEST", connection);
			logger.info("recIdForApp - " + recIdForApp);

			appReqStmt = connection.prepareStatement("INSERT INTO st_iw_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, remarks) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			int taskId = 0;

			appReqStmt.setString(1, recIdForApp);
			appReqStmt.setInt(2, playerId);
			appReqStmt.setString(3, "PLAYER");
			appReqStmt.setString(4, requestBean.getTicketNumber());
			appReqStmt.setInt(5, requestBean.getGameId());
			appReqStmt.setDouble(6, requestBean.getTotalAmount());
			appReqStmt.setDouble(7, requestBean.getTaxAmt());
			appReqStmt.setDouble(8, requestBean.getNetAmt());
			appReqStmt.setString(9, "PND_MAS");
			appReqStmt.setInt(10, userBean.getUserId());
			appReqStmt.setString(11, userBean.getUserType());
			appReqStmt.setString(12, transactionTime);
			appReqStmt.setString(13, (requestBean.getRemarks() == null) ? null : requestBean.getRemarks().replaceAll("\\+", " "));
			logger.info("Insert In st_iw_approval_req_master - " + appReqStmt);
			appReqStmt.executeUpdate();
			rs = appReqStmt.getGeneratedKeys();
			if (rs.next()) {
				taskId = rs.getInt(1);
			} else {
				throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
			}

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setResponseCode(0);
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setTxnId(String.valueOf(taskId));
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
		try {
			String transactionTime = Util.getCurrentTimeString();

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			int playerId = 000;

			appReqStmt = connection.prepareStatement("UPDATE st_iw_approval_req_master SET request_status=?, approved_by_user_id=?, approved_by_type=?, approval_date=?, remarks=?, payment_done_by_user_id=?, payment_done_by_type=?, transaction_id=? WHERE task_id=?;");

			PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(requestBean.getTxnIdIw()).setClaimAt("BO").setStatus("CLAIM_AT_BO").setDirPly(true);
			int pwtInvId = WinningTransactionManagerIW.insertPwtInventory(invBean, connection);

			TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyType("PLAYER").setPartyId(playerId).setTransType("IW_PWT_PLR").setTransTime(transactionTime);
			long transId = WinningTransactionManagerIW.insertBOTransaction(transBean, connection);

			DirectPlrPwtBean dirPwtBean = new DirectPlrPwtBean().setUserBean(userBean).setGameId(requestBean.getGameId()).setPwtInvId(pwtInvId).setTaskId(Long.parseLong(requestBean.getTxnIdIw())).setTransId(transId).setTransTime(transactionTime).setPlayerId(playerId).setTaxAmount(requestBean.getTaxAmt()).setNetAmount(requestBean.getNetAmt()).setWinningAmount(requestBean.getTotalAmount()) .setPaymentType("CASH");
			WinningTransactionManagerIW.insertBODirectPlrPwt(dirPwtBean, connection);

			appReqStmt.setString(1, "PAID");
			appReqStmt.setInt(2, userBean.getUserId());
			appReqStmt.setString(3, userBean.getUserType());
			appReqStmt.setString(4, transactionTime);
			appReqStmt.setString(5, requestBean.getRemarks());
			appReqStmt.setInt(6, userBean.getUserId());
			appReqStmt.setString(7, userBean.getUserType());
			appReqStmt.setLong(8, transId);
			appReqStmt.setLong(9, Long.valueOf(requestBean.getTxnIdIw()));
			logger.info("UPDATE st_sle_approval_req_master - " + appReqStmt);
			appReqStmt.executeUpdate();

			WinningTransactionManagerIW.updateBOPwtInv(requestBean.getGameId(), requestBean.getTicketNumber(), transId, connection);

			connection.commit();

			responseBean = new TPPwtResponseBean();
			responseBean.setGameId(requestBean.getGameId());
			responseBean.setTicketNumber(requestBean.getTicketNumber());
			responseBean.setTxnId(String.valueOf(transId));
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
			String firstName = (playerBean.getFirstName() == null) ? null : playerBean.getFirstName().replaceAll("\\+", " ");
			String lastName = (playerBean.getLastName() == null) ? null : playerBean.getLastName().replaceAll("\\+", " ");
			String idType = (playerBean.getIdType() == null) ? null : playerBean.getIdType().replaceAll("\\+", " ");
			String idNumber = (playerBean.getIdNumber() == null) ? null : playerBean.getIdNumber().replaceAll("\\+", " ");

			stmt = connection.createStatement();
			String query = "SELECT player_id FROM st_lms_player_master WHERE first_name='" + firstName + "' AND last_name='" + lastName + "' AND photo_id_type='" + idType + "' AND photo_id_nbr='" + idNumber + "';";
			logger.info("Get Player Basic Info - " + query);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				playerId = rs.getInt("player_id");
			} else {
				String emailId = (playerBean.getEmailId() == null) ? null : playerBean.getEmailId().replaceAll("\\+", " ");
				String phoneNo = (playerBean.getPhoneNumber() == null) ? null : playerBean.getPhoneNumber().replaceAll("\\+", " ");
				String address1 = (playerBean.getAddress1() == null) ? null : playerBean.getAddress1().replaceAll("\\+", " ");
				String address2 = (playerBean.getAddress2() == null) ? null : playerBean.getAddress2().replaceAll("\\+", " ");
				String city = (playerBean.getCity() == null) ? null : playerBean.getCity().replaceAll("\\+", " ");
				String state = (playerBean.getState() == null) ? null : playerBean.getState().replaceAll("\\+", " ");
				String country = (playerBean.getCountry() == null) ? null : playerBean.getCountry().replaceAll("\\+", " ");

				query = "INSERT INTO st_lms_player_master (first_name, last_name, email_id, phone_nbr, addr_line1, addr_line2, city, state_code, country_code, pin_code, photo_id_type, photo_id_nbr) VALUES (" + stmtParamSetter(firstName, lastName, emailId, phoneNo, address1, address2, city, state, country, "0", idType, idNumber) + ");";
				logger.info("Insert In st_lms_player_master - " + query);
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
		logger.info("Player ID - " + playerId);
		return playerId;
	}

	private static final String stmtParamSetter(String... params) {
		StringBuilder stmtBuilder = new StringBuilder();

		for (String param : params) {
			stmtBuilder.append("'" + param + "',");
		}
		stmtBuilder.deleteCharAt(stmtBuilder.length() - 1);

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
			if (requestBean.getRequestId() != null && requestBean.getRequestId().length() > 0)
				appender += " AND request_id='" + requestBean.getRequestId() + "'";
			if (requestBean.getFirstName() != null && requestBean.getFirstName().length() > 0)
				appender += " AND first_name='" + requestBean.getFirstName() + "'";
			if (requestBean.getLastName() != null && requestBean.getLastName().length() > 0) 
				appender += " AND last_name='" + requestBean.getLastName() + "'";

			query = "SELECT task_id, game_id, draw_id, ticket_nbr, pwt_amt, request_id, (SELECT NAME FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE um.user_id=11001) request_by, request_status, party_type, first_name, last_name, city, remarks FROM st_sle_approval_req_master arm INNER JOIN st_lms_player_master pm ON arm.party_id=pm.player_id WHERE request_status='" + requestType + "'" + appender + ";";
			stmt = connection.createStatement();
			logger.info("getMasterApprovalRequests - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				approvalBean = new BOMasterApprovalBean();
				approvalBean.setTaskId(rs.getInt("task_id"));
				approvalBean.setGameId(rs.getInt("game_id"));
				approvalBean.setDrawId(rs.getInt("draw_id"));
				approvalBean.setTicketNumber(rs.getString("ticket_nbr"));
				approvalBean.setWinningAmount(rs.getDouble("pwt_amt"));
				approvalBean.setRequestId(rs.getString("request_id"));
				approvalBean.setRequestFor(rs.getString("party_type"));
				approvalBean.setRequestedBy(rs.getString("request_by"));
				approvalBean.setRequestStatus(rs.getString("request_status"));
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
			logger.info("processMasterApproval - " + pstmt);
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

	public static Map<String, Integer> getIdDetails(String ticketNumber,
			Connection connection) throws LMSException {

		Map<String, Integer> map = new HashMap<String, Integer>() ;
		
		Statement statement = null ;
		ResultSet resultSet = null ;
		
		try{
			String data = "select user_id, organization_id, parent_user_id from st_lms_user_master where organization_id = (select retailer_org_id from st_iw_ret_sale where engine_tx_id = '"+ticketNumber+"');" ;
			statement = connection.createStatement();
			resultSet = statement.executeQuery(data);
			logger.info("Data Statement : " + statement) ;
			
			if(resultSet.next()){
				map.put("userId", resultSet.getInt("user_id"));
				map.put("orgId", resultSet.getInt("organization_id"));
				map.put("parentUserId", resultSet.getInt("parent_user_id"));
			}
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(statement, resultSet);
		}
		
		return map;
	}
}