package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DLChallanDetails;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.coreEngine.scratchService.common.beans.OrderGameBookBeanMaster;
import com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.daoImpl.ScratchInvoiceDaoImpl;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;

public class BookRecieveRegistrationRetailerHelper {
	static Log logger = LogFactory.getLog(BookRecieveRegistrationRetailerHelper.class);

	public Map<String, List<String>> getBooks(int userOrgId, String challanId) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DBConnect.getConnection();
		Map<String, List<String>> gameBookMap = new HashMap<String, List<String>>();
		List<String> booksList = null;
		String seperator = "-";
		String query=null;
		String dcId=null;
		try{
			stmt=con.createStatement();
			query="select receipt_id from st_lms_agent_receipts where generated_id='"+challanId+"';";
			rs=stmt.executeQuery(query);
			if(rs.next())
				dcId=rs.getString("receipt_id");
			
			query="select gis.game_id,gm.game_nbr,gm.game_name,gis.book_nbr from st_se_game_inv_status gis,st_se_game_master gm where gis.current_owner_id="+userOrgId+" and gis.current_owner='RETAILER' and gis.game_id=gm.game_id and gis.book_status='IN_TRANSIT' and ret_dl_id='"+dcId+"';";
			rs = stmt.executeQuery(query);
			String gameDel = null;
			while (rs.next()) {
				gameDel = rs.getInt("game_id") + seperator
						+ rs.getInt("game_nbr") + seperator
						+ rs.getString("game_name");
				if (gameBookMap.containsKey(gameDel)) {
					booksList.add(rs.getString("book_nbr"));
				} else {
					booksList = new ArrayList<String>();
					gameBookMap.put(gameDel, booksList);
					booksList.add(rs.getString("book_nbr"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return gameBookMap;
	}

	public String[] updateBooks(int userOrgId, int userId, List<String> bookNumberList) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		String[] response = new String[3];
		int gameId = 0;
		String packNbr = null;
		String query = null;
		int warehouseId = -1;
		String invoiceReceipt = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			CommonFunctionsHelper helper = new CommonFunctionsHelper();
			stmt = con.createStatement();

			Map<Integer, List<String>> gameBookMap = new HashMap<Integer, List<String>>();
			List<String> bookList = null;
			for (String bookNbr : bookNumberList) {
				query = "select game_id, pack_nbr, book_nbr, warehouse_id from st_se_game_inv_status where book_nbr='" + bookNbr + "';";
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					warehouseId = rs.getInt("warehouse_id");
					gameId = rs.getInt("game_id");
					packNbr = rs.getString("pack_nbr");

					bookList = gameBookMap.get(gameId);
					if (bookList == null) {
						bookList = new ArrayList<String>();
						gameBookMap.put(gameId, bookList);
					}
					bookList.add(rs.getString("book_nbr"));
				}
				query = "update st_se_game_inv_status set book_status='RECEIVED_BY_RET',book_receive_reg_date_ret='" + new Timestamp(Calendar.getInstance().getTimeInMillis()) + "' where book_nbr = '" + bookNbr + "';";
				stmt.executeUpdate(query);
				helper.updateGameInvDetail(gameId, packNbr, bookNbr, "RETAILER", userOrgId, userId, "RECEIVED_BY_RET", warehouseId, con);
			}

			Map<Integer, OrderGameBookBeanMaster> invoiceMap = new HashMap<Integer, OrderGameBookBeanMaster>();
			query = null;
			for(Map.Entry<Integer, List<String>> entry : gameBookMap.entrySet()) {
				gameId = entry.getKey();
				bookList = entry.getValue();

				OrderGameBookBeanMaster masterBean = null;
				query = "SELECT scheme_type, invoice_method_value FROM st_se_invoicing_methods im INNER JOIN st_se_org_game_invoice_methods gim ON im.invoice_method_id=gim.invoice_method_id WHERE gim.organization_id="+userOrgId+" AND gim.game_id="+gameId+";";
				logger.info("Query - "+query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					if("ON_BOOK_RECEIVE_REG_RET".equals(rs.getString("scheme_type")) && "YES".equals(rs.getString("invoice_method_value"))) {
						masterBean = new OrderGameBookBeanMaster();
						masterBean.setBookList(bookList);
						invoiceMap.put(gameId, masterBean);
					}
				}
			}
			if(invoiceMap.size() > 0) {
				int invoiceId = 0;
				
				ScratchInvoiceDaoImpl daoImpl = new ScratchInvoiceDaoImpl();

				daoImpl.checkAndUpdateForInvoice(invoiceMap, con);
				if(invoiceMap.size() > 0) {
					daoImpl.generateInvoiceForAgent(userOrgId, invoiceMap, null, con);
					invoiceId = daoImpl.generateInvoiceForRetailer(userOrgId, invoiceMap, null, con);
				}

				if(invoiceId > 0) {
					invoiceReceipt = daoImpl.getInvoiceReceiptFromInvoiceId("AGENT", invoiceId, con);
					response[0] = String.valueOf(invoiceId);
					response[1] = invoiceReceipt;
				}
			}
			String firstName=null;
			String lastName=null;
			String emailId=null;
			query="select first_name,last_name,email_id from st_lms_user_contact_details where user_id="+userId+";";
			rs=stmt.executeQuery(query);
			if(rs.next()){
			 	firstName = rs.getString("first_name");
				lastName=rs.getString("last_name");
				emailId=rs.getString("email_id");
			}
			con.commit();
			response[2]="SUCCESS";
			if(invoiceReceipt!=null && invoiceReceipt.length()>0)
				helper.sendMail(bookNumberList,firstName,lastName,emailId,invoiceReceipt);		
		} catch (Exception e) {
			e.printStackTrace();
			con.rollback();	
			response[2] = "FAIL";
		}finally{
			con.close();
		}
		return response;
	}

	public List<DLChallanDetails> fetchAvailableDLChallan(int userOrgId) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		String fetchQuery = null;
		ResultSet rs = null;
		List<DLChallanDetails> challanList = new ArrayList<DLChallanDetails>();
		DLChallanDetails dlBean = null;
		int count = 0;
		try {
			connection = DBConnect.getConnection();
			fetchQuery = "select distinct(slar.generated_id),slar.voucher_date "
					+ "from st_lms_agent_receipts slar INNER JOIN st_se_game_inv_status ssgis on slar.receipt_id = ssgis.ret_dl_id "
					+ "where party_id = " + userOrgId + " and ssgis.book_status = 'IN_TRANSIT';";
			logger.info("Query : " + fetchQuery);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(fetchQuery);
			if (!rs.next()) {
				throw new LMSException(LMSErrors.SCRATCH_NO_CHALLAN_AVAILABLE_TO_RECEIVE_ERROR_CODE, LMSErrors.SCRATCH_NO_CHALLAN_AVAILABLE_TO_RECEIVE_ERROR_MESSAGE);
			}
			rs.beforeFirst();
			while (rs.next()) {
				dlBean = new DLChallanDetails();
				dlBean.setChallanID(++count);
				dlBean.setDlChallanNumber(rs.getString("generated_id"));
				dlBean.setDlDate(rs.getString("voucher_date").replace(".0", ""));
				challanList.add(dlBean);
			}
		}catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			logger.error("Some Generic exception occurred while fetching Challan Details : ",e);
		}finally {
			DBConnect.closeConnection(connection, stmt, rs);
		}
		return challanList;
	}
}