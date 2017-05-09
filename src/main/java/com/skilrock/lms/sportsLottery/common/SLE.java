package com.skilrock.lms.sportsLottery.common;

public final class SLE {
	public static final class Activity {
		public static final int USER_REGISTER = 101;
		public static final int NOTIFY_ON_LOGIN = 102;
		public static final int NOTIFY_ON_LOGOUT = 103;

		public static final int GET_ROLE_PRIVILEGES = 201;
		public static final int GET_CREATE_ROLE_PRIVILEGES = 202;
		public static final int GET_USER_PRIVILEGES = 203;
		public static final int GET_CREATE_USER_PRIVILEGES = 204;
		public static final int ROLE_REGISTRATION = 205;
		public static final int ROLE_EDIT = 206;
		public static final int ROLE_HEAD_REGISTRATION = 207;
		public static final int SUB_USER_REGISTRATION = 208;
		public static final int SUB_USER_EDIT = 209;
		public static final int LAST_TICKET_INFO = 210;
		public static final int GET_AUDIT_TRAIL_DATA = 211;
		public static final int FETCH_USER_PRIV_HISTORY = 212;
		public static final int GET_RETAILER_PRIVILEGES = 213;
		public static final int UPDATE_RETAILER_PRIVILEGES = 214;
	}

	public static final class Errors {
		public static final int GENERAL_EXCEPTION_CODE = 1001;
		public static final String GENERAL_EXCEPTION_MESSAGE = "Exception Occured.";
	}

	public static final class Status {
		public static final String NORMAL_PAY = "NORMAL_PAY";
		public static final String HIGH_PRIZE = "HIGH_PRIZE";
		public static final String MAS_APPROVAL = "MAS_APPROVAL";
		public static final String MAS_APPROVAL_INIT = "MAS_APPROVAL_INIT";
		public static final String MAS_APPROVAL_DONE = "MAS_APPROVAL_DONE";
	}
}