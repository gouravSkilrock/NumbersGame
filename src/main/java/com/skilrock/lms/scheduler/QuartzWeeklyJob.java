package com.skilrock.lms.scheduler;

import java.util.Calendar;
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
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.controllerImpl.TrackFullTicketControllerImpl;

public class QuartzWeeklyJob implements Job {

	private static Log logger = LogFactory.getLog(QuartzWeeklyJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public QuartzWeeklyJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with
	 * the <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try {

			String jobName = context.getJobDetail().getFullName();
			logger.info("Weekly Job says: " + jobName + " executing at "+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				if(scheBeanMap.get("Agent_TrngExp_Weekly_SCHEDULER").isActive()){
					Calendar calendar = Calendar.getInstance();
					if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
						String errorMsg = null;
						try{
							SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Agent_TrngExp_Weekly_SCHEDULER").getJobId());
							//give credit note to agents on weekly basis as training expenses
							logger.info("issuing credit note to agents on weekly basis as training expenses.... ");
							if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_DG_TRAINING_EXPENSE_ENABLED"))){
								new TrainingExpAgentHelper().submitWeeklyTrngExpForAgents();
							}
							if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW())){
								if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_TRAINING_EXPENSE_ENABLED")) || "YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_INCENTIVE_EXPENSE_ENABLED"))){
									new TrainingExpAgentHelper().submitWeeklyTrngExpInstantWinForAgents();
								}
							}
							logger.info("successfully done.....");
							SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Agent_TrngExp_Weekly_SCHEDULER").getJobId());
						}catch (Exception e) {
							logger.error("Exception in Agent_TrngExp_Weekly_SCHEDULER ", e);
							if(e.getMessage()!=null){
								errorMsg =e.getMessage();
							}else{
								errorMsg="Error Occurred Msg Is Null ";
							}
						}
						if(errorMsg!=null){
							SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Agent_TrngExp_Weekly_SCHEDULER").getJobId(), errorMsg);
						}
					} else {
						logger.info("Today is Not Monday, Cannot Perform Action.");
					}
				}
				if(scheBeanMap.get("Send_Mail_Weekly_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Send_Mail_Weekly_SCHEDULER").getJobId());
						String str = "Current Week";
						DateBeans datebean = new GetDate().getDateForSchedular(str);
						
						SendReportMailerMain sendmail = new SendReportMailerMain(datebean);
						sendmail.sendMailToBO(str);

						// sleep thread to 5 minute
						Thread.sleep(1000 * 60 * 1);
						

						// send mail to agents
						sendmail.sendMailToAgent(str);
						//logger.info("Weekly Job says: " + jobName + " executing at "+ new java.util.Date());
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Send_Mail_Weekly_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Send_Mail_Weekly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Send_Mail_Weekly_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				if(scheBeanMap.get("Responsible_Gaming_Weekly_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Responsible_Gaming_Weekly_SCHEDULER").getJobId());
						//give credit note to agents on weekly basis as training expenses
						logger.info("issuing credit note to agents on weekly basis as training expenses.... ");
						// call responsible gaming
							ResponsibleGaming.insertWeeklyHistory();
						logger.info("successfully done.....");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Responsible_Gaming_Weekly_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Responsible_Gaming_Weekly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Responsible_Gaming_Weekly_SCHEDULER").getJobId(), errorMsg);
					}
					
				}

				/*	Weekly Scheduler for Track Full Ticket Data Start	*/
				logger.info("Active status of Track_Full_Ticket_Weekly_SCHEDULER is - "+scheBeanMap.get("Track_Full_Ticket_Weekly_SCHEDULER").isActive());
				if(scheBeanMap.get("Track_Full_Ticket_Weekly_SCHEDULER").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Track_Full_Ticket_Weekly_SCHEDULER").getJobId());
						logger.info("Reset Track Full Ticket User Attemps Starts.");
						TrackFullTicketControllerImpl.resetUsersAttemptLimits();
						logger.info("Successfully Reset Track Full Ticket User Attemps.");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Track_Full_Ticket_Weekly_SCHEDULER").getJobId());
					} catch (Exception e) {
						logger.error("Exception in Track_Full_Ticket_Weekly_SCHEDULER - ", e);
						if(e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null.";
						}
					}

					if(errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Track_Full_Ticket_Weekly_SCHEDULER").getJobId(), errorMsg);
					}
				}
				/*	Weekly Scheduler for Track Full Ticket Data End	*/
			}
		
		} catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}

	}

}