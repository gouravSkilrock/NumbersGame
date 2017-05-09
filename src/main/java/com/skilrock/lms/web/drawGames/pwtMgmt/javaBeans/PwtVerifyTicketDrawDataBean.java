package com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class PwtVerifyTicketDrawDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int drawId;
	private String drawName;
	private String drawDateTime;
	private String drawResult;
	private List<BoardTicketDataBean> boardTicketBeanList;
	private String drawStatus;
	private String drawClaimTime;
	private double drawWinAmt;
	private int boardCount;
	private String message;

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

	public List<BoardTicketDataBean> getBoardTicketBeanList() {
		return boardTicketBeanList;
	}

	public void setBoardTicketBeanList(
			List<BoardTicketDataBean> boardTicketBeanList) {
		this.boardTicketBeanList = boardTicketBeanList;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	
	public String getDrawClaimTime() {
		return drawClaimTime;
	}

	public void setDrawClaimTime(String drawClaimTime) {
		this.drawClaimTime = drawClaimTime;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}