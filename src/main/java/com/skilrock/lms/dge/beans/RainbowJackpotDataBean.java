package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class RainbowJackpotDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private int eventId;
	private String drawDate;
	private double carriedOverJackpotBasic;
	private double totalPrizeFundBasic;
	private double fixedPrizesFundBasic;
	private double jackpotForThisDrawBasic;
	private double totalAvailableJackpotBasic;
	private double totalSaleAmtBasic;
	private double carriedOverJackpotPower;
	private double totalPrizeFundPower;
	private double fixedPrizesFundPower;
	private double jackpotForThisDrawPower;
	private double totalAvailableJackpotPower;
	private double totalSaleAmtPower;

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

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public double getCarriedOverJackpotBasic() {
		return carriedOverJackpotBasic;
	}

	public void setCarriedOverJackpotBasic(double carriedOverJackpotBasic) {
		this.carriedOverJackpotBasic = carriedOverJackpotBasic;
	}

	public double getTotalPrizeFundBasic() {
		return totalPrizeFundBasic;
	}

	public void setTotalPrizeFundBasic(double totalPrizeFundBasic) {
		this.totalPrizeFundBasic = totalPrizeFundBasic;
	}

	public double getFixedPrizesFundBasic() {
		return fixedPrizesFundBasic;
	}

	public void setFixedPrizesFundBasic(double fixedPrizesFundBasic) {
		this.fixedPrizesFundBasic = fixedPrizesFundBasic;
	}

	public double getJackpotForThisDrawBasic() {
		return jackpotForThisDrawBasic;
	}

	public void setJackpotForThisDrawBasic(double jackpotForThisDrawBasic) {
		this.jackpotForThisDrawBasic = jackpotForThisDrawBasic;
	}

	public double getTotalAvailableJackpotBasic() {
		return totalAvailableJackpotBasic;
	}

	public void setTotalAvailableJackpotBasic(double totalAvailableJackpotBasic) {
		this.totalAvailableJackpotBasic = totalAvailableJackpotBasic;
	}
	
	public double getTotalSaleAmtBasic() {
		return totalSaleAmtBasic;
	}

	public void setTotalSaleAmtBasic(double totalSaleAmtBasic) {
		this.totalSaleAmtBasic = totalSaleAmtBasic;
	}

	public double getCarriedOverJackpotPower() {
		return carriedOverJackpotPower;
	}

	public void setCarriedOverJackpotPower(double carriedOverJackpotPower) {
		this.carriedOverJackpotPower = carriedOverJackpotPower;
	}

	public double getTotalPrizeFundPower() {
		return totalPrizeFundPower;
	}

	public void setTotalPrizeFundPower(double totalPrizeFundPower) {
		this.totalPrizeFundPower = totalPrizeFundPower;
	}

	public double getFixedPrizesFundPower() {
		return fixedPrizesFundPower;
	}

	public void setFixedPrizesFundPower(double fixedPrizesFundPower) {
		this.fixedPrizesFundPower = fixedPrizesFundPower;
	}

	public double getJackpotForThisDrawPower() {
		return jackpotForThisDrawPower;
	}

	public void setJackpotForThisDrawPower(double jackpotForThisDrawPower) {
		this.jackpotForThisDrawPower = jackpotForThisDrawPower;
	}

	public double getTotalAvailableJackpotPower() {
		return totalAvailableJackpotPower;
	}

	public void setTotalAvailableJackpotPower(double totalAvailableJackpotPower) {
		this.totalAvailableJackpotPower = totalAvailableJackpotPower;
	}

	public double getTotalSaleAmtPower() {
		return totalSaleAmtPower;
	}

	public void setTotalSaleAmtPower(double totalSaleAmtPower) {
		this.totalSaleAmtPower = totalSaleAmtPower;
	}
	
}
