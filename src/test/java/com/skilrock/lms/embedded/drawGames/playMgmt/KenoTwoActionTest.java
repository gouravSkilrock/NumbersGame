package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ServletActionContext.class, Util.class, TransactionManager.class, Utility.class, DBConnect.class, CommonMethods.class })
@SuppressStaticInitializationFor("com.skilrock.lms.web.drawGames.common.Util")
public class KenoTwoActionTest {

	private KenoTwoAction kenoTwoAction = null;
	private UserInfoBean userBean = null;
	private HttpServletResponse response = null;
	private ServletContext servletContext = null;
	private HttpSession session = null;
	private HttpServletRequest request;
	KenoPurchaseBean kenoPurchaseBean = null;
	private DrawGameRPOSHelper drawGameRPOSHelper;
	
	@Before
	public void setUpDependency() throws Exception {
		setMocking();
		setPowerMocking();
		setUpKenoPurchaseBeanData();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_1BetWithManualForSale() throws Exception {
		setKenoTwoActionDataForPerm_1BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}	
	
	@Test
	public void purchaseTicketProcessForPerm_1BetWithQpForSale() throws Exception {
		setKenoTwoActionDataForPerm_1BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_2BetWithManualForSale() throws Exception {
		setKenoTwoActionDataForPerm_2BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_2BetWithQpForSale() throws Exception {
		setKenoTwoActionDataForPerm_2BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_3BetWithManualForSale() throws Exception {
		setKenoTwoActionDataForPerm_3BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_3BetWithQpForSale() throws Exception {
		setKenoTwoActionDataForPerm_3BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForAdvancePalyBetWithManualForSale() throws Exception {
		setKenoTwoActionDataForAdvancePlayWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForAdvancePalyBetWithQpForSale() throws Exception {
		setKenoTwoActionDataForAdvancePlayWithQP();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcess_IfSellNotSuccess() throws Exception {
		setKenoTwoActionDataForAdvancePlayWithQP();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		kenoPurchaseBean.setSaleStatus("Fail");
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateSellIsFail();
	}
	
	@Test
	public void purchaseTicketProcessFailureIfDrawGameNotAvailable() throws Exception{
		setKenoTwoActionDataForAdvancePlayWithQP();
		Mockito.when(servletContext.getAttribute("IS_DRAW")).thenReturn("NO");
		validateDrawGameNotAvailable();
	}
	
	@Test(expected=AssertionError.class)
	public void purchaseTicketProcessFailureIfCurrentUserMappingIdIsZero() throws Exception{
		setKenoTwoActionDataForAdvancePlayWithQP();
		setUserInfoBeanData();
		settingMockingDataForServletContext();
		userBean.setCurrentUserMappingId(0);
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateSellIsFail();
	}
	
	@Test(expected=NullPointerException.class)
	public void purchaseTicketProcessFailureIfNothingSet() throws Exception{
		settingMockingDataForServletContext();
		setUpMockingDataForDrawGameRPOSHelper(kenoPurchaseBean);
		validateSellIsFail();
	}
	
	private void validateDrawGameNotAvailable() throws Exception {		
		kenoTwoAction.purchaseTicketProcess();
		Assert.assertEquals(kenoTwoAction.getFinalPurchaseData(), "ErrorMsg:Draw Game Not Available|");
	}
	
	private void validateSellIsFail() throws Exception {		
		kenoTwoAction.purchaseTicketProcess();
		Assert.assertEquals(kenoTwoAction.getFinalPurchaseData(), "ErrorMsg:Error!Try Again|");
	}
    
	private void validateResponses() throws Exception {
		validatekenoTwoActionResponse();		
		validateKenoPurchaseBeanOnCommonPurchaseHelper();
	}
	
	private void validatekenoTwoActionResponse() throws Exception {
		kenoTwoAction.purchaseTicketProcess();
		Assert.assertEquals(kenoTwoAction.getFinalPurchaseData(),
				"TicketNo:7782313201324401700|brCd:7782313201324401700|Date:2016-11-15|Time:17:44:13|PlayType:Perm1|Num:EEE|PanelPrice:1|QP:2|TicketCost:20.0|DrawDate:2016-11-15|DrawTime:18:10:00|DrawDate:2016-11-15|DrawTime:20:10:00|balance:4800.00|");
	}

	private void validateKenoPurchaseBeanOnCommonPurchaseHelper() throws Exception {
		// Setup To validate KenoPurchaseBean Before Calling Helper
		ArgumentCaptor<KenoPurchaseBean> kenoBeanCaptor = ArgumentCaptor.forClass(KenoPurchaseBean.class);
		Mockito.verify(drawGameRPOSHelper).commonPurchseProcessKenoTwo(Mockito.any(UserInfoBean.class),
				kenoBeanCaptor.capture());
		// Validating KenoPurchaseBean Data After Calling Helper
		KenoPurchaseBean helperKenoBean = kenoBeanCaptor.getValue();
		Assert.assertNotNull(helperKenoBean.getBarcodeType());
		Assert.assertNotNull(helperKenoBean.getNoPicked());
		Assert.assertNotNull(helperKenoBean.getUserId());
		Assert.assertNotNull(helperKenoBean.getNoOfPanel());
	}

			
	private void setKenoTwoActionDataForPerm_1BetWithManual() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("01,02,03,04,05,06,07,08,09,10");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("1.00");
		kenoTwoAction.setNoPicked("10");
		kenoTwoAction.setPlayType("Perm1");
		kenoTwoAction.setQP("2");		
		kenoTwoAction.setBetAmt("1");
		kenoTwoAction.setIsAdvancedPlay(0);
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForPerm_1BetWithQp() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("QP");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("1.00");
		kenoTwoAction.setNoPicked("10");
		kenoTwoAction.setPlayType("Perm1");
		kenoTwoAction.setQP("1");		
		kenoTwoAction.setBetAmt("1");
		kenoTwoAction.setIsAdvancedPlay(0);
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForPerm_2BetWithManual() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("105.00");
		kenoTwoAction.setNoPicked("15");
		kenoTwoAction.setPlayType("Perm2");
		kenoTwoAction.setQP("2");		
		kenoTwoAction.setBetAmt("10");
		kenoTwoAction.setIsAdvancedPlay(0);
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForPerm_2BetWithQp() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("QP");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("28.00");
		kenoTwoAction.setBetAmt("10");
		kenoTwoAction.setNoPicked("8");
		kenoTwoAction.setPlayType("Perm2");
		kenoTwoAction.setQP("1");		
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForPerm_3BetWithManual() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("45.50");
		kenoTwoAction.setNoPicked("15");
		kenoTwoAction.setPlayType("Perm3");
		kenoTwoAction.setQP("2");		
		kenoTwoAction.setBetAmt("1");
		kenoTwoAction.setIsAdvancedPlay(0);
		setUpCommonDataForKenoTwo();
	}	  
	
	private void setKenoTwoActionDataForPerm_3BetWithQp() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("QP");
		kenoTwoAction.setNoOfDraws(1);
		kenoTwoAction.setTotalPurchaseAmt("28.60");
		kenoTwoAction.setBetAmt("1");
		kenoTwoAction.setNoPicked("13");
		kenoTwoAction.setPlayType("Perm3");
		kenoTwoAction.setQP("1");		
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForAdvancePlayWithManual() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("01,02,03,04,05,06,07,08,09,10Nxt01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		kenoTwoAction.setNoOfDraws(2);
		kenoTwoAction.setTotalPurchaseAmt("41.00");
		kenoTwoAction.setNoPicked("10Nxt15");
		kenoTwoAction.setPlayType("Perm1NxtPerm2");
		kenoTwoAction.setQP("2Nxt2");
		kenoTwoAction.setBetAmt("10Nxt1");
		kenoTwoAction.setIsAdvancedPlay(1);
		kenoTwoAction.setDrawIdArr(new String[] { "74680", "74681"});
		setUpCommonDataForKenoTwo();
	}
	
	private void setKenoTwoActionDataForAdvancePlayWithQP() {
		kenoTwoAction = new KenoTwoAction(drawGameRPOSHelper);		
		kenoTwoAction.setPickedNumbers("QPNxtQPNxtQP");
		kenoTwoAction.setNoOfDraws(3);
		kenoTwoAction.setTotalPurchaseAmt("112.20");
		kenoTwoAction.setNoPicked("10Nxt8Nxt9");
		kenoTwoAction.setPlayType("Perm1NxtPerm2NxtPerm3");
		kenoTwoAction.setQP("1Nxt1Nxt1");
		kenoTwoAction.setBetAmt("1Nxt10Nxt1");
		kenoTwoAction.setIsAdvancedPlay(1);
		kenoTwoAction.setDrawIdArr(new String[] { "74680", "74681", "74682"});
		setUpCommonDataForKenoTwo();
	}

	private void setUpCommonDataForKenoTwo() {
		kenoTwoAction.setUserName("testret");
		kenoTwoAction.setGameId(1);
		kenoTwoAction.setLSTktNo(0);		
		kenoTwoAction.setPlrMobileNumber("1234567890");
		kenoTwoAction.setServletResponse(response);
		kenoTwoAction.setServletRequest(request);
		kenoTwoAction.setRequest(request);
		kenoTwoAction.setResponse(response);
	}		
	
	private void setUserInfoBeanData() {
		userBean = new UserInfoBean();
		userBean.setCurrentUserMappingId(1);
		userBean.setAvailableCreditLimit(5000.00);
		userBean.setClaimableBal(200.0);
	}

	private void setUpKenoPurchaseBeanData() {
		kenoPurchaseBean = new KenoPurchaseBean();
		kenoPurchaseBean.setSaleStatus("SUCCESS");
		kenoPurchaseBean.setTicket_no("778231320132440170");
		kenoPurchaseBean.setBarcodeType("77823132013244017010");
		kenoPurchaseBean.setPlayType(new String[] { "Perm1" });
		kenoPurchaseBean.setIsQuickPick(new int[] { 2 });
		Map<String, List<String>> map = new HashMap<String, List<String>>();		
		kenoPurchaseBean.setAdvMsg(map);
		List<String> drawDateTime = new ArrayList<String>();
		drawDateTime.add("2016-11-15 18:10:00");
		drawDateTime.add("2016-11-15 20:10:00");		
		kenoPurchaseBean.setDrawDateTime(drawDateTime);		
		kenoPurchaseBean.setPurchaseTime("2016-11-15 17:44:13");
		kenoPurchaseBean.setActionName("Action");
		kenoPurchaseBean.setReprintCount("0");
		kenoPurchaseBean.setBarcodeCount((short) 0);
		kenoPurchaseBean.setNoOfPanel(1);
		kenoPurchaseBean.setTotalPurchaseAmt(20.00);
		kenoPurchaseBean.setUnitPrice(new double[] { 1.0 });
		kenoPurchaseBean.setBetAmountMultiple(new int[] { 1 });
		kenoPurchaseBean.setNoOfLines(new int[] { 1 });
		kenoPurchaseBean.setNoOfDraws(1);
		kenoPurchaseBean.setPlayerData(new String[] { "EEE" });
	}
	
	private void setMocking() throws Exception {
		setUpMockingForHttpServletResponse();
		servletContext = Mockito.mock(ServletContext.class);
		session = Mockito.mock(HttpSession.class);
		setUpMockingForHttpServletRequest();
		drawGameRPOSHelper = Mockito.mock(DrawGameRPOSHelper.class);
	}
	
	private void settingMockingDataForServletContext() {
		Map<String, HttpSession> currentUserSessionMap = new HashMap<String, HttpSession>();
		currentUserSessionMap.put("testret", session);
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);
		Map<Integer, Map<Integer, String>> drawIdTableMap = new HashMap<Integer, Map<Integer, String>>();
		Mockito.when(servletContext.getAttribute("IS_DRAW")).thenReturn("Yes");
		Mockito.when(servletContext.getAttribute("drawIdTableMap")).thenReturn(drawIdTableMap);
		Mockito.when(servletContext.getAttribute("LOGGED_IN_USERS")).thenReturn(currentUserSessionMap);
		Mockito.when(servletContext.getAttribute("REF_MERCHANT_ID")).thenReturn("2");
		Mockito.when(servletContext.getAttribute("AUTO_CANCEL_CLOSER_DAYS")).thenReturn("2");
		Mockito.when(servletContext.getAttribute("BARCODE_TYPE")).thenReturn("BARCODE_TYPE");
		LMSUtility.sc = servletContext;
	}

	private void setUpMockingForHttpServletRequest() {
		request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute("code")).thenReturn("1");
	}

	private void setUpMockingDataForDrawGameRPOSHelper(KenoPurchaseBean kenoPurchaseBean) throws Exception {		
		Mockito.when(drawGameRPOSHelper.commonPurchseProcessKenoTwo(Mockito.any(UserInfoBean.class),
				Mockito.any(KenoPurchaseBean.class))).thenReturn(kenoPurchaseBean);
	}

	private void setUpMockingForHttpServletResponse() throws IOException {
		ServletOutputStream write = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				// Dummy stream, Doesn't need to do anything
			}
		};
		response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(response.getOutputStream()).thenReturn(write);
	}

	private void setPowerMocking() throws Exception {
		setUpPowerMockingForCommonMethods();
		setUpPowerMockForUtilClass();
		setUpPowerMockingForServletActionContext();
		PowerMockito.mockStatic(TransactionManager.class);
	}

	private void setUpPowerMockingForCommonMethods() {
		PowerMockito.mockStatic(CommonMethods.class);
		PowerMockito.when(CommonMethods.sendSMS(Mockito.anyString())).thenReturn(null);
	}

	private void setUpPowerMockingForServletActionContext() throws Exception {
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(servletContext);
	}

	private void setUpPowerMockForUtilClass() throws Exception {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class, "getServiceIdFormCode", "1").thenReturn(1);
		PowerMockito.when(Util.class, "getGameNumberFromGameId", 1).thenReturn(1);
		PowerMockito.when(Util.class, "getGameDisplayName", 1).thenReturn("KenoTwo");
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt", 2).thenReturn(1);
		PowerMockito.when(Util.class, "getGamenoFromTktnumber", "20").thenReturn(2);
		PowerMockito.when(Util.class, "getGameIdFromGameNumber", 2).thenReturn(1);
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt",258252).thenReturn(20);
	}

}
