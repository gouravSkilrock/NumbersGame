package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class KenoSevenConstants {
	public static Map<String, Integer> BET_TYPE_MAP = new HashMap<String, Integer>();
	
	public static int START_RANGE = 1;
	public static int END_RANGE = 90;
	public static int MAX_PLAYER_PICKED = 10;
	public static String GAME_NAME = "kenoSeven";
	public static boolean IS_DUPLICATE = false;
	public static int WINNING_NO = 10;

	static {
		BET_TYPE_MAP.put("Direct10", 10);
	}
}