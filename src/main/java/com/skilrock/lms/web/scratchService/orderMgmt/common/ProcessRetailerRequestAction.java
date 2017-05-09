package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.ProcessRetailerRequestHelper;

/**
 * 
 * This class used to process UnApproved Requests by the BO Admin
 * 
 * @author Skilrock Technologies
 * 
 */
public class ProcessRetailerRequestAction extends ActionSupport implements
		ServletRequestAware,ServletResponseAware {
	static Log logger = LogFactory.getLog(ProcessRetailerRequestAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<OrderRequestBean> list;

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	public List<OrderRequestBean> getList() {
		return list;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 
	 * This method is used to process Unapproved requested by the agent.
	 * 
	 * @author SkilRock Technologies
	 * @return String throws LMSException
	 */

	private String gameName;
	private String gameNumber;
	private String agtOrgName;
	private String orderNumber;
	private String orderStatus;
	private String startDate;
	private String endDate;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRequestedOrders() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("RequestList", null);
		session.setAttribute("orderId", null);
		session.setAttribute("TOTAL", null);
		session.setAttribute("BALANCE", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		ProcessRetailerRequestHelper processRequestHelper = new ProcessRetailerRequestHelper();
		list = processRequestHelper.getRequestedOrders(userBean.getUserOrgId(), gameName, gameNumber, agtOrgName, orderNumber);
		session.setAttribute("RequestList", list);
		return SUCCESS;
	}
	
/*	public String getRetailerRequestedOrders() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("RequestList", null);
		session.setAttribute("orderId", null);
		session.setAttribute("TOTAL", null);
		session.setAttribute("BALANCE", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		ProcessRetailerRequestHelper processRequestHelper = new ProcessRetailerRequestHelper();
		list = processRequestHelper.getRetailerRequestedOrders(userBean.getUserOrgId(), gameName, gameNumber, orderNumber, orderStatus, startDate, endDate);
		session.setAttribute("RequestList", list);
		return SUCCESS;
	}*/
	
	
	public void getRetailerRequestedOrders() throws Exception {
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			HttpSession session = getRequest().getSession();
			session.setAttribute("orderId", null);
			session.setAttribute("TOTAL", null);
			session.setAttribute("BALANCE", null);
			session.setAttribute("CREDIT_LIMIT", null);
			session.setAttribute("CREDIT_AMT", null);
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
	
			ProcessRetailerRequestHelper processRequestHelper = new ProcessRetailerRequestHelper();
			list = processRequestHelper.getRetailerRequestedOrders(userBean.getUserOrgId(), gameName, gameNumber, orderNumber, orderStatus, startDate, endDate);
			res.addProperty("responseCode", 0);
			res.addProperty("responseData",new Gson().toJson(list));
		}catch (LMSException e) {
			res.addProperty("responseCode", e.getErrorCode());
			res.addProperty("responseMsg", e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			res.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			res.addProperty("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(res);
		out.flush();
		out.close();
		
	}
	
	public void setList(List<OrderRequestBean> list) {
		this.list = list;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}

}
