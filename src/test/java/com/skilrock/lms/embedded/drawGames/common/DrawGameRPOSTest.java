package com.skilrock.lms.embedded.drawGames.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({ "com.skilrock.lms.web.drawGames.common.Util", "com.skilrock.lms.common.Utility", "com.skilrock.lms.web.drawGames.common.UtilApplet" })
@PrepareForTest({ServletActionContext.class})
public class DrawGameRPOSTest {

	private DrawGameRPOSHelper drawGameRPOSHelper = Mockito.mock(DrawGameRPOSHelper.class);
	private ReprintFactory reprintFactory = Mockito.mock(ReprintFactory.class);
	private DrawGameRPOS drawGameRPOS = new DrawGameRPOS(drawGameRPOSHelper,reprintFactory);
	private ReprintContext reprintContext = Mockito.mock(ReprintContext.class);
	private ServletContext servletContext = Mockito.mock(ServletContext.class);
	private UserInfoBean userInfoBean = new UserInfoBean();
	private RafflePurchaseBean rafflePurchaseBean = new RafflePurchaseBean();
	private KenoPurchaseBean kenoPurchaseBean = new KenoPurchaseBean();
	Map currentUserSessionMap = new HashMap();
	HttpServletResponse httpServletResponse;
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
	HttpSession httpSession = Mockito.mock(HttpSession.class);
	private static final String REPRINT_RESPONSE_IF_IS_DRAW_IS_NO = "ErrorMsg:Draw Game Not Available|ErrorCode:139|";
	private static final String REPRINT_RESPONSE_LAST_TRANSACTION_NOT_SALE = "ErrorMsg:Last Transaction Not Sale|ErrorCode:154|";
	private static final String CORRECT_REPRINT_RESPONSE = "TicketNo:790932254511560101|brCd:790932254511560101|Date:2015-09-11|Time:08:41:58|PlayType:Direct2|Num:01,02|PanelPrice:10.0|QP:2|TicketCost:10.0|DrawDate:2015-10-01|DrawTime:19:45:00*FORTUNE|DrawId:515|balance:185723.15|QP:2|";
	private static final String RAFFLE_REPRINT_RESPONSE = "RaffleData:TicNo:790932254511560101|brCd:7909322545115601010|Date:2015-10-01|Time:08:41:58|GameName:Raffle|DrawDate:2015-10-01|Time:08:41:58|DrawName:RaffleDraw";
	private static final String DRAW_PERFORMED_RESPONSE = "ErrorMsg:Draw Performed|ErrorCode:152";
	
