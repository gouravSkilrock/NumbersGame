/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 */

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

import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GenerateRetailerOrderHelper;

/**
 * This class provides methods for generating order for agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class GenerateRetailerOrderAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware  {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(GenerateRetailerOrderAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameName[];
	private String noOfBooks[];

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String retOrgName;

	public void generateQuickOrder() throws LMSException {
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
		/*	if (gameName == null || gameName.length == 0 || noOfBooks == null
					|| noOfBooks.length == 0 || gameName.length != noOfBooks.length) {
				addActionError("Please Enter Valid No. Of Books.");
				logger.debug(" game name is = " + gameName + "   no of books is = "
						+ noOfBooks);
				return INPUT;
			}*/
			logger.debug(" game name is = " + gameName + " game name length = "
					+ gameName.length + "   no of books is = " + noOfBooks
					+ " no of books length = " + noOfBooks.length);

			HttpSession session = getRequest().getSession();
			GenerateRetailerOrderHelper orderHelper = new GenerateRetailerOrderHelper();
			List<GameBean> cartList = orderHelper.createCartOfOrder(gameName[0].split(","),
					noOfBooks[0].split(","));
			if (cartList.size()<=0) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			// int agtId = userInfoBean.getUserId();
			int agtOrgId = userInfoBean.getParentOrgId();

			int retId = userInfoBean.getUserId();
			int retOrgId = userInfoBean.getUserOrgId();
			int isOrderCreated = orderHelper.generateOrder(agtOrgId, retId, retOrgId, cartList);
			if(isOrderCreated!=-1){
				res.addProperty("responseCode", 0);
				res.addProperty("responseMsg","Quick Order has been requested Successfully");
			}else{
				res.addProperty("responseCode",LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				res.addProperty("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}

		} catch (LMSException e) {
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
	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}
}
