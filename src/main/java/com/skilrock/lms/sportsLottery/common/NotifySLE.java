package com.skilrock.lms.sportsLottery.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AuditTrailRequestBean;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RoleHeadDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.SubUserDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

public class NotifySLE extends Thread {
	static Log logger = LogFactory.getLog(NotifySLE.class);

	private int activityCode;
	private SLEDataFace requestData;

	public NotifySLE(int activityCode, SLEDataFace requestData) {
		this.activityCode = activityCode;
		this.requestData = requestData;
	}

	public SLEDataFace asyncCall(NotifySLE notifyData) throws SLEException {
		RolePrivilegeBean roleBean = null;
		AuditTrailRequestBean auditTrailRequestBean = null;
		PriviledgeModificationMasterBean modificationBean = null;
		switch (activityCode) {
			case SLE.Activity.GET_ROLE_PRIVILEGES:
				roleBean = SLENotificationManager.getRolePrivileges((RolePrivilegeBean) requestData);
				return roleBean;

			case SLE.Activity.GET_CREATE_ROLE_PRIVILEGES:
				roleBean = SLENotificationManager.getCreateRolePrivileges((RolePrivilegeBean) requestData);
				return roleBean;

			case SLE.Activity.GET_USER_PRIVILEGES:
				roleBean = SLENotificationManager.getUserPrivileges((RolePrivilegeBean) requestData);
				return roleBean;

			case SLE.Activity.GET_CREATE_USER_PRIVILEGES:
				roleBean = SLENotificationManager.getCreateUserPrivileges((RolePrivilegeBean) requestData);
				return roleBean;

			case SLE.Activity.ROLE_REGISTRATION:
				SLENotificationManager.roleRegistration((RolePrivilegeBean) requestData);
				break;

			case SLE.Activity.ROLE_EDIT:
				SLENotificationManager.roleEdit((RolePrivilegeBean) requestData);
				break;

			case SLE.Activity.ROLE_HEAD_REGISTRATION:
				SLENotificationManager.roleHeadRegistration((RoleHeadDataBean) requestData);
				break;

			case SLE.Activity.SUB_USER_REGISTRATION:
				SLENotificationManager.subUserRegistration((SubUserDataBean) requestData);
				break;

			case SLE.Activity.SUB_USER_EDIT:
				SLENotificationManager.subUserEdit((SubUserDataBean) requestData);
				break;

			case SLE.Activity.LAST_TICKET_INFO:
				SLENotificationManager.checkForAutoCancel((UserDataBean) requestData);
				break;

			case SLE.Activity.GET_AUDIT_TRAIL_DATA:
				auditTrailRequestBean = SLENotificationManager.fetchAuditTrailData((AuditTrailRequestBean) requestData);
				return auditTrailRequestBean;

			case SLE.Activity.FETCH_USER_PRIV_HISTORY:
				modificationBean = SLENotificationManager.fetchUserPriviledgeHistory((PriviledgeModificationMasterBean) requestData);
				return modificationBean;

			case SLE.Activity.GET_RETAILER_PRIVILEGES:
				roleBean = SLENotificationManager.getRetailerPrivileges((RolePrivilegeBean) requestData);
				return roleBean;
	
			case SLE.Activity.UPDATE_RETAILER_PRIVILEGES:
				SLENotificationManager.updateRetailerPrivileges((RolePrivilegeBean) requestData);

			default:
				break;
		}
		return null;
	}

	@Override
	public void run() {
		try {
			switch (activityCode) {
				case SLE.Activity.USER_REGISTER:
					SLENotificationManager.userRegistration((UserDataBean) requestData);
					break;

				case SLE.Activity.NOTIFY_ON_LOGIN:
					SLENotificationManager.notifyOnLogin((UserDataBean) requestData);
					break;

				case SLE.Activity.NOTIFY_ON_LOGOUT:
					SLENotificationManager.notifyOnLogout((UserDataBean) requestData);
					break;

				default:
					break;
			}
		} catch (SLEException e) {
			logger.info("Exception Occured - "+e.getErrorCode()+" - "+e.getErrorMessage());
		} catch (Exception e) {
			logger.info("Exception Occured - "+e.getMessage());
		}
	}
}