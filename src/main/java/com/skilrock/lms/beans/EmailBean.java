package com.skilrock.lms.beans;

import java.util.List;

public class EmailBean {

	private List<String> attachFilePathList;
	private String emailMsg;
	private boolean emailSendControlFlag;
	private String from;
	private String to;

	public List<String> getAttachFilePathList() {
		return attachFilePathList;
	}

	public String getEmailMsg() {
		return emailMsg;
	}

	public String getFrom() {
		return from;
	}

	public boolean getIsEmailSendControlFlag() {
		return emailSendControlFlag;
	}

	public String getTo() {
		return to;
	}

	public void setAttachFilePathList(List<String> attachFilePathList) {
		this.attachFilePathList = attachFilePathList;
	}

	public void setEmailMsg(String emailMsg) {
		this.emailMsg = emailMsg;
	}

	public void setEmailSendControlFlag(boolean emailSendControlFlag) {
		this.emailSendControlFlag = emailSendControlFlag;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
