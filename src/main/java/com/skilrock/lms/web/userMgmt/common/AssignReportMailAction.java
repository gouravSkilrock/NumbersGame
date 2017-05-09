package com.skilrock.lms.web.userMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.userMgmt.common.AssignReportMailhelper;

public class AssignReportMailAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String activePriv;

	private String email;
	private String firstName;
	private String inactivePriv;

	private String isRoleHead;
	private String lastName;
	Log logger = LogFactory.getLog(AssignReportMailAction.class);
	private long mobile;

	private String orgType;

	private String ownerId;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	private String user;

	private String userId;

	private String userType;

	public void createNewUSerForEmail() throws IOException {
		logger.info("Inside createNewUSerForEmail");
		logger.debug("createNewUSerForEmail === " + orgType + " " + firstName
				+ "  " + lastName + "      " + mobile + "  " + email
				+ " Active=" + activePriv + " InActive = " + inactivePriv);
		System.out.println("createNewUSerForEmail === " + orgType + " "
				+ firstName + "  " + lastName + "      " + mobile + "  "
				+ email + " Active=" + activePriv + " InActive = "
				+ inactivePriv);
		String list = "successfully updated";

		String activePrivIdList[] = new String[0];
		if (!"".equalsIgnoreCase(activePriv.trim())) {
			logger.debug("inside if of activePriv===== ");
			System.out.println("inside if of activePriv===== ");
			activePrivIdList = activePriv.split(",");
		}
		String inactivePrivIdList[] = new String[0];
		if (!"".equalsIgnoreCase(inactivePriv.trim())) {
			inactivePrivIdList = inactivePriv.split(",");
			logger.debug("inside if of inactivePriv===== ");
			System.out.println("inside if of inactivePriv===== ");
		}

		AssignReportMailhelper helper = new AssignReportMailhelper();
		boolean flag = helper.createNewEmailPriviledges(orgType, firstName,
				lastName, mobile, email, activePrivIdList, inactivePrivIdList);

		PrintWriter out = response.getWriter();
		if (!flag) {
			out.print("NO");
		} else {
			out.print(list);
		}

	}

	public String getActivePriv() {
		return activePriv;
	}

	public void getAgentListForEmail() throws IOException {
		AssignReportMailhelper helper = new AssignReportMailhelper();
		String list = null;

		List<String> agentList = helper.getAgentList();
		list = agentList.toString();

		list = list.replace("[", "");
		list = list.replace("]", "");

		PrintWriter out = response.getWriter();
		if ("".equalsIgnoreCase(list.trim())) {
			out.print("NO");
		} else {
			out.print(list);
		}

	}

	public String getEmail() {
		return email;
	}

	public void getEmailPriviledges() throws IOException {
		AssignReportMailhelper helper = new AssignReportMailhelper();
		String list = null;
		List<String> privList = null;

		if ("REG_USER".equalsIgnoreCase(user.trim())) {
			privList = helper.getEmailPriviledgesFromEmail(userId, email);
		} else if ("NEW_USER".equalsIgnoreCase(user.trim())) {
			privList = helper.getEmailPriviledgesFromEmail(userId, email);
		} else {
			privList = helper.getEmailPriviledges(userId, email, orgType);
		}
		list = privList.toString();

		list = list.replace("[", "");
		list = list.replace("]", "");

		PrintWriter out = response.getWriter();
		if ("".equalsIgnoreCase(list.trim())) {
			out.print("NO");
		} else {
			out.print(list);
		}

	}

	public String getFirstName() {
		return firstName;
	}

	public String getInactivePriv() {
		return inactivePriv;
	}

	public String getIsRoleHead() {
		return isRoleHead;
	}

	public String getLastName() {
		return lastName;
	}

	public long getMobile() {
		return mobile;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getUser() {
		return user;
	}

	public String getUserId() {
		return userId;
	}

	public void getUserListForReportMail() throws IOException {
		logger.debug("org type = " + orgType + "   userType = " + userType
				+ "   isroleHead = " + isRoleHead);
		System.out.println("org type = " + orgType + "   userType = "
				+ userType + "   isroleHead = " + isRoleHead);
		session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		logger.info("User Info :" + userInfo);
		System.out.println("333333333333333333 ========== " + userInfo);
		AssignReportMailhelper helper = new AssignReportMailhelper();
		String list = null;
		Map<String, String> map = new TreeMap<String, String>();
		if ("BO".equalsIgnoreCase(orgType.trim())) {
			map = helper.getBOUserList(userType, isRoleHead, userInfo
					.getUserId());
			list = map.toString();
		} else if ("AGENT".equalsIgnoreCase(orgType.trim())) {
			map = helper.getAgentUserList(userInfo.getUserId());
			list = map.toString();
		} else if ("RETAILER".equalsIgnoreCase(orgType.trim())) {
			map = helper.getRetailerUserList(userInfo.getUserId(), ownerId);
			list = map.toString();
		}

		list = list.replace("{", "");
		list = list.replace("}", "");

		/*
		 * Set listSet = new TreeSet(); listSet.addAll(map);
		 * System.out.println();
		 */

		PrintWriter out = response.getWriter();
		if ("".equalsIgnoreCase(list.trim())) {
			out.print("NO");
		} else {
			out.print(list);
		}

	}

	public String getUserType() {
		return userType;
	}

	public void setActivePriv(String activePriv) {
		this.activePriv = activePriv;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setInactivePriv(String inactivePriv) {
		this.inactivePriv = inactivePriv;
	}

	public void setIsRoleHead(String isRoleHead) {
		this.isRoleHead = isRoleHead;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void updatePrivlist() throws IOException {
		logger.info("Inside updatePrivlist");
		logger.debug("total privleges checked" + orgType + " " + user + "  "
				+ userId + "  " + email + " Active=" + activePriv
				+ " InActive = " + inactivePriv);
		System.out.println("total privleges checked" + orgType + " " + user
				+ "  " + userId + "  " + email + " Active=" + activePriv
				+ " InActive = " + inactivePriv);
		String list = "successfully updated";
		System.out.println("");
		String activePrivIdList[] = new String[0];
		if (!"".equalsIgnoreCase(activePriv.trim())) {
			logger.debug("inside if of activePriv===== ");
			System.out.println("inside if of activePriv===== ");
			activePrivIdList = activePriv.split(",");
		}
		String inactivePrivIdList[] = new String[0];
		if (!"".equalsIgnoreCase(inactivePriv.trim())) {
			inactivePrivIdList = inactivePriv.split(",");
			logger.debug("inside if of inactivePriv===== ");
			System.out.println("inside if of inactivePriv===== ");
		}

		AssignReportMailhelper helper = new AssignReportMailhelper();

		if ("REG_USER".equalsIgnoreCase(user.trim())
				|| "NEW_USER".equalsIgnoreCase(user.trim())) {
			helper.updateEmailPriviledges(userId, activePrivIdList,
					inactivePrivIdList);
		} else {
			helper.createEmailPriviledges(userId, orgType, email,
					activePrivIdList, inactivePrivIdList);
		}

		PrintWriter out = response.getWriter();
		if ("".equalsIgnoreCase(list.trim())) {
			out.print("NO");
		} else {
			out.print(list);
		}

	}

	public void verifyAssignEmail() throws IOException {
		AssignReportMailhelper helper = new AssignReportMailhelper();
		String list = null;
		List<String> privList = null;
		logger.info("Inside verifyAssignEmail()");
		logger.debug("email ====== " + email);
		System.out.println("email ====== " + email);
		privList = helper.verifyEmailPriviledgesFromEmail(email);
		logger.debug("privList === " + privList);
		System.out.println("privList === " + privList);
		list = privList.toString();

		list = list.replace("[", "");
		list = list.replace("]", "");

		PrintWriter out = response.getWriter();
		if ("".equalsIgnoreCase(list.trim())) {
			out.print("NO");
			logger.debug("privList === NO");
			System.out.println("privList === NO");
		} else {
			out.print(list);
			logger.debug("privList === " + list);
			System.out.println("privList === " + list);
		}

	}

}
