package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class GameInfoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameCode;
	private String gameDisplayName;
	private int gameNo;
	private int type;
	private int typeSpecificInfo;
	private int noOfDraws;
	private double unit_price;
	private int ticket_validity;
	private String jackpotAmount;
	private int online_FTG;
	private List<DrawDetailsBean> drawBeanList;
	public String getGameCode() {
		return gameCode;
	}
	public String getGameDisplayName() {
		return gameDisplayName;
	}
	public int getGameNo() {
		return gameNo;
	}
	public int getType() {
		return type;
	}
	
	public int getNoOfDraws() {
		return noOfDraws;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public int getTicket_validity() {
		return ticket_validity;
	}
	public String getJackpotAmount() {
		return jackpotAmount;
	}
	public int getOnline_FTG() {
		return online_FTG;
	}
	public List<DrawDetailsBean> getDrawBeanList() {
		return drawBeanList;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setGameDisplayName(String gameDisplayName) {
		this.gameDisplayName = gameDisplayName;
	}
	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}
	public void setUnit_price(double unitPrice) {
		unit_price = unitPrice;
	}
	public void setTicket_validity(int ticketValidity) {
		ticket_validity = ticketValidity;
	}
	public void setJackpotAmount(String jackpotAmount) {
		this.jackpotAmount = jackpotAmount;
	}
	public void setOnline_FTG(int onlineFTG) {
		online_FTG = onlineFTG;
	}
	public void setDrawBeanList(List<DrawDetailsBean> drawBeanList) {
		this.drawBeanList = drawBeanList;
	}
	public int getTypeSpecificInfo() {
		return typeSpecificInfo;
	}
	public void setTypeSpecificInfo(int typeSpecificInfo) {
		this.typeSpecificInfo = typeSpecificInfo;
	}
	
	

}
