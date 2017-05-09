package com.skilrock.lms.rest.scratchService.pwtMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.Gson;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.ScratchPayPWTBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.RetailerPwtProcessHelper;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchTicketVirnData;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchWinningDataBean;
import com.skilrock.lms.rest.scratchService.pwtMgmt.daoImpl.ScratchWinningDaoImpl;
import com.skilrock.lms.rest.services.bean.ScratchWinningPaymentRequest;


@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.skilrock.lms.common.db.DBConnect","com.skilrock.lms.common.Utility"})
@PrepareForTest({MD5Encoder.class,DBConnect.class,Utility.class})
public class ScratchWinningServiceImplTest {
	
	private  final String HIGH_PRIZE_VERIFY_SUCCEESS_RESPONSE="{\"vCode\":\"7NJTUx1PPI/iny+FP9CTWQ\u003d\u003d\",\"pwtAmount\":\"15.00\",\"prizeLevel\":\"HIGH\",\"prizeStatus\":\"UNCLM_PWT\"}";
	private  final String NO_PRIZE_PWT_RESPONSE="{\"vCode\":\"7NJTUx1PPI/iny+FP9CTWQ\u003d\u003d\",\"pwtAmount\":\"15.00\",\"prizeLevel\":\"HIGH\",\"prizeStatus\":\"NO_PRIZE_PWT\"}";
	private  final String CLAIMED_PWT_RESPONSE="{\"vCode\":\"7NJTUx1PPI/iny+FP9CTWQ\u003d\u003d\",\"pwtAmount\":\"15.00\",\"prizeLevel\":\"HIGH\",\"prizeStatus\":\"CLAIMED\"}";
	private  final String NORMAL_UNCLAIMED_VERIFY_SUCCEESS_RESPONSE="{\"vCode\":\"7NJTUx1PPI/iny+FP9CTWQ\u003d\u003d\",\"pwtAmount\":\"6.00\",\"prizeLevel\":\"HIGH\",\"prizeStatus\":\"UNCLM_PWT\"}";
	private  final String ORG_LIMIT_BEAN_DATA="{\"approvalLimit\":1000.0,\"isPwtAutoScrap\":\"YES\",\"organizationId\":3,\"payLimit\":1000.0,\"scrapLimit\":1000.0,\"verificationLimit\":5.0,\"olaDepositLimit\":0.0,\"olaWithdrawlLimit\":0.0,\"claimAanyTicket\":false,\"actingAspwt\":false,\"maxDailyClaim\":0.0,\"levyRate\":0.0,\"securityDepositRate\":0.0,\"minClaimPerTicket\":0.0,\"maxClaimPerTicket\":0.0,\"blockAmt\":0.0,\"blockDays\":0}";
	ScratchDao scratchDao = Mockito.mock(ScratchDaoImpl.class); 
	CommonFunctionsHelper commonFunctionsHelper = Mockito.mock(CommonFunctionsHelper.class);
	RetailerPwtProcessHelper retailerPwtProcessHelper = Mockito.mock(RetailerPwtProcessHelper.class);
	ScratchWinningDaoImpl scratchWinningDaoImpl = Mockito.mock(ScratchWinningDaoImpl.class);
	ScratchWinningServiceImpl scratchWinningServiceImpl = new ScratchWinningServiceImpl(scratchDao, commonFunctionsHelper, retailerPwtProcessHelper, scratchWinningDaoImpl);
	TicketBean ticketBean1 = null;
	TicketBean ticketBean2 = null;
	UserInfoBean userInfoBean = null;
	ScratchWinningPaymentRequest scratchWinningPaymentRequest = null;
	ScratchWinningDataBean scratchWinningDataBean = null;
	ScratchGameDataBean scratchGameDataBean = null;
	OrgPwtLimitBean orgPwtLimitBean = null;;
	Connection connection = null;
	
	@Rule
	ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setTestCaseData(){
		ticketBean1 = new TicketBean();
		ticketBean1.setValid(false);
		ticketBean1.setTicketNumber("323-001001-08");
		ticketBean2 = new TicketBean();
		ticketBean2.setValid(true);
		ticketBean2.setTicketNumber("323-001001-08");
		userInfoBean = new UserInfoBean();
		userInfoBean.setParentOrgId(10);
		userInfoBean.setUserId(1);
		scratchWinningPaymentRequest = new ScratchWinningPaymentRequest();
		scratchWinningPaymentRequest.setRequestId("545466847");
		scratchWinningPaymentRequest.setTpUserId("12015");
		scratchWinningPaymentRequest.setTicketNumber("456-01210588");
		scratchWinningPaymentRequest.setVirnNumber("56465489894365");
		scratchWinningDataBean = new ScratchWinningDataBean();
		scratchWinningDataBean.setStatus("WIN");
		scratchWinningDataBean.setWinningAmount(1235.00);
		scratchGameDataBean  = new ScratchGameDataBean();
		scratchGameDataBean.setGameId(1);
		scratchGameDataBean.setGameNbr(423);
		scratchGameDataBean.setGameName("sdfdsf");
		orgPwtLimitBean = new OrgPwtLimitBean();
		connection = Mockito.mock(Connection.class);
	}
	
