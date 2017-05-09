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
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.AgentOrderProcessHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GameDetailsHelper;

/**
 * This class provides methods for handling the order process at Agent end
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentOrderProcessAction extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(AgentOrderProcessAction.class);

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

	private String date;
	private String edit = null;
	private String end = null;
	private int gameId;
	private String gameName = null;
	private String gameNumber;
	private boolean isDispatch;

	private java.sql.Date orderDate;
	private int orderId;
	private String orderNumber;
	private String orderStatus;
	private HttpServletRequest request;
	private int retOrgId;
	private String retOrgName;
	private String searchCriteria = "";

	private int start = 0;

	/**
	 * This method displays the details of the selected order
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String agentOrderDetail() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("AGT_APP_ORDER_GAME_LIST", null);
		session.setAttribute("Total_Approve_books", null);
		double currentBalance = 0.0;
		double totalAmount = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		double availableCredit = 0.0;
		double claimable = 0.0;
		double balance = 0.0;
		int retOrgId = getRetOrgId();
		// try {
		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
		List<OrderedGameBean> orderdGameList = helper.fetchOrderDetails(
				getOrderId(), retOrgId);
		int total = helper.getTotalOrderedBooks();
		System.out.println("TOTAL APPROVED BOOKS FOR THIS ORDER" + total);
		session.setAttribute("Total_Approve_books", total);
		session.setAttribute("AGT_APP_ORDER_GAME_LIST", orderdGameList);
		session.setAttribute("AGT_ORDER_ID", getOrderId());

		// int retOrgId = getRetOrgId();

		OrgAddressBean addrBean = helper.fetchAddress(retOrgId);

		if (addrBean != null) {
			session.setAttribute("RET_ORG_ADDR", addrBean);
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		// added by Hanu
		session.setAttribute("RETAILER_ORG_ID", retOrgId);
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null) {
			List<Double> accountList = gameHelper
					.fetchAgentRetailerAccDetail(getRetOrgName());
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
			// System.out.println("Balance is not null and value
			// is"+((Double)session.getAttribute("BALANCE")).doubleValue());
			session.setAttribute("CREDIT_LIMIT",
					roundTo2DecimalPlaces(creditrLimit));
			// session.setAttribute("TOTAL", totalAmount);
			// System.out.println("creditLimit"+creditrLimit);
			// System.out.println("BALANCE"+currentBalance);
			session.setAttribute("CREDIT_AMT",
					roundTo2DecimalPlaces(currentBalance));
			session.setAttribute("AVAILABLE_CREDIT_AMT_AGT",
					roundTo2DecimalPlaces(availableCredit));
			session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT_AGT",
					availableCredit);
			session.setAttribute("AVAILABLE_BALANCE",
					roundTo2DecimalPlaces(balance));// added by amit
			System.out
					.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@setttttttttt");

			// session.setAttribute("BALANCE", 0.0);
		}
		System.out.println("Retailer  Org Id::" + getRetOrgId());

		session.setAttribute("AGT_ORDER_DATE", getOrderDate());

		session.setAttribute("RET_ORG_NAME", getRetOrgName());
		session.setAttribute("RET_ORG_ID", getRetOrgId());

		List<InvOrderBean> invOrderList = new ArrayList<InvOrderBean>();
		session.setAttribute("AGT_INV_ORDER_LIST", invOrderList);

		return SUCCESS;
	}

	/**
	 * This method displays the games for the selected order
	 * 
	 * @return String
	 */
	public String agentOrderGameDetail() throws LMSException {
		int countBooks = 0;
		HttpSession session = getRequest().getSession();
		List<OrderedGameBean> gameList = (List<OrderedGameBean>) session
				.getAttribute("AGT_APP_ORDER_GAME_LIST");
		OrderedGameBean orderedGameBean = null;
		session.setAttribute("TOTAL_BOOKS", null);
		int gameId = getGameId();
		System.out.println("GameId:" + gameId);

		if (gameList != null) {
			for (int i = 0; i < gameList.size(); i++) {
				orderedGameBean = gameList.get(i);
				if (gameId == orderedGameBean.getGameId()) {
					session.setAttribute("AGT_ORDERED_GAME", orderedGameBean);
					break;
				}
			}
		}
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		countBooks = gameHelper.fetchAgentBooksWithRetailer(gameId,
				(String) session.getAttribute("RET_ORG_NAME"));
		System.out.println("Retailer Books ::" + countBooks);
		session.setAttribute("TOTAL_BOOKS", countBooks);
		return SUCCESS;
	}

	/**
	 * This method takes the user back to the order detail page
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String backToAgentOrderDetail() throws Exception {

		HttpSession session = getRequest().getSession();
		int orderId = (Integer) session.getAttribute("AGT_ORDER_ID");
		int retOrgId = (Integer) session.getAttribute("RET_ORG_ID");
		// try {
		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
		List<OrderedGameBean> orderdGameList = helper.fetchOrderDetails(
				orderId, retOrgId);

		List<InvOrderBean> invOrderList = (List<InvOrderBean>) session
				.getAttribute("AGT_INV_ORDER_LIST");
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
			if (orderSize >= invOrderSize) {
				boolean isDispatch = true;
				for (int i = 0; i < invOrderList.size(); i++) {
					bean = invOrderList.get(i);

					isDispatch = isDispatch
							&& bean.getOrderedGameBean()
									.getIsReadyForDispatch();
					System.out.println("isDispatch>>>>>>>>?????????/"
							+ isDispatch);
					// // It checks wheather this order is completely dispatched
					// or not.
					if (isDispatch) {
						setDispatch(true);
						System.out.println("isDispatch>>>>>>>>");
						break;
					}
				}

			}

		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		return SUCCESS;
	}

	/**
	 * This method dispatches the order for the retailer
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String assignAgentOrder() throws Exception {

		HttpSession session = getRequest().getSession();
		List<InvOrderBean> invOrderList = (List<InvOrderBean>) session
				.getAttribute("AGT_INV_ORDER_LIST");

		int orderId = (Integer) session.getAttribute("AGT_ORDER_ID");
		int retOrgId = (Integer) session.getAttribute("RETAILER_ORG_ID");
		int totalApproveBooksForOrder = (Integer) session
				.getAttribute("Total_Approve_books");
		// *******************remove this************
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtId = userInfoBean.getUserId();
		String loggedInUserOrgName = userInfoBean.getOrgName();
		int userOrgID = userInfoBean.getUserOrgId();
		// int agtId = 111111;
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		System.out.println("----------------InvOrderList::"
				+ invOrderList.size());
		if (invOrderList != null) {
			// try {
			AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
			int DCId = helper.assignOrder(invOrderList, totalApproveBooksForOrder, orderId, retOrgId, agtId, rootPath, loggedInUserOrgName, userOrgID);
			session.setAttribute("DEL_CHALLAN_ID", DCId);
			/*
			 * } catch (LMSException le) { return APPLICATION_ERROR; }
			 */
		}
		return SUCCESS;
	}

	public String EditAgentOrdertatus() throws Exception {

		System.out.println("inside method ---------  EditAgentOrdertatus()");
		HttpSession session = getRequest().getSession();
		session.setAttribute("AGT_APP_ORDER_GAME_LIST", null);
		session.setAttribute("Total_Approve_books", null);
		double currentBalance = 0.0;
		double totalAmount = 0.0;
		double creditrLimit = 0.0;
		double current = 0.0;
		int retOrgId = getRetOrgId();
		// try {
		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
		List<OrderedGameBean> orderdGameList = helper.fetchOrderDetails(
				getOrderId(), retOrgId);
		int total = helper.getTotalOrderedBooks();
		System.out.println("TOTAL APPROVED BOOKS FOR THIS ORDER : " + total);
		session.setAttribute("Total_Approve_books", total);
		session.setAttribute("AGT_APP_ORDER_GAME_LIST", orderdGameList);
		session.setAttribute("AGT_ORDER_ID", getOrderId());

		// int retOrgId =getRetOrgId();

		OrgAddressBean addrBean = helper.fetchAddress(retOrgId);

		if (addrBean != null) {
			session.setAttribute("RET_ORG_ADDR", addrBean);
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		// added by Hanu
		session.setAttribute("RETAILER_ORG_ID", retOrgId);
		GameDetailsHelper gameHelper = new GameDetailsHelper();
		if (session.getAttribute("BALANCE") == null
				|| session.getAttribute("CREDIT_LIMIT") == null)

		{
			List<Double> accountList = gameHelper
					.fetchAgentRetailerAccDetail(getRetOrgName());
			if (accountList != null) {
				creditrLimit = (Double) accountList.get(0);
				current = (Double) accountList.get(1);
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
		System.out.println("Retailer  Org Id::" + getRetOrgId());

		session.setAttribute("AGT_ORDER_DATE", getDate());
		System.out
				.println("oder date : ------------------------------------------------"
						+ getDate());
		session.setAttribute("date", getDate());
		session.setAttribute("RET_ORG_NAME", getRetOrgName());
		session.setAttribute("RET_ORG_ID", getRetOrgId());

		return SUCCESS;
	}

	public String getDate() {
		return date;
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

	/**
	 * This method is used for displaying approved orders
	 * 
	 * @return String
	 * @throws Exception
	 */
	/*
	 * public String displayAppAgentOrders() throws Exception { HttpSession
	 * session = getRequest().getSession();
	 * session.setAttribute("AGT_APP_ORDER_LIST", null);
	 * 
	 * UserInfoBean userInfoBean = (UserInfoBean) session
	 * .getAttribute("USER_INFO"); int agtOrgId = userInfoBean.getUserOrgId();
	 * //session.setAttribute("TOTAL",null);
	 * //session.setAttribute("BALANCE",null);
	 * session.setAttribute("CREDIT_LIMIT",null);
	 * session.setAttribute("CREDIT_AMT",null);
	 * 
	 * //try { AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
	 * List<AgentOrderBean> orderList = helper.getApprovedOrders(agtOrgId);
	 * session.setAttribute("AGT_APP_ORDER_LIST", orderList); } catch
	 * (LMSException le) { return APPLICATION_ERROR; }
	 * 
	 * return SUCCESS; }
	 */

	public String getGameNumber() {
		return gameNumber;
	}

	public boolean getIsDispatch() {
		return isDispatch;
	}

	public java.sql.Date getOrderDate() {
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

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public String getSearchCriteria() {
		return searchCriteria;
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
		List ajaxList = (List) session.getAttribute("AGT_APP_ORDER_LIST1");
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
			session.setAttribute("AGT_APP_ORDER_LIST", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);

		}
		return SUCCESS;

	}

	/**
	 * This method provides the search order
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String SearchOrder() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("AGT_APP_ORDER_LIST1", null);
		session.setAttribute("AGT_APP_ORDER_LIST", null);
		session.setAttribute("SearchResultsAvailable", null);
		session.setAttribute("Total_Approve_books", null);
		// session.setAttribute("TOTAL",null);
		// session.setAttribute("BALANCE",null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT_AGT", null);
		// try {
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtOrgId = userInfoBean.getUserOrgId();

		String status = null;

		System.out.println("Game Name:" + gameName);
		System.out.println("Game Nbr:" + gameNumber);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNumber);
		searchMap.put(TableConstants.ORG_NAME, retOrgName);
		searchMap.put(GameContants.ORDER_ID, orderNumber);
		status = getStatusCondition();
		searchMap.put(TableConstants.SBO_ORDER_STATUS, status);

		// try {
		// BOOrderProcessHelper helper = new BOOrderProcessHelper();
		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();

		List<OrderBean> searchResults = helper.SearchOrder(searchMap, agtOrgId);
		if (searchResults != null && searchResults.size() > 0) {
			session.setAttribute("AGT_APP_ORDER_LIST1", searchResults);

			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");

		} else {

			session.setAttribute("SearchResultsAvailable", "No");
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		searchAjax();

		return SUCCESS;
	}

	public void setDate(String date) {
		this.date = date;
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

	public void setOrderDate(java.sql.Date orderDate) {
		System.out.println("====order date " + orderDate);
		this.orderDate = orderDate;
	}

	public void setOrderId(int orderId) {
		System.out.println("==================== order id " + orderId);
		this.orderId = orderId;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setRetOrgId(int retOrgId) {
		System.out.println(" retailer org id : " + retOrgId);
		this.retOrgId = retOrgId;
	}

	public void setRetOrgName(String retOrgName) {
		System.out.println(" retailer org name : " + retOrgName);
		this.retOrgName = retOrgName;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * This method is used to initialize Dispatch order process ,it used to make
	 * all session attributes null.
	 * 
	 * @return
	 * @throws Exception
	 */

	public String start() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("AGT_APP_ORDER_LIST", null);
		session.setAttribute("AGT_APP_ORDER_LIST1", null);
		session.setAttribute("SearchResultsAvailable", null);

		// session.setAttribute("TOTAL",null);
		// session.setAttribute("BALANCE",null);
		// //credit limit and the current credit amount
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		return SUCCESS;

	}

	public String SuccessStatusUpdate() throws Exception {
		AgentOrderProcessHelper helper = new AgentOrderProcessHelper();
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
