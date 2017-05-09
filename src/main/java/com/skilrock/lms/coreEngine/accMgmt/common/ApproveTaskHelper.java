package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.beans.VatDetailBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

/**
 * This class provides method to approve task for a given task id
 * 
 * @author Skilrock Technologies
 * 
 */
public class ApproveTaskHelper {
	/**
	 * This method set the status of task to APPROVED for a given Id
	 * 
	 * @param id
	 *            is task id
	 * @throws LMSException
	 */
	static Log logger = LogFactory.getLog(ApproveTaskHelper.class);

	public Map getVatDetailsDG(java.sql.Date startDate, java.sql.Date endDate,
			UserInfoBean infoBean) {
		System.out.println("getVatDetailsDG called");
		Map map = new TreeMap();
		System.out.println(startDate + " " + endDate + "  " + infoBean);
		Connection conn = DBConnect.getConnection();
		try {
			// calculate vat details of total purchase
			List<VatDetailBean> purchaseDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String purchaseVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr'," +"
			 * sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt)
			 * 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name,
			 * cc.transaction_type" +" from st_bo_receipt_gen_mapping aa,
			 * st_lms_bo_receipts_trn_mapping bb, st_se_bo_agent_transaction cc,
			 * st_lms_bo_receipts dd, st_lms_bo_transaction_master ee,
			 * st_se_game_master ff, st_lms_organization_master gg" +" where
			 * aa.id = bb.id and cc.transaction_id = bb.transaction_id and dd.id
			 * = bb.id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type =
			 * 'CR_NOTE') and ee.transaction_id = cc.transaction_id" +" and
			 * cc.game_id = ff.game_id and gg.organization_id = 1 and
			 * cc.agent_org_id = ? and (ee.transaction_date>=? and
			 * ee.transaction_date<?)" +" group by generated_id order by
			 * transaction_date";
			 */
			String purchaseVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt + cc.taxable_sale) 'tax_sales',  sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',  sum(cc.govt_comm) 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name, ee.transaction_type"
					+ " from st_lms_bo_receipts_trn_mapping bb,  st_dg_bo_sale cc,  st_lms_bo_receipts dd, st_lms_bo_transaction_master ee, st_dg_game_master ff, st_lms_organization_master gg"
					+ " where  cc.transaction_id = bb.transaction_id and dd.receipt_id = bb.receipt_id  and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE')  and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id and gg.organization_type = 'BO'  "
					+ " and cc.agent_org_id = ? and (ee.transaction_date>=? and ee.transaction_date<?) "
					+ " group by generated_id  order by transaction_date";
			PreparedStatement pstmt = conn
					.prepareStatement(purchaseVatDetailQuery);
			pstmt.setInt(1, infoBean.getUserOrgId());
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			ResultSet result = pstmt.executeQuery();
			System.out.println("purchase details query = " + pstmt);

			VatDetailBean purchaseBean = null;
			double sumTotalSale = 0.0;
			double sumTaxSalesVatInc = 0.0;
			double sumNonTaxablePpr = 0.0;
			double sumNonTaxableGoodCause = 0.0;
			double sumVatAmount = 0.0;
			double sumTaxableSale = 0.0;
			int mulFlag = 1;
			while (result.next()) {
				purchaseBean = new VatDetailBean();
				if ("DG_SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				purchaseBean.setDate(result.getDate("transaction_date"));
				purchaseBean.setInvoiceNo(result.getString("generated_id"));
				double totalSale = result.getDouble("net_amt");
				purchaseBean.setTotalSale(FormatNumber
						.formatNumberForJSP(totalSale * mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				purchaseBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				double nonTaxableGoodCause = result
						.getDouble("non_taxable_good_cause");
				purchaseBean
						.setNonTaxablePpr(FormatNumber
								.formatNumberForJSP((nonTaxablePpr + nonTaxableGoodCause)
										* mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr
						+ (nonTaxablePpr + nonTaxableGoodCause) * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * purchaseBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */

				double taxableSale = result.getDouble("taxable_sale");
				purchaseBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				purchaseBean.setVatAmount(FormatNumber
						.formatNumberForJSP(vatAmount * mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				purchaseBean.setName(result.getString("name"));
				purchaseBean.setTransactionType(result
						.getString("transaction_type"));

				purchaseDetailsList.add(purchaseBean);
			}
			map.put("purchaseDetailsList", purchaseDetailsList);

			// calculate total of all values

			VatDetailBean sumPurchaseBean = new VatDetailBean();
			sumPurchaseBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumPurchaseBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumPurchaseBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumPurchaseBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumPurchaseBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			double payableVatOfAgent = sumVatAmount;
			sumPurchaseBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));
			map.put("sumPurchaseBean", sumPurchaseBean);

			result.close();
			pstmt.close();

			// calculate vat details of total sale and saleReturn
			List<VatDetailBean> saleDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String saleVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr'," + "
			 * 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt',
			 * sum(cc.taxable_sale) 'taxable_sale', gg.name, cc.transaction_type
			 * " + "from st_lms_agent_receipts_gen_mapping aa,
			 * st_lms_agent_receipts_trn_mapping bb,
			 * st_se_agent_retailer_transaction cc, st_lms_agent_receipts dd,
			 * st_lms_agent_transaction_master ee, st_se_game_master ff,
			 * st_lms_organization_master gg " + "where aa.id = bb.id and
			 * cc.transaction_id = bb.transaction_id and dd.id = bb.id and
			 * (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE') and
			 * ee.transaction_id = cc.transaction_id and cc.game_id = ff.game_id
			 * and gg.organization_id = cc.retailer_org_id " + "and cc.agent_id
			 * = ? and (ee.transaction_date>=? and ee.transaction_date<?) " +
			 * "group by generated_id order by transaction_date ";
			 */
			String saleVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt+cc.taxable_sale) 'tax_sales', sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',"
					+ " 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name, ee.transaction_type "
					+ " from  st_lms_agent_receipts_trn_mapping bb, st_dg_agt_sale cc, st_lms_agent_receipts dd, st_lms_agent_transaction_master ee, st_dg_game_master ff, st_lms_organization_master gg "
					+ " where  cc.transaction_id = bb.transaction_id   and dd.receipt_id = bb.receipt_id  and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE') and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id     and gg.organization_id = cc.retailer_org_id  "
					+ " and ee.user_org_id = ?  and (ee.transaction_date>=? and ee.transaction_date<?) and (ee.transaction_type = 'DG_SALE' or ee.transaction_type = 'DG_SALE_RET' )"
					+ " group by generated_id order by transaction_date ";
			pstmt = conn.prepareStatement(saleVatDetailQuery);
			pstmt.setInt(1, infoBean.getUserOrgId());
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			result = pstmt.executeQuery();
			System.out.println("sale details query = " + pstmt);
			VatDetailBean saleBean = null;
			sumTotalSale = 0.0;
			sumTaxSalesVatInc = 0.0;
			sumNonTaxablePpr = 0.0;
			sumNonTaxableGoodCause = 0.0;
			sumVatAmount = 0.0;
			sumTaxableSale = 0.0;
			mulFlag = 1;
			while (result.next()) {
				saleBean = new VatDetailBean();
				if ("DG_SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				saleBean.setDate(result.getDate("transaction_date"));
				saleBean.setInvoiceNo(result.getString("generated_id"));

				double totalSale = result.getDouble("net_amt");
				saleBean.setTotalSale(FormatNumber.formatNumberForJSP(totalSale
						* mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				saleBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				saleBean.setNonTaxablePpr(FormatNumber
						.formatNumberForJSP(nonTaxablePpr * mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr + nonTaxablePpr * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * saleBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */
				double taxableSale = result.getDouble("taxable_sale");
				saleBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				saleBean.setVatAmount(FormatNumber.formatNumberForJSP(vatAmount
						* mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				saleBean.setName(result.getString("name"));
				saleBean.setTransactionType(result
						.getString("transaction_type"));

				saleDetailsList.add(saleBean);
			}
			map.put("saleDetailsList", saleDetailsList);

			// calculate total of all values

			VatDetailBean sumSaleBean = new VatDetailBean();
			sumSaleBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumSaleBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumSaleBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumSaleBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumSaleBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			payableVatOfAgent = -1 * payableVatOfAgent + sumVatAmount;
			sumSaleBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));

			map.put("sumSaleBean", sumSaleBean);
			map.put("payableVatAgent", FormatNumber
					.formatNumberForJSP(payableVatOfAgent));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	public Map getVatDetailsForBoDG(java.sql.Date startDate,
			java.sql.Date endDate, UserInfoBean infoBean) {
		System.out.println("getVatDetailsForBoDG called ");
		Map map = new TreeMap();
		System.out.println(startDate + " " + endDate + "  " + infoBean);
		Connection conn = DBConnect.getConnection();
		try {
			// calculate vat details of total sale and saleReturn
			List<VatDetailBean> saleDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String saleVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',
			 * sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt)
			 * 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name,
			 * cc.transaction_type " + "from st_bo_receipt_gen_mapping aa,
			 * st_lms_bo_receipts_trn_mapping bb, st_se_bo_agent_transaction cc,
			 * st_lms_bo_receipts dd, st_lms_bo_transaction_master ee,
			 * st_se_game_master ff, st_lms_organization_master gg " + "where
			 * aa.id = bb.id and cc.transaction_id = bb.transaction_id and dd.id
			 * = bb.id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type =
			 * 'CR_NOTE') and ee.transaction_id = cc.transaction_id and
			 * cc.game_id = ff.game_id and gg.organization_id = cc.agent_org_id
			 * and (ee.transaction_date>=? and ee.transaction_date<?)" + "group
			 * by generated_id order by transaction_date ";
			 */
			String idsForDateFilterQuery = "select transaction_id from st_lms_bo_transaction_master where transaction_date>=? and transaction_date<? and transaction_type in ('DG_SALE','DG_REFUND_CANCEL', 'DG_REFUND_FAILED')";
			String saleMinusSaleReturnQuery = "(select transaction_id, agent_org_id, game_id, mrp_amt, net_amt, taxable_sale, govt_comm, vat_amt from st_dg_bo_sale where transaction_id in ("
					+ idsForDateFilterQuery
					+ ")) union (select transaction_id, agent_org_id, game_id, mrp_amt, net_amt, taxable_sale, govt_comm, vat_amt from st_dg_bo_sale_refund where transaction_id in ("
					+ idsForDateFilterQuery + "))";
			String saleVatDetailQuery = "select ee.transaction_date, dd.generated_id, ee.transaction_id, sum(cc.net_amt) 'net_amt', "
					+ " sum(cc.vat_amt+cc.taxable_sale) 'tax_sales', sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr', "
					+ " sum(cc.govt_comm) 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt',  sum(cc.taxable_sale) 'taxable_sale', "
					+ "	gg.name, ee.transaction_type   from  st_lms_bo_receipts_trn_mapping bb, ("
					+ saleMinusSaleReturnQuery
					+ ") cc, st_lms_bo_receipts dd, "
					+ " st_lms_bo_transaction_master ee, 	st_dg_game_master ff, st_lms_organization_master gg  "
					+ "  where 	cc.transaction_id = bb.transaction_id and dd.receipt_id = bb.receipt_id and (dd.receipt_type = 'DG_INVOICE' "
					+ " OR dd.receipt_type = 'CR_NOTE')  and ee.transaction_id = cc.transaction_id and cc.game_id = ff.game_id "
					+ " and gg.organization_id = cc.agent_org_id"
					+ " group by generated_id order by transaction_date ";
			PreparedStatement pstmt = conn.prepareStatement(saleVatDetailQuery);
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			logger.debug("vat detail query " + pstmt);
			ResultSet result = pstmt.executeQuery();
			System.out.println("sale details query = " + pstmt);
			VatDetailBean saleBean = null;
			double sumTotalSale = 0.0;
			double sumTaxSalesVatInc = 0.0;
			double sumNonTaxablePpr = 0.0;
			double sumNonTaxableGoodCause = 0.0;
			double sumVatAmount = 0.0;
			double sumTaxableSale = 0.0;
			int mulFlag = 1;
			while (result.next()) {
				saleBean = new VatDetailBean();
				if ("DG_REFUND_FAILED".equalsIgnoreCase(result.getString(
						"transaction_type").trim())
						|| "DG_REFUND_CANCEL".equalsIgnoreCase(result
								.getString("transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				saleBean.setDate(result.getDate("transaction_date"));
				saleBean.setInvoiceNo(result.getString("generated_id"));

				double totalSale = result.getDouble("net_amt");
				saleBean.setTotalSale(FormatNumber.formatNumberForJSP(totalSale
						* mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				saleBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				double nonTaxableGoodCause = result
						.getDouble("non_taxable_good_cause");
				saleBean
						.setNonTaxablePpr(FormatNumber
								.formatNumberForJSP((nonTaxablePpr + nonTaxableGoodCause)
										* mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr
						+ (nonTaxablePpr + nonTaxableGoodCause) * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * saleBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */
				double taxableSale = result.getDouble("taxable_sale");
				saleBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				saleBean.setVatAmount(FormatNumber.formatNumberForJSP(vatAmount
						* mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				saleBean.setName(result.getString("name"));
				saleBean.setTransactionType(result
						.getString("transaction_type"));

				saleDetailsList.add(saleBean);
			}
			map.put("saleDetailsList", saleDetailsList);

			// calculate total of all values

			VatDetailBean sumSaleBean = new VatDetailBean();
			sumSaleBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumSaleBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumSaleBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumSaleBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumSaleBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			sumSaleBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));
			map.put("serviceName", "DG");
			map.put("sumSaleBean", sumSaleBean);
			map.put("payableVatAgent", FormatNumber
					.formatNumberForJSP(sumVatAmount));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;

	}

	public Map getVatDetailsForBoSE(java.sql.Date startDate,
			java.sql.Date endDate, UserInfoBean infoBean) {
		System.out.println("getVatDetailsForBoSE called");
		Map map = new TreeMap();
		System.out.println(startDate + " " + endDate + "  " + infoBean);
		Connection conn = DBConnect.getConnection();
		try {
			// calculate vat details of total sale and saleReturn
			List<VatDetailBean> saleDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String saleVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',
			 * sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt)
			 * 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name,
			 * cc.transaction_type " + "from st_bo_receipt_gen_mapping aa,
			 * st_lms_bo_receipts_trn_mapping bb, st_se_bo_agent_transaction cc,
			 * st_lms_bo_receipts dd, st_lms_bo_transaction_master ee,
			 * st_se_game_master ff, st_lms_organization_master gg " + "where
			 * aa.id = bb.id and cc.transaction_id = bb.transaction_id and dd.id
			 * = bb.id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type =
			 * 'CR_NOTE') and ee.transaction_id = cc.transaction_id and
			 * cc.game_id = ff.game_id and gg.organization_id = cc.agent_org_id
			 * and (ee.transaction_date>=? and ee.transaction_date<?)" + "group
			 * by generated_id order by transaction_date ";
			 */
			String saleVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt+cc.taxable_sale) 'tax_sales', sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr', sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt',  sum(cc.taxable_sale) 'taxable_sale', 	gg.name, cc.transaction_type  "
					+ " from  st_lms_bo_receipts_trn_mapping bb, st_se_bo_agent_transaction cc, st_lms_bo_receipts dd, st_lms_bo_transaction_master ee, 	st_se_game_master ff, st_lms_organization_master gg  "
					+ "  where 	cc.transaction_id = bb.transaction_id and dd.receipt_id = bb.receipt_id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE')  and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id  and gg.organization_id = cc.agent_org_id "
					+ " and (ee.transaction_date>=? and ee.transaction_date<?)"
					+ "group by generated_id 	order by transaction_date ";
			PreparedStatement pstmt = conn.prepareStatement(saleVatDetailQuery);
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			ResultSet result = pstmt.executeQuery();
			System.out.println("sale details query = " + pstmt);
			VatDetailBean saleBean = null;
			double sumTotalSale = 0.0;
			double sumTaxSalesVatInc = 0.0;
			double sumNonTaxablePpr = 0.0;
			double sumNonTaxableGoodCause = 0.0;
			double sumVatAmount = 0.0;
			double sumTaxableSale = 0.0;
			int mulFlag = 1;
			while (result.next()) {
				saleBean = new VatDetailBean();

				if ("SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				saleBean.setDate(result.getDate("transaction_date"));
				saleBean.setInvoiceNo(result.getString("generated_id"));

				double totalSale = result.getDouble("net_amt");
				saleBean.setTotalSale(FormatNumber.formatNumberForJSP(totalSale
						* mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				saleBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				double nonTaxableGoodCause = result
						.getDouble("non_taxable_good_cause");
				saleBean
						.setNonTaxablePpr(FormatNumber
								.formatNumberForJSP((nonTaxablePpr + nonTaxableGoodCause)
										* mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr
						+ (nonTaxablePpr + nonTaxableGoodCause) * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * saleBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */
				double taxableSale = result.getDouble("taxable_sale");
				saleBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				saleBean.setVatAmount(FormatNumber.formatNumberForJSP(vatAmount
						* mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				saleBean.setName(result.getString("name"));
				saleBean.setTransactionType(result
						.getString("transaction_type"));

				saleDetailsList.add(saleBean);
			}
			map.put("saleDetailsList", saleDetailsList);

			// calculate total of all values

			VatDetailBean sumSaleBean = new VatDetailBean();
			sumSaleBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumSaleBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumSaleBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumSaleBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumSaleBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			sumSaleBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));

			map.put("sumSaleBean", sumSaleBean);
			map.put("payableVatAgent", FormatNumber
					.formatNumberForJSP(sumVatAmount));
			map.put("serviceName", "SE");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;

	}
	
	public Map getVatDetailsForBoIW(java.sql.Date startDate,
			java.sql.Date endDate, UserInfoBean infoBean) {
		System.out.println("getVatDetailsForBoIW called");
		Map map = new TreeMap();
		System.out.println(startDate + " " + endDate + "  " + infoBean);
		Connection conn = DBConnect.getConnection();
		try {
			// calculate vat details of total sale and saleReturn
			List<VatDetailBean> saleDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String saleVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',
			 * sum(cc.govt_comm) 'non_taxable_good_cause', sum(cc.vat_amt)
			 * 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name,
			 * cc.transaction_type " + "from st_bo_receipt_gen_mapping aa,
			 * st_lms_bo_receipts_trn_mapping bb, st_iw_bo_sale cc,
			 * st_lms_bo_receipts dd, st_lms_bo_transaction_master ee,
			 * st_iw_game_master ff, st_lms_organization_master gg " + "where
			 * aa.id = bb.id and cc.transaction_id = bb.transaction_id and dd.id
			 * = bb.id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type =
			 * 'CR_NOTE') and ee.transaction_id = cc.transaction_id and
			 * cc.game_id = ff.game_id and gg.organization_id = cc.agent_org_id
			 * and (ee.transaction_date>=? and ee.transaction_date<?)" + "group
			 * by generated_id order by transaction_date ";
			 */
			String saleVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt+cc.taxable_sale) 'tax_sales', sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr', sum(cc.govt_comm) 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt',  sum(cc.taxable_sale) 'taxable_sale', 	gg.name  "
					+ " from  st_lms_bo_receipts_trn_mapping bb, st_iw_bo_sale cc, st_lms_bo_receipts dd, st_lms_bo_transaction_master ee, 	st_iw_game_master ff, st_lms_organization_master gg  "
					+ "  where 	cc.transaction_id = bb.transaction_id and dd.receipt_id = bb.receipt_id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE')  and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id  and gg.organization_id = cc.agent_org_id "
					+ " and (ee.transaction_date>=? and ee.transaction_date<?)"
					+ "group by generated_id 	order by transaction_date ";
			PreparedStatement pstmt = conn.prepareStatement(saleVatDetailQuery);
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			ResultSet result = pstmt.executeQuery();
			System.out.println("sale details query = " + pstmt);
			VatDetailBean saleBean = null;
			double sumTotalSale = 0.0;
			double sumTaxSalesVatInc = 0.0;
			double sumNonTaxablePpr = 0.0;
			double sumNonTaxableGoodCause = 0.0;
			double sumVatAmount = 0.0;
			double sumTaxableSale = 0.0;
			int mulFlag = 1;
			while (result.next()) {
				saleBean = new VatDetailBean();

				/*if ("SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}*/

				saleBean.setDate(result.getDate("transaction_date"));
				saleBean.setInvoiceNo(result.getString("generated_id"));

				double totalSale = result.getDouble("net_amt");
				saleBean.setTotalSale(FormatNumber.formatNumberForJSP(totalSale
						* mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				saleBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				double nonTaxableGoodCause = result
						.getDouble("non_taxable_good_cause");
				saleBean
						.setNonTaxablePpr(FormatNumber
								.formatNumberForJSP((nonTaxablePpr + nonTaxableGoodCause)
										* mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr
						+ (nonTaxablePpr + nonTaxableGoodCause) * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * saleBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */
				double taxableSale = result.getDouble("taxable_sale");
				saleBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				saleBean.setVatAmount(FormatNumber.formatNumberForJSP(vatAmount
						* mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				saleBean.setName(result.getString("name"));
				saleBean.setTransactionType(result
						.getString("transaction_type"));

				saleDetailsList.add(saleBean);
			}
			map.put("saleDetailsList", saleDetailsList);

			// calculate total of all values

			VatDetailBean sumSaleBean = new VatDetailBean();
			sumSaleBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumSaleBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumSaleBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumSaleBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumSaleBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			sumSaleBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));

			map.put("sumSaleBean", sumSaleBean);
			map.put("payableVatAgent", FormatNumber
					.formatNumberForJSP(sumVatAmount));
			map.put("serviceName", "IW");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;

	}

	public Map getVatDetailsSE(java.sql.Date startDate, java.sql.Date endDate,
			UserInfoBean infoBean) {
		System.out.println("getVatDetailsSE called");
		Map map = new TreeMap();
		System.out.println(startDate + " " + endDate + "  " + infoBean);
		Connection conn = DBConnect.getConnection();
		try {
			// calculate vat details of total purchase
			List<VatDetailBean> purchaseDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String purchaseVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr'," +"
			 * sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt)
			 * 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name,
			 * cc.transaction_type" +" from st_bo_receipt_gen_mapping aa,
			 * st_lms_bo_receipts_trn_mapping bb, st_se_bo_agent_transaction cc,
			 * st_lms_bo_receipts dd, st_lms_bo_transaction_master ee,
			 * st_se_game_master ff, st_lms_organization_master gg" +" where
			 * aa.id = bb.id and cc.transaction_id = bb.transaction_id and dd.id
			 * = bb.id and (dd.receipt_type = 'INVOICE' OR dd.receipt_type =
			 * 'CR_NOTE') and ee.transaction_id = cc.transaction_id" +" and
			 * cc.game_id = ff.game_id and gg.organization_id = 1 and
			 * cc.agent_org_id = ? and (ee.transaction_date>=? and
			 * ee.transaction_date<?)" +" group by generated_id order by
			 * transaction_date";
			 */
			String purchaseVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt + cc.taxable_sale) 'tax_sales',  sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',  sum(cc.gov_comm_amt) 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name, cc.transaction_type"
					+ " from st_lms_bo_receipts_trn_mapping bb,  st_se_bo_agent_transaction cc,  st_lms_bo_receipts dd, st_lms_bo_transaction_master ee, st_se_game_master ff, st_lms_organization_master gg"
					+ " where  cc.transaction_id = bb.transaction_id and dd.receipt_id = bb.receipt_id  and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE')  and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id and gg.organization_type = 'BO'  "
					+ " and cc.agent_org_id = ? and (ee.transaction_date>=? and ee.transaction_date<?) "
					+ " group by generated_id  order by transaction_date";
			PreparedStatement pstmt = conn
					.prepareStatement(purchaseVatDetailQuery);
			pstmt.setInt(1, infoBean.getUserOrgId());
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			ResultSet result = pstmt.executeQuery();
			System.out.println("purchase details query = " + pstmt);

			VatDetailBean purchaseBean = null;
			double sumTotalSale = 0.0;
			double sumTaxSalesVatInc = 0.0;
			double sumNonTaxablePpr = 0.0;
			double sumNonTaxableGoodCause = 0.0;
			double sumVatAmount = 0.0;
			double sumTaxableSale = 0.0;
			int mulFlag = 1;
			while (result.next()) {
				purchaseBean = new VatDetailBean();
				if ("SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				purchaseBean.setDate(result.getDate("transaction_date"));
				purchaseBean.setInvoiceNo(result.getString("generated_id"));
				double totalSale = result.getDouble("net_amt");
				purchaseBean.setTotalSale(FormatNumber
						.formatNumberForJSP(totalSale * mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				purchaseBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				double nonTaxableGoodCause = result
						.getDouble("non_taxable_good_cause");
				purchaseBean
						.setNonTaxablePpr(FormatNumber
								.formatNumberForJSP((nonTaxablePpr + nonTaxableGoodCause)
										* mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr
						+ (nonTaxablePpr + nonTaxableGoodCause) * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * purchaseBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */

				double taxableSale = result.getDouble("taxable_sale");
				purchaseBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				purchaseBean.setVatAmount(FormatNumber
						.formatNumberForJSP(vatAmount * mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				purchaseBean.setName(result.getString("name"));
				purchaseBean.setTransactionType(result
						.getString("transaction_type"));

				purchaseDetailsList.add(purchaseBean);
			}
			map.put("purchaseDetailsList", purchaseDetailsList);

			// calculate total of all values

			VatDetailBean sumPurchaseBean = new VatDetailBean();
			sumPurchaseBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumPurchaseBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumPurchaseBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumPurchaseBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumPurchaseBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			double payableVatOfAgent = sumVatAmount;
			sumPurchaseBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));
			map.put("sumPurchaseBean", sumPurchaseBean);

			result.close();
			pstmt.close();

			// calculate vat details of total sale and saleReturn
			List<VatDetailBean> saleDetailsList = new ArrayList<VatDetailBean>();
			/*
			 * String saleVatDetailQuery = "select ee.transaction_date,
			 * aa.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt',
			 * sum(cc.vat_amt+cc.taxable_sale) 'tax_sales',
			 * sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr'," + "
			 * 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt',
			 * sum(cc.taxable_sale) 'taxable_sale', gg.name, cc.transaction_type
			 * " + "from st_lms_agent_receipts_gen_mapping aa,
			 * st_lms_agent_receipts_trn_mapping bb,
			 * st_se_agent_retailer_transaction cc, st_lms_agent_receipts dd,
			 * st_lms_agent_transaction_master ee, st_se_game_master ff,
			 * st_lms_organization_master gg " + "where aa.id = bb.id and
			 * cc.transaction_id = bb.transaction_id and dd.id = bb.id and
			 * (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE') and
			 * ee.transaction_id = cc.transaction_id and cc.game_id = ff.game_id
			 * and gg.organization_id = cc.retailer_org_id " + "and cc.agent_id
			 * = ? and (ee.transaction_date>=? and ee.transaction_date<?) " +
			 * "group by generated_id order by transaction_date ";
			 */
			String saleVatDetailQuery = "select ee.transaction_date, dd.generated_id, cc.transaction_id, sum(cc.net_amt) 'net_amt', sum(cc.vat_amt+cc.taxable_sale) 'tax_sales', sum(ff.prize_payout_ratio*cc.mrp_amt/100) 'non_taxable_ppr',"
					+ " 'non_taxable_good_cause', sum(cc.vat_amt) 'vat_amt', sum(cc.taxable_sale) 'taxable_sale', gg.name, cc.transaction_type "
					+ " from  st_lms_agent_receipts_trn_mapping bb, st_se_agent_retailer_transaction cc, st_lms_agent_receipts dd, st_lms_agent_transaction_master ee, st_se_game_master ff, st_lms_organization_master gg "
					+ " where  cc.transaction_id = bb.transaction_id   and dd.receipt_id = bb.receipt_id  and (dd.receipt_type = 'INVOICE' OR dd.receipt_type = 'CR_NOTE') and ee.transaction_id = cc.transaction_id  and cc.game_id = ff.game_id     and gg.organization_id = cc.retailer_org_id  "
					+ " and ee.user_org_id = ?  and (ee.transaction_date>=? and ee.transaction_date<?)"
					+ " group by generated_id order by transaction_date ";
			pstmt = conn.prepareStatement(saleVatDetailQuery);
			pstmt.setInt(1, infoBean.getUserOrgId());
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			result = pstmt.executeQuery();
			System.out.println("sale details query = " + pstmt);
			VatDetailBean saleBean = null;
			sumTotalSale = 0.0;
			sumTaxSalesVatInc = 0.0;
			sumNonTaxablePpr = 0.0;
			sumNonTaxableGoodCause = 0.0;
			sumVatAmount = 0.0;
			sumTaxableSale = 0.0;
			mulFlag = 1;
			while (result.next()) {
				saleBean = new VatDetailBean();
				if ("SALE_RET".equalsIgnoreCase(result.getString(
						"transaction_type").trim())) {
					mulFlag = -1;
				} else {
					mulFlag = 1;
				}

				saleBean.setDate(result.getDate("transaction_date"));
				saleBean.setInvoiceNo(result.getString("generated_id"));

				double totalSale = result.getDouble("net_amt");
				saleBean.setTotalSale(FormatNumber.formatNumberForJSP(totalSale
						* mulFlag));
				sumTotalSale = sumTotalSale + totalSale * mulFlag;

				double taxSalesVatInc = result.getDouble("tax_sales");
				saleBean.setTaxSalesVatInc(FormatNumber
						.formatNumberForJSP(taxSalesVatInc * mulFlag));
				sumTaxSalesVatInc = sumTaxSalesVatInc + taxSalesVatInc
						* mulFlag;

				double nonTaxablePpr = result.getDouble("non_taxable_ppr");
				saleBean.setNonTaxablePpr(FormatNumber
						.formatNumberForJSP(nonTaxablePpr * mulFlag));
				sumNonTaxablePpr = sumNonTaxablePpr + nonTaxablePpr * mulFlag;

				/*
				 * double nonTaxableGoodCause =
				 * result.getDouble("non_taxable_good_cause");
				 * saleBean.setNonTaxableGoodCause
				 * (FormatNumber.formatNumberForJSP(nonTaxableGoodCause));
				 * sumNonTaxableGoodCause = sumNonTaxableGoodCause +
				 * nonTaxableGoodCause*mulFlag;
				 */
				double taxableSale = result.getDouble("taxable_sale");
				saleBean.setTaxableSale(FormatNumber
						.formatNumberForJSP(taxableSale * mulFlag));
				sumTaxableSale = sumTaxableSale + taxableSale * mulFlag;

				double vatAmount = result.getDouble("vat_amt");
				saleBean.setVatAmount(FormatNumber.formatNumberForJSP(vatAmount
						* mulFlag));
				sumVatAmount = sumVatAmount + vatAmount * mulFlag;

				saleBean.setName(result.getString("name"));
				saleBean.setTransactionType(result
						.getString("transaction_type"));

				saleDetailsList.add(saleBean);
			}
			map.put("saleDetailsList", saleDetailsList);

			// calculate total of all values

			VatDetailBean sumSaleBean = new VatDetailBean();
			sumSaleBean.setTotalSale(FormatNumber
					.formatNumberForJSP(sumTotalSale));
			sumSaleBean.setTaxSalesVatInc(FormatNumber
					.formatNumberForJSP(sumTaxSalesVatInc));
			sumSaleBean.setNonTaxablePpr(FormatNumber
					.formatNumberForJSP(sumNonTaxablePpr));
			sumSaleBean.setNonTaxableGoodCause(FormatNumber
					.formatNumberForJSP(sumNonTaxableGoodCause));
			sumSaleBean.setTaxableSale(FormatNumber
					.formatNumberForJSP(sumTaxableSale));
			payableVatOfAgent = -1 * payableVatOfAgent + sumVatAmount;
			sumSaleBean.setVatAmount(FormatNumber
					.formatNumberForJSP(sumVatAmount));

			map.put("sumSaleBean", sumSaleBean);
			map.put("payableVatAgent", FormatNumber
					.formatNumberForJSP(payableVatOfAgent));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	public void tdsTask(int id, String userType) throws LMSException {

		int taskId = id;

		Connection con = null;
		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String updateTask = null;

			if (userType.equals("BO")) {
				updateTask = QueryManager.updateST3Task() + " where task_id='"
						+ taskId + "' ";
			} else if (userType.equals("AGENT")) {
				updateTask = QueryManager.updateST3TaskAgt()
						+ " where task_id='" + taskId + "' ";
			}

			PreparedStatement stmt1 = con.prepareStatement(updateTask);
			stmt1.setDate(1, new java.sql.Date(new Date().getTime()));
			stmt1.executeUpdate();
			System.out.println("1234567777777");
			System.out.println("status approved");
			// stmt1.executeUpdate("update st_lms_bo_tasks set status='APPROVED'
			// where task_id='"+taskId+"'");

			con.commit();

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
			}
		}

	}

}
