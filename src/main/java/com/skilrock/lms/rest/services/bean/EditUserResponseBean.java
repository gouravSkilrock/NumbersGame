package com.skilrock.lms.rest.services.bean;

public class EditUserResponseBean {
	
	private int requestId;
	private int responseCode;
	private String  tpUserId;
	private String responseData;
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getTpUserId() {
		return tpUserId;
	}
	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

}
