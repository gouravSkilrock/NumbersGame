package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class SuperTwoConstants {

	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static int END_RANGE = 99;
	public static String GAME_NAME = "superTwo";
	public static boolean IS_DUPLICATE = true;
	public static Map<Integer, String> SUPERTWO_DRAWNAME_MAP = new HashMap<Integer, String>();
	public static int MAX_PLAYER_PICKED = 2;
	public static int START_RANGE = 0;
	public static boolean IS_DUP_FOR_PERM2=true;

	public static int WINNING_NO = 2;

	static {
		BET_TYPE_MAP.put("Dir-2 Position", "2");
		BET_TYPE_MAP.put("Dir-2 Regular", "2");
		BET_TYPE_MAP.put("Banker-Front", "2");
		BET_TYPE_MAP.put("Banker-Rear", "2");
		BET_TYPE_MAP.put("Perm-2 Position", "3,20");
		BET_TYPE_MAP.put("Perm-2 Regular", "3,20");
		BET_TYPE_MAP.put("Perm-2", "3,20");
	}

	static {
		SUPERTWO_DRAWNAME_MAP.put(2, "SPECIAL");// "MONDAY"
		SUPERTWO_DRAWNAME_MAP.put(3, "LUCKY");// "TUESDAY"
		SUPERTWO_DRAWNAME_MAP.put(4, "MIDWEEK");// "WEDNESDAY"
		SUPERTWO_DRAWNAME_MAP.put(5, "FORTUNE");// "THURSDAY"
		SUPERTWO_DRAWNAME_MAP.put(6, "BONANZA");// "FRIDAY"
		SUPERTWO_DRAWNAME_MAP.put(7, "NATIONAL");// "SATURDAY"
	}

}
