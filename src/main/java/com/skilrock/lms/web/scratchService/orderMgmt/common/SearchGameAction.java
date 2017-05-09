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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.SearchGameHelper;

/**
 * This class provides methods for handling the Search Game functionality
 * 
 * @author Skilrock Technologies
 * 
 */
public class SearchGameAction extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(SearchGameAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agtOrgName;
	private String cartEmpty;
	private String end = null;
	private String endFromDate;

	private String endToDate;
	private String gameName;
	private String gameNbr;
	private String priceMatch;

	private HttpServletRequest request;
	private String retOrgName;

	private String searchResultsAvailable;
	int start = 0;

	private String startFromDate;
	private String startToDate;

	private String ticketPrice;

	/**
	 * This method provides the search game to create order
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String agentSearch() throws Exception {
		System.out.println("----Ret OrgName---" + getRetOrgName());

		String retOrgName = getRetOrgName();

		HttpSession session = getRequest().getSession();
		session.setAttribute("RET_ORG_NAME", retOrgName);

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtOrgId = userInfoBean.getUserOrgId();

		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);

		System.out.println("Game Name:" + gameName);
		System.out.println("Game Nbr:" + gameNbr);

		session.setAttribute("RET_ORG_NAME", getRetOrgName());

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNbr);
		searchMap.put(GameContants.FROM_DATE, startFromDate);
		searchMap.put(GameContants.TO_DATE, startToDate);
		searchMap.put(GameContants.ST1_END_FROM_DATE, endFromDate);
		searchMap.put(GameContants.ST1_END_TO_DATE, endToDate);

		String priceCondition = getPriceCondition();

		searchMap.put(GameContants.PRICE_CONDITION, priceCondition);

		// try {
		SearchGameHelper searchGameHelper = new SearchGameHelper();
		List searchResults = searchGameHelper.searchAgentGame(searchMap,
				agtOrgId);

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			setSearchResultsAvailable("Yes");
		} else {
			setSearchResultsAvailable("No");
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		List cartList = (List) session.getAttribute("CART");

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		searchAjax();

		return SUCCESS;
	}

	/**
	 * This method provides the search game to create order
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String boSearch() throws Exception {
		// System.out.println("----Ret OrgName---" + getAgtOrgName());

		String agtOrgName = getAgtOrgName();

		HttpSession session = getRequest().getSession();
		session.setAttribute("AGT_ORG_NAME", agtOrgName);

		return search();
	}

	/**
	 * This method is called when the cancel button is pressed
	 * 
	 * @return String
	 */
	public String cancelCart() {

		HttpSession session = getRequest().getSession();
		session.setAttribute("CART", null);
		session.setAttribute("AGT_ORG_NAME", null);
		session.setAttribute("RET_ORG_NAME", null);
		session.setAttribute("SEARCH_RESULTS1", null);
		// session.setAttribute("TOTAL", null);
		// session.setAttribute("BALANCE", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);

		System.out.println("---Abt to set org name as null");
		setAgtOrgName(null);
		setRetOrgName(null);

		return SUCCESS;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getCartEmpty() {
		return cartEmpty;
	}

	public String getEnd() {
		return end;
	}

	public String getEndFromDate() {
		return endFromDate;
	}

	public String getEndToDate() {
		return endToDate;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNbr() {
		return gameNbr;
	}

	private String getPriceCondition() {
		String priceCond = null;
		boolean isPresent = false;
		if (priceMatch != null && !priceMatch.trim().equals("")) {
			if (priceMatch.equals("Less Than Equal To")) {
				priceCond = new String("<= ");
			} else if (priceMatch.equals("Greater Than Equal To")) {
				priceCond = new String(">= ");
			} else if (priceMatch.equals("Equal To")) {
				priceCond = new String("= ");
			}
		}

		if (priceCond != null && ticketPrice != null
				&& !ticketPrice.trim().equals("")) {
			isPresent = true;
			priceCond = priceCond + ticketPrice.trim();

		}

		System.out.println("PriceCond:" + priceCond);
		if (isPresent) {
			return priceCond;
		}
		return null;
	}

	public String getPriceMatch() {
		return priceMatch;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStartFromDate() {
		return startFromDate;
	}

	public String getStartToDate() {
		return startToDate;
	}

	public String getTicketPrice() {
		return ticketPrice;
	}

	/**
	 * This method takes the user back to the search page
	 * 
	 * @return String
	 */
	public String goToSearchGame() {

		HttpSession session = getRequest().getSession();
		List cartList = (List) session.getAttribute("CART");

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		return SUCCESS;
	}

	/**
	 * This method provides the search game for order.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String search() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);

		System.out.println("Game Name:" + gameName);
		System.out.println("Game Nbr:" + gameNbr);

		// added by yogesh to disable agent org name for second time

		session.setAttribute("AGENT_ORG_NAME", getAgtOrgName());
		System.out.println("Agent org name is    " + getAgtOrgName());

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNbr);
		searchMap.put(GameContants.FROM_DATE, startFromDate);
		searchMap.put(GameContants.TO_DATE, startToDate);
		searchMap.put(GameContants.ST1_END_FROM_DATE, endFromDate);
		searchMap.put(GameContants.ST1_END_TO_DATE, endToDate);

		String priceCondition = getPriceCondition();

		searchMap.put(GameContants.PRICE_CONDITION, priceCondition);

		// try {
		SearchGameHelper searchGameHelper = new SearchGameHelper();

		// get agent org Id by agent name added by yogesh

		int agtOrgId = searchGameHelper.getOrgIdByOrgName(getAgtOrgName());

		// Helper class method call
		List searchResults = searchGameHelper.searchGame(searchMap, agtOrgId);

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			setSearchResultsAvailable("Yes");
		} else {
			setSearchResultsAvailable("No");
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		List cartList = (List) session.getAttribute("CART");

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		searchAjax();

		return SUCCESS;
	}

	/**
	 * This method handles the first,next,previous and last click on the order
	 * request page
	 * 
	 * @return String
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			System.out.println("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;

			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

			List cartList = (List) session.getAttribute("CART");

			if (cartList != null && cartList.size() > 0) {
				setCartEmpty("No");
			}
		}
		return SUCCESS;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setCartEmpty(String cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEndFromDate(String endFromDate) {
		this.endFromDate = endFromDate;
	}

	public void setEndToDate(String endToDate) {
		this.endToDate = endToDate;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(String gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setPriceMatch(String priceMatch) {
		this.priceMatch = priceMatch;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStartFromDate(String startFromDate) {
		this.startFromDate = startFromDate;
	}

	public void setStartToDate(String startToDate) {
		this.startToDate = startToDate;
	}

	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

}
