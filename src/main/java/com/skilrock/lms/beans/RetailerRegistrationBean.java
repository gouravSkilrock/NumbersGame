package com.skilrock.lms.beans;

import java.util.Arrays;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class RetailerRegistrationBean {
	@NotNull(message="Address should not be null")
	@Size(min=1,message="Address should not be empty")
	private String addrLine1;
	private String addrLine2;
	private int agtOrgId;
	private String appLimit;
	private String autoScrap;
	@NotNull(message="City should not be null")
	@Size(min=1,message="City should not be empty")
	private String city;
	@NotNull(message="Country should not be null")
	@Size(min=1,message="Country should not be empty")
	private String country;
	private double creditLimit;
	@NotNull(message="Email Id should not be null")
	@Size(min=1,message="Email Id should not be empty")
	private String email;
	private String emailPrivId[];
	@NotNull(message="First Name should not be null")
	@Size(min=1,message="First Name should not be empty")
	@Pattern(regexp="[a-zA-Z]+", message="First Name should contain alphabets only")
	private String firstName;
	private int id[];
	@NotNull(message="Id number should not be null")
	@Size(min=1,message="Id number should not be empty")
	private String idNo;
	@NotNull(message="Please give an Id Type")
	@Size(min=1,message="Id Type should not be empty")
	private String idType;
	private String isOffLine;
	@NotNull(message="Last Name should not be null")
	@Size(min=1,message="Last Name should not be empty")
	@Pattern(regexp="[a-zA-Z]+", message="Last Name should contain alphabets only")
	private String lastName;
	@NotNull(message="Organization Name should not be null")
	@Size(min=1,message="Organization Name should not be empty")
	private String orgName;
	private String orgType;
	private String payLimit;
	private long phone;
	@Min(message="mobile number should not be empty or null",value=1 )
	private long mobile;
	private long pin;
	private int pntId;
	private String reconReportType;
	private String role;
	private String scrapLimit;
	private String secAns;
	private String secQues;
	private double security;
	@NotNull(message="State should not be null")
	@Size(min=1,message="State should not be empty")
	private String state;
	private String status;
	private String statusorg;
	private String[] statusTable;
	private String olaDepositLimit;
	private String olaWithdrawalLimit;
	private String terminalId;
	@NotNull(message="User Name not mentioned")
	@Size(min=1,message="User Name should not be empty")
	private String userName;
	private String vatRegNo;
	// private String isCreditSale;
	private String verLimit;
	private String modelName;
	private String isRetailerOnline;
	private double maxPerDayPayLimit;
	private String area;
	private String division;
	private String sim[];
	private String simModelName[];
	private String selfClaim;
	private String otherClaim;
	private double minClaimPerTicket;
	private double maxClaimPerTicket;
	private double blockAmt;
	private int blockDays;
	private String blockAction;
	private int registerById;
	private int userId;
	private int orgId;
	@NotNull(message="TpUserId should not be null")
	@Size(min=1,message="TpUserId should not be empty")
	private String tpUserId;

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getTpUserId() {
		return tpUserId;
	}

	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public String getAppLimit() {
		return appLimit;
	}

	public void setAppLimit(String appLimit) {
		this.appLimit = appLimit;
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

	public String[] getEmailPrivId() {
		return emailPrivId;
	}

	public void setEmailPrivId(String[] emailPrivId) {
		this.emailPrivId = emailPrivId;
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

	public String getIsOffLine() {
		return isOffLine;
	}

	public void setIsOffLine(String isOffLine) {
		this.isOffLine = isOffLine;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getOlaDepositLimit() {
		return olaDepositLimit;
	}

	public void setOlaDepositLimit(String olaDepositLimit) {
		this.olaDepositLimit = olaDepositLimit;
	}

	public String getOlaWithdrawalLimit() {
		return olaWithdrawalLimit;
	}

	public void setOlaWithdrawalLimit(String olaWithdrawalLimit) {
		this.olaWithdrawalLimit = olaWithdrawalLimit;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getIsRetailerOnline() {
		return isRetailerOnline;
	}

	public void setIsRetailerOnline(String isRetailerOnline) {
		this.isRetailerOnline = isRetailerOnline;
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

	public String[] getSim() {
		return sim;
	}

	public void setSim(String[] sim) {
		this.sim = sim;
	}

	public String[] getSimModelName() {
		return simModelName;
	}

	public void setSimModelName(String[] simModelName) {
		this.simModelName = simModelName;
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

	public int getRegisterById() {
		return registerById;
	}

	public void setRegisterById(int registerById) {
		this.registerById = registerById;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
	@Override
	public String toString() {
		return "RetailerRegistrationBean [addrLine1=" + addrLine1
				+ ", addrLine2=" + addrLine2 + ", agtOrgId=" + agtOrgId
				+ ", appLimit=" + appLimit + ", autoScrap=" + autoScrap
				+ ", city=" + city + ", country=" + country + ", creditLimit="
				+ creditLimit + ", email=" + email + ", emailPrivId="
				+ Arrays.toString(emailPrivId) + ", firstName=" + firstName
				+ ", id=" + Arrays.toString(id) + ", idNo=" + idNo
				+ ", idType=" + idType + ", isOffLine=" + isOffLine
				+ ", lastName=" + lastName + ", orgName=" + orgName
				+ ", orgType=" + orgType + ", payLimit=" + payLimit
				+ ", phone=" + phone + ", mobile=" + mobile + ", pin=" + pin
				+ ", pntId=" + pntId + ", reconReportType=" + reconReportType
				+ ", role=" + role + ", scrapLimit=" + scrapLimit + ", secAns="
				+ secAns + ", secQues=" + secQues + ", security=" + security
				+ ", state=" + state + ", status=" + status + ", statusorg="
				+ statusorg + ", statusTable=" + Arrays.toString(statusTable)
				+ ", olaDepositLimit=" + olaDepositLimit
				+ ", olaWithdrawalLimit=" + olaWithdrawalLimit
				+ ", terminalId=" + terminalId + ", userName=" + userName
				+ ", vatRegNo=" + vatRegNo + ", verLimit=" + verLimit
				+ ", modelName=" + modelName + ", isRetailerOnline="
				+ isRetailerOnline + ", maxPerDayPayLimit=" + maxPerDayPayLimit
				+ ", area=" + area + ", division=" + division + ", sim="
				+ Arrays.toString(sim) + ", simModelName="
				+ Arrays.toString(simModelName) + ", selfClaim=" + selfClaim
				+ ", otherClaim=" + otherClaim + ", minClaimPerTicket="
				+ minClaimPerTicket + ", maxClaimPerTicket="
				+ maxClaimPerTicket + ", blockAmt=" + blockAmt + ", blockDays="
				+ blockDays + ", blockAction=" + blockAction
				+ ", registerById=" + registerById + ", userId=" + userId
				+ ", orgId=" + orgId + "]";
	}

}