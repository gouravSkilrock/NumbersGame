package com.skilrock.ola.userMgmt.javaBeans;
import java.io.Serializable;

import com.skilrock.ola.api.beans.RequestResponseBean;

public class OlaPlayerRegistrationRequestBean extends RequestResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String gender;
	private String dateOfBirth;
	private String username;
	private String tgUserName;
	private String password;
	private String email;
	public String tgEmail;
	private String phone;
	private String tgPhone;
	private String address;
	private String city;
	private String state;
	private String country;
	private String msg;
	private boolean isSuccess;
	private String accountId;
	private int walletId;
	private String plrRegDate;
	private String walletName;
	private String regType;
	private String regFieldType;
	private String requestIp;
	private int playerId;
	
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getPlrRegDate() {
		return plrRegDate;
	}
	public void setPlrRegDate(String plrRegDate) {
		this.plrRegDate = plrRegDate;
	}

	public int getWalletId() {
		return walletId;
	}
	public void setWalletId(int walletId) {
		this.walletId = walletId;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	public String getWalletName() {
		return walletName;
	}
	public String getTgPhone() {
		return tgPhone;
	}
	public void setTgPhone(String tgPhone) {
		this.tgPhone = tgPhone;
	}
	public String getRegFieldType() {
		return regFieldType;
	}
	public void setRegFieldType(String regFieldType) {
		this.regFieldType = regFieldType;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getTgUserName() {
		return tgUserName;
	}
	public void setTgUserName(String tgUserName) {
		this.tgUserName = tgUserName;
	}
	public String getTgEmail() {
		return tgEmail;
	}
	public void setTgEmail(String tgEmail) {
		this.tgEmail = tgEmail;
	}
	
	
	
}
