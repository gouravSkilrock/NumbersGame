package com.skilrock.lms.beans;

import java.util.List;

import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;

public class AuditTrailRequestBean implements SLEDataFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int merchantId;
	private int userId;
	private String startTime;
	private String endTime;
	private List<AuditTrailBean> auditTrailBeans = null;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<AuditTrailBean> getAuditTrailBeans() {
		return auditTrailBeans;
	}

	public void setAuditTrailBeans(List<AuditTrailBean> auditTrailBeans) {
		this.auditTrailBeans = auditTrailBeans;
	}

	@Override
	public String toString() {
		return "AuditTrailRequestBean [merchantId=" + merchantId + ", userId="
				+ userId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", auditTrailBeans=" + auditTrailBeans + "]";
	}

}
