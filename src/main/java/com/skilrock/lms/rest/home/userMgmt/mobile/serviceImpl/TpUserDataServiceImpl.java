package com.skilrock.lms.rest.home.userMgmt.mobile.serviceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.userMgmt.controllerImpl.LmsCityDataControllerImpl;
import com.skilrock.lms.userMgmt.controllerImpl.LmsStateDateControllerImpl;
import com.skilrock.lms.userMgmt.controllerImpl.LmsUserDataControllerImpl;
import com.skilrock.lms.userMgmt.javaBeans.LmsCityDataBean;
import com.skilrock.lms.userMgmt.javaBeans.LmsStateDataBean;
import com.skilrock.lms.userMgmt.javaBeans.LmsUserDataBean;

@Path("/home/dataMgmt")
public class TpUserDataServiceImpl {
	
	
	@Path("/getStateData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStateData(String requestData){
		
		List<LmsStateDataBean> stateDataList = null;
		try{
			stateDataList = LmsStateDateControllerImpl.getSingleInstance().getLmsStateData();
		}catch (GenericException e) {
			e.printStackTrace();
			return Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}
		return Response.ok().entity(stateDataList).build();
	}
	
	@Path("/getCityData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLmsCityData(String requestData){
		
		List<LmsCityDataBean> cityDataList = null;
		try{
			JsonParser parser = new JsonParser();
			JsonObject requestObj = (JsonObject) parser.parse(requestData);
			
			
			cityDataList = LmsCityDataControllerImpl.getSingleInstance().getLmsCityData(requestObj.get("stateCode").getAsString());
			
		}catch (GenericException e) {
			e.printStackTrace();
			return Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}
		return Response.ok().entity(cityDataList).build();
	}
	
	@Path("/getUserData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLmsUserData(String requestData){
		
		List<LmsUserDataBean> userList = null;
		try{
			JsonParser parser = new JsonParser();
			JsonObject requestObj = (JsonObject) parser.parse(requestData);
			
			LmsUserDataControllerImpl userDataControllerImpl = LmsUserDataControllerImpl.getSingleInstance();
			userList = userDataControllerImpl
					.getUserInfoData(requestObj.get("cityCode").getAsString(),
							requestObj.get("userType").getAsString());
				
		}catch (GenericException e) {
			e.printStackTrace();
			return Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}
		return Response.ok().entity(userList).build();
	}
	@Path("/fetchNearByUserData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * This Method Fetch Lms Outlets(Organizations) List according to two types 
	 * 1. On the basis of City and State Code 
	 * 2. On the basis of Latitude Longitude Of Player 
	 */
	public Response fetchNearByUserData(String requestData){
		
		List<LmsUserDataBean> userList = null;
		try{
			JsonParser parser = new JsonParser();
			JsonObject requestObj = (JsonObject) parser.parse(requestData);
			LmsUserDataControllerImpl userDataControllerImpl = LmsUserDataControllerImpl.getSingleInstance();
			if(requestObj.get("isCitySearch").getAsBoolean()){
				userList=userDataControllerImpl.getUserData(requestObj.get("stateCode").getAsString(),requestObj.get("cityCode").getAsString()); 
			}else{
				userList = userDataControllerImpl.getNearByUserInfoData(requestObj.get("lat").getAsString(),requestObj.get("lng").getAsString());
			
			}
			
		
		}catch (GenericException e) {
			e.printStackTrace();
			return Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}
		return Response.ok().entity(userList).build();
	}
}
