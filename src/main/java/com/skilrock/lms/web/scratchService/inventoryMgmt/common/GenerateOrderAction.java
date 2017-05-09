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

package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.GenerateOrderHelper;

/**
 * This class provides methods for generating order for agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class GenerateOrderAction extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(GenerateOrderAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameName[];
	private String noOfBooks[];

	private HttpServletRequest request;

	private String retOrgName;

	/**
	 * This method is used for generating order request to BO
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String generateOrder() throws Exception {

		HttpSession session = getRequest().getSession();
		List cartList = (List) session.getAttribute("CART");

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtId = userInfoBean.getUserId();
		int agtOrgId = userInfoBean.getUserOrgId();

		GenerateOrderHelper orderHelper = new GenerateOrderHelper();
		boolean isOrderCreated = orderHelper.generateOrder(agtId, agtOrgId,
				cartList);
		if (isOrderCreated) {
			session.setAttribute("CART", null);
		}
		return SUCCESS;
	}

	public String generateQuickOrder() throws LMSException {

		if (gameName == null || gameName.length == 0 || noOfBooks == null
				|| noOfBooks.length == 0 || gameName.length != noOfBooks.length) {
			addActionError("Please Enter Valid No. Of Books.");
			logger.debug(" game name is = " + gameName + "   no of books is = "
					+ noOfBooks);
			return INPUT;
		}
		logger.debug(" game name is = " + gameName + " game name length = "
				+ gameName.length + "   no of books is = " + noOfBooks
				+ " no of books length = " + noOfBooks.length);

		HttpSession session = getRequest().getSession();
		GenerateOrderHelper orderHelper = new GenerateOrderHelper();
		List<GameBean> cartList = orderHelper.createCartOfOrder(gameName,
				noOfBooks);
		if (cartList == null) {
			addActionError("Please Enter Valid No. Of Books.");
			logger.debug(" game name is = " + gameName + "   no of books is = "
					+ noOfBooks);
			return INPUT;
		}
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtId = userInfoBean.getUserId();
		int agtOrgId = userInfoBean.getUserOrgId();
		boolean isOrderCreated = orderHelper.generateOrder(agtId, agtOrgId,
				cartList);
		return SUCCESS;
	}

	public String[] getGameName() {
		return gameName;
	}

	public String[] getNoOfBooks() {
		return noOfBooks;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public void setNoOfBooks(String[] noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
