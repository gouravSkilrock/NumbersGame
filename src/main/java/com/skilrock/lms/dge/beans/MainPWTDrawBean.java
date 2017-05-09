package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainPWTDrawBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isReprint = false;
	private boolean isValid = true;
	private Object purchaseBean;
	private String pwtTicketType;
	private String status;
	private String pwtStatus;
	private String ticketNo;
	private double govtTaxAmount;
	private double totlticketAmount;
	private boolean isWinTkt;
	private boolean isHighPrize;
	private Map<String, List<String>> advMsg;
	private List<PWTDrawBean> winningBeanList;
	private int mainTktGameNo;
	private List<Long> transactionIdList;
	private boolean ismPesaEnable;
	private String refNumber;
	private String mobileNumber;
	private int InpType;
	private int gameId;
	private int barcodeCount;
	
	

	public int getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(int barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public Object getPurchaseBean() {
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

	public List<PWTDrawBean> getWinningBeanList() {
		return winningBeanList;
	}

	public boolean isReprint() {
		return isReprint;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setPurchaseBean(Object purchaseBean) {
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

	public void setWinningBeanList(List<PWTDrawBean> winningBeanList) {
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

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public List<Long> getTransactionIdList() {
		return transactionIdList;
	}

	public void setTransactionIdList(List<Long> transactionIdList) {
		this.transactionIdList = transactionIdList;
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

	public int getInpType() {
		return InpType;
	}

	public void setInpType(int inpType) {
		InpType = inpType;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

	public double getGovtTaxAmount() {
		return govtTaxAmount;
	}

	public void setGovtTaxAmount(double govtTaxAmount) {
		this.govtTaxAmount = govtTaxAmount;
	}
}