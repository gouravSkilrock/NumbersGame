package com.skilrock.lms.beans;

import java.io.Serializable;

public class UserInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double availableCreditLimit;
	private String channel;
	private double claimableBal;
	private Double currentCreditAmt;
	private Double extendedCreditLimit;
	private String interfaceType;
	private String isMasterRole;
	private String isRoleHeadUser;
	private String loginChannel;
	private String orgName;
	private String orgStatus;
	private int parentUserId;
	private int parentOrgId;
	private String parentOrgName;
	private String parentOrgCode;
	private String parentOrgStatus;
	private String pwtSacrap;
	private java.sql.Date regDate;
	private int roleId;
	private String roleName;
	private String status;
	private int tierId;
	private double unclaimableBal;
	private int userId;
	private int currentUserMappingId;
	private String userName;
	private int userOrgId;
	private String userType; // tier code
	private boolean isTPUser;
	private String tpIp;
	private String tpTxnPassword;
	private String orgCode;   // This code contains name of the organization as per the property NAME,NAME_CODE,CODE_NAME
	private String userOrgCode;// this the org code of the login user
	private double terminalBuildVersion; // FOR BACKWARD COMPATIBILITY 
	private String userSession;
	private String outstandingBal;
	
	
	

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getTpIp() {
		return tpIp;
	}

	public String getTpTxnPassword() {
		return tpTxnPassword;
	}

	public void setTpIp(String tpIp) {
		this.tpIp = tpIp;
	}

	public void setTpTxnPassword(String tpTxnPassword) {
		this.tpTxnPassword = tpTxnPassword;
	}

	public boolean isTPUser() {
		return isTPUser;
	}

	public void setTPUser(boolean isTPUser) {
		this.isTPUser = isTPUser;
	}

	public Double getAvailableCreditLimit() {
		return availableCreditLimit;
	}

	public String getChannel() {
		return channel;
	}

	public double getClaimableBal() {
		return claimableBal;
	}

	public Double getCurrentCreditAmt() {
		return currentCreditAmt;
	}

	public Double getExtendedCreditLimit() {
		return extendedCreditLimit;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public String getIsMasterRole() {
		return isMasterRole;
	}

	public String getIsRoleHeadUser() {
		return isRoleHeadUser;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public int getParentOrgId() {
		return parentOrgId;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public String getPwtSacrap() {
		return pwtSacrap;
	}

	public java.sql.Date getRegDate() {
		return regDate;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getStatus() {
		return status;
	}

	public int getTierId() {
		return tierId;
	}

	public double getUnclaimableBal() {
		return unclaimableBal;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getUserOrgId() {
		return userOrgId;
	}

	public String getUserType() {
		return userType;
	}

	public void setAvailableCreditLimit(Double availableCreditLimit) {
		this.availableCreditLimit = availableCreditLimit;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setClaimableBal(double claimableBal) {
		this.claimableBal = claimableBal;
	}

	public void setCurrentCreditAmt(Double currentCreditAmt) {
		this.currentCreditAmt = currentCreditAmt;
	}

	public void setExtendedCreditLimit(Double extendedCreditLimit) {
		this.extendedCreditLimit = extendedCreditLimit;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public void setIsMasterRole(String isMasterRole) {
		this.isMasterRole = isMasterRole;
	}

	public void setIsRoleHeadUser(String isRoleHeadUser) {
		this.isRoleHeadUser = isRoleHeadUser;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public void setPwtSacrap(String pwtSacrap) {
		this.pwtSacrap = pwtSacrap;
	}

	public void setRegDate(java.sql.Date regDate) {
		this.regDate = regDate;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTierId(int tierId) {
		this.tierId = tierId;
	}

	public void setUnclaimableBal(double unclaimableBal) {
		this.unclaimableBal = unclaimableBal;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginChannel() {
		return loginChannel;
	}

	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}

	public int getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(int parentUserId) {
		this.parentUserId = parentUserId;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public String getParentOrgStatus() {
		return parentOrgStatus;
	}

	public void setParentOrgStatus(String parentOrgStatus) {
		this.parentOrgStatus = parentOrgStatus;
	}

	public String getUserOrgCode() {
		return userOrgCode;
	}

	public void setUserOrgCode(String userOrgCode) {
		this.userOrgCode = userOrgCode;
	}

	public double getTerminalBuildVersion() {
		return terminalBuildVersion;
	}

	public void setTerminalBuildVersion(double terminalBuildVersion) {
		this.terminalBuildVersion = terminalBuildVersion;
	}

	public int getCurrentUserMappingId() {
		return currentUserMappingId;
	}

	public void setCurrentUserMappingId(int currentUserMappingId) {
		this.currentUserMappingId = currentUserMappingId;
	}

	public String getOutstandingBal() {
		return outstandingBal;
	}

	public void setOutstandingBal(String outstandingBal) {
		this.outstandingBal = outstandingBal;
	}
	
	
	
	
}
