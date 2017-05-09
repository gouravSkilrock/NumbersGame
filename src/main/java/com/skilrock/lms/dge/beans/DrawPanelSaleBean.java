package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class DrawPanelSaleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String drawDateTime;
	private int drawId;
	private boolean isPurg;
	private String purgLastDate;
	private Map<String, Double> panelSaleMap;

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getPurgLastDate() {
		return purgLastDate;
	}

	public void setPurgLastDate(String purgLastDate) {
		this.purgLastDate = purgLastDate;
	}

	public boolean isPurg() {
		return isPurg;
	}

	public void setPurg(boolean isPurg) {
		this.isPurg = isPurg;
	}

	public Map<String, Double> getPanelSaleMap() {
		return panelSaleMap;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setPanelSaleMap(Map<String, Double> panelSaleMap) {
		this.panelSaleMap = panelSaleMap;
	}

}
