package com.skilrock.lms.web.scratchService.customerSpecificReport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;


public class TicketByTicketSalePwtDao {
  
	public static Map<String, Map<Integer, TicketByTicketSalePwt>>  ticketByTicketSaleRetailerWiseForAllAgent(Timestamp startDate, Timestamp endDate, Connection con, int roleId) throws LMSException{
		Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		try {	
			//String agentOrgId=CommonMethods.appendRoleAgentMappingQueryForJoin(roleId);
			/*String fetchQuery = "SELECT game_name, user_name, sum(sale), game_id, ret_org_id FROM ( "
					+ " SELECT gm.game_name, usr.user_name, (sum(setkt.ticket_price)) sale, gm.game_id, setkt.ret_org_id  FROM st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'SOLD' "
					+ " AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id IN "+ agentOrgId +")"
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' AND usr.role_id = 3 AND usr.isrolehead='Y' group by ret_user_id, game_name "
					+ " UNION ALL "
					+ " SELECT gm.game_name, usr.user_name, (-sum(setkt.ticket_price)) sale, gm.game_id, setkt.ret_org_id  FROM st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'UNSOLD' "
					+ " AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id IN "+ agentOrgId +" ) "
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"'  AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id, game_name"
					+ ")main group by ret_org_id, game_name ";*/
			String fetchQuery ="SELECT game_name,NAME,SUM(IF(setkt.ticket_status='SOLD',setkt.ticket_price,-setkt.ticket_price)) totalprice ,gm.game_id,organization_id FROM st_se_ticket_by_ticket_sale_txn setkt INNER JOIN st_lms_organization_master ON ret_org_id = organization_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =parent_id INNER JOIN st_se_game_master gm ON gm.game_id = setkt.game_id WHERE    agtMap.role_id = "+roleId+"  AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' GROUP BY ret_org_id,gm.game_id";
			
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    extractingSaleData(mapOrgWiseTicketByTicket, rs);	  
			
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return mapOrgWiseTicketByTicket;		
	}
	
	public static  Map<String, Map<Integer, TicketByTicketSalePwt>>  ticketByTicketSaleRetailerWiseForSingleAgent(Integer agentOrgId, Timestamp startDate, Timestamp endDate, Connection con) throws LMSException{
		Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		try {
			/*String fetchQuery = "SELECT game_name, user_name, sum(sale), game_id, ret_org_id FROM ( "
					+ " SELECT gm.game_name, usr.user_name, (sum(setkt.ticket_price)) sale, gm.game_id, setkt.ret_org_id  FROM st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'SOLD' "
					+ " AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id = "+ agentOrgId +")"
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' AND usr.role_id = 3 AND usr.isrolehead='Y' group by ret_user_id, game_name "
					+ " UNION ALL "
					+ " SELECT gm.game_name, usr.user_name, (-sum(setkt.ticket_price)) sale, gm.game_id, setkt.ret_org_id  FROM st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'UNSOLD' "
					+ " AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id = "+ agentOrgId +" ) "
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"'  AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id, game_name"
					+ ")main group by ret_org_id, game_name ";*/
			String fetchQuery ="SELECT game_name,NAME,SUM(IF(setkt.ticket_status='SOLD',setkt.ticket_price,-setkt.ticket_price)) totalprice ,gm.game_id,organization_id FROM st_se_ticket_by_ticket_sale_txn setkt INNER JOIN st_lms_organization_master ON ret_org_id = organization_id INNER JOIN st_se_game_master gm ON gm.game_id = setkt.game_id WHERE    parent_id = "+agentOrgId+"  AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' GROUP BY ret_org_id,gm.game_id";

			System.out.println("fetchQuery"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    extractingSaleData(mapOrgWiseTicketByTicket, rs);
			
			
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return mapOrgWiseTicketByTicket;		
	}
	
	public static Map<String, Map<Integer, TicketByTicketSalePwt>> ticketByTicketSaleForAllAgentWise(Timestamp startDate, Timestamp endDate, Connection con,int roleId) throws LMSException{
		Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		try {
			
		//	String agentOrgId=CommonMethods.appendRoleAgentMappingQueryForJoin( roleId);
			
			/*String fetchQuery = "SELECT game_name, name, sum(sale), game_id, organization_id from ( "
					+ " SELECT gm.game_name, org.name, (sum(setkt.ticket_price)) sale, gm.game_id, org.organization_id   from st_se_ticket_by_ticket_sale_txn setkt "
					+ " JOIN st_lms_organization_master org ON org.organization_id IN "+ agentOrgId +" "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  "
					+ " JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  "
					+ " where ticket_status = 'SOLD' AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id IN "+ agentOrgId +") AND "
					+ " sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id,org.organization_id, game_name "
					+ " UNION  ALL "
					+ " SELECT gm.game_name, org.name, (-sum(setkt.ticket_price)) sale, gm.game_id, org.organization_id from st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_lms_organization_master org ON org.organization_id IN "+ agentOrgId +" "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  "
					+ " JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'UNSOLD' AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id IN "+ agentOrgId +") "
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"'  AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id, game_name "
					+ ")main group by organization_id, game_name  ";*/
			
			String fetchQuery ="SELECT game_name,agtUsr.NAME,SUM(IF(setkt.ticket_status='SOLD',setkt.ticket_price,-setkt.ticket_price)) totalprice ,gm.game_id,agtUsr.organization_id FROM st_se_ticket_by_ticket_sale_txn setkt INNER JOIN st_lms_organization_master retusr ON ret_org_id = retusr.organization_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =parent_id INNER JOIN st_se_game_master gm ON gm.game_id = setkt.game_id INNER JOIN st_lms_organization_master agtUsr ON agtUsr. organization_id= retusr.parent_id  WHERE    agtMap.role_id = "+roleId+"  AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' GROUP BY  agtUsr. organization_id,gm.game_id   " ;
			
			System.out.println("fetchQuery"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    extractingSaleData(mapOrgWiseTicketByTicket, rs);
			
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return mapOrgWiseTicketByTicket;		
	}
	
	public static Map<String, Map<Integer, TicketByTicketSalePwt>> ticketByTicketSaleForSingleAgentWise(Integer agentOrgId, Timestamp startDate, Timestamp endDate, Connection con) throws LMSException{
		Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		try {			
		/*	String fetchQuery = "SELECT game_name, name, sum(sale), game_id, organization_id from ( "
					+ " SELECT gm.game_name, org.name, (sum(setkt.ticket_price)) sale, gm.game_id, org.organization_id   from st_se_ticket_by_ticket_sale_txn setkt "
					+ " JOIN st_lms_organization_master org ON org.organization_id = "+ agentOrgId +" "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  "
					+ " JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  "
					+ " where ticket_status = 'SOLD' AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id = "+ agentOrgId +") AND "
					+ " sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id, game_name "
					+ " UNION  ALL "
					+ " SELECT gm.game_name, org.name, (-sum(setkt.ticket_price)) sale, gm.game_id, org.organization_id from st_se_ticket_by_ticket_sale_txn setkt  "
					+ " JOIN st_lms_organization_master org ON org.organization_id = "+ agentOrgId +" "
					+ " JOIN st_se_game_master gm ON gm.game_id = setkt.game_id  "
					+ " JOIN st_lms_user_master usr ON usr.user_id = setkt.ret_user_id  where ticket_status = 'UNSOLD' AND usr.parent_user_id IN (Select user_id From st_lms_user_master Where organization_id = "+ agentOrgId +") "
					+ " AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"'  AND usr.role_id=3 AND usr.isrolehead='Y' group by ret_user_id, game_name "
					+ ")main group by organization_id, game_name  ";
			*/
			
			String fetchQuery ="SELECT game_name,agtUsr.NAME,SUM(IF(setkt.ticket_status='SOLD',setkt.ticket_price,-setkt.ticket_price)) totalprice ,gm.game_id,agtUsr.organization_id FROM st_se_ticket_by_ticket_sale_txn setkt INNER JOIN st_lms_organization_master retusr ON ret_org_id = retusr.organization_id INNER JOIN st_se_game_master gm ON gm.game_id = setkt.game_id INNER JOIN st_lms_organization_master agtUsr ON agtUsr. organization_id= retusr.parent_id  WHERE    agtUsr.organization_id = "+agentOrgId+"  AND sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' GROUP BY  agtUsr. organization_id,gm.game_id   " ;
			
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    extractingSaleData(mapOrgWiseTicketByTicket, rs);
			
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return mapOrgWiseTicketByTicket;		
	}

	private static void extractingSaleData(Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket, ResultSet rs) throws SQLException {
		Map<Integer, TicketByTicketSalePwt> gameTicketMap = null;
		while (rs.next()) {
			String orgName = rs.getString(2);
			int gameId = rs.getInt(4);
			int orgId = rs.getInt(5);
			
			TicketByTicketSalePwt ticketByTicketSalePwt = new TicketByTicketSalePwt();				
			ticketByTicketSalePwt.setGameName(rs.getString(1));
			ticketByTicketSalePwt.setOrgName(orgName);
			ticketByTicketSalePwt.setSale(rs.getDouble(3));
			ticketByTicketSalePwt.setGameId(gameId);
			ticketByTicketSalePwt.setOrgId(orgId);
			
			if (mapOrgWiseTicketByTicket.containsKey(orgName)) {
				mapOrgWiseTicketByTicket.get(orgName).put(gameId, ticketByTicketSalePwt);
			} else {
				gameTicketMap = new HashMap<Integer, TicketByTicketSalePwt>();
				gameTicketMap.put(gameId, ticketByTicketSalePwt);	
				mapOrgWiseTicketByTicket.put(orgName, gameTicketMap);
			}
		}
	}
	
	
	public static void getPwtRetailerWise(Map<String, Map<Integer, TicketByTicketSalePwt>> mapTktByTkt, Timestamp startDate, Timestamp endDate, Connection con,int roleId) throws LMSException{
		try {			
			String fetchQuery = "SELECT name, sum(pwt_amt), se.game_id FROM st_se_retailer_pwt se "
					+ " JOIN st_lms_organization_master usr  ON usr.organization_id = se.retailer_org_id "
					+ " JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id "
					+ " INNER JOIN  st_lms_role_agent_mapping ON agent_id=usr.parent_id where transaction_date BETWEEN '"+startDate+"' AND '"+endDate+"'  AND  transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')  AND role_id="+roleId+"  group by name, se.game_id";
			
			
			System.out.println("fetchQuery"+fetchQuery);
			
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {
		    	if(mapTktByTkt.containsKey(rs.getString(1))){
			    	for(Map.Entry<Integer, TicketByTicketSalePwt> map : mapTktByTkt.get(rs.getString(1)).entrySet()){
			    		if(map.getKey() == rs.getInt(3)){
				    		TicketByTicketSalePwt tkt = map.getValue();
					    	tkt.setWinning(rs.getDouble(2));
				    	}
			    	}
		    	}else{
		    		Map<Integer, TicketByTicketSalePwt>   ticketGameMap = new HashMap<Integer, TicketByTicketSalePwt>();
		    		TicketByTicketSalePwt   tkt = new TicketByTicketSalePwt();
			    	tkt.setWinning(rs.getDouble(2));
		    		ticketGameMap.put(rs.getInt(3), tkt);
		    		mapTktByTkt.put(rs.getString(1), ticketGameMap);
		    	}		    				
		    }
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		
	}
	
	public static void getPwtAgentWise(Map<String, Map<Integer, TicketByTicketSalePwt>> mapTktByTkt, Timestamp startDate, Timestamp endDate, Connection con,int roleId) throws LMSException{
		try {
		/*	String fetchQuery = "SELECT name, sum(pwt), game_id from ("
		    		+ " SELECT agusr.name, sum(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id  "
		    		+ " WHERE transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' group by agusr.name, se.game_id "
		    		+ " UNION ALL "
		    		+ " SELECT agusr.name, sum(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se  "
		    		+ " JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id  "
		    		+ " JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id  "
		    		+ " where transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')  group by usr.parent_id, se.game_id "
		    		+ " )main group by name, game_id";*/
			
			String fetchQuery ="SELECT NAME, SUM(pwt), game_id FROM ( SELECT agusr.name, SUM(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se   JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =agusr.organization_id WHERE transaction_date BETWEEN "
					+ "  '"+startDate+"' AND '"+endDate+"'  AND  agtMap.role_id="+roleId+" GROUP BY agusr.name, se.game_id  UNION ALL  SELECT agusr.name, SUM(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se   JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id   JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id   JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =agusr.organization_id WHERE transaction_date BETWEEN   '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')    AND agtMap.role_id="+roleId+" GROUP BY  agusr.organization_id , se.game_id  )main GROUP BY NAME, game_id" ;
			
			
			System.out.println("fetchQuerypwt"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {	
		    	if(mapTktByTkt.containsKey(rs.getString(1))){
			    	for(Map.Entry<Integer, TicketByTicketSalePwt> map : mapTktByTkt.get(rs.getString(1)).entrySet()){
			    		if(map.getKey() == rs.getInt(3)){
				    		TicketByTicketSalePwt tkt = map.getValue();
					    	tkt.setWinning(rs.getDouble(2));
				    	}
			    	}
		    	}		    				
		    }
		    
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	public static Map<String, Map<Integer, TicketByTicketSalePwt>> ticketByTicketSaleForAllAgentRegionalOfficeWise(Timestamp startDate, Timestamp endDate, Connection con,int roleId) throws LMSException{
		Map<String, Map<Integer, TicketByTicketSalePwt>> mapOrgWiseTicketByTicket = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		try {
			
		//	String agentOrgId1=CommonMethods.appendRoleAgentMappingQueryForJoin( roleId);
			
			String fetchQuery = "SELECT gm.game_name,user_name,SUM(IF(setkt.ticket_status='SOLD',setkt.ticket_price,-setkt.ticket_price)) totalprice ,gm.game_id,agtMap.role_id FROM st_se_ticket_by_ticket_sale_txn setkt INNER JOIN st_lms_organization_master ON ret_org_id = organization_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =parent_id INNER JOIN st_se_game_master gm ON gm.game_id = setkt.game_id INNER JOIN st_lms_user_master um ON    um.role_id= agtMap.role_id WHERE   sale_time BETWEEN '"+startDate+"' AND '"+endDate+"' AND  agtMap.role_id!=1  AND um.isrolehead='Y'  GROUP BY agtMap.role_id,gm.game_id";
			
			System.out.println("fetchQuery"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    extractingSaleData(mapOrgWiseTicketByTicket, rs);
		  
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return mapOrgWiseTicketByTicket;		
	}
	public static Map<Integer, String> getGameMap() throws LMSException{
		Connection con =  DBConnect.getConnection();
		Map<Integer, String> gameMap = new HashMap<Integer, String>();
		try {
			String fetchQuery = "select game_id, game_name from st_se_game_master where game_status = 'OPEN'";
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {
		    	gameMap.put(rs.getInt(1), rs.getString(2));
		    }
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameMap;
	}
	public static void getPwtAgentRegionalOfficeWise(Map<String, Map<Integer, TicketByTicketSalePwt>> mapTktByTkt, Timestamp startDate, Timestamp endDate, Connection con) throws LMSException{
		try {
		/*	String fetchQuery = "SELECT name, sum(pwt), game_id from ("
		    		+ " SELECT agusr.name, sum(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id  "
		    		+ " WHERE transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' group by agusr.name, se.game_id "
		    		+ " UNION ALL "
		    		+ " SELECT agusr.name, sum(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se  "
		    		+ " JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id  "
		    		+ " JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id  "
		    		+ " where transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')  group by usr.parent_id, se.game_id "
		    		+ " )main group by name, game_id";
			*/
			String fetchQuery ="SELECT user_name, SUM(pwt), game_id FROM ( SELECT um.user_name,agtMap.role_id, SUM(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se   JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =agusr.organization_id INNER JOIN st_lms_user_master um ON um.role_id=agtMap.role_id WHERE transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"'   AND agtMap.role_id!=1 AND isrolehead='Y'   GROUP BY agtMap.role_id, se.game_id UNION ALL  SELECT um.user_name,agtMap.role_id, SUM(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se   JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id   JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id   JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id INNER JOIN st_lms_role_agent_mapping agtMap ON agent_id =agusr.organization_id INNER JOIN st_lms_user_master um ON um.role_id=agtMap.role_id WHERE transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')     AND agtMap.role_id!=1 AND isrolehead='Y'    GROUP BY agtMap.role_id, se.game_id UNION ALL SELECT   user_name,role_id,SUM(pwt_amt) pwt,game_id FROM st_se_direct_player_pwt  pwtTickets  INNER JOIN st_lms_bo_transaction_master  stm ON stm.transaction_id=pwtTickets.transaction_id INNER JOIN st_lms_user_master um ON  stm.user_id=um.user_id  WHERE  stm.`transaction_date`  BETWEEN  '"+startDate+"' AND '"+endDate+"' AND isrolehead='Y'  GROUP BY role_id, game_id)main GROUP BY role_id, game_id";
			

			System.out.println("fetchQuerypwt"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {	
		    	if(mapTktByTkt.containsKey(rs.getString(1))){
			    	for(Map.Entry<Integer, TicketByTicketSalePwt> map : mapTktByTkt.get(rs.getString(1)).entrySet()){
			    		if(map.getKey() == rs.getInt(3)){
				    		TicketByTicketSalePwt tkt = map.getValue();
					    	tkt.setWinning(rs.getDouble(2));
				    	}
			    	}
		    	}		    				
		    }
		    
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public static void getPwtForAgent(Map<String, Map<Integer, TicketByTicketSalePwt>> mapTktByTkt, Timestamp startDate, Timestamp endDate, Connection con,int agentOrgId) throws LMSException{
		try {
		/*	String fetchQuery = "SELECT name, sum(pwt), game_id from ("
		    		+ " SELECT agusr.name, sum(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id  "
		    		+ " WHERE transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' group by agusr.name, se.game_id "
		    		+ " UNION ALL "
		    		+ " SELECT agusr.name, sum(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se  "
		    		+ " JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id  "
		    		+ " JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id  "
		    		+ " JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id  "
		    		+ " where transaction_date BETWEEN  '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')  group by usr.parent_id, se.game_id "
		    		+ " )main group by name, game_id";*/
			String fetchQuery ="SELECT NAME, SUM(pwt), game_id FROM ( SELECT agusr.name, SUM(se.pwt_amt) pwt, se.game_id FROM st_se_agt_direct_player_pwt se   JOIN st_lms_organization_master agusr ON agusr.organization_id = se.agent_org_id  WHERE transaction_date BETWEEN "
					+ "  '"+startDate+"' AND '"+endDate+"'  AND se.agent_org_id ="+agentOrgId+"  GROUP BY agusr.name, se.game_id  UNION ALL  SELECT agusr.name, SUM(pwt_amt) pwt, se.game_id FROM st_se_retailer_pwt se   JOIN st_lms_organization_master usr ON usr.organization_id = se.retailer_org_id   JOIN st_lms_organization_master agusr ON agusr.organization_id = usr.parent_id   JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id  WHERE transaction_date BETWEEN   '"+startDate+"' AND '"+endDate+"' AND transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')    AND agusr.organization_id="+agentOrgId+" GROUP BY  agusr.organization_id , se.game_id  )main GROUP BY NAME, game_id" ;
			
			System.out.println("fetchQuerypwt"+fetchQuery);
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {	
		    	if(mapTktByTkt.containsKey(rs.getString(1))){
			    	for(Map.Entry<Integer, TicketByTicketSalePwt> map : mapTktByTkt.get(rs.getString(1)).entrySet()){
			    		if(map.getKey() == rs.getInt(3)){
				    		TicketByTicketSalePwt tkt = map.getValue();
					    	tkt.setWinning(rs.getDouble(2));
				    	}
			    	}
		    	}		    				
		    }
		    
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	public static void getPwtForSingleAgentRetailerWise(Map<String, Map<Integer, TicketByTicketSalePwt>> mapTktByTkt, Timestamp startDate, Timestamp endDate, Connection con,int agentOrgId) throws LMSException{
		try {			
			String fetchQuery = "SELECT name, sum(pwt_amt), se.game_id FROM st_se_retailer_pwt se "
					+ " JOIN st_lms_organization_master usr  ON usr.organization_id = se.retailer_org_id "
					+ " JOIN st_lms_retailer_transaction_master txn ON txn.transaction_id = se.transaction_id "
					+ " INNER JOIN  st_lms_role_agent_mapping ON agent_id=usr.parent_id where transaction_date BETWEEN '"+startDate+"' AND '"+endDate+"'  AND  transaction_type IN ('PWT', 'PWT_AUTO', 'PWT_PLR')  AND agent_id="+agentOrgId+"  AND role_id!=1 group by name, se.game_id";
			
			
			System.out.println("fetchQuery"+fetchQuery);
			
			PreparedStatement ps  = con.prepareStatement(fetchQuery);
		    ResultSet rs =	ps.executeQuery();
		    while (rs.next()) {
		    	if(mapTktByTkt.containsKey(rs.getString(1))){
			    	for(Map.Entry<Integer, TicketByTicketSalePwt> map : mapTktByTkt.get(rs.getString(1)).entrySet()){
			    		if(map.getKey() == rs.getInt(3)){
				    		TicketByTicketSalePwt tkt = map.getValue();
					    	tkt.setWinning(rs.getDouble(2));
				    	}
			    	}
		    	}else{
		    		Map<Integer, TicketByTicketSalePwt>   ticketGameMap = new HashMap<Integer, TicketByTicketSalePwt>();
		    		TicketByTicketSalePwt   tkt = new TicketByTicketSalePwt();
			    	tkt.setWinning(rs.getDouble(2));
		    		ticketGameMap.put(rs.getInt(3), tkt);
		    		mapTktByTkt.put(rs.getString(1), ticketGameMap);
		    	}		    				
		    }
		} catch (Exception e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		
	}
}
