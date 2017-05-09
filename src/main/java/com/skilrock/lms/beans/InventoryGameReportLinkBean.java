package com.skilrock.lms.beans;

public class InventoryGameReportLinkBean {

	private String gamename;
	private int gamenbr;
	private int purchaseFromBo;
	private int returnToAgent;
	private int returnToBo;
	private String saleenddate;
	private int soldByAgent;

	public String getGamename() {
		return gamename;
	}

	public int getGamenbr() {
		return gamenbr;
	}

	public int getPurchaseFromBo() {
		return purchaseFromBo;
	}

	public int getReturnToAgent() {
		return returnToAgent;
	}

	public int getReturnToBo() {
		return returnToBo;
	}

	public String getSaleenddate() {
		return saleenddate;
	}

	public int getSoldByAgent() {
		return soldByAgent;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void setGamenbr(int gamenbr) {
		this.gamenbr = gamenbr;
	}

	public void setPurchaseFromBo(int purchaseFromBo) {
		this.purchaseFromBo = purchaseFromBo;
	}

	public void setReturnToAgent(int returnToAgent) {
		this.returnToAgent = returnToAgent;
	}

	public void setReturnToBo(int returnToBo) {
		this.returnToBo = returnToBo;
	}

	public void setSaleenddate(String saleenddate) {
		this.saleenddate = saleenddate;
	}

	public void setSoldByAgent(int soldByAgent) {
		this.soldByAgent = soldByAgent;
	}

}
