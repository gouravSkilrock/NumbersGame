package com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.BookSaleBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.common.beans.InvoiceMethodValueMaster;
import com.skilrock.lms.coreEngine.scratchService.common.beans.OrderGameBookBeanMaster;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.DirectSaleBOHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class ScratchInvoiceDaoImpl {

	static Log logger = LogFactory.getLog(ScratchInvoiceDaoImpl.class);

	// Done
	public InvoiceMethodValueMaster fetchInvoiceMethod(int gameId, Connection con) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		InvoiceMethodValueMaster invoiceMethodValueMaster = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select scheme_type, scheme_value_type, scheme_value from st_se_game_master gm inner join st_se_invoicing_methods im on gm.invoice_scheme_id = im.invoice_method_id where game_id = " + gameId + " and game_status = 'OPEN';");
			if (rs.next()) {
				invoiceMethodValueMaster = new InvoiceMethodValueMaster();
				invoiceMethodValueMaster.setInvoiceMethodName(rs.getString("scheme_type"));
				invoiceMethodValueMaster.setValue(rs.getString("scheme_value"));
				invoiceMethodValueMaster.setValueType(rs.getString("scheme_value_type"));
			} else {
				throw new ScratchException(ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_CODE, ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return invoiceMethodValueMaster;
	}

	// Done
	public InvoiceMethodValueMaster fetchInvoiceMethod(int gameId, int orgId, Connection con) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		InvoiceMethodValueMaster invoiceMethodValueMaster = null;
		try {
			stmt = con.createStatement();
//			rs = stmt.executeQuery("select SQL_CACHE scheme_type, scheme_value_type, scheme_value from st_se_game_master gm inner join st_se_org_game_invoice_methods gim on gim.game_id = gm.game_id inner join st_se_invoicing_methods im on im.invoice_method_id = gim.invoice_method_id where gm.game_id = " + gameId + " and game_status = 'OPEN' and gim.organization_id = " + orgId + ";");
			rs = stmt.executeQuery("select SQL_CACHE scheme_type, scheme_value_type, invoice_method_value from st_se_game_master gm inner join st_se_org_game_invoice_methods gim on gim.game_id = gm.game_id inner join st_se_invoicing_methods im on im.invoice_method_id = gim.invoice_method_id where gm.game_id = " + gameId + " and game_status = 'OPEN' and gim.organization_id = " + orgId + ";");
			if (rs.next()) {
				invoiceMethodValueMaster = new InvoiceMethodValueMaster();
				invoiceMethodValueMaster.setInvoiceMethodName(rs.getString("scheme_type"));
				invoiceMethodValueMaster.setValue(rs.getString("invoice_method_value"));
				invoiceMethodValueMaster.setValueType(rs.getString("scheme_value_type"));
			} else {
				throw new ScratchException(ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_CODE, ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return invoiceMethodValueMaster;
	}

	// Done
	public void generateScratchInvoiceAndDeductBalance(Connection con) throws ScratchException {
		Map<Integer, Map<Integer, OrderGameBookBeanMaster>> orderGameOrgBookMap = null;
		try {
			orderGameOrgBookMap = fetchBookForInvoice(con);

			for (Map.Entry<Integer, Map<Integer, OrderGameBookBeanMaster>> gameBookEntry : orderGameOrgBookMap.entrySet()) {
				checkAndUpdateForInvoice(gameBookEntry.getKey(), gameBookEntry.getValue(), con);
				if(!gameBookEntry.getValue().isEmpty())
					generateInvoice(gameBookEntry.getKey(), gameBookEntry.getValue(), con);
			}
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public Map<Integer, Map<Integer, OrderGameBookBeanMaster>> fetchBookForInvoice(Connection con) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		List<String> bookList = null;
		OrderGameBookBeanMaster orderGameBookBeanMaster = null;
		Map<Integer, OrderGameBookBeanMaster> gameOrgBookMap = null;
		Map<Integer, Map<Integer, OrderGameBookBeanMaster>> orderGameOrgBookMap = new HashMap<Integer, Map<Integer, OrderGameBookBeanMaster>>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select order_id, game_id, pack_nbr, book_nbr, current_owner_id, book_receive_reg_date_ret, book_activation_date_ret, first_ticket_claim_date, total_low_win_tier_tickets, total_low_win_tier_tickets_claimed, book_status from st_se_game_inv_status where current_owner = 'RETAILER' and ret_invoice_id is null and agent_invoice_id is null;");
			while (rs.next()) {
				if (orderGameOrgBookMap.containsKey(rs.getInt("current_owner_id"))) {
					gameOrgBookMap = orderGameOrgBookMap.get(rs.getInt("current_owner_id"));
					if (gameOrgBookMap.containsKey(rs.getInt("game_id"))) {
						bookList = orderGameOrgBookMap.get(rs.getInt("current_owner_id")).get(rs.getInt("game_id")).getBookList();
						bookList.add(rs.getString("book_nbr"));
						orderGameOrgBookMap.get(rs.getInt("current_owner_id")).get(rs.getInt("game_id")).setBookList(bookList);
					} else {
						gameOrgBookMap = new HashMap<Integer, OrderGameBookBeanMaster>();
						orderGameBookBeanMaster = new OrderGameBookBeanMaster();
						bookList = new ArrayList<String>();

						bookList.add(rs.getString("book_nbr"));
//						orderGameBookBeanMaster.setOrderId(rs.getInt("order_id"));
						orderGameBookBeanMaster.setBookList(bookList);

						orderGameOrgBookMap.get(rs.getInt("current_owner_id")).put(rs.getInt("game_id"), orderGameBookBeanMaster);
					}
				} else {
					gameOrgBookMap = new HashMap<Integer, OrderGameBookBeanMaster>();
					orderGameBookBeanMaster = new OrderGameBookBeanMaster();
					bookList = new ArrayList<String>();

					bookList.add(rs.getString("book_nbr"));
//					orderGameBookBeanMaster.setOrderId(rs.getInt("order_id"));
					orderGameBookBeanMaster.setBookList(bookList);

					gameOrgBookMap.put(rs.getInt("game_id"), orderGameBookBeanMaster);

					orderGameOrgBookMap.put(rs.getInt("current_owner_id"), gameOrgBookMap);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return orderGameOrgBookMap;
	}

	// Done
	private void generateInvoice(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, Connection con) throws ScratchException {
		try {
			con.setAutoCommit(false);
			generateInvoiceForAgent(orgId, gameBookMap, null, con);
			generateInvoiceForRetailer(orgId, gameBookMap, null, con);
			con.commit();
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public String getInvoiceReceiptFromInvoiceId(String userType, int invoiceId, Connection connection) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;

		String invoiceReceipt = null;
		String query = null;
		try {
			String tableName = "BO".equals(userType) ? "st_lms_bo_receipts" : "st_lms_agent_receipts";
			stmt = connection.createStatement();
			query = "SELECT generated_id FROM "+tableName+" WHERE receipt_id="+invoiceId+";";
			logger.info("getInvoiceReceiptFromInvoiceId - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next())
				invoiceReceipt = rs.getString("generated_id");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return invoiceReceipt;
	}

	public void checkAndUpdateForInvoice(Map<Integer, OrderGameBookBeanMaster> gameBookMap, Connection connection) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			int gameId = 0;
			List<String> bookList = null;

			stmt = connection.createStatement();
			for(Map.Entry<Integer, OrderGameBookBeanMaster> entry : gameBookMap.entrySet()) {
				gameId = entry.getKey();
				bookList = entry.getValue().getBookList();
				query = "SELECT book_nbr FROM st_se_game_inv_status WHERE book_nbr IN ("+bookList.toString().replace("[", "'").replace(",", "','").replace("]", "'").replace(" ", "")+") AND agent_invoice_id<>'NULL' AND ret_invoice_id<>'NULL' AND game_id="+gameId+";";
				logger.info("checkAndUpdateForInvoice - "+query);
				rs = stmt.executeQuery(query);
				while(rs.next())
					bookList.remove(rs.getString("book_nbr"));

				if(bookList.size() == 0)
					gameBookMap.remove(gameId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	// Done
	public void checkAndUpdateForInvoice(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, Connection con) throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder bookNbr = new StringBuilder();
		InvoiceMethodValueMaster invoiceMethodValueMaster = null;
		long dayCount = 0;
		List<Integer> gameIdToRemove = new ArrayList<Integer>();
		try {
			for (Map.Entry<Integer, OrderGameBookBeanMaster> bookMap : gameBookMap.entrySet()) {
				bookNbr.setLength(0);
				for (String str : bookMap.getValue().getBookList()) {
					bookNbr.append("'");
					bookNbr.append(str);
					bookNbr.append("',");
				}

				if (bookMap.getValue().getBookList().size() > 0) {
					bookNbr.setLength(bookNbr.length() - 1);
				}

				invoiceMethodValueMaster = fetchInvoiceMethod(bookMap.getKey(), orgId, con);
				
				stmt = con.createStatement();
				rs = stmt.executeQuery("select game_id, book_nbr, current_owner_id, cur_rem_tickets, active_tickets_upto, book_receive_reg_date_ret, book_activation_date_ret, first_ticket_claim_date, total_low_win_tier_tickets, total_low_win_tier_tickets_claimed, book_status from st_se_game_inv_status where current_owner = 'RETAILER' and game_id = " + bookMap.getKey() + " and book_nbr in (" + bookNbr.toString() + ") and ret_invoice_id is null and agent_invoice_id is null;");
				while (rs.next()) {
					if ("AFTER_BOOK_RECEIVE_DAYS".equals(invoiceMethodValueMaster.getInvoiceMethodName())) {
						if (!"FIXED".equals(invoiceMethodValueMaster.getValueType())) {
							throw new ScratchException(ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_CODE, ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_MESSAGE);
						} else {
							dayCount = (Calendar.getInstance().getTime().getTime() / (24 * 60 * 60 * 1000))- new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("book_receive_reg_date_ret")).getTime() / (24 * 60 * 60 * 1000);
							if (dayCount >= Long.parseLong(invoiceMethodValueMaster.getValue())) {
								// Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						}
					} else if ("ON_SALES_RET".equals(invoiceMethodValueMaster.getInvoiceMethodName())) {
						if ("BOOLEAN".equals(invoiceMethodValueMaster.getValueType())) {
							throw new ScratchException(ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_CODE, ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_MESSAGE);
						} else if ("FIXED".equals(invoiceMethodValueMaster.getValueType())) {
							if (rs.getInt("active_tickets_upto") >= Integer.parseInt(invoiceMethodValueMaster.getValue())) {
								// Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						} else if ("PERCENTAGE".equals(invoiceMethodValueMaster.getValueType())) {
							if (((rs.getInt("active_tickets_upto") * 100) / (rs.getInt("active_tickets_upto") + rs.getInt("cur_rem_tickets"))) >= Integer.parseInt(invoiceMethodValueMaster.getValue())) {
								// Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						}
					} else if ("AFTER_DAYS_ACTIVATION".equals(invoiceMethodValueMaster.getInvoiceMethodName())) {
						if (!"FIXED".equals(invoiceMethodValueMaster.getValueType())) {
							throw new ScratchException(ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_CODE, ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_MESSAGE);
						} else {
							dayCount = (Calendar.getInstance().getTime().getTime() / (24 * 60 * 60 * 1000)) - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("book_activation_date_ret")).getTime() / (24 * 60 * 60 * 1000);
							if (dayCount >= Long.parseLong(invoiceMethodValueMaster.getValue())) {
								//Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						}
					} else if ("ON_WIN_CLAIM_RET".equals(invoiceMethodValueMaster.getInvoiceMethodName())) {
						if ("BOOLEAN".equals(invoiceMethodValueMaster.getValueType())) {
							throw new ScratchException(ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_CODE, ScratchErrors.SCRATCH_INVALID_INVOICE_MENTHOD_VALUE_TYPE_ERROR_MESSAGE);
						} else if ("FIXED".equals(invoiceMethodValueMaster.getValueType())) {
							if (rs.getInt("total_low_win_tier_tickets_claimed") >= Integer.parseInt(invoiceMethodValueMaster.getValue())) {
								//Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						} else if ("PERCENTAGE".equals(invoiceMethodValueMaster.getValueType())) {
							if (((rs.getInt("total_low_win_tier_tickets_claimed") * 100) / rs.getInt("total_low_win_tier_tickets")) >= Integer.parseInt(invoiceMethodValueMaster.getValue())) {
								//Generate invoice
							} else {
								bookMap.getValue().getBookList().remove(rs.getString("book_nbr"));
							}
						}
					}
				}
				if(bookMap.getValue().getBookList().size() == 0) {
					gameIdToRemove.add(bookMap.getKey());
				}
			}
			for(Integer gameid : gameIdToRemove) 
				gameBookMap.remove(gameid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	// Done
	private List<Integer> fetchParentUserAndOrgId(int orgId, Connection con) throws ScratchException {
		List<Integer> list = null;

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select parent.user_id, parent.organization_id from st_lms_user_master child inner join st_lms_user_master parent on child.parent_user_id = parent.user_id where child.organization_id = "
							+ orgId + " and parent.isrolehead = 'Y'");
			if(rs.next()) {
				list = new ArrayList<Integer>();
				list.add(rs.getInt("user_id"));
				list.add(rs.getInt("organization_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return list;
	}

	public int generateInvoiceForAgent(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, String dl, Connection connection) throws ScratchException {
		int invoiceId = 0;

		String lastRecieptNoGenerated = null, autoGeneRecieptNo = null;

		PreparedStatement pStmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		PreparedStatement pStmtInvDetail = null;

		double bookPrice = 0.0;
		double agt_sale_comm_rate = 0.0;
		double agtGameCommVariance = 0.0;
		double prizepayoutRatio = 0.0;
		double vat = 0.0;
		double fixed_amt = 0.0;
		long tickets_in_scheme = 0l;
		String govtCommRule = null;
		double govt_comm_rate = 0.0;
		int masterTrnId = -1;

		double mrpAmt = 0.0;
		double netAmt = 0.0;
		double vatAmt = 0.0;
		double taxableSale = 0.0;
		double creditAmt = 0.0;
		double govtCommAmt = 0.0;
		double agtSaleCommRate = 0.0;
		double agtcommAmt = 0.0;
		String packNbr = null;

		List<Integer> trnIdList = new ArrayList<Integer>();
		List<Integer> list = null;
		StringBuilder bookNbrList = new StringBuilder();
		
		int gameId = 0;
//		int orderId = 0;
		List<String> bookList = null;
		try {
			pStmt = connection.prepareStatement(QueryManager.insertInReceiptMaster());
			pStmt.setString(1, "BO");
			pStmt.executeUpdate();

			rs = pStmt.getGeneratedKeys();
			while (rs.next()) {
				invoiceId = rs.getInt(1);
			}

			java.sql.Timestamp currentDate = new java.sql.Timestamp(new Date().getTime());
			stmt = connection.createStatement();
			for(Map.Entry<Integer, OrderGameBookBeanMaster> gameMap : gameBookMap.entrySet()) {
				gameId = gameMap.getKey();
//				orderId = gameMap.getValue().getOrderId();
				bookList = gameMap.getValue().getBookList();
				rs = stmt.executeQuery("select * from st_se_game_master where game_id = " + gameId);
				if (rs.next()) {
					list = fetchParentUserAndOrgId(orgId, connection);
	
					bookPrice = rs.getDouble("ticket_price") * rs.getInt("nbr_of_tickets_per_book");
					agt_sale_comm_rate = rs.getDouble("agent_sale_comm_rate");
					prizepayoutRatio = rs.getDouble("prize_payout_ratio");
					vat = rs.getDouble("vat_amt");
					fixed_amt = rs.getDouble("fixed_amt");
					tickets_in_scheme = rs.getLong("tickets_in_scheme");
					govtCommRule = rs.getString("govt_comm_type");
					govt_comm_rate = rs.getDouble("govt_comm_rate");
					
					// Fetch Commission variance of agent
					rs = stmt.executeQuery("select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id=" + list.get(1) + " and game_id=" + gameId);
					logger.info("comm var query :: " + "select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id=" + list.get(1) + " and game_id=" + gameId);
					if (rs.next()) {
						agtGameCommVariance = rs.getDouble("sale_comm_variance");
					}
					logger.info("agtGameCommVariance==" + agtGameCommVariance + "  agt_sale_comm_rate==" + agt_sale_comm_rate);
	
					pStmt = connection.prepareStatement(QueryManager.insertInLMSTransactionMasterQuery());
	//				pStmt = connection.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?, 'SE', 'WEB')");
	
					// insert in transaction master
					pStmt.setString(1, "BO");
					pStmt.setString(2, "SE");
					pStmt.setString(3, "WEB");
					pStmt.executeUpdate();
					rs = pStmt.getGeneratedKeys();
	
					while (rs.next()) {
						masterTrnId = rs.getInt(1);
						trnIdList.add(masterTrnId);
					}
	
					pStmt = connection.prepareStatement(QueryManager.insertInBOTransactionMaster());
					pStmt.setInt(1, masterTrnId);
					pStmt.setInt(2, 11001);
					pStmt.setInt(3, 1);
					pStmt.setString(4, "AGENT");
					pStmt.setInt(5, list.get(1));
					pStmt.setTimestamp(6, currentDate);
					pStmt.setString(7, "SALE");
					pStmt.execute();
	
					mrpAmt = bookList.size() * bookPrice;
					govtCommAmt = mrpAmt * govt_comm_rate * .01;
					agtSaleCommRate = agt_sale_comm_rate + agtGameCommVariance;
					agtcommAmt = mrpAmt * agtSaleCommRate * 0.01;
					logger.info("agtcommAmt === " + agtcommAmt);
					netAmt = mrpAmt - agtcommAmt;
					creditAmt += netAmt;
	
					vatAmt = CommonMethods.calculateVat(mrpAmt, agtSaleCommRate, prizepayoutRatio, govt_comm_rate, vat, govtCommRule, fixed_amt, tickets_in_scheme);
	//				vatAmt = 0.0;
					logger.info("mrp :" + mrpAmt + "ppr :" + prizepayoutRatio + "gov comm :" + govt_comm_rate + "agt net comm :" + agtSaleCommRate);
	
					taxableSale = CommonMethods.calTaxableSale(mrpAmt, agtSaleCommRate, prizepayoutRatio, govt_comm_rate, vat);
	//				taxableSale = 0.0;
					logger.info("vat amount is :  " + vatAmt + " and taxable sale is  : " + taxableSale);
	
					pStmt = connection.prepareStatement(QueryManager.getST1BOAgentQuery());
					pStmt.setInt(1, masterTrnId);
					pStmt.setInt(2, bookList.size());
					pStmt.setInt(3, gameId);
					pStmt.setInt(4, list.get(1));
					
					pStmt.setDouble(5, mrpAmt);
					pStmt.setDouble(6, agtcommAmt);
					pStmt.setDouble(7, netAmt);
					pStmt.setString(8, "SALE");
					pStmt.setDouble(9, vatAmt);
					pStmt.setDouble(10, taxableSale);
					pStmt.setDouble(11, govtCommAmt);
					pStmt.execute();
	
					bookNbrList.setLength(0);
					for (String str : bookList) {
						bookNbrList.append("'");
						bookNbrList.append(str);
						bookNbrList.append("',");
					}
	
					if (bookList.size() > 0) {
						bookNbrList.setLength(bookNbrList.length() - 1);
					}
					
					if (bookList != null) {
						// ************Check Below Query
						stmt = connection.createStatement();
						
						pStmt = connection.prepareStatement(QueryManager.getST1BOUpdGameInvStatusInvoiceAgentQuery() + " and book_nbr = ?;");
						pStmtInvDetail = connection.prepareStatement(QueryManager.getST1InvAgentInvoiceDetailInsertQuery());
	
						for (String book : bookList) {
							packNbr = new DirectSaleBOHelper().getPackNbrForBook(connection, gameId, book);
							rs = stmt.executeQuery("select book_status, warehouse_id, current_owner from st_se_game_inv_status where game_id = " +gameId+" and book_nbr = '"+book+"' and pack_nbr = '"+packNbr+"'");
	
							if(!rs.next()) {
								throw new ScratchException(ScratchErrors.SCRATCH_BOOK_NOT_AVAILABLE_ERROR_CODE, ScratchErrors.SCRATCH_BOOK_NOT_AVAILABLE_ERROR_MESSAGE);
							}
	
							pStmt.setInt(1, invoiceId);
							pStmt.setInt(2, gameId);
//							pStmt.setInt(3, orderId);
							pStmt.setString(3, packNbr);
							pStmt.setString(4, book);
							
							pStmt.execute();
	
							pStmtInvDetail.setInt(1, masterTrnId);
							pStmtInvDetail.setInt(2, gameId);
							pStmtInvDetail.setString(3, packNbr);
							pStmtInvDetail.setString(4, book);
							pStmtInvDetail.setString(5, rs.getString("current_owner"));
							pStmtInvDetail.setInt(6, list.get(1));
							pStmtInvDetail.setTimestamp(7, currentDate);
							pStmtInvDetail.setString(8, "BO");
							pStmtInvDetail.setDouble(9, agtSaleCommRate);
							pStmtInvDetail.setDouble(10, govt_comm_rate);
							pStmtInvDetail.setObject(11, null);
							pStmtInvDetail.setInt(12, 11001);
							pStmtInvDetail.setString(13, rs.getString("book_status"));
							pStmtInvDetail.setInt(14, rs.getInt("warehouse_id"));
							pStmtInvDetail.setInt(15, invoiceId);
							pStmtInvDetail.execute();
						}
					}
	
					// for org credit updation
					logger.info("Org Id For Credit Update:" + list.get(1) + ":" + creditAmt);
	
					boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", list.get(1), 0, "AGENT", 0, connection);
	
					if (!isValid)
						throw new LMSException();
				}
			}

			pStmt = connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
			pStmt.setString(1, "INVOICE");
			rs = pStmt.executeQuery();
			while (rs.next()) {
				lastRecieptNoGenerated = rs.getString("generated_id");
			}
			autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("INVOICE", lastRecieptNoGenerated, "BO");

			logger.info("lastInvoiceNoGenerated " + lastRecieptNoGenerated + "autoGeneInvoiceNo " + autoGeneRecieptNo);

			// insert into BO receipt table
			pStmt = connection.prepareStatement(QueryManager.insertInBOReceipts());
			pStmt.setInt(1, invoiceId);
			pStmt.setString(2, "INVOICE");
			pStmt.setInt(3, list.get(1));
			pStmt.setString(4, "AGENT");
			pStmt.setString(5, autoGeneRecieptNo);
			pStmt.setTimestamp(6, Util.getCurrentTimeStamp());
			logger.info("bo invoice receipt = " + pStmt);
			pStmt.execute();

			pStmt = connection.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			// insert for receipt and transaction mapping table for Invoice
			for (int i = 0; i < trnIdList.size(); i++) {
				pStmt.setInt(1, invoiceId);
				pStmt.setInt(2, trnIdList.get(i));
				pStmt.execute();
			}

			// insert into invoice and delivery challan mapping table
			if (dl != null) {
				mapInvoiceToDL(invoiceId, autoGeneRecieptNo, dl, connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return invoiceId;
	}

	public int generateInvoiceForRetailer(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, String dl, Connection connection) throws ScratchException {
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
		int invoiceId = -1;
		int masterTrnId = -1;
		List<Integer> trnIdList = new ArrayList<Integer>();
		List<Integer> list = null;
		double purRate = 0.0;

		double mrpAmt = 0.0;
		double commAmt = 0.0;
		double netAmt = 0.0;
		double vatAmt = 0.0;
		double taxableSale = 0.0;
		double retSaleCommRate = 0.0;
		double creditAmt = 0.0;

		PreparedStatement pStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		int gameId = 0;
//		int orderId = 0;
		List<String> bookList = null;

		try {
			pStmt = connection.prepareStatement(QueryManager.insertInReceiptMaster());
			pStmt.setString(1, "AGENT");
			pStmt.executeUpdate();

			rs = pStmt.getGeneratedKeys();

			while (rs.next()) {
				invoiceId = rs.getInt(1);
			}

			stmt = connection.createStatement();
			for (Map.Entry<Integer, OrderGameBookBeanMaster> gameMap : gameBookMap.entrySet()) {
				gameId = gameMap.getKey();
//				orderId = gameMap.getValue().getOrderId();
				bookList = gameMap.getValue().getBookList();
				rs = stmt.executeQuery("select * from st_se_game_master where game_id=" + gameId);
				if (rs.next()) {
					list = fetchParentUserAndOrgId(orgId, connection);
					java.sql.Timestamp currentDate = new java.sql.Timestamp(new Date().getTime());
	
					gameId = rs.getInt("game_id");
					bookPrice = rs.getDouble("ticket_price") * rs.getInt("nbr_of_tickets_per_book");
					noOfTktPerBooks = rs.getInt("nbr_of_tickets_per_book");
					retailer_sale_comm_rate = rs.getDouble("retailer_sale_comm_rate");
					prizepayoutRatio = rs.getDouble("prize_payout_ratio");
					vat = rs.getDouble("vat_amt");
					fixed_amt = rs.getDouble("fixed_amt");
					tickets_in_scheme = rs.getLong("tickets_in_scheme");
					govtCommRule = rs.getString("govt_comm_type");
					govt_comm_rate = rs.getDouble("govt_comm_rate");
	
					// To Generate Invoice Receipt
					
	
					rs = stmt.executeQuery("select sale_comm_variance  from st_se_agent_retailer_sale_pwt_comm_variance where retailer_org_id=" + orgId + " and game_id=" + gameId);
					if (rs.next()) {
						retGameCommVariance = rs.getDouble("sale_comm_variance");
					}
	
					pStmt = connection.prepareStatement("select count(*) from st_se_game_govt_comm_history where game_id=?");
					pStmt.setInt(1, gameId);
					rs = pStmt.executeQuery();
					if (rs.next()) {
						if (rs.getInt(1) > 1) {
							isGovtCommChanged = true;
						}
					}
	
					if (isGovtCommChanged == false) {
						// insert in LMS transaction master
						pStmt = connection.prepareStatement(QueryManager.insertInLMSTransactionMasterQuery());
						pStmt.setString(1, "AGENT");
						pStmt.setString(2, "SE");
						pStmt.setString(3, "WEB");
						pStmt.executeUpdate();
						rs = pStmt.getGeneratedKeys();
	
						while (rs.next()) {
							masterTrnId = rs.getInt(1);
							trnIdList.add(masterTrnId);
						}
	
						// insert in agent transaction master
						pStmt = connection.prepareStatement(QueryManager.insertInAgentTransactionMaster());
						pStmt.setInt(1, masterTrnId);
						pStmt.setInt(2, list.get(0));
						pStmt.setInt(3, list.get(1));
						pStmt.setString(4, "RETAILER");
						pStmt.setInt(5, orgId);
						pStmt.setString(6, "SALE");
						pStmt.setTimestamp(7, currentDate);
						pStmt.execute();
	
						// Agent - Retailer Trn Master
						mrpAmt = bookList.size() * bookPrice;
						retSaleCommRate = retailer_sale_comm_rate + retGameCommVariance;
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;
						vatAmt = CommonMethods.calculateVat(mrpAmt, retSaleCommRate, prizepayoutRatio, govt_comm_rate, vat, govtCommRule, fixed_amt, tickets_in_scheme);
						taxableSale = CommonMethods.calTaxableSale(mrpAmt, retSaleCommRate, prizepayoutRatio, govt_comm_rate, vat);
	
						pStmt = connection.prepareStatement(QueryManager.getST1AgentRetQuery());
						pStmt.setInt(1, masterTrnId);
						pStmt.setInt(2, gameId);
						pStmt.setInt(3, list.get(0));
						pStmt.setInt(4, orgId);
						pStmt.setDouble(5, mrpAmt);
						pStmt.setDouble(6, commAmt);
						pStmt.setDouble(7, netAmt);
						pStmt.setString(8, "SALE");
						pStmt.setInt(9, bookList.size());
						pStmt.setDouble(10, vatAmt);
						pStmt.setDouble(11, taxableSale);
						pStmt.setInt(12, list.get(1));
						pStmt.execute();
	
						if (bookList != null) {
							// ************Check Below Query
							stmt = connection.createStatement();
							
							StringBuilder bookNbrList = new StringBuilder();
							
							for (String str : bookList) {
								bookNbrList.append("'");
								bookNbrList.append(str);
								bookNbrList.append("',");
							}
	
							if (bookList.size() > 0) {
								bookNbrList.setLength(bookNbrList.length() - 1);
							}
	
							pStmt = connection.prepareStatement(QueryManager.getST1BOUpdGameInvStatusInvoiceRetailerQuery() + " and book_nbr in (" + bookNbrList + ");");
							PreparedStatement pStmtInvDetail = connection.prepareStatement(QueryManager.getST1InvRetailerInvoiceDetailInsertQuery());
	
							for (String book : bookList) {
								String packNbr = new DirectSaleBOHelper().getPackNbrForBook(connection, gameId, book);
								rs = stmt.executeQuery("select book_status, warehouse_id, current_owner, agent_invoice_id from st_se_game_inv_status where game_id = " +gameId+" and book_nbr = '"+book+"' and pack_nbr = '"+packNbr+"'");
	
								if(!rs.next()) {
									throw new ScratchException(ScratchErrors.SCRATCH_BOOK_NOT_AVAILABLE_ERROR_CODE, ScratchErrors.SCRATCH_BOOK_NOT_AVAILABLE_ERROR_MESSAGE);
								}
	
								pStmt.setInt(1, invoiceId);
								pStmt.setInt(2, gameId);
//								pStmt.setInt(3, orderId);
								pStmt.setString(3, packNbr);
	
								pStmt.execute();
	
								pStmtInvDetail.setInt(1, masterTrnId);
								pStmtInvDetail.setInt(2, gameId);
								pStmtInvDetail.setString(3, packNbr);
								pStmtInvDetail.setString(4, book);
								pStmtInvDetail.setString(5, rs.getString("current_owner"));
								pStmtInvDetail.setInt(6, list.get(1));
								pStmtInvDetail.setTimestamp(7, currentDate);
								pStmtInvDetail.setString(8, "RETAILER");
								pStmtInvDetail.setDouble(9, retailer_sale_comm_rate);
								pStmtInvDetail.setDouble(10, govt_comm_rate);
								pStmtInvDetail.setObject(11, null);
								pStmtInvDetail.setInt(12, 11001);
								pStmtInvDetail.setString(13, rs.getString("book_status"));
								pStmtInvDetail.setInt(14, rs.getInt("warehouse_id"));
								pStmtInvDetail.setInt(15, rs.getInt("agent_invoice_id"));
								pStmtInvDetail.setInt(16, invoiceId);
								pStmtInvDetail.execute();
							}
						}
					} else {
						mrpAmt = bookList.size() * bookPrice;
						retSaleCommRate = retailer_sale_comm_rate + retGameCommVariance;
						commAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - commAmt;
						creditAmt += netAmt;
	
						ResultSet rsCommVar;
						ArrayList<BookSaleBean> bookSaleBeanList = new ArrayList<BookSaleBean>();
						BookSaleBean bookSaleBean = null;
						for (String bookName : bookList) {
							bookSaleBean = new BookSaleBean();
							double govtCommRate = 0.0;
							double vatAmtForBook = 0.0;
							double taxableSaleForbook = 0.0;
	
							pStmt = connection.prepareStatement("select transaction_gov_comm_rate,transaction_date from st_se_game_inv_detail where current_owner=? and current_owner_id=? and game_id=? and book_nbr=? and transaction_at=? order by transaction_date desc limit 1 ");
							pStmt.setString(1, "AGENT");
							pStmt.setInt(2, orgId);
							pStmt.setInt(3, gameId);
							pStmt.setString(4, bookName);
							pStmt.setString(5, "BO");
							rsCommVar = pStmt.executeQuery();
							while (rsCommVar.next()) {
								govtCommRate = rsCommVar.getDouble("transaction_gov_comm_rate");
							}
	
							vatAmtForBook = CommonMethods.calculateVat(bookPrice, retSaleCommRate, prizepayoutRatio, govtCommRate, vat, govtCommRule, fixed_amt, tickets_in_scheme);
							taxableSaleForbook = CommonMethods.calTaxableSale(bookPrice, retSaleCommRate, prizepayoutRatio, govtCommRate, vat);
	
							bookSaleBean.setBookNumber(bookName);
							bookSaleBean.setBookTaxableSale(taxableSaleForbook);
							bookSaleBean.setBookVatAmt(vatAmtForBook);
							bookSaleBean.setTotalSaleGovtComm(govtCommRate);
							bookSaleBeanList.add(bookSaleBean);
						}
	
						Set<Double> GovtCommVarianceSet = new HashSet<Double>();
						pStmt = connection.prepareStatement("select DISTINCT govt_comm_rate from st_se_game_govt_comm_history where game_id=" + gameId);
						rs = pStmt.executeQuery();
						while (rs.next()) {
							GovtCommVarianceSet.add(rs.getDouble("govt_comm_rate"));
						}
	
						Iterator<Double> govtCommSetItr;
						Iterator<BookSaleBean> bookSaleBeanItr;
						govtCommSetItr = GovtCommVarianceSet.iterator();
	
						while (govtCommSetItr.hasNext()) {
							boolean bookGovtCommVarMatch = false;
							bookSaleBeanItr = bookSaleBeanList.iterator();
							List<String> bookListforSingleTrn = new ArrayList<String>();
	
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
									totMrpAmt = totMrpAmt + bookPrice;
									bookListforSingleTrn.add(bookSaleBean.getBookNumber());
								}
							}
	
							totalCommAmt = totMrpAmt * retSaleCommRate * 0.01;
							totalNetAmt = totMrpAmt - totalCommAmt;
							if (bookGovtCommVarMatch) {
								// insert in LMS transaction master
								pStmt.setString(1, "RETAILER");
								pStmt.setString(2, "SE");
								pStmt.setString(3, "WEB");
								pStmt.executeUpdate();
								rs = pStmt.getGeneratedKeys();
	
								while (rs.next()) {
									masterTrnId = rs.getInt(1);
									trnIdList.add(masterTrnId);
								}
	
								// insert in agent transaction master
								pStmt = connection.prepareStatement(QueryManager.insertInAgentTransactionMaster());
								pStmt.setInt(1, masterTrnId);
								pStmt.setInt(2, list.get(0));
								pStmt.setInt(3, list.get(1));
								pStmt.setString(4, "RETAILER");
								pStmt.setInt(5, orgId);
								pStmt.setString(6, "SALE");
								pStmt.setTimestamp(7, currentDate);
								pStmt.execute();
	
								// Agent - Retailer Trn Master
								mrpAmt = bookList.size() * bookPrice;
								retSaleCommRate = retailer_sale_comm_rate + retGameCommVariance;
								commAmt = mrpAmt * retSaleCommRate * 0.01;
								netAmt = mrpAmt - commAmt;
								creditAmt += netAmt;
								vatAmt = CommonMethods.calculateVat(mrpAmt, retSaleCommRate, prizepayoutRatio, govt_comm_rate, vat, govtCommRule, fixed_amt, tickets_in_scheme);
								taxableSale = CommonMethods.calTaxableSale(mrpAmt, retSaleCommRate, prizepayoutRatio, govt_comm_rate, vat);
	
								pStmt = connection.prepareStatement(QueryManager.getST1AgentRetQuery());
								pStmt.setInt(1, masterTrnId);
								pStmt.setInt(2, gameId);
								pStmt.setInt(3, list.get(0));
								pStmt.setInt(4, orgId);
								pStmt.setDouble(5, mrpAmt);
								pStmt.setDouble(6, commAmt);
								pStmt.setDouble(7, netAmt);
								pStmt.setString(8, "SALE");
								pStmt.setInt(9, bookList.size());
								pStmt.setDouble(10, vatAmt);
								pStmt.setDouble(11, taxableSale);
								pStmt.setInt(12, list.get(1));
								pStmt.execute();
	
								for (String bookNubr : bookListforSingleTrn) {
									// added by arun
									pStmt = connection.prepareStatement("select transacrion_sale_comm_rate from st_se_game_inv_detail where book_nbr=? and transaction_at='BO' order by transaction_date desc limit 1");
									pStmt.setString(1, bookNubr);
									rs = pStmt.executeQuery();
									if (rs.next()) {
										purRate = rs.getDouble("transacrion_sale_comm_rate");
									}
								}
							}
						}
						if (bookList != null) {
							String newBookStatus = "ACTIVE";
							//pStmt = connection.prepareStatement(QueryManager.getST1AgtUpdGameInvStatusQuery() + " and book_nbr = ?");
							pStmt = connection.prepareStatement("UPDATE st_se_game_inv_status SET current_owner=?, current_owner_id=?, book_status=?, cur_rem_tickets=?, active_tickets_upto=? WHERE game_id=? AND book_nbr=?;");
							for (String book : bookList) {
								pStmt.setString(1, "RETAILER");
								pStmt.setInt(2, orgId);
								pStmt.setString(3, newBookStatus);
								pStmt.setInt(4, noOfTktPerBooks);
								if ("ACTIVE".equalsIgnoreCase(newBookStatus.trim())) {
									pStmt.setInt(5, noOfTktPerBooks);
								} else {
									pStmt.setInt(5, 0);
								}
								pStmt.setInt(6, gameId);
								pStmt.setString(7, book);
								pStmt.execute();
							}
						}
					}
				}
				// for org credit updation
				System.out.println("Org Id For Credit Update:" + orgId + ":" + creditAmt);
	
				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "SALE", orgId, list.get(1), "RETAILER", 0, connection);
				if (!isValid)
					throw new LMSException();
			}

			pStmt = connection.prepareStatement(QueryManager.getAGENTLatestReceiptNb());
			pStmt.setString(1, "INVOICE");
			pStmt.setInt(2, list.get(1));
			rs = pStmt.executeQuery();
			String lastRecieptNoGenerated = null;
			while (rs.next()) {
				lastRecieptNoGenerated = rs.getString("generated_id");
			}

			String autoGeneRecieptNo = null;
			autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt("INVOICE", lastRecieptNoGenerated, "AGENT", list.get(1));

			pStmt = connection.prepareStatement(QueryManager.insertInAgentReceipts());
			pStmt.setInt(1, invoiceId);
			pStmt.setString(2, "INVOICE");
			pStmt.setInt(3, list.get(1));
			pStmt.setInt(4, orgId);
			pStmt.setString(5, "RETAILER");
			pStmt.setString(6, autoGeneRecieptNo);
			pStmt.setTimestamp(7, Util.getCurrentTimeStamp());
			pStmt.execute();

			pStmt = connection.prepareStatement(QueryManager.insertAgentReceiptTrnMapping());

			for (Integer txnId : trnIdList) {
				pStmt.setInt(1, invoiceId);
				pStmt.setInt(2, txnId);
				pStmt.execute();
			}

			if (dl != null) {
				mapInvoiceToDL(invoiceId, autoGeneRecieptNo, dl, connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return invoiceId;
	}

	// DONE
	public void mapInvoiceToDL(int invcoiceId, String invoiceReceiptId, String dl, Connection connection) throws ScratchException {
		PreparedStatement pStmt = null;
		try {
			pStmt = connection.prepareStatement("insert into st_se_bo_invoice_delchallan_mapping(id,generated_invoice_id,generated_del_challan_id) values(?,?,?)");
			pStmt.setInt(1, invcoiceId);
			pStmt.setString(2, invoiceReceiptId);
			pStmt.setString(3, dl);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

}