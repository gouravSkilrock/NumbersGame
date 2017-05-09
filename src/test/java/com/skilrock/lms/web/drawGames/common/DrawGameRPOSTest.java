package com.skilrock.lms.web.drawGames.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({ "com.skilrock.lms.web.drawGames.common.Util",
		"com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber" })
@PrepareForTest({ ServletActionContext.class,LMSUtility.class,CookieMgmtForTicketNumber.class })
public class DrawGameRPOSTest {

	private DrawGameRPOSHelper drawGameRPOSHelper = Mockito.mock(DrawGameRPOSHelper.class);
	private WebReprintFactory webReprintFactory = Mockito.mock(WebReprintFactory.class);
	private WebReprintContext webReprintContext = Mockito.mock(WebReprintContext.class);
	private HttpSession httpSession = Mockito.mock(HttpSession.class);
	private ServletContext servletContext = Mockito.mock(ServletContext.class);
	private UserInfoBean userInfoBean = new UserInfoBean();
	private HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
	private HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	private Map currentUserSessionMap = new HashMap<>();
	private DrawGameRPOS drawGameRPOS = new DrawGameRPOS(drawGameRPOSHelper,webReprintFactory){
		public HttpSession getSession(String userName){
			return httpSession;
		}
		public UserInfoBean getUserInfoBean(String userName){
			return userInfoBean;
		}
	};
	private DrawGameRPOS drawGameRPOSForSessionTimeOutTest = new DrawGameRPOS(drawGameRPOSHelper,webReprintFactory);
	private String errorJsonResponse = "{\"isSuccess\":false,\"errorCode\":2002,\"errorMsg\":\"Some Internal Error !\"}";
	private String requestJson = "{\"userName\":\"abcd\"}";
	private String sessionTimeOutJsonResponse = "{\"isSuccess\":false,\"errorCode\":118,\"errorMsg\":\"Time Out. Login Again\"}";
	private String fortuneResponseJson = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonReprintData\":{\"ticketNumber\":\"1234185698132168198\",\"reprintCount\":0,\"barcodeCount\":\"12341856981321681980005865\",\"gameName\":\"1/12 Zodiac\",\"gameDevName\":\"oneToTwelve\",\"purchaseDate\":\"2017-02-04\",\"purchaseTime\":\"17:30:00\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2017-02-05\",\"drawTime\":\"18:00:00\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"oneToTwelve\",\"pickedNumbers\":\"01\",\"unitPrice\":0.5,\"noOfLines\":\"1\",\"betAmtMul\":1,\"panelPrice\":2}],\"orgName\":\"Testret\",\"userName\":\"testret\",\"parentOrgName\":\"TestAgent\"},\"isPromo\":false}";
	private String kenoResponseJson = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonReprintData\":{\"ticketNumber\":\"1234185698132168198\",\"reprintCount\":0,\"barcodeCount\":\"12341856981321681980005865\",\"gameName\":\"10/20\",\"gameDevName\":\"tenByTwenty\",\"purchaseDate\":\"2017-02-04\",\"purchaseTime\":\"17:30:00\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2017-02-05\",\"drawTime\":\"18:00:00\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"Direct10\",\"pickedNumbers\":\"01,02,03,04,05,06,07,08,09,10\",\"unitPrice\":0.5,\"noOfLines\":\"1\",\"betAmtMul\":1,\"panelPrice\":2}],\"orgName\":\"Testret\",\"userName\":\"testret\",\"parentOrgName\":\"TestAgent\"},\"isPromo\":false}";
	private String kenoTwoResponseJson = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonReprintData\":{\"ticketNumber\":\"1234185698132168198\",\"reprintCount\":0,\"barcodeCount\":\"12341856981321681980005865\",\"gameName\":\"5/90\",\"gameDevName\":\"kenoTwo\",\"purchaseDate\":\"2017-02-04\",\"purchaseTime\":\"17:30:00\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2017-02-05\",\"drawTime\":\"18:00:00\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"Direct5\",\"pickedNumbers\":\"01,02,03,04,05\",\"unitPrice\":0.5,\"noOfLines\":\"1\",\"betAmtMul\":1,\"panelPrice\":2}],\"orgName\":\"Testret\",\"userName\":\"testret\",\"parentOrgName\":\"TestAgent\"},\"isPromo\":false}";
	private String twelveByTwentyFourResponseJson = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonReprintData\":{\"ticketNumber\":\"1234185698132168198\",\"reprintCount\":0,\"barcodeCount\":\"12341856981321681980005865\",\"gameName\":\"12/24\",\"gameDevName\":\"twelveByTwentyFour\",\"purchaseDate\":\"2017-02-04\",\"purchaseTime\":\"17:30:00\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2017-02-05\",\"drawTime\":\"18:00:00\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"Direct12\",\"pickedNumbers\":\"01,02,03,04,05,06,07,08,09,10,11,12\",\"unitPrice\":0.5,\"noOfLines\":\"1\",\"betAmtMul\":1,\"panelPrice\":2}],\"orgName\":\"Testret\",\"userName\":\"testret\",\"parentOrgName\":\"TestAgent\"},\"isPromo\":false}";
	private String lottoResponseJson = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonReprintData\":{\"ticketNumber\":\"1234185698132168198\",\"reprintCount\":0,\"barcodeCount\":\"12341856981321681980005865\",\"gameName\":\"6/42\",\"gameDevName\":\"zimLottoBonus\",\"purchaseDate\":\"2017-02-04\",\"purchaseTime\":\"17:30:00\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2017-02-05\",\"drawTime\":\"18:00:00\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"Direct6\",\"pickedNumbers\":[\"01,02,03,04,05,06\"],\"unitPrice\":0.5,\"noOfLines\":\"1\",\"betAmtMul\":1,\"panelPrice\":2}],\"orgName\":\"Testret\",\"userName\":\"testret\",\"parentOrgName\":\"TestAgent\"},\"isPromo\":false}";
	private JSONObject errorCaseResponse = new JSONObject();
	private JSONObject fortuneActualResponse = new JSONObject();
	private JSONObject kenoActualResponse = new JSONObject();
	private JSONObject kenoTwoActualResponse = new JSONObject();
	private JSONObject twelveByTwentyFourActualResponse = new JSONObject();
	private JSONObject lottoActualResponse = new JSONObject();

