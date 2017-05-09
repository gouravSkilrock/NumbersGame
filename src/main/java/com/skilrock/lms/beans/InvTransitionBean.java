package com.skilrock.lms.beans;

import java.io.Serializable;

public class InvTransitionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromOwner;
	private String fromOwnerName;
	private boolean isAgentToBO;
	private boolean isAgentToRetailer;
	private boolean isBOToAgent;

	private boolean isRetailerToAgent;
	private String toOwner;
	private String toOwnerName;
	private String txTime;

	public InvTransitionBean(String fromOwnerName, String toOwnerName,
			String fromOwner, String toOwner, String txTime) {
		super();
		this.fromOwnerName = fromOwnerName;
		this.toOwnerName = toOwnerName;
		this.fromOwner = fromOwner;
		this.toOwner = toOwner;
		this.txTime = txTime;
	}

	public String getFromOwner() {
		return fromOwner;
	}

	public String getFromOwnerName() {
		return fromOwnerName;
	}

	public boolean getIsAgentToBO() {
		return isAgentToBO;
	}

	public boolean getIsAgentToRetailer() {
		return isAgentToRetailer;
	}

	public boolean getIsBOToAgent() {
		return isBOToAgent;
	}

	public boolean getIsRetailerToAgent() {
		return isRetailerToAgent;
	}

	public String getToOwner() {
		return toOwner;
	}

	public String getToOwnerName() {
		return toOwnerName;
	}

	public String getTxTime() {
		return txTime;
	}

	public void setAgentToBO(boolean isAgentToBO) {
		this.isAgentToBO = isAgentToBO;
	}

	public void setAgentToRetailer(boolean isAgentToRetailer) {
		this.isAgentToRetailer = isAgentToRetailer;
	}

	public void setBOToAgent(boolean isBOToAgent) {
		this.isBOToAgent = isBOToAgent;
	}

	public void setFromOwner(String fromOwner) {
		this.fromOwner = fromOwner;
	}

	public void setFromOwnerName(String fromOwnerName) {
		this.fromOwnerName = fromOwnerName;
	}

	public void setRetailerToAgent(boolean isRetailerToAgent) {
		this.isRetailerToAgent = isRetailerToAgent;
	}

	public void setToOwner(String toOwner) {
		this.toOwner = toOwner;
	}

	public void setToOwnerName(String toOwnerName) {
		this.toOwnerName = toOwnerName;
	}

	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("From Owner::" + fromOwner);
		sb.append(" To Owner::" + toOwner);
		sb.append(" From Owner Id::" + fromOwnerName);
		sb.append(" To Owner Id::" + toOwnerName);
		sb.append(" bo-agt:" + isBOToAgent + " agt-bo:" + isAgentToBO
				+ " agt-ret:" + isAgentToRetailer + " ret-agt:"
				+ isRetailerToAgent);
		sb.append(" txTime:" + txTime);
		return sb.toString();
	}

}
