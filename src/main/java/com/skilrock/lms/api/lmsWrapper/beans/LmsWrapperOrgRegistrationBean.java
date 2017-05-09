package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.List;

public class LmsWrapperOrgRegistrationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private List<String> terminalIdList;
    private String orgName;
    private String autoScrap;
	private String city;
	private String country;
	private String state;
	private String addrLine1;
	private String addrLine2;
	private String appLimit;
	private double creditLimit;
	private String orgType;
	private String payLimit;
	private String scrapLimit;
	private String statusorg;
	private String[] statusTable;
	private String vatRegNo;
	private String verLimit;
	private String emailPrivId[];
	private int agtOrgId;
	private int orgId;
	private String pinCode;
	private String reconReportType;
	private double security;
	private String agentName;
	private String sim[];
	private String orgCode;
	
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	private String simModelName[];
	public List<String> getTerminalIdList() {
		return terminalIdList;
	}
	public void setTerminalIdList(List<String> terminalIdList) {
		this.terminalIdList = terminalIdList;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public String getScrapLimit() {
		return scrapLimit;
	}
	public void setScrapLimit(String scrapLimit) {
		this.scrapLimit = scrapLimit;
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
	public int getAgtOrgId() {
		return agtOrgId;
	}
	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getReconReportType() {
		return reconReportType;
	}
	public void setReconReportType(String reconReportType) {
		this.reconReportType = reconReportType;
	}
	public double getSecurity() {
		return security;
	}
	public void setSecurity(double security) {
		this.security = security;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
}
