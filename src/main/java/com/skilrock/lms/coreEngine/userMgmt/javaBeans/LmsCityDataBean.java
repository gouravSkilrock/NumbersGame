package com.skilrock.lms.coreEngine.userMgmt.javaBeans;

import java.io.Serializable;

public class LmsCityDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cityCode;
	private String CityName;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	@Override
	public String toString() {
		return "LmsCityDataBean [CityName=" + CityName + ", cityCode="
				+ cityCode + "]";
	}
}
