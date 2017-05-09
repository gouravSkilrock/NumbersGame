package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PWTDrawBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, List<String>> advMsg;
	private List<DrawIdBean> drawWinList;
	private String gameDispName;
	private int gameId;
	private int gameNo;
	private boolean isHighPrize;
	private boolean isRaffelAssociated;
	private boolean isReprint = false;
	private boolean isResAwaited;
	private boolean isValid = true;
	private boolean isWinTkt;
	private int partyId;
	private String partyType;
	private Object purchaseBean;
	private String pwtStatus;
	private String pwtTicketType;
	private List<RaffleDrawIdBean> raffleDrawIdBeanList;
	private String refMerchantId;
	private String reprintCount;
	private int barCodeCount;
	private String status;
	private String ticketNo;
	private boolean byPassDates;
	private double govtTaxAmount;
	private double totalAmount;
	private int userId;
	private int errorCode;
	private String errorMessage;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public List<DrawIdBean> getDrawWinList() {
		return drawWinList;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public Object getPurchaseBean() {
		return purchaseBean;
	}

	public String getPwtStatus() {
		return pwtStatus;
	}

	public String getPwtTicketType() {
		return pwtTicketType;
	}

	public List<RaffleDrawIdBean> getRaffleDrawIdBeanList() {
		return raffleDrawIdBeanList;
	}

	public String getRefMerchantId() {
		return refMerchantId;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isHighPrize() {
		return isHighPrize;
	}

	public boolean isRaffelAssociated() {
		return isRaffelAssociated;
	}

	public boolean isReprint() {
		return isReprint;
	}

	public boolean isResAwaited() {
		return isResAwaited;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isWinTkt() {
		return isWinTkt;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public void setDrawWinList(List<DrawIdBean> drawWinList) {
		this.drawWinList = drawWinList;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setHighPrize(boolean isHighPrize) {
		this.isHighPrize = isHighPrize;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPurchaseBean(Object purchaseBean) {
		this.purchaseBean = purchaseBean;
	}

	public void setPwtStatus(String pwtStatus) {
		this.pwtStatus = pwtStatus;
	}

	public void setPwtTicketType(String pwtTicketType) {
		this.pwtTicketType = pwtTicketType;
	}

	public void setRaffelAssociated(boolean isRaffelAssociated) {
		this.isRaffelAssociated = isRaffelAssociated;
	}

	public void setRaffleDrawIdBeanList(List<RaffleDrawIdBean> raffleDrawIdBeanList) {
		this.raffleDrawIdBeanList = raffleDrawIdBeanList;
	}

	public void setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
	}

	public void setReprint(boolean isReprint) {
		this.isReprint = isReprint;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public void setResAwaited(boolean isResAwaited) {
		this.isResAwaited = isResAwaited;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setWinTkt(boolean isWinTkt) {
		this.isWinTkt = isWinTkt;
	}

	public int getBarCodeCount() {
		return barCodeCount;
	}

	public void setBarCodeCount(int barCodeCount) {
		this.barCodeCount = barCodeCount;
	}

	public boolean isByPassDates() {
		return byPassDates;
	}

	public void setByPassDates(boolean byPassDates) {
		this.byPassDates = byPassDates;
	}

	public double getGovtTaxAmount() {
		return govtTaxAmount;
	}

	public void setGovtTaxAmount(double govtTaxAmount) {
		this.govtTaxAmount = govtTaxAmount;
	}
}