package com.skilrock.lms.web.ola;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaGetCancelWithdrawalDetailsBean;
import com.skilrock.lms.beans.OlaGetPendingWithdrawalDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;

public class OlaPendingWithdrawalAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playerName;
	private String newwalletName;
	private double depositAmount;
	private String[] withdrawalCodeArr;

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	public String cancelPendingWithdrawalRequest() {

		try {
			HttpSession session = getRequest().getSession();
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			if (withdrawalCodeArr.length > 0) {
				OlaGetCancelWithdrawalDetailsBean bean = null;
				for (String element : withdrawalCodeArr) {
					OlaGetCancelWithdrawalDetailsBean resultBean = OLAUtility
							.parseCancelWithdrawalXML(element, bean, playerName,rootPath);
					if (resultBean != null
							&& resultBean.getErrorCode().equalsIgnoreCase("0")) {
						System.out.println("Request for withdrawal code "+ element+" is cancelled successfully");
						return SUCCESS;

					} else {
						addActionMessage(resultBean.getErrorText());
						return ERROR;
					}
				}
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	public String depositMoneyWithoutCancelPendingRequest() {
		try {
			int walletId = 0;
			String devWalletName = null;
			if (newwalletName.equalsIgnoreCase("-1")
					|| newwalletName.equalsIgnoreCase("null")) {
				return ERROR;
			} else {
				String[] walletArr = newwalletName.split(":");
				for (int i = 0; i < walletArr.length; i++) {

					walletId = Integer.parseInt(walletArr[0]);
					devWalletName = walletArr[1];
				}
			}
			boolean isPendingData = false;
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			ServletContext sc = ServletActionContext.getServletContext();
			String depositAnyWhere = (String) sc
					.getAttribute("OLA_DEP_ANYWHERE");
			OlaHelper helper = new OlaHelper();
			OlaGetPendingWithdrawalDetailsBean bean = null;
			bean = helper.depositMoney(playerName,
					depositAmount, devWalletName, userBean, walletId,
					depositAnyWhere,bean,isPendingData,rootPath);
			if (bean.getReturnType().equalsIgnoreCase("true"))
				return SUCCESS;
			else {
				addActionMessage(bean.getReturnType());
			return ERROR;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String[] getWithdrawalCodeArr() {
		return withdrawalCodeArr;
	}

	public void setWithdrawalCodeArr(String[] withdrawalCodeArr) {
		this.withdrawalCodeArr = withdrawalCodeArr;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getNewwalletName() {
		return newwalletName;
	}

	public void setNewwalletName(String newwalletName) {
		this.newwalletName = newwalletName;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
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
}
