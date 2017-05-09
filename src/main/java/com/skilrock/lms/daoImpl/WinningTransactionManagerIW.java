package com.skilrock.lms.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.instantWin.common.IWErrors;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;

class WinningTransactionManagerIW {
	// private static Logger logger = LoggerFactory.getLogger(WinningTransactionManagerSLE.class);

	static class PwtInvBean {
		private int gameId;
		private double winningAmount;
		private String ticketNumber;
		private String engineTransactionId;
		private String claimAt;
		private String status;
		private boolean isDirPly;

		public PwtInvBean setGameId(int gameId) {
			this.gameId = gameId;
			return this;
		}

		public PwtInvBean setWinningAmount(double winningAmount) {
			this.winningAmount = winningAmount;
			return this;
		}

		public PwtInvBean setTicketNumber(String ticketNumber) {
			this.ticketNumber = ticketNumber;
			return this;
		}

		public PwtInvBean setEngineTransactionId(String engineTransactionId) {
			this.engineTransactionId = engineTransactionId;
			return this;
		}

		public PwtInvBean setClaimAt(String claimAt) {
			this.claimAt = claimAt;
			return this;
		}

		public PwtInvBean setStatus(String status) {
			this.status = status;
			return this;
		}

		public PwtInvBean setDirPly(boolean isDirPly) {
			this.isDirPly = isDirPly;
			return this;
		}
	}

