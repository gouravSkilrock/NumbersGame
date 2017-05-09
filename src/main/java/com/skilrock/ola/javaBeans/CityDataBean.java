package com.skilrock.ola.javaBeans;

public class CityDataBean {
	private String cityCode;
	private String cityName;

	public String getCityCode() {
		return cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return "CityDataBean [cityCode=" + cityCode + ", cityName=" + cityName
				+ "]";
	}
}
