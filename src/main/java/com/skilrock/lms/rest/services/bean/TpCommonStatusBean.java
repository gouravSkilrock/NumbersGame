package com.skilrock.lms.rest.services.bean;

public class TpCommonStatusBean {
	private boolean status;
	private String message;

	public TpCommonStatusBean() {
	}

	public TpCommonStatusBean(boolean status, String message) {
		this.status = status;
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}