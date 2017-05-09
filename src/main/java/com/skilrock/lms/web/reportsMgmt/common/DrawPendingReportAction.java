package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.skilrock.lms.beans.DrawPendingSettlementBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.DrawPendingReportHelper;

public class DrawPendingReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public DrawPendingReportAction() {
		super(DrawPendingReportAction.class);
	}

	private int agentOrgId;
	private String deploymentDate;
	private String interfaceType;
	private String startDate;
	private String endDate;
	private String reportType;
	private List<DrawPendingSettlementBean> drawPendingSettlementList;
	private String tableValue;

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getDeploymentDate() {
		return deploymentDate;
	}

	public void setDeploymentDate(String deploymentDate) {
		this.deploymentDate = deploymentDate;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
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

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<DrawPendingSettlementBean> getDrawPendingSettlementList() {
		return drawPendingSettlementList;
	}

	public void setDrawPendingSettlementList(List<DrawPendingSettlementBean> drawPendingSettlementList) {
		this.drawPendingSettlementList = drawPendingSettlementList;
	}

	public String getTableValue() {
		return tableValue;
	}

	public void setTableValue(String tableValue) {
		this.tableValue = tableValue;
	}

	public String drawPendingSettlementMenu() {
		deploymentDate = CommonMethods.convertDateInGlobalFormat(Utility.getPropertyValue("DEPLOYMENT_DATE"), "yyyy-mm-dd", Utility.getPropertyValue("date_format"));
		return SUCCESS;
	}

	public String drawPendingSettlementData() {
		DrawPendingReportHelper helper = new DrawPendingReportHelper();
		SimpleDateFormat simpleDateFormat = null;
		try {
			if("processing".equals(reportType)) {
				drawPendingSettlementList = helper.getProcessTicketsData(agentOrgId, interfaceType);
			} else if("retTicketProcess".equals(reportType)) {
				drawPendingSettlementList = helper.getRetTicketProcessData(agentOrgId, interfaceType);
			} else {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Timestamp startTime = new Timestamp(simpleDateFormat.parse(startDate+" 00:00:00").getTime());
				Timestamp endTime = new Timestamp(simpleDateFormat.parse(endDate+" 23:59:59").getTime());

				if("unsuccessful".equals(reportType)) {
					drawPendingSettlementList = helper.getUnsuccessfulTicketsData(agentOrgId, startTime, endTime);
				} else if("retUnsuccessfulData".equals(reportType)) {
					drawPendingSettlementList = helper.getRetailerUnsuccessfulData(agentOrgId, startTime, endTime);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return reportType;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=drawPendingSettlementReport.xls");
		PrintWriter out = response.getWriter();
		if (tableValue != null) {
			tableValue = tableValue.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + tableValue + "</table>");
			out.write(tableValue);
		}
	}
}