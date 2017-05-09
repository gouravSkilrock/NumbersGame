package com.skilrock.lms.coreEngine.sportsLottery.common;

public class SLEErrors {
	public static final String CANCEL_DRAW_PERFORMED = "Invalid Ticket or Ticket can not be cancelled|";

	public static final String CANCEL_FRAUD = "Cancel Ticket Limit Exceed|";
	public static final String CANCEL_INVALID = "Invalid Ticket or Ticket can not be cancelled|";
	public static final String DRAW_GAME_NOT_AVAILABLE = "Draw Game Not Available|";
	public static final String DRAW_PERFORMED = "Draw Performed|";
	public static final String FILE_UPLOAD_ERROR = "Error|";
	public static final String FILE_UPLOAD_INVALID_DATA = "Please send proper data|";
	public static final String FILE_UPLOAD_INVALID_USERID = "Invalid User Id|";
	public static final String FILE_UPLOAD_LATE_UPLOAD = "Late Upload|";

	public static final String FILE_UPLOAD_UPLOADED = "Uploaded|";

	public static final String LIMIT_EXCEEDED = "Reprint Limit Exceeded|";

	public static final String LOGIN_ALREADY_LOGGED_IN = "Already LOGGED IN.|";
	public static final String LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY = "ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY|";
	public static final String LOGIN_ERROR = "Enter Correct Username and Password|";
	public static final String LOGIN_ERROR_INACTIVE = "Inactive user. Contact Principal Agent/BO|";
	public static final String LOGIN_INVALID_TERMINAL_ID = "Invalid Terminal Id|";
	public static final String LOGIN_INVALID_USERNAME_PASSWORD = "Invalid UserName or Password|";
	public static final String LOGIN_ONLY_RETAILER_ALLOWED = "Invalid UserName or Password|";
	public static final String LOGIN_LIMIT_REACHED = "Login Attempts Finished|";

	public static final String LOGIN_TIER_INACTIVE = "Inactive user. Contact Principal Agent/BO|";
	public static final String LOGOUT_ERROR = "File Upload Error. Contact Principal Agent/BO|";
	public static final String PASSWORD_ERROR = "Error in Retype Password|";
	public static final String PASSWORD_INCORRECT = "Incorrect Password|";
	public static final String PASSWORD_INPUT = "Password Used Previously|";

	public static final String PASSWORD_WRONG_PASS = "Incorrect Password|";

	public static final String PURCHSE_AGT_INS_BAL = "Principal Agent has insufficient Balance|";
	public static final String PURCHSE_FRAUD = "Sale Limit Reached|";
	public static final String PURCHSE_INVALID_DATA = "Data Error|";
	public static final String PURCHSE_RET_INS_BAL = "Sub-Agent has insufficient Balance|";
	public static final String PWT_ERROR = "ERROR! Please Try Again.|";
	public static final String PWT_FRAUD = "Can Not Verify.Invalid PWT|";
	public static final String PWT_INVALID = "Invalid Ticket. Terminal would be inactivated |";

	public static final String PWT_LIMIT_EXCEED = "PWT Limit Exceed|";
	public static final String PWT_OUT_VERIFY_LIMIT = "Can Not Verify.High Prize|";
	public static final String PWT_TICKET_EXPIRED = "Expired or Invalid Ticket|";

	public static final String PWT_UN_AUTH = "Can Not Verify.Invalid Sub-Agent|";
	public static final String REPRINT_FAIL = "Last Transaction Not Sale|";
	public static final String REPRINT_FRAUD = "Reprint Limit Reached|";
	public static final String RESULT_DRAW_NOT_AVAILABLE = "Result has not been declared|";

	public static final String RESULT_GAME_NOT_AVAILABLE = "Game Not Available|";
	public static final String SESSION_EXPIRED = "Time Out. Login Again|";
	
	public static final String RAFFLE_DATA = "RaffleData:";
	
	public static final String REPORT_DATA_NOT_AVAILABLE="No Transactions available";
	
