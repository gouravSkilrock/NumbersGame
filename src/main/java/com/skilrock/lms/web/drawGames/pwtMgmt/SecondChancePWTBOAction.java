package com.skilrock.lms.web.drawGames.pwtMgmt;

import com.skilrock.lms.common.BaseAction;

public class SecondChancePWTBOAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public SecondChancePWTBOAction() {
		super(SecondChancePWTBOAction.class);
	}

	private String orgType;
	private long ticketNumber;
	private long verificationCode;

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public long getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public long getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(long verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String boSecondChancePwtMenu() {
		return SUCCESS;
	}

	public String boSecondChancePwtVerifyTicket() {
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}