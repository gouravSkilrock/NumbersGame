package com.skilrock.lms.common.utility;

import java.util.Properties;

public abstract class TextConfigurator{
	private static Properties propLoader = null;
	static{
		try{
			propLoader = PropertyLoader.loadProperties("RMS/lagos.properties");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getText(String keyName){
		return propLoader.getProperty(keyName);
	}
}
