package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.exception.LMSException;

public interface IDrawSaleReportHelper {
	public List<SalePwtReportsBean> drawSaleAgentWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException;

	public List<SalePwtReportsBean> drawSaleRetailerWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException;

	public List<SalePwtReportsBean> drawSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException;

	public List<SalePwtReportsBean> drawSaleRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException;

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException;

	public List<SalePwtReportsBean> drawSaleGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException;

	public List<SalePwtReportsBean> drawSaleGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException;

	public List<SalePwtReportsBean> drawSaleGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException;

	public List<SalePwtReportsBean> drawSaleGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException;

	
	public Map<Integer, List<String>> fetchOrgAddMap(String orgType,
			Integer agtOrgId) throws LMSException;
}
