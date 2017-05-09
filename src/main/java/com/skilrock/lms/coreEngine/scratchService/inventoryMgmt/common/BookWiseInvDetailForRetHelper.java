package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.common.beans.OrderGameBookBeanMaster;
import com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.daoImpl.ScratchInvoiceDaoImpl;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class BookWiseInvDetailForRetHelper {
	static Log logger = LogFactory.getLog(BookWiseInvDetailForRetHelper.class);

	public Map<String,List<String>> activateBooks(int retOrgId) {
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> booksList = null;
		logger.info("---retOrgId::" + retOrgId);

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String seperator = "-";
		try {
			String selQuery = "select gis.game_id,gm.game_nbr,gm.game_name,gis.book_nbr from st_se_game_inv_status gis,st_se_game_master gm where gis.current_owner_id=? and gis.current_owner='RETAILER' and gis.game_id=gm.game_id and gis.book_status='RECEIVED_BY_RET'";
			pstmt = con.prepareStatement(selQuery);
			pstmt.setInt(1, retOrgId);
			ResultSet rs = pstmt.executeQuery();
			String gameDel = null;
			while (rs.next()) {
				gameDel = rs.getInt("game_id") + seperator
						+ rs.getInt("game_nbr") + seperator
						+ rs.getString("game_name");
				if (gameBookMap.containsKey(gameDel)) {
					booksList.add(rs.getString("book_nbr"));
				} else {
					booksList = new ArrayList<String>();
					gameBookMap.put(gameDel, booksList);
					booksList.add(rs.getString("book_nbr"));
				}

			}
			logger.info("***gameBookMap**" + gameBookMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return gameBookMap;
	}

	private Map<String, List<String>> createSeriesOfBooks(
			Map<String, List<String>> map, int booksPerPack) {
		Map<String, List<String>> seriesPackWiseMap = new TreeMap<String, List<String>>();
		String firstBook = "";
		String lastBook = "";
		List<String> bookSeriesList = null;
		List<String> bookList = null;
		int totalbooks = 0;
		Set<String> packSet = map.keySet();

		for (String pack : packSet) {
			bookList = map.get(pack);
			firstBook = bookList.get(0);
			bookSeriesList = new ArrayList<String>();
			int compTicket = Integer.parseInt(firstBook.substring(firstBook
					.indexOf("-") + 1)) + 1;
			int length = bookList.size();
			totalbooks = totalbooks + length;
			if (length == 1) {
				bookSeriesList.add(firstBook + "TO" + bookList.get(length - 1));
			} else if (length == booksPerPack) {
				bookSeriesList.add(firstBook + "TO" + bookList.get(length - 1));
			} else {
				for (int i = 0; i < length - 1; i++) {
					int newTicket = Integer.parseInt(bookList.get(i + 1)
							.substring(firstBook.indexOf("-") + 1));
					if (newTicket != compTicket) {
						lastBook = bookList.get(i);
						bookSeriesList.add(firstBook + "TO" + lastBook);

						firstBook = bookList.get(i + 1);
						compTicket = Integer.parseInt(firstBook
								.substring(firstBook.indexOf("-") + 1));
					}
					compTicket += 1;
					if (!(i + 1 < length - 1)) {
						lastBook = bookList.get(i + 1);
						bookSeriesList.add(firstBook + "TO" + lastBook);
					}
				}
			}
			seriesPackWiseMap.put(pack, bookSeriesList);
		}
		// System.out.println("Total no of books === "+totalbooks);
		return seriesPackWiseMap;
	}

	public Map<String, String> getGameMap() throws LMSException {
		Map<String, String> gameMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select game_id, game_nbr, concat(game_nbr, concat('-',game_name)) 'game_name'  from st_se_game_master order by game_nbr");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getgameList");
			while (rs.next()) {
				String gameId = rs.getInt("game_id") + "";
				String gameName = rs.getString("game_name");
				gameMap.put(gameId, gameName);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public String getTotalBooksWithOrg(int gameid, int orgId, String orgType)
			throws LMSException {
		StringBuilder resString = new StringBuilder("NONE");
		Map<String, List<String>> packWiseBookSeriesMap = null;
		Map<String, List<String>> packBooksMap = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			String agentBooksDet = "select gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from  st_se_game_inv_status gis, st_se_game_master gm, st_lms_organization_master om where  gis.current_owner_id=om.organization_id  and gm.game_id=gis.game_id and gis.current_owner = ? and om.organization_id=? and gis.game_id  = ? order by gis.book_nbr";
			// String agentBooksDet = "select gm.nbr_of_books_per_pack,
			// gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from
			// st_se_game_inv_status gis, st_se_game_master gm,
			// st_lms_organization_master om where
			// gis.current_owner_id=om.organization_id and
			// gm.game_id=gis.game_id and gis.current_owner = ? and
			// (gis.book_status = 'ACTIVE' or gis.book_status = 'INACTIVE' ) and
			// om.organization_id=? and gis.game_id = ? order by gis.book_nbr";
			pstmt = con.prepareStatement(agentBooksDet);
			pstmt.setString(1, orgType);
			pstmt.setInt(2, orgId);
			pstmt.setInt(3, gameid);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("QUERY IS ===" + pstmt + "\n\n\n");
			String bookNbr = "";
			boolean first = true;
			String packNbr = "";
			String newPackNbr = "";
			int booksPerPack = -1;

			while (rs.next()) {
				booksPerPack = rs.getInt("nbr_of_books_per_pack");
				newPackNbr = rs.getString("pack_nbr");
				bookNbr = rs.getString("book_nbr");
				if (packNbr.equals(newPackNbr)) {
					list.add(bookNbr);
				} else {
					if (!first) {
						packBooksMap.put(packNbr, list);
					}
					list = new ArrayList<String>();
					packNbr = newPackNbr;
					list.add(bookNbr);
				}
				first = false;
			}
			packBooksMap.put(packNbr, list);
			rs.close();

			packBooksMap.remove("");

			// this method return the series of books on based of packs
			packWiseBookSeriesMap = createSeriesOfBooks(packBooksMap,
					booksPerPack);

			first = true;
			if (packWiseBookSeriesMap.size() > 0) {
				resString = new StringBuilder("");
				Set<String> packs = packWiseBookSeriesMap.keySet();
				for (String pack : packs) {
					if (first) {
						first = false;
					} else {
						resString.append("pack");
					}
					resString.append(pack);
					resString.append("book");
					// System.out.println(pack+", No of books
					// ="+(packBooksMap.get(pack)).size());
					List<String> bookSeriesList = packWiseBookSeriesMap
							.get(pack);
					String books = bookSeriesList.toString().replace("[", "")
							.replace("]", "");
					// System.out.println("books === "+books);
					resString.append(books);

				}
				// System.out.println("========================================");

			}
			System.out.println(" response  String  === " + resString);
			// generate the book_nbr from series
			// generateSeries(packWiseBookSeriesMap);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resString.toString();
	}

	public String[] updateBooks(int userOrgId,int userId, List<String> bookNumberList) throws LMSException {
		logger.info("---retOrgId::" + userOrgId);

		Connection con = null;
		PreparedStatement pStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		String invoiceReceipt = null;
		String[] response = new String[3];
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			String bookNumberStr = bookNumberList.toString().replace("[", "'").replace("]", "'").replace(", ", "','");
			logger.info("***bookNumberStr**" + bookNumberStr);
			if(!bookNumberStr.contains("-"))
				bookNumberStr = bookNumberStr.substring(0,4) + "-" + bookNumberStr.substring(4);
			pStmt = con.prepareStatement("insert into st_se_game_inv_detail(game_id, pack_nbr, book_nbr, current_owner, current_owner_id, changed_by_user_id, book_status, warehouse_id, agent_invoice_id, ret_invoice_id, transaction_at, transaction_date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			Map<Integer, List<String>> gameBookMap = new HashMap<Integer, List<String>>();
			List<String> bookList = null;

			stmt = con.createStatement();
			rs = stmt.executeQuery("select game_id, pack_nbr, book_nbr, current_owner, current_owner_id, book_status, warehouse_id, agent_invoice_id, ret_invoice_id from st_se_game_inv_status where book_nbr in(" + bookNumberStr + ");");
			while(rs.next()) {
				int gameId = rs.getInt("game_id");
				bookList = gameBookMap.get(gameId);
				if(bookList == null) {
					bookList = new ArrayList<String>();
					gameBookMap.put(gameId, bookList);
				}
				bookList.add(rs.getString("book_nbr"));

				pStmt.setInt(1, gameId);
				pStmt.setString(2, rs.getString("pack_nbr"));
				pStmt.setString(3, rs.getString("book_nbr"));
				pStmt.setString(4, rs.getString("current_owner"));
				pStmt.setInt(5, rs.getInt("current_owner_id"));
				pStmt.setInt(6, userId);
				pStmt.setString(7, rs.getString("book_status"));
				pStmt.setInt(8, rs.getInt("warehouse_id"));
				pStmt.setInt(9, rs.getInt("agent_invoice_id"));
				pStmt.setInt(10, rs.getInt("ret_invoice_id"));
				pStmt.setString(11, "RETAILER");
				pStmt.setString(12, Util.getCurrentTimeString());
				pStmt.executeUpdate();
			}

			Map<Integer, OrderGameBookBeanMaster> invoiceMap = new HashMap<Integer, OrderGameBookBeanMaster>();
			String query = null;
			for(Map.Entry<Integer, List<String>> entry : gameBookMap.entrySet()) {
				int gameId = entry.getKey();
				bookList = entry.getValue();

				OrderGameBookBeanMaster masterBean = null;
				query = "SELECT scheme_type, invoice_method_value FROM st_se_invoicing_methods im INNER JOIN st_se_org_game_invoice_methods gim ON im.invoice_method_id=gim.invoice_method_id WHERE gim.organization_id="+userOrgId+" AND gim.game_id="+gameId+";";
				logger.info("Query - "+query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					if("ON_BOOK_ACTIVATION_RET".equals(rs.getString("scheme_type")) && "YES".equals(rs.getString("invoice_method_value"))) {
						masterBean = new OrderGameBookBeanMaster();
						masterBean.setBookList(bookList);
						invoiceMap.put(gameId, masterBean);
					}
				}
			}
			if(invoiceMap.size() > 0) {
				int invoiceId = 0;
				
				ScratchInvoiceDaoImpl daoImpl = new ScratchInvoiceDaoImpl();

				daoImpl.checkAndUpdateForInvoice(invoiceMap, con);
				if(invoiceMap.size() > 0) {
					daoImpl.generateInvoiceForAgent(userOrgId, invoiceMap, null, con);
					invoiceId = daoImpl.generateInvoiceForRetailer(userOrgId, invoiceMap, null, con);
				}

				if(invoiceId > 0) {
					invoiceReceipt = daoImpl.getInvoiceReceiptFromInvoiceId("AGENT", invoiceId, con);
					response[0] = String.valueOf(invoiceId);
					response[1] = invoiceReceipt;
				}
			}

			String updateBookStatus = "update st_se_game_inv_status set book_status = ?, book_activation_date_ret = ? where current_owner_id = ? and book_nbr in(" + bookNumberStr + ")";
			PreparedStatement updateBookStatusPstmt = con.prepareStatement(updateBookStatus);
			updateBookStatusPstmt.setString(1, "ACTIVE");
			updateBookStatusPstmt.setTimestamp(2, Util.getCurrentTimeStamp());
			updateBookStatusPstmt.setInt(3, userOrgId);
			int retBalRow = updateBookStatusPstmt.executeUpdate();
			logger.info(retBalRow + " row updated,  updateBookStatus = " + updateBookStatusPstmt);
			con.commit();
			response[2]="SUCCESS";
			if(invoiceReceipt!=null && invoiceReceipt.length()>0) {
				con.setAutoCommit(true);
				String firstName=null;
				String lastName=null;
				String emailId=null;
				query="select first_name,last_name,email_id from st_lms_user_contact_details where user_id="+userId+";";
				rs=stmt.executeQuery(query);
				if(rs.next()){
					firstName = rs.getString("first_name");
					lastName=rs.getString("last_name");
					emailId=rs.getString("email_id");
				}

				String books = null;
				for (String bookNbr : bookNumberList) 
					 books = bookNbr+",";
				books = books.substring(0, books.length()-1);

				String content = "Your invoice raised.<br /><br /> Book Number : <b>"+books+"</b><br />Invoice Receipt Id : <b>"+invoiceReceipt+"</b>";
				MessageInboxBean messageBean = new MessageInboxBean();
				messageBean.setMessageTypeId(8);
				messageBean.setMessageSubject("Invoice Raised - "+invoiceReceipt);
				messageBean.setMessageContent(content);
				messageBean.setExpiryPeriod(null);
				messageBean.setIsPopup(true);
				messageBean.setIsMandatory(true);
				messageBean.setUserId(userId);
				String[] organizationList = new String[]{String.valueOf(userId)};
				MessageInboxDaoImpl.getSingleInstance().addNewMessage(messageBean, organizationList, con);

				new CommonFunctionsHelper().sendMail(bookNumberList,firstName,lastName,emailId,invoiceReceipt);
			}
			
		}catch (ScratchException se) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}  catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e){
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeCon(con);
		}

		return response;
	}

	public String validateBookAndStatus(int userId, String bookNumber) throws SQLException {
		String returnValue = null;

		Connection connection = DBConnect.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT book_nbr, book_status FROM st_se_game_inv_status WHERE current_owner_id="+userId+" AND REPLACE(book_nbr,'-','')='"+bookNumber+"';");
		if(rs.next()) {
			if(!("INACTIVE".equals(rs.getString("book_status")) || "RECEIVED_BY_RET".equals(rs.getString("book_status")))) {
				returnValue = rs.getString("book_status").toUpperCase();
			} else {
				returnValue = rs.getString("book_nbr");
			}
		} else {
			returnValue = "NO_BOOK_FOUND";
		}

		return returnValue;
	}
	public String checkAndReturnBookNumberFromTicketNumber(String ticketNumber) throws SQLException {
		String bookNumber = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		int ticketNumberDigits = 0;
		int packNumberDigits = 0;
		int bookNumberDigits = 0;
		int gameNumberDigits = 0;
		try{
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			String bookNumberAfterRemovalOfGameNumber = ticketNumber.substring(0,3);
			String fetchTicketNumberDigitQuery = "select pack_nbr_digits,book_nbr_digits,ticket_nbr_digits,game_nbr_digits from st_se_game_ticket_nbr_format a inner join st_se_game_master b on a.game_id = b.game_id where b.game_nbr = "+bookNumberAfterRemovalOfGameNumber;
			logger.info("inside checkAndReturnBookNumberFromTicketNumber with ticketNumber"+ "fetchTicketNumberDigitQuery :-"+fetchTicketNumberDigitQuery);
			rs = stmt.executeQuery(fetchTicketNumberDigitQuery);
			if(rs.next()) {
				ticketNumberDigits = rs.getInt("ticket_nbr_digits");
				packNumberDigits = rs.getInt("pack_nbr_digits");
				bookNumberDigits = rs.getInt("book_nbr_digits");
				gameNumberDigits = rs.getInt("game_nbr_digits");
			}
			if(ticketNumber.length() == (ticketNumberDigits +packNumberDigits+bookNumberDigits+gameNumberDigits)){
				bookNumber =  ticketNumber.substring(0,(ticketNumber.length()-ticketNumberDigits));
			}else if(ticketNumber.length() == (packNumberDigits+bookNumberDigits+gameNumberDigits)){
				bookNumber =  ticketNumber;
			}
		}catch (Exception e) {
			logger.error(e);
		}finally {
			DBConnect.closeResource(connection,stmt,rs);
		}
		
		return bookNumber;
	}
}
