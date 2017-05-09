package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.PriviledgeModificationControllerImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationMasterBean;

public class PriviledgeModificationReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public PriviledgeModificationReportAction() {
		super(PriviledgeModificationReportAction.class);
	}

	private Map<Integer, String> boUserMap;
	private Map<String, String> serviceMap;
	private int boUserId;
	private String startDate;
	private String endDate;
	private String serviceCode;
	private PriviledgeModificationMasterBean masterBean;
	private String tableValue;


	public String getTableValue() {
		return tableValue;
	}

	public void setTableValue(String tableValue) {
		this.tableValue = tableValue;
	}

	public Map<Integer, String> getBoUserMap() {
		return boUserMap;
	}

	public void setBoUserMap(Map<Integer, String> boUserMap) {
		this.boUserMap = boUserMap;
	}

	public Map<String, String> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, String> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public int getBoUserId() {
		return boUserId;
	}

	public void setBoUserId(int boUserId) {
		this.boUserId = boUserId;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public PriviledgeModificationMasterBean getMasterBean() {
		return masterBean;
	}

	public void setMasterBean(PriviledgeModificationMasterBean masterBean) {
		this.masterBean = masterBean;
	}

	public String reportMenu() {
		try {
			boUserMap = PriviledgeModificationControllerImpl.getInstance().getBoUsersList();
			serviceMap = PriviledgeModificationControllerImpl.getInstance().getServiceMap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String reportSearch() {
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));

			masterBean = PriviledgeModificationControllerImpl.getInstance().fetchUserPriviledgeHistoryData(boUserId, startTime, endTime, serviceCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	/*public String drawPendingSettlementData() {
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}*/

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=PriviledgeModificationReport.xls");
		PrintWriter out = response.getWriter();
		if (tableValue != null) {
			tableValue = tableValue.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write("<table border='1' width='100%' >" + tableValue + "</table>");
		}
	}
}