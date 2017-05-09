package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;

public class GameUtilityHelper {
	private static Connection connection = null;

	static Log logger = LogFactory.getLog(GameUtilityHelper.class);
	private static PreparedStatement pstmt = null;
	private static ResultSet resultSet = null;

	public synchronized static List fetchRemainingPrizeList(int gameId) {

		List<PrizeStatusBean> prizeStatusList = new ArrayList<PrizeStatusBean>();
		PrizeStatusBean bean = null;
		try {
			logger.debug("=================== ======================");
			logger.debug(Calendar.getInstance().getTime());

			connection = DBConnect.getConnection();
			String query = QueryManager.getST_NO_OF_PRIZE_REM();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, gameId);
			resultSet = pstmt.executeQuery();
			logger.debug("query ==" + pstmt);
			while (resultSet.next()) {
				bean = new PrizeStatusBean();
				double pwtAmt = resultSet.getDouble(TableConstants.SPI_PWT_AMT);
				int nbrOfPrizeLeft = resultSet
						.getInt(TableConstants.PRIZES_REMAINING);
				bean.setPrizeAmt(pwtAmt);
				bean.setNbrOfPrizeLeft(nbrOfPrizeLeft);
				prizeStatusList.add(bean);
			}
			resultSet.close();
			pstmt.close();

			logger.debug(Calendar.getInstance().getTime());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("=============================================");
		return prizeStatusList;
	}

	public synchronized static String getNoOfPrizeFromValue() {
		String pValue = null;
		PropertyLoader.loadProperties("RMS/LMS.properties");
		pValue = PropertyLoader.getProperty("get_no_of_prize_from");
		logger.debug("get_no_of_prize_from === " + pValue);
		return pValue;
	}

	public synchronized static String getPwtStatusOfVirn(int gameId,
			String virnCode, Connection con, int gameNbr) throws SQLException {
		String status = null;
		connection = con;
		String pwtStatusQuery = "select status from st_se_pwt_inv_? where virn_code = ? and game_id = ? ";
		pstmt = connection.prepareStatement(pwtStatusQuery);
		pstmt.setInt(1, gameNbr);
		pstmt.setString(2, virnCode);
		pstmt.setInt(3, gameId);		
		resultSet = pstmt.executeQuery();
		logger.debug("query ==" + pstmt);
		if (resultSet.next()) {
			status = resultSet.getString("status");
		}
		logger.debug(" virn code ===== " + virnCode + "status ========= "
				+ status);
		resultSet.close();
		pstmt.close();
		return status;
	}

	public synchronized static String getPwtStatusOfVirnNewZim(int gameId,
			String virnCode, Connection con, int gameNbr,String enTktNbr) throws SQLException {
		String status = null;
		connection = con;
		String pwtStatusQuery = "select status from st_se_pwt_inv_? where virn_code = ? and game_id = ? and id1=?";
		pstmt = connection.prepareStatement(pwtStatusQuery);
		pstmt.setInt(1, gameNbr);
		pstmt.setString(2, virnCode);
		pstmt.setInt(3, gameId);
		pstmt.setString(4, enTktNbr);
		resultSet = pstmt.executeQuery();
		logger.debug("query ==" + pstmt);
		if (resultSet.next()) {
			status = resultSet.getString("status");
		}
		logger.debug(" virn code ===== " + virnCode + "status ========= "
				+ status);
		resultSet.close();
		pstmt.close();
		return status;
	}
	
	public static void main(String[] args) {
		fetchRemainingPrizeList(48);
	}

	private static boolean newReducePrizeRem(String status, String nextStatus) {
		boolean flag = false;

		if ("UNCLM_PWT".equalsIgnoreCase(status.trim())
				|| "UNCLM_CANCELLED".equalsIgnoreCase(status.trim())) {
			if ("CLAIM_PLR_RET_CLM".equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_PLR_RET_UNCLM"
							.equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_RET_CLM".equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_RET_UNCLM".equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_AGT".equalsIgnoreCase(nextStatus.trim())) {
				flag = true;
				return flag;
			}
		} else if ("PND_PAY".equalsIgnoreCase(status.trim())) {
			if ("CLAIM_PLR_RET_CLM_DIR".equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_PLR_RET_UNCLM_DIR".equalsIgnoreCase(nextStatus
							.trim())
					|| "CLAIM_PLR_AGT_CLM_DIR".equalsIgnoreCase(nextStatus
							.trim())
					|| "CLAIM_PLR_AGT_UNCLM_DIR".equalsIgnoreCase(nextStatus
							.trim())
					|| "CLAIM_PLR_BO".equalsIgnoreCase(nextStatus.trim())
					|| "CLAIM_AGT".equalsIgnoreCase(nextStatus.trim())) {
				flag = true;
				return flag;
			}
		} else if ("CLAIM_AGT_TEMP".equalsIgnoreCase(status.trim())) {
			if ("CLAIM_AGT".equalsIgnoreCase(nextStatus.trim())) {
				flag = true;
				return flag;
			}
		}

		// || "CLAIM_RET_TEMP".equalsIgnoreCase(status.trim()) ||
		// "PND_PAY".equalsIgnoreCase(status.trim())) {

