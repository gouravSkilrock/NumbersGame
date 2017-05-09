package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.MTNWinningTransferControllerImpl;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.PendingWinningTransferDataBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class MTNWinningTransferAction extends BaseAction{
	public MTNWinningTransferAction() {
		super(MTNWinningTransferAction.class);
	}

	/**
	 * 
	 */
	
	static Log logger = LogFactory.getLog(MTNWinningTransferAction.class);
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String gameNumber ;
	private String drawId ;
	private String startDate ;
	private String endDate ;
	private String message ;
	private List<PendingWinningTransferDataBean> pendingData ;
	
	public String execute()
	{
		HttpSession session = getRequest().getSession() ;
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchActiveGameDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS ;
	}
	
	public String getPendingWinningDraw()
	{
		logger.info("entry test") ;
		logger.info("game no : " + gameNumber + ", start date : " + startDate + ", end date : " + endDate) ;
		
		try{
		pendingData = MTNWinningTransferControllerImpl.getInstance().fetchPendingData(gameNumber, startDate+" 00:00:00", endDate+" 23:59:59");
		} catch (LMSException e) {
			message = e.getErrorMessage();
		}
		return SUCCESS ;
	}
	
	public synchronized String pushPendingWinning()
	{
		logger.info("gameId : " + gameNumber + " and drawId : " + drawId) ;
		MTNWinningTransferControllerImpl.getInstance().pushPendingWinning(gameNumber, drawId) ;
		
		return SUCCESS ;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response ;
		
	}





	public String getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<PendingWinningTransferDataBean> getPendingData() {
		return pendingData;
	}

	public void setPendingData(List<PendingWinningTransferDataBean> pendingData) {
		this.pendingData = pendingData;
	}

	public String getDrawId() {
		return drawId;
	}

	public void setDrawId(String drawId) {
		this.drawId = drawId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
