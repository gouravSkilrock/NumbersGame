/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.AgentOrderBean;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSaleBean;
import com.skilrock.lms.beans.InvOrderBean;
import com.skilrock.lms.beans.OrderBean;
import com.skilrock.lms.beans.OrderedGameBean;
import com.skilrock.lms.beans.OrgAddressBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.scratchService.dataMgmt.daoImpl.ScratchGameDataDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This is a helper class providing methods handling the order process at Agent
 * end
 * 
 * @author Skilrock Technologies
 * 
 */

public class AgentOrderProcessHelper {
	private int gameID;
	private int totalOrderedBooks;

	/**
	 * This method is used for dispatching the retailer order
	 * 
	 * @param invOrderList
	 * @param orderId
	 * @param retOrgId
	 * @param agtId
	 * @param rootPath
	 * @param loggedInUserOrgName
	 * @throws LMSException
	 */
	/*public void dispatchOrder(List<InvOrderBean> invOrderList, int total,
			int orderId, int retOrgId, int agtId, String rootPath,
			String loggedInUserOrgName, int userOrgID, HttpSession session)
			throws LMSException {

		InvOrderBean invOrderBean = null;
		OrderedGameBean gameBean = null;

		// check if retailer online

		// String
		// isretOnline=(String)ServletActionContext.getServletContext().getAttribute("RET_ONLINE");
		Connection connection = null;
		PreparedStatement govtCommChangeHistoryPstmt = null;
		String govtCommChangeHistoryQuery = "select count(*) from st_se_game_govt_comm_history where ";

		if (invOrderList != null) {

			PreparedStatement agtMasterStmt = null;
			PreparedStatement LMSMasterStmt = null;
			PreparedStatement agtAgentStmt = null;
			PreparedStatement agtOrdGameStmt = null;
			PreparedStatement agtReceiptStmt = null;
			PreparedStatement agtReceiptNoGenStmt = null;
			PreparedStatement agtReceiptMappingStmt = null;
			PreparedStatement agtOrderStmt = null;
			PreparedStatement agtInvoiceDCMappingStmt = null;

			PreparedStatement gameInvStatusStmt = null;
			PreparedStatement gameInvDetailStmt = null;
			PreparedStatement orderInvoicesPrtSt = null;

			PreparedStatement getBookFromPackPstmt = null;
			PreparedStatement isGovtCommChngdPstmt = null;

			ResultSet rsMaster = null;
			Statement totalDispatch = null;
			ResultSet totalDispatchResult = null;
			String orderInvoicesQuery = null;
			String agtMasterQuery = null;
			String LMSMasterQuery = null;
			String agtAgentQuery = null;
			String agtOrdGamesQuery = null;
			String agtReceiptMappingQuery = null;
			String agtOrderQuery = null;
			String gameInvStatusQuery = null;
			String invQuery = null;

			String gameInvDetailQuery = null;
			String getBookfromPackQuery = null;
			String isGovtCommChangedQuery = null;

			String countDispatchedTotal = "select nbr_of_books_dlvrd from st_se_agent_ordered_games where order_id="
					+ orderId;
			List<Integer> trnIdList = new ArrayList<Integer>();
			int masterTrnId = -1;
			int totalDispatchedForOrder = 0;

			try {

				 
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);

				agtMasterQuery = QueryManager.insertInAgentTransactionMaster();
				agtMasterStmt = connection.prepareStatement(agtMasterQuery);

				LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
				LMSMasterStmt = connection.prepareStatement(LMSMasterQuery);

				agtAgentQuery = QueryManager.getST1AgentRetQuery();
				agtAgentStmt = connection.prepareStatement(agtAgentQuery);

				agtOrdGamesQuery = QueryManager.getST1AgtOrdGamesUpdQuery();
				agtOrdGameStmt = connection.prepareStatement(agtOrdGamesQuery);

				java.sql.Timestamp currentDate = new java.sql.Timestamp(
						new Date().getTime());

				// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
				// agtReceiptStmt =
				// connection.prepareStatement(agtReceiptQuery);

				agtReceiptMappingQuery = QueryManager
						.insertAgentReceiptTrnMapping();
				agtReceiptMappingStmt = connection
						.prepareStatement(agtReceiptMappingQuery);

				agtOrderQuery = QueryManager.getST1AgtOrdUpdQuery();
				agtOrderStmt = connection.prepareStatement(agtOrderQuery);

				gameInvStatusQuery = QueryManager
						.getST1AgtUpdGameInvStatusQuery();

				gameInvDetailQuery = QueryManager.getST1InvDetailInsertQuery();
				gameInvDetailStmt = connection
						.prepareStatement(gameInvDetailQuery);

				orderInvoicesQuery = QueryManager
						.getST1AgentOrderInInsertQuery();
				orderInvoicesPrtSt = connection
						.prepareStatement(orderInvoicesQuery);

				getBookfromPackQuery = "select book_nbr from st_se_game_inv_status where pack_nbr=?";
				getBookFromPackPstmt = connection
						.prepareStatement(getBookfromPackQuery);

				isGovtCommChangedQuery = "select count(*) from st_se_game_govt_comm_history where game_id=?";
				isGovtCommChngdPstmt = connection
						.prepareStatement(isGovtCommChangedQuery);

				// added by arun
				String fetchPurRateQuery = "select transacrion_sale_comm_rate from st_se_game_inv_detail where "
						+ "book_nbr=? and transaction_at='BO' order by transaction_date desc limit 1";
				PreparedStatement fetchPurRatePstmt = connection
						.prepareStatement(fetchPurRateQuery);
				ResultSet fetchPurRateRs = null;
				double purRate = 0.0;

				String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
						+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
						+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement detHistoryInsPstmt = connection
						.prepareStatement(detHistoryInsQuery);

				// check book Status on dispatch
				String newBookStatus = "ACTIVE";

				double mrpAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double vatAmt = 0.0;
				double taxableSale = 0.0;
				double retSaleCommRate = 0.0;
				double creditAmt = 0.0;
				int nbrOfbooksApp = 0;
				int nbrBooksalreadyDispatched = 0;
				int nbrOfBooksDispatched = 0;

				totalDispatch = connection.createStatement();
				totalDispatchResult = totalDispatch
						.executeQuery(countDispatchedTotal);
				while (totalDispatchResult.next()) {
					totalDispatchedForOrder = totalDispatchedForOrder
							+ totalDispatchResult.getInt("nbr_of_books_dlvrd");

				}
				System.out.println("--------totalDispatchedForOrder"
						+ totalDispatchedForOrder + "-------");

				int gameId = -1;
				List<PackBean> packList = null;
				List<BookBean> bookList = null;
				PackBean packBean = null;
				BookBean bookBean = null;

				// insert in receipt master for invoice

				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet rs = agtReceiptStmt.getGeneratedKeys();
				int invoiceId = -1;
				while (rs.next()) {
					invoiceId = rs.getInt(1);
				}

				// insert in receipt master for delevery challan
				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet DCrs = agtReceiptStmt.getGeneratedKeys();
				int DCId = -1;
				while (DCrs.next()) {
					DCId = DCrs.getInt(1);
				}

				for (int i = 0; i < invOrderList.size(); i++) {

					boolean isGovtCommChanged = false;
					invOrderBean = invOrderList.get(i);

					gameBean = invOrderBean.getOrderedGameBean();
					gameId = gameBean.getGameId();

					// fetch game details from game master
					String fetchGameDetQuery = "select nbr_of_tickets_per_book from st_se_game_master where game_id ="
							+ gameId;
					Statement fetchGameDetStmt = connection.createStatement();
					ResultSet fetchGameDetRs = fetchGameDetStmt
							.executeQuery(fetchGameDetQuery);
					int noOfTktPerBooks = -1;
					if (fetchGameDetRs.next()) {
						noOfTktPerBooks = fetchGameDetRs
								.getInt("nbr_of_tickets_per_book");
					}

					// check if govt comm of this game is changed or not in govt
					// comm history table
					isGovtCommChngdPstmt.setInt(1, gameId);
					ResultSet isGovtCommChngdRs = isGovtCommChngdPstmt
							.executeQuery();
					if (isGovtCommChngdRs.next()) {
						if (isGovtCommChngdRs.getInt(1) > 1) {
							isGovtCommChanged = true;
						}
					}
					if (isGovtCommChanged == false) {

						System.out
								.println(" >>>>>>>>>>>>>>>>>>>>>>  :: inside false ");

						nbrOfbooksApp = gameBean.getNbrOfBooksApp();
						nbrBooksalreadyDispatched = gameBean
								.getNbrOfBooksDlvrd();
						int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();

						nbrOfBooksDispatched = nbrBooksalreadyDispatched
								+ getDispatchedNbrOfBooks(invOrderBean,
										nbrOfBooksPerPack);
						totalDispatchedForOrder = totalDispatchedForOrder
								+ getDispatchedNbrOfBooks(invOrderBean,
										nbrOfBooksPerPack);
						int currentlyDispatched = getDispatchedNbrOfBooks(
								invOrderBean, nbrOfBooksPerPack);
						System.out
								.println("No of book already dispatched at the time of dispatch for this game"
										+ nbrBooksalreadyDispatched
										+ "------------");
						System.out
								.println("No of book just dispatched at the time of dispatch"
										+ nbrOfBooksDispatched + "------------");
						// System.out.println("tital diapatcehd
						// now"+totalDispatched+"------------");
						System.out.println("total approved books" + total
								+ "------------");

						// insert in LMS transaction master
						LMSMasterStmt.setString(1, "AGENT");
						LMSMasterStmt.executeUpdate();
						rsMaster = null;
						rsMaster = LMSMasterStmt.getGeneratedKeys();

						while (rsMaster.next()) {
							masterTrnId = rsMaster.getInt(1);
							trnIdList.add(masterTrnId);
						}

						// insert in agent transaction master

						agtMasterStmt.setInt(1, masterTrnId);
						agtMasterStmt.setInt(2, agtId);
						agtMasterStmt.setInt(3, userOrgID);
						agtMasterStmt.setString(4, "RETAILER");
						agtMasterStmt.setInt(5, retOrgId);
						agtMasterStmt.setString(6, "SALE");
						agtMasterStmt.setTimestamp(7, currentDate);

						
						 * agtMasterStmt.setInt(1,agtId);
						 * agtMasterStmt.setInt(2,retOrgId);
						 * agtMasterStmt.setString(3,"SALE");
						 * agtMasterStmt.setTimestamp(4,currentDate);
						 

						agtMasterStmt.execute();

						agtAgentStmt.setInt(1, masterTrnId);
						agtAgentStmt.setInt(2, gameId);
						agtAgentStmt.setInt(3, agtId);
						agtAgentStmt.setInt(4, retOrgId);

						mrpAmt = currentlyDispatched * gameBean.getBookPrice();
						agtAgentStmt.setDouble(5, mrpAmt);

						retSaleCommRate = gameBean.getRetSaleCommRate()
								+ gameBean.getRetGameCommVariance();
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						agtAgentStmt.setDouble(6, commAmt);

						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						// added by yogehs to calculate vat amount

						vatAmt = CommonMethods.calculateVat(mrpAmt,
								retSaleCommRate,
								gameBean.getPrizePayOutRatio(), gameBean
										.getGovtComm(), gameBean.getVat(),
								gameBean.getGovtCommRule(), gameBean
										.getFixedAmt(), gameBean
										.getTicketsInScheme());
						// taxableSale=(((mrpAmt*(100-(retSaleCommRate +
						// gameBean.getPrizePayOutRatio()
						// +gameBean.getGovtComm())))/100)*100)/(100+gameBean.getVat());
						// taxableSale=vatAmt/(gameBean.getVat() * 0.01);
						taxableSale = CommonMethods.calTaxableSale(mrpAmt,
								retSaleCommRate,
								gameBean.getPrizePayOutRatio(), gameBean
										.getGovtComm(), gameBean.getVat());

						agtAgentStmt.setDouble(7, netAmt);
						agtAgentStmt.setString(8, "SALE");
						agtAgentStmt.setInt(9, currentlyDispatched);
						agtAgentStmt.setDouble(10, vatAmt);
						agtAgentStmt.setDouble(11, taxableSale);
						agtAgentStmt.setInt(12, userOrgID);
						agtAgentStmt.execute();

						agtOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						agtOrdGameStmt.setInt(2, orderId);
						agtOrdGameStmt.setInt(3, gameId);

						agtOrdGameStmt.execute();

						packList = invOrderBean.getPackList();
						if (packList != null) {
							invQuery = gameInvStatusQuery
									+ " and pack_nbr = ? ";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String packNbr = null;

							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();

								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);

								// if(isretOnline.equals("YES"))
								// {
								// gameInvStatusStmt.setString(3,"ACTIVE");
								
								 * } else {
								 * gameInvStatusStmt.setString(3,"ACTIVE"); }
								 

								gameInvStatusStmt.setString(3, newBookStatus);

								// Added by arun on update status table

								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus
										.trim())) {
									gameInvStatusStmt
											.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								// ----------------

								gameInvStatusStmt.setInt(6, gameId);
								gameInvStatusStmt.setString(7, packNbr);
								gameInvStatusStmt.execute();

								List<String> books = getBooksForPack(
										connection, gameId, packNbr);

								for (int k = 0; k < books.size(); k++) {
									gameInvDetailStmt.setInt(1, masterTrnId);
									gameInvDetailStmt.setInt(2, gameId);
									gameInvDetailStmt.setString(3, packNbr);
									gameInvDetailStmt
											.setString(4, books.get(k));
									gameInvDetailStmt.setString(5, "RETAILER");
									gameInvDetailStmt.setInt(6, retOrgId);
									gameInvDetailStmt.setTimestamp(7,
											currentDate);
									gameInvDetailStmt.setString(8, "AGENT");
									gameInvDetailStmt.setDouble(9,
											retSaleCommRate);
									gameInvDetailStmt.setDouble(10, gameBean
											.getGovtComm());
									gameInvDetailStmt.setObject(11, purRate);
									gameInvDetailStmt.execute();

									// insert into detail history table Added by
									// arun
									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, books
											.get(k));
									detHistoryInsPstmt.setString(3, "RETAILER");
									detHistoryInsPstmt.setInt(4, retOrgId);
									detHistoryInsPstmt.setTimestamp(5,
											currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, agtId);
									detHistoryInsPstmt.setInt(8,
											noOfTktPerBooks);
									if ("ACTIVE".equalsIgnoreCase(newBookStatus
											.trim())) {
										detHistoryInsPstmt.setInt(9,
												noOfTktPerBooks);
									} else {
										detHistoryInsPstmt.setInt(9, 0);
									}
									detHistoryInsPstmt.setInt(10, 0);
									detHistoryInsPstmt.setString(11,
											newBookStatus);
									detHistoryInsPstmt.execute();

								}

							}
						}

						bookList = invOrderBean.getBookList();
						if (bookList != null) {
							invQuery = gameInvStatusQuery + " and book_nbr = ?";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);

								// Added by arun on update status table
								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus
										.trim())) {
									gameInvStatusStmt
											.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								// ----------------

								gameInvStatusStmt.setInt(6, gameId);
								gameInvStatusStmt.setString(7, bookNbr);
								gameInvStatusStmt.execute();

								// added by arun
								fetchPurRatePstmt.setString(1, bookNbr);
								fetchPurRateRs = fetchPurRatePstmt
										.executeQuery();

								if (fetchPurRateRs.next()) {
									purRate = fetchPurRateRs
											.getDouble("transacrion_sale_comm_rate");
								}

								packNbr = getPackNbrForBook(connection, gameId,
										bookNbr);

								gameInvDetailStmt.setInt(1, masterTrnId);
								gameInvDetailStmt.setInt(2, gameId);
								gameInvDetailStmt.setString(3, packNbr);
								gameInvDetailStmt.setString(4, bookNbr);
								gameInvDetailStmt.setString(5, "RETAILER");
								gameInvDetailStmt.setInt(6, retOrgId);
								gameInvDetailStmt.setTimestamp(7, currentDate);
								gameInvDetailStmt.setString(8, "AGENT");
								gameInvDetailStmt.setDouble(9, retSaleCommRate);
								gameInvDetailStmt.setDouble(10, gameBean
										.getGovtComm());
								gameInvDetailStmt.setObject(11, purRate);
								gameInvDetailStmt.execute();

								// insert into detail history table Added by
								// arun
								detHistoryInsPstmt.setInt(1, gameId);
								detHistoryInsPstmt.setString(2, bookNbr);
								detHistoryInsPstmt.setString(3, "RETAILER");
								detHistoryInsPstmt.setInt(4, retOrgId);
								detHistoryInsPstmt.setTimestamp(5, currentDate);
								detHistoryInsPstmt.setInt(6, userOrgID);
								detHistoryInsPstmt.setInt(7, agtId);
								detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus
										.trim())) {
									detHistoryInsPstmt.setInt(9,
											noOfTktPerBooks);
								} else {
									detHistoryInsPstmt.setInt(9, 0);
								}
								detHistoryInsPstmt.setInt(10, 0);
								detHistoryInsPstmt.setString(11, newBookStatus);
								detHistoryInsPstmt.execute();

							}
						}

						if (total == totalDispatchedForOrder) {

							agtOrderStmt.setString(1, "PROCESSED");
							agtOrderStmt.setInt(2, orderId);

							agtOrderStmt.executeUpdate();
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.executeUpdate();
							System.out
									.println("Total aaproved and total dispatched are equal----PROCESSED");
						} else {
							agtOrderStmt.setString(1, "SEMI_PROCESSED");
							agtOrderStmt.setInt(2, orderId);
							agtOrderStmt.executeUpdate();
							System.out.println("In the Semi processed block");
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "SEMI_PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.executeUpdate();
						}

					}

					else {
						System.out
								.println(" >>>>>>>>>>>>>>>>>>>>>>  :: inside true ");

						nbrOfbooksApp = gameBean.getNbrOfBooksApp();
						nbrBooksalreadyDispatched = gameBean
								.getNbrOfBooksDlvrd();
						int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();

						nbrOfBooksDispatched = nbrBooksalreadyDispatched
								+ getDispatchedNbrOfBooks(invOrderBean,
										nbrOfBooksPerPack);
						totalDispatchedForOrder = totalDispatchedForOrder
								+ getDispatchedNbrOfBooks(invOrderBean,
										nbrOfBooksPerPack);
						int currentlyDispatched = getDispatchedNbrOfBooks(
								invOrderBean, nbrOfBooksPerPack);

						mrpAmt = currentlyDispatched * gameBean.getBookPrice();
						retSaleCommRate = gameBean.getRetSaleCommRate()
								+ gameBean.getRetGameCommVariance();
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						packList = invOrderBean.getPackList();
						bookList = invOrderBean.getBookList();

						if (packList != null) {
							String packNbr = null;

							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();
								getBookFromPackPstmt.setString(1, packNbr);
								ResultSet rsBookFrmPack = getBookFromPackPstmt
										.executeQuery();
								while (rsBookFrmPack.next()) {
									bookBean = new BookBean();
									String bookNbrfromPack = rsBookFrmPack
											.getString("book_nbr");
									bookBean.setBookNumber(bookNbrfromPack);
									// here u have to set book status also
									// in book bean
									bookList.add(bookBean);
								}

							}

						}
						ResultSet rsCommVar;
						ArrayList<BookSaleBean> bookSaleBeanList = new ArrayList<BookSaleBean>();
						BookSaleBean bookSaleBean;
						for (int bookListSize = 0; bookListSize < bookList
								.size(); bookListSize++) {
							bookSaleBean = new BookSaleBean();
							double govtCommRate = 0.0;
							double vatAmtForBook = 0.0;
							double taxableSaleForbook = 0.0;
							bookBean = bookList.get(bookListSize);
							String bookfromBookList = bookBean.getBookNumber();
							String govtCommVarianceQuery = "select transaction_gov_comm_rate,transaction_date from st_se_game_inv_detail where current_owner=? and current_owner_id=? and game_id=? and book_nbr=? and transaction_at=? order by transaction_date desc limit 1 ";
							PreparedStatement govtCommVariaceStmt = connection
									.prepareStatement(govtCommVarianceQuery);
							govtCommVariaceStmt.setString(1, "AGENT");
							govtCommVariaceStmt.setInt(2, userOrgID);
							govtCommVariaceStmt.setInt(3, gameId);
							govtCommVariaceStmt.setString(4, bookfromBookList);
							govtCommVariaceStmt.setString(5, "BO");
							rsCommVar = govtCommVariaceStmt.executeQuery();
							while (rsCommVar.next()) {
								govtCommRate = rsCommVar
										.getDouble("transaction_gov_comm_rate");
							}

							vatAmtForBook = CommonMethods.calculateVat(gameBean
									.getBookPrice(), retSaleCommRate, gameBean
									.getPrizePayOutRatio(), govtCommRate,
									gameBean.getVat(), gameBean
											.getGovtCommRule(), gameBean
											.getFixedAmt(), gameBean
											.getTicketsInScheme());
							// taxableSaleForbook=(((gameBean.getBookPrice()*(100-(retSaleCommRate
							// + gameBean.getPrizePayOutRatio()
							// +govtCommRate)))/100)*100)/(100+gameBean.getVat());
							// taxableSaleForbook=vatAmtForBook/(gameBean.getVat()
							// * 0.01);
							taxableSaleForbook = CommonMethods.calTaxableSale(
									gameBean.getBookPrice(), retSaleCommRate,
									gameBean.getPrizePayOutRatio(),
									govtCommRate, gameBean.getVat());

							bookSaleBean.setBookNumber(bookfromBookList);
							bookSaleBean.setBookTaxableSale(taxableSaleForbook);
							bookSaleBean.setBookVatAmt(vatAmtForBook);
							bookSaleBean.setTotalSaleGovtComm(govtCommRate);
							bookSaleBeanList.add(bookSaleBean);
						}

						Set<Double> GovtCommVarianceSet = new HashSet<Double>();
						String queryGovtCommVarHistory = "select DISTINCT govt_comm_rate from st_se_game_govt_comm_history where game_id="
								+ gameId;
						PreparedStatement stmtGovtCommVarHistory = connection
								.prepareStatement(queryGovtCommVarHistory);
						ResultSet rsGovtCommVarHistory = stmtGovtCommVarHistory
								.executeQuery();
						while (rsGovtCommVarHistory.next()) {
							GovtCommVarianceSet.add(rsGovtCommVarHistory
									.getDouble("govt_comm_rate"));
						}

						Iterator govtCommSetItr;
						Iterator bookSaleBeanItr;
						govtCommSetItr = GovtCommVarianceSet.iterator();

						while (govtCommSetItr.hasNext()) {
							boolean bookGovtCommVarMatch = false;
							bookSaleBeanItr = bookSaleBeanList.iterator();
							List bookListforSingleTrn = null;
							bookListforSingleTrn = new ArrayList<String>();

							double totVatAmt = 0.0;
							double totTaxableSale = 0.0;
							double totMrpAmt = 0.0;
							double totalCommAmt = 0.0;
							double totalNetAmt = 0.0;

							Double govtCommFormSet = (Double) govtCommSetItr
									.next();

							while (bookSaleBeanItr.hasNext()) {
								bookSaleBean = (BookSaleBean) bookSaleBeanItr
										.next();
								// System.out.println("compare comm ::: " +
								// bookSaleBean.getTotalSaleGovtComm() + " :: "
								// + govtCommFormSet);
								if (bookSaleBean.getTotalSaleGovtComm() == govtCommFormSet) {
									bookGovtCommVarMatch = true;
									totVatAmt = totVatAmt
											+ bookSaleBean.getBookVatAmt();
									totTaxableSale = totTaxableSale
											+ bookSaleBean.getBookTaxableSale();
									totMrpAmt = totMrpAmt
											+ gameBean.getBookPrice();
									bookListforSingleTrn.add(bookSaleBean
											.getBookNumber());
								}

							}

							totalCommAmt = totMrpAmt * retSaleCommRate * 0.01;
							totalNetAmt = totMrpAmt - totalCommAmt;
							if (bookGovtCommVarMatch) {

								// insert in LMS transaction master
								LMSMasterStmt.setString(1, "AGENT");
								LMSMasterStmt.executeUpdate();

								rsMaster = null;
								rsMaster = LMSMasterStmt.getGeneratedKeys();

								while (rsMaster.next()) {
									masterTrnId = rsMaster.getInt(1);
									trnIdList.add(masterTrnId);
								}

								// insert in agent transaction master

								agtMasterStmt.setInt(1, masterTrnId);
								agtMasterStmt.setInt(2, agtId);
								agtMasterStmt.setInt(3, userOrgID);
								agtMasterStmt.setString(4, "RETAILER");
								agtMasterStmt.setInt(5, retOrgId);
								agtMasterStmt.setString(6, "SALE");
								agtMasterStmt.setTimestamp(7, currentDate);
								agtMasterStmt.execute();

								agtAgentStmt.setInt(1, masterTrnId);
								agtAgentStmt.setInt(2, gameId);
								agtAgentStmt.setInt(3, agtId);
								agtAgentStmt.setInt(4, retOrgId);

								// mrpAmt = currentlyDispatched *
								// gameBean.getBookPrice();
								agtAgentStmt.setDouble(5, totMrpAmt);

								// retSaleCommRate=gameBean.getRetSaleCommRate()+gameBean.getRetGameCommVariance();
								// commAmt = mrpAmt * retSaleCommRate * 0.01 ;
								agtAgentStmt.setDouble(6, totalCommAmt);
								agtAgentStmt.setDouble(7, totalNetAmt);
								agtAgentStmt.setString(8, "SALE");
								agtAgentStmt.setInt(9, bookListforSingleTrn
										.size());
								agtAgentStmt.setDouble(10, totVatAmt);
								agtAgentStmt.setDouble(11, totTaxableSale);
								agtAgentStmt.setInt(12, userOrgID);

								agtAgentStmt.execute();

								for (int j = 0; j < bookListforSingleTrn.size(); j++) {
									String bknbr = (String) bookListforSingleTrn
											.get(j);
									String pkNbr = getPackNbrForBook(
											connection, gameId, bknbr);

									// added by arun
									fetchPurRatePstmt.setString(1, bknbr);
									fetchPurRateRs = fetchPurRatePstmt
											.executeQuery();

									if (fetchPurRateRs.next()) {
										purRate = fetchPurRateRs
												.getDouble("transacrion_sale_comm_rate");
									}

									gameInvDetailStmt.setInt(1, masterTrnId);
									gameInvDetailStmt.setInt(2, gameId);
									gameInvDetailStmt.setString(3, pkNbr);
									gameInvDetailStmt.setString(4, bknbr);
									gameInvDetailStmt.setString(5, "RETAILER");
									gameInvDetailStmt.setInt(6, retOrgId);
									gameInvDetailStmt.setTimestamp(7,
											currentDate);
									gameInvDetailStmt.setString(8, "AGENT");
									gameInvDetailStmt.setDouble(9,
											retSaleCommRate);
									gameInvDetailStmt.setDouble(10,
											govtCommFormSet);
									gameInvDetailStmt.setObject(11, purRate);
									gameInvDetailStmt.execute();

									// insert into detail history table Added by
									// arun
									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, bknbr);
									detHistoryInsPstmt.setString(3, "RETAILER");
									detHistoryInsPstmt.setInt(4, retOrgId);
									detHistoryInsPstmt.setTimestamp(5,
											currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, agtId);
									detHistoryInsPstmt.setInt(8,
											noOfTktPerBooks);
									if ("ACTIVE".equalsIgnoreCase(newBookStatus
											.trim())) {
										detHistoryInsPstmt.setInt(9,
												noOfTktPerBooks);
									} else {
										detHistoryInsPstmt.setInt(9, 0);
									}
									detHistoryInsPstmt.setInt(10, 0);
									detHistoryInsPstmt.setString(11,
											newBookStatus);
									detHistoryInsPstmt.execute();

								}
							}
						}

						agtOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						agtOrdGameStmt.setInt(2, orderId);
						agtOrdGameStmt.setInt(3, gameId);

						agtOrdGameStmt.execute();

						if (bookList != null) {
							invQuery = gameInvStatusQuery + " and book_nbr = ?";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);// Added
								// by
								// arun on
								// update
								// status
								// table
								gameInvStatusStmt.setString(3, newBookStatus);
								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus
										.trim())) {
									gameInvStatusStmt
											.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								// ----------------

								gameInvStatusStmt.setInt(6, gameId);
								gameInvStatusStmt.setString(7, bookNbr);
								gameInvStatusStmt.execute();

								
								 * packNbr =
								 * getPackNbrForBook(connection,gameId,bookNbr);
								 * 
								 * gameInvDetailStmt.setInt(1,masterTrnId);
								 * gameInvDetailStmt.setInt(2,gameId);
								 * gameInvDetailStmt.setString(3,packNbr);
								 * gameInvDetailStmt.setString(4,bookNbr);
								 * gameInvDetailStmt.setString(5,"RETAILER");
								 * gameInvDetailStmt.setInt(6,retOrgId);
								 * gameInvDetailStmt.setTimestamp(7,currentDate);
								 * gameInvDetailStmt.setString(8,"AGENT");
								 * gameInvDetailStmt.setDouble(9,retSaleCommRate);
								 * gameInvDetailStmt.setDouble(10,
								 * gameBean.getGovtComm());
								 * 
								 * gameInvDetailStmt.execute();
								 

							}
						}

						if (total == totalDispatchedForOrder) {

							agtOrderStmt.setString(1, "PROCESSED");
							agtOrderStmt.setInt(2, orderId);

							agtOrderStmt.executeUpdate();
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.executeUpdate();
							System.out
									.println("Total aaproved and total dispatched are equal----PROCESSED");
						} else {
							agtOrderStmt.setString(1, "SEMI_PROCESSED");
							agtOrderStmt.setInt(2, orderId);
							agtOrderStmt.executeUpdate();
							System.out.println("In the Semi processed block");
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "SEMI_PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.executeUpdate();
						}

					}

				}

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts_gen_mapping where receipt_type=? and
				// agt_org_id=? ORDER BY generated_id DESC LIMIT 1 ";
				agtReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getAGENTLatestReceiptNb());
				agtReceiptNoGenStmt.setString(1, "INVOICE");
				agtReceiptNoGenStmt.setInt(2, userOrgID);
				ResultSet recieptRs = agtReceiptNoGenStmt.executeQuery();
				String lastRecieptNoGenerated = null;
				// System.out.println("queryyyyyyyyyy:::::: " +
				// agtReceiptNoGenStmt);
				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = null;
				autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"INVOICE", lastRecieptNoGenerated, "AGENT", userOrgID);

				// get auto generated delivery Challan number
				// String getLatestDCNumber="SELECT * from
				// st_lms_agent_receipts_gen_mapping where receipt_type=? and
				// agt_org_id=? ORDER BY generated_id DESC LIMIT 1 ";
				agtReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getAGENTLatestReceiptNb());
				agtReceiptNoGenStmt.setString(1, "DLCHALLAN");
				agtReceiptNoGenStmt.setInt(2, userOrgID);
				ResultSet DCRs = agtReceiptNoGenStmt.executeQuery();
				String lastDCNoGenerated = null;

				while (DCRs.next()) {
					lastDCNoGenerated = DCRs.getString("generated_id");
				}

				String autoGeneDCNo = null;
				autoGeneDCNo = GenerateRecieptNo.getRecieptNoAgt("DLCHALLAN",
						lastDCNoGenerated, "AGENT", userOrgID);

				// insert into agent receipt table

				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInAgentReceipts());

				agtReceiptStmt.setInt(1, invoiceId);
				agtReceiptStmt.setString(2, "INVOICE");
				agtReceiptStmt.setInt(3, userOrgID);
				agtReceiptStmt.setInt(4, retOrgId);
				agtReceiptStmt.setString(5, "RETAILER");
				agtReceiptStmt.setString(6, autoGeneRecieptNo);
				agtReceiptStmt.setTimestamp(7, Util.getCurrentTimeStamp());

				
				 * agtReceiptStmt.setString(1,"INVOICE");
				 * agtReceiptStmt.setInt(2,agtId);
				 * agtReceiptStmt.setInt(3,retOrgId);
				 

				agtReceiptStmt.execute();

				// insert into agent receipt table for dl challan
				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInAgentReceipts());
				agtReceiptStmt.setInt(1, DCId);
				agtReceiptStmt.setString(2, "DLCHALLAN");
				agtReceiptStmt.setInt(3, userOrgID);
				agtReceiptStmt.setInt(4, retOrgId);
				agtReceiptStmt.setString(5, "RETAILER");
				agtReceiptStmt.setString(6, autoGeneDCNo);
				agtReceiptStmt.setTimestamp(7, Util.getCurrentTimeStamp());
				
				 * agtReceiptStmt.setString(1,"DLCHALLAN");
				 * agtReceiptStmt.setInt(2,agtId);
				 * agtReceiptStmt.setInt(3,retOrgId);
				 

				agtReceiptStmt.execute();

				for (int i = 0; i < trnIdList.size(); i++) {
					agtReceiptMappingStmt.setInt(1, invoiceId);
					agtReceiptMappingStmt.setInt(2, trnIdList.get(i));
					agtReceiptMappingStmt.execute();

				}

				for (int i = 0; i < trnIdList.size(); i++) {
					agtReceiptMappingStmt.setInt(1, DCId);
					agtReceiptMappingStmt.setInt(2, trnIdList.get(i));
					agtReceiptMappingStmt.execute();

				}

				// insert into invoice and delivery challan mapping table

				String insertInvoiceDCMapping = "insert into st_se_agent_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
				agtInvoiceDCMappingStmt = connection
						.prepareStatement(insertInvoiceDCMapping);
				agtInvoiceDCMappingStmt.setInt(1, invoiceId);
				agtInvoiceDCMappingStmt.setString(2, autoGeneRecieptNo);
				agtInvoiceDCMappingStmt.setString(3, autoGeneDCNo);
				agtInvoiceDCMappingStmt.executeUpdate();

				// for org credit updation
				System.out.println("Org Id For Credit Update:" + retOrgId + ":"
						+ creditAmt);
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", retOrgId,
						userOrgID, "RETAILER", 0, connection);
				
				if(!isValid)
					throw new LMSException();
				
				
				boolean isUpdateDone = OrgCreditUpdation
						.updateCreditLimitForRetailer(retOrgId, "SALE",
								creditAmt, connection);

				connection.commit();
				session.setAttribute("DEL_CHALLAN_ID", DCId);
				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportAgent(invoiceId,
							rootPath, userOrgID, loggedInUserOrgName);
				}
			} catch (SQLException se) {
				try {
					connection.rollback();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new LMSException("Error During Rollback", e);
				}
				System.out
						.println("*************************Printing stack trace***************************");
				se.printStackTrace();
				throw new LMSException(se);
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

	}*/

