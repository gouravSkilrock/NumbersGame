package com.skilrock.lms.coreEngine.inventoryMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
	public LinkedHashMap<String, String> getvoucherDetail(int dlId) {
		LinkedHashMap<String, String> voucherDetailMap=new LinkedHashMap<String, String>();
		Connection con=DBConnect.getConnection();
		String voucherQuery="select generated_dl_id,task_id,inventory_type from st_lms_inv_dl_task_mapping m,st_lms_inv_dl_detail d" +
				" where d.dl_id= "+dlId+" and d.dl_id=m.dl_id group by inventory_type";
		try{
		PreparedStatement pstmt=con.prepareStatement(voucherQuery);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			if("CONS".equalsIgnoreCase(rs.getString("inventory_type"))){
				voucherDetailMap.put("dcId", rs.getString("generated_dl_id"));
				String getDetData="select date order_date,current_owner_id from st_lms_cons_inv_detail where task_id="+ rs.getInt("task_id");
				Statement stmt=con.createStatement();
				ResultSet rs1=stmt.executeQuery(getDetData);
				if(rs1.next()){
				voucherDetailMap.put("transactionDate", new SimpleDateFormat("dd/MM/yyyy")
				.format(sqlDateToutilDate(rs1.getDate("order_date"))));
				voucherDetailMap.put("currentOwnerId", rs1.getString("current_owner_id"));
				}
			break;	
			}else{
				voucherDetailMap.put("dcId", rs.getString("generated_dl_id"));
				String getDetData="select date order_date,current_owner_id from st_lms_inv_detail where task_id="+ rs.getInt("task_id");
				Statement stmt=con.createStatement();
				ResultSet rs1=stmt.executeQuery(getDetData);
				if(rs1.next()){
				voucherDetailMap.put("transactionDate", new SimpleDateFormat("dd/MM/yyyy")
				.format(sqlDateToutilDate(rs1.getDate("order_date"))));
				voucherDetailMap.put("currentOwnerId", rs1.getString("current_owner_id"));
				}
			break;	
			}
		}
		
		}
		catch (Exception e) {
	e.printStackTrace();
		}
		return voucherDetailMap;
	}
	public LinkedHashMap<String,Map<String,String>> getInvoiceDetail(int id) {
		Set gameIdSet = new TreeSet();
		Set packNbrSet = new TreeSet();
		Map invoiceMap = new TreeMap();
		LinkedHashMap<String,Map<String,String>> deliveryNoteMap= new LinkedHashMap<String, Map<String,String>>();
		List bookNbrList = new ArrayList();
		con = DBConnect.getConnection();
		
		String nonConQuery = "SELECT im.inv_name, ibm.brand_name, imm.model_name, id.serial_no FROM st_lms_inv_master im INNER JOIN st_lms_inv_brand_master ibm ON im.inv_id = ibm.inv_id INNER JOIN st_lms_inv_model_master imm ON ibm.brand_id = imm.brand_id INNER JOIN st_lms_inv_detail id ON imm.model_id = id.inv_model_id INNER JOIN st_lms_inv_dl_task_mapping idltm ON idltm.task_id = id.task_id WHERE idltm.inventory_type='NON_CONS' AND idltm.dl_id = " + id;
		
//		String nonConQuery = "select (select brand_name from st_lms_inv_brand_master b where brand_id=m.brand_id) brand_name," +
//				"model_name,(select inv_name from st_lms_inv_master i where i.inv_id=m.inv_id)inv_name,serial_no from " +
//				"(select inv_model_id,serial_no from st_lms_inv_detail where task_id in (select task_id from st_lms_inv_dl_task_mapping where dl_id =" +id+ " and inventory_type='NON_CONS')) det,st_lms_inv_model_master m " +
//				"where det.inv_model_id=m.model_id";
		try {
			LinkedHashMap<String,String> serialMap=new LinkedHashMap<String, String>();
			StringBuilder serialList;
			Statement stmt =con.createStatement();
			
			logger.debug("query" + stmt);
			ResultSet rs = stmt.executeQuery(nonConQuery);
			//InvoiceGameDetailBean gameDetailBean = null;
		
			while (rs.next()) {
				String deliveryNoteKey=rs.getString("inv_name")+" - "+rs.getString("brand_name")+" - "+rs.getString("model_name");
				
		if(serialMap.containsKey(deliveryNoteKey)){
			
			serialList=new StringBuilder(serialMap.get(deliveryNoteKey));
			serialList.append(","+rs.getString("serial_no"));
			serialMap.put(deliveryNoteKey, serialList.toString());
		}else{
			serialMap.put(deliveryNoteKey, rs.getString("serial_no"));
		}
		
			}
			deliveryNoteMap.put("Non Consumable", serialMap);
			
			String conQuery = "SELECT im.inv_name, cid.quantity FROM st_lms_inv_master im INNER JOIN st_lms_cons_inv_specification cis ON im.inv_id = cis.inv_id INNER JOIN st_lms_cons_inv_detail cid ON cis.inv_model_id = cid.inv_model_id INNER JOIN st_lms_inv_dl_task_mapping idltm ON cid.task_id = idltm.task_id WHERE idltm.inventory_type = 'CONS' AND idltm.dl_id = " + id;
			
//			String conQuery="  select (select inv_name from st_lms_inv_master i where i.inv_id=s.inv_id)inv_name,c.quantity from" +
//					" (select inv_model_id,quantity from st_lms_cons_inv_detail where task_id in (select task_id from st_lms_inv_dl_task_mapping where dl_id = "+id+" and inventory_type='CONS'))c,st_lms_cons_inv_specification s where s.inv_model_id=c.inv_model_id";
          stmt =con.createStatement();
			
			logger.debug("query" + stmt);
			 rs = stmt.executeQuery(conQuery);
			 TreeMap<String,String> quantityMap=new TreeMap<String, String>();
			 while (rs.next()) {
				
				 quantityMap.put(rs.getString("inv_name"), rs.getString("quantity"));
			
				}
			 deliveryNoteMap.put("Consumable", quantityMap);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}  finally {
			try {
				if (!con.isClosed()) {
					con.close();
				}

			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return deliveryNoteMap;
	}

	public CustomerBean getInvoiceForCustomerDetail(int orgId)
			 {
		Connection con = DBConnect.getConnection();
		try{
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
		}}
		catch (Exception e) {
			e.printStackTrace();
		}
return bean;
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
						//getInvoiceForCustomerDetail(rs.getInt("party_id"), con);
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