	//Embedded Errors for PayWorld Client 
	public static final String ERROR_101 = "Balance Insufficient";	
	public static final String ERROR_102 = "Parent Balance Insufficient";
	public static final String ERROR_103 = "Invalid UserName or Password";
	public static final String ERROR_104 = "Wrong Auth Token";
	public static final String ERROR_105 = "Inactive Player";
	public static final String ERROR_106 = "Inactive Parent";
	public static final String ERROR_107 = "product not exists in RMS";
	public static final String ERROR_108 = "Invalid Transaction Id";
	public static final String ERROR_109 = "Invalid LMS Trans Id";
	public static final String ERROR_110 = "improper Winlot Cost Calculation"; //jv cost
	public static final String ERROR_111 = "invalid retailer id";
	public static final String ERROR_112 = "Limit Reached";
	public static final String ERROR_113 = "Wrong MRP Calculation";
	public static final String ERROR_114 = "Some Internal server error";
	public static final String ERROR_115 = "Wrong Unit Price";
	public static final String ERROR_116 = "Balance Insufficient";
	
	public static final String ERROR_117 = "Error in updating Transaction";
	public static final String ERROR_118 = "Time Out. Login Again";
	public static final String ERROR_119 = "User Name already exist";
	public static final String ERROR_120 = "Draw Game Not Available|";
	public static final String ERROR_121 = "Duplicate Mobile Number";
	public static final String ERROR_123 = "Sale Time Expired";
	public static final String ERROR_124 = "Invalid User";


	public static final String RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE = "Retailer has Insufficient Balance";
	public static final int RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE = 101;

	public static final String AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE = "Agent has Insufficient Balance";
	public static final int AGENT_BALANCE_INSUFFICIENT_ERROR_CODE = 102;
	
	public static final String INVALID_USER_NAME_PASSWORD_ERROR_MESSAGE = "Invalid UserName or Password.";
	public static final int INVALID_USER_NAME_PASSWORD_ERROR_CODE = 103;

	public static final String BLOCK_PLAYER_LOGIN_ERROR_MESSAGE = "Your account is blocked";
	public static final int BLOCK_PLAYER_LOGIN_ERROR_CODE = 104;
	
	public static final String INACTIVE_PLAYER_ERROR_MESSAGE = "Inactive Player.";
	public static final int INACTIVE_PLAYER_ERROR_CODE = 105;

	public static final String TICKET_SALE_LIMIT_REACHED_ERROR_MESSAGE = "Ticket sale amount exceed";
	public static final int TICKET_SALE_LIMIT_REACHED_ERROR_CODE = 106;
	
	public static final String LOGIN_LIMIT_REACHED_ERROR_MESSAGE = "Login Limit reached";
	public static final int LOGIN_LIMIT_REACHED_ERROR_CODE = 112;
	
	public static final String ERROR_IN_UPDATING_TRANSACTION_MESSAGE = "Error in updating Transaction.";
	public static final int ERROR_IN_UPDATING_TRANSACTION_CODE = 117;

	public static final String SESSION_TIME_OUT_ERROR_MESSAGE = "Time Out. Login Again";
	public static final int SESSION_TIME_OUT_ERROR_CODE = 118;

	public static final String USER_NAME_ALREADY_EXIST_MESSAGE = "User Name already exist";
	public static final int USER_NAME_ALREADY_EXIST_CODE = 119;

	public static final String INVALID_TICKET_NUMBER_ERROR_MESSAGE = "Invalid Ticket No";
	public static final int INVALID_TICKET_NUMBER_ERROR_CODE = 121;

	public static final String INVALID_USER_NAME_MOBILENO_ERROR_MESSAGE = "Invalid Username or Mobile Number.";
	public static final int INVALID_USER_NAME_MOBILENO_ERROR_CODE = 122;

	public static final String INVALID_USER_NAME_MESSAGE = "Invalid User Name.";
	public static final int INVALID_USER_NAME_CODE = 124;

	public static final String INVALID_USER_NAME_EXIST_ERROR_MESSAGE = "User Name already Exist.";
	public static final int INVALID_USER_NAME_EXIST_ERROR_CODE = 127;
	
	public static final String INVALID_MOBILE_NO_MESSAGE = "Invalid Mobile Number.";
	public static final int INVALID_MOBILE_NO_CODE = 125;

	public static final String INVALID_MOBILE_NO_EXIST_ERROR_MESSAGE = "Mobile Number already Exist.";
	public static final int INVALID_MOBILE_NO_EXIST_ERROR_CODE = 128;
	
