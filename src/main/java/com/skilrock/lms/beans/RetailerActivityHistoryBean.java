package com.skilrock.lms.beans;

import java.io.Serializable;

public class RetailerActivityHistoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int dgCount;
	private int dgSaleCount;
	private int dgPwtCount;
	private int slCount;
	private int slSaleCount;
	private int slPwtCount;
	private int iwCount;
	private int iwSaleCount;
	private int iwPwtCount;
	private int seCount;
	private int seSaleCount;
	private int sePwtCount;
	private int olaCount;
	private int olaDepoCount;
	private int olaWdlCount;
	private int csCount;
	private int csSaleCount;
	private int loginCount;
	private int heartBeatCount;
	private int inactiveRetCount;
	private int terRetCount;
	private int totalCount;

	private double dgTotalSale;
	private double dgTotalPwt;
	private double seTotalSale;
	private double seTotalPwt;
	private double olaTotalDepo;
	private double olaTotalWdrl;
	private double csTotalSale;
	private int dgTicketCount;
	private int dgPwtTotalCount;
	private double dgAvgSalePerRet;
	private double slTotalSale;
	private double slTotalPwt;
	private int slTicketCount;
	private int slPwtTotalCount;
	private double slAvgSalePerRet;
	private double iwTotalSale;
	private double iwTotalPwt;
	private int iwTicketCount;
	private int iwPwtTotalCount;
	private double iwAvgSalePerRet;

	private int vsCount;
	private int vsSaleCount;
	private int vsPwtCount;
	private double vsTotalSale;
	private double vsTotalPwt;
	private int vsTicketCount;
	private int vsPwtTotalCount;
	private double vsAvgSalePerRet;

	public double getSlTotalSale() {
		return slTotalSale;
	}

	public void setSlTotalSale(double slTotalSale) {
		this.slTotalSale = slTotalSale;
	}

	public double getSlTotalPwt() {
		return slTotalPwt;
	}

	public void setSlTotalPwt(double slTotalPwt) {
		this.slTotalPwt = slTotalPwt;
	}

	public int getSlTicketCount() {
		return slTicketCount;
	}

	public void setSlTicketCount(int slTicketCount) {
		this.slTicketCount = slTicketCount;
	}

	public int getSlPwtTotalCount() {
		return slPwtTotalCount;
	}

	public void setSlPwtTotalCount(int slPwtTotalCount) {
		this.slPwtTotalCount = slPwtTotalCount;
	}

	public double getSlAvgSalePerRet() {
		return slAvgSalePerRet;
	}

	public void setSlAvgSalePerRet(double slAvgSalePerRet) {
		this.slAvgSalePerRet = slAvgSalePerRet;
	}

	public int getDgSaleCount() {
		return dgSaleCount;
	}

	public void setDgSaleCount(int dgSaleCount) {
		this.dgSaleCount = dgSaleCount;
	}

	public int getDgPwtCount() {
		return dgPwtCount;
	}

	public void setDgPwtCount(int dgPwtCount) {
		this.dgPwtCount = dgPwtCount;
	}

	public int getSeSaleCount() {
		return seSaleCount;
	}

	public void setSeSaleCount(int seSaleCount) {
		this.seSaleCount = seSaleCount;
	}

	public int getSlCount() {
		return slCount;
	}

	public void setSlCount(int slCount) {
		this.slCount = slCount;
	}

	public int getSlSaleCount() {
		return slSaleCount;
	}

	public void setSlSaleCount(int slSaleCount) {
		this.slSaleCount = slSaleCount;
	}

	public int getSlPwtCount() {
		return slPwtCount;
	}

	public void setSlPwtCount(int slPwtCount) {
		this.slPwtCount = slPwtCount;
	}

	public int getSePwtCount() {
		return sePwtCount;
	}

	public void setSePwtCount(int sePwtCount) {
		this.sePwtCount = sePwtCount;
	}

	public int getCsSaleCount() {
		return csSaleCount;
	}

	public void setCsSaleCount(int csSaleCount) {
		this.csSaleCount = csSaleCount;
	}

	public int getOlaDepoCount() {
		return olaDepoCount;
	}

	public void setOlaDepoCount(int olaDepoCount) {
		this.olaDepoCount = olaDepoCount;
	}

	public int getOlaWdlCount() {
		return olaWdlCount;
	}

	public void setOlaWdlCount(int olaWdlCount) {
		this.olaWdlCount = olaWdlCount;
	}

	public int getDgCount() {
		return dgCount;
	}

	public void setDgCount(int dgCount) {
		this.dgCount = dgCount;
	}

	public int getSeCount() {
		return seCount;
	}

	public void setSeCount(int seCount) {
		this.seCount = seCount;
	}

	public int getCsCount() {
		return csCount;
	}

	public void setCsCount(int csCount) {
		this.csCount = csCount;
	}

	public int getOlaCount() {
		return olaCount;
	}

	public void setOlaCount(int olaCount) {
		this.olaCount = olaCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public int getHeartBeatCount() {
		return heartBeatCount;
	}

	public void setHeartBeatCount(int heartBeatCount) {
		this.heartBeatCount = heartBeatCount;
	}

	public int getInactiveRetCount() {
		return inactiveRetCount;
	}

	public void setInactiveRetCount(int inactiveRetCount) {
		this.inactiveRetCount = inactiveRetCount;
	}

	public int getTerRetCount() {
		return terRetCount;
	}

	public void setTerRetCount(int terRetCount) {
		this.terRetCount = terRetCount;
	}

	public double getDgTotalSale() {
		return dgTotalSale;
	}

	public void setDgTotalSale(double dgTotalSale) {
		this.dgTotalSale = dgTotalSale;
	}

	public double getDgTotalPwt() {
		return dgTotalPwt;
	}

	public void setDgTotalPwt(double dgTotalPwt) {
		this.dgTotalPwt = dgTotalPwt;
	}

	public double getSeTotalSale() {
		return seTotalSale;
	}

	public void setSeTotalSale(double seTotalSale) {
		this.seTotalSale = seTotalSale;
	}

	public double getSeTotalPwt() {
		return seTotalPwt;
	}

	public void setSeTotalPwt(double seTotalPwt) {
		this.seTotalPwt = seTotalPwt;
	}

	public double getOlaTotalDepo() {
		return olaTotalDepo;
	}

	public void setOlaTotalDepo(double olaTotalDepo) {
		this.olaTotalDepo = olaTotalDepo;
	}

	public double getOlaTotalWdrl() {
		return olaTotalWdrl;
	}

	public void setOlaTotalWdrl(double olaTotalWdrl) {
		this.olaTotalWdrl = olaTotalWdrl;
	}

	public double getCsTotalSale() {
		return csTotalSale;
	}

	public void setCsTotalSale(double csTotalSale) {
		this.csTotalSale = csTotalSale;
	}

	public int getDgTicketCount() {
		return dgTicketCount;
	}

	public void setDgTicketCount(int dgTicketCount) {
		this.dgTicketCount = dgTicketCount;
	}

	public int getDgPwtTotalCount() {
		return dgPwtTotalCount;
	}

	public void setDgPwtTotalCount(int dgPwtTotalCount) {
		this.dgPwtTotalCount = dgPwtTotalCount;
	}

	public double getDgAvgSalePerRet() {
		return dgAvgSalePerRet;
	}

	public void setDgAvgSalePerRet(double dgAvgSalePerRet) {
		this.dgAvgSalePerRet = dgAvgSalePerRet;
	}

	public int getIwCount() {
		return iwCount;
	}

	public void setIwCount(int iwCount) {
		this.iwCount = iwCount;
	}

	public int getIwSaleCount() {
		return iwSaleCount;
	}

	public void setIwSaleCount(int iwSaleCount) {
		this.iwSaleCount = iwSaleCount;
	}

	public int getIwPwtCount() {
		return iwPwtCount;
	}

	public void setIwPwtCount(int iwPwtCount) {
		this.iwPwtCount = iwPwtCount;
	}

	public double getIwTotalSale() {
		return iwTotalSale;
	}

	public void setIwTotalSale(double iwTotalSale) {
		this.iwTotalSale = iwTotalSale;
	}

	public double getIwTotalPwt() {
		return iwTotalPwt;
	}

	public void setIwTotalPwt(double iwTotalPwt) {
		this.iwTotalPwt = iwTotalPwt;
	}

	public int getIwTicketCount() {
		return iwTicketCount;
	}

	public void setIwTicketCount(int iwTicketCount) {
		this.iwTicketCount = iwTicketCount;
	}

	public int getIwPwtTotalCount() {
		return iwPwtTotalCount;
	}

	public void setIwPwtTotalCount(int iwPwtTotalCount) {
		this.iwPwtTotalCount = iwPwtTotalCount;
	}

	public double getIwAvgSalePerRet() {
		return iwAvgSalePerRet;
	}

	public void setIwAvgSalePerRet(double iwAvgSalePerRet) {
		this.iwAvgSalePerRet = iwAvgSalePerRet;
	}

	public int getVsCount() {
		return vsCount;
	}

	public void setVsCount(int vsCount) {
		this.vsCount = vsCount;
	}

	public int getVsSaleCount() {
		return vsSaleCount;
	}

	public void setVsSaleCount(int vsSaleCount) {
		this.vsSaleCount = vsSaleCount;
	}

	public int getVsPwtCount() {
		return vsPwtCount;
	}

	public void setVsPwtCount(int vsPwtCount) {
		this.vsPwtCount = vsPwtCount;
	}

	public double getVsTotalSale() {
		return vsTotalSale;
	}

	public void setVsTotalSale(double vsTotalSale) {
		this.vsTotalSale = vsTotalSale;
	}

	public double getVsTotalPwt() {
		return vsTotalPwt;
	}

	public void setVsTotalPwt(double vsTotalPwt) {
		this.vsTotalPwt = vsTotalPwt;
	}

	public int getVsTicketCount() {
		return vsTicketCount;
	}

	public void setVsTicketCount(int vsTicketCount) {
		this.vsTicketCount = vsTicketCount;
	}

	public int getVsPwtTotalCount() {
		return vsPwtTotalCount;
	}

	public void setVsPwtTotalCount(int vsPwtTotalCount) {
		this.vsPwtTotalCount = vsPwtTotalCount;
	}

	public double getVsAvgSalePerRet() {
		return vsAvgSalePerRet;
	}

	public void setVsAvgSalePerRet(double vsAvgSalePerRet) {
		this.vsAvgSalePerRet = vsAvgSalePerRet;
	}

}
