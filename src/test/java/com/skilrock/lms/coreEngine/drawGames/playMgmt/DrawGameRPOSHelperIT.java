package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.itg.IDBarcode.IDBarcode;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ResponsibleGamingBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({orgOnLineSaleCreditUpdation.class, IDBarcode.class})
public class DrawGameRPOSHelperIT {
	UserInfoBean userBean = new UserInfoBean();
	LottoPurchaseBean drawPurchaseBean = new LottoPurchaseBean();
	GameMasterLMSBean gameMasterLMSBean = new GameMasterLMSBean();
	Map<Integer, GameMasterLMSBean> gameMap = new HashMap<Integer, GameMasterLMSBean>();
	ResponsibleGamingBean respCritBean = new ResponsibleGamingBean();
	Map<Integer, Map<Integer, String>> drawIdTableMap = new HashMap<Integer, Map<Integer, String>>();
	Map<Integer, String> drawMap = new HashMap<Integer, String>();
	
	@Before
	public void zimLottoBonusPurchaseTicket_SetGameMap() {
		String gameData = "{\"5\":{\"agentPwtCommRate\":0.0,\"agentSaleCommRate\":0.0,\"gameId\":5,\"gameName\":\"Bonus Lotto\",\"gameNameDev\":\"ZimLottoBonus\",\"gameNo\":5,\"govtComm\":0.0,\"highPrizeAmount\":0.0,\"priceMap\":{\"Direct6\":{\"betDispName\":\"Direct6\",\"unitPrice\":0.2,\"maxBetAmtMultiple\":100,\"betStatus\":\"ACTIVE\",\"betCode\":1,\"betOrder\":1},\"Perm6\":{\"betDispName\":\"Perm6\",\"unitPrice\":0.2,\"maxBetAmtMultiple\":100,\"betStatus\":\"ACTIVE\",\"betCode\":2,\"betOrder\":2}},\"prizePayoutRatio\":0.0,\"retPwtCommRate\":0.0,\"retSaleCommRate\":0.0,\"ticketExpiryPeriod\":60,\"vatAmount\":0.0,\"isDependent\":0,\"jackpotCounter\":0,\"jackpotLimit\":50000.0}}";
		String propertyMapData = "{\"IS_DAILY_IW_TRAINING_EXPENSE_ENABLED\":\"NO\",\"RESULT_SUBMIT_USERS_COUNT\":\"5\",\"OLA_DEP_ANYWHERE\":\"YES\",\"REPORTING_OFF_START_TIME\":\"19:00:00\",\"ADVT_MSG\":\"YES\",\"MAX_AUTH_ATTEMP_TRACK_TICKET\":\"30\",\"SE_SALE_REP_TYPE\":\"BOOK_WISE\",\"MAPPING_ID_GEN_BY_THIRD_PARTY\":\"false\",\"RET_PWT_COMM_RATE\":\"0\",\"hbInterval\":\"120\",\"MAX_CLAIM_PER_TICKET_RET\":\"10000\",\"SLE_TKT_CANCELLATION_CHARGES\":\"0.00\",\"Supervisor_PWD\":\"17042014\",\"IS_CASH_REGISTER\":\"INACTIVE\",\"CLAIM_AT_SELF_RET\":\"YES\",\"OLA_PLAYER_MOBILE_NUMBER_LIMIT\":\"10\",\"TERMINAL_INBOX_MESSAGE_LIMIT\":\"14\",\"agtScrapLimit\":\"1000.00\",\"agtDepositLimit\":\"2000\",\"MAIL_SMTP_HOST_IP\":\"smtp.gmail.com\",\"ACTIVE_BROWSER_FOR_RETAILER\":\"Explorer,Firefox,Mozilla,Chrome\",\"DATE_FOR_USER_ID_CHANGE\":\"2013-09-22 00:00:00\",\"DECIMAL_PRECISION\":\"2\",\"RAFFLE_GAME_DRAW_DAY\":\"NA\",\"uIDFill\":\"N\",\"IW_TKT_CANCELLATION_CHARGES\":\"0.00\",\"agtPayLimit\":\"1000.00\",\"BLOCK_AMT\":\"10000\",\"DO_MATH_ROUNDING_FOR_PWT_AMT\":\"true\",\"cs_isVoucherPrintON\":\"YES\",\"COMM_APPLICABLE\":\"yes\",\"isRepFrmSP\":\"true\",\"RET_ONLINE\":\"YES\",\"RESPONSE_TIME_OUT\":\"500\",\"NO_OF_PROMO_TICKET\":\"10\",\"IS_CASH_RCPT_ON_THERMAL_PRINTER\":\"false\",\"CLAIM_AT_SELF_AGT\":\"NO\",\"COUNTRY_DEPLOYED\":\"ZIMBABWE\",\"PARENT_AGT_ALLOWED\":\"true\",\"MAIL_PROJ_NAME\":\"AfricaLotto\",\"RET_SCRAP_LIMIT\":\"20\",\"retAuth\":\"0\",\"raffle_ticket_type\":\"ORIGINAL\",\"GOVT_COMM_RULE\":\"fixedper\",\"TDS_PARTY_NAME\":\"Zimbabwe Revenue Services\",\"ADD_OF_THIRD_PARTY_GEN_MAPPING_ID\":\"192.168.124.254:8081/LMSWrapper\",\"ORG_LIST_TYPE\":\"NAME\",\"LOGIN_BINDING_PC\":\"NO\",\"autoArchiving\":\"true\",\"SELF_CLAIM_RET\":\"YES\",\"BO_AUTO_PWT_UPDATE\":\"YES\",\"SALE_START_TIME\":\"07:00:00\",\"Draw_Schedule_Result_Delay\":\"0\",\"MAX_CLAIM_PER_TICKET_AGT\":\"10000\",\"TEXT_FOR_TAX\":\"Deduction\",\"DATABASE_HOST_ADDRESS\":\"192.168.4.36\",\"isOfflineFileApproval\":\"false\",\"sim_binding\":\"NO\",\"BOOK_ACTIVATION_AT\":\"AGENT-RETAILER\",\"PERM_COMB_DISP\":\"NO\",\"retAppLimit\":\"1000.00\",\"SYSTEM_AUTHENTICATION_USERNAME\":\"LMS\",\"TIME_FORMAT\":\"HH:mm:ss\",\"DATABASE_PASSWORD\":\"st\",\"USER_MAPPING_ID_DEPLOYMENT_DATE\":\"2014-06-29 00:00:00\",\"LOGIN_MAIL_ALERT\":\"NO\",\"IS_DRAW\":\"YES\",\"NO_OF_GAME_PER_PAGE\":\"10\",\"CHARTS_GRAPHS_PATH_IN_LMS\":\"/graphs/csvFiles/\",\"HOST\":\"http\",\"MAX_PER_DAY_PAY_LIMIT_FOR_RET\":\"5000\",\"HIGH_PRIZE_CRITERIA\":\"amt\",\"PW_MERCHANT_ID\":\"1\",\"CLAIM_AT_OTHER_AGT\":\"NO\",\"SAMPLE\":\"NO\",\"agtAppLimit\":\"1000.00\",\"RETAILER_PASS\":\"integer\",\"IS_MACHINE_ENABLED\":\"NO\",\"retPayLimit\":\"1000.00\",\"CANCEL_DURATION\":\"30\",\"MOBILE_NO_WLS\":\"9650615449,9910123243\",\"VERSION_DETAILS\":\"Release ZIM2015.11.23\",\"OLA_WITHDRAWL_ANYWHERE\":\"YES\",\"RANDOM_RET_ID_START_RANGE\":\"10001\",\"VAT_REF_NUMBER\":\"xxxxxx\",\"MAX_UNAUTH_ATTEMP_TRACK_TICKET\":\"30\",\"BARCODE_TYPE\":\"applet\",\"SCRATCH_PWT_PRINT\":\"NO\",\"IS_CS_SHOW_CIRCLE\":\"YES\",\"expiry_period\":\"60\",\"LAST_MONTH_SPAN_REPORT\":\"1\",\"AGT_PWT_COMM_RATE\":\"0\",\"ORG_TYPE_ON_TICKET\":\"RETAILER\",\"VIRTUAL_BETTING_SOURCE_ID\":\"7307\",\"netGamingUpdateMode\":\"AUTO\",\"MERCHANT_LIST_FOR_PWT_VERIFICATION\":\"Asoft\",\"RANDOM_RET_ID_END_RANGE\":\"25000\",\"arch_db_name\":\"LMS_ARCH_06062016\",\"DAILY_TRAINING_EXP_MODE\":\"AUTO\",\"PORT\":\":8080\",\"SCRATCH_INVOICING_METHOD_DEFAULT\":\"ON_SALES_RET\",\"GET_NO_OF_PRIZE\":\"VIRN\",\"date_format\":\"dd-MM-yyyy\",\"IS_AUTO_BLOCK_ACTIVE\":\"NO\",\"SE_LAST_SOLD_TKT_ENTRY\":\"NO\",\"SERVICE_DELEGATE_URL\":\"localhost:1099\",\"CURRENCY_SYMBOL\":\"USD\",\"SALE_END_TIME\":\"23:00:00\",\"BLOCK_ACTION\":\"NO_ACTION\",\"DG_WEB_IP\":\"localhost\",\"CS_ALLOWED_IP\":\"192.168.124.150 192.168.124.145\",\"SLE_LAST_TICKET_CANCEL\":\"YES\",\"DEPLOYMENT_DATE\":\"21-10-2010\",\"IS_CANCEL_DURATION\":\"false\",\"MAX_PER_DAY_PAY_LIMIT_FOR_AGENT\":\"5000\",\"RET_PASSWORD_SHOWN_STATUS\":\"YES\",\"IS_MPESA_ENABLE\":\"NO\",\"GOVT_COMM_RATE\":\"25\",\"ORG_NAME_JSP\":\"AFRICA LOTTO\",\"MESSAGE_INBOX_DELETE_STATUS\":\"NO\",\"CLAIM_AT_BO\":\"YES\",\"agtWithdrawalLimit\":\"10\",\"MOBILE_COUNTRY_CODE\":\"648\",\"IS_WEEKLY_DG_TRAINING_EXPENSE_ENABLED\":\"NO\",\"IS_DAILY_DG_TRAINING_EXPENSE_ENABLED\":\"NO\",\"PRINT_USING_APPLET\":\"YES\",\"TIME_ELAPSE_FOR_DASHBOARD_NEWCALL\":\"60\",\"EXCLUDE_CURRENT_DAY_SALE\":\"NO\",\"PWT_CLAIM_EVERYWHERE\":\"YES\",\"RET_OFFLINE\":\"NO\",\"PWT_LIMIT\":\"1000\",\"OLA_MIN_DEPOSIT_LIMIT\":\"5\",\"SLE_MAS_APPROVE_LIMIT\":\"6000\",\"FlashMsg\":\"Welcome Message From Winlot\",\"DO_MATH_ROUNDING_FOR_SALE_AMT\":\"YES\",\"DATABASE_USER_NAME\":\"root\",\"ON_FREEZE_SALE\":\"YES\",\"TwelveByTwentyFourFreeAmtLimit\":\"10000\",\"IS_RECEIPT_WISE\":\"true\",\"IS_DAILY_IW_INCENTIVE_EXPENSE_ENABLED\":\"NO\",\"DRAW_GAME_HIGH_PRIZE_SCHEME\":\"TICKET_WISE\",\"VAT_APPLICABLE\":\"yes\",\"GPS_ACTIVATION\":\"NO\",\"NEW_USERID_ADDITION_VALUE\":\"10000\",\"LOGIN_BINDING\":\"NO\",\"retDepositLimit\":\"1000\",\"IS_WEEKLY_IW_INCENTIVE_EXPENSE_ENABLED\":\"NO\",\"IS_BARCODE_REQUIRED\":\"true\",\"IS_MAIL_SEND\":\"NO\",\"RAFFLE_GAME_DATA\":\"\",\"retScrapLimit\":\"1000.00\",\"HIGH_PRIZE_AMT\":\"100000000000\",\"JBOSS_LOG_PATH\":\"/root/server.log\",\"SELF_CLAIM_AGT\":\"NO\",\"WEAVER_CARD_DURING_SALE\":\"TRUE\",\"DECIMAL_FORMAT\":\"#,##0.00\",\"retVerLimit\":\"1000.00\",\"ORG_LIST_ORDER\":\"ASC\",\"PWT_APPROVAL_LIMIT\":\"10000000000\",\"RET_SALE_COMM_RATE\":\"0\",\"Fixed_%_SALE\":\"10\",\"IS_DIRECT_CASH_ENABLE_FOR_RETAILER\":\"NO\",\"AGT_SALE_COMM_RATE\":\"0\",\"PMS_WebLink\":\"http://localhost:8080/PMS\",\"DATABASE_NAME\":\"lms_zim\",\"IS_PLAYER_MOBILE_REQ\":\"N\",\"IS_SCRATCH\":\"YES\",\"JSP_PAGE_TITLE\":\"Lottery Management System: Africa Lotto\",\"VIRTUAL_BETTING_AUTHENTICATION_SIGNATURE\":\"a36accc86112746c94df895dfe3b4326\",\"IS_WEEKLY_IW_TRAINING_EXPENSE_ENABLED\":\"NO\",\"JBOSS_PATH\":\"/home/jboss/server/default/deploy/LMSLinuxNew.war/WEB-INF/classes/config/Audit_Script.py\",\"BLOCK_DAYS\":\"1\",\"SESSION_VARIABLES\":\"USER_INFO,ROOT_PATH,ACTION_LIST,date_format,FIRST,presentDate,PRIV_MAP,jre_version\",\"CANCEL_TYPE\":\"LAST_SOLD_TICKET\",\"retWithdrawalLimit\":\"1000\",\"LOGIN_ATTEMPTS\":\"9\",\"CRITICAL_EMAIL_USERS\":\"mandeep.mukheja@skilrock.com,support.wgrl@skilrock.com,yogesh@skilrock.com\",\"PW_PAYWORLD_API_VERSION\":\"3.34\",\"InpType\":\"3\",\"WEEKLY_TRAINING_EXP_MODE\":\"MANUAL\",\"DRAW_GAME_CANCELLATION_CHARGES\":\"0.00\",\"EBET_EXPIRY_PERIOD\":\"7\",\"AGT_SCRAP_LIMIT\":\"1001\",\"CLAIM_AT_OTHER_RET_SAME_AGT\":\"YES\",\"SYSTEM_AUTHENTICATION_PASSWORD\":\"password!\",\"TICKET_EXPIRY_ENABLED\":\"NO\",\"PW_MERCHANT_LOGIN_STATUS\":\"LIVE\",\"GOVT_COMM_PARTY_NAME\":\"Africa Lotto\",\"IW_HIGH_PRIZE_AMT\":\"5000\",\"RET_SCRAP\":\"NO\",\"MIN_CLAIM_PER_TICKET_AGT\":\"0.5\",\"OTHER_CLAIM_AGT\":\"NO\",\"REPRINT_TYPE\":\"LAST_SOLD_TICKET\",\"isICS\":\"0\",\"INCLUDE_HOLIDAY\":\"NO\",\"CS_PROVIDER\":\"PAYWORLD\",\"OLA_COMM_UPDATE_TYPE\":\"WEEKLY\",\"IS_DIRECT_DEBIT_NOTE_ENABLE_FOR_RETAILER\":\"NO\",\"RET_SALE_BOUND\":\"Buy,verifyTicket,cancelTicket,reprintTicket,csTerminalSale,ret_cs_sale_Submit\",\"GAME_DEV_NAME_FOR_DRAW_WISE_GRAPH\":\"ZimLottoBonusTwo\",\"PW_PAYWORLD_SERVER_PATH\":\"http://192.168.126.7:8181/zim/mainlinkpos/purchase/\",\"APPLET_SIGNED\":\"SIGNED\",\"IS_DATA_FROM_REPLICA\":\"NO\",\"VIRTUAL_BETTING_API_URL\":\"https://asia.golden-race.net/\",\"CURRENT_TERMINAL_BUILD_VERSION\":\"9.72\",\"EMBED_REPORT_DAYS\":\"100\",\"AUTO_CANCEL_CLOSER_DAYS\":\"3\",\"IS_MUL_LEDGER_TYPE\":\"true\",\"CLAIM_BY_CLICK\":\"YES\",\"WEB_LINK\":\"localhost\",\"MAXIMUM_AUTOBLOCK_DAYS\":\"5\",\"ORG_LOGO_FILE\":\"C\\:\\\\Documents and Settings\\\\abc\\\\workspace\\\\Integ\",\"archiving_duration\":\"2\",\"CLAIM_AT_OTHER_RET\":\"YES\",\"IW_MAS_APPROVE_LIMIT\":\"6000\",\"SCRATCH_PWT_WIN_PRINT\":\"YES\",\"REAL_TIME_LOCATION_UPDATE\":\"YES\",\"VAT_PARTY_NAME\":\"Zimbabwe Revenue Services\",\"PLAYER_WINNING_TAX_APPLICABLE_AMOUNT\":\"10000\",\"REPORTING_OFF_END_TIME\":\"20:00:00\",\"SCRATCH_WINNING_AFTER_SOLD_STATUS\":\"YES\",\"IS_WRAPPER_ENABLED\":\"YES\",\"OTHER_CLAIM_RET\":\"YES\",\"RET_OUTSTANDING_BALANCE_LIMIT_AUTO_BLOCK\":\"0\",\"DG_SCH_IP\":\"localhost\",\"MAX_DEVICE_PER_RETAILER\":\"5\",\"agtVerLimit\":\"1000.00\",\"PW_MERCHANT_PWD\":\"\",\"USER_MAPPING_ID_EXPIRY\":\"4\",\"MIN_TERMINAL_VERSION\":\"10.86\",\"SLE_HIGH_PRIZE_AMT\":\"5000\",\"AMOUNT_FOR_LONG_BEEP\":\"50000\",\"MIN_CLAIM_PER_TICKET_RET\":\"0.5\"}";
		Map<Integer, GameMasterLMSBean> gameMap = new Gson().fromJson(gameData,
				new TypeToken<HashMap<Integer, GameMasterLMSBean>>() {
				}.getType());
		Util.setGameMap(gameMap);
		ServerStartUpData.onStartOrganizationData();
		ServerStartUpData.onStartAdvMessageData();
		Utility.setLmsPropertyMap((HashMap<String, String>) new Gson().fromJson(propertyMapData,
				new TypeToken<HashMap<String, String>>() {
				}.getType()));
		
	}

