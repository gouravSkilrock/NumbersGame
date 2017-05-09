package com.skilrock.lms.web.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CashierDrawerDataForPWTBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsAgentHelperSPGhana;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICashChqReportsAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class CashCheqReportActionGhana extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String edit = null;

	private String end = null;
	private String end_Date;
	Log logger = LogFactory.getLog(CashCheqReportActionGhana.class);
	private int orgId;
	private String orgName;
	private String reportType;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String searchResultsAvailable;

	private int start = 0;

	private String start_date;

	private String totaltime;
	private String lastDate;
	private String userData;
	private String userName;
	private String cashierType;
	String[] type = { "Daily", "Weekly", "Monthly" };
	private List<CashChqReportBean> list1;
	public String cashChqForBOAgtWise() throws LMSException, ParseException {
		boolean isExpand1 = false;
		boolean isExpand = false;
		HttpSession session = request.getSession();
		//ICashChqReportsHelper helper = null;
		lastDate = CommonMethods.getLastArchDate();		
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		Calendar calStart1 = Calendar.getInstance();
		Calendar calTest = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
		Date devLastDate = format.parse(lastDate);
		calTest.setTime(frmt.parse(start_date));
		Date devStartDate = format.parse(format.format(calTest.getTime()));
		Date devStartDate1 = format.parse(end_Date);
		calStart.setTime(devStartDate);
		calStart1.setTime(devStartDate1);
		calLast.setTime(devLastDate);
		if(calStart.before(calLast) || calStart.equals(calLast))
		{
			isExpand1 = true;
		}
		else if(calStart.after(calLast))
		{
			isExpand1 = false;
		}
		if(calStart1.before(calLast) || calStart1.equals(calLast))
		{
			isExpand = true;
		}
		else if(calStart1.after(calLast))
		{
			isExpand = false;
		}
		session.setAttribute("isExpand", isExpand);

		/*if(LMSFilterDispatcher.isRepFrmSP)
		{
		helper = new CashChqReportsHelperSP((DateBeans) session.getAttribute("datebean"));
		}
		else
		{*/
		CashChqReportsHelper helper = new CashChqReportsHelper((DateBeans) session.getAttribute("datebean"));
		//}
		List list = helper.getCashChqDetailAgentWise(end_Date, end_Date+ " 23:59:59",-1,isExpand1,"","");
	
		session.setAttribute("searchCashChqResultDate", list);
		return SUCCESS;
	}

	public String cashChqReportForAgent() throws LMSException, ParseException {
		logger.debug("Cash & Cheque report for AGENT result is executed. ");
		// System.out.println("Cash & Cheque report for AGENT result is
		// executed. ");
		executeHelper("AGENT");
		return SUCCESS;
	}

	public String cashChqReportForBO() throws LMSException, ParseException {
		logger.debug("Cash & Cheque report for Bo result is executed. ");
		// System.out.println("Cash & Cheque report for Bo result is executed.
		// ");
		executeHelper("BO");
		return SUCCESS;
	}

	public String cashChqReportForBODate() throws LMSException, ParseException {
		boolean isExpand = false;
		HttpSession session = request.getSession();
		/*ICashChqReportsHelper helper = null;
		if(LMSFilterDispatcher.isRepFrmSP)
		{
		helper = new CashChqReportsHelperSP((DateBeans) session.getAttribute("datebean"));
		}
		else
		{*/
		CashChqReportsHelper helper = new CashChqReportsHelper((DateBeans) session.getAttribute("datebean"));
		//}
	
		List list = helper.getCashChqDetailDateWise(orgId,-1);
		start_date = ((DateBeans) session.getAttribute("datebean")).getFirstdate().toString();
		lastDate = CommonMethods.getLastArchDate();
		System.out.println("last archieve date"+lastDate);
		System.out.println("last archieve date"+start_date);
		
		System.out.println("last archieve date"+lastDate);
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date devLastDate = format.parse(lastDate);
		Date devStartDate = format.parse(start_date);
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
		session.setAttribute("isExpand", isExpand);

		session.setAttribute("searchCashChqResultDate", list);
		return SUCCESS;
	}
	public String execute() {
		request.getSession().setAttribute("stDate",new java.sql.Date(new java.util.Date().getTime()));
		return SUCCESS;
	}

	public void executeHelper(String owner) throws LMSException, ParseException {
		HttpSession session = request.getSession();
		DateBeans dateBeans = GetDate.getDate(totaltime);
		dateBeans.setStartDate(new java.util.Date());
		dateBeans.setEndDate(new java.util.Date());
		SimpleDateFormat sdf=null;
		Timestamp startDate=null;
		Timestamp endDate=null;
		List<CashChqReportBean> list2=null;
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			SimpleDateFormat utilDateFormat = new SimpleDateFormat((String) session.getAttribute("date_format"));
			try {
				dateBeans.setStartDate(utilDateFormat.parse(start_date));
				dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(start_date).getTime()));
				dateBeans.setEndDate(utilDateFormat.parse(end_Date));
				dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(utilDateFormat.parse(end_Date)).getTime()));
				dateBeans.setReportType("");
			} catch (ParseException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		if (owner.equalsIgnoreCase("AGENT")) {
			UserInfoBean infoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			if (infoBean == null) {
				infoBean = new UserInfoBean();
				infoBean.setUserId(280);
			}
			logger.debug(" Inside agent block");
			
			ICashChqReportsAgentHelper agentHelper = null;
			
			if (LMSFilterDispatcher.isRepFrmSP) {
				agentHelper = new CashChqReportsAgentHelperSPGhana(infoBean, dateBeans);
			} else {
				agentHelper = new CashChqReportsAgentHelper(infoBean, dateBeans);
			}
			list1 = agentHelper.getCashChqDetail();
			CashChqReportBean cashChqBean = agentHelper.getCashChqDetailWithBO();
			session.setAttribute("agentbocashchqdetail", cashChqBean);
			session.setAttribute("cashchqDetail", list1);
			session.setAttribute("agentcashchq", "agent");
		} else {
			CashChqReportsHelper helper = new CashChqReportsHelper(dateBeans);
			if ("Agentwise".equalsIgnoreCase(reportType)) {
				list1 = helper.getCashChqDetail("","");
			} else if("Daywise".equalsIgnoreCase(reportType)){
				list1 = helper.getCashChqDetailDayWise(0, false);
			} else if("Userwise".equalsIgnoreCase(reportType)){
				list1 = helper.getCashChqDetailUserWise();
			} 
			session.setAttribute("reportType", reportType);
		}

		session.setAttribute("datebean", dateBeans);
		session.setAttribute("orgName", ((UserInfoBean) session.getAttribute("USER_INFO")).getOrgName());
		session.setAttribute("orgAdd", ReportUtility.getOrgAdd(((UserInfoBean) session.getAttribute("USER_INFO")).getUserOrgId()));
	    sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        startDate=new Timestamp(sdf.parse(start_date+" 00:00:00").getTime());
        endDate=new Timestamp(sdf.parse(end_Date+" 23:59:59").getTime());
        OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
        List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
        list2=new ArrayList<CashChqReportBean>();
        list2.addAll(list1);
        CashChqReportBean bean;	
	    for(Object o:list2){
	    	bean=(CashChqReportBean)o;
			if(terminateAgentList.contains(bean.getOrgId())){
				list1.remove(bean);
			}
	   }
	   // System.out.println("On Action:"+list1.size());
		session.setAttribute("searchCashChqResult", list1);
	}

	public String exportExcel() {
		ArrayList<CashChqReportBean> data = new ArrayList<CashChqReportBean>();
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();
		data = (ArrayList<CashChqReportBean>) session.getAttribute("searchCashChqResult");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename=Cash Cheque Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
			WriteExcelForCashChqGhana excel = new WriteExcelForCashChqGhana((DateBeans) request.getSession().getAttribute("datebean"));
			Map<Integer, List<CashChqPmntBean>> detailDataMap = new LinkedHashMap<Integer, List<CashChqPmntBean>>();
			Map<String, List<CashChqPmntBean>> detailDataList2 = new TreeMap<String, List<CashChqPmntBean>>();
			CashChqReportsHelper helper = new CashChqReportsHelper((DateBeans) session.getAttribute("datebean"));
			if (((String) session.getAttribute("reportType")).equalsIgnoreCase("Agentwise") || ((String) session.getAttribute("reportType")).equalsIgnoreCase("Userwise")) {
				helper.getCashChqDetailAgentWise(-1 , detailDataMap,null, null);
				excel.writeFullDetailDateWise(data,	detailDataMap, w, (String) session.getAttribute("orgName"), (String) session.getAttribute("orgAdd"));
			} else {
				Calendar c1 = Calendar.getInstance();
				DateBeans dbean = (DateBeans) session.getAttribute("datebean");
				c1.setTime(dbean.getStartDate());
				long diffDays = (dbean.getEndDate().getTime() - dbean.getStartDate().getTime())/ (24 * 60 * 60 * 1000);
				long i = 0L;
				while (i <= diffDays) {
					detailDataList2.put((new java.sql.Date(c1.getTime().getTime())).toString(), helper.getCashChqDetailAgentWise((new java.sql.Date(c1.getTime().getTime())).toString(),(new java.sql.Date(c1.getTime().getTime()+ 24 * 60 * 60 * 1000)).toString(),-1,false, null, null));
					c1.add(Calendar.DAY_OF_MONTH, 1);
					i++;
				}
				excel.writeFullDetailDayWise(data, detailDataList2, w,(String) session.getAttribute("orgName"),(String) session.getAttribute("orgAdd"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String exportExcelForAgent() {
		List<CashChqReportBean> data = new ArrayList<CashChqReportBean>();
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();
		data = (List<CashChqReportBean>)session.getAttribute("cashchqDetail");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename=Cash Cheque Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
			WriteExcelForCashChqGhana excel = new WriteExcelForCashChqGhana((DateBeans) request.getSession().getAttribute("datebean"));
			  if (session.getAttribute("agentcashchq") == null){
				  excel.write(data, w, (String)session.getAttribute("orgName"), (String)session.getAttribute("orgAdd")); 
			  }
			  else { 
				  CashChqReportBean agentBoDetail =	(CashChqReportBean) session.getAttribute("agentbocashchqdetail"); 
				  excel.writeAgent(data,agentBoDetail, w); 
			  }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void createExcelForPwtTicketsCashierWise() {
		WritableWorkbook w = null;
		WriteExcelForCashChq excel = null;
		HttpSession session = null;
		DateBeans dateBeans = null;
		CashChqReportsHelper helper = null;
		List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWise = null;
		try {
			session = getRequest().getSession();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename=CashierWiseClaimedTicketsDetail.xls");
			w = Workbook.createWorkbook(response.getOutputStream());
			dateBeans = (DateBeans) request.getSession().getAttribute("datebean");
			dateBeans.setReportType("PWT Details Cashier Wise");
			excel = new WriteExcelForCashChq(dateBeans);
			helper = new CashChqReportsHelper(dateBeans);
			detailsForPwtTicketsCashierWise = helper.getDetailsForPwtTicketsCashierWise(dateBeans, cashierType,((UserInfoBean) session.getAttribute("USER_INFO")).getUserId());
			excel.writeFullDetailForPwtTicketsCashierWise(detailsForPwtTicketsCashierWise, w, (String) session.getAttribute("orgName"), (String) session.getAttribute("orgAdd"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public int getOrgId() {
		return orgId;
	}

	public String getOrgName() {
		return orgName;
	}
	public String getLastDate()
	{
		return lastDate;
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
			logger.debug("List Size " + ajaxList.size());
			// System.out.println("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				logger.debug("i m in first");
				// System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				logger.debug("i m in Previous");
				// System.out.println("i m in Previous");
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				logger.debug("i m in Next");
				// System.out.println("i m in Next");
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
			logger.debug("End value" + endValue);
			logger.debug("Start Value" + startValue);

			// System.out.println("End value" + endValue);
			// System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("cashChqResultBO", ajaxSearchList);
			logger.debug("result in search ajax == " + ajaxSearchList.size());

			// System.out.println("result in search ajax ==
			// "+ajaxSearchList.size());
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		logger.debug("value of Edit" + edit);
		// System.out.println("value of Edit" + edit);
		return SUCCESS;

	}

	public String cashChqForBOUserWise() throws LMSException, ParseException, SQLException {
		HttpSession session = request.getSession();
		CashChqReportsHelper helper = new CashChqReportsHelper((DateBeans) session.getAttribute("datebean"));
		List<Integer> agtOrgIdList = helper.getAgentOrgList();
		//Map<Integer, String> agtNameOrgIdMap = new TreeMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		boolean isExpand = false;
		start_date = ((DateBeans) session.getAttribute("datebean")).getFirstdate().toString();
		lastDate = CommonMethods.getLastArchDate();
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date devLastDate = format.parse(lastDate);
		Date devStartDate = format.parse(start_date);
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
		session.setAttribute("isExpand", isExpand);
		/*for (Integer orgId : agtOrgIdList) {
			agtNameOrgIdMap.put(orgId, ReportUtility.getOrgNameFromOrgId(con,orgId));
		}*/
		List list = helper.getCashChqDetailUserAgentWise(agtOrgIdList,orgId);
		session.setAttribute("cashChqResultUser", list);
		session.setAttribute("username", userName);
		return SUCCESS;
	}
	
	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEnd_Date(String end_Date) {
		logger.debug("end date called" + end_Date);
		// System.out.println("end date called"+end_Date);
		if (end_Date != null) {
			// this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
			this.end_Date = end_Date;
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public void setLastDate(String lastDate)
	{
		this.lastDate = lastDate;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
		logger.debug("first date called" + start_date);
		// System.out.println("first date called"+start_date);
		if (start_date != null) {
			// this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
			this.start_date = start_date;
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCashierType() {
		return cashierType;
	}

	public void setCashierType(String cashierType) {
		this.cashierType = cashierType;
	}

	public List getList1() {
		return list1;
	}

	public void setList1(List list1) {
		this.list1 = list1;
	}

	
}
