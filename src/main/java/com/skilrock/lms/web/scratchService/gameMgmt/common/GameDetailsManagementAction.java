package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.GameDetailsManagementHelper;

public class GameDetailsManagementAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(GameDetailsManagementAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> gameList;
	private String gameName;
	private HttpServletRequest request;

	@Override
	public String execute() {
		logger.debug("GameDetailsManagementAction execute ");
		GameDetailsManagementHelper helper = new GameDetailsManagementHelper();
		gameList = helper.getGameList();
		// request.getSession().setAttribute(arg0, arg1)
		return SUCCESS;
	}

	public String getGameDetails() {
		logger.debug("getGameDetails");
		GameDetailsManagementHelper helper = new GameDetailsManagementHelper();
		Map<String, Object> gameDetailsMap = new TreeMap<String, Object>();
		gameDetailsMap = helper.getGameDetails(gameName);
		request.getSession().setAttribute("gameDetailsMap", gameDetailsMap);

		return SUCCESS;
	}

	public Map<String, String> getGameList() {
		return gameList;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameList(Map<String, String> gameList) {
		this.gameList = gameList;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

}
