package com.skilrock.lms.web.accMgmt.common;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.GetAgentRegDate;
import com.skilrock.lms.coreEngine.accMgmt.common.VatAgentHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

public class ProcessVatAgentAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String serviceName;

	@Override
	public String execute() {
		System.out.println("hello.....");
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

	/*
	 * public String search()throws Exception{ String
	 * depDate=(String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE");
	 * 
	 * //String depDate = properties.getProperty("def_start_search_date");
	 * System.out.println("Date is " + depDate); int depMonth=
	 * Integer.parseInt(depDate.substring(5,7)); int
	 * depYear=Integer.parseInt(depDate.substring(0,4));
	 * System.out.println("deploy month iss "+ depMonth);
	 * System.out.println("deploy year iss" + depYear);
	 * 
	 * HttpSession session = getRequest().getSession();
	 * session.setAttribute("TASK_SEARCH_RESULTS", null);
	 * 
	 * TDSHelper tdsHelper = new TDSHelper(); List searchResults
	 * =tdsHelper.tdsSearch(depMonth,depYear);
	 * 
	 * 
	 * if(searchResults!= null && searchResults.size() > 0){
	 * System.out.println("Yes:---Search result Processed");
	 * session.setAttribute("TASK_SEARCH_RESULTS", searchResults);
	 * //setUsersearchResultsAvailable("Yes"); }else {
	 * //setUsersearchResultsAvailable("No"); System.out.println("No:---Search
	 * result Processed"); }
	 * 
	 * return SUCCESS; }
	 */

	public String searchForVat() throws Exception {
		/*
		 * Properties properties = new Properties(); InputStream inputStream =
		 * this.getClass().getClassLoader().getResourceAsStream("config/LMS.properties");
		 * System.out.println(">>>>" + inputStream);
		 * properties.load(inputStream);
		 */

		// String
		// depDate=(String)ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE");
		// String depDate = properties.getProperty("def_start_search_date");
		/*
		 * System.out.println("Date is " + depDate); int depMonth=
		 * Integer.parseInt(depDate.substring(5,7)); int
		 * depYear=Integer.parseInt(depDate.substring(0,4));
		 * System.out.println("deploy month iss "+ depMonth);
		 * System.out.println("deploy year iss" + depYear);
		 */
		System.out.println("service Name " + serviceName);
		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_SEARCH_RESULTS", null);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userOrgId = userBean.getUserOrgId();
		// added by yogesh to get agent registration date as refrence date

		Timestamp agtRegDate = GetAgentRegDate.getAgtRegDate(userBean
				.getUserOrgId());
		long timeMillis = agtRegDate.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMillis);
		int depMonth = cal.get(Calendar.MONTH) + 1;
		int depYear = cal.get(Calendar.YEAR);
		System.out.println("deploy month iss***************** " + depMonth);
		System.out.println("deploy year iss?????????????????? " + depYear);

		VatAgentHelper vatHelper = new VatAgentHelper();
		Class help = VatAgentHelper.class;
		Object[] param = { depMonth, depYear, userBean.getUserOrgId(),
				userBean.getUserId() };
		Method method = help.getDeclaredMethod("vatSearch" + serviceName + "",
				Integer.class, Integer.class, Integer.class, Integer.class);
		List searchResults = (List) method.invoke(vatHelper, param);

		// List searchResults
		// =vatHelper.vatSearchSE(depMonth,depYear,userBean.getUserOrgId(),userBean.getUserId());

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
