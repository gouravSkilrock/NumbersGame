package com.skilrock.lms.web.accMgmt.common;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.accMgmt.common.TDSHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

public class ProcessTDSAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;

	public String serviceName;

	@Override
	public String execute() {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
		Map<String, String> serviceNameMap = helper.getServiceList();
		session.setAttribute("serviceNameMap", serviceNameMap);
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String search() throws Exception {
		System.out.println("Service Name " + serviceName);
		/*
		 * Properties properties = new Properties(); InputStream inputStream =
		 * this.getClass().getClassLoader().getResourceAsStream("config/LMS.properties");
		 * System.out.println(">>>>" + inputStream);
		 * properties.load(inputStream);
		 */

		String depDate = (String) ServletActionContext.getServletContext()
				.getAttribute("DEPLOYMENT_DATE");
		String depDateFormat = (String) ServletActionContext
				.getServletContext().getAttribute("date_format");
		SimpleDateFormat dateFormat = new SimpleDateFormat(depDateFormat);
		java.util.Date depdateUtil = dateFormat.parse(depDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(depdateUtil);

		// String depDate = properties.getProperty("def_start_search_date");
		System.out.println("Date is   " + depDate);

		/*
		 * int depMonth= Integer.parseInt(depDate.substring(5,7)); int
		 * depYear=Integer.parseInt(depDate.substring(0,4));
		 */

		int depMonth = cal.get(Calendar.MONTH) + 1;
		int depYear = cal.get(Calendar.YEAR);

		System.out.println("deploy month iss " + depMonth);
		System.out.println("deploy year iss" + depYear);

		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_SEARCH_RESULTS", null);

		TDSHelper tdsHelper = new TDSHelper();
		Class help = TDSHelper.class;
		Object[] param = { depMonth, depYear };
		Method method = help.getDeclaredMethod("tdsSearch" + serviceName,
				Integer.class, Integer.class);
		List searchResults = (ArrayList) method.invoke(tdsHelper, param);

		// List searchResults =tdsHelper.tdsSearch(depMonth,depYear);

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("TASK_SEARCH_RESULTS", searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public String searchForVat() throws Exception {
		System.out.println("serviceName " + serviceName);
		/*
		 * Properties properties = new Properties(); InputStream inputStream =
		 * this.getClass().getClassLoader().getResourceAsStream("config/LMS.properties");
		 * System.out.println(">>>>" + inputStream);
		 * properties.load(inputStream);
		 */
		HttpSession session = request.getSession();

		String depDate = (String) ServletActionContext.getServletContext()
				.getAttribute("DEPLOYMENT_DATE");
		String depDateFormat = (String) ServletActionContext
				.getServletContext().getAttribute("date_format");
		// String depDate = properties.getProperty("def_start_search_date");
		SimpleDateFormat dateFormat = new SimpleDateFormat(depDateFormat);
		java.util.Date depdateUtil = dateFormat.parse(depDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(depdateUtil);

		System.out.println("Date is  dd-mm-yy " + depDate);

		/*
		 * int depMonth= Integer.parseInt(depDate.substring(5,7)); int
		 * depYear=Integer.parseInt(depDate.substring(0,4));
		 */

		int depMonth = cal.get(Calendar.MONTH) + 1;
		int depYear = cal.get(Calendar.YEAR);

		System.out.println("deploy month iss " + depMonth);
		System.out.println("deploy year iss" + depYear);

		session.setAttribute("TASK_SEARCH_RESULTS", null);

		TDSHelper tdsHelper = new TDSHelper();
		Class help = TDSHelper.class;
		Object[] param = { depMonth, depYear };
		Method method = help.getDeclaredMethod("vatSearch" + serviceName,
				Integer.class, Integer.class);
		List searchResults = (ArrayList) method.invoke(tdsHelper, param);
		// List searchResults =tdsHelper.vatSearchSE(depMonth,depYear);

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("TASK_SEARCH_RESULTS", searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
