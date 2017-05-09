package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.accMgmt.common.RetailerOpeningBalanceHelper;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.controller.IWRetailerReportsControllerImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLERetailerReportsControllerImpl;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.controller.VSRetailerReportsControllerImpl;

public class PaymentLedgerRetailerWiseReportHelper {
	Log logger = LogFactory.getLog(PaymentLedgerRetailerWiseReportHelper.class);

	
	
	public void collectionDateWise(Timestamp startDate, Timestamp endDate,
			Connection con,Map<String, CollectionReportOverAllBean> agtMap, int agtOrgId,int retOrgId)
			throws LMSException {

		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		if (startDate.after(endDate)) {
			return;
		}

		try {
			// Get Account Details

			//String addOrgQry = "right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId+" ) om on agent_org_id=organization_id";
			String cashQry = "(select party_id,sum(amount) cash,date(transaction_date) date from st_lms_agent_cash_transaction cash,st_lms_agent_transaction_master atm where cash.transaction_id=atm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and party_id= "+retOrgId+ " group by date(transaction_date)) cash";
			String chqQry = "(select party_id,sum(cheque_amt) chq,date(transaction_date) date from st_lms_agent_sale_chq chq,st_lms_agent_transaction_master atm where chq.transaction_id=atm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and party_id= "+retOrgId+ " group by date(transaction_date)) chq ";
			String chqRetQry = "select sum(cheque_amt) chq_ret,date(transaction_date) date from st_lms_agent_sale_chq chq,st_lms_agent_transaction_master atm where chq.transaction_id=atm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and party_id= "+retOrgId+ " group by date(transaction_date)  ";
					
					
			String debitQry = "(select party_id,sum(amount) debit,date(transaction_date) date from st_lms_agent_debit_note debit,st_lms_agent_transaction_master atm where debit.transaction_id=atm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and party_id= "+retOrgId+ " group by date(transaction_date))  debit";
			String creditQry = "(select party_id,sum(amount) credit,date(transaction_date) date from st_lms_agent_credit_note credit,st_lms_agent_transaction_master atm where credit.transaction_id=atm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and party_id= "+retOrgId+ " group by date(transaction_date)) credit";
			String bankQry = "select sum(amount) bank,date(transaction_date) date from st_lms_agent_bank_deposit_transaction bank,st_lms_agent_transaction_master atm where bank.transaction_id=atm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "'and retailer_org_id= "+retOrgId+ " group by date(transaction_date) ";
			
			
			String acc1Qry = "select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ("
					+ cashQry
					+ " left join "
					+ chqQry
					+" on cash.date=chq.date ) union"
					
					+" select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ("
					+ cashQry
					+ " right join "
					+ chqQry
					+" on cash.date=chq.date )";
					
					
			String acc2Qry = "select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from ("
				+ debitQry
				+ " left join "
				+ creditQry
				+" on debit.date=credit.date ) union"
							
				+" select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from ("
				+ debitQry
				+ " right join "
				+ creditQry
				+" on debit.date=credit.date )";
			
			String acc3Qry=chqRetQry;

			String acc4Qry=bankQry;

			if (LMSFilterDispatcher.isRepFrmSP) {
				String cashChqArchQry="union all select finaldate,sum(cash_amt) as cash ,sum(cheque_amt) as chq from st_rep_agent_payments where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by finaldate";
				String creditDebitArchQry="union all select finaldate,sum(debit_note) as debit ,sum(credit_note) as credit from st_rep_agent_payments where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by finaldate";
				String chqRetArchQry="union all select sum(cheque_bounce_amt) as chq_ret,finaldate  from st_rep_agent_payments where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by finaldate";
				String bankDepositArchQry="union all select sum(bank_deposit) as bank, finaldate from st_rep_agent_payments where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by finaldate";

				acc1Qry=acc1Qry+cashChqArchQry;
				acc2Qry=acc2Qry+creditDebitArchQry;
				acc3Qry=acc3Qry+chqRetArchQry;
				acc4Qry=acc4Qry+bankDepositArchQry;
			}
			
			
			pstmt = con.prepareStatement(acc1Qry);
			logger.debug("---Account Detail Query---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				//String agtOrgId = rs.getString("organization_id");
			
				agtMap.get(dateFrDtParse).setCash(agtMap.get(dateFrDtParse).getCash()+ rs.getDouble("cash"));
				agtMap.get(dateFrDtParse).setCheque(agtMap.get(dateFrDtParse).getCheque()+ rs.getDouble("chq"));
				
				
			}
			pstmt1 = con.prepareStatement(acc2Qry);
			logger.debug("---Account Detail Query---" + pstmt1);
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs1.getDate("date"));
				//String agtOrgId = rs.getString("organization_id");
			
				agtMap.get(dateFrDtParse).setCredit(agtMap.get(dateFrDtParse).getCredit()+ rs1.getDouble("credit"));
				agtMap.get(dateFrDtParse).setDebit(agtMap.get(dateFrDtParse).getDebit()+ rs1.getDouble("debit"));
				
			}
			
