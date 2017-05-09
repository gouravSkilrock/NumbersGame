/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Date;

public class PromoGameBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int schemeId;
	private int promoGameNo;
	private String promoGametype;
	private String promoTicketType;
	private int noOfPromoTickets;
	private int noOfDraws;
	private String promoGameName;
	private String mainGameName;
	private String promoGameDisplayName;
	private Date  startTime;
	private Date  endTime;
	private double saleTicketAmt;
	private String saleStartTime;
	private String saleEndTime;
	public int getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}

	public int getPromoGameNo() {
		return promoGameNo;
	}

	public void setPromoGameNo(int promoGameNo) {
		this.promoGameNo = promoGameNo;
	}

	public String getPromoGametype() {
		return promoGametype;
	}

	public void setPromoGametype(String promoGametype) {
		this.promoGametype = promoGametype;
	}

	public String getPromoTicketType() {
		return promoTicketType;
	}

	public void setPromoTicketType(String promoTicketType) {
		this.promoTicketType = promoTicketType;
	}

	public int getNoOfPromoTickets() {
		return noOfPromoTickets;
	}

	public void setNoOfPromoTickets(int noOfPromoTickets) {
		this.noOfPromoTickets = noOfPromoTickets;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getPromoGameName() {
		return promoGameName;
	}

	public void setPromoGameName(String promoGameName) {
		this.promoGameName = promoGameName;
	}

	public String getMainGameName() {
		return mainGameName;
	}

	public void setMainGameName(String mainGameName) {
		this.mainGameName = mainGameName;
	}

	public String getPromoGameDisplayName() {
		return promoGameDisplayName;
	}

	public void setPromoGameDisplayName(String promoGameDisplayName) {
		this.promoGameDisplayName = promoGameDisplayName;
	}

	@Override
	public String toString() {
		return "PromoGameBean [schemeId=" + schemeId + ", promoGameNo="
				+ promoGameNo + ", promoGametype=" + promoGametype
				+ ", promoTicketType=" + promoTicketType
				+ ", noOfPromoTickets=" + noOfPromoTickets + ", noOfDraws="
				+ noOfDraws + ", promoGameName=" + promoGameName
				+ ", mainGameName=" + mainGameName + ", promoGameDisplayName="
				+ promoGameDisplayName + "]";
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getSaleTicketAmt() {
		return saleTicketAmt;
	}

	public void setSaleTicketAmt(double saleTicketAmt) {
		this.saleTicketAmt = saleTicketAmt;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}


}
