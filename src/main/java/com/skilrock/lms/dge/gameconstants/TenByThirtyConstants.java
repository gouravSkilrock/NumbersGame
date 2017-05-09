package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenByThirtyConstants {
	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static int END_RANGE = 30;
	public static String GAME_NAME = "TenByThirty";
	public static boolean IS_DUPLICATE = false;
	public static List<String> TenByThirty_DRAWNAME_LIST = new ArrayList<String>();
	public static int MAX_PLAYER_PICKED = 10;
	public static int START_RANGE = 1;

	public static int WINNING_NO = 10;
	static {
		BET_TYPE_MAP.put("Direct10", "10");
		BET_TYPE_MAP.put("First10", "10");
		BET_TYPE_MAP.put("Last10", "10");
		BET_TYPE_MAP.put("Middle10", "10");
	}
}
