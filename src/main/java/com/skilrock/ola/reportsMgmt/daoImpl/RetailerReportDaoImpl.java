package com.skilrock.ola.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class RetailerReportDaoImpl  {
	
	static Log logger = LogFactory.getLog(RetailerReportDaoImpl.class);
	
	public static String getArchDate(){
		Connection con = DBConnect.getConnection();
		String lastArchDate = ReportUtility.getLastArchDate(con);
		DBConnect.closeCon(con);
		return lastArchDate;
		
	}
	
}
