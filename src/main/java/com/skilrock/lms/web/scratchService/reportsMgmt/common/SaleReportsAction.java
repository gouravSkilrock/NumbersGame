package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.DGSaleReportsHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.SaleReportsHelper;

public class SaleReportsAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(SaleReportsAction.class);

	private static final long serialVersionUID = -6526264479874310368L;
	private String edit = null;
	private String end = null;
	private String end_Date;
	private String gameType;
	private String groupBy;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String searchResultsAvailable;

	private int start = 0;

	private String start_date;

	private String totaltime;

	String[] type = { "Daily", "Weekly", "Monthly" };

	@Override
	public String execute() {
		request.getSession().setAttribute("stDate",
				new java.sql.Date(new java.util.Date().getTime()));
		return SUCCESS;
	}

	public String exportExcel() {
		ArrayList<SaleReportBean> data = new ArrayList<SaleReportBean>();
		HttpSession session = getRequest().getSession();
		data = (ArrayList<SaleReportBean>) session
				.getAttribute("searchsaleresult");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=Sale Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			DateBeans dateBeans = (DateBeans) request.getSession()
					.getAttribute("datebean");
			System.out.println(" date bean object is=============" + dateBeans);
			WriteExcelForSaleReport excel = new WriteExcelForSaleReport(
					dateBeans);
			if (session.getAttribute("saletype") != null) {
				excel.writeAgent(data, w);
			} else {
				excel.writeGame(data, w);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getEdit() {
		return edit;
	}

	public String getEnd() {
		return end;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getGameType() {
		return gameType;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public String saleReportForBO() throws LMSException {
		String returnType;
		System.out.println("gameType = " + gameType);
		if ("DRAWGAME".equalsIgnoreCase(gameType.trim())) {
			returnType = saleReportOfDGForBO();
		} else {
			returnType = saleReportOfScratchForBO();
		}
		return returnType;
	}

	public String saleReportOfDGForBO() throws LMSException {
		List<SaleReportBean> salelist = null;
		String returnType = "dg_success";
		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		HttpSession session = request.getSession();
		DGSaleReportsHelper helper = new DGSaleReportsHelper(dateBeans);
		System.out.println("group by == " + groupBy);
		if (groupBy.equalsIgnoreCase("ALL")) {
			salelist = helper.fetchDGSaleDetail();
			session.setAttribute("saletype", null);
		} else if (groupBy.equalsIgnoreCase("AGTWISE")) {
			salelist = helper.fetchDGSaleDetailAgentWise();
			session.setAttribute("saletype", "AGT");
		} else { // when game wise selected
			salelist = helper.fetchDGSaleDetailGameWise();
			session.setAttribute("saletype", "GM");
		}

		session.setAttribute("datebean", dateBeans);
		session.setAttribute("searchResultBO", salelist);
		session.setAttribute("searchsaleresult", salelist);

		if (salelist != null && salelist.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", salelist);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
		}
		return returnType;
	}

	public String saleReportOfScratchForBO() throws LMSException {
		List<SaleReportBean> salelist = null;
		// String returnType = "success";

		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		HttpSession session = request.getSession();

		SaleReportsHelper helper = new SaleReportsHelper(dateBeans);

		if (groupBy.equalsIgnoreCase("GMWISE")) {
			salelist = helper.getSaleDetailGameWise(helper.getGameId());
			session.setAttribute("saletype", null);
		}
		// when agent wise selected
		else {
			salelist = helper.getSaleDetailAgentWise(helper.getAgentId());
			session.setAttribute("saletype", "Agent Wise");
		}

		session.setAttribute("datebean", dateBeans);

		session.setAttribute("searchResultBO", salelist);
		session.setAttribute("searchsaleresult", salelist);

		if (salelist != null && salelist.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", salelist);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
		}
		return SUCCESS;
	}

	/**
	 * This method handles the pagination(first,next,previous and last click) in
	 * the searched results.
	 * 
	 * @return String
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		List ajaxList = (List) session.getAttribute("APP_ORDER_LIST5");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			System.out.println("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;

			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("searchResultBO", ajaxSearchList);
			System.out.println("result in search ajax == "
					+ ajaxSearchList.size());
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		System.out.println("value of Edit" + edit);
		return SUCCESS;

	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEnd_Date(String end_Date) {
		System.out.println("end date called" + end_Date);
		if (end_Date != null) {
			this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStart_date(String start_date) {
		System.out.println("first date called" + start_date);
		if (start_date != null) {
			this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

}
