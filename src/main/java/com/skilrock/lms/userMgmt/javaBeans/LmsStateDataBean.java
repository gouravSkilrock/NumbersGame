package com.skilrock.lms.userMgmt.javaBeans;

import java.io.Serializable;

public class LmsStateDataBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stateCode;
	private String stateName;

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public String toString() {
		return "LmsStateDataBean [stateId=" + stateCode + ", stateName="
				+ stateName + "]";
	}
}
