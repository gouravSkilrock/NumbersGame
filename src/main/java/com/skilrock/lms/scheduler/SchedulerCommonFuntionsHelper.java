package com.skilrock.lms.scheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class SchedulerCommonFuntionsHelper {
	private static Log logger = LogFactory.getLog(SchedulerCommonFuntionsHelper.class);

public static void insertSchedulerGroupHistory(String groupName) throws LMSException{
		Connection con =null;
		Statement stmt =null;
		try{
			con =DBConnect.getConnection();
			con.setAutoCommit(false);
			String qry = "insert into st_lms_scheduler_history(scheId,status,last_start_time,last_end_time,last_status,status_msg,record_insertion_time) select id,status,last_start_time,last_end_time,last_status,status_msg,now() recordTime from st_lms_scheduler_master    where jobGroup='"+groupName+"'";
			logger.info("Insert Scheduler History  "+qry);
			stmt = con.createStatement();
			int rowInserted =stmt.executeUpdate(qry);
			con.commit();
			logger.info("Row inserted: "+rowInserted);
			
		}catch (SQLException e) {
			logger.error("SQL Exception ",e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			logger.error("Exception ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
			DBConnect.closeStmt(stmt);
		}
		
	}

public static void updateSchedulerStart(int jobId) throws LMSException{
	Connection con =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		updateSchedulerStart(jobId, con);
		con.commit();
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
	}
}
public static void updateSchedulerStart(int jobId, Connection con) throws LMSException{
	Statement stmt =null;
	try{

		
		String qry = "update st_lms_scheduler_master set last_start_time = now(), last_status='RUNNING' where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeStmt(stmt);
	}
	
}

public static void updateSchedulerEnd(int jobId) throws LMSException{
	Connection con =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		updateSchedulerEnd(jobId, con);
		con.commit();
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
	}
}
public static void updateSchedulerEnd(int jobId, Connection con) throws LMSException{
	Statement stmt =null;
	try{

	
	

		String qry = "update st_lms_scheduler_master set last_end_time = now() , status_msg = 'Success',last_status='DONE',last_success_time=now()  where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
	
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeStmt(stmt);
	}
	
}

public static void updateSchedulerError(int jobId,String errorMsg) throws LMSException{
	Connection con =null;
	Statement stmt =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		String qry = "update st_lms_scheduler_master set last_end_time = now() , status_msg = '"+errorMsg+"' , last_status='ERROR' where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
		con.commit();
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
		DBConnect.closeStmt(stmt);
	}
	
}

public static 	Map<String,SchedulerDetailsBean> getSchedulerBeanMap(String jobGroup) throws LMSException{
	Connection con =null;
	Statement stmt =null;
	ResultSet rs =null;
	Map<String,SchedulerDetailsBean> scheBeanMap = new HashMap<String ,SchedulerDetailsBean>();
	try{
		con =DBConnect.getConnection();
		stmt = con.createStatement();
		
		String qry = "select id,dev_name,status from st_lms_scheduler_master where jobGroup ='"+jobGroup+"'";
		rs = stmt.executeQuery(qry);
		
		while (rs.next()) {
			SchedulerDetailsBean scheDetailsBean = new SchedulerDetailsBean();
			scheDetailsBean.setActive(rs.getString("status").equalsIgnoreCase("ACTIVE"));
			scheDetailsBean.setJobId(rs.getInt("id"));
			scheDetailsBean.setJobDevName(rs.getString("dev_name"));
			scheBeanMap.put(rs.getString("dev_name"),scheDetailsBean );
		}
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeCon(con);
		DBConnect.closeRs(rs);
		DBConnect.closeStmt(stmt);
	}
	return scheBeanMap ;
	
}

public  static void printAllScheduledJobDetails(Scheduler sched){
	
	try{
		for (String groupName : sched.getJobGroupNames()) {
			 
			//loop all jobs by groupname
			for (String jobName : sched.getJobNames(groupName)) {
		 
		          //get job's trigger
				if(!sched.getJobDetail(jobName, groupName).isDurable()){
					Trigger[] triggers = sched.getTriggersOfJob(jobName,groupName);
					  Date nextFireTime = (Date) triggers[0].getNextFireTime();
					  
					  
					  logger.info("[triggername] : " + triggers[0].getFullName() + " [triggerGroupName] : "
								+ triggers[0].getGroup() + " - " + nextFireTime+"-Job Class"+sched.getJobDetail(jobName, groupName).getJobClass());
				}
				
			  
			  
				logger.info("[jobName] : " + jobName + " [groupName] : "
						+ groupName +"-Job Class"+sched.getJobDetail(jobName, groupName).getJobClass()+"desc"+sched.getJobDetail(jobName, groupName).getDescription());
			}
		 
		    }
	}catch (Exception e) {
		logger.error("Error In Printing Scheduled Jobs", e);
	}
	
	
}

public static String getStartDateForLastSuccessfulScheduler(int jobId,Connection con) throws LMSException{
	String startDate=null;
	Statement stmt =null;
	ResultSet rs =null;
	String qry=null;
	SimpleDateFormat dateFormat = null;
	try{
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		qry="SELECT last_start_time from st_lms_scheduler_history WHERE last_status='DONE' AND scheId="+jobId+" ORDER BY last_start_time desc limit 1";
		stmt=con.createStatement();
		rs=stmt.executeQuery(qry);
		if(rs.next()){
			startDate=dateFormat.format(rs.getTimestamp("last_start_time"));
		}
		else{
			logger.error("************Scheduler Info Not In History Table***********");
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}catch (Exception e) {
		e.printStackTrace();
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	} finally{
		DBConnect.closeConnection(stmt, rs);
	}
	return startDate;
}

	
}
