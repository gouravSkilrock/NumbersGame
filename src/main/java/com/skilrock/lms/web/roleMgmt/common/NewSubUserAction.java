package com.skilrock.lms.web.roleMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.roleMgmt.common.NewSubUserHelper;
import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.common.SLEUtils;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.RolePrivilegeBean;

public class NewSubUserAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(NewSubUserAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int agentOrgId;

	private String email;
	private String firstName;
	private String[] groupName;
	private Map<String, TreeMap<String, TreeMap<String, List<String>>>> headPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>();
	List headsUserList = new ArrayList();
	private String lastName;
	private int[] mappingId;
	String organizationType;

	private int orgId;
	private Long phone;

	private int[] privCount;
	List<String> privGroupName;
	HttpServletRequest request;
	private int retOrgId;
	private int[] rolePriv;
	private String secAns;
	private String secQues;
	private String status;
	private String userName;

	private Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> userPriviledgeMap = new TreeMap<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>>();

	@SuppressWarnings("unchecked")
	public String assignPriviledges() throws LMSException {

		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		HttpSession session = getRequest().getSession();
		UserDetailsBean usrdetBean = (UserDetailsBean) session
				.getAttribute("USER_DETAILS");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int roleId = userBean.getRoleId();
		logger.debug("UserName =" + getUserName());
		logger.debug("GroupName =" + getGroupName());
		logger.debug("UserId =" + userBean.getUserId());
		logger.debug("rolId =" + roleId);
		
		usrdetBean.setMailSend(true);

		List<PrivilegeDataBean> privilegeListSLE = null;
		boolean checkSLE = false ;
		for(String str : getGroupName())
		{
			if(str.contains("Sports Lottery"))
				checkSLE = true ;
		}
		if(checkSLE) {
			privilegeListSLE = (List<PrivilegeDataBean>) session.getAttribute("SLE_PRIV_LIST");
		}

		String password = subUserHelper.assignGroups(userBean.getUserOrgId(),
				getGroupName(), userBean.getUserId(), roleId, usrdetBean,
				mappingId, privCount, privilegeListSLE, request.getRemoteAddr());
		logger.debug("AFTER COMMITT DATA");
		//HttpSession seesion = getRequest().getSession();
		session.setAttribute("SUB_RET_PASSWORD", password);
		return SUCCESS;
	}

	public String editOrgPriv() throws LMSException {
		HttpSession session = getRequest().getSession();
		// Map<String,TreeMap<String,TreeMap<String,List<userPrivBean>>>>
		// orgPrivMap = (Map) session.getAttribute("ORG_PRIV_MAP");
		int orgId = 0;
		RolePrivilegeBean rolePrivilegeBean = null;
		logger.debug(organizationType + "*****************" + agentOrgId);
		logger.debug(organizationType + "*****************" + retOrgId);

		List<Integer> serviceDeliveryList = new ArrayList<Integer>();
		Map<Integer, String> sleService = new HashMap<Integer, String>();
		
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		
		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		
		if ("AGENT".equalsIgnoreCase(organizationType) && agentOrgId != 0) {
			orgId = agentOrgId;
		} else if ("RETAILER".equalsIgnoreCase(organizationType) && retOrgId != 0) {
			orgId = retOrgId;
			if(ServicesBean.isSLE()) {
				rolePrivilegeBean = (RolePrivilegeBean) getRequest().getSession().getAttribute("SLEPRIV");
				rolePrivilegeBean.setRequestIp(request.getRemoteAddr());
				rolePrivilegeBean.setCreatorUserId(userBean.getUserId());
				sleService = subUserHelper.fetchSLEServiceDeliveryMasterId(mappingId);
			}

			for (int iValue : mappingId) {
				serviceDeliveryList.add(iValue);
			}

			if(ServicesBean.isSLE()) {
				for (Integer iValue : sleService.keySet()) {
					mappingId = ArrayUtils.removeElement(mappingId, iValue);
				}
			}
		}
		/*
		 * if (agentOrgId == 0 && retOrgId != 0) orgId = retOrgId; else if
		 * (retOrgId == 0 && agentOrgId != 0){ orgId = agentOrgId;
		 * logger.debug("Org*****************"+orgId); }
		 */else {
			throw new LMSException();
		}

		subUserHelper.editOrgPriv(orgId, organizationType, getGroupName(), mappingId, privCount, rolePrivilegeBean, sleService, serviceDeliveryList);
		
		String orgName = subUserHelper.getOrgName(orgId);
		session.setAttribute("ORG_NAME", orgName);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String editUserPriv() throws LMSException {
		HttpSession session = getRequest().getSession();
		
		NewSubUserHelper subUserHelper = new NewSubUserHelper();

		List<PrivilegeDataBean> privilegeListSLE = null;
		
		boolean checkSLE = false ;
		for(String str : getGroupName())
		{
			if(str.contains("||"))
				str.replaceAll("||", "|") ;
			if(str.contains("Sports Lottery"))
				checkSLE = true ;
		}
		if(checkSLE) {
			privilegeListSLE = (List<PrivilegeDataBean>) session.getAttribute("SLE_PRIV_LIST");
		}

		if (getUserName().equals("bomaster")) {
			subUserHelper.editBOPriv(getUserName(), getGroupName(), mappingId,
					privCount, privilegeListSLE);
		} else {
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			subUserHelper.editUserPriv(getUserName(), getGroupName(),
					mappingId, privCount, privilegeListSLE, userBean.getUserId(), request.getRemoteAddr());
		}
		session.setAttribute("USER_NAME", getUserName());
		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String getBOMasPriviledges() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		

		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		/*
		 * UserPrivMap Discription <service_type<InterFaceMap<interface,privMap<relatedTo,PrivList>>>
		 * 
		 */

		List<PrivilegeDataBean> privilegeList = new ArrayList<PrivilegeDataBean>();

		userPriviledgeMap = subUserHelper.getBOMasPriviledges(getUserName(),userBean.getUserId(), privilegeList);

		if(ServicesBean.isSLE()) {
			session.setAttribute("SLE_PRIV_LIST", privilegeList);
		}

		return SUCCESS;

	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String[] getGroupName() {
		return groupName;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<String>>>> getHeadPriviledgeMap() {
		return headPriviledgeMap;
	}

	public List getHeadsUserList() {
		return headsUserList;
	}

	public String getLastName() {
		return lastName;
	}

	public int[] getMappingId() {
		return mappingId;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgPriviledges() throws LMSException {
		HttpSession session = getRequest().getSession();
		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		userPriviledgeMap = subUserHelper.getOrgGroupNames(getOrgId(),
				getOrganizationType());
		session.setAttribute("ORG_PRIV_MAP", userPriviledgeMap);
		return SUCCESS;

	}

	public Long getPhone() {
		return phone;
	}

	public String getPrivandUser() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userdata = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int roleId = userdata.getRoleId();
		int userId = userdata.getUserId();
		int orgId = userdata.getUserOrgId();

		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		headsUserList = subUserHelper.getUsers(roleId, userId, orgId);
	

		return SUCCESS;
	}

	public int[] getPrivCount() {
		return privCount;
	}

	public List<String> getPrivGroupName() {
		return privGroupName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public int[] getRolePriv() {
		return rolePriv;
	}

	public String getSecAns() {
		return secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public String getStatus() {
		return status;
	}

	public String getUserName() {
		return userName;
	}

	public Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> getUserPriviledgeMap() {
		return userPriviledgeMap;
	}

	public String getUserPriviledges() throws LMSException {

		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		/*
		 * UserPrivMap Discription <service_type<InterFaceMap<interface,privMap<relatedTo,PrivList>>>
		 * 
		 */
		userPriviledgeMap = subUserHelper.getUserPriviledges(getUserName());
		return SUCCESS;

	}

	public String newUserReg() throws LMSException {

		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int orgId = userBean.getUserOrgId();
		int roleId = userBean.getRoleId();
		String type = userBean.getUserType();

		NewSubUserHelper subUserHelper = new NewSubUserHelper();
		String returnType = subUserHelper.newUserReg(getUserName().trim());
		if (returnType.equals("ERROR")) {
			addActionError(getText("msg.user.already.exists") + "!!");
			return ERROR;
		}

		// now get the priviledges list of head
		headPriviledgeMap = subUserHelper.getHeadsGroupNames(userBean
				.getUserId(), userBean.getRoleId(), userBean.getUserOrgId());

		if(ServicesBean.isSLE()) {
			RolePrivilegeBean roleBean = null;
			List<PrivilegeDataBean> privilegeList = null;
			try {
				roleBean = new RolePrivilegeBean();
				roleBean.setCreatorUserId(userBean.getUserId());
				NotifySLE notifySLE = new NotifySLE(SLE.Activity.GET_CREATE_USER_PRIVILEGES, roleBean);
				roleBean = (RolePrivilegeBean) notifySLE.asyncCall(notifySLE);
				privilegeList = roleBean.getPrivilegeList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			session.setAttribute("SLE_PRIV_LIST", privilegeList);

			SLEUtils.addSLEPrivileges(headPriviledgeMap, privilegeList);
		}

		logger.debug("Head Priv Map: " + headPriviledgeMap);
		// put user details in session
		UserDetailsBean usrdetBean = new UserDetailsBean();
		usrdetBean.setFirstName(getFirstName());
		usrdetBean.setLastName(getLastName());
		usrdetBean.setEmailId(getEmail());
		usrdetBean.setPhoneNbr(getPhone());
		usrdetBean.setUserName(getUserName());
		usrdetBean.setStatus(getStatus());
		usrdetBean.setSecQues(getSecQues());
		usrdetBean.setSecAns(getSecAns());
		usrdetBean.setOrgId(orgId);
		usrdetBean.setRoleId(roleId);
		usrdetBean.setOrgType(type);
		usrdetBean.setRegisterByUserId(userBean.getUserId());

		session.setAttribute("USER_DETAILS", usrdetBean);
		session.setAttribute("HEAD_PRIV_MAP", headPriviledgeMap);

		return SUCCESS;

	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public void setHeadPriviledgeMap(
			Map<String, TreeMap<String, TreeMap<String, List<String>>>> headPriviledgeMap) {
		this.headPriviledgeMap = headPriviledgeMap;
	}

	public void setHeadsUserList(List headsUserList) {
		this.headsUserList = headsUserList;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMappingId(int[] mappingId) {
		this.mappingId = mappingId;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public void setPrivCount(int[] privCount) {
		this.privCount = privCount;
	}

	public void setPrivGroupName(List<String> privGroupName) {
		this.privGroupName = privGroupName;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setRolePriv(int[] rolePriv) {
		this.rolePriv = rolePriv;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPriviledgeMap(
			Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> userPriviledgeMap) {
		this.userPriviledgeMap = userPriviledgeMap;
	}

}