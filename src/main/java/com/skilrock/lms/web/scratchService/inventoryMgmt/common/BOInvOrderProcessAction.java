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

import java.text.DecimalFormat;
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
import com.skilrock.lms.beans.InvOrderBean;
import com.skilrock.lms.beans.OrderBean;
import com.skilrock.lms.beans.OrderedGameBean;
import com.skilrock.lms.beans.OrgAddressBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BOOrderProcessHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GameDetailsHelper;

/**
 * This class provides methods for handling the order process(Dispatch) at BO's
 * end
 * 
 * @author Skilrock Technologies
 * 
 */

public class BOInvOrderProcessAction extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";

	/**
	 * 
	 */
	static Log logger = LogFactory.getLog(BOInvOrderProcessAction.class);
	private static final long serialVersionUID = 7945479482655333607L;

	private static String roundTo2DecimalPlaces(double value) {

		DecimalFormat df = new DecimalFormat("0.000");
		String doublevalue = df.format(value);

		System.out.println("------kfkdjd" + doublevalue + "--------");
		return doublevalue;
	}

	private int agentOrgId;
	private String agtOrgName;
	private String edit = null;
	private String end = null;
	private int gameId;
	private String gameName = null;
	private String gameNumber = null;
	private boolean isDispatch;
	private int nbrOfBooksApp;
	private int nbrOfBooksToDispatch;
	private String orderDate;
	private int orderId;
	private String orderNumber = null;
	private String orderStatus = null;
	private HttpServletRequest request;
	private String searchResultsAvailable;

	private int start = 0;

	/**
	 * When books/packs of one game have been dispatched then it come to again
	 * detail of the order having all the games to dispatch . But at this time
	 * ,that game will not be enabled to dispatch again because that is already
	 * been dispatched.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String backToOrderDetail() throws Exception {
		System.out.println("In the backToOrderDetail() method");
		HttpSession session = getRequest().getSession();
		int orderId = (Integer) session.getAttribute("ORDER_ID");
		int agtOrgId = (Integer) session.getAttribute("AGENT_ORG_ID");
		// try {
		BOOrderProcessHelper helper = new BOOrderProcessHelper();
		List<OrderedGameBean> orderdGameList = helper.fetchOrderDetails(
				orderId, agtOrgId);

		List<InvOrderBean> invOrderList = (List<InvOrderBean>) session
				.getAttribute("INV_ORDER_LIST");
		System.out
				.println("In the backToOrderDetail() method orderDetail List "
						+ orderdGameList);
		System.out.println("INV_ORDER_LIST " + invOrderList);
		InvOrderBean bean = null;
		if (invOrderList != null) {

			int orderSize = orderdGameList.size();
			int invOrderSize = invOrderList.size();
			System.out.println("Ordered game size" + orderSize
					+ "and INV_ORDER_LIST" + invOrderSize);
			// // It checks wheather has been dispatched or not if yes then
			// isDispatch will be true.
			if (orderSize >= invOrderSize) {
				boolean isDispatch = true;
				for (int i = 0; i < invOrderList.size(); i++) {
					bean = invOrderList.get(i);

					isDispatch = isDispatch
							&& bean.getOrderedGameBean()
									.getIsReadyForDispatch();
					System.out.println("isDispatch>>>>>>>>?????????/"
							+ isDispatch);
					if (isDispatch) {
						setDispatch(true);
						System.out.println("isDispatch>>>>>>>>");
						break;
					}
				}

			}

		}

		// } catch (Exception le) {
		// le.printStackTrace();
		// return ERROR;
		// }

		return SUCCESS;
	}

	/**
	 * This method is used to display details of the order which is supposed to
	 * close by the boInventory.
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String EditBoOrderStatus() throws LMSException {
		double currentBalance = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		HttpSession session = getRequest().getSession();

		// try {
		BOOrderProcessHelper helper = new BOOrderProcessHelper();
		int agtOrgId = getAgentOrgId();
		OrgAddressBean addrBean = helper.fetchAddress(agtOrgId);
		if (addrBean != null) {
			session.setAttribute("ORG_ADDR", addrBean);
		}

		// /code is added by Hanu
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		// // Agent Account Details.
		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchBOAgentAccDetail(agtOrgName);
			if (accountList != null) {
				creditrLimit = (Double) accountList.get(0);
				current = (Double) accountList.get(1);
			}
			currentBalance = current;

			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));

			System.out.println("creditLimit" + creditrLimit);

			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
		}
		session.setAttribute("ORDER_ID", getOrderId());
		session.setAttribute("ORDER_DATE", getOrderDate());

		session.setAttribute("AGT_ORG_NAME", getAgtOrgName());

		session.setAttribute("AGENT_ORG_ID", agtOrgId);
		System.out.println("Agent Org Id::" + getAgentOrgId());
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getEdit() {
		return edit;
	}

	public String getEnd() {
		return end;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNumber() {
		return gameNumber;
	}

	public boolean getIsDispatch() {
		return isDispatch;
	}

	public int getNbrOfBooksApp() {
		return nbrOfBooksApp;
	}

	public int getNbrOfBooksToDispatch() {
		return nbrOfBooksToDispatch;
	}

	public String getOrderDate() {
		return orderDate;

	}

	public int getOrderId() {
		return orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	private String getStatusCondition() {
		String actualStatus = "";
		boolean isPresent = false;
		if (orderStatus != null && !orderStatus.trim().equals("")) {
			if (orderStatus.equals("Semi Processed")) {
				actualStatus = new String("SEMI_PROCESSED");
			} else if (orderStatus.equals("Approved")) {
				actualStatus = new String("APPROVED");
			} else if (orderStatus.equals("Processed")) {
				actualStatus = new String("PROCESSED");
			}
		}

		return actualStatus;
	}

	/**
	 * This method displays the details of the selected order.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String orderDetail() throws Exception {
		double currentBalance = 0.0;

		double creditrLimit = 0.0;
		double current = 0.0;
		double availableCredit = 0.0;
		double claimable = 0.0;
		double balance = 0.0;
		int agtOrgId = getAgentOrgId();
		int total = -1;
		HttpSession session = getRequest().getSession();
		session.setAttribute("APP_ORDER_GAME_LIST", null);

		// try {
		BOOrderProcessHelper helper = new BOOrderProcessHelper();
		List<OrderedGameBean> orderdGameList = helper.fetchOrderDetails(
				getOrderId(), agtOrgId);

		total = helper.getTotalOrderedBooks();
		if (total > -1) {
			session.setAttribute("Total_Approve_books", total);
		} else {
			return ERROR;
		}
		// int agtOrgId = getAgentOrgId();
		OrgAddressBean addrBean = helper.fetchAddress(agtOrgId);
		if (addrBean != null) {
			session.setAttribute("ORG_ADDR", addrBean);
		}

		// /code is added by Hanu
		GameDetailsHelper gameHelper = new GameDetailsHelper();

		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchBOAgentAccDetail(agtOrgName);
			if (accountList != null) {
				creditrLimit = (Double) accountList.get(0);
				current = (Double) accountList.get(1);
				availableCredit = (Double) accountList.get(2);
				claimable = (Double) accountList.get(3);
				balance = availableCredit - claimable;
				System.out
						.println("**********************************available credit is ::"
								+ availableCredit);
			}
			currentBalance = current;
			// Decimal display of the double on the jsp page.
			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));
			System.out.println("creditLimit" + creditrLimit);
			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
			session.setAttribute("AVAILABLE_CREDIT_AMT",
					roundTo2DecimalPlaces(availableCredit));
			session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT",
					availableCredit);
			session.setAttribute("AVAILABLE_BALANCE",
					roundTo2DecimalPlaces(balance));// added by amit
			System.out
					.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@setttttttttt");
		}

		session.setAttribute("APP_ORDER_GAME_LIST", orderdGameList);
		session.setAttribute("ORDER_ID", getOrderId());
		session.setAttribute("ORDER_DATE", getOrderDate());
		session.setAttribute("AGT_ORG_NAME", getAgtOrgName());
		session.setAttribute("AGENT_ORG_ID", agtOrgId);
		System.out.println("Agent Org Id::" + getAgentOrgId());
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		List<InvOrderBean> invOrderList = new ArrayList<InvOrderBean>();
		session.setAttribute("INV_ORDER_LIST", invOrderList);

		return SUCCESS;
	}

	/**
	 * This method displays the games for the selected order
	 * 
	 * @return String
	 */
	public String orderGameDetail() throws LMSException {
		int countRetBooks = 0;
		int totalToAgent = 0;
		int countBooks = 0;
		HttpSession session = getRequest().getSession();
		List<OrderedGameBean> gameList = (List<OrderedGameBean>) session
				.getAttribute("APP_ORDER_GAME_LIST");
		session.setAttribute("TOTAL_BOOKS", null);
		session.setAttribute("TOTAL_BOOKS_WITH_RET_BY_AGT", null);
		session.setAttribute("TOTAL_BOOKS_FOR_AGT_BY_BO", null);
		OrderedGameBean orderedGameBean = null;
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		int gameId = getGameId();
		String agentOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		System.out.println("GameId:" + gameId);
		// How much books already with this Agent by BO
		countBooks = gameHelper.fetchBOBooksWithAgent(gameId, agentOrgName);
		// Books dispatched to retailer by this agent.
		countRetBooks = gameHelper.fetchBooksWithRetailerFromAgent(gameId,
				agentOrgName);
		// total books dispatched to this agent from BO
		totalToAgent = countBooks + countRetBooks;
		session.setAttribute("TOTAL_BOOKS", countBooks);
		session.setAttribute("TOTAL_BOOKS_WITH_RET_BY_AGT", countRetBooks);
		session.setAttribute("TOTAL_BOOKS_FOR_AGT_BY_BO", totalToAgent);
		if (gameList != null) {
			for (int i = 0; i < gameList.size(); i++) {
				orderedGameBean = gameList.get(i);
				if (gameId == orderedGameBean.getGameId()) {
					session.setAttribute("ORDERED_GAME", orderedGameBean);
					break;
				}
			}
		}

		return SUCCESS;
	}

	/**
	 * This method handles the pagination(first,next,previous and last click) in
	 * the searched results.
	 * 
	 * @return String
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("APP_ORDER_LIST1");
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
			session.setAttribute("APP_ORDER_LIST", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		System.out.println("value of Edit" + edit);
		return SUCCESS;

	}

	/**
	 * This method provide searching of order to dispatch.
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String SearchOrder() throws LMSException {

		HttpSession session = getRequest().getSession();
		session.setAttribute("APP_ORDER_LIST1", null);
		session.setAttribute("APP_ORDER_LIST", null);
		session.setAttribute("SearchResultsAvailable", null);
		session.setAttribute("Total_Approve_books", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT", null);
		// try {

		String status = null;

		System.out.println("Game Name:" + gameName);
		System.out.println("Game Nbr:" + gameNumber);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNumber);
		searchMap.put(TableConstants.ORG_NAME, agtOrgName);
		searchMap.put(GameContants.ORDER_ID, orderNumber);
		status = getStatusCondition();
		searchMap.put(TableConstants.SBO_ORDER_STATUS, status);

		// try {
		BOOrderProcessHelper helper = new BOOrderProcessHelper();

		List<OrderBean> searchResults = helper.SearchOrder(searchMap,((UserInfoBean) session.getAttribute("USER_INFO")).getRoleId());

		if (searchResults != null && searchResults.size() > 0) {
			session.setAttribute("APP_ORDER_LIST1", searchResults);
			// / session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");

		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */
		// ////pagination
		searchAjax();

		return SUCCESS;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setDispatch(boolean isDispatch) {
		this.isDispatch = isDispatch;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setNbrOfBooksApp(int nbrOfBooksApp) {
		this.nbrOfBooksApp = nbrOfBooksApp;
	}

	public void setNbrOfBooksToDispatch(int nbrOfBooksToDispatch) {
		this.nbrOfBooksToDispatch = nbrOfBooksToDispatch;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	/**
	 * This method is used to initialize all the session variable which would be
	 * used in the Order Dispatch
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String start() throws LMSException {
		HttpSession session = getRequest().getSession();
		session.setAttribute("APP_ORDER_LIST1", null);
		session.setAttribute("APP_ORDER_LIST", null);
		session.setAttribute("SearchResultsAvailable", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		return SUCCESS;

	}

	/**
	 * This method is used to Close to the Order.
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String SuccessStatusUpdate() throws LMSException {
		BOOrderProcessHelper helper = new BOOrderProcessHelper();
		if (orderStatus.equals("Close")) {
			orderStatus = "CLOSED";

		}
		if (helper.SuccessStatusUpdate(orderId, orderStatus)) {

			return SUCCESS;

		} else {

			addActionError("There is some error to update the status");
			return ERROR;
		}

	}
}
