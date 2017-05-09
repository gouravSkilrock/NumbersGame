package com.skilrock.ola.reportsMgmt.javaBeans;

import java.io.Serializable;

public class OlaAgentReportBean implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int retailerId;
private double playerNetGaming;
private double commissionCalculated;
private double doneCommission;
private double pendingCommission;
private String playerName;


private String fromDate;
private String toDate;
private double depositAmt;
private double withdrawlAmt;
private double agentNetGaming;
private double agentNetGamingCommission;
private double agentDepositCommission;
private double agentWithdrawlCommission;



private double agentCommissionCalculated;
private String agentName;
private double totalDepositAmount;
private double totalWithdrawlAmount ;
private double totalDepositCommission;
private double totalWithdrawlCommission;
private double totalPlayerNetGaming;
private double totalCommissionCalculated ;




public double getAgentCommissionCalculated() {
	return agentCommissionCalculated;
}
public void setAgentCommissionCalculated(double agentCommissionCalculated) {
	this.agentCommissionCalculated = agentCommissionCalculated;
}
public String getAgentName() {
	return agentName;
}
public void setAgentName(String agentName) {
	this.agentName = agentName;
}
public double getTotalDepositAmount() {
	return totalDepositAmount;
}
public void setTotalDepositAmount(double totalDepositAmount) {
	this.totalDepositAmount = totalDepositAmount;
}
public double getTotalWithdrawlAmount() {
	return totalWithdrawlAmount;
}
public void setTotalWithdrawlAmount(double totalWithdrawlAmount) {
	this.totalWithdrawlAmount = totalWithdrawlAmount;
}
public double getTotalDepositCommission() {
	return totalDepositCommission;
}
public void setTotalDepositCommission(double totalDepositCommission) {
	this.totalDepositCommission = totalDepositCommission;
}
public double getTotalWithdrawlCommission() {
	return totalWithdrawlCommission;
}
public void setTotalWithdrawlCommission(double totalWithdrawlCommission) {
	this.totalWithdrawlCommission = totalWithdrawlCommission;
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
public double getAgentNetGamingCommission() {
	return agentNetGamingCommission;
}
public void setAgentNetGamingCommission(double agentNetGamingCommission) {
	this.agentNetGamingCommission = agentNetGamingCommission;
}
public String getFromDate() {
	return fromDate;
}
public void setFromDate(String fromDate) {
	this.fromDate = fromDate;
}
public String getToDate() {
	return toDate;
}
public void setToDate(String toDate) {
	this.toDate = toDate;
}
public double getDepositAmt() {
	return depositAmt;
}
public void setDepositAmt(double depositAmt) {
	this.depositAmt = depositAmt;
}
public double getWithdrawlAmt() {
	return withdrawlAmt;
}
public void setWithdrawlAmt(double withdrawlAmt) {
	this.withdrawlAmt = withdrawlAmt;
}
public double getAgentNetGaming() {
	return agentNetGaming;
}
public void setAgentNetGaming(double agentNetGaming) {
	this.agentNetGaming = agentNetGaming;
}
public double getAgentDepositCommission() {
	return agentDepositCommission;
}
public void setAgentDepositCommission(double agentDepositCommission) {
	this.agentDepositCommission = agentDepositCommission;
}
public double getAgentWithdrawlCommission() {
	return agentWithdrawlCommission;
}
public void setAgentWithdrawlCommission(double agentWithdrawlCommission) {
	this.agentWithdrawlCommission = agentWithdrawlCommission;
}

public String getPlayerName() {
	return playerName;
}
public void setPlayerName(String playerName) {
	this.playerName = playerName;
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
public double getDoneCommission() {
	return doneCommission;
}
public void setDoneCommission(double doneCommission) {
	this.doneCommission = doneCommission;
}
public double getPendingCommission() {
	return pendingCommission;
}
public void setPendingCommission(double pendingCommission) {
	this.pendingCommission = pendingCommission;
}
public int getRetailerId() {
	return retailerId;
}
public void setRetailerId(int retailerId) {
	this.retailerId = retailerId;
}


}
