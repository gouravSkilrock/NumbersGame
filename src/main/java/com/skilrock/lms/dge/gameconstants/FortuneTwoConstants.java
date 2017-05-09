package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class FortuneTwoConstants {

	public static String[] fortuneData = new String[] { "", "Aries", "Taurus",
			"Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio",
			"Sagittarius", "Capricorn", "Aquarius", "Pisces" };
	// public static int[] fortuneData=new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
	public static String GAME_NAME = "fortunetwo";
	public static int MAX_PLAYER_PICKED = 4;
	public static boolean IS_DUPLICATE = true;
	public static int WINNING_NUMBERS = 4;
	public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	
	static {
		
		BET_TYPE_MAP.put("Direct1", "1");
		BET_TYPE_MAP.put("Direct2", "2");
		BET_TYPE_MAP.put("Direct3", "3");
		BET_TYPE_MAP.put("Direct4", "4");
		BET_TYPE_MAP.put("Banker2", "2");
		BET_TYPE_MAP.put("Banker3", "3");
		
	}

}
