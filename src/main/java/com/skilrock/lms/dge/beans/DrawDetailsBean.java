package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class DrawDetailsBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private String drawName;
	private Timestamp drawDateTime;
	private String purchaseTableName;
	private String winningResult;
	private String machineResult;

	public String getPurchaseTableName() {
		return purchaseTableName;
	}

	public void setPurchaseTableName(String purchaseTableName) {
		this.purchaseTableName = purchaseTableName;
	}

	public Timestamp getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(Timestamp drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public String getMachineResult() {
		return machineResult;
	}

	public void setMachineResult(String machineResult) {
		this.machineResult = machineResult;
	}
}
