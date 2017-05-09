/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 * 
 */
package com.skilrock.lms.web.userMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgDetailsExl;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchOrgHelper;

/**
 * 
 * This class is used to process the Org search @ BO.
 * @author Skilrock Technologies
 * 
 */
public class ProcessSearchOrgAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String avlblCrLimit = null;

	private String avlblCrLimitSign = null;
	private String CrLimit = null;

	private String CrLimitSign = null;
	private String end = null;
	private String extendCrLimit = null;
	private String extendCrLimitSign = null;
	Log logger = LogFactory.getLog(ProcessSearchOrgAction.class);
	private String orgName = null;
	private String orgsearchResultsAvailable;
	private String orgStatus = null;
	private String orgType = null;
	private String parentCompName = null;
	private String pwtScrapStatus = null;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String securityDeposit = null;
	private String securityDepositSign = null;
	private String terminalStatusType = null;

	int start = 0;

	String reportType = null;
	
	public String getTerminalStatusType() {
		return terminalStatusType;
	}

	public void setTerminalStatusType(String terminalStatusType) {
		this.terminalStatusType = terminalStatusType;
	}

	public String exportExcel() {
		ArrayList<OrganizationBean> orgDtlList = new ArrayList<OrganizationBean>();
		HttpSession session = getRequest().getSession();
		orgDtlList = (ArrayList<OrganizationBean>) session
				.getAttribute("ORG_SEARCH_RESULTS1");
		ProcessSearchOrgAction filterVal = (ProcessSearchOrgAction) session
				.getAttribute("ORG_SEARCH_RESULTS_FILTER");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=org_detail_list.xls");
			WritableWorkbook workbk = Workbook.createWorkbook(response
					.getOutputStream());
			OrgDetailsExl excel = new OrgDetailsExl();
			excel.createExlForOrgDetails(orgDtlList, workbk, filterVal);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAvlblCrLimit() {
		return avlblCrLimit;
	}

	public String getAvlblCrLimitSign() {
		return avlblCrLimitSign;
	}

	public String getCrLimit() {
		return CrLimit;
	}

	// ---------------------------Changed by Arun --------

	public String getCrLimitSign() {
		return CrLimitSign;
	}

	public String getEnd() {
		return end;
	}

	public String getExtendCrLimit() {
		return extendCrLimit;
	}

	public String getExtendCrLimitSign() {
		return extendCrLimitSign;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgsearchResultsAvailable() {
		return orgsearchResultsAvailable;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getParentCompName() {
		return parentCompName;
	}

	public String getPwtScrapStatus() {
		return pwtScrapStatus;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getSecurityDeposit() {
		return securityDeposit;
	}

	public String getSecurityDepositSign() {
		return securityDepositSign;
	}

	public int getStart() {
		return start;
	}

	/**
	 * This method is used to search Company(Org)
	 * 
	 * @BO
	 * @return SUCCESS
	 * @throws LMSException
	 */

	public String search() throws LMSException {
		HttpSession session = getRequest().getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		session.setAttribute("ORG_SEARCH_RESULTS1", null);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("ORG_NAME", orgName);
		searchMap.put("PARENT_ORG_NAME", parentCompName);
		// if Organization status is not selected
		searchMap.put("ORG_STATUS", orgStatus.equals("1") ? null : orgStatus);
		// if Organization Type is not selected
		searchMap.put("ORG_TYPE", orgType.equals("1") ? null : orgType);

		SearchOrgHelper searchOrgHelper = new SearchOrgHelper();
		List<OrganizationBean> searchResults = searchOrgHelper.searchOrg(
				orgName, orgType, orgStatus, parentCompName, CrLimitSign,
				extendCrLimitSign, avlblCrLimitSign, securityDepositSign,
				CrLimit, extendCrLimit, avlblCrLimit, securityDeposit,
				pwtScrapStatus, reportType, terminalStatusType);
		logger.debug("Org Search Results " + searchResults);
		logger.debug("Org Search Results " + searchResults);
		if (searchResults != null && searchResults.size() > 0) {
			logger.debug("Yes:---Search result Processed");
			logger.debug("Yes:---Search result Processed");
			session.setAttribute("ORG_SEARCH_RESULTS1", searchResults);
			session.setAttribute("ORG_SEARCH_RESULTS_FILTER", this);
			session.setAttribute("startValueOrgSearch", new Integer(0));
			setOrgsearchResultsAvailable("Yes");
		} else {
			setOrgsearchResultsAvailable("No");
			logger.debug("No:---Search result Processed");
			logger.debug("No:---Search result Processed");
		}

		searchAjax();

		return SUCCESS;
	}

	public String searchAgent() throws LMSException {
		HttpSession session = getRequest().getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		session.setAttribute("ORG_SEARCH_RESULTS1", null);
		logger.debug("hello i am in search Organization @Agent");
		logger.debug("org Name:" + orgName);
		logger.debug("org Type:" + orgType);
		logger.debug("org Status:" + orgStatus);
		logger.debug("hello i am in search Organization @Agent");
		logger.debug("org Name:" + orgName);
		logger.debug("org Type:" + orgType);
		logger.debug("org Status:" + orgStatus);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("ORG_NAME", orgName);
		searchMap.put("ORG_TYPE", orgType);
		searchMap.put("ORG_STATUS", orgStatus);
		// if org status is not selected
		if (orgStatus.equals("1")) {
			searchMap.put("ORG_STATUS", null);
		} else {
			searchMap.put("ORG_STATUS", orgStatus);
		}
		// if org type is not selected
		if (orgType.equals("1")) {
			searchMap.put("ORG_TYPE", null);
		} else {
			searchMap.put("ORG_TYPE", orgType);
		}

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int orgId = userInfoBean.getUserOrgId();
		SearchOrgHelper searchOrgHelper = new SearchOrgHelper();
		List<OrganizationBean> searchResults = searchOrgHelper
				.searchOrgRetailer(searchMap, orgId);

		logger.debug("Org Search Results " + searchResults);
		logger.debug("Org Search Results " + searchResults);
		if (searchResults != null && searchResults.size() > 0) {
			logger.debug("Yes:---Search result Processed");
			logger.debug("Yes:---Search result Processed");
			session.setAttribute("ORG_SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueOrgSearch", new Integer(0));
			setOrgsearchResultsAvailable("Yes");
		} else {
			setOrgsearchResultsAvailable("No");
			logger.debug("No:---Search result Processed");
			logger.debug("No:---Search result Processed");
		}

		searchAjax();

		return SUCCESS;
	}

	/**
	 * This method is used for pagination of Company(Org) search Results .
	 * 
	 * @return SUCCESS
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List<OrganizationBean> ajaxList = (List) session
				.getAttribute("ORG_SEARCH_RESULTS1");
		List<OrganizationBean> ajaxSearchList = new ArrayList();
		logger.debug("end " + ajaxList);
		logger.debug("end " + ajaxList);
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// logger.debug("end "+end);
			startValue = (Integer) session.getAttribute("startValueOrgSearch");
			if (end.equals("first")) {
				logger.debug("i m in first");
				startValue = 0;
				endValue = startValue + 10;

				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				logger.debug("i m in Previous");
				logger.debug("i m in Previous");
				startValue = startValue - 10;
				if (startValue < 10) {
					startValue = 0;
				}

				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				logger.debug("i m in Next");
				logger.debug("i m in Next");
				startValue = startValue + 10;
				endValue = startValue + 10;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 10;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 10;

			}
			if (startValue == endValue) {
				startValue = endValue - 10;
			}
			// logger.debug("End value"+endValue);
			// logger.debug("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("ORG_SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrgSearch", startValue);
		}

		return SUCCESS;
	}

	public void setAvlblCrLimit(String avlblCrLimit) {
		this.avlblCrLimit = avlblCrLimit;
	}

	public void setAvlblCrLimitSign(String avlblCrLimitSign) {
		this.avlblCrLimitSign = avlblCrLimitSign;
	}

	public void setCrLimit(String crLimit) {
		CrLimit = crLimit;
	}

	public void setCrLimitSign(String crLimitSign) {
		CrLimitSign = crLimitSign;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setExtendCrLimit(String extendCrLimit) {
		this.extendCrLimit = extendCrLimit;
	}

	public void setExtendCrLimitSign(String extendCrLimitSign) {
		this.extendCrLimitSign = extendCrLimitSign;
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

	public void setParentCompName(String parentCompName) {
		this.parentCompName = parentCompName;
	}

	public void setPwtScrapStatus(String pwtScrapStatus) {
		this.pwtScrapStatus = pwtScrapStatus;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSecurityDeposit(String securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public void setSecurityDepositSign(String securityDepositSign) {
		this.securityDepositSign = securityDepositSign;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}