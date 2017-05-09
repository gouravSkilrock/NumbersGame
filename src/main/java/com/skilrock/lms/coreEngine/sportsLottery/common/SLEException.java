package com.skilrock.lms.coreEngine.sportsLottery.common;

public class SLEException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private Integer errorCode;
	
	public SLEException() {
		super();
	}
	public SLEException(String message, Exception exp) {
		super(message, exp);
	}
	public SLEException(String ErrorMessage)
	{
		this.errorMessage=ErrorMessage;
	}
	
	public SLEException(int errorCode)
	{
		this.errorCode=errorCode;
	}
	public SLEException(Integer ErrorCode,String ErrorMessage){
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
