package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class DrawPwtDetailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private String drawDateTime;
	private Map<Integer,Double> totalWinMap;
	private Map<Integer,Double> totalClaimedAtBoMap;
	private String startDate;
	private String endDate;
	private boolean isUnclmAftr7Days;
	
	
	public boolean isUnclmAftr7Days() {
		return isUnclmAftr7Days;
	}
	public void setUnclmAftr7Days(boolean isUnclmAftr7Days) {
		this.isUnclmAftr7Days = isUnclmAftr7Days;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public Map<Integer, Double> getTotalWinMap() {
		return totalWinMap;
	}
	public void setTotalWinMap(Map<Integer, Double> totalWinMap) {
		this.totalWinMap = totalWinMap;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Map<Integer, Double> getTotalClaimedAtBoMap() {
		return totalClaimedAtBoMap;
	}
	public void setTotalClaimedAtBoMap(Map<Integer, Double> totalClaimedAtBoMap) {
		this.totalClaimedAtBoMap = totalClaimedAtBoMap;
	}
	
	
}
