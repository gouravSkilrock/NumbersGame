package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;

public class VSSaleReconciliationController {

	public Map<String, VSRequestBean> getDataToReconcile(String startDate,
			String endDate, String status) throws ParseException{
		
		Map<String, VSRequestBean> reconDataMap = null ;
		VSRequestBean vsRequestBean = null ;
		Connection conn = null ;
		try{
			
		vsRequestBean = new VSRequestBean();
		conn = DBConnect.getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Timestamp startTime = new Timestamp(dateFormat.parse(startDate+" 00:00:00").getTime());
		Timestamp endTime = new Timestamp(dateFormat.parse(endDate+" 23:59:59").getTime());

		vsRequestBean.setStartDate(startTime);
		vsRequestBean.setEndDate(endTime);
		
		reconDataMap = CommonMethodsDaoImpl.getInstance().getPendingSaleTxns(vsRequestBean, conn, status);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(conn);
		}
		
		return reconDataMap;
	}

}
