package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperVerifyBeforeShiftRes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private List<LmsWrapperVerifyBeforeShiftBean> orgList;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public List<LmsWrapperVerifyBeforeShiftBean> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<LmsWrapperVerifyBeforeShiftBean> orgList) {
		this.orgList = orgList;
	}

}
