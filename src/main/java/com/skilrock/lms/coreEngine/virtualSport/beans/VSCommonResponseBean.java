package com.skilrock.lms.coreEngine.virtualSport.beans;

public class VSCommonResponseBean {
	private String result;
	private String sessionToken;
	private String targetId;
	private String newEntityId;
	private String newEntityName;
	private String newhardWareId;
	private String newPinHash;
	private String sourceId;
	private String targetSection;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getNewEntityId() {
		return newEntityId;
	}

	public void setNewEntityId(String newEntityId) {
		this.newEntityId = newEntityId;
	}

	public String getNewEntityName() {
		return newEntityName;
	}

	public void setNewEntityName(String newEntityName) {
		this.newEntityName = newEntityName;
	}

	public String getNewhardWareId() {
		return newhardWareId;
	}

	public void setNewhardWareId(String newhardWareId) {
		this.newhardWareId = newhardWareId;
	}

	public String getNewPinHash() {
		return newPinHash;
	}

	public void setNewPinHash(String newPinHash) {
		this.newPinHash = newPinHash;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetSection() {
		return targetSection;
	}

	public void setTargetSection(String targetSection) {
		this.targetSection = targetSection;
	}

	@Override
	public String toString() {
		return "VSCommonResponseBean [newEntityId=" + newEntityId
				+ ", newEntityName=" + newEntityName + ", newPinHash="
				+ newPinHash + ", newhardWareId=" + newhardWareId + ", result="
				+ result + ", sessionToken=" + sessionToken + ", sourceId="
				+ sourceId + ", targetId=" + targetId + ", targetSection="
				+ targetSection + "]";
	}

}