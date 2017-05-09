package com.skilrock.lms.web.userMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentManagementHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationManagementHelper;

/**
 * This class provides method for get Organization details and to edit
 * Organization details
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentManagementAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int daysAfter;
	private double extendedLimit = 0.0;

	Log logger = LogFactory.getLog(AgentManagementAction.class);

	private double orgCreditLimit;
	private int orgId;
	private double orgSecurityDeposit;
	private double newExtendedLimit;
	private int newDaysAfter;
	private String isResetORExtend;
	
	private HttpServletRequest request;

	private String scrapStatus;

	public String editCreditLimit() throws Exception {

		HttpSession session = getRequest().getSession();
		OrganizationBean bean = (OrganizationBean) session
				.getAttribute("ORG_SEARCH_RESULTS");
		UserInfoBean userbean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// UserDetailsBean bean=new UserDetailsBean();
		// TODO what happens if OrganizationBean is null
		boolean resetORExtendflag=true;
		String[] xclLimits=CommonMethods.getXclOrClDetails(bean.getOrgId(), "CL").split(":");

		if(Double.parseDouble(xclLimits[0])==orgCreditLimit && Double.parseDouble(xclLimits[2])==orgSecurityDeposit){
				session.setAttribute("ACA", getText("please.update.atleast.one.amt"));
				resetORExtendflag=false;
		}
		if(resetORExtendflag){
		int orgId = bean.getOrgId();
		double currentCreditAmt = bean.getCurrentCreditAmt();
		double exCreditAmt = bean.getExtendedCredit();
		logger.info("***  inside editCreditLimit  ****");
		AgentManagementHelper editOrgDetail = new AgentManagementHelper();
		if (!DrawGameOfflineHelper.fetchLoginStatus(orgId)) {
			String agtBalDist = "TRUE";
			System.out.println("---editCreditLimit----Org Type:"
					+ bean.getOrgType());
			if ("RETAILER".equals(bean.getOrgType())) {
				agtBalDist = CommonMethods.changeCreditLimitRet(
						bean.getOrgId(), getOrgCreditLimit(), "CL");
			}
			if (agtBalDist.equals("TRUE")) {
				session.setAttribute("creditLimit", orgCreditLimit);
				session.setAttribute("securityDeposit", orgSecurityDeposit);
				session.setAttribute("ACA", editOrgDetail
						.editCreditLimit( getOrgCreditLimit(), orgSecurityDeposit,
								userbean,bean, request.getRemoteAddr()));
			} else {
				session.setAttribute("ACA", agtBalDist);
			}
		} else {
			session.setAttribute("ACA", "LOGIN");
		}
		}
		session.setAttribute("claimBal", bean.getClaimableBal());
		logger.info("session attribute removed");
		System.out.println("session attribute removed");
		session.setAttribute("ORG_SEARCH_RESULTS",
				new OrganizationManagementHelper().orgDetails(orgId));
		new CommonFunctions().logoutAnyUserForcefully(bean.getOrgId());
		return SUCCESS;

	}

	public String editExtendedLimit() throws Exception {

		HttpSession session = getRequest().getSession();
		OrganizationBean bean = (OrganizationBean) session
				.getAttribute("ORG_SEARCH_RESULTS");
		UserInfoBean userbean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// UserDetailsBean bean=new UserDetailsBean();
		// TODO what happens if OrganizationBean is null
				
		boolean resetORExtendflag=true;
		String[] xclLimits=CommonMethods.getXclOrClDetails(bean.getOrgId(), "XCL").split(":");
		
		if(isResetORExtend.equalsIgnoreCase("extend")){
			
			if(Double.parseDouble(xclLimits[0])==extendedLimit && Integer.parseInt(xclLimits[1])==daysAfter){
				session.setAttribute("ACA", getText("msg.pls.update.amt.then.click.btn.thanks"));
				resetORExtendflag=false;
			}
		}else if(isResetORExtend.equalsIgnoreCase("reset")){
			if(Double.parseDouble(xclLimits[0])==0.0){
				session.setAttribute("ACA", getText("msg.cant.reset.val.when.amt.already.zero"));
				resetORExtendflag=false;
			}
		}
		if(resetORExtendflag){
		int orgId = bean.getOrgId();
		double currentCreditAmt = bean.getCurrentCreditAmt();
		double creditLimit = bean.getOrgCreditLimit();
		logger.info("  inside editExtendlimit  " + bean.getParentOrgId());
		System.out.println("  inside editExtendlimit  ");
		if (!DrawGameOfflineHelper.fetchLoginStatus(orgId)) {
			String agtBalDist = "TRUE";
			System.out.println("---editExtendedLimit----Org Type:"
					+ bean.getOrgType());
			if ("RETAILER".equals(bean.getOrgType())) {
				System.out.println("------inside changeCreditLimitRet");
				agtBalDist = CommonMethods.changeCreditLimitRet(
						bean.getOrgId(), getExtendedLimit(), "XCL");
			}
			if (agtBalDist.equals("TRUE")) {
				AgentManagementHelper editOrgDetail = new AgentManagementHelper();
				session.setAttribute("extendedLimit", extendedLimit);
				session.setAttribute("extendedLimitForDays", getDaysAfter());
				session.setAttribute("ACA", editOrgDetail.editExtendedLimit(getExtendedLimit(),
						getDaysAfter(),userbean,bean));
			} else {
				session.setAttribute("ACA", agtBalDist);
			}
		} else {
			session.setAttribute("ACA", "LOGIN");
		}
		}
		session.setAttribute("claimBal", bean.getClaimableBal());
		logger.info("session attribute removed");
		System.out.println("session attribute removed");

		session.setAttribute("ORG_SEARCH_RESULTS",
				new OrganizationManagementHelper().orgDetails(orgId));

		return SUCCESS;

	}

	public int getDaysAfter() {
		return daysAfter;
	}

	public double getExtendedLimit() {
		return extendedLimit;
	}

	public double getOrgCreditLimit() {
		return orgCreditLimit;
	}

	public int getOrgId() {
		return orgId;
	}

	public double getOrgSecurityDeposit() {
		return orgSecurityDeposit;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getScrapStatus() {
		return scrapStatus;
	}

	
	public double getNewExtendedLimit() {
		return newExtendedLimit;
	}

	public void setNewExtendedLimit(double newExtendedLimit) {
		this.newExtendedLimit = newExtendedLimit;
	}

	public int getNewDaysAfter() {
		return newDaysAfter;
	}

	public void setNewDaysAfter(int newDaysAfter) {
		this.newDaysAfter = newDaysAfter;
	}

	public String getIsResetORExtend() {
		return isResetORExtend;
	}

	public void setIsResetORExtend(String isResetORExtend) {
		this.isResetORExtend = isResetORExtend;
	}

	public String orgDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);

		AgentManagementHelper orgDetail = new AgentManagementHelper();
		OrganizationBean bean = orgDetail.orgDetails(orgId);

		session.setAttribute("ORG_SEARCH_RESULTS", bean);
		return SUCCESS;

	}

	public void setDaysAfter(int daysAfter) {
		this.daysAfter = daysAfter;
	}

	public void setExtendedLimit(double extendedLimit) {
		this.extendedLimit = extendedLimit;
	}

	public void setOrgCreditLimit(double orgCreditLimit) {
		this.orgCreditLimit = orgCreditLimit;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgSecurityDeposit(double orgSecurityDeposit) {
		this.orgSecurityDeposit = orgSecurityDeposit;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setScrapStatus(String scrapStatus) {
		this.scrapStatus = scrapStatus;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String updateScrapStatus() throws Exception {

		HttpSession session = getRequest().getSession();
		OrganizationBean bean = (OrganizationBean) session
				.getAttribute("ORG_SEARCH_RESULTS");

		// UserDetailsBean bean=new UserDetailsBean();
		// TODO what happens if OrganizationBean is null
		int orgId = bean.getOrgId();
		System.out.println("  inside update Status for pwt scrap  ");
		logger.info(" inside update Status for pwt scrap  ");
		AgentManagementHelper updateScrapStatus = new AgentManagementHelper();
		updateScrapStatus.updateOrgScrapStatus(orgId, getScrapStatus());
		session.removeAttribute("ORG_SEARCH_RESULTS");
		logger.info("session attribute removed");
		System.out.println("session attribute removed");
		return SUCCESS;

	}

}