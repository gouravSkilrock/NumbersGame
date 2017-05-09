package com.skilrock.lms.web.accMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.OrganizationBalanceData;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
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

public class AgentOpeningBalanceHelper {
	Log logger = LogFactory.getLog(AgentOpeningBalanceHelper.class);

	public void collectionAgentWiseOpenningBal(Timestamp startDate,
			Timestamp endDate, Connection con,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		ResultSet rs = null;
		Date lastArchDate = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		if (startDate.after(endDate)) {
			return;
		}
		Calendar calendar = Calendar.getInstance();

		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);
		checkCal.set(Calendar.HOUR_OF_DAY, 0);
		checkCal.set(Calendar.MINUTE, 0);
		checkCal.set(Calendar.SECOND, 0);
		checkCal.set(Calendar.MILLISECOND, 0);
		try {
			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(checkCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (agtMap.get(rs.getString("organization_id")) != null) {
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					}
				}
				return;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(lastRunCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (agtMap.get(rs.getString("organization_id")) != null) {
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					}
				}
			}

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);
			
			Timestamp fromDate = new Timestamp(cal.getTimeInMillis());

			// Get Account Details
			cstmt = con
					.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, fromDate);
			cstmt.setTimestamp(2, endDate);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				if(agtMap.containsKey(agtOrgId)) {
					agtMap.get(agtOrgId).setCash(rs.getDouble("cash"));
					agtMap.get(agtOrgId).setCheque(rs.getDouble("chq"));
					agtMap.get(agtOrgId).setChequeReturn(rs.getDouble("chq_ret"));
					agtMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
					agtMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
					agtMap.get(agtOrgId).setBankDep(rs.getDouble("bank"));
				}
			}

			if (ReportUtility.isDG) {
				// Game Master Query
				// String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con
						.prepareStatement(ReportUtility
								.getDrawGameMapQuery(fromDate));
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call drawCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("parent_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						if(agtMap.containsKey(agtOrgId)) {
							agtMap.get(agtOrgId).setDgSale(
									agtMap.get(agtOrgId).getDgSale() + sale);
							agtMap.get(agtOrgId).setDgCancel(
									agtMap.get(agtOrgId).getDgCancel() + cancel);
							agtMap.get(agtOrgId).setDgPwt(
									agtMap.get(agtOrgId).getDgPwt() + pwt);
							agtMap.get(agtOrgId).setDgDirPlyPwt(
									agtMap.get(agtOrgId).getDgDirPlyPwt()
											+ rs.getDouble("pwtDir"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call scratchCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						if(agtMap.containsKey(agtOrgId)) {
							agtMap.get(agtOrgId).setSeSale(
									agtMap.get(agtOrgId).getSeSale()
											+ (sale - cancel));
							agtMap.get(agtOrgId).setSePwt(
									agtMap.get(agtOrgId).getSePwt() + pwt);
							agtMap.get(agtOrgId).setSeDirPlyPwt(
									agtMap.get(agtOrgId).getSeDirPlyPwt()
											+ rs.getDouble("pwtDir"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				while (rsProduct.next()) {
					int catId = rsProduct.getInt("category_id");
					cstmt = con
							.prepareCall("call csCollectionAgentWisePerCategory(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						if(agtMap.containsKey(rs.getString("parent_id"))) {
							agtMap.get(rs.getString("parent_id")).setCSSale(
									agtMap.get(rs.getString("parent_id"))
											.getCSSale()
											+ rs.getDouble("sale"));
							agtMap.get(rs.getString("parent_id")).setCSCancel(
									agtMap.get(rs.getString("parent_id"))
											.getCSCancel()
											+ rs.getDouble("cancel"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isOLA) {
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(fromDate.toString());
				requestBean.setToDate(endDate.toString());
				Map<Integer, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl
						.fetchDepositWithdrawlMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, OlaOrgReportResponseBean> entry : olaResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					OlaOrgReportResponseBean olaResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setWithdrawal(
								olaResponseBean.getNetWithdrawalAmt());
						agtMap.get(orgId).setDeposit(
								olaResponseBean.getNetDepositAmt());
					}
				}
				
				String netGamingQry = "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id";
				
				//net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while(rs.next()){
					if (agtMap.containsKey(rs.getString("agtOrgId"))) {
						agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
								rs.getDouble("netAmt"));
					}
					
				}
				/*
				 * StringBuilder olaQuery = new StringBuilder(
				 * "select WID.agtOrgId, wdraw,wdrawRef,depoAmt,depoRefAmt,netAmt from (select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from (select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WID, (select om.parent_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from (select wrs.retailer_org_id, agent_net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WIDREF,(select om.parent_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEP,(select om.parent_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from (select wrs.retailer_org_id, agent_net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEPREF,(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)NETGAME where WID.agtOrgId=WIDREF.agtOrgId and WIDREF.agtOrgId =DEP.agtOrgId and DEP.agtOrgId =DEPREF.agtOrgId and DEPREF.agtOrgId=NETGAME.agtOrgId and NETGAME.agtOrgId=WID.agtOrgId"
				 * );
				 * 
				 * StringBuilder unionQuery=null; StringBuilder mainQuery=null;
				 * 
				 * if(LMSFilterDispatcher.isRepFrmSP){ mainQuery=new
				 * StringBuilder(
				 * "select agtOrgId,sum(wdraw) wdraw,sum(wdrawRef) wdrawRef,sum(depoAmt) depoAmt,sum(depoRefAmt)depoRefAmt,sum(netAmt) netAmt from ("
				 * ); unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId , sum(withdrawal_net_amt) wdraw , sum(ref_withdrawal_net_amt) wdrawRef , sum(deposit_net) depoAmt  , sum(ref_deposit_net_amt) depoRefAmt, sum(net_gaming_net_comm) netAmt from st_rep_ola_retailer where finaldate>='"
				 * +fromDate+"' and finaldate<='"+endDate+
				 * "' group by parent_id) repTable group by agtOrgId");
				 * mainQuery
				 * .append(olaQuery.toString()).append(unionQuery.toString());
				 * pstmt = con.prepareStatement(mainQuery.toString()); } else {
				 * pstmt = con.prepareStatement(olaQuery.toString()); } rs =
				 * pstmt.executeQuery(); while (rs.next()) {
				 * agtMap.get(rs.getString("agtOrgId")).setWithdrawal(
				 * rs.getDouble("wdraw"));
				 * agtMap.get(rs.getString("agtOrgId")).setWithdrawalRefund(
				 * rs.getDouble("wdrawRef"));
				 * agtMap.get(rs.getString("agtOrgId")).setDeposit(
				 * rs.getDouble("depoAmt"));
				 * agtMap.get(rs.getString("agtOrgId")).setDepositRefund(
				 * rs.getDouble("depoRefAmt"));
				 * agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
				 * rs.getDouble("netAmt")); }
				 */
				/*
				 * 
				 * // Wallet Master Query String walletQry =
				 * "select wallet_id from st_ola_wallet_master";
				 * PreparedStatement walletPstmt =
				 * con.prepareStatement(walletQry); ResultSet rsWallet =
				 * walletPstmt.executeQuery();
				 * 
				 * while (rsWallet.next()) { int walletId =
				 * rsWallet.getInt("wallet_id"); cstmt = con
				 * .prepareCall("call OLACollectionAgentWisePerWallet(?,?,?)");
				 * cstmt.setTimestamp(1, startDate); cstmt.setTimestamp(2,
				 * endDate); cstmt.setInt(3, walletId); rs =
				 * cstmt.executeQuery(); String agtOrgId = null; double sale =
				 * 0, cancel = 0, pwt = 0; while (rs.next()) { agtOrgId =
				 * rs.getString("parent_id"); sale = rs.getDouble("withdraw");
				 * cancel = rs.getDouble("deposit");
				 * agtMap.get(agtOrgId).setDgSale(
				 * agtMap.get(agtOrgId).getDgSale() + sale);
				 * agtMap.get(agtOrgId).setDgCancel(
				 * agtMap.get(agtOrgId).getDgCancel() + cancel);
				 * agtMap.get(agtOrgId).setDgPwt(
				 * agtMap.get(agtOrgId).getDgPwt() + pwt);
				 * agtMap.get(agtOrgId).setDgDirPlyPwt(
				 * agtMap.get(agtOrgId).getDgDirPlyPwt() +
				 * rs.getDouble("pwtDir")); } }
				 */
			}

			if (ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl
						.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					SLEOrgReportResponseBean sleResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setSleSale(
								sleResponseBean.getSaleAmt());
						agtMap.get(orgId).setSleCancel(
								sleResponseBean.getCancelAmt());
						agtMap.get(orgId)
								.setSlePwt(sleResponseBean.getPwtAmt());
						agtMap.get(orgId).setSleDirPlyPwt(
								sleResponseBean.getPwtDirAmt());
					}
				}
			}
			
			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					IWOrgReportResponseBean iwResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setIwSale(iwResponseBean.getSaleAmt());
						agtMap.get(orgId).setIwCancel(iwResponseBean.getCancelAmt());
						agtMap.get(orgId).setIwPwt(iwResponseBean.getPwtAmt());
						agtMap.get(orgId).setIwDirPlyPwt(iwResponseBean.getPwtDirAmt());
					}
				}
			}
			
			if (ReportUtility.isVS) {
				String gameQry = ReportUtility.getVSGameMapQuery(fromDate);		
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);		
				ResultSet rsGame = gamePstmt.executeQuery();		
				while(rsGame.next()){
					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(fromDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(rsGame.getInt("game_id"));
					Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);
	
					for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						VSOrgReportResponseBean vsResponseBean = entry.getValue();
						if (agtMap.containsKey(orgId)) {
							agtMap.get(orgId).setVsSale(agtMap.get(orgId).getVsSale()+ vsResponseBean.getSaleAmt());
							agtMap.get(orgId).setVsCancel(agtMap.get(orgId).getVsCancel()+ vsResponseBean.getCancelAmt());
							agtMap.get(orgId).setVsPwt(agtMap.get(orgId).getVsPwt()+ vsResponseBean.getPwtAmt());
							agtMap.get(orgId).setVsDirPlyPwt(agtMap.get(orgId).getVsDirPlyPwt()+ vsResponseBean.getPwtDirAmt());
						}
					}
				}
			}

			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr = agtMap
					.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, CollectionReportOverAllBean> pair = itr
						.next();
				CollectionReportOverAllBean bean = pair.getValue();
				double openingBal = bean.getOpeningBal()
						+ bean.getDgSale()
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
								.getChequeReturn());

				bean.setOpeningBal(0.0);
				bean.setCash(0.0);
				bean.setCheque(0.0);
				bean.setChequeReturn(0.0);
				bean.setCredit(0.0);
				bean.setDebit(0.0);
				bean.setBankDep(0.0);
				if (ReportUtility.isDG) {
					bean.setDgSale(0.0);
					bean.setDgPwt(0.0);
					bean.setDgCancel(0.0);
					bean.setDgDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					bean.setSeSale(0.0);
					bean.setSePwt(0.0);
					bean.setSeDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					bean.setCSSale(0.0);
					bean.setCSCancel(0.0);
				}
				if (ReportUtility.isOLA) {
					bean.setDeposit(0.0);
					bean.setDepositRefund(0.0);
					bean.setWithdrawal(0.0);
					bean.setWithdrawalRefund(0.0);
					bean.setNetGamingComm(0.0);
				}
				if (ReportUtility.isSLE) {
					bean.setSleSale(0.0);
					bean.setSlePwt(0.0);
					bean.setSleCancel(0.0);
					bean.setSleDirPlyPwt(0.0);
				}
				if (ReportUtility.isIW) {
					bean.setIwSale(0.0);
					bean.setIwPwt(0.0);
					bean.setIwCancel(0.0);
					bean.setIwDirPlyPwt(0.0);
				}
				if (ReportUtility.isVS) {
					bean.setVsSale(0.0);
					bean.setVsPwt(0.0);
					bean.setVsCancel(0.0);
					bean.setVsDirPlyPwt(0.0);
				}

				bean.setOpeningBal(openingBal);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closeCstmt(cstmt);
		}
	}
	
	public void collectionAgentWiseBalanceData(Timestamp startDate, Timestamp endDate, Connection con, Map<String, OrganizationBalanceData> agtMap) throws LMSException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		Date lastArchDate = null;
		if (startDate.after(endDate)) {
			return;
		}

		try {
			Timestamp fromDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar lastRunDateCal = Calendar.getInstance();

			// String date = ReportUtility.fetchLastRunDate(con);

			String date = ReportUtility.fetchLastRunDate("AGENT", -1, con);
			lastRunDateCal.setTimeInMillis(sdf.parse(date).getTime());
				
			pstmt = con.prepareStatement("select organization_id, (opening_bal_cl_inc - net_amount_transaction + cl + xcl) live_open_bal, (opening_bal + net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
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
			cstmt = con.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, fromDate);
			cstmt.setTimestamp(2, endDate);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				if (agtMap.containsKey(agtOrgId)) {
					agtMap.get(agtOrgId).setCash(rs.getDouble("cash"));
					agtMap.get(agtOrgId).setCheque(rs.getDouble("chq"));
					agtMap.get(agtOrgId).setChequeReturn(rs.getDouble("chq_ret"));
					agtMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
					agtMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
					agtMap.get(agtOrgId).setBankDep(rs.getDouble("bank"));
				}
			}

			if (ReportUtility.isDG) {
				// Game Master Query
				// String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(ReportUtility.getDrawGameMapQuery(fromDate));
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con.prepareCall("call drawCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("parent_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						if (agtMap.containsKey(agtOrgId)) {
							agtMap.get(agtOrgId).setDgSale(agtMap.get(agtOrgId).getDgSale() + sale);
							agtMap.get(agtOrgId).setDgCancel(agtMap.get(agtOrgId).getDgCancel() + cancel);
							agtMap.get(agtOrgId).setDgPwt(agtMap.get(agtOrgId).getDgPwt() + pwt);
							agtMap.get(agtOrgId).setDgDirPlyPwt(agtMap.get(agtOrgId).getDgDirPlyPwt() + rs.getDouble("pwtDir"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con.prepareCall("call scratchCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						if (agtMap.containsKey(agtOrgId)) {
							agtMap.get(agtOrgId).setSeSale(agtMap.get(agtOrgId).getSeSale() + (sale - cancel));
							agtMap.get(agtOrgId).setSePwt(agtMap.get(agtOrgId).getSePwt() + pwt);
							agtMap.get(agtOrgId).setSeDirPlyPwt(agtMap.get(agtOrgId).getSeDirPlyPwt() + rs.getDouble("pwtDir"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				while (rsProduct.next()) {
					int catId = rsProduct.getInt("category_id");
					cstmt = con.prepareCall("call csCollectionAgentWisePerCategory(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						if (agtMap.containsKey(rs.getString("parent_id"))) {
							agtMap.get(rs.getString("parent_id")).setCSSale(agtMap.get(rs.getString("parent_id")).getCSSale() + rs.getDouble("sale"));
							agtMap.get(rs.getString("parent_id")).setCSCancel(agtMap.get(rs.getString("parent_id")).getCSCancel() + rs.getDouble("cancel"));
						}
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isOLA) {
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(fromDate.toString());
				requestBean.setToDate(endDate.toString());
				Map<Integer, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl.fetchDepositWithdrawlMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, OlaOrgReportResponseBean> entry : olaResponseBeanMap.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					OlaOrgReportResponseBean olaResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setWithdrawal(olaResponseBean.getNetWithdrawalAmt());
						agtMap.get(orgId).setDeposit(olaResponseBean.getNetDepositAmt());
					}
				}
			}

			if (ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					SLEOrgReportResponseBean sleResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setSleSale(sleResponseBean.getSaleAmt());
						agtMap.get(orgId).setSleCancel(sleResponseBean.getCancelAmt());
						agtMap.get(orgId).setSlePwt(sleResponseBean.getPwtAmt());
						agtMap.get(orgId).setSleDirPlyPwt(sleResponseBean.getPwtDirAmt());
					}
				}
			}

			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					IWOrgReportResponseBean iwResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setIwSale(iwResponseBean.getSaleAmt());
						agtMap.get(orgId).setIwCancel(iwResponseBean.getCancelAmt());
						agtMap.get(orgId).setIwPwt(iwResponseBean.getPwtAmt());
						agtMap.get(orgId).setIwDirPlyPwt(iwResponseBean.getPwtDirAmt());
					}
				}
			}
			
			if (ReportUtility.isVS) {
				String gameQry = ReportUtility.getVSGameMapQuery(fromDate);		
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);		
				ResultSet rsGame = gamePstmt.executeQuery();		
				while(rsGame.next()){
					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(fromDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(rsGame.getInt("game_id"));
					Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);
	
					for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						VSOrgReportResponseBean vsResponseBean = entry.getValue();
						if (agtMap.containsKey(orgId)) {
							agtMap.get(orgId).setVsSale(agtMap.get(orgId).getVsSale()+ vsResponseBean.getSaleAmt());
							agtMap.get(orgId).setVsCancel(agtMap.get(orgId).getVsCancel()+ vsResponseBean.getCancelAmt());
							agtMap.get(orgId).setVsPwt(agtMap.get(orgId).getVsPwt()+ vsResponseBean.getPwtAmt());
							agtMap.get(orgId).setVsDirPlyPwt(agtMap.get(orgId).getVsDirPlyPwt()+ vsResponseBean.getPwtDirAmt());
						}
					}
				}
			}
			
