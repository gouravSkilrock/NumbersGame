package com.skilrock.lms.scheduler;

import java.util.Map;

import org.quartz.Job;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.admin.SetResetUserPasswordAction;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;

public class DailyLogoutAllRetJob  implements Job{
	
Log logger  = LogFactory.getLog(DailyLogoutAllRetJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			

			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap( context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("DailyLogoutAllRetJob_SCHEDULER").isActive()){
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("DailyLogoutAllRetJob_SCHEDULER").getJobId());
						logger.info("Daily Logout All Ret Scheduler Started");
						new SetResetUserPasswordAction().logOutAllRets();			
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("DailyLogoutAllRetJob_SCHEDULER").getJobId());
					}catch (LMSException e) {
						logger.error("LMSException in DailyLogoutAllRetJob_SCHEDULER ");
						errorMsg =e.getMessage();
					}catch (Exception e) {
						logger.error("Exception in DailyLogoutAllRetJob_SCHEDULER ", e);
						errorMsg =e.getMessage();
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("DailyLogoutAllRetJob_SCHEDULER").getJobId(), errorMsg);
					}
				}
			}
		} catch (LMSException e) {
			logger.error("LMSException in Daily Logout All Ret Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Daily Logout All Ret Scheduler  ", e);
		}
	}

}
