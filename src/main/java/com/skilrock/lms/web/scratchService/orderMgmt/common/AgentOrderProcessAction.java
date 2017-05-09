/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.AgentOrderProcessHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GameDetailsHelper;

/**
 * This class provides method for creating agent initiated orders - self and for
 * retailer
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentOrderProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(AgentOrderProcessAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String roundTo2DecimalPlaces(double value) {

		DecimalFormat df = new DecimalFormat("0.000");
		String doublevalue = df.format(value);

		System.out.println("------kfkdjd" + doublevalue + "--------");
		return doublevalue;
	}

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String retOrgName;
	
	private int orderId;
	/**
	 * This method is used for displaying the self initiated order request page
	 * for the retailer
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String displayAgentOrderRequest() throws Exception {

		HttpSession session = getRequest().getSession();

		// get currently logged in user info from the session

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtOrgId = userInfoBean.getUserOrgId();

		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();

		// get retailers for the logged agent
		List<OrgBean> retOrgList = helper.getRetailers(agtOrgId);
		session.setAttribute("RET_ORG_LIST", retOrgList);
		// session.setAttribute("TOTAL",null);
		// session.setAttribute("BALANCE",null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);

		Date currDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String strCurrDate = dateFormat.format(currDate);

		// set the current date
		session.setAttribute("END_DATE_START", strCurrDate);

		// set the order cart to null
		session.setAttribute("CART", null);
		return SUCCESS;
	}

	/**
	 * This method is used for displaying the order request page for agent
	 * 
	 * @return String
	 */
	public String displayOrderRequest() {

		HttpSession session = getRequest().getSession();

		Date currDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// session.setAttribute("TOTAL",null);
		// session.setAttribute("BALANCE",null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		String strCurrDate = dateFormat.format(currDate);

		// set the current date
		session.setAttribute("END_DATE_START", strCurrDate);

		// set the order cart to null
		session.setAttribute("CART", null);
		return SUCCESS;
	}

	/**
	 * This method generates the self initiated order for the retailer
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String generateAgentOrder() throws Exception {

		HttpSession session = getRequest().getSession();
		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		List<OrgBean> retOrgList = (List<OrgBean>) session
				.getAttribute("RET_ORG_LIST");

		List<GameBean> cartList = (List<GameBean>) session.getAttribute("CART");

		// get currently logged in user info from the session
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userId = userInfoBean.getUserId();
		int userOrgId = userInfoBean.getUserOrgId();

		AgentOrderProcessHelper orderHelper = new AgentOrderProcessHelper();
		// try {
		orderId = orderHelper.generateOrder(userId, cartList,
				retOrgList, retOrgName, userOrgId);

		if (orderId > -1) {
			session.setAttribute("CART", null);
		}
		
		System.out.println("oder_id_action_2" + orderId);
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public List<Double> getRetCreditDetails() throws Exception {
		System.out.println("hellllloooooooo");
		PrintWriter out = getResponse().getWriter();
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		List<Double> accountList = null;
		try {
			accountList = gameHelper
					.fetchAgentRetailerAccDetail(getRetOrgName());
		} catch (LMSException e) {
			System.out.println("In boOrderProcessAction get Credit details");
			e.printStackTrace();
		}
		HttpSession session = getRequest().getSession();
		double creditrLimit = 0.0;
		double currentCrLimit = 0.0;
		double availableLimit = 0.0;
		if (accountList != null) {
			System.out.println("acountlist not null");
			creditrLimit = accountList.get(0);
			currentCrLimit = accountList.get(1);
			availableLimit = accountList.get(2);
			session.setAttribute("RETCRLIMIT",
					roundTo2DecimalPlaces(currentCrLimit));
			session.setAttribute("RETCURRBAL",
					roundTo2DecimalPlaces(availableLimit));
			// session.setAttribute("RETAVAILBAL",availableBalance);
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		String availableLimitToDisplay = nf.format(availableLimit).replace(",",
				"");
		String html = "<tr><td><font color='red'>  Order Cannot be Dispatched !   Available Credit Amount of '"
				+ getRetOrgName()
				+ "' is Insufficient </font> </td><td><br><font color='red'>Available Credit Amount is :</font><input type='text' readonly='true' name='crBal' id='crBal' value='"
				+ availableLimitToDisplay + "'/></td></tr>";
		System.out.println(html + "99999999999");
		response.setContentType("text/html");

		out.print(html);
		System.out.println("crredit amount" + currentCrLimit
				+ "avalaible credit" + availableLimit);
		return null;

	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	

}
