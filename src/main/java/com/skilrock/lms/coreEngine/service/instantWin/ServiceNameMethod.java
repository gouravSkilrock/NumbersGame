package com.skilrock.lms.coreEngine.service.instantWin;

public final class ServiceNameMethod {

	public static final class ServiceName {
		public static final String DATA_MGMT = "dataMgmt";
		public static final String PCPOS_MGMT = "pcpos";
	}

	public static final class ServiceMethod {
		/* Methods For USER_MGT Service Start */
		/* Methods For USER_MGT Service End */

		/* Methods For ROLE_MGMT Service Start */
		/* Methods For ROLE_MGMT Service End */

		/* Methods For DATA_MGMT Service Start */
		public static final String FETCH_VERIFY_TKT_DATA = "service/retailer/terminal/BOWinVerificationCall?";
		public static final String PAY_WINNING_TKT = "service/retailer/terminal/BOWinClaimCall?";
		public static final String FETCH_GAME_DATA = "gameList";
		public static final String PURCHASE_PCPOS_TICKET = "purchase";
		public static final String WIN_VERIFY_TICKET = "winVerificationCall";
		public static final String PAY_PAY_TICKET = "winClaimCall";
		
		/* Methods For DATA_MGMT Service End */
	}
}