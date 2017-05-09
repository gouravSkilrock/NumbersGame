package com.skilrock.lms.rest.services.bean;

import java.io.Serializable;

public class TPRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userName;
	private String userType;
	private int userId;
	private int userOrgId;
	private String userSession;
	private Object requestData;
	private String serviceCode;
	private String interfaceType;

	public TPRequestBean() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}

	public Object getRequestData() {
		return requestData;
	}

	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public String toString() {
		return "TPRequestBean [userName=" + userName + ", userType=" + userType
				+ ", userId=" + userId + ", userOrgId=" + userOrgId
				+ ", userSession=" + userSession + ", requestData="
				+ requestData + ", serviceCode=" + serviceCode
				+ ", interfaceType=" + interfaceType + "]";
	}

}