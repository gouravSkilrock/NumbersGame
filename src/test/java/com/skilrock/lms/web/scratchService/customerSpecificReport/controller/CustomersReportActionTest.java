package com.skilrock.lms.web.scratchService.customerSpecificReport.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;
import com.skilrock.lms.web.scratchService.customerSpecificReport.service.TicketByTicketSalePwtService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ActionContext.class, ReportUtility.class})
public class CustomersReportActionTest {
	private CustomersReportAction customersReportAction = null;
	private TicketByTicketSalePwtService ticketByTicketSalePwtService = null;
	private Map<String, Map<Integer, TicketByTicketSalePwt>> orgWiseTktByTktSalePwt = null;
	private Map<Integer, String> gameMap = null;
	private ActionContext actioncontext;
	private ReportStatusBean reportBean ;

	@Before
	public void setData() throws Exception{
		ticketByTicketSalePwtService = Mockito.mock(TicketByTicketSalePwtService.class);
		setCustomersReportActionTestData();		
		mockForDateFormat();
	}

	
	private void mockForDateFormat() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		customersReportAction.setServletRequest(request);		
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		ServletContext servletContext = Mockito.mock(ServletContext.class);
		Mockito.when(session.getServletContext()).thenReturn(servletContext);
		Mockito.when(servletContext.getAttribute("date_format")).thenReturn("dd-MM-yyyy");
		UserInfoBean userInfoBean = Mockito.mock(UserInfoBean.class);
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.when(userInfoBean.getRoleId()).thenReturn(1);
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
	public void customerSpecificReportCall() throws Exception {
		PowerMockito.mockStatic(ActionContext.class);
		actioncontext = Mockito.mock(ActionContext.class);
		PowerMockito.when(ActionContext.class,"getContext").thenReturn(actioncontext); 
		Mockito.when(actioncontext.getName()).thenReturn("ticketHelperAction");		
		reportBean = Mockito.mock(ReportStatusBean.class);		
		PowerMockito.mockStatic(ReportUtility.class);
		PowerMockito.when(ReportUtility.class,"getReportStatus",Matchers.any(String.class)).thenReturn(reportBean);
		Mockito.when(reportBean.getReportStatus()).thenReturn("SUCCESS");
		
		String resp = customersReportAction.customerSpecificReport();
		Assert.assertEquals("success", resp);
	}
	
	@Test
	public void generateCustomerReportActionTest() throws Exception{
		setOrgWiseTktByTktSalePwtDummyData();
		setGamMapDummyData();
		mockTicketByTicketSalePwtServiceMethods();
		String resp = customersReportAction.generateCustomerReport();
		Assert.assertEquals("success", resp);
		Assert.assertNotNull(customersReportAction.getResponseData());
	}
	
	@Test
	public void errorIfDataNotPresent(){
		customersReportAction.setStart_date("");
		customersReportAction.setEnd_Date("");
		String resp = customersReportAction.generateCustomerReport();
		Assert.assertEquals("error", resp);
	}
	
	private void setCustomersReportActionTestData(){
		customersReportAction = new CustomersReportAction(ticketByTicketSalePwtService);
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Agent Wise");
    	customersReportAction.setAgentOrgId(100001);
	}
	
	private void mockTicketByTicketSalePwtServiceMethods() throws Exception {
		String dateFormat = "dd-MM-yyyy";
		Timestamp startDate = null;
		Timestamp endDate = null;
		int roleId=1;
		startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(customersReportAction.getStart_date()).getTime());
		endDate =   new Timestamp((new SimpleDateFormat(dateFormat)).parse(customersReportAction.getEnd_Date()).getTime()+ 24 * 60 * 60 * 1000 - 1000);
		Mockito.when(ticketByTicketSalePwtService.getGameMap()).thenReturn(gameMap);
		Mockito.when(ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(customersReportAction.getReportType(), customersReportAction.getAgentOrgId(), startDate, endDate,roleId)).thenReturn(orgWiseTktByTktSalePwt);
	}
	
	private void setOrgWiseTktByTktSalePwtDummyData(){
		orgWiseTktByTktSalePwt = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		Map<Integer, TicketByTicketSalePwt> gameTktMap = new HashMap<Integer, TicketByTicketSalePwt>();
		gameTktMap.put(1, getTicketByTicketSalePwtDummyData());
		orgWiseTktByTktSalePwt.put("testAgent", gameTktMap);
		
	}
	
	private void setGamMapDummyData(){
		gameMap = new HashMap<Integer, String>();
		gameMap.put(1, "peace");
		gameMap.put(2, "Friends");
	}
	
	private TicketByTicketSalePwt getTicketByTicketSalePwtDummyData(){
		TicketByTicketSalePwt ticketByTicketSalePwt = new TicketByTicketSalePwt();
		ticketByTicketSalePwt.setGameId(1);
		ticketByTicketSalePwt.setGameName("peace");
		ticketByTicketSalePwt.setSale(200.00);
		ticketByTicketSalePwt.setWinning(100.00);
		ticketByTicketSalePwt.setOrgName("testAgent");
		ticketByTicketSalePwt.setOrgId(100001);
		return ticketByTicketSalePwt;
	}

}
