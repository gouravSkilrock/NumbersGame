package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({ "com.skilrock.lms.web.drawGames.common.Util", "com.skilrock.lms.common.Utility" ,"com.skilrock.lms.common.db.DBConnect"})
@PrepareForTest({ ServletActionContext.class, CookieMgmtForTicketNumber.class, Util.class, ActionContext.class,
		TransactionManager.class, Utility.class, DBConnect.class, CommonMethods.class })

public class KenoTwoActionTest {
	KenoPurchaseBean kenoBean = new KenoPurchaseBean();
	private ServletContext servletContext;
	private ActionContext context;
	private DrawGameRPOSHelper helper;
	HttpServletResponse response;
	HttpServletRequest httpServletRequest;
	HttpSession realsession;

	private KenoTwoAction oneMethod(String json) throws IOException, Exception {
		KenoTwoAction kenoTwoAction = mockedClasses();
		getOutputWriter();
		UserInfoBean userInfo = getUserInfoBean();
		PowerMockito.mockStatic(TransactionManager.class);
		powerMockForUtilityClass();
		kenoTwoAction.setServletRequest(httpServletRequest);
		kenoTwoAction.setServletResponse(response);
		kenoTwoAction.setJson(json);
		powermockUtilClassMethods();
		mockServletNActionContext();
		Map<Integer, Map<Integer, String>> drawIdTableMap = new HashMap<Integer, Map<Integer, String>>();
		prepareSessionRequestAndContext(userInfo, drawIdTableMap);
		PowerMockito.mockStatic(DBConnect.class);
		powermockCommonMethodClassMethods();
		return kenoTwoAction;
	}

	private void sendKenoPurchaseBean() throws Exception {
		Mockito.when(helper.commonPurchseProcessKenoTwo(Mockito.any(UserInfoBean.class),
				Mockito.any(KenoPurchaseBean.class))).thenReturn(kenoBean);
	}

	private void powermockCommonMethodClassMethods() throws Exception {
		PowerMockito.mockStatic(CommonMethods.class);
		PowerMockito
				.when(CommonMethods.class, "prepareSMSData", Matchers.any(byte[].class), Matchers.any(byte[].class),
						Matchers.any(StringBuilder.class), Matchers.any(StringBuilder.class), Matchers.any(List.class))
				.thenReturn("SMS");
		PowerMockito.when(CommonMethods.class, "sendSMS", Matchers.any(String.class)).thenReturn("Hello");
	}

