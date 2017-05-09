package com.skilrock.lms.embedded.drawGames.common;

import java.util.Set;
import java.util.TreeSet;

import rng.RNGUtilities;

public class Util {

	public static int getRandomNo(int startRange, int endRange) { 
		//int randomNo = (int) ((Math.random() * (endRange - startRange)) + startRange);
		int randomNo=RNGUtilities.generateRandomNumber(startRange, endRange);
		return randomNo;
	}
	public static String getRandomNoKeno(int startRange, int endRange,int noOfQP) {
		String randStr="";
		Set randSet = new TreeSet();
		while(randSet.size()!=noOfQP){
			//randStr=""+(int)((Math.random() * (endRange - startRange)) + startRange);
			randStr=""+(RNGUtilities.generateRandomNumber(startRange, endRange));
			randSet.add(randStr.length()>1?randStr:"0"+randStr);
			}
		
		
		return randSet.toString().replace("[", "").replace("]", "").replace(" ", "");
	}

}