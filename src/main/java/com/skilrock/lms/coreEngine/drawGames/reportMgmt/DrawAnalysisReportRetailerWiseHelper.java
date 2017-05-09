package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.RegionWiseDataBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawAnalysisReportRetailerWiseHelper {
private static Log logger = LogFactory.getLog(DrawAnalysisReportRetailerWiseHelper.class);
	public List<AnalysisBean> fetchData(AnalysisBean anaBean) {

		String partyId;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<Integer,String> userMap;
 
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		List<AnalysisBean> anaBeanList = helper.fetchDrawAnalysisData(anaBean);
		List<AnalysisBean> newAnaBeanList = null;
		try {
			String orgCodeQry = " a.name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode ";
			

			}	
			con = DBConnect.getConnection();
			if(!anaBeanList.isEmpty()){
				userMap=new HashMap<Integer, String>();
				String query = "select b.user_id user_id ,"+orgCodeQry+" from st_lms_organization_master a, st_lms_user_master b  where a.organization_type='Retailer' and a.organization_id=b.organization_id";
				pstmt = con.prepareStatement(query);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					userMap.put(rs.getInt("user_id"), rs.getString("orgCode"));
				}
			
			newAnaBeanList=new ArrayList<AnalysisBean>();
			Iterator<AnalysisBean> it = anaBeanList.iterator();
			while (it.hasNext()) {
				AnalysisBean anabean1 = new AnalysisBean();
				anabean1 = (AnalysisBean) it.next();
				partyId = anabean1.getPartyId();
				if (partyId.equalsIgnoreCase("999999999")) {
					anabean1.setRetailerName("Total");
					anabean1.setTotalSaleAmount(anabean1.getTotalSaleAmount());
					anabean1.setTotalSaleCount(anabean1.getTotalSaleCount());
					anabean1.setTotalPwtAmount(anabean1.getTotalPwtAmount());
					anabean1.setTotalPwtCount(anabean1.getTotalPwtCount());
					anabean1.setTotalPprAmount(anabean1.getTotalPprAmount());
					newAnaBeanList.add(anabean1);
				} else {
					anabean1.setRetailerName(userMap.get(Integer.parseInt(anabean1.getPartyId())));
					anabean1.setSaleAmount(anabean1.getSaleAmount());
					anabean1.setSaleCount(anabean1.getSaleCount());
					anabean1.setPwtAmount(anabean1.getPwtAmount());
					anabean1.setPwtCount(anabean1.getPwtCount());
					anabean1
							.setPrizePayoutRatio(anabean1.getPrizePayoutRatio());
					newAnaBeanList.add(anabean1);
				}
			}
		}
	}	 catch (Exception e) {
			System.out.println("INSIDE ERROR OF NEW ANABEAN LIST.....");
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		return newAnaBeanList;
	}
	
