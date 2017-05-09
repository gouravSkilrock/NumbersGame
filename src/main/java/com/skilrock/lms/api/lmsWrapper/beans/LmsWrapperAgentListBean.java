package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperAgentListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AgentInfoBean> agentInfoBean;
	private boolean isSuccess;
	private String errorCode;
	
	
	public List<AgentInfoBean> getAgentInfoBean() {
		return agentInfoBean;
	}
	public void setAgentInfoBean(List<AgentInfoBean> agentInfoBean) {
		this.agentInfoBean = agentInfoBean;
	}
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
	

}
