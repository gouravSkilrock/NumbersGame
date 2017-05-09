/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 * 
 */
package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.skilrock.lms.beans.ScratchBookOrderDataBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.RequestDetailHelper;

/**
 * 
 * This class used to get the details of the request.
 * 
 * @author Skilrock Technologies
 * 
 */
public class RequestDetailAction extends ActionSupport implements
		ServletRequestAware ,ServletResponseAware{
	static Log logger = LogFactory.getLog(RequestDetailAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<OrderRequestBean> list;
	private String name;
	OrderRequestBean orderBean;
	private int orderId;
	private String ordersearchResultsAvailable;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String searchResultsAvailableForPWTRemain;
	private ScratchBookOrderDataBean scratchBookOrderDataBean;

	/**
	 * 
	 * This method is used to get the details of the request by the BO Admin
	 * 
	 * @return SUCCESS or ERROR throws LMSException
	 */

	@Override
	public String execute() throws Exception {

		List<OrderRequestBean> list = new ArrayList<OrderRequestBean>();
		HttpSession session = getRequest().getSession();
		session.setAttribute("SEARCH_RESULTS", null);
		session.setAttribute("SEARCH_AGENT", null);
		session.setAttribute("OrgId", null);
		RequestDetailHelper requestDetailHelper = new RequestDetailHelper();

		// Order Id is coming from user end.
		session.setAttribute("OrgId", orderId);
		List searchResults = requestDetailHelper.searchResult(orderId);
		// Agent who have created the Order.
		List searchAgent = requestDetailHelper.searchAgent(orderId, name);

		if (searchAgent != null && searchAgent.size() > 0) {

			session.setAttribute("SEARCH_AGENT", searchAgent);
			// flag variable which is used for display the search result.
			setOrdersearchResultsAvailable("Yes");
		} else {
			setOrdersearchResultsAvailable("No");
		}

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("SEARCH_RESULTS", searchResults);

			setOrdersearchResultsAvailable("Yes");
		} else {
			setOrdersearchResultsAvailable("No");
		}

		return SUCCESS;
	}

	public String executeAgent() throws Exception {
		System.out
				.println("55555555555555555555555555555555555555555555555555555555555555555555");
		// List<OrderRequestBean> list=new ArrayList<OrderRequestBean>();
		HttpSession session = getRequest().getSession();
		session.setAttribute("SEARCH_RESULTS", null);
		session.setAttribute("SEARCH_AGENT", null);
		session.setAttribute("OrgId", null);
		RequestDetailHelper requestDetailHelper = new RequestDetailHelper();

		// Order Id is coming from user end.
		session.setAttribute("OrgId", orderId);

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		List searchResults = requestDetailHelper.searchResultRetailer(orderId,
				userBean.getUserOrgId());
		// Retailer who have created the Order.
		List searchAgent = requestDetailHelper.searchRetailer(orderId, name);

		if (searchAgent != null && searchAgent.size() > 0) {

			session.setAttribute("SEARCH_AGENT", searchAgent);
			// flag variable which is used for display the search result.
			setOrdersearchResultsAvailable("Yes");
		} else {
			setOrdersearchResultsAvailable("No");
		}

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("SEARCH_RESULTS", searchResults);

			setOrdersearchResultsAvailable("Yes");
		} else {
			setOrdersearchResultsAvailable("No");
		}

		return SUCCESS;
	}

	public List<OrderRequestBean> getList() {
		return list;
	}

	public String getName() {
		return name;
	}

	public OrderRequestBean getOrderBean() {
		return orderBean;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getOrdersearchResultsAvailable() {
		return ordersearchResultsAvailable;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSearchResultsAvailableForPWTRemain() {
		return searchResultsAvailableForPWTRemain;
	}

	public void setList(List<OrderRequestBean> list) {
		this.list = list;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderBean(OrderRequestBean orderBean) {
		this.orderBean = orderBean;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOrdersearchResultsAvailable(
			String ordersearchResultsAvailable) {
		this.ordersearchResultsAvailable = ordersearchResultsAvailable;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSearchResultsAvailableForPWTRemain(
			String searchResultsAvailableForPWTRemain) {
		this.searchResultsAvailableForPWTRemain = searchResultsAvailableForPWTRemain;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public ScratchBookOrderDataBean getScratchBookOrderDataBean() {
		return scratchBookOrderDataBean;
	}

	public void setScratchBookOrderDataBean(ScratchBookOrderDataBean scratchBookOrderDataBean) {
		this.scratchBookOrderDataBean = scratchBookOrderDataBean;
	}

	public void fetchOrderDetail() throws Exception {
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			RequestDetailHelper requestDetailHelper = new RequestDetailHelper();
	
			scratchBookOrderDataBean = requestDetailHelper.fetchOrderDetail(orderId);
			if(scratchBookOrderDataBean!=null){
				res.addProperty("responseCode", 0);
				res.addProperty("responseData",new Gson().toJson(scratchBookOrderDataBean));
			}else{
				res.addProperty("responseCode", 1);
				res.addProperty("responseMsg","Order detail not found");
			}
			
		}catch (ScratchException e) {
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

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}

}
