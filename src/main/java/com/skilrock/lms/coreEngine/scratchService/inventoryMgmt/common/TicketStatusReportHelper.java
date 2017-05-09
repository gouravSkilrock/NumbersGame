package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ScratchTicketDetailStatusBean;
import com.skilrock.lms.beans.ScratchTicketStatusBean;
import com.skilrock.lms.common.db.DBConnect;

public class TicketStatusReportHelper {

	static Log logger = LogFactory.getLog(TicketStatusReportHelper.class);

	public static List<ScratchTicketStatusBean> fetchTicketStatus(int retOrgId, int gameId, int parentOrgId) {
		Connection con = null;

		try {
			StringBuilder appendQuery = new StringBuilder();

			StringBuilder appendQueryInvStatus = new StringBuilder();

			if (retOrgId == -1) {
				appendQuery.append(" WHERE parent_id =" + parentOrgId);
				appendQueryInvStatus.append(" and parent_id =" + parentOrgId);
			} else {
				appendQuery.append(" where ret_org_id=" + retOrgId);
				appendQueryInvStatus.append(" and current_owner_id =" + retOrgId);

			}
			if (gameId != -1) {
				appendQuery.append(" AND sm.game_id=" + gameId);
				appendQuery.append(" AND sm.game_id=" + gameId);
			}
			con = DBConnect.getConnection();
			String query = "SELECT NAME,game_name,soldToday,(nbr_of_tickets_per_book-sold)  total_ticket, ret_org_id,gameNumber,bookNumber FROM (SELECT NAME,game_name,SUM(IF(DATE(sale_time)=DATE(NOW()), (soldTickets- unsoldTickets) , 0))     soldToday ,  SUM(   IF(DATE(sale_time)!=DATE(NOW()), (soldTickets-unsoldTickets) ,0) ) sold,  nbr_of_tickets_per_book  ,ret_org_id,gameNumber,bookNumber    FROM(SELECT SUBSTRING_INDEX(ticket_nbr, '-', 2) bookNumber,SUBSTRING_INDEX(ticket_nbr, '-', 1) gameNumber,NAME,game_name ,SUM(IF(ticket_status='SOLD',1,0) ) soldTickets,SUM( IF(ticket_status='UNSOLD',1,0)) unsoldTickets ,nbr_of_tickets_per_book,ret_org_id ,sale_time  FROM st_se_ticket_by_ticket_sale_txn INNER JOIN st_se_game_master sm ON game_nbr =SUBSTRING_INDEX(ticket_nbr, '-', 1) INNER JOIN  st_lms_organization_master ON  ret_org_id=organization_id "
					+ appendQuery.toString()
					+ " GROUP BY SUBSTRING_INDEX(ticket_nbr, '-', 2) ,DATE(sale_time)) main GROUP BY ret_org_id,bookNumber) fulldata "
					+ " union  SELECT  NAME,game_name, 0 soldToday,nbr_of_tickets_per_book total_ticket, organization_id,game_nbr gameNumber,book_nbr bookNumber FROM st_se_game_inv_status   invs INNER JOIN st_lms_organization_master  ON  current_owner_id=organization_id INNER JOIN st_se_game_master sm ON sm.game_id =invs.game_id    WHERE     book_status='ACTIVE'  and book_nbr NOT IN (SELECT book_nbr FROM st_se_ticket_by_ticket_sale_txn ) "
					+ appendQueryInvStatus.toString();
			System.out.println("query" + query);
			Statement ticketStatus = con.createStatement();
			ResultSet ticketStatusList = ticketStatus.executeQuery(query);
			List<ScratchTicketStatusBean> ticketStatusBeanList = new ArrayList<ScratchTicketStatusBean>();
			while (ticketStatusList.next()) {
				ScratchTicketStatusBean statusBean = new ScratchTicketStatusBean();
				statusBean.setBookNumber(ticketStatusList.getString("bookNumber"));
				statusBean.setGameName(ticketStatusList.getString("game_name"));
				statusBean.setRetailerOrgName(ticketStatusList.getString("NAME"));
				statusBean.setTicketsSold(ticketStatusList.getInt("soldToday"));
				statusBean.setTotalTickets(ticketStatusList.getInt("total_ticket"));
				int remainingTicket = statusBean.getTotalTickets() - statusBean.getTicketsSold();
				statusBean.setTicketsRemaining(remainingTicket < 0 ? 0 : remainingTicket);
				ticketStatusBeanList.add(statusBean);
			}

			return ticketStatusBeanList;

		} catch (Exception e) {
			logger.error("Execption", e);
		}

		return null;

	}

	public static List<ScratchTicketDetailStatusBean> fetchTicketDetailStatus(String bookNo, String is_remain) {
		List<ScratchTicketDetailStatusBean> ticketDetailStatusBeanList = null;
		Map<String, ScratchTicketDetailStatusBean> ticketMap = new TreeMap<String, ScratchTicketDetailStatusBean>();
		String[] noOfZeroArr = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			String query = "SELECT nbr_of_tickets_per_book FROM st_se_game_master WHERE game_nbr="
					+ bookNo.split("-")[0];
			ResultSet rs = con.prepareStatement(query).executeQuery();
			int totalTicket = 0;
			if (rs.next())
				totalTicket = rs.getInt("nbr_of_tickets_per_book");
			int noOfDigit = (totalTicket + "").length();
			noOfZeroArr = new String[noOfDigit];
			String temp = "-";
			for (int i = noOfDigit - 1; i >= 0; i--) {
				if (i != noOfDigit - 1)
					temp = temp + "0";
				noOfZeroArr[i] = temp;
			}

			for (int i = 1; i <= totalTicket; i++) {
				int digits = (i + "").length() - 1;
				String tkt = bookNo + noOfZeroArr[digits] + i;
				ScratchTicketDetailStatusBean statusBean = new ScratchTicketDetailStatusBean();
				statusBean.setTicketNumber(tkt);
				statusBean.setStatus("UNSOLD");
				ticketMap.put(tkt, statusBean);
			}
			
			query = "SELECT ticket_nbr, sale_time, ticket_status FROM (SELECT ticket_nbr, sale_time, ticket_status FROM st_se_ticket_by_ticket_sale_txn "
					+ "where ticket_nbr LIKE '"+ bookNo + "-%' ORDER BY sale_time desc) txn GROUP BY ticket_nbr";
			logger.debug("query " + query);
			Statement ticketStatus = con.createStatement();
			ResultSet ticketStatusList = ticketStatus.executeQuery(query);
			while (ticketStatusList.next()) {
				String ticket_nbr = ticketStatusList.getString("ticket_nbr");
				String status = ticketStatusList.getString("ticket_status");
				if ("Y".equals(is_remain)){
					if("SOLD".equals(status))
						ticketMap.remove(ticket_nbr);					
				}
				else {
					ScratchTicketDetailStatusBean statusBean = ticketMap.get(ticket_nbr);					
					statusBean.setStatus(status);
					if("SOLD".equals(status))
						statusBean.setSoldTime(ticketStatusList.getString("sale_time").replace(".0",""));
				}
			}
			ticketDetailStatusBeanList = new ArrayList<ScratchTicketDetailStatusBean>(ticketMap.values());
			
			return ticketDetailStatusBeanList;
		} catch (Exception e) {
			logger.error("Execption", e);
		}
		return null;
	}

}