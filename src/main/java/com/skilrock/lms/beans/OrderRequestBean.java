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
import java.text.DecimalFormat;

/**
 * This bean is used to set and to get details of the Order created bty the
 * Agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class OrderRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String roundTo2DecimalPlaces(double value) {

		DecimalFormat df = new DecimalFormat("0.000");
		String doublevalue = df.format(value);

		System.out.println("------kfkdjd" + doublevalue + "--------");
		return doublevalue;
	}

	private String address;
	private int allowedBooks;
	private double availableCredit;
	// private double ticketPrice;
	// private int ticketsPerBook;
	private double bookPrice;
	private String city;
	private String country;
	private double criditLimit;
	private double currentBalance;
	private java.sql.Date date;
	private int differenceBtAgentandApprBooks; // for retailer
	private int differenceBtBOndApprBooks;
	private int gameId;
	private String gameName;
	private int gameNumber;
	private String name = null;
	private int nbrAppBooks;
	private int nbrOfBooksAtAgent; // for retailer request
	private int nbrOfBooksAtBO; // for agent request
	private int nbrOfBooksReq;
	private int nbrOfBooksDlvrd;
	private int orderId;

	private long pinCode;
	private String state;
	private String strCreditAmount;

	private String strCreditLimit;

	public String getAddress() {
		return address;
	}

	public int getNbrOfBooksDlvrd() {
		return nbrOfBooksDlvrd;
	}

	public void setNbrOfBooksDlvrd(int nbrOfBooksDlvrd) {
		this.nbrOfBooksDlvrd = nbrOfBooksDlvrd;
	}

	public int getAllowedBooks() {
		return allowedBooks;
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public double getBookPrice() {
		return bookPrice;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public double getCriditLimit() {
		return criditLimit;

	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public int getDifferenceBtAgentandApprBooks() {
		return differenceBtAgentandApprBooks;
	}

	public int getDifferenceBtBOndApprBooks() {
		return differenceBtBOndApprBooks;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public String getName() {
		return name;
	}

	public int getNbrAppBooks() {
		return nbrAppBooks;
	}

	public int getNbrOfBooksAtAgent() {
		return nbrOfBooksAtAgent;
	}

	public int getNbrOfBooksAtBO() {
		return nbrOfBooksAtBO;
	}

	public int getNbrOfBooksReq() {
		return nbrOfBooksReq;
	}

	public int getOrderId() {
		return orderId;
	}

	public long getPinCode() {
		return pinCode;
	}

	public String getState() {
		return state;
	}

	public String getStrCreditAmount() {
		return strCreditAmount;
	}

	public String getStrCreditLimit() {
		return strCreditLimit;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAllowedBooks(int allowedBooks) {
		this.allowedBooks = allowedBooks;
	}

	public void setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCriditLimit(double criditLimit) {
		this.criditLimit = criditLimit;
		setStrCreditLimit(roundTo2DecimalPlaces(criditLimit));
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
		setStrCreditAmount(roundTo2DecimalPlaces(currentBalance));
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public void setDifferenceBtAgentandApprBooks(
			int differenceBtAgentandApprBooks) {
		this.differenceBtAgentandApprBooks = differenceBtAgentandApprBooks;
	}

	public void setDifferenceBtBOndApprBooks(int differenceBtBOndApprBooks) {
		this.differenceBtBOndApprBooks = differenceBtBOndApprBooks;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNbrAppBooks(int nbrAppBooks) {
		this.nbrAppBooks = nbrAppBooks;
	}

	public void setNbrOfBooksAtAgent(int nbrOfBooksAtAgent) {
		this.nbrOfBooksAtAgent = nbrOfBooksAtAgent;
	}

	public void setNbrOfBooksAtBO(int nbrOfBooksAtBO) {
		this.nbrOfBooksAtBO = nbrOfBooksAtBO;
	}

	public void setNbrOfBooksReq(int nbrOfBooksReq) {
		this.nbrOfBooksReq = nbrOfBooksReq;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setPinCode(long pinCode) {
		this.pinCode = pinCode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStrCreditAmount(String strCreditAmount) {
		this.strCreditAmount = strCreditAmount;
	}

	public void setStrCreditLimit(String strCreditLimit) {
		this.strCreditLimit = strCreditLimit;
	}

}
