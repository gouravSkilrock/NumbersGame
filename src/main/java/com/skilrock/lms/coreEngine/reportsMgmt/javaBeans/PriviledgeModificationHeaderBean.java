package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;

public class PriviledgeModificationHeaderBean {
	private String doneByUser;
	private String doneByIP;
	private String changeTime;

	public PriviledgeModificationHeaderBean() {
	}

	public String getDoneByUser() {
		return doneByUser;
	}

	public void setDoneByUser(String doneByUser) {
		this.doneByUser = doneByUser;
	}

	public String getDoneByIP() {
		return doneByIP;
	}

	public void setDoneByIP(String doneByIP) {
		this.doneByIP = doneByIP;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
}