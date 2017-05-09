package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrderRequestIT {
	private ScratchController orederRequest = null;
	
	@Before
	public void setUpDependency() throws Exception {
		orederRequest = new ScratchController();
	}
	
	@Test
	public void orderInventoryWithOrderList() throws JSONException{
		String requestData = "{'requestId':24010,'tpUserId':1125,'orderList':[{'gameId':111,'noOfItems':4},{'gameId':112,'noOfItems':45}]}";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("200", requestObj.get("responseCode").toString());
		Assert.assertEquals("24010", requestObj.get("requestId").toString());
		Assert.assertEquals("Order Created Successfully", requestObj.get("responseMsj").toString().replaceAll("\"", " ").trim());
	}
	
	@Test
	public void orderInventoryWithoutData() throws JSONException{
		String requestData = "{}";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("2002", requestObj.get("responseCode").toString());
	}
	
	@Test
	public void orderInventoryWithEmpty() throws JSONException{
		String requestData = "";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("2021", requestObj.get("responseCode").toString());
		Assert.assertEquals("Invalid Input.", requestObj.get("responseMsj").toString().replaceAll("\"", " ").trim());
	}
}
