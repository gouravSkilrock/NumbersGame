 package com.skilrock.lms.beans;

import java.io.Serializable;

public class OrganizationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appLimit;

	private double availableCredit;
	private double claimableBal;
	private double currentCreditAmt;
	private double extendedCredit;
	private int extendsCreditLimitUpto;
	private double ledgerBalance;
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
	private int parentOrgId;
	private String parentOrgName;
	private String parPwtScrap;
	private String payLimit;

	private String pwtScrapStatus;
	private String scrapLimit;
	private double secDeposit;
	private double securityDeposit;
	private double unclaimableBal;

	private String vatRegNumber;
	private String verLimit;
	private String area;
	private String division;
	private String idType;
	private String idNbr;
	private String serialNo;
	private int terminalCount;
	private String outstandingBal ;
	
	private String userOrgType;

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNbr() {
		return idNbr;
	}

	public void setIdNbr(String idNbr) {
		this.idNbr = idNbr;
	}

	public String getAppLimit() {
		return appLimit;
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public double getClaimableBal() {
		return claimableBal;
	}

	public double getCurrentCreditAmt() {
		return currentCreditAmt;
	}

	public double getExtendedCredit() {
		return extendedCredit;
	}

	public int getExtendsCreditLimitUpto() {
		return extendsCreditLimitUpto;
	}

	public double getLedgerBalance() {
		return ledgerBalance;
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

	public int getParentOrgId() {
		return parentOrgId;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public String getParPwtScrap() {
		return parPwtScrap;
	}

	public String getPayLimit() {
		return payLimit;
	}

	public String getPwtScrapStatus() {
		return pwtScrapStatus;
	}

	public String getScrapLimit() {
		return scrapLimit;
	}

	public double getSecDeposit() {
		return secDeposit;
	}

	public double getSecurityDeposit() {
		return securityDeposit;
	}

	public double getUnclaimableBal() {
		return unclaimableBal;
	}

	public String getVatRegNumber() {
		return vatRegNumber;
	}

	public String getVerLimit() {
		return verLimit;
	}

	public void setAppLimit(String appLimit) {
		this.appLimit = appLimit;
	}

	public void setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public void setClaimableBal(double claimableBal) {
		this.claimableBal = claimableBal;
	}

	public void setCurrentCreditAmt(double currentCreditAmt) {
		this.currentCreditAmt = currentCreditAmt;
	}

	public void setExtendedCredit(double extendedCredit) {
		this.extendedCredit = extendedCredit;
	}

	public void setExtendsCreditLimitUpto(int extendsCreditLimitUpto) {
		this.extendsCreditLimitUpto = extendsCreditLimitUpto;
	}

	public void setLedgerBalance(double ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
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

	public void setOrgCreditLimit(int orgCreditLimit) {
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

	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public void setParPwtScrap(String parPwtScrap) {
		this.parPwtScrap = parPwtScrap;
	}

	public void setPayLimit(String payLimit) {
		this.payLimit = payLimit;
	}

	public void setPwtScrapStatus(String pwtScrapStatus) {
		this.pwtScrapStatus = pwtScrapStatus;
	}

	public void setScrapLimit(String scrapLimit) {
		this.scrapLimit = scrapLimit;
	}

	public void setSecDeposit(double secDeposit) {
		this.secDeposit = secDeposit;
	}

	public void setSecurityDeposit(double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public void setUnclaimableBal(double unclaimableBal) {
		this.unclaimableBal = unclaimableBal;
	}

	public void setVatRegNumber(String vatRegNumber) {
		this.vatRegNumber = vatRegNumber;
	}

	public void setVerLimit(String verLimit) {
		this.verLimit = verLimit;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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
		return "OrganizationBean [appLimit=" + appLimit + ", area=" + area
				+ ", availableCredit=" + availableCredit + ", claimableBal="
				+ claimableBal + ", currentCreditAmt=" + currentCreditAmt
				+ ", division=" + division + ", extendedCredit="
				+ extendedCredit + ", extendsCreditLimitUpto="
				+ extendsCreditLimitUpto + ", idNbr=" + idNbr + ", idType="
				+ idType + ", ledgerBalance=" + ledgerBalance + ", orgAddr1="
				+ orgAddr1 + ", orgAddr2=" + orgAddr2 + ", orgCity=" + orgCity
				+ ", orgCountry=" + orgCountry + ", orgCreditLimit="
				+ orgCreditLimit + ", orgId=" + orgId + ", orgName=" + orgName
				+ ", orgPin=" + orgPin + ", orgState=" + orgState
				+ ", orgStatus=" + orgStatus + ", orgType=" + orgType
				+ ", outstandingBal=" + outstandingBal + ", parPwtScrap="
				+ parPwtScrap + ", parentOrgId=" + parentOrgId
				+ ", parentOrgName=" + parentOrgName + ", payLimit=" + payLimit
				+ ", pwtScrapStatus=" + pwtScrapStatus + ", scrapLimit="
				+ scrapLimit + ", secDeposit=" + secDeposit
				+ ", securityDeposit=" + securityDeposit + ", serialNo="
				+ serialNo + ", terminalCount=" + terminalCount
				+ ", unclaimableBal=" + unclaimableBal + ", vatRegNumber="
				+ vatRegNumber + ", verLimit=" + verLimit + "]";
	}

	public String getUserOrgType() {
		return userOrgType;
	}

	public void setUserOrgType(String userOrgType) {
		this.userOrgType = userOrgType;
	}

}