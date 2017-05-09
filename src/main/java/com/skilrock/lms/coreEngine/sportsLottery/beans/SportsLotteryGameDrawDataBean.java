package com.skilrock.lms.coreEngine.sportsLottery.beans;

import java.io.Serializable;
import java.util.Map;


public class SportsLotteryGameDrawDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private SportsLotteryGameEventDataBean[] gameEventDataBeanArray=null;
	private int noOfLines;
	private int betAmountMultiple;
	private double boardPurchaseAmount;
	private String drawDateTime;
	private String drawDisplayname;
	private Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap;
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	
	public SportsLotteryGameEventDataBean[] getGameEventDataBeanArray() {
		return gameEventDataBeanArray;
	}
	public void setGameEventDataBeanArray(
			SportsLotteryGameEventDataBean[] gameEventDataBeanArray) {
		this.gameEventDataBeanArray = gameEventDataBeanArray;
	}
	public int getBetAmountMultiple() {
		return betAmountMultiple;
	}
	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}
	public double getBoardPurchaseAmount() {
		return boardPurchaseAmount;
	}
	public void setBoardPurchaseAmount(double boardPurchaseAmount) {
		this.boardPurchaseAmount = boardPurchaseAmount;
	}
	public int getNoOfLines() {
		return noOfLines;
	}
	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public String getDrawDisplayname() {
		return drawDisplayname;
	}
	public void setDrawDisplayname(String drawDisplayname) {
		this.drawDisplayname = drawDisplayname;
	}
	public Map<Integer, PrizeRankDrawWinningBean> getDrawPrizeRankMap() {
		return drawPrizeRankMap;
	}
	public void setDrawPrizeRankMap(Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap) {
		this.drawPrizeRankMap = drawPrizeRankMap;
	}
	
	
}
