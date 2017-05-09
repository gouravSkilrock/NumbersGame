package com.skilrock.lms.beans;

import java.util.List;
import java.util.Map;

public class ServiceDataBean {

	private List interfaceList;
	private Map<String, String> serviceDataMap;
	private List tierList;

	public List getInterfaceList() {
		return interfaceList;
	}

	public Map<String, String> getServiceDataMap() {
		return serviceDataMap;
	}

	public List getTierList() {
		return tierList;
	}

	public void setInterfaceList(List interfaceList) {
		this.interfaceList = interfaceList;
	}

	public void setServiceDataMap(Map<String, String> serviceDataMap) {
		this.serviceDataMap = serviceDataMap;
	}

	public void setTierList(List tierList) {
		this.tierList = tierList;
	}
}
