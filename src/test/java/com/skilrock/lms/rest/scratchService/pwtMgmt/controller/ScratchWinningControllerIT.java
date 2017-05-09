package com.skilrock.lms.rest.scratchService.pwtMgmt.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl.ScratchServiceImpl;
import com.skilrock.lms.rest.scratchService.pwtMgmt.serviceImpl.ScratchWinningServiceImpl;

public class ScratchWinningControllerIT {

	private static final String NON_WIN_TICKET_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323-001001-08\",\"virnNumber\": \"32303233232172\"}";
	private static final String LOW_WIN_TICKET_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323-001001-06\",\"virnNumber\": \"32318223914873\"}";
	private static final String CLAIMED_TICKET_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323-001001-06\",\"virnNumber\": \"32318223914873\"}";
	private static final String HIGH_PRIZE_TICKET_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323-001001-02\",\"virnNumber\": \"32313231107192\"}";
	private static final String INVALID_TICKET_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323001001-08\",\"virnNumber\": \"32303233232172\"}";
	private static final String INVALID_VIRN_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"10987\",\"ticketNumber\": \"323-001001-08\",\"virnNumber\": \"3230323232172\"}";
	private static final String INVALID_USER_REQUEST = "{\"requestId\": \"24008\",\"tpUserId\": \"6584987\",\"ticketNumber\": \"323-001001-08\",\"virnNumber\": \"32303233232172\"}";
	ScratchService scratchService = new ScratchServiceImpl();
	ScratchWinningServiceImpl scratchWinningServiceImpl = new ScratchWinningServiceImpl();
	ScratchWinningController scratchWinningController = new ScratchWinningController(scratchService,scratchWinningServiceImpl);
	Map<String,String> lmsMap = null;
	
	@Before
	public void setTestCaseData(){
		lmsMap = new HashMap<String,String>();
		lmsMap.put("HIGH_PRIZE_CRITERIA", "amt");
		lmsMap.put("HIGH_PRIZE_AMT", "1000.00");
		Utility.setLmsPropertyMap(lmsMap);
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsWinStatusAsNonWin(){
		String response = scratchWinningController.verifyAndPayWinning(NON_WIN_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("NON_WIN", actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsWinStatusAsWin(){
		String response = scratchWinningController.verifyAndPayWinning(LOW_WIN_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("WIN", actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsWinStatusAsClaimed(){
		String response = scratchWinningController.verifyAndPayWinning(CLAIMED_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("CLAIMED", actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsWinStatusAsHighPrize(){
		String response = scratchWinningController.verifyAndPayWinning(HIGH_PRIZE_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("HIGH_PRIZE", actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsResponseMessageAsInvalidTicket(){
		String response = scratchWinningController.verifyAndPayWinning(INVALID_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid Ticket No", actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsResponseMessageAsInvalidVirnNumber(){
		String response = scratchWinningController.verifyAndPayWinning(INVALID_VIRN_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid virn number", actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsResponseMessageAsInvalidUser(){
		String response = scratchWinningController.verifyAndPayWinning(INVALID_USER_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid User", actualJson.get("responseMsg").getAsString());
	}
	
	//Ticket Verification 
	
	@Test
	public void verifyWinning_ReturnsWinStatusAsNonWin(){
		String response = scratchWinningController.verifyWinning(NON_WIN_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("NON_WIN", actualJson.get("responseData").getAsJsonObject().get("status").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsWinStatusAsWin(){
		String response = scratchWinningController.verifyWinning(LOW_WIN_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("WIN", actualJson.get("responseData").getAsJsonObject().get("status").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsWinStatusAsClaimed(){
		String response = scratchWinningController.verifyWinning(CLAIMED_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("CLAIMED", actualJson.get("responseData").getAsJsonObject().get("status").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsWinStatusAsHighPrize(){
		String response = scratchWinningController.verifyWinning(HIGH_PRIZE_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("HIGH_PRIZE", actualJson.get("responseData").getAsJsonObject().get("status").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsResponseMessageAsInvalidTicket(){
		String response = scratchWinningController.verifyWinning(INVALID_TICKET_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid Ticket No", actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsResponseMessageAsInvalidVirnNumber(){
		String response = scratchWinningController.verifyWinning(INVALID_VIRN_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid virn number", actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsResponseMessageAsInvalidUser(){
		String response = scratchWinningController.verifyWinning(INVALID_USER_REQUEST);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid User", actualJson.get("responseMsg").getAsString());
	}
	
}
