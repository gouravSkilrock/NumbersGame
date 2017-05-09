package com.skilrock.ola.javaBeans;

import java.util.List;

public class StateDataBean {
	private String stateCode;
	private String stateName;
	private List<CityDataBean> cityBeanList;
	public String getStateCode() {
		return stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public List<CityDataBean> getCityBeanList() {
		return cityBeanList;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public void setCityBeanList(List<CityDataBean> cityBeanList) {
		this.cityBeanList = cityBeanList;
	}
	@Override
	public String toString() {
		return "StateDataBean [cityBeanList=" + cityBeanList + ", stateCode="
				+ stateCode + ", stateName=" + stateName + "]";
	}
	
	
}
