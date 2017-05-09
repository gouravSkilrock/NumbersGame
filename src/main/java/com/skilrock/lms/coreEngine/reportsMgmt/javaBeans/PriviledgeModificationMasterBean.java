package com.skilrock.lms.coreEngine.reportsMgmt.javaBeans;

import java.util.List;
import java.util.Map;

import com.skilrock.lms.sportsLottery.javaBeans.SLEDataFace;

public class PriviledgeModificationMasterBean implements SLEDataFace {
	private static final long serialVersionUID = 1L;

	private int userId;
	private String startDate;
	private String endDate;
	private String username;
	private String registerDate;
	private String registerBy;
	private String emailId;
	private List<PriviledgeModificationHeaderBean> headerList;
	private Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap;

	public PriviledgeModificationMasterBean() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<PriviledgeModificationHeaderBean> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<PriviledgeModificationHeaderBean> headerList) {
		this.headerList = headerList;
	}

	public Map<String, Map<String, List<PriviledgeModificationDataBean>>> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap) {
		this.serviceMap = serviceMap;
	}
}