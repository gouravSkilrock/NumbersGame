package com.skilrock.lms.beans;

import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;

public class AuditTrailBean implements Comparable<AuditTrailBean>, SLEDataFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loginName;
	private String userName;
	private String accessIp;
	private String accessTime;
	private String activity;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Override
	public int compareTo(AuditTrailBean obj) {
		if (obj != null && obj.accessTime != null) {
			return this.accessTime.compareTo(obj.accessTime);
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "AuditTrailBean [loginName=" + loginName + ", userName="
				+ userName + ", accessIp=" + accessIp + ", accessTime="
				+ accessTime + ", activity=" + activity + "]";
	}

}
