package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ISaleReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.SaleReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.SaleReportsAgentHelperSP;

public class SaleReportsAgentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(SaleReportsAgentAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String edit = null;
	private String end = null;
	private String end_Date;
	private String groupBy;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String searchResultsAvailable;

	private int start = 0;

	private String start_date;

	private String totaltime;
	private String lastDate;
	String[] type = { "Daily", "Weekly", "Monthly" };

	@Override
	public String execute() {
		request.getSession().setAttribute("stDate",
				new java.sql.Date(new java.util.Date().getTime()));
		return SUCCESS;
	}

	public String exportExcel() {
		HttpSession session = getRequest().getSession();
		ArrayList<SaleReportBean> data = new ArrayList<SaleReportBean>();
		SaleReportBean purchDetail = (SaleReportBean) getRequest().getSession()
				.getAttribute("purchased");
		data = (ArrayList<SaleReportBean>) getRequest().getSession()
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
				excel.writeAgentExcelRetailerWise(data, purchDetail, w);
			} else {
				excel.writeAgentExcelGameWise(data, w);
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

	/**
	 * @return the groupBy
	 */
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

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String saleReportForAgent() throws ParseException {

		HttpSession session = request.getSession();
		boolean isExpand = false;
		DateBeans dateBeans = GetDate.getDate(totaltime);
		if ("Date Wise".equalsIgnoreCase(totaltime)) {

			SimpleDateFormat utilDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateBeans.setStartDate(utilDateFormat.parse(start_date));
				dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(
						start_date).getTime()));

				dateBeans.setEndDate(utilDateFormat.parse(end_Date));
				dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
						utilDateFormat.parse(end_Date)).getTime()));
				dateBeans.setReportType("");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (infoBean == null) {
			infoBean = new UserInfoBean();
			infoBean.setUserId(280);
		}

		System.out.println("Agent user Id : " + infoBean.getUserId()
				+ "   agent Org id : " + infoBean.getUserOrgId());
		List<SaleReportBean> salelist;
		ISaleReportsAgentHelper helper = null;
		lastDate = CommonMethods.getLastArchDate();
		System.out.println("last archieve date"+lastDate);
		SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
		Date oldDate = formatOld.parse(start_date);
		System.out.println("last archieve date"+lastDate);
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date devLastDate = format.parse(lastDate);
		Date devStartDate = format.parse(format.format(oldDate));
		calStart.setTime(devStartDate);
		calLast.setTime(devLastDate);
		if(calStart.before(calLast) || calStart.equals(calLast))
		{
			isExpand = true;
		}
		else if(calStart.after(calLast))
		{
			isExpand = false;
		}
	
		if(LMSFilterDispatcher.isRepFrmSP)
		{
		helper = new SaleReportsAgentHelperSP(infoBean,dateBeans);
		}
		else
		{
			helper = new SaleReportsAgentHelper(infoBean, dateBeans);
		}
		if (!groupBy.equalsIgnoreCase("Retailer Wise")) {
			salelist = helper.getSaleDetailGameWise(helper.getGameId());
			session.setAttribute("saletype", null);
			session.setAttribute("isExpand", isExpand);
		} else {
			SaleReportBean purchWithBO = helper.getPurchaseDetailWithBo();
			salelist = helper.getSaleDetailRetailerWise(helper.getRetailerId());
			session.setAttribute("saletype", "Retailer Wise");
			if (purchWithBO != null) {
				session.setAttribute("purchased", purchWithBO);
			} else {
				session.setAttribute("purchased", null);
			}
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

	/**
	 * @param groupBy
	 *            the groupBy to set
	 */
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
