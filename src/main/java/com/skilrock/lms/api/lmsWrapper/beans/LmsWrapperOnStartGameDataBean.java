package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LmsWrapperOnStartGameDataBean implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isSuccess;
	private String errorCode;
	private String status;
	private LmsWrapperGameDataBean gameDataBean ;
	private LinkedHashMap<String, LmsWrapperGameDataBean> returnGameDataBean;
	

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LinkedHashMap<String, LmsWrapperGameDataBean> getReturnGameDataBean() {
		return returnGameDataBean;
	}

	public void setReturnGameDataBean(
			LinkedHashMap<String, LmsWrapperGameDataBean> returnGameDataBean) {
		this.returnGameDataBean = returnGameDataBean;
	}

	
	public LmsWrapperGameDataBean getGameDataBean() {
		return gameDataBean;
	}

	public void setGameDataBean(LmsWrapperGameDataBean gameDataBean) {
		this.gameDataBean = gameDataBean;
	}

	

	

}
