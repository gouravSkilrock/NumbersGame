package com.skilrock.lms.web.roleMgmt.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.roleMgmt.common.RoleManagementHelper;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.common.SLEUtils;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;

public class RoleManagementAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(RoleManagementAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isNewService;
	private int[] mappingId;
	private int[] privCount;
	HttpServletRequest request;
	private String roleDesc;
	private int roleId;
	Map<Integer, String> roleMap = new TreeMap<Integer, String>();
	private String roleName;

	private String[] rolePriv;
	private int[] rolePrivId;
	Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> rolePriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();

	Map<String, TreeMap<String, TreeMap<String, List<String>>>> rolePrivMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>();

	@SuppressWarnings("unchecked")
	public String createRole()  {
		
		try{
		HttpSession session = getRequest().getSession();
		RoleManagementHelper roleMgmeHelper = new RoleManagementHelper();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		if (!roleName.equals("") && !roleDesc.equals("") && rolePriv.length > 0) {
			List<PrivilegeDataBean> privilegeListSLE = null;
			if(ServicesBean.isSLE()) {
				privilegeListSLE = (List<PrivilegeDataBean>) session.getAttribute("SLE_PRIV_LIST");
			}
			roleMgmeHelper.createRole(roleName, roleDesc, userBean,rolePriv, mappingId, privCount, privilegeListSLE);
			return SUCCESS;
		} else {
			addActionError(getText("msg.fill.correct.entry"));
			return ERROR;
		}
		
		}catch (Exception e) {
			addActionError(e.getMessage());
			return ERROR;
		}
	}

	public String createRoleMenu() throws LMSException {
		RoleManagementHelper roleMgmtHelper = new RoleManagementHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		rolePrivMap = roleMgmtHelper.getHeadsGroupNames(userBean.getUserId(),
				userBean.getRoleId(), userBean.getUserOrgId());

		if(ServicesBean.isSLE()) {
			RolePrivilegeBean roleBean = null;
			List<PrivilegeDataBean> privilegeList = null;
			try {
				roleBean = new RolePrivilegeBean();
				roleBean.setCreatorUserId(userBean.getUserId());
				NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_CREATE_ROLE_PRIVILEGES, roleBean);
				roleBean = (RolePrivilegeBean) notifySLE.asyncCall(notifySLE);
				privilegeList = roleBean.getPrivilegeList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			SLEUtils.addSLEPrivileges(rolePrivMap, privilegeList);

			session.setAttribute("SLE_PRIV_LIST", privilegeList);
		}

		session.setAttribute("ROLE_PRIV_MAP", rolePrivMap);
		return SUCCESS;
	}

	public String editRoleMenu() throws LMSException {
		HttpSession session = getRequest().getSession();
		RoleManagementHelper roleMgmtHelper = new RoleManagementHelper();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		roleMap = roleMgmtHelper.fetchRoles(userBean.getRoleId(), userBean
				.getUserType(), userBean.getUserOrgId());
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String editRolePriv() throws SQLException {
		try {
			HttpSession session = getRequest().getSession();
			RoleManagementHelper roleMgmeHelper = new RoleManagementHelper();
			UserInfoBean userBean = new UserInfoBean();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			List<PrivilegeDataBean> privilegeListSLE = null;
			if(ServicesBean.isSLE()) {
				privilegeListSLE = (List<PrivilegeDataBean>) session.getAttribute("SLE_PRIV_LIST");
			}
			/*roleMgmeHelper.editRolePriv(Integer.parseInt(roleName), rolePriv,
					mappingId, privCount, userBean.getRoleId(), userBean
							.getTierId(), userBean.getUserOrgId(), privilegeListSLE);*/
			roleMgmeHelper.editRolePriv(Integer.parseInt(roleName), rolePriv, mappingId, privCount, userBean, privilegeListSLE, request.getRemoteAddr());
			return SUCCESS;
		}catch (Exception e) {
			addActionError(e.getMessage());
			return ERROR;
		}
	}

	public String fetchRolePriv() throws SQLException, LMSException {
		HttpSession session = getRequest().getSession();
		RoleManagementHelper roleMgmeHelper = new RoleManagementHelper();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		rolePriviledgeMap = roleMgmeHelper.fetchRolePriv(roleId, userBean
				.getUserType(), userBean.getUserOrgId(), userBean.getUserId());

		if(ServicesBean.isSLE() && rolePriviledgeMap.get("Sports Lottery") != null) {
			RolePrivilegeBean roleBean = null;
			List<PrivilegeDataBean> privilegeList = null;
			try {
				roleBean = new RolePrivilegeBean();
				roleBean.setRoleId(roleId);
				NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_ROLE_PRIVILEGES, roleBean);
				roleBean = (RolePrivilegeBean) notifySLE.asyncCall(notifySLE);
				privilegeList = roleBean.getPrivilegeList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			SLEUtils.editSLEPrivileges(rolePriviledgeMap, privilegeList);

			session.setAttribute("SLE_PRIV_LIST", privilegeList);
		}

		return SUCCESS;

	}

	public int[] getMappingId() {
		return mappingId;
	}

	public int[] getPrivCount() {
		return privCount;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public int getRoleId() {
		return roleId;
	}

	public Map<Integer, String> getRoleMap() {
		return roleMap;
	}

	public String getRoleName() {
		return roleName;
	}

	public String[] getRolePriv() {
		return rolePriv;
	}

	public int[] getRolePrivId() {
		return rolePrivId;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getRolePriviledgeMap() {
		return rolePriviledgeMap;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<String>>>> getRolePrivMap() {
		return rolePrivMap;
	}

	public void setMappingId(int[] mappingId) {
		this.mappingId = mappingId;
	}

	public void setPrivCount(int[] privCount) {
		this.privCount = privCount;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleMap(Map<Integer, String> roleMap) {
		this.roleMap = roleMap;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setRolePriv(String[] rolePriv) {
		this.rolePriv = rolePriv;
	}

	public void setRolePrivId(int[] rolePrivId) {
		this.rolePrivId = rolePrivId;
	}

	public void setRolePriviledgeMap(
			Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> rolePriviledgeMap) {
		this.rolePriviledgeMap = rolePriviledgeMap;
	}

	public void setRolePrivMap(
			Map<String, TreeMap<String, TreeMap<String, List<String>>>> rolePrivMap) {
		this.rolePrivMap = rolePrivMap;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getIsNewService() {
		return isNewService;
	}

	public void setIsNewService(String isNewService) {
		this.isNewService = isNewService;
	}

	public String updateRolePrivDeployment() throws SQLException {

		RoleManagementHelper rmh = new RoleManagementHelper();
		if(isNewService==null || isNewService==""){
			isNewService="No";
		}
		rmh.managePriv("BO", request.getRemoteAddr(),isNewService);
		rmh.managePriv("AGENT", request.getRemoteAddr(),isNewService);
		rmh.managePriv("RETAILER", request.getRemoteAddr(),isNewService);
		return SUCCESS;
	}

}