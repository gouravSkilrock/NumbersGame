package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class PanelDAO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int betAmountMultiple;
	private String bonus;
	private String isQuickPick;
	private int noPicked;
	private int panelId;
	private String playerPickedItemsSet;
	private String status;

	public int getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public String getBonus() {
		return bonus;
	}

	public String getIsQuickPick() {
		return isQuickPick;
	}

	public int getNoPicked() {
		return noPicked;
	}

	public int getPanelId() {
		return panelId;
	}

	public String getPlayerPickedItemsSet() {
		return playerPickedItemsSet;
	}

	public String getStatus() {
		return status;
	}

	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public void setIsQuickPick(String isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoPicked(int noPicked) {
		this.noPicked = noPicked;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public void setPlayerPickedItemsSet(String playerPickedItemsSet) {
		this.playerPickedItemsSet = playerPickedItemsSet;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
