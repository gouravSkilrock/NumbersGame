package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.MTNLedgerReportControllerImpl;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawGameMtnDataBean;

public class MtnLedgerReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public MtnLedgerReportAction() {
		super("MtnLedgerReportAction");
	}

	private String startDate;
	private String reportData;
	private String endDate;
	private String walletName;
	private String message ;
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	private Map<String, DrawGameMtnDataBean> mtnDataMap;

	public Map<String, DrawGameMtnDataBean> getMtnDataMap() {
		return mtnDataMap;
	}

	public void setMtnDataMap(Map<String, DrawGameMtnDataBean> mtnDataMap) {
		this.mtnDataMap = mtnDataMap;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
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
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String fetchMtnLedgerReport() {
		try {
			if (endDate != null && startDate != null) {
				DrawDataBean drawDataBean = new DrawDataBean();
				drawDataBean.setFromDate(startDate + " 00:00:00");
				drawDataBean.setToDate(endDate + " 23:59:59");
				drawDataBean.setWalletName(walletName);
					
				try{
				mtnDataMap = new MTNLedgerReportControllerImpl().fetchMtnLedgerData(drawDataBean);
				} catch (LMSException e) {
					message = e.getErrorMessage();
					return SUCCESS;
				}
				if (mtnDataMap != null) {
					return SUCCESS;
				}
			} else {
				logger.info("Incorrect Inputs");
				addActionMessage("Please Enter Correct Values");
				return ERROR;
			}
		} catch (Exception e) {
			addActionMessage("Some Error In Draw Data ");
			e.printStackTrace();
		}

		return ERROR;
	}

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=MTN_Ledger_Report.xls");
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
