package com.skilrock.lms.embedded.ola.olaMgmt;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
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

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OlaReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.ola.OLAPlrLotteryHelper;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithdrawalHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaRetailerReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class OlaRpos implements ServletRequestAware,ServletResponseAware{

	private String userName ;
	private double version; 
	private HttpServletRequest servletRequest ;
	private HttpServletResponse response ;
	private HttpSession session = null;
	private int walletId ;
	private double amount ;
	private  String userPhone;
	private String smsCode;
	private String plrId;
	private String fromDate;


	Log logger = LogFactory.getLog(OlaRpos.class);
	public void fetchWalletData(){
		logger.info("Fetch Wallet Data for userName:"+userName+"version:"+version);
	try{
		ServletContext sc = LMSUtility.sc;
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if(currentUserSessionMap==null){
			try{
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
				return ;
			}catch(Exception e){
				
				e.printStackTrace();
				
			}
			
		}
		session = (HttpSession)currentUserSessionMap.get(userName);
		if(!CommonFunctionsHelper.isSessionValid(session)){
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return ;
		}
		if(version>=8.5){
			OlaRposHelper helper = new OlaRposHelper();
			String walletInfo ="WalletData:"+helper.walletData();
			logger.info("reponse:"+walletInfo);
			response.getOutputStream().write(walletInfo.getBytes());
		}else{
			response.getOutputStream().write(("ErrorMsg: Bad Version").getBytes());
			
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
	
public  void depositMoney(){
	String walletName = null;
 try{
	 if(walletId>0){
			walletName = OLAUtility.getWalletName(walletId);
		 
			if(amount>0 && walletName!=null&&plrId!=null){
				ServletContext sc = ServletActionContext.getServletContext();
				String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
				Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
				HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
				UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
				 if(walletName.equalsIgnoreCase("PLAYER_LOTTERY")){
						OLAPlrLotteryHelper plrLottery = new OLAPlrLotteryHelper();
						String  returnType =plrLottery.plrLotteryDeposit(depositAnyWhere,plrId.trim(),amount,
								userBean, walletId,userPhone);
						if(returnType.equalsIgnoreCase("true")){
							response.getOutputStream().write(("SuccessMsg: "+"Amount Deposited Successfully").getBytes());
						}else{
							
							response.getOutputStream().write(("ErrorMsg: "+returnType).getBytes());
							///addActionMessage(returnType);
							//return ERROR;
						}
				 	}else if(walletName.equalsIgnoreCase("RUMMY")){
				 		int  validMonths= Integer.parseInt((String)sc.getAttribute("olaDepositExpiry"));
						String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
						String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
						logger.info("User :"+userBean.getUserName()+"DesKey "+desKey+"propKey"+propKey);
						Calendar cal = Calendar.getInstance();
						java.sql.Date purchaseDate = new java.sql.Date(cal.getTime().getTime());
						cal.add(Calendar.MONTH,validMonths);//  Expiry date 
						java.sql.Date expiryDate = new java.sql.Date(cal.getTime().getTime());
						OLARummyHelper olaRummy = new OLARummyHelper();
						FlexiCardPurchaseBean flexiCardPurchaseBean = new 	FlexiCardPurchaseBean();
						flexiCardPurchaseBean.setAmount(amount);
						flexiCardPurchaseBean.setDenomiationType("FLEXI");
		          		flexiCardPurchaseBean.setPlayerName(userName.trim());
		          		flexiCardPurchaseBean.setPurchaseDate(purchaseDate.toString());
						logger.info("Expiry Date "+expiryDate+" Deposit Amount "+amount+" For Player "+userName);
						flexiCardPurchaseBean  = olaRummy.initRummyDeposit(amount,
										userBean, walletId,depositAnyWhere,
										flexiCardPurchaseBean,expiryDate,
										userPhone,desKey,propKey);
						AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
						ajxHelper.getAvlblCreditAmt(userBean);
						double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
				
						NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
				
						String balance = nf.format(bal).replaceAll(",", "");
						
						if(flexiCardPurchaseBean.isSuccess()){
							logger.info("SuccessMsg: Amount Deposited Successfully |Refcode: "+flexiCardPurchaseBean.getSerialNumber()+"|Balance:"+balance);
							response.getOutputStream().write(("SuccessMsg: Amount Deposited Successfully |Refcode: "+flexiCardPurchaseBean.getSerialNumber()+"|Balance:"+balance).getBytes());
						}else {
							response.getOutputStream().write(("ErrorMsg: "+flexiCardPurchaseBean.getReturnType()).getBytes());
							
						}
				 		
				 	}
				 
				
				
			}else{
				
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
				
			}
			
		}else{
			response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
			
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

	String walletName = null;
 try{
	 if(walletId>0){
			walletName = OLAUtility.getWalletName(walletId);
		 
			if(amount>0 &&  walletName!=null){
				ServletContext sc = ServletActionContext.getServletContext();
				String WithdrawlAnyWhere = (String) sc.getAttribute("OLA_WITHDRAWL_ANYWHERE");
				Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
				HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
				UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
				if(walletName.equalsIgnoreCase("RUMMY")) {
					OlaRummyWithdrawalHelper rummyHelper =new OlaRummyWithdrawalHelper();
					String isSuccess = rummyHelper.olaWithdrawalMoneyFromLMSForRummy(plrId.trim(), amount,
							walletName, userBean, walletId, WithdrawlAnyWhere,
							smsCode);
					AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
					ajxHelper.getAvlblCreditAmt(userBean);
					double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
			
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(2);
			
					String balance = nf.format(bal).replaceAll(",", "");
					if(isSuccess.equalsIgnoreCase("true")){
						response.getOutputStream().write(("SuccessMsg: Withdrawal Successful |Balance:"+balance).getBytes());
					}else {
						response.getOutputStream().write(("ErrorMsg: "+isSuccess).getBytes());
						
					}
					
				}else if(walletName.equalsIgnoreCase("PLAYER_LOTTERY"))  {	//Player Mgmt Withdrawal 
					OLAPlrLotteryHelper plrLottery = new OLAPlrLotteryHelper();
					String  returnType =plrLottery.plrLotteryWithdrawal(plrId.trim(), amount,
							walletName, userBean, walletId, WithdrawlAnyWhere,
							smsCode);
									
					if(returnType.equalsIgnoreCase("true")){
						response.getOutputStream().write(("SuccessMsg: Withdrawal Successful ").getBytes());
					}else {
						response.getOutputStream().write(("ErrorMsg: "+returnType).getBytes());
						
					}
					
				}
						
				
				
			}else{
				
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
				
			}
			
		}else{
			response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
			
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

public void depositWithdrawRepData (){

	OlaRetailerReportHelper helper = new OlaRetailerReportHelper();
	
	try{
		ServletContext sc = ServletActionContext.getServletContext();
		 if(walletId>0){
			String	walletName = OLAUtility.getWalletName(walletId);
			if(walletName!=null&&fromDate!=null&&(!(fromDate.trim()).equalsIgnoreCase(""))){
				Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
				HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
				UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
				if(walletName.equalsIgnoreCase("RUMMY")) {
					String repData ="repData:"+helper.fetchOlaRetailerReportDataConsolidate(fromDate, walletId,userBean.getUserOrgId());
					response.getOutputStream().write(repData.getBytes());
						}
				
				}
			}
		
		
	}catch(Exception e){
		e.printStackTrace();
		
		try {
			response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	}
	
}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest=servletRequest;
		
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.response =servletResponse;
		
	}
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public String getPlrId() {
		return plrId;
	}

	public void setPlrId(String plrId) {
		this.plrId = plrId;
	}
	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
}
