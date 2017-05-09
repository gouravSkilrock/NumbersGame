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

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.GameDetailsBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GameDetailsHelper;

/**
 * This class provides methods displaying the game details
 * 
 * @author Skilrock Technologies
 * 
 */
public class GameDetailsAction extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(GameDetailsAction.class);

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

	private String agtOrgName;
	private String cartEmpty;
	private int gameId;
	private int gameQty;
	/*
	 * private String gameName; private int gameNbr; private int ticketPrice;
	 * private int orderedQty; private java.sql.Date startDate; private
	 * java.sql.Date saleEndDate; private java.sql.Date pwtEndDate; private int
	 * ticketsPerBook; private int booksPerPack;
	 */
	private List<PrizeStatusBean> prizeStatusList;

	private HttpServletRequest request;

	private String retOrgName;

	private String searchResultsAvailable;

	/**
	 * This method handles the cancel button on the game details page ,Used to
	 * cancel the process.
	 * 
	 * @return String
	 */
	public String cancelAgentGameDetails() {
		System.out.println("-----Abt to cancel Agent Game Details----");

		HttpSession session = getRequest().getSession();

		List cartList = (List) session.getAttribute("CART");
		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		List searchResults = (List) session.getAttribute("SEARCH_RESULTS");
		if (searchResults != null && searchResults.size() > 0) {
			setSearchResultsAvailable("Yes");
		}
		return SUCCESS;

	}

	/**
	 * This method handles the cancel button on the game details page .It cancel
	 * the process.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String cancelBOGameDetails() {
		System.out.println("-----Abt to cancel BO Game Details----");

		HttpSession session = getRequest().getSession();

		List cartList = (List) session.getAttribute("CART");
		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}

		List searchResults = (List) session.getAttribute("SEARCH_RESULTS");
		if (searchResults != null && searchResults.size() > 0) {
			setSearchResultsAvailable("Yes");
		}
		return SUCCESS;

	}

	/**
	 * This method handles the cancel button on the game details page
	 * 
	 * @return String
	 */
	public String cancelGameDetails() {
		System.out.println("-----Abt to cancel Game Details----");

		HttpSession session = getRequest().getSession();

		List cartList = (List) session.getAttribute("CART");
		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		return SUCCESS;

	}

	/**
	 * This method fetches the game details
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String fetchAgentGameDetails() throws Exception {

		System.out.println("GameId::" + getGameId());
		int gameId = getGameId();
		HttpSession session = getRequest().getSession();
		double currentBalance = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		double availableCreditforRet = 0.0;
		int countBooks = 0;
		session.setAttribute("TOTAL_BOOKS", null);

		// *********Get Agent Org Id from Session*******
		// get currently logged in user info from the session
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agentOrgId = userInfoBean.getUserOrgId();
		String agentOrgName = userInfoBean.getOrgName();
		// *********************************************

		// try {
		String RetOrgName = (String) session.getAttribute("RET_ORG_NAME");
		// fetch game details
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		GameDetailsBean bean = gameHelper.fetchAgentGameDetails(gameId,
				agentOrgId);

		countBooks = gameHelper.fetchAgentBooksWithRetailer(gameId,
				agentOrgName);

		// fetch remaining prizes for the game

		List<PrizeStatusBean> prizeList = gameHelper
				.fetchRemainingPrizeList(gameId);
		if (prizeList != null) {
			setPrizeStatusList(prizeList);

		}
		session.setAttribute("GAME_DETAILS_BEAN", bean);
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		session.setAttribute("TOTAL_BOOKS", countBooks);

		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchAgentRetailerAccDetail(RetOrgName);
			if (accountList != null) {
				creditrLimit = accountList.get(0);
				current = accountList.get(1);
				availableCreditforRet = accountList.get(2);
			}
			currentBalance = current;
			// System.out.println("Balance is not null and value
			// is"+((Double)session.getAttribute("BALANCE")).doubleValue());
			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));
			// session.setAttribute("TOTAL", totalAmount);
			// System.out.println("creditLimit"+creditrLimit);
			// System.out.println("BALANCE"+currentBalance);
			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
			session.setAttribute("AVAIL_AMT_RET", CommonMethods
					.fmtToTwoDecimal(availableCreditforRet));
			// session.setAttribute("BALANCE", 0.0);
		}

		List cartList = (List) session.getAttribute("CART");

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
			Iterator it = cartList.iterator();
			while (it.hasNext()) {
				GameBean gameBean = (GameBean) it.next();
				int id = gameBean.getGameId();
				if (id == gameId) {
					setGameQty(gameBean.getOrderedQty());
					break;
				}

			}
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		return SUCCESS;
	}

	/**
	 * This method fetches the game details ,and the owner details means that
	 * how much books of this game is with Retailer and the Agent .
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String fetchBOGameDetails() throws Exception {

		System.out.println("GameId::" + getGameId());
		HttpSession session = getRequest().getSession();
		double currentBalance = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		double availableCreditLimit = 0.0;
		int countBooks = 0;
		int countRetBooks = 0;
		int totalToAgent = 0;

		session.setAttribute("TOTAL_BOOKS", null);
		session.setAttribute("TOTAL_BOOKS_WITH_RET_BY_AGT", null);
		session.setAttribute("TOTAL_BOOKS_FOR_AGT_BY_BO", null);
		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		GameDetailsBean bean = gameHelper.fetchBOGameDetails(getGameId());
		// /
		countBooks = gameHelper.fetchBOBooksWithAgent(gameId, agtOrgName);
		countRetBooks = gameHelper.fetchBooksWithRetailerFromAgent(gameId,
				agtOrgName);
		totalToAgent = countBooks + countRetBooks;
		// fetch remaining prizes for the game
		List<PrizeStatusBean> prizeList = gameHelper
				.fetchRemainingPrizeList(gameId);
		if (prizeList != null) {
			setPrizeStatusList(prizeList);

		}
		session.setAttribute("TOTAL_BOOKS", countBooks);
		session.setAttribute("TOTAL_BOOKS_WITH_RET_BY_AGT", countRetBooks);
		session.setAttribute("TOTAL_BOOKS_FOR_AGT_BY_BO", totalToAgent);

		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchBOAgentAccDetail(agtOrgName);
			if (accountList != null) {
				creditrLimit = accountList.get(0);
				current = accountList.get(1);
				availableCreditLimit = accountList.get(2);
			}
			currentBalance = current;
			// System.out.println("Balance is not null and value
			// is"+((Double)session.getAttribute("BALANCE")).doubleValue());
			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));
			// session.setAttribute("TOTAL", totalAmount);
			// System.out.println("creditLimit"+creditrLimit);
			// System.out.println("BALANCE"+currentBalance);
			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
			session.setAttribute("AVAIL_AMT", CommonMethods
					.fmtToTwoDecimal(availableCreditLimit));
			// session.setAttribute("BALANCE", 0.0);
		}
		// else{

		// currentBalance=current-((Double)session.getAttribute("BALANCE")).doubleValue();
		// System.out.println("Balance is not null and value
		// is"+((Double)session.getAttribute("BALANCE")).doubleValue());

		// }

		// session.setAttribute("CREDIT_LIMIT", null);
		// session.setAttribute("BALANCE", null);

		List cartList = (List) session.getAttribute("CART");
		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
			Iterator it = cartList.iterator();
			while (it.hasNext()) {
				GameBean gameBean = (GameBean) it.next();
				int id = gameBean.getGameId();
				if (id == gameId) {
					setGameQty(gameBean.getOrderedQty());
					break;
				}

			}
		}
		session.setAttribute("GAME_DETAILS_BEAN", bean);
		/*
		 * }catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		return SUCCESS;
	}

	/**
	 * This method fetches the game details:-
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String fetchGameDetails() throws Exception {

		System.out.println("GameId::" + getGameId());
		double currentBalance = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		int countBooks = 0;
		int gameId = getGameId();
		HttpSession session = getRequest().getSession();
		session.setAttribute("TOTAL_BOOKS", null);
		// try {

		// fetch game details

		GameDetailsHelper gameHelper = new GameDetailsHelper();
		GameDetailsBean bean = gameHelper.fetchGameDetails(getGameId());

		session.setAttribute("GAME_DETAILS_BEAN", bean);

		// fetch remaining prizes for the game
		List<PrizeStatusBean> prizeList = gameHelper
				.fetchRemainingPrizeList(gameId);
		if (prizeList != null) {
			setPrizeStatusList(prizeList);
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String agentOrgName = userInfoBean.getOrgName();
		countBooks = gameHelper.fetchBOBooksWithAgent(gameId, agentOrgName);
		session.setAttribute("TOTAL_BOOKS", countBooks);
		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchBOAgentAccDetail(agentOrgName);
			if (accountList != null) {
				creditrLimit = accountList.get(0);
				current = accountList.get(1);
			}
			currentBalance = current;
			// System.out.println("Balance is not null and value
			// is"+((Double)session.getAttribute("BALANCE")).doubleValue());
			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));
			// session.setAttribute("TOTAL", totalAmount);
			// System.out.println("creditLimit"+creditrLimit);
			// System.out.println("BALANCE"+currentBalance);
			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
			// session.setAttribute("BALANCE", 0.0);
		}

		List cartList = (List) session.getAttribute("CART");

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
			Iterator it = cartList.iterator();
			while (it.hasNext()) {
				GameBean gameBean = (GameBean) it.next();
				int id = gameBean.getGameId();
				if (id == gameId) {
					setGameQty(gameBean.getOrderedQty());
					break;
				}

			}
		}

		return SUCCESS;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getCartEmpty() {
		return cartEmpty;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameQty() {
		return gameQty;
	}

	public List<PrizeStatusBean> getPrizeStatusList() {
		return prizeStatusList;
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

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setCartEmpty(String cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameQty(int gameQty) {
		this.gameQty = gameQty;
	}

	public void setPrizeStatusList(List<PrizeStatusBean> prizeStatusList) {
		this.prizeStatusList = prizeStatusList;
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
}
