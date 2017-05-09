package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLERetailerReportsControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaRetailerReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class LiveGameReportHelperSP implements ILiveGameReportHelper {
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
		Statement stmt = null;
		CallableStatement cstmt = null;
		ResultSet rsRetOrg = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		String gameQry = null;
		String retOrgQry = null;
		String cashQry = null;
		String dirPlrPwtQry = null;
		String dirPlrPwtAmt = "0.0";
		Map<Integer, String> orgMap = new LinkedHashMap<Integer, String>();
		Map<String, String> reportMap = new LinkedHashMap<String, String>();
		Map<Integer, Double> saleDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cancelDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtDrawRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> saleScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> pwtScratchRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cashRetMap = new HashMap<Integer, Double>();

		Map<Integer, Double> saleCSRetMap = new HashMap<Integer, Double>();
		Map<Integer, Double> cancelCSRetMap = new HashMap<Integer, Double>();
		try {
			con = DBConnect.getConnection();

			// Check if The Report Is AgentWise or RetailerWise
			if(agtOrgId==1)
				retOrgQry = "select "+QueryManager.getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='RETAILER' order by  "+QueryManager.getAppendOrgOrder();
			else
				retOrgQry = "select "+QueryManager.getOrgCodeQuery()+",organization_id from st_lms_organization_master where parent_id="+agtOrgId+" order by  "+QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(retOrgQry);
			//pstmt.setInt(1, agtOrgId);
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

				cstmt = con.prepareCall("{call drawLiveGameReportRetailerWise(?,?,?,?)}");
				cstmt.setInt(1, gameId);
				cstmt.setTimestamp(2, startDate);
				cstmt.setTimestamp(3, endDate);
				cstmt.setInt(4, agtOrgId);

				rs = cstmt.executeQuery();

				while (rs.next()) {
					double salValue = saleDrawRetMap.get(rs
							.getInt("organization_id"));
					saleDrawRetMap.put(rs.getInt("organization_id"), salValue
							+ rs.getDouble("sale"));
					double canValue = cancelDrawRetMap.get(rs
							.getInt("organization_id"));
					cancelDrawRetMap.put(rs.getInt("organization_id"), canValue
							+ rs.getDouble("cancel"));
					double pwtValue = pwtDrawRetMap.get(rs
							.getInt("organization_id"));
					pwtDrawRetMap.put(rs.getInt("organization_id"), pwtValue
							+ rs.getDouble("pwt"));
				}
			}
			cstmt = con.prepareCall("{call liveConsolidatedDirectPlrPwt(?,?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				dirPlrPwtAmt = rs.getString("netAgtDirPlrPwt");
			}

			cstmt = con.prepareCall("{call scratchLiveGameReportRetailerWise(?,?,?)}");
			cstmt.setInt(1, agtOrgId);
			cstmt.setTimestamp(2, startDate);
			cstmt.setTimestamp(3, endDate);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				saleScratchRetMap.put(rs.getInt("organization_id"), rs
						.getDouble("sale"));
				pwtScratchRetMap.put(rs.getInt("organization_id"), rs
						.getDouble("pwt"));
			}

			cstmt = con.prepareCall("{call cashLiveGameReportRetailerWise(?,?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.setInt(3, agtOrgId);

			rs = cstmt.executeQuery();

			while (rs.next()) {
				cashRetMap.put(rs.getInt("retailer_org_id"), rs
						.getDouble("cash"));
			}

			/*	OLA Deposit/Withdraw Start	*/
			Map<Integer, OlaOrgReportResponseBean> olaMap = null;
			if(ReportUtility.isOLA) {
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				requestBean.setOrgId((agtOrgId==1)?0:agtOrgId);
				requestBean.setWalletId(0);
				requestBean.setFromDate(dateFormat.format(startDate));
				requestBean.setToDate(dateFormat.format(endDate));
				olaMap = OlaRetailerReportControllerImpl.fetchDepositWithdrawlMultipleRetailer(requestBean, con);
			}
			/*	OLA Deposit/Withdraw End	*/

			/*	CS Sale Start	*/
			if(ReportUtility.isCS) {
				String catQry = "SELECT category_id FROM st_cs_product_category_master WHERE STATUS='ACTIVE';";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					cstmt = con.prepareCall("{CALL csCollectionRetailerWisePerCategory(?,?,?,?)}");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, (agtOrgId==1)?0:agtOrgId);
					rs = cstmt.executeQuery();
					int retOrgId = 0;
					while (rs.next()) {
						retOrgId = rs.getInt("organization_id");
						double preSaleAmt = (saleCSRetMap.get(retOrgId)==null)?0.0:saleCSRetMap.get(retOrgId);
						double preCancelAmt = (cancelCSRetMap.get(retOrgId)==null)?0.0:cancelCSRetMap.get(retOrgId);
						saleCSRetMap.put(retOrgId, preSaleAmt+rs.getDouble("sale"));
						cancelCSRetMap.put(retOrgId, preCancelAmt+rs.getDouble("cancel"));
					}
				}
			}
			/*	CS Sale End	*/
			
			Map<Integer, SLEOrgReportResponseBean> sleMap = null;
			if(ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setOrgId((agtOrgId==1)?0:agtOrgId);
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				
				sleMap = SLERetailerReportsControllerImpl.fetchSaleCancelPwtMultipleRetailer(requestBean, con);
				
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

			double depositOLAAmtTot = 0.0;
			double withdrawOLAAmtTot = 0.0;
			double saleCSAmtTot = 0.0;
			double cancelCSAmtTot = 0.0;
			
			double saleSleAmtTot = 0.0;
			double cancelSleAmtTot = 0.0;
			double pwtSleAmtTot = 0.0;

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

				double depositOLAAmt = 0.0;
				double withdrawOLAAmt = 0.0;
				if(ReportUtility.isOLA) {
					if(olaMap.get(key) != null) {
						depositOLAAmt = olaMap.get(key).getMrpDepositAmt();
						withdrawOLAAmt = olaMap.get(key).getMrpWithdrawalAmt();
					}
				}
				double saleCSAmt = 0.0;
				double cancelCSAmt = 0.0;
				if(ReportUtility.isCS) {
					saleCSAmt = saleCSRetMap.get(key);
					cancelCSAmt = cancelCSRetMap.get(key);
				}
				
				double saleSleAmt = saleDrawRetMap.get(key);
				double cancelSleAmt = cancelDrawRetMap.get(key);
				double pwtSleAmt = pwtDrawRetMap.get(key);
				if(ReportUtility.isSLE) {
					if(sleMap.get(key) != null) {
						saleSleAmt = sleMap.get(key).getSaleAmt();
						cancelSleAmt = sleMap.get(key).getCancelAmt();
						pwtSleAmt = sleMap.get(key).getPwtAmt();
					}
				}

				double cashInHand = saleDrawAmt + saleScratchAmt + depositOLAAmt + (saleCSAmt-cancelCSAmt) + (saleSleAmt-cancelSleAmt)
						- (cancelDrawAmt + pwtDrawAmt + pwtScratchAmt + withdrawOLAAmt + pwtSleAmt);
				amtVal.append(saleDrawAmt + "," + cancelDrawAmt + ","
						+ pwtDrawAmt + "," + saleScratchAmt + ","
						+ pwtScratchAmt + "," + depositOLAAmt + "," + withdrawOLAAmt + ","
						+ (saleCSAmt-cancelCSAmt) + "," + saleSleAmt + "," + cancelSleAmt + ","
						+ pwtSleAmt + ",");
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

				depositOLAAmtTot += depositOLAAmt;
				withdrawOLAAmtTot += withdrawOLAAmt;
				saleCSAmtTot += saleCSAmt;
				cancelCSAmtTot += cancelCSAmt;
				
				saleSleAmtTot += saleSleAmt;
				cancelSleAmtTot += cancelSleAmt;
				pwtSleAmtTot += pwtSleAmt;
			}
			reportMap
					.put("Total", saleDrawAmtTot + "," + cancelDrawAmtTot + ","
							+ pwtDrawAmtTot + "," + saleScratchAmtTot + ","
							+ pwtScratchAmtTot + "," + depositOLAAmtTot + "," + withdrawOLAAmtTot + "," + (saleCSAmtTot-cancelCSAmtTot) + "," +saleSleAmtTot + "," + cancelSleAmtTot + ","
							+ pwtSleAmtTot + "," + cashAmtTot + ","
							+ cashInHandTot);
			if (isNoCash) {
				reportMap.put("Total", saleDrawAmtTot + "," + cancelDrawAmtTot
						+ "," + pwtDrawAmtTot + "," + saleScratchAmtTot + ","
						+ pwtScratchAmtTot + "," + saleSleAmtTot + "," + cancelSleAmtTot
						+ "," + pwtSleAmtTot + "," + cashInHandTot);
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

	public Map<String, Boolean> checkAvailableService() {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, Boolean> serMap = new HashMap<String, Boolean>();
		try {
			con = DBConnect.getConnection();
			String chkService = "select service_code,status from st_lms_service_master where service_code!='MGMT'";
			pstmt = con.prepareStatement(chkService);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				serMap.put(rs.getString("service_code"), "ACTIVE".equals(rs
						.getString("status")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return serMap;
	}

	public List<ArrayList<String>> consolidateReportExpand(
			Timestamp startDate, Timestamp endDate, int agtOrgId,
			boolean isFrmSP) {

		Connection con = null;
		List<ArrayList<String>> agtMap = null;
		boolean isSE = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			agtMap = consolidateReportExpandRetailerWise(startDate, endDate,
					isSE, agtOrgId,isFrmSP);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}

	public List<String> getGameList() throws SQLException {
		String gameQry = "select game_id,game_name from st_se_game_master order by game_id";
		Connection con = DBConnect.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(gameQry);
		List<String> gameName = new ArrayList<String>();
		while (rs.next()) {
			gameName.add(rs.getString("game_name"));
		}
		return gameName;
	}

	public List<ArrayList<String>> consolidateReportExpandRetailerWise(
			Timestamp startDate, Timestamp endDate, boolean isSE, int orgId,
			boolean isFrmSP) {

		String reportingquery = "";
		String ifAgentQuery = "";
		Connection con = null;
		double totalSaleAmtRetWise;
		double totalPwtAmtRetWise;
		double netAmtRetWise;
		
		List<ArrayList<String>> agentDataList = new ArrayList<ArrayList<String>>();

		if (startDate.after(endDate)) {
			return agentDataList; 
		}
		try {
			if (isSE) {
				con = DBConnect.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("select game_id,game_name from st_se_game_master order by game_id");
				ResultSet rs = pstmt.executeQuery();
				StringBuilder startQuery = new StringBuilder(" select name ");
				StringBuilder unionQuery = new StringBuilder(
						" from st_lms_organization_master om inner join  (select retailer_org_id");
				StringBuilder innerQuery = new StringBuilder(
						" from (select retailer_org_id ");
				while (rs.next()) {
					int gameId = rs.getInt("game_id");
					startQuery.append(",sale_" + gameId + ",pwt_" + gameId);
					unionQuery.append(",sum(sale_" + gameId + ")" + " sale_"
							+ gameId + ",sum(pwt_" + gameId + ")" + " pwt_"
							+ gameId);
					innerQuery.append(",if(game_id=" + gameId
							+ ",sum(sale),0.0) sale_" + gameId + ",if(game_id="
							+ gameId + ",sum(pwt),0.0) pwt_" + gameId);
				}
				if (orgId != 1) {
					ifAgentQuery = "where organization_id in(select organization_id from st_lms_organization_master where parent_id="
							+ orgId + ")";
				}
				if (isFrmSP) {
					reportingquery = " union all select organization_id retailer_org_id,game_id,sum(sale_ticket_mrp) sale,sum(pwt_net_amt) pwt  from  st_rep_se_retailer  where finaldate>=date('"
							+ startDate
							+ "') and finaldate<=date('"
							+ endDate
							+ "')  group by organization_id,game_id";
				}
				String salePwtQuery = " from (select current_owner_id retailer_org_id,a.game_id,sum(sold_tickets*ticket_price) sale, 0 pwt from st_se_game_ticket_inv_history a inner join st_se_game_master b on a.game_id=b.game_id and current_owner='RETAILER' and a.date>='"
						+ startDate
						+ "' and a.date<='"
						+ endDate
						+ "'  group by current_owner_id,game_id union all select  c.retailer_org_id,c.game_id,0 sale,ifnull(sum(pwt_amt),0.0) pwt from st_se_retailer_pwt c inner join st_lms_retailer_transaction_master d on c.transaction_id=d.transaction_id where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "'  and transaction_type='PWT'  group by c.retailer_org_id,c.game_id "
						+ reportingquery
						+ ")main  group by retailer_org_id,game_id order by retailer_org_id,game_id)sss group by retailer_org_id) final on organization_id=retailer_org_id "
						+ ifAgentQuery + " order by name";

				pstmt = con.prepareStatement(startQuery.append(
						unionQuery.toString()).append(innerQuery).append(
						salePwtQuery).toString());
				ResultSet rs1 = pstmt.executeQuery();

				int check = rs1.getMetaData().getColumnCount();

				while (rs1.next()) {
					ArrayList<String> agentData = new ArrayList<String>();
					totalSaleAmtRetWise=0.0;
					totalPwtAmtRetWise=0.0;
					netAmtRetWise=0.0;
					for (int i = 1; i <= check; i++) {
						agentData.add(rs1.getString(i));
						if(i>1){
							if(i%2==0){
								totalSaleAmtRetWise=totalSaleAmtRetWise+Double.parseDouble(rs1.getString(i));
								}
							else{
								totalPwtAmtRetWise=totalPwtAmtRetWise+Double.parseDouble(rs1.getString(i));
								}

						}
					}
					netAmtRetWise=totalSaleAmtRetWise-totalPwtAmtRetWise;
					agentData.add(String.valueOf(totalSaleAmtRetWise));
					agentData.add(String.valueOf(totalPwtAmtRetWise));
					agentData.add(String.valueOf(netAmtRetWise));
					agentDataList.add(agentData);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		System.out.println(agentDataList);
		return agentDataList;
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
