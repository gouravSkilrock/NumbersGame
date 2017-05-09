/*
703002008002
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSaleReturnBean;
import com.skilrock.lms.beans.CommVarGovtCommBean;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.OrgInfoBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This helper class provides methods to verify packs and books and to get valid
 * packs and books from verified books at the time of sale return
 * 
 * @author Skilrock Technologies
 * 
 */
public class SalesReturnHelper {
private Log logger = LogFactory.getLog(SalesReturnHelper.class);
	String bookFlag = "Invalid";
	String packFlag = "Invalid";
	
	
	public String doTransaction(int game_id, int org_id, String orgName,
			List<PackBean> packlist, List<BookBean> booklist, String rootPath,
			int userOrgId, int userId, String newBookStatus, Connection conn)
			throws LMSException {

		int receipt_id = 0;
		int transaction_id = 0;
		double ticket_price = 0, book_price = 0;
		int nbr_of_tickets_per_book = 0, nbr_of_books_per_pack = 0;
		double agent_sale_comm_rate = 0;
		double prizePayOutPer = 0.0;
		double vat = 0.0;
		double govtComm = 0.0;
		double vatAmt = 0.0;
		double bookTaxableSale = 0.0;
		double govtCommAmt = 0.0;
		double commAmt = 0.0;
		double netAmt = 0.0;
		double taxableSale = 0.0;
		double vatBalance = 0.0;
		long ticketsInScheme = 0;
		String govtCommRule = null;
		double fixedAmt = 0.0;
		double netAmtOrg = 0.0;
		int DCId = 0;
		String bookNumber = "";
		boolean isBookActivated = true;

		List<Integer> trnIdList = new ArrayList<Integer>();

		try {
			// get books list from packlist and copy to booklist

			// new book status on sales return
			// String newBookStatus = "INACTIVE";
			logger.info("***Return Book Status Should be**************"
					+ newBookStatus);
			if (packlist.size() != 0) {
				PackBean packbean = null;
				BookBean bookbean = null;
				Iterator<PackBean> packListItr = packlist.iterator();
				while (packListItr.hasNext()) {
					packbean = packListItr.next();
					String packNbr = packbean.getPackNumber();
					String querytoGetBookFrmPack = QueryManager
							.getST4BookNbrOfPackNbr();
					PreparedStatement stmtbookFrmPack = conn
							.prepareStatement(querytoGetBookFrmPack);
					stmtbookFrmPack.setString(1, packNbr);
					ResultSet set = stmtbookFrmPack.executeQuery();
					while (set.next()) {

						String bookNbrfromPack = set.getString("book_nbr");

						bookbean = new BookBean();
						bookbean.setBookNumber(bookNbrfromPack);
						bookbean.setValid(true);
						bookbean.setStatus("Book Is Valid");
						booklist.add(bookbean);
					}

				}

			}

			// Getting Game Details using game id
			String querytoGameDetail = QueryManager
					.getST4GameDetailsUsingGameId();
			PreparedStatement stmtgamedeatil = conn
					.prepareStatement(querytoGameDetail);
			stmtgamedeatil.setInt(1, game_id);
			ResultSet rsGameDetail = stmtgamedeatil.executeQuery();
			while (rsGameDetail.next()) {
				ticket_price = rsGameDetail.getDouble("ticket_price");
				nbr_of_tickets_per_book = rsGameDetail
						.getInt("nbr_of_tickets_per_book");
				nbr_of_books_per_pack = rsGameDetail
						.getInt("nbr_of_books_per_pack");
				agent_sale_comm_rate = rsGameDetail
						.getDouble("agent_sale_comm_rate");
				prizePayOutPer = rsGameDetail.getDouble("prize_payout_ratio");
				vat = rsGameDetail.getDouble("vat_amt");
				govtComm = rsGameDetail.getDouble("govt_comm_rate");
				vatBalance = rsGameDetail.getDouble("vat_balance");
				ticketsInScheme = rsGameDetail.getLong("tickets_in_scheme");
				govtCommRule = rsGameDetail.getString("govt_comm_type");
				fixedAmt = rsGameDetail.getDouble("fixed_amt");
			}
			book_price = ticket_price * nbr_of_tickets_per_book;

			BookSaleReturnBean bookSaleRetBean;
			ArrayList<BookSaleReturnBean> bookSaleReturnList = new ArrayList<BookSaleReturnBean>();
			String bookNbr, packNbr = null;
			double commVariance = 0.0;
			double govtCommRate = 0.0;
			ResultSet rsCommVar;
			Iterator iteratorCommVar = booklist.iterator();
			while (iteratorCommVar.hasNext()) {
				bookSaleRetBean = new BookSaleReturnBean();
				bookNbr = ((BookBean) iteratorCommVar.next()).getBookNumber();
				String commVarianceQuery = "select transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_date from st_se_game_inv_detail where current_owner=? and current_owner_id=? and game_id=? and book_nbr=? and transaction_at=? order by transaction_date desc limit 1 ";
				PreparedStatement commVariaceStmt = conn
						.prepareStatement(commVarianceQuery);
				commVariaceStmt.setString(1, "AGENT");
				commVariaceStmt.setInt(2, org_id);
				commVariaceStmt.setInt(3, game_id);
				commVariaceStmt.setString(4, bookNbr);
				commVariaceStmt.setString(5, "BO");
				rsCommVar = commVariaceStmt.executeQuery();
				while (rsCommVar.next()) {
					commVariance = rsCommVar
							.getDouble("transacrion_sale_comm_rate");
					govtCommRate = rsCommVar
							.getDouble("transaction_gov_comm_rate");
				}
				commAmt = commAmt + book_price * commVariance * 0.01;
				netAmt = netAmt + book_price * (1 - commVariance * 0.01);
				govtCommAmt = book_price * govtCommRate * .01;

				vatAmt = vatAmt
						+ CommonMethods.calculateVat(book_price, commVariance,
								prizePayOutPer, govtCommRate, vat,
								govtCommRule, fixedAmt, ticketsInScheme);
				/*
				 * bookTaxableSale = (((book_price * (100 - (commVariance +
				 * prizePayOutPer + govtCommRate))) / 100) * 100) / (100 + vat);
				 */
				// bookTaxableSale=vatAmt/(vat * 0.01);
				bookTaxableSale = CommonMethods.calTaxableSale(book_price,
						commVariance, prizePayOutPer, govtCommRate, vat);

				bookSaleRetBean.setBookNumber(bookNbr);
				bookSaleRetBean.setBookCommVariance(commVariance);
				bookSaleRetBean.setDefaultCommVariance(agent_sale_comm_rate);
				bookSaleRetBean.setTotalSaleComm(commVariance);
				bookSaleRetBean.setTotalSaleGovtComm(govtCommRate);
				bookSaleRetBean.setGovtCommAmt(govtCommAmt);
				bookSaleRetBean.setBookVatAmt(vatAmt);
				bookSaleRetBean.setBookCommAmt(commAmt);
				bookSaleRetBean.setBookNetAmt(netAmt);
				bookSaleRetBean.setBookTaxableSale(bookTaxableSale);
				bookSaleReturnList.add(bookSaleRetBean);
				commVariance = 0.0;
				govtCommRate = 0.0;
				bookTaxableSale = 0.0;
				govtCommAmt = 0.0;
				vatAmt = 0.0;
				commAmt = 0.0;
				netAmt = 0.0;

			}

			// get comm variance history

			List<CommVarGovtCommBean> commVariancelist = new ArrayList<CommVarGovtCommBean>();
			CommVarGovtCommBean commVarGovtCommBean;

			String queryCommVarHistory = "select DISTINCT transacrion_sale_comm_rate,transaction_gov_comm_rate from st_se_game_inv_detail where current_owner_id="
					+ org_id + " and game_id=" + game_id;
			PreparedStatement stmtCommVarHistory = conn
					.prepareStatement(queryCommVarHistory);
			ResultSet rsCommVarHistory = stmtCommVarHistory.executeQuery();
			while (rsCommVarHistory.next()) {
				commVarGovtCommBean = new CommVarGovtCommBean();
				commVarGovtCommBean.setCommVariance(rsCommVarHistory
						.getDouble("transacrion_sale_comm_rate"));
				commVarGovtCommBean.setGovtComm(rsCommVarHistory
						.getDouble("transaction_gov_comm_rate"));
				commVariancelist.add(commVarGovtCommBean);
				// commVarianceSet.add(rsCommVarHistory.getDouble("transacrion_sale_comm_rate"));
			}
			System.out
					.println(" 22222222222222222222222size for comm var history "
							+ commVariancelist.size()
							+ "pstmt "
							+ stmtCommVarHistory);
			Iterator iteratorBookSaleReturn;
			// Iterator iteratorCommVarHistory= commVarianceSet.iterator();
			Iterator iteratorCommVarHistory = commVariancelist.iterator();
			while (iteratorCommVarHistory.hasNext()) {
				boolean bookCommVarMatch = false;
				// logger.info("comm var from history--------------------
				// ");
				Double totCommAmt = 0.0;
				Double totVatAmt = 0.0;
				Double totNetAmt = 0.0;
				Double totTaxableSale = 0.0;
				Double totGovtComm = 0.0;

				List bookListforSingleTrn = null;
				bookListforSingleTrn = new ArrayList<String>();
				double commFromHistory = 0.0;
				double govtCommFromHistory = 0.0;
				// commFromHistory=(Double)iteratorCommVarHistory.next();

				CommVarGovtCommBean commBean = (CommVarGovtCommBean) iteratorCommVarHistory
						.next();
				commFromHistory = commBean.getCommVariance();
				govtCommFromHistory = commBean.getGovtComm();

				// logger.info("comm var from history--------------------
				// "+commFromHistory);
				iteratorBookSaleReturn = bookSaleReturnList.iterator();
				while (iteratorBookSaleReturn.hasNext()) {
					bookSaleRetBean = (BookSaleReturnBean) iteratorBookSaleReturn
							.next();
					double bookCommVariance = 0.0;
					double bookGovtCommVariance = 0.0;
					bookCommVariance = bookSaleRetBean.getTotalSaleComm();
					bookGovtCommVariance = bookSaleRetBean
							.getTotalSaleGovtComm();
					// logger.info("commFromHistory " + commFromHistory +
					// "bookCommVariance " + bookCommVariance);
					// logger.info("GovtcommFromHistory " +
					// govtCommFromHistory + "bookGovtCommVariance " +
					// bookGovtCommVariance);
					if (commFromHistory == bookCommVariance
							&& govtCommFromHistory == bookGovtCommVariance) {
						// logger.info("inside
						boolean isSaleTransactionExist = true;
						// if%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
						if("YES".equals(Utility.getPropertyValue("IS_SCRATCH_NEW_FLOW_ENABLED"))){
							bookNumber = bookSaleRetBean.getBookNumber();
								String saleTransactionQuery = "select current_owner_id from st_se_game_inv_status where book_nbr = '"+bookSaleRetBean.getBookNumber()+"' and agent_invoice_id IS NOT NULL";	
								Statement saleTransactionQueryStmt = conn.createStatement();		
								ResultSet saleTransactionQueryResultSet = saleTransactionQueryStmt.executeQuery(saleTransactionQuery);		
								if (saleTransactionQueryResultSet.next()) {
									isSaleTransactionExist = true;
								} else{
									isSaleTransactionExist = false;
								}
						}
						if(isSaleTransactionExist){
							bookCommVarMatch = true;
							isBookActivated = false;
							totCommAmt = totCommAmt
									+ bookSaleRetBean.getBookCommAmt();
							totVatAmt = totVatAmt + bookSaleRetBean.getBookVatAmt();
							totTaxableSale = totTaxableSale
									+ bookSaleRetBean.getBookTaxableSale();
							// logger.info("hello :::::::: " + totTaxableSale
							// + "history detail "
							// +bookSaleRetBean.getBookTaxableSale());
							totGovtComm = totGovtComm
									+ bookSaleRetBean.getGovtCommAmt();
							totNetAmt = totNetAmt + bookSaleRetBean.getBookNetAmt();
							bookListforSingleTrn.add(bookSaleRetBean
									.getBookNumber());
						}
					}

				}
				netAmtOrg = netAmtOrg + totNetAmt;

				if (bookCommVarMatch) {

					// insert in LMS transaction master
					String QueryLMSTransMaster = QueryManager
							.insertInLMSTransactionMaster();
					PreparedStatement stmtLMSTransmas = conn
							.prepareStatement(QueryLMSTransMaster);
					stmtLMSTransmas.setString(1, "BO");
					stmtLMSTransmas.executeUpdate();

					// Get Transaction Id From the table.
					ResultSet rsTransId = stmtLMSTransmas.getGeneratedKeys();
					while (rsTransId.next()) {
						transaction_id = rsTransId.getInt(1);
						trnIdList.add(transaction_id);
					}

					logger.info("transaction_id:  " + transaction_id);

					// 1. Insert Entry in st_lms_bo_transaction_master Table.
					String queryTranMas = QueryManager
							.insertInBOTransactionMaster();
					PreparedStatement stmtTransmas = conn
							.prepareStatement(queryTranMas);

					stmtTransmas.setInt(1, transaction_id);
					stmtTransmas.setInt(2, userId);
					stmtTransmas.setInt(3, userOrgId);
					stmtTransmas.setString(4, "AGENT");
					stmtTransmas.setInt(5, org_id);
					stmtTransmas.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					stmtTransmas.setString(7, "SALE_RET");

					/*
					 * stmtTransmas.setString(1, "AGENT");
					 * stmtTransmas.setInt(2, org_id);
					 * stmtTransmas.setTimestamp(3, new java.sql.Timestamp(new
					 * java.util.Date().getTime())); stmtTransmas.setString(4,
					 * "SALE_RET");
					 */

					stmtTransmas.executeUpdate();

					// 2. Insert Entry in st_se_bo_agent_transaction table.
					String queryBoAgtTrans = QueryManager
							.getST4InsertBoAgentTransaction();
					PreparedStatement stmtBoAgtTrans = conn
							.prepareStatement(queryBoAgtTrans);
					stmtBoAgtTrans.setInt(1, transaction_id);
					stmtBoAgtTrans.setInt(2, bookListforSingleTrn.size());
					stmtBoAgtTrans.setInt(3, game_id);
					stmtBoAgtTrans.setInt(4, org_id);
					stmtBoAgtTrans.setDouble(5, book_price
							* bookListforSingleTrn.size());
					stmtBoAgtTrans.setDouble(6, totCommAmt);
					stmtBoAgtTrans.setDouble(7, totNetAmt);
					stmtBoAgtTrans.setString(8, "SALE_RET");
					stmtBoAgtTrans.setDouble(9, totVatAmt);
					stmtBoAgtTrans.setDouble(10, totTaxableSale);
					stmtBoAgtTrans.setDouble(11, totGovtComm);
					stmtBoAgtTrans.executeUpdate();

					String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
							+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
							+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement detHistoryInsPstmt = conn
							.prepareStatement(detHistoryInsQuery);

					// fetch game details from game master
					String fetchGameDetQuery = "select nbr_of_tickets_per_book from st_se_game_master where game_id ="
							+ game_id;
					Statement fetchGameDetStmt = conn.createStatement();
					ResultSet fetchGameDetRs = fetchGameDetStmt
							.executeQuery(fetchGameDetQuery);
					int noOfTktPerBooks = -1;
					if (fetchGameDetRs.next()) {
						noOfTktPerBooks = fetchGameDetRs
								.getInt("nbr_of_tickets_per_book");
					}

					// 6. Insert in to st_se_game_inv_detail table.
					for (int i = 0; i < bookListforSingleTrn.size(); i++) {
						String bknbr = (String) bookListforSingleTrn.get(i);
						// logger.info("//Get the pack number of book
						// number. ");
						// Get the pack number of book number.
						String pknbr = null;
						String queryTogetPackNbr = QueryManager
								.getST4PackNbrOfBookNbr();
						PreparedStatement stm = conn
								.prepareStatement(queryTogetPackNbr);
						stm.setString(1, bknbr);
						ResultSet resultSet = stm.executeQuery();
						while (resultSet.next()) {
							pknbr = resultSet.getString("pack_nbr");
						}

						String queryGameInvDtl = "insert into st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id,book_status) select ?,?,?,?,?,?,?,?,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id,book_status from st_se_game_inv_detail where book_nbr=? and transaction_at=? order by transaction_date desc limit 1";
						PreparedStatement stmtGameInvDtl = conn
								.prepareStatement(queryGameInvDtl);
						stmtGameInvDtl.setInt(1, transaction_id);
						stmtGameInvDtl.setInt(2, game_id);
						stmtGameInvDtl.setString(3, pknbr);
						stmtGameInvDtl.setString(4, bknbr);
						stmtGameInvDtl.setString(5, "BO");
						stmtGameInvDtl.setInt(6, userOrgId);
						stmtGameInvDtl.setTimestamp(7, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						stmtGameInvDtl.setString(8, "BO");
						stmtGameInvDtl.setString(9, bknbr);
						stmtGameInvDtl.setString(10, "BO");

						stmtGameInvDtl.executeUpdate();

						// insert into detail history table Added by arun
						detHistoryInsPstmt.setInt(1, game_id);
						detHistoryInsPstmt.setString(2, bknbr);
						detHistoryInsPstmt.setString(3, "BO");
						detHistoryInsPstmt.setInt(4, userOrgId);
						detHistoryInsPstmt.setTimestamp(5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						detHistoryInsPstmt.setInt(6, userOrgId);
						detHistoryInsPstmt.setInt(7, userId);
						detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
						// logger.info("detHistoryInsPstmt ==
						// "+detHistoryInsPstmt);
						if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
							detHistoryInsPstmt.setInt(9, noOfTktPerBooks);
						} else {
							detHistoryInsPstmt.setInt(9, 0);
						}
						detHistoryInsPstmt.setInt(10, 0);
						detHistoryInsPstmt.setString(11, newBookStatus);

						detHistoryInsPstmt.execute();
						// ---------------------

					}
			}
				if(isBookActivated){
					String bknbr = bookNumber;
					// System.out.println("//Get the pack number of book
					// number. ");
					// Get the pack number of book number.
					String pknbr = null;
					String queryTogetPackNbr = QueryManager
							.getST4PackNbrOfBookNbr();
					PreparedStatement stm = conn
							.prepareStatement(queryTogetPackNbr);
					stm.setString(1, bknbr);
					ResultSet resultSet = stm.executeQuery();
					while (resultSet.next()) {
						pknbr = resultSet.getString("pack_nbr");
					}
					
					String queryGameInvDtl = "insert into st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id,book_status) select ?,?,?,?,?,?,?,?,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id,book_status from st_se_game_inv_detail where book_nbr=? and transaction_at=? order by transaction_date desc limit 1";
					PreparedStatement stmtGameInvDtl = conn
							.prepareStatement(queryGameInvDtl);
					stmtGameInvDtl.setInt(1, transaction_id);
					stmtGameInvDtl.setInt(2, game_id);
					stmtGameInvDtl.setString(3, pknbr);
					stmtGameInvDtl.setString(4, bknbr);
					stmtGameInvDtl.setString(5, "BO");
					stmtGameInvDtl.setInt(6, userOrgId);
					stmtGameInvDtl.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					stmtGameInvDtl.setString(8, "BO");
					stmtGameInvDtl.setString(9, bknbr);
					stmtGameInvDtl.setString(10, "BO");

					stmtGameInvDtl.executeUpdate();
			}
			}

			logger.info("5. Update st_se_game_inv_status Table.	");
			// 5. Update st_se_game_inv_status Table.
			for (int i = 0; i < bookSaleReturnList.size(); i++) {
				String bknbr = ((BookSaleReturnBean) bookSaleReturnList.get(i))
						.getBookNumber();
				String queryGameInvStatus = QueryManager
						.getST4UdateGameInvStatusForBook();
				PreparedStatement stmtGameInvStatus = conn
						.prepareStatement(queryGameInvStatus);
				stmtGameInvStatus.setString(1, newBookStatus);
				stmtGameInvStatus.setInt(2, userOrgId);
				stmtGameInvStatus.setString(3, null);
				stmtGameInvStatus.setInt(4, game_id);
				stmtGameInvStatus.setString(5, bknbr);
				stmtGameInvStatus.executeUpdate();

			}

			// get auto generated treciept number
			Statement autoGenRecptPstmtBOCRNote = null;
			// String getLatestRecieptNumberBO="SELECT * from
			// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY
			// generated_id DESC LIMIT 1 ";
			String queryRcpt = "SELECT * from st_lms_bo_receipts where receipt_type like ('CR_NOTE%')  ORDER BY generated_id DESC LIMIT 1";
			autoGenRecptPstmtBOCRNote = conn.createStatement();
			// autoGenRecptPstmtBO.setString(1, "CR_NOTE");
			logger.info("queryRcpt--" + queryRcpt);
			ResultSet recieptRsBO = autoGenRecptPstmtBOCRNote
					.executeQuery(queryRcpt);
			String lastRecieptNoGeneratedBO = null;

			while (recieptRsBO.next()) {
				lastRecieptNoGeneratedBO = recieptRsBO
						.getString("generated_id");
			}

			String autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
					"CR_NOTE", lastRecieptNoGeneratedBO, "BO");

			// get auto generated delivery Challan number
			// String getLatestDSRCNumber="SELECT * from
			// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY
			// generated_id DESC LIMIT 1 ";
			// autoGenRecptPstmtBO=conn.prepareStatement(getLatestDSRCNumber);
			PreparedStatement autoGenRecptPstmtBO = null;
			autoGenRecptPstmtBO = conn.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			autoGenRecptPstmtBO.setString(1, "DSRCHALLAN");
			ResultSet DCRs = autoGenRecptPstmtBO.executeQuery();
			String lastDSRCNoGenerated = null;

			while (DCRs.next()) {
				lastDSRCNoGenerated = DCRs.getString("generated_id");
			}

			String autoGeneDCNo = null;
			autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DSRCHALLAN",
					lastDSRCNoGenerated, "BO");

			// insert into receipts master

			PreparedStatement stmtRecptId = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			stmtRecptId.setString(1, "BO");
			stmtRecptId.executeUpdate();

			ResultSet rsRecptId = stmtRecptId.getGeneratedKeys();
			while (rsRecptId.next()) {
				receipt_id = rsRecptId.getInt(1);
			}

			// Insert Entry in st_bo_receipt table.
			// String queryRecptId=QueryManager.getST4InsertBoReceipts();
			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInBOReceipts());

