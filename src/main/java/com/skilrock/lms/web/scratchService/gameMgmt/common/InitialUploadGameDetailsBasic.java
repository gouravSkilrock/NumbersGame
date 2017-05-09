package com.skilrock.lms.web.scratchService.gameMgmt.common;

/***
 *  * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 * 
 */
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This class is used to read properties from Application context.
 * 
 * @author Skilrock Technologies
 * 
 */
public class InitialUploadGameDetailsBasic extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(InitialUploadGameDetailsBasic.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String agent_pwt_comm_rate = null;
	String agent_sale_comm_rate = null;
	String govt_pwt_comm_rate = null;
	// String govtCommRate = null;
	// String dateFormate = "dd-MM-yyyy";
	String govtCommRule = null;
	String income_tax = null;
	String retailer_pwt_comm_rate = null;
	String retailer_sale_comm_rate = null;
	private HttpServletRequest servletRequest = null;
	private HttpSession session = null;
	String vat_applicable = null;

	// private String uploadinventoryHidden = null;

	public String getAgent_pwt_comm_rate() {
		return agent_pwt_comm_rate;
	}

	public String getAgent_sale_comm_rate() {
		return agent_sale_comm_rate;
	}

	public String getGovtCommRule() {
		return govtCommRule;
	}

	public String getIncome_tax() {
		return income_tax;
	}

	public HttpServletRequest getRequest() {
		return servletRequest;
	}

	public String getRetailer_pwt_comm_rate() {
		return retailer_pwt_comm_rate;
	}

	public String getRetailer_sale_comm_rate() {
		return retailer_sale_comm_rate;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getVat_applicable() {
		return vat_applicable;
	}

	/**
	 * This method is used to read properties from Application context.
	 * 
	 * @return SUCCESS
	 */
	public String initialUploadBasicDetails() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		agent_sale_comm_rate = (String) sc.getAttribute("AGT_SALE_COMM_RATE");
		retailer_sale_comm_rate = (String) sc
				.getAttribute("RET_SALE_COMM_RATE");
		agent_pwt_comm_rate = (String) sc.getAttribute("AGT_PWT_COMM_RATE");
		retailer_pwt_comm_rate = (String) sc.getAttribute("RET_PWT_COMM_RATE");
		govtCommRule = (String) sc.getAttribute("GOVT_COMM_RULE");
		vat_applicable = (String) sc.getAttribute("VAT_APPLICABLE");
		session = getRequest().getSession();

		// this.setAgent_sale_comm_rate(agent_sale_comm_rate);
		session.setAttribute("x", this);
		return SUCCESS;

	}

	public void setAgent_pwt_comm_rate(String agent_pwt_comm_rate) {
		this.agent_pwt_comm_rate = agent_pwt_comm_rate;
	}

	public void setAgent_sale_comm_rate(String agent_sale_comm_rate) {
		this.agent_sale_comm_rate = agent_sale_comm_rate;
	}

	public void setGovtCommRule(String govtCommRule) {
		this.govtCommRule = govtCommRule;
	}

	public void setIncome_tax(String income_tax) {
		this.income_tax = income_tax;
	}

	public void setRequest(HttpServletRequest request) {
		this.servletRequest = request;
	}

	public void setRetailer_pwt_comm_rate(String retailer_pwt_comm_rate) {
		this.retailer_pwt_comm_rate = retailer_pwt_comm_rate;
	}

	public void setRetailer_sale_comm_rate(String retailer_sale_comm_rate) {
		this.retailer_sale_comm_rate = retailer_sale_comm_rate;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setVat_applicable(String vat_applicable) {
		this.vat_applicable = vat_applicable;
	}

}