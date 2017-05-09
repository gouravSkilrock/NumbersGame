package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperDrawIdBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimedTime;
	private String drawDateTime;
	private int drawId;
	private boolean isAppReq;
	private boolean isHighLevel;
	private boolean isValid;
	// added by yogesh for BO direct player pwt
	private String message;
	private String messageCode;

	private List<LmsWrapperPanelIdBean> panelWinList;
	private String status;
	private String tableName;
	private String verificationStatus;
	private String winningAmt;
	private String winResult;

	// private boolean isRetPayLimit;

	public String getClaimedTime() {
		return claimedTime;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public List<LmsWrapperPanelIdBean> getPanelWinList() {
		return panelWinList;
	}

	public String getStatus() {
		return status;
	}

	public String getTableName() {
		return tableName;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public String getWinningAmt() {
		return winningAmt;
	}

	public String getWinResult() {
		return winResult;
	}

	public boolean isAppReq() {
		return isAppReq;
	}

	public boolean isHighLevel() {
		return isHighLevel;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setAppReq(boolean isAppReq) {
		this.isAppReq = isAppReq;
	}

	public void setClaimedTime(String claimedTime) {
		this.claimedTime = claimedTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setHighLevel(boolean isHighLevel) {
		this.isHighLevel = isHighLevel;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setPanelWinList(List<LmsWrapperPanelIdBean> panelWinList) {
		this.panelWinList = panelWinList;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public void setWinningAmt(String winningAmt) {
		this.winningAmt = winningAmt;
	}

	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}

}