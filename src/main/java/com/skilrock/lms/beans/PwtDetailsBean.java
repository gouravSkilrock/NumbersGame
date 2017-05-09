package com.skilrock.lms.beans;

public class PwtDetailsBean {
	private String amount;
	private String gameName;
	private int noOfTkt;
	private String playerName;
	private String prize;
	private int srNo;

	public String getAmount() {
		return amount;
	}

	public String getGameName() {
		return gameName;
	}

	public int getNoOfTkt() {
		return noOfTkt;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getPrize() {
		return prize;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setNoOfTkt(int noOfTkt) {
		this.noOfTkt = noOfTkt;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

}
