package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;


public class OrderManagementIT {
	
	@Test
	public void getOrderHistoryResponse() throws Exception{
		String requestData="{'requestId': 24011,'tpUserId': 1001,'orderReferenceId': 123456,'orderStatus': 'RECEIVED'}";
		ScratchController scratchController=new ScratchController();
		 String jsonObject=scratchController.getOrderHistory(requestData);
		//Assert.assertTrue(jsonObject.getInt("responseCode")==100);
	}
}
