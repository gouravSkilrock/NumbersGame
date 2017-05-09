package com.skilrock.lms.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityLogger {
	private static final Logger logger = LoggerFactory.getLogger("LmsActivityLog");
		
	public static void printInfo(String pattern,Object... a){
		logger.info(pattern,a);
	}
	
	public static void printDebug(String pattern,Object... a){
		logger.debug(pattern,a);
	}
	
	public static void printError(String pattern,Object... a){
		
		logger.error(pattern,a);
	}
	
	public static void printWarning(String pattern,Object... a){
		logger.warn(pattern,a);
	}
	
	
	
	
}
