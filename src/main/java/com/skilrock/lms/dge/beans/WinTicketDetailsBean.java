package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Set;

public class WinTicketDetailsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<Integer> winningbrSet;
	private int gameNo;
	private int purchasetableName;
	private int drawId;
	private Connection con;
	private List<Integer> orgIdList;
	
	public Set<Integer> getWinningbrSet() {
		return winningbrSet;
	}
	public void setWinningbrSet(Set<Integer> winningbrSet) {
		this.winningbrSet = winningbrSet;
	}
	public int getGameNo() {
		return gameNo;
	}
	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}
	public int getPurchasetableName() {
		return purchasetableName;
	}
	public void setPurchasetableName(int purchasetableName) {
		this.purchasetableName = purchasetableName;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public Connection getCon() {
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}
	public List<Integer> getOrgIdList() {
		return orgIdList;
	}
	public void setOrgIdList(List<Integer> orgIdList) {
		this.orgIdList = orgIdList;
	}

	
}
