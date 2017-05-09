package com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;

public class WinningTransactionManagerVS {
	
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

	public static int insertPwtInventory(PwtInvBean invBean, Connection connection) throws VSException {
		PreparedStatement pwtInvStmt = null;
		int pwtInvId = 0;
		ResultSet rs = null;
		try {
			pwtInvStmt = connection.prepareStatement("SELECT pwt_inv_id FROM st_vs_pwt_inv WHERE ticket_nbr = ? ");
			pwtInvStmt.setString(1, invBean.ticketNumber);
			rs = pwtInvStmt.executeQuery();
			if (rs.next()) {
				throw new VSException(VSErrors.DUP_PAYMENT_ERROR_CODE, VSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			}
			pwtInvStmt.clearParameters();

			pwtInvStmt = connection.prepareStatement("INSERT INTO st_vs_pwt_inv (game_id, ticket_nbr, pwt_amt, engine_transaction_id, claim_at, status, is_direct_plr) VALUES (?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			pwtInvStmt.setInt(1, invBean.gameId);
			pwtInvStmt.setString(2, invBean.ticketNumber);
			pwtInvStmt.setDouble(3, CommonMethods.fmtToTwoDecimal(invBean.winningAmount));
			pwtInvStmt.setString(4, invBean.engineTransactionId);
			pwtInvStmt.setString(5, invBean.claimAt);
			pwtInvStmt.setString(6, invBean.status);
			pwtInvStmt.setString(7, invBean.isDirPly ? "YES" : "NO");
			//System.out.println("Insert In st_vs_pwt_inv - " + pwtInvStmt);
			pwtInvStmt.executeUpdate();
			rs = pwtInvStmt.getGeneratedKeys();
			if (rs.next()) {
				pwtInvId = rs.getInt(1);
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pwtInvStmt, rs);
		}
		return pwtInvId;
	}

	public static void updateBOPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws VSException {
		PreparedStatement pwtInvStmt = null;
		int isUpdated = 0;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_vs_pwt_inv SET bo_transaction_id = ? WHERE game_id = ? AND ticket_nbr = ?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			//System.out.println("updateBOPwtInv - " + pwtInvStmt);
			isUpdated = pwtInvStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getErrorCode() == 1062) {
				throw new VSException(VSErrors.DUP_PAYMENT_ERROR_CODE, VSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public static void updateAgentPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws VSException {
		PreparedStatement pwtInvStmt = null;
		int isUpdated = 0;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_vs_pwt_inv SET agent_transaction_id = ? WHERE game_id = ? AND ticket_nbr = ?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			//System.out.println("updateAgentPwtInv - " + pwtInvStmt);
			isUpdated = pwtInvStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getErrorCode() == 1062) {
				throw new VSException(VSErrors.DUP_PAYMENT_ERROR_CODE, VSErrors.DUP_PAYMENT_ERROR_MESSAGE);
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pwtInvStmt);
		}
	}

	public static void updateRetailerPwtInv(int gameId, String ticketNumber, long transactionId, Connection connection) throws VSException {
		PreparedStatement pwtInvStmt = null;
		int isUpated = 0;
		try {
			pwtInvStmt = connection.prepareStatement("UPDATE st_vs_pwt_inv SET retailer_transaction_id=? WHERE game_id=? AND ticket_nbr=?;");
			pwtInvStmt.setLong(1, transactionId);
			pwtInvStmt.setInt(2, gameId);
			pwtInvStmt.setString(3, ticketNumber);
			//System.out.println("updateRetailerPwtInv - " + pwtInvStmt);
			isUpated = pwtInvStmt.executeUpdate();
			if(isUpated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pwtInvStmt);
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

	private static long insertMainTransaction(String userType, String serviceCode, String interfaceType, Connection connection) throws VSException {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		long transId = 0L;
		try {
			query = "INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES ('" + userType + "','" + serviceCode + "','" + interfaceType + "');";
			//System.out.println("Insert In st_lms_transaction_master - " + query);
			stmt = connection.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				transId = rs.getLong(1);
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return transId;
	}

	public static long insertBOTransaction(TransactionBean transBean, Connection connection) throws VSException {
		PreparedStatement boTransStmt = null;
		long mainTransId = 0L;
		int isUpdated = 0;
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
			//System.out.println("Insert In st_lms_bo_transaction_master - " + boTransStmt);
			isUpdated = boTransStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(boTransStmt);
		}
		return mainTransId;
	}
	
	

	public static long insertAgentTransaction(TransactionBean transBean, Connection connection) throws VSException {
		PreparedStatement agentTransStmt = null;
		int isUpdated = 0;
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
			//System.out.println("Insert In st_lms_agent_transaction_master - " + agentTransStmt);
			isUpdated = agentTransStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(agentTransStmt);
		}
		return mainTransId;
	}

	public static long insertRetailerTransaction(TransactionBean transBean, Connection connection) throws VSException {
		PreparedStatement retTransStmt = null;
		long mainTransId = 0L;
		int isUpdated = 0;
		try {
			mainTransId = insertMainTransaction(transBean.userBean.getUserType(), transBean.serviceCode, transBean.interfaceType, connection);
			retTransStmt = connection.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id, retailer_user_id, retailer_org_id, game_id, transaction_date, transaction_type) VALUES (?,?,?,?,?,?);");
			retTransStmt.setLong(1, mainTransId);
			retTransStmt.setInt(2, transBean.userBean.getUserId());
			retTransStmt.setInt(3, transBean.userBean.getUserOrgId());
			retTransStmt.setInt(4, transBean.partyId);
			retTransStmt.setString(5, transBean.transTime);
			retTransStmt.setString(6, transBean.transType);
		//	System.out.println("Insert In st_lms_retailer_transaction_master - " + retTransStmt);
			isUpdated = retTransStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException ve) {
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(retTransStmt);
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
		private String ticketNumber;
		private double retailerClaimComm;
		private double retailerNetAmt;
		private double agentClaimComm;
		private double agentNetAmt;
		private double govtClaimComm;

		public double getGovtClaimComm() {
			return govtClaimComm;
		}

		public DirectPlrPwtBean setGovtClaimComm(double govtClaimComm) {
			this.govtClaimComm = govtClaimComm;
			return this;
		}

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

		public DirectPlrPwtBean setTicketNumber(String ticketNumber) {
			this.ticketNumber = ticketNumber;
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

	public static void insertBODirectPlrPwt(DirectPlrPwtBean pwtBean, Connection connection) throws VSException {
		PreparedStatement boPwtStmt = null;
		int isUpdated = 0;
		try {
			boPwtStmt = connection.prepareStatement("INSERT INTO st_vs_bo_direct_plr_pwt (bo_org_id, bo_user_id, game_id, pwt_inv_id, task_id, transaction_id, transaction_date, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
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
			System.out.println("Insert In st_vs_bo_direct_plr_pwt - " + boPwtStmt);
			isUpdated = boPwtStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(boPwtStmt);
		}
	}

	public static void insertAgentDirectPlrPwt(DirectPlrPwtBean pwtBean, Connection connection) throws VSException {
		PreparedStatement agentPwtStmt = null;
		int isUpdated = 0;
		try {
			agentPwtStmt = connection.prepareStatement("INSERT INTO st_vs_agent_direct_plr_pwt (agent_org_id, agent_user_id, game_id, pwt_inv_id, task_id, transaction_id, transaction_date, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name, pwt_claim_status, agt_claim_comm) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
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
			System.out.println("Insert In st_vs_agent_direct_plr_pwt - " + agentPwtStmt);
			isUpdated = agentPwtStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(agentPwtStmt);
		}
	}

	public static void insertRetailerPwt(DirectPlrPwtBean pwtBean, Connection connection) throws VSException {
		PreparedStatement retailerPwtStmt = null;
		int isUpdated = 0;
		try {
			retailerPwtStmt = connection.prepareStatement("INSERT INTO st_vs_ret_pwt (transaction_id, ticket_nbr, pwt_inv_id, game_id, retailer_org_id, retailer_user_id, pwt_amt, retailer_claim_comm, retailer_net_amt, agt_claim_comm, agent_net_amt, govt_claim_comm, transaction_date, pwt_claim_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			retailerPwtStmt.setLong(1, pwtBean.transId);
			retailerPwtStmt.setString(2, pwtBean.ticketNumber);
			retailerPwtStmt.setInt(3, pwtBean.pwtInvId);
			retailerPwtStmt.setInt(4, pwtBean.gameId);
			retailerPwtStmt.setInt(5, pwtBean.userBean.getUserOrgId());
			retailerPwtStmt.setInt(6, pwtBean.userBean.getUserId());
			retailerPwtStmt.setDouble(7, pwtBean.winningAmount);
			retailerPwtStmt.setDouble(8, pwtBean.retailerClaimComm);
			retailerPwtStmt.setDouble(9, pwtBean.retailerNetAmt);
			retailerPwtStmt.setDouble(10, pwtBean.agentClaimComm);
			retailerPwtStmt.setDouble(11, pwtBean.agentNetAmt);
			retailerPwtStmt.setDouble(12, pwtBean.govtClaimComm);
			retailerPwtStmt.setString(13, pwtBean.transTime);
			retailerPwtStmt.setString(14, pwtBean.pwtClaimStatus);
			//System.out.println("Insert In st_vs_ret_pwt - " + retailerPwtStmt);
			isUpdated = retailerPwtStmt.executeUpdate();
			if(isUpdated == 0){
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(VSException ve){
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(retailerPwtStmt);
		}
	}
	
}