public static Map<String,RegionWiseDataBean>  fetchRegionWiseDrawData(AnalysisBean anaBean,String stateCode,ReportStatusBean reportStatusBean) throws LMSException{
	Connection con =null;
	try{
		
	
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		//Get Data From DGE
		Map<Integer,AnalysisBean> anaMap = helper.getDrawDataRetailerWise(anaBean);
		
		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();
		//con=DBConnect.getConnection();
		
		Map<Integer,UserDetailsBean> usrMap = ReportUtility.getRetUserDetailsMap(con,stateCode, "-1", "-1",anaBean.getStartDate(),anaBean.getEndDate());
		
		DBConnect.closeCon(con);
	
		Map<String,RegionWiseDataBean> dataMap = new LinkedHashMap<String,RegionWiseDataBean>();
		Iterator  itr = usrMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Integer,UserDetailsBean> entry = (Map.Entry<Integer,UserDetailsBean>)itr.next();
			RegionWiseDataBean dataBean =null;
			
			int userId =entry.getKey();
			AnalysisBean  analysisBean =anaMap.get(userId);
			String mapKey = entry.getValue().getOrgStateCode();
			if(analysisBean!=null){
				
				if(dataMap.containsKey(mapKey)){
					dataBean =dataMap.get(mapKey);
					dataBean.setSaleAmt(dataBean.getSaleAmt()+analysisBean.getSaleAmount());
					dataBean.setPwtAmt(dataBean.getPwtAmt()+analysisBean.getPwtAmount());
			
				}else{
					dataBean = new RegionWiseDataBean();
					dataBean.setStateName(usrMap.get(userId).getOrgState());
					dataBean.setStateCode(usrMap.get(userId).getOrgStateCode());
					dataBean.setSaleAmt(analysisBean.getSaleAmount());
					dataBean.setPwtAmt(analysisBean.getPwtAmount());
				
					dataMap.put(mapKey, dataBean);
				}
				
			}
			
			
			
			
		
			
			
		}
		
	
		return dataMap;
		
		
		
		
	}catch (LMSException e) {
		logger.error("LMS Exception :", e);
		throw new LMSException(e.getErrorCode(),e.getErrorMessage());
	}catch (Exception e) {
		logger.error("Exception :", e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
		
		
	}
	
	
}	
public static Map<String,RegionWiseDataBean>  fetchAreaWiseDrawData(AnalysisBean anaBean,String stateCode,ReportStatusBean reportStatusBean) throws LMSException{
	Connection con =null;
	Map<String,RegionWiseDataBean> dataMap = new LinkedHashMap<String,RegionWiseDataBean>();
	try{
		
		
		
		/*if(stateCode!=null&&!stateCode.equalsIgnoreCase("-1")){
			Set<Integer> keySet =usrMap.keySet();
			anaBean.setSalePartyIdList(keySet);
		}*/
	
		DGNumberAnalysisReportHelper helper = new DGNumberAnalysisReportHelper();
		Map<Integer,AnalysisBean> anaMap = helper.getDrawDataRetailerWise(anaBean);
		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();
		Map<Integer,UserDetailsBean> usrMap = ReportUtility.getRetUserDetailsMap(con,stateCode, "-1", "-1",anaBean.getStartDate(),anaBean.getEndDate());
		
		DBConnect.closeCon(con);
		Iterator  itr = usrMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Integer,UserDetailsBean> entry = (Map.Entry<Integer,UserDetailsBean>)itr.next();
			//System.out.println(entry.getKey()+" :"+entry.getValue().getSaleAmt());
			

			RegionWiseDataBean dataBean =null;
			int userId =entry.getKey();
			AnalysisBean  analysisBean =anaMap.get(userId);
			String mapKey = usrMap.get(userId).getOrgStateCode()+usrMap.get(userId).getOrgCity()+usrMap.get(userId).getOrgAreaName();
		if(analysisBean!=null){
			
			if(dataMap.containsKey(mapKey)){
				dataBean =dataMap.get(mapKey);
				dataBean.setSaleAmt(dataBean.getSaleAmt()+analysisBean.getSaleAmount());
				dataBean.setPwtAmt(dataBean.getPwtAmt()+analysisBean.getPwtAmount());
		
			}else{
				dataBean = new RegionWiseDataBean();
				dataBean.setStateName(usrMap.get(userId).getOrgState());
				dataBean.setStateCode(usrMap.get(userId).getOrgStateCode());
				dataBean.setCityName(usrMap.get(userId).getOrgCity());
				dataBean.setAreaName(usrMap.get(userId).getOrgAreaName());
				dataBean.setSaleAmt(analysisBean.getSaleAmount());
				dataBean.setPwtAmt(analysisBean.getPwtAmount());
			
				dataMap.put(mapKey, dataBean);
			}
			
		}	
			
			
			
			
			
		
			
			
		}
		
	
		
		//con =DBConnect.getConnection();
		
	}catch (LMSException e) {
		logger.error("LMS Exception :", e);
		throw new LMSException(e.getErrorCode(),e.getErrorMessage());
	}catch (Exception e) {
		logger.error("Exception :", e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
		
		
	}
	return dataMap;
	
}	

	
	
}
