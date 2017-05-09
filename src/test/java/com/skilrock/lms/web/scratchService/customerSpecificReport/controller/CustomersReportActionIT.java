package com.skilrock.lms.web.scratchService.customerSpecificReport.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Utility.class })
public class CustomersReportActionIT {
	private CustomersReportAction customersReportAction = null;
	public Map<String, Map<Integer, TicketByTicketSalePwt>> orgWiseTktByTktSalePwt = null;
	public Map<Integer, String> gameMap = null;

	@Before
	public void setData() throws Exception{
		setCustomersReportActionTestData();
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		customersReportAction.setServletRequest(request);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
	}

	
	@Test
	public void isCustomerReportActionExist(){
		CustomersReportAction customersReportActionTest = new CustomersReportAction();
		Assert.assertNotNull(customersReportActionTest);
	}
	
	@Test
	public void validateData(){
		Assert.assertEquals("02-02-2016", customersReportAction.getStart_date());
		Assert.assertEquals("15-02-2017", customersReportAction.getEnd_Date());
		Assert.assertEquals("Agent Wise", customersReportAction.getReportType());
		Assert.assertEquals(100001, customersReportAction.getAgentOrgId());
	}
	
	@Test
	public void customerSpecificReportCall(){
		String resp = customersReportAction.customerSpecificReport();
		Assert.assertEquals("success", resp);
	}
	
	@Test
	public void generateCustomerReportActionTest() throws Exception{
		poweMockUtilityClass();
		String resp = customersReportAction.generateCustomerReport();
		Assert.assertEquals("success", resp);
		Assert.assertNotNull(customersReportAction.getResponseData());
	}
	
	@Test
	public void errorIfDataNotPresent(){
		CustomersReportAction customersReportActionTest = new CustomersReportAction();
		String resp = customersReportActionTest.generateCustomerReport();
		Assert.assertEquals("error", resp);
	}
	
	private void setCustomersReportActionTestData(){
		customersReportAction = new CustomersReportAction();
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Agent Wise");
    	customersReportAction.setAgentOrgId(100001);
	}
	
	private void poweMockUtilityClass() throws Exception{
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", "IS_DATA_FROM_REPLICA").thenReturn("No");
		
	}
	
}
