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

public class RankDetailBean {
	private static int gameId;
	private long noOfPrize;
	private double prize_amount;
	private int rank;
	private String status;

	public int getGameId() {
		return gameId;
	}

	public long getNoOfPrize() {
		return noOfPrize;
	}

	public double getPrize_amount() {
		return prize_amount;
	}

	public int getRank() {
		return rank;
	}

	public String getStatus() {
		return status;
	}

	public void setGameId(int gameId) {
		RankDetailBean.gameId = gameId;
	}

	public void setNoOfPrize(long noOfPrize) {
		this.noOfPrize = noOfPrize;
	}

	public void setPrize_amount(double prize_amount) {
		this.prize_amount = prize_amount;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