	@Before
	public void zimLottoBonusPurchaseTicket_SetRespGamingCriteriaStatusMap() {
		Map<String, ResponsibleGamingBean> respGamingCriteriaStatusMap = new HashMap<String, ResponsibleGamingBean>();
		respCritBean.setCriteriaLimit(90000.0);
		respCritBean.setStatus("ACTIVE");
		respCritBean.setOrgType("RETAILER");
		respCritBean.setCriAction("NO_ACTION");
		respGamingCriteriaStatusMap.put("dg_sale_amtWEEKLY", respCritBean);

		respCritBean.setCriteriaLimit(10000.0);
		respCritBean.setStatus("ACTIVE");
		respCritBean.setOrgType("RETAILER");
		respCritBean.setCriAction("NO_ACTION");
		respGamingCriteriaStatusMap.put("dg_sale_amtDAILY", respCritBean);
	}

	@Before
	public void zimLottoBonusPurchaseTicket_SetUpUserInfoBean() {
		String userBeanData = "{\"availableCreditLimit\":301279.7,\"claimableBal\":-5.05,\"currentCreditAmt\":-300279.7,\"isMasterRole\":\"Y\",\"isRoleHeadUser\":\"Y\",\"loginChannel\":\"WEB\",\"orgName\":\"Test Retailer\",\"orgStatus\":\"ACTIVE\",\"parentUserId\":0,\"parentOrgId\":2,\"parentOrgName\":\"Test Agent\",\"parentOrgCode\":\"testagent\",\"parentOrgStatus\":\"ACTIVE\",\"pwtSacrap\":\"YES\",\"roleId\":3,\"roleName\":\"RETAILER MASTER\",\"status\":\"ACTIVE\",\"tierId\":3,\"unclaimableBal\":0.0,\"userId\":11004,\"currentUserMappingId\":17128,\"userName\":\"testret\",\"userOrgId\":3,\"userType\":\"RETAILER\",\"isTPUser\":false,\"orgCode\":\"Test Retailer\",\"userOrgCode\":\"testagent1\",\"terminalBuildVersion\":0.0,\"userSession\":\"FEC1D21BC178428539ED0943839B1B25\"}";
		userBean = new Gson().fromJson(userBeanData, UserInfoBean.class);
	}

