package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.ReconciliationReportControllerImpl;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.ReconcileBean;

public class ReconciliationReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String merchantName;
	private String transactionType;
	private String walletType;
	private String startDate;
	private String endDate;
	private String status;
	private String merchantWallets;
	private List<ReconcileBean> reconcileList;
	private String action;
	private String jsonParamData;
	private String reportData;
	private String message ;

	public ReconciliationReportAction() {
		super("ReconciliationReportAction");
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
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

	public String getMerchantWallets() {
		return merchantWallets;
	}

	public void setMerchantWallets(String merchantWallets) {
		this.merchantWallets = merchantWallets;
	}

	public List<ReconcileBean> getReconcileList() {
		return reconcileList;
	}

	public void setReconcileList(List<ReconcileBean> reconcileList) {
		this.reconcileList = reconcileList;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String reconciliationMenu() throws Exception {
		Map<String, List<String>> merchantWalletMap = ReconciliationReportControllerImpl.getInstance().fetchMerchantWalletData();
		merchantWallets = new Gson().toJson(merchantWalletMap);
		return SUCCESS;
	}

	public String reconciliationSearch() throws Exception {
		try{
		reconcileList = ReconciliationReportControllerImpl.getInstance().fetchMerchantTransactions(merchantName, transactionType, walletType, startDate+" 00:00:00", endDate+" 23:59:59", status);
		} catch (LMSException e) {
			message = e.getErrorMessage();
		}
		if("MTN".equals(merchantName))
			return "MTN";

		return SUCCESS;
	}

	public String processRequest() throws Exception {
		JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
		Map<Long, String> transMap = new HashMap<Long, String>();
		JsonObject transData = null;
		for(int i=0; i<dataArray.size(); i++) {
			transData = dataArray.get(i).getAsJsonObject();
			transMap.put(transData.get("transId").getAsLong(), transData.get("merchantTransId").getAsString());
    	}

		ReconciliationReportControllerImpl.getInstance().processRequest(merchantName, action, transMap);

		if("MTN".equals(merchantName))
			return "MTN";

		return SUCCESS;
	}

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Draw_Game_Reconciliation_Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			reportData =reportData.replaceAll("<br>", "").replaceAll("</br>", "").trim();
			reportData =reportData.replaceAll("</div>", "</div></br>").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}
}