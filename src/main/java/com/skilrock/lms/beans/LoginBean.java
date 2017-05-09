/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap actionServiceMap;
	private String status;
	private ArrayList<String> userActionList;
	private UserInfoBean userInfo;
	private int errorCode;
	public HashMap getActionServiceMap() {
		return actionServiceMap;
	}

	public String getStatus() {
		return status;
	}

	public ArrayList<String> getUserActionList() {
		return userActionList;
	}

	public UserInfoBean getUserInfo() {
		return userInfo;
	}

	public void setActionServiceMap(HashMap actionServiceMap) {
		this.actionServiceMap = actionServiceMap;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserActionList(ArrayList<String> userActionList) {
		this.userActionList = userActionList;
	}

	public void setUserInfo(UserInfoBean userInfo) {
		this.userInfo = userInfo;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
