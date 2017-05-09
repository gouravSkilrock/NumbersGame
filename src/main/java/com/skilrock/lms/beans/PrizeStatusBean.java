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

import com.skilrock.lms.common.utility.FormatNumber;

public class PrizeStatusBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nbrOfPrizeLeft;
	private double prizeAmt;

	public int getNbrOfPrizeLeft() {
		return nbrOfPrizeLeft;
	}

	public String getPrizeAmt() {
		return FormatNumber.formatNumberForJSP(prizeAmt);
	}

	public void setNbrOfPrizeLeft(int nbrOfPrizeLeft) {
		this.nbrOfPrizeLeft = nbrOfPrizeLeft;
	}

	public void setPrizeAmt(double prizeAmt) {
		this.prizeAmt = prizeAmt;
	}

}
