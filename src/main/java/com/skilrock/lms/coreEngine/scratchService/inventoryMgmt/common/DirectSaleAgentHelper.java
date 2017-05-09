package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSaleBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class DirectSaleAgentHelper {

	public static void main(String[] args) throws LMSException {
		List<String> l1 = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();
		Map<Integer, List<String>> gameBookMap = new HashMap<Integer, List<String>>();
		l1.add("111-001011");
		// l1.add("111-001010");

		l2.add("500-002036");
		// l2.add("500-002084");
		gameBookMap.put(52, l1);
		gameBookMap.put(57, l2);
		DirectSaleAgentHelper gg = new DirectSaleAgentHelper();
		// gg.dispatchOrderDirectSale(gameBookMap,486,114,"ANONYMOUS");

	}
	
	public String dispatchOrderDirectSale(Map<Integer, List<String>> gameBookMap,
			int agtId, int userOrgID, int retailerName, String rootPath,
			String loggedInUserOrgName, String newBookStatus, Connection connection)
			throws LMSException {

		int DCId = -1;
		int invoiceId = -1;
		int warehouseId = -1;
		if (gameBookMap != null) {

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

			PreparedStatement orderPstmt = null;
			PreparedStatement gamePstmt = null;

			ResultSet rsMaster = null;
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
			String orderQuery = null;
			String gameQuery = null;

			List<Integer> trnIdList = new ArrayList<Integer>();
			int masterTrnId = -1;
			try {

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

				agtReceiptMappingQuery = QueryManager
						.insertAgentReceiptTrnMapping();
				agtReceiptMappingStmt = connection
						.prepareStatement(agtReceiptMappingQuery);

				agtOrderQuery = QueryManager.getST1AgtOrdUpdQuery();
				agtOrderStmt = connection.prepareStatement(agtOrderQuery);

				gameInvStatusQuery = QueryManager
						.getST1AgtUpdGameInvStatusQuery();

				gameInvDetailQuery = QueryManager.getST1InvDetailWithWarehouseInsertQuery();
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

				String detHistoryInsQuery = "insert into st_se_game_ticket_inv_history(game_id, book_nbr, "
						+ " current_owner, current_owner_id, date, done_by_oid, done_by_uid, cur_rem_tickets, "
						+ " active_tickets_upto, sold_tickets, status) values (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement detHistoryInsPstmt = connection
						.prepareStatement(detHistoryInsQuery);

				double purRate = 0.0;

				double mrpAmt = 0.0;
				double commAmt = 0.0;
				double netAmt = 0.0;
				double vatAmt = 0.0;
				double taxableSale = 0.0;
				double retSaleCommRate = 0.0;
				double creditAmt = 0.0;

				Statement stmt = connection.createStatement();
				// ResultSet rsRet=stmt.executeQuery("select
				// organization_id,user_id from st_lms_user_master where
				// organization_id=(select organization_id from
				// st_lms_organization_master where
				// name='"+retailerName.trim()+"')");
				ResultSet rsRet = stmt
						.executeQuery("select organization_id,user_id from st_lms_user_master "
								+ " where organization_id = "+ retailerName
								+ " and isrolehead = 'Y' and role_id = "
								+ " (select role_id from st_lms_role_master where is_master = 'Y' and tier_id = "
								+ " (select tier_id from st_lms_tier_master where tier_code = 'RETAILER'))");
				int retId = -1;
				int retOrgId = -1;
				while (rsRet.next()) {
					retId = rsRet.getInt("user_id");
					retOrgId = rsRet.getInt("organization_id");

				}

				int gameId = -1;
				int orderId = -1;
				// get order query

				orderQuery = QueryManager.getST1InsertAgtOrderQuery();
				orderPstmt = connection.prepareStatement(orderQuery);

				// get ordered game query
				gameQuery = QueryManager.getST1InsertAgtOrderedGamesQuery();
				gamePstmt = connection.prepareStatement(gameQuery);

				// set parameters for insert into order table

				orderPstmt.setInt(1, agtId);
				orderPstmt.setInt(2, retId);
				orderPstmt.setInt(3, retOrgId);
				orderPstmt.setDate(4, new java.sql.Date(new Date().getTime()));
				orderPstmt.setString(5, "AUTO_APPROVED");
				orderPstmt.setString(6, "Y");
				orderPstmt.setInt(7, userOrgID);
				orderPstmt.execute();
				ResultSet resultSet = orderPstmt.getGeneratedKeys();

				while (resultSet.next()) {
					orderId = resultSet.getInt(1);
				}

				System.out.println("OrderId::" + orderId);

				// now we have to iterate for all games to enter orederd games
				// set parameters for insert into ordered games table
				Set<Integer> keySet = gameBookMap.keySet();
				// insert in receipt master for invoice

				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet rs = agtReceiptStmt.getGeneratedKeys();
				
				while (rs.next()) {
					invoiceId = rs.getInt(1);
				}

				// insert in receipt master for delevery challan
				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "AGENT");
				agtReceiptStmt.executeUpdate();

				ResultSet DCrs = agtReceiptStmt.getGeneratedKeys();
				DCId = -1;
				while (DCrs.next()) {
					DCId = DCrs.getInt(1);
				}

				// check book Status on dispatch
				// String newBookStatus = "ACTIVE";

				for (int gameNbr : keySet) {
					List<String> bookList = gameBookMap.get(gameNbr);
					double bookPrice = 0.0;
					double retailer_sale_comm_rate = 0.0;
					double retGameCommVariance = 0.0;
					double prizepayoutRatio = 0.0;
					double vat = 0.0;
					double fixed_amt = 0.0;
					long tickets_in_scheme = 0l;
					String govtCommRule = null;
					double govt_comm_rate = 0.0;
					boolean isGovtCommChanged = false;
					int noOfTktPerBooks = -1;
					// now get the game information from game ID

					ResultSet rsGame = stmt
							.executeQuery("select * from st_se_game_master where game_nbr="
									+ gameNbr);
					while (rsGame.next()) {
						gameId = rsGame.getInt("game_id");
						bookPrice = rsGame.getDouble("ticket_price")
								* rsGame.getInt("nbr_of_tickets_per_book");
						noOfTktPerBooks = rsGame
								.getInt("nbr_of_tickets_per_book");
						retailer_sale_comm_rate = rsGame
								.getDouble("retailer_sale_comm_rate");
						prizepayoutRatio = rsGame
								.getDouble("prize_payout_ratio");
						vat = rsGame.getDouble("vat_amt");
						fixed_amt = rsGame.getDouble("fixed_amt");
						tickets_in_scheme = rsGame.getLong("tickets_in_scheme");
						govtCommRule = rsGame.getString("govt_comm_type");
						govt_comm_rate = rsGame.getDouble("govt_comm_rate");
					}

					rsGame = stmt
							.executeQuery("select sale_comm_variance  from st_se_agent_retailer_sale_pwt_comm_variance where retailer_org_id="
									+ retOrgId + " and game_id=" + gameId);
					// System.out.println("comm var query :: " + "select
					// sale_comm_variance from
					// st_se_agent_retailer_sale_pwt_comm_variance where
					// ret_org_id="+retOrgId+" and game_id="+gameId);
					if (rsGame.next()) {
						retGameCommVariance = rsGame
								.getDouble("sale_comm_variance");
						// retailer_sale_comm_rate=rsGame.getDouble("default_sale_comm_rate");

					}
					gamePstmt.setInt(1, orderId);
					gamePstmt.setInt(2, gameId);
					gamePstmt.setInt(3, bookList.size());
					gamePstmt.setInt(4, bookList.size());
					gamePstmt.execute();
					;

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

						// Agent - Retailer Trn Master

						agtAgentStmt.setInt(1, masterTrnId);
						agtAgentStmt.setInt(2, gameId);
						agtAgentStmt.setInt(3, agtId);
						agtAgentStmt.setInt(4, retOrgId);

						mrpAmt = bookList.size() * bookPrice;
						agtAgentStmt.setDouble(5, mrpAmt);

						retSaleCommRate = retailer_sale_comm_rate
								+ retGameCommVariance;
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						agtAgentStmt.setDouble(6, commAmt);

						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						// added by yogesh to calculate vat amount
						System.out.println("&&&&&&&&&&& " + govtCommRule);
						vatAmt = CommonMethods.calculateVat(mrpAmt,
								retSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat, govtCommRule, fixed_amt,
								tickets_in_scheme);
						// taxableSale=(((mrpAmt*(100-(retSaleCommRate +
						// prizepayoutRatio
						// +govt_comm_rate)))/100)*100)/(100+vat);
						// taxableSale=vatAmt/(gameBean.getVat() * 0.01);
						taxableSale = CommonMethods.calTaxableSale(mrpAmt,
								retSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat);

						agtAgentStmt.setDouble(7, netAmt);
						agtAgentStmt.setString(8, "SALE");
						agtAgentStmt.setInt(9, bookList.size());
						agtAgentStmt.setDouble(10, vatAmt);
						agtAgentStmt.setDouble(11, taxableSale);
						agtAgentStmt.setInt(12, userOrgID);
						agtAgentStmt.execute();

						agtOrdGameStmt.setInt(1, bookList.size());
						agtOrdGameStmt.setInt(2, orderId);
						agtOrdGameStmt.setInt(3, gameId);

						agtOrdGameStmt.execute();

						int orderInvoiceDCId = 0;
						orderInvoicesPrtSt.setInt(1, orderId);
						orderInvoicesPrtSt.setInt(2, invoiceId);
						orderInvoicesPrtSt.setInt(3, retOrgId);
						orderInvoicesPrtSt.setInt(4, gameId);
						orderInvoicesPrtSt.setString(5, "AUTO_PROCESSED");
						orderInvoicesPrtSt.setInt(6, bookList.size());
						orderInvoicesPrtSt.setInt(7, agtId);
						orderInvoicesPrtSt.setInt(8, userOrgID);
						orderInvoicesPrtSt.setInt(9, DCId);
						orderInvoicesPrtSt.executeUpdate();
						rs = orderInvoicesPrtSt.getGeneratedKeys();
						while (rs.next()) {
							orderInvoiceDCId = rs.getInt(1);
						}

						if (bookList != null) {
							invQuery = gameInvStatusQuery + " and book_nbr = ?";
							gameInvStatusStmt = connection
									.prepareStatement(invQuery);
							String bookNbr = null;
							String packNbr = null;
							for (int j = 0; j < bookList.size(); j++) {
								// bookBean = bookList.get(j);

								bookNbr = bookList.get(j);
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
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, bookNbr);
								gameInvStatusStmt.execute();

								packNbr = getPackNbrForBook(connection, gameId,
										bookNbr);
								
								warehouseId = com.skilrock.lms.scratchService.dataMgmt.daoImpl.ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, bookNbr);

								// added by arun
								fetchPurRatePstmt.setString(1, bookNbr);
								fetchPurRateRs = fetchPurRatePstmt
										.executeQuery();

								if (fetchPurRateRs.next()) {
									purRate = fetchPurRateRs
											.getDouble("transacrion_sale_comm_rate");

								}

								gameInvDetailStmt.setInt(1, masterTrnId);
								gameInvDetailStmt.setInt(2, gameId);
								gameInvDetailStmt.setString(3, packNbr);
								gameInvDetailStmt.setString(4, bookNbr);
								gameInvDetailStmt.setString(5, "RETAILER");
								gameInvDetailStmt.setInt(6, retOrgId);
								gameInvDetailStmt.setTimestamp(7, currentDate);
								gameInvDetailStmt.setString(8, "AGENT");
								gameInvDetailStmt.setString(9, newBookStatus);
								//gameInvDetailStmt.setDouble(9, retSaleCommRate);
								//gameInvDetailStmt.setDouble(10, govt_comm_rate);
								gameInvDetailStmt.setInt(10, orderInvoiceDCId);
								gameInvDetailStmt.setInt(11, warehouseId);
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
					}

					else {

						mrpAmt = bookList.size() * bookPrice;
						retSaleCommRate = retailer_sale_comm_rate
								+ retGameCommVariance;
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;

						ResultSet rsCommVar;
						ArrayList<BookSaleBean> bookSaleBeanList = new ArrayList<BookSaleBean>();
						BookSaleBean bookSaleBean;

						int orderInvoiceDCId = 0;
						orderInvoicesPrtSt.setInt(1, orderId);
						orderInvoicesPrtSt.setInt(2, invoiceId);
						orderInvoicesPrtSt.setInt(3, retOrgId);
						orderInvoicesPrtSt.setInt(4, gameId);
						orderInvoicesPrtSt.setString(5, "AUTO_PROCESSED");
						orderInvoicesPrtSt.setInt(6, bookList.size());
						orderInvoicesPrtSt.setInt(7, agtId);
						orderInvoicesPrtSt.setInt(8, userOrgID);
						orderInvoicesPrtSt.executeUpdate();
						rs = orderInvoicesPrtSt.getGeneratedKeys();
						while (rs.next()) {
							orderInvoiceDCId = rs.getInt(1);
						}

						for (int bookListSize = 0; bookListSize < bookList
								.size(); bookListSize++) {
							bookSaleBean = new BookSaleBean();
							double govtCommRate = 0.0;
							double vatAmtForBook = 0.0;
							double taxableSaleForbook = 0.0;
							// bookBean=bookList.get(bookListSize);
							// String bookfromBookList=bookBean.getBookNumber();
							String govtCommVarianceQuery = "select transaction_gov_comm_rate,transaction_date from st_se_game_inv_detail where current_owner=? and current_owner_id=? and game_id=? and book_nbr=? and transaction_at=? order by transaction_date desc limit 1 ";
							PreparedStatement govtCommVariaceStmt = connection
									.prepareStatement(govtCommVarianceQuery);
							govtCommVariaceStmt.setString(1, "AGENT");
							govtCommVariaceStmt.setInt(2, userOrgID);
							govtCommVariaceStmt.setInt(3, gameId);
							govtCommVariaceStmt.setString(4, bookList
									.get(bookListSize));
							govtCommVariaceStmt.setString(5, "BO");
							rsCommVar = govtCommVariaceStmt.executeQuery();
							while (rsCommVar.next()) {
								govtCommRate = rsCommVar
										.getDouble("transaction_gov_comm_rate");
							}

							vatAmtForBook = CommonMethods.calculateVat(
									bookPrice, retSaleCommRate,
									prizepayoutRatio, govtCommRate, vat,
									govtCommRule, fixed_amt, tickets_in_scheme);
							// taxableSaleForbook=(((bookPrice*(100-(retSaleCommRate
							// + prizepayoutRatio
							// +govtCommRate)))/100)*100)/(100+vat);
							// taxableSaleForbook=vatAmtForBook/(gameBean.getVat()
							// * 0.01);
							taxableSaleForbook = CommonMethods.calTaxableSale(
									bookPrice, retSaleCommRate,
									prizepayoutRatio, govtCommRate, vat);

							bookSaleBean.setBookNumber(bookList
									.get(bookListSize));
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
									totMrpAmt = totMrpAmt + bookPrice;
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
									
									warehouseId = com.skilrock.lms.scratchService.dataMgmt.daoImpl.ScratchGameDataDaoImpl.getSingleInstance().getWarehouseNbrForBook(connection, gameId, bknbr);

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
									gameInvDetailStmt.setTimestamp(7, currentDate);
									gameInvDetailStmt.setString(8, "AGENT");
									gameInvDetailStmt.setString(9, newBookStatus);
									//gameInvDetailStmt.setDouble(9, retSaleCommRate);
									//gameInvDetailStmt.setDouble(10, govtCommFormSet);
									gameInvDetailStmt.setInt(10, orderInvoiceDCId);
									gameInvDetailStmt.setInt(11, warehouseId);
									//gameInvDetailStmt.setObject(11, purRate);
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

						agtOrdGameStmt.setInt(1, bookList.size());
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
								gameInvStatusStmt.setString(6, String.valueOf(DCId));
								gameInvStatusStmt.setInt(7, gameId);
								gameInvStatusStmt.setString(8, bookList.get(j));
								gameInvStatusStmt.execute();

							}
						}
					}
				}
				agtOrderStmt.setString(1, "AUTO_PROCESSED");
				agtOrderStmt.setInt(2, orderId);
				agtOrderStmt.executeUpdate();

				agtReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getAGENTLatestReceiptNb());
				agtReceiptNoGenStmt.setString(1, "INVOICE");
				agtReceiptNoGenStmt.setInt(2, userOrgID);
				ResultSet recieptRs = agtReceiptNoGenStmt.executeQuery();
				String lastRecieptNoGenerated = null;
				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = null;
				autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"INVOICE", lastRecieptNoGenerated, "AGENT", userOrgID);

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
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
						"SALE", creditAmt, connection);*/

