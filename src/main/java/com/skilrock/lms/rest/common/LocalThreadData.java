package com.skilrock.lms.rest.common;

public class LocalThreadData {

	private long auditId;
	private long auditTime;
	private int merchantId;
	private String responseData;
	private long activityId;
	public long getAuditId() {
		return auditId;
	}
	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}
	public long getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(long auditTime) {
		this.auditTime = auditTime;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	public long getActivityId() {
		return activityId;
	}
	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}
	
	
}
