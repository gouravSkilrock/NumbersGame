package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.HashMap;


public class LmsWrapperInventoryMenuDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> agentMap;
	private HashMap<String, String> retMap ;
	private HashMap<String, String> modelMap ;
	private HashMap<String, String> invSpecMap;
	private HashMap<String, String> nonConsInvMap ;
	private HashMap<String, String> consInvMap;
	private HashMap<String, String> brandMap;
	private HashMap<String,String> brandIdMap;
	private HashMap<String,String> modelIdMap;
	private HashMap<String,String> inventoryIdMap;
	private HashMap<String,String> brandNameMap;
	private HashMap<String,String> modelNameMap;
	private HashMap<String,String> inventoryNameMap;
	private HashMap<String,String> consInventoryIdMap;
	private HashMap<String,String> consInventoryNameMap;
	private HashMap<String,String> consModelIdMap;
	private HashMap<String,String> consInvIdFromModelMap;
	private String status;
	private String errorcode;
	private boolean isSuccess;
	
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public HashMap<String, String> getAgentMap() {
		return agentMap;
	}
	public void setAgentMap(HashMap<String, String> agentMap) {
		this.agentMap = agentMap;
	}
	public HashMap<String, String> getRetMap() {
		return retMap;
	}
	public void setRetMap(HashMap<String, String> retMap) {
		this.retMap = retMap;
	}
	public HashMap<String, String> getModelMap() {
		return modelMap;
	}
	public void setModelMap(HashMap<String, String> modelMap) {
		this.modelMap = modelMap;
	}
	public HashMap<String, String> getInvSpecMap() {
		return invSpecMap;
	}
	public void setInvSpecMap(HashMap<String, String> invSpecMap) {
		this.invSpecMap = invSpecMap;
	}
	public HashMap<String, String> getNonConsInvMap() {
		return nonConsInvMap;
	}
	public void setNonConsInvMap(HashMap<String, String> nonConsInvMap) {
		this.nonConsInvMap = nonConsInvMap;
	}
	public HashMap<String, String> getConsInvMap() {
		return consInvMap;
	}
	public void setConsInvMap(HashMap<String, String> consInvMap) {
		this.consInvMap = consInvMap;
	}
	public HashMap<String, String> getBrandMap() {
		return brandMap;
	}
	public void setBrandMap(HashMap<String, String> brandMap) {
		this.brandMap = brandMap;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public HashMap<String, String> getBrandIdMap() {
		return brandIdMap;
	}
	public void setBrandIdMap(HashMap<String, String> brandIdMap) {
		this.brandIdMap = brandIdMap;
	}
	public HashMap<String, String> getModelIdMap() {
		return modelIdMap;
	}
	public void setModelIdMap(HashMap<String, String> modelIdMap) {
		this.modelIdMap = modelIdMap;
	}
	public HashMap<String, String> getInventoryIdMap() {
		return inventoryIdMap;
	}
	public void setInventoryIdMap(HashMap<String, String> inventoryIdMap) {
		this.inventoryIdMap = inventoryIdMap;
	}
	public HashMap<String, String> getBrandNameMap() {
		return brandNameMap;
	}
	public void setBrandNameMap(HashMap<String, String> brandNameMap) {
		this.brandNameMap = brandNameMap;
	}
	public HashMap<String, String> getModelNameMap() {
		return modelNameMap;
	}
	public void setModelNameMap(HashMap<String, String> modelNameMap) {
		this.modelNameMap = modelNameMap;
	}
	public HashMap<String, String> getInventoryNameMap() {
		return inventoryNameMap;
	}
	public void setInventoryNameMap(HashMap<String, String> inventoryNameMap) {
		this.inventoryNameMap = inventoryNameMap;
	}
	public HashMap<String, String> getConsInventoryIdMap() {
		return consInventoryIdMap;
	}
	public void setConsInventoryIdMap(HashMap<String, String> consInventoryIdMap) {
		this.consInventoryIdMap = consInventoryIdMap;
	}
	public HashMap<String, String> getConsInventoryNameMap() {
		return consInventoryNameMap;
	}
	public void setConsInventoryNameMap(HashMap<String, String> consInventoryNameMap) {
		this.consInventoryNameMap = consInventoryNameMap;
	}
	public HashMap<String, String> getConsModelIdMap() {
		return consModelIdMap;
	}
	public void setConsModelIdMap(HashMap<String, String> consModelIdMap) {
		this.consModelIdMap = consModelIdMap;
	}
	public HashMap<String, String> getConsInvIdFromModelMap() {
		return consInvIdFromModelMap;
	}
	public void setConsInvIdFromModelMap(
			HashMap<String, String> consInvIdFromModelMap) {
		this.consInvIdFromModelMap = consInvIdFromModelMap;
	}
	
	
}
