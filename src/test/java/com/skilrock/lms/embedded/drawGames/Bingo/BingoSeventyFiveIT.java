package com.skilrock.lms.embedded.drawGames.Bingo;

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
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.embedded.drawGames.Bingo.Beans.BingoSeventyFive;
import com.skilrock.lms.embedded.drawGames.Bingo.Controller.BingoSeventyFiveAction;
import com.skilrock.lms.embedded.drawGames.Bingo.Service.BingoService;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ServletActionContext.class, Util.class, TransactionManager.class, Utility.class })
public class BingoSeventyFiveIT {

	private BingoSeventyFiveAction bingoSeventyFive = null;
	private UserInfoBean userBean = null;
	private HttpServletResponse response = null;
	private ServletContext servletContext = null;
	private HttpSession session = null;
	private HttpServletRequest request;
	private BingoService bingoServiceHelper;
	
	@Before
	public void setUpDependency() throws Exception {
		setMocking();
		setPowerMocking();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_1BetWithManualForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_1BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}	
	
	@Test
	public void purchaseTicketProcessForPerm_1BetWithQpForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_1BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_2BetWithManualForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_2BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_2BetWithQpForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_2BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_3BetWithManualForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_3BetWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForPerm_3BetWithQpForSale() throws Exception {
		setBingoSeventyFiveDataForPerm_3BetWithQp();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForAdvancePalyBetWithManualForSale() throws Exception {
		setBingoSeventyFiveDataForAdvancePlayWithManual();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
	
	@Test
	public void purchaseTicketProcessForAdvancePalyBetWithQpForSale() throws Exception {
		setBingoSeventyFiveDataForAdvancePlayWithQP();
		setUserInfoBeanData();
		settingMockingDataForServletContext();	
		validateResponses();
	}
    
	private void validateResponses() throws Exception {
		validatebingoSeventyFiveResponse();		
//		validateKenoPurchaseBeanOnCommonPurchaseHelper();
	}
	
	private void validatebingoSeventyFiveResponse() throws Exception {
		bingoSeventyFive.purchaseTicketProcess();
		Assert.assertEquals(bingoSeventyFive.getFinalPurchaseData(),
				"TicketNo:7782313201324401700|brCd:7782313201324401700|Date:2016-11-15|Time:17:44:13|PlayType:Perm1|Num:EEE|PanelPrice:1|QP:2|TicketCost:20.0|DrawDate:2016-11-15|DrawTime:18:10:00|DrawDate:2016-11-15|DrawTime:20:10:00|balance:4800.00|");
	}

	private void validateKenoPurchaseBeanOnCommonPurchaseHelper() throws Exception {
		// Setup To validate KenoPurchaseBean Before Calling Helper
		ArgumentCaptor<BingoSeventyFive> bingoSeventyFive = ArgumentCaptor.forClass(BingoSeventyFive.class);
		Mockito.verify(bingoServiceHelper).commonPurchseProcessBingoSeventyFive(Mockito.any(UserInfoBean.class),
				bingoSeventyFive.capture());
		// Validating KenoPurchaseBean Data After Calling Helper
		BingoSeventyFive helperBingoBean = bingoSeventyFive.getValue();
		Assert.assertNotNull(helperBingoBean.getBarcodeType());
		Assert.assertNotNull(helperBingoBean.getNoPicked());
		Assert.assertNotNull(helperBingoBean.getUserId());
		Assert.assertNotNull(helperBingoBean.getNoOfPanel());
	}

			
	private void setBingoSeventyFiveDataForPerm_1BetWithManual() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);	
		bingoSeventyFive.setPickedNumbers("01,02,03,04,05,06,07,08,09,10");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("1.00");
		bingoSeventyFive.setNoPicked("10");
		bingoSeventyFive.setPlayType("Perm1");
		bingoSeventyFive.setQP("2");		
		bingoSeventyFive.setBetAmt("1");
		bingoSeventyFive.setIsAdvancedPlay(0);
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setLSTktNo(0);			
	}
	
	private void setBingoSeventyFiveDataForPerm_1BetWithQp() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);			
		bingoSeventyFive.setPickedNumbers("QP");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("1.00");
		bingoSeventyFive.setNoPicked("10");
		bingoSeventyFive.setPlayType("Perm1");
		bingoSeventyFive.setQP("1");		
		bingoSeventyFive.setBetAmt("1");
		bingoSeventyFive.setIsAdvancedPlay(0);
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setLSTktNo(0);			
	}
	
	private void setBingoSeventyFiveDataForPerm_2BetWithManual() {
		bingoSeventyFive = new BingoSeventyFiveAction();	
		bingoSeventyFive.setPickedNumbers("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("105.00");
		bingoSeventyFive.setNoPicked("15");
		bingoSeventyFive.setPlayType("Perm2");
		bingoSeventyFive.setQP("2");		
		bingoSeventyFive.setBetAmt("10");
		bingoSeventyFive.setIsAdvancedPlay(0);
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setLSTktNo(0);			
	}
	
	private void setBingoSeventyFiveDataForPerm_2BetWithQp() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);			
		bingoSeventyFive.setPickedNumbers("QP");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("28.00");
		bingoSeventyFive.setBetAmt("10");
		bingoSeventyFive.setNoPicked("8");
		bingoSeventyFive.setPlayType("Perm2");
		bingoSeventyFive.setQP("1");		
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setLSTktNo(0);
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setGameId(1);		
	}
	
	private void setBingoSeventyFiveDataForPerm_3BetWithManual() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);		
		bingoSeventyFive.setPickedNumbers("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("45.50");
		bingoSeventyFive.setNoPicked("15");
		bingoSeventyFive.setPlayType("Perm3");
		bingoSeventyFive.setQP("2");		
		bingoSeventyFive.setBetAmt("1");
		bingoSeventyFive.setIsAdvancedPlay(0);
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setLSTktNo(0);			
	}	  
	
	private void setBingoSeventyFiveDataForPerm_3BetWithQp() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);		
		bingoSeventyFive.setPickedNumbers("QP");
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setTotalPurchaseAmt("28.60");
		bingoSeventyFive.setBetAmt("1");
		bingoSeventyFive.setNoPicked("13");
		bingoSeventyFive.setPlayType("Perm3");
		bingoSeventyFive.setQP("1");		
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setLSTktNo(0);
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
		bingoSeventyFive.setGameId(1);		
	}
	
	private void setBingoSeventyFiveDataForAdvancePlayWithManual() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);			
		bingoSeventyFive.setPickedNumbers("01,02,03,04,05,06,07,08,09,10Nxt01,02,03,04,05,06,07,08,09,10,11,12,13,14,15");
		bingoSeventyFive.setNoOfDraws(2);
		bingoSeventyFive.setTotalPurchaseAmt("41.00");
		bingoSeventyFive.setNoPicked("10Nxt15");
		bingoSeventyFive.setPlayType("Perm1NxtPerm2");
		bingoSeventyFive.setQP("2Nxt2");
		bingoSeventyFive.setBetAmt("10Nxt1");
		bingoSeventyFive.setIsAdvancedPlay(1);
		bingoSeventyFive.setDrawIdArr(new String[] { "74680", "74681"});
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setLSTktNo(0);		
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
	}
	
	private void setBingoSeventyFiveDataForAdvancePlayWithQP() {
		bingoSeventyFive = new BingoSeventyFiveAction(bingoServiceHelper);			
		bingoSeventyFive.setPickedNumbers("QPNxtQPNxtQP");
		bingoSeventyFive.setNoOfDraws(3);
		bingoSeventyFive.setTotalPurchaseAmt("112.20");
		bingoSeventyFive.setNoPicked("10Nxt8Nxt9");
		bingoSeventyFive.setPlayType("Perm1NxtPerm2NxtPerm3");
		bingoSeventyFive.setQP("1Nxt1Nxt1");
		bingoSeventyFive.setBetAmt("1Nxt10Nxt1");
		bingoSeventyFive.setIsAdvancedPlay(1);
		bingoSeventyFive.setDrawIdArr(new String[] { "74680", "74681", "74682"});
		bingoSeventyFive.setUserName("testret");
		bingoSeventyFive.setGameId(1);
		bingoSeventyFive.setLSTktNo(0);		
		bingoSeventyFive.setServletResponse(response);
		bingoSeventyFive.setServletRequest(request);
	}		
	
	private void setUserInfoBeanData() {
		userBean = new UserInfoBean();
		userBean.setCurrentUserMappingId(1);
		userBean.setAvailableCreditLimit(5000.00);
		userBean.setClaimableBal(200.0);
	}

	private void setMocking() throws Exception {
		setUpMockingForHttpServletResponse();
		servletContext = Mockito.mock(ServletContext.class);
		session = Mockito.mock(HttpSession.class);
		setUpMockingForHttpServletRequest();
//		setUpMockingForBingoService();
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

	private void setUpMockingForBingoService() throws Exception {
		bingoServiceHelper = Mockito.mock(BingoService.class);
		Mockito.when(bingoServiceHelper.commonPurchseProcessBingoSeventyFive(Mockito.any(UserInfoBean.class), Mockito.any(BingoSeventyFive.class) )).thenReturn(bingoSeventyFive());
	}

	private BingoSeventyFive bingoSeventyFive() {
		BingoSeventyFive bingoSeventyFive = new BingoSeventyFive();
		setUpBingoSeventyFiveBean(bingoSeventyFive);
		return bingoSeventyFive;
	}

	private void setUpBingoSeventyFiveBean(BingoSeventyFive bingoSeventyFive) {
		bingoSeventyFive.setSaleStatus("SUCCESS");
		bingoSeventyFive.setTicket_no("778231320132440170");
		bingoSeventyFive.setBarcodeType("77823132013244017010");
		bingoSeventyFive.setPlayType(new String[] { "Perm1" });
		bingoSeventyFive.setIsQuickPick(new int[] { 2 });
		Map<String, List<String>> map = new HashMap<String, List<String>>();		
		bingoSeventyFive.setAdvMsg(map);
		List<String> drawDateTime = new ArrayList<String>();
		drawDateTime.add("2016-11-15 18:10:00");
		drawDateTime.add("2016-11-15 20:10:00");		
		bingoSeventyFive.setDrawDateTime(drawDateTime);		
		bingoSeventyFive.setPurchaseTime("2016-11-15 17:44:13");
		bingoSeventyFive.setActionName("Action");
		bingoSeventyFive.setReprintCount("0");
		bingoSeventyFive.setBarcodeCount((short) 0);
		bingoSeventyFive.setNoOfPanel(1);
		bingoSeventyFive.setTotalPurchaseAmt(20.00);
		bingoSeventyFive.setUnitPrice(new double[] { 1.0 });
		bingoSeventyFive.setBetAmountMultiple(new int[] { 1 });
		bingoSeventyFive.setNoOfLines(new int[] { 1 });
		bingoSeventyFive.setNoOfDraws(1);
		bingoSeventyFive.setPlayerData(new String[] { "EEE" });
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
		setUpPowerMockForUtilClass();
		setUpPowerMockForUtilityClass();
		setUpPowerMockingForServletActionContext();
		PowerMockito.mockStatic(TransactionManager.class);
	}

	private void setUpPowerMockingForServletActionContext() throws Exception {
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(servletContext);
	}

	private void setUpPowerMockForUtilClass() throws Exception {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class, "getServiceIdFormCode", "1").thenReturn(1);
		PowerMockito.when(Util.class, "getGameNumberFromGameId", 1).thenReturn(75);
		PowerMockito.when(Util.class, "getGameDisplayName", 1).thenReturn("Bingo SeventyFive");
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt", 2).thenReturn(1);
		PowerMockito.when(Util.class, "getGamenoFromTktnumber", "75").thenReturn(2);
		PowerMockito.when(Util.class, "getGameIdFromGameNumber", 21).thenReturn(1);
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt",258252).thenReturn(20);		
	}

	private void setUpPowerMockForUtilityClass() throws Exception {
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", "MOBILE_NO_WLS").thenReturn("1234567896");
	}

}