			stmtRecptId.setInt(1, receipt_id);
			stmtRecptId.setString(2, "CR_NOTE");
			stmtRecptId.setInt(3, org_id);
			stmtRecptId.setString(4, "AGENT");
			stmtRecptId.setString(5, autoGeneRecieptNoBO);
			stmtRecptId.setTimestamp(6, Util.getCurrentTimeStamp());

			/*
			 * stmtRecptId.setString(1, "CR_NOTE"); stmtRecptId.setInt(2,
			 * org_id);
			 */

			stmtRecptId.executeUpdate();

			// insert reciept id for delivery challan
			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			stmtRecptId.setString(1, "BO");
			stmtRecptId.executeUpdate();

			ResultSet rsDC = stmtRecptId.getGeneratedKeys();
			while (rsDC.next()) {
				DCId = rsDC.getInt(1);
			}
			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInBOReceipts());
			stmtRecptId.setInt(1, DCId);
			stmtRecptId.setString(2, "DSRCHALLAN");
			stmtRecptId.setInt(3, org_id);
			stmtRecptId.setString(4, "AGENT");
			stmtRecptId.setString(5, autoGeneDCNo);
			stmtRecptId.setTimestamp(6, Util.getCurrentTimeStamp());
			stmtRecptId.execute();

			// 4. Insert Entry in st_lms_bo_receipts_trn_mapping table.
			PreparedStatement stmtRcptTrnMapping = conn
					.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			for (int i = 0; i < trnIdList.size(); i++) {
				stmtRcptTrnMapping.setInt(1, receipt_id);
				stmtRcptTrnMapping.setInt(2, (Integer) trnIdList.get(i));
				stmtRcptTrnMapping.executeUpdate();
			}

			// 4. Insert Entry in st_lms_bo_receipts_trn_mapping table for
			// delivery
			// challan
			for (int i = 0; i < trnIdList.size(); i++) {
				stmtRcptTrnMapping.setInt(1, DCId);
				stmtRcptTrnMapping.setInt(2, (Integer) trnIdList.get(i));
				stmtRcptTrnMapping.executeUpdate();
			}

			// insert into invoice and delivery challan mapping table
			String insertCRNoteDCMapping = "insert into st_se_bo_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
			PreparedStatement boCRNoteDCMappingStmt = conn
					.prepareStatement(insertCRNoteDCMapping);
			boCRNoteDCMappingStmt.setInt(1, receipt_id);
			boCRNoteDCMappingStmt.setString(2, autoGeneRecieptNoBO);
			boCRNoteDCMappingStmt.setString(3, autoGeneDCNo);
			boCRNoteDCMappingStmt.executeUpdate();

			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(netAmtOrg, "TRANSACTION", "SALE_RET", org_id,
					0, "AGENT", 0, conn);
			
			if(!isValid)
				throw new LMSException();
			
			/*boolean isUpdateDone = OrgCreditUpdation.updateCreditLimitForAgent(
					org_id, "SALE_RET", netAmtOrg, conn);*/

