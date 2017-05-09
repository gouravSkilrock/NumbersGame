package com.skilrock.lms.coreEngine.virtualSport.common;

public class VSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private Integer errorCode;
	
	public VSException() {
		super();
	}
	public VSException(String message, Exception exp) {
		super(message, exp);
	}
	public VSException(String ErrorMessage)
	{
		this.errorMessage=ErrorMessage;
	}
	
	public VSException(int errorCode)
	{
		this.errorCode=errorCode;
	}
	public VSException(Integer ErrorCode,String ErrorMessage){
		this.errorMessage = ErrorMessage;
		this.errorCode = ErrorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
