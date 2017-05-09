package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.GameInventoryStatusForBOHelper;

public class GameInventoryStatusForBO extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(GameInventoryStatusForBO.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agentName;
	private String agentOrgName;
	private int gameid;

	private String gamename;
	private String gamenumber;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String retName;
	private HttpSession session;
	private String type;
	private int agtId;
	private int retId;

	@Override
	public String execute() {
		logger.info("inside execute");
		session = request.getSession();
		GameInventoryStatusForBOHelper helper = new GameInventoryStatusForBOHelper();
		/*List<String> agentList = helper.getAgentList();
		System.out.println("list " + agentList);*/
		Map<String, String> gameMap = helper.getGameMap();
		logger.info("gameMAp ==== " + gameMap);
		session.setAttribute("boAgentListGame", gameMap);
		//session.setAttribute("boAgentList", agentList);
		// session.setAttribute("boRetList", null);
		return SUCCESS;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	/*
	 * public String boTotalBooks () { session=request.getSession();
	 * System.out.println(" inside boTotalBooks "+gameid+"
	 * "+gamenumber+"-"+gamename); GameInventoryStatusForBOHelper helper=new
	 * GameInventoryStatusForBOHelper(); Map gameMap
	 * =helper.getGameInvetoryTotal(gameid); session.setAttribute("header",
	 * "Total No. of Books Uploaded of "+gamenumber+"-"+gamename);
	 * session.setAttribute("totalBooks", gameMap.get("bookList")); return
	 * SUCCESS; }
	 * 
	 * 
	 * public String boTotalBooksWithBO () { session=request.getSession();
	 * System.out.println(" inside boTotalBooksWithBO "+gameid+"
	 * "+gamenumber+"-"+gamename);
	 * 
	 * return SUCCESS; }
	 * 
	 * public String boTotalActiveBooks () { session=request.getSession();
	 * GameInventoryStatusForBOHelper helper=new
	 * GameInventoryStatusForBOHelper(); Map gameMap
	 * =helper.getBoTotalActiveBooks(gameid); session.setAttribute("header",
	 * "Total No. of Books With BO of "+gamenumber+"-"+gamename);
	 * session.setAttribute("totalBooksWithBo", gameMap); return SUCCESS; }
	 * 
	 * public String boTotalBooksWithRetailer () { session=request.getSession();
	 * GameInventoryStatusForBOHelper helper=new
	 * GameInventoryStatusForBOHelper(); Map gameMap
	 * =helper.getBoTotalBooksWithRetailer(gameid);
	 * session.setAttribute("header", "Total No. of Books With BO of
	 * "+gamenumber+"-"+gamename); session.setAttribute("totalBooksWithBo",
	 * gameMap); return SUCCESS; }
	 * 
	 * 
	 * 
	 * 
	 * public String boTotalBooksAgentWise () throws IOException {
	 * session=request.getSession(); System.out.println("agentName =============
	 * "+agentName); GameInventoryStatusForBOHelper helper=new
	 * GameInventoryStatusForBOHelper(); Map gameMap
	 * =helper.getBoTotalBooksWithAgent(gameid, agentName); System.out.println("
	 * gmae map === "+gameMap); StringBuilder string=new StringBuilder(" ");
	 * session.setAttribute("header", "Total No. of Books With BO of
	 * "+gamenumber+"-"+gamename); session.setAttribute("boTotalBooksWithAgent",
	 * gameMap); return SUCCESS; }
	 * 
	 * 
	 * 
	 * public String boTotalBooksWithAgent () throws IOException {
	 * session=request.getSession(); System.out.println("agentName =============
	 * "+agentName); if(agentName!=null) agentName.replace("amp", "&");
	 * 
	 * GameInventoryStatusForBOHelper helper=new
	 * GameInventoryStatusForBOHelper(); Map gameMap
	 * =helper.getBoTotalBooksWithAgent(gameid, agentName); System.out.println("
	 * gmae map === "+gameMap); StringBuilder string=new StringBuilder(" ");
	 * session.setAttribute("header", "Total No. of Books With BO of
	 * "+gamenumber+"-"+gamename); session.setAttribute("boTotalBooksWithAgent",
	 * gameMap); return SUCCESS; }
	 */

	public int getGameid() {
		return gameid;
	}

	public String getGamename() {
		return gamename;
	}

	public String getGamenumber() {
		return gamenumber;
	}

	public void getInventoryDetailsForBO() throws IOException {
		System.out.println("type = " + type + "\t,agtId = " + agtId
				+ "\t,gameId = " + gameid + "\t,retailer = " + retId);
		String responseStr = null;
		PrintWriter out = response.getWriter();

		GameInventoryStatusForBOHelper helper = new GameInventoryStatusForBOHelper();
		if ("BO".equalsIgnoreCase(type.trim())) {
			responseStr = helper.getGameInvetoryWithBO(gameid);
		} else if ("AGENT".equalsIgnoreCase(type.trim())) {
			responseStr = helper.getBoTotalBooksWithAgent(gameid, agtId);
		} else {
			responseStr = helper.getBoTotalBooksWithRetailer(gameid, retId);
		}

		out.print(responseStr);

	}

	public void getRetailerList() throws Exception {
		PrintWriter out = response.getWriter();
		// StringBuffer retNameDetails = new StringBuffer("");
		GameInventoryStatusForBOHelper helper = new GameInventoryStatusForBOHelper();

		System.out.println("agent orgazition name  ======= "
				+ getAgentOrgName());
		List<String> retList = helper.getRetailerList(agentOrgName);
		// boolean flag=true;
		if (retList.size() < 1) {
			retList = null;
			out.print("NO_RET");
		} else {
			System.out.println(" -- getRetailerList---" + retList.toString());
			out.print(retList.toString());
		}
	}

	public String getRetName() {
		return retName;
	}

	public String getType() {
		return type;
	}

	public void setAgentName(String agentName) {
		System.out.println(" inside setter agentName === " + agentName);
		this.agentName = agentName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void setGamenumber(String gamenumber) {
		this.gamenumber = gamenumber;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAgtId() {
		return agtId;
	}

	public int getRetId() {
		return retId;
	}

	public void setAgtId(int agtId) {
		this.agtId = agtId;
	}

	public void setRetId(int retId) {
		this.retId = retId;
	}
}
