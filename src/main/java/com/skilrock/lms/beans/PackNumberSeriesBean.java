package com.skilrock.lms.beans;

import java.io.Serializable;

public class PackNumberSeriesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isValid = false;
	private String packNumberFrom;
	private String packNumberTo;
	private String status = null;
	private int totalpack;

	public String getPackNumberFrom() {
		return packNumberFrom;
	}

	public String getPackNumberTo() {
		return packNumberTo;
	}

	public String getStatus() {
		return status;
	}

	public int getTotalpack() {
		return totalpack;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setPackNumberFrom(String packNumberFrom) {
		this.packNumberFrom = packNumberFrom;
	}

	public void setPackNumberTo(String packNumberTo) {
		this.packNumberTo = packNumberTo;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalpack(int totalpack) {
		this.totalpack = totalpack;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
