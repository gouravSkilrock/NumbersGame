package com.skilrock.lms.beans;

import java.util.Arrays;

public class AgentRegistrationBean {

	private String orgName;
	private String userName;
	private String idNo;
	private String idType;
	private String autoScrap;
	private String city;
	private String country;
	private String state;
	private String addrLine1;
	private String addrLine2;
	private String appLimit;
	private double creditLimit;
	private String email;
	private String firstName;
	private int id[];
	private String lastName;
	private String orgType;
	private String payLimit;
	private long phone;
	private long mobile;
	private long pin;
	private int pntId;
	private String reconReportType;
	private String role;
	private String scrapLimit;
	private String secAns;
	private String secQues;
	private double security;
	private String status;
	private String statusorg;
	private String[] statusTable;
	private String vatRegNo;
	private String verLimit;
	private String emailPrivId[];
	private double maxPerDayPayLimit;
	private String area;
	private String division;
	private int branchId;
	private int bankId;
	private String accountNbr;
	private String selfClaim;
	private String otherClaim;
	private double minClaimPerTicket;
	private double maxClaimPerTicket;
	private double blockAmt;
	private int blockDays;
	private String blockAction;
	private String regFrom;
	private String tpUserId;

	public String getTpUserId() {
		return tpUserId;
	}

	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}

	public enum Action {
		LOGIN_BLOCK, SALE_BLOCK, NO_ACTION;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getAutoScrap() {
		return autoScrap;
	}

	public void setAutoScrap(String autoScrap) {
		this.autoScrap = autoScrap;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public String getAppLimit() {
		return appLimit;
	}

	public void setAppLimit(String appLimit) {
		this.appLimit = appLimit;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int[] getId() {
		return id;
	}

	public void setId(int[] id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getPayLimit() {
		return payLimit;
	}

	public void setPayLimit(String payLimit) {
		this.payLimit = payLimit;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public long getPin() {
		return pin;
	}

	public void setPin(long pin) {
		this.pin = pin;
	}

	public int getPntId() {
		return pntId;
	}

	public void setPntId(int pntId) {
		this.pntId = pntId;
	}

	public String getReconReportType() {
		return reconReportType;
	}

	public void setReconReportType(String reconReportType) {
		this.reconReportType = reconReportType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getScrapLimit() {
		return scrapLimit;
	}

	public void setScrapLimit(String scrapLimit) {
		this.scrapLimit = scrapLimit;
	}

	public String getSecAns() {
		return secAns;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
	}

	public double getSecurity() {
		return security;
	}

	public void setSecurity(double security) {
		this.security = security;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusorg() {
		return statusorg;
	}

	public void setStatusorg(String statusorg) {
		this.statusorg = statusorg;
	}

	public String[] getStatusTable() {
		return statusTable;
	}

	public void setStatusTable(String[] statusTable) {
		this.statusTable = statusTable;
	}

	public String getVatRegNo() {
		return vatRegNo;
	}

	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}

	public String getVerLimit() {
		return verLimit;
	}

	public void setVerLimit(String verLimit) {
		this.verLimit = verLimit;
	}

	public String[] getEmailPrivId() {
		return emailPrivId;
	}

	public void setEmailPrivId(String[] emailPrivId) {
		this.emailPrivId = emailPrivId;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public double getMaxPerDayPayLimit() {
		return maxPerDayPayLimit;
	}

	public void setMaxPerDayPayLimit(double maxPerDayPayLimit) {
		this.maxPerDayPayLimit = maxPerDayPayLimit;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getAccountNbr() {
		return accountNbr;
	}

	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}

	public String getSelfClaim() {
		return selfClaim;
	}

	public void setSelfClaim(String selfClaim) {
		this.selfClaim = selfClaim;
	}

	public String getOtherClaim() {
		return otherClaim;
	}

	public void setOtherClaim(String otherClaim) {
		this.otherClaim = otherClaim;
	}

	public double getMinClaimPerTicket() {
		return minClaimPerTicket;
	}

	public void setMinClaimPerTicket(double minClaimPerTicket) {
		this.minClaimPerTicket = minClaimPerTicket;
	}

	public double getMaxClaimPerTicket() {
		return maxClaimPerTicket;
	}

	public void setMaxClaimPerTicket(double maxClaimPerTicket) {
		this.maxClaimPerTicket = maxClaimPerTicket;
	}

	public double getBlockAmt() {
		return blockAmt;
	}

	public void setBlockAmt(double blockAmt) {
		this.blockAmt = blockAmt;
	}

	public int getBlockDays() {
		return blockDays;
	}

	public void setBlockDays(int blockDays) {
		this.blockDays = blockDays;
	}

	public String getBlockAction() {
		return blockAction;
	}

	public void setBlockAction(String blockAction) {
		this.blockAction = blockAction;
	}

	public String getRegFrom() {
		return regFrom;
	}

	public void setRegFrom(String regFrom) {
		this.regFrom = regFrom;
	}

	@Override
	public String toString() {
		return "AgentRegistrationBean [orgName=" + orgName + ", userName="
				+ userName + ", idNo=" + idNo + ", idType=" + idType
				+ ", autoScrap=" + autoScrap + ", city=" + city + ", country="
				+ country + ", state=" + state + ", addrLine1=" + addrLine1
				+ ", addrLine2=" + addrLine2 + ", appLimit=" + appLimit
				+ ", creditLimit=" + creditLimit + ", email=" + email
				+ ", firstName=" + firstName + ", id=" + Arrays.toString(id)
				+ ", lastName=" + lastName + ", orgType=" + orgType
				+ ", payLimit=" + payLimit + ", phone=" + phone + ", mobile="
				+ mobile + ", pin=" + pin + ", pntId=" + pntId
				+ ", reconReportType=" + reconReportType + ", role=" + role
				+ ", scrapLimit=" + scrapLimit + ", secAns=" + secAns
				+ ", secQues=" + secQues + ", security=" + security
				+ ", status=" + status + ", statusorg=" + statusorg
				+ ", statusTable=" + Arrays.toString(statusTable)
				+ ", vatRegNo=" + vatRegNo + ", verLimit=" + verLimit
				+ ", emailPrivId=" + Arrays.toString(emailPrivId)
				+ ", maxPerDayPayLimit=" + maxPerDayPayLimit + ", area=" + area
				+ ", division=" + division + ", branchId=" + branchId
				+ ", bankId=" + bankId + ", accountNbr=" + accountNbr
				+ ", selfClaim=" + selfClaim + ", otherClaim=" + otherClaim
				+ ", minClaimPerTicket=" + minClaimPerTicket
				+ ", maxClaimPerTicket=" + maxClaimPerTicket + ", blockAmt="
				+ blockAmt + ", blockDays=" + blockDays + ", blockAction="
				+ blockAction + ", regFrom=" + regFrom + "]";
	}

}