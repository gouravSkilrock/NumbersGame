package com.skilrock.lms.coreEngine.virtualSport.beans;

public class VSResponseBean {
	private String methodName;
	private String requestIp;
	private String utcDate;
	private VSCommonResponseBean vsCommonResponseBean;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getUtcDate() {
		return utcDate;
	}

	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}

	public VSCommonResponseBean getVsCommonResponseBean() {
		return vsCommonResponseBean;
	}

	public void setVsCommonResponseBean(
			VSCommonResponseBean vsCommonResponseBean) {
		this.vsCommonResponseBean = vsCommonResponseBean;
	}

	@Override
	public String toString() {
		return "VSResponseBean [methodName=" + methodName + ", requestIp="
				+ requestIp + ", utcDate=" + utcDate
				+ ", vsCommonResponseBean=" + vsCommonResponseBean + "]";
	}

}
