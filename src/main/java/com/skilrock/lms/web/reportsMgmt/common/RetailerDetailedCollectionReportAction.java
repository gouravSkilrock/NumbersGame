package com.skilrock.lms.web.reportsMgmt.common;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionLiveReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetailerDetailedCollectionReportHelper;

public class RetailerDetailedCollectionReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public RetailerDetailedCollectionReportAction() {
		super(RetailerDetailedCollectionReportAction.class.getName());
	}

	private String start_date;
	private String end_Date;

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

	public String retDetailedCollectionReportMenu() {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			return SUCCESS;
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}
	}

	public String retDetailedCollectionReportExport() {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = null;
		UserInfoBean userBean = null;

		SimpleDateFormat formFormat = null;
		PrintWriter out = null;
		RetailerDetailedCollectionReportHelper helper = null;
		Map<String, CollectionReportOverAllBean> retailerMap = null;
		CollectionReportOverAllBean reportBean = null;
		String countryDeployed = (String)ServletActionContext.getServletContext().getAttribute("COUNTRY_DEPLOYED");
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				session = getSession();
				formFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				Timestamp startDate = new Timestamp(formFormat.parse(start_date+" 00:00:00").getTime());
				Timestamp endDate = new Timestamp(formFormat.parse(end_Date+" 23:59:59").getTime());
				Timestamp deployDate = new Timestamp((new SimpleDateFormat((String) sc.getAttribute("date_format"))).parse((String) sc.getAttribute("DEPLOYMENT_DATE")).getTime());
				helper = new RetailerDetailedCollectionReportHelper();
				retailerMap = helper.getRetailerDetailedCollection(deployDate, startDate, endDate, reportStatusBean);

				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=Retailer_Detail_Collection_Report.xls");
				out = response.getWriter();

				byte count = 4;
				StringBuilder responseMessage = new StringBuilder();
				start_date = GetDate.getConvertedDate(new Date(formFormat.parse(start_date+" 00:00:00").getTime()));
				end_Date = GetDate.getConvertedDate(new Date(formFormat.parse(end_Date+" 23:59:59").getTime()));
				responseMessage.append("<center><h4>Retailer Detailed Collection Report : ").append(start_date).append(" to ").append(end_Date).append("</h4></center>");

				boolean isDG = ReportUtility.isDG;
				String dgMainTh = (isDG)?"<th colspan=2>Draw Game</th>":"";
				String dgSubTh = (isDG)?"<th>Sale Amount</th><th>PWT Amount</th>":"";
				count = (isDG)?count+=2:count;

				boolean isSE = ReportUtility.isSE;
				String seMainTh = (isSE)?"<th colspan=2>Scratch Game</th>":"";
				String seSubTh = (isSE)?"<th>Purchase From Agent</th><th>PWT Amount</th>":"";
				count = (isSE)?count+=2:count;

				boolean isCS = ReportUtility.isCS;
				String csMainTh = (isCS)?"<th colspan=2>Commertial Services</th>":"";
				String csSubTh = (isCS)?"<th>Sale Amount</th><th>Cancel Amount</th>":"";
				count = (isCS)?count+=2:count;

				boolean isOLA = ReportUtility.isOLA;
				String olaMainTh = (isOLA)?"<th colspan=2>Offline Affiliates</th>":"";
				String olaSubTh = (isOLA)?"<th>Deposit Amount</th><th>Withdrawal Amount</th>":"";
				count = (isOLA)?count+=2:count;

				boolean isSLE = ReportUtility.isSLE;
				String sleMainTh = (isSLE)?"<th colspan=2>Sports Lottery</th>":"";
				String sleSubTh = (isSLE)?"<th>Sale Amount</th><th>PWT Amount</th>":"";
				count = (isSLE)?count+=2:count;

				userBean = (UserInfoBean) session.getAttribute("USER_INFO");
				String orgCode = userBean.getOrgCode();
				String orgAddress = new CollectionLiveReportOverAllHelper().getOrgAdd(userBean.getUserOrgId());
				responseMessage.append("<table border=1px><tbody>");
				responseMessage.append("<tr><th colspan=").append(count).append(">").append(orgCode).append("</th><th rowspan=2>Amount in ").append(Utility.getPropertyValue("CURRENCY_SYMBOL")).append("</th></tr>");
				responseMessage.append("<tr><th colspan=").append(count).append(">").append(orgAddress).append("</th></tr>");

				responseMessage.append("<tr><th rowspan=2>Party Name</th><th rowspan=2>Agent Name</th><th rowspan=2>Opening Balance</th>");
				responseMessage.append(dgMainTh).append(seMainTh).append(csMainTh).append(olaMainTh).append(sleMainTh);
				responseMessage.append("<th rowspan=2>Net Collection</th><th rowspan=2>Closing Balance</th></tr><tr>");
				responseMessage.append(dgSubTh).append(seSubTh).append(csSubTh).append(olaSubTh).append(sleSubTh);
				responseMessage.append("</tr>");

				double dgSale = 0.0;
				double dgPwt = 0.0;
				double seSale = 0.0;
				double sePwt = 0.0;
				double csSale = 0.0;
				double csCancel = 0.0;
				double olaDeposit = 0.0;
				double olaWithdraw = 0.0;
				double sleSale = 0.0;
				double slePwt = 0.0;
				double netPayment = 0.0;
				double openingBalance = 0.0;
				for(String str : retailerMap.keySet()) {
					reportBean = retailerMap.get(str);
					openingBalance = reportBean.getOpeningBal();
					responseMessage.append("<tr>");
					responseMessage.append("<td>").append(reportBean.getAgentName()).append("</td>");
					responseMessage.append("<td>").append(reportBean.getParentName()).append("</td>");
					responseMessage.append("<td>").append("PHLIP".equalsIgnoreCase(countryDeployed) ? openingBalance : -openingBalance).append("</td>");
					//openingBalance -= reportBean.getClLimit();
					if(isDG) {
						dgSale = reportBean.getDgSale()-reportBean.getDgCancel();
						dgPwt = reportBean.getDgPwt();
						openingBalance += (dgSale-dgPwt);
						responseMessage.append("<td>").append(dgSale).append("</td>");
						responseMessage.append("<td>").append(dgPwt).append("</td>");
					}
					if(isSE) {
						seSale = reportBean.getSeSale();
						sePwt = reportBean.getSePwt();
						openingBalance += (seSale-sePwt);
						responseMessage.append("<td>").append(seSale).append("</td>");
						responseMessage.append("<td>").append(sePwt).append("</td>");
					}
					if(isCS) {
						csSale = reportBean.getCSSale();
						csCancel = reportBean.getCSCancel();
						openingBalance += (csSale-csCancel);
						responseMessage.append("<td>").append(csSale).append("</td>");
						responseMessage.append("<td>").append(csCancel).append("</td>");
					}
					if(isOLA) {
						olaDeposit = reportBean.getDeposit();
						olaWithdraw = reportBean.getWithdrawal();
						openingBalance += (olaDeposit-olaWithdraw);
						responseMessage.append("<td>").append(olaDeposit).append("</td>");
						responseMessage.append("<td>").append(olaWithdraw).append("</td>");
					}
					if(isSLE) {
						sleSale = reportBean.getSleSale()-reportBean.getSleCancel();
						slePwt = reportBean.getSlePwt();
						openingBalance += (sleSale-slePwt);
						responseMessage.append("<td>").append(sleSale).append("</td>");
						responseMessage.append("<td>").append(slePwt).append("</td>");
					}
					netPayment = reportBean.getCash() + reportBean.getCheque() - reportBean.getChequeReturn() - reportBean.getDebit() + reportBean.getCredit() + reportBean.getBankDep();
					openingBalance -= netPayment;
					responseMessage.append("<td>").append(netPayment).append("</td>");
					responseMessage.append("<td>").append("PHILIP".equalsIgnoreCase(countryDeployed) ? openingBalance : -openingBalance).append("</td>");
					responseMessage.append("</tr>");
				}
				responseMessage.append("</tbody></table>");
				out.write(responseMessage.toString());
				out.flush();
				out.close();
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}