package com.skilrock.ola.javaBeans;

import java.util.List;

public class CountryDataBean {

	private String countryCode;
	private String countryName;
	private List<StateDataBean> stateBeanList;
	public String getCountryCode() {
		return countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public List<StateDataBean> getStateBeanList() {
		return stateBeanList;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public void setStateBeanList(List<StateDataBean> stateBeanList) {
		this.stateBeanList = stateBeanList;
	}
	@Override
	public String toString() {
		return "CountryDataBean [countryCode=" + countryCode + ", countryName="
				+ countryName + ", stateBeanList=" + stateBeanList + "]";
	}
	
	
	
}
