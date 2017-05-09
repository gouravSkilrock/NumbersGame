package com.skilrock.lms.scheduler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.admin.common.GenSecurityCodeHelper;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;

public class GenerateMappingIds  implements Job{
	
Log logger  = LogFactory.getLog(DailyLogoutAllRetJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "	+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap( context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("GenMapIdweeklyCronExpr_SCHEDULER").isActive()){
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("GenMapIdweeklyCronExpr_SCHEDULER").getJobId());
						int doneByUserId = 11001;
						boolean isGenPlaceLMS = "true".equalsIgnoreCase(com.skilrock.lms.common.Utility.getPropertyValue("MAPPING_ID_GEN_BY_THIRD_PARTY").trim());
						int noOfExpDays = Integer.parseInt(com.skilrock.lms.common.Utility.getPropertyValue("USER_MAPPING_ID_EXPIRY").trim());
						new GenSecurityCodeHelper().checkAndGenerateNewSecurityCode(0, doneByUserId, /*codeExpiryDays,*/ true, isGenPlaceLMS, noOfExpDays, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())), "AUTO_GEN");
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("GenMapIdweeklyCronExpr_SCHEDULER").getJobId());
					}catch (LMSException e) {
						logger.error("LMSException in GenMapIdweeklyCronExpr_SCHEDULER ");
						errorMsg =e.getMessage();
					}catch (Exception e) {
						logger.error("Exception in GenMapIdweeklyCronExpr_SCHEDULER ", e);
						errorMsg =e.getMessage();
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("GenMapIdweeklyCronExpr_SCHEDULER").getJobId(), errorMsg);
					}
				}
			}
		} catch (LMSException e) {
			logger.error("LMSException in GenMapIdweeklyCronExpr SCHEDULER  ", e);
		}catch (Exception e) {
			logger.error("Exception in GenMapIdweeklyCronExpr SCHEDULER  ", e);
		}
	}

}
