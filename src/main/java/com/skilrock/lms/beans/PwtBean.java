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

public class PwtBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bookNumber;
	private String bookStatus;
	private String enctktNumber;
	private String encVirnCode;
	private String inUnclmed = "NONE";
	// added by ARUN
	private boolean isAppReq;
	private boolean isHighLevel;
	private boolean isTicketValid;
	private boolean isValid;
	private String message;
	private String messageCode;

	private String pwtAmount;
	private String ticketMessage;

	private String ticketNumber;
	private String ticketVerificationStatus;
	private String updateTicketType;
	private String verificationStatus;
	private String virnCode;

	public boolean getAppReq() {
		return isAppReq;
	}

	public String getBookNumber() {
		return bookNumber;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public String getEnctktNumber() {
		return enctktNumber;
	}

	public String getEncVirnCode() {
		return encVirnCode;
	}

	public String getInUnclmed() {
		return inUnclmed;
	}

	public boolean getIsHighLevel() {
		return isHighLevel;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getPwtAmount() {
		return pwtAmount;
	}

	public String getTicketMessage() {
		return ticketMessage;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public String getTicketVerificationStatus() {
		return ticketVerificationStatus;
	}

	public String getUpdateTicketType() {
		return updateTicketType;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public String getVirnCode() {
		return virnCode;
	}

	public boolean isTicketValid() {
		return isTicketValid;
	}

	public void setAppReq(boolean isAppReq) {
		this.isAppReq = isAppReq;
	}

	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public void setEnctktNumber(String enctktNumber) {
		this.enctktNumber = enctktNumber;
	}

	public void setEncVirnCode(String encVirnCode) {
		this.encVirnCode = encVirnCode;
	}

	public void setHighLevel(boolean isHighLevel) {
		this.isHighLevel = isHighLevel;
	}

	public void setInUnclmed(String inUnclmed) {
		this.inUnclmed = inUnclmed;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setPwtAmount(String pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	public void setTicketMessage(String ticketMessage) {
		this.ticketMessage = ticketMessage;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTicketValid(boolean isTicketValid) {
		this.isTicketValid = isTicketValid;
	}

	public void setTicketVerificationStatus(String ticketVerificationStatus) {
		this.ticketVerificationStatus = ticketVerificationStatus;
	}

	public void setUpdateTicketType(String updateTicketType) {
		this.updateTicketType = updateTicketType;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

}
