package com.skilrock.lms.web.ola;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CashCardDepositBean;
import com.skilrock.lms.beans.CashCardPurchaseDataBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.ola.OLARummyHelper;

public class CashCardPurchaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private String cardType;
	private String userName;
	private double[] denoType;
	private int[] numberofCards;
	HttpServletRequest request;
	HttpServletResponse response;
	private HttpServletRequest servletRequest;
	static final long serialVersionUID = 1L;
	private String userPhone;
    public static boolean isPinPurchAllow=true;
	public String cashCardPurchase() {
		
		if(!isPinPurchAllow)
			return ERROR;
		int walletId = 2;// Rummy Wallet 
		boolean isPendingData = true;
		ServletContext sc = ServletActionContext.getServletContext();
		String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
		System.out.println("depositAnyWhere" + depositAnyWhere);
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		int   validMonths= Integer.parseInt((String)sc.getAttribute("olaDepositExpiry"));
		String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
		String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
		CashCardPurchaseHelper purchaseHelper = new CashCardPurchaseHelper();
		CashCardDepositBean cashCardDepositBean = new CashCardDepositBean();
		cashCardDepositBean.setPartyId(null);
		ArrayList<CashCardPurchaseDataBean> cashCardList = new ArrayList<CashCardPurchaseDataBean>();

		// here add the condition to check the size of both the arrays and
		// should be same
		if (!(numberofCards.length == denoType.length)) {
			addActionMessage("Wrong Combination of Denomiation Amount and Number of Cards");
			return ERROR;
		}

		double totalAmount = 0.0;
		for (int i = 0; i < numberofCards.length; i++) {

			for (int j = 0; j < numberofCards[i]; j++) {
			
				if (numberofCards[i] == -1 || denoType[i] == -1)
					continue;
				CashCardPurchaseDataBean cashCardPurchaseDataBean = new CashCardPurchaseDataBean();
				cashCardPurchaseDataBean.setAmount(denoType[i]);
				cashCardPurchaseDataBean.setDenomiationType("FIXED");
				totalAmount += denoType[i];
				cashCardList.add(cashCardPurchaseDataBean);

			}
		}

		if ((cashCardList.size() < 1)) {
			addActionMessage("Please Select Card For Purchasing ");
			return ERROR;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, validMonths);// Expiry date
		java.sql.Date expiryDate = new java.sql.Date(cal.getTime().getTime());
		cashCardDepositBean.setCashCardList(cashCardList);
		cashCardDepositBean.setTotalAmount(totalAmount);
		cashCardDepositBean.setPartyId(userName);
		cashCardDepositBean = purchaseHelper.cashCardPurchase(totalAmount,
				userBean, walletId, depositAnyWhere, cashCardDepositBean,
				isPendingData, rootPath, userPhone, expiryDate,desKey,propKey);

		if (cashCardDepositBean.isSuccess()) {
			session.setAttribute("cashCardList", cashCardDepositBean
					.getCashCardList());
			return SUCCESS;
		} else {
			addActionMessage(cashCardDepositBean.getReturnType());
			return ERROR;
		}

	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public double[] getDenoType() {
		return denoType;
	}

	public void setDenoType(double[] denoType) {
		this.denoType = denoType;
	}

	public int[] getNumberofCards() {
		return numberofCards;
	}

	public void setNumberofCards(int[] numberofCards) {
		this.numberofCards = numberofCards;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

}
