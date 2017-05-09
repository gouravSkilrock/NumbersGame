package com.skilrock.lms.common.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CalculateAvailable {
	static Log logger = LogFactory.getLog(CalculateAvailable.class);

	public static double calculateAvlCredit(double creditLimit,
			double exCreditLimit, double curCreditAmt) {
		return creditLimit + exCreditLimit - curCreditAmt;

	}

}
