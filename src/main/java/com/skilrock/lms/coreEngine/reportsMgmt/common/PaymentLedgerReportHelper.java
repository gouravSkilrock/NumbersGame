package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class PaymentLedgerReportHelper implements
		IPaymentLedgerReportAgtWiseHelper {
	Log logger = LogFactory.getLog(PaymentLedgerReportHelper.class);



	public void collectionDateWise(Timestamp startDate, Timestamp endDate,
			Connection con, Map<String, CollectionReportOverAllBean> agtMap,
			int agtOrgId) throws LMSException {

		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		if (startDate.after(endDate)) {
			return;
		}

		try {
			// Get Account Details

			// String addOrgQry =
			// "right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId+" ) om on agent_org_id=organization_id";
			String cashQry = "(select agent_org_id,sum(amount) cash,date(transaction_date) date from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date)) cash";
			String chqQry = "(select agent_org_id,sum(cheque_amt) chq,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date)) chq ";
			String chqRetQry = "(select agent_org_id,sum(cheque_amt) chq_ret,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date)) chq_ret ";

			String debitQry = "(select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date))  debit";
			String creditQry = "(select agent_org_id,sum(amount) credit,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date)) credit";
			String bankQry = "(select agent_org_id,sum(amount) bank,date(transaction_date) date from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and agent_org_id= "
					+ agtOrgId
					+ " group by date(transaction_date))  bank";

			String acc1Qry = "select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ("
					+ cashQry
					+ " left join "
					+ chqQry
					+ " on cash.date=chq.date ) union"

					+ " select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ("
					+ cashQry
					+ " right join "
					+ chqQry
					+ " on cash.date=chq.date )";

			String acc2Qry = "select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from ("
					+ debitQry
					+ " left join "
					+ creditQry
					+ " on debit.date=credit.date ) union"

					+ " select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from ("
					+ debitQry
					+ " right join "
					+ creditQry
					+ " on debit.date=credit.date )";

			String acc3Qry = "select ifnull(chq_ret.date,bank.date) date,ifnull(chq_ret,0.0) chq_ret,ifnull(bank,0.0) bank from ("
					+ chqRetQry
					+ " left join "
					+ bankQry
					+ " on chq_ret.date=bank.date ) union"

					+ " select ifnull(chq_ret.date,bank.date) date,ifnull(chq_ret,0.0) chq_ret,ifnull(bank,0.0) bank from ("
					+ chqRetQry
					+ " right join "
					+ bankQry
					+ " on chq_ret.date=bank.date )";
			pstmt = con.prepareStatement(acc1Qry);
			logger.debug("---Account Detail Query---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String dateFrDtParse = dateformat.format(rs.getDate("date"));
				// String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setCash(rs.getDouble("cash"));
				agtMap.get(dateFrDtParse).setCheque(rs.getDouble("chq"));

			}
			pstmt1 = con.prepareStatement(acc2Qry);
			logger.debug("---Account Detail Query---" + pstmt1);
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String dateFrDtParse = dateformat.format(rs1.getDate("date"));
				// String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setCredit(rs1.getDouble("credit"));
				agtMap.get(dateFrDtParse).setDebit(rs1.getDouble("debit"));

			}

			pstmt = con.prepareStatement(acc3Qry);
			logger.debug("---Account Detail Query---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String dateFrDtParse = dateformat.format(rs.getDate("date"));
				// String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setChequeReturn(
						rs.getDouble("chq_ret"));
				agtMap.get(dateFrDtParse).setBankDep(rs.getDouble("bank"));
			}

			if (ReportUtility.isDG) {
				/*
				 * saleTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')"
				 * ; cancelTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')"
				 * ; pwtTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')"
				 * ;
				 */
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select date(transaction_date) date,parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select date(transaction_date) date,parent_id,sum(cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select date(transaction_date) date,parent_id,sum(pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
									+ startDate
									+ "'and transaction_date<='"
									+ endDate
									+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
									+ agtOrgId
									+ " group by date(transaction_date)) union all ");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
									+ startDate
									+ "'and transaction_date<='"
									+ endDate
									+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
									+ agtOrgId
									+ " group by date(transaction_date)) union all ");

					pwtQry
							.append("(select bb.parent_id,sum(pwt) as pwt,transaction_date from(select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt,rtm.transaction_date from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
									+ startDate
									+ "'and transaction_date<='"
									+ endDate
									+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
									+ agtOrgId
									+ " group by date(transaction_date)) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saletable group by date(transaction_date)");
				cancelQry.append(") cancelTlb group by date(transaction_date)");
				pwtQry.append(") pwtTlb group by date(transaction_date)");
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					// String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setDgSale(rs.getDouble("sale"));

				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					// String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setDgCancel(
							rs.getDouble("cancel"));

				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));

					agtMap.get(dateFrDtParse).setDgPwt(rs.getDouble("pwt"));

				}

				// Draw Direct Player Qry

				String dirPwtQry = "select date(transaction_date) date,agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? and agent_org_id="
						+ agtOrgId + " group by date(transaction_date) ";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger.debug("-------Draw Direct Player Qry------\n" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					// String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setDgDirPlyPwt(
							rs.getDouble("pwtDir"));

				}
			}
			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				String cancelQry = "";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt, date from((select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and sbt.agent_org_id="
							+ agtOrgId
							+ " group by date(transaction_date)) union all "
							+ "(select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_loose_book_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and sbt.agent_org_id="
							+ agtOrgId
							+ " group by date(transaction_date)))saleTlb group by date";

					cancelQry = "select agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt, date from(select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and sbt.agent_org_id="
							+ agtOrgId
							+ " group by date(transaction_date)) union all (select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_loose_book_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and sbt.agent_org_id="
							+ agtOrgId
							+ " group by date(transaction_date)))cancelTlb group by date";

				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select gid.current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt,date(transaction_date) date from "
							+ "st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' and gid.current_owner_id="
							+ agtOrgId + " group by date(transaction_date) ";
				}
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));

					agtMap.get(dateFrDtParse).setSeSale(rs.getDouble("netAmt"));

				}
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					pstmt = con.prepareStatement(cancelQry);
					logger.debug("***Scratch Cancel Query*** \n" + pstmt);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
								"yyyy-MM-dd");
						String dateFrDtParse = dateformat.format(rs
								.getDate("date"));

						agtMap.get(dateFrDtParse).setSeCancel(
								rs.getDouble("netAmt"));

					}
				}

				// Calculate Scratch Pwt
				String pwtQry = "select date(transaction_date) date,parent_id,sum(pwt) as pwt from (select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt,transaction_date from "
						+ " st_se_retailer_pwt srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ? and transaction_type='PWT' and srp.retailer_org_id in "
						+ "(select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
						+ agtOrgId
						+ " group by date(transaction_date) union all select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt,transaction_date from st_se_agent_pwt srp "
						+ "inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ?  and transaction_type='PWT' and srp.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
						+ agtOrgId
						+ " group by date(transaction_date)"
						+ "union all select agent_org_id,sum(pwt_amt+comm_amt) Pwt,transaction_date  from st_se_bo_pwt sbp inner join st_lms_bo_transaction_master btm on sbp.transaction_id=btm.transaction_id and transaction_date>= ? and transaction_date<= ? and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id= "
						+ agtOrgId
						+ ") group by date(transaction_date))pwtTlb group by date(transaction_date) ";
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
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSePwt(rs.getDouble("pwt"));
				}

				// Scratch Direct Player Qry
				String dirPwtQry = "select date(transaction_date) date,agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? and agent_org_id="
						+ agtOrgId + " group by date(transaction_date) ";

				// String dirPwtQry =
				// "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId+") om on agent_org_id=organization_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger
						.debug("-------Scratch Direct Player Qry------\n"
								+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSeDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if (ReportUtility.isCS) {

				/*
				 * saleTranCS =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')"
				 * ; cancelTranCS =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')"
				 * ;
				 */
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();

				StringBuilder saleQry = new StringBuilder(
						"select date(transaction_date) date,parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select date(transaction_date) date,parent_id,sum(cancel) as cancel from (");

				while (rsProduct.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_cs_sale_"
									+ rsProduct.getInt("category_id")
									+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_SALE') and transaction_date>='"
									+ startDate
									+ "'and transaction_date<='"
									+ endDate
									+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
									+ agtOrgId
									+ " group by date(transaction_date)) union all ");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_cs_refund_"
									+ rsProduct.getInt("category_id")
									+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
									+ startDate
									+ "'and transaction_date<='"
									+ endDate
									+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
									+ agtOrgId
									+ " group by date(transaction_date)) union all ");

					/*
					 * saleQry.append(
					 * "(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_"
					 * + rsProduct.getInt("category_id") +
					 * " drs where transaction_id in (" + saleTranCS +
					 * ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all"
					 * );
					 * 
					 * cancelQry.append(
					 * "(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_"
					 * + rsProduct.getInt("category_id") +
					 * " drs where transaction_id in (" + cancelTranCS +
					 * ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all"
					 * );
					 */
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());

				saleQry.append(") saleTlb group by date(transaction_date)");
				cancelQry.append(") cancelTlb group by date(transaction_date)");

				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n" + cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					// String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setDgSale(rs.getDouble("sale"));

				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String dateFrDtParse = dateformat
							.format(rs.getDate("date"));
					// String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setCSCancel(
							rs.getDouble("cancel"));

				}
			}
			if (ReportUtility.isOLA) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder wdQry = new StringBuilder(
						"select parent_id,sum(wdrawAmt) as wdraw,date(transaction_date) date from");
				StringBuilder wdRefQry = new StringBuilder(
						"select parent_id,sum(wdrawRefAmt) as wdrawRef,date(transaction_date) date from");// wdrawRef
				StringBuilder depQry = new StringBuilder(
						"select parent_id,sum(depo) as depoAmt,date(transaction_date) date from");
				StringBuilder depRefQry = new StringBuilder(
						"select parent_id,sum(depoRef) as depoRefAmt,date(transaction_date) date from");

				wdQry
						.append("(select drs.retailer_org_id,sum(agent_net_amt) as wdrawAmt,rtm.transaction_date from st_ola_ret_withdrawl drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_WITHDRAWL') and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
								+ agtOrgId + " group by date(transaction_date)");

				wdRefQry
						.append("(select drs.retailer_org_id,sum(agent_net_amt) as wdrawRefAmt,rtm.transaction_date from st_ola_ret_withdrawl_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_WITHDRAWL_REFUND') and transaction_date>='"
								+ startDate
								+ "'and transaction_date<='"
								+ endDate
								+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
								+ agtOrgId + " group by date(transaction_date)");
				depQry
						.append("(select drs.retailer_org_id,sum(agent_net_amt) as depo,rtm.transaction_date from st_ola_ret_deposit drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_DEPOSIT') and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
								+ agtOrgId + " group by date(transaction_date)");

				depRefQry
						.append("(select drs.retailer_org_id,sum(agent_net_amt) as depoRef,rtm.transaction_date from st_ola_ret_deposit_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="
								+ agtOrgId + " group by date(transaction_date)");

				String netGamingQry = "select om.parent_id parent_id, sum(netGamingAmt) netAmt,date(transaction_date) date from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt,transaction_date from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"+ startDate+ "' and transaction_date<='"+ endDate+ "') wdret inner join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id where om.parent_id="+ agtOrgId + " group by date(transaction_date)";
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
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setWithdrawal(rs.getDouble("wdraw"));
				}
				// WithDrawal Refund Query
				pstmt = con.prepareStatement(wdRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setWithdrawalRefund(rs.getDouble("wdrawRef"));
				}

				// Deposit Query
				pstmt = con.prepareStatement(depQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setDeposit(rs.getDouble("depoAmt"));
				}
				// Deposit Refund Query
				pstmt = con.prepareStatement(depRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setDepositRefund(rs.getDouble("depoRefAmt"));
				}

				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setNetGamingComm(rs.getDouble("netAmt"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}

	public void collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con, Map<String, CollectionReportOverAllBean> agtMap,
			int agtOrgId) throws LMSException {

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

			String addOrgQry = "right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
					+ agtOrgId + " ) om on agent_org_id=organization_id";
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
				// String agtOrgId = rs.getString("organization_id");
				agtMap.get(agtOrgId + "").setCash(rs.getDouble("cash"));
				agtMap.get(agtOrgId + "").setCheque(rs.getDouble("chq"));
				agtMap.get(agtOrgId + "").setChequeReturn(
						rs.getDouble("chq_ret"));
				agtMap.get(agtOrgId + "").setCredit(rs.getDouble("credit"));
				agtMap.get(agtOrgId + "").setDebit(rs.getDouble("debit"));
				agtMap.get(agtOrgId + "").setBankDep(rs.getDouble("bank"));
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
				String gameQry = "select game_id from st_dg_game_master";
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
						.append(") saleTlb group by parent_id) saleTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
								+ agtOrgId
								+ ") om on parent_id=organization_id");
				cancelQry
						.append(") cancelTlb group by parent_id) cancelTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
								+ agtOrgId
								+ ") om on parent_id=organization_id");
				pwtQry
						.append(") pwtTlb group by parent_id) pwtTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
								+ agtOrgId
								+ ") om on parent_id=organization_id");
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

				String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
						+ agtOrgId + ") om on agent_org_id=organization_id";
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
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select sale.organization_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select sale1.agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_loose_book_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by agent_org_id)sale1 group by agent_org_id )sale on organization_id=agent_org_id where organization_type='AGENT')sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select cancel1.agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and agent_org_id="
							+ agtOrgId
							+ " group by agent_org_id)cancel1 group by agent_org_id) saleRet on organization_id=agent_org_id where organization_type='AGENT') saleRet on sale.organization_id=saleRet.organization_id";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT' and organization_id="
							+ agtOrgId;
				}
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSeSale(
							rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String pwtQry = "select organization_id,ifnull(sum(pwt),0.0) pwt from (select bb.parent_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id union all select agent_org_id,sum(pwt_amt+comm_amt) boPwt from st_se_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
						+ agtOrgId
						+ ")) group by agent_org_id) pwtTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
						+ agtOrgId
						+ ") om on parent_id=organization_id group by organization_id";
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

				String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
						+ agtOrgId + ") om on agent_org_id=organization_id";
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
						.append(") saleTlb group by parent_id) saleTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
								+ agtOrgId
								+ ") om on parent_id=organization_id");
				cancelQry
						.append(") cancelTlb group by parent_id) cancelTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="
								+ agtOrgId
								+ ") om on parent_id=organization_id");

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
			if (ReportUtility.isOLA) {

				StringBuilder wdQry = new StringBuilder(
						"select parent_id agtOrgId,wdraw from ");
				StringBuilder wdRefQry = new StringBuilder(
						"select parent_id agtOrgId,wdrawRef from ");
				StringBuilder depQry = new StringBuilder(
						"select parent_id agtOrgId,depoAmt from ");
				StringBuilder depRefQry = new StringBuilder(
						"select parent_id agtOrgId,depoRefAmt from");

				wdQry
						.append("(select parent_id,ifnull(sum(withd),0.0) wdraw from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as withd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "'))withdRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
								+ agtOrgId + " group by parent_id) withRef");

				wdRefQry
						.append("(select parent_id,ifnull(sum(withd),0.0) wdrawRef from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as withd from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "'))withdRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
								+ agtOrgId + " group by parent_id) withRef");
				depQry
						.append("(select parent_id,ifnull(sum(depo),0.0) depoAmt from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "'))deposit on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
								+ agtOrgId + " group by parent_id) depo");

				depRefQry
						.append("(select parent_id,ifnull(sum(depo),0.0) depoRefAmt from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "'))depositRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
								+ agtOrgId + " group by parent_id) depoRef");

				String netGamingQry = "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"+ startDate + "' and transaction_date<='"	+ endDate+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id where om.parent_id="+agtOrgId+" group by om.parent_id";
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
					agtMap.get(rs.getString("agtOrgId" + "")).setWithdrawal(
							rs.getDouble("wdraw"));
				}
				// WithDrawal Refund Query
				pstmt = con.prepareStatement(wdRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId" + ""))
							.setWithdrawalRefund(rs.getDouble("wdrawRef"));
				}

				// Deposit Query
				pstmt = con.prepareStatement(depQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId" + "")).setDeposit(
							rs.getDouble("depoAmt"));
				}
				// Deposit Refund Query
				pstmt = con.prepareStatement(depRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId" + "")).setDepositRefund(
							rs.getDouble("depoRefAmt"));
				}

				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId" + "")).setNetGamingComm(
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
		StringBuilder drawQry = null;
		StringBuilder scratchQry = null;
		StringBuilder CSQry = null;
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
				boolean isTrue = false;
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					isTrue = true;
					saleTranDG = "(select sale.organization_id parent_id,sale.name,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) sale from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where game_id=? and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') group by agent_org_id) sale on organization_id=agent_org_id where organization_type='AGENT') sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where game_id=? and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') group by agent_org_id ) saleRet on organization_id=agent_org_id where organization_type='AGENT') saleRet on sale.organization_id=saleRet.organization_id) sale left outer join ";
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
						.append(" "
								+ ((isTrue) ? "on" : "where")
								+ " sale.parent_id=pwt.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
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
						prodBean.setDrawSale(rs.getDouble("sale"));
						prodBean.setDrawPwt(rs.getDouble("cancel"));

						System.out.println("map size:" + prodMap.size());
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
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,
			int agtOrgId) throws LMSException {

		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CollectionReportOverAllBean> agtMapOpenningBalance = new LinkedHashMap<String, CollectionReportOverAllBean>();
		Map<String, CollectionReportOverAllBean> agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		// Map<String, CollectionReportOverAllBean> resultMap = new
		// LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;
		Double openingBalance = 0.0;
		try {
			con = DBConnect.getConnection();
			// String agtOrgQry =
			// "select name,organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId;
			// pstmt = con.prepareStatement(agtOrgQry);
			// rsRetOrg = pstmt.executeQuery();

			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			Calendar nextCal = Calendar.getInstance();
			startCal.setTimeInMillis(startDate.getTime());
			endCal.setTimeInMillis(endDate.getTime());
			nextCal.setTimeInMillis(startDate.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			collBean = new CollectionReportOverAllBean();
			collBean.setAgentName(new Timestamp(nextCal.getTimeInMillis())
					.toString().split(" ")[0]);// its date for this report
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
			agtMapOpenningBalance.put(agtOrgId + "", collBean);

			collectionAgentWise(deployDate, new Timestamp(
					startDate.getTime() - 1000), con,
					agtMapOpenningBalance, agtOrgId);
			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr = agtMapOpenningBalance
					.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, CollectionReportOverAllBean> pair = itr
						.next();
				CollectionReportOverAllBean bean = pair.getValue();
				double openingBal = bean.getDgSale()
						- bean.getDgCancel()
						- bean.getDgPwt()
						- bean.getDgDirPlyPwt()
						+ bean.getSeSale()
						- bean.getSePwt()
						- bean.getSeDirPlyPwt()
						+ bean.getCSSale()
						- bean.getCSCancel()
						+ bean.getDeposit()
						- bean.getDepositRefund()
						- bean.getWithdrawal()
						- bean.getNetGamingComm()
						- (bean.getCash() + bean.getCheque() + bean.getCredit()
								+ bean.getBankDep() - bean.getDebit() - bean
								.getChequeReturn());
				bean.setOpeningBal(openingBal);
				openingBalance = openingBal;
			}
			if (endCal.after(startCal)) {
				while (nextCal.compareTo(endCal) <= 0) {

					collBean = new CollectionReportOverAllBean();
					String AgentName = new Timestamp(nextCal.getTimeInMillis())
							.toString().split(" ")[0];// its date for this
					// report
					collBean.setAgtId(agtOrgId);
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
					agtMap.put(AgentName, collBean);

					nextCal.add(Calendar.DAY_OF_MONTH, 1);// increment the date

				}
			}
			System.out.println("***agentMap***" + agtMap);

			collectionDateWise(startDate, endDate, con,agtMap, agtOrgId);

			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr1 = agtMap
					.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry<String, CollectionReportOverAllBean> pair = itr1
						.next();
				CollectionReportOverAllBean bean = pair.getValue();
				double openingBal = bean.getDgSale()
						- bean.getDgCancel()
						- bean.getDgPwt()
						- bean.getDgDirPlyPwt()
						+ bean.getSeSale()
						- bean.getSeCancel()
						- bean.getSePwt()
						- bean.getSeDirPlyPwt()
						+ bean.getCSSale()
						- bean.getCSCancel()
						+ bean.getDeposit()
						- bean.getDepositRefund()
						- bean.getWithdrawal()
						- bean.getNetGamingComm()
						- (bean.getCash() + bean.getCheque() + bean.getCredit()
								+ bean.getBankDep() - bean.getDebit() - bean
								.getChequeReturn()) + openingBalance;
				bean.setOpeningBal(openingBalance);
				openingBalance = openingBal;
			}

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

	


}