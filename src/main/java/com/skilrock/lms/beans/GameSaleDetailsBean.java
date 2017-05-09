package com.skilrock.lms.beans;

public class GameSaleDetailsBean {

	private int gameId;
	private String gameName;
	private double totalSale;
	private double  avgSalePerTerminal;
	public double getAvgSalePerTerminal() {
		return avgSalePerTerminal;
	}
	public void setAvgSalePerTerminal(double avgSalePerTerminal) {
		this.avgSalePerTerminal = avgSalePerTerminal;
	}
	public int getGameId() {
		return gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public double getTotalSale() {
		return totalSale;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public void setTotalSale(double totalSale) {
		this.totalSale = totalSale;
	}
	
	
}
