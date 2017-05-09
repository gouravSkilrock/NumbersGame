package com.skilrock.lms.web.scratchService.customerSpecificReport.beans;

public class TicketByTicketSalePwt {
	private double sale;
	private double winning;
	private double netAmount;
	private String orgName;
	private String gameName;
	private int gameId;
	private int orgId;
	
	public double getSale() {
		return sale;
	}
	public void setSale(double sale) {
		this.sale = sale;
	}
	public double getWinning() {
		return winning;
	}
	public void setWinning(double winning) {
		this.winning = winning;
	}
	public double getNetAmount() {
		netAmount = sale-winning;
		return netAmount;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
}
