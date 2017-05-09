package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.LooseSaleBOHelper;

public class LooseSaleBOAction extends ActionSupport implements
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
	private int agtOrgId;
	private int gameId;
	
	public String execute()
	{
		LooseSaleBOHelper helper = new LooseSaleBOHelper();
		userData = helper.getAgentList().toString();
		gameData = helper.getGameList();
		return SUCCESS;
	}
	public String dispatchOrder(){
			HttpSession session = getRequest().getSession();
			
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			LooseSaleBOHelper boHelper = new LooseSaleBOHelper();
				int agentOrgId = Integer.parseInt(userName.split(":")[0]);
				boHelper.looseSaleForBo(gameName,NumTickets,ticketAmt,ticketComm,agentOrgId,userInfoBean,rootPath);
		
		return SUCCESS;
	}
	
	public void getCommission()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			LooseSaleBOHelper helper = new LooseSaleBOHelper();
			String commDetails = helper.getCommDetails(agtOrgId,gameId);
			pw.print(commDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public int getAgtOrgId() {
		return agtOrgId;
	}
	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
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