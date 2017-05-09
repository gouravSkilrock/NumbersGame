package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class PerformDrawBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int currentDrawPerform;
	private int gameNumber;
	private int randomNo;
	private String performStyle;
	private List<Integer> winNumList;
	private List<String> winNumListStr;
	private String winningString;
	private List<Integer> machineNumList;
	
	public String getWinningString() {
		return winningString;
	}
	public void setWinningString(String winningString) {
		this.winningString = winningString;
	}
	public int getCurrentDrawPerform() {
		return currentDrawPerform;
	}
	public void setCurrentDrawPerform(int currentDrawPerform) {
		this.currentDrawPerform = currentDrawPerform;
	}
	public int getGameNumber() {
		return gameNumber;
	}
	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}
	public int getRandomNo() {
		return randomNo;
	}
	public void setRandomNo(int randomNo) {
		this.randomNo = randomNo;
	}
	public String getPerformStyle() {
		return performStyle;
	}
	public void setPerformStyle(String performStyle) {
		this.performStyle = performStyle;
	}
	public List<Integer> getWinNumList() {
		return winNumList;
	}
	public void setWinNumList(List<Integer> winNumList) {
		this.winNumList = winNumList;
	}
	public List<String> getWinNumListStr() {
		return winNumListStr;
	}
	public void setWinNumListStr(List<String> winNumListStr) {
		this.winNumListStr = winNumListStr;
	}
	public List<Integer> getMachineNumList() {
		return machineNumList;
	}
	public void setMachineNumList(List<Integer> machineNumList) {
		this.machineNumList = machineNumList;
	}
	
}
