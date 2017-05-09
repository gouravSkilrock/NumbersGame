package com.skilrock.lms.beans;

import java.io.Serializable;

public class GameDetailsManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double agentPwtCommRate;
	private double agentSaleCommRate;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String gameStatus;
	private double govCommRate;
	private int nbrOfBooksPerPack;

	private int nbrOfPackInGame;
	private long nbrOfTicketCancel;
	private long nbrOfTicketUploaded;
	private int nbrTicketsPerBook;
	private double prizeFund;

	private double prizePayOutRatioOfCancel;
	private double prizePayOutRatioOfScheme;
	private double prizePayOutRatioOfSold;
	private double prizePayOutRatioOfSoldAtoR;
	private double prizePayOutRatioOfSoldAtR;
	private double prizePayOutRatioOfUploaded;
	private String pwtEndDate;

	private double retPwtCommRate;
	private double retSaleCommRate;
	private String saleEndDate;
	private String startDate;
	private double ticketPrice;

	private long totalNbrOfTicketInGame;
	private long totalNbrOfTicketSold;

	private long totalNbrOfTicketSoldAtoR;
	private long totalNbrOfTicketSoldAtR;
	private double totalPrizeFundOfCancelVirn;
	private double totalPrizeFundOfScheme;
	private double totalPrizeFundOfSold;

	private double totalPrizeFundOfSoldAtoR;
	private double totalPrizeFundOfSoldAtR;
	private Double totalSaleOfScheme;
	private Double totalSaleOfSold;
	private Double totalSaleOfSoldAtoR;

	private Double totalSaleOfSoldAtR;
	private Double totalSales;
	private Double totalSaleValueOfCancel;
	private double vat;

	public double getAgentPwtCommRate() {
		return agentPwtCommRate;
	}

	public double getAgentSaleCommRate() {
		return agentSaleCommRate;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public double getGovCommRate() {
		return govCommRate;
	}

	public int getNbrOfBooksPerPack() {
		return nbrOfBooksPerPack;
	}

	public int getNbrOfPackInGame() {
		return nbrOfPackInGame;
	}

	public long getNbrOfTicketCancel() {
		return nbrOfTicketCancel;
	}

	public long getNbrOfTicketUploaded() {
		return nbrOfTicketUploaded;
	}

	public int getNbrTicketsPerBook() {
		return nbrTicketsPerBook;
	}

	public double getPrizeFund() {
		return prizeFund;
	}

	public double getPrizePayOutRatioOfCancel() {
		return prizePayOutRatioOfCancel;
	}

	public double getPrizePayOutRatioOfScheme() {
		return prizePayOutRatioOfScheme;
	}

	public double getPrizePayOutRatioOfSold() {
		return prizePayOutRatioOfSold;
	}

	public double getPrizePayOutRatioOfSoldAtoR() {
		return prizePayOutRatioOfSoldAtoR;
	}

	public double getPrizePayOutRatioOfSoldAtR() {
		return prizePayOutRatioOfSoldAtR;
	}

	public double getPrizePayOutRatioOfUploaded() {
		return prizePayOutRatioOfUploaded;
	}

	public String getPwtEndDate() {
		return pwtEndDate;
	}

	public double getRetPwtCommRate() {
		return retPwtCommRate;
	}

	public double getRetSaleCommRate() {
		return retSaleCommRate;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public long getTotalNbrOfTicketInGame() {
		return totalNbrOfTicketInGame;
	}

	public long getTotalNbrOfTicketSold() {
		return totalNbrOfTicketSold;
	}

	public long getTotalNbrOfTicketSoldAtoR() {
		return totalNbrOfTicketSoldAtoR;
	}

	public long getTotalNbrOfTicketSoldAtR() {
		return totalNbrOfTicketSoldAtR;
	}

	public double getTotalPrizeFundOfCancelVirn() {
		return totalPrizeFundOfCancelVirn;
	}

	public double getTotalPrizeFundOfScheme() {
		return totalPrizeFundOfScheme;
	}

	public double getTotalPrizeFundOfSold() {
		return totalPrizeFundOfSold;
	}

	public double getTotalPrizeFundOfSoldAtoR() {
		return totalPrizeFundOfSoldAtoR;
	}

	public double getTotalPrizeFundOfSoldAtR() {
		return totalPrizeFundOfSoldAtR;
	}

	public Double getTotalSaleOfScheme() {
		return totalSaleOfScheme;
	}

	public Double getTotalSaleOfSold() {
		return totalSaleOfSold;
	}

	public Double getTotalSaleOfSoldAtoR() {
		return totalSaleOfSoldAtoR;
	}

	public Double getTotalSaleOfSoldAtR() {
		return totalSaleOfSoldAtR;
	}

	public Double getTotalSales() {
		return totalSales;
	}

	public Double getTotalSaleValueOfCancel() {
		return totalSaleValueOfCancel;
	}

	public double getVat() {
		return vat;
	}

	public void setAgentPwtCommRate(double agentPwtCommRate) {
		this.agentPwtCommRate = agentPwtCommRate;
	}

	public void setAgentSaleCommRate(double agentSaleCommRate) {
		this.agentSaleCommRate = agentSaleCommRate;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setGovCommRate(double govCommRate) {
		this.govCommRate = govCommRate;
	}

	public void setNbrOfBooksPerPack(int nbrOfBooksPerPack) {
		this.nbrOfBooksPerPack = nbrOfBooksPerPack;
	}

	public void setNbrOfPackInGame(int nbrOfPackInGame) {
		this.nbrOfPackInGame = nbrOfPackInGame;
	}

	public void setNbrOfTicketCancel(long nbrOfTicketCancel) {
		this.nbrOfTicketCancel = nbrOfTicketCancel;
	}

	public void setNbrOfTicketUploaded(long nbrOfTicketUploaded) {
		this.nbrOfTicketUploaded = nbrOfTicketUploaded;
	}

	public void setNbrTicketsPerBook(int nbrTicketsPerBook) {
		this.nbrTicketsPerBook = nbrTicketsPerBook;
	}

	public void setPrizeFund(double prizeFund) {
		this.prizeFund = prizeFund;
	}

	public void setPrizePayOutRatioOfCancel(double prizePayOutRatioOfCancel) {
		this.prizePayOutRatioOfCancel = prizePayOutRatioOfCancel;
	}

	public void setPrizePayOutRatioOfScheme(double prizePayOutRatioOfScheme) {
		this.prizePayOutRatioOfScheme = prizePayOutRatioOfScheme;
	}

	public void setPrizePayOutRatioOfSold(double prizePayOutRatioOfSold) {
		this.prizePayOutRatioOfSold = prizePayOutRatioOfSold;
	}

	public void setPrizePayOutRatioOfSoldAtoR(double prizePayOutRatioOfSoldAtoR) {
		this.prizePayOutRatioOfSoldAtoR = prizePayOutRatioOfSoldAtoR;
	}

	public void setPrizePayOutRatioOfSoldAtR(double prizePayOutRatioOfSoldAtR) {
		this.prizePayOutRatioOfSoldAtR = prizePayOutRatioOfSoldAtR;
	}

	public void setPrizePayOutRatioOfUploaded(double prizePayOutRatioOfUploaded) {
		this.prizePayOutRatioOfUploaded = prizePayOutRatioOfUploaded;
	}

	public void setPwtEndDate(String pwtEndDate) {
		this.pwtEndDate = pwtEndDate;
	}

	public void setRetPwtCommRate(double retPwtCommRate) {
		this.retPwtCommRate = retPwtCommRate;
	}

	public void setRetSaleCommRate(double retSaleCommRate) {
		this.retSaleCommRate = retSaleCommRate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public void setTotalNbrOfTicketInGame(long totalNbrOfTicketInGame) {
		this.totalNbrOfTicketInGame = totalNbrOfTicketInGame;
	}

	public void setTotalNbrOfTicketSold(long totalNbrOfTicketSold) {
		this.totalNbrOfTicketSold = totalNbrOfTicketSold;
	}

	public void setTotalNbrOfTicketSoldAtoR(long totalNbrOfTicketSoldAtoR) {
		this.totalNbrOfTicketSoldAtoR = totalNbrOfTicketSoldAtoR;
	}

	public void setTotalNbrOfTicketSoldAtR(long totalNbrOfTicketSoldAtR) {
		this.totalNbrOfTicketSoldAtR = totalNbrOfTicketSoldAtR;
	}

	public void setTotalPrizeFundOfCancelVirn(double totalPrizeFundOfCancelVirn) {
		this.totalPrizeFundOfCancelVirn = totalPrizeFundOfCancelVirn;
	}

	public void setTotalPrizeFundOfScheme(double totalPrizeFundOfScheme) {
		this.totalPrizeFundOfScheme = totalPrizeFundOfScheme;
	}

	public void setTotalPrizeFundOfSold(double totalPrizeFundOfSold) {
		this.totalPrizeFundOfSold = totalPrizeFundOfSold;
	}

	public void setTotalPrizeFundOfSoldAtoR(double totalPrizeFundOfSoldAtoR) {
		this.totalPrizeFundOfSoldAtoR = totalPrizeFundOfSoldAtoR;
	}

	public void setTotalPrizeFundOfSoldAtR(double totalPrizeFundOfSoldAtR) {
		this.totalPrizeFundOfSoldAtR = totalPrizeFundOfSoldAtR;
	}

	public void setTotalSaleOfScheme(Double totalSaleOfScheme) {
		this.totalSaleOfScheme = totalSaleOfScheme;
	}

	public void setTotalSaleOfSold(Double totalSaleOfSold) {
		this.totalSaleOfSold = totalSaleOfSold;
	}

	public void setTotalSaleOfSoldAtoR(Double totalSaleOfSoldAtoR) {
		this.totalSaleOfSoldAtoR = totalSaleOfSoldAtoR;
	}

	public void setTotalSaleOfSoldAtR(Double totalSaleOfSoldAtR) {
		this.totalSaleOfSoldAtR = totalSaleOfSoldAtR;
	}

	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}

	public void setTotalSaleValueOfCancel(Double totalSaleValueOfCancel) {
		this.totalSaleValueOfCancel = totalSaleValueOfCancel;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

}
