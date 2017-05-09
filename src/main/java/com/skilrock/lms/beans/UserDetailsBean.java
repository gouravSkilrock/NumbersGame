package com.skilrock.lms.beans;

import java.io.Serializable;

public class UserDetailsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String boUserType;

	private String department;
	private String emailId;
	private String firstName;
	private String isOffline;
	private String lastName;
	private String mailingStatus;
	private String orgAddr1;
	private String orgAddr2;
	private String orgCity;
	private String orgCountry;
	private double orgCreditLimit;

	private int orgId;
	private String orgName;
	private long orgPin;
	private String orgState;
	private String orgStatus;
	private String orgType;
	private long mobileNbr;
	private long phoneNbr;
	private int roleId;
	private String secAns;

	private String secQues;
	private String status;
	private int userId;
	private String userName;
	private boolean isMailSend;
	private String orgStateCode;
	private String orgAreaName;

	private String isRoleHead;
	private int registerByUserId;
	private String registrationDate;
	private String registerByUserName;
	private int terminalCount ;
	private String outstandingBal ;

	public String getIsRoleHead() {
		return isRoleHead;
	}

	public void setIsRoleHead(String isRoleHead) {
		this.isRoleHead = isRoleHead;
	}

	public String getBoUserType() {
		return boUserType;
	}

	public String getDepartment() {
		return department;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getIsOffline() {
		return isOffline;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMailingStatus() {
		return mailingStatus;
	}

	public String getOrgAddr1() {
		return orgAddr1;
	}

	public String getOrgAddr2() {
		return orgAddr2;
	}

	public String getOrgCity() {
		return orgCity;
	}

	public String getOrgCountry() {
		return orgCountry;
	}

	public double getOrgCreditLimit() {
		return orgCreditLimit;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public long getOrgPin() {
		return orgPin;
	}

	public String getOrgState() {
		return orgState;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public String getOrgType() {
		return orgType;
	}

	public long getPhoneNbr() {
		return phoneNbr;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getSecAns() {
		return secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public String getStatus() {
		return status;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setBoUserType(String boUserType) {
		this.boUserType = boUserType;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMailingStatus(String mailingStatus) {
		this.mailingStatus = mailingStatus;
	}

	public void setOrgAddr1(String orgAddr1) {
		this.orgAddr1 = orgAddr1;
	}

	public void setOrgAddr2(String orgAddr2) {
		this.orgAddr2 = orgAddr2;
	}

	public void setOrgCity(String orgCity) {
		this.orgCity = orgCity;
	}

	public void setOrgCountry(String orgCountry) {
		this.orgCountry = orgCountry;
	}

	public void setOrgCreditLimit(double orgCreditLimit) {
		this.orgCreditLimit = orgCreditLimit;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgPin(long orgPin) {
		this.orgPin = orgPin;
	}

	public void setOrgState(String orgState) {
		this.orgState = orgState;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPhoneNbr(long phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isMailSend() {
		return isMailSend;
	}

	public void setMailSend(boolean isMailSend) {
		this.isMailSend = isMailSend;
	}

	public String getOrgStateCode() {
		return orgStateCode;
	}

	public void setOrgStateCode(String orgStateCode) {
		this.orgStateCode = orgStateCode;
	}

	public String getOrgAreaName() {
		return orgAreaName;
	}

	public void setOrgAreaName(String orgAreaName) {
		this.orgAreaName = orgAreaName;
	}

	public long getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(long mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public int getRegisterByUserId() {
		return registerByUserId;
	}

	public void setRegisterByUserId(int registerByUserId) {
		this.registerByUserId = registerByUserId;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRegisterByUserName() {
		return registerByUserName;
	}

	public void setRegisterByUserName(String registerByUserName) {
		this.registerByUserName = registerByUserName;
	}
	
	

	public int getTerminalCount() {
		return terminalCount;
	}

	public void setTerminalCount(int terminalCount) {
		this.terminalCount = terminalCount;
	}

	public String getOutstandingBal() {
		return outstandingBal;
	}

	public void setOutstandingBal(String outstandingBal) {
		this.outstandingBal = outstandingBal;
	}

	
	
	@Override
	public String toString() {
		return "UserDetailsBean [boUserType=" + boUserType + ", department="
				+ department + ", emailId=" + emailId + ", firstName="
				+ firstName + ", isMailSend=" + isMailSend + ", isOffline="
				+ isOffline + ", isRoleHead=" + isRoleHead + ", lastName="
				+ lastName + ", mailingStatus=" + mailingStatus
				+ ", mobileNbr=" + mobileNbr + ", orgAddr1=" + orgAddr1
				+ ", orgAddr2=" + orgAddr2 + ", orgAreaName=" + orgAreaName
				+ ", orgCity=" + orgCity + ", orgCountry=" + orgCountry
				+ ", orgCreditLimit=" + orgCreditLimit + ", orgId=" + orgId
				+ ", orgName=" + orgName + ", orgPin=" + orgPin + ", orgState="
				+ orgState + ", orgStateCode=" + orgStateCode + ", orgStatus="
				+ orgStatus + ", orgType=" + orgType + ", outstandingBal="
				+ outstandingBal + ", phoneNbr=" + phoneNbr
				+ ", registerByUserId=" + registerByUserId
				+ ", registerByUserName=" + registerByUserName
				+ ", registrationDate=" + registrationDate + ", roleId="
				+ roleId + ", secAns=" + secAns + ", secQues=" + secQues
				+ ", status=" + status + ", terminalCount=" + terminalCount
				+ ", userId=" + userId + ", userName=" + userName + "]";
	}

}