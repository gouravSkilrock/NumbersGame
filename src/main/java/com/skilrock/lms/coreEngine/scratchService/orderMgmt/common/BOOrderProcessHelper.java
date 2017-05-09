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

package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.InvOrderBean;
import com.skilrock.lms.beans.OrderBean;
import com.skilrock.lms.beans.OrderedGameBean;
import com.skilrock.lms.beans.OrgAddressBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.scratchService.dataMgmt.daoImpl.ScratchGameDataDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This is a helper class providing methods for creating BO initiated orders
 * 
 * @author Skilrock Technologies
 * 
 */
public class BOOrderProcessHelper {

	// INVENTORY ORDER PROCESS HELPER OF LMS15MAY
	private int gameID;

	private int totalOrderedBooks;

	/*
	 * public int getRetailerOrgId(List<OrgBean> retOrgList, String retOrgName) {
	 * 
	 * 
	 * if(retOrgList != null){ OrgBean bean = null; for(int i=0; i<retOrgList.size();
	 * i++ ){ bean = retOrgList.get(i);
	 * if("retOrgName".equals(bean.getOrgName())){ return bean.getOrgId(); } } }
	 * return -1; }
	 */

	/**
	 * This method is used for dispatching the agent order
	 * 
	 * @param invOrderList
	 * @param orderId
	 * @param agentOrgId
	 * @param rootPath
	 * @throws LMSException
	 */
	/*public void dispatchOrder(List<InvOrderBean> invOrderList, int total,
			int orderId, String boOrgName, int userOrgID, int agentOrgId,
			String rootPath, HttpSession session, int userId)
			throws LMSException {
		System.out.println("Start to Dispach");
		InvOrderBean invOrderBean = null;
		OrderedGameBean gameBean = null;

		if (invOrderList != null) {

			Connection connection = null;
			PreparedStatement boMasterStmt = null;
			PreparedStatement LMSMasterStmt = null;
			PreparedStatement boAgentStmt = null;
			PreparedStatement boOrdGameStmt = null;
			PreparedStatement boReceiptStmt = null;
			PreparedStatement boReceiptNoGenStmt = null;

			PreparedStatement boReceiptMappingStmt = null;
			PreparedStatement boOrderStmt = null;

			PreparedStatement boReceiptGenMappingStmt = null;
			PreparedStatement boInvoiceDCMappingStmt = null;

			PreparedStatement gameInvStatusStmt = null;
			PreparedStatement gameInvDetailStmt = null;
			PreparedStatement orderInvoicesPrtSt = null;
			Statement totalDispatch = null;
			ResultSet totalDispatchResult = null;

			ResultSet rsMaster = null;

			String boMasterQuery = null;
			String LMSMasterQuery = null;
			String boAgentQuery = null;
			String boOrdGamesQuery = null;
			String boReceiptQuery = null;
			String boReceiptMappingQuery = null;
			String boOrderQuery = null;
			String gameInvStatusQuery = null;
			String invQuery = null;
			String orderInvoicesQuery = null;

			String gameInvDetailQuery = null;
			String bookQuery = null;

			// added by yogehs to inser vat details into game master
			String gameMasterQuery = null;
			PreparedStatement gameMasterStmt = null;

			// String countDispatchedTotal = "select nbr_of_books_dlvrd from
			// st_se_bo_ordered_games where order_id="+orderId;
			String countDispatchedTotal = QueryManager
					.getST5ToatlDispatchQuery()
					+ orderId;
			List<Integer> trnIdList = new ArrayList<Integer>();
			int masterTrnId = -1;
			int totalDispatchedForOrder = 0;

			try {

				 
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);

				totalDispatch = connection.createStatement();

				totalDispatchResult = totalDispatch
						.executeQuery(countDispatchedTotal);
				*//**
				 * ///Checking that wheather total books have been dispatched or
				 * not .On the basis of that Order would be Processed or
				 * SemiProcessed
				 *//*

				while (totalDispatchResult.next()) {
					totalDispatchedForOrder = totalDispatchedForOrder
							+ totalDispatchResult.getInt("nbr_of_books_dlvrd");
					if (totalDispatchedForOrder == total) {
						break;

					}
				}
				System.out.println("--------totalDispatchedForOrder"
						+ totalDispatchedForOrder + "-------");

				boMasterQuery = QueryManager.insertInBOTransactionMaster();
				boMasterStmt = connection.prepareStatement(boMasterQuery);

				LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
				LMSMasterStmt = connection.prepareStatement(LMSMasterQuery);

				boAgentQuery = QueryManager.getST1BOAgentQuery();
				boAgentStmt = connection.prepareStatement(boAgentQuery);

				boOrdGamesQuery = QueryManager.getST1BOOrdGamesUpdQuery();
				boOrdGameStmt = connection.prepareStatement(boOrdGamesQuery);

				java.sql.Timestamp currentDate = new java.sql.Timestamp(
						new Date().getTime());

				// boReceiptQuery = QueryManager.getST1BOReceiptsQuery();
				// boReceiptStmt = connection.prepareStatement(boReceiptQuery);

				boReceiptMappingQuery = QueryManager
						.insertBOReceiptTrnMapping();
				boReceiptMappingStmt = connection
						.prepareStatement(boReceiptMappingQuery);

				boOrderQuery = QueryManager.getST1BOOrdUpdQuery();
				boOrderStmt = connection.prepareStatement(boOrderQuery);

				gameInvStatusQuery = QueryManager
						.getST1BOUpdGameInvStatusQuery();

				gameInvDetailQuery = QueryManager.getST1InvDetailInsertQuery();
				gameInvDetailStmt = connection
						.prepareStatement(gameInvDetailQuery);

				orderInvoicesQuery = QueryManager.getST1OrderInInsertQuery();
				orderInvoicesPrtSt = connection
						.prepareStatement(orderInvoicesQuery);

				gameMasterQuery = QueryManager.insertVatDetailsintoGameMaster();
				gameMasterStmt = connection.prepareStatement(gameMasterQuery);

				String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
						+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
						+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement detHistoryInsPstmt = connection
						.prepareStatement(detHistoryInsQuery);

				double mrpAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double creditAmt = 0.0;
				double vatAmt = 0.0;
				double taxableSale = 0.0;
				double agtSaleCommRate = 0.0;
				int nbrOfbooksApp = 0;
				int nbrBooksalreadyDispatched = 0;
				int nbrOfBooksDispatched = 0;
				int totalDispatched = 0;

				double govtComm = 0.0;

				int gameId = -1;
				List<PackBean> packList = null;
				List<BookBean> bookList = null;
				PackBean packBean = null;
				BookBean bookBean = null;

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY
				// generated_id DESC LIMIT 1 ";
				boReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getBOLatestReceiptNb());
				boReceiptNoGenStmt.setString(1, "INVOICE");
				ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = null;
				autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("INVOICE",
						lastRecieptNoGenerated, "BO");

				// get auto generated delivery Challan number
				// String getLatestDCNumber="SELECT * from
				// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY
				// generated_id DESC LIMIT 1 ";
				boReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getBOLatestReceiptNb());
				boReceiptNoGenStmt.setString(1, "DLCHALLAN");
				ResultSet DCRs = boReceiptNoGenStmt.executeQuery();
				String lastDCNoGenerated = null;

				while (DCRs.next()) {
					lastDCNoGenerated = DCRs.getString("generated_id");
				}

				String autoGeneDCNo = null;
				autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DLCHALLAN",
						lastDCNoGenerated, "BO");
				System.out.println("lastDCNoGenerated " + lastDCNoGenerated
						+ "autoGeneDCNo " + autoGeneDCNo);

				// insert in receipt transaction master for invoice
				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				boReceiptStmt.setString(1, "BO");
				boReceiptStmt.executeUpdate();

				ResultSet rs = boReceiptStmt.getGeneratedKeys();
				int invoiceId = -1;
				while (rs.next()) {
					invoiceId = rs.getInt(1);
				}

				// insert in bo receipts

				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInBOReceipts());

				boReceiptStmt.setInt(1, invoiceId);
				boReceiptStmt.setString(2, "INVOICE");
				boReceiptStmt.setInt(3, agentOrgId);
				boReceiptStmt.setString(4, "AGENT");
				boReceiptStmt.setString(5, autoGeneRecieptNo);
				boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());
				// System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhh : " +
				// boReceiptStmt );
				// boReceiptStmt.setString(1,autoGeneRecieptNo);
				
				 * boReceiptStmt.setString(1,"INVOICE");
				 * boReceiptStmt.setInt(2,agentOrgId);
				 

				boReceiptStmt.execute();

				// insert in receipt transaction master for delivery challan
				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				boReceiptStmt.setString(1, "BO");
				boReceiptStmt.executeUpdate();

				ResultSet rsDC = boReceiptStmt.getGeneratedKeys();
				int DCId = -1;
				while (rsDC.next()) {
					DCId = rsDC.getInt(1);
				}

				// insert bo reciept id for delivery challan

				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInBOReceipts());

				boReceiptStmt.setInt(1, DCId);
				boReceiptStmt.setString(2, "DLCHALLAN");
				boReceiptStmt.setInt(3, agentOrgId);
				boReceiptStmt.setString(4, "AGENT");
				boReceiptStmt.setString(5, autoGeneDCNo);
				boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());

				
				 * boReceiptStmt.setString(1,"DLCHALLAN");
				 * boReceiptStmt.setInt(2,agentOrgId);
				 

				boReceiptStmt.execute();

				for (int i = 0; i < invOrderList.size(); i++) {

					invOrderBean = invOrderList.get(i);

					gameBean = invOrderBean.getOrderedGameBean();
					gameId = gameBean.getGameId();
					nbrOfbooksApp = gameBean.getNbrOfBooksApp();
					nbrBooksalreadyDispatched = gameBean.getNbrOfBooksDlvrd();

					int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();

					nbrOfBooksDispatched = nbrBooksalreadyDispatched
							+ getDispatchedNbrOfBooks(invOrderBean,
									nbrOfBooksPerPack);
					totalDispatchedForOrder = totalDispatchedForOrder
							+ getDispatchedNbrOfBooks(invOrderBean,
									nbrOfBooksPerPack);
					int currentlyDispatched = getDispatchedNbrOfBooks(
							invOrderBean, nbrOfBooksPerPack);
					if (currentlyDispatched > 0) {
						// totalDispatched=nbrBooksalreadyDispatched+nbrOfBooksDispatched+totalDispatchedForOrder;

						// fetch game details from game master
						String fetchGameDetQuery = "select nbr_of_tickets_per_book from st_se_game_master where game_id ="
								+ gameId;
						Statement fetchGameDetStmt = connection
								.createStatement();
						ResultSet fetchGameDetRs = fetchGameDetStmt
								.executeQuery(fetchGameDetQuery);
						int noOfTktPerBooks = -1;
						if (fetchGameDetRs.next()) {
							noOfTktPerBooks = fetchGameDetRs
									.getInt("nbr_of_tickets_per_book");
						}

						// insert in LMS transaction master
						LMSMasterStmt.setString(1, "BO");
						LMSMasterStmt.executeUpdate();
						rsMaster = null;
						rsMaster = LMSMasterStmt.getGeneratedKeys();

						while (rsMaster.next()) {
							masterTrnId = rsMaster.getInt(1);
							trnIdList.add(masterTrnId);
						}

						// insert in bo transaction master

						boMasterStmt.setInt(1, masterTrnId);
						boMasterStmt.setInt(2, userId);
						boMasterStmt.setInt(3, userOrgID);
						boMasterStmt.setString(4, "AGENT");
						boMasterStmt.setInt(5, agentOrgId);
						boMasterStmt.setTimestamp(6, currentDate);
						boMasterStmt.setString(7, "SALE");

						
						 * boMasterStmt.setString(1,"AGENT");
						 * boMasterStmt.setInt(2,agentOrgId);
						 * boMasterStmt.setTimestamp(3,currentDate);
						 * boMasterStmt.setString(4,"SALE");
						 

						boMasterStmt.execute();

						boAgentStmt.setInt(1, masterTrnId);
						boAgentStmt.setInt(2, currentlyDispatched);
						boAgentStmt.setInt(3, gameId);
						boAgentStmt.setInt(4, agentOrgId);

						mrpAmt = currentlyDispatched * gameBean.getBookPrice();
						boAgentStmt.setDouble(5, mrpAmt);

						govtComm = mrpAmt * gameBean.getGovtComm() * .01;

						agtSaleCommRate = gameBean.getAgtSaleCommRate()
								+ gameBean.getAgtGameCommVariance();
						commAmt = mrpAmt * agtSaleCommRate * 0.01;
						boAgentStmt.setDouble(6, commAmt);

						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						// added by yogesh to calculate vat amount
						vatAmt = CommonMethods.calculateVat(mrpAmt,
								agtSaleCommRate,
								gameBean.getPrizePayOutRatio(), gameBean
										.getGovtComm(), gameBean.getVat(),
								gameBean.getGovtCommRule(), gameBean
										.getFixedAmt(), gameBean
										.getTicketsInScheme());
						
						 * double
						 * deducted_sale=(100-(gameBean.getAgtSaleCommRate() +
						 * gameBean.getPrizePayOutRatio()
						 * +gameBean.getGovtComm())); double
						 * vat_plus_taxablesale = (mrpAmt*deducted_sale)/100;
						 * double
						 * new_taxable_sale=(vat_plus_taxablesale*100)/(100+gameBean.getVat());
						 * double
						 * new_vat=(vat_plus_taxablesale*gameBean.getVat())/(100+gameBean.getVat());
						 
						System.out.println("mrp :: " + mrpAmt + "ppr :: "
								+ gameBean.getPrizePayOutRatio()
								+ "gov comm ::" + gameBean.getGovtComm()
								+ "agt net comm :: " + agtSaleCommRate);

						// vatAmt=(mrpAmt-(mrpAmt
						// *((gameBean.getAgtSaleCommRate() +
						// gameBean.getPrizePayOutRatio()
						// +gameBean.getGovtComm())/100))) * ((gameBean.getVat()
						// * 0.01)/(1+(gameBean.getVat() * 0.01)));
						// taxableSale=(mrpAmt-(mrpAmt
						// *((gameBean.getAgtSaleCommRate() +
						// gameBean.getPrizePayOutRatio()
						// +gameBean.getGovtComm())/100))) *
						// (1/(1+(gameBean.getVat() * 0.01)));
						
						 * taxableSale = (((mrpAmt * (100 - (agtSaleCommRate +
						 * gameBean.getPrizePayOutRatio() + gameBean
						 * .getGovtComm()))) / 100) * 100) / (100 +
						 * gameBean.getVat());
						 
						// taxableSale=vatAmt/(gameBean.getVat() * 0.01);
						taxableSale = CommonMethods.calTaxableSale(mrpAmt,
								agtSaleCommRate,
								gameBean.getPrizePayOutRatio(), gameBean
										.getGovtComm(), gameBean.getVat());
						System.out
								.println("vat anount is by yogesh  " + vatAmt);
						System.out.println("taxable sale is  by yogesh "
								+ taxableSale);

						boAgentStmt.setDouble(7, netAmt);
						boAgentStmt.setString(8, "SALE");

						boAgentStmt.setDouble(9, vatAmt);
						boAgentStmt.setDouble(10, taxableSale);
						boAgentStmt.setDouble(11, govtComm);

						boAgentStmt.execute();

						gameMasterStmt.setDouble(1, gameBean.getVatBalance()
								+ vatAmt);
						gameMasterStmt.setDouble(2, gameId);

						gameMasterStmt.execute();

						boOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						boOrdGameStmt.setInt(2, orderId);
						boOrdGameStmt.setInt(3, gameId);

						boOrdGameStmt.execute();

						packList = invOrderBean.getPackList();

						// check book Status on dispatch
						String newBookStatus = "ACTIVE";

						if (packList != null) {
							invQuery = gameInvStatusQuery
									+ " and pack_nbr = ? ";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String packNbr = null;

							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();

								gameInvStatusStmt.setString(1, "AGENT");
								gameInvStatusStmt.setInt(2, agentOrgId);
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
									gameInvDetailStmt.setString(5, "AGENT");
									gameInvDetailStmt.setInt(6, agentOrgId);
									gameInvDetailStmt.setTimestamp(7,
											currentDate);
									gameInvDetailStmt.setString(8, "BO");
									gameInvDetailStmt.setDouble(9,
											agtSaleCommRate);
									gameInvDetailStmt.setDouble(10, gameBean
											.getGovtComm());
									gameInvDetailStmt.setObject(11, null);
									gameInvDetailStmt.execute();

									// insert into detail history table Added by
									// arun
									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, books
											.get(k));
									detHistoryInsPstmt.setString(3, "AGENT");
									detHistoryInsPstmt.setInt(4, agentOrgId);
									detHistoryInsPstmt.setTimestamp(5,
											currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, userId);
									detHistoryInsPstmt.setInt(8,
											noOfTktPerBooks);
									// System.out.println("detHistoryInsPstmt ==
									// "+detHistoryInsPstmt);
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
									// ---------------------
								}

							}
						}

						bookList = invOrderBean.getBookList();
						if (bookList != null) {
							invQuery = gameInvStatusQuery
									+ " and book_nbr = ? ";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "AGENT");
								gameInvStatusStmt.setInt(2, agentOrgId);
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

								packNbr = getPackNbrForBook(connection, gameId,
										bookNbr);

								gameInvDetailStmt.setInt(1, masterTrnId);
								gameInvDetailStmt.setInt(2, gameId);
								gameInvDetailStmt.setString(3, packNbr);
								gameInvDetailStmt.setString(4, bookNbr);
								gameInvDetailStmt.setString(5, "AGENT");
								gameInvDetailStmt.setInt(6, agentOrgId);
								gameInvDetailStmt.setTimestamp(7, currentDate);
								gameInvDetailStmt.setString(8, "BO");
								gameInvDetailStmt.setDouble(9, agtSaleCommRate);
								gameInvDetailStmt.setDouble(10, gameBean
										.getGovtComm());
								gameInvDetailStmt.setObject(11, null);
								gameInvDetailStmt.execute();

								// insert into detail history table Added by
								// arun
								detHistoryInsPstmt.setInt(1, gameId);
								detHistoryInsPstmt.setString(2, bookNbr);
								detHistoryInsPstmt.setString(3, "AGENT");
								detHistoryInsPstmt.setInt(4, agentOrgId);
								detHistoryInsPstmt.setTimestamp(5, currentDate);
								detHistoryInsPstmt.setInt(6, userOrgID);
								detHistoryInsPstmt.setInt(7, userId);
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
								// System.out.println("detHistoryInsPstmt ==
								// "+detHistoryInsPstmt);
								detHistoryInsPstmt.execute();
								// ---------------------

							}
						}
						if (total == totalDispatchedForOrder) {
							boOrderStmt.setString(1, "PROCESSED");
							boOrderStmt.setInt(2, orderId);

							boOrderStmt.execute();
							System.out
									.println("Total aaproved and total dispatched are equal");
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, agentOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.execute();

						} else {
							boOrderStmt.setString(1, "SEMI_PROCESSED");
							boOrderStmt.setInt(2, orderId);
							boOrderStmt.execute();

							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setInt(2, invoiceId);
							orderInvoicesPrtSt.setInt(3, agentOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "SEMI_PROCESSED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.execute();
						}
					}
				}
				for (int i = 0; i < trnIdList.size(); i++) {
					boReceiptMappingStmt.setInt(1, invoiceId);
					boReceiptMappingStmt.setInt(2, trnIdList.get(i));
					boReceiptMappingStmt.execute();

				}

				// insert for receipt and transaction mapping table for delivery
				// challan
				for (int i = 0; i < trnIdList.size(); i++) {
					boReceiptMappingStmt.setInt(1, DCId);
					boReceiptMappingStmt.setInt(2, trnIdList.get(i));
					boReceiptMappingStmt.execute();

				}

				
				 * //insert into recipt gen reciept mapping String
				 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
				 * boReceiptGenMappingStmt=connection.prepareStatement(updateBoRecieptGenMapping);
				 * boReceiptGenMappingStmt.setInt(1,invoiceId);
				 * boReceiptGenMappingStmt.setString(2,autoGeneRecieptNo);
				 * boReceiptGenMappingStmt.setString(3,"INVOICE");
				 * boReceiptGenMappingStmt.executeUpdate();
				 * 
				 * 
				 * //insert for delivery challan into receipt_gen_mapping
				 * //String
				 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
				 * boReceiptGenMappingStmt=connection.prepareStatement(updateBoRecieptGenMapping);
				 * boReceiptGenMappingStmt.setInt(1,DCId);
				 * boReceiptGenMappingStmt.setString(2,autoGeneDCNo);
				 * boReceiptGenMappingStmt.setString(3,"DLCHALLAN");
				 * boReceiptGenMappingStmt.executeUpdate();
				 

				// insert into invoice and delivery challan mapping table
				String insertInvoiceDCMapping = "insert into st_se_bo_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
				boInvoiceDCMappingStmt = connection
						.prepareStatement(insertInvoiceDCMapping);
				boInvoiceDCMappingStmt.setInt(1, invoiceId);
				boInvoiceDCMappingStmt.setString(2, autoGeneRecieptNo);
				boInvoiceDCMappingStmt.setString(3, autoGeneDCNo);
				boInvoiceDCMappingStmt.executeUpdate();

				// for org credit updation
				System.out.println("Org Id For Credit Update:" + agentOrgId
						+ ":" + creditAmt);
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", agentOrgId,
						0, "AGENT", 0, connection);
				
				if(!isValid)
					throw new LMSException();
				
				boolean isUpdateDone = OrgCreditUpdation
						.updateCreditLimitForAgent(agentOrgId, "SALE",
								creditAmt, connection);

				connection.commit();

				session.setAttribute("DEL_CHALLAN_ID", DCId);
				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportBO(invoiceId, boOrgName,
							userOrgID, rootPath);
				}

			} catch (SQLException se) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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

	}*/
	
