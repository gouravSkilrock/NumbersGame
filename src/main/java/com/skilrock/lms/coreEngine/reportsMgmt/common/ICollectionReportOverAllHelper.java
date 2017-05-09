package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;


import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.common.exception.LMSException;

public interface ICollectionReportOverAllHelper {
	

	public void collectionAgentWise(Timestamp startDate, Timestamp endDate,
			Connection con,	Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException;

	public void collectionAgentWiseExpand(Timestamp startDate,
			Timestamp endDate,Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException;

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode) throws LMSException;

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate, String cityCode, String stateCode,int roleId) throws LMSException;

	

	

}
