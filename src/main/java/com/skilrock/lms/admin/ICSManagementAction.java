package com.skilrock.lms.admin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.admin.common.ICSManagementHelper;
import com.skilrock.lms.beans.ICSDailyQueryStatusBean;
import com.skilrock.lms.beans.ICSQueryMasterBean;
import com.skilrock.lms.beans.ICSQueryStatusReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class ICSManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ICSManagementAction() {
		super(ICSManagementAction.class);
	}

	private String startDate;
	private String endDate;
	private List<ICSQueryMasterBean> icsReportList;
	private String mailToNameList;

	private String[] queryId;
	private List<ICSDailyQueryStatusBean> dailyQueryStatusList;

	private String queryDescription;
	private String isCritical;
	private String errorMessage;
	private String status;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<ICSQueryMasterBean> getIcsReportList() {
		return icsReportList;
	}

	public void setIcsReportList(List<ICSQueryMasterBean> icsReportList) {
		this.icsReportList = icsReportList;
	}

	public String getMailToNameList() {
		return mailToNameList;
	}

	public void setMailToNameList(String mailToNameList) {
		this.mailToNameList = mailToNameList;
	}

	public String[] getQueryId() {
		return queryId;
	}

	public void setQueryId(String[] queryId) {
		this.queryId = queryId;
	}

	public List<ICSDailyQueryStatusBean> getDailyQueryStatusList() {
		return dailyQueryStatusList;
	}

	public void setDailyQueryStatusList(
			List<ICSDailyQueryStatusBean> dailyQueryStatusList) {
		this.dailyQueryStatusList = dailyQueryStatusList;
	}

	public String getQueryDescription() {
		return queryDescription;
	}

	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}

	public String getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(String isCritical) {
		this.isCritical = isCritical;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String queryStatusReportMenu() {
		ICSManagementHelper helper = new ICSManagementHelper();
		try {
			icsReportList = helper.getAllQueries();
		} catch (LMSException le) {
			le.printStackTrace();
			logger.error("Exception",le);
    		return "applicationException";
		} catch (Exception e) {
			e.printStackTrace();
	    	return "applicationException";
		}

		return SUCCESS;
	}

	public String queryStatusReportSearch() {
		HttpSession session = null;
		ICSManagementHelper helper = new ICSManagementHelper();
		SimpleDateFormat dateFormat = null;
		Timestamp startTime = null;
		Timestamp endTime = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			startTime = new Timestamp(ReportUtility.getZeroTimeDate(dateFormat.parse(startDate)).getTime());
			endTime = new Timestamp(ReportUtility.getLastTimeDate(dateFormat.parse(endDate)).getTime());

			session = getSession();
			session.setAttribute("ICS_REPORT_LIST", helper.getIcsQueryStatusData(startTime, endTime));
		} catch (LMSException le) {
			le.printStackTrace();
			logger.error("Exception",le);
    		return "applicationException";
		} catch (Exception e) {
			e.printStackTrace();
	    	return "applicationException";
		}

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String queryStatusReportMail() {
		HttpSession session = null;
		System.out.println(mailToNameList);
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			session = getSession();
			List<ICSQueryStatusReportBean> icsReportList = (List<ICSQueryStatusReportBean>) session.getAttribute("ICS_REPORT_LIST");

			StringBuilder messageBody = new StringBuilder("<html><table border='1px' style='border-color:black;'>");
			messageBody
				.append("<tr>")
					.append("<th>").append("Query Id").append("</th>")
					.append("<th>").append("Query Title").append("</th>")
					.append("<th>").append("Query Description").append("</th>")
					.append("<th>").append("Actual Result").append("</th>")
					.append("<th>").append("Expected Result").append("</th>")
				.append("</tr>");
			for(ICSQueryStatusReportBean bean : icsReportList) {
				messageBody
				.append("<tr>")
					.append("<td>").append(bean.getQueryId()).append("</td>")
					.append("<td>").append(bean.getQueryTitle()).append("</td>")
					.append("<td>").append(bean.getQueryDescription()).append("</td>")
					.append("<td>").append(bean.getActualResult()).append("</td>")
					.append("<td>").append(bean.getExpectedResult()).append("</td>")
				.append("</tr>");
			}
			String messageText = messageBody.append("</table></html>").toString();
			String subject = "ICS Daily Status of "+simpleDateFormat.format(Util.getCurrentTimeStamp().getTime());
			String[] mailToArray = mailToNameList.split(",");
			MailSend mailSend = null;
			for(String mailTo : mailToArray) {
				mailSend = new MailSend(mailTo.trim(), messageText);
				mailSend.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
	    	return "applicationException";
		}

		return SUCCESS;
	}

	public String runIcsQueries() {
		String queryIdString = "";
		for(int i=0; i<queryId.length; i++) {
			queryIdString += queryId[i]+",";
		}
		queryIdString = queryIdString.substring(0, queryIdString.length()-1);

		ICSManagementHelper helper = new ICSManagementHelper();

		try {
			dailyQueryStatusList = helper.executeICSQuery(queryIdString, "MANNUAL");
		} catch (LMSException le) {
			le.printStackTrace();
			logger.error("Exception",le);
    		return "applicationException";
		} catch (Exception e) {
			e.printStackTrace();
	    	return "applicationException";
		}

		return SUCCESS;
	}

	public String updateIcsQueries() {
		UserInfoBean userBean = null;
		ICSManagementHelper helper = new ICSManagementHelper();
		try {
			userBean = getUserBean();
			if(errorMessage.length()==0)
				errorMessage = null;
			helper.updateIcsQueries(Integer.parseInt(queryId[0]), queryDescription, errorMessage, isCritical, status, userBean.getUserId());
		} catch (LMSException le) {
			le.printStackTrace();
			logger.error("Exception",le);
    		return "applicationException";
		} catch (Exception e) {
			e.printStackTrace();
	    	return "applicationException";
		}

		return SUCCESS;
	}
}