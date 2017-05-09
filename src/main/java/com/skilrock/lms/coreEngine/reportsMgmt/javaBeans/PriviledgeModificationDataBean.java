package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;


public class PriviledgeModificationDataBean {
	private int privId;
	private String privName;
	private String status;

	public PriviledgeModificationDataBean() {
	}

	public int getPrivId() {
		return privId;
	}

	public void setPrivId(int privId) {
		this.privId = privId;
	}

	public String getPrivName() {
		return privName;
	}

	public void setPrivName(String privName) {
		this.privName = privName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}