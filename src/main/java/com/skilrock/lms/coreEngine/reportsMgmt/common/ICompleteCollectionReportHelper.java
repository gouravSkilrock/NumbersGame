package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.exception.LMSException;

public interface ICompleteCollectionReportHelper {
	
	StringBuilder dates = null;

	public Map<String, Double> agentDirectPlayerPwt(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean);

	

	public Map<String, CompleteCollectionBean> collectionAgentWise(
			Timestamp startDate, Timestamp endDate, Connection con);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con);

	public Map<String, CompleteCollectionBean> collectionDayWise(
			Timestamp startDate, Timestamp endDate, Connection con, String viewBy, int orgId);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionDayWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, String viewBy, int orgId);

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType);

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportExpand(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean);

	public Map<String, CompleteCollectionBean> collectionReportForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId,
			String reportType);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportForAgentExpand(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId);

	public Map<String, CompleteCollectionBean> collectionRetailerWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId);

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpandMrpWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId);

	
	public Map<Integer, String> getOrgAddMap(String orgType, int parentId)
			throws LMSException;

	

	public Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> transactionReportForAgent(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId);
}
