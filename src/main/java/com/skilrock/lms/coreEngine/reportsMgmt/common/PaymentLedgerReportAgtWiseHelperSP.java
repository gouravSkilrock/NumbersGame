package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.skilrock.lms.web.accMgmt.common.AgentOpeningBalanceHelper;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.controller.IWAgentReportsControllerImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLEAgentReportsControllerImpl;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.controller.VSAgentReportsControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaAgentReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class PaymentLedgerReportAgtWiseHelperSP implements IPaymentLedgerReportAgtWiseHelper {
	Log logger = LogFactory.getLog(PaymentLedgerReportAgtWiseHelperSP.class);



	public void collectionDateWise(Timestamp startDate, Timestamp endDate,
			Connection con,Map<String, CollectionReportOverAllBean> agtMap, int agtOrgId)
	throws LMSException {

		ResultSet rs = null;
		PreparedStatement pstmt=null;
		if (startDate.after(endDate)) {
			return;
		}
		
		try {
			// Get Account Details

			CallableStatement cstmt = con
			.prepareCall("call cashChqPaymentDateWiseOverAll(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				//String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setCash(rs.getDouble("cash"));
				agtMap.get(dateFrDtParse).setCheque(rs.getDouble("chq"));


			}			
			cstmt = con
			.prepareCall("call creditDebitPaymentDateWiseOverAll(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();			

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				//String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setCredit(rs.getDouble("credit"));
				agtMap.get(dateFrDtParse).setDebit(rs.getDouble("debit"));
				agtMap.get(dateFrDtParse).setTraining(rs.getDouble("training"));
				agtMap.get(dateFrDtParse).setIncentive(rs.getDouble("incentive"));
				agtMap.get(dateFrDtParse).setOthers(rs.getDouble("others"));

			}
			cstmt = con
			.prepareCall("call chqRetBankPaymentDateWiseOverAll(?,?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();	

			while (rs.next()) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd");
				String dateFrDtParse =dateformat.format(rs.getDate("date"));
				//String agtOrgId = rs.getString("organization_id");

				agtMap.get(dateFrDtParse).setChequeReturn(rs.getDouble("chq_ret"));
				agtMap.get(dateFrDtParse).setBankDep(rs.getDouble("bank"));
			}

			if (ReportUtility.isDG) {

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
					.prepareCall("call saleCancelPaymentDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
								"yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));
						//String agtOrgId = rs.getString("organization_id");

						agtMap.get(dateFrDtParse).setDgSale(agtMap.get(dateFrDtParse).getDgSale()+rs.getDouble("sale"));
						agtMap.get(dateFrDtParse).setDgCancel(agtMap.get(dateFrDtParse).getDgCancel()+rs.getDouble("cancel"));

					}

					cstmt = con
					.prepareCall("call pwtPaymentDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));
						//String agtOrgId = rs.getString("organization_id");

						agtMap.get(dateFrDtParse).setDgPwt(agtMap.get(dateFrDtParse).getDgPwt() + rs.getDouble("pwt"));

					}
				}

				cstmt = con
				.prepareCall("call DirPlayerpwtPaymentDateWisePerGame(?,?,?)");
				cstmt.setTimestamp(1, startDate);
				cstmt.setTimestamp(2, endDate);

				cstmt.setInt(3, agtOrgId);
				rs = cstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					//String agtOrgId = rs.getString("organization_id");

					agtMap.get(dateFrDtParse).setDgDirPlyPwt(
							rs.getDouble("pwtDir"));

				}
				// Draw Direct Player Qry



			}
			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
					.prepareCall("call scratchSaleCancelDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4,agtOrgId);
					logger.debug("***Scratch Sale Query*** \n" + cstmt);
					rs = cstmt.executeQuery();
					//String agtOrgId = null;
					if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
						while (rs.next()) {
							//agtOrgId = rs.getString("organization_id");

							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
							String dateFrDtParse =dateformat.format(rs.getDate("date"));
							agtMap.get(dateFrDtParse).setSeSale(agtMap.get(dateFrDtParse).getSeSale()+rs.getDouble("sale"));
							agtMap.get(dateFrDtParse).setSeCancel(agtMap.get(dateFrDtParse).getSeCancel()+ rs.getDouble("cancel"));
						}}
					else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));

						agtMap.get(dateFrDtParse).setSeSale(agtMap.get(dateFrDtParse).getSeSale()+ rs.getDouble("sale"));
					}

					cstmt = con
					.prepareCall("call scratchPwtDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					logger.debug("***Scratch Pwt Query*** \n" + cstmt);
					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));
						//String agtOrgId = rs.getString("organization_id");
						agtMap.get(dateFrDtParse).setSePwt(agtMap.get(dateFrDtParse).getSePwt()+rs.getDouble("pwt"));


					}

					cstmt = con
					.prepareCall("call scratchDirPwtPlayerDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					logger.debug("***Scratch DirectPlayerPwt Query*** \n" + cstmt);
					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));
						//String agtOrgId = rs.getString("organization_id");
						agtMap.get(dateFrDtParse).setSeDirPlyPwt(agtMap.get(dateFrDtParse).getSeDirPlyPwt()+rs.getDouble("pwtDir"));


					}

				}



			}
			if (ReportUtility.isCS) {

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				while (rsProduct.next()) {
					int catId = rsProduct.getInt("category_id");
					cstmt = con.prepareCall("call csCollectionDateWisePerCategory(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4,agtOrgId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd");
						String dateFrDtParse =dateformat.format(rs.getDate("date"));
						//String agtOrgId = rs.getString("organization_id");

						agtMap.get(dateFrDtParse).setCSSale(agtMap.get(dateFrDtParse).getCSSale()+rs.getDouble("sale"));
						agtMap.get(dateFrDtParse).setCSCancel(agtMap.get(dateFrDtParse).getCSCancel()+rs.getDouble("cancel"));

					}

				}
			}


			if(ReportUtility.isOLA){
				
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				
				OlaOrgReportRequestBean requestBean=new OlaOrgReportRequestBean();
				requestBean.setFromDate(startDate.toString());
				requestBean.setToDate(endDate.toString());
				requestBean.setOrgId(agtOrgId);
				Map<String, OlaOrgReportResponseBean> olaResponseBeanMap=OlaAgentReportControllerImpl.fetchDepositWithdrawlSingleAgentDateWise(requestBean, con);
				
				for(Map.Entry<String, OlaOrgReportResponseBean> entry:olaResponseBeanMap.entrySet()){
					String txndate=entry.getKey();
					OlaOrgReportResponseBean olaResponseBean=entry.getValue();
					if(agtMap.containsKey(txndate)){
						agtMap.get(txndate).setWithdrawal(olaResponseBean.getNetWithdrawalAmt());
						agtMap.get(txndate).setDeposit(olaResponseBean.getNetDepositAmt());
					}
				}
				
				String netGamingQry = "select om.parent_id parent_id, sum(netGamingAmt) netAmt,date(transaction_date) date from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt,transaction_date from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"+ startDate+ "' and transaction_date<='"+ endDate+ "') wdret inner join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id where om.parent_id="+ agtOrgId + " group by date(transaction_date)";
				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setNetGamingComm(rs.getDouble("netAmt"));
				}
				
				
				/*
				
				
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
				
				
				StringBuilder unionQuery=null;
				StringBuilder mainQuery=null;		
				
				// Withdrawal Query
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select parent_id,sum(wdraw) wdraw,date from (");
					unionQuery=new StringBuilder(" union all select parent_id agtOrgId,ifnull(sum(withdrawal_net_amt),0) as wdraw ,finaldate date from st_rep_ola_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId+" group by finaldate) repTable group by date");
					mainQuery.append(wdQry.toString()).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				}
				else
				{
					pstmt = con.prepareStatement(wdQry.toString());
				}
				
				logger.debug("-------Withdrawal Query------\n" + wdQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setWithdrawal(rs.getDouble("wdraw"));
				}
				
				// WithDrawal Refund Query
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select parent_id,sum(wdrawRef) wdrawRef,date from (");
					unionQuery=new StringBuilder(" union all select parent_id agtOrgId,ifnull(sum(ref_withdrawal_net_amt),0) as wdrawRef ,finaldate date from st_rep_ola_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId+" group by finaldate) repTable group by date");
					mainQuery.append(wdRefQry.toString()).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				}
				else
				{
					pstmt = con.prepareStatement(wdRefQry.toString());
				}
				
				logger.debug("-------WithDrawal Refund Query------\n"
						+ wdRefQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setWithdrawalRefund(rs.getDouble("wdrawRef"));
				}
				
				// Deposit Query
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select parent_id,sum(depoAmt) depoAmt ,date from (");
					unionQuery=new StringBuilder(" union all select parent_id agtOrgId,ifnull(sum(deposit_net),0) as depoAmt ,finaldate date from st_rep_ola_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId+" group by finaldate) repTable group by date");
					mainQuery.append(depQry.toString()).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				}
				else
				{
					pstmt = con.prepareStatement(depQry.toString());
				}
				
				logger.debug("-------Deposit Query------\n" + depQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setDeposit(rs.getDouble("depoAmt"));
				}
				
				// Deposit Refund Query
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select parent_id,sum(depoRefAmt) depoRefAmt ,date from (");
					unionQuery=new StringBuilder(" union all select parent_id agtOrgId,ifnull(sum(ref_deposit_net_amt),0) as depoRefAmt ,finaldate date from st_rep_ola_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId+" group by finaldate) repTable group by date");
					mainQuery.append(depRefQry.toString()).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				}
				else
				{
					pstmt = con.prepareStatement(depRefQry.toString());
				}
				
				logger.debug("-------Deposit Refund Query------\n" + depRefQry);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setDepositRefund(rs.getDouble("depoRefAmt"));
				}

				// net gaming commission query
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select parent_id,sum(netAmt) netAmt ,date from (");
					unionQuery=new StringBuilder(" union all select parent_id agtOrgId,ifnull(sum(net_gaming_net_comm),0) as netAmt ,finaldate date from st_rep_ola_retailer where finaldate>='"+startDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId+" group by finaldate) repTable group by date");
					mainQuery.append(netGamingQry.toString()).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				}
				else
				{
					pstmt = con.prepareStatement(netGamingQry.toString());
				}
				rs = pstmt.executeQuery();
		
				logger.debug("-------Net Gaming Query------\n" + netGamingQry);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(dateformat.format(rs.getDate("date")))
							.setNetGamingComm(rs.getDouble("netAmt"));
				}*/
			}
			
			if(ReportUtility.isSLE){
				SLEOrgReportRequestBean requestBean=new SLEOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<String, SLEOrgReportResponseBean> sleResponseBeanMap=SLEAgentReportsControllerImpl.fetchSalePWTDayWiseAllGameSingleAgent(requestBean, con);
				
				for(Map.Entry<String, SLEOrgReportResponseBean> entry : sleResponseBeanMap.entrySet()){
					String txndate=entry.getKey();
					SLEOrgReportResponseBean sleResponseBean = entry.getValue();
					if(agtMap.containsKey(txndate)){
						agtMap.get(txndate).setSleSale(sleResponseBean.getSaleAmt());
						agtMap.get(txndate).setSleCancel(sleResponseBean.getCancelAmt());
						agtMap.get(txndate).setSlePwt(sleResponseBean.getPwtAmt());
						agtMap.get(txndate).setSleDirPlyPwt(sleResponseBean.getPwtDirAmt());
					}
				}
			}
			
			if(ReportUtility.isIW){
				IWOrgReportRequestBean requestBean=new IWOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<String, IWOrgReportResponseBean> iwResponseBeanMap=IWAgentReportsControllerImpl.fetchSalePWTDayWiseAllGameSingleAgent(requestBean, con);
				
				for(Map.Entry<String, IWOrgReportResponseBean> entry : iwResponseBeanMap.entrySet()){
					String txndate=entry.getKey();
					IWOrgReportResponseBean iwResponseBean = entry.getValue();
					if(agtMap.containsKey(txndate)){
						agtMap.get(txndate).setIwSale(iwResponseBean.getSaleAmt());
						agtMap.get(txndate).setIwCancel(iwResponseBean.getCancelAmt());
						agtMap.get(txndate).setIwPwt(iwResponseBean.getPwtAmt());
						agtMap.get(txndate).setIwDirPlyPwt(iwResponseBean.getPwtDirAmt());
					}
				}
			}

			if (ReportUtility.isVS) {
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<String, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseAllGameSingleAgent(requestBean, con);

				for (Map.Entry<String, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
					String txndate = entry.getKey();
					VSOrgReportResponseBean vsResponseBean = entry.getValue();
					if (agtMap.containsKey(txndate)) {
						agtMap.get(txndate).setVsSale(vsResponseBean.getSaleAmt());
						agtMap.get(txndate).setVsCancel(vsResponseBean.getCancelAmt());
						agtMap.get(txndate).setVsPwt(vsResponseBean.getPwtAmt());
						agtMap.get(txndate).setVsDirPlyPwt(vsResponseBean.getPwtDirAmt());
					}
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
		ResultSet rs = null;

		CollectionReportOverAllBean agentBean = null;
		CompleteCollectionBean gameBean;
		try {
			if (ReportUtility.isDG) {
				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					CallableStatement cstmt = con
							.prepareCall("call drawCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);					
					logger.debug("-------Indivisual Game Qry-------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("parent_id");
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
						gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					CallableStatement cstmt = con
							.prepareCall("call scratchCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					logger.debug("-------Indivisual Game Qry-------\n" + cstmt);
					rs = cstmt.executeQuery();
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
						gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String prodQry = "select category_id,category_code from st_cs_product_category_master";
				PreparedStatement prodPstmt = con.prepareStatement(prodQry);
				ResultSet rsProd = prodPstmt.executeQuery();
				while (rsProd.next()) {
					int catId = rsProd.getInt("category_id");
					String catName = rsProd.getString("category_code");
					CallableStatement cstmt = con
							.prepareCall("call csCollectionAgentWisePerGame(?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);					
					logger.debug("-------Indivisual Category Qry-------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("parent_id");
						agentBean = agtMap.get(agtOrgId);
						Map<String, CompleteCollectionBean> gameMap = agentBean
								.getGameBeanMap();
						if (gameMap == null) {
							gameMap = new HashMap<String, CompleteCollectionBean>();
							agentBean.setGameBeanMap(gameMap);
						}
						gameBean = gameMap.get(catName);
						if (gameBean == null) {
							gameBean = new CompleteCollectionBean();
							gameMap.put(catName, gameBean);
						}
						gameBean.setOrgName(catName);
						gameBean.setDrawSale(rs.getDouble("sale"));
						gameBean.setDrawCancel(rs.getDouble("cancel"));
						gameBean.setDrawPwt(rs.getDouble("pwt"));
						gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
					}
				}
			
				
				
				/*CSQry = new StringBuilder(
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

				}*/

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
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,int agtOrgId) throws LMSException {
		
		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		//Map<String, CollectionReportOverAllBean> agtMapOpenningBalance = new LinkedHashMap<String, CollectionReportOverAllBean>();
		Map<String, CollectionReportOverAllBean> agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		//Map<String, CollectionReportOverAllBean> resultMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;
        Double openingBalance=0.0;
		try {
			con = DBConnect.getConnection();
			//String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId;
			//pstmt = con.prepareStatement(agtOrgQry);
			//rsRetOrg = pstmt.executeQuery();
			
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			Calendar nextCal = Calendar.getInstance();
			startCal.setTimeInMillis(startDate.getTime());
			endCal.setTimeInMillis(endDate.getTime());
			nextCal.setTimeInMillis(startDate.getTime());
			
			//nextCal.add(Calendar.DAY_OF_MONTH, 1);
		//	startDate = new Timestamp(sdf.parse(sdf.format(startCal.getTime())).getTime());
		//	endDate = new Timestamp(sdf.parse(sdf.format(endCal.getTime())).getTime());
		//	collBean = new CollectionReportOverAllBean();
		//	collBean.setAgentName(new Timestamp(nextCal.getTimeInMillis()).toString().split(" ")[0]);//its date for this report
			
			
			//agtMapOpenningBalance.put(agtOrgId+"", collBean);
			AgentOpeningBalanceHelper agentOpenHelper=new AgentOpeningBalanceHelper();
			openingBalance=agentOpenHelper.collectionAgentWise(deployDate, startDate, con,agtOrgId);
//			openingBalance=agentOpenHelper.collectionAgentWise(deployDate, new Timestamp(startDate.getTime()-1000), con,agtOrgId);
	/*		Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr = agtMapOpenningBalance
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
			*/
			logger.info("AgentId=="+agtOrgId+"==Openning Balance="+openingBalance+"=date="+startDate);
			if (endCal.after(startCal)) {
				while(nextCal.compareTo(endCal)<=0){
					
				collBean = new CollectionReportOverAllBean();
				String AgentName=new Timestamp(nextCal.getTimeInMillis()).toString().split(" ")[0];//its date for this report
				collBean.setAgtId(agtOrgId);
				
				agtMap.put(AgentName, collBean);
				
				nextCal.add(Calendar.DAY_OF_MONTH, 1);//increment the date
				
				}
			}
			
			
			collectionDateWise(startDate, endDate, con,agtMap,agtOrgId);
			
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
		+ bean.getSleSale()
		- bean.getSleCancel()
		- bean.getSlePwt()
		- bean.getSleDirPlyPwt()
		+ bean.getIwSale()
		- bean.getIwCancel()
		- bean.getIwPwt()
		- bean.getIwDirPlyPwt()
		+ bean.getVsSale()
		- bean.getVsCancel()
		- bean.getVsPwt()
		- bean.getVsDirPlyPwt()
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