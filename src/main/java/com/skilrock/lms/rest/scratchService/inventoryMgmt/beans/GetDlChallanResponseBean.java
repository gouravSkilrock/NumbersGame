package com.skilrock.lms.rest.scratchService.inventoryMgmt.beans;

import java.util.List;

public class GetDlChallanResponseBean {
	
	private int requestId;
	private int responseCode;
	private String responseMsg;
	private List<GetDlChallanRequestDataBean> requestData;
	
	public List<GetDlChallanRequestDataBean> getRequestData() {
		return requestData;
	}
	public void setRequestData(List<GetDlChallanRequestDataBean> requestData) {
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
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	

}
