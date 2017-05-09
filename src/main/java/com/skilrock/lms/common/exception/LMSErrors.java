package com.skilrock.lms.common.exception;

/**
 * 
 * @author stpl
 *Error Code 1001-2000 For LMS
 *Error Code 2001-3000 For Common
 *Error Code 3001-4000 For DGE
 *Error Code 4001-5000 For SE
 *Error Code 5001-6000 For CS
 *Error Code 10000-15000 for OLA
 */
public class LMSErrors {


	// LMS ERRORS
	//or Cash Payment
	public static final String DRAWER_NOT_ASSIGN_ERROR_MESSAGE = "You have not assign any Drawer.";
	public static final int DRAWER_NOT_ASSIGN_ERROR_CODE = 1001;
	
	public static final String CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE = "Invalidate Data in Agent Cash Payment.";
	public static final int CASH_PAYMENT_INVALIDATE_DATA_ERROR_CODE = 1002;
	
	public static final String RETAILER_CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE = "Invalidate Data in Retailer Cash Payment.";
	public static final int RETAILER_PAYMENT_INVALIDATE_DATA_ERROR_CODE = 1003;
		
	public static final String INVALIDATE_RETAILER_ERROR_MESSAGE = "Invalidate RETAILER!";
	public static final int INVALIDATE_RETAILER_ERROR_CODE= 1004;
	
	public static final String INVALIDATE_AGENT_ERROR_MESSAGE = "Invalidate AGENT!";
	public static final int INVALIDATE_AGENT_ERROR_CODE= 1005;
	
	public static final String AGENT_CASH_CONNECTION_ERROR_MESSAGE = "Error in Agent cash Connection Close";
	public static final int AGENT_CASH_CONNECTION_ERROR_CODE = 1006;

	public static final String RETAILER_CASH_CONNECTION_ERROR_MESSAGE = "Error in retailer cash Connection Close";
	public static final int RETAILER_CASH_CONNECTION_ERROR_CODE = 1007;
	
	public static final String INVALID_TICKET_ERROR_MESSAGE = "Invalid Ticket";
	public static final String INVALID_TICKET_ERROR_MESSAGE_SELL_TICKET = "Invalid Ticket or Ticket can not be sold !!";
	public static final int INVALID_TICKET_ERROR_CODE = 1008;
	
	public static final String INVALID_INPUT_RANGE_ERROR_MESSAGE = "Please Enter The Input Range Properly";
	public static final int INVALID_INPUT_RANGE_ERROR_CODE = 1009;
	
	public static final String INVALID_DATE_INPUT_RANGE_ERROR_MESSAGE = "Please Enter The Date Input Properly";
	public static final int INVALID_DATE_INPUT_RANGE_ERROR_CODE = 1010;
	
	public static final String INVALID_CANCEL_TICKET_DATA_ERROR_MESSAGE = "Ticket Cannot Be Cancelled ";
	public static final int INVALID_CANCEL_TICKET_DATA_ERROR_CODE = 1011;
	
	public static final String INVALID_CANCEL_PROMOTIONAL_TICKET_DATA_ERROR_MESSAGE = "Promotional Ticket Cannot Be Cancelled ";
	public static final int INVALID_CANCEL_PROMOTIONAL_TICKET_DATA_ERROR_CODE = 1012;
	
	public static final String INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_MESSAGE = "Select Reason for Cancellation. ";
	public static final int INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_CODE = 1013;
	
	public static final String BO_CANCEL_TICKET_DATA_ERROR_MESSAGE = "Select Reason for Cancellation. ";
	public static final int BO_CANCEL_TICKET_DATA_ERROR_CODE = 1014;
	
	public static final String BO_ADD_MESSAGING_ERROR_MESSAGE = "Problem Setting Advertisment Message. ";
	public static final int BO_ADD_MESSAGING_ERROR_CODE = 1015;
	
	public static final String RAMDOM_ID_GENERATION_PROCESS_ERROR_MESSAGE = "ALERT !!! Problem In Generate Random Id's.!!!! ";
	public static final int RAMDOM_ID_GENERATION_PROCESS_ERROR_CODE = 1016;
	
