package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class GameDrawInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameCode;
	private List<DrawDetailsBean> drawResultList;
	
	public String getGameCode() {
		return gameCode;
	}
	public List<DrawDetailsBean> getDrawResultList() {
		return drawResultList;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setDrawResultList(List<DrawDetailsBean> drawResultList) {
		this.drawResultList = drawResultList;
	}
	
}
