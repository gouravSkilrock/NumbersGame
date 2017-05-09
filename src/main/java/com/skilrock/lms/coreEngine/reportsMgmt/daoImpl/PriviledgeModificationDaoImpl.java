package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationDataBean;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationHeaderBean;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationMasterBean;

public class PriviledgeModificationDaoImpl {
	final static Log logger = LogFactory.getLog(PriviledgeModificationDaoImpl.class);

	private static PriviledgeModificationDaoImpl instance;

	private PriviledgeModificationDaoImpl() {
	}

	public static PriviledgeModificationDaoImpl getInstance() {
		if (instance == null) {
			synchronized (PriviledgeModificationDaoImpl.class) {
				if (instance == null) {
					instance = new PriviledgeModificationDaoImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, String> getBoUsersList(Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> boUserMap = new LinkedHashMap<Integer, String>();
		try {
			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE user_id, user_name FROM st_lms_user_master WHERE organization_type='BO' AND parent_user_id<>0 ORDER BY user_name;";
			logger.info("getBoUsersList - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				boUserMap.put(rs.getInt("user_id"), rs.getString("user_name"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return boUserMap;
	}

	public Map<String, String> getServiceMap(Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> serviceMap = new LinkedHashMap<String, String>();
		try {
			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE service_code, service_name FROM st_lms_service_master WHERE status='ACTIVE';";
			logger.info("getServiceMap - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				serviceMap.put(rs.getString("service_code"), rs.getString("service_name"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return serviceMap;
	}

	public void getUserBasicData(int userId, PriviledgeModificationMasterBean masterBean, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();

			String query = "SELECT user_name, registration_date, 'bomaster' register_by, email_id FROM st_lms_user_master um INNER JOIN st_lms_user_contact_details ucd ON um.user_id=ucd.user_id WHERE um.user_id="+userId+";";
			logger.info("User Data Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				masterBean.setUsername(rs.getString("user_name"));
				masterBean.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("registration_date")));
				masterBean.setRegisterBy(rs.getString("register_by"));
				masterBean.setEmailId(rs.getString("email_id"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}
	}

	public PriviledgeModificationMasterBean fetchUserPriviledgeHistoryData(int userId, Timestamp startTime, Timestamp endTime, String serviceCode, Connection connection) throws LMSException {
		Statement mainStmt = null;
		ResultSet mainRs = null;
		Statement innerStmt = null;
		ResultSet innerRs = null;

		SimpleDateFormat compareFormat = null;
		SimpleDateFormat dateFormat = null;

		PriviledgeModificationMasterBean masterBean = new PriviledgeModificationMasterBean();
		List<PriviledgeModificationHeaderBean> headerList = new ArrayList<PriviledgeModificationHeaderBean>();
		PriviledgeModificationHeaderBean headerBean = null;
		Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap = new HashMap<String, Map<String,List<PriviledgeModificationDataBean>>>();
		Map<String, List<PriviledgeModificationDataBean>> dataMap = null;
		List<PriviledgeModificationDataBean> priviledgeList = null;
		PriviledgeModificationDataBean dataBean = null;
		try {
			mainStmt = connection.createStatement();
			innerStmt = connection.createStatement();

			compareFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			masterBean.setHeaderList(headerList);
			masterBean.setServiceMap(serviceMap);
			getUserBasicData(userId, masterBean, connection);

			mainStmt.execute("SET SESSION group_concat_max_len=1000000;");

			boolean isCurrentSet;
			String currentDate = compareFormat.format(new Date().getTime());
			String endDate = compareFormat.format(endTime.getTime());
			isCurrentSet = currentDate.equals(endDate);

			boolean isHeaderSet = false;
			String query = "SELECT service_name, service_delivery_master_id, priv_rep_table FROM st_lms_service_master sm INNER JOIN st_lms_service_delivery_master sdm ON sm.service_id=sdm.service_id WHERE service_code='"+serviceCode+"' AND channel='RETAIL' AND interface='WEB' AND tier_id=1 AND sm.status='ACTIVE' AND sdm.status='ACTIVE';";
			logger.info("Fetch Priviledge Tables Name Query - "+query);
			mainRs = mainStmt.executeQuery(query);
			if(mainRs.next()) {
				String serviceName = mainRs.getString("service_name");
				int serviceMappingId = mainRs.getInt("service_delivery_master_id");
				String privTable = mainRs.getString("priv_rep_table");

				dataMap = new TreeMap<String, List<PriviledgeModificationDataBean>>();

				int dateCount = 0;
				query = "SELECT user_id, data_value, change_date, change_by, user_name, request_ip FROM (" + ((isCurrentSet) ? "SELECT map.user_id, GROUP_CONCAT(CONCAT(map.priv_id,'~',group_name,'~',map.status) SEPARATOR '#') data_value, change_date, change_by, (SELECT user_name FROM st_lms_user_master WHERE user_id=change_by) user_name, request_ip FROM st_lms_user_priv_mapping map INNER JOIN "+privTable+" priv ON map.priv_id=priv.priv_id WHERE map.service_mapping_id="+serviceMappingId+" AND map.user_id="+userId+" AND priv.is_start='Y' AND priv.status<>'INACTIVE' AND map.status<>'NA' UNION " : "") + "SELECT user_id, data_value, change_date, change_by, user_name, request_ip FROM (SELECT hist.user_id, GROUP_CONCAT(CONCAT(hist.priv_id,'~',group_name,'~',hist.status) SEPARATOR '#') data_value, change_date, change_by, user_name, request_ip FROM st_lms_user_priv_history hist INNER JOIN "+privTable+" priv ON hist.priv_id=priv.priv_id INNER JOIN st_lms_user_master stum ON hist.change_by=stum.user_id WHERE hist.service_mapping_id="+serviceMappingId+" AND hist.user_id="+userId+" AND priv.is_start='Y' AND priv.status<>'INACTIVE' AND change_date>='"+startTime+"' AND change_date<='"+endTime+"' AND hist.status<>'NA' GROUP BY change_date ORDER BY change_date, hist.priv_id) aa ORDER BY change_date DESC LIMIT 5) aa ORDER BY change_date ASC;";
				logger.info("fetchUserPriviledgeHistoryData Query - "+query);
				innerRs = innerStmt.executeQuery(query);
				while(innerRs.next()) {
					dateCount++;
					if(innerRs.getString("data_value") != null) {
						if(isHeaderSet == false) {
							headerBean = new PriviledgeModificationHeaderBean();
							headerBean.setChangeTime(dateFormat.format(innerRs.getTimestamp("change_date")));
							headerBean.setDoneByUser(innerRs.getString("user_name"));
							headerBean.setDoneByIP(innerRs.getString("request_ip"));
							headerList.add(headerBean);
						}

						String dataValues = innerRs.getString("data_value");
						for(String dataValue : dataValues.split("#")) {
							String privName = dataValue.split("~")[1];
							priviledgeList = dataMap.get(privName);
							if(priviledgeList == null) {
								priviledgeList = new ArrayList<PriviledgeModificationDataBean>();
								dataMap.put(privName, priviledgeList);

								for(int i=1; i<dateCount; i++) {
									priviledgeList.add(new PriviledgeModificationDataBean());
								}
							}
	
							dataBean = new PriviledgeModificationDataBean();
							dataBean.setPrivId(Integer.parseInt(dataValue.split("~")[0]));
							dataBean.setPrivName(privName);
							dataBean.setStatus(dataValue.split("~")[2]);
							priviledgeList.add(dataBean);
						}
					}
				}
				isHeaderSet = true;
				serviceMap.put(serviceName, dataMap);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(mainStmt);
			DBConnect.closeStmt(innerStmt);
			DBConnect.closeRs(mainRs);
			DBConnect.closeRs(innerRs);
		}

		return masterBean;
	}
}