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

package com.skilrock.lms.common.db;

public class TableConstants {

	// st_bo_receipt table constant

	public static final String AGENT_ORG_ID = "agent_org_id";
	public static final String AUTO_PASSWORD = "auto_password";

	public static final String BO_RECEIPT_ID = "receipt_id";
	public static final String BO_RECEIPT_TYPE = "receipt_type";
	public static final String BOOK_NBR_DIGITS = "book_nbr_digits";
	public static final String BOOKS_PER_PACK = "nbr_of_books_per_pack";
	public static final String CHEQUE_AMT = "cheque_amt";
	public static final String CHEQUE_DATE = "cheque_date";
	public static final String CHEQUE_NUMBER = "cheque_nbr";
	public static final String COUNTRY = "country";
	public static final String COUNTRY_CODE = "country_code";
	public static final String CREDIT_LIMIT = "credit_limit";
	public static final String CURRENT_CREDIT_AMT = "current_credit_amt";
	public static final String DRAWEE_BANK = "drawee_bank";
	public static final String EMAIL = "email_id";
	// Hanuman's Constant------------------------------------------------
	public static final String END_DATE = "end_date";

	public static final String EXTENDED_CREDIT_LIMIT = "extended_credit_limit";
	public static final String FIRST_NAME = "first_name";
	public static final String FIXED_AMT = "fixed_amt";
	// Vishal's Constants--------------------------------------------------
	public static final String GAME_ID = "game_id";
	public static final String GAME_NAME = "game_name";
	public static final String GAME_NBR = "game_nbr";
	public static final String GAME_STATUS = "game_status";
	public static final String GOVT_COMM_RATE = "govt_comm_rate";
	public static final String GOVT_COMM_TYPE = "govt_comm_type";

	public static final String ISSUE_PARTY_NAME = "issuing_party_name";

	public static final String LAST_NAME = "last_name";
	public static final String MONTH_TASK = "month";
	public static final String NAME = "name";
	public static final String NBR_OF_BOOKS = "nbr_of_books";

	// Yogesh's Constants-------------------------------------------------------
	public static final String NO_OF_BOOKS = "nbr_of_books";
	public static final String NO_OF_BOOKS_APP = "nbr_of_books_appr";
	public static final String NO_OF_TICKETS = "nbr_of_tickets";
	public static final String ORG_ADDR1 = "addr_line1";

	public static final String ORG_ADDR2 = "addr_line2";
	public static final String ORG_CITY = "city";
	public static final String ORG_COUNTRY = "country_code";
	public static final String ORG_ID = "organization_id";
	public static final String ORG_NAME = "name";

	public static final String ORG_PIN = "pin_code";
	public static final String ORG_STATE = "state_code";
	public static final String ORG_STATUS = "organization_status";
	public static final String ORG_TYPE = "organization_type";

	public static final String ORGANIZATION_ID = "organization_id";
	public static final String ORGANIZATION_NAME = "name";
	public static final String ORGANIZATION_STATUS = "organization_status";

	// public static final String CREDIT_LIMIT="credit_limit";
	public static final String ORGANIZATION_TYPE = "organization_type";
	public static final String PACK_NBR_DIGITS = "pack_nbr_digits";
	public static final String PARENT_ORGANIZATION_ID = "parent_id";
	public static final String PARTY_ID = "party_id";
	public static final String PHONE = "phone_nbr";
	public static final String MOBILE = "mobile_nbr";

	// *********st_se_game_ticket_nbr_format**************

	public static final String PLAYER_ADDR1 = "addr1";
	public static final String PLAYER_ADDR2 = "addr2";
	public static final String PLAYER_CITY = "city";
	public static final String PLAYER_COUNTRY = "country";

	public static final String PLAYER_EMAIL = "email";
	// //--START---Player constants by Hanuman Mishra
	public static final String PLAYER_FIRSTNAME = "firstName";
	public static final String PLAYER_IDNUMBER = "idNumber";
	public static final String PLAYER_IDTYPE = "idType";
	public static final String PLAYER_LASTNAME = "lastName";

	public static final String PLAYER_PHONE = "phone";
	public static final String PLAYER_PIN = "pin";
	public static final String PLAYER_STATE = "state";
	public static final String PLR_FIRSTNAME = "first_name";
	public static final String PLR_ID = "player_id";
	public static final String PLR_LASTNAME = "last_name";
	public static final String PRICE = "ticket_price";
	public static final String PRIZE_AMT = "prize_amt";
	public static final String PRIZE_LEVEL = "prize_level";
	// new
	public static final String PRIZES_REMAINING = "No of Prizes Remaining";
	public static final String PWT_END_DATE = "pwt_end_date";
	public static final String PWT_RECEIPT_ID = "pwt_receipt_id";
	public static final String PWT_STATUS = "status";
	public static final String RANK_NBR = "rank_nbr";
	public static final String Register_DATE = "registration_date";
	public static final String ROLE_ID = "role_id";

