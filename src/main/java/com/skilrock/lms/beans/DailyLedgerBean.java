package com.skilrock.lms.beans;

public class DailyLedgerBean {
	private String cashCol;
	private String clrBal;
	private String date;
	private String netPayment;
	private String netPwt;
	private String netsale;
	private String netSaleRefund;
	private String openBal;
	private String profit;
	private String purchase;
	private String scratchSale;
	private String netSaleCS;
	private String balance;
	private String clXclAmount;
	private String olaDeposit;
	private String olaWithdrawal;

	private String sleSale;
	private String slePwt;

	private String iwSale;
	private String iwPwt;

	private String vsSale;
	private String vsPwt;

	public String getIwSale() {
		return iwSale;
	}

	public void setIwSale(String iwSale) {
		this.iwSale = iwSale;
	}

	public String getIwPwt() {
		return iwPwt;
	}

	public void setIwPwt(String iwPwt) {
		this.iwPwt = iwPwt;
	}

	public String getCashCol() {
		return cashCol;
	}

	public String getClrBal() {
		return clrBal;
	}

	public String getDate() {
		return date;
	}

	public String getNetPayment() {
		return netPayment;
	}

	public String getNetPwt() {
		return netPwt;
	}

	public String getNetsale() {
		return netsale;
	}

	public String getNetSaleRefund() {
		return netSaleRefund;
	}

	public String getOpenBal() {
		return openBal;
	}

	public String getProfit() {
		return profit;
	}

	public String getPurchase() {
		return purchase;
	}

	public String getScratchSale() {
		return scratchSale;
	}

	public void setCashCol(String cashCol) {
		this.cashCol = cashCol;
	}

	public void setClrBal(String clrBal) {
		this.clrBal = clrBal;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setNetPayment(String netPayment) {
		this.netPayment = netPayment;
	}

	public void setNetPwt(String netPwt) {
		this.netPwt = netPwt;
	}

	public void setNetsale(String netsale) {
		this.netsale = netsale;
	}

	public void setNetSaleRefund(String netSaleRefund) {
		this.netSaleRefund = netSaleRefund;
	}

	public void setOpenBal(String openBal) {
		this.openBal = openBal;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}

	public void setScratchSale(String scratchSale) {
		this.scratchSale = scratchSale;
	}

	public String getNetSaleCS() {
		return netSaleCS;
	}

	public void setNetSaleCS(String netSaleCS) {
		this.netSaleCS = netSaleCS;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getClXclAmount() {
		return clXclAmount;
	}

	public void setClXclAmount(String clXclAmount) {
		this.clXclAmount = clXclAmount;
	}

	public void setOlaDeposit(String olaDeposit) {
		this.olaDeposit = olaDeposit;
	}

	public String getOlaDeposit() {
		return olaDeposit;
	}

	public void setOlaWithdrawal(String olaWithdrawal) {
		this.olaWithdrawal = olaWithdrawal;
	}

	public String getOlaWithdrawal() {
		return olaWithdrawal;
	}

	public String getSleSale() {
		return sleSale;
	}

	public void setSleSale(String sleSale) {
		this.sleSale = sleSale;
	}

	public String getSlePwt() {
		return slePwt;
	}

	public void setSlePwt(String slePwt) {
		this.slePwt = slePwt;
	}

	public String getVsSale() {
		return vsSale;
	}

	public void setVsSale(String vsSale) {
		this.vsSale = vsSale;
	}

	public String getVsPwt() {
		return vsPwt;
	}

	public void setVsPwt(String vsPwt) {
		this.vsPwt = vsPwt;
	}

	@Override
	public String toString() {
		return "DailyLedgerBean [cashCol=" + cashCol + ", clrBal=" + clrBal
				+ ", date=" + date + ", netPayment=" + netPayment + ", netPwt="
				+ netPwt + ", netsale=" + netsale + ", netSaleRefund="
				+ netSaleRefund + ", openBal=" + openBal + ", profit=" + profit
				+ ", purchase=" + purchase + ", scratchSale=" + scratchSale
				+ ", netSaleCS=" + netSaleCS + ", balance=" + balance
				+ ", clXclAmount=" + clXclAmount + ", olaDeposit=" + olaDeposit
				+ ", olaWithdrawal=" + olaWithdrawal + ", sleSale=" + sleSale
				+ ", slePwt=" + slePwt + ", iwSale=" + iwSale + ", iwPwt="
				+ iwPwt + ", vsSale=" + vsSale + ", vsPwt=" + vsPwt + "]";
	}

}