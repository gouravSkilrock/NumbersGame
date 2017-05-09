package com.skilrock.lms.web.userMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.IPaymentLedgerReportAgtWiseHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PaymentLedgerReportAgtWiseHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PaymentLedgerReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetDailyLedgerHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationManagementHelper;
import com.skilrock.lms.embedded.reportsMgmt.common.LedgerAction;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.WriteExcelForCashChq;

/**
 * This class provides method for get Organization details and to edit
 * Organization details
 * 
 * @author Skilrock Technologies
 * 
 */
public class OrganizationManagementAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	
	private static Log logger = LogFactory.getLog(OrganizationManagementAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orgAddr1 = "";

	private String orgAddr2 = "";
	private OrganizationBean orgBean;

	private String orgCity = "";
	private double orgCreditLimit;
	private int orgId = 2;
	private long orgPin;
	private double orgSecurityDeposit;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String scrapStatus;
	private String statusNew = "";
	private String comment="";
	private String area;
	private String ifuCode;
	private String prevIfu;
	private String division ;
	private String orgType ;
	private String toTerminate ;
	public String getComment() {
		return comment;
	}

	public String getPrevIfu() {
		return prevIfu;
	}

	public void setPrevIfu(String prevIfu) {
		this.prevIfu = prevIfu;
	}

	public String getIfuCode() {
		return ifuCode;
	}


	public void setIfuCode(String ifuCode) {
		this.ifuCode = ifuCode;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	
	
	public String getToTerminate() {
		return toTerminate;
	}

	public void setToTerminate(String toTerminate) {
		this.toTerminate = toTerminate;
	}

	/**
	 * This method is used to edit organization details
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String editOrgDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		OrganizationBean bean = (OrganizationBean) session
				.getAttribute("ORG_SEARCH_RESULTS");
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		if (statusNew == null) {
			statusNew = "INACTIVE";
		}
		if ("BLOCK".equalsIgnoreCase(statusNew) || "TERMINATE".equalsIgnoreCase(statusNew)) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");

			List<String> userList = CommonFunctionsHelper.getUsersList(bean
					.getOrgId());
			for (int i = 0; i < userList.size(); i++) {
				currentUserSessionMap.remove(userList.get(i));
			}
		}

		// edit the organization
		OrganizationManagementHelper editOrgDetail = new OrganizationManagementHelper();
		int doneByUserId = ((UserInfoBean)session.getAttribute("USER_INFO")).getUserId();
		String requestIp = request.getRemoteAddr();
		String returnStatus = editOrgDetail.editOrgDetails(
				userInfo.getUserId(), bean, getOrgAddr1(), getOrgAddr2(),
				getOrgCity(), getOrgPin(), getStatusNew(),
				getOrgSecurityDeposit(), getOrgCreditLimit(), scrapStatus, comment,getArea(),getDivision(), doneByUserId, requestIp,ifuCode,prevIfu);

		if("error".equalsIgnoreCase(returnStatus)){
			addActionError(getText("label.ifu.already.exist"));
			return ERROR;
		}
		session.removeAttribute("ORG_SEARCH_RESULTS");
		session.setAttribute("ORG_NAME", bean.getOrgName());
		session.setAttribute("RETURN_STATUS", returnStatus);
		return SUCCESS;

	}

	public String exportExcel() {
		ArrayList<CashChqReportBean> data = new ArrayList<CashChqReportBean>();
		HttpSession session = getRequest().getSession();
		data = (ArrayList<CashChqReportBean>) session
				.getAttribute("searchCashChqResult");

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=Cash Cheque Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WriteExcelForCashChq excel = new WriteExcelForCashChq(
					(DateBeans) request.getSession().getAttribute("datebean"));
			if (session.getAttribute("agentcashchq") == null) {
				excel.write(data, w, "", "");
			} else {
				CashChqReportBean agentBoDetail = (CashChqReportBean) session
						.getAttribute("agentbocashchqdetail");
				excel.writeAgent(data, agentBoDetail, w);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String extendCrLimitOrgDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		OrganizationManagementHelper orgDetail = new OrganizationManagementHelper();
		OrganizationBean bean = orgDetail.orgDetails(orgId);
		session.setAttribute("ORG_SEARCH_RESULTS", bean);
		return SUCCESS;

	}

	public String getOrgAddr1() {
		return orgAddr1;
	}

	public String getOrgAddr2() {
		return orgAddr2;
	}

	public OrganizationBean getOrgBean() {
		return orgBean;
	}

	public String getOrgCity() {
		return orgCity;
	}

	public double getOrgCreditLimit() {
		return orgCreditLimit;
	}

	public int getOrgId() {
		return orgId;
	}

	public long getOrgPin() {
		return orgPin;
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

	public String getStatusNew() {
		return statusNew;
	}

	/**
	 * This method is used to get the organization details
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String orgDetails() throws Exception {

		HttpSession session = request.getSession();
		session.setAttribute("ORG_SEARCH_RESULTS", null);
		OrganizationManagementHelper orgDetail = new OrganizationManagementHelper();
		UserInfoBean userBean = new UserInfoBean() ;
		userBean.setUserOrgId(orgId) ;
	//	AjaxRequestHelper helper = new AjaxRequestHelper() ;
		OrganizationManagementHelper helper = new OrganizationManagementHelper() ;
		OrganizationBean bean = orgDetail.orgDetails(orgId);
		 bean.setTerminalCount(orgDetail.fetchTerminalCount(orgId));
		 toTerminate = (String) ServletActionContext
			.getServletContext().getAttribute("TERMINATE_USER") ;
		 logger.info("User Type : " + orgType) ; 
		 // start
		
		 if("RETAILER".equalsIgnoreCase(orgType))
		 {
				
			bean.setOutstandingBal(FormatNumber.formatNumberForJSP(orgDetail.getRetOutstandingBal(orgId, request, session, AjaxRequestHelper.getAgentOrgIdByRetailerOrgId(orgId)))) ;
		 }
		 else
		 {
			 bean.setOutstandingBal(FormatNumber.formatNumberForJSP(helper.getAgentOutstandingBal(orgId, request, session))) ;
		 }
		 
		 // end
		 
		 
		 
		 
		 
		 
		 
//		 bean.setOutstandingBal(helper.getOutstandingBalance(userBean)) ;
		session.setAttribute("ORG_SEARCH_RESULTS", bean);
		session.setAttribute("CITY_LIST", new CommonFunctionsHelper()
				.getCityNameList(bean.getOrgCountry(), bean.getOrgState()));
		session.setAttribute("TO_TERMINATE", toTerminate) ;
		
		return SUCCESS;

	}
	
	

	public void setOrgAddr1(String orgAddr1) {
		this.orgAddr1 = orgAddr1;
	}

	public void setOrgAddr2(String orgAddr2) {
		this.orgAddr2 = orgAddr2;
	}

	public void setOrgBean(OrganizationBean orgBean) {
		this.orgBean = orgBean;
		System.out.println(" =============== " + orgBean);
	}

	public void setOrgCity(String orgCity) {
		this.orgCity = orgCity;
	}

	public void setOrgCreditLimit(double orgCreditLimit) {
		this.orgCreditLimit = orgCreditLimit;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgPin(long orgPin) {
		this.orgPin = orgPin;
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

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setStatusNew(String statusNew) {
		this.statusNew = statusNew;
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

}