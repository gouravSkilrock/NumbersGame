package com.skilrock.lms.web.bankMgmt.actions;


import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.web.bankMgmt.Helpers.BankHelper;
import com.skilrock.lms.web.bankMgmt.beans.BankDetailsBean;
import com.skilrock.lms.web.bankMgmt.beans.BankRepDataBean;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;
import com.skilrock.lms.web.bankReports.BankRepHelper;



public class BankAction extends ActionSupport implements ServletRequestAware,
ServletResponseAware{
	/**
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(BankAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private BankDetailsBean bankDetailsBean ;
	private BranchDetailsBean branchDetailsBean ;
	private String bankName;
	private int branchId;
	private int userId;
	private int bankId;
	private int agtOrgId;
	private String accountNbr;
	private String delType;//Details Type whether for bank or branch
	private String regStartDate;
	private String regEndDate;
	private String currDate;
	private List<BankRepDataBean> bankRepDataBeanList;
	public String execute () {
		Calendar cal = Calendar.getInstance();
		setCurrDate(CommonMethods.convertDateInGlobalFormat(new java.sql.Date(cal
						.getTimeInMillis()).toString(), "yyyy-mm-dd",
						"yyyy-mm-dd"));
	
		return SUCCESS;
		
	
	}
	public String createBank(){
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		BankHelper helper = new BankHelper();
		Calendar cal = Calendar.getInstance();
		Timestamp creationTime = new Timestamp(cal.getTimeInMillis());
		boolean isCreated = helper.createBank(bankDetailsBean,userBean,creationTime);
		if(isCreated){
			return SUCCESS;

		}
		addActionError("Error In Bank Creation Please Check Details");
		return ERROR;

	}
	public String createBranch(){
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		BankHelper helper = new BankHelper();
		Calendar cal = Calendar.getInstance();
		Timestamp creationTime = new Timestamp(cal.getTimeInMillis());
		boolean isCreated = helper.createBranch(branchDetailsBean, userBean.getUserId(), creationTime);
		if(isCreated){
			return SUCCESS;

		}
		addActionError("Error In Branch Creation Please Check Details");
		return ERROR;

	}
	
	public String assignBranch(){
		BankHelper helper = new BankHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		Calendar cal = Calendar.getInstance();
		Timestamp mapTime = new Timestamp(cal.getTimeInMillis());
		boolean isMapped = helper.assignBranch(branchId,userId,userBean,mapTime);
		if(isMapped){
			return SUCCESS;

		}
		addActionError("Error In Branch Mapping");
		return ERROR;
		
	}
	
	
	public void checkBankNameAvailability(){
		logger.info("Check Availability for bank  "+bankName);
		PrintWriter out=null;
		String isAvail="Invalid Input";
		try {
			out = getResponse().getWriter();
			if(bankName!=null){
				isAvail = new BankHelper().checkBankNameAvailability(bankName);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(" bank  name is "+isAvail);
		response.setContentType("text/html");
		out.print(isAvail);
	
		
	}
	public void fetchUserList(){
		PrintWriter out=null;
		String userList="";
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		try {
			out = getResponse().getWriter();
			if(userBean!=null){
				userList = new BankHelper().fetchUserList(userBean.getRoleId());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("userList "+userList);
		response.setContentType("text/html");
		out.print(userList);
	
		
	}
	public void fetchBankList(){
		PrintWriter out = null;
		String bankList = null;
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		try {
			
			out = getResponse().getWriter();
			bankList = new BankHelper().fetchBankList(userBean.getRoleId());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("bankList - "+bankList);
		response.setContentType("text/html");
		out.print(bankList);
		out.flush();
		out.close();
	}
	public void fetchBranchList(){
		PrintWriter out=null;
		String branchList="";
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		try {
			out = getResponse().getWriter();
		
				if(userBean.getRoleId()==1 || userBean.getIsRoleHeadUser().equalsIgnoreCase("Y")){
					if(bankId>0){
						branchList = new BankHelper().fetchBranchListForBank(bankId); // Branch List For a Bank
					}else{
						branchList = new BankHelper().fetchBranchList(userBean.getRoleId());// Branch List For Role Head
					}
					
				}else{
					branchList= new BankHelper().fetchUserBranchDetails(userBean.getUserId());
				}
		
			 
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("branchList "+branchList);
		response.setContentType("text/html");
		out.print(branchList);
	
		
	}
	public void fetchRoleList(){
		PrintWriter out=null;
		String roleList="";
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		try {
			out = getResponse().getWriter();
			if(userBean!=null){
				roleList = new BankHelper().fetchRoleList();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("roleList "+roleList);
		response.setContentType("text/html");
		out.print(roleList);
	
		
	}
	public void fetchUserBranchDetails(){
		PrintWriter out=null;
		String brachDteails="";
	
		try {
			
			out = getResponse().getWriter();
			brachDteails = new BankHelper().fetchUserBranchDetails(userId);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("brachDteails "+brachDteails);
		response.setContentType("text/html");
		out.print(brachDteails);
	
		
	}
	public void fetchAgentBankList(){
		PrintWriter out = null;
		String bankList = null;
	
		try {
			
			out = getResponse().getWriter();
			bankList = new BankHelper().fetchAgentBankList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("bankList - "+bankList);
		response.setContentType("text/html");
		out.print(bankList);
		out.flush();
		out.close();
	}
	
	public void fetchAgentBranchList(){
		PrintWriter out=null;
		String branchList="";
		
		try {
			out = getResponse().getWriter();
		
			branchList = new BankHelper().fetchAgentBranchListForBank(bankId); 
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("branchList "+branchList);
		response.setContentType("text/html");
		out.print(branchList);
	
		
	}
	public String saveDetails(){
		
		BankHelper helper = new BankHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		Calendar cal = Calendar.getInstance();
		Timestamp mapTime = new Timestamp(cal.getTimeInMillis());
		boolean isUpdated = helper.saveDetails(bankId,branchId,agtOrgId,accountNbr,userBean,mapTime);
		if(isUpdated){
			return SUCCESS;
		}
		addActionError("Error In Saving Details (Duplicate Account Number Insertion) ");
		return ERROR;
		
	}
	public void fetchAgentBankDetails(){
		PrintWriter out=null;
		String agtBankDetails="";
	
		try {
			
			out = getResponse().getWriter();
			agtBankDetails = new BankHelper().fetchAgentBankDetails(agtOrgId);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("agtbrachDteails "+agtBankDetails);
		response.setContentType("text/html");
		out.print(agtBankDetails);
	}
	public String fetchBankandBranchDetails(){
		
			BankHelper	 helper = new BankHelper();
			UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
			setBankRepDataBeanList(helper.fetchBankandBranchDetails(delType,bankId,branchId,regStartDate,regEndDate,userBean.getRoleId()));
			
			return SUCCESS;
			
		
		
	}
	public String fetchCashierDetails(){
		
		BankHelper	 helper = new BankHelper();
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		setBankRepDataBeanList(helper.fetchCashierDetails(bankId,branchId,regStartDate,regEndDate,userBean.getRoleId()));
		
		return SUCCESS;
		
	
	
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public BankDetailsBean getBankDetailsBean() {
		return bankDetailsBean;
	}

	public void setBankDetailsBean(BankDetailsBean bankDetailsBean) {
		this.bankDetailsBean = bankDetailsBean;
	}
	public BranchDetailsBean getBranchDetailsBean() {
		return branchDetailsBean;
	}
	public void setBranchDetailsBean(BranchDetailsBean branchDetailsBean) {
		this.branchDetailsBean = branchDetailsBean;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getBranchId() {
		return branchId;
	}
	public int getUserId() {
		return userId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public int getAgtOrgId() {
		return agtOrgId;
	}
	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}
	public String getAccountNbr() {
		return accountNbr;
	}
	public void setAccountNbr(String accountNbr) {
		this.accountNbr = accountNbr;
	}
	public String getDelType() {
		return delType;
	}
	public void setDelType(String delType) {
		this.delType = delType;
	}
	public String getRegStartDate() {
		return regStartDate;
	}
	public void setRegStartDate(String regStartDate) {
		this.regStartDate = regStartDate;
	}
	public String getRegEndDate() {
		return regEndDate;
	}
	public void setRegEndDate(String regEndDate) {
		this.regEndDate = regEndDate;
	}
	public String getCurrDate() {
		return currDate;
	}
	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}
	public List<BankRepDataBean> getBankRepDataBeanList() {
		return bankRepDataBeanList;
	}
	public void setBankRepDataBeanList(List<BankRepDataBean> bankRepDataBeanList) {
		this.bankRepDataBeanList = bankRepDataBeanList;
	}
}
