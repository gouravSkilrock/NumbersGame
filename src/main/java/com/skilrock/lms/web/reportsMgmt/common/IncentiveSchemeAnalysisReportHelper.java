package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Statement;
import java.sql.Timestamp;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.IncentiveReportBean;
import com.skilrock.lms.beans.CompleteCollectionBean;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

import java.sql.SQLException;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class IncentiveSchemeAnalysisReportHelper {
	
static Log logger=LogFactory.getLog(IncentiveSchemeAnalysisReportHelper.class);
final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	@SuppressWarnings("unchecked")
	public Map<Integer , IncentiveReportBean> fetchIncentiveSchemeAnalysisReport(Timestamp startDate,Timestamp endDate,String gameList,
			String agentList, String grtrThnAmt, String lssThnAmt,boolean isArchTablesReq) {

		
		String gameQry=null;
		String orgCodeQuery=null;
		String agntRetInfoQuery=null;
		String queryOrderAppender=null;
		
		boolean checkLimitsGtr=false;
		boolean checkLimitsBoth=false;
		boolean checkLimitsLess=false;

		StringBuilder dgSaleQuery=null;
		
		ResultSet rs=null;
		ResultSet rsGame=null;
		
		Statement stmt=null;
		Statement gameStmt = null;
	
		IncentiveReportBean agentBean=null;
		CompleteCollectionBean gameBean=null;
		
		Map<Integer , IncentiveReportBean> agentRetInfoMap=null;
		Map<Integer , IncentiveReportBean> reportingInfoMap=null;
		LinkedHashMap<Integer, IncentiveReportBean> sortedMap=null;
		
		Connection con=null;

		try{
			
		orgCodeQuery=QueryManager.getOrgCodeQuery().replace("orgCode", "");
		queryOrderAppender=QueryManager.getAppendOrgOrder();
		con=DBConnect.getConnection();
		if(agentList.contains("-1")){
			//agntRetInfoQuery="select a.organization_id,name agentName,retailerName,address from st_lms_organization_master b,(select organization_id,"+QueryManager.getOrgCodeQuery()+",parent_id ,concat(addr_line1,' ',city) address from st_lms_organization_master where organization_type='RETAILER') a where b.organization_id=a.parent_id";
			agntRetInfoQuery="select a.organization_id ret_id, b.organization_id agent_id ,upper("+orgCodeQuery+") orgCode  , orgCode retailerName,address from st_lms_organization_master b,(select organization_id, upper("+orgCodeQuery+") orgCode  ,parent_id ,upper(city) address from st_lms_organization_master where organization_type='RETAILER') a where b.organization_id=a.parent_id  order by "+ ("organization_id".equals(queryOrderAppender)?"agent_id":queryOrderAppender);
			
		}else {
			//agntRetInfoQuery="select a.organization_id organization_id ,name agentName,retailerName,address from st_lms_organization_master b,(select organization_id,name retailerName,parent_id ,concat(addr_line1,' ',city) address from st_lms_organization_master where parent_id in ("+agentList+")) a where b.organization_id=a.parent_id order by agentName";
			agntRetInfoQuery="select a.organization_id ret_id, b.organization_id agent_id ,upper("+orgCodeQuery+") orgCode  , orgCode retailerName,address from st_lms_organization_master b,(select organization_id, upper("+orgCodeQuery+") orgCode  ,parent_id ,upper(city) address from st_lms_organization_master where parent_id in ("+agentList+")) a where b.organization_id=a.parent_id  order by "+ ("organization_id".equals(queryOrderAppender)?"agent_id":queryOrderAppender);
		}

		stmt=con.createStatement();
		rs=stmt.executeQuery(agntRetInfoQuery);
		agentRetInfoMap=new LinkedHashMap<Integer, IncentiveReportBean>();
		reportingInfoMap=new LinkedHashMap<Integer, IncentiveReportBean>();
		
		while(rs.next()){
			IncentiveReportBean incentiveReportBean=new IncentiveReportBean();
			incentiveReportBean.setAgentOrgId(rs.getInt("agent_id"));
			incentiveReportBean.setAgentName(rs.getString("orgCode"));
			incentiveReportBean.setRetailerName(rs.getString("retailerName"));
			incentiveReportBean.setAddress(rs.getString("address"));
			agentRetInfoMap.put(rs.getInt("ret_id"), incentiveReportBean);
		}
		
		if(gameList.contains("-1")){
			gameQry = ReportUtility.getDrawGameMapQuery(startDate);
		}else{
			gameQry="select game_id,game_name from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where closing_time <='"+startDate+"') and game_id in ("+getGameNames(gameList.split(","))+")order by display_order";
		}

		gameStmt = con.createStatement();
		rsGame = gameStmt.executeQuery(gameQry);
		while (rsGame.next()) {
			StringBuilder mainQuery=new StringBuilder("");
			int gameId = rsGame.getInt("game_id");
			String gameName = rsGame.getString("game_name");
			dgSaleQuery = new StringBuilder("select retailer_org_id,game_id,sum(mrp_amt)  mrp_amt from (select rs.game_id,-sum(mrp_amt)  mrp_amt ,rs.retailer_org_id,rtm.transaction_date from st_dg_ret_sale_refund_"
				+ gameId
				+ "  rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
				+ startDate
				+ "' and rtm.transaction_date<='"
				+ endDate
				+ "' group by  retailer_org_id union all select rs.game_id,sum(mrp_amt)  mrp_amt ,rs.retailer_org_id,rtm.transaction_date from st_dg_ret_sale_"
				+ gameId
				+ "  rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
				+ startDate
				+ "' and rtm.transaction_date<='"
				+ endDate
				+ "' group by  retailer_org_id) a group by retailer_org_id");
			
			
			if(isArchTablesReq){
				String unionQuery=" union all select organization_id retailer_org_id ,game_id,sum(sale_mrp)-  sum(ref_sale_mrp) sale_mrp from st_rep_dg_retailer where game_id="+gameId+" and finaldate>= '"+startDate+"' and finaldate<='"+endDate+"' and if((sale_mrp - ref_sale_mrp) > 0, 1, 0) group by organization_id ) a group by retailer_org_id";
				mainQuery.append("select retailer_org_id,game_id,sum(mrp_amt)  mrp_amt from (").append(dgSaleQuery).append(unionQuery);
			}else{
				mainQuery.append(dgSaleQuery);
			}

			logger.info("Game Wise Data ;->" +mainQuery.toString());
			stmt = con.createStatement();
			rs = stmt.executeQuery(mainQuery.toString());

			while (rs.next()) {

				int agtOrgId = rs.getInt("retailer_org_id");
				agentBean = reportingInfoMap.get(agtOrgId);
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
					gameBean.setDrawSale(rs.getDouble("mrp_amt"));
					}else {
						agentBean=agentRetInfoMap.get(agtOrgId);
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
							gameBean.setDrawSale(rs.getDouble("mrp_amt"));
							reportingInfoMap.put(agtOrgId, agentBean);
					}
				}
			}	
		}
		
		if (!"".equals(grtrThnAmt) && !"".equals(lssThnAmt)) {
			checkLimitsBoth=true;
		} else if (!"".equals(grtrThnAmt)) {
			checkLimitsGtr=true;
		} else if (!"".equals(lssThnAmt)) {
			checkLimitsLess=true;
		}
		if(checkLimitsBoth || checkLimitsGtr || checkLimitsLess || true)
			removeOutOfSaleLimitRetailers(reportingInfoMap,grtrThnAmt , lssThnAmt,checkLimitsBoth ,checkLimitsGtr ,checkLimitsLess);
		 sortedMap = sortByComparator(reportingInfoMap);
	}

		catch(SQLException ex){
			ex.printStackTrace();
		}
		catch(Exception ex){
			ex.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, gameStmt, rsGame);
			DBConnect.closeRs(rs);
			DBConnect.closeStmt(stmt);
		}
		
		return sortedMap;
	}

	private String getGameNames(String [] gameNamesArray){
		StringBuilder drawIds=new StringBuilder();
		for(int i=0; i<gameNamesArray.length ;i++){
			drawIds.append("'").append(gameNamesArray[i]).append("',");	
		}
		return drawIds.replace(drawIds.lastIndexOf(","), drawIds.length(), "").toString();
	}
	
	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
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
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return orgAdd;
	}
	
	
	public Map<String, String> allGameMap(String gameList ,java.sql.Timestamp fromDate) throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con=null;
		String gameQry=null;
		try {
			
				con = DBConnect.getConnection();
			gameQry=gameList.contains("-1")?"select game_name,'DG' as game_type,display_order from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where closing_time <='"+fromDate+"') order by display_order":"select game_name,'DG' as game_type,display_order from st_dg_game_master where game_id in("+getGameNames(gameList.split(","))+")";
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
			DBConnect.closeConnection(con, pstmt, rs);
		}
		logger.info(gameMap);
		return gameMap;
	}
	
	
	private void removeOutOfSaleLimitRetailers(Map<Integer , IncentiveReportBean> reportingInfoMap,String grtrThnAmt , String lssThnAmt,boolean checkLimitsBoth ,boolean checkLimitsGtr ,boolean checkLimitsLess){
		
		double grtAmt=0.0;
		double lessAmt=0.0;
		List<Integer> retailersToBeRetainedList=new ArrayList<Integer>();;
		if(checkLimitsBoth){
			grtAmt=Double.parseDouble(grtrThnAmt.trim());
			lessAmt=Double.parseDouble(lssThnAmt.trim());
		}else if(checkLimitsGtr){
			grtAmt=Double.parseDouble(grtrThnAmt.trim());
		}else if(checkLimitsLess){
			lessAmt=Double.parseDouble(lssThnAmt.trim());
		}
		
		for(Map.Entry<Integer, IncentiveReportBean> reportingInfoMapEntry : reportingInfoMap.entrySet()){
			double total=0.0;
			boolean isRetain=false;
			int retailerOrgId=reportingInfoMapEntry.getKey();
			Map<String, CompleteCollectionBean> gameBeanMap=reportingInfoMapEntry.getValue().getGameBeanMap();
			for(Map.Entry<String, CompleteCollectionBean> gameBeanMapEntry: gameBeanMap.entrySet()){
				total+=gameBeanMapEntry.getValue().getDrawSale();
			}
			if(checkLimitsBoth){
				if(total>grtAmt && total<lessAmt)
					isRetain=true;
			}else if(checkLimitsGtr){
				if(total>grtAmt)
					isRetain=true;
			}else if(total<lessAmt){
					isRetain=true;
			}
			
			reportingInfoMap.get(retailerOrgId).setGameTotal(reportingInfoMap.get(retailerOrgId).getGameTotal()+ total);
			
			if(isRetain)
				retailersToBeRetainedList.add(retailerOrgId);

		}
		if(checkLimitsBoth || checkLimitsGtr || checkLimitsLess){
		Set<Integer> retailerListSet=reportingInfoMap.keySet();
		logger.info("Terminate Retailer List:: " + retailersToBeRetainedList);
		retailerListSet.retainAll(retailersToBeRetainedList);
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static LinkedHashMap sortByComparator(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());
		if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")){
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Comparable) ((Map.Entry<Integer,IncentiveReportBean>) (o1)).getValue().getAgentOrgId())
	                                       .compareTo(((Map.Entry<Integer,IncentiveReportBean>) (o2)).getValue().getAgentOrgId());
				}
			});
			
		}else{
		//List list = new LinkedList(unsortMap.entrySet());
 		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry<Integer,IncentiveReportBean>) (o1)).getValue().getAgentName().toUpperCase())
                                       .compareTo(((Map.Entry<Integer,IncentiveReportBean>) (o2)).getValue().getAgentName().toUpperCase());
			}
		});
	}
		// put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
		LinkedHashMap sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static void main(String s[]) throws SQLException {
		//fetchIncentiveSchemeAnalysisReport("2", "100,1021", 1.0, 1.0);

	}

}
