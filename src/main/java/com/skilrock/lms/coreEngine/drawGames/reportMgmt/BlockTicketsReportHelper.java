package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.BlockTicketUserBean;

public class BlockTicketsReportHelper {
	private static Log logger = LogFactory.getLog(BlockTicketsReportHelper.class);
	public List<BlockTicketUserBean> fetchBlockTickets(String startDate,String endDate , int gameId) throws SQLException {
		logger.info("---Block Tickets Report Game Wise Helper---");
		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		Connection con=null;
		IServiceDelegate  delegate=null;
		Statement st=null;
		ResultSet rs=null;
		Map<Integer,String> map=null;
		List<BlockTicketUserBean>list=null;
		JSONObject obj=null;
		SimpleDateFormat sdfOld=null;
		SimpleDateFormat sdfNew=null;
		Date date1=null;
		Date date2=null;
		String newStartDate=null;
		String newEndDate=null;
		try {
			con=DBConnect.getConnection();
			serReq = new ServiceRequest();
			serResp = new ServiceResponse();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_BLOCKED_TICKETS);
			sdfOld = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			date1 = sdfOld.parse(startDate);
			sdfNew=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    newStartDate = sdfNew.format(date1);
			date2=sdfOld.parse(endDate);
		    newEndDate=sdfNew.format(date2);
			obj=new JSONObject();
			obj.put("startDate", newStartDate);
			obj.put("endDate",newEndDate);
			obj.put("gameId",gameId);
            serReq.setServiceData(obj);
        	delegate=ServiceDelegate.getInstance(); 
			serResp=delegate.getResponse(serReq);
			if(serResp.getIsSuccess()){
				String responseString = serResp.getResponseData().toString();
				list = new Gson().fromJson(responseString, new TypeToken<List<BlockTicketUserBean>>() {}.getType());	
				st=con.createStatement();
				rs=st.executeQuery("select user_id,user_name from st_lms_user_master where  organization_type ='BO';");
				map=new HashMap<Integer,String>();
				while(rs.next()){
				   map.put(rs.getInt("user_id"),rs.getString("user_name"));
				}
				for(BlockTicketUserBean temp:list){
					temp.setUserName(map.get(temp.getDoneByUserId()));
				}
				return list;
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
		
	

}
