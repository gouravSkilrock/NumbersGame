package com.skilrock.lms.rest.services.bean;

/**
 * @author stpl
 * 
 */
public class TpEBetDataBean {

	private int requestId;
	private int userId;
	private int organizationId;
	private int modelId;
	private int DeviceMapId;
	private String tokenId;
	private String requestData;
	private String deviceId;
	private String mobileNumber;
	private String status;
	private String deviceCode;
	private String saleType;
	private String ebetRequestDatetime;
	private String ebetExpiryDatetime;
	private String processedDatetime;
	private String userName;

	public int getDeviceMapId() {
		return DeviceMapId;
	}

	public void setDeviceMapId(int deviceMapId) {
		DeviceMapId = deviceMapId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public int getUserId() {
		return userId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public String getRequestData() {
		return requestData;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getStatus() {
		return status;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public String getSaleType() {
		return saleType;
	}

	public int getModelId() {
		return modelId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEbetRequestDatetime() {
		return ebetRequestDatetime;
	}

	public void setEbetRequestDatetime(String ebetRequestDatetime) {
		this.ebetRequestDatetime = ebetRequestDatetime;
	}

	public String getEbetExpiryDatetime() {
		return ebetExpiryDatetime;
	}

	public void setEbetExpiryDatetime(String ebetExpiryDatetime) {
		this.ebetExpiryDatetime = ebetExpiryDatetime;
	}

	public String getProcessedDatetime() {
		return processedDatetime;
	}

	public void setProcessedDatetime(String processedDatetime) {
		this.processedDatetime = processedDatetime;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
