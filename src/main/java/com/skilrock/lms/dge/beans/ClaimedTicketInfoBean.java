package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ClaimedTicketInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private int gameId;
	private int gameTypeId;
	private List<String> ticketList;
	private Map<Long, String> ticketMap;
	private int refMerchantId;
	private String refMerchantName;
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	public List<String> getTicketList() {
		return ticketList;
	}
	public void setTicketList(List<String> ticketList) {
		this.ticketList = ticketList;
	}
	public Map<Long, String> getTicketMap() {
		return ticketMap;
	}
	public void setTicketMap(Map<Long, String> ticketMap) {
		this.ticketMap = ticketMap;
	}
	public int getRefMerchantId() {
		return refMerchantId;
	}
	public void setRefMerchantId(int refMerchantId) {
		this.refMerchantId = refMerchantId;
	}
	public String getRefMerchantName() {
		return refMerchantName;
	}
	public void setRefMerchantName(String refMerchantName) {
		this.refMerchantName = refMerchantName;
	}
	
	
	
	
}
