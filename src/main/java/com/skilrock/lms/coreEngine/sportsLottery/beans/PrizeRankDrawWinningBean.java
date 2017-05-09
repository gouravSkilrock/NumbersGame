package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;

public class PrizeRankDrawWinningBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int matchId;
	private double prizeValue;
	private int prizeRank;
	private String rankName;
	private double saleValue;
	
	public PrizeRankDrawWinningBean() {
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public double getPrizeValue() {
		return prizeValue;
	}

	public void setPrizeValue(double prizeValue) {
		this.prizeValue = prizeValue;
	}

	public int getPrizeRank() {
		return prizeRank;
	}

	public void setPrizeRank(int prizeRank) {
		this.prizeRank = prizeRank;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public double getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(double saleValue) {
		this.saleValue = saleValue;
	}

	
}