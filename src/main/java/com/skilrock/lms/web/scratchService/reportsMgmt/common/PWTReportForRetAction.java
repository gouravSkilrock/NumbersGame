package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.DGPwtReportsRetHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.PwtReportsRetHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class PWTReportForRetAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {
	static Log logger = LogFactory.getLog(PWTReportForRetAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String edit = null;

	private String end = null;
	private String end_Date;
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
		ArrayList<PwtReportBean> data = new ArrayList<PwtReportBean>();
		data = (ArrayList<PwtReportBean>) request.getSession().getAttribute(
				"pwtResult");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=PWT Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForPwt excel = new WriteExcelForPwt((DateBeans) request
					.getSession().getAttribute("datebean"));
			excel.writeAgentWise(data, w);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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

	public String pwtReportForRet() throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		ServletContext sc = LMSUtility.sc;
		int gameId=0;
		long lastPrintedTicket=0;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(infoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, infoBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, infoBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		PwtReportsRetHelper retHelper = new PwtReportsRetHelper(infoBean,
				dateBeans);
		Map<String, List<PwtReportBean>> pwtMap = retHelper.getPwtDetail();
		List<PwtReportBean> plrPwtList = pwtMap.get("plrPwtDtlList");
		session.setAttribute("datebean", dateBeans);
		session.setAttribute("pwtResult", plrPwtList);
		List<PwtReportBean> agtPwtList = pwtMap.get("agtPwtDtlList");
		String agtPwtAmt = "0.0";
		if (agtPwtList.get(0) != null) {
			agtPwtAmt = agtPwtList.get(0).getPwtAmt();
		}
		session.setAttribute("agtPwtAmt", agtPwtAmt);

		if (plrPwtList != null && plrPwtList.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", plrPwtList);
			session.setAttribute("pwtResultRet", plrPwtList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
			session.setAttribute("pwtResultRet", new ArrayList(0));
		}

		return SUCCESS;
	}

	public String pwtReportOfDGForRet() throws LMSException {
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		DGPwtReportsRetHelper retHelper = new DGPwtReportsRetHelper(infoBean,
				dateBeans);
		Map<String, List<PwtReportBean>> pwtMap = retHelper.getPwtDetail();
		List<PwtReportBean> plrPwtList = pwtMap.get("plrPwtDtlList");
		session.setAttribute("datebean", dateBeans);
		session.setAttribute("pwtResult", plrPwtList);
		List<PwtReportBean> agtPwtList = pwtMap.get("agtPwtDtlList");
		String agtPwtAmt = "0.0";
		if (agtPwtList.get(0) != null) {
			agtPwtAmt = agtPwtList.get(0).getPwtAmt();
		}
		session.setAttribute("agtPwtAmt", agtPwtAmt);

		if (plrPwtList != null && plrPwtList.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", plrPwtList);
			session.setAttribute("pwtResultRet", plrPwtList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
			session.setAttribute("pwtResultRet", new ArrayList(0));
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
			session.setAttribute("pwtResult", ajaxSearchList);
			System.out.println("result in search ajax == "
					+ ajaxSearchList.size());
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}

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
