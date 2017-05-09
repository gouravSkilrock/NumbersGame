package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LmsWrapperMainPWTDrawBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isReprint = false;
	private boolean isValid = true;
	private LmsWrapperKenoPurchaseBean purchaseBean;
	private String pwtTicketType;
	private String status;
	private String pwtStatus;
	private String ticketNo;
	private double totlticketAmount;
	private boolean isWinTkt;
	private boolean isHighPrize;
	private HashMap<String, ArrayList<String>> advMsg;

	private List<LmsWrapperPWTDrawBean> winningBeanList;
	private int mainTktGameNo; 
	private List<Long> transactionIdList;//changes for API...
	
	public List<Long> getTransactionIdList() {
		return transactionIdList;
	}

	public void setTransactionIdList(List<Long> transactionIdList) {
		this.transactionIdList = transactionIdList;
	}

	//added for payment through mPesa
	private boolean ismPesaEnable;
	private String refNumber;
	private String mobileNumber;
	private String orgName;

	public LmsWrapperKenoPurchaseBean getPurchaseBean() {
		return purchaseBean;
	}

	public String getPwtTicketType() {
		return pwtTicketType;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public double getTotlticketAmount() {
		return totlticketAmount;
	}

	public List<LmsWrapperPWTDrawBean> getWinningBeanList() {
		return winningBeanList;
	}

	public boolean isReprint() {
		return isReprint;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setPurchaseBean(LmsWrapperKenoPurchaseBean purchaseBean) {
		this.purchaseBean = purchaseBean;
	}

	public void setPwtTicketType(String pwtTicketType) {
		this.pwtTicketType = pwtTicketType;
	}

	public void setReprint(boolean isReprint) {
		this.isReprint = isReprint;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setTotlticketAmount(double totlticketAmount) {
		this.totlticketAmount = totlticketAmount;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setWinningBeanList(List<LmsWrapperPWTDrawBean> winningBeanList) {
		this.winningBeanList = winningBeanList;
	}

	public String getPwtStatus() {
		return pwtStatus;
	}

	public void setPwtStatus(String pwtStatus) {
		this.pwtStatus = pwtStatus;
	}

	public boolean isWinTkt() {
		return isWinTkt;
	}

	public void setWinTkt(boolean isWinTkt) {
		this.isWinTkt = isWinTkt;
	}

	public boolean isHighPrize() {
		return isHighPrize;
	}

	public void setHighPrize(boolean isHighPrize) {
		this.isHighPrize = isHighPrize;
	}

	public int getMainTktGameNo() {
		return mainTktGameNo;
	}

	public void setMainTktGameNo(int mainTktGameNo) {
		this.mainTktGameNo = mainTktGameNo;
	}

	public HashMap<String, ArrayList<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(HashMap<String, ArrayList<String>> advMsg) {
		this.advMsg = advMsg;
	}



	public boolean isIsmPesaEnable() {
		return ismPesaEnable;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setIsmPesaEnable(boolean ismPesaEnable) {
		this.ismPesaEnable = ismPesaEnable;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}