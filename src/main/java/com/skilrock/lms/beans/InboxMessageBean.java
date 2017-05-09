package com.skilrock.lms.beans;

import java.io.Serializable;

public class InboxMessageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int messageId;
	private String messageSubject;
	private String messageBody;
	private String messageDate;
	private String expiryDate;
	private long expiryPeriod;
	private String messageType;
	private String isPopup;
	private String isMandatory;
	private int creatorUserId;
	private String creatorUserName;
	private String userType;
	private String interfaceType;
	private String status;

	public InboxMessageBean() {
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public long getExpiryPeriod() {
		return expiryPeriod;
	}

	public void setExpiryPeriod(long expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getIsPopup() {
		return isPopup;
	}

	public void setIsPopup(String isPopup) {
		this.isPopup = isPopup;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}