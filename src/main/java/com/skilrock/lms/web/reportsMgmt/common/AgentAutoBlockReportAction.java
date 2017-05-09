package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.AgentAutoBlockBean;
import com.skilrock.lms.coreEngine.accMgmt.serviceImpl.AgentAutoBlockServiceImpl;

public class AgentAutoBlockReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public AgentAutoBlockReportAction() {
		super(AgentAutoBlockReportAction.class.getName());
	}

	private Map<Integer, AgentAutoBlockBean> autoBlockMap;
	private int orgId;
	private String currentStatus;
	private String reportData;

	public Map<Integer, AgentAutoBlockBean> getAutoBlockMap() {
		return autoBlockMap;
	}

	public void setAutoBlockMap(Map<Integer, AgentAutoBlockBean> autoBlockMap) {
		this.autoBlockMap = autoBlockMap;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String agentAutoBlockMenu() {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			return SUCCESS;
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}
	}

	public String agentAutoBlockDetails() {
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				autoBlockMap = AgentAutoBlockServiceImpl.getInstance().agentAutoBlockReport(reportStatusBean);
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String updateOrganizationStatus() {
		UserInfoBean userBean = null;
		try {
			userBean = getUserBean();
			AgentAutoBlockServiceImpl.getInstance().updateOrganizationStatus(orgId, currentStatus, userBean.getUserId(), request.getRemoteAddr());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Agent_Auto_Block_Status_Report.xls");
		PrintWriter out = response.getWriter();
		reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
		//out.write("<table border='1' width='100%' >"+reportData+"</table>");
		out.write(reportData);
	}
}