package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.AuditTrailBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AuditReportHelper;

public class AuditTrailReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public AuditTrailReportAction() {
		super(AuditTrailReportAction.class);
	}

	private String region;
	private String startDate;
	private String endDate;
	private String user ;
	private List<String> regionList;
	private List<AuditTrailBean> auditTrailBeans;
	private String orgName;
	private String orgAddress;
	private String reportData;
	private String reportName;
	private Map<Integer, String> orgNameMap = null;
	private int userId = 0;

	public String getRegion() {
		return region;
	}
	
	

	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public void setRegion(String region) {
		this.region = region;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<String> regionList) {
		this.regionList = regionList;
	}

	public List<AuditTrailBean> getAuditTrailBeans() {
		return auditTrailBeans;
	}

	public void setAuditTrailBeans(List<AuditTrailBean> auditTrailBeans) {
		this.auditTrailBeans = auditTrailBeans;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgaddress() {
		return orgAddress;
	}

	public void setOrgaddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Map<Integer, String> getOrgNameMap() {
		return orgNameMap;
	}

	public void setOrgNameMap(Map<Integer, String> orgNameMap) {
		this.orgNameMap = orgNameMap;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String auditTrailReportMenu() throws LMSException {
		orgNameMap = new AuditReportHelper().fetchOrgMap();

		List<Entry<Integer, String>> list = new ArrayList<Entry<Integer, String>>(orgNameMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
			public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
				return (o1.getValue()).compareToIgnoreCase(o2.getValue());
			}
		});

		Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
		for (Iterator<Entry<Integer, String>> it = list.iterator(); it.hasNext();) {
			Entry<Integer, String> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		orgNameMap = sortedMap;

		return SUCCESS;
	}

	public String auditTrailReportSearch() {
		String dateFormat = null;
		HttpSession session = request.getSession();

		String sDate = null, eDate = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		logger.info("***** Inside auditTrailReportSearch Function");
		try {
			dateFormat = Utility.getPropertyValue("date_format");

			orgName = ((UserInfoBean) session.getAttribute("USER_INFO")).getOrgName();

			sDate = df.format((new SimpleDateFormat(dateFormat).parse(startDate).getTime())) + " 00:00:00";
			eDate = df.format((new SimpleDateFormat(dateFormat).parse(endDate).getTime())) + " 23:59:59";
			auditTrailBeans = new AuditReportHelper().fetchAuditTrailReport(userId, sDate, eDate);
		} catch (LMSException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ reportName + ".xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "")
					.replaceAll("</tbody>", "").trim();
			out.write("<table border='1' width='100%' >" + reportData
					+ "</table>");
		}
	}

}