package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KenoNineConstants {
	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static int END_RANGE = 36;
	public static String GAME_NAME = "KenoNine";
	public static boolean IS_DUPLICATE = false;
	public static List<String> KenoNine_DRAWNAME_LIST = new ArrayList<String>();
	public static int MAX_PLAYER_PICKED = 6;
	public static int START_RANGE = 1;

	public static int WINNING_NO = 6;
	static {
		BET_TYPE_MAP.put("Direct1", "1");
		BET_TYPE_MAP.put("Direct2", "2");
		BET_TYPE_MAP.put("Direct3", "3");
		BET_TYPE_MAP.put("Direct4", "4");
		BET_TYPE_MAP.put("Direct5", "5");
		BET_TYPE_MAP.put("Direct6", "6");
		BET_TYPE_MAP.put("Perm2", "3,10");
		BET_TYPE_MAP.put("Perm3", "4,10");
		BET_TYPE_MAP.put("Perm4", "5,10");
	}
}
