package com.skilrock.lms.admin.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ICSDailyQueryStatusBean;
import com.skilrock.lms.beans.ICSQueryMasterBean;
import com.skilrock.lms.beans.ICSQueryStatusReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.web.drawGames.common.Util;

public class ICSManagementHelper {
	private static final Log logger = LogFactory.getLog(ICSManagementHelper.class);

	public List<ICSQueryStatusReportBean> getIcsQueryStatusData(Timestamp startTime, Timestamp endTime) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<ICSQueryStatusReportBean> icsReportList = new ArrayList<ICSQueryStatusReportBean>();
		ICSQueryStatusReportBean icsReportBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("SELECT qs.query_id, query_title, qurey_description, expected_result, actual_result, ics_run_date, run_by " +
					"FROM st_ics_daily_query_status qs INNER JOIN st_ics_query_master qm ON qs.query_id=qm.query_id " +
					"AND ics_run_date>=? AND ics_run_date<=? AND is_success='NO';");
			pstmt.setTimestamp(1, startTime);
			pstmt.setTimestamp(2, endTime);
			logger.info("ICS Query Status Data Query - "+pstmt);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				icsReportBean = new ICSQueryStatusReportBean();
				icsReportBean.setQueryId(rs.getInt("query_id"));
				icsReportBean.setQueryTitle(rs.getString("query_title"));
				icsReportBean.setQueryDescription(rs.getString("qurey_description"));
				icsReportBean.setExpectedResult(rs.getString("expected_result"));
				icsReportBean.setActualResult(rs.getString("actual_result"));
				icsReportBean.setIcsRunDate(dateFormat.format(rs.getTimestamp("ics_run_date")));
				icsReportBean.setRunBy(rs.getString("run_by"));
				icsReportList.add(icsReportBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(connection);
		}

