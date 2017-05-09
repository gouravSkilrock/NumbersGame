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
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class CompleteCollectionReportMRPHelper implements
		ICompleteCollectionReportHelper {
	static Log logger = LogFactory
			.getLog(CompleteCollectionReportMRPHelper.class);
	StringBuilder dates = null;

	public Map<String, Double> agentDirectPlayerPwt(Timestamp startDate,
			Timestamp endDate, int agtOrgId) {
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		Map<String, Double> dirPlrPwtMap = new LinkedHashMap<String, Double>();
		// Draw Direct Player Qry
		try {
			CallableStatement cstmt = con
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
			DBConnect.closeCon(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dirPlrPwtMap;
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

	public Map<String, CompleteCollectionBean> collectionAgentWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA) {
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
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("name"));
				if (isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			if (isDG) {
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					CallableStatement cstmt = con
							.prepareCall("{call drawCollectionAgentWisePerGame(?,?,?)}");
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
			}
			if (isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					CallableStatement cstmt = con
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
			}
			if (isCS) {

				// category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					CallableStatement cstmt = con
							.prepareCall("{call csCollectionAgentWisePerCategory(?,?,?)}");
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

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA) {
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
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
					CallableStatement cstmt = con
							.prepareCall("{call drawCollectionAgentWisePerGame(?,?,?)}");
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

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
					CallableStatement cstmt = con
							.prepareCall("{call scratchCollectionAgentWisePerGame(?,?,?)}");
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
				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (isCS) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
					CallableStatement cstmt = con
							.prepareCall("{call csCollectionAgentWisePerCategory(?,?,?)}");
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

				serGameAgtMap.put("CS", gameAgtMap);

			}
			System.out.println(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionDayWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			String viewBy, int orgId) {
		ResultSet rsGame = null;
		ResultSet rs = null;
		Map<String, CompleteCollectionBean> dateMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		String date = null;
		PreparedStatement pstmt = null;
		StringBuilder unionQuery = null;
		StringBuilder mainQuery = null;

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
				if (isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
			}
			CallableStatement cstmt = con
					.prepareCall("{call fillDateTable(?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.executeQuery();
			if (isDG) {
				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				String dgSaleCol = "";
				String dgPwtCol = "";
				String addDrawQry = "";
				String addDirPlrQry = "";
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");

					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						dgSaleCol = "mrp_amt";
						dgPwtCol = "pwt_amt";
						addDrawQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
						addDirPlrQry = " and agent_org_id=" + orgId;
					} else {
						dgSaleCol = "mrp_amt";
						dgPwtCol = "pwt_amt";
						addDrawQry = "";
						addDirPlrQry = "";
					}
					String dgQuery = "select date(alldate) alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0) pwt from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgSaleCol
							+ ") sale from st_dg_ret_sale_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') "
							+ addDrawQry
							+ " group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgSaleCol
							+ ") cancel from st_dg_ret_sale_refund_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
							+ addDrawQry
							+ " group by tx_date) cancel on alldate=cancel.tx_date) cancel inner join (select alldate,pwt from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgPwtCol
							+ ") pwt from st_dg_ret_pwt_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') "
							+ addDrawQry
							+ " group by tx_date) pwt on alldate=pwt.tx_date) pwt on sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate) a left outer join (select date(transaction_date) tx_date ,sum(pwt_amt) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " "
							+ addDirPlrQry
							+ " group by tx_date) b on alldate=tx_date  ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (");
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
							unionQuery = new StringBuilder(
									"union all select dgr.finaldate as alldate,sum(dgr.sale_mrp) as sale ,sum(dgr.ref_sale_mrp) cancel, sum(dgr.pwt_mrp) pwt,sum(ifnull(dga.direct_pwt_amt,0.00))as pwtDir  from st_rep_dg_retailer as dgr left outer join  st_rep_dg_agent as dga on dgr.finaldate=dga.finaldate and dgr.parent_id=dga.parent_id where dgr.parent_id="
											+ orgId
											+ " and dgr.game_id="
											+ gameNo
											+ " and dgr.finaldate>=date('"
											+ startDate
											+ "') and dgr.finaldate<=date('"
											+ endDate
											+ "') group by dgr.finaldate)as main group by main.alldate ");
						} else {
							unionQuery = new StringBuilder(
									" union all select finaldate as alldate,sum(sale_mrp) as sale,sum(ref_sale_mrp) as cancel, sum(pwt_mrp) as pwt, sum(direct_pwt_amt) as pwtDir from st_rep_dg_agent where game_id="
											+ gameNo
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by finaldate)as main group by main.alldate  ");
						}
						pstmt = con.prepareStatement(mainQuery.append(dgQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(dgQuery);
					}
					rs = pstmt.executeQuery();
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
				}
			}
			if (isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();

				String sePwtCol = "";
				String addScratchPwtQry = "";
				String addDirPlrQry = "";
				String addSaleQry = "";
				String saleQry = "";
				String cancelQry = "";
				String pwtQry = "";
				String dirPwtQry = "";
				String seQuery = "";

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						sePwtCol = " pwt_amt";
						addScratchPwtQry = " and rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
						addDirPlrQry = " and agent_org_id=" + orgId;
						addSaleQry = "'RETAILER' and gid.current_owner_id in(select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ") group by date";
					} else {
						sePwtCol = "pwt_amt";
						addScratchPwtQry = "";
						addDirPlrQry = "";
						addSaleQry = "'AGENT'  group by date";
					}

					if ("BOOK_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
							saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) sale from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id="
									+ orgId
									+ " and game_id="
									+ gameNo
									+ " group by transaction_date) sale on date(alldate)=transaction_date";
							cancelQry = "select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) cancel from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id="
									+ orgId
									+ " and game_id="
									+ gameNo
									+ " group by transaction_date) cancel on date(alldate)=transaction_date";
						} else {
							saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) sale from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id="
									+ gameNo
									+ " group by transaction_date) sale on date(alldate)=transaction_date";
							cancelQry = "select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) cancel from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id="
									+ gameNo
									+ " group by transaction_date) cancel on date(alldate)=transaction_date";
						}
					} else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select date, sum(soldTkt*ticket_price) sale from st_se_game_master gm inner join st_se_game_inv_detail gid inner join (select date(date) date,game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
								+ startDate
								+ "' and date<='"
								+ endDate
								+ "' and current_owner='RETAILER' group by book_nbr,date(date)) TktTlb on gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr where gm.game_id="
								+ gameNo
								+ " and gid.current_owner="
								+ addSaleQry + ") sale on date(alldate)=date";
						cancelQry = "select date(alldate) tx_date,0.0 cancel from tempdate";
					}

					pwtQry = "select date(alldate) tx_date,ifnull(pwt,0.0) pwt from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum("
							+ sePwtCol
							+ ") pwt from st_se_retailer_pwt rp,st_lms_retailer_transaction_master rtm where rp.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and rtm.transaction_type='PWT' and rp.game_id="
							+ gameNo
							+ " "
							+ addScratchPwtQry
							+ " group by tx_date) pwt on date(alldate)=tx_date";
					dirPwtQry = "select tx_date,sum(net_amt) pwtDir from (select date(transaction_date) tx_date ,pwt_amt net_amt from st_se_agt_direct_player_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " "
							+ addDirPlrQry
							+ ") aa group by tx_date";
					seQuery = "select a.tx_date as alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.tx_date,sale,cancel,pwt from ("
							+ saleQry
							+ ") sale inner join ("
							+ cancelQry
							+ ") cancel inner join ("
							+ pwtQry
							+ ") pwt on sale.tx_date=cancel.tx_date and sale.tx_date=pwt.tx_date and cancel.tx_date=pwt.tx_date) a left outer join ("
							+ dirPwtQry + ")pwtDir on a.tx_date=pwtDir.tx_date";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale) sale,sum(main.cancel) cancel,sum(main.pwt) pwt,sum(main.pwtDir) pwtdir from(");
						if ("BOOK_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_book_mrp)as sale,sum(ref_sale_mrp)as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_agent where game_id="
												+ gameNo
												+ " and finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
							else
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_book_mrp)as sale,sum(ref_sale_mrp)as cancel, sum(pwt_net_amt)as pwt ,sum(direct_pwt_amt)as pwtDir  from st_rep_se_bo where game_id="
												+ gameNo
												+ " and    finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
						} else {
							if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_agent where  game_id="
												+ gameNo
												+ " and finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
							else
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_bo where game_id="
												+ gameNo
												+ " and  finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate )as main group by main.alldate");
						}

						pstmt = con.prepareStatement(mainQuery.append(seQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(seQuery);
					}
					rs = pstmt.executeQuery();

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
			}

			if (isCS) {

				String csSaleCol = "";
				String addCSQry = "";
				String csQuery = "";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						csSaleCol = "mrp_amt";
						addCSQry = " and cs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
					} else {
						csSaleCol = "mrp_amt";
						addCSQry = "";
					}

					csQuery = "select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ csSaleCol
							+ ") sale from st_cs_sale_"
							+ catId
							+ " cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type ='CS_SALE' "
							+ addCSQry
							+ " group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ csSaleCol
							+ ") cancel from st_cs_refund_"
							+ catId
							+ " cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') "
							+ addCSQry
							+ " group by tx_date) cancel on alldate=cancel.tx_date) cancel on sale.alldate=cancel.alldate ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel from (");
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
							unionQuery = new StringBuilder(
									" union all select csr.finaldate as alldate,sum(csr.sale_mrp) as sale ,sum(csr.ref_sale_mrp) cancel  from st_rep_cs_retailer as csr, st_rep_cs_agent as csa where csr.finaldate=csa.finaldate and csr.parent_id=csa.parent_id and csr.parent_id="
											+ orgId
											+ " and csr.category_id="
											+ catId
											+ " and csr.finaldate>=date('"
											+ startDate
											+ "') and csr.finaldate<=date('"
											+ endDate
											+ "') group by csr.finaldate)as main group by main.alldate ");
						else
							unionQuery = new StringBuilder(
									" union all select finaldate as alldate,sum(sale_mrp) as sale,sum(ref_sale_mrp) as cancel from st_rep_cs_agent where category_id="
											+ catId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by finaldate)as main group by main.alldate  ");

						pstmt = con.prepareStatement(mainQuery.append(csQuery)
								.append(unionQuery.toString()).toString());

					} else {
						pstmt = con.prepareStatement(csQuery);
					}

					rs = pstmt.executeQuery();
					while (rs.next()) {
						String tempDate = rs.getString("alldate");
						dateMap.get(tempDate).setCSSale(
								dateMap.get(tempDate).getCSSale()
										+ rs.getDouble("sale"));
						dateMap.get(tempDate).setCSCancel(
								dateMap.get(tempDate).getCSCancel()
										+ rs.getDouble("cancel"));
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionDayWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			String viewBy, int orgId) {
		ResultSet rsGame = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		StringBuilder unionQuery = null;
		StringBuilder mainQuery = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;

		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}

		try {
			CallableStatement cstmt = con
					.prepareCall("{call fillDateTable(?,?)}");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
			cstmt.executeQuery();
			if (isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate, isDG, isSE,
							isCS));
					int gameNo = rsGame.getInt("game_id");
					String dgSaleCol = "";
					String dgPwtCol = "";
					String addDrawQry = "";
					String addDirPlrQry = "";

					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						dgSaleCol = "mrp_amt";
						dgPwtCol = "pwt_amt";
						addDrawQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
						addDirPlrQry = " and agent_org_id=" + orgId;
					} else {
						dgSaleCol = "mrp_amt";
						dgPwtCol = "pwt_amt";
						addDrawQry = "";
						addDirPlrQry = "";
					}
					String dgQuery = "select date(alldate) alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0) pwt from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgSaleCol
							+ ") sale from st_dg_ret_sale_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') "
							+ addDrawQry
							+ " group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgSaleCol
							+ ") cancel from st_dg_ret_sale_refund_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
							+ addDrawQry
							+ " group by tx_date) cancel on alldate=cancel.tx_date) cancel inner join (select alldate,pwt from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ dgPwtCol
							+ ") pwt from st_dg_ret_pwt_"
							+ gameNo
							+ " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') "
							+ addDrawQry
							+ " group by tx_date) pwt on alldate=pwt.tx_date) pwt on sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate) a left outer join (select date(transaction_date) tx_date ,sum(pwt_amt) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " "
							+ addDirPlrQry
							+ " group by tx_date) b on alldate=tx_date  ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (");
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
							unionQuery = new StringBuilder(
									"union all select dgr.finaldate as alldate,sum(dgr.sale_mrp) as sale ,sum(dgr.ref_sale_mrp) cancel, sum(dgr.pwt_mrp) pwt,sum(ifnull(dga.direct_pwt_amt,0.00))as pwtDir  from st_rep_dg_retailer as dgr left outer join  st_rep_dg_agent as dga on dgr.finaldate=dga.finaldate and dgr.parent_id=dga.parent_id where dgr.parent_id="
											+ orgId
											+ " and dgr.game_id="
											+ gameNo
											+ " and dgr.finaldate>=date('"
											+ startDate
											+ "') and dgr.finaldate<=date('"
											+ endDate
											+ "') group by dgr.finaldate)as main group by main.alldate ");
						} else {
							unionQuery = new StringBuilder(
									" union all select finaldate as alldate,sum(sale_mrp) as sale,sum(ref_sale_mrp) as cancel, sum(pwt_mrp) as pwt, sum(direct_pwt_amt) as pwtDir from st_rep_dg_agent where game_id="
											+ gameNo
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by finaldate)as main group by main.alldate  ");
						}
						pstmt = con.prepareStatement(mainQuery.append(dgQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(dgQuery);
					}
					rs = pstmt.executeQuery();
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

				serGameAgtMap.put("DG", gameAgtMap);
			}

			if (isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				String sePwtCol = "";
				String addScratchPwtQry = "";
				String addDirPlrQry = "";
				String addSaleQry = "";
				String saleQry = "";
				String cancelQry = "";
				String pwtQry = "";
				String dirPwtQry = "";
				String seQuery = "";

				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate, isDG, isSE,
							isCS));
					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						sePwtCol = " pwt_amt";
						addScratchPwtQry = " and rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
						addDirPlrQry = " and agent_org_id=" + orgId;
						addSaleQry = "'RETAILER' and gid.current_owner_id in(select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ") group by date";
					} else {
						sePwtCol = "pwt_amt";
						addScratchPwtQry = "";
						addDirPlrQry = "";
						addSaleQry = "'AGENT'  group by date";
					}

					if ("BOOK_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
							saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) sale from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id="
									+ orgId
									+ " and game_id="
									+ gameNo
									+ " group by transaction_date) sale on date(alldate)=transaction_date";
							cancelQry = "select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) cancel from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id="
									+ orgId
									+ " and game_id="
									+ gameNo
									+ " group by transaction_date) cancel on date(alldate)=transaction_date";
						} else {
							saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) sale from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id="
									+ gameNo
									+ " group by transaction_date) sale on date(alldate)=transaction_date";
							cancelQry = "select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (select transaction_date,sum(mrp_amt) mrpAmt,sum(mrp_amt) cancel from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
									+ startDate
									+ "' and transaction_date<='"
									+ endDate
									+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id="
									+ gameNo
									+ " group by transaction_date) cancel on date(alldate)=transaction_date";
						}
					} else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select date, sum(soldTkt*ticket_price) sale from st_se_game_master gm inner join st_se_game_inv_detail gid inner join (select date(date) date,game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
								+ startDate
								+ "' and date<='"
								+ endDate
								+ "' and current_owner='RETAILER' group by book_nbr,date(date)) TktTlb on gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr where gm.game_id="
								+ gameNo
								+ " and gid.current_owner="
								+ addSaleQry + ") sale on date(alldate)=date";
						cancelQry = "select date(alldate) tx_date,0.0 cancel from tempdate";
					}

					pwtQry = "select date(alldate) tx_date,ifnull(pwt,0.0) pwt from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum("
							+ sePwtCol
							+ ") pwt from st_se_retailer_pwt rp,st_lms_retailer_transaction_master rtm where rp.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and rtm.transaction_type='PWT' and rp.game_id="
							+ gameNo
							+ " "
							+ addScratchPwtQry
							+ " group by tx_date) pwt on date(alldate)=tx_date";
					dirPwtQry = "select tx_date,sum(net_amt) pwtDir from (select date(transaction_date) tx_date ,pwt_amt net_amt from st_se_agt_direct_player_pwt where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and game_id="
							+ gameNo
							+ " "
							+ addDirPlrQry
							+ ") aa group by tx_date";
					seQuery = "select a.tx_date as alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.tx_date,sale,cancel,pwt from ("
							+ saleQry
							+ ") sale inner join ("
							+ cancelQry
							+ ") cancel inner join ("
							+ pwtQry
							+ ") pwt on sale.tx_date=cancel.tx_date and sale.tx_date=pwt.tx_date and cancel.tx_date=pwt.tx_date) a left outer join ("
							+ dirPwtQry + ")pwtDir on a.tx_date=pwtDir.tx_date";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale) sale,sum(main.cancel) cancel,sum(main.pwt) pwt,sum(main.pwtDir) pwtdir from(");
						if ("BOOK_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_book_mrp)as sale,sum(ref_sale_mrp)as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_agent where game_id="
												+ gameNo
												+ " and finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
							else
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_book_mrp)as sale,sum(ref_sale_mrp)as cancel, sum(pwt_net_amt)as pwt ,sum(direct_pwt_amt)as pwtDir  from st_rep_se_bo where game_id="
												+ gameNo
												+ " and    finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
						} else {
							if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_agent where  game_id="
												+ gameNo
												+ " and finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate)as main group by main.alldate");
							else
								unionQuery = new StringBuilder(
										" union all select finaldate as alldate,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt,sum(direct_pwt_amt)as pwtDir from st_rep_se_bo where game_id="
												+ gameNo
												+ " and  finaldate>=date('"
												+ startDate
												+ "') and finaldate<=date('"
												+ endDate
												+ "') group by finaldate )as main group by main.alldate");
						}

						pstmt = con.prepareStatement(mainQuery.append(seQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(seQuery);
					}
					rs = pstmt.executeQuery();
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

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (isCS) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();

				String csSaleCol = "";
				String addCSQry = "";
				String csQuery = "";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getDayMap(startDate, endDate, isDG, isSE,
							isCS));
					int catId = rsGame.getInt("category_id");
					if (viewBy.equalsIgnoreCase("Agent") && orgId != 0) {
						csSaleCol = "mrp_amt";
						addCSQry = " and cs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
					} else {
						csSaleCol = "mrp_amt";
						addCSQry = "";
					}

					csQuery = "select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ csSaleCol
							+ ") sale from st_cs_sale_"
							+ catId
							+ " cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type ='CS_SALE' "
							+ addCSQry
							+ " group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum("
							+ csSaleCol
							+ ") cancel from st_cs_refund_"
							+ catId
							+ " cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' and transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') "
							+ addCSQry
							+ " group by tx_date) cancel on alldate=cancel.tx_date) cancel on sale.alldate=cancel.alldate ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel from (");
						if (viewBy.equalsIgnoreCase("Agent") && orgId != 0)
							unionQuery = new StringBuilder(
									" union all select csr.finaldate as alldate,sum(csr.sale_mrp) as sale ,sum(csr.ref_sale_mrp) cancel  from st_rep_cs_retailer as csr, st_rep_cs_agent as csa where csr.finaldate=csa.finaldate and csr.parent_id=csa.parent_id and csr.parent_id="
											+ orgId
											+ " and csr.category_id="
											+ catId
											+ " and csr.finaldate>=date('"
											+ startDate
											+ "') and csr.finaldate<=date('"
											+ endDate
											+ "') group by csr.finaldate)as main group by main.alldate ");
						else
							unionQuery = new StringBuilder(
									" union all select finaldate as alldate,sum(sale_mrp) as sale,sum(ref_sale_mrp) as cancel from st_rep_cs_agent where category_id="
											+ catId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by finaldate)as main group by main.alldate  ");

						pstmt = con.prepareStatement(mainQuery.append(csQuery)
								.append(unionQuery.toString()).toString());

					} else {
						pstmt = con.prepareStatement(csQuery);
					}

					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}

					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}

				serGameAgtMap.put("CS", gameAgtMap);

			}
			logger.debug(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;
		boolean isSE = false;
		boolean isDG = false;
		boolean isCS = false;
		boolean isOLA = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			isDG = serMap.get("DG");
			isCS = serMap.get("CS");
			isOLA = serMap.get("OLA");
			if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con, isDG,
						isSE, isCS, isOLA);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con, isDG, isSE,
						isCS, isOLA, "BO", 0);
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
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportExpand(
			Timestamp startDate, Timestamp endDate, String reportType) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		boolean isSE = false;
		boolean isDG = false;
		boolean isCS = false;
		boolean isOLA = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			isDG = serMap.get("DG");
			isCS = serMap.get("CS");
			isOLA = serMap.get("OLA");
			if ("Agent Wise Expand".equals(reportType)) {
				agtMap = collectionAgentWiseExpand(startDate, endDate, con,
						isDG, isSE, isCS, isOLA);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con, isDG,
						isSE, isCS, isOLA, "BO", 0);
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
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReportForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId,
			String reportType) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;
		boolean isSE = false;
		boolean isDG = false;
		boolean isCS = false;
		boolean isOLA = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			isDG = serMap.get("DG");
			isCS = serMap.get("CS");
			isOLA = serMap.get("OLA");
			if ("Retailer Wise".equals(reportType)) {
				agtMap = collectionRetailerWise(startDate, endDate, con, isDG,
						isSE, isCS, isOLA, agtOrgId);
			} else if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con, isDG,
						isSE, isCS, isOLA);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con, isDG, isSE,
						isCS, isOLA, "AGENT", agtOrgId);
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
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportForAgentExpand(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		boolean isSE = false;
		boolean isDG = false;
		boolean isCS = false;
		boolean isOLA = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			isDG = serMap.get("DG");
			isCS = serMap.get("CS");
			isOLA = serMap.get("OLA");
			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con,
						isDG, isSE, isCS, isOLA, agtOrgId);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con, isDG,
						isSE, isCS, isOLA, "AGENT", agtOrgId);
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
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionRetailerWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			int agtOrgId) {
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
				agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='RETAILER' order by name";
			} else {
				agtOrgQry = "select name,organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId + " order by name";

			}
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("name"));
				if (isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (isSE) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			agtOrgQry = agtOrgQry.replace("name,", "");
			StringBuilder unionQuery = null;
			StringBuilder mainQuery = null;
			if (isDG) {
				// Game Master Query
				String addQry = "";
				if (agtOrgId != 0)
					addQry = "and om.parent_id=" + agtOrgId;

				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					String dgQuery = "select sale.organization_id,sale,cancel,pwt from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' "
							+ addQry
							+ ") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt from (");
						unionQuery = new StringBuilder(
								"union all select dgr.organization_id as organization_id,sum(dgr.sale_mrp) as sale ,sum(dgr.ref_sale_mrp) cancel, sum(dgr.pwt_mrp) pwt  from  st_rep_dg_retailer as dgr  where  dgr.game_id="
										+ gameNo
										+ " and dgr.parent_id="
										+ agtOrgId
										+ " and dgr.finaldate>=date('"
										+ startDate
										+ "') and dgr.finaldate<=date('"
										+ endDate
										+ "') group by dgr.organization_id)as main group by main.organization_id ");

						pstmt = con.prepareStatement(mainQuery.append(dgQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(dgQuery.toString());
					}

					logger.debug("-------Draw Game Query------\n" + pstmt);
					rs = pstmt.executeQuery();
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

			}

			if (isSE) {
				// Game Master Query
				String gameQry = "select game_id from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				String saleQry = "";
				String cancelQry = "";
				String seQuery = "";
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");

					if ("BOOK_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select retailer_org_id,parent_id,sum(mrp_amt) sale from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and retailer_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and game_id="
								+ gameNo
								+ " and parent_id="
								+ agtOrgId
								+ " group by retailer_org_id) sale on retailer_org_id=organization_id where organization_type='RETAILER' and om.parent_id="
								+ agtOrgId;
						cancelQry = "select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt) cancel from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and game_id=1 group by retailer_org_id) cancel on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
					} else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master left outer join (select current_owner_id , sum(soldTkt*ticket_price)  mrpAmt,sum(soldTkt*ticket_price) sale from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
								+ startDate
								+ "' and date<='"
								+ endDate
								+ "' and current_owner='RETAILER' and game_id="
								+ gameNo
								+ " group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
						cancelQry = "select organization_id,0.00 cancel from st_lms_organization_master where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
					}
					seQuery = "select sale.organization_id,sale,cancel,pwt from ("
							+ saleQry
							+ ") sale inner join ("
							+ cancelQry
							+ ") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join (select rp.retailer_org_id ,sum(pwt_amt) pwt from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and transaction_type='PWT' and rp.game_id="
							+ gameNo
							+ " group by rp.retailer_org_id union all select retailer_org_id,sum(pwt_amt) pwt from st_se_agent_pwt ap inner join st_lms_agent_transaction_master tm on ap.transaction_id=tm.transaction_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and transaction_type='PWT' and ap.game_id="
							+ gameNo
							+ " group by retailer_org_id) pwt on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id="
							+ agtOrgId
							+ ")  pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale) as sale ,sum(main.cancel) as cancel,sum(main.pwt) as pwt from (");
						if ("BOOK_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							unionQuery = new StringBuilder(
									" union all select organization_id,sum(sale_book_mrp) as sale,sum(ref_sale_mrp) as cancel,sum(pwt_mrp)as pwt from st_rep_se_retailer where game_id="
											+ gameNo
											+ " and parent_id="
											+ agtOrgId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by organization_id)as main group by main.organization_id ");
						} else if ("TICKET_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							unionQuery = new StringBuilder(
									" union all select organization_id,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt from st_rep_se_retailer where game_id="
											+ gameNo
											+ " and parent_id="
											+ agtOrgId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by organization_id)as main group by main.organization_id");
						}

						pstmt = con.prepareStatement(mainQuery.append(seQuery)
								.append(unionQuery).toString());
					} else {
						pstmt = con.prepareStatement(seQuery);
					}

					logger.debug("-------Scratch Game Query------\n" + pstmt);
					rs = pstmt.executeQuery();
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
			}
			if (isCS) {
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();

				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					String addQry = "";
					if (agtOrgId != 0)
						addQry = "and om.parent_id=" + agtOrgId;

					String csQuery = "select sale.organization_id,sale,cancel from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(mrp_amt) as sale from st_cs_sale_"
							+ catId
							+ " cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where rtm.transaction_type ='CS_SALE' and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by cs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' "
							+ addQry
							+ ") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(mrp_amt) as cancel from st_cs_refund_"
							+ catId
							+ " cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by cs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") cancel on sale.organization_id=cancel.organization_id";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel from (");
						unionQuery = new StringBuilder(
								" union all select organization_id,sum(sale_mrp) as sale ,sum(ref_sale_mrp) cancel  from  st_rep_cs_retailer where  category_id="
										+ catId
										+ " and parent_id="
										+ agtOrgId
										+ " and finaldate>=date('"
										+ startDate
										+ "') and finaldate<=date('"
										+ endDate
										+ "') group by organization_id)as main group by main.organization_id ");
						pstmt = con.prepareStatement(mainQuery.append(csQuery)
								.append(unionQuery.toString()).toString());
					}

					logger.debug("-------Comm Service Query------\n" + pstmt);
					rs = pstmt.executeQuery();
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

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			int agtOrgId) {
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
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			StringBuilder unionQuery = null;
			StringBuilder mainQuery = null;
			if (isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query

				// Game Master Query
				String addQry = "";
				if (agtOrgId != 0)
					addQry = "and om.parent_id=" + agtOrgId;

				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
					String dgQuery = "select sale.organization_id,sale,cancel,pwt from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' "
							+ addQry
							+ ") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
							+ gameNo
							+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id ";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt from (");
						unionQuery = new StringBuilder(
								"union all select dgr.organization_id as organization_id,sum(dgr.sale_mrp) as sale ,sum(dgr.ref_sale_mrp) cancel, sum(dgr.pwt_mrp) pwt  from  st_rep_dg_retailer as dgr  where  dgr.game_id="
										+ gameNo
										+ " and dgr.parent_id="
										+ agtOrgId
										+ " and dgr.finaldate>=date('"
										+ startDate
										+ "') and dgr.finaldate<=date('"
										+ endDate
										+ "') group by dgr.organization_id)as main group by main.organization_id ");

						pstmt = con.prepareStatement(mainQuery.append(dgQuery)
								.append(unionQuery.toString()).toString());
					} else {
						pstmt = con.prepareStatement(dgQuery.toString());
					}

					logger.debug("-------Draw Game Query------\n" + pstmt);
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

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Game Master Query

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				String saleQry = "";
				String cancelQry = "";
				String seQuery = "";
				while (rsGame.next()) {
					int gameNo = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));

					if ("BOOK_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select retailer_org_id,parent_id,sum(mrp_amt) sale from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and retailer_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and game_id="
								+ gameNo
								+ " and parent_id="
								+ agtOrgId
								+ " group by retailer_org_id) sale on retailer_org_id=organization_id where organization_type='RETAILER' and om.parent_id="
								+ agtOrgId;
						cancelQry = "select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt) cancel from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "' and game_id=1 group by retailer_org_id) cancel on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
					} else if ("TICKET_WISE"
							.equals(LMSFilterDispatcher.seSaleReportType)) {
						saleQry = "select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master left outer join (select current_owner_id , sum(soldTkt*ticket_price)  mrpAmt,sum(soldTkt*ticket_price) sale from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
								+ startDate
								+ "' and date<='"
								+ endDate
								+ "' and current_owner='RETAILER' and game_id="
								+ gameNo
								+ " group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
						cancelQry = "select organization_id,0.00 cancel from st_lms_organization_master where organization_type='RETAILER' and parent_id="
								+ agtOrgId;
					}
					seQuery = "select sale.organization_id,sale,cancel,pwt from ("
							+ saleQry
							+ ") sale inner join ("
							+ cancelQry
							+ ") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join (select rp.retailer_org_id ,sum(pwt_amt) pwt from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and transaction_type='PWT' and rp.game_id="
							+ gameNo
							+ " group by rp.retailer_org_id union all select retailer_org_id,sum(pwt_amt) pwt from st_se_agent_pwt ap inner join st_lms_agent_transaction_master tm on ap.transaction_id=tm.transaction_id where transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' and transaction_type='PWT' and ap.game_id="
							+ gameNo
							+ " group by retailer_org_id) pwt on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id="
							+ agtOrgId
							+ ")  pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale) as sale ,sum(main.cancel) as cancel,sum(main.pwt) as pwt from (");
						if ("BOOK_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							unionQuery = new StringBuilder(
									" union all select organization_id,sum(sale_book_mrp) as sale,sum(ref_sale_mrp) as cancel,sum(pwt_mrp)as pwt from st_rep_se_retailer where game_id="
											+ gameNo
											+ " and parent_id="
											+ agtOrgId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by organization_id)as main group by main.organization_id ");
						} else if ("TICKET_WISE"
								.equals(LMSFilterDispatcher.seSaleReportType)) {
							unionQuery = new StringBuilder(
									" union all select organization_id,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_mrp)as pwt from st_rep_se_retailer where game_id="
											+ gameNo
											+ " and parent_id="
											+ agtOrgId
											+ " and finaldate>=date('"
											+ startDate
											+ "') and finaldate<=date('"
											+ endDate
											+ "') group by organization_id)as main group by main.organization_id");
						}

						pstmt = con.prepareStatement(mainQuery.append(seQuery)
								.append(unionQuery).toString());
					} else {
						pstmt = con.prepareStatement(seQuery);
					}

					logger.debug("-------Scratch Game Query------\n" + pstmt);
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

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if (isCS) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				// Category Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();

				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
					String addQry = "";
					if (agtOrgId != 0)
						addQry = "and om.parent_id=" + agtOrgId;

					String csQuery = "select sale.organization_id,sale,cancel from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(mrp_amt) as sale from st_cs_sale_"
							+ catId
							+ " cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where rtm.transaction_type ='CS_SALE' and rtm.transaction_date>='"
							+ startDate
							+ "' and rtm.transaction_date<='"
							+ endDate
							+ "' group by cs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' "
							+ addQry
							+ ") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(mrp_amt) as cancel from st_cs_refund_"
							+ catId
							+ " cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "' group by cs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  "
							+ addQry
							+ ") cancel on sale.organization_id=cancel.organization_id";

					if (LMSFilterDispatcher.isRepFrmSP) {
						mainQuery = new StringBuilder(
								"select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel from (");
						unionQuery = new StringBuilder(
								" union all select organization_id,sum(sale_mrp) as sale ,sum(ref_sale_mrp) cancel  from  st_rep_cs_retailer where  category_id="
										+ catId
										+ " and parent_id="
										+ agtOrgId
										+ " and finaldate>=date('"
										+ startDate
										+ "') and finaldate<=date('"
										+ endDate
										+ "') group by organization_id)as main group by main.organization_id ");
						pstmt = con.prepareStatement(mainQuery.append(csQuery)
								.append(unionQuery.toString()).toString());
					}

					logger.debug("-------Comm Service Query------\n" + pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}
					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}

				serGameAgtMap.put("CS", gameAgtMap);

			}
			logger.debug(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpandMrpWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			int agtOrgId) {
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

			if (isDG) {
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
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
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

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (isSE) {
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
					gameMap.putAll(getAgentMap(rsRetOrg, isDG, isSE, isCS,
							isOLA));
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
				serGameAgtMap.put("SE", gameAgtMap);
			}
			System.out.println(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	private Map<String, CompleteCollectionBean> getAgentMap(ResultSet rsRetOrg,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA)
			throws SQLException {
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;

		while (rsRetOrg.next()) {
			collBean = new CompleteCollectionBean();
			collBean.setOrgName(rsRetOrg.getString("name"));
			if (isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
			}
			if (isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
			}
			if (isOLA) {
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
			Timestamp endDate, boolean isDG, boolean isSE, boolean isCS)
			throws SQLException {
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
			if (isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
				collBean.setDrawDirPlyPwt(0.0);
			}
			if (isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
				collBean.setScratchDirPlyPwt(0.0);
			}
			if (isCS) {
				collBean.setCSSale(0.0);
				collBean.setCSCancel(0.0);
			}
		}
		return dateMap;
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
		return orgAddMap;
	}

	public Map<String, String> getOrgMap(String orgType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, String> orgMap = new LinkedHashMap<String, String>();
		try {
			con = DBConnect.getConnection();
			String chkService = "select name,organization_id from st_lms_organization_master where organization_type=? order by name";
			pstmt = con.prepareStatement(chkService);
			pstmt.setString(1, orgType);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgMap.put(rs.getString("organization_id"), rs
						.getString("name"));
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
		return orgMap;
	}

	public Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> transactionReportForAgent(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> resultMap = new HashMap<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>>();
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		boolean isSE = false;
		boolean isDG = false;
		boolean isCS = false;
		boolean isOLA = false;
		try {
			con = DBConnect.getConnection();
			Map<String, Boolean> serMap = checkAvailableService();
			isSE = serMap.get("SE");
			isDG = serMap.get("DG");
			isCS = serMap.get("CS");
			isOLA = serMap.get("OLA");
			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con,
						isDG, isSE, isCS, isOLA, agtOrgId);
				resultMap.put("NetAmt", agtMap);
				agtMap = collectionRetailerWiseExpandMrpWise(startDate,
						endDate, con, isDG, isSE, isCS, isOLA, agtOrgId);
				resultMap.put("MrpAmt", agtMap);
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
		return resultMap;
	}

	@Override
	public Map<String, CompleteCollectionBean> collectionAgentWise(
			Timestamp startDate, Timestamp endDate, Connection con) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, CompleteCollectionBean> collectionDayWise(
			Timestamp startDate, Timestamp endDate, Connection con,
			String viewBy, int orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionDayWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con,
			String viewBy, int orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, CompleteCollectionBean> collectionRetailerWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpandMrpWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> agentDirectPlayerPwt(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType,
			ReportStatusBean reportStatusBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportExpand(
			Timestamp startDate, Timestamp endDate, String reportType,
			ReportStatusBean reportStatusBean) {
		// TODO Auto-generated method stub
		return null;
	}
}