	public static final String RAMDOM_ID_GENERATION_ERROR_MESSAGE = "Generate Random Ids Some Other Time";
	public static final int RAMDOM_ID_GENERATION_ERROR_CODE = 1017;
	
	public static final String INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE = "Invalid Service !!!";
	public static final int INVALID_SERVICE_ON_TICKET_ERROR_CODE = 1018;
	
	public static final String LMS_ERROR_MESSAGE = "LMS Errors !!!";
	public static final int LMS_ERROR_CODE  = 1019;
	
	public static final String AGENT_BANK_DETAILS_ERROR_MESSAGE = "Error In Agent Bank Mapping !!!";
	public static final int AGENT_BANK_DETAILS_ERROR_CODE = 1020;
	
	public static final String DATA_UNAVAILABLE_ERROR_MESSAGE = "Error In Agent Bank Mapping !!!";
	public static final int DATA_UNAVAILABLE_ERROR_CODE = 1021;
	
	public static final String ACTIVE_TICKET_ERROR_MESSAGE = "Ticket is not active,can't marked as sold";
	public static final String TICKET_EXPIRE_ERROR_MESSAGE = "Ticket is expired !!";
	public static final String GAME_NOT_AVAILABLE_ERROR_MESSAGE = "Invalid Ticket Number !!";
	
	// COMMON ERRORS
	public static final String SQL_EXCEPTION_ERROR_MESSAGE = "SQL Exception !";
	public static final int SQL_EXCEPTION_ERROR_CODE = 2001;
	public static final String SQL_EXCEPTION_CODE = "2001";
	
	public static final String GENERAL_EXCEPTION_ERROR_MESSAGE = "Some Internal Error !";
	public static final int GENERAL_EXCEPTION_ERROR_CODE = 2002;
	public static final String GENERAL_EXCEPTION_CODE = "2002";
	
	public static final String COMPILE_TIME_ERROR_MESSAGE = "Compile Time Error !";
	public static final int COMPILE_TIME_ERROR_CODE = 2003;

	public static final String AUTHENTICATION_FAILED_EXCEPTION_ERROR_MESSAGE = "Authentication Failed Exception !";
	public static final int AUTHENTICATION_FAILED_EXCEPTION_ERROR_CODE = 2004;

	public static final String MESSAGING_EXCEPTION_ERROR_MESSAGE = "Messaging Exception !";
	public static final int MESSAGING_EXCEPTION_ERROR_CODE = 2005;
	
	public static final String CONNECTION_CLOSE_ERROR_MESSAGE = "Error in  Connection Close";
	public static final int CONNECTION_CLOSE_ERROR_CODE = 2006;
	
	public static final String SECURITY_BREACH_ERROR_MESSAGE = "JAVA SCRIPT DISABLED OR SECURITY BREACH ERROR";
	public static final int SECURITY_BREACH_ERROR_CODE = 2007;
	
	public static final String TRANSACTION_NOT_GENERATED_ERROR_MESSAGE = "Transaction id is not generated ";
	public static final int TRANSACTION_NOT_GENERATED_ERROR_CODE = 2008;
	
	public static final String RECIEPT_GENERATION_ERROR_MESSAGE = "Error in reciept generation ";
	public static final int RECIEPT_GENERATION_ERROR_CODE = 2009;
	
	public static final String BALANCE_UPDATION_ERROR_MESSAGE = "Error updating the organization Balance ";
	public static final int BALANCE_UPDATION_ERROR_CODE = 2010;
	
	public static final String NO_RECORD_FOUND_ERROR_MESSAGE = "No Record Found";
	public static final int NO_RECORD_FOUND_ERROR_CODE = 2011;
	
	public static final String INVALIDATE_TO_ARCHIVE_DATE_ERROR_MESSAGE = "Select Date After Archiving Date.";
	public static final int INVALIDATE_TO_ARCHIVE_DATE_ERROR_CODE = 2012;
		
