package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwelveByTwentyFourConstants {
	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static int END_RANGE = 24;
	public static String GAME_NAME = "TwelveByTwentyFour";
	public static boolean IS_DUPLICATE = false;
	public static List<String> TwelveByTwentyFour_DRAWNAME_LIST = new ArrayList<String>();
	public static int MAX_PLAYER_PICKED = 12;
	public static int START_RANGE = 1;

	public static int WINNING_NO = 12;
	static {
		BET_TYPE_MAP.put("Direct12", "12");
		BET_TYPE_MAP.put("First12", "12");
		BET_TYPE_MAP.put("Last12", "12");
		BET_TYPE_MAP.put("AllOdd", "12");
		BET_TYPE_MAP.put("AllEven", "12");
		BET_TYPE_MAP.put("OddEven", "12");
		BET_TYPE_MAP.put("EvenOdd", "12");
		BET_TYPE_MAP.put("JumpEvenOdd", "12");
		BET_TYPE_MAP.put("JumpOddEven", "12");
		BET_TYPE_MAP.put("Perm12", "13,14");
		BET_TYPE_MAP.put("Match10", "10");
	}
}
