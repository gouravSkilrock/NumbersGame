package com.skilrock.lms.coreEngine.drawGames.common;

import java.util.Arrays;
import java.util.List;

public class DGErrorMsg {

	public static String buyErrMsg(String errCons) {
		List<String> errList = Arrays.asList("AGT_INS_BAL", "RET_INS_BAL",
				"REPRINT_FAIL", "PERFORMED", "LIMIT_EXCEEDED", "FRAUD",
				"RG_RPERINT", "NO_SALE", "NO_DRAWS", "ERROR_TICKET_LIMIT",
				"TKT_REG","INVALID_TKT", "TKT_CANCELLED", "RESULT_AWAITED",
				"WIN_TKT", "TKT_EXPIRED","LAST_TXN_NOT_SALE");

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
			errMsg = "Last Transaction Not Sale";
			break;
		case 3:
			errMsg = "Draw Performed";
			break;
		case 4:
			errMsg = "Reprint Limit Exceeded";
			break;
		case 5:
			errMsg = "Sale Limit Exceed";
			break;
		case 6:
			errMsg = "Reprint Limit Reached";
			break;
		case 7:
			errMsg = "Sale Not Allowed";
			break;
		case 8:
			errMsg = "Draw Not Available";
			break;
		case 9:
			errMsg = "Ticket Sale Limit Exceed";
			break;
		case 10:
			errMsg = "Ticket Already Registered";
			break;
		case 11:
			errMsg = "Invalid Ticket";
			break;
		case 12:
			errMsg = "Ticket Cancelled";
			break;
		case 13:
			errMsg = "Result Awaited";
			break;
		case 14:
			errMsg = "Winning Ticket";
			break;
		case 15:
			errMsg = "Ticket Expired";
			break;
		case 16:
			errMsg = "No Sale Transaction For Reprint";
			break;
		default:
			errMsg = "Error!Try Again";
			break;
		}

		return errMsg;
	}

	public static String buyErrorCode(String errCons){
		List<String> errList = Arrays.asList("AGT_INS_BAL", "RET_INS_BAL",
				"REPRINT_FAIL", "PERFORMED", "LIMIT_EXCEEDED", "FRAUD",
				"RG_RPERINT", "NO_SALE", "NO_DRAWS", "ERROR_TICKET_LIMIT",
				"TKT_REG","INVALID_TKT", "TKT_CANCELLED", "RESULT_AWAITED",
				"WIN_TKT", "TKT_EXPIRED");

		int errVal = errList.indexOf(errCons);
		String errMsg;
		switch (errVal) {
		case 0:
			errMsg = "107";//"Agent has insufficient Balance";
			break;
		case 1:
			errMsg = "108";//"Retailer has insufficient Balance";
			break;
		case 2:
			errMsg = "109";//"Last Transaction Not Sale";
			break;
		case 3:
			errMsg ="110";// "Draw Performed";
			break;
		case 4:
			errMsg = "111";//"Reprint Limit Exceeded";
			break;
		case 5:
			errMsg = "112";//"Sale Limit Exceed";
			break;
		case 6:
			errMsg = "113";//"Reprint Limit Reached";
			break;
		case 7:
			errMsg = "114";//"Sale Not Allowed";
			break;
		case 8:
			errMsg = "115";//"Draw Not Available";
			break;
		case 9:
			errMsg = "117";//"Ticket Sale Limit Exceed";
			break;
		case 10:
			errMsg = "118";//"Ticket Already Registered";
			break;
		case 11:
			errMsg = "119";//"Invalid Ticket";
			break;
		case 12:
			errMsg = "120";//"Ticket Cancelled";
			break;
		case 13:
			errMsg = "121";//"Result Awaited";
			break;
		case 14:
			errMsg = "122";//"Winning Ticket";
			break;
		case 15:
			errMsg = "123";//"Ticket Expired";
			break;
		default:
			errMsg = "116";//"Error!Try Again";
			break;
		}

		return errMsg;
	}
}
