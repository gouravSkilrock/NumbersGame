package com.skilrock.lms.dge.beans;


public class DrawGameMtnDataBean {
	
	private int gameId;
	private String drawName;
	private String drawDateTime;
	private String winningResult;
	private String drawStatus;
	private String drawFreezeTime;
	public String getWinningResult() {
		return winningResult;
	}
	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}
	private double airtimeSale;
	private double dyaSale;
	private double totalSale;
	private double airtimeDoneWinning;
	private double airtimePendingWinning;
	private double dyaDoneWinning;
	private double dyaPendingWinning;
	private double totalDoneWinning;
	private double totalPendingWinning;
	private double totalWinning;
	private double winlotDoneWinning;
	private double winlotPendingWinning;
	
	public double getWinlotPendingWinning() {
		return winlotPendingWinning;
	}
	public void setWinlotPendingWinning(double winlotPendingWinning) {
		this.winlotPendingWinning = winlotPendingWinning;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public double getAirtimeSale() {
		return airtimeSale;
	}
	public void setAirtimeSale(double airtimeSale) {
		this.airtimeSale = airtimeSale;
	}
	public double getDyaSale() {
		return dyaSale;
	}
	public void setDyaSale(double dyaSale) {
		this.dyaSale = dyaSale;
	}
	public double getTotalSale() {
		return totalSale;
	}
	public void setTotalSale(double totalSale) {
		this.totalSale = totalSale;
	}
	public double getAirtimeDoneWinning() {
		return airtimeDoneWinning;
	}
	public void setAirtimeDoneWinning(double airtimeDoneWinning) {
		this.airtimeDoneWinning = airtimeDoneWinning;
	}
	public double getAirtimePendingWinning() {
		return airtimePendingWinning;
	}
	public void setAirtimePendingWinning(double airtimePendingWinning) {
		this.airtimePendingWinning = airtimePendingWinning;
	}
	public double getDyaDoneWinning() {
		return dyaDoneWinning;
	}
	public void setDyaDoneWinning(double dyaDoneWinning) {
		this.dyaDoneWinning = dyaDoneWinning;
	}
	public double getDyaPendingWinning() {
		return dyaPendingWinning;
	}
	public void setDyaPendingWinning(double dyaPendingWinning) {
		this.dyaPendingWinning = dyaPendingWinning;
	}
	public double getTotalDoneWinning() {
		return totalDoneWinning;
	}
	public void setTotalDoneWinning(double totalDoneWinning) {
		this.totalDoneWinning = totalDoneWinning;
	}
	public double getTotalPendingWinning() {
		return totalPendingWinning;
	}
	public void setTotalPendingWinning(double totalPendingWinning) {
		this.totalPendingWinning = totalPendingWinning;
	}
	public double getTotalWinning() {
		return totalWinning;
	}
	public void setTotalWinning(double totalWinning) {
		this.totalWinning = totalWinning;
	}
	public double getWinlotDoneWinning() {
		return winlotDoneWinning;
	}
	public void setWinlotDoneWinning(double winlotDoneWinning) {
		this.winlotDoneWinning = winlotDoneWinning;
	}

}
