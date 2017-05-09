package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.LSControllerImpl;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.accMgmt.common.CalculateGovCommAmtScheduler;
import com.skilrock.lms.web.drawGames.common.Util;

public class ManageGoodCauseScheduleJob implements Job {
	private static Log logger = LogFactory
			.getLog(ManageGoodCauseScheduleJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("ManageGoodCauseScheduleJob Start At - "
				+ Util.getCurrentTimeString());

		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("Job Name - " + jobName);

			Map<String, SchedulerDetailsBean> schedulerMap = SchedulerCommonFuntionsHelper
					.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context
					.getJobDetail().getGroup());
			if (schedulerMap.size() > 0
					&& schedulerMap.get("Manage_Good_Cause_Scheduler")
							.isActive()) {

				String errorMsg = null;

				try {
					SchedulerCommonFuntionsHelper
							.updateSchedulerStart(schedulerMap.get(
									"Manage_Good_Cause_Scheduler").getJobId());
					new CalculateGovCommAmtScheduler().calculateGovernmentCommission();
					SchedulerCommonFuntionsHelper
							.updateSchedulerEnd(schedulerMap.get(
									"Manage_Good_Cause_Scheduler").getJobId());
				} catch (Exception e) {
					logger
							.error("Exception in Manage_Good_Cause_Scheduler ",
									e);
					if (e.getMessage() != null) {
						errorMsg = e.getMessage();
					} else {

						errorMsg = "Error Occurred Msg Is Null ";
					}
				}
				if (errorMsg != null) {
					SchedulerCommonFuntionsHelper.updateSchedulerError(
							schedulerMap.get("Manage_Good_Cause_Scheduler")
									.getJobId(), errorMsg);
				}

			}

		} catch (LMSException e) {
			logger.error(
					"LMSException LicensingServerScheduleJob Scheduler - ", e);
		} catch (Exception e) {
			logger.error(
					"Exception in LicensingServerScheduleJob Scheduler - ", e);
		}

		logger.info("LicensingServerScheduleJob End At - "
				+ Util.getCurrentTimeString());
	}
}