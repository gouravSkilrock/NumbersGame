package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperPerformDrawDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	private String gameNo;
	private int[] drawIds;
	private String[] drawType;
	private Integer[] winningNumbers;
	private Integer[] machineNumbers;
	private int winNumSize;
	private int[] cardType;
	private String systemUserName;
	private String systemPassword;
	private boolean isSuccess;
	private String errorCode;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGameNo() {
		return gameNo;
	}
	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}
	public int[] getDrawIds() {
		return drawIds;
	}
	public void setDrawIds(int[] drawIds) {
		this.drawIds = drawIds;
	}
	public String[] getDrawType() {
		return drawType;
	}
	public void setDrawType(String[] drawType) {
		this.drawType = drawType;
	}
	public Integer[] getWinningNumbers() {
		return winningNumbers;
	}
	public void setWinningNumbers(Integer[] winningNumbers) {
		this.winningNumbers = winningNumbers;
	}
	public int getWinNumSize() {
		return winNumSize;
	}
	public void setWinNumSize(int winNumSize) {
		this.winNumSize = winNumSize;
	}
	public int[] getCardType() {
		return cardType;
	}
	public void setCardType(int[] cardType) {
		this.cardType = cardType;
	}
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemPassword() {
		return systemPassword;
	}
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Integer[] getMachineNumbers() {
		return machineNumbers;
	}
	public void setMachineNumbers(Integer[] machineNumbers) {
		this.machineNumbers = machineNumbers;
	}

	
	
}
