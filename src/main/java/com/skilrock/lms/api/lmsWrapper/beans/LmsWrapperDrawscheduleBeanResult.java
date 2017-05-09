package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;



public class LmsWrapperDrawscheduleBeanResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<LmsWrapperDrawScheduleDataBean> drawScheduleDataBeanList;
	private String status;
	private boolean isSuccess;
	private String errorCode;
	
	
	public ArrayList<LmsWrapperDrawScheduleDataBean> getDrawScheduleDataBeanList() {
		return drawScheduleDataBeanList;
	}
	public void setDrawScheduleDataBeanList(
			ArrayList<LmsWrapperDrawScheduleDataBean> drawScheduleDataBeanList) {
		this.drawScheduleDataBeanList = drawScheduleDataBeanList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
