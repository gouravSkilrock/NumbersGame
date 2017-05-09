package com.skilrock.lms.beans;

import java.io.Serializable;

public class MainPriviledgeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	int actionId;
	int privId;
	String privDispName;
	String actionMapping;
	String privOwner;
	String relatedTo;
	String manuDispName;
	String status;

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public int getPrivId() {
		return privId;
	}

	public void setPrivId(int privId) {
		this.privId = privId;
	}

	public String getPrivDispName() {
		return privDispName;
	}

	public void setPrivDispName(String privDispName) {
		this.privDispName = privDispName;
	}

	public String getActionMapping() {
		return actionMapping;
	}

	public void setActionMapping(String actionMapping) {
		this.actionMapping = actionMapping;
	}

	public String getPrivOwner() {
		return privOwner;
	}

	public void setPrivOwner(String privOwner) {
		this.privOwner = privOwner;
	}

	public String getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(String relatedTo) {
		this.relatedTo = relatedTo;
	}

	public String getManuDispName() {
		return manuDispName;
	}

	public void setManuDispName(String manuDispName) {
		this.manuDispName = manuDispName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
