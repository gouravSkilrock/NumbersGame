package com.skilrock.lms.rest.scratchService.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.ScratchPayPWTBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchTicketVirnData;

public class ScratchWinningDaoImpl {

	private static final String AGENT = "AGENT";
	private static final String API = "API";
	private static final String SE = "SE";
	private static final String RETAILER = "RETAILER";
	private static final String CLAIM_PLR_RET_UNCLM = "CLAIM_PLR_RET_UNCLM";
	private static final String CLAIM_PLR_RET_CLM = "CLAIM_PLR_RET_CLM";
	private static final String ACTIVE = "ACTIVE";
	private static final String CLAIMED = "CLAIMED";
	private static final String CREDIT = "CREDIT";
	private static final String UNCLAIM_BAL = "UNCLAIM_BAL";
	private static final String DEBIT = "DEBIT";
	private static final String CLAIM_BAL = "CLAIM_BAL";
	private static Logger logger = LoggerFactory.getLogger(ScratchWinningDaoImpl.class);
	private int gameId;
	private double pwtAmt;
	private boolean isAutoScrap;
	private int transactionId;
	private double agentCommission;
	private double retailerCommission;
	private String pwtStatus;
	
	public ScratchTicketVirnData verifyAndGetTicketVirnData(String virnCode, ScratchGameDataBean gameDataBean,
			Connection connection, TicketBean tktBean) throws LMSException {

		int gameId = gameDataBean.getGameId();
		int gameNbr = gameDataBean.getGameNbr();
		ScratchTicketVirnData dataBean = new ScratchTicketVirnData();

		String encodedVirnCode = MD5Encoder.encode(virnCode);
		logger.info("Encoded virn == " + encodedVirnCode);

		String encodedTktNo = MD5Encoder.encode(tktBean.getTicketNumber());
		logger.info("Encoded Ticket Number == " + encodedTktNo);
		try {
			Statement statement = connection.createStatement();
			StringBuilder query = new StringBuilder();
			query.append(QueryManager.getST1PWTBOCheckQuery()).append(" st_se_pwt_inv_" + gameNbr + " where ");
			query.append(" game_id = " + gameId).append(" and virn_code='");
			query.append(encodedVirnCode).append("'").append(" and id1='");
			query.append(encodedTktNo).append("'").append(" and ticket_status in ('SOLD')");

			logger.info("GameId:" + gameId + "\nQuery:: " + query);

			ResultSet resultSet = statement.executeQuery(query.toString());

			if (resultSet.next()) {
				String vCode = resultSet.getString(TableConstants.SPI_VIRN_CODE);
				String pwtAmount = resultSet.getString(TableConstants.SPI_PWT_AMT);
				String prizeLevel = resultSet.getString(TableConstants.SPI_PRIZE_LEVEL);
				String prizeStatus = resultSet.getString("status");
				logger.info("Vcode : " + vCode + "\nPWT Amt : " + pwtAmount + "\nPrize level : " + prizeLevel
						+ "\nstatus : " + prizeStatus);

				if (MD5Encoder.encode(virnCode).equals(vCode)) {
					dataBean.setPrizeLevel(prizeLevel);
					dataBean.setPrizeStatus(prizeStatus);
					dataBean.setvCode(vCode);
					dataBean.setPwtAmount(pwtAmount);
				} else {
					throw new LMSException(ScratchErrors.VIRN_NUMBER_INVALID_ERROR_CODE,
							ScratchErrors.VIRN_NUMBER_INVALID_ERROR_MESSAGE);
				}
			} else {
				throw new LMSException(ScratchErrors.VIRN_NUMBER_INVALID_ERROR_CODE,
						ScratchErrors.VIRN_NUMBER_INVALID_ERROR_MESSAGE);
			}

		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			logger.error("SQL EXception", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("EXception", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return dataBean;

	}
	
	public void payPWTProcess(ScratchPayPWTBean scratchPayPWTBean, Connection connection) throws LMSException{
		try{
			gameId = scratchPayPWTBean.getTicketBean().getTicketGameId();
			pwtAmt = Double.parseDouble(scratchPayPWTBean.getPwtBean().getPwtAmount());
			isAutoScrap = "YES".equalsIgnoreCase(scratchPayPWTBean.getOrgPwtLimitBean().getIsPwtAutoScrap()) && pwtAmt <= scratchPayPWTBean.getOrgPwtLimitBean().getScrapLimit();
			if (pwtAmt <= scratchPayPWTBean.getOrgPwtLimitBean().getPayLimit()) {
				payPWTIfPayLimitNotExceeded(scratchPayPWTBean, connection);
			} else {
				throw new LMSException(ScratchErrors.LIMIT_EXCEEDED_ERROR_CODE,ScratchErrors.LIMIT_EXCEEDED_ERROR_MESSAGE);
			}
		} catch(LMSException e){
			throw e;
		} catch(SQLException e){
			logger.error("SQLException Occurred :", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(Exception e){
			logger.error("Exception Occurred :", e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	private void payPWTIfPayLimitNotExceeded(ScratchPayPWTBean scratchPayPWTBean, Connection connection) throws SQLException, LMSException {
		PreparedStatement pstmt = insertInLMSTransactionMaster(connection);
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
			payPWTIfTransactionInsertedInLMSTransactionMaster(scratchPayPWTBean, connection, rs);
		} else {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private PreparedStatement insertInLMSTransactionMaster(Connection connection) throws LMSException {
		String transMasQuery = null;
		PreparedStatement pstmt = null;
		try{
			transMasQuery = "INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)";
			pstmt = connection.prepareStatement(transMasQuery);
			pstmt.setString(1, RETAILER);
			pstmt.setString(2, SE);
			pstmt.setString(3, API);
			pstmt.executeUpdate();
		}catch(SQLException e){
			logger.error("SQLException occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return pstmt;
	}

	private void payPWTIfTransactionInsertedInLMSTransactionMaster(ScratchPayPWTBean scratchPayPWTBean, Connection connection, ResultSet rs) throws SQLException, LMSException {
		CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
		transactionId = rs.getInt(1);
		insertInLMSRetailerTransactionMaster(scratchPayPWTBean, connection);
		fetchAgentAndRetailerCommission(scratchPayPWTBean, connection, commHelper);
		insertInScratchRetailerPWT(scratchPayPWTBean, connection, transactionId);
		if (scratchPayPWTBean.getPendingReqId() != null && !scratchPayPWTBean.isAnonymous()) {
			updateScratchPWTApproval(scratchPayPWTBean, connection);
		}
		pwtStatus = isAutoScrap ? CLAIM_PLR_RET_CLM: CLAIM_PLR_RET_UNCLM;
		updateGameInformation(scratchPayPWTBean, connection, commHelper);
		updateOrganizationBalance(scratchPayPWTBean, connection);
	}

	private void insertInLMSRetailerTransactionMaster(ScratchPayPWTBean scratchPayPWTBean,Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String retTransMasterQuery = null;
		try{
			retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id , transaction_date , transaction_type,game_id ) values (?,?,?,?,?,?)";
			pstmt = connection.prepareStatement(retTransMasterQuery);
			pstmt.setInt(1, transactionId);
			pstmt.setInt(2, scratchPayPWTBean.getUserInfoBean().getUserId());
			pstmt.setInt(3, scratchPayPWTBean.getUserInfoBean().getUserOrgId());
			pstmt.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
			pstmt.setString(5, "PWT");
			pstmt.setInt(6, gameId);
			pstmt.executeUpdate();
		}catch(SQLException e){
			logger.error("SQLException occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private void fetchAgentAndRetailerCommission(ScratchPayPWTBean scratchPayPWTBean, Connection connection, CommonFunctionsHelper commHelper) throws SQLException {
		retailerCommission = commHelper.fetchCommOfOrganization(gameId,	scratchPayPWTBean.getUserInfoBean().getUserOrgId(), "PWT", RETAILER, connection);
		agentCommission = commHelper.fetchCommOfOrganization(gameId, scratchPayPWTBean.getUserInfoBean().getParentOrgId(), "PWT", AGENT, connection);
	}

	private void insertInScratchRetailerPWT(ScratchPayPWTBean scratchPayPWTBean, Connection connection, int transactionId) throws LMSException {
		PreparedStatement pstmt = null;
		String retPwtEntry = null;
		try{
			retPwtEntry = "insert into  st_se_retailer_pwt ( retailer_user_id , retailer_org_id , virn_code , ticket_nbr , game_id , transaction_id , pwt_amt , claim_comm , agt_claim_comm, status ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pwtStatus = isAutoScrap ? CLAIM_BAL : UNCLAIM_BAL;
			pstmt = connection.prepareStatement(retPwtEntry);
			pstmt.setInt(1, scratchPayPWTBean.getUserInfoBean().getUserId());
			pstmt.setInt(2, scratchPayPWTBean.getUserInfoBean().getUserOrgId());
			pstmt.setString(3, scratchPayPWTBean.getPwtBean().getEncVirnCode());
			pstmt.setString(4, scratchPayPWTBean.getTicketBean().getTicketNumber());
			pstmt.setInt(5, gameId);
			pstmt.setInt(6, transactionId);
			pstmt.setDouble(7, pwtAmt);
			pstmt.setDouble(8, retailerCommission);
			pstmt.setDouble(9, agentCommission);
			pstmt.setString(10, pwtStatus);
			pstmt.executeUpdate();
		}catch(SQLException e){
			logger.error("SQLException occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private void updateScratchPWTApproval(ScratchPayPWTBean scratchPayPWTBean, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String updateAppTable = null;
		try{
			updateAppTable = "update  st_se_pwt_approval_request_master  set req_status ='DONE', remarks ='Payment Done', payment_done_by_type =?, payment_done_by =? where  task_id = ?";
			pstmt = connection.prepareStatement(updateAppTable);
			pstmt.setString(1, scratchPayPWTBean.getUserInfoBean().getUserType());
			pstmt.setInt(2, scratchPayPWTBean.getUserInfoBean().getUserOrgId());
			pstmt.setInt(3, Integer.parseInt(scratchPayPWTBean.getPendingReqId()));
			int i = pstmt.executeUpdate();
			if (i < 1) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch(LMSException e){
			throw e;
		} catch(SQLException e){
			logger.error("SQLException occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private void updateGameInformation(ScratchPayPWTBean scratchPayPWTBean, Connection connection, CommonFunctionsHelper commHelper) throws SQLException, LMSException {
		GameUtilityHelper.updateNoOfPrizeRemNewZim(gameId, pwtAmt, pwtStatus,scratchPayPWTBean.getPwtBean().getEncVirnCode(), connection, scratchPayPWTBean.getTicketBean().getGameNbr(),scratchPayPWTBean.getPwtBean().getEnctktNumber());
		commHelper.updateVirnStatus(scratchPayPWTBean.getTicketBean().getGameNbr(), pwtStatus,gameId, scratchPayPWTBean.getPwtBean().getEncVirnCode(), connection,scratchPayPWTBean.getPwtBean().getEnctktNumber());
		commHelper.updateTicketInvTable(scratchPayPWTBean.getTicketBean().getTicketNumber(),scratchPayPWTBean.getTicketBean().getBook_nbr(), 
				scratchPayPWTBean.getTicketBean().getGameNbr(), gameId,pwtStatus, scratchPayPWTBean.getUserInfoBean().getUserId(),
				scratchPayPWTBean.getUserInfoBean().getUserOrgId(), scratchPayPWTBean.getTicketBean().getUpdateTicketType(),0, 
				RETAILER, API,connection);
		if (ACTIVE.equalsIgnoreCase(scratchPayPWTBean.getTicketBean().getBookStatus())) {
			commHelper.updateBookStatus(gameId, scratchPayPWTBean.getTicketBean().getBook_nbr(),connection, CLAIMED);
		}
		commHelper.updatePwtDateAndTierTickets(scratchPayPWTBean.getTicketBean(),connection);
	}
	
	private void updateOrganizationBalance(ScratchPayPWTBean scratchPayPWTBean, Connection connection) throws LMSException {
		if (isAutoScrap) {
			updateOrganizationBalanceIfAutoScrap(scratchPayPWTBean, connection);
		} else {
			OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt, UNCLAIM_BAL, CREDIT, scratchPayPWTBean.getUserInfoBean().getUserOrgId(),scratchPayPWTBean.getUserInfoBean().getParentOrgId(), RETAILER, 0, connection);
		}
	}

	private void updateOrganizationBalanceIfAutoScrap(ScratchPayPWTBean scratchPayPWTBean, Connection connection) throws LMSException {
		double retailerAmount = pwtAmt + pwtAmt * .01 * retailerCommission;
		double agentAmount = pwtAmt + pwtAmt * .01 * agentCommission;
		OrgCreditUpdation.updateOrganizationBalWithValidate(retailerAmount, CLAIM_BAL, DEBIT, scratchPayPWTBean.getUserInfoBean().getUserOrgId(),scratchPayPWTBean.getUserInfoBean().getParentOrgId(), RETAILER, 0, connection);
		OrgCreditUpdation.updateOrganizationBalWithValidate(agentAmount, CLAIM_BAL, DEBIT,scratchPayPWTBean.getUserInfoBean().getParentOrgId(), 0, AGENT, 0, connection);
	}

}
