package com.skilrock.ipe.Bean;

import java.io.Serializable;
import java.util.Map;



public class StartGameLMSBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean success;
	private Map<Integer, GameLMSBean> gameMap;
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<Integer, GameLMSBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameLMSBean> gameMap) {
		this.gameMap = gameMap;
	}

}
