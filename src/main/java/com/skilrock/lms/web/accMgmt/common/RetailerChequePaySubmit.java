package com.skilrock.lms.web.accMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.accMgmt.common.RetailerPaymentSubmitHelper;

public class RetailerChequePaySubmit extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankNameHidden[];
	private String chequeAmountHidden[];
	private String chequeDateHidden[];
	private String chequeNumberHidden[];
	private String issuePartyNameHidden[];
	private String orgNameHidden;
	private String orgTypeHidden;
	private HttpServletRequest request;
	private HttpSession session;

	public String[] getBankNameHidden() {
		return bankNameHidden;
	}

	public String[] getChequeAmountHidden() {
		return chequeAmountHidden;
	}

	public String[] getChequeDateHidden() {
		return chequeDateHidden;
	}

	public String[] getChequeNumberHidden() {
		return chequeNumberHidden;
	}

	public String[] getIssuePartyNameHidden() {
		return issuePartyNameHidden;
	}

	public String getOrgNameHidden() {
		return orgNameHidden;
	}

	public String getOrgTypeHidden() {
		return orgTypeHidden;
	}

	public String retailerChequePaymentSubmit() throws LMSException {
		System.out.println(orgTypeHidden + ", " + orgNameHidden + ", "
				+ chequeNumberHidden + ", " + issuePartyNameHidden + ", "
				+ bankNameHidden + ", " + chequeAmountHidden + ", " + ", "
				+ chequeDateHidden);
		int len = chequeNumberHidden.length;

		// check the length of all arrays
		if (len != chequeNumberHidden.length
				|| len != issuePartyNameHidden.length
				|| len != bankNameHidden.length
				|| len != chequeAmountHidden.length
				|| len != chequeDateHidden.length) {
			addActionError("You Are Enter The Wrong Entries. Please Re-Enter The Cheque Entries Again !!");
			return INPUT;
		}

		// verify for duplicate Cheque entries
		for (int i = 0; i < chequeNumberHidden.length; i++) {
			for (int j = i + 1; j < chequeNumberHidden.length; j++) {
				if (chequeNumberHidden[i].trim().equalsIgnoreCase(
						chequeNumberHidden[j].trim())
						&& bankNameHidden[i].trim().equalsIgnoreCase(
								bankNameHidden[j].trim())) {
					addActionError("You Are Enter The Duplicate Cheque Entries. Please Re-Enter The Cheque Entries Again !!");
					return INPUT;
				}
			}
		}

		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		double totalAmount = 0.0;
		for (String element : chequeAmountHidden) {
			totalAmount = totalAmount + Double.parseDouble(element);
		}
		String errMsg = CommonMethods.chkCreditLimitAgt(
				userInfo.getUserOrgId(), totalAmount);
		if (!"TRUE".equals(errMsg)) {
			addActionError(errMsg);
			return INPUT;
		}

		RetailerPaymentSubmitHelper helper = new RetailerPaymentSubmitHelper();
		java.util.Map map = helper.retailerChqPaySubmit(len, userInfo,
				orgTypeHidden, orgNameHidden, chequeNumberHidden,
				issuePartyNameHidden, bankNameHidden, chequeAmountHidden,
				chequeDateHidden, (String) session.getAttribute("ROOT_PATH"));
		if (map.size() > 2) {
			session.setAttribute("totalPay", map.get("totalAmt"));
			session.setAttribute("Receipt_Id", map.get("genId"));
			return SUCCESS;
		} else {
			addActionError("You Are Enter The Wrong Entries. Please Re-Enter The Cheque Entries Again !!");
			return INPUT;
		}

	}

	public void setBankNameHidden(String[] bankNameHidden) {
		this.bankNameHidden = bankNameHidden;
	}

	public void setChequeAmountHidden(String[] chequeAmountHidden) {
		this.chequeAmountHidden = chequeAmountHidden;
	}

	public void setChequeDateHidden(String[] chequeDateHidden) {
		this.chequeDateHidden = chequeDateHidden;
	}

	public void setChequeNumberHidden(String[] chequeNumberHidden) {
		this.chequeNumberHidden = chequeNumberHidden;
	}

	public void setIssuePartyNameHidden(String[] issuePartyNameHidden) {
		this.issuePartyNameHidden = issuePartyNameHidden;
	}

	public void setOrgNameHidden(String orgNameHidden) {
		this.orgNameHidden = orgNameHidden;
	}

	public void setOrgTypeHidden(String orgTypeHidden) {
		this.orgTypeHidden = orgTypeHidden;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

}
