package com.skilrock.lms.scheduler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ManualRequest;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithRequestHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.updateLedgerHelper;

public class QuartzClmBalUpdateJob implements Job {

	private static Log logger = LogFactory.getLog(QuartzClmBalUpdateJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public QuartzClmBalUpdateJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with
	 * the <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		
		try {
			

			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Quartz_ClmBalUpdate_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Quartz_ClmBalUpdate_SCHEDULER").getJobId());
						HttpServletRequest request = new ManualRequest();
						request.setAttribute("code", "MGMT");
						request.setAttribute("interfaceType", "WEB");
						logger.info(request.getAttribute("code") + "*********test6*********"+ request.getAttribute("interfaceType"));

						ServletActionContext.setRequest(request);
						updateLedgerHelper.updateLedger();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Quartz_ClmBalUpdate_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Quartz_ClmBalUpdate_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Quartz_ClmBalUpdate_SCHEDULER").getJobId(), errorMsg);
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
			
		
			
			
			
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}