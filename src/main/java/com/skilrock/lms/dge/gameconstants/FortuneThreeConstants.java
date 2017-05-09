package com.skilrock.lms.dge.gameconstants;

import java.util.HashMap;
import java.util.Map;

public class FortuneThreeConstants {

	public static String[] fortuneData = new String[] { "","A-Spade",
		 "A-Heart",
		 "A-Club",
		 "A-Diamond",
		 "Q-Spade",
		 "Q-Heart",
		 "Q-Club",
		 "Q-Diamond",
		 "K-Spade",
		 "K-Heart",
		 "K-Club",
		 "K-Diamond" };
	 
	
	// public static int[] fortuneData=new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
	public static String GAME_NAME = "fortunethree";
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
