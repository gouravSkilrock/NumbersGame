package com.skilrock.lms.rest.scratchService.inventoryMgmt.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.beans.GetDlChallanDetailReqBean;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.service.BookActivationService;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;


public class BookActivationTest {

	private BookActivation book = new BookActivation();
	private BookActivationService bookActivationService = Mockito.mock(BookActivationService.class);
	private BookRecieveRegistrationRetailerHelper bookRecieveRegistrationRetailerHelper;
	private Map<String, List<String>> gameBookMap = null;
	private ScratchDaoImpl scratchdao;
	
	@Before
	public void setUp() {
		bookRecieveRegistrationRetailerHelper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		scratchdao = Mockito.mock(ScratchDaoImpl.class);
	}
	
	@Test
	public void canCreateActivationMethod(){
		BookActivation book = new BookActivation();
		String requestData="";
		book.getBookActivated(requestData);
	}
	
	@Test
	public void serviceGetsCreated(){
		Assert.assertNotEquals(null, book.getBookActivationService());
	}
	
	@Test
	public void returnsErrorCode102ForNonJsonREquest() throws JSONException{
		String requestData="requestData=24005";
		String response = book.getBookActivated(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":102,\"errorMessage\":\"Some Internal Error Occured\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	@Test
	public void parsestoScracthMgmtBeanWithJSON(){
		String requestData="{\"requestId\": 24005}";
		book.getBookActivated(requestData);
	}
	
	@Test
	public void getErrorMessageIfMandatoryParametersAreNotProvidedInRequest() throws JSONException{
		String requestData="{\"requestId\": 24005}";
		String response = book.getBookActivated(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":101,\"errorMessage\":\"Mandatory parameteres are not provided\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	@Test
	public void successIfMandatoryParametersAreProvidedInRequest() throws JSONException{
		String requestData="{\"requestId\": 24005,\"tpUserId\": 1001,\"bookNumber\": \"111-001001\"}";
		Mockito.when(bookActivationService.isBookNumberValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(bookActivationService.updateBookNumberStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		book.getBookActivated(requestData);
	}
	
	@Test
	public void getResponseCode100IfAllDataIsCorrect() throws JSONException{
		String requestData="{\"requestId\": 24005,\"tpUserId\": 26,\"bookNumber\": \"101-001001\"}";
		Mockito.when(bookActivationService.isBookNumberValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(bookActivationService.updateBookNumberStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("SUCCESS");
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		String response = book.getBookActivated(requestData);
		Assert.assertEquals(100, new JSONObject(response).getInt("responseCode"));
	}
	
	@Test
	public void getResponseCode103IfAllDataIsCorrectButBookNbrsAreIncorrect() throws JSONException{
		String requestData="{\"requestId\": 24005,\"tpUserId\": 26,\"bookNumber\": \"11-001001\"}";
		Mockito.when(bookActivationService.isBookNumberValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(false);
		Mockito.when(bookActivationService.updateBookNumberStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("SUCCESS");
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		String response = book.getBookActivated(requestData);
		Assert.assertEquals(103, new JSONObject(response).getInt("errorCode"));
	}
	
	@Test
	public void getResponseSomeInternalErrorIfAllDataIsCorrectButServiceLayerFails() throws JSONException{
		String requestData="{\"requestId\": 24005,\"tpUserId\": \"26\",\"bookNumber\": \"101-001001\"}";
		Mockito.when(bookActivationService.isBookNumberValid(Mockito.any(ScracthMgmtBean.class))).thenReturn(true);
		Mockito.when(bookActivationService.updateBookNumberStatus(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		String response = book.getBookActivated(requestData);
		Assert.assertEquals(102, new JSONObject(response).getInt("errorCode"));
	}
	
	@Test
	public void getUserIdFromTpUserId() throws SQLException{
		DaoBean value = null;
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId("1125")).thenReturn(value);
				
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"1125\", \"dlNumber\":\"DLA21604000001\"}";
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		String dlChallanDetail = book.getDlChallanDetail(requestData);
		
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"Invalid Input.\"}", dlChallanDetail);
	}
	
	@Test
	public void getGameBookMapForDlChallan() throws SQLException{
		DaoBean value = new DaoBean();
		value.setUserOrgId(1125);
		gameBookMap = new HashMap<String, List<String>>();
		List<String> bookId = new ArrayList<String> ();
		bookId.add("34813");
		bookId.add("2445");
		gameBookMap.put("play-play-play", bookId);
		
		Mockito.when(bookRecieveRegistrationRetailerHelper.getBooks(Mockito.anyInt(), Mockito.anyString())).thenReturn(gameBookMap);
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId("1125")).thenReturn(value);
		
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"1125\", \"dlNumber\":\"DLA21604000001\"}";
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		String dlChallanDetail = book.getDlChallanDetail(requestData);
		Assert.assertEquals("{\"requestId\":24003,\"responseCode\":100,\"requestData\":[{\"gameName\":\"play\",\"bookList\":[\"34813\",\"2445\"]}]}", dlChallanDetail);
	}
	
	
	@Test
	public void getDlChallanDetails_acceptsJsonRequest() {
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"1001\", \"dlNumber\":\"DLA21604000001\"}";
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		book.getDlChallanDetail(requestData);
	}
	
	@Test
	public void getDlChallanDetails_rejectNonJsonRequest() throws Exception {
		String requestData = "1";
		BookActivation book = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		book.getDlChallanDetail(requestData);
	}
	
	@Test
	public void getDlChallanDetails_MandatoryParamsValidation() {
		GetDlChallanDetailReqBean dlChallanBean = new GetDlChallanDetailReqBean();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<GetDlChallanDetailReqBean>> violations = validator.validate(dlChallanBean);
		System.out.println(violations.isEmpty());
	}
	
	
	@Test 
	public void isTicketByTicketSaleMethodExists(){
		BookActivation book = new BookActivation();
		String requestData = "";
		book.ticketByTicketSale(requestData);
	}
	
	@Test
	public void ticketByTicketSale_returnsErrorMsgIfMandatoryParametersAreNotProvided() throws JSONException{
		BookActivation book = new BookActivation();
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":101,\"errorMessage\":\"Mandatory parameteres are not provided\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	@Test
	public void ticketByTicketSale_returnsNoErrorMsgIfMandatoryParametersAreProvided() throws JSONException{
		BookActivation book = new BookActivation();
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		book.ticketByTicketSale(requestData);
	}
	

	@Test
	public void ticketByTicketSale_Returns100IfAllDataIsCorrect() throws JSONException{
		BookActivation book = new BookActivation(bookActivationService);
		Mockito.when(bookActivationService.ticketByTicketSale(Mockito.any(ScracthMgmtBean.class))).thenReturn("1480916360311");
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		Assert.assertEquals(100,new JSONObject(response).get("responseCode"));
	}
	
	@Test
	public void ticketByTicketSale_Returns1008IfAllTicketIsNotValid() throws JSONException{
		Mockito.when(bookActivationService.ticketByTicketSale(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		BookActivation book = new BookActivation(bookActivationService);
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		Assert.assertEquals(1008,new JSONObject(response).getInt("errorCode"));
	}
	
	
	@Test
	public void ticketByTicketSale_Returns102IfBookActivationServiceIsNotPRovided() throws JSONException{
		BookActivation book = new BookActivation(null);
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		Assert.assertEquals(102,new JSONObject(response).getInt("errorCode"));
	}
	
	@Test 
	public void isTicketByTicketUnSoldMethodExists(){
		BookActivation book = new BookActivation();
		String requestData = "";
		book.ticketByTicketUnSold(requestData);
	}
	
	@Test
	public void ticketByTicketUnSold_returnsErrorMsgIfMandatoryParametersAreNotProvided() throws JSONException{
		BookActivation book = new BookActivation();
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketUnSold(requestData);
		JSONObject expectedJson = new JSONObject("{\"errorCode\":101,\"errorMessage\":\"Mandatory parameteres are not provided\"}");
		Assert.assertEquals(expectedJson.getString("errorMessage"), new JSONObject(response).getString("errorMessage"));
	}
	
	@Test
	public void ticketByTicketUnSold_returnsNoErrorMsgIfMandatoryParametersAreProvided() throws JSONException{
		BookActivation book = new BookActivation();
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		book.ticketByTicketUnSold(requestData);
	}
	

	@Test
	public void ticketByTicketUnSold_Returns100IfAllDataIsCorrect() throws JSONException{
		Mockito.when(bookActivationService.ticketByTicketSale(Mockito.any(ScracthMgmtBean.class))).thenReturn("1480916360311");
		BookActivation book = new BookActivation(bookActivationService);
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketUnSold(requestData);
		Assert.assertEquals(100,new JSONObject(response).get("responseCode"));
	}
	
	@Test
	public void ticketByTicketUnSold_Returns1008IfTicketIsNotValid() throws JSONException{
		Mockito.when(bookActivationService.ticketByTicketSale(Mockito.any(ScracthMgmtBean.class))).thenReturn("FAIL");
		BookActivation book = new BookActivation(bookActivationService);
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		Assert.assertEquals(1008,new JSONObject(response).getInt("errorCode"));
	}
	
	
	@Test
	public void ticketByTicketUnSold_Returns102IfBookActivationServiceIsNotPRovided() throws JSONException{
		BookActivation book = new BookActivation(null);
		String requestData = "{\"requestId\": 24006,\"tpUserId\":\"26\" ,\"ticketNumber\": \"111-001001001\",\"tpTransactionId\": \"7737579312\"}";
		String response = book.ticketByTicketSale(requestData);
		Assert.assertEquals(102,new JSONObject(response).getInt("errorCode"));
	}
	
}
