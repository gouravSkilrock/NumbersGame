package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.GoodCauseControllerImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.GoodCauseDataBean;

public class GoodCauseReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public GoodCauseReportAction() {
		super(GoodCauseReportAction.class);
	}

	private String startDate;
	private String endDate;
	private List<GoodCauseDataBean> reportList;

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

	public List<GoodCauseDataBean> getReportList() {
		return reportList;
	}

	public void setReportList(List<GoodCauseDataBean> reportList) {
		this.reportList = reportList;
	}

	public String reportMenu() {
		return SUCCESS;
	}

	public String reportSearch() {
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));

			reportList = GoodCauseControllerImpl.getInstance().fetchGoodCauseData(startTime, endTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	/*public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=PriviledgeModificationReport.xls");
		PrintWriter out = response.getWriter();
		if (tableValue != null) {
			tableValue = tableValue.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write("<table border='1' width='100%' >" + tableValue + "</table>");
		}
	}*/
}