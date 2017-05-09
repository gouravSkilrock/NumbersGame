/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.beans;

import java.io.Serializable;

public class OrderedGameBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double agtGameCommVariance;
	private double agtSaleCommRate;
	private int bookDigits;
	private double bookPrice;
	private double fixedAmt;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private int gameNbrDigits;
	private double govtComm;
	private String govtCommRule;
	private boolean isReadyForDispatch;
	private int nbrOfBooksApp;

	private int nbrOfBooksDlvrd;
	private int nbrOfBooksPerPack;
	private int nbrOfBooksToDispatch;

	private int orderId;
	private int packDigits;
	private double prizePayOutRatio;
	private int remainingBooksToDispatch;
	private double retGameCommVariance;
	private double retSaleCommRate;
	private long ticketsInScheme;
	private double vat;
	private double vatBalance;

	public double getAgtGameCommVariance() {
		return agtGameCommVariance;
	}

	public double getAgtSaleCommRate() {
		return agtSaleCommRate;
	}

	public int getBookDigits() {
		return bookDigits;
	}

	public double getBookPrice() {
		return bookPrice;
	}

	public double getFixedAmt() {
		return fixedAmt;
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

	public int getGameNbrDigits() {
		return gameNbrDigits;
	}

	public double getGovtComm() {
		return govtComm;
	}

	public String getGovtCommRule() {
		return govtCommRule;
	}

	public boolean getIsReadyForDispatch() {
		return isReadyForDispatch;
	}

	public int getNbrOfBooksApp() {
		return nbrOfBooksApp;
	}

	public int getNbrOfBooksDlvrd() {
		return nbrOfBooksDlvrd;
	}

	public int getNbrOfBooksPerPack() {
		return nbrOfBooksPerPack;
	}

	public int getNbrOfBooksToDispatch() {
		return nbrOfBooksToDispatch;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getPackDigits() {
		return packDigits;
	}

	public double getPrizePayOutRatio() {
		return prizePayOutRatio;
	}

	public int getRemainingBooksToDispatch() {
		return remainingBooksToDispatch;
	}

	public double getRetGameCommVariance() {
		return retGameCommVariance;
	}

	public double getRetSaleCommRate() {
		return retSaleCommRate;
	}

	public long getTicketsInScheme() {
		return ticketsInScheme;
	}

	public double getVat() {
		return vat;
	}

	public double getVatBalance() {
		return vatBalance;
	}

	public void setAgtGameCommVariance(double agtGameCommVariance) {
		this.agtGameCommVariance = agtGameCommVariance;
	}

	public void setAgtSaleCommRate(double agtSaleCommRate) {
		this.agtSaleCommRate = agtSaleCommRate;
	}

	public void setBookDigits(int bookDigits) {
		this.bookDigits = bookDigits;
	}

	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public void setFixedAmt(double fixedAmt) {
		this.fixedAmt = fixedAmt;
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

	public void setGameNbrDigits(int gameNbrDigits) {
		this.gameNbrDigits = gameNbrDigits;
	}

	public void setGovtComm(double govtComm) {
		this.govtComm = govtComm;
	}

	public void setGovtCommRule(String govtCommRule) {
		this.govtCommRule = govtCommRule;
	}

	public void setNbrOfBooksApp(int nbrOfBooksApp) {
		this.nbrOfBooksApp = nbrOfBooksApp;
	}

	public void setNbrOfBooksDlvrd(int nbrOfBooksDlvrd) {
		this.nbrOfBooksDlvrd = nbrOfBooksDlvrd;
	}

	public void setNbrOfBooksPerPack(int nbrOfBooksPerPack) {
		this.nbrOfBooksPerPack = nbrOfBooksPerPack;
	}

	public void setNbrOfBooksToDispatch(int nbrOfBooksToDispatch) {
		this.nbrOfBooksToDispatch = nbrOfBooksToDispatch;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setPackDigits(int packDigits) {
		this.packDigits = packDigits;
	}

	public void setPrizePayOutRatio(double prizePayOutRatio) {
		this.prizePayOutRatio = prizePayOutRatio;
	}

	public void setReadyForDispatch(boolean isReadyForDispatch) {
		this.isReadyForDispatch = isReadyForDispatch;
	}

	public void setRemainingBooksToDispatch(int remainingBooksToDispatch) {
		this.remainingBooksToDispatch = remainingBooksToDispatch;
	}

	public void setRetGameCommVariance(double retGameCommVariance) {
		this.retGameCommVariance = retGameCommVariance;
	}

	public void setRetSaleCommRate(double retSaleCommRate) {
		this.retSaleCommRate = retSaleCommRate;
	}

	public void setTicketsInScheme(long ticketsInScheme) {
		this.ticketsInScheme = ticketsInScheme;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public void setVatBalance(double vatBalance) {
		this.vatBalance = vatBalance;
	}

}
