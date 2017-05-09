package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.exception.LMSException;

public interface IScratchPwtReportHelper {
	Log logger = LogFactory.getLog(IScratchPwtReportHelper.class);
	public List<SalePwtReportsBean> scratchBODirPlyPwtGameWise(Timestamp startDate,Timestamp endDate) throws SQLException ;
	
	public List<SalePwtReportsBean> scratchBODirPlyPwtGameWiseExpand(Timestamp startDate,Timestamp endDate) throws SQLException;
	public List<SalePwtReportsBean> scratchPwtAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException ;

	public List<SalePwtReportsBean> scratchPwtAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException ;

	public List<SalePwtReportsBean> scratchPwtGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException ;

	public List<SalePwtReportsBean> scratchPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException ;
	
	public String getOrgAdd(int orgId) throws LMSException ;
	
	public Map<Integer, List<String>> fetchOrgAddMap()throws LMSException;
}
