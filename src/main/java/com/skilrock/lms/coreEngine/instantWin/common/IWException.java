package com.skilrock.lms.coreEngine.instantWin.common;

public class IWException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private Integer errorCode;
	
	public IWException() {
		super();
	}
	public IWException(String message, Exception exp) {
		super(message, exp);
	}
	public IWException(String ErrorMessage)
	{
		this.errorMessage=ErrorMessage;
	}
	
	public IWException(int errorCode)
	{
		this.errorCode=errorCode;
	}
	public IWException(Integer ErrorCode,String ErrorMessage){
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
