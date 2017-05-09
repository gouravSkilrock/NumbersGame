package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentCollectionReportOverAllBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
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

public class RetailerOpeningBalanceHelper {
	Log logger = LogFactory.getLog(RetailerOpeningBalanceHelper.class);

	public Double getRetailerOpeningBal(Timestamp tartDate, Timestamp endDate, int retOrgId, int agentOrgId, Connection con) {

		Date lastArchDate = null;
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		double RetOpenBal = 0.0;
		double archivedRetOpenBal = 0.0;
		ResultSet rs = null;
		logger.info("Retailer Org Id " + retOrgId);

		PreparedStatement pstmt = null;
		Statement stmt = null;

		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);
		try {
			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='RETAILER'");
				pstmt.setTimestamp(1, new Timestamp(checkCal.getTimeInMillis()));
				pstmt.setInt(2, retOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					archivedRetOpenBal = rs.getDouble("open_bal");
				}
				return archivedRetOpenBal;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='RETAILER'");
				pstmt.setTimestamp(1, new Timestamp(lastRunCal.getTimeInMillis()));
				pstmt.setInt(2, retOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					archivedRetOpenBal = rs.getDouble("open_bal");
				}
			}

//			boolean isDataFromArch = ReportUtility.checkDateLessThanLastRunDate(endDate, agentOrgId, "RETAILER", con);
//			if(isDataFromArch){
//				Calendar cal1=Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//				
//				pstmt=con.prepareStatement("select organization_id,opening_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER' and organization_id=?");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//				pstmt.setInt(2, retOrgId);
//				
//				rs=pstmt.executeQuery();
//				if(rs.next()){
//					archivedRetOpenBal=rs.getDouble("opening_bal");
//					
//				}
//				return archivedRetOpenBal;
//				
//			}else{
//				lastArchDate=ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt=con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER' and organization_id=?");
//				pstmt.setDate(1, lastArchDate);
//				pstmt.setInt(2, retOrgId);
//				rs=pstmt.executeQuery();
//				if(rs.next()){
//					archivedRetOpenBal=rs.getDouble("open_bal");
//					
//				}
//			}

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);
					
			Timestamp fromDate=new Timestamp(cal.getTimeInMillis());
			
				int partyid = retOrgId;
				String accCollectionDetail="select aa.a 'cash', bb.b 'cheque', cc.c 'cheque_ret', gg.debit_amt 'debit', kk.credit_amt 'credit'  from  (( select ifnull(sum(cash.amount),0) 'a' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where cash.agent_org_id=? and cash.retailer_org_id = ? and ( btm.transaction_date >=? and btm.transaction_date<= ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and chq.retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<= ?) and chq.transaction_id=btm.transaction_id  ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and chq.retailer_org_id =? and (btm.transaction_date>=? and btm.transaction_date<=?) and chq.transaction_id=btm.transaction_id  ) cc,  ( select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<=?) )gg, ( select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<=?) )kk)";
				/*if (LMSFilterDispatcher.isRepFrmSP) {
					
					String cashCHqCollectionArchQry="union all select sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt) as chq_ret,sum(debit_note) as debit ,sum(credit_note) as credit from st_rep_agent_payments where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by retailer_org_id";
					
					accCollectionDetail=accCollectionDetail+cashCHqCollectionArchQry;
				
				}*/
				
				pstmt = con.prepareStatement(accCollectionDetail);
				pstmt.setInt(1, agentOrgId);
				pstmt.setInt(2, partyid);
				pstmt.setTimestamp(3, fromDate);
				pstmt.setTimestamp(4, endDate);

				pstmt.setInt(5, agentOrgId);
				pstmt.setInt(6, partyid);
				pstmt.setTimestamp(7, fromDate);
				pstmt.setTimestamp(8, endDate);

				pstmt.setInt(9, agentOrgId);
				pstmt.setInt(10, partyid);
				pstmt.setTimestamp(11, fromDate);
				pstmt.setTimestamp(12, endDate);

				pstmt.setInt(13, agentOrgId);
				pstmt.setInt(14, partyid);
				pstmt.setTimestamp(15, fromDate);
				pstmt.setTimestamp(16, endDate);
				
				pstmt.setInt(17, agentOrgId);
				pstmt.setInt(18, partyid);
				pstmt.setTimestamp(19, fromDate);
				pstmt.setTimestamp(20, endDate);

				rs = pstmt.executeQuery();
				logger
						.debug("get Agent accounts collections details query- ==== -"
								+ pstmt);
				
				
				
				double recTotal = 0.0;
				while (rs.next()) {
					double cash = rs.getDouble("cash");
					double cheque = rs.getDouble("cheque");
					double credit = rs.getDouble("credit");
					double debit = rs.getDouble("debit");
					double chqRet = rs.getDouble("cheque_ret");
					recTotal =recTotal+ cash + cheque +credit - debit - chqRet;
										
				}
				double scratchTotal = 0.0;
				double drawTotal = 0.0;
				double csTotal=0.0;
				double olaTotal=0.0;
				double sleTotal = 0.0;
				double iwTotal = 0.0;
				double vsTotal = 0.0;
				
				if(ReportUtility.isDG){
					double dgSale=0.0;
					double dgSaleRefund =0.0;
					double dgPwt=0.0;
					saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ='"+retOrgId+"'";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ='"+retOrgId+"'";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ='"+retOrgId+"'";

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(pwt),0.0) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append(" (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ saleTranDG
									+ ") group by drs.retailer_org_id)  union all");

					cancelQry
							.append("(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ cancelTranDG
									+ ") group by drs.retailer_org_id)  union all");

					pwtQry
							.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm-govt_claim_comm) as pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ pwtTranDG
									+ ") group by drs.retailer_org_id)  union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry
						.append(") saleTlb group by retailer_org_id");
				cancelQry
						.append(") cancelTlb group by retailer_org_id");
				pwtQry
						.append(") pwtTlb group by retailer_org_id");
				
				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry=" union all select organization_id,sum(sale_net) as sale from st_rep_dg_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
					String cancelDebitArchQry=" union all select organization_id,sum(ref_net_amt) as cancel from st_rep_dg_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
					String pwtArchQry=" union all select organization_id,sum(pwt_net_amt) as pwt  from st_rep_dg_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
				 saleQry.append(saleArchQry);
				 cancelQry.append(cancelDebitArchQry);
				 pwtQry.append(pwtArchQry);
											
				}
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
						dgSale=dgSale+	rs.getDouble("sale");
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
							dgSaleRefund=dgSaleRefund+ rs.getDouble("cancel");
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
						dgPwt=dgPwt+rs.getDouble("pwt");
				}
				drawTotal = dgSale - dgSaleRefund - dgPwt;
			
			}
				if(ReportUtility.isCS){

					double csSale=0.0;
					double csSaleRefund=0.0;
					saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and retailer_org_id ='"+retOrgId+"'";
					cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and retailer_org_id ='"+retOrgId+"'";

					// Category Master Query
					String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
					PreparedStatement gamePstmt = con.prepareStatement(catQry);
					ResultSet rsProduct = gamePstmt.executeQuery();
					StringBuilder saleQry = new StringBuilder(
							"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
					StringBuilder cancelQry = new StringBuilder(
							"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
					while (rsProduct.next()) {
						saleQry
								.append(" (select drs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_"
										+ rsProduct.getInt("category_id")
										+ " drs where transaction_id in ("
										+ saleTranCS
										+ ") group by drs.retailer_org_id)  union all");

						cancelQry
								.append("(select drs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_"
										+ rsProduct.getInt("category_id")
										+ " drs where transaction_id in ("
										+ cancelTranCS
										+ ") group by drs.retailer_org_id) union all");

					}

					saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
							.length());
					cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
							.length());

					saleQry
							.append(") saleTlb group by retailer_org_id");
					cancelQry
							.append(") cancelTlb group by retailer_org_id");

					if (LMSFilterDispatcher.isRepFrmSP) {
						String saleArchQry=" union all select organization_id,sum(sale_net) as sale from st_rep_cs_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
						String cancelDebitArchQry=" union all select organization_id,sum(ref_net_amt) as cancel from st_rep_cs_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
						
					 saleQry.append(saleArchQry);
					 cancelQry.append(cancelDebitArchQry);
					}
					logger.debug("-------CS Sale Query------\n" + saleQry);
					logger.debug("-------CS Cancel Query------\n" + cancelQry);

					// CS Sale Query
					pstmt = con.prepareStatement(saleQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
								 csSale=csSale+rs.getDouble("sale");
					}
					// CS Cancel Query
					pstmt = con.prepareStatement(cancelQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
								 csSaleRefund=csSaleRefund+rs.getDouble("cancel");
					}
					csTotal=csSale-csSaleRefund;
				}
				if(ReportUtility.isSE){

					// Calculate Scratch Sale
					double seSale=0.0;
					double seSaleRefund=0.0;
					double sePwt=0.0;
					String saleQry = "";
					String cancelQry="";
					logger.info("----Type Select ---"
							+ LMSFilterDispatcher.seSaleReportType);
					if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
						
						saleQry = "select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt from((select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_retailer_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='SALE' and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and art.retailer_org_id='"+retOrgId+"' group by retailer_org_id ) " 
						          +	"union all (select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_ret_loose_book_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE' and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and art.retailer_org_id='"+retOrgId+"' group by retailer_org_id))saleTlb group by retailer_org_id ";
						
						cancelQry = "select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt from ((select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_retailer_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='SALE_RET' and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and art.retailer_org_id='"+retOrgId+"' group by retailer_org_id) union all" +
								" (select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_ret_loose_book_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE_RET' and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and art.retailer_org_id='"+retOrgId+"' group by retailer_org_id))cancelTlb group by retailer_org_id";

						
						/*saleQry = "select sale.organization_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') group by agent_org_id) sale on organization_id=agent_org_id where organization_type='AGENT' and organization_id="+agtOrgId+") sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') group by agent_org_id ) saleRet on organization_id=agent_org_id where organization_type='AGENT' and organization_id="+agtOrgId+") saleRet on sale.organization_id=saleRet.organization_id";
				
						
						 cancelQry="select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE_RET' and transaction_date>='" 
								+ startDate
							    +"' and transaction_date<='" 
							    + endDate
							    +"' and sbt.agent_org_id="+agtOrgId +" group by date(transaction_date)";
					
					*/
					
					
					
					} else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						/*saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
								+ startDate
								+ "' and date<='"
								+ endDate
								+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT' and organization_id="+agtOrgId;
				*/	}
					
					if (LMSFilterDispatcher.isRepFrmSP) {
						String saleArchQry=" union all select organization_id,sum(sale_book_mrp),sum(sale_book_net) as sale from st_rep_se_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
						String cancelDebitArchQry=" union all select organization_id,sum(ref_sale_mrp),sum(ref_net_amt) as cancel from st_rep_se_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
						//String cancelDebitArchQry="union all select finaldate,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					 saleQry=saleQry+saleArchQry;
					 cancelQry=cancelQry+cancelDebitArchQry;
					}
					
					pstmt = con.prepareStatement(saleQry);
					logger.debug("***Scratch Sale Query*** \n" + pstmt);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						seSale=seSale+	rs.getDouble("netAmt");
					}

					pstmt = con.prepareStatement(cancelQry);
					logger.debug("***Scratch Cancel Query*** \n" + pstmt);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						seSaleRefund=seSaleRefund+	rs.getDouble("netAmt");
					}

					
					// Calculate Scratch Pwt
					String pwtQry = "select "+retOrgId+",ifnull(sum(pwt_amt+(pwt_amt*claim_comm*0.01)),0.0) pwt  from st_lms_retailer_transaction_master rtm,st_se_retailer_pwt srw where rtm.transaction_id=srw.transaction_id and rtm.transaction_type in('PWT','PWT_AUTO') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and " 
					+ "srw.retailer_org_id='"+retOrgId+"' ";
					if (LMSFilterDispatcher.isRepFrmSP) {
						String pwtArchQry="union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by organization_id";
						pwtQry=pwtQry+pwtArchQry;
					 
					}
					stmt = con.createStatement();
					logger.debug("***Scratch Pwt Query*** \n" + pwtQry);
					rs = stmt.executeQuery(pwtQry);
