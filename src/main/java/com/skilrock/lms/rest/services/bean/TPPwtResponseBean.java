package com.skilrock.lms.rest.services.bean;

import java.util.List;
import java.util.Map;

public class TPPwtResponseBean {
	private int gameId;
	private int gameTypeId;
	private String ticketNumber;
	private Map<Integer, String> drawTransMap;
	private int doneByUserId;
	private String status;
	private String requestId;
	private double balance;
	private double oldBalance;
	private String txnId;
	private int responseCode;
	private String responseMsg;
	private Map<String, List<String>> advMessageMap;
	private double govtTaxPwt;
	private Map<String, List<String>> advMsg;
	
	public TPPwtResponseBean() {
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Map<Integer, String> getDrawTransMap() {
		return drawTransMap;
	}

	public void setDrawTransMap(Map<Integer, String> drawTransMap) {
		this.drawTransMap = drawTransMap;
	}

	public int getDoneByUserId() {
		return doneByUserId;
	}

	public void setDoneByUserId(int doneByUserId) {
		this.doneByUserId = doneByUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public Map<String, List<String>> getAdvMessageMap() {
		return advMessageMap;
	}

	public void setAdvMessageMap(Map<String, List<String>> advMessageMap) {
		this.advMessageMap = advMessageMap;
	}

	public double getGovtTaxPwt() {
		return govtTaxPwt;
	}

	public void setGovtTaxPwt(double govtTaxPwt) {
		this.govtTaxPwt = govtTaxPwt;
	}

	public double getOldBalance() {
		return oldBalance;
	}

	public void setOldBalance(double oldBalance) {
		this.oldBalance = oldBalance;
	}

	@Override
	public String toString() {
		return "TPPwtResponseBean [gameId=" + gameId + ", gameTypeId="
				+ gameTypeId + ", ticketNumber=" + ticketNumber
				+ ", drawTransMap=" + drawTransMap + ", doneByUserId="
				+ doneByUserId + ", status=" + status + ", requestId="
				+ requestId + ", balance=" + balance + ", oldBalance="
				+ oldBalance + ", txnId=" + txnId + ", responseCode="
				+ responseCode + ", responseMsg=" + responseMsg
				+ ", advMessageMap=" + advMessageMap + ", govtTaxPwt="
				+ govtTaxPwt + ", advMsg=" + advMsg + "]";
	}
}