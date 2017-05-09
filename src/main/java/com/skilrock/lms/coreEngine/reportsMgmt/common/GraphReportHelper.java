
package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.AgtLedAccDetailsBean;
import com.skilrock.lms.beans.RetLedAccDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.web.reportsMgmt.common.GraphReportAction;
import com.skilrock.lms.web.reportsMgmt.common.LedgerAction;

/**
 * This Helper class is for Generating the Reports in Pdf format using Jasper
 * Reports
 * 
 * @author Skilrock Technologies
 * 
 */

public class GraphReportHelper {
	private static Log logger = LogFactory.getLog(GraphReportHelper.class);

	public static void main(String[] args) {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		Timestamp fromDate = new Timestamp(new Date().getTime());
		from.setTimeInMillis(fromDate.getTime());
		SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");

		System.out.println("frTimeList " + formatDB.format(from.getTime()));
	}

	byte bytes[] = null;
	Connection connection = null;
	String EXPORT_LOCATION = null;
	JasperPrint jasperPrint;
	JasperReport jasperReport;
	String newfile = null;
	PreparedStatement Pstmt = null;
	ResultSet resultSet = null;
	ResultSet resultSet1 = null;
	ResultSet resultSet2 = null;
	Statement statement = null;
	PreparedStatement statement2 = null;

	String str = null;

	public void createTempChqReceipt(String receipt_id, String boOrgName,
			int userOrgID, String root_path) {
		String query = null;
		String file = null;

		HashMap parameterMap = new HashMap();

		System.out
				.println("======================= reciept_id : " + receipt_id);
		System.setProperty("java.awt.headless", "true");
		String orgAdd = null;
		String addQuery = null;
		String vatNum = null;
		LedgerHelper ledger = new LedgerHelper();
		String timeFormat = null;
		String currencySymbol = null;
		String decimalFormat = null;
		String dateFormat = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			addQuery = QueryManager.getST6AddressQuery();
			orgAdd = ledger.getAddress(addQuery, "" + userOrgID, null);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("header", boOrgName);

			vatNum = ledger.getVatNumber("" + userOrgID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("inside helper vat number");
			e.printStackTrace();
		}
		parameterMap.put("vatNum", vatNum);
		parameterMap.put("receipt_id", receipt_id);
		// query = "select receipt_type,id from st_lms_bo_receipts where id
		// ='"+receipt_id+"'";

		System.out
				.println("================== inside reciept =================");
		// query="select
		// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
		// ,comm_amount from st_lms_organization_master som ,(select 'cash'
		// t_type,cash_agent as agent,cash_amount as amount ,cash_amount as
		// net_amount ,0.00 comm_amount ,cash_trans_id as trans_id, 'cash'
		// issuing_party_name,'cash' cheque_nbr,'cash' cheque_date, 'cash'
		// drawee_bank,cash_id as id from ( select id as
		// cash_id,sbct.agent_org_id as cash_agent,sbct.amount as cash_amount
		// ,sbct.transaction_id as cash_trans_id from st_lms_bo_cash_transaction
		// sbct, (select id,transaction_id from st_lms_bo_receipts_trn_mapping
		// where id= '"+receipt_id+"') innermost where
		// sbct.transaction_id=innermost.transaction_id) cash union select 'pwt'
		// t_type,pwt_agent as agent,pwt_amount as amount,net_amount
		// ,comm_amount ,pwt_trans_id as trans_id,'pwt' issuing_party_name,'pwt'
		// cheque_nbr, 'pwt' cheque_date, 'pwt' drawee_bank , pwt_id as id from
		// (select id as pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt)
		// as pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
		// comm_amount,sbp.transaction_id as pwt_trans_id from st_se_bo_pwt sbp,
		// (select id,transaction_id from st_lms_bo_receipts_trn_mapping where
		// id= '"+receipt_id+"') innermost where
		// sbp.transaction_id=innermost.transaction_id group by
		// sbp.transaction_id) pwt union select 'cheque' t_type,cheque_agent as
		// agent,cheque_amt as amount,cheque_amt as net_amount ,0.00
		// comm_amount,cheque_id as
		// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id
		// from ( select id ,agent_org_id as
		// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
		// as cheque_id from st_lms_bo_sale_chq sbsc, (select id,transaction_id
		// from st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
		// innermost where sbsc.transaction_id=innermost.transaction_id) cheque)
		// result where result.agent = som.organization_id";
		query = "select  name,addr_line1,addr_line2,city,cheque_nbr,cheque_date,cheque_receiving_date,issuing_party_name,drawee_bank,cheque_amt from st_lms_bo_cheque_temp_receipt sbctr, st_lms_organization_master som  where temp_receipt_id='"
				+ receipt_id
				+ "' and (sbctr.agent_org_id = som.organization_id)";
		System.out.println(" RECEIPT queryyyyyyyyyyyyyyyy BOOOOO " + query);
		file = root_path
				+ "com/skilrock/lms/web/reportsMgmt/compiledReports/TempReceipt.jasper";
		generateReport(query, file, parameterMap, null, null, null, null);

		System.out.println("Query -- " + query);

	}