		return false;
	}

	/*
	 * private static boolean canReducePrizeRem(String status, String
	 * nextStatus) { boolean flag = false;
	 * 
	 * if("CLAIM_PLR_RET_CLM".equalsIgnoreCase(nextStatus.trim()) ||
	 * "CLAIM_PLR_RET_UNCLM".equalsIgnoreCase(nextStatus.trim()) ||
	 * ("CLAIM_PLR_RET_CLM_DIR".equalsIgnoreCase(nextStatus.trim()) &&
	 * "PND_PAY".equalsIgnoreCase(status.trim())) ||
	 * ("CLAIM_PLR_RET_UNCLM_DIR".equalsIgnoreCase(nextStatus.trim()) &&
	 * "PND_PAY".equalsIgnoreCase(status.trim())) ) {
	 * 
	 * if("UNCLM_PWT".equalsIgnoreCase(status.trim()) ||
	 * "UNCLM_CANCELLED".equalsIgnoreCase(status.trim()) ||
	 * "PND_PAY".equalsIgnoreCase(status.trim())) { flag = true; return flag; } }
	 * else if("CLAIM_RET".equalsIgnoreCase(nextStatus.trim()) ||
	 * "CLAIM_RET_CLM".equalsIgnoreCase(nextStatus.trim()) ||
	 * "CLAIM_RET_UNCLM".equalsIgnoreCase(nextStatus.trim()) ||
	 * ("CLAIM_PLR_AGT_CLM_DIR".equalsIgnoreCase(nextStatus.trim()) &&
	 * "PND_PAY".equalsIgnoreCase(status.trim())) ||
	 * ("CLAIM_PLR_AGT_UNCLM_DIR".equalsIgnoreCase(nextStatus.trim()) &&
	 * "PND_PAY".equalsIgnoreCase(status.trim())) ) {
	 * if("UNCLM_PWT".equalsIgnoreCase(status.trim()) ||
	 * "UNCLM_CANCELLED".equalsIgnoreCase(status.trim()) ||
	 * "CLAIM_RET_TEMP".equalsIgnoreCase(status.trim()) ||
	 * "PND_PAY".equalsIgnoreCase(status.trim())) { flag = true; return flag; } }
	 * else if("CLAIM_AGT".equalsIgnoreCase(nextStatus.trim())) {
	 * if("UNCLM_PWT".equalsIgnoreCase(status.trim()) ||
	 * "UNCLM_CANCELLED".equalsIgnoreCase(status.trim()) ||
	 * "CLAIM_AGT_TEMP".equalsIgnoreCase(status.trim()) ||
	 * "PND_PAY".equalsIgnoreCase(status.trim())) { flag = true; return flag; } }
	 * else if("CLAIM_PLR_BO".equalsIgnoreCase(nextStatus.trim())) {
	 * if("PND_PAY".equalsIgnoreCase(status.trim())) { flag = true; return flag; } }
	 * return false; }
	 */

	// this method is updated by yogesh to get the pwt tables by game number
	// This method update the no_of_prize_claim in st_se_rank_master table.
	public synchronized static boolean updateNoOfPrizeRem(int gameId,
			double pwtAmt, String nextStatus, String virnCode, Connection con,
			int gameNbr) {

		boolean success = false;
		String status = null;
		boolean flag = false;
		logger
				.debug("========= ======================================inside game_utility");
		try {
			connection = con;

			// get the current status of virn code
			status = getPwtStatusOfVirn(gameId, virnCode, con, gameNbr);

			// here it check that can we reduce the no of prize remaining list
			// flag = canReducePrizeRem(status, nextStatus);
			flag = newReducePrizeRem(status, nextStatus);

			// update the no of prize remaining in rank master table
			if (flag) {
				String updateNoOfPrizeClaimedQuery = "update st_se_rank_master set no_of_prize_claim = no_of_prize_claim+1 where game_id = ? and prize_amt = ?";
				pstmt = connection
						.prepareStatement(updateNoOfPrizeClaimedQuery);
				pstmt.setInt(1, gameId);
				pstmt.setDouble(2, pwtAmt);
				int a = pstmt.executeUpdate();
				logger.debug(" row updated ======= " + a);
				pstmt.close();
			}

			logger.debug(Calendar.getInstance().getTime());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logger.debug("=============================================");
		return success;

	}



public synchronized static boolean updateNoOfPrizeRemNewZim(int gameId,
		double pwtAmt, String nextStatus, String virnCode, Connection con,
		int gameNbr,String enTktNbr) {

	boolean success = false;
	String status = null;
	boolean flag = false;
	logger
			.debug("========= ======================================inside game_utility");
	try {
		connection = con;

		// get the current status of virn code
		status = getPwtStatusOfVirnNewZim(gameId, virnCode, con, gameNbr,enTktNbr);

		// here it check that can we reduce the no of prize remaining list
		// flag = canReducePrizeRem(status, nextStatus);
		flag = newReducePrizeRem(status, nextStatus);

		// update the no of prize remaining in rank master table
		if (flag) {
			String updateNoOfPrizeClaimedQuery = "update st_se_rank_master set no_of_prize_claim = no_of_prize_claim+1 where game_id = ? and prize_amt = ?";
			pstmt = connection
					.prepareStatement(updateNoOfPrizeClaimedQuery);
			pstmt.setInt(1, gameId);
			pstmt.setDouble(2, pwtAmt);
			int a = pstmt.executeUpdate();
			logger.debug(" row updated ======= " + a);
			pstmt.close();
		}

		logger.debug(Calendar.getInstance().getTime());
	} catch (SQLException e) {
		e.printStackTrace();
	}
	logger.debug("=============================================");
	return success;

}

}
