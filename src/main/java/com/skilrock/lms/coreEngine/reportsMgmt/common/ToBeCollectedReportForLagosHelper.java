package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.accMgmt.common.AgentOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class ToBeCollectedReportForLagosHelper {
	Log logger = LogFactory.getLog(ToBeCollectedReportForLagosHelper.class);
	final static long oneDay = 1 * 24 * 60 * 60 * 1000;

	public Map<String, CollectionReportOverAllBean> fetchDataForAgent(
			Timestamp deployDate, Timestamp startDate) throws LMSException {

		String agtOrgQry = null;
		Connection con = null;
		ResultSet rsRetOrg = null;
		PreparedStatement pstmt = null;

		CollectionReportOverAllBean collBean = null;
		//Map<Integer, CollectionReportOverAllBean> agtMap = null;
		Map<String, CollectionReportOverAllBean> agtMap = null;

		try {

		/*	con = DBConnect.getConnection();
			agtMap = new LinkedHashMap<Integer, CollectionReportOverAllBean>();
			agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
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
				collBean.setAgentName(rsRetOrg.getString("name"));
				agtMap.put(rsRetOrg.getInt("organization_id"), collBean);
			}*/
			agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
			
			agtOrgQry = QueryManager.getOrgQry("AGENT");
		/*	String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";*/
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
				collBean.setAgentName(rsRetOrg.getString("orgCode"));
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			collBean = new CollectionReportOverAllBean();
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
			
			collBean.setAgentName("Total");
			agtMap.put("-1111", collBean);

			// for calculation of opening Balance
			AgentOpeningBalanceHelper agentOpenHelper=new AgentOpeningBalanceHelper();
//			agentOpenHelper.collectionAgentWiseOpenningBal(deployDate,new Timestamp(startDate.getTime()- oneDay -1000), con,	agtMap);
			agentOpenHelper.collectionAgentWiseOpenningBal(deployDate,new Timestamp(startDate.getTime()- oneDay), con,	agtMap);

			// for calculation of opening Balance
			/*agentWiseOpenningBalForLagos(deployDate, new Timestamp(startDate
					.getTime()
					- oneDay - 1000), con, agtMap);*/

			// remove terminate Agent
			OrganizationTerminateReportHelper
					.getTerminateAgentListForRep(new Timestamp(startDate
							.getTime()
							- 1 * 24 * 60 * 60 * 1000), new Timestamp(startDate
							.getTime()
							+ 1 * 24 * 60 * 60 * 1000 - 1000));

			List<String> terminateAgentList = OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
			logger.info("Terminate agent List:: " + terminateAgentList);
			Set<String> agentListSet = agtMap.keySet();
			agentListSet.removeAll(terminateAgentList);

			// Get The Actual Data For Report
			getActualReportForLagos(startDate, con, agtMap);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in TO BE COLLECTED REPORT");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (rsRetOrg != null)
					rsRetOrg.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}

	public void agentWiseOpenningBalForLagos(Timestamp startDate,
			Timestamp endDate, Connection con,
			Map<Integer, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			// Get Account Details
			CallableStatement cstmt = con
					.prepareCall("call collectionCashChqOverAll(?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				int agtOrgId = rs.getInt("organization_id");
				agtMap.get(agtOrgId).setCash(rs.getDouble("cash"));
				agtMap.get(agtOrgId).setCheque(rs.getDouble("chq"));
				agtMap.get(agtOrgId).setChequeReturn(rs.getDouble("chq_ret"));
				agtMap.get(agtOrgId).setCredit(rs.getDouble("credit"));
				agtMap.get(agtOrgId).setDebit(rs.getDouble("debit"));
				agtMap.get(agtOrgId).setBankDep(rs.getDouble("bank"));
			}

			if (ReportUtility.isDG) {
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
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
					int agtOrgId;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getInt("parent_id");
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
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameNo);
					rs = cstmt.executeQuery();
					int agtOrgId;
					double sale = 0, cancel = 0, pwt = 0;
					while (rs.next()) {
						agtOrgId = rs.getInt("organization_id");
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
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					logger.debug("-------CS Sale Query------\n" + cstmt);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						agtMap.get(rs.getInt("parent_id")).setCSSale(
								agtMap.get(rs.getInt("parent_id")).getCSSale()
										+ rs.getDouble("sale"));
						agtMap.get(rs.getInt("parent_id")).setCSCancel(
								agtMap.get(rs.getInt("parent_id"))
										.getCSCancel()
										+ rs.getDouble("cancel"));
					}
				}
			}
			if (ReportUtility.isOLA) {

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
					agtMap.get(rs.getInt("agtOrgId")).setWithdrawal(
							rs.getDouble("wdraw"));
					agtMap.get(rs.getInt("agtOrgId")).setWithdrawalRefund(
							rs.getDouble("wdrawRef"));
					agtMap.get(rs.getInt("agtOrgId")).setDeposit(
							rs.getDouble("depoAmt"));
					agtMap.get(rs.getInt("agtOrgId")).setDepositRefund(
							rs.getDouble("depoRefAmt"));
					agtMap.get(rs.getInt("agtOrgId")).setNetGamingComm(
							rs.getDouble("netAmt"));
				}
			}
			Iterator<Map.Entry<Integer, CollectionReportOverAllBean>> itr = agtMap
					.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, CollectionReportOverAllBean> pair = itr
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

				bean.setOpeningBal(openingBal);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"agentWiseOpenningBalForLagos in To Be Collected");
		}
	}

	public void getActualReportForLagos(Timestamp stDate, Connection con,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		String toBeQuery = null;

		double totalOpeningBal = 0.0;
		double totalSale = 0.0;
		double totalDebit = 0.0;
		double totalCredit = 0.0;
		double totalPwt = 0.0;
		double totalCash = 0.0;
		double totalBank = 0.0;

		ResultSet rs = null;
		ResultSet rsGame = null;
		PreparedStatement pstmt = null;
		Statement stmt=null;
		PreparedStatement gamePstmt = null;

		CompleteCollectionBean gameBean = null;
		CollectionReportOverAllBean agentBean = null;

		Timestamp startDate = new Timestamp(stDate.getTime() - oneDay);
		Timestamp endDate = new Timestamp(startDate.getTime() + oneDay - 1000);

		try {
			// Query To Add/Subtract The Variables From The Opening Balance

			toBeQuery = "select  organization_id ,sum(cash) cashForOpnin from (select agent_org_id organization_id,sum(amount) cash from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select agent_org_id organization_id,sum(cheque_amt) cash from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select agent_org_id organization_id,-(sum(cheque_amt)) cash from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select agent_org_id organization_id,sum(amount) cash from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select organization_id,-(ifnull(amount,0.0)) cash from (select agent_org_id ,sum(amount) amount  from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 on a.transaction_id=a1.transaction_id and a.transaction_date>='"
					+ startDate
					+ "'  and a.transaction_date<='"
					+ endDate
					+ "'   and a.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and reason in ('DR_WRONG_RECEIPT_ON_CASH','DR_WRONG_RECEIPT_ON_BD') group by agent_org_id) debit   right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id ) pymntsTbl group by organization_id ";

			stmt=con.createStatement();
			logger.info("QUERY FOR OPENING BAL VAR****-> " + toBeQuery);
			rs=stmt.executeQuery(toBeQuery);
			
			/*pstmt = con.prepareStatement(toBeQuery);
			logger.info("QUERY FOR OPENING BAL VAR****-> " + pstmt);
			rs = pstmt.executeQuery();*/
			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					agtMap.get(agtOrgId).setOpeningBal(
							agtMap.get(agtOrgId).getOpeningBal()
									- rs.getDouble("cashForOpnin"));
				}
			}

			
			/*toBeQuery = "select agent_org_id parent_id ,sum(training_exp_amt) teAmtd from st_lms_agent_weekly_training_exp ex,(select generated_id from st_lms_bo_receipts a,(select receipt_id from st_lms_bo_receipts_trn_mapping map ,(select  transaction_id from st_lms_bo_transaction_master a where a.transaction_date>='"
					+ startDate
					+ "' and a.transaction_date<='"
					+ endDate
					+ "'  and transaction_type='CR_NOTE_CASH') a where a.transaction_id=map.transaction_id) rec where a.receipt_id=rec.receipt_id) gen where ex.credit_note_number=gen.generated_id group by agent_org_id";*/
			
			// Weekly Training EXP For Opening Balance
			toBeQuery = "select parent_id ,sum(teAmtd) teAmtd  from	(select agent_org_id parent_id ,sum(amount) teAmtd from st_lms_bo_credit_note cn inner join st_lms_bo_transaction_master btm on cn.transaction_id = btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and reason  = 'CR_WEEKLY_EXP' group by agent_org_id union all select agent_org_id parent_id ,-sum(amount) teAmtd from st_lms_bo_debit_note cn inner join st_lms_bo_transaction_master btm on cn.transaction_id = btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and reason  = 'DR_WEEKLY_TE_RETURN' group by agent_org_id) a group by parent_id";

			
			
			pstmt = con.prepareStatement(toBeQuery);
			logger.info("Game Wise Data Without Daily Training EXP.****-> "
					+ pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String agtOrgId = rs.getString("parent_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					agtMap.get(agtOrgId).setOpeningBal(
							agtMap.get(agtOrgId).getOpeningBal()
									- rs.getDouble("teAmtd"));
				}
			}
			
			
			if (ReportUtility.isDG) {
				// Get Game Wise Sale Data With Excluding The Daily Training EXP
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					totalSale = 0.0;
					int gameNo = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					toBeQuery = "select parent_id,(sum(sale)-sum(cancel)-sum(teAmt)) sale, (sum(pwt)+sum(pwtDir)) pwt  from (select  0.0  teAmt,parent_id,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.parent_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER') sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_"
							+ gameNo
							+ "  drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER') cancel inner join (select organization_id,parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_"
							+ gameNo
							+ "  drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER') pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master left outer join st_dg_agt_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and organization_type='AGENT' and game_id="
							+ gameNo
							+ "  group by agent_org_id) bb on parent_id=organization_id union all select a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_credit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d   on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('CR_DAILY_EXP') group by d.agent_org_id union all select -a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d   on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('DR_DAILY_TE_RETURN') group by d.agent_org_id) mainTbl group by parent_id";
					/*pstmt = con.prepareStatement(toBeQuery);
					logger
							.info("Game Wise Data Without Daily Training EXP.****-> "
									+ pstmt);
					rs = pstmt.executeQuery();*/
					stmt=con.createStatement();
					logger
					.info("Game Wise Data Without Daily Training EXP.****-> "
							+ toBeQuery);
					rs=stmt.executeQuery(toBeQuery);
					while (rs.next()) {

						String agtOrgId = rs.getString("parent_id");
						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							agtMap.get(agtOrgId).setOpeningBal(
									agtMap.get(agtOrgId).getOpeningBal()
											- rs.getDouble("pwt"));
							if (rsGame.isLast()) {
								totalOpeningBal = totalOpeningBal
										+ agtMap.get(agtOrgId).getOpeningBal();
							}
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
							totalSale = totalSale + rs.getDouble("sale");
						}
					}

					agentBean = agtMap.get("-1111");
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
						gameBean.setDrawSale(totalSale);
					}
				}
				agtMap.get("-1111").setOpeningBal(totalOpeningBal);
			}
			
			if (ReportUtility.isIW) {
				// Get Game Wise Sale Data With Excluding The Daily Training EXP
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					totalSale = 0.0;
					int gameNo = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					toBeQuery = "select parent_id,(sum(sale)-sum(cancel)-sum(teAmt)) sale, (sum(pwt)+sum(pwtDir)) pwt from (select 0.0 teAmt,parent_id,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.parent_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(agent_net_amt) as sale from st_iw_ret_sale irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and is_cancel = 'N' and rtm.transaction_type in('IW_SALE') and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER') sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(agent_net_amt) as cancel from st_iw_ret_sale_refund irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and irs.transaction_date>='"
							+ startDate
							+ "' and irs.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER') cancel inner join (select organization_id,parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(pwt_amt+agt_claim_comm) as pwt from st_iw_ret_pwt  irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and transaction_type in('IW_PWT_AUTO','IW_PWT_PLR','IW_PWT') and irs.transaction_date>='"
							+ startDate
							+ "' and irs.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER') pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master left outer join st_iw_agent_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and organization_type='AGENT' and game_id="
							+ gameNo
							+ "  group by agent_org_id) bb on parent_id=organization_id union all select a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_credit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('CR_DAILY_EXP') group by d.agent_org_id union all select -a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('DR_DAILY_TE_RETURN') group by d.agent_org_id) mainTbl group by parent_id";
					stmt = con.createStatement();
					logger.info("IW Game Wise Data Without Daily Training EXP.****-> " + toBeQuery);
					rs = stmt.executeQuery(toBeQuery);
					while (rs.next()) {
						String agtOrgId = rs.getString("parent_id");
						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							agtMap.get(agtOrgId).setOpeningBal(agtMap.get(agtOrgId).getOpeningBal() - rs.getDouble("pwt"));
							if (rsGame.isLast()) {
								totalOpeningBal = totalOpeningBal + agtMap.get(agtOrgId).getOpeningBal();
							}
							Map<String, CompleteCollectionBean> gameMap = agentBean.getGameBeanMap();
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
							gameBean.setIwSale(rs.getDouble("sale"));
							totalSale = totalSale + rs.getDouble("sale");
						}
					}

					agentBean = agtMap.get("-1111");
					if (agentBean != null) {
						Map<String, CompleteCollectionBean> gameMap = agentBean.getGameBeanMap();
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
						gameBean.setIwSale(totalSale);
					}
				}
				agtMap.get("-1111").setOpeningBal(totalOpeningBal);
			}
			
			if (ReportUtility.isVS) {
				// Get Game Wise Sale Data With Excluding The Daily Training EXP
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					totalSale = 0.0;
					int gameNo = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					toBeQuery = "select parent_id,(sum(sale)-sum(cancel)-sum(teAmt)) sale, (sum(pwt)+sum(pwtDir)) pwt from (select 0.0 teAmt,parent_id,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.parent_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(agent_net_amt) as sale from st_vs_ret_sale irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and is_cancel = 'N' and rtm.transaction_type in('VS_SALE') and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER') sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(agent_net_amt) as cancel from st_vs_ret_sale_refund irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and transaction_type in('VS_SALE_REFUND') and irs.transaction_date>='"
							+ startDate
							+ "' and irs.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER') cancel inner join (select organization_id,parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select irs.retailer_org_id, sum(pwt_amt+agt_claim_comm) as pwt from st_vs_ret_pwt  irs inner join st_lms_retailer_transaction_master rtm on irs.transaction_id=rtm.transaction_id where irs.game_id = "
							+ gameNo
							+ " and transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') and irs.transaction_date>='"
							+ startDate
							+ "' and irs.transaction_date<='"
							+ endDate
							+ "' group by irs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER') pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master left outer join st_vs_agent_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and organization_type='AGENT' and game_id="
							+ gameNo
							+ "  group by agent_org_id) bb on parent_id=organization_id union all select a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_credit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('CR_DAILY_EXP') group by d.agent_org_id union all select -a1.amount teAmtd,d.agent_org_id parent_id,0.0 sale,0.0 cancel,0.0 pwt,0.0 pwdDir from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 inner join st_lms_bo_receipts_trn_mapping b inner join st_lms_bo_receipts c inner join st_lms_agent_daily_training_exp d on a.transaction_id=a1.transaction_id and a1.transaction_id=b.transaction_id and b.receipt_id=c.receipt_id and c.generated_id=d.credit_note_number and a.transaction_date>='"
							+ startDate
							+ "' and a.transaction_date<='"
							+ endDate
							+ "' and d.game_id="
							+ gameNo
							+ " and reason in ('DR_DAILY_TE_RETURN') group by d.agent_org_id) mainTbl group by parent_id";
					stmt = con.createStatement();
					logger.info("VS Game Wise Data Without Daily Training EXP.****-> " + toBeQuery);
					rs = stmt.executeQuery(toBeQuery);
					while (rs.next()) {
						String agtOrgId = rs.getString("parent_id");
						agentBean = agtMap.get(agtOrgId);
						if (agentBean != null) {
							agtMap.get(agtOrgId).setOpeningBal(agtMap.get(agtOrgId).getOpeningBal() - rs.getDouble("pwt"));
							if (rsGame.isLast()) {
								totalOpeningBal = totalOpeningBal + agtMap.get(agtOrgId).getOpeningBal();
							}
							Map<String, CompleteCollectionBean> gameMap = agentBean.getGameBeanMap();
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
							gameBean.setVsSale(rs.getDouble("sale"));
							totalSale = totalSale + rs.getDouble("sale");
						}
					}

					agentBean = agtMap.get("-1111");
					if (agentBean != null) {
						Map<String, CompleteCollectionBean> gameMap = agentBean.getGameBeanMap();
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
						gameBean.setVsSale(totalSale);
					}
				}
				agtMap.get("-1111").setOpeningBal(totalOpeningBal);
			}

			// Debit and Credit Note Against Other Reasons
			toBeQuery = "select agent_org_id organization_id,sum(amount) amount from (select agent_org_id,-sum(amount) amount  from st_lms_bo_transaction_master a inner join st_lms_bo_credit_note a1 on a.transaction_id=a1.transaction_id where a.transaction_date>='"
					+ startDate
					+ "' and a.transaction_date<='"
					+ endDate
					+ "' and a.transaction_type in ('CR_NOTE_CASH','CR_NOTE') and reason in ('OTHERS','CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') group by agent_org_id union all select agent_org_id,(sum(amount)) amount from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 on a.transaction_id=a1.transaction_id where a.transaction_date>='"
					+ startDate
					+ "' and a.transaction_date<='"
					+ endDate
					+ "' and a.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and reason ='OTHERS' group by agent_org_id union all select organization_id agent_org_id,0.0 amount from st_lms_organization_master where organization_type='AGENT') mainTbl group by agent_org_id";

			pstmt = con.prepareStatement(toBeQuery);
			logger.info("Current CR DR Note****-> " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					agtMap.get(agtOrgId).setDebit(rs.getDouble("amount"));
					totalDebit = totalDebit + rs.getDouble("amount");
				}
			}

			agentBean = agtMap.get("-1111");
			if (agentBean != null) {
				agtMap.get("-1111").setDebit(totalDebit);
			}

			if (ReportUtility.isOLA) {
				toBeQuery = "select WID.agtOrgId agtOrgId , wdraw,wdrawRef,depoAmt,depoRefAmt,netAmt from (select om.parent_id agtOrgId, ifnull(sum(wd), 0.0) wdraw from (select wrs.retailer_org_id, agent_net_amt as wd from st_ola_ret_withdrawl wrs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'OLA_WITHDRAWL' and transaction_date>='"
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
						+ "') wdret right outer join (select organization_id, parent_id from st_lms_organization_master where organization_type = 'RETAILER')om on om.organization_id = wdret.retailer_org_id group by om.parent_id)NETGAME where WID.agtOrgId=WIDREF.agtOrgId and WIDREF.agtOrgId =DEP.agtOrgId and DEP.agtOrgId =DEPREF.agtOrgId and DEPREF.agtOrgId=NETGAME.agtOrgId and NETGAME.agtOrgId=WID.agtOrgId";
				
				stmt=con.createStatement();
				logger.info("QUERY FOR OPENING BAL OLA****-> " + toBeQuery);
				rs=stmt.executeQuery(toBeQuery);
				
				/*pstmt = con.prepareStatement(toBeQuery);
				rs = pstmt.executeQuery();*/
				while (rs.next()) {
					String agtOrgId = rs.getString("agtOrgId");
					double dep = rs.getDouble("depoAmt")
							- rs.getDouble("depoRefAmt");
					double wid = rs.getDouble("wdraw")
							- rs.getDouble("wdrawRef");
					if (agtMap.get(agtOrgId) != null) {
						agtMap.get(agtOrgId).setDebit(
								agtMap.get(agtOrgId).getDebit() + dep - wid);
						totalDebit = totalDebit
								+ agtMap.get(agtOrgId).getDebit();
					}
				}

				agentBean = agtMap.get("-1111");
				if (agentBean != null) {
					agtMap.get("-1111").setDebit(totalDebit);
				}
			}
			// CHANGE DATES FOR CURRENT DAY
			startDate = new Timestamp(stDate.getTime());
			endDate = new Timestamp(startDate.getTime() + oneDay - 1000);


			/*toBeQuery = "select agent_org_id parent_id ,sum(training_exp_amt) teAmtd from st_lms_agent_weekly_training_exp ex,(select generated_id from st_lms_bo_receipts a,(select receipt_id from st_lms_bo_receipts_trn_mapping map ,(select  transaction_id from st_lms_bo_transaction_master a where a.transaction_date>='"
					+ startDate
					+ "' and a.transaction_date<='"
					+ endDate
					+ "'  and transaction_type='CR_NOTE_CASH') a where a.transaction_id=map.transaction_id) rec where a.receipt_id=rec.receipt_id) gen where ex.credit_note_number=gen.generated_id group by agent_org_id";*/
			
			
			// Weekly Training EXP
			
			toBeQuery = "select parent_id ,sum(teAmtd) teAmtd  from	(select agent_org_id parent_id ,sum(amount) teAmtd from st_lms_bo_credit_note cn inner join st_lms_bo_transaction_master btm on cn.transaction_id = btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and reason  = 'CR_WEEKLY_EXP' group by agent_org_id union all select agent_org_id parent_id ,-sum(amount) teAmtd from st_lms_bo_debit_note cn inner join st_lms_bo_transaction_master btm on cn.transaction_id = btm.transaction_id and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' and reason  = 'DR_WEEKLY_TE_RETURN' group by agent_org_id) a group by parent_id";

			

			pstmt = con.prepareStatement(toBeQuery);
			logger.info("Game Wise Data Without Daily Training EXP.****-> "
					+ pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String agtOrgId = rs.getString("parent_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					agtMap.get(agtOrgId).setCredit(rs.getDouble("teAmtd"));
					totalCredit = totalCredit + rs.getDouble("teAmtd");
				}
			}

			agentBean = agtMap.get("-1111");
			if (agentBean != null) {
				agtMap.get("-1111").setCredit(totalCredit);
			}
			// Current Day's/CASH/BD+CHQ-CHQRET
			toBeQuery = "select organization_id,sum(bank) bank,sum(cash) cash from (select  organization_id,sum(bank)+sum(chq) -sum(chkRet) bank,sum(cash) cash from (select agent_org_id organization_id,0.0 bank, 0.0 chq, 0.0 chkRet,sum(amount) cash from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select agent_org_id organization_id,0.0 bank,sum(cheque_amt) chq,0.0 chkRet,0.0 cash from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select agent_org_id organization_id,0.0 bank, 0.0 chq,sum(cheque_amt) chkRet,0.0 cash from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id union all select organization_id,ifnull(bank,0.0) bank, 0.0 chq,0.0 chkRet,0.0 cash from (select agent_org_id ,sum(amount) bank from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='"
					+ startDate
					+ "'  and transaction_date<='"
					+ endDate
					+ "'  group by agent_org_id) bank right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id) pymntsTbl group by organization_id   union all select agent_org_id organization_id ,0.0 bank ,-sum(amount) cash  from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 on a.transaction_id=a1.transaction_id and a.transaction_date>='"+startDate+"'  and a.transaction_date<='"+endDate+"'   and a.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and reason ='DR_WRONG_RECEIPT_ON_CASH' group by agent_org_id union all select agent_org_id organization_id ,-sum(amount) bank  , 0.0 cash from st_lms_bo_transaction_master a inner join st_lms_bo_debit_note a1 on a.transaction_id=a1.transaction_id and a.transaction_date>='"+startDate+"'  and a.transaction_date<='"+endDate+"'   and a.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and reason ='DR_WRONG_RECEIPT_ON_BD' group by agent_org_id) outerQuery  group by organization_id";

			stmt=con.createStatement();
			logger.info("Current Day's TX's.****-> " + toBeQuery);
			rs=stmt.executeQuery(toBeQuery);
			
			/*pstmt = con.prepareStatement(toBeQuery);
			logger.info("Current Day's TX's.****-> " + pstmt);
			rs = pstmt.executeQuery();*/
			while (rs.next()) {
				String agtOrgId = rs.getString("organization_id");
				agentBean = agtMap.get(agtOrgId);
				if (agentBean != null) {
					agtMap.get(agtOrgId).setCash(
							agtMap.get(agtOrgId).getCash()
									+ rs.getDouble("cash"));
					agtMap.get(agtOrgId).setBankDep(
							agtMap.get(agtOrgId).getBankDep()
									+ rs.getDouble("bank"));

					totalCash = totalCash + rs.getDouble("cash");
					totalBank = totalBank + rs.getDouble("bank");
				}
			}

			agentBean = agtMap.get("-1111");
			if (agentBean != null) {
				agtMap.get("-1111").setCash(totalCash);
				agtMap.get("-1111").setBankDep(totalBank);
			}
			
			if (ReportUtility.isIW) {
				// Current Day's PWT
				String gameQry = ReportUtility.getIWGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					toBeQuery = "select organization_id, sum(pwt) pwt from (select parent_id organization_id ,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id, sum(pwt_amt+agt_claim_comm) pwt from st_iw_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id and (drs.game_id = "
							+ gameNo
							+ " and drs.transaction_date>='"
							+ startDate
							+ "' and drs.transaction_date<='"
							+ endDate
							+ "') and transaction_type in('IW_PWT_AUTO','IW_PWT_PLR','IW_PWT') group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id union all select organization_id,sum(pwt_amt+agt_claim_comm) pwt from st_lms_organization_master, st_iw_agent_direct_plr_pwt where organization_id=agent_org_id and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " and organization_type='AGENT' group by agent_org_id) pwt group by organization_id";

					stmt = con.createStatement();
					logger.info("Current Day's PWT/CASH/BD+CHQ-CHQREF****-> "
							+ toBeQuery);
					rs = stmt.executeQuery(toBeQuery);

					String agtOrgId;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						if (agtMap.get(agtOrgId) != null) {
							agtMap.get(agtOrgId).setIwPwt(agtMap.get(agtOrgId).getIwPwt() + rs.getDouble("pwt"));
							totalPwt = totalPwt + rs.getDouble("pwt");
						}
					}
					agtMap.get("-1111").setIwPwt(totalPwt);
				}
			}
			
			if (ReportUtility.isVS) {
				// Current Day's PWT
				String gameQry = ReportUtility.getVSGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					toBeQuery = "select organization_id, sum(pwt) pwt from (select parent_id organization_id ,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id, sum(pwt_amt+agt_claim_comm) pwt from st_vs_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id and (drs.game_id = "
							+ gameNo
							+ " and drs.transaction_date>='"
							+ startDate
							+ "' and drs.transaction_date<='"
							+ endDate
							+ "') and transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id union all select organization_id,sum(pwt_amt+agt_claim_comm) pwt from st_lms_organization_master, st_vs_agent_direct_plr_pwt where organization_id=agent_org_id and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " and organization_type='AGENT' group by agent_org_id) pwt group by organization_id";

					stmt = con.createStatement();
					logger.info("Current Day's PWT/CASH/BD+CHQ-CHQREF****-> "
							+ toBeQuery);
					rs = stmt.executeQuery(toBeQuery);

					String agtOrgId;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						if (agtMap.get(agtOrgId) != null) {
							agtMap.get(agtOrgId).setVsPwt(agtMap.get(agtOrgId).getVsPwt() + rs.getDouble("pwt"));
							totalPwt = totalPwt + rs.getDouble("pwt");
						}
					}
					agtMap.get("-1111").setVsPwt(totalPwt);
				}
			}

			if (ReportUtility.isDG) {
				// Current Day's PWT
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					/*toBeQuery = "select parent_id organization_id,pwt+ifnull(pwtDir,0.0) pwt from (select parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om , (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_"
							+ gameNo
							+ "  drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  group by drs.retailer_org_id) pwt where om.organization_id=retailer_org_id and om.organization_type='RETAILER' group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master , st_dg_agt_direct_plr_pwt where organization_id=agent_org_id and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and game_id="
							+ gameNo
							+ " and organization_type='AGENT' group by agent_org_id) bb on parent_id=organization_id";*/
					
					toBeQuery = "select organization_id , sum(pwt) pwt from (select parent_id organization_id ,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join  (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) pwt  from st_dg_ret_pwt_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id and (transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "') and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id union all select organization_id,sum(pwt_amt+agt_claim_comm) pwt from st_lms_organization_master , st_dg_agt_direct_plr_pwt where organization_id=agent_org_id and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "'  and game_id="
							+ gameNo
							+ " and organization_type='AGENT' group by agent_org_id) pwt group by organization_id";

					stmt=con.createStatement();
					logger.info("Current Day's PWT/CASH/BD+CHQ-CHQREF****-> " + toBeQuery);
					rs=stmt.executeQuery(toBeQuery);
					
					/*pstmt = con.prepareStatement(toBeQuery);
					logger.info("Current Day's PWT/CASH/BD+CHQ-CHQREF****-> "
							+ pstmt);
					rs = pstmt.executeQuery();*/
					String agtOrgId;
					while (rs.next()) {
						agtOrgId = rs.getString("organization_id");
						if (agtMap.get(agtOrgId) != null) {
							agtMap.get(agtOrgId).setDgPwt(
									agtMap.get(agtOrgId).getDgPwt()
											+ rs.getDouble("pwt"));

							totalPwt = totalPwt + rs.getDouble("pwt");
						}
					}
					agtMap.get("-1111").setDgPwt(totalPwt);
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
			String gameQry = "select game_name,'DG' as game_type from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where game_status='SALE_CLOSE') union all select game_name,'SE' as game_type from st_se_game_master union all select category_code,'CS' as game_type from st_cs_product_category_master where status = 'ACTIVE' union all select wallet_name,'OLA' as game_type from st_ola_wallet_master order by game_type";
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

}