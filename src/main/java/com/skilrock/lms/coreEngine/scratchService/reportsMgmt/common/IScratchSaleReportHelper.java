package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.exception.LMSException;


public interface IScratchSaleReportHelper {
	Log logger = LogFactory.getLog(IScratchSaleReportHelper.class);

	public List<SalePwtReportsBean> scratchSaleAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException;
	public List<SalePwtReportsBean> scratchSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException;

	public List<SalePwtReportsBean> scratchSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException;

	public List<SalePwtReportsBean> scratchSaleGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException;

	public String getOrgAdd(int orgId) throws LMSException;

	public Map<Integer, List<String>> fetchOrgAddMap() throws LMSException;
}
