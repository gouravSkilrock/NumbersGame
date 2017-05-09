package com.skilrock.lms.web.ola;


import java.io.IOException;
import java.io.PrintWriter;
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


import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OLAPlrLotteryHelper;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaHelper;


public class OlaAgtAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(OlaAgtAction.class);
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
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
	
		
	
		if (amount > 0) {

			
			if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
			
		
				String  returnType =OLAPlrLotteryHelper.plrLotteryAgtDeposit(depositAnyWhere,userName.trim(),amount,
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
		String WithdrawlAnyWhere = (String) sc.getAttribute("OLA_WITHDRAWL_ANYWHERE");
		logger.info("WithdrawlAnyWhere" + WithdrawlAnyWhere);
		
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				devWalletName = walletArr[1];
		}
		
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		if (amount > 0) {
		
			if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY"))  {	//Player Mgmt Withdrawal 
				
				String  returnType =OLAPlrLotteryHelper.plrLotteryAgtWithdrawal(userName, amount,
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
			String devWalletName=null;
			if (walletName.equalsIgnoreCase("-1")
					|| walletName.equalsIgnoreCase("null")) {
				pw.print("");
			} else {
				String[] walletArr = walletName.split(":");
				 devWalletName = walletArr[1];
			}
			OLARummyHelper helper = new 	OLARummyHelper();
			if(devWalletName.equalsIgnoreCase("RUMMY")){
				errorMap = helper.verifyOrgName(userName);
				
			}else if (devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
				errorMap=helper.verifyPlrName(userName);
				
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
