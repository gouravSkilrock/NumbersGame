package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.TicketByTicketSaleTxnReportDaoImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Utility.class,DBConnect.class,DBConnectReplica.class,TicketByTicketSaleTxnReportDaoImpl.class})
@SuppressStaticInitializationFor({ "com.skilrock.lms.common.Utility" })
public class TicketByTicketSaleTxnReportHelperTest {
	
	
	Map<String,String> orgMap ;
	AgentWiseTktByTktSaleTxnBean agentBean;
	Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportMap;
	HttpServletRequest httpServletRequest;
	Map<String, AgentWiseTktByTktSaleTxnBean> beanMap ;
	
	Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>> gameMap ;
	ReportStatusBean  reportBean ;
	ReportUtility reportUtility = null;
	
	@Before public void setInitialTestData() throws Exception{
		orgMap = new LinkedHashMap<String,String>();
		beanMap = new LinkedHashMap<String, AgentWiseTktByTktSaleTxnBean>();
		gameMap = new LinkedHashMap<String, Map<String, AgentWiseTktByTktSaleTxnBean>>();
		reportMap = new LinkedHashMap<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>>();
		
		agentBean = new AgentWiseTktByTktSaleTxnBean();
		agentBean.setGame_name("Game1");
		agentBean.setRetailerName("Retailer1");
		agentBean.setScratchGameWiseSale(0.0);
		agentBean.setScratchGameWiseWinning(0.0);
		
		beanMap.put("RetailerId",agentBean);
		gameMap.put("Game1",beanMap);
		reportMap.put("SE", gameMap);
		
		orgMap.put("RetailerId","RetailerName");
		
		powerMockClassestocallStaticMethods();
		/*actionObject.setStart_date("2016-12-21 00:00:00.0");
		actionObject.setEnd_Date("2017-2-2 00:00:00.0");*/
		
		
	}
	
	private void powerMockClassestocallStaticMethods() throws Exception{
		
		reportBean = Mockito.mock(ReportStatusBean.class);
		reportBean.setReportingFrom("MAIN_DB");
		PowerMockito.mockStatic(TicketByTicketSaleTxnReportDaoImpl.class);
		PowerMockito.when(TicketByTicketSaleTxnReportDaoImpl.class, "reportForRetailerTicketByTktTxn",
				Matchers.any(Timestamp.class), Matchers.any(Timestamp.class), Matchers.any(Connection.class), Matchers.anyInt(),Matchers.anyString())
				.thenReturn(reportMap);
		
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class,"getPropertyValue",Matchers.anyString()).thenReturn("NO");
		
		
	}
	@Test
	public void reportForRetailerTicketByTktTxnTest() throws Exception{
		Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> testMap = new LinkedHashMap<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>>();
		testMap = TicketByTicketSaleTxnReportHelper.reportForRetailerTicketByTktTxn(null, null, 1, reportBean,"AGENT");
		Assert.assertEquals(reportMap, testMap);
}
	
	

}
 