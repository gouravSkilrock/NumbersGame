package com.skilrock.lms.rolemgmt.beans;

public class PriviledgeBeanRole {

	private int pid;
	private String privTitle;
	private int roleId;
	private String roleName;
	private int userId;
	private String userName;

	public int getPid() {
		return pid;
	}

	public String getPrivTitle() {
		return privTitle;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setPrivTitle(String privTitle) {
		this.privTitle = privTitle;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}