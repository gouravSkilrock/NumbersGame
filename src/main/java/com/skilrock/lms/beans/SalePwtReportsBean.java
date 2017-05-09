package com.skilrock.lms.beans;

import org.apache.commons.collections.Predicate;

import com.skilrock.lms.dge.beans.UssdSubscriberDataBean;

public class SalePwtReportsBean implements Predicate {
	String gameName;
	int gameNo;
	int noOfTkt;
	int noOfLosTkt;
	double priceAmt;
	double pwtMrpAmt;
	double pwtNetAmt;
	double saleMrpAmt;
	double saleNetAmt;
	double unitPriceAmt;
	String stateCode;
	String cityCode;

	public int getNoOfLosTkt() {
		return noOfLosTkt;
	}

	public void setNoOfLosTkt(int noOfLosTkt) {
		this.noOfLosTkt = noOfLosTkt;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public int getNoOfTkt() {
		return noOfTkt;
	}

	public double getPriceAmt() {
		return priceAmt;
	}

	public double getPwtMrpAmt() {
		return pwtMrpAmt;
	}

	public double getPwtNetAmt() {
		return pwtNetAmt;
	}

	public double getSaleMrpAmt() {
		return saleMrpAmt;
	}

	public double getSaleNetAmt() {
		return saleNetAmt;
	}

	public double getUnitPriceAmt() {
		return unitPriceAmt;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setNoOfTkt(int noOfTkt) {
		this.noOfTkt = noOfTkt;
	}

	public void setPriceAmt(double priceAmt) {
		this.priceAmt = priceAmt;
	}

	public void setPwtMrpAmt(double pwtMrpAmt) {
		this.pwtMrpAmt = pwtMrpAmt;
	}

	public void setPwtNetAmt(double pwtNetAmt) {
		this.pwtNetAmt = pwtNetAmt;
	}

	public void setSaleMrpAmt(double saleMrpAmt) {
		this.saleMrpAmt = saleMrpAmt;
	}

	public void setSaleNetAmt(double saleNetAmt) {
		this.saleNetAmt = saleNetAmt;
	}

	public void setUnitPriceAmt(double unitPriceAmt) {
		this.unitPriceAmt = unitPriceAmt;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public boolean evaluate(Object object1) {
		SalePwtReportsBean bean = (SalePwtReportsBean) object1;
		return ((String.valueOf(bean.getGameNo()).equals(String.valueOf(this.gameNo))));
	}

}