//					pstmt = con.prepareStatement(pwtQry);
//					logger.debug("***Scratch Pwt Query*** \n" + pstmt);
//					rs = pstmt.executeQuery();

					while (rs.next()) {
						sePwt=sePwt+rs.getDouble("pwt");
					}

				scratchTotal=seSale-seSaleRefund-sePwt;
				
				}
				if(ReportUtility.isOLA){

					double olaDeposit=0.0;
					double olaDepositRefund=0.0;
					double olaWithdrawal=0.0;
					double olaWithdrawalRefund=0.0;
					double netGaming = 0.0;
					

				StringBuilder wdQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as wdraw ");
				StringBuilder wdRefQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as wdrawRef from ");
				StringBuilder depQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as depoAmt");
				StringBuilder depRefQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as depoRefAmt from ");

				wdQry
						.append("from st_ola_ret_withdrawl drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and retailer_org_id ="+retOrgId+") group by drs.retailer_org_id");

				wdRefQry
						.append("st_ola_ret_withdrawl_refund drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL_REFUND') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and retailer_org_id ="+retOrgId+") group by drs.retailer_org_id");
				depQry
						.append(" from st_ola_ret_deposit drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and retailer_org_id ="+retOrgId+") group by drs.retailer_org_id");

				depRefQry
						.append("st_ola_ret_deposit_refund drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and retailer_org_id ="+retOrgId+") group by drs.retailer_org_id");

				String netGamingQry = "select drs.retailer_org_id,sum(retailer_net_claim_comm) as netAmt from st_ola_ret_comm drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('OLA_COMMISSION') and transaction_date>='"+fromDate+"' and transaction_date<='"+endDate+"' and drs.retailer_org_id ="+retOrgId+" group by drs.retailer_org_id";
				logger.debug("-------Withdrawal Query------\n" + wdQry);
				logger.debug("-------WithDrawal Refund Query------\n"
						+ wdRefQry);
				logger.debug("-------Deposit Query------\n" + depQry);
				logger.debug("-------Deposit Refund Query------\n" + depRefQry);
				logger.debug("-------Net Gaming Query------\n" + netGamingQry);

					// OLA Deposit Query
					pstmt = con.prepareStatement(depQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						olaDeposit=olaDeposit+rs.getDouble("depoAmt");
					}
					// OLA Deposit Refund Query
					pstmt = con.prepareStatement(depRefQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						olaDepositRefund=olaDepositRefund+rs.getDouble("depoRefAmt");
					}
					// OLA Withdrawal Query
					pstmt = con.prepareStatement(wdQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						olaWithdrawal=olaWithdrawal+rs.getDouble("wdraw");
					}
					// OLA Withdrawal Refund Query
					pstmt = con.prepareStatement(wdRefQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						olaWithdrawalRefund=olaWithdrawalRefund+rs.getDouble("wdrawRef");
					}
					// OLA Net Gaming Query
					pstmt = con.prepareStatement(netGamingQry.toString());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						netGaming=netGaming+rs.getDouble("netAmt");
					}
					olaTotal=olaDeposit-olaDepositRefund-olaWithdrawal-olaWithdrawalRefund-netGaming;
				}
				
				if(ReportUtility.isSLE){
					double sleSale=0.0;
					double sleSaleRefund =0.0;
					double slePwt=0.0;
					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(fromDate);
					requestBean.setToDate(endDate);
					requestBean.setOrgId(agentOrgId);
					Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLERetailerReportsControllerImpl
							.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
					for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap
							.entrySet()) {
						SLEOrgReportResponseBean sleResponseBean = entry.getValue();
						sleSale += sleResponseBean.getSaleAmt();
						sleSaleRefund += sleResponseBean.getCancelAmt();
						slePwt += sleResponseBean.getPwtAmt();
					}
					sleTotal = sleSale - sleSaleRefund - slePwt;
			
				}
				if(ReportUtility.isIW){
					double iwSale=0.0;
					double iwSaleRefund =0.0;
					double iwPwt=0.0;
					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(fromDate);
					requestBean.setToDate(endDate);
					requestBean.setOrgId(retOrgId);
					Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWRetailerReportsControllerImpl
							.fetchSaleCancelPwtSingleRetailer(requestBean, con);
					for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap
							.entrySet()) {
						IWOrgReportResponseBean iwResponseBean = entry.getValue();
						iwSale += iwResponseBean.getSaleAmt();
						iwSaleRefund += iwResponseBean.getCancelAmt();
						iwPwt += iwResponseBean.getPwtAmt();
					}
					iwTotal = iwSale - iwSaleRefund - iwPwt;
			
				}
				
			if (ReportUtility.isVS) {
				double vsSale = 0.0;
				double vsSaleRefund = 0.0;
				double vsPwt = 0.0;
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(retOrgId);
				Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSRetailerReportsControllerImpl
						.fetchSaleCancelPwtSingleRetailer(requestBean, con);
				for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap
						.entrySet()) {
					VSOrgReportResponseBean vsResponseBean = entry.getValue();
					vsSale += vsResponseBean.getSaleAmt();
					vsSaleRefund += vsResponseBean.getCancelAmt();
					vsPwt += vsResponseBean.getPwtAmt();
				}
				vsTotal = vsSale - vsSaleRefund - vsPwt;
			}
			RetOpenBal = drawTotal + scratchTotal + csTotal + olaTotal + sleTotal + iwTotal - recTotal + archivedRetOpenBal + vsTotal;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return RetOpenBal;
	}
	
	
	public Double getRetailerOpeningBalIncludingCLXCL(Timestamp tartDate, Timestamp endDate, int retOrgId, int agentOrgId, Connection con) {
		Date lastArchDate = null;
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		double RetOpenBal = 0.0;
		double archivedRetOpenBal = 0.0;
		ResultSet rs = null;
		logger.info(con + "Retailer Org Id " + retOrgId);

		PreparedStatement pstmt = null;
		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);
		try {
//			boolean isDataFromArch=ReportUtility.checkDateLessThanLastRunDate(endDate, agentOrgId, "RETAILER", con);
//			if(isDataFromArch){
//				Calendar cal1=Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//				
//				pstmt=con.prepareStatement("select organization_id,opening_bal_cl_inc from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER' and organization_id=?");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//				pstmt.setInt(2, retOrgId);
//				rs=pstmt.executeQuery();
//				if(rs.next()){
//					archivedRetOpenBal=rs.getDouble("opening_bal_cl_inc");
//					
//				}
//				return archivedRetOpenBal;
//				
//			}else{
//				lastArchDate=ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt=con.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER' and organization_id=?");
//				pstmt.setDate(1, lastArchDate);
//				pstmt.setInt(2, retOrgId);
//				rs=pstmt.executeQuery();
//				if(rs.next()){
//					archivedRetOpenBal=rs.getDouble("open_bal");
//					
//				}
//			}

			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='RETAILER'");
				pstmt.setDate(1, new Date(checkCal.getTimeInMillis()));
				pstmt.setInt(2, retOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					archivedRetOpenBal = rs.getDouble("open_bal");
				}
				return archivedRetOpenBal;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='RETAILER'");
				pstmt.setDate(1, new Date(lastRunCal.getTimeInMillis()));
				pstmt.setInt(2, retOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					archivedRetOpenBal = rs.getDouble("open_bal");
				}
			}

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);

