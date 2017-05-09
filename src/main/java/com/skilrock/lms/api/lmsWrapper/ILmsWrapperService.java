package com.skilrock.lms.api.lmsWrapper;


import java.util.ArrayList;
import java.util.HashMap;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperAgentListBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperAllRetInfoBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperBankDepositBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashPaymentBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataForPWTBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataReportBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCollectionReportOverAllBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperDrawscheduleBeanResult;
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
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftReq;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftRes;



//Generated by MyEclipse

public interface ILmsWrapperService {
	
	
	
	public LmsWrapperCashPaymentBean depositCashPayment(LmsWrapperCashPaymentBean cashPaymentBean);
	
	public LmsWrapperAgentListBean getAgentList(String systemUserName,String systemPassword);
	
	public LmsWrapperPwtApiBean verifyDirectPlrTicketNo(String systemUserName,String systemPassword,String ticketNo,int userId);
	//public LmsWrapperPlayerBean plrRegistrationAndApprovalReq(LmsWrapperPlayerBean lmsPlrBean);
		
	public String authenticateRetailer(String systemUserName,String systemPassword,String retUserName,String terminalId);	
	
	
	public LmsWrapperOrganizationRegShiftDataBean getOrgRegistrationShiftDataList(String systemUserName,String systemPassword,String agentUserId);
	
	public String agentRetailerRegistrationAction(LmsWrapperOrgRegShiftBean orgRegShiftBean);
	
	public LmsWrapperRetailerListBean getRetailerList(String systemUserName,String systemPassword,String agentUserId);
	
	public String removeStatusOfAgentRetailer(String systemUserName,String systemPassword,String agentOrgId);
	
	public LmsWrapperDrawscheduleBeanResult fetchDrawListForResSubmission(LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean);
	
	public String performManualWinningEntry(LmsWrapperPerformDrawDataBean performDrawDataBean);
	
	public LmsWrapperOnStartGameDataBean getGameData(String systemUserName,String systemPassword);	
	
	public LmsWrapperInventoryMenuDataBean consNonConsAsignInvMenuData(String systemUserName,String systemPassword);
	
	public String verifyConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean);
		
	public String assignConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean);
	
	public String removeConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean);
	
	public String returnConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean);
	
	public LmsWrapperBankDepositBean agentBankDeposit(LmsWrapperBankDepositBean bankDepositBean);
	
	public String RegisterNewSubUser(LmsWrapperUserDetailsBean wrapperUserDetailsBean);
	
	public HashMap<String,LmsWrapperCashierDrawerDataReportBean> fetchCashierWiseDrawerData(LmsWrapperCashierDrawerDataReportBean cashierDrawerDataBean);
	
	public String collectionAgentWiseWithOpeningBal(String orgName);
	
	public LmsWrapperSearchInventoryResponseDataBean consNonConsSearchInvDetail(LmsWrapperSearchInventoryRequestDataBean searchInventoryReqBean);
	
	public ArrayList<LmsWrapperCashierDrawerDataForPWTBean> fetchDataForExportToExcelOfPWT(LmsWrapperCashierDrawerDataForPWTBean lmsWrapperCashierDrawerDataForPWTBean);
	
	public LmsWrapperDrawscheduleBeanResult fetchDrawListForResSubmissionForMacNumber(LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean);
	
	public String performManualWinningMachineNumberEntry(LmsWrapperPerformDrawDataBean performDrawDataBean);
	
	public LmsWrapperToBeCollectedBean fetchToBeCollectedDataForAgent(LmsWrapperCollectionReportOverAllBean lmsWrapperCollectionReportOverAllBean);
	
	// *********** RANDOME ID GENERATION RELATED API's
	
	public LmsWrapperAllRetInfoBean getAllRetailerUserId(String systemUserName , String systemUserPassword);
	
	public LmsWrapperRandomIdResponseBean updateInsertRandomIdInLMS(LmsWrapperRandomIdRequestBean lmsWrapperRandomIdRequestBean);
	
	// *********** RANDOME ID GENERATION RELATED API's
	
	
	public LmsWrapperVerifyBeforeShiftRes verifyBeforeShift(LmsWrapperVerifyBeforeShiftReq lmsWrapperVerifyBeforeShiftReq);

	
	
	
}