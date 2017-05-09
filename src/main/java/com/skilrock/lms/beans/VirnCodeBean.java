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

public class VirnCodeBean {
	private int game_id;
	private String id1;
	private String id2;
	private String prize_level;
	private double pwt_amt;
	private String status;
	private String virn_code;

	public int getGame_id() {
		return game_id;
	}

	public String getId1() {
		return id1;
	}

	public String getId2() {
		return id2;
	}

	public String getPrize_level() {
		return prize_level;
	}

	public double getPwt_amt() {
		return pwt_amt;
	}

	public String getStatus() {
		return status;
	}

	public String getVirn_code() {
		return virn_code;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public void setPrize_level(String prize_level) {
		this.prize_level = prize_level;
	}

	public void setPwt_amt(double pwt_amt) {
		this.pwt_amt = pwt_amt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setVirn_code(String virn_code) {
		this.virn_code = virn_code;
	}

}
