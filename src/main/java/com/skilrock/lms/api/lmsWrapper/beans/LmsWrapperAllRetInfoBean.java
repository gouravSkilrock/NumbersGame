package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class LmsWrapperAllRetInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private int errorCode;
	private  ArrayList<Integer> allRetUserId;
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public ArrayList<Integer> getAllRetUserId() {
		return allRetUserId;
	}
	public void setAllRetUserId(ArrayList<Integer> allRetUserId) {
		this.allRetUserId = allRetUserId;
	}
	
	
}
