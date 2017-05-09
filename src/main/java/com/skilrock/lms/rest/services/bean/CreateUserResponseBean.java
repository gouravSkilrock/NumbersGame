package com.skilrock.lms.rest.services.bean;

public class CreateUserResponseBean {
	
	private int requestId;
	private int responseCode;
	private String responseMsg;
	private ResponseDataBean requestData;
	
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public ResponseDataBean getRequestData() {
		return requestData;
	}
	public void setRequestData(ResponseDataBean requestData) {
		this.requestData = requestData;
	}
	
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
	

}
