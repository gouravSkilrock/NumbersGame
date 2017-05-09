package com.skilrock.lms.coreEngine.drawGames.drawMgmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.drawGames.common.Util;

public class LSDaoImpl {
	private static Log logger = LogFactory.getLog(LSDaoImpl.class);

	private static LSDaoImpl instance = null;
	private LSDaoImpl() {
	}
	public static LSDaoImpl getInstance() {
		if (instance == null) {
			instance = new LSDaoImpl();
		}
		return instance;
	}

	public String fetchKey(String warName) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		String key = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT ticket_id1 FROM st_lms_key_details WHERE ticket_name='"+warName+"';");
			if (rs.next())
				key = rs.getString("ticket_id1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return key;
	}

	public void updateKey(String encodeKey, String warName) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE st_lms_key_details SET ticket_id1='"+encodeKey+"' WHERE ticket_name='"+warName+"';");
			logger.info("Key Updated on LMS");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public Map<String, String> getLMSParamMap() throws Exception {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT SQL_CACHE service_code, status FROM st_lms_service_master;");
			while (rs.next()) {
				paramMap.put(rs.getString("service_code"), rs.getString("status"));
			}

			rs = stmt.executeQuery("SELECT SQL_CACHE b.service_code, a.interface, c.tier_code, a.status FROM st_lms_service_delivery_master a, st_lms_service_master b, st_lms_tier_master c WHERE a.service_id=b.service_id AND a.tier_id=c.tier_id AND c.tier_code <> 'BO';");
			while (rs.next()) {
				paramMap.put((rs.getString("service_code") + "_" + rs.getString("interface") + "_" + rs.getString("tier_code")), rs.getString("status"));
			}

			rs = stmt.executeQuery("SELECT SQL_CACHE COUNT(*) active_games FROM st_dg_game_master WHERE game_status='OPEN';");
			if (rs.next()) {
				paramMap.put(MonitoringParameters.NO_OF_ACTIVE_GAMES, rs.getString("active_games"));
			}

			rs = stmt.executeQuery("SELECT SQL_CACHE organization_type, COUNT(user_id) user_count FROM st_lms_user_master WHERE organization_type<>'BO' AND status<>'TERMINATE' GROUP BY organization_type;");
			while (rs.next()) {
				paramMap.put(("NO_OF_ACTIVE_" + rs.getString("organization_type")), rs.getString("user_count"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return paramMap;
	}

	public void setGracePeriod(boolean flag, Date startDate, String warName) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			stmt = connection.createStatement();
			String value = (flag) ? "YES" : "NO";
			stmt.executeUpdate("UPDATE st_lms_property_master SET value='"+value+"' WHERE property_dev_name='IS_GRACE_PERIOD';");
			logger.info("Grace Period Set to - "+flag+" ("+value+")");

			String graceStartDate = (flag) ? "'"+new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"'" : "NULL";

			stmt.executeUpdate("UPDATE st_lms_key_details SET ticket_start_date="+graceStartDate+" WHERE ticket_name='"+warName+"';");
			logger.info("Grace Date Set to - "+graceStartDate);

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public boolean validateGracePeriod(String warName) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = DBConnect.getConnection();
			String isGracePeriod = Utility.getPropertyFromDB("IS_GRACE_PERIOD", connection);
			if("YES".equals(isGracePeriod)) {
				Timestamp graceTime = null;
				int graceCount = 0;
				stmt = connection.createStatement();
				rs = stmt.executeQuery("SELECT ticket_start_date, ticket_valid_count FROM st_lms_key_details WHERE ticket_name='"+warName+"';");
				if(rs.next()) {
					graceTime = rs.getTimestamp("ticket_start_date");
					graceCount = rs.getInt("ticket_valid_count");
				}
				logger.info("Grace Time - "+graceTime+" | Grace Count - "+graceCount);

				if(graceTime == null)
					setGracePeriod(true, Util.getCurrentTimeStamp(), warName);
				else {
					Timestamp todayTime = Util.getCurrentTimeStamp();

					int difference = (int) (todayTime.getTime() - graceTime.getTime())/(24*60*60*1000);
					logger.info("Difference - "+difference);
					if(difference >= graceCount)
						return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return true;
	}
}