package com.skilrock.lms.beans;

import java.sql.Timestamp;

public class MessageDetailsBean {
	private int messageId;
	private Timestamp date;
	private int creatorUserId;
	private String messageText;
	private String status;
	private String editable;
	private String messageFor;
	private String messageLocation;
	private String activity;

	public MessageDetailsBean()
	{
	}

	public MessageDetailsBean(int messageId) {
		this.messageId = messageId;
	}

	public MessageDetailsBean(int messageId, Timestamp date, int creatorUserId,
			String messageText, String status, String editable,
			String messageFor, String messageLocation, String activity) {
		this.messageId = messageId;
		this.date = date;
		this.creatorUserId = creatorUserId;
		this.messageText = messageText;
		this.status = status;
		this.editable = editable;
		this.messageFor = messageFor;
		this.messageLocation = messageLocation;
		this.activity = activity;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getMessageFor() {
		return messageFor;
	}

	public void setMessageFor(String messageFor) {
		this.messageFor = messageFor;
	}

	public String getMessageLocation() {
		return messageLocation;
	}

	public void setMessageLocation(String messageLocation) {
		this.messageLocation = messageLocation;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Override
	public boolean equals(Object object)
	{
		MessageDetailsBean bean =(MessageDetailsBean) object;
		return bean.messageId == this.messageId;
	}

	@Override
	public String toString() {
		return "MessageDetailsBean [activity=" + activity + ", creatorUserId="
				+ creatorUserId + ", date=" + date + ", editable=" + editable
				+ ", messageFor=" + messageFor + ", messageId=" + messageId
				+ ", messageLocation=" + messageLocation + ", messageText="
				+ messageText + ", status=" + status + "]";
	}
}