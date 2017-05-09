package com.skilrock.lms.scheduler;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.web.reportsMgmt.common.TallyXMLFilesDailyScheduleHelper;

public class TallyXmlCashBankDailySchedulerJob implements Job {
	Logger logger = LoggerFactory.getLogger(TallyXmlCashBankDailySchedulerJob.class);

	@Deprecated
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("Tally Xml Cash Bank Daily Scheduler Job says: " + jobName + " executing at " + new java.util.Date());
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if(scheBeanMap.get("Tally_XML_CashColBankDeposit_Scheduler").isActive()){
					int jobId = 0;
					String errorMsg = null;
					jobId = scheBeanMap.get("Tally_XML_CashColBankDeposit_Scheduler").getJobId();
					logger.info("Starting Tally_XML_CashColBankDeposit_Scheduler. ");
					//errorMsg=TallyXMLFilesDailyScheduleHelper.perform(jobId);
					logger.info("Ending Tally_XML_CashColBankDeposit_Scheduler. ");
					if(errorMsg!=null)
						SchedulerCommonFuntionsHelper.updateSchedulerError( jobId, errorMsg);
					else
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(jobId);
				}
			}
		
		} catch (Exception e) {
			logger.error("Exception in TallyXmlCashBankDailySchedulerJob  ", e);
		}
	}
}
