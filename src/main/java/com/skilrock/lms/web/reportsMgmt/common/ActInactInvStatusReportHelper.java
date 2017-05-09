package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.ActiveInactiveTerminalReportBean; 
import com.skilrock.lms.beans.ServicesBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.reportsMgmt.common.reportsMgmtUtility;

public class ActInactInvStatusReportHelper {

	Log logger = LogFactory.getLog(ActInactInvStatusReportHelper.class);

	public List<ActiveInactiveTerminalReportBean> getActInactInvStatus(Timestamp startDate, Timestamp endDate ,int reportType, int agentOrgId,String[] cityNamesArray, String creteria, double amount) {
		
		String mainQuery = null;
		String cityAppender = null;
		String queryAppender = null;
		String orgCodeQuery=null;
		String queryOrderAppender=null;
		Map<String,Double> map=null;
		ResultSet rs= null;
		Connection con= null;
		PreparedStatement pstmt = null;
		Timestamp currentDate = null;
		List<ActiveInactiveTerminalReportBean> actInactTerminalBeanList = null;
		ActiveInactiveTerminalReportBean bean = null;
		try {
			orgCodeQuery=QueryManager.getOrgCodeQuery().replace("orgCode", "");
			if(reportType==1){ // ALL 
				cityAppender = " '' name ,dt.city ,";
				queryAppender="";
				queryOrderAppender=QueryManager.getAppendOrgOrder();
			}else if(reportType==2){ // AGENT WISE
				queryAppender = " and parent_id="+agentOrgId;
				cityAppender = " upper( "+orgCodeQuery+"  ) , name ,dt.city , ";
				queryOrderAppender=" retOrgCode ";
			}else if(reportType==3){  // REGION WISE
				if(cityNamesArray==null || "".equals(cityNamesArray))
					throw new LMSException();
				queryAppender = " and city in ("+getCityArray(cityNamesArray)+") ";
				cityAppender = " '' name , dt.city , ";
				queryOrderAppender=QueryManager.getAppendOrgOrder();
			}

			con =  DBConnect.getConnection();

			String gameQuery = "SELECT game_id FROM st_dg_game_master WHERE game_status='OPEN';";
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(gameQuery);
			StringBuilder dgSaleBuilder = new StringBuilder("SELECT organization_id, IFNULL(SUM(mrp_amt),0.0) mrp_amt, IFNULL(SUM(net_amt),0.0) net_amt, transaction_date FROM (");
			int gameId = 0;
			while(rs.next()) {
				gameId = rs.getInt("game_id");
				dgSaleBuilder.append("SELECT rs.retailer_org_id, mrp_amt mrp_amt, net_amt net_amt, transaction_date FROM st_dg_ret_sale_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' ")
						.append("UNION ALL ")
						.append("SELECT rs.retailer_org_id, -mrp_amt mrp_amt, -net_amt net_amt, transaction_date FROM st_dg_ret_sale_refund_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' ")
						.append("UNION ALL ")
						.append("SELECT rs.retailer_org_id, -pwt_amt mrp_amt, -pwt_amt net_amt, transaction_date FROM st_dg_ret_pwt_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND transaction_date>='").append(startDate).append("' AND transaction_date<='").append(endDate).append("' ")
						.append("UNION ALL ");
			}
			dgSaleBuilder.delete(dgSaleBuilder.lastIndexOf(" UNION ALL "), dgSaleBuilder.length()-1);
			
			StringBuilder sleSaleBuilder = new StringBuilder();
			if(ServicesBean.isSLE()){				
				sleSaleBuilder.append(" UNION ALL ")
							  .append("SELECT rs.retailer_org_id, mrp_amt mrp_amt, retailer_net_amt net_amt, rs.transaction_date FROM st_sle_ret_sale  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND rs.transaction_date>='").append(startDate).append("' AND rs.transaction_date<='").append(endDate).append("' ")
							  .append("UNION ALL ")
							  .append("SELECT rs.retailer_org_id, -mrp_amt mrp_amt, -retailer_net_amt net_amt, rs.transaction_date FROM st_sle_ret_sale_refund  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND rs.transaction_date>='").append(startDate).append("' AND rs.transaction_date<='").append(endDate).append("' ")
							  .append("UNION ALL ")
							  .append("SELECT rs.retailer_org_id, -pwt_amt mrp_amt, -pwt_amt net_amt, rs.transaction_date FROM st_sle_ret_pwt  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND rs.transaction_date>='").append(startDate).append("' AND rs.transaction_date<='").append(endDate).append("' ");
			}else{
				sleSaleBuilder.append("");
			}
			dgSaleBuilder.append(sleSaleBuilder);
			String limitAppender = null;
			if("GT".equals(creteria)) {
				limitAppender = ">=";
			} else if("EQ".equals(creteria)) {
				limitAppender = "=";
			} else if("LT".equals(creteria)) {
				limitAppender = "<=";
			}

			if(reportType == 1) {
				dgSaleBuilder.append(")main RIGHT JOIN st_lms_organization_master slom ON main.retailer_org_id=slom.organization_id WHERE slom.organization_status<>'TERMINATE' AND organization_type='RETAILER' GROUP BY slom.organization_id HAVING mrp_amt").append(limitAppender).append(amount).append(";");
			} else if (reportType == 2) {
				dgSaleBuilder.append(")main RIGHT JOIN st_lms_organization_master slom ON slom.organization_id=main.retailer_org_id WHERE parent_id=").append(agentOrgId).append(" AND slom.organization_status<>'TERMINATE' AND organization_type='RETAILER' GROUP BY retailer_org_id HAVING mrp_amt").append(limitAppender).append(amount).append(";");
			} else if (reportType == 3) {
				dgSaleBuilder.append(")main RIGHT JOIN st_lms_organization_master slom ON slom.organization_id=main.retailer_org_id WHERE city IN (").append(getCityArray(cityNamesArray)).append(") AND slom.organization_status<>'TERMINATE' AND organization_type='RETAILER' GROUP BY retailer_org_id HAVING mrp_amt").append(limitAppender).append(amount).append(";");
			}
			
		
			rs = stmt.executeQuery(dgSaleBuilder.toString());
			map=new HashMap<String,Double>();
			
			while(rs.next()) {
			
				map.put(rs.getString("organization_id"),rs.getDouble("mrp_amt"));
				
			}
             
            
            
             /*

             select  '' name ,dt.city , dt.area_code,dt.area_name,if(dg_last_sale_time is null ,registration_date,dg_last_sale_time)  dg_last_sale_time,upper( concat(org_code,'_',name)    ) orgCode  , orgCode retOrgCode, dt.organization_id from (select  city ,am.area_code,area_name,upper( concat(org_code,'_',name)    ) orgCode , parent_id,dg_last_sale_time,registration_date, om.organization_id from st_lms_organization_master om inner join  st_lms_ret_offline_master rom inner join st_lms_user_master um inner join st_lms_area_master am on om.organization_id=rom.organization_id and rom.organization_id =um.organization_id and am.area_code=om.area_code where  organization_status != 'TERMINATE'  and registration_date<='2014-11-14 23:59:59.0' and om.organization_type='RETAILER' ) dt , st_lms_organization_master om where dt.parent_id=om.organization_id order by orgCode ASC 


              */
			mainQuery = "select "+cityAppender+" dt.area_code,dt.area_name,if(dg_last_sale_time is null ,registration_date,dg_last_sale_time)  dg_last_sale_time,upper("+orgCodeQuery+") orgCode  , orgCode retOrgCode, dt.organization_id from (select  city ,am.area_code,area_name,upper("+orgCodeQuery+") orgCode , parent_id,dg_last_sale_time,registration_date, om.organization_id from st_lms_organization_master om inner join  st_lms_ret_offline_master rom inner join st_lms_user_master um inner join st_lms_area_master am on om.organization_id=rom.organization_id and rom.organization_id =um.organization_id and am.area_code=om.area_code where  organization_status != 'TERMINATE'  and registration_date<=? and om.organization_type='RETAILER' "+queryAppender+") dt , st_lms_organization_master om where dt.parent_id=om.organization_id order by "+queryOrderAppender;
			pstmt =  con.prepareStatement(mainQuery);
			pstmt.setTimestamp(1, endDate);
			rs =  pstmt.executeQuery();

			currentDate = new Timestamp(System.currentTimeMillis());
			actInactTerminalBeanList = new ArrayList<ActiveInactiveTerminalReportBean>();
			
			while(rs.next()){
			
				bean = new ActiveInactiveTerminalReportBean();
				bean.setAgentId(rs.getString("orgCode"));
				bean.setRetId(rs.getString("retOrgCode"));
				bean.setIdleTime(calculateTimeDiff(rs.getTimestamp("dg_last_sale_time") ,currentDate));
				bean.setRegion(rs.getString("city"));
				bean.setAgentName(rs.getString("name"));
				bean.setZone(rs.getString("area_name"));
				if(map.containsKey(rs.getString("organization_id"))) {
					bean.setSaleAmount(map.get(rs.getString("organization_id")));	
					actInactTerminalBeanList.add(bean);
				}
			}
		}catch (LMSException e) {
			logger.error("City came Blank");
		} 
		catch (Exception e) {
			logger.error("ERROR");
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return actInactTerminalBeanList;
	}

	
	public List<ActiveInactiveTerminalReportBean> getActInactInvStatus(Timestamp startDate,String[] cityNamesArray, String creteria, double amount) {
		
		String mainQuery = null;
		String orgCodeQuery=null;
		String queryOrderAppender=null;
		
		ResultSet rs= null;
		Connection con= null;
		PreparedStatement pstmt = null;
		List<ActiveInactiveTerminalReportBean> actInactTerminalBeanList = null;
		Map<String,Double> map=null;
		try {
			con =  DBConnect.getConnection();
			String limitAppender = null;
			if("GT".equals(creteria)) {
				limitAppender = ">=";
			} else if("EQ".equals(creteria)) {
				limitAppender = "=";
			} else if("LT".equals(creteria)) {
				limitAppender = "<=";
			}
			String gameQuery = "SELECT game_id FROM st_dg_game_master WHERE game_status='OPEN';";
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(gameQuery);
			StringBuilder dgSaleBuilder = new StringBuilder("SELECT organization_id, parent_id, IFNULL(SUM(mrp_amt),0.0) mrp_amt, IFNULL(SUM(net_amt),0.0) net_amt, transaction_date FROM (");
			int gameId = 0;
			while(rs.next()) {
				gameId = rs.getInt("game_id");
				dgSaleBuilder.append("SELECT rs.retailer_org_id, mrp_amt mrp_amt, net_amt net_amt, transaction_date FROM st_dg_ret_sale_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(transaction_date)=DATE('").append(startDate).append("') ")
						.append("UNION ALL ")
						.append("SELECT rs.retailer_org_id, -mrp_amt mrp_amt, -net_amt net_amt, transaction_date FROM st_dg_ret_sale_refund_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(transaction_date)=DATE('").append(startDate).append("') ")
						.append("UNION ALL ")
						.append("SELECT rs.retailer_org_id, -pwt_amt mrp_amt, -pwt_amt net_amt, transaction_date FROM st_dg_ret_pwt_").append(gameId).append(" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(transaction_date)=DATE('").append(startDate).append("') ")
						.append("UNION ALL ");
			}
			dgSaleBuilder.delete(dgSaleBuilder.lastIndexOf(" UNION ALL "), dgSaleBuilder.length()-1);
			StringBuilder sleSaleBuilder = new StringBuilder();
			if(ServicesBean.isSLE()){				
				sleSaleBuilder.append(" UNION ALL ")
							  .append("SELECT rs.retailer_org_id, mrp_amt mrp_amt, retailer_net_amt net_amt, rs.transaction_date FROM st_sle_ret_sale  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(rs.transaction_date)=DATE('").append(startDate).append("') ")
							  .append("UNION ALL ")
							  .append("SELECT rs.retailer_org_id, -mrp_amt mrp_amt, -rtailer_net_amt net_amt, rs.transaction_date FROM st_sle_ret_sale_refund  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(rs.transaction_date)=DATE('").append(startDate).append("') ")
							  .append("UNION ALL ")
							  .append("SELECT rs.retailer_org_id, -pwt_amt mrp_amt, -pwt_amt net_amt, rs.transaction_date FROM st_sle_ret_pwt  rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id AND DATE(rs.transaction_date)=DATE('").append(startDate).append("') ");
			}else{
				sleSaleBuilder.append("");
			}
			
			dgSaleBuilder.append(sleSaleBuilder)
						 .append(")main RIGHT JOIN st_lms_organization_master slom ON slom.organization_id=retailer_org_id WHERE organization_status<>'TERMINATE' AND organization_type='RETAILER' GROUP BY parent_id HAVING mrp_amt").append(limitAppender).append(amount).append(";");
			List<String> list = new ArrayList<String>();
			rs = stmt.executeQuery(dgSaleBuilder.toString());
			map=new HashMap<String,Double>();
	
			while(rs.next()) {
		
				map.put(rs.getString("organization_id"),rs.getDouble("mrp_amt"));
				list.add(rs.getString("parent_id"));
			}
			
             
            
			orgCodeQuery=QueryManager.getOrgCodeQuery().replace("orgCode", "");
			queryOrderAppender=QueryManager.getAppendOrgOrder();

			mainQuery = "select organization_id, city,am.area_code,am.area_name, upper("+orgCodeQuery+") orgCode ,(assigned_total+notAssigned_total ) totalPos , active_Ret activePos,  ((assigned_total+notAssigned_total )-active_Ret) inActivePos from st_lms_ret_activityHistory_agentwise aht inner join  st_lms_organization_master om inner join st_lms_area_master am on organization_id = agent_id and om.area_code=am.area_code where date =date(?) and city in ("+(cityNamesArray!=null?getCityArray(cityNamesArray):"select city_name from st_lms_city_master")+")  order by " + queryOrderAppender;
			pstmt =  con.prepareStatement(mainQuery);
			pstmt.setTimestamp(1, startDate);
			rs =  pstmt.executeQuery();
			
			actInactTerminalBeanList = new ArrayList<ActiveInactiveTerminalReportBean>();
			while(rs.next()){
				ActiveInactiveTerminalReportBean bean = new ActiveInactiveTerminalReportBean();
				bean.setAgentId(rs.getString("orgCode"));
				bean.setActivePos(rs.getInt("activePos"));
				bean.setTotalPos(rs.getInt("totalPos"));
				bean.setInActivePos(rs.getInt("inActivePos"));
				bean.setRegion(rs.getString("city"));
				bean.setZone(rs.getString("area_name"));
				if(list.contains(rs.getString("organization_id"))) {
					bean.setSaleAmount(map.get(rs.getString("oganization_id")));
					actInactTerminalBeanList.add(bean);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return actInactTerminalBeanList;
	}
	
	private String calculateTimeDiff(Timestamp lastActiveDate , Timestamp currentDate){
		long diff = currentDate.getTime() - lastActiveDate.getTime();
	    long diffSeconds = diff / 1000 % 60;
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000);
	    //logger.info(diffHours+":"+diffMinutes+":"+diffSeconds);
		return diffHours+":"+makeMinAndSecFormat(diffMinutes)+":"+makeMinAndSecFormat(diffSeconds);
	}
	
	private String makeMinAndSecFormat(long value){
		return (value/10>0?value+"":"0"+value);
	}
	
	public String cityAndStateBuilder(){

		ResultSet rs= null;
 		Connection con= null;
		PreparedStatement pstmt = null;
		TreeMap<String, String> map = null;
		try {
			con =  DBConnect.getConnection();
			pstmt =  con.prepareStatement("select sm.state_code ,city_code, name state_name, city_name from st_lms_state_master sm inner join st_lms_city_master cm on sm.state_code=cm.state_code order by state_code");
			rs =  pstmt.executeQuery();
			map = new TreeMap<String, String>();
			while(rs.next())
			{
				String cityBuilder = "";
					if(map.containsKey(rs.getString("state_name"))){
						cityBuilder += map.get(rs.getString("state_name"))+rs.getString("city_name")+"," ;
						map.put(rs.getString("state_name"), cityBuilder);
					}else{
						map.put(rs.getString("state_name"), rs.getString("city_name")+",");
					}
			}
		} catch (Exception e) {
			logger.error(e);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return map.toString().replace(",,", "|").replace("{","").replace("}", "");
	}
	
	public String getAgentList(Timestamp endDate) throws LMSException{
		
		StringBuilder orgList = null;
		String orgCodeAppender = null;
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt =null;
		try {
			orgCodeAppender = QueryManager.getOrgCodeQuery().replace("orgCode", "");
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("select om.organization_id, upper("+orgCodeAppender+") orgCode  from st_lms_organization_master om inner join st_lms_user_master um  on om.organization_id = um.organization_id where om.organization_type= 'AGENT'  and organization_status <> 'TERMINATE' and registration_date<=? order by "+QueryManager.getAppendOrgOrder());
			pstmt.setTimestamp(1,endDate);
			rs = pstmt.executeQuery();
			orgList = new StringBuilder();
			while (rs.next()) 
				orgList.append(rs.getString("organization_id")).append("|").append(rs.getString("orgCode")).append(":"); 
			
			logger.info("orgList-------" + orgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return orgList.toString();
	}
	
	private String getCityArray(String [] cityNamesArray){
		StringBuilder drawIds=new StringBuilder();
		for(int i=0; i<cityNamesArray.length ;i++){
			drawIds.append("'").append(cityNamesArray[i]).append("',");	
		}
		return drawIds.replace(drawIds.lastIndexOf(","), drawIds.length(), "").toString();
	}
}