package com.skilrock.lms.coreEngine.scratchService.common;

public class ScratchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private Integer errorCode;
	
	public ScratchException() {
		super();
	}
	public ScratchException(String message, Exception exp) {
		super(message, exp);
	}
	public ScratchException(String ErrorMessage)
	{
		this.errorMessage=ErrorMessage;
	}
	
	public ScratchException(int errorCode)
	{
		this.errorCode=errorCode;
	}
	public ScratchException(Integer ErrorCode,String ErrorMessage){
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
