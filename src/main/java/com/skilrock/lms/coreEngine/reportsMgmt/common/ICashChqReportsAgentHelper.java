package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.CashChqReportBean;

public interface ICashChqReportsAgentHelper {

	//public ICashChqReportsAgentHelper(UserInfoBean userInfoBean,
		//	DateBeans dateBeans);

	public List<CashChqReportBean> getCashChqDetail();
	public CashChqReportBean getCashChqDetailWithBO() ;

	public String getRetailerName(Connection conn, int agentid);

	public List<Integer> getRetailerOrgId(Connection conn);
	public Map<Integer,String> getUserList();
	
}