	@Test
	public void reprintTicket_ReturnGameNotAvailableIfIsDrawNO() throws Exception{
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(servletContext);
		Mockito.when(servletContext.getAttribute("IS_DRAW")).thenReturn("NO");
		callReprintTicket();
		Assert.assertEquals(REPRINT_RESPONSE_IF_IS_DRAW_IS_NO, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnLastTransactionNotSaleIfLMSExceptionThrown() throws Exception{
		mockRequiredFunctions();
		Mockito.doThrow(new LMSException()).when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		callReprintTicket();
		Assert.assertEquals(REPRINT_RESPONSE_LAST_TRANSACTION_NOT_SALE, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnLastTransactionNotSaleIfExceptionOccurred() throws Exception{
		mockRequiredFunctions();
		userInfoBean.setClaimableBal(233.00);
		Object gameBean = new Object();
		Mockito.doNothing().when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		callReprintTicket();
		Assert.assertEquals(REPRINT_RESPONSE_LAST_TRANSACTION_NOT_SALE, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnRaffleReprintResponseForRafflePurchaseBean() throws Exception{
		userInfoBean.setAvailableCreditLimit(12300.00);
		userInfoBean.setClaimableBal(233.00);
		mockRequiredFunctions();
		Object gameBean = (Object) rafflePurchaseBean;
		Mockito.doNothing().when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(reprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(reprintContext);
		Mockito.when(reprintContext.reprintTicket(Mockito.any(EmbeddedReprint.class))).thenReturn(RAFFLE_REPRINT_RESPONSE);
		callReprintTicket();
		Assert.assertEquals(RAFFLE_REPRINT_RESPONSE, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnDrawPerformedIfSaleStatusIsPerformed() throws Exception{
		userInfoBean.setAvailableCreditLimit(12300.00);
		userInfoBean.setClaimableBal(233.00);
		mockRequiredFunctions();
		Object bean = (Object) rafflePurchaseBean;
		Mockito.doNothing().when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(bean);
		Mockito.when(reprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(reprintContext);
		Mockito.when(reprintContext.reprintTicket(Mockito.any(EmbeddedReprint.class))).thenReturn(DRAW_PERFORMED_RESPONSE);
		callReprintTicket();
		Assert.assertEquals(DRAW_PERFORMED_RESPONSE, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnLastTransactionNotSaleForDefaultCaseReturnedFromReprintFactory() throws Exception{
		userInfoBean.setAvailableCreditLimit(12300.00);
		userInfoBean.setClaimableBal(233.00);
		mockRequiredFunctions();
		Object gameBean = (Object) kenoPurchaseBean;
		Mockito.doNothing().when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(reprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenThrow(new LMSException(EmbeddedErrors.REPRINT_FAIL_ERROR_CODE,EmbeddedErrors.REPRINT_FAIL));
		callReprintTicket();
		Assert.assertEquals(REPRINT_RESPONSE_LAST_TRANSACTION_NOT_SALE, drawGameRPOS.finalReprintData);
	}

	private void callReprintTicket() throws IOException, Exception {
		ServletOutputStream write = mockHttpServletResponse();
		Mockito.when(httpServletResponse.getOutputStream()).thenReturn(write);
		setGlobalVariables();
		drawGameRPOS.reprintTicket();
	}
	
	@Test
	public void reprintTicket_ReturnCorrectReprintResponseForKenoPurchaseBean() throws Exception{
		long lastTicketNumber = 1122554488;
		userInfoBean.setAvailableCreditLimit(12300.00);
		userInfoBean.setClaimableBal(233.00);
		mockRequiredFunctions();
		Object gameBean = (Object) kenoPurchaseBean;
		Mockito.doNothing().when(drawGameRPOSHelper).checkLastPrintedTicketStatusAndUpdate(Mockito.any(UserInfoBean.class), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.when(drawGameRPOSHelper.reprintTicket(Mockito.any(UserInfoBean.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString())).thenReturn(gameBean);
		Mockito.when(reprintFactory.fetchReprintGameTypeInstance(Mockito.anyString())).thenReturn(reprintContext);
		Mockito.when(reprintContext.reprintTicket(Mockito.any(EmbeddedReprint.class))).thenReturn(CORRECT_REPRINT_RESPONSE);
		ServletOutputStream write = mockHttpServletResponse();
		Mockito.when(httpServletResponse.getOutputStream()).thenReturn(write);
		setGlobalVariables();
		drawGameRPOS.setLSTktNo(lastTicketNumber);
		drawGameRPOS.reprintTicket();
		Assert.assertEquals(CORRECT_REPRINT_RESPONSE, drawGameRPOS.finalReprintData);
	}
	
	@Test
	public void reprintTicket_ReturnLastTransactionNotSaleIfServletContextIsNull() throws Exception{
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(null);
		callReprintTicket();
		Assert.assertEquals(REPRINT_RESPONSE_LAST_TRANSACTION_NOT_SALE, drawGameRPOS.finalReprintData);
	}
	
	private void setGlobalVariables() {
		drawGameRPOS.setUserName("testret");
		drawGameRPOS.setLSTktNo(0);
		drawGameRPOS.setServletResponse(httpServletResponse);
	}

	private void mockRequiredFunctions() throws Exception {
		httpSession.setAttribute("USER_INFO", userInfoBean);
		currentUserSessionMap.put("testret", httpSession);
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(servletContext);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt",Mockito.anyInt()).thenReturn(1546161);
		PowerMockito.when(Util.class,"getGameIdFromGameNumber",Mockito.anyInt()).thenReturn(1);
		Mockito.mock(ActionContext.class, "reprintTicket");
		Mockito.when(servletContext.getAttribute("IS_DRAW")).thenReturn("YES");
		Mockito.when(servletContext.getAttribute("COUNTRY_DEPLOYED")).thenReturn("ZIM");
		Mockito.when(servletContext.getAttribute("LOGGED_IN_USERS")).thenReturn(currentUserSessionMap);
		Mockito.when(servletContext.getAttribute("REF_MERCHANT_ID")).thenReturn("11025");
		Mockito.when(servletContext.getAttribute("AUTO_CANCEL_CLOSER_DAYS")).thenReturn("25");
		Mockito.when(httpSession.getAttribute("USER_INFO")).thenReturn(userInfoBean);
	}

	private ServletOutputStream mockHttpServletResponse() {
		ServletOutputStream write = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				
			}
		};
		httpServletResponse = Mockito.mock(HttpServletResponse.class);
		return write;
	}
	
}
