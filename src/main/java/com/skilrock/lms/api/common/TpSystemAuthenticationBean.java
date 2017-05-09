package com.skilrock.lms.api.common;

import java.io.Serializable;

public class TpSystemAuthenticationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int tpSystemId;
	private String systemIp;
	private String systemUserName;
	private String systemPassword;
	public int getTpSystemId() {
		return tpSystemId;
	}
	public void setTpSystemId(int tpSystemId) {
		this.tpSystemId = tpSystemId;
	}
	public String getSystemIp() {
		return systemIp;
	}
	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemPassword() {
		return systemPassword;
	}
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
	
	
}
