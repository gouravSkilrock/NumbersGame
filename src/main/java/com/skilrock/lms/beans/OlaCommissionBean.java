package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Date;

public class OlaCommissionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double playerNetGaming;
	private double commissionCalculated;
	private double totalPlayerNetGaming;
	private double totalCommissionCalculated;
	private int boUserId;
	private int boUserOrgId;
	private int id;
	private String playerId;
	private double debitCardDeposit;
	private double creditCardDeposit;
	private double netBankingDeposit;
	private double cashCardDeposit;
	private double olaBindDeposit;
	private double olaNonBindDeposit;
	private double techProcessDeposit;
	private double bonusDeposit;
	private double inhouseDeposit;
	private double wireTransferDeposit;
	private double totalDeposit;
	private double totalPlay;
	private double totalWin;
	private Date depositDate;
	private String retOrgName;
	private double retNetGaming;
	private double retComm;
	private double tdsRetComm;
	private double netRetComm;
	private double agtComm;
	private double tdsagtComm;
	private double netAgtComm;
	private int retOrdId;
	private String agtOrgName;
	private int agtOrdId;
	private double agtNetGaming;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBoUserId() {
		return boUserId;
	}

	public void setBoUserId(int boUserId) {
		this.boUserId = boUserId;
	}

	public int getBoUserOrgId() {
		return boUserOrgId;
	}

	public void setBoUserOrgId(int boUserOrgId) {
		this.boUserOrgId = boUserOrgId;
	}

	public double getPlayerNetGaming() {
		return playerNetGaming;
	}

	public void setPlayerNetGaming(double playerNetGaming) {
		this.playerNetGaming = playerNetGaming;
	}

	public double getCommissionCalculated() {
		return commissionCalculated;
	}

	public void setCommissionCalculated(double commissionCalculated) {
		this.commissionCalculated = commissionCalculated;
	}
	public double getTotalPlayerNetGaming() {
		return totalPlayerNetGaming;
	}

	public void setTotalPlayerNetGaming(double totalPlayerNetGaming) {
		this.totalPlayerNetGaming = totalPlayerNetGaming;
	}

	public double getTotalCommissionCalculated() {
		return totalCommissionCalculated;
	}

	public void setTotalCommissionCalculated(double totalCommissionCalculated) {
		this.totalCommissionCalculated = totalCommissionCalculated;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public double getDebitCardDeposit() {
		return debitCardDeposit;
	}

	public void setDebitCardDeposit(double debitCardDeposit) {
		this.debitCardDeposit = debitCardDeposit;
	}

	public double getCreditCardDeposit() {
		return creditCardDeposit;
	}

	public void setCreditCardDeposit(double creditCardDeposit) {
		this.creditCardDeposit = creditCardDeposit;
	}

	public double getNetBankingDeposit() {
		return netBankingDeposit;
	}

	public void setNetBankingDeposit(double netBankingDeposit) {
		this.netBankingDeposit = netBankingDeposit;
	}

	public double getCashCardDeposit() {
		return cashCardDeposit;
	}

	public void setCashCardDeposit(double cashCardDeposit) {
		this.cashCardDeposit = cashCardDeposit;
	}

	public double getOlaBindDeposit() {
		return olaBindDeposit;
	}

	public void setOlaBindDeposit(double olaBindDeposit) {
		this.olaBindDeposit = olaBindDeposit;
	}

	public double getOlaNonBindDeposit() {
		return olaNonBindDeposit;
	}

	public void setOlaNonBindDeposit(double olaNonBindDeposit) {
		this.olaNonBindDeposit = olaNonBindDeposit;
	}

	public double getTechProcessDeposit() {
		return techProcessDeposit;
	}

	public void setTechProcessDeposit(double techProcessDeposit) {
		this.techProcessDeposit = techProcessDeposit;
	}

	public double getTotalDeposit() {
		return totalDeposit;
	}

	public void setTotalDeposit(double totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	public double getTotalPlay() {
		return totalPlay;
	}

	public void setTotalPlay(double totalPlay) {
		this.totalPlay = totalPlay;
	}

	public double getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public double getRetNetGaming() {
		return retNetGaming;
	}

	public void setRetNetGaming(double retNetGaming) {
		this.retNetGaming = retNetGaming;
	}

	public double getRetComm() {
		return retComm;
	}

	public void setRetComm(double retComm) {
		this.retComm = retComm;
	}

	public double getTdsRetComm() {
		return tdsRetComm;
	}

	public void setTdsRetComm(double tdsRetComm) {
		this.tdsRetComm = tdsRetComm;
	}

	public double getNetRetComm() {
		return netRetComm;
	}

	public void setNetRetComm(double netRetComm) {
		this.netRetComm = netRetComm;
	}

	public double getAgtComm() {
		return agtComm;
	}

	public void setAgtComm(double agtComm) {
		this.agtComm = agtComm;
	}

	public double getTdsagtComm() {
		return tdsagtComm;
	}

	public void setTdsagtComm(double tdsagtComm) {
		this.tdsagtComm = tdsagtComm;
	}

	public double getNetAgtComm() {
		return netAgtComm;
	}

	public void setNetAgtComm(double netAgtComm) {
		this.netAgtComm = netAgtComm;
	}

	public int getRetOrdId() {
		return retOrdId;
	}

	public void setRetOrdId(int retOrdId) {
		this.retOrdId = retOrdId;
	}

	public double getBonusDeposit() {
		return bonusDeposit;
	}

	public void setBonusDeposit(double bonusDeposit) {
		this.bonusDeposit = bonusDeposit;
	}

	public double getInhouseDeposit() {
		return inhouseDeposit;
	}

	public void setInhouseDeposit(double inhouseDeposit) {
		this.inhouseDeposit = inhouseDeposit;
	}

	public double getWireTransferDeposit() {
		return wireTransferDeposit;
	}

	public void setWireTransferDeposit(double wireTransferDeposit) {
		this.wireTransferDeposit = wireTransferDeposit;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public int getAgtOrdId() {
		return agtOrdId;
	}

	public void setAgtOrdId(int agtOrdId) {
		this.agtOrdId = agtOrdId;
	}

	public double getAgtNetGaming() {
		return agtNetGaming;
	}

	public void setAgtNetGaming(double agtNetGaming) {
		this.agtNetGaming = agtNetGaming;
	}

	
}