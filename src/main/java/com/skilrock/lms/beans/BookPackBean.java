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

public class BookPackBean {
	private String book_nbr = null;
	private int game_id = 0;
	private int game_nbr = 0;
	private String pack_nbr = null;

	public String getBook_nbr() {
		return book_nbr;
	}

	public int getGame_id() {
		return game_id;
	}

	public int getGame_nbr() {
		return game_nbr;
	}

	public String getPack_nbr() {
		return pack_nbr;
	}

	public void setBook_nbr(String book_nbr) {
		this.book_nbr = book_nbr;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGame_nbr(int game_nbr) {
		this.game_nbr = game_nbr;
	}

	public void setPack_nbr(String pack_nbr) {
		this.pack_nbr = pack_nbr;
	}

}
