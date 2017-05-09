package com.skilrock.lms.scheduler;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.Utility;

import com.skilrock.lms.common.exception.LMSException;

import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.SendReportMailerMain;

import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentPostSaleCommissionHelper;

public class QuartzMonthlyJob implements Job {

	private static Log logger = LogFactory.getLog(QuartzMonthlyJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public QuartzMonthlyJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				if(scheBeanMap.get("Post_Commission_Monthly_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Post_Commission_Monthly_SCHEDULER").getJobId());
						logger.info("Post Sale Commission Scheduler Started");
						String deploymentDate =Utility.getPropertyValue("DEPLOYMENT_DATE") ;// (String) AutoQuartzMain.scx.getAttribute("DEPLOYMENT_DATE");
						new AgentPostSaleCommissionHelper().insertMonthlyPostDepositAgentWiseScheduler(deploymentDate);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Post_Commission_Monthly_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Post_Commission_Monthly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Post_Commission_Monthly_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				
				if(scheBeanMap.get("Send_Mail_Monthly_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Send_Mail_Monthly_SCHEDULER").getJobId());
						String str = "Current Month";
						DateBeans datebean = new GetDate().getDateForSchedular(str);
						logger.debug("SimpleJob says: " + jobName + " executing at "+ new java.util.Date());
						
					
						SendReportMailerMain sendmail = new SendReportMailerMain(datebean);
						sendmail.sendMailToBO(str);
						// sleep thread to 5 minute
						Thread.sleep(1000 * 60 * 1);
						// send mail to agents
						sendmail.sendMailToAgent(str);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Send_Mail_Monthly_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Send_Mail_Monthly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Send_Mail_Monthly_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				
				}
		} catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
	}
}
