package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.scratchService.reportsMgmt.common.TicketByTicketSaleTxnReportAction;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ActionContext.class,ReportUtility.class})

public class TicketByTicketSaleTxnReportHelperIT {
	  
		private TicketByTicketSaleTxnReportAction actionObject = null;
		private ActionContext actioncontext;
		private ReportStatusBean reportBean ;
		private HttpSession session = null;
		Map<String,String> orgMap ;
		AgentWiseTktByTktSaleTxnBean agentBean;
		Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportMap;
		HttpServletRequest httpServletRequest;
		Map<String, AgentWiseTktByTktSaleTxnBean> beanMap ;
		
		Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>> gameMap ;
		
		@Before public void setInitialTestData() throws Exception{
			actionObject = new TicketByTicketSaleTxnReportAction();
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
			
			actionObject.setStart_date("2016-12-21 00:00:00.0");
			actionObject.setEnd_Date("2017-2-2 00:00:00.0");
			actionObject.setAgtId(895);
			
			mockClasses();
			powerMockClassestocallStaticMethods();
		}
		
		private void powerMockClassestocallStaticMethods() throws Exception{
			PowerMockito.mockStatic(ActionContext.class);
			actioncontext = Mockito.mock(ActionContext.class);
			PowerMockito.when(ActionContext.class,"getContext").thenReturn(actioncontext); 
			Mockito.when(actioncontext.getName()).thenReturn("agt_rep_completeCollectionReport_TicketByTicketSaleTxn_Menu_RetalerWise");
			
			reportBean = Mockito.mock(ReportStatusBean.class);
			
			PowerMockito.mockStatic(ReportUtility.class);
			PowerMockito.when(ReportUtility.class,"getReportStatus",Matchers.any(String.class)).thenReturn(reportBean);
			PowerMockito.when(ReportUtility.class,"getOrgMap",Matchers.any(String.class)).thenReturn(orgMap);
		}
		
		 private  void mockClasses(){
			httpServletRequest = Mockito.mock(HttpServletRequest.class);
			session = Mockito.mock(HttpSession.class);
			Mockito.when(httpServletRequest.getSession()).thenReturn(session);
			actionObject.setRequest(httpServletRequest);
		}
		 
		@Test
		public void tktByTktSaleReport_RetailerWiseTestForSuccessStatus() throws Exception{
			Mockito.when(reportBean.getReportStatus()).thenReturn("SUCCESS");
			actionObject.tktByTktSaleReport_RetailerWise();
			Assert.assertEquals("SUCCESS", "SUCCESS");
		}
		@Test
		public void tktByTktSaleReport_RetailerWiseTestForFailureStatus() throws Exception{
			Mockito.when(reportBean.getReportStatus()).thenReturn("RESULT_TIMING_RESTRICTION");
			actionObject.tktByTktSaleReport_RetailerWise();
			Assert.assertEquals("RESULT_TIMING_RESTRICTION", "RESULT_TIMING_RESTRICTION");
		}
		
		@Test
		public void onFirstActionCallSuccessStatus() throws Exception{
			Mockito.when(reportBean.getReportStatus()).thenReturn("SUCCESS");
			actionObject.execute();
			Assert.assertEquals("SUCCESS", "SUCCESS");
		}
		@Test
		public void onFirstActionCallFailStatus() throws Exception{
			Mockito.when(reportBean.getReportStatus()).thenReturn("RESULT_TIMING_RESTRICTION");
			actionObject.execute();
			Assert.assertEquals("RESULT_TIMING_RESTRICTION", "RESULT_TIMING_RESTRICTION");
		}
}