	public static int insertPwtInventory(PwtInvBean invBean, Connection connection) throws LMSException {
		PreparedStatement pwtInvStmt = null;
		int pwtInvId = 0;
		ResultSet rs = null;
		try {
			// Duplicacy check for transaction
			pwtInvStmt = connection.prepareStatement("SELECT pwt_inv_id FROM st_iw_pwt_inv WHERE ticket_nbr = ? ");
			pwtInvStmt.setString(1, invBean.ticketNumber);
			rs = pwtInvStmt.executeQuery();
			if (rs.next()) {
				throw new LMSException(LMSErrors.DUP_PAYMENT_ERROR_CODE, LMSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			}
			pwtInvStmt.clearParameters();

			pwtInvStmt = connection.prepareStatement("INSERT INTO st_iw_pwt_inv (game_id, ticket_nbr, pwt_amt, engine_transaction_id, claim_at, status, is_direct_plr) VALUES (?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			pwtInvStmt.setInt(1, invBean.gameId);
			pwtInvStmt.setString(2, invBean.ticketNumber);
			pwtInvStmt.setDouble(3, CommonMethods.fmtToTwoDecimal(invBean.winningAmount));
			pwtInvStmt.setString(4, invBean.engineTransactionId);
			pwtInvStmt.setString(5, invBean.claimAt);
			pwtInvStmt.setString(6, invBean.status);
			pwtInvStmt.setString(7, invBean.isDirPly ? "YES" : "NO");
			System.out.println("Insert In st_iw_pwt_inv - " + pwtInvStmt);
			pwtInvStmt.executeUpdate();
			rs = pwtInvStmt.getGeneratedKeys();
			if (rs.next()) {
				pwtInvId = rs.getInt(1);
			} else {
				throw new LMSException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
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
			DBConnect.closeConnection(pwtInvStmt, rs);
		}
		return pwtInvId;
	}

	public static void updateBOPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws LMSException {
		PreparedStatement pwtInvStmt = null;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_iw_pwt_inv SET bo_transaction_id = ? WHERE game_id = ? AND ticket_nbr = ?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			System.out.println("updateBOPwtInv - " + pwtInvStmt);
			pwtInvStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getErrorCode() == 1062) {
				throw new LMSException(LMSErrors.DUP_PAYMENT_ERROR_CODE, LMSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			} else {
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public static void updateAgentPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws LMSException {
		PreparedStatement pwtInvStmt = null;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_iw_pwt_inv SET agent_transaction_id = ? WHERE game_id = ? AND ticket_nbr = ?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			System.out.println("updateAgentPwtInv - " + pwtInvStmt);
			pwtInvStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getErrorCode() == 1062) {
				throw new LMSException(LMSErrors.DUP_PAYMENT_ERROR_CODE, LMSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			} else {
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void updateRetailerPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws LMSException {
		PreparedStatement pwtInvStmt = null;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_iw_pwt_inv SET retailer_transaction_id=? WHERE game_id=? AND ticket_nbr=?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			System.out.println("updateRetailerPwtInv - " + pwtInvStmt);
			pwtInvStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	static class TransactionBean {
		private UserInfoBean userBean;
		private String serviceCode;
		private String interfaceType;
		private String partyType;
		private int partyId;
		private String transType;
		private String transTime;

		public TransactionBean setUserBean(UserInfoBean userBean) {
			this.userBean = userBean;
			return this;
		}

		public TransactionBean setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
			return this;
		}

		public TransactionBean setInterfaceType(String interfaceType) {
			this.interfaceType = interfaceType;
			return this;
		}

		public TransactionBean setPartyType(String partyType) {
			this.partyType = partyType;
			return this;
		}

		public TransactionBean setPartyId(int partyId) {
			this.partyId = partyId;
			return this;
		}

		public TransactionBean setTransType(String transType) {
			this.transType = transType;
			return this;
		}

		public TransactionBean setTransTime(String transTime) {
			this.transTime = transTime;
			return this;
		}
	}

	private static long insertMainTransaction(String userType, String serviceCode, String interfaceType, Connection connection) throws LMSException {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		long transId = 0L;
		try {
			query = "INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES ('" + userType + "','" + serviceCode + "','" + interfaceType + "');";
			System.out.println("Insert In st_lms_transaction_master - " + query);
			stmt = connection.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				transId = rs.getLong(1);
			} else {
				throw new LMSException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return transId;
	}

	public static long insertBOTransaction(TransactionBean transBean, Connection connection) throws LMSException {
		PreparedStatement boTransStmt = null;
		long mainTransId = 0L;
		try {
			mainTransId = insertMainTransaction(transBean.userBean.getUserType(), transBean.serviceCode, transBean.interfaceType, connection);

			boTransStmt = connection.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id, user_id, user_org_id, party_type, party_id, transaction_date, transaction_type) VALUES (?,?,?,?,?,?,?);");
			boTransStmt.setLong(1, mainTransId);
			boTransStmt.setInt(2, transBean.userBean.getUserId());
			boTransStmt.setInt(3, transBean.userBean.getUserOrgId());
			boTransStmt.setString(4, transBean.partyType);
			boTransStmt.setInt(5, transBean.partyId);
			boTransStmt.setString(6, transBean.transTime);
			boTransStmt.setString(7, transBean.transType);
			System.out.println("Insert In st_lms_bo_transaction_master - " + boTransStmt);
			boTransStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return mainTransId;
	}
	
	public static long insertPWTApproval(UserInfoBean userBean, TPPwtRequestBean requestBean, String serviceCode, String interfaceType, long transId, String transactionTime, Connection connection) {
		PreparedStatement appReqStmt = null;
		ResultSet rs = null;
		long taskId = 0L;
		try {
			appReqStmt = connection.prepareStatement("INSERT INTO st_iw_approval_req_master (request_id, party_id, party_type, ticket_nbr, game_id, pwt_amt, tax_amt, net_amt, request_status, requested_by_user_id, requester_by_type, request_date, approved_by_user_id, approved_by_type, approval_date, remarks, payment_done_by_user_id, payment_done_by_type, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

			String recIdForApp = GenerateRecieptNo.generateRequestIdIW("IWREQUEST", connection);

			appReqStmt.setString(1, recIdForApp);
			appReqStmt.setInt(2, requestBean.getPlayerId());
			appReqStmt.setString(3, "PLAYER");
			appReqStmt.setString(4, requestBean.getTicketNumber());
			appReqStmt.setInt(5, requestBean.getGameId());
			appReqStmt.setDouble(6, requestBean.getTotalAmount());
			appReqStmt.setDouble(7, requestBean.getTaxAmt());
			appReqStmt.setDouble(8, requestBean.getNetAmt());
			appReqStmt.setString(9, "PAID");
			appReqStmt.setInt(10, userBean.getUserId());
			appReqStmt.setString(11, userBean.getUserType());
			appReqStmt.setString(12, transactionTime);
			appReqStmt.setInt(13, userBean.getUserId());
			appReqStmt.setString(14, userBean.getUserType());
			appReqStmt.setString(15, transactionTime);
			appReqStmt.setString(16, (requestBean.getRemarks() == null) ? null : requestBean.getRemarks().replaceAll("\\+", " "));
			appReqStmt.setInt(17, userBean.getUserId());
			appReqStmt.setString(18, userBean.getUserType());
			appReqStmt.setLong(19, transId);
			appReqStmt.executeUpdate();
			rs = appReqStmt.getGeneratedKeys();
			if (rs.next()) {
				taskId = rs.getInt(1);
			} else {
				throw new LMSException(LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_CODE, LMSErrors.APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskId;
	}

	public static long insertAgentTransaction(TransactionBean transBean, Connection connection) throws LMSException {
		PreparedStatement agentTransStmt = null;

		long mainTransId = 0L;
		try {
			mainTransId = insertMainTransaction(transBean.userBean.getUserType(), transBean.serviceCode, transBean.interfaceType, connection);

			agentTransStmt = connection.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id, user_id, user_org_id, party_type, party_id, transaction_type, transaction_date) VALUES (?,?,?,?,?,?,?);");
			agentTransStmt.setLong(1, mainTransId);
			agentTransStmt.setInt(2, transBean.userBean.getUserId());
			agentTransStmt.setInt(3, transBean.userBean.getUserOrgId());
			agentTransStmt.setString(4, transBean.partyType);
			agentTransStmt.setInt(5, transBean.partyId);
			agentTransStmt.setString(6, transBean.transType);
			agentTransStmt.setString(7, transBean.transTime);
			System.out.println("Insert In st_lms_agent_transaction_master - " + agentTransStmt);
			agentTransStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return mainTransId;
	}

	public static long insertRetailerTransaction(TransactionBean transBean, Connection connection) throws LMSException {
		PreparedStatement retTransStmt = null;
		long mainTransId = 0L;
		try {
			mainTransId = insertMainTransaction(transBean.userBean.getUserType(), transBean.serviceCode, transBean.interfaceType, connection);

			retTransStmt = connection.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id, retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type) VALUES (?,?,?,?,?,?);");
			retTransStmt.setLong(1, mainTransId);
			retTransStmt.setInt(2, transBean.userBean.getUserId());
			retTransStmt.setInt(3, transBean.userBean.getUserOrgId());
			retTransStmt.setInt(4, transBean.partyId);
			retTransStmt.setString(5, transBean.transTime);
			retTransStmt.setString(6, transBean.transType);
			System.out.println("Insert In st_lms_retailer_transaction_master - " + retTransStmt);
			retTransStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return mainTransId;
	}

	static class DirectPlrPwtBean {
		private UserInfoBean userBean;
		private int gameId;
		private int pwtInvId;
		private long taskId;
		private long transId;
		private String transTime;
		private int playerId;
		private double taxAmount;
		private double netAmount;
		private double winningAmount;
		private String paymentType;
		private String chequeNumber;
		private String chequeDate;
		private String draweeBank;
		private String issuePartyName;
		private String pwtClaimStatus;
		private double retailerClaimComm;
		private double retailerNetAmt;
		private double agentClaimComm;
		private double agentNetAmt;

		public DirectPlrPwtBean setUserBean(UserInfoBean userBean) {
			this.userBean = userBean;
			return this;
		}

		public DirectPlrPwtBean setGameId(int gameId) {
			this.gameId = gameId;
			return this;
		}

		public DirectPlrPwtBean setPwtInvId(int pwtInvId) {
			this.pwtInvId = pwtInvId;
			return this;
		}

		public DirectPlrPwtBean setTaskId(long taskId) {
			this.taskId = taskId;
			return this;
		}

		public DirectPlrPwtBean setTransId(long transId) {
			this.transId = transId;
			return this;
		}

		public DirectPlrPwtBean setTransTime(String transTime) {
			this.transTime = transTime;
			return this;
		}

		public DirectPlrPwtBean setPlayerId(int playerId) {
			this.playerId = playerId;
			return this;
		}

		public DirectPlrPwtBean setTaxAmount(double taxAmount) {
			this.taxAmount = taxAmount;
			return this;
		}

		public DirectPlrPwtBean setNetAmount(double netAmount) {
			this.netAmount = netAmount;
			return this;
		}

		public DirectPlrPwtBean setWinningAmount(double winningAmount) {
			this.winningAmount = winningAmount;
			return this;
		}

		public DirectPlrPwtBean setPaymentType(String paymentType) {
			this.paymentType = paymentType;
			return this;
		}

		public DirectPlrPwtBean setChequeNumber(String chequeNumber) {
			this.chequeNumber = chequeNumber;
			return this;
		}

		public DirectPlrPwtBean setChequeDate(String chequeDate) {
			this.chequeDate = chequeDate;
			return this;
		}

		public DirectPlrPwtBean setDraweeBank(String draweeBank) {
			this.draweeBank = draweeBank;
			return this;
		}

		public DirectPlrPwtBean setIssuePartyName(String issuePartyName) {
			this.issuePartyName = issuePartyName;
			return this;
		}

		public DirectPlrPwtBean setPwtClaimStatus(String pwtClaimStatus) {
			this.pwtClaimStatus = pwtClaimStatus;
			return this;
		}

		public DirectPlrPwtBean setRetailerClaimComm(double retailerClaimComm) {
			this.retailerClaimComm = retailerClaimComm;
			return this;
		}

		public DirectPlrPwtBean setRetailerNetAmt(double retailerNetAmt) {
			this.retailerNetAmt = retailerNetAmt;
			return this;
		}

		public DirectPlrPwtBean setAgentClaimComm(double agentClaimComm) {
			this.agentClaimComm = agentClaimComm;
			return this;
		}

		public DirectPlrPwtBean setAgentNetAmt(double agentNetAmt) {
			this.agentNetAmt = agentNetAmt;
			return this;
		}
	}

	public static void insertBODirectPlrPwt(DirectPlrPwtBean pwtBean, Connection connection) throws LMSException {
		PreparedStatement boPwtStmt = null;
		try {
			boPwtStmt = connection.prepareStatement("INSERT INTO st_iw_bo_direct_plr_pwt (bo_org_id, bo_user_id, game_id, pwt_inv_id, task_id, transaction_id, transaction_date, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			boPwtStmt.setInt(1, pwtBean.userBean.getUserOrgId());
			boPwtStmt.setInt(2, pwtBean.userBean.getUserId());
			boPwtStmt.setInt(3, pwtBean.gameId);
			boPwtStmt.setInt(4, pwtBean.pwtInvId);
			boPwtStmt.setLong(5, pwtBean.taskId);
			boPwtStmt.setLong(6, pwtBean.transId);
			boPwtStmt.setString(7, pwtBean.transTime);
			boPwtStmt.setInt(8, pwtBean.playerId);
			boPwtStmt.setDouble(9, pwtBean.winningAmount);
			boPwtStmt.setDouble(10, pwtBean.taxAmount);
			boPwtStmt.setDouble(11, pwtBean.netAmount);
			boPwtStmt.setString(12, pwtBean.paymentType);
			boPwtStmt.setString(13, pwtBean.chequeNumber);
			boPwtStmt.setString(14, pwtBean.chequeDate);
			boPwtStmt.setString(15, pwtBean.draweeBank);
			boPwtStmt.setString(16, pwtBean.issuePartyName);
			System.out.println("Insert In st_iw_bo_direct_plr_pwt - " + boPwtStmt);
			boPwtStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void insertAgentDirectPlrPwt(DirectPlrPwtBean pwtBean, Connection connection) throws LMSException {
		PreparedStatement agentPwtStmt = null;
		try {
			agentPwtStmt = connection.prepareStatement("INSERT INTO st_iw_agent_direct_plr_pwt (agent_org_id, agent_user_id, game_id, pwt_inv_id, task_id, transaction_id, transaction_date, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name, pwt_claim_status, agt_claim_comm) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			agentPwtStmt.setInt(1, pwtBean.userBean.getUserOrgId());
			agentPwtStmt.setInt(2, pwtBean.userBean.getUserId());
			agentPwtStmt.setInt(3, pwtBean.gameId);
			agentPwtStmt.setInt(4, pwtBean.pwtInvId);
			agentPwtStmt.setLong(5, pwtBean.taskId);
			agentPwtStmt.setLong(6, pwtBean.transId);
			agentPwtStmt.setString(7, pwtBean.transTime);
			agentPwtStmt.setInt(8, pwtBean.playerId);
			agentPwtStmt.setDouble(9, pwtBean.winningAmount);
			agentPwtStmt.setDouble(10, 0.00);
			agentPwtStmt.setDouble(11, pwtBean.agentNetAmt);
			agentPwtStmt.setString(12, pwtBean.paymentType);
			agentPwtStmt.setString(13, pwtBean.chequeNumber);
			agentPwtStmt.setString(14, pwtBean.chequeDate);
			agentPwtStmt.setString(15, pwtBean.draweeBank);
			agentPwtStmt.setString(16, pwtBean.issuePartyName);
			agentPwtStmt.setString(17, pwtBean.pwtClaimStatus);
			agentPwtStmt.setDouble(18, pwtBean.agentClaimComm);
			System.out.println("Insert In st_iw_agent_direct_plr_pwt - " + agentPwtStmt);
			agentPwtStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void insertRetailerPwt(DirectPlrPwtBean pwtBean, Connection connection) throws LMSException {
		PreparedStatement retailerPwtStmt = null;
		try {
			retailerPwtStmt = connection.prepareStatement("INSERT INTO st_iw_ret_pwt (transaction_id, pwt_inv_id, game_id, retailer_org_id, retailer_user_id, pwt_amt, retailer_claim_comm, retailer_net_amt, agt_claim_comm, agent_net_amt, transaction_date, pwt_claim_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
			retailerPwtStmt.setLong(1, pwtBean.transId);
			retailerPwtStmt.setInt(2, pwtBean.pwtInvId);
			retailerPwtStmt.setInt(3, pwtBean.gameId);
			retailerPwtStmt.setInt(4, pwtBean.userBean.getUserOrgId());
			retailerPwtStmt.setInt(5, pwtBean.userBean.getUserId());
			retailerPwtStmt.setDouble(6, pwtBean.winningAmount);
			retailerPwtStmt.setDouble(7, pwtBean.retailerClaimComm);
			retailerPwtStmt.setDouble(8, pwtBean.retailerNetAmt);
			retailerPwtStmt.setDouble(9, pwtBean.agentClaimComm);
			retailerPwtStmt.setDouble(10, pwtBean.agentNetAmt);
			retailerPwtStmt.setString(11, pwtBean.transTime);
			retailerPwtStmt.setString(12, pwtBean.pwtClaimStatus);
			System.out.println("Insert In st_iw_ret_pwt - " + retailerPwtStmt);
			retailerPwtStmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
}