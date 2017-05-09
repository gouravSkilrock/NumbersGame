package com.skilrock.lms.scratchService.orderMgmt.controllerImpl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.scratchService.orderMgmt.serviceImpl.GameListServiceImpl;

import net.sf.json.JSONObject;

@Path("/orderManagement")
public class OrderManagementControllerImpl {
	
	GameListServiceImpl gameListServiceImpl=null;
	
	public OrderManagementControllerImpl(){
		gameListServiceImpl = new GameListServiceImpl();
	}
	
	public OrderManagementControllerImpl(GameListServiceImpl gameListServiceImpl){
		 this.gameListServiceImpl=new GameListServiceImpl();
	}
	
	@Path("/getGameList")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGameList (String requestData){
		JSONObject resObj = new JSONObject();
		JsonObject reqObj = null;
		int tpUserId=0;
		int requestId=0;
		GameListResponse gameListResponse = null;
		GameListResponseData gameListResponseData=null;
		List<GameListResponseData> gameList =new ArrayList<GameListResponseData>();
		
		try{
			reqObj = new JsonObject();
			gameListResponse=new GameListResponse();
			
			if(requestData == null || requestData.trim().length() < 1 ){
				throw new ScratchException(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			reqObj=new JsonParser().parse(requestData).getAsJsonObject();
			requestId=reqObj.get("requestId").getAsInt();
			tpUserId=reqObj.get("tpUserId").getAsInt();
			if(reqObj.get("tpUserId").getAsString()==null || reqObj.get("requestId").getAsString()==null ){
				throw new ScratchException(ScratchErrors.NO_REQUEST_ID_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_ID_PROVIDED_ERROR_MESSAGE);
			}
			gameList=gameListServiceImpl.getGameListServiceImpl();
			gameListResponse.setRequestId(requestId);
			gameListResponse.setResponseCode(100);
			gameListResponse.setResponseData(gameList);
			
			resObj.put("requestId", gameListResponse.getRequestId());
			resObj.put("responseCode", gameListResponse.getResponseCode());
			resObj.put("responseData",gameListResponse.getResponseData());
			
		}catch(Exception e){
			resObj.put("requestId", gameListResponse.getRequestId());
			resObj.put("responseCode", ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseData",ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			e.printStackTrace();
			
		}
		return Response.ok().entity(resObj).build();
	}
	
}
