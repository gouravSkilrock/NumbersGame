package com.skilrock.lms.embedded.drawGames.common;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.RaffleHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class RaffleAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	static Log logger = LogFactory.getLog(RaffleAction.class);
	private static final long serialVersionUID = 1L;

	TreeMap drawGameData;
	private String gameName;
	private HttpServletResponse response;
	private HttpServletRequest servletRequest;

	public void fetchRaffleWinningResult() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		RaffleHelper helper = new RaffleHelper();
		setDrawGameData(helper.getDrawGameData());
		sc.setAttribute("GAME_DATA", drawGameData);
		Set keySet = drawGameData.keySet();
		Iterator iter = keySet.iterator();
		List list = null;
		List<String> drawResultList = null;
		String[] drawResult = null;
		int drawResultCount = 0;
		StringBuilder sBuilder = new StringBuilder("");
		list = (List) drawGameData.get(Util.getGameMap().get(gameName).getGameNo());
		if (list == null) {
			System.out.println(" game name is " + gameName + " not found");
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.RESULT_GAME_NOT_AVAILABLE).getBytes());
			return;
		}

		drawResultList = (List<String>) list.get(1);
		if (drawResultList != null) {
			drawResultCount = drawResultList.size() > 5 ? 5 : drawResultList.size();
		}
		if (drawResultList != null && drawResultCount == 0) {
			System.out.println("No Draw Available");
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE+"ErrorCode:"+EmbeddedErrors.RESULT_DRAW_NOT_AVAILABLE_ERROR_CODE+"|").getBytes());
			return;
		}

		String raffTktType = (String)sc.getAttribute("raffle_ticket_type");
		
		for (int i = 0; i < drawResultCount; i++) {
			drawResult = drawResultList.get(i).split("=");
			if(!"NA".equalsIgnoreCase(raffTktType) && ("RaffleGame".equalsIgnoreCase(gameName) || "RaffleGame1".equalsIgnoreCase(gameName))){
				if (drawResult[1] != null && !"NULL".equalsIgnoreCase(drawResult[1]) && !"0".equalsIgnoreCase(drawResult[1])) {
					String[] drawRsltArr = drawResult[1].split(",");
					StringBuilder tmpRslt = new StringBuilder("");
					if("ORIGINAL".equalsIgnoreCase(raffTktType)){
						for(int k=0; k<drawRsltArr.length; k++){
							drawRsltArr[k] = drawRsltArr[k].substring(0, drawRsltArr[k].length() - 4) + "XXXX,";
							tmpRslt.append(drawRsltArr[k]);
						}
					} else if("REFERENCE".equalsIgnoreCase(raffTktType)){
						RaffleHelper raffHelper = new RaffleHelper();
						for(int k=0; k<drawRsltArr.length; k++){
							drawRsltArr[k] = raffHelper.swapRaffleResult(drawRsltArr[k]);
							tmpRslt.append(drawRsltArr[k] + ",");
						}
					}
					if(tmpRslt.length() > 0){
						tmpRslt.deleteCharAt(tmpRslt.length() - 1);
					}
					drawResult[1] = tmpRslt.toString();
				} else {
					drawResult[1] = "No Sale";	// No Tickets sold for this draw...
				}
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(drawResult[0]));

			sBuilder.append("DrawTime:"
					+ cal.get(Calendar.YEAR)
					+ "-"
					+ (cal.get(Calendar.MONTH) + 1 > 9 ? cal.get(Calendar.MONTH) + 1 : "0" + (cal.get(Calendar.MONTH) + 1))
					+ "-"
					+ (cal.get(Calendar.DAY_OF_MONTH) > 9 ? cal.get(Calendar.DAY_OF_MONTH) : "0" + cal.get(Calendar.DAY_OF_MONTH))
					+ " "
					+ (cal.get(Calendar.HOUR_OF_DAY) > 9 ? cal.get(Calendar.HOUR_OF_DAY) : "0" + cal.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ (cal.get(Calendar.MINUTE) > 9 ? cal.get(Calendar.MINUTE) : "0" + cal.get(Calendar.MINUTE))
					+ ":"
					+ (cal.get(Calendar.SECOND) > 9 ? cal.get(Calendar.SECOND) : "0" + cal.get(Calendar.SECOND)) + ",Sign:"
					+ drawResult[1] + "|");

		}
		logger.debug(" Draw result for fortune is " + sBuilder.toString());
		response.getOutputStream().write(sBuilder.toString().getBytes());
	}

	public TreeMap getDrawGameData() {
		return drawGameData;
	}

	public String getGameName() {
		return gameName;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public void setDrawGameData(TreeMap drawGameData) {
		this.drawGameData = drawGameData;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}