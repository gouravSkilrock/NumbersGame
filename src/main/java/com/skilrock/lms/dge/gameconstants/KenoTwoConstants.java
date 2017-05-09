package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class KenoTwoConstants {

	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static int END_RANGE = 90;
	public static String GAME_NAME = "kenoTwo";
	public static boolean IS_DUPLICATE = false;
	public static Map<Integer, String> KENOTWO_DRAWNAME_MAP = new HashMap<Integer, String>();
	public static int MAX_PLAYER_PICKED = 5;
	public static int START_RANGE = 1;

	public static int WINNING_NO = 5;

	static {
		BET_TYPE_MAP.put("Direct1", "1");
		BET_TYPE_MAP.put("Direct2", "2");
		BET_TYPE_MAP.put("Direct3", "3");
		BET_TYPE_MAP.put("Direct4", "4");
		BET_TYPE_MAP.put("Direct5", "5");
		BET_TYPE_MAP.put("Perm1", "1,20");
		BET_TYPE_MAP.put("Perm2", "3,20");
		BET_TYPE_MAP.put("Perm3", "4,20");
		BET_TYPE_MAP.put("Banker", "1,4,1,20");
		BET_TYPE_MAP.put("Banker1AgainstAll", "1");
	}

	static {
		KENOTWO_DRAWNAME_MAP.put(2, "SPECIAL");// "MONDAY"
		KENOTWO_DRAWNAME_MAP.put(3, "LUCKY");// "TUESDAY"
		KENOTWO_DRAWNAME_MAP.put(4, "MIDWEEK");// "WEDNESDAY"
		KENOTWO_DRAWNAME_MAP.put(5, "FORTUNE");// "THURSDAY"
		KENOTWO_DRAWNAME_MAP.put(6, "BONANZA");// "FRIDAY"
		KENOTWO_DRAWNAME_MAP.put(7, "NATIONAL");// "SATURDAY"
	}

}