//				connection.commit();
				// session.setAttribute("DEL_CHALLAN_ID", DCId);
//				if (invoiceId > -1) {
//					GraphReportHelper graphReportHelper = new GraphReportHelper();
//					graphReportHelper.createTextReportAgent(invoiceId,
//							rootPath, userOrgID, loggedInUserOrgName);
//				}
				// return DCId;
			} catch (SQLException se) {
				try {
					connection.rollback();

				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException("Error During Rollback", e);
				}
				se.printStackTrace();
				throw new LMSException(se);
			}

		}
		return String.valueOf(DCId) + "Nxt" + String.valueOf(invoiceId);
	}

	public int dispatchOrderDirectSale(Map<Integer, List<String>> gameBookMap,
			int agtId, int userOrgID, int retailerName, String rootPath,
			String loggedInUserOrgName, String newBookStatus)
			throws LMSException {
		Connection connection = null;
		String returnValue = null;

		int DCId = -1;
		int invoiceId = -1;
		if (gameBookMap != null) {
			try {
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				
				returnValue = dispatchOrderDirectSale(gameBookMap,
						agtId, userOrgID, retailerName, rootPath,
						loggedInUserOrgName, newBookStatus, connection);

				connection.commit();
				
				DCId = Integer.parseInt(returnValue.split("Nxt")[0]);
				invoiceId = Integer.parseInt(returnValue.split("Nxt")[1]);
				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportAgent(invoiceId,
							rootPath, userOrgID, loggedInUserOrgName);
				}
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
					throw new LMSException(se);
				}
			}
		}
		return DCId;
	}

	public List<String> getGames() throws LMSException {
		List<String> gameList = new ArrayList<String>();
		 
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			ResultSet rsGame = stmt
					.executeQuery("select game_name,game_nbr from st_se_game_master where game_status='OPEN'");
			while (rsGame.next()) {
				gameList.add(rsGame.getString("game_name") + "-"
						+ rsGame.getString("game_nbr"));
			}
			return gameList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}
	}

	public String getPackNbrForBook(Connection connection, int gameId,
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

	public List<String> getRetailers(int parent_id) throws LMSException {
		List<String> retailerList = new ArrayList<String>();
		 
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			ResultSet rsRet = stmt
					.executeQuery("select name from st_lms_organization_master where organization_type='RETAILER' and parent_id="
							+ parent_id);
			// ResultSet rsGame=stmt.executeQuery("select game_name from
			// st_se_game_master where game_status='OPEN'");
			while (rsRet.next()) {
				retailerList.add(rsRet.getString("name"));
			}
			return retailerList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sql exception", e);
		}
	}

	public boolean getSalePrice(Map<Integer, List<String>> dispatchMap,
			int organizationId) throws LMSException {

		// boolean isDispatch=false;
		 
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		Statement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		double totalGamebooksAmount = 0.0;
		try {

			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select organization_id,available_credit from st_lms_organization_master where organization_id="
							+ organizationId);
			double availableCredit = 0.0;
			if (rs.next()) {
				availableCredit = rs.getDouble("available_credit");
			}

			Set<Integer> keyset = dispatchMap.keySet();
			for (Integer gameNbr : keyset) {
				int nbrOfbooks = dispatchMap.get(gameNbr).size();
				stmt = con.createStatement();
				stmt1 = con.createStatement();
				rs1 = stmt1
						.executeQuery("select sale_comm_variance from st_se_agent_retailer_sale_pwt_comm_variance where game_id=(select game_id from st_se_game_master where game_nbr="
								+ gameNbr + ") and retailer_org_id=" + organizationId);
				double sale_comm_variance = 0.0;
				while (rs1.next()) {
					sale_comm_variance = rs1.getDouble("sale_comm_variance");
				}
				rs = stmt
						.executeQuery("select ticket_price,nbr_of_tickets_per_book,retailer_sale_comm_rate from st_se_game_master  where game_id=(select game_id from st_se_game_master where game_nbr="
								+ gameNbr + ")");
				// System.out.println("11111111112222222222 " + "select
				// aa.sale_comm_variance,bb.pwt_scrap,cc.ticket_price,cc.nbr_of_tickets_per_book,cc.retailer_sale_comm_rate
				// from st_se_agent_retailer_sale_pwt_comm_variance
				// aa,st_lms_organization_master bb ,st_se_game_master cc where
				// aa.game_id=(select game_id from st_se_game_master where
				// game_nbr="+gameNbr+") and aa.ret_org_id=(select
				// organization_id from st_lms_organization_master where
				// name='"+organizationName+"') and bb.organization_id=(select
				// organization_id from st_lms_organization_master where
				// name='"+organizationName+"') and cc.game_id=(select game_id
				// from st_se_game_master where game_nbr="+gameNbr+")");
				if (rs.next()) {
					double netCommRate = rs
							.getDouble("retailer_sale_comm_rate")
							+ sale_comm_variance;
					double bookPrice = rs.getDouble("ticket_price")
							* rs.getInt("nbr_of_tickets_per_book");
					double netSaleAmountAllBooks = (bookPrice - bookPrice
							* 0.01 * netCommRate)
							* nbrOfbooks;
					totalGamebooksAmount = totalGamebooksAmount
							+ netSaleAmountAllBooks;
				}

			}

			if (totalGamebooksAmount <= availableCredit) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}
	}

	public boolean verifyBook(int agtOrgId, String bookToVerify, int gameId,
			int gameNbr, Connection connection) throws LMSException {

		// System.out.println("Agent Org Id::" + agtOrgId);
		boolean isValid = false;
		boolean ownercheck = false;
		boolean pwtCheck = false;
		boolean pwtCheckTemp = false;

		try {
			PreparedStatement pstmt = null;
			ResultSet resultSet = null;
			String query = null;
			// get the game id from game number
			query = QueryManager.getST1AgentVerifyQuery()
					+ "  and (book_status!='MISSING' and book_status!='CLAIMED')";
			pstmt = connection.prepareStatement(query);

			pstmt.setInt(1, gameId);
			pstmt.setString(2, bookToVerify);

			resultSet = pstmt.executeQuery();

			String currOwner = null;
			int currOwnerId = -1;
			while (resultSet.next()) {
				currOwner = resultSet.getString(TableConstants.SGIS_CURR_OWNER);
				currOwnerId = resultSet
						.getInt(TableConstants.SGIS_CURR_OWNER_ID);
				if (currOwner != null && currOwner.equals("AGENT")
						&& currOwnerId == agtOrgId) {
					ownercheck = true;
				}
			}

			// ckeck for ticket of this book in main ticket table
			pwtCheck = verifyBookValidityWithPWT(gameId, bookToVerify,
					connection, gameNbr);
			pwtCheckTemp = verifyBookValidityWithPWTTempTable(gameId,
					bookToVerify, connection);
			if (ownercheck == true && pwtCheck == true && pwtCheckTemp == true) {
				isValid = true;
				System.out.println(isValid + "********" + bookToVerify);
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		}

		return isValid;
	}

	// direct dispatch new

	private boolean verifyBooks(int gameId, String books,
			List<BookBean> bookList, int agtOrgId, Connection connection)
			throws LMSException {

		// Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isBookValid = true;
		try {
			statement = connection.createStatement();
			String query = QueryManager.getST1AgentBookInvVerifyQuery();
			query = query + gameId + " and book_nbr in ( " + books + " )";

			resultSet = statement.executeQuery(query);

			String currOwner = null;
			String bookNbr = null;
			BookBean bean = null;
			int currOwnerId = -1;
			while (resultSet.next()) {
				currOwner = resultSet.getString(TableConstants.SGIS_CURR_OWNER);
				currOwnerId = resultSet
						.getInt(TableConstants.SGIS_CURR_OWNER_ID);
				bookNbr = resultSet.getString(TableConstants.SGIS_BOOK_NBR);

				for (int i = 0; i < bookList.size(); i++) {
					bean = bookList.get(i);
					if (bookNbr.equals(bean.getBookNumber())) {

						if (currOwner != null && currOwner.equals("AGENT")
								&& currOwnerId == agtOrgId) {
							bean.setValid(true);
							bean.setStatus(null);
						} else {
							isBookValid = false;
							bean.setValid(false);
							bean.setStatus("Wrong Book Number");
						}
					}
					// isBookValid = isBookValid && bean.getIsValid();
				}

			}

			for (int i = 0; i < bookList.size(); i++) {
				bean = bookList.get(i);
				isBookValid = isBookValid && bean.getIsValid();
			}

			if (resultSet != null && !resultSet.previous()) {
				isBookValid = false;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				/*
				 * if (connection != null) { connection.close(); }
				 */
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		System.out.println("isBookValid::" + isBookValid);
		return isBookValid;
	}

	// Check Book Verification For PWT Tickets in main ticket table.
	public boolean verifyBookValidityWithPWT(int game_id, String bknbr,
			Connection conn, int gameNbr) {
		boolean bookPwtFlag = true;
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
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookPwtFlag;
	}

	// Check Book Verification For PWT Tickets in temp ticket table.
	public boolean verifyBookValidityWithPWTTempTable(int game_id,
			String bknbr, Connection conn) {

		boolean bookPwtFlag = true;
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
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookPwtFlag;
	}

}