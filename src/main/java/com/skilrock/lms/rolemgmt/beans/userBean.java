package com.skilrock.lms.rolemgmt.beans;

public class userBean {

	String addressLine1;
	String addressLine2;
	String emailId;
	String firstName;
	String lastName;
	Long mobileNum;
	Long phoneNum;
	Long postalCode;
	String status;
	Integer userId;

	public userBean(String firstName, String lastName, int userId,
			String emailId, String addressLine1, String addressLine2,
			Long postalCode, Long phoneNum, Long mobileNum, String status) {
		System.out.println("object created");
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.userId = userId;
		this.status = status;
		this.postalCode = postalCode;
		this.phoneNum = phoneNum;
		this.mobileNum = mobileNum;
		this.emailId = emailId;

		// TODO Auto-generated constructor stub
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Long getMobileNum() {
		return mobileNum;
	}

	public Long getPhoneNum() {
		return phoneNum;
	}

	public Long getPostalCode() {
		return postalCode;
	}

	public String getStatus() {
		return status;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMobileNum(Long mobileNum) {
		this.mobileNum = mobileNum;
	}

	public void setPhoneNum(Long phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setPostalCode(Long postalCode) {
		this.postalCode = postalCode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}