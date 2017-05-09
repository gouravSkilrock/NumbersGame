package com.skilrock.lms.beans;

public class GovCommBean {

	private long taskId;
	private String endDate;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String govAmount;
	private double govPwtAmount;
	private double saleAmount;
	private double saleRetAmount;
	private String startDate;

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getEndDate() {
		return endDate;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getGovAmount() {
		return govAmount;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public double getSaleRetAmount() {
		return saleRetAmount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGovAmount(String govAmount) {
		this.govAmount = govAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public void setSaleRetAmount(double saleRetAmount) {
		this.saleRetAmount = saleRetAmount;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public double getGovPwtAmount() {
		return govPwtAmount;
	}

	public void setGovPwtAmount(double govPwtAmount) {
		this.govPwtAmount = govPwtAmount;
	}
}