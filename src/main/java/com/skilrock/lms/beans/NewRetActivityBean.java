// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 11/15/2010 12:02:59 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RetActivityBean.java

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

public class NewRetActivityBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Double> critMap;
	private String currentVersion;
	private Timestamp drawCancel;
	private Timestamp drawPwt;
	private Timestamp drawSale;
	private Timestamp iwPwt;
	private Timestamp iwSale;
	private int iwDays;
	private String location;
	private Timestamp login;
	private String offlineStatus;
	private String orgStatus;
	private String retName;
	private int retOrgId;
	private String retParentName;
	private int retUserId;
	private Timestamp scratchPwt;
	private Timestamp scratchSale;
	private String terminalId;
	private Timestamp lastHeartBeatTime;
	private String lastConDevice;
	private Timestamp lastOlaDepositTime;
	private Timestamp lastOlaWithdrawalTime;
	private Timestamp lastCSSaleTime;
	private String retAddress;
	private String loginStatus;
	private String retCoordinate;
	private int days;// noOfDays Since Last DG Sale

	private Timestamp slePwt;
	private Timestamp sleSale;
	private int sleDays;// noOfDays Since Last SLE Sale

	private Timestamp vsPwt;
	private Timestamp vsSale;
	private int vsDays;

	public Map<String, Double> getCritMap() {
		return critMap;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public Timestamp getDrawCancel() {
		return drawCancel;
	}

	public Timestamp getDrawPwt() {
		return drawPwt;
	}

	public Timestamp getDrawSale() {
		return drawSale;
	}

	public String getLocation() {
		return location;
	}

	public Timestamp getLogin() {
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

	public Timestamp getScratchPwt() {
		return scratchPwt;
	}

	public Timestamp getScratchSale() {
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

	public void setDrawCancel(Timestamp drawCancel) {
		this.drawCancel = drawCancel;
	}

	public void setDrawPwt(Timestamp drawPwt) {
		this.drawPwt = drawPwt;
	}

	public void setDrawSale(Timestamp drawSale) {
		this.drawSale = drawSale;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setLogin(Timestamp login) {
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

	public void setScratchPwt(Timestamp scratchPwt) {
		this.scratchPwt = scratchPwt;
	}

	public void setScratchSale(Timestamp scratchSale) {
		this.scratchSale = scratchSale;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public Timestamp getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(Timestamp lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public String getLastConDevice() {
		return lastConDevice;
	}

	public void setLastConDevice(String lastConDevice) {
		this.lastConDevice = lastConDevice;
	}

	public Timestamp getLastOlaDepositTime() {
		return lastOlaDepositTime;
	}

	public void setLastOlaDepositTime(Timestamp lastOlaDepositTime) {
		this.lastOlaDepositTime = lastOlaDepositTime;
	}

	public Timestamp getLastOlaWithdrawalTime() {
		return lastOlaWithdrawalTime;
	}

	public void setLastOlaWithdrawalTime(Timestamp lastOlaWithdrawalTime) {
		this.lastOlaWithdrawalTime = lastOlaWithdrawalTime;
	}

	public Timestamp getLastCSSaleTime() {
		return lastCSSaleTime;
	}

	public void setLastCSSaleTime(Timestamp lastCSSaleTime) {
		this.lastCSSaleTime = lastCSSaleTime;
	}

	public String getRetAddress() {
		return retAddress;
	}

	public void setRetAddress(String retAddress) {
		this.retAddress = retAddress;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getRetCoordinate() {
		return retCoordinate;
	}

	public void setRetCoordinate(String retCoordinate) {
		this.retCoordinate = retCoordinate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Timestamp getSlePwt() {
		return slePwt;
	}

	public void setSlePwt(Timestamp slePwt) {
		this.slePwt = slePwt;
	}

	public Timestamp getSleSale() {
		return sleSale;
	}

	public void setSleSale(Timestamp sleSale) {
		this.sleSale = sleSale;
	}

	public int getSleDays() {
		return sleDays;
	}

	public void setSleDays(int sleDays) {
		this.sleDays = sleDays;
	}

	public Timestamp getIwPwt() {
		return iwPwt;
	}

	public void setIwPwt(Timestamp iwPwt) {
		this.iwPwt = iwPwt;
	}

	public Timestamp getIwSale() {
		return iwSale;
	}

	public void setIwSale(Timestamp iwSale) {
		this.iwSale = iwSale;
	}

	public int getIwDays() {
		return iwDays;
	}

	public void setIwDays(int iwDays) {
		this.iwDays = iwDays;
	}

	public Timestamp getVsPwt() {
		return vsPwt;
	}

	public void setVsPwt(Timestamp vsPwt) {
		this.vsPwt = vsPwt;
	}

	public Timestamp getVsSale() {
		return vsSale;
	}

	public void setVsSale(Timestamp vsSale) {
		this.vsSale = vsSale;
	}

	public int getVsDays() {
		return vsDays;
	}

	public void setVsDays(int vsDays) {
		this.vsDays = vsDays;
	}

}