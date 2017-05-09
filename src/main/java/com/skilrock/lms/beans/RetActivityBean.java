// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 11/15/2010 12:02:59 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RetActivityBean.java

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.Map;

public class RetActivityBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Double> critMap;
	private String currentVersion;
	private long drawCancel;
	private long drawPwt;
	private long drawSale;
	private String location;
	private long login;
	private String offlineStatus;
	private String orgStatus;
	private String retName;
	private int retOrgId;
	private String retParentName;
	private int retUserId;
	private long scratchPwt;
	private long scratchSale;
	private String terminalId;

	public Map<String, Double> getCritMap() {
		return critMap;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public long getDrawCancel() {
		return drawCancel;
	}

	public long getDrawPwt() {
		return drawPwt;
	}

	public long getDrawSale() {
		return drawSale;
	}

	public String getLocation() {
		return location;
	}

	public long getLogin() {
		return login;
	}

	public String getOfflineStatus() {
		return offlineStatus;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public String getRetName() {
		return retName;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getRetParentName() {
		return retParentName;
	}

	public int getRetUserId() {
		return retUserId;
	}

	public long getScratchPwt() {
		return scratchPwt;
	}

	public long getScratchSale() {
		return scratchSale;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setCritMap(Map<String, Double> critMap) {
		this.critMap = critMap;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public void setDrawCancel(long drawCancel) {
		this.drawCancel = drawCancel;
	}

	public void setDrawPwt(long drawPwt) {
		this.drawPwt = drawPwt;
	}

	public void setDrawSale(long drawSale) {
		this.drawSale = drawSale;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setLogin(long login) {
		this.login = login;
	}

	public void setOfflineStatus(String offlineStatus) {
		this.offlineStatus = offlineStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setRetParentName(String retParentName) {
		this.retParentName = retParentName;
	}

	public void setRetUserId(int retUserId) {
		this.retUserId = retUserId;
	}

	public void setScratchPwt(long scratchPwt) {
		this.scratchPwt = scratchPwt;
	}

	public void setScratchSale(long scratchSale) {
		this.scratchSale = scratchSale;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

}