package com.skilrock.lms.api.beans;

public class TpPwtApiBean {
	private String ticketNbr;
	private String systemUserName;
	private String systemUserPassword;
	private double amount;
	private String refTransId;
	private String firstName;
	private String lastName;
	private String idType;
	private String idNumber;
	private String plrCountry;
	private String plrState;
	
	public String getTicketNbr() {
		return ticketNbr;
	}
	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemUserPassword() {
		return systemUserPassword;
	}
	public void setSystemUserPassword(String systemUserPassword) {
		this.systemUserPassword = systemUserPassword;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRefTransId() {
		return refTransId;
	}
	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getPlrCountry() {
		return plrCountry;
	}
	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}
	public String getPlrState() {
		return plrState;
	}
	public void setPlrState(String plrState) {
		this.plrState = plrState;
	}
}
