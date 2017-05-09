package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.scheduler.controllerImpl.DailySecurityNLevySettlementControllerImpl;
import com.skilrock.lms.web.drawGames.common.Util;

public class DailySecurityNLevySettlementJob implements Job {
	Log logger = LogFactory.getLog(DailyLogoutAllRetJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "+ new java.util.Date());
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("DailyUpdateLevyNSecuityDeposit_SCHEDULER").isActive()) {
					String errorMsg = null;
					String endDate=null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("DailyUpdateLevyNSecuityDeposit_SCHEDULER").getJobId());
						endDate=Util.getCurrentTimeString();
						logger.info("Daily levy and security deposit settelement scheduler started");
						DailySecurityNLevySettlementControllerImpl.getInstance().settleLevynNSecurityDeposit(scheBeanMap,endDate);
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("DailyUpdateLevyNSecuityDeposit_SCHEDULER").getJobId());
						logger.info("Daily levy and security deposit settelement scheduler Ends");
					} catch (LMSException e) {
						logger.error("LMSException in DailyUpdateLevyNSecuityDeposit_SCHEDULER");
						errorMsg = e.getErrorMessage();
					} catch (Exception e1) {
						logger.error("Exception in DailyUpdateLevyNSecuityDeposit_SCHEDULER",e1);
						errorMsg = e1.getMessage();
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("DailyUpdateLevyNSecuityDeposit_SCHEDULER").getJobId(), errorMsg);
					}
				}
			}
		} catch (LMSException e) {
			logger.error("LMSException in daily levy and security deposit settelement scheduler",e);
		} catch (Exception e) {
			logger.error("Exception in daily levy and security deposit settelement scheduler",e);
		}
	}

}