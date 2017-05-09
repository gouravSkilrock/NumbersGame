package com.skilrock.lms.beans;

public class ScratchPayPWTBean {

	private UserInfoBean userInfoBean;
	private PwtBean pwtBean;
	private TicketBean ticketBean;
	private OrgPwtLimitBean orgPwtLimitBean;
	private boolean isAnonymous;
	private String pendingReqId;

	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}

	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}

	public PwtBean getPwtBean() {
		return pwtBean;
	}

	public void setPwtBean(PwtBean pwtBean) {
		this.pwtBean = pwtBean;
	}

	public TicketBean getTicketBean() {
		return ticketBean;
	}

	public void setTicketBean(TicketBean ticketBean) {
		this.ticketBean = ticketBean;
	}

	public OrgPwtLimitBean getOrgPwtLimitBean() {
		return orgPwtLimitBean;
	}

	public void setOrgPwtLimitBean(OrgPwtLimitBean orgPwtLimitBean) {
		this.orgPwtLimitBean = orgPwtLimitBean;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getPendingReqId() {
		return pendingReqId;
	}

	public void setPendingReqId(String pendingReqId) {
		this.pendingReqId = pendingReqId;
	}

}
