package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.NetSaleWinningReportDataBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.NetSaleWinningRepControllerImpl;

public class NetSaleWinningReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private int agentOrgId;
	private String reportType;
	private String startDate;
	private String endDate;
	private String depDate;
	private Map<String, NetSaleWinningReportDataBean> netSaleWinDataMap;
	
	public NetSaleWinningReportAction() {
		super(NetSaleWinningReportAction.class);
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
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

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Map<String, NetSaleWinningReportDataBean> getDataMap() {
		return netSaleWinDataMap;
	}

	public Map<String, NetSaleWinningReportDataBean> getNetSaleWinDataMap() {
		return netSaleWinDataMap;
	}

	public void setNetSaleWinDataMap(
			Map<String, NetSaleWinningReportDataBean> netSaleWinDataMap) {
		this.netSaleWinDataMap = netSaleWinDataMap;
	}

	public String execute() throws Exception {
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)LMSUtility.sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)LMSUtility.sc.getAttribute("date_format")));
		return SUCCESS;
	}

	public String fetchNetSaleWinData() {
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			System.out.println("Data:" + agentOrgId + ":" + reportType + ":"
					+ this.startDate + ":" + this.endDate);
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility
					.getReportStatus(actionName);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if ("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				startDate = new Timestamp(dateFormat.parse(
						this.startDate + " 00:00:00").getTime());
				endDate = new Timestamp(dateFormat.parse(
						this.endDate + " 00:00:00").getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				Map<String, NetSaleWinningReportDataBean> dataMap = NetSaleWinningRepControllerImpl
						.getInstance().fetchNetSaleWinData(agentOrgId,
								reportType, startDate, endDate,
								getText("label.net.sale.win.rep"));
				setNetSaleWinDataMap(dataMap);
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}

			return SUCCESS;
		} catch (LMSException e) {
			return INPUT;
		} catch (Exception e) {
			return INPUT;
		}
	}

}
