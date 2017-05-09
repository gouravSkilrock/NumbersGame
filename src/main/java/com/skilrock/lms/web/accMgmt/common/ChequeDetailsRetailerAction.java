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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.common.db.QueryHelper;

/**
 * This class is used to process the cheque details from Retailer.
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChequeDetailsRetailerAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double chequeBounceCharges;
	private String chequeNumber;
	private HttpServletRequest request = null;
	private long transactionId;
	private String varFromChequeSearchRetailer = null;

	/**
	 * This Method is used to process the cheque details.
	 * 
	 * @return SUCCESS throws LMSException
	 */

	@Override
	public String execute() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("ChequeDetailsRetailer", null);
		System.out.println("session" + session);
		System.out.println("chequeNumber" + chequeNumber);

		System.out.println("transactionId" + transactionId);
		QueryHelper qp = new QueryHelper();
		List<ChequeBean> searchResults = qp.SearchChequeRetailer(chequeNumber,
				transactionId, chequeBounceCharges);
		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("ChequeDetailsRetailer", searchResults);

			setVarFromChequeSearchRetailer("Yes");
		} else {
			setVarFromChequeSearchRetailer("No");

		}

		return SUCCESS;
	}

	public double getChequeBounceCharges() {
		return chequeBounceCharges;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getVarFromChequeSearchRetailer() {
		return varFromChequeSearchRetailer;
	}

	public void setChequeBounceCharges(double chequeBounceCharges) {
		this.chequeBounceCharges = chequeBounceCharges;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public void setVarFromChequeSearchRetailer(
			String varFromChequeSearchRetailer) {
		this.varFromChequeSearchRetailer = varFromChequeSearchRetailer;
	}

}
