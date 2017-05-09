package com.skilrock.lms.web.loginMgmt;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to generate the password automatically for every new user
 * created.
 * 
 * @author SkilRockTechnologies
 */
public class AutoGenerate {
	static Log logger = LogFactory.getLog(AutoGenerate.class);

	/**
	 * This static method is used for generating password automatically for
	 * every new user created
	 * 
	 * @return String
	 * 
	 */
	public static String autoPassword() {
		Random r = new Random();

		StringBuffer newString = new StringBuffer();
		// No of Big letters in password
		int a1 = r.nextInt(6) + 1;
		// No of small letters in password
		int a2 = r.nextInt(7 - a1) + 1;
		// no on numbers
		int a3 = 8 - a1 - a2;

		for (int j = 0; j < a1; j++) {
			char c1 = (char) (r.nextInt(26) + 65);
			// AL.add(new Character(c1));
			newString.append(new Character(c1));
		}
		for (int j = 0; j < a2; j++) {
			char c1 = (char) (r.nextInt(26) + 97);
			// AL.add(new Character(c1));
			newString.append(new Character(c1));
		}
		for (int j = 0; j < a3; j++) {
			char c1 = (char) (r.nextInt(9) + 48);
			// AL.add(new Character(c1));
			newString.append(new Character(c1));
		}
		logger.debug("pwd = " + newString.toString());
		return newString.toString();
	}

	/**
	 * This method generate 8 digit random number with digit having range 0-9
	 * 
	 * @return
	 */
	public static int autoPasswordInt() {
		SecureRandom random = new SecureRandom();
		StringBuffer sb = new StringBuffer();
		int randomPasswd;
		for (int i = 0; i < 8; i++) {
			int rndmInt = random.nextInt(9) + 1;
			sb.append(rndmInt);
		}
		randomPasswd = Integer.parseInt(new String(sb));
		return randomPasswd;
	}

	public static void main(String args[]) {
		String str = Integer.toString(autoPasswordInt());
		logger.debug(autoPasswordInt());
	}
}
