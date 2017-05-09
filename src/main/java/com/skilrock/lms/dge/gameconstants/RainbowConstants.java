package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class RainbowConstants {

	public static Map<String, Integer> BET_TYPE_MAP = new HashMap<String, Integer>();
	public static int START_RANGE = 0;
	public static int END_RANGE = 19;
	public static int MAX_PLAYER_PICKED = 6;
	public static String GAME_NAME = "RainbowGame";
	public static boolean IS_DUPLICATE = true;
	public static int WINNING_NO = 6;

	public static enum colorsCode {
		V(1), I(2), B(3), G(4), Y(5), O(6), R(7);

		private int value;

		private colorsCode(int value) {
			this.value = value;
		}
	}

	static {
		BET_TYPE_MAP.put("BasicGame", 3);
		BET_TYPE_MAP.put("BasicRainbow", 6);
		BET_TYPE_MAP.put("PowerGame", 3);
		BET_TYPE_MAP.put("PowerRainbow", 6);
	}
}