	public int assignOrder(List<InvOrderBean> invOrderList, int total, int orderId, String boOrgName, int userOrgID, int agentOrgId, String rootPath, int userId) throws LMSException {
		System.out.println("Start to Dispach");

		int DCId = -1;
		int warehouseId = -1;
		InvOrderBean invOrderBean = null;
		OrderedGameBean gameBean = null;
		if (invOrderList != null) {

			Connection connection = null;			
			PreparedStatement boOrdGameStmt = null;
			PreparedStatement boReceiptStmt = null;
			PreparedStatement boReceiptNoGenStmt = null;		
			PreparedStatement boOrderStmt = null;		
			PreparedStatement gameInvStatusStmt = null;
			PreparedStatement gameInvDetailStmt = null;
			PreparedStatement orderInvoicesPrtSt = null;
			Statement totalDispatch = null;
			ResultSet totalDispatchResult = null;

			//String boMasterQuery = null;
			//String LMSMasterQuery = null;
			//String boAgentQuery = null;
			String boOrdGamesQuery = null;
			//String boReceiptQuery = null;
			//String boReceiptMappingQuery = null;
			String boOrderQuery = null;
			String gameInvStatusQuery = null;
			String invQuery = null;
			String orderInvoicesQuery = null;

			String gameInvDetailQuery = null;
			String bookQuery = null;

			// added by yogehs to inser vat details into game master
			String gameMasterQuery = null;
			PreparedStatement gameMasterStmt = null;

			// String countDispatchedTotal = "select nbr_of_books_dlvrd from
			// st_se_bo_ordered_games where order_id="+orderId;
			String countDispatchedTotal = QueryManager
					.getST5ToatlDispatchQuery()
					+ orderId;
			//List<Integer> trnIdList = new ArrayList<Integer>();
			//int masterTrnId = -1;
			int totalDispatchedForOrder = 0;

			try {

				 
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);

				totalDispatch = connection.createStatement();

				totalDispatchResult = totalDispatch
						.executeQuery(countDispatchedTotal);
				/**
				 * ///Checking that wheather total books have been dispatched or
				 * not .On the basis of that Order would be Processed or
				 * SemiProcessed
				 */

				while (totalDispatchResult.next()) {
					totalDispatchedForOrder = totalDispatchedForOrder
							+ totalDispatchResult.getInt("nbr_of_books_dlvrd");
					if (totalDispatchedForOrder == total) {
						break;

					}
				}
				System.out.println("--------totalDispatchedForOrder"
						+ totalDispatchedForOrder + "-------");

				
				boOrdGamesQuery = QueryManager.getST1BOOrdGamesUpdQuery();
				boOrdGameStmt = connection.prepareStatement(boOrdGamesQuery);

				java.sql.Timestamp currentDate = new java.sql.Timestamp(
						new Date().getTime());

				boOrderQuery = QueryManager.getST1BOOrdUpdQuery();
				boOrderStmt = connection.prepareStatement(boOrderQuery);

				gameInvStatusQuery = QueryManager
						.getST1BOUpdGameInvStatusQuery();

				gameInvDetailQuery = QueryManager.getST1InvDetailWithWarehouseInsertQuery();
				gameInvDetailStmt = connection
						.prepareStatement(gameInvDetailQuery);

				orderInvoicesQuery = QueryManager.getST1OrderInInsertQuery();
				orderInvoicesPrtSt = connection
						.prepareStatement(orderInvoicesQuery);

				/*gameMasterQuery = QueryManager.insertVatDetailsintoGameMaster();
				gameMasterStmt = connection.prepareStatement(gameMasterQuery);*/

				String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
						+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
						+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement detHistoryInsPstmt = connection
						.prepareStatement(detHistoryInsQuery);
				//int nbrOfbooksApp = 0;
				int nbrBooksalreadyDispatched = 0;
				int nbrOfBooksDispatched = 0;
				//int totalDispatched = 0;

			

				int gameId = -1;
				List<PackBean> packList = null;
				List<BookBean> bookList = null;
				PackBean packBean = null;
				BookBean bookBean = null;

				
				boReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getBOLatestReceiptNb());
				boReceiptNoGenStmt.setString(1, "DLCHALLAN");
				ResultSet DCRs = boReceiptNoGenStmt.executeQuery();
				String lastDCNoGenerated = null;

				while (DCRs.next()) {
					lastDCNoGenerated = DCRs.getString("generated_id");
				}

				String autoGeneDCNo = null;
				autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DLCHALLAN",
						lastDCNoGenerated, "BO");
				System.out.println("lastDCNoGenerated " + lastDCNoGenerated
						+ "autoGeneDCNo " + autoGeneDCNo);

				

