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
import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.coreEngine.accMgmt.common.SearchChequeHelper;

/**
 * This method is used for Cheque Search whose owner is Agent and status is
 * CHEQUE
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChequeSearchAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String chequeNumber;

	private HttpServletRequest request;
	private String varFromChequeSearch;

	// private double bounceCharges;

	public String getChequeNumber() {
		return chequeNumber;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVarFromChequeSearch() {
		return varFromChequeSearch;
	}

	public String processChequeSearch() {

		return SUCCESS;
	}

	/**
	 * This method is used for Cheque Search whose owner is Agent and status is
	 * CHEQUE
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */

	public String search() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("CHEQUE_SEARCH_RESULTS", null);

		System.out.println("hello i sm in search game");

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(Constants.CHEQUE_NUMBER, chequeNumber);

		SearchChequeHelper searchChequeHelper = new SearchChequeHelper();
		List<ChequeBean> searchResults = searchChequeHelper
				.searchCheque(searchMap);

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("CHEQUE_SEARCH_RESULTS", searchResults);
			setVarFromChequeSearch("Yes");
		} else {
			setVarFromChequeSearch("No");
		}

		return SUCCESS;
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

	public void setVarFromChequeSearch(String varFromChequeSearch) {
		this.varFromChequeSearch = varFromChequeSearch;
	}

}