	public static final String INVALID_WITHDRAWL_VERIFICATION_ERROR_MESSAGE = "Verification Code Mismatch..";
	public static final int INVALID_WITHDRAWL_VERIFICATION_ERROR_CODE = 126;

	public static final String DEVICE_NOT_AVAILABLE_ERROR_MESSAGE = "Device Not Available";
	public static final int DEVICE_NOT_AVAILABLE_ERROR_CODE = 129;
	
	public static final String DEVICE_BINDING_ID_NOT_MATCH_ERROR_MESSAGE = "Binding id not match";
	public static final int DEVICE_BINDING_ID_NOT_MATCH_ERROR_CODE = 130;
	
	public static final String BINDING_VERIFICATION_CODE_NOT_MATCH_ERROR_MESSAGE = "Verification Code not match";
	public static final int BINDING_VERIFICATION_CODE_NOT_MATCH_ERROR_CODE = 131;
	
	public static final String BINDING_VERIFICATION_CODE_EXPIRED_ERROR_MESSAGE = "Verification Code Expired";
	public static final int BINDING_VERIFICATION_CODE_EXPIRED_ERROR_CODE = 132;

	public static final String SALE_ON_RESTRICT_BET_MULTIPLE_ERROR_MESSAGE = "ErrorMsg:Max Bet Amount Multiple ";
	public static final int SALE_ON_RESTRICT_BET_MULTIPLE_ERROR_CODE = 133;
	
	public static final String SALE_ON_RESTRICT_BET_TIME_ERROR_MESSAGE = "ErrorMsg:Server Busy Amount Not Deducted";
	public static final int SALE_ON_RESTRICT_BET_TIME_ERROR_CODE = 134;

	public static final String INVALID_MESSAGE_CONTENT_LENGTH_ERROR_MESSAGE = "Message Length is too long.";
	public static final int INVALID_MESSAGE_CONTENT_LENGTH_ERROR_CODE = 135;

	public static final String INVALID_DESCRIPTION_CONTENT_LENGTH_ERROR_MESSAGE = "Description Length is too long.";
	public static final int INVALID_DESCRIPTION_CONTENT_LENGTH_ERROR_CODE = 136;

	public static final String PAGA_PAYMENT_REQUEST_INITIATED_ERROR_MESSAGE = "Payment request Not Intiated Successfully";
	public static final int PAGA_PAYMENT_REQUEST_INITIATED_ERROR_CODE = 137;

	public static final String PAGA_PAYMENT_REQUESTID_NOT_EXIST_ERROR_MESSAGE = "Payment request Not Intiated Successfully";
	public static final int PAGA_PAYMENT_REQUESTID_NOT_EXIST_ERROR_CODE = 138;

	public static final String PAGA_PAYMENT_RESPONSE_NOT_SUCCESS_ERROR_MESSAGE = "Payment request Not Intiated Successfully";
	public static final int PAGA_PAYMENT_RESPONSE_NOT_SUCCESS_ERROR_CODE = 139;
	
	public static final String DRAW_PERFORMED_ERROR_MESSAGE = "Draw Performed";
	public static final int DRAW_PERFORMED_ERROR_CODE = 151;

	public static final String SALE_NOT_ALLOWED_ERROR_MESSAGE = "Sale Not Allowed";
	public static final int SALE_NOT_ALLOWED_ERROR_CODE = 152;

	public static final String DRAW_NOT_AVAILABLE_ERROR_MESSAGE = "Draw Not Available";
	public static final int DRAW_NOT_AVAILABLE_ERROR_CODE = 153;

	public static final String ERROR_TRY_AGAIN_ERROR_MESSAGE = "Error!Try Again";
	public static final int ERROR_TRY_AGAIN_ERROR_CODE = 154;

	public static final String GAME_NOT_AVAILABLE_ERROR_MESSAGE = "Draw Game Not Available";
	public static final int GAME_NOT_AVAILABLE_ERROR_CODE = 155;

	public static final String CANCEL_INVALID_ERROR_MESSAGE = "Invalid Ticket or Ticket can not be cancelled|";
	public static final int CANCEL_INVALID_ERROR_CODE = 156;

	public static final String INVALID_DEVICE_ERROR_MESSAGE = "Invalid Device name or Type";
	public static final int INVALID_DEVICE_ERROR_CODE = 157;
	