	/*
	 * Ticket Verification Test Case Start
	 * */
	
	@Before
	public void preDataSetForWinningVerification() throws Exception{
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", "HIGH_PRIZE_CRITERIA").thenReturn("amt");
		PowerMockito.when(Utility.class, "getPropertyValue", "HIGH_PRIZE_AMT").thenReturn("10.0");
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.encode(Mockito.anyString())).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
	}
	
	@Test
	public void verifyTicketAndVirn_ThrowInvalidTicketIfTicketBeanIsNotNull() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean1);
		try {
			scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		} catch (LMSException e) {
			Assert.assertEquals("Invalid Ticket No", e.getErrorMessage());
		}
	}
	
	@Test
	public void verifyTicketAndVirn_ThrowInvalidTicketIfTicketBeanIsNull() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		} catch (LMSException e) {
			Assert.assertEquals("Invalid Ticket No", e.getErrorMessage());
		}
	}
	
	@Test
	public void verifyTicketAndVirn_ThrowInvalidTicketIfCheckTicketStatusReturnsFalseWithUnknownErrorCode() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean1);
		try {
			scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		} catch (LMSException e) {
			Assert.assertEquals("Invalid Ticket No", e.getErrorMessage());
		}
	}
	
	@Test
	public void verifyTicketAndVirn_CheckWhenTicketStatusIsClaimed() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		ticketBean1.setMessageCode("221002");
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean1);
		ScratchWinningDataBean scratchWinningDataBean=scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		Assert.assertEquals("CLAIMED",scratchWinningDataBean.getStatus());
	
	}
	
	
	
	@Test
	public void verifyTicketAndVirn_ThrowSomeInternalErrorIfDaoLayerReturnNull() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		try {
			scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		} catch (LMSException e) {
			Assert.assertEquals("Some Internal Error !", e.getErrorMessage());
		}
	}
	
	@Test
	public void verifyTicketAndVirn_CheckHighPrizeWinningTicket() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(scratchWinningDaoImpl.verifyAndGetTicketVirnData(Mockito.anyString(), Mockito.any(ScratchGameDataBean.class), Mockito.any(Connection.class), Mockito.any(TicketBean.class))).thenReturn(new Gson().fromJson(HIGH_PRIZE_VERIFY_SUCCEESS_RESPONSE, ScratchTicketVirnData.class));
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(new Gson().fromJson(ORG_LIMIT_BEAN_DATA, OrgPwtLimitBean.class));
		ScratchWinningDataBean scratchWinningDataBean=scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		Assert.assertEquals("HIGH_PRIZE", scratchWinningDataBean.getStatus());
	}
	
	@Ignore
	@Test
	public void verifyTicketAndVirn_CheckNormalUnclaimedeWinningTicket() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(scratchWinningDaoImpl.verifyAndGetTicketVirnData(Mockito.anyString(), Mockito.any(ScratchGameDataBean.class), Mockito.any(Connection.class), Mockito.any(TicketBean.class))).thenReturn(new Gson().fromJson(NORMAL_UNCLAIMED_VERIFY_SUCCEESS_RESPONSE, ScratchTicketVirnData.class));
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(new Gson().fromJson(ORG_LIMIT_BEAN_DATA, OrgPwtLimitBean.class));
		ScratchWinningDataBean scratchWinningDataBean=scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		Assert.assertEquals("UNCLAIMED", scratchWinningDataBean.getStatus());
	}
	
	
	@Test
	public void verifyTicketAndVirn_CheckNoPrizeTicket() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(scratchWinningDaoImpl.verifyAndGetTicketVirnData(Mockito.anyString(), Mockito.any(ScratchGameDataBean.class), Mockito.any(Connection.class), Mockito.any(TicketBean.class))).thenReturn(new Gson().fromJson(NO_PRIZE_PWT_RESPONSE, ScratchTicketVirnData.class));
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(new Gson().fromJson(ORG_LIMIT_BEAN_DATA, OrgPwtLimitBean.class));
		ScratchWinningDataBean scratchWinningDataBean=scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		Assert.assertEquals("NON_WIN", scratchWinningDataBean.getStatus());
	}
	
	@Test
	public void verifyTicketAndVirn_CheckCLAIMEDTicket() throws Exception{
		preDataSetForWinningVerification();
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(scratchWinningDaoImpl.verifyAndGetTicketVirnData(Mockito.anyString(), Mockito.any(ScratchGameDataBean.class), Mockito.any(Connection.class), Mockito.any(TicketBean.class))).thenReturn(new Gson().fromJson(CLAIMED_PWT_RESPONSE, ScratchTicketVirnData.class));
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(new Gson().fromJson(ORG_LIMIT_BEAN_DATA, OrgPwtLimitBean.class));
		ScratchWinningDataBean scratchWinningDataBean=scratchWinningServiceImpl.verifyTicketAndVirn("555-001001-01", "2229755576", userInfoBean);
		Assert.assertEquals("CLAIMED", scratchWinningDataBean.getStatus());
	}
	
	/*
	 * Ticket Verification Test Case Stop
	 * */
	
	@Test
	public void payPWTProcess_ThrowInvalidTicketIfTicketBeanIsNotNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.encode(Mockito.anyString())).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean1);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals("Invalid Ticket No", e.getErrorMessage());
		}
	}
	
	@Test
	public void payPWTProcess_Throw121ErrorCodeIfTicketBeanIsNotNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.encode(Mockito.anyString())).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean1);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals(121, e.getErrorCode().intValue());
		}
	}
	
	@Test
	public void payPWTProcess_ThrowsPayLimitExceededIfPWTAmountIsGreaterThanPayLimit() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(orgPwtLimitBean);
		Mockito.doThrow(new LMSException(ScratchErrors.LIMIT_EXCEEDED_ERROR_CODE,ScratchErrors.LIMIT_EXCEEDED_ERROR_MESSAGE)).when(scratchWinningDaoImpl).payPWTProcess(Mockito.any(ScratchPayPWTBean.class), Mockito.any(Connection.class));
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals("Limit Exceeded", e.getErrorMessage());
		}
	}
	
	@Test
	public void payPWTProcess_Throws515ErrorCodeIfPWTAmountIsGreaterThanPayLimit() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(orgPwtLimitBean);
		Mockito.doThrow(new LMSException(ScratchErrors.LIMIT_EXCEEDED_ERROR_CODE,ScratchErrors.LIMIT_EXCEEDED_ERROR_MESSAGE)).when(scratchWinningDaoImpl).payPWTProcess(Mockito.any(ScratchPayPWTBean.class), Mockito.any(Connection.class));
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals(515, e.getErrorCode().intValue());
		}
	}
	
	@Test
	public void payPWTProcess_ThrowScratchGameNotAvailableIfScratchGameDataBeanIsNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals("Scratch Game Not Available", e.getErrorMessage());
		}
	}
	
	@Test
	public void payPWTProcess_Throw140ErrorCodeIfScratchGameDataBeanIsNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals(140, e.getErrorCode().intValue());
		}
	}
	
	@Test
	public void payPWTProcess_ThrowGeneralExceptionIfOrgPwtLimitBeanIsNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals("Some Internal Error !", e.getErrorMessage());
		}
	}
	
	@Test
	public void payPWTProcess_Throw2002IfOrgPwtLimitBeanIsNull() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(retailerPwtProcessHelper.checkTicketStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(ticketBean2);
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals(2002, e.getErrorCode().intValue());
		}
	}
	
	@Test
	public void payPWTProcess_ThrowGeneralExceptionIfSQLExceptionThrownFromIsTicketFormatValid() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class, "getConnection").thenReturn(connection);
		PowerMockito.mockStatic(MD5Encoder.class);
		PowerMockito.when(MD5Encoder.class,"encode",Mockito.anyString()).thenReturn("YU2FOPJ1VQS83fa+-256DAF==");
		Mockito.when(scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(Mockito.anyString(), Mockito.any(Connection.class))).thenReturn(scratchGameDataBean);
		Mockito.when(commonFunctionsHelper.isTicketFormatValid(Mockito.anyString(), Mockito.anyInt(), Mockito.any(Connection.class))).thenThrow(new SQLException());
		Mockito.when(commonFunctionsHelper.fetchPwtLimitsOfOrgnization(Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(null);
		try {
			scratchWinningServiceImpl.payPWTProcess(userInfoBean, scratchWinningPaymentRequest,scratchWinningDataBean);
		} catch (LMSException e) {
			Assert.assertEquals("Some Internal Error !", e.getErrorMessage());
		}
	}
}
