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
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BOOrderProcessHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GameDetailsHelper;

/**
 * This class provides methods for creating order for an agent - both self and
 * agent initiated
 * 
 * @author Skilrock Technologies
 * 
 */
public class BOOrderProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(BOOrderProcessAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agtOrgName;
	private HttpServletRequest request;
	private int orderId;


	
	private HttpServletResponse response;

	/**
	 * This method displays BO initiated order request page for agent
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String displayBOOrderRequest() throws Exception {

		HttpSession session = getRequest().getSession();
		BOOrderProcessHelper helper = new BOOrderProcessHelper();
		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		List<OrgBean> agtOrgList = helper.getAgents(userInfo);

		// set the agent list for BO
		session.setAttribute("AGT_ORG_LIST", agtOrgList);

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
	 * This method generates a self initiated order for an agent
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String generateBOOrder() throws Exception {

		HttpSession session = getRequest().getSession();
		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		List<OrgBean> agtOrgList = (List<OrgBean>) session
				.getAttribute("AGT_ORG_LIST");

		List<GameBean> cartList = (List<GameBean>) session.getAttribute("CART");

		BOOrderProcessHelper orderHelper = new BOOrderProcessHelper();

		// try {

		// generate order

		orderId = orderHelper.generateOrder(cartList, agtOrgList,
				agtOrgName);
		if (orderId > -1) {
			session.setAttribute("CART", null);
		}
		/*
		 * } catch (LMSException e) {
		 * 
		 * //log Exception using logger }
		 */
		System.out.println("orderId_action: "+ orderId);
		return SUCCESS;
	}

	public List<Double> getAgtCreditDetails() throws Exception {
		PrintWriter out = getResponse().getWriter();
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		List<Double> accountList = null;
		try {
			accountList = gameHelper.fetchBOAgentAccDetail(getAgtOrgName());
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
			session.setAttribute("CRLIMIT", currentCrLimit);
			session.setAttribute("CURRBAL", availableLimit);
			// session.setAttribute("CURRBAL",currentBalance);
		}
		// changed by yogesh to display available limit on jsp page(without E
		// type value)
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		String availableCreditTodisplay = nf.format(availableLimit).replace(
				",", "");
		System.out
				.println("available credit as string#######################3  "
						+ availableCreditTodisplay);
		String html = "<tr><td><font color='red'>  Order Cannot be Dispatched !   Available Credit Amount of '"
				+ getAgtOrgName()
				+ "' is Insufficient </font> </td><td><br><font color='red'>Available Credit Amount is :</font><input type='text' readonly='true' name='crBal' id='crBal' value='"
				+ availableCreditTodisplay + "'/></td></tr>";
		System.out.println(html + "99999999999");
		response.setContentType("text/html");

		out.print(html);
		System.out.println("crredit amount" + currentCrLimit
				+ "avalaible credit" + availableLimit);
		return null;

	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		// TODO Auto-generated method stub

	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	
	

}