		return icsReportList;
	}

	public List<ICSQueryMasterBean> getAllQueries() throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ICSQueryMasterBean> icsReportList = new ArrayList<ICSQueryMasterBean>();
		ICSQueryMasterBean icsBean = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			String query = "SELECT query_id, query_title, is_sp, is_date_req, last_successful_date, query_result, qurey_description, " +
					"related_to, tier_type, error_msg, is_critical, query_status, last_updated_date, updated_by " +
					"FROM st_ics_query_master;";
			logger.info("ICS All Queries - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				icsBean = new ICSQueryMasterBean();
				icsBean.setQueryId(rs.getInt("query_id"));
				icsBean.setQueryTitle(rs.getString("query_title").trim());
				icsBean.setIsSP(rs.getString("is_sp"));
				icsBean.setIsDateRequired(rs.getString("is_date_req"));
				String lastSuccessDate = null;
				if(rs.getTimestamp("last_successful_date") == null)
					lastSuccessDate = "";
				else
					lastSuccessDate =Util.getDateTimeFormat(rs.getTimestamp("last_successful_date"));
				icsBean.setLastSuccessfulDate(lastSuccessDate);
				icsBean.setQueryResult(rs.getString("query_result"));
				icsBean.setQueryDescription(rs.getString("qurey_description").trim());
				icsBean.setRelatedTo(rs.getString("related_to"));
				icsBean.setTierType(rs.getString("tier_type"));
				icsBean.setErrorMessage(rs.getString("error_msg"));
				icsBean.setIsCritical(rs.getString("is_critical"));
				icsBean.setQueryStatus(rs.getString("query_status"));
				icsBean.setLastUpdatedDate(rs.getString("last_updated_date"));
				icsBean.setUpdatedBy(rs.getString("updated_by"));
				icsReportList.add(icsBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeCon(connection);
		}

		return icsReportList;
	}

	public List<ICSQueryMasterBean> fetchICSQueryMasterData(String queryIds, Connection connection) {
		List<ICSQueryMasterBean> beans = new ArrayList<ICSQueryMasterBean>();
		ICSQueryMasterBean queryMasterBean = null;
		Statement statement = null;
		ResultSet set = null;
		String appender = "";
		try {
			if(queryIds != null) {
				appender = " AND query_id IN ("+queryIds+")";
			}
			statement = connection.createStatement();
			set = statement.executeQuery("SELECT query_id, query_title, main_query, is_sp, query_result, qurey_description, related_to, tier_type, error_msg, is_critical, query_status " +
					"FROM st_ics_query_master WHERE query_status='ACTIVE'"+appender+" AND related_to NOT IN (SELECT service_code FROM st_lms_service_master WHERE STATUS='INACTIVE');");
			while(set.next())
			{
				queryMasterBean = new ICSQueryMasterBean();
				queryMasterBean.setQueryId(set.getInt("query_id"));
				queryMasterBean.setQueryTitle(set.getString("query_title"));
				queryMasterBean.setMainQuery(set.getString("main_query"));
				queryMasterBean.setIsSP(set.getString("is_sp"));
				queryMasterBean.setQueryResult(set.getString("query_result"));
				queryMasterBean.setQueryDescription(set.getString("qurey_description"));
				queryMasterBean.setRelatedTo(set.getString("related_to"));
				queryMasterBean.setTierType(set.getString("tier_type"));
				queryMasterBean.setErrorMessage(set.getString("error_msg"));
				queryMasterBean.setIsCritical(set.getString("is_critical"));
				queryMasterBean.setQueryStatus(set.getString("query_status"));

				beans.add(queryMasterBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return beans;
	}

	public List<ICSDailyQueryStatusBean> executeICSQuery(String queryIdString, String runBy) throws LMSException {
		SimpleDateFormat simpleDateFormat = null;
		List<ICSQueryMasterBean> queryMasterList = null;
		List<ICSDailyQueryStatusBean> beans = new ArrayList<ICSDailyQueryStatusBean>();
		ICSDailyQueryStatusBean dailyQueryStatusBean = null;

		Connection connection = null;
		Statement statement = null;
		PreparedStatement pstmtQM = null;
		PreparedStatement pstmtQS = null;
		PreparedStatement pstmt = null;
		CallableStatement callableStatement = null;
		ResultSet set = null;

		Date icsStartTime = null;
		Date queryStartTime = null;
		Date queryEndTime = null;
		Date icsEndTime = null;
		long queryExecutionTime = 0L;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			queryMasterList = fetchICSQueryMasterData(queryIdString, connection);

			statement = connection.createStatement();
			pstmtQM = connection.prepareStatement("UPDATE st_ics_query_master SET last_successful_date=? WHERE query_id=?;");
			pstmtQS = connection.prepareStatement("INSERT INTO st_ics_daily_query_status (query_id, expected_result, actual_result, ics_run_date, query_execution_time, is_success, run_by) VALUES (?,?,?,?,?,?,?);");

			icsStartTime = new Date();
			for(ICSQueryMasterBean queryMasterBean : queryMasterList) {
				dailyQueryStatusBean = new ICSDailyQueryStatusBean();
				dailyQueryStatusBean.setQueryId(queryMasterBean.getQueryId());
				dailyQueryStatusBean.setQueryTitle(queryMasterBean.getQueryTitle());
				dailyQueryStatusBean.setQueryDescription(queryMasterBean.getQueryDescription());
				dailyQueryStatusBean.setExpectedResult(queryMasterBean.getQueryResult());
				dailyQueryStatusBean.setIcsRunDate(Util.getCurrentTimeStamp());
				dailyQueryStatusBean.setIsCritical(queryMasterBean.getIsCritical());

				if("YES".equals(queryMasterBean.getIsSP())) {
					callableStatement = connection.prepareCall("CALL "+queryMasterBean.getMainQuery()+"(?);");
					callableStatement.setInt(1, queryMasterBean.getQueryId());
					queryStartTime = new Date();
					callableStatement.executeQuery();
					queryEndTime = new Date();
					queryExecutionTime = (queryEndTime.getTime()-queryStartTime.getTime());
					logger.info("Query Execution Time ("+dailyQueryStatusBean.getQueryId()+") - "+queryExecutionTime);
					dailyQueryStatusBean.setQueryExecutionTime(queryExecutionTime);

					int id = 0;
					pstmt = connection.prepareStatement("SELECT id, actual_result FROM st_ics_daily_query_status WHERE query_id="+queryMasterBean.getQueryId()+" ORDER BY id DESC LIMIT 1;");
					set = pstmt.executeQuery();
					if(set.next()) {
						id = set.getInt("id");
						dailyQueryStatusBean.setActualResult(set.getString("actual_result"));
					}

					pstmt = connection.prepareStatement("UPDATE st_ics_daily_query_status SET query_execution_time=?, run_by=? WHERE id=?;");
					pstmt.setLong(1, queryExecutionTime);
					pstmt.setString(2, runBy);
					pstmt.setInt(3, id);
					pstmt.executeUpdate();

					if((Long.parseLong(dailyQueryStatusBean.getExpectedResult().replaceAll("\\.", ""))) == Long.parseLong((dailyQueryStatusBean.getActualResult().replaceAll("\\.", "")))) {
						dailyQueryStatusBean.setIsSuccess("YES");
						pstmtQM.setTimestamp(1, Util.getCurrentTimeStamp());
						pstmtQM.setInt(2, dailyQueryStatusBean.getQueryId());
						pstmtQM.addBatch();
					} else {
						dailyQueryStatusBean.setIsSuccess("NO");
					}
				}
				else {
					queryStartTime = new Date();
					set = statement.executeQuery(queryMasterBean.getMainQuery());
					queryEndTime = new Date();
					if (set.next())
						dailyQueryStatusBean.setActualResult(set.getString(1));

					queryExecutionTime = (queryEndTime.getTime()-queryStartTime.getTime());
					logger.info("Query Execution Time ("+dailyQueryStatusBean.getQueryId()+") - "+queryExecutionTime);
					dailyQueryStatusBean.setQueryExecutionTime(queryExecutionTime);

					if((Long.parseLong(dailyQueryStatusBean.getExpectedResult().replaceAll("\\.", ""))) == Long.parseLong((dailyQueryStatusBean.getActualResult().replaceAll("\\.", "")))) {
						dailyQueryStatusBean.setIsSuccess("YES");
						pstmtQM.setTimestamp(1, Util.getCurrentTimeStamp());
						pstmtQM.setInt(2, dailyQueryStatusBean.getQueryId());
						pstmtQM.addBatch();
					} else {
						dailyQueryStatusBean.setIsSuccess("NO");
					}

					pstmtQS.setInt(1, dailyQueryStatusBean.getQueryId());
					pstmtQS.setString(2, dailyQueryStatusBean.getExpectedResult());
					pstmtQS.setString(3, dailyQueryStatusBean.getActualResult());
					pstmtQS.setTimestamp(4, dailyQueryStatusBean.getIcsRunDate());
					pstmtQS.setLong(5, dailyQueryStatusBean.getQueryExecutionTime());
					pstmtQS.setString(6, dailyQueryStatusBean.getIsSuccess());
					pstmtQS.setString(7, runBy);
					pstmtQS.executeUpdate();
				}

				beans.add(dailyQueryStatusBean);
			}
			pstmtQM.executeBatch();

			icsEndTime = new Date();
			long icsExecutionTime = (icsEndTime.getTime()-icsStartTime.getTime());
			logger.info("ICS Execution Time - "+icsExecutionTime);
			if("AUTO".equals(runBy)) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				pstmt = connection.prepareStatement("INSERT INTO st_ics_daily_status (ics_datetime, ics_execution_time, is_success) VALUES " +
						"(?,?,(SELECT IF(COUNT(*)>0,'NO','YES') AS is_success FROM st_ics_daily_query_status qs INNER JOIN st_ics_query_master qm ON qs.query_id=qm.query_id " +
						"WHERE is_success='NO' AND is_critical='YES' AND DATE(ics_run_date)=? AND run_by='AUTO'));");
				pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
				pstmt.setLong(2, icsExecutionTime);
				pstmt.setString(3, simpleDateFormat.format(Util.getCurrentTimeStamp().getTime()));
				pstmt.executeUpdate();
			}

			connection.commit();

			/*	Mail Send to Long Queries	*/
			long maxExecutionTime = Long.parseLong(getPropertyValue("MAX_EXECUTION_TIME"));
			if(icsExecutionTime > maxExecutionTime) {
				String messageText = "ICS Total Execution time taken is <b>"+icsExecutionTime+"</b>ms.";
				String mailToString = new ICSManagementHelper().getPropertyValue("MAIL_TO");
				String[] mailToArray = mailToString.split(",");
				MailSend mailSend = null;
				for(String mailTo : mailToArray) {
					mailSend = new MailSend(mailTo.trim(), messageText);
					mailSend.start();
				}
			}
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(connection);
		}

		return beans;
	}

	public void updateIcsQueries(int queryId, String queryDescription, String errorMessage, String isCritical, String status, int userId) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("UPDATE st_ics_query_master SET qurey_description=?, error_msg=?, is_critical=?, query_status=?, last_updated_date=?, updated_by=? WHERE query_id=?;");
			pstmt.setString(1, queryDescription);
			pstmt.setString(2, errorMessage);
			pstmt.setString(3, isCritical);
			pstmt.setString(4, status);
			pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
			pstmt.setInt(6, userId);
			pstmt.setInt(7, queryId);
			logger.info("Update ICS Queries - "+pstmt);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public String getPropertyValue(String propertyDevName) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String value = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			String query = "SELECT value FROM st_ics_property_master WHERE property_dev_name='"+propertyDevName+"' AND status='ACTIVE';";
			logger.info("getPropertyValue Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				value = rs.getString("value");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return value;
	}
}