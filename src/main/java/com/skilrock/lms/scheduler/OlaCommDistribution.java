package com.skilrock.lms.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ManualRequest;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.ola.OlaCommissionHelper;

public class OlaCommDistribution implements Job {
	private static	Log logger = LogFactory.getLog(OlaRummyWithRequest.class);
	public OlaCommDistribution() {
	}
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException{
		try {			
			
			
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("OLA_Comm_Distribution_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("OLA_Comm_Distribution_SCHEDULER").getJobId());
						ServletContext sc = AutoQuartzMain.scx;
						String commUpdateType = (String) sc
								.getAttribute("OLA_COMM_UPDATE_TYPE");
						OlaCommissionHelper olaCommissionHelper = new OlaCommissionHelper();
						//int walletId = 2;
						String netGamingApprovalUpdateMode = (String)sc.getAttribute("approveNetGamingUpdateMode");
						HttpServletRequest request = new ManualRequest();
						ServletActionContext.setRequest(request);
						
						if(netGamingApprovalUpdateMode.equalsIgnoreCase("AUTO"))
						{
						if ("MONTHLY".equalsIgnoreCase(commUpdateType)) {
							logger.info("Monthly update");
							String jobName = context.getJobDetail().getFullName();
							logger.info("Monthly Job says: " + jobName
									+ " executing at " + new java.util.Date());
							String dateString = getStartNEndDates(commUpdateType);
							Calendar calStart = Calendar.getInstance();
							Calendar calEnd = Calendar.getInstance();
							SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
							Date startDate = frmt.parse(dateString);
							Date endDate = frmt.parse(new java.sql.Date(new Date().getTime()) + "");
							calStart.setTime(startDate);
							calEnd.setTime(endDate);
							calEnd.add(Calendar.DAY_OF_MONTH,-2);
							while (calStart.compareTo(calEnd) < 0)
							{   
								java.sql.Date newStartDate = new java.sql.Date(calStart
										.getTime().getTime());
								calStart.add(Calendar.MONTH,1);
								// calStart.add(Calendar.DAY_OF_MONTH, -1);
								java.sql.Date newEndDate = new java.sql.Date(calStart
										.getTime().getTime());
								olaCommissionHelper.updateRetailerCommissionDetail(
										newStartDate, newEndDate, netGamingApprovalUpdateMode,"MONTHLY");
								calStart.add(Calendar.DAY_OF_MONTH, 1);
							}
							logger.info("entering into getMonthlyNetGamingActivity.......");
							logger.info("successfully done");
						}
						if ("WEEKLY".equalsIgnoreCase(commUpdateType)) {
							logger.info("weekly Scheduler for Net Gaming");
							String jobName = context.getJobDetail().getFullName();
							logger.info("Weekly Job says: " + jobName
									+ " executing at " + new java.util.Date());
							String dateString = getStartNEndDates(commUpdateType);
							Calendar calStart = Calendar.getInstance();
							Calendar calEnd = Calendar.getInstance(); 
							SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
							Date startDate = frmt.parse(dateString);
							Date endDate = frmt.parse(new java.sql.Date(new Date().getTime()) + "");
							calStart.setTime(startDate);
							calEnd.setTime(endDate);
							calEnd.add(Calendar.DAY_OF_MONTH,-2);
							while (calStart.compareTo(calEnd) < 0)
							{
								java.sql.Date newStartDate = new java.sql.Date(calStart
										.getTime().getTime());
								calStart.add(Calendar.DAY_OF_MONTH, 6);
								java.sql.Date newEndDate = new java.sql.Date(calStart
										.getTime().getTime());
								olaCommissionHelper.updateRetailerCommissionDetail(
										newStartDate, newEndDate, netGamingApprovalUpdateMode,"WEEKLY");
								calStart.add(Calendar.DAY_OF_MONTH, 1);
							}
							logger.info("entering into getWeeklyNetGamingActivity.......");
							logger.info("successfully done");
						}
						}
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("OLA_Comm_Distribution_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in OLA_Comm_Distribution_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("OLA_Comm_Distribution_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}

	}

	public String getStartNEndDates(String type) {
		Connection con = DBConnect.getConnection();
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			con.setAutoCommit(false);
			PreparedStatement pStatement = con
					.prepareStatement("SELECT end_date FROM st_ola_agt_ret_commission ORDER BY transaction_id DESC LIMIT 1");
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next()) {
				startDate = rSet.getDate("end_date");
			}
			Calendar calStart = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date currDate = new java.util.Date(calStart
					.getTimeInMillis());
		
			if ("MONTHLY".equalsIgnoreCase(type)) {	
						if(startDate!=null)
						{
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH, 1);
						}
						else
						{
							calStart.setTime(currDate);	
							calStart.add(Calendar.DAY_OF_MONTH,-2);	
							calStart.add(Calendar.MONTH,-1);
						}
				
						sb.append(sdf.format(calStart.getTimeInMillis()));
			}

			if ("WEEKLY".equalsIgnoreCase(type)) {
						if(startDate!=null)
						{
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH,1);
							
						}
						else
						{
							calStart.setTime(currDate);
							calStart.add(Calendar.DAY_OF_MONTH,-2);	
							calStart.add(Calendar.DAY_OF_MONTH, -7);
						}
						sb.append(sdf.format(calStart.getTimeInMillis()));
			}
			
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	

}
