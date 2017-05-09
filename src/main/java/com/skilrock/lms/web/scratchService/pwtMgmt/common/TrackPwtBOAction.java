package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.InvTransitionBean;
import com.skilrock.lms.beans.InvTransitionWarehouseWiseBean;
import com.skilrock.lms.beans.TrackTicketBean;
import com.skilrock.lms.beans.TrackVirnBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookFlowHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.TrackPwtBOHelper;

public class TrackPwtBOAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(TrackPwtBOAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bookNumber;
	private boolean bookValidity = false;
	private int gameId;
	// private int gameNbr;
	private String gameIdNbrName;
	HttpServletRequest request;
	HttpServletResponse response;
	private String ticketNbr;

	TrackTicketBean trackTicketBean;

	TrackVirnBean trackVirnBean;
	private List<InvTransitionBean> transitionList;
	private String virnNbr;
	private InvTransitionWarehouseWiseBean invTransitionWarehouseWiseBean;
	
	public InvTransitionWarehouseWiseBean getInvTransitionWarehouseWiseBean() {
		return invTransitionWarehouseWiseBean;
	}

	public void setInvTransitionWarehouseWiseBean(
			InvTransitionWarehouseWiseBean invTransitionWarehouseWiseBean) {
		this.invTransitionWarehouseWiseBean = invTransitionWarehouseWiseBean;
	}

	public String getBookNumber() {
		return bookNumber;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameIdNbrName() {
		return gameIdNbrName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getTicketFlow() throws LMSException {
		BookFlowHelper bookHelper = new BookFlowHelper();
		Map bookFlowDetailMap = bookHelper.getBookFlow(gameId, bookNumber);
		bookValidity = (Boolean) bookFlowDetailMap.get("bookValidity");
		transitionList = (List<InvTransitionBean>) bookFlowDetailMap
				.get("transitionList");
		return SUCCESS;
	}
	
	public String getTicketFlowNew() throws LMSException {
		BookFlowHelper bookHelper = new BookFlowHelper();
		invTransitionWarehouseWiseBean = bookHelper.getBookFlowNew(gameId, bookNumber);
		return SUCCESS;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public TrackTicketBean getTrackTicketBean() {
		return trackTicketBean;
	}

	public TrackVirnBean getTrackVirnBean() {
		return trackVirnBean;
	}

	public List<InvTransitionBean> getTransitionList() {
		return transitionList;
	}

	public String getVirnNbr() {
		return virnNbr;
	}

	public boolean isBookValidity() {
		return bookValidity;
	}

	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setBookValidity(boolean bookValidity) {
		this.bookValidity = bookValidity;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameIdNbrName(String gameIdNbrName) {
		this.gameIdNbrName = gameIdNbrName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setTrackTicketBean(TrackTicketBean trackTicketBean) {
		this.trackTicketBean = trackTicketBean;
	}

	public void setTrackVirnBean(TrackVirnBean trackVirnBean) {
		this.trackVirnBean = trackVirnBean;
	}

	public void setTransitionList(List<InvTransitionBean> transitionList) {
		this.transitionList = transitionList;
	}

	public void setVirnNbr(String virnNbr) {
		this.virnNbr = virnNbr;
	}

	public String trackTicketNVirn() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		TrackPwtBOHelper trackHelper = new TrackPwtBOHelper();
		int gameId = Integer.parseInt(gameIdNbrName.split("-")[0]);
		int gameNbr = Integer.parseInt(gameIdNbrName.split("-")[1]);
		String gameName = gameIdNbrName.split("-")[2];
		List ticketNVirnBeanList = trackHelper.trackTicketNVirn(ticketNbr,
				virnNbr, gameNbr, gameId, gameName, userBean.getOrgName());
		trackTicketBean = (TrackTicketBean) ticketNVirnBeanList.get(0);
		trackVirnBean = (TrackVirnBean) ticketNVirnBeanList.get(1);
		return SUCCESS;
	}

}