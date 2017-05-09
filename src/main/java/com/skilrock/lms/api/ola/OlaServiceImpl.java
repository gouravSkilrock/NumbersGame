package com.skilrock.lms.api.ola;



import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.api.ola.beans.OlaRummyDepositBean;
import com.skilrock.lms.api.ola.beans.OlaWithdrawlRequestBean;




public class OlaServiceImpl  implements IOlaService{
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(OlaServiceImpl.class);
	public OlaRummyDepositBean plrDepositVerification(OlaRummyDepositBean olaServiceBean)
	{
		HttpServletRequest request = XFireServletController.getRequest();		
		//String ip = request.getRemoteHost();
		String ip = request.getHeader("X-Real-IP") == null ? request.getRemoteAddr() : request.getHeader("X-Real-IP");
		ServletContext sc = request.getSession().getServletContext();
		String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
		String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
		logger.info("Verifying for user "+olaServiceBean.getUserName()+" with password"+olaServiceBean.getPassword()+"for IP "+ip);
		logger.info("having Serial Number"+olaServiceBean.getSerialNumber()+"Pin Number"+olaServiceBean.getOlaPIN()+" and Amount"+olaServiceBean.getDepositeAmount()+"with Reference transaction id"+olaServiceBean.getRefTransId()
				+"For Player"+olaServiceBean.getPlayerId()+"Deposit Type"+olaServiceBean.getDepositType()+"ErrorCode"+olaServiceBean.getErrorCode());
		System.out.println("Verifying for User "+olaServiceBean.getUserName()+" For Reference Id"+olaServiceBean.getOlaTranxId()+"With IP "+ip);
		request.setAttribute("code","OLA");
		request.setAttribute("interfaceType","OLA SERVICE");
		ServletActionContext.setRequest(request);  
		OlaServiceApiHelper helper = new OlaServiceApiHelper();
		try {
			olaServiceBean=helper.verifyUserRefId(olaServiceBean,ip,desKey,propKey);
			logger.info("Verification Status "+olaServiceBean.getDepositType()+" Error Code"+olaServiceBean.getErrorCode());
		    System.out.println("Message: "+olaServiceBean.getDepositType()+" Error Code"+olaServiceBean.getErrorCode());		 			 			 
		}
		catch (Exception e) {
			e.printStackTrace();
						
		}
		  
		  
		return olaServiceBean;
		
		
	}
	@Override
	public OlaWithdrawlRequestBean plrWithdrawlRequest(OlaWithdrawlRequestBean olaServiceBean) {
		HttpServletRequest request = XFireServletController.getRequest();		
		//String ip = request.getRemoteHost();
		String ip = request.getHeader("X-Real-IP") == null ? request.getRemoteAddr() : request.getHeader("X-Real-IP");
		
		logger.info("Verifying for user "+olaServiceBean.getUserName()+" with password"+olaServiceBean.getPassword()+"for IP "+ip);
		request.setAttribute("code","OLA");
		request.setAttribute("interfaceType","OLA SERVICE");
		ServletActionContext.setRequest(request);  
		OlaServiceApiHelper helper = new OlaServiceApiHelper();
		try {
			olaServiceBean.setSuccess(false);
			olaServiceBean=helper.processWithdrawalRequest(olaServiceBean,ip);
			logger.info("Verification Status "+olaServiceBean.isSuccess()+" Error Code"+olaServiceBean.getErrorCode()+"Error Msg"+olaServiceBean.getErrorMsg());
		 	 			 			 
		}
		catch (Exception e) {
			e.printStackTrace();
						
		}
		  
		  
		return olaServiceBean;
	}
	

}
