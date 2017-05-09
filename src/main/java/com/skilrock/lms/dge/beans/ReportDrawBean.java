package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReportDrawBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double claimedAmount;
	private String drawDateTime;
	private String drawDay;
	private String drawFreezeTime;
	// Draw Specific Information
	private int drawId;
	private String drawName;
	private String drawStatus;
	private String performStatus;
	private Map<Double, Double> przClaimedMap;
	
	private List<DrawResultReportBean> drawResultBean;

	private List<ReportTicketBean> repTicketBean;
	private int totalWinningTickets;
	private int totalClaimedTickets;
	private int totalNoTickets;
	private Double totalSaleValue;
	private Double totalWinningAmount;
	private Double unClaimedAmount;
	private String winningResult;	
	private Double avgSalePerRet;
	private String machineResult;
	private int winningResultSum;
	private int machineResultSum;
	
	public String getMachineResult() {
		return machineResult;
	}

	public void setMachineResult(String machineResult) {
		this.machineResult = machineResult;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public String getDrawDay() {
		return drawDay;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public String getPerformStatus() {
		return performStatus;
	}

	public Map<Double, Double> getPrzClaimedMap() {
		return przClaimedMap;
	}

	public List<ReportTicketBean> getRepTicketBean() {
		return repTicketBean;
	}

	public int getTotalClaimedTickets() {
		return totalClaimedTickets;
	}

	public int getTotalNoTickets() {
		return totalNoTickets;
	}

	public Double getTotalSaleValue() {
		return totalSaleValue;
	}

	public Double getTotalWinningAmount() {
		return totalWinningAmount;
	}

	public Double getUnClaimedAmount() {
		return unClaimedAmount;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setPerformStatus(String performStatus) {
		this.performStatus = performStatus;
	}

	public void setPrzClaimedMap(Map<Double, Double> przClaimedMap) {
		this.przClaimedMap = przClaimedMap;
	}


	public void setRepTicketBean(List<ReportTicketBean> repTicketBean) {
		this.repTicketBean = repTicketBean;
	}

	public void setTotalClaimedTickets(int totalClaimedTickets) {
		this.totalClaimedTickets = totalClaimedTickets;
	}

	public void setTotalNoTickets(int totalNoTickets) {
		this.totalNoTickets = totalNoTickets;
	}

	public void setTotalSaleValue(Double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}

	public void setTotalWinningAmount(Double totalWinningAmount) {
		this.totalWinningAmount = totalWinningAmount;
	}

	public void setUnClaimedAmount(Double unClaimedAmount) {
		this.unClaimedAmount = unClaimedAmount;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public int getTotalWinningTickets() {
		return totalWinningTickets;
	}

	public void setTotalWinningTickets(int totalWinningTickets) {
		this.totalWinningTickets = totalWinningTickets;
	}

	public Double getAvgSalePerRet() {
		return avgSalePerRet;
	}

	public void setAvgSalePerRet(Double avgSalePerRet) {
		this.avgSalePerRet = avgSalePerRet;
	}

	public int getMachineResultSum() {
		return machineResultSum;
	}

	public void setMachineResultSum(int machineResultSum) {
		this.machineResultSum = machineResultSum;
	}

	public int getWinningResultSum() {
		return winningResultSum;
	}

	public void setWinningResultSum(int winningResultSum) {
		this.winningResultSum = winningResultSum;
	}

	public List<DrawResultReportBean> getDrawResultBean() {
		return drawResultBean;
	}

	public void setDrawResultBean(List<DrawResultReportBean> drawResultBean) {
		this.drawResultBean = drawResultBean;
	}
	
}
