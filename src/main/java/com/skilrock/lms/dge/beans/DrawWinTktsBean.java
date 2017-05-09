package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawWinTktsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<WinningTicketInfoBean> winTicketInfoBean;
	private String eventId;
	private int drawId;
	private String drawName;
	private String drawDateTime;
	private String ticketExpDate;
	private String drawStatus;
	
	public List<WinningTicketInfoBean> getWinTicketInfoBean() {
		return winTicketInfoBean;
	}
	public void setWinTicketInfoBean(List<WinningTicketInfoBean> winTicketInfoBean) {
		this.winTicketInfoBean = winTicketInfoBean;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
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
	public String getTicketExpDate() {
		return ticketExpDate;
	}
	public void setTicketExpDate(String ticketExpDate) {
		this.ticketExpDate = ticketExpDate;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	@Override
	public String toString() {
		return "DrawBean [drawDateTime=" + drawDateTime + ", drawId=" + drawId
				+ ", drawName=" + drawName + ", drawStatus=" + drawStatus
				+ ", eventId=" + eventId + ", ticketExpDate=" + ticketExpDate
				+ ", winTicketInfoBean=" + winTicketInfoBean + "]";
	}
	
	
	
	
}