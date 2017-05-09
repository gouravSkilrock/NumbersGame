package com.skilrock.lms.coreEngine.userMgmt.javaBeans;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageInboxBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int messageId;
	private int messageTypeId;
	private String messageFor;
	private String messageType;
	private String messageSubject;
	private String messageContent;
	private String messageDate;
	private String expiryDate;
	private String expHr;
	private String expMin;
	private String expSec;
	private Timestamp expiryPeriod;
	private long expiryTimeInSec;
	private boolean isPopup;
	private boolean isMandatory;
	private String userSelection;
	private String isForNewUser;
	private String status;
	private int userId;
	private String creatorUserName;

	private int messageNumber;
	private int totalMessageCount;

	private int newMessageCount;
	private String popupStatus;
	private String mandatoryStatus;

	public MessageInboxBean() {
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(int messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	public String getMessageFor() {
		return messageFor;
	}

	public void setMessageFor(String messageFor) {
		this.messageFor = messageFor;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
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

	public String getExpHr() {
		return expHr;
	}

	public void setExpHr(String expHr) {
		this.expHr = expHr;
	}

	public String getExpMin() {
		return expMin;
	}

	public void setExpMin(String expMin) {
		this.expMin = expMin;
	}

	public String getExpSec() {
		return expSec;
	}

	public void setExpSec(String expSec) {
		this.expSec = expSec;
	}

	public Timestamp getExpiryPeriod() {
		return expiryPeriod;
	}

	public void setExpiryPeriod(Timestamp expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}

	public long getExpiryTimeInSec() {
		return expiryTimeInSec;
	}

	public void setExpiryTimeInSec(long expiryTimeInSec) {
		this.expiryTimeInSec = expiryTimeInSec;
	}

	public boolean getIsPopup() {
		return isPopup;
	}

	public void setIsPopup(boolean isPopup) {
		this.isPopup = isPopup;
	}

	public boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getUserSelection() {
		return userSelection;
	}

	public void setUserSelection(String userSelection) {
		this.userSelection = userSelection;
	}

	public String getIsForNewUser() {
		return isForNewUser;
	}

	public void setIsForNewUser(String isForNewUser) {
		this.isForNewUser = isForNewUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public int getTotalMessageCount() {
		return totalMessageCount;
	}

	public void setTotalMessageCount(int totalMessageCount) {
		this.totalMessageCount = totalMessageCount;
	}

	public int getNewMessageCount() {
		return newMessageCount;
	}

	public void setNewMessageCount(int newMessageCount) {
		this.newMessageCount = newMessageCount;
	}

	public String getPopupStatus() {
		return popupStatus;
	}

	public void setPopupStatus(String popupStatus) {
		this.popupStatus = popupStatus;
	}

	public String getMandatoryStatus() {
		return mandatoryStatus;
	}

	public void setMandatoryStatus(String mandatoryStatus) {
		this.mandatoryStatus = mandatoryStatus;
	}
}