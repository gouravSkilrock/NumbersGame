package com.skilrock.lms.scheduler;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.coreEngine.virtualSport.scheduler.VirtualSportReconciliationScheduler;

public class VSSaleReconciliationScheduler implements Job {
	Logger logger = LoggerFactory.getLogger(VSSaleReconciliationScheduler.class);

	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("VS Sale Reconciliation Scheduler Job says: " + jobName + " executing at " + new java.util.Date());
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if(scheBeanMap.get("VS_Sale_Reconciliation_Scheduler").isActive()){
					logger.info("Starting VS_Sale_Reconciliation_Scheduler. ");
					SchedulerCommonFuntionsHelper
					.updateSchedulerStart(scheBeanMap.get(
							"VS_Sale_Reconciliation_Scheduler").getJobId());
					new VirtualSportReconciliationScheduler().settleVSSaleTransactions();
					logger.info("Ending VS_Sale_Reconciliation_Scheduler. ");
					SchedulerCommonFuntionsHelper
					.updateSchedulerEnd(scheBeanMap.get(
							"VS_Sale_Reconciliation_Scheduler").getJobId());
				}
			}
		
		} catch (Exception e) {
			logger.error("Exception in VS_Sale_Reconciliation_Scheduler  ", e);
		}
	}
}
