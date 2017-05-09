package com.skilrock.lms.coreEngine.virtualSport.common;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl.CommonMethodsControllerImpl;

public class VSUtil {
	public static Map<Integer, GameMasterBean> gameInfoMap = null; 
	public static Map<Integer, Map<Integer, List<MessageDetailsBean>>> advMessageMap = null;

	static {
		setGameMap();
	}

	private static void setGameMap() {
		gameInfoMap = CommonMethodsControllerImpl.getInstance().getGameMap();
	}
	
	public static int getGameId(String gameName) {
		int gameId = 1;

		for (Map.Entry<Integer, GameMasterBean> gameMap : gameInfoMap.entrySet()) {
			if (gameName.equals(gameMap.getValue().getGameDevName())) {
				gameId = gameMap.getKey();
				break;
			}
		}
		return gameId;
	}

}