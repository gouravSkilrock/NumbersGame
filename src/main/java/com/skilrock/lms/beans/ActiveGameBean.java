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

public class ActiveGameBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double agentPwtCommRate;
	private int gameId;
	private String gameNbr_Name;
	private double playerPwtCommRate;
	private double retailerPwtCommRate;

	public double getAgentPwtCommRate() {
		return agentPwtCommRate;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameNbr_Name() {
		return gameNbr_Name;
	}

	public double getPlayerPwtCommRate() {
		return playerPwtCommRate;
	}

	public double getRetailerPwtCommRate() {
		return retailerPwtCommRate;
	}

	public void setAgentPwtCommRate(double agentPwtCommRate) {
		this.agentPwtCommRate = agentPwtCommRate;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setPlayerPwtCommRate(double playerPwtCommRate) {
		this.playerPwtCommRate = playerPwtCommRate;
	}

	public void setRetailerPwtCommRate(double retailerPwtCommRate) {
		this.retailerPwtCommRate = retailerPwtCommRate;
	}

}
