package com.skilrock.lms.web.userMgmt.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationHelper;

/**
 * This class is used to hold organization entries and checks for duplicate
 * organization
 * 
 * @author Skilrock Technologies
 * 
 */
public class OrganizationAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addrLine1 = "";

	private String addrLine2 = "";
	private String city = "";
	private String country = "";
	private double creditLimit;
	Log logger = LogFactory.getLog(OrganizationAction.class);
	private String orgName = "";
	private String orgType = "";
	private long pin = 0;
	private int pntId;
	private HttpServletRequest request;
	private double security = 0;;
	private String state = "";
	private String statusorg = "";
	private String vatRegNo;

	/**
	 * This method is used to check duplicate organization and to hold the
	 * organization entries
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String createOrganization() throws Exception {
		HttpSession session = request.getSession();
		session.setAttribute("org", this);
		OrganizationHelper orgReg = new OrganizationHelper();
		String returntype = orgReg.verifyOrgName(getOrgName());
		if (returntype.equals("ERROR")) {
			addActionError("ORGANIZATION Already Exists!!");
			return ERROR;
		} else if (returntype.equals("SUCCESS")) {
			// get the email mailing privilege list from database
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");

			Map<Integer, String> reportTypeTitleMap = null;
			if ("BO".equalsIgnoreCase(userBean.getUserType())) {
				reportTypeTitleMap = orgReg.getMailingReportTitle("AGENT");
			} else if ("AGENT".equalsIgnoreCase(userBean.getUserType())) {
				reportTypeTitleMap = orgReg.getMailingReportTitle("RETAILER");
			}
			logger.debug("email privilege list in orgnization creation == "
					+ reportTypeTitleMap);
			System.out
					.println("email privilege list in orgnization creation == "
							+ reportTypeTitleMap);
			session.setAttribute("reportTypeTitleMap", reportTypeTitleMap);
			return SUCCESS;
		}
		addActionError("PLEASE ENTER ORGANIZATION NAME!!");
		return ERROR;

	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public long getPin() {
		return pin;
	}

	public int getPntId() {
		return pntId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public double getSecurity() {
		return security;
	}

	public String getState() {
		return state;
	}

	public String getStatusorg() {
		return statusorg;
	}

	public String getVatRegNo() {
		return vatRegNo;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPin(long pin) {
		this.pin = pin;
	}

	public void setPntId(int pntId) {
		this.pntId = pntId;
	}

	public void setSecurity(double security) {
		this.security = security;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStatusorg(String statusorg) {
		this.statusorg = statusorg;
	}

	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}

}