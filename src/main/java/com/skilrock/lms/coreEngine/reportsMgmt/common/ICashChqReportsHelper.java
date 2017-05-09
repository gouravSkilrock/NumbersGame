package com.skilrock.lms.coreEngine.reportsMgmt.common;


import java.util.List;
import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.common.exception.LMSException;

public interface ICashChqReportsHelper {


	public List<Integer> getAgentId() throws LMSException;
	
	public List<Integer> getAgentOrgList();

	public List<CashChqReportBean> getCashChqDetail(List<Integer> agtidlist,int userId,boolean isExpand) throws LMSException;
	
	public List<CashChqPmntBean> getCashChqDetailAgentWise(String startDate,String endDate,int userId,boolean isExpand,String state,String city) throws LMSException;
	public List<CashChqPmntBean> getCashChqDetailDateWise(int orgId,int userId) throws LMSException;
	public List<CashChqReportBean> getCashChqDetailDayWise(int userId,boolean isExpand) throws LMSException;
		

}
