package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class PWTApiBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ticketNo;
	private String totalWinning;
	private List<DrawDetailsBean> drawBeanList;
	private List<Long> lmsTranxIdList;	
	private double balance;
	private boolean isReprntReq;
	private String reprintTicketGameCode;
	private ReprintBean reprintBean;
	private boolean isSuccess;
	private String errorCode;
	private String gameCode;
	private String refTransId;
	private RaffleBean raffleBean;
	
	public String getTicketNo() {
		return ticketNo;
	}
	public String getTotalWinning() {
		return totalWinning;
	}
	public List<DrawDetailsBean> getDrawBeanList() {
		return drawBeanList;
	}
	public List<Long> getLmsTranxIdList() {
		return lmsTranxIdList;
	}
	public double getBalance() {
		return balance;
	}
	public boolean isReprntReq() {
		return isReprntReq;
	}
	public String getReprintTicketGameCode() {
		return reprintTicketGameCode;
	}
	public ReprintBean getReprintBean() {
		return reprintBean;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	
	public String getGameCode() {
		return gameCode;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public void setTotalWinning(String totalWinning) {
		this.totalWinning = totalWinning;
	}
	public void setDrawBeanList(List<DrawDetailsBean> drawBeanList) {
		this.drawBeanList = drawBeanList;
	}
	public void setLmsTranxIdList(List<Long> lmsTranxIdList) {
		this.lmsTranxIdList = lmsTranxIdList;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void setReprntReq(boolean isReprntReq) {
		this.isReprntReq = isReprntReq;
	}
	public void setReprintTicketGameCode(String reprintTicketGameCode) {
		this.reprintTicketGameCode = reprintTicketGameCode;
	}
	public void setReprintBean(ReprintBean reprintBean) {
		this.reprintBean = reprintBean;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public RaffleBean getRaffleBean() {
		return raffleBean;
	}
	public void setRaffleBean(RaffleBean raffleBean) {
		this.raffleBean = raffleBean;
	}
	
	
	
	
	
}