//			session.setAttribute("DEL_CHALLAN_ID", DCId);
//			if (receipt_id > 0) {
//				GraphReportHelper graphReportHelper = new GraphReportHelper();
//				graphReportHelper.createTextReportBO(receipt_id, orgName,
//						userOrgId, rootPath);
//			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException(e);
		}
		return String.valueOf(DCId) + "Nxt" + String.valueOf(receipt_id);
	}

	/**
	 * This method is used to make entry into database for sale return for valid
	 * packs and books
	 * 
	 * @param game_id
	 * @param org_id
	 * @param orgName
	 * @param packlist
	 *            is Listy of valid packs
	 * @param booklist
	 *            is list of valid books
	 * @param rootPath
	 * @return void
	 * @throws LMSException
	 */

	public int doTransaction(int game_id, int org_id, String orgName,
			List<PackBean> packlist, List<BookBean> booklist, String rootPath,
			int userOrgId, HttpSession session, int userId, String newBookStatus)
			throws LMSException {
			Connection conn = null;
			int receipt_id = 0;
		
		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			
			String returnValue = doTransaction(game_id, org_id, orgName, packlist, booklist, rootPath, userOrgId, userId, newBookStatus, conn);
			
			conn.commit();
			session.setAttribute("DEL_CHALLAN_ID", Integer.parseInt(returnValue.split("Nxt")[0]));
			receipt_id = Integer.parseInt(returnValue.split("Nxt")[1]);
			if (receipt_id > 0) {
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportBO(receipt_id, orgName,
						userOrgId, rootPath);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return receipt_id;
	}

	/**
	 * This method is used to verify the books for sale return
	 * 
	 * @param bookList
	 * @param game_id
	 * @param org_id
	 * @return List of verified books
	 */
	// Do Bulk Verification Of Books.
	public List<BookBean> doVerifiedBooks(List<BookBean> bookList, int game_id,
			int org_id, String isRetOnline, int gameNbr, String newBookStatus)
			throws LMSException {
		Connection conn = DBConnect.getConnection();
		System.out
				.println("connection created here =============================");
		try {
			System.out
					.println("Enter in to the bulk Book verification@@@@@@@@@@@@@@@@@@@@@@");
			List<BookBean> list = new ArrayList<BookBean>();
			Iterator<BookBean> iterator = bookList.iterator();
			BookBean bean = null;
			String bknbr = null;
			// boolean bookExistancecheck = false;
			// boolean ownercheck = false;
			// boolean pwtCheck = false;
			// boolean pwtCheckTemp = false;
			boolean bookValidity = false;

			while (iterator.hasNext()) {
				bean = (BookBean) iterator.next();
				bknbr = bean.getBookNumber();

				if (bknbr != null) {
					bookValidity = verifyBook(game_id, bknbr, org_id, conn,
							newBookStatus);
					if (bookValidity) {
						logger.info("book is valid " + bknbr);
						bean.setValid(true);
						bean.setStatus("Book Is Valid");
						list.add(bean);
						setPackFlag("Valid");
					} else {
						logger.info("book is Invalid " + bknbr);
						bean.setValid(false);
						bean.setStatus("Book Is InValid");
						list.add(bean);
					}
				}
				/*
				 * if(bknbr!=null){
				 * 
				 * 
				 * logger.info("Book is not null"+bknbr);
				 * bookExistancecheck = verifyBookWithExistance(game_id, bknbr,
				 * org_id, conn); ownercheck = verifyBookWithOwner(game_id,
				 * bknbr, org_id,isRetOnline, conn); pwtCheck =
				 * verifyBookValidityWithPWT(game_id, bknbr, conn,gameNbr);
				 * pwtCheckTemp=verifyBookValidityWithPWTTempTable(game_id,
				 * bknbr, conn);
				 * 
				 * logger.info("bookExistancecheck:
				 * "+bookExistancecheck); logger.info("ownercheck:
				 * "+ownercheck); logger.info("pwtCheck: "+pwtCheck); }
				 * if(ownercheck == true && pwtCheck == true &&
				 * bookExistancecheck == true && pwtCheckTemp==true){
				 * logger.info("book is valid "+bknbr);
				 * bean.setValid(true); bean.setStatus("Book Is Valid");
				 * list.add(bean); setPackFlag("Valid"); } else{
				 * logger.info("book is Invalid "+bknbr);
				 * bean.setValid(false); bean.setStatus("Book Is InValid");
				 * list.add(bean); }
				 */
			}
			logger.info("verified booklist:  " + list);
			System.out
					.println("@@@@@@@@@@@@@@@@@@@@@@@@@@ bulk book verification is complete");

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale return");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method inside helper class is used to verify the packs for sale
	 * return
	 * 
	 * @param packList
	 *            is the list of packs
	 * @param game_id
	 *            is games Id
	 * @param org_id
	 *            Is Organization Id
	 * @return List of verified packs
	 */
	// Do Bulk Verification Of Packs.
	public List<PackBean> doVerifiedPacks(List<PackBean> packList, int game_id,
			int org_id, String isRetOnline, int gameNbr) throws LMSException {
		System.out
				.println("Enter in to the bulk pack verification============================");
		List<PackBean> list = new ArrayList<PackBean>();
		Iterator<PackBean> iterator = packList.iterator();
		PackBean bean = null;
		String pknbr = null;
		// boolean packExistancecheck = false;
		// boolean ownercheck = false;
		// boolean pwtCheck = false;
		// boolean pwtCheckTemp = false;
		boolean isValidPack = false;
		Connection conn = null;
		try {
			 
			conn = DBConnect.getConnection();

			while (iterator.hasNext()) {
				bean = (PackBean) iterator.next();
				pknbr = bean.getPackNumber();

				if (pknbr != null) {
					isValidPack = verifyPack(game_id, pknbr, org_id, conn);
					if (isValidPack) {
						logger.info("pack is valid " + pknbr);
						bean.setValid(true);
						bean.setStatus("Pack Is Valid");
						setPackFlag("Valid");
						list.add(bean);
					} else {
						logger.info("pack is Invalid " + pknbr);
						bean.setValid(false);
						bean.setStatus("Pack Is InValid");
						list.add(bean);
					}
				}

				/*
				 * if(pknbr!=null){
				 * 
				 * logger.info("Pack is not null"+pknbr);
				 * 
				 * packExistancecheck = verifyPackWithExistance(game_id, pknbr,
				 * org_id); ownercheck = verifyPackWithOwner(game_id, pknbr,
				 * org_id,isRetOnline); pwtCheck =
				 * verifyPackValidityWithPWT(game_id, pknbr,gameNbr);
				 * pwtCheckTemp=verifyPackValidityWithPWTTempTable(game_id,
				 * pknbr);
				 * 
				 * logger.info("packExistancecheck:
				 * "+packExistancecheck); logger.info("ownercheck:
				 * "+ownercheck); logger.info("pwtCheck: "+pwtCheck); }
				 * 
				 * if(ownercheck == true && pwtCheck == true &&
				 * packExistancecheck == true && pwtCheckTemp==true){
				 * logger.info("pack is valid "+pknbr);
				 * bean.setValid(true); bean.setStatus("Pack Is Valid");
				 * setPackFlag("Valid"); list.add(bean); } else{
				 * logger.info("pack is Invalid "+pknbr);
				 * 
				 * bean.setValid(false); bean.setStatus("Pack Is InValid");
				 * 
				 * list.add(bean); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
		}
		logger.info("verified pack list:  " + list);
		System.out
				.println("============================ bulk pack verification is complete");
		return list;
	}

	public String getBookFlag() {
		return bookFlag;

	}

	public String getBookNbrFromTicketNo(String ticket_nbr) {
		String book_nbr = "";
		StringTokenizer st = new StringTokenizer(ticket_nbr, "-");
		for (int i = 0; i < 2; i++) {
			if (st.hasMoreTokens()) {
				book_nbr = book_nbr + st.nextToken();
				if (i == 0) {
					book_nbr = book_nbr + "-";
				}
			}
		}
		return book_nbr;
	}

	public int getGameId(List<ActiveGameBean> activeGameList,
			String gameNbr_Name) {
		ActiveGameBean bean = null;
		if (activeGameList != null) {
			for (int i = 0; i < activeGameList.size(); i++) {
				bean = activeGameList.get(i);
				if (gameNbr_Name.equals(bean.getGameNbr_Name())) {
					return bean.getGameId();
				}
			}
		}
		return 0;
	}

	/**
	 * this method inside helper class is used to get the game ID from the game
	 * name
	 * 
	 * @param game_Name
	 * @return int (game Id)
	 */
	// Get Game Id From Data Base Using the Game Name.
	public int getGameIdFromDataBase(String game_Name) {
		Connection connection = null;
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String query = QueryManager.getST4GameDetailsUsingGameName();
		int game_id = 0;
		try {
			 
			connection = DBConnect.getConnection();
			Pstmt = connection.prepareStatement(query);
			Pstmt.setString(1, game_Name);
			resultSet = Pstmt.executeQuery();
			while (resultSet.next()) {
				game_id = resultSet.getInt("game_id");
				return game_id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (Pstmt != null) {
					Pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public ArrayList getGameList(int orgId) {
		 
		ArrayList<GameBean> list = new ArrayList<GameBean>();
		GameBean gameBean = null;
		Connection con;
		con = DBConnect.getConnection();

		//Statement stmt1 = null;
		Statement stmt2 = null;

		//ResultSet rs1 = null;
		ResultSet rs2 = null;

		try {
			//stmt1 = con.createStatement();
			stmt2 = con.createStatement();

			logger.info("Url>>>>>>>>>>>>>>>..." + orgId);

			/*String owid = "select organization_id from st_lms_organization_master where name = '"
					+ orgName + "'";
			rs1 = stmt1.executeQuery(owid);
			int orgId = 0;
			while (rs1.next()) {
				orgId = rs1.getInt("organization_id");
			}*/
			// java.sql.Date currentDate= new java.sql.Date(new
			// Date().getTime());

			String gmdetail = "select distinct p.game_id, p.game_name,p.game_nbr,p.sale_end_date  from st_se_game_master p, st_se_game_inv_status e where e.game_id = p.game_id and e.current_owner_id='"
					+ orgId
					+ "' and p.game_status in('OPEN','SALE_HOLD','SALE_CLOSE')";
			rs2 = stmt2.executeQuery(gmdetail);
			while (rs2.next()) {
				gameBean = new GameBean();
				gameBean.setGameId(rs2.getInt("game_id"));
				gameBean.setGameNbr(rs2.getInt("game_nbr"));
				gameBean.setGameName(rs2.getString("game_name"));
				gameBean.setSaleEndDate(rs2.getDate("sale_end_date"));
				list.add(gameBean);
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
			}
		}

		return list;

	} // End getCharacters()

	public int getOrganizationId(List<OrgInfoBean> organizationBeanList,
			String organization_Name) {
		OrgInfoBean bean = null;
		if (organizationBeanList != null) {
			for (int i = 0; i < organizationBeanList.size(); i++) {
				bean = organizationBeanList.get(i);
				if (organization_Name.equals(bean.getOrganization_Name())) {
					return bean.getOrganizationId();
				}
			}
		}
		return 0;
	}

	/**
	 * This method inside helper class is used to get the organization Id by
	 * Organization Name
	 * 
	 * @param organization_Name
	 * @return int (organizatiuon Id)
	 */
	// Get Organization Id From Data Base Using Organization Name.
	public int getOrganizationIdFromDataBase(String organization_Name) {
		Connection connection = null;
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String query = QueryManager.getST4OrganizationIdUsingOrganizationName();
		int organization_id = 0;
		try {
			 
			connection = DBConnect.getConnection();
			Pstmt = connection.prepareStatement(query);
			Pstmt.setString(1, organization_Name);
			resultSet = Pstmt.executeQuery();
			while (resultSet.next()) {
				organization_id = resultSet
						.getInt(TableConstants.ORGANIZATION_ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (Pstmt != null) {
					Pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return organization_id;
	}

	// For Get The OrganizationBean of Agents in a list.
	/**
	 * This method is used to get the Agents Organizations Name
	 * 
	 * @return List of Organizations Information
	 */
	public List<OrgInfoBean> getOrganizations() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			OrgInfoBean orgBean = null;
			List<OrgInfoBean> orgList = new ArrayList<OrgInfoBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query = QueryManager.getST4AgentOrganizationSearchQuery()
					+ " order by name";
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				orgBean = new OrgInfoBean();
				orgBean.setOrganizationId(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				orgBean.setOrganiztion_Type(resultSet
						.getString(TableConstants.ORGANIZATION_TYPE));
				orgBean.setOrganization_Name(resultSet
						.getString(TableConstants.ORGANIZATION_NAME));
				orgList.add(orgBean);
			}
			return orgList;
		} catch (SQLException e) {
			e.printStackTrace();
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
		return null;
	}

	public String getPackFlag() {
		return packFlag;

	}

	public String getTicketNbrFromTicketNo(String ticket_nbr) {
		String tkt_nbr = "";
		StringTokenizer st = new StringTokenizer(ticket_nbr, "-");
		for (int i = 0; i < 3; i++) {
			if (st.hasMoreTokens()) {
				tkt_nbr = st.nextToken();
			}
		}
		return tkt_nbr;
	}

	public List<BookBean> getVerifiedBooks(List<BookBean> bookList,
			int game_id, int org_id) {
		return bookList;
	}

	public List<PackBean> getVerifiedPacks(List<PackBean> packList,
			int game_id, int org_id) {
		return packList;
	}

	public List<TicketBean> getVerifiedTickets(List<TicketBean> ticketBean,
			int game_id) {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			 
			connection = DBConnect.getConnection();
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			String query2 = QueryManager.getST4PwtTicketDetailsUsingTicketNbr();
			String query3 = QueryManager.getST4GameDetailsUsingGameId();

			Iterator<TicketBean> iterator = ticketBean.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				int size = 0;
				ticket_nbr = iterator.next().getTicketNumber();
				size = ticket_nbr.length();

				if (size != 0) {

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					String book_nbr = getBookNbrFromTicketNo(ticket_nbr);
					int actual_tkt_nbr = Integer
							.parseInt(getTicketNbrFromTicketNo(ticket_nbr));
					logger.info("Book No is: " + book_nbr);

					preparedStatement1 = connection.prepareStatement(query1);
					preparedStatement1.setInt(1, game_id);
					preparedStatement1.setString(2, book_nbr);
					resultSet1 = preparedStatement1.executeQuery();

					if (resultSet1.next()) {

						if (resultSet1.getString("current_owner").equals(
								"RETAILER")) {

							preparedStatement2 = connection
									.prepareStatement(query2);
							preparedStatement2.setString(1, ticket_nbr);
							resultSet2 = preparedStatement2.executeQuery();

							if (resultSet2.next()) {
								bean.setValid(false);
								bean.setStatus("Ticket's PWT Has Been Paid.");
								verifyResults.add(bean);
								System.out
										.println("Ticket IS exist in pwt table.");
							} else {
								preparedStatement3 = connection
										.prepareStatement(query3);
								preparedStatement3.setInt(1, game_id);
								resultSet3 = preparedStatement3.executeQuery();
								if (resultSet3.next()) {
									if (resultSet3
											.getInt("nbr_of_tickets_per_book") <= actual_tkt_nbr) {
										bean.setValid(false);
										bean.setStatus("Ticket Is Fake.");
										verifyResults.add(bean);
										System.out
												.println("Ticket number is fake.");
									} else {
										bean.setValid(true);
										bean
												.setStatus("Ticket Is Valid For PWT.");
										verifyResults.add(bean);
										System.out
												.println("Ticket IS valid for pwt. (Means not fake and not in pwt table)");
									}
								}
							}
						} else {
							bean.setValid(false);
							bean
									.setStatus("Ticket's Owner Is Not Valid. Or Ticket Is In Our Stock In Our Chain");
							verifyResults.add(bean);
							logger.info("Ticket owner is not Retailer.");
						}
					} else {
						bean.setValid(false);
						bean.setStatus("Ticket Is Not Of The Our Company.");
						verifyResults.add(bean);
						logger.info("Ticket Is not of the company.");
					}
				}
			}
			logger.info("The verified List Is: " + verifyResults);
			return verifyResults;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement1 != null) {
					preparedStatement1.close();
				}
				if (preparedStatement2 != null) {
					preparedStatement2.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public List<BookBean> saveBookData(int game_id, int org_id,
			List<BookBean> bookList) {
		return bookList;
	}

	public List<PackBean> savePackData(int game_id, int org_id,
			List<PackBean> packList) {
		return packList;
	}

	public List<TicketBean> saveTicketsData(int game_id,
			List<TicketBean> verifiedTicketList) {
		List<TicketBean> savedResults = new ArrayList<TicketBean>();
		Connection connection = null;
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String query = QueryManager.updateIntoPwtTicketsInv();
		try {
			 
			connection = DBConnect.getConnection();
			Pstmt = connection.prepareStatement(query);
			Iterator<TicketBean> iterator = verifiedTicketList.iterator();
			while (iterator.hasNext()) {
				String ticket_nbr = null;
				boolean isValid = false;
				int size = 0;
				TicketBean ticketBean = (TicketBean) iterator.next();
				isValid = ticketBean.getIsValid();
				if (isValid == true) {
					ticket_nbr = ticketBean.getTicketNumber();
					String book_nbr = getBookNbrFromTicketNo(ticket_nbr);
					Pstmt.setString(1, ticket_nbr);
					Pstmt.setInt(2, game_id);
					Pstmt.setString(3, book_nbr);
					Pstmt.executeUpdate();

					TicketBean bean = new TicketBean();
					bean.setTicketNumber(ticket_nbr);
					bean.setValid(isValid);
					savedResults.add(bean);
				}
			}
			return savedResults;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (Pstmt != null) {
					Pstmt.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * This method is used to get the valid books from verified books
	 * 
	 * @param bookBeanList
	 * @return List(BookBean type) of valid books
	 */
	// Get Valid Books from the verified book list.
	public List<BookBean> selectValidBooks(List<BookBean> bookBeanList) {
		List<BookBean> validBookBeanList = new ArrayList<BookBean>();
		BookBean bean = null;
		Iterator<BookBean> itr = bookBeanList.iterator();
		while (itr.hasNext()) {
			bean = itr.next();
			if (bean.getIsValid() == true) {
				validBookBeanList.add(bean);
			}
		}
		return validBookBeanList;
	}

	/**
	 * This method is used to get the valid packs from verified packs
	 * 
	 * @param packBeanList
	 * @return List of PackBean type of valid packs
	 */
	// Get Valid Packs from the verified pack list.
	public List<PackBean> selectValidPacks(List<PackBean> packBeanList) {
		List<PackBean> validPackBeanList = new ArrayList<PackBean>();
		PackBean bean = null;
		Iterator<PackBean> itr = packBeanList.iterator();
		while (itr.hasNext()) {
			bean = itr.next();
			if (bean.getIsValid() == true) {
				validPackBeanList.add(bean);
			}
		}
		return validPackBeanList;
	}

	void setBookFlag(String bookflag) {
		bookFlag = bookflag;

	}

	void setPackFlag(String packflag) {
		packFlag = packflag;

	}

	/**
	 * This method is used to check book validy only by current owner id and
	 * status
	 */

	public boolean verifyBook(int game_id, String bknbr, int org_id,
			Connection conn, String newBookStatus) throws LMSException {
		System.out
				.println("Enter In To verify book with owner ******************************");

		boolean bookflag = false;
		try {
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn.prepareStatement(query1);
			pstmt.setInt(1, game_id);
			pstmt.setString(2, bknbr);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (org_id == rs.getInt("current_owner_id")
						&& rs.getString("book_status").equals(newBookStatus)
						&& "AGENT".equalsIgnoreCase(rs
								.getString("current_owner"))) {
					bookflag = true;
				}
			}
						

			System.out
					.println("************************* book is verified and flag is "
							+ bookflag + " for book " + bknbr);
			return bookflag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Exception while book verification");
		}

	}

	/**
	 * This method is used to check book for paid pwts
	 * 
	 * @param game_id
	 * @param bknbr
	 *            is poch number
	 * @return boolean
	 */
	// Check Book Verification For PWT Tickets.
	public boolean verifyBookValidityWithPWT(int game_id, String bknbr,
			Connection con, int gameNbr) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		 
		Connection conn = null;
		// conn= DBConnect.getConnection();
		conn = con;
		String query6 = QueryManager.getST4BookOfTicketsOfPwt();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, gameNbr);
			stmt6.setInt(2, game_id);
			stmt6.setString(3, bknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				bookPwtFlag = false;
			}

			if (bookPwtFlag == false) {
				logger.info("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				logger.info("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT is complete");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			/*
			 * try{ if(conn!=null) conn.close(); }catch(SQLException se){
			 * se.printStackTrace(); }
			 */
		}
		return bookPwtFlag;
	}

	/**
	 * This method is used to check book for paid pwts
	 * 
	 * @param game_id
	 * @param bknbr
	 *            is poch number
	 * @return boolean
	 */
	// Check Book Verification For PWT Tickets.
	public boolean verifyBookValidityWithPWTTempTable(int game_id,
			String bknbr, Connection con) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		 
		Connection conn = null;
		// conn= DBConnect.getConnection();
		conn = con;
		String query6 = QueryManager.getST4BookOfTicketsOfPwtTmpTable();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, game_id);
			stmt6.setString(2, bknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				bookPwtFlag = false;
			}

			if (bookPwtFlag == false) {
				logger.info("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				logger.info("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT is complete in temporary table");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// try{
			// if(conn!=null)
			// conn.close();
			// }catch(SQLException se){
			// se.printStackTrace();
			// }
		}
		return bookPwtFlag;
	}

	/**
	 * This method is used to check whether book number exists for organization
	 * 
	 * @param game_id
	 * @param bknbr
	 * @param org_id
	 * @return boolean
	 */
	// Check Book Verification for Existence.
	public boolean verifyBookWithExistance(int game_id, String bknbr,
			int org_id, Connection con) {
		System.out
				.println("Enter In To verify Book with Existence -----------------------------");
		boolean bookExistenceflag = true;
		 
		Connection conn = null;
		// conn= DBConnect.getConnection();
		conn = con;
		PreparedStatement stmt1 = null;
		String query1 = QueryManager.getST4BookValidityQuery();
		try {
			stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, bknbr);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				if (!(rs1.getInt("total") > 0)) {
					bookExistenceflag = false;
					// logger.info("Book No. :"+bknbr +" is not Exist in
					// Organization. Totla count is: "+rs1.getInt("total"));
				}
			}
			if (bookExistenceflag == false) {
				logger.info("bookExistenceflag is not Exist: "
						+ bookExistenceflag);
			} else {
				logger.info("bookExistenceflag is Exist: "
						+ bookExistenceflag);
			}
			System.out
					.println("----------------------------- verify book with Existence is complete");
			return bookExistenceflag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
					// if(conn!=null)
					// conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bookExistenceflag;
	}

	/**
	 * This method is used to verify book owner
	 * 
	 * @param game_id
	 * @param bknbr
	 * @param org_id
	 * @return boolean
	 */

	// Check Book Verification for Owner.
	public boolean verifyBookWithOwner(int game_id, String bknbr, int org_id,
			String isRetOnline, Connection con) {

		System.out
				.println("Enter In To verify book with owner ******************************");

		boolean bookflag = false;
		 
		Connection conn = null;
		// conn= DBConnect.getConnection();
		conn = con;
		try {
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, bknbr);
			ResultSet rs1 = stmt1.executeQuery();

			if (isRetOnline.equals("NO")) {
				while (rs1.next()) {
					if (org_id == rs1.getInt("current_owner_id")) {
						bookflag = true;
						// logger.info("bookflag: "+bookflag);
					}
				}
			}

			else {
				while (rs1.next()) {
					if (!(rs1.getString("book_status") == null)
							&& org_id == rs1.getInt("current_owner_id")
							&& rs1.getString("book_status").equals("INACTIVE")) {
						bookflag = true;
						// logger.info("bookflag: "+bookflag + "book
						// status " + rs1.getString("book_status"));
					}
				}

			}

			if (bookflag == false) {
				logger.info("bookflag is not valid: " + bookflag);
			} else {
				logger.info("bookflag is valid: " + bookflag);
			}

			System.out
					.println("************************* verify pack with owner is complete");
			return bookflag;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// try{
			// if(conn!=null)
			// conn.close();
			// }catch(SQLException se){
			// se.printStackTrace();
			// }
		}

		return bookflag;
	}

	/**
	 * This method is used to check pack validy only by current owner id and
	 * status
	 */

	public boolean verifyPack(int game_id, String pknbr, int org_id,
			Connection conn) throws LMSException {
		System.out
				.println("Enter In To verify pack ******************************");

		boolean bookflag = false;
		try {
			// String query1=
			// QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn
					.prepareStatement("select book_status,current_owner_id,current_owner from st_se_game_inv_status where game_id=? and pack_nbr = ?");
			pstmt.setInt(1, game_id);
			pstmt.setString(2, pknbr);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (org_id != rs.getInt("current_owner_id")
						&& !"ACTIVE".equalsIgnoreCase(rs
								.getString("book_status"))
						&& !"AGENT".equalsIgnoreCase(rs
								.getString("current_owner"))) {
					return bookflag;
				} else {
					bookflag = true;
				}
			}
			System.out
					.println("************************* pack is verified and flag is "
							+ bookflag + " for pack " + pknbr);
			return bookflag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(
					"Exception while pack verification in sale return @ BO");
		}
	}

	/**
	 * This method inside helper class is used to verify pack eith pwt(either
	 * pwt is paid or not)
	 * 
	 * @param game_id
	 * @param pknbr
	 * @return boolean
	 */
	// Check Pack Verification For PWT tickets.
	public boolean verifyPackValidityWithPWT(int game_id, String pknbr,
			int gameNbr) throws LMSException {

		System.out
				.println("Enter In To verify pack with PWT +++++++++++++++++++++++++++++");

		boolean packPwtFlag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		String query6 = QueryManager
				.getST4PWTDetailsForBookNbrUsingGamePackNbr();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, gameNbr);
			stmt6.setInt(2, game_id);
			stmt6.setInt(3, game_id);
			stmt6.setString(4, pknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				packPwtFlag = false;
			}
			if (packPwtFlag == false) {
				logger.info("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				logger.info("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete");
			return packPwtFlag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql exception ", e);

		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("error during closing connection ", se);

			}
		}

		// return packPwtFlag;
	}

	// Check Pack Verification For PWT tickets.
	public boolean verifyPackValidityWithPWTTempTable(int game_id, String pknbr)
			throws LMSException {

		System.out
				.println("Enter In To verify pack with PWT +++++++++++++++++++++++++++++");

		boolean packPwtFlag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		String query6 = QueryManager
				.getST4PWTDetailsForBookNbrUsingGamePackNbrTempTable();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, game_id);
			stmt6.setInt(2, game_id);
			stmt6.setString(3, pknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				packPwtFlag = false;
			}
			if (packPwtFlag == false) {
				logger.info("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				logger.info("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete in temporary table");
			return packPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return packPwtFlag;
	}

	/**
	 * This method is used to check whether pack is valid for the game
	 * 
	 * @param game_id
	 * @param pknbr
	 * @param org_id
	 * @return boolean
	 */
	// Check Pack Verification for Existence.
	public boolean verifyPackWithExistance(int game_id, String pknbr, int org_id) {
		System.out
				.println("Enter In To verify pack with Existence -----------------------------");
		boolean packExistenceflag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement stmt1 = null;
		String query1 = QueryManager.getST4PackValidityQuery();
		try {
			stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, pknbr);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				if (!(rs1.getInt("total") > 0)) {
					packExistenceflag = false;
					// logger.info("Pack No. :"+pknbr +" is not Exist in
					// Organization. Totla count is: "+rs1.getInt("total"));
				}
			}
			if (packExistenceflag == false) {
				logger.info("packflag is not Exist: "
						+ packExistenceflag);
			} else {
				logger.info("packflag is Exist: " + packExistenceflag);

			}
			System.out
					.println("----------------------------- verify pack with Existence is complete");
			return packExistenceflag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return packExistenceflag;
	}

	/**
	 * This method is used to check the current owner of the pack
	 * 
	 * @param game_id
	 * @param pknbr
	 * @param org_id
	 * @return boolean
	 */
	// Check Pack Verification for Owner.
	public boolean verifyPackWithOwner(int game_id, String pknbr, int org_id,
			String isRetOnline) {
		System.out
				.println("Enter In To verify pack with owner -----------------------------");
		boolean packflag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement stmt1 = null;
		String query1 = QueryManager
				.getST4CurrentOwnerDetailsAndBookNbrUsingGamePackNbr();
		try {
			stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, pknbr);
			ResultSet rs1 = stmt1.executeQuery();

			if (isRetOnline.equals("NO")) {
				while (rs1.next()) {
					if (!(org_id == rs1.getInt("current_owner_id"))) {
						packflag = false;
						// logger.info("Pack No. :"+pknbr +" of Book no
						// :"+rs1.getString("book_nbr")+ " is of the
						// Organization: "+rs1.getInt("current_owner_id")+" not
						// of the salected Organization: "+org_id);
					}
				}
			} else {
				while (rs1.next()) {
					if (rs1.getString("book_status") == null
							|| !(org_id == rs1.getInt("current_owner_id"))
							|| !rs1.getString("book_status").equals("INACTIVE")) {
						packflag = false;
						// logger.info("Pack No. :"+pknbr +" of Book no
						// :"+rs1.getString("book_nbr")+ " is of the
						// Organization: "+rs1.getInt("current_owner_id")+" not
						// of the salected Organization: "+org_id);
						// logger.info("book status for this pack is " +
						// rs1.getString("book_status"));
					}
				}
			}

			if (packflag == false) {
				logger.info("packflag is not valid: " + packflag);
			} else {
				logger.info("packflag is valis: " + packflag);
			}
			System.out
					.println("----------------------------- verify pack with owner is complete");
			return packflag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return packflag;
	}
	
	public boolean showCreditNote(int receipt_id) {
		boolean showCreditNote = false; 
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query =  "select sbtm.transaction_id,transaction_type,sbr.receipt_type,sbr.generated_id, sbr.voucher_date from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_transaction_master sbtm,st_lms_bo_receipts sbr where sbrtm.receipt_id=? and  sbrtm.transaction_id=sbtm.transaction_id and sbr.receipt_id=sbrtm.receipt_id";
		try {
			conn = DBConnect.getConnection();
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, receipt_id);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement);

			if(rs.next()) {
				showCreditNote = true;
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			DBConnect.closeResource(conn,preparedStatement,rs);
		}

		return showCreditNote;
	}
}
