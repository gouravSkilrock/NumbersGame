package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class PurchaseTicketDAO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String channel;
	private List<Integer> drawIdList;
	private int gameNo;
	private String isAdvancedPlay;
	private int noOfDraws;
	private List<PanelDAO> panelList;
	private int partyId;
	private String partyType;
	private String refTransId;
	private double ticketCost;
	private String ticketNo;

	public String getChannel() {
		return channel;
	}

	public List<Integer> getDrawIdList() {
		return drawIdList;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public List<PanelDAO> getPanelList() {
		return panelList;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public double getTicketCost() {
		return ticketCost;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setDrawIdList(List<Integer> drawIdList) {
		this.drawIdList = drawIdList;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setIsAdvancedPlay(String isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setPanelList(List<PanelDAO> panelList) {
		this.panelList = panelList;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public void setTicketCost(double ticketCost) {
		this.ticketCost = ticketCost;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

}