	public static final String ROLE_NAME = "role_name";
	public static final String SALE_END_DATE = "sale_end_date";
	public static final String SAO_AGENT_ID = "agent_user_id";
	public static final String SAO_ORDER_DATE = "order_date";
	// *****************st_se_agent_order************
	public static final String SAO_ORDER_ID = "order_id";
	public static final String SAO_RETAILER_ID = "retailer_user_id";
	public static final String SAO_RETAILER_ORG_ID = "retailer_org_id";
	public static final String SAOG_GAME_ID = "game_id";
	public static final String SAOG_NBR_OF_BOOKS_APP = "nbr_of_books_appr";
	// **************st_se_bo_ordered_games**************
	public static final String SAOG_ORDER_ID = "order_id";
	public static final String SBO_AGENT_ORG_ID = "agent_org_id";
	public static final String SBO_ORDER_DATE = "order_date";
	// *****************st_se_bo_order************
	public static final String SBO_ORDER_ID = "order_id";
	public static final String SBO_ORDER_STATUS = "order_status";
	public static final String SBOG_GAME_ID = "game_id";
	public static final String SBOG_NBR_OF_BOOKS_APP = "nbr_of_books_appr";
	public static final String SBOG_NBR_OF_BOOKS_DLVRD = "nbr_of_books_dlvrd";
	// **************st_se_bo_ordered_games**************
	public static final String SBOG_ORDER_ID = "order_id";
	public static final String SEC_ANS = "secret_ans";
	public static final String SEC_QUES1 = "secret_ques";
	public static final String SGIS_BOOK_NBR = "book_nbr";
	public static final String SGIS_CURR_OWNER = "current_owner";
	public static final String SGIS_CURR_OWNER_ID = "current_owner_id";
	// ************st_se_game_inv_status**********
	public static final String SGIS_GAME_ID = "game_id";
	public static final String SGIS_PACK_NBR = "pack_nbr";
	public static final String SGIS_WAREHOUSE_ID = "warehouse_id";
	public static final String SGM_AGT_PWT_RATE = "agent_pwt_comm_rate";

	public static final String SGM_AGT_SALE_RATE = "agent_sale_comm_rate";
	// Aman's
	// Constants----------------------------------------------------------
	// *********st_se_game_master**********
	public static final String SGM_GAME_ID = "game_id";
	public static final String SGM_GAME_NAME = "game_name";
	public static final String SGM_GAME_NBR = "game_nbr";
	public static final String SGM_NBR_OF_BOOKS = "nbr_of_books";
	public static final String SGM_NBR_OF_BOOKS_PER_PACK = "nbr_of_books_per_pack";
	public static final String SGM_NBR_OF_TICKETS_PER_BOOK = "nbr_of_tickets_per_book";
	// *********st_se_game_master**********
	public static final String SGM_NBR_OF_TICKETS_PER_PACK = "nbr_of_tickets_per_book";
	// //hanuman
	public static final String SGM_PWT_END_DATE = "pwt_end_date";
	public static final String SGM_RET_PWT_RATE = "retailer_pwt_comm_rate";
	public static final String SGM_RET_SALE_RATE = "retailer_sale_comm_rate";
	public static final String SGM_SALE_END_DATE = "sale_end_date";
	public static final String SGM_START_DATE = "start_date";
	public static final String SGM_TICKET_PRICE = "ticket_price";
	public static final String SGTNF_BOOK_DIGITS = "book_nbr_digits";
	public static final String SGTNF_GAME_ID = "game_id";
	public static final String SGTNF_GAME_NBR_DIGITS = "game_nbr_digits";
	public static final String SGTNF_PACK_DIGITS = "pack_nbr_digits";
	public static final String SOM_ADDR_LINE1 = "addr_line1";
	public static final String SOM_ADDR_LINE2 = "addr_line2";
	public static final String SOM_AVAILABLE_CREDIT = "available_credit";
	public static final String SOM_CITY = "city";
	public static final String SOM_CREDIT_LIMIT = "credit_limit";
	// new
	public static final String SOM_CURR_CREDIT_AMT = "current_credit_amt";
	// ************* st_lms_organization_master*********
	public static final String SOM_ORG_ID = "organization_id";
	public static final String SOM_ORG_NAME = "name";
	public static final String SOM_ORG_TYPE = "organization_type";
	public static final String SPI_NET_AMT = "net_amt";
	// new
	public static final String SPI_PRIZE_LEVEL = "prize_level";
	public static final String SPI_PWT_AMT = "pwt_amt";
	// ************st_pwt_inv***************
	public static final String SPI_VIRN_CODE = "virn_code";
	public static final String START_DATE = "start_date";
	public static final String STATE = "state";
	public static final String STATE_CODE = "state_code";
	public static final String STATUS = "status";
	// *************st_lms_user_master****************
	public static final String SUM_USER_ID = "user_id";
	public static final String SUPPLIER_ID = "supplier_id";
	public static final String SUPPLIER_NAME = "name";
	public static final String TASK_AMOUNT = "amount";

	public static final String TASK_APPROVE_DATE = "approve_date";
	public static final String TASK_ID = "task_id";
	public static final String TASK_STATUS = "status";

	public static final String TASK_TYPE = "transaction_type";

	public static final String TAX_AMOUNT = "tax_amt";
	public static final String TICKET_PRICE = "ticket_price";
	// Gaurav's Constants------------------------------------------------------
	public static final String TICKETS_PER_BOOK = "nbr_of_tickets_per_book";
	public static final String TIER_ID = "tier_id";
	// ***************************
	public static final String TOTAL = "total";
	public static final String TRANC_DATE = "transaction_date";
	public static final String TRANSACTION_DATE = "transaction_date";
	public static final String TRANSACTION_ID = "transaction_id";
	public static final String USER_ID = "user_id";
	public static final String MAPPING_ID = "user_mapping_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_PASSWORD = "password";
	public static final String USER_STATUS = "status";

	// /// END

}
