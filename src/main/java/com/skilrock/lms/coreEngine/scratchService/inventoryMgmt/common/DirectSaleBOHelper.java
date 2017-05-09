package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class DirectSaleBOHelper {
private static Log logger = LogFactory.getLog(DirectSaleBOHelper.class);
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
			int userId, int userOrgID, int orgId, String rootPath,
			String loggedInUserOrgName, String newBookStatus, Connection connection)
			throws LMSException {

		int DCId = -1;
		int invoiceId = -1;
		if (gameBookMap != null) {
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

			ResultSet rsMaster = null;
			String orderInvoicesQuery = null;

			String agtAgentQuery = null;
			String agtOrdGamesQuery = null;
			String agtReceiptMappingQuery = null;
			String agtOrderQuery = null;

			String invQuery = null;
			String gameInvDetailQuery = null;
			String getBookfromPackQuery = null;
			String isGovtCommChangedQuery = null;
			String orderQuery = null;

			List<Integer> trnIdList = new ArrayList<Integer>();
			int masterTrnId = -1;
			try {

				String boMasterQuery = QueryManager
						.insertInBOTransactionMaster();
				PreparedStatement boMasterStmt = connection
						.prepareStatement(boMasterQuery);

				String LMSMasterQuery = QueryManager
						.insertInLMSTransactionMaster();
				PreparedStatement LMSMasterStmt = connection
						.prepareStatement(LMSMasterQuery);

				String boAgentQuery = QueryManager.getST1BOAgentQuery();
				PreparedStatement boAgentStmt = connection
						.prepareStatement(boAgentQuery);

				String boOrdGamesQuery = QueryManager
						.getST1BOOrdGamesUpdQuery();
				PreparedStatement boOrdGameStmt = connection
						.prepareStatement(boOrdGamesQuery);

				String gameInvStatusQuery = QueryManager
						.getST1BOUpdGameInvStatusQuery();

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

				gameInvDetailQuery = QueryManager.getST1InvDetailInsertQuery();
				gameInvDetailStmt = connection
						.prepareStatement(gameInvDetailQuery);

				orderInvoicesQuery = QueryManager.getST1OrderInInsertQuery();
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
				// double commAmt = 0.0;
				double netAmt = 0.0;
				double vatAmt = 0.0;
				double taxableSale = 0.0;
				// double retSaleCommRate=0.0;
				double creditAmt = 0.0;

				Statement stmt = connection.createStatement();
				ResultSet rsRet = stmt.executeQuery("select organization_id,user_id from st_lms_user_master where organization_id="+orgId);
				int agtUserId = -1;
				int agtOrgId = -1;
				while (rsRet.next()) {
					agtUserId = rsRet.getInt("user_id");
					agtOrgId = rsRet.getInt("organization_id");

				}

				int gameId = -1;
				int orderId = -1;
				// get order query

				// insert in receipt master for invoice
				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "BO");
				agtReceiptStmt.executeUpdate();

				ResultSet rs = agtReceiptStmt.getGeneratedKeys();
				
				while (rs.next()) {
					invoiceId = rs.getInt(1);
				}
				// insert in receipt master for delivery challan
				agtReceiptStmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptStmt.setString(1, "BO");
				agtReceiptStmt.executeUpdate();
				ResultSet DCrs = agtReceiptStmt.getGeneratedKeys();
				DCId = -1;
				while (DCrs.next()) {
					DCId = DCrs.getInt(1);
				}

				// set parameters for insert into order table
				orderQuery = QueryManager.getST1InsertBOOrderQuery();
				orderPstmt = connection.prepareStatement(orderQuery);
				orderPstmt.setInt(1, agtUserId);
				orderPstmt.setInt(2, agtOrgId);
				orderPstmt.setDate(3, new java.sql.Date(new Date().getTime()));
				orderPstmt.setString(4, "AUTO_PROCESSED");
				orderPstmt.setString(5, "Y");
				orderPstmt.execute();
				ResultSet resultSet = orderPstmt.getGeneratedKeys();
				while (resultSet.next()) {
					orderId = resultSet.getInt(1);
				}
				logger.info("OrderId::" + orderId);

				// String newBookStatus = "ACTIVE";

				// now we have to iterate for all games to enter ordered games
				// set parameters for insert into ordered games table
				// get ordered game query
				String gameQuery = QueryManager
						.getST1AutoInsertBOOrderedGamesQuery();
				PreparedStatement gamePstmt = connection
						.prepareStatement(gameQuery);
				Set<Integer> keySet = gameBookMap.keySet();
				for (int gameNbr : keySet) {

					double bookPrice = 0.0;
					double agt_sale_comm_rate = 0.0;
					double agtGameCommVariance = 0.0;
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
					if (rsGame.next()) {
						gameId = rsGame.getInt("game_id");
						bookPrice = rsGame.getDouble("ticket_price")
								* rsGame.getInt("nbr_of_tickets_per_book");
						noOfTktPerBooks = rsGame
								.getInt("nbr_of_tickets_per_book");
						agt_sale_comm_rate = rsGame
								.getDouble("agent_sale_comm_rate");
						prizepayoutRatio = rsGame
								.getDouble("prize_payout_ratio");
						vat = rsGame.getDouble("vat_amt");
						fixed_amt = rsGame.getDouble("fixed_amt");
						tickets_in_scheme = rsGame.getLong("tickets_in_scheme");
						govtCommRule = rsGame.getString("govt_comm_type");
						govt_comm_rate = rsGame.getDouble("govt_comm_rate");
					}

					// Insert the ordered game details into
					List<String> bookList = gameBookMap.get(gameNbr);
					gamePstmt.setInt(1, orderId);
					gamePstmt.setInt(2, gameId);
					gamePstmt.setInt(3, bookList.size());
					gamePstmt.setInt(4, bookList.size());
					gamePstmt.execute();

					// update delivered book list
					boOrdGameStmt.setInt(1, bookList.size());
					boOrdGameStmt.setInt(2, orderId);
					boOrdGameStmt.setInt(3, gameId);
					boOrdGameStmt.execute();

					// Fetch Commission variance of agent
					rsGame = stmt
							.executeQuery("select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
									+ agtOrgId + " and game_id=" + gameId);
					System.out
							.println("comm var query :: "
									+ "select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
									+ agtOrgId + " and game_id=" + gameId);
					if (rsGame.next()) {
						agtGameCommVariance = rsGame
								.getDouble("sale_comm_variance");
					}
					logger.info("agtGameCommVariance=="
							+ agtGameCommVariance + "  agt_sale_comm_rate=="
							+ agt_sale_comm_rate);

					// insert in transaction master
					LMSMasterStmt.setString(1, "BO");
					LMSMasterStmt.executeUpdate();
					rsMaster = null;
					rsMaster = LMSMasterStmt.getGeneratedKeys();

					while (rsMaster.next()) {
						masterTrnId = rsMaster.getInt(1);
						trnIdList.add(masterTrnId);
					}

					// insert in BO transaction master
					boMasterStmt.setInt(1, masterTrnId);
					boMasterStmt.setInt(2, userId);
					boMasterStmt.setInt(3, userOrgID);
					boMasterStmt.setString(4, "AGENT");
					boMasterStmt.setInt(5, agtOrgId);
					boMasterStmt.setTimestamp(6, currentDate);
					boMasterStmt.setString(7, "SALE");
					boMasterStmt.execute();

					// Insert into st_bo_agt_transaction_master
					boAgentStmt.setInt(1, masterTrnId);
					boAgentStmt.setInt(2, bookList.size());
					boAgentStmt.setInt(3, gameId);
					boAgentStmt.setInt(4, agtOrgId);
					mrpAmt = bookList.size() * bookPrice;
					boAgentStmt.setDouble(5, mrpAmt);

					double govtCommAmt = mrpAmt * govt_comm_rate * .01;
					double agtSaleCommRate = agt_sale_comm_rate
							+ agtGameCommVariance;
					double agtcommAmt = mrpAmt * agtSaleCommRate * 0.01;
					logger.info("agtcommAmt===" + agtcommAmt);
					netAmt = mrpAmt - agtcommAmt;
					creditAmt += netAmt;

					// added by Arun to calculate vat amount
					vatAmt = CommonMethods.calculateVat(mrpAmt,
							agtSaleCommRate, prizepayoutRatio, govt_comm_rate,
							vat, govtCommRule, fixed_amt, tickets_in_scheme);
					logger.info("mrp :" + mrpAmt + "ppr :"
							+ prizepayoutRatio + "gov comm :" + govt_comm_rate
							+ "agt net comm :" + agtSaleCommRate);

					/*
					 * taxableSale = (((mrpAmt * (100 - (agtSaleCommRate +
					 * prizepayoutRatio + govt_comm_rate))) / 100) * 100) / (100 +
					 * vat);
					 */taxableSale = CommonMethods.calTaxableSale(mrpAmt,
							agtSaleCommRate, prizepayoutRatio, govt_comm_rate,
							vat);

					logger.info("vat amount is :  " + vatAmt
							+ "\ntaxable sale is  : " + taxableSale);

					boAgentStmt.setDouble(6, agtcommAmt);
					boAgentStmt.setDouble(7, netAmt);
					boAgentStmt.setString(8, "SALE");

					boAgentStmt.setDouble(9, vatAmt);
					boAgentStmt.setDouble(10, taxableSale);
					boAgentStmt.setDouble(11, govtCommAmt);
					boAgentStmt.execute();

					int orderInvoiceDCId = 0;
					orderInvoicesPrtSt.setInt(1, orderId);
					orderInvoicesPrtSt.setInt(2, invoiceId);
					orderInvoicesPrtSt.setInt(3, agtOrgId);
					orderInvoicesPrtSt.setInt(4, gameId);
					orderInvoicesPrtSt.setString(5, "AUTO_PROCESSED");
					orderInvoicesPrtSt.setInt(6, bookList.size());
					orderInvoicesPrtSt.setInt(7, DCId);
					orderInvoicesPrtSt.execute();
					rs = orderInvoicesPrtSt.getGeneratedKeys();
					while (rs.next()) {
						orderInvoiceDCId = rs.getInt(1);
					}

					if (bookList != null) {
						invQuery = gameInvStatusQuery + " and book_nbr = ? ";
						gameInvStatusStmt = connection
								.prepareStatement(invQuery);
						String bookNbr = null;
						String packNbr = null;
						for (int j = 0; j < bookList.size(); j++) {
							bookNbr = bookList.get(j);
							gameInvStatusStmt.setString(1, "AGENT");
							gameInvStatusStmt.setInt(2, agtOrgId);
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

							gameInvDetailStmt.setInt(1, masterTrnId);
							gameInvDetailStmt.setInt(2, gameId);
							gameInvDetailStmt.setString(3, packNbr);
							gameInvDetailStmt.setString(4, bookNbr);
							gameInvDetailStmt.setString(5, "AGENT");
							gameInvDetailStmt.setInt(6, agtOrgId);
							gameInvDetailStmt.setTimestamp(7, currentDate);
							gameInvDetailStmt.setString(8, "BO");
							gameInvDetailStmt.setString(9, newBookStatus);
							//gameInvDetailStmt.setDouble(9, agtSaleCommRate);
							//gameInvDetailStmt.setDouble(10, govt_comm_rate);
							gameInvDetailStmt.setInt(10, orderInvoiceDCId);
							gameInvDetailStmt.setInt(11, fetchWarehouseId(bookNbr, connection));
							gameInvDetailStmt.execute();

							// insert into detail history table Added by arun
							detHistoryInsPstmt.setInt(1, gameId);
							detHistoryInsPstmt.setString(2, bookNbr);
							detHistoryInsPstmt.setString(3, "AGENT");
							detHistoryInsPstmt.setInt(4, agtOrgId);
							detHistoryInsPstmt.setTimestamp(5, currentDate);
							detHistoryInsPstmt.setInt(6, userOrgID);
							detHistoryInsPstmt.setInt(7, userId);
							detHistoryInsPstmt.setInt(8, noOfTktPerBooks);
							if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
								detHistoryInsPstmt.setInt(9, noOfTktPerBooks);
							} else {
								detHistoryInsPstmt.setInt(9, 0);
							}
							detHistoryInsPstmt.setInt(10, 0);
							detHistoryInsPstmt.setString(11, newBookStatus);
							// logger.info("detHistoryInsPstmt ==
							// "+detHistoryInsPstmt);
							detHistoryInsPstmt.execute();
							// ---------------------

						}
					}

					System.out
							.println("Total aaproved and total dispatched are equal----PROCESSED");

				}

				String lastRecieptNoGenerated = null, autoGeneRecieptNo = null;

				PreparedStatement boReceiptNoGenStmt = connection
						.prepareStatement(QueryManager.getBOLatestReceiptNb());
				boReceiptNoGenStmt.setString(1, "INVOICE");
				ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}
				autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("INVOICE",
						lastRecieptNoGenerated, "BO");

				String lastDCNoGenerated = null, autoGeneDCNo = null;
				boReceiptNoGenStmt = connection.prepareStatement(QueryManager
						.getBOLatestReceiptNb());
				boReceiptNoGenStmt.setString(1, "DLCHALLAN");
				ResultSet DCRs = boReceiptNoGenStmt.executeQuery();

				while (DCRs.next()) {
					lastDCNoGenerated = DCRs.getString("generated_id");
				}
				autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DLCHALLAN",
						lastDCNoGenerated, "BO");
				logger.info("lastDCNoGenerated " + lastDCNoGenerated
						+ "autoGeneDCNo " + autoGeneDCNo);

				// insert into BO receipt table
				PreparedStatement boReceiptStmt = connection
						.prepareStatement(QueryManager.insertInBOReceipts());
				boReceiptStmt.setInt(1, invoiceId);
				boReceiptStmt.setString(2, "INVOICE");
				boReceiptStmt.setInt(3, agtOrgId);
				boReceiptStmt.setString(4, "AGENT");
				boReceiptStmt.setString(5, autoGeneRecieptNo);
				boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());
				logger.info("bo invoice receipt = " + boReceiptStmt);
				boReceiptStmt.execute();

				// insert into BO receipt table for delivery challan
				boReceiptStmt = connection.prepareStatement(QueryManager
						.insertInBOReceipts());
				boReceiptStmt.setInt(1, DCId);
				boReceiptStmt.setString(2, "DLCHALLAN");
				boReceiptStmt.setInt(3, agtOrgId);
				boReceiptStmt.setString(4, "AGENT");
				boReceiptStmt.setString(5, autoGeneDCNo);
				boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());
				logger.info("bo DL receipt = " + boReceiptStmt);
				boReceiptStmt.execute();

				String boReceiptMappingQuery = QueryManager
						.insertBOReceiptTrnMapping();
				PreparedStatement boReceiptMappingStmt = connection
						.prepareStatement(boReceiptMappingQuery);
				// insert for receipt and transaction mapping table for Invoice
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

				// insert into invoice and delivery challan mapping table
				String insertInvoiceDCMapping = "insert into st_se_bo_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)";
				PreparedStatement boInvoiceDCMappingStmt = connection
						.prepareStatement(insertInvoiceDCMapping);
				boInvoiceDCMappingStmt.setInt(1, invoiceId);
				boInvoiceDCMappingStmt.setString(2, autoGeneRecieptNo);
				boInvoiceDCMappingStmt.setString(3, autoGeneDCNo);
				boInvoiceDCMappingStmt.executeUpdate();

				// for org credit updation
				logger.info("Org Id For Credit Update:" + agtOrgId + ":"
						+ creditAmt);
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", agtOrgId,
						0, "AGENT", 0, connection);
				
				if(!isValid)
					throw new LMSException();
				
				/*boolean isUpdateDone = OrgCreditUpdation
						.updateCreditLimitForAgent(agtOrgId, "SALE", creditAmt,
								connection);*/

