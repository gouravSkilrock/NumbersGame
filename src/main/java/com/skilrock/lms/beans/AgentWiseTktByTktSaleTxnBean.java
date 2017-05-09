package com.skilrock.lms.beans;

public class AgentWiseTktByTktSaleTxnBean {
	private double scratchGameWiseWinning;
	private double scratchGameWiseSale;
	private String game_name;
    private String retailerName;
   
	
    public double getScratchGameWiseWinning() {
		return scratchGameWiseWinning;
	}
	public void setScratchGameWiseWinning(double scratchGameWiseWinning) {
		this.scratchGameWiseWinning = scratchGameWiseWinning;
	}
	public double getScratchGameWiseSale() {
		return scratchGameWiseSale;
	}
	public void setScratchGameWiseSale(double scratchGameWiseSale) {
		this.scratchGameWiseSale = scratchGameWiseSale;
	}
	public String getGame_name() {
		return game_name;
	}
	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}
	public String getRetailerName() {
		return retailerName;
	}
	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}
	
}