//			Calendar cal=Calendar.getInstance();
//			cal.setTimeInMillis(lastArchDate.getTime());
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			ReportUtility.clearTimeFromDate(cal);
					
			Timestamp fromDate=new Timestamp(cal.getTimeInMillis());

			int partyid = retOrgId;
			String accCollectionDetail = "select aa.a 'cash', bb.b 'cheque', cc.c 'cheque_ret', gg.debit_amt 'debit', kk.credit_amt 'credit',ll.bank_depo 'bank_depo'  from  (( select ifnull(sum(cash.amount),0) 'a' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where cash.agent_org_id=? and cash.retailer_org_id = ? and ( btm.transaction_date >=? and btm.transaction_date<= ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and chq.retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<= ?) and chq.transaction_id=btm.transaction_id  ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and chq.retailer_org_id =? and (btm.transaction_date>=? and btm.transaction_date<=?) and chq.transaction_id=btm.transaction_id  ) cc,  ( select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<=?) )gg, ( select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( btm.transaction_date>=? and btm.transaction_date<=?) )kk,(select ifnull(sum(bdt.amount),0) 'bank_depo' from st_lms_agent_bank_deposit_transaction bdt, st_lms_agent_transaction_master btm where bdt.retailer_org_id = ? and ( btm.transaction_date >=? and btm.transaction_date<= ?) and bdt.transaction_id=btm.transaction_id )ll)";
			/*if (LMSFilterDispatcher.isRepFrmSP) {
			
			String cashCHqCollectionArchQry="union all select sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt) as chq_ret,sum(debit_note) as debit ,sum(credit_note) as credit from st_rep_agent_payments where finaldate>='"+fromDate+"' and finaldate<='"+endDate+"' and retailer_org_id="+retOrgId+" group by retailer_org_id";
			
			accCollectionDetail=accCollectionDetail+cashCHqCollectionArchQry;
		
		}*/

			pstmt = con.prepareStatement(accCollectionDetail);
			pstmt.setInt(1, agentOrgId);
			pstmt.setInt(2, partyid);
			pstmt.setTimestamp(3, fromDate);
			pstmt.setTimestamp(4, endDate);

			pstmt.setInt(5, agentOrgId);
			pstmt.setInt(6, partyid);
			pstmt.setTimestamp(7, fromDate);
			pstmt.setTimestamp(8, endDate);

			pstmt.setInt(9, agentOrgId);
			pstmt.setInt(10, partyid);
			pstmt.setTimestamp(11, fromDate);
			pstmt.setTimestamp(12, endDate);

			pstmt.setInt(13, agentOrgId);
			pstmt.setInt(14, partyid);
			pstmt.setTimestamp(15, fromDate);
			pstmt.setTimestamp(16, endDate);

			pstmt.setInt(17, agentOrgId);
			pstmt.setInt(18, partyid);
			pstmt.setTimestamp(19, fromDate);
			pstmt.setTimestamp(20, endDate);

			pstmt.setInt(21, partyid);
			pstmt.setTimestamp(22, fromDate);
			pstmt.setTimestamp(23, endDate);
			rs = pstmt.executeQuery();
			logger.debug("get Agent accounts collections details query- ==== -"
					+ pstmt);

			double recTotal = 0.0;
			while (rs.next()) {
				double cash = rs.getDouble("cash");
				double cheque = rs.getDouble("cheque");
				double credit = rs.getDouble("credit");
				double debit = rs.getDouble("debit");
				double chqRet = rs.getDouble("cheque_ret");
				double bankDepo = rs.getDouble("bank_depo");
				recTotal = recTotal + cash + cheque + credit + bankDepo - debit
						- chqRet;

			}
			double scratchTotal = 0.0;
			double drawTotal = 0.0;
			double csTotal = 0.0;
			double olaTotal = 0.0;
			double sleTotal = 0.0;
			double iwTotal = 0.0;
			double vsTotal = 0.0;

			if (ReportUtility.isSLE) {
				double sleSale = 0.0;
				double sleSaleRefund = 0.0;
				double slePwt = 0.0;

				// String gameQry = "select game_id from st_sle_game_master";
				// PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				// ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(pwt),0.0) as pwt from (");
				// while (rsGame.next()) {
				saleQry.append(" (select  drs.retailer_org_id,sum(retailer_net_amt) as sale from st_sle_ret_sale"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('SLE_SALE','SLE_SALE_OFFLINE') group by rtm.retailer_org_id)  union all");

				cancelQry
						.append("(select drs.retailer_org_id,sum(retailer_net_amt) as cancel from st_sle_ret_sale_refund"
								+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
								+ "	rtm.retailer_org_id ="
								+ retOrgId
								+ " and rtm.transaction_date>='"
								+ fromDate
								+ "'  and rtm.transaction_date<='"
								+ endDate
								+ "'  and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') group by rtm.retailer_org_id)  union all");

				pwtQry.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_sle_ret_pwt"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') group by rtm.retailer_org_id)  union all");

				// }

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				pwtQry.append(") pwtTlb group by retailer_org_id");

				logger.debug("-------SLE Sale Qurey------\n" + saleQry);
				logger.debug("-------SLE Cancel Qurey------\n" + cancelQry);
				logger.debug("-------SLE Pwt Qurey------\n" + pwtQry);

				Statement stmt = con.createStatement();

				rs = stmt.executeQuery(saleQry.toString());
				// sle sale Query
				while (rs.next()) {
					sleSale = sleSale + rs.getDouble("sale");
				}
				// sle Cancel Query
				rs = stmt.executeQuery(cancelQry.toString());
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */
				while (rs.next()) {
					sleSaleRefund = sleSaleRefund + rs.getDouble("cancel");
				}
				// sle Pwt Query
				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					slePwt = slePwt + rs.getDouble("pwt");
				}
				sleTotal = sleSale - sleSaleRefund - slePwt;
			}

			if (ReportUtility.isIW) {
				double iwSale = 0.0;
				double iwSaleRefund = 0.0;
				double iwPwt = 0.0;

				// String gameQry = "select game_id from st_sle_game_master";
				// PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				// ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(pwt),0.0) as pwt from (");
				// while (rsGame.next()) {
				saleQry.append(" (select  drs.retailer_org_id,sum(retailer_net_amt) as sale from st_iw_ret_sale"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('IW_SALE','IW_SALE_OFFLINE') group by rtm.retailer_org_id)  union all");

				cancelQry
						.append("(select drs.retailer_org_id,sum(retailer_net_amt) as cancel from st_iw_ret_sale_refund"
								+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
								+ "	rtm.retailer_org_id ="
								+ retOrgId
								+ " and rtm.transaction_date>='"
								+ fromDate
								+ "'  and rtm.transaction_date<='"
								+ endDate
								+ "'  and transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') group by rtm.retailer_org_id)  union all");

				pwtQry.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_iw_ret_pwt"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('IW_PWT_AUTO','IW_PWT_PLR','IW_PWT') group by rtm.retailer_org_id)  union all");

				// }

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				pwtQry.append(") pwtTlb group by retailer_org_id");

				logger.debug("-------IW Sale Qurey------\n" + saleQry);
				logger.debug("-------IW Cancel Qurey------\n" + cancelQry);
				logger.debug("-------IW Pwt Qurey------\n" + pwtQry);

				Statement stmt = con.createStatement();

				rs = stmt.executeQuery(saleQry.toString());
				// sle sale Query
				while (rs.next()) {
					iwSale = iwSale + rs.getDouble("sale");
				}
				// sle Cancel Query
				rs = stmt.executeQuery(cancelQry.toString());
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */
				while (rs.next()) {
					iwSaleRefund = iwSaleRefund + rs.getDouble("cancel");
				}
				// sle Pwt Query
				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					iwPwt = iwPwt + rs.getDouble("pwt");
				}
				iwTotal = iwSale - iwSaleRefund - iwPwt;
			}

			if (ReportUtility.isDG) {
				double dgSale = 0.0;
				double dgSaleRefund = 0.0;
				double dgPwt = 0.0;
				/*
				 * saleTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id ='"+retOrgId+"'"; cancelTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id ='"+retOrgId+"'"; pwtTranDG =
				 * "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "' and retailer_org_id ='"+retOrgId+"'";
				 */

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(pwt),0.0) as pwt from (");
				while (rsGame.next()) {
					/*
					 * saleQry .append(
					 * " (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_"
					 * + rsGame.getInt("game_id") +
					 * " drs where transaction_id in (" + saleTranDG +
					 * ") group by drs.retailer_org_id)  union all");
					 * 
					 * cancelQry .append(
					 * "(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_"
					 * + rsGame.getInt("game_id") +
					 * " drs where transaction_id in (" + cancelTranDG +
					 * ") group by drs.retailer_org_id)  union all");
					 * 
					 * pwtQry .append(
					 * "(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
					 * + rsGame.getInt("game_id") +
					 * " drs where transaction_id in (" + pwtTranDG +
					 * ") group by drs.retailer_org_id)  union all");
					 */
					saleQry.append(" (select  drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_"
							+ rsGame.getInt("game_id")
							+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
							+ "	rtm.retailer_org_id ="
							+ retOrgId
							+ " and transaction_date>='"
							+ fromDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and transaction_type in('DG_SALE','DG_SALE_OFFLINE') group by rtm.retailer_org_id)  union all");

					cancelQry
							.append("(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
									+ "	rtm.retailer_org_id ="
									+ retOrgId
									+ " and transaction_date>='"
									+ fromDate
									+ "'  and transaction_date<='"
									+ endDate
									+ "'  and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') group by rtm.retailer_org_id)  union all");

					pwtQry.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
							+ rsGame.getInt("game_id")
							+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
							+ "	rtm.retailer_org_id ="
							+ retOrgId
							+ " and transaction_date>='"
							+ fromDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') group by rtm.retailer_org_id)  union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				pwtQry.append(") pwtTlb group by retailer_org_id");

				/*
				 * if (LMSFilterDispatcher.isRepFrmSP) { String saleArchQry=
				 * " union all select organization_id,sum(sale_net) as sale from st_rep_dg_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and organization_id="
				 * +retOrgId+" group by organization_id"; String
				 * cancelDebitArchQry=
				 * " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_dg_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and organization_id="
				 * +retOrgId+" group by organization_id"; String pwtArchQry=
				 * " union all select organization_id,sum(pwt_net_amt) as pwt  from st_rep_dg_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and organization_id="
				 * +retOrgId+" group by organization_id";
				 * saleQry.append(saleArchQry);
				 * cancelQry.append(cancelDebitArchQry);
				 * pwtQry.append(pwtArchQry);
				 * 
				 * }
				 */
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				// pstmt = con.prepareStatement(saleQry.toString());
				// rs = pstmt.executeQuery();

				Statement stmt = con.createStatement();
				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {

					dgSale = dgSale + rs.getDouble("sale");
				}
				// Draw Cancel Query
				rs = stmt.executeQuery(cancelQry.toString());
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */
				while (rs.next()) {

					dgSaleRefund = dgSaleRefund + rs.getDouble("cancel");
				}
				// Draw Pwt Query
				rs = stmt.executeQuery(pwtQry.toString());
				/*
				 * pstmt = con.prepareStatement(pwtQry.toString()); rs =
				 * pstmt.executeQuery();
				 */
				while (rs.next()) {

					dgPwt = dgPwt + rs.getDouble("pwt");
				}
				drawTotal = dgSale - dgSaleRefund - dgPwt;

			}
			if (ReportUtility.isCS) {

				double csSale = 0.0;
				double csSaleRefund = 0.0;
				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ='" + retOrgId + "'";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ='" + retOrgId + "'";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				while (rsProduct.next()) {
					saleQry.append(" (select drs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_"
							+ rsProduct.getInt("category_id")
							+ " drs where transaction_id in ("
							+ saleTranCS
							+ ") group by drs.retailer_org_id)  union all");

					cancelQry
							.append("(select drs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_"
									+ rsProduct.getInt("category_id")
									+ " drs where transaction_id in ("
									+ cancelTranCS
									+ ") group by drs.retailer_org_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");

				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry = " union all select organization_id,sum(sale_net) as sale from st_rep_cs_retailer where finaldate>='"
							+ fromDate
							+ "' and finaldate<='"
							+ endDate
							+ "' and organization_id="
							+ retOrgId
							+ " group by organization_id";
					String cancelDebitArchQry = " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_cs_retailer where finaldate>='"
							+ fromDate
							+ "' and finaldate<='"
							+ endDate
							+ "' and organization_id="
							+ retOrgId
							+ " group by organization_id";

					saleQry.append(saleArchQry);
					cancelQry.append(cancelDebitArchQry);
				}
				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n" + cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					csSale = csSale + rs.getDouble("sale");
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					csSaleRefund = csSaleRefund + rs.getDouble("cancel");
				}
				csTotal = csSale - csSaleRefund;
			}
			if (ReportUtility.isSE) {

				// Calculate Scratch Sale
				double seSale = 0.0;
				double seSaleRefund = 0.0;
				double sePwt = 0.0;
				String saleQry = "";
				String cancelQry = "";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {

					saleQry = "select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt from((select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_retailer_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='SALE' and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and art.retailer_org_id='"
							+ retOrgId
							+ "' group by retailer_org_id ) "
							+ "union all (select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_ret_loose_book_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE' and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and art.retailer_org_id='"
							+ retOrgId
							+ "' group by retailer_org_id))saleTlb group by retailer_org_id ";

					cancelQry = "select retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt from ((select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_retailer_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='SALE_RET' and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and art.retailer_org_id='"
							+ retOrgId
							+ "' group by retailer_org_id) union all"
							+ " (select art.retailer_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt  from st_se_agent_ret_loose_book_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and atm.transaction_type ='LOOSE_SALE_RET' and transaction_date>='"
							+ fromDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and art.retailer_org_id='"
							+ retOrgId
							+ "' group by retailer_org_id))cancelTlb group by retailer_org_id";

					/*
					 * saleQry =
					 * "select sale.organization_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
					 * + startDate + "' and transaction_date<='" + endDate +
					 * "') group by agent_org_id) sale on organization_id=agent_org_id where organization_type='AGENT' and organization_id="
					 * +agtOrgId+
					 * ") sale inner join (select organization_id,name,ifnull(mrpAmtRet,0.0) mrpAmtRet,ifnull(netAmtRet,0.0) netAmtRet from st_lms_organization_master left outer join (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
					 * + startDate + "' and transaction_date<='" + endDate +
					 * "') group by agent_org_id ) saleRet on organization_id=agent_org_id where organization_type='AGENT' and organization_id="
					 * +agtOrgId+
					 * ") saleRet on sale.organization_id=saleRet.organization_id"
					 * ;
					 * 
					 * 
					 * cancelQry=
					 * "select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE_RET' and transaction_date>='"
					 * + startDate +"' and transaction_date<='" + endDate
					 * +"' and sbt.agent_org_id="+agtOrgId
					 * +" group by date(transaction_date)";
					 */

				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					/*
					 * saleQry =
					 * "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
					 * + startDate + "' and date<='" + endDate +
					 * "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT' and organization_id="
					 * +agtOrgId;
					 */}

				if (LMSFilterDispatcher.isRepFrmSP) {
					String saleArchQry = " union all select organization_id,sum(sale_book_mrp),sum(sale_book_net) as sale from st_rep_se_retailer where finaldate>='"
							+ fromDate
							+ "' and finaldate<='"
							+ endDate
							+ "' and organization_id="
							+ retOrgId
							+ " group by organization_id";
					String cancelDebitArchQry = " union all select organization_id,sum(ref_sale_mrp),sum(ref_net_amt) as cancel from st_rep_se_retailer where finaldate>='"
							+ fromDate
							+ "' and finaldate<='"
							+ endDate
							+ "' and organization_id="
							+ retOrgId
							+ " group by organization_id";
					// String
					// cancelDebitArchQry="union all select finaldate,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and organization_id="+retOrgId+" group by finaldate";
					saleQry = saleQry + saleArchQry;
					cancelQry = cancelQry + cancelDebitArchQry;
				}

				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					seSale = seSale + rs.getDouble("netAmt");
				}

				pstmt = con.prepareStatement(cancelQry);
				logger.debug("***Scratch Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					seSaleRefund = seSaleRefund + rs.getDouble("netAmt");
				}

				// Calculate Scratch Pwt
				String pwtQry = "select "
						+ retOrgId
						+ ",ifnull(sum(pwt_amt+(pwt_amt*claim_comm*0.01)),0.0) pwt  from st_lms_retailer_transaction_master rtm,st_se_retailer_pwt srw where rtm.transaction_id=srw.transaction_id and rtm.transaction_type in('PWT','PWT_AUTO') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and "
						+ "srw.retailer_org_id='"
						+ retOrgId
						+ "' union all select "
						+ retOrgId
						+ ",ifnull(sum(pwt_amt+(pwt_amt*claim_comm*0.01)),0.0) pwt  from st_lms_agent_transaction_master atm,st_se_agent_pwt saw where atm.transaction_id=saw.transaction_id and atm.transaction_type in('PWT','PWT_AUTO') and transaction_date>='"
						+ fromDate + "' and transaction_date<='" + endDate
						+ "' and " + "saw.retailer_org_id='" + retOrgId + "'";
				if (LMSFilterDispatcher.isRepFrmSP) {
					String pwtArchQry = "union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='"
							+ fromDate
							+ "' and finaldate<='"
							+ endDate
							+ "' and organization_id="
							+ retOrgId
							+ " group by organization_id";
					pwtQry = pwtQry + pwtArchQry;

				}
				pstmt = con.prepareStatement(pwtQry);
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					sePwt = sePwt + rs.getDouble("pwt");
				}
				scratchTotal = seSale - seSaleRefund - sePwt;
			}
			if (ReportUtility.isOLA) {
				double olaDeposit = 0.0;
				double olaDepositRefund = 0.0;
				double olaWithdrawal = 0.0;
				double olaWithdrawalRefund = 0.0;
				double netGaming = 0.0;

				StringBuilder wdQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as wdraw ");
				StringBuilder wdRefQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as wdrawRef from ");
				StringBuilder depQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as depoAmt");
				StringBuilder depRefQry = new StringBuilder(
						"select drs.retailer_org_id,sum(net_amt) as depoRefAmt from ");

				wdQry.append("from st_ola_ret_withdrawl drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ="
						+ retOrgId
						+ ") group by drs.retailer_org_id");

				wdRefQry.append("st_ola_ret_withdrawl_refund drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL_REFUND') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ="
						+ retOrgId
						+ ") group by drs.retailer_org_id");
				depQry.append(" from st_ola_ret_deposit drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id ="
						+ retOrgId
						+ ") group by drs.retailer_org_id");

				depRefQry
						.append("st_ola_ret_deposit_refund drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and retailer_org_id ="
								+ retOrgId + ") group by drs.retailer_org_id");

				String netGamingQry = "select drs.retailer_org_id,sum(retailer_net_claim_comm) as netAmt from st_ola_ret_comm drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('OLA_COMMISSION') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and drs.retailer_org_id ="
						+ retOrgId
						+ " group by drs.retailer_org_id";
				logger.debug("-------Withdrawal Query------\n" + wdQry);
				logger.debug("-------WithDrawal Refund Query------\n"
						+ wdRefQry);
				logger.debug("-------Deposit Query------\n" + depQry);
				logger.debug("-------Deposit Refund Query------\n" + depRefQry);
				logger.debug("-------Net Gaming Query------\n" + netGamingQry);

				// OLA Deposit Query
				pstmt = con.prepareStatement(depQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					olaDeposit = olaDeposit + rs.getDouble("depoAmt");
				}
				// OLA Deposit Refund Query
				pstmt = con.prepareStatement(depRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					olaDepositRefund = olaDepositRefund
							+ rs.getDouble("depoRefAmt");
				}
				// OLA Withdrawal Query
				pstmt = con.prepareStatement(wdQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					olaWithdrawal = olaWithdrawal + rs.getDouble("wdraw");
				}
				// OLA Withdrawal Refund Query
				pstmt = con.prepareStatement(wdRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					olaWithdrawalRefund = olaWithdrawalRefund
							+ rs.getDouble("wdrawRef");
				}
				// OLA Net Gaming Query
				pstmt = con.prepareStatement(netGamingQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					netGaming = netGaming + rs.getDouble("netAmt");
				}
				olaTotal = olaDeposit - olaDepositRefund - olaWithdrawal
						- olaWithdrawalRefund - netGaming;
			}
			
			
			if (ReportUtility.isVS) {
				double vsSale = 0.0;
				double vsSaleRefund = 0.0;
				double vsPwt = 0.0;

				StringBuilder saleQry = new StringBuilder("select retailer_org_id,ifnull(sum(sale),0.0) as sale from (");
				StringBuilder cancelQry = new StringBuilder("select retailer_org_id,ifnull(sum(cancel),0.0) as cancel from (");
				StringBuilder pwtQry = new StringBuilder("select retailer_org_id,ifnull(sum(pwt),0.0) as pwt from (");
				// while (rsGame.next()) {
				saleQry.append(" (select  drs.retailer_org_id,sum(retailer_net_amt) as sale from st_vs_ret_sale"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('VS_SALE','VS_SALE_OFFLINE') group by rtm.retailer_org_id)  union all");

				cancelQry
						.append("(select drs.retailer_org_id,sum(retailer_net_amt) as cancel from st_vs_ret_sale_refund"
								+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
								+ "	rtm.retailer_org_id ="
								+ retOrgId
								+ " and rtm.transaction_date>='"
								+ fromDate
								+ "'  and rtm.transaction_date<='"
								+ endDate
								+ "'  and transaction_type in('VS_SALE_REFUND') group by rtm.retailer_org_id)  union all");

				pwtQry.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_vs_ret_pwt"
						+ " drs  inner join	st_lms_retailer_transaction_master rtm  on drs.transaction_id=rtm.transaction_id where "
						+ "	rtm.retailer_org_id ="
						+ retOrgId
						+ " and rtm.transaction_date>='"
						+ fromDate
						+ "'  and rtm.transaction_date<='"
						+ endDate
						+ "'  and transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') group by rtm.retailer_org_id)  union all");

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				pwtQry.append(") pwtTlb group by retailer_org_id");

				logger.debug("-------VS Sale Qurey------\n" + saleQry);
				logger.debug("-------VS Cancel Qurey------\n" + cancelQry);
				logger.debug("-------VS Pwt Qurey------\n" + pwtQry);

				Statement stmt = con.createStatement();

				rs = stmt.executeQuery(saleQry.toString());
				// sle sale Query
				while (rs.next()) {
					vsSale = vsSale + rs.getDouble("sale");
				}
				// sle Cancel Query
				rs = stmt.executeQuery(cancelQry.toString());
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */
				while (rs.next()) {
					vsSaleRefund = vsSaleRefund + rs.getDouble("cancel");
				}
				// sle Pwt Query
				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					vsPwt = vsPwt + rs.getDouble("pwt");
				}
				vsTotal = vsSale - vsSaleRefund - vsPwt;
			}
			
			RetOpenBal = sleTotal + drawTotal + scratchTotal + csTotal + olaTotal + iwTotal - recTotal + vsTotal;
			double clXclToatl = 0.0;
			String clXclamt = "select ifnull(sum(amount),0.0) as amount from st_lms_cl_xcl_update_history where organization_id=? and date_time>=? and date_time< ? ";
			pstmt = con.prepareStatement(clXclamt);
			pstmt.setInt(1, retOrgId);
			pstmt.setTimestamp(2, fromDate);
			pstmt.setTimestamp(3, endDate);
			ResultSet rs3 = pstmt.executeQuery();
			while (rs3.next()) {
				clXclToatl = rs3.getDouble("amount");
			}
			RetOpenBal = (-RetOpenBal) + clXclToatl + archivedRetOpenBal;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return RetOpenBal;
	}
	
	public void collectionRetailerWiseOpenBal(int userId, Timestamp startDate, Timestamp endDate, Connection con,Map<String, AgentCollectionReportOverAllBean> agtMap, int count) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		Date lastArchDate=null;
		if (startDate.after(endDate)) {
			return;
		}

		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);

		try {
//			boolean isDataFromArch=ReportUtility.checkDateLessThanLastRunDate(endDate, userId, "RETAILER", con);
//			if(isDataFromArch){
//				Calendar cal1=Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//				
//				pstmt=con.prepareStatement("select organization_id,opening_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//				
//				rs=pstmt.executeQuery();
//				while(rs.next()){
//					if(agtMap.get(rs.getString("organization_id")) !=null){
//						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("opening_bal"));
//					}
//				}
//				return;
//				
//			}else{
//				lastArchDate=ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt=con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
//				pstmt.setDate(1, lastArchDate);
//				
//				rs=pstmt.executeQuery();
//				while(rs.next()){
//					if(agtMap.get(rs.getString("organization_id")) !=null){
//						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
//					}
//				}
//			}

			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
				pstmt.setTimestamp(1, new Timestamp(checkCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while(rs.next()){
					if(agtMap.get(rs.getString("organization_id")) !=null){
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("opening_bal"));
					}
				}
				return;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
				pstmt.setTimestamp(1, new Timestamp(lastRunCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while(rs.next()){
					if(agtMap.get(rs.getString("organization_id")) !=null){
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					}
				}
			}

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);

//			Calendar cal=Calendar.getInstance();
//			cal.setTimeInMillis(lastArchDate.getTime());
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			ReportUtility.clearTimeFromDate(cal);

			Timestamp fromDate = new Timestamp(cal.getTimeInMillis());
			
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
					+ fromDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) cash " + addOrgQry + ") cash";
			String chqQry = "(select organization_id,chq from (select party_id,sum(cheque_amt) chq from st_lms_agent_sale_chq chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ fromDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) chq " + addOrgQry + ") chq";
			String chqRetQry = "(select organization_id,chq_ret from (select party_id,sum(cheque_amt) chq_ret from st_lms_agent_sale_chq  chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ fromDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) chq_ret " + addOrgQry + ") chq_ret";
			String debitQry = "(select organization_id,debit from (select party_id,sum(amount) debit from st_lms_agent_debit_note debit,st_lms_agent_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='"
					+ fromDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' group by party_id) debit " + addOrgQry + ") debit";
			String creditQry = "(select organization_id,credit from (select party_id,sum(amount) credit from st_lms_agent_credit_note credit,st_lms_agent_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='"
					+ fromDate
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
						+ fromDate
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
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ fromDate
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
							+ fromDate
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
							+ fromDate
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
							+ fromDate
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
							+ fromDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
							+ fromDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id) sale group by retailer_org_id) sale on om.retailer_org_id=sale.retailer_org_id) sale , (select om.retailer_org_id,ifnull(mrpAmtRet,0) mrpAmtRet ,ifnull(netAmtRet,0) netAmtRet from (select organization_id retailer_org_id from st_lms_organization_master where parent_id="+userId+") om left join (select retailer_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ fromDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and user_org_id="
							+ userId
							+ " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ fromDate
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
							+ fromDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER') FINAL_TABLE on om.organization_id=FINAL_TABLE.organization_id";

				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder(
							"select organization_id,sum(mrpAmt) mrpAmt, sum(netAmt) netAmt from (");
					unionQuery = " union all select organization_id,sum(sale_book_mrp)-sum(ref_sale_mrp) mrpAmt ,sum(sale_book_net) -sum(ref_net_amt) as netAmt from st_rep_se_retailer where finaldate>='"
							+ fromDate
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
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "'  and "
						+ transactionType
						+ " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId
						+ " )) tx on  tx.transaction_id=pwt.transaction_id group by retailer_org_id) tx on om.organization_id=tx.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt pwt inner join (select transaction_id from st_lms_agent_transaction_master where transaction_date>='"
						+ fromDate
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
							+ fromDate
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
						+ fromDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
						+ userId + ")";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ fromDate
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
							+ fromDate
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
							+ fromDate
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
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				wdRefQry
						.append("(select wrs.retailer_org_id,net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");
				depQry
						.append("(select wrs.retailer_org_id, net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				depRefQry
						.append("(select wrs.retailer_org_id, net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ userId
								+ ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				String netGamingQry = "select om.organization_id agtOrgId, ifnull(sum(commission_calculated),0.0) netAmt from (select ret_org_id, plr_net_gaming, commission_calculated from st_ola_agt_ret_commission where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type = 'OLA_COMMISSION' and transaction_date >= '"
						+ fromDate
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
			Iterator<Map.Entry<String, AgentCollectionReportOverAllBean>> itr = agtMap
			.entrySet().iterator();
	while (itr.hasNext()) {
		Map.Entry<String, AgentCollectionReportOverAllBean> pair = itr
				.next();
		AgentCollectionReportOverAllBean bean = pair.getValue();
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
		bean.setOpeningBal(bean.getOpeningBal()+openingBal);
	}

		
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionRetailerWise");
		}
	}
	
	public void collectionRetailerWiseOpenBalData(int userId, Timestamp startDate, Timestamp endDate, Connection con, Map<String, AgentCollectionReportOverAllBean> agtMap, int count) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		
		Date lastArchDate = null;
		if (startDate.after(endDate)) {
			return;
		}

		try {
//			boolean isDataFromArch = ReportUtility.checkDateLessThanArchiveDate(endDate, con);
//			if (isDataFromArch) {
//				Calendar cal1 = Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//
//				pstmt = con
//						.prepareStatement("select organization_id, opening_bal_cl_inc,opening_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					if (agtMap.get(rs.getString("organization_id")) != null) {
//						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("opening_bal"));
//						agtMap.get(rs.getString("organization_id")).setLiveOpeningBal(rs.getDouble("opening_bal_cl_inc"));
//					}
//				}
//				return;
//
//			} else {
//				lastArchDate = ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt = con
//						.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl) live_open_bal,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
//				pstmt.setDate(1, lastArchDate);
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					if (agtMap.get(rs.getString("organization_id")) != null) {
//						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
//						agtMap.get(rs.getString("organization_id")).setLiveOpeningBal(rs.getDouble("live_open_bal"));
//					}
//				}
//			}
//
//			Timestamp fromDate = startDate;

			Timestamp fromDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar lastRunDateCal = Calendar.getInstance();

			String date = ReportUtility.fetchLastRunDate("RETAILER", userId, con);
			lastRunDateCal.setTimeInMillis(sdf.parse(date).getTime());
			
			pstmt = con.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl) live_open_bal,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='RETAILER'");
			pstmt.setTimestamp(1, new Timestamp(lastRunDateCal.getTimeInMillis()));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (agtMap.get(rs.getString("organization_id")) != null) {
					agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					agtMap.get(rs.getString("organization_id")).setLiveOpeningBal(rs.getDouble("live_open_bal"));
				}
			}
			
			if(lastRunDateCal.getTimeInMillis() < startDate.getTime()) {
				lastRunDateCal.add(Calendar.DAY_OF_MONTH, 1);
				fromDate = new Timestamp(lastRunDateCal.getTimeInMillis());
			} else if(lastRunDateCal.getTimeInMillis() >= startDate.getTime() && lastRunDateCal.getTimeInMillis() < endDate.getTime()) {
				fromDate = startDate;
			} else {
				return;
			}

			// Get Account Details
			String accQry;
			String unionQuery;
			StringBuilder accountArchQry;
			StringBuilder saleRefundArchQry;
			StringBuilder saleArchQry;
			StringBuilder pwtArchQry;

			String addOrgQry = "right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on party_id=organization_id";
			String cashQry = "(select organization_id,cash from (select party_id,sum(amount) cash from st_lms_agent_cash_transaction cash,st_lms_agent_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' group by party_id) cash " + addOrgQry + ") cash";
			String chqQry = "(select organization_id,chq from (select party_id,sum(cheque_amt) chq from st_lms_agent_sale_chq chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' group by party_id) chq " + addOrgQry + ") chq";
			String chqRetQry = "(select organization_id,chq_ret from (select party_id,sum(cheque_amt) chq_ret from st_lms_agent_sale_chq  chq,st_lms_agent_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' group by party_id) chq_ret " + addOrgQry + ") chq_ret";
			String debitQry = "(select organization_id,debit from (select party_id,sum(amount) debit from st_lms_agent_debit_note debit,st_lms_agent_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' group by party_id) debit " + addOrgQry + ") debit";
			String creditQry = "(select organization_id,credit from (select party_id,sum(amount) credit from st_lms_agent_credit_note credit,st_lms_agent_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' group by party_id) credit " + addOrgQry + ") credit";

			accQry = "select cash.organization_id,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq,ifnull(chq_ret,0.0) chq_ret,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit from " + cashQry + "," + chqQry + "," + chqRetQry + "," + debitQry + "," + creditQry + " where cash.organization_id=chq.organization_id and cash.organization_id=chq_ret.organization_id and cash.organization_id=debit.organization_id and cash.organization_id=credit.organization_id and chq.organization_id=chq_ret.organization_id and chq.organization_id=debit.organization_id and chq.organization_id=credit.organization_id and chq_ret.organization_id=debit.organization_id and chq_ret.organization_id=credit.organization_id and debit.organization_id=credit.organization_id";

			if (LMSFilterDispatcher.isRepFrmSP) {
				accountArchQry = new StringBuilder("select FINAL.organization_id, sum(FINAL.cash) cash, sum(FINAL.chq) chq, sum(FINAL.chq_ret) chq_ret, sum(FINAL.debit) debit, sum(FINAL.credit) credit from (");
				unionQuery = " union all select retailer_org_id organization_id, sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt) as chq_ret,sum(debit_note) as debit ,sum(credit_note) as credit from st_rep_agent_payments where finaldate>='" + fromDate + "' and finaldate<='" + endDate + "' and  parent_id=" + userId + " group by retailer_org_id ) FINAL group by organization_id";
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
				saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + ")";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + ")";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + ")";

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder("select om.organization_id,ifnull(FINAL_TABLE.sale,0.0) sale from (select m.retailer_org_id,sum(m.sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder("select om.organization_id,ifnull(FINAL_TABLE.cancel,0.0) cancel from (select m.retailer_org_id,sum(m.cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder("select om.organization_id,ifnull(FINAL_TABLE.pwt,0.0) pwt from (select m.retailer_org_id,sum(m.pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry.append("(select aa.retailer_org_id,aa.sale as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_" + rsGame.getInt("game_id") + " drs inner join (" + saleTranDG + ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");
					cancelQry.append("(select aa.retailer_org_id,aa.cancel as cancel from(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_" + rsGame.getInt("game_id") + " drs inner join (" + cancelTranDG + ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");
					pwtQry.append("(select aa.retailer_org_id,aa.pwt as pwt from(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_" + rsGame.getInt("game_id") + " drs inner join (" + pwtTranDG + ")tx on tx.transaction_id=drs.transaction_id group by drs.retailer_org_id) aa) union all");
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				cancelQry.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				pwtQry.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on FINAL_TABLE.retailer_org_id=om.organization_id");

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder("select organization_id,sum(sale) sale from (");
					unionQuery = " union all select organization_id,sum(sale_net) as sale from st_rep_dg_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + "  group by organization_id ) Final group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry.toString());
				}
				logger.debug("-------Draw Sale Query------\n" + pstmt);

				// Draw Sale Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgSale(rs.getDouble("sale"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleRefundArchQry = new StringBuilder("select organization_id,sum(cancel) cancel from (");
					unionQuery = " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_dg_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + "  group by organization_id ) Final group by organization_id";
					saleRefundArchQry.append(cancelQry).append(unionQuery);
					pstmt = con.prepareStatement(saleRefundArchQry.toString());
				} else {
					pstmt = con.prepareStatement(cancelQry.toString());
				}
				logger.debug("-------Draw Cancel Qurey------\n" + pstmt);

				// Draw Cancel Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgCancel(rs.getDouble("cancel"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					pwtArchQry = new StringBuilder("select organization_id,sum(pwt) pwt from (");
					unionQuery = " union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_dg_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + "  group by organization_id ) Final group by organization_id";
					pwtArchQry.append(pwtQry).append(unionQuery);
					pstmt = con.prepareStatement(pwtArchQry.toString());
				} else {
					pstmt = con.prepareStatement(pwtQry.toString());
				}
				logger.debug("-------Draw Pwt Qurey------\n" + pstmt);

				// Draw Pwt Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setDgPwt(rs.getDouble("pwt"));
				}
			}

			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				logger.info("----Type Select ---" + LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select sale.retailer_org_id organization_id ,mrpAmt-mrpAmtRet  mrpAmt,  netAmt-netAmtRet netAmt from (select om.retailer_org_id,ifnull(mrpAmt,0) mrpAmt ,ifnull(netAmt,0) netAmt from (select organization_id retailer_org_id from st_lms_organization_master where parent_id=" + userId + ") om left join  (select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='" + fromDate + "'  and transaction_date<='" + endDate + "'  and user_org_id=" + userId + " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='" + fromDate + "'  and transaction_date<='" + endDate + "'  and user_org_id=" + userId + " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id) sale group by retailer_org_id) sale on om.retailer_org_id=sale.retailer_org_id) sale , (select om.retailer_org_id,ifnull(mrpAmtRet,0) mrpAmtRet ,ifnull(netAmtRet,0) netAmtRet from (select organization_id retailer_org_id from st_lms_organization_master where parent_id=" + userId + ") om left join (select retailer_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='" + fromDate + "'  and transaction_date<='" + endDate + "'  and user_org_id=" + userId + " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_ret_loose_book_transaction artx inner join (select transaction_id from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='" + fromDate + "'  and transaction_date<='" + endDate + "'  and user_org_id=" + userId + " )tx  on  artx.transaction_id= tx.transaction_id group by retailer_org_id) sale group by retailer_org_id) saleRet on om.retailer_org_id=saleRet.retailer_org_id) saleRet where saleRet.retailer_org_id=sale.retailer_org_id";
				} else if ("TICKET_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select om.organization_id,FINAL_TABLE.name,FINAL_TABLE.mrpAmt,FINAL_TABLE.netAmt from (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om left join (select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='" + fromDate + "' and date<='" + endDate + "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER') FINAL_TABLE on om.organization_id=FINAL_TABLE.organization_id";
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder("select organization_id,sum(mrpAmt) mrpAmt, sum(netAmt) netAmt from (");
					unionQuery = " union all select organization_id,sum(sale_book_mrp)-sum(ref_sale_mrp) mrpAmt ,sum(sale_book_net) -sum(ref_net_amt) as netAmt from st_rep_se_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + " group by organization_id) FINAL group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry);
				}
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSeSale(rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String transactionType = null;
				if (count == 0)
					transactionType = "transaction_type in ('PWT','PWT_AUTO')";
				else
					transactionType = "transaction_type='PWT'";

				String pwtQry = "select retailer_org_id organization_id, sum(pwt) pwt from (select om.organization_id retailer_org_id,ifnull(tx.pwt,0) pwt from (select organization_id from st_lms_organization_master where parent_id=" + userId + " ) om left join (select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt pwt inner join (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "'  and " + transactionType + " and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + " )) tx on  tx.transaction_id=pwt.transaction_id group by retailer_org_id) tx on om.organization_id=tx.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt pwt inner join (select transaction_id from st_lms_agent_transaction_master where transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "'  and " + transactionType + " and party_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + " )) tx on  tx.transaction_id=pwt.transaction_id group by retailer_org_id) pwt group by retailer_org_id";
				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					pwtArchQry = new StringBuilder("select organization_id,sum(pwt) pwt from (");
					unionQuery = " union all select organization_id,sum(pwt_net_amt) as pwt from st_rep_se_retailer where finaldate>='" + fromDate + "' and finaldate<='" + endDate + "'  and parent_id=" + userId + " group by organization_id ) FINAL group by organization_id";
					pwtArchQry.append(pwtQry).append(unionQuery);
					pstmt = con.prepareStatement(pwtArchQry.toString());
				} else {
					pstmt = con.prepareStatement(pwtQry);
				}
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setSePwt(rs.getDouble("pwt"));
				}
			}

			if (ReportUtility.isCS) {

				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + ")";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=" + userId + ")";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder("select om.organization_id,ifnull(FINAL_TABLE.sale,0.0) sale from (select m.retailer_org_id,sum(m.sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder("select om.organization_id,ifnull(FINAL_TABLE.cancel,0.0) cancel from (select m.retailer_org_id,sum(m.cancel) as cancel from (");
				while (rsProduct.next()) {
					saleQry.append("(select aa.retailer_org_id,aa.sale as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_" + rsProduct.getInt("category_id") + " drs inner join (" + saleTranCS + ")tx on drs.transaction_id=tx.transaction_id group by drs.retailer_org_id) aa) union all");
					cancelQry.append("(select aa.retailer_org_id,aa.cancel as cancel from(select drs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_" + rsProduct.getInt("category_id") + " drs inner join (" + cancelTranCS + ")tx on drs.transaction_id=tx.transaction_id group by drs.retailer_org_id) aa) union all");
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry.length());

				saleQry.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on FINAL_TABLE.retailer_org_id=om.organization_id");
				cancelQry.append(") m group by retailer_org_id )FINAL_TABLE right join (select organization_id from st_lms_organization_master where parent_id=" + userId + ") om on FINAL_TABLE.retailer_org_id=om.organization_id");

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleArchQry = new StringBuilder("select organization_id,sum(sale) sale from (");
					unionQuery = " union all select organization_id,sum(sale_net) as sale from st_rep_cs_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + "  group by organization_id ) Final group by organization_id";
					saleArchQry.append(saleQry).append(unionQuery);
					pstmt = con.prepareStatement(saleArchQry.toString());
				} else {
					pstmt = con.prepareStatement(saleQry.toString());
				}
				logger.debug("-------CS Sale Query------\n" + pstmt);
				// CS Sale Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSSale(rs.getDouble("sale"));
				}

				if (LMSFilterDispatcher.isRepFrmSP) {
					unionQuery = null;
					saleRefundArchQry = new StringBuilder("select organization_id,sum(cancel) cancel from (");
					unionQuery = " union all select organization_id,sum(ref_net_amt) as cancel from st_rep_cs_retailer where finaldate>='" + fromDate + "'  and finaldate<='" + endDate + "'  and parent_id=" + userId + "  group by organization_id ) Final group by organization_id";
					saleRefundArchQry.append(cancelQry).append(unionQuery);
					pstmt = con.prepareStatement(saleRefundArchQry.toString());
				} else {
					pstmt = con.prepareStatement(cancelQry.toString());
				}
				logger.debug("-------CS Cancel Query------\n" + pstmt);

				// CS Cancel Query
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setCSCancel(rs.getDouble("cancel"));
				}
			}
			if (ReportUtility.isOLA) {
				StringBuilder wdQry = new StringBuilder("select om.organization_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from ");
				StringBuilder wdRefQry = new StringBuilder("select om.organization_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from");
				StringBuilder depQry = new StringBuilder("select om.organization_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from");
				StringBuilder depRefQry = new StringBuilder("select om.organization_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from");

				wdQry.append("(select wrs.retailer_org_id, net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				wdRefQry.append("(select wrs.retailer_org_id,net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");
				depQry.append("(select wrs.retailer_org_id, net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				depRefQry.append("(select wrs.retailer_org_id, net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='" + fromDate + "' and transaction_date<='" + endDate + "')) wdret right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ")om on om.organization_id = wdret.retailer_org_id group by om.organization_id");

				String netGamingQry = "select om.organization_id agtOrgId, ifnull(sum(commission_calculated),0.0) netAmt from (select ret_org_id, plr_net_gaming, commission_calculated from st_ola_agt_ret_commission where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type = 'OLA_COMMISSION' and transaction_date >= '" + fromDate + "' and transaction_date <= '" + endDate + "')) bac right outer join (select organization_id from st_lms_organization_master where parent_id=" + userId + ")om on bac.ret_org_id = om.organization_id group by om.organization_id";

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
					agtMap.get(rs.getString("agtOrgId")).setWithdrawal(rs.getDouble("wdraw"));
				}
				// WithDrawal Refund Query
				pstmt = con.prepareStatement(wdRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setWithdrawalRefund(rs.getDouble("wdrawRef"));
				}

				// Deposit Query
				pstmt = con.prepareStatement(depQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setDeposit(rs.getDouble("depoAmt"));
				}
				// Deposit Refund Query
				pstmt = con.prepareStatement(depRefQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setDepositRefund(rs.getDouble("depoRefAmt"));
				}

				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(rs.getDouble("netAmt"));
				}
			}
			
			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(userId);
				Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWRetailerReportsControllerImpl.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
				for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap.entrySet()) {
					String key = String.valueOf(entry.getKey());
					if(agtMap.containsKey(key)) {
						agtMap.get(key).setIwSale(entry.getValue().getSaleAmt());
						agtMap.get(key).setIwCancel(entry.getValue().getCancelAmt());
						agtMap.get(key).setIwPwt(entry.getValue().getPwtAmt());
					}
				}
			}
			
			if (ReportUtility.isVS) {
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(userId);
				Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSRetailerReportsControllerImpl.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
				for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
					String key = String.valueOf(entry.getKey());
					if(agtMap.containsKey(key)) {
						agtMap.get(key).setVsSale(entry.getValue().getSaleAmt());
						agtMap.get(key).setVsCancel(entry.getValue().getCancelAmt());
						agtMap.get(key).setVsPwt(entry.getValue().getPwtAmt());
					}
				}
			}

			pstmt = con.prepareStatement("select CL.organization_id, sum(amount)amount, type from st_lms_cl_xcl_update_history CL inner join st_lms_organization_master om on CL.organization_id=om.organization_id where organization_type='RETAILER' and om.parent_id = " + userId + " and date_time>=? and date_time<=? group by CL.organization_id, CL.type");
			pstmt.setTimestamp(1, fromDate);
			pstmt.setTimestamp(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ("CL".equals(rs.getString("type")))
					agtMap.get(rs.getString("organization_id")).setClLimit(rs.getDouble("amount"));
				else if ("XCL".equals(rs.getString("type")))
					agtMap.get(rs.getString("organization_id")).setXclLimit(rs.getDouble("amount"));
			}
			
