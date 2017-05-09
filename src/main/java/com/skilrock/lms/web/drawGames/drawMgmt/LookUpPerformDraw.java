package com.skilrock.lms.web.drawGames.drawMgmt;

import java.util.Properties;

import com.skilrock.lms.common.utility.PropertyLoader;
/**
 * 
 * @author Arun Tanwar
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2010     ABxxxxxx       CR#zzzzzz: blah blah blah... 
 * </pre>
 */
public class LookUpPerformDraw {
	private static Properties performDraw = null;
	static {
		performDraw = PropertyLoader.loadProperties("performDraw.properties");
	}

	public static Class getPerformClass(String gameNo) {
		try {
			// Here we solve the name the client uses to a class name
			String moduleClass = performDraw.getProperty(gameNo);
			// With the class name we use reflection
			// and instantiate a new service
			Class clazz = Class.forName(moduleClass);
			return clazz;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
