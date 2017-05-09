package com.skilrock.lms.beans;

import java.util.List;

public class ServiceBean {
	private int serviceId;
	private String serviceCode;
	private String serviceDisplayName;
	private List<GameBean> gameBeans;

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceDisplayName() {
		return serviceDisplayName;
	}

	public void setServiceDisplayName(String serviceDisplayName) {
		this.serviceDisplayName = serviceDisplayName;
	}

	public List<GameBean> getGameBeans() {
		return gameBeans;
	}

	public void setGameBeans(List<GameBean> gameBeans) {
		this.gameBeans = gameBeans;
	}

	@Override
	public String toString() {
		return "ServiceBean [serviceId=" + serviceId + ", serviceCode="
				+ serviceCode + ", serviceDisplayName=" + serviceDisplayName
				+ ", gameBeans=" + gameBeans + "]";
	}

}
