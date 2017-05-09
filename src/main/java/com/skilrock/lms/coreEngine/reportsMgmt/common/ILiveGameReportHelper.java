package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;

public interface ILiveGameReportHelper {
	static Log logger = LogFactory.getLog(LiveGameReportHelper.class);

	public Map<String, String> consolidateLiveGameReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate, boolean isNoCash);

	public TreeMap<String, String> drawReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate);

	public String getOrgAdd(int orgId) throws LMSException;

	public void retDrawReport() ;
	
	public TreeMap<String, String> retStatusReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate);

	public TreeMap<String, String> scratchReport(int agtOrgId,
			Timestamp startDate, Timestamp endDate) ;
}
