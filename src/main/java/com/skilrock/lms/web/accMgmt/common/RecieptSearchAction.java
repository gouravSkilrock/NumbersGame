package com.skilrock.lms.web.accMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import com.skilrock.lms.ajax.AjaxRequestHelper;

import com.skilrock.lms.beans.RecieptDetail;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;

import com.skilrock.lms.coreEngine.accMgmt.common.RecieptSearchActionHelper;
import com.skilrock.lms.web.common.drawables.CommonMethods;

/**
 * 
 * @author Arun Upadhyay
 * 
 */

public class RecieptSearchAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentOrgId;
	private String edit = null;
	private String end = null;
	private String endDate;
	private String receiptstatus;
	private String recieptnumber;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String retOrgId;

	private String searchResultsAvailable;

	private int start = 0;

	private String startDate;

	private String usertype;
	
	private String VStartDate;
	private String TStartDate;
	private String VEndDate;
	private String TEndDate;
	
	
	public String getAgent() throws IOException, LMSException  {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if(uib != null){
			userOrgId = uib.getUserOrgId();
		}
	
		String agtOrgList = new AjaxRequestHelper()
				.getOrgIdList(userOrgId,"AGENT");
		/*String html = "&nbsp;&nbsp;";
		html = "<select class=\"option\" name=\"agentOrgId\"  onchange = \"getRetailerList('bo_rep_searchReceipt_FetchRetailer.action','retlist',this.value)\"><option value=''>--PleaseSelect--</option>";

		for (OrgBean orgBean : characters) {
			html += "<option class=\"option\" value=\"" + orgBean.getOrgId()
					+ "\">" + orgBean.getOrgName() + "</option>";
		}

		html += "</select>";*/
		response.setContentType("text/html");
		out.print(agtOrgList);
		return null;
	}

	public String getAgentOrgId() {
		return agentOrgId;
	}

	public String getEdit() {
		return edit;
	}

	public String getEnd() {
		return end;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getReceiptstatus() {
		return receiptstatus;
	}

	public String getRecieptnumber() {
		return recieptnumber;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	// private int agentOrgId;
	public String getRetailerList() throws IOException {
		PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if ("AGENT".equalsIgnoreCase(infoBean.getUserType().trim())) {
			agentOrgId = Integer.toString(infoBean.getUserOrgId());
			System.out
					.println("agent side search voucher called  and agent_org_id is = "
							+ agentOrgId);
		}
	
		
		String  retOrgList = new AjaxRequestHelper()
				.getUserIdList(Integer.parseInt(agentOrgId.trim()));
		/*String html = "&nbsp;&nbsp;";
		html = "<select class=\"option\" name=\"retOrgId\" ><option value='-1'>--PleaseSelect--</option>";
		for (OrgBean orgBean : characters) {
			html += "<option class=\"option\" value=\"" + orgBean.getOrgId()
					+ "\">" + orgBean.getOrgName() + "</option>";
		}
		html += "</select>";*/
		response.setContentType("text/html");
		out.print(retOrgList);
		return null;
	}

	public String getRetOrgId() {
		return retOrgId;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getUsertype() {
		return usertype;
	}
	

	/**
	 * This method handles the pagination(first,next,previous and last click) in
	 * the searched results.
	 * 
	 * @return String
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = request.getSession();
		List ajaxList = (List) session.getAttribute("APP_ORDER_LIST5");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			System.out.println("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 20;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 20;
				if (startValue < 20) {
					startValue = 0;
				}
				endValue = startValue + 20;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 20;
				endValue = startValue + 20;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 20;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 20;

			}
			if (startValue == endValue) {
				startValue = endValue - 20;
			}
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("boreceiptsearch", ajaxSearchList);
			System.out.println("result in search ajax == "
					+ ajaxSearchList.size());
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		System.out.println("value of Edit" + edit);
		return SUCCESS;

	}

	public String searchForAgent() throws LMSException {
		List<RecieptDetail> receiptList = null;
		StringBuilder archivingDate = new StringBuilder();
		System.out.println("receipt no : " + recieptnumber
				+ ",  receipt status : " + receiptstatus + ",  user type : "
				+ usertype + ",  agent orgId : " + agentOrgId + " , Voucher startDate = "
				+ VStartDate + " , Voucher endDate = " + VEndDate + " , Transaction startDate = "
				+ TStartDate + " , Transaction endDate = " + TEndDate);
		if (validateIsDataArchived(TStartDate,VStartDate,archivingDate)) {
			addActionError("Select date after archive date");
			return INPUT;
		}
		
		HttpSession session = request.getSession();
		RecieptSearchActionHelper actionHelper = new RecieptSearchActionHelper();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		String query = "";
		if ("GOVT".equalsIgnoreCase(usertype.trim())
				|| "PLAYER".equalsIgnoreCase(usertype.trim())) {
			query = actionHelper.getReceiptSeacrhQueryForGovPLR(recieptnumber,
					receiptstatus, usertype, Integer.toString(infoBean
							.getUserOrgId()), "AGENT", VStartDate,
							VEndDate, TStartDate, TEndDate);
			receiptList = actionHelper.getRecieptListForGovPLR(query,"AGENT");
			receiptList = receiptList.size() > 0 ? receiptList : null;
		} else {
			query = actionHelper.getReceiptSeacrhQueryForAgtRet(recieptnumber,
					receiptstatus, "RETAILER", Integer.toString(infoBean
							.getUserOrgId()), retOrgId, VStartDate,
							VEndDate, TStartDate,	TEndDate);
			receiptList = actionHelper.getRecieptList(query,usertype);
			receiptList = receiptList.size() > 0 ? receiptList : null;
		}
		// String
		// query=actionHelper.getReceiptSeacrhQueryForAgtRet(recieptnumber,
		// receiptstatus, "RETAILER",
		// Integer.toString(infoBean.getUserOrgId()));
		// receiptList=actionHelper.getRecieptList(query).size()>0?
		// actionHelper.getRecieptList(query): null;

		session.setAttribute("boreceiptsearch", receiptList);

		System.out.println("session values in Action ==== "
				+ session.getAttribute("boreceiptsearch"));
		if (receiptList != null) {
			session.setAttribute("APP_ORDER_LIST5", receiptList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();

		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
		}

		return SUCCESS;
	}

	public String searchForBO() throws LMSException {
		StringBuilder archivingDate = new StringBuilder();
		List<RecieptDetail> receiptList = new ArrayList<RecieptDetail>();
		RecieptSearchActionHelper actionHelper = new RecieptSearchActionHelper();
		System.out.println("receipt no : " + recieptnumber
				+ ",  receipt status : " + receiptstatus + ",  user type : "
				+ usertype + ",  agent orgId : " + agentOrgId + " , Voucher startDate = "
				+ VStartDate + " , Voucher endDate = " + VEndDate + " , Transaction startDate = "
				+ TStartDate + " , Transaction endDate = " + TEndDate);

		if (validateIsDataArchived(TStartDate,VStartDate,archivingDate)) {
			addActionError("Select date after archive date");
			return INPUT;
		}
			
		
		// create query for search the receipts
		String query = "";
		if ("GOVT".equalsIgnoreCase(usertype.trim())	
				|| "PLAYER".equalsIgnoreCase(usertype.trim())) {
			query = actionHelper.getReceiptSeacrhQueryForGovPLR(recieptnumber,
					receiptstatus, usertype, agentOrgId, "BO", VStartDate,
					VEndDate, TStartDate, TEndDate);
			receiptList = actionHelper.getRecieptListForGovPLR(query,"BO");
			receiptList = receiptList.size() > 0 ? receiptList : null;
		} else if ("RETAILER".equalsIgnoreCase(usertype)
				|| "AGENT".equalsIgnoreCase(usertype)) {
			if ("AGENT".equalsIgnoreCase(usertype)) {
			if("DLNOTE".equalsIgnoreCase(receiptstatus)||"".equalsIgnoreCase(receiptstatus)){
			query = actionHelper.getDlNoteSeacrhQueryForAgtRet(recieptnumber,
					receiptstatus, usertype, agentOrgId, retOrgId, VStartDate,
					VEndDate, TStartDate, TEndDate);
			receiptList = actionHelper.getRecieptDNList(query);
			}
			}
			if(!"DLNOTE".equalsIgnoreCase(receiptstatus)){
				query = actionHelper.getReceiptSeacrhQueryForAgtRet(recieptnumber,
						receiptstatus, usertype, agentOrgId, retOrgId, VStartDate,
						VEndDate, TStartDate, TEndDate);
				receiptList.addAll(actionHelper.getRecieptList(query,usertype)) ;
			}
			receiptList = receiptList.size() > 0 ? receiptList : null;
		}else{
			String vNum = recieptnumber;
			String vType = vNum.substring(2,vNum.length());
			if(vType.equals("GOB") || vType.equals("REB")){
				query = actionHelper.getReceiptSeacrhQueryForGovPLR(recieptnumber,
						receiptstatus, usertype, agentOrgId, "BO", VStartDate,
						VEndDate, TStartDate, TEndDate);
				receiptList = actionHelper.getRecieptListForGovPLR(query,"BO");
				receiptList = receiptList.size() > 0 ? receiptList : null;
			}else{
				query = actionHelper.getReceiptSeacrhQueryForAgtRet(recieptnumber,
						receiptstatus, usertype, agentOrgId, retOrgId, VStartDate,
						VEndDate, TStartDate, TEndDate);
				receiptList = actionHelper.getRecieptList(query,usertype);
				receiptList = receiptList.size() > 0 ? receiptList : null;
			}
		}
		// return the search receipt list or null

		HttpSession session = request.getSession();

		if ("RETAILER".equalsIgnoreCase(usertype.trim())) {
			session.setAttribute("usertype", "Retailer");
			session.setAttribute("agentOrgIdSearch", agentOrgId);
		} else {
			session.setAttribute("usertype", null);
		}

		session.setAttribute("boreceiptsearch", receiptList);
		System.out.println("session values in Action ==== "
				+ session.getAttribute("boreceiptsearch"));

		if (receiptList != null) {
			session.setAttribute("APP_ORDER_LIST5", receiptList);
			// session attribute used for pagination.
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			searchAjax();

		} else {
			// / session attribute used for pagination.
			session.setAttribute("SearchResultsAvailable", "No");
			session.setAttribute("APP_ORDER_LIST5", new ArrayList(0));
		}
       
		return SUCCESS;
	}
  /** checks whether the voucher start date and transaction start date 
   *  should not be less than the archiving date.
   * 
   * @param txnStartDate
   * @param voucherStartDate
   * @return {@code true} if both the given dates are less than the archiving date
   *         else {@code false} if any of the date is greater than the archiving date. 
   * @author Mayank
   */
	
	private boolean validateIsDataArchived(String txnStartDate, String voucherStartDate,StringBuilder archivingDate) {
		if (txnStartDate != null && !txnStartDate.trim().isEmpty()) {
			return CommonMethods.isArchData(txnStartDate,archivingDate);
		}
		if (voucherStartDate != null && !voucherStartDate.trim().isEmpty()) {
			return CommonMethods.isArchData(voucherStartDate,archivingDate);
		}
		return false;
	}

	public void setAgentOrgId(String agentOrgId) {
		if ("".equals(agentOrgId.trim())) {
			this.agentOrgId = null;
		} else {
			this.agentOrgId = agentOrgId;
		}
		System.out.println("Agent org id --" + agentOrgId + "---");
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEndDate(String endDate) {
		System.out.println("end date called" + endDate);
		if (endDate != null) {
			this.endDate = GetDate.getSqlToUtilFormatStr(endDate);
		} else {
			this.endDate = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setReceiptstatus(String receiptstatus) {
		if ("All".equalsIgnoreCase(receiptstatus)) {
			this.receiptstatus = "";
		} else if ("Sales Return Note".equalsIgnoreCase(receiptstatus)) {
			this.receiptstatus = "DSRCHALLAN";
		} else {
			this.receiptstatus = receiptstatus.trim();
		}

	}

	public void setRecieptnumber(String recieptnumber) {
		if (recieptnumber == null) {
			recieptnumber = "";
		}
		this.recieptnumber = recieptnumber;
	}

	public void setRetOrgId(String retOrgId) {
		if ("-1".equals(retOrgId.trim())) {
			this.retOrgId = null;
		} else {
			this.retOrgId = retOrgId;
		}
		System.out.println("Agent org id --" + this.retOrgId + "---");

	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
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

	public void setStartDate(String startDate) {
		System.out.println("first date called" + startDate);
		if (startDate != null) {
			this.startDate = GetDate.getSqlToUtilFormatStr(startDate);
		} else {
			this.startDate = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype.trim();
	}

	public String getVStartDate() {
		return VStartDate;
	}

	public void setVStartDate(String vStartDate) {
		System.out.println("first date called" + vStartDate);
		if (vStartDate != null && ! "".equalsIgnoreCase(vStartDate)) {
			this.VStartDate = GetDate.getSqlToUtilFormatStr(vStartDate);
		} else {
			this.VStartDate = "";
		}
	}

	public String getTStartDate() {
		return TStartDate;
	}

	public void setTStartDate(String tStartDate) {
		System.out.println("first date called" + tStartDate);
		if (tStartDate != null && ! "".equalsIgnoreCase(tStartDate)) {
			this.TStartDate = GetDate.getSqlToUtilFormatStr(tStartDate);
		} else {
			this.TStartDate = "";
		}
	}

	public String getVEndDate() {
		return VEndDate;
	}

	public void setVEndDate(String vEndDate) {
		System.out.println("Voucher end date called" + vEndDate);
		if (vEndDate != null && ! "".equalsIgnoreCase(vEndDate)) {
			this.VEndDate = GetDate.getSqlToUtilFormatStr(vEndDate);
		} else {
			this.VEndDate = "";
		}
	}

	public String getTEndDate() {
		return TEndDate;
	}

	public void setTEndDate(String tEndDate) {
		System.out.println("Transaction end date called" + tEndDate);
		if (tEndDate != null && ! "".equalsIgnoreCase(tEndDate)) {
			this.TEndDate = GetDate.getSqlToUtilFormatStr(tEndDate);
		} else {
			this.TEndDate = "";
		}
	}
	
}
