package com.skilrock.ola.embedded.accMgmt.action;


import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetDepositControllerImpl;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetWithdrawlControllerImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalResponseBean;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;


public class RetailerAccountMgmtAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(RetailerAccountMgmtAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private HttpSession session = null;
	
	private OLADepositRequestBean depositReqBean;
	private OLAWithdrawalRequestBean withdrawalReqBean;
	private Map<Integer, OlaWalletBean> walletDetailsMap=null;	
	
	private String userName;
	private String refCode;
	private double depositAmt;
	private String walletName;
	private int walletId;
	private double version;
	private double wthdrwAmt;
	private String smsCode;	
	
	public  void depositMoney(){
		ServletContext sc = ServletActionContext.getServletContext();
		String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		double olaMinDepositLimit = OLAUtility.getOlaWalletDataMap().get(walletId).getMinDeposit();
		HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		logger.info("depositAnyWhere" + depositAnyWhere+"WalletId :"+walletId);
		try{
				if(walletId > 0){
					walletName = OLAUtility.getWalletName(walletId);
					if(depositAmt>=olaMinDepositLimit){
						if( walletName!=null && refCode!=null){
							OlaRetDepositControllerImpl olaDepositController = new OlaRetDepositControllerImpl();
							depositReqBean = new OLADepositRequestBean();
							depositReqBean.setDepositAnyWhere(depositAnyWhere);
							depositReqBean.setWalletDevName(walletName);
							depositReqBean.setRefCode(refCode);
							depositReqBean.setDepositAmt(depositAmt);
							depositReqBean.setDeviceType("TERMINAL");
							depositReqBean.setWalletId(walletId);
							OLADepositResponseBean  depositResBean = null;
							OlaCommonMethodControllerImpl controller = new OlaCommonMethodControllerImpl();
							boolean isSuccess = controller.verifyRefCode(refCode, walletName);
							if(isSuccess){
								depositResBean = olaDepositController.olaRetPlrDeposit(depositReqBean, userBean);
							
							//For Retailer Balance
								AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
								ajxHelper.getAvlblCreditAmt(userBean);
								double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();				
								NumberFormat nf = NumberFormat.getInstance();
								nf.setMinimumFractionDigits(2);				
								String balance = nf.format(bal).replaceAll(",", "");
							if(depositResBean.isSuccess()){
								String transactionDate=Util.getDateTimeFormat(depositResBean.getTxnDate());
								String txnDate = transactionDate.split(" ")[0];
								String txnTime = transactionDate.split(" ")[1];
								response.getOutputStream().write(("SuccessMsg: "+"Amount Deposited Successfully |balance:"+balance+"|TDate:"+txnDate+"|TTime:"+txnTime+"|TId:"+depositResBean.getTxnId()).getBytes());
								}
							}else{
								response.getOutputStream().write(("ErrorMsg: "+LMSErrors.INVALID_PHONE_NUMBER_MESSAGE).getBytes());
							}
						}else{				
							response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());				
						}
					}else{
						response.getOutputStream().write(("ErrorMsg: "+LMSErrors.MIN_DEPOSIT_LIMIT_ERROR_MESSAGE+" "+olaMinDepositLimit).getBytes());
					}
				}else{
					response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());			
				}
			}catch(LMSException e){
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				if(e.getErrorCode() == 10001){
					errorMessage = errorMessage+" "+e.getErrorMessage()+" ) !!";
				}
				logger.info(e.getErrorCode()+errorMessage);
				try {
						response.getOutputStream().write(("ErrorMsg: "+errorMessage).getBytes());
				}catch(IOException e1){
					logger.info("Error In Setting Response");
					e1.printStackTrace();
				}
			}catch(GenericException e) {
				String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
				logger.info(e.getErrorCode()+errorMessage);
				try {
						response.getOutputStream().write(("ErrorMsg: "+errorMessage).getBytes());
				}catch(IOException e1){
					logger.info("Error In Setting Response");
					e1.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
				try {
					response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
				} catch (IOException e1) {
					logger.info("Error In Setting Response");
					e1.printStackTrace();
				}
			}
		}
	
	public void withdrawMoney(){
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");		
		String withdrawlAnyWhere = (String) sc.getAttribute("OLA_WITHDRAWL_ANYWHERE");		
		logger.info("WithdrawlAnyWhere" + withdrawlAnyWhere);
		try{
			if(walletId>0){
					walletName = OLAUtility.getWalletName(walletId);
					if(wthdrwAmt>0 &&  walletName!=null){
						OlaRetWithdrawlControllerImpl olaWithdrawalController = new OlaRetWithdrawlControllerImpl();
						withdrawalReqBean = new OLAWithdrawalRequestBean();						
						withdrawalReqBean.setWithdrawlAmt(wthdrwAmt);
						withdrawalReqBean.setDevWalletName(walletName);
						withdrawalReqBean.setWithdrawlAnyWhere(withdrawlAnyWhere);
						withdrawalReqBean.setAuthenticationCode(smsCode);
						withdrawalReqBean.setRefCode(refCode);	
						withdrawalReqBean.setDeviceType("TERMINAL");
						withdrawalReqBean.setWalletId(walletId);
						OlaCommonMethodControllerImpl controller = new OlaCommonMethodControllerImpl();
						boolean isSuccess = controller.verifyRefCode(refCode, walletName);
						if(isSuccess){
							
							OLAWithdrawalResponseBean withdrawalResBean = olaWithdrawalController.olaRetPlrWithdrawal(withdrawalReqBean, userBean);
							//For Retailer Updated Balance
								AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
								ajxHelper.getAvlblCreditAmt(userBean);
								double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();				
								NumberFormat nf = NumberFormat.getInstance();
								nf.setMinimumFractionDigits(2);			
								String balance = nf.format(bal).replaceAll(",", "");
							if(withdrawalResBean.isSuccess()){
								String transactionDate=Util.getDateTimeFormat(withdrawalResBean.getTxnDate());
								String txnDate = transactionDate.split(" ")[0];
								String txnTime = transactionDate.split(" ")[1];
								response.getOutputStream().write(("SuccessMsg: "+"Withdrawal Successfull |balance:"+balance+"|TDate:"+txnDate+"|TTime:"+txnTime+"|TId:"+withdrawalResBean.getTxnId()).getBytes());
							}
						}else{
							response.getOutputStream().write(("ErrorMsg: "+LMSErrors.INVALID_PHONE_NUMBER_MESSAGE).getBytes());
						}
				}else{				
					response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());				
				}			
			}else{
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());			
			}
	 }catch(LMSException e){
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			try {
					response.getOutputStream().write(("ErrorMsg: "+errorMessage).getBytes());
			}catch(IOException e1){
				logger.info("Error In Setting Response");
				e1.printStackTrace();
			}
		}catch(GenericException e) {
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			try {
					response.getOutputStream().write(("ErrorMsg: "+errorMessage).getBytes());
			}catch(IOException e1){
				logger.info("Error In Setting Response");
				e1.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
			} catch (IOException e1) {
				logger.info("Error In Setting Response");
				e1.printStackTrace();
			}
		}
	}
	
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}	

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setWalletDetailsMap(Map<Integer, OlaWalletBean> walletDetailsMap) {
		this.walletDetailsMap = walletDetailsMap;
	}

	public Map<Integer, OlaWalletBean> getWalletDetailsMap() {
		return walletDetailsMap;
	}
	
	public void setDepositReqBean(OLADepositRequestBean depositReqBean) {
		this.depositReqBean = depositReqBean;
	}

	public OLADepositRequestBean getDepositReqBean() {
		return depositReqBean;
	}

	public OLAWithdrawalRequestBean getWithdrawalReqBean() {
		return withdrawalReqBean;
	}

	public void setWithdrawalReqBean(OLAWithdrawalRequestBean withdrawalReqBean) {
		this.withdrawalReqBean = withdrawalReqBean;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	
	public double getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(double depositAmt) {
		this.depositAmt = depositAmt;
	}

	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	
	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}
	
	public double getWthdrwAmt() {
		return wthdrwAmt;
	}

	public void setWthdrwAmt(double wthdrwAmt) {
		this.wthdrwAmt = wthdrwAmt;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	
}
