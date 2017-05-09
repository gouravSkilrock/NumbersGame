package com.skilrock.lms.web.scratchService.customerSpecificReport.controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;
import com.skilrock.lms.web.scratchService.customerSpecificReport.dao.TicketByTicketSalePwtDao;
import com.skilrock.lms.web.scratchService.customerSpecificReport.service.TicketByTicketSalePwtService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TicketByTicketSalePwtDao.class, Utility.class})
@SuppressStaticInitializationFor("com.skilrock.lms.common.Utility")
public class TicketByTicketSalePwtServiceTest {
    private TicketByTicketSalePwtService ticketByTicketSalePwtService = null;
	public Map<String, Map<Integer, TicketByTicketSalePwt>> ticketByTicketSalePwtMap = null;
	public Map<Integer, String> gameMap = null;
	private Timestamp startDate = null;
	private Timestamp endDate = null;
	int roleId=1;
	
	@Before
	public void setIntialData(){
	  setOrgWiseTktByTktSalePwtDummyData();
	  setGamMapDummyData();
	  powerMockOnClasses();
	}


	private void powerMockOnClasses() {
	    powerMockUtilityClass();
	}


	@Test
	public void getTicketByTicketSaleNPwtForAllAgentForAgentWise() throws ParseException, LMSException {
		CustomersReportAction customersReportAction = setCustomersReportTestDataForAllAgent();
		powerMockDaoLayer(customersReportAction);
		setTimeStampData(customersReportAction);
		ticketByTicketSalePwtService = new TicketByTicketSalePwtService();
		ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(customersReportAction.getReportType(), customersReportAction.getAgentOrgId(), startDate, endDate,roleId);	
	}
	
	@Test
	public void getTicketByTicketSaleNPwtForAllSingleAgentForAgentWise() throws ParseException, LMSException {
		CustomersReportAction customersReportAction = setCustomersReportTestDataForSingleAgent();
		powerMockDaoLayer(customersReportAction);
		setTimeStampData(customersReportAction);
		ticketByTicketSalePwtService = new TicketByTicketSalePwtService();
		ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(customersReportAction.getReportType(), customersReportAction.getAgentOrgId(), startDate, endDate, roleId);	
	}
	
	@Test
	public void getTicketByTicketSaleNPwtForAllAgentForRetailerWise() throws ParseException, LMSException {
		CustomersReportAction customersReportAction = setCustomersReportTestDataForAllAgentRetailerWise();
		powerMockDaoLayer(customersReportAction);
		setTimeStampData(customersReportAction);
		ticketByTicketSalePwtService = new TicketByTicketSalePwtService();
		ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(customersReportAction.getReportType(), customersReportAction.getAgentOrgId(), startDate, endDate, roleId);	
	}
	
	@Test
	public void getTicketByTicketSaleNPwtForAllSingleAgentForRetailerWise() throws ParseException, LMSException {
		CustomersReportAction customersReportAction = setCustomersReportTestDataForSingleAgentRetailerWise();
		powerMockDaoLayer(customersReportAction);
		setTimeStampData(customersReportAction);
		ticketByTicketSalePwtService = new TicketByTicketSalePwtService();
		ticketByTicketSalePwtService.getTicketByTicketSaleNPwt(customersReportAction.getReportType(), customersReportAction.getAgentOrgId(), startDate, endDate, roleId);	
	}
	
	@Test
	public void getGameMap() throws LMSException, ParseException{
		CustomersReportAction customersReportAction = setCustomersReportTestDataForSingleAgentRetailerWise();
		powerMockDaoLayer(customersReportAction);
		setTimeStampData(customersReportAction);
		ticketByTicketSalePwtService = new TicketByTicketSalePwtService();
		ticketByTicketSalePwtService.getGameMap();
	}
	
	private CustomersReportAction setCustomersReportTestDataForAllAgent(){
		CustomersReportAction customersReportAction = new CustomersReportAction();
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Agent Wise");
    	customersReportAction.setAgentOrgId(100001);
		return customersReportAction;
	}
	
	private CustomersReportAction setCustomersReportTestDataForSingleAgent(){
		CustomersReportAction customersReportAction = new CustomersReportAction();
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Agent Wise");
    	customersReportAction.setAgentOrgId(895);
		return customersReportAction;
	}
	
	private CustomersReportAction setCustomersReportTestDataForSingleAgentRetailerWise(){
		CustomersReportAction customersReportAction = new CustomersReportAction();
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Retailer Wise");
    	customersReportAction.setAgentOrgId(895);
		return customersReportAction;
	}
	
	private CustomersReportAction setCustomersReportTestDataForAllAgentRetailerWise(){
		CustomersReportAction customersReportAction = new CustomersReportAction();
		customersReportAction.setStart_date("2016-02-02");
		customersReportAction.setEnd_Date("2017-02-15");
    	customersReportAction.setReportType("Retailer Wise");
    	customersReportAction.setAgentOrgId(100001);
		return customersReportAction;
	}
	
	private void powerMockDaoLayer(CustomersReportAction customersReportAction) throws LMSException{
		PowerMockito.mockStatic(TicketByTicketSalePwtDao.class);
		Connection con = Mockito.mock(Connection.class);
		PowerMockito.when(TicketByTicketSalePwtDao.ticketByTicketSaleForAllAgentWise(startDate, endDate, con, roleId)).thenReturn(ticketByTicketSalePwtMap);
		PowerMockito.when(TicketByTicketSalePwtDao.ticketByTicketSaleForSingleAgentWise(customersReportAction.getAgentOrgId(), startDate, endDate, con)).thenReturn(ticketByTicketSalePwtMap);
		PowerMockito.when(TicketByTicketSalePwtDao.ticketByTicketSaleRetailerWiseForAllAgent(startDate, endDate, con,roleId)).thenReturn(ticketByTicketSalePwtMap);
		PowerMockito.when(TicketByTicketSalePwtDao.ticketByTicketSaleRetailerWiseForSingleAgent(customersReportAction.getAgentOrgId(), startDate, endDate, con)).thenReturn(ticketByTicketSalePwtMap);
		PowerMockito.when(TicketByTicketSalePwtDao.getGameMap()).thenReturn(gameMap);
	}
	
		
	private void powerMockUtilityClass(){
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")).thenReturn("No");
	}
	
	private void setOrgWiseTktByTktSalePwtDummyData(){
		ticketByTicketSalePwtMap = new HashMap<String, Map<Integer, TicketByTicketSalePwt>>();
		Map<Integer, TicketByTicketSalePwt> gameTktMap = new HashMap<Integer, TicketByTicketSalePwt>();
		gameTktMap.put(1, getTicketByTicketSalePwtDummyData());
		ticketByTicketSalePwtMap.put("testAgent", gameTktMap);
		
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
	
	private void setTimeStampData(CustomersReportAction customersReportAction) throws ParseException {
		String dateFormat = "dd-MM-yyyy";
		  startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(customersReportAction.getStart_date()).getTime());
		  endDate =   new Timestamp((new SimpleDateFormat(dateFormat)).parse(customersReportAction.getEnd_Date()).getTime()+ 24 * 60 * 60 * 1000 - 1000);
	}
}
