package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
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

public class CollectionReportOverAllHelperSP implements
		ICollectionReportOverAllHelper {
	Log logger = LogFactory.getLog(CollectionReportOverAllHelperSP.class);

	public void collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con, Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		if (startDate.after(endDate)) {
			return;
		}

		try {
			// Get Account Details
			cstmt = con
					.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, startDate);
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
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call drawCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, startDate);
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
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("call scratchCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, startDate);
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
					cstmt.setTimestamp(1, startDate);
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
				requestBean.setFromDate(startDate.toString());
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
					if(agtMap.containsKey(rs.getString("agtOrgId"))){
						agtMap.get(rs.getString("agtOrgId")).setNetGamingComm(
								rs.getDouble("netAmt"));
					}
					
				}	
				/*
				 * StringBuilder olaQuery = new StringBuilder(
				 * "select WID.agtOrgId, wdraw,wdrawRef,depoAmt,depoRefAmt,netAmt from (select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from (select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WID, (select om.parent_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from (select wrs.retailer_org_id, agent_net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WIDREF,(select om.parent_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEP,(select om.parent_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from (select wrs.retailer_org_id, agent_net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEPREF,(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
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
				 * +startDate+"' and finaldate<='"+endDate+
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
				requestBean.setFromDate(startDate);
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
				requestBean.setFromDate(startDate);
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
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);		
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);		
				ResultSet rsGame = gamePstmt.executeQuery();		
				while(rsGame.next()){
					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(rsGame.getInt("game_id"));
					Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap =VSAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);
	
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closeCstmt(cstmt);
		}
	}

	public void collectionAgentWiseExpand(Timestamp startDate,
			Timestamp endDate, Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		StringBuilder CSQry = null;
		CollectionReportOverAllBean agentBean = null;
		CompleteCollectionBean gameBean, prodBean = null;
		try {
			if (ReportUtility.isDG) {
				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
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

						if (agentBean != null) {
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
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
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
						if (agentBean != null) {
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
							.prepareCall("call csCollectionAgentWisePerGame(?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					logger.debug("-------Indivisual Category Qry-------\n"
							+ cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("parent_id");
						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
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
							gameBean.setCSSale(rs.getDouble("sale"));
							gameBean.setCSCancel(rs.getDouble("cancel"));
						}
					}
				}

				/*
				 * CSQry = new StringBuilder(
				 * "select organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select sale.parent_id,sale,cancel from "
				 * ); saleTranCS =
				 * "(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,"
				 * ; cancelTranCS =
				 * "(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate +
				 * "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel"
				 * ; CSQry.append(saleTranCS); CSQry.append(cancelTranCS); CSQry
				 * .append(
				 * " where sale.parent_id=cancel.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id"
				 * ); logger.debug("For Expand CS game Qry:: " + CSQry);
				 * 
				 * // Category Master Query String prodQry =
				 * "select category_id, category_code from st_cs_product_category_master where status = 'ACTIVE'"
				 * ; PreparedStatement prodPstmt =
				 * con.prepareStatement(prodQry); ResultSet rsProd =
				 * prodPstmt.executeQuery(); while (rsProd.next()) { int catId =
				 * rsProd.getInt("category_id"); String catName =
				 * rsProd.getString("category_code"); pstmt =
				 * con.prepareStatement(CSQry.toString()); pstmt.setInt(1,
				 * catId); pstmt.setInt(2, catId);
				 * logger.debug("-------Indivisual Category Qry-------\n" +
				 * pstmt); rs = pstmt.executeQuery(); while (rs.next()) { String
				 * agtOrgId = rs.getString("organization_id"); agentBean =
				 * agtMap.get(agtOrgId); Map<String, CompleteCollectionBean>
				 * prodMap = agentBean .getGameBeanMap(); if (prodMap == null) {
				 * prodMap = new HashMap<String, CompleteCollectionBean>();
				 * agentBean.setGameBeanMap(prodMap); } prodBean =
				 * prodMap.get(catName); if (prodBean == null) { prodBean = new
				 * CompleteCollectionBean(); prodMap.put(catName, prodBean); }
				 * prodBean.setOrgName(catName);
				 * prodBean.setDrawSale(rs.getDouble("sale"));
				 * prodBean.setDrawPwt(rs.getDouble("cancel")); gameBean.se
				 * System.out.println("map size:" + prodMap.size()); }
				 * 
				 * }
				 */

			}

			/*
			 * String wdTranOLA = null; String wdRefTranOLA = null; String
			 * depoTranOLA = null; String depoRefTranOLA= null; String
			 * netGTranOLA = null; StringBuilder OLAQry = null;
			 */
			if (ReportUtility.isOLA) {
				/*
				 * OLAQry = new StringBuilder(
				 * "select withdraw.agtOrgId, (wdra-wdraRef) withdrawAmt, (depo-depoRef) depositAmt, netAmt netGamingComm from "
				 * ); wdTranOLA =
				 * "(select om.parent_id agtOrgId, ifnull(sum(wdra),0.0)wdra from (select ifnull(orw.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdra from st_ola_ret_withdrawl orw where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL' and transaction_date>='"
				 * + startDate + "' and transaction_date <='" + endDate+
				 * "') and orw.wallet_id = ? group by retId)wd right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wd.retId = om.organization_id group by parent_id)wd,"
				 * ; wdRefTranOLA =
				 * "(select om.parent_id agtOrgId, ifnull(sum(wdraRef),0.0)wdraRef from (select ifnull(orwRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdraRef from st_ola_ret_withdrawl_refund orwRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL_REFUND' and transaction_date>= '"
				 * + startDate + "' and transaction_date <= '" + endDate +
				 * "') and orwRef.wallet_id = ? group by retId)wdRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wdRef.retId = om.organization_id group by parent_id)wdRef"
				 * ; depoTranOLA =
				 * "(select om.parent_id agtOrgId, ifnull(sum(depo),0.0)depo from (select ifnull(ordepo.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depo from st_ola_ret_deposit ordepo where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT' and transaction_date>='"
				 * + startDate + "' and transaction_date <='" + endDate+
				 * "') and ordepo.wallet_id = ? group by retId)depo right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depo.retId = om.organization_id group by parent_id)depo,"
				 * ; depoRefTranOLA =
				 * "(select om.parent_id agtOrgId, ifnull(sum(depoRef),0.0)depoRef from (select ifnull(ordepoRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depoRef from st_ola_ret_deposit_refund ordepoRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT_REFUND' and transaction_date>= '"
				 * + startDate + "' and transaction_date <= '" + endDate +
				 * "') and ordepoRef.wallet_id = ? group by retId)depoRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depoRef.retId = om.organization_id group by parent_id)depoRef"
				 * ; netGTranOLA=
				 * "(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
				 * + startDate + "' and transaction_date<='" + endDate+
				 * "' and wallet_id = ?) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id) netgaming"
				 * ; OLAQry.append("(select wd.agtOrgId, wdra, wdraRef from");
				 * OLAQry.append(wdTranOLA); OLAQry.append(wdRefTranOLA);
				 * OLAQry.
				 * append(" where wd.agtOrgId = wdRef.agtOrgId)withdraw,");
				 * OLAQry.append("(select depo.agtOrgId, depo, depoRef from");
				 * OLAQry.append(depoTranOLA); OLAQry.append(depoRefTranOLA);
				 * OLAQry
				 * .append(" where depo.agtOrgId = depoRef.agtOrgId)deposit, ");
				 * OLAQry.append(netGTranOLA); OLAQry.append(
				 * " where withdraw.agtOrgId = deposit.agtOrgId and netgaming.agtOrgId = withdraw.agtOrgId and netgaming.agtOrgId = deposit.agtOrgId"
				 * );
				 * 
				 * 
				 * StringBuilder unionQuery=null; StringBuilder mainQuery=null;
				 * 
				 * if(LMSFilterDispatcher.isRepFrmSP){ mainQuery=new
				 * StringBuilder(
				 * "select agtOrgId, sum(withdrawAmt) withdrawAmt,sum(depositAmt) depositAmt ,sum(netGamingComm) netGamingComm from ("
				 * ); unionQuery=newStringBuilder(
				 * " union all select parent_id agtOrgId, sum(withdrawal_net_amt)-sum(ref_withdrawal_net_amt) withdrawAmt ,sum(deposit_net) -sum(ref_deposit_net_amt) depositAmt,sum(net_gaming_net_comm) netGamingComm from st_rep_ola_retailer where finaldate>='"
				 * +startDate+"' and finaldate<='"+endDate+
				 * "'  and wallet_id=? group by  parent_id) repTable group by agtOrgId"
				 * );
				 * mainQuery.append(OLAQry.toString()).append(unionQuery.toString
				 * ()); pstmt = con.prepareStatement(mainQuery.toString()); }
				 * else { pstmt = con.prepareStatement(OLAQry.toString()); }
				 * logger.debug("For Expand OLA wallet Qry:: " + OLAQry);
				 */
				
				
				String netGamingQry = "select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt,ifnull(wallet_id,0)wallet_id from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt,wallet_id from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id,wallet_id";
				
				//net gaming commission query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				Map<String,Map<Integer, Double>> agtNetGamingMap=new HashMap<String, Map<Integer,Double>>();
				while(rs.next()){
					if(agtNetGamingMap.containsKey(rs.getString("agtOrgId"))){
						if(rs.getInt("wallet_id")!=0){
							agtNetGamingMap.get(rs.getString("agtOrgId")).put(rs.getInt("wallet_id"),rs.getDouble("netAmt"));
						}
							
					}else{
						Map<Integer, Double> walletNetGamingMap=new HashMap<Integer, Double>();
						if(rs.getInt("wallet_id")!=0)
							walletNetGamingMap.put(rs.getInt("wallet_id"),rs.getDouble("netAmt"));
						agtNetGamingMap.put(rs.getString("agtOrgId"),walletNetGamingMap);
					}
					
				}
				// Wallet Master Query
				String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master where wallet_status='ACTIVE'";
				PreparedStatement walletPstmt = con.prepareStatement(walletQry);
				ResultSet rsWallet = walletPstmt.executeQuery();
				while (rsWallet.next()) {
					int walletId = rsWallet.getInt("wallet_id");
					String walletName = rsWallet.getString("wallet_name");

					OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
					requestBean.setFromDate(startDate.toString());
					requestBean.setToDate(endDate.toString());
					requestBean.setWalletId(walletId);
					Map<Integer, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl
							.fetchDepositWithdrawlMultipleAgent(requestBean,
									con);

					for (Entry<String, Map<Integer, Double>> entry : agtNetGamingMap.entrySet()) {
						String agtOrgId = String.valueOf(entry.getKey());

						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
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

							OlaOrgReportResponseBean olaResponseBean =olaResponseBeanMap.get(Integer.parseInt(agtOrgId));

							gameBean.setOrgName(walletName);
							
							gameBean.setOlaWithdrawalAmt(olaResponseBean!=null?olaResponseBean.getNetWithdrawalAmt():0);
							gameBean.setOlaDepositAmt(olaResponseBean!=null?olaResponseBean.getNetDepositAmt():0);
							gameBean.setOlaNetGaming(agtNetGamingMap.get(agtOrgId).containsKey(walletId)?agtNetGamingMap.get(agtOrgId).get(walletId):0);
							//gameBean.setOlaNetGaming(0);

						}
					}

				}

			}

			if (ReportUtility.isSLE) {
				// Wallet Master Query
				String sleGameQry = ReportUtility.getSLEGameMapQuery(startDate);
				PreparedStatement sleGamePstmt = con
						.prepareStatement(sleGameQry);
				ResultSet rsSLEGame = sleGamePstmt.executeQuery();
				while (rsSLEGame.next()) {
					int gameId = rsSLEGame.getInt("game_id");
					String gameName = rsSLEGame.getString("game_disp_name");

					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl
							.fetchSaleCancelPWTMultipleAgent(requestBean, con);

					for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap
							.entrySet()) {
						String agtOrgId = String.valueOf(entry.getKey());

						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							Map<String, CompleteCollectionBean> sleGameMap = agentBean
									.getGameBeanMap();
							if (sleGameMap == null) {
								sleGameMap = new HashMap<String, CompleteCollectionBean>();
								agentBean.setGameBeanMap(sleGameMap);
							}
							gameBean = sleGameMap.get(gameName);
							if (gameBean == null) {
								gameBean = new CompleteCollectionBean();
								sleGameMap.put(gameName, gameBean);
							}

							SLEOrgReportResponseBean sleResponseBean = entry
									.getValue();

							gameBean.setOrgName(gameName);
							gameBean.setSleSale(sleResponseBean.getSaleAmt());
							gameBean.setSleCancel(sleResponseBean
									.getCancelAmt());
							gameBean.setSlePwt(sleResponseBean.getPwtAmt());
							gameBean.setSleDirPlyPwt(sleResponseBean
									.getPwtDirAmt());

						}
					}

				}

			}

			if (ReportUtility.isIW) {
				String iwGameQry = ReportUtility.getIWGameMapQuery(startDate);
				PreparedStatement iwGamePstmt = con.prepareStatement(iwGameQry);
				ResultSet rsIWGame = iwGamePstmt.executeQuery();
				while (rsIWGame.next()) {
					int gameId = rsIWGame.getInt("game_id");
					String gameName = rsIWGame.getString("game_name");

					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);

					for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap.entrySet()) {
						String agtOrgId = String.valueOf(entry.getKey());

						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							Map<String, CompleteCollectionBean> iwGameMap = agentBean.getGameBeanMap();
							if (iwGameMap == null) {
								iwGameMap = new HashMap<String, CompleteCollectionBean>();
								agentBean.setGameBeanMap(iwGameMap);
							}
							gameBean = iwGameMap.get(gameName);
							if (gameBean == null) {
								gameBean = new CompleteCollectionBean();
								iwGameMap.put(gameName, gameBean);
							}

							IWOrgReportResponseBean iwResponseBean = entry.getValue();

							gameBean.setOrgName(gameName);
							gameBean.setIwSale(iwResponseBean.getSaleAmt());
							gameBean.setIwCancel(iwResponseBean .getCancelAmt());
							gameBean.setIwPwt(iwResponseBean.getPwtAmt());
							gameBean.setIwDirPlyPwt(iwResponseBean .getPwtDirAmt());
						}
					}
				}
			}
			if (ReportUtility.isVS) {
				String vsGameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement vsGamePstmt = con.prepareStatement(vsGameQry);
				ResultSet rsVSGame = vsGamePstmt.executeQuery();
				while (rsVSGame.next()) {
					int gameId = rsVSGame.getInt("game_id");
					String gameName = rsVSGame.getString("game_name");

					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl.fetchSaleCancelPWTMultipleAgent(requestBean, con);

					for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
						String agtOrgId = String.valueOf(entry.getKey());

						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							Map<String, CompleteCollectionBean> vsGameMap = agentBean.getGameBeanMap();
							if (vsGameMap == null) {
								vsGameMap = new HashMap<String, CompleteCollectionBean>();
								agentBean.setGameBeanMap(vsGameMap);
							}
							gameBean = vsGameMap.get(gameName);
							if (gameBean == null) {
								gameBean = new CompleteCollectionBean();
								vsGameMap.put(gameName, gameBean);
							}

							VSOrgReportResponseBean vsResponseBean = entry.getValue();

							gameBean.setOrgName(gameName);
							gameBean.setVsSale(vsResponseBean.getSaleAmt());
							gameBean.setVsCancel(vsResponseBean .getCancelAmt());
							gameBean.setVsPwt(vsResponseBean.getPwtAmt());
							gameBean.setVsDirPlyPwt(vsResponseBean .getPwtDirAmt());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise Expand");
		} finally {
			if (con != null) {
				DBConnect.closeCon(con);
			}
		}
	}

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode)
			throws LMSException {
		return commanMethodOfCollectionAgentWiseWithOpeningBal(deployDate, startDate, endDate, cityCode, stateCode,0);
	}
	
	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode,int roleId)
			throws LMSException {
		return commanMethodOfCollectionAgentWiseWithOpeningBal(deployDate, startDate, endDate, cityCode, stateCode,
				roleId);
	}
	

	private Map<String, CollectionReportOverAllBean> commanMethodOfCollectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode, int roleId)
			throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		Connection con = null;
		String orgCodeQry = " om.name orgCode ";
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CollectionReportOverAllBean> agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;

		try {
			con = DBConnect.getConnection();
			//String agtOrgQry = QueryManager.getOrgQry("AGENT");
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',om.name)  orgCode  ";
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(om.name,'_',om.org_code)  orgCode  ";
			}
			String agtOrgQry1= "select "+orgCodeQry+",organization_id from st_lms_organization_master om inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name where organization_type='AGENT'" ;
			
			if(roleId!=0){
				agtOrgQry1 = CommonMethods.appendRoleAgentMappingQuery(agtOrgQry1,"om.organization_id",roleId); // get perticular agent corresponding roleId
			}
			
			if(!(stateCode.equalsIgnoreCase("ALL"))) {
				agtOrgQry1 = agtOrgQry1.concat(" and om.state_code = '" + stateCode + "' ");
			}
			
			if(!(cityCode.equalsIgnoreCase("ALL"))) {
				agtOrgQry1 = agtOrgQry1.concat(" and cm.city_name = '" + cityCode + "' ");
			}
			
			
			agtOrgQry1 = agtOrgQry1.concat(" order by "+QueryManager.getAppendOrgOrder());

			logger.debug("Organization Query:" + agtOrgQry1);

			/*
			 * String agtOrgQry =
			 * "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name"
			 * ;
			 */
			pstmt = con.prepareStatement(agtOrgQry1);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
				collBean.setAgentName(rsRetOrg.getString("orgCode"));

				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			// for calculation of opening Balance
			AgentOpeningBalanceHelper agentOpenHelper = new AgentOpeningBalanceHelper();
			agentOpenHelper.collectionAgentWiseOpenningBal(deployDate,startDate, con, agtMap);

			collectionAgentWise(startDate, endDate, con, agtMap);
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
