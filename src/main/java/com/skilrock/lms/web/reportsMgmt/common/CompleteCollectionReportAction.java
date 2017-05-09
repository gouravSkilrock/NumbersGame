package com.skilrock.lms.web.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CompleteCollectionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CompleteCollectionReportHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICompleteCollectionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

public class CompleteCollectionReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(CompleteCollectionReportAction.class);
	private static final long serialVersionUID = 1L;
	private int agtId;
	private String agtOrgName;
	private String dateType;
	private String end_Date;
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private String orgType;

	public String collectionRetailerWise() throws LMSException {
		logger.debug("----------Retailer Wise---Agent Id--" + agtId);

		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		if ("SQL".equals(dateType)) {
			dateFormat = "yyyy-MM-dd";
		}
		Timestamp startDate = null;
		Timestamp endDate = null;

		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
	
				ICompleteCollectionReportHelper helper = null;
	
				if (LMSFilterDispatcher.isRepFrmSP) {
					helper = new CompleteCollectionReportHelperSP();
				} else {
					helper = new CompleteCollectionReportHelper();
				}
				UserInfoBean userInfoBean = (UserInfoBean) session
						.getAttribute("USER_INFO");
				if ("Retailer Wise".equals(reportType) && agtId != 0) {
					Map<String, Double> dirPlrPwtMap = helper.agentDirectPlayerPwt(
							startDate, endDate, agtId, reportStatusBean);
					Double drawDirPlyPwt = dirPlrPwtMap.get("DG");
					Double scratchDirPlyPwt = dirPlrPwtMap.get("SE");
					Double sleDirPlyPwt = dirPlrPwtMap.get("SLE");
					Double iwDirPlyPwt = dirPlrPwtMap.get("IW");
					Double vsDirPlyPwt = dirPlrPwtMap.get("VS");
					session.setAttribute("drawDirPlyPwt", drawDirPlyPwt);
					session.setAttribute("scratchDirPlyPwt", scratchDirPlyPwt);
					session.setAttribute("sleDirPlyPwt", sleDirPlyPwt);
					session.setAttribute("iwDirPlyPwt", iwDirPlyPwt);
					session.setAttribute("vsDirPlyPwt", vsDirPlyPwt);
					session.setAttribute("AgtOrgName", agtOrgName);
					session.setAttribute("AgtOrgAdd", ReportUtility.getOrgAdd(agtId));
				} else {
					session.setAttribute("AgtOrgName", userInfoBean.getOrgCode());
					session.setAttribute("AgtOrgAdd", ReportUtility.getOrgAdd(userInfoBean
							.getUserOrgId()));
				}
				session.setAttribute("orgName", userInfoBean.getOrgCode());
				session.setAttribute("orgAdd", ReportUtility.getOrgAdd(userInfoBean
						.getUserOrgId()));
				session.setAttribute("reportType", reportType);
	
				Map<String, CompleteCollectionBean> reportMap = helper
						.collectionReportForAgent(startDate, endDate, agtId,
								reportType);
				
				//remove terminate Agent
				OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
				List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
		        logger.info("Terminate agent List::"+terminateAgentList);
				Set<String> agentListSet=reportMap.keySet();
				agentListSet.removeAll(terminateAgentList);
				
				logger.debug("----------reportMap-----" + reportMap);
				session.setAttribute("result_retailer", reportMap);
				DateBeans dBean = new DateBeans();
				dBean.setStartDate(startDate);
				dBean.setEndDate(endDate);
				if ("current Day".equalsIgnoreCase(totaltime)) {
					dBean.setReportType(totaltime);
				} else {
					dBean.setReportType("");
				}
				session.setAttribute("datebean", dBean);
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}

	public String collectionRetailerWiseExpand() throws LMSException {
		logger.debug("------Complete Collection Report------");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			logger.debug("******Start Date Timestamp*****" + startDate);
			logger.debug("******End Date Timestamp*****" + endDate);
			ICompleteCollectionReportHelper helper = null;
			if(LMSFilterDispatcher.isRepFrmSP){
				 helper = new CompleteCollectionReportHelperSP();
			}else{
				 helper = new CompleteCollectionReportHelper();
			}
			
			Map<String, Map<String, Map<String, CompleteCollectionBean>>> reportMap = helper
					.collectionReportForAgentExpand(startDate, endDate,
							reportType, agtId);
			Map<String, String> orgMap = ReportUtility.getOrgMap("RETAILER");
			session.setAttribute("resultService", reportMap);
			session.setAttribute("orgMap", orgMap);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}

	@Override
	public String execute() throws LMSException {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			HttpSession session = request.getSession();

			session.setAttribute("isSE",ReportUtility.isSE);
			session.setAttribute("isDG", ReportUtility.isDG);
			session.setAttribute("isCS", ReportUtility.isCS);
			session.setAttribute("isOLA", ReportUtility.isOLA);
			session.setAttribute("isSLE", ReportUtility.isSLE);
			session.setAttribute("isIW", ReportUtility.isIW);
			session.setAttribute("isVS", ReportUtility.isVS);
		} else
			return "RESULT_TIMING_RESTRICTION";

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String exportExcel() {

		HttpSession session = getRequest().getSession();
		Timestamp startDate = null;
		Timestamp endDate = null;
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");

		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				logger.debug("******Start Date Timestamp*****" + startDate);
				logger.debug("******End Date Timestamp*****" + endDate);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment;filename=Sale_Winning_Collection_Report.xls");
				WritableWorkbook w = Workbook.createWorkbook(response
						.getOutputStream());
				WriteExcelForComplCollReport excel = new WriteExcelForComplCollReport(
						(DateBeans) request.getSession().getAttribute("datebean"));
				/*Map<String, CompleteCollectionBean> reportMap = (Map<String, CompleteCollectionBean>) session
						.getAttribute("result");*/
				Map<String, CompleteCollectionBean> reportMap = (Map<String, CompleteCollectionBean>)  request.getSession().getAttribute("result");
				if (reportMap == null) {
					reportMap = (Map<String, CompleteCollectionBean>) session
							.getAttribute("result_retailer");
				}
				Set<String> agtIdList = reportMap.keySet();
				ICompleteCollectionReportHelper helper = null;
				if (LMSFilterDispatcher.isRepFrmSP) {
					helper = new CompleteCollectionReportHelperSP();
				} else {
					helper = new CompleteCollectionReportHelper();
				}
				Iterator<String> it = agtIdList.iterator();
				if (((String) session.getAttribute("reportType"))
						.equalsIgnoreCase("Agent Wise")) {
					Map<Integer, Map<String, CompleteCollectionBean>> detailMapList = new TreeMap<Integer, Map<String, CompleteCollectionBean>>();
					Map<Integer, Double> drawDirPlyPwtMap = new TreeMap<Integer, Double>();
					Map<Integer, Double> scratchDirPlyPwtMap = new TreeMap<Integer, Double>();
					Map<Integer, Double> sleDirPlyPwtMap = new TreeMap<Integer, Double>();
					Map<Integer, Double> iwDirPlyPwtMap = new TreeMap<Integer, Double>();
					while (it.hasNext()) {
						Integer agtID = Integer.parseInt(it.next());
						detailMapList.put(agtID, helper.collectionReportForAgent(
								startDate, endDate, agtID, "Retailer Wise"));
						Map<String, Double> dirPlrPwtMap = helper
								.agentDirectPlayerPwt(startDate, endDate, agtID, reportStatusBean);
						drawDirPlyPwtMap.put(agtID, dirPlrPwtMap.get("DG"));
						scratchDirPlyPwtMap.put(agtID, dirPlrPwtMap.get("SE"));
						sleDirPlyPwtMap.put(agtID, dirPlrPwtMap.get("SLE"));
						iwDirPlyPwtMap.put(agtID, dirPlrPwtMap.get("IW"));
						
						session.setAttribute("drawDirPlyPwtMap", drawDirPlyPwtMap);
						session.setAttribute("scratchDirPlyPwtMap",
								scratchDirPlyPwtMap);
						session.setAttribute("sleDirPlyPwtMap", sleDirPlyPwtMap);
						session.setAttribute("iwDirPlyPwtMap", iwDirPlyPwtMap);
					}
					session.setAttribute("orgAddMap", helper.getOrgAddMap("AGENT",
							0));
					excel.writeFullDetailDateWise(reportMap, detailMapList, w,
							(String) session.getAttribute("orgName"),
							(String) session.getAttribute("orgAdd"),
							(Map<Integer, String>) session
									.getAttribute("orgAddMap"),
							(Map<Integer, Double>) session
									.getAttribute("drawDirPlyPwtMap"),
							(Map<Integer, Double>) session
									.getAttribute("scratchDirPlyPwtMap"),
							(Map<Integer, Double>) session
									.getAttribute("sleDirPlyPwtMap"),
							(Map<Integer, Double>) session
									.getAttribute("iwDirPlyPwtMap"),
							(Boolean) session.getAttribute("isSE"),
							(Boolean) session.getAttribute("isDG"),
							(Boolean) session.getAttribute("isCS"),
							(Boolean) session.getAttribute("isOLA"),
							(Boolean) session.getAttribute("isSLE"),
							(Boolean) session.getAttribute("isIW"));
				} else if (((String) session.getAttribute("reportType"))
						.equalsIgnoreCase("Day Wise")) {
					Map<String, Map<String, CompleteCollectionBean>> reportMapDayWise = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
					Calendar c1 = Calendar.getInstance();
					DateBeans dbean = (DateBeans) session.getAttribute("datebean");
					c1.setTime(dbean.getStartDate());
					long diffDays = (dbean.getEndDate().getTime() - dbean
							.getStartDate().getTime())
							/ (24 * 60 * 60 * 1000);
					long i = 0L;
	/*				while (i <= diffDays) {
						reportMapDayWise.put((new java.sql.Date(c1.getTime()
								.getTime())).toString(), helper.collectionReport(
								new java.sql.Timestamp(c1.getTime().getTime()),
								new java.sql.Timestamp(c1.getTime().getTime() + 24
										* 60 * 60 * 1000), "Agent Wise"));
						c1.add(Calendar.DAY_OF_MONTH, 1);
						i++;
					}*/
					logger
							.debug("reportMapDayWise: "
									+ reportMapDayWise.toString());
					excel.writeFullDetailDayWise(reportMap, reportMapDayWise, w,
							(String) session.getAttribute("orgName"),
							(String) session.getAttribute("orgAdd"),
							(Boolean) session.getAttribute("isSE"),
							(Boolean) session.getAttribute("isDG"),
							(Boolean) session.getAttribute("isCS"),
							(Boolean) session.getAttribute("isOLA"),
							(Boolean) session.getAttribute("isSLE"),
							(Boolean) session.getAttribute("isIW"));
	
				} else if (((String) session.getAttribute("reportType"))
						.equalsIgnoreCase("Retailer Wise")) {
					reportMap = (Map<String, CompleteCollectionBean>) session
							.getAttribute("result_retailer");
					excel.WriteFullDetailRetailerWise(reportMap, w,
							(String) session.getAttribute("AgtOrgName"),
							(String) session.getAttribute("AgtOrgAdd"),
							(Double) session.getAttribute("drawDirPlyPwt"),
							(Double) session.getAttribute("scratchDirPlyPwt"),
							(Double) session.getAttribute("sleDirPlyPwt"),
							(Boolean) session.getAttribute("isSE"),
							(Boolean) session.getAttribute("isDG"),
							(Boolean) session.getAttribute("isCS"),
							(Boolean) session.getAttribute("isOLA"),
							(Boolean) session.getAttribute("isSLE"),
							(Boolean) session.getAttribute("isIW"));
				}
				/*
				 * if (session.getAttribute("agentcashchq") == null)
				 * excel.write(data, w); else { CashChqReportBean agentBoDetail =
				 * (CashChqReportBean) session
				 * .getAttribute("agentbocashchqdetail"); excel.writeAgent(data,
				 * agentBoDetail, w); }
				 */
			} else {
				return "RESULT_TIMING_RESTRICTION";
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

	public int getAgtId() {
		return agtId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getDateType() {
		return dateType;
	}

	public String getEnd_Date() {
		return end_Date;
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

	public String getStart_date() {
		return start_date;
	}

	public String search() throws LMSException {
		logger.debug("------Complete Collection Report------");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			if (start_date != null && end_Date != null) {
				String actionName = ActionContext.getContext().getName();
				ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

				if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
					startDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(start_date).getTime());
					endDate = new Timestamp((new SimpleDateFormat(dateFormat))
							.parse(end_Date).getTime()
							+ 24 * 60 * 60 * 1000 - 1000);
	
					DateBeans datebeans = new DateBeans();
					datebeans.setStartDate(new java.util.Date(new SimpleDateFormat(
							dateFormat).parse(start_date).getTime()));
					datebeans.setEndDate(new java.util.Date(new SimpleDateFormat(
							dateFormat).parse(end_Date).getTime()));
					if (totaltime.equalsIgnoreCase("current day")) {
						datebeans.setReportType(totaltime);
					} else if (totaltime.equalsIgnoreCase("date wise")) {
						datebeans.setReportType("");
					}
					datebeans.setReportday(new java.util.Date(new SimpleDateFormat(
							dateFormat).parse(start_date).getTime()));
					session.setAttribute("datebean", datebeans);
	
					
					ICompleteCollectionReportHelper helper = null;
	
					if (LMSFilterDispatcher.isRepFrmSP) {
						helper = new CompleteCollectionReportHelperSP();
					} else {
						helper = new CompleteCollectionReportHelper();
					}
	
					Map<String, CompleteCollectionBean> reportMap = helper
							.collectionReport(startDate, endDate, reportType, reportStatusBean);
					
					//remove terminate Agent
					OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
					List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
			        logger.info("Terminate agent List::"+terminateAgentList);
					Set<String> agentListSet=reportMap.keySet();
					agentListSet.removeAll(terminateAgentList);

					session.setAttribute("result", reportMap);
					session.setAttribute("orgName", ((UserInfoBean) session
							.getAttribute("USER_INFO")).getOrgName());
					session.setAttribute("orgAdd", ReportUtility
							.getOrgAdd(((UserInfoBean) session
									.getAttribute("USER_INFO")).getUserOrgId()));
					session.setAttribute("reportType", reportType);
				} else {
					return "RESULT_TIMING_RESTRICTION";
				}
			} else {
				return NONE;
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}

	public String searchExpand() throws LMSException {
		logger.debug("------Complete Collection Report------");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						start_date).getTime());
				endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
						end_Date).getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
	
				logger.debug("******Start Date Timestamp*****" + startDate);
				logger.debug("******End Date Timestamp*****" + endDate);
				ICompleteCollectionReportHelper helper = null;
	
				if (LMSFilterDispatcher.isRepFrmSP) {
					helper = new CompleteCollectionReportHelperSP();
				} else {
					helper = new CompleteCollectionReportHelper();
				}
				Map<String, Map<String, Map<String, CompleteCollectionBean>>> reportMap = helper
						.collectionReportExpand(startDate, endDate, reportType, reportStatusBean);
				Map<String, String> orgMap = ReportUtility.getOrgMap("AGENT");
				OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
				List<String> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdStringTypeList;
		        logger.info("Terminate agent List::"+terminateAgentList);
		        for(Map<String,Map<String,CompleteCollectionBean>> str: reportMap.values())
		        {
		        	for( Map<String,CompleteCollectionBean> str1 : str.values())
		        	{
		        		str1.keySet().removeAll(terminateAgentList);
		        	}
		        }
				
				
				session.setAttribute("resultService", reportMap);
				session.setAttribute("orgMap", orgMap);
				session.setAttribute("orgName", ((UserInfoBean) session
						.getAttribute("USER_INFO")).getOrgName());
				session.setAttribute("orgAdd", ReportUtility
						.getOrgAdd(((UserInfoBean) session
								.getAttribute("USER_INFO")).getUserOrgId()));
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}

	public void setAgtId(int agtId) {
		this.agtId = agtId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public String getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String transcationReport() throws LMSException {
		logger.debug("------Complete Collection Report------");
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp startDate = null;
		Timestamp endDate = null;
		try {
			startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					start_date).getTime());
			endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
					end_Date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);

			logger.debug("******Start Date Timestamp*****" + startDate);
			logger.debug("******End Date Timestamp*****" + endDate);
			CompleteCollectionReportHelper helper = new CompleteCollectionReportHelper();
			Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> reportMap = helper
					.transactionReportForAgent(startDate, endDate, reportType,
							agtId);
			Map<String, String> orgMap = ReportUtility.getOrgMap("RETAILER");
			session
					.setAttribute("resultServiceNetAmt", reportMap
							.get("NetAmt"));
			session
					.setAttribute("resultServiceMrpAmt", reportMap
							.get("MrpAmt"));
			session.setAttribute("orgMap", orgMap);
		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}

		return SUCCESS;
	}
}
