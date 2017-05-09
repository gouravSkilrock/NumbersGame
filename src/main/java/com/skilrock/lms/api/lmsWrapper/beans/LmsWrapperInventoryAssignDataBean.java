package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperInventoryAssignDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int userOrgId;
	private String[] nonConsInvId;
	private String[] nonConsModelId;
	private String[] nonConsBrandId;
	private String[] serNo;
    private	String[] consInvId;
    private String[] consModelId;
    private int[] consQty;
    private int agtOrgId;
    private int retOrgId;
    private String status;
    private String errorcode;
    private String systemUserName;
	private String systemPassword;
	public int getUserOrgId() {
		return userOrgId;
	}
	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}
	public String[] getNonConsInvId() {
		return nonConsInvId;
	}
	public void setNonConsInvId(String[] nonConsInvId) {
		this.nonConsInvId = nonConsInvId;
	}
	public String[] getNonConsModelId() {
		return nonConsModelId;
	}
	public void setNonConsModelId(String[] nonConsModelId) {
		this.nonConsModelId = nonConsModelId;
	}
	public String[] getNonConsBrandId() {
		return nonConsBrandId;
	}
	public void setNonConsBrandId(String[] nonConsBrandId) {
		this.nonConsBrandId = nonConsBrandId;
	}
	public String[] getSerNo() {
		return serNo;
	}
	public void setSerNo(String[] serNo) {
		this.serNo = serNo;
	}
	public String[] getConsInvId() {
		return consInvId;
	}
	public void setConsInvId(String[] consInvId) {
		this.consInvId = consInvId;
	}
	public String[] getConsModelId() {
		return consModelId;
	}
	public void setConsModelId(String[] consModelId) {
		this.consModelId = consModelId;
	}
	public int[] getConsQty() {
		return consQty;
	}
	public void setConsQty(int[] consQty) {
		this.consQty = consQty;
	}
	public int getAgtOrgId() {
		return agtOrgId;
	}
	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}
	public int getRetOrgId() {
		return retOrgId;
	}
	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
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
	public String getSystemUserName() {
		return systemUserName;
	}
	public void setSystemUserName(String systemUserName) {
		this.systemUserName = systemUserName;
	}
	public String getSystemPassword() {
		return systemPassword;
	}
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
	
    
    
}
