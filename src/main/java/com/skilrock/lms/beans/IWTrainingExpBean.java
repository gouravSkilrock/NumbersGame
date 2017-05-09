package com.skilrock.lms.beans;

import java.util.List;

public class IWTrainingExpBean {
	
	private String responseCode;
	private String responseMsg;
	private List<IWTrainingExpDataBean> dataArray;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public List<IWTrainingExpDataBean> getDataArray() {
		return dataArray;
	}
	public void setDataArray(List<IWTrainingExpDataBean> dataArray) {
		this.dataArray = dataArray;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	

}
