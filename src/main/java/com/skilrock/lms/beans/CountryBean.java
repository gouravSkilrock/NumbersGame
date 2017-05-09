package com.skilrock.lms.beans;

import java.io.Serializable;

public class CountryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String countryName;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