			pstmt = con.prepareStatement(acc3Qry);
			logger.debug("---Account Detail Query---" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setChequeReturn(agtMap.get(dateFrDtParse).getChequeReturn()+ rs.getDouble("chq_ret"));
			}

			pstmt = con.prepareStatement(acc4Qry);
			logger.debug("---Account Detail Bank Deposit Query---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setBankDep(agtMap.get(dateFrDtParse).getBankDep()+ rs.getDouble("bank"));
			}

			if (ReportUtility.isDG) {
				/*saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
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
*/
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select date(transaction_date) date,retailer_org_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select date(transaction_date) date,retailer_org_id,sum(cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select date(transaction_date) date,retailer_org_id,sum(pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry.append("(select drs.retailer_org_id,sum(net_amt) as sale,rtm.transaction_date from st_dg_ret_sale_" 
							+ rsGame.getInt("game_id")
					+" drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='" +
					 startDate 		
					+"'and transaction_date<='" 
					+ endDate
						+	"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)) union all ");
					
					
					cancelQry.append("(select drs.retailer_org_id,sum(net_amt) as cancel,rtm.transaction_date from st_dg_ret_sale_refund_" 
							+ rsGame.getInt("game_id")
					+" drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='" +
					 startDate 		
					+"'and transaction_date<='" 
					+ endDate
						+	"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)) union all ");
					
				pwtQry.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm-govt_claim_comm) as pwt,rtm.transaction_date from st_dg_ret_pwt_" 
							+ rsGame.getInt("game_id")
					+" drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='" +
					 startDate 		
					+"'and transaction_date<='" 
					+ endDate
						+	"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)) union all");

			

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry
						.append(") saletable group by date(transaction_date)");
				cancelQry
						.append(") cancelTlb group by date(transaction_date)");
				pwtQry
						.append(") pwtTlb group by date(transaction_date)");
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				
				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry="union all select finaldate,organization_id,sum(sale_net) as sale from st_rep_dg_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					String cancelDebitArchQry="union all select finaldate,organization_id,sum(ref_net_amt) as cancel from st_rep_dg_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					String pwtArchQry="union all select finaldate,organization_id,sum(pwt_net_amt) as pwt  from st_rep_dg_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
				 saleQry.append(saleArchQry);
				 cancelQry.append(cancelDebitArchQry);
				 pwtQry.append(pwtArchQry);
				 
				
				
				}
				
				
				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");
				
				agtMap.get(dateFrDtParse).setDgSale(agtMap.get(dateFrDtParse).getDgSale()+ rs.getDouble("sale"));
					
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");
				
				agtMap.get(dateFrDtParse).setDgCancel(agtMap.get(dateFrDtParse).getDgCancel()+ 
						rs.getDouble("cancel"));
					
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
									
				agtMap.get(dateFrDtParse).setDgPwt(agtMap.get(dateFrDtParse).getDgPwt()+ rs.getDouble("pwt"));
					
				}

				/*// Draw Direct Player Qry

				String dirPwtQry = "select date(transaction_date) date,agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? and agent_org_id="+agtOrgId+" group by date(transaction_date) ";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger.debug("-------Draw Direct Player Qry------\n" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");
				
				agtMap.get(dateFrDtParse).setDgDirPlyPwt(
								rs.getDouble("pwtDir"));
			
				}*/
			}
			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				String cancelQry="";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry="select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt, date from ((select srt.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(atm.transaction_date) date from st_se_agent_retailer_transaction srt,st_lms_agent_transaction_master atm where srt.transaction_id=atm.transaction_id and atm.transaction_type ='SALE' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and srt.retailer_org_id="+retOrgId +" group by date(transaction_date))  "
					    +"union all (select srt.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(atm.transaction_date) date from st_se_agent_ret_loose_book_transaction srt,st_lms_agent_transaction_master atm where srt.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and srt.retailer_org_id="+retOrgId +" group by date(transaction_date)))saleTlb group by date";
					
					
				 cancelQry="select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt, date from ((select srt.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(atm.transaction_date) date from st_se_agent_retailer_transaction srt,st_lms_agent_transaction_master atm where srt.transaction_id=atm.transaction_id and atm.transaction_type ='SALE_RET' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and srt.retailer_org_id="+retOrgId +" group by date(transaction_date)) union all"
					    +"(select srt.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(atm.transaction_date) date from st_se_agent_ret_loose_book_transaction srt,st_lms_agent_transaction_master atm where srt.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE_RET' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and srt.retailer_org_id="+retOrgId +" group by date(transaction_date)))cancelTlb group by date";
				
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select gid.current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt,date(transaction_date) date from "
                              +"st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='" 
                              + startDate
                              +"' and date<='" 
                              + endDate
                              +"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' and gid.current_owner_id="+ agtOrgId +" group by date(transaction_date) ";
				}
				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry=" union all select organization_id,sum(sale_book_mrp),sum(sale_book_net),finaldate as sale from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					String cancelDebitArchQry=" union all select organization_id,sum(ref_sale_mrp),sum(ref_net_amt) as cancel,finaldate from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					//String cancelDebitArchQry="union all select finaldate,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
				 saleQry=saleQry+saleArchQry;
				 cancelQry=cancelQry+cancelDebitArchQry;
				}
				
				
				
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
									
				agtMap.get(dateFrDtParse).setSeSale(agtMap.get(dateFrDtParse).getSeSale()+ rs.getDouble("netAmt"));
					
				}
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				pstmt = con.prepareStatement(cancelQry);
				logger.debug("***Scratch Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
									
				agtMap.get(dateFrDtParse).setSeCancel(agtMap.get(dateFrDtParse).getSeCancel()+ rs.getDouble("netAmt"));
					
				}
				}
				
				// Calculate Scratch Pwt
				String pwtQry = "select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt,transaction_date as date from  st_se_retailer_pwt srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ? and transaction_type='PWT' and srp.retailer_org_id ="+retOrgId+"  group by date(transaction_date) " 
				              + "union all select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt,transaction_date from st_se_agent_pwt srp inner join st_lms_agent_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ?  and transaction_type='PWT' and srp.retailer_org_id ="+retOrgId+" group by date(transaction_date) ";
				if (LMSFilterDispatcher.isRepFrmSP) {
					String pwtArchQry="union all select organization_id,sum(pwt_net_amt) as pwt,finaldate from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					pwtQry=pwtQry+pwtArchQry;
				 
				}
				
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setTimestamp(3, startDate);
				pstmt.setTimestamp(4, endDate);
				
				
				
				
				
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSePwt(agtMap.get(dateFrDtParse).getSePwt()+ rs.getDouble("pwt"));
					}

				/*// Scratch Direct Player Qry
				String dirPwtQry = "select date(transaction_date) date,agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? and agent_org_id="+agtOrgId+" group by date(transaction_date) ";
				
					
			
				
				//String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId+") om on agent_org_id=organization_id";
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
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSeDirPlyPwt(rs.getDouble("pwtDir"));
				}*/
			}
			if (ReportUtility.isCS) {

				
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				
				StringBuilder saleQry = new StringBuilder(
				"select date(transaction_date) date,retailer_org_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
				"select date(transaction_date) date,retailer_org_id,sum(cancel) as cancel from (");
		
				while (rsProduct.next()) {
					saleQry.append("(select drs.retailer_org_id,sum(net_amt) as sale,rtm.transaction_date from st_cs_sale_" 
							+ rsProduct.getInt("category_id")
					+" drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_SALE') and transaction_date>='" +
					 startDate 		
					+"'and transaction_date<='" 
					+ endDate
						+	"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)) union all ");
					
					
					cancelQry.append("(select drs.retailer_org_id,sum(net_amt) as cancel,rtm.transaction_date from st_cs_refund_" 
							+ rsProduct.getInt("category_id")
					+" drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='" +
					 startDate 		
					+"'and transaction_date<='" 
					+ endDate
						+	"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)) union all ");
					
				
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());

				saleQry
						.append(") saleTlb group by date(transaction_date)");
				cancelQry
						.append(") cancelTlb group by date(transaction_date)");

				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n" + cancelQry);

				
				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry=" union all select finaldate,organization_id,sum(sale_net) as sale from st_rep_cs_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					String cancelDebitArchQry=" union all select finaldate,organization_id,sum(ref_net_amt) as cancel from st_rep_cs_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					
				 saleQry.append(saleArchQry);
				 cancelQry.append(cancelDebitArchQry);
				}
				
				
				
				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");
				
				agtMap.get(dateFrDtParse).setCSSale(agtMap.get(dateFrDtParse).getCSSale()+ rs.getDouble("sale"));
					
					
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");
				
				agtMap.get(dateFrDtParse).setCSCancel(agtMap.get(dateFrDtParse).getCSCancel()+
						rs.getDouble("cancel"));
					
				}
			}
			if (ReportUtility.isOLA) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			StringBuilder wdQry = new StringBuilder(
					"select drs.retailer_org_id,sum(net_amt) as withAmt,rtm.transaction_date date ");
			StringBuilder wdRefQry = new StringBuilder(
					"select drs.retailer_org_id,sum(net_amt) as withRefAmt,rtm.transaction_date date from ");
			StringBuilder depQry = new StringBuilder(
					"select drs.retailer_org_id,sum(net_amt) as depoAmt,rtm.transaction_date date from ");
			StringBuilder depRefQry = new StringBuilder(
					"select drs.retailer_org_id,sum(net_amt) as depoRefAmt,rtm.transaction_date date from ");

			wdQry
					.append("from st_ola_ret_withdrawl drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_WITHDRAWL') and transaction_date>='"+startDate+"'and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)");

			wdRefQry
					.append("st_ola_ret_withdrawl_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_WITHDRAWL_REFUND') and transaction_date>='"+startDate+"'and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)");
			depQry
					.append("st_ola_ret_deposit drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_DEPOSIT') and transaction_date>='"+startDate+"'and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)");

			depRefQry
					.append("st_ola_ret_deposit_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"+startDate+"'and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)");

			String netGamingQry = "select drs.retailer_org_id,sum(retailer_net_claim_comm) as netAmt,rtm.transaction_date date from st_ola_ret_comm drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('OLA_COMMISSION') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by date(transaction_date)";
			
			StringBuilder mainQuery=null;
			StringBuilder unionQuery=null;
			
			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select retailer_org_id,sum(withAmt) withAmt ,date from(");
				unionQuery=new StringBuilder(" union all select organization_id retailer_org_id,sum(withdrawal_net_amt) withAmt,finaldate from st_rep_ola_retailer  where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id ="+retOrgId+" group by finaldate) wthtable group by date");
				mainQuery.append(wdQry.toString()).append(unionQuery.toString());
				pstmt = con.prepareStatement(mainQuery.toString());
			} else{
				pstmt = con.prepareStatement(wdQry.toString());
			}
			
			logger.debug("-------Withdrawal Query------\n" + pstmt);
			
			// OLA Withdrawal Query
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setWithdrawal(agtMap.get(dateFrDtParse).getWithdrawal()+
						rs.getDouble("withAmt"));
			}
			
			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select retailer_org_id,sum(withRefAmt) withRefAmt,date from(");
				unionQuery=new StringBuilder(" union all select organization_id retailer_org_id,sum(ref_withdrawal_net_amt) withRefAmt,finaldate from st_rep_ola_retailer  where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id ="+retOrgId+" group by finaldate) wthtable group by date");
				mainQuery.append(wdRefQry.toString()).append(unionQuery.toString());
				pstmt = con.prepareStatement(mainQuery.toString());
			} else{
				pstmt = con.prepareStatement(wdRefQry.toString());
			}
			
			logger.debug("-------WithDrawal Refund Query------\n" + pstmt);
			// OLA Withdrawal Refund Query
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setWithdrawalRefund(agtMap.get(dateFrDtParse).getWithdrawalRefund()+
						rs.getDouble("withRefAmt"));
			}

			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select retailer_org_id,sum(depoAmt) depoAmt ,date from(");
				unionQuery=new StringBuilder(" union all select organization_id retailer_org_id,sum(deposit_net) depoAmt,finaldate from st_rep_ola_retailer  where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id ="+retOrgId+" group by finaldate) deptbl group by date");
				mainQuery.append(depQry.toString()).append(unionQuery.toString());
				pstmt = con.prepareStatement(mainQuery.toString());
			} else{
				pstmt = con.prepareStatement(depQry.toString());
			}
			
			logger.debug("-------Deposit Query------\n" + pstmt);
			
			// OLA Deposit Query
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
			agtMap.get(dateFrDtParse).setDeposit(agtMap.get(dateFrDtParse).getDeposit()+
					rs.getDouble("depoAmt"));
			}
			
			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select retailer_org_id,sum(depoRefAmt) depoRefAmt ,date from(");
				unionQuery=new StringBuilder(" union all select organization_id retailer_org_id,sum(ref_deposit_net_amt) depoRefAmt,finaldate from st_rep_ola_retailer  where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id ="+retOrgId+" group by finaldate) deptbl group by date");
				mainQuery.append(depRefQry.toString()).append(unionQuery.toString());
				pstmt = con.prepareStatement(mainQuery.toString());
			} else{
				pstmt = con.prepareStatement(depRefQry.toString());
			}
			
			
			logger.debug("-------Deposit Refund Query------\n" + pstmt);
			// OLA Deposit Refund Query

			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setDepositRefund(agtMap.get(dateFrDtParse).getDepositRefund()+
						rs.getDouble("depoRefAmt"));
			}
		
			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select retailer_org_id,sum(netAmt) netAmt,date from(");
				unionQuery=new StringBuilder(" union all select organization_id retailer_org_id,sum(net_gaming_net_comm) netAmt,finaldate from st_rep_ola_retailer  where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id ="+retOrgId+" group by finaldate) comtbl group by date");
				mainQuery.append(netGamingQry.toString()).append(unionQuery.toString());
				pstmt = con.prepareStatement(mainQuery.toString());
			} else{
				pstmt = con.prepareStatement(netGamingQry.toString());
			}
			
			logger.debug("-------Net Gaming Query------\n" + pstmt);

			// OLA Net Gaming Query
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				agtMap.get(dateFrDtParse).setNetGamingComm(agtMap.get(dateFrDtParse).getNetGamingComm()+
						rs.getDouble("netAmt"));
			}
				
			}

			if (ReportUtility.isSLE) { 
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(retOrgId);
				
				Map<String, SLEOrgReportResponseBean> map = SLERetailerReportsControllerImpl.fetchSaleCancelPwtDateWiseSingleRetailerAllGame(requestBean, con);
				for (Map.Entry<String, SLEOrgReportResponseBean> entry : map.entrySet()) {
					SLEOrgReportResponseBean bean = entry.getValue();
					agtMap.get(entry.getKey()).setSleSale(agtMap.get(entry.getKey()).getSleSale()+ bean.getSaleAmt());
					agtMap.get(entry.getKey()).setSleCancel(agtMap.get(entry.getKey()).getSleCancel()+ bean.getCancelAmt());
					agtMap.get(entry.getKey()).setSlePwt(agtMap.get(entry.getKey()).getSlePwt()+ bean.getPwtAmt());
				}
			}
			if (ReportUtility.isIW) { 
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(retOrgId);
				
				Map<String, IWOrgReportResponseBean> map = IWRetailerReportsControllerImpl.fetchSaleCancelPwtDateWiseSingleRetailerAllGame(requestBean, con);
				for (Map.Entry<String, IWOrgReportResponseBean> entry : map.entrySet()) {
					IWOrgReportResponseBean bean = entry.getValue();
					agtMap.get(entry.getKey()).setIwSale(agtMap.get(entry.getKey()).getIwSale()+ bean.getSaleAmt());
					agtMap.get(entry.getKey()).setIwCancel(agtMap.get(entry.getKey()).getIwCancel()+ bean.getCancelAmt());
					agtMap.get(entry.getKey()).setIwPwt(agtMap.get(entry.getKey()).getIwPwt()+ bean.getPwtAmt());
				}
			}
			if (ReportUtility.isVS) { 
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(retOrgId);
				
				Map<String, VSOrgReportResponseBean> map = VSRetailerReportsControllerImpl.fetchSaleCancelPwtDateWiseSingleRetailerAllGame(requestBean, con);
				for (Map.Entry<String, VSOrgReportResponseBean> entry : map.entrySet()) {
					VSOrgReportResponseBean bean = entry.getValue();
					agtMap.get(entry.getKey()).setVsSale(agtMap.get(entry.getKey()).getVsSale()+ bean.getSaleAmt());
					agtMap.get(entry.getKey()).setVsCancel(agtMap.get(entry.getKey()).getVsCancel()+ bean.getCancelAmt());
					agtMap.get(entry.getKey()).setVsPwt(agtMap.get(entry.getKey()).getVsPwt()+ bean.getPwtAmt());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		} finally {
			
		}
	}
	
	

	public Map<String, CollectionReportOverAllBean> collectionRetailerWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,int agtOrgId,int retOrgId, ReportStatusBean reportStatusBean) throws LMSException {
		
		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CollectionReportOverAllBean> agtMapOpenningBalance = new LinkedHashMap<String, CollectionReportOverAllBean>();
		Map<String, CollectionReportOverAllBean> agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		//Map<String, CollectionReportOverAllBean> resultMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;
         Double openingBalance=0.0;
		try {
			if ("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();
			//String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId;
			//pstmt = con.prepareStatement(agtOrgQry);
			//rsRetOrg = pstmt.executeQuery();
			
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			Calendar nextCal = Calendar.getInstance();
			startCal.setTimeInMillis(startDate.getTime());
			endCal.setTimeInMillis(endDate.getTime());
			nextCal.setTimeInMillis(startDate.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//nextCal.add(Calendar.DAY_OF_MONTH, 1);
		//	startDate = new Timestamp(sdf.parse(sdf.format(startCal.getTime())).getTime());
		//	endDate = new Timestamp(sdf.parse(sdf.format(endCal.getTime())).getTime());
			collBean = new CollectionReportOverAllBean();
			collBean.setAgentName(new Timestamp(nextCal.getTimeInMillis()).toString().split(" ")[0]);//its date for this report
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
			if (ReportUtility.isIW) {
				collBean.setIwSale(0.0);
				collBean.setIwPwt(0.0);
				collBean.setIwCancel(0.0);
				collBean.setIwDirPlyPwt(0.0);
			}
			if (ReportUtility.isVS) {
				collBean.setVsSale(0.0);
				collBean.setVsPwt(0.0);
				collBean.setVsCancel(0.0);
				collBean.setVsDirPlyPwt(0.0);
			}
			agtMapOpenningBalance.put(agtOrgId+"", collBean);
			RetailerOpeningBalanceHelper openingHelper=new RetailerOpeningBalanceHelper();
			openingBalance=openingHelper.getRetailerOpeningBal(deployDate, startDate, retOrgId, agtOrgId, con);
			
			if (endCal.after(startCal)) {
				while(nextCal.compareTo(endCal)<=0){
					
				collBean = new CollectionReportOverAllBean();
				String AgentName=new Timestamp(nextCal.getTimeInMillis()).toString().split(" ")[0];//its date for this report
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
				if(ReportUtility.isOLA){
					collBean.setDeposit(0.0);
					collBean.setDepositRefund(0.0);
					collBean.setWithdrawal(0.0);
					collBean.setWithdrawalRefund(0.0);
					collBean.setNetGamingComm(0.0);
				}
				if (ReportUtility.isIW) {
					collBean.setIwSale(0.0);
					collBean.setIwPwt(0.0);
					collBean.setIwCancel(0.0);
					collBean.setIwDirPlyPwt(0.0);
				}
				if (ReportUtility.isVS) {
					collBean.setVsSale(0.0);
					collBean.setVsPwt(0.0);
					collBean.setVsCancel(0.0);
					collBean.setVsDirPlyPwt(0.0);
				}
				agtMap.put(AgentName, collBean);
				
				nextCal.add(Calendar.DAY_OF_MONTH, 1);//increment the date
				
				}
			}
			System.out.println("***agentMap***"+agtMap);
			
			collectionDateWise(startDate, endDate, con,
					agtMap,agtOrgId,retOrgId);
			
			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr1 = agtMap
			.entrySet().iterator();
			while (itr1.hasNext()) {
					Map.Entry<String, CollectionReportOverAllBean> pair = itr1.next();
					CollectionReportOverAllBean bean = pair.getValue();
					double openingBal = bean.getDgSale()
					- bean.getDgCancel()
					- bean.getDgPwt()
					- bean.getDgDirPlyPwt()
					+ bean.getSeSale()
					- bean.getSeCancel()
					- bean.getSePwt()
					- bean.getSeDirPlyPwt()
					+ bean.getIwSale()
					- bean.getIwCancel()
					- bean.getIwDirPlyPwt()
					- bean.getIwPwt()
					+ bean.getVsSale()
					- bean.getVsCancel()
					- bean.getVsDirPlyPwt()
					- bean.getVsPwt()
					+ bean.getCSSale()
					- bean.getCSCancel()
					+bean.getSleSale()
					-bean.getSleCancel()
					-bean.getSlePwt()
					-bean.getSleDirPlyPwt()
					+ bean.getDeposit()
					- bean.getDepositRefund()
					- bean.getWithdrawal()
					- bean.getNetGamingComm()
					- (bean.getCash() + bean.getCheque() + bean.getCredit()
						+ bean.getBankDep() - bean.getDebit() - bean
						.getChequeReturn())
						+openingBalance;
		bean.setOpeningBal(openingBalance);
		openingBalance=openingBal;
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