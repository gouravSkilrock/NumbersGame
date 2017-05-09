package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentCollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.accMgmt.common.RetailerOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class AgentCollectionReportOverAllHelper {
	Log logger = LogFactory.getLog(ICollectionReportOverAllHelper.class);

	public Map<String, AgentCollectionReportOverAllBean> collectionRetailerWiseWithOpeningBal(
			int userId, Timestamp deployDate, Timestamp startDate,
			Timestamp endDate) throws LMSException {
		int count = 0;
		PreparedStatement pstmt = null;

		ResultSet rsRetOrg = null;
		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, AgentCollectionReportOverAllBean> agtMap = new LinkedHashMap<String, AgentCollectionReportOverAllBean>();
		AgentCollectionReportOverAllBean collBean = null;

		try {
			con = DBConnect.getConnection();
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where parent_id="
					+ userId + " order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new AgentCollectionReportOverAllBean();
				collBean.setRetailerName(rsRetOrg.getString("name"));
				collBean.setOpeningBal(0.0);
				collBean.setCash(0.0);
				collBean.setCheque(0.0);
				collBean.setChequeReturn(0.0);
				collBean.setCredit(0.0);
				collBean.setDebit(0.0);
				collBean.setBankDep(0.0);
				if (ReportUtility.isDG) {
					collBean.setDgSale(0.0);
					collBean.setDgPwt(0.0);
					collBean.setDgCancel(0.0);
					collBean.setDgDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setSeSale(0.0);
					collBean.setSePwt(0.0);
					collBean.setSeDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if (ReportUtility.isOLA) {
					collBean.setDeposit(0.0);
					collBean.setDepositRefund(0.0);
					collBean.setWithdrawal(0.0);
					collBean.setWithdrawalRefund(0.0);
					collBean.setNetGamingComm(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}

			// for calculation of opening Balance
			Timestamp endDateForOpeningBal = startDate;
			RetailerOpeningBalanceHelper retOpenHelper=new RetailerOpeningBalanceHelper();
			retOpenHelper.collectionRetailerWiseOpenBal(userId, deployDate, endDateForOpeningBal,
					con, agtMap, count);

			count++;
			collectionRetailerWise(userId, startDate, endDate, con, agtMap, count);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionRetailerWiseWithOpeningBal");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}

	public void collectionRetailerWise(int userId, Timestamp startDate,
			Timestamp endDate, Connection con,Map<String, AgentCollectionReportOverAllBean> agtMap, int count)
			throws LMSException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;

		if (startDate.after(endDate)) {
			return;
		}

		try {
			// Get Account Details
			String accQry;
			String unionQuery;
			StringBuilder accountArchQry;
			StringBuilder saleRefundArchQry;
			StringBuilder saleArchQry;
			StringBuilder pwtArchQry;

			String addOrgQry = "right outer join (select organization_id from st_lms_organization_master where parent_id="
					+ userId + ") om on party_id=organization_id";
			String cashQry = "(select organization_id,cash from (select party_id,sum(amount) cash from st_lms_agent_cash_transaction cash,st_lms_agent_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) cash " + addOrgQry + ") cash";
			String chqQry = "(select organization_id,chq from (select party_id,sum(cheque_amt) chq from st_lms_agent_sale_chq chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) chq " + addOrgQry + ") chq";
			String chqRetQry = "(select organization_id,chq_ret from (select party_id,sum(cheque_amt) chq_ret from st_lms_agent_sale_chq  chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) chq_ret " + addOrgQry + ") chq_ret";
			String debitQry = "(select organization_id,debit from (select party_id,sum(amount) debit from st_lms_agent_debit_note debit,st_lms_agent_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) debit " + addOrgQry + ") debit";
			String creditQry = "(select organization_id,credit from (select party_id,sum(amount) credit from st_lms_agent_credit_note credit,st_lms_agent_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) credit " + addOrgQry + ") credit";

			accQry = "select cash.organization_id,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq,ifnull(chq_ret,0.0) chq_ret,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from "
					+ cashQry
					+ ","
					+ chqQry
					+ ","
					+ chqRetQry
					+ ","
					+ debitQry
					+ ","
					+ creditQry
					+ " where cash.organization_id=chq.organization_id and cash.organization_id=chq_ret.organization_id and cash.organization_id=debit.organization_id and cash.organization_id=credit.organization_id and chq.organization_id=chq_ret.organization_id and chq.organization_id=debit.organization_id and chq.organization_id=credit.organization_id and chq_ret.organization_id=debit.organization_id and chq_ret.organization_id=credit.organization_id and debit.organization_id=credit.organization_id";

			if (LMSFilterDispatcher.isRepFrmSP) {
				accountArchQry = new StringBuilder(
						"select FINAL.organization_id, sum(FINAL.cash) cash, sum(FINAL.chq) chq, sum(FINAL.chq_ret) chq_ret, sum(FINAL.debit) debit, sum(FINAL.credit) credit from (");
				unionQuery = " union all select retailer_org_id organization_id, sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt) as chq_ret,sum(debit_note) as debit ,sum(credit_note) as credit from st_rep_agent_payments where finaldate>='"
						+ startDate
						+ "' and finaldate<='"
						+ endDate
						+ "' and  parent_id="
						+ userId
						+ " group by retailer_org_id ) FINAL group by organization_id";
				accountArchQry.append(accQry).append(unionQuery);
				pstmt = con.prepareStatement(accountArchQry.toString());
			} else {
				pstmt = con.prepareStatement(accQry);
			}

			logger.debug("---Account Detail Query At Agents End---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				agtMap.get(agtOrgId).setCash(rs.getDouble("cash"));
				agtMap.get(agtOrgId).setCheque(rs.getDouble("chq"));
				agtMap.get(agtOrgId).setChequeReturn(rs.getDouble("chq_ret"));
				agtMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
				agtMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
			}

			if (ReportUtility.isDG) {
				saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select om.organization_id,ifnull(FINAL_TABLE.sale,0.0) sale from (select m.retailer_org_id,sum(m.sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select om.organization_id,ifnull(FINAL_TABLE.cancel,0.0) cancel from (select m.retailer_org_id,sum(m.cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select om.organization_id,ifnull(FINAL_TABLE.pwt,0.0) pwt from (select m.retailer_org_id,sum(m.pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select aa.retailer_org_id,aa.sale as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs inner join ("
									+ saleTranDG
									+ ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");

					cancelQry
							.append("(select aa.retailer_org_id,aa.cancel as cancel from(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs inner join ("
									+ cancelTranDG
									+ ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");

					pwtQry
							.append("(select aa.retailer_org_id,aa.pwt as pwt from(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs inner join ("
									+ pwtTranDG
									+ ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry
						.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				cancelQry
						.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				pwtQry
						.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") om on FINAL_TABLE.retailer_org_id=om.organization_id");

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder(
							"select organization_id,sum(sale) sale from (");
					unionQuery = " union all select organization_id,sum(sale_net) as sale from st_rep_dg_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ "  group by organization_id ) Final group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry.toString());
				}
				logger.debug("-------Draw Sale Query------\n" + pstmt);

				// Draw Sale Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgSale(
							rs.getDouble("sale"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleRefundArchQry = new StringBuilder(
							"select organization_id,sum(cancel) cancel from (");
					unionQuery = " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_dg_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ "  group by organization_id ) Final group by organization_id";
					saleRefundArchQry.append(cancelQry).append(unionQuery);
					pstmt = con.prepareStatement(saleRefundArchQry.toString());
				} else {
					pstmt = con.prepareStatement(cancelQry.toString());
				}
				logger.debug("-------Draw Cancel Qurey------\n" + pstmt);

				// Draw Cancel Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgCancel(
							rs.getDouble("cancel"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					pwtArchQry = new StringBuilder(
							"select organization_id,sum(pwt) pwt from (");
					unionQuery = " union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_dg_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ "  group by organization_id ) Final group by organization_id";
					pwtArchQry.append(pwtQry).append(unionQuery);
					pstmt = con.prepareStatement(pwtArchQry.toString());
				} else {
					pstmt = con.prepareStatement(pwtQry.toString());
				}
				logger.debug("-------Draw Pwt Qurey------\n" + pstmt);

				// Draw Pwt Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgPwt(
							rs.getDouble("pwt"));
				}
		}

			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select sale.retailer_org_id organization_id ,mrpAmt-mrpAmtRet  mrpAmt,  netAmt-netAmtRet netAmt from (select om.retailer_org_id,ifnull(mrpAmt,0) mrpAmt ,ifnull(netAmt,0) netAmt from (select organization_id retailer_org_id from st_lms_organization_master where parent_id="+userId+") om left join  (select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id) sale group by retailer_org_id) sale on om.retailer_org_id=sale.retailer_org_id) sale , (select om.retailer_org_id,ifnull(mrpAmtRet,0) mrpAmtRet ,ifnull(netAmtRet,0) netAmtRet from (select organization_id retailer_org_id from st_lms_organization_master where parent_id="+userId+") om left join (select retailer_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id) sale group by retailer_org_id) saleRet on om.retailer_org_id=saleRet.retailer_org_id) saleRet where saleRet.retailer_org_id=sale.retailer_org_id";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select om.organization_id,FINAL_TABLE.name,FINAL_TABLE.mrpAmt,FINAL_TABLE.netAmt from (select organization_id from st_lms_organization_master where parent_id="
							+ userId
							+ ") om left join (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER') FINAL_TABLE on om.organization_id=FINAL_TABLE.organization_id";

				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder(
							"select organization_id,sum(mrpAmt) mrpAmt, sum(netAmt) netAmt from (");
					unionQuery = " union all select organization_id,sum(sale_book_mrp)-sum(ref_sale_mrp) mrpAmt ,sum(sale_book_net) -sum(ref_net_amt) as netAmt from st_rep_se_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ " group by organization_id) FINAL group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry);
				}
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSeSale(
							rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String transactionType = null;
				if (count == 0)
					transactionType = "transaction_type in ('PWT','PWT_AUTO')";
				else
					transactionType = "transaction_type='PWT'";

				String pwtQry = "select retailer_org_id organization_id, sum(pwt) pwt from (select om.organization_id retailer_org_id,ifnull(tx.pwt,0) pwt from (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ " ) om left join (select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt pwt inner join (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "'  and "
						+ transactionType
						+ " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ " )) tx on  tx.transaction_id=pwt.transaction_id group by retailer_org_id) tx on om.organization_id=tx.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt pwt inner join (select transaction_id from st_lms_agent_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "'  and "
						+ transactionType
						+ " and party_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ " )) tx on  tx.transaction_id=pwt.transaction_id group by retailer_org_id) pwt group by retailer_org_id";
				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					pwtArchQry = new StringBuilder(
							"select organization_id,sum(pwt) pwt from (");
					unionQuery = " union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"
							+ startDate
							+ "' and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ " group by organization_id ) FINAL group by organization_id";
					pwtArchQry.append(pwtQry).append(unionQuery);
					pstmt = con.prepareStatement(pwtArchQry.toString());
				} else {
					pstmt = con.prepareStatement(pwtQry);
				}
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSePwt(
							rs.getDouble("pwt"));
				}
			}

			if (ReportUtility.isCS) {

				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select om.organization_id,ifnull(FINAL_TABLE.sale,0.0) sale from (select m.retailer_org_id,sum(m.sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select om.organization_id,ifnull(FINAL_TABLE.cancel,0.0) cancel from (select m.retailer_org_id,sum(m.cancel) as cancel from (");
				while (rsProduct.next()) {
					saleQry
							.append("(select aa.retailer_org_id,aa.sale as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_"
									+ rsProduct.getInt("category_id")
									+ " drs inner join ("
									+ saleTranCS
									+ ")tx on drs.transaction_id=tx.transaction_id group by drs.retailer_org_id) aa) union all");

					cancelQry
							.append("(select aa.retailer_org_id,aa.cancel as cancel from(select drs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_"
									+ rsProduct.getInt("category_id")
									+ " drs inner join ("
									+ cancelTranCS
									+ ")tx on drs.transaction_id=tx.transaction_id group by drs.retailer_org_id) aa) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());

				saleQry
						.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				cancelQry
						.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") om on FINAL_TABLE.retailer_org_id=om.organization_id");

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder(
							"select organization_id,sum(sale) sale from (");
					unionQuery = " union all select organization_id,sum(sale_net) as sale from st_rep_cs_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ "  group by organization_id ) Final group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry.toString());
				}
				logger.debug("-------CS Sale Query------\n" + pstmt);
				// CS Sale Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSSale(
							rs.getDouble("sale"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleRefundArchQry = new StringBuilder(
							"select organization_id,sum(cancel) cancel from (");
					unionQuery = " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_cs_retailer where finaldate>='"
							+ startDate
							+ "'  and finaldate<='"
							+ endDate
							+ "'  and parent_id="
							+ userId
							+ "  group by organization_id ) Final group by organization_id";
					saleRefundArchQry.append(cancelQry).append(unionQuery);
					pstmt = con.prepareStatement(saleRefundArchQry.toString());
				} else {
					pstmt = con.prepareStatement(cancelQry.toString());
				}
				logger.debug("-------CS Cancel Query------\n" + pstmt);

				// CS Cancel Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSCancel(
							rs.getDouble("cancel"));
				}
			}
			if (ReportUtility.isOLA) {

				StringBuilder wdQry = new StringBuilder(
						"select om.organization_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from ");
				StringBuilder wdRefQry = new StringBuilder(
						"select om.organization_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from");
				StringBuilder depQry = new StringBuilder(
						"select om.organization_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from");
				StringBuilder depRefQry = new StringBuilder(
						"select om.organization_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from");

				wdQry
						.append("(select wrs.retailer_org_id, net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				wdRefQry
						.append("(select wrs.retailer_org_id,net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");
				depQry
						.append("(select wrs.retailer_org_id, net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				depRefQry
						.append("(select wrs.retailer_org_id, net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				String netGamingQry = "select om.organization_id agtOrgId, ifnull(sum(commission_calculated),0.0) netAmt from (select ret_org_id, plr_net_gaming, commission_calculated from st_ola_agt_ret_commission where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type = 'OLA_COMMISSION' and transaction_date >= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "')) bac right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on bac.ret_org_id = om.organization_id group by om.organization_id";

				if (LMSFilterDispatcher.isRepFrmSP) {
					// WRITE CODE FOR REP TABLES FOR OLA
				}

				logger.debug("-------Withdrawal Query------\n" + wdQry);
				logger.debug("-------WithDrawal Refund Query------\n"
						+ wdRefQry);
				logger.debug("-------Deposit Query------\n" + depQry);
				logger.debug("-------Deposit Refund Query------\n" + depRefQry);
				logger.debug("-------Net Gaming Query------\n" + netGamingQry);

				// Withdrawal Query
				pstmt = con.prepareStatement(wdQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setWithdrawal(
							rs.getDouble("wdraw"));
				}
				// WithDrawal Refund Query
				pstmt = con.prepareStatement(wdRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setWithdrawalRefund(
							rs.getDouble("wdrawRef"));
				}

				// Deposit Query
				pstmt = con.prepareStatement(depQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setDeposit(
							rs.getDouble("depoAmt"));
				}
				// Deposit Refund Query
				pstmt = con.prepareStatement(depRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setDepositRefund(
							rs.getDouble("depoRefAmt"));
				}

				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
							rs.getDouble("netAmt"));
				}
			}

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionRetailerWise");
		}
	}

	public void collectionRetailerWiseExpand(int userId, Timestamp startDate,
			Timestamp endDate, Map<String, AgentCollectionReportOverAllBean> agtMap)
			throws LMSException {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		String wdTranOLA = null;
		String wdRefTranOLA = null;
		String depoTranOLA = null;
		String depoRefTranOLA = null;
		String netGTranOLA = null;
		StringBuilder drawQry = null;
		StringBuilder scratchQry = null;
		StringBuilder CSQry = null;
		StringBuilder OLAQry = null;
		AgentCollectionReportOverAllBean agentBean = null;
		CompleteCollectionBean gameBean, prodBean = null;
		try {
			if (ReportUtility.isDG) {
				drawQry = new StringBuilder(
						"select retTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from  ");
				saleTranDG = "(select organization_id,ifnull(sum(sale),0) as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")bb on retailer_org_id=organization_id group by organization_id) pwt";

				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") retTlb on gameTlb.organization_id=retTlb.organization_id");
				logger.debug("For Expand draw game Qry:: " + drawQry);

				/*
				 * dirPlrPwt =
				 * "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id"
				 * ;
				 * 
				 * logger.debug("For Expand draw game Qry Direct Ply:: " +
				 * dirPlrPwt);
				 */

				// Game Master Query

				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					logger.debug("-------Indivisual Game Qry-------\n" + pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String retOrgId = rs.getString("organization_id");
						agentBean = agtMap.get(retOrgId);
						Map<String, CompleteCollectionBean> gameMap = agentBean
								.getGameBeanMap();
						if (gameMap == null) {
							gameMap = new HashMap<String, CompleteCollectionBean>();
							agentBean.setGameBeanMap(gameMap);
						}
						gameBean = gameMap.get(gameName);
						if (gameBean == null) {
							gameBean = new CompleteCollectionBean();
							gameMap.put(gameName, gameBean);
						}
						gameBean.setOrgName(gameName);
						gameBean.setDrawSale(rs.getDouble("sale"));
						gameBean.setDrawCancel(rs.getDouble("cancel"));
						gameBean.setDrawPwt(rs.getDouble("pwt"));
					}

					// Direct Player Pwt
					/*
					 * pstmt = con.prepareStatement(dirPlrPwt); pstmt.setInt(1,
					 * gameId); rs = pstmt.executeQuery(); while (rs.next()) {
					 * String agtOrgId = rs.getString("organization_id");
					 * agentBean = agtMap.get(agtOrgId); Map<String,
					 * CompleteCollectionBean> gameMap = agentBean
					 * .getGameBeanMap(); if (gameMap == null) { gameMap = new
					 * HashMap<String, CompleteCollectionBean>();
					 * agentBean.setGameBeanMap(gameMap); } gameBean =
					 * gameMap.get(gameName); if (gameBean == null) { gameBean =
					 * new CompleteCollectionBean(); gameMap.put(gameName,
					 * gameBean); }
					 * gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir")); }
					 */
				}
			}

			if (ReportUtility.isSE) {
				scratchQry = new StringBuilder(
						"select retTlb.organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.organization_id,sale,pwt from");
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				boolean isTrue = false;
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					isTrue = true;
					saleTranDG = "(select sale.organization_id ,sale.name,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) sale from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join 	(   select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction right outer join st_lms_organization_master on organization_id=retailer_org_id where game_id=?   and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  ) group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_ret_loose_book_transaction where game_id=?   and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  ) group by retailer_org_id )sale group by retailer_org_id) sale on organization_id=retailer_org_id where parent_id="
							+ userId
							+ " ) sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select retailer_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction where game_id=?   and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  ) group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_ret_loose_book_transaction where game_id=?   and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  ) group by retailer_org_id )saleRet group by retailer_org_id) saleRet on organization_id=retailer_org_id where parent_id="
							+ userId
							+ " ) saleRet on sale.organization_id=saleRet.organization_id) sale left outer join ";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id parent_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' and game_id=? group by book_nbr) TktTlb where gm.game_id=? and gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT') sale,";
				}
				pwtTranDG = "(select retailer_org_id organization_id ,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt where game_id=?  and transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "'    and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")) group by retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt where status!='DONE_UNCLM' and game_id=?  and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "'    and transaction_type='PWT') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ") group by retailer_org_id) aa group by retailer_org_id ) pwt";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" "
								+ ((isTrue) ? "on" : "where")
								+ " sale.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") retTlb on gameTlb.organization_id=retTlb.organization_id");
				logger.debug("For Expand scratch game Qry:: " + scratchQry);

				/*
				 * dirPlrPwt =
				 * "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id"
				 * ;
				 * 
				 * logger.debug("For Expand scratch game Qry Direct Ply:: " +
				 * dirPlrPwt);
				 */
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					pstmt.setInt(4, gameId);
					pstmt.setInt(5, gameId);
					pstmt.setInt(6, gameId);
					// pstmt.setInt(5, gameId);
					logger.debug("-------Indivisual Game Qry-------\n" + pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("organization_id");
						agentBean = agtMap.get(agtOrgId);
						Map<String, CompleteCollectionBean> gameMap = agentBean
								.getGameBeanMap();
						if (gameMap == null) {
							gameMap = new HashMap<String, CompleteCollectionBean>();
							agentBean.setGameBeanMap(gameMap);
						}
						gameBean = gameMap.get(gameName);
						if (gameBean == null) {
							gameBean = new CompleteCollectionBean();
							gameMap.put(gameName, gameBean);
						}
						gameBean.setOrgName(gameName);
						gameBean.setDrawSale(rs.getDouble("sale"));
						gameBean.setDrawCancel(0.0);
						gameBean.setDrawPwt(rs.getDouble("pwt"));
					}

					// Direct Player Pwt
					/*
					 * pstmt = con.prepareStatement(dirPlrPwt); pstmt.setInt(1,
					 * gameId); rs = pstmt.executeQuery(); while (rs.next()) {
					 * String agtOrgId = rs.getString("organization_id");
					 * agentBean = agtMap.get(agtOrgId); Map<String,
					 * CompleteCollectionBean> gameMap = agentBean
					 * .getGameBeanMap(); if (gameMap == null) { gameMap = new
					 * HashMap<String, CompleteCollectionBean>();
					 * agentBean.setGameBeanMap(gameMap); } gameBean =
					 * gameMap.get(gameName); if (gameBean == null) { gameBean =
					 * new CompleteCollectionBean(); gameMap.put(gameName,
					 * gameBean); }
					 * gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir")); }
					 */
				}
			}

			if (ReportUtility.isCS) {

				CSQry = new StringBuilder(
						"select retTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select sale.organization_id,sale,cancel from  ");
				saleTranCS = "(select organization_id,ifnull(sum(sale),0) as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranCS = "(select organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")bb on retailer_org_id=organization_id group by organization_id) cancel";

				CSQry.append(saleTranCS);
				CSQry.append(cancelTranCS);
				CSQry
						.append(" where sale.organization_id=cancel.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ") retTlb on gameTlb.organization_id=retTlb.organization_id");
				logger.debug("For Expand draw game Qry:: " + CSQry);

				// Category Master Query
				String prodQry = "select category_id, category_code from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(prodQry);
				ResultSet rsProd = prodPstmt.executeQuery();
				while (rsProd.next()) {
					int catId = rsProd.getInt("category_id");
					String catName = rsProd.getString("category_code");
					pstmt = con.prepareStatement(CSQry.toString());
					pstmt.setInt(1, catId);
					pstmt.setInt(2, catId);
					logger.debug("-------Indivisual Category Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("organization_id");
						agentBean = agtMap.get(agtOrgId);
						Map<String, CompleteCollectionBean> prodMap = agentBean
								.getGameBeanMap();
						if (prodMap == null) {
							prodMap = new HashMap<String, CompleteCollectionBean>();
							agentBean.setGameBeanMap(prodMap);
						}
						prodBean = prodMap.get(catName);
						if (prodBean == null) {
							prodBean = new CompleteCollectionBean();
							prodMap.put(catName, prodBean);
						}
						prodBean.setOrgName(catName);
						prodBean.setCSSale(rs.getDouble("sale"));
						prodBean.setCSCancel(rs.getDouble("cancel"));

						System.out.println("map size:" + prodMap.size());
					}

				}

			}
			if (ReportUtility.isOLA) {

				OLAQry = new StringBuilder(
						"select withdraw.retOrgId, (wdra-wdraRef) withdrawAmt, (depo-depoRef) depositAmt, netAmt netGamingComm from ");
				wdTranOLA = "(select om.organization_id retOrgId, ifnull(sum(wdra),0.0)wdra from (select orw.retailer_org_id retId, ifnull(sum(net_amt),0.0) as wdra from st_ola_ret_withdrawl orw where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "') and orw.wallet_id = ? group by retId)wd right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on wd.retId = om.organization_id group by om.organization_id)wd,";

				wdRefTranOLA = "(select om.organization_id retOrgId, ifnull(sum(wdraRef),0.0)wdraRef from (select orwRef.retailer_org_id retId, ifnull(sum(net_amt),0.0) as wdraRef from st_ola_ret_withdrawl_refund orwRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL_REFUND' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "') and orwRef.wallet_id = ? group by retId)wdRef right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on wdRef.retId = om.organization_id group by organization_id)wdRef ";

				depoTranOLA = "(select om.organization_id retOrgId, ifnull(sum(depo),0.0)depo from (select ordepo.retailer_org_id retId, ifnull(sum(net_amt),0.0) as depo from st_ola_ret_deposit ordepo where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "') and ordepo.wallet_id =? group by retId)depo right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on depo.retId = om.organization_id group by organization_id)depo,";

				depoRefTranOLA = "(select om.organization_id retOrgId, ifnull(sum(depoRef),0.0) depoRef from (select ordepoRef.retailer_org_id retId, ifnull(sum(net_amt),0.0) as depoRef from st_ola_ret_deposit_refund ordepoRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT_REFUND' and transaction_date>= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "') and ordepoRef.wallet_id = ? group by retId)depoRef right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on depoRef.retId = om.organization_id group by organization_id)depoRef";

				netGTranOLA = "(select om.organization_id retOrgId, ifnull(sum(commission_calculated),0.0)netAmt from (select ret_org_id, plr_net_gaming, commission_calculated from st_ola_agt_ret_commission where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type = 'OLA_COMMISSION' and transaction_date>= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "') and wallet_id = ?)netg right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ ")om on netg.ret_org_id = om.organization_id group by om.organization_id) netgaming";

				OLAQry.append("(select wd.retOrgId, wdra, wdraRef from ");
				OLAQry.append(wdTranOLA);
				OLAQry.append(wdRefTranOLA);
				OLAQry.append(" where wd.retOrgId = wdRef.retOrgId)withdraw,");
				OLAQry.append("(select depo.retOrgId, depo, depoRef from");
				OLAQry.append(depoTranOLA);
				OLAQry.append(depoRefTranOLA);
				OLAQry
						.append(" where depo.retOrgId = depoRef.retOrgId)deposit, ");
				OLAQry.append(netGTranOLA);
				OLAQry
						.append(" where withdraw.retOrgId = deposit.retOrgId and netgaming.retOrgId = withdraw.retOrgId and netgaming.retOrgId = deposit.retOrgId");

				logger.debug("For Expand OLA wallet Qry:: " + OLAQry);

				// Wallet Master Query
				String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master";
				PreparedStatement walletPstmt = con.prepareStatement(walletQry);
				ResultSet rsWallet = walletPstmt.executeQuery();
				while (rsWallet.next()) {
					int walletId = rsWallet.getInt("wallet_id");
					String walletName = rsWallet.getString("wallet_name");
					pstmt = con.prepareStatement(OLAQry.toString());
					pstmt.setInt(1, walletId);
					pstmt.setInt(2, walletId);
					pstmt.setInt(3, walletId);
					pstmt.setInt(4, walletId);
					pstmt.setInt(5, walletId);
					logger.debug("-------Indivisual Wallet Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("agtOrgId");
						agentBean = agtMap.get(agtOrgId);
						Map<String, CompleteCollectionBean> walletMap = agentBean
								.getGameBeanMap();
						if (walletMap == null) {
							walletMap = new HashMap<String, CompleteCollectionBean>();
							agentBean.setGameBeanMap(walletMap);
						}
						gameBean = walletMap.get(walletName);
						if (gameBean == null) {
							gameBean = new CompleteCollectionBean();
							walletMap.put(walletName, gameBean);
						}
						gameBean.setOrgName(walletName);
						gameBean.setOlaWithdrawalAmt(rs
								.getDouble("withdrawAmt"));
						gameBean.setOlaDepositAmt(rs.getDouble("depositAmt"));
						gameBean.setOlaNetGaming(rs.getDouble("netGamingComm"));
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise Expand");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
