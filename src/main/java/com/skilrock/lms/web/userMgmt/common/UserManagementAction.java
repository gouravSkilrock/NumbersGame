package com.skilrock.lms.web.userMgmt.common;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationManagementHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.UserManagementHelper;
import com.skilrock.lms.web.loginMgmt.AutoGenerate;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class UserManagementAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId = "";
	private String isOffline;
	private String offlineStatus;
	private long phoneNbr;
	private long mobileNbr;
	private String comments;

	private HttpServletRequest request;
	private String status1 = "";
	private int userId = 1;
	private String userName;
	private String toTerminate;

	public String editDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		UserDetailsBean bean = (UserDetailsBean) session
				.getAttribute("USER_SEARCH_RESULTS");

		// UserDetailsBean bean=new UserDetailsBean();
		System.out.println(getPhoneNbr());
		int userId = bean.getUserId();
		System.out.println("hello  " + bean.getFirstName());
		System.out.println("user id is  from bean  " + userId);

		if (status1 == null) {
			status1 = "INACTIVE";
		}
		if ("BLOCK".equalsIgnoreCase(status1) || "TERMINATE".equalsIgnoreCase(status1)) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(bean.getUserName());
		}
		int doneByUserId = ((UserInfoBean)session.getAttribute("USER_INFO")).getUserId();
		String requestIp = request.getRemoteAddr();

		UserManagementHelper editUserDetail = new UserManagementHelper();
		if (editUserDetail.editUserDetails(userId, getPhoneNbr(), getMobileNbr(), getEmailId().trim(), getStatus1(), doneByUserId, comments, requestIp)) {
			session.removeAttribute("ORG_SEARCH_RESULTS");
			session.setAttribute("User_Name", bean.getFirstName().toUpperCase()
					+ " " + bean.getLastName().toUpperCase());
			System.out.println("removed attribute from session");
			return SUCCESS;
		} else {
			return "error";
		}

	}

	public String editOfflineDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		UserBean bean = (UserBean) session.getAttribute("USER_BEAN");
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		int userId = bean.getUserId();
		System.out.println("user id is  from bean  " + userId);
		UserManagementHelper editOfflineUserDetail = new UserManagementHelper();
		boolean checkUpdate = false;
		System.out.println("user name is  from bean  " + userName);
		boolean isSession = currentUserSessionMap.containsKey(userName);
		checkUpdate = editOfflineUserDetail.editOfflineUserDetails(userId,
				getOfflineStatus(), getIsOffline(), isSession);

		session.setAttribute("ORG_Name", bean.getUserOrgName().toUpperCase());
		session.setAttribute("checkUpdate", checkUpdate);
		return SUCCESS;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getIsOffline() {
		return isOffline;
	}

	public String getOfflineStatus() {
		return offlineStatus;
	}

	// Method Added by Umesh for 'Offline File Upload Via Applet'

	public long getPhoneNbr() {
		return phoneNbr;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getStatus1() {
		return status1;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
	
	

	public String getToTerminate() {
		return toTerminate;
	}

	public void setToTerminate(String toTerminate) {
		this.toTerminate = toTerminate;
	}

	public String offlineFileUploadViaApplet() throws Exception {
		PropertyLoader.loadProperties("RMS/LMS.properties");
		System.out.println("onlyParentAgentAllowed status: "
				+ PropertyLoader.getProperty("onlyParentAgentAllowed"));
		if (PropertyLoader.getProperty("onlyParentAgentAllowed")
				.equalsIgnoreCase("true")) {
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			UserManagementHelper helper = new UserManagementHelper();
			String retIdAndNameList = helper
					.offlineFileUploadViaApplet(userBean.getUserId());

			System.out.println("Retailer's Ids and Names List: "
					+ retIdAndNameList);
			session.setAttribute("RETAILER_LIST_FOR_OFFLINE_FILE_UPLOAD",
					retIdAndNameList);
		}

		return SUCCESS;
	}

	public String offlineFileUploadViaAppletAtBO() throws Exception {
		HttpSession session = getRequest().getSession();
		String agtIdAndNameList = new UserManagementHelper()
				.offlineFileUploadViaAppletAtBO();
		session.setAttribute("AGENT_LIST_FOR_OFFLINE_FILE_UPLOAD",
				agtIdAndNameList);

		return SUCCESS;
	}

	public String offlineFileUploadViaBrowsingAtBO() throws Exception {

		return SUCCESS;
	}

	public String offlineFileUploadViaBrowsingAtBOSave() throws Exception {

		return SUCCESS;
	}

	public String offlineUserDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_BEAN", null);
		UserManagementHelper userDetail = new UserManagementHelper();
		UserBean bean = userDetail.offlineUserDetails(userId);
		session.setAttribute("USER_BEAN", bean);
		return SUCCESS;
	}

	public String resetPass() throws Exception {

		HttpSession session = getRequest().getSession();
		UserDetailsBean bean = (UserDetailsBean) session
				.getAttribute("USER_SEARCH_RESULTS");

		String autoPass = null;

		if (bean.getOrgType().equalsIgnoreCase("RETAILER")
				&& "integer".equalsIgnoreCase(((String) ServletActionContext
						.getServletContext().getAttribute("RETAILER_PASS"))
						.trim())) {
			autoPass = AutoGenerate.autoPasswordInt() + "";
		} else {
			autoPass = AutoGenerate.autoPassword();
		}
		UserManagementHelper resetPass = new UserManagementHelper();
		// resetPass.resetPassword(bean.getUserId(),autoPass);
		session.setAttribute("NEW_PASS", autoPass + ":" + bean.getOrgType());

		resetPass.resetPassword(bean.getUserId(), autoPass, bean.getEmailId(),
				bean.getUserName(), bean.getFirstName(), bean.getLastName());

		System.out.println("id at time of pas is  " + bean.getUserId());
		addActionError("User's Password is Successfully Sent to Users Email Address ");
		session.removeAttribute("ORG_SEARCH_RESULTS");
		System.out.println("removed attribute from session");
		return SUCCESS;

	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	public void setOfflineStatus(String offlineStatus) {
		this.offlineStatus = offlineStatus;
	}

	public void setPhoneNbr(long phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(long mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String userDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_SEARCH_RESULTS", null);
		UserManagementHelper userDetail = new UserManagementHelper();
		UserInfoBean userInfoBean = new UserInfoBean() ;
		OrganizationManagementHelper orgDetail = new OrganizationManagementHelper();
		UserDetailsBean bean = userDetail.UserDetails(userId, "YES"
				.equalsIgnoreCase((String) ServletActionContext
						.getServletContext().getAttribute("RET_OFFLINE")));
		AjaxRequestHelper helper = new AjaxRequestHelper() ;
		userInfoBean.setUserOrgId(bean.getOrgId()) ;
		toTerminate = (String) ServletActionContext
		.getServletContext().getAttribute("TERMINATE_USER") ;
		if("RETAILER".equalsIgnoreCase(bean.getOrgType()))
		{
			
			bean.setOutstandingBal(FormatNumber.formatNumberForJSP(orgDetail.getRetOutstandingBal(bean.getOrgId(), request, session, AjaxRequestHelper.getAgentOrgIdByRetailerOrgId(bean.getOrgId())))) ;
		}
		else
		{
			bean.setOutstandingBal(FormatNumber.formatNumberForJSP(orgDetail.getAgentOutstandingBal(bean.getOrgId(), request, session))) ;
		}
		bean.setTerminalCount(orgDetail.fetchTerminalCount(bean.getOrgId()));
		
		session.setAttribute("USER_SEARCH_RESULTS", bean);
		session.setAttribute("TO_TERMINATE", toTerminate) ;
		return SUCCESS;
	}
	
	public String resetUserLoginAttempts() throws Exception{
		HttpSession session = getRequest().getSession();
		UserDetailsBean bean = (UserDetailsBean) session
				.getAttribute("USER_SEARCH_RESULTS");
		setUserName(bean.getUserName());
		new UserManagementHelper().resetUserLoginAttempts(bean.getUserId());
		return SUCCESS;
	}

}
