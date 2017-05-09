package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperRetailerListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LmsWrapperRetailerInfoBean> retailerInfoBean;
	private boolean isSuccess;
	private String errorCode;
	
	
	
	public List<LmsWrapperRetailerInfoBean> getRetailerInfoBean() {
		return retailerInfoBean;
	}
	public void setRetailerInfoBean(
			List<LmsWrapperRetailerInfoBean> retailerInfoBean) {
		this.retailerInfoBean = retailerInfoBean;
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
