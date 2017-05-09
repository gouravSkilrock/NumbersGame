package com.skilrock.lms.rest.services.bean;

import java.io.Serializable;
import java.util.List;

import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawTicketDataBean;
import com.skilrock.lms.dge.beans.PlayerBean;

public class TPPwtRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pwtType;
	private int gameId;
	private int gameTypeId;
	private String ticketNumber;
	private String txnIdIw;
	private List<DrawTicketDataBean> drawDataList;
	private double totalAmount;
	private String remarks;
	private PlayerBean playerBean;
	private double taxAmt;
	private double netAmt;
	private int playerId;

	public TPPwtRequestBean() {
	}

	public String getPwtType() {
		return pwtType;
	}

	public void setPwtType(String pwtType) {
		this.pwtType = pwtType;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getTxnIdIw() {
		return txnIdIw;
	}

	public void setTxnIdIw(String txnIdIw) {
		this.txnIdIw = txnIdIw;
	}

	public List<DrawTicketDataBean> getDrawDataList() {
		return drawDataList;
	}

	public void setDrawDataList(List<DrawTicketDataBean> drawDataList) {
		this.drawDataList = drawDataList;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PlayerBean getPlayerBean() {
		return playerBean;
	}

	public void setPlayerBean(PlayerBean playerBean) {
		this.playerBean = playerBean;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "TPPwtRequestBean [pwtType=" + pwtType + ", gameId=" + gameId
				+ ", gameTypeId=" + gameTypeId + ", ticketNumber="
				+ ticketNumber + ", txnIdIw=" + txnIdIw + ", drawDataList="
				+ drawDataList + ", totalAmount=" + totalAmount + ", remarks="
				+ remarks + ", playerBean=" + playerBean + ", taxAmt=" + taxAmt
				+ ", netAmt=" + netAmt + ", playerId=" + playerId + "]";
	}

}