package com.skilrock.lms.coreEngine.drawGames.drawMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans.TrackFullTicketBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class TrackFullTicketDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger("TrackFullTicketDaoImpl");

	public boolean checkAuthUnauthAttempts(int userId, int maxAuthAttempt, int maxUnauthAttempt, Connection connection) {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		int authAttemp = 0;
		int unauthAttemp = 0;
		try {
			query = "SELECT auth_attempt, unauth_attempt FROM st_lms_track_ticket_user_details WHERE req_by_user_id="+userId+";";
			stmt = connection.createStatement();
			logger.info("Select Authorized and UnAuthorize Limit from st_lms_track_ticket_user_details - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				authAttemp = rs.getInt("auth_attempt");
				unauthAttemp = rs.getInt("unauth_attempt");
			} else {
				logger.info("Insert In st_lms_track_ticket_user_details - "+query);
				query = "INSERT INTO st_lms_track_ticket_user_details (req_by_user_id, auth_attempt, unauth_attempt) VALUES ("+userId+", 0, 0);";
				logger.info("insertTrackTicketHistoryData - "+query);
				stmt.executeUpdate(query);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (authAttemp<maxAuthAttempt && unauthAttemp<maxUnauthAttempt);
	}

	public boolean trackTicketInLMS(TrackFullTicketBean ticketBean, Connection connection) {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		String compareAppender = null;
		boolean found = false;
		try {
			compareAppender = ("OLD".equals(ticketBean.getTicketFormat()))?"<":">=";
			stmt = connection.createStatement();
			query = "SELECT ticket_nbr FROM st_dg_ret_sale_"+ticketBean.getGameId()+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE ticket_nbr LIKE '%"+ticketBean.getTicketNumber()+"%' AND transaction_date"+compareAppender+"'"+ticketBean.getIdGenDate()+"';";
			logger.info("trackTicketInLMS in st_dg_ret_sale_"+ticketBean.getGameId()+" - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				ticketBean.setTicketNumber(rs.getString("ticket_nbr"));
				found = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return found;
	}

	public void insertTrackTicketHistoryData(int userId, TrackFullTicketBean ticketBean, String requestIp, Connection connection) {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		String query = null;
		String status = null;
		String updateColumn = null;
		String ticketNumber = null;
		try {
			status = "SUCCESS".equals(ticketBean.getStatus())?"VALID":"INVALID";
			updateColumn = "SUCCESS".equals(ticketBean.getStatus())?"auth_attempt":"unauth_attempt";
			ticketNumber = "SUCCESS".equals(ticketBean.getStatus())?ticketBean.getTicketNumber()+""+ticketBean.getReprintCount():ticketBean.getTicketNumber();

			pstmt = connection.prepareStatement("INSERT INTO st_lms_track_ticket_data (req_by_user_id, ticket_number, ticket_format, remarks, entry_time, request_ip, status) VALUES (?,?,?,?,?,?,?);");
			pstmt.setInt(1, userId);
			pstmt.setString(2, ticketNumber);
			pstmt.setString(3, ticketBean.getTicketFormat());
			pstmt.setString(4, ticketBean.getRemarks());
			pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
			pstmt.setString(6, requestIp);
			pstmt.setString(7, (status));
			logger.info("Insert in st_lms_track_ticket_data - "+pstmt);
			pstmt.executeUpdate();

			stmt = connection.createStatement();
			query = "UPDATE st_lms_track_ticket_user_details SET "+updateColumn+"="+updateColumn+"+1 WHERE req_by_user_id="+userId+";";
			logger.info("Update in st_lms_track_ticket_user_details - "+query);
			stmt.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void resetUsersAttemptLimits(Connection connection) {
		Statement stmt = null;
		String query = null;
		try {
			stmt = connection.createStatement();
			query = "INSERT INTO st_lms_track_ticket_user_details_history (req_by_user_id, auth_attempt, unauth_attempt, history_date) SELECT req_by_user_id, auth_attempt, unauth_attempt, '"+Util.getCurrentTimeString()+"' FROM st_lms_track_ticket_user_details;";
			logger.info("Insert in st_lms_track_ticket_user_details_history - "+query);
			stmt.executeUpdate(query);

			query = "UPDATE st_lms_track_ticket_user_details SET auth_attempt=0, unauth_attempt=0;";
			logger.info("Update in st_lms_track_ticket_user_details - "+query);
			stmt.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}