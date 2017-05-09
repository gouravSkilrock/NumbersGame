package com.skilrock.lms.beans;

import java.io.Serializable;

public class UserQuesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String firstName;
	private String lastName;
	private String secAns = null;
	private String secQues = null;
	private int userId;
	private String userName = null;
	private String userNameDb = null;

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSecAns() {
		return secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserNameDb() {
		return userNameDb;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserNameDb(String userNameDb) {
		this.userNameDb = userNameDb;
	}

}
