package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class DrawResultDAO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timestamp drawTime;
	private String winningResult;

	public Timestamp getDrawTime() {
		return drawTime;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setDrawTime(Timestamp drawTime) {
		this.drawTime = drawTime;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

}
