package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.skilrock.lms.rest.scratchService.inventoryMgmt.controller.BookActivation;
import com.skilrock.lms.rest.scratchService.orderMgmt.controller.ScratchController;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

public class ScratchControllerTest {

	private ScratchController scratchItem = new ScratchController();
	private ScratchService scratchService = Mockito.mock(ScratchService.class);

	@Test
	public void canCreateUpdateRecievedBookNbrStatusMethod(){
		String requestData="";
		scratchItem.getUpdateRecievedBookNbrStatus(requestData);
	}
	
	@Test
	public void serviceGetsCreated(){
		Assert.assertNotEquals(null, scratchItem.getScracthService());
	}
	
	@Test
	public void returnsErrorCode102ForNonJsonREquest() throws JSONException{
		String requestData="requestData=24005";
		String response = scratchItem.getUpdateRecievedBookNbrStatus(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":102,\"errorMessage\":\"Some Internal Error Occured\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	@Test
	public void parsestoOrderMgmtBeanWithJSON(){
		String requestData="{\"requestId\": 24005}";
		scratchItem.getUpdateRecievedBookNbrStatus(requestData);
	}
	
	@Test
	public void getErrorMessageIfMandatoryParametersAreNotProvidedInRequest() throws JSONException{
		String requestData="{\"requestId\": 24005}";
		String response = scratchItem.getUpdateRecievedBookNbrStatus(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":101,\"errorMessage\":\"Mandatory parameteres are not provided\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	
	@Test
	public void successIfMandatoryParametersAreProvidedInRequest() throws JSONException{
		String requestData = "{\"requestId\": 24004,\"tpUserId\": 26,\"dlNumber\": \"DLA1481511000020\",\"bookList\": [\"101-001001\"]}";
		Mockito.when(scratchService.isBookNumberListValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(scratchService.updateBookListStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		ScratchController scratchItem = new ScratchController(scratchService);
		scratchItem.getUpdateRecievedBookNbrStatus(requestData);
	}
	
	@Test
	public void getResponseCode100IfAllDataIsCorrect() throws JSONException{
		String requestData = "{\"requestId\": 24004,\"tpUserId\": 26,\"dlNumber\": \"DLA1481511000020\",\"bookList\": [\"101-001001\"]}";
		Mockito.when(scratchService.isBookNumberListValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(scratchService.updateBookListStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("SUCCESS");
		ScratchController scratchItem = new ScratchController(scratchService);
		String response = scratchItem.getUpdateRecievedBookNbrStatus(requestData);
		Assert.assertEquals(100, new JSONObject(response).getInt("responseCode"));
	}
	
	
	@Test
	public void getResponseSomeInternalErrorIfAllDataIsCorrectButServiceLayerFails() throws JSONException{
		String requestData = "{\"requestId\": 24004,\"tpUserId\": 26,\"dlNumber\": \"DLA1481511000020\",\"bookList\": [\"101-001001\"]}";
		Mockito.when(scratchService.isBookNumberListValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(scratchService.updateBookListStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		ScratchController scratchItem = new ScratchController(scratchService);
		String response = scratchItem.getUpdateRecievedBookNbrStatus(requestData);
		Assert.assertEquals(102, new JSONObject(response).getInt("errorCode"));
	}
	
	
	@Test
	public void getResponseCode103IfAllDataIsCorrectButBookNbrsAreIncorrect() throws JSONException{
		String requestData = "{\"requestId\": 24004,\"tpUserId\": 26,\"dlNumber\": \"DLA1481511000020\",\"bookList\": [\"10-001001\"]}";
		Mockito.when(scratchService.isBookNumberListValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(false);
		Mockito.when(scratchService.updateBookListStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("SUCCESS");
		ScratchController scratchItem = new ScratchController(scratchService);
		String response = scratchItem.getUpdateRecievedBookNbrStatus(requestData);
		Assert.assertEquals(103, new JSONObject(response).getInt("errorCode"));
	}
	
	
}
