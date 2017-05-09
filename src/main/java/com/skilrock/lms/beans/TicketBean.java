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

public class TicketBean implements Serializable {

	private String ticketNumber = null;
	private int ticketGameId;
	private int gameNbr;
	private boolean isValid;
	private String status = null;
	private String validity = null;
	private String messageCode;
	private String book_nbr;
	private String bookStatus;
	private String updateTicketType;

	public String getBook_nbr() {
		return book_nbr;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getStatus() {
		return status;
	}

	public int getTicketGameId() {
		return ticketGameId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public String getUpdateTicketType() {
		return updateTicketType;
	}

	public String getValidity() {
		return validity;
	}

	public void setBook_nbr(String book_nbr) {
		this.book_nbr = book_nbr;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketGameId(int ticketGameId) {
		this.ticketGameId = ticketGameId;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setUpdateTicketType(String updateTicketType) {
		this.updateTicketType = updateTicketType;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

}
