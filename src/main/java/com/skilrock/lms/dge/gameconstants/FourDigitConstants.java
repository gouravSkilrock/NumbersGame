package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourDigitConstants {

	
	public static Map<String, Integer> BET_TYPE_MAP = null;
	public static List<String> DIGIT_RANGE = new ArrayList<String>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));
	public static int END_RANGE = 9999;
	public static String GAME_NAME = "fourDigit";
	public static boolean IS_DUPLICATE = false;
	public static int START_RANGE = 0000;
	public static int WINNING_NO = 23;	
	public static int DIGIT_PICKED = 4;
	public static int NUMBER_PICKED = 1;
	public static int FORECAST_TYPES = 2;

	static {
		BET_TYPE_MAP = new HashMap<String, Integer>();
		BET_TYPE_MAP.put("Straight", DIGIT_PICKED);
		BET_TYPE_MAP.put("Roll", DIGIT_PICKED);
		BET_TYPE_MAP.put("Permute", DIGIT_PICKED);
	}

}
