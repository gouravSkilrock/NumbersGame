package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.LooseSaleAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.LooseSaleBOHelper;

public class LooseSaleAgentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String userData;
	private String gameData;
	private String[] gameName;
	private String[] NumTickets;
	private String[] ticketAmt;
	private String[] ticketComm;
	private String userName;
	private int retOrgId;
	private int gameId;
	public String execute()
	{
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		LooseSaleAgentHelper helper = new LooseSaleAgentHelper();
		userData = helper.getRetailerList(userInfoBean).toString();
		gameData = helper.getGameList();
		return SUCCESS;
	}
	public String dispatchOrder(){
		HttpSession session = getRequest().getSession();
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			LooseSaleAgentHelper agentHelper = new LooseSaleAgentHelper();
				int retailerOrgId = Integer.parseInt(userName.split(":")[0]);
				agentHelper.looseSaleForAgent(gameName,NumTickets,ticketAmt,ticketComm,retailerOrgId,userInfoBean,rootPath);		
		return SUCCESS;
	}
	public void getCommission()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			LooseSaleAgentHelper helper = new LooseSaleAgentHelper();
			String commDetails = helper.getCommDetails(retOrgId,gameId);
			pw.print(commDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getRetOrgId() {
		return retOrgId;
	}
	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String[] getTicketAmt() {
		return ticketAmt;
	}
	public void setTicketAmt(String[] ticketAmt) {
		this.ticketAmt = ticketAmt;
	}
	public String[] getTicketComm() {
		return ticketComm;
	}
	public void setTicketComm(String[] ticketComm) {
		this.ticketComm = ticketComm;
	}
	public String[] getNumTickets() {
		return NumTickets;
	}
	public void setNumTickets(String[] numTickets) {
		NumTickets = numTickets;
	}
	
	public String[] getGameName() {
		return gameName;
	}
	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}
	public String getGameData() {
		return gameData;
	}
	public void setGameData(String gameData) {
		this.gameData = gameData;
	}
	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
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

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public void setServletResponse(HttpServletResponse resp) {
		response = resp;

	}

}