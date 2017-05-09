package com.skilrock.lms.rest.scratchService.inventoryMgmt.controller;

import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.service.BookActivationService;

public class BookActivationIt {
	
	private BookActivation invMgmt = new BookActivation();
	private BookActivationService bookActivationService = Mockito.mock(BookActivationService.class);
	private BookRecieveRegistrationRetailerHelper bookRecieveRegistrationRetailerHelper;
	private ScratchDaoImpl scratchdao;
	
	@Before
	public void setUp() {
		scratchdao = new ScratchDaoImpl();
		bookRecieveRegistrationRetailerHelper = new BookRecieveRegistrationRetailerHelper();
		invMgmt = new BookActivation(bookActivationService,bookRecieveRegistrationRetailerHelper,scratchdao);
		
	}
	
	@Test
	public void getDlChallanDetail_returnsFailureForInvalidChallan() {
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"1125\", \"dlNumber\":\"DLA216040001\"}";
		String dlChallanDetail = invMgmt.getDlChallanDetail(requestData);
		
		Assert.assertEquals("{\"requestId\":24003,\"responseCode\":2002,\"responseMsg\":\"Invalid Challan Id For InTransit Items\"}", dlChallanDetail);
	}
	
	@Test
	public void getDlChallanDetail_returnsFailureForMandatoryParamsNotPresent(){
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"25\"}";
		String dlChallanDetail = invMgmt.getDlChallanDetail(requestData);
		
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"Invalid Input.\"}", dlChallanDetail);
	}
	
	@Test
	public void getDlChallanDetail_returnsFailureForInvalidTpUserId(){
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"25\", \"dlNumber\":\"DLA21604000001\"}";
		String dlChallanDetail = invMgmt.getDlChallanDetail(requestData);
		
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"Invalid Input.\"}", dlChallanDetail);
	}
	
	@Test
	public void getDlChallanDetail_returnsSuccess(){
		String requestData = "{\"requestId\": \"24003\", \"tpUserId\":\"1125\", \"dlNumber\":\"DLA21604000001\"}";
		String dlChallanDetail = invMgmt.getDlChallanDetail(requestData);
		
		Assert.assertEquals("{\"requestId\":24003,\"responseCode\":100,\"requestData\":[{\"gameName\":\"Cash It\",\"bookList\":[\"101-001001\"]},{\"gameName\":\"Play Play Play\",\"bookList\":[\"101-001002\"]}]}", dlChallanDetail);
	}

}
