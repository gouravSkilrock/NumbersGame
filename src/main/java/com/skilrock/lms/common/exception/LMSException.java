package com.skilrock.lms.common.exception;

public class LMSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer errorCode;
	private String errorMessage;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LMSException() {
		super();
	}

	public LMSException(Exception exp) {
		super(exp);
	}

	public LMSException(String message) {
		super(message);
	}

	public LMSException(String message, Exception exp) {
		super(message, exp);
	}

	public LMSException(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public LMSException(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}