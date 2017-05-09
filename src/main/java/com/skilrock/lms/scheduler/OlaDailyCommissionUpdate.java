package com.skilrock.lms.scheduler;


import javax.servlet.ServletContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
public class OlaDailyCommissionUpdate implements Job {

	public OlaDailyCommissionUpdate() {
	}
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {			
			ServletContext sc = AutoQuartzMain.scx;
		
			String rootPath = sc.getRealPath("/").toString();
			//int walletId = 2;
			
				//This Scheduler must be run after 12'o clock in the night
				CommonFunctionsHelper.getNUpdatePlrCommForAllRetailers(rootPath);
			}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
}
