package com.skilrock.lms.web.userMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.RetailerRegistrationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;

public class RetailerOrgUserRegAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addrLine1;

	private String addrLine2;
	private int agtOrgId;
	private String appLimit;
	private String autoScrap;
	private String city;
	private String country;
	private double creditLimit;
	private String email;
	private String emailPrivId[];
	private Map<String, String> errorMap;
	// user details
	private String firstName;
	private int id[];
	private String idNo;
	private String idType;
	private String isOffLine;

	private String lastName;
	static Log logger = LogFactory.getLog(RetailerOrgUserRegAction.class);
	// organization details
	private String orgName;
	private String orgType;
	private String payLimit;
	private long phone;
	private String cityCode;
	private long mobile;
	private String code;
	private long pin;
	private int pntId;
	private String reconReportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String role;

	private String scrapLimit;
	private String secAns;
	private String secQues;
	private double security;

	private String state;
	private String status;

	private String statusorg;
	private String[] statusTable;

	private String olaDepositLimit;
	private String olaWithdrawalLimit;

	private String terminalId;
	private String userName;
	private String vatRegNo;
	// private String isCreditSale;

	private String verLimit;
	private String modelName;
	private String vat_applicable;
	private String area;
	private String division;
	private String sim[];
	private String simModelName[];

	// verify for organization and user name is duplicate or Not
	public void checkAvlOrgUser() throws LMSException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			OrgNUserRegHelper helper = new OrgNUserRegHelper();
			Map<String, String> errorMap = helper.verifyOrgName(getOrgName(),
					getUserName(), con);
			PrintWriter pw = response.getWriter();
			pw.print(errorMap.toString().replace("{", "").replace("}", ""));
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String createQuickRetailerOrg() throws LMSException {

		// Register Retailer Organization and user info details
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		/*
		 * //now this code is commented because of we are not assigning the
		 * reporting to retailers OrganizationHelper orgReg = new
		 * OrganizationHelper(); Map<Integer, String> privTitleMap =
		 * orgReg.getMailingReportTitle("AGENT");
		 */
		String errMsg = CommonMethods.chkCreditLimitAgt(userInfoBean
				.getUserOrgId(), creditLimit);
		if (!"TRUE".equals(errMsg)) {
			errorMap = new HashMap<String, String>();
			errorMap.put("orgError", errMsg);
			this.errorMap = errorMap;
			return INPUT;
		}
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		String generatedName = "";
		if ((firstName + "-" + lastName + "-" + idNo).length() <= 30) {
			generatedName = firstName + "-" + lastName + "-" + idNo;
		} else if ((firstName + "-" + idNo).length() <= 30) {
			generatedName = firstName + "-" + idNo;
		} else {
			String subStr = firstName.substring(0, 13);
			generatedName = subStr + "-" + idNo;
		}
		setOrgName(generatedName);
		setUserName(generatedName);
		Map<String, String> errorMap = helper.createNewQuickRetailerOrgNUser(
				userInfoBean, this, new TreeMap().keySet());

		if (!errorMap.containsKey("returnTypeError")) {
			session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			session.setAttribute("USER_NAME", this.getUserName());
			session.setAttribute("RETAILER_PASSWORD", errorMap
					.get("NewPassword"));
			return SUCCESS;
		} else {
			this.errorMap = errorMap;
			logger.error("Organization and USER Name Already Exists!!");
			System.out.println("Organization and USER Name Already Exists!!");
			return INPUT;
		}

	}

	public String createRetailerOrg() throws LMSException {

		// Register Retailer Organization and user info details
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		double maxPerDayPayLimit = Double.parseDouble(((String) LMSUtility.sc
				.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_RET")));

		// set data to retailerRegistrationBean
		RetailerRegistrationBean retailerRegistrationBean = new RetailerRegistrationBean();
		retailerRegistrationBean.setAddrLine1(addrLine1);
		retailerRegistrationBean.setAddrLine2(addrLine2);
		retailerRegistrationBean.setAgtOrgId(agtOrgId);
		retailerRegistrationBean.setAppLimit(appLimit);
		retailerRegistrationBean.setAutoScrap(autoScrap);
		retailerRegistrationBean.setCity(city);
		retailerRegistrationBean.setCountry(country);
		retailerRegistrationBean.setCreditLimit(creditLimit);
		retailerRegistrationBean.setEmail(email);
		retailerRegistrationBean.setEmailPrivId(emailPrivId);
		retailerRegistrationBean.setFirstName(firstName);
		retailerRegistrationBean.setId(id);
		retailerRegistrationBean.setIdNo(idNo);
		retailerRegistrationBean.setIdType(idType);
		retailerRegistrationBean.setIsOffLine(isOffLine);
		retailerRegistrationBean.setLastName(lastName);
		retailerRegistrationBean.setModelName(modelName);
		retailerRegistrationBean.setOlaDepositLimit(olaDepositLimit);
		retailerRegistrationBean.setOlaWithdrawalLimit(olaWithdrawalLimit);
		retailerRegistrationBean.setOrgName(orgName);
		retailerRegistrationBean.setOrgType(orgType);
		retailerRegistrationBean.setPayLimit(payLimit);
		retailerRegistrationBean.setPhone(phone == 0 ? phone : Long
				.parseLong(cityCode.concat(String.valueOf(phone))));
		retailerRegistrationBean.setMobile(Long.parseLong(code.concat(String
				.valueOf(mobile))));
		retailerRegistrationBean.setPin(pin);
		retailerRegistrationBean.setPntId(pntId);
		retailerRegistrationBean.setReconReportType(reconReportType);
		retailerRegistrationBean.setScrapLimit(scrapLimit);
		retailerRegistrationBean.setSecAns(secAns);
		retailerRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_RET"));
		retailerRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_RET"));
		retailerRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
		retailerRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
		retailerRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
		retailerRegistrationBean.setSecQues(secQues);
		retailerRegistrationBean.setSecurity(security);
		retailerRegistrationBean.setState(state);
		retailerRegistrationBean.setStatus(status);
		retailerRegistrationBean.setStatusorg(statusorg);
		retailerRegistrationBean.setStatusTable(statusTable);
		retailerRegistrationBean.setTerminalId(terminalId);
		retailerRegistrationBean.setUserName(userName);
		retailerRegistrationBean.setVatRegNo(vatRegNo);
		retailerRegistrationBean.setVerLimit(verLimit);
		retailerRegistrationBean.setIsRetailerOnline((String) LMSUtility.sc
				.getAttribute("RET_ONLINE"));

		retailerRegistrationBean.setArea(area);
		retailerRegistrationBean.setDivision(division);
		retailerRegistrationBean.setMaxPerDayPayLimit(maxPerDayPayLimit);
		if ("YES".equalsIgnoreCase(Utility.getPropertyValue("sim_binding"))) {

			if (isValidSim(sim, simModelName)) {
				retailerRegistrationBean.setSim(sim);
				retailerRegistrationBean.setSimModelName(simModelName);
			} else {
				addActionError(getText("msg.invaid.sim.inp"));
				logger.debug("Invalid Sim Input");
				return INPUT;
			}

		}
		retailerRegistrationBean.setRegisterById(userInfoBean.getUserId());

		Map<String, String> errorMap = null;
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		logger.info("---------------Check BAL");
		System.out.println("---------------Check BAL");
		String errMsg = CommonMethods.chkCreditLimitAgt(userInfoBean
				.getUserOrgId(), creditLimit);

		if (!"TRUE".equals(errMsg)) {
			errorMap = new HashMap<String, String>();
			errorMap.put("orgError", errMsg);
			this.errorMap = errorMap;
			return INPUT;
		}

		errorMap = helper.createNewRetailerOrgNUser(userInfoBean,
				retailerRegistrationBean, userInfoBean.getUserType());

		if (!errorMap.containsKey("returnTypeError")) {
			if (terminalId != null) {
				helper.assignInventoryToRetailer(userInfoBean,
						retailerRegistrationBean);
			}

			session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			session.setAttribute("USER_NAME", this.getUserName().toLowerCase());
			session.setAttribute("RETAILER_PASSWORD", errorMap
					.get("NewPassword"));
			session.setAttribute("ORG_CODE", errorMap.get("orgCode"));
			return SUCCESS;
		} else {
			this.errorMap = errorMap;
			logger.error("Organization and USER Name Already Exists!!");
			System.out.println("Organization and USER Name Already Exists!!");
			return INPUT;
		}

	}

	public String createRetailerOrgAtBO() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		vat_applicable = (String) sc.getAttribute("VAT_APPLICABLE");
		System.out.println("---------------createRetailerOrgAtBO" + agtOrgId);
		// Register Retailer Organization and user info details
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
		UserInfoBean agtInfoBean = null;
		double maxPerDayPayLimit = Double.parseDouble(((String) LMSUtility.sc
				.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_RET")));
		String blockAction = Utility.getPropertyValue("BLOCK_ACTION");
		double blockAmt = Double.parseDouble(((String) LMSUtility.sc
				.getAttribute("BLOCK_AMT")));
		int blockDays = Integer.parseInt((String)LMSUtility.sc
				.getAttribute("BLOCK_DAYS"));
		// set data to retailerRegistrationBean
		RetailerRegistrationBean retailerRegistrationBean = new RetailerRegistrationBean();
		retailerRegistrationBean.setAddrLine1(addrLine1);
		retailerRegistrationBean.setAddrLine2(addrLine2);
		retailerRegistrationBean.setAgtOrgId(agtOrgId);
		retailerRegistrationBean.setAppLimit(appLimit);
		retailerRegistrationBean.setAutoScrap(autoScrap);
		retailerRegistrationBean.setCity(city);
		retailerRegistrationBean.setCountry(country);
		retailerRegistrationBean.setCreditLimit(creditLimit);
		retailerRegistrationBean.setEmail(email);
		retailerRegistrationBean.setEmailPrivId(emailPrivId);
		retailerRegistrationBean.setFirstName(firstName);
		retailerRegistrationBean.setId(id);
		retailerRegistrationBean.setIdNo(idNo);
		retailerRegistrationBean.setIdType(idType);
		retailerRegistrationBean.setIsOffLine(isOffLine);
		retailerRegistrationBean.setLastName(lastName);
		retailerRegistrationBean.setModelName(modelName);
		retailerRegistrationBean.setOlaDepositLimit(olaDepositLimit);
		retailerRegistrationBean.setOlaWithdrawalLimit(olaWithdrawalLimit);
		retailerRegistrationBean.setOrgName(orgName);
		retailerRegistrationBean.setOrgType(orgType);
		retailerRegistrationBean.setPayLimit(payLimit);
		retailerRegistrationBean.setPhone(phone == 0 ? phone : Long
				.parseLong(cityCode.concat(String.valueOf(phone))));
		retailerRegistrationBean.setMobile(Long.parseLong(code.concat(String
				.valueOf(mobile))));
		retailerRegistrationBean.setPin(pin);
		retailerRegistrationBean.setPntId(pntId);
		retailerRegistrationBean.setReconReportType(reconReportType);
		retailerRegistrationBean.setScrapLimit(scrapLimit);
		retailerRegistrationBean.setSecAns(secAns);
		retailerRegistrationBean.setSecQues(secQues);
		retailerRegistrationBean.setSecurity(security);
		retailerRegistrationBean.setState(state);
		retailerRegistrationBean.setStatus(status);
		retailerRegistrationBean.setStatusorg(statusorg);
		retailerRegistrationBean.setStatusTable(statusTable);
		retailerRegistrationBean.setTerminalId(terminalId);
		retailerRegistrationBean.setUserName(userName);
		retailerRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_RET"));
		retailerRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_RET"));
		retailerRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
		retailerRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
		retailerRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
		retailerRegistrationBean.setVatRegNo(vatRegNo);
		retailerRegistrationBean.setVerLimit(verLimit);
		retailerRegistrationBean.setIsRetailerOnline((String) LMSUtility.sc
				.getAttribute("RET_ONLINE"));
		retailerRegistrationBean.setMaxPerDayPayLimit(maxPerDayPayLimit);
		retailerRegistrationBean.setArea(area);
		retailerRegistrationBean.setDivision(division);
		if ("YES".equalsIgnoreCase(Utility.getPropertyValue("sim_binding"))) {

			if (isValidSim(sim, simModelName)) {
				retailerRegistrationBean.setSim(sim);
				retailerRegistrationBean.setSimModelName(simModelName);
			} else {
				addActionError(getText("msg.invaid.sim.inp"));
				logger.debug("Invalid Sim Input");
				return INPUT;
			}

		}
		retailerRegistrationBean.setRegisterById(userInfoBean.getUserId());

		Map<String, String> errorMap = null;
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		logger.info("---------------Check BAL");
		System.out.println("---------------Check BAL");
		String errMsg = CommonMethods.chkCreditLimitAgt(agtOrgId, creditLimit);

		if (!"TRUE".equals(errMsg)) {
			errorMap = new HashMap<String, String>();
			errorMap.put("orgError", errMsg);
			this.errorMap = errorMap;
			return INPUT;
		}

		// create agent info bean
		agtInfoBean = helper.createAgtBean(agtOrgId);
		System.out
				.println(verLimit + "*******verLimit***" + this.getVerLimit());
		errorMap = helper.createNewRetailerOrgNUser(agtInfoBean,
				retailerRegistrationBean, "BO");
		System.out.println("*****errorMap" + errorMap);
		if (!errorMap.containsKey("returnTypeError")) {
			if (terminalId != null) {
				helper.assignInventoryToRetailer(agtInfoBean,
						retailerRegistrationBean);
			}

			session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			session.setAttribute("USER_NAME", this.getUserName().toLowerCase());
			session.setAttribute("RETAILER_PASSWORD", errorMap
					.get("NewPassword"));
			session.setAttribute("ORG_CODE", errorMap.get("orgCode"));
			return SUCCESS;
		} else {
			this.errorMap = errorMap;
			logger.error("Organization and USER Name Already Exists!!");
			System.out.println("Organization and USER Name Already Exists!!");
			return INPUT;
		}

	}

	public boolean isValidSim(String sim[], String simModelName[]) {
		try {
			Set<String> modelSet = new HashSet<String>();
			boolean oneSelected = false;
			if (simModelName != null) {

				for (String simModel : simModelName) {

					if (simModel != null
							&& !"-1".equalsIgnoreCase(simModel.trim())) {
						if (!modelSet.add(simModel)) {
							logger
									.info("Cannot Assign Multiple Sim of Same Model !!");
							return false;
						} else {

							oneSelected = true;
						}
					}

				}
				if (!oneSelected) {

					logger.info("Please Select Atleast One Sim");
					return false;
				}

			} else {

				return false;
			}
			oneSelected = false;
			if (sim != null) {

				for (String simSrNo : sim) {

					if (simSrNo != null && !"".equalsIgnoreCase(simSrNo.trim())) {
						oneSelected = true;
					}

				}

				if (!oneSelected) {

					logger
							.info("Please provide Atleast One Sim Serial Number ");
					return false;
				}
			} else {

				return false;
			}

			return true;

		} catch (Exception e) {

			logger.error("Exception invalid Sim Input", e);

		}

		return false;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public int getAgtOrgId() {
		return agtOrgId;
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

	public String getIsOffLine() {
		return isOffLine;
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

	public String getTerminalId() {
		return terminalId;
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

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
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

	public void setIsOffLine(String isOffLine) {
		this.isOffLine = isOffLine;
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

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
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

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

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

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getOlaDepositLimit() {
		return olaDepositLimit;
	}

	public void setOlaDepositLimit(String olaDepositLimit) {
		this.olaDepositLimit = olaDepositLimit;
	}

	public String getOlaWithdrawalLimit() {
		return olaWithdrawalLimit;
	}

	public void setOlaWithdrawalLimit(String olaWithdrawalLimit) {
		this.olaWithdrawalLimit = olaWithdrawalLimit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getVat_applicable() {
		return vat_applicable;
	}

	public void setVat_applicable(String vatApplicable) {
		vat_applicable = vatApplicable;
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

	public String[] getSim() {
		return sim;
	}

	public void setSim(String[] sim) {
		this.sim = sim;
	}

	public String[] getSimModelName() {
		return simModelName;
	}

	public void setSimModelName(String[] simModelName) {
		this.simModelName = simModelName;
	}

}
