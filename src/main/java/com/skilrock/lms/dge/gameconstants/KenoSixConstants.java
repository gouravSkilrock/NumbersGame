package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class KenoSixConstants {

	public static Map<String, Integer> BET_TYPE_MAP = new HashMap<String, Integer>();
	
	public static int END_RANGE = 80;
	public static int START_RANGE = 1;
	public static int MAX_PLAYER_PICKED = 10;
	public static String GAME_NAME = "kenoSix";
	public static boolean IS_DUPLICATE = false;

	public static int WINNING_NO = 20;

	static {
		BET_TYPE_MAP.put("Direct1", 1);
		BET_TYPE_MAP.put("Direct2", 2);
		BET_TYPE_MAP.put("Direct3", 3);
		BET_TYPE_MAP.put("Direct4", 4);
		BET_TYPE_MAP.put("Direct5", 5);
		BET_TYPE_MAP.put("Direct6", 6);
		BET_TYPE_MAP.put("Direct7", 7);
		BET_TYPE_MAP.put("Direct8", 8);
		BET_TYPE_MAP.put("Direct9", 9);
		BET_TYPE_MAP.put("Direct10", 10);
		BET_TYPE_MAP.put("Perm2MIN", 3);
		BET_TYPE_MAP.put("Perm2MAX", 20);
		BET_TYPE_MAP.put("Perm3MIN", 4);
		BET_TYPE_MAP.put("Perm3MAX", 20);
	}
	
}
