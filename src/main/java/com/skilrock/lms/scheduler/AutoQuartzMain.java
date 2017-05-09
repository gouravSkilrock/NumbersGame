package com.skilrock.lms.scheduler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

import com.skilrock.lms.common.RetailerActivityHistory;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.loginMgmt.AutomaticArchiving;

public class AutoQuartzMain extends GenericServlet {

	public static ServletContext scx = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(AutoQuartzMain.class);

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		/*System.out
				.println(" ======================================================================= ");*/
		scx = servletConfig.getServletContext();
		logger.info("servletContext name  == " + scx.getContextPath());
		/*System.out
				.println(" ======================================================================= ");*/
		Connection con =null;
		Statement stmt = null;
		ResultSet rs =null;
		try {
			logger.info("Initializing Scheduler PlugIn for Jobs!");
			// Log log = LogFactory.getLog(AutoQuartzMainForMail.class);

			//String cronExpr = null;
			//String dailyCronExpr = null;
			//String weeklyCronExpr = null;
			//String montlyCronExpr = null;
			//JobDetail job = null;
			//String olaCommDistribution = null;
			//String olaDailyCronExpr = null;
			//String olaCommUpdateMonthly = null;
			//String olaCommUpdateWeekly = null;
			//String dailyWithdrawalRequest=null;// Rummy Withdrawal 
			//String autoArchivingExpr = null;
			//String retActivityHistoryExpr = null;
			//CronTrigger trigger = null;
			//	Date ft = null;

			// First we must get a reference to a scheduler
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			con =DBConnect.getConnection();
			stmt = con.createStatement();
			String qry = "select jobGroup, scheduled_Time from st_lms_scheduler_master where status ='ACTIVE' group by jobGroup ";
			rs = stmt.executeQuery(qry);
			
			while (rs.next()) {

				// jobs can be scheduled before sched.start() has been called
		
				String jobGroup = rs.getString("jobGroup");
				String scheduledTime = rs.getString("scheduled_Time");
			
				if (jobGroup.equalsIgnoreCase("QuartzMonthlyJob")) {
					scheduleJob("job1", jobGroup, "trigger1", QuartzMonthlyJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("QuartzWeeklyJob")) {
					scheduleJob("job2", jobGroup, "trigger2", QuartzWeeklyJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("QuartzDailyJob")) {
					scheduleJob("job3", jobGroup, "trigger3", QuartzDailyJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("AutoQuartzJob")) {
					scheduleJob("job4", jobGroup, "trigger4", AutoQuartzJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("QuartzJobUpdateLedgerBalance")) {
					scheduleJob("job5", jobGroup, "trigger5", QuartzJobUpdateLedgerBalance.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("ICS")) {
					scheduleJob("job6", jobGroup, "trigger6", QuartzJobIcsCronExpr.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("UpdateLedger")) {
					scheduleJob("job7", jobGroup, "trigger7", QuartzClmBalUpdateJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("QuartzReconEntryJob")) {
					scheduleJob("job8", jobGroup, "trigger8", QuartzReconEntryJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("OlaCommDistribution")) {
					scheduleJob("job11", jobGroup, "trigger11", OlaCommDistribution.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("OlaCommUpdateWeekly")) {
					scheduleJob("job12", jobGroup, "trigger12", OlaCommUpdateWeekly.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("OlaCommUpdateMonthly")) {
					scheduleJob("job13", jobGroup, "trigger13", OlaCommUpdateMonthly.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("OlaRummyWithRequest")) {
					scheduleJob("job14", jobGroup, "trigger14", OlaRummyWithRequest.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("AutomaticArchiving")) {
					scheduleJob("job15", jobGroup, "trigger15", AutomaticArchiving.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("DailyActivityHistory")) {
					scheduleJob("job16", jobGroup, "trigger16", RetailerActivityHistory.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("DailyLogoutAllRetJob")) {
					scheduleJob("job17", jobGroup, "trigger17", DailyLogoutAllRetJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("GenMapIdweeklyCronExpr")) {
					scheduleJob("job18", jobGroup, "trigger18", GenerateMappingIds.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("AgentAutoBlockJob")) {
					scheduleJob("job19", jobGroup, "trigger19", AgentAutoBlockScheduleJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("updateLevyNSecDeposit")) {
					scheduleJob("job20", jobGroup, "trigger20", DailySecurityNLevySettlementJob.class, sched,
							scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("auditScriptDailyGroup")) {
					scheduleJob("job21", jobGroup, "trigger21", AuditScriptSchedulerJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("TallyXmlCashBankJob")) {
					scheduleJob("job22", jobGroup, "trigger22", TallyXmlCashBankDailySchedulerJob.class, sched,
							scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("TallyXmlSalePwtTrainingJob")) {
					scheduleJob("job23", jobGroup, "trigger23", TallyXmlSalePwtTrainingMonthlySchedulerJob.class, sched,
							scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("OrgBalUpdateJob")) {
					scheduleJob("job24", jobGroup, "trigger24", OrgBalUpdateJob.class, sched, scheduledTime);
				} else if ("ScratchQuartzDailyJob".equalsIgnoreCase(jobGroup)) {
					scheduleJob("job25", jobGroup, "trigger25", ScratchQuartzDailyJob.class, sched, scheduledTime);
				} else if ("VSSaleReconcGroup".equalsIgnoreCase(jobGroup)) {
					scheduleJob("job26", jobGroup, "trigger26", VSSaleReconciliationScheduler.class, sched,
							scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("LicensingServerJob")) {
					scheduleJob("job27", jobGroup, "trigger27", LicensingServerScheduleJob.class, sched, scheduledTime);
				} else if (jobGroup.equalsIgnoreCase("GoodCauseJob")) {
					scheduleJob("job28", jobGroup, "trigger28", ManageGoodCauseScheduleJob.class, sched, scheduledTime);
				}
			}

			sched.start();
			logger.info("------- Started Scheduler -----------------");
			logger.info("******Scheduler Jobs******** ");
			SchedulerCommonFuntionsHelper.printAllScheduledJobDetails(sched);
			SchedulerMetaData metaData = sched.getMetaData();
			logger.info("Executed " + metaData.numJobsExecuted() + " jobs.");

		}catch(SQLException e){
			logger.error("SQL Exception ",e);
		} catch (Exception e) {
			logger.error("Exception ",e);
		}finally{
			DBConnect.closeCon(con);
			DBConnect.closeRs(rs);
			DBConnect.closeStmt(stmt);
		}

	}

	public void service(ServletRequest serveletRequest,
			ServletResponse servletResponse) throws ServletException,
			IOException {

	}
	
	private static void scheduleJob(String jobName,String jobGroup,String triggerName,Class className,Scheduler sched,String cronExpression) throws LMSException {
		try{ 
		JobDetail job = new JobDetail(jobName, jobGroup,className);
		
		CronTrigger trigger = new CronTrigger(triggerName, jobGroup,jobName,jobGroup,cronExpression);
		sched.addJob(job, true);
		Date ft = sched.scheduleJob(trigger);
		logger.info(job.getFullName() + " has been scheduled to run at: " + ft+ " and repeat based on expression: "+ trigger.getCronExpression());
		
		}catch (Exception e){
			logger.error("Exception ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			
		}
	}

}
