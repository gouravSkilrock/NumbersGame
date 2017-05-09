package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;

public class ScratchTicketVerifyDaoImpl {
	final static Log logger = LogFactory
			.getLog(ScratchTicketVerifyDaoImpl.class);

	public void isScratchTicketValid(String ticketNbr, String virnNbr,
			Connection con) throws LMSException {
		logger
				.info("***** Inside isScratchTicketValid Method of ScratchTicketVerifyDaoImpl Class");

		String sqlQuery = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		int gameId = -1;

		try {
			// sqlQuery =
			// "select game_id from st_se_game_master where game_nbr=?";
			sqlQuery = "SELECT gm.game_id from st_se_game_master gm inner join st_se_second_chance_game_mapping scgm on "
					+ "gm.game_id = scgm.game_id where scgm.status = 'ACTIVE' and gm.game_nbr = "
					+ Integer.parseInt(ticketNbr.substring(0, 3));

			logger.info("Query Checking Game Status and Fetching Game Id "
					+ sqlQuery);

			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);

			if (rs.next()) {
				gameId = rs.getInt("game_id");
				if (ticketNbr.indexOf("-") == -1) {
					sqlQuery = " select game_nbr_digits, pack_nbr_digits, book_nbr_digits,ticket_nbr_digits"
							+ " from st_se_game_ticket_nbr_format where game_id="
							+ gameId;

					logger.info("Query Check Game Format " + sqlQuery);

					stmt = con.createStatement();
					rs = stmt.executeQuery(sqlQuery);

					logger.info("Query Check Game Format " + sqlQuery);

					if (rs.next()) {
						int gameNoDigit = rs.getInt("game_nbr_digits");
						int packDigit = rs.getInt("pack_nbr_digits");
						int bookDigit = rs.getInt("book_nbr_digits");

						ticketNbr = ticketNbr.substring(0, gameNoDigit)
								+ "-"
								+ ticketNbr.substring(gameNoDigit, (gameNoDigit
										+ packDigit + bookDigit))
								+ "-"
								+ ticketNbr
										.substring((gameNoDigit + packDigit + bookDigit));
					} else {
						logger.info("No Entry in st_se_game_ticket_nr_format");
						throw new LMSException(
								LMSErrors.SCRATCH_INVALID_TICKET_NUMBER_FORMAT_ERROR_CODE,
								LMSErrors.SCRATCH_INVALID_TICKET_NUMBER_FORMAT_ERROR_MESSAGE);
					}
				}

				sqlQuery = "select gis.game_id from st_se_game_inv_status gis inner join st_se_pwt_inv_? pig on gis.game_id = pig.game_id where gis.game_id = ? and gis.book_nbr = ? and gis.current_owner in ('RETAILER', 'AGENT') and gis.book_status in ('CLAIMED', 'ACTIVE') and pig.id1 = ? and pig.virn_code = ? and pig.game_id = ? and pig.status = ?";
				pStmt = con.prepareStatement(sqlQuery);

				pStmt.setInt(1, Integer.parseInt(ticketNbr.substring(0, 3)));
				pStmt.setInt(2, gameId);
				pStmt.setString(3, ticketNbr.substring(0, 10)); // Book Number
				pStmt.setString(4, MD5Encoder.encode(ticketNbr));
				pStmt.setString(5, MD5Encoder.encode(virnNbr));
				pStmt.setInt(6, gameId);
				pStmt.setString(7, "NO_PRIZE_PWT");

				logger
						.info("Query Checking Ticket and Virn Status in  st_se_game_inv_status and st_se_pwt_inv  Table "
								+ pStmt);

				rs = pStmt.executeQuery();
				if (!rs.next()) {
					throw new LMSException(
							LMSErrors.SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_CODE,
							LMSErrors.SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_MESSAGE);
				}
			} else {
				// Game Number Doesn't Exists
				logger.info("Game Number Not Aval.");
				throw new LMSException(
						LMSErrors.SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_CODE,
						LMSErrors.SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_MESSAGE);
			}
		} catch (SQLException se) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) throws LMSException {
		Connection con = DBConnect.getConnection();
		new ScratchTicketVerifyDaoImpl().isScratchTicketValid("101-011011-110",
				"", con);
	}
}
