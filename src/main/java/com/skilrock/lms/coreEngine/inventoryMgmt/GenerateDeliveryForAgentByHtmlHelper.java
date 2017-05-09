package com.skilrock.lms.coreEngine.inventoryMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.skilrock.lms.web.inventoryMgmt.common.CustomerBean;
import com.skilrock.lms.web.accMgmt.common.InvoiceGameDetailBean;

public class GenerateDeliveryForAgentByHtmlHelper {
	static Log logger = LogFactory
			.getLog(GenerateDeliveryForAgentByHtmlHelper.class);

	public static void main(String[] args) {
		GenerateDeliveryForAgentByHtmlHelper helper = new GenerateDeliveryForAgentByHtmlHelper(
				"DRCHALLAN", 2);
		Map map = helper.getInvoiceDetail(10);
		Set mapkey = map.keySet();
		for (Object key : mapkey) {
			InvoiceGameDetailBean bean = (InvoiceGameDetailBean) map.get(key);
			System.out.print("\n" + bean.getGameName() + "  : -  \n");
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
			logger.debug("\ntotal books ==== " + bean.getBookNbrList().size());
			logger.debug("\n DC id " + helper.detailMap);
			logger.debug("\n DC Date " + helper.transactionDate);
		}
		logger.debug("\n" + helper.bean.getCustomerName() + " a1: "
				+ helper.bean.getCustomerAdd1() + "  a2: "
				+ helper.bean.getCustomerAdd2());

	}

	public CustomerBean bean;
	private List bookNbrList = new ArrayList();
	private Connection con;
	private int currentUserorgId = -1;
	public String dcId = null;
	public Map detailMap = new TreeMap();
	private Set gameIdSet = new TreeSet();

	private Map invoiceMap = new TreeMap();

	private Set packNbrSet = new TreeSet();
	public String transactionDate = null;

	private String type = null;

	public GenerateDeliveryForAgentByHtmlHelper(String type, int orgId) {
		logger.debug("type is ========= " + type);
		this.type = type;
		this.currentUserorgId = orgId;
	}

	public int getAgentOrgId(String id) {
		Connection conn = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet result = null;
		int organizationId = -1;
		try {
			pstmt = conn
					.prepareStatement("select distinct(um.organization_id) from st_lms_user_master um, st_se_agent_retailer_transaction art where  (art.agent_user_id = um.user_id) and  exists (select rtm.transaction_id 'tid' from st_lms_agent_receipts_trn_mapping rtm  where rtm.receipt_id="
							+ id
							+ " and rtm.transaction_id= art.transaction_id)");
			result = pstmt.executeQuery();
			if (result.next()) {
				organizationId = result.getInt("organization_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Exception: " + e);
		} finally {
			try {
				result.close();
				pstmt.close();
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}

		}
		return organizationId;
	}

	public Map getInvoiceDetail(int id) {

		con = DBConnect.getConnection();
		String query = QueryManager.getST_AGENT_INVOICE_DETAILS();
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "AGENT");
				pstmt.setInt(3, id);
			} else {
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "RETAILER");
				pstmt.setInt(3, id);
			}
			logger.debug("query" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			InvoiceGameDetailBean gameDetailBean = null;
			String gameName = null;
			boolean flag = true;
			boolean elseFlag = true;
			int i = rs.getFetchSize();
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
					getInvoiceForCustomerDetail(rs.getInt("retailer_org_id"),
							con);
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
				.prepareStatement("select aa.generated_invoice_id 'invoiceId', aa.generated_del_challan_id 'dcId', bb.order_id, 	cc.order_date from ( 	select generated_invoice_id, generated_del_challan_id  	from st_se_agent_invoice_delchallan_mapping 	where id=( 	select idm.id 	from st_lms_agent_receipts rgm, st_se_agent_invoice_delchallan_mapping idm  		where rgm.receipt_id=? 	and rgm.generated_id =idm.generated_del_challan_id) )aa, ( select  distinct order_id  from st_se_agent_order_invoices where invoice_id= (select idm.id from st_lms_agent_receipts rgm, st_se_agent_invoice_delchallan_mapping idm  where rgm.receipt_id=? and rgm.generated_id =idm.generated_del_challan_id) )bb, ( select order_date from st_se_agent_order where order_id=(select  distinct order_id  from st_se_agent_order_invoices where invoice_id=(select idm.id from st_lms_agent_receipts rgm, st_se_agent_invoice_delchallan_mapping idm  where rgm.receipt_id=? and rgm.generated_id =idm.generated_del_challan_id) ) )cc");
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
		logger.debug("map on agent when sale  : " + detailMap);
	}

	private void getInvoiceIDFORSaleRet(int id, Connection conn)
			throws SQLException, ParseException {
		Connection con = conn;
		if (conn == null) {
			conn = DBConnect.getConnection();
		}
		PreparedStatement pstmt = con
				.prepareStatement("select rgm.generated_id 'credit_note' , generated_invoice_id 'sale_ret' from st_lms_agent_receipts rgm, st_se_agent_invoice_delchallan_mapping idm   where rgm.receipt_id=? and rgm.generated_id =idm.generated_del_challan_id ");
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
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		String query = QueryManager.getST_AGENT_SALE_RETURN_DETAILS();
		try {
			List<Integer> tids = getTransactionIds(con, id);
			for (Integer tid : tids) {
				gameIdSet = new TreeSet();
				packNbrSet = new TreeSet();
				bookNbrList = new ArrayList();
				invoiceMap = new TreeMap();
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setInt(1, currentUserorgId);
				pstmt.setString(2, "AGENT");
				pstmt.setInt(3, tid);

				logger.debug("query" + pstmt);
				ResultSet rs = pstmt.executeQuery();
				InvoiceGameDetailBean gameDetailBean = null;
				String gameName = null;
				boolean flag = true;
				boolean elseFlag = true;
				// gameDetailBean.setSalCommVar(FormatNumber.formatNumber(rs.getDouble("transacrion_sale_comm_rate")));
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
						getInvoiceForCustomerDetail(rs
								.getInt("retailer_org_id"), con);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Exception: " + e);
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
				.prepareStatement("select rtm.transaction_id 'tid' from st_lms_agent_receipts_trn_mapping rtm where rtm.receipt_id="
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