//			Timestamp tempTimestamp = new Timestamp(endDate.getTime() + (((23 * 60 * 60) + (59 * 60) + 59) * 1000));
			pstmt = con.prepareStatement("select CL.organization_id, sum(amount)amount, type from st_lms_cl_xcl_update_history CL inner join st_lms_organization_master om on CL.organization_id=om.organization_id where organization_type='AGENT' and date_time>=? and date_time<=? group by CL.organization_id, CL.type");
			pstmt.setTimestamp(1, fromDate);
			pstmt.setTimestamp(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ("CL".equals(rs.getString("type"))) {
					if(agtMap.containsKey(rs.getString("organization_id")))
						agtMap.get(rs.getString("organization_id")).setClLimit(rs.getDouble("amount"));
				}
				else if ("XCL".equals(rs.getString("type"))) {
					if(agtMap.containsKey(rs.getString("organization_id")))
						agtMap.get(rs.getString("organization_id")).setXclLimit(rs.getDouble("amount"));
				}
			}

			for(Map.Entry<String, OrganizationBalanceData> entrySet : agtMap.entrySet()) {
				OrganizationBalanceData bean = entrySet.getValue();
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
						- (bean.getCash() + bean.getCheque() + bean.getCredit() + bean.getBankDep() - bean.getDebit() - bean.getChequeReturn());
				
				if(bean.getOrgId() == 1256)
				{
					System.out.println("asdasd");
				}
				
				bean.setNetTxnAmt(openingBal);
//				bean.setLiveOpeningBal(bean.getClLimit() + bean.getXclLimit() - openingBal + bean.getLiveOpeningBal());
				bean.setLiveOpeningBal(bean.getLiveOpeningBal());
//				bean.setOpeningBal(openingBal + bean.getOpeningBal());
				bean.setOpeningBal(bean.getOpeningBal());

				bean.setCash(0.0);
				bean.setCheque(0.0);
				bean.setChequeReturn(0.0);
				bean.setCredit(0.0);
				bean.setDebit(0.0);
				bean.setBankDep(0.0);
				if (ReportUtility.isDG) {
					bean.setDgSale(0.0);
					bean.setDgPwt(0.0);
					bean.setDgCancel(0.0);
					bean.setDgDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					bean.setSeSale(0.0);
					bean.setSePwt(0.0);
					bean.setSeDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					bean.setCSSale(0.0);
					bean.setCSCancel(0.0);
				}
				if (ReportUtility.isOLA) {
					bean.setDeposit(0.0);
					bean.setDepositRefund(0.0);
					bean.setWithdrawal(0.0);
					bean.setWithdrawalRefund(0.0);
					bean.setNetGamingComm(0.0);
				}
				if (ReportUtility.isSLE) {
					bean.setSleSale(0.0);
					bean.setSlePwt(0.0);
					bean.setSleCancel(0.0);
					bean.setSleDirPlyPwt(0.0);
				}
				if (ReportUtility.isIW) {
					bean.setIwSale(0.0);
					bean.setIwPwt(0.0);
					bean.setIwCancel(0.0);
					bean.setIwDirPlyPwt(0.0);
				}
				if (ReportUtility.isVS) {
					bean.setVsSale(0.0);
					bean.setVsPwt(0.0);
					bean.setVsCancel(0.0);
					bean.setVsDirPlyPwt(0.0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closeCstmt(cstmt);
		}
	}

	public void collectionAgentWiseLiveOpenningBal(Timestamp startDate,
			Timestamp endDate, Connection con,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		ResultSet rs = null;
		Date lastArchDate = null;
		PreparedStatement pstmt = null;
		if (startDate.after(endDate)) {
			return;
		}

		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);

		try {
//			boolean isDataFromArch = ReportUtility.checkDateLessThanLastRunDate(endDate, -1, "AGENT", con);
//			if (isDataFromArch) {
//				Calendar cal1 = Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//
//				pstmt = con
//						.prepareStatement("select organization_id,opening_bal_cl_inc from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					if (agtMap.get(rs.getString("organization_id")) != null) {
//						agtMap.get(rs.getString("organization_id"))
//								.setOpeningBal(
//										rs.getDouble("opening_bal_cl_inc"));
//					}
//				}
//				return;
//
//			} else {
//				lastArchDate = ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt = con
//						.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
//				pstmt.setDate(1, lastArchDate);
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					if (agtMap.get(rs.getString("organization_id")) != null) {
//						agtMap.get(rs.getString("organization_id"))
//								.setOpeningBal(rs.getDouble("open_bal"));
//					}
//				}
//			}
			
			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal_cl_inc-net_amount_transaction+cl+xcl)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(checkCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (agtMap.get(rs.getString("organization_id")) != null) {
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					}
				}
				return;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(lastRunCal.getTimeInMillis()));
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (agtMap.get(rs.getString("organization_id")) != null) {
						agtMap.get(rs.getString("organization_id")).setOpeningBal(rs.getDouble("open_bal"));
					}
				}
			}
			

