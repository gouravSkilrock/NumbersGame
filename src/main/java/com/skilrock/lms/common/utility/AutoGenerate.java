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

package com.skilrock.lms.common.utility;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to generate the password automatically for every new user
 * created.
 * 
 * @author SkilRockTechnologies
 * 
 */
public class AutoGenerate {
	static Log logger = LogFactory.getLog(AutoGenerate.class);

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
		//logger.debug("pwd = " + newString.toString());
		return newString.toString();
	}
}
