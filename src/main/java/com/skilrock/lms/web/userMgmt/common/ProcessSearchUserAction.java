/***
 *  * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 * 
 */
package com.skilrock.lms.web.userMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchUserHelper;

/**
 * 
 * This class is used to process the user search.
 * 
 * @author Skilrock Technologies
 * 
 */

public class ProcessSearchUserAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String compName = null;

	private String deptName = null;
	private String end = null;
	private String parentCompName = null;
	private HttpServletRequest request;
	int start = 0;
	private String userdetailResultsAvailable;
	private int userId;

	private String userName = null;
	private String userRole = null;
	private String usersearchResultsAvailable;

	private String userStatus = null;

	private String userType;

	/**
	 * 
	 * This methood is used to display the details of the selected user.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String displayDetail() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_SEARCH_RESULTS_DETAIL", null);

		System.out.println("hello i sm in search User");
		System.out.println("User iD:" + userId);

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List searchResults = searchUserHelper.searchUserDetail(userId);

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("USER_SEARCH_RESULTS_DETAIL", searchResults);
			setUserdetailResultsAvailable("Yes");
		} else {
			setUserdetailResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return "success";
	}

	public String getCompName() {
		return compName;
	}

	public String getDeptName() {
		return deptName;
	}

	public String getEnd() {
		return end;
	}

	public String getParentCompName() {
		return parentCompName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getStart() {
		return start;
	}

	public String getUserdetailResultsAvailable() {
		return userdetailResultsAvailable;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public String getUsersearchResultsAvailable() {
		return usersearchResultsAvailable;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public String getUserType() {
		return userType;
	}

	public String search() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_SEARCH_RESULTS", null);
		session.setAttribute("USER_SEARCH_RESULTS1", null);
		// System.out.println("hello i sm in search User");
		// System.out.println("User Name:" + userName);
		// System.out.println("User Status:" + userStatus);
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.USER_NAME, userName);
		searchMap.put(GameContants.USER_ROLE, userRole);
		searchMap.put(GameContants.USER_STATUS, userStatus);
		searchMap.put(GameContants.COMP_NAME, compName);
		searchMap.put(GameContants.PARENT_COMP_NAME, parentCompName);
		System.out.println("status" + userStatus);
		System.out.println("role" + userRole);
		System.out.println("company name is @@@@@@@ " + compName);
		// if there is no any status is selected
		if (userStatus.equals("1")) {
			System.out.println("xxxxxxxxxxx");
			searchMap.put(GameContants.USER_STATUS, null);
		} else {
			searchMap.put(GameContants.USER_STATUS, userStatus);
		}
		// if there is no any role is selected
		if (userRole.equals("1")) { // System.out.println("xxxxxxxxxxx");
			searchMap.put(GameContants.USER_ROLE, null);
		} else {
			searchMap.put(GameContants.USER_ROLE, userRole);
		}

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List searchResults = searchUserHelper.searchUser(searchMap);

		if (searchResults != null && searchResults.size() > 0) {
			// System.out.println("Yes:---Search result Processed");
			session.setAttribute("USER_SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueUserSearch", new Integer(0));
			setUsersearchResultsAvailable("Yes");
			// for pagination
			searchAjax();
		} else {
			setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	/**
	 * This method is used to search user.
	 * 
	 * @return SUCCESS OR ERROR
	 * @throws Exception
	 */

	public String searchAgtRetRoleList() throws Exception {
		HttpSession session = request.getSession();

		UserInfoBean userinfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List<String> roleMasterName = searchUserHelper
				.getRoleMasterName(userinfo.getTierId());
		session.setAttribute("roleList", roleMasterName);
		return SUCCESS;
	}

	/**
	 * 
	 * This method is used for pagination of the search Result
	 * 
	 * @author Skilrock Technologies
	 * @Return SUCCESS or ERROR
	 */

	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("USER_SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("List Size "+ajaxList.size());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueUserSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;
			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("USER_SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueUserSearch", startValue);
		}
		UserInfoBean bean = (UserInfoBean) session.getAttribute("USER_INFO");
		if ("BO".equalsIgnoreCase(bean.getUserType())) {
			return SUCCESS;
		} else {
			return "agent";
		}
	}

	/**
	 * This method is used to search user.
	 * 
	 * @return SUCCESS OR ERROR
	 * @throws Exception
	 */

	public String searchBoUser() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("BO_USER_SEARCH_RESULTS", null);
		session.setAttribute("BO_USER_SEARCH_RESULTS1", null);
		// System.out.println("hello i sm in search User");
		// System.out.println("User Name:" + userName);
		// System.out.println("User Status:" + userStatus);
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.USER_NAME, userName);
		searchMap.put(GameContants.USER_ROLE, userRole);
		searchMap.put(GameContants.USER_STATUS, userStatus);
		searchMap.put(GameContants.COMP_NAME, compName);
		searchMap.put(GameContants.DEPTT_NAME, deptName);
		System.out.println("status" + userStatus);
		System.out.println("role" + userRole);
		System.out.println("company name is @@@@@@@ " + compName);
		// if there is no any status is selected
		if (userStatus.equals("1")) {
			System.out.println("xxxxxxxxxxx");
			searchMap.put(GameContants.USER_STATUS, null);
		} else {
			searchMap.put(GameContants.USER_STATUS, userStatus);
		}
		// if there is no any role is selected

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List searchResults = searchUserHelper.searchUser(searchMap);

		if (searchResults != null && searchResults.size() > 0) {
			// System.out.println("Yes:---Search result Processed");
			session.setAttribute("BO_USER_SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueUserSearch", new Integer(0));
			setUsersearchResultsAvailable("Yes");
			// for pagination
			searchAjax();
		} else {
			setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public String searchOffline() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_SEARCH_RESULTS", null);
		session.setAttribute("USER_SEARCH_RESULTS1", null);
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.USER_NAME, userName);
		// searchMap.put(GameContants.USER_STATUS, userStatus);
		searchMap.put(GameContants.COMP_NAME, compName);
		searchMap.put(GameContants.PARENT_COMP_NAME, parentCompName);

		// if there is no any status is selected
		if (userStatus.equals("1")) {
			searchMap.put("offline_status", null);
		} else {
			searchMap.put("offline_status", userStatus);
		}

		if (userType.equals("1")) {
			searchMap.put("user_type", null);
		} else {
			searchMap.put("user_type", userType);
		}

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List searchResults = searchUserHelper.searchOfflineUser(searchMap);

		if (searchResults != null && searchResults.size() > 0) {
			// System.out.println("Yes:---Search result Processed");
			session.setAttribute("USER_SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueUserSearch", new Integer(0));
			setUsersearchResultsAvailable("Yes");
			// for pagination
			searchAjax();
		} else {
			setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public String searchretailer() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("USER_SEARCH_RESULTS", null);
		session.setAttribute("USER_SEARCH_RESULTS1", null);
		// System.out.println("hello i sm in search User");
		// System.out.println("User Name:" + userName);
		// System.out.println("User Status:" + userStatus);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.USER_NAME, userName);
		searchMap.put(GameContants.USER_ROLE, userRole);
		searchMap.put(GameContants.USER_STATUS, userStatus);
		searchMap.put(GameContants.COMP_NAME, compName);
		System.out.println("status" + userStatus);
		System.out.println("role" + userRole);
		System.out.println("company  name is @@@@@@@ " + compName);
		// if there is no any status is selected
		if (userStatus == null || userStatus.equals("1")) {
			System.out.println("xxxxxxxxxxx");
			searchMap.put(GameContants.USER_STATUS, null);
		} else {
			searchMap.put(GameContants.USER_STATUS, userStatus);
		}
		// if there is no any role is selected
		if (userRole == null || userRole.equals("1")) { // System.out.println("xxxxxxxxxxx");
			searchMap.put(GameContants.USER_ROLE, null);
		} else {
			searchMap.put(GameContants.USER_ROLE, userRole);
		}

		SearchUserHelper searchUserHelper = new SearchUserHelper();
		List searchResults = searchUserHelper.searchUserRetailer(searchMap,
				userBean.getUserOrgId());

		if (searchResults != null && searchResults.size() > 0) {
			// System.out.println("Yes:---Search result Processed");
			session.setAttribute("USER_SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueUserSearch", new Integer(0));
			setUsersearchResultsAvailable("Yes");
			// for pagination
			searchAjax();
		} else {
			setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}

		return SUCCESS;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setParentCompName(String parentCompName) {
		this.parentCompName = parentCompName;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setUserdetailResultsAvailable(String userdetailResultsAvailable) {
		this.userdetailResultsAvailable = userdetailResultsAvailable;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public void setUsersearchResultsAvailable(String usersearchResultsAvailable) {
		this.usersearchResultsAvailable = usersearchResultsAvailable;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