//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(lastArchDate.getTime());
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			ReportUtility.clearTimeFromDate(cal);
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);

			Timestamp fromDate = new Timestamp(cal.getTimeInMillis());

			// Get Account Details
			CallableStatement cstmt = con
					.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, fromDate);
			cstmt.setTimestamp(2, endDate);
			rs = cstmt.executeQuery();

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
				// Game Master Query
				// String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con
						.prepareStatement(ReportUtility
								.getDrawGameMapQuery(fromDate));
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call drawCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("parent_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						agtMap.get(agtOrgId).setDgSale(
								agtMap.get(agtOrgId).getDgSale() + sale);
						agtMap.get(agtOrgId).setDgCancel(
								agtMap.get(agtOrgId).getDgCancel() + cancel);
						agtMap.get(agtOrgId).setDgPwt(
								agtMap.get(agtOrgId).getDgPwt() + pwt);
						agtMap.get(agtOrgId).setDgDirPlyPwt(
								agtMap.get(agtOrgId).getDgDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call scratchCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						agtMap.get(agtOrgId).setSeSale(
								agtMap.get(agtOrgId).getSeSale()
										+ (sale - cancel));
						agtMap.get(agtOrgId).setSePwt(
								agtMap.get(agtOrgId).getSePwt() + pwt);
						agtMap.get(agtOrgId).setSeDirPlyPwt(
								agtMap.get(agtOrgId).getSeDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
				}
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				while (rsProduct.next()) {
					int catId = rsProduct.getInt("category_id");
					cstmt = con
							.prepareCall("call csCollectionAgentWisePerCategory(?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						agtMap.get(rs.getString("parent_id")).setCSSale(
								agtMap.get(rs.getString("parent_id"))
										.getCSSale()
										+ rs.getDouble("sale"));
						agtMap.get(rs.getString("parent_id")).setCSCancel(
								agtMap.get(rs.getString("parent_id"))
										.getCSCancel()
										+ rs.getDouble("cancel"));
					}
				}
			}
			if (ReportUtility.isOLA) {

				StringBuilder olaQuery = new StringBuilder(
						"select WID.agtOrgId, wdraw,wdrawRef,depoAmt,depoRefAmt,netAmt from (select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from (select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WID, (select om.parent_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from (select wrs.retailer_org_id, agent_net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WIDREF,(select om.parent_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEP,(select om.parent_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from (select wrs.retailer_org_id, agent_net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEPREF,(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
								+ fromDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)NETGAME where WID.agtOrgId=WIDREF.agtOrgId and WIDREF.agtOrgId =DEP.agtOrgId and DEP.agtOrgId =DEPREF.agtOrgId and DEPREF.agtOrgId=NETGAME.agtOrgId and NETGAME.agtOrgId=WID.agtOrgId");

				StringBuilder unionQuery = null;
				StringBuilder mainQuery = null;

				if (LMSFilterDispatcher.isRepFrmSP) {
					mainQuery = new StringBuilder(
							"select agtOrgId,sum(wdraw) wdraw,sum(wdrawRef) wdrawRef,sum(depoAmt) depoAmt,sum(depoRefAmt)depoRefAmt,sum(netAmt) netAmt from (");
					unionQuery = new StringBuilder(
							" union all select parent_id agtOrgId , sum(withdrawal_net_amt) wdraw , sum(ref_withdrawal_net_amt) wdrawRef , sum(deposit_net) depoAmt  , sum(ref_deposit_net_amt) depoRefAmt, sum(net_gaming_net_comm) netAmt from st_rep_ola_retailer where finaldate>='"
									+ fromDate
									+ "' and finaldate<='"
									+ endDate
									+ "' group by parent_id) repTable group by agtOrgId");
					mainQuery.append(olaQuery.toString()).append(
							unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				} else {
					pstmt = con.prepareStatement(olaQuery.toString());
				}
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setWithdrawal(
							rs.getDouble("wdraw"));
					agtMap.get(rs.getString("agtOrgId")).setWithdrawalRefund(
							rs.getDouble("wdrawRef"));
					agtMap.get(rs.getString("agtOrgId")).setDeposit(
							rs.getDouble("depoAmt"));
					agtMap.get(rs.getString("agtOrgId")).setDepositRefund(
							rs.getDouble("depoRefAmt"));
					agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
							rs.getDouble("netAmt"));
				}
				/*
				 * 
				 * // Wallet Master Query String walletQry =
				 * "select wallet_id from st_ola_wallet_master";
				 * PreparedStatement walletPstmt =
				 * con.prepareStatement(walletQry); ResultSet rsWallet =
				 * walletPstmt.executeQuery();
				 * 
				 * while (rsWallet.next()) { int walletId =
				 * rsWallet.getInt("wallet_id"); cstmt = con
				 * .prepareCall("call OLACollectionAgentWisePerWallet(?,?,?)");
				 * cstmt.setTimestamp(1, startDate); cstmt.setTimestamp(2,
				 * endDate); cstmt.setInt(3, walletId); rs =
				 * cstmt.executeQuery(); String agtOrgId = null; double sale =
				 * 0, cancel = 0, pwt = 0; while (rs.next()) { agtOrgId =
				 * rs.getString("parent_id"); sale = rs.getDouble("withdraw");
				 * cancel = rs.getDouble("deposit");
				 * agtMap.get(agtOrgId).setDgSale(
				 * agtMap.get(agtOrgId).getDgSale() + sale);
				 * agtMap.get(agtOrgId).setDgCancel(
				 * agtMap.get(agtOrgId).getDgCancel() + cancel);
				 * agtMap.get(agtOrgId).setDgPwt(
				 * agtMap.get(agtOrgId).getDgPwt() + pwt);
				 * agtMap.get(agtOrgId).setDgDirPlyPwt(
				 * agtMap.get(agtOrgId).getDgDirPlyPwt() +
				 * rs.getDouble("pwtDir")); } }
				 */
			}

			if (ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl
						.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					SLEOrgReportResponseBean sleResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setSleSale(
								agtMap.get(orgId).getSleSale()
										+ sleResponseBean.getSaleAmt());
						agtMap.get(orgId).setSleCancel(
								agtMap.get(orgId).getSleCancel()
										+ sleResponseBean.getCancelAmt());
						agtMap.get(orgId).setSlePwt(
								agtMap.get(orgId).getSlePwt()
										+ sleResponseBean.getPwtAmt());
						agtMap.get(orgId).setSleDirPlyPwt(
								agtMap.get(orgId).getSleDirPlyPwt()
										+ sleResponseBean.getPwtDirAmt());
					}
				}
			}

			// set cl and xcl in openning bal
			pstmt = con
					.prepareStatement("select CL.organization_id,sum(amount)amount from st_lms_cl_xcl_update_history CL inner join st_lms_organization_master om on CL.organization_id=om.organization_id where organization_type='AGENT' and date_time>=? and date_time<=? group by CL.organization_id");
			pstmt.setTimestamp(1, fromDate);
			pstmt.setTimestamp(2, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtMap.get(rs.getString("organization_id")).setClLimit(
						rs.getDouble("amount"));
			}

			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr = agtMap
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
						+ bean.getSleSale()
						- bean.getSleCancel()
						- bean.getSlePwt()
						- bean.getSleDirPlyPwt()
						- (bean.getCash() + bean.getCheque() + bean.getCredit()
								+ bean.getBankDep() - bean.getDebit() - bean
								.getChequeReturn());
				openingBal = bean.getClLimit() - openingBal
						+ bean.getOpeningBal();
				bean.setOpeningBal(0.0);
				bean.setCash(0.0);
				bean.setCheque(0.0);
				bean.setChequeReturn(0.0);
				bean.setCredit(0.0);
				bean.setDebit(0.0);
				bean.setBankDep(0.0);
				bean.setClLimit(0.0);
				if (ReportUtility.isDG) {
					bean.setDgSale(0.0);
					bean.setDgPwt(0.0);
					bean.setDgCancel(0.0);
					bean.setDgDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					bean.setSeSale(0.0);
					bean.setSePwt(0.0);
					bean.setSeDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					bean.setCSSale(0.0);
					bean.setCSCancel(0.0);
				}
				if (ReportUtility.isOLA) {
					bean.setDeposit(0.0);
					bean.setDepositRefund(0.0);
					bean.setWithdrawal(0.0);
					bean.setWithdrawalRefund(0.0);
					bean.setNetGamingComm(0.0);
				}
				if (ReportUtility.isSLE) {
					bean.setSleSale(0.0);
					bean.setSlePwt(0.0);
					bean.setSleCancel(0.0);
					bean.setSleDirPlyPwt(0.0);
				}

				bean.setOpeningBal(openingBal);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}

	public double collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con, int agtOrgId) throws LMSException {

		ResultSet rs = null;
		double RetOpenBal = 0.0;
		Date lastArchDate = null;
		if (startDate.after(endDate)) {
			throw new LMSException("Error in report collectionAgentWise");
		}
		PreparedStatement pstmt = null;
		Calendar checkCal = Calendar.getInstance();
		checkCal.setTimeInMillis(endDate.getTime());
		checkCal.add(Calendar.DAY_OF_MONTH, -1);

		try {
			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			Calendar lastRunCal = Calendar.getInstance();
			lastRunCal.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
			if (lastRunCal.getTimeInMillis() >= checkCal.getTimeInMillis()) {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(checkCal.getTimeInMillis()));
				pstmt.setInt(2, agtOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					RetOpenBal = rs.getDouble("open_bal");
				}
				return RetOpenBal;
			} else {
				pstmt = con.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='AGENT'");
				pstmt.setTimestamp(1, new Timestamp(lastRunCal.getTimeInMillis()));
				pstmt.setInt(2, agtOrgId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					RetOpenBal = rs.getDouble("open_bal");
				}
			}
			
//			boolean isDataFromArch = ReportUtility.checkDateLessThanLastRunDate(endDate, -1, "AGENT", con);
//			if (isDataFromArch) {
//				Calendar cal1 = Calendar.getInstance();
//				cal1.setTimeInMillis(endDate.getTime());
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				ReportUtility.clearTimeFromDate(cal1);
//
//				pstmt = con
//						.prepareStatement("select organization_id,opening_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='AGENT'");
//				pstmt.setDate(1, new Date(cal1.getTimeInMillis()));
//				pstmt.setInt(2, agtOrgId);
//				rs = pstmt.executeQuery();
//				if (rs.next()) {
//					RetOpenBal = rs.getDouble("opening_bal");
//
//				}
//				return RetOpenBal;
//
//			} else {
//				lastArchDate = ReportUtility.getLastArchDateInDateFormat(con);
//				pstmt = con
//						.prepareStatement("select organization_id,(opening_bal+net_amount_transaction)open_bal from st_rep_org_bal_history where finaldate=? and organization_id =? and organization_type='AGENT'");
//				pstmt.setDate(1, lastArchDate);
//				pstmt.setInt(2, agtOrgId);
//				rs = pstmt.executeQuery();
//				if (rs.next()) {
//					RetOpenBal = rs.getDouble("open_bal");
//
//				}
//			}

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(lastRunCal.getTimeInMillis());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			ReportUtility.clearTimeFromDate(cal);

			Timestamp fromDate = new Timestamp(cal.getTimeInMillis());

			// Get Account Details
			CallableStatement cstmt = con
					.prepareCall("call paymentCashChqOverAll(?,?,?)");
			cstmt.setTimestamp(1, fromDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();
			double recTotal = 0.0;
			double scratchTotal = 0.0;
			double drawTotal = 0.0;
			double csTotal = 0.0;
			double olaTotal = 0.0;
			double sleTotal = 0.0;
			double iwTotal = 0.0;
			double vsTotal = 0.0;
			while (rs.next()) {
				double cash = rs.getDouble("cash");
				double cheque = rs.getDouble("chq");
				double credit = rs.getDouble("credit");
				double debit = rs.getDouble("debit");
				double chqRet = rs.getDouble("chq_ret");
				double bank = rs.getDouble("bank");
				recTotal = recTotal + cash + cheque + credit + bank - debit
						- chqRet;

				/*
				 * agtMap.get(agtOrgId+"").setCash(rs.getDouble("cash"));
				 * agtMap.get(agtOrgId+"").setCheque(rs.getDouble("chq"));
				 * agtMap
				 * .get(agtOrgId+"").setChequeReturn(rs.getDouble("chq_ret"));
				 * agtMap.get(agtOrgId+"").setCredit(rs.getDouble("credit"));
				 * agtMap.get(agtOrgId+"").setDebit(rs.getDouble("debit"));
				 * agtMap.get(agtOrgId+"").setBankDep(rs.getDouble("bank"));
				 */
			}

			if (ReportUtility.isDG) {
				// Game Master Query
				// String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con
						.prepareStatement(ReportUtility
								.getDrawGameMapQuery(fromDate));
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call drawPaymentAgentWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					// String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					double dirPlrPwt = 0.0;
					while (rs.next()) {
						// agtOrgId = rs.getString("parent_id");

						sale = sale + rs.getDouble("sale");
						cancel = cancel + rs.getDouble("cancel");
						pwt = pwt + rs.getDouble("pwt");
						dirPlrPwt = dirPlrPwt + rs.getDouble("pwtDir");
						drawTotal = drawTotal + sale - cancel - pwt - dirPlrPwt;
						/*
						 * agtMap.get(agtOrgId+"").setDgSale(
						 * agtMap.get(agtOrgId+"").getDgSale() + sale);
						 * agtMap.get(agtOrgId+"").setDgCancel(
						 * agtMap.get(agtOrgId+"").getDgCancel() + cancel);
						 * agtMap.get(agtOrgId+"").setDgPwt(
						 * agtMap.get(agtOrgId+"").getDgPwt() + pwt);
						 */
						/*
						 * agtMap.get(agtOrgId+"").setDgDirPlyPwt(
						 * agtMap.get(agtOrgId+"").getDgDirPlyPwt() +
						 * rs.getDouble("pwtDir"));
						 */
					}
				}

			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call scratchPaymentAgentWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					// String agtOrgId = null;
					double sale = 0, cancel = 0, pwt = 0;
					double dirPlrPwt = 0.0;
					while (rs.next()) {
						// agtOrgId = rs.getString("organization_id");
						sale = sale + rs.getDouble("sale");
						cancel = cancel + rs.getDouble("cancel");
						pwt = pwt + rs.getDouble("pwt");
						dirPlrPwt = dirPlrPwt + rs.getDouble("pwtDir");
						scratchTotal = scratchTotal + sale - cancel - pwt
								- dirPlrPwt;
						/*
						 * agtMap.get(agtOrgId+"").setSeSale(
						 * agtMap.get(agtOrgId+"").getSeSale() + (sale -
						 * cancel)); agtMap.get(agtOrgId+"").setSePwt(
						 * agtMap.get(agtOrgId+"").getSePwt() + pwt);
						 * agtMap.get(agtOrgId+"").setSeDirPlyPwt(
						 * agtMap.get(agtOrgId+"").getSeDirPlyPwt() +
						 * rs.getDouble("pwtDir"));
						 */
					}
				}
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				while (rsProduct.next()) {
					int catId = rsProduct.getInt("category_id");
					cstmt = con
							.prepareCall("call csPaymentAgentWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, fromDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, agtOrgId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					double sale = 0.0;
					double cancel = 0.0;
					while (rs.next()) {
						sale = sale + rs.getDouble("sale");
						cancel = cancel + rs.getDouble("cancel");
						csTotal = csTotal + sale - cancel;
						/*
						 * agtMap.get(agtOrgId+"").setCSSale(agtMap.get(agtOrgId+
						 * "").getCSSale()+ rs.getDouble("sale"));
						 * agtMap.get(agtOrgId
						 * +"").setCSCancel(agtMap.get(agtOrgId
						 * +"").getCSCancel()+ rs.getDouble("cancel"));
						 */
					}
				}
			}

			if (ReportUtility.isOLA) {
				double olaDeposit = 0.0;
				double olaDepositRefund = 0.0;
				double olaWithdrawal = 0.0;
				double olaWithdrawalRefund = 0.0;
				double netGaming = 0.0;
				/*
				 * StringBuilder wdQry = new StringBuilder(
				 * "select parent_id agtOrgId,wdraw from "); StringBuilder
				 * wdRefQry = new StringBuilder(
				 * "select parent_id agtOrgId,wdrawRef from "); StringBuilder
				 * depQry = new StringBuilder(
				 * "select parent_id agtOrgId,depoAmt from "); StringBuilder
				 * depRefQry = new StringBuilder(
				 * "select parent_id agtOrgId,depoRefAmt from");
				 * 
				 * wdQry.append(
				 * "(select parent_id,ifnull(sum(withd),0.0) wdraw from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as withd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "'))withdRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
				 * + agtOrgId + " group by parent_id) withRef");
				 * 
				 * wdRefQry.append(
				 * "(select parent_id,ifnull(sum(withd),0.0) wdrawRef from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as withd from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "'))withdRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
				 * + agtOrgId + " group by parent_id) withRef"); depQry.append(
				 * "(select parent_id,ifnull(sum(depo),0.0) depoAmt from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "'))deposit on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
				 * + agtOrgId + " group by parent_id) depo");
				 * 
				 * depRefQry.append(
				 * "(select parent_id,ifnull(sum(depo),0.0) depoRefAmt from st_lms_organization_master om left outer join (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate +
				 * "'))depositRef on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
				 * + agtOrgId + " group by parent_id) depoRef");
				 * 
				 * String netGamingQry =
				 * "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
				 * + fromDate + "' and transaction_date<='" + endDate+
				 * "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id where om.parent_id="
				 * +agtOrgId+" group by om.parent_id"; StringBuilder
				 * unionQuery=null; StringBuilder mainQuery=null;
				 * 
				 * // Withdrawal Query if(LMSFilterDispatcher.isRepFrmSP){
				 * mainQuery=new
				 * StringBuilder("select agtOrgId,sum(wdraw) wdraw from (");
				 * unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId,ifnull(sum(withdrawal_net_amt),0) as wdraw from st_rep_ola_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId
				 * +" ) repTable");
				 * mainQuery.append(wdQry.toString()).append(unionQuery
				 * .toString()); pstmt =
				 * con.prepareStatement(mainQuery.toString()); } else { pstmt =
				 * con.prepareStatement(wdQry.toString()); }
				 * logger.debug("-------Withdrawal Query------\n" + pstmt); rs =
				 * pstmt.executeQuery(); while (rs.next()) {
				 * olaWithdrawal=olaWithdrawal+rs.getDouble("wdraw");
				 * 
				 * agentBean=agtMap.get(rs.getString("agtOrgId"));
				 * if(agentBean!=null){ agtMap.get(rs.getString("agtOrgId" +
				 * "")).setWithdrawal( rs.getDouble("wdraw")); } }
				 */

				/*
				 * // WithDrawal Refund Query
				 * if(LMSFilterDispatcher.isRepFrmSP){ mainQuery=new
				 * StringBuilder
				 * ("select agtOrgId,sum(wdrawRef) wdrawRef from (");
				 * unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId,ifnull(sum(ref_withdrawal_net_amt),0) as wdrawRef from st_rep_ola_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId
				 * +" ) repTable");
				 * mainQuery.append(wdRefQry.toString()).append(
				 * unionQuery.toString()); pstmt =
				 * con.prepareStatement(mainQuery.toString()); } else { pstmt =
				 * con.prepareStatement(wdRefQry.toString()); }
				 * 
				 * logger.debug("-------WithDrawal Refund Query------\n" +
				 * pstmt); rs = pstmt.executeQuery(); while (rs.next()) {
				 * olaWithdrawalRefund
				 * =olaWithdrawalRefund+rs.getDouble("wdrawRef");
				 * agentBean=agtMap.get(rs.getString("agtOrgId"));
				 * if(agentBean!=null){ agtMap.get(rs.getString("agtOrgId" +
				 * "")) .setWithdrawalRefund(rs.getDouble("wdrawRef")); } }
				 */

				/*
				 * // Deposit Query if(LMSFilterDispatcher.isRepFrmSP){
				 * mainQuery=new
				 * StringBuilder("select agtOrgId,sum(depoAmt) depoAmt from (");
				 * unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId,ifnull(sum(deposit_net),0) as depoAmt from st_rep_ola_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId
				 * +" ) repTable");
				 * mainQuery.append(depQry.toString()).append(unionQuery
				 * .toString()); pstmt =
				 * con.prepareStatement(mainQuery.toString()); } else { pstmt =
				 * con.prepareStatement(depQry.toString()); }
				 * logger.debug("-------Deposit Query------\n" + pstmt);
				 * 
				 * rs = pstmt.executeQuery(); while (rs.next()) {
				 * olaDeposit=olaDeposit+rs.getDouble("depoAmt");
				 * agentBean=agtMap.get(rs.getString("agtOrgId"));
				 * if(agentBean!=null){ agtMap.get(rs.getString("agtOrgId" +
				 * "")).setDeposit( rs.getDouble("depoAmt")); } }
				 */

				/*
				 * // Deposit Refund Query if(LMSFilterDispatcher.isRepFrmSP){
				 * mainQuery=new
				 * StringBuilder("select agtOrgId,sum(depoRefAmt) depoRefAmt from ("
				 * ); unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId,ifnull(sum(ref_deposit_net_amt),0) as depoRefAmt from st_rep_ola_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId
				 * +" ) repTable");
				 * mainQuery.append(depRefQry.toString()).append
				 * (unionQuery.toString()); pstmt =
				 * con.prepareStatement(mainQuery.toString()); } else { pstmt =
				 * con.prepareStatement(depRefQry.toString()); }
				 * logger.debug("-------Deposit Refund Query------\n" + pstmt);
				 */

				/*
				 * rs = pstmt.executeQuery(); while (rs.next()) {
				 * olaDepositRefund=olaDepositRefund+rs.getDouble("depoRefAmt");
				 * 
				 * agentBean=agtMap.get(rs.getString("agtOrgId"));
				 * if(agentBean!=null){ agtMap.get(rs.getString("agtOrgId" +
				 * "")).setDepositRefund( rs.getDouble("depoRefAmt")); } }
				 */

				/*
				 * // net gaming commission query
				 * if(LMSFilterDispatcher.isRepFrmSP){ mainQuery=new
				 * StringBuilder("select agtOrgId,sum(netAmt) netAmt from (");
				 * unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId,ifnull(sum(net_gaming_net_comm),0) as netAmt from st_rep_ola_retailer where finaldate>='"
				 * +
				 * fromDate+"' and finaldate<='"+endDate+"' and parent_id="+agtOrgId
				 * +" ) repTable");
				 * mainQuery.append(netGamingQry).append(unionQuery.toString());
				 * pstmt = con.prepareStatement(mainQuery.toString()); } else {
				 * pstmt = con.prepareStatement(netGamingQry.toString()); }
				 */

				/*
				 * logger.debug("-------Net Gaming Query------\n" + pstmt); rs =
				 * pstmt.executeQuery(); while (rs.next()) {
				 * netGaming=netGaming+rs.getDouble("netAmt");
				 * 
				 * agentBean=agtMap.get(rs.getString("agtOrgId"));
				 * if(agentBean!=null){ agtMap.get(rs.getString("agtOrgId" +
				 * "")).setNetGamingComm( rs.getDouble("netAmt")); } }
				 */
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(fromDate.toString());
				requestBean.setToDate(endDate.toString());
				requestBean.setOrgId(agtOrgId);
				OlaOrgReportResponseBean responseBean = OlaAgentReportControllerImpl
						.fetchDepositWithdrawlSinglaAgent(requestBean, con);
				String netGamingQry = "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"+ startDate + "' and transaction_date<='"	+ endDate+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id where om.parent_id="+agtOrgId+" group by om.parent_id";
				
				
				// net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					netGaming=netGaming+rs.getDouble("netAmt");
				}
				
				
				
				olaTotal = responseBean.getNetDepositAmt()
						- responseBean.getNetWithdrawalAmt() - netGaming;
			}

			if (ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				SLEOrgReportResponseBean responseBean = SLEAgentReportsControllerImpl.fetchSaleCancelPWTSingleAgentAllGame(requestBean, con);

				sleTotal = responseBean.getSaleAmt() - responseBean.getCancelAmt() - responseBean.getPwtAmt() - responseBean.getPwtDirAmt();
			}
			
			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				IWOrgReportResponseBean responseBean = IWAgentReportsControllerImpl.fetchSaleCancelPWTSingleAgentAllGame(requestBean, con);

				iwTotal = responseBean.getSaleAmt() - responseBean.getCancelAmt() - responseBean.getPwtAmt() - responseBean.getPwtDirAmt();
			}
			
			if (ReportUtility.isVS) {
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(fromDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				VSOrgReportResponseBean responseBean = VSAgentReportsControllerImpl.fetchSaleCancelPWTSingleAgentAllGame(requestBean, con);

				vsTotal = responseBean.getSaleAmt() - responseBean.getCancelAmt() - responseBean.getPwtAmt() - responseBean.getPwtDirAmt();
			}

			RetOpenBal = RetOpenBal + drawTotal + scratchTotal + csTotal
					+ olaTotal + sleTotal + iwTotal - recTotal + vsTotal;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
		return RetOpenBal;
	}
}