package com.skilrock.lms.api.lmsPayment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.beans.TpPwtApiBean;
import com.skilrock.lms.api.common.TpUtilityHelper;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentResponseBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsOrgInfoBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;

public   class LmsPaymentServiceImpl implements ILmsPaymentService {
	static Log logger=LogFactory.getLog(LmsPaymentServiceImpl.class);
	
	
	public LmsCashPaymentResponseBean depositCashPayment(
			LmsCashPaymentBean cashPaymentBean) {
		logger.debug("Request Cash Payment Data="+cashPaymentBean);
		LmsCashPaymentResponseBean cashResponseBean=new LmsCashPaymentResponseBean();
		HttpServletRequest request = XFireServletController.getRequest();
		String ip=request.getRemoteHost();
		if(!TpUtilityHelper.validateTpSystemUser(ip,cashPaymentBean.getSystemUserName(),cashPaymentBean.getSystemPassword())){
			cashResponseBean.setErrorCode("102");
			cashResponseBean.setSuccess(false);
			return cashResponseBean;
		}
		
		if(cashPaymentBean.getRefTransId() == null || cashPaymentBean.getRefTransId().length() == 0){
			cashResponseBean.setErrorCode("119");
			cashResponseBean.setSuccess(false);
			return cashResponseBean;
		}
		
		if(cashPaymentBean.getAmount() == 0){
			cashResponseBean.setErrorCode("101");
			cashResponseBean.setSuccess(false);
			return cashResponseBean;
		}
        request.setAttribute("code", "MGMT");
        request.setAttribute("interfaceType", "API");
        ServletActionContext.setRequest(request);
		LmsPaymentServiceApiHelper paymentHelper=new LmsPaymentServiceApiHelper();
		try {
			if(cashPaymentBean.getAmount()>0)
				cashResponseBean=paymentHelper.depositCashPayment(cashPaymentBean,TpUtilityHelper.tpSystemAuthenticationMap.get(ip).getTpSystemId());
			else if(cashPaymentBean.getAmount()<0)
				cashResponseBean=paymentHelper.depositDebitNotePayment(cashPaymentBean,TpUtilityHelper.tpSystemAuthenticationMap.get(ip).getTpSystemId());

			cashResponseBean.setErrorCode("100");
			cashResponseBean.setSuccess(true);
		} catch (LMSException e) {
		//e.printStackTrace();
		cashResponseBean.setErrorCode(e.getMessage());
		cashResponseBean.setSuccess(false);
		}
		logger.debug("Response Cash Payment Data="+cashResponseBean);
		return cashResponseBean;
	}

	
	public LmsOrgInfoBean getOrgInfo(String organizationCode,
			String systemUserName, String systemUserPassword) {
		logger.debug("Request Get Org Info Data=organizationCode"+organizationCode+"systemUserName="+systemUserName+"systemUserPassword"+systemUserPassword);
		LmsOrgInfoBean orgInfoBean=new LmsOrgInfoBean();
		HttpServletRequest request = XFireServletController.getRequest();
		String ip=request.getRemoteHost();
		if(!TpUtilityHelper.validateTpSystemUser(ip,systemUserName,systemUserPassword)){
			orgInfoBean.setErrorCode("102");
			orgInfoBean.setSuccess(false);
			return orgInfoBean;
		}
		
		LmsPaymentServiceApiHelper serviceApiHelper=new LmsPaymentServiceApiHelper();
		try {
			orgInfoBean=serviceApiHelper.getOrgInfo(organizationCode,"RETAILER");
			orgInfoBean.setErrorCode("100");
			orgInfoBean.setSuccess(true);
		} catch (LMSException e) {
			//e.printStackTrace();
			orgInfoBean.setErrorCode(e.getMessage());
			orgInfoBean.setSuccess(false);
		}
		logger.debug("Response Get Org Info Data="+orgInfoBean);

		return orgInfoBean;
	}

	
	public LmsCashPaymentResponseBean getPaymentStatus(String refTransId,
			String systemUserName, String systemUserPassword) {
		logger.debug("Request Get Ref Transaction Id Data=refTransId"+refTransId+"systemUserName="+systemUserName+"systemUserPassword"+systemUserPassword);

		LmsCashPaymentResponseBean cashPaymentResponseBean=new LmsCashPaymentResponseBean();
		HttpServletRequest request = XFireServletController.getRequest();
		String ip=request.getRemoteHost();
		if(!TpUtilityHelper.validateTpSystemUser(ip,systemUserName,systemUserPassword)){
			cashPaymentResponseBean.setErrorCode("102");
			cashPaymentResponseBean.setSuccess(false);
			return cashPaymentResponseBean;
		}
		LmsPaymentServiceApiHelper serviceApiHelper=new LmsPaymentServiceApiHelper();
		try {
			cashPaymentResponseBean=serviceApiHelper.getPaymentStatus(refTransId,TpUtilityHelper.tpSystemAuthenticationMap.get(ip).getTpSystemId());
			cashPaymentResponseBean.setErrorCode("100");
			cashPaymentResponseBean.setSuccess(true);
		} catch (LMSException e) {
		//e.printStackTrace();
		cashPaymentResponseBean.setErrorCode(e.getMessage());
		cashPaymentResponseBean.setSuccess(false);
		}
		logger.debug("Response Get Ref Transaction Id Data="+cashPaymentResponseBean);

		return cashPaymentResponseBean;
	}


