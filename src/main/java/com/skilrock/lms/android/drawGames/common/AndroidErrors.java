package com.skilrock.lms.android.drawGames.common;

import java.util.Locale;

import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class AndroidErrors extends LocalizedTextUtil {
	
	private static Locale locale = Locale.getDefault(); 
	
	public static final String CANCEL_DRAW_PERFORMED = "Invalid Ticket or Ticket can not be cancelled";

	public static final String CANCEL_FRAUD = "Cancel Ticket Limit Exceed";
	public static final String CANCEL_INVALID = "Invalid Ticket or Ticket can not be cancelled";
	public static final String DRAW_GAME_NOT_AVAILABLE = "Draw Game Not Available";
	public static final String DRAW_PERFORMED = "Draw Performed";
	public static final String FILE_UPLOAD_ERROR = "Error";
	public static final String FILE_UPLOAD_INVALID_DATA = "Please send proper data";
	public static final String FILE_UPLOAD_INVALID_USERID = "Invalid User Id";
	public static final String FILE_UPLOAD_LATE_UPLOAD = "Late Upload";

	public static final String FILE_UPLOAD_UPLOADED = "Uploaded";

	public static final String LIMIT_EXCEEDED = "Reprint Limit Exceeded";

	public static final String LOGIN_ALREADY_LOGGED_IN = "Already LOGGED IN.";
	public static final String LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY = "ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY";
	public static final String LOGIN_ERROR = "Enter Correct Username and Password";
	public static final String LOGIN_ERROR_INACTIVE = "Inactive user. Contact Agent/BO";
	public static final String LOGIN_INVALID_TERMINAL_ID = "Invalid Terminal Id";
	public static final String LOGIN_INVALID_USERNAME_PASSWORD = "Invalid UserName or Password";
	public static final String LOGIN_ONLY_RETAILER_ALLOWED = "Invalid UserName or Password";
	public static final String LOGIN_LIMIT_REACHED = "Login Attempts Finished";
	public static final String ERROR_TIME_LIMIT = "Login not Allowed";
	public static final String LOGIN_INVALID_SIM = "Login Not Allowed With This SIM";

	public static final String LOGIN_TIER_INACTIVE = "Inactive user. Contact Agent/BO";
	public static final String LOGOUT_ERROR = "File Upload Error. Contact Principal Agent/BO";
	public static final String PASSWORD_ERROR = "Error in Retype Password";
	public static final String PASSWORD_INCORRECT = "Incorrect Password";
	public static final String PASSWORD_INPUT = "Cannot Use Last 3 Passwords";

	public static final String PASSWORD_WRONG_PASS = "Incorrect Old Password";

	public static final String PURCHSE_AGT_INS_BAL = "Principal Agent has insufficient Balance";
	public static final String PURCHSE_FRAUD = "Sale Limit Reached";
	public static final String PURCHSE_INVALID_DATA = "Data Error";
	public static final String PURCHSE_RET_INS_BAL = "Sub-Agent has insufficient Balance";
	public static final String PWT_ERROR = "ERROR! Please Try Again.";
	public static final String PWT_FRAUD = "Cannot Verify.Invalid PWT";
	public static final String PWT_INVALID = "Invalid Ticket.";

	public static final String PWT_LIMIT_EXCEED = "PWT Limit Exceed";
	public static final String INVALID_PWT_LIMIT_EXCEED = "Invalid PWT Limit Exceed";
	public static final String PWT_OUT_VERIFY_LIMIT = "Cannot Verify.High Prize";
	public static final String PWT_TICKET_EXPIRED = "Expired or Invalid Ticket";

	public static final String PWT_UN_AUTH = "Cannot Verify.Invalid Sub-Agent";
	public static final String REPRINT_FAIL = "Last Transaction Not Sale";
	public static final String REPRINT_FRAUD = "Reprint Limit Reached";
	public static final String RESULT_DRAW_NOT_AVAILABLE = "Result has not been declared";

	public static final String RESULT_GAME_NOT_AVAILABLE = "Game Not Available";
	public static final String SESSION_EXPIRED = "Time Out. Login Again";
	public static final String ERROR_MSG = "Error!Try Again";
	public static final String RAFFLE_DATA = "RaffleData:";
	public static final String SALE_NOT_ALLOWED_ERROR = "Please Contact Your PA";
	public static final String OUT_ASSIGNED_LIMITS = "Out Of Assigned Limits";

	public static final String REPORT_DATA_NOT_AVAILABLE="No Transactions available";
	public static final String BAD_VERSION_ERROR_MESSAGE="Bad Version";
	
	//Embedded Errors for PayWorld Client 
	public static final String ERROR_101 = findDefaultText("error.balance.insuff", locale);	
	public static final String ERROR_102 = findDefaultText("error.parent.balance.insuff", locale);
	public static final String ERROR_103 = findDefaultText("error.wrong.username", locale);
	public static final String ERROR_104 = findDefaultText("error.wrong.auth.token", locale);
	public static final String ERROR_105 = findDefaultText("error.inactive.retailetr", locale);
	public static final String ERROR_106 = findDefaultText("error.inactive.parent", locale);
	public static final String ERROR_107 = findDefaultText("error.product.not.exist.in.rms", locale);
	public static final String ERROR_108 = findDefaultText("error.invalid.txn.id", locale);
	public static final String ERROR_109 = findDefaultText("error.invalid.lms.trans.id", locale);
	public static final String ERROR_110 = findDefaultText("error.improper.winlot.cost.calculation", locale); //jv cost
	public static final String ERROR_111 = findDefaultText("error.invalid.retailer.id", locale);
	public static final String ERROR_112 = findDefaultText("error.limit.reached",locale);
	public static final String ERROR_113 = findDefaultText("error.wrong.mrp.calculation", locale);
	public static final String ERROR_114 = findDefaultText("error.some.internal.server.error", locale);
	public static final String ERROR_115 = findDefaultText("error.wrong.unit.price", locale);
	public static final String ERROR_116 = findDefaultText("error.balance.insuff", locale);
	
	public static final String ERROR_117 = findDefaultText("error.update.txn", locale);
	// Error Codes For EmbeddedLogin 
	public static final int ERROR_000 =0;// Default
	public static final int ERROR_100 =100;// Success	
	public static final int ERROR_200 =200;//	
	public static final int ERROR_201 =201;//	
	public static final int ERROR_202 =202;//	
	public static final int ERROR_203 =203;//	
	public static final int ERROR_204 =204;//	
	
//	public static final String BAD_VER_ERROR_MESSAGE=findDefaultText("Bad Version", locale);
//	public static final int BAD_VER_ERROR_CODE=1015;
	
	public static final int SESSION_EXPIRED_ERROR_CODE=01;
	public static final int LOGIN_BLOCK_ERROR_CODE=124;
	public static final int NEGATIVE_BALANCE_ERROR_CODE=125;
	public static final int LOGIN_INVALID_USERNAME_PASSWORD_ERROR_CODE=126; 
	public static final int LOGIN_ERROR_INACTIVE_ERROR_CODE=127;
	public static final int LOGIN_ALREADY_LOGGED_IN_ERROR_CODE=128;
	public static final int LOGIN_TIER_INACTIVE_ERROR_CODE=129;
	public static final int LOGIN_LIMIT_REACHED_ERROR_CODE=130;
	public static final int ERROR_TIME_LIMIT_ERROR_CODE=131;
	public static final int LOGIN_INVALID_TERMINAL_ID_ERROR_CODE=132;
	public static final int LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY_ERROR_CODE=133;
	public static final int LOGIN_INVALID_SIM_ERROR_CODE=134;
	public static final int LOGIN_ERROR_ERROR_CODE=135;
	public static final int ERROR_MSG_ERROR_CODE=136;
	public static final int PURCHSE_INVALID_DATA_ERROR_CODE=137;
	
	public static final int BAD_VERSION_ERROR_CODE=138;
	
	public static final int DRAW_GAME_NOT_AVAILABLE_ERROR_CODE = 139;
	public static final int CANCEL_INVALID_ERROR_CODE = 140;
	
	public static final String UNAUTH_RET_FOR_THIS_TKT_ERROR_MSG="Unautorized Retailer for This Ticket";
	public static final int UNAUTH_RET_FOR_THIS_TKT_ERROR_CODE=141;
	
	public static final String CANCEL_TKT_LIMIT_EXCEED_ERROR_MSG="Cancel Ticket Limit Exceed";
	public static final int CANCEL_TKT_LIMIT_EXCEED_ERROR_CODE=142;
	
	public static final int RESULT_GAME_NOT_AVAILABLE_ERROR_CODE = 143;
	public static final int RESULT_DRAW_NOT_AVAILABLE_ERROR_CODE = 144;
	public static final int PWT_FRAUD_ERROR_CODE = 145;
	public static final int PWT_INVALID_ERROR_CODE = 146;
	public static final int PWT_UN_AUTH_ERROR_CODE = 147;
	public static final int PWT_OUT_VERIFY_LIMIT_ERROR_CODE = 148;
	public static final int PWT_TICKET_EXPIRED_ERROR_CODE = 149;
	public static final int PWT_LIMIT_EXCEED_ERROR_CODE = 150;
	public static final int PWT_ERROR_CODE = 151;
	public static final int DRAW_PERFORMED_ERROR_CODE = 152;
	public static final int REPRINT_FRAUD_ERROR_CODE = 153;
	public static final int REPRINT_FAIL_ERROR_CODE = 154;
	public static final int INVALID_PWT_LIMIT_EXCEED_ERROR_CODE = 155;
	
	public static final String TRY_AGAIN_ERROR_MSG="Error Try again";
	public static final String NO_ACTIVE_APPLICATION_AVAILABLE="No Active Application Available";
	public static final String REPRINT_FAIL_ERROR_MSG="REPRINT_FAIL";
	public static final String NO_RESULT_AVAILABLE_ERROR_MSG="No Result Available";
	public static final String DRAW_EXP_MSG="DRAW EXP,"; 
	public static final String VER_PND_MSG="VER PND,";
	public static final String AWAITED_MSG="Awaited,";
	public static final String TRY_AGAIN_MSG="TRY AGAIN,";
	public static final String REG_REQ_MSG="REG. REQ.,";
	public static final String CLAIMED_MSG="CLAIMED,";
	public static final String IN_PROCESS_MSG="IN PROCESS,";
	public static final String OUT_OF_VERIFY_MSG="OUT OF VERIFY,"; 
	public static final int TRY_AGAIN_ERROR_CODE=155;
	
	public static final int REPORTING_TIME_ERROR_CODE=156;
	public static final int PASSWORD_ERROR_CODE=157;
	public static final int PASSWORD_INPUT_ERROR_CODE=158;
	public static final int PASSWORD_INCORRECT_ERROR_CODE=159;
	public static final int NO_ACTIVE_APPLICATION_AVAILABLE_ERROR_CODE=160;
	public static final int REPRINT_FAIL_ERR_CODE=161;
	public static final int NO_RESULT_AVAILABLE_ERROR_CODE=162;
	public static final int DRAW_EXP_MSG_CODE=163;
	public static final int VER_PND_MSG_CODE=164;
	public static final int AWAITED_MSG_CODE=165;
	public static final int TRY_AGAIN_MSG_CODE=166;
	public static final int REG_REQ_MSG_CODE=167;
	public static final int CLAIMED_MSG_CODE=168;
	public static final int IN_PROCESS_MSG_CODE=169;
	public static final int OUT_OF_VERIFY_MSG_CODE=170;
	public static final int LIMIT_EXCEEDED_ERROR_CODE=171;

	public static final int NO_BANK_EXIST_ERROR_CODE = 172;
	public static final String NO_BANK_EXIST_ERROR_MESSAGE = "No Bank Exist";

	public static final int NO_RECORD_EXIST_ERROR_CODE = 173;
	public static final String NO_RECORD_EXIST_ERROR_MESSAGE = "No Record Exist";

	public static final int NEW_OLD_PASSWORD_SAME_ERROR_CODE = 174;
	public static final String NEW_OLD_PASSWORD_SAME_ERROR_MESSAGE = "Old and New Password are Same";

	public static final int SALE_TIME_EXPIRED_ERROR_CODE = 175;
	public static final String SALE_TIME_EXPIRED_ERROR_MESSAGE = "Sale Time Expired";
}