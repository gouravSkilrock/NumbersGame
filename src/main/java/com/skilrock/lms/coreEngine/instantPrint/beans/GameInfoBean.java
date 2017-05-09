package com.skilrock.lms.coreEngine.instantPrint.beans;

public class GameInfoBean implements Cloneable{
	private int gameId;
	private int gameNo;
	private String gameName;
	private double agentSaleComm;
	private double agentPwtComm;
	private double retSaleComm;
	private double retPwtComm;
	private double govtComm;
	private double vatComm;
	private double ppr;
	private long ticketInScheme;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getAgentSaleComm() {
		return agentSaleComm;
	}

	public void setAgentSaleComm(double agentSaleComm) {
		this.agentSaleComm = agentSaleComm;
	}

	public double getAgentPwtComm() {
		return agentPwtComm;
	}

	public void setAgentPwtComm(double agentPwtComm) {
		this.agentPwtComm = agentPwtComm;
	}

	public double getRetSaleComm() {
		return retSaleComm;
	}

	public void setRetSaleComm(double retSaleComm) {
		this.retSaleComm = retSaleComm;
	}

	public double getRetPwtComm() {
		return retPwtComm;
	}

	public void setRetPwtComm(double retPwtComm) {
		this.retPwtComm = retPwtComm;
	}

	public double getGovtComm() {
		return govtComm;
	}

	public void setGovtComm(double govtComm) {
		this.govtComm = govtComm;
	}

	public double getVatComm() {
		return vatComm;
	}

	public void setVatComm(double vatComm) {
		this.vatComm = vatComm;
	}

	public double getPpr() {
		return ppr;
	}

	public void setPpr(double ppr) {
		this.ppr = ppr;
	}

	public long getTicketInScheme() {
		return ticketInScheme;
	}

	public void setTicketInScheme(long ticketInScheme) {
		this.ticketInScheme = ticketInScheme;
	}
}
