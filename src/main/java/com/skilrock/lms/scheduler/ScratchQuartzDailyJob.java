package com.skilrock.lms.scheduler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.serviceImpl.ScratchInvoiceServiceImpl;

public class ScratchQuartzDailyJob implements Job {

	private static Log logger = LogFactory.getLog(ScratchQuartzDailyJob.class);

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public ScratchQuartzDailyJob() {
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
		// Connection con =null;
		// Statement stmt =null;
		// ResultSet rs =null;
		try {
			String jobName = context.getJobDetail().getFullName();

			logger.info("Daily Job says: " + jobName + " executing at " + new java.util.Date());
			// con =DBConnect.getConnection();
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			// con.setAutoCommit(false);
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("SCRATCH_INVOICE_GENERATE_SCHEDULER").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper.updateSchedulerStart(scheBeanMap.get("SCRATCH_INVOICE_GENERATE_SCHEDULER").getJobId());
						new ScratchInvoiceServiceImpl().generateScratchInvoiceAndDeductBalance();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("SCRATCH_INVOICE_GENERATE_SCHEDULER").getJobId());
					} catch (Exception e) {
						logger.error("Exception in Responsible_Gaming_Daily_SCHEDULER ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper.updateSchedulerError(scheBeanMap.get("SCRATCH_INVOICE_GENERATE_SCHEDULER").getJobId(), errorMsg);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in Daily Job Scheduler  ", e);
		}
	}
}