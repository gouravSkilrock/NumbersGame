package com.skilrock.lms.dge.gameconstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KenoFourConstants {

	public final static int WINNING_NO = 5;
	public final static int START_RANGE = 1;
	public final static int END_RANGE = 90;
	public final static int MAX_PLAYER_PICKED = 5;
	public final static boolean IS_DUPLICATE = false;
	public final static String GAME_NAME = "kenoFour";
	public final static List<String> KENO_INDOORDRAWNAME_LIST = new ArrayList<String>();
	public final static Map<String, String> BET_TYPE_MAP = new HashMap<String, String>();
	public final static Map<Integer, String> KENOFOUR_DRAWNAME_MAP = new HashMap<Integer, String>();
	

	static {

		BET_TYPE_MAP.put("Direct1", "1");
		BET_TYPE_MAP.put("Direct2", "2");
		BET_TYPE_MAP.put("Direct3", "3");
		BET_TYPE_MAP.put("DC-Direct1", "1");
		BET_TYPE_MAP.put("DC-Direct2", "2");
		BET_TYPE_MAP.put("DC-Direct3", "3");
		BET_TYPE_MAP.put("MN-Direct1", "1");
		BET_TYPE_MAP.put("MN-Direct2", "2");
		BET_TYPE_MAP.put("MN-Direct3", "3");
		BET_TYPE_MAP.put("MN-Direct4", "4");
		BET_TYPE_MAP.put("MN-Direct5", "5");
		BET_TYPE_MAP.put("Direct4", "4");
		BET_TYPE_MAP.put("Direct5", "5");
		BET_TYPE_MAP.put("DC-Direct4", "4");
		BET_TYPE_MAP.put("DC-Direct5", "5");
		BET_TYPE_MAP.put("Perm2", "3,20");
		BET_TYPE_MAP.put("Perm3", "4,20");
		BET_TYPE_MAP.put("DC-Perm2", "3,20");
		BET_TYPE_MAP.put("DC-Perm3", "4,20");
		BET_TYPE_MAP.put("MN-Perm2", "3,20");
		BET_TYPE_MAP.put("MN-Perm3", "4,20");
		BET_TYPE_MAP.put("Banker", "1,4,1,20");
		BET_TYPE_MAP.put("Banker1AgainstAll", "1");
		BET_TYPE_MAP.put("MN-Banker", "1,4,1,20");
		BET_TYPE_MAP.put("MN-Banker1AgainstAll", "1");
		BET_TYPE_MAP.put("4By90-Direct2", "2");
		BET_TYPE_MAP.put("3By90-Direct2", "2");
		BET_TYPE_MAP.put("2By90-Direct2", "2");
		BET_TYPE_MAP.put("4By90-Perm2", "3,20");
		BET_TYPE_MAP.put("3By90-Perm2", "3,20");
		BET_TYPE_MAP.put("2By90-Perm2", "3,20");
		


	}

	static {
		KENOFOUR_DRAWNAME_MAP.put(2, "MSP");// "MONDAY"
		KENOFOUR_DRAWNAME_MAP.put(3, "LUCKY");// "TUESDAY"
		KENOFOUR_DRAWNAME_MAP.put(4, "MIDWEEK");// "WEDNESDAY"
		KENOFOUR_DRAWNAME_MAP.put(5, "FORTUNE");// "THURSDAY"
		KENOFOUR_DRAWNAME_MAP.put(6, "BONANZA");// "FRIDAY"
		KENOFOUR_DRAWNAME_MAP.put(7, "NATIONAL");// "SATURDAY"
	}

	static {
		KENO_INDOORDRAWNAME_LIST.add("GOLD COAST");
		KENO_INDOORDRAWNAME_LIST.add("INTERNATIONAL");
		KENO_INDOORDRAWNAME_LIST.add("CONTINENTAL");
		KENO_INDOORDRAWNAME_LIST.add("WINLOT SPECIAL");
		KENO_INDOORDRAWNAME_LIST.add("ROYAL CASH");
		KENO_INDOORDRAWNAME_LIST.add("WINLOT ULTIMATE");
		KENO_INDOORDRAWNAME_LIST.add("WINLOT JACKPOT");
		KENO_INDOORDRAWNAME_LIST.add("MEGA CASH");
	}
}
