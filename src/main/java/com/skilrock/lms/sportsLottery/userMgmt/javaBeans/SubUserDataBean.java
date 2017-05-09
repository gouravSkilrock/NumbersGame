package com.skilrock.lms.sportsLottery.userMgmt.javaBeans;

import java.util.List;

import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;

public class SubUserDataBean implements SLEDataFace {
	private static final long serialVersionUID = 1L;

	private int creatorUserId;
	private int roleId;
	private int userId;
	private String userName;
	private String userType;
	private String firstName;
	private String lastName;
	private String mobileNbr;
	private String emailId;
	private String secretQues;
	private String secretAns;
	private String requestIp;
	private List<PrivilegeDataBean> privilegeList;

	public SubUserDataBean() {
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getSecretQues() {
		return secretQues;
	}

	public void setSecretQues(String secretQues) {
		this.secretQues = secretQues;
	}

	public String getSecretAns() {
		return secretAns;
	}

	public void setSecretAns(String secretAns) {
		this.secretAns = secretAns;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public List<PrivilegeDataBean> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<PrivilegeDataBean> privilegeList) {
		this.privilegeList = privilegeList;
	}
}