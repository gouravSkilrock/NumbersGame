package com.skilrock.lms.web.accMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.BankDepositBean;
import com.skilrock.lms.coreEngine.accMgmt.serviceImpl.BankDepositServiceImpl;

public class ProcessBankDepositAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String retName;
	private String receiptNo;
	private String startDate;
	private String endDate;
	private String status;
	private String message;
	private List<BankDepositBean> depositList;
	private String jsonParamData;
	private String reportData;

	public ProcessBankDepositAction() {
		super(ProcessBankDepositAction.class.getName());
	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BankDepositBean> getDepositList() {
		return depositList;
	}

	public void setDepositList(List<BankDepositBean> depositList) {
		this.depositList = depositList;
	}

	public String getJsonParamData() {
		return jsonParamData;
	}

	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String processBankDepositRequestSearch() {
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(dateFormat.parse(startDate).compareTo(dateFormat.parse(endDate)) > 0) {
				message = "Start Date Should be Less Then End Date.";
				return ERROR;
			}

			depositList = BankDepositServiceImpl.getInstance().processBankDepositRequestSearch(retName, receiptNo, startDate, endDate, status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String processBankDepositRequest() {
		List<Integer> idList = new ArrayList<Integer>();
		try {
			UserInfoBean userBean = getUserBean();
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
			JsonObject requestData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		requestData = dataArray.get(i).getAsJsonObject();
				idList.add(requestData.get("id").getAsInt());
			}

			boolean updateStatus = BankDepositServiceImpl.getInstance().processBankDepositRequest(userBean, status, idList);
			logger.info("Update Status - "+updateStatus);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public void exportExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Process_Bank_Deposit.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}
}