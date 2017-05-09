package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.common.exception.LMSException;

public interface IPaymentLedgerReportAgtWiseHelper {
	
	

	public void collectionAgentWiseExpand(Timestamp startDate,
			Timestamp endDate,Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException;

	public Map<String, CollectionReportOverAllBean> collectionAgentWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,int agtOrgId) throws LMSException;

	

}