	@Override
	public PWTApiBean verifyTicket(String ticketNbr, String systemUserName,
			String systemUserPassword) {
		HttpServletRequest request = XFireServletController.getRequest();
		String ip = request.getRemoteHost();
		logger.info("Ticket Verification Data for FastPayApi Ticket Number:-"+ticketNbr+"System User Name:"+systemUserName+"System User Password:"+systemUserPassword+" IP:"+ip);
		PWTApiBean  verifyTicketBean = new PWTApiBean();
		if(!TpUtilityHelper.validateTpSystemUser(ip,systemUserName,systemUserPassword)){
			verifyTicketBean.setErrorCode("102");
			verifyTicketBean.setSuccess(false);
			logger.info("Authentication Error");
			return verifyTicketBean;
		}
	    LmsPaymentServiceApiHelper helper = new LmsPaymentServiceApiHelper();
        UserInfoBean userInfoBean = TpUtilityHelper.getUserData();
        if(userInfoBean!=null&&ticketNbr!=null){
        	if("FAILED".equalsIgnoreCase(userInfoBean.getStatus())){
        		verifyTicketBean.setSuccess(false);
        		verifyTicketBean.setErrorCode("500");
        		logger.info("Error In User Data(UserInfoBean) ");
    			return verifyTicketBean;
    		}
    		try {
    			verifyTicketBean =helper.verifyTicketNo(userInfoBean,ticketNbr);
    			return verifyTicketBean;
    		} catch (LMSException e) {
    			
    			e.printStackTrace();
    			verifyTicketBean.setSuccess(false);
    			verifyTicketBean.setErrorCode("500");
    		}
        	
        }else{
        	verifyTicketBean.setSuccess(false);
        	verifyTicketBean.setErrorCode("500");
        }
		return verifyTicketBean;
	}


	@Override
	public PWTApiBean pwtPayment(TpPwtApiBean pwtReqBean) {
		
		PWTApiBean  verifyTicketBean = new PWTApiBean();
	try{
		HttpServletRequest request = XFireServletController.getRequest();
		String ip = request.getRemoteHost();
		String ticketNbr=pwtReqBean.getTicketNbr();
		String systemUserName=pwtReqBean.getSystemUserName();
		String systemUserPassword=pwtReqBean.getSystemUserPassword();
		String refTransId =pwtReqBean.getRefTransId();
		double amount =0.0;
		amount=pwtReqBean.getAmount();
		PlayerBean plrInfoBean =new PlayerBean();
		plrInfoBean.setFirstName(pwtReqBean.getFirstName());
		plrInfoBean.setLastName(pwtReqBean.getLastName());
		plrInfoBean.setIdNumber(pwtReqBean.getIdNumber());
		plrInfoBean.setIdType(pwtReqBean.getIdType());
		plrInfoBean.setPlrCountry(pwtReqBean.getPlrCountry());
		plrInfoBean.setPlrState(pwtReqBean.getPlrState());
		
		logger.info("Ticket VerificationAndPayment Data for FastPayApi Ticket Number:-"+ticketNbr+"System User Name:"+systemUserName+"System User Password:"+systemUserPassword+" IP:"+ip+"Amount:"+amount+"Player Name: "+plrInfoBean.getFirstName()+" Player IdNumber:"+plrInfoBean.getIdNumber()+" Player IdType:"+plrInfoBean.getIdType()+"Player Mobile:"+plrInfoBean.getPhone());
		
		if(!TpUtilityHelper.validateTpSystemUser(ip,systemUserName,systemUserPassword)){
			verifyTicketBean.setErrorCode("102");
			verifyTicketBean.setSuccess(false);
			logger.info("Authentication Error");
			return verifyTicketBean;
		}
		
		request.setAttribute("code", "MGMT");	
        request.setAttribute("interfaceType", "API");
        ServletActionContext.setRequest(request);
        LmsPaymentServiceApiHelper helper = new LmsPaymentServiceApiHelper();
        UserInfoBean userInfoBean = TpUtilityHelper.getUserData();
        if(userInfoBean!=null&&ticketNbr!=null&&amount>0&&plrInfoBean!=null&&plrInfoBean.getFirstName()!=null
        		&&plrInfoBean.getFirstName().trim()!=""&&plrInfoBean.getIdNumber()!=null&&plrInfoBean.getIdNumber().trim()!=""
        		&&plrInfoBean.getIdType().trim()!=null&&plrInfoBean.getIdType().trim()!=""){
        	if("FAILED".equalsIgnoreCase(userInfoBean.getStatus())){
        		verifyTicketBean.setSuccess(false);
        		verifyTicketBean.setErrorCode("500");
        		logger.info("Error In User Data(UserInfoBean) ");
    			return verifyTicketBean;
    		}
    		try {
    			// set Default Player Country and State  
    			plrInfoBean.setPlrState("Volta");
    			plrInfoBean.setPlrCountry("GHANA");
    			// set Default Player Info
    			plrInfoBean.setPlrAddr1("");
    			plrInfoBean.setPlrCity("");
    			plrInfoBean.setPlrPin(0);
    			
    			verifyTicketBean =helper.pwtVerifyAndPay(userInfoBean,ticketNbr,amount,refTransId,plrInfoBean,TpUtilityHelper.tpSystemAuthenticationMap.get(ip).getTpSystemId());
    			return verifyTicketBean;
    		} catch (LMSException e) {
    			
    			e.printStackTrace();
    			verifyTicketBean.setSuccess(false);
    			verifyTicketBean.setErrorCode("500");
    		}
        	
        }else{
        	verifyTicketBean.setSuccess(false);
        	verifyTicketBean.setErrorCode("500");
        }
	}catch(Exception e){
		e.printStackTrace();
		logger.info("Error In Request");
		verifyTicketBean.setSuccess(false);
    	verifyTicketBean.setErrorCode("500");
	}	
		
		return verifyTicketBean;
	}
	
}