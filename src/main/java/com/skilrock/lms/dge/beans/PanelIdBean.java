package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class PanelIdBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int betAmtMultiple;
	private boolean isAppReq;
	private boolean isHighLevel;
	private boolean isValid;
	// added by yogesh for BO direct player pwt
	private String message;
	private String messageCode;
	private int panelId;
	private int lineId; // for keno
	private int partyId;
	private String partyName;
	private String pickedData;
	private String status;
	private String verificationStatus;
	private double winningAmt;
	private String playType;
	private int rankId;
	private double promoWinAmt;
	private double govtCommPwt;
	private String betDispName;

	// private boolean isRetPayLimit;

	public String getBetDispName() {
		return betDispName;
	}

	public void setBetDispName(String betDispName) {
		this.betDispName = betDispName;
	}

	public double getGovtCommPwt() {
		return govtCommPwt;
	}

	public void setGovtCommPwt(double govtCommPwt) {
		this.govtCommPwt = govtCommPwt;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public int getBetAmtMultiple() {
		return betAmtMultiple;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public int getPanelId() {
		return panelId;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyName() {
		return partyName;
	}

	public String getPickedData() {
		return pickedData;
	}

	public String getStatus() {
		return status;
	}

	public String getVerificationStatus() {
		return verificationStatus;
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

	public void setBetAmtMultiple(int betAmtMultiple) {
		this.betAmtMultiple = betAmtMultiple;
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

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public void setPickedData(String pickedData) {
		this.pickedData = pickedData;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public int getRankId() {
		return rankId;
	}

	public void setRankId(int rankId) {
		this.rankId = rankId;
	}

	public double getPromoWinAmt() {
		return promoWinAmt;
	}

	public void setPromoWinAmt(double promoWinAmt) {
		this.promoWinAmt = promoWinAmt;
	}
}