package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.SearchOrgHelper;

public class UpdateUserLedgerBalance extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Log logger = LogFactory.getLog(UpdateUserLedgerBalance.class);

	private int orgId;
	private String orgType;
	private int parentOrgId;
	private HttpServletResponse res;
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getOrgId() {
		return orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public int getParentOrgId() {
		return parentOrgId;
	}

	public void getUpdatedLedgerBalance() {
		PrintWriter out = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		logger.debug("orgType = " + orgType + "   parentOrgId" + parentOrgId
				+ "  orgId = " + orgId);
		logger.debug("orgType = " + orgType + "   parentOrgId" + parentOrgId
				+ "  orgId = " + orgId);
		LedgerHelper ledgerHelper = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		double ledger=0.0;
		try {
			out = res.getWriter();
			if ("AGENT".equalsIgnoreCase(orgType.trim())) {
				ledgerHelper = new LedgerHelper();
				ledgerHelper
						.ledgerBoEntry(new Timestamp(cal.getTimeInMillis()));
			} else {
				ledgerHelper = new LedgerHelper();
				ledgerHelper.ledgerAgentEntry(new Timestamp(cal
						.getTimeInMillis()), parentOrgId);
			}
			logger
					.info("============ ledger balance updation completed ======== ");
			System.out
					.println("============ ledger balance updation completed ======== ");

			// get the last updated ledger balance of AGENT/RETAILER
			connection = DBConnect.getConnection();
			if ("AGENT".equalsIgnoreCase(orgType.trim())) {
				pstmt = connection
						.prepareStatement("select *  from st_lms_bo_current_balance where agent_org_id = ?");
			} else {
				pstmt = connection
						.prepareStatement("select *  from st_lms_agent_current_balance where account_type = ?");
			}
			pstmt.setInt(1, orgId);
			ResultSet rsLedger = pstmt.executeQuery();
			String ledgerBal = "0";
			if (rsLedger.next()) {
			ledger = rsLedger.getDouble("current_balance");
				if (ledger != 0) {
					ledgerBal = FormatNumber.formatNumberForJSP(ledger);
				} else {
					ledgerBal = String.valueOf(ledger);
				}
			}
			// @ ledger balance in export as excel
			System.out
					.println("code for updating ledger balance in excel is executed...");
			System.out
			.println("code for updating ledger balance in excel is executed..."+request);
			ArrayList<OrganizationBean> orgDtlList = new ArrayList<OrganizationBean>();
			HttpSession session = getRequest().getSession();
			orgDtlList = (ArrayList<OrganizationBean>) session
					.getAttribute("ORG_SEARCH_RESULTS1");
			for (OrganizationBean bean : orgDtlList) {
				if(bean.getOrgId()== orgId){
					bean.setLedgerBalance(ledger);
				}
			}
			session.setAttribute("ORG_SEARCH_RESULTS1", orgDtlList);
			

			// ends
			out.print(ledgerBal);
			logger.debug("ledgerBal === " + ledgerBal);
			logger.debug("ledgerBal === " + ledgerBal);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.res = res;
	}

}
