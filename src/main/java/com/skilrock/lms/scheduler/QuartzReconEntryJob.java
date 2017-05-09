package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.reportsMgmt.common.ReconcilationReportAction;

public class QuartzReconEntryJob implements Job {

	private static Log logger = LogFactory.getLog(QuartzReconEntryJob.class);

	public QuartzReconEntryJob() {

	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {

			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Quartz_ReconEntry_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Quartz_ReconEntry_SCHEDULER").getJobId());


						
						new ReconcilationReportAction().quartzEntry();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Quartz_ReconEntry_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Quartz_ReconEntry_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Quartz_ReconEntry_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
		
			
		

		} catch (LMSException e) {
			e.printStackTrace();
		}
	}

}