//				connection.commit();
				// session.setAttribute("DEL_CHALLAN_ID", DCId);
//				if (invoiceId > -1) {
//					GraphReportHelper graphReportHelper = new GraphReportHelper();
//					graphReportHelper.createTextReportBO(invoiceId,
//							loggedInUserOrgName, userOrgID, rootPath);
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
	
	
private int fetchWarehouseId(String bookNbr, Connection conn) {
	Statement statement = null;
	ResultSet rs = null;
	int warehouseId=0;
	try {
		String query = "select warehouse_id from st_se_game_inv_status where book_nbr = '"+bookNbr+"';" ;
		statement = conn.createStatement();
		rs = statement.executeQuery(query);
		if(rs.next())
			warehouseId = rs.getInt("warehouse_id");
	} catch (Exception e) {
		logger.info("Exception occurred due to : "+e.getMessage());
	} finally{
		DBConnect.closeResource(statement, rs);
	}
		return warehouseId;
	}

/**
 * @changed agentOrgName to orgId during orgCode implementation
 * @param gameBookMap
 * @param userId
 * @param userOrgID
 * @param orgId
 * @param rootPath
 * @param loggedInUserOrgName
 * @param newBookStatus
 * @return
 * @throws LMSException
 */
	public int dispatchOrderDirectSale(Map<Integer, List<String>> gameBookMap,
			int userId, int userOrgID, int orgId, String rootPath,
			String loggedInUserOrgName, String newBookStatus)
			throws LMSException {
		int invoiceId = 0;
		Connection connection = null;
		String returnValue = null;
		int DCId = -1;
		if (gameBookMap != null) {			
			try {
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				returnValue = dispatchOrderDirectSale(gameBookMap, userId, userOrgID, orgId, rootPath,
						loggedInUserOrgName, newBookStatus, connection);
				connection.commit();
				DCId = Integer.parseInt(returnValue.split("Nxt")[0]);
				invoiceId = Integer.parseInt(returnValue.split("Nxt")[1]);
				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportBO(invoiceId,
							loggedInUserOrgName, userOrgID, rootPath);
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
						.executeQuery("select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
								+ organizationId
								+ " and game_id=(select game_id from st_se_game_master where game_nbr="
								+ gameNbr + ")");
				double sale_comm_variance = 0.0;
				while (rs1.next()) {
					sale_comm_variance = rs1.getDouble("sale_comm_variance");
				}
				rs = stmt
						.executeQuery("select ticket_price, nbr_of_tickets_per_book, agent_sale_comm_rate from st_se_game_master  where game_id=(select game_id from st_se_game_master where game_nbr="
								+ gameNbr + ")");
				// logger.info("11111111112222222222 " + "select
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
					double netCommRate = rs.getDouble("agent_sale_comm_rate")
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

	public boolean verifyBook(int boOrgId, String bookToVerify, int gameId,
			int gameNbr, Connection connection) throws LMSException {

		// logger.info("Agent Org Id::" + agtOrgId);
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
				if (currOwner != null && currOwner.equals("BO")
						&& currOwnerId == boOrgId) {
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
				logger.info(isValid + "********" + bookToVerify);
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

		logger.info("isBookValid::" + isBookValid);
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