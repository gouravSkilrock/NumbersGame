package com.skilrock.lms.coreEngine.reportsMgmt.customized;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.LinkedHashMap;


import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;


import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CustomSaleReportHelper {

private static Log logger =LogFactory.getLog(CustomSaleReportHelper.class);

private static final boolean isDailyRep =true;
/***
 * This Method Fetch Data For Slot Sale and Mrp Sale for Date Duration 
 * 
 * 
 * @imp isDailyRep Variable Configured to Display Report From Daily/Weekly Table 
 * @param fromDate
 * @param toDate
 * @param agentOrgId
 * @return
 * @throws LMSException
 */
public static 	Map<Integer,CollectionReportOverAllBean>  fetchSlotSaleData(String fromDate,String toDate,int agentOrgId,Map<Integer,String> gameMap) throws LMSException{
	Connection con =null;

	PreparedStatement pstmt = null;
	Statement  stmt =null;
	ResultSet rs =null;
	ResultSet gameRs =null;
	Map<Integer,CollectionReportOverAllBean> agentDataMap = new LinkedHashMap<Integer,CollectionReportOverAllBean>()   ;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try{
		con =DBConnect.getConnection();
		stmt =con.createStatement();
		 
		String gameQry =ReportUtility.getDrawGameMapQuery(new Timestamp(sdf.parse(fromDate).getTime()));
		gameRs =stmt.executeQuery(gameQry);
		
		int gameId =0;
		String subQry ="";
		if(agentOrgId!=-1){
			subQry ="  and  om.organization_id="+agentOrgId;
			
		}

		int agtOrgId =0;
		double slotMrpSale =0.00;
		double mrpSale =0.00;
		String qry =" "; 
		if(isDailyRep){
			qry =" select "+QueryManager.getOrgCodeQuery()+",ifnull(mrpSale,0.0) mrpSale,ifnull(slotSale,0.0) slotSale,organization_id from st_lms_organization_master om  left join ( select agent_org_id,sum(mrp_sale) mrpSale,sum(time_slotted_mrp_sale) slotSale from st_lms_agent_daily_training_exp where  game_id=?  and date(date)<=date(?) and date(date)>=date(?)  group by agent_org_id )sale on sale.agent_org_id=om.organization_id   where  om.organization_type='AGENT' "+subQry+" order by "+QueryManager.getAppendOrgOrder();
		}else{
			
			qry =" select "+QueryManager.getOrgCodeQuery()+",ifnull(mrpSale,0.0) mrpSale,ifnull(slotSale,0.0) slotSale,organization_id from st_lms_organization_master om  left join ( select agent_org_id,sum(mrp_sale) mrpSale,sum(time_slotted_mrp_sale) slotSale from st_lms_agent_weekly_training_exp where game_id=?  and date(date)<=date(?) and date(date)>=date(?)"+subQry+"  group by agent_org_id )sale on sale.agent_org_id=om.organization_id  where  om.organization_type='AGENT'  "+subQry+"  order by  "+QueryManager.getAppendOrgOrder();
		}
	
		pstmt =con.prepareStatement(qry);

		CollectionReportOverAllBean totalagtBean = new CollectionReportOverAllBean();
		Map<String, CompleteCollectionBean> totalGameBeanMap = new LinkedHashMap<String, CompleteCollectionBean>();
		while(gameRs.next()){
		
			
			double mrpSaleSum =0.00;
			double slotSum =0.00;
			gameId =gameRs.getInt("game_id");
			
			pstmt.setInt(1,gameId);
			pstmt.setString(2,toDate);
			pstmt.setString(3, fromDate);
		
			logger.debug("Mrp Sale Qry "+pstmt);
			rs=pstmt.executeQuery();
			String gameName =gameRs.getString("game_name");
			gameMap.put(gameId, gameName);
		while(rs.next()){
				agtOrgId =rs.getInt("organization_id");
				String orgName = rs.getString("orgCode");
				slotMrpSale =rs.getDouble("slotSale");
				slotSum=slotSum+slotMrpSale;
				mrpSale =rs.getDouble("mrpSale");
				mrpSaleSum=mrpSaleSum+mrpSale;
				CollectionReportOverAllBean agtBean = new CollectionReportOverAllBean();
		
				if(agentDataMap.containsKey(agtOrgId)){
					agtBean=agentDataMap.get(agtOrgId);
					CompleteCollectionBean   gameDataBean = new CompleteCollectionBean();
					gameDataBean.setDrawSlotSale(slotMrpSale);
					gameDataBean.setDrawSale(mrpSale);
					agtBean.getGameBeanMap().put(gameId+"", gameDataBean);
					
				}else{
					CompleteCollectionBean   gameDataBean = new CompleteCollectionBean();
					Map<String, CompleteCollectionBean> gameBeanMap = new LinkedHashMap<String, CompleteCollectionBean>();
					agtBean.setAgentName(orgName);
					gameDataBean.setDrawSlotSale(slotMrpSale);
					gameDataBean.setDrawSale(mrpSale);
					gameBeanMap.put(gameId+"", gameDataBean);
					agtBean.setGameBeanMap(gameBeanMap);
					agentDataMap.put(agtOrgId, agtBean);
					
				}
			
			}
			CompleteCollectionBean   totalgameDataBean = new CompleteCollectionBean();
			totalgameDataBean.setDrawSale(mrpSaleSum);
			totalgameDataBean.setDrawSlotSale(slotSum);
			totalGameBeanMap.put(gameId+"", totalgameDataBean);
			
			totalagtBean.setGameBeanMap(totalGameBeanMap);
		
		}
		

		agentDataMap.put(-1, totalagtBean);
	}catch (SQLException e) {
		logger.error("SQL Exception",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeRs(rs);
		DBConnect.closePstmt(pstmt);
		DBConnect.closeCon(con);
	}
	return agentDataMap;
}
	

}
