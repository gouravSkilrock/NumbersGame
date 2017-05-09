package com.skilrock.lms.rest.services.userMgmtService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.UserManagementHelper;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.UserRegistrationRequest;
import com.skilrock.lms.rest.services.common.TpUtility;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.skilrock.lms.web.drawGames.common.Util", "com.skilrock.lms.common.Utility"})
@PrepareForTest({ Utility.class,OrgNUserRegHelper.class,CommonMethods.class})
public class UserMgmtServiceIt {
	
	private UserMgmtService userReg;
	private ServletContext servletContext;
	private HttpServletRequest requestContext;
	private TpUtility tpUtil;
	private UserManagementHelper userMgmtHlpr;
	private ScratchDaoImpl scratchdao;
	
	@Before
	public void setUp() throws Exception{
		tpUtil = Mockito.mock(TpUtility.class);
		userMgmtHlpr = Mockito.mock(UserManagementHelper.class);
		scratchdao = Mockito.mock(ScratchDaoImpl.class);
		userReg = new UserMgmtService(userMgmtHlpr,scratchdao,tpUtil);
		
		setUpPowerMockForLMSUtilityClass();
		setUpPowerMockForUtilityClass();
	}
	
	private void setUpPowerMockForLMSUtilityClass() throws Exception{
		servletContext = Mockito.mock(ServletContext.class);
		Mockito.when(servletContext.getAttribute("agtVerLimit")).thenReturn(1);
		Mockito.when(servletContext.getAttribute("agtAppLimit")).thenReturn(1);
		Mockito.when(servletContext.getAttribute("agtPayLimit")).thenReturn(1);
		Mockito.when(servletContext.getAttribute("agtScrapLimit")).thenReturn(1);
		Mockito.when(servletContext.getAttribute("RET_ONLINE")).thenReturn("Yes");
		Mockito.when(servletContext.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_RET")).thenReturn("12");
		LMSUtility.sc = servletContext;
		
	}
	
	private void setUpPowerMockForUtilityClass() throws Exception {
		Map<String, String> lmsPropertyMap = new HashMap<String, String>();
		lmsPropertyMap.put("BLOCK_ACTION", "Yes");
		lmsPropertyMap.put("MIN_CLAIM_PER_TICKET_RET","10.0");
		lmsPropertyMap.put("MAX_CLAIM_PER_TICKET_RET","20.0");
		lmsPropertyMap.put("BLOCK_AMT","10.0");
		lmsPropertyMap.put("BLOCK_DAYS","5");
		Utility.setLmsPropertyMap(lmsPropertyMap );
		Utility.setLmsPropertyMap(lmsPropertyMap );
	}
	
	@Test
	public void createUser_rejectNonJsonRequest() throws Exception {
		String requestData = "1";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2002,\"responseMsg\":\"Some Internal Error !\"}", responseData);
	}
	
	@Test
	public void createUser_returnExceptionForEmptyRequest() throws Exception {
		String requestData = null;
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"Invalid Input.\"}", responseData);
	}
	
	@Test
	public void createUser_createExceptionForWorngFirstName() throws Exception{
		int[] serviceList = {1,2};
		String[] statusTableValue = {"ACTIVE","TERMINATE"};
		
		Mockito.when(tpUtil.getServiceList()).thenReturn(serviceList);
		Mockito.when(tpUtil.getStatusTableValues()).thenReturn(statusTableValue);
		String requestData = "{\"requestId\":24012,\"tpUserId\":10236,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123\",\"securityDeposit\":\"124\",\"firstName\":\"afh123\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"First Name should contain alphabets only\"}", responseData);
	}
	
	@Test
	public void createUser_successResponseData() throws Exception{
		
		int[] serviceList = {1,2};
		String[] statusTableValue = {"ACTIVE","TERMINATE"};
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("NewPassword", "sdkhfkjsdh");
		errorMap.put("orgCode", "Ret123");
		
		Mockito.when(tpUtil.getServiceList()).thenReturn(serviceList);
		Mockito.when(tpUtil.getStatusTableValues()).thenReturn(statusTableValue);
		
		PowerMockito.mockStatic(OrgNUserRegHelper.class);
		PowerMockito.when(OrgNUserRegHelper.class, "createNewRetailerOrgNUser",Mockito.any(UserInfoBean.class),Mockito.any(UserRegistrationRequest.class),Mockito.anyString()).thenReturn(errorMap);
		
		String requestData = "{\"requestId\":24012,\"tpUserId\":19345,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123\",\"securityDeposit\":\"124\",\"firstName\":\"afh\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":24012,\"responseCode\":100,\"responseMsg\":\"Success\",\"requestData\":{\"refUserId\":0,\"tpUserId\":\"19345\",\"userName\":\"attbjryui\"}}", responseData);
	}
		
	@Test
	public void editUser_returnExceptionForEmptyRequest() throws Exception {
		String requestData = null;
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseData\":\"Invalid Input.\"}", responseData);
	}
		
	@Test
	public void editUser_returnExceptionForIpAddressNotGiven() throws LMSException {
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252190\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":24002,\"responseCode\":2002,\"tpUserId\":\"1001\",\"responseData\":\"Some Internal Error !\"}", responseData);
	}
		
	@Test
	public void editUser_returnSuccessForEditUserHelperTrueCase() throws LMSException, SQLException {
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"10028\", \"mobileNo\":\"8800252\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		requestContext = Mockito.mock(HttpServletRequest.class);
		Mockito.when(requestContext.getRemoteAddr()).thenReturn("192.168.124.215");
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId("1234")).thenReturn(Mockito.any(DaoBean.class));
		
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":24002,\"responseCode\":100,\"tpUserId\":\"10028\",\"responseData\":\"User Edit Successfully\"}", responseData);
	}

}
