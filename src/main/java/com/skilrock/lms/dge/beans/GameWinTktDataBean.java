package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class GameWinTktDataBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Map<Integer, DrawWinTktsBean> drawMap;
	private Set<String> userIdDetailList;
	private Set<String> claimetAtBoTickets;
	private Set<String> claimetAtRetTickets;
	private Set<String> claimetAtAgtTickets;
	
	public Map<Integer, DrawWinTktsBean> getDrawMap() {
		return drawMap;
	}
	public void setDrawMap(Map<Integer, DrawWinTktsBean> drawMap) {
		this.drawMap = drawMap;
	}
	public Set<String> getUserIdDetailList() {
		return userIdDetailList;
	}
	public void setUserIdDetailList(Set<String> userIdDetailList) {
		this.userIdDetailList = userIdDetailList;
	}
	public Set<String> getClaimetAtBoTickets() {
		return claimetAtBoTickets;
	}
	public void setClaimetAtBoTickets(Set<String> claimetAtBoTickets) {
		this.claimetAtBoTickets = claimetAtBoTickets;
	}
	public Set<String> getClaimetAtRetTickets() {
		return claimetAtRetTickets;
	}
	public void setClaimetAtRetTickets(Set<String> claimetAtRetTickets) {
		this.claimetAtRetTickets = claimetAtRetTickets;
	}
	public Set<String> getClaimetAtAgtTickets() {
		return claimetAtAgtTickets;
	}
	public void setClaimetAtAgtTickets(Set<String> claimetAtAgtTickets) {
		this.claimetAtAgtTickets = claimetAtAgtTickets;
	}
	
	
	
}