package com.skilrock.ola.userMgmt.javaBeans;
import java.io.Serializable;

public class OlaPlayerRegistrationResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isSuccess;
	private String message;
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
