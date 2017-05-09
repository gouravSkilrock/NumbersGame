package com.skilrock.lms.scheduler;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.admin.common.ICSManagementHelper;
import com.skilrock.lms.beans.ICSDailyQueryStatusBean;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.web.drawGames.common.Util;

public class QuartzJobIcsCronExpr implements Job {

	private static Log logger = LogFactory.getLog(QuartzDailyJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try{
		String jobName = context.getJobDetail().getFullName();

		logger.debug("Daily Job says: " + jobName + " executing at "
				+ new java.util.Date());
		logger.debug("DailyJob says: " + jobName + " executing at "
				+ new java.util.Date());
		Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
		//List<ICSBean> icsBean = new ICSForLMS().executeICSQueries();
		SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
		if(scheBeanMap.size()>0){
			
			if(scheBeanMap.get("Quartz_IcsCron_SCHEDULER").isActive()){

		String errorMsg = null;
		SimpleDateFormat simpleDateFormat = null;
		String mailToString = null;
		try {
			SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Quartz_IcsCron_SCHEDULER").getJobId());
			simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			StringBuilder messageBody = new StringBuilder("<html><table border='1px' style='border-color:black;'>");
			messageBody
				.append("<tr>")
					.append("<th>").append("Query Id").append("</th>")
					.append("<th>").append("Query Description").append("</th>")
					.append("<th>").append("Actual Result").append("</th>")
					.append("<th>").append("Expected Result").append("</th>")
					.append("<th>").append("Is Critical").append("</th>")
				.append("</tr>");

			ICSManagementHelper helper = new ICSManagementHelper();
			List<ICSDailyQueryStatusBean> dailyQueryStatusList = helper.executeICSQuery(null, "AUTO");

			int count = 0;
			for(ICSDailyQueryStatusBean dailyQueryStatusBean : dailyQueryStatusList) {
				if("NO".equals(dailyQueryStatusBean.getIsSuccess())) {
					count++;
					int queryId = dailyQueryStatusBean.getQueryId();
					String queryDescription = dailyQueryStatusBean.getQueryDescription();
					String expectedResult = dailyQueryStatusBean.getExpectedResult();
					String actualResult = dailyQueryStatusBean.getActualResult();
					String isCritical = dailyQueryStatusBean.getIsCritical();
					System.err.println("Query ID "+queryId+" As ("+queryDescription+") Gives Result "+actualResult+" Instead of "+expectedResult+" with Criticality "+isCritical);
					messageBody
						.append("<tr>")
							.append("<td>").append(queryId).append("</td>")
							.append("<td>").append(queryDescription).append("</td>")
							.append("<td>").append(actualResult).append("</td>")
							.append("<td>").append(expectedResult).append("</td>")
							.append("<td>").append(isCritical).append("</td>")
						.append("</tr>");
				}
			}
			if(count == 0) {
				messageBody
				.append("<tr>")
					.append("<td colspan=5>").append("ICS Run Successfully").append("</td>")
				.append("</tr>");
			}

			/*statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT value FROM st_ics_property_master WHERE property_dev_name='MAIL_TO' AND status='ACTIVE';");
			if(resultSet.next()) {
				mailToString = resultSet.getString("value");
			}
			connection.close();*/
			mailToString = new ICSManagementHelper().getPropertyValue("MAIL_TO");
			SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Quartz_IcsCron_SCHEDULER").getJobId());
			String messageText = messageBody.append("</table></html>").toString();
			String subject = "ICS Daily Status of "+simpleDateFormat.format(Util.getCurrentTimeStamp().getTime());
			String[] mailToArray = mailToString.split(",");
			MailSend mailSend = null;
			for(String mailTo : mailToArray) {
				mailSend = new MailSend(mailTo.trim(), messageText);
				mailSend.start();
			}
		} catch (Exception ex) {
			logger.error("Exception in Quartz_IcsCron_SCHEDULER ", ex);
			if(ex.getMessage()!=null){
				errorMsg =ex.getMessage();
			}else{
				
				errorMsg="Error Occurred Msg Is Null ";
			}
		}
		
		if(errorMsg!=null){
			SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Quartz_IcsCron_SCHEDULER").getJobId(), errorMsg);
		}
		
		
			}
		}

}catch (LMSException e) {
	logger.error("LMSException in Weekly Job Scheduler  ", e);
}catch (Exception e) {
	logger.error("Exception in Weekly Job Scheduler  ", e);
}
}
	}