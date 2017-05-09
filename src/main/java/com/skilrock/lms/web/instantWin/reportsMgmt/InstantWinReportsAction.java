package com.skilrock.lms.web.instantWin.reportsMgmt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.IWUserIncentiveBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.WriteExcelForAgentIWIncentiveReport;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * This is action class for Instant Winning Reports.
 * @author Mukesh
 *
 */
public class InstantWinReportsAction  extends ActionSupport implements ServletRequestAware,ServletResponseAware{	
	
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(InstantWinReportsAction.class);

	
	private HashMap<Integer,IWUserIncentiveBean> agentMap;
	private HashMap<Integer,IWUserIncentiveBean> retailerMap;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int orgId;
	private String fromDate;
	private String toDate;
	private String weekDate;
	private double totalSale;
	private double totalWinning;
	private double totalIncentive;
	private String reportData;
	private String userType;
	private String reportType;
	private String orgName;
	
	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}
	
	public HashMap<Integer, IWUserIncentiveBean> getAgentMap() {
		return agentMap;
	}

	public void setAgentMap(HashMap<Integer, IWUserIncentiveBean> agentMap) {
		this.agentMap = agentMap;
	}

	public HashMap<Integer, IWUserIncentiveBean> getRetailerMap() {
		return retailerMap;
	}

	public void setRetailerMap(HashMap<Integer, IWUserIncentiveBean> retailerMap) {
		this.retailerMap = retailerMap;
	}

	public Double getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(Double totalSale) {
		this.totalSale = totalSale;
	}

	public Double getTotalWinning() {
		return totalWinning;
	}

	public void setTotalWinning(Double totalWinning) {
		this.totalWinning = totalWinning;
	}

	public Double getTotalIncentive() {
		return totalIncentive;
	}

	public void setTotalIncentive(Double totalIncentive) {
		this.totalIncentive = totalIncentive;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(String weekDate) {
		this.weekDate = weekDate;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * This method fetches all agents incentive data
	 * @return SUCCESS
	 * @throws LMSException
	 * @author Rishi
	 */
	public String getAgentWiseIncentiveData() throws LMSException {
		logger.info("In method getAgentWiseIncentiveData()");
		HttpSession session = null;
		InstantWinReportsHelper iwHelper = null;
		String getWeekDates = null;
		try {
			session = request.getSession();
			agentMap =  new HashMap<Integer,IWUserIncentiveBean>();
			iwHelper = new InstantWinReportsHelper();
			if("Weekly".equalsIgnoreCase(reportType)){
				getWeekDates = fetchDate(weekDate.replaceAll("/", "-"), "WEEKLY");
				iwHelper.getAgentData(agentMap, (getWeekDates.split("Nxt")[0]).split(" ")[0], (getWeekDates.split("Nxt")[1]).split(" ")[0], reportType);
				session.setAttribute("startDate", getWeekDates.split("Nxt")[0]);
				session.setAttribute("endDate", getWeekDates.split("Nxt")[1]);
				setFromDate(((getWeekDates.split("Nxt")[0]).split(" ")[0]).replaceAll("-", "/"));
				setToDate(((getWeekDates.split("Nxt")[1]).split(" ")[0]).replaceAll("-", "/"));
			}else{
				iwHelper.getAgentData(agentMap, fromDate.replaceAll("/", "-"), toDate.replaceAll("/", "-"), reportType);
				session.setAttribute("startDate", fromDate);
				session.setAttribute("endDate", toDate);
			}
			session.setAttribute("agentRetailersMap", agentMap);
			logger.info("AgentWise Map : "+ agentMap);			
		} catch (LMSException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return SUCCESS;
	}
	
	/**
	 * This method fetches all retailers data corressponding to a particular agent 
	 * @return SUCCESS
	 * @throws LMSException
	 * @author Rishi
	 */
	public String getRetailerWiseIncentiveData() throws LMSException{
		logger.info("In method getRetailerWiseIncentiveData()");
		InstantWinReportsHelper iwHelper = null;
		try{
			iwHelper = new InstantWinReportsHelper();
			retailerMap = new HashMap<Integer, IWUserIncentiveBean>();
			iwHelper.getRetailerData(retailerMap,fromDate,toDate,orgId,reportType,orgName);
			logger.info("Retailer Map : "+ retailerMap);
		}catch (LMSException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return SUCCESS;
	}
	
	/**
	 * This method creates excel sheet for agent data.
	 * @author Rishi
	 */
	public void exportToExcelAgentIncentive(){
		logger.info("In exportToExcelAgentIncentive Method");
		HttpSession session = null;
		HashMap<Integer,IWUserIncentiveBean> agentMap = null;
		String startDate = null;
		String endDate = null;
		try {		 
			session = request.getSession();
			agentMap = (HashMap<Integer,IWUserIncentiveBean>) session.getAttribute("agentRetailersMap");
			startDate = (String) session.getAttribute("startDate");
			endDate = (String) session.getAttribute("endDate");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename="+userType+" Incentive Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
			WriteExcelForAgentIWIncentiveReport excel = new WriteExcelForAgentIWIncentiveReport();
			excel.write(agentMap, startDate, endDate, w);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method creates excel sheet for retailers
	 * @return
	 * @author Rishi
	 */
	public String exportToExcelRetailerIncentive(){
		logger.info("In exportToExcelRetailerIncentive Method");
		try {		    
		    response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename="+userType+" Incentive Report.xls");
			PrintWriter out = response.getWriter();
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").replaceAll("Sort", "").trim();
			out.write(reportData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method returns the range of week for a particular date
	 * @param tDate
	 * @param type
	 * @return startDate + "Nxt" + endDate
	 * @author Rishi
	 */
	public String fetchDate(String tDate, String type) {
		Timestamp startDate = null;
		Timestamp endDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ("WEEKLY".equalsIgnoreCase(type)) {
				cal.setTimeInMillis(sdf.parse(tDate).getTime());
				if(cal.get(Calendar.DAY_OF_WEEK) != 1)
					cal.add(Calendar.DAY_OF_WEEK,-(cal.get(Calendar.DAY_OF_WEEK) - 2));
				else
					cal.add(Calendar.DAY_OF_WEEK,-6);
				startDate = new Timestamp(sdf.parse(new java.sql.Date(cal.getTimeInMillis()).toString()).getTime());
				cal.add(Calendar.DAY_OF_MONTH, +6);
				endDate = new Timestamp(sdf.parse(new java.sql.Date(cal.getTimeInMillis()).toString()).getTime() + 24 * 60 * 60 * 1000 - 1000);
				return startDate + "Nxt" + endDate;
			} else {
				startDate = new Timestamp(sdf.parse(tDate).getTime());
				endDate = new Timestamp(sdf.parse(tDate).getTime() + 24 * 60 * 60 * 1000 - 1000);
				return startDate + "Nxt" + endDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method provides retailer wise incentive information for an agent at AGENT end.
	 * @return SUCCESS
	 * @throws LMSException
	 * @author Rishi 
	 */
	public String fetchRetailerWiseDataForAgent() throws LMSException{
		logger.info("In method fetchRetailerWiseDataForAgent()");
		HttpSession session = null;
		UserInfoBean userBean = null;
		InstantWinReportsHelper iwHelper = null;
		String getWeekDates = null;
		try{
			iwHelper = new InstantWinReportsHelper();
			session = request.getSession();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			retailerMap = new HashMap<Integer, IWUserIncentiveBean>();
			if("Weekly".equalsIgnoreCase(reportType)){
				getWeekDates = fetchDate(weekDate.replaceAll("/", "-"), "WEEKLY");
				iwHelper.getRetailerData(retailerMap,(getWeekDates.split("Nxt")[0]).split(" ")[0], (getWeekDates.split("Nxt")[1]).split(" ")[0],userBean.getUserOrgId(),reportType,userBean.getOrgName());
				setFromDate(((getWeekDates.split("Nxt")[0]).split(" ")[0]).replaceAll("-", "/"));
				setToDate(((getWeekDates.split("Nxt")[1]).split(" ")[0]).replaceAll("-", "/"));
			}else{
				iwHelper.getRetailerData(retailerMap,fromDate.replaceAll("/", "-"),fromDate.replaceAll("/", "-"),userBean.getUserOrgId(),reportType,userBean.getOrgName());
			}
			setOrgName(userBean.getOrgName());
			logger.info("Retailer Map: "+retailerMap);
		}catch(LMSException e){
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return SUCCESS;
	}
	
}