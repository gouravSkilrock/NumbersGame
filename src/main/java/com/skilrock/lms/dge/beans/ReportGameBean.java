package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReportGameBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int checkPpr;
	private String defaultDrawMode;
	private int gameId;
	private String gameName;
	private String gameNameDev;
	private int gameNo;
	private int jackpotCounter;
	private double jackpotLimit;
	private double operatorProfit;
	private Map<String, Double> priceMap;
	private List<ReportDrawBean> repDrawBean;
	private List<AnalysisReportDrawBean> repAnalysisDrawBean; 
	private double taxPercentage;

	private int ticketExpiryPeriod;

	public int getCheckPpr() {
		return checkPpr;
	}

	public String getDefaultDrawMode() {
		return defaultDrawMode;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNameDev() {
		return gameNameDev;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getJackpotCounter() {
		return jackpotCounter;
	}

	public double getJackpotLimit() {
		return jackpotLimit;
	}

	public double getOperatorProfit() {
		return operatorProfit;
	}

	public Map<String, Double> getPriceMap() {
		return priceMap;
	}

	public List<ReportDrawBean> getRepDrawBean() {
		return repDrawBean;
	}

	public double getTaxPercentage() {
		return taxPercentage;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public void setCheckPpr(int checkPpr) {
		this.checkPpr = checkPpr;
	}

	public void setDefaultDrawMode(String defaultDrawMode) {
		this.defaultDrawMode = defaultDrawMode;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNameDev(String gameNameDev) {
		this.gameNameDev = gameNameDev;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setJackpotCounter(int jackpotCounter) {
		this.jackpotCounter = jackpotCounter;
	}

	public void setJackpotLimit(double jackpotLimit) {
		this.jackpotLimit = jackpotLimit;
	}

	public void setOperatorProfit(double operatorProfit) {
		this.operatorProfit = operatorProfit;
	}

	public void setPriceMap(Map<String, Double> priceMap) {
		this.priceMap = priceMap;
	}

	public void setRepDrawBean(List<ReportDrawBean> repDrawBean) {
		this.repDrawBean = repDrawBean;
	}
	
	

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}
	

	public void setAnalysisRepDrawBean(List<AnalysisReportDrawBean>  repAnalysisDrawBean) {
		this.repAnalysisDrawBean = repAnalysisDrawBean;
	}

	public List<AnalysisReportDrawBean> getRepAnalysisDrawBean() {
		return repAnalysisDrawBean;
	}
		

}
