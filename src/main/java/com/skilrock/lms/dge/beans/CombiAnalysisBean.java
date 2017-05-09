package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class CombiAnalysisBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int combiCount;
	double totalSale;
	String drawName;
	String combinition;
	private Map<String , Map<Integer , Double>> playerInterestMap;

	public int getCombiCount() {
		return combiCount;
	}
	public void setCombiCount(int combiCount) {
		this.combiCount = combiCount;
	}
	public double getTotalSale() {
		return totalSale;
	}
	public void setTotalSale(double totalSale) {
		this.totalSale = totalSale;
	}
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public String getCombinition() {
		return combinition;
	}
	public void setCombinition(String combinition) {
		this.combinition = combinition;
	}
	public Map<String, Map<Integer, Double>> getPlayerInterestMap() {
		return playerInterestMap;
	}
	public void setPlayerInterestMap(
			Map<String, Map<Integer, Double>> playerInterestMap) {
		this.playerInterestMap = playerInterestMap;
	}
}
