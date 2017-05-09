package com.skilrock.lms.scheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This Class is used to scheduling the jobs at specific interval of time.
 * 
 * @author Arun Upadhyay
 */
public class AutoQuartzMainForMail {

	public static void main(String[] args) throws Exception {

		AutoQuartzMainForMail example = new AutoQuartzMainForMail();
		example.run();
	}

	public void run() throws Exception {
		Log log = LogFactory.getLog(AutoQuartzMainForMail.class);

		System.out
				.println("------- Initializing the Auto generated mailing system -------------------");
		log.debug("Sample debug message");
		log.info("Sample info message");
		log.warn("Sample warn message");
		log.error("Sample error message");
		log.fatal("Sample fatal message");

		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization Complete --------");
		log.info("------- Scheduling Jobs ----------------");
		System.out.println("------- Initialization Complete --------");
		System.out.println("------- Scheduling Jobs ----------------");

		// jobs can be scheduled before sched.start() has been called

		JobDetail job = null;
		CronTrigger trigger = null;
		Date ft = null;

		// job 1 will run at 00:00:02 PM of First day of the month
		job = new JobDetail("monthly", "group1", QuartzMonthlyJob.class);
		trigger = new CronTrigger("trigger1", "group1", "monthly", "group1",
				"2 0 0 1 * ?");
		sched.addJob(job, true);
		ft = sched.scheduleJob(trigger);
		log.info(job.getFullName() + " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());
		System.out.println(job.getFullName()
				+ " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());

		// job 2 will run on every 'MONDAY 00:00:02' PM
		job = new JobDetail("weekly", "group1", QuartzWeeklyJob.class);
		trigger = new CronTrigger("trigger2", "group1", "weekly", "group1",
				"2 0 0 ? * MON");
		sched.addJob(job, true);
		ft = sched.scheduleJob(trigger);
		log.info(job.getFullName() + " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());
		System.out.println(job.getFullName()
				+ " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());

		// job 3 will run every day in 00:00:02 PM
		job = new JobDetail("daily", "group1", QuartzDailyJob.class);
		trigger = new CronTrigger("trigger3", "group1", "daily", "group1",
				"2 43 17 ? * *");
		sched.addJob(job, true);
		ft = sched.scheduleJob(trigger);
		log.info(job.getFullName() + " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());
		System.out.println(job.getFullName()
				+ " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());

		log.info("------- Starting Scheduler ----------------");
		System.out.println("------- Starting Scheduler ----------------");

		// All of the jobs have been added to the scheduler, but none of the
		// jobs will run until the scheduler has been started
		sched.start();

		log.info("------- Started Scheduler -----------------");
		System.out.println("------- Started Scheduler -----------------");

		SchedulerMetaData metaData = sched.getMetaData();

		log.info("Executed " + metaData.numJobsExecuted() + " jobs.");
		System.out.println("Executed " + metaData.numJobsExecuted() + " jobs.");

	}

}