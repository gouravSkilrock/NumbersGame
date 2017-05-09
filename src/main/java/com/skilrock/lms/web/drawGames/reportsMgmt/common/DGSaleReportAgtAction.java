package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportAgtHelper;

public class DGSaleReportAgtAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DGSaleReportAgtAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
				.get(Calendar.DATE), 0, 0, 0);
		logger.debug("Inside MAin: " + calendarNew.getTime());
		// logger.debug(calendarNew.getTime());
	}

	List<SaleReportBean> dgSaleDetail;
	private String end_Date;
	private String filter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;

	private String totaltime;

	public String createReport() throws LMSException, ParseException {
		DateBeans dateBean = new DateBeans();
		HttpSession session = getRequest().getSession();
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean = GetDate.getDate(totaltime);
		}
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGSaleReportAgtHelper dgSale = new DGSaleReportAgtHelper(userInfoBean,
				dateBean);
		if ("Game Wise".equalsIgnoreCase(filter)) {
			dgSaleDetail = dgSale.fetchDGSaleDetailGameWise();
		} else if ("Retailer Wise".equalsIgnoreCase(filter)) {
			dgSaleDetail = dgSale.fetchDGSaleDetailRetailerWise();
		}
		logger.debug("dgSaleDetail :" + dgSaleDetail);
		logger.debug("presentDate :" + session.getAttribute("presentDate"));
		logger.debug("date_format :" + session.getAttribute("date_format"));
		// logger.debug(dgSaleDetail);
		// logger.debug(session.getAttribute("presentDate" ));
		// logger.debug(session.getAttribute("date_format"));
		session.setAttribute("datebean", dateBean);
		session.setAttribute("searchResultRet", dgSaleDetail);
		session.setAttribute("orgName", userInfoBean.getOrgName());
		session.setAttribute("orgAdd", dgSale.getOrgAdd(userInfoBean
				.getUserOrgId()));
		session.setAttribute("filter", filter);
		return SUCCESS;
	}

	public void exportExcel() {
		HttpSession session = request.getSession();
		List<SaleReportBean> data = new ArrayList<SaleReportBean>();
		data = (ArrayList) request.getSession().getAttribute("searchResultRet");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DGSaleReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForDGSaleReport excel = new WriteExcelForDGSaleReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.write(data, w, (String) session.getAttribute("orgName"),
					(String) session.getAttribute("orgAdd"), "AGENT",
					(String) request.getSession().getServletContext()
							.getAttribute("CURRENCY_SYMBOL"), (String) session
							.getAttribute("filter"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getFilter() {
		return filter;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

}
