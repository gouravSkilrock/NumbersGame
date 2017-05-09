package com.skilrock.lms.common.exception;

public class GenericException extends Exception {

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

	public GenericException() {
		super();
	}

	public GenericException(Exception exp) {
		super(exp);
	}

	public GenericException(String message) {
		super(message);
	}

	public GenericException(String errorCode, Exception exp) {
		this.errorCode=Integer.parseInt(errorCode);
	}

	public GenericException(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public GenericException(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}