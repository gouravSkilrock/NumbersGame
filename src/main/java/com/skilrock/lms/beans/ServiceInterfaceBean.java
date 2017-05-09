package com.skilrock.lms.beans;

public class ServiceInterfaceBean {

	private String channel;
	private String status;
	private String tier_id;
	private String tier_interface;

	public String getChannel() {
		return channel;
	}

	public String getStatus() {
		return status;
	}

	public String getTier_id() {
		return tier_id;
	}

	public String getTier_interface() {
		return tier_interface;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTier_id(String tier_id) {
		this.tier_id = tier_id;
	}

	public void setTier_interface(String tier_interface) {
		this.tier_interface = tier_interface;
	}

}
