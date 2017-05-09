package com.skilrock.lms.rest.scratchService.pwtMgmt.controller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl.ScratchServiceImpl;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchWinningDataBean;
import com.skilrock.lms.rest.scratchService.pwtMgmt.serviceImpl.ScratchWinningServiceImpl;
import com.skilrock.lms.rest.services.bean.ScratchWinningPaymentRequest;

public class ScratchWinningControllerTest {
	
	private static final String SUCCESS_RESPONSE_WITH_UNCLAIMED_WINNING = "{\"responseCode\":0,\"responseMsg\":\"SUCCESS\",\"responseData\":{\"status\":\"UNCLAIMED\",\"winningAmount\":10.0}}";
	private static final String STATUS_UNCLAIMED_WITH_WINNING_AMOUNT = "{\"status\":\"UNCLAIMED\",\"winningAmount\":10.0}";
	ScratchService scratchService = Mockito.mock(ScratchServiceImpl.class);
	ScratchWinningServiceImpl scratchWinningServiceImpl = Mockito.mock(ScratchWinningServiceImpl.class);
	ScratchWinningController scratchWinningController = new ScratchWinningController(scratchService,scratchWinningServiceImpl);

	
	//Test Case FOr Ticket Verification Starts
	
	@Test
	public void verifyWinning_ReturnsErrorMessageSomeInternalErrorForInvalidJson(){
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenEmptyJSON(){
		String response = scratchWinningController.verifyWinning("");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenTktNoNotProvided(){
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008,\"tpUserId\":1001,\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.TICKET_NUMBER_NOT_PROVIDED_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenVirnNoNotProvided(){
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008,\"tpUserId\":1001,\"ticketNumber\":\"111-001001\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.VIRN_NUMBER_NOT_PROVIDED_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenRequestIdNotProvided(){
		String response = scratchWinningController.verifyWinning("{\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.REQUEST_ID_NOT_PROVIDED_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenTPUserIdNotProvided(){
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.USER_ID_NOT_PROVIDED_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyWinning_ReturnsSuccessWhenTicketNumberHasWinningWithUnclaimedStatus() throws JsonSyntaxException, LMSException{
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(new Gson().fromJson(STATUS_UNCLAIMED_WITH_WINNING_AMOUNT, ScratchWinningDataBean.class));
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(new UserInfoBean());
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008,\"tpUserId\":555,\"ticketNumber\":\"555-001001-03\",\"virnNumber\":\"2224424776\"}");
		Assert.assertEquals(SUCCESS_RESPONSE_WITH_UNCLAIMED_WINNING,response);
	}
	
	@Test
	public void verifyWinning_ReturnsErrorMessageWhenUserNotExist() throws JsonSyntaxException, LMSException{
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(null);
		String response = scratchWinningController.verifyWinning("{\"requestId\":24008,\"tpUserId\":555,\"ticketNumber\":\"555-001001-03\",\"virnNumber\":\"2224424776\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(ScratchErrors.INVALID_USER_ERROR_MESSAGE,actualJson.get("responseMsg").getAsString());
	}
	
	//Test Case FOr Ticket Verification Ends
	
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageSomeInternalErrorForInvalidJson(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Some Internal Error !",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageSomeInternalErrorForEmptyString(){
		String response = scratchWinningController.verifyAndPayWinning("");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("No Request Data Provided !",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageSomeInternalErrorForNullInput(){
		String response = scratchWinningController.verifyAndPayWinning(null);
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("No Request Data Provided !",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_Returns2002CodeForInvalidJson(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(2002,actualJson.get("responseCode").getAsInt());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageRequestIdNotProvidedIfRequestIdNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Please provide requestId",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageRequestIdCannotBeEmptyIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"\",\"tpUserId\":\"1001\",\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("RequestId cannot be empty",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_Returns2021CodeIfRequestIdNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"tpUserId\":\"1001\",\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(2021,actualJson.get("responseCode").getAsInt());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageTpUserIdNotProvidedIfTpUserIdNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Please provide TP UserId",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageTpUserIdCannotBeEmptyIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":\"\",\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("TpUserId cannot be empty",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_Returns2021CodeIfTpUserIdNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(2021,actualJson.get("responseCode").getAsInt());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageTicketNumberNotProvidedIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Please provide Ticket Number",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageTicketNumberCannotBeEmptyIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"ticketNumber\":\"\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Ticket Number cannot be empty",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_Returns2021CodeIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(2021,actualJson.get("responseCode").getAsInt());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageVIRNNumberNotProvidedIfVIRNNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"ticketNumber\":\"111-001001\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Please provide VIRN Number",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsMessageVIRNNumberCannotBeEmptyIfTicketNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"ticketNumber\":\"111-001001\",\"virnNumber\":\"\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("VIRN Number cannot be empty",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_Returns2021CodeIfVIRNNumberNotSentInRequest(){
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":24008,\"tpUserId\":1001,\"ticketNumber\":\"111-001001\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(2021,actualJson.get("responseCode").getAsInt());
	}
	
	@Ignore
	@Test
	public void verifyAndPayWinning_ReturnsInvalidUserIfUserInfoBeanIsNull() throws LMSException{
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(null);
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid User",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsInvalidTicketForWrongTicketFormatInRequest() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn("111-001-001", "8774123687", userInfoBean)).thenThrow(new LMSException(ScratchErrors.INVALID_TICKET_NUMBER_ERROR_CODE, ScratchErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE));
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("Invalid Ticket No",actualJson.get("responseMsg").getAsString());
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsNoWinningForNoPWTTicketNumber() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		ScratchWinningDataBean scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("NON_WIN");
		scratchWinningDataBean.setWinningAmount(0.00);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(scratchWinningDataBean);
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		String actualResponse = actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString();
		Assert.assertEquals("NON_WIN",actualResponse);
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsStatusHighPrizeForNoPWTTicketNumber() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		ScratchWinningDataBean scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("HIGH_PRIZE");
		scratchWinningDataBean.setWinningAmount(125546.00);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(scratchWinningDataBean);
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		String actualResponse = actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString();
		Assert.assertEquals("HIGH_PRIZE",actualResponse);
	}

	@Test
	public void verifyAndPayWinning_ReturnsStatusClaimedForNoPWTTicketNumber() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		ScratchWinningDataBean scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("CLAIMED");
		scratchWinningDataBean.setWinningAmount(125546.00);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(scratchWinningDataBean);
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		String actualResponse = actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString();
		Assert.assertEquals("CLAIMED",actualResponse);
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsStatusWinForPWTTicketNumber() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		ScratchWinningDataBean scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("UNCLAIMED");
		scratchWinningDataBean.setWinningAmount(546.00);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(scratchWinningDataBean);
		Mockito.doNothing().when(scratchWinningServiceImpl).payPWTProcess(Mockito.any(UserInfoBean.class), Mockito.any(ScratchWinningPaymentRequest.class), Mockito.any(ScratchWinningDataBean.class));
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		String actualResponse = actualJson.get("responseData").getAsJsonObject().get("winStatus").getAsString();
		Assert.assertEquals("WIN",actualResponse);
	}
	
	@Test
	public void verifyAndPayWinning_ReturnsWinAmountForPWTTicketNumber() throws LMSException{
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setUserId(1);
		userInfoBean.setUserOrgId(1212);
		ScratchWinningDataBean scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("UNCLAIMED");
		scratchWinningDataBean.setWinningAmount(546.00);
		Mockito.when(scratchService.getUserBeanFromTPUserId(Mockito.anyString())).thenReturn(userInfoBean);
		Mockito.when(scratchWinningServiceImpl.verifyTicketAndVirn(Mockito.anyString(), Mockito.anyString(), Mockito.any(UserInfoBean.class))).thenReturn(scratchWinningDataBean);
		Mockito.doNothing().when(scratchWinningServiceImpl).payPWTProcess(Mockito.any(UserInfoBean.class), Mockito.any(ScratchWinningPaymentRequest.class), Mockito.any(ScratchWinningDataBean.class));
		String response = scratchWinningController.verifyAndPayWinning("{\"requestId\":\"24008\",\"tpUserId\":\"25874\",\"ticketNumber\":\"111-001-001\",\"virnNumber\":\"8774123687\"}");
		JsonObject actualJson = new JsonParser().parse(response).getAsJsonObject();
		double actualResponse = actualJson.get("responseData").getAsJsonObject().get("winAmt").getAsDouble();
		Assert.assertEquals(546.00, actualResponse,1e-15);
	}
	
}
