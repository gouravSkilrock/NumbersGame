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

public class GameBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double agentSaleRate;
	private int gameId;
	private String gameName;
	private int gameNbr;
	private String gameStatus;
	private int orderedQty;
	private double retSaleRate;
	private java.sql.Date saleEndDate;
	private java.sql.Date startDate;
	private double ticketPrice;
	private int ticketsPerBook;

	public double getAgentSaleRate() {
		return agentSaleRate;
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

	public int getOrderedQty() {
		return orderedQty;
	}

	public double getRetSaleRate() {
		return retSaleRate;
	}

	public java.sql.Date getSaleEndDate() {
		return saleEndDate;
	}

	public java.sql.Date getStartDate() {
		return startDate;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public int getTicketsPerBook() {
		return ticketsPerBook;
	}

	public void setAgentSaleRate(double agentSaleRate) {
		this.agentSaleRate = agentSaleRate;
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

	public void setOrderedQty(int orderedQty) {
		this.orderedQty = orderedQty;
	}

	public void setRetSaleRate(double retSaleRate) {
		this.retSaleRate = retSaleRate;
	}

	public void setSaleEndDate(java.sql.Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public void setTicketsPerBook(int ticketsPerBook) {
		this.ticketsPerBook = ticketsPerBook;
	}

}
