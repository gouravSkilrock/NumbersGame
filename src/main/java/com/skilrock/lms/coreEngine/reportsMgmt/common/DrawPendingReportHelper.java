package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DrawPendingSettlementBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class DrawPendingReportHelper {
	private static final Log logger = LogFactory.getLog(DrawPendingReportHelper.class);

	public List<DrawPendingSettlementBean> getProcessTicketsData(int agentOrgId, String interfaceType) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String gameQuery = null;
		StringBuilder queryBuilder = new StringBuilder("SELECT parent_id agentOrgId, (SELECT ").append(QueryManager.getOrgCodeQuery()).append(" FROM st_lms_organization_master mm WHERE mm.organization_id=aa.parent_id)org_name, COUNT(ticket_nbr) ticket_count, SUM(mrp_amt) total_mrp FROM (");
		String query = "";
		List<DrawPendingSettlementBean> drawPendingSettlementList = new ArrayList<DrawPendingSettlementBean>();
		DrawPendingSettlementBean drawPendingSettlementBean = null;
		try {
			String appender = "";
			if(agentOrgId != -1) {
				appender = " AND parent_id="+agentOrgId;
			}

			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			gameQuery = "SELECT game_id FROM st_dg_game_master WHERE game_status='OPEN';";
			logger.info("Game List Query - "+gameQuery);
			rs = stmt.executeQuery(gameQuery);
			while(rs.next()) {
				queryBuilder.append("SELECT om.parent_id, ret_org_id, NAME, ").append(interfaceType).append("_ticket_number ticket_nbr, mrp_amt FROM st_dg_last_sold_ticket lst INNER JOIN st_dg_ret_sale_").append(rs.getInt("game_id")).append(" rs ON lst.").append(interfaceType).append("_ticket_number=rs.ticket_nbr INNER JOIN st_lms_organization_master om ON lst.ret_org_id=om.organization_id WHERE ").append(interfaceType).append("_ticket_status='SOLD'").append(appender).append(" UNION ALL ");
			}
			query = queryBuilder.substring(0, queryBuilder.lastIndexOf(" UNION ALL "))+")aa GROUP BY parent_id ORDER BY org_name;";

			stmt = connection.createStatement();
			logger.info("getProcessTicketsData Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawPendingSettlementBean = new DrawPendingSettlementBean();
				drawPendingSettlementBean.setAgentOrgId(rs.getInt("agentOrgId"));
				drawPendingSettlementBean.setAgentOrgName(rs.getString("org_name"));
				drawPendingSettlementBean.setProcessingTickets(rs.getInt("ticket_count"));
				drawPendingSettlementBean.setProcessingAmount(rs.getDouble("total_mrp"));
				drawPendingSettlementList.add(drawPendingSettlementBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(connection);
		}

		return drawPendingSettlementList;
	}

	public List<DrawPendingSettlementBean> getUnsuccessfulTicketsData(int agentOrgId, Timestamp startTime, Timestamp endTime) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DrawPendingSettlementBean> drawPendingSettlementList = new ArrayList<DrawPendingSettlementBean>();
		DrawPendingSettlementBean drawPendingSettlementBean = null;
		try {
			String appender = "";
			if(agentOrgId != -1) {
				appender = " AND om.parent_id="+agentOrgId;
			}

			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("SELECT agentOrgId, org_name, cancel_expired, cancel_expired_mrp, cancel_failed, cancel_failed_mrp, server_error, server_error_mrp, cancel_expired+cancel_failed+server_error total_ticket, (cancel_expired_mrp+cancel_failed_mrp+server_error_mrp) total_mrp FROM (SELECT SUM(cancel_expired) cancel_expired, SUM(cancel_failed) cancel_failed, SUM(server_error) server_error, (SELECT "+QueryManager.getOrgCodeQuery()+" FROM st_lms_organization_master mm WHERE mm.organization_id=om.parent_id) org_name, parent_id agentOrgId, SUM(cancel_expired_mrp) cancel_expired_mrp, SUM(cancel_failed_mrp) cancel_failed_mrp, SUM(server_error_mrp) server_error_mrp FROM (SELECT retailer_org_id, IF(reason='CANCEL_EXPIRED', SUM(mrp_amt), 0) cancel_expired_mrp, IF(reason='AUTO_CANCEL_FAILED', SUM(mrp_amt),0)cancel_failed_mrp, IF(reason IN ('CANCEL_SERVER_FAILED','DG_ERROR'), SUM(mrp_amt), 0) server_error_mrp, IF(reason='CANCEL_EXPIRED', COUNT(ticket_nbr),0)cancel_expired, IF(reason='AUTO_CANCEL_FAILED', COUNT(ticket_nbr), 0) cancel_failed, IF(reason IN ('CANCEL_SERVER_FAILED','DG_ERROR'), COUNT(ticket_nbr), 0) server_error FROM st_dg_ret_pending_cancel WHERE transaction_date>=? AND transaction_date<=? GROUP BY reason, retailer_org_id)cancel INNER JOIN st_lms_organization_master om ON cancel.retailer_org_id=om.organization_id"+appender+" GROUP BY parent_id)main ORDER BY org_name;");
			pstmt.setTimestamp(1, startTime);
			pstmt.setTimestamp(2, endTime);
			logger.info("getUnsuccessfulTicketsData Query - "+pstmt);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				drawPendingSettlementBean = new DrawPendingSettlementBean();
				drawPendingSettlementBean.setAgentOrgId(rs.getInt("agentOrgId"));
				drawPendingSettlementBean.setAgentOrgName(rs.getString("org_name"));
				drawPendingSettlementBean.setDelayExpTicket(rs.getInt("cancel_expired"));
				drawPendingSettlementBean.setDelayExpAmount(rs.getDouble("cancel_expired_mrp"));
				drawPendingSettlementBean.setUnNotifyExpTicket(rs.getInt("cancel_failed"));
				drawPendingSettlementBean.setUnNotifyExpAmount(rs.getDouble("cancel_failed_mrp"));
				drawPendingSettlementBean.setServerErrorExpTicket(rs.getInt("server_error"));
				drawPendingSettlementBean.setServerErrorExpAmount(rs.getDouble("server_error_mrp"));
				drawPendingSettlementBean.setTotalExpTicket(rs.getInt("total_ticket"));
				drawPendingSettlementBean.setTotalExpAmount(rs.getDouble("total_mrp"));
				drawPendingSettlementList.add(drawPendingSettlementBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(connection);
		}

		return drawPendingSettlementList;
	}

	public List<DrawPendingSettlementBean> getRetTicketProcessData(int agentOrgId, String interfaceType) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String gameQuery = null;
		StringBuilder queryBuilder = null;
		String query = "";
		List<DrawPendingSettlementBean> drawPendingSettlementList = new ArrayList<DrawPendingSettlementBean>();
		DrawPendingSettlementBean drawPendingSettlementBean = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			gameQuery = "SELECT game_id FROM st_dg_game_master WHERE game_status='OPEN';";
			logger.info("Game List Query - "+gameQuery);
			rs = stmt.executeQuery(gameQuery);
			queryBuilder = new StringBuilder("SELECT ret_org_id, orgCode, ticket_nbr, mrp_amt FROM (");
			while(rs.next()) {
				queryBuilder.append("SELECT ret_org_id,").append(QueryManager.getOrgCodeQuery()).append(", ").append(interfaceType).append("_ticket_number ticket_nbr, mrp_amt FROM st_dg_last_sold_ticket lst INNER JOIN st_dg_ret_sale_").append(rs.getInt("game_id")).append(" rs ON lst.").append(interfaceType).append("_ticket_number=rs.ticket_nbr LEFT JOIN st_lms_organization_master om ON lst.ret_org_id=om.organization_id WHERE ").append(interfaceType).append("_ticket_status='SOLD' AND om.parent_id=").append(agentOrgId).append(" UNION ALL ");
			}
			query = queryBuilder.substring(0, queryBuilder.lastIndexOf(" UNION ALL "))+")aa ORDER BY orgCode;";

			stmt = connection.createStatement();
			logger.info("getRetailerProcessData Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawPendingSettlementBean = new DrawPendingSettlementBean();
				drawPendingSettlementBean.setRetailerOrgId(rs.getInt("ret_org_id"));
				drawPendingSettlementBean.setRetailerOrgName(rs.getString("orgCode"));
				String ticketNumber = rs.getString("ticket_nbr");
				if(ticketNumber.length()==1) {
					drawPendingSettlementBean.setTicketNumber(ticketNumber);
				} else if(ticketNumber.length()==17) {
					if("NEWTKTFORMAT".equalsIgnoreCase(Util.getTicketNumberFormat(ticketNumber+Util.getRpcAppenderForTickets(ticketNumber.length())))) {
						drawPendingSettlementBean.setTicketNumber("XXXX"+ticketNumber.substring(4, 17));
					} else {
						drawPendingSettlementBean.setTicketNumber(ticketNumber.substring(0, 13)+"XXXXX");
					}
				} else {
					drawPendingSettlementBean.setTicketNumber(ticketNumber.substring(0, 12)+"XX");
				}
				drawPendingSettlementBean.setProcessingAmount(rs.getDouble("mrp_amt"));
				drawPendingSettlementList.add(drawPendingSettlementBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(connection);
		}

		return drawPendingSettlementList;
	}

	public List<DrawPendingSettlementBean> getRetailerUnsuccessfulData(int agentOrgId, Timestamp startTime, Timestamp endTime) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DrawPendingSettlementBean> drawPendingSettlementList = new ArrayList<DrawPendingSettlementBean>();
		DrawPendingSettlementBean drawPendingSettlementBean = null;
		try {
			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("SELECT retailer_org_id,"+QueryManager.getOrgCodeQuery()+", ticket_nbr, ticket_nbr, mrp_amt, reason FROM (SELECT retailer_org_id, ticket_nbr, mrp_amt, reason FROM st_dg_ret_pending_cancel WHERE transaction_date>=? AND transaction_date<=?)cancel INNER JOIN st_lms_organization_master om ON cancel.retailer_org_id=om.organization_id AND om.parent_id="+agentOrgId+" ORDER BY "+QueryManager.getAppendOrgOrder()+";");
			pstmt.setTimestamp(1, startTime);
			pstmt.setTimestamp(2, endTime);
			logger.info("getRetailerUnsuccessfulData Query - "+pstmt);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				drawPendingSettlementBean = new DrawPendingSettlementBean();
				drawPendingSettlementBean.setRetailerOrgId(rs.getInt("retailer_org_id"));
				drawPendingSettlementBean.setRetailerOrgName(rs.getString("orgCode"));
				drawPendingSettlementBean.setTicketNumber(rs.getString("ticket_nbr"));
				String ticketNumber = rs.getString("ticket_nbr");
				if(ticketNumber.length()==1) {
					drawPendingSettlementBean.setTicketNumber(ticketNumber);
				} else if(ticketNumber.length()==17) {
					//drawPendingSettlementBean.setTicketNumber(ticketNumber.substring(0, 13)+"XXXXX");
					if("NEWTKTFORMAT".equalsIgnoreCase(Util.getTicketNumberFormat(ticketNumber+Util.getRpcAppenderForTickets(ticketNumber.length())))) {
						drawPendingSettlementBean.setTicketNumber("XXXX"+ticketNumber.substring(4, 17));
					} else {
						drawPendingSettlementBean.setTicketNumber(ticketNumber.substring(0, 13)+"XXXXX");
					}
				} else {
					drawPendingSettlementBean.setTicketNumber(ticketNumber.substring(0, 12)+"XX");
				}
				drawPendingSettlementBean.setProcessingAmount(rs.getDouble("mrp_amt"));
				drawPendingSettlementBean.setCancelReason(rs.getString("reason"));
				drawPendingSettlementList.add(drawPendingSettlementBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closeCon(connection);
		}

		return drawPendingSettlementList;
	}
}