	public static final String INVALID_KENO_DATA_ERROR_MESSAGE = "Keno Validation data Error";
	public static final int INVALID_KENO_DATA_ERROR_CODE = 158;
	
	public static final String DRAW_CLAIM_HOLD_ON_PAY_PWT_ERROR_MESSAGE = "High Prize and claim hold";
	public static final int DRAW_CLAIM_HOLD_ON_PAY_PWT_ERROR_CODE = 159;
	
	public static final String INVALID_BO_USER_NAME_PASSWORD_ERROR_MESSAGE = "Invalid UserName or Password.";
	public static final int INVALID_BO_USER_NAME_PASSWORD_ERROR_CODE = 301;
	
	public static final String INVALID_BO_USER_NAME_ERROR_MESSAGE = "Invalid User Name.";
	public static final int INVALID_BO_USER_NAME_ERROR_CODE = 302;
	
	public static final String INVALID_BO_USER_NAME_EXIST_ERROR_MESSAGE = "User Name already Exist.";
	public static final int INVALID_BO_USER_NAME_EXIST_ERROR_CODE = 303;
	
	public static final String INVALID_ROLE_NAME_EXIST_ERROR_MESSAGE = "Role already Exist.";
	public static final int INVALID_ROLE_NAME_EXIST_ERROR_CODE = 304;
	
	public static final String NO_ROLE_PRIVILEDGE_EXIST_ERROR_MESSAGE = "Please Select at least one priviledge";
	public static final int NO_ROLE_PRIVILEDGE_EXIST_ERROR_CODE = 305;
	
	public static final String NO_USER_PRIVILEDGE_EXIST_ERROR_MESSAGE = "Please Select at least one priviledge";
	public static final int NO_USER_PRIVILEDGE_EXIST_ERROR_CODE = 306;

	public static final String PLAYER_ALREADY_BLOCKED_ERROR_MESSAGE = "Player is Already Blocked";
	public static final int PLAYER_ALREADY_BLOCKED_ERROR_CODE = 307;

	public static final String INVALID_PASSWORD_ERROR_MESSAGE = "Invalid Password";
	public static final int INVALID_PASSWORD_ERROR_CODE = 308;

	public static final String WEAK_PASSWORD_STRENGTH_ERROR_MESSAGE = "Password Strength is too weak";
	public static final int WEAK_PASSWORD_STRENGTH_ERROR_CODE = 309;

	public static final String BOTH_PASSWORD_SAME_ERROR_MESSAGE = "Old password and New password Cannot be same";
	public static final int BOTH_PASSWORD_SAME_ERROR_CODE = 310;

	public static final String ACCOUNT_BLOCK_BY_BO_ERROR_MESSAGE = "Account is Blocked by Back Office.";
	public static final int ACCOUNT_BLOCK_BY_BO_ERROR_CODE = 311;

	public static final String SQL_EXCEPTION_ERROR_MESSAGE = "SQL Exception !";
	public static final int SQL_EXCEPTION_ERROR_CODE = 501;

	public static final String GENERAL_EXCEPTION_ERROR_MESSAGE = "Some Internal Error !";
	public static final int GENERAL_EXCEPTION_ERROR_CODE = 502;

	public static final String COMPILE_TIME_ERROR_MESSAGE = "Compile Time Error !";
	public static final int COMPILE_TIME_ERROR_CODE = 503;

	public static final String AUTHENTICATION_FAILED_EXCEPTION_ERROR_MESSAGE = "Authentication Failed Exception !";
	public static final int AUTHENTICATION_FAILED_EXCEPTION_ERROR_CODE = 504;

	public static final String MESSAGING_EXCEPTION_ERROR_MESSAGE = "Messaging Exception !";
	public static final int MESSAGING_EXCEPTION_ERROR_CODE = 505;
	
	public static final String SLE_EXCEPTION_ERROR_MESSAGE = "Some Internal Error !";
	public static final int SLE_EXCEPTION_ERROR_CODE = 506;
	
	public static final String PURCHASE_FRAUD_ERROR_MESSAGE = "Sale Limit Exceed !";
	public static final int PURCHASE_FRAUD_ERROR_CODE = 507;
}