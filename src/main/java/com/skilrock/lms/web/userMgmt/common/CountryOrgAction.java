package com.skilrock.lms.web.userMgmt.common;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.coreEngine.userMgmt.common.CountryOrgHelper;

//import example.Yogi;

/**
 * This class is used to fetch active country country list from the database.
 * 
 * @author SkilRockTechnologies
 * 
 */
public class CountryOrgAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Log logger = LogFactory.getLog(CountryOrgAction.class);

	private HttpServletRequest request;
	private String role;
	private List serviceList;
	private String userType;
	private String vat_applicable;
	private boolean agtBnkMapping;

	public String fetchService() {
		logger.info("********User Services Fatch**********");
		System.out.println("********User Services Fatch**********");
		CountryOrgHelper country = new CountryOrgHelper();
		request.getSession().setAttribute("service_list",
				country.getAvlSerInterface(userType));
		return SUCCESS;
	}

	/**
	 * This class provides method for fetching active country list
	 * 
	 * @author Skilrock Technologies
	 * 
	 */

	public String getCountry() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		vat_applicable = (String) sc.getAttribute("VAT_APPLICABLE");

		CountryOrgHelper country = new CountryOrgHelper();
		List countryList = country.getCountry();
		HttpSession session = request.getSession();
		session.setAttribute("list", countryList);

		return SUCCESS;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRole() {
		return role;
	}

	public List getServiceList() {
		return serviceList;
	}

	public String getUserType() {
		return userType;
	}

	public String getVat_applicable() {
		return vat_applicable;
	}

	public String registration() throws Exception {
		System.out.println("*******Retailer Registration********");
		ServletContext sc = ServletActionContext.getServletContext();
		vat_applicable = (String) sc.getAttribute("VAT_APPLICABLE");
		// List countryList=new ArrayList();
		CountryOrgHelper country = new CountryOrgHelper();
		List countryList = country.getCountry();
		setServiceList(country.getAvlSerInterface(userType));
		setRole(country.getRoleMasterName(userType)); // Vaibhav
		System.out.println("*********Role" + role);
		HttpSession session = request.getSession();
		session.setAttribute("list", countryList);
		session.setAttribute("service_list", serviceList);
		setAgtBnkMapping("true".equalsIgnoreCase(Utility.getPropertyValue("AGENT_BANK_MAPPING")));
		return SUCCESS;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setServiceList(List serviceList) {
		this.serviceList = serviceList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setVat_applicable(String vat_applicable) {
		this.vat_applicable = vat_applicable;
	}

	public boolean isAgtBnkMapping() {
		return agtBnkMapping;
	}

	public void setAgtBnkMapping(boolean agtBnkMapping) {
		this.agtBnkMapping = agtBnkMapping;
	}

	
}
