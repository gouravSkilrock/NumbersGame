package com.skilrock.lms.scheduler;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.updateLedgerHelper;

public class QuartzJobUpdateLedgerBalance implements Job {

	private static Log logger = LogFactory.getLog(QuartzJobUpdateLedgerBalance.class);

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		LedgerHelper ledgerHelper = new LedgerHelper();
		try {
			List<Integer> orgidList = CommonMethods.getAgentOrgIdList();
			ledgerHelper.ledgerBoEntry(new Timestamp(cal.getTimeInMillis()));
			/*
			 * for(int i = 6; i<8; i++) { cal.set(Calendar.MONTH, i);
			 * ledgerHelper.ledgerBoEntry(new Timestamp(cal.getTimeInMillis()));
			 * for (Integer orgId : orgidList) { LedgerHelper ledgerHelperAgt =
			 * new LedgerHelper(); //ledgerHelperAgt.ledgerAgentEntry(new
			 * Timestamp(cal.getTimeInMillis()), orgId); } }
			 */
		} catch (LMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Quartz requires a public empty constructor so that the scheduler can
	 * instantiate the class whenever it needs.
	 */
	public QuartzJobUpdateLedgerBalance() {
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


		
		LedgerHelper ledgerHelper = new LedgerHelper();
		Calendar cal =Calendar.getInstance();
		
		try{
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Quartz_UpdateLedgerBalance_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Quartz_UpdateLedgerBalance_SCHEDULER").getJobId());
						System.out
						.println("============  BO ledger balance  updation completed ======== "
								+ cal.getTime());
				//ledgerHelper.ledgerBoEntry(new Timestamp(cal.getTimeInMillis()));
				System.out
						.println("============  BO ledger balance  updation completed ======== "
								+ "\n ============  BO ledger balance  updation completed ======== "
								+ cal.getTime());

				/*List<Integer> orgidList = CommonMethods.getAgentOrgIdList();
				for (Integer orgId : orgidList) {
					LedgerHelper ledgerHelperAgt = new LedgerHelper();
					ledgerHelperAgt.ledgerAgentEntry(new Timestamp(cal
							.getTimeInMillis()), orgId);
				}*/
				System.out
						.println("============ ledger balance updation completed ======== ");

				System.out
						.println("============Claimable Balance Updation ======== ");
				//new CommonFunctionsHelper().updateClmableBalOfOrgList();
				//updateLedgerHelper.updateLedger();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Quartz_UpdateLedgerBalance_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Auto_Quartz_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Quartz_UpdateLedgerBalance_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
	
		}catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
		
	
		
		
		
	

		
	}

}