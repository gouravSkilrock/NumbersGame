package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.List;

public class GameMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private int gameNo;
	private int merchantId;
	private String gameDevName;
	private String gameDispName;
	private double maxTicketAmt;
	private double thersholdTickerAmt;
	private int minBoardCount;
	private int maxBoardCount;
	private int displayOrder;
	private String gameStatus;
	private List<GameTypeMasterBean> gameTypeMasterList;

	public GameMasterBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public double getMaxTicketAmt() {
		return maxTicketAmt;
	}

	public void setMaxTicketAmt(double maxTicketAmt) {
		this.maxTicketAmt = maxTicketAmt;
	}

	public double getThersholdTickerAmt() {
		return thersholdTickerAmt;
	}

	public void setThersholdTickerAmt(double thersholdTickerAmt) {
		this.thersholdTickerAmt = thersholdTickerAmt;
	}

	public int getMinBoardCount() {
		return minBoardCount;
	}

	public void setMinBoardCount(int minBoardCount) {
		this.minBoardCount = minBoardCount;
	}

	public int getMaxBoardCount() {
		return maxBoardCount;
	}

	public void setMaxBoardCount(int maxBoardCount) {
		this.maxBoardCount = maxBoardCount;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public List<GameTypeMasterBean> getGameTypeMasterList() {
		return gameTypeMasterList;
	}

	public void setGameTypeMasterList(List<GameTypeMasterBean> gameTypeMasterList) {
		this.gameTypeMasterList = gameTypeMasterList;
	}

	@Override
	public String toString() {
		return "GameMasterBean [displayOrder=" + displayOrder
				+ ", gameDevName=" + gameDevName + ", gameDispName="
				+ gameDispName + ", gameId=" + gameId + ", gameNo=" + gameNo
				+ ", gameStatus=" + gameStatus + ", gameTypeMasterList="
				+ gameTypeMasterList + ", maxBoardCount=" + maxBoardCount
				+ ", maxTicketAmt=" + maxTicketAmt + ", merchantId="
				+ merchantId + ", minBoardCount=" + minBoardCount
				+ ", thersholdTickerAmt=" + thersholdTickerAmt + "]";
	}
}