	public int assignOrder(List<InvOrderBean> invOrderList, int total, int orderId, int retOrgId, int agtId, String rootPath, String loggedInUserOrgName, int userOrgID) throws LMSException {
		int DCId = -1;
		int warehouseId = -1;
		Connection connection = null;
		InvOrderBean invOrderBean = null;
		OrderedGameBean gameBean = null;
		if (invOrderList != null) {
			//PreparedStatement agtMasterStmt = null;
			//PreparedStatement LMSMasterStmt = null;
			//PreparedStatement agtAgentStmt = null;
			PreparedStatement agtOrdGameStmt = null;
			PreparedStatement agtReceiptStmt = null;
			PreparedStatement agtReceiptNoGenStmt = null;
			PreparedStatement agtReceiptMappingStmt = null;
			PreparedStatement agtOrderStmt = null;
			PreparedStatement gameInvStatusStmt = null;
			PreparedStatement gameInvDetailStmt = null;
			PreparedStatement orderInvoicesPrtSt = null;
			PreparedStatement getBookFromPackPstmt = null;
			PreparedStatement isGovtCommChngdPstmt = null;

			ResultSet rsMaster = null;
			Statement totalDispatch = null;
			ResultSet totalDispatchResult = null;
			String orderInvoicesQuery = null;
			//String agtMasterQuery = null;
			//String LMSMasterQuery = null;
			//String agtAgentQuery = null;
			String agtOrdGamesQuery = null;
			String agtReceiptMappingQuery = null;
			String agtOrderQuery = null;
			String gameInvStatusQuery = null;
			String invQuery = null;

			String gameInvDetailQuery = null;
			String getBookfromPackQuery = null;
			String isGovtCommChangedQuery = null;

			String countDispatchedTotal = "select nbr_of_books_dlvrd from st_se_agent_ordered_games where order_id=" + orderId;
			List<Integer> trnIdList = new ArrayList<Integer>();
			int masterTrnId = -1;
			int totalDispatchedForOrder = 0;
			try {
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);

				//agtMasterQuery = QueryManager.insertInAgentTransactionMaster();
				//agtMasterStmt = connection.prepareStatement(agtMasterQuery);

				//LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
				//LMSMasterStmt = connection.prepareStatement(LMSMasterQuery);

				//agtAgentQuery = QueryManager.getST1AgentRetQuery();
				//agtAgentStmt = connection.prepareStatement(agtAgentQuery);

				agtOrdGamesQuery = QueryManager.getST1AgtOrdGamesUpdQuery();
				agtOrdGameStmt = connection.prepareStatement(agtOrdGamesQuery);

				java.sql.Timestamp currentDate = new java.sql.Timestamp(new Date().getTime());

				agtReceiptMappingQuery = QueryManager.insertAgentReceiptTrnMapping();
				agtReceiptMappingStmt = connection.prepareStatement(agtReceiptMappingQuery);

				agtOrderQuery = QueryManager.getST1AgtOrdUpdQuery();
				agtOrderStmt = connection.prepareStatement(agtOrderQuery);

				gameInvStatusQuery = QueryManager.getST1AgtUpdGameInvStatusQuery();

				gameInvDetailQuery = QueryManager.getST1InvDetailWithWarehouseInsertQuery();
				gameInvDetailStmt = connection.prepareStatement(gameInvDetailQuery);

				orderInvoicesQuery = QueryManager.getST1AgentOrderInInsertQuery();
				orderInvoicesPrtSt = connection.prepareStatement(orderInvoicesQuery);

				getBookfromPackQuery = "select book_nbr from st_se_game_inv_status where pack_nbr=?";
				getBookFromPackPstmt = connection.prepareStatement(getBookfromPackQuery);

				isGovtCommChangedQuery = "select count(*) from st_se_game_govt_comm_history where game_id=?";
				isGovtCommChngdPstmt = connection.prepareStatement(isGovtCommChangedQuery);

				String fetchPurRateQuery = "select transacrion_sale_comm_rate from st_se_game_inv_detail where " + "book_nbr=? and transaction_at='BO' order by transaction_date desc limit 1";
				PreparedStatement fetchPurRatePstmt = connection.prepareStatement(fetchPurRateQuery);
				ResultSet fetchPurRateRs = null;

				String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
						+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
						+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement detHistoryInsPstmt = connection
						.prepareStatement(detHistoryInsQuery);

				double mrpAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double vatAmt = 0.0;
				double taxableSale = 0.0;
				double retSaleCommRate = 0.0;
				double creditAmt = 0.0;
				int nbrOfbooksApp = 0;
				int nbrBooksalreadyDispatched = 0;
				int nbrOfBooksDispatched = 0;

				totalDispatch = connection.createStatement();
				totalDispatchResult = totalDispatch.executeQuery(countDispatchedTotal);
				while (totalDispatchResult.next()) {
					totalDispatchedForOrder = totalDispatchedForOrder + totalDispatchResult.getInt("nbr_of_books_dlvrd");

				}

				int gameId = -1;
				List<PackBean> packList = null;
				List<BookBean> bookList = null;
				PackBean packBean = null;
				BookBean bookBean = null;

				/*agtReceiptStmt = connection.prepareStatement(QueryManager.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet rs = agtReceiptStmt.getGeneratedKeys();
				int invoiceId = -1;
				while (rs.next()) {
					invoiceId = rs.getInt(1);
				}*/

				agtReceiptStmt = connection.prepareStatement(QueryManager.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet DCrs = agtReceiptStmt.getGeneratedKeys();
				while (DCrs.next()) {
					DCId = DCrs.getInt(1);
				}

				for (int i = 0; i < invOrderList.size(); i++) {
					boolean isGovtCommChanged = false;
					invOrderBean = invOrderList.get(i);
					gameBean = invOrderBean.getOrderedGameBean();
					gameId = gameBean.getGameId();

					String fetchGameDetQuery = "select nbr_of_tickets_per_book from st_se_game_master where game_id =" + gameId;
					Statement fetchGameDetStmt = connection.createStatement();
					ResultSet fetchGameDetRs = fetchGameDetStmt.executeQuery(fetchGameDetQuery);
					int noOfTktPerBooks = -1;
					if (fetchGameDetRs.next()) {
						noOfTktPerBooks = fetchGameDetRs.getInt("nbr_of_tickets_per_book");
					}

					isGovtCommChngdPstmt.setInt(1, gameId);
					ResultSet isGovtCommChngdRs = isGovtCommChngdPstmt.executeQuery();
					if (isGovtCommChngdRs.next()) {
						if (isGovtCommChngdRs.getInt(1) > 1) {
							isGovtCommChanged = true;
						}
					}

					String newBookStatus = "ASSIGNED";
					if (isGovtCommChanged == false) {
						nbrOfbooksApp = gameBean.getNbrOfBooksApp();
						nbrBooksalreadyDispatched = gameBean.getNbrOfBooksDlvrd();
						int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();
						nbrOfBooksDispatched = nbrBooksalreadyDispatched + getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);
						totalDispatchedForOrder = totalDispatchedForOrder + getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);
						int currentlyDispatched = getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);

						// insert in LMS transaction master
						/*LMSMasterStmt.setString(1, "AGENT");
						LMSMasterStmt.executeUpdate();
						rsMaster = null;
						rsMaster = LMSMasterStmt.getGeneratedKeys();
						while (rsMaster.next()) {
							masterTrnId = rsMaster.getInt(1);
							trnIdList.add(masterTrnId);
						}

						agtMasterStmt.setInt(1, masterTrnId);
						agtMasterStmt.setInt(2, agtId);
						agtMasterStmt.setInt(3, userOrgID);
						agtMasterStmt.setString(4, "RETAILER");
						agtMasterStmt.setInt(5, retOrgId);
						agtMasterStmt.setString(6, "SALE");
						agtMasterStmt.setTimestamp(7, currentDate);
						agtMasterStmt.execute();

						agtAgentStmt.setInt(1, masterTrnId);
						agtAgentStmt.setInt(2, gameId);
						agtAgentStmt.setInt(3, agtId);
						agtAgentStmt.setInt(4, retOrgId);

						mrpAmt = currentlyDispatched * gameBean.getBookPrice();
						agtAgentStmt.setDouble(5, mrpAmt);

						retSaleCommRate = gameBean.getRetSaleCommRate() + gameBean.getRetGameCommVariance();
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						agtAgentStmt.setDouble(6, commAmt);

						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						vatAmt = CommonMethods.calculateVat(mrpAmt, retSaleCommRate, gameBean.getPrizePayOutRatio(), gameBean.getGovtComm(), gameBean.getVat(), gameBean.getGovtCommRule(), gameBean.getFixedAmt(), gameBean.getTicketsInScheme());
						taxableSale = CommonMethods.calTaxableSale(mrpAmt, retSaleCommRate, gameBean.getPrizePayOutRatio(), gameBean.getGovtComm(), gameBean.getVat());

						agtAgentStmt.setDouble(7, netAmt);
						agtAgentStmt.setString(8, "SALE");
						agtAgentStmt.setInt(9, currentlyDispatched);
						agtAgentStmt.setDouble(10, vatAmt);
						agtAgentStmt.setDouble(11, taxableSale);
						agtAgentStmt.setInt(12, userOrgID);
						agtAgentStmt.execute();*/
						agtOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						agtOrdGameStmt.setInt(2, orderId);
						agtOrdGameStmt.setInt(3, gameId);
						agtOrdGameStmt.execute();

						if (total == totalDispatchedForOrder) {
							agtOrderStmt.setString(1, "ASSIGNED");
							agtOrderStmt.setInt(2, orderId);
							agtOrderStmt.executeUpdate();

							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setString(2, null);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "ASSIGNED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.setInt(9, DCId);
							orderInvoicesPrtSt.executeUpdate();
						} else {
							agtOrderStmt.setString(1, "SEMI_ASSIGNED");
							agtOrderStmt.setInt(2, orderId);
							agtOrderStmt.executeUpdate();

							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setString(2, null);
							orderInvoicesPrtSt.setInt(3, retOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "SEMI_ASSIGNED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, agtId);
							orderInvoicesPrtSt.setInt(8, userOrgID);
							orderInvoicesPrtSt.setInt(9, DCId);
							orderInvoicesPrtSt.executeUpdate();
						}

						ResultSet rs = orderInvoicesPrtSt.getGeneratedKeys();
						int orderInvoiceDCId = -1;
						while (rs.next()) {
							orderInvoiceDCId = rs.getInt(1);
						}

						packList = invOrderBean.getPackList();
						if (packList != null) {
							invQuery = gameInvStatusQuery + " and pack_nbr = ? ";
							gameInvStatusStmt = connection.prepareStatement(invQuery);
							String packNbr = null;

							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();

								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);

								// Added by arun on update status table

								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									gameInvStatusStmt
											.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, packNbr);
								gameInvStatusStmt.execute();

								List<String> books = getBooksForPack(connection, gameId, packNbr);

								for (int k = 0; k < books.size(); k++) {
									warehouseId = ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, books.get(k));
									gameInvDetailStmt.setString(1, null);
									gameInvDetailStmt.setInt(2, gameId);
									gameInvDetailStmt.setString(3, packNbr);
									gameInvDetailStmt.setString(4, books.get(k));
									gameInvDetailStmt.setString(5, "RETAILER");
									gameInvDetailStmt.setInt(6, retOrgId);
									gameInvDetailStmt.setTimestamp(7, currentDate);
									gameInvDetailStmt.setString(8, "AGENT");
									gameInvDetailStmt.setString(9, newBookStatus);
									gameInvDetailStmt.setInt(10, orderInvoiceDCId);
									gameInvDetailStmt.setInt(11, warehouseId);
									gameInvDetailStmt.execute();

									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, books.get(k));
									detHistoryInsPstmt.setString(3, "RETAILER");
									detHistoryInsPstmt.setInt(4, retOrgId);
									detHistoryInsPstmt.setTimestamp(5,
											currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, agtId);
									detHistoryInsPstmt.setInt(8,
											noOfTktPerBooks);
									if ("ACTIVE".equalsIgnoreCase(newBookStatus
											.trim())) {
										detHistoryInsPstmt.setInt(9,
												noOfTktPerBooks);
									} else {
										detHistoryInsPstmt.setInt(9, 0);
									}
									detHistoryInsPstmt.setInt(10, 0);
									detHistoryInsPstmt.setString(11,
											newBookStatus);
									detHistoryInsPstmt.execute();

								}

							}
						}

						bookList = invOrderBean.getBookList();
						if (bookList != null) {
							invQuery = gameInvStatusQuery + " and book_nbr = ?";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);
								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									gameInvStatusStmt.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, bookNbr);
								gameInvStatusStmt.execute();

								/*fetchPurRatePstmt.setString(1, bookNbr);
								fetchPurRateRs = fetchPurRatePstmt.executeQuery();
								if (fetchPurRateRs.next()) {
									purRate = fetchPurRateRs.getDouble("transacrion_sale_comm_rate");
								}*/

								packNbr = getPackNbrForBook(connection, gameId, bookNbr);
								warehouseId = ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, bookNbr);
								gameInvDetailStmt.setString(1, null);
								gameInvDetailStmt.setInt(2, gameId);
								gameInvDetailStmt.setString(3, packNbr);
								gameInvDetailStmt.setString(4, bookNbr);
								gameInvDetailStmt.setString(5, "RETAILER");
								gameInvDetailStmt.setInt(6, retOrgId);
								gameInvDetailStmt.setTimestamp(7, currentDate);
								gameInvDetailStmt.setString(8, "AGENT");
								gameInvDetailStmt.setString(9, newBookStatus);
								gameInvDetailStmt.setInt(10, orderInvoiceDCId);
								gameInvDetailStmt.setInt(11, warehouseId);
								gameInvDetailStmt.execute();

								detHistoryInsPstmt.setInt(1, gameId);
								detHistoryInsPstmt.setString(2, bookNbr);
								detHistoryInsPstmt.setString(3, "RETAILER");
								detHistoryInsPstmt.setInt(4, retOrgId);
								detHistoryInsPstmt.setTimestamp(5, currentDate);
								detHistoryInsPstmt.setInt(6, userOrgID);
								detHistoryInsPstmt.setInt(7, agtId);
								detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									detHistoryInsPstmt.setInt(9, noOfTktPerBooks);
								} else {
									detHistoryInsPstmt.setInt(9, 0);
								}
								detHistoryInsPstmt.setInt(10, 0);
								detHistoryInsPstmt.setString(11, newBookStatus);
								detHistoryInsPstmt.execute();
							}
						}
					} else {
						nbrOfbooksApp = gameBean.getNbrOfBooksApp();
						nbrBooksalreadyDispatched = gameBean.getNbrOfBooksDlvrd();
						int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();

						nbrOfBooksDispatched = nbrBooksalreadyDispatched + getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);
						totalDispatchedForOrder = totalDispatchedForOrder + getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);
						int currentlyDispatched = getDispatchedNbrOfBooks(invOrderBean, nbrOfBooksPerPack);

						mrpAmt = currentlyDispatched * gameBean.getBookPrice();
						retSaleCommRate = gameBean.getRetSaleCommRate() + gameBean.getRetGameCommVariance();
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						packList = invOrderBean.getPackList();
						bookList = invOrderBean.getBookList();

						if (packList != null) {
							String packNbr = null;
							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();
								getBookFromPackPstmt.setString(1, packNbr);
								ResultSet rsBookFrmPack = getBookFromPackPstmt.executeQuery();
								while (rsBookFrmPack.next()) {
									bookBean = new BookBean();
									String bookNbrfromPack = rsBookFrmPack.getString("book_nbr");
									bookBean.setBookNumber(bookNbrfromPack);
									bookList.add(bookBean);
								}
							}
						}

						ResultSet rsCommVar;
						ArrayList<BookSaleBean> bookSaleBeanList = new ArrayList<BookSaleBean>();
						BookSaleBean bookSaleBean;
						for (int bookListSize = 0; bookListSize < bookList.size(); bookListSize++) {
							bookSaleBean = new BookSaleBean();
							double govtCommRate = 0.0;
							double vatAmtForBook = 0.0;
							double taxableSaleForbook = 0.0;
							bookBean = bookList.get(bookListSize);
							String bookfromBookList = bookBean.getBookNumber();
							String govtCommVarianceQuery = "select transaction_gov_comm_rate,transaction_date from st_se_game_inv_detail where current_owner=? and current_owner_id=? and game_id=? and book_nbr=? and transaction_at=? order by transaction_date desc limit 1 ";
							PreparedStatement govtCommVariaceStmt = connection.prepareStatement(govtCommVarianceQuery);
							govtCommVariaceStmt.setString(1, "AGENT");
							govtCommVariaceStmt.setInt(2, userOrgID);
							govtCommVariaceStmt.setInt(3, gameId);
							govtCommVariaceStmt.setString(4, bookfromBookList);
							govtCommVariaceStmt.setString(5, "BO");
							rsCommVar = govtCommVariaceStmt.executeQuery();
							while (rsCommVar.next()) {
								govtCommRate = rsCommVar.getDouble("transaction_gov_comm_rate");
							}

							vatAmtForBook = CommonMethods.calculateVat(gameBean.getBookPrice(), retSaleCommRate, gameBean.getPrizePayOutRatio(), govtCommRate, gameBean.getVat(), gameBean.getGovtCommRule(), gameBean.getFixedAmt(), gameBean.getTicketsInScheme());
							taxableSaleForbook = CommonMethods.calTaxableSale(gameBean.getBookPrice(), retSaleCommRate, gameBean.getPrizePayOutRatio(), govtCommRate, gameBean.getVat());

							bookSaleBean.setBookNumber(bookfromBookList);
							bookSaleBean.setBookTaxableSale(taxableSaleForbook);
							bookSaleBean.setBookVatAmt(vatAmtForBook);
							bookSaleBean.setTotalSaleGovtComm(govtCommRate);
							bookSaleBeanList.add(bookSaleBean);
						}

						Set<Double> GovtCommVarianceSet = new HashSet<Double>();
						String queryGovtCommVarHistory = "select DISTINCT govt_comm_rate from st_se_game_govt_comm_history where game_id=" + gameId;
						PreparedStatement stmtGovtCommVarHistory = connection.prepareStatement(queryGovtCommVarHistory);
						ResultSet rsGovtCommVarHistory = stmtGovtCommVarHistory.executeQuery();
						while (rsGovtCommVarHistory.next()) {
							GovtCommVarianceSet.add(rsGovtCommVarHistory.getDouble("govt_comm_rate"));
						}

						Iterator<Double> govtCommSetItr;
						Iterator<BookSaleBean> bookSaleBeanItr;
						govtCommSetItr = GovtCommVarianceSet.iterator();

						while (govtCommSetItr.hasNext()) {
							boolean bookGovtCommVarMatch = false;
							bookSaleBeanItr = bookSaleBeanList.iterator();
							List<String> bookListforSingleTrn = null;
							bookListforSingleTrn = new ArrayList<String>();

							double totVatAmt = 0.0;
							double totTaxableSale = 0.0;
							double totMrpAmt = 0.0;
							double totalCommAmt = 0.0;
							double totalNetAmt = 0.0;

							Double govtCommFormSet = (Double) govtCommSetItr.next();
							while (bookSaleBeanItr.hasNext()) {
								bookSaleBean = (BookSaleBean) bookSaleBeanItr.next();
								if (bookSaleBean.getTotalSaleGovtComm() == govtCommFormSet) {
									bookGovtCommVarMatch = true;
									totVatAmt = totVatAmt + bookSaleBean.getBookVatAmt();
									totTaxableSale = totTaxableSale + bookSaleBean.getBookTaxableSale();
									totMrpAmt = totMrpAmt + gameBean.getBookPrice();
									bookListforSingleTrn.add(bookSaleBean.getBookNumber());
								}
							}

							if (total == totalDispatchedForOrder) {
								agtOrderStmt.setString(1, "ASSIGNED");
								agtOrderStmt.setInt(2, orderId);
								agtOrderStmt.executeUpdate();

								orderInvoicesPrtSt.setInt(1, orderId);
								orderInvoicesPrtSt.setString(2, null);
								orderInvoicesPrtSt.setInt(3, retOrgId);
								orderInvoicesPrtSt.setInt(4, gameId);
								orderInvoicesPrtSt.setString(5, "ASSIGNED");
								orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
								orderInvoicesPrtSt.setInt(7, agtId);
								orderInvoicesPrtSt.setInt(8, userOrgID);
								orderInvoicesPrtSt.setInt(9, DCId);
								orderInvoicesPrtSt.executeUpdate();
							} else {
								agtOrderStmt.setString(1, "SEMI_ASSIGNED");
								agtOrderStmt.setInt(2, orderId);
								agtOrderStmt.executeUpdate();

								orderInvoicesPrtSt.setInt(1, orderId);
								orderInvoicesPrtSt.setString(2, null);
								orderInvoicesPrtSt.setInt(3, retOrgId);
								orderInvoicesPrtSt.setInt(4, gameId);
								orderInvoicesPrtSt.setString(5, "SEMI_ASSIGNED");
								orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
								orderInvoicesPrtSt.setInt(7, agtId);
								orderInvoicesPrtSt.setInt(8, userOrgID);
								orderInvoicesPrtSt.setInt(9, DCId);
								orderInvoicesPrtSt.executeUpdate();
							}

							ResultSet rs = orderInvoicesPrtSt.getGeneratedKeys();
							int orderInvoiceDCId = -1;
							while (rs.next()) {
								orderInvoiceDCId = rs.getInt(1);
							}

							totalCommAmt = totMrpAmt * retSaleCommRate * 0.01;
							totalNetAmt = totMrpAmt - totalCommAmt;
							if (bookGovtCommVarMatch) {
								/*LMSMasterStmt.setString(1, "AGENT");
								LMSMasterStmt.executeUpdate();

								rsMaster = null;
								rsMaster = LMSMasterStmt.getGeneratedKeys();
								while (rsMaster.next()) {
									masterTrnId = rsMaster.getInt(1);
									trnIdList.add(masterTrnId);
								}

								agtMasterStmt.setInt(1, masterTrnId);
								agtMasterStmt.setInt(2, agtId);
								agtMasterStmt.setInt(3, userOrgID);
								agtMasterStmt.setString(4, "RETAILER");
								agtMasterStmt.setInt(5, retOrgId);
								agtMasterStmt.setString(6, "SALE");
								agtMasterStmt.setTimestamp(7, currentDate);
								agtMasterStmt.execute();

								agtAgentStmt.setInt(1, masterTrnId);
								agtAgentStmt.setInt(2, gameId);
								agtAgentStmt.setInt(3, agtId);
								agtAgentStmt.setInt(4, retOrgId);
								agtAgentStmt.setDouble(5, totMrpAmt);
								agtAgentStmt.setDouble(6, totalCommAmt);
								agtAgentStmt.setDouble(7, totalNetAmt);
								agtAgentStmt.setString(8, "SALE");
								agtAgentStmt.setInt(9, bookListforSingleTrn.size());
								agtAgentStmt.setDouble(10, totVatAmt);
								agtAgentStmt.setDouble(11, totTaxableSale);
								agtAgentStmt.setInt(12, userOrgID);
								agtAgentStmt.execute();*/

								for (int j = 0; j < bookListforSingleTrn.size(); j++) {
									String bknbr = (String) bookListforSingleTrn.get(j);
									String pkNbr = getPackNbrForBook(connection, gameId, bknbr);
									warehouseId = ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, bknbr);
									/*fetchPurRatePstmt.setString(1, bknbr);
									fetchPurRateRs = fetchPurRatePstmt.executeQuery();
									if (fetchPurRateRs.next()) {
										purRate = fetchPurRateRs.getDouble("transacrion_sale_comm_rate");
									}*/

									gameInvDetailStmt.setString(1, null);
									gameInvDetailStmt.setInt(2, gameId);
									gameInvDetailStmt.setString(3, pkNbr);
									gameInvDetailStmt.setString(4, bknbr);
									gameInvDetailStmt.setString(5, "RETAILER");
									gameInvDetailStmt.setInt(6, retOrgId);
									gameInvDetailStmt.setTimestamp(7, currentDate);
									gameInvDetailStmt.setString(8, "AGENT");
									gameInvDetailStmt.setString(9, newBookStatus);
									gameInvDetailStmt.setInt(10, orderInvoiceDCId);
									gameInvDetailStmt.setInt(11, warehouseId);
									gameInvDetailStmt.execute();

									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, bknbr);
									detHistoryInsPstmt.setString(3, "RETAILER");
									detHistoryInsPstmt.setInt(4, retOrgId);
									detHistoryInsPstmt.setTimestamp(5, currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, agtId);
									detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
									if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
										detHistoryInsPstmt.setInt(9, noOfTktPerBooks);
									} else {
										detHistoryInsPstmt.setInt(9, 0);
									}
									detHistoryInsPstmt.setInt(10, 0);
									detHistoryInsPstmt.setString(11, newBookStatus);
									detHistoryInsPstmt.execute();
								}
							}
						}

						agtOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						agtOrdGameStmt.setInt(2, orderId);
						agtOrdGameStmt.setInt(3, gameId);
						agtOrdGameStmt.execute();

						if (bookList != null) {
							invQuery = gameInvStatusQuery + " and book_nbr = ?";
							gameInvStatusStmt = connection.prepareStatement(invQuery);
							String bookNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "RETAILER");
								gameInvStatusStmt.setInt(2, retOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);
								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									gameInvStatusStmt.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, bookNbr);
								gameInvStatusStmt.execute();
							}
						}

						
					}
				}

				/*agtReceiptNoGenStmt = connection.prepareStatement(QueryManager.getAGENTLatestReceiptNb());
				agtReceiptNoGenStmt.setString(1, "INVOICE");
				agtReceiptNoGenStmt.setInt(2, userOrgID);
				ResultSet recieptRs = agtReceiptNoGenStmt.executeQuery();
				String lastRecieptNoGenerated = null;
				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs.getString("generated_id");
				}
				String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt("INVOICE", lastRecieptNoGenerated, "AGENT", userOrgID);*/

				agtReceiptNoGenStmt = connection.prepareStatement(QueryManager.getAGENTLatestReceiptNb());
				agtReceiptNoGenStmt.setString(1, "DLCHALLAN");
				agtReceiptNoGenStmt.setInt(2, userOrgID);
				ResultSet DCRs = agtReceiptNoGenStmt.executeQuery();
				String lastDCNoGenerated = null;
				while (DCRs.next()) {
					lastDCNoGenerated = DCRs.getString("generated_id");
				}
				String autoGeneDCNo = GenerateRecieptNo.getRecieptNoAgt("DLCHALLAN", lastDCNoGenerated, "AGENT", userOrgID);

				/*agtReceiptStmt = connection.prepareStatement(QueryManager.insertInAgentReceipts());
				agtReceiptStmt.setInt(1, invoiceId);
				agtReceiptStmt.setString(2, "INVOICE");
				agtReceiptStmt.setInt(3, userOrgID);
				agtReceiptStmt.setInt(4, retOrgId);
				agtReceiptStmt.setString(5, "RETAILER");
				agtReceiptStmt.setString(6, autoGeneRecieptNo);
				agtReceiptStmt.setTimestamp(7, Util.getCurrentTimeStamp());
				agtReceiptStmt.execute();*/

				agtReceiptStmt = connection.prepareStatement(QueryManager.insertInAgentReceipts());
				agtReceiptStmt.setInt(1, DCId);
				agtReceiptStmt.setString(2, "DLCHALLAN");
				agtReceiptStmt.setInt(3, userOrgID);
				agtReceiptStmt.setInt(4, retOrgId);
				agtReceiptStmt.setString(5, "RETAILER");
				agtReceiptStmt.setString(6, autoGeneDCNo);
				agtReceiptStmt.setTimestamp(7, Util.getCurrentTimeStamp());
				agtReceiptStmt.execute();

				/*for (int i = 0; i < trnIdList.size(); i++) {
					agtReceiptMappingStmt.setInt(1, invoiceId);
					agtReceiptMappingStmt.setInt(2, trnIdList.get(i));
					agtReceiptMappingStmt.execute();
				}*/

				for (int i = 0; i < trnIdList.size(); i++) {
					agtReceiptMappingStmt.setInt(1, DCId);
					agtReceiptMappingStmt.setInt(2, trnIdList.get(i));
					agtReceiptMappingStmt.execute();
				}

				/*String insertInvoiceDCMapping = "insert into st_se_agent_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
				agtInvoiceDCMappingStmt = connection.prepareStatement(insertInvoiceDCMapping);
				agtInvoiceDCMappingStmt.setInt(1, invoiceId);
				agtInvoiceDCMappingStmt.setString(2, autoGeneRecieptNo);
				agtInvoiceDCMappingStmt.setString(3, autoGeneDCNo);
				agtInvoiceDCMappingStmt.executeUpdate();*/

				/*boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", retOrgId, userOrgID, "RETAILER", 0, connection);
				if (!isValid)
					throw new LMSException();*/

				connection.commit();
				//session.setAttribute("DEL_CHALLAN_ID", DCId);
				/*if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportAgent(invoiceId, rootPath, userOrgID, loggedInUserOrgName);
				}*/
			} catch (SQLException se) {
				try {
					connection.rollback();

				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException("Error During Rollback", e);
				}
				se.printStackTrace();
				throw new LMSException(se);
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

		return DCId;
	}

	/**
	 * This method returns the address of the passed organization
	 * 
	 * @param orgId
	 * @return OrgAddressBean
	 * @throws LMSException
	 */
	public OrgAddressBean fetchAddress(int orgId) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			OrgAddressBean orgAddrBean = null;

			 
			connection = DBConnect.getConnection();

			String query = QueryManager.getST1OrgAddrQuery();
			statement = connection.prepareStatement(query);

			statement.setInt(1, orgId);

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				orgAddrBean = new OrgAddressBean();

				orgAddrBean.setOrgId(orgId);
				orgAddrBean.setAddrLine1(resultSet
						.getString(TableConstants.SOM_ADDR_LINE1));
				orgAddrBean.setAddrLine2(resultSet
						.getString(TableConstants.SOM_ADDR_LINE2));
				orgAddrBean.setCity(resultSet
						.getString(TableConstants.SOM_CITY));
				orgAddrBean.setState(resultSet.getString(TableConstants.STATE));
				orgAddrBean.setCountry(resultSet
						.getString(TableConstants.COUNTRY));

			}

			return orgAddrBean;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	/**
	 * This method returns the list of games for the passed order number
	 * 
	 * @param orderId
	 * @return List
	 * @throws LMSException
	 */
	public List fetchOrderDetails(int orderId, int retOrgId)
			throws LMSException {
		int totalApprovedBooks = 0;
		Connection connection = null;
		Statement statement = null;
		Statement statementCommVariance = null;
		ResultSet resultSet = null;
		ResultSet resultSetCommVariance = null;
		try {

			OrderedGameBean orderedGameBean = null;
			List<OrderedGameBean> searchResults = new ArrayList<OrderedGameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST1AgentAppOrderGamesQuery();
			query = query + orderId;

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);
			int nbrBooksToDispatch = 0;
			double bookPrice = 0.0;
			while (resultSet.next()) {

				System.out.println("-------------------------------");
				orderedGameBean = new OrderedGameBean();
				orderedGameBean.setOrderId(resultSet
						.getInt(TableConstants.SAOG_ORDER_ID));
				orderedGameBean.setGameId(resultSet
						.getInt(TableConstants.SAOG_GAME_ID));
				orderedGameBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				orderedGameBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				orderedGameBean.setNbrOfBooksApp(resultSet
						.getInt(TableConstants.SAOG_NBR_OF_BOOKS_APP));

				// here added by yogesh
				// added by yogesh to get retailer commission variance for every
				// game
				double retGameCommVariace = 0.0;
				String getAgtCommVariance = "select sale_comm_variance from st_se_agent_retailer_sale_pwt_comm_variance where retailer_org_id="
						+ retOrgId
						+ " and game_id="
						+ resultSet.getInt(TableConstants.SAOG_GAME_ID);
				statementCommVariance = connection.createStatement();
				resultSetCommVariance = statementCommVariance
						.executeQuery(getAgtCommVariance);
				while (resultSetCommVariance.next()) {
					retGameCommVariace = resultSetCommVariance
							.getDouble("sale_comm_variance");

				}

				orderedGameBean.setRetGameCommVariance(retGameCommVariace);
				// added by yogesh to implement vat

				orderedGameBean.setPrizePayOutRatio(resultSet
						.getDouble("prize_payout_ratio"));
				orderedGameBean.setVat(resultSet.getDouble("vat_amt"));
				orderedGameBean.setGovtComm(resultSet
						.getDouble("govt_comm_rate"));
				orderedGameBean.setVatBalance(resultSet
						.getDouble("vat_balance"));
				orderedGameBean.setGovtCommRule(resultSet
						.getString("govt_comm_type"));
				orderedGameBean.setFixedAmt(resultSet.getDouble("fixed_amt"));
				orderedGameBean.setTicketsInScheme(resultSet
						.getLong("tickets_in_scheme"));

				orderedGameBean.setGameNbrDigits(resultSet
						.getInt(TableConstants.SGTNF_GAME_NBR_DIGITS));
				orderedGameBean.setPackDigits(resultSet
						.getInt(TableConstants.SGTNF_PACK_DIGITS));
				orderedGameBean.setBookDigits(resultSet
						.getInt(TableConstants.SGTNF_BOOK_DIGITS));

				totalApprovedBooks = totalApprovedBooks
						+ resultSet
								.getInt(TableConstants.SBOG_NBR_OF_BOOKS_APP);
				orderedGameBean.setNbrOfBooksDlvrd(resultSet
						.getInt(TableConstants.SBOG_NBR_OF_BOOKS_DLVRD));

				nbrBooksToDispatch = resultSet
						.getInt(TableConstants.SBOG_NBR_OF_BOOKS_APP)
						- resultSet
								.getInt(TableConstants.SBOG_NBR_OF_BOOKS_DLVRD);

				orderedGameBean.setRemainingBooksToDispatch(nbrBooksToDispatch);

				orderedGameBean.setNbrOfBooksPerPack(resultSet
						.getInt(TableConstants.SGM_NBR_OF_BOOKS_PER_PACK));
				bookPrice = resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK)
						* resultSet.getDouble(TableConstants.SGM_TICKET_PRICE);
				orderedGameBean.setBookPrice(bookPrice);
				orderedGameBean.setRetSaleCommRate(resultSet
						.getDouble(TableConstants.SGM_RET_SALE_RATE));
				if (!(nbrBooksToDispatch == 0)) {
					searchResults.add(orderedGameBean);
				}

			}
			this.setTotalOrderedBooks(totalApprovedBooks);
			System.out.println("----total approved for order : "
					+ totalApprovedBooks + "");
			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);

		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	private List<String> getBooksForPack(Connection connection, int gameId,
			String packNbr) throws SQLException {

		String packQuery = null;
		PreparedStatement packStmt = null;
		List<String> bookList = new ArrayList<String>();

		packQuery = QueryManager.getST1BooksForPack();
		packStmt = connection.prepareStatement(packQuery);

		packStmt.setInt(1, gameId);
		packStmt.setString(2, packNbr);

		ResultSet rs = packStmt.executeQuery();
		while (rs.next()) {
			bookList.add(rs.getString(TableConstants.SGIS_BOOK_NBR));
		}

		return bookList;
	}

	/**
	 * This method returns a list of approved orders for the passed agent
	 * 
	 * @param agtOrgId
	 * @return List
	 * @throws LMSException
	 */
	/*
	 * public List getApprovedOrders(int agtOrgId) throws LMSException {
	 * 
	 * Connection connection = null; PreparedStatement statement = null;
	 * ResultSet resultSet = null; try {
	 * 
	 * AgentOrderBean orderBean = null; List<AgentOrderBean> searchResults =
	 * new ArrayList<AgentOrderBean>();
	 * 
	 *   connection =
	 * dbConnect.getConnection();
	 * 
	 * 
	 * String query = QueryManager.getST1AgentAppOrderQuery(); statement =
	 * connection.prepareStatement(query); statement.setInt(1,agtOrgId);
	 * 
	 * System.out.println("-----Query----::" + query);
	 * 
	 * resultSet = statement.executeQuery();
	 * 
	 * while (resultSet.next()) {
	 * 
	 * orderBean = new AgentOrderBean(); orderBean.setOrderId(resultSet
	 * .getInt(TableConstants.SAO_ORDER_ID)); orderBean.setAgentId(resultSet
	 * .getInt(TableConstants.SAO_AGENT_ID)); orderBean.setRetailerId(resultSet
	 * .getInt(TableConstants.SAO_RETAILER_ID));
	 * orderBean.setRetailerOrgId(resultSet
	 * .getInt(TableConstants.SAO_RETAILER_ORG_ID));
	 * System.out.println("====Organisation Id :
	 * "+orderBean.getRetailerOrgId()); orderBean.setRetOrgName(resultSet
	 * .getString(TableConstants.SOM_ORG_NAME));
	 * System.out.println("====Organisation NAme : "+orderBean.getRetOrgName());
	 * orderBean.setOrderDate(resultSet
	 * .getDate(TableConstants.SAO_ORDER_DATE));
	 * 
	 * searchResults.add(orderBean); }
	 * 
	 * return searchResults; } catch (SQLException e) {
	 * 
	 * e.printStackTrace(); throw new LMSException(e); } finally {
	 * 
	 * try {
	 * 
	 * if (statement != null) { statement.close(); } if (connection != null) {
	 * connection.close(); } } catch (SQLException se) { se.printStackTrace(); } }
	 * 
	 * //return null; }
	 */

	private java.sql.Date getDate(String date) {

		System.out.println("Passed date::" + date);
		String format = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {

			Date parsedDate = dateFormat.parse(date);
			System.out.println("Parsed date::" + parsedDate);
			System.out.println(new java.sql.Date(parsedDate.getTime()));
			return new java.sql.Date(parsedDate.getTime());

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	private int getDispatchedNbrOfBooks(InvOrderBean invOrderBean,
			int nbrOfBooksPerPack) {

		List<PackBean> packList = invOrderBean.getPackList();
		List<BookBean> bookList = invOrderBean.getBookList();

		PackBean packBean = null;
		BookBean bookBean = null;

		int packCount = 0;
		int bookCount = 0;

		if (packList != null) {
			for (int i = 0; i < packList.size(); i++) {
				packBean = packList.get(i);
				if (packBean.getIsValid()) {
					packCount++;
				}
			}
		}
		if (bookList != null) {
			for (int i = 0; i < bookList.size(); i++) {
				bookBean = bookList.get(i);
				if (bookBean.getIsValid()) {
					bookCount++;
				}
			}
		}

		int totalBooks = bookCount + packCount * nbrOfBooksPerPack;
		return totalBooks;
	}

	public int getGameID() {
		return gameID;
	}

	private String getPackNbrForBook(Connection connection, int gameId,
			String bookNbr) throws SQLException {

		String bookQuery = null;
		PreparedStatement bookStmt = null;
		String packNbr = null;

		bookQuery = QueryManager.getST1PackForBook();
		bookStmt = connection.prepareStatement(bookQuery);

		bookStmt.setInt(1, gameId);
		bookStmt.setString(2, bookNbr);

		ResultSet rs = bookStmt.executeQuery();
		while (rs.next()) {
			packNbr = rs.getString(TableConstants.SGIS_PACK_NBR);
		}
		return packNbr;

	}

	public int getTotalOrderedBooks() {
		return totalOrderedBooks;
	}

	private String getWhereClause(Map searchMap) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();

		if (searchMap != null) {
			keySet = searchMap.keySet();

			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;

			int fieldAdded = 1;

			while (itr.hasNext()) {
				key = (String) itr.next();
				if (key.equals(GameContants.GAME_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {
						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_GAME_NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.GAME_NBR)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_GAME_NBR);
						whereClause.append(" = '");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						fieldAdded++;

					}

				} else if (key.equals(TableConstants.ORG_NAME)) {

					strValue = (String) searchMap.get(key);
					System.out.println(strValue + "...............Status");
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.ORG_NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%'");

						fieldAdded++;

					}
				}

				else if (key.equals(TableConstants.SBO_ORDER_STATUS)) {

					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SBO_ORDER_STATUS);
						whereClause.append(" = '");
						whereClause.append(strValue.trim());
						whereClause.append("' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.ORDER_ID)) {
					String str1 = "-1";
					str1 = (String) searchMap.get(key);

					if (str1 != null && !str1.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append("sbo." + GameContants.ORDER_ID);
						whereClause.append(" = '");
						whereClause.append(str1.trim());
						whereClause.append("' ");

						fieldAdded++;
					}
				}

			}
			if (fieldAdded == 1) {
				whereClause.append(" and 1=1 ");
			}

		}

		return whereClause.toString();
	}

	/**
	 * This method returns the list of order based on the search parameters
	 * passed
	 * 
	 * @param searchMap
	 * @return List
	 * @throws LMSException
	 */
	public List SearchOrder(Map searchMap, int agentOrgId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Statement statement1 = null;
		ResultSet resultSet1 = null;
		try {

			AgentOrderBean orderBean = null;
			List<AgentOrderBean> searchResults = new ArrayList<AgentOrderBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();

			String query = QueryManager.getST1AgentAppOrderQuery() + agentOrgId
					+ getWhereClause(searchMap) + " group by order_id";

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				orderBean = new AgentOrderBean();
				orderBean.setOrderId(resultSet
						.getInt(TableConstants.SAO_ORDER_ID));
				/*
				 * orderBean.setAgentId(resultSet
				 * .getInt(TableConstants.SAO_AGENT_ID));
				 */
				orderBean.setRetailerId(resultSet
						.getInt(TableConstants.SAO_RETAILER_ID));
				orderBean.setRetailerOrgId(resultSet
						.getInt(TableConstants.SAO_RETAILER_ORG_ID));
				orderBean.setRetOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				orderBean.setOrderDate(resultSet
						.getDate(TableConstants.SAO_ORDER_DATE));

				if (resultSet.getString(TableConstants.SBO_ORDER_STATUS)
						.equalsIgnoreCase("APPROVED")) {
					orderBean.setOrderStatus("Approved");

				}
				if (resultSet.getString(TableConstants.SBO_ORDER_STATUS)
						.equalsIgnoreCase("PROCESSED")) {
					orderBean.setOrderStatus("Processed");

				}
				if (resultSet.getString(TableConstants.SBO_ORDER_STATUS)
						.equalsIgnoreCase("SEMI_PROCESSED")) {
					orderBean.setOrderStatus("Semi Processed");

				}
				if (resultSet.getString(TableConstants.SBO_ORDER_STATUS)
						.equalsIgnoreCase("ASSIGNED")) {
					orderBean.setOrderStatus("Assigned");

				}

				searchResults.add(orderBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);

		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public void setTotalOrderedBooks(int totalOrderedBooks) {
		this.totalOrderedBooks = totalOrderedBooks;
	}

	public boolean SuccessStatusUpdate(int oderId, String status)
			throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;
		ResultSet resultSet = null;
		try {

			OrderBean orderBean = null;
			List<OrderBean> searchResults = new ArrayList<OrderBean>();

			 
			connection = DBConnect.getConnection();
			String query = QueryManager.getST1AppOrderQuery();
			String query1 = "update st_se_agent_order set order_status=? where order_id=?";
			String query2 = "update st_se_agent_order_invoices set order_status=? where order_id=?";
			System.out.println("-----Query----::" + query);
			statement = connection.prepareStatement(query1);
			statement1 = connection.prepareStatement(query2);

			statement.setString(1, status);
			statement.setInt(2, oderId);
			int ii = statement.executeUpdate();

			statement1.setString(1, status);
			statement1.setInt(2, oderId);
			int i = statement1.executeUpdate();
			if (i > 0 || ii > 0) {

				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// return null;

	}

}
