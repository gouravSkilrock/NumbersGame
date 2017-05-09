package com.skilrock.lms.dge.beans;

import java.io.Serializable;

/**
 * 
 * @author Arun Tanwar
 * 
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2010     Arun Tanwar    CR#L0375: blah blah blah... 
 * </pre>
 */
public class ManualWinningBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int[] cardType;
	int[] drawIds;
	String drawType[];
	String gameNumber;
	String[] status;
	String updateTime;
	int userId;
	Integer[] winningNumbers;
	Integer[] machineNumbers;
	int winNumSize;
	int gameId;

	public int[] getCardType() {
		return cardType;
	}

	public int[] getDrawIds() {
		return drawIds;
	}

	public String[] getDrawType() {
		return drawType;
	}

	public String getGameNumber() {
		return gameNumber;
	}

	public String[] getStatus() {
		return status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public int getUserId() {
		return userId;
	}

	public Integer[] getWinningNumbers() {
		return winningNumbers;
	}

	public int getWinNumSize() {
		return winNumSize;
	}

	public void setCardType(int[] cardType) {
		this.cardType = cardType;
	}

	public void setDrawIds(int[] drawIds) {
		this.drawIds = drawIds;
	}

	public void setDrawType(String[] drawType) {
		this.drawType = drawType;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setWinningNumbers(Integer[] winningNumbers) {
		this.winningNumbers = winningNumbers;
	}

	public void setWinNumSize(int winNumSize) {
		this.winNumSize = winNumSize;
	}

	public Integer[] getMachineNumbers() {
		return machineNumbers;
	}

	public void setMachineNumbers(Integer[] machineNumbers) {
		this.machineNumbers = machineNumbers;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
}
