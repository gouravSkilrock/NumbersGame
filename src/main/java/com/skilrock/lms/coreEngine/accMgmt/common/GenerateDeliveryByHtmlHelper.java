package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.accMgmt.common.CustomerBean;
import com.skilrock.lms.web.accMgmt.common.InvoiceGameDetailBean;

public class GenerateDeliveryByHtmlHelper {
	static Log logger = LogFactory.getLog(GenerateDeliveryByHtmlHelper.class);

	public static void main(String[] args) {
		GenerateDeliveryByHtmlHelper helper = new GenerateDeliveryByHtmlHelper(
				"DRCHALLAN", 1);
		Map returnMap = helper.getSaleReturnChallan(425);
		Set<Integer> returnSet = returnMap.keySet();
		for (Integer returnkey : returnSet) {
			logger.debug("transaction id = " + returnkey);
			Map map = (Map) returnMap.get(returnkey);
			Set mapkey = map.keySet();
			for (Object key : mapkey) {
				InvoiceGameDetailBean bean = (InvoiceGameDetailBean) map
						.get(key);
				System.out.print("\n" + bean.getGameName()
						+ "  and  Sale comm  " + bean.getSalCommVar()
						+ ": -  \n");
				System.out.print("packs : ");
				for (String pack : bean.getPackNbrList()) {
					System.out.print(pack + "  ");
				}
				System.out.print("\nbooks : ");
				// Set<String> bookList=new TreeSet<String>();
				// bookList.addAll(bean.getBookNbrList());
				for (String book : bean.getBookNbrList()) {

					System.out.print(book + "  ");
				}
				logger.debug("\ntotal books ==== "
						+ bean.getBookNbrList().size());
				logger.debug("\n DC id " + helper.detailMap);
				logger.debug("\n DC Date " + helper.transactionDate);
			}
		}
		logger.debug("\n" + helper.bean.getCustomerName() + " a1: "
				+ helper.bean.getCustomerAdd1() + "  a2: "
				+ helper.bean.getCustomerAdd2());

	}

	public CustomerBean bean;
	private Connection con;
	private int currentUserorgId = -1;

	public String dcId = null;

	public Map detailMap = new TreeMap();
	public String transactionDate = null;

	private String type = null;

	public GenerateDeliveryByHtmlHelper(String type, int orgId) {
		logger.debug("type is ========= " + type);
		this.type = type;
		this.currentUserorgId = orgId;
	}

