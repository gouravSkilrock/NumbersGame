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
package com.skilrock.lms.web.accMgmt.common;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;

import com.skilrock.lms.coreEngine.accMgmt.common.RetailerPaymentSubmitHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

/**
 * This class is used to submit the total payment from Agent.
 * 
 * @author Skilrock Technologies
 * 
 */
public class RetailerPaymentSubmit extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 1271130427666936592L;
	static Log logger=LogFactory.getLog(RetailerPaymentSubmit.class);
	
	private int orgId;
	private String orgType = null;
	private String paymentType;
	private HttpServletRequest request = null;
	private double tootalCashAmount;
	private double totalAmount;
	private double cashAmnt;
	

	private double totalPay;
	private long transaction_id;
	private long[] transaction_id2;
	private String userName = null;
	private Map<Integer,String> retailerInfoMap;

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public Map<Integer, String> getRetailerInfoMap() {
		return retailerInfoMap;
	}

	public void setRetailerInfoMap(Map<Integer, String> retailerInfoMap) {
		this.retailerInfoMap = retailerInfoMap;
	}

	public long[] getTransaction_id2() {
		return transaction_id2;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTransaction_id(long transactionId) {
		transaction_id = transactionId;
	}


	
 
	public String getOrgType() {
		return orgType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public double getTootalCashAmount() {
		return tootalCashAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public String getUserName() {
		return userName;
	}
	public double getCashAmnt() {
		return cashAmnt;
	}

	public void setCashAmnt(double cashAmnt) {
		this.cashAmnt = cashAmnt;
	}

	/**
	 * This method is used to process cash from Agent
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String start() {
		try {
			HttpSession session = null;

			session = getRequest().getSession();
			UserInfoBean userInfo = null;
			userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
			logger.info("REQUEST_RETAILER_CASH_PAYMENT_MENU-"
					+ request.getAttribute("AUDIT_ID") + ":"
					+ userInfo.getUserId());
			retailerInfoMap = CommonMethods.getOrgInfoMap(userInfo
					.getUserOrgId(), "RETAILER");

		} catch (LMSException le) {
			logger.info("RESPONSE_RETAILER_CASH_PAYMENT_MENU-: ErrorCode:"
					+ le.getErrorCode() + " ErrorMessage:"
					+ le.getErrorMessage());
			request.setAttribute("LMS_EXCEPTION", le.getErrorMessage());
			return "applicationException";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("RESPONSE_RETAILER_PAYMENT_AGENT_MENU-: ErrorCode:"
					+ LMSErrors.GENERAL_EXCEPTION_ERROR_CODE + " ErrorMessage:"
					+ LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationException";
		}
		return SUCCESS;
	}
	
	
	
	
	public String retailerPayment() throws Exception {
		HttpSession session = null;
		session = getRequest().getSession();
		session.setAttribute("Receipt_Id", null);
		UserInfoBean userInfo;
		try {
			if(totalAmount!=cashAmnt)
			throw new LMSException(LMSErrors.RETAILER_PAYMENT_INVALIDATE_DATA_ERROR_CODE,LMSErrors.RETAILER_CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE);

			userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
			logger.info("REQUEST_RETAILER_CASH_PAYMENT_SUBMIT-"
					+ request.getAttribute("AUDIT_ID") + ":"
					+ userInfo.getUserId());
			int agentId = userInfo.getUserId();
			String agentName = userInfo.getOrgName();
			int userOrgID = userInfo.getUserOrgId();

			RetailerPaymentSubmitHelper retailerPaymentHelper = new RetailerPaymentSubmitHelper();

			String autoGeneRecieptNoAndId = retailerPaymentHelper
					.retailerCashPaySubmit(orgId, "RETAILER",totalAmount, agentId, userOrgID, userInfo
									.getUserType());
			if (!autoGeneRecieptNoAndId.contains("#")) {
				addActionError(autoGeneRecieptNoAndId);
				retailerInfoMap = CommonMethods.getOrgInfoMap(userInfo
						.getUserOrgId(), "RETAILER");
				return ERROR;
			}
			String[] autoGeneReceipt = autoGeneRecieptNoAndId.split("#");
			String autoGeneRecieptNo = autoGeneReceipt[0];
			int id = Integer.parseInt(autoGeneReceipt[1]);
			session.setAttribute("totalPay", totalAmount);
			//session.setAttribute("orgName", orgName);
			session.setAttribute("Receipt_Id", autoGeneRecieptNo);

			GraphReportHelper graphReportHelper = new GraphReportHelper();
			graphReportHelper.createTextReportAgent(id, (String) session
					.getAttribute("ROOT_PATH"), userOrgID, agentName);

			session.removeAttribute("CASH");
		} catch (LMSException le) {
		   
			logger.info("RESPONSE_RETAILER_CASH_PAYMENT_SUBMIT-: ErrorCode:"
					+ le.getErrorCode() + " ErrorMessage:"
					+ le.getErrorMessage());
			request.setAttribute("LMS_EXCEPTION", le.getErrorMessage());
			return "applicationException";
		} catch (Exception e) {
			logger.error("Exception",e);
			logger.info("RESPONSE_RETAILER_CASH_PAYMENT_SUBMIT-: ErrorCode:"
					+ LMSErrors.GENERAL_EXCEPTION_ERROR_CODE + " ErrorMessage:"
					+ LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationException";
		}
		return SUCCESS;

	}
	

	

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	

	public void setTootalCashAmount(double tootalCashAmount) {
		this.tootalCashAmount = tootalCashAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public void setTransaction_id2(long[] transaction_id2) {
		this.transaction_id2 = transaction_id2;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
