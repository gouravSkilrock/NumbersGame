package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GenerateRetailerOrderHelper;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.services.bean.DaoBean;


public class OrderRequestTest {
	private ScratchController orederRequest;
	private GenerateRetailerOrderHelper orderHelper;
	private ScratchService scratchService;
	
	@Before
	public void setUpDependency() throws Exception {
		mockingGenerateRetailerOrderHelper();
        mockingScratchService();
		orederRequest = new ScratchController(orderHelper, scratchService);
	}

	private void mockingGenerateRetailerOrderHelper() throws LMSException {
		orderHelper = Mockito.mock(GenerateRetailerOrderHelper.class);
		Mockito.when(orderHelper.generateOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList())).thenReturn(2);
	}

	private void mockingScratchService() throws Exception {
		DaoBean value = new DaoBean();
		scratchService = Mockito.mock(ScratchService.class);
		Mockito.when(scratchService.getAgentOrganizationId(Mockito.anyString())).thenReturn(Mockito.anyInt());
		Mockito.when(scratchService.getRetailerData("2")).thenReturn(value);
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
	public void orderInventoryWithoutOrderList() throws JSONException{
		String requestData = "{}";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("2021", requestObj.get("responseCode").toString());
	}
	
	@Test
	public void orderInventoryWithoutTpUserId() throws JSONException{
		String requestData = "{'requestId':24010,'orderList':[{'gameId':111,'noOfItems':4},{'gameId':112,'noOfItems':45}]}";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("2021", requestObj.get("responseCode").toString());
		Assert.assertEquals("TpUser Id Is Not Present", requestObj.get("responseMsj").toString().replaceAll("\"", " ").trim());
	}
	
	@Test
	public void orderInventoryWithNothing() throws JSONException{
		String requestData = "";
		String responseData = orederRequest.orderInventory(requestData);
		JsonObject requestObj = new JsonParser().parse(responseData).getAsJsonObject();
		Assert.assertEquals("2021", requestObj.get("responseCode").toString());
		Assert.assertEquals("Invalid Input.", requestObj.get("responseMsj").toString().replaceAll("\"", " ").trim());
	}
}

