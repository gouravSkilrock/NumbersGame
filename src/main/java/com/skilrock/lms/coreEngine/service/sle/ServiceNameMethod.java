package com.skilrock.lms.coreEngine.service.sle;

public final class ServiceNameMethod {

	public static final class ServiceName {
		public static final String USER_MGMT = "userMgmt";
		public static final String ROLE_MGMT = "userMgmt";
		public static final String DATA_MGMT = "dataMgmt";
		
		public static final String WRAPPER_USER_MGMT = "tpUserMgmt";
	}

	public static final class ServiceMethod {
		/* Methods For USER_MGT Service Start */
		public static final String USER_REGISTER = "userRegistration";
		public static final String NOTIFY_ON_LOGIN = "userLoginNotify";
		public static final String NOTIFY_ON_LOGOUT = "userLogoutNotify";
		public static final String WRAPPER_USER_REGISTER = "registerUser";
		/* Methods For USER_MGT Service End */

		/* Methods For ROLE_MGMT Service Start */
		public static final String GET_ROLE_PRIVILEGES = "getRolePriviledge";
		public static final String GET_CREATE_ROLE_PRIVILEGES = "getCreateRoleUserPriviledge";
		public static final String GET_USER_PRIVILEGES = "getSubUserPriviledge";
		public static final String GET_RETAILER_PRIVILEGES = "getRetailerPrivilege";
		public static final String GET_CREATE_USER_PRIVILEGES = "getCreateUserPriviledge";
		public static final String FETCH_USER_PRIV_HISTORY = "fetchUserPriviledgeHistory";

		public static final String ROLE_REGISTER = "createRole";
		public static final String ROLE_EDIT = "editRole";
		public static final String ROLE_HEAD_REGISTER = "createRoleHead";
		public static final String SUB_USER_REGISTER = "createSubUser";
		public static final String SUB_USER_EDIT = "editSubUser";
		public static final String UPDATE_RETAILER_PRIVILEGES = "updateRetailerPrivilege";
		/* Methods For ROLE_MGMT Service End */

		/* Methods For DATA_MGMT Service Start */
		public static final String CHECK_FOR_AUTO_CANCEL = "checkForAutoCancel";
		public static final String FETCH_AUDIT_TRAIL_DATA = "fetchAuditTrailData";
		/* Methods For DATA_MGMT Service End */
	}
}