package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OpenGameBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.CalculateUnclaimedHelper;

/**
 * This class provides methods to calculate unclaimed pwt , Govt Commission for
 * the terminated game
 * 
 * @author Arun Upadhyay
 * 
 */
public class CalculateUnclaimedAction extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";
	static Log logger = LogFactory.getLog(CalculateUnclaimedAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;

	/**
	 * This method calculate Unclaimed PWT and Govt Commission for a game
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String calculate() throws Exception {

		HttpSession session = request.getSession();
		List<OpenGameBean> gameData = new ArrayList((List) session
				.getAttribute("GAME_SEARCH_RESULTS"));
		logger.debug(gameData);
		int gameId = gameData.get(0).getGameId();
		int gameNbr = gameData.get(0).getGameNbr();
		String gameName = gameData.get(0).getGameName();
		session.setAttribute("GAME_NBR", gameNbr);
		session.setAttribute("GAME_NAME", gameName);

		logger.debug(" game id is      " + gameId);

		CalculateUnclaimedHelper calculateUnclaimed = new CalculateUnclaimedHelper();
		try {
			calculateUnclaimed.calculateUnclaimed(gameId);
			session.removeAttribute("GAME_SEARCH_RESULTS");
			logger.debug("session attribute removed");
		} catch (LMSException le) {
			return APPLICATION_ERROR;
		}

		return SUCCESS;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}