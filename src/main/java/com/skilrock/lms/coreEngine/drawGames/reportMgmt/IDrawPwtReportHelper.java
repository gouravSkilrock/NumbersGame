package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.exception.LMSException;

public interface IDrawPwtReportHelper {
	Log logger = LogFactory.getLog(IDrawPwtReportHelper.class);

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate) throws SQLException ;

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException ;

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWise(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException ;

	public List<SalePwtReportsBean> drawBODirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException ;

	public List<SalePwtReportsBean> drawAgentDirPlyPwtGameWiseExpand(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException ;

	public List<SalePwtReportsBean> drawPwtAgentWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, ReportStatusBean reportStatusBean) throws SQLException ;

	public List<SalePwtReportsBean> drawPwtAgentWiseExpand(Timestamp startDate,
			Timestamp endDate, int agentOrgId, ReportStatusBean reportStatusBean) throws SQLException ;

	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException ;

	
	public List<SalePwtReportsBean> drawPwtGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException ;


	public List<SalePwtReportsBean> drawPwtGameWiseExpand(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException ;

	public String getOrgAdd(int orgId) throws LMSException ;

	public Map<Integer, List<String>> fetchOrgAddMap(String orgType, Integer agtOrgId) throws LMSException ;

	public List<SalePwtReportsBean> drawPwtRetailerWise(Timestamp startDate,
			Timestamp endDate, String stateCode, String cityCode, int agtOrgId, ReportStatusBean reportStatusBean) throws SQLException ;

	public List<SalePwtReportsBean> drawPwtRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int retOrgId, ReportStatusBean reportStatusBean, String stateCode, String cityCode)
			throws SQLException ;

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId)
			throws SQLException ;

	public List<SalePwtReportsBean> drawPwtGameWiseForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException ;

	public List<SalePwtReportsBean> drawPwtGameWiseExpandForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean)
			throws SQLException ;
}
