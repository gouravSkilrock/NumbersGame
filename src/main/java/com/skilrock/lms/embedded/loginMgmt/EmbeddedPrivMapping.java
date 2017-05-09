package com.skilrock.lms.embedded.loginMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class EmbeddedPrivMapping {
	public static Log logger = LogFactory.getLog(EmbeddedPrivMapping.class);
/**
 * @deprecated  Use  getPriviledge(ArrayList<String> userActionList,String userType,Connection con) 
 * @param userActionList
 * @param userType
 * @return
 */
	public static String getPriviledge(ArrayList<String> userActionList,
			String userType) {
		// logger.debug(" userACtion list in privmapping class is
		// "+userActionList);
		Connection con = DBConnect.getConnection();
		StringBuilder stBuilder = new StringBuilder("");
		try {
			String query = "select lms_priv_name,terminal_priv from st_lms_terminal_mapping where user_type='"
					+ userType + "'";
			PreparedStatement pStatement = con.prepareStatement(query);
			logger.debug("terminal_priv query::::::::" + query);
			ResultSet rs = pStatement.executeQuery();
			while (rs.next()) {
				// logger.debug("lms_priv_name--->>>"+rs.getString("lms_priv_name"));
				// logger.debug("terminal_priv --->>
				// "+rs.getString("terminal_priv"));
				if (!userActionList.contains(rs.getString("lms_priv_name"))) {
					stBuilder.append(rs.getString("terminal_priv"));
				}
			}
			stBuilder.append("|");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			DBConnect.closeCon(con);
		}
		return stBuilder.toString();
	}
/**
 * @deprecated Use getPriviledgeNew(int userId,Connection con)	
 * @param userId
 * @return
 */

	public static String getPriviledgeNew(int userId) {
		Connection con = DBConnect.getConnection();
		StringBuilder stBuilder = new StringBuilder("");
		try {
			String query = "select sdm.priv_rep_table, sdm.service_delivery_master_id from st_lms_service_delivery_master sdm inner join st_lms_user_master um inner join st_lms_role_master rm on um.role_id = rm.role_id and rm.tier_id = sdm.tier_id and um.user_id = "
				+ userId + " and sdm.interface = 'TERMINAL'";
			Statement st = con.createStatement();
			logger.debug("terminal_priv query::::::::" + query);
			ResultSet rs = st.executeQuery(query);
			int flag = 0;
			StringBuilder qryBuilder =  new StringBuilder();
			while (rs.next()) {
				String tableName = rs.getString("priv_rep_table");
				String mapId = rs.getString("service_delivery_master_id");
				if(flag == 0){
					qryBuilder.append("select lpr.priv_code from st_lms_user_priv_mapping upm inner join "
							+ tableName + " lpr on upm.priv_id = lpr.priv_id where lpr.channel = 'TERMINAL' and upm.user_id = "
							+ userId + " and service_mapping_id = "
							+ mapId + " and upm.status = 'INACTIVE'");
					flag = 1;
				}
				else{
					qryBuilder.append(" union select lpr.priv_code from st_lms_user_priv_mapping upm inner join "
							+ tableName + " lpr on upm.priv_id = lpr.priv_id where lpr.channel = 'TERMINAL' and upm.user_id = "
							+ userId + " and service_mapping_id = "
							+ mapId + " and upm.status = 'INACTIVE'");
				}
			}
			rs = st.executeQuery(qryBuilder.toString());
			while (rs.next()) {
				stBuilder.append(rs.getString("priv_code"));
			}
			stBuilder.append("|");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			DBConnect.closeCon(con);
		}
		return stBuilder.toString();
	}
	public static String getPriviledge(ArrayList<String> userActionList,
			String userType,Connection con) {
		// logger.debug(" userACtion list in privmapping class is
		// "+userActionList);
		
		StringBuilder stBuilder = new StringBuilder("");
		try {
			String query = "select lms_priv_name,terminal_priv from st_lms_terminal_mapping where user_type=?";
			PreparedStatement pStatement = con.prepareStatement(query);
			pStatement.setString(1,userType);
			logger.debug("terminal_priv query::::::::" + query);
			ResultSet rs = pStatement.executeQuery();
			while (rs.next()) {
				// logger.debug("lms_priv_name--->>>"+rs.getString("lms_priv_name"));
				// logger.debug("terminal_priv --->>
				// "+rs.getString("terminal_priv"));
				if (!userActionList.contains(rs.getString("lms_priv_name"))) {
					stBuilder.append(rs.getString("terminal_priv"));
				}
			}
			stBuilder.append("|");
			//con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			//DBConnect.closeCon(con);
		}
		return stBuilder.toString();
	}
	public static String getPriviledgeNew(int userId,Connection con) throws LMSException {
		
		StringBuilder stBuilder = new StringBuilder("");
		Statement st =null;
		ResultSet rs=null;
		try {
			/*String query = "select sdm.priv_rep_table, sdm.service_delivery_master_id from st_lms_service_delivery_master sdm inner join st_lms_user_master um inner join st_lms_role_master rm on um.role_id = rm.role_id and rm.tier_id = sdm.tier_id and um.user_id = "
				+ userId + " and sdm.interface = 'TERMINAL'";*/
			String query = "select priv_rep_table,service_delivery_master_id from(select priv_rep_table,service_delivery_master_id,tier_id from st_lms_service_delivery_master sdm inner join st_lms_service_master sm on sdm.service_id=sm.service_id where sm.status='ACTIVE' and sdm.interface = 'TERMINAL')aa inner join (select tier_id from st_lms_user_master um inner join st_lms_role_master rm " +
					" on um.role_id = rm.role_id and um.user_id = "+ userId +")bb on aa.tier_id=bb.tier_id";
			st = con.createStatement();
			logger.info("terminal_priv query::::::::" + query);
			rs = st.executeQuery(query);
			int flag = 0;
			StringBuilder qryBuilder =  new StringBuilder();
			while (rs.next()) {
				String tableName = rs.getString("priv_rep_table");
				String mapId = rs.getString("service_delivery_master_id");
				if(flag == 0){
					qryBuilder.append("select lpr.priv_code from st_lms_user_priv_mapping upm inner join "
							+ tableName + " lpr on upm.priv_id = lpr.priv_id where lpr.channel = 'TERMINAL' and upm.user_id = "
							+ userId + " and service_mapping_id = "
							+ mapId + " and upm.status = 'INACTIVE'");
					flag = 1;
				}
				else{
					qryBuilder.append(" union select lpr.priv_code from st_lms_user_priv_mapping upm inner join "
							+ tableName + " lpr on upm.priv_id = lpr.priv_id where lpr.channel = 'TERMINAL' and upm.user_id = "
							+ userId + " and service_mapping_id = "
							+ mapId + " and upm.status = 'INACTIVE'");
				}
			}
			logger.info("priv query::::::::" + qryBuilder.toString());
			rs = st.executeQuery(qryBuilder.toString());
			while (rs.next()) {
				stBuilder.append(rs.getString("priv_code"));
			}
			stBuilder.append("|");
			//	con.close();
		} catch (SQLException e) {
		     logger.error("SQL Exception "+e);
		     throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	       }catch(Exception e){
	    	   logger.error(" Exception "+e);
	    	   throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	       } finally{
			DBConnect.closeStmt(st);
			DBConnect.closeRs(rs);
		}
		return stBuilder.toString();
	}
}
