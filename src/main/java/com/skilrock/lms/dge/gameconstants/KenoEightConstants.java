package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KenoEightConstants {

	//public static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public static Map<String, Integer> BET_TYPE_MAP = new HashMap<String, Integer>();
	public static int END_RANGE = 90;
	public static String GAME_NAME = "kenoEight";
	public static boolean IS_DUPLICATE = false;
	//this method is depricated and not used commented on 11 april 2013 by sumit
	/*public static Map<Integer, String> KENO_DRAWNAME_MAP = new HashMap<Integer, String>();*/
	public static List<String> KENO_EIGHT_DRAWNAME_LIST = new ArrayList<String>();
	public static int MAX_PLAYER_PICKED = 5;
	public static int START_RANGE = 1;

	public static int WINNING_NO = 5;

	/*static {
		BET_TYPE_MAP.put("Direct1", "1");
		BET_TYPE_MAP.put("Direct2", "2");
		BET_TYPE_MAP.put("Direct3", "3");
		BET_TYPE_MAP.put("Direct4", "4");
		BET_TYPE_MAP.put("Direct5", "5");
		BET_TYPE_MAP.put("Perm2", "3,20");
		BET_TYPE_MAP.put("Perm3", "4,20");
		BET_TYPE_MAP.put("Banker", "1,4,1,20");
		BET_TYPE_MAP.put("Banker1AgainstAll", "1");
	}*/
	static {
		BET_TYPE_MAP.put("Direct1", 1);
		BET_TYPE_MAP.put("Direct2", 2);
		BET_TYPE_MAP.put("Direct3", 3);
		BET_TYPE_MAP.put("DC-Direct2", 2);
		BET_TYPE_MAP.put("DC-Direct3", 3);
		BET_TYPE_MAP.put("MN-Direct2", 2);
		BET_TYPE_MAP.put("MN-Direct3", 3);
		BET_TYPE_MAP.put("Direct4", 4);
		BET_TYPE_MAP.put("Direct5", 5);
		BET_TYPE_MAP.put("Perm2MIN", 3);
		BET_TYPE_MAP.put("Perm2MAX", 20);
		BET_TYPE_MAP.put("Perm3MIN", 4);
		BET_TYPE_MAP.put("Perm3MAX", 20);
		BET_TYPE_MAP.put("DC-Perm2MIN", 3);
		BET_TYPE_MAP.put("DC-Perm2MAX", 20);
		BET_TYPE_MAP.put("DC-Perm3MIN", 4);
		BET_TYPE_MAP.put("DC-Perm3MAX", 20);
		BET_TYPE_MAP.put("MN-Perm2MIN", 3);
		BET_TYPE_MAP.put("MN-Perm2MAX", 20);
		BET_TYPE_MAP.put("MN-Perm3MIN", 4);
		BET_TYPE_MAP.put("MN-Perm3MAX", 20);
		BET_TYPE_MAP.put("BankerULMIN", 1);
		BET_TYPE_MAP.put("BankerULMAX", 4);
		BET_TYPE_MAP.put("BankerBLMIN", 1);
		BET_TYPE_MAP.put("BankerBLMAX", 20);
		BET_TYPE_MAP.put("Banker1AgainstAll", 1);
	}
	//this method is depricated and not used commented on 11 april 2013 by sumit
	/*static {
		KENO_DRAWNAME_MAP.put(2, "MSP");// "MONDAY"
		KENO_DRAWNAME_MAP.put(3, "LUCKY");// "TUESDAY"
		KENO_DRAWNAME_MAP.put(4, "MIDWEEK");// "WEDNESDAY"
		KENO_DRAWNAME_MAP.put(5, "FORTUNE");// "THURSDAY"
		KENO_DRAWNAME_MAP.put(6, "BONANZA");// "FRIDAY"
		KENO_DRAWNAME_MAP.put(7, "NATIONAL");// "SATURDAY"
	}
	*/
	static{
		//KENO_INDOORDRAWNAME_LIST.add("SPECIAL");
		//KENO_INDOORDRAWNAME_LIST.add("ULTIMATE");
		KENO_EIGHT_DRAWNAME_LIST.add("MONDAY SPECIAL(MACHINE)");
		KENO_EIGHT_DRAWNAME_LIST.add("LUCKY TUESDAY(MACHINE)");
		KENO_EIGHT_DRAWNAME_LIST.add("MID-WEEK(MACHINE)");	
		KENO_EIGHT_DRAWNAME_LIST.add("FORTUNE THURSDAY(MACHINE)");	
		KENO_EIGHT_DRAWNAME_LIST.add("FRIDAY BONANZA(MACHINE)");	
		KENO_EIGHT_DRAWNAME_LIST.add("NATIONAL WEEKLY(MACHINE)");	
		}

}
