package com.skilrock.lms.beans;

public class AvailableServiceBean {
	private String channel = "";
	private String interfaceType = "";
	private int mappingId;
	private String privRepTable = "";
	private int serviceId;
	private String serviceName = "";
	private String statusInterface = "";
	private String statusService = "";

	public String getChannel() {
		return channel;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public int getMappingId() {
		return mappingId;
	}

	public String getPrivRepTable() {
		return privRepTable;
	}

	public int getServiceId() {
		return serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getStatusInterface() {
		return statusInterface;
	}

	public String getStatusService() {
		return statusService;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}

	public void setPrivRepTable(String privRepTable) {
		this.privRepTable = privRepTable;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setStatusInterface(String statusInterface) {
		this.statusInterface = statusInterface;
	}

	public void setStatusService(String statusService) {
		this.statusService = statusService;
	}

}
