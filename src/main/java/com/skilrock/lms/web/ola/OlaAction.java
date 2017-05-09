package com.skilrock.lms.web.ola;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
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
import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OlaGetPendingWithdrawalDetailsBean;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OLAPlrLotteryHelper;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithdrawalHelper;

public class OlaAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(OlaAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private String userName;
	private double amount;
	private String walletName;
	private String userPhone;
	private String smsCode;
	private String start_date;
	private String end_Date;
	public String depositMoney() throws LMSException {
		int walletId = 0;
		boolean isPendingData = true;
		String devWalletName = null;
		ServletContext sc = ServletActionContext.getServletContext();
		String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
		logger.info("depositAnyWhere" + depositAnyWhere+"WalletName :"+walletName);
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				devWalletName = walletArr[1];
		}
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		
		OlaHelper olaHelper = new OlaHelper();
		if (amount > 0) {

			OlaGetPendingWithdrawalDetailsBean pendingResponseBean = null;
			if (devWalletName.equalsIgnoreCase("KhelPlayRummy")) {
				
				
				String  returnType =OlaHelper.depositMoneyForKpRummy(depositAnyWhere,userName.trim(),amount,
													userBean,devWalletName,walletId,userPhone);
				if(returnType.equalsIgnoreCase("true")){
					return SUCCESS;
				}else{
					addActionMessage(returnType);
					return ERROR;
				}
				
			}else	if (devWalletName.equalsIgnoreCase("PLAYTECH_CASINO")) {
				try {
					pendingResponseBean = olaHelper.depositMoney(userName, amount, devWalletName, userBean, walletId,
							depositAnyWhere, pendingResponseBean,isPendingData,rootPath);
					if (pendingResponseBean.getReturnType().equalsIgnoreCase("true"))
						return SUCCESS;
					else if (pendingResponseBean.getReturnType().equalsIgnoreCase(
					         "PENDING_WITHDRAWAL_REQUEST")) {
						Map<String, ArrayList<Object>> pendingRequestMap = new TreeMap<String, ArrayList<Object>>();

						System.out.println(pendingResponseBean
								.getPendingWithdrawalList());
						for (int i = 0; i < pendingResponseBean
								.getPendingWithdrawalList().size(); i++) {
							String date = pendingResponseBean
									.getPendingWithdrawalList().get(i).getDateList()
									.get(0).getWithdrawRequestDate();
							double amount = pendingResponseBean
							.getPendingWithdrawalList().get(i).getAmountList()
							.get(0).getAmount();
							String withdrawalRequestCode = pendingResponseBean
									.getPendingWithdrawalList().get(i)
									.getPendingWithdrawalCodeList().get(0)
									.getPendingWithdrawalCode();
							ArrayList<Object> list = new ArrayList<Object>();
							list.add(amount);
							list.add(date);
							pendingRequestMap.put(withdrawalRequestCode, list);
						}
						session.setAttribute("pendingRequestMap", pendingRequestMap);
						return "PENDING_WITHDRAWAL_REQUEST";
					} else {
						addActionMessage(pendingResponseBean.getReturnType().toString());
						return ERROR;
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (devWalletName.equalsIgnoreCase("RUMMY")) {
				
				int  validMonths= Integer.parseInt((String)sc.getAttribute("olaDepositExpiry"));
				String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
				String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
				logger.info("User :"+userBean.getUserName()+"Root Path"+rootPath+"DesKey "+desKey+"propKey"+propKey);
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
				if(flexiCardPurchaseBean.isSuccess()){	
					session.setAttribute("cashCardList",
							flexiCardPurchaseBean);
					return SUCCESS;
				}else{
					addActionMessage(flexiCardPurchaseBean.getReturnType());
					return ERROR;
				}
				
			}else if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
				OLAPlrLotteryHelper plrLottery = new OLAPlrLotteryHelper();
				String  returnType =plrLottery.plrLotteryDeposit(depositAnyWhere,userName.trim(),amount,
						userBean, walletId,userPhone);
				if(returnType.equalsIgnoreCase("true")){
					return SUCCESS;
				}else{
					addActionMessage(returnType);
					return ERROR;
				}
			}
		} else {
			return ERROR;
		}
		return ERROR;
	}

	public String withdrawalMoney() throws LMSException {
		int walletId = 0;
		String devWalletName = null;
		ServletContext sc = ServletActionContext.getServletContext();
		String WithdrawlAnyWhere = (String) sc
				.getAttribute("OLA_WITHDRAWL_ANYWHERE");
		System.out.println("WithdrawlAnyWhere" + WithdrawlAnyWhere);
		String[] walletArr = walletName.split(":");
		for (int i = 0; i < walletArr.length; i++) {
			walletId = Integer.parseInt(walletArr[0]);
			devWalletName = walletArr[1];
		}
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		OlaHelper olaHelper = new OlaHelper();
		if (amount > 0) {
			if (devWalletName.equalsIgnoreCase("PLAYTECH_CASINO")){
				String returnStr = olaHelper.withdrawlMoney(userName, amount,
						devWalletName, userBean, walletId, WithdrawlAnyWhere,
						smsCode);
				if (returnStr.equalsIgnoreCase("true")) {
					return SUCCESS;
				} else {

					addActionMessage(returnStr.toString());
					return ERROR;
				}
			}
			// Rummy Withdrawal 
			else if(devWalletName.equalsIgnoreCase("RUMMY")||devWalletName.equalsIgnoreCase("KhelPlayRummy")) {
				OlaRummyWithdrawalHelper rummyHelper =new OlaRummyWithdrawalHelper();
				String isSuccess = rummyHelper.olaWithdrawalMoneyFromLMSForRummy(userName, amount,
						devWalletName, userBean, walletId, WithdrawlAnyWhere,
						smsCode);
				if(isSuccess.equalsIgnoreCase("true")){
					
					return SUCCESS;
				}
				else{
					addActionMessage(isSuccess);
					return ERROR;
				}
				
			}else if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY"))  {	//Player Mgmt Withdrawal 
				OLAPlrLotteryHelper plrLottery = new OLAPlrLotteryHelper();
				String  returnType =plrLottery.plrLotteryWithdrawal(userName, amount,
						devWalletName, userBean, walletId, WithdrawlAnyWhere,
						smsCode);
				if(returnType.equalsIgnoreCase("true")){
					return SUCCESS;
				}else{
					addActionMessage(returnType);
					return ERROR;
				}
			}	
		} else {
			return ERROR;
		}
		return ERROR;
	}
	public void olaWalletData() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			OlaHelper olaHelper = new OlaHelper();
			String walletDetails = olaHelper.olaWalletDetails();
			System.out.println("wallet details String on retailer OLA == "
					+ walletDetails);
			pw.print(walletDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Added For commUpdateType

	public void commUpdateType() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			OlaHelper olaHelper = new OlaHelper();
			String commUpTyp = olaHelper.commUpdateTypes();
			System.out.println("wallet details String on retailer OLA == "
					+ commUpTyp);
			pw.print(commUpTyp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// By Neeraj

	public void checkUserName() throws LMSException {
		try {
			PrintWriter pw = response.getWriter();
			Map<String, String> errorMap=null;
			int walletId=0;
			String devWalletName=null;
			if (walletName.equalsIgnoreCase("-1")
					|| walletName.equalsIgnoreCase("null")) {
				pw.print("");
			} else {
				String[] walletArr = walletName.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				devWalletName = walletArr[1];
				
			}
			OLARummyHelper helper = new 	OLARummyHelper();
			OlaHelper olaHelper = new OlaHelper();
			if (devWalletName.equalsIgnoreCase("KhelPlayRummy")){
				
				errorMap=olaHelper.verifyPlrName(userName,walletId,"USER_DEPOSIT_AVAILABILITY");
				
			}else if (devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
				errorMap=helper.verifyPlrName(userName);
				
			}else if(devWalletName.equalsIgnoreCase("RUMMY")){
				errorMap = helper.verifyOrgName(userName);
				
			}
			pw.print(errorMap.toString().replace("{", "").replace("}", ""));
			} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		} 
	}
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	
}
