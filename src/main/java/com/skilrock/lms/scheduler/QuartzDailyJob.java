package com.skilrock.lms.scheduler;



import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.ola.NetGamingForRummyHelper;
import com.skilrock.lms.coreEngine.ola.OlaRummyCounterReset;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentPostSaleCommissionHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CancelPendingTicketSchedularDG;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetailerPostSaleCommissionHelper;
import com.skilrock.lms.web.accMgmt.common.CalculateGovCommAmtScheduler;
import com.skilrock.lms.web.drawGames.reportsMgmt.common.reportsMgmtUtility;

public class QuartzDailyJob implements Job {

	private static Log logger = LogFactory.getLog(QuartzDailyJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public QuartzDailyJob() {
	}

	private void createTempFolder() {

		String projectName = AutoQuartzMain.scx.getContextPath();
		String folderName = "Mail_Excel_Files" + projectName;
		logger.info("Created folder Name = " + folderName);
		File tempFolder = new File(folderName);

		int i = 0;
		while (!tempFolder.exists()) {
			tempFolder.mkdirs();
			i++;
			if (i > 5) {
				break;
			}
		}

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
		
		//Connection con =null;
		// Statement stmt =null;
		// ResultSet rs =null;
	try {
			createTempFolder();
			String jobName = context.getJobDetail().getFullName();
			String str = "Current Day";
			DateBeans datebean = new GetDate().getDateForSchedular(str);

			logger.info("Daily Job says: " + jobName + " executing at "	+ new java.util.Date());
			//con =DBConnect.getConnection();
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			// con.setAutoCommit(false);
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId());
						ResponsibleGaming.insertDailyHistory();	
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Responsible_Gaming_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				if(scheBeanMap.get("Retailer_Activity_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Retailer_Activity_Daily_SCHEDULER").getJobId());
						// call daily sale/PWT retailer date entry
						logger.info("entering into getDailyRetActivity.......");
						reportsMgmtUtility.getCommonDailyRetActivity();	
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Retailer_Activity_Daily_SCHEDULER").getJobId());
						logger.info("successfully done");
					}catch (Exception e) {
						logger.error("Exception in Retailer_Activity_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Retailer_Activity_Daily_SCHEDULER").getJobId(), errorMsg);
					}

				}
				if (scheBeanMap.get("Agent_TrngExp_Daily_SCHEDULER").isActive()) {
					String errorMsg = null;
					try {
						// generate credit notes to all agent as training
						// expenses. .
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Agent_TrngExp_Daily_SCHEDULER").getJobId());
						logger.info("genrating redit notes to all agent as training expenses.... ");
						if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_DG_TRAINING_EXPENSE_ENABLED"))){
							new TrainingExpAgentHelper().submitDailyTrainingExpForAgents(AutoQuartzMain.scx);
						}
						if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW())){
							if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_TRAINING_EXPENSE_ENABLED")) || "YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_INCENTIVE_EXPENSE_ENABLED"))){
								new TrainingExpAgentHelper().submitDailyInstantWinTrainingExpForAgents(AutoQuartzMain.scx);
							}
						}
						logger.info("successfully generated .. .");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Agent_TrngExp_Daily_SCHEDULER").getJobId());
					} catch (Exception e) {
						logger.error("Exception in Agent_TrngExp_Daily_SCHEDULER ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Agent_TrngExp_Daily_SCHEDULER").getJobId(), errorMsg);
					}
				}
				if(scheBeanMap.get("Post_Agt_Commission_Daily_SCHEDULER").isActive()){
						String errorMsg = null;
					try{
						//Post Sale Deposit Commission  by sumit
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Post_Agt_Commission_Daily_SCHEDULER").getJobId());
						String deploymentDate = (String) AutoQuartzMain.scx.getAttribute("DEPLOYMENT_DATE");
						logger.info("daily post commission schedular started");
						new AgentPostSaleCommissionHelper().insertDailyPostDepositAgentWiseScheduler(deploymentDate);
						//new RetailerPostSaleCommissionHelper().insertDailyPostDepositRetailerWiseScheduler(deploymentDate);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Post_Agt_Commission_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Post_Agt_Commission_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Post_Agt_Commission_Daily_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				
				if(scheBeanMap.get("Post_Ret_Commission_Daily_SCHEDULER").isActive()){
						String errorMsg = null;
					try{
						//Post Sale Deposit Commission  by sumit
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Post_Ret_Commission_Daily_SCHEDULER").getJobId());
						String deploymentDate = (String) AutoQuartzMain.scx.getAttribute("DEPLOYMENT_DATE");
						logger.info("daily post commission schedular started");
						//new AgentPostSaleCommissionHelper().insertDailyPostDepositAgentWiseScheduler(deploymentDate);
						new RetailerPostSaleCommissionHelper().insertDailyPostDepositRetailerWiseScheduler(deploymentDate);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Post_Ret_Commission_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Post_Ret_Commission_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Post_Ret_Commission_Daily_SCHEDULER").getJobId(), errorMsg);
					}
					
				} 
				
				if(scheBeanMap.get("Send_Combined_Mail_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						// send mail to back office users
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Send_Combined_Mail_Daily_SCHEDULER").getJobId());
						logger.info("send mail to back office users started");
						SendReportMailerMain sendmail = new SendReportMailerMain(datebean);
						// sendmail.sendMailToBO(str);
						sendmail.allDailyReportsCombined();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Send_Combined_Mail_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Send_Combined_Mail_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}

					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Send_Combined_Mail_Daily_SCHEDULER").getJobId(), errorMsg);
					}
				}
				
				if(scheBeanMap.get("Send_Mail_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{

						// sleep thread to 5 minute
						Thread.sleep(1000 * 60 * 1);

						// send mail to agents
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Send_Mail_Daily_SCHEDULER").getJobId());
						logger.info("send mail to agents started");
						SendReportMailerMain sendmail = new SendReportMailerMain(datebean);
						sendmail.sendMailToAgent(str);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Send_Mail_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Send_Mail_Daily_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Send_Mail_Daily_SCHEDULER").getJobId(), errorMsg);
					}
				}
				
				if(scheBeanMap.get("Reset_Login_Attempts_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Reset_Login_Attempts_Daily_SCHEDULER").getJobId());
						
						logger.info("Reset_Login_Attempts_Daily_SCHEDULER started");
						UserAuthenticationHelper.resetAll();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Reset_Login_Attempts_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Reset_Login_Attempts_Daily_SCHEDULER  ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Reset_Login_Attempts_Daily_SCHEDULER").getJobId(), errorMsg);
					}
				}
				
				if(scheBeanMap.get("Ola_Rummy_PinCounterReset_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Ola_Rummy_PinCounterReset_SCHEDULER").getJobId());
						logger.info("--RMS Counter Reset Processing Started....");
						
						OlaRummyCounterReset.resetCounter();
						logger.info("--RMS Counter Reset Processing Ended....");
					
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Ola_Rummy_PinCounterReset_SCHEDULER").getJobId());
						
					}catch (Exception e) {
						logger.error("Exception in Ola_Rummy_PinCounterReset_SCHEDULER  ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Ola_Rummy_PinCounterReset_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				
				if(scheBeanMap.get("Ola_Rummy_NetGamingDaily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Ola_Rummy_NetGamingDaily_SCHEDULER").getJobId());
						logger.info("--Insert Daily Deposit and wagering Data Started....");
						
						NetGamingForRummyHelper.insertNetGamingData();
						logger.info("--Insert Daily Deposit and wagering Data Ended....");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Ola_Rummy_NetGamingDaily_SCHEDULER").getJobId());
						
					}catch (Exception e) {
						logger.error("Exception in Ola_Rummy_NetGamingDaily_SCHEDULER  ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Ola_Rummy_NetGamingDaily_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
				/*if(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").isActive()){
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId());
						logger.info("--Insert responsible gaming history Started....");
						
						ResponsibleGaming.insertDailyHistory();
						logger.info("--Insert responsible gaming history Ended....");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId());
					
						
					}catch (Exception e) {
						logger.error("Exception in Responsible_Gaming_Daily_SCHEDULER  ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("Responsible_Gaming_Daily_SCHEDULER").getJobId(), errorMsg);
					}
					
				} */
				
				if(scheBeanMap.get("Cancel_Pending_Ticket_DG_SCHEDULER").isActive()){
					int jobId = 0;
					String errorMsg = null;
					try {
						jobId = scheBeanMap.get("Cancel_Pending_Ticket_DG_SCHEDULER").getJobId();
						logger.info("Cancel_Pending_Ticket_DG_SCHEDULER Start ...");
						//	Call PendingCancelTktDG of CancelPendingTicketSchedulerDg
						CancelPendingTicketSchedularDG.pendingCancelTicketInsertSchedular(jobId);
						logger.info("Cancel_Pending_Ticket_DG_SCHEDULER End ...");
					} catch (Exception e) {
						logger.error("Exception in Cancel_Pending_Ticket_DG_SCHEDULER - ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(jobId, errorMsg);
					}
				}

				if(scheBeanMap.get("Manage_Good_Cause_Scheduler").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Manage_Good_Cause_Scheduler").getJobId());
						new CalculateGovCommAmtScheduler().calculateGovernmentCommission() ;
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Manage_Good_Cause_Scheduler").getJobId());
					} catch (Exception e) {
						logger.error("Exception in Auto_Quartz_SCHEDULER ", e);
						if(e.getMessage()!=null) {
							errorMsg =e.getMessage();
						} else {
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Auto_Quartz_SCHEDULER").getJobId(), errorMsg);
					}
				}

				/*if(scheBeanMap.get("Agent_Auto_Block_SCHEDULER").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("Agent_Auto_Block_SCHEDULER").getJobId());
						logger.info("Agent_Auto_Block_SCHEDULER Started");

						AgentAutoBlockServiceImpl.getInstance().autoBlock();

						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Agent_Auto_Block_SCHEDULER").getJobId());
					} catch (Exception e) {
						logger.error("Exception in Agent_Auto_Block_SCHEDULER - ", e);
						if(e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg="Error Occurred Msg is Null.";
						}
					}

					if(errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Agent_Auto_Block_SCHEDULER").getJobId(), errorMsg);
					}
				}*/
				
				/*if(scheBeanMap.get("Organization_Balance_Update_Scheduler").isActive()){
					int jobId = 0;
					String errorMsg = null;
					try {
						jobId = scheBeanMap.get("Organization_Balance_Update_Scheduler").getJobId();
						logger.info("Organization_Balance_Update_Scheduler Start ...");
						new OrgBalanceUpdateHelper().inserOpeningBalForAgentAndRetailer();
						logger.info("Organization_Balance_Update_Scheduler End ...");
					} catch (Exception e) {
						logger.error("Exception in Organization_Balance_Update_Scheduler - ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(jobId, errorMsg);
					}
				}*/
			}
		} catch (Exception e) {
			logger.error("Exception in Daily Job Scheduler  ", e);
		}
	}
}