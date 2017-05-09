package com.skilrock.lms.rest.services.userMgmtService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import com.skilrock.lms.rest.services.bean.EditUserRequestBean;
import com.skilrock.lms.rest.services.bean.UserRegistrationRequest;
import com.skilrock.lms.rest.services.common.TpUtility;


@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.skilrock.lms.web.drawGames.common.Util", "com.skilrock.lms.common.Utility", "com.skilrock.lms.rest.services.common.TpUtility"})
@PrepareForTest({ Utility.class,OrgNUserRegHelper.class,TpUtility.class,CommonMethods.class})
public class UserMgmtServiceTest {

	private UserMgmtService userReg;
	private ServletContext servletContext;
	private HttpServletRequest requestContext;
	private UserManagementHelper userMgmtHlpr;
	private ScratchDaoImpl scratchdao;
	private TpUtility tpUtil;

	@Before
	public void setUp() throws Exception{
		userMgmtHlpr = Mockito.mock(UserManagementHelper.class);
		scratchdao = Mockito.mock(ScratchDaoImpl.class);
		tpUtil = Mockito.mock(TpUtility.class);
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
	}

	 @Ignore("Tries to hit DB, Need to find a better approach")
	@Test
	public void createUser_acceptsJsonRequest() {
		String requestData = "{\"requestId\":24012,\"tpUserId\":10236,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123\",\"securityDeposit\":\"124\",\"firstName\":\"afh\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2002,\"responseMsg\":\"Some Internal Error !\"}", responseData);
	}

	/*@Test(expected = IllegalStateException.class)
	public void createUser_rejectNonJsonRequest() throws Exception {
		String requestData = "1";
		userReg.createUser(requestData);
	}*/
	
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
	public void createUser_returnExceptionForCreditLimitGreaterThanAgentLimit() throws Exception {
		
		PowerMockito.mockStatic(CommonMethods.class);
		PowerMockito.when(CommonMethods.class, "chkCreditLimitAgt",Mockito.anyInt(),Mockito.anyDouble()).thenReturn("You Can Accept 10000 Amount Only");
		
		String requestData = "{\"requestId\":24012,\"tpUserId\":10236,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123515645611\",\"securityDeposit\":\"12415454544544\",\"firstName\":\"afh\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"You Can Accept 10000 Amount Only\"}", responseData);
	}
	
	@Test
	public void createUser_returnExceptionForParamsNotPresent() throws Exception{
		String requestData = "{\"requestId\":24012,\"tpUserId\":10236,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123\",\"securityDeposit\":\"124\",\"firstName\":\"afh123\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseMsg\":\"First Name should contain alphabets only\"}", responseData);
	}
	
	@Test
	public void createUser_successResponseData() throws Exception{
		
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("NewPassword", "sdkhfkjsdh");
		errorMap.put("orgCode", "Ret123");
		
		PowerMockito.mockStatic(CommonMethods.class);
		PowerMockito.when(CommonMethods.class, "chkCreditLimitAgt",Mockito.anyInt(),Mockito.anyDouble()).thenReturn("TRUE");
		
		PowerMockito.mockStatic(OrgNUserRegHelper.class);
		PowerMockito.when(OrgNUserRegHelper.class, "createNewRetailerOrgNUser",Mockito.any(UserInfoBean.class),Mockito.any(UserRegistrationRequest.class),Mockito.anyString()).thenReturn(errorMap);
		
		String requestData = "{\"requestId\":24012,\"tpUserId\":10236,\"userName\":\"attbjryui\",\"partyName\":\"ptytbjser\",\"mobile\":\"8808961187\",\"email\":\"sam.atkis@hotmail.com\",\"parentUserId\":11005,\"userType\":\"BO\",\"orgName\":\"lotteytby\",\"address\":\"asfh\",\"state\":\"punwaz\",\"country\":\"ZIMBABWE\",\"city\":\"Zhombe\",\"creditLimit\":\"123\",\"securityDeposit\":\"124\",\"firstName\":\"afh\",\"lastName\":\"zvfbj\",\"idType\":\"passport\",\"idNo\":\"abfbbjty24\"}";
		String responseData = userReg.createUser(requestData);
		Assert.assertEquals("{\"requestId\":24012,\"responseCode\":100,\"responseMsg\":\"Success\",\"requestData\":{\"refUserId\":0,\"tpUserId\":\"10236\",\"userName\":\"attbjryui\"}}", responseData);
	}

	@Test
	public void createUser_MandatoryParamsValidation() {
		UserRegistrationRequest bean = new UserRegistrationRequest();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(bean);
		System.out.println(violations.isEmpty());
	}
	
	@Test
	public void editUser_acceptsJsonRequest() throws LMSException {
		
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252190\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":24002,\"responseCode\":2002,\"tpUserId\":\"1001\",\"responseData\":\"Some Internal Error !\"}", responseData);
	}
	
	@Test
	public void editUser_returnExceptionForEmptyRequest() throws Exception {
		String requestData = null;
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseData\":\"Invalid Input.\"}", responseData);
	}
	
	@Test
	public void editUser_MandatoryParamsValidation() {
		EditUserRequestBean editUserBean = new EditUserRequestBean();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<EditUserRequestBean>> violations = validator.validate(editUserBean);
		System.out.println(violations.isEmpty());
	}
		
	@Test
	public void editUser_returnExceptionForIpAddressNotGiven() throws LMSException {
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252190\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":24002,\"responseCode\":2002,\"tpUserId\":\"1001\",\"responseData\":\"Some Internal Error !\"}", responseData);
	}
	
	@Test
	public void editUser_returnExceptionForUserIdNotPresent() throws LMSException {
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252190\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		requestContext = Mockito.mock(HttpServletRequest.class);
		Mockito.when(requestContext.getRemoteAddr()).thenReturn("192.168.124.215");
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseData\":\"Invalid Input.\"}", responseData);
	}
	
	@Test
	public void editUser_returnExceptionForEditUserHelperFnc() throws LMSException, SQLException {
		DaoBean value = new DaoBean();
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252190\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		requestContext = Mockito.mock(HttpServletRequest.class);
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(value);
		Mockito.when(requestContext.getRemoteAddr()).thenReturn("192.168.124.215");
		
  		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseData\":\"Invalid Input.\"}", responseData);
	}

	@Test
	public void editUser_returnExceptionForEditUserHelperFalseCase() throws LMSException, SQLException {
		DaoBean value = new DaoBean();
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		requestContext = Mockito.mock(HttpServletRequest.class);
		Mockito.when(requestContext.getRemoteAddr()).thenReturn("192.168.124.215");
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(value);
		Mockito.when(userMgmtHlpr.editUserDetails(Mockito.anyInt(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		
		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":0,\"responseCode\":2021,\"responseData\":\"Invalid Input.\"}", responseData);
	}
	
	@Test
	public void editUser_returnSuccessForEditUserHelperTrueCase() throws LMSException, SQLException {
		DaoBean value = new DaoBean();
		String requestData = "{\"requestId\": \"24002\", \"tpUserId\":\"1001\", \"mobileNo\":\"8800252\", \"emailId\":\"sam.atkiston@hotmail.com\", \"status\":\"TERMINATE\"}";
		requestContext = Mockito.mock(HttpServletRequest.class);
		Mockito.when(requestContext.getRemoteAddr()).thenReturn("192.168.124.215");
		Mockito.when(scratchdao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(value);
		Mockito.when(userMgmtHlpr.editUserDetails(Mockito.anyInt(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);

		String responseData = userReg.editUser(requestContext, requestData);
		Assert.assertEquals("{\"requestId\":24002,\"responseCode\":100,\"tpUserId\":\"1001\",\"responseData\":\"User Edit Successfully\"}", responseData);
	}
	
}
