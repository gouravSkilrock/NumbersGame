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

public class DrawPWTBean implements Serializable {
	// //

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawId;
	private int gameId;
	private int gameNbr;
	private String inUnclmed = "NONE";
	private boolean isAppReq;
	private boolean isAutoScrap;
	private boolean isHighLevel;
	private boolean isRetPayLimit;
	private boolean isRetVerificationLinit;
	private boolean isValid;
	private String message;
	private String messageCode;
	private String pwtAmount;
	private Long ticketNbr;
	private String verificationStatus;

	public boolean getAppReq() {
		return isAppReq;
	}

	public int getDrawId() {
		return drawId;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNbr() {
		return gameNbr;
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

	public Long getTicketNbr() {
		return ticketNbr;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public boolean isAppReq() {
		return isAppReq;
	}

	public boolean isAutoScrap() {
		return isAutoScrap;
	}

	public boolean isRetPayLimit() {
		return isRetPayLimit;
	}

	public boolean isRetVerificationLinit() {
		return isRetVerificationLinit;
	}

	public void setAppReq(boolean isAppReq) {
		this.isAppReq = isAppReq;
	}

	public void setAutoScrap(boolean isAutoScrap) {
		this.isAutoScrap = isAutoScrap;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
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

	public void setRetPayLimit(boolean isRetPayLimit) {
		this.isRetPayLimit = isRetPayLimit;
	}

	public void setRetVerificationLinit(boolean isRetVerificationLinit) {
		this.isRetVerificationLinit = isRetVerificationLinit;
	}

	public void setTicketNbr(Long ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

}
