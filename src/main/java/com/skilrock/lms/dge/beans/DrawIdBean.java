package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawIdBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimedTime;
	private String drawDateTime;
	private int drawId;
	private int eventId;
	private boolean isAppReq;
	private boolean isHighLevel;
	private boolean isValid;
	// added by yogesh for BO direct player pwt
	private String message;
	private String messageCode;

	private List<PanelIdBean> panelWinList;
	private String status;
	private String tableName;
	private String verificationStatus;
	private String winningAmt;
	private String winResult;
	private String drawname;
	private String drawStatusForticket;
	private String tktStatusForDraw;
	private double govtCommPwt;
	// private boolean isRetPayLimit;
	
	private int rankId;
	
	public double getGovtCommPwt() {
		return govtCommPwt;
	}

	public void setGovtCommPwt(double govtCommPwt) {
		this.govtCommPwt = govtCommPwt;
	}

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

	public List<PanelIdBean> getPanelWinList() {
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

	public void setPanelWinList(List<PanelIdBean> panelWinList) {
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

	public String getDrawname() {
		return drawname;
	}

	public void setDrawname(String drawname) {
		this.drawname = drawname;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDrawStatusForticket() {
		return drawStatusForticket;
	}

	public void setDrawStatusForticket(String drawStatusForticket) {
		this.drawStatusForticket = drawStatusForticket;
	}

	public String getTktStatusForDraw() {
		return tktStatusForDraw;
	}

	public void setTktStatusForDraw(String tktStatusForDraw) {
		this.tktStatusForDraw = tktStatusForDraw;
	}

	public int getRankId() {
		return rankId;
	}

	public void setRankId(int rankId) {
		this.rankId = rankId;
	}
	
	
}