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
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.accMgmt.common.AgentOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CollectionReportOverAllHelper implements ICollectionReportOverAllHelper{
	Log logger = LogFactory.getLog(CollectionReportOverAllHelper.class);

	

	public void collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con,	Map<String, CollectionReportOverAllBean> agtMap)
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

			String addOrgQry = "right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id";
			String cashQry = "(select organization_id,cash from (select agent_org_id,sum(amount) cash from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) cash " + addOrgQry + ") cash";
			String chqQry = "(select organization_id,chq from (select agent_org_id,sum(cheque_amt) chq from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) chq " + addOrgQry + ") chq";
			String chqRetQry = "(select organization_id,chq_ret from (select agent_org_id,sum(cheque_amt) chq_ret from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) chq_ret "
					+ addOrgQry
					+ ") chq_ret";
			String debitQry = "(select organization_id,debit from (select agent_org_id,sum(amount) debit from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) debit " + addOrgQry + ") debit";
			String creditQry = "(select organization_id,credit from (select agent_org_id,sum(amount) credit from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) credit "
					+ addOrgQry
					+ ") credit";
			String bankQry = "(select organization_id,bank from (select agent_org_id,sum(amount) bank from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by agent_org_id) bank " + addOrgQry + ") bank";

			String accQry = "select cash.organization_id,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq,ifnull(chq_ret,0.0) chq_ret,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit, ifnull(bank,0.0) bank from "
					+ cashQry
					+ ","
					+ chqQry
					+ ","
					+ chqRetQry
					+ ","
					+ debitQry
					+ ","
					+ creditQry
					+ ","
					+ bankQry
					+ " where cash.organization_id=chq.organization_id and cash.organization_id=chq_ret.organization_id and cash.organization_id=debit.organization_id and cash.organization_id=credit.organization_id and cash.organization_id=bank.organization_id and chq.organization_id=chq_ret.organization_id and chq.organization_id=debit.organization_id and chq.organization_id=credit.organization_id and chq.organization_id=bank.organization_id and chq_ret.organization_id=debit.organization_id and chq_ret.organization_id=credit.organization_id and chq_ret.organization_id=bank.organization_id  and debit.organization_id=credit.organization_id and debit.organization_id=bank.organization_id and credit.organization_id=bank.organization_id ";
			pstmt = con.prepareStatement(accQry);
			logger.debug("---Account Detail Query---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				agtMap.get(agtOrgId).setCash(rs.getDouble("cash"));
				agtMap.get(agtOrgId).setCheque(rs.getDouble("chq"));
				agtMap.get(agtOrgId).setChequeReturn(rs.getDouble("chq_ret"));
				agtMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
				agtMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
				agtMap.get(agtOrgId).setBankDep(rs.getDouble("bank"));
			}

			if (ReportUtility.isDG) {
				saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale from (select parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select organization_id,ifnull(cancel,0.0) cancel from (select parent_id,sum(cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select organization_id,ifnull(pwt,0.0) pwt from (select parent_id,sum(pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ saleTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ cancelTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					pwtQry
							.append("(select bb.parent_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ pwtTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry
						.append(") saleTlb group by parent_id) saleTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id");
				cancelQry
						.append(") cancelTlb group by parent_id) cancelTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id");
				pwtQry
						.append(") pwtTlb group by parent_id) pwtTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id");
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgSale(
							rs.getDouble("sale"));
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgCancel(
							rs.getDouble("cancel"));
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgPwt(
							rs.getDouble("pwt"));
				}

				// Draw Direct Player Qry

				String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger.debug("-------Draw Direct Player Qry------\n" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select sale.organization_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id)sale group by agent_org_id) sale on organization_id=agent_org_id where organization_type='AGENT') sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id )saleRet group by agent_org_id ) saleRet on organization_id=agent_org_id where organization_type='AGENT') saleRet on sale.organization_id=saleRet.organization_id";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT'";
				}
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSeSale(
							rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String pwtQry = "select organization_id,ifnull(sum(pwt),0.0) pwt from (select bb.parent_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt where status!='DONE_UNCLM' and transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id union all select agent_org_id,sum(pwt_amt+comm_amt) boPwt from st_se_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT')) group by agent_org_id) pwtTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id group by organization_id";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setTimestamp(3, startDate);
				pstmt.setTimestamp(4, endDate);
				pstmt.setTimestamp(5, startDate);
				pstmt.setTimestamp(6, endDate);
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSePwt(
							rs.getDouble("pwt"));
				}

				// Scratch Direct Player Qry

				String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger
						.debug("-------Scratch Direct Player Qry------\n"
								+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSeDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if (ReportUtility.isCS) {

				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale from (select parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select organization_id,ifnull(cancel,0.0) cancel from (select parent_id,sum(cancel) as cancel from (");
				while (rsProduct.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_"
									+ rsProduct.getInt("category_id")
									+ " drs where transaction_id in ("
									+ saleTranCS
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_"
									+ rsProduct.getInt("category_id")
									+ " drs where transaction_id in ("
									+ cancelTranCS
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());

				saleQry
						.append(") saleTlb group by parent_id) saleTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id");
				cancelQry
						.append(") cancelTlb group by parent_id) cancelTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on parent_id=organization_id");

				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n" + cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSSale(
							rs.getDouble("sale"));
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSCancel(
							rs.getDouble("cancel"));
				}
			}
			if(ReportUtility.isOLA){
				
			StringBuilder wdQry = new StringBuilder(
					"select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from ");
			StringBuilder wdRefQry = new StringBuilder(
					"select om.parent_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from");
			StringBuilder depQry = new StringBuilder(
					"select om.parent_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from");
			StringBuilder depRefQry = new StringBuilder(
					"select om.parent_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from");
						
				wdQry
						.append("(select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
									+ startDate 
									+ "' and transaction_date<='"
									+ endDate
									+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id");

				wdRefQry
						.append("(select wrs.retailer_org_id, agent_net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ startDate 
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id");
				depQry
						.append("(select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ startDate 
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id");

				depRefQry
						.append("(select wrs.retailer_org_id, agent_net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ startDate 
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id");

				String netGamingQry = "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id";
			logger.debug("-------Withdrawal Query------\n" + wdQry);
			logger.debug("-------WithDrawal Refund Query------\n" + wdRefQry);
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
			
			//net gaming commission query
			pstmt = con.prepareStatement(netGamingQry);
			rs = pstmt.executeQuery();
			while(rs.next()){
				agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
						rs.getDouble("netAmt"));
			}
		}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}

	public void collectionAgentWiseExpand(Timestamp startDate,
			Timestamp endDate,Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		String dirPlrPwt = null;
		String wdTranOLA = null;
		String wdRefTranOLA = null;
		String depoTranOLA = null;
		String depoRefTranOLA= null;
		String netGTranOLA = null;
		StringBuilder drawQry = null;
		StringBuilder scratchQry = null;
		StringBuilder CSQry = null;
		StringBuilder OLAQry = null;
		CollectionReportOverAllBean agentBean = null;
		CompleteCollectionBean gameBean, prodBean = null;
		try {
			if (ReportUtility.isDG) {
				drawQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.parent_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,";
				cancelTranDG = "(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel,";
				pwtTranDG = "(select bb.parent_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) pwt";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.parent_id=cancel.parent_id and sale.parent_id=pwt.parent_id and cancel.parent_id=pwt.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				logger.debug("For Expand draw game Qry:: " + drawQry);

				dirPlrPwt = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id";

				logger.debug("For Expand draw game Qry Direct Ply:: "
						+ dirPlrPwt);
				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
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
						gameBean.setDrawCancel(rs.getDouble("cancel"));
						gameBean.setDrawPwt(rs.getDouble("pwt"));
					}

					// Direct Player Pwt
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
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
						gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isSE) {
				scratchQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.parent_id,sale,pwt from");
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				boolean isTrue=false;
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					isTrue=true;
					saleTranDG = "(select sale.organization_id parent_id,sale.name,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) sale from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where game_id=?   and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where game_id=?   and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id)sale group by agent_org_id ) sale on organization_id=agent_org_id where organization_type='AGENT') sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where game_id=?   and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id union all	select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction where game_id=?   and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
						+ startDate
						+ "'  and transaction_date<='"
						+ endDate
						+ "' ) group by agent_org_id )saleRet group by agent_org_id ) saleRet on organization_id=agent_org_id where organization_type='AGENT') saleRet on sale.organization_id=saleRet.organization_id) sale left outer join";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id parent_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' and game_id=? group by book_nbr) TktTlb where gm.game_id=? and gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT') sale,";
				}
				pwtTranDG = "(select parent_id,sum(pwt) pwt from (select bb.parent_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id union all select agent_org_id,sum(pwt_amt+comm_amt) boPwt from st_se_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT')) and game_id=? group by agent_org_id) pwt group by parent_id) pwt";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" "+((isTrue)?"on":"where")+" sale.parent_id=pwt.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				logger.debug("For Expand scratch game Qry:: " + scratchQry);

				dirPlrPwt = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id";

				logger.debug("For Expand scratch game Qry Direct Ply:: "
						+ dirPlrPwt);
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
					pstmt.setInt(7, gameId);
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
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
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
						gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isCS) {

				CSQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select sale.parent_id,sale,cancel from ");
				saleTranCS = "(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,";
				cancelTranCS = "(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel";
				CSQry.append(saleTranCS);
				CSQry.append(cancelTranCS);
				CSQry
						.append(" where sale.parent_id=cancel.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				logger.debug("For Expand CS game Qry:: " + CSQry);

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
			if(ReportUtility.isOLA){

				OLAQry = new StringBuilder(
						"select withdraw.agtOrgId, (wdra-wdraRef) withdrawAmt, (depo-depoRef) depositAmt, netAmt netGamingComm from ");
				wdTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(wdra),0.0)wdra from (select ifnull(orw.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdra from st_ola_ret_withdrawl orw where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL' and transaction_date>='"
							+ startDate
							+ "' and transaction_date <='"
							+ endDate 
							+"') and orw.wallet_id = ? group by retId)wd right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wd.retId = om.organization_id group by parent_id)wd,";
				wdRefTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(wdraRef),0.0)wdraRef from (select ifnull(orwRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdraRef from st_ola_ret_withdrawl_refund orwRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL_REFUND' and transaction_date>= '"
							+ startDate
							+ "' and transaction_date <= '"
							+ endDate 
							+ "') and orwRef.wallet_id = ? group by retId)wdRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wdRef.retId = om.organization_id group by parent_id)wdRef";
				depoTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(depo),0.0)depo from (select ifnull(ordepo.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depo from st_ola_ret_deposit ordepo where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate 
						+"') and ordepo.wallet_id = ? group by retId)depo right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depo.retId = om.organization_id group by parent_id)depo,";
				depoRefTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(depoRef),0.0)depoRef from (select ifnull(ordepoRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depoRef from st_ola_ret_deposit_refund ordepoRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT_REFUND' and transaction_date>= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate 
						+ "') and ordepoRef.wallet_id = ? group by retId)depoRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depoRef.retId = om.organization_id group by parent_id)depoRef";
				netGTranOLA ="(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"+ startDate + "' and transaction_date<='"	+ endDate+ "' and wallet_id = ?) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id) netgaming";
				OLAQry.append("(select wd.agtOrgId, wdra, wdraRef from");
				OLAQry.append(wdTranOLA);
				OLAQry.append(wdRefTranOLA);
				OLAQry.append(" where wd.agtOrgId = wdRef.agtOrgId)withdraw,");
				OLAQry.append("(select depo.agtOrgId, depo, depoRef from");
				OLAQry.append(depoTranOLA);
				OLAQry.append(depoRefTranOLA);
				OLAQry.append(" where depo.agtOrgId = depoRef.agtOrgId)deposit, ");
				OLAQry.append(netGTranOLA);
				OLAQry
						.append(" where withdraw.agtOrgId = deposit.agtOrgId and netgaming.agtOrgId = withdraw.agtOrgId and netgaming.agtOrgId = deposit.agtOrgId");
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
					logger.debug("-------Indivisual Wallet Qry-------\n" + pstmt);
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
						gameBean.setOlaWithdrawalAmt(rs.getDouble("withdrawAmt"));
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

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode) throws LMSException {
		return commanMethodOfCollectionAgentWiseWithOpeningBal(deployDate, startDate, endDate,0);
	}
	
	
	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode,int roleId) throws LMSException {
		return commanMethodOfCollectionAgentWiseWithOpeningBal(deployDate, startDate, endDate, roleId);
	}

	private Map<String, CollectionReportOverAllBean> commanMethodOfCollectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, int roleId) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		Connection con = null;
		String agtOrgQry=null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CollectionReportOverAllBean> agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;

		try {
			con = DBConnect.getConnection();
			
			if(roleId!=0){
				agtOrgQry = QueryManager.getOrgQryUsingRoleId("AGENT",roleId);	
			}else{
				agtOrgQry = QueryManager.getOrgQry("AGENT");
			}
			
			
			/*String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";*/
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
				collBean.setAgentName(rsRetOrg.getString("orgCode"));
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
				if(ReportUtility.isOLA){
					collBean.setDeposit(0.0);
					collBean.setDepositRefund(0.0);
					collBean.setWithdrawal(0.0);
					collBean.setWithdrawalRefund(0.0);
					collBean.setNetGamingComm(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			// for calculation of opening Balance
			AgentOpeningBalanceHelper openingHelper=new AgentOpeningBalanceHelper();
			openingHelper.collectionAgentWiseOpenningBal(deployDate, startDate, con,agtMap);
			

			collectionAgentWise(startDate, endDate, con,
					agtMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionAgentWiseWithOpeningBal");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}


	//Unimplemented Method
	public Map<String, AgentCollectionReportOverAllBean> collectionRetailerWiseWithOpeningBal(int userId,
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA) throws LMSException{ return null;}

	public void collectionRetailerWiseExpand(int userId, Timestamp startDate,
			Timestamp endDate, boolean isDG, boolean isSE, boolean isCS,
			boolean isOLA, Map<String, AgentCollectionReportOverAllBean> agtMap)
			throws LMSException {
		// TODO Auto-generated method stub
		
	}
	
	

}
