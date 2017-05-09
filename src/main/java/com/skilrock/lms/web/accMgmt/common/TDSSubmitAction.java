package com.skilrock.lms.web.accMgmt.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.accMgmt.common.TDSSubmitHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

/**
 * This class provides methods to submit govt commision,unclaim pwt and to get
 * game details
 * 
 * @author Skilrock Technologies
 * 
 */
public class TDSSubmitAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double amt;
	private int gameId;
	private String month;
	private List<PrizeStatusBean> prizeStatusList;
	private HttpServletRequest request;
	private String serviceCode;
	private String serviceName;

	private int taskId;

	private String transactionType = null;

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

	public String gameDetails() throws Exception {

		gameId = getGameId();
		HttpSession session = getRequest().getSession();
		session.setAttribute("GAME_DETAILS", null);
		session.setAttribute("taskId", getTaskId());
		session.setAttribute("transactionType", getTransactionType());
		session.setAttribute("gameId", getGameId());
		session.setAttribute("amount", getAmt());
		session.setAttribute("month", getMonth());

		System.out.println("task id is  " + taskId);
		System.out.println("type is  " + getTransactionType());
		System.out.println("amount is " + getAmt());

		TDSSubmitHelper gameDetails = new TDSSubmitHelper();
		session.setAttribute("serviceCode", serviceName);
		List gameInfo = gameDetails.getGameDetails(getGameId(), serviceName);

		if (!getTransactionType().equals("GOVT_COMM")) {
			List<PrizeStatusBean> prizeList = gameDetails
					.fetchRemainingPrizeList(gameId);
			if (prizeList != null) {
				setPrizeStatusList(prizeList);

			}
		}

		if (gameInfo != null && gameInfo.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("GAME_DETAILS", gameInfo);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}
		return SUCCESS;
	}

	public String gameDetailsUnclm() throws Exception {

		System.out
				.println("444444444444444444444444444444444444445555555555555555555555");
		gameId = getGameId();
		HttpSession session = getRequest().getSession();
		session.setAttribute("GAME_DETAILS", null);
		session.setAttribute("taskId", getTaskId());
		session.setAttribute("transactionType", getTransactionType());
		session.setAttribute("gameId", getGameId());
		session.setAttribute("amount", getAmt());
		session.setAttribute("month", getMonth());

		System.out.println("id a gayi hai  " + taskId);
		System.out.println("type is  " + getTransactionType());
		System.out.println("amount is " + getAmt());

		TDSSubmitHelper gameDetails = new TDSSubmitHelper();
		List gameInfo = gameDetails.getGameDetails(getGameId(), "DG");

		List<PrizeStatusBean> prizeList = gameDetails
				.fetchRemainingPrizeListUnclm(gameId);
		if (prizeList != null) {
			setPrizeStatusList(prizeList);

		}

		if (gameInfo != null && gameInfo.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("GAME_DETAILS", gameInfo);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public double getAmt() {
		return amt;
	}

	public int getGameId() {
		return gameId;
	}

	public String getMonth() {
		return month;
	}

	public List<PrizeStatusBean> getPrizeStatusList() {
		return prizeStatusList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	// this method is commented to remove code duplicity
	/*
	 * public String submitGovtComm()throws Exception{
	 * 
	 * System.out.println("999999999999999999999999999999999");
	 * //taskId=getTaskId(); HttpSession session = getRequest().getSession();
	 * transactionType= (String) session.getAttribute("transactionType");
	 * taskId= (Integer) session.getAttribute("taskId"); gameId= (Integer)
	 * session.getAttribute("gameId"); amt= (Double)
	 * session.getAttribute("amount");
	 * 
	 * 
	 * 
	 * System.out.println("id a gayi hai " + taskId); System.out.println("type
	 * is " + getTransactionType());
	 * 
	 * TDSSubmitHelper taskGovtCommHelper = new TDSSubmitHelper();
	 * taskGovtCommHelper.submitGovtComm(taskId,transactionType,gameId,amt);
	 * 
	 * return SUCCESS; }
	 */

	public int getTaskId() {
		return taskId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String search() throws Exception {
		System.out.println("Service Name " + serviceName);

		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_TDS_SEARCH_RESULTS", null);

		TDSSubmitHelper taskTDSHelper = new TDSSubmitHelper();
		Class help = TDSSubmitHelper.class;
		Method method = help.getDeclaredMethod("taskTDSSearch" + serviceName);
		List searchResults = (ArrayList) method.invoke(taskTDSHelper);
		// List searchResults =taskTDSHelper.taskTDSSearch();
		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("TASK_TDS_SEARCH_RESULTS", searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	/**
	 * This method is used to get the govt commission details
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String searchGovtCommission() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS", null);

		TDSSubmitHelper taskTDSHelper = new TDSSubmitHelper();
		List searchResults = taskTDSHelper.taskGovtCommSearch();

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session
					.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS",
							searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	/**
	 * This method is used to get the Unclaimed Pwt details
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String searchUnclmPwt() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_Unclm_Pwt_SEARCH_RESULTS", null);

		TDSSubmitHelper taskUnclmPwtHelper = new TDSSubmitHelper();
		List searchResults = taskUnclmPwtHelper.taskUnclmPwtSearch();

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session
					.setAttribute("TASK_Unclm_Pwt_SEARCH_RESULTS",
							searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public String searchVat() throws Exception {

		System.out.println("serviceName " + serviceName);
		HttpSession session = getRequest().getSession();
		session.setAttribute("TASK_VAT_SEARCH_RESULTS", null);

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		TDSSubmitHelper taskVATHelper = new TDSSubmitHelper();
		Class help = TDSSubmitHelper.class;
		Object[] param = { userBean.getUserType(), userBean.getUserOrgId() };
		Method method = help.getDeclaredMethod("taskVATSearch" + serviceName,
				String.class, Integer.class);
		List searchResults = (ArrayList) method.invoke(taskVATHelper, param);

		// List searchResults
		// =taskVATHelper.taskVATSearch(userBean.getUserType(),userBean.getUserOrgId());

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("TASK_VAT_SEARCH_RESULTS", searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setPrizeStatusList(List<PrizeStatusBean> prizeStatusList) {
		this.prizeStatusList = prizeStatusList;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String submitTds() throws Exception {
		HttpSession session = getRequest().getSession();
		System.out.println("service Code is " + serviceCode);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		// transactionType= (String) session.getAttribute("transactionType");
		transactionType = getTransactionType();
		System.out.println(transactionType);
		// taskId= (Integer) session.getAttribute("taskId");
		// amount= (Integer) session.getAttribute("amount");
		// month= (String) session.getAttribute("month");
		taskId = getTaskId();
		amt = getAmt();
		// TaskBean task=new TaskBean();
		// month=task.getMonth();
		// System.out.println("month from bean is " + month);

		month = getMonth();
		System.out.println("month get from jsp is " + month);
		session.setAttribute("month", getMonth());
		System.out.println("task id is " + taskId);
		TDSSubmitHelper taskTdsHelper = new TDSSubmitHelper();
		Class help = TDSSubmitHelper.class;
		Object[] param = { taskId, transactionType, month, amt,
				userBean.getUserOrgId(), userBean.getUserId(), rootPath,
				userBean.getOrgName() };
		Method method = help.getDeclaredMethod("submitTds" + serviceCode,
				Integer.class, String.class, String.class, Double.class,
				Integer.class, Integer.class, String.class, String.class);
		method.invoke(taskTdsHelper, param);
		// taskTdsHelper.submitTdsSE(taskId,transactionType,month,amt,userBean.getUserOrgId(),userBean.getUserId(),rootPath,userBean.getOrgName());
		return SUCCESS;
	}

	public String submitUnclmPwt() throws Exception {

		HttpSession session = getRequest().getSession();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		transactionType = (String) session.getAttribute("transactionType");
		taskId = (Integer) session.getAttribute("taskId");
		gameId = (Integer) session.getAttribute("gameId");
		amt = (Double) session.getAttribute("amount");
		TDSSubmitHelper taskUnclmPwtHelper = new TDSSubmitHelper();
		taskUnclmPwtHelper.submitUnclmPwt(taskId, transactionType, gameId, amt,
				userBean.getUserOrgId(), userBean.getUserId(), rootPath,
				userBean.getOrgName(),serviceCode);

		return SUCCESS;
	}

	public String submitVatAgt() throws Exception {

		System.out.println("Service Code " + serviceCode);
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		// transactionType= (String) session.getAttribute("transactionType");
		transactionType = getTransactionType();
		System.out.println(transactionType);
		// taskId= (Integer) session.getAttribute("taskId");
		// amount= (Integer) session.getAttribute("amount");
		// month= (String) session.getAttribute("month");
		taskId = getTaskId();
		amt = getAmt();
		// TaskBean task=new TaskBean();
		// month=task.getMonth();
		// System.out.println("month from bean is " + month);

		month = getMonth();
		System.out.println("month get from jsp is " + month);
		session.setAttribute("month", getMonth());
		System.out.println("task id is " + taskId);

		TDSSubmitHelper taskTdsHelper = new TDSSubmitHelper();
		Class help = TDSSubmitHelper.class;
		Object[] param = { taskId, transactionType, month, amt,
				userBean.getUserId(), userBean.getUserOrgId(), rootPath,
				userBean.getOrgName() };
		Method method = help.getDeclaredMethod("submitVatAgt" + serviceCode
				+ "", Integer.class, String.class, String.class, Double.class,
				Integer.class, Integer.class, String.class, String.class);
		method.invoke(taskTdsHelper, param);

		// taskTdsHelper.submitVatAgtSE(taskId,transactionType,month,amt,userBean.getUserId(),userBean.getUserOrgId(),rootPath,userBean.getOrgName());

		return SUCCESS;
	}
}
