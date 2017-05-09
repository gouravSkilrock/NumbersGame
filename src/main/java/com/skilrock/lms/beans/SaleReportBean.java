package com.skilrock.lms.beans;

public class SaleReportBean implements Cloneable {

	private String agentName;
	private int agentorgid;
	private int agentsRemBooks;

	private String bookCost;

	private int boRemBooks;
	private int existingBooks;

	private int gameid;
	private String gamename;
	private String name;
	private String netAmt;

	private String netMrpAmt;
	private int retailerOrgId;
	private String retName;

	private String returnAmt;
	private int sale;
	private String saleAmt;
	private String saleMrp;

	private int salereturn;
	private int saleReturnByRetailer;
	private String saleReturnMrp;

	private int saleToRetailer;
	private double ticketCost;
	private int ticketsPerBook;
	private int totalsale;

	private int looseSale;
	private int looseSaleReturn;
	private int looseSaleToRetailer;
	private int looseSaleReturnByRetailer;
	
	@Override
	public SaleReportBean clone() {
		try {
			return (SaleReportBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAgentName() {
		return agentName;
	}

	public int getAgentorgid() {
		return agentorgid;
	}

	/**
	 * @return the agentsRemBooks
	 */
	public int getAgentsRemBooks() {
		return agentsRemBooks;
	}

	public String getBookCost() {
		return bookCost;
	}

	/**
	 * @return the boRemBooks
	 */
	public int getBoRemBooks() {
		return boRemBooks;
	}

	/**
	 * @return the existingBooks
	 */
	public int getExistingBooks() {
		return existingBooks;
	}

	public int getGameid() {
		return gameid;
	}

	public String getGamename() {
		return gamename;
	}

	public String getName() {
		return name;
	}

	public String getNetAmt() {
		return netAmt;
	}

	public String getNetMrpAmt() {
		return netMrpAmt;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public String getRetName() {
		return retName;
	}

	public String getReturnAmt() {
		return returnAmt;
	}

	public int getSale() {
		return sale;
	}

	public String getSaleAmt() {
		return saleAmt;
	}

	public String getSaleMrp() {
		return saleMrp;
	}

	public int getSalereturn() {
		return salereturn;
	}

	/**
	 * @return the saleReturnByRetailer
	 */
	public int getSaleReturnByRetailer() {
		return saleReturnByRetailer;
	}

	public String getSaleReturnMrp() {
		return saleReturnMrp;
	}

	/**
	 * @return the saleToRetailer
	 */
	public int getSaleToRetailer() {
		return saleToRetailer;
	}

	public double getTicketCost() {
		return ticketCost;
	}

	public int getTicketsPerBook() {
		return ticketsPerBook;
	}

	public int getTotalsale() {
		return totalsale;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setAgentorgid(int agentorgid) {
		this.agentorgid = agentorgid;
	}

	/**
	 * @param agentsRemBooks
	 *            the agentsRemBooks to set
	 */
	public void setAgentsRemBooks(int agentsRemBooks) {
		this.agentsRemBooks = agentsRemBooks;
	}

	public void setBookCost(String bookCost) {
		this.bookCost = bookCost;
	}

	/**
	 * @param boRemBooks
	 *            the boRemBooks to set
	 */
	public void setBoRemBooks(int boRemBooks) {
		this.boRemBooks = boRemBooks;
	}

	/**
	 * @param existingBooks
	 *            the existingBooks to set
	 */
	public void setExistingBooks(int existingBooks) {
		this.existingBooks = existingBooks;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}

	public void setNetMrpAmt(String netMrpAmt) {
		this.netMrpAmt = netMrpAmt;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public void setReturnAmt(String returnAmt) {
		this.returnAmt = returnAmt;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public void setSaleAmt(String saleAmt) {
		this.saleAmt = saleAmt;
	}

	public void setSaleMrp(String saleMrp) {
		this.saleMrp = saleMrp;
	}

	public void setSalereturn(int salereturn) {
		this.salereturn = salereturn;
	}

	/**
	 * @param saleReturnByRetailer
	 *            the saleReturnByRetailer to set
	 */
	public void setSaleReturnByRetailer(int saleReturnByRetailer) {
		this.saleReturnByRetailer = saleReturnByRetailer;
	}

	public void setSaleReturnMrp(String saleReturnMrp) {
		this.saleReturnMrp = saleReturnMrp;
	}

	/**
	 * @param saleToRetailer
	 *            the saleToRetailer to set
	 */
	public void setSaleToRetailer(int saleToRetailer) {
		this.saleToRetailer = saleToRetailer;
	}

	public void setTicketCost(double ticketCost) {
		this.ticketCost = ticketCost;
	}

	public void setTicketsPerBook(int ticketsPerBook) {
		this.ticketsPerBook = ticketsPerBook;
	}

	public void setTotalsale(int totalsale) {
		this.totalsale = totalsale;
	}

	public int getLooseSale() {
		return looseSale;
	}

	public void setLooseSale(int looseSale) {
		this.looseSale = looseSale;
	}

	public int getLooseSaleReturn() {
		return looseSaleReturn;
	}

	public void setLooseSaleReturn(int looseSaleReturn) {
		this.looseSaleReturn = looseSaleReturn;
	}

	public int getLooseSaleToRetailer() {
		return looseSaleToRetailer;
	}

	public void setLooseSaleToRetailer(int looseSaleToRetailer) {
		this.looseSaleToRetailer = looseSaleToRetailer;
	}

	public int getLooseSaleReturnByRetailer() {
		return looseSaleReturnByRetailer;
	}

	public void setLooseSaleReturnByRetailer(int looseSaleReturnByRetailer) {
		this.looseSaleReturnByRetailer = looseSaleReturnByRetailer;
	}

	
}