	public Map getInvoiceDetail(int id) {
		Set gameIdSet = new TreeSet();
		Set packNbrSet = new TreeSet();
		Map invoiceMap = new TreeMap();
		List bookNbrList = new ArrayList();
		con = DBConnect.getConnection();
		String query = QueryManager.getST_BO_INVOICE_DETAILS();
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "BO");
				pstmt.setInt(3, id);
			} else {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "AGENT");
				pstmt.setInt(3, id);
			}
			logger.debug("query" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			InvoiceGameDetailBean gameDetailBean = null;
			String gameName = null;
			boolean flag = true;
			boolean elseFlag = true;
			while (rs.next()) {

				if (flag) {
					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));
					gameDetailBean.setNbrBooks(rs.getInt("books_per_pack"));
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
					transactionDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(sqlDateToutilDate(rs
									.getDate("transaction_date")));
					getInvoiceForCustomerDetail(rs.getInt("party_id"), con);
					logger
							.debug("DSRCHALLAN.equalsIgnoreCase(this.type.trim()) ======    abc"
									+ this.type.trim()
									+ "efg    "
									+ "DSRCHALLAN".equalsIgnoreCase(this.type
											.trim()));
					if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
						getInvoiceIDFORSaleRet(id, con);
					} else {
						getInvoiceIDFORSale(id, con);
					}

					flag = false;
					continue;
				}

				int gameId = rs.getInt("game_id");
				String packNbr = rs.getString("pack_nbr");
				String bookNbr = rs.getString("total_books");

				gameName = rs.getString("game_name").trim();

				if (gameIdSet.contains(gameId)) {

					gameDetailBean.setGameName(rs.getString("game_name"));
					int booksPerPack = rs.getInt("books_per_pack");
					gameDetailBean.setNbrBooks(booksPerPack);

					if (packNbrSet.contains(packNbr)
							&& bookNbrList.size() < booksPerPack) {
						bookNbrList.add(bookNbr);
						if (bookNbrList.size() == booksPerPack) {
							gameDetailBean.setPackNbrList(packNbr);
							logger.debug("complete pack list" + packNbrSet
									+ "book list " + bookNbrList);
							bookNbrList.clear();
							packNbrSet.clear();
						}
					} else if (bookNbrList.size() < booksPerPack) {
						if (bookNbrList.size() > 0) {
							gameDetailBean.setBookNbrList(bookNbrList);
						}
						bookNbrList.clear();
						bookNbrList.add(bookNbr);
						packNbrSet.clear();
						packNbrSet.add(packNbr);
						System.out
								.print("pack series changed ======  booklist : "
										+ bookNbrList);
						logger.debug("pack Nbr list " + packNbrSet);

					}
					// invoiceMap.put(gameDetailBean.getGameName(),
					// gameDetailBean);
					// continue;
				} else {
					elseFlag = false;
					if (bookNbrList.size() == gameDetailBean.getNbrBooks()) {
						gameDetailBean.setPackNbrList(packNbr);
						logger.debug(" inside else final pack "
								+ gameDetailBean.getPackNbrList() + "\n\n");
					} else {
						gameDetailBean.setBookNbrList(bookNbrList);
						logger.debug(" inside else finalbooks "
								+ gameDetailBean.getBookNbrList() + "\n\n");
					}

					invoiceMap
							.put(gameDetailBean.getGameName(), gameDetailBean);

					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));

					bookNbrList.clear();
					packNbrSet.clear();
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
				}
			}
			if (rs.last() && gameDetailBean != null) {

				gameDetailBean.setBookNbrList(bookNbrList);
				invoiceMap.put(gameDetailBean.getGameName(), gameDetailBean);

			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}

			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return invoiceMap;
	}

	public Map getDelChallanDetails(int id) {
		Set gameIdSet = new TreeSet();
		Set packNbrSet = new TreeSet();
		Map invoiceMap = new TreeMap();
		List bookNbrList = new ArrayList();
		con = DBConnect.getConnection();
		String query = "SELECT game_id, pack_nbr, total_books, transaction_date, transaction_at, agent_org_id, current_owner_id,(SELECT game_name FROM st_se_game_master WHERE game_id=aa.game_id) 'game_name', (SELECT nbr_of_books_per_pack FROM st_se_game_master WHERE game_id=aa.game_id) 'books_per_pack' FROM (SELECT gid.game_id, pack_nbr, book_nbr 'total_books', transaction_date, transaction_at, agent_org_id, current_owner_id FROM st_se_game_inv_detail gid INNER JOIN st_se_bo_order_invoices boi ON gid.order_invoice_dc_id=boi.order_invoice_dc_id WHERE transaction_at=(SELECT organization_type FROM st_lms_organization_master WHERE organization_id=?) AND dc_id=? AND gid.current_owner=?)aa ORDER BY game_id, pack_nbr, total_books;";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setInt(2, id);
				pstmt.setString(3, "BO");
			} else {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setInt(2, id);
				pstmt.setString(3, "AGENT");
			}
			logger.debug("query" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			InvoiceGameDetailBean gameDetailBean = null;
			String gameName = null;
			boolean flag = true;
			boolean elseFlag = true;
			while (rs.next()) {

				if (flag) {
					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));
					gameDetailBean.setNbrBooks(rs.getInt("books_per_pack"));
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
					transactionDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(sqlDateToutilDate(rs
									.getDate("transaction_date")));
					getInvoiceForCustomerDetail(rs.getInt("agent_org_id"), con);
					logger
							.debug("DSRCHALLAN.equalsIgnoreCase(this.type.trim()) ======    abc"
									+ this.type.trim()
									+ "efg    "
									+ "DSRCHALLAN".equalsIgnoreCase(this.type
											.trim()));
					if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
						getInvoiceIDFORSaleRet(id, con);
					} else {
						getInvoiceIDFORSaleNew(id, con);
					}

					flag = false;
					continue;
				}

				int gameId = rs.getInt("game_id");
				String packNbr = rs.getString("pack_nbr");
				String bookNbr = rs.getString("total_books");

				gameName = rs.getString("game_name").trim();

				if (gameIdSet.contains(gameId)) {

					gameDetailBean.setGameName(rs.getString("game_name"));
					int booksPerPack = rs.getInt("books_per_pack");
					gameDetailBean.setNbrBooks(booksPerPack);

					if (packNbrSet.contains(packNbr)
							&& bookNbrList.size() < booksPerPack) {
						bookNbrList.add(bookNbr);
						if (bookNbrList.size() == booksPerPack) {
							gameDetailBean.setPackNbrList(packNbr);
							logger.debug("complete pack list" + packNbrSet
									+ "book list " + bookNbrList);
							bookNbrList.clear();
							packNbrSet.clear();
						}
					} else if (bookNbrList.size() < booksPerPack) {
						if (bookNbrList.size() > 0) {
							gameDetailBean.setBookNbrList(bookNbrList);
						}
						bookNbrList.clear();
						bookNbrList.add(bookNbr);
						packNbrSet.clear();
						packNbrSet.add(packNbr);
						System.out
								.print("pack series changed ======  booklist : "
										+ bookNbrList);
						logger.debug("pack Nbr list " + packNbrSet);

					}
					// invoiceMap.put(gameDetailBean.getGameName(),
					// gameDetailBean);
					// continue;
				} else {
					elseFlag = false;
					if (bookNbrList.size() == gameDetailBean.getNbrBooks()) {
						gameDetailBean.setPackNbrList(packNbr);
						logger.debug(" inside else final pack "
								+ gameDetailBean.getPackNbrList() + "\n\n");
					} else {
						gameDetailBean.setBookNbrList(bookNbrList);
						logger.debug(" inside else finalbooks "
								+ gameDetailBean.getBookNbrList() + "\n\n");
					}

					invoiceMap
							.put(gameDetailBean.getGameName(), gameDetailBean);

					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));

					bookNbrList.clear();
					packNbrSet.clear();
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
				}
			}
			if (rs.last() && gameDetailBean != null) {

				gameDetailBean.setBookNbrList(bookNbrList);
				invoiceMap.put(gameDetailBean.getGameName(), gameDetailBean);

			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}

			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return invoiceMap;
	}

	public Map getDelChallanDetailsWarehouse(int id) {
		Set gameIdSet = new TreeSet();
		Set packNbrSet = new TreeSet();
		Map invoiceMap = new TreeMap();
		List bookNbrList = new ArrayList();
		con = DBConnect.getConnection();
		//String query = "SELECT game_id, pack_nbr, total_books, transaction_date, transaction_at, agent_org_id, current_owner_id,(SELECT game_name FROM st_se_game_master WHERE game_id=aa.game_id) 'game_name', (SELECT nbr_of_books_per_pack FROM st_se_game_master WHERE game_id=aa.game_id) 'books_per_pack' FROM (SELECT gid.game_id, pack_nbr, book_nbr 'total_books', transaction_date, transaction_at, agent_org_id, current_owner_id FROM st_se_game_inv_detail gid INNER JOIN st_se_bo_order_invoices boi ON gid.order_invoice_dc_id=boi.order_invoice_dc_id WHERE transaction_at=(SELECT organization_type FROM st_lms_organization_master WHERE organization_id=?) AND dc_id=? AND gid.current_owner=?)aa ORDER BY game_id, pack_nbr, total_books;";
		String query = "SELECT game_id, pack_nbr, total_books, transaction_date, transaction_at, agent_org_id, current_owner_id,(SELECT game_name FROM st_se_game_master WHERE game_id=aa.game_id) 'game_name', (SELECT nbr_of_books_per_pack FROM st_se_game_master WHERE game_id=aa.game_id) 'books_per_pack' FROM (SELECT gid.game_id, pack_nbr, book_nbr 'total_books', transaction_date, transaction_at, 1 agent_org_id, current_owner_id FROM st_se_game_inv_detail gid WHERE transaction_at=(SELECT organization_type FROM st_lms_organization_master WHERE organization_id=?) AND gid.order_invoice_dc_id=? AND gid.current_owner=?)aa ORDER BY game_id, pack_nbr, total_books;";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, currentUserorgId);
			pstmt.setInt(2, id);
			pstmt.setString(3, "BO");

			logger.debug("query" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			InvoiceGameDetailBean gameDetailBean = null;
			String gameName = null;
			boolean flag = true;
			boolean elseFlag = true;
			while (rs.next()) {

				if (flag) {
					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));
					gameDetailBean.setNbrBooks(rs.getInt("books_per_pack"));
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
					transactionDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(sqlDateToutilDate(rs
									.getDate("transaction_date")));
					getInvoiceForCustomerDetail(rs.getInt("agent_org_id"), con);
					logger
							.debug("DSRCHALLAN.equalsIgnoreCase(this.type.trim()) ======    abc"
									+ this.type.trim()
									+ "efg    "
									+ "DSRCHALLAN".equalsIgnoreCase(this.type
											.trim()));
					if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
						getInvoiceIDFORSaleRet(id, con);
					} else {
						getInvoiceIDFORSaleWarehouse(id, con);
					}

					flag = false;
					continue;
				}

				int gameId = rs.getInt("game_id");
				String packNbr = rs.getString("pack_nbr");
				String bookNbr = rs.getString("total_books");

				gameName = rs.getString("game_name").trim();

				if (gameIdSet.contains(gameId)) {

					gameDetailBean.setGameName(rs.getString("game_name"));
					int booksPerPack = rs.getInt("books_per_pack");
					gameDetailBean.setNbrBooks(booksPerPack);

					if (packNbrSet.contains(packNbr)
							&& bookNbrList.size() < booksPerPack) {
						bookNbrList.add(bookNbr);
						if (bookNbrList.size() == booksPerPack) {
							gameDetailBean.setPackNbrList(packNbr);
							logger.debug("complete pack list" + packNbrSet
									+ "book list " + bookNbrList);
							bookNbrList.clear();
							packNbrSet.clear();
						}
					} else if (bookNbrList.size() < booksPerPack) {
						if (bookNbrList.size() > 0) {
							gameDetailBean.setBookNbrList(bookNbrList);
						}
						bookNbrList.clear();
						bookNbrList.add(bookNbr);
						packNbrSet.clear();
						packNbrSet.add(packNbr);
						System.out
								.print("pack series changed ======  booklist : "
										+ bookNbrList);
						logger.debug("pack Nbr list " + packNbrSet);

					}
					// invoiceMap.put(gameDetailBean.getGameName(),
					// gameDetailBean);
					// continue;
				} else {
					elseFlag = false;
					if (bookNbrList.size() == gameDetailBean.getNbrBooks()) {
						gameDetailBean.setPackNbrList(packNbr);
						logger.debug(" inside else final pack "
								+ gameDetailBean.getPackNbrList() + "\n\n");
					} else {
						gameDetailBean.setBookNbrList(bookNbrList);
						logger.debug(" inside else finalbooks "
								+ gameDetailBean.getBookNbrList() + "\n\n");
					}

					invoiceMap
							.put(gameDetailBean.getGameName(), gameDetailBean);

					gameDetailBean = new InvoiceGameDetailBean();
					gameDetailBean.setGameName(rs.getString("game_name"));

					bookNbrList.clear();
					packNbrSet.clear();
					gameIdSet.add(rs.getInt("game_id"));
					packNbrSet.add(rs.getString("pack_nbr"));
					bookNbrList.add(rs.getString("total_books"));
				}
			}
			if (rs.last() && gameDetailBean != null) {

				gameDetailBean.setBookNbrList(bookNbrList);
				invoiceMap.put(gameDetailBean.getGameName(), gameDetailBean);

			}
			String bookNumber = "";
			Statement stmt = con.createStatement();
			query = "select book_nbr  from st_se_game_inv_detail where order_invoice_dc_id="+id+"  limit 1;";
			logger.info(query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				bookNumber =  rs.getString("book_nbr");
			}
			
			
			Statement statement = con.createStatement();
			query ="SELECT movement, main.warehouse_id, warehouse_name, address1, address2, (SELECT city_name FROM st_lms_city_master WHERE city_code=city_id) city, (SELECT NAME FROM st_lms_state_master WHERE state_code=state_id) state, (SELECT NAME FROM st_lms_country_master WHERE STATUS='ACTIVE' LIMIT 1) country FROM (SELECT 'TO' movement, warehouse_id FROM st_se_game_inv_detail WHERE order_invoice_dc_id="+id+" UNION ALL SELECT 'FROM' movement, warehouse_id FROM (SELECT warehouse_id FROM st_se_game_inv_detail WHERE transaction_date<(SELECT transaction_date FROM st_se_game_inv_detail WHERE order_invoice_dc_id= "+id+"  limit 1) AND warehouse_id<>'NULL' and book_nbr=  '"+bookNumber+"' ORDER BY transaction_date DESC LIMIT 1)aa) main INNER JOIN st_se_warehouse_master wm ON main.warehouse_id=wm.warehouse_id;";
			logger.info(query);
			rs = statement.executeQuery(query);
			while(rs.next()) {
				if("FROM".equals(rs.getString("movement"))) {
					detailMap.put("fromWareHouseName", rs.getString("warehouse_name"));
					detailMap.put("fromWareHouseAddress1", rs.getString("address1"));
					detailMap.put("fromWareHouseAddress2", rs.getString("address2"));
					detailMap.put("fromWareHouseCityStateCountry", rs.getString("city")+" , "+rs.getString("state")+" , "+rs.getString("country"));
				} else if("TO".equals(rs.getString("movement"))) {
					detailMap.put("toWareHouseName", rs.getString("warehouse_name"));
					detailMap.put("toWareHouseAddress1", rs.getString("address1"));
					detailMap.put("toWareHouseAddress2", rs.getString("address2"));
					detailMap.put("toWareHouseCityStateCountry", rs.getString("city")+" , "+rs.getString("state")+" , "+rs.getString("country"));
				}
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}

			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return invoiceMap;
	}

	private void getInvoiceForCustomerDetail(int orgId, Connection conn)
			throws SQLException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}
		PreparedStatement pstmt = con.prepareStatement(QueryManager
				.getST_BO_INVOICE_CUSTOMER_DETAILS());
		pstmt.setInt(1, orgId);
		ResultSet rs = pstmt.executeQuery();
		if (rs != null) {
			if (rs.next()) {
				bean = new CustomerBean();
				bean.setCustomerName(rs.getString("name"));
				bean.setCustomerAdd1(rs.getString("addr_line1"));
				bean.setCustomerAdd2(rs.getString("addr_line2") + ", "
						+ rs.getString("city") + ", " + rs.getString("state")
						+ ", " + rs.getString("country"));
				bean.setVatRefNo(rs.getString("vat_ref_No"));
			}
		}

	}

	private void getInvoiceIDFORSale(int id, Connection conn)
			throws SQLException, ParseException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}
		PreparedStatement pstmt = con
				.prepareStatement("select aa.generated_invoice_id 'invoiceId', aa.generated_id 'dcId', bb.order_id, cc.order_date from ( 	select rgm.generated_id , generated_invoice_id 	from st_lms_bo_receipts rgm, st_se_bo_invoice_delchallan_mapping idm  	where rgm.receipt_id=?  and rgm.generated_id =idm.generated_del_challan_id )aa, 	( select  distinct order_id  from st_se_bo_order_invoices where invoice_id=( 	select idm.id  from st_lms_bo_receipts rgm, st_se_bo_invoice_delchallan_mapping idm 	where rgm.receipt_id=? and idm.generated_del_challan_id=rgm.generated_id) )bb, (  select order_date from st_se_bo_order where order_id=( select  distinct order_id  from st_se_bo_order_invoices where invoice_id=(select idm.id  from st_lms_bo_receipts rgm, st_se_bo_invoice_delchallan_mapping idm where rgm.receipt_id=? and idm.generated_del_challan_id=rgm.generated_id))  )cc");
		pstmt.setInt(1, id);
		pstmt.setInt(2, id);
		pstmt.setInt(3, id);

		ResultSet rs = pstmt.executeQuery();

		if (rs != null) {
			if (rs.next()) {
				detailMap.put("dcId", rs.getString("dcId"));
				detailMap.put("invoiceId", rs.getString("invoiceId"));
				detailMap.put("orderId", rs.getInt("order_id"));
				detailMap.put("orderDate", new SimpleDateFormat("dd/MM/yyyy")
						.format(sqlDateToutilDate(rs.getDate("order_date"))));
			}
		}

		logger.debug("pstmt sale  ===== " + pstmt);
		logger.debug("map when sale  : " + detailMap);
	}

	private void getInvoiceIDFORSaleNew(int id, Connection conn) throws SQLException, ParseException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}

		PreparedStatement pstmt = con.prepareStatement("SELECT generated_id, bo.order_id, order_date FROM st_lms_bo_receipts br INNER JOIN st_se_bo_order_invoices boi ON br.receipt_id=boi.dc_id INNER JOIN st_se_bo_order bo ON boi.order_id=bo.order_id WHERE dc_id=?;");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs != null) {
			if (rs.next()) {
				detailMap.put("dcId", rs.getString("generated_id"));
				detailMap.put("invoiceId", "");
				detailMap.put("orderId", rs.getInt("order_id"));
				detailMap.put("orderDate", new SimpleDateFormat("dd/MM/yyyy")
						.format(sqlDateToutilDate(rs.getDate("order_date"))));
			}
		}

		logger.debug("pstmt sale  ===== " + pstmt);
		logger.debug("map when sale  : " + detailMap);
	}

	private void getInvoiceIDFORSaleWarehouse(int id, Connection conn) throws SQLException, ParseException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}

		PreparedStatement pstmt = con.prepareStatement("SELECT generated_id, '' order_id, voucher_date order_date FROM st_lms_bo_receipts br WHERE receipt_id=?;");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs != null) {
			if (rs.next()) {
				detailMap.put("dcId", rs.getString("generated_id"));
				detailMap.put("invoiceId", "");
				detailMap.put("orderId", rs.getInt("order_id"));
				detailMap.put("orderDate", new SimpleDateFormat("dd/MM/yyyy")
						.format(sqlDateToutilDate(rs.getDate("order_date"))));
			}
		}

		logger.debug("pstmt sale  ===== " + pstmt);
		logger.debug("map when sale  : " + detailMap);
	}

	private void getInvoiceIDFORSaleRet(int id, Connection conn)
			throws SQLException, ParseException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}
		PreparedStatement pstmt = con
				.prepareStatement("select rgm.generated_id 'credit_note' , generated_invoice_id 'sale_ret' from st_lms_bo_receipts rgm, st_se_bo_invoice_delchallan_mapping idm  where rgm.receipt_id=? and rgm.generated_id =idm.generated_del_challan_id");
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		if (rs != null) {
			if (rs.next()) {
				detailMap.put("srNo", rs.getString("sale_ret"));
				detailMap.put("creditNote", rs.getString("credit_note"));

			}
		}
		logger.debug("pstmt sale return ===== " + pstmt);
		logger.debug("map when sale return : " + detailMap);

	}

	public List<String> getOrgDetails(int orgId) {
		Connection con = DBConnect.getConnection();
		List<String> list = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_BO_INVOICE_CUSTOMER_DETAILS());
			pstmt.setInt(1, orgId);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					list = new ArrayList<String>();
					String add1 = rs.getString("addr_line1");

					String add2 = rs.getString("addr_line2") + ", "
							+ rs.getString("city") + ", "
							+ rs.getString("state") + ", "
							+ rs.getString("country");
					list.add(add1 + ", " + add2);
					String vatRef = rs.getString("vat_ref_no");
					list.add(vatRef);

				}
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return list;
	}

	public Map getSaleReturnChallan(int id) {
		Map compSaleReturnMap = new TreeMap();

		Set gameIdSet = null;
		Set packNbrSet = null;
		Map invoiceMap = null;
		List bookNbrList = null;
		con = DBConnect.getConnection();
		String query = QueryManager.getST_BO_SALERETURN_DETAILS();
		try {
			List<Integer> tids = getTransactionIds(con, id);
			for (Integer tid : tids) {
				gameIdSet = new TreeSet();
				packNbrSet = new TreeSet();
				bookNbrList = new ArrayList();
				invoiceMap = new TreeMap();
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "BO");
				pstmt.setInt(3, tid);

				logger.debug("query" + pstmt);
				ResultSet rs = pstmt.executeQuery();
				InvoiceGameDetailBean gameDetailBean = null;
				String gameName = null;
				boolean flag = true;
				boolean elseFlag = true;
				while (rs.next()) {

					if (flag) {
						gameDetailBean = new InvoiceGameDetailBean();
						gameDetailBean.setGameName(rs.getString("game_name"));
						gameDetailBean
								.setSalCommVar(FormatNumber
										.formatNumber(rs
												.getDouble("transacrion_sale_comm_rate")));
						gameDetailBean.setNbrBooks(rs.getInt("books_per_pack"));
						gameIdSet.add(rs.getInt("game_id"));
						packNbrSet.add(rs.getString("pack_nbr"));
						bookNbrList.add(rs.getString("total_books"));
						transactionDate = new SimpleDateFormat("dd/MM/yyyy")
								.format(sqlDateToutilDate(rs
										.getDate("transaction_date")));
						getInvoiceForCustomerDetail(rs.getInt("party_id"), con);
						getInvoiceIDFORSaleRet(id, con);
						flag = false;
						continue;
					}

					int gameId = rs.getInt("game_id");
					String packNbr = rs.getString("pack_nbr");
					String bookNbr = rs.getString("total_books");

					gameName = rs.getString("game_name").trim();

					if (gameIdSet.contains(gameId)) {

						gameDetailBean.setGameName(rs.getString("game_name"));
						int booksPerPack = rs.getInt("books_per_pack");
						gameDetailBean.setNbrBooks(booksPerPack);

						if (packNbrSet.contains(packNbr)
								&& bookNbrList.size() < booksPerPack) {
							bookNbrList.add(bookNbr);
							if (bookNbrList.size() == booksPerPack) {
								gameDetailBean.setPackNbrList(packNbr);
								logger.debug("complete pack list" + packNbrSet
										+ "book list " + bookNbrList);
								bookNbrList.clear();
								packNbrSet.clear();
							}
						} else if (bookNbrList.size() < booksPerPack) {
							if (bookNbrList.size() > 0) {
								gameDetailBean.setBookNbrList(bookNbrList);
							}
							bookNbrList.clear();
							bookNbrList.add(bookNbr);
							packNbrSet.clear();
							packNbrSet.add(packNbr);
							System.out
									.print("pack series changed ======  booklist : "
											+ bookNbrList);
							logger.debug("pack Nbr list " + packNbrSet);

						}
						// invoiceMap.put(gameDetailBean.getGameName(),
						// gameDetailBean);
						// continue;
					} else {
						elseFlag = false;
						if (bookNbrList.size() == gameDetailBean.getNbrBooks()) {
							gameDetailBean.setPackNbrList(packNbr);
							logger.debug(" inside else final pack "
									+ gameDetailBean.getPackNbrList() + "\n\n");
						} else {
							gameDetailBean.setBookNbrList(bookNbrList);
							logger.debug(" inside else finalbooks "
									+ gameDetailBean.getBookNbrList() + "\n\n");
						}

						invoiceMap.put(gameDetailBean.getGameName(),
								gameDetailBean);

						gameDetailBean = new InvoiceGameDetailBean();
						gameDetailBean.setGameName(rs.getString("game_name"));

						bookNbrList.clear();
						packNbrSet.clear();
						gameIdSet.add(rs.getInt("game_id"));
						packNbrSet.add(rs.getString("pack_nbr"));
						bookNbrList.add(rs.getString("total_books"));
					}
				}
				if (rs.last() && gameDetailBean != null) {

					gameDetailBean.setBookNbrList(bookNbrList);
					invoiceMap
							.put(gameDetailBean.getGameName(), gameDetailBean);

				}
				compSaleReturnMap.put(tid, invoiceMap);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}

			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return compSaleReturnMap;

	}

	private List<Integer> getTransactionIds(Connection conn, int dsrId)
			throws SQLException {
		List<Integer> tids = new ArrayList<Integer>();
		PreparedStatement pstatement = conn
				.prepareStatement("select rtm.transaction_id 'tid' from st_lms_bo_receipts_trn_mapping rtm where rtm.receipt_id="
						+ dsrId);
		ResultSet result = pstatement.executeQuery();
		while (result.next()) {
			tids.add(result.getInt("tid"));
		}
		logger.debug("transaction ids list of DSRCHALLAN ===== " + tids);
		return tids;
	}

	private java.util.Date sqlDateToutilDate(java.sql.Date sDate)
			throws ParseException {
		final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		return (java.util.Date) utilDateFormatter.parse(utilDateFormatter
				.format(sDate));
	}

}
