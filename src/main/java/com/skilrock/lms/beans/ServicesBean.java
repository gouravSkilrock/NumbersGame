package com.skilrock.lms.beans;

import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class ServicesBean {
	private static final boolean isDG="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw());
	private static final boolean isSE="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch());
	private static final boolean isCS ="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS());
	private static final boolean isOLA ="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA());
	private static final boolean isIPE="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIPE());
	private static final boolean isSLE="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE());
	private static final boolean isIW = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW());
	private static final boolean isVS = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS());
	public static boolean isDG() {
		return isDG;
	}
	public static boolean isSE() {
		return isSE;
	}
	public static boolean isCS() {
		return isCS;
	}
	public static boolean isOLA() {
		return isOLA;
	}
	public static boolean isIPE() {
		return isIPE;
	}
	public static boolean isSLE() {
		return isSLE;
	}
	public static boolean isIW() {
		return isIW;
	}
	public static boolean isVS() {
		return isVS;
	}
	
}
