package com.skilrock.lms.web.ola;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithdrawalHelper;

public class PlayerDashBoardAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	String name;
	String code;
	String walletType;
	String wallet;
	String phone;
	double amount;

	public void getOLAPlayerDashboard() throws IOException {
		PrintWriter out = response.getWriter();
		PlayerDashBoardHelper helper = new PlayerDashBoardHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession()
				.getAttribute("USER_INFO");
		out.print(helper.getPlrList(userBean.getUserName()));
	}

	public void updatePlayersWallet() throws IOException,
			NumberFormatException, LMSException {

		boolean isPendingData = true;
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		PrintWriter out = response.getWriter();
		String walletDeatils[] = wallet.split(":");
		String walletId = walletDeatils[0];
		String devWalletName = walletDeatils[1];
		String smsCode = walletDeatils[2];
		String WithdrawlAnyWhere = (String) sc
				.getAttribute("OLA_WITHDRAWL_ANYWHERE");
		int validMonths = Integer.parseInt((String) sc
				.getAttribute("olaDepositExpiry"));

		if (walletType.equalsIgnoreCase("withdraw")) {
			if (devWalletName.equalsIgnoreCase("PLAYTECH_CASINO")) {
				out.write("No Definition Given");
			} else if (devWalletName.equalsIgnoreCase("RUMMY")) {
				OlaRummyWithdrawalHelper rummyHelper = new OlaRummyWithdrawalHelper();
				String isSuccess = rummyHelper
						.olaWithdrawalMoneyFromLMSForRummy(name, amount,
								devWalletName, userBean, Integer
										.parseInt(walletId), WithdrawlAnyWhere,
								smsCode);
				if (isSuccess.equalsIgnoreCase("true")) {
					out.write("success");
				} else {
					out.write(isSuccess);
				}
			}
		} else if (walletType.equalsIgnoreCase("deposit")) {
			String rootPath = (String) session.getAttribute("ROOT_PATH");
			String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
			String propKey = (String) sc.getAttribute("OLA_PIN_AES_KEY");
			String depositAnyWhere = (String) sc
					.getAttribute("OLA_DEP_ANYWHERE");
			if (amount > 0) {
				if (devWalletName.equalsIgnoreCase("PLAYTECH_CASINO")) {
					out.write("No Definition Given");
				} else if (devWalletName.equalsIgnoreCase("RUMMY")) {
					Calendar cal = Calendar.getInstance();
					java.sql.Date purchaseDate = new java.sql.Date(cal
							.getTime().getTime());
					cal.add(Calendar.MONTH, validMonths);// Expiry date
					java.sql.Date expiryDate = new java.sql.Date(cal.getTime()
							.getTime());
					OLARummyHelper olaRummy = new OLARummyHelper();
					FlexiCardPurchaseBean flexiCardPurchaseBean = new FlexiCardPurchaseBean();
					flexiCardPurchaseBean.setAmount(amount);
					flexiCardPurchaseBean.setDenomiationType("FLEXI");
					flexiCardPurchaseBean.setPlayerName(name.trim());
					flexiCardPurchaseBean.setPurchaseDate(purchaseDate
							.toString());
					flexiCardPurchaseBean = olaRummy.initRummyDeposit(amount,
							userBean, Integer.parseInt(walletId),
							depositAnyWhere, flexiCardPurchaseBean,
							expiryDate, phone, desKey,
							propKey);
					if (flexiCardPurchaseBean.isSuccess()) {
						session.setAttribute("cashCardList",
								flexiCardPurchaseBean);
						out.write("success");
					} else {
						out.write(flexiCardPurchaseBean.getReturnType());

					}
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

}
