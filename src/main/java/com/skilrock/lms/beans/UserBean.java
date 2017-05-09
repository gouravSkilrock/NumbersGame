/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 */

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isOffline;
	private String loginStatus;
	private String offlineStatus;
	private String parentOrgName;
	private Date registerDate;
	private int userId;
	private String userName;
	private String userOrgName;
	private int userRoleId;
	private String userRoleName;
	private String userStatus;

	public String getIsOffline() {
		return isOffline;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public String getOfflineStatus() {
		return offlineStatus;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public int getUserRoleId() {
		return userRoleId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public void setOfflineStatus(String offlineStatus) {
		this.offlineStatus = offlineStatus;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}
