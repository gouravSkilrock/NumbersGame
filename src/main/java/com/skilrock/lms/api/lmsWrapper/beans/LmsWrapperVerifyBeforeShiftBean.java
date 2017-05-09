package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;
import java.util.HashMap;

public class LmsWrapperVerifyBeforeShiftBean implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String orgName;
	
	// USED MAP IF NEW VALIDATIONS ARE ADDED YOU NEED NOT TO REGENERATE THE CLIENT 
	private HashMap <String, String> errorMap;
	
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public HashMap<String, String> getErrorMap() {
		return errorMap;
	}
	public void setErrorMap(HashMap<String, String> errorMap) {
		this.errorMap = errorMap;
	}
	
	public boolean equals(Object object){
		
		if(object != null && object instanceof LmsWrapperVerifyBeforeShiftBean)
			return ((LmsWrapperVerifyBeforeShiftBean)object).getOrgName().equalsIgnoreCase(this.orgName);
		
	return false; 	
	}
	
	public int hashCode(){
		return orgName.toLowerCase().hashCode();
	}
	
	
	
}
