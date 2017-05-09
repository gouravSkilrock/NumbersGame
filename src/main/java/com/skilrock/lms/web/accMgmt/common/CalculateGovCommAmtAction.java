package com.skilrock.lms.web.accMgmt.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GovCommBean;
import com.skilrock.lms.beans.TaskBean;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.accMgmt.common.CalculateGovCommAmtHelper;

public class CalculateGovCommAmtAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] aprovedGameId;
	private long[] taskIds;
	private String endDate;
	private HttpServletRequest request;
	private String serviceName;

	private String startDate;
	String start_date;
	private String type;
	String end_Date;
	String commissionType;

	public long[] getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(long[] taskIds) {
		this.taskIds = taskIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String approveGovComm() {
		CalculateGovCommAmtHelper helper = new CalculateGovCommAmtHelper();
		if (taskIds != null && taskIds.length > 0)
			helper.approveGovCommDetails(taskIds, "APPROVED");

		return SUCCESS;
	}

	public String calculateGovCommAmt() throws Exception {
		CalculateGovCommAmtHelper helper = new CalculateGovCommAmtHelper();
		Map<Integer, GovCommBean> map = null;
		if (this.serviceName.equalsIgnoreCase("SE")) {
			map = helper.getGovCommAmtDetailToApprove(endDate);
		} else if (this.serviceName.equalsIgnoreCase("DG") || this.serviceName.equalsIgnoreCase("SLE") || this.serviceName.equalsIgnoreCase("IW")) {
			SimpleDateFormat s1 = new SimpleDateFormat((String) request.getSession().getAttribute("date_format"));
			SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
			String depDate = (String) ServletActionContext.getServletContext().getAttribute("DEPLOYMENT_DATE");
			Timestamp startTime = new Timestamp(s1.parse(depDate).getTime());
			Timestamp endTime = new Timestamp(s2.parse(endDate).getTime() + 24 * 60 * 60 * 1000);
			map = helper.getCommissionDetailsToApprove(startTime, endTime, serviceName, type);
		}
		HttpSession session = request.getSession();
		session.setAttribute("boTaskApproveList", map);
		session.setAttribute("serviceCode", serviceName);
		session.setAttribute("boTaskApproveTotalAmt", FormatNumber.formatNumber(helper.getTotalGovCommAmt()));
		return SUCCESS;
	}

	@Override
	public String execute() {
		HttpSession session = request.getSession();
		CalculateGovCommAmtHelper helper = new CalculateGovCommAmtHelper();
		// this method return yesterday date
		endDate = helper.getYesterdayDate();
		try {
			session.setAttribute("GOV_CUR_DATE", new java.sql.Date(
					new java.util.Date().getTime()).toString());
			session.setAttribute("GOV_END_DATE", endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> serviceNameMap = helper.getServiceList();
		Map<String, String> commissionTypeMap = helper.getcommissionTypeList();
		session.setAttribute("commissionTypeMap", commissionTypeMap);
		session.setAttribute("serviceNameMap", serviceNameMap);
		return SUCCESS;

	}

	public int[] getAprovedGameId() {
		return aprovedGameId;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public String getcommissionType() {
		return commissionType;
	}

	public void setcommissionType(String commissionType) {
		this.commissionType = commissionType;
	}
	public String searchGovtCommission() throws Exception {
		HttpSession session = request.getSession();
		session.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS", null);
		CalculateGovCommAmtHelper BoTaskHelper = new CalculateGovCommAmtHelper();
		List<TaskBean> searchResults = BoTaskHelper.taskGovtCommSearch(serviceName, type);
		session.setAttribute("serviceCode", serviceName);
		if (searchResults != null && searchResults.size() > 0)
			session.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS", searchResults);

		return SUCCESS;
	}
	public String searchGovtAndVatCommission() throws Exception {
		System.out.println("in searcg govt and vat");
		HttpSession session = request.getSession();
		session.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS", null);
		String startDate = start_date;
		String endDate = end_Date;
		CalculateGovCommAmtHelper BoTaskHelper = new CalculateGovCommAmtHelper();
		List<TaskBean> searchResults = BoTaskHelper
				.taskGovtCommAndVatCommSearch(serviceName,startDate,endDate,commissionType);
		session.setAttribute("serviceCode", serviceName);
		session.setAttribute("commissionType", commissionType);
		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session
					.setAttribute("TASK_Govt_Comm_SEARCH_RESULTS",
							searchResults);
			// setUsersearchResultsAvailable("Yes");
		} else {
			// setUsersearchResultsAvailable("No");
			System.out.println("No:---Search result Processed");
		}
		return SUCCESS;
	}

	public void setAprovedGameId(int[] aprovedGameId) {
		this.aprovedGameId = aprovedGameId;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}