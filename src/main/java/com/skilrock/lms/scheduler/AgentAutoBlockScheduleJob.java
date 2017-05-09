package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.serviceImpl.AgentAutoBlockServiceImpl;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentAutoBlockScheduleJob implements Job {
	private static Log logger = LogFactory.getLog(AgentAutoBlockScheduleJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("AgentAutoBlockScheduleJob Start At - " + Util.getCurrentTimeString());

		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("Job Name - "+jobName);

			Map<String, SchedulerDetailsBean> schedulerMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(schedulerMap.size()>0 && schedulerMap.get("Agent_Auto_Block_SCHEDULER").isActive()) {
				String errorMsg = null;
				try {
					SchedulerCommonFuntionsHelper.updateSchedulerStart(schedulerMap.get("Agent_Auto_Block_SCHEDULER").getJobId());
					AgentAutoBlockServiceImpl.getInstance().autoBlock();
					SchedulerCommonFuntionsHelper.updateSchedulerEnd(schedulerMap.get("Agent_Auto_Block_SCHEDULER").getJobId());
				} catch(Exception ex) {
					logger.error("Exception in Agent_Auto_Block_SCHEDULER - ", ex);
					errorMsg = (ex.getMessage() != null) ? errorMsg = ex.getMessage() : "Error Occurred Messge is Null.";
				}

				if(errorMsg != null) {
					SchedulerCommonFuntionsHelper.updateSchedulerError(schedulerMap.get("Agent_Auto_Block_SCHEDULER").getJobId(), errorMsg);
				}
			}
		} catch (LMSException e) {
			logger.error("LMSException AgentAutoBlockScheduleJob Scheduler - ", e);
		} catch (Exception e) {
			logger.error("Exception in AgentAutoBlockScheduleJob Job Scheduler - ", e);
		}

		logger.info("AgentAutoBlockScheduleJob End At - " + Util.getCurrentTimeString());
	}
}