package com.skilrock.lms.web.loginMgmt;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;

public class OpenSLEPageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String userType;

	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public OpenSLEPageAction() {
		super(OpenSLEPageAction.class);
	}

	public String openTab() throws Exception {
		try{
			if("RETAILER".equalsIgnoreCase(userType)){
				response.sendRedirect(Utility.serverSLEWEBURL+"com/skilrock/sle/web/merchantUser/loginMgmt/Action/merchantUserLogin.action?userType=RETAILER&userName="+getUserBean().getUserName().trim()+"&merCode=RMS"+"&sessId="+getSession().getId()+"&parentOrgName="+getUserBean().getParentOrgName());
			} else{
				response.sendRedirect(Utility.serverSLEWEBURL+"com/skilrock/sle/web/merchantUser/loginMgmt/Action/merchantUserLogin.action?userType=BO&userName="+getUserBean().getUserName().trim()+"&merCode=RMS"+"&sessId="+getSession().getId());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
}