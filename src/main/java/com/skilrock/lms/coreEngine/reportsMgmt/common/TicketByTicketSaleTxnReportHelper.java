package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.TicketByTicketSaleTxnReportDaoImpl;

public class TicketByTicketSaleTxnReportHelper {
	private static final String NO = "NO";
	private static final String MAIN_DB = "MAIN_DB";
	private static final String IS_DATA_FROM_REPLICA = "IS_DATA_FROM_REPLICA";
	static Log logger = LogFactory.getLog(TicketByTicketSaleTxnReportHelper.class);
	public static Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportForRetailerTicketByTktTxn(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean, String organizationType) {
		
		Connection con = null;
		
		Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> agtMap = null;
		try{
			if(NO.equals(Utility.getPropertyValue(IS_DATA_FROM_REPLICA)) || MAIN_DB.equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();
			//TicketByTicketSaleTxnReportDaoImpl daoHelper = new TicketByTicketSaleTxnReportDaoImpl();
			agtMap = TicketByTicketSaleTxnReportDaoImpl.reportForRetailerTicketByTktTxn(startDate, endDate, con,
					agtOrgId, organizationType);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return agtMap;
		
	}
       
}
