package com.skilrock.lms.embedded.drawGames.common;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.MobileDrawMgmtHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class MobileDrawMgmtAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private HttpServletResponse response;
	private HttpServletRequest servletRequest;
	private String gameNameDev=null;
		
	public void fetchActiveGames() throws Exception{
		HttpSession session = servletRequest.getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		/*List drawNoNameList = (List) sc.getContext("/DrawGameWeb").getAttribute("gameNameList");*/
		List drawNoNameList = (List) sc.getAttribute("gameNameList");
		String gameNameDev=null;
		String gameName=null;
		int gameNo=0;
		StringBuilder str = new StringBuilder("");
		str.append("<rmi><?xml version=\"1.0\" encoding=\"UTF-8\"?><gameNames>");
		System.out.println("gamessssssssssssss:"+drawNoNameList);
		for(int i=0;i<drawNoNameList.size();i++){
			gameNo=Integer.parseInt(((String)drawNoNameList.get(i)).split("-")[0]);
			gameNameDev=Util.getGameName(gameNo);
			gameName=Util.getGameDisplayName(gameNo);
			str.append("<gameInfo>");
			str.append("<gameDispName>"+gameName+"</gameDispName>");
			str.append("<gameNameDev>"+gameNameDev+"</gameNameDev>");
			str.append("</gameInfo>");
		}
		str.append("</gameNames></rmi>");
		
		System.out.println("active games data sent:"+str);
		
		response.getOutputStream().write(str.toString().getBytes());
	}
	
	public void fetchDrawGamesData() throws Exception{
		System.out.println("URL from Mobile called for game "+gameNameDev);
		String str = MobileDrawMgmtHelper.fetchDrawDetails(gameNameDev);
		if(str!=null)
		response.getOutputStream().write(str.toString().getBytes());
		else
			response.getOutputStream().write("Error....".getBytes());
	}
	
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}



	public String getGameNameDev() {
		return gameNameDev;
	}



	public void setGameNameDev(String gameNameDev) {
		this.gameNameDev = gameNameDev;
	}
	
}
