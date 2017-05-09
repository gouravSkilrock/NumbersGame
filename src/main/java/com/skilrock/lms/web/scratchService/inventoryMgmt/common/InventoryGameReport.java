package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.InventoryGameReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.InventoryGameReportHelper;

public class InventoryGameReport extends ActionSupport implements
		ServletRequestAware {

	static Log logger = LogFactory.getLog(InventoryGameReport.class);

	private static final long serialVersionUID = 5L;

	private String edit = null;

	private String end = null;
	private String gamename;
	private String gamenumber;
	private String gamestatus;

	private String searchResultsAvailable;

	private HttpServletRequest servletRequest;
	private int start = 0;

	@Override
	public String execute() {
		logger.debug("==========Inventory Game Report called==========");
		return "success";
	}

	public String gameSearchResult() {
		List<InventoryGameReportBean> gameBean = null;
		List<InventoryGameReportBean> inventoryReport = null;

		HttpSession session = getServletRequest().getSession();
		session.setAttribute("invGameResultflag", "flag");
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		String usertype = infoBean.getUserType();
		int orgId = infoBean.getUserOrgId();

		System.out
				.println("---------------------------------\n\n organization id : "
						+ orgId + "\t user Type : " + usertype);

		InventoryGameReportHelper gameReportHelper = new InventoryGameReportHelper();

		// here we get the game details from st_se_game_master table
		gameBean = gameReportHelper.getGameDetail(gamename, gamenumber,
				gamestatus);
		// here we get the complete inventory report according to gameid
		if (gameBean != null) {
			inventoryReport = gameReportHelper.getInventoryGameReport(gameBean);
		}

		String retailerOnline = (String) ServletActionContext
				.getServletContext().getAttribute("RET_ONLINE");

		if ("YES".equalsIgnoreCase(retailerOnline.trim())) {
			session.setAttribute("retailerOnline", retailerOnline);
		}
		if (inventoryReport.size() == 0) {
			inventoryReport = null;
		}
		session.setAttribute("searchResultBO", inventoryReport);

		logger.debug("session values in Action ==== "
				+ session.getAttribute("searchResultBO"));

		if (inventoryReport != null) {
			session.setAttribute("APP_ORDER_LIST5", inventoryReport);
			// / session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();

		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
		}

		return SUCCESS;
	}

	public String getEdit() {
		return edit;
	}

	public String getEnd() {
		return end;
	}

	public String getGamename() {
		return gamename;
	}

	public String getGamenumber() {
		return gamenumber;
	}

	public String getGamestatus() {
		return gamestatus;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public int getStart() {
		return start;
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
		HttpSession session = getServletRequest().getSession();
		List ajaxList = (List) session.getAttribute("APP_ORDER_LIST5");
		List ajaxSearchList = new ArrayList();
		// logger.debug("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			logger.debug("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				logger.debug("i m in first");
				startValue = 0;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				logger.debug("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				logger.debug("i m in Next");
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
			logger.debug("End value" + endValue);
			logger.debug("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("searchResultBO", ajaxSearchList);
			System.out
					.println("================= I Ajax Search ==== search result bo : ===== "
							+ session.getAttribute("searchResultBO"));
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		logger.debug("value of Edit" + edit);
		return SUCCESS;

	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename.trim();
		if ("".equals(gamename.trim())) {
			this.gamename = null;
		}

	}

	public void setGamenumber(String gamenumber) {
		this.gamenumber = gamenumber.trim();
		if ("".equals(gamenumber.trim())) {
			this.gamenumber = null;
		}
	}

	public void setGamestatus(String gamestatus) {
		this.gamestatus = gamestatus.trim();
		if ("ALL".equalsIgnoreCase(gamestatus.trim())) {
			this.gamestatus = null;
		}
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		// TODO Auto-generated method stub
		this.servletRequest = servletRequest;

	}

	public void setStart(int start) {
		this.start = start;
	}

}
