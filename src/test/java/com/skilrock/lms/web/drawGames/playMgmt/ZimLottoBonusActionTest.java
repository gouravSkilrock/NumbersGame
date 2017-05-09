package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.playMgmt.ZimLottoBonusAction;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Util.class, ServletActionContext.class, TransactionManager.class, CookieMgmtForTicketNumber.class, CommonMethods.class, DBConnect.class})
public class ZimLottoBonusActionTest {

	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ZimLottoBonusAction zimLottoBonusAction;
	UserInfoBean user;
	LottoPurchaseBean lottoPurchaseBean;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	
	private final String INPUT_REQUEST_1 = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"ZimLottoBonus\"},\"betTypeData\":[{\"noPicked\":\"6\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"03,05,13,28,37,41\",\"betName\":\"Direct6\",\"QPPreGenerated\":true},{\"noPicked\":\"6\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"02,06,13,21,28,36\",\"betName\":\"Direct6\",\"QPPreGenerated\":true},{\"noPicked\":\"6\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"21,28,30,35,39,42\",\"betName\":\"Direct6\",\"QPPreGenerated\":true},{\"noPicked\":\"6\",\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"01,04,08,10,22,42\",\"betName\":\"Direct6\",\"QPPreGenerated\":true}],\"noOfPanel\":4,\"totalPurchaseAmt\":\"0.8\"}";
	//private final String OUTPUT_RESPONCE_1 = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123\",\"barcodeCount\":\"123123\",\"gameName\":\"6/42\",\"purchaseDate\":\"1\",\"purchaseTime\":\"2\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"Direct6\",\"pickedNumbers\":[\"03,05,13,28,37,41\",\"02,06,13,21,28,36\",\"21,28,30,35,39,42\",\"01,04,08,10,22,42\"],\"numberPicked\":6,\"unitPrice\":0,\"noOfLines\":0,\"betAmtMul\":1,\"panelPrice\":0}]},\"isPromo\":false}";
	private final String OUTPUT_RESPONCE_1 = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123\",\"barcodeCount\":\"123123\",\"gameName\":\"6/42\",\"purchaseDate\":\"1\",\"purchaseTime\":\"2\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"Direct6\",\"betAmtMul\":1,\"panelPrice\":0,\"pickedNumbers\":[\"03,05,13,28,37,41\",\"02,06,13,21,28,36\",\"21,28,30,35,39,42\",\"01,04,08,10,22,42\"],\"numberPicked\":6,\"unitPrice\":0,\"noOfLines\":0}]},\"isPromo\":false}";
	private final String INPUT_REQUEST_2 = "{\"commonSaleData\":{\"isAdvancePlay\":false,\"drawData\":[],\"noOfDraws\":1,\"isDrawManual\":true,\"gameName\":\"ZimLottoBonus\"},\"betTypeData\":[{\"noPicked\":8,\"betAmtMul\":1,\"isQp\":true,\"pickedNumbers\":\"07,08,10,12,27,31,35,37\",\"betName\":\"Perm6\",\"QPPreGenerated\":true}],\"noOfPanel\":1,\"totalPurchaseAmt\":\"5.6\"}";
	//private final String OUTPUT_RESPONCE_2 = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123\",\"barcodeCount\":\"123123\",\"gameName\":\"6/42\",\"purchaseDate\":\"1\",\"purchaseTime\":\"2\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"Perm6\",\"pickedNumbers\":[\"07,08,10,12,27,31,35,37\"],\"numberPicked\":8,\"unitPrice\":0,\"noOfLines\":0,\"betAmtMul\":1,\"panelPrice\":0}]},\"isPromo\":false}";
	private final String OUTPUT_RESPONCE_2 = "{\"isSuccess\":true,\"errorMsg\":\"\",\"mainData\":{\"commonSaleData\":{\"ticketNumber\":\"123\",\"barcodeCount\":\"123123\",\"gameName\":\"6/42\",\"purchaseDate\":\"1\",\"purchaseTime\":\"2\",\"purchaseAmt\":2,\"drawData\":[{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"},{\"drawDate\":\"2016-10-22\",\"drawTime\":\"19:45:00.0\"}]},\"betTypeData\":[{\"isQp\":true,\"betName\":\"Perm6\",\"betAmtMul\":1,\"panelPrice\":0,\"pickedNumbers\":[\"07,08,10,12,27,31,35,37\"],\"numberPicked\":8,\"unitPrice\":0,\"noOfLines\":0}]},\"isPromo\":false}";
	
	private final String AGENT_HAS_INSUFFICIENT_BALANCE = "{\"isSuccess\":false,\"errorCode\":10003,\"errorMsg\":\"Agent has insufficient balance\"}";
	private final String RETAILER_HAS_INSUFFICIENT_BALANCE = "{\"isSuccess\":false,\"errorCode\":10004,\"errorMsg\":\"Retailer has insufficient Balance \"}";
	private final String TRANSACTION_FAILED = "{\"isSuccess\":false,\"errorCode\":2023,\"errorMsg\":\"Transaction Failed.\"}";
	private final String SALE_LIMIT_REACHED = "{\"isSuccess\":false,\"errorCode\":7001,\"errorMsg\":\"Sale Limit Reached !!!\"}";
	private final String UNAUTHORIZED_TO_SALE_TICKET = "{\"isSuccess\":false,\"errorCode\":2031,\"errorMsg\":\"Unauthorized to Sale Ticket.\"}";
	private final String LIMIT_REACHED = "{\"isSuccess\":false,\"errorCode\":2032,\"errorMsg\":\"Playing for this bet type is not possible for the current draw,Limit Reached\"}";
	private final String INVALID_INPUT = "{\"isSuccess\":false,\"errorCode\":2021,\"errorMsg\":\"Invalid Input.\"}";
	//private final String RETAILER_HAS_INSUFFICIENT_bALANCE = "";
	
	
	@Before
    public void executedBeforeEach() {
		mockDataBase();
		neccessaryPowerMocking();
		zimLottoBonusAction = new ZimLottoBonusAction();
        setLottoPurchaseBean();
        
    }
	
	@Test
	public void purchaseTicketProcess_Direct6SaleSuccess() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("SUCCESS");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = OUTPUT_RESPONCE_1.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Perm6SaleSuccess() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("SUCCESS");
		httpSetupForMock();
		String json = INPUT_REQUEST_2;
		zimLottoBonusAction.setJson(json);
		setInputDataForMock();
		httpSetupForMock();
		zimLottoBonusAction.purchaseTicketProcess();
		String[] split = OUTPUT_RESPONCE_2.split(",");
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString, responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_AGENT_HAS_INSUFFICIENT_BALANCE() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("AGT_INS_BAL");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = AGENT_HAS_INSUFFICIENT_BALANCE.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_RETAILER_HAS_INSUFFICIENT_BALANCE() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("RET_INS_BAL");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = RETAILER_HAS_INSUFFICIENT_BALANCE.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_TRANSACTION_FAILED() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("NO_DRAWS");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = TRANSACTION_FAILED.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_SALE_LIMIT_REACHED() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("FRAUD");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = SALE_LIMIT_REACHED.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_UNAUTHORIZED_TO_SALE_TICKET() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("UNAUTHORISED");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = UNAUTHORIZED_TO_SALE_TICKET.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_LIMIT_REACHED() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("LIMIT_REACHED");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = LIMIT_REACHED.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	@Test
	public void purchaseTicketProcess_Error_INVALID_INPUT() throws Exception {
		setInputDataForMock();
		setDrawGameRPOSHelperMock("KUCH_BHI");
		httpSetupForMock();
		String json = INPUT_REQUEST_1;
		zimLottoBonusAction.setJson(json);
		zimLottoBonusAction.purchaseTicketProcess();
		String responceForTest = zimLottoBonusAction.getResponceForTest();
		String[] split = INVALID_INPUT.split(",");
		for(String splitString : split){
			Assert.assertTrue("Sequence not found "+splitString,responceForTest.contains(splitString));
		}
	}
	
	private void setDrawGameRPOSHelperMock(String saleStatus){
		DrawGameRPOSHelper drawGameRPOSHelper = Mockito.mock(DrawGameRPOSHelper.class);
		try {
			List<String> drawDateTime = createDrawDateTime();
			LottoPurchaseBean lottoPurchaseBean = createLottoPurchaseBean(drawDateTime);
			lottoPurchaseBean.setSaleStatus(saleStatus);
			Mockito.when(drawGameRPOSHelper.zimLottoBonusPurchaseTicket(Mockito.any(UserInfoBean.class), Mockito.any(LottoPurchaseBean.class))).thenReturn(lottoPurchaseBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		zimLottoBonusAction.setDrawGameRPOSHelper(drawGameRPOSHelper);
	}
	
	private void setInputDataForMock() {
		setBeansForMock();
		setMapsForMock();
		setDependenciesForMock();
	}
	
	private void httpSetupForMock() {
		setRequestForMock();
		setResponceForMock();
		setSessiontForMock();
		setServletContextForMock();
	}
	
	private void setDependenciesForMock(){
		//setDrawGameRPOSHelperMock();
		PowerMockito.mockStatic(TransactionManager.class);
	}
	
	private void setRequestForMock() {
		request = Mockito.mock(HttpServletRequest.class);
		zimLottoBonusAction.setServletRequest(request);
		Mockito.when(request.getAttribute("code")).thenReturn("123");
	}
	
	private void setResponceForMock() {
		response = Mockito.mock(HttpServletResponse.class);
		zimLottoBonusAction.setServletResponse(response);
		PrintWriter printWriter = new PrintWriter(System.out);
		try {
			Mockito.when(response.getWriter()).thenReturn(printWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setSessiontForMock() {
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(user);
	}
	
	private void setBeansForMock() {
		setUserInfoBean();
		//setLottoPurchaseBean();
	}
	
	private void setUserInfoBean() {
		user = new UserInfoBean();
		user.setUserId(1);
		user.setUserOrgId(2);
		user.setUserType("userType");
		user.setCurrentUserMappingId(3);
	}
	
	private void setLottoPurchaseBean() {
		lottoPurchaseBean = new LottoPurchaseBean();
		zimLottoBonusAction.setZimLottoBonusPurchaseBean(lottoPurchaseBean);
	}
	
	private void setMapsForMock() {
		setDrawIdTableMapForMock();
		//setUtilGameMapForMock();
	}
	
	private void setServletContextForMock() {
		PowerMockito.mockStatic(ServletActionContext.class);
		ServletContext sc = Mockito.mock(ServletContext.class);
		Mockito.when(ServletActionContext.getServletContext()).thenReturn(sc);
		Mockito.when(sc.getAttribute("drawIdTableMap")).thenReturn(drawIdTableMap);
	}
	
	private void setDrawIdTableMapForMock() {
		drawIdTableMap = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, String> drawMap = new HashMap<Integer, String>();
		drawMap.put(256, "256");
		drawMap.put(257, "257");
		drawMap.put(258, "258");
		drawIdTableMap.put(5, drawMap);
		Util.drawIdTableMap = drawIdTableMap;
	}

	private LottoPurchaseBean createLottoPurchaseBean(List<String> drawDateTime) {
		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		lottoPurchaseBean.setDrawDateTime(drawDateTime);
		lottoPurchaseBean.setTicket_no("123");
		lottoPurchaseBean.setReprintCount("123");
		lottoPurchaseBean.setBarcodeCount((short)1);
		lottoPurchaseBean.setGameDispName("6/42");
		lottoPurchaseBean.setPurchaseTime("1 2 3 4 5");
		lottoPurchaseBean.setTotalPurchaseAmt(2);
		return lottoPurchaseBean;
	}

	private List<String> createDrawDateTime() {
		List<String> drawDateTime = new ArrayList<String>();
		drawDateTime.add("2016-10-22 19:45:00.0");
		drawDateTime.add("2016-10-22 19:45:00.0");
		drawDateTime.add("2016-10-22 19:45:00.0");
		return drawDateTime;
	}
	
	private void mockDataBase(){
		PowerMockito.mockStatic(DBConnect.class);
		Connection connection = Mockito.mock(Connection.class);
		Statement statement = Mockito.mock(Statement.class);
		ResultSet resultSet = Mockito.mock(ResultSet.class);
		
		PowerMockito.when(DBConnect.getConnection()).thenReturn(connection);
		try {
			PowerMockito.when(connection.createStatement()).thenReturn(statement);
			PowerMockito.when(statement.executeQuery(Mockito.anyString())).thenReturn(resultSet);
			PowerMockito.when(resultSet.next()).thenReturn(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void neccessaryPowerMocking(){
		PowerMockito.mockStatic(CookieMgmtForTicketNumber.class);
		PowerMockito.mockStatic(CommonMethods.class);
		PowerMockito.mockStatic(Util.class);
		
		PowerMockito.when(Util.getGameId("ZimLottoBonus")).thenReturn(5);
		PowerMockito.when(Util.validateNumber(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
	}
	
	/*private void setUtilGameMapForMock() {
		Map<Integer, GameMasterLMSBean> gameMap = new HashMap<Integer, GameMasterLMSBean>();
		Util.setGameMap(gameMap);
	}*/
	
	/*private void setGameMapForUtil() {
		String gameData = "{\"5\":{\"agentPwtCommRate\":0.0,\"agentSaleCommRate\":0.0,\"gameId\":5,\"gameName\":\"Bonus Lotto\",\"gameNameDev\":\"ZimLottoBonus\",\"gameNo\":5,\"govtComm\":0.0,\"highPrizeAmount\":0.0,\"priceMap\":{\"Direct6\":{\"betDispName\":\"Direct6\",\"unitPrice\":0.2,\"maxBetAmtMultiple\":100,\"betStatus\":\"ACTIVE\",\"betCode\":1,\"betOrder\":1},\"Perm6\":{\"betDispName\":\"Perm6\",\"unitPrice\":0.2,\"maxBetAmtMultiple\":100,\"betStatus\":\"ACTIVE\",\"betCode\":2,\"betOrder\":2}},\"prizePayoutRatio\":0.0,\"retPwtCommRate\":0.0,\"retSaleCommRate\":0.0,\"ticketExpiryPeriod\":60,\"vatAmount\":0.0,\"isDependent\":0,\"jackpotCounter\":0,\"jackpotLimit\":50000.0}}";
		Map<Integer, GameMasterLMSBean> gameMap = new Gson().fromJson(gameData, new TypeToken<HashMap<Integer, GameMasterLMSBean>>(){}.getType());
		gameMap.put(6, gameMap.get(5));
		gameMap.put(7, gameMap.get(5));
		gameMap.put(8, gameMap.get(5));
		Util.setGameMap(gameMap);
	}*/
}
