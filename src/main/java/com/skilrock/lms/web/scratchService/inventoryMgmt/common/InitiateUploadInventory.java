package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.QueryHelper;

/**
 * This class is used to read properties from Application context.
 * 
 * @author Skilrock Technologies
 * 
 */
public class InitiateUploadInventory extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(InitiateUploadInventory.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String agent_pwt_comm_rate = null;
	String agent_sale_comm_rate = null;
	String dateFormate = "dd-MM-yyyy";
	String govt_pwt_comm_rate = null;
	String govtCommRate = null;
	String income_tax = null;
	String retailer_pwt_comm_rate = null;
	String retailer_sale_comm_rate = null;
	private HttpServletRequest servletRequest = null;
	private HttpSession session = null;
	private String uploadinventoryHidden = null;

	/**
	 * This method is used to read properties from Application context.
	 * 
	 * @return SUCCESS
	 */
	@Override
	public String execute() throws Exception {

		/*
		 * Properties properties = new Properties(); InputStream inputStream =
		 * this.getClass().getClassLoader()
		 * .getResourceAsStream("config/LMS.properties"); logger.debug(">>>>" +
		 * inputStream); properties.load(inputStream); agent_sale_comm_rate =
		 * properties .getProperty("agent_sale_comm_rate"); logger.debug("agent
		 * rate>>>" + agent_sale_comm_rate); retailer_sale_comm_rate =
		 * properties .getProperty("retailer_sale_comm_rate");
		 * agent_pwt_comm_rate = properties.getProperty("agent_pwt_comm_rate");
		 * retailer_pwt_comm_rate = properties
		 * .getProperty("retailer_pwt_comm_rate"); govt_pwt_comm_rate =
		 * properties .getProperty("retailer_pwt_comm_rate"); govtCommRate =
		 * properties.getProperty("govt_comm_rate");
		 */

		ServletContext sc = ServletActionContext.getServletContext();
		agent_sale_comm_rate = (String) sc.getAttribute("AGT_SALE_COMM_RATE");
		retailer_sale_comm_rate = (String) sc
				.getAttribute("RET_SALE_COMM_RATE");
		agent_pwt_comm_rate = (String) sc.getAttribute("AGT_PWT_COMM_RATE");
		retailer_pwt_comm_rate = (String) sc.getAttribute("RET_PWT_COMM_RATE");
		govtCommRate = (String) sc.getAttribute("GOVT_COMM_RATE");
		logger.debug(govtCommRate + "govtCommRate");
		session = getRequest().getSession();
		session.setAttribute("START_DATE", null);
		Date currDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String strCurrDate = dateFormat.format(currDate);

		logger.debug(strCurrDate + "dateeeeeeeee");
		session.setAttribute("START_DATE", strCurrDate);

		QueryHelper searchQuery = new QueryHelper();
		List searchResults = searchQuery.SearchSupplier();

		if (searchResults != null && searchResults.size() > 0) {
			logger.debug(searchResults);
			session.setAttribute("SUPPLIER_SEARCH_RESULTS", searchResults);

		} else {
			session.setAttribute("SUPPLIER_SEARCH_RESULTS", null);
		}
		this.setAgent_sale_comm_rate(agent_sale_comm_rate);
		session.setAttribute("x", this);
		return SUCCESS;

	}

	public String getAgent_pwt_comm_rate() {
		return agent_pwt_comm_rate;
	}

	public String getAgent_sale_comm_rate() {
		return agent_sale_comm_rate;
	}

	public String getDateFormate() {
		return dateFormate;
	}

	public String getGovtCommRate() {
		return govtCommRate;
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

	public String getUploadinventoryHidden() {
		return uploadinventoryHidden;
	}

	public void setAgent_pwt_comm_rate(String agent_pwt_comm_rate) {
		this.agent_pwt_comm_rate = agent_pwt_comm_rate;
	}

	public void setAgent_sale_comm_rate(String agent_sale_comm_rate) {
		this.agent_sale_comm_rate = agent_sale_comm_rate;
	}

	public void setDateFormate(String dateFormate) {
		this.dateFormate = dateFormate;
	}

	public void setGovtCommRate(String govtCommRate) {
		this.govtCommRate = govtCommRate;
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

	public void setUploadinventoryHidden(String uploadinventoryHidden) {
		this.uploadinventoryHidden = uploadinventoryHidden;
	}

}