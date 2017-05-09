/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.common;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationVariables {

	public static final boolean BO_AUTO_PWT_UPDATION = false;
	public static String[] card12Data = { "ace_spade", "ace_heart",
			"ace_diamond", "ace_club", "king_spade", "king_heart",
			"king_diamond", "king_club", "queen_spade", "queen_heart",
			"queen_diamond", "queen_club" };
	public static String[] card16Data = { "ace_spade", "ace_heart",
			"ace_diamond", "ace_club", "king_spade", "king_heart",
			"king_diamond", "king_club", "queen_spade", "queen_heart",
			"queen_diamond", "queen_club", "jack_spade", "jack_heart",
			"jack_diamond", "jack_club" };
	public final static int dateLenA = 3;

	public final static int dateLenB = 4;
	public static final int FASTLOTTO_END_RANGE = 35;
	public static final int FASTLOTTO_START_RANGE = 1;

	public static final int FASTLOTTO_WIN_NUMBERS = 5;
	public static String[] fortuneData = { "Aries", "Taurus", "Gemini",
			"Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius",
			"Capricorn", "Aquarius", "Pisces" };
	public final static int gameNoLenA = 1;

	public final static int gameNoLenB = 1;
	public static final int GRAPH_STARTING_YEAR = 2008;
	public static final int KENO_END_RANGE = 90;

	public static final int KENO_START_RANGE = 1;
	public static final int KENO_WIN_NUMBERS = 5;
	
	public static final int KENOTWO_END_RANGE = 90;

	public static final int KENOTWO_START_RANGE = 1;
	public static final int KENOTWO_WIN_NUMBERS = 5;
	
	public static final int LOTTO_END_RANGE = 49;

	public static final int LOTTO_START_RANGE = 1;

	public static final int LOTTO_WIN_NUMBERS = 6;

	public final static int retIdLenA = 4;

	public final static int retIdLenB = 5;

	public final static int rpcLimit = 20;
	public static final int STARTING_YEAR = 2000;
	public final static int currentTktLen = 18;
	public final static int tktLenA = 16;
	public final static int tktLenB = 18;
	public final static int barcodeCount = 20;
	public final static int barcodeRandomLength = 2;
	public static final boolean USER_RELOGIN_SESSION_TERMINATE = true;
	public static String[] zerotonineData = { "Zero(0)", "One(1)", "Two(2)",
			"Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)",
			"Nine(9)" };
	public static final int ZIMLOTTO_END_RANGE = 42;
	public static final int ZIMLOTTO_START_RANGE = 1;

	public static final int ZIMLOTTO_WIN_NUMBERS = 6;
	
	public final static int RANDOM_RET_ID_A_START_RANGE = 1004;
	public final static int RANDOM_RET_ID_A_END_RANGE = 9999;
	public final static int RANDOM_RET_ID_B_START_RANGE = 11001;
	public final static int RANDOM_RET_ID_B_END_RANGE = 25000;
	public final static int RANDOM_RET_ID_B_RAND_DIGIT = 4;
	
	public static final int ZIMLOTTOBONUSTWO_END_RANGE = 36;
	public static final int ZIMLOTTOBONUSTWO_START_RANGE = 1;
	public static final int ZIMLOTTOBONUSTWO_WIN_NUMBERS = 7;
	
	public static final int ZIMLOTTOTWO_END_RANGE = 42;
	public static final int ZIMLOTTOTWO_START_RANGE = 1;
	public static final int ZIMLOTTOTWO_WIN_NUMBERS = 6;
	
	public static final int ZIMLOTTOBONUS_END_RANGE = 42;
	public static final int ZIMLOTTOBONUS_START_RANGE = 1;
	public static final int ZIMLOTTOBONUS_WIN_NUMBERS = 7;
	
	public static final int ZIMLOTTOBONUSFREE_END_RANGE = 42;
	public static final int ZIMLOTTOBONUSFREE_START_RANGE = 1;
	public static final int ZIMLOTTOBONUSFREE_WIN_NUMBERS = 7;
	
	public static final int ZIMLOTTOTHREE_END_RANGE = 42;
	public static final int ZIMLOTTOTHREE_START_RANGE = 1;

	public static final int ZIMLOTTOTHREE_WIN_NUMBERS = 6;
	public static final String from ="lms.user@skilrock.com";
	public static final String password ="skilrock";
	public static final	List<String> to = new ArrayList<String>();
	static{ 
		to.add("vishal.verma@skilrock.com");
		to.add("Vishal.vijay@skilrock.com");
	}
	
	public static final String LMS_MERCHANT_CODE = "RMS";
	
}
