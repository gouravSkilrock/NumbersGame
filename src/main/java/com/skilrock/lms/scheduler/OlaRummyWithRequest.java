package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithRequestHelper;
import com.skilrock.lms.web.loginMgmt.AutomaticArchiving;
/**
 * This Class performs job of Withdrawal Request processing 
 * @author Neeraj Jain
 *
 */
public class OlaRummyWithRequest implements Job{
private static	Log logger = LogFactory.getLog(OlaRummyWithRequest.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
	
		try{
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("OLA_RummyWithRequest_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("OLA_RummyWithRequest_SCHEDULER").getJobId());
						logger.info("--RMS Withdrawal Request Processing Started....");
						 OlaRummyWithRequestHelper.processWithRequest();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("OLA_RummyWithRequest_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in OLA_RummyWithRequest_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("OLA_RummyWithRequest_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
			/*ServletContext servletContext = LMSUtility.sc;
			String  isAutoArch =(String)servletContext.getAttribute("autoArchiving"); 
			logger.info("Auto Archiving"+isAutoArch);
			System.out.println("Auto Archiving");
			if(isAutoArch.equalsIgnoreCase("true")){
				logger.info("Inside Auto Archiving");
					startArchiving();	
			}else{
				logger.info("System is not configured for the automatic archiving");
			}*/
			
		}catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
		
	}
}
