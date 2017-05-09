package com.skilrock.lms.web.instantWin.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.coreEngine.service.ServiceDelegateIWPos;
import com.skilrock.lms.coreEngine.service.instantWin.ServiceNameMethod;

/**
 * This class contains all methods to integrate with InsantWin
 * @author Nikhil K. Bansal
 */
public class InstantWinPCPOS {
	Log logger = LogFactory.getLog(InstantWinPCPOS.class);
	
	private String requestData;
	

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	
	/**
	 * method to get all game data from InsantWin
	 * @author Nikhil K. Bansal
	 */
	public void fetchGameData() {
		PrintWriter out = null;
		JsonObject res = null;
		JSONObject js=null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceNameMethod.ServiceName.PCPOS_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.FETCH_GAME_DATA);
			js =JSONObject.fromObject(requestData);		
			logger.info("Iw Pcpos Game Request Data"+js.toString());
			sReq.setServiceData(js);
			String s = ServiceDelegateIWPos.getInstance().getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Iw Pcpos Game Response Data"+res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			if(res==null){
				res=new JsonObject();
				res.addProperty("responseCode", "501");
				res.addProperty("responseMsg", "Internal System Error!!");
			}
		} finally {
			out.print(res);
			out.flush();
			out.close();
		}

	}
	
	/**
	 * method to purchase ticket of InsantWin
	 * @author Nikhil K. Bansal
	 */
	public void purchaseIWPcPosTicket() {
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		JSONObject js=null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceNameMethod.ServiceName.PCPOS_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.PURCHASE_PCPOS_TICKET);

			js =JSONObject.fromObject(requestData);		
			logger.info("Iw Pcpos Purchase Request Data"+js.toString());
			sReq.setServiceData(js);
			String s = ServiceDelegateIWPos.getInstance().getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Iw Pcpos Purchase Response Data"+res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			if(res==null){
				res=new JsonObject();
				res.addProperty("responseCode", "501");
				res.addProperty("responseMsg", "Internal System Error!!");
			}
		}finally {
			out.print(res);
			out.flush();
			out.close();
		}

	}
	
	/**
	 * method to Pwt Verification
	 * @author Nikhil K. Bansal
	 */
	public void verifyIWTicket() {
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		JSONObject js=null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceNameMethod.ServiceName.PCPOS_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.WIN_VERIFY_TICKET);

			js =JSONObject.fromObject(requestData);		
			logger.info("Iw Pcpos Verify IW Request Data"+js.toString());
			sReq.setServiceData(js);
			String s = ServiceDelegateIWPos.getInstance().getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Iw Pcpos Verify IW Response Data"+res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			if(res==null){
				res=new JsonObject();
				res.addProperty("responseCode", "501");
				res.addProperty("responseMsg", "Internal System Error!!");
			}
		}finally {
			out.print(res);
			out.flush();
			out.close();
		}

	}
	

	/**
	 * method to Pwt payment
	 * @author Nikhil K. Bansal
	 */
	public void payIWPwt() {
		PrintWriter out = null;
		JsonObject res = null;
		HttpServletResponse response = null;
		JSONObject js=null;
		try {
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			out = response.getWriter();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceNameMethod.ServiceName.PCPOS_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.PAY_PAY_TICKET);

			js =JSONObject.fromObject(requestData);		
			logger.info("Iw Pcpos Pay Pwt Request Data"+js.toString());
			sReq.setServiceData(js);
			String s = ServiceDelegateIWPos.getInstance().getResponseString(sReq);
			res = (JsonObject) new JsonParser().parse(s);
			logger.info("Iw Pcpos Pay Pwt Response Data"+res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			if(res==null){
				res=new JsonObject();
				res.addProperty("responseCode", "501");
				res.addProperty("responseMsg", "Internal System Error!!");
			}
		}finally {
			out.print(res);
			out.flush();
			out.close();
		}

	}

}
