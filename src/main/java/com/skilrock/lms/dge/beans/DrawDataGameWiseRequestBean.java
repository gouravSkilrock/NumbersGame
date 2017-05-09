package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class DrawDataGameWiseRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Integer> gameId;
	private int drawDataListCount;

	public List<Integer> getGameId() {
		return gameId;
	}

	public void setGameId(List<Integer> gameId) {
		this.gameId = gameId;
	}

	public int getDrawDataListCount() {
		return drawDataListCount;
	}

	public void setDrawDataListCount(int drawDataListCount) {
		this.drawDataListCount = drawDataListCount;
	}

	@Override
	public String toString() {
		return "DrawDataGameWiseRequestBean [drawDataListCount="
				+ drawDataListCount + ", gameId=" + gameId + "]";
	}
}
