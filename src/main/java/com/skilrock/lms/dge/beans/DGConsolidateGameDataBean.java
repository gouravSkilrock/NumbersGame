package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DGConsolidateGameDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameDispName;
	private int gameId;
	private String gameName;
	private int gameNo;
	private List<DGConsolidateDrawBean> drawDataBeanList;

	public String getGameDispName() {
		return gameDispName;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public List<DGConsolidateDrawBean> getDrawDataBeanList() {
		return drawDataBeanList;
	}

	public void setDrawDataBeanList(List<DGConsolidateDrawBean> drawDataBeanList) {
		this.drawDataBeanList = drawDataBeanList;
	}

	@Override
	public String toString() {
		return "DGConsolidateGameDataBean [gameDispName=" + gameDispName
				+ ", gameId=" + gameId + ", gameName=" + gameName + ", gameNo="
				+ gameNo + ", drawDataBeanList=" + drawDataBeanList + "]";
	}

}
