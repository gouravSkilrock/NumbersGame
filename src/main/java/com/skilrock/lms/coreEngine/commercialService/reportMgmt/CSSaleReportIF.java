package com.skilrock.lms.coreEngine.commercialService.reportMgmt;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.common.exception.LMSException;

public interface CSSaleReportIF {

	public List<CSSaleReportBean> CSSaleCategoryWise(Timestamp startDate,
			Timestamp endDate) throws SQLException;
	
	public List<CSSaleReportBean> CSSaleAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException;
	
	public List<CSSaleReportBean> CSSaleRetailerWise(Timestamp startDate,
			Timestamp endDate,int agtOrgId) throws SQLException;

	public List<CSSaleReportBean> CSSaleProductWise(Timestamp startDate,
			Timestamp endDate, int catId) throws SQLException;
	
	public List<CSSaleReportBean> CSSaleProductWiseAgentWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId) throws SQLException;
	
	public List<CSSaleReportBean> CSSaleProductWiseRetailerWise(Timestamp startDate,
			Timestamp endDate, int retOrgId)throws SQLException;

	public List<CSSaleReportBean> getCSSaleRetailerWise(Timestamp startDate,
			Timestamp endDate, int retOrgId)throws SQLException;

	public String getOrgAdd(int orgId) throws LMSException;
	
	public Map<Integer, List<String>> fetchOrgAddMap(String orgType, Integer agtOrgId)throws LMSException;
	
	public Map<Integer, String> fetchActiveCategoryMap()throws LMSException;
}