	private void getOutputWriter() throws IOException {
		Writer write = new Writer() {
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				// TODO Auto-generated method stub
			}

			@Override
			public void flush() throws IOException {
				// TODO Auto-generated method stub
			}

			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
			}
		};
		PrintWriter out = new PrintWriter(write);
		Mockito.when(response.getWriter()).thenReturn(out);
	}

	private void mockServletNActionContext() throws Exception {
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito
				.when(CookieMgmtForTicketNumber.class, "getTicketNumberFromCookie",
						Matchers.any(HttpServletRequest.class), Matchers.any(UserInfoBean.class))
				.thenReturn((long) 70310200);
		PowerMockito.doNothing().when(CookieMgmtForTicketNumber.class);
		CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(httpServletRequest, response, null, null);
		PowerMockito.mockStatic(ServletActionContext.class);
		PowerMockito.mockStatic(ActionContext.class);
		PowerMockito.when(ServletActionContext.class, "getServletContext").thenReturn(servletContext);
		PowerMockito.when(ActionContext.class, "getContext").thenReturn(context);
	}

	private void prepareSessionRequestAndContext(UserInfoBean userInfo, Map<Integer, Map<Integer, String>> drawIdTableMap) {
		Mockito.when(httpServletRequest.getSession()).thenReturn(realsession);
		Mockito.when(realsession.getAttribute("USER_INFO")).thenReturn(userInfo);
		Mockito.when(httpServletRequest.getAttribute("code")).thenReturn("");
		Mockito.when(servletContext.getAttribute("drawIdTableMap")).thenReturn(drawIdTableMap);
		Mockito.when(servletContext.getAttribute("REF_MERCHANT_ID")).thenReturn("WGRL");
		Mockito.when(context.getName()).thenReturn("ActionNmae");
	}

	private KenoTwoAction mockedClasses() {
		servletContext = Mockito.mock(ServletContext.class);
		context = Mockito.mock(ActionContext.class);
		helper = Mockito.mock(DrawGameRPOSHelper.class);
		KenoTwoAction kenoTwoAction = new KenoTwoAction(helper);
		response = Mockito.mock(HttpServletResponse.class);
		httpServletRequest = Mockito.mock(HttpServletRequest.class);
		realsession = Mockito.mock(HttpSession.class);
		return kenoTwoAction;
	}

	private UserInfoBean getUserInfoBean() {
		UserInfoBean userInfo = new UserInfoBean();
		userInfo.setUserName("testret");
		userInfo.setUserType("RETAILER");
		userInfo.setUserOrgId(4);
		return userInfo;
	}

	private void powermockUtilClassMethods() throws Exception {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.class, "getDivValueForLastSoldTkt", Matchers.any(Integer.class)).thenReturn(100);
		PowerMockito.when(Util.class, "getGameIdFromGameNumber", Matchers.any(Integer.class)).thenReturn(8);
		PowerMockito.when(Util.class, "getCurrentTimeStamp")
				.thenReturn(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		PowerMockito.when(Util.class, "getServiceIdFormCode", Matchers.any(String.class)).thenReturn(5);
		PowerMockito.when(Util.class, "getGameId", Matchers.any(String.class)).thenReturn(1);
		PowerMockito.when(Util.class, "validateNumber", Matchers.any(Integer.class), Matchers.any(Integer.class),
				Matchers.any(String.class), Matchers.any(Boolean.class)).thenReturn(true);
		PowerMockito.doNothing().when(Util.class);
		Util.setEbetSaleRequestStatusDone(Matchers.any(String.class), Matchers.any(Integer.class));
	}

	private void powerMockForUtilityClass() throws Exception {
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", Matchers.any(String.class)).thenReturn("12345696");
	}

	private void prepareKenoPurchaseBean(KenoPurchaseBean kenoBean, boolean isQP) {
		kenoBean.setPurchaseTime("14 Nov 2016 00:00:00");
		kenoBean.setActionName("Action");
		List<String> drawDateTime = new ArrayList<String>();
		drawDateTime.add("14 Nov 2016 00:00:00");
		kenoBean.setDrawDateTime(drawDateTime);
		kenoBean.setTicket_no("123456789987654321");
		kenoBean.setReprintCount("0");
		kenoBean.setBarcodeCount((short) 0);
		kenoBean.setNoOfPanel(1);
		kenoBean.setPlayType(new String[] { "BetType" });
		kenoBean.setTotalPurchaseAmt(20.00);
		kenoBean.setUnitPrice(new double[] { 1.0 });
		kenoBean.setBetAmountMultiple(new int[] { 1 });
		kenoBean.setNoOfLines(new int[] { 1 });
		kenoBean.setNoOfDraws(1);
		kenoBean.setPlayerData(new String[] { "Numbers" });
		kenoBean.setIsQuickPick(new int[] {isQP ? 1 : 2});
		kenoBean.setNoPicked(new String[] { "11" });
	}

	private void setGameMap() {
		Map<Integer, GameMasterLMSBean> gameMap = new HashMap<Integer, GameMasterLMSBean>();
		Util.setGameMap(gameMap);
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm1QP() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"10\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"11,17,38,64,73,76,80,84,85,87\",\"betName\":\"Perm1\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm1Advance_QP() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":true,\"drawData\":[{\"drawId\":\"74716\"},{\"drawId\":\"74717\"}],\"noOfDraws\":2,\"isDrawManual\":false,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"10\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"02,05,13,16,18,31,32,66,74,85\",\"betName\":\"Perm1\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"2\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawId\":\"74716\",\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm1Manual() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"10\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"03,04,01,02,05,06,07,08,09,10\",\"betName\":\"Perm1\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm1AdvanceManual() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":true,\"drawData\":[{\"drawId\":\"74716\"},{\"drawId\":\"74717\"}],\"noOfDraws\":2,\"isDrawManual\":false,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"10\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"08,23,22,39,70,56,53,41,43,58\",\"betName\":\"Perm1\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"2\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawId\":\"74716\",\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm2QP() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"4\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"14,27,35,80\",\"betName\":\"Perm2\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.6\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm2Advance_QP()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":true,\"drawData\":[{\"drawId\":\"74716\"},{\"drawId\":\"74717\"}],\"noOfDraws\":2,\"isDrawManual\":false,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"7\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"07,11,14,42,62,67,72\",\"betName\":\"Perm2\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"4.2\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawId\":\"74716\",\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm2Manual() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"7\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"51,39,22,44,57,71,41\",\"betName\":\"Perm2\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"2.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());

	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm2AdvanceManual() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":true,\"drawData\":[{\"drawId\":\"74716\"},{\"drawId\":\"74717\"}],\"noOfDraws\":2,\"isDrawManual\":false,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"20\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"10,03,08,07,06,05,11,12,13,14,15,30,29,27,26,25,09,23,22,20\",\"betName\":\"Perm2\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"38\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawId\":\"74716\",\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());

	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm3QP() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"4\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"40,49,66,83\",\"betName\":\"Perm3\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.4\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true );
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm3Advance_QP()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":true,\"drawData\":[{\"drawId\":\"74716\"},{\"drawId\":\"74717\"}],\"noOfDraws\":2,\"isDrawManual\":false,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"10\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"06,14,16,27,43,46,52,58,64,90\",\"betName\":\"Perm3\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"24\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawId\":\"74716\",\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForPerm3Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"8\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"25,21,38,36,23,52,55,72\",\"betName\":\"Perm3\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"5.6\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect1QP() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"1\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"68\",\"betName\":\"Direct1\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect1Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"1\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"68\",\"betName\":\"Direct1\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect2QP() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"2\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"23,70\",\"betName\":\"Direct2\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect2Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"2\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"27,22\",\"betName\":\"Direct2\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect3QP()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"3\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"32,45,76\",\"betName\":\"Direct3\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect3Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"3\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"53,55,40\",\"betName\":\"Direct3\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect4QP()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"4\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"14,19,35,42\",\"betName\":\"Direct4\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect4Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"4\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"43,22,27,41\",\"betName\":\"Direct4\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect5QP()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"02,44,49,61,62\",\"betName\":\"Direct5\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_returnsSuccessForDirect5Manual()
			throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123456789987654321\",\"barcodeCount\":\"1234567899876543210\",\"purchaseDate\":\"14\",\"purchaseTime\":\"Nov\",\"purchaseAmt\":20,\"drawData\":[{\"drawDate\":\"14\",\"drawTime\":\"Nov\"}]},\"betTypeData\":[{\"isQp\":false,\"betName\":\"BetType\",\"pickedNumbers\":\"Numbers\",\"numberPicked\":\"11\",\"unitPrice\":1,\"noOfLines\":1,\"betAmtMul\":1,\"panelPrice\":1}],\"userName\":\"testret\"},\"isPromo\":false}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test
	public void purchaseTicketProcess_IfSaleStatusFraud() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("FRAUD");
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":7001,\"errorMsg\":\"Sale Limit Reached !!!\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}

	@Test 
	public void purchaseTicketProcess_IfSaleStatusUNAUTHORISED() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		kenoBean.setSaleStatus("UNAUTHORISED");
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":2031,\"errorMsg\":\"Unauthorized to Sale Ticket.\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	@Test 
	public void purchaseTicketProcess_IfSaleStatusLIMIT_REACHED() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		kenoBean.setSaleStatus("LIMIT_REACHED");
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":2032,\"errorMsg\":\"Playing for this bet type is not possible for the current draw,Limit Reached\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	@Test
	public void purchaseTicketProcess_IfSaleStatusNO_DRAWS() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		kenoBean.setSaleStatus("NO_DRAWS");
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":2023,\"errorMsg\":\"Transaction Failed.\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	@Test
	public void purchaseTicketProcess_IfSaleStatusRET_INS_BAL() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		kenoBean.setSaleStatus("RET_INS_BAL");
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":10004,\"errorMsg\":\"Retailer has insufficient Balance \"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	@Test
	public void purchaseTicketProcess_IfAgentHaveInsufficientBalance() throws IOException, Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"5\",\"betAmtMul\":1,\"isQp\":false,\"pickedNumbers\":\"22,25,34,54,41\",\"betName\":\"Direct5\",\"QPPreGenerated\":false}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"0.1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		kenoBean.setSaleStatus("AGT_INS_BAL");
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":10003,\"errorMsg\":\"Agent has insufficient balance\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	
	@Test
	public void purchaseTicketProcess_FailureWhenRequestDataIsNull() throws IOException, Exception {
		String json = null;
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		prepareKenoPurchaseBean(kenoBean, false);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":2002,\"errorMsg\":\"Some Internal Error !\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
	@Test
	public void purchaseTicketProcess_FailureWhenInvalidInput() throws Exception {
		String json = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"KenoTwo\"},\"betTypeData\":[{\"noPicked\":\"1\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"11\",\"betName\":\"Direct5\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"1\"}";
		setGameMap();
		KenoTwoAction kenoTwoAction = oneMethod(json);
		kenoBean.setSaleStatus("SUCCESS");
		prepareKenoPurchaseBean(kenoBean, true);
		sendKenoPurchaseBean();
		kenoTwoAction.purchaseTicketProcess();
		String output = "{\"isSuccess\":false,\"errorCode\":2021,\"errorMsg\":\"Invalid Input.\"}";
		JSONObject js = (JSONObject) JSONSerializer.toJSON(output);
		Assert.assertEquals(js, kenoTwoAction.getTestResponse());
	}
}
