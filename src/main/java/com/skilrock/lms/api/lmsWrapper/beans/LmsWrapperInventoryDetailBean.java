package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class LmsWrapperInventoryDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serNo[];
	private String brandsId;
	private String invId;
	private double cost;
	private String isNew;
	private String modelId;
	private int orgId;
	private String nonConsInvName;
	private String nonConsBrandName;
	private String nonConsModelName;
	public String[] getSerNo() {
		return serNo;
	}
	public void setSerNo(String[] serNo) {
		this.serNo = serNo;
	}
	public String getBrandsId() {
		return brandsId;
	}
	public void setBrandsId(String brandsId) {
		this.brandsId = brandsId;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getInvId() {
		return invId;
	}
	public void setInvId(String invId) {
		this.invId = invId;
	}
	public String getNonConsInvName() {
		return nonConsInvName;
	}
	public void setNonConsInvName(String nonConsInvName) {
		this.nonConsInvName = nonConsInvName;
	}
	public String getNonConsBrandName() {
		return nonConsBrandName;
	}
	public void setNonConsBrandName(String nonConsBrandName) {
		this.nonConsBrandName = nonConsBrandName;
	}
	public String getNonConsModelName() {
		return nonConsModelName;
	}
	public void setNonConsModelName(String nonConsModelName) {
		this.nonConsModelName = nonConsModelName;
	}
	
	
}
