package com.skilrock.lms.web.sportsLottery.reportsMgmt.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class SLEOrgReportRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int orgId;
	private String orgType;
	private Timestamp fromDate;
	private Timestamp toDate;
	private int gameId;

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getToDate() {
		return toDate;
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
