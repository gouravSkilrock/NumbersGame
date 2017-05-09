package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawWiseTicketInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private int gameId;
	private List<TicketInfoBean> ticketInfoBeanList;
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
	public List<TicketInfoBean> getTicketInfoBeanList() {
		return ticketInfoBeanList;
	}
	public void setTicketInfoBeanList(List<TicketInfoBean> ticketInfoBeanList) {
		this.ticketInfoBeanList = ticketInfoBeanList;
	}
	
	
}
