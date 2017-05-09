package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

public class LiveGameReportHelper implements ILiveGameReportHelper{
	static Log logger = LogFactory.getLog(LiveGameReportHelper.class);

	public static void main(String[] args) {
		LiveGameReportHelper helper = new LiveGameReportHelper();
		helper.drawReport(4, new Timestamp(
				new Date().getTime() - 24 * 60 * 1000), new Timestamp(
				new Date().getTime()));
		logger.debug("******************Scratch Game*************");
		helper.scratchReport(4, new Timestamp(
				new Date().getTime() - 24 * 60 * 1000), new Timestamp(
				new Date().getTime()));
	}

	public Map<String, String> consolidateLiveGameReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate, boolean isNoCash) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		String gameQry = null;
		String retOrgQry = null;
		String saleQry = null;
		String cancelQry = null;
		String pwtQry = null;
		String cashQry = null;
		String dirPlrPwtQry = null;
		String dirPlrPwtAmt = null;
		Map<Integer, String> orgMap = new LinkedHashMap<Integer, String>();
		Map<String, String> reportMap = new LinkedHashMap<String, String>();
		Map<Integer, Double> saleDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cancelDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> saleScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cashRetMap = new HashMap<Integer, Double>();
		try {
			con = DBConnect.getConnection();
			// Check if The Report Is AgentWise or RetailerWise
			if(agtOrgId==1)
				retOrgQry = "select "+QueryManager.getOrgCodeQuery()+" , organization_id from st_lms_organization_master where organization_type='RETAILER' order by "+QueryManager.getAppendOrgOrder();
			else
				retOrgQry = "select "+QueryManager.getOrgCodeQuery()+" , organization_id from st_lms_organization_master where parent_id="+agtOrgId+" order by "+QueryManager.getAppendOrgOrder();
				pstmt = con.prepareStatement(retOrgQry);
				rsRetOrg = pstmt.executeQuery();
				while (rsRetOrg.next()) {
					int orgId = rsRetOrg.getInt("organization_id");
					orgMap.put(orgId, rsRetOrg.getString("orgCode"));
					saleDrawRetMap.put(orgId, 0.0);
					cancelDrawRetMap.put(orgId, 0.0);
				pwtDrawRetMap.put(orgId, 0.0);
				saleScratchRetMap.put(orgId, 0.0);
				pwtScratchRetMap.put(orgId, 0.0);
				cashRetMap.put(orgId, 0.0);
			}

			// Game Master Query for Draw Game
			gameQry = "select game_id from st_dg_game_master";
			PreparedStatement gamePstmt = con.prepareStatement(gameQry);
			rsGame = gamePstmt.executeQuery();

			while (rsGame.next()) {
				int gameId = rsGame.getInt("game_id");
				
				if(agtOrgId==1)	{
					saleQry="select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') group by drs.retailer_org_id";
					cancelQry="select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') group by drs.retailer_org_id";
					pwtQry="select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') group by drs.retailer_org_id";
					dirPlrPwtQry="select ifnull(sum(pwt_amt + agt_claim_comm),0) as netAgtDirPlrPwt from st_dg_agt_direct_plr_pwt where agent_org_id = -111;";
					cashQry="select retailer_org_id,sum(amount) cash from st_lms_agent_cash_transaction where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CASH' and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') group by retailer_org_id";
				}
				else{
					saleQry = "select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="+agtOrgId+")) group by drs.retailer_org_id";
					cancelQry = "select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="+agtOrgId+")) group by drs.retailer_org_id";
					pwtQry = "select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_"+gameId+" drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="+agtOrgId+")) group by drs.retailer_org_id";
					dirPlrPwtQry = "select ifnull(sum(pwt_amt + agt_claim_comm),0) as netAgtDirPlrPwt from st_dg_agt_direct_plr_pwt where agent_org_id ="+agtOrgId+" and date(transaction_date) >= '"+startDate+"' and date(transaction_date) <'"+endDate+"'";
					cashQry = "select retailer_org_id,sum(amount) cash from st_lms_agent_cash_transaction where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CASH' and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') and agent_org_id="+agtOrgId+" group by retailer_org_id";		
				}
				// Calculate Sale
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double salValue = saleDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					saleDrawRetMap.put(rs.getInt("retailer_org_id"), salValue
							+ rs.getDouble("sale"));
				}
				// Calculate Cancel
				pstmt = con.prepareStatement(cancelQry);
				logger.debug("***Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double canValue = cancelDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					cancelDrawRetMap.put(rs.getInt("retailer_org_id"), canValue
							+ rs.getDouble("cancel"));
				}
				// Calculate Pwt
				pstmt = con.prepareStatement(pwtQry);
				logger.debug("***Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double pwtValue = pwtDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					pwtDrawRetMap.put(rs.getInt("retailer_org_id"), pwtValue
							+ rs.getDouble("pwt"));
				}

			}
			// Calculate DirplrPwt
			pstmt = con.prepareStatement(dirPlrPwtQry);
			logger.debug("****dir plr plwt qry*** " + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dirPlrPwtAmt = rs.getString("netAgtDirPlrPwt");
			}

			logger.debug("***Sale Draw Map***" + saleDrawRetMap);
			logger.debug("***Cancel Draw Map***" + cancelDrawRetMap);
			logger.debug("***Pwt Draw Map***" + pwtDrawRetMap);

			// Scratch Game
			saleQry=null;
			pwtQry=null;
			dirPlrPwtQry=null;

			if(agtOrgId==1)	{
			   		saleQry="select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id,gmti.date, (sum(gmti.sold_tickets)*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER' and gmti.date>='"+startDate+"' and gmti.date<='"+endDate+"' and gmti.game_id=gm.game_id group by gmti.current_owner_id,gmti.game_id) aa group by current_owner_id";
					pwtQry="select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and transaction_type='PWT') group by retailer_org_id";
			}
			else			{
					saleQry = "select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id,gmti.date, (sum(gmti.sold_tickets)*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER' and gmti.current_owner_id in(select organization_id from st_lms_organization_master where parent_id="+agtOrgId+") and gmti.date>='"+startDate+"' and gmti.date<='"+endDate+"' and gmti.game_id=gm.game_id group by gmti.current_owner_id,gmti.game_id) aa group by current_owner_id";
					pwtQry = "select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="+agtOrgId+")) group by retailer_org_id";
			}
						
				// Calculate Sale
			pstmt = con.prepareStatement(saleQry);	
			logger.debug("***Sale Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saleScratchRetMap.put(rs.getInt("current_owner_id"), rs
						.getDouble("sale"));
			}
			// Calculate Pwt
			pstmt = con.prepareStatement(pwtQry);
			logger.debug("***PWT Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pwtScratchRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("pwt"));
			}

			logger.debug("***Sale Scratch Map***" + saleScratchRetMap);
			logger.debug("***Pwt Scratch Map***" + pwtScratchRetMap);

			// Cash Payment Calculations
			pstmt = con.prepareStatement(cashQry);
			logger.debug("***Cash Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cashRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("cash"));
			}
			// Result Map
			Iterator<Map.Entry<Integer, String>> iter = orgMap.entrySet()
					.iterator();
			double saleDrawAmtTot = 0.0;
			double cancelDrawAmtTot = 0.0;
			double pwtDrawAmtTot = 0.0;
			double saleScratchAmtTot = 0.0;
			double pwtScratchAmtTot = 0.0;
			double cashAmtTot = 0.0;
			double cashInHandTot = 0.0;

			while (iter.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) iter
						.next();
				int key = pair.getKey(); // orgId
				StringBuilder amtVal = new StringBuilder("");
				String orgName = pair.getValue();
				double saleDrawAmt = saleDrawRetMap.get(key);
				double cancelDrawAmt = cancelDrawRetMap.get(key);
				double pwtDrawAmt = pwtDrawRetMap.get(key);
				double saleScratchAmt = saleScratchRetMap.get(key);
				double pwtScratchAmt = pwtScratchRetMap.get(key);

				double cashAmt = cashRetMap.get(key);

				double cashInHand = saleDrawAmt + saleScratchAmt
						- (cancelDrawAmt + pwtDrawAmt + pwtScratchAmt);
				amtVal.append(saleDrawAmt + "," + cancelDrawAmt + ","
						+ pwtDrawAmt + "," + saleScratchAmt + ","
						+ pwtScratchAmt + ",");
				if (isNoCash) {
					amtVal.append(cashInHand);
				} else {
					cashInHand = cashInHand - cashAmt;
					amtVal.append(cashAmt + "," + cashInHand);
					cashAmtTot += cashAmt;
				}
				reportMap.put(orgName, amtVal.toString());

				saleDrawAmtTot += saleDrawAmt;
				cancelDrawAmtTot += cancelDrawAmt;
				pwtDrawAmtTot += pwtDrawAmt;
				saleScratchAmtTot += saleScratchAmt;
				pwtScratchAmtTot += pwtScratchAmt;
				cashInHandTot += cashInHand;
			}
			reportMap
					.put("Total", saleDrawAmtTot + "," + cancelDrawAmtTot + ","
							+ pwtDrawAmtTot + "," + saleScratchAmtTot + ","
							+ pwtScratchAmtTot + "," + cashAmtTot + ","
							+ cashInHandTot);
			if (isNoCash) {
				reportMap.put("Total", saleDrawAmtTot + "," + cancelDrawAmtTot
						+ "," + pwtDrawAmtTot + "," + saleScratchAmtTot + ","
						+ pwtScratchAmtTot + "," + cashInHandTot);
			}
			reportMap.put("dirPlrPwt", dirPlrPwtAmt);
			logger.debug("***report Map***" + reportMap);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportMap;
	}

	public TreeMap<String, String> drawReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		String gameQry = null;
		String retOrgQry = null;
		String saleQry = null;
		String cancelQry = null;
		String pwtQry = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		TreeMap<String, String> reportMap = new TreeMap<String, String>();
		Map<Integer, Double> saleRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cancelRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtRetMap = new HashMap<Integer, Double>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			retOrgQry = "select name,organization_id from st_lms_organization_master where parent_id=?";
			pstmt = con.prepareStatement(retOrgQry);
			pstmt.setInt(1, agtOrgId);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				orgMap.put(rsRetOrg.getInt("organization_id"), rsRetOrg
						.getString("name"));
				saleRetMap.put(rsRetOrg.getInt("organization_id"), 0.0);
				cancelRetMap.put(rsRetOrg.getInt("organization_id"), 0.0);
				pwtRetMap.put(rsRetOrg.getInt("organization_id"), 0.0);
			}

			// Game Master Query
			gameQry = "select game_id from st_dg_game_master";
			PreparedStatement gamePstmt = con.prepareStatement(gameQry);
			rsGame = gamePstmt.executeQuery();

			while (rsGame.next()) {
				// Calculate Sale
				int gameId = rsGame.getInt("game_id");
				saleQry = "select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_? drs,"
						+ "(select retailer_org_id,transaction_id,transaction_type from "
						+ "st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE')"
						+ " and transaction_date>=? and transaction_date<=?"
						+ " and retailer_org_id in (select organization_id from st_lms_organization_master "
						+ "where parent_id=?)) retTran where retTran.transaction_id=drs.transaction_id "
						+ "group by drs.retailer_org_id";
				pstmt = con.prepareStatement(saleQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double salValue = saleRetMap.get(rs
							.getInt("retailer_org_id"));
					saleRetMap.put(rs.getInt("retailer_org_id"), salValue
							+ rs.getDouble("sale"));
				}

				// Calculate Cancel

				cancelQry = "select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_? drs,"
						+ "(select retailer_org_id,transaction_id,transaction_type from "
						+ "st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
						+ "and transaction_date>=? and transaction_date<=?"
						+ " and retailer_org_id in (select organization_id "
						+ "from st_lms_organization_master where parent_id=?)) retTran "
						+ "where retTran.transaction_id=drs.transaction_id group by drs.retailer_org_id";
				pstmt = con.prepareStatement(cancelQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double canValue = cancelRetMap.get(rs
							.getInt("retailer_org_id"));
					cancelRetMap.put(rs.getInt("retailer_org_id"), canValue
							+ rs.getDouble("cancel"));
				}

				// Calculate Pwt

				pwtQry = "select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_? drs,(select retailer_org_id,transaction_id,transaction_type from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>=? and transaction_date<=? and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=?)) retTran where retTran.transaction_id=drs.transaction_id group by drs.retailer_org_id";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double pwtValue = pwtRetMap.get(rs
							.getInt("retailer_org_id"));
					pwtRetMap.put(rs.getInt("retailer_org_id"), pwtValue
							+ rs.getDouble("pwt"));
				}

			}

			logger.debug("***Sale Map***" + saleRetMap);
			logger.debug("***Cancel Map***" + cancelRetMap);
			logger.debug("***Pwt Map***" + pwtRetMap);

			Iterator<Map.Entry<Integer, String>> iter = orgMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) iter
						.next();
				int key = pair.getKey();
				StringBuilder amtVal = new StringBuilder("");
				String orgName = pair.getValue();
				double saleAmt = saleRetMap.get(key);
				double cancelAmt = cancelRetMap.get(key);
				double pwtAmt = pwtRetMap.get(key);
				double cashInHand = saleAmt - cancelAmt - pwtAmt;

				amtVal.append(saleAmt + "," + cancelAmt + "," + pwtAmt + ","
						+ cashInHand);
				reportMap.put(orgName, amtVal.toString());
			}
			logger.debug("***report Map***" + reportMap);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportMap;
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
			logger.debug(pstmt);
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

	public void retDrawReport() {

	}

	public TreeMap<String, String> retStatusReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		String gameQry = null;
		String retOrgQry = null;
		String saleQry = null;
		String cancelQry = null;
		String pwtQry = null;
		String cashQry = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		TreeMap<String, String> reportMap = new TreeMap<String, String>();
		Map<Integer, Double> saleDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cancelDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> saleScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cashRetMap = new HashMap<Integer, Double>();
		try {
			con = DBConnect.getConnection();
			retOrgQry = "select name,organization_id from st_lms_organization_master where parent_id=? and organization_type=?";
			pstmt = con.prepareStatement(retOrgQry);
			pstmt.setInt(1, agtOrgId);
			pstmt.setString(2, "RETAILER");
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				orgMap.put(rsRetOrg.getInt("organization_id"), rsRetOrg
						.getString("name"));
			}

			// Game Master Query for Draw Game
			gameQry = "select game_id from st_dg_game_master";
			PreparedStatement gamePstmt = con.prepareStatement(gameQry);
			rsGame = gamePstmt.executeQuery();

			while (rsGame.next()) {
				// Calculate Sale
				int gameId = rsGame.getInt("game_id");
				saleQry = "select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_? drs "
						+ "where transaction_id in (select transaction_id from "
						+ "st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE')"
						+ " and transaction_date>=? and transaction_date<=?"
						+ " and retailer_org_id in (select organization_id from st_lms_organization_master "
						+ "where parent_id=?)) "
						+ "group by drs.retailer_org_id";
				pstmt = con.prepareStatement(saleQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double salValue = saleDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					saleDrawRetMap.put(rs.getInt("retailer_org_id"), salValue
							+ rs.getDouble("sale"));
				}

				// Calculate Cancel

				cancelQry = "select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_? drs "
						+ "where transaction_id in (select transaction_id from "
						+ "st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
						+ "and transaction_date>=? and transaction_date<=?"
						+ " and retailer_org_id in (select organization_id "
						+ "from st_lms_organization_master where parent_id=?)) "
						+ "group by drs.retailer_org_id";
				pstmt = con.prepareStatement(cancelQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double canValue = cancelDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					cancelDrawRetMap.put(rs.getInt("retailer_org_id"), canValue
							+ rs.getDouble("cancel"));
				}

				// Calculate Pwt

				pwtQry = "select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>=? and transaction_date<=? and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=?)) group by drs.retailer_org_id";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setInt(1, gameId);
				pstmt.setTimestamp(2, startDate);
				pstmt.setTimestamp(3, endDate);
				pstmt.setInt(4, agtOrgId);

				logger.debug("***Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					double pwtValue = pwtDrawRetMap.get(rs
							.getInt("retailer_org_id"));
					pwtDrawRetMap.put(rs.getInt("retailer_org_id"), pwtValue
							+ rs.getDouble("pwt"));
				}

			}

			logger.debug("***Sale Draw Map***" + saleDrawRetMap);
			logger.debug("***Cancel Draw Map***" + cancelDrawRetMap);
			logger.debug("***Pwt Draw Map***" + pwtDrawRetMap);

			// Scratch Game

			// Calculate Sale
			saleQry = "select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id,gmti.date, (sum(gmti.sold_tickets)*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER' and gmti.current_owner_id in(select organization_id from st_lms_organization_master where parent_id=?) and gmti.date>=? and gmti.date<=? and gmti.game_id=gm.game_id group by gmti.current_owner_id,gmti.game_id) aa group by current_owner_id";
			pstmt = con.prepareStatement(saleQry);
			pstmt.setInt(1, agtOrgId);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);

			logger.debug("***Sale Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saleScratchRetMap.put(rs.getInt("current_owner_id"), rs
						.getDouble("sale"));
			}

			// Calculate Pwt
			pwtQry = "select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=?)) group by retailer_org_id";
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.debug("***PWT Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pwtScratchRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("pwt"));
			}

			logger.debug("***Sale Scratch Map***" + saleScratchRetMap);
			logger.debug("***Pwt Scratch Map***" + pwtScratchRetMap);

			// Cash Payment Calculations
			cashQry = "select retailer_org_id,sum(amount) cash from st_lms_agent_cash_transaction where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CASH' and transaction_date>=? and transaction_date<=?) and agent_org_id=? group by retailer_org_id";
			pstmt = con.prepareStatement(cashQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.debug("***Cash Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cashRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("cash"));
			}
			// Cheque Payment Calculations
			cashQry = "select retailer_org_id,sum(cheque_amt) cheque from st_lms_agent_sale_chq where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CHEQUE' and transaction_date>=? and transaction_date<=?) and agent_org_id=? group by retailer_org_id";
			pstmt = con.prepareStatement(cashQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.debug("***Cash Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cashRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("cheque")
						+ cashRetMap.get(rs.getInt("retailer_org_id")));
			}
			// Result Map
			Iterator<Map.Entry<Integer, String>> iter = orgMap.entrySet()
					.iterator();
			double saleDrawAmtTot = 0.0;
			double cancelDrawAmtTot = 0.0;
			double pwtDrawAmtTot = 0.0;
			double saleScratchAmtTot = 0.0;
			double pwtScratchAmtTot = 0.0;
			double cashAmtTot = 0.0;
			double cashInHandTot = 0.0;

			while (iter.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) iter
						.next();
				int key = pair.getKey(); // orgId
				StringBuilder amtVal = new StringBuilder("");
				String orgName = pair.getValue();
				double saleDrawAmt = saleDrawRetMap.get(key);
				double cancelDrawAmt = cancelDrawRetMap.get(key);
				double pwtDrawAmt = pwtDrawRetMap.get(key);
				double saleScratchAmt = saleScratchRetMap.get(key);
				double pwtScratchAmt = pwtScratchRetMap.get(key);
				double cashAmt = cashRetMap.get(key);

				double cashInHand = saleDrawAmt
						+ saleScratchAmt
						- (cancelDrawAmt + pwtDrawAmt + pwtScratchAmt + cashAmt);

				amtVal.append(saleDrawAmt + "," + cancelDrawAmt + ","
						+ pwtDrawAmt + "," + saleScratchAmt + ","
						+ pwtScratchAmt + "," + cashAmt + "," + cashInHand);
				reportMap.put(orgName, amtVal.toString());

				saleDrawAmtTot += saleDrawAmt;
				cancelDrawAmtTot += cancelDrawAmt;
				pwtDrawAmtTot += pwtDrawAmt;
				saleScratchAmtTot += saleScratchAmt;
				pwtScratchAmtTot += pwtScratchAmt;
				cashAmtTot += cashAmt;
				cashInHandTot += cashInHand;
			}
			reportMap
					.put("Total", saleDrawAmtTot + "," + cancelDrawAmtTot + ","
							+ pwtDrawAmtTot + "," + saleScratchAmtTot + ","
							+ pwtScratchAmtTot + "," + cashAmtTot + ","
							+ cashInHandTot);
			logger.debug("***report Map***" + reportMap);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportMap;
	}

	public TreeMap<String, String> scratchReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rsRetOrg = null;
		ResultSet rs = null;
		String retOrgQry = null;
		String saleQry = null;
		String pwtQry = null;
		Map<Integer, String> orgMap = new HashMap<Integer, String>();
		TreeMap<String, String> reportMap = new TreeMap<String, String>();
		Map<Integer, Double> saleRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtRetMap = new HashMap<Integer, Double>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			retOrgQry = "select name,organization_id from st_lms_organization_master where parent_id=?";
			pstmt = con.prepareStatement(retOrgQry);
			pstmt.setInt(1, agtOrgId);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				orgMap.put(rsRetOrg.getInt("organization_id"), rsRetOrg
						.getString("name"));
				saleRetMap.put(rsRetOrg.getInt("organization_id"), 0.0);
				pwtRetMap.put(rsRetOrg.getInt("organization_id"), 0.0);
			}

			// Calculate Sale
			saleQry = "select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id,gmti.date, (sum(gmti.sold_tickets)*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER' and gmti.current_owner_id in(select organization_id from st_lms_organization_master where parent_id=?) and gmti.date>=? and gmti.date<=? and gmti.game_id=gm.game_id group by gmti.current_owner_id,gmti.game_id) aa group by current_owner_id";
			pstmt = con.prepareStatement(saleQry);
			pstmt.setInt(1, agtOrgId);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);

			logger.debug("***Sale Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saleRetMap.put(rs.getInt("current_owner_id"), rs
						.getDouble("sale"));
			}

			// Calculate Pwt
			pwtQry = "select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=?)) group by retailer_org_id";
			pstmt = con.prepareStatement(pwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);

			logger.debug("***PWT Query*** \n" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pwtRetMap
						.put(rs.getInt("retailer_org_id"), rs.getDouble("pwt"));
			}

			logger.debug("***Sale Map***" + saleRetMap);
			logger.debug("***Pwt Map***" + pwtRetMap);

			Iterator<Map.Entry<Integer, String>> iter = orgMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) iter
						.next();
				int key = pair.getKey();
				StringBuilder amtVal = new StringBuilder("");
				String orgName = pair.getValue();
				double saleAmt = saleRetMap.get(key);
				double pwtAmt = pwtRetMap.get(key);
				double cashInHand = saleAmt - pwtAmt;

				amtVal.append(saleAmt + "," + pwtAmt + "," + cashInHand);
				reportMap.put(orgName, amtVal.toString());
			}
			logger.debug("***report Map***" + reportMap);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportMap;
	}
}
