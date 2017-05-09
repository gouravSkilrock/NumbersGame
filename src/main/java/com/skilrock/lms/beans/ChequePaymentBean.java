/***
 *  * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Date;
import java.text.DecimalFormat;

/**
 * This class is used to set and get the details of the Payment
 * 
 * @author Skilrock Technlogies
 * 
 */
public class ChequePaymentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String roundTo2DecimalPlaces(double value) {

		DecimalFormat df = new DecimalFormat("0.000");
		String doublevalue = df.format(value);

		System.out.println("------kfkdjd" + doublevalue + "--------");
		return doublevalue;
	}

	private double amount;
	private String bankName;
	private Date chequeDate;
	private String chequeNo;
	private String chqeueStatus;
	private String issuePartyname;
	private String orgName;
	private int sNo;

	private String strAmount;

	public double getAmount() {
		return amount;
	}

	public String getBankName() {
		return bankName;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public String getChqeueStatus() {
		return chqeueStatus;
	}

	public String getIssuePartyname() {
		return issuePartyname;
	}

	public String getOrgName() {
		return orgName;
	}

	public int getSNo() {
		return sNo;
	}

	public String getStrAmount() {
		return strAmount;
	}

	public void setAmount(double amount) {

		this.amount = amount;
		setStrAmount(roundTo2DecimalPlaces(amount));
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public void setChqeueStatus(String chqeueStatus) {
		this.chqeueStatus = chqeueStatus;
	}

	public void setIssuePartyname(String issuePartyname) {
		this.issuePartyname = issuePartyname;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setSNo(int no) {
		sNo = no;
	}

	public void setStrAmount(String strAmount) {
		this.strAmount = strAmount;

	}

}
