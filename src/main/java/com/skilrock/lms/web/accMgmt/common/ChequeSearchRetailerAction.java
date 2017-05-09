/***
 *  * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.accMgmt.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.Constants;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.accMgmt.common.SearchChequeHelper;

/**
 * This method is used for Cheque Search whose owner is Retailer and status is
 * CHEQUE
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChequeSearchRetailerAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double chequeBounceCharge;

	private String chequeNumber;
	private HttpServletRequest request;
	private String varFromChequeSearchRetailer = null;

	public double getChequeBounceCharge() {
		return chequeBounceCharge;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVarFromChequeSearchRetailer() {
		return varFromChequeSearchRetailer;
	}

	public String processChequeSearch() {
		return SUCCESS;
	}

	/**
	 * This method is used for Cheque Search whose owner is Retailer and status
	 * is CHEQUE
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */

	public String search() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("CHEQUE_SEARCH_RESULTS_RETAILER", null);

		System.out.println("hello i sm in search cheque");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(Constants.CHEQUE_NUMBER, chequeNumber);

		SearchChequeHelper searchChequeHelper = new SearchChequeHelper();
		List searchResults = searchChequeHelper.searchChequeRetailer(searchMap,
				userBean.getUserId(), getChequeBounceCharge());

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("CHEQUE_SEARCH_RESULTS_RETAILER",
					searchResults);
			setVarFromChequeSearchRetailer("Yes");
		} else {
			setVarFromChequeSearchRetailer("No");
		}

		return SUCCESS;
	}

	public void setChequeBounceCharge(double chequeBounceCharge) {
		this.chequeBounceCharge = chequeBounceCharge;
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

	public void setVarFromChequeSearchRetailer(
			String varFromChequeSearchRetailer) {
		this.varFromChequeSearchRetailer = varFromChequeSearchRetailer;
	}

}
