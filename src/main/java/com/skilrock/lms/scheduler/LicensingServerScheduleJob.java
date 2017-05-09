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
import com.skilrock.lms.web.drawGames.common.Util;

public class LicensingServerScheduleJob implements Job {
	private static Log logger = LogFactory.getLog(LicensingServerScheduleJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("LicensingServerScheduleJob Start At - " + Util.getCurrentTimeString());

		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("Job Name - "+jobName);

			Map<String, SchedulerDetailsBean> schedulerMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(schedulerMap.size()>0 && schedulerMap.get("Licensing_Server_SCHEDULER").isActive()) {
				String errorMsg = null;
				try {
					SchedulerCommonFuntionsHelper.updateSchedulerStart(schedulerMap.get("Licensing_Server_SCHEDULER").getJobId());
					LSControllerImpl.getInstance().clientValidation();
					SchedulerCommonFuntionsHelper.updateSchedulerEnd(schedulerMap.get("Licensing_Server_SCHEDULER").getJobId());
				} catch(Exception ex) {
					logger.error("Exception in Licensing_Server_SCHEDULER - ", ex);
					errorMsg = (ex.getMessage() != null) ? errorMsg = ex.getMessage() : "Error Occurred Messge is Null.";
				}

				if(errorMsg != null) {
					SchedulerCommonFuntionsHelper.updateSchedulerError(schedulerMap.get("Licensing_Server_SCHEDULER").getJobId(), errorMsg);
				}
			}
		} catch (LMSException e) {
			logger.error("LMSException LicensingServerScheduleJob Scheduler - ", e);
		} catch (Exception e) {
			logger.error("Exception in LicensingServerScheduleJob Scheduler - ", e);
		}

		logger.info("LicensingServerScheduleJob End At - " + Util.getCurrentTimeString());
	}
}