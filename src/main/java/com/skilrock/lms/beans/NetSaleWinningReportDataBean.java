package com.skilrock.lms.beans;

import java.io.Serializable;

public class NetSaleWinningReportDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	String orgName;
	double dgMrpSale;
	double dgNetSale;
	double dgPwt;
	double dgCommAmt;
	double dgSDAmt;
	double dgLeviAmt;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public double getDgMrpSale() {
		return dgMrpSale;
	}

	public void setDgMrpSale(double dgMrpSale) {
		this.dgMrpSale = dgMrpSale;
	}

	public double getDgNetSale() {
		return dgNetSale;
	}

	public void setDgNetSale(double dgNetSale) {
		this.dgNetSale = dgNetSale;
	}

	public double getDgPwt() {
		return dgPwt;
	}

	public void setDgPwt(double dgPwt) {
		this.dgPwt = dgPwt;
	}

	public double getDgCommAmt() {
		return dgCommAmt;
	}

	public void setDgCommAmt(double dgCommAmt) {
		this.dgCommAmt = dgCommAmt;
	}

	public double getDgSDAmt() {
		return dgSDAmt;
	}

	public void setDgSDAmt(double dgSDAmt) {
		this.dgSDAmt = dgSDAmt;
	}

	public double getDgLeviAmt() {
		return dgLeviAmt;
	}

	public void setDgLeviAmt(double dgLeviAmt) {
		this.dgLeviAmt = dgLeviAmt;
	}

	@Override
	public String toString() {
		return "NetSaleWinningReportDataBean [dgCommAmt=" + dgCommAmt
				+ ", dgLeviAmt=" + dgLeviAmt + ", dgMrpSale=" + dgMrpSale
				+ ", dgNetSale=" + dgNetSale + ", dgPwt=" + dgPwt
				+ ", dgSDAmt=" + dgSDAmt + ", orgName=" + orgName + "]";
	}

}
