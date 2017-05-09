package com.skilrock.lms.wrapper.javaBeans;

import java.io.Serializable;

public class UserRegistrationRequestBean implements Serializable, WrapperDataFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private String serverLocation;
	private String regType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerLocation() {
		return serverLocation;
	}

	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	@Override
	public String toString() {
		return "UserRegistrationRequestBean [userName=" + userName
				+ ", serverLocation=" + serverLocation + ", regType=" + regType
				+ "]";
	}

}
