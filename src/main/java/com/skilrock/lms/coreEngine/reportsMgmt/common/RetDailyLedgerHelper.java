package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;
import com.skilrock.lms.coreEngine.commercialService.common.CSUtil;
import com.skilrock.lms.coreEngine.commercialService.productMgmt.CSProductRegistrationHelper;
import com.skilrock.lms.web.accMgmt.common.RetailerOpeningBalanceHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.ola.reportsMgmt.controllerImpl.DepositWthdrwReportControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaRetailerReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaReportBean;

public class RetDailyLedgerHelper {
	private static Log logger = LogFactory.getLog(RetDailyLedgerHelper.class);

	public Map<Integer, String> getRetailerList(int agtOrgId) {

		Connection connection = DBConnect.getConnection();

		Map<Integer, String> selectMap = new TreeMap<Integer, String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection
					.prepareStatement("select organization_id, name from st_lms_organization_master where organization_type='RETAILER' and parent_id = ? order by name");
			pstmt.setInt(1, agtOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				selectMap.put(rs.getInt("organization_id"), rs
						.getString("name"));
			}
		} catch (SQLException e) {
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
		return selectMap;
	}

	public DailyLedgerBean getRetLegderDetail(DateBeans dBean,
			boolean isScratch, boolean isDraw, boolean isCS, int orgId) {
		DailyLedgerBean bean = new DailyLedgerBean();
		Connection connection = null;

		try {

			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null;
			// List<Integer> gameNumList = Util.getGameNumberList();
			List<Integer> gameNumList = Util.getLMSGameNumberList();
			StringBuilder drawSaleQueryBuilder = new StringBuilder();
			StringBuilder drawSaleRefQueryBuilder = new StringBuilder();
			StringBuilder drawPwtQueryBuilder = new StringBuilder();
			String drawSaleQuery = null;
			String drawSaleRefQuery = null;
			String drawPwtQuery = null;
			String scratchPurchaseQuery = null;
			String scratchSaleRetQuery = null;
			String scratchPwtQuery = null;
			String scratchSaleQuery = null;
			String paymentQuery = null;

			String sleSaleQuery = null;
			String sleSaleRefQuery = null;
			String slePwtQuery = null;
			
			String iwSaleQuery = null;
			String iwSaleRefQuery = null;
			String iwPwtQuery = null;
			
			String vsSaleQuery = null;
			String vsSaleRefQuery = null;
			String vsPwtQuery = null;

			double netSaleCS = 0.0;
			double mrpSaleCS = 0.0;
			double olaDeposit = 0.0;
			double olaWithdrawal = 0.0;
			double netDeposit = 0.0;
			double netWdrwl = 0.0;

			double netSleSale = 0.0;
			double mrpSleSale = 0.0;
			double netSlePWT = 0.0;
			double mrpSlePWT = 0.0;
			
			double netIwSale = 0.0;
			double mrpIwSale = 0.0;
			double netIwPWT = 0.0;
			double mrpIwPWT = 0.0;
			
			double netVsSale = 0.0;
			double mrpVsSale = 0.0;
			double netVsPWT = 0.0;
			double mrpVsPWT = 0.0;

			for (int i = 0; i < gameNumList.size(); i++) {
				drawSaleQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
				drawSaleRefQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_refund_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
				drawPwtQueryBuilder
						.append("select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm-govt_claim_comm)as  net_amt from st_dg_ret_pwt_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
			}
			drawSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
					+ drawSaleQueryBuilder.toString().substring(0,
							drawSaleQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
			drawSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from ("
					+ drawSaleRefQueryBuilder.toString().substring(0,
							drawSaleRefQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
			drawPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from ("
					+ drawPwtQueryBuilder.toString().substring(0,
							drawPwtQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
			scratchPurchaseQuery = "select ifnull(sum(mrp_amt),0)as mrpPur, ifnull(sum(net_amt),0)as netPur from st_se_agent_retailer_transaction sart, st_lms_agent_transaction_master atm where sart.transaction_type='SALE' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
			scratchSaleRetQuery = "select ifnull(sum(mrp_amt),0)as mrpSaleRet, ifnull(sum(net_amt),0)as netSaleRet from st_se_agent_retailer_transaction sart, st_lms_retailer_transaction_master atm where sart.transaction_type='SALE_RET' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
			scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as  net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and srp.transaction_id = atm.transaction_id";
			scratchSaleQuery = "select ifnull(sum(cnt.ticket_count*unit.ticket_price),0)as soldTicketPrice from (select game_id, ifnull(sum(sold_tickets),0) as ticket_count from st_se_game_ticket_inv_history where current_owner='RETAILER' and current_owner_id = ? and date(date)=? group by game_id)cnt, (select game_id, ticket_price from st_se_game_master)unit where cnt.game_id = unit.game_id";
			paymentQuery = "select cash.cashAmt, chq.chqAmt, chqboun.chqBounce, dbnote.dbnAmt,crnote.crnAmt,bankDeposit.bankDepoAmt from (select ifnull(sum(slact.amount),0)as cashAmt from st_lms_agent_cash_transaction slact, st_lms_agent_transaction_master atm where slact.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slact.transaction_id = atm.transaction_id)as cash, (select ifnull(sum(cheque_amt),0)as chqAmt from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and (slasc.transaction_type='CHEQUE' or slasc.transaction_type='CLOSED') and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chq, (select ifnull(sum(cheque_amt),0)as chqBounce from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and slasc.transaction_type='CHQ_BOUNCE' and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chqboun, (select ifnull(sum(amount),0)as dbnAmt from st_lms_agent_debit_note sladn, st_lms_agent_transaction_master atm where sladn.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sladn.transaction_id = atm.transaction_id) as dbnote ,"
					+ "(select ifnull(sum(amount),0)as crnAmt from st_lms_agent_credit_note slacn, st_lms_agent_transaction_master atm where slacn.retailer_org_id =  ? and (atm.transaction_date)>= ? and (atm.transaction_date)<= ? and slacn.transaction_id = atm.transaction_id) as crnote,(select ifnull(sum(slabdt.amount),0)as bankDepoAmt from st_lms_agent_bank_deposit_transaction slabdt, st_lms_agent_transaction_master atm where slabdt.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slabdt.transaction_id = atm.transaction_id)as bankDeposit";

			if (isDraw) {
				pstmt = connection.prepareStatement(drawSaleQuery);
				int i = 0;
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("drawSaleQuery:- " + pstmt);
				rs1 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(drawSaleRefQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("drawSaleRefQuery:- " + pstmt);
				rs2 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(drawPwtQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("drawPwtQuery:- " + pstmt);
				rs3 = pstmt.executeQuery();
			}
			if (isScratch) {
				pstmt = connection.prepareStatement(scratchPurchaseQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("scratchPurchaseQuery:- " + pstmt);
				rs4 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchSaleRetQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("scratchSaleRetQuery:- " + pstmt);
				rs5 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("scratchPwtQuery:- " + pstmt);
				rs6 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());

				logger.debug("scratchSaleQuery:- " + pstmt);
				rs7 = pstmt.executeQuery();
			}

			if (isCS) {
				List<Integer> csGameNumList = CSUtil.fetchCSGameNumList();
				StringBuilder csSaleQueryBuilder = new StringBuilder("");
				String csSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from (";

				StringBuilder csRefundQueryBuilder = new StringBuilder("");
				String csRefundQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from (";

				for (int j = 0; j < csGameNumList.size(); j++) {
					csSaleQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_cs_sale_"
									+ csGameNumList.get(j)
									+ " where retailer_org_id = "
									+ orgId
									+ " union ");

					csRefundQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_cs_refund_"
									+ csGameNumList.get(j)
									+ " where retailer_org_id = "
									+ orgId
									+ " union ");
				}

				csSaleQuery = csSaleQuery
						+ csSaleQueryBuilder.toString().substring(
								0,
								csSaleQueryBuilder.toString().lastIndexOf(
										" union "));

				csRefundQuery = csRefundQuery
						+ csRefundQueryBuilder.toString().substring(
								0,
								csRefundQueryBuilder.toString().lastIndexOf(
										" union "));

				csSaleQuery = csSaleQuery
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				csRefundQuery = csRefundQuery
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				PreparedStatement pstmtCS = connection
						.prepareStatement(csSaleQuery);
				pstmtCS.setTimestamp(1, dBean.getStartTime());
				pstmtCS.setTimestamp(2, dBean.getEndTime());
				System.out.println("***getRetLegderDetail CS Sale Query :"
						+ pstmtCS);
				ResultSet rsCSSale = pstmtCS.executeQuery();

				pstmtCS = connection.prepareStatement(csRefundQuery);
				pstmtCS.setTimestamp(1, dBean.getStartTime());
				pstmtCS.setTimestamp(2, dBean.getEndTime());
				System.out.println("***getRetLegderDetail CS Refund Query :"
						+ pstmtCS);
				ResultSet rsCSRefund = pstmtCS.executeQuery();

				if (rsCSSale != null && rsCSRefund != null) {
					rsCSSale.next();
					rsCSRefund.next();

					netSaleCS = rsCSSale.getDouble("netSale")
							- rsCSRefund.getDouble("netRef");
					mrpSaleCS = rsCSSale.getDouble("mrpSale")
							- rsCSRefund.getDouble("mrpRef");
				}

			}
			if (ReportUtility.isOLA) {
				OlaOrgReportRequestBean reqBean = new OlaOrgReportRequestBean();
				reqBean.setFromDate(dBean.getStartTime().toString());
				reqBean.setToDate(dBean.getEndTime().toString());
				reqBean.setOrgId(orgId);
				OlaOrgReportResponseBean respBean = OlaRetailerReportControllerImpl
						.fetchDepositWithdrawlSinglaRetailer(reqBean,
								connection);
				olaDeposit = respBean.getMrpDepositAmt();
				olaWithdrawal = respBean.getMrpWithdrawalAmt();
				netDeposit = respBean.getNetDepositAmt();
				netWdrwl = respBean.getNetWithdrawalAmt();
				bean.setOlaDeposit(Double.toString(netDeposit));
				bean.setOlaWithdrawal(Double.toString(netWdrwl));
			}

			if (ReportUtility.isSLE) {
				ResultSet rs = null;

				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				// sleSaleQuery =
				// "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				// sleSaleRefQuery =
				// "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				// slePwtQuery =
				// "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				sleSaleQuery = "select ifnull(sum(mrp_amt),0)as mrpSale, ifnull(sum(retailer_net_amt),0) as netSale from st_sle_ret_sale where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				sleSaleRefQuery = "select ifnull(sum(mrp_amt),0)as mrpRef, ifnull(sum(retailer_net_amt),0)as netRef from st_sle_ret_sale_refund where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				slePwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm-govt_claim_comm),0)as netPwt from st_sle_ret_pwt where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";

				pstmt = connection.prepareStatement(sleSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLESaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(sleSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLERefundQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(slePwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSlePWT = rs.getDouble("netPwt");
					mrpSlePWT = rs.getDouble("mrpPwt");
				}

				netSleSale = netSaleTemp - netRefTemp;
				mrpSleSale = mrpSaleTemp - mrpRefTemp;
			}
			if (ReportUtility.isIW) {
				ResultSet rs = null;

				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				// sleSaleQuery =
				// "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				// sleSaleRefQuery =
				// "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				// slePwtQuery =
				// "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				iwSaleQuery = "select ifnull(sum(mrp_amt),0)as mrpSale, ifnull(sum(retailer_net_amt),0) as netSale from st_iw_ret_sale where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				iwSaleRefQuery = "select ifnull(sum(mrp_amt),0)as mrpRef, ifnull(sum(retailer_net_amt),0)as netRef from st_iw_ret_sale_refund where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				iwPwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_iw_ret_pwt where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";

				pstmt = connection.prepareStatement(iwSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(iwSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWRefundQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(iwPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netIwPWT = rs.getDouble("netPwt");
					mrpIwPWT = rs.getDouble("mrpPwt");
				}

				netIwSale = netSaleTemp - netRefTemp;
				mrpIwSale = mrpSaleTemp - mrpRefTemp;
			}
			
			if (ReportUtility.isVS) {
				ResultSet rs = null;

				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				vsSaleQuery = "select ifnull(sum(mrp_amt),0)as mrpSale, ifnull(sum(retailer_net_amt),0) as netSale from st_vs_ret_sale where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				vsSaleRefQuery = "select ifnull(sum(mrp_amt),0)as mrpRef, ifnull(sum(retailer_net_amt),0)as netRef from st_vs_ret_sale_refund where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
				vsPwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_vs_ret_pwt where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";

				pstmt = connection.prepareStatement(vsSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(vsSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSRefundQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(vsPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netVsPWT = rs.getDouble("netPwt");
					mrpVsPWT = rs.getDouble("mrpPwt");
				}

				netVsSale = netSaleTemp - netRefTemp;
				mrpVsSale = mrpSaleTemp - mrpRefTemp;
			}

			pstmt = connection.prepareStatement(paymentQuery);
			pstmt.setInt(1, orgId);
			pstmt.setTimestamp(2, dBean.getStartTime());
			pstmt.setTimestamp(3, dBean.getEndTime());
			pstmt.setInt(4, orgId);
			pstmt.setTimestamp(5, dBean.getStartTime());
			pstmt.setTimestamp(6, dBean.getEndTime());
			pstmt.setInt(7, orgId);
			pstmt.setTimestamp(8, dBean.getStartTime());
			pstmt.setTimestamp(9, dBean.getEndTime());
			pstmt.setInt(10, orgId);
			pstmt.setTimestamp(11, dBean.getStartTime());
			pstmt.setTimestamp(12, dBean.getEndTime());
			pstmt.setInt(13, orgId);
			pstmt.setTimestamp(14, dBean.getStartTime());
			pstmt.setTimestamp(15, dBean.getEndTime());
			pstmt.setInt(16,orgId);
			pstmt.setTimestamp(17, dBean.getStartTime());
			pstmt.setTimestamp(18, dBean.getEndTime());
			logger.debug("paymentQuery:- " + pstmt);
			rs8 = pstmt.executeQuery();

			double netSale = 0.0;
			double mrpSale = 0.0;
			double netPwt = 0.0;
			double mrpPwt = 0.0;
			double netPurchase = 0.0;
			double mrpPurchase = 0.0;
			double netPayment = 0.0;
			double scratchSale = 0.0;
			double opBal = 0.0;
			double cashCol = 0.0;
			double profit = 0.0;

			if (rs1 != null && rs2 != null && rs3 != null) {
				rs1.next();
				rs2.next();
				rs3.next();
				netSale = rs1.getDouble("netSale") - rs2.getDouble("netRef");
				mrpSale = rs1.getDouble("mrpSale") - rs2.getDouble("mrpRef");
				netPwt = rs3.getDouble("netPwt");
				mrpPwt = rs3.getDouble("mrpPwt");
			}
			if (rs4 != null && rs5 != null && rs6 != null && rs7 != null) {
				rs4.next();
				rs5.next();
				rs6.next();
				rs7.next();
				netPurchase = rs4.getDouble("netPur")
						- rs5.getDouble("netSaleRet");
				mrpPurchase = rs4.getDouble("mrpPur")
						- rs5.getDouble("mrpSaleRet");
				netPwt += rs6.getDouble("net_amt");
				mrpPwt += rs6.getDouble("mrpPwt");
				scratchSale = rs7.getDouble("soldTicketPrice");
			}
			rs8.next();
			netPayment = rs8.getDouble("cashAmt") + rs8.getDouble("chqAmt")
					+ rs8.getDouble("crnAmt") + rs8.getDouble("bankDepoAmt")
					- rs8.getDouble("chqBounce") - rs8.getDouble("dbnAmt");

			opBal = netPayment + netPwt - netPurchase - netSale - netSaleCS
					+ netWdrwl - netDeposit + netSlePWT - netSleSale + netIwPWT
					- netIwSale + netVsPWT - netVsSale;
			cashCol = -netPayment - mrpPwt - olaWithdrawal + mrpSale
					+ scratchSale + mrpSaleCS + olaDeposit - mrpSlePWT
					+ mrpSleSale - mrpIwPWT + mrpIwSale - mrpVsPWT + mrpVsSale;
			profit = mrpPurchase - netPurchase + mrpSale - netSale + mrpPwt
					- netPwt + mrpSaleCS - netSaleCS + olaDeposit - netDeposit
					+ (olaWithdrawal - netWdrwl) + mrpSleSale - netSleSale
					+ mrpSlePWT - netSlePWT + mrpIwSale - netIwSale + mrpIwPWT
					- netIwPWT + mrpVsSale - netVsSale + mrpVsPWT - netVsPWT;
			bean.setClrBal(opBal + "");
			bean.setPurchase(netPurchase + "");
			// bean.setNetPwt(netPwt + "");
			// bean.setNetsale(netSale + "");
			bean.setNetPwt(netPwt +"");
			bean.setNetsale(netSale +"");
			bean.setNetSaleRefund("");
			bean.setNetPayment(netPayment + "");
			bean.setScratchSale(scratchSale + "");
			bean.setCashCol(cashCol + "");
			bean.setProfit(profit + "");
			bean.setNetSaleCS(netSaleCS + "");

			bean.setSleSale(netSleSale + "");
			bean.setSlePwt(netSlePWT + "");
			
			bean.setIwSale(netIwSale + "");
			bean.setIwPwt(netIwPWT + "");
			
			bean.setVsSale(netVsSale + "");
			bean.setVsPwt(netVsPWT + "");

			return bean;
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
		return bean;
	}

	 public DailyLedgerBean getRetLegderDetail(Timestamp deployDate, DateBeans dBean, UserInfoBean userBean, ReportStatusBean reportStatusBean) {
	        DailyLedgerBean bean = new DailyLedgerBean();
	        Connection connection = null;
	        
	        String drawSaleQuery = null;
	        String drawSaleRefQuery = null;
	        String drawPwtQuery = null;
	        String scratchPurchaseQuery = null;
	        String scratchSaleRetQuery = null;
	        String scratchPwtQuery = null;
	        String scratchSaleQuery = null;
	        String paymentQuery = null;

	        String sleSaleQuery = null;
	        String sleSaleRefQuery = null;
	        String slePwtQuery = null;

	        String iwSaleQuery = null;
	        String iwSaleRefQuery = null;
	        String iwPwtQuery = null;

	        double netSaleCS = 0.0;
	        double mrpSaleCS = 0.0;
	        double olaDeposit = 0.0;
	        double olaWithdrawal = 0.0;
	        double netDeposit = 0.0;
	        double netWdrwl = 0.0;

	        double netSleSale = 0.0;
	        double mrpSleSale = 0.0;
	        double netSlePWT = 0.0;
	        double mrpSlePWT = 0.0;

	        double netIwSale = 0.0;
	        double mrpIwSale = 0.0;
	        double netIwPWT = 0.0;
	        double mrpIwPWT = 0.0;
	        
	        int orgId = 0;
	        try {
	            connection = DBConnect.getConnection();
	            PreparedStatement pstmt = null;
	            ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null;
	            // List<Integer> gameNumList = Util.getGameNumberList();
	            List<Integer> gameNumList = Util.getLMSGameNumberList();
	            StringBuilder drawSaleQueryBuilder = new StringBuilder();
	            StringBuilder drawSaleRefQueryBuilder = new StringBuilder();
	            StringBuilder drawPwtQueryBuilder = new StringBuilder();
	            
	            
	            orgId = userBean.getUserOrgId();

	            double openingbal = new RetailerOpeningBalanceHelper().getRetailerOpeningBal(deployDate, dBean.getStartTime(), userBean.getUserOrgId(), userBean.getParentOrgId(), connection);

	            for (int i = 0; i < gameNumList.size(); i++) {
	                drawSaleQueryBuilder
	                        .append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_"
	                                + gameNumList.get(i)
	                                + " where retailer_org_id = ? union ");
	                drawSaleRefQueryBuilder
	                        .append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_refund_"
	                                + gameNumList.get(i)
	                                + " where retailer_org_id = ? union ");
	                drawPwtQueryBuilder
	                        .append("select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_dg_ret_pwt_"
	                                + gameNumList.get(i)
	                                + " where retailer_org_id = ? union ");
	            }
	            drawSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
	                    + drawSaleQueryBuilder.toString().substring(0,
	                            drawSaleQueryBuilder.lastIndexOf(" union "))
	                    + ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	            drawSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from ("
	                    + drawSaleRefQueryBuilder.toString().substring(0,
	                            drawSaleRefQueryBuilder.lastIndexOf(" union "))
	                    + ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	            drawPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from ("
	                    + drawPwtQueryBuilder.toString().substring(0,
	                            drawPwtQueryBuilder.lastIndexOf(" union "))
	                    + ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	            scratchPurchaseQuery = "select ifnull(sum(mrp_amt),0)as mrpPur, ifnull(sum(net_amt),0)as netPur from st_se_agent_retailer_transaction sart, st_lms_agent_transaction_master atm where sart.transaction_type='SALE' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
	            scratchSaleRetQuery = "select ifnull(sum(mrp_amt),0)as mrpSaleRet, ifnull(sum(net_amt),0)as netSaleRet from st_se_agent_retailer_transaction sart, st_lms_retailer_transaction_master atm where sart.transaction_type='SALE_RET' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
	            scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as  net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and srp.transaction_id = atm.transaction_id";
	            scratchSaleQuery = "select ifnull(sum(cnt.ticket_count*unit.ticket_price),0)as soldTicketPrice from (select game_id, ifnull(sum(sold_tickets),0) as ticket_count from st_se_game_ticket_inv_history where current_owner='RETAILER' and current_owner_id = ? and date(date)=? group by game_id)cnt, (select game_id, ticket_price from st_se_game_master)unit where cnt.game_id = unit.game_id";
	            paymentQuery = "select cash.cashAmt, chq.chqAmt, chqboun.chqBounce, dbnote.dbnAmt,crnote.crnAmt,bankDeposit.bankDepoAmt from (select ifnull(sum(slact.amount),0)as cashAmt from st_lms_agent_cash_transaction slact, st_lms_agent_transaction_master atm where slact.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slact.transaction_id = atm.transaction_id)as cash, (select ifnull(sum(cheque_amt),0)as chqAmt from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and (slasc.transaction_type='CHEQUE' or slasc.transaction_type='CLOSED') and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chq, (select ifnull(sum(cheque_amt),0)as chqBounce from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and slasc.transaction_type='CHQ_BOUNCE' and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chqboun, (select ifnull(sum(amount),0)as dbnAmt from st_lms_agent_debit_note sladn, st_lms_agent_transaction_master atm where sladn.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sladn.transaction_id = atm.transaction_id) as dbnote ,"
	                    + "(select ifnull(sum(amount),0)as crnAmt from st_lms_agent_credit_note slacn, st_lms_agent_transaction_master atm where slacn.retailer_org_id =  ? and (atm.transaction_date)>= ? and (atm.transaction_date)<= ? and slacn.transaction_id = atm.transaction_id) as crnote,(select ifnull(sum(slabdt.amount),0)as bankDepoAmt from st_lms_agent_bank_deposit_transaction slabdt, st_lms_agent_transaction_master atm where slabdt.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slabdt.transaction_id = atm.transaction_id)as bankDeposit";

	            if (ReportUtility.isDG) {
	                pstmt = connection.prepareStatement(drawSaleQuery);
	                int i = 0;
	                for (i = 1; i <= gameNumList.size(); i++) {
	                    pstmt.setInt(i, orgId);
	                }
	                pstmt.setTimestamp(i, dBean.getStartTime());
	                pstmt.setTimestamp(i + 1, dBean.getEndTime());
	                logger.debug("drawSaleQuery:- " + pstmt);
	                rs1 = pstmt.executeQuery();

	                pstmt = connection.prepareStatement(drawSaleRefQuery);
	                for (i = 1; i <= gameNumList.size(); i++) {
	                    pstmt.setInt(i, orgId);
	                }
	                pstmt.setTimestamp(i, dBean.getStartTime());
	                pstmt.setTimestamp(i + 1, dBean.getEndTime());
	                logger.debug("drawSaleRefQuery:- " + pstmt);
	                rs2 = pstmt.executeQuery();

	                pstmt = connection.prepareStatement(drawPwtQuery);
	                for (i = 1; i <= gameNumList.size(); i++) {
	                    pstmt.setInt(i, orgId);
	                }
	                pstmt.setTimestamp(i, dBean.getStartTime());
	                pstmt.setTimestamp(i + 1, dBean.getEndTime());
	                logger.debug("drawPwtQuery:- " + pstmt);
	                rs3 = pstmt.executeQuery();
	            }
	            if (ReportUtility.isSE) {
	                pstmt = connection.prepareStatement(scratchPurchaseQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("scratchPurchaseQuery:- " + pstmt);
	                rs4 = pstmt.executeQuery();

	                pstmt = connection.prepareStatement(scratchSaleRetQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("scratchSaleRetQuery:- " + pstmt);
	                rs5 = pstmt.executeQuery();

	                pstmt = connection.prepareStatement(scratchPwtQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("scratchPwtQuery:- " + pstmt);
	                rs6 = pstmt.executeQuery();

	                pstmt = connection.prepareStatement(scratchSaleQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());

	                logger.debug("scratchSaleQuery:- " + pstmt);
	                rs7 = pstmt.executeQuery();
	            }

	            if (ReportUtility.isCS) {
	                List<Integer> csGameNumList = CSUtil.fetchCSGameNumList();
	                StringBuilder csSaleQueryBuilder = new StringBuilder("");
	                String csSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from (";

	                StringBuilder csRefundQueryBuilder = new StringBuilder("");
	                String csRefundQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from (";

	                for (int j = 0; j < csGameNumList.size(); j++) {
	                    csSaleQueryBuilder
	                            .append("select transaction_id, mrp_amt, net_amt from st_cs_sale_"
	                                    + csGameNumList.get(j)
	                                    + " where retailer_org_id = "
	                                    + orgId
	                                    + " union ");

	                    csRefundQueryBuilder
	                            .append("select transaction_id, mrp_amt, net_amt from st_cs_refund_"
	                                    + csGameNumList.get(j)
	                                    + " where retailer_org_id = "
	                                    + orgId
	                                    + " union ");
	                }

	                csSaleQuery = csSaleQuery
	                        + csSaleQueryBuilder.toString().substring(
	                                0,
	                                csSaleQueryBuilder.toString().lastIndexOf(
	                                        " union "));

	                csRefundQuery = csRefundQuery
	                        + csRefundQueryBuilder.toString().substring(
	                                0,
	                                csRefundQueryBuilder.toString().lastIndexOf(
	                                        " union "));

	                csSaleQuery = csSaleQuery
	                        + ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

	                csRefundQuery = csRefundQuery
	                        + ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

	                PreparedStatement pstmtCS = connection
	                        .prepareStatement(csSaleQuery);
	                pstmtCS.setTimestamp(1, dBean.getStartTime());
	                pstmtCS.setTimestamp(2, dBean.getEndTime());
	                System.out.println("***getRetLegderDetail CS Sale Query :"
	                        + pstmtCS);
	                ResultSet rsCSSale = pstmtCS.executeQuery();

	                pstmtCS = connection.prepareStatement(csRefundQuery);
	                pstmtCS.setTimestamp(1, dBean.getStartTime());
	                pstmtCS.setTimestamp(2, dBean.getEndTime());
	                System.out.println("***getRetLegderDetail CS Refund Query :"
	                        + pstmtCS);
	                ResultSet rsCSRefund = pstmtCS.executeQuery();

	                if (rsCSSale != null && rsCSRefund != null) {
	                    rsCSSale.next();
	                    rsCSRefund.next();

	                    netSaleCS = rsCSSale.getDouble("netSale")
	                            - rsCSRefund.getDouble("netRef");
	                    mrpSaleCS = rsCSSale.getDouble("mrpSale")
	                            - rsCSRefund.getDouble("mrpRef");
	                }

	            }
	            if (ReportUtility.isOLA) {
	                OlaOrgReportRequestBean reqBean = new OlaOrgReportRequestBean();
	                reqBean.setFromDate(dBean.getStartTime().toString());
	                reqBean.setToDate(dBean.getEndTime().toString());
	                reqBean.setOrgId(orgId);
	                OlaOrgReportResponseBean respBean = OlaRetailerReportControllerImpl
	                        .fetchDepositWithdrawlSinglaRetailer(reqBean,
	                                connection);
	                olaDeposit = respBean.getMrpDepositAmt();
	                olaWithdrawal = respBean.getMrpWithdrawalAmt();
	                netDeposit = respBean.getNetDepositAmt();
	                netWdrwl = respBean.getNetWithdrawalAmt();
	                bean.setOlaDeposit(Double.toString(netDeposit));
	                bean.setOlaWithdrawal(Double.toString(netWdrwl));
	            }

	            if (ReportUtility.isSLE) {
	                ResultSet rs = null;

	                double netSaleTemp = 0.0;
	                double netRefTemp = 0.0;
	                double mrpSaleTemp = 0.0;
	                double mrpRefTemp = 0.0;

	                // sleSaleQuery =
	                // "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	                // sleSaleRefQuery =
	                // "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	                // slePwtQuery =
	                // "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

	                sleSaleQuery = "select ifnull(sum(mrp_amt),0)as mrpSale, ifnull(sum(retailer_net_amt),0) as netSale from st_sle_ret_sale where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
	                sleSaleRefQuery = "select ifnull(sum(mrp_amt),0)as mrpRef, ifnull(sum(retailer_net_amt),0)as netRef from st_sle_ret_sale_refund where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
	                slePwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_sle_ret_pwt where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";

	                pstmt = connection.prepareStatement(sleSaleQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("SLESaleQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netSaleTemp = rs.getDouble("netSale");
	                    mrpSaleTemp = rs.getDouble("mrpSale");
	                }

	                pstmt = connection.prepareStatement(sleSaleRefQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("SLERefundQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netRefTemp = rs.getDouble("netRef");
	                    mrpRefTemp = rs.getDouble("mrpRef");
	                }

	                pstmt = connection.prepareStatement(slePwtQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("SLEPwtQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netSlePWT = rs.getDouble("netPwt");
	                    mrpSlePWT = rs.getDouble("mrpPwt");
	                }

	                netSleSale = netSaleTemp - netRefTemp;
	                mrpSleSale = mrpSaleTemp - mrpRefTemp;
	            }
	            if (ReportUtility.isIW) {
	                ResultSet rs = null;

	                double netSaleTemp = 0.0;
	                double netRefTemp = 0.0;
	                double mrpSaleTemp = 0.0;
	                double mrpRefTemp = 0.0;

	                // sleSaleQuery =
	                // "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	                // sleSaleRefQuery =
	                // "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
	                // slePwtQuery =
	                // "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

	                iwSaleQuery = "select ifnull(sum(mrp_amt),0)as mrpSale, ifnull(sum(retailer_net_amt),0) as netSale from st_iw_ret_sale where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
	                iwSaleRefQuery = "select ifnull(sum(mrp_amt),0)as mrpRef, ifnull(sum(retailer_net_amt),0)as netRef from st_iw_ret_sale_refund where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";
	                iwPwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_iw_ret_pwt where retailer_org_id = ? and transaction_date>=? and transaction_date<=?";

	                pstmt = connection.prepareStatement(iwSaleQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("IWSaleQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netSaleTemp = rs.getDouble("netSale");
	                    mrpSaleTemp = rs.getDouble("mrpSale");
	                }

	                pstmt = connection.prepareStatement(iwSaleRefQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("IWRefundQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netRefTemp = rs.getDouble("netRef");
	                    mrpRefTemp = rs.getDouble("mrpRef");
	                }

	                pstmt = connection.prepareStatement(iwPwtQuery);
	                pstmt.setInt(1, orgId);
	                pstmt.setTimestamp(2, dBean.getStartTime());
	                pstmt.setTimestamp(3, dBean.getEndTime());
	                logger.debug("IWPwtQuery - " + pstmt);
	                rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    netIwPWT = rs.getDouble("netPwt");
	                    mrpIwPWT = rs.getDouble("mrpPwt");
	                }

	                netIwSale = netSaleTemp - netRefTemp;
	                mrpIwSale = mrpSaleTemp - mrpRefTemp;
	            }

	            pstmt = connection.prepareStatement(paymentQuery);
	            pstmt.setInt(1, orgId);
	            pstmt.setTimestamp(2, dBean.getStartTime());
	            pstmt.setTimestamp(3, dBean.getEndTime());
	            pstmt.setInt(4, orgId);
	            pstmt.setTimestamp(5, dBean.getStartTime());
	            pstmt.setTimestamp(6, dBean.getEndTime());
	            pstmt.setInt(7, orgId);
	            pstmt.setTimestamp(8, dBean.getStartTime());
	            pstmt.setTimestamp(9, dBean.getEndTime());
	            pstmt.setInt(10, orgId);
	            pstmt.setTimestamp(11, dBean.getStartTime());
	            pstmt.setTimestamp(12, dBean.getEndTime());
	            pstmt.setInt(13, orgId);
	            pstmt.setTimestamp(14, dBean.getStartTime());
	            pstmt.setTimestamp(15, dBean.getEndTime());
	            pstmt.setInt(16, orgId);
	            pstmt.setTimestamp(17, dBean.getStartTime());
	            pstmt.setTimestamp(18, dBean.getEndTime());
	            logger.debug("paymentQuery:- " + pstmt);
	            rs8 = pstmt.executeQuery();

	            double netSale = 0.0;
	            double mrpSale = 0.0;
	            double netPwt = 0.0;
	            double mrpPwt = 0.0;
	            double netPurchase = 0.0;
	            double mrpPurchase = 0.0;
	            double netPayment = 0.0;
	            double scratchSale = 0.0;
	            double opBal = 0.0;
	            double cashCol = 0.0;
	            double profit = 0.0;

	            if (rs1 != null && rs2 != null && rs3 != null) {
	                rs1.next();
	                rs2.next();
	                rs3.next();
	                netSale = rs1.getDouble("netSale") - rs2.getDouble("netRef");
	                mrpSale = rs1.getDouble("mrpSale") - rs2.getDouble("mrpRef");
	                netPwt = rs3.getDouble("netPwt");
	                mrpPwt = rs3.getDouble("mrpPwt");
	            }
	            if (rs4 != null && rs5 != null && rs6 != null && rs7 != null) {
	                rs4.next();
	                rs5.next();
	                rs6.next();
	                rs7.next();
	                netPurchase = rs4.getDouble("netPur")
	                        - rs5.getDouble("netSaleRet");
	                mrpPurchase = rs4.getDouble("mrpPur")
	                        - rs5.getDouble("mrpSaleRet");
	                netPwt += rs6.getDouble("net_amt");
	                mrpPwt += rs6.getDouble("mrpPwt");
	                scratchSale = rs7.getDouble("soldTicketPrice");
	            }
	            rs8.next();
	            netPayment = rs8.getDouble("cashAmt") + rs8.getDouble("chqAmt") + rs8.getDouble("crnAmt") + rs8.getDouble("bankDepoAmt") - rs8.getDouble("chqBounce") - rs8.getDouble("dbnAmt");

	            opBal = netPayment + netPwt - netPurchase - netSale - netSaleCS
	                    + netWdrwl - netDeposit + netSlePWT - netSleSale + netIwPWT
	                    - netIwSale;
	            cashCol = -netPayment - mrpPwt - olaWithdrawal + mrpSale
	                    + scratchSale + mrpSaleCS + olaDeposit - mrpSlePWT
	                    + mrpSleSale - mrpIwPWT + mrpIwSale;
	            profit = mrpPurchase - netPurchase + mrpSale - netSale + mrpPwt
	                    - netPwt + mrpSaleCS - netSaleCS + olaDeposit - netDeposit
	                    + (olaWithdrawal - netWdrwl) + mrpSleSale - netSleSale
	                    + mrpSlePWT - netSlePWT + mrpIwSale - netIwSale + mrpIwPWT
	                    - netIwPWT;

	            AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
	            ajxHelper.getAvlblCreditAmt(userBean, connection);
	            double bal = userBean.getAvailableCreditLimit()
	                    - userBean.getClaimableBal();
	            String balance = Util.getBalInString(bal);
	            bean.setBalance(balance);
	            bean.setOpenBal(String.valueOf(openingbal));
	            bean.setClrBal(openingbal - opBal + "");
	            bean.setPurchase(netPurchase + "");
	            // bean.setNetPwt(netPwt + "");
	            // bean.setNetsale(netSale + "");
	            bean.setNetPwt(netPwt + "");
	            bean.setNetsale(netSale + "");
	            bean.setNetSaleRefund("");
	            bean.setNetPayment(netPayment + "");
	            bean.setScratchSale(scratchSale + "");
	            bean.setCashCol(cashCol + "");
	            bean.setProfit(profit + "");
	            bean.setNetSaleCS(netSaleCS + "");

	            bean.setSleSale(netSleSale + "");
	            bean.setSlePwt(netSlePWT + "");
	            bean.setVsSale(0.0+"");
	            bean.setVsPwt(0.0+"");

	            bean.setIwSale(netIwSale + "");
	            bean.setIwPwt(netIwPWT + "");

	            return bean;
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
	        return bean;
	    }
	 
	public DailyLedgerBean getRetLegderDetailClXclInc(Timestamp deployDate,
			DateBeans dBean, UserInfoBean userBean,
			ReportStatusBean reportStatusBean) throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int orgId = 0;
		double netSale = 0.0;
		double mrpSale = 0.0;
		double netPwt = 0.0;
		double mrpPwt = 0.0;
		double netPurchase = 0.0;
		double mrpPurchase = 0.0;
		double netPayment = 0.0;
		double scratchSale = 0.0;
		double netSaleCS = 0.0;
		double mrpSaleCS = 0.0;
		double netSleSale = 0.0;
		double mrpSleSale = 0.0;
		double netSlePWT = 0.0;
		double mrpSlePWT = 0.0;
		
		double netIwSale = 0.0;
		double mrpIwSale = 0.0;
		double netIwPWT = 0.0;
		double mrpIwPWT = 0.0;
		
		double netVsSale = 0.0;
		double mrpVsSale = 0.0;
		double netVsPWT = 0.0;
		double mrpVsPWT = 0.0;

		DailyLedgerBean bean = null;
		try {
			if ("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA"))
					|| "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				connection = DBConnect.getConnection();
			else
				connection = DBConnectReplica.getConnection();

			orgId = userBean.getUserOrgId();

			double openingbal = new RetailerOpeningBalanceHelper()
					.getRetailerOpeningBalIncludingCLXCL(deployDate, dBean
							.getStartTime(), userBean.getUserOrgId(), userBean
							.getParentOrgId(), connection);
			System.out.println("openingbal - " + openingbal);
			// double openingbal = 800;

			if (ServicesBean.isDG()) {
				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				// List<Integer> gameNumList = Util.getGameNumberList();
				List<Integer> gameNumList = Util.getLMSGameNumberList();

				StringBuilder drawSaleQueryBuilder = new StringBuilder();
				StringBuilder drawSaleRefQueryBuilder = new StringBuilder();
				StringBuilder drawPwtQueryBuilder = new StringBuilder();

				for (int i = 0; i < gameNumList.size(); i++) {
					drawSaleQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_"
									+ gameNumList.get(i)
									+ " where retailer_org_id = ? union ");
					drawSaleRefQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_refund_"
									+ gameNumList.get(i)
									+ " where retailer_org_id = ? union ");
					drawPwtQueryBuilder
							.append("select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm-govt_claim_comm)as  net_amt from st_dg_ret_pwt_"
									+ gameNumList.get(i)
									+ " where retailer_org_id = ? union ");
				}
				String drawSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
						+ drawSaleQueryBuilder.toString().substring(0,
								drawSaleQueryBuilder.lastIndexOf(" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String drawSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from ("
						+ drawSaleRefQueryBuilder.toString().substring(0,
								drawSaleRefQueryBuilder.lastIndexOf(" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String drawPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from ("
						+ drawPwtQueryBuilder.toString().substring(0,
								drawPwtQueryBuilder.lastIndexOf(" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(drawSaleQuery);
				int i = 0;
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("DrawSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(drawSaleRefQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("DrawSaleRefundQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(drawPwtQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setTimestamp(i, dBean.getStartTime());
				pstmt.setTimestamp(i + 1, dBean.getEndTime());
				logger.debug("DrawPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netPwt = rs.getDouble("netPwt");
					mrpPwt = rs.getDouble("mrpPwt");
				}

				netSale = netSaleTemp - netRefTemp;
				mrpSale = mrpSaleTemp - mrpRefTemp;
			}
			if (ServicesBean.isSE()) {
				double netPurTemp = 0.0;
				double netSaleRetTemp = 0.0;
				double mrpPurTemp = 0.0;
				double mrpSaleRetTemp = 0.0;

				String scratchPurchaseQuery = "select ifnull(sum(mrp_amt),0)as mrpPur, ifnull(sum(net_amt),0)as netPur from st_se_agent_retailer_transaction sart, st_lms_agent_transaction_master atm where sart.transaction_type='SALE' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
				String scratchSaleRetQuery = "select ifnull(sum(mrp_amt),0)as mrpSaleRet, ifnull(sum(net_amt),0)as netSaleRet from st_se_agent_retailer_transaction sart, st_lms_retailer_transaction_master atm where sart.transaction_type='SALE_RET' and sart.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sart.transaction_id = atm.transaction_id";
				String scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as  net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and srp.transaction_id = atm.transaction_id";
				String scratchSaleQuery = "select ifnull(sum(cnt.ticket_count*unit.ticket_price),0)as soldTicketPrice from (select game_id, ifnull(sum(sold_tickets),0) as ticket_count from st_se_game_ticket_inv_history where current_owner='RETAILER' and current_owner_id = ? and date(date)=? group by game_id)cnt, (select game_id, ticket_price from st_se_game_master)unit where cnt.game_id = unit.game_id";

				pstmt = connection.prepareStatement(scratchPurchaseQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("ScratchPurchaseQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netPurTemp = rs.getDouble("netPur");
					mrpPurTemp = rs.getDouble("mrpPur");
				}

				pstmt = connection.prepareStatement(scratchSaleRetQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("ScratchSaleRetQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleRetTemp = rs.getDouble("netSaleRet");
					mrpSaleRetTemp = rs.getDouble("mrpSaleRet");
				}

				pstmt = connection.prepareStatement(scratchPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("ScratchPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netPwt += rs.getDouble("net_amt");
					mrpPwt += rs.getDouble("mrpPwt");
				}

				pstmt = connection.prepareStatement(scratchSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				logger.debug("scratchSaleQuery:- " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					scratchSale = rs.getDouble("soldTicketPrice");
				}

				netPurchase = netPurTemp - netSaleRetTemp;
				mrpPurchase = mrpPurTemp - mrpSaleRetTemp;
			}

			if (ServicesBean.isCS()) {
				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				List<Integer> csGameNumList = CSUtil.fetchCSGameNumList();
				StringBuilder csSaleQueryBuilder = new StringBuilder();
				StringBuilder csRefundQueryBuilder = new StringBuilder();

				for (int j = 0; j < csGameNumList.size(); j++) {
					csSaleQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_cs_sale_"
									+ csGameNumList.get(j)
									+ " where retailer_org_id = "
									+ orgId
									+ " union ");
					csRefundQueryBuilder
							.append("select transaction_id, mrp_amt, net_amt from st_cs_refund_"
									+ csGameNumList.get(j)
									+ " where retailer_org_id = "
									+ orgId
									+ " union ");
				}

				String csSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
						+ csSaleQueryBuilder.toString().substring(
								0,
								csSaleQueryBuilder.toString().lastIndexOf(
										" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				String csRefundQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from ("
						+ csRefundQueryBuilder.toString().substring(
								0,
								csRefundQueryBuilder.toString().lastIndexOf(
										" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(csSaleQuery);
				pstmt.setTimestamp(1, dBean.getStartTime());
				pstmt.setTimestamp(2, dBean.getEndTime());
				logger.debug("CSSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(csRefundQuery);
				pstmt.setTimestamp(1, dBean.getStartTime());
				pstmt.setTimestamp(2, dBean.getEndTime());
				logger.debug("CSSaleRefundQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				netSaleCS = netSaleTemp - netRefTemp;
				mrpSaleCS = mrpSaleTemp - mrpRefTemp;
			}

			if (ServicesBean.isSLE()) {
				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				String sleSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String sleSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String slePwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm-govt_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(sleSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLESaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(sleSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(slePwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSlePWT = rs.getDouble("netPwt");
					mrpSlePWT = rs.getDouble("mrpPwt");
				}

				netSleSale = netSaleTemp - netRefTemp;
				mrpSleSale = mrpSaleTemp - mrpRefTemp;
			}
			
			if (ServicesBean.isIW()) {
				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				String iwSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_iw_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(iwSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(iwSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(iwPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("IWPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netIwPWT = rs.getDouble("netPwt");
					mrpIwPWT = rs.getDouble("mrpPwt");
				}

				netIwSale = netSaleTemp - netRefTemp;
				mrpIwSale = mrpSaleTemp - mrpRefTemp;
			}

			if (ServicesBean.isVS()) {
				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				String vsSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_vs_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String vsSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_vs_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String vsPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_vs_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(vsSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSSaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(vsSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(vsPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setTimestamp(2, dBean.getStartTime());
				pstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("VSPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netVsPWT = rs.getDouble("netPwt");
					mrpVsPWT = rs.getDouble("mrpPwt");
				}

				netVsSale = netSaleTemp - netRefTemp;
				mrpVsSale = mrpSaleTemp - mrpRefTemp;
			}
			
			String paymentQuery = "select cash.cashAmt, chq.chqAmt, chqboun.chqBounce, dbnote.dbnAmt,crnote.crnAmt from (select ifnull(sum(slact.amount),0)as cashAmt from st_lms_agent_cash_transaction slact, st_lms_agent_transaction_master atm where slact.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slact.transaction_id = atm.transaction_id)as cash, (select ifnull(sum(cheque_amt),0)as chqAmt from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and (slasc.transaction_type='CHEQUE' or slasc.transaction_type='CLOSED') and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chq, (select ifnull(sum(cheque_amt),0)as chqBounce from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and slasc.transaction_type='CHQ_BOUNCE' and (atm.transaction_date)>=? and (atm.transaction_date)<=? and slasc.transaction_id = atm.transaction_id)as chqboun, (select ifnull(sum(amount),0)as dbnAmt from st_lms_agent_debit_note sladn, st_lms_agent_transaction_master atm where sladn.retailer_org_id = ? and (atm.transaction_date)>=? and (atm.transaction_date)<=? and sladn.transaction_id = atm.transaction_id) as dbnote ,"
					+ "(select ifnull(sum(amount),0)as crnAmt from st_lms_agent_credit_note slacn, st_lms_agent_transaction_master atm where slacn.retailer_org_id =  ? and (atm.transaction_date)>= ? and (atm.transaction_date)<= ? and slacn.transaction_id = atm.transaction_id) as crnote";
			pstmt = connection.prepareStatement(paymentQuery);
			pstmt.setInt(1, orgId);
			pstmt.setTimestamp(2, dBean.getStartTime());
			pstmt.setTimestamp(3, dBean.getEndTime());
			pstmt.setInt(4, orgId);
			pstmt.setTimestamp(5, dBean.getStartTime());
			pstmt.setTimestamp(6, dBean.getEndTime());
			pstmt.setInt(7, orgId);
			pstmt.setTimestamp(8, dBean.getStartTime());
			pstmt.setTimestamp(9, dBean.getEndTime());
			pstmt.setInt(10, orgId);
			pstmt.setTimestamp(11, dBean.getStartTime());
			pstmt.setTimestamp(12, dBean.getEndTime());
			pstmt.setInt(13, orgId);
			pstmt.setTimestamp(14, dBean.getStartTime());
			pstmt.setTimestamp(15, dBean.getEndTime());
			logger.debug("PaymentQuery - " + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				netPayment = rs.getDouble("cashAmt") + rs.getDouble("chqAmt")
						+ rs.getDouble("crnAmt") - rs.getDouble("chqBounce")
						- rs.getDouble("dbnAmt");
			}

			double opBal = 0.0;
			double cashCol = 0.0;
			double profit = 0.0;

			double clXclAmount = 0.0;
			pstmt = connection
					.prepareStatement("SELECT IFNULL(SUM(amount),0.0) AS amount FROM st_lms_cl_xcl_update_history WHERE organization_id=? AND date_time>=? AND date_time<?;");
			pstmt.setInt(1, orgId);
			pstmt.setTimestamp(2, dBean.getStartTime());
			pstmt.setTimestamp(3, dBean.getEndTime());
			logger.debug("ClXcl Statement - " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clXclAmount = rs.getDouble("amount");
			}

			opBal = netPayment + netPwt + netSlePWT + netIwPWT + netVsPWT - netPurchase - netSale
					- netSaleCS - netSleSale - netIwSale  - netVsSale;
			cashCol = -netPayment - mrpPwt - mrpSlePWT - mrpIwPWT - mrpVsPWT + mrpSale + scratchSale
					+ mrpSaleCS + mrpSleSale + mrpIwSale + mrpVsSale;
			profit = mrpPurchase - netPurchase + mrpSale - netSale + mrpPwt
					- netPwt + mrpSaleCS - netSaleCS + mrpSleSale + mrpIwSale
					+ mrpVsSale - netSleSale - netIwSale - netVsSale + mrpSlePWT + mrpIwPWT + mrpVsPWT - netSlePWT - netIwPWT - netVsPWT;

			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean, connection);
			double bal = userBean.getAvailableCreditLimit()
					- userBean.getClaimableBal();
			String balance = Util.getBalInString(bal);

			bean = new DailyLedgerBean();
			bean.setOpenBal(String.valueOf(openingbal));
			bean.setClrBal(String.valueOf(openingbal + opBal + clXclAmount));
			bean.setPurchase(String.valueOf(netPurchase));
			bean.setNetPwt(String.valueOf(netPwt));
			bean.setNetsale(String.valueOf(netSale));
			bean.setNetSaleRefund("");
			bean.setNetPayment(String.valueOf(netPayment));
			bean.setScratchSale(String.valueOf(scratchSale));
			bean.setCashCol(String.valueOf(cashCol));
			bean.setProfit(String.valueOf(profit));
			bean.setNetSaleCS(String.valueOf(netSaleCS));
			bean.setClXclAmount(String.valueOf(clXclAmount));
			bean.setBalance(balance);

			bean.setSleSale(String.valueOf(netSleSale));
			bean.setSlePwt(String.valueOf(netSlePWT));
			
			bean.setIwSale(String.valueOf(netIwSale));
			bean.setIwPwt(String.valueOf(netIwPWT));
			
			bean.setVsSale(String.valueOf(netVsSale));
			bean.setVsPwt(String.valueOf(netVsPWT));
		} catch (SQLException se) {
			logger.error("Exception", se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection, pstmt, rs);
		}

		return bean;
	}

	public String getRetName(int OrgId) {

		Connection connection = DBConnect.getConnection();
		String retName = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String appendOrder = QueryManager.getAppendOrgOrder();
			String agtOrgQry = "select name orgCode,organization_id from st_lms_organization_master where  organization_id = ? and organization_type=?  order by "
					+ appendOrder;
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {

				agtOrgQry = "select org_code orgCode,organization_id from st_lms_organization_master where  organization_id = ? and organization_type=?  order by "
						+ appendOrder;

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {

				agtOrgQry = "select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where  organization_id = ? and organization_type=?  order by "
						+ appendOrder;

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				agtOrgQry = "select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where  organization_id = ? and organization_type=?  order by "
						+ appendOrder;

			}

			pstmt = connection.prepareStatement(agtOrgQry);
			pstmt.setInt(1, OrgId);
			pstmt.setString(2, "RETAILER");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retName = rs.getString("orgCode");
			}
		} catch (SQLException e) {
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
		return retName;
	}

	public static synchronized  Map<String, List<Map<String, Double>>> retDailyLedgerGameWiseTerminal(
			DateBeans dBean, int retOrgId, ReportStatusBean reportStatusBean) throws Exception {
		Map<String, List<Map<String, Double>>> dataMap = new HashMap<String, List<Map<String, Double>>>();

		List<Map<String, Double>> dgDataList = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> sleDataList = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> iwDataList = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> vsDataList = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> seDataList = new ArrayList<Map<String, Double>>();

		Connection connection = null;

		try {
//			connection = DBConnect.getConnection();
			if ("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			connection = DBConnect.getConnection();
			else
				connection = DBConnectReplica.getConnection();
			
			PreparedStatement pstmt = null;
			ResultSet rs1 = null, rs2 = null, rs3 = null, rs_2 = null, rs4 = null, rs5 = null;

			// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

			// String saleSlotTimeString =
			// Utility.getPropertyValue("SALE_SLOT_TIME");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String saleSlotTimeString = new TrainingExpAgentHelper()
					.getPropertyValue(connection, sdf.format(dBean
							.getStartTime().getTime()));
			String[] gameWiseTimingArr = saleSlotTimeString.split("~");
			Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
			for (String gameWiseTiming : gameWiseTimingArr) {
				int gameId = Integer.parseInt(gameWiseTiming.split("#")[0]);
				String timeString = gameWiseTiming.split("#")[1];
				if (!"00:00:00_00:00:00".equals(timeString)) {
					gameSlotTimingMap.put(gameId, timeString);
				}
			}

			// Thisis hard coded check becuase onw to twellve game is added in
			// LMS but WGRL does not wants to display it in retailer reports
			String activeGamesQry = "select game_id, game_name from st_dg_game_master where game_status = 'OPEN' and game_name_dev<>'OneToTwelve'";
			pstmt = connection.prepareStatement(activeGamesQry);
			rs1 = pstmt.executeQuery();

			String saleQuery = "SELECT IFNULL(SUM(mrp_amt),0) mrpSale, IFNULL(SUM(net_amt),0) netSale FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_SALE','DG_SALE_OFFLINE') AND rs.retailer_org_id=?;";
			String saleRetQuery = "SELECT IFNULL(SUM(mrp_amt),0) mrpSaleRef, IFNULL(SUM(net_amt),0) netSaleRef FROM st_dg_ret_sale_refund_? rsr INNER JOIN st_lms_retailer_transaction_master rtm ON rsr.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND rsr.retailer_org_id=?;";
			String pwtQuery = "SELECT IFNULL(SUM(pwt_amt),0)AS mrpPwt, IFNULL(SUM(pwt_amt+retailer_claim_comm-govt_claim_comm),0)AS netPwt FROM st_dg_ret_pwt_? rp INNER JOIN st_lms_retailer_transaction_master rtm ON rp.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_PWT','DG_PWT_AUTO') AND rp.retailer_org_id=?;";

			/*
			 * String saleSlotQuery =
			 * "SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
			 * +
			 * "WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date>=? AND transaction_date<=? AND transaction_date<=? AND rs.retailer_org_id=? "
			 * +
			 * "AND rs.transaction_id NOT IN (SELECT ref_transaction_id FROM st_dg_ret_sale_refund_? refund INNER JOIN  st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
			 * +
			 * "AND transaction_date>=? AND transaction_date<=?  and refund.retailer_org_id=?);"
			 * ;
			 */

			String saleSlotQuery = " SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt  FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
					+ " left join (SELECT ref_transaction_id FROM st_dg_ret_sale_refund_? refund INNER JOIN  st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND transaction_date>=? AND transaction_date<=? and refund.retailer_org_id=?)ref on ref.ref_transaction_id=rs.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date>=? AND transaction_date<=? AND transaction_date<=? AND rs.retailer_org_id=? AND ref.ref_transaction_id is null ";

			PreparedStatement slotPstmt = connection
					.prepareStatement(saleSlotQuery);
			PreparedStatement salePstmt = connection
					.prepareStatement(saleQuery);
			PreparedStatement saleReturnPstmt = connection
					.prepareStatement(saleRetQuery);
			PreparedStatement pwtPstmt = connection.prepareStatement(pwtQuery);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			while (rs1.next()) {
				int gameId = rs1.getInt("game_id");
				/*
				 * String saleQuery =
				 * "select ifnull(sum(mrp_amt),0)mrpSale, ifnull(sum(net_amt),0)netSale from st_dg_ret_sale_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type in ('DG_SALE','DG_SALE_OFFLINE')) and retailer_org_id = ?"
				 * ; String saleRetQuery =
				 * "select ifnull(sum(mrp_amt),0)mrpSaleRef, ifnull(sum(net_amt),0)netSaleRef from st_dg_ret_sale_refund_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')) and retailer_org_id = ?"
				 * ; String pwtQuery =
				 * "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_dg_ret_pwt_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >=? and transaction_date<=? and transaction_type in ('DG_PWT','DG_PWT_AUTO')) and retailer_org_id = ?"
				 * ; String saleSlotQuery =
				 * "SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt FROM st_dg_ret_sale_"
				 * + rs1.getInt("game_id") +
				 * " WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date<=?"
				 * +
				 * " AND retailer_org_id=? AND transaction_id NOT IN(select ref_transaction_id from st_dg_ret_sale_refund_"
				 * +rs1.getInt("game_id")+
				 * " refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>=?));"
				 * ;
				 */

				String slotTiming = gameSlotTimingMap.get(gameId);
				if (slotTiming != null) {
					// SimpleDateFormat simpleDateFormat = new
					// SimpleDateFormat("HH:mm:ss");
					// simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					// long startTimeSlot =
					// simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
					// long endTimeSlot =
					// simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

					/*
					 * pstmt = connection.prepareStatement(saleQuery);
					 * pstmt.setTimestamp(1, new
					 * Timestamp(dBean.getStartTime().getTime()+startTimeSlot));
					 * pstmt.setTimestamp(2, new
					 * Timestamp(dBean.getStartTime().getTime()+endTimeSlot));
					 * pstmt.setInt(3, retOrgId); logger.debug("SaleSlot Qry: "
					 * + pstmt); rs_2 = pstmt.executeQuery();
					 * 
					 * pstmt = connection.prepareStatement(saleRetQuery);
					 * pstmt.setTimestamp(1, new
					 * Timestamp(dBean.getStartTime().getTime()+startTimeSlot));
					 * pstmt.setTimestamp(2, new
					 * Timestamp(dBean.getStartTime().getTime()+endTimeSlot));
					 * pstmt.setInt(3, retOrgId);
					 * logger.debug("SaleReturnSlot Qry: " + pstmt); rs_3 =
					 * pstmt.executeQuery();
					 */

					// SimpleDateFormat timeFormat = new
					// SimpleDateFormat("HH:mm:ss");

					String startDate = dateFormat.format(dBean.getStartTime()
							.getTime());
					/*
					 * String startTime =
					 * timeFormat.format(dBean.getStartTime().getTime()); String
					 * endDate =
					 * dateFormat.format(dBean.getEndTime().getTime()); String
					 * endTime =
					 * timeFormat.format(dBean.getEndTime().getTime());
					 */
					String slotStartTime = slotTiming.split("_")[0];
					String slotEndTime = slotTiming.split("_")[1];

					slotPstmt.setInt(1, gameId);
					slotPstmt.setInt(2, gameId);
					slotPstmt.setTimestamp(3, dBean.getStartTime());
					slotPstmt.setTimestamp(4, dBean.getEndTime());
					slotPstmt.setInt(5, retOrgId);
					slotPstmt.setTimestamp(6, dBean.getStartTime());
					slotPstmt.setString(7, startDate + " " + slotStartTime);
					slotPstmt.setTimestamp(8, dBean.getEndTime());
					slotPstmt.setString(9, startDate + " " + slotEndTime);
					slotPstmt.setInt(10, retOrgId);

					logger.info("SaleSlot Qry: " + slotPstmt);
					rs_2 = slotPstmt.executeQuery();
				}

				salePstmt.setInt(1, gameId);
				salePstmt.setTimestamp(2, dBean.getStartTime());
				salePstmt.setTimestamp(3, dBean.getEndTime());
				salePstmt.setInt(4, retOrgId);
				logger.debug("Sale Qry: " + salePstmt);
				rs2 = salePstmt.executeQuery();

				saleReturnPstmt.setInt(1, gameId);
				saleReturnPstmt.setTimestamp(2, dBean.getStartTime());
				saleReturnPstmt.setTimestamp(3, dBean.getEndTime());
				saleReturnPstmt.setInt(4, retOrgId);
				logger.debug("SaleReturn Qry: " + saleReturnPstmt);
				rs3 = saleReturnPstmt.executeQuery();

				pwtPstmt.setInt(1, gameId);
				pwtPstmt.setTimestamp(2, dBean.getStartTime());
				pwtPstmt.setTimestamp(3, dBean.getEndTime());
				pwtPstmt.setInt(4, retOrgId);
				logger.debug("PWT Qry: " + pwtPstmt);
				rs4 = pwtPstmt.executeQuery();

				Map<String, Double> dgDataMap = new HashMap<String, Double>();
				dgDataMap.put("gameId", rs1.getDouble("game_id"));
				dgDataMap.put("name" + rs1.getString("game_name"), 0.0);

				if (rs_2 != null) {
					while (rs_2.next()) {
						dgDataMap.put("mrpSaleSlot", rs_2.getDouble("mrpAmt"));
						dgDataMap.put("netSaleSlot", rs_2.getDouble("netAmt"));
					}
				}
				while (rs2.next()) {
					if (rs3.next()) {
						dgDataMap.put("mrpSale", rs2.getDouble("mrpSale")
								- rs3.getDouble("mrpSaleRef"));
						dgDataMap.put("netSale", rs2.getDouble("netSale")
								- rs3.getDouble("netSaleRef"));
					} else {
						dgDataMap.put("mrpSale", rs2.getDouble("mrp_amt"));
						dgDataMap.put("netSale", rs2.getDouble("net_amt"));
					}
				}
				while (rs4.next()) {
					dgDataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
					dgDataMap.put("netPwt", rs4.getDouble("netPwt"));
				}
				dgDataList.add(dgDataMap);
				logger.debug("game Wise Report Map " + dgDataMap);
			}
			dataMap.put("DG", dgDataList);

			if (ServicesBean.isSLE()) {

				String sleActiveGamesQry = "select game_id, game_disp_name from st_sle_game_master where game_status = 'SALE_OPEN'";
				pstmt = connection.prepareStatement(sleActiveGamesQry);
				rs1 = pstmt.executeQuery();

				String sleSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String sleSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String slePwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm-govt_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				PreparedStatement sleSalePstmt = connection
						.prepareStatement(sleSaleQuery);
				PreparedStatement sleSaleReturnPstmt = connection
						.prepareStatement(sleSaleRefQuery);
				PreparedStatement slePwtPstmt = connection
						.prepareStatement(slePwtQuery);

				while (rs1.next()) {
					int gameId = rs1.getInt("game_id");

					sleSalePstmt = connection.prepareStatement(sleSaleQuery);
					sleSalePstmt.setInt(1, retOrgId);
					sleSalePstmt.setInt(2, gameId);
					sleSalePstmt.setTimestamp(3, dBean.getStartTime());
					sleSalePstmt.setTimestamp(4, dBean.getEndTime());
					rs2 = sleSalePstmt.executeQuery();

					sleSaleReturnPstmt = connection
							.prepareStatement(sleSaleRefQuery);
					sleSaleReturnPstmt.setInt(1, retOrgId);
					sleSaleReturnPstmt.setInt(2, gameId);
					sleSaleReturnPstmt.setTimestamp(3, dBean.getStartTime());
					sleSaleReturnPstmt.setTimestamp(4, dBean.getEndTime());
					rs3 = sleSaleReturnPstmt.executeQuery();

					slePwtPstmt = connection.prepareStatement(slePwtQuery);
					slePwtPstmt.setInt(1, retOrgId);
					slePwtPstmt.setInt(2, gameId);
					slePwtPstmt.setTimestamp(3, dBean.getStartTime());
					slePwtPstmt.setTimestamp(4, dBean.getEndTime());
					rs4 = slePwtPstmt.executeQuery();

					Map<String, Double> sleDataMap = new HashMap<String, Double>();

					sleDataMap.put("gameId", rs1.getDouble("game_id"));
					sleDataMap.put("name" + rs1.getString("game_disp_name"),
							0.0);

					if (rs2.next()) {
						if (rs3.next()) {
							sleDataMap.put("mrpSale", rs2.getDouble("mrpSale")
									- rs3.getDouble("mrpRef"));
							sleDataMap.put("netSale", rs2.getDouble("netSale")
									- rs3.getDouble("netRef"));
						} else {
							sleDataMap.put("mrpSale", rs3.getDouble("mrpSale"));
							sleDataMap.put("netSale", rs3.getDouble("netSale"));
						}
					}

					if (rs4.next()) {
						sleDataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
						sleDataMap.put("netPwt", rs4.getDouble("netPwt"));
					}
					logger.debug("SLE Report Map " + sleDataMap);
					sleDataList.add(sleDataMap);
				}

			}

			dataMap.put("SLE", sleDataList);
			if (ServicesBean.isIW()) {

				String iwActiveGamesQry = "select game_id, game_disp_name from st_iw_game_master where game_status = 'SALE_OPEN'";
				pstmt = connection.prepareStatement(iwActiveGamesQry);
				rs1 = pstmt.executeQuery();

				String iwSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale_refund where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_iw_ret_pwt where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				PreparedStatement iwSalePstmt = connection
						.prepareStatement(iwSaleQuery);
				PreparedStatement iwSaleReturnPstmt = connection
						.prepareStatement(iwSaleRefQuery);
				PreparedStatement iwPwtPstmt = connection
						.prepareStatement(iwPwtQuery);

				while (rs1.next()) {
					int gameId = rs1.getInt("game_id");

					iwSalePstmt = connection.prepareStatement(iwSaleQuery);
					iwSalePstmt.setInt(1, retOrgId);
					iwSalePstmt.setInt(2, gameId);
					iwSalePstmt.setTimestamp(3, dBean.getStartTime());
					iwSalePstmt.setTimestamp(4, dBean.getEndTime());
					rs2 = iwSalePstmt.executeQuery();

					iwSaleReturnPstmt = connection
							.prepareStatement(iwSaleRefQuery);
					iwSaleReturnPstmt.setInt(1, retOrgId);
					iwSaleReturnPstmt.setInt(2, gameId);
					iwSaleReturnPstmt.setTimestamp(3, dBean.getStartTime());
					iwSaleReturnPstmt.setTimestamp(4, dBean.getEndTime());
					rs3 = iwSaleReturnPstmt.executeQuery();

					iwPwtPstmt = connection.prepareStatement(iwPwtQuery);
					iwPwtPstmt.setInt(1, retOrgId);
					iwPwtPstmt.setInt(2, gameId);
					iwPwtPstmt.setTimestamp(3, dBean.getStartTime());
					iwPwtPstmt.setTimestamp(4, dBean.getEndTime());
					rs4 = iwPwtPstmt.executeQuery();

					Map<String, Double> iwDataMap = new HashMap<String, Double>();

					iwDataMap.put("gameId", rs1.getDouble("game_id"));
					iwDataMap.put("name" + rs1.getString("game_disp_name"),
							0.0);

					if (rs2.next()) {
						if (rs3.next()) {
							iwDataMap.put("mrpSale", rs2.getDouble("mrpSale")
									- rs3.getDouble("mrpRef"));
							iwDataMap.put("netSale", rs2.getDouble("netSale")
									- rs3.getDouble("netRef"));
						} else {
							iwDataMap.put("mrpSale", rs3.getDouble("mrpSale"));
							iwDataMap.put("netSale", rs3.getDouble("netSale"));
						}
					}

					if (rs4.next()) {
						iwDataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
						iwDataMap.put("netPwt", rs4.getDouble("netPwt"));
					}
					logger.debug("IW Report Map " + iwDataMap);
					iwDataList.add(iwDataMap);
				}

			}

			dataMap.put("IW", iwDataList);
			
			if (ServicesBean.isVS()) {
				String vsActiveGamesQry = "select game_id, game_disp_name from st_vs_game_master where game_status = 'SALE_OPEN'";
				pstmt = connection.prepareStatement(vsActiveGamesQry);
				rs1 = pstmt.executeQuery();

				String vsSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_vs_ret_sale where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String vsSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_vs_ret_sale_refund where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String vsPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_vs_ret_pwt where retailer_org_id = ? AND game_id=?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				PreparedStatement vsSalePstmt = connection
						.prepareStatement(vsSaleQuery);
				PreparedStatement vsSaleReturnPstmt = connection
						.prepareStatement(vsSaleRefQuery);
				PreparedStatement vsPwtPstmt = connection
						.prepareStatement(vsPwtQuery);

				while (rs1.next()) {
					int gameId = rs1.getInt("game_id");

					vsSalePstmt = connection.prepareStatement(vsSaleQuery);
					vsSalePstmt.setInt(1, retOrgId);
					vsSalePstmt.setInt(2, gameId);
					vsSalePstmt.setTimestamp(3, dBean.getStartTime());
					vsSalePstmt.setTimestamp(4, dBean.getEndTime());
					rs2 = vsSalePstmt.executeQuery();

					vsSaleReturnPstmt = connection.prepareStatement(vsSaleRefQuery);
					vsSaleReturnPstmt.setInt(1, retOrgId);
					vsSaleReturnPstmt.setInt(2, gameId);
					vsSaleReturnPstmt.setTimestamp(3, dBean.getStartTime());
					vsSaleReturnPstmt.setTimestamp(4, dBean.getEndTime());
					rs3 = vsSaleReturnPstmt.executeQuery();

					vsPwtPstmt = connection.prepareStatement(vsPwtQuery);
					vsPwtPstmt.setInt(1, retOrgId);
					vsPwtPstmt.setInt(2, gameId);
					vsPwtPstmt.setTimestamp(3, dBean.getStartTime());
					vsPwtPstmt.setTimestamp(4, dBean.getEndTime());
					rs4 = vsPwtPstmt.executeQuery();

					Map<String, Double> vsDataMap = new HashMap<String, Double>();

					vsDataMap.put("gameId", rs1.getDouble("game_id"));
					vsDataMap.put("name" + rs1.getString("game_disp_name"), 0.0);

					if (rs2.next()) {
						if (rs3.next()) {
							vsDataMap.put("mrpSale", rs2.getDouble("mrpSale") - rs3.getDouble("mrpRef"));
							vsDataMap.put("netSale", rs2.getDouble("netSale") - rs3.getDouble("netRef"));
						} else {
							vsDataMap.put("mrpSale", rs3.getDouble("mrpSale"));
							vsDataMap.put("netSale", rs3.getDouble("netSale"));
						}
					}

					if (rs4.next()) {
						vsDataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
						vsDataMap.put("netPwt", rs4.getDouble("netPwt"));
					}
					logger.debug("VS Report Map " + vsDataMap);
					vsDataList.add(vsDataMap);
				}
			}
			dataMap.put("VS", vsDataList);

			// Added code for addition of Scratch data in txn report...
			if (ServicesBean.isSE()) {
				String scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and srp.transaction_id = atm.transaction_id";
				PreparedStatement scratchPwtPstmt = connection
						.prepareStatement(scratchPwtQuery);
				scratchPwtPstmt.setInt(1, retOrgId);
				scratchPwtPstmt.setTimestamp(2, dBean.getStartTime());
				scratchPwtPstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("PWT Qry: " + scratchPwtQuery);
				rs5 = scratchPwtPstmt.executeQuery();

				Map<String, Double> scratchDataMap = new HashMap<String, Double>();

				if (rs5.next()) {
					scratchDataMap
							.put("scratchMrpPwt", rs5.getDouble("mrpPwt"));
					scratchDataMap.put("scratchNetPwt", rs5
							.getDouble("net_amt"));
				}
				logger.debug("Scratch Report Map " + scratchDataMap);
				seDataList.add(scratchDataMap);
			}
			dataMap.put("SE", seDataList);

			// Addition of Scratch data in txn report ends here...

			return dataMap;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return dataMap;
	}

	public List<Map<String, Double>> retDailyLedgerGameWise(DateBeans dBean,
			int retOrgId) {
		List<Map<String, Double>> dataList = new ArrayList<Map<String, Double>>();
		Connection connection = null;

		try {

			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null;

			String activeGamesQry = "select game_id, game_name from st_dg_game_master where game_status = 'OPEN'";
			pstmt = connection.prepareStatement(activeGamesQry);
			rs1 = pstmt.executeQuery();
			while (rs1.next()) {
				String saleQuery = "select ifnull(sum(mrp_amt),0)mrpSale, ifnull(sum(net_amt),0)netSale from st_dg_ret_sale_"
						+ rs1.getString("game_id")
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type in ('DG_SALE','DG_SALE_OFFLINE')) and retailer_org_id = ?";
				String saleRetQuery = "select ifnull(sum(mrp_amt),0)mrpSaleRef, ifnull(sum(net_amt),0)netSaleRef from st_dg_ret_sale_refund_"
						+ rs1.getString("game_id")
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')) and retailer_org_id = ?";
				String pwtQuery = "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_dg_ret_pwt_"
						+ rs1.getString("game_id")
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >=? and transaction_date<? and transaction_type in ('DG_PWT','DG_PWT_AUTO')) and retailer_org_id = ?";

				pstmt = connection.prepareStatement(saleQuery);
				pstmt.setTimestamp(1, dBean.getStartTime());
				pstmt.setTimestamp(2, dBean.getEndTime());
				pstmt.setInt(3, retOrgId);
				logger.debug("Sale Qry: " + pstmt);
				rs2 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(saleRetQuery);
				pstmt.setTimestamp(1, dBean.getStartTime());
				pstmt.setTimestamp(2, dBean.getEndTime());
				pstmt.setInt(3, retOrgId);
				logger.debug("SaleReturn Qry: " + pstmt);
				rs3 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(pwtQuery);
				pstmt.setTimestamp(1, dBean.getStartTime());
				pstmt.setTimestamp(2, dBean.getEndTime());
				pstmt.setInt(3, retOrgId);
				logger.debug("PWT Qry: " + pstmt);
				rs4 = pstmt.executeQuery();

				Map<String, Double> dataMap = new HashMap<String, Double>();
				dataMap.put("gameId", rs1.getDouble("game_id"));
				dataMap.put("name" + rs1.getString("game_name"), 0.0);
				while (rs2.next()) {
					if (rs3.next()) {
						dataMap.put("mrpSale", rs2.getDouble("mrpSale")
								- rs3.getDouble("mrpSaleRef"));
						dataMap.put("netSale", rs2.getDouble("netSale")
								- rs3.getDouble("netSaleRef"));
					} else {
						dataMap.put("mrpSale", rs2.getDouble("mrp_amt"));
						dataMap.put("netSale", rs2.getDouble("net_amt"));
					}
				}
				while (rs4.next()) {
					dataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
					dataMap.put("netPwt", rs4.getDouble("netPwt"));
				}
				logger.debug("game Wise Report Map " + dataMap);
				dataList.add(dataMap);
			}
			return dataList;
		} catch (SQLException e) {
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
		return dataList;
	}

	// Change this method as retDailyLedgerGameWiseTerminal if SLE data needs to
	// be added in Web reports...
	public List<Map<String, Double>> retDailyLedgerGameWiseWeb(DateBeans dBean,
			int retOrgId) throws Exception {
		List<Map<String, Double>> dataList = new ArrayList<Map<String, Double>>();
		Connection connection = null;

		try {
			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs1 = null, rs2 = null, rs3 = null, rs_2 = null, rs4 = null, rs5 = null;

			// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

			// String saleSlotTimeString =
			// Utility.getPropertyValue("SALE_SLOT_TIME");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String saleSlotTimeString = new TrainingExpAgentHelper()
					.getPropertyValue(connection, sdf.format(dBean
							.getStartTime().getTime()));
			String[] gameWiseTimingArr = saleSlotTimeString.split("~");
			Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
			for (String gameWiseTiming : gameWiseTimingArr) {
				int gameId = Integer.parseInt(gameWiseTiming.split("#")[0]);
				String timeString = gameWiseTiming.split("#")[1];
				if (!"00:00:00_00:00:00".equals(timeString)) {
					gameSlotTimingMap.put(gameId, timeString);
				}
			}

			// Thisis hard coded check becuase onw to twellve game is added in
			// LMS but WGRL does not wants to display it in retailer reports
			String activeGamesQry = "select game_id, game_name from st_dg_game_master where game_status = 'OPEN' and game_name_dev<>'OneToTwelve'";
			pstmt = connection.prepareStatement(activeGamesQry);
			rs1 = pstmt.executeQuery();

			String saleQuery = "SELECT IFNULL(SUM(mrp_amt),0) mrpSale, IFNULL(SUM(net_amt),0) netSale FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_SALE','DG_SALE_OFFLINE') AND rs.retailer_org_id=?;";
			String saleRetQuery = "SELECT IFNULL(SUM(mrp_amt),0) mrpSaleRef, IFNULL(SUM(net_amt),0) netSaleRef FROM st_dg_ret_sale_refund_? rsr INNER JOIN st_lms_retailer_transaction_master rtm ON rsr.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND rsr.retailer_org_id=?;";
			String pwtQuery = "SELECT IFNULL(SUM(pwt_amt),0)AS mrpPwt, IFNULL(SUM(pwt_amt+retailer_claim_comm),0)AS netPwt FROM st_dg_ret_pwt_? rp INNER JOIN st_lms_retailer_transaction_master rtm ON rp.transaction_id=rtm.transaction_id "
					+ "AND transaction_date>=? AND transaction_date<=? AND transaction_type IN ('DG_PWT','DG_PWT_AUTO') AND rp.retailer_org_id=?;";

			/*
			 * String saleSlotQuery =
			 * "SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
			 * +
			 * "WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date>=? AND transaction_date<=? AND transaction_date<=? AND rs.retailer_org_id=? "
			 * +
			 * "AND rs.transaction_id NOT IN (SELECT ref_transaction_id FROM st_dg_ret_sale_refund_? refund INNER JOIN  st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
			 * +
			 * "AND transaction_date>=? AND transaction_date<=?  and refund.retailer_org_id=?);"
			 * ;
			 */

			String saleSlotQuery = " SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt  FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id "
					+ " left join (SELECT ref_transaction_id FROM st_dg_ret_sale_refund_? refund INNER JOIN  st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND transaction_date>=? AND transaction_date<=? and refund.retailer_org_id=?)ref on ref.ref_transaction_id=rs.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date>=? AND transaction_date<=? AND transaction_date<=? AND rs.retailer_org_id=? AND ref.ref_transaction_id is null ";

			PreparedStatement slotPstmt = connection
					.prepareStatement(saleSlotQuery);
			PreparedStatement salePstmt = connection
					.prepareStatement(saleQuery);
			PreparedStatement saleReturnPstmt = connection
					.prepareStatement(saleRetQuery);
			PreparedStatement pwtPstmt = connection.prepareStatement(pwtQuery);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			while (rs1.next()) {
				int gameId = rs1.getInt("game_id");
				/*
				 * String saleQuery =
				 * "select ifnull(sum(mrp_amt),0)mrpSale, ifnull(sum(net_amt),0)netSale from st_dg_ret_sale_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type in ('DG_SALE','DG_SALE_OFFLINE')) and retailer_org_id = ?"
				 * ; String saleRetQuery =
				 * "select ifnull(sum(mrp_amt),0)mrpSaleRef, ifnull(sum(net_amt),0)netSaleRef from st_dg_ret_sale_refund_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')) and retailer_org_id = ?"
				 * ; String pwtQuery =
				 * "select ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt+retailer_claim_comm),0)as netPwt from st_dg_ret_pwt_"
				 * + rs1.getInt("game_id") +
				 * " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >=? and transaction_date<=? and transaction_type in ('DG_PWT','DG_PWT_AUTO')) and retailer_org_id = ?"
				 * ; String saleSlotQuery =
				 * "SELECT IFNULL(SUM(mrp_amt),0) mrpAmt, IFNULL(SUM(net_amt),0) netAmt FROM st_dg_ret_sale_"
				 * + rs1.getInt("game_id") +
				 * " WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>=? AND transaction_date<=?"
				 * +
				 * " AND retailer_org_id=? AND transaction_id NOT IN(select ref_transaction_id from st_dg_ret_sale_refund_"
				 * +rs1.getInt("game_id")+
				 * " refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>=?));"
				 * ;
				 */

				String slotTiming = gameSlotTimingMap.get(gameId);
				if (slotTiming != null) {
					// SimpleDateFormat simpleDateFormat = new
					// SimpleDateFormat("HH:mm:ss");
					// simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					// long startTimeSlot =
					// simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
					// long endTimeSlot =
					// simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

					/*
					 * pstmt = connection.prepareStatement(saleQuery);
					 * pstmt.setTimestamp(1, new
					 * Timestamp(dBean.getStartTime().getTime()+startTimeSlot));
					 * pstmt.setTimestamp(2, new
					 * Timestamp(dBean.getStartTime().getTime()+endTimeSlot));
					 * pstmt.setInt(3, retOrgId); logger.debug("SaleSlot Qry: "
					 * + pstmt); rs_2 = pstmt.executeQuery();
					 * 
					 * pstmt = connection.prepareStatement(saleRetQuery);
					 * pstmt.setTimestamp(1, new
					 * Timestamp(dBean.getStartTime().getTime()+startTimeSlot));
					 * pstmt.setTimestamp(2, new
					 * Timestamp(dBean.getStartTime().getTime()+endTimeSlot));
					 * pstmt.setInt(3, retOrgId);
					 * logger.debug("SaleReturnSlot Qry: " + pstmt); rs_3 =
					 * pstmt.executeQuery();
					 */

					// SimpleDateFormat timeFormat = new
					// SimpleDateFormat("HH:mm:ss");

					String startDate = dateFormat.format(dBean.getStartTime()
							.getTime());
					/*
					 * String startTime =
					 * timeFormat.format(dBean.getStartTime().getTime()); String
					 * endDate =
					 * dateFormat.format(dBean.getEndTime().getTime()); String
					 * endTime =
					 * timeFormat.format(dBean.getEndTime().getTime());
					 */
					String slotStartTime = slotTiming.split("_")[0];
					String slotEndTime = slotTiming.split("_")[1];

					slotPstmt.setInt(1, gameId);
					slotPstmt.setInt(2, gameId);
					slotPstmt.setTimestamp(3, dBean.getStartTime());
					slotPstmt.setTimestamp(4, dBean.getEndTime());
					slotPstmt.setInt(5, retOrgId);
					slotPstmt.setTimestamp(6, dBean.getStartTime());
					slotPstmt.setString(7, startDate + " " + slotStartTime);
					slotPstmt.setTimestamp(8, dBean.getEndTime());
					slotPstmt.setString(9, startDate + " " + slotEndTime);
					slotPstmt.setInt(10, retOrgId);

					logger.info("SaleSlot Qry: " + slotPstmt);
					rs_2 = slotPstmt.executeQuery();
				}

				salePstmt.setInt(1, gameId);
				salePstmt.setTimestamp(2, dBean.getStartTime());
				salePstmt.setTimestamp(3, dBean.getEndTime());
				salePstmt.setInt(4, retOrgId);
				logger.debug("Sale Qry: " + salePstmt);
				rs2 = salePstmt.executeQuery();

				saleReturnPstmt.setInt(1, gameId);
				saleReturnPstmt.setTimestamp(2, dBean.getStartTime());
				saleReturnPstmt.setTimestamp(3, dBean.getEndTime());
				saleReturnPstmt.setInt(4, retOrgId);
				logger.debug("SaleReturn Qry: " + saleReturnPstmt);
				rs3 = saleReturnPstmt.executeQuery();

				pwtPstmt.setInt(1, gameId);
				pwtPstmt.setTimestamp(2, dBean.getStartTime());
				pwtPstmt.setTimestamp(3, dBean.getEndTime());
				pwtPstmt.setInt(4, retOrgId);
				logger.debug("PWT Qry: " + pwtPstmt);
				rs4 = pwtPstmt.executeQuery();

				Map<String, Double> dataMap = new HashMap<String, Double>();
				dataMap.put("gameId", rs1.getDouble("game_id"));
				dataMap.put("name" + rs1.getString("game_name"), 0.0);

				if (rs_2 != null) {
					while (rs_2.next()) {
						dataMap.put("mrpSaleSlot", rs_2.getDouble("mrpAmt"));
						dataMap.put("netSaleSlot", rs_2.getDouble("netAmt"));
					}
				}
				while (rs2.next()) {
					if (rs3.next()) {
						dataMap.put("mrpSale", rs2.getDouble("mrpSale")
								- rs3.getDouble("mrpSaleRef"));
						dataMap.put("netSale", rs2.getDouble("netSale")
								- rs3.getDouble("netSaleRef"));
					} else {
						dataMap.put("mrpSale", rs2.getDouble("mrp_amt"));
						dataMap.put("netSale", rs2.getDouble("net_amt"));
					}
				}
				while (rs4.next()) {
					dataMap.put("mrpPwt", rs4.getDouble("mrpPwt"));
					dataMap.put("netPwt", rs4.getDouble("netPwt"));
				}

				logger.debug("game Wise Report Map " + dataMap);
				dataList.add(dataMap);
			}

			// Added code for addition of Scratch data in txn report...
			if (ServicesBean.isSE()) {
				String scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and srp.transaction_id = atm.transaction_id";
				PreparedStatement scratchPwtPstmt = connection
						.prepareStatement(scratchPwtQuery);
				scratchPwtPstmt.setInt(1, retOrgId);
				scratchPwtPstmt.setTimestamp(2, dBean.getStartTime());
				scratchPwtPstmt.setTimestamp(3, dBean.getEndTime());
				logger.debug("PWT Qry: " + scratchPwtQuery);
				rs5 = scratchPwtPstmt.executeQuery();

				Map<String, Double> scratchDataMap = new HashMap<String, Double>();

				if (rs5.next()) {
					scratchDataMap
							.put("scratchMrpPwt", rs5.getDouble("mrpPwt"));
					scratchDataMap.put("scratchNetPwt", rs5
							.getDouble("net_amt"));
				}
				logger.debug("Scratch Report Map " + scratchDataMap);
				dataList.add(scratchDataMap);
			}

			// Addition of Scratch data in txn report ends here...

			return dataList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return dataList;
	}

	public CollectionReportBean getSummaryTxnReport(DateBeans dBean, int orgId)
			throws Exception {
		Connection connection = null;
		CollectionReportBean localBean = new CollectionReportBean();

		try {

			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null;

			// List<Integer> gameNumList = Util.getGameNumberList();
			List<Integer> gameNumList = Util.getLMSGameNumberList();
			List<Integer> categoryList = CSProductRegistrationHelper
					.getActiveCategoriesList();
			StringBuilder drawSaleQueryBuilder = new StringBuilder();
			StringBuilder drawSaleRefQueryBuilder = new StringBuilder();
			StringBuilder drawPwtQueryBuilder = new StringBuilder();
			StringBuilder CSSaleQueryBuilder = new StringBuilder();
			StringBuilder CSSaleRefQueryBuilder = new StringBuilder();
			String drawSaleQuery = null;
			String drawSaleRefQuery = null;
			String drawPwtQuery = null;
			String scratchPurchaseQuery = null;
			String scratchSaleRetQuery = null;
			String scratchPwtQuery = null;
			String scratchSaleQuery = null;
			String CSSaleQuery = null;
			String CSSaleRefQuery = null;

			double olaDeposit = 0.0;
			double olaWithdrawal = 0.0;
			double netDeposit = 0.0;
			double netWdrwl = 0.0;

			double netSleSale = 0.0;
			double mrpSleSale = 0.0;
			double netSlePwt = 0.0;
			double mrpSlePwt = 0.0;
			
			double netIwSale = 0.0;
			double mrpIwSale = 0.0;
			double netIwPwt = 0.0;
			double mrpIwPwt = 0.0;

			for (int i = 0; i < gameNumList.size(); i++) {
				drawSaleQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
				drawSaleRefQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_refund_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
				drawPwtQueryBuilder
						.append("select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_dg_ret_pwt_"
								+ gameNumList.get(i)
								+ " where retailer_org_id = ? union ");
			}
			for (int i = 0; i < categoryList.size(); i++) {
				CSSaleQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_cs_sale_"
								+ categoryList.get(i)
								+ " where retailer_org_id = ? union ");
				CSSaleRefQueryBuilder
						.append("select transaction_id, mrp_amt, net_amt from st_cs_refund_"
								+ categoryList.get(i)
								+ " where retailer_org_id = ? union ");
			}
			drawSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
					+ drawSaleQueryBuilder.toString().substring(0,
							drawSaleQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where date(atm.transaction_date)>=? and date(atm.transaction_date)<? and uni.transaction_id = atm.transaction_id";
			drawSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0)as netRef from ("
					+ drawSaleRefQueryBuilder.toString().substring(0,
							drawSaleRefQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where date(atm.transaction_date)>=? and date(atm.transaction_date)<? and uni.transaction_id = atm.transaction_id";
			drawPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from ("
					+ drawPwtQueryBuilder.toString().substring(0,
							drawPwtQueryBuilder.lastIndexOf(" union "))
					+ ")uni, st_lms_retailer_transaction_master atm where date(atm.transaction_date)>=? and date(atm.transaction_date)<? and uni.transaction_id = atm.transaction_id";
			scratchPurchaseQuery = "select ifnull(sum(mrp_amt),0)as mrpPur, ifnull(sum(net_amt),0)as netPur from st_se_agent_retailer_transaction sart, st_lms_agent_transaction_master atm where sart.transaction_type='SALE' and sart.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and sart.transaction_id = atm.transaction_id";
			scratchSaleRetQuery = "select ifnull(sum(mrp_amt),0)as mrpSaleRet, ifnull(sum(net_amt),0)as netSaleRet from st_se_agent_retailer_transaction sart, st_lms_retailer_transaction_master atm where sart.transaction_type='SALE_RET' and sart.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and sart.transaction_id = atm.transaction_id";
			scratchPwtQuery = "select ifnull(sum(pwt_amt),0) as mrpPwt, ifnull(sum(pwt_amt*(1+claim_comm*0.01)),0)as  net_amt from st_se_retailer_pwt srp, st_lms_retailer_transaction_master atm where srp.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and srp.transaction_id = atm.transaction_id";
			scratchSaleQuery = "select ifnull(sum(cnt.ticket_count*unit.ticket_price),0)as soldTicketPrice from (select game_id, ifnull(sum(sold_tickets),0) as ticket_count from st_se_game_ticket_inv_history where current_owner='RETAILER' and current_owner_id = ? and date(date)=? group by game_id)cnt, (select game_id, ticket_price from st_se_game_master)unit where cnt.game_id = unit.game_id";
			if (CSSaleQueryBuilder.length() > 0) {
				CSSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.net_amt),0) as netSale from ("
						+ CSSaleQueryBuilder.toString().substring(0,
								CSSaleQueryBuilder.lastIndexOf(" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where date(atm.transaction_date)>=? and date(atm.transaction_date)<? and uni.transaction_id = atm.transaction_id and atm.transaction_type='CS_SALE'";
				CSSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.net_amt),0) as netRef from ("
						+ CSSaleRefQueryBuilder.toString().substring(0,
								CSSaleRefQueryBuilder.lastIndexOf(" union "))
						+ ")uni, st_lms_retailer_transaction_master atm where date(atm.transaction_date)>=? and date(atm.transaction_date)<? and uni.transaction_id = atm.transaction_id and (atm.transaction_type = 'CS_CANCEL_SERVER' or atm.transaction_type = 'CS_CANCEL_RET')";
			}
			if (ReportUtility.isDG) {
				pstmt = connection.prepareStatement(drawSaleQuery);
				int i = 0;
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setDate(i, dBean.getFirstdate());
				pstmt.setDate(i + 1, dBean.getLastdate());
				logger.debug("drawSaleQuery:- " + pstmt);
				rs1 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(drawSaleRefQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setDate(i, dBean.getFirstdate());
				pstmt.setDate(i + 1, dBean.getLastdate());
				logger.debug("drawSaleRefQuery:- " + pstmt);
				rs2 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(drawPwtQuery);
				for (i = 1; i <= gameNumList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setDate(i, dBean.getFirstdate());
				pstmt.setDate(i + 1, dBean.getLastdate());
				logger.debug("drawPwtQuery:- " + pstmt);
				rs3 = pstmt.executeQuery();
			}
			if (ReportUtility.isSE) {
				pstmt = connection.prepareStatement(scratchPurchaseQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("scratchPurchaseQuery:- " + pstmt);
				rs4 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchSaleRetQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("scratchSaleRetQuery:- " + pstmt);
				rs5 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("scratchPwtQuery:- " + pstmt);
				rs6 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(scratchSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				logger.debug("scratchSaleQuery:- " + pstmt);
				rs7 = pstmt.executeQuery();
			}

			if (ReportUtility.isCS) {
				pstmt = connection.prepareStatement(CSSaleQuery);
				int i = 0;
				for (i = 1; i <= categoryList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setDate(i, dBean.getFirstdate());
				pstmt.setDate(i + 1, dBean.getLastdate());
				logger.debug("CSSaleQuery:- " + pstmt);
				rs8 = pstmt.executeQuery();

				pstmt = connection.prepareStatement(CSSaleRefQuery);
				for (i = 1; i <= categoryList.size(); i++) {
					pstmt.setInt(i, orgId);
				}
				pstmt.setDate(i, dBean.getFirstdate());
				pstmt.setDate(i + 1, dBean.getLastdate());
				logger.debug("CSSaleRefQuery:- " + pstmt);
				rs9 = pstmt.executeQuery();
			}

			if (ReportUtility.isOLA) {

				DepositWthdrwReportControllerImpl controller = new DepositWthdrwReportControllerImpl();
				OlaReportBean totBean = controller.fetchTxnReportData(dBean
						.getFirstdate().toString(), dBean.getLastdate()
						.toString(), orgId);
				olaDeposit = totBean.getDepositAmt();
				olaWithdrawal = totBean.getWithdrawlAmt();
				netDeposit = totBean.getTotalDepositAmount();
				netWdrwl = totBean.getTotalWithdrawlAmount();
				localBean.setOlaDeposit(olaDeposit);
				localBean.setOlaWithdraw(olaWithdrawal);
			}

			if (ReportUtility.isSLE) {

				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				String sleSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String sleSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_sle_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String slePwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_sle_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(sleSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLESaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(sleSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(slePwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSlePwt = rs.getDouble("netPwt");
					mrpSlePwt = rs.getDouble("mrpPwt");
				}

				netSleSale = netSaleTemp - netRefTemp;
				mrpSleSale = mrpSaleTemp - mrpRefTemp;
			}
			
			if(ReportUtility.isIW){


				double netSaleTemp = 0.0;
				double netRefTemp = 0.0;
				double mrpSaleTemp = 0.0;
				double mrpRefTemp = 0.0;

				String iwSaleQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpSale, ifnull(sum(uni.retailer_net_amt),0) as netSale from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale where retailer_org_id = ? )uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwSaleRefQuery = "select ifnull(sum(uni.mrp_amt),0)as mrpRef, ifnull(sum(uni.retailer_net_amt),0)as netRef from (select transaction_id, mrp_amt, retailer_net_amt from st_iw_ret_sale_refund where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";
				String iwPwtQuery = "select ifnull(sum(uni.mrpPwt),0)as mrpPwt, ifnull(sum(uni.net_amt),0)as netPwt from (select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_iw_ret_pwt where retailer_org_id = ?)uni, st_lms_retailer_transaction_master atm where (atm.transaction_date)>=? and (atm.transaction_date)<=? and uni.transaction_id = atm.transaction_id";

				pstmt = connection.prepareStatement(iwSaleQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLESaleQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netSaleTemp = rs.getDouble("netSale");
					mrpSaleTemp = rs.getDouble("mrpSale");
				}

				pstmt = connection.prepareStatement(iwSaleRefQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netRefTemp = rs.getDouble("netRef");
					mrpRefTemp = rs.getDouble("mrpRef");
				}

				pstmt = connection.prepareStatement(iwPwtQuery);
				pstmt.setInt(1, orgId);
				pstmt.setDate(2, dBean.getFirstdate());
				pstmt.setDate(3, dBean.getLastdate());
				logger.debug("SLEPwtQuery - " + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					netIwPwt = rs.getDouble("netPwt");
					mrpIwPwt = rs.getDouble("mrpPwt");
				}

				netIwSale = netSaleTemp - netRefTemp;
				mrpIwSale = mrpSaleTemp - mrpRefTemp;
			
			}

			double netDrawSale = 0.0;
			double netScratchSale = 0.0;
			double mrpDrawSale = 0.0;
			double mrpScratchSale = 0.0;
			double netDrawPwt = 0.0;
			double netScratchPwt = 0.0;
			double mrpDrawPwt = 0.0;
			double mrpScratchPwt = 0.0;
			double mrpCSSale = 0.0;
			double netCSSale = 0.0;
			double mrpPurchase = 0.0;
			double netPurchase = 0.0;
			double cashCol = 0.0;
			double payToAgt = 0.0;

			if (rs1 != null && rs2 != null && rs3 != null) {
				rs1.next();
				rs2.next();
				rs3.next();
				netDrawSale = rs1.getDouble("netSale")
						- rs2.getDouble("netRef");
				mrpDrawSale = rs1.getDouble("mrpSale")
						- rs2.getDouble("mrpRef");
				netDrawPwt = rs3.getDouble("netPwt");
				mrpDrawPwt = rs3.getDouble("mrpPwt");
			}
			if (rs4 != null && rs5 != null && rs6 != null && rs7 != null) {
				rs4.next();
				rs5.next();
				rs6.next();
				rs7.next();
				netPurchase = rs4.getDouble("netPur")
						- rs5.getDouble("netSaleRet");
				mrpPurchase = rs4.getDouble("mrpPur")
						- rs5.getDouble("mrpSaleRet");
				netScratchPwt += rs6.getDouble("net_amt");
				mrpScratchPwt += rs6.getDouble("mrpPwt");
				mrpScratchSale = rs7.getDouble("soldTicketPrice");

			}
			if (rs8 != null && rs9 != null) {
				rs8.next();
				rs9.next();
				mrpCSSale = rs8.getDouble("mrpSale") - rs9.getDouble("mrpRef");
				netCSSale = rs8.getDouble("netSale") - rs9.getDouble("netRef");
			}

			cashCol = mrpDrawSale + mrpScratchSale + mrpCSSale - mrpDrawPwt
					- mrpScratchPwt + olaDeposit - olaWithdrawal + mrpSleSale
					- mrpSlePwt + mrpIwSale- mrpIwPwt;
			payToAgt = netDrawSale + netScratchSale + netCSSale - netDrawPwt
					- netScratchPwt + netDeposit - netWdrwl + netSleSale
					- netSlePwt + netIwSale - netIwPwt ;
			localBean.setGrandTotal(FormatNumber.formatNumber(cashCol) + "");
			localBean.setOpenBal(FormatNumber.formatNumber(payToAgt) + "");
			localBean.setDrawSale(mrpDrawSale + "");
			localBean.setScratchSale(mrpScratchSale + "");
			localBean.setCSSale(mrpCSSale + "");
			localBean.setDrawPwt(mrpDrawPwt + "");
			localBean.setScratchPwt(mrpScratchPwt + "");
			localBean.setSleSale(mrpSleSale + "");
			localBean.setSlePwt(mrpSlePwt + "");
			localBean.setIwSale(mrpIwSale + "");
			localBean.setIwPwt(mrpIwPwt + "");

		} catch (SQLException e) {
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
		return localBean;
	}

	public Map<String, List<String>> getDGDetailReport(DateBeans dBean,
			int orgId) {
		Map<String, List<String>> dgMap = new HashMap<String, List<String>>();
		DBConnect dbConnect = null;
		Connection connection = null;
		List<Integer> gameNbrList = Util.getGameNumberList();

		try {

			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			StringBuilder saleQueryBuilder = new StringBuilder();
			StringBuilder saleRefQueryBuilder = new StringBuilder();
			StringBuilder PwtQueryBuilder = new StringBuilder();
			String intSaleQuery = "";
			String leftSaleQuery = "";
			String rightSaleQuery = "";
			String pwtQuery = "";
			for (int i = 0; i < gameNbrList.size(); i++) {
				saleQueryBuilder
						.append("select transaction_id, game_id, mrp_amt, net_amt from st_dg_ret_sale_"
								+ gameNbrList.get(i)
								+ " where retailer_org_id = ? union ");
				saleRefQueryBuilder
						.append("select transaction_id, game_id, mrp_amt, net_amt from st_dg_ret_sale_refund_"
								+ gameNbrList.get(i)
								+ " where retailer_org_id = ? union ");
				PwtQueryBuilder
						.append("select transaction_id, game_id, pwt_amt as mrp_pwt,(pwt_amt+retailer_claim_comm)as  net_pwt from st_dg_ret_pwt_"
								+ gameNbrList.get(i)
								+ " where retailer_org_id = ? union ");
			}
			leftSaleQuery = "select ifnull(sale.game_id, saleRef.game_id) as gid, mrpSale, netSale, mrpSaleRef, netsaleRef from ((select ifnull(sum(mrp_amt),0) as mrpSale, ifnull(sum(net_amt),0) as netSale, game_id from ("
					+ saleQueryBuilder.substring(0, saleQueryBuilder
							.lastIndexOf(" union "))
					+ ")saletab where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >= ? and transaction_date < ? and transaction_type in('DG_SALE', 'DG_SALE_OFFLINE') and retailer_org_id = ?) group by game_id)sale left outer join (select ifnull(sum(mrp_amt),0) as mrpSaleRef, ifnull(sum(net_amt),0) as netSaleRef, game_id from ("
					+ saleRefQueryBuilder.substring(0, saleRefQueryBuilder
							.lastIndexOf(" union "))
					+ ")saleReftab where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >= ? and transaction_date < ? and transaction_type in ( 'DG_REFUND_CANCEL', 'DG_REFUND_FAILED') and retailer_org_id = ?) group by game_id)saleRef on sale.game_id = saleRef.game_id)";
			rightSaleQuery = "select ifnull(sale.game_id, saleRef.game_id) as gid, mrpSale, netSale, mrpSaleRef, netsaleRef from ((select ifnull(sum(mrp_amt),0) as mrpSale, ifnull(sum(net_amt),0) as netSale, game_id from ("
					+ saleQueryBuilder.substring(0, saleQueryBuilder
							.lastIndexOf(" union "))
					+ ")saletab where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >= ? and transaction_date < ? and transaction_type in('DG_SALE', 'DG_SALE_OFFLINE') and retailer_org_id = ?) group by game_id)sale right outer join (select ifnull(sum(mrp_amt),0) as mrpSaleRef, ifnull(sum(net_amt),0) as netSaleRef, game_id from ("
					+ saleRefQueryBuilder.substring(0, saleRefQueryBuilder
							.lastIndexOf(" union "))
					+ ")saleReftab where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >= ? and transaction_date < ? and transaction_type in ( 'DG_REFUND_CANCEL', 'DG_REFUND_FAILED') and retailer_org_id = ?) group by game_id)saleRef on sale.game_id = saleRef.game_id)";
			intSaleQuery = "select game_name, mrpSale, netSale, mrpSaleRef, netSaleRef from ("
					+ leftSaleQuery
					+ " union "
					+ rightSaleQuery
					+ ")full, st_dg_game_master dgm where full.gid = dgm.game_id";
			pwtQuery = "select game_id, ifnull(sum(mrp_pwt),0) mrpPwt, ifnull(sum(net_pwt),0)netPwt from ("
					+ PwtQueryBuilder.substring(0, PwtQueryBuilder
							.lastIndexOf(" union "))
					+ ") pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date >= ? and transaction_date < ? and transaction_type = 'DG_PWT' and retailer_org_id  = ?) group by pwt.game_id";

			pstmt = connection.prepareStatement(intSaleQuery);
			int i = 0, j = 0;
			while (i < gameNbrList.size()) {
				pstmt.setInt(++i, orgId);
			}
			pstmt.setDate(++i, dBean.getFirstdate());
			pstmt.setDate(++i, dBean.getLastdate());
			pstmt.setInt(++i, orgId);
			while (j < gameNbrList.size()) {
				pstmt.setInt(++i, orgId);
				j++;
			}
			pstmt.setDate(++i, dBean.getFirstdate());
			pstmt.setDate(++i, dBean.getLastdate());
			pstmt.setInt(++i, orgId);
			j = 0;
			while (j < gameNbrList.size()) {
				pstmt.setInt(++i, orgId);
				j++;
			}
			pstmt.setDate(++i, dBean.getFirstdate());
			pstmt.setDate(++i, dBean.getLastdate());
			pstmt.setInt(++i, orgId);
			j = 0;
			while (j < gameNbrList.size()) {
				pstmt.setInt(++i, orgId);
				j++;
			}
			pstmt.setDate(++i, dBean.getFirstdate());
			pstmt.setDate(++i, dBean.getLastdate());
			pstmt.setInt(++i, orgId);
			logger.debug("query for draw sale for detail txn report " + pstmt);
			ResultSet rs1 = pstmt.executeQuery();

			pstmt = connection.prepareStatement(pwtQuery);
			i = 0;
			while (i < gameNbrList.size()) {
				pstmt.setInt(++i, orgId);
			}
			pstmt.setDate(++i, dBean.getFirstdate());
			pstmt.setDate(++i, dBean.getLastdate());
			pstmt.setInt(++i, orgId);
			logger.debug("query for draw pwt for detail txn report " + pstmt);
			ResultSet rs2 = pstmt.executeQuery();

			List<String> gameAmtList = null;
			while (rs1.next()) {
				gameAmtList = new ArrayList<String>();
				gameAmtList.add(rs1.getString("game_name"));
				gameAmtList.add(FormatNumber.formatNumber(rs1
						.getDouble("mrpSale")
						- rs1.getDouble("mrpSaleRef")));
				gameAmtList.add(FormatNumber.formatNumber(rs1
						.getDouble("netSale")
						- rs1.getDouble("netSaleRef")));
			}
			dgMap.put("sale", gameAmtList);
			while (rs2.next()) {
				gameAmtList = new ArrayList<String>();
				gameAmtList.add(rs2.getString("game_name"));
				gameAmtList.add(rs2.getDouble("mrpPwt") + "");
				gameAmtList.add(rs2.getDouble("netPwt") + "");
			}
			dgMap.put("pwt", gameAmtList);
		} catch (SQLException e) {
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
		return dgMap;
	}

	public Map<String, List<String>> getSEDetailReport(DateBeans dBean,
			int orgId) {
		Map<String, List<String>> seMap = new HashMap<String, List<String>>();
		DBConnect dbConnect = null;
		Connection connection = null;
		List<Integer> gameNbrList = null;

		try {

			connection = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			pstmt = connection
					.prepareStatement("select game_nbr from st_se_game_master where game_status != 'CLOSE' and game_status != 'TERMINATE'");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				gameNbrList.add(rs.getInt("game_nbr"));
			}

			String scratchSaleQuery = "select sgm.game_id, sgm.game_name, (ticket_price*ifnull(sum(sold_tickets),0)) as ticketSale from st_se_game_master sgm, st_se_game_ticket_inv_history gtih where sgm.game_id = gtih.game_id and gtih.current_owner = 'RETAILER' and date >= ? and date < ? and current_owner_id = ? group by gtih.game_id";
			String scratchPwtQuery = "";
		} catch (SQLException e) {
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
		return seMap;
	}
}
