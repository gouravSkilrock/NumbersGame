package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaRetailerReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class RetailerDetailedCollectionReportHelper {
	private Timestamp newStartDate=null;
	private Timestamp newEndDate=null;
	Log logger = LogFactory.getLog(RetailerDetailedCollectionReportHelper.class.getName());

	public Map<String, CollectionReportOverAllBean> getRetailerDetailedCollection(Timestamp deployDate, Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws Exception {
		Map<String, CollectionReportOverAllBean> retailerMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean reportBean = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String retOrgQry = null;
		String terminateOrgQry = null;
		String clXclQuery = null;
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				connection = DBConnect.getConnection();
			else
				connection = DBConnectReplica.getConnection();

			retOrgQry = "SELECT SQL_CACHE  concat(a.org_code,'_',a.name)  orgCode  , a.organization_id, b.name parent_name FROM st_lms_organization_master a inner join st_lms_organization_master b on a.parent_id = b.organization_id  WHERE a.organization_type='RETAILER' and a.organization_status <> 'TERMINATE' ORDER BY orgCode ASC ;";
			logger.info("Get All Retailer Query - "+retOrgQry);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(retOrgQry);
			while (rs.next()) {
				reportBean = new CollectionReportOverAllBean();
				reportBean.setAgentName(rs.getString("orgCode"));
				reportBean.setParentName(rs.getString("parent_name"));
				retailerMap.put(rs.getString("organization_id"), reportBean);
			}
			
			terminateOrgQry = "SELECT organization_id FROM st_lms_user_master WHERE (termination_date<'"+startDate+"' OR registration_date>'"+endDate+"') AND organization_type='RETAILER';";
			logger.info("Get Terminated Retailer Query - "+terminateOrgQry);
			rs = stmt.executeQuery(terminateOrgQry);
			while (rs.next()) {
				retailerMap.remove(rs.getString("organization_id"));
			}

			boolean isDataFromArch = ReportUtility.checkDateLessThanArchiveDate(new Timestamp(startDate.getTime()-1000), connection);
			Date lastArchDate = null;
			if(isDataFromArch) {
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(startDate.getTime()-1000);
				cal1.add(Calendar.DAY_OF_MONTH, 1);
				ReportUtility.clearTimeFromDate(cal1);

				rs = stmt.executeQuery("SELECT organization_id, opening_bal_cl_inc FROM st_rep_org_bal_history WHERE finaldate='"+(new Date(cal1.getTimeInMillis()))+"' AND organization_type='RETAILER';");
				while(rs.next()){
					if(retailerMap.get(rs.getString("organization_id")) != null) {
						reportBean = retailerMap.get(rs.getString("organization_id"));
						reportBean.setOpeningBal(-rs.getDouble("opening_bal_cl_inc"));
					}
				}

				collectionRetailerWise(startDate, endDate, connection, retailerMap);

				clXclQuery = "SELECT organization_id, IFNULL(SUM(amount),0.0) AS amount FROM (SELECT organization_id, amount FROM st_lms_cl_xcl_update_history WHERE date_time>='"+startDate+"' AND date_time<'"+endDate+"' UNION ALL SELECT organization_id, (cl+xcl) clXcl FROM st_rep_org_bal_history WHERE finaldate>='"+startDate+"' AND finaldate<='"+endDate+"' AND organization_type='RETAILER')aa GROUP BY organization_id;";
				logger.info("clXcl Query - "+clXclQuery);
				rs = stmt.executeQuery(clXclQuery);
				while(rs.next()) {
					reportBean = retailerMap.get(rs.getString("organization_id"));
					if(reportBean != null) {
						reportBean.setClLimit(rs.getDouble("amount"));
					}
				}

				return retailerMap;
			} else {
				lastArchDate = ReportUtility.getLastArchDateInDateFormat(connection);
				rs = stmt.executeQuery("SELECT organization_id, (opening_bal_cl_inc+net_amount_transaction) opening_bal FROM st_rep_org_bal_history WHERE finaldate='"+lastArchDate+"' AND organization_type='RETAILER';");
				while(rs.next()) {
					if(retailerMap.get(rs.getString("organization_id")) != null) {
						retailerMap.get(rs.getString("organization_id")).setOpeningBal(-rs.getDouble("opening_bal"));
					}
				}
			}
			
			collectionRetailerWise(new Timestamp(lastArchDate.getTime()), startDate, connection, retailerMap);
			
			clXclQuery = "SELECT organization_id, IFNULL(SUM(amount),0.0) AS amount FROM st_lms_cl_xcl_update_history WHERE date_time>='"+new Timestamp(lastArchDate.getTime())+"' AND date_time<'"+startDate+"' GROUP BY organization_id;";
			logger.info("clXcl Query - "+clXclQuery);
			rs = stmt.executeQuery(clXclQuery);
			while(rs.next()) {
				if(retailerMap.get(rs.getString("organization_id")) != null)
					retailerMap.get(rs.getString("organization_id")).setClLimit(rs.getDouble("amount"));
			}

			double openingBal = 0.0;
			for(String retOrgId : retailerMap.keySet()) {
				reportBean = retailerMap.get(retOrgId);
				openingBal = reportBean.getOpeningBal() - reportBean.getClLimit() + reportBean.getDgSale() - reportBean.getDgCancel() - reportBean.getDgPwt()
							+ reportBean.getSeSale() - reportBean.getSePwt()
							+ reportBean.getCSSale() - reportBean.getCSCancel()
							+ reportBean.getDeposit() - reportBean.getWithdrawal()
							+ reportBean.getSleSale() - reportBean.getSleCancel() - reportBean.getSlePwt()
							- (reportBean.getCash() + reportBean.getCheque() + reportBean.getCredit() - reportBean.getDebit() - reportBean.getChequeReturn() + reportBean.getBankDep());
				reportBean.setOpeningBal(openingBal);
				reportBean.setClLimit(0.0);
				reportBean.setDgSale(0.0);
				reportBean.setDgCancel(0.0);
				reportBean.setDgPwt(0.0);
				reportBean.setSeSale(0.0);
				reportBean.setSePwt(0.0);
				reportBean.setCSSale(0.0);
				reportBean.setCSCancel(0.0);
				reportBean.setDeposit(0.0);
				reportBean.setWithdrawal(0.0);
				reportBean.setSleSale(0.0);
				reportBean.setSleCancel(0.0);
				reportBean.setSlePwt(0.0);
				reportBean.setCash(0.0);
				reportBean.setCheque(0.0);
				reportBean.setChequeReturn(0.0);
				reportBean.setCredit(0.0);
				reportBean.setDebit(0.0);
				reportBean.setBankDep(0.0);
			}
			collectionRetailerWise(startDate, endDate, connection, retailerMap);
			
			clXclQuery = "SELECT organization_id, IFNULL(SUM(amount),0.0) AS amount FROM st_lms_cl_xcl_update_history WHERE date_time>='"+startDate+"' AND date_time<'"+endDate+"' GROUP BY organization_id;";
			logger.info("clXcl Query - "+clXclQuery);
			rs = stmt.executeQuery(clXclQuery);
			while(rs.next()) {
				if(retailerMap.get(rs.getString("organization_id")) != null)
					retailerMap.get(rs.getString("organization_id")).setClLimit(rs.getDouble("amount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in Report getRetailerDetailedCollection");
		} finally {
			DBConnect.closeCon(connection);
		}
		return retailerMap;
	}

	public void collectionRetailerWise(Timestamp startDate, Timestamp endDate, Connection connection, Map<String, CollectionReportOverAllBean> retailerMap) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String cashQry = "SELECT retailer_org_id, amount cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit, 0.0 bank FROM st_lms_agent_cash_transaction cash INNER JOIN st_lms_agent_transaction_master atm ON cash.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"'";
			String chqQry = "SELECT retailer_org_id, 0.0 cash, cheque_amt cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit, 0.0 bank FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master atm ON chq.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND chq.transaction_type IN ('CHEQUE','CLOSED')";
			String chqRetQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, cheque_amt cheque_ret, 0.0 debit, 0.0 credit, 0.0 bank FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master atm ON chq.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND chq.transaction_type IN ('CHQ_BOUNCE')";
			String debitQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, 0.0 cheque_ret, amount debit, 0.0 credit, 0.0 bank FROM st_lms_agent_debit_note dbt INNER JOIN st_lms_agent_transaction_master atm ON dbt.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND dbt.transaction_type IN ('DR_NOTE_CASH','DR_NOTE')";
			String creditQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, amount credit, 0.0 bank FROM st_lms_agent_credit_note crd INNER JOIN st_lms_agent_transaction_master atm ON crd.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND crd.transaction_type IN ('CR_NOTE_CASH','CR_NOTE')";
			String bankQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit, amount bank FROM st_lms_agent_bank_deposit_transaction bd INNER JOIN st_lms_agent_transaction_master atm ON bd.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"'";
			String repQry = "SELECT retailer_org_id, cash_amt, cheque_amt, cheque_bounce_amt, debit_note, credit_note, bank_deposit FROM st_rep_agent_payments WHERE finaldate>=DATE('"+startDate+"') AND finaldate<=DATE('"+endDate+"')";
			String accQry = "SELECT retailer_org_id, IFNULL(SUM(cash),0.0) cash, IFNULL(SUM(cheque),0.0) cheque, IFNULL(SUM(cheque_ret),0.0) cheque_ret, IFNULL(SUM(debit),0.0) debit, IFNULL(SUM(credit),0.0) credit, IFNULL(SUM(bank),0.0) bank FROM ("
					+ cashQry
					+ " UNION ALL "
					+ chqQry
					+ " UNION ALL "
					+ chqRetQry
					+ " UNION ALL "
					+ debitQry
					+ " UNION ALL "
					+ creditQry
					+ " UNION ALL "
					+ bankQry
					+ " UNION ALL "
					+ repQry
					+")main GROUP BY retailer_org_id;";
			logger.info("Account Detail Query - " + accQry);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(accQry);
			while (rs.next()) {
				String agtOrgId = rs.getString("retailer_org_id");
				if(retailerMap.get(agtOrgId) != null) {
					retailerMap.get(agtOrgId).setCash(rs.getDouble("cash"));
					retailerMap.get(agtOrgId).setCheque(rs.getDouble("cheque"));
					retailerMap.get(agtOrgId).setChequeReturn(rs.getDouble("cheque_ret"));
					retailerMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
					retailerMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
					retailerMap.get(agtOrgId).setBankDep(rs.getDouble("bank"));
				}
			}

			if (ReportUtility.isDG) {
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				StringBuilder saleQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_sale, IFNULL(SUM(net_amt),0.0) net_sale FROM (");
				StringBuilder cancelQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_cancel, IFNULL(SUM(net_amt),0.0) net_cancel FROM (");
				StringBuilder pwtQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(pwt_amt),0.0) net_pwt FROM (");

				ResultSet gameRs = stmt.executeQuery(gameQry);
				while (gameRs.next()) {
					int gameId = gameRs.getInt("game_id");
					saleQry.append("SELECT rs.retailer_org_id, mrp_amt, net_amt FROM st_dg_ret_sale_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, sale_mrp mrp_amt, sale_net net_amt FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
					cancelQry.append("SELECT rs.retailer_org_id, mrp_amt, net_amt FROM st_dg_ret_sale_refund_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, ref_sale_mrp mrp_cancel, ref_net_amt net_cancel FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
					pwtQry.append("SELECT rs.retailer_org_id, (pwt_amt + ifnull(retailer_claim_comm, 0.00) - ifnull(govt_claim_comm, 0.00)) pwt_amt FROM st_dg_ret_pwt_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, pwt_net_amt net_pwt FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
				}

				saleQry.delete(saleQry.lastIndexOf(" UNION ALL "), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf(" UNION ALL "), cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf(" UNION ALL "), pwtQry.length());

				saleQry.append(")main GROUP BY retailer_org_id;");
				cancelQry.append(")main GROUP BY retailer_org_id;");
				pwtQry.append(")main GROUP BY retailer_org_id;");

				// Draw Sale Query
				logger.info("Draw Sale Query - " + saleQry.toString());
				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgSale(rs.getDouble("net_sale"));
				}

				// Draw Cancel Query
				logger.info("Draw Cancel Query - " + cancelQry.toString());
				rs = stmt.executeQuery(cancelQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgCancel(rs.getDouble("net_cancel"));
				}
				// Draw Pwt Query
				logger.info("Draw Pwt Query - " + pwtQry.toString());
				rs = stmt.executeQuery(pwtQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgPwt(rs.getDouble("net_pwt"));
				}
			}

			if (ReportUtility.isSE) {
				String saleQry = null, purchaseQuery=null;
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "SELECT organization_id, IFNULL(SUM(mrp_amt),0.00) mrp_amt, IFNULL(SUM(net_amt),0.00) net_amt FROM (SELECT lbt.retailer_org_id organization_id, lbt.mrp_amt, lbt.net_amt FROM st_se_agent_ret_loose_book_transaction lbt INNER JOIN st_se_agent_retailer_transaction trans ON lbt.transaction_id=trans.transaction_id INNER JOIN st_lms_agent_transaction_master atm ON atm.transaction_id=trans.transaction_id AND atm.transaction_type='LOOSE_SALE' AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' UNION ALL SELECT lbt.retailer_org_id organization_id, -lbt.mrp_amt, -lbt.net_amt FROM st_se_agent_ret_loose_book_transaction lbt INNER JOIN st_se_agent_retailer_transaction trans ON lbt.transaction_id=trans.transaction_id INNER JOIN st_lms_agent_transaction_master atm ON atm.transaction_id=trans.transaction_id AND atm.transaction_type='LOOSE_SALE_RET' AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"')main GROUP BY organization_id;";
				} else if ("TICKET_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "SELECT organization_id, IFNULL(mrpAmt,0.0) mrp_amt, IFNULL(netAmt,0.0) net_amt FROM st_lms_organization_master LEFT OUTER JOIN (SELECT current_owner_id,SUM(soldTkt*ticket_price) mrpAmt,SUM((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt FROM st_se_game_master gm, st_se_game_inv_detail gid,(SELECT game_id,book_nbr,SUM(sold_tickets) soldTkt FROM st_se_game_ticket_inv_history WHERE DATE>='"+startDate+"' AND DATE<='"+endDate+"' AND current_owner='RETAILER' GROUP BY book_nbr) TktTlb WHERE gm.game_id=TktTlb.game_id AND TktTlb.book_nbr=gid.book_nbr AND gid.current_owner='RETAILER' GROUP BY current_owner_id) saleTlb ON organization_id=current_owner_id WHERE organization_type='RETAILER';";
				}
				
				logger.info("Scratch Sale Query - "+saleQry);
				rs = stmt.executeQuery(saleQry);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("organization_id")) != null)
						retailerMap.get(rs.getString("organization_id")).setSeSale(rs.getDouble("net_amt"));
				}
				
				if ("NO".equals(Utility.getPropertyValue("SE_LAST_SOLD_TKT_ENTRY"))) {
					purchaseQuery = "select retailer_org_id as organization_id, ifnull(mrp_amt,0)as mrpPur, ifnull(net_amt,0)as netPur from st_se_agent_retailer_transaction sart, st_lms_agent_transaction_master atm where sart.transaction_type='SALE' and (atm.transaction_date)>='"+startDate+"' and (atm.transaction_date)<='"+endDate+"' and sart.transaction_id = atm.transaction_id group by retailer_org_id";								
					logger.info("Scratch Purchase Query - "+purchaseQuery);
					rs = stmt.executeQuery(purchaseQuery);
					while (rs.next()) {
						if(retailerMap.get(rs.getString("organization_id")) != null)
							retailerMap.get(rs.getString("organization_id")).setSeSale(retailerMap.get(rs.getString("organization_id")).getSeSale()+rs.getDouble("netPur"));
					}
				}
				// Scratch Pwt Query
				String pwtQuery = "SELECT retailer_org_id, IFNULL(SUM(pwt),0.0) pwt_amt FROM (SELECT retailer_org_id, SUM(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt FROM st_se_retailer_pwt WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND transaction_type='PWT' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER')) GROUP BY retailer_org_id UNION ALL SELECT retailer_org_id,SUM(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt FROM st_se_agent_pwt WHERE STATUS!='DONE_UNCLM' AND transaction_id IN (SELECT transaction_id FROM st_lms_agent_transaction_master WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+startDate+"' AND transaction_type='PWT' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER')) GROUP BY retailer_org_id)main GROUP BY retailer_org_id;";
				logger.info("Scratch Pwt Query - "+pwtQuery);
				rs = stmt.executeQuery(pwtQuery);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setSePwt(rs.getDouble("pwt_amt"));
				}
			}

			if (ReportUtility.isCS) {
				String catQry = "SELECT category_id FROM st_cs_product_category_master WHERE status='ACTIVE';";
				StringBuilder saleQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_sale, IFNULL(SUM(net_amt),0.0) net_sale FROM (");
				StringBuilder cancelQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_cancel, IFNULL(SUM(net_amt),0.0) net_cancel FROM (");

				ResultSet catRs = stmt.executeQuery(catQry);
				while (catRs.next()) {
					int catId = catRs.getInt("category_id");
					saleQry.append("SELECT cs.retailer_org_id, mrp_amt, net_amt FROM st_cs_sale_").append(catId).append(" cs INNER JOIN st_lms_retailer_transaction_master rtm ON rtm.transaction_id=cs.transaction_id WHERE transaction_type IN('CS_SALE') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL ");
					cancelQry.append("SELECT cs.retailer_org_id, mrp_amt, net_amt FROM st_cs_refund_").append(catId).append(" cs INNER JOIN st_lms_retailer_transaction_master rtm ON rtm.transaction_id=cs.transaction_id WHERE transaction_type IN('CS_CANCEL_SERVER','CS_CANCEL_RET') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL ");
				}
				saleQry.delete(saleQry.lastIndexOf(" UNION ALL "), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf(" UNION ALL "), cancelQry.length());

				saleQry.append(")main GROUP BY retailer_org_id;");
				cancelQry.append(")main GROUP BY retailer_org_id;");

				// CS Sale Query
				logger.info("CS Sale Query - " + saleQry.toString());
				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setCSSale(rs.getDouble("net_sale"));
				}

				// CS Cancel Query
				logger.info("CS Cancel Query - " + cancelQry.toString());
				rs = stmt.executeQuery(cancelQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setCSCancel(rs.getDouble("net_cancel"));
				}
			}

			if(ReportUtility.isOLA){
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				requestBean.setFromDate(dateFormat.format(startDate));
				requestBean.setToDate(dateFormat.format(endDate));
				Map<Integer, OlaOrgReportResponseBean> olaMap = OlaRetailerReportControllerImpl.fetchDepositWithdrawlMultipleRetailer(requestBean, connection);
				for(Integer retOrgId : olaMap.keySet()) {
					if(retailerMap.get(String.valueOf(retOrgId)) != null) {
						retailerMap.get(String.valueOf(retOrgId)).setDeposit(olaMap.get(retOrgId).getNetDepositAmt());
						retailerMap.get(String.valueOf(retOrgId)).setWithdrawal(olaMap.get(retOrgId).getNetWithdrawalAmt());
					}
				}
			}

			if (ReportUtility.isSLE) {
				// SLE Sale Query
				String saleQuery = "SELECT retailer_org_id, SUM(mrp_sale) mrp_sale, SUM(net_sale) net_sale FROM (SELECT retailer_org_id, mrp_amt mrp_sale, retailer_net_amt net_sale FROM st_sle_ret_sale WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' UNION ALL SELECT organization_id, sale_mrp, sale_net FROM st_rep_sle_retailer WHERE finaldate>=DATE('"+startDate+"') AND finaldate<=DATE('"+endDate+"'))aa GROUP BY retailer_org_id;";
				logger.info("SLE Sale Query - " + saleQuery);
				rs = stmt.executeQuery(saleQuery);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setSleSale(rs.getDouble("net_sale"));
				}

				// SLE Cancel Query
				String cancelQuery = "SELECT retailer_org_id, SUM(mrp_cancel) mrp_cancel, SUM(net_cancel) net_cancel FROM (SELECT retailer_org_id, mrp_amt mrp_cancel, retailer_net_amt net_cancel FROM st_sle_ret_sale_refund WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' UNION ALL SELECT organization_id, ref_sale_mrp, ref_net_amt FROM st_rep_sle_retailer WHERE finaldate>=DATE('"+startDate+"') AND finaldate<=DATE('"+endDate+"'))aa GROUP BY retailer_org_id;";
				logger.info("SLE Cancel Query - " + cancelQuery);
				rs = stmt.executeQuery(cancelQuery);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setSleCancel(rs.getDouble("net_cancel"));
				}
				// SLE Pwt Query
				String pwtQuery = "SELECT retailer_org_id, SUM(net_pwt) net_pwt FROM (SELECT retailer_org_id, (pwt_amt+retailer_claim_comm-govt_claim_comm) net_pwt FROM st_sle_ret_pwt WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' UNION ALL SELECT organization_id, pwt_net_amt FROM st_rep_sle_retailer WHERE finaldate>=DATE('"+startDate+"') AND finaldate<=DATE('"+endDate+"'))aa GROUP BY retailer_org_id;";
				logger.info("SLE Pwt Query - " + pwtQuery);
				rs = stmt.executeQuery(pwtQuery);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setSlePwt(rs.getDouble("net_pwt"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in Retailer Detailed Collection Report");
		}
	}
	//for all retailer coresponding to a single agent
	public Map<String, CollectionReportOverAllBean> getRetailerWiseClosingBalance(Timestamp deployDate, Timestamp startDate, Timestamp endDate,int agentOrgId) throws Exception {
		Map<String, CollectionReportOverAllBean> retailerMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean reportBean = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String retOrgQry = null;
		String terminateOrgQry = null;
		//String clXclQuery = null;
		try {
			connection = DBConnect.getConnection();
			retOrgQry = "SELECT SQL_CACHE "+QueryManager.getOrgCodeQuery()+", organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER' AND organization_status<>'TERMINATE'   and parent_id='"+agentOrgId+"' ORDER BY "+QueryManager.getAppendOrgOrder()+";";
			logger.info("Get All Retailer Query - "+retOrgQry);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(retOrgQry);
			while (rs.next()) {
				reportBean = new CollectionReportOverAllBean();
				reportBean.setAgentName(rs.getString("orgCode"));
				retailerMap.put(rs.getString("organization_id"), reportBean);
			}
	
			terminateOrgQry = "SELECT organization_id FROM st_lms_user_master WHERE (termination_date<'"+startDate+"' OR registration_date>'"+endDate+"') AND organization_type='RETAILER' AND parent_user_id='"+agentOrgId+"';";
			logger.info("Get Terminated Retailer Query - "+terminateOrgQry);
			rs = stmt.executeQuery(terminateOrgQry);
			while (rs.next()) {
				retailerMap.remove(rs.getString("organization_id"));
			}

			boolean isDataFromArch = ReportUtility.checkDateLessThanLastRunDate(new Timestamp(startDate.getTime()-1000), agentOrgId, "RETAILER", connection);
			Date lastArchDate = null;
			if(isDataFromArch) {
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(startDate.getTime()-1000);
				cal1.add(Calendar.DAY_OF_MONTH, 1);
				ReportUtility.clearTimeFromDate(cal1);

				rs = stmt.executeQuery("SELECT organization_id, opening_bal_cl_inc FROM st_rep_org_bal_history WHERE finaldate='"+(new Date(cal1.getTimeInMillis()))+"' AND organization_type='RETAILER' AND parent_id='"+agentOrgId+"';");
				while(rs.next()){
					if(retailerMap.get(rs.getString("organization_id")) != null) {
						reportBean = retailerMap.get(rs.getString("organization_id"));
						reportBean.setOpeningBal(-rs.getDouble("opening_bal_cl_inc"));
					}
				}

				collectionRetailerWiseNew(startDate, endDate, connection, retailerMap,agentOrgId);

			/*	clXclQuery = "SELECT organization_id, IFNULL(SUM(amount),0.0) AS amount FROM (SELECT organization_id, amount FROM st_lms_cl_xcl_update_history WHERE date_time>='"+startDate+"' AND date_time<'"+endDate+"' UNION ALL SELECT organization_id, (cl+xcl) clXcl FROM st_rep_org_bal_history WHERE finaldate>='"+startDate+"' AND finaldate<='"+endDate+"' AND organization_type='RETAILER')aa GROUP BY organization_id;";
				logger.info("clXcl Query - "+clXclQuery);
				rs = stmt.executeQuery(clXclQuery);
				while(rs.next()) {
					reportBean = retailerMap.get(rs.getString("organization_id"));
					if(reportBean != null) {
						reportBean.setClLimit(rs.getDouble("amount"));
					}
				}
*/
				return retailerMap;
			} else {
				lastArchDate = ReportUtility.getLastArchDateInDateFormat(connection);
				rs = stmt.executeQuery("SELECT organization_id, (opening_bal_cl_inc+net_amount_transaction) opening_bal FROM st_rep_org_bal_history WHERE finaldate='"+lastArchDate+"' AND organization_type='RETAILER' AND parent_id='"+agentOrgId+"';");
				while(rs.next()) {
					if(retailerMap.get(rs.getString("organization_id")) != null) {
						retailerMap.get(rs.getString("organization_id")).setOpeningBal(-rs.getDouble("opening_bal"));
					}
				}
			}

			collectionRetailerWiseNew(new Timestamp(lastArchDate.getTime()), startDate, connection, retailerMap,agentOrgId);

			double openingBal = 0.0;
			for(String retOrgId : retailerMap.keySet()) {
				reportBean = retailerMap.get(retOrgId);
				openingBal = reportBean.getOpeningBal()/* - reportBean.getClLimit()*/ + reportBean.getDgSale() - reportBean.getDgCancel() - reportBean.getDgPwt()
							+ reportBean.getSeSale() - reportBean.getSePwt()
							+ reportBean.getCSSale() - reportBean.getCSCancel()
							+ reportBean.getDeposit() - reportBean.getWithdrawal()
							- (reportBean.getCash() + reportBean.getCheque() + reportBean.getCredit()+reportBean.getBankDep() - reportBean.getDebit() - reportBean.getChequeReturn());
				reportBean.setOpeningBal(openingBal);
				reportBean.setClLimit(0.0);
				reportBean.setDgSale(0.0);
				reportBean.setDgCancel(0.0);
				reportBean.setDgPwt(0.0);
				reportBean.setSeSale(0.0);
				reportBean.setSePwt(0.0);
				reportBean.setCSSale(0.0);
				reportBean.setCSCancel(0.0);
				reportBean.setDeposit(0.0);
				reportBean.setWithdrawal(0.0);
				reportBean.setCash(0.0);
				reportBean.setCheque(0.0);
				reportBean.setChequeReturn(0.0);
				reportBean.setCredit(0.0);
				reportBean.setDebit(0.0);
				reportBean.setBankDep(0.0);
			}
			collectionRetailerWiseNew(startDate, endDate, connection, retailerMap,agentOrgId);

			/*clXclQuery = "SELECT organization_id, IFNULL(SUM(amount),0.0) AS amount FROM st_lms_cl_xcl_update_history WHERE date_time>='"+startDate+"' AND date_time<'"+endDate+"' GROUP BY organization_id;";
			logger.info("clXcl Query - "+clXclQuery);
			rs = stmt.executeQuery(clXclQuery);
			while(rs.next()) {
				if(retailerMap.get(rs.getString("organization_id")) != null)
					retailerMap.get(rs.getString("organization_id")).setClLimit(rs.getDouble("amount"));
			}*/
			String retOrgQuery="select organization_id,  name orgCode  from st_lms_organization_master where parent_id='"+agentOrgId+"' AND organization_status<>'TERMINATE' order by orgCode ASC;"; 
			PreparedStatement pstmt = connection.prepareStatement(retOrgQuery);
			ResultSet retclos = pstmt.executeQuery();
			while(retclos.next()){
				if(ReportUtility.isDG){
					if(retailerMap.get(retclos.getString("organization_id")) != null) {
					CollectionReportOverAllBean retBean=retailerMap.get(retclos.getString("organization_id"));
					retBean.setClosingBalance(retBean.getOpeningBal()+((retBean.getDgSale()-retBean.getDgCancel())-retBean.getDgPwt())-(retBean.getCash()+retBean.getCheque()-retBean.getChequeReturn()-retBean.getDebit()+retBean.getCredit()+retBean.getBankDep()));	
					}
				}
				if(ReportUtility.isSE){
					if(retailerMap.get(retclos.getString("organization_id")) != null) {
					CollectionReportOverAllBean retBean=retailerMap.get(retclos.getString("organization_id"));
					retBean.setClosingBalance(retBean.getOpeningBal()+(retBean.getSeSale()-retBean.getSePwt())-(retBean.getCash()+retBean.getCheque()-retBean.getChequeReturn()-retBean.getDebit()+retBean.getCredit()+retBean.getBankDep()));	
					}
				}
				if(ReportUtility.isCS){
					if(retailerMap.get(retclos.getString("organization_id")) != null) {
					CollectionReportOverAllBean retBean=retailerMap.get(retclos.getString("organization_id"));
					retBean.setClosingBalance(retBean.getOpeningBal()+(retBean.getCSSale()-retBean.getCSCancel())-(retBean.getCash()+retBean.getCheque()-retBean.getChequeReturn()-retBean.getDebit()+retBean.getCredit()+retBean.getBankDep()));	
					}
				}
				if(ReportUtility.isOLA){
					if(retailerMap.get(retclos.getString("organization_id")) != null) {
					CollectionReportOverAllBean retBean=retailerMap.get(retclos.getString("organization_id"));
					retBean.setClosingBalance(retBean.getOpeningBal()+(retBean.getDeposit()-retBean.getWithdrawal())-(retBean.getCash()+retBean.getCheque()-retBean.getChequeReturn()-retBean.getDebit()+retBean.getCredit()+retBean.getBankDep()));	
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in Fetching Closing Balance Of Retailer");
		} finally {
			DBConnect.closeCon(connection);
		}
		return retailerMap;
	}
	public void collectionRetailerWiseNew(Timestamp startDate, Timestamp endDate, Connection connection, Map<String, CollectionReportOverAllBean> retailerMap,int agentOrgId) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String cashQry = "SELECT retailer_org_id, amount cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit,0.0 bank_deposit FROM st_lms_agent_cash_transaction cash INNER JOIN st_lms_agent_transaction_master atm ON cash.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"'";
			String chqQry = "SELECT retailer_org_id, 0.0 cash, cheque_amt cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit,0.0 bank_deposit FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master atm ON chq.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND chq.transaction_type IN ('CHEQUE','CLOSED')";
			String chqRetQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, cheque_amt cheque_ret, 0.0 debit, 0.0 credit,0.0 bank_deposit FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master atm ON chq.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND chq.transaction_type IN ('CHQ_BOUNCE')";
			String debitQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, 0.0 cheque_ret, amount debit, 0.0 credit,0.0 bank_deposit FROM st_lms_agent_debit_note dbt INNER JOIN st_lms_agent_transaction_master atm ON dbt.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND dbt.transaction_type IN ('DR_NOTE_CASH','DR_NOTE')";
			String creditQry = "SELECT retailer_org_id, 0.0 cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, amount credit,0.0 bank_deposit FROM st_lms_agent_credit_note crd INNER JOIN st_lms_agent_transaction_master atm ON crd.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND crd.transaction_type IN ('CR_NOTE_CASH','CR_NOTE')";
			String repQry = "SELECT retailer_org_id, cash_amt, cheque_amt, cheque_bounce_amt, debit_note, credit_note,bank_deposit FROM st_rep_agent_payments WHERE finaldate>=DATE('"+startDate+"') AND finaldate<=DATE('"+endDate+"')";
			String bankDepoQry="SELECT retailer_org_id,0.0 cash, 0.0 cheque, 0.0 cheque_ret, 0.0 debit, 0.0 credit,amount bank_deposit FROM st_lms_agent_bank_deposit_transaction bdt INNER JOIN st_lms_agent_transaction_master atm ON bdt.transaction_id=atm.transaction_id AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"'";
			String accQry = "SELECT t.retailer_org_id,t.cash,t.cheque,t.cheque_ret,t.debit,t.credit,t.bank_deposit from(SELECT retailer_org_id, IFNULL(SUM(cash),0.0) cash, IFNULL(SUM(cheque),0.0) cheque, IFNULL(SUM(cheque_ret),0.0) cheque_ret, IFNULL(SUM(debit),0.0) debit, IFNULL(SUM(credit),0.0) credit,IFNULL(SUM(bank_deposit),0.0) bank_deposit FROM ("
					+ cashQry
					+ " UNION ALL "
					+ chqQry
					+ " UNION ALL "
					+ chqRetQry
					+ " UNION ALL "
					+ debitQry
					+ " UNION ALL "
					+ creditQry
					+ " UNION ALL "
					+ bankDepoQry
					+" UNION ALL "
					+ repQry
					+")main GROUP BY retailer_org_id)t inner join (select organization_id from st_lms_organization_master where parent_id='"+agentOrgId+"') o on t.retailer_org_id =o.organization_id;";
			logger.info("Account Detail Query - " + accQry);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(accQry);
			while (rs.next()) {
				String agtOrgId = rs.getString("retailer_org_id");
				if(retailerMap.get(agtOrgId) != null) {
					retailerMap.get(agtOrgId).setCash(rs.getDouble("cash"));
					retailerMap.get(agtOrgId).setCheque(rs.getDouble("cheque"));
					retailerMap.get(agtOrgId).setChequeReturn(rs.getDouble("cheque_ret"));
					retailerMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
					retailerMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
					retailerMap.get(agtOrgId).setBankDep(rs.getDouble("bank_deposit"));
				}
			}

			if (ReportUtility.isDG) {
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				StringBuilder saleQry = new StringBuilder("SELECT s.retailer_org_id,s.mrp_sale,s.net_sale from (SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_sale, IFNULL(SUM(net_amt),0.0) net_sale FROM (");
				StringBuilder cancelQry = new StringBuilder("SELECT c.retailer_org_id,c.mrp_cancel,c.net_cancel from ( SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_cancel, IFNULL(SUM(net_amt),0.0) net_cancel FROM (");
				StringBuilder pwtQry = new StringBuilder("SELECT p.retailer_org_id,p.net_pwt from ( SELECT retailer_org_id, IFNULL(SUM(pwt_amt),0.0) net_pwt FROM (");

				ResultSet gameRs = stmt.executeQuery(gameQry);
				while (gameRs.next()) {
					int gameId = gameRs.getInt("game_id");
					saleQry.append("SELECT rs.retailer_org_id, mrp_amt, net_amt FROM st_dg_ret_sale_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, sale_mrp mrp_amt, sale_net net_amt FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
					cancelQry.append("SELECT rs.retailer_org_id, mrp_amt, net_amt FROM st_dg_ret_sale_refund_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, ref_sale_mrp mrp_cancel, ref_net_amt net_cancel FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
					pwtQry.append("SELECT rs.retailer_org_id, pwt_amt FROM st_dg_ret_pwt_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL SELECT organization_id, pwt_net_amt net_pwt FROM st_rep_dg_retailer WHERE game_id=").append(gameId).append(" AND finaldate>=DATE('").append(startDate).append("') AND finaldate<=DATE('").append(endDate).append("') UNION ALL ");
				}

				saleQry.delete(saleQry.lastIndexOf(" UNION ALL "), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf(" UNION ALL "), cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf(" UNION ALL "), pwtQry.length());

				saleQry.append(")main GROUP BY retailer_org_id )s inner join (select organization_id from st_lms_organization_master where parent_id='"+agentOrgId+"')o on s.retailer_org_id =o.organization_id;");
				cancelQry.append(")main GROUP BY retailer_org_id )c inner join (select organization_id from st_lms_organization_master where parent_id='"+agentOrgId+"')o on c.retailer_org_id =o.organization_id;");
				pwtQry.append(")main GROUP BY retailer_org_id )p inner join (select organization_id from st_lms_organization_master where parent_id='"+agentOrgId+"')o on p.retailer_org_id =o.organization_id;");

				// Draw Sale Query
				logger.info("Draw Sale Query - " + saleQry.toString());
				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgSale(rs.getDouble("net_sale"));
				}

				// Draw Cancel Query
				logger.info("Draw Cancel Query - " + cancelQry.toString());
				rs = stmt.executeQuery(cancelQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgCancel(rs.getDouble("net_cancel"));
				}
				// Draw Pwt Query
				logger.info("Draw Pwt Query - " + pwtQry.toString());
				rs = stmt.executeQuery(pwtQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setDgPwt(rs.getDouble("net_pwt"));
				}
			}

			if (ReportUtility.isSE) {
				String saleQry = null;
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "SELECT organization_id, IFNULL(SUM(mrp_amt),0.00) mrp_amt, IFNULL(SUM(net_amt),0.00) net_amt FROM (SELECT lbt.retailer_org_id organization_id, lbt.mrp_amt, lbt.net_amt FROM st_se_agent_ret_loose_book_transaction lbt INNER JOIN st_se_agent_retailer_transaction trans ON lbt.transaction_id=trans.transaction_id INNER JOIN st_lms_agent_transaction_master atm ON atm.transaction_id=trans.transaction_id AND atm.transaction_type='LOOSE_SALE' AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' UNION ALL SELECT lbt.retailer_org_id organization_id, -lbt.mrp_amt, -lbt.net_amt FROM st_se_agent_ret_loose_book_transaction lbt INNER JOIN st_se_agent_retailer_transaction trans ON lbt.transaction_id=trans.transaction_id INNER JOIN st_lms_agent_transaction_master atm ON atm.transaction_id=trans.transaction_id AND atm.transaction_type='LOOSE_SALE_RET' AND transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"')main GROUP BY organization_id;";
				} else if ("TICKET_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "SELECT organization_id, IFNULL(mrpAmt,0.0) mrp_amt, IFNULL(netAmt,0.0) net_amt FROM st_lms_organization_master LEFT OUTER JOIN (SELECT current_owner_id,SUM(soldTkt*ticket_price) mrpAmt,SUM((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt FROM st_se_game_master gm, st_se_game_inv_detail gid,(SELECT game_id,book_nbr,SUM(sold_tickets) soldTkt FROM st_se_game_ticket_inv_history WHERE DATE>='"+startDate+"' AND DATE<='"+endDate+"' AND current_owner='RETAILER' GROUP BY book_nbr) TktTlb WHERE gm.game_id=TktTlb.game_id AND TktTlb.book_nbr=gid.book_nbr AND gid.current_owner='RETAILER' GROUP BY current_owner_id) saleTlb ON organization_id=current_owner_id WHERE organization_type='RETAILER';";
				}
				logger.info("Scratch Sale Query - "+saleQry);
				rs = stmt.executeQuery(saleQry);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("organization_id")) != null)
						retailerMap.get(rs.getString("organization_id")).setSeSale(rs.getDouble("net_amt"));
				}

				// Scratch Pwt Query
				String pwtQuery = "SELECT retailer_org_id, IFNULL(SUM(pwt),0.0) pwt_amt FROM (SELECT retailer_org_id, SUM(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt FROM st_se_retailer_pwt WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+endDate+"' AND transaction_type='PWT' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER')) GROUP BY retailer_org_id UNION ALL SELECT retailer_org_id,SUM(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt FROM st_se_agent_pwt WHERE STATUS!='DONE_UNCLM' AND transaction_id IN (SELECT transaction_id FROM st_lms_agent_transaction_master WHERE transaction_date>='"+startDate+"' AND transaction_date<='"+startDate+"' AND transaction_type='PWT' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER')) GROUP BY retailer_org_id)main GROUP BY retailer_org_id;";
				logger.info("Scratch Pwt Query - "+pwtQuery);
				rs = stmt.executeQuery(pwtQuery);
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setSePwt(rs.getDouble("pwt_amt"));
				}
			}
			if (ReportUtility.isCS) {
				String catQry = "SELECT category_id FROM st_cs_product_category_master WHERE status='ACTIVE';";
				StringBuilder saleQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_sale, IFNULL(SUM(net_amt),0.0) net_sale FROM (");
				StringBuilder cancelQry = new StringBuilder("SELECT retailer_org_id, IFNULL(SUM(mrp_amt),0.0) mrp_cancel, IFNULL(SUM(net_amt),0.0) net_cancel FROM (");

				ResultSet catRs = stmt.executeQuery(catQry);
				while (catRs.next()) {
					int catId = catRs.getInt("category_id");
					saleQry.append("SELECT cs.retailer_org_id, mrp_amt, net_amt FROM st_cs_sale_").append(catId).append(" cs INNER JOIN st_lms_retailer_transaction_master rtm ON rtm.transaction_id=cs.transaction_id WHERE transaction_type IN('CS_SALE') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL ");
					cancelQry.append("SELECT cs.retailer_org_id, mrp_amt, net_amt FROM st_cs_refund_").append(catId).append(" cs INNER JOIN st_lms_retailer_transaction_master rtm ON rtm.transaction_id=cs.transaction_id WHERE transaction_type IN('CS_CANCEL_SERVER','CS_CANCEL_RET') AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' UNION ALL ");
				}
				saleQry.delete(saleQry.lastIndexOf(" UNION ALL "), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf(" UNION ALL "), cancelQry.length());

				saleQry.append(")main GROUP BY retailer_org_id;");
				cancelQry.append(")main GROUP BY retailer_org_id;");

				// CS Sale Query
				logger.info("CS Sale Query - " + saleQry.toString());
				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setCSSale(rs.getDouble("net_sale"));
				}

				// CS Cancel Query
				logger.info("CS Cancel Query - " + cancelQry.toString());
				rs = stmt.executeQuery(cancelQry.toString());
				while (rs.next()) {
					if(retailerMap.get(rs.getString("retailer_org_id")) != null)
						retailerMap.get(rs.getString("retailer_org_id")).setCSCancel(rs.getDouble("net_cancel"));
				}
			}
			if(ReportUtility.isOLA){
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				requestBean.setFromDate(dateFormat.format(startDate));
				requestBean.setToDate(dateFormat.format(endDate));
				Map<Integer, OlaOrgReportResponseBean> olaMap = OlaRetailerReportControllerImpl.fetchDepositWithdrawlMultipleRetailer(requestBean, connection);
				for(Integer retOrgId : olaMap.keySet()) {
					if(retailerMap.get(String.valueOf(retOrgId)) != null) {
						retailerMap.get(String.valueOf(retOrgId)).setDeposit(olaMap.get(retOrgId).getNetDepositAmt());
						retailerMap.get(String.valueOf(retOrgId)).setWithdrawal(olaMap.get(retOrgId).getNetWithdrawalAmt());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}
	public Map<String, CollectionReportOverAllBean> getLowRetailerClosingBalanceWithAnyDayLimit(Timestamp deployDate, Timestamp startDate, Timestamp endDate,int agentOrgId) throws Exception {
		Map<String, CollectionReportOverAllBean> retailerMap =null; 
		Map<String, CollectionReportOverAllBean> lowRetailerMap =null; 
		CollectionReportOverAllBean reportBean = null;
		int maxDay=0;
		try {
			newStartDate=startDate;
			
			retailerMap=new LinkedHashMap<String, CollectionReportOverAllBean>();
			lowRetailerMap=new LinkedHashMap<String, CollectionReportOverAllBean>();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			maxDay=Integer.parseInt(Utility.getPropertyValue("RET_OUTSTANDING_BALANCE_LIMIT_AUTO_BLOCK"));
			Calendar cal=Calendar.getInstance();
				for(int day=1;day<=maxDay;day++){
					cal.setTime (newStartDate); // convert your date to Calendar object
					cal.add(Calendar.DATE, -1);
					java.util.Date fromDate = (java.util.Date) cal.getTime();
					String start_date=dateFormat.format(fromDate);
					String end_date=start_date;
					dateChanger(start_date, end_date);
					retailerMap=getRetailerWiseClosingBalance(deployDate, newStartDate, newEndDate, agentOrgId);
					Iterator<Map.Entry<String, CollectionReportOverAllBean>> iterator = retailerMap.entrySet().iterator() ;
			        while(iterator.hasNext()){
			        	    Map.Entry<String, CollectionReportOverAllBean> retEntry = iterator.next();
			        	    if(lowRetailerMap.get(retEntry.getKey()) == null)
			        	    	lowRetailerMap.put(retEntry.getKey(),retEntry.getValue());
			        	    if(lowRetailerMap.get(retEntry.getKey()) != null && lowRetailerMap.get(retEntry.getKey()).getNoOfDays() == null)
			        	    	lowRetailerMap.put(retEntry.getKey(),retEntry.getValue());
							if(retEntry.getValue().getClosingBalance() <= 0 && lowRetailerMap.get(retEntry.getKey()) != null && lowRetailerMap.get(retEntry.getKey()).getNoOfDays() == null){
								retailerMap.get(retEntry.getKey()).setNoOfDays(String.valueOf(day));
									
							}else if(day==maxDay){
									retailerMap.get(retEntry.getKey()).setNoOfDays(">="+String.valueOf(maxDay));    
								}
							 }
						}
					
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in Fetching Closing Balance Of Retailer");
		} finally {
			//DBConnect.closeCon(connection);
		}
		return lowRetailerMap;
	}
	private  void  dateChanger(String start_date, String end_date){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try{
		newStartDate = new Timestamp(dateFormat
				.parse(start_date).getTime());
		newEndDate = new Timestamp(dateFormat
				.parse(end_date).getTime()+ 24 * 60 * 60 * 1000 - 1000);
		
		}catch(ParseException ex){
			ex.printStackTrace();
		}
	}
}