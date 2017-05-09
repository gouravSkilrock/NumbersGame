package com.skilrock.lms.instantWin.common;

public final class IW {
	public static final class Activity {
		public static final int FETCH_VERIFY_TKT_DATA = 101;
		public static final int PAY_WINNING_TKT = 102;
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