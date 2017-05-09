package com.skilrock.lms.web.userMgmt.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.AgentRegistrationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.CreateOrgUserHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;

/**
 * This class provides methods for creating new user
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentUserAction extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addrLine1;

	private String addrLine2;

	private String appLimit;
	private String autoScrap;
	private String city;
	private String country;
	private double creditLimit;
	private String email;
	private String emailPrivId[];
	private Map<String, String> errorMap;
	private String firstName;
	private int id[];
	private String idNo;
	private String idType;
	private String lastName;

	Log logger = LogFactory.getLog(AgentUserAction.class);
	// organization details
	private String orgName;
	private String orgType;
	private String payLimit;
	private long phone;
	private long mobile;
	private String code;
	private long pin;
	private int pntId;
	private String reconReportType;
	private HttpServletRequest request;
	private String role;
	private String scrapLimit;
	private String secAns;
	private String secQues;

	private double security;
	private String state;
	private String status;
	private String statusorg;
	private String[] statusTable;
	private String cityCode;

	private String userName;
	private String vatRegNo;
	private String verLimit;
	private String area;
	private String division;
	private int branchId;
	private int bankId;
	private String accountNbr;
	private boolean agtBnkMapping;

	// this method is used to create agent registration process
	public String agentOrgUserReg() throws LMSException {

		// Register AGENT Organization and user info details
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Map<String, String> errorMap = null;
		setAgtBnkMapping("true".equalsIgnoreCase(Utility
				.getPropertyValue("AGENT_BANK_MAPPING")));
		String countryDep = ((String) LMSUtility.sc
				.getAttribute("COUNTRY_DEPLOYED")).trim();
		double maxPerDayPayLimit = Double.parseDouble(((String) LMSUtility.sc
				.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_AGENT")));

		if (!"GHANA".equalsIgnoreCase(countryDep)) {
			System.out.println("Inside country check while LMC registration");
			if (userName.matches(".*[0-9].*")) {
				errorMap = new HashMap<String, String>();
				this.errorMap = errorMap;
				errorMap.put("userError",
						getText("msg.use.alphabet.in.user.name"));
				return INPUT;
			}
		}

		// set data to agentRegistrationBean
		AgentRegistrationBean agentRegistrationBean = new AgentRegistrationBean();
		agentRegistrationBean.setAddrLine1(addrLine1);
		agentRegistrationBean.setAddrLine2(addrLine2);
		agentRegistrationBean.setAutoScrap(autoScrap);
		agentRegistrationBean.setCity(city);
		agentRegistrationBean.setCountry(country);
		agentRegistrationBean.setState(state);
		agentRegistrationBean.setCreditLimit(creditLimit);
		agentRegistrationBean.setEmail(email);
		agentRegistrationBean.setEmailPrivId(emailPrivId);
		agentRegistrationBean.setFirstName(firstName);
		agentRegistrationBean.setIdNo(idNo);
		agentRegistrationBean.setIdType(idType);
		agentRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_AGT"));
		agentRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_AGT"));
		agentRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_AGT")));
		agentRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_AGT")));
		agentRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
		agentRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
		agentRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
		agentRegistrationBean.setLastName(lastName);
		agentRegistrationBean.setOrgName(orgName);
		agentRegistrationBean.setOrgType(orgType);
		agentRegistrationBean.setPhone(phone == 0 ? 0 : Long.parseLong(cityCode
				.concat(String.valueOf(phone))));
		agentRegistrationBean.setMobile(Long.parseLong(code.concat(String
				.valueOf(mobile))));
		agentRegistrationBean.setSecAns(secAns);
		agentRegistrationBean.setSecQues(secQues);
		agentRegistrationBean.setUserName(userName);
		agentRegistrationBean.setPin(pin);
		agentRegistrationBean.setSecurity(security);
		agentRegistrationBean.setVerLimit(verLimit);
		agentRegistrationBean.setPayLimit(payLimit);
		agentRegistrationBean.setVatRegNo(vatRegNo);
		agentRegistrationBean.setStatusorg(statusorg);
		agentRegistrationBean.setReconReportType(reconReportType);
		agentRegistrationBean.setAppLimit(appLimit);
		agentRegistrationBean.setScrapLimit(scrapLimit);
		agentRegistrationBean.setStatus(status);
		agentRegistrationBean.setStatusTable(statusTable);
		agentRegistrationBean.setId(id);

		agentRegistrationBean.setArea(area);
		agentRegistrationBean.setDivision(division);
		agentRegistrationBean.setMaxPerDayPayLimit(maxPerDayPayLimit);

		// if("true".equalsIgnoreCase(Utility.getPropertyValue("AGENT_BANK_MAPPING").trim())){
		if ("true".equalsIgnoreCase(Utility
				.getPropertyValue("AGENT_BANK_MAPPING"))) {
			agentRegistrationBean.setAccountNbr(accountNbr);
			agentRegistrationBean.setBankId(bankId);
			agentRegistrationBean.setBranchId(branchId);
		}

		System.out.println("emailPrivId::" + emailPrivId + "::id" + id);
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		errorMap = helper.createNewAgentOrgNUser(userInfoBean,
				agentRegistrationBean, "BO");

		if (!errorMap.containsKey("returnTypeError")) {
			session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			session.setAttribute("USER_NAME", this.getUserName());
			session.setAttribute("ORG_CODE", errorMap.get("orgCode"));
			return SUCCESS;
		} else {
			this.errorMap = errorMap;
			logger.error("Organization and USER Name Already Exists!!");
			logger.debug("Organization and USER Name Already Exists!!");
			return INPUT;
		}

	}

	// this method is used to create the BO Users
	public String createUser() throws Exception {

		HttpSession session = getRequest().getSession();

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String returnType = "ERROR";
		CreateOrgUserHelper helper = new CreateOrgUserHelper();
		// OrganizationAction
		// orgData=(OrganizationAction)session.getAttribute("org");

		// Map privTitleMap = ((Map)session.getAttribute("reportTypeTitleMap"));
		// if(privTitleMap == null) privTitleMap = new TreeMap();

		// if orgData is null mean we are registering a BO User otherwise we are
		// register new AGENT/RETAILER
		// if(orgData==null) {
		returnType = helper.createBoUser(userInfoBean, userName, status,
				secQues, secAns, firstName, lastName, email, phone, idType,
				idNo, role, emailPrivId, request.getRemoteAddr());
		session.setAttribute("USER_NAME", getUserName());
		// }else {
		// returnType = helper.createNewOrgUser(userInfoBean, orgData, userName,
		// status, secQues, secAns, firstName
		// , lastName, email, phone, idType, idNo, role, autoScrap, emailPrivId,
		// privTitleMap.keySet());
		// session.setAttribute("ORGANIZATION_NAME", orgData.getOrgName());
		// }

		// commented by arun
		/*
		 * AgentUserHelper orgreg = new AgentUserHelper(); try {
		 * returntype=orgreg.createorg(orgList, getOrgName(), getUserName(),
		 * getStatus(), getSecQues(), getSecAns() , getFirstName(),
		 * getLastName(), getEmail(), getPhone(), roleName, getRole(),
		 * userOrgId, loggedinUserId); }catch(LMSException le) { return
		 * APPLICATION_ERROR; }
		 */

		if (returnType.equals("ERROR")) {
			addActionError(getText("msg.usr.already.exist")+"!!");
			logger.error("error return");
			logger.debug("error return");
			return ERROR;
		} else if (returnType.equals("INPUT")) {
			addActionError(getText("msg.bo.user.already.exists") + "!!");
			logger.error("error return");
			logger.debug("error return");
			return INPUT;
		} else if (returnType.equals("MASSUCCESS")) {
			return "MASSUCCESS";
		} else if (returnType.equals("SUCCESS")) {
			return SUCCESS;
		}

		addActionError(getText("msg.enter.user.name") + "!!");
		return ERROR;

	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public String getAppLimit() {
		return appLimit;
	}

	public String getAutoScrap() {
		return autoScrap;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public String getEmail() {
		return email;
	}

	public String[] getEmailPrivId() {
		return emailPrivId;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public String getFirstName() {
		return firstName;
	}

	public int[] getId() {
		return id;
	}

	public String getIdNo() {
		return idNo;
	}

	public String getIdType() {
		return idType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getPayLimit() {
		return payLimit;
	}

	public long getPhone() {
		return phone;
	}

	public long getPin() {
		return pin;
	}

	public int getPntId() {
		return pntId;
	}

	public String getReconReportType() {
		return reconReportType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRole() {
		return role;
	}

	public String getScrapLimit() {
		return scrapLimit;
	}

	public String getSecAns() {
		return secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public double getSecurity() {
		return security;
	}

	public String getState() {
		return state;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusorg() {
		return statusorg;
	}

	public String[] getStatusTable() {
		return statusTable;
	}

	public String getUserName() {
		return userName;
	}

	public String getVatRegNo() {
		return vatRegNo;
	}

	public String getVerLimit() {
		return verLimit;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public void setAppLimit(String appLimit) {
		this.appLimit = appLimit;
	}

	public void setAutoScrap(String autoScrap) {
		this.autoScrap = autoScrap;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailPrivId(String[] emailPrivId) {
		this.emailPrivId = emailPrivId;
	}

	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(int[] id) {
		this.id = id;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPayLimit(String payLimit) {
		this.payLimit = payLimit;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public void setPin(long pin) {
		this.pin = pin;
	}

	public void setPntId(int pntId) {
		this.pntId = pntId;
	}

	public void setReconReportType(String reconReportType) {
		this.reconReportType = reconReportType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setScrapLimit(String scrapLimit) {
		this.scrapLimit = scrapLimit;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
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

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusorg(String statusorg) {
		this.statusorg = statusorg;
	}

	public void setStatusTable(String[] statusTable) {
		this.statusTable = statusTable;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}

	public void setVerLimit(String verLimit) {
		this.verLimit = verLimit;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getAccountNbr() {
		return accountNbr;
	}

	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}

	public boolean isAgtBnkMapping() {
		return agtBnkMapping;
	}

	public void setAgtBnkMapping(boolean agtBnkMapping) {
		this.agtBnkMapping = agtBnkMapping;
	}
}