package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.MD5Encoder;

public class ScratchTicketVerifyHelper {
	final static Log logger = LogFactory.getLog(ScratchTicketVerifyHelper.class);

	public boolean isScratchTicketValid(String ticketNbr, String virnNbr) {
		boolean isValid = false;

		Connection con = null;
		String sqlQuery = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		int gameId = 0;
		try {
			con = DBConnect.getConnection();
			sqlQuery = "select game_id from st_se_game_master where game_nbr=?";

			pStmt = con.prepareStatement(sqlQuery);
			pStmt.setInt(1, Integer.parseInt(ticketNbr.substring(0, 3)));

			rs = pStmt.executeQuery();

			if (rs.next()) {
				gameId = rs.getInt("game_id");
				if (ticketNbr.indexOf("-") == -1) {
					sqlQuery = " select game_nbr_digits,pack_nbr_digits,book_nbr_digits,ticket_nbr_digits"
							+ " from st_se_game_ticket_nbr_format where game_id=?";

					pStmt = con.prepareStatement(sqlQuery);
					pStmt.setInt(1, gameId);

					rs = pStmt.executeQuery();
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
					}
				}

				sqlQuery = QueryManager
						.getST4CurrentOwnerDetailsUsingGameBookNbr();

				pStmt = con.prepareStatement(sqlQuery);
				pStmt.setInt(1, gameId);
				pStmt.setString(2, ticketNbr.substring(0, 10)); // Book Number
				rs = pStmt.executeQuery();

				if (rs.next()) {
					String ownerType = rs.getString("current_owner");
					String bookStatus = rs.getString("book_status");
					if (("RETAILER".equalsIgnoreCase(ownerType.trim()) || "AGENT"
							.equalsIgnoreCase(ownerType.trim()))
							&& ("ACTIVE".equalsIgnoreCase(bookStatus) || "CLAIMED"
									.equalsIgnoreCase(bookStatus))) {
						// Book Available
						sqlQuery = "select virn_code, id1 from st_se_pwt_inv_? where game_id=? and status=?";
						pStmt = con.prepareStatement(sqlQuery);
						pStmt.setInt(1, Integer.parseInt(ticketNbr.substring(0,
								3)));
						pStmt.setInt(2, gameId);
						pStmt.setString(3, "NO_PRIZE_PWT");

						rs = pStmt.executeQuery();
						while (rs.next()) {
							if (MD5Encoder.encode(ticketNbr).equals(
									rs.getString("id1"))
									&& MD5Encoder.encode(virnNbr).equals(
											rs.getString("virn_code"))) {
								isValid = true;
								break;
							}
						}
						if (!isValid) {
							logger
									.info("Ticket Or VIRN Not Available in st_se_pwt_inv_gameNbr Table");
							// Ticket Entry Not Available
						}
					} else {
						logger
								.info("Book Not Available or book not assigned to AGENT/RETAILER in st_se_game_inv_status Table");
						// Book Not Available or book not assigned to
						// AGENT/RETAILER
					}
				} else {
					// Game Number Doesn't Exists
					logger.info("Game Number Not Aval.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}
}