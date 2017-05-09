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
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.GameWisePWTBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.DGPwtReportsHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.IPwtReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.PwtReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.PwtReportsAgentHelperSP;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.PwtReportsHelper;

public class PwtReportAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

	static Log logger = LogFactory.getLog(PwtReportAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String edit = null;

	private String end = null;
	private String end_Date;
	private String gameStatus;
	private String gameType;

	private String reportType;
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

	public void executeHelperForDG(String owner) throws LMSException {
		List list = null;
		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}

		HttpSession session = request.getSession();

		DGPwtReportsHelper agentHelper = new DGPwtReportsHelper(dateBeans);
		if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
			list = agentHelper.getPwtDetailAGTWise();
		} else {
			list = agentHelper.getPwtDetailGameWise(gameStatus);
		}

		session.setAttribute("datebean", dateBeans);
		session.setAttribute("pwtResult", list);
		session.setAttribute("pwtReportTypeForPagination", reportType);

		if (list != null && list.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", list);
			session.setAttribute("pwtResultBO", list);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));

			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
			session.setAttribute("pwtResultBO", new ArrayList(0));
		}

	}

	public void executeHelperForScratch(String owner) throws LMSException, ParseException {
		List list = null;
		boolean isExpand = false;
		DateBeans dateBeans = null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBeans = GetDate.getDate(start_date, end_Date);
		} else {
			dateBeans = GetDate.getDate(totaltime);
		}
		HttpSession session = request.getSession();
		
		// PWT details for AGENT
		if (owner.equalsIgnoreCase("AGENT")) {
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
			UserInfoBean infoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			if (infoBean == null) {
				infoBean = new UserInfoBean();
				infoBean.setUserId(280);
			}
			IPwtReportsAgentHelper agentHelper = null;
			if(LMSFilterDispatcher.isRepFrmSP)
			{
			agentHelper = new PwtReportsAgentHelperSP(infoBean, dateBeans);
			}
			else
			{
			agentHelper = new PwtReportsAgentHelper(infoBean, dateBeans);	
			}
			list = agentHelper.getPwtDetail();
			// add details of claimed PWT at BO End
			String boPwtAmt = "0.0";
			if (list.get(0) != null) {
				boPwtAmt = ((PwtReportBean) list.get(0)).getPwtAmt();
			}
			session.setAttribute("boPwtAmt", boPwtAmt);
			list.remove(0);
		}
		// PWT details for BO
		else {
			PwtReportsHelper agentHelper = new PwtReportsHelper(dateBeans);
			if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
				list = agentHelper.getPwtDetail();
			} else {
				list = agentHelper.getPwtDetailGameWise(gameStatus);
			}
		}
		session.setAttribute("isExpand", isExpand);
		session.setAttribute("datebean", dateBeans);
		session.setAttribute("pwtResult", list);
		session.setAttribute("pwtReportTypeForPagination", reportType);

		if (list != null && list.size() > 0) {
			session.setAttribute("APP_ORDER_LIST5", list);
			session.setAttribute("pwtResultBO", list);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));

			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();
		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
			session.setAttribute("pwtResultBO", new ArrayList(0));
		}

	}

	public String exportExcel() {
		ArrayList<PwtReportBean> data = new ArrayList<PwtReportBean>();
		ArrayList<GameWisePWTBean> datagame = new ArrayList<GameWisePWTBean>();
		HttpSession session = request.getSession();

		data = (ArrayList<PwtReportBean>) session.getAttribute("pwtResult");
		datagame = (ArrayList<GameWisePWTBean>) session
				.getAttribute("pwtResult");

		String boPwtAmt = (String) session.getAttribute("boPwtAmt");
		if (boPwtAmt != null) {
			PwtReportBean boPwtBean = new PwtReportBean();
			boPwtBean.setPartyName("PWT Claimed At BO End");
			boPwtBean.setPwtAmt(boPwtAmt);
			data.add(0, boPwtBean);
		}

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=PWT Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForPwt excel = new WriteExcelForPwt((DateBeans) request
					.getSession().getAttribute("datebean"));

			reportType = (String) session
					.getAttribute("pwtReportTypeForPagination");
			System.out.println("*******reporttype" + reportType);
			if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
				System.out.println("in if");
				excel.writeAgentWise(data, w);
			} else {
				excel.writeGameWise(datagame, w);
			}

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

	public String getGameStatus() {
		return gameStatus;
	}

	public String getGameType() {
		return gameType;
	}

	public String getReportType() {
		return reportType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
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

	public String pwtReportForAgent() throws LMSException, ParseException {
		System.out.println("PWT report for AGENT result is executed. ");
		executeHelperForScratch("AGENT");
		return SUCCESS;
	}

	public String pwtReportForBO() throws LMSException, ParseException {
		System.out.println("PWT report for Bo result is executed. ");
		if ("DRAWGAME".equalsIgnoreCase(gameType.trim())) {
			executeHelperForDG("BO");
			if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
				return "dg_agtWise";
			} else {
				return "dg_gameWise";
			}
		} else {
			executeHelperForScratch("BO");
			if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
				return SUCCESS;
			} else {
				return "gameWise";
			}
		}

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

		System.out.println("value of Edit" + edit);
		reportType = (String) session
				.getAttribute("pwtReportTypeForPagination");
		if (reportType == null) {
			reportType = "Agent Wise";
		}
		if ("Agent Wise".equalsIgnoreCase(reportType.trim())) {
			return SUCCESS;
		} else {
			return "gameWise";
		}
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

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

}
