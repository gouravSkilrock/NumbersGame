package com.skilrock.lms.web.reportsMgmt.common;

import java.util.List;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.SecurityDepositAndLevyBean;
import com.skilrock.lms.coreEngine.reportsMgmt.common.SecurityDepositAndLevyReportHelper;

public class SecurityDepositAndLevyReportAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private List<SecurityDepositAndLevyBean> securityDepostAndLevyBeanList;
	private int agentOrgId;

	public String fetchReportData() {
		try {
			SecurityDepositAndLevyReportHelper helper = new SecurityDepositAndLevyReportHelper();
			setSecurityDepostAndLevyBeanList(helper.fetchReportData(agentOrgId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setSecurityDepostAndLevyBeanList(
			List<SecurityDepositAndLevyBean> securityDepostAndLevyBeanList) {
		this.securityDepostAndLevyBeanList = securityDepostAndLevyBeanList;
	}

	public List<SecurityDepositAndLevyBean> getSecurityDepostAndLevyBeanList() {
		return securityDepostAndLevyBeanList;
	}

}
