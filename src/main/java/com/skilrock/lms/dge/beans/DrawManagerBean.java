package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DrawManagerBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String autoDrawSec;
	private String currDrawId;
	private String currDrawTime;
	private String currFreezeTime;
	private String currNoOfTicket;
	private String gameName;
	private String lastDrawTime;
	private String lastNoOfTicket;
	private String lastNoOfWinner;
	private String lastWinSymbol;
	private String performStatus;
	private String currDrawName;
	private String lastMachineResult;
	public String getAutoDrawSec() {
		return autoDrawSec;
	}

	public String getCurrDrawId() {
		return currDrawId;
	}

	public String getCurrDrawTime() {
		return currDrawTime;
	}

	public String getCurrFreezeTime() {
		return currFreezeTime;
	}

	public String getCurrNoOfTicket() {
		return currNoOfTicket;
	}

	public String getGameName() {
		return gameName;
	}

	public String getLastDrawTime() {
		return lastDrawTime;
	}

	public String getLastNoOfTicket() {
		return lastNoOfTicket;
	}

	public String getLastNoOfWinner() {
		return lastNoOfWinner;
	}

	public String getLastWinSymbol() {
		return lastWinSymbol;
	}

	public void setAutoDrawSec(String autoDrawSec) {
		this.autoDrawSec = autoDrawSec;
	}

	public void setCurrDrawId(String currDrawId) {
		this.currDrawId = currDrawId;
	}

	public void setCurrDrawTime(String currDrawTime) {
		this.currDrawTime = currDrawTime;
	}

	public void setCurrFreezeTime(String currFreezeTime) {
		this.currFreezeTime = currFreezeTime;
	}

	public void setCurrNoOfTicket(String currNoOfTicket) {
		this.currNoOfTicket = currNoOfTicket;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setLastDrawTime(String lastDrawTime) {
		this.lastDrawTime = lastDrawTime;
	}

	public void setLastNoOfTicket(String lastNoOfTicket) {
		this.lastNoOfTicket = lastNoOfTicket;
	}

	public void setLastNoOfWinner(String lastNoOfWinner) {
		this.lastNoOfWinner = lastNoOfWinner;
	}

	public void setLastWinSymbol(String lastWinSymbol) {
		this.lastWinSymbol = lastWinSymbol;
	}

	public String getPerformStatus() {
		return performStatus;
	}

	public void setPerformStatus(String performStatus) {
		this.performStatus = performStatus;
	}

	public String getCurrDrawName() {
		return currDrawName;
	}

	public void setCurrDrawName(String currDrawName) {
		this.currDrawName = currDrawName;
	}
	public String getLastMachineResult() {
		return lastMachineResult;
	}

	public void setLastMachineResult(String lastMachineResult) {
		this.lastMachineResult = lastMachineResult;
	}
}
