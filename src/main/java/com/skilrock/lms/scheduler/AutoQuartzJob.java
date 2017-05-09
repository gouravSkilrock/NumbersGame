package com.skilrock.lms.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class AutoQuartzJob implements Job {
	static Log logger = LogFactory.getLog(AutoQuartzJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		

		
		try{
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Auto_Quartz_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Auto_Quartz_SCHEDULER").getJobId());
						autoQuartz();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Auto_Quartz_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Auto_Quartz_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Auto_Quartz_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
		
			
		}catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
		
	
		
		
		
	}
	
public static void autoQuartz() throws LMSException{
	


	logger.info("Before connection......:" + new Date());

	Connection con = null;

	Date currentDate = new Date();
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	String datetime = dateFormat.format(currentDate);
	logger.info("Todat date is=" + datetime);

	try {
		con = DBConnect.getConnection();
		con.setAutoCommit(false);
		logger.info("Connected to the database1111111111......."
				+ datetime);

		Statement stmt1 = con.createStatement();
		stmt1
				.executeUpdate("update st_se_game_master set game_status='OPEN' where game_status='NEW' and start_date='"
						+ datetime + "'");
		logger.info("game_status is now--NEW---to---OPEN update st_se_game_master set game_status='OPEN' where game_status='NEW' and start_date='"
						+ datetime + "'");

		Statement stmt2 = con.createStatement();
		stmt2
				.executeUpdate("update st_se_game_master set game_status='SALE_HOLD' where game_status='OPEN' and sale_end_date < '"
						+ datetime + "'");
		logger.info("game_status is now--OPEN----to---SALE_HOLD update st_se_game_master set game_status='SALE_HOLD' where game_status='OPEN' and sale_end_date < '"
						+ datetime + "'");

		Statement stmt3 = con.createStatement();
		stmt3
				.executeUpdate("update st_se_game_master set game_status='CLOSE' where game_status='SALE_HOLD' and pwt_end_date < '"
						+ datetime + "'");
		logger.info("game_status is now--SALE_HOLD----to---CLOSE update st_se_game_master set game_status='CLOSE' where game_status='SALE_HOLD' and pwt_end_date < '"
						+ datetime + "'");
		
		//update XCL Scheduler
		Statement stmt6 = con.createStatement();
		ResultSet rsUser=stmt6.executeQuery("select user_id from st_lms_user_master where parent_user_id=0");
		int boUserId=0;
		if(rsUser.next()){
			boUserId=rsUser.getInt("user_id");
		}
		PreparedStatement insertHistoryPstmt=null;
		PreparedStatement selectPstmt=con.prepareStatement("select organization_id,organization_type,parent_id,-(extended_credit_limit) amount ,(available_credit-claimable_bal)live_bal from st_lms_organization_master where  extends_credit_limit_upto<=? and extended_credit_limit > 0.00");
		selectPstmt.setString(1, datetime);
		ResultSet rs=selectPstmt.executeQuery();
		while(rs.next()){
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(rs.getDouble("amount"), "XCL", "CREDIT", rs.getInt("organization_id"),rs.getInt("parent_id"), rs.getString("organization_type"), 0, con);
			if(isValid){
				insertHistoryPstmt=con.prepareStatement("insert into st_lms_cl_xcl_update_history(organization_id,done_by_user_id,date_time,type,amount,updated_value,total_bal_before_update) values(?,?,?,?,?,?,?)");
				insertHistoryPstmt.setInt(1,rs.getInt("organization_id"));
				insertHistoryPstmt.setInt(2, boUserId);
				insertHistoryPstmt.setTimestamp(3, Util.getCurrentTimeStamp());
				insertHistoryPstmt.setString(4, "XCL");
				insertHistoryPstmt.setDouble(5,rs.getDouble("amount"));
				insertHistoryPstmt.setDouble(6, rs.getDouble("live_bal")+rs.getDouble("amount"));
				insertHistoryPstmt.setDouble(7, rs.getDouble("live_bal"));
				insertHistoryPstmt.executeUpdate();
				}else{
					logger.info("There is some problem while reseting XCL of agent: " +rs.getInt("organization_id") );
				}
		}
		
       // insert in cl_xcl_update history
		
/*		String updtCreditXtendedLimitHistory="insert into st_lms_cl_xcl_update_history(organization_id,date_time,type,amount,updated_value,total_bal_before_update) " 
			+"select organization_id,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"','XCL',-(extended_credit_limit),(credit_limit-current_credit_amt-claimable_bal),(available_credit-claimable_bal) from st_lms_organization_master where  extends_credit_limit_upto<='" + datetime + "' and extended_credit_limit > 0.00";
		logger.info("inset Query for st_lms_cl_xcl_update history:"+updtCreditXtendedLimitHistory);
		stmt6.executeUpdate(updtCreditXtendedLimitHistory);
					
		Statement stmt7 = con.createStatement();
		String updateHistoryQuery = "insert into st_lms_bo_extended_limit_history(bo_user_id,agent_org_id,extended_credit_limit,date_changed,extends_credit_limit_upto) select 1001,organization_id,0.0,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',extends_credit_limit_upto from st_lms_organization_master where extends_credit_limit_upto<='" + datetime + "' and extended_credit_limit > 0.00";
		logger.info("insert st_lms_bo_extended_limit_history::"+updateHistoryQuery);
		stmt7.executeUpdate(updateHistoryQuery);
		
	
		
		Statement stmt4 = con.createStatement();
		stmt4
				.executeUpdate("update st_lms_organization_master set extended_credit_limit=0.0,available_credit=available_credit-xcl (credit_limit-current_credit_amt) where  extends_credit_limit_upto<='"
						+ datetime + "'");
		System.out
				.println("update st_lms_organization_master set extended_credit_limit=0.0,available_credit=(credit_limit-current_credit_amt) where  extends_credit_limit_upto<='"
						+ datetime + "'");
		
		Statement stmt8 = con.createStatement();
		stmt8
				.executeUpdate("insert into st_lms_organization_master_history select '1001',organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'INACTIVE_AUTO_ACT','NEW STATUS','INACTIVE','"+new java.sql.Timestamp(new java.util.Date().getTime()).toString()+"',pwt_scrap,recon_report_type from st_lms_organization_master where (credit_limit-current_credit_amt-claimable_bal)<0 and organization_status='ACTIVE'");
		
		
		Statement stmt5 = con.createStatement();
		stmt5
				.executeUpdate("update st_lms_organization_master set organization_status='INACTIVE' where  (available_credit-claimable_bal)<=0.0 and organization_type!='BO' and organization_status !='TERMINATE'");

		stmt5
				.executeUpdate("update st_lms_organization_master set organization_status='ACTIVE' where  (available_credit-claimable_bal)>0.0 and organization_type!='BO' and organization_status !='TERMINATE'");
*/
		con.commit();

	} catch (SQLException e) {
		logger.info("SQL Exception ",e);
		throw new LMSException("SQL Exception "+e.getMessage());
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException("Exception"+e.getMessage());
	} finally {
		DBConnect.closeCon(con);
	}

	
}	
}
