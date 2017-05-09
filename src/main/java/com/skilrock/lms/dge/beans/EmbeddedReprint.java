package com.skilrock.lms.dge.beans;

import com.skilrock.lms.beans.UserInfoBean;

public class EmbeddedReprint {

	private Object gameBean;
	private UserInfoBean userInfoBean;
	private String balance;
	private String countryDeployed;
	private String raffleDrawDay;
	private String raffleGameDataString;

	public Object getGameBean() {
		return gameBean;
	}

	public void setGameBean(Object gameBean) {
		this.gameBean = gameBean;
	}

	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}

	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCountryDeployed() {
		return countryDeployed;
	}

	public void setCountryDeployed(String countryDeployed) {
		this.countryDeployed = countryDeployed;
	}

	public String getRaffleDrawDay() {
		return raffleDrawDay;
	}

	public void setRaffleDrawDay(String raffleDrawDay) {
		this.raffleDrawDay = raffleDrawDay;
	}

	public String getRaffleGameDataString() {
		return raffleGameDataString;
	}

	public void setRaffleGameDataString(String raffleGameDataString) {
		this.raffleGameDataString = raffleGameDataString;
	}

}
