package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDrawSaleReportHelper;
import com.skilrock.lms.web.drawGames.reportsMgmt.common.WriteExcelForDrawSaleReport;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class SaleReportLsAction extends BaseAction {
	/**
	 * 
	 */
	public SaleReportLsAction() {
		super(SaleReportLsAction.class.getName());
	}
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String reportType;
	public String getReportType() {
		return reportType;
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
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
	private String endDate;
	

	public String fetchReportData() {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HttpSession session = request.getSession();
			ServletContext sc = session.getServletContext();
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
			
			session.setAttribute("reportList", fetchReportGameWise(
					new Timestamp(format.parse(startDate+" 00:00:00").getTime()), new Timestamp(format.parse(endDate+" 23:59:59").getTime()), reportStatusBean, "ALL","ALL"));
			session.setAttribute("excelData",
					(List<SalePwtReportsBean>) session
							.getAttribute("reportList"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	public List<SalePwtReportsBean> fetchReportGameWise(Timestamp startDate,
			Timestamp endDate, ReportStatusBean reportStatusBean, String cityCode, String stateCode) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWise(startDate, endDate, reportStatusBean, cityCode, stateCode);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		Iterator<SalePwtReportsBean> iterator = reportList.iterator();
		while(iterator.hasNext()) {
			SalePwtReportsBean bean = iterator.next();
		    if ("INSTANT WIN".equalsIgnoreCase(bean.getGameName())) {
		    		iterator.remove();
		    }
		}
		return reportList;
	}
	public void exportToExcel() {
		HttpSession session = request.getSession();
		List<SalePwtReportsBean> data = new ArrayList<SalePwtReportsBean>();
		List<SalePwtReportsBean> dataExpended = new ArrayList<SalePwtReportsBean>();
		ServletContext sc = session.getServletContext();
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		String dateFormat = "yyyy-MM-dd";
		data = (ArrayList) session.getAttribute("excelData");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DrawSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			
			try {
				String actionName = ActionContext.getContext().getName();
				ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

				if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
					Timestamp start_Date = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(startDate).getTime());
					Timestamp end_Date = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(endDate).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
					WriteExcelForDrawSaleReport excel = new WriteExcelForDrawSaleReport(
							start_Date, end_Date, reportType);
					if ("Game Wise".equalsIgnoreCase(reportType)) {
						dataExpended = fetchReportGameWiseExpand(start_Date, end_Date, reportStatusBean);
						excel.writeGameWise(data, dataExpended, w, (String) session
								.getAttribute("orgName"), (String) session
								.getAttribute("orgAdd"), "BO", (String) request
								.getSession().getServletContext().getAttribute(
										"CURRENCY_SYMBOL"),"ALL","ALL");
					}
				} else {
					throw new LMSException("Result Timing Restriction");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new LMSException("Date Format Error");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SalePwtReportsBean> fetchReportGameWiseExpand(
			Timestamp startDate, Timestamp endDate, ReportStatusBean reportStatusBean) throws SQLException {
		IDrawSaleReportHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP){
			helper = new DrawSaleReportHelperSP();
		}else{
			helper = new DrawSaleReportHelper();
		}
		List<SalePwtReportsBean> reportList = null;
		reportList = helper.drawSaleGameWiseExpand(startDate, endDate, reportStatusBean);
		HttpSession session = request.getSession();
		session.setAttribute("orgName", ((UserInfoBean) session
				.getAttribute("USER_INFO")).getOrgName());
		try {
			session.setAttribute("orgAdd", ReportUtility
					.getOrgAdd(((UserInfoBean) session
							.getAttribute("USER_INFO")).getUserOrgId()));
		} catch (LMSException ex) {
			ex.printStackTrace();
		}
		logger.info("---reportList---" + reportList);
		return reportList;
	}
	
}
