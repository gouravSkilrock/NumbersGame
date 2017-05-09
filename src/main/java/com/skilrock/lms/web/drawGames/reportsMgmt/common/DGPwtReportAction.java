package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.DirPlrPwtRepBean;
import com.skilrock.lms.beans.PwtDetailsBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGPwtReportHelper;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawWinTktsBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DGPwtReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DGPwtReportAction.class);
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
		// System.out.println(calendarNew.getTime());
	}

	private int AgentId;
	private String agtOrgName;
	List<DirPlrPwtRepBean> dgDirPwtRepBean;
	private String end_Date;
	private String filter;
	List<PwtReportBean> pwtDetail;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String archDate;
	private String depDate;
	private String dateOfDraw;
	private String startDate;
	private int agentOrgId;
	private String gameNo;
	private Map<String,String> orgCodeNameMap;
	private Map<String,String> paymentDateMap;
	private Map<String, String> parentOrgNameMap;
	private Map<Integer,DrawWinTktsBean> drawBeanMap;
	
	public void ajaxAgentListForPwt() throws LMSException, IOException {
		Map<Integer, String> map = new TreeMap<Integer, String>();
		DGPwtReportHelper dgSale = new DGPwtReportHelper();
		PrintWriter out = getResponse().getWriter();

		map = dgSale.ajaxAgentListForPwt();
		logger.debug("agent list ajax" + map);
		// System.out.println("agent list ajax"+map);
		String html = "";
		html = "Select Agent: <select class=\"option\" name=\"AgentId\"><OPTION VALUE=-1>--Please Select--";
		int i = 0;
		for (Object element : map.entrySet()) {
			Map.Entry<Integer, String> mypair = (Map.Entry<Integer, String>) element;
			i++;
			html += "<option class=\"option\" value=\"" + mypair.getKey()
					+ "\">" + mypair.getValue() + "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");
		out.print(html);
	}

	public String createPwtDetailReport() throws LMSException, ParseException {
		DateBeans dateBean1 = new DateBeans();
		HttpSession session = getRequest().getSession();
		dateBean1 = (DateBeans) session.getAttribute("datebean");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGPwtReportHelper dgPwt = new DGPwtReportHelper(userInfoBean, dateBean1);
		List<PwtDetailsBean> pwtBean = dgPwt.fetchClaimTicketData(agtOrgName,
				filter);
		session.setAttribute("searchResult", pwtBean);
		return SUCCESS;
	}

	public String createReport() throws LMSException, ParseException {
		DateBeans dateBean1 = new DateBeans();
		HttpSession session = getRequest().getSession();
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totaltime);
		}

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DGPwtReportHelper dgPwt = new DGPwtReportHelper(userInfoBean, dateBean1);
		if ("Game Wise".equalsIgnoreCase(filter)) {
			pwtDetail = dgPwt.getPwtDetailGameWise();
			dgDirPwtRepBean = dgPwt.getDirPlrPwtDetailGameWise();
		}
		if ("Agent Wise".equalsIgnoreCase(filter)) {
			pwtDetail = dgPwt.getPwtDetailAgentWise();
		}
		if ("Retailer Wise".equalsIgnoreCase(filter)) {
			pwtDetail = dgPwt.getPwtDetailRetailerWise(AgentId);
		}
		session.setAttribute("datebean", dateBean1);
		session.setAttribute("searchResultRet", pwtDetail);
		session.setAttribute("dgDirPlrPwtRepList", dgDirPwtRepBean);
		session.setAttribute("orgName", userInfoBean.getOrgName());
		session.setAttribute("orgAdd", dgPwt.getOrgAdd(userInfoBean
				.getUserOrgId()));
		session.setAttribute("filter", filter);
		return SUCCESS;
	}

	public void exportExcel() {
		HttpSession session = request.getSession();
		List<PwtReportBean> data = new ArrayList<PwtReportBean>();
		List<DirPlrPwtRepBean> dataDirPlr = new ArrayList<DirPlrPwtRepBean>();
		dataDirPlr = (ArrayList) request.getSession().getAttribute(
				"dgDirPlrPwtRepList");
		data = (ArrayList) request.getSession().getAttribute("searchResultRet");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=DGPWTReport.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForDGPwtReport excel = new WriteExcelForDGPwtReport(
					(DateBeans) session.getAttribute("datebean"));

			excel.write(data, dataDirPlr, w, (String) session
					.getAttribute("orgName"), (String) session
					.getAttribute("orgAdd"), "BO", (String) request
					.getSession().getServletContext().getAttribute(
							"CURRENCY_SYMBOL"), (String) session
					.getAttribute("filter"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String fetchRetaierWiseWinningTicketsMenu() throws LMSException,ParseException{
		getRequest().getSession().setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)LMSUtility.sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)LMSUtility.sc.getAttribute("date_format")));
		return SUCCESS;
	}
	
	public String fetchRetaierWiseWinningTicketsReport() throws Exception {
		logger.info("startDate is" + start_date + " endDate is" + end_Date
				+ " agentOrgId " + agentOrgId + " gameNo" + gameNo);
		try {
			int gameId = Integer.parseInt(gameNo);
			if (gameId > 0 && start_date != null && agentOrgId != 0) {
				DrawDataBean drawDataBean = new DrawDataBean();
				drawDataBean.setAgentOrgId(agentOrgId);
				// The to date has been Hard Code to Avoid Use of calendar and
				// appended
				// with 23:59:59 to achieve the data
				// corresponding to the midnight of given date
				drawDataBean.setFromDate(start_date + " 00:00:00");
				drawDataBean.setToDate(end_Date + " 23:59:59");
				drawDataBean.setGameNo(gameId);
				String refMerchantId = (String) LMSUtility.sc.getAttribute("REF_MERCHANT_ID");
				drawDataBean.setMerchantId(refMerchantId);
				DGPwtReportHelper dgPwt = new DGPwtReportHelper();
				orgCodeNameMap = new HashMap<String, String>();
				paymentDateMap = new HashMap<String, String>();
				parentOrgNameMap=new HashMap<String, String>();
				drawBeanMap = dgPwt.fetchRetailerWiseWinningTicketsInfo(drawDataBean,orgCodeNameMap,paymentDateMap,parentOrgNameMap);
				return SUCCESS;
			}else {
				logger.info("Incorrect Inputs ");
				addActionMessage("Please Enter Correct Values");
				return INPUT;
				}
		} catch (LMSException el) {
			el.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", getText(el.getErrorMessage()));
			return "applicationLMSException";
		}catch(Exception e){
			e.printStackTrace();
		}
		return ERROR;
		
	}

	public int getAgentId() {
		return AgentId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
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

	public void setAgentId(int agentId) {
		AgentId = agentId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
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

	public String getArchDate() {
		return archDate;
	}

	public void setArchDate(String archDate) {
		this.archDate = archDate;
	}

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	public String getDateOfDraw() {
		return dateOfDraw;
	}

	public void setDateOfDraw(String dateOfDraw) {
		this.dateOfDraw = dateOfDraw;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public Map<String, String> getOrgCodeNameMap() {
		return orgCodeNameMap;
	}

	public void setOrgCodeNameMap(Map<String, String> orgCodeNameMap) {
		this.orgCodeNameMap = orgCodeNameMap;
	}

	public Map<String, String> getPaymentDateMap() {
		return paymentDateMap;
	}

	public void setPaymentDateMap(Map<String, String> paymentDateMap) {
		this.paymentDateMap = paymentDateMap;
	}

	public Map<Integer, DrawWinTktsBean> getDrawBeanMap() {
		return drawBeanMap;
	}

	public void setDrawBeanMap(Map<Integer, DrawWinTktsBean> drawBeanMap) {
		this.drawBeanMap = drawBeanMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Map<String, String> getParentOrgNameMap() {
		return parentOrgNameMap;
	}
	public void setParentOrgNameMap(Map<String, String> parentOrgNameMap) {
		this.parentOrgNameMap = parentOrgNameMap;
	}
	
	
}
