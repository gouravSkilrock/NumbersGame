package com.skilrock.lms.sportsLottery.userMgmt.javaBeans;

import java.util.List;

import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;

public class RolePrivilegeBean implements SLEDataFace {
	private static final long serialVersionUID = 1L;

	private int creatorUserId;
	private int userId;
	private int roleId;
	private String roleName;
	private String roleDescription;
	private String requestIp;
	private List<PrivilegeDataBean> privilegeList;

	public RolePrivilegeBean() {
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
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

	@Override
	public String toString() {
		return "RolePrivilegeBean [creatorUserId=" + creatorUserId
				+ ", userId=" + userId + ", roleId=" + roleId + ", roleName="
				+ roleName + ", roleDescription=" + roleDescription
				+ ", requestIp=" + requestIp + ", privilegeList="
				+ privilegeList + "]";
	}

}