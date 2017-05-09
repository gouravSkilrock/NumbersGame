package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class CollectionReportOverAllHelperLagos {
	static Log logger = LogFactory
			.getLog(CollectionReportOverAllHelperLagos.class);

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA)
			throws LMSException {

		double totalOpnBal = 0.0;
		String agtOrgQry = null;

		Connection con = null;
		ResultSet rsRetOrg = null;
		PreparedStatement pstmt = null;

		Map<String, CollectionReportOverAllBean> agtMap = null;
		CollectionReportOverAllBean collBean = null;

		try {
			if (startDate.after(endDate))
				return null;

			con = DBConnect.getConnection();
			agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
			agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
				collBean.setAgentName(rsRetOrg.getString("name"));
				collBean.setOpeningBal(0.0);
				collBean.setCash(0.0);
				collBean.setCheque(0.0);
				collBean.setChequeReturn(0.0);
				collBean.setCredit(0.0);
				collBean.setDebit(0.0);
				collBean.setBankDep(0.0);
				if (isDG) {
					collBean.setDgSale(0.0);
					collBean.setDgPwt(0.0);
					collBean.setDgCancel(0.0);
					collBean.setDgDirPlyPwt(0.0);
				}
				if (isSE) {
					collBean.setSeSale(0.0);
					collBean.setSePwt(0.0);
					collBean.setSeDirPlyPwt(0.0);
				}
				if (isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if (isOLA) {
					collBean.setDeposit(0.0);
					collBean.setDepositRefund(0.0);
					collBean.setWithdrawal(0.0);
					collBean.setWithdrawalRefund(0.0);
					collBean.setNetGamingComm(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}

			// for calculation of opening Balance
			collectionAgentWiseLagos(deployDate, new Timestamp(startDate
					.getTime() - 1000), con, isDG, isSE, isCS, isOLA, agtMap);
			Iterator<Map.Entry<String, CollectionReportOverAllBean>> itr = agtMap
					.entrySet().iterator();
			double totalNetPymnts = 0.0;
			while (itr.hasNext()) {
				Map.Entry<String, CollectionReportOverAllBean> pair = itr
						.next();
				CollectionReportOverAllBean bean = pair.getValue();
				double netPymnts = -bean.getCash() - bean.getCheque()
						- bean.getCredit() - bean.getBankDep()
						+ bean.getDebit() + bean.getChequeReturn();

				totalNetPymnts += netPymnts;
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
				totalOpnBal += openingBal;
				bean.setOpeningBal(0.0);
				bean.setNetPayments(0.0);
				if (isDG) {
					bean.setDgSale(0.0);
					bean.setDgPwt(0.0);
					bean.setDgCancel(0.0);
					bean.setDgDirPlyPwt(0.0);
				}
				if (isSE) {
					bean.setSeSale(0.0);
					bean.setSePwt(0.0);
					bean.setSeDirPlyPwt(0.0);
				}
				if (isCS) {
					bean.setCSSale(0.0);
					bean.setCSCancel(0.0);
				}
				if (isOLA) {
					collBean.setDeposit(0.0);
					collBean.setDepositRefund(0.0);
					collBean.setWithdrawal(0.0);
					collBean.setWithdrawalRefund(0.0);
					collBean.setNetGamingComm(0.0);
				}

				bean.setOpeningBal(openingBal);
				bean.setNetPayments(netPymnts);
			}
			collBean = new CollectionReportOverAllBean();
			collBean.setAgentName("Total");
			collBean.setOpeningBal(0.0);
			collBean.setCash(0.0);
			collBean.setCheque(0.0);
			collBean.setChequeReturn(0.0);
			collBean.setCredit(0.0);
			collBean.setDebit(0.0);
			collBean.setBankDep(0.0);
			collBean.setNetPayments(totalNetPymnts);
			if (isDG) {
				collBean.setDgSale(0.0);
				collBean.setDgPwt(0.0);
				collBean.setDgCancel(0.0);
				collBean.setDgDirPlyPwt(0.0);
			}
			if (isSE) {
				collBean.setSeSale(0.0);
				collBean.setSePwt(0.0);
				collBean.setSeDirPlyPwt(0.0);
			}
			if (isCS) {
				collBean.setCSSale(0.0);
				collBean.setCSCancel(0.0);
			}
			if (isOLA) {
				collBean.setDeposit(0.0);
				collBean.setDepositRefund(0.0);
				collBean.setWithdrawal(0.0);
				collBean.setWithdrawalRefund(0.0);
				collBean.setNetGamingComm(0.0);
			}
			collBean.setOpeningBal(totalOpnBal);
			collBean.setNetPayments(totalNetPymnts);
			agtMap.put("9999999999", collBean);

			collectionAgentWiseExpandLagos(startDate, endDate, con, isDG, isSE,
					isCS, isOLA, agtMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionAgentWiseWithOpeningBal");
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rsRetOrg.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}

	public void collectionAgentWiseLagos(Timestamp startDate,
			Timestamp endDate, Connection con, boolean isDG, boolean isSE,
			boolean isCS, boolean isOLA,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		ResultSet rs = null;
		ResultSet rsGame = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		PreparedStatement gamePstmt = null;

		try {
			if (startDate.after(endDate)) {
				return;
			}

			// Get Account Details
			cstmt = con.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, startDate);
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

			if (isDG) {
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

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
				gamePstmt.close();
				rsGame.close();
			}
			if (isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
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
				gamePstmt.close();
				rsGame.close();
			}
			if (isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				gamePstmt = con.prepareStatement(catQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					cstmt = con
							.prepareCall("call csCollectionAgentWisePerCategory(?,?,?)");
					cstmt.setTimestamp(1, startDate);
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
				gamePstmt.close();
				rsGame.close();
			}
			if (isOLA) {

				StringBuilder olaQuery = new StringBuilder(
						"select WID.agtOrgId, wdraw,wdrawRef,depoAmt,depoRefAmt,netAmt from (select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from (select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WID, (select om.parent_id agtOrgId, ifnull(sum(wdRef), 0.0) wdrawRef from (select wrs.retailer_org_id, agent_net_amt as wdRef from st_ola_ret_withdrawl_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)WIDREF,(select om.parent_id agtOrgId, ifnull(sum(depo), 0.0) depoAmt from (select wrs.retailer_org_id, agent_net_amt as depo from st_ola_ret_deposit wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEP,(select om.parent_id agtOrgId, ifnull(sum(depoRef), 0.0) depoRefAmt from (select wrs.retailer_org_id, agent_net_amt as depoRef from st_ola_ret_deposit_refund wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_DEPOSIT_REFUND' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "')) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)DEPREF,(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
								+ startDate
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
									+ startDate
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}

	public void collectionAgentWiseExpandLagos(Timestamp startDate,
			Timestamp endDate, Connection con, boolean isDG, boolean isSE,
			boolean isCS, boolean isOLA,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		double totalNetPymnts = 0.0;

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;

		CompleteCollectionBean gameBean = null;
		CollectionReportOverAllBean agentBean = null;

		try {

			// Get Account Details
			cstmt = con.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				double netPymnts = 0.0;
				String agtOrgId = rs.getString("organization_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					netPymnts = rs.getDouble("cash") + rs.getDouble("chq")
							+ rs.getDouble("credit") + rs.getDouble("bank")
							- rs.getDouble("debit") - rs.getDouble("chq_ret");
					totalNetPymnts += netPymnts;
					agtMap.get(agtOrgId).setNetPayments(netPymnts);
				}
			}
			agtMap.get("9999999999").setNetPayments(totalNetPymnts);
			if (isDG) {
				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					double totalSale = 0.0;
					double totalCancel = 0.0;
					double totalPwt = 0.0;
					double totalDPlrpwt = 0.0;
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					cstmt = con
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
							totalSale += rs.getDouble("sale");
							totalCancel += rs.getDouble("cancel");
							totalPwt += rs.getDouble("pwt");
							totalDPlrpwt += rs.getDouble("pwtDir");
							gameBean.setOrgName(gameName);
							gameBean.setDrawSale(rs.getDouble("sale"));
							gameBean.setDrawCancel(rs.getDouble("cancel"));
							gameBean.setDrawPwt(rs.getDouble("pwt"));
							gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
						}
					}
					agentBean = agtMap.get("9999999999");
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
					gameBean.setDrawSale(totalSale);
					gameBean.setDrawCancel(totalCancel);
					gameBean.setDrawPwt(totalPwt);
					gameBean.setDrawDirPlyPwt(totalDPlrpwt);

				}
			}
			if (isSE) {
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					double totalSale = 0.0;
					double totalCancel = 0.0;
					double totalPwt = 0.0;
					double totalDPlrpwt = 0.0;
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					cstmt = con
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
							totalSale += rs.getDouble("sale");
							totalCancel += rs.getDouble("cancel");
							totalPwt += rs.getDouble("pwt");
							totalDPlrpwt += rs.getDouble("pwtDir");
							gameBean.setOrgName(gameName);
							gameBean.setDrawSale(rs.getDouble("sale"));
							gameBean.setDrawCancel(rs.getDouble("cancel"));
							gameBean.setDrawPwt(rs.getDouble("pwt"));
							gameBean.setDrawDirPlyPwt(rs.getDouble("pwtDir"));
						}
					}
					agentBean = agtMap.get("9999999999");
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
					gameBean.setDrawSale(totalSale);
					gameBean.setDrawCancel(totalCancel);
					gameBean.setDrawPwt(totalPwt);
					gameBean.setDrawDirPlyPwt(totalDPlrpwt);
				}
			}
			if (isCS) {
				// Category Master Query
				String prodQry = "select category_id,category_code from st_cs_product_category_master";
				PreparedStatement prodPstmt = con.prepareStatement(prodQry);
				ResultSet rsProd = prodPstmt.executeQuery();
				while (rsProd.next()) {
					double totalSale = 0.0;
					double totalCancel = 0.0;
					int catId = rsProd.getInt("category_id");
					String catName = rsProd.getString("category_code");
					cstmt = con
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
							totalSale += rs.getDouble("sale");
							totalCancel += rs.getDouble("cancel");
							gameBean.setOrgName(catName);
							gameBean.setCSSale(rs.getDouble("sale"));
							gameBean.setCSCancel(rs.getDouble("cancel"));
						}
					}
					agentBean = agtMap.get("9999999999");
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

			String wdTranOLA = null;
			String wdRefTranOLA = null;
			String depoTranOLA = null;
			String depoRefTranOLA = null;
			String netGTranOLA = null;
			StringBuilder OLAQry = null;
			if (isOLA) {
				OLAQry = new StringBuilder(
						"select withdraw.agtOrgId, (wdra-wdraRef) withdrawAmt, (depo-depoRef) depositAmt, netAmt netGamingComm from ");
				wdTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(wdra),0.0)wdra from (select ifnull(orw.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdra from st_ola_ret_withdrawl orw where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "') and orw.wallet_id = ? group by retId)wd right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wd.retId = om.organization_id group by parent_id)wd,";
				wdRefTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(wdraRef),0.0)wdraRef from (select ifnull(orwRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as wdraRef from st_ola_ret_withdrawl_refund orwRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_WITHDRAWL_REFUND' and transaction_date>= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "') and orwRef.wallet_id = ? group by retId)wdRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on wdRef.retId = om.organization_id group by parent_id)wdRef";
				depoTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(depo),0.0)depo from (select ifnull(ordepo.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depo from st_ola_ret_deposit ordepo where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT' and transaction_date>='"
						+ startDate
						+ "' and transaction_date <='"
						+ endDate
						+ "') and ordepo.wallet_id = ? group by retId)depo right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depo.retId = om.organization_id group by parent_id)depo,";
				depoRefTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(depoRef),0.0)depoRef from (select ifnull(ordepoRef.retailer_org_id,0)retId, ifnull(sum(agent_net_amt),0.0) as depoRef from st_ola_ret_deposit_refund ordepoRef where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type='OLA_DEPOSIT_REFUND' and transaction_date>= '"
						+ startDate
						+ "' and transaction_date <= '"
						+ endDate
						+ "') and ordepoRef.wallet_id = ? group by retId)depoRef right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on depoRef.retId = om.organization_id group by parent_id)depoRef";
				netGTranOLA = "(select om.parent_id agtOrgId, ifnull(sum(netGamingAmt), 0.0) netAmt from(select wrs.retailer_org_id, agt_net_claim_comm as netGamingAmt from st_ola_ret_comm wrs inner join st_lms_retailer_transaction_master rt on wrs.transaction_id=rt.transaction_id where transaction_type = 'OLA_COMMISSION' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and wallet_id = ?) wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id) netgaming";
				OLAQry.append("(select wd.agtOrgId, wdra, wdraRef from");
				OLAQry.append(wdTranOLA);
				OLAQry.append(wdRefTranOLA);
				OLAQry.append(" where wd.agtOrgId = wdRef.agtOrgId)withdraw,");
				OLAQry.append("(select depo.agtOrgId, depo, depoRef from");
				OLAQry.append(depoTranOLA);
				OLAQry.append(depoRefTranOLA);
				OLAQry
						.append(" where depo.agtOrgId = depoRef.agtOrgId)deposit, ");
				OLAQry.append(netGTranOLA);
				OLAQry
						.append(" where withdraw.agtOrgId = deposit.agtOrgId and netgaming.agtOrgId = withdraw.agtOrgId and netgaming.agtOrgId = deposit.agtOrgId");

				StringBuilder unionQuery = null;
				StringBuilder mainQuery = null;

				if (LMSFilterDispatcher.isRepFrmSP) {
					mainQuery = new StringBuilder(
							"select agtOrgId, sum(withdrawAmt) withdrawAmt,sum(depositAmt) depositAmt ,sum(netGamingComm) netGamingComm from (");
					unionQuery = new StringBuilder(
							" union all select parent_id agtOrgId, sum(withdrawal_net_amt)-sum(ref_withdrawal_net_amt) withdrawAmt ,sum(deposit_net) -sum(ref_deposit_net_amt) depositAmt,sum(net_gaming_net_comm) netGamingComm from st_rep_ola_retailer where finaldate>='"
									+ startDate
									+ "' and finaldate<='"
									+ endDate
									+ "'  and wallet_id=? group by  parent_id) repTable group by agtOrgId");
					mainQuery.append(OLAQry.toString()).append(
							unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				} else {
					pstmt = con.prepareStatement(OLAQry.toString());
				}
				logger.debug("For Expand OLA wallet Qry:: " + OLAQry);
				// Wallet Master Query
				String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master";
				PreparedStatement walletPstmt = con.prepareStatement(walletQry);
				ResultSet rsWallet = walletPstmt.executeQuery();
				while (rsWallet.next()) {
					double withdrawAmt = 0.0;
					double depositAmt = 0.0;
					double netGamingComm = 0.0;
					int walletId = rsWallet.getInt("wallet_id");
					String walletName = rsWallet.getString("wallet_name");
					// pstmt = con.prepareStatement(OLAQry.toString());
					pstmt.setInt(1, walletId);
					pstmt.setInt(2, walletId);
					pstmt.setInt(3, walletId);
					pstmt.setInt(4, walletId);
					pstmt.setInt(5, walletId);
					pstmt.setInt(6, walletId);
					logger.debug("-------Indivisual Wallet Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtOrgId = rs.getString("agtOrgId");
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
							withdrawAmt += rs.getDouble("withdrawAmt");
							depositAmt += rs.getDouble("depositAmt");
							netGamingComm += rs.getDouble("netGamingComm");
							gameBean.setOrgName(walletName);
							gameBean.setOlaWithdrawalAmt(rs
									.getDouble("withdrawAmt"));
							gameBean.setOlaDepositAmt(rs
									.getDouble("depositAmt"));
							gameBean.setOlaNetGaming(rs
									.getDouble("netGamingComm"));
						}
					}
					agentBean = agtMap.get("9999999999");
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

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise Expand");
		} finally {
			if (con != null) {
				DBConnect.closeCon(con);
			}
		}
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			System.out.println(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}

	public Map<String, String> allGameMap() throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select game_name,'DG' as game_type from st_dg_game_master union all select game_name,'SE' as game_type from st_se_game_master union all select category_code,'CS' as game_type from st_cs_product_category_master where status = 'ACTIVE' union all select wallet_name,'OLA' as game_type from st_ola_wallet_master order by game_type";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("game_name"), rs
						.getString("game_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Game List");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public static void main(String s[]) throws ParseException, LMSException {
		/*
		 * SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 * Timestamp depDate=new
		 * Timestamp(sdf.parse("29-10-2010 00:00:00").getTime()); Timestamp
		 * stDate=new Timestamp(sdf.parse("08-03-2013 23:59:59").getTime());
		 * Timestamp enDate=new
		 * Timestamp(sdf.parse("09-03-2013 23:59:59").getTime());
		 * 
		 * Map<String, CollectionReportOverAllBean>
		 * map=collectionAgentWiseWithOpeningBal( depDate, stDate,enDate,true,
		 * false,false,false);
		 * 
		 * 
		 * 
		 * System.out.println(map);
		 */

	}

}