	/**
	 * This method checks which type of Receipt is to be Generated at Agent End
	 * and send the data to Graph Report Method for generation of Pdf Receipts
	 * 
	 * @param receipt_id
	 *            as Integer
	 * @param root_path
	 *            as String
	 * @param agent_name
	 *            as String
	 */
	public void createTextReportAgent(int receipt_id, String root_path,
			int userOrgID, String agent_name) {
		String receipt_type = null;

		connection = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String file = null;
		HashMap parameterMap = new HashMap();
		String orgAdd = null;
		String addQuery = null;
		String vatQuery = null;
		String vatNum = null;
		String receipt_no = null;
		String trnType = null;
		String fileName = null;
		String isVatApplicable = null;
		String isCommApplicable = null;
		LedgerHelper ledger = new LedgerHelper();
		addQuery = QueryManager.getST6AddressQuery();
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String tdsPartyName = null;
		String vatPartyName = null;
		String govComPartyName = null;
		String textForTax = null;
		Timestamp voucher_date = null;
		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			orgAdd = ledger.getAddress(addQuery, "" + userOrgID, null);

			parameterMap.put("orgAdd", orgAdd);

			System.out.println("createTextReportAgent  ))))))))))))");
			System.setProperty("java.awt.headless", "true");
			parameterMap.put("header", agent_name);

			vatNum = ledger.getVatNumber("" + userOrgID);
			parameterMap.put("vatNum", vatNum);
			stmt = connection.createStatement();
			// query = "select receipt_type,id from st_lms_agent_receipts where
			// id ='"+receipt_id+"'";
			// query = "select receipt_type,id,generated_id from
			// st_lms_agent_receipts_gen_mapping where id='"+receipt_id+"'";
			// query="select
			// sbtm.transaction_id,transaction_type,sbrgm.receipt_type,sbrgm.generated_id
			// from st_lms_agent_receipts_trn_mapping
			// sbrtm,st_lms_agent_transaction_master
			// sbtm,st_lms_agent_receipts_gen_mapping sbrgm where
			// sbrgm.id='"+receipt_id+"' and
			// sbrtm.transaction_id=sbtm.transaction_id and sbrgm.id=sbrtm.id";
			query = "select sbtm.transaction_id,transaction_type,sbr.voucher_date,sbr.receipt_type,sbr.generated_id from st_lms_agent_receipts_trn_mapping sbrtm,st_lms_agent_transaction_master sbtm,st_lms_agent_receipts sbr where sbr.receipt_id='"
					+ receipt_id
					+ "' and  sbrtm.transaction_id=sbtm.transaction_id and sbr.receipt_id=sbrtm.receipt_id";
			rs = stmt.executeQuery(query);
			System.out.println(rs.getFetchSize() + ":::: " + query);
			while (rs.next()) {
				System.out.println("inside whileeeeeeeeeeeee"
						+ rs.getString("receipt_type"));
				receipt_type = rs.getString("receipt_type");
				receipt_no = rs.getString("generated_id");
				trnType = rs.getString("transaction_type");
				voucher_date = rs.getTimestamp("voucher_date");
			}
			System.out.println("receipt type is  " + receipt_type);
			root_path = root_path.replace("\\", "/");
			parameterMap.put("receipt_no", receipt_no);
			parameterMap.put("voucher_date", voucher_date);
			isVatApplicable = (String) ServletActionContext.getServletContext()
					.getAttribute("VAT_APPLICABLE");
			isCommApplicable = (String) ServletActionContext
					.getServletContext().getAttribute("COMM_APPLICABLE");
			if (receipt_type.equalsIgnoreCase("DG_INVOICE")) {
				query = "select id,game_name,transaction_date,voucher_date,mrp_amt as sale, net_amt,comm_amt,vat,taxable_sale,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select voucher_date,retailer_id,id,game_id ,retailer_comm as comm_amt, mrp_amt, net_amt, vat_amt as vat, taxable_sale,sbs.transaction_id, btm.transaction_date from st_dg_agt_sale sbs, st_lms_agent_transaction_master btm, (select slbr.voucher_date, slbr.party_id as retailer_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
						+ receipt_id
						+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id = sale.retailer_id order by transaction_date";
				System.out.println("DG_INVOICE Query****: " + query);
				if ("DG_SALE".equalsIgnoreCase(trnType)) {
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcRdpVat.jasper";
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if (receipt_type.equalsIgnoreCase("SLE_INVOICE")) {
				query = "select id,game_disp_name game_name,transaction_date,voucher_date,mrp_amt as sale, net_amt,comm_amt,vat,taxable_sale,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_sle_game_master sdgm ,st_lms_organization_master som ,(select voucher_date,retailer_id,id,game_id ,retailer_comm as comm_amt, mrp_amt, net_amt, vat_amt as vat, taxable_sale,sbs.transaction_id, btm.transaction_date from st_sle_agt_sale sbs, st_lms_agent_transaction_master btm, (select slbr.voucher_date, slbr.party_id as retailer_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
						+ receipt_id
						+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id = sale.retailer_id order by transaction_date";
				System.out.println("SLE_INVOICE Query****: " + query);
				if ("SLE_SALE".equalsIgnoreCase(trnType)) {
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "SLESaleInvcComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "SLESaleInvcComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "SLESaleInvcRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "SLESaleInvcRdpVat.jasper";
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if(receipt_type.equalsIgnoreCase("CS_INVOICE")){
			if ("CS_SALE".equalsIgnoreCase(trnType)) {
				query = "select id,product_code,description,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_cs_product_master scpm ,st_lms_organization_master som ,(select retailer_id,voucher_date,id,product_id ,(mrp_amt*retailer_comm/100) as comm_amt, mrp_amt, net_amt, vat_amt as vat, sbs.transaction_id, btm.transaction_date from st_cs_agt_sale sbs, st_lms_agent_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as retailer_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+
				+ receipt_id
				+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.product_id = scpm.product_id and scpm.product_id and som.organization_id = sale.retailer_id order by transaction_date";
				System.out
						.println(" CS_INVOICE queryyyyyyyyyyyyyyyy AGTTTTTTTTTT "
								+ query);
				if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcComVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcComNoVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcRdpNoVat.jasper";// changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcRdpVat.jasper";//
				}
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			if (receipt_type.equalsIgnoreCase("OLA_INVOICE")) {
				if ("OLA_DEPOSIT".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,deposit_amt as deposit, net_amt,(deposit_amt-net_amt) as comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select retailer_org_id,voucher_date,id,wallet_id ,retailer_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_agt_deposit sbs, st_lms_agent_transaction_master btm, (select slbr.party_id as retailer_id,slbr.voucher_date as voucher_date,slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)deposit where deposit.wallet_id = sdgm.wallet_id and som.organization_id = deposit.retailer_org_id";
				System.out.println("OLA_INVOICE Query****: " + query);
				
				fileName = "OLADepositInvcRdpNoVat.jasper";
					
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if (receipt_type.equalsIgnoreCase("OLA_RECEIPT")) {
				if ("OLA_WITHDRAWL".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,withdrawl_amt as withdrawal, net_amt,(withdrawl_amt-net_amt) as comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select retailer_org_id,voucher_date,id,wallet_id ,retailer_comm as comm_amt, withdrawl_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_agt_withdrawl sbs, st_lms_agent_transaction_master btm, (select slbr.party_id as retailer_id,slbr.voucher_date as voucher_date,slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)deposit where deposit.wallet_id = sdgm.wallet_id and som.organization_id = deposit.retailer_org_id";
				System.out.println("OLA_RECEIPT Query****: " + query);
				
				fileName = "OLAWithdrawalInvcRdpNoVat.jasper";
					
				}else if("OLA_COMMISSION".equalsIgnoreCase(trnType)){
					query = "select id,wallet_name,transaction_date,voucher_date,comm_amt,name,addr_line1,addr_line2,city,netgame.transaction_id as transaction_id ,netgame.tds_comm_rate,net_comm_amt from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select retailer_org_id,voucher_date,id,wallet_id ,ret_claim_comm as net_comm_amt, sbs.transaction_id, btm.transaction_date,sbs.tds_comm_rate ,ret_mrp_comm comm_amt from st_ola_agt_comm sbs, st_lms_agent_transaction_master btm, (select slbr.party_id as retailer_id,slbr.voucher_date as voucher_date,slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id ='"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)netgame where netgame.wallet_id = sdgm.wallet_id and som.organization_id = netgame.retailer_org_id";
					System.out.println("OLA_RECEIPT Query****: " + query);
					fileName = "OLANetGameInvcRdpNoVat.jasper";
				}
					
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if (receipt_type.equalsIgnoreCase("INVOICE")) {
				if ("SALE".equalsIgnoreCase(trnType)) {
					// query="select
					// distinct(transaction_id),retailer_sale_comm_rate,name,addr_line1,addr_line2,city,retailer_org_id,id,game_name
					// , ticket_price, nbr_of_tickets_per_book ,
					// nbr_of_books,taxable_sale,vat_amt,comm_amt, sale,
					// (nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets,transaction_date,order_id,order_date from
					// st_lms_organization_master som ,(select
					// retailer_sale_comm_rate,id,sale.retailer_org_id,game_name
					// , ticket_price, nbr_of_tickets_per_book ,
					// sale.nbr_of_books, (net_amt) as
					// sale,taxable_sale,vat_amt,comm_amt,transaction_date,sale.order_id,order_date,transaction_id
					// from st_se_game_master ,st_se_agent_order sao,(select
					// sbat.retailer_org_id,id,sbat.game_id ,
					// nbr_of_books,vat_amt,comm_amt,taxable_sale,net_amt,sbat.transaction_id,transaction_date,order_id
					// from st_se_agent_retailer_transaction sbat
					// ,st_se_agent_order_invoices saoi,(select receipt_id as
					// id,transaction_id from st_lms_agent_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"') innermost,(select
					// transaction_date,transaction_id from
					// st_lms_agent_transaction_master )satm where
					// sbat.transaction_id = innermost.transaction_id and
					// innermost.transaction_id=satm.transaction_id and
					// saoi.invoice_id=innermost.id ) sale where sale.game_id =
					// st_se_game_master.game_id and sao.order_id=sale.order_id
					// )result1 where
					// som.organization_id=result1.retailer_org_id";
//					query = "select distinct(transaction_id),retailer_sale_comm_rate,name,addr_line1,addr_line2,city,retailer_org_id,id,game_name , ticket_price, nbr_of_tickets_per_book , nbr_of_books,taxable_sale,vat_amt,comm_amt,  sale, (nbr_of_tickets_per_book*nbr_of_books)as total_tickets,transaction_date,order_id,order_date from  st_lms_organization_master som ,(select retailer_sale_comm_rate,id,sale.retailer_org_id,game_name , ticket_price, nbr_of_tickets_per_book , sale.nbr_of_books, (net_amt) as sale,taxable_sale,sale.vat_amt,comm_amt,transaction_date,sale.order_id,order_date,transaction_id from st_se_game_master ,st_se_agent_order sao,(select sbat.retailer_org_id,id,sbat.game_id , nbr_of_books,vat_amt,comm_amt,taxable_sale,net_amt,sbat.transaction_id,transaction_date,order_id from st_se_agent_retailer_transaction sbat ,st_se_agent_order_invoices saoi,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id = '"
//							+ receipt_id
//							+ "') innermost,(select transaction_date,transaction_id from st_lms_agent_transaction_master )satm where sbat.transaction_id = innermost.transaction_id and  innermost.transaction_id=satm.transaction_id  and saoi.invoice_id=innermost.id ) sale where sale.game_id = st_se_game_master.game_id  and sao.order_id=sale.order_id )result1 where som.organization_id=result1.retailer_org_id order by transaction_date";
					
					query = "select distinct(transaction_id),retailer_sale_comm_rate,name,addr_line1,addr_line2,city,retailer_org_id,id,game_name , ticket_price, nbr_of_tickets_per_book , nbr_of_books,taxable_sale,vat_amt,comm_amt,  sale, (nbr_of_tickets_per_book*nbr_of_books)as total_tickets,transaction_date, 'NA' order_id, 'NA' order_date from  st_lms_organization_master som ,(select retailer_sale_comm_rate,id,sale.retailer_org_id,game_name , ticket_price, nbr_of_tickets_per_book , sale.nbr_of_books, (net_amt) as sale,taxable_sale,sale.vat_amt,comm_amt,transaction_date,transaction_id from st_se_game_master,(select sbat.retailer_org_id,id,sbat.game_id , nbr_of_books,vat_amt,comm_amt, taxable_sale, net_amt, sbat.transaction_id, transaction_date from st_se_agent_retailer_transaction sbat,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "') innermost,(select transaction_date,transaction_id from st_lms_agent_transaction_master)satm where sbat.transaction_id = innermost.transaction_id and  innermost.transaction_id=satm.transaction_id) sale where sale.game_id = st_se_game_master.game_id)result1 where som.organization_id=result1.retailer_org_id order by transaction_date";
					System.out.println(" INVOICE QUERYYYY" + query);

					String addressInvoice = getAddress(query);
					parameterMap.put("address", addressInvoice);
					// file=root_path+"com/skilrock/lms/web/reportsMgmt/compiledReports/InvoiceRet.jasper";
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcRdpVat.jasper";
					}
				}  else if ("LOOSE_SALE".equalsIgnoreCase(trnType)) {
					
					query = "select id,game_name,organization_id,transaction_date,nbr_of_tickets nbrOfTickets, net_amt sale, retailer_sale_comm_rate agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,retComm agtSaleCommRate from st_lms_organization_master som ,(select retailer_org_id,id,game_name ,retailer_sale_comm_rate,sbs.nbr_of_tickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,retComm from st_se_agent_ret_loose_book_transaction sbs,st_se_game_master game, st_lms_agent_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+ receipt_id+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.retailer_org_id";
					System.out.println(" INVOICE QUERYYYY" + query);

					String addressInvoice = getAddress(query);
					parameterMap.put("address", addressInvoice);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcRdpVat.jasper";
					}
				}
				
				else {
					// query=" select id,game_name as name,taxable_sale,
					// vat,net_amt as sale,comm_amt,transaction_id from
					// st_dg_game_master sdgm ,(select id,game_id
					// ,retailer_org_id, agent_comm as comm_amt, mrp_amt,
					// vat_amt as vat,taxable_sale,net_amt,sbat.transaction_id
					// from st_dg_agt_sale sbat,(select receipt_id as
					// id,transaction_id from st_lms_agent_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' ) innermost where
					// sbat.transaction_id = innermost.transaction_id )sale
					// where sale.game_id = sdgm.game_id";
					query = "select name,addr_line1,addr_line2,city,game_name,taxable_sale,vat,sale,comm_amt from(select id,retailer_org_id,game_name,taxable_sale, vat,net_amt as sale,comm_amt,transaction_id from st_dg_game_master sdgm ,(select id,game_id ,retailer_org_id, agent_comm as comm_amt, mrp_amt, vat_amt as vat,taxable_sale,net_amt,sbat.transaction_id from  st_dg_agt_sale sbat,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id )sale where sale.game_id = sdgm.game_id)tab1 , st_lms_organization_master as som where tab1.retailer_org_id = som.organization_id";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcRdpVat.jasper";
					}
				}

				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("DG_RECEIPT")) {
				query = "select id,game_nbr,game_name,t_type,transaction_date,pwt_amt as amount, net_amt as net_amount,comm_amt as comm_amount,name,addr_line1,addr_line2,city,sale.transaction_id as trans_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select 'pwt' t_type, retailer_id,id,game_id ,comm_amt, pwt_amt, net_amt,sbat.transaction_id,btm.transaction_date from  st_dg_agt_pwt sbat,st_lms_agent_transaction_master btm,(select slbr.party_id as retailer_id, slbr.receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
						+ receipt_id
						+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id=sale.retailer_id order by transaction_date";
				System.out
						.println("DG RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGNewPWTReceipt.jasper";
				generateReport(query, file, parameterMap, null, null);
			} 
			else if (receipt_type.equalsIgnoreCase("SLE_RECEIPT")) {
				query = "select id,game_no game_nbr,game_disp_name game_name,t_type,transaction_date,pwt_amt as amount, net_amt as net_amount,comm_amt as comm_amount,name,addr_line1,addr_line2,city,sale.transaction_id as trans_id from st_sle_game_master sdgm ,st_lms_organization_master som ,(select 'pwt' t_type, retailer_id,id,game_id ,comm_amt, pwt_amt, net_amt,sbat.transaction_id,btm.transaction_date from  st_sle_agt_pwt sbat,st_lms_agent_transaction_master btm,(select slbr.party_id as retailer_id, slbr.receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
						+ receipt_id
						+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id=sale.retailer_id order by transaction_date";
				System.out
						.println("SLE RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/SLENewPWTReceipt.jasper";
				generateReport(query, file, parameterMap, null, null);
			} 
			else if(receipt_type.equalsIgnoreCase("RECEIPT") && trnType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				query = "select id,wallet_name,transaction_date,commission_calculated as commission_calculated,name,addr_line1,addr_line2,city,netGaming.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select ret_org_id,id,wallet_id ,commission_calculated,sbs.transaction_id, btm.transaction_date from st_ola_agt_ret_commission sbs, st_lms_agent_transaction_master btm, (select slbr.party_id as retailer_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)netGaming where netGaming.wallet_id = sdgm.wallet_id and som.organization_id = netGaming.ret_org_id";
				System.out
						.println(" RECEIPT OLA COMMISSION queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/OLAReceipt.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			else if (receipt_type.equalsIgnoreCase("RECEIPT")) {
				System.out.println("***************");
				if (trnType.equalsIgnoreCase("PWT")
						|| trnType.equalsIgnoreCase("PWT_AUTO")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr,game_name from
					// st_lms_organization_master som ,(select 'pwt'
					// t_type,pwt_agent as agent,pwt_amount as amount,net_amount
					// ,comm_amount ,pwt_trans_id as trans_id, pwt_id as
					// id,transaction_date,game_nbr,game_name from (select id as
					// pwt_id,sbp.retailer_org_id as pwt_agent,sum(sbp.pwt_amt)
					// as pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr,game_name
					// from st_se_agent_pwt sbp,(select
					// game_nbr,game_id,game_name from st_se_game_master
					// )sgm,(select id,transaction_id from
					// st_lms_agent_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_agent_transaction_master )sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id )
					// pwt)result where result.agent = som.organization_id";
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,net_amount ,comm_amount,transaction_date,game_nbr,game_name from st_lms_organization_master som ,(select 'pwt' t_type,pwt_agent  as agent,pwt_amount as amount,net_amount ,comm_amount ,pwt_trans_id as trans_id, pwt_id as id,transaction_date,game_nbr,game_name from (select id as pwt_id,sbp.retailer_org_id as pwt_agent,sum(sbp.pwt_amt) as pwt_amount,sum(sbp.net_amt)as net_amount   ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as pwt_trans_id,transaction_date,sbp.game_id,game_nbr,game_name from   st_se_agent_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master )sgm,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id,transaction_date from st_lms_agent_transaction_master )sbtm where sbp.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id group by sbp.transaction_id ) pwt)result where result.agent = som.organization_id order by transaction_date";
					// "select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr from
					// st_lms_organization_master som , (select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_amount as net_amount ,0.00 comm_amount
					// ,cash_trans_id as trans_id,'cash'
					// issuing_party_name,'cash' cheque_nbr,'cash' cheque_date,
					// 'cash' drawee_bank,cash_id as id,transaction_date,'cash'
					// game_nbr from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct, (select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost, (select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cash union
					// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
					// amount,net_amount ,comm_amount ,pwt_trans_id as
					// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
					// cheque_date, 'pwt' drawee_bank , pwt_id as
					// id,transaction_date,game_nbr from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr from
					// st_se_bo_pwt sbp,(select game_nbr,game_id from
					// st_se_game_master )sgm, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id) pwt
					// union select 'cheque' t_type,cheque_agent as
					// agent,cheque_amt as amount,cheque_amt as net_amount,0.00
					// comm_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// , 'cheque' game_nbr from (select id ,agent_org_id as
					// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id) cheque)
					// result where result.agent = som.organization_id";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/PWTReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("DG_PWT")
						|| trnType.equalsIgnoreCase("DG_PWT_AUTO")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr,game_name from
					// st_lms_organization_master som ,(select 'pwt'
					// t_type,pwt_agent as agent,pwt_amount as amount,net_amount
					// ,comm_amount ,pwt_trans_id as trans_id, pwt_id as
					// id,transaction_date,game_nbr,game_name from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr,game_name
					// from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name
					// from st_se_game_master )sgm,(select id,transaction_id
					// from st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id )
					// pwt)result where result.agent = som.organization_id";
					// "select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr from
					// st_lms_organization_master som , (select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_amount as net_amount ,0.00 comm_amount
					// ,cash_trans_id as trans_id,'cash'
					// issuing_party_name,'cash' cheque_nbr,'cash' cheque_date,
					// 'cash' drawee_bank,cash_id as id,transaction_date,'cash'
					// game_nbr from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct, (select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost, (select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cash union
					// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
					// amount,net_amount ,comm_amount ,pwt_trans_id as
					// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
					// cheque_date, 'pwt' drawee_bank , pwt_id as
					// id,transaction_date,game_nbr from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr from
					// st_se_bo_pwt sbp,(select game_nbr,game_id from
					// st_se_game_master )sgm, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id) pwt
					// union select 'cheque' t_type,cheque_agent as
					// agent,cheque_amt as amount,cheque_amt as net_amount,0.00
					// comm_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// , 'cheque' game_nbr from (select id ,agent_org_id as
					// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id) cheque)
					// result where result.agent = som.organization_id";
					query = "select name,addr_line1,addr_line2,city,t_type,trans_id,receipt_id as id,amount,net_amount ,comm_amt,game_nbr,game_name,ret_orgId from st_lms_organization_master som ,(select 'pwt' t_type,pwt_amount as amount,ret_orgId,net_amount ,comm_amt , receipt_id,game_nbr,game_name from (select receipt_id,sbp.retailer_org_id as ret_orgId,sbp.pwt_amt as pwt_amount, sbp.net_amt as net_amount ,sbp.comm_amt as comm_amt,sbp.transaction_id as pwt_trans_id, sbp.game_id,game_nbr,game_name from st_dg_agt_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master  )sgm,(select receipt_id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id from st_lms_agent_transaction_master)sbtm where sbp.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id and retailer_org_id='') pwt )result where result.ret_orgId = som.organization_id";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGPWTReceipt.jasper";
				}

				else if (trnType.equalsIgnoreCase("cheque")) {
					// query=" select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date,temp_receipt_id
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date,temp_receipt_id from
					// st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt
					// sbctr,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbctr.cheque_nbr=sbsc.cheque_nbr ) cheque ) result where
					// result.agent = som.organization_id";
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// from (select id ,sbsc.retailer_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_agent_sale_chq
					// sbsc,(select id,transaction_id from
					// st_lms_agent_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_agent_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cheque )
					// result where result.agent = som.organization_id";
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,user_name,cheque_nbr as cheque_nbr,cheque_date,drawee_bank,amount,transaction_date from  st_lms_organization_master som ,(select 'cheque' t_type,cheque_agent as agent,cheque_amt as amount,user_name,cheque_amt as net_amount,cheque_id as trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date  from (select id ,slum.user_name,sbsc.retailer_org_id as cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id as cheque_id,transaction_date from st_lms_agent_sale_chq sbsc,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= "
							+receipt_id+
							") innermost,(select user_id,transaction_id,transaction_date from st_lms_agent_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum where sbsc.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and slum.user_id=sbtm.user_id  ) cheque ) result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT CASH queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/ChequeReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("CASH")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_trans_id as trans_id,cash_id as id,transaction_date
					// from ( select id as cash_id,sbct.retailer_org_id as
					// cash_agent,sbct.amount as cash_amount
					// ,sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_agent_cash_transaction sbct,(select
					// id,transaction_id from st_lms_agent_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_agent_transaction_master )sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id )
					// cash)result where result.agent = som.organization_id";
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,user_name,amount,transaction_date from st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,cash_amount as amount ,user_name,cash_trans_id as trans_id,cash_id as id,transaction_date from  ( select id as cash_id,sbct.retailer_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as cash_trans_id,slum.user_name,transaction_date from  st_lms_agent_cash_transaction sbct,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= "
							+receipt_id+
							") innermost,(select user_id,transaction_id,transaction_date from st_lms_agent_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id) cash)result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT CASH queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
				}else if (trnType.equalsIgnoreCase("bank_deposit")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,user_name,amount,transaction_date from  st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,user_name,cash_amount as amount ,trans_id,receipt_id,transaction_date from ( select receipt_id,sbct.retailer_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as trans_id,slum.user_name,transaction_date from  st_lms_agent_bank_deposit_transaction sbct,( select receipt_id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "' ) innermost, (select  user_id,transaction_id,transaction_date from st_lms_agent_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id  ) cash )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT BANK DEPOSIT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
			     }

				generateReport(query, file, parameterMap, null, null);

			} else if (receipt_type.equalsIgnoreCase("DR_NOTE")) {
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// retailer_org_id,id,issuing_party_name,chq_nbr as
				// cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_agent_sale_chq sbsc, (select id,transaction_id
				// from st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.retailer_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,issuing_party_name,cheque_nbr,amount,transaction_type,remarks,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som , (select
				// retailer_org_id,id,issuing_party_name,chq_nbr as
				// cheque_nbr,cheque_date,drawee_bank,amount,
				// sbdn.transaction_type,remarks ,cheque_amt,sbsc.transaction_id
				// from st_lms_agent_sale_chq sbsc,(select
				// amount,transaction_type,remarks,transaction_id from
				// st_lms_agent_debit_note) sbdn, (select id,transaction_id from
				// st_lms_agent_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbsc.transaction_id=innermost.transaction_id
				// and sbdn.transaction_id =innermost.transaction_id )result1
				// where som.organization_id=result1.retailer_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,issuing_party_name,chq_nbr
				// as cheque_nbr
				// ,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// retailer_org_id,id,issuing_party_name, chq_nbr, cheque_date,
				// drawee_bank, cheque_amt as amount,transaction_id,
				// transaction_type,remarks from (select 0.00
				// amount,id,transaction_type,'Cheque
				// Bounce'remarks,sbsc.transaction_id,retailer_org_id,
				// issuing_party_name, chq_nbr, cheque_date,drawee_bank,
				// cheque_amt from st_lms_agent_sale_chq sbsc,(select
				// id,transaction_id from st_lms_agent_receipts_trn_mapping
				// where id= '"+receipt_id+"')innermost where
				// innermost.transaction_id=sbsc.transaction_id )cheque union
				// select retailer_org_id,id,'N.A.'issuing_party_name,'N.A.'
				// chq_nbr,'N.A.' cheque_date,'N.A.' drawee_bank,
				// amount,transaction_id,transaction_type,remarks from (select
				// amount,id,transaction_type,remarks,sbdn.transaction_id,retailer_org_id
				// from st_lms_agent_debit_note sbdn,(select id,transaction_id
				// from st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost where
				// innermost.transaction_id=sbdn.transaction_id )bounce) result1
				// where som.organization_id=result1.retailer_org_id ";
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,issuing_party_name,chq_nbr
				// as cheque_nbr
				// ,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// retailer_org_id,id,issuing_party_name, chq_nbr, cheque_date,
				// drawee_bank, cheque_amt as amount,transaction_id,
				// transaction_type,remarks,transaction_date from (select 0.00
				// amount,id,transaction_type,'Cheque
				// Bounce'remarks,sbsc.transaction_id,retailer_org_id,
				// issuing_party_name, chq_nbr, cheque_date,drawee_bank,
				// cheque_amt,transaction_date from st_lms_agent_sale_chq
				// sbsc,(select id,transaction_id from
				// st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_agent_transaction_master)satm where
				// innermost.transaction_id=sbsc.transaction_id and
				// satm.transaction_id=innermost.transaction_id)cheque union
				// select retailer_org_id,id,'N.A.'issuing_party_name,'N.A.'
				// chq_nbr,'N.A.' cheque_date,'N.A.' drawee_bank,
				// amount,transaction_id,transaction_type,remarks,transaction_date
				// from (select
				// amount,id,transaction_type,remarks,sbdn.transaction_id,retailer_org_id,transaction_date
				// from st_lms_agent_debit_note sbdn,(select id,transaction_id
				// from st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_agent_transaction_master)satm where
				// innermost.transaction_id=sbdn.transaction_id and
				// satm.transaction_id=innermost.transaction_id )bounce) result1
				// where som.organization_id=result1.retailer_org_id ";
				query = "select name,addr_line1,addr_line2,city,retailer_org_id,id,issuing_party_name,cheque_nbr as cheque_nbr ,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date from st_lms_organization_master som ,(select retailer_org_id,id,issuing_party_name, cheque_nbr, cheque_date, drawee_bank, cheque_amt as amount,transaction_id, transaction_type,remarks,transaction_date from (select 0.00 amount,id,transaction_type,'Cheque Bounce'remarks,sbsc.transaction_id,retailer_org_id, issuing_party_name, cheque_nbr, cheque_date,drawee_bank, cheque_amt,transaction_date from st_lms_agent_sale_chq  sbsc,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "')innermost,(select transaction_id,transaction_date from st_lms_agent_transaction_master)satm  where innermost.transaction_id=sbsc.transaction_id and satm.transaction_id=innermost.transaction_id)cheque union select retailer_org_id,id,'N.A.'issuing_party_name,'N.A.' cheque_nbr,'N.A.' cheque_date,'N.A.' drawee_bank, amount,transaction_id,transaction_type,remarks,transaction_date from (select  amount,id,transaction_type,remarks,sbdn.transaction_id,retailer_org_id,transaction_date from st_lms_agent_debit_note  sbdn,(select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "')innermost,(select transaction_id,transaction_date from st_lms_agent_transaction_master)satm  where innermost.transaction_id=sbdn.transaction_id and satm.transaction_id=innermost.transaction_id  )bounce) result1 where som.organization_id=result1.retailer_org_id order by transaction_date";
				System.out.println(" DR_NOTE queryyyyyyyyyyyyyyyyyyy" + query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Debit_Note.jasper";
				generateReport(query, file, parameterMap, null, null);

			} else if (receipt_type.equalsIgnoreCase("DR_NOTE_CASH")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// retailer_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id
				// from (select
				// amount,transaction_type,remarks,transaction_id,retailer_org_id
				// from st_lms_agent_debit_note) sbdn,(select id,transaction_id
				// from st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where sbdn.transaction_id
				// =innermost.transaction_id )result1 where
				// som.organization_id=result1.retailer_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,retailer_org_id,id,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// retailer_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date
				// from (select
				// amount,transaction_type,remarks,transaction_id,retailer_org_id
				// from st_lms_agent_debit_note) sbdn,(select id,transaction_id
				// from st_lms_agent_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_agent_transaction_master)satm where
				// sbdn.transaction_id =innermost.transaction_id and
				// satm.transaction_id =innermost.transaction_id)result1 where
				// som.organization_id=result1.retailer_org_id";
				query = "select name,addr_line1,addr_line2,city,retailer_org_id,innermost.receipt_id as id,amount,sbdn.transaction_type,remarks, user_name,sbdn.transaction_id,transaction_date from st_lms_organization_master som INNER Join   st_lms_agent_debit_note  sbdn on som.organization_id=sbdn.retailer_org_id inner join  st_lms_agent_receipts_trn_mapping innermost on sbdn.transaction_id =innermost.transaction_id  and receipt_id= '"+receipt_id+"' inner join st_lms_agent_transaction_master satm on satm.transaction_id =innermost.transaction_id inner join  st_lms_user_master slum on slum.user_id= satm.user_id order by transaction_date;";
				System.out.println(" DR_NOTE_CASH queryyyyyyyyyyyyyyyyyyy"
						+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Debit_Note_Cash.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("CR_NOTE")) {

				if ("SALE_RET".equalsIgnoreCase(trnType)) {
					// vat amount ambiguous query = "select
					// retailer_sale_comm_rate,name,addr_line1,addr_line2,city,retailer_org_id,id,game_name
					// , ticket_price, nbr_of_tickets_per_book , nbr_of_books,
					// taxable_sale,vat_amt,comm_amt, sale,
					// (nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets,transaction_date from
					// st_lms_organization_master som , (select
					// retailer_sale_comm_rate,id,retailer_org_id,game_name,ticket_price,nbr_of_tickets_per_book,
					// result.game_id,result.nbr_of_books ,(net_amt) as
					// sale,taxable_sale,vat_amt,comm_amt,transaction_date from
					// st_se_game_master sgm, (select
					// sbat.retailer_org_id,game_id,nbr_of_books,net_amt,mrp_amt,vat_amt,comm_amt,
					// taxable_sale,sbat.transaction_id,id,transaction_date from
					// st_se_agent_retailer_transaction sbat, (select receipt_id
					// as id,transaction_id from
					// st_lms_agent_receipts_trn_mapping where receipt_id=
					// '"+receipt_id+"' ) innermost , (select
					// transaction_id,transaction_date from
					// st_lms_agent_transaction_master )satm where
					// sbat.transaction_id=innermost.transaction_id and
					// satm.transaction_id=innermost.transaction_id ) result
					// where sgm.game_id = result.game_id )result1 where
					// som.organization_id=result1.retailer_org_id";
					query = "select retailer_sale_comm_rate,name,addr_line1,addr_line2,city,retailer_org_id,id,game_name ,  ticket_price, nbr_of_tickets_per_book , nbr_of_books, taxable_sale,vat_amt,comm_amt, sale, (nbr_of_tickets_per_book*nbr_of_books)as total_tickets,transaction_date from st_lms_organization_master som , (select retailer_sale_comm_rate,id,retailer_org_id,game_name,ticket_price,nbr_of_tickets_per_book, result.game_id,result.nbr_of_books ,(net_amt) as sale,taxable_sale,result.vat_amt,comm_amt,transaction_date from  st_se_game_master sgm, (select sbat.retailer_org_id,game_id,nbr_of_books,net_amt,mrp_amt,vat_amt,comm_amt, taxable_sale,sbat.transaction_id,id,transaction_date from st_se_agent_retailer_transaction sbat, (select receipt_id as id,transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "' ) innermost , (select transaction_id,transaction_date from st_lms_agent_transaction_master )satm where sbat.transaction_id=innermost.transaction_id and satm.transaction_id=innermost.transaction_id ) result where sgm.game_id = result.game_id  )result1 where som.organization_id=result1.retailer_org_id order by transaction_date";
					System.out.println(" CR_NOTE queryyyyyyyyyyyyyyyyyyy"
							+ query);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtRdpVat.jasper";
					}
				}
				
				  if("CS_CANCEL_SERVER".equalsIgnoreCase(trnType)
	                        || "CS_CANCEL_RET".equalsIgnoreCase(trnType)){
	                    query = "select name,addr_line1,addr_line2,city,product_code,description,sale,vat as vat_amt,comm_amt, transaction_date from st_lms_organization_master som, st_cs_product_master scpm,(select sbtm.transaction_date, sdbs.retailer_org_id as agt_id, product_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, net_amt, (mrp_amt*sdbs.retailer_comm/100) as comm_amt from st_cs_agt_refund sdbs,(select receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id='"+ receipt_id+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_agent_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.retailer_org_id)result where result.agt_id = som.organization_id and result.product_id = scpm.product_id";
	                    System.out
	                            .println("CS CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
	                                    + query);
	                    file = root_path
	                            + "com/skilrock/lms/web/reportsMgmt/compiledReports/"
	                            + fileName;
	                    if (isCommApplicable.equalsIgnoreCase("yes")
	                            && isVatApplicable.equalsIgnoreCase("yes")) {
	                        fileName = "CSCrdtNtComVat.jasper";
	                    } else if (isCommApplicable.equalsIgnoreCase("yes")
	                            && isVatApplicable.equalsIgnoreCase("no")) {
	                        fileName = "CSCrdtNtComNoVat.jasper";
	                    } else if (isCommApplicable.equalsIgnoreCase("no")
	                            && isVatApplicable.equalsIgnoreCase("no")) {
	                        fileName = "CSCrdtNtRdpNoVat.jasper";
	                    } else if (isCommApplicable.equalsIgnoreCase("no")
	                            && isVatApplicable.equalsIgnoreCase("yes")) {
	                        fileName = "CSCrdtNtRdpVat.jasper";
	                    }
	               
	                }
				if ("LOOSE_SALE_RET".equalsIgnoreCase(trnType)) {
					query = "select id,game_name,organization_id,transaction_date,nbr_of_tickets nbrOfTickets, net_amt sale, retailer_sale_comm_rate agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,retComm agtSaleCommRate from st_lms_organization_master som ,(select retailer_org_id,id,game_name ,retailer_sale_comm_rate,sbs.nbr_of_tickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,retComm from st_se_agent_ret_loose_book_transaction sbs,st_se_game_master game, st_lms_agent_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+ receipt_id+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.retailer_org_id";
					System.out.println(" CR_NOTE queryyyyyyyyyyyyyyyyyyy"
							+ query);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtRdpVat.jasper";
					}
				}
				if ("DG_REFUND_CANCEL".equalsIgnoreCase(trnType)
						|| "DG_REFUND_FAILED".equalsIgnoreCase(trnType)) {
					query = "select name,addr_line1,addr_line2,city,game_name ,sale,taxable_sale,vat as vat_amt,agent_comm as comm_amt, transaction_date from st_lms_organization_master som, st_dg_game_master sdgm,(select sbtm.transaction_date, sdbs.retailer_org_id as ret_id, game_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, taxable_sale, net_amt, agent_comm from st_dg_agt_sale_refund sdbs,(select receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id='"
							+ receipt_id
							+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_agent_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.retailer_org_id)result where result.ret_id = som.organization_id and result.game_id = sdgm.game_id order by transaction_date";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtRdpVat.jasper";
					}
				}
				
				if ("OLA_DEPOSIT_REFUND".equalsIgnoreCase(trnType)) {
					query = "select id,wallet_name,transaction_date,deposit_amt as deposit_refund, net_amt,(deposit_amt-net_amt) as comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select retailer_org_id,id,wallet_id ,retailer_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_agt_deposit_refund sbs, st_lms_agent_transaction_master btm, (select slbr.party_id as retailer_id, slbr.receipt_id as id, transaction_id from st_lms_agent_receipts_trn_mapping slbrtm, st_lms_agent_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)deposit where deposit.wallet_id = sdgm.wallet_id and som.organization_id = deposit.retailer_org_id";
					System.out
							.println("OLA Agent end CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
						fileName = "OLACrdtNtRdpNoVat.jasper";
				}
				/*
				 * else{ query="select
				 * name,addr_line1,addr_line2,city,id,game_name
				 * ,sale,taxable_sale,vat_amt,comm_amt from
				 * st_lms_organization_master som ,(select
				 * id,game_name,result.game_id,taxable_sale,vat_amt,(net_amt) as
				 * sale,comm_amt,transaction_id from st_se_game_master
				 * sgm,(select sdbs.retailer_org_id,game_id,mrp_amt
				 * ,sdbs.transaction_id,id,
				 * vat_amt,taxable_sale,net_amt,comm_amt from st_dg_agt_sale
				 * sdbs,(select receipt_id as id,transaction_id from
				 * st_lms_agent_receipts_trn_mapping where receipt_id=
				 * '"+receipt_id+"') innermost,(select
				 * transaction_id,transaction_date from
				 * st_lms_agent_transaction_master )sbtm where
				 * sdbs.transaction_id=innermost.transaction_id and
				 * sbtm.transaction_id=innermost.transaction_id and
				 * retailer_org_id='') result where sgm.game_id = result.game_id
				 * )result1 where som.organization_id=result1.retailer_org_id";
				 * System.out.println("CR_NOTE
				 * queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"+query);file=root_path+
				 * "com/skilrock/lms/web/reportsMgmt/compiledReports/"+fileName;
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable.
				 * equalsIgnoreCase("yes")){ fileName="DGCrdtNtComVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtComNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtRdpNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("yes")){ fileName="DGCrdtNtRdpVat.jasper";
				 * } }
				 */

				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			} else if (receipt_type.equalsIgnoreCase("CR_NOTE_CASH")) {
				System.out.println("inside CR Note CASH");
				query = "select name,addr_line1,addr_line2,city,retailer_org_id,innermost.receipt_id as id,amount,sbdn.transaction_type,remarks, user_name,sbdn.transaction_id,transaction_date from st_lms_organization_master som INNER Join   st_lms_agent_credit_note  sbdn on som.organization_id=sbdn.retailer_org_id inner join  st_lms_agent_receipts_trn_mapping innermost on sbdn.transaction_id =innermost.transaction_id  and receipt_id= '"+receipt_id+"' inner join st_lms_agent_transaction_master satm on satm.transaction_id =innermost.transaction_id inner join  st_lms_user_master slum on slum.user_id= satm.user_id order by transaction_date;";
				System.out.println(" CR_NOTE_CASH for agent queryyyy");
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Credit_Note_Cash.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("GOVT_RCPT")) {
				vatPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("VAT_PARTY_NAME");
				tdsPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("TDS_PARTY_NAME");
				govComPartyName = (String) ServletActionContext
						.getServletContext().getAttribute(
								"GOVT_COMM_PARTY_NAME");
				parameterMap.put("vatPartyName", vatPartyName);
				parameterMap.put("tdsPartyName", tdsPartyName);
				parameterMap.put("govComPartyName", govComPartyName);
				System.out
						.println("================== inside GOVT_RCPT ================="
								+ trnType);
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount from st_lms_organization_master som ,(select
				// 'cash' t_type,cash_agent as agent,cash_amount as amount
				// ,cash_amount as net_amount ,0.00 comm_amount ,cash_trans_id
				// as trans_id, 'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id from ( select id as cash_id,sbct.agent_org_id as
				// cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as
				// cash_trans_id from st_lms_bo_cash_transaction sbct, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbct.transaction_id=innermost.transaction_id) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr, 'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as id from (select id
				// as pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as pwt_trans_id from
				// st_se_bo_pwt sbp, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbp.transaction_id=innermost.transaction_id
				// group by sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount ,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id
				// from ( select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id from st_lms_bo_sale_chq sbsc, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbsc.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount,transaction_date from st_lms_organization_master
				// som ,(select 'cash' t_type,cash_agent as agent,cash_amount as
				// amount ,cash_amount as net_amount ,0.00 comm_amount
				// ,cash_trans_id as trans_id,'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id,transaction_date from ( select id as
				// cash_id,sbct.agent_org_id as cash_agent,sbct.amount as
				// cash_amount , sbct.transaction_id as
				// cash_trans_id,transaction_date from
				// st_lms_bo_cash_transaction sbct, (select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost, (select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbct.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id ) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as
				// id,transaction_date from (select id as
				// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as
				// pwt_trans_id,transaction_date from st_se_bo_pwt sbp, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbp.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id group by
				// sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
				// from (select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id,transaction_date from st_lms_bo_sale_chq sbsc,
				// (select id,transaction_id from st_lms_bo_receipts_trn_mapping
				// where id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbsc.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				if (trnType.equalsIgnoreCase("VAT")) {
					// query=" select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date,temp_receipt_id
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date,temp_receipt_id from
					// st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt
					// sbctr,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbctr.cheque_nbr=sbsc.cheque_nbr ) cheque ) result where
					// result.agent = som.organization_id";
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cheque )
					// result where result.agent = som.organization_id";
					query = "select generated_id,sbrtm.transaction_id,amount,month, transaction_date, 'VAT Paid' as remarks from st_lms_agent_receipts_trn_mapping sbrtm,st_lms_agent_receipts sbr,st_lms_agent_govt_transaction sbgt ,st_lms_agent_transaction_master sbtm where sbr.receipt_id='"
							+ receipt_id
							+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by transaction_date";
					System.out
							.println(" GOVT_RCPT VAT  queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/VATAgent.jasper";
				} 

				generateReport(query, file, parameterMap, null, null);

			}
			System.out.println("Query -- " + query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method checks which type of Receipt is to be Generated at Back
	 * Office and send the data to Graph Report Method for generation of Pdf
	 * Receipts
	 * 
	 * @param receipt_id
	 *            as Integer
	 * @param root_path
	 *            as String
	 */
	public void createTextReportBO(int receipt_id, String boOrgName,
			int userOrgID, String root_path) {
		String isVatApplicable = null;
		String isCommApplicable = null;
		String receipt_type = null;
		String receipt_no = null;
		Timestamp voucher_date = null;

		connection = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String file = null;
		String trnType = null;

		HashMap parameterMap = new HashMap();

		System.out
				.println("======================= reciept_id : " + receipt_id);
		System.setProperty("java.awt.headless", "true");
		String orgAdd = null;
		String addQuery = null;
		String vatNum = null;
		String fileName = null;
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String tdsPartyName = null;
		String vatPartyName = null;
		String govComPartyName = null;
		String isScratch = null;
		String isDraw = null;
		Statement statement = null;
		ResultSet set = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			isScratch = (String) ServletActionContext.getServletContext()
					.getAttribute("IS_SCRATCH");
			isDraw = (String) ServletActionContext.getServletContext()
					.getAttribute("IS_DRAW");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			LedgerHelper ledger = new LedgerHelper();
			addQuery = QueryManager.getST6AddressQuery();
			orgAdd = ledger.getAddress(addQuery, "" + userOrgID, null);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("header", boOrgName);
			
			vatNum = ledger.getVatNumber("" + userOrgID);
			parameterMap.put("vatNum", vatNum);
			stmt = connection.createStatement();
			// query = "select receipt_type,id from st_lms_bo_receipts where id
			// ='"+receipt_id+"'";
			// query ="select
			// sbtm.transaction_id,transaction_type,sbrgm.receipt_type,sbrgm.generated_id
			// from st_lms_bo_receipts_trn_mapping
			// sbrtm,st_lms_bo_transaction_master sbtm,st_bo_receipt_gen_mapping
			// sbrgm where sbrgm.id='"+receipt_id+"' andlms.
			// sbrtm.transaction_id=sbtm.transaction_id and sbrgm.id=sbrtm.id";
			// "select receipt_type,generated_id from st_bo_receipt_gen_mapping
			// where id='"+receipt_id+"'";
			query = "select sbtm.transaction_id,transaction_type,sbr.receipt_type,sbr.generated_id, sbr.voucher_date from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_transaction_master sbtm,st_lms_bo_receipts sbr where sbrtm.receipt_id='"
					+ receipt_id
					+ "' and  sbrtm.transaction_id=sbtm.transaction_id and sbr.receipt_id=sbrtm.receipt_id";
			System.out.println("-------------------- " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				receipt_type = rs.getString("receipt_type");
				receipt_no = rs.getString("generated_id");
				trnType = rs.getString("transaction_type");
				voucher_date = rs.getTimestamp("voucher_date");
			}
			/*
			 * query for temp receipt no
			 */

			parameterMap.put("receipt_no", receipt_no);
			parameterMap.put("voucher_date", voucher_date);
			isVatApplicable = (String) ServletActionContext.getServletContext()
					.getAttribute("VAT_APPLICABLE");
			isCommApplicable = (String) ServletActionContext
					.getServletContext().getAttribute("COMM_APPLICABLE");
			
			if (receipt_type.equalsIgnoreCase("SLE_INVOICE")) {
				root_path = root_path.replace("\\", "/");
				if ("SLE_SALE".equalsIgnoreCase(trnType)) {
					query = "select id,game_disp_name game_name,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,taxable_sale,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_sle_game_master sdgm ,st_lms_organization_master som ,(select agent_id,id,game_id ,agent_comm as comm_amt, mrp_amt, net_amt, vat_amt as vat, taxable_sale,sbs.transaction_id, btm.transaction_date, voucher_date from st_sle_bo_sale sbs, st_lms_bo_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id = sale.agent_id order by transaction_date";
					System.out
							.println(" SLE_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "SLESaleInvcComVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "SLESaleInvcComNoVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "SLESaleInvcRdpNoVat.jasper";// changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "SLESaleInvcRdpVat.jasper";//
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			
			
			
			
			if (receipt_type.equalsIgnoreCase("DG_INVOICE")) {
				root_path = root_path.replace("\\", "/");
				if ("DG_SALE".equalsIgnoreCase(trnType)) {
					query = "select id,game_name,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,taxable_sale,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select agent_id,id,game_id ,agent_comm as comm_amt, mrp_amt, net_amt, vat_amt as vat, taxable_sale,sbs.transaction_id, btm.transaction_date, voucher_date from st_dg_bo_sale sbs, st_lms_bo_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id = sale.agent_id order by transaction_date";
					System.out
							.println(" DG_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcComVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcComNoVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcRdpNoVat.jasper";// changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcRdpVat.jasper";//
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if(receipt_type.equalsIgnoreCase("CS_INVOICE")){
				root_path = root_path.replace("\\", "/");
			if ("CS_SALE".equalsIgnoreCase(trnType)) {
				query = "select id,product_code,description,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_cs_product_master scpm ,st_lms_organization_master som ,(select agent_id,voucher_date,id,product_id ,(mrp_amt*agent_comm/100) as comm_amt, mrp_amt, net_amt, vat_amt as vat, sbs.transaction_id, btm.transaction_date from st_cs_bo_sale sbs, st_lms_bo_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+
						+ receipt_id
						+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.product_id = scpm.product_id and scpm.product_id and som.organization_id = sale.agent_id order by transaction_date";
				System.out
						.println(" CS_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcComVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcComNoVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcRdpNoVat.jasper";// changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcRdpVat.jasper";//
				}
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			
			if(receipt_type.equalsIgnoreCase("OLA_INVOICE")){
				root_path = root_path.replace("\\", "/");
			if ("OLA_DEPOSIT".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,deposit_amt as deposit, net_amt , (deposit_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,voucher_date,id,wallet_name ,agent_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_deposit sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id,slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
				System.out
						.println(" OLA_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLADepositInvcRdpNoVat.jasper";
			}
			if ("OLA_CASHCARD_SALE".equalsIgnoreCase(trnType)) {
				query = "select distributor name,'india' addr_line1,'XYZ' addr_line2,'delhi' city,voucher_date,id,wallet_name ,totalamount , sale_comm_rate,amount,sbs.transaction_id, btm.transaction_date from st_ola_bo_distributor_transaction sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select  slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = "+receipt_id+")innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id";
				System.out
						.println(" OLA_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLACashCardSaleInvcRdpNoVat.jasper";
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			if(receipt_type.equalsIgnoreCase("OLA_RECEIPT")){
				root_path = root_path.replace("\\", "/");
			if ("OLA_WITHDRAWL".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,withdrawl_amt as withdrawal, net_amt ,(withdrawl_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,voucher_date,id,wallet_name ,agent_comm as comm_amt, withdrawl_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_withdrawl sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id,slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
				System.out
						.println(" OLA_RECEIPT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLAWithdrawalInvcRdpNoVat.jasper";
			}
			// Check for OLA_COMMISSION transaction type
			if ("OLA_COMMISSION".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,comm_amt,netgame.tds_comm_rate,net_comm_amt,name,addr_line1,addr_line2,city,netgame.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select agent_org_id ,voucher_date,id,wallet_id ,agt_claim_comm as comm_amt,sbs.tds_comm_rate,agt_net_claim_comm as net_comm_amt , sbs.transaction_id, btm.transaction_date from st_ola_bo_comm sbs, st_lms_bo_transaction_master btm, (select slbr.party_id as agent_id,slbr.voucher_date as voucher_date,slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)netgame where netgame.wallet_id = sdgm.wallet_id and som.organization_id = netgame.agent_org_id";
				System.out
						.println(" OLA_RECEIPT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLANetGameInvcRdpNoVat.jasper";
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			if (receipt_type.equalsIgnoreCase("INVOICE")) {
				System.out
						.println("================== inside Invoice =================");
				System.out.println(root_path);
				root_path = root_path.replace("\\", "/");
				if ("SALE".equalsIgnoreCase(trnType)) {
					// VAT AMOUNT AMBIGUOUS "select
					// distinct(transaction_id),agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name
					// ,sale,taxable_sale,vat_amt,ticket_price,
					// nbr_of_tickets_per_book ,
					// nbr_of_books,total_tickets,transaction_date,order_id,order_date,comm_amt
					// from (select
					// agent_sale_comm_rate,name,addr_line1,addr_line2,city,
					// agent_org_id,id, game_name
					// ,sale,taxable_sale,sale.vat_amt,ticket_price,
					// nbr_of_tickets_per_book ,
					// nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets,transaction_date,transaction_id,order_id,order_date,comm_amt
					// from st_lms_organization_master som ,( select
					// agent_sale_comm_rate,id,sale.agent_org_id,game_name
					// ,ticket_price, nbr_of_tickets_per_book ,
					// sale.nbr_of_books,taxable_sale, vat_amt,(net_amt) as
					// sale,transaction_date,transaction_id,sale.order_id,order_date,comm_amt
					// from st_se_game_master ,st_se_bo_order sbo, (select
					// sbat.agent_org_id,id,sbat.game_id , nbr_of_books,
					// mrp_amt,sbat.transaction_id,
					// vat_amt,taxable_sale,net_amt,transaction_date,order_id,comm_amt
					// from st_se_bo_agent_transaction sbat ,
					// st_se_bo_order_invoices sboi,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' )innermost ,(select
					// transaction_date,transaction_id from
					// st_lms_bo_transaction_master )sbtm where
					// sbat.transaction_id = innermost.transaction_id and
					// innermost.transaction_id=sbtm.transaction_id and
					// sboi.invoice_id=innermost.id )sale where sale.game_id =
					// st_se_game_master.game_id and sbo.order_id=sale.order_id
					// )result1 where som.organization_id=result1.agent_org_id )
					// res";
//					query = "select distinct(transaction_id),agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name ,sale,taxable_sale,vat_amt,ticket_price, nbr_of_tickets_per_book , nbr_of_books,total_tickets,transaction_date,order_id,order_date,comm_amt from (select agent_sale_comm_rate,name,addr_line1,addr_line2,city, agent_org_id,id, game_name ,sale,taxable_sale,vat_amt,ticket_price, nbr_of_tickets_per_book , nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as total_tickets,transaction_date,transaction_id,order_id,order_date,comm_amt from  st_lms_organization_master som ,( select agent_sale_comm_rate,id,sale.agent_org_id,game_name ,ticket_price, nbr_of_tickets_per_book , sale.nbr_of_books,taxable_sale, sale.vat_amt,(net_amt) as sale,transaction_date,transaction_id,sale.order_id,order_date,comm_amt from  st_se_game_master ,st_se_bo_order sbo, (select sbat.agent_org_id,id,sbat.game_id , nbr_of_books, mrp_amt,sbat.transaction_id, sbat.vat_amt,taxable_sale,net_amt,transaction_date,order_id,comm_amt from st_se_bo_agent_transaction sbat , st_se_bo_order_invoices sboi,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id = '"
//							+ receipt_id
//							+ "' )innermost ,(select transaction_date,transaction_id from st_lms_bo_transaction_master     )sbtm where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id=sbtm.transaction_id and sboi.invoice_id=innermost.id )sale where sale.game_id = st_se_game_master.game_id and sbo.order_id=sale.order_id  )result1   where som.organization_id=result1.agent_org_id ) res order by transaction_date";
					
					query = "select distinct(transaction_id), agent_sale_comm_rate, name, addr_line1, addr_line2, city, agent_org_id, id, game_name, sale, taxable_sale, vat_amt, ticket_price, nbr_of_tickets_per_book,  nbr_of_books, total_tickets, transaction_date, 'NA' order_id, 'NA' order_date, comm_amt from(select agent_sale_comm_rate, name, addr_line1, addr_line2, city, agent_org_id, id, game_name , sale, taxable_sale, vat_amt, ticket_price, nbr_of_tickets_per_book , nbr_of_books, (nbr_of_tickets_per_book * nbr_of_books) as total_tickets, transaction_date, transaction_id, comm_amt from st_lms_organization_master som, (select agent_sale_comm_rate, id, sale.agent_org_id, game_name, ticket_price, nbr_of_tickets_per_book, sale.nbr_of_books, taxable_sale, sale.vat_amt, (net_amt) as sale, transaction_date, transaction_id, comm_amt from st_se_game_master,(select sbat.agent_org_id, id, sbat.game_id, nbr_of_books, mrp_amt, sbat.transaction_id, sbat.vat_amt, taxable_sale, net_amt, transaction_date, comm_amt from st_se_bo_agent_transaction sbat,(select receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "')innermost, (select transaction_date, transaction_id from st_lms_bo_transaction_master)sbtm where sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = sbtm.transaction_id)sale where sale.game_id = st_se_game_master.game_id)result1 where som.organization_id = result1.agent_org_id)res order by transaction_date;";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcComVat.jasper"; // changes .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcComNoVat.jasper"; // changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcRdpNoVat.jasper";// changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcRdpVat.jasper";//
					}
				} 
				else if ("LOOSE_SALE".equalsIgnoreCase(trnType)) {
					
					query = "select id,game_name,organization_id,transaction_date,nbrOfTickets, net_amt sale, agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,agtSaleCommRate  from st_lms_organization_master som ,(select agent_id,id,game_name ,agent_sale_comm_rate as agent_sale_comm_rate,nbrOfTickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,agtSaleCommRate from st_se_bo_agent_loose_book_transaction sbs,st_se_game_master game, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id ='"+ receipt_id+ "' )innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.agent_id";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcComVat.jasper"; // changes .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcComNoVat.jasper"; // changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcRdpNoVat.jasper";// changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcRdpVat.jasper";//
					}
				}
				
				else {
					// select id,game_name,taxable_sale, vat_amt,net_amt as
					// sale,comm_amt,transaction_id from st_dg_game_master sdgm
					// ,(select id,game_id ,agent_comm as comm_amt, mrp_amt,
					// vat_amt,taxable_sale,net_amt,sbat.transaction_id from
					// st_dg_bo_sale sbat,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' ) innermost where
					// sbat.transaction_id = innermost.transaction_id )sale
					// where sale.game_id = sdgm.game_id and
					// agent_org_id='"+userOrgID+"'";
					// query=" select id,game_name,taxable_sale,
					// sale.vat_amt,net_amt as sale,comm_amt,sale.transaction_id
					// from st_dg_game_master sdgm ,(select
					// agent_org_id,id,game_id ,agent_comm as comm_amt, mrp_amt,
					// vat_amt,taxable_sale,net_amt,sbat.transaction_id from
					// st_dg_bo_sale sbat,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' ) innermost where
					// sbat.transaction_id = innermost.transaction_id )sale
					// where sale.game_id = sdgm.game_id and
					// agent_org_id='"+userOrgID+"'";
					query = "select id,game_name,taxable_sale, sale.vat_amt,net_amt as sale,comm_amt,name,addr_line1,addr_line2,city,sale.transaction_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select agent_org_id,id,game_id ,agent_comm as comm_amt, mrp_amt, vat_amt,taxable_sale,net_amt,sbat.transaction_id from  st_dg_bo_sale sbat,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id )sale where sale.game_id = sdgm.game_id and sale.agent_org_id='"
							+ userOrgID
							+ "' and som.organization_id=sale.agent_org_id";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcComVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcComNoVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcRdpNoVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcRdpVat.jasper";//
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);

			}
			
			if (receipt_type.equalsIgnoreCase("DG_RECEIPT")) {
				if (trnType.equalsIgnoreCase("DG_PWT")
						|| trnType.equalsIgnoreCase("DG_PWT_AUTO")) {
					query = "select id,game_nbr,game_name,t_type,transaction_date,voucher_date,pwt_amt as amount, net_amt as net_amount,comm_amt as comm_amount,name,addr_line1,addr_line2,city,sale.transaction_id as trans_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select 'pwt' t_type, agent_id,id,game_id ,comm_amt, pwt_amt, net_amt,sbat.transaction_id,btm.transaction_date,voucher_date from  st_dg_bo_pwt sbat,st_lms_bo_transaction_master btm,(select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id=sale.agent_id order by transaction_date";
					System.out
							.println("DG_RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGNewPWTReceipt.jasper";
					generateReport(query, file, parameterMap, "Receipt",
							receipt_id + "");
				}
			} 
			
			if (receipt_type.equalsIgnoreCase("SLE_RECEIPT")) {
				if (trnType.equalsIgnoreCase("SLE_PWT")
						|| trnType.equalsIgnoreCase("SLE_PWT_AUTO")) {
					query = "select id,game_no game_nbr,game_disp_name game_name,t_type,transaction_date,voucher_date,pwt_amt as amount, net_amt as net_amount,comm_amt as comm_amount,name,addr_line1,addr_line2,city,sale.transaction_id as trans_id from st_sle_game_master sdgm ,st_lms_organization_master som ,(select 'pwt' t_type, agent_id,id,game_id ,comm_amt, pwt_amt, net_amt,sbat.transaction_id,btm.transaction_date,voucher_date from  st_sle_bo_pwt sbat,st_lms_bo_transaction_master btm,(select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id=sale.agent_id order by transaction_date";
					System.out
							.println("SLE_RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/SLENewPWTReceipt.jasper";
					generateReport(query, file, parameterMap, "Receipt",
							receipt_id + "");
				}
			} 
			
			else if(receipt_type.equalsIgnoreCase("RECEIPT") && trnType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				query = "select id,wallet_name,transaction_date,voucher_date,commission_calculated as commission_calculated, name,addr_line1,addr_line2,city,netGaming.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,id,wallet_name, commission_calculated,sbs.transaction_id, btm.transaction_date from st_ola_bo_agt_commission sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)netGaming where som.organization_id = netGaming.agent_id";
				System.out
						.println(" RECEIPT OLA COMMISSION queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/OLAReceipt.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			else if (receipt_type.equalsIgnoreCase("RECEIPT")) {
				System.out
						.println("================== inside reciept111 ================="
								+ trnType);
				if (trnType.equalsIgnoreCase("cheque")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,issuing_party_name,user_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id from  st_lms_organization_master som , (select 'cheque' t_type,cheque_agent as agent,cheque_amt as amount,user_name,cheque_amt as net_amount, cheque_id as trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,receipt_id,transaction_date,temp_receipt_id  from (select receipt_id ,slum.user_name,sbsc.agent_org_id as cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id as cheque_id,transaction_date,temp_receipt_id from st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt sbctr, (select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id="
							+receipt_id+
							") innermost,(select user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm,(select user_id, user_name from st_lms_user_master)slum where sbsc.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id  and sbsc.transaction_id=sbctr.transaction_id and slum.user_id = sbtm.user_id) cheque ) result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT Cheque queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/";
				} else if (trnType.equalsIgnoreCase("bank_deposit")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,user_name,amount,transaction_date from  st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,user_name,cash_amount as amount ,trans_id,receipt_id,transaction_date from ( select receipt_id,sbct.agent_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as trans_id,slum.user_name,transaction_date from  st_lms_bo_bank_deposit_transaction sbct,( select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "' ) innermost, (select  user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id  ) cash )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT BANK DEPOSIT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("CASH")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,user_name,amount,transaction_date from  st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,user_name,cash_amount as amount ,trans_id,receipt_id,transaction_date from ( select receipt_id,sbct.agent_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as trans_id,slum.user_name,transaction_date from  st_lms_bo_cash_transaction sbct,( select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id="
							+receipt_id+
							") innermost, (select user_id, transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id ) cash )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT CASH queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("PWT")
						|| trnType.equalsIgnoreCase("PWT_AUTO")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,amount,net_amount ,comm_amount,transaction_date,game_nbr,game_name from st_lms_organization_master som ,(select 'pwt' t_type,pwt_agent  as agent,pwt_amount as amount,net_amount ,comm_amount ,pwt_trans_id as trans_id, receipt_id,transaction_date,game_nbr,game_name from  (select receipt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as pwt_amount, sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as pwt_trans_id, transaction_date,sbp.game_id,game_nbr,game_name from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master )sgm, (select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm where sbp.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id group by sbp.transaction_id ) pwt )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/PWTReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("DG_PWT")
						|| trnType.equalsIgnoreCase("DG_PWT_AUTO")) {
					query = "select name,addr_line1,addr_line2,city,t_type,trans_id,receipt_id as id,amount,net_amount ,comm_amt,game_nbr,game_name from st_lms_organization_master som ,(select 'pwt' t_type,pwt_amount as amount,net_amount ,comm_amt , receipt_id,game_nbr,game_name from (select receipt_id,sbp.agent_org_id as agent,sbp.pwt_amt as pwt_amount, sbp.net_amt as net_amount ,sbp.comm_amt as comm_amt,sbp.transaction_id as pwt_trans_id, sbp.game_id,game_nbr,game_name from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master  )sgm,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id from st_lms_bo_transaction_master)sbtm where sbp.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id and agent_org_id='') pwt )result where result.agent = som.organization_id";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGPWTReceipt.jasper";
				}
				generateReport(query, file, parameterMap, "Receipt", receipt_id
						+ "");
			}

			else if (receipt_type.equalsIgnoreCase("DR_NOTE")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query= "select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,amount,transaction_type,remarks,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,
				// sbdn.transaction_type,remarks, cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc,(select
				// amount,transaction_type,remarks,transaction_id from
				// st_lms_bo_debit_note) sbdn, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbsc.transaction_id=innermost.transaction_id
				// and sbdn.transaction_id =innermost.transaction_id )result1
				// where som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name, cheque_nbr, cheque_date,
				// drawee_bank, cheque_amt as amount,transaction_id,
				// transaction_type,remarks,transaction_date from (select 0.00
				// amount,id,transaction_type,'Cheque
				// Bounce'remarks,sbsc.transaction_id,agent_org_id,
				// issuing_party_name, cheque_nbr, cheque_date,drawee_bank,
				// cheque_amt,transaction_date from st_lms_bo_sale_chq
				// sbsc,(select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// innermost.transaction_id=sbsc.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)cheque union
				// select agent_org_id,id,'N.A.'issuing_party_name,'N.A.'
				// cheque_nbr,'N.A.' cheque_date,'N.A.' drawee_bank,
				// amount,transaction_id,transaction_type,remarks,transaction_date
				// from (select
				// amount,id,transaction_type,remarks,sbdn.transaction_id,agent_org_id,transaction_date
				// from st_lms_bo_debit_note sbdn,(select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// innermost.transaction_id=sbdn.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id )bounce) result1
				// where som.organization_id=result1.agent_org_id ";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,receipt_id as id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date from st_lms_organization_master som ,(select agent_org_id,receipt_id,issuing_party_name, cheque_nbr, cheque_date, drawee_bank,cheque_amt as amount,transaction_id, transaction_type,remarks,transaction_date from (select 0.00 amount,receipt_id,transaction_type,'Cheque Bounce'remarks,sbsc.transaction_id,agent_org_id, issuing_party_name, cheque_nbr, cheque_date,drawee_bank, cheque_amt,transaction_date from st_lms_bo_sale_chq  sbsc,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "' )innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master)sbtm  where innermost.transaction_id=sbsc.transaction_id and sbtm.transaction_id=innermost.transaction_id )cheque union select agent_org_id,receipt_id as id,'N.A.'issuing_party_name,'N.A.' cheque_nbr,'N.A.' cheque_date,'N.A.' drawee_bank, amount,transaction_id,transaction_type,remarks,transaction_date from (select  amount,receipt_id,transaction_type,remarks,sbdn.transaction_id,agent_org_id,transaction_date from st_lms_bo_debit_note  sbdn,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "' )innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm  where innermost.transaction_id=sbdn.transaction_id and  sbtm.transaction_id=innermost.transaction_id  )bounce) result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out.println("queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO" + query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/";
				generateReport(query, file, parameterMap, "DebitNote",
						receipt_id + "");

			} else if (receipt_type.equalsIgnoreCase("DR_NOTE_CASH")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where sbdn.transaction_id
				// =innermost.transaction_id )result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where sbdn.transaction_id
				// =innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,id,user_name,amount,transaction_type,remarks,transaction_id,transaction_date from  st_lms_organization_master som , (select agent_org_id,id,amount,sbdn.transaction_type,slum.user_name,remarks,sbdn.transaction_id,transaction_date from (select  amount,transaction_type,remarks,transaction_id,agent_org_id from st_lms_bo_debit_note ) sbdn,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= "
						+receipt_id+
						") innermost,(select user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum where sbdn.transaction_id =innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id)result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out
						.println("DR_NOTE_CASH queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Debit_Note_Cash.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("CR_NOTE")) {
				if ("SALE_RET".equalsIgnoreCase(trnType)) {
					// query="select
					// agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name
					// ,sale,taxable_sale,vat_amt,comm_amt, ticket_price,
					// nbr_of_tickets_per_book
					// ,nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets, transaction_date from
					// st_lms_organization_master som , (select
					// agent_sale_comm_rate,id,agent_org_id,game_name,ticket_price,nbr_of_tickets_per_book,result.game_id,result.nbr_of_books
					// ,taxable_sale,vat_amt,(net_amt) as
					// sale,comm_amt,transaction_id,transaction_date from
					// st_se_game_master sgm,(select
					// sbat.agent_org_id,game_id,nbr_of_books,mrp_amt
					// ,sbat.transaction_id,id,
					// vat_amt,taxable_sale,net_amt,comm_amt,transaction_date
					// from st_se_bo_agent_transaction sbat, (select receipt_id
					// as id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id= '"+receipt_id+"' ) innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbat.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) result
					// where sgm.game_id = result.game_id )result1 where
					// som.organization_id=result1.agent_org_id";
					query = "select agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name ,sale,taxable_sale,vat_amt,comm_amt, ticket_price, nbr_of_tickets_per_book ,nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as total_tickets, transaction_date from  st_lms_organization_master som , (select agent_sale_comm_rate,id,agent_org_id,game_name,ticket_price,nbr_of_tickets_per_book,result.game_id,result.nbr_of_books ,taxable_sale,result.vat_amt,(net_amt) as sale,comm_amt,transaction_id,transaction_date from st_se_game_master sgm,(select sbat.agent_org_id,game_id,nbr_of_books,mrp_amt ,sbat.transaction_id,id, vat_amt,taxable_sale,net_amt,comm_amt,transaction_date from st_se_bo_agent_transaction sbat, (select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "' ) innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm where sbat.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id ) result where sgm.game_id = result.game_id  )result1 where som.organization_id=result1.agent_org_id order by transaction_date";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtRdpVat.jasper";
					}
				}
				if ("LOOSE_SALE_RET".equalsIgnoreCase(trnType)) {
					
					query = "select id,game_name,organization_id,transaction_date,nbrOfTickets, net_amt sale, agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,agtSaleCommRate from st_lms_organization_master som ,(select agent_id,id,game_name ,agent_sale_comm_rate as agent_sale_comm_rate,nbrOfTickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,agtSaleCommRate from st_se_bo_agent_loose_book_transaction sbs,st_se_game_master game, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+ receipt_id+ "' )innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.agent_id";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtRdpVat.jasper";
					}
				}
				if ("DG_REFUND_CANCEL".equalsIgnoreCase(trnType)
						|| "DG_REFUND_FAILED".equalsIgnoreCase(trnType)) {
					query = "select name,addr_line1,addr_line2,city,game_name ,sale,taxable_sale,vat as vat_amt,agent_comm as comm_amt, transaction_date from st_lms_organization_master som, st_dg_game_master sdgm,(select sbtm.transaction_date, sdbs.agent_org_id as agt_id, game_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, taxable_sale, net_amt, agent_comm from st_dg_bo_sale_refund sdbs,(select receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id='"
							+ receipt_id
							+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_bo_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.agent_org_id)result where result.agt_id = som.organization_id and result.game_id = sdgm.game_id order by transaction_date";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtRdpVat.jasper";
					}
				}
				if("CS_CANCEL_SERVER".equalsIgnoreCase(trnType)
						|| "CS_CANCEL_RET".equalsIgnoreCase(trnType)){
					query = "select name,addr_line1,addr_line2,city,product_code,description,sale,vat as vat_amt,comm_amt, transaction_date from st_lms_organization_master som, st_cs_product_master scpm,(select sbtm.transaction_date, sdbs.agent_org_id as agt_id, product_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, net_amt, (mrp_amt*sdbs.agent_comm/100) as comm_amt from st_cs_bo_refund sdbs,(select receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id='"
							+ receipt_id
							+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_bo_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.agent_org_id)result where result.agt_id = som.organization_id and result.product_id = scpm.product_id order by transaction_date";
					System.out
							.println("CS CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CSCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CSCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CSCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CSCrdtNtRdpVat.jasper";
					}
				
				}
				if("OLA_DEPOSIT_REFUND".equalsIgnoreCase(trnType)){
					query = "select id,wallet_name,transaction_date,voucher_date,deposit_amt as deposit_refund, net_amt ,(deposit_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,id,wallet_name ,agent_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_deposit_refund sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
					System.out
							.println("OLA CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					
						fileName = "OLACrdtNtRdpNoVat.jasper";
				}
				/*
				 * else{ query="select
				 * name,addr_line1,addr_line2,city,id,game_name
				 * ,sale,taxable_sale,vat_amt,comm_amt from
				 * st_lms_organization_master som ,(select
				 * id,game_name,result.game_id,taxable_sale,vat_amt,(net_amt) as
				 * sale,comm_amt,transaction_id from st_se_game_master
				 * sgm,(select sdbs.agent_org_id,game_id,mrp_amt
				 * ,sdbs.transaction_id,id,
				 * vat_amt,taxable_sale,net_amt,comm_amt from st_dg_bo_sale
				 * sdbs,(select receipt_id as id,transaction_id from
				 * st_lms_bo_receipts_trn_mapping where receipt_id=
				 * '"+receipt_id+"') innermost,(select
				 * transaction_id,transaction_date from
				 * st_lms_bo_transaction_master )sbtm where
				 * sdbs.transaction_id=innermost.transaction_id and
				 * sbtm.transaction_id=innermost.transaction_id and
				 * agent_org_id='') result where sgm.game_id = result.game_id
				 * )result1 where som.organization_id=result1.agent_org_id";
				 * System.out.println("CR_NOTE
				 * queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"+query);file=root_path+
				 * "com/skilrock/lms/web/reportsMgmt/compiledReports/"+fileName;
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable.
				 * equalsIgnoreCase("yes")){ fileName="DGCrdtNtComVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtComNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtRdpNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("yes")){ fileName="DGCrdtNtRdpVat.jasper";
				 * } }
				 */

				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				generateReport(query, file, parameterMap, null, null, null,
						null);
			}

			else if (receipt_type.equalsIgnoreCase("CR_NOTE_CASH")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where sbdn.transaction_id
				// =innermost.transaction_id )result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where sbdn.transaction_id
				// =innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,user_name,transaction_id,transaction_date from  st_lms_organization_master som , (select agent_org_id,id,slum.user_name,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date from (select  amount,transaction_type,remarks,transaction_id,agent_org_id from st_lms_bo_credit_note ) sbdn,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= "
						+receipt_id+
						") innermost,(select user_id, transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_name, user_id from st_lms_user_master) slum where sbdn.transaction_id =innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id)result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out
						.println("CR_NOTE_CASH queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Credit_Note_Cash.jasper";
				generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("GOVT_RCPT")) {
				vatPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("VAT_PARTY_NAME");
				tdsPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("TDS_PARTY_NAME");
				govComPartyName = (String) ServletActionContext
						.getServletContext().getAttribute(
								"GOVT_COMM_PARTY_NAME");
				parameterMap.put("vatPartyName", vatPartyName);
				parameterMap.put("tdsPartyName", tdsPartyName);
				parameterMap.put("govComPartyName", govComPartyName);
				System.out
						.println("================== inside GOVT_RCPT ================="
								+ trnType);
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount from st_lms_organization_master som ,(select
				// 'cash' t_type,cash_agent as agent,cash_amount as amount
				// ,cash_amount as net_amount ,0.00 comm_amount ,cash_trans_id
				// as trans_id, 'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id from ( select id as cash_id,sbct.agent_org_id as
				// cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as
				// cash_trans_id from st_lms_bo_cash_transaction sbct, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbct.transaction_id=innermost.transaction_id) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr, 'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as id from (select id
				// as pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as pwt_trans_id from
				// st_se_bo_pwt sbp, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbp.transaction_id=innermost.transaction_id
				// group by sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount ,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id
				// from ( select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id from st_lms_bo_sale_chq sbsc, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbsc.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount,transaction_date from st_lms_organization_master
				// som ,(select 'cash' t_type,cash_agent as agent,cash_amount as
				// amount ,cash_amount as net_amount ,0.00 comm_amount
				// ,cash_trans_id as trans_id,'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id,transaction_date from ( select id as
				// cash_id,sbct.agent_org_id as cash_agent,sbct.amount as
				// cash_amount , sbct.transaction_id as
				// cash_trans_id,transaction_date from
				// st_lms_bo_cash_transaction sbct, (select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost, (select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbct.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id ) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as
				// id,transaction_date from (select id as
				// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as
				// pwt_trans_id,transaction_date from st_se_bo_pwt sbp, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbp.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id group by
				// sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
				// from (select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id,transaction_date from st_lms_bo_sale_chq sbsc,
				// (select id,transaction_id from st_lms_bo_receipts_trn_mapping
				// where id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbsc.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				if (trnType.equalsIgnoreCase("VAT")) {
					// query=" select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date,temp_receipt_id
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date,temp_receipt_id from
					// st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt
					// sbctr,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbctr.cheque_nbr=sbsc.cheque_nbr ) cheque ) result where
					// result.agent = som.organization_id";
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cheque )
					// result where result.agent = som.organization_id";
					query = "select generated_id,sbrtm.transaction_id,amount,start_date,end_date, transaction_date, sbr.voucher_date,'VAT Paid' as remarks from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm where sbr.receipt_id='"
							+ receipt_id
							+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by transaction_date";
					System.out
							.println(" RECEIPT VAT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/VAT.jasper";
				} else if (trnType.equalsIgnoreCase("TDS")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_trans_id as trans_id,cash_id as id,transaction_date
					// from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct,(select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id )
					// cash)result where result.agent = som.organization_id";
					// query="select
					// generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date,
					// sbtm.transaction_date, 'TDS Paid' as
					// remarks,sdpp.transaction_date as plr_date
					// ,sdpp.player_id,
					// first_name,last_name,photo_id_type,photo_id_nbr,sdpp.tax_amt,sdpp.pwt_amt
					// from st_lms_bo_receipts_trn_mapping
					// sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction
					// sbgt ,st_lms_bo_transaction_master
					// sbtm,st_se_direct_player_pwt sdpp,st_lms_player_master
					// spm where sbr.receipt_id='"+receipt_id+"' and
					// sbrtm.receipt_id=sbr.receipt_id and
					// sbgt.transaction_id=sbrtm.transaction_id and
					// sbtm.transaction_id=sbrtm.transaction_id and
					// sdpp.transaction_date >= start_date and
					// sdpp.transaction_date <=end_date and
					// sdpp.player_id=spm.player_id";
					statement = connection.createStatement();
					set = statement
							.executeQuery("select player_id  from   st_se_direct_player_pwt");
					System.out.println(set.absolute(1));
					if (set.absolute(1)) {
						query = "select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, sbr.voucher_date, '"
								+ textForTax
								+ " Paid' as remarks,sdpp.player_id, first_name,last_name,photo_id_type,photo_id_nbr,sdpp.tax_amt,sdpp.pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_se_direct_player_pwt sdpp,st_lms_player_master spm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sdpp.transaction_date >= start_date and sdpp.transaction_date <=end_date and sdpp.player_id=spm.player_id union select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, '"
								+ textForTax
								+ " Paid' as remarks, 'NA' player_id, 'NA'first_name,'NA' last_name,'NA' photo_id_type,'NA' photo_id_nbr,'NA' tax_amt,'NA' pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_lms_player_master spm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by sbtm.transaction_date";
					} else {
						query = "select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, sbr.voucher_date, '"
								+ textForTax
								+ " Paid' as remarks, 'NA' player_id, 'NA'first_name,'NA' last_name,'NA' photo_id_type,'NA' photo_id_nbr,'NA' tax_amt,'NA' pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by sbtm.transaction_date";
					}
					System.out
							.println(" RECEIPT TDS queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/TDS.jasper";
				} else if (trnType.equalsIgnoreCase("GOVT_COMM")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr,game_name from
					// st_lms_organization_master som ,(select 'pwt'
					// t_type,pwt_agent as agent,pwt_amount as amount,net_amount
					// ,comm_amount ,pwt_trans_id as trans_id, pwt_id as
					// id,transaction_date,game_nbr,game_name from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr,game_name
					// from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name
					// from st_se_game_master )sgm,(select id,transaction_id
					// from st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id )
					// pwt)result where result.agent = som.organization_id";
					// "select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr from
					// st_lms_organization_master som , (select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_amount as net_amount ,0.00 comm_amount
					// ,cash_trans_id as trans_id,'cash'
					// issuing_party_name,'cash' cheque_nbr,'cash' cheque_date,
					// 'cash' drawee_bank,cash_id as id,transaction_date,'cash'
					// game_nbr from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct, (select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost, (select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cash union
					// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
					// amount,net_amount ,comm_amount ,pwt_trans_id as
					// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
					// cheque_date, 'pwt' drawee_bank , pwt_id as
					// id,transaction_date,game_nbr from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr from
					// st_se_bo_pwt sbp,(select game_nbr,game_id from
					// st_se_game_master )sgm, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id) pwt
					// union select 'cheque' t_type,cheque_agent as
					// agent,cheque_amt as amount,cheque_amt as net_amount,0.00
					// comm_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// , 'cheque' game_nbr from (select id ,agent_org_id as
					// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id) cheque)
					// result where result.agent = som.organization_id";
					String serviceCode = checkServiceCode(receipt_id);
					if (serviceCode.equalsIgnoreCase("se")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_se_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					} else if (serviceCode.equalsIgnoreCase("dg")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_dg_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					} else if (serviceCode.equalsIgnoreCase("SLE")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,type_disp_name as game_name,game_type_id as game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_sle_game_type_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					}
					System.out
							.println(" GOVT_COMM queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/GovtComm.jasper";
				
				} else if (trnType.equalsIgnoreCase("GOVT_COMM_PWT")) {

					String serviceCode = checkServiceCode(receipt_id);
					if (serviceCode.equalsIgnoreCase("se")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_se_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					} else if (serviceCode.equalsIgnoreCase("dg")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_dg_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					} else if (serviceCode.equalsIgnoreCase("SLE")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,type_disp_name as game_name,game_type_id as game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_sle_game_type_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					}
					System.out.println(" GOVT_COMM_PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/GovtComm.jasper";
				}
				generateReport(query, file, parameterMap, null, null);

			}

			System.out.println("Query -- " + query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void createTextReportPlayer(int task_id, String root_path,
			String gameType) {
		String receipt_type = null;

		Connection connection = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String query = null;
		String addQuery = null;
		String file = null;
		String name = null;
		String addr_line1 = null;
		String addr_line2 = null;
		String city = null;
		String receipt_no = null;
		String orgAdd = null;

		HashMap parameterMap = new HashMap();
		System.out
				.println("heeeeeellllllllloooooooooo  createTextReportPlayer");
		System.setProperty("java.awt.headless", "true");

		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;

		String queryForPartyType = null;
		String queryForPlayer = null;
		String queryForAnonymous = null;
		String queryforAgtRet = null;
		String partyType = null;
		String requestId = null;
		String payerType = null;
		int payersId = 0;
		String payersName = null;
		String payersAdd = null;
		String tableName = null;
		String rcptNoQuery = null;
		long  transactionId = 0;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);
			if ("DRAW_GAME".equalsIgnoreCase(gameType)) {
				queryForAnonymous = "select 'Anonymous' name,'' addr_line1,'' addr_line2,'' city,'' photo_id_type,'' photo_id_nbr, transaction_id,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id from "
						+ tableName + " dpp where task_id='" + task_id + "'";
				// queryForPlayer= "select
				// transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,'
				// ', last_name)as name
				// ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id
				// from st_dg_bo_direct_plr_pwt dpp, st_lms_player_master spm
				// where task_id='"+task_id+"' and spm.player_id=dpp.player_id";
				queryForPartyType = "select transaction_id,party_type,request_id,payment_done_by,payment_done_by_type,name from st_dg_approval_req_master ,st_lms_organization_master where task_id='"
						+ task_id + "' and payment_done_by=organization_id";

			} else {
				queryForAnonymous = "select 'Anonymous' name,'' addr_line1,'' addr_line2,'' city,'' photo_id_type,'' photo_id_nbr, transaction_id,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from "
						+ tableName + " dpp where task_id='" + task_id + "'";
				// queryForPlayer= "select
				// transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,'
				// ', last_name)as name
				// ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from
				// "+tableName +" dpp, st_lms_player_master spm where
				// task_id='"+task_id+"' and spm.player_id=dpp.player_id";
				queryForPartyType = "select transaction_id,party_type,request_id,payment_done_by,payment_done_by_type,name from st_se_pwt_approval_request_master,st_lms_organization_master where task_id='"
						+ task_id + "' and payment_done_by=organization_id";
			}
			// queryForPartyType="select
			// transaction_id,party_type,request_id,payment_done_by,payment_done_by_type,name
			// from st_se_pwt_approval_request_master,st_lms_organization_master
			// where task_id='"+task_id+"' and payment_done_by=organization_id";
			stmt = connection.createStatement();
			rs1 = stmt.executeQuery(queryForPartyType);
			System.out.println("queryForPartyType  " + queryForPartyType + "  "
					+ stmt.toString());
			while (rs1.next()) {
				partyType = rs1.getString("party_type");
				requestId = rs1.getString("request_id");
				payerType = rs1.getString("payment_done_by_type");
				payersId = rs1.getInt("payment_done_by");
				payersName = rs1.getString("name");
				transactionId = rs1.getLong("transaction_id");
				System.out.println("payerType " + payerType);
			}
			LedgerHelper ledger = new LedgerHelper();
			addQuery = QueryManager.getST6AddressQuery();
			payersAdd = ledger.getAddress(addQuery, "" + payersId, null);
			parameterMap.put("orgAdd", payersAdd);
			parameterMap.put("header", payersName);
			parameterMap.put("requestId", requestId);
			root_path = root_path.replace("\\", "/");
			if ("DRAW_GAME".equalsIgnoreCase(gameType)) {
				if (payerType.equalsIgnoreCase("BO")) {
					if (partyType.equalsIgnoreCase("PLAYER")) {
						tableName = "st_dg_bo_direct_plr_pwt";
						query = "select transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id from "
								+ tableName
								+ " dpp, st_lms_player_master spm where task_id='"
								+ task_id
								+ "' and  spm.player_id=dpp.player_id order by transaction_date";
						rcptNoQuery = "select generated_id,receipt_type,voucher_date from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts  sbr where transaction_id='"
								+ transactionId
								+ "' and sbr.receipt_id=sbrtm.receipt_id";
					}

				} else if (payerType.equalsIgnoreCase("AGENT")) {

					if (partyType.equalsIgnoreCase("PLAYER")) {
						tableName = "st_dg_agt_direct_plr_pwt";
						query = "select transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id from "
								+ tableName
								+ " dpp, st_lms_player_master spm where task_id='"
								+ task_id
								+ "' and  spm.player_id=dpp.player_id order by transaction_date";
						rcptNoQuery = "select generated_id,receipt_type from st_lms_agent_receipts_trn_mapping sartm,st_lms_agent_receipts  sar where transaction_id='"
								+ transactionId
								+ "' and sar.receipt_id=sartm.receipt_id";
					}

				} else if (payerType.equalsIgnoreCase("RETAILER")) {
					rcptNoQuery = "select generated_id,receipt_type from st_ret_receipts_trn_mapping sartm,st_ret_receipts  sar where transaction_id='"
							+ transactionId
							+ "' and sar.receipt_id=sartm.receipt_id";
					tableName = "st_dg_ret_direct_plr_pwt";
					if (partyType.equalsIgnoreCase("PLAYER")) {
						query = "select transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id from "
								+ tableName
								+ " dpp, st_lms_player_master spm where task_id='"
								+ task_id
								+ "' and  spm.player_id=dpp.player_id order by transaction_date";

					} else if (partyType.equalsIgnoreCase("ANONYMOUS")) {
						query = queryForAnonymous;
					}
				}

				// queryForPlayer="select
				// transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,'
				// ', last_name)as name
				// ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id
				// from "+tableName +" dpp, st_lms_player_master spm where
				// task_id='"+task_id+"' and spm.player_id=dpp.player_id";
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGDirectPlayerReceipt.jasper";
			} else {
				if (payerType.equalsIgnoreCase("BO")) {
					if (partyType.equalsIgnoreCase("PLAYER")) {
						tableName = "st_se_direct_player_pwt";
						query = "select transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from "
								+ tableName
								+ " dpp, st_lms_player_master spm where task_id='"
								+ task_id
								+ "' and  spm.player_id=dpp.player_id order by transaction_date";
						rcptNoQuery = "select generated_id,receipt_type,voucher_date from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts  sbr where transaction_id='"
								+ transactionId
								+ "' and sbr.receipt_id=sbrtm.receipt_id";
					}

				} else if (payerType.equalsIgnoreCase("AGENT")) {

					if (partyType.equalsIgnoreCase("PLAYER")) {
						tableName = "st_se_agt_direct_player_pwt";
						query = "select transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from "
								+ tableName
								+ " dpp, st_lms_player_master spm where task_id='"
								+ task_id
								+ "' and  spm.player_id=dpp.player_id order by transaction_date";
						rcptNoQuery = "select generated_id,receipt_type from st_lms_agent_receipts_trn_mapping sartm,st_lms_agent_receipts  sar where transaction_id='"
								+ transactionId
								+ "' and sar.receipt_id=sartm.receipt_id";
					}

				}
				// queryForPlayer="select
				// transaction_id,transaction_date,cheque_nbr,cheque_date,drawee_bank,issuing_party_name,payment_type,pwt_amt,tax_amt,net_amt,concat(first_name,'
				// ', last_name)as name
				// ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from
				// "+tableName +" dpp, st_lms_player_master spm where
				// task_id='"+task_id+"' and spm.player_id=dpp.player_id";
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DirectPlayerReceipt.jasper";
			}
			rs = stmt.executeQuery(rcptNoQuery);

			while (rs.next()) {
				receipt_no = rs.getString("generated_id");
				receipt_type = rs.getString("receipt_type");
				System.out.println("receipt_type " + receipt_type
						+ "  receipt_no " + receipt_no);
			}

			parameterMap.put("receipt_no", receipt_no);
			parameterMap.put("requestId", requestId);

			if (receipt_type.equalsIgnoreCase("RECEIPT")) {
				// query=queryForPlayer;
				System.out.println("queryyyyyyyy createTextReportPlayer"
						+ query);
				generateReport(query, file, parameterMap, null, null, null,
						null);

			}

			System.out.println("Query -- " + query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void createTextReportTempPlayerReceipt(String task_id,
			String viewerType, String root_path, String gameType) {
		String receipt_type = null;

		connection = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String file = null;
		HashMap parameterMap = new HashMap();
		System.out
				.println("heeeeeellllllllloooooooooo  createTextReportTempPlayerReceipt");
		System.setProperty("java.awt.headless", "true");

		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;
		String queryForPartyType = null;
		String queryForPlayer = null;
		String queryForAnonymous = null;
		String queryforAgtRet = null;
		String partType = null;
		int requestorsOrgId = 0;
		String addQuery = null;
		String approverName = null;
		String approverAdd = null;
		String requestid = task_id;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);
			parameterMap.put("viewerType", viewerType);
			if ("DRAW_GAME".equalsIgnoreCase(gameType)) {
				queryForPartyType = "select party_type,requested_by_org_id ,name from st_dg_approval_req_master,st_lms_organization_master where request_id='"
						+ requestid
						+ "' and requested_by_org_id=organization_id";
				queryForPlayer = "select party_type,party_id,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr ,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr,draw_id from st_dg_approval_req_master sparm, st_se_game_master sgm,st_lms_player_master where request_id='"
						+ requestid
						+ "' and sparm.game_id=sgm.game_id and player_id=party_id";
				queryForAnonymous = "select 'Anonymous' name,'' addr_line1,'' addr_line2,'' city,'' photo_id_type,'' photo_id_nbr,party_type,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr,draw_id from st_dg_approval_req_master sparm, st_se_game_master sgm where request_id='"
						+ requestid + "'  and sparm.game_id=sgm.game_id";
				queryforAgtRet = "select party_type,party_id,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr ,name,addr_line1,addr_line2,city,draw_id from st_dg_approval_req_master sparm, st_se_game_master sgm,st_lms_organization_master where request_id='"
						+ requestid
						+ "' and sparm.game_id=sgm.game_id and organization_id=party_id";

			} else {
				queryForPartyType = "select party_type,requested_by_org_id ,name from st_se_pwt_approval_request_master,st_lms_organization_master where task_id='"
						+ task_id + "' and requested_by_org_id=organization_id";
				queryForPlayer = "select party_type,party_id,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr ,concat(first_name,' ', last_name)as name ,addr_line1,addr_line2,city,photo_id_type,photo_id_nbr from st_se_pwt_approval_request_master sparm, st_se_game_master sgm,st_lms_player_master where task_id='"
						+ task_id
						+ "' and sparm.game_id=sgm.game_id and player_id=party_id;";
				queryForAnonymous = "select 'Anonymous' name,'' addr_line1,'' addr_line2,'' city,'' photo_id_type,'' photo_id_nbr,party_type,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr from st_se_pwt_approval_request_master sparm, st_se_game_master sgm where task_id='"
						+ task_id + "'  and sparm.game_id=sgm.game_id";
				queryforAgtRet = "select party_type,party_id,request_id,ticket_nbr,pwt_amt,request_date,game_name,game_nbr ,name,addr_line1,addr_line2,city from st_se_pwt_approval_request_master sparm, st_se_game_master sgm,st_lms_organization_master where task_id='"
						+ task_id
						+ "' and sparm.game_id=sgm.game_id and organization_id=party_id";
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(queryForPartyType);
			while (rs.next()) {
				partType = rs.getString("party_type");
				requestorsOrgId = rs.getInt("requested_by_org_id");
				approverName = rs.getString("name");

			}
			LedgerHelper ledger = new LedgerHelper();
			addQuery = QueryManager.getST6AddressQuery();
			approverAdd = ledger.getAddress(addQuery, "" + requestorsOrgId,
					null);
			parameterMap.put("orgAdd", approverAdd);
			parameterMap.put("header", approverName);

			if (partType.equalsIgnoreCase("PLAYER")) {
				query = queryForPlayer;
			} else if (partType.equalsIgnoreCase("ANONYMOUS")) {
				query = queryForAnonymous;

			} else if (partType.equalsIgnoreCase("AGENT")
					|| partType.equalsIgnoreCase("RETAILER")) {
				query = queryforAgtRet;
			}

			root_path = root_path.replace("\\", "/");
			// query="select
			// a.game_name,b.transaction_date,b.pwt_amt,ticket_nbr,game_name,game_nbr
			// from st_se_game_master a,st_se_direct_player_pwt_temp_receipt b
			// where a.game_id="+gameId +" and
			// b.pwt_receipt_id='"+receipt_id+"'";
			System.out.println("queryyyyyyyy createTextReportTempPlayerReceipt"
					+ query);

			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DirectPlayerTempReceipt.jasper";
			generateReport(query, file, parameterMap, null, null, null, null);

			System.out.println("Query -- " + query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public List generateConsolidatedReport(String query, List consolidatedList,
			String agentName, Timestamp fromDate, Timestamp todate) {
		DBConnect dbConnect = null;
		Connection connection = null;
		Timestamp date_entered = null;
		String tmp_receipt_id = null;
		String t_type = null;
		String tempType = null;
		int rowexists = 0;
		String type = null;
		double openingBalance = 0.0;
		double balance = 0.0;
		double amount = 0.0;
		String timeFormat = null;
		String currencySymbol = null;
		String decimalFormat = null;
		String dateFormat = null;
		String textForTax = null;
		Map<String, List<AgtLedAccDetailsBean>> AgentMap = new HashMap<String, List<AgtLedAccDetailsBean>>();
		Map<String, Double> accountMap = new HashMap<String, Double>();
		AgtLedAccDetailsBean accDetailsBean = null;
		List<AgtLedAccDetailsBean> ledAccBeanlist = new ArrayList<AgtLedAccDetailsBean>();
		double accbal = 0.0;
		String trParticulars = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			consolidatedList.add(decimalFormat);// 5 7
			consolidatedList.add(timeFormat);// 6 8
			consolidatedList.add(currencySymbol);// 7 9
			consolidatedList.add(textForTax);// 8 10

			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ agentName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "' limit 1");

			while (resultSet2.next()) {
				balance = resultSet2.getDouble("balance");
				amount = resultSet2.getDouble("amount");

			}
			openingBalance = balance - amount;
			consolidatedList.add(openingBalance);// 9 11

			System.out.println("openingBalance   _________-------"
					+ openingBalance);
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ agentName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "'");
			while (resultSet2.next()) {

				if (("SALE_RET".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DG_REFUND_CANCEL".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_REFUND_FAILED"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By Sales Return";
				} else if (("SALE".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_SALE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Gross Sales";
				} else if (("PWT_PLR".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_PWT_PLR"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& "PWT_PAY".equalsIgnoreCase(resultSet2
								.getString("account_type"))) {
					trParticulars = "Player PWT";
				} else if ("PLAYER_CAS".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Player Net";
				} else if ("CASH".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CHEQUE".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By  Reciepts";
				} else if (("CHQ_BOUNCE".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DR_NOTE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Reciept";
				} else if (!resultSet2.getString("account_type").contains("_")
						&& !resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By PWT Collection";
				} else if (resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Collection Charges";
				} else if ("GOVT_COMM".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Govt Contribution";
				} else if ("VAT_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To VAT Contribution";
				} else if ("TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& "TDS".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))) {
					trParticulars = "To Deductions";
				} else if ("TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& ("PWT_PLR".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_PWT_PLR"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Player Deduction";
				}

				if (accountMap.containsKey(trParticulars)) {

					accbal = accountMap.get(trParticulars);
					accountMap.put(trParticulars, accbal
							+ resultSet2.getDouble("amount"));
				} else {
					accountMap.put(trParticulars, resultSet2
							.getDouble("amount"));

				}
				/*
				 * for ledger details List
				 */
				accDetailsBean = new AgtLedAccDetailsBean();

				accDetailsBean.setLedTransType(trParticulars);
				accDetailsBean.setTransactionID(resultSet2
						.getString("transaction_id"));
				accDetailsBean.setAccBalance(resultSet2.getDouble("balance"));
				accDetailsBean.setAmount(resultSet2.getDouble("amount"));
				accDetailsBean.setTrDate(resultSet2
						.getTimestamp("transaction_date"));
				accDetailsBean.setTransactionWith(resultSet2
						.getString("transaction_with"));
				ledAccBeanlist.add(accDetailsBean);

			}
			TreeMap<String, Map<String, AgtLedAccDetailsBean>> dayWiseLedMap = getDayWiseLedMap(
					agentName, fromDate, todate);

			consolidatedList.add(accountMap);// 10 12
			consolidatedList.add(ledAccBeanlist);// detailed ledger 11 13
			consolidatedList.add(dayWiseLedMap);// day wise map 12 14

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return consolidatedList;
	}

	public List generateConsolidatedReportAgt(String query,
			List consolidatedList, String retName, Timestamp fromDate,
			Timestamp todate) {
		DBConnect dbConnect = null;
		Connection connection = null;
		Timestamp date_entered = null;
		String tmp_receipt_id = null;
		String t_type = null;
		String tempType = null;
		int rowexists = 0;
		String type = null;
		double openingBalance = 0.0;
		double balance = 0.0;
		double amount = 0.0;
		String timeFormat = null;
		String currencySymbol = null;
		String decimalFormat = null;
		String dateFormat = null;
		String textForTax = null;
		Map<String, List<RetLedAccDetailsBean>> AgentMap = new HashMap<String, List<RetLedAccDetailsBean>>();
		Map<String, Double> accountMap = new HashMap<String, Double>();
		RetLedAccDetailsBean accDetailsBean = null;
		List<RetLedAccDetailsBean> ledAccBeanlist = new ArrayList<RetLedAccDetailsBean>();
		double accbal = 0.0;
		String trParticulars = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			consolidatedList.add(decimalFormat);// 5 7
			consolidatedList.add(timeFormat);// 6 8
			consolidatedList.add(currencySymbol);// 7 9
			consolidatedList.add(textForTax);// 8 10

			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ retName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "' limit 1");

			while (resultSet2.next()) {
				balance = resultSet2.getDouble("balance");
				amount = resultSet2.getDouble("amount");

			}
			openingBalance = balance - amount;
			consolidatedList.add(openingBalance);// 9 11

			System.out.println("openingBalance   _________-------"
					+ openingBalance);
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ retName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "'");
			while (resultSet2.next()) {

				if (("SALE_RET".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DG_REFUND_CANCEL".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_REFUND_FAILED"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By Sales Return";
				} else if (("SALE".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_SALE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Gross Sales";
				} else if ("BO_SALE_ACC".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "By  Gross Purchase";
				} else if ("BO_SALE_RET_ACC".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Purchase Return";
				} else if ("BO_PWT_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To  PWT Recievable";
				} else if ("CASH".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CHEQUE".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By  Reciepts";
				} else if (("CHQ_BOUNCE".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DR_NOTE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Reciept";
				}

				else if (resultSet2.getString("transaction_type")
						.equalsIgnoreCase("BO_CASH")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_CHEQUE")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_CR_NOTE_CASH")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")) {
					trParticulars = "To Reciept";
				} else if ((resultSet2.getString("transaction_type")
						.equalsIgnoreCase("BO_CH_BOUN")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_DR_NOTE_CASH") || resultSet2
						.getString("transaction_type").equalsIgnoreCase(
								"BO_DR_NOTE"))
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")) {
					trParticulars = "By Reciept";
				}

				else if (resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Collection Charges";
				} else if ("BO_PWT_CHARGES".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Collection Charges Recievable";
				} else if (!resultSet2.getString("account_type").contains("_")
						&& !resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By PWT Collection";
				} else if (("PWT_PLR".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_PWT_PLR"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& "AGNT_PWT_PAY".equalsIgnoreCase(resultSet2
								.getString("account_type"))) {
					trParticulars = "Player PWT";

				} else if ("PLAYER_CAS".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Player Net";
				} else if ("AGNT_TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& "PWT_PLR".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))) {
					trParticulars = "By Player Deduction";
				}

				if (accountMap.containsKey(trParticulars)) {

					accbal = accountMap.get(trParticulars);
					accountMap.put(trParticulars, accbal
							+ resultSet2.getDouble("amount"));
				} else {
					accountMap.put(trParticulars, resultSet2
							.getDouble("amount"));

				}
				/*
				 * for ledger details List
				 */
				accDetailsBean = new RetLedAccDetailsBean();

				accDetailsBean.setLedTransType(trParticulars);
				accDetailsBean.setTransactionID(resultSet2
						.getString("transaction_id"));
				accDetailsBean.setAccBalance(resultSet2.getDouble("balance"));
				accDetailsBean.setAmount(resultSet2.getDouble("amount"));
				accDetailsBean.setTrDate(resultSet2
						.getTimestamp("transaction_date"));
				accDetailsBean.setTransactionWith(resultSet2
						.getString("transaction_with"));
				ledAccBeanlist.add(accDetailsBean);

			}
			TreeMap<String, Map<String, RetLedAccDetailsBean>> dayWiseLedMap = getDayWiseLedAgtMap(
					retName, fromDate, todate);

			consolidatedList.add(accountMap);// 10 12
			consolidatedList.add(ledAccBeanlist);// detailed ledger 11 13
			consolidatedList.add(dayWiseLedMap);// day wise map 12 14

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return consolidatedList;
	}

	/**
	 * This method Genarates the PDF Report using Jasper Compiler
	 * 
	 * @param query
	 *            as String
	 * @param file
	 *            as String
	 * @param connection
	 *            as Connection
	 * @return Byte Array
	 * @throws SQLException
	 */

	public byte[] generateReport(String query, String file,
			Connection connection) throws SQLException {
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;
		HashMap parameterMap = new HashMap();

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
					resultSet);
			LedgerAction ledgerAction = new LedgerAction();
			System.out.println("received file" + file);
			System.out
					.println("heeeeeellllllllloooooooooo  generateReport33333333333");
			System.setProperty("java.awt.headless", "true");
			// System.out.println("Compiling report...");
			// jasperReport = JasperCompileManager.compileReport(file);
			System.out.println("Filling report...");
			jasperPrint = JasperFillManager.fillReport(file, new HashMap(),
					resultSetDataSource);
			System.out.println("Done!");
			System.out.println("received file" + file);
			newfile = file.substring(file.lastIndexOf("/"), file
					.lastIndexOf("."));
			System.out.println("Created File" + newfile);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("generated########" + bytes.length);
		} catch (JRException jr) {

		}
		return bytes;
	}

	/**
	 * This method Genarates the PDF Report using Jasper Compiler
	 * 
	 * @param query
	 *            as String
	 * @param file
	 *            as String
	 * @return Byte Array
	 */
	public byte[] generateReport(String query, String file, HashMap parameterMap) {
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
					resultSet);
			System.out
					.println("heeeeeellllllllloooooooooo  generateReport2222222");
			System.setProperty("java.awt.headless", "true");

			// System.out.println("Compiling report...");//commented earlier
			// jasperReport =
			// JasperCompileManager.compileReport(file);//commented earlier

			System.out.println("Filling report..." + parameterMap);
			jasperPrint = JasperFillManager.fillReport(file, parameterMap,
					resultSetDataSource);
			System.out.println("Done!");
			System.out.println("received file" + file);
			newfile = file.substring(file.lastIndexOf("/"), file
					.lastIndexOf("."));
			System.out.println("Created File" + newfile);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("generated########" + bytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		GraphReportAction reportAction = new GraphReportAction();
		reportAction.getPDFReport(bytes);
		return bytes;
	}

	public byte[] generateReport(String query, String file,
			HashMap parameterMap, String TypeName, String receipt_id) {
		DBConnect dbConnect = null;
		Connection connection = null;
		Timestamp date_entered = null;
		String tmp_receipt_id = null;
		String t_type = null;
		String tempType = null;
		int rowexists = 0;
		String type = null;
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			connection = DBConnect.getConnection();
			statement2 = connection.prepareStatement(query);
			resultSet1 = statement2.executeQuery();
			// System.out.println("type name "+TypeName +" "+
			// TypeName.equalsIgnoreCase("DebitNote")+"
			// "+TypeName.equalsIgnoreCase("Receipt"));

			if (TypeName != null && TypeName.equalsIgnoreCase("Receipt")) {
				System.out.println("RRRRRRR111111");
				rowexists = resultSet1.findColumn("t_type");

			}

			if (TypeName != null && TypeName.equalsIgnoreCase("DebitNote")) {
				System.out.println("DDDDDD");
				rowexists = resultSet1.findColumn("transaction_type");

			}
			if (TypeName != null && TypeName.equalsIgnoreCase("GOVT_COMM")) {
				rowexists = resultSet1.findColumn("transaction_type");
			}

			System.out.println(resultSet1.isFirst() + "   ##########  "
					+ rowexists);

			if (rowexists != 0) {
				while (resultSet1.next()) {

					if (TypeName.equalsIgnoreCase("receipt")) {
						t_type = resultSet1.getString("t_type");
						System.out.println("receipt " + t_type);
					} else if (TypeName.equalsIgnoreCase("DebitNote")) {
						tempType = resultSet1.getString("transaction_type");
						if (tempType.equalsIgnoreCase("CHQ_BOUNCE")) {
							t_type = "CHQ_BOUNCE";
						}
						System.out.println("Debit_Note " + t_type);
					}

					System.out
							.println("t_type    +++++++++++++++++  " + t_type);

				}

				if (t_type.equalsIgnoreCase("pwt")) {
					query = "select tmp_receipt_id,date_entered from st_se_tmp_pwt_receipt_mapping,st_se_tmp_pwt_receipt where final_receipt_id='"
							+ receipt_id + "'and tmp_receipt_id=receipt_id";
					System.out.println("pwt cheque ##### " + query);
					statement = connection.createStatement();
					resultSet = statement.executeQuery(query);

					while (resultSet.next()) {
						tmp_receipt_id = resultSet.getString("tmp_receipt_id");
						date_entered = resultSet.getTimestamp("date_entered");
					}
					parameterMap.put("tmp_receipt_id", tmp_receipt_id);
					parameterMap.put("date_entered", date_entered);

				} else if (t_type.equalsIgnoreCase("cheque")) {

					query = "select temp_receipt_id,cheque_receiving_date,receipt_id as id from st_lms_bo_cheque_temp_receipt sbctr,st_lms_bo_receipts_trn_mapping sbrtm where receipt_id='"
							+ receipt_id
							+ "'and sbctr.transaction_id=sbrtm.transaction_id";
					System.out.println("temp cheque ##### " + query);
					statement = connection.createStatement();
					resultSet = statement.executeQuery(query);

					while (resultSet.next()) {
						tmp_receipt_id = resultSet.getString("temp_receipt_id");
						date_entered = resultSet
								.getTimestamp("cheque_receiving_date");
					}
					parameterMap.put("tmp_receipt_id", tmp_receipt_id);
					parameterMap.put("date_entered", date_entered);

					if (tmp_receipt_id != null) {
						file = file + "ChequeReceiptNew.jasper";
					} else {
						file = file + "ChequeReceipt.jasper";
					}

				}

				else if (t_type.equalsIgnoreCase("CHQ_BOUNCE")) {

					query = "select temp_receipt_id,cheque_receiving_date,receipt_id as id from st_lms_bo_cheque_temp_receipt sbctr,st_lms_bo_receipts_trn_mapping sbrtm where receipt_id='"
							+ receipt_id
							+ "'and sbctr.transaction_id=sbrtm.transaction_id";
					System.out.println("temp cheque ##### " + query);
					statement = connection.createStatement();
					resultSet = statement.executeQuery(query);

					while (resultSet.next()) {
						tmp_receipt_id = resultSet.getString("temp_receipt_id");
						date_entered = resultSet
								.getTimestamp("cheque_receiving_date");
					}
					parameterMap.put("tmp_receipt_id", tmp_receipt_id);
					parameterMap.put("date_entered", date_entered);

					if (tmp_receipt_id != null) {
						file = file + "DebitNoteTmpRcpt.jasper";
					} else {
						file = file + "Debit_Note.jasper";
					}

				}

			}

			resultSet2 = statement2.executeQuery();
			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
					resultSet2);
			System.out
					.println("heeeeeellllllllloooooooooo   generateReport11111111");
			System.setProperty("java.awt.headless", "true");
			System.out.println("vineet---- " + file);
			File fileObj = new File(file);
			System.out.println("file is ========= " + fileObj.exists());

			// jasperReport =
			// JasperCompileManager.compileReport(file);//commented earlier
			System.out
					.println("Filling report...11111111111111111111111111111");

			jasperPrint = JasperFillManager.fillReport(file, parameterMap,
					resultSetDataSource);
			System.out.println("Done!");
			System.out.println("received file" + file);

			newfile = file.substring(file.lastIndexOf("/"), file
					.lastIndexOf("."));
			System.out.println("Created File" + newfile);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("generated########" + bytes.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		GraphReportAction reportAction = new GraphReportAction();
		reportAction.getPDFReport(bytes);
		return bytes;
	}

	/**
	 * This method Genarates the PDF Report using Jasper Compiler
	 * 
	 * @param query
	 *            as String
	 * @param file
	 *            as String
	 * @param parameterMap
	 *            as HAshMap
	 * @return Byte Array
	 */

	public byte[] generateReport(String query, String file,
			HashMap parameterMap, String TypeName, Timestamp fromTimeStamp,
			Timestamp dt, String id) {
		DBConnect dbConnect = null;
		Connection connection = null;
		Timestamp date_entered = null;
		String tmp_receipt_id = null;
		String t_type = null;
		String tempType = null;
		int rowexists = 0;
		String type = null;
		double openingBalance = 0.0;
		double balance = 0.0;
		double amount = 0.0;
		String timeFormat = null;
		String currencySymbol = null;
		String decimalFormat = null;
		String dateFormat = null;
		String textForTax = null;
		String tempQryForBal = query;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			connection = DBConnect.getConnection();
			statement2 = connection.prepareStatement(query);

			if (TypeName != null) {
				tempQryForBal = tempQryForBal.replace("sum(amount)", "");
				if (tempQryForBal.indexOf("group") != -1)
					tempQryForBal = tempQryForBal.substring(0, tempQryForBal
							.indexOf("group"));
				PreparedStatement fetchOpenBal = connection
						.prepareStatement(tempQryForBal);
				if (id != null) {
					System.out.println("id and typename not null");
					fetchOpenBal.setString(1, TypeName);
					fetchOpenBal.setString(2, id);
					fetchOpenBal.setTimestamp(3, fromTimeStamp);
					fetchOpenBal.setTimestamp(4, dt);

					statement2.setString(1, TypeName);
					statement2.setString(2, id);
					statement2.setTimestamp(3, fromTimeStamp);
					statement2.setTimestamp(4, dt);
					System.out
							.println("In GRH**Opening Balance Query*************** "
									+ fetchOpenBal);

					resultSet2 = fetchOpenBal.executeQuery();

					if (resultSet2.next()) {
						balance = resultSet2.getDouble("balance");
						amount = resultSet2.getDouble("amount");

					}
					openingBalance = balance - amount;
					parameterMap.put("openingBalance", openingBalance);
					System.out
							.println("In GRH**Opening Balance *********************"
									+ openingBalance);

				}

				else {
					System.out.println("id  is null and typename not null");
					fetchOpenBal.setString(1, TypeName);
					fetchOpenBal.setTimestamp(2, fromTimeStamp);
					fetchOpenBal.setTimestamp(3, dt);

					statement2.setString(1, TypeName);
					statement2.setTimestamp(2, fromTimeStamp);
					statement2.setTimestamp(3, dt);
					resultSet2 = fetchOpenBal.executeQuery();
					System.out
							.println("In GRH**Opening Balance Query*************** "
									+ fetchOpenBal);
					if (resultSet2.next()) {
						balance = resultSet2.getDouble("balance");
						amount = resultSet2.getDouble("amount");

					}
					openingBalance = balance - amount;
					parameterMap.put("openingBalance", openingBalance);
					System.out
							.println("In GRH**Opening Balance *********************"
									+ openingBalance);
				}

			} else {

				if (id != null) {
					System.out.println("id  not  null and typename  null");
					statement2.setTimestamp(1, fromTimeStamp);
					statement2.setTimestamp(2, dt);
					statement2.setString(3, id);
					System.out.println(" id !=0 " + statement2);
				} else {
					System.out.println("id   and typename  null");
					if (fromTimeStamp != null) {
						statement2.setTimestamp(1, fromTimeStamp);
						if (dt != null) {
							statement2.setTimestamp(2, dt);
							System.out.println("dt!=null " + statement2);
						}
					}
				}
			}

			System.out.println("In GRH**" + statement2.toString());

			LedgerAction ledgerAction = new LedgerAction();
			resultSet2 = statement2.executeQuery();
			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
					resultSet2);
			System.setProperty("java.awt.headless", "true");
			System.out.println("In GRH**File***********" + file);

			// jasperReport = JasperCompileManager.compileReport(file);
			// //commented earlier
			System.out.println("In GRH**Filling report******************");

			jasperPrint = JasperFillManager.fillReport(file, parameterMap,
					resultSetDataSource);
			System.out.println("Done!");
			System.out.println("received file" + file);
			newfile = file.substring(file.lastIndexOf("/"), file
					.lastIndexOf("."));
			System.out.println("Created File" + newfile);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("generated########" + bytes.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		GraphReportAction reportAction = new GraphReportAction();
		reportAction.getPDFReport(bytes);
		return bytes;
	}

	/*
	 * This method is for Temporary PWT Receipt
	 */
	public byte[] generateTempPWTReceipt(String receipt_id, String boOrgName,
			int userOrgID, String root_path, int agentOrgId)
			throws SQLException {

		System.out.println("Inside  generateTempPWTReceipt");

		connection = DBConnect.getConnection();
		Statement stmt = null;
		stmt = connection.createStatement();
		ResultSet rs1 = null;
		String addr_line1 = null;
		String addr_line2 = null;
		String city = null;
		String name = null;

		rs1 = stmt
				.executeQuery("select name,addr_line1,addr_line2,city,state_code,country_code from st_lms_organization_master som where som.organization_id ='"
						+ agentOrgId + "'");
		// System.out.println("Add Query "+"select
		// name,addr_line1,addr_line2,city,state_code,country_code from
		// st_lms_organization_master som,(select organization_id from
		// st_lms_user_master where user_id='"+agentId+"')stum where
		// stum.organization_id=som.organization_id");;
		while (rs1.next()) {
			name = rs1.getString("name");
			addr_line1 = rs1.getString("addr_line1");
			addr_line2 = rs1.getString("addr_line2");
			city = rs1.getString("city");
			System.out.println(name + addr_line1 + addr_line2 + city);
		}
		String query = null;
		String file = null;

		HashMap parameterMap = new HashMap();

		System.out
				.println("======================= reciept_id : " + receipt_id);
		System.setProperty("java.awt.headless", "true");
		String orgAdd = null;
		String addQuery = null;
		String vatNum = null;
		LedgerHelper ledger = new LedgerHelper();
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);
			addQuery = QueryManager.getST6AddressQuery();
			orgAdd = ledger.getAddress(addQuery, "" + userOrgID, null);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("header", boOrgName);
			parameterMap.put("addr_line1", addr_line1);
			parameterMap.put("addr_line2", addr_line2);
			parameterMap.put("city", city);
			parameterMap.put("name", name);
			parameterMap.put("name", name);
			parameterMap.put("receipt_id", receipt_id);

			statement = connection.createStatement();
			query = "select game_name,no_of_tickets,prize_amt,date_entered from st_se_game_master sgm,st_se_tmp_pwt_receipt_detail stpr,st_se_tmp_pwt_receipt stp where stpr.receipt_id='"
					+ receipt_id
					+ "'  and sgm.game_id=stpr.game_id  and stpr.receipt_id=stp.receipt_id order by game_name,prize_amt";

			System.out.println("qqqqqqqqqqq " + query);
			resultSet = statement.executeQuery(query);

			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
					resultSet);

			root_path = root_path.replace("\\", "/");
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/TempPWTReceipt.jasper";
			// file="C:/Documents and
			// Settings/USER12/workspace/LMS_DELIVERY/WebRoot/com/skilrock/lms/web/reportsMgmt/rawReports/TempPWTReceipt.jasper";
			System.out.println("received file" + file);
			System.out
					.println("heeeeeellllllllloooooooooo  generateReport33333333333");
			System.setProperty("java.awt.headless", "true");
			// System.out.println("Compiling report...");
			// jasperReport = JasperCompileManager.compileReport(file);
			System.out.println("Filling report...");
			// System.out.println(jasperReport.getName());
			// System.out.println(jasperReport.getDatasets());
			jasperPrint = JasperFillManager.fillReport(file, parameterMap,
					resultSetDataSource);
			System.out.println("Done!");
			System.out.println("received file" + file);
			newfile = file.substring(file.lastIndexOf("/"), file
					.lastIndexOf("."));
			System.out.println("Created File" + newfile);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			// JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/pp.pdf"
			// );
			System.out.println("generated########" + bytes.length);
		} catch (JRException jr) {
			jr.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		GraphReportAction reportAction = new GraphReportAction();
		System.out.println("generated*************" + bytes.length);
		reportAction.getPDFReport(bytes);
		return bytes;

	}

	public String getAddress(String query) {
		Connection connection = null;

		String add1 = null;
		String add2 = null;
		String add3 = null;

		try {
			connection = DBConnect.getConnection();
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs1 = null;
			rs1 = stmt.executeQuery(query);
			while (rs1.next()) {

				add1 = rs1.getString("addr_line1");
				add2 = rs1.getString("addr_line2");
				add3 = rs1.getString("city");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return add1 + " " + add2 + " " + add3;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public Map<String, RetLedAccDetailsBean> getDayWiseAgtLeder(String retName,
			String fromDate, String todate) {
		Map<String, RetLedAccDetailsBean> ledDayMap = new HashMap<String, RetLedAccDetailsBean>();
		String trParticulars = null;
		String trDate = null;
		double accbal = 0.0;
		RetLedAccDetailsBean retLedAccDetailsBean = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dateDB = new SimpleDateFormat("yyyy-MM-dd");
		try {
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ retName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "'");

			System.out
					.println("Queryyyy   "
							+ "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ retName + "' ) and transaction_date >= '"
							+ fromDate + "' and transaction_date <= '" + todate
							+ "'");
			while (resultSet2.next()) {

				if (("SALE_RET".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DG_REFUND_FAILED".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_REFUND_CANCEL"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By Sales Return";
				} else if (("SALE".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_SALE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Gross Sales";
				} else if ("BO_SALE_ACC".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "By  Gross Purchase";
				} else if ("BO_SALE_RET_ACC".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Purchase Return";
				} else if ("BO_PWT_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To  PWT Recievable";
				} else if ("CASH".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CHEQUE".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By  Reciepts";
				} else if (("CHQ_BOUNCE".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DR_NOTE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Reciept";
				}

				else if (resultSet2.getString("transaction_type")
						.equalsIgnoreCase("BO_CASH")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_CHEQUE")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_CR_NOTE_CASH")
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")) {
					trParticulars = "To Reciept";
				} else if ((resultSet2.getString("transaction_type")
						.equalsIgnoreCase("BO_CH_BOUN")
						|| resultSet2.getString("transaction_type")
								.equalsIgnoreCase("BO_DR_NOTE_CASH") || resultSet2
						.getString("transaction_type").equalsIgnoreCase(
								"BO_DR_NOTE"))
						&& resultSet2.getString("account_type")
								.equalsIgnoreCase("BO_BANK_ACC")) {
					trParticulars = "By Reciept";
				}

				else if (resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Collection Charges";
				} else if ("BO_PWT_CHARGES".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Collection Charges Recievable";
				} else if (!resultSet2.getString("account_type").contains("_")
						&& !resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By PWT Collection";
				} else if (("PWT_PLR".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_PWT_PLR"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& "AGNT_PWT_PAY".equalsIgnoreCase(resultSet2
								.getString("account_type"))) {
					trParticulars = "Player PWT";

				} else if ("PLAYER_CAS".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Player Net";
				} else if ("AGNT_TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& ("PWT_PLR".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_PWT_PLR"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Player Deduction";
				}

				System.out.println(" trParticulars ===== " + trParticulars);

				if (ledDayMap.containsKey(trParticulars)) {

					retLedAccDetailsBean = ledDayMap.get(trParticulars);

					retLedAccDetailsBean.setLedTransType(trParticulars);
					retLedAccDetailsBean.setAccBalance(resultSet2
							.getDouble("balance"));
					retLedAccDetailsBean.setAmount(retLedAccDetailsBean
							.getAmount()
							+ resultSet2.getDouble("amount"));
					retLedAccDetailsBean.setTrDateStr(dateFormat.format(dateDB
							.parse(fromDate)));
					ledDayMap.put(trParticulars, retLedAccDetailsBean);
					System.out.println("Les day Map ===== " + ledDayMap);

				} else {
					retLedAccDetailsBean = new RetLedAccDetailsBean();
					System.out.println("======trParticulars===== "
							+ trParticulars + " ======== " + fromDate);
					retLedAccDetailsBean.setLedTransType(trParticulars);
					retLedAccDetailsBean.setAccBalance(resultSet2
							.getDouble("balance"));
					retLedAccDetailsBean.setAmount(resultSet2
							.getDouble("amount"));
					retLedAccDetailsBean.setTrDateStr(dateFormat.format(dateDB
							.parse(fromDate)));
					ledDayMap.put(trParticulars, retLedAccDetailsBean);

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=======================  " + ledDayMap);
		return ledDayMap;
	}

	public TreeMap<String, Map<String, RetLedAccDetailsBean>> getDayWiseLedAgtMap(
			String retName, Timestamp fromDate, Timestamp todate) {
		List frTimeList = new ArrayList();
		List toTimeList = new ArrayList();
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.setTimeInMillis(fromDate.getTime());
		to.setTimeInMillis(todate.getTime());
		Map<String, RetLedAccDetailsBean> ledDayMap = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = null;
		String frDtString = null;
		String toDtString = null;

		while (from.before(to)) {
			frTimeList.add(from.getTime());
			from.add(Calendar.DATE, 1);
			toTimeList.add(from.getTime());
		}
		System.out.println("frTimeList " + frTimeList);
		System.out.println("toTimeList " + toTimeList);
		TreeMap<String, Map<String, RetLedAccDetailsBean>> ledDayWiseMap = new TreeMap<String, Map<String, RetLedAccDetailsBean>>();
		System.out.println("fomr date ####### "
				+ formatDB.format((Date) frTimeList.get(0)));
		System.out.println("To   date ####### "
				+ formatDB.format((Date) toTimeList.get(0)));
		for (int i = 0; i < frTimeList.size(); i++) {
			frDtString = formatDB.format((Date) frTimeList.get(i));
			toDtString = formatDB.format((Date) toTimeList.get(i));
			ledDayMap = getDayWiseAgtLeder(retName, frDtString, toDtString);
			dateString = format.format((Date) frTimeList.get(i));
			if (ledDayMap.size() > 0) {
				ledDayWiseMap.put(dateString, ledDayMap);
			}
		}

		System.out.println("DAYWISE MAP " + ledDayWiseMap.size());
		return ledDayWiseMap;

	}

	public Map<String, AgtLedAccDetailsBean> getDayWiseLeder(String agentName,
			String fromDate, String todate) {
		Map<String, AgtLedAccDetailsBean> ledDayMap = new HashMap<String, AgtLedAccDetailsBean>();
		String trParticulars = null;
		String trDate = null;
		double accbal = 0.0;
		AgtLedAccDetailsBean agtLedAccDetailsBean = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dateDB = new SimpleDateFormat("yyyy-MM-dd");
		try {
			resultSet2 = statement
					.executeQuery("select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = (select organization_id from st_lms_organization_master where name = '"
							+ agentName
							+ "' ) and transaction_date >= '"
							+ fromDate
							+ "' and transaction_date <= '"
							+ todate
							+ "'");
			while (resultSet2.next()) {

				if (("SALE_RET".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DG_REFUND_CANCEL".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_REFUND_FAILED"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By Sales Return";
				} else if (("SALE".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_SALE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Gross Sales";
				} else if (("PWT_PLR".equalsIgnoreCase(resultSet2
						.getString("transaction_type")) || "DG_PWT_PLR"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& "PWT_PAY".equalsIgnoreCase(resultSet2
								.getString("account_type"))) {
					trParticulars = "Player PWT";
				} else if ("PLAYER_CAS".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Player Net";
				} else if ("CASH".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CHEQUE".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")
						|| "CR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "By  Reciepts";
				} else if (("CHQ_BOUNCE".equalsIgnoreCase(resultSet2
						.getString("transaction_type"))
						|| "DR_NOTE_CASH".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DR_NOTE"
						.equalsIgnoreCase(resultSet2
								.getString("transaction_type")))
						&& !resultSet2.getString("account_type").contains("_")) {
					trParticulars = "To Reciept";
				} else if (!resultSet2.getString("account_type").contains("_")
						&& !resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By PWT Collection";
				} else if (resultSet2.getString("account_type").contains("#")
						&& ("PWT".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))
								|| "PWT_AUTO".equalsIgnoreCase(resultSet2
										.getString("transaction_type"))
								|| "DG_PWT".equalsIgnoreCase(resultSet2
										.getString("transaction_type")) || "DG_PWT_AUTO"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Collection Charges";
				} else if ("GOVT_COMM".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To Govt Contribution";
				} else if ("VAT_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))) {
					trParticulars = "To VAT Contribution";
				} else if ("TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& "TDS".equalsIgnoreCase(resultSet2
								.getString("transaction_type"))) {
					trParticulars = "To Deductions";
				} else if ("TDS_PAY".equalsIgnoreCase(resultSet2
						.getString("account_type"))
						&& ("PWT_PLR".equalsIgnoreCase(resultSet2
								.getString("transaction_type")) || "DG_PWT_PLR"
								.equalsIgnoreCase(resultSet2
										.getString("transaction_type")))) {
					trParticulars = "By Player Deduction";
				}

				if (ledDayMap.containsKey(trParticulars)) {

					agtLedAccDetailsBean = ledDayMap.get(trParticulars);

					agtLedAccDetailsBean.setLedTransType(trParticulars);
					agtLedAccDetailsBean.setAccBalance(resultSet2
							.getDouble("balance"));
					agtLedAccDetailsBean.setAmount(agtLedAccDetailsBean
							.getAmount()
							+ resultSet2.getDouble("amount"));
					agtLedAccDetailsBean.setTrDateStr(dateFormat.format(dateDB
							.parse(fromDate)));
					ledDayMap.put(trParticulars, agtLedAccDetailsBean);
				} else {
					agtLedAccDetailsBean = new AgtLedAccDetailsBean();
					System.out.println("======trParticulars===== "
							+ trParticulars + " ======== " + fromDate);
					agtLedAccDetailsBean.setLedTransType(trParticulars);
					agtLedAccDetailsBean.setAccBalance(resultSet2
							.getDouble("balance"));
					agtLedAccDetailsBean.setAmount(resultSet2
							.getDouble("amount"));
					agtLedAccDetailsBean.setTrDateStr(dateFormat.format(dateDB
							.parse(fromDate)));
					ledDayMap.put(trParticulars, agtLedAccDetailsBean);

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=======================  " + ledDayMap);
		return ledDayMap;
	}

	public TreeMap<String, Map<String, AgtLedAccDetailsBean>> getDayWiseLedMap(
			String agentName, Timestamp fromDate, Timestamp todate) {
		List frTimeList = new ArrayList();
		List toTimeList = new ArrayList();
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.setTimeInMillis(fromDate.getTime());
		to.setTimeInMillis(todate.getTime());
		Map<String, AgtLedAccDetailsBean> ledDayMap = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = null;
		String frDtString = null;
		String toDtString = null;

		while (from.before(to)) {
			frTimeList.add(from.getTime());
			from.add(Calendar.DATE, 1);
			toTimeList.add(from.getTime());
		}
		System.out.println("frTimeList " + frTimeList);
		System.out.println("toTimeList " + toTimeList);
		TreeMap<String, Map<String, AgtLedAccDetailsBean>> ledDayWiseMap = new TreeMap<String, Map<String, AgtLedAccDetailsBean>>();

		for (int i = 0; i < frTimeList.size(); i++) {
			frDtString = formatDB.format((Date) frTimeList.get(i));
			toDtString = formatDB.format((Date) toTimeList.get(i));
			ledDayMap = getDayWiseLeder(agentName, frDtString, toDtString);
			dateString = format.format((Date) frTimeList.get(i));
			if (ledDayMap.size() > 0) {
				ledDayWiseMap.put(dateString, ledDayMap);
			}
		}

		System.out.println("DAYWISE MAP " + ledDayWiseMap.size());
		return ledDayWiseMap;

	}

	// This Function checks for service Code of the GOVT_RCPT
	public String checkServiceCode(int id) {
		String serviceCode = "";
		Connection connection = null;

		String query = null;
		try {
			connection = DBConnect.getConnection();
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs1 = null;
			query = "select lbgt.service_code from st_lms_bo_receipts lbr, st_lms_bo_receipts_trn_mapping lbrtm , st_lms_bo_govt_transaction lbgt where lbr.receipt_id = lbrtm.receipt_id and lbrtm.transaction_id = lbgt.transaction_id and lbr.receipt_id = "
					+ id;
			rs1 = stmt.executeQuery(query);

			while (rs1.next()) {
				serviceCode = rs1.getString("service_code");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return serviceCode;
	}

	/**
	 * This method send the currently present Games and their Game Id's for the
	 * User Interface
	 * 
	 * @return Map of Games
	 */

	public Map getDefaultSelectGames(String gameStatus) {
		Map m = new LinkedHashMap();
		Connection connection = null;

		String query = null;
		try {
			connection = DBConnect.getConnection();
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs1 = null;
			query = "select game_id,game_nbr from st_se_game_master where game_status='"
					+ gameStatus + "'";
			System.out.println("query -----------     " + query);
			if (gameStatus.equals("--Please Select--")) {
				query = "select game_id,game_nbr from st_se_game_master where game_status!='NEW' ";
				System.out
						.println("query******************************************* -----------please select----     "
								+ query);
			}

			rs1 = stmt.executeQuery(query);

			while (rs1.next()) {
				m.put(rs1.getInt("game_id"), rs1.getInt("game_nbr"));
			}
			System.out.println(m);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return m;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] createMultiTextReportBO(int receipt_id, String boOrgName,int userOrgID, String root_path) {
		byte[] byteArr = null;
		
		String isVatApplicable = null;
		String isCommApplicable = null;
		String receipt_type = null;
		String receipt_no = null;
		Timestamp voucher_date = null;

		connection = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String file = null;
		String trnType = null;

		HashMap parameterMap = new HashMap();

		System.out
				.println("======================= reciept_id : " + receipt_id);
		System.setProperty("java.awt.headless", "true");
		String orgAdd = null;
		String addQuery = null;
		String vatNum = null;
		String fileName = null;
		String timeFormat = null;
		String currencySymbol = null;
		String dateFormat = null;
		String decimalFormat = null;
		String tdsPartyName = null;
		String vatPartyName = null;
		String govComPartyName = null;
		String isScratch = null;
		String isDraw = null;
		Statement statement = null;
		ResultSet set = null;
		String textForTax = null;

		try {
			timeFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("TIME_FORMAT");
			currencySymbol = (String) ServletActionContext.getServletContext()
					.getAttribute("CURRENCY_SYMBOL");
			decimalFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("DECIMAL_FORMAT");
			dateFormat = (String) ServletActionContext.getServletContext()
					.getAttribute("date_format");
			textForTax = (String) ServletActionContext.getServletContext()
					.getAttribute("TEXT_FOR_TAX");
			isScratch = (String) ServletActionContext.getServletContext()
					.getAttribute("IS_SCRATCH");
			isDraw = (String) ServletActionContext.getServletContext()
					.getAttribute("IS_DRAW");
			parameterMap.put("decimal_format", decimalFormat);
			parameterMap.put("timeFormat", timeFormat);
			parameterMap.put("currency_symbol", currencySymbol);
			parameterMap.put("dateFormat", dateFormat);
			parameterMap.put("textForTax", textForTax);

			LedgerHelper ledger = new LedgerHelper();
			addQuery = QueryManager.getST6AddressQuery();
			orgAdd = ledger.getAddress(addQuery, "" + userOrgID, null);
			parameterMap.put("orgAdd", orgAdd);
			parameterMap.put("header", boOrgName);
			
			vatNum = ledger.getVatNumber("" + userOrgID);
			parameterMap.put("vatNum", vatNum);
			stmt = connection.createStatement();
			query = "select sbtm.transaction_id,transaction_type,sbr.receipt_type,sbr.generated_id, sbr.voucher_date from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_transaction_master sbtm,st_lms_bo_receipts sbr where sbrtm.receipt_id='"
					+ receipt_id
					+ "' and  sbrtm.transaction_id=sbtm.transaction_id and sbr.receipt_id=sbrtm.receipt_id";
			System.out.println("-------------------- " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				receipt_type = rs.getString("receipt_type");
				receipt_no = rs.getString("generated_id");
				trnType = rs.getString("transaction_type");
				voucher_date = rs.getTimestamp("voucher_date");
			}
			/*
			 * query for temp receipt no
			 */

			parameterMap.put("receipt_no", receipt_no);
			parameterMap.put("voucher_date", voucher_date);
			isVatApplicable = (String) ServletActionContext.getServletContext()
					.getAttribute("VAT_APPLICABLE");
			isCommApplicable = (String) ServletActionContext
					.getServletContext().getAttribute("COMM_APPLICABLE");
			if (receipt_type.equalsIgnoreCase("DG_INVOICE")) {
				root_path = root_path.replace("\\", "/");
				if ("DG_SALE".equalsIgnoreCase(trnType)) {
					query = "select id,game_name,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,taxable_sale,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select agent_id,id,game_id ,agent_comm as comm_amt, mrp_amt, net_amt, vat_amt as vat, taxable_sale,sbs.transaction_id, btm.transaction_date, voucher_date from st_dg_bo_sale sbs, st_lms_bo_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id = sale.agent_id order by transaction_date";
					System.out
							.println(" DG_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcComVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcComNoVat.jasper"; // changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGSaleInvcRdpNoVat.jasper";// changes
						// .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGSaleInvcRdpVat.jasper";//
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			if(receipt_type.equalsIgnoreCase("CS_INVOICE")){
				root_path = root_path.replace("\\", "/");
			if ("CS_SALE".equalsIgnoreCase(trnType)) {
				query = "select id,product_code,description,transaction_date,voucher_date,mrp_amt as sale, net_amt, comm_amt,vat,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id from st_cs_product_master scpm ,st_lms_organization_master som ,(select agent_id,voucher_date,id,product_id ,(mrp_amt*agent_comm/100) as comm_amt, mrp_amt, net_amt, vat_amt as vat, sbs.transaction_id, btm.transaction_date from st_cs_bo_sale sbs, st_lms_bo_transaction_master btm, (select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+
						+ receipt_id
						+ "')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.product_id = scpm.product_id and scpm.product_id and som.organization_id = sale.agent_id order by transaction_date";
				System.out
						.println(" CS_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcComVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("yes")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcComNoVat.jasper"; // changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("no")) {
					fileName = "CSSaleInvcRdpNoVat.jasper";// changes
					// .jasper to
					// jrxml
				} else if (isCommApplicable.equalsIgnoreCase("no")
						&& isVatApplicable.equalsIgnoreCase("yes")) {
					fileName = "CSSaleInvcRdpVat.jasper";//
				}
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			byteArr = generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			
			if(receipt_type.equalsIgnoreCase("OLA_INVOICE")){
				root_path = root_path.replace("\\", "/");
			if ("OLA_DEPOSIT".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,deposit_amt as deposit, net_amt , (deposit_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,voucher_date,id,wallet_name ,agent_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_deposit sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id,slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
				System.out
						.println(" OLA_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLADepositInvcRdpNoVat.jasper";
			}
			if ("OLA_CASHCARD_SALE".equalsIgnoreCase(trnType)) {
				query = "select distributor name,'india' addr_line1,'XYZ' addr_line2,'delhi' city,voucher_date,id,wallet_name ,totalamount , sale_comm_rate,amount,sbs.transaction_id, btm.transaction_date from st_ola_bo_distributor_transaction sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select  slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = "+receipt_id+")innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id";
				System.out
						.println(" OLA_INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLACashCardSaleInvcRdpNoVat.jasper";
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			byteArr =  generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			if(receipt_type.equalsIgnoreCase("OLA_RECEIPT")){
				root_path = root_path.replace("\\", "/");
			if ("OLA_WITHDRAWL".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,withdrawl_amt as withdrawal, net_amt ,(withdrawl_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,voucher_date,id,wallet_name ,agent_comm as comm_amt, withdrawl_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_withdrawl sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id,slbr.voucher_date as voucher_date, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
				System.out
						.println(" OLA_RECEIPT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLAWithdrawalInvcRdpNoVat.jasper";
			}
			// Check for OLA_COMMISSION transaction type
			if ("OLA_COMMISSION".equalsIgnoreCase(trnType)) {
				query = "select id,wallet_name,transaction_date,voucher_date,comm_amt,netgame.tds_comm_rate,net_comm_amt,name,addr_line1,addr_line2,city,netgame.transaction_id as transaction_id from st_ola_wallet_master sdgm ,st_lms_organization_master som ,(select agent_org_id ,voucher_date,id,wallet_id ,agt_claim_comm as comm_amt,sbs.tds_comm_rate,agt_net_claim_comm as net_comm_amt , sbs.transaction_id, btm.transaction_date from st_ola_bo_comm sbs, st_lms_bo_transaction_master btm, (select slbr.party_id as agent_id,slbr.voucher_date as voucher_date,slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id)netgame where netgame.wallet_id = sdgm.wallet_id and som.organization_id = netgame.agent_org_id";
				System.out
						.println(" OLA_RECEIPT queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
			fileName = "OLANetGameInvcRdpNoVat.jasper";
			}
			file = root_path
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
					+ fileName;
			byteArr = generateReport(query, file, parameterMap, null, null, null,
					null);
		}
			if (receipt_type.equalsIgnoreCase("INVOICE")) {
				System.out
						.println("================== inside Invoice =================");
				System.out.println(root_path);
				root_path = root_path.replace("\\", "/");
				if ("SALE".equalsIgnoreCase(trnType)) {
					// VAT AMOUNT AMBIGUOUS "select
					// distinct(transaction_id),agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name
					// ,sale,taxable_sale,vat_amt,ticket_price,
					// nbr_of_tickets_per_book ,
					// nbr_of_books,total_tickets,transaction_date,order_id,order_date,comm_amt
					// from (select
					// agent_sale_comm_rate,name,addr_line1,addr_line2,city,
					// agent_org_id,id, game_name
					// ,sale,taxable_sale,sale.vat_amt,ticket_price,
					// nbr_of_tickets_per_book ,
					// nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets,transaction_date,transaction_id,order_id,order_date,comm_amt
					// from st_lms_organization_master som ,( select
					// agent_sale_comm_rate,id,sale.agent_org_id,game_name
					// ,ticket_price, nbr_of_tickets_per_book ,
					// sale.nbr_of_books,taxable_sale, vat_amt,(net_amt) as
					// sale,transaction_date,transaction_id,sale.order_id,order_date,comm_amt
					// from st_se_game_master ,st_se_bo_order sbo, (select
					// sbat.agent_org_id,id,sbat.game_id , nbr_of_books,
					// mrp_amt,sbat.transaction_id,
					// vat_amt,taxable_sale,net_amt,transaction_date,order_id,comm_amt
					// from st_se_bo_agent_transaction sbat ,
					// st_se_bo_order_invoices sboi,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' )innermost ,(select
					// transaction_date,transaction_id from
					// st_lms_bo_transaction_master )sbtm where
					// sbat.transaction_id = innermost.transaction_id and
					// innermost.transaction_id=sbtm.transaction_id and
					// sboi.invoice_id=innermost.id )sale where sale.game_id =
					// st_se_game_master.game_id and sbo.order_id=sale.order_id
					// )result1 where som.organization_id=result1.agent_org_id )
					// res";
					query = "select distinct(transaction_id),agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name ,sale,taxable_sale,vat_amt,ticket_price, nbr_of_tickets_per_book , nbr_of_books,total_tickets,transaction_date,order_id,order_date,comm_amt from (select agent_sale_comm_rate,name,addr_line1,addr_line2,city, agent_org_id,id, game_name ,sale,taxable_sale,vat_amt,ticket_price, nbr_of_tickets_per_book , nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as total_tickets,transaction_date,transaction_id,order_id,order_date,comm_amt from  st_lms_organization_master som ,( select agent_sale_comm_rate,id,sale.agent_org_id,game_name ,ticket_price, nbr_of_tickets_per_book , sale.nbr_of_books,taxable_sale, sale.vat_amt,(net_amt) as sale,transaction_date,transaction_id,sale.order_id,order_date,comm_amt from  st_se_game_master ,st_se_bo_order sbo, (select sbat.agent_org_id,id,sbat.game_id , nbr_of_books, mrp_amt,sbat.transaction_id, sbat.vat_amt,taxable_sale,net_amt,transaction_date,order_id,comm_amt from st_se_bo_agent_transaction sbat , st_se_bo_order_invoices sboi,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "' )innermost ,(select transaction_date,transaction_id from st_lms_bo_transaction_master     )sbtm where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id=sbtm.transaction_id and sboi.invoice_id=innermost.id )sale where sale.game_id = st_se_game_master.game_id and sbo.order_id=sale.order_id  )result1   where som.organization_id=result1.agent_org_id ) res order by transaction_date";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcComVat.jasper"; // changes .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcComNoVat.jasper"; // changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "InvcRdpNoVat.jasper";// changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "InvcRdpVat.jasper";//
					}
				} 
				else if ("LOOSE_SALE".equalsIgnoreCase(trnType)) {
					
					query = "select id,game_name,organization_id,transaction_date,nbrOfTickets, net_amt sale, agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,agtSaleCommRate  from st_lms_organization_master som ,(select agent_id,id,game_name ,agent_sale_comm_rate as agent_sale_comm_rate,nbrOfTickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,agtSaleCommRate from st_se_bo_agent_loose_book_transaction sbs,st_se_game_master game, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id ='"+ receipt_id+ "' )innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.agent_id";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcComVat.jasper"; // changes .jasper to
						// jrxml
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcComNoVat.jasper"; // changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleInvcRdpNoVat.jasper";// changes .jasper
						// to jrxml
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleInvcRdpVat.jasper";//
					}
				}
				
				else {
					// select id,game_name,taxable_sale, vat_amt,net_amt as
					// sale,comm_amt,transaction_id from st_dg_game_master sdgm
					// ,(select id,game_id ,agent_comm as comm_amt, mrp_amt,
					// vat_amt,taxable_sale,net_amt,sbat.transaction_id from
					// st_dg_bo_sale sbat,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' ) innermost where
					// sbat.transaction_id = innermost.transaction_id )sale
					// where sale.game_id = sdgm.game_id and
					// agent_org_id='"+userOrgID+"'";
					// query=" select id,game_name,taxable_sale,
					// sale.vat_amt,net_amt as sale,comm_amt,sale.transaction_id
					// from st_dg_game_master sdgm ,(select
					// agent_org_id,id,game_id ,agent_comm as comm_amt, mrp_amt,
					// vat_amt,taxable_sale,net_amt,sbat.transaction_id from
					// st_dg_bo_sale sbat,(select receipt_id as
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id = '"+receipt_id+"' ) innermost where
					// sbat.transaction_id = innermost.transaction_id )sale
					// where sale.game_id = sdgm.game_id and
					// agent_org_id='"+userOrgID+"'";
					query = "select id,game_name,taxable_sale, sale.vat_amt,net_amt as sale,comm_amt,name,addr_line1,addr_line2,city,sale.transaction_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select agent_org_id,id,game_id ,agent_comm as comm_amt, mrp_amt, vat_amt,taxable_sale,net_amt,sbat.transaction_id from  st_dg_bo_sale sbat,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id )sale where sale.game_id = sdgm.game_id and sale.agent_org_id='"
							+ userOrgID
							+ "' and som.organization_id=sale.agent_org_id";
					System.out.println(" INVOICE queryyyyyyyyyyyyyyyy BOOOOO "
							+ query);

					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcComVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcComNoVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGInvcRdpNoVat.jasper";//
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGInvcRdpVat.jasper";//
					}
				}
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);

			}
			
			if (receipt_type.equalsIgnoreCase("DG_RECEIPT")) {
				if (trnType.equalsIgnoreCase("DG_PWT")
						|| trnType.equalsIgnoreCase("DG_PWT_AUTO")) {
					query = "select id,game_nbr,game_name,t_type,transaction_date,voucher_date,pwt_amt as amount, net_amt as net_amount,comm_amt as comm_amount,name,addr_line1,addr_line2,city,sale.transaction_id as trans_id from st_dg_game_master sdgm ,st_lms_organization_master som ,(select 'pwt' t_type, agent_id,id,game_id ,comm_amt, pwt_amt, net_amt,sbat.transaction_id,btm.transaction_date,voucher_date from  st_dg_bo_pwt sbat,st_lms_bo_transaction_master btm,(select slbr.voucher_date as voucher_date,slbr.party_id as agent_id, slbr.receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"
							+ receipt_id
							+ "' ) innermost where  sbat.transaction_id = innermost.transaction_id and innermost.transaction_id = btm.transaction_id)sale where sale.game_id = sdgm.game_id and som.organization_id=sale.agent_id order by transaction_date";
					System.out
							.println("DG_RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGNewPWTReceipt.jasper";
					byteArr = generateReport(query, file, parameterMap, "Receipt",
							receipt_id + "");
				}
			} 
			
			else if(receipt_type.equalsIgnoreCase("RECEIPT") && trnType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				query = "select id,wallet_name,transaction_date,voucher_date,commission_calculated as commission_calculated, name,addr_line1,addr_line2,city,netGaming.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,id,wallet_name, commission_calculated,sbs.transaction_id, btm.transaction_date from st_ola_bo_agt_commission sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)netGaming where som.organization_id = netGaming.agent_id";
				System.out
						.println(" RECEIPT OLA COMMISSION queryyyyyyyyyyyyyyyy BOOOOO "
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/OLAReceipt.jasper";
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);
			}
			else if (receipt_type.equalsIgnoreCase("RECEIPT")) {
				System.out
						.println("================== inside reciept111 ================="
								+ trnType);
				if (trnType.equalsIgnoreCase("cheque")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,issuing_party_name,user_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id from  st_lms_organization_master som , (select 'cheque' t_type,cheque_agent as agent,cheque_amt as amount,user_name,cheque_amt as net_amount, cheque_id as trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,receipt_id,transaction_date,temp_receipt_id  from (select receipt_id ,slum.user_name,sbsc.agent_org_id as cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id as cheque_id,transaction_date,temp_receipt_id from st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt sbctr, (select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id="
							+receipt_id+
							") innermost,(select user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm,(select user_id, user_name from st_lms_user_master)slum where sbsc.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id  and sbsc.transaction_id=sbctr.transaction_id and slum.user_id = sbtm.user_id) cheque ) result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT Cheque queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/";
				} else if (trnType.equalsIgnoreCase("bank_deposit")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,user_name,amount,transaction_date from  st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,user_name,cash_amount as amount ,trans_id,receipt_id,transaction_date from ( select receipt_id,sbct.agent_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as trans_id,slum.user_name,transaction_date from  st_lms_bo_bank_deposit_transaction sbct,( select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "' ) innermost, (select  user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id  ) cash )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT BANK DEPOSIT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("CASH")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,user_name,amount,transaction_date from  st_lms_organization_master som ,(select 'cash' t_type,cash_agent  as agent,user_name,cash_amount as amount ,trans_id,receipt_id,transaction_date from ( select receipt_id,sbct.agent_org_id as cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as trans_id,slum.user_name,transaction_date from  st_lms_bo_cash_transaction sbct,( select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id="
							+receipt_id+
							") innermost, (select user_id, transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum  where sbct.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id ) cash )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT CASH queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/CashReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("PWT")
						|| trnType.equalsIgnoreCase("PWT_AUTO")) {
					query = "select name,addr_line1,addr_line2,city,t_type,agent,trans_id,receipt_id as id,amount,net_amount ,comm_amount,transaction_date,game_nbr,game_name from st_lms_organization_master som ,(select 'pwt' t_type,pwt_agent  as agent,pwt_amount as amount,net_amount ,comm_amount ,pwt_trans_id as trans_id, receipt_id,transaction_date,game_nbr,game_name from  (select receipt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as pwt_amount, sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as pwt_trans_id, transaction_date,sbp.game_id,game_nbr,game_name from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master )sgm, (select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm where sbp.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id group by sbp.transaction_id ) pwt )result where result.agent = som.organization_id order by transaction_date";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/PWTReceipt.jasper";
				} else if (trnType.equalsIgnoreCase("DG_PWT")
						|| trnType.equalsIgnoreCase("DG_PWT_AUTO")) {
					query = "select name,addr_line1,addr_line2,city,t_type,trans_id,receipt_id as id,amount,net_amount ,comm_amt,game_nbr,game_name from st_lms_organization_master som ,(select 'pwt' t_type,pwt_amount as amount,net_amount ,comm_amt , receipt_id,game_nbr,game_name from (select receipt_id,sbp.agent_org_id as agent,sbp.pwt_amt as pwt_amount, sbp.net_amt as net_amount ,sbp.comm_amt as comm_amt,sbp.transaction_id as pwt_trans_id, sbp.game_id,game_nbr,game_name from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name from st_se_game_master  )sgm,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "') innermost,(select transaction_id from st_lms_bo_transaction_master)sbtm where sbp.transaction_id=innermost.transaction_id and  sbtm.transaction_id=innermost.transaction_id and sbp.game_id=sgm.game_id and agent_org_id='') pwt )result where result.agent = som.organization_id";
					System.out
							.println(" RECEIPT PWT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/DGPWTReceipt.jasper";
				}
				byteArr = generateReport(query, file, parameterMap, "Receipt", receipt_id
						+ "");
			}

			else if (receipt_type.equalsIgnoreCase("DR_NOTE")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query= "select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,amount,transaction_type,remarks,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,
				// sbdn.transaction_type,remarks, cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc,(select
				// amount,transaction_type,remarks,transaction_id from
				// st_lms_bo_debit_note) sbdn, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbsc.transaction_id=innermost.transaction_id
				// and sbdn.transaction_id =innermost.transaction_id )result1
				// where som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name, cheque_nbr, cheque_date,
				// drawee_bank, cheque_amt as amount,transaction_id,
				// transaction_type,remarks,transaction_date from (select 0.00
				// amount,id,transaction_type,'Cheque
				// Bounce'remarks,sbsc.transaction_id,agent_org_id,
				// issuing_party_name, cheque_nbr, cheque_date,drawee_bank,
				// cheque_amt,transaction_date from st_lms_bo_sale_chq
				// sbsc,(select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// innermost.transaction_id=sbsc.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)cheque union
				// select agent_org_id,id,'N.A.'issuing_party_name,'N.A.'
				// cheque_nbr,'N.A.' cheque_date,'N.A.' drawee_bank,
				// amount,transaction_id,transaction_type,remarks,transaction_date
				// from (select
				// amount,id,transaction_type,remarks,sbdn.transaction_id,agent_org_id,transaction_date
				// from st_lms_bo_debit_note sbdn,(select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"')innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// innermost.transaction_id=sbdn.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id )bounce) result1
				// where som.organization_id=result1.agent_org_id ";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,receipt_id as id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_type,remarks,transaction_id,transaction_date from st_lms_organization_master som ,(select agent_org_id,receipt_id,issuing_party_name, cheque_nbr, cheque_date, drawee_bank,cheque_amt as amount,transaction_id, transaction_type,remarks,transaction_date from (select 0.00 amount,receipt_id,transaction_type,'Cheque Bounce'remarks,sbsc.transaction_id,agent_org_id, issuing_party_name, cheque_nbr, cheque_date,drawee_bank, cheque_amt,transaction_date from st_lms_bo_sale_chq  sbsc,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "' )innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master)sbtm  where innermost.transaction_id=sbsc.transaction_id and sbtm.transaction_id=innermost.transaction_id )cheque union select agent_org_id,receipt_id as id,'N.A.'issuing_party_name,'N.A.' cheque_nbr,'N.A.' cheque_date,'N.A.' drawee_bank, amount,transaction_id,transaction_type,remarks,transaction_date from (select  amount,receipt_id,transaction_type,remarks,sbdn.transaction_id,agent_org_id,transaction_date from st_lms_bo_debit_note  sbdn,(select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
						+ receipt_id
						+ "' )innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm  where innermost.transaction_id=sbdn.transaction_id and  sbtm.transaction_id=innermost.transaction_id  )bounce) result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out.println("queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO" + query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/";
				byteArr = generateReport(query, file, parameterMap, "DebitNote",
						receipt_id + "");

			} else if (receipt_type.equalsIgnoreCase("DR_NOTE_CASH")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where sbdn.transaction_id
				// =innermost.transaction_id )result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where sbdn.transaction_id
				// =innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,id,user_name,amount,transaction_type,remarks,transaction_id,transaction_date from  st_lms_organization_master som , (select agent_org_id,id,amount,sbdn.transaction_type,slum.user_name,remarks,sbdn.transaction_id,transaction_date from (select  amount,transaction_type,remarks,transaction_id,agent_org_id from st_lms_bo_debit_note ) sbdn,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= "
						+receipt_id+
						") innermost,(select user_id,transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_id, user_name from st_lms_user_master)slum where sbdn.transaction_id =innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id)result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out
						.println("DR_NOTE_CASH queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Debit_Note_Cash.jasper";
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("CR_NOTE")) {
				if ("SALE_RET".equalsIgnoreCase(trnType)) {
					// query="select
					// agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name
					// ,sale,taxable_sale,vat_amt,comm_amt, ticket_price,
					// nbr_of_tickets_per_book
					// ,nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as
					// total_tickets, transaction_date from
					// st_lms_organization_master som , (select
					// agent_sale_comm_rate,id,agent_org_id,game_name,ticket_price,nbr_of_tickets_per_book,result.game_id,result.nbr_of_books
					// ,taxable_sale,vat_amt,(net_amt) as
					// sale,comm_amt,transaction_id,transaction_date from
					// st_se_game_master sgm,(select
					// sbat.agent_org_id,game_id,nbr_of_books,mrp_amt
					// ,sbat.transaction_id,id,
					// vat_amt,taxable_sale,net_amt,comm_amt,transaction_date
					// from st_se_bo_agent_transaction sbat, (select receipt_id
					// as id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where receipt_id= '"+receipt_id+"' ) innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbat.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) result
					// where sgm.game_id = result.game_id )result1 where
					// som.organization_id=result1.agent_org_id";
					query = "select agent_sale_comm_rate,name,addr_line1,addr_line2,city,agent_org_id,id,game_name ,sale,taxable_sale,vat_amt,comm_amt, ticket_price, nbr_of_tickets_per_book ,nbr_of_books,(nbr_of_tickets_per_book*nbr_of_books)as total_tickets, transaction_date from  st_lms_organization_master som , (select agent_sale_comm_rate,id,agent_org_id,game_name,ticket_price,nbr_of_tickets_per_book,result.game_id,result.nbr_of_books ,taxable_sale,result.vat_amt,(net_amt) as sale,comm_amt,transaction_id,transaction_date from st_se_game_master sgm,(select sbat.agent_org_id,game_id,nbr_of_books,mrp_amt ,sbat.transaction_id,id, vat_amt,taxable_sale,net_amt,comm_amt,transaction_date from st_se_bo_agent_transaction sbat, (select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= '"
							+ receipt_id
							+ "' ) innermost,(select transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm where sbat.transaction_id=innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id ) result where sgm.game_id = result.game_id  )result1 where som.organization_id=result1.agent_org_id order by transaction_date";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CrdtNtRdpVat.jasper";
					}
				}
				if ("LOOSE_SALE_RET".equalsIgnoreCase(trnType)) {
					
					query = "select id,game_name,organization_id,transaction_date,nbrOfTickets, net_amt sale, agent_sale_comm_rate,name,addr_line1,addr_line2,city,sale.transaction_id as transaction_id,vat_amt,taxable_sale,ticket_price,comm_amt,agtSaleCommRate from st_lms_organization_master som ,(select agent_id,id,game_name ,agent_sale_comm_rate as agent_sale_comm_rate,nbrOfTickets, ticket_price,taxable_sale,sbs.vat_amt ,net_amt,sbs.transaction_id, btm.transaction_date,comm_amt,agtSaleCommRate from st_se_bo_agent_loose_book_transaction sbs,st_se_game_master game, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+ receipt_id+ "' )innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.game_id=game.game_id)sale where som.organization_id = sale.agent_id";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "looseSaleReturnCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "looseSaleReturnCrdtNtRdpVat.jasper";
					}
				}
				if ("DG_REFUND_CANCEL".equalsIgnoreCase(trnType)
						|| "DG_REFUND_FAILED".equalsIgnoreCase(trnType)) {
					query = "select name,addr_line1,addr_line2,city,game_name ,sale,taxable_sale,vat as vat_amt,agent_comm as comm_amt, transaction_date from st_lms_organization_master som, st_dg_game_master sdgm,(select sbtm.transaction_date, sdbs.agent_org_id as agt_id, game_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, taxable_sale, net_amt, agent_comm from st_dg_bo_sale_refund sdbs,(select receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id='"
							+ receipt_id
							+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_bo_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.agent_org_id)result where result.agt_id = som.organization_id and result.game_id = sdgm.game_id order by transaction_date";
					System.out
							.println("CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "DGCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "DGCrdtNtRdpVat.jasper";
					}
				}
				if("CS_CANCEL_SERVER".equalsIgnoreCase(trnType)
						|| "CS_CANCEL_RET".equalsIgnoreCase(trnType)){
					query = "select name,addr_line1,addr_line2,city,product_code,description,sale,vat as vat_amt,comm_amt, transaction_date from st_lms_organization_master som, st_cs_product_master scpm,(select sbtm.transaction_date, sdbs.agent_org_id as agt_id, product_id, mrp_amt as sale, sdbs.transaction_id, vat_amt as vat, net_amt, (mrp_amt*sdbs.agent_comm/100) as comm_amt from st_cs_bo_refund sdbs,(select receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id='"
							+ receipt_id
							+ "')innermost,(select party_id, transaction_id, transaction_date from st_lms_bo_transaction_master)sbtm where sdbs.transaction_id = innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and sbtm.party_id = sdbs.agent_org_id)result where result.agt_id = som.organization_id and result.product_id = scpm.product_id order by transaction_date";
					System.out
							.println("CS CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CSCrdtNtComVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("yes")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CSCrdtNtComNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("no")) {
						fileName = "CSCrdtNtRdpNoVat.jasper";
					} else if (isCommApplicable.equalsIgnoreCase("no")
							&& isVatApplicable.equalsIgnoreCase("yes")) {
						fileName = "CSCrdtNtRdpVat.jasper";
					}
				
				}
				if("OLA_DEPOSIT_REFUND".equalsIgnoreCase(trnType)){
					query = "select id,wallet_name,transaction_date,voucher_date,deposit_amt as deposit_refund, net_amt ,(deposit_amt-net_amt) comm_amt,name,addr_line1,addr_line2,city,deposit.transaction_id as transaction_id from st_lms_organization_master som ,(select agent_id,id,wallet_name ,agent_comm as comm_amt, deposit_amt, net_amt,sbs.transaction_id, btm.transaction_date from st_ola_bo_deposit_refund sbs,st_ola_wallet_master wamaster, st_lms_bo_transaction_master btm,(select slbr.party_id as agent_id, slbr.receipt_id as id, transaction_id from st_lms_bo_receipts_trn_mapping slbrtm, st_lms_bo_receipts slbr where slbrtm.receipt_id = slbr.receipt_id and slbr.receipt_id = '"+receipt_id+"')innermost where innermost.transaction_id = sbs.transaction_id and innermost.transaction_id = btm.transaction_id and sbs.wallet_id=wamaster.wallet_id)deposit where som.organization_id = deposit.agent_id";
					System.out
							.println("OLA CR_NOTE queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
							+ fileName;
					
						fileName = "OLACrdtNtRdpNoVat.jasper";
				}
				/*
				 * else{ query="select
				 * name,addr_line1,addr_line2,city,id,game_name
				 * ,sale,taxable_sale,vat_amt,comm_amt from
				 * st_lms_organization_master som ,(select
				 * id,game_name,result.game_id,taxable_sale,vat_amt,(net_amt) as
				 * sale,comm_amt,transaction_id from st_se_game_master
				 * sgm,(select sdbs.agent_org_id,game_id,mrp_amt
				 * ,sdbs.transaction_id,id,
				 * vat_amt,taxable_sale,net_amt,comm_amt from st_dg_bo_sale
				 * sdbs,(select receipt_id as id,transaction_id from
				 * st_lms_bo_receipts_trn_mapping where receipt_id=
				 * '"+receipt_id+"') innermost,(select
				 * transaction_id,transaction_date from
				 * st_lms_bo_transaction_master )sbtm where
				 * sdbs.transaction_id=innermost.transaction_id and
				 * sbtm.transaction_id=innermost.transaction_id and
				 * agent_org_id='') result where sgm.game_id = result.game_id
				 * )result1 where som.organization_id=result1.agent_org_id";
				 * System.out.println("CR_NOTE
				 * queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"+query);file=root_path+
				 * "com/skilrock/lms/web/reportsMgmt/compiledReports/"+fileName;
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable.
				 * equalsIgnoreCase("yes")){ fileName="DGCrdtNtComVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("yes")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtComNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("no")){ fileName="DGCrdtNtRdpNoVat.jasper";
				 * }else
				 * if(isCommApplicable.equalsIgnoreCase("no")&&isVatApplicable
				 * .equalsIgnoreCase("yes")){ fileName="DGCrdtNtRdpVat.jasper";
				 * } }
				 */

				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/"
						+ fileName;
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);
			}

			else if (receipt_type.equalsIgnoreCase("CR_NOTE_CASH")) {
				System.out
						.println("================== inside note =================");
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// from st_lms_bo_sale_chq sbsc, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where
				// sbsc.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost where sbdn.transaction_id
				// =innermost.transaction_id )result1 where
				// som.organization_id=result1.agent_org_id";
				// query="select
				// name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,transaction_id,transaction_date
				// from st_lms_organization_master som ,(select
				// agent_org_id,id,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date
				// from (select
				// amount,transaction_type,remarks,transaction_id,agent_org_id
				// from st_lms_bo_debit_note) sbdn,(select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where sbdn.transaction_id
				// =innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id)result1 where
				// som.organization_id=result1.agent_org_id";
				query = "select name,addr_line1,addr_line2,city,agent_org_id,id,amount,transaction_type,remarks,user_name,transaction_id,transaction_date from  st_lms_organization_master som , (select agent_org_id,id,slum.user_name,amount,sbdn.transaction_type,remarks,sbdn.transaction_id,transaction_date from (select  amount,transaction_type,remarks,transaction_id,agent_org_id from st_lms_bo_credit_note ) sbdn,(select receipt_id as id,transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id= "
						+receipt_id+
						") innermost,(select user_id, transaction_id,transaction_date from st_lms_bo_transaction_master )sbtm, (select user_name, user_id from st_lms_user_master) slum where sbdn.transaction_id =innermost.transaction_id and sbtm.transaction_id=innermost.transaction_id and slum.user_id = sbtm.user_id)result1 where som.organization_id=result1.agent_org_id order by transaction_date";
				System.out
						.println("CR_NOTE_CASH queryyyyyyyyyyyyyyyyyyyBOOOOOOOOO"
								+ query);
				file = root_path
						+ "com/skilrock/lms/web/reportsMgmt/compiledReports/Credit_Note_Cash.jasper";
				byteArr = generateReport(query, file, parameterMap, null, null, null,
						null);

			} else if (receipt_type.equalsIgnoreCase("GOVT_RCPT")) {
				vatPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("VAT_PARTY_NAME");
				tdsPartyName = (String) ServletActionContext
						.getServletContext().getAttribute("TDS_PARTY_NAME");
				govComPartyName = (String) ServletActionContext
						.getServletContext().getAttribute(
								"GOVT_COMM_PARTY_NAME");
				parameterMap.put("vatPartyName", vatPartyName);
				parameterMap.put("tdsPartyName", tdsPartyName);
				parameterMap.put("govComPartyName", govComPartyName);
				System.out
						.println("================== inside GOVT_RCPT ================="
								+ trnType);
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount from st_lms_organization_master som ,(select
				// 'cash' t_type,cash_agent as agent,cash_amount as amount
				// ,cash_amount as net_amount ,0.00 comm_amount ,cash_trans_id
				// as trans_id, 'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id from ( select id as cash_id,sbct.agent_org_id as
				// cash_agent,sbct.amount as cash_amount ,sbct.transaction_id as
				// cash_trans_id from st_lms_bo_cash_transaction sbct, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbct.transaction_id=innermost.transaction_id) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr, 'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as id from (select id
				// as pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as pwt_trans_id from
				// st_se_bo_pwt sbp, (select id,transaction_id from
				// st_lms_bo_receipts_trn_mapping where id= '"+receipt_id+"')
				// innermost where sbp.transaction_id=innermost.transaction_id
				// group by sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount ,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id
				// from ( select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id from st_lms_bo_sale_chq sbsc, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost where
				// sbsc.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				// query="select
				// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
				// ,comm_amount,transaction_date from st_lms_organization_master
				// som ,(select 'cash' t_type,cash_agent as agent,cash_amount as
				// amount ,cash_amount as net_amount ,0.00 comm_amount
				// ,cash_trans_id as trans_id,'cash' issuing_party_name,'cash'
				// cheque_nbr,'cash' cheque_date, 'cash' drawee_bank,cash_id as
				// id,transaction_date from ( select id as
				// cash_id,sbct.agent_org_id as cash_agent,sbct.amount as
				// cash_amount , sbct.transaction_id as
				// cash_trans_id,transaction_date from
				// st_lms_bo_cash_transaction sbct, (select id,transaction_id
				// from st_lms_bo_receipts_trn_mapping where id=
				// '"+receipt_id+"') innermost, (select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbct.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id ) cash union
				// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
				// amount,net_amount ,comm_amount ,pwt_trans_id as
				// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
				// cheque_date, 'pwt' drawee_bank , pwt_id as
				// id,transaction_date from (select id as
				// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
				// pwt_amount,sum(sbp.net_amt)as net_amount ,sum(sbp.comm_amt)as
				// comm_amount,sbp.transaction_id as
				// pwt_trans_id,transaction_date from st_se_bo_pwt sbp, (select
				// id,transaction_id from st_lms_bo_receipts_trn_mapping where
				// id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbp.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id group by
				// sbp.transaction_id) pwt union select 'cheque'
				// t_type,cheque_agent as agent,cheque_amt as amount,cheque_amt
				// as net_amount,0.00 comm_amount,cheque_id as
				// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
				// from (select id ,agent_org_id as
				// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
				// as cheque_id,transaction_date from st_lms_bo_sale_chq sbsc,
				// (select id,transaction_id from st_lms_bo_receipts_trn_mapping
				// where id= '"+receipt_id+"') innermost,(select
				// transaction_id,transaction_date from
				// st_lms_bo_transaction_master)sbtm where
				// sbsc.transaction_id=innermost.transaction_id and
				// sbtm.transaction_id=innermost.transaction_id) cheque) result
				// where result.agent = som.organization_id";
				if (trnType.equalsIgnoreCase("VAT")) {
					// query=" select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date,temp_receipt_id
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date,temp_receipt_id
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date,temp_receipt_id from
					// st_lms_bo_sale_chq sbsc,st_lms_bo_cheque_temp_receipt
					// sbctr,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbctr.cheque_nbr=sbsc.cheque_nbr ) cheque ) result where
					// result.agent = som.organization_id";
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cheque'
					// t_type,cheque_agent as agent,cheque_amt as
					// amount,cheque_amt as net_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// from (select id ,sbsc.agent_org_id as
					// cheque_agent,sbsc.issuing_party_name,sbsc.cheque_nbr,sbsc.cheque_date,sbsc.drawee_bank,sbsc.cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc,(select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cheque )
					// result where result.agent = som.organization_id";
					query = "select generated_id,sbrtm.transaction_id,amount,start_date,end_date, transaction_date, sbr.voucher_date,'VAT Paid' as remarks from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm where sbr.receipt_id='"
							+ receipt_id
							+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by transaction_date";
					System.out
							.println(" RECEIPT VAT queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/VAT.jasper";
				} else if (trnType.equalsIgnoreCase("TDS")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,transaction_date
					// from st_lms_organization_master som ,(select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_trans_id as trans_id,cash_id as id,transaction_date
					// from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct,(select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id )
					// cash)result where result.agent = som.organization_id";
					// query="select
					// generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date,
					// sbtm.transaction_date, 'TDS Paid' as
					// remarks,sdpp.transaction_date as plr_date
					// ,sdpp.player_id,
					// first_name,last_name,photo_id_type,photo_id_nbr,sdpp.tax_amt,sdpp.pwt_amt
					// from st_lms_bo_receipts_trn_mapping
					// sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction
					// sbgt ,st_lms_bo_transaction_master
					// sbtm,st_se_direct_player_pwt sdpp,st_lms_player_master
					// spm where sbr.receipt_id='"+receipt_id+"' and
					// sbrtm.receipt_id=sbr.receipt_id and
					// sbgt.transaction_id=sbrtm.transaction_id and
					// sbtm.transaction_id=sbrtm.transaction_id and
					// sdpp.transaction_date >= start_date and
					// sdpp.transaction_date <=end_date and
					// sdpp.player_id=spm.player_id";
					statement = connection.createStatement();
					set = statement
							.executeQuery("select player_id  from   st_se_direct_player_pwt");
					System.out.println(set.absolute(1));
					if (set.absolute(1)) {
						query = "select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, sbr.voucher_date, '"
								+ textForTax
								+ " Paid' as remarks,sdpp.player_id, first_name,last_name,photo_id_type,photo_id_nbr,sdpp.tax_amt,sdpp.pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_se_direct_player_pwt sdpp,st_lms_player_master spm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sdpp.transaction_date >= start_date and sdpp.transaction_date <=end_date and sdpp.player_id=spm.player_id union select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, '"
								+ textForTax
								+ " Paid' as remarks, 'NA' player_id, 'NA'first_name,'NA' last_name,'NA' photo_id_type,'NA' photo_id_nbr,'NA' tax_amt,'NA' pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_lms_player_master spm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by sbtm.transaction_date";
					} else {
						query = "select distinct(generated_id),sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, sbtm.transaction_date, sbr.voucher_date, '"
								+ textForTax
								+ " Paid' as remarks, 'NA' player_id, 'NA'first_name,'NA' last_name,'NA' photo_id_type,'NA' photo_id_nbr,'NA' tax_amt,'NA' pwt_amt from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id order by sbtm.transaction_date";
					}
					System.out
							.println(" RECEIPT TDS queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/TDS.jasper";
				} else if (trnType.equalsIgnoreCase("GOVT_COMM")) {
					// query="select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr,game_name from
					// st_lms_organization_master som ,(select 'pwt'
					// t_type,pwt_agent as agent,pwt_amount as amount,net_amount
					// ,comm_amount ,pwt_trans_id as trans_id, pwt_id as
					// id,transaction_date,game_nbr,game_name from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr,game_name
					// from st_se_bo_pwt sbp,(select game_nbr,game_id,game_name
					// from st_se_game_master )sgm,(select id,transaction_id
					// from st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master )sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id )
					// pwt)result where result.agent = som.organization_id";
					// "select
					// name,addr_line1,addr_line2,city,t_type,agent,trans_id,id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,amount,net_amount
					// ,comm_amount,transaction_date,game_nbr from
					// st_lms_organization_master som , (select 'cash'
					// t_type,cash_agent as agent,cash_amount as amount
					// ,cash_amount as net_amount ,0.00 comm_amount
					// ,cash_trans_id as trans_id,'cash'
					// issuing_party_name,'cash' cheque_nbr,'cash' cheque_date,
					// 'cash' drawee_bank,cash_id as id,transaction_date,'cash'
					// game_nbr from ( select id as cash_id,sbct.agent_org_id as
					// cash_agent,sbct.amount as cash_amount ,
					// sbct.transaction_id as cash_trans_id,transaction_date
					// from st_lms_bo_cash_transaction sbct, (select
					// id,transaction_id from st_lms_bo_receipts_trn_mapping
					// where id= '"+receipt_id+"') innermost, (select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbct.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id ) cash union
					// select 'pwt' t_type,pwt_agent as agent,pwt_amount as
					// amount,net_amount ,comm_amount ,pwt_trans_id as
					// trans_id,'pwt' issuing_party_name,'pwt' cheque_nbr,'pwt'
					// cheque_date, 'pwt' drawee_bank , pwt_id as
					// id,transaction_date,game_nbr from (select id as
					// pwt_id,sbp.agent_org_id as pwt_agent,sum(sbp.pwt_amt) as
					// pwt_amount,sum(sbp.net_amt)as net_amount
					// ,sum(sbp.comm_amt)as comm_amount,sbp.transaction_id as
					// pwt_trans_id,transaction_date,sbp.game_id,game_nbr from
					// st_se_bo_pwt sbp,(select game_nbr,game_id from
					// st_se_game_master )sgm, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbp.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id and
					// sbp.game_id=sgm.game_id group by sbp.transaction_id) pwt
					// union select 'cheque' t_type,cheque_agent as
					// agent,cheque_amt as amount,cheque_amt as net_amount,0.00
					// comm_amount,cheque_id as
					// trans_id,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,id,transaction_date
					// , 'cheque' game_nbr from (select id ,agent_org_id as
					// cheque_agent,issuing_party_name,cheque_nbr,cheque_date,drawee_bank,cheque_amt,sbsc.transaction_id
					// as cheque_id,transaction_date from st_lms_bo_sale_chq
					// sbsc, (select id,transaction_id from
					// st_lms_bo_receipts_trn_mapping where id=
					// '"+receipt_id+"') innermost,(select
					// transaction_id,transaction_date from
					// st_lms_bo_transaction_master)sbtm where
					// sbsc.transaction_id=innermost.transaction_id and
					// sbtm.transaction_id=innermost.transaction_id) cheque)
					// result where result.agent = som.organization_id";
					String serviceCode = checkServiceCode(receipt_id);
					if (serviceCode.equalsIgnoreCase("se")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_se_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					} else if (serviceCode.equalsIgnoreCase("dg")) {
						query = "select generated_id,sbrtm.transaction_id,amount,sbgt.start_date,sbgt.end_date, transaction_date, sbr.voucher_date, sbtm.transaction_type, 'Good Cause' as remarks,sbgt.game_id,game_name,game_nbr from st_lms_bo_receipts_trn_mapping sbrtm,st_lms_bo_receipts sbr,st_lms_bo_govt_transaction sbgt ,st_lms_bo_transaction_master sbtm,st_dg_game_master sgm where sbr.receipt_id='"
								+ receipt_id
								+ "' and sbrtm.receipt_id=sbr.receipt_id and sbgt.transaction_id=sbrtm.transaction_id and sbtm.transaction_id=sbrtm.transaction_id and sgm.game_id=sbgt.game_id order by transaction_date";
					}
					System.out
							.println(" GOVT_COMM queryyyyyyyyyyyyyyyy BOOOOO "
									+ query);
					file = root_path
							+ "com/skilrock/lms/web/reportsMgmt/compiledReports/GovtComm.jasper";
				}
				byteArr = generateReport(query, file, parameterMap, null, null);

			}

			System.out.println("Query -- " + query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return byteArr;

	}

}