	@Before
	public void zimLottoBonusPurchaseTicket_SetUpDrawIDTableMap() {
		drawMap.put(256, "256");
		drawMap.put(257, "257");
		drawMap.put(257, "257");
		drawIdTableMap.put(5, drawMap);
		Util.drawIdTableMap = drawIdTableMap;
	}

	private IServiceDelegate zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate() {
		IServiceDelegate iServiceDelegate = new IServiceDelegate() {
			
			@Override
			public ServiceResponse getResponse(ServiceRequest sReq) {
				ServiceResponse response = new ServiceResponse();
				JsonElement responseData =  new Gson().fromJson("{\"drawNameList\":[\"null\"],\"barcodeCount\":27,\"gameId\":5,\"isQuickPick\":[1,1,1,1],\"noOfLines\":4,\"noPicked\":6,\"panel_id\":[1,2,3,4],\"picknumbers\":[\"02,03,13,14,27,40\",\"06,11,12,14,17,32\",\"03,14,18,19,20,39\",\"05,06,13,15,16,29\"],\"playerPicked\":[\"02,03,13,14,27,40\",\"06,11,12,14,17,32\",\"03,14,18,19,20,39\",\"05,06,13,15,16,29\"],\"playType\":\"Direct6\",\"betAmtMultiple\":5,\"unitPrice\":0.2,\"promoSaleStatus\":\"SUCCESS\",\"userMappingId\":17128,\"serviceId\":3,\"dgTxId\":0,\"QPPreGenerated\":true,\"betAmountMultiple\":[5,5,5,5],\"drawDateTime\":[\"2016-11-16 19:45:00&256\"],\"drawIdTableMap\":{\"5\":{\"256\":\"256\",\"257\":\"257\"}},\"game_no\":5,\"gameDispName\":\"Bonus Lotto\",\"isAdvancedPlay\":0,\"isPromotkt\":false,\"isRaffelAssociated\":false,\"noOfDraws\":1,\"partyId\":3,\"partyType\":\"RETAILER\",\"purchaseChannel\":\"LMS_Web\",\"purchaseTime\":\"2016-11-16 18:21:29\",\"raffleNo\":0,\"refMerchantId\":\"WGRL\",\"refTransId\":\"13204548\",\"reprintCount\":\"0\",\"saleStatus\":\"FAILED\",\"ticket_no\":\"78303532117128010\",\"totalPurchaseAmt\":4.0,\"roundedPurchaseAmt\":0.0,\"userId\":11004}", JsonElement.class);
				response.setResponseData(responseData);
				response.setIsSuccess(true);
				return response;
			}

			@Override
			public String getResponseString(ServiceRequest sReq) {
				return null;
			}

			@Override
			public String getResponseStringForTrainingExpense(ServiceRequest sReq) {
				return null;
			}
		};
		return iServiceDelegate;
	}

