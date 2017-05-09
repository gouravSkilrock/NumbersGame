package com.skilrock.lms.scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.Utility;

public class AuditScriptSchedulerJob implements Job {
	Logger logger = LoggerFactory.getLogger(DailyLogoutAllRetJob.class);

	@Override
//	public void execute(JobExecutionContext context)
//			throws JobExecutionException {
////		String jobName = context.getJobDetail().getFullName();
////		logger.info("SimpleJob says: " + jobName + " executing at "
////				+ new java.util.Date());
//		try {
////			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
////			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
//
//			Runtime rt = Runtime.getRuntime();
//			Process pr;
//			pr = rt.exec("python /home/stpl/jboss-4.2.2.GA/server/default/deploy/LMSLinuxNew.war/WEB-INF/classes/config/Audit_Script.py");
//
//			// retrieve output from python script
//			BufferedReader bfr = new BufferedReader(new InputStreamReader(
//					pr.getInputStream()));
//			String line = "";
//			while ((line = bfr.readLine()) != null) {
//				// display each output line form python script
//				System.out.println(line);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("Audit Script Activity Job says: " + jobName + " executing at " + new java.util.Date());
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("DAILY_AUDIT_ACTIVITY_SCRIPT").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("DAILY_AUDIT_ACTIVITY_SCRIPT").getJobId());
						Runtime rt = Runtime.getRuntime();
						Process pr;
						String path = "python " + Utility.getPropertyValue("JBOSS_PATH");
//						pr = rt.exec("python /home/jboss-4.2.2.GA/server/default/deploy/LMSLinuxNew.war/WEB-INF/classes/config/Audit_Script.py");
						pr = rt.exec(path);

						// retrieve output from python script
						BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
						String line = "";
						while ((line = bfr.readLine()) != null) {
							// display each output line form python script
							logger.error("Audit Script Error", line);
//							System.out.println(line);
						}
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("DAILY_AUDIT_ACTIVITY_SCRIPT").getJobId());
					} catch (Exception e) {
						logger.error("Exception in DAILY_AUDIT_ACTIVITY_SCRIPT ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("DAILY_AUDIT_ACTIVITY_SCRIPT").getJobId(), errorMsg);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in Daily Job Scheduler  ", e);
		}
	}
}
