package com.skilrock.lms.coreEngine.reportsMgmt.drawables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.TitleBeanForDrawables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.common.drawables.CommonMethods;
import com.skilrock.lms.web.common.drawables.PrepareDrawables;

public class GraphicalRepOfActInactInvStatusReportHelper {
	static Log logger = LogFactory.getLog(GraphicalRepOfActInactInvStatusReportHelper.class);
	
public String getActInactInvStatus(Timestamp startDate, Timestamp endDate ,String reportType, String region,String[] zoneNamesArray,String cityName, String chartType) {
		
	String mainQuery = null;
	String commonQuery = null;

	ResultSet rs= null;
	Connection con= null;
	PreparedStatement pstmt = null;

	List<Object> regions = null ;
	StringBuilder chartSubTitle = null;
	LinkedHashMap<String, Object> pieSeriesMap = null;
	LinkedHashMap<String, List<Object>> actInactBeanMap = null;
	String jsonString = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = format1.parse(startDate.toString());
			Date date2 = format1.parse(endDate.toString());
			chartSubTitle= new StringBuilder("FROM ").append(format2.format(date1)).append(" TO ").append(format2.format(date2));
			con =  DBConnect.getConnection();
			commonQuery = "(select agent_id , max(assigned_total+notAssigned_total) totalPos, max( active_Ret) activeRet , (max(assigned_total+notAssigned_total) - max( active_Ret)) inactiveRet from  st_lms_ret_activityHistory_agentwise  where date >= ? and date<=? group by agent_id) his ";
			if(reportType.equalsIgnoreCase("REGION")){ // STATE 
				mainQuery="select  name,totalPos, activeRet , inactiveRet  from st_lms_state_master sm inner join (select  state_code ,sum( totalPos)totalPos, sum(activeRet)activeRet , sum(inactiveRet)inactiveRet  from (select  state_code ,sum( totalPos)totalPos, sum(activeRet)activeRet , sum(inactiveRet)inactiveRet  from st_lms_organization_master om inner join "+commonQuery+ " on his.agent_id=om.organization_id group by state_code union all select state_code ,0 totalPos, 0 activeRet , 0  inactiveRet  from st_lms_state_master)his group by state_code) his on sm.state_code= his.state_code" ;
				pstmt =  con.prepareStatement(mainQuery);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
			}else if(reportType.equalsIgnoreCase("CITY")){ // CITY
				mainQuery = "select name, agent_id ,sum( totalPos)totalPos, sum(activeRet)activeRet , sum(inactiveRet)inactiveRet  from ( select city name, agent_id ,sum( totalPos)totalPos, sum(activeRet)activeRet , sum(inactiveRet)inactiveRet  from st_lms_organization_master om inner join "+ commonQuery +" on his.agent_id=om.organization_id and city in ((select city_name from st_lms_state_master sm inner join st_lms_city_master cm  on cm.state_code =sm.state_code and  sm.name=?))group by city union all select city_name name ,0 agent_id, 0 totalPos , 0 activeRet, 0 inactiveRet from st_lms_state_master sm inner join st_lms_city_master cm  on cm.state_code =sm.state_code and  sm.name=?) a group by name" ;
				pstmt =  con.prepareStatement(mainQuery);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setString(3, region.trim());
				pstmt.setString(4, region.trim());
			}else if(reportType.equalsIgnoreCase("ZONE")){  // ZONES
				mainQuery ="select  name,sum(totalPos) totalPos , sum(activeRet)activeRet, sum(inactiveRet)inactiveRet from (select  area_name name,totalPos, activeRet , inactiveRet  from st_lms_area_master sm inner join (select  area_code ,sum( totalPos)totalPos, sum(activeRet)activeRet , sum(inactiveRet)inactiveRet  from st_lms_organization_master om inner join" + commonQuery + "  on his.agent_id=om.organization_id  and area_code in ("+(zoneNamesArray==null ? "-1" :CommonMethods.getArrayAsString(zoneNamesArray))+")group by area_code) his on sm.area_code= his.area_code union all select area_name name , 0 totalPos, 0 activeRet , 0 inactiveRet from st_lms_area_master where area_code in ("+(zoneNamesArray==null ? "-1" :CommonMethods.getArrayAsString(zoneNamesArray))+")) a group by name;";
				pstmt =  con.prepareStatement(mainQuery);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
			}
			
			rs =  pstmt.executeQuery();
			actInactBeanMap =  new LinkedHashMap<String, List<Object>>();
			List<Object> liveRetList = new ArrayList<Object>();
			List<Object> noSaleRetList = new ArrayList<Object>();
			List<Object> totRetList = new ArrayList<Object>();
			List<String> categoryList = new ArrayList<String>();
			categoryList.add("TOTAL RET");
			categoryList.add("ACTIVE RET");
			categoryList.add("INACTIVE RET");
			regions = new ArrayList<Object>();
			while(rs.next()){
				totRetList.add(rs.getInt("totalPos"));
				liveRetList.add(rs.getInt("activeRet"));
				noSaleRetList.add(rs.getInt("inactiveRet"));
				regions.add(rs.getString("name"));
			}
			actInactBeanMap.put("totalPos", totRetList);
			actInactBeanMap.put("activeRet", liveRetList);
			actInactBeanMap.put("inactiveRet", noSaleRetList);
			
			TitleBeanForDrawables titleBeanForDrawables= new TitleBeanForDrawables(); 
			titleBeanForDrawables.setChartTitle("ACTIVE INACTIVE STATUS");
			titleBeanForDrawables.setxAxisTitle("Retailer status Analytics For All Regions");
			titleBeanForDrawables.setyAxisTitle("Terminal Count");
			titleBeanForDrawables.setChartSubTitle(chartSubTitle.toString());
			titleBeanForDrawables.setData(regions);

			if(chartType.equals("pie"))
				pieSeriesMap =preparePieChart(titleBeanForDrawables ,actInactBeanMap);

			jsonString = PrepareDrawables.prepareDrawablesJSON(titleBeanForDrawables, chartType, actInactBeanMap ,pieSeriesMap);
		}
		catch (Exception e) {
			logger.error("EXCEPTION :- " , e);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		System.out.println(jsonString);
		return jsonString;
		
	}

private LinkedHashMap<String, Object> preparePieChart(TitleBeanForDrawables titleBeanForDrawables ,LinkedHashMap<String, List<Object>> actInactBeanMap) {
	
	int total = 0;
	int totalActive = 0;
	int totalInActive = 0;

	LinkedHashMap<String, Object> pieSeriesMap = new LinkedHashMap<String, Object>();
	Iterator<Object> it = actInactBeanMap.get("activeRet").iterator();
	while(it.hasNext()){
		totalActive+=(Integer)it.next();
	}
	it = actInactBeanMap.get("inactiveRet").iterator();
	while(it.hasNext()){
		totalInActive+=(Integer)it.next();
	}
	it = actInactBeanMap.get("totalPos").iterator();
	while(it.hasNext()){
		total+=(Integer)it.next();
	}
	titleBeanForDrawables.setChartSubTitle((total==0)?"NO DATA AVAILABLE":"OF "+total+" TERMINALS");
	if(total!=0){
		pieSeriesMap.put("activeRet", (totalActive*100)/total);
		pieSeriesMap.put("inactiveRet" , (totalInActive*100)/total);
	}
	return pieSeriesMap;
}
}