	private void zimLottoBonusPurchaseTicket_PowerMockStaticMethods() throws Exception {
		PowerMockito.mockStatic(orgOnLineSaleCreditUpdation.class);
		PowerMockito
				.when(orgOnLineSaleCreditUpdation.class, "drawTcketSaleBalDeduction", Mockito.any(UserInfoBean.class),
						Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyString(), Mockito.any(Connection.class))
				.thenReturn(13204548L);
		PowerMockito.when(orgOnLineSaleCreditUpdation.class, "drawTicketSaleTicketUpdate", Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.any(UserInfoBean.class), Mockito.anyString(),
				Mockito.any(Connection.class)).thenReturn(1);
		PowerMockito.mockStatic(IDBarcode.class);
		PowerMockito.when(IDBarcode.getBarcode(Mockito.anyString())).thenReturn(null);
	}
	private void zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForDirectSix() {
		drawPurchaseBean.setGameId(5);
		drawPurchaseBean.setPlayType("Direct6");
		drawPurchaseBean.setPicknumbers(
				new String[] { "02,03,13,14,27,40", "06,11,12,14,17,32", "03,14,18,19,20,39", "05,06,13,15,16,29" });
		drawPurchaseBean.setNoPicked(6);
		drawPurchaseBean.setNoOfDraws(1);
		drawPurchaseBean.setGame_no(5);
		drawPurchaseBean.setGameDispName("Bonus Lotto");
		drawPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawPurchaseBean.setRefMerchantId("WGRL");
		drawPurchaseBean.setIsQuickPick(new Integer[] { 1, 1, 1, 1 });
		drawPurchaseBean.setDrawDateTime(Arrays.asList(""));
		drawPurchaseBean.setPurchaseChannel("LMS_Web");
		drawPurchaseBean.setServiceId(3);
		drawPurchaseBean.setIsAdvancedPlay(0);
		drawPurchaseBean.setUserId(11004);
		drawPurchaseBean.setPartyId(3);
		drawPurchaseBean.setPartyType("RETAILER");
		drawPurchaseBean.setUserMappingId(17128);
		drawPurchaseBean.setTotalPurchaseAmt(4.0);
		drawPurchaseBean.setBetAmountMultiple(new int[] { 5, 5, 5, 5 });
		drawPurchaseBean.setQPPreGenerated(true);
		drawPurchaseBean.setBetAmtMultiple(5);
	}
	private void zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix() {
		drawPurchaseBean.setGameId(5);
		drawPurchaseBean.setPlayType("Perm6");
		drawPurchaseBean.setPicknumbers(
				new String[] {"02,09,11,13,22,24,27,29,32,42"});
		drawPurchaseBean.setNoPicked(10);
		drawPurchaseBean.setNoOfDraws(1);
		drawPurchaseBean.setGame_no(5);
		drawPurchaseBean.setGameDispName("Bonus Lotto");
		drawPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawPurchaseBean.setRefMerchantId("WGRL");
		drawPurchaseBean.setIsQuickPick(new Integer[] { 1 });
		drawPurchaseBean.setDrawDateTime(Arrays.asList(""));
		drawPurchaseBean.setPurchaseChannel("LMS_Web");
		drawPurchaseBean.setServiceId(3);
		drawPurchaseBean.setIsAdvancedPlay(0);
		drawPurchaseBean.setUserId(11004);
		drawPurchaseBean.setPartyId(3);
		drawPurchaseBean.setPartyType("RETAILER");
		drawPurchaseBean.setUserMappingId(17128);
		drawPurchaseBean.setTotalPurchaseAmt(42.0);
		drawPurchaseBean.setBetAmountMultiple(new int[] { 1 });
		drawPurchaseBean.setQPPreGenerated(true);
		drawPurchaseBean.setBetAmtMultiple(1);
	}
	
