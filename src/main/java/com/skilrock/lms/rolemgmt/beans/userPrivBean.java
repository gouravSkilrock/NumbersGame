package com.skilrock.lms.rolemgmt.beans;

public class userPrivBean {
	private int pid;
	private String privRelatedTo;
	private String privTitle;
	private String status;

	public int getPid() {
		return pid;
	}

	public String getPrivRelatedTo() {
		return privRelatedTo;
	}

	public String getPrivTitle() {
		return privTitle;
	}

	public String getStatus() {
		return status;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setPrivRelatedTo(String privRelatedTo) {
		this.privRelatedTo = privRelatedTo;
	}

	public void setPrivTitle(String privTitle) {
		this.privTitle = privTitle;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}