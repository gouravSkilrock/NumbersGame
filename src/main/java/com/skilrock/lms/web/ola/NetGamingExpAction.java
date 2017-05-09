package com.skilrock.lms.web.ola;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.DateFormatConverter;
import com.skilrock.lms.coreEngine.ola.CreateTransactionForCommissionHelper;
import com.skilrock.lms.coreEngine.ola.NetGamingExpAgentHelper;

public class NetGamingExpAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log logger = LogFactory.getLog(NetGamingExpAction.class);

	private int[] orgId;
	private String walletName;
	private String date;
	private String netGamingExpType;
	private HttpServletRequest request;
	private HttpServletResponse response;
    private Date startDate;
    private Date endDate;
	public String fetchMenuData() throws LMSException, Exception {
		logger.debug("Net Gaming Training Expense fetchMenuData");
		int walletId = 0;
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			walletId = Integer.parseInt(walletArr[0]);
		}
		if(!"-1".equalsIgnoreCase(netGamingExpType)){
		NetGamingExpAgentHelper helper = new NetGamingExpAgentHelper();
		HttpSession session = request.getSession();
		String[] tempTimeStamp = fetchDate(date, netGamingExpType).split("Nxt");
		DateBeans dateBeans = new DateBeans();
		System.out.println(tempTimeStamp[0]);
		System.out.println(getStartDate());
		System.out.println(getEndDate());
		
		
		dateBeans.setReportType(netGamingExpType);
		dateBeans.setStrDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[0]));
		dateBeans.setEndDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[1]));
		session.setAttribute("NetGamingExpenseData", helper.fetchMenuData(netGamingExpType,
				tempTimeStamp[0], tempTimeStamp[1],walletId));
		session.setAttribute("DATE_BEANS", dateBeans);
		}
		return SUCCESS;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void updateAgentData() throws LMSException, IOException {
		int walletId = 0;
		String result="";
		ServletContext sc = ServletActionContext.getServletContext();
		String OlaNetGamingUpdateMode=(String) sc.getAttribute("approveNetGamingUpdateMode");
		String[] walletArr = walletName.split(":");
		walletId = Integer.parseInt(walletArr[0]);
		PrintWriter out = getResponse().getWriter();
		//NetGamingExpAgentHelper helper = new NetGamingExpAgentHelper();
		//HttpSession session = request.getSession();
		// set Start Date and endDate ;
		String[] tempTimeStamp = fetchDate(date, netGamingExpType).split("Nxt");
		System.out.println("start Date:"+startDate);
		System.out.println("End date:"+endDate);
		if("MANUAL".equalsIgnoreCase(OlaNetGamingUpdateMode)){
			 result=CreateTransactionForCommissionHelper.retOlaCommissionAgentWiseTransaction(walletId,orgId,startDate, endDate, netGamingExpType, OlaNetGamingUpdateMode);
			}
		
		/*String result = helper.updateAgentData(orgId, (UserInfoBean) session
				.getAttribute("USER_INFO"), netGamingExpType,tempTimeStamp[0],tempTimeStamp[1],walletId);*/
		out.print(result);
	}

	public String updateAllAgent() throws LMSException, ParseException {
		ServletContext sc = ServletActionContext.getServletContext();
		String OlaNetGamingUpdateMode=(String) sc.getAttribute("approveNetGamingUpdateMode");
		int walletId = 0;
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			walletId = Integer.parseInt(walletArr[0]);
		}
		NetGamingExpAgentHelper helper = new NetGamingExpAgentHelper();
		HttpSession session = request.getSession();
		String[] tempTimeStamp = fetchDate(date, netGamingExpType).split("Nxt");
		System.out.println("start Date:"+startDate);
		System.out.println("End date:"+endDate);
		if("MANUAL".equalsIgnoreCase(OlaNetGamingUpdateMode)){
		CreateTransactionForCommissionHelper.retOlaCommissionTransaction(walletId,startDate, endDate, netGamingExpType, OlaNetGamingUpdateMode);
		}
		//helper.updateAllAgentData(tempTimeStamp[0], tempTimeStamp[1],
				//netGamingExpType, (UserInfoBean) session.getAttribute("USER_INFO"),walletId);
		DateBeans dateBeans = new DateBeans();
		dateBeans.setReportType(netGamingExpType);
		dateBeans.setStrDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[0]));
		dateBeans.setEndDateString(DateFormatConverter.convertDateInGlobalFormat(tempTimeStamp[1]));
		session.setAttribute("NetGamingExpenseData", helper.fetchMenuData(netGamingExpType,tempTimeStamp[0], tempTimeStamp[1],walletId));
		//session.setAttribute("DATE_BEANS", dateBeans);
		return SUCCESS;
	}

	/*public String updateAllAgent()throws LMSException{
		int walletId = 0;
		if (walletName.equalsIgnoreCase("-1")
				|| walletName.equalsIgnoreCase("null")) {
			return ERROR;
		} else {
			String[] walletArr = walletName.split(":");
			walletId = Integer.parseInt(walletArr[0]);
		}
		String[] tempTimeStamp = fetchDate(date, netGamingExpType).split("Nxt");
		CreateTransactionForCommissionHelper helper =new CreateTransactionForCommissionHelper();
		helper.retOlaCommissionTransaction(startDate, endDate, netGamingExpType, "MANNUAL");
		
		return SUCCESS;
	}*/
	
	private String fetchDate(String tDate, String type) {
		Timestamp startDate = null;
		Timestamp endDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// code to get week for given date... goes here
		try {
			if ("WEEKLY".equalsIgnoreCase(type)) {
				cal.setTimeInMillis(sdf.parse(tDate).getTime());
				if(cal.get(Calendar.DAY_OF_WEEK) != 1)
					cal.add(Calendar.DAY_OF_WEEK,
							-(cal.get(Calendar.DAY_OF_WEEK) - 2));
				else
					cal.add(Calendar.DAY_OF_WEEK,-6);
				setStartDate(new Date(cal.getTime().getTime()));
				startDate = new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime());
				cal.add(Calendar.DAY_OF_MONTH, +6);
				setEndDate(new Date(cal.getTime().getTime()));
				endDate = new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime()
						+ 24 * 60 * 60 * 1000 - 1000);
				return startDate + "Nxt" + endDate;
			} else if ("MONTHLY".equalsIgnoreCase(type)){
				cal.setTimeInMillis(sdf.parse(date).getTime());
				cal.add(Calendar.DAY_OF_MONTH,-cal.get(Calendar.DAY_OF_MONTH));
				startDate = new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime()+ 24 * 60 * 60 * 1000);
				cal.setTimeInMillis(startDate.getTime());
				cal.add(Calendar.MONTH,1);
				endDate =  new Timestamp(sdf.parse(
						new java.sql.Date(cal.getTimeInMillis()).toString())
						.getTime() - 1000);
				return startDate + "Nxt" + endDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int[] getOrgId() {
		return orgId;
	}

	public void setOrgId(int[] orgId) {
		this.orgId = orgId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNetGamingExpType() {
		return netGamingExpType;
	}

	public void setNetGamingExpType(String netGamingExpType) {
		this.netGamingExpType = netGamingExpType;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	
}