	@Test
	public void zimLottoBonusPurchaseTicket_NoOfLinesIsNotZero() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForDirectSix();
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		AjaxRequestHelper ajaxHelper = null;
		LottoPurchaseBean zimLottoBonusPurchaseBean = new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,
				drawPurchaseBean);
		Assert.assertTrue("Number of lines is zero", 0 < zimLottoBonusPurchaseBean.getNoOfLines());
	}
	@Test
	public void zimLottoBonusPurchaseTicket_SaleStatusIsSuccessForDirectSix() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForDirectSix();
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		LottoPurchaseBean zimLottoBonusPurchaseBean = new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,
				drawPurchaseBean);
		Assert.assertEquals("Sale status is Failed", "SUCCESS", zimLottoBonusPurchaseBean.getSaleStatus());
	}
	@Test
	public void zimLottoBonusPurchaseTicket_SaleStatusIsSuccessForPermSix() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix();
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		LottoPurchaseBean zimLottoBonusPurchaseBean = new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,
				drawPurchaseBean);
		Assert.assertEquals("Sale status is Failed", "SUCCESS", zimLottoBonusPurchaseBean.getSaleStatus());
	}
	@Test
	public void zimLottoBonusPurchaseTicket_SaleStatusFailedIfNumberPickedIsGreaterThanFifteen() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix();
		drawPurchaseBean.setNoPicked(20);
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		LottoPurchaseBean zimLottoBonusPurchaseBean = new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,
				drawPurchaseBean);
		Assert.assertEquals("Sale status is Success", "FAILED", zimLottoBonusPurchaseBean.getSaleStatus());
	}
	@Test
	public void zimLottoBonusPurchaseTicket_SaleStatusFailedIfNumberPickedIsLessThanSeven() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix();
		drawPurchaseBean.setNoPicked(2);
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		LottoPurchaseBean zimLottoBonusPurchaseBean = new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,
				drawPurchaseBean);
		Assert.assertEquals("Sale status is Success", "FAILED", zimLottoBonusPurchaseBean.getSaleStatus());
	}
	@Test (expected = LMSException.class)
	public void zimLottoBonusPurchaseTicket_UserBeanIsNull() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix();
		userBean = null;
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,drawPurchaseBean);
	}
	@Test (expected = NullPointerException.class)
	public void zimLottoBonusPurchaseTicket_LottoPurchaseBeanIsNull() throws Exception {
		zimLottoBonusPurchaseTicket_SetUpDrawPurchaseBeanForPermSix();
		drawPurchaseBean = null;
		zimLottoBonusPurchaseTicket_PowerMockStaticMethods();
		AjaxRequestHelper ajaxHelper = new AjaxRequestHelper();
		IServiceDelegate iServiceDelegate = zimLottoBonusPurchaseTicket_InnerClassIServiceDelegate();
		new DrawGameRPOSHelper(iServiceDelegate,ajaxHelper).zimLottoBonusPurchaseTicket(userBean,drawPurchaseBean);
	}
}
