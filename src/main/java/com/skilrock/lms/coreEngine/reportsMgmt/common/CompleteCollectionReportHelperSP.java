package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.controller.IWAgentReportsControllerImpl;
import com.skilrock.lms.web.instantWin.reportsMgmt.controller.IWRetailerReportsControllerImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLEAgentReportsControllerImpl;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLERetailerReportsControllerImpl;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.controller.VSAgentReportsControllerImpl;
import com.skilrock.lms.web.virtualSports.reportsMgmt.controller.VSRetailerReportsControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaAgentReportControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaRetailerReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class CompleteCollectionReportHelperSP implements
		ICompleteCollectionReportHelper {
	static Log logger = LogFactory
			.getLog(CompleteCollectionReportHelperSP.class);
	StringBuilder dates = null;

	public Map<String, Double> agentDirectPlayerPwt(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean) {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		Map<String, Double> dirPlrPwtMap = new LinkedHashMap<String, Double>();
		// Draw Direct Player Qry
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			cstmt = con
					.prepareCall("{call fetchDrawDirectPlyPwtofAgent(?,?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwtMap.put("DG", rs.getDouble("pwtDir"));
			} else {
				dirPlrPwtMap.put("DG", 0.0);
			}
			// Scratch Direct Player Qry

			cstmt = con
					.prepareCall("{call fetchScratchDirectPlyPwtofAgent(?,?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwtMap.put("SE", rs.getDouble("pwtDir"));
			} else {
				dirPlrPwtMap.put("SE", 0.0);
			}

			// SLE Direct Player data
			SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
			requestBean.setFromDate(startDate);
			requestBean.setToDate(endDate);
			requestBean.setOrgId(agtOrgId);
			SLEOrgReportResponseBean responseBean = SLEAgentReportsControllerImpl
					.getDirPWTofAgent(requestBean, con);
			dirPlrPwtMap.put("SLE", responseBean.getPwtDirAmt());
			
			
			// IW Direct Player data
			
			IWOrgReportRequestBean requestIWBean = new IWOrgReportRequestBean();
			requestIWBean.setFromDate(startDate);
			requestIWBean.setToDate(endDate);
			requestIWBean.setOrgId(agtOrgId);
			IWOrgReportResponseBean responseIWBean = IWAgentReportsControllerImpl
					.getDirPWTofAgent(requestIWBean, con);
			dirPlrPwtMap.put("IW", responseIWBean.getPwtDirAmt());
			
			VSOrgReportRequestBean requestVSBean = new VSOrgReportRequestBean();
			requestVSBean.setFromDate(startDate);
			requestVSBean.setToDate(endDate);
			requestVSBean.setOrgId(agtOrgId);
			VSOrgReportResponseBean responseVSBean = VSAgentReportsControllerImpl
					.getDirPWTofAgent(requestVSBean, con);
			dirPlrPwtMap.put("VS", responseVSBean.getPwtDirAmt());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closeCstmt(cstmt);
		}
		return dirPlrPwtMap;
	}

	public Map<String, CompleteCollectionBean> collectionAgentWise(
			Timestamp startDate, Timestamp endDate, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		// for Draw Game

		if (startDate.after(endDate)) {
			return agtMap;
		}

		// for scratch game
		try {

			// Get All Agent

			String agtOrgQry = QueryManager.getOrgQry("AGENT");
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("orgCode"));
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if (ReportUtility.isSLE) {
					collBean.setSleSale(0.0);
					collBean.setSlePwt(0.0);
					collBean.setSleCancel(0.0);
					collBean.setSleDirPlyPwt(0.0);
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
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			if (ReportUtility.isDG) {
				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con.prepareCall("{call drawCollectionAgentWisePerGame(?,?,?)}");
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
						agtMap.get(agtOrgId).setDrawSale(
								agtMap.get(agtOrgId).getDrawSale() + sale);
						agtMap.get(agtOrgId).setDrawCancel(
								agtMap.get(agtOrgId).getDrawCancel() + cancel);
						agtMap.get(agtOrgId).setDrawPwt(
								agtMap.get(agtOrgId).getDrawPwt() + pwt);
						agtMap.get(agtOrgId).setDrawDirPlyPwt(
								agtMap.get(agtOrgId).getDrawDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("{call scratchCollectionAgentWisePerGame(?,?,?)}");
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
						agtMap.get(agtOrgId).setScratchSale(
								agtMap.get(agtOrgId).getScratchSale()
										+ (sale - cancel));
						agtMap.get(agtOrgId).setScratchPwt(
								agtMap.get(agtOrgId).getScratchPwt() + pwt);
						agtMap.get(agtOrgId).setScratchDirPlyPwt(
								agtMap.get(agtOrgId).getScratchDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);
			}
			if (ReportUtility.isCS) {

				// category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					cstmt = con.prepareCall("{call csCollectionAgentWisePerCategory(?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					rs = cstmt.executeQuery();
					String agtOrgId = null;
					double sale = 0, cancel = 0;
					while (rs.next()) {
						agtOrgId = rs.getString("parent_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						agtMap.get(agtOrgId).setCSSale(
								agtMap.get(agtOrgId).getCSSale() + sale);
						agtMap.get(agtOrgId).setCSCancel(
								agtMap.get(agtOrgId).getCSCancel() + cancel);
					}
				}
				DBConnect.closePstmt(prodPstmt);
				DBConnect.closeCstmt(cstmt);
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
						agtMap.get(orgId).setOlaWithdrawalAmt(
								olaResponseBean.getNetWithdrawalAmt());
						agtMap.get(orgId).setOlaDepositAmt(
								olaResponseBean.getNetDepositAmt());
					}
				}
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
			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl
						.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					IWOrgReportResponseBean iwResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setIwSale(
								agtMap.get(orgId).getIwSale()
										+ iwResponseBean.getSaleAmt());
						agtMap.get(orgId).setIwCancel(
								agtMap.get(orgId).getIwCancel()
										+ iwResponseBean.getCancelAmt());
						agtMap.get(orgId).setIwPwt(
								agtMap.get(orgId).getIwPwt()
										+ iwResponseBean.getPwtAmt());
						agtMap.get(orgId).setIwDirPlyPwt(
								agtMap.get(orgId).getIwDirPlyPwt()
										+ iwResponseBean.getPwtDirAmt());
					}
				}
			}
			if (ReportUtility.isVS) {
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while(rsGame.next()){
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setGameId(rsGame.getInt("game_id"));
				Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl
						.fetchSaleCancelPWTMultipleAgent(requestBean, con);

				for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					VSOrgReportResponseBean vsResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setVsSale(
								agtMap.get(orgId).getVsSale()
										+ vsResponseBean.getSaleAmt());
						agtMap.get(orgId).setVsCancel(
								agtMap.get(orgId).getVsCancel()
										+ vsResponseBean.getCancelAmt());
						agtMap.get(orgId).setVsPwt(
								agtMap.get(orgId).getVsPwt()
										+ vsResponseBean.getPwtAmt());
						agtMap.get(orgId).setVsDirPlyPwt(
								agtMap.get(orgId).getVsDirPlyPwt()
										+ vsResponseBean.getPwtDirAmt());
					}
				}
			}	}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(pstmt, rsGame);
		}

		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {

			// Get All Agent
			String agtOrgQry = QueryManager.getOrgQry("AGENT");
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					cstmt = con.prepareCall("{call drawCollectionAgentWisePerGame(?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("parent_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
						gameMap.get(agtId).setDrawDirPlyPwt(
								rs.getDouble("pwtDir"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					cstmt = con.prepareCall("{call scratchCollectionAgentWisePerGame(?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						sale = rs.getDouble("sale");
						cancel = rs.getDouble("cancel");
						pwt = rs.getDouble("pwt");
						gameMap.get(agtId).setDrawSale(sale - cancel);
						gameMap.get(agtId).setDrawPwt(pwt);
						gameMap.get(agtId).setScratchDirPlyPwt(
								rs.getDouble("pwtDir"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (ReportUtility.isCS) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg));
					cstmt = con.prepareCall("{call csCollectionAgentWisePerCategory(?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("parent_id");
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}
					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}
				DBConnect.closePstmt(prodPstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("CS", gameAgtMap);

			}

			if (ReportUtility.isOLA) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master where wallet_status='ACTIVE'";
				PreparedStatement walletPstmt = con.prepareStatement(walletQry);
				ResultSet rsWallet = walletPstmt.executeQuery();
				while (rsWallet.next()) {
					int walletId = rsWallet.getInt("wallet_id");
					String walletName = rsWallet.getString("wallet_name");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg));
					OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
					requestBean.setFromDate(startDate.toString());
					requestBean.setToDate(endDate.toString());
					requestBean.setWalletId(walletId);
					Map<Integer, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl
							.fetchDepositWithdrawlMultipleAgent(requestBean,
									con);
					for (Map.Entry<Integer, OlaOrgReportResponseBean> entry : olaResponseBeanMap
							.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						OlaOrgReportResponseBean olaResponseBean = entry
								.getValue();
						if (prodMap.containsKey(orgId)) {
							prodMap.get(orgId).setOlaWithdrawalAmt(
									olaResponseBean.getNetWithdrawalAmt());
							prodMap.get(orgId).setOlaDepositAmt(
									olaResponseBean.getNetDepositAmt());
							prodMap.get(orgId).setOlaNetGaming(0.0);
						}
					}

					gameAgtMap.put(walletName, prodMap);
				}

				serGameAgtMap.put("OLA", gameAgtMap);
			}

			if (ReportUtility.isSLE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getSLEGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl
							.fetchSaleCancelPWTMultipleAgentGameWise(
									requestBean, con);
					for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : sleResponseBeanMap
							.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						SLEOrgReportResponseBean sleResponseBean = entry
								.getValue();
						if (gameMap.containsKey(orgId)) {
							gameMap.get(orgId).setSleSale(
									sleResponseBean.getSaleAmt());
							gameMap.get(orgId).setSleCancel(
									sleResponseBean.getCancelAmt());
							gameMap.get(orgId).setSlePwt(
									sleResponseBean.getPwtAmt());
							gameMap.get(orgId).setSleDirPlyPwt(
									sleResponseBean.getPwtDirAmt());
						}
					}

					gameAgtMap.put(rsGame.getString("game_disp_name"), gameMap);
				}

				serGameAgtMap.put("SLE", gameAgtMap);
			}
			if (ReportUtility.isIW) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl
							.fetchSaleCancelPWTMultipleAgentGameWise(
									requestBean, con);
					for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap
							.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						IWOrgReportResponseBean iwResponseBean = entry
								.getValue();
						if (gameMap.containsKey(orgId)) {
							gameMap.get(orgId).setIwSale(
									iwResponseBean.getSaleAmt());
							gameMap.get(orgId).setIwCancel(
									iwResponseBean.getCancelAmt());
							gameMap.get(orgId).setIwPwt(
									iwResponseBean.getPwtAmt());
							gameMap.get(orgId).setIwDirPlyPwt(
									iwResponseBean.getPwtDirAmt());
						}
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("IW", gameAgtMap);
			}
			if (ReportUtility.isVS) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl
							.fetchSaleCancelPWTMultipleAgentGameWise(
									requestBean, con);
					for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap
							.entrySet()) {
						String orgId = String.valueOf(entry.getKey());
						VSOrgReportResponseBean vsResponseBean = entry
								.getValue();
						if (gameMap.containsKey(orgId)) {
							gameMap.get(orgId).setVsSale(
									vsResponseBean.getSaleAmt());
							gameMap.get(orgId).setVsCancel(
									vsResponseBean.getCancelAmt());
							gameMap.get(orgId).setVsPwt(
									vsResponseBean.getPwtAmt());
							gameMap.get(orgId).setVsDirPlyPwt(
									vsResponseBean.getPwtDirAmt());
						}
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("VS", gameAgtMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
		}

		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionDayWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			String viewBy, int orgId) {
		CallableStatement cstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		Map<String, CompleteCollectionBean> dateMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		String date = null;

		if (startDate.after(endDate)) {
			return dateMap;
		}

		try {

			// Get All Agent
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			while (startCal.getTime().before(endCal.getTime())) {
				date = new java.sql.Date(startCal.getTimeInMillis()).toString();
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(date);
				dateMap.put(date, collBean);
				startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60
						* 60 * 1000);
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (ReportUtility.isSLE) {
					collBean.setSleSale(0.0);
					collBean.setSlePwt(0.0);
					collBean.setSleCancel(0.0);
					collBean.setSleDirPlyPwt(0.0);
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
			}

			cstmt = con.prepareCall("{call fillDateTable(?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.executeQuery();
			cstmt.close();
			if (ReportUtility.isDG) {
				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("{call drawCollectionDayWisePerGame(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String tempDate = rs.getString("alldate");
						dateMap.get(tempDate).setDrawSale(
								dateMap.get(tempDate).getDrawSale()
										+ rs.getDouble("sale"));
						dateMap.get(tempDate).setDrawCancel(
								dateMap.get(tempDate).getDrawCancel()
										+ rs.getDouble("cancel"));
						dateMap.get(tempDate).setDrawPwt(
								dateMap.get(tempDate).getDrawPwt()
										+ rs.getDouble("pwt"));
						dateMap.get(tempDate).setDrawDirPlyPwt(
								dateMap.get(tempDate).getDrawDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
					//cstmt.close();
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con
							.prepareCall("{call scratchCollectionDayWisePerGame(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						String tempDate = rs.getString("alldate");
						double sale = rs.getDouble("sale")
								- rs.getDouble("cancel");
						dateMap.get(tempDate).setScratchSale(
								dateMap.get(tempDate).getScratchSale() + sale);
						dateMap.get(tempDate).setScratchPwt(
								dateMap.get(tempDate).getScratchPwt()
										+ rs.getDouble("pwt"));
						dateMap.get(tempDate).setScratchDirPlyPwt(
								dateMap.get(tempDate).getScratchDirPlyPwt()
										+ rs.getDouble("pwtDir"));
					}
				}
				DBConnect.closePstmt(gamePstmt);
			}
			if (ReportUtility.isCS) {

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();

				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					cstmt = con
							.prepareCall("{call csCollectionDayWisePerCategory(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String tempDate = rs.getDate("alldate").toString();
						dateMap.get(tempDate).setCSSale(
								dateMap.get(tempDate).getCSSale()
										+ rs.getDouble("sale"));
						dateMap.get(tempDate).setCSCancel(
								dateMap.get(tempDate).getCSCancel()
										+ rs.getDouble("cancel"));
					}
				}
				DBConnect.closePstmt(prodPstmt);
			}

			if (ReportUtility.isOLA) {

				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(startDate.toString());
				requestBean.setToDate(endDate.toString());
				requestBean.setOrgId(orgId);
				Map<String, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl
						.fetchDepositWithdrawlSingleAgentDateWise(requestBean,
								con);

				for (Map.Entry<String, OlaOrgReportResponseBean> entry : olaResponseBeanMap
						.entrySet()) {
					String txndate = entry.getKey();
					OlaOrgReportResponseBean olaResponseBean = entry.getValue();
					if (dateMap.containsKey(txndate)) {
						dateMap.get(txndate).setOlaWithdrawalAmt(
								olaResponseBean.getNetWithdrawalAmt());
						dateMap.get(txndate).setOlaDepositAmt(
								olaResponseBean.getNetDepositAmt());
					}
				}
			}

			if (ReportUtility.isSLE) {
				String gameQry = ReportUtility.getSLEGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");

					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, SLEOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = SLEAgentReportsControllerImpl
								.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = SLEAgentReportsControllerImpl
								.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, SLEOrgReportResponseBean> entry : responseMap
							.entrySet()) {
						String tmpdate = entry.getKey();
						SLEOrgReportResponseBean responseBean = entry
								.getValue();
						dateMap.get(tmpdate).setSleSale(
								dateMap.get(tmpdate).getSleSale()
										+ responseBean.getSaleAmt());
						dateMap.get(tmpdate).setSleCancel(
								dateMap.get(tmpdate).getSleCancel()
										+ responseBean.getCancelAmt());
						dateMap.get(tmpdate).setSlePwt(
								dateMap.get(tmpdate).getSlePwt()
										+ responseBean.getPwtAmt());
						dateMap.get(tmpdate).setSleDirPlyPwt(
								dateMap.get(tmpdate).getSleDirPlyPwt()
										+ responseBean.getPwtDirAmt());
					}
				}
			}
			if (ReportUtility.isIW) {
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");

					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, IWOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = IWAgentReportsControllerImpl.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = IWAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, IWOrgReportResponseBean> entry : responseMap.entrySet()) {
						String tmpdate = entry.getKey();
						IWOrgReportResponseBean responseBean = entry.getValue();
						dateMap.get(tmpdate).setIwSale(dateMap.get(tmpdate).getIwSale() + responseBean.getSaleAmt());
						dateMap.get(tmpdate).setIwCancel(dateMap.get(tmpdate).getIwCancel() + responseBean.getCancelAmt());
						dateMap.get(tmpdate).setIwPwt(dateMap.get(tmpdate).getIwPwt() + responseBean.getPwtAmt());
						dateMap.get(tmpdate).setIwDirPlyPwt(dateMap.get(tmpdate).getIwDirPlyPwt() + responseBean.getPwtDirAmt());
					}
				}
			}
			if (ReportUtility.isVS) {
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");

					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, VSOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, VSOrgReportResponseBean> entry : responseMap.entrySet()) {
						String tmpdate = entry.getKey();
						VSOrgReportResponseBean responseBean = entry.getValue();
						dateMap.get(tmpdate).setVsSale(dateMap.get(tmpdate).getVsSale() + responseBean.getSaleAmt());
						dateMap.get(tmpdate).setVsCancel(dateMap.get(tmpdate).getVsCancel() + responseBean.getCancelAmt());
						dateMap.get(tmpdate).setVsPwt(dateMap.get(tmpdate).getVsPwt() + responseBean.getPwtAmt());
						dateMap.get(tmpdate).setVsDirPlyPwt(dateMap.get(tmpdate).getVsDirPlyPwt() + responseBean.getPwtDirAmt());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCstmt(cstmt);
		}
		return dateMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionDayWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con,
			String viewBy, int orgId) {
		CallableStatement cstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;

		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}

		try {
			cstmt = con.prepareCall("{call fillDateTable(?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.executeQuery();
			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));
					cstmt = con
							.prepareCall("{call drawCollectionDayWisePerGame(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
						gameMap.get(agtId).setDrawDirPlyPwt(
								rs.getDouble("pwtDir"));
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("DG", gameAgtMap);
			}

			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));
					cstmt = con
							.prepareCall("{call scratchCollectionDayWisePerGame(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						String tempDate = rs.getString("alldate");
						double sale = rs.getDouble("sale")
								- rs.getDouble("cancel");
						gameMap.get(tempDate).setDrawSale(sale);
						gameMap.get(tempDate).setDrawPwt(rs.getDouble("pwt"));
						gameMap.get(tempDate).setScratchDirPlyPwt(
								rs.getDouble("pwtDir"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (ReportUtility.isCS) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Category Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getDayMap(startDate, endDate));
					cstmt = con
							.prepareCall("{call csCollectionDayWisePerCategory(?,?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, orgId);
					cstmt.setString(5, viewBy);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate").split(" ")[0];
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}

					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}
				DBConnect.closePstmt(prodPstmt);

				serGameAgtMap.put("CS", gameAgtMap);

			}

			if (ReportUtility.isOLA) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master where wallet_status='ACTIVE'";
				PreparedStatement walletPstmt = con.prepareStatement(walletQry);
				ResultSet rsWallet = walletPstmt.executeQuery();
				while (rsWallet.next()) {
					int walletId = rsWallet.getInt("wallet_id");
					String walletName = rsWallet.getString("wallet_name");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getDayMap(startDate, endDate));
					OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
					requestBean.setFromDate(startDate.toString());
					requestBean.setToDate(endDate.toString());
					requestBean.setWalletId(walletId);
					Map<String, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl
							.fetchDepositWithdrawlSingleAgentDateWise(
									requestBean, con);
					for (Map.Entry<String, OlaOrgReportResponseBean> entry : olaResponseBeanMap
							.entrySet()) {
						String txnDate = entry.getKey();
						OlaOrgReportResponseBean olaResponseBean = entry
								.getValue();
						if (prodMap.containsKey(txnDate)) {
							prodMap.get(txnDate).setOlaWithdrawalAmt(
									olaResponseBean.getNetWithdrawalAmt());
							prodMap.get(txnDate).setOlaDepositAmt(
									olaResponseBean.getNetDepositAmt());

						}
					}

					gameAgtMap.put(walletName, prodMap);
				}

				serGameAgtMap.put("OLA", gameAgtMap);
			}

			if (ReportUtility.isSLE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getSLEGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));

					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, SLEOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = SLEAgentReportsControllerImpl
								.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = SLEAgentReportsControllerImpl
								.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, SLEOrgReportResponseBean> entry : responseMap
							.entrySet()) {
						String agtId = entry.getKey();
						SLEOrgReportResponseBean responseBean = entry
								.getValue();
						gameMap.get(agtId)
								.setSleSale(responseBean.getSaleAmt());
						gameMap.get(agtId).setSleCancel(
								responseBean.getCancelAmt());
						gameMap.get(agtId).setSlePwt(responseBean.getPwtAmt());
						gameMap.get(agtId).setSleDirPlyPwt(
								responseBean.getPwtDirAmt());
					}
					gameAgtMap.put(rsGame.getString("game_disp_name"), gameMap);
				}

				serGameAgtMap.put("SLE", gameAgtMap);
			}
			if (ReportUtility.isIW) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));

					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, IWOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = IWAgentReportsControllerImpl
								.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = IWAgentReportsControllerImpl
								.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, IWOrgReportResponseBean> entry : responseMap
							.entrySet()) {
						String agtId = entry.getKey();
						IWOrgReportResponseBean responseBean = entry
								.getValue();
						gameMap.get(agtId)
								.setIwSale(responseBean.getSaleAmt());
						gameMap.get(agtId).setIwCancel(
								responseBean.getCancelAmt());
						gameMap.get(agtId).setIwPwt(responseBean.getPwtAmt());
						gameMap.get(agtId).setIwDirPlyPwt(
								responseBean.getPwtDirAmt());
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("IW", gameAgtMap);
			}
			if (ReportUtility.isVS) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));

					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setGameId(gameId);
					requestBean.setOrgId(orgId);

					Map<String, VSOrgReportResponseBean> responseMap = null;

					if ("BO".equals(viewBy)) {
						responseMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseForBO(requestBean, con);
					} else if ("AGENT".equals(viewBy)) {
						responseMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, con);
					}

					for (Map.Entry<String, VSOrgReportResponseBean> entry : responseMap.entrySet()) {
						String agtId = entry.getKey();
						VSOrgReportResponseBean responseBean = entry.getValue();
						gameMap.get(agtId).setVsSale(responseBean.getSaleAmt());
						gameMap.get(agtId).setVsCancel(responseBean.getCancelAmt());
						gameMap.get(agtId).setVsPwt(responseBean.getPwtAmt());
						gameMap.get(agtId).setVsDirPlyPwt(responseBean.getPwtDirAmt());
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("VS", gameAgtMap);
			}
			logger.debug(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCstmt(cstmt);
		}
		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType) {

		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;

		try {
			con = DBConnect.getConnection();

			if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con, "BO", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean) {

		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;

		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con, "BO", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportExpand(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean) {

		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;

		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			if ("Agent Wise Expand".equals(reportType)) {
				agtMap = collectionAgentWiseExpand(startDate, endDate, con);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con, "BO",
						0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReportForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId,
			String reportType) {

		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;

		try {
			con = DBConnect.getConnection();

			if ("Retailer Wise".equals(reportType)) {
				agtMap = collectionRetailerWise(startDate, endDate, con,
						agtOrgId);
			} else if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con, "AGENT",
						agtOrgId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportForAgentExpand(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {

		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;

		try {
			con = DBConnect.getConnection();

			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con,
						agtOrgId);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con,
						"AGENT", agtOrgId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionRetailerWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;

		String agtOrgQry = null;

		if (startDate.after(endDate)) {
			return agtMap;
		}

		// for scratch game
		try {

			if (agtOrgId == 0) {
				/*
				 * agtOrgQry =
				 * "select name,organization_id from st_lms_organization_master where organization_type='RETAILER' order by name"
				 * ;
				 */
				agtOrgQry = QueryManager.getOrgQry("RETAILER");
			} else {
				/*
				 * agtOrgQry =
				 * "select name,organization_id from st_lms_organization_master where parent_id="
				 * + agtOrgId + " order by name";
				 */

				String appendOrder = QueryManager.getAppendOrgOrder();
				agtOrgQry = "select name orgCode,organization_id from st_lms_organization_master where  parent_id="
						+ agtOrgId + "  order by " + appendOrder;
				if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {

					agtOrgQry = "select org_code orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("CODE_NAME")) {

					agtOrgQry = "select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("NAME_CODE")) {
					agtOrgQry = "select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				}

			}
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("orgCode"));
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (ReportUtility.isSLE) {
					collBean.setSleSale(0.0);
					collBean.setSlePwt(0.0);
					collBean.setSleCancel(0.0);
					collBean.setSleDirPlyPwt(0.0);
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
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			agtOrgQry = agtOrgQry.replace("name,", "");
			if (ReportUtility.isDG) {

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con.prepareCall("{call drawCollectionRetailerWisePerGame(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					String retOrgId = null;
					while (rs.next()) {
						retOrgId = rs.getString("organization_id");
						double sale = agtMap.get(retOrgId).getDrawSale()
								+ rs.getDouble("sale");
						agtMap.get(retOrgId).setDrawSale(sale);
						agtMap.get(retOrgId).setDrawCancel(
								agtMap.get(retOrgId).getDrawCancel()
										+ rs.getDouble("cancel"));
						agtMap.get(retOrgId).setDrawPwt(
								agtMap.get(retOrgId).getDrawPwt()
										+ rs.getDouble("pwt"));
					}
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);
			}
			if (ReportUtility.isSE) {
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					cstmt = con.prepareCall("{call scratchCollectionRetailerWisePerGame(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					String retOrgId = null;
					while (rs.next()) {
						retOrgId = rs.getString("organization_id");
						double sale = agtMap.get(retOrgId).getScratchSale()
								+ (rs.getDouble("sale") - rs
										.getDouble("cancel"));
						agtMap.get(retOrgId).setScratchSale(sale);
						agtMap.get(retOrgId).setScratchPwt(
								agtMap.get(retOrgId).getScratchPwt()
										+ rs.getDouble("pwt"));
					}
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);
			}
			if (ReportUtility.isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					cstmt = con.prepareCall("{call csCollectionRetailerWisePerCategory(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					String retOrgId = null;
					while (rs.next()) {
						retOrgId = rs.getString("organization_id");
						agtMap.get(retOrgId).setCSSale(
								agtMap.get(retOrgId).getCSSale()
										+ rs.getDouble("sale"));
						agtMap.get(retOrgId).setCSCancel(
								agtMap.get(retOrgId).getCSCancel()
										+ rs.getDouble("cancel"));
					}
				}
				DBConnect.closePstmt(prodPstmt);
				DBConnect.closeCstmt(cstmt);
			}
			if (ReportUtility.isOLA) {

				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(startDate.toString());
				requestBean.setToDate(endDate.toString());
				requestBean.setOrgId(agtOrgId);
				Map<Integer, OlaOrgReportResponseBean> olaResponseBeanMap = OlaRetailerReportControllerImpl
						.fetchDepositWithdrawlMultipleRetailer(requestBean, con);
				for (Map.Entry<Integer, OlaOrgReportResponseBean> entry : olaResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					OlaOrgReportResponseBean olaResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setOlaWithdrawalAmt(
								olaResponseBean.getNetWithdrawalAmt());
						agtMap.get(orgId).setOlaDepositAmt(
								olaResponseBean.getNetDepositAmt());
						agtMap.get(orgId).setOlaNetGaming(0.0);
					}
				}

			}

			if (ReportUtility.isSLE) {

				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<Integer, SLEOrgReportResponseBean> sleResponseBeanMap = SLERetailerReportsControllerImpl
						.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
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
					}
				}

			}
			if (ReportUtility.isIW) {

				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<Integer, IWOrgReportResponseBean> iwResponseBeanMap = IWRetailerReportsControllerImpl
						.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
				for (Map.Entry<Integer, IWOrgReportResponseBean> entry : iwResponseBeanMap
						.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					IWOrgReportResponseBean iwResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setIwSale(
								iwResponseBean.getSaleAmt());
						agtMap.get(orgId).setIwCancel(	
								iwResponseBean.getCancelAmt());
						agtMap.get(orgId)
								.setIwPwt(iwResponseBean.getPwtAmt());
					}
				}
			}
			
			if (ReportUtility.isVS) {
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				requestBean.setOrgId(agtOrgId);
				Map<Integer, VSOrgReportResponseBean> vsResponseBeanMap = VSRetailerReportsControllerImpl
						.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
				for (Map.Entry<Integer, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
					String orgId = String.valueOf(entry.getKey());
					VSOrgReportResponseBean vsResponseBean = entry.getValue();
					if (agtMap.containsKey(orgId)) {
						agtMap.get(orgId).setVsSale(vsResponseBean.getSaleAmt());
						agtMap.get(orgId).setVsCancel(vsResponseBean.getCancelAmt());
						agtMap.get(orgId).setVsPwt(vsResponseBean.getPwtAmt());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
		}

		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {

			// Get All Agent
			String orgCodeQry = " name orgCode ";

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "org_code orgCode ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";

			}

			String agtOrgQry = "select "
					+ orgCodeQry
					+ ",organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by "
					+ QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					cstmt = con.prepareCall("{call drawCollectionRetailerWisePerGame(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility
						.getScratchGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					cstmt = con.prepareCall("{call scratchCollectionRetailerWisePerGame(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (ReportUtility.isCS) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Category Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master WHERE status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				CallableStatement cstmt = null;
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg));
					cstmt = con.prepareCall("{call csCollectionRetailerWisePerCategory(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, agtOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}
					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}
				DBConnect.closePstmt(prodPstmt);
				DBConnect.closeCstmt(cstmt);

				serGameAgtMap.put("CS", gameAgtMap);

			}

			if (ReportUtility.isSLE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility.getSLEGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setOrgId(agtOrgId);
					requestBean.setGameId(gameId);
					Map<Integer, SLEOrgReportResponseBean> respMap = SLERetailerReportsControllerImpl
							.fetchSaleCancelPwtMultipleRetailerGameWise(
									requestBean, con);
					for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : respMap
							.entrySet()) {
						String agtId = String.valueOf(entry.getKey());
						SLEOrgReportResponseBean bean = entry.getValue();
						gameMap.get(agtId).setSleSale(bean.getSaleAmt());
						gameMap.get(agtId).setSleCancel(bean.getCancelAmt());
						gameMap.get(agtId).setSlePwt(bean.getPwtAmt());
					}
					gameAgtMap.put(rsGame.getString("game_disp_name"), gameMap);
				}
				serGameAgtMap.put("SLE", gameAgtMap);
			}
			
			if (ReportUtility.isIW) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setOrgId(agtOrgId);
					requestBean.setGameId(gameId);
					Map<Integer, IWOrgReportResponseBean> respMap = IWRetailerReportsControllerImpl.fetchSaleCancelPwtMultipleRetailerGameWise(requestBean, con);
					for (Map.Entry<Integer, IWOrgReportResponseBean> entry : respMap.entrySet()) {
						String agtId = String.valueOf(entry.getKey());
						IWOrgReportResponseBean bean = entry.getValue();
						gameMap.get(agtId).setIwSale(bean.getSaleAmt());
						gameMap.get(agtId).setIwCancel(bean.getCancelAmt());
						gameMap.get(agtId).setIwPwt(bean.getPwtAmt());
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("IW", gameAgtMap);
			}

			if (ReportUtility.isVS) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));

					VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
					requestBean.setFromDate(startDate);
					requestBean.setToDate(endDate);
					requestBean.setOrgId(agtOrgId);
					requestBean.setGameId(gameId);
					Map<Integer, VSOrgReportResponseBean> respMap = VSRetailerReportsControllerImpl.fetchSaleCancelPwtMultipleRetailerGameWise(requestBean, con);
					for (Map.Entry<Integer, VSOrgReportResponseBean> entry : respMap.entrySet()) {
						String agtId = String.valueOf(entry.getKey());
						VSOrgReportResponseBean bean = entry.getValue();
						gameMap.get(agtId).setVsSale(bean.getSaleAmt());
						gameMap.get(agtId).setVsCancel(bean.getCancelAmt());
						gameMap.get(agtId).setVsPwt(bean.getPwtAmt());
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("VS", gameAgtMap);
			}

			logger.debug(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
		}

		return serGameAgtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpandMrpWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		StringBuilder drawQry = null;
		// for scratch game
		StringBuilder scratchQry = null;
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {

			// Get All Agent
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select bb.organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ agtOrgId
								+ ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				scratchQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.organization_id,sale,pwt from");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id, (gmti.sold_tickets*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER'and gmti.date>='"
						+ startDate
						+ "' and gmti.date<='"
						+ endDate
						+ "' and gmti.game_id=gm.game_id and gm.game_id=?) aa group by current_owner_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on current_owner_id=organization_id group by organization_id) sale, ";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" where sale.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id=4) agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand scratch game Qry:: "
						+ scratchQry);

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (ReportUtility.isIW) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder("select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select drs.retailer_org_id,sum(mrp_amt) as sale from st_iw_ret_sale drs where drs.game_id = ? and drs.is_cancel != 'Y' and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id=" + agtOrgId + ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select bb.organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_iw_ret_sale_refund drs where drs.game_id = ? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id=" + agtOrgId + ") bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt drs where drs.game_id = ? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id=" + agtOrgId + ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id=" + agtOrgId + ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n" + pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("IW", gameAgtMap);
			}
			if (ReportUtility.isVS) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select drs.retailer_org_id,sum(mrp_amt) as sale from st_vs_ret_sale drs where drs.game_id = ? and drs.is_cancel != 'Y' and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('VS_SALE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select bb.organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_vs_ret_sale_refund drs where drs.game_id = ? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('VS_SALE_REFUND') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt) as pwt from st_vs_ret_pwt drs where drs.game_id = ? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				DBConnect.closePstmt(gamePstmt);

				serGameAgtMap.put("VS", gameAgtMap);
			}
			System.out.println(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
		}
		return serGameAgtMap;
	}

	private Map<String, CompleteCollectionBean> getAgentMap(ResultSet rsRetOrg)
			throws SQLException {
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;

		while (rsRetOrg.next()) {
			collBean = new CompleteCollectionBean();
			collBean.setOrgName(rsRetOrg.getString("orgCode"));
			if (ReportUtility.isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
			}
			if (ReportUtility.isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
			}
			if (ReportUtility.isOLA) {
				collBean.setOlaDepositAmt(0.0);
				collBean.setOlaDepositCancelAmt(0.0);
				collBean.setOlaWithdrawalAmt(0.0);
			}
			agtMap.put(rsRetOrg.getString("organization_id"), collBean);
		}
		rsRetOrg.beforeFirst();
		return agtMap;
	}

	private Map<String, CompleteCollectionBean> getDayMap(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Map<String, CompleteCollectionBean> dateMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		String date = null;
		// Get All Day
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while (startCal.getTime().before(endCal.getTime())) {
			date = new java.sql.Date(startCal.getTimeInMillis()).toString();
			collBean = new CompleteCollectionBean();
			collBean.setOrgName(date);
			dateMap.put(date, collBean);
			startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60 * 60
					* 1000);
			if (ReportUtility.isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
				collBean.setDrawDirPlyPwt(0.0);
			}
			if (ReportUtility.isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
				collBean.setScratchDirPlyPwt(0.0);
			}
		}
		return dateMap;
	}

	public Map<Integer, String> getOrgAddMap(String orgType, int parentId)
			throws LMSException {
		Map<Integer, String> orgAddMap = new TreeMap<Integer, String>();
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (orgType.equalsIgnoreCase("AGENT")) {
				pstmt = con
						.prepareStatement("select addr_line1, addr_line2, city, organization_id from st_lms_organization_master where organization_type='AGENT'");

			} else if (orgType.equalsIgnoreCase("RETAILER")) {
				pstmt = con
						.prepareStatement("select addr_line1, addr_line2, city, organization_id from st_lms_organization_master where organization_type='RETAILER' and parent_id = ?");
				pstmt.setInt(1, parentId);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgAddMap.put(rs.getInt("organization_id"), rs
						.getString("addr_line1")
						+ ","
						+ rs.getString("addr_line2")
						+ ","
						+ rs.getString("city"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closePstmt(pstmt);
		}
		return orgAddMap;
	}

	public Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> transactionReportForAgent(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {

		Connection con = null;
		Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> resultMap = new HashMap<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>>();
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;

		try {
			con = DBConnect.getConnection();

			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con,
						agtOrgId);
				resultMap.put("NetAmt", agtMap);
				agtMap = collectionRetailerWiseExpandMrpWise(startDate,
						endDate, con, agtOrgId);
				resultMap.put("MrpAmt", agtMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return resultMap;
	}
}
