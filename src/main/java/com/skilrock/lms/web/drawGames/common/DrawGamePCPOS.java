package com.skilrock.lms.web.drawGames.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;

public class DrawGamePCPOS {
	Log logger = LogFactory.getLog(DrawGamePCPOS.class);
	private String json;
		
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void fetchDrawGameData() {
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.TP_DATA_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_DATA);
			JSONObject js = new JSONObject();
			js.put("merchantCode", "LMS");
			sReq.setServiceData(js);
			ServiceDelegate delegate = ServiceDelegate.getInstance();
			String s = delegate.getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Fetch Draw Game Data Response:"+res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(res);
			out.flush();
			out.close();
		}

	}
	
	public void fetchDrawGameResultData(){
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.TP_DATA_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_RESULT_DATA);
			JSONObject js =JSONObject.fromObject(json);		
			sReq.setServiceData(js);
			ServiceDelegate delegate = ServiceDelegate.getInstance();
			String s = delegate.getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Fetch Draw Game Result Response:"+res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(res);
			out.flush();
			out.close();
		}
	}
	
	public void fetchGameWiseRNG(){
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		try {
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			String gameCode = requestData.getString("gameCode");
			String noPicked = requestData.getString("noPicked");
			String noOfLines = requestData.getString("noOfLines");
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.TP_DATA_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_GAMEWISE_RANDOM_NUMBER);
			JSONObject js = new JSONObject();
			js.put("gameCode", gameCode);
			js.put("noPicked",noPicked);
			js.put("noOfLines",noOfLines);
			sReq.setServiceData(js);
			ServiceDelegate delegate = ServiceDelegate.getInstance();
			String s = delegate.getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Fetch RNG Data Response:"+res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(res);
			out.flush();
			out.close();
		}
	}
	
	public void getUserActionList(){
		HttpServletResponse response = null;		
		PrintWriter out = null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");			
			response.setContentType("application/json");
			out = response.getWriter();
			ArrayList<String> userActionList = new ArrayList<String>();
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			userActionList = (ArrayList<String>) session.getAttribute("ACTION_LIST");
			logger.debug("userActionList: " + userActionList);
			String res = new Gson().toJson(userActionList);
			logger.info("List of allowed Action:"+res);
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
