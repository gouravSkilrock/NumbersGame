package com.skilrock.lms.rest.services.bean;

import java.io.Serializable;

public class TPResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int responseCode;
	private String responseMessage;
	private Object responseData;

	public TPResponseBean() {
	}

	public TPResponseBean(int responseCode) {
		this.responseCode = responseCode;
	}

	public TPResponseBean(int responseCode, String responseMessage, Object responseData) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.responseData = responseData;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
}