package com.skilrock.lms.beans;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class holds the details sent from the service layer to the presentation
 * layer.
 * 
 */
public class ServiceResponse implements Serializable {
	

	static Log logger = LogFactory.getLog(ServiceResponse.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private String errorMessage;

	private Boolean isRaffleSuccess;
	// Implies whether the service operation
	// was successful or not.
	private Boolean isSuccess;

	// contains the data that needs to sent
	// the presentation layer.
	private Object responseData;

	public Boolean getIsRaffleSuccess() {
		return isRaffleSuccess;
	}

	/**
	 * 
	 * 
	 * @return the error code and error message
	 */
	
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @return the isSuccess
	 */
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * @return the responseData
	 */
	public Object getResponseData() {
		return responseData;
	}

	public void setIsRaffleSuccess(Boolean isRaffleSuccess) {
		this.isRaffleSuccess = isRaffleSuccess;
	}

	/**
	 * @param isSuccess
	 *            the isSuccess to set
	 */
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * @param responseData
	 *            the responseData to set
	 */
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

}
