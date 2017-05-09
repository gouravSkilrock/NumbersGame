package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CancelTicketBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map<String, List<String>> advMsg;
	private String autoCancel;
	private String cancelChannel;
	private String cancelDrawIdStr;
	private String cancelTime;
	private String cancelType;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private String errMsg;
	private int gameNo;
	private int gameId;
	private boolean isError = false;
	private boolean isValid = true;
	private String panelIdStr;
	private int partyId;
	private String partyType;
	private List<String> promoTicketList;
	Map<Integer, Map<Integer, String>> raffleDrawIdTableMap;
	private String refMerchantId;
	private String refTransId;
	private double refundAmount;
	private String reprintCount;
	private String ticketNo;
	private int userId;
	private boolean isAutoCancel;
	private boolean isCancelDuaraion;
	private int cancelDuration;
	private int autoCancelHoldDays;
	private boolean isHoldAutoCancel;
	private int dayOfTicket;
	private String reason;
	private int barCodeCount;
	private int inpType;
	private boolean isPromo;

	public boolean isCancelDuaraion() {
		return isCancelDuaraion;
	}

	public void setCancelDuaraion(boolean isCancelDuaraion) {
		this.isCancelDuaraion = isCancelDuaraion;
	}

	public int getCancelDuration() {
		return cancelDuration;
	}

	public void setCancelDuration(int cancelDuration) {
		this.cancelDuration = cancelDuration;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public String getAutoCancel() {
		return autoCancel;
	}

	public String getCancelChannel() {
		return cancelChannel;
	}

	public String getCancelDrawIdStr() {
		return cancelDrawIdStr;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public String getCancelType() {
		return cancelType;
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getPanelIdStr() {
		return panelIdStr;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public List<String> getPromoTicketList() {
		return promoTicketList;
	}

	public Map<Integer, Map<Integer, String>> getRaffleDrawIdTableMap() {
		return raffleDrawIdTableMap;
	}

	public String getRefMerchantId() {
		return refMerchantId;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public String getReprintCount() {
		return reprintCount;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public void setAutoCancel(String autoCancel) {
		this.autoCancel = autoCancel;
	}

	public void setCancelChannel(String cancelChannel) {
		this.cancelChannel = cancelChannel;
	}

	public void setCancelDrawIdStr(String cancelDrawIdStr) {
		this.cancelDrawIdStr = cancelDrawIdStr;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public void setDrawIdTableMap(
			Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setPanelIdStr(String panelIdStr) {
		this.panelIdStr = panelIdStr;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPromoTicketList(List<String> promoTicketList) {
		this.promoTicketList = promoTicketList;
	}

	public void setRaffleDrawIdTableMap(
			Map<Integer, Map<Integer, String>> raffleDrawIdTableMap) {
		this.raffleDrawIdTableMap = raffleDrawIdTableMap;
	}

	public void setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isAutoCancel() {
		return isAutoCancel;
	}

	public void setAutoCancel(boolean isAutoCancel) {
		this.isAutoCancel = isAutoCancel;
	}

	@Override
	public String toString() {
		return "CancelTicketBean [advMsg=" + advMsg + ", autoCancel="
				+ autoCancel + ", cancelChannel=" + cancelChannel
				+ ", cancelDrawIdStr=" + cancelDrawIdStr + ", cancelDuration="
				+ cancelDuration + ", cancelTime=" + cancelTime
				+ ", cancelType=" + cancelType + ", drawIdTableMap="
				+ drawIdTableMap + ", errMsg=" + errMsg + ", gameNo=" + gameNo
				+ ", isAutoCancel=" + isAutoCancel + ", isCancelDuaraion="
				+ isCancelDuaraion + ", isError=" + isError + ", isValid="
				+ isValid + ", panelIdStr=" + panelIdStr + ", partyId="
				+ partyId + ", partyType=" + partyType + ", promoTicketList="
				+ promoTicketList + ", raffleDrawIdTableMap="
				+ raffleDrawIdTableMap + ", refMerchantId=" + refMerchantId
				+ ", refTransId=" + refTransId + ", refundAmount="
				+ refundAmount + ", reprintCount=" + reprintCount
				+ ", ticketNo=" + ticketNo + ", userId=" + userId + "]";
	}

	public void setAutoCancelHoldDays(int autoCancelHoldDays) {
		this.autoCancelHoldDays = autoCancelHoldDays;
	}

	public int getAutoCancelHoldDays() {
		return autoCancelHoldDays;
	}

	public void setHoldAutoCancel(boolean isHoldAutoCancel) {
		this.isHoldAutoCancel = isHoldAutoCancel;
	}

	public boolean isHoldAutoCancel() {
		return isHoldAutoCancel;
	}

	public void setDayOfTicket(int dayOfTicket) {
		this.dayOfTicket = dayOfTicket;
	}

	public int getDayOfTicket() {
		return dayOfTicket;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getBarCodeCount() {
		return barCodeCount;
	}

	public void setBarCodeCount(int barCodeCount) {
		this.barCodeCount = barCodeCount;
	}

	public int getInpType() {
		return inpType;
	}

	public void setInpType(int inpType) {
		this.inpType = inpType;
	}

	public boolean isPromo() {
		return isPromo;
	}

	public void setPromo(boolean isPromo) {
		this.isPromo = isPromo;
	}
}