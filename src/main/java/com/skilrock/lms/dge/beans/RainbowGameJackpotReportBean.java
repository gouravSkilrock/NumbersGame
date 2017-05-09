package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class RainbowGameJackpotReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String drawDate;
	private int drawId;
	private int eventId;
	private double totalAvailableJackpotAmtBasic;
	private double totalAvailableJackpotAmtPower;
	private double securityDepositAmt;
	private List<RainbowJackpotDataBean> jackpotDataList;

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public double getTotalAvailableJackpotAmtBasic() {
		return totalAvailableJackpotAmtBasic;
	}

	public void setTotalAvailableJackpotAmtBasic(double totalAvailableJackpotAmtBasic) {
		this.totalAvailableJackpotAmtBasic = totalAvailableJackpotAmtBasic;
	}

	public double getTotalAvailableJackpotAmtPower() {
		return totalAvailableJackpotAmtPower;
	}

	public void setTotalAvailableJackpotAmtPower(double totalAvailableJackpotAmtPower) {
		this.totalAvailableJackpotAmtPower = totalAvailableJackpotAmtPower;
	}

	public double getSecurityDepositAmt() {
		return securityDepositAmt;
	}

	public void setSecurityDepositAmt(double securityDepositAmt) {
		this.securityDepositAmt = securityDepositAmt;
	}

	public List<RainbowJackpotDataBean> getJackpotDataList() {
		return jackpotDataList;
	}

	public void setJackpotDataList(List<RainbowJackpotDataBean> jackpotDataList) {
		this.jackpotDataList = jackpotDataList;
	}

}
