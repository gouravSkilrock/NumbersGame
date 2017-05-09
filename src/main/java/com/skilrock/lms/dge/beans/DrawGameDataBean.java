package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawGameDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int gameId;
	private String gameName;
	private List<DrawInfoBean> drawInfoList;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public List<DrawInfoBean> getDrawInfoList() {
		return drawInfoList;
	}

	public void setDrawInfoList(List<DrawInfoBean> drawInfoList) {
		this.drawInfoList = drawInfoList;
	}

	@Override
	public String toString() {
		return "DrawGameDataBean [drawInfoList=" + drawInfoList + ", gameId="
				+ gameId + ", gameName=" + gameName + "]";
	}

}
