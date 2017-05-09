package com.skilrock.lms.beans;

public class OrgDO {

	private String name = "";
	private int orgId;
	private String orgType = "";

	public String getName() {
		return name;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}