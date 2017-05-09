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

public class GameTicketFormatBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bookDigits;
	private int gameId;
	private int gameNbr;
	private int gameNbrDigits;
	private int gameVirnDigits;
	private int maxRankDigits;
	private int packDigits;

	public int getBookDigits() {
		return bookDigits;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public int getGameNbrDigits() {
		return gameNbrDigits;
	}

	public int getGameVirnDigits() {
		return gameVirnDigits;
	}

	public int getMaxRankDigits() {
		return maxRankDigits;
	}

	public int getPackDigits() {
		return packDigits;
	}

	public void setBookDigits(int bookDigits) {
		this.bookDigits = bookDigits;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGameNbrDigits(int gameNbrDigits) {
		this.gameNbrDigits = gameNbrDigits;
	}

	public void setGameVirnDigits(int gameVirnDigits) {
		this.gameVirnDigits = gameVirnDigits;
	}

	public void setMaxRankDigits(int maxRankDigits) {
		this.maxRankDigits = maxRankDigits;
	}

	public void setPackDigits(int packDigits) {
		this.packDigits = packDigits;
	}

}
