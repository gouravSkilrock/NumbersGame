package com.skilrock.lms.coreEngine.sportsLottery.common;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;

public class SLEUtil {
	public static Map<Integer, GameMasterBean> gameInfoMap = null; 
	public static Map<Integer, GameTypeMasterBean> gameTypeInfoMap = null; 
	public static Map<Integer, Map<Integer, List<MessageDetailsBean>>> advMessageMap = null;

	static {
		setGameMap();
		setGameTypeMap();
	}

	private static void setGameMap() {
		gameInfoMap = CommonMethodsControllerImpl.getInstance().getGameMap();
	}

	private static void setGameTypeMap() {
		gameTypeInfoMap = CommonMethodsControllerImpl.getInstance().getGameTypeMap();
	}
}