	public static final String SESSION_EXPIRED_ERROR_MESSAGE = "Time Out. Login Again";
	public static final int SESSION_EXPIRED_ERROR_CODE = 2013;
	
	public static final String MORE_THAN_ONE_RECORD_MESSAGE = "More Than One Record Fetched";
	public static final int MORE_THAN_ONE_RECORD_CODE = 2014;
	
	public static final String IO_EXCEPTION_ERROR_MESSAGE = "Input/Output Exception !!!!";
	public static final int IO_EXCEPTION_ERROR_CODE = 2015;
	
	public static final String RG_LIMIT_EXCEPTION_ERROR_MESSAGE = "RG Limit Reached !!!";
	public static final int RG_LIMIT_EXCEPTION_ERROR_CODE = 2016;

	public static final String PLAYER_REGISTRATION_ERROR_MESSAGE = "Error in Player Registration.";
	public static final int PLAYER_REGISTRATION_ERROR_CODE = 2017;

	public static final String APPROVAL_REQUEST_INSERTION_ERROR_MESSAGE = "NO Data Inserted in st_pwt_approval_request_master table.";
	public static final int APPROVAL_REQUEST_INSERTION_ERROR_CODE = 2018;
	
	public static final String TRANSACTION_NOT_AVAILABLE_ERROR_MESSAGE = "Transaction Not Available at LMS end ";
	public static final int TRANSACTION_NOT_AVAILABLE_ERROR_CODE = 2008;
	
	public static final String FAILURE_AT_TIMEOF_TRANSACTION_ERROR_MESSAGE = "Failure at the Time of Transaction Process";
	public static final int FAILURE_AT_TIMEOF_TRANSACTION_ERROR_CODE = 2019;
	
	public static final String DUP_PAYMENT_ERROR_MESSAGE = "Request is already processed";
	public static final int DUP_PAYMENT_ERROR_CODE = 2020;

	public static final int INVALID_DATA_ERROR_CODE = 2021;
	public static final String INVALID_DATA_ERROR_MESSAGE = "Invalid Input.";
	
	public static final int DRAW_NOT_EXISTS_ERROR_CODE = 2022;
	public static final String DRAW_NOT_EXISTS_ERROR_MESSAGE = "Active Draws not available!";
	
	public static final int TRANSACTION_FAILED_ERROR_CODE = 2023;
	public static final String TRANSACTION_FAILED_ERROR_MESSAGE = "Transaction Failed.";
	
	public static final int TICKET_CANCELLED_ERROR_CODE = 2024;
	public static final String TICKET_CANCELLED_ERROR_MESSAGE = "Ticket Cancelled.";
	
	public static final int TICKET_EXPIRED_ERROR_CODE = 2025;
	public static final String TICKET_EXPIRED_ERROR_MESSAGE = "Ticket Expired.";
	
	public static final int UNAUTHORIZED_PWT_CLAIM_ERROR_CODE = 2026;
	public static final String UNAUTHORIZED_PWT_CLAIM_ERROR_MESSAGE = "Unauthorized to claim PWT.";

	public static final int HIGH_PRIZE_PWT_ERROR_CODE = 2026;
	public static final String HIGH_PRIZE_PWT_ERROR_MESSAGE = "High Prize PWT.";

	public static final int OUT_VERIFY_LIMIT_ERROR_CODE = 2027;
	public static final String OUT_VERIFY_LIMIT_ERROR_MESSAGE = "Pwt Amount more than Verify Limit.";

	public static final int OUT_PAY_LIMIT_ERROR_CODE = 2028;
	public static final String OUT_PAY_LIMIT_ERROR_MESSAGE = "Pwt Amount more than Pay Limit.";

	public static final String USER_NAME_DOES_NOT_EXISTS_MESSAGE = "User Name Doesn't Exists";
	public static final int USER_NAME_DOES_NOT_EXISTS_CODE = 2029;
	
	public static final int TICKET_ALREADY_CANCELLED_ERROR_CODE = 2030;
	public static final String TICKET_ALREADY_CANCELLED_ERROR_MESSAGE = "Ticket Already Cancelled.";
	
	public static final int UNAUTHORIZED_SALE_ERROR_CODE = 2031;
	public static final String UNAUTHORIZED_SALE_ERROR_MESSAGE = "Unauthorized to Sale Ticket.";
	
	public static final int LIMIT_REACHED_ERROR_CODE = 2032;
	public static final String LIMIT_REACHED_ERROR_MESSAGE = "Playing for this bet type is not possible for the current draw,Limit Reached";
	
	public static final int BET_SLIP_NOT_GENERATED_ERROR_CODE = 2033;
	public static final String BET_SLIP_NOT_GENERATED_ERROR_MESSAGE = "Request can not be processed , please try again after some time !";

	
	//DGE ERRORS
	
	public static final String DRAW_DOES_NOT_EXISTS_ERROR_MESSAGE = "Details not available as result is not declared yet !";
	public static final int DRAW_DOES_NOT_EXISTS_ERROR_CODE = 3013;
	
	public static final String DATA_ARCHIEVED_ERROR_MESSAGE = "Please select the Date After ";
	public static final int DATA_ARCHIEVED_ERROR_CODE = 3014;
	
	public static final String PURCHASE_FRAUD_ERROR_MESSAGE = "Sale Limit Reached !!!";
	public static final int PURCHASE_FRAUD_ERROR_CODE = 7001;
	
	// SE ERRORS
	public static final String SCRATCH_INVALID_TICKET_NUMBER_FORMAT_ERROR_MESSAGE = "Invalid Ticket Number Format Error !";
	public static final int SCRATCH_INVALID_TICKET_NUMBER_FORMAT_ERROR_CODE = 5001;

	public static final String SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_MESSAGE = "Invalid Ticket OR Virn Number !!!";
	public static final int SCRATCH_INVALID_TICKET_VIRN_NUMBER_ERROR_CODE = 5002;
	
	public static final String SCRATCH_NO_CHALLAN_AVAILABLE_TO_RECEIVE_ERROR_MESSAGE = "No DL Challan available!!";
	public static final int SCRATCH_NO_CHALLAN_AVAILABLE_TO_RECEIVE_ERROR_CODE = 5003;
	
	// Inventory ERRORS

	public static final String INVALID_INV_BIND_LENGTH_ERROR_MESSAGE = "Invalid Bind Length!";
	public static final int INVALID_INV_BIND_LENGTH_ERROR_CODE = 6001;
	public static final String INVALID_INV_ERROR_MESSAGE = "Invalid Inventory Please Check Inventory Details!";
	public static final int INVALID_INV_ERROR_CODE = 6002;
	public static final String DUP_INV_ERROR_MESSAGE = "Duplicate Inventory Please Check Inventory Details !";
	public static final int DUP_INV_ERROR_CODE = 6003;
	public static final String INV_LIST_EMPTY_ERROR_MESSAGE = "Please Provide atleast one Inventory through interface or file!";
	public static final int   INV_LIST_EMPTY_ERROR_CODE = 6004;
	public static final String INV_ALREADY_ASSIGNED_ERROR_MESSAGE = "Inventory Already Assigned to This User Please Return Before You Reassign!";
	public static final int  INV_ALREADY_ASSIGNED_ERROR_CODE = 6005;

	//for Ola Error codes
	public static final int  EXCEED_DEPOSIT_AMOUNT_REQUEST_ERROR_CODE = 10001;
	public static final String EXCEED_DEPOSIT_AMOUNT_REQUEST_ERROR_MESSAGE = "Deposit amount is greater then deposit limit";
	public static final int  INV_OLA_LIMITS_ERROR_CODE = 10002;
	public static final String INV_OLA_LIMITS_ERROR_MESSAGE = "OLA Limits Are Not defined Properly!!"; 
	public static final int  INSUFFICIENT_AGENT_BALANCE_ERROR_CODE = 10003;
	public static final String INSUFFICIENT_AGENT_BALANCE_ERROR_MESSAGE = "Agent has insufficient balance";
	public static final int  INSUFFICIENT_RETAILER_BALANCE_ERROR_CODE = 10004;
	public static final String INSUFFICIENT_RETAILER_BALANCE_ERROR_MESSAGE = "Retailer has insufficient Balance ";
	public static final int  DEPOSIT_MONEY_ERROR_CODE = 10005;
	public static final String DEPOSIT_MONEY_ERROR_MESSAGE = "error in Deposit the money";
	public static final int  BALANCE_VERIFICATION_ERROR_CODE = 10006;
	public static final String BALANCE_VERIFICATION_ERROR_MESSAGE = "Error During balance verification";
	public static final int  PLAYER_LOTTERY_ERROR_CODE = 10007;
	public static final String PLAYER_LOTTERY_ERROR_MESSAGE = "Error In Deposit. Amount Refunded Successfully";
	public static final int  DEPOSIT_REFUND_ERROR_CODE = 10008;
	public static final String DEPOSIT_REFUND_ERROR_MESSAGE = "Error In LMS Deposit Refund";
	public static final int  INSERT_ENTRY_AFFLIATE_BINDING_TABLE_ERROR_CODE = 10009;
	public static final String INSERT_ENTRY_AFFLIATE_BINDING_TABLE_ERROR_MESSAGE = "ERROR occured during inserting entry in plr affiliate binding table";
	public static final int  AFFILIATE_PLAYER_MAPPING_ERROR_CODE = 10010;
	public static final String AFFILIATE_PLAYER_MAPPING_ERROR_MESSAGE = "Player not mapped with this Affiliate in OLA";
	public static final int  DEPOSIT_ERROR_CODE = 10011;
	public static final String DEPOSIT_ERROR_MESSAGE = "Error During Deposit !!";
	public static final int  INV_PWT_LIMITS_ERROR_CODE = 10012;
	public static final String INV_PWT_LIMITS_ERROR_MESSAGE = "PWT Limits Are Not defined Properly!!";
	public static final int  EXCEED_WITHDRAWL_LIMIT_ERROR_CODE = 10013;
	public static final String EXCEED_WITHDRAWL_LIMIT_ERROR_MESSAGE = "withdrawl amount is greater then withdrawl limit";
	public static final int  PMS_CONNECTION_ERROR_CODE = 10014;
	public static final String PMS_CONNECTION_ERROR_MESSAGE = "Error In Connection With Player Lottery";
	public static final int  PMS_WITHDRAWL_DENY_ERROR_CODE = 10015;
	public static final String  PMS_WITHDRAWL_DENY_ERROR_MESSAGE = "Withdrawal Denied From Player Lottery";
	public static final int  MONEY_WITHDRAWL_ERROR_CODE = 10016;
	public static final String  MONEY_WITHDRAWL_ERROR_MESSAGE = "error in withdrawl the money (Transaction Id is not generated.)";
	public static final int  RMS_WITHDRAWL_ERROR_CODE = 10017;
	public static final String  RMS_WITHDRAWL_ERROR_MESSAGE = "Error in RMS";
	public static final int  WITHDRAWL_ERROR_CODE = 10018;
	public static final String VALID_PHONE_NUMBER_MESSAGE = "Mobile Number is Valid !!";
	public static final String INVALID_PHONE_NUMBER_MESSAGE = "Mobile Number is Invalid !! ";
	public static final String VALID_CARD_NUMBER_MESSAGE = "Card Number is Valid !!";
	public static final String INVALID_CARD_NUMBER_MESSAGE = "Card Number is Invalid !! ";
	public static final String EMAIL_ALREADY_EXIST_MESSAGE = "User with this email already exist !!";
	public static final String EMAIL_EXIST_MESSAGE = "Email is valid !! ";
	public static final String USER_NAME_AVAL_MESSAGE = "User Name is Available !!";
	public static final String USER_NAME_ALREADY_EXIST_MESSAGE = "User already Exist ";
	public static final String PHONE_NUM_AVAL_MESSAGE = "Mobile Num is valid !!";
	public static final String PHONE_NUM_ALREADY_EXIST_MESSAGE = "User with this mobile number already exist !! ";
	public static final String VERIFICATION_ERROR_MESSAGE = "Error occurred during verification. Please Try again !!";
	
	public static final int  INVALID_REF_CODE = 10021;
	public static final String INVALID_REF_CODE_MESSAGE = "Invalid Ref Code !!";
	public static final int  INVALID_RETAILER_ERROR_CODE = 10022;
	public static final String INVALID_RETAILER_ERROR_MESSAGE = "Retailer Not Exist !!";
	public static final int  INVALID_PLAYER_ERROR_CODE = 10023;
	public static final String INVALID_PLAYER_ERROR_MESSAGE = "Player Not Exist !!";
	public static final int  INVALID_AGENT_ERROR_CODE = 10024;
	public static final String INVALID_AGENT_ERROR_MESSAGE = "Agent Not Exist !!";
	public static final int  REG_BUT_NOT_DEPOSIT_ERROR_CODE = 10025;
	public static final String REG_BUT_NOT_DEPOSIT_ERROR_MESSAGE = "Player Registered Successfully. </br>Error Occured during Deposit";
	public static final int  PMS_REG_PLAYER_FAILED_ERROR_CODE = 10026;
	public static final String PMS_REG_PLAYER_FAILED_ERROR_MESSAGE = "Error in Player Registration.";
	public static final int  OLA_REG_ERROR_CODE = 10027;
	public static final String OLA_REG_ERROR_MESSAGE = "Error in Player Registration in OLA";
	public static final int INVALID_WITHDRAWL_VERIFICATION_ERROR_CODE = 10028;
	public static final String INVALID_WITHDRAWL_VERIFICATION_ERROR_MESSAGE = "Verification Code Mismatch..";
	public static final int INVALID_PHONE_NUMBER_ERROR_CODE = 10029;
	public static final String INVALID_PHONE_NUMBER_ERROR_MESSAGE = "Please Don't use Prefix 0 in Mobile Number.";
	public static final String MIN_DEPOSIT_LIMIT_ERROR_MESSAGE = "Deposit Amount should be Greater than or equal to ";
	public static final int MIN_DEPOSIT_LIMIT_ERROR_CODE = 10030;
	public static final String PLAYER_BLOCK_WDRWL_ERROR_MESSAGE = "Player is blocked so withdrawal can not be proceeded.";
	public static final int PLAYER_BLOCK_WDRWL_ERROR_CODE = 10031;
	public static final String PLAYER_BLOCK_DEPOSIT_ERROR_MESSAGE = "Player is blocked so deposit can not be proceeded.";
	public static final int PLAYER_BLOCK_DEPOSIT_ERROR_CODE = 10032;
	public static final String PLAYER_BINDING_ERROR_MESSAGE = "Error in player Binding!!";
	public static final int PLAYER_BINDING_ERROR_CODE = 10034;
	public static final String NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE = "No request data provided!!";
	public static final int NO_REQUEST_DATA_PROVIDED_ERROR_CODE = 10035;
	public static final String INVALID_DEVICEID_ERROR_MESSAGE = "Invalid Device Id provided!!";
	public static final int INVALID_DEVICEID_ERROR_CODE = 10036;
	public static final String INVALID_SALE_ERROR_MESSAGE = "Invalid sale Data provided!!";
	public static final int INVALID_SALE_ERROR_CODE = 10037;
	public static final String MAX_DEVICE_PER_RETAILER_ERROR_MESSAGE ="Maximum limits reached";
	public static final int MAX_DEVICE_PER_RETAILER_ERROR_CODE = 10038;
	public static final String INVALID_RETAILERID_ERROR_MESSAGE = "Invalid Retailer Id provided!!";
	public static final int INVALID_RETAILERID_ERROR_CODE = 10039;
	public static final int BET_SLIP_EXPIRED_ERROR_CODE = 10040;
	public static final String BET_SLIP_EXPIRED_ERROR_MESSAGE = "Bet slip expired!! ";
	public static final String INVALID_REQUESTID_ERROR_MESSAGE = "Invalid Request Id provided!!";
	public static final int INVALID_REQUESTID_ERROR_CODE = 10041;
}