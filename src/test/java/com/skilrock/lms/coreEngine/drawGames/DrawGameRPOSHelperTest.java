package com.skilrock.lms.coreEngine.drawGames;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.skilrock.lms.web.drawGames.common.Util", "com.skilrock.lms.common.Utility"})
@PrepareForTest({orgOnLineSaleCreditUpdation.class,ResponsibleGaming.class,Utility.class,DBConnect.class,Util.class})
public class DrawGameRPOSHelperTest {
	Connection connection;
	ServiceDelegate delegate;
	AjaxRequestHelper ajxReqHelper;

	String kenoBeanData = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130982\":\"5314\",\"130983\":\"5314\",\"130984\":\"5314\",\"130985\":\"5314\",\"130986\":\"5314\"},\"15\":{\"746\":\"180\",\"747\":\"180\",\"748\":\"180\"},\"18\":{\"510\":\"4\",\"511\":\"5\",\"512\":\"5\"},\"19\":{\"679\":\"6\",\"680\":\"6\",\"681\":\"6\"},\"20\":{\"694\":\"32\",\"695\":\"32\",\"696\":\"32\"},\"16\":{\"48\":\"5\",\"49\":\"5\",\"50\":\"5\"},\"17\":{\"767\":\"8\",\"768\":\"8\",\"769\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"10\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"04,08,16,24,25,54,61,63,82,83\"],\"playType\":[\"Perm1\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":1.0,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	String userBeanData = "{\"availableCreditLimit\":301279.7,\"claimableBal\":-5.05,\"currentCreditAmt\":-300279.7,\"isMasterRole\":\"Y\",\"isRoleHeadUser\":\"Y\",\"loginChannel\":\"WEB\",\"orgName\":\"Test Retailer\",\"orgStatus\":\"ACTIVE\",\"parentUserId\":0,\"parentOrgId\":2,\"parentOrgName\":\"Test Agent\",\"parentOrgCode\":\"testagent\",\"parentOrgStatus\":\"ACTIVE\",\"pwtSacrap\":\"YES\",\"roleId\":3,\"roleName\":\"RETAILER MASTER\",\"status\":\"ACTIVE\",\"tierId\":3,\"unclaimableBal\":0.0,\"userId\":11004,\"currentUserMappingId\":17128,\"userName\":\"testret\",\"userOrgId\":3,\"userType\":\"RETAILER\",\"isTPUser\":false,\"orgCode\":\"Test Retailer\",\"userOrgCode\":\"testagent1\",\"terminalBuildVersion\":0.0,\"userSession\":\"FEC1D21BC178428539ED0943839B1B25\"}";
	String gameBeanData = "{\"agentPwtCommRate\":0.0,\"agentSaleCommRate\":0.0,\"gameId\":1,\"gameName\":\"Lucky Numbers\",\"gameNameDev\":\"KenoTwo\",\"gameNo\":1,\"gameStatus\":\"OPEN\",\"govtComm\":25.0,\"govtCommPwt\":0.0,\"highPrizeAmount\":5000.0,\"priceMap\":{\"Perm1\":{\"betDispName\":\"Perm1\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":1,\"betOrder\":1},\"Perm2\":{\"betDispName\":\"Perm2\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":2,\"betOrder\":2},\"Perm3\":{\"betDispName\":\"Perm3\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":3,\"betOrder\":3},\"Direct1\":{\"betDispName\":\"Direct1\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":4,\"betOrder\":4},\"Direct2\":{\"betDispName\":\"Direct2\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":5,\"betOrder\":5},\"Direct3\":{\"betDispName\":\"Direct3\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":6,\"betOrder\":6},\"Direct4\":{\"betDispName\":\"Direct4\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":7,\"betOrder\":7},\"Direct5\":{\"betDispName\":\"Direct5\",\"unitPrice\":0.1,\"maxBetAmtMultiple\":10,\"betStatus\":\"ACTIVE\",\"betCode\":8,\"betOrder\":8}},\"prizePayoutRatio\":50.0,\"retPwtCommRate\":0.0,\"retSaleCommRate\":0.0,\"ticketExpiryPeriod\":60,\"vatAmount\":15.0,\"isDependent\":0,\"jackpotCounter\":0,\"jackpotLimit\":0.0,\"bonusBallEnable\":\"N\"}";
	String direct1KenoPurchaseBean = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130994\":\"5314\",\"130995\":\"5314\",\"130996\":\"5314\",\"130997\":\"5314\",\"130998\":\"5314\"},\"15\":{\"759\":\"180\",\"760\":\"180\",\"761\":\"180\"},\"18\":{\"566\":\"5\",\"567\":\"5\",\"568\":\"5\"},\"19\":{\"691\":\"6\",\"692\":\"6\",\"693\":\"6\"},\"20\":{\"706\":\"32\",\"707\":\"32\",\"708\":\"32\"},\"16\":{\"49\":\"5\",\"50\":\"5\",\"51\":\"5\"},\"17\":{\"778\":\"8\",\"779\":\"8\",\"780\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"1\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"19\"],\"playType\":[\"Direct1\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":0.1,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	String direct2KenoPurchaseBean = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130994\":\"5314\",\"130995\":\"5314\",\"130996\":\"5314\",\"130997\":\"5314\",\"130998\":\"5314\"},\"15\":{\"759\":\"180\",\"760\":\"180\",\"761\":\"180\"},\"18\":{\"566\":\"5\",\"567\":\"5\",\"568\":\"5\"},\"19\":{\"691\":\"6\",\"692\":\"6\",\"693\":\"6\"},\"20\":{\"706\":\"32\",\"707\":\"32\",\"708\":\"32\"},\"16\":{\"49\":\"5\",\"50\":\"5\",\"51\":\"5\"},\"17\":{\"778\":\"8\",\"779\":\"8\",\"780\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"2\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"20,73\"],\"playType\":[\"Direct2\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":0.1,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	String direct3KenoPurchaseBean = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130994\":\"5314\",\"130995\":\"5314\",\"130996\":\"5314\",\"130997\":\"5314\",\"130998\":\"5314\"},\"15\":{\"759\":\"180\",\"760\":\"180\",\"761\":\"180\"},\"18\":{\"566\":\"5\",\"567\":\"5\",\"568\":\"5\"},\"19\":{\"691\":\"6\",\"692\":\"6\",\"693\":\"6\"},\"20\":{\"706\":\"32\",\"707\":\"32\",\"708\":\"32\"},\"16\":{\"49\":\"5\",\"50\":\"5\",\"51\":\"5\"},\"17\":{\"778\":\"8\",\"779\":\"8\",\"780\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"3\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"58,59,60\"],\"playType\":[\"Direct3\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":0.1,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	String direct4KenoPurchaseBean = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130994\":\"5314\",\"130995\":\"5314\",\"130996\":\"5314\",\"130997\":\"5314\",\"130998\":\"5314\"},\"15\":{\"759\":\"180\",\"760\":\"180\",\"761\":\"180\"},\"18\":{\"566\":\"5\",\"567\":\"5\",\"568\":\"5\"},\"19\":{\"691\":\"6\",\"692\":\"6\",\"693\":\"6\"},\"20\":{\"706\":\"32\",\"707\":\"32\",\"708\":\"32\"},\"16\":{\"49\":\"5\",\"50\":\"5\",\"51\":\"5\"},\"17\":{\"778\":\"8\",\"779\":\"8\",\"780\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"4\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"21,46,49,56\"],\"playType\":[\"Direct4\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":0.1,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	String direct5KenoPurchaseBean = "{\"betAmountMultiple\":[1],\"bonus\":\"N\",\"drawDateTime\":[],\"drawIdTableMap\":{\"1\":{\"74710\":\"73814\",\"74711\":\"73814\",\"74712\":\"73814\"},\"5\":{\"256\":\"256\",\"257\":\"257\",\"258\":\"258\"},\"12\":{\"130994\":\"5314\",\"130995\":\"5314\",\"130996\":\"5314\",\"130997\":\"5314\",\"130998\":\"5314\"},\"15\":{\"759\":\"180\",\"760\":\"180\",\"761\":\"180\"},\"18\":{\"566\":\"5\",\"567\":\"5\",\"568\":\"5\"},\"19\":{\"691\":\"6\",\"692\":\"6\",\"693\":\"6\"},\"20\":{\"706\":\"32\",\"707\":\"32\",\"708\":\"32\"},\"16\":{\"49\":\"5\",\"50\":\"5\",\"51\":\"5\"},\"17\":{\"778\":\"8\",\"779\":\"8\",\"780\":\"8\"}},\"game_no\":1,\"gameId\":1,\"gameDispName\":\"Lucky Numbers\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isQuickPick\":[1],\"isRaffelAssociated\":false,\"noOfDraws\":1,\"noOfPanel\":1,\"noPicked\":[\"5\"],\"partyId\":3,\"partyType\":\"RETAILER\",\"playerData\":[\"01,32,42,45,66\"],\"playType\":[\"Direct5\"],\"promoSaleStatus\":\"SUCCESS\",\"purchaseChannel\":\"LMS_Web\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"totalPurchaseAmt\":0.1,\"userId\":11004,\"userMappingId\":17128,\"serviceId\":3,\"barcodeCount\":0,\"lastGameId\":0,\"noofDrawsForPromo\":0,\"QPPreGenerated\":[true]}";
	KenoPurchaseBean kenoPurchaseBean = null;
	UserInfoBean userBean = null;
	String dgeSuccessResponse="{\"ticketNo\":\"50903132217128043\",\"barcodeCount\":22,\"noOfDraws\":1,\"purchaseTime\":\"2016-11-17 17:14:46\",\"reprintCount\":\"0\",\"playerData\":[\"22,23,39,41,42,43,56,57,68,83\"],\"totalPurchaseAmt\":1.0,\"drawDateTime\":[\"2016-11-17 18:10:00\u00261646\"],\"isSuccess\":true,\"dgeTxnId\":0,\"errorCode\":0}";
	String dgeErrorResponse="{\"barcodeCount\":0,\"noOfDraws\":0,\"totalPurchaseAmt\":0.0,\"drawDateTime\":[\"2016-11-17 18:10:00\u00261646\"],\"isSuccess\":false,\"dgeTxnId\":0,\"errorCode\":0}";
	@Before
	public void setPreTestCaseData() throws SQLException{
		kenoPurchaseBean = new Gson().fromJson(kenoBeanData, KenoPurchaseBean.class);
		userBean = new Gson().fromJson(userBeanData, UserInfoBean.class);
		Map<Integer, Map<Integer, String>> drawIdTableMap = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, String> drawMap = new HashMap<Integer, String>();
		Map<Integer, GameMasterLMSBean> gameMap = new HashMap<Integer, GameMasterLMSBean>();
		drawMap.put(74699, "73814");
		drawMap.put(74700, "73814");
		drawMap.put(74701, "73814");
		drawIdTableMap.put(1, drawMap);
		Util.drawIdTableMap = drawIdTableMap;
		Util.onfreezeSale=true;
		gameMap.put(1, new Gson().fromJson(gameBeanData, GameMasterLMSBean.class));
		Util.setGameMap(gameMap);
		Util.promoGameBeanMap=new HashMap<Integer, List<PromoGameBean>>();
		delegate=Mockito.mock(ServiceDelegate.class);
		ajxReqHelper=Mockito.mock(AjaxRequestHelper.class);
		connection=Mockito.mock(Connection.class);
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.mockStatic(orgOnLineSaleCreditUpdation.class, ResponsibleGaming.class,DBConnect.class,Util.class);
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessPerm1() throws Exception {
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessDirect1() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct1KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleFailedDirect1WithBalanceDeductedZero() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct1KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(0,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Failed", "RET_INS_BAL", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleFailedDirect1WhenAgentHasInsufficentBalance() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct1KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(-1,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Failed", "AGT_INS_BAL", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleFailedDirect1WhenErrorInBalanceDeduction() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct1KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(-2,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Failed", "FAILED", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessDirect2() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct2KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessDirect3() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct3KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessDirect4() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct4KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsSuccessDirect5() throws Exception {
		kenoPurchaseBean = new Gson().fromJson(direct5KenoPurchaseBean, KenoPurchaseBean.class);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "SUCCESS", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	
	@Test(expected = LMSException.class)
	public void commonPurchseProcessKenoTwo_ExceptionIfRequestBeanIsNull() throws Exception {
		kenoPurchaseBean = null;
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
			kenoPurchaseBean);
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_ExceptionIfUserBeanIsNull() throws Exception {
		userBean = null;
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
			kenoPurchaseBean);
		Assert.assertEquals("Sale Failed", "FAILED", kenoPurchaseResponseBean.getSaleStatus());
	}
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusIsFailedInCaseFailedAtDGE() throws Exception {
		PowerMockito.when(orgOnLineSaleCreditUpdation.class, "drawTcketSaleBalDeduction", Mockito.any(UserInfoBean.class),
						Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyString(), Mockito.any(Connection.class))
				.thenReturn(3L);
		PowerMockito.when(orgOnLineSaleCreditUpdation.class, "drawTicketSaleTicketUpdate", Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.any(UserInfoBean.class), Mockito.anyString(),
				Mockito.any(Connection.class)).thenReturn(1);
		PowerMockito.when(ResponsibleGaming.class, "respGaming", Mockito.any(UserInfoBean.class), Mockito.anyString(),Mockito.anyString(),
				Mockito.any(Connection.class)).thenReturn(false);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.when(Util.class, "getUnitPrice",Mockito.anyInt(),Mockito.anyString()).thenReturn(1.0);
		PowerMockito.when(Util.class,"getDGSaleAdvMessage",Mockito.anyInt(),Mockito.anyInt()).thenReturn(Mockito.any(HashMap.class));
		setUpPowerMockForUtilityClass("AUTO_CANCEL_CLOSER_DAYS");
		Mockito.when(delegate.getResponseString(Mockito.any(ServiceRequest.class))).thenReturn(dgeErrorResponse);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale Failed", "FAILED", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_FailureIfNoOfPanelZero() throws Exception{
		kenoPurchaseBean.setNoOfPanel(0);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean=new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean, kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "FAILED", kenoPurchaseResponseBean.getSaleStatus());	
	}
	
	@Test(expected=LMSException.class)
	public void commonPurchseProcessKenoTwo_FailureIfGameIdZero() throws Exception{
		kenoPurchaseBean.setGameId(0);
		setMockingForSuccessSale(3L,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean=new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean, kenoPurchaseBean);
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_FailureIfBalanceDeductedOfRetailerIsZero() throws Exception{
		kenoPurchaseBean.setNoOfPanel(0);
		setMockingForSuccessSale(0,1.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean=new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean, kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "FAILED", kenoPurchaseResponseBean.getSaleStatus());	
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_FailureIfUnitPriceForGameIsZero() throws Exception{
		kenoPurchaseBean.setNoOfPanel(0);
		setMockingForSuccessSale(3,0.0,false);
		KenoPurchaseBean kenoPurchaseResponseBean=new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean, kenoPurchaseBean);
		Assert.assertEquals("Sale Success", "FAILED", kenoPurchaseResponseBean.getSaleStatus());	
	}
	
	@Test
	public void commonPurchseProcessKenoTwo_SaleStatusFailedInFraud() throws Exception {
		setMockingForSuccessSale(3L,1.0,true);
		KenoPurchaseBean kenoPurchaseResponseBean = new DrawGameRPOSHelper(delegate,ajxReqHelper).commonPurchseProcessKenoTwo(userBean,
				kenoPurchaseBean);
		Assert.assertEquals("Sale failed", "FRAUD", kenoPurchaseResponseBean.getSaleStatus());
		
	}
	
	
	private void setMockingForSuccessSale(long balDeducted,double unitPrice,boolean isFraud) throws Exception{
		PowerMockito.when(orgOnLineSaleCreditUpdation.class, "drawTcketSaleBalDeduction", Mockito.any(UserInfoBean.class),
				Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyString(), Mockito.any(Connection.class)).thenReturn(balDeducted);
		
		PowerMockito.when(orgOnLineSaleCreditUpdation.class, "drawTicketSaleTicketUpdate", Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.any(UserInfoBean.class), Mockito.anyString(),
				Mockito.any(Connection.class)).thenReturn(1);
		PowerMockito.when(ResponsibleGaming.class, "respGaming", Mockito.any(UserInfoBean.class), Mockito.anyString(),Mockito.anyString(),
				Mockito.any(Connection.class)).thenReturn(isFraud);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.when(Util.class, "getUnitPrice",Mockito.anyInt(),Mockito.anyString()).thenReturn(unitPrice);
		PowerMockito.when(Util.class,"getDGSaleAdvMessage",Mockito.anyInt(),Mockito.anyInt()).thenReturn(Mockito.any(HashMap.class));
		setUpPowerMockForUtilityClass("AUTO_CANCEL_CLOSER_DAYS");
		Mockito.when(delegate.getResponseString(Mockito.any(ServiceRequest.class))).thenReturn(dgeSuccessResponse);
	}
	
	 private void setUpPowerMockForUtilityClass(String argument) throws Exception {
		  PowerMockito.when(Utility.class, "getPropertyValue", "AUTO_CANCEL_CLOSER_DAYS").thenReturn("3");
		 }

}