				// insert in receipt transaction master for delivery challan
				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				boReceiptStmt.setString(1, "BO");
				boReceiptStmt.executeUpdate();

				ResultSet rsDC = boReceiptStmt.getGeneratedKeys();
				while (rsDC.next()) {
					DCId = rsDC.getInt(1);
				}

				// insert bo reciept id for delivery challan

				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInBOReceipts());

				boReceiptStmt.setInt(1, DCId);
				boReceiptStmt.setString(2, "DLCHALLAN");
				boReceiptStmt.setInt(3, agentOrgId);
				boReceiptStmt.setString(4, "AGENT");
				boReceiptStmt.setString(5, autoGeneDCNo);
				boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());

				/*
				 * boReceiptStmt.setString(1,"DLCHALLAN");
				 * boReceiptStmt.setInt(2,agentOrgId);
				 */

				boReceiptStmt.execute();

				for (int i = 0; i < invOrderList.size(); i++) {

					invOrderBean = invOrderList.get(i);

					gameBean = invOrderBean.getOrderedGameBean();
					gameId = gameBean.getGameId();
					//nbrOfbooksApp = gameBean.getNbrOfBooksApp();
					nbrBooksalreadyDispatched = gameBean.getNbrOfBooksDlvrd();

					int nbrOfBooksPerPack = gameBean.getNbrOfBooksPerPack();

					nbrOfBooksDispatched = nbrBooksalreadyDispatched
							+ getDispatchedNbrOfBooks(invOrderBean,
									nbrOfBooksPerPack);
					totalDispatchedForOrder = totalDispatchedForOrder
							+ getDispatchedNbrOfBooks(invOrderBean,
									nbrOfBooksPerPack);
					int currentlyDispatched = getDispatchedNbrOfBooks(
							invOrderBean, nbrOfBooksPerPack);
					if (currentlyDispatched > 0) {
						// totalDispatched=nbrBooksalreadyDispatched+nbrOfBooksDispatched+totalDispatchedForOrder;

						// fetch game details from game master
						String fetchGameDetQuery = "select nbr_of_tickets_per_book from st_se_game_master where game_id ="
								+ gameId;
						Statement fetchGameDetStmt = connection
								.createStatement();
						ResultSet fetchGameDetRs = fetchGameDetStmt
								.executeQuery(fetchGameDetQuery);
						int noOfTktPerBooks = -1;
						if (fetchGameDetRs.next()) {
							noOfTktPerBooks = fetchGameDetRs
									.getInt("nbr_of_tickets_per_book");
						}

						boOrdGameStmt.setInt(1, nbrOfBooksDispatched);
						boOrdGameStmt.setInt(2, orderId);
						boOrdGameStmt.setInt(3, gameId);

						boOrdGameStmt.execute();

						packList = invOrderBean.getPackList();

						if (total == totalDispatchedForOrder) {
							boOrderStmt.setString(1, "ASSIGNED");
							boOrderStmt.setInt(2, orderId);

							boOrderStmt.execute();
							System.out
									.println("Total aaproved and total dispatched are equal");	
							
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setString(2, null);
							orderInvoicesPrtSt.setInt(3, agentOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "ASSIGNED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, DCId);
							orderInvoicesPrtSt.execute();

						} else {
							//boOrderStmt.setString(1, "SEMI_PROCESSED");
							boOrderStmt.setString(1, "SEMI_ASSIGNED");
							boOrderStmt.setInt(2, orderId);
							boOrderStmt.execute();		
							
							orderInvoicesPrtSt.setInt(1, orderId);
							orderInvoicesPrtSt.setString(2, null);
							orderInvoicesPrtSt.setInt(3, agentOrgId);
							orderInvoicesPrtSt.setInt(4, gameId);
							orderInvoicesPrtSt.setString(5, "SEMI_ASSIGNED");
							orderInvoicesPrtSt.setInt(6, nbrOfBooksDispatched);
							orderInvoicesPrtSt.setInt(7, DCId);
							orderInvoicesPrtSt.execute();
						}

						ResultSet rs = orderInvoicesPrtSt.getGeneratedKeys();
						int orderInvoiceDCId = -1;
						while (rs.next()) {
							orderInvoiceDCId = rs.getInt(1);
						}

						String newBookStatus = "ASSIGNED";
						if (packList != null) {
							invQuery = gameInvStatusQuery
									+ " and pack_nbr = ? ";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String packNbr = null;

							for (int j = 0; j < packList.size(); j++) {
								packBean = packList.get(j);
								packNbr = packBean.getPackNumber();

								gameInvStatusStmt.setString(1, "AGENT");
								gameInvStatusStmt.setInt(2, agentOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);
								// Added by arun on update status table
								gameInvStatusStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									gameInvStatusStmt.setInt(5, noOfTktPerBooks);
								} else {
									gameInvStatusStmt.setInt(5, 0);
								}
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, packNbr);
								gameInvStatusStmt.execute();

								List<String> books = getBooksForPack(
										connection, gameId, packNbr);

								for (int k = 0; k < books.size(); k++) {
									warehouseId = ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, books.get(k));
									gameInvDetailStmt.setString(1, null);
									gameInvDetailStmt.setInt(2, gameId);
									gameInvDetailStmt.setString(3, packNbr);
									gameInvDetailStmt
											.setString(4, books.get(k));
									gameInvDetailStmt.setString(5, "AGENT");
									gameInvDetailStmt.setInt(6, agentOrgId);
									gameInvDetailStmt.setTimestamp(7,
											currentDate);
									gameInvDetailStmt.setString(8, "BO");
									gameInvDetailStmt.setString(9, newBookStatus);
									gameInvDetailStmt.setInt(10, orderInvoiceDCId);
									gameInvDetailStmt.setInt(11, warehouseId);
									/*gameInvDetailStmt.setDouble(9,
											agtSaleCommRate);
									gameInvDetailStmt.setDouble(10, gameBean
											.getGovtComm());*/
									//gameInvDetailStmt.setObject(9, null);
									gameInvDetailStmt.execute();

									// insert into detail history table Added by
									// arun
									detHistoryInsPstmt.setInt(1, gameId);
									detHistoryInsPstmt.setString(2, books
											.get(k));
									detHistoryInsPstmt.setString(3, "AGENT");
									detHistoryInsPstmt.setInt(4, agentOrgId);
									detHistoryInsPstmt.setTimestamp(5,
											currentDate);
									detHistoryInsPstmt.setInt(6, userOrgID);
									detHistoryInsPstmt.setInt(7, userId);
									detHistoryInsPstmt.setInt(8,
											noOfTktPerBooks);
									// System.out.println("detHistoryInsPstmt ==
									// "+detHistoryInsPstmt);
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

						bookList = invOrderBean.getBookList();
						if (bookList != null) {
							invQuery = gameInvStatusQuery
									+ " and book_nbr = ? ";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								bookBean = bookList.get(j);
								bookNbr = bookBean.getBookNumber();
								gameInvStatusStmt.setString(1, "AGENT");
								gameInvStatusStmt.setInt(2, agentOrgId);
								gameInvStatusStmt.setString(3, newBookStatus);

								// Added by arun on update status table
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

								packNbr = getPackNbrForBook(connection, gameId,
										bookNbr);
								
								warehouseId = ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, bookNbr);
								gameInvDetailStmt.setString(1, null);
								gameInvDetailStmt.setInt(2, gameId);
								gameInvDetailStmt.setString(3, packNbr);
								gameInvDetailStmt.setString(4, bookNbr);
								gameInvDetailStmt.setString(5, "AGENT");
								gameInvDetailStmt.setInt(6, agentOrgId);
								gameInvDetailStmt.setTimestamp(7, currentDate);
								gameInvDetailStmt.setString(8, "BO");
								gameInvDetailStmt.setString(9, newBookStatus);
								gameInvDetailStmt.setInt(10, orderInvoiceDCId);
								gameInvDetailStmt.setInt(11, warehouseId);
								/*gameInvDetailStmt.setDouble(9, agtSaleCommRate);
								gameInvDetailStmt.setDouble(10, gameBean
										.getGovtComm());*/
								//gameInvDetailStmt.setObject(11, null);
								gameInvDetailStmt.execute();

								// insert into detail history table Added by
								// arun
								detHistoryInsPstmt.setInt(1, gameId);
								detHistoryInsPstmt.setString(2, bookNbr);
								detHistoryInsPstmt.setString(3, "AGENT");
								detHistoryInsPstmt.setInt(4, agentOrgId);
								detHistoryInsPstmt.setTimestamp(5, currentDate);
								detHistoryInsPstmt.setInt(6, userOrgID);
								detHistoryInsPstmt.setInt(7, userId);
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
								// System.out.println("detHistoryInsPstmt ==
								// "+detHistoryInsPstmt);
								detHistoryInsPstmt.execute();
								// ---------------------

							}
						}
					}
				}
				connection.commit();

				/*if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportBO(invoiceId, boOrgName,
							userOrgID, rootPath);
				}*/

			} catch (SQLException se) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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
	 * This method returns the address of the organization
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
	public List fetchOrderDetails(int orderId, int agtOrgId)
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

			String query = QueryManager.getST1AppOrderGamesQuery();
			query = query + orderId;

			System.out.println("-----Query11111----::" + query);

			resultSet = statement.executeQuery(query);
			int nbrBooksToDispatch = 0;
			double bookPrice = 0.0;
			while (resultSet.next()) {

				orderedGameBean = new OrderedGameBean();

				orderedGameBean.setOrderId(resultSet
						.getInt(TableConstants.SBOG_ORDER_ID));
				orderedGameBean.setGameId(resultSet
						.getInt(TableConstants.SBOG_GAME_ID));
				orderedGameBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				orderedGameBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				orderedGameBean.setNbrOfBooksApp(resultSet
						.getInt(TableConstants.SBOG_NBR_OF_BOOKS_APP));

				// here added by yogesh
				// added by yogesh to get agent commission variance for every
				// game
				double agtGameCommVariace = 0.0;
				String getAgtCommVariance = "select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
						+ agtOrgId
						+ " and game_id="
						+ resultSet.getInt(TableConstants.SBOG_GAME_ID);
				statementCommVariance = connection.createStatement();
				resultSetCommVariance = statementCommVariance
						.executeQuery(getAgtCommVariance);
				while (resultSetCommVariance.next()) {
					agtGameCommVariace = resultSetCommVariance
							.getDouble("sale_comm_variance");

				}
				orderedGameBean.setAgtGameCommVariance(agtGameCommVariace);

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
				orderedGameBean.setAgtSaleCommRate(resultSet
						.getDouble(TableConstants.SGM_AGT_SALE_RATE));
				if (!(nbrBooksToDispatch == 0)) {
					searchResults.add(orderedGameBean);
				}
			}
			this.setTotalOrderedBooks(totalApprovedBooks);
			System.out.println("----total  approved for order"
					+ totalApprovedBooks + "---------");
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

	/*
	 * This method generates the BO initiated order for the agent. Returns true
	 * if successfully generated @param cartList @param agtOrgList @param
	 * agtOrgName @return boolean @throws LMSException
	 */
	public int generateOrder(List<GameBean> cartList,
			List<OrgBean> agtOrgList, String agtOrgName) throws LMSException {

		int agtOrgId = -1;
		int agtId = -1;
		if (agtOrgList != null) {
			OrgBean bean = null;
			for (int i = 0; i < agtOrgList.size(); i++) {
				bean = agtOrgList.get(i);
				System.out.println("---OrG Name::" + bean.getOrgName());
				if (agtOrgName.equals(bean.getOrgName())) {
					agtOrgId = bean.getOrgId();
					agtId = bean.getUserId();

					break;
				}
			}
		}

		Connection connection = null;
		PreparedStatement orderPstmt = null;
		PreparedStatement gamePstmt = null;

		ResultSet resultSet = null;
		int orderId = -1;

		if (cartList != null) {
			int size = cartList.size();
			QueryManager queryManager = null;
			GameBean gameBean = null;

			String orderQuery = null;
			String gameQuery = null;

			if (size > 0) {
				try {

					// create database connection
					 
					connection = DBConnect.getConnection();
					connection.setAutoCommit(false);

					orderQuery = QueryManager.getST1InsertBOOrderQuery();
					orderPstmt = connection.prepareStatement(orderQuery);

					gameQuery = QueryManager
							.getST1AutoInsertBOOrderedGamesQuery();
					gamePstmt = connection.prepareStatement(gameQuery);

					// set parameters for insert into order table

					orderPstmt.setInt(1, agtId);
					orderPstmt.setInt(2, agtOrgId);
					orderPstmt.setDate(3, new java.sql.Date(new Date()
							.getTime()));

					orderPstmt.setString(4, "APPROVED");
					orderPstmt.setString(5, "Y");

					orderPstmt.execute();
					resultSet = orderPstmt.getGeneratedKeys();

					while (resultSet.next()) {
						orderId = resultSet.getInt(1);
					}

					System.out.println("OrderId::" + orderId);

					// set parameters for insert into ordered games table

					for (int i = 0; i < size; i++) {
						gameBean = cartList.get(i);

						System.out.println("1:" + gameBean.getGameId());
						System.out.println("2:" + gameBean.getOrderedQty());

						gamePstmt.setInt(1, orderId);
						gamePstmt.setInt(2, gameBean.getGameId());
						gamePstmt.setInt(3, gameBean.getOrderedQty());
						gamePstmt.setInt(4, gameBean.getOrderedQty());
						gamePstmt.execute();

					}

					// commit the connection

					connection.commit();
					return orderId;

				} catch (SQLException e) {

					try {
						connection.rollback();
					} catch (SQLException e1) {

						e1.printStackTrace();
						throw new LMSException("Exception During Rollback", e1);
					}
					e.printStackTrace();
					throw new LMSException(e);
				} finally {

					try {
						if (orderPstmt != null) {
							orderPstmt.close();
						}
						if (gamePstmt != null) {
							gamePstmt.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}
		return orderId;
	}

	/*
	 * This method returns the agent list for BO @return String @throws
	 * LMSException
	 */
	public List<OrgBean> getAgents(UserInfoBean userInfo) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {

			OrgBean agentOrgBean = null;
			List<OrgBean> searchResults = new ArrayList<OrgBean>();

			// create database connection
			 
			connection = DBConnect.getConnection();

			// fetch agents for BO
			String query = QueryManager.getST1AgtOrgQuery(); // + " group by
			// name ";

			statement = connection.prepareStatement(query);
			statement.setInt(1, userInfo.getUserOrgId());
			System.out
					.println(userInfo.getUserOrgId() + "========" + statement);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				agentOrgBean = new OrgBean();
				agentOrgBean.setOrgId(resultSet
						.getInt(TableConstants.SOM_ORG_ID));
				agentOrgBean.setOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				agentOrgBean.setUserId(resultSet
						.getInt(TableConstants.SUM_USER_ID));
				System.out.println(agentOrgBean.getOrgName());
				searchResults.add(agentOrgBean);

			}
			System.out.println("=============" + searchResults);
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

	/**
	 * This method returns a list of approved orders
	 * 
	 * @return List
	 * @throws LMSException
	 */
	public List getApprovedOrders() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {

			OrderBean orderBean = null;
			List<OrderBean> searchResults = new ArrayList<OrderBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST1AppOrderQuery();

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				orderBean = new OrderBean();
				orderBean.setOrderId(resultSet
						.getInt(TableConstants.SBO_ORDER_ID));
				orderBean.setAgentOrgId(resultSet
						.getInt(TableConstants.SBO_AGENT_ORG_ID));
				orderBean.setAgentOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				orderBean.setOrderDate(resultSet
						.getDate(TableConstants.SBO_ORDER_DATE));

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
	public List SearchOrder(Map searchMap, int roleId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Statement statement1 = null;
		ResultSet resultSet1 = null;
		try {

			OrderBean orderBean = null;
			List<OrderBean> searchResults = new ArrayList<OrderBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();

			String query = CommonMethods.appendRoleAgentMappingQuery(QueryManager.getST1AppOrderQuery(), "agent_org_id",roleId)
					+ getWhereClause(searchMap) + " group by order_id";

			System.out.println("-----Query----::" + query);
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				orderBean = new OrderBean();
				orderBean.setOrderId(resultSet
						.getInt(TableConstants.SBO_ORDER_ID));
				orderBean.setAgentOrgId(resultSet
						.getInt(TableConstants.SBO_AGENT_ORG_ID));
				orderBean.setAgentOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));

				orderBean.setOrderDate(resultSet
						.getDate(TableConstants.SBO_ORDER_DATE));

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

	/**
	 * This method is used to Close the Status of the Order.
	 * 
	 * @param oderId
	 * @param status
	 * @return
	 * @throws LMSException
	 */

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
			// String query1="update st_se_bo_order set order_status=? where
			// order_id=?";
			String query1 = QueryManager.getST5UpdateBOorderQuery();
			String query2 = QueryManager.getST5UpdateBOorderInvoicesQuery();
			// String query2="update st_se_bo_order_invoices set order_status=?
			// where order_id=?";
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
