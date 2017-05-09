package com.skilrock.lms.scratchService.orderMgmt.controllerImpl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.skilrock.lms.rest.scratchService.orderMgmt.controller.ScratchController;

public class GetGameListIT {

	@Test
	public void getGameListIT() throws JSONException{
		String requestData = "{'requestId': 24009,'tpUserId': 1001}";
		ScratchController orderManagementControllerImpl =new ScratchController();
		
		String jsonObject=orderManagementControllerImpl.getGameList(requestData);
		System.out.println(jsonObject);
		Boolean flag=false;
		if(jsonObject!=null){
			flag=true;
		}
		Assert.assertTrue(flag);
	}
}
