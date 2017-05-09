package com.skilrock.lms.beans;

import java.io.Serializable;

public class ResponsibleGamingBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String criAction;
	private String criLimit;
	private String criteria;
	private int criteriaId;
	private String orgType;
	private String status;
	private double criteriaLimit;
	public String getCriAction() {
		return criAction;
	}
	public void setCriAction(String criAction) {
		this.criAction = criAction;
	}
	public String getCriLimit() {
		return criLimit;
	}
	public void setCriLimit(String criLimit) {
		this.criLimit = criLimit;
	}
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public int getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(int criteriaId) {
		this.criteriaId = criteriaId;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCriteriaLimit(double criteriaLimit) {
		this.criteriaLimit = criteriaLimit;
	}
	public double getCriteriaLimit() {
		return criteriaLimit;
	}

	
}
