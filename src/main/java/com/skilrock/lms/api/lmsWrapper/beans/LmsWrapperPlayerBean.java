package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperPlayerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankAccNbr;
	private String bankBranch;
	// Added by Sumit
	private String bankName;
	private String emailId;
	private String firstName;
	private String idNumber;
	private String idType;
	private String lastName;
	private String locationCity;
	private String phone;
	private String plrAddr1;
	private String plrAddr2;
	private String plrCity;

	private String plrCountry;
	private int plrId;
	private long plrPin;
	private String plrState;
	public String getBankAccNbr() {
		return bankAccNbr;
	}
	public void setBankAccNbr(String bankAccNbr) {
		this.bankAccNbr = bankAccNbr;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPlrAddr1() {
		return plrAddr1;
	}
	public void setPlrAddr1(String plrAddr1) {
		this.plrAddr1 = plrAddr1;
	}
	public String getPlrAddr2() {
		return plrAddr2;
	}
	public void setPlrAddr2(String plrAddr2) {
		this.plrAddr2 = plrAddr2;
	}
	public String getPlrCity() {
		return plrCity;
	}
	public void setPlrCity(String plrCity) {
		this.plrCity = plrCity;
	}
	public String getPlrCountry() {
		return plrCountry;
	}
	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}
	public int getPlrId() {
		return plrId;
	}
	public void setPlrId(int plrId) {
		this.plrId = plrId;
	}
	public long getPlrPin() {
		return plrPin;
	}
	public void setPlrPin(long plrPin) {
		this.plrPin = plrPin;
	}
	public String getPlrState() {
		return plrState;
	}
	public void setPlrState(String plrState) {
		this.plrState = plrState;
	}

	
}