	@Test
	public void reprintTicket_ReturnsErrorJsonResponseIfNoSessionFound() throws IOException{
		drawGameRPOS.setJson(null);
		getOutputWriter();
		drawGameRPOS.setServletResponse(httpServletResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(errorJsonResponse, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsErrorJsonResponseIfNoUserInformationInSession() throws Exception{
		drawGameRPOS.setJson(null);
		mockRequiredClasses();
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(errorJsonResponse, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsSessionTimeOutJsonResponse() throws IOException{
		drawGameRPOSForSessionTimeOutTest.setJson(requestJson);
		getOutputWriter();
		drawGameRPOSForSessionTimeOutTest.setServletRequest(httpServletRequest);
		drawGameRPOSForSessionTimeOutTest.setServletResponse(httpServletResponse);
		Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
		drawGameRPOSForSessionTimeOutTest.reprintTicket();
		Assert.assertEquals(sessionTimeOutJsonResponse, drawGameRPOSForSessionTimeOutTest.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsErrorJsonResponseIfExceptionThrownFromInsertEntryIntoPrintedTicketTable() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doThrow(new Exception()).when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(errorJsonResponse, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsErrorJsonResponseIfReprintTicketReturnsNull() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		mockRequiredClasses();
		prepareErrorJsonResponse();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(errorCaseResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(errorJsonResponse, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsFortuneResponseJsonIfReprintTicketReturnsFortunePurchaseBean() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new FortunePurchaseBean();
		prepareFortunePurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(fortuneActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(fortuneResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsKenoResponseJsonIfReprintTicketReturnsKenoPurchaseBean() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new KenoPurchaseBean();
		prepareKenoPurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(kenoActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(kenoResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsKenoTwoResponseJsonIfReprintTicketReturnsKenoPurchaseBean() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new KenoPurchaseBean();
		prepareKenoTwoPurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(kenoTwoActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(kenoTwoResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsTwelveByTwentyFourResponseJsonIfReprintTicketReturnsTwelveByTwentyFourPurchaseBean() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new KenoPurchaseBean();
		prepareTwelveByTwentyFourJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(twelveByTwentyFourActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(twelveByTwentyFourResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsLottoResponseJsonIfReprintTicketReturnsLottoPurchaseBean() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new KenoPurchaseBean();
		prepareLottoPurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(lottoActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(lottoResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsFortuneResponseJsonIfReprintTicketReturnsFortunePurchaseBeanIfJsonNotNull() throws Exception{
		drawGameRPOS.setJson(requestJson);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new FortunePurchaseBean();
		prepareFortunePurchaseBeanJsonResponse();
		mockRequiredClasses();
		currentUserSessionMap.put("testret", httpSession);
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(fortuneActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(fortuneResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsFortuneResponseJsonIfReprintTicketReturnsFortunePurchaseBeanWhenTicketNumberNotNull() throws Exception{
		drawGameRPOS.setJson(null);
		drawGameRPOS.setTicketNo("465465464564546598484");
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new FortunePurchaseBean();
		prepareFortunePurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long) 49684984);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(fortuneActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(fortuneResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	@Test
	public void reprintTicket_ReturnsFortuneResponseJsonIfReprintTicketReturnsFortunePurchaseBeanWhenLastTicketNumberIsZero() throws Exception{
		drawGameRPOS.setJson(null);
		userInfoBean.setUserId(1);
		userInfoBean.setUserName("ABCD");
		httpSession.setAttribute("USER_INFO", userInfoBean);
		Object gameBean = (Object) new FortunePurchaseBean();
		prepareFortunePurchaseBeanJsonResponse();
		mockRequiredClasses();
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
		Mockito.mock(ActionContext.class, "reprintTicket");
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class)).thenReturn((long)0);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class,"getDivValueForLastSoldTkt",Matchers.any(Integer.class)).thenReturn(12);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Matchers.anyInt()).thenReturn(1);
		Mockito.doNothing().when(drawGameRPOSHelper).insertEntryIntoPrintedTktTableForWeb(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.anyString());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(webReprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(webReprintContext);
		Mockito.when(webReprintContext.reprintTicket(Mockito.any(WebReprint.class))).thenReturn(fortuneActualResponse);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(fortuneResponseJson, drawGameRPOS.jsonResponse.toString());
	}
	
	private void mockRequiredClasses() throws IOException{
		getOutputWriter();
		drawGameRPOS.setServletRequest(httpServletRequest);
		drawGameRPOS.setServletResponse(httpServletResponse);
		Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
	}
	

	private void getOutputWriter() throws IOException {
		Writer write = new Writer() {
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
			}

			@Override
			public void flush() throws IOException {
			}

			@Override
			public void close() throws IOException {
			}
		};
		PrintWriter out = new PrintWriter(write);
		Mockito.when(httpServletResponse.getWriter()).thenReturn(out);
	}
	
	private void prepareErrorJsonResponse(){
		errorCaseResponse.put("isSuccess", false);
		errorCaseResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		errorCaseResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	
	private void prepareFortunePurchaseBeanJsonResponse(){
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		betTypeDataRes = new JSONObject();
		betTypeDataRes.put("isQp", false);
		betTypeDataRes.put("betName", "oneToTwelve");
		betTypeDataRes.put("pickedNumbers", "01");
		betTypeDataRes.put("unitPrice", 0.5);
		betTypeDataRes.put("noOfLines", "1");
		betTypeDataRes.put("betAmtMul", 1);
		betTypeDataRes.put("panelPrice", 2);
		betTypeArray.add(betTypeDataRes);
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		drawData = new JSONObject();
		drawData.put("drawDate", "2017-02-05");
		drawData.put("drawTime", "18:00:00");
		drawDataArray.add(drawData);
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", "1234185698132168198");
		commonSaleDataRes.put("reprintCount", 0);
		commonSaleDataRes.put("barcodeCount","12341856981321681980005865");
		commonSaleDataRes.put("gameName", "1/12 Zodiac");
		commonSaleDataRes.put("gameDevName", "oneToTwelve");
		commonSaleDataRes.put("purchaseDate", "2017-02-04");
		commonSaleDataRes.put("purchaseTime", "17:30:00");
		commonSaleDataRes.put("purchaseAmt", 2.00);
		commonSaleDataRes.put("drawData", drawDataArray);
		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", null);
		mainData.put("orgName", "Testret");
		mainData.put("userName", "testret");
		mainData.put("parentOrgName", "TestAgent");
		fortuneActualResponse.put("isSuccess", true);
		fortuneActualResponse.put("errorMsg", "");
		fortuneActualResponse.put("mainData", mainData);
		fortuneActualResponse.put("isPromo", false);
	}
	
	private void prepareKenoPurchaseBeanJsonResponse(){
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		betTypeDataRes = new JSONObject();
		betTypeDataRes.put("isQp", false);
		betTypeDataRes.put("betName", "Direct10");
		betTypeDataRes.put("pickedNumbers", "01,02,03,04,05,06,07,08,09,10");
		betTypeDataRes.put("unitPrice", 0.5);
		betTypeDataRes.put("noOfLines", "1");
		betTypeDataRes.put("betAmtMul", 1);
		betTypeDataRes.put("panelPrice", 2);
		betTypeArray.add(betTypeDataRes);
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		drawData = new JSONObject();
		drawData.put("drawDate", "2017-02-05");
		drawData.put("drawTime", "18:00:00");
		drawDataArray.add(drawData);
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", "1234185698132168198");
		commonSaleDataRes.put("reprintCount", 0);
		commonSaleDataRes.put("barcodeCount","12341856981321681980005865");
		commonSaleDataRes.put("gameName", "10/20");
		commonSaleDataRes.put("gameDevName", "tenByTwenty");
		commonSaleDataRes.put("purchaseDate", "2017-02-04");
		commonSaleDataRes.put("purchaseTime", "17:30:00");
		commonSaleDataRes.put("purchaseAmt", 2.00);
		commonSaleDataRes.put("drawData", drawDataArray);
		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", null);
		mainData.put("orgName", "Testret");
		mainData.put("userName", "testret");
		mainData.put("parentOrgName", "TestAgent");
		kenoActualResponse.put("isSuccess", true);
		kenoActualResponse.put("errorMsg", "");
		kenoActualResponse.put("mainData", mainData);
		kenoActualResponse.put("isPromo", false);
	}
	
	private void prepareLottoPurchaseBeanJsonResponse(){
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		List<String> pickedNumbers = new ArrayList<>();
		pickedNumbers.add("01,02,03,04,05,06");
		betTypeDataRes = new JSONObject();
		betTypeDataRes.put("isQp", false);
		betTypeDataRes.put("betName", "Direct6");
		betTypeDataRes.put("pickedNumbers", pickedNumbers);
		betTypeDataRes.put("unitPrice", 0.5);
		betTypeDataRes.put("noOfLines", "1");
		betTypeDataRes.put("betAmtMul", 1);
		betTypeDataRes.put("panelPrice", 2);
		betTypeArray.add(betTypeDataRes);
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		drawData = new JSONObject();
		drawData.put("drawDate", "2017-02-05");
		drawData.put("drawTime", "18:00:00");
		drawDataArray.add(drawData);
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", "1234185698132168198");
		commonSaleDataRes.put("reprintCount", 0);
		commonSaleDataRes.put("barcodeCount","12341856981321681980005865");
		commonSaleDataRes.put("gameName", "6/42");
		commonSaleDataRes.put("gameDevName", "zimLottoBonus");
		commonSaleDataRes.put("purchaseDate", "2017-02-04");
		commonSaleDataRes.put("purchaseTime", "17:30:00");
		commonSaleDataRes.put("purchaseAmt", 2.00);
		commonSaleDataRes.put("drawData", drawDataArray);
		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", null);
		mainData.put("orgName", "Testret");
		mainData.put("userName", "testret");
		mainData.put("parentOrgName", "TestAgent");
		lottoActualResponse.put("isSuccess", true);
		lottoActualResponse.put("errorMsg", "");
		lottoActualResponse.put("mainData", mainData);
		lottoActualResponse.put("isPromo", false);
	}

	private void prepareKenoTwoPurchaseBeanJsonResponse(){
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		betTypeDataRes = new JSONObject();
		betTypeDataRes.put("isQp", false);
		betTypeDataRes.put("betName", "Direct5");
		betTypeDataRes.put("pickedNumbers", "01,02,03,04,05");
		betTypeDataRes.put("unitPrice", 0.5);
		betTypeDataRes.put("noOfLines", "1");
		betTypeDataRes.put("betAmtMul", 1);
		betTypeDataRes.put("panelPrice", 2);
		betTypeArray.add(betTypeDataRes);
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		drawData = new JSONObject();
		drawData.put("drawDate", "2017-02-05");
		drawData.put("drawTime", "18:00:00");
		drawDataArray.add(drawData);
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", "1234185698132168198");
		commonSaleDataRes.put("reprintCount", 0);
		commonSaleDataRes.put("barcodeCount","12341856981321681980005865");
		commonSaleDataRes.put("gameName", "5/90");
		commonSaleDataRes.put("gameDevName", "kenoTwo");
		commonSaleDataRes.put("purchaseDate", "2017-02-04");
		commonSaleDataRes.put("purchaseTime", "17:30:00");
		commonSaleDataRes.put("purchaseAmt", 2.00);
		commonSaleDataRes.put("drawData", drawDataArray);
		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", null);
		mainData.put("orgName", "Testret");
		mainData.put("userName", "testret");
		mainData.put("parentOrgName", "TestAgent");
		kenoTwoActualResponse.put("isSuccess", true);
		kenoTwoActualResponse.put("errorMsg", "");
		kenoTwoActualResponse.put("mainData", mainData);
		kenoTwoActualResponse.put("isPromo", false);
	}
	
	private void prepareTwelveByTwentyFourJsonResponse(){
		JSONArray betTypeArray = new JSONArray();
		JSONObject betTypeDataRes = null;
		betTypeDataRes = new JSONObject();
		betTypeDataRes.put("isQp", false);
		betTypeDataRes.put("betName", "Direct12");
		betTypeDataRes.put("pickedNumbers", "01,02,03,04,05,06,07,08,09,10,11,12");
		betTypeDataRes.put("unitPrice", 0.5);
		betTypeDataRes.put("noOfLines", "1");
		betTypeDataRes.put("betAmtMul", 1);
		betTypeDataRes.put("panelPrice", 2);
		betTypeArray.add(betTypeDataRes);
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		drawData = new JSONObject();
		drawData.put("drawDate", "2017-02-05");
		drawData.put("drawTime", "18:00:00");
		drawDataArray.add(drawData);
		JSONObject commonSaleDataRes = new JSONObject();
		commonSaleDataRes.put("ticketNumber", "1234185698132168198");
		commonSaleDataRes.put("reprintCount", 0);
		commonSaleDataRes.put("barcodeCount","12341856981321681980005865");
		commonSaleDataRes.put("gameName", "12/24");
		commonSaleDataRes.put("gameDevName", "twelveByTwentyFour");
		commonSaleDataRes.put("purchaseDate", "2017-02-04");
		commonSaleDataRes.put("purchaseTime", "17:30:00");
		commonSaleDataRes.put("purchaseAmt", 2.00);
		commonSaleDataRes.put("drawData", drawDataArray);
		JSONObject mainData = new JSONObject();
		mainData.put("commonReprintData", commonSaleDataRes);
		mainData.put("betTypeData", betTypeArray);
		mainData.put("advMessage", null);
		mainData.put("orgName", "Testret");
		mainData.put("userName", "testret");
		mainData.put("parentOrgName", "TestAgent");
		twelveByTwentyFourActualResponse.put("isSuccess", true);
		twelveByTwentyFourActualResponse.put("errorMsg", "");
		twelveByTwentyFourActualResponse.put("mainData", mainData);
		twelveByTwentyFourActualResponse.put("isPromo", false);
	}
	
}
