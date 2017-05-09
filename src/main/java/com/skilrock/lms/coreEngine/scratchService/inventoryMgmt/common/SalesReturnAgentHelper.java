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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSaleReturnBean;
import com.skilrock.lms.beans.CommVarGovtCommBean;
import com.skilrock.lms.beans.OrgInfoBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.beans.UserInfoBean;
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

public class SalesReturnAgentHelper {
	String bookFlag = "Invalid";
	String packFlag = "Invalid";

	
	public String doTransaction(int game_id, int org_id, List<PackBean> packlist,
			List<BookBean> booklist, String rootPath,
			String newBookStatus, int agent_id, int agent_org_id, String agent_org_name, Connection conn) throws LMSException {

		int receipt_id = 0;
		int transaction_id = 0;
		double ticket_price = 0, book_price = 0;
		int nbr_of_tickets_per_book = 0, nbr_of_books_per_pack = 0;
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

		List<Integer> trnIdList = new ArrayList<Integer>();
		 
//		Connection conn = null;
//		conn = DBConnect.getConnection();
		try {
//			conn.setAutoCommit(false);
			// get books list from packlist and copy to booklist

			// new book status on sales return
			// String newBookStatus = "ACTIVE";
			System.out.println("***Return Book Status Should be**************"
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
				// agent_sale_comm_rate =
				// rsGameDetail.getDouble("agent_sale_comm_rate");
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
				commVariaceStmt.setString(1, "RETAILER");
				commVariaceStmt.setInt(2, org_id);
				commVariaceStmt.setInt(3, game_id);
				commVariaceStmt.setString(4, bookNbr);
				commVariaceStmt.setString(5, "AGENT");
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
				// bookTaxableSale=(((book_price*(100-(commVariance +
				// prizePayOutPer +govtCommRate)))/100)*100)/(100+vat);
				// bookTaxableSale=vatAmt/(vat * 0.01);
				bookTaxableSale = CommonMethods.calTaxableSale(book_price,
						commVariance, prizePayOutPer, govtCommRate, vat);

				bookSaleRetBean.setBookNumber(bookNbr);
				bookSaleRetBean.setBookCommVariance(commVariance);
				// bookSaleRetBean.setDefaultCommVariance(agent_sale_comm_rate);
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

			// Set<Double> commVarianceSet=new HashSet<Double>();

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
			// System.out.println(" 22222222222222222222222size for comm var
			// history " + commVariancelist.size() + "pstmt " +
			// stmtCommVarHistory);
			Iterator iteratorBookSaleReturn;
			// Iterator iteratorCommVarHistory= commVarianceSet.iterator();
			Iterator iteratorCommVarHistory = commVariancelist.iterator();
			while (iteratorCommVarHistory.hasNext()) {
				boolean bookCommVarMatch = false;
				// System.out.println("comm var from history--------------------
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

				// System.out.println("comm var from history--------------------
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
					// System.out.println("commFromHistory " + commFromHistory +
					// "bookCommVariance " + bookCommVariance);
					// System.out.println("GovtcommFromHistory " +
					// govtCommFromHistory + "bookGovtCommVariance " +
					// bookGovtCommVariance);
					
					if (commFromHistory == bookCommVariance
							&& govtCommFromHistory == bookGovtCommVariance) {
						boolean isSaleTransactionExist = true;
						if("YES".equals(Utility.getPropertyValue("IS_SCRATCH_NEW_FLOW_ENABLED"))){
							bookNumber = bookSaleRetBean.getBookNumber();
								String saleTransactionQuery = "select current_owner_id from st_se_game_inv_status where book_nbr = '"+bookSaleRetBean.getBookNumber()+"' and ret_invoice_id IS NOT NULL";	
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
								// System.out.println("inside
								// if%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
								totCommAmt = totCommAmt
										+ bookSaleRetBean.getBookCommAmt();
								totVatAmt = totVatAmt + bookSaleRetBean.getBookVatAmt();
								totNetAmt = totNetAmt + bookSaleRetBean.getBookNetAmt();
								totTaxableSale = totTaxableSale
										+ bookSaleRetBean.getBookTaxableSale();
								totGovtComm = totGovtComm
										+ bookSaleRetBean.getGovtCommAmt();
								bookListforSingleTrn.add(bookSaleRetBean
										.getBookNumber());
						}
					}

				}
				netAmtOrg = netAmtOrg + totNetAmt;

				if (bookCommVarMatch) {

					// insert into LMS transaction master
					String queryLMSTrans = QueryManager
							.insertInLMSTransactionMaster();
					PreparedStatement stmtLMSTransmas = conn
							.prepareStatement(queryLMSTrans);
					stmtLMSTransmas.setString(1, "AGENT");
					stmtLMSTransmas.executeUpdate();
					// Get Transaction Id From the table.
					ResultSet rsTransId = stmtLMSTransmas.getGeneratedKeys();
					while (rsTransId.next()) {
						transaction_id = rsTransId.getInt(1);
						trnIdList.add(transaction_id);
					}

					System.out.println("transaction_id:  " + transaction_id);

					// 1. Insert Entry in st_lms_agent_transaction_master Table.
					String queryTranMas = QueryManager
							.insertInAgentTransactionMaster();
					PreparedStatement stmtTransmas = conn
							.prepareStatement(queryTranMas);

					stmtTransmas.setInt(1, transaction_id);
					stmtTransmas.setInt(2, agent_id);
					stmtTransmas.setInt(3, agent_org_id);
					stmtTransmas.setString(4, "RETAILER");
					stmtTransmas.setInt(5, org_id);
					stmtTransmas.setString(6, "SALE_RET");
					stmtTransmas.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					stmtTransmas.executeUpdate();

					// Insert Entry in st_se_agent_retailer_transaction table.
					String queryAgtRetTrans = QueryManager
							.getST4InsertAgentRetailerTransaction();
					PreparedStatement stmtAgtRetTrans = conn
							.prepareStatement(queryAgtRetTrans);
					stmtAgtRetTrans.setInt(1, transaction_id);
					stmtAgtRetTrans.setInt(2, game_id);
					stmtAgtRetTrans.setInt(3, agent_id);
					stmtAgtRetTrans.setInt(4, org_id);
					stmtAgtRetTrans.setDouble(5, book_price
							* bookListforSingleTrn.size());
					stmtAgtRetTrans.setDouble(6, totCommAmt);
					stmtAgtRetTrans.setDouble(7, totNetAmt);
					stmtAgtRetTrans.setString(8, "SALE_RET");
					stmtAgtRetTrans.setInt(9, bookListforSingleTrn.size());
					stmtAgtRetTrans.setDouble(10, totVatAmt);
					stmtAgtRetTrans.setDouble(11, totTaxableSale);
					stmtAgtRetTrans.setInt(12, agent_org_id);
					stmtAgtRetTrans.executeUpdate();

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

					// Insert in to st_se_game_inv_detail table.
					for (int i = 0; i < bookListforSingleTrn.size(); i++) {
						String bknbr = (String) bookListforSingleTrn.get(i);
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

						String queryGameInvDtl = "INSERT INTO st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id) SELECT ?,?,?,?,?,?,?,?,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id FROM st_se_game_inv_detail WHERE book_nbr=? AND transaction_at='AGENT' ORDER BY transaction_date DESC LIMIT 1";
						PreparedStatement stmtGameInvDtl = conn
								.prepareStatement(queryGameInvDtl);
						stmtGameInvDtl.setInt(1, transaction_id);
						stmtGameInvDtl.setInt(2, game_id);
						stmtGameInvDtl.setString(3, pknbr);
						stmtGameInvDtl.setString(4, bknbr);
						stmtGameInvDtl.setString(5, "AGENT");
						stmtGameInvDtl.setInt(6, agent_org_id); // person who is
						// logged in
						stmtGameInvDtl.setTimestamp(7, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						stmtGameInvDtl.setString(8, "AGENT");
						stmtGameInvDtl.setString(9, bknbr);
						//stmtGameInvDtl.setString(10, "AGENT");
						stmtGameInvDtl.executeUpdate();

						// insert into detail history table Added by arun
						detHistoryInsPstmt.setInt(1, game_id);
						detHistoryInsPstmt.setString(2, bknbr);
						detHistoryInsPstmt.setString(3, "AGENT");
						detHistoryInsPstmt.setInt(4, agent_org_id);
						detHistoryInsPstmt.setTimestamp(5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						detHistoryInsPstmt.setInt(6, agent_org_id);
						detHistoryInsPstmt.setInt(7, agent_id);
						detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
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
						// ---------------------

					}
	
				}else{
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
						
						String queryGameInvDtl = "INSERT INTO st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id) SELECT ?,?,?,?,?,?,?,?,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate,warehouse_id FROM st_se_game_inv_detail WHERE book_nbr=? AND transaction_at='AGENT' ORDER BY transaction_date DESC LIMIT 1";
						PreparedStatement stmtGameInvDtl = conn
								.prepareStatement(queryGameInvDtl);
						stmtGameInvDtl.setInt(1, transaction_id);
						stmtGameInvDtl.setInt(2, game_id);
						stmtGameInvDtl.setString(3, pknbr);
						stmtGameInvDtl.setString(4, bknbr);
						stmtGameInvDtl.setString(5, "AGENT");
						stmtGameInvDtl.setInt(6, agent_org_id); // person who is
						// logged in
						stmtGameInvDtl.setTimestamp(7, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						stmtGameInvDtl.setString(8, "AGENT");
						stmtGameInvDtl.setString(9, bknbr);
						//stmtGameInvDtl.setString(10, "AGENT");
						stmtGameInvDtl.executeUpdate();
				}
			}

			System.out.println("5. Update st_se_game_inv_status Table.	");
			// Update st_se_game_inv_status Table.
			for (int i = 0; i < bookSaleReturnList.size(); i++) {
				String bknbr = ((BookSaleReturnBean) bookSaleReturnList.get(i))
						.getBookNumber();
				String queryGameInvStatus = QueryManager
						.getST4UdateGameInvStatusForBookForAgent();
				PreparedStatement stmtGameInvStatus = conn
						.prepareStatement(queryGameInvStatus);
				stmtGameInvStatus.setString(1, newBookStatus);
				stmtGameInvStatus.setInt(2, agent_org_id);
				stmtGameInvStatus.setString(3, null);
				stmtGameInvStatus.setInt(4, game_id);
				stmtGameInvStatus.setString(5, bknbr);
				stmtGameInvStatus.executeUpdate();
				System.out.println("******Update st_se_game_inv_status*****"
						+ stmtGameInvStatus);
			}

			// get auto generated treciept number
			PreparedStatement autoGenRecptPstmt = null;
			// String getLatestRecieptNumber="SELECT * from
			// st_lms_agent_receipts_gen_mapping where receipt_type=? and
			// agt_org_id=? ORDER BY generated_id DESC LIMIT 1 ";
			String queryRcpt = "SELECT * from st_lms_agent_receipts where receipt_type like ('CR_NOTE%') and agent_org_id=?  ORDER BY generated_id DESC LIMIT 1";
			/*autoGenRecptPstmt = conn.prepareStatement(QueryManager
					.getAGENTLatestReceiptNb());*/
			autoGenRecptPstmt = conn.prepareStatement(queryRcpt);
			//autoGenRecptPstmt.setString(1, "CR_NOTE");
			autoGenRecptPstmt.setInt(1, agent_org_id);
			ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNoAgt = GenerateRecieptNo.getRecieptNoAgt(
					"CR_NOTE", lastRecieptNoGenerated, "AGENT", agent_org_id);

			// get auto generated delivery Challan number
			String getLatestDCNumber = "SELECT * from st_lms_agent_receipts_gen_mapping where receipt_type=? and agt_org_id=?  ORDER BY generated_id DESC LIMIT 1 ";
			// autoGenRecptPstmt=conn.prepareStatement(QueryManager.getAGENTLatestReceiptNb());			
			autoGenRecptPstmt = conn.prepareStatement("SELECT * from st_lms_agent_receipts where receipt_type = ? and agent_org_id=?  ORDER BY generated_id DESC LIMIT 1");		
			autoGenRecptPstmt.setString(1, "DSRCHALLAN");
			autoGenRecptPstmt.setInt(2, agent_org_id);
			ResultSet DCRs = autoGenRecptPstmt.executeQuery();
			String lastDCNoGenerated = null;

			while (DCRs.next()) {
				lastDCNoGenerated = DCRs.getString("generated_id");
			}

			String autoGeneDCNo = null;
			autoGeneDCNo = GenerateRecieptNo.getRecieptNoAgt("DSRCHALLAN",
					lastDCNoGenerated, "AGENT", agent_org_id);

			// insert in receipt master for receipt
			PreparedStatement stmtRecptId = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			stmtRecptId.setString(1, "AGENT");
			stmtRecptId.executeUpdate();

			ResultSet rsRecptId = stmtRecptId.getGeneratedKeys();
			while (rsRecptId.next()) {
				receipt_id = rsRecptId.getInt(1);
			}

			// insert in receipt master for delevery challan
			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			stmtRecptId.setString(1, "AGENT");
			stmtRecptId.executeUpdate();

			ResultSet rsDC = stmtRecptId.getGeneratedKeys();
			
			while (rsDC.next()) {
				DCId = rsDC.getInt(1);
			}

			// Insert Entry in st_agent_receipt table.

			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInAgentReceipts());
			stmtRecptId.setInt(1, receipt_id);
			stmtRecptId.setString(2, "CR_NOTE");
			stmtRecptId.setInt(3, agent_org_id);
			stmtRecptId.setInt(4, org_id);
			stmtRecptId.setString(5, "RETAILER");
			stmtRecptId.setString(6, autoGeneRecieptNoAgt);
			stmtRecptId.setTimestamp(7, Util.getCurrentTimeStamp());
			stmtRecptId.executeUpdate();

			// insert in agent receipt for delevery challan

			stmtRecptId = conn.prepareStatement(QueryManager
					.insertInAgentReceipts());
			stmtRecptId.setInt(1, DCId);
			stmtRecptId.setString(2, "DSRCHALLAN");
			stmtRecptId.setInt(3, agent_org_id);
			stmtRecptId.setInt(4, org_id);
			stmtRecptId.setString(5, "RETAILER");
			stmtRecptId.setString(6, autoGeneDCNo);
			stmtRecptId.setTimestamp(7, Util.getCurrentTimeStamp());
			stmtRecptId.executeUpdate();

			// Insert Entry in st_lms_agent_receipts_trn_mapping table.
			String queryRcptTrnMapping = QueryManager
					.insertAgentReceiptTrnMapping();
			PreparedStatement stmtRcptTrnMapping = conn
					.prepareStatement(queryRcptTrnMapping);
			for (int i = 0; i < trnIdList.size(); i++) {
				stmtRcptTrnMapping.setInt(1, receipt_id);
				stmtRcptTrnMapping.setInt(2, (Integer) trnIdList.get(i));
				stmtRcptTrnMapping.executeUpdate();
			}

			// Insert Entry in st_lms_agent_receipts_trn_mapping table for
			// delivery challan
			stmtRcptTrnMapping = conn.prepareStatement(queryRcptTrnMapping);
			for (int i = 0; i < trnIdList.size(); i++) {
				stmtRcptTrnMapping.setInt(1, DCId);
				stmtRcptTrnMapping.setInt(2, (Integer) trnIdList.get(i));
				stmtRcptTrnMapping.executeUpdate();
			}
			// insert into invoice and delivery challan mapping table

			String insertInvoiceDCMapping = "insert into st_se_agent_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
			PreparedStatement stmt22 = conn
					.prepareStatement(insertInvoiceDCMapping);
			stmt22 = conn.prepareStatement(insertInvoiceDCMapping);
			stmt22.setInt(1, receipt_id);
			stmt22.setString(2, autoGeneRecieptNoAgt);
			stmt22.setString(3, autoGeneDCNo);
			stmt22.executeUpdate();

			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(netAmtOrg, "TRANSACTION", "SALE_RET", org_id,
					agent_org_id, "RETAILER", 0, conn);
			
			if(!isValid)
				throw new LMSException();
			
			/*boolean isUpdateDone = OrgCreditUpdation
					.updateCreditLimitForRetailer(org_id, "SALE_RET",
							netAmtOrg, conn);*/
//			conn.commit();
//			session.setAttribute("DEL_CHALLAN_ID", DCId);
//			if (receipt_id > 0) {
//				GraphReportHelper graphReportHelper = new GraphReportHelper();
//				// graphReportHelper.createTextReportBO(receipt_id,orgName,userOrgId,rootPath);
//				graphReportHelper.createTextReportAgent(receipt_id, rootPath,
//						agent_org_id, agent_org_name);
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException(e);
		}
		return String.valueOf(DCId) + "Nxt" +  String.valueOf(receipt_id);
	}
	
	
	/**
	 * This method is used to make entry into database for returned packs and
	 * books
	 * 
	 * @param game_id
	 * @param org_id
	 * @param packlist
	 *            list of valid packs
	 * @param booklist
	 *            list of valid books
	 * @param session
	 * @param rootPath
	 * @param newBookStatus
	 * @param loggedInUserOrgName
	 *            is agents organization name
	 * @return void
	 * @throws LMSException
	 */

	public int doTransaction(int game_id, int org_id, List<PackBean> packlist,
			List<BookBean> booklist, HttpSession session, String rootPath,
			String newBookStatus) throws LMSException {

		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		 
		Connection conn = null;
		int receipt_id =0;
		
		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			String returnValue = doTransaction(game_id, org_id, packlist, booklist, rootPath, newBookStatus, userBean.getUserId(), userBean.getUserOrgId(), userBean.getOrgName(), conn);
			conn.commit();
			session.setAttribute("DEL_CHALLAN_ID", Integer.parseInt(returnValue.split("Nxt")[0]));
			receipt_id = Integer.parseInt(returnValue.split("Nxt")[1]);
			if (receipt_id > 0) {
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				// graphReportHelper.createTextReportBO(receipt_id,orgName,userOrgId,rootPath);
				graphReportHelper.createTextReportAgent(receipt_id, rootPath,
						userBean.getUserOrgId(), userBean.getOrgName());
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
	 * This method is used to verify books at the time of sale return @ agent
	 * @param bookList
	 * @param game_id
	 * @param org_id
	 * @return List of verified books
	 */
	// Do Bulk Verification Of Books.
	public List<BookBean> doVerifiedBooks(List<BookBean> bookList, int game_id,
			int org_id, String isRetOnline, int game_nbr) throws LMSException {
		System.out
				.println("Enter in to the bulk Book verification@@@@@@@@@@@@@@@@@@@@@@");
		Connection conn = DBConnect.getConnection();
		try {
			List<BookBean> list = new ArrayList<BookBean>();
			Iterator<BookBean> iterator = bookList.iterator();
			BookBean bean = null;
			String bknbr = null;

			// boolean bookExistancecheck = false;
			// boolean ownercheck = false;
			// boolean pwtCheck = false;
			// boolean pwtCheckTemp = false;
			// boolean bookStatusCheck=false;
			boolean isBookValid = false;

			while (iterator.hasNext()) {
				bean = (BookBean) iterator.next();
				bknbr = bean.getBookNumber();

				if (bknbr != null) {
					isBookValid = verifyBook(game_id, bknbr, org_id, conn);
					if (isBookValid) {
						System.out.println("book is valid " + bknbr);
						setPackFlag("Valid");
						bean.setValid(true);
						bean.setStatus("Book Is Valid");
						list.add(bean);
					} else {
						System.out.println("book is Invalid " + bknbr);
						bean.setValid(false);
						bean.setStatus("Book Is InValid");
						list.add(bean);
					}
				}

				/*
				 * if(bknbr!=null){
				 * 
				 * bookExistancecheck = verifyBookWithExistance(game_id, bknbr,
				 * org_id, conn); ownercheck = verifyBookWithOwner(game_id,
				 * bknbr, org_id, conn); pwtCheck =
				 * verifyBookValidityWithPWT(game_id, bknbr, conn,game_nbr);
				 * pwtCheckTemp=verifyBookValidityWithPWTTempTable(game_id,
				 * bknbr, conn);
				 * 
				 * if(isRetOnline.equals("YES")) {
				 * bookStatusCheck=verifyBookWithBookStatus(game_id, bknbr); }
				 * else bookStatusCheck=true; } if(ownercheck == true &&
				 * pwtCheck == true && bookExistancecheck == true &&
				 * bookStatusCheck == true && pwtCheckTemp == true){
				 * 
				 * System.out.println("book is valid "+bknbr);
				 * setPackFlag("Valid"); bean.setValid(true);
				 * bean.setStatus("Book Is Valid"); list.add(bean); } else{
				 * System.out.println("book is Invalid "+bknbr);
				 * 
				 * bean.setValid(false); bean.setStatus("Book Is InValid");
				 * list.add(bean); }
				 */
			}
			return list;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to verify packs for sale return
	 * 
	 * @param packList
	 * @param game_id
	 * @param org_id
	 * @param game_nbr
	 * @return List of verified packs
	 */
	// Do Bulk Verification Of Packs.
	public List<PackBean> doVerifiedPacks(List<PackBean> packList, int game_id,
			int org_id, String isRetOnline, int game_nbr) throws LMSException {
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
		// boolean bookStatusCheck=false;
		boolean isPackValid = false;
		Connection conn = null;
		 
		conn = DBConnect.getConnection();
		try {
			while (iterator.hasNext()) {
				bean = (PackBean) iterator.next();
				pknbr = bean.getPackNumber();

				if (pknbr != null) {
					isPackValid = verifyPack(game_id, pknbr, org_id, conn);
					if (isPackValid) {
						System.out.println("pack is valid " + pknbr);
						bean.setValid(true);
						bean.setStatus("Pack Is Valid");
						setPackFlag("Valid");
						list.add(bean);
					} else {
						bean.setValid(false);
						bean.setStatus("Pack Is InValid");
						list.add(bean);
					}
				}

				/*
				 * if(pknbr!=null){
				 * 
				 * //System.out.println("Pack is not null"+pknbr);
				 * 
				 * packExistancecheck = verifyPackWithExistance(game_id, pknbr,
				 * org_id); ownercheck = verifyPackWithOwner(game_id, pknbr,
				 * org_id); pwtCheck = verifyPackValidityWithPWT(game_id,
				 * pknbr,game_nbr);
				 * pwtCheckTemp=verifyPackValidityWithPWTTempTable(game_id,
				 * pknbr);
				 * 
				 * if(isRetOnline.equals("YES")) {
				 * bookStatusCheck=verifyBookStatus(game_id, pknbr); } else {
				 * bookStatusCheck=true; }
				 * 
				 * System.out.println("packExistancecheck:
				 * "+packExistancecheck); System.out.println("ownercheck:
				 * "+ownercheck); System.out.println("pwtCheck: "+pwtCheck); }
				 * 
				 * if(ownercheck == true && pwtCheck == true &&
				 * packExistancecheck == true && bookStatusCheck == true &&
				 * pwtCheckTemp==true){ //System.out.println("pack is valid
				 * "+pknbr);
				 * 
				 * bean.setValid(true); bean.setStatus("Pack Is Valid");
				 * setPackFlag("Valid"); list.add(bean); } else{
				 * bean.setValid(false); bean.setStatus("Pack Is InValid");
				 * list.add(bean); }
				 */
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
	 * This method is used to get game Id from game name
	 * 
	 * @param game_Name
	 * @return int game Id
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
	 * This method is used to get the organization Id from the organization name
	 * 
	 * @param organization_Name
	 * @return int (organization Id)
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

	// For Get The OrganizationBean of Retailer in a list.
	public List<OrgInfoBean> getOrganizations(HttpSession session) {

		// Get Logged in User's organization Id
		int agent_id = 0;
		int agent_org_id = 0;
		UserInfoBean u = (UserInfoBean) session.getAttribute("USER_INFO");
		agent_id = u.getUserId();
		agent_org_id = u.getUserOrgId();
		System.out.println("agent id is: " + agent_id + " agent_org_id : "
				+ agent_org_id);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			OrgInfoBean orgBean = null;
			List<OrgInfoBean> orgList = new ArrayList<OrgInfoBean>();
			 
			connection = DBConnect.getConnection();

			String query = QueryManager.getST4RetailerOrganizationSearchQuery()
					+ " order by name";
			statement = connection.prepareStatement(query);
			statement.setInt(1, agent_org_id);
			resultSet = statement.executeQuery();
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

	public List<BookBean> saveBookData(int game_id, int org_id,
			List<BookBean> bookList) {
		return bookList;
	}

	public List<PackBean> savePackData(int game_id, int org_id,
			List<PackBean> packList) {
		return packList;
	}

	/**
	 * This method is used to get the valid books from verifird books
	 * 
	 * @param bookBeanList
	 *            is List of verifies books
	 * @return List(BookBean type) of valid books
	 */
	// Get Valid Books from the verified book list.
	public List<BookBean> selectValidBooks(List<BookBean> bookBeanList) {
		List<BookBean> validBookBeanList = new ArrayList<BookBean>();
		BookBean bean = null;
		Iterator<BookBean> itr = bookBeanList.iterator();
		while (itr.hasNext()) { // if loop is replaced by while by yogesh
			bean = itr.next();
			if (bean.getIsValid() == true) {
				validBookBeanList.add(bean);
			}
		}
		return validBookBeanList;
	}

	/**
	 * This method is used to get the valid packs from verified pack list
	 * 
	 * @param packBeanList
	 *            is List of verified packs
	 * @return List(PackBean type) of valid packs
	 */
	// Get Valid Packs from the verified pack list.
	public List<PackBean> selectValidPacks(List<PackBean> packBeanList) {
		List<PackBean> validPackBeanList = new ArrayList<PackBean>();
		PackBean bean = null;
		Iterator<PackBean> itr = packBeanList.iterator();
		while (itr.hasNext()) { // changed by yogesh at the place of if loop
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
	 * This method is used to verify book @ agent end
	 * @param game_id
	 * @param bknbr
	 * @param org_id
	 * @param con
	 * @return
	 */

	public boolean verifyBook(int game_id, String bknbr, int org_id,
			Connection conn) throws LMSException {
		System.out
				.println("Enter In To verify book with owner ******************************");

		boolean bookflag = false;
		try {
			String query1 = "select current_owner_id,current_owner,book_status from st_se_game_inv_status aa, st_se_game_master bb where aa.game_id=? and book_nbr= ? and aa.game_id = bb.game_id and cur_rem_tickets = nbr_of_tickets_per_book ";
			// "QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn.prepareStatement(query1);
			pstmt.setInt(1, game_id);
			pstmt.setString(2, bknbr);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (org_id == rs.getInt("current_owner_id")
						&& "RETAILER".equalsIgnoreCase(rs
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

	// check pack number with book status
	public boolean verifyBookStatus(int game_id, String pknbr) {

		System.out
				.println("Enter In To verify pack with book status -----------------------------");
		boolean packflag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement stmt1 = null;
		String query1 = QueryManager
				.getST4BookStatusDetailsAndBookNbrUsingGamePackNbr();
		try {
			stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, pknbr);
			ResultSet rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				if (!rs1.getString("book_status").equals("INACTIVE")) {
					packflag = false;
					// System.out.println("Pack No. :"+pknbr +" of Book no
					// :"+rs1.getString("book_nbr")+ " is of the Organization:
					// "+rs1.getInt("current_owner_id")+" ");
				}
			}
			if (packflag == false) {
				System.out.println("packflag is not valid: " + packflag);
			} else {
				System.out.println("packflag is valid: " + packflag);
			}
			System.out
					.println("----------------------------- verify pack with book staus is complete");
			return packflag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return packflag;

	}

	// Check Book Verification For PWT Tickets.
	public boolean verifyBookValidityWithPWT(int game_id, String bknbr,
			Connection con, int game_nbr) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		//  
		Connection conn = null;
		// conn= DBConnect.getConnection();
		conn = con;
		String query6 = QueryManager.getST4BookOfTicketsOfPwt();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, game_nbr);
			stmt6.setInt(2, game_id);
			stmt6.setString(3, bknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				bookPwtFlag = false;
			}

			if (bookPwtFlag == false) {
				System.out.println("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				System.out.println("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT is complete");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bookPwtFlag;
	}

	// Check Book Verification For PWT Tickets in temp ticket table.
	public boolean verifyBookValidityWithPWTTempTable(int game_id,
			String bknbr, Connection con) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		//  
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
				System.out.println("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				System.out.println("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT in temporary ticket table  is complete");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookPwtFlag;
	}

	// check book with book status
	public boolean verifyBookWithBookStatus(int game_id, String bknbr) {

		boolean bookflag = false;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		try {
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement stmt1 = conn.prepareStatement(query1);
			stmt1.setInt(1, game_id);
			stmt1.setString(2, bknbr);
			ResultSet rs1 = stmt1.executeQuery();
			;
			while (rs1.next()) {
				if (rs1.getString("book_status").equals("INACTIVE")) {
					bookflag = true;
					// System.out.println("bookflag: "+bookflag);
				}
			}
			if (bookflag == false) {
				System.out.println("bookflag is not valid: " + bookflag);
			} else {
				System.out.println("bookflag is valid: " + bookflag);
			}

			System.out
					.println("************************* verify book with book status is complete");
			return bookflag;

		} catch (SQLException e) {
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
		return bookflag;

	}

	// Check Book Verification for Existence.
	public boolean verifyBookWithExistance(int game_id, String bknbr,
			int org_id, Connection con) {
		System.out
				.println("Enter In To verify Book with Existence -----------------------------");
		boolean bookExistenceflag = true;
		//  
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
					// System.out.println("Book No. :"+bknbr +" is not Exist in
					// Organization. Totla count is: "+rs1.getInt("total"));
				}
			}
			if (bookExistenceflag == false) {
				System.out.println("bookExistenceflag is not Exist: "
						+ bookExistenceflag);
			} else {
				System.out.println("bookExistenceflag is Exist: "
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
					/*
					 * if(conn!=null) conn.close();
					 */
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bookExistenceflag;
	}

	/*
	 * public List<TicketBean> getVerifiedTickets(List<TicketBean> ticketBean,
	 * int game_id){ List<TicketBean> verifyResults = new ArrayList<TicketBean>();
	 * Connection connection = null; PreparedStatement preparedStatement1=null;
	 * PreparedStatement preparedStatement2=null; PreparedStatement
	 * preparedStatement3=null; ResultSet resultSet1 = null; ResultSet
	 * resultSet2 = null; ResultSet resultSet3 = null; try { 
	 * connection = DBConnect.getConnection(); String query1=
	 * QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr(); String query2=
	 * QueryManager.getST4PwtTicketDetailsUsingTicketNbr(); String query3=
	 * QueryManager.getST4GameDetailsUsingGameId();
	 * 
	 * 
	 * 
	 * Iterator<TicketBean> iterator=ticketBean.iterator();
	 * while(iterator.hasNext()){ String ticket_nbr=null; int size=0;
	 * ticket_nbr=iterator.next().getTicketNumber(); size=ticket_nbr.length();
	 * 
	 * if(size!=0){
	 * 
	 * TicketBean bean=new TicketBean(); bean.setTicketNumber(ticket_nbr);
	 * String book_nbr=getBookNbrFromTicketNo(ticket_nbr); int
	 * actual_tkt_nbr=Integer.parseInt(getTicketNbrFromTicketNo(ticket_nbr));
	 * System.out.println("Book No is: "+book_nbr);
	 * 
	 * preparedStatement1=connection.prepareStatement(query1);
	 * preparedStatement1.setInt(1,game_id);
	 * preparedStatement1.setString(2,book_nbr);
	 * resultSet1=preparedStatement1.executeQuery();
	 * 
	 * if(resultSet1.next()){
	 * 
	 * if(resultSet1.getString("current_owner").equals("RETAILER")){
	 * 
	 * preparedStatement2=connection.prepareStatement(query2);
	 * preparedStatement2.setString(1,ticket_nbr);
	 * resultSet2=preparedStatement2.executeQuery();
	 * 
	 * if(resultSet2.next()){ bean.setValid(false); bean.setStatus("Ticket's PWT
	 * Has Been Paid."); verifyResults.add(bean); System.out.println("Ticket IS
	 * exist in pwt table."); } else{
	 * preparedStatement3=connection.prepareStatement(query3);
	 * preparedStatement3.setInt(1,game_id);
	 * resultSet3=preparedStatement3.executeQuery(); if(resultSet3.next()){
	 * if(resultSet3.getInt("nbr_of_tickets_per_book")<=actual_tkt_nbr){
	 * bean.setValid(false); bean.setStatus("Ticket Is Fake.");
	 * verifyResults.add(bean); System.out.println("Ticket number is fake."); }
	 * else{ bean.setValid(true); bean.setStatus("Ticket Is Valid For PWT.");
	 * verifyResults.add(bean); System.out.println("Ticket IS valid for pwt.
	 * (Means not fake and not in pwt table)"); } } } } else{
	 * bean.setValid(false); bean.setStatus("Ticket's Owner Is Not Valid. Or
	 * Ticket Is In Our Stock In Our Chain"); verifyResults.add(bean);
	 * System.out.println("Ticket owner is not Retailer."); } } else{
	 * bean.setValid(false); bean.setStatus("Ticket Is Not Of The Our
	 * Company."); verifyResults.add(bean); System.out.println("Ticket Is not of
	 * the company."); } } } System.out.println("The verified List Is: "+
	 * verifyResults); return verifyResults; }catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }finally{ try {
	 * if(preparedStatement1!=null) preparedStatement1.close();
	 * if(preparedStatement2!=null) preparedStatement2.close(); } catch
	 * (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * return null; }
	 * 
	 * 
	 */

	/*
	 * public List<TicketBean> saveTicketsData(int game_id, List<TicketBean>
	 * verifiedTicketList){ List<TicketBean> savedResults = new ArrayList<TicketBean>();
	 * Connection connection = null; PreparedStatement Pstmt = null; ResultSet
	 * resultSet = null; String query=QueryManager.updateIntoPwtTicketsInv();
	 * try {  
	 * connection=DBConnect.getConnection();
	 * Pstmt=connection.prepareStatement(query); Iterator<TicketBean>
	 * iterator=verifiedTicketList.iterator(); while(iterator.hasNext()){ String
	 * ticket_nbr=null; boolean isValid=false; int size=0; TicketBean
	 * ticketBean=(TicketBean)iterator.next(); isValid=ticketBean.getIsValid();
	 * if(isValid==true){ ticket_nbr=ticketBean.getTicketNumber(); String
	 * book_nbr=getBookNbrFromTicketNo(ticket_nbr); Pstmt.setString(1,
	 * ticket_nbr); Pstmt.setInt(2,game_id); Pstmt.setString(3, book_nbr);
	 * Pstmt.executeUpdate();
	 * 
	 * TicketBean bean=new TicketBean(); bean.setTicketNumber(ticket_nbr);
	 * bean.setValid(isValid); savedResults.add(bean); } } return savedResults; }
	 * catch (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }finally{ try { if(Pstmt!=null) Pstmt.close(); }
	 * catch (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * return null; }
	 */

	// Check Book Verification for Owner.
	public boolean verifyBookWithOwner(int game_id, String bknbr, int org_id,
			Connection con) {

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
			;
			while (rs1.next()) {
				if (org_id == rs1.getInt("current_owner_id")) {
					bookflag = true;
					// System.out.println("bookflag: "+bookflag);
				}
			}
			if (bookflag == false) {
				System.out.println("bookflag is not valid: " + bookflag);
			} else {
				System.out.println("bookflag is valid: " + bookflag);
			}

			System.out
					.println("************************* verify pack with owner is complete");
			return bookflag;

		} catch (SQLException e) {
			e.printStackTrace();
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
				.println("Enter In To verify pack ******************************"
						+ org_id);

		boolean bookflag = false;
		try {
			// String query1=
			// QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
			// "select book_status,current_owner_id from st_se_game_inv_status
			// where game_id=? and pack_nbr = ?"
			String verifyPackQuery = "select book_status,current_owner_id,current_owner, nbr_of_books_per_pack from st_se_game_inv_status aa, st_se_game_master bb where aa.game_id=? and pack_nbr = ? and aa.game_id = bb.game_id and cur_rem_tickets = nbr_of_tickets_per_book";
			PreparedStatement pstmt = conn.prepareStatement(verifyPackQuery);
			pstmt.setInt(1, game_id);
			pstmt.setString(2, pknbr);
			ResultSet rs = pstmt.executeQuery();
			int totalRow = 0;
			if (rs.next()) {
				rs.last();
				totalRow = rs.getRow();
				rs.beforeFirst();
			}
			System.out.println(totalRow + "totalRow" + "----verify pack = "
					+ pstmt);
			while (rs.next()) {

				if (rs.getInt("nbr_of_books_per_pack") == totalRow) {
					if (org_id == rs.getInt("current_owner_id")
							&& "RETAILER".equalsIgnoreCase(rs
									.getString("current_owner"))) {
						bookflag = true;
					} else {
						System.out
								.println("return because of org id or status or owner ");
						return false;
						// bookflag=true;
					}
				} else {
					System.out.println("return because of less book returned ");
					return false;
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

	// Check Pack Verification For PWT tickets.
	public boolean verifyPackValidityWithPWT(int game_id, String pknbr,
			int game_nbr) {

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
			stmt6.setInt(1, game_nbr);
			stmt6.setInt(2, game_id);
			stmt6.setInt(3, game_id);
			stmt6.setString(4, pknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				packPwtFlag = false;
			}
			if (packPwtFlag == false) {
				System.out.println("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				System.out.println("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete");
			return packPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return packPwtFlag;
	}

	// Check Pack Verification For PWT tickets in temporary ticket table.
	public boolean verifyPackValidityWithPWTTempTable(int game_id, String pknbr) {

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
				System.out.println("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				System.out.println("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete in temp ticket table ");
			return packPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return packPwtFlag;
	}

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
					// System.out.println("Pack No. :"+pknbr +" is not Exist in
					// Organization. Totla count is: "+rs1.getInt("total"));
				}
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return packExistenceflag;
	}

	// Check Pack Verification for Owner.
	public boolean verifyPackWithOwner(int game_id, String pknbr, int org_id) {
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
			while (rs1.next()) {
				if (!(org_id == rs1.getInt("current_owner_id"))) {
					packflag = false;
					// System.out.println("Pack No. :"+pknbr +" of Book no
					// :"+rs1.getString("book_nbr")+ " is of the Organization:
					// "+rs1.getInt("current_owner_id")+" not of the salected
					// Organization: "+org_id);
				}
			}
			if (packflag == false) {
				System.out.println("packflag is not valid: " + packflag);
			} else {
				System.out.println("packflag is valis: " + packflag);
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
		String query =  "select sbtm.transaction_id,transaction_type,sbr.receipt_type,sbr.generated_id, sbr.voucher_date from st_lms_agent_receipts_trn_mapping sbrtm,st_lms_agent_transaction_master sbtm,st_lms_agent_receipts sbr where sbrtm.receipt_id=? and  sbrtm.transaction_id=sbtm.transaction_id and sbr.receipt_id=sbrtm.receipt_id;";
		try {
			conn = DBConnect.getConnection();
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, receipt_id);
			rs = preparedStatement.executeQuery();

			if(rs.next()) {
				showCreditNote = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(conn,preparedStatement,rs);
		}

		return showCreditNote;
	}
}
