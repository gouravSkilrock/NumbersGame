package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean;
import com.skilrock.lms.common.db.DBConnect;


public class TicketByTicketSaleTxnReportDaoImpl {

	static Log logger = LogFactory.getLog(TicketByTicketSaleTxnReportDaoImpl.class);
	public static Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportForRetailerTicketByTktTxn(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId, String organizationType) {
		PreparedStatement pstmt = null;
		ResultSet retailerForCurrentAgent = null;
		Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> finalGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>>();
		Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>> gameNameMap = null;
		
		if (startDate.after(endDate)) {
			return finalGameAgtMap;
		}
		
		try{
			String agtOrgQry=null;
			
			if("RETAILER".equalsIgnoreCase(organizationType)){
				agtOrgQry = "select "
						+ "name as retailerName , organization_id from st_lms_organization_master where organization_id="
						+ agtOrgId;
			}else{
				agtOrgQry = "select "
					+ "name as retailerName , organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by retailerName ";
			}
					
			pstmt = con.prepareStatement(agtOrgQry);
			retailerForCurrentAgent = pstmt.executeQuery();
			
			gameNameMap = new LinkedHashMap<String, Map<String, AgentWiseTktByTktSaleTxnBean>>();
			
			String totalOpenGame = "Select game_id , game_name from st_se_game_master where game_status = 'OPEN' ";
			
			pstmt = con.prepareStatement(totalOpenGame);
			
			ResultSet rsGames = pstmt.executeQuery();
			
			while(rsGames.next()){
				int game_id = rsGames.getInt("game_id");
				Map<String, AgentWiseTktByTktSaleTxnBean> gameMap = new LinkedHashMap<String, AgentWiseTktByTktSaleTxnBean>();
				
				gameMap.putAll(getAgentMap(retailerForCurrentAgent));
				
				ResultSet salePerGame = getTotalSale(startDate,endDate,game_id,agtOrgId,con, organizationType);
				while (salePerGame.next()) {
					String agtId = salePerGame.getString("organization_id");
					gameMap.get(agtId).setScratchGameWiseSale(salePerGame.getDouble("sale"));
				}
				ResultSet pwtPerGame = getTotalPwt(startDate,endDate,game_id,agtOrgId,con, organizationType);
				while (pwtPerGame.next()) {
					String agtId = pwtPerGame.getString("organization_id");
					gameMap.get(agtId).setScratchGameWiseWinning(pwtPerGame.getDouble("pwt"));
				}
				gameNameMap.put(rsGames.getString("game_name"), gameMap);
				
			}
			DBConnect.closePstmt(pstmt);
			finalGameAgtMap.put("SE", gameNameMap);
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalGameAgtMap;
	}
	
	private static ResultSet getTotalPwt(Timestamp startDate, Timestamp endDate, int game_id, int agtOrgId, Connection con, String organizationType) {
		
		ResultSet retailerWisepwt = null;
		String query = null;
		
		if("RETAILER".equalsIgnoreCase(organizationType)){
			query = "select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join "
					+ " (select rp.retailer_org_id ,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt"
					+ " from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id "
					+ " where transaction_date >= '"+startDate
			 		+ "' and transaction_date <= '"+ endDate
			 		+ "' and  transaction_type='PWT' and rp.game_id="+game_id
			 		+ " group by rp.retailer_org_id) pwt on organization_id=retailer_org_id "
					+ " where organization_id="+agtOrgId
			 		+ " and organization_type='RETAILER'" ;
		}else{
			query = "select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join "
				+ " (select rp.retailer_org_id ,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt"
				+ " from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id "
				+ " where transaction_date >= '"+startDate
		 		+ "' and transaction_date <= '"+ endDate
		 		+ "' and  transaction_type='PWT' and rp.game_id="+game_id
		 		+ " group by rp.retailer_org_id) pwt on organization_id=retailer_org_id "
				+ " where parent_id="+ agtOrgId
		 		+ " and organization_type='RETAILER'" ;
		}
		
		   System.out.println("pwtquery"+query);
		try{
			 PreparedStatement pstmt = con.prepareStatement(query);
			 retailerWisepwt = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
		e.printStackTrace();
		}
		return retailerWisepwt;
	}

	private static ResultSet getTotalSale(Timestamp startDate, Timestamp endDate, int game_id, int agtOrgId, Connection con, String organizationType) {
		
	     ResultSet retailerWiseSale = null;
	     String query = null;
	     if("RETAILER".equalsIgnoreCase(organizationType.trim())){
	    	 
	    	 query = "select main.game_name,retailerName,organization_id,game_id,sum(sale) as sale from (Select gm.game_name, um.user_name retailerName, "
	 	     		+ "um.organization_id, setkt.game_id ,(sum(setkt.ticket_price)) sale from st_se_ticket_by_ticket_sale_txn setkt "
	 	     		+ "INNER JOIN st_se_game_master gm on gm.game_id = setkt.game_id INNER JOIN st_lms_user_master um ON um.organization_id = setkt.ret_org_id "
	 	     		+ "where ticket_status = 'SOLD' And sale_time >= '" + startDate
	 		 		+ "' and sale_time <= '"+ endDate +"' and setkt.game_id ="+ game_id
	 		 		+ " AND um.user_id = (Select user_id From st_lms_user_master Where organization_id = " + agtOrgId
	 		 		+ " and isrolehead='Y') and um.role_id = 3 "
	 	     		+ "group by ret_user_id,game_id "
	 	     		+ "union all"
	 	     		+ " Select gm.game_name, um.user_name retailerName, um.organization_id, setkt.game_id "
	 	     		+ ",(sum(-setkt.ticket_price)) sale from st_se_ticket_by_ticket_sale_txn setkt "
	 	     		+ "INNER JOIN st_se_game_master gm on gm.game_id = setkt.game_id INNER JOIN st_lms_user_master um ON "
	 	     		+ "um.organization_id = setkt.ret_org_id where ticket_status = 'UNSOLD' And sale_time >= '"+ startDate
	 		 		+ "' and"
	 	     		+ " sale_time <= '"+ endDate +"'  and setkt.game_id = "+ game_id
	 		 		+ " AND um.user_id = (Select user_id From st_lms_user_master"
	 	     		+ " Where organization_id =" + agtOrgId
	 		 		+ " and isrolehead='Y') and um.role_id = 3 group by ret_user_id,game_id)main group by organization_id,game_id";
	     }else{
	    	 query = "select main.game_name,retailerName,organization_id,game_id,sum(sale) as sale from (Select gm.game_name, um.user_name retailerName, "
	     		+ "um.organization_id, setkt.game_id ,(sum(setkt.ticket_price)) sale from st_se_ticket_by_ticket_sale_txn setkt "
	     		+ "INNER JOIN st_se_game_master gm on gm.game_id = setkt.game_id INNER JOIN st_lms_user_master um ON um.organization_id = setkt.ret_org_id "
	     		+ "where ticket_status = 'SOLD' And sale_time >= '" + startDate
		 		+ "' and sale_time <= '"+ endDate +"' and setkt.game_id ="+ game_id
		 		+ " AND um.parent_user_id = (Select user_id From st_lms_user_master Where organization_id = " + agtOrgId
		 		+ " and isrolehead='Y') and um.role_id = 3 "
	     		+ "group by ret_user_id,game_id "
	     		+ "union all"
	     		+ " Select gm.game_name, um.user_name retailerName, um.organization_id, setkt.game_id "
	     		+ ",(sum(-setkt.ticket_price)) sale from st_se_ticket_by_ticket_sale_txn setkt "
	     		+ "INNER JOIN st_se_game_master gm on gm.game_id = setkt.game_id INNER JOIN st_lms_user_master um ON "
	     		+ "um.organization_id = setkt.ret_org_id where ticket_status = 'UNSOLD' And sale_time >= '"+ startDate
		 		+ "' and"
	     		+ " sale_time <= '"+ endDate +"'  and setkt.game_id = "+ game_id
		 		+ " AND um.parent_user_id = (Select user_id From st_lms_user_master"
	     		+ " Where organization_id =" + agtOrgId
		 		+ " and isrolehead='Y') and um.role_id = 3 group by ret_user_id,game_id)main group by organization_id,game_id";
	     }
	     
	     System.out.println("salewquery"+query);
		try{
			 PreparedStatement pstmt = con.prepareStatement(query);
			 retailerWiseSale = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
		e.printStackTrace();
		}
		  
		return retailerWiseSale;
	}

	private static String getRetailerParentId(int agtOrgId) {
		String retParentId="(select parent_id from st_lms_organization_master where organization_id="+agtOrgId+")";
		return retParentId;
	}

	private static Map<String, AgentWiseTktByTktSaleTxnBean> getAgentMap(ResultSet retailerForCurrentAgent)
			throws SQLException {
		Map<String, AgentWiseTktByTktSaleTxnBean> agtMap = new LinkedHashMap<String, AgentWiseTktByTktSaleTxnBean>();
		AgentWiseTktByTktSaleTxnBean agentBean = null;

		while (retailerForCurrentAgent.next()) {
			agentBean = new AgentWiseTktByTktSaleTxnBean();
			agentBean.setRetailerName(retailerForCurrentAgent.getString("retailerName"));
			agentBean.setScratchGameWiseSale(0.0);
	        agentBean.setScratchGameWiseWinning(0.0);
			agtMap.put(retailerForCurrentAgent.getString("organization_id"), agentBean);
		}
		retailerForCurrentAgent.beforeFirst();
		return agtMap;
	}
	

}
