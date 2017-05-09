package com.skilrock.lms.web.accMgmt.common;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.ApproveTaskHelper;

/**
 * This class provide methods to approve tasks by the admin
 * 
 * @author Skilrock Technologies
 * 
 */
public class AproveTaskAction1 extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String month;
	private String monthArr[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private HttpServletRequest request;
	private String serviceCode;

	private HttpSession session;

	private int taskId;

	public String detailTaskVatAgt() throws ParseException, Exception {
		System.out.println("Service Code " + serviceCode);
		System.out.println("detailTaskVatAgt   month = " + month);
		session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		Date startDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(
				month).getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(month));
		session.setAttribute("vatDetailMonth",
				monthArr[cal.get(Calendar.MONTH)]);
		session.setAttribute("vatDetailYear", cal.get(Calendar.YEAR));
		System.out.println("monthArr[cal.get(Calendar.MONTH)] = "
				+ monthArr[cal.get(Calendar.MONTH)]);
		cal.add(Calendar.MONTH, 1);
		Date endDate = new Date(cal.getTime().getTime());

		ApproveTaskHelper approveTaskHelper = new ApproveTaskHelper();
		Class help = ApproveTaskHelper.class;
		Object[] param = { startDate, endDate, infoBean };
		Method method = help.getDeclaredMethod("getVatDetails" + serviceCode
				+ "", java.sql.Date.class, java.sql.Date.class,
				UserInfoBean.class);
		Map vatDetailMap = (Map) method.invoke(approveTaskHelper, param);

		// Map vatDetailMap = new ApproveTaskHelper().getVatDetails(startDate,
		// endDate,infoBean);
		session.setAttribute("vatDetailMap", vatDetailMap);
		return SUCCESS;
	}

	public String detailTaskVatBo() throws Exception {
		System.out.println("detailTaskVatAgt   month = " + month);
		System.out.println("Service Code " + serviceCode);
		session = request.getSession();

		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		Date startDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(
				month).getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(month));
		session.setAttribute("vatDetailMonth",
				monthArr[cal.get(Calendar.MONTH)]);
		session.setAttribute("vatDetailYear", cal.get(Calendar.YEAR));
		System.out.println("monthArr[cal.get(Calendar.MONTH)] = "
				+ monthArr[cal.get(Calendar.MONTH)]);
		cal.add(Calendar.MONTH, 1);
		Date endDate = new Date(cal.getTime().getTime());

		ApproveTaskHelper approveTaskHelper = new ApproveTaskHelper();
		Class help = ApproveTaskHelper.class;
		Object[] param = { startDate, endDate, infoBean };
		Method method = help.getDeclaredMethod("getVatDetailsForBo"
				+ serviceCode + "", java.sql.Date.class, java.sql.Date.class,
				UserInfoBean.class);
		Map vatDetailMap = (Map) method.invoke(approveTaskHelper, param);

		// Map vatDetailMap = new
		// ApproveTaskHelper().getVatDetailsForBoSE(startDate,
		// endDate,infoBean);
		session.setAttribute("vatDetailMap", vatDetailMap);

		return SUCCESS;
	}

	public String getMonth() {
		return month;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public int getTaskId() {
		return taskId;
	}

	/**
	 * This method is used to approve the task selected from the task table
	 * 
	 * @return String
	 */
	public String search() {
		System.out.println("Service Code " + serviceCode);
		System.out.println("taskId = " + taskId + "     month = " + month);
		session = getRequest().getSession();
		session.setAttribute("TASK_SEARCH_RESULTS", null);
		session.setAttribute("MONTH", month);

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ApproveTaskHelper tdsTask = new ApproveTaskHelper();
		try {
			tdsTask.tdsTask(taskId, userBean.getUserType());
		} catch (LMSException le) {
			return APPLICATION_ERROR;
		}

		return SUCCESS;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

}
