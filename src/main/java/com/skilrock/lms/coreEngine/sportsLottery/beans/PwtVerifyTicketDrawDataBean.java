package com.skilrock.lms.coreEngine.sportsLottery.beans;

import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.BoardTicketDataBean;

public class PwtVerifyTicketDrawDataBean {

	private String drawName;
	private String drawDateTime;
	private String drawResult;
	private BoardTicketDataBean[] boardTicketBeanArray;	
	private String drawStatus;
	private double drawWinAmt;
	private int boardCount;
	private int drawId;
	private String message;
	public String getDrawName() {
		return drawName;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public String getDrawResult() {
		return drawResult;
	}
	public void setDrawResult(String drawResult) {
		this.drawResult = drawResult;
	}
	public BoardTicketDataBean[] getBoardTicketBeanArray() {
		return boardTicketBeanArray;
	}
	public void setBoardTicketBeanArray(BoardTicketDataBean[] boardTicketBeanArray) {
		this.boardTicketBeanArray = boardTicketBeanArray;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	public double getDrawWinAmt() {
		return drawWinAmt;
	}
	public void setDrawWinAmt(double drawWinAmt) {
		this.drawWinAmt = drawWinAmt;
	}
	public int getBoardCount() {
		return boardCount;
	}
	public void setBoardCount(int boardCount) {
		this.boardCount = boardCount;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
