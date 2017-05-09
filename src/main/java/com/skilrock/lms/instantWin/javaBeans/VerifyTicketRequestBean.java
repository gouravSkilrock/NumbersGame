package com.skilrock.lms.instantWin.javaBeans;

public class VerifyTicketRequestBean implements IWDataFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ticketNbr;
	private String userType;
	private String userName;
	private String merchantSessionId;
	private String lmsPlayerId;
	private String winType;
	private String domainName = "lms";
	private String currencyCode = "NGN";
	private int merchantKey = 2;
	private String clientType = "print";
	private String lang = "eng";
	private String screenSize = "320x424";
	private String deviceType = "PC";
	private String appType = "retapp";
	private String remarks = "Paid";

	// domainName=lms&currencyCode=USD&merchantKey=2&playerId=1234567&clientType=print&merchantSessionId=123456&ticketNbr=323037049001&lang=eng&screenSize=320x424&userName=gourav

	public String getTicketNbr() {
		return ticketNbr;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMerchantSessionId() {
		return merchantSessionId;
	}

	public void setMerchantSessionId(String merchantSessionId) {
		this.merchantSessionId = merchantSessionId;
	}

	public String getLmsPlayerId() {
		return lmsPlayerId;
	}

	public void setLmsPlayerId(String lmsPlayerId) {
		this.lmsPlayerId = lmsPlayerId;
	}

	public String getWinType() {
		return winType;
	}

	public void setWinType(String winType) {
		this.winType = winType;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public int getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(int merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "VerifyTicketRequestBean [ticketNbr=" + ticketNbr
				+ ", userType=" + userType + ", userName=" + userName
				+ ", merchantSessionId=" + merchantSessionId + ", lmsPlayerId="
				+ lmsPlayerId + ", winType=" + winType + ", domainName="
				+ domainName + ", currencyCode=" + currencyCode
				+ ", merchantKey=" + merchantKey + ", clientType=" + clientType
				+ ", lang=" + lang + ", screenSize=" + screenSize
				+ ", deviceType=" + deviceType + ", appType=" + appType
				+ ", remarks=" + remarks + "]";
	}

}
