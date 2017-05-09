package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.RegionWiseBankDetailBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RegionWiseBankReportHelper;

public class RegionWiseBankReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public RegionWiseBankReportAction() {
		super(RegionWiseBankReportAction.class);
	}

	private String region;
	private String startDate;
	private String endDate;
	private List<String> regionList;
	private List<RegionWiseBankDetailBean> regionWiseBankDetailBeanList;
	private String orgName;
	private String orgAddress;
	private String reportData;
	private String reportName;

	public String getRegion() {
		return region;
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

	public List<RegionWiseBankDetailBean> getRegionWiseBankDetailBeanList() {
		return regionWiseBankDetailBeanList;
	}

	public void setRegionWiseBankDetailBeanList(
			List<RegionWiseBankDetailBean> regionWiseBankDetailBeanList) {
		this.regionWiseBankDetailBeanList = regionWiseBankDetailBeanList;
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

	public String bankReportMenu() {
		return SUCCESS;
	}

	public String bankReportSearch() {
		HttpSession session = request.getSession();

		String dateFormat = null;
		int orgId;

		RegionWiseBankReportHelper regionWiseBankReportHelper = null;

		Timestamp start_date = null, end_date = null;

		logger.info("***** Inside bankReportSearch Function");
		try {
			dateFormat = Utility.getPropertyValue("date_format");

			start_date = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(startDate).getTime());
			end_date = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					endDate).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			orgName = ((UserInfoBean) session.getAttribute("USER_INFO"))
					.getOrgName();

			orgId = ((UserInfoBean) session.getAttribute("USER_INFO"))
					.getUserOrgId();

			regionWiseBankReportHelper = new RegionWiseBankReportHelper();

			orgAddress = regionWiseBankReportHelper.fetchOrgAddress(orgId);

			regionWiseBankDetailBeanList = regionWiseBankReportHelper
					.fetchRegionWiseBankReport(start_date, end_date);

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