package com.skilrock.lms.beans;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PriviledgeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int actionId;
	private int privId;
	private String privTitle;
	private String privDisplayName;
	private String actionMapping;
	private int parentPrivId;
	private String status;
	private String privOwner;
	private String relatedTo;
	private String groupName;
	private String isStart;
	private String channel;
	private int privCode;
	private String hidden;
	private String module;

	public PriviledgeBean() {
	}

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

	public String getPrivTitle() {
		return privTitle;
	}

	public void setPrivTitle(String privTitle) {
		this.privTitle = privTitle;
	}

	public String getPrivDisplayName() {
		return privDisplayName;
	}

	public void setPrivDisplayName(String privDisplayName) {
		this.privDisplayName = privDisplayName;
	}

	public String getActionMapping() {
		return actionMapping;
	}

	public void setActionMapping(String actionMapping) {
		this.actionMapping = actionMapping;
	}

	public int getParentPrivId() {
		return parentPrivId;
	}

	public void setParentPrivId(int parentPrivId) {
		this.parentPrivId = parentPrivId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getPrivCode() {
		return privCode;
	}

	public void setPrivCode(int privCode) {
		this.privCode = privCode;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("Action ID", this.actionId)
		.append("Privilege ID", this.privId)
		.append("Privilege Title", this.privTitle)
		.append("Privilege Display Name", this.privDisplayName)
		.append("Action Mapping", this.actionMapping)
		.append("Parent Privilege ID", this.parentPrivId)
		.append("Status", this.status)
		.append("Privilege Owner", this.privOwner)
		.append("Related To", this.relatedTo)
		.append("Group Name", this.groupName)
		.append("Is Start", this.isStart)
		.append("Channel", this.channel)
		.append("Privilege Code", this.privCode)
		.append("Hidden", this.hidden)
		.append("Module", this.module).toString();
	}
}