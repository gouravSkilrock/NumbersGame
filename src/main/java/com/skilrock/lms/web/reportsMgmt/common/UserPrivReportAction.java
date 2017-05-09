package com.skilrock.lms.web.reportsMgmt.common;

import java.util.*;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.UserPrivReportHelper;


public class UserPrivReportAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(UserPrivReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map<String, Map<String, List<String>>> userActivePrivsDetails = null;
	Map<String, String> serviceMap = null;
	Map<String, String> groupMap = null;
	Map<String, String> grpPrivMap = null;
	Map<String, String> userTypeMap = null;
	
	Map<String, String> userMap = null;
	Map<String, String> dirSerPrivMap = null;
	Map<String, String> dirRelPrivMap = null;
	Map deptMap = null;
	
	

	List<UserDetailsBean> privUsersList;	
	
	private String serviceId;	
	private String groupId;	
	private String privIds;
	private String message;	
	HttpServletRequest request;
	private HttpSession session;	

	private String userId;
	private String userType;
	
	UserPrivReportHelper userPrivReportHelper = new UserPrivReportHelper();
		
	public String fetchServicePriviledgeDetails() throws LMSException{
		
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		Map<String, Map<String, String>> servicePrivDetailMap, tierUserDetailMap;
		Map roleMap;
		
		if(!userPrivReportHelper.isUserHead(userInfoBean.getUserId())){
			return "unauthorize";
		}else{			
			servicePrivDetailMap = userPrivReportHelper.getServicePrivDetails();
			tierUserDetailMap = userPrivReportHelper.getTierUserDetails(userInfoBean.getRoleId(), userPrivReportHelper.isBoMaster(userInfoBean.getUserId()), userInfoBean.getUserId());
			roleMap = userPrivReportHelper.dispSearchBoUser(userInfoBean.getRoleId(), userInfoBean.getUserId());
		
			setServiceMap(servicePrivDetailMap.get("ServiceMap"));
			setGroupMap(servicePrivDetailMap.get("GroupMap"));
			setGrpPrivMap(servicePrivDetailMap.get("GroupPrivMap"));
			setDirSerPrivMap(servicePrivDetailMap.get("DirServPrivMap"));
			setDirRelPrivMap(servicePrivDetailMap.get("DirRelPrivMap"));
			setUserTypeMap(tierUserDetailMap.get("UserTypeMap"));
			setUserMap(tierUserDetailMap.get("UserMap"));
			setDeptMap(roleMap);					
		}
		return SUCCESS;		
	}
	
	public String searchPriviledgedUsers() throws LMSException{			
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		privUsersList = null;
		if(!("".equals( privIds )))
			setPrivUsersList(userPrivReportHelper.getPriviledgedUsers(serviceId, privIds, userPrivReportHelper.isBoMaster(userInfoBean.getUserId()), userInfoBean.getUserId()));
		else
			privUsersList = new ArrayList<UserDetailsBean>();
		if(userPrivReportHelper.isBoMaster(userInfoBean.getUserId())){
			setMessage("**Table lists only BO Subusers and Roleheads data.");
		}else{
			setMessage("**Table lists only subusers data.");
		}	
		return SUCCESS;
	}
	
	
	public String fetchUserActivePrivs() throws LMSException{			
		setUserActivePrivsDetails(userPrivReportHelper.fetchUserActivePrivs(userId));
		return SUCCESS;
	}	

	public Map<String, Map<String, List<String>>> getUserActivePrivsDetails() {
		return userActivePrivsDetails;
	}

	public void setUserActivePrivsDetails(
		Map<String, Map<String, List<String>>> userActivePrivsDetails) {
		this.userActivePrivsDetails = userActivePrivsDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}	
	
	public List<UserDetailsBean> getPrivUsersList() {
		return privUsersList;
	}

	public void setPrivUsersList(List<UserDetailsBean> privUsersList) {
		this.privUsersList = privUsersList;
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}
	
	
	public String getPrivIds() {
		return privIds;
	}

	public void setPrivIds(String privIds) {
		this.privIds = privIds;
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public Map<String, String> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, String> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public Map<String, String> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, String> groupMap) {
		this.groupMap = groupMap;
	}

	public Map<String, String> getGrpPrivMap() {
		return grpPrivMap;
	}

	public void setGrpPrivMap(Map<String, String> grpPrivMap) {
		this.grpPrivMap = grpPrivMap;
	}

	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}	

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}	

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public Map<String, String> getDirSerPrivMap() {
		return dirSerPrivMap;
	}

	public void setDirSerPrivMap(Map<String, String> dirSerPrivMap) {
		this.dirSerPrivMap = dirSerPrivMap;
	}

	public Map<String, String> getDirRelPrivMap() {
		return dirRelPrivMap;
	}

	public void setDirRelPrivMap(Map<String, String> dirRelPrivMap) {
		this.dirRelPrivMap = dirRelPrivMap;
	}
	
	public Map<String, String> getUserTypeMap() {
		return userTypeMap;
	}

	public void setUserTypeMap(Map<String, String> userTypeMap) {
		this.userTypeMap = userTypeMap;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map getDeptMap() {
		return deptMap;
	}

	public void setDeptMap(Map deptMap) {
		this.deptMap = deptMap;
	}


}