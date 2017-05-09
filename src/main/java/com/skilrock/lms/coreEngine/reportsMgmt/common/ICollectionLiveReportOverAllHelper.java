package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.exception.LMSException;

public interface ICollectionLiveReportOverAllHelper {
	Log logger = LogFactory.getLog(ICollectionLiveReportOverAllHelper.class);

	public Map<String, String> allGameMap() throws LMSException;

	public Map<String, String> allCatMap() throws LMSException;

	public Map<String, Boolean> checkAvailableService();

	public void collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con, boolean isDG, boolean isSE, boolean isCS,
			boolean isOLA, boolean isSLE,
			Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException;

	public void collectionAgentWiseExpand(Timestamp startDate,
			Timestamp endDate, boolean isDG, boolean isSE, boolean isCS,
			boolean isOLA, boolean isSLE,
			Map<String, CollectionReportOverAllBean> agtMap, ReportStatusBean reportStatusBean)
			throws LMSException;

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,
			boolean isDG, boolean isSE, boolean isCS, boolean isOLA,
			boolean isSLE, ReportStatusBean reportStatusBean) throws LMSException;

	public Map<String, String> getOrgMap(String orgType);

	public String getOrgAdd(int orgId) throws LMSException;

}
