package com.skilrock.lms.api.lmsWrapper.beans;

import java.util.List;

public class LmsWrapperSearchInventoryResponseDataBean {

	private boolean isSuccess;
	private String errorCode;
	private List<LmsWrapperConsNNonConsDetailBean> consNNonConsDataBeanList;
	private int agentOrgId;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public List<LmsWrapperConsNNonConsDetailBean> getConsNNonConsDataBeanList() {
		return consNNonConsDataBeanList;
	}
	public void setConsNNonConsDataBeanList(
			List<LmsWrapperConsNNonConsDetailBean> consNNonConsDataBeanList) {
		this.consNNonConsDataBeanList = consNNonConsDataBeanList;
	}
	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	public int getAgentOrgId() {
		return agentOrgId;
	}
	
	
}
