package com.skilrock.lms.api.lmsWrapper;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.api.lmsWrapper.beans.AgentInfoBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperAgentListBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperAllRetInfoBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperBankDepositBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashPaymentBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataForPWTBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataReportBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCollectionReportOverAllBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCompleteCollectionBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperDrawscheduleBeanResult;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperGameDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperInventoryAssignDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperInventoryMenuDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOnStartGameDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrgRegShiftBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrganizationRegShiftDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPerformDrawDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPwtApiBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRandomIdRequestBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRandomIdResponseBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperResultSubmissionDrawDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRetailerListBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperSearchInventoryRequestDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperSearchInventoryResponseDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperToBeCollectedBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperUserDetailsBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftReq;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftRes;
import com.skilrock.lms.api.lmsWrapper.common.InventoryHelper;
import com.skilrock.lms.api.lmsWrapper.common.LmsWrapperDrawGameMgmtHelper;
import com.skilrock.lms.api.lmsWrapper.common.LmsWrapperUserMgmtHelper;
import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.api.lmsWrapper.reportsMgmt.LmsWrapperCashierDrawerDataAction;
import com.skilrock.lms.api.lmsWrapper.reportsMgmt.LmsWrapperSearchInventoryReportAction;
import com.skilrock.lms.beans.CashierDrawerDataForPWTBean;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentBankDepositHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentPaymentSubmitHelper;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ToBeCollectedReportForLagosHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationManagementHelper;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class LmsWrapperServiceImpl implements ILmsWrapperService,  ServletRequestAware,
ServletResponseAware {
	static Log logger = LogFactory.getLog(LmsWrapperServiceImpl.class);
	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public LmsWrapperCashPaymentBean depositCashPayment(LmsWrapperCashPaymentBean cashPaymentBean) {
		System.out.println("CASH PAYMENT REQUEST DATA:: userName="+cashPaymentBean.getSystemUserName()+"|AgentOrgId="+cashPaymentBean.getAgentOrgId()+"" 
				+" |AgentName="+cashPaymentBean.getAgentName()+"|cashPayment="+cashPaymentBean.getCashAmount() );
		
		String ip = XFireServletController.getRequest().getRemoteHost();
		Connection con = null;
		if(!WrapperUtility.validateUser(ip,cashPaymentBean.getSystemUserName(),cashPaymentBean.getSystemPassword())){
			cashPaymentBean.setErrorCode("101");
			cashPaymentBean.setSuccess(false);
			return cashPaymentBean;
		}
		
		HttpServletRequest request = XFireServletController.getRequest();
          request.setAttribute("code", "MGMT");
          request.setAttribute("interfaceType", "API");
          ServletActionContext.setRequest(request);
		AgentPaymentSubmitHelper helper=new AgentPaymentSubmitHelper();
		     String autoGeneRecieptNoAndId;
		try {
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			LmsWrapperServiceApiHelper wrapperHelper=new LmsWrapperServiceApiHelper();
			//String userData=wrapperHelper.getWrapperUserData();
			UserInfoBean userBean = wrapperHelper.getUserDataFromUserId(cashPaymentBean.getUserId());
			//String[] userDataArr=userData.split("-");
			
			int orgId=WrapperUtility.getAgentIdFromAgentName(cashPaymentBean.getAgentName());
			autoGeneRecieptNoAndId = helper.submitCashAgentAmt(orgId,"AGENT",cashPaymentBean.getCashAmount(), userBean.getUserId(),userBean.getUserOrgId(),userBean.getUserType(),null,null,null,null,con);
			con.commit();
			String[] autoGeneReceipt=autoGeneRecieptNoAndId.split("#");
			String autoGeneRecieptNo=autoGeneReceipt[0];
			int id=Integer.parseInt(autoGeneReceipt[1]);
			cashPaymentBean.setRecieptNo(autoGeneRecieptNo);
            cashPaymentBean.setSuccess(true);
            cashPaymentBean.setErrorCode("100");
		} catch (LMSException e) {
			
			e.printStackTrace();
			 cashPaymentBean.setSuccess(false);
	            cashPaymentBean.setErrorCode("500");
		}catch (Exception e) {
			logger.error("Exception",e);
			logger.info("RESPONSE_CASH_PAYMENT_SUBMIT-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		System.out.println("CASH PAYMENT RESPONSE DATA:: userName="+cashPaymentBean.getSystemUserName()+"|AgentOrgId="+cashPaymentBean.getAgentOrgId()+"|AgentName="+cashPaymentBean.getAgentName()+"|cashPayment="+cashPaymentBean.getCashAmount()+"|getRecieptNo="+cashPaymentBean.getRecieptNo()+"|isSuccess="+cashPaymentBean.isSuccess()+"Errorcode="+cashPaymentBean.getErrorCode() );

		return cashPaymentBean; 
	}
	
	public LmsWrapperAgentListBean getAgentList(String systemUserName,String systemPassword){
		LmsWrapperAgentListBean agentListBean=new LmsWrapperAgentListBean();
		String[] params = systemUserName.split("_");
		systemUserName = params[0];
		String listType = "ALL";
		if(params.length > 1) {
			listType = params[1];
		}

		System.out.println("Get Agent List Request::userName="+systemUserName);
		String ip = XFireServletController.getRequest().getRemoteHost();
		System.out.println("ip::"+ip);
		if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
			agentListBean.setErrorCode("101");
			agentListBean.setSuccess(false);
			return agentListBean;
		}
		
		
		if(WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
		List<AgentInfoBean> agentInfoList=new ArrayList<AgentInfoBean>();
	try{
		LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
		agentInfoList=helper.getAgentList(listType);
		agentListBean.setAgentInfoBean(agentInfoList);
		agentListBean.setSuccess(true);
		agentListBean.setErrorCode("100");
	}
	catch (Exception e) {
		agentListBean.setSuccess(false);
		agentListBean.setErrorCode("500");
	}
		}else{
			
		}
	System.out.println("Agent List Response Data::Issuccess="+agentListBean.isSuccess()+"|ErrorCode="+agentListBean.getErrorCode());
		return agentListBean;
	}
	
      public LmsWrapperPwtApiBean verifyDirectPlrTicketNo(String systemUserName,String systemPassword,String ticketNo,int userId){
    	 
    	  System.out.println("REQUEST Verification Direct Player Ticket NO Request data:: TicketNo="+ticketNo+"|userName="+systemUserName);
    	  String ip = XFireServletController.getRequest().getRemoteHost();
    	  LmsWrapperPwtApiBean pwtApiBean=new LmsWrapperPwtApiBean();
    	  if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
    		  pwtApiBean.setErrorCode("101");
    		  pwtApiBean.setSuccess(false);
  			return pwtApiBean;
  		}
  		
    	  
    	  HttpServletRequest request = XFireServletController.getRequest();
          request.setAttribute("code", "MGMT");
          request.setAttribute("interfaceType", "API");
          ServletActionContext.setRequest(request);
    	 
    		LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
		UserInfoBean userInfoBean = helper.getUserDataFromUserId(userId);
		if("FAILED".equalsIgnoreCase(userInfoBean.getStatus())){
			pwtApiBean.setSuccess(false);
			pwtApiBean.setErrorCode("102");
			return pwtApiBean;
		}
		try {
			pwtApiBean =helper.verifyDirectPlrTicketNo(userInfoBean,ticketNo);
			if("ERROR_INVALID".equalsIgnoreCase(pwtApiBean.getStatus())){
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("110");
				return pwtApiBean;
			}
			pwtApiBean.setSuccess(true);
			pwtApiBean.setErrorCode("100");
		} catch (LMSException e) {
			
			e.printStackTrace();
			pwtApiBean.setSuccess(false);
			pwtApiBean.setErrorCode("500");
		}
		System.out.println("Verification TicketNo Response data:: TotalPwtAmount="+pwtApiBean.getTotalTicketPwtAmount()+"|message="+pwtApiBean.getMessage()
				                                                                  +"|isSuccess="+pwtApiBean.isSuccess()+"|Errorcode="+pwtApiBean.getErrorCode()); 
    	  return pwtApiBean;
      }
      
      public String authenticateRetailer(String systemUserName,String systemPassword,String retUserName,String terminalId) {
    	  String ip = XFireServletController.getRequest().getRemoteHost();
    	  if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
    		  // set code 101 for invalid username or password
  			return "101";
  		}
    	  LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
		
		String isValid="false";
		try {
			isValid = helper.checkRetailerLoginPlace(retUserName,terminalId);
		} catch (LMSException e) {
			System.out.println("INSIDE LMSEXCEPTION......");
			e.printStackTrace();
		}
		System.out.println("RESPONSE fetchLmsLocationForRetailer......"+isValid);
     	return isValid;
		}
      
      
    public LmsWrapperOrganizationRegShiftDataBean getOrgRegistrationShiftDataList(String systemUserName,String systemPassword,String agentUserId){
    	System.out.println("REQUEST AGENT ID FOR SHIFT DATA::"+agentUserId);  
    	  LmsWrapperOrganizationRegShiftDataBean orgRegistrationShiftDataBean=new LmsWrapperOrganizationRegShiftDataBean();
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	 if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
    		 orgRegistrationShiftDataBean.setErrorCode("101");
    		 orgRegistrationShiftDataBean.setSuccess(false);
 			return orgRegistrationShiftDataBean;
 		}
    	LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
    	   String agentName=WrapperUtility.getAgentNameFromAgentUserId(agentUserId);
    	    //orgRegistrationShiftDataBean=helper.getOrgRegShiftData(agentName);
    	   orgRegistrationShiftDataBean=helper.getOrgRegShiftData(agentName , agentUserId);
			System.out.println("RESPONSE::"+orgRegistrationShiftDataBean);
		
        return orgRegistrationShiftDataBean;
       }
      
    public String agentRetailerRegistrationAction(LmsWrapperOrgRegShiftBean orgRegShiftBean){
    	
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	if(!WrapperUtility.validateUser(ip,orgRegShiftBean.getSystemUserName(),orgRegShiftBean.getSystemPassword())){
  		  // set code 101 for invalid username or password
			return "101";
		}
    	String status="FAILED";
    	 LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
    	 status=helper.agentRetailerRegistrationAction(orgRegShiftBean);
    	 if("FAILED".equalsIgnoreCase(status)){
    		 return status;
    	 }
    	 status="SUCCESS";
    	return status;
    }
    
    public String removeStatusOfAgentRetailer(String agentUserId,String systemUserName,String systemPassword){
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	 if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
   		  // set code 101 for invalid username or password
 			return "101";
 		}
    	
    	String status="FAILED";
    	String agentOrgId=WrapperUtility.getAgentOrgIdFromAgentUserId(agentUserId);
    	 LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
    	 status=helper.removeStatusOfAgentRetailer(agentOrgId, ip);
    	return status;
    }
    
    public LmsWrapperRetailerListBean getRetailerList(String systemUserName,String systemPassword,String agentUserId){
    	LmsWrapperRetailerListBean retailerListBean =new LmsWrapperRetailerListBean();
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	
    	 if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
    		 retailerListBean.setErrorCode("101");
    		 retailerListBean.setSuccess(false);
 			return retailerListBean;
 		}
    	     	
    	LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
    	retailerListBean=helper.getRetailerListBean(agentUserId);
    	return retailerListBean;
    }
    public LmsWrapperDrawscheduleBeanResult fetchDrawListForResSubmission(LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean){
    	
    	LmsWrapperDrawscheduleBeanResult drawScheduleBeanResult=new LmsWrapperDrawscheduleBeanResult();
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	 if(!WrapperUtility.validateUser(ip,resultSubmissionDrawDataBean.getSystemUserName(),resultSubmissionDrawDataBean.getSystemPassword())){
    		 drawScheduleBeanResult.setErrorCode("101");
    		 drawScheduleBeanResult.setSuccess(false);
 			return drawScheduleBeanResult;
 		}
    	
		logger.debug("INside getManualEntryData");
		logger.debug("action call------------------");
		logger.debug("Before--" + new Date());
        LmsWrapperDrawGameMgmtHelper helper=new LmsWrapperDrawGameMgmtHelper();
	
        drawScheduleBeanResult=helper.fetchDrawListForResSubmission(resultSubmissionDrawDataBean);
		
		return drawScheduleBeanResult;
	}	

   public String performManualWinningEntry(LmsWrapperPerformDrawDataBean performDrawDataBean) {
	  
	   String ip = XFireServletController.getRequest().getRemoteHost();
	   if(!WrapperUtility.validateUser(ip,performDrawDataBean.getSystemUserName(),performDrawDataBean.getSystemPassword())){
 		  // set code 101 for invalid username or password
			return "101";
		}
	   LmsWrapperDrawGameMgmtHelper helper=new LmsWrapperDrawGameMgmtHelper();
	   String result="";
	   result=helper.performManualWinningEntry(performDrawDataBean);
	   if("FAILED".equalsIgnoreCase(result)){
		   return "102";
	   }
		return result;
	}

	
	public LmsWrapperOnStartGameDataBean getGameData(String systemUserName,String systemPassword) {
		String ip = XFireServletController.getRequest().getRemoteHost();
		LmsWrapperOnStartGameDataBean returnGameDataBean=new LmsWrapperOnStartGameDataBean();
		
		if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
			returnGameDataBean.setErrorCode("101");
			returnGameDataBean.setSuccess(false);
			return returnGameDataBean;
		}
		
		HashMap<Integer, GameMasterLMSBean> GameMap=(HashMap<Integer, GameMasterLMSBean>) Util.getGameMap();
		LmsWrapperGameDataBean gameDataBean=null;
		LinkedHashMap<String, LmsWrapperGameDataBean> localGameMap=new LinkedHashMap<String, LmsWrapperGameDataBean>();
		
		for (Map.Entry<Integer, GameMasterLMSBean> entry : GameMap.entrySet())
		{
			gameDataBean=new LmsWrapperGameDataBean();
		
			Integer game=entry.getKey();
			GameMasterLMSBean bean=(GameMasterLMSBean)entry.getValue();
			gameDataBean.setGameId(bean.getGameId());
			String gameName=bean.getGameName();
			gameDataBean.setGameName(gameName);
			gameDataBean.setGameDevName(bean.getGameNameDev());
			String gameN=new Integer(bean.getGameNo()).toString();
			gameDataBean.setGameNo(gameN);
			gameDataBean.setGameStatus(bean.getGameStatus());
			localGameMap.put(gameName, gameDataBean);
		}
		returnGameDataBean.setReturnGameDataBean(localGameMap);
		return returnGameDataBean;
	}
	
	public LmsWrapperInventoryMenuDataBean consNonConsAsignInvMenuData(String systemUserName,String systemPassword){
		LmsWrapperInventoryMenuDataBean invMenuDataBean=new LmsWrapperInventoryMenuDataBean();
		
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,systemUserName,systemPassword)){
			invMenuDataBean.setErrorcode("101");
			invMenuDataBean.setSuccess(false);
			return invMenuDataBean;
		}
		
		ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		try {
			Map<String, HashMap<String, String>> map = helper.fetchAgentNRetList();
			invMenuDataBean.setNonConsInvMap(map.get("nonConsInvMap"));
			invMenuDataBean.setConsInvMap(map.get("consInvMap"));
			invMenuDataBean.setBrandMap( map.get("brandMap"));
			invMenuDataBean.setAgentMap( map.get("agentMap"));
			invMenuDataBean.setRetMap(map.get("retMap"));
			invMenuDataBean.setInvSpecMap(map.get("invSpecMap"));
			invMenuDataBean.setModelMap( map.get("modelMap"));
			
			InventoryHelper.setInventoryDataToMap();
			invMenuDataBean.setModelIdMap(InventoryHelper.modelIdMap);
			invMenuDataBean.setModelNameMap(InventoryHelper.modelNameMap);
			invMenuDataBean.setBrandIdMap(InventoryHelper.brandIdMap);
			invMenuDataBean.setBrandNameMap(InventoryHelper.brandNameMap);
			invMenuDataBean.setConsInventoryIdMap(InventoryHelper.consInventoryIdMap);
			invMenuDataBean.setConsInventoryNameMap(InventoryHelper.consInventoryNameMap);
			invMenuDataBean.setConsModelIdMap(InventoryHelper.consModelIdMap);
			invMenuDataBean.setInventoryIdMap(InventoryHelper.inventoryIdMap);
			invMenuDataBean.setInventoryNameMap(InventoryHelper.inventoryNameMap);
			invMenuDataBean.setConsInvIdFromModelMap(InventoryHelper.consInvIdFromModelMap);
			invMenuDataBean.setStatus("SUCCESS");
			invMenuDataBean.setErrorcode("100");
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			invMenuDataBean.setStatus("FAILED");
			invMenuDataBean.setErrorcode("500");
		}
		return invMenuDataBean;
	}
	
	public String verifyConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean){
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,invAssignDataBean.getSystemUserName(),invAssignDataBean.getSystemPassword())){
	 		  // set code 101 for invalid username or password
				return "101";
			}
		
		InventoryHelper.setInventoryDataToMap();
		
		 String[] nonConsInvId=invAssignDataBean.getNonConsInvId();
		 String[] nonConsModelId=invAssignDataBean.getNonConsModelId();
		 String[] nonConsBrandId=invAssignDataBean.getNonConsBrandId();
		 String[] consInvId=invAssignDataBean.getConsInvId();
	     String[] consModelId=invAssignDataBean.getConsModelId(); 
	   
		int[] nonConsumableInvId=new int[nonConsInvId.length];
		for(int i=0;i<nonConsInvId.length;i++){
			nonConsumableInvId[i]=Integer.parseInt(InventoryHelper.inventoryNameMap.get(nonConsInvId[i]));
		}
		
		int[] nonConsumableModelId=new int[nonConsModelId.length];
		for(int i=0;i<nonConsModelId.length;i++){
			nonConsumableModelId[i]=Integer.parseInt(InventoryHelper.modelNameMap.get(nonConsModelId[i]));
		}
		
		int[] nonConsumableBrandId=new int[nonConsBrandId.length];
		for(int i=0;i<nonConsBrandId.length;i++){
			nonConsumableBrandId[i]=Integer.parseInt(InventoryHelper.brandNameMap.get(nonConsBrandId[i]));
		}
		
		int[] consumableInvId=new int[consInvId.length];
		for(int i=0;i<consInvId.length;i++){
			consumableInvId[i]=Integer.parseInt(InventoryHelper.consInventoryNameMap.get(consInvId[i]));
		}
		
		int[] consumableModelId=new int[consInvId.length];
		for(int i=0;i<consInvId.length;i++){			
			String consInv=InventoryHelper.consInventoryNameMap.get(consInvId[i]);
			consumableModelId[i]=Integer.parseInt(InventoryHelper.consModelIdMap.get(consInv));
		}
		
		LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
		UserInfoBean userInfo = helper.getUserData();
		if("FAILED".equalsIgnoreCase(userInfo.getStatus())){
			return "102";
		}
		
		ConsNNonConsInvHelper invHelper = new ConsNNonConsInvHelper();
		 
		
		
		String verifyConsNNonConsInv="";
		try {
			if(!"ASSIGN_INVENTORY".equals(invAssignDataBean.getStatus())){
				int agtOrgId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(invAssignDataBean.getAgtOrgId()+""));
				verifyConsNNonConsInv = invHelper.verifyConsNNonConsInv(agtOrgId
						, nonConsumableInvId,nonConsumableModelId, nonConsumableBrandId,
						invAssignDataBean.getSerNo(), consumableInvId, consumableModelId, invAssignDataBean.getConsQty(), userInfo.getUserType());
		
			}else{
			verifyConsNNonConsInv = invHelper.verifyConsNNonConsInv(userInfo
						.getUserOrgId(), nonConsumableInvId,nonConsumableModelId, nonConsumableBrandId,
						invAssignDataBean.getSerNo(), consumableInvId, consumableModelId, invAssignDataBean.getConsQty(), userInfo.getUserType(),invAssignDataBean.getAgtOrgId());
			}
			} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Return Verify Inventory::"+verifyConsNNonConsInv);
		return verifyConsNNonConsInv;
	}
	
	public String assignConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean){
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,invAssignDataBean.getSystemUserName(),invAssignDataBean.getSystemPassword())){
	 		  // set code 101 for invalid username or password
				return "101";
			}
		String status="";
         LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
         UserInfoBean userInfo = helper.getUserData();
         
         if("FAILED".equalsIgnoreCase(userInfo.getStatus())){
 			return "102";
 		}
         ConsNNonConsInvHelper invHelper = new ConsNNonConsInvHelper();
         try {
        	 InventoryHelper.setInventoryDataToMap();
     		
    		 String[] nonConsInvId=invAssignDataBean.getNonConsInvId();
    		 String[] nonConsModelId=invAssignDataBean.getNonConsModelId();
    		 String[] nonConsBrandId=invAssignDataBean.getNonConsBrandId();
    		 String[] consInvId=invAssignDataBean.getConsInvId();
    	     String[] consModelId=invAssignDataBean.getConsModelId();
    	   
    		int[] nonConsumableInvId=new int[nonConsInvId.length];
    		if(nonConsInvId[0] != null){
    		for(int i=0;i<nonConsInvId.length;i++){
    			nonConsumableInvId[i]=Integer.parseInt(InventoryHelper.inventoryNameMap.get(nonConsInvId[i]));
    		}
    		}
    		
    		
    		int[] nonConsumableModelId=new int[nonConsModelId.length];
    		if(nonConsModelId[0] != null){
    		for(int i=0;i<nonConsModelId.length;i++){
    			nonConsumableModelId[i]=Integer.parseInt(InventoryHelper.modelNameMap.get(nonConsModelId[i]));
    		}
    		}
    		
    		int[] nonConsumableBrandId=new int[nonConsBrandId.length];
    		if(nonConsBrandId[0] != null){
    		for(int i=0;i<nonConsBrandId.length;i++){
    			nonConsumableBrandId[i]=Integer.parseInt(InventoryHelper.brandNameMap.get(nonConsBrandId[i]));
    		}
    		}
    		
    		int[] consumableInvId=new int[consInvId.length];
    		if(consInvId[0] != null){
    		for(int i=0;i<consInvId.length;i++){
    			consumableInvId[i]=Integer.parseInt(InventoryHelper.consInventoryNameMap.get(consInvId[i]));
    		}
    		}
    		
    		int[] consumableModelId=new int[consInvId.length];
    		if(consInvId[0] != null){
    		for(int i=0;i<consInvId.length;i++){
    			String consInv=InventoryHelper.consInventoryNameMap.get(consInvId[i]);
    			consumableModelId[i]=Integer.parseInt(InventoryHelper.consModelIdMap.get(consInv));
    		}
    		}
    		
    		if(nonConsModelId[0] != null){
    		for(int i=0;i<nonConsModelId.length;i++){
    			
    		
    		//check duplicate terminal id
			List<String> dupSerialNoList=helper.checkDuplicateTerminal(invAssignDataBean.getSerNo(),InventoryHelper.modelNameMap.get(nonConsModelId[i]));
			
			//change status from REMOVED TO BO
			if(dupSerialNoList.size()>0){
			status=WrapperUtility.changeStatusFromRemovedToBo(dupSerialNoList,InventoryHelper.modelNameMap.get(nonConsModelId[i]));
			if("FAILED".equalsIgnoreCase(status)){
				return status;
			}
			}
			
			String[] serialNo=invAssignDataBean.getSerNo();
			List<String> serialNoList=new LinkedList<String>(Arrays.asList(serialNo[i].split(",")));
			
			if(dupSerialNoList.size()>0){
				for(int j=0;j<dupSerialNoList.size();j++){
					serialNoList.remove(dupSerialNoList.get(j));
					//dupSerialNoList.remove(serialNoList.get(j));
				}
				
			}
				
				if(serialNoList.size()>0 && serialNoList !=null){
				String[] serialNoUpload=serialNoList.toArray(new String[serialNoList.size()]);
				String[] invCodeUpload=new String[serialNoList.size()];// Set to Null...  Fetch Inv code from serial numbers  
		/*	ArrayList arr = invHelper.nonConsInvUpload(InventoryHelper.modelNameMap.get(nonConsModelId[i])+"-"+InventoryHelper.brandNameMap.get(nonConsBrandId[i]), 1000.00, "Y",
					null, serialNoUpload, userInfo.getUserId(), userInfo
							.getUserOrgId(), userInfo.getUserType());*/
			
	
			ArrayList arr1 = invHelper.nonConsInvUpload(serialNoUpload, invCodeUpload, null, Integer.parseInt(InventoryHelper.modelNameMap.get(nonConsModelId[i])), 1000.00, "Y", null, userInfo.getUserId(), userInfo
					.getUserOrgId(), userInfo.getUserType());
			
			
			
    		}
    		}
    		}
    		
        	 int agtOrgId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(""+invAssignDataBean.getAgtOrgId()));
			int DNID = invHelper.assignConsNNonConsInv(
						userInfo.getUserOrgId(), userInfo.getUserId(), "AGENT",
						agtOrgId,invAssignDataBean.getRetOrgId(), nonConsumableInvId, nonConsumableModelId,
						nonConsumableBrandId, invAssignDataBean.getSerNo(), consumableInvId, consumableModelId,
						invAssignDataBean.getConsQty(),	userInfo.getUserType());
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	
	public String returnConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean){
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,invAssignDataBean.getSystemUserName(),invAssignDataBean.getSystemPassword())){
	 		  // set code 101 for invalid username or password
				return "101";
			}
		String status="";
         LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
         UserInfoBean userInfo = helper.getUserData();
         
         if("FAILED".equalsIgnoreCase(userInfo.getStatus())){
 			return "102";
 		}
         ConsNNonConsInvHelper invHelper = new ConsNNonConsInvHelper();
         try {
        	 InventoryHelper.setInventoryDataToMap();
     		
    		 String[] nonConsInvId=invAssignDataBean.getNonConsInvId();
    		 String[] nonConsModelId=invAssignDataBean.getNonConsModelId();
    		 String[] nonConsBrandId=invAssignDataBean.getNonConsBrandId();
    		 String[] consInvId=invAssignDataBean.getConsInvId();
    	     String[] consModelId=invAssignDataBean.getConsModelId();
    	   
    		int[] nonConsumableInvId=new int[nonConsInvId.length];
    		if(nonConsInvId[0] != null){
    		for(int i=0;i<nonConsInvId.length;i++){
    			nonConsumableInvId[i]=Integer.parseInt(InventoryHelper.inventoryNameMap.get(nonConsInvId[i]));
    		}
    		}
    		int[] nonConsumableModelId=new int[nonConsModelId.length];
    		if(nonConsModelId[0] != null){
    		for(int i=0;i<nonConsModelId.length;i++){
    			nonConsumableModelId[i]=Integer.parseInt(InventoryHelper.modelNameMap.get(nonConsModelId[i]));
    		}
    		}
    		
    		int[] nonConsumableBrandId=new int[nonConsBrandId.length];
    		if(nonConsBrandId[0] != null){
    		for(int i=0;i<nonConsBrandId.length;i++){
    			nonConsumableBrandId[i]=Integer.parseInt(InventoryHelper.brandNameMap.get(nonConsBrandId[i]));
    		}
    		}
    		
    		int[] consumableInvId=new int[consInvId.length];
    		for(int i=0;i<consInvId.length;i++){
    			consumableInvId[i]=Integer.parseInt(InventoryHelper.consInventoryNameMap.get(consInvId[i]));
    		}
    		
    		int[] consumableModelId=new int[consModelId.length];
    		for(int i=0;i<consModelId.length;i++){
    			consumableModelId[i]=Integer.parseInt(InventoryHelper.consModelIdMap.get(consModelId[i]));
    		}
    		String[] serialNo=new String[nonConsModelId.length];
    		
    		for(int i=0;i<nonConsModelId.length;i++){
    		
			 serialNo=invAssignDataBean.getSerNo();
			
			
    		}
    	
			
        	 int agtOrgId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(""+invAssignDataBean.getAgtOrgId()));
        	 
				int DNID = invHelper.returnConsNNonConsInv(
						userInfo.getUserOrgId(), userInfo.getUserId(), "AGENT",
						agtOrgId, -1, nonConsumableInvId, nonConsumableModelId,
						nonConsumableBrandId, serialNo, consumableInvId, consumableModelId, invAssignDataBean.getConsQty(),
						userInfo.getUserType());
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	public String removeConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean){
		String ip = XFireServletController.getRequest().getRemoteHost();
		
		if(!WrapperUtility.validateUser(ip,invAssignDataBean.getSystemUserName(),invAssignDataBean.getSystemPassword())){
	 		  // set code 101 for invalid username or password
				return "101";
			}
		String status="FAILED";
		LmsWrapperServiceApiHelper helper=new LmsWrapperServiceApiHelper();
		status=helper.removeConsNNonConsInv( invAssignDataBean);
		return status;
		
	}
	
	public LmsWrapperBankDepositBean agentBankDeposit(LmsWrapperBankDepositBean bankDepositBean){
		System.out.println("Bank Deposit REQUEST DATA:: userName="+bankDepositBean.getSystemUserName()+"|AgentOrgId="+bankDepositBean.getAgentOrgId()+"" 
				+" |AgentName="+bankDepositBean.getAgentName()+"|cashPayment="+bankDepositBean.getDepositAmount() );
		
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,bankDepositBean.getSystemUserName(),bankDepositBean.getSystemPassword())){
			bankDepositBean.setErrorCode("101");
			bankDepositBean.setSuccess(false);
			return bankDepositBean;
		}
		
		HttpServletRequest request = XFireServletController.getRequest();
          request.setAttribute("code", "MGMT");
          request.setAttribute("interfaceType", "API");
          ServletActionContext.setRequest(request);
          AgentBankDepositHelper helper=new AgentBankDepositHelper();
	     String autoGeneRecieptNoAndId;
	try {
		LmsWrapperServiceApiHelper wrapperHelper=new LmsWrapperServiceApiHelper();
		UserInfoBean userBean = wrapperHelper.getUserDataFromUserId(bankDepositBean.getUserId());
		
		int agentUserId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(bankDepositBean.getAgentOrgId()+""));
		
		autoGeneRecieptNoAndId = helper.submitBankDepositAmt(agentUserId,"AGENT", bankDepositBean.getDepositAmount(),bankDepositBean.getBankRecieptNo(),bankDepositBean.getBankName(),bankDepositBean.getBranchName(),bankDepositBean.getDepositDate(),userBean );
		
		String[] autoGeneReceipt=autoGeneRecieptNoAndId.split("-");
		String autoGeneRecieptNo=autoGeneRecieptNoAndId.split("Nxt")[1];
		int id=Integer.parseInt(autoGeneRecieptNoAndId.split("Nxt")[0]);
		bankDepositBean.setRecieptNo(autoGeneRecieptNo);
		bankDepositBean.setSuccess(true);
		bankDepositBean.setErrorCode("100");
	} catch (LMSException e) {
		
		e.printStackTrace();
		bankDepositBean.setSuccess(false);
		bankDepositBean.setErrorCode("500");
	}
	System.out.println("Bank Deposit RESPONSE DATA:: userName="+bankDepositBean.getSystemUserName()+"|AgentOrgId="+bankDepositBean.getAgentOrgId()+"|AgentName="+bankDepositBean.getAgentName()+"|DepositAmount="+bankDepositBean.getDepositAmount()+"|getRecieptNo="+bankDepositBean.getRecieptNo()+"|isSuccess="+bankDepositBean.isSuccess()+"Errorcode="+bankDepositBean.getErrorCode() );
		return bankDepositBean;		
	}
	
	public String RegisterNewSubUser(LmsWrapperUserDetailsBean wrapperUserDetailsBean){
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,wrapperUserDetailsBean.getSystemUserName(),wrapperUserDetailsBean.getSystemPassword())){
			wrapperUserDetailsBean.setErrorCode("101");
			wrapperUserDetailsBean.setSuccess(false);
			return "101";
		}
		LmsWrapperUserMgmtHelper helper=new LmsWrapperUserMgmtHelper();
		
		return helper.RegisterNewSubUser(wrapperUserDetailsBean);
		
	}

	
	public HashMap<String,LmsWrapperCashierDrawerDataReportBean> fetchCashierWiseDrawerData(
			LmsWrapperCashierDrawerDataReportBean cashierDrawerDataBean)
			 {
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,cashierDrawerDataBean.getSystemUserName(),cashierDrawerDataBean.getSystemPassword())){
			cashierDrawerDataBean.setErrorCode("101");
			cashierDrawerDataBean.setSuccess(false);
		      return null;
		}
		LmsWrapperCashierDrawerDataAction drawerAction=new LmsWrapperCashierDrawerDataAction();
		HashMap<String,LmsWrapperCashierDrawerDataReportBean> cahierDataMap=new HashMap<String, LmsWrapperCashierDrawerDataReportBean>();
		try {
		cahierDataMap=drawerAction.fetchCashierWiseDrawerData(cashierDrawerDataBean);
		
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cahierDataMap;
	}

	public String collectionAgentWiseWithOpeningBal(
			String orgName)
			 {
		String outstandingBal = "" ;
		HttpServletRequest request = XFireServletController.getRequest();
		HttpSession session = request.getSession() ;
		OrganizationManagementHelper agtHelper = new OrganizationManagementHelper() ;
		try {
			outstandingBal = FormatNumber.formatNumberForJSP(agtHelper.getAgentOutstandingBal(Util.fetchOrgIdOfUser(Integer.parseInt(orgName)), request, session)) ;
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return outstandingBal;
	}

	
	public LmsWrapperSearchInventoryResponseDataBean consNonConsSearchInvDetail(
			LmsWrapperSearchInventoryRequestDataBean searchInventoryReqBean) {
		LmsWrapperSearchInventoryReportAction searchInvAction=new LmsWrapperSearchInventoryReportAction();
		LmsWrapperSearchInventoryResponseDataBean responseDataBean =new LmsWrapperSearchInventoryResponseDataBean();
		try {
			responseDataBean = searchInvAction.consNonConsSearchInvDetail(searchInventoryReqBean);
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseDataBean;
	}

	public ArrayList<LmsWrapperCashierDrawerDataForPWTBean> fetchDataForExportToExcelOfPWT(
			LmsWrapperCashierDrawerDataForPWTBean lmsWrapperCashierDrawerDataForPWTBean) {

		CashChqReportsHelper helper = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		LmsWrapperCashierDrawerDataForPWTBean pwtDataBean=null;
		List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWiseList=null;
		ArrayList<LmsWrapperCashierDrawerDataForPWTBean> lmsWrapperCashierDrawerDataForPWTBeanList=null;
		
		String ip = XFireServletController.getRequest().getRemoteHost();
		if (!WrapperUtility.validateUser(ip,
				lmsWrapperCashierDrawerDataForPWTBean.getSystemUserName(),
				lmsWrapperCashierDrawerDataForPWTBean.getSystemPassword())) {
				lmsWrapperCashierDrawerDataForPWTBean.setErrorCode("101");
				lmsWrapperCashierDrawerDataForPWTBean.setSuccess(false);
			return null;
		}

		try {
			DateBeans dateBeans=new DateBeans();
			dateBeans.setStartDate(new java.util.Date(sdf.parse(lmsWrapperCashierDrawerDataForPWTBean.getStartDate()).getTime()));
			dateBeans.setEndDate(new java.util.Date(sdf.parse(lmsWrapperCashierDrawerDataForPWTBean.getEndDate()).getTime()));
			helper = new CashChqReportsHelper(dateBeans);
			
			if("SELF".equals(lmsWrapperCashierDrawerDataForPWTBean.getCashierType()))
					lmsWrapperCashierDrawerDataForPWTBean.setUserId(WrapperUtility.getUserIdFromUserName(lmsWrapperCashierDrawerDataForPWTBean.getCashierName()));
			
			detailsForPwtTicketsCashierWiseList = helper
					.getDetailsForPwtTicketsCashierWise(dateBeans,
							lmsWrapperCashierDrawerDataForPWTBean
									.getCashierType(),
							lmsWrapperCashierDrawerDataForPWTBean.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			lmsWrapperCashierDrawerDataForPWTBean.setErrorCode("101");
			lmsWrapperCashierDrawerDataForPWTBean.setSuccess(false);
			return null;
		}
		
		
		Iterator<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWiseiterator =detailsForPwtTicketsCashierWiseList.iterator();
		lmsWrapperCashierDrawerDataForPWTBeanList=new ArrayList<LmsWrapperCashierDrawerDataForPWTBean>();
		while (detailsForPwtTicketsCashierWiseiterator.hasNext()) {
			pwtDataBean=new LmsWrapperCashierDrawerDataForPWTBean();
			CashierDrawerDataForPWTBean cashierDrawerDataForPWTBean = (CashierDrawerDataForPWTBean) detailsForPwtTicketsCashierWiseiterator
					.next();
			pwtDataBean.setCashierName(cashierDrawerDataForPWTBean.getCashierName());
			pwtDataBean.setUserId(cashierDrawerDataForPWTBean.getUserId());
			pwtDataBean.setTicketNumber(cashierDrawerDataForPWTBean.getTicketNumber());
			pwtDataBean.setGameName(cashierDrawerDataForPWTBean.getGameName());
			pwtDataBean.setClaimedTime(cashierDrawerDataForPWTBean.getClaimedTime().toString());
			pwtDataBean.setClaimedAmount(cashierDrawerDataForPWTBean.getClaimedAmount());
			lmsWrapperCashierDrawerDataForPWTBeanList.add(pwtDataBean);
		}

		return lmsWrapperCashierDrawerDataForPWTBeanList;
	}
	
	@Override
	public LmsWrapperDrawscheduleBeanResult fetchDrawListForResSubmissionForMacNumber(
			LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean) {

    	
    	LmsWrapperDrawscheduleBeanResult drawScheduleBeanResult=new LmsWrapperDrawscheduleBeanResult();
    	String ip = XFireServletController.getRequest().getRemoteHost();
    	 if(!WrapperUtility.validateUser(ip,resultSubmissionDrawDataBean.getSystemUserName(),resultSubmissionDrawDataBean.getSystemPassword())){
    		 drawScheduleBeanResult.setErrorCode("101");
    		 drawScheduleBeanResult.setSuccess(false);
 			return drawScheduleBeanResult;
 		}
    	
		logger.debug("INside getManualEntryData");
		logger.debug("action call------------------");
		logger.debug("Before--" + new Date());
        LmsWrapperDrawGameMgmtHelper helper=new LmsWrapperDrawGameMgmtHelper();
	
        drawScheduleBeanResult=helper.fetchDrawListForMachineResSubmission(resultSubmissionDrawDataBean);
		
		return drawScheduleBeanResult;
	
	}

	@Override
	public String performManualWinningMachineNumberEntry(
			LmsWrapperPerformDrawDataBean performDrawDataBean) {

		  
		   String ip = XFireServletController.getRequest().getRemoteHost();
		   if(!WrapperUtility.validateUser(ip,performDrawDataBean.getSystemUserName(),performDrawDataBean.getSystemPassword())){
	 		  // set code 101 for invalid username or password
				return "101";
			}
		   LmsWrapperDrawGameMgmtHelper helper=new LmsWrapperDrawGameMgmtHelper();
		   String result="";
		   result=helper.performManualWinningMachineNumberEntry(performDrawDataBean);
		   if("FAILED".equalsIgnoreCase(result)){
			   return "102";
		   }
			return result;
	}

	public LmsWrapperToBeCollectedBean fetchToBeCollectedDataForAgent(
			LmsWrapperCollectionReportOverAllBean lmsWrapperCollectionReportOverAllBean) {

		Timestamp strtDate = null;
		Timestamp deployDate = null;
		LmsWrapperToBeCollectedBean lmsWrapperToBeCollectedBean = null;
		LmsWrapperCollectionReportOverAllBean lmsWrapperCollectionReportBean = null;
		//HashMap<Integer, LmsWrapperCollectionReportOverAllBean> resultReportMap = null;
		HashMap<String, LmsWrapperCollectionReportOverAllBean> resultReportMap = null;
		HashMap<String, LmsWrapperCompleteCollectionBean> gameBeanMapForWrapper = null;
		try {
			if (lmsWrapperCollectionReportOverAllBean.getStartDate() != null) {
				strtDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd")
						.parse(
								lmsWrapperCollectionReportOverAllBean
										.getStartDate()).getTime());
				deployDate = new Timestamp(new SimpleDateFormat(
						lmsWrapperCollectionReportOverAllBean
								.getLmsDateFormat()).parse(
						lmsWrapperCollectionReportOverAllBean.getEndDate())
						.getTime());

				ToBeCollectedReportForLagosHelper helper = new ToBeCollectedReportForLagosHelper();
				/*Map<Integer, CollectionReportOverAllBean> resultMap = helper
						.fetchDataForAgent(deployDate, strtDate);*/
				
				Map<String, CollectionReportOverAllBean> resultMap = helper
				.fetchDataForAgent(deployDate, strtDate);

				//resultReportMap = new HashMap<Integer, LmsWrapperCollectionReportOverAllBean>();
				resultReportMap = new HashMap<String, LmsWrapperCollectionReportOverAllBean>();
				/*for (Map.Entry<Integer, CollectionReportOverAllBean> tempEntry : resultMap
						.entrySet()) {*/
				for (Map.Entry<String, CollectionReportOverAllBean> tempEntry : resultMap
						.entrySet()) {
					lmsWrapperCollectionReportBean = new LmsWrapperCollectionReportOverAllBean();
					CollectionReportOverAllBean tempBean = tempEntry.getValue();
					lmsWrapperCollectionReportBean.setOpeningBal(tempBean
							.getOpeningBal());
					lmsWrapperCollectionReportBean.setCash(tempBean.getCash());
					lmsWrapperCollectionReportBean.setCheque(tempBean
							.getCheque());
					lmsWrapperCollectionReportBean.setChequeReturn(tempBean
							.getChequeReturn());
					lmsWrapperCollectionReportBean.setCredit(tempBean
							.getCredit());
					lmsWrapperCollectionReportBean
							.setDebit(tempBean.getDebit());
					lmsWrapperCollectionReportBean.setBankDep(tempBean
							.getBankDep());
					lmsWrapperCollectionReportBean.setDgSale(tempBean
							.getDgSale());
					lmsWrapperCollectionReportBean
							.setDgPwt(tempBean.getDgPwt());
					lmsWrapperCollectionReportBean.setDgCancel(tempBean
							.getDgCancel());
					lmsWrapperCollectionReportBean.setDgDirPlyPwt(tempBean
							.getDgDirPlyPwt());
					lmsWrapperCollectionReportBean.setIwSale(tempBean.getIwSale());
					lmsWrapperCollectionReportBean.setIwPwt(tempBean.getIwPwt());
					lmsWrapperCollectionReportBean.setIwCancel(tempBean.getIwCancel());
					lmsWrapperCollectionReportBean.setIwDirPlyPwt(tempBean.getIwDirPlyPwt());
					lmsWrapperCollectionReportBean.setAgentName(tempBean
							.getAgentName());
					gameBeanMapForWrapper = new HashMap<String, LmsWrapperCompleteCollectionBean>();

					if (tempBean.getGameBeanMap() != null) {
						for (Entry<String, CompleteCollectionBean> innerTempEntry : tempBean
								.getGameBeanMap().entrySet()) {
							LmsWrapperCompleteCollectionBean lmsWrapperCompleteCollectionBean = new LmsWrapperCompleteCollectionBean();
							CompleteCollectionBean innerTempBean = innerTempEntry
									.getValue();
							lmsWrapperCompleteCollectionBean
									.setOrgName(innerTempBean.getOrgName());
							lmsWrapperCompleteCollectionBean
									.setDrawSale(innerTempBean.getDrawSale());
							lmsWrapperCompleteCollectionBean
							.setIwSale(innerTempBean.getIwSale());
							gameBeanMapForWrapper.put(innerTempEntry.getKey(),
									lmsWrapperCompleteCollectionBean);
						}
						/*lmsWrapperCollectionReportBean
								.setGameBeanMap(gameBeanMapForWrapper);
						resultReportMap.put(tempEntry.getKey(),
								lmsWrapperCollectionReportBean);*/
					}/* else {
						lmsWrapperCollectionReportBean.setGameBeanMap(gameBeanMapForWrapper);
				resultReportMap.put(tempEntry.getKey(),
						lmsWrapperCollectionReportBean);
						//resultReportMap.remove(tempEntry.getKey());
					}*/
					
					lmsWrapperCollectionReportBean.setGameBeanMap(gameBeanMapForWrapper);
					resultReportMap.put(tempEntry.getKey(),lmsWrapperCollectionReportBean);
					
				}
				lmsWrapperToBeCollectedBean = new LmsWrapperToBeCollectedBean();
				lmsWrapperToBeCollectedBean.setAgentMap(resultReportMap);
				lmsWrapperToBeCollectedBean.setGameNameMap(ReportUtility.allGameMap(new Timestamp(strtDate.getTime()-1000)));
			} else {
				logger.debug("DATE CAME NULL FORM THE OTHER SIDE .....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lmsWrapperToBeCollectedBean;
	}

	/*public List<LmsWrapperCashierDrawerDataReportBean> fetchCashierWiseDrawerData(LmsWrapperCashierDrawerDataReportBean cashierDrawerDataBean) throws LMSException{
		String ip = XFireServletController.getRequest().getRemoteHost();
		if(!WrapperUtility.validateUser(ip,cashierDrawerDataBean.getSystemUserName(),cashierDrawerDataBean.getSystemPassword())){
			cashierDrawerDataBean.setErrorCode("101");
			cashierDrawerDataBean.setSuccess(false);
			throw new LMSException("Invalid user Name");
		}
	LmsWrapperCashierDrawerDataAction drawerAction=new LmsWrapperCashierDrawerDataAction();
	
		return drawerAction.fetchCashierWiseDrawerData(cashierDrawerDataBean);
	}*/

	@Override
	public LmsWrapperAllRetInfoBean getAllRetailerUserId(String systemUserName , String systemUserPassword) {

		String ip = null;
		LmsWrapperAllRetInfoBean lmsWrapperAllRetInfoBean = null;
		try {
			logger.info("Request for getAllRetailerUserId() Initiated @ " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()))
			+ "With systemUserName and Password resp." + systemUserName	+ " " + systemUserPassword);
			
			ip = XFireServletController.getRequest().getRemoteHost();
			lmsWrapperAllRetInfoBean = new LmsWrapperAllRetInfoBean();
			  if(!WrapperUtility.validateUser(ip,systemUserName,systemUserPassword)) {
				  lmsWrapperAllRetInfoBean.setErrorCode(101);
				  lmsWrapperAllRetInfoBean.setErrorMessage("Invalid Authentication ");
				  throw new LMSException(); // TEMP
			  }else{
				  lmsWrapperAllRetInfoBean.setAllRetUserId(new LmsWrapperDrawGameMgmtHelper().getAllUserIds());
				  lmsWrapperAllRetInfoBean.setErrorCode(100);
			  }
		} catch (LMSException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
		return lmsWrapperAllRetInfoBean;
	}

	@Override
	public LmsWrapperRandomIdResponseBean updateInsertRandomIdInLMS(LmsWrapperRandomIdRequestBean lmsWrapperRandomIdRequestBean) {

		String ip = null;
		String requesInitiateTime = null;
		LmsWrapperRandomIdResponseBean lmsWrapperRandomIdResponseBean = null; 
		try {
			requesInitiateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
			logger.info("Request Initiated @ " + requesInitiateTime + "With systemUserName and Password resp." + lmsWrapperRandomIdRequestBean.getSystemUserName() 
					+ " " + lmsWrapperRandomIdRequestBean.getSystemUserPassword());
			ip = XFireServletController.getRequest().getRemoteHost();
			lmsWrapperRandomIdResponseBean = new LmsWrapperRandomIdResponseBean();
			  if(!WrapperUtility.validateUser(ip,lmsWrapperRandomIdRequestBean.getSystemUserName(),lmsWrapperRandomIdRequestBean.getSystemUserPassword())) {
				  lmsWrapperRandomIdResponseBean.setErrorCode(101);
				  lmsWrapperRandomIdResponseBean.setErrorMessage("Invalid Authentication ");
				  logger.error("Invalid Authentication ");
				  throw new LMSException(); // TEMP
			  }else{
				  new LmsWrapperDrawGameMgmtHelper().updateInsertRandomIdInLMS(lmsWrapperRandomIdRequestBean , lmsWrapperRandomIdResponseBean);
			  }
		} catch (LMSException e) {
			logger.error("EXCEPTION", e);
		} catch (Exception e) {
			logger.error("EXCEPTION", e);
		}
		return lmsWrapperRandomIdResponseBean;
	}

	@Override
	public LmsWrapperVerifyBeforeShiftRes verifyBeforeShift(LmsWrapperVerifyBeforeShiftReq lmsWrapperVerifyBeforeShiftReq) {

		String ip = null;
		int errorCode = 0;
		String requesInitiateTime = null;
		LmsWrapperVerifyBeforeShiftRes lmsWrapperVerifyBeforeShiftRes = null; 
		List<LmsWrapperVerifyBeforeShiftBean> orgList = null;
		try {
			requesInitiateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
			logger.info("Request Initiated to verifyBeforeShift() @ " + requesInitiateTime + "With systemUserName and Password resp." + lmsWrapperVerifyBeforeShiftReq.getSysUserName() 
					+ " " + lmsWrapperVerifyBeforeShiftReq.getSysUserPass());
			
			ip = XFireServletController.getRequest().getRemoteHost();
			lmsWrapperVerifyBeforeShiftRes = new LmsWrapperVerifyBeforeShiftRes();
			  if(!WrapperUtility.validateUser(ip,lmsWrapperVerifyBeforeShiftReq.getSysUserName(),lmsWrapperVerifyBeforeShiftReq.getSysUserPass())) {
				  lmsWrapperVerifyBeforeShiftRes.setErrorCode(101);
				  logger.error("Invalid Authentication ");
				  errorCode  = 101;
			  }else{
				  orgList = new LmsWrapperServiceApiHelper().verifyBeforeShift(lmsWrapperVerifyBeforeShiftReq);
			  }
		} catch (LMSException e) {
			errorCode = e.getErrorCode();
			logger.error("EXCEPTION", e);
		}catch (Exception e) {
			if(lmsWrapperVerifyBeforeShiftRes == null)
				lmsWrapperVerifyBeforeShiftRes = new LmsWrapperVerifyBeforeShiftRes();

			errorCode = LMSErrors.GENERAL_EXCEPTION_ERROR_CODE;
		}
		
		lmsWrapperVerifyBeforeShiftRes.setErrorCode(errorCode);
		lmsWrapperVerifyBeforeShiftRes.setOrgList(orgList);
		return lmsWrapperVerifyBeforeShiftRes;
	}

	

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response ;
		
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
		
	}
	
	
	
	
}