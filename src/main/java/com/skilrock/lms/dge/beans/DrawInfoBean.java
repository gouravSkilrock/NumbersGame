package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String drawName;
	private String drawDateTime;
	private String winningResult;
	private List<DrawPrizeDetailBean> drawPrizeDetailList;

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

	public String getWinningResult() {
		return winningResult;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public List<DrawPrizeDetailBean> getDrawPrizeDetailList() {
		return drawPrizeDetailList;
	}

	public void setDrawPrizeDetailList(
			List<DrawPrizeDetailBean> drawPrizeDetailList) {
		this.drawPrizeDetailList = drawPrizeDetailList;
	}

	@Override
	public String toString() {
		return "DrawInfoBean [drawDateTime=" + drawDateTime + ", drawName="
				+ drawName + ", drawPrizeDetailList=" + drawPrizeDetailList
				+ ", winningResult=" + winningResult + "]";
	}
}