//			Iterator<Map.Entry<String, AgentCollectionReportOverAllBean>> itr = agtMap
//					.entrySet().iterator();
//			while (itr.hasNext()) {
//				Map.Entry<String, AgentCollectionReportOverAllBean> pair = itr
//						.next();
			for(Map.Entry<String, AgentCollectionReportOverAllBean> entrySet : agtMap.entrySet()) {
				AgentCollectionReportOverAllBean bean = entrySet.getValue();
					
				double openingBal = bean.getDgSale()
						- bean.getDgCancel()
						- bean.getDgPwt()
						- bean.getDgDirPlyPwt()
						+ bean.getSeSale()
						- bean.getSePwt()
						- bean.getSeDirPlyPwt()
						+ bean.getCSSale()
						- bean.getCSCancel()
						+ bean.getIwSale()
						- bean.getIwCancel()
						- bean.getIwPwt()
						- bean.getIwDirPlyPwt()
						+ bean.getVsSale()
						- bean.getVsCancel()
						- bean.getVsPwt()
						- bean.getVsDirPlyPwt()
						+ bean.getDeposit()
						- bean.getDepositRefund()
						- bean.getWithdrawal()
						- bean.getNetGamingComm()
						- (bean.getCash() + bean.getCheque() + bean.getCredit()
								+ bean.getBankDep() - bean.getDebit() - bean
									.getChequeReturn());
				bean.setNetTxnAmt(openingBal);
//				bean.setOpeningBal(bean.getOpeningBal() + openingBal);
//				bean.setLiveOpeningBal(bean.getClLimit() + bean.getXclLimit() - openingBal + bean.getLiveOpeningBal());
				bean.setOpeningBal(bean.getOpeningBal());
				bean.setLiveOpeningBal(bean.getLiveOpeningBal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionRetailerWise");
		}
	}
}
