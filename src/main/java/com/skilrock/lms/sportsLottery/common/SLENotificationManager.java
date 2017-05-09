package com.skilrock.lms.sportsLottery.common;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.AuditTrailBean;
import com.skilrock.lms.beans.AuditTrailRequestBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationMasterBean;
import com.skilrock.lms.coreEngine.service.ServiceDelegateSLE;
import com.skilrock.lms.coreEngine.service.sle.ServiceNameMethod;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RoleHeadDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.SubUserDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

class SLENotificationManager {
	private static Log logger = LogFactory.getLog(SLENotificationManager.class);

	private static ServiceRequest sReq = null;

	static {
		sReq = new ServiceRequest();
	}

	private SLENotificationManager()
	{
	}

	static void userRegistration(UserDataBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.USER_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.USER_REGISTER);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void notifyOnLogin(UserDataBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.USER_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.NOTIFY_ON_LOGIN);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void notifyOnLogout(UserDataBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.USER_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.NOTIFY_ON_LOGOUT);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static RolePrivilegeBean getRolePrivileges(RolePrivilegeBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.GET_ROLE_PRIVILEGES);
			requestObject.put("roleId", dataBean.getRoleId());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			//	Create
			//responseString = "[{\"interfaceDispName\": \"Web\",\"interfaceDevName\": \"WEB\",\"menuList\": [{\"menuId\": 1,\"menuName\": \"REPORTS\",\"menuItems\": [{\"menuItemId\": 1,\"menuItemName\": \"Draw Game Report\",\"isAssign\": false},{\"menuItemId\": 2,\"menuItemName\": \"Panel Wise Report\",\"isAssign\": false}]},{\"menuId\": 2,\"menuName\": \"PWT\",\"menuItems\": [{\"menuItemId\": 1,\"menuItemName\": \"PWT Verify\",\"isAssign\": false},{\"menuItemId\": 2,\"menuItemName\": \"PWT Claim\",\"isAssign\": false}]}]},{\"interfaceDispName\": \"Terminal\",\"interfaceDevName\": \"TERMINAL\",\"menuList\": [{\"menuId\": 3,\"menuName\": \"DRAW_MGT\",\"menuItems\":[{\"menuItemId\": 1,\"menuItemName\": \"Sale\",\"isAssign\": false},{\"menuItemId\": 2,\"menuItemName\": \"Reprint\",\"isAssign\": false}]}]}]";
			//	Edit
			//responseString = "[{\"interfaceDispName\": \"Web\",\"interfaceDevName\": \"WEB\",\"menuList\": [{\"menuId\": 1,\"menuName\": \"REPORTS\",\"menuItems\": [{\"menuItemId\": 1,\"menuItemName\": \"Draw Game Report\",\"isAssign\": false},{\"menuItemId\": 2,\"menuItemName\": \"Panel Wise Report\",\"isAssign\": true}]},{\"menuId\": 2,\"menuName\": \"PWT\",\"menuItems\": [{\"menuItemId\": 1,\"menuItemName\": \"PWT Verify\",\"isAssign\": false},{\"menuItemId\": 2,\"menuItemName\": \"PWT Claim\",\"isAssign\": true}]}]},{\"interfaceDispName\": \"Terminal\",\"interfaceDevName\": \"TERMINAL\",\"menuList\": [{\"menuId\": 3,\"menuName\": \"DRAW_MGT\",\"menuItems\":[{\"menuItemId\": 1,\"menuItemName\": \"Sale\",\"isAssign\": true},{\"menuItemId\": 2,\"menuItemName\": \"Reprint\",\"isAssign\": false}]}]}]";

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privilegeString = responseObject.get("privData").getAsJsonArray().toString();
			List<PrivilegeDataBean> privilegeList = new Gson().fromJson(privilegeString, new TypeToken<List<PrivilegeDataBean>>() {}.getType());
			dataBean.setPrivilegeList(privilegeList);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return dataBean;
	}

	static RolePrivilegeBean getCreateRolePrivileges(RolePrivilegeBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.GET_CREATE_ROLE_PRIVILEGES);
			requestObject.put("userId", dataBean.getCreatorUserId());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privilegeString = responseObject.get("privData").getAsJsonArray().toString();
			List<PrivilegeDataBean> privilegeList = new Gson().fromJson(privilegeString, new TypeToken<List<PrivilegeDataBean>>() {}.getType());
			dataBean.setPrivilegeList(privilegeList);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return dataBean;
	}

	static RolePrivilegeBean getUserPrivileges(RolePrivilegeBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.GET_USER_PRIVILEGES);
			requestObject.put("userId", dataBean.getUserId());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privilegeString = responseObject.get("privData").getAsJsonArray().toString();
			List<PrivilegeDataBean> privilegeList = new Gson().fromJson(privilegeString, new TypeToken<List<PrivilegeDataBean>>() {}.getType());
			dataBean.setPrivilegeList(privilegeList);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return dataBean;
	}

	static RolePrivilegeBean getRetailerPrivileges(RolePrivilegeBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.GET_RETAILER_PRIVILEGES);
			requestObject.put("userId", dataBean.getUserId());
			requestObject.put("merCode", "RMS");
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privilegeString = responseObject.get("privData").getAsJsonArray().toString();
			List<PrivilegeDataBean> privilegeList = new Gson().fromJson(privilegeString, new TypeToken<List<PrivilegeDataBean>>() {}.getType());
			dataBean.setPrivilegeList(privilegeList);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return dataBean;
	}

	static RolePrivilegeBean getCreateUserPrivileges(RolePrivilegeBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.GET_CREATE_USER_PRIVILEGES);
			requestObject.put("userId", dataBean.getCreatorUserId());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privilegeString = responseObject.get("privData").getAsJsonArray().toString();
			List<PrivilegeDataBean> privilegeList = new Gson().fromJson(privilegeString, new TypeToken<List<PrivilegeDataBean>>() {}.getType());
			dataBean.setPrivilegeList(privilegeList);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return dataBean;
	}

	static void roleRegistration(RolePrivilegeBean dataBean) throws SLEException {
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.ROLE_REGISTER);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void roleEdit(RolePrivilegeBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.ROLE_EDIT);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void roleHeadRegistration(RoleHeadDataBean dataBean) throws SLEException {
		String responseString = null;
		int responseCode = -1;
		String responseMessage = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.ROLE_HEAD_REGISTER);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
			responseCode = data.get("responseCode").getAsInt();
			if(responseCode != 0) {
				responseMessage = data.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}	
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void subUserRegistration(SubUserDataBean dataBean) throws SLEException {
		String responseString = null;
		int responseCode = -1;
		String responseMessage = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.SUB_USER_REGISTER);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
			responseCode = data.get("responseCode").getAsInt();
			if(responseCode != 0) {
				responseMessage = data.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}	
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void subUserEdit(SubUserDataBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.SUB_USER_EDIT);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static void checkForAutoCancel(UserDataBean dataBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.DATA_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.CHECK_FOR_AUTO_CANCEL);
			sReq.setServiceData(dataBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}

	static AuditTrailRequestBean fetchAuditTrailData(AuditTrailRequestBean dataBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		List<AuditTrailBean> auditTrailBeans = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.DATA_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.FETCH_AUDIT_TRAIL_DATA);
			requestObject.put("merchantId", dataBean.getMerchantId());
			requestObject.put("userId", dataBean.getUserId());
			requestObject.put("startTime", dataBean.getStartTime());
			requestObject.put("endTime", dataBean.getEndTime());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if (responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if (responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String auditDataString = responseObject.get("auditData").getAsJsonArray().toString();
			auditTrailBeans = new Gson().fromJson(auditDataString, new TypeToken<List<AuditTrailBean>>() {}.getType());
			dataBean.setAuditTrailBeans(auditTrailBeans);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
		return dataBean;
	}

	static PriviledgeModificationMasterBean fetchUserPriviledgeHistory(PriviledgeModificationMasterBean modificationBean) throws SLEException {
		JSONObject requestObject = new JSONObject();
		String responseString = null;
		JsonObject responseObject = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.FETCH_USER_PRIV_HISTORY);
			requestObject.put("userId", modificationBean.getUserId());
			requestObject.put("startDate", modificationBean.getStartDate());
			requestObject.put("endDate", modificationBean.getEndDate());
			sReq.setServiceData(requestObject);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);

			if(responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);

			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode != 0) {
				String responseMessage = responseObject.get("responseMessage").getAsString();
				throw new SLEException(responseCode, responseMessage);
			}

			String privHistoryString = responseObject.get("responseData").getAsJsonObject().toString();
			modificationBean = new Gson().fromJson(privHistoryString, new TypeToken<PriviledgeModificationMasterBean>() {}.getType());
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}

		return modificationBean;
	}

	static void updateRetailerPrivileges(RolePrivilegeBean rolePrivilegeBean) throws SLEException {
		String responseString = null;
		try {
			sReq.setServiceName(ServiceNameMethod.ServiceName.ROLE_MGMT);
			sReq.setServiceMethod(ServiceNameMethod.ServiceMethod.UPDATE_RETAILER_PRIVILEGES);
			sReq.setServiceData(rolePrivilegeBean);
			responseString = ServiceDelegateSLE.getInstance().getResponseString(sReq);
			if (responseString == null) {
				throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
			}
			logger.info(responseString);
		} catch (Exception ex) {
			throw new SLEException(SLE.Errors.GENERAL_EXCEPTION_CODE, SLE.Errors.GENERAL_EXCEPTION_MESSAGE);
		}
	}
}