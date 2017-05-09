package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.SearchOpenGameHelper;

/**
 * This Action class provides methods for search close Games and sale close
 * Games
 * 
 * @author ABC
 * 
 */
public class ProcessOpenGameAction extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";
	static Log logger = LogFactory.getLog(ProcessOpenGameAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end = null;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private List<PrizeStatusBean> prizeStatusList;
	private HttpServletRequest request;
	int start = 0;
	private String usersearchResultsAvailable;

	public String getEnd() {
		return end;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public List<PrizeStatusBean> getPrizeStatusList() {
		return prizeStatusList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getStart() {
		return start;
	}

	public String getUsersearchResultsAvailable() {
		return usersearchResultsAvailable;
	}

	/**
	 * This method is used to search the sale close games,and provides details
	 * of any Game for given game id
	 * 
	 * @return String SUCCESS
	 */
	public String saleCloseGames() {
		HttpSession session = getRequest().getSession();
		session.setAttribute("SALE_ClOSE_SEARCH_RESULTS", null);
		session.setAttribute("SALE_ClOSE_SEARCH_RESULTS1", null);
		gameId = getGameId();
		try {
			SearchOpenGameHelper searchGameHelper = new SearchOpenGameHelper();
			List searchResults;
			if (gameId == 0) {
				System.out
						.println("**********************inside gami id=0 method");
				searchResults = searchGameHelper.searcCloseSale();
			} else {
				System.out.println("yesssssssssss");
				// System.out.println(searchGameHelper.searchGameDetails(gameId));
				// another function is called for game details
				System.out
						.println("another function is called for game details ");
				searchResults = searchGameHelper.searchGameDetails(gameId);

				List<PrizeStatusBean> prizeList = searchGameHelper
						.fetchRemainingPrizeList(gameId);
				if (prizeList != null) {
					System.out.println("inside list");
					setPrizeStatusList(prizeList);
				}
				System.out.println("1   " + searchResults);
			}
			System.out.println(searchResults);
			if (searchResults != null && searchResults.size() > 0) {
				System.out.println("Yes:---Search result Processed");

				session.setAttribute("SALE_ClOSE_SEARCH_RESULTS1",
						searchResults);

				session.setAttribute("startValueOrgSearch", new Integer(0));
				setUsersearchResultsAvailable("Yes");
			} else {
				setUsersearchResultsAvailable("No");
				System.out.println("No:---Search result Processed");
			}

			searchAjaxSale();
			return SUCCESS;
		} catch (LMSException le) {
			return APPLICATION_ERROR;
		}
	}

	public String saleCloseGamesAfterCancel() {
		HttpSession session = getRequest().getSession();
		session.setAttribute("SALE_ClOSE_SEARCH_RESULTS", null);
		session.setAttribute("SALE_ClOSE_SEARCH_RESULTS1", null);
		gameId = getGameId();
		try {
			SearchOpenGameHelper searchGameHelper = new SearchOpenGameHelper();
			List searchResults;

			System.out
					.println("**********************inside gami id=0 method 2222222222");
			searchResults = searchGameHelper.searcCloseSale();

			System.out.println(searchResults);
			if (searchResults != null && searchResults.size() > 0) {
				System.out.println("Yes:---Search result Processed");

				session.setAttribute("SALE_ClOSE_SEARCH_RESULTS1",
						searchResults);

				session.setAttribute("startValueOrgSearch", new Integer(0));
				setUsersearchResultsAvailable("Yes");
			} else {
				setUsersearchResultsAvailable("No");
				System.out.println("No:---Search result Processed");
			}

			searchAjaxSale();
			return SUCCESS;
		} catch (LMSException le) {
			return APPLICATION_ERROR;
		}
	}

	/**
	 * This method is used to set the game status from OPEN to SALE_HOLD for
	 * given game_id
	 * 
	 * @return String SUCCESS
	 */

	public String saleOpenStatus() {
		HttpSession session = getRequest().getSession();
		gameId = getGameId();
		System.out.println("game id is " + gameId);
		session.setAttribute("GAME_NAME", getGameName());
		session.setAttribute("GAME_NBR", getGameNbr());
		SearchOpenGameHelper searchGameHelper = new SearchOpenGameHelper();
		try {
			searchGameHelper.saleOpenStatus(gameId);
		} catch (LMSException le) {
			return APPLICATION_ERROR;

		}
		return SUCCESS;
	}

	/**
	 * This method is used to search closed games
	 * 
	 * @return String
	 */
	public String search() {
		HttpSession session = getRequest().getSession();
		session.setAttribute("GAME_SEARCH_RESULTS", null);
		session.setAttribute("GAME_SEARCH_RESULTS1", null);
		gameId = getGameId();
		// Map<String,String> searchMap = new HashMap<String,String>();
		try {
			SearchOpenGameHelper searchGameHelper = new SearchOpenGameHelper();
			List searchResults;
			if (gameId == 0) {
				searchResults = searchGameHelper.searchUser();

			} else {
				System.out.println("555555555555555555");
				searchResults = searchGameHelper.searchGameDetails(gameId);
				System.out.println(searchResults);

				List<PrizeStatusBean> prizeList = searchGameHelper
						.fetchRemainingPrizeList(gameId);
				if (prizeList != null) {
					setPrizeStatusList(prizeList);
				}

				System.out.println("1   " + searchResults);
			}
			System.out.println(searchResults);
			if (searchResults != null && searchResults.size() > 0) {
				System.out.println("Yes:---Search result Processed");
				session.setAttribute("GAME_SEARCH_RESULTS1", searchResults);
				session.setAttribute("startValueOrgSearch", new Integer(0));
				setUsersearchResultsAvailable("Yes");
			} else {
				setUsersearchResultsAvailable("No");
				System.out.println("No:---Search result Processed");
			}
			searchAjax();

			return SUCCESS;
		}

		catch (LMSException le) {
			return APPLICATION_ERROR;

		}
	}

	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("GAME_SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueOrgSearch");
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
			// System.out.println("End value"+endValue);
			// System.out.println("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("GAME_SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrgSearch", startValue);
		}
		return SUCCESS;
	}

	public String searchAjaxSale() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session
				.getAttribute("SALE_ClOSE_SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueOrgSearch");
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
			// System.out.println("End value"+endValue);
			// System.out.println("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("SALE_ClOSE_SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrgSearch", startValue);
		}
		return SUCCESS;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setGameId(int gameId) {
		System.out.println("#################game id is" + gameId);
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setPrizeStatusList(List<PrizeStatusBean> prizeStatusList) {
		this.prizeStatusList = prizeStatusList;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setUsersearchResultsAvailable(String usersearchResultsAvailable) {
		this.usersearchResultsAvailable = usersearchResultsAvailable;
	}

}
