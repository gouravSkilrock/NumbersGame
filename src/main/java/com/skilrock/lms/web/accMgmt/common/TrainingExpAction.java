package com.skilrock.lms.web.accMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.DateFormatConverter;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class TrainingExpAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(TrainingExpAction.class);

	private int[] taskId;

	private String date;

	private String trngExpType;
//    private  Map<Integer, String> gameMap;
    private int gameNo;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String serviceBeanData;
	private int serviceId;
	
	public String getServiceBeanData() {
		return serviceBeanData;
	}

	public void setServiceBeanData(String serviceBeanData) {
		this.serviceBeanData = serviceBeanData;
	}
	
	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String appTrainingExpMenu(){
		try {
			serviceBeanData = new Gson().toJson(ReportUtility.fetchGameServiceWise());
//			setGameMap(ReportUtility.fetchDrawDataMenu());
		} catch (LMSException e) {
			
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String fetchMenuData() throws LMSException, Exception {
		logger.debug("hi fetchMenuData");
		if (!"-1".equalsIgnoreCase(trngExpType)) {
			TrainingExpAgentHelper helper = new TrainingExpAgentHelper();
			HttpSession session = request.getSession();
			String[] tempTimeStamp = fetchDate(date, trngExpType).split("Nxt");
			DateBeans dateBeans = new DateBeans();
			dateBeans.setReportType(trngExpType);
			dateBeans.setStrDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[0]));
			dateBeans.setEndDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[1]));
			session.setAttribute("TrExpenseData", helper.fetchMenuData(trngExpType, tempTimeStamp[0], tempTimeStamp[1], serviceId, gameNo));
			session.setAttribute("DATE_BEANS", dateBeans);
		}
		return SUCCESS;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void updateAgentData() throws LMSException, IOException {
		PrintWriter out = getResponse().getWriter();
		TrainingExpAgentHelper helper = new TrainingExpAgentHelper();
		HttpSession session = request.getSession();
		String[] tempTimeStamp = fetchDate(date, trngExpType).split("Nxt");
		String result = helper.updateAgentData(taskId, (UserInfoBean) session.getAttribute("USER_INFO"), trngExpType, tempTimeStamp[0], tempTimeStamp[1], serviceId, gameNo);
		out.print(result);
	}

	public String updateAllAgent() throws LMSException {
		TrainingExpAgentHelper helper = new TrainingExpAgentHelper();
		HttpSession session = request.getSession();
		String[] tempTimeStamp = fetchDate(date, trngExpType).split("Nxt");
		helper.updateAllAgentData(tempTimeStamp[0], tempTimeStamp[1], trngExpType, (UserInfoBean) session.getAttribute("USER_INFO"), serviceId, gameNo);
		session.setAttribute("TrExpenseData", helper.fetchMenuData(trngExpType, tempTimeStamp[0], tempTimeStamp[1], serviceId, gameNo));
		return SUCCESS;
	}

	private String fetchDate(String tDate, String type) {
		Timestamp startDate = null;
		Timestamp endDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// code to get week for given date... goes here
		try {
			if ("WEEKLY".equalsIgnoreCase(type)) {
				cal.setTimeInMillis(sdf.parse(tDate).getTime());
				if(cal.get(Calendar.DAY_OF_WEEK) != 1)
					cal.add(Calendar.DAY_OF_WEEK,
							-(cal.get(Calendar.DAY_OF_WEEK) - 2));
				else
					cal.add(Calendar.DAY_OF_WEEK,-6);
				startDate = new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime());
				cal.add(Calendar.DAY_OF_MONTH, +6);
				endDate = new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				return startDate + "Nxt" + endDate;
			} else {
				startDate = new Timestamp(sdf.parse(tDate).getTime());
				endDate = new Timestamp(sdf.parse(tDate).getTime() + 24 * 60
						* 60 * 1000 - 1000);
				return startDate + "Nxt" + endDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int[] getTaskId() {
		return taskId;
	}

	public void setTaskId(int[] taskId) {
		this.taskId = taskId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTrngExpType() {
		return trngExpType;
	}

	public void setTrngExpType(String trngExpType) {
		this.trngExpType = trngExpType;
	}



	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public int getGameNo() {
		return gameNo;
	}

//	public Map<Integer, String> getGameMap() {
//		return gameMap;
//	}
//
//	public void setGameMap(Map<Integer, String> gameMap) {
//		this.gameMap = gameMap;
//	}
	


}
