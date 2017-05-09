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
package com.skilrock.lms.web.userMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchOrgHelper;

/**
 * 
 * This class is used to process the Org search @ Agent.
 * @author SkilRock Technology
 * 
 */
public class ProcessSearchOrgForAgentAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String end = null;

	Log logger = LogFactory.getLog(ProcessSearchOrgForAgentAction.class);

	private String orgName = null;
	private String orgsearchResultsAvailable;
	private String orgStatus = null;
	private String orgType = null;
	private HttpServletRequest request;
	int start = 0;

	public String getEnd() {
		return end;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgsearchResultsAvailable() {
		return orgsearchResultsAvailable;
	}

	/*
	 * 
	 * This method is used to search Company(Org) @Agent @author SkilRock
	 * Technology @Param @Return String throws LMSException
	 */

	public String getOrgStatus() {
		return orgStatus;
	}

	public String getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getStart() {
		return start;
	}

	public String search() throws LMSException {
		HttpSession session = getRequest().getSession(true);
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		session.setAttribute("ORG_SEARCH_RESULTS1", null);
		logger.debug("hello i sm in search User");
		logger.debug("org Name:" + orgName);
		logger.debug("org Type:" + orgType);
		logger.debug("org Status:" + orgStatus);

		logger.debug("hello i sm in search User");
		logger.debug("org Name:" + orgName);
		logger.debug("org Type:" + orgType);
		logger.debug("org Status:" + orgStatus);

		UserInfoBean userInfo = null;
		// logger.debug(userInfo.getgetRoleName());
		userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
		int intagentId = userInfo.getUserOrgId();
		Integer ii = new Integer(intagentId);
		String agentId = ii.toString();
		logger.debug("User Id: " + userInfo.getUserId());
		logger.debug(">>>>>>>>" + userInfo.getUserId());
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("ORG_NAME", orgName);
		searchMap.put("ORG_TYPE", orgType);
		searchMap.put("ORG_STATUS", orgStatus);
		searchMap.put("AGENT_ID", agentId);
		if (orgStatus.equals("1")) {

			searchMap.put("ORG_STATUS", null);
		} else {
			searchMap.put("ORG_STATUS", orgStatus);
		}
		if (orgType.equals("1")) {

			searchMap.put("ORG_TYPE", null);
		} else {
			searchMap.put("ORG_TYPE", orgType);
		}

		SearchOrgHelper searchOrgHelper = new SearchOrgHelper();
		List searchResults = searchOrgHelper.searchOrgForRetailer(searchMap);
		logger.debug("Search Results : " + searchResults);
		logger.debug(searchResults);
		if (searchResults != null && searchResults.size() > 0) {
			logger.debug("Yes:---Search result Processed");
			logger.debug("Yes:---Search result Processed");
			session.setAttribute("ORG_SEARCH_RESULTS1", searchResults);
			logger.debug("List " + searchResults);
			session.setAttribute("startValueOrgAgSearch", new Integer(0));
			setOrgsearchResultsAvailable("Yes");
			searchAjax();
		} else {
			setOrgsearchResultsAvailable("No");
			logger.debug("No:---Search result Processed");
			logger.debug("No:---Search result Processed");
		}

		return SUCCESS;
	}

	/*
	 * 
	 * This method is used for pagination of Company(Org) search Results .
	 * @author SkilRock Technology @Parama @Return String throws LMSException
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("ORG_SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// logger.debug("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// logger.debug("end "+end);
			startValue = (Integer) session
					.getAttribute("startValueOrgAgSearch");
			if (end.equals("first")) {
				logger.debug("i m in first");
				startValue = 0;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				logger.debug("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				logger.debug("i m in Next");
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;
			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			// logger.debug("End value"+endValue);
			// logger.debug("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("ORG_SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrgAgSearch", startValue);
		}
		return SUCCESS;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgsearchResultsAvailable(String orgsearchResultsAvailable) {
		this.orgsearchResultsAvailable = orgsearchResultsAvailable;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
