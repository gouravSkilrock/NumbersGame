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
import java.util.Date;

public class ChequeBeanClearance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agtOrgName;// organization Id 
	private double chequeAmount;
	private Date chequeClearanceDate;
	private Date chequeDate;
	private String chequeStatus;
	private String chqNBR;
	private Double commAmt;
	private String draweebank;
	private String isCleared;
	private String issuingPartyName;

	private String taskId;

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public double getChequeAmount() {
		return chequeAmount;
	}

	public Date getChequeClearanceDate() {
		return chequeClearanceDate;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public String getChequeStatus() {
		return chequeStatus;
	}

	public String getChqNBR() {
		return chqNBR;
	}

	public Double getCommAmt() {
		return commAmt;
	}

	public String getDraweebank() {
		return draweebank;
	}

	public String getIsCleared() {
		return isCleared;
	}

	public String getIssuingPartyName() {
		return issuingPartyName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setChequeAmount(double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public void setChequeClearanceDate(Date chequeClearanceDate) {
		this.chequeClearanceDate = chequeClearanceDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public void setChqNBR(String chqNBR) {
		this.chqNBR = chqNBR;
	}

	public void setCommAmt(Double commAmt) {
		this.commAmt = commAmt;
	}

	public void setDraweebank(String draweebank) {
		this.draweebank = draweebank;
	}

	public void setIsCleared(String isCleared) {
		this.isCleared = isCleared;
	}

	public void setIssuingPartyName(String issuingPartyName) {
		this.issuingPartyName = issuingPartyName;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
