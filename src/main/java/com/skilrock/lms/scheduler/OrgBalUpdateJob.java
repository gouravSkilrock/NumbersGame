package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.coreEngine.accMgmt.common.OrgBalanceUpdateHelper;

public class OrgBalUpdateJob implements Job {

	private static Log logger = LogFactory.getLog(OrgBalUpdateJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public OrgBalUpdateJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getFullName();

			logger.info("Org Bal Update Job says: " + jobName + " executing at " + new java.util.Date());
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("Organization_Balance_Update_Scheduler").isActive()) {
					int jobId = 0;
					String errorMsg = null;
					try {
						jobId = scheBeanMap.get("Organization_Balance_Update_Scheduler").getJobId();
						logger.info("Organization_Balance_Update_Scheduler Start ...");
						SchedulerCommonFuntionsHelper.updateSchedulerStart(jobId);
						new OrgBalanceUpdateHelper().inserOpeningBalForAgentAndRetailer();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(jobId);
						logger.info("Organization_Balance_Update_Scheduler End ...");
					} catch (Exception e) {
						logger.error("Exception in Organization_Balance_Update_Scheduler - ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(jobId, errorMsg);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in Daily Job Scheduler  ", e);
		}
	}

}