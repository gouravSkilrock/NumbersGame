package com.skilrock.lms.coreEngine.instantPrint.common;

import java.util.Arrays;
import java.util.List;

public class IPEErrorMsg {

	public static String buyErrMsg(String errCons) {
		List<String> errList = Arrays.asList("AGT_INS_BAL", "RET_INS_BAL","FRAUD", "NO_SALE");

		int errVal = errList.indexOf(errCons);
		String errMsg;
		switch (errVal) {
		case 0:
			errMsg = "Agent has insufficient Balance";
			break;
		case 1:
			errMsg = "Retailer has insufficient Balance";
			break;
		case 2:
			errMsg = "Sale Limit Exceed";
			break;
		case 3:
			errMsg = "Sale Not Allowed";
			break;
		default:
			errMsg = "Error!Try Again";
			break;
		}

		return errMsg;
	}

}
