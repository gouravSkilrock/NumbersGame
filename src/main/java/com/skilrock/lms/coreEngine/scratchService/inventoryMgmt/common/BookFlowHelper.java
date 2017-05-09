package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.InvTransitionBean;
import com.skilrock.lms.beans.InvTransitionWarehouseWiseBean;
import com.skilrock.lms.beans.InvTransitionWarehouseWiseDataBean;
import com.skilrock.lms.beans.ScratchBookDataBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class BookFlowHelper {

	public Map getBookFlow(int gameId, String bookNbr) throws LMSException {

		/*
		 * int gameNbr=0; StringTokenizer stringtoken=new
		 * StringTokenizer(getGameNameNbr(),"-");
		 * while(stringtoken.hasMoreTokens()) {
		 * gameNbr=Integer.parseInt(stringtoken.nextToken()); break; }
		 * System.out.println("game number is " + gameNbr); int
		 * gameId=getGameIdfromgameNbr(gameNbr);
		 */

		List<InvTransitionBean> transitionList = new ArrayList<InvTransitionBean>();
		boolean bookValidity = false;

		 
		Connection connection = DBConnect.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;

		List<String> currOwnerList = new ArrayList<String>();
		List<String> currOwnerNameList = new ArrayList<String>();
		List<String> txTimeList = new ArrayList<String>();

		String currOwner = null;
		String nextOwner = null;
		Timestamp txTime = null;

		String currOwnerName = null;
		String nextOwnerName = null;

		InvTransitionBean invTransitionBean = null;
		transitionList = new ArrayList<InvTransitionBean>();
		SimpleDateFormat sdt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		String time = null;
		// String bookNbr=getBookNumber();

		GameTicketFormatBean ticketformatBean = getGameTicketinfo(gameId);

		if (bookNbr.indexOf("-") == -1
				&& bookNbr.length() >= ticketformatBean.getGameNbrDigits()
						+ ticketformatBean.getBookDigits()
						+ ticketformatBean.getPackDigits()) {
			bookNbr = bookNbr.substring(0, ticketformatBean.getGameNbrDigits())
					+ "-"
					+ bookNbr.substring(ticketformatBean.getGameNbrDigits());
			System.out.println("New book nbr:::" + bookNbr);
		}

		try {
			statement = connection
					.prepareStatement("select a.current_owner,a.current_owner_id,a.transaction_date,b.name from st_se_game_inv_detail a, st_lms_organization_master b where game_id = "
							+ gameId
							+ "  and book_nbr = '"
							+ bookNbr
							+ "' and a.current_owner_id = b.organization_id order by transaction_date");
			System.out
					.println("query    :"
							+ "select a.current_owner,a.current_owner_id,a.transaction_date,b.name from st_se_game_inv_detail a, st_lms_organization_master b where game_id = "
							+ gameId
							+ "  and book_nbr = '"
							+ bookNbr
							+ "' and a.current_owner_id = b.organization_id order by transaction_date");
			rs = statement.executeQuery();

			while (rs.next()) {
				currOwnerList.add(rs.getString("current_owner"));
				currOwnerNameList.add(rs.getString("name"));
				txTime = rs.getTimestamp("transaction_date");
				txTimeList.add(sdt.format(txTime));
				bookValidity = true;
			}
			if (currOwnerList != null && currOwnerList.size() > 1) {

				for (int i = 0; i < currOwnerList.size() - 1; i++) {

					currOwner = currOwnerList.get(i);
					nextOwner = currOwnerList.get(i + 1);
					time = txTimeList.get(i + 1);

					currOwnerName = currOwnerNameList.get(i);
					nextOwnerName = currOwnerNameList.get(i + 1);

					invTransitionBean = new InvTransitionBean(currOwnerName,
							nextOwnerName, currOwner, nextOwner, time);
					transitionList.add(invTransitionBean);

					if (currOwner.equals("BO")) {
						invTransitionBean.setBOToAgent(true);
					} else if (currOwner.equals("AGENT")) {

						if (nextOwner.equals("RETAILER")) {
							invTransitionBean.setAgentToRetailer(true);
						} else {
							invTransitionBean.setAgentToBO(true);
						}
					} else if (currOwner.equals("RETAILER")) {
						invTransitionBean.setRetailerToAgent(true);
					}
				}
			}

			System.out.println();
			for (InvTransitionBean i : transitionList) {
				System.out.println(i);
			}

			Map bookFlowDetailMap = new HashMap();
			bookFlowDetailMap.put("transitionList", transitionList);
			bookFlowDetailMap.put("bookValidity", bookValidity);
			return bookFlowDetailMap;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public InvTransitionWarehouseWiseBean getBookFlowNew(int gameId, String bookNbr) throws LMSException {
		List<String> headers = new LinkedList<String>();
		InvTransitionWarehouseWiseBean invTransitionWarehouseWiseBean = new InvTransitionWarehouseWiseBean();
		List<InvTransitionWarehouseWiseDataBean> invTransitionWarehouseWiseDataBeans = new ArrayList<InvTransitionWarehouseWiseDataBean>();
		InvTransitionWarehouseWiseDataBean invTransitionWarehouseWiseDataBean = null;
		ScratchBookDataBean scratchBookDataBean = null;
		List<Integer> loopList = null;

		Connection connection = DBConnect.getConnection();
		Statement statement = null;
		ResultSet rs = null;

		List<String> currOwnerList = new ArrayList<String>();
		List<String> currOwnerNameList = new ArrayList<String>();
		List<String> txTimeList = new ArrayList<String>();
		List<String> warehouseName = new ArrayList<String>();

		List<String> bookStatus = new ArrayList<String>();
		
		String query = null;
		
		SimpleDateFormat sdt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		GameTicketFormatBean ticketformatBean = getGameTicketinfo(gameId);
		if (bookNbr.indexOf("-") == -1 && bookNbr.length() >= ticketformatBean.getGameNbrDigits() + ticketformatBean.getBookDigits() + ticketformatBean.getPackDigits()) {
			bookNbr = bookNbr.substring(0, ticketformatBean.getGameNbrDigits()) + "-" + bookNbr.substring(ticketformatBean.getGameNbrDigits());
			System.out.println("New book nbr:::" + bookNbr);
		}

		try {
			statement = connection.createStatement();

			query = "select a.current_owner,a.current_owner_id,a.transaction_date,b.name, c.warehouse_name, a.book_status from st_se_game_inv_detail a inner join st_lms_organization_master b on a.current_owner_id = b.organization_id inner join st_se_warehouse_master c on a.warehouse_id = c.warehouse_id  where game_id = " + gameId + " and book_nbr = '" + bookNbr + "' order by transaction_date";
			System.out.println("query    :" + query);
			rs = statement.executeQuery(query);
			Set<String> tempHeaders = new LinkedHashSet<String>();
			while (rs.next()) {
				currOwnerList.add(rs.getString("current_owner"));
				currOwnerNameList.add(rs.getString("name"));
				txTimeList.add(sdt.format(rs.getTimestamp("transaction_date")));
				warehouseName.add(rs.getString("warehouse_name"));
				if ("BO".equals(rs.getString("current_owner")))
					tempHeaders.add(rs.getString("warehouse_name"));
				bookStatus.add(rs.getString("book_status"));
			}

			headers.addAll(tempHeaders);
			tempHeaders = null;
			
			query = "select a.book_nbr, a.pack_nbr, a.active_tickets_upto, (a.cur_rem_tickets + a.active_tickets_upto) totalTkts, ifnull(a.book_activation_date_ret, 'NA') book_activation_date_ret, ifnull(a.book_receive_reg_date_ret, 'NA') book_receive_reg_date_ret, a.current_owner,a.current_owner_id,b.name, c.warehouse_name, a.book_status, gm.game_nbr, gm.game_name, gm.ticket_price,gm.nbr_of_tickets_per_book, (gm.ticket_price * gm.nbr_of_tickets_per_book) book_cost, ifnull(a.ret_invoice_id, 'NA') ret_invoice_id, a.total_low_win_tier_tickets_claimed, gm.nbr_of_tickets from st_se_game_inv_status a inner join st_lms_organization_master b on a.current_owner_id = b.organization_id inner join st_se_warehouse_master c on a.warehouse_id = c.warehouse_id inner join st_se_game_master gm on gm.game_id = a.game_id where a.game_id = "
					+ gameId + " and book_nbr = '" + bookNbr + "'";
			System.out.println("query    :" + query);
			rs = statement.executeQuery(query);
			
			if(rs.next()) {
				scratchBookDataBean = new ScratchBookDataBean();
				scratchBookDataBean.setPackNbr(rs.getString("pack_nbr"));
				scratchBookDataBean.setBookNbr(rs.getString("book_nbr"));
				scratchBookDataBean.setSoldTkts(rs.getInt("active_tickets_upto"));
				//scratchBookDataBean.setTotalTkts(rs.getInt("totalTkts"));
				scratchBookDataBean.setTotalTkts(rs.getInt("nbr_of_tickets_per_book"));
				if(!"NA".equals(rs.getString("book_activation_date_ret")))
					scratchBookDataBean.setBookActivationDate(sdt.format(rs.getTimestamp("book_activation_date_ret")));
				else
					scratchBookDataBean.setBookActivationDate(rs.getString("book_activation_date_ret"));
				if(!"NA".equals(rs.getString("book_receive_reg_date_ret")))
					scratchBookDataBean.setBookReceiveDate(sdt.format(rs.getTimestamp("book_receive_reg_date_ret")));
				else
					scratchBookDataBean.setBookReceiveDate(rs.getString("book_receive_reg_date_ret"));
				scratchBookDataBean.setOwnerType(rs.getString("current_owner"));
				scratchBookDataBean.setOrgId(rs.getInt("current_owner_id"));
				scratchBookDataBean.setOwnerName(rs.getString("name"));
				scratchBookDataBean.setWarehouseName(rs.getString("warehouse_name"));
				scratchBookDataBean.setBookStatus(rs.getString("book_status"));
				scratchBookDataBean.setGameNbr(rs.getString("game_nbr"));
				scratchBookDataBean.setGameName(rs.getString("game_name"));
				scratchBookDataBean.setTicketPrice(Util.getBalInString(rs.getDouble("ticket_price")));
				scratchBookDataBean.setBookPrice(Util.getBalInString(rs.getDouble("book_cost")));
				scratchBookDataBean.setRetailerInvoiceId(rs.getString("ret_invoice_id"));
				scratchBookDataBean.setTotalClaimedTkts(rs.getInt("total_low_win_tier_tickets_claimed"));
				invTransitionWarehouseWiseBean.setScratchBookDataBean(scratchBookDataBean);
			}
			
			if (scratchBookDataBean != null) {
				if (!"NA".equals(scratchBookDataBean.getRetailerInvoiceId())) {
					query = "select voucher_date from st_lms_agent_receipts where receipt_id = " + Integer.parseInt(scratchBookDataBean.getRetailerInvoiceId());
					System.out.println("query    :" + query);
					rs = statement.executeQuery(query);
					if (rs.next()) {
						scratchBookDataBean.setRetailerInvoiceDate(sdt.format(rs.getTimestamp("voucher_date")));
					}
				} else {
					scratchBookDataBean.setRetailerInvoiceDate("NA");
				}

				query = "select scheme_type from st_se_invoicing_methods im inner join st_se_org_game_invoice_methods gim on im.invoice_method_id = gim.invoice_method_id where gim.game_id = " + gameId + " and gim.organization_id = " + scratchBookDataBean.getOrgId();
				System.out.println("query    :" + query);
				rs = statement.executeQuery(query);
				if (rs.next()) {
					scratchBookDataBean.setRetailerInvoiceMethod(rs.getString("scheme_type"));
				} else {
					scratchBookDataBean.setRetailerInvoiceMethod("NA");
				}

				if ("BO".equals(scratchBookDataBean.getOwnerType())) {
					scratchBookDataBean.setOwnerName(scratchBookDataBean.getWarehouseName());
				}
			} else {
				invTransitionWarehouseWiseBean.setBookLocation(0);
				return invTransitionWarehouseWiseBean;
			}

			if (currOwnerList != null && currOwnerList.size() > 1) {
				for (int iLoop = 0; iLoop < currOwnerList.size() - 1; iLoop++) {
					invTransitionWarehouseWiseDataBean = new InvTransitionWarehouseWiseDataBean();
					if ("BO".equals(currOwnerList.get(iLoop))) {
						if ("BO".equals(currOwnerList.get(iLoop + 1))) {
							if(warehouseName.get(iLoop).equals(warehouseName.get(iLoop + 1))) {

							} else {
								invTransitionWarehouseWiseDataBean.setSize((headers.indexOf(warehouseName.get(iLoop)) > headers.indexOf(warehouseName.get(iLoop + 1))) ? headers.indexOf(warehouseName.get(iLoop)) - headers.indexOf(warehouseName.get(iLoop + 1)) : headers.indexOf(warehouseName.get(iLoop + 1)) - headers.indexOf(warehouseName.get(iLoop)));
								if(headers.indexOf(warehouseName.get(iLoop)) > headers.indexOf(warehouseName.get(iLoop + 1))) {
									invTransitionWarehouseWiseDataBean.setDirection(false);
								} else {
									invTransitionWarehouseWiseDataBean.setDirection(true);
								}
								invTransitionWarehouseWiseDataBean.setLowerString(txTimeList.get(iLoop + 1));
								if(invTransitionWarehouseWiseDataBean.isDirection())
									invTransitionWarehouseWiseDataBean.setStartIndex(headers.indexOf(warehouseName.get(iLoop)));
								else {
									invTransitionWarehouseWiseDataBean.setStartIndex(headers.indexOf(warehouseName.get(iLoop + 1)));
								}
								invTransitionWarehouseWiseDataBean.setUpperString(warehouseName.get(iLoop) + " To " + warehouseName.get(iLoop + 1));
								loopList = new ArrayList<Integer>();
								for (int jLoop = 0; jLoop < headers.size() + 2 - invTransitionWarehouseWiseDataBean.getSize() + 1; jLoop++) {
									loopList.add(iLoop);
								}
								invTransitionWarehouseWiseDataBean.setLoopList(loopList);
								invTransitionWarehouseWiseDataBeans.add(invTransitionWarehouseWiseDataBean);
							}
						} else if ("AGENT".equals(currOwnerList.get(iLoop + 1))) {		//Done
							invTransitionWarehouseWiseDataBean.setSize(headers.size() - (headers.indexOf(warehouseName.get(iLoop))));
							invTransitionWarehouseWiseDataBean.setDirection(true);
							invTransitionWarehouseWiseDataBean.setLowerString(txTimeList.get(iLoop + 1));
							invTransitionWarehouseWiseDataBean.setStartIndex(headers.indexOf(warehouseName.get(iLoop)));
							invTransitionWarehouseWiseDataBean.setUpperString(warehouseName.get(iLoop) + " To Agent : " + currOwnerNameList.get(iLoop + 1));
							loopList = new ArrayList<Integer>();
							for (int jLoop = 0; jLoop < headers.size() - invTransitionWarehouseWiseDataBean.getSize() + 1 + 2; jLoop++) {
								loopList.add(iLoop);
							}
							invTransitionWarehouseWiseDataBean.setLoopList(loopList);
							invTransitionWarehouseWiseDataBeans.add(invTransitionWarehouseWiseDataBean);
						}
					} else if ("AGENT".equals(currOwnerList.get(iLoop))) {
						if ("AGENT".equals(currOwnerList.get(iLoop + 1))) {
							//Do Nothing
						} else if ("BO".equals(currOwnerList.get(iLoop + 1))) {		// Done
							invTransitionWarehouseWiseDataBean.setSize(headers.size() - (headers.indexOf(warehouseName.get(iLoop + 1))));
							invTransitionWarehouseWiseDataBean.setDirection(false);
							invTransitionWarehouseWiseDataBean.setLowerString(txTimeList.get(iLoop + 1));
							invTransitionWarehouseWiseDataBean.setStartIndex(headers.indexOf(warehouseName.get(iLoop + 1)));
							invTransitionWarehouseWiseDataBean.setUpperString("Agent : " + currOwnerNameList.get(iLoop + 1) + " To " + warehouseName.get(iLoop + 1));
							loopList = new ArrayList<Integer>();
							for (int jLoop = 0; jLoop < headers.size() - invTransitionWarehouseWiseDataBean.getSize() + 1 + 2; jLoop++) {
								loopList.add(iLoop);
							}
							invTransitionWarehouseWiseDataBean.setLoopList(loopList);
							invTransitionWarehouseWiseDataBeans.add(invTransitionWarehouseWiseDataBean);
						} else if ("RETAILER".equals(currOwnerList.get(iLoop + 1))) {		// Done
							invTransitionWarehouseWiseDataBean.setSize(1);
							invTransitionWarehouseWiseDataBean.setDirection(true);
							invTransitionWarehouseWiseDataBean.setLowerString(txTimeList.get(iLoop + 1));
							invTransitionWarehouseWiseDataBean.setStartIndex(headers.size());
							invTransitionWarehouseWiseDataBean.setUpperString("Agent : " + currOwnerNameList.get(iLoop) + " To Retailer : " + currOwnerNameList.get(iLoop + 1));
							loopList = new ArrayList<Integer>();
							for (int jLoop = 0; jLoop < headers.size() + 2; jLoop++) {
								loopList.add(iLoop);
							}
							invTransitionWarehouseWiseDataBean.setLoopList(loopList);
							
//							bookStatusString.append("Book Status " + bookStatus.get(iLoop + 1));
//							bookStatusString.append("@" + txTimeList.get(iLoop));
//
//							for (int jLoop = iLoop + 1; jLoop < currOwnerList.size(); jLoop++) {
//								if (!"RETAILER".equals(currOwnerList.get(jLoop + 1))) {
//									break;
//								} else {
//									iLoop++;
//									bookStatusString.append(" TO " + bookStatus.get(jLoop + 1) + "@" + txTimeList.get(jLoop + 1));
//								}
//							}
//							invTransitionWarehouseWiseDataBean.setBookStatusString(bookStatusString.toString());
							invTransitionWarehouseWiseDataBeans.add(invTransitionWarehouseWiseDataBean);
						}
					} else if ("RETAILER".equals(currOwnerList.get(iLoop))) {
						if ("RETAILER".equals(currOwnerList.get(iLoop + 1))) {
							//Do Nothing
						} else if ("AGENT".equals(currOwnerList.get(iLoop + 1))) { // Done
							invTransitionWarehouseWiseDataBean.setSize(1);
							invTransitionWarehouseWiseDataBean.setDirection(false);
							invTransitionWarehouseWiseDataBean.setLowerString(txTimeList.get(iLoop + 1));
							invTransitionWarehouseWiseDataBean.setStartIndex(headers.size());
							invTransitionWarehouseWiseDataBean.setUpperString("Retailer : " + currOwnerNameList.get(iLoop) + " To Agent : " + currOwnerNameList.get(iLoop + 1));
							loopList = new ArrayList<Integer>();
							for (int jLoop = 0; jLoop < headers.size() + 2; jLoop++) {
								loopList.add(iLoop);
							}
							invTransitionWarehouseWiseDataBean.setLoopList(loopList);
							invTransitionWarehouseWiseDataBeans.add(invTransitionWarehouseWiseDataBean);
						}
					}
				}
				headers.add("Agent");
				headers.add("Retailer");
				invTransitionWarehouseWiseBean.setHeaders(headers);
				invTransitionWarehouseWiseBean.setWarehouseWiseDataBeans(invTransitionWarehouseWiseDataBeans);
				invTransitionWarehouseWiseBean.setBookLocation(2);
			} else if (currOwnerList.size() == 1) {
				invTransitionWarehouseWiseBean.setBookLocation(1);
			} else if(currOwnerList.size() == 0) {
				invTransitionWarehouseWiseBean.setBookLocation(0);
			}
			System.out.println("Data Bean is " + invTransitionWarehouseWiseBean);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return invTransitionWarehouseWiseBean;
	}

	public GameTicketFormatBean getGameTicketinfo(int gameId)
			throws LMSException {
		GameTicketFormatBean ticketformatBean = null;
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();

			String ticketinfoQuery = QueryManager.getGameFormatInformation()
					+ gameId + ")";
			System.out.println("query for ticket format :: " + ticketinfoQuery);
			ResultSet rs = stmt.executeQuery(ticketinfoQuery);
			while (rs.next()) {
				ticketformatBean = new GameTicketFormatBean();
				ticketformatBean.setBookDigits(rs.getInt("book_nbr_digits"));
				ticketformatBean.setGameNbrDigits(rs.getInt("game_nbr_digits"));
				ticketformatBean.setPackDigits(rs.getInt("pack_nbr_digits"));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (con != null) {
					con.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During closing connection", se);
			}
		}
		return ticketformatBean;
	}
	
	
	public Boolean isValidUserForTrackingBook(String bookNumber, int roleId) throws LMSException{
		
		String query=null;
		Statement statement=null;
		Connection connection=null;
		ResultSet resultSet=null;
		int roleIdthroughBookNumber=0;
		Boolean isValidUser=false;
		try{
			connection=DBConnect.getConnection();
			statement=connection.createStatement();
			query="select role_id from st_lms_user_master where user_id =(select warehouse_owner_id from st_se_warehouse_master where warehouse_id = (select warehouse_id from st_se_game_inv_status where book_nbr = '"+bookNumber+"'))";
			resultSet=statement.executeQuery(query);
			if(resultSet.next()) {
				roleIdthroughBookNumber=resultSet.getInt("role_id");
			}
			if(roleIdthroughBookNumber == roleId){
				isValidUser=true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{

			try {
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During closing connection", se);
			}
		
		}
		return isValidUser;
	}
}