package com.skilrock.lms.rest.scratchService.inventoryMgmt.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetDlChallanDetailReqBean {
	
	private int requestId;
	@NotNull(message="Please mention TpUserId")
	@Size(min=1,message="TpUserId should not be empty")
	private String tpUserId;
	@NotNull(message="Please mention Challan Id")
	@Size(min=1,message="Challan Id should not be empty")
	private String dlNumber;
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getTpUserId() {
		return tpUserId;
	}
	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}
	public String getDlNumber() {
		return dlNumber;
	}
	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}

}
