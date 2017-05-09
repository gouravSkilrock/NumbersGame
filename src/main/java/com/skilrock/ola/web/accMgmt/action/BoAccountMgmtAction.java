package com.skilrock.ola.web.accMgmt.action;


import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.ola.accMgmt.controllerImpl.OlaBoDepositControllerImpl;
import com.skilrock.ola.accMgmt.controllerImpl.OlaBoWithdrawlControllerImpl;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetWithdrawlControllerImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalResponseBean;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;


public class BoAccountMgmtAction extends BaseAction{
	public BoAccountMgmtAction() {
		super(BoAccountMgmtAction.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(BoAccountMgmtAction.class);
		
	private String walletName;

	private OLADepositRequestBean depositReqBean;
	private OLAWithdrawalRequestBean withdrawalReqBean;
	private Map<Integer, OlaWalletBean> walletDetailsMap=null;
	private int mobileNumberlimit;
	
	
	public String olaBoMenu(){		
		OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
		setWalletDetailsMap(olaCommonMethodController.getWalletDetails());
		mobileNumberlimit = Integer.parseInt((String)LMSUtility.sc.getAttribute("OLA_PLAYER_MOBILE_NUMBER_LIMIT"));
		return SUCCESS;
	}	
	
	public String depositMoney(){
		OlaCommonMethodControllerImpl olaCommonMethodController=null;
		int walletId=0;
		try{
			walletId=OLAUtility.getWalletId(walletName);
			ServletContext sc = ServletActionContext.getServletContext();
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");	
			double olaMinDepositLimit = OLAUtility.getOlaWalletDataMap().get(walletId).getMinDeposit();
			logger.info("depositAnyWhere" + depositAnyWhere+"WalletName :"+walletName);
			olaCommonMethodController = new OlaCommonMethodControllerImpl();
		
			OlaBoDepositControllerImpl olaDepositController = new OlaBoDepositControllerImpl();
			if (depositReqBean.getDepositAmt() >= olaMinDepositLimit) {
					depositReqBean.setDepositAnyWhere(depositAnyWhere);
					depositReqBean.setWalletDevName(walletName);
					depositReqBean.setDeviceType("WEB");
					depositReqBean.setWalletId(walletId);
					OLADepositResponseBean  depositResBean =olaDepositController.olaBoPlrDeposit(depositReqBean, userBean);
					if(depositResBean.isSuccess()){
						return SUCCESS;
					}else{
						addActionMessage(depositResBean.getErrorMsg());
						setWalletDetailsMap(olaCommonMethodController.getWalletDetails());
						return ERROR;
					}					
			}else{
				addActionMessage(LMSErrors.MIN_DEPOSIT_LIMIT_ERROR_MESSAGE+" "+olaMinDepositLimit);
			}				
		}catch (LMSException e) {
			System.out.println(e.getErrorCode());
			String errorMessage=e.getErrorCode()==0?e.getErrorMessage():LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);				
		}catch (GenericException e){
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);
		}
		setWalletDetailsMap(olaCommonMethodController.getWalletDetails());
		return ERROR;
		
	}
	
	
	public String withdrawalMoney() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		ServletContext sc = ServletActionContext.getServletContext();
		String withdrawlAnyWhere = (String) sc.getAttribute("OLA_WITHDRAWL_ANYWHERE");
		logger.info("WithdrawlAnyWhere" + withdrawlAnyWhere);
		OlaCommonMethodControllerImpl olaCommonMethodController = new OlaCommonMethodControllerImpl();
		int walletId=0;
		try{
			walletId=OLAUtility.getWalletId(walletName);
			OlaBoWithdrawlControllerImpl olaWithdrawalController = new OlaBoWithdrawlControllerImpl();
			if (withdrawalReqBean.getWithdrawlAmt() > 0) {
				withdrawalReqBean.setWithdrawlAnyWhere(withdrawlAnyWhere);
				withdrawalReqBean.setDevWalletName(walletName);
				withdrawalReqBean.setDeviceType("WEB");
				withdrawalReqBean.setWalletId(walletId);
				OLAWithdrawalResponseBean withdrawalResBean = olaWithdrawalController.olaBoPlrWithdrawal(withdrawalReqBean, userBean);
				if(withdrawalResBean.isSuccess()){
					return SUCCESS;
				}else{
					addActionMessage(withdrawalResBean.getErrorMsg());
					return ERROR;
				}
			}
		}catch (LMSException e) {
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);
		}catch (GenericException e){
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);
		}
		setWalletDetailsMap(olaCommonMethodController.getWalletDetails());
		return ERROR;
	}
	
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public OLADepositRequestBean getDepositReqBean() {
		return depositReqBean;
	}

	public void setDepositReqBean(OLADepositRequestBean depositReqBean) {
		this.depositReqBean = depositReqBean;
	}

	public OLAWithdrawalRequestBean getWithdrawalReqBean() {
		return withdrawalReqBean;
	}

	public void setWithdrawalReqBean(OLAWithdrawalRequestBean withdrawalReqBean) {
		this.withdrawalReqBean = withdrawalReqBean;
	}

	public Map<Integer, OlaWalletBean> getWalletDetailsMap() {
		return walletDetailsMap;
	}

	public void setWalletDetailsMap(Map<Integer, OlaWalletBean> walletDetailsMap) {
		this.walletDetailsMap = walletDetailsMap;
	}

	public int getMobileNumberlimit() {
		return mobileNumberlimit;
	}

	public void setMobileNumberlimit(int mobileNumberlimit) {
		this.mobileNumberlimit = mobileNumberlimit;
	}
	
	
	
}
