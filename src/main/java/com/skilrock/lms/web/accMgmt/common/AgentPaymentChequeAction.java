package com.skilrock.lms.web.accMgmt.common;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ChequePaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentPaymentChequeHelper;

public class AgentPaymentChequeAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankName = null;
	private double chequeAmount;
	private Date chequeDate;

	private String chequeNumber;
	private String issuePartyname;
	private String orgName = null;
	private List<String> orgNameList = new ArrayList();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<Integer,String> orgNameMap= new LinkedHashMap<Integer,String>();

	private int sNo;
	private String agentNameValue;


	/**
	 * This method is used to generate temp receipt for temp cheque deposit
	 * 
	 * @return
	 */
	public String agentChequePaymentSubmit() throws LMSException {

		HttpSession session = null;
		List<ChequePaymentBean> paymetList = null;
		session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		paymetList = (List) session.getAttribute("CHEQUE_PAYMENT_LIST");
		// System.out.println("cheque date in action is:: "
		// +paymetList.get(0).getChequeDate());
		AgentPaymentChequeHelper chequeHelper = new AgentPaymentChequeHelper();
		chequeHelper.submitChequePaymentTemp(paymetList, userBean.getOrgName(),
				userBean.getUserOrgId(), rootPath);

		return SUCCESS;
	}

	public String Cheque() throws LMSException {
		HttpSession session = null;
		session = getRequest().getSession();
		
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		//orgNameList = chequeHelper.getOrganizations();
		// System.out.println(orgNameList.size() + orgNameList.get(0));
		 //setOrgNameList(orgNameList);
		
		setOrgNameMap(CommonMethods.getOrgInfoMap(userBean.getUserOrgId(),"AGENT"));
		return SUCCESS;
	}

	public String ChequeProcess() throws LMSException {

		HttpSession session = null;
		session = getRequest().getSession();
		List<ChequePaymentBean> paymetList = null;
		System.out.println("in cheqye process date is  " + chequeDate);
		System.out.println("cheque process cheque date is  " + getChequeDate());
		AgentPaymentChequeHelper chequeHelper = new AgentPaymentChequeHelper();

		if (chequeHelper.validateCheque(bankName, chequeNumber)) {

			addActionError("Cheque Number: " + chequeNumber + " from bank "
					+ bankName + " has already been submitted");
			return ERROR;
		}

		ChequePaymentBean chequePaymentBean = new ChequePaymentBean();

		chequePaymentBean.setAmount(chequeAmount);
		chequePaymentBean.setChequeNo(chequeNumber);
		chequePaymentBean.setBankName(bankName);
		chequePaymentBean.setChequeDate(chequeDate);
		chequePaymentBean.setIssuePartyname(issuePartyname);
		chequePaymentBean.setOrgName(getOrgName());

		if (session.getAttribute("ORG_NAME") == null) {
			session.setAttribute("ORG_NAME", orgName);

		}
		if (session.getAttribute("AGT_NAME_VALUE") == null) {
			session.setAttribute("AGT_NAME_VALUE", agentNameValue);

		}

		if (session.getAttribute("COUNT") == null) {
			session.setAttribute("COUNT", new Integer(1));
			chequePaymentBean.setSNo(new Integer(1));
		} else {

			int count = ((Integer) session.getAttribute("COUNT")).intValue();
			count = count + 1;
			session.setAttribute("COUNT", new Integer(count));
			chequePaymentBean.setSNo(new Integer(count));
		}

		paymetList = (List) session.getAttribute("CHEQUE_PAYMENT_LIST");
		if (paymetList != null) {
			paymetList.add(chequePaymentBean);
		} else {
			paymetList = new ArrayList<ChequePaymentBean>();
			paymetList.add(chequePaymentBean);

		}
		session.setAttribute("CHEQUE_PAYMENT_LIST", paymetList);
		System.out.println("before success " + paymetList.get(0).getChequeNo()
				+ "and size is :: " + paymetList.size()
				+ "and cheque date is  " + paymetList.get(0).getChequeDate());
		return SUCCESS;
	}

	public String getBankName() {
		return bankName;
	}

	public double getChequeAmount() {
		return chequeAmount;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public String getIssuePartyname() {
		return issuePartyname;
	}

	public String getOrgName() {
		return orgName;
	}

	public List<String> getOrgNameList() {
		return orgNameList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getSNo() {
		return sNo;
	}

	public String RemovePayEntity() throws LMSException {

		HttpSession session = null;
		List<ChequePaymentBean> paymetList = null;
		session = getRequest().getSession();
		ChequePaymentBean chequePaymentBean = null;

		paymetList = (List) session.getAttribute("CHEQUE_PAYMENT_LIST");
		if (paymetList != null) {
			for (int i = 0; i < paymetList.size(); i++) {

				chequePaymentBean = paymetList.get(i);

				if (chequePaymentBean.getSNo() == sNo
						&& chequePaymentBean.getChequeNo().equals(chequeNumber)) {
					System.out.println("--------------------removing  entry ");
					paymetList.remove(i);
					break;
				}
			}
			session.setAttribute("CHEQUE_PAYMENT_LIST", paymetList);
		}

		return SUCCESS;

	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setChequeAmount(double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public void setIssuePartyname(String issuePartyname) {
		this.issuePartyname = issuePartyname;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgNameList(List<String> orgNameList) {
		this.orgNameList = orgNameList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setSNo(int no) {
		sNo = no;
	}

	public String start() {
		HttpSession session = null;
		session = getRequest().getSession();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		java.util.Date currDate = cal.getTime();
		Long stLong = currDate.getTime() - 60L * 60L * 24 * 1000 * 30 * 6;
		Date startDate = new Date(stLong);
		String startDateString = dateFormat.format(startDate);
		String strCurrDate = dateFormat.format(currDate);

		session.setAttribute("CHEQUE_START_DATE", startDateString);
		session.setAttribute("CHEQUE_END_DATE", strCurrDate);

		session.setAttribute("CHEQUE_PAYMENT_LIST", null);
		session.setAttribute("COUNT", null);
		session.setAttribute("ORG_NAME", null);
		return SUCCESS;
	}

	public Map<Integer, String> getOrgNameMap() {
		return orgNameMap;
	}

	public void setOrgNameMap(Map<Integer, String> orgNameMap) {
		this.orgNameMap = orgNameMap;
	}

	public String getAgentNameValue() {
		return agentNameValue;
	}

	public void setAgentNameValue(String agentNameValue) {
		this.agentNameValue = agentNameValue;
	}
}