package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.PriviledgeBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServiceBean;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;

public class ReportUtility {
	static Log logger = LogFactory.getLog(ReportUtility.class);
	
	public static Map<String, Object> sysPropertiesMap=null;
	public static Boolean isDG="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw());
	public static Boolean isSE="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsScratch());
	public static Boolean isCS ="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS());
	public static Boolean isOLA ="YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA());
	public static Boolean isSLE = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE());
	public static Boolean isIW = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW());
	public static Boolean isVS = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS());
	public static HashMap<Integer, String> gameMap=null;
	public static HashMap<Integer, String> gameMapWithoutPromo=null; 
	
	//	Map added for Priviledge and Report bean
	public static Map<String, PriviledgeBean> priviledgeMap = null;
	public static Map<String, ReportStatusBean> reportsMap = null;

	public static Object getPropertyValue(String propertyDevName){
		return sysPropertiesMap.get(propertyDevName);
	}
	
	public static void setSysPropertyMap(){
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			sysPropertiesMap=new HashMap<String, Object>();
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			rs=stmt.executeQuery("select property_dev_name,value from ge_property_master where status='ACTIVE'");
			while(rs.next()){
				sysPropertiesMap.put(rs.getString("property_dev_name"), rs.getString("value"));
			}
			
		}catch (SQLException se) {
			se.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	
	public static String getSLEGameMapQuery(java.sql.Timestamp fromDate)
			throws LMSException {
		return "select game_id,game_disp_name from st_sle_game_master where game_id not in(select game_id from st_sle_game_master where closing_time <='"
				+ fromDate + "') order by display_order";

	}

	public static List<Integer> getActiveSLEGameIdList(
			java.sql.Timestamp fromDate, Connection con) throws LMSException {
		List<Integer> gameNoList = new ArrayList<Integer>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getSLEGameMapQuery(fromDate));
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				gameNoList.add(rs.getInt("game_id"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} /*
		 * finally { try { if (pstmt != null && !pstmt.isClosed()) {
		 * pstmt.close(); } } catch (SQLException e) {
		 * logger.error("Exception: " + e); e.printStackTrace(); throw new
		 * LMSException(e); } }
		 */
		return gameNoList;
	}
	
	public static String getDrawGameMapQuery(java.sql.Timestamp fromDate) throws LMSException{
		//return "select game_id,game_name from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where closing_time <='"+fromDate+"') order by display_order";
		return "select game_id,game_name, game_status, closing_time from st_dg_game_master  where closing_time  > '"+fromDate+"' or  closing_time is null order by display_order;";
		
	}
	
	public static Map<Integer, String> getActiveGameNumMap(String startDate) throws LMSException {
		Map<Integer, String> gameIdMap = new LinkedHashMap<Integer, String>();
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select game_id,game_name from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where closing_time <='"+startDate+"') order by display_order");
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				gameIdMap.put(rs.getInt("game_id"), rs.getString("game_name"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return gameIdMap;
	}
	
	
	public static String getScratchGameMapQuery(java.sql.Timestamp fromDate) throws LMSException{
		return "select game_id, game_name from st_se_game_master";
		
	}
	
	
	
	public static HashMap<Integer, String> fetchDrawDataMenu() throws LMSException{
	
	if(gameMap==null){
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select game_id,game_name from st_dg_game_master order by display_order");
			
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			gameMap=new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				gameMap.put(rs.getInt("game_id"),rs.getString("game_name").toUpperCase());
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}
	
	return gameMap;
	}
	
	
	public static HashMap<Integer, String> fetchActiveGameDrawDataMenu(){
		HashMap<Integer, String> gameMap=null;

		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select game_id,game_name from st_dg_game_master where game_status!= 'SALE_CLOSE' order by display_order");
			
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			gameMap=new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				gameMap.put(rs.getInt("game_id"),rs.getString("game_name").toUpperCase());
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				
			}
		}
		
	
		return gameMap;
		
	}
	
	public static HashMap<Integer, String> fetchActiveCategoriesCommSerData(){
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, String> catMap=null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("select category_id , category_code from st_cs_product_category_master where status='ACTIVE'");
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			catMap=new LinkedHashMap<Integer, String>();
			while (rs.next()) 
				catMap.put(rs.getInt("category_id"),rs.getString("category_code").toUpperCase());
		} catch (SQLException e){
			logger.error("Exception: " + e);
		} finally {
			DBConnect.closeConnection(con, pstmt, rs); 
		 }
		return catMap;
	}
	
	
	
	public static String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}
	
	public static Map<String, String> getOrgMap(String orgType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, String> orgMap = new LinkedHashMap<String, String>();
		try {
			con = DBConnect.getConnection();
		//	String chkService = "select name,organization_id from st_lms_organization_master where organization_type=? order by name";
			
			pstmt = con.prepareStatement(QueryManager.getOrgQry(orgType));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgMap.put(rs.getString("organization_id"), rs
						.getString("orgCode"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orgMap;
	}

	public static Map<String, String> allGameMap() throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select game_name,'DG' as game_type from st_dg_game_master union all select game_name,'SE' as game_type from st_se_game_master union all select category_code,'CS' as game_type from st_cs_product_category_master where status = 'ACTIVE' union all select wallet_name,'OLA' as game_type from st_ola_wallet_master order by game_type";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("game_name"), rs
						.getString("game_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Game List");
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return gameMap;
	}
	
	public static Map<String, String> allGameMap(java.sql.Timestamp fromDate) throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry="select game_name,'DG' as game_type,display_order  from st_dg_game_master  where closing_time  > '"+fromDate+"' or  closing_time is null union all select game_name,'SE' as game_type,0 display_order from st_se_game_master union all select category_code,'CS' as game_type,0 display_order from st_cs_product_category_master where status = 'ACTIVE' UNION ALL SELECT game_disp_name,'SLE' AS game_type,display_order FROM st_sle_game_master WHERE game_no NOT IN(SELECT game_no FROM st_sle_game_master where closing_time <='"+fromDate+"') union all SELECT game_disp_name, 'IW' AS game_type, display_order FROM st_iw_game_master WHERE game_no NOT IN(SELECT game_no FROM st_iw_game_master where closing_time <='"+fromDate+"') union all SELECT game_disp_name, 'VS' AS game_type, display_order FROM st_vs_game_master WHERE game_no NOT IN(SELECT game_no FROM st_vs_game_master where closing_time <='"+fromDate+"') union all select wallet_name,'OLA' as game_type,0 display_order from st_ola_wallet_master order by game_type,display_order";
			//String gameQry = "select game_name,'DG' as game_type from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where closing_time <='"+fromDate+"') union all select game_name,'SE' as game_type from st_se_game_master union all select category_code,'CS' as game_type from st_cs_product_category_master where status = 'ACTIVE' union all select wallet_name,'OLA' as game_type from st_ola_wallet_master order by game_type";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("game_name"), rs
						.getString("game_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Game List");
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		System.out.println(gameMap);
		return gameMap;
	}
	

	public Map<String, String> allCatMap() throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select category_code,'CS' as cat_type from st_cs_product_category_master where status = 'ACTIVE'";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("category_code"), rs
						.getString("cat_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Cat List");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public Map<String, Boolean> checkAvailableService() {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, Boolean> serMap = new HashMap<String, Boolean>();
		try {
			con = DBConnect.getConnection();
			String chkService = "select service_code,status from st_lms_service_master where service_code!='MGMT'";
			pstmt = con.prepareStatement(chkService);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				serMap.put(rs.getString("service_code"), "ACTIVE".equals(rs
						.getString("status")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return serMap;
	}
	
	public static int getOrgIdFromOrgName(String orgName, String type) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		int orgId = 0;
		try {

			String query = null;
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			if (type.equals("AGENT")) {
				query = QueryManager.getST5PartyTypeQuery() + "and name='"
						+ orgName + "'";
			}
			if (type.equals("RETAILER")) {
				query = QueryManager.getST5PartyTypeForAgentQuery()
						+ "and name='" + orgName + "'";
			}
			logger.debug("-----Query----::" + query);
			resultSet = statement.executeQuery(query);

			resultSet.next();
			Integer intx = resultSet.getInt(TableConstants.ORGANIZATION_ID);
			orgId = intx.intValue();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}
		return orgId;

	}

	public static String getOrgNameFromOrgId(Connection conn, int orgId)
			throws SQLException {
		PreparedStatement pstmt=null;
		String name = null;
		pstmt = conn.prepareStatement("select name from st_lms_organization_master where organization_id=?");
		pstmt.setInt(1, orgId);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			name = rs.getString("name");
		}

		return name;
	}
	
	public static int getUserIdFromOrgId(int orgId, Connection con) throws SQLException, LMSException {
		int userId = 0;
		PreparedStatement pstmt = null;
		pstmt = con.prepareStatement("select user_id  from st_lms_user_master where organization_id=?");
		pstmt.setInt(1, orgId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			userId = rs.getInt("user_id");
		}
		return userId;
	}

	public static void getOrgNameMap(Connection conn,	Map<Integer, String> agtNameOrgIdMap, String orgIds)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try{
			String name = null;

		
			String agtOrgQry = "select "+QueryManager.getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_id in("+orgIds+") order by "+QueryManager.getAppendOrgOrder();
			
		
			pstmt = conn
					.prepareStatement(agtOrgQry);
			 rs = pstmt.executeQuery();
			while (rs.next()) {
				name = rs.getString("orgCode");
				agtNameOrgIdMap.put(rs.getInt("organization_id"),name);
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}finally {
			
			if(pstmt!=null){
				pstmt.close();
			}
			if(rs!=null){
				rs.close();
			}
			
		}
		
		

	
	}
public static int getDaysBetweenDate(Calendar startDate,Calendar endDate){
	
	long diff = (clearTimeFromDate(endDate)).getTimeInMillis()-(clearTimeFromDate(startDate)).getTimeInMillis();
	try{
		return (int) (diff/86400000);
		
	}catch(Exception e){
		
	}
	return 0;
}	
public static Calendar  clearTimeFromDate(Calendar cal){
	
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal;
}
/**
 * Check if startDate is before or after Last Archiving Date
 * @param startDate
 * @return
 */

	public static boolean isArchData(Date startDate) {
		boolean isArchData = true;

		try {

			String lastDate = CommonMethods.getLastArchDate();
			//SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			logger.info("start date" + startDate + "last archieve date"+ lastDate);
			Calendar calStart = Calendar.getInstance();
			Calendar calLast = Calendar.getInstance();
			calStart.setTime(format.parse(lastDate));
			calLast.setTime(startDate);
			if (calStart.compareTo(calLast)>=0) {
				isArchData = true;
			} else {
				isArchData = false;

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return isArchData;

	}
	
	/*public static boolean checkDateLessThanArchiveDate(Object fromDate,Connection con){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			pstmt=con.prepareStatement("select if(last_date > ?,'TRUE','FALSE') status from tempdate_history order by id desc limit 1");
			pstmt.setString(1,fromDate.toString());
			rs=pstmt.executeQuery();
			if(rs.next()){
				if("TRUE".equals(rs.getString("status"))){
					return true;
				}
			}else{
				pstmt = con.prepareStatement("select if(last_date > ?,'TRUE','FALSE') status from st_lms_property_master where property_code='deployment_date'");
				pstmt.setString(1,fromDate.toString());
				rs=pstmt.executeQuery();
				if(rs.next()){
					if("TRUE".equals(rs.getString("status"))){
					return true;
					}
				}
			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}*/

	public static boolean checkDateLessThanArchiveDate(Object fromDate,Connection con){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				SimpleDateFormat sdk = new SimpleDateFormat("dd-mm-yyyy");
				java.util.Date startDate=(java.util.Date) sdf.parse(fromDate.toString());
		
			pstmt=con.prepareStatement("select if(last_date > ?,'TRUE','FALSE') status from tempdate_history order by id desc limit 1");
			pstmt.setString(1,fromDate.toString());
			rs=pstmt.executeQuery();
			if(rs.next()){
				if("TRUE".equals(rs.getString("status"))){
					return true;
				}
			}else{
				pstmt = con.prepareStatement("select value from st_lms_property_master where property_code='deployment_date'");
				rs=pstmt.executeQuery();
				if(rs.next()){
					java.util.Date dbDate = (java.util.Date) sdk.parse(rs.getString("value"));				    
					 if(startDate.before(dbDate)){
						 return true;					       
					 }
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkDateLessThanArchiveDate(Object fromDate){
		Connection con=null;
		try{
			 con=DBConnect.getConnection();
			 return checkDateLessThanArchiveDate(fromDate, con);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		return false;
		 
	}
	
	public static String getLastArchDate(Connection con) {
		 
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		String lastDate="";
		String oldLastDate = "";
		
		try {
			pstmt = con.prepareStatement("select last_date from tempdate_history order by id desc limit 1");
			rs=pstmt.executeQuery();
			if(rs.next()){
				lastDate=rs.getDate("last_date").toString();	
			}else{
				pstmt = con.prepareStatement("select value last_date from st_lms_property_master where property_code='deployment_date'");
				rs=pstmt.executeQuery();
				if(rs.next()){
					oldLastDate=rs.getString("last_date");
					SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date oldDate = formatOld.parse(oldLastDate);
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(oldDate);
					cal1.add(Calendar.DAY_OF_MONTH, -1);
					lastDate = format.format(cal1.getTime());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lastDate;
	}
	
	public static Date getLastArchDateInDateFormat(Connection con) {
		 
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 Date lastDate=null;
		
		
		try {
			pstmt = con.prepareStatement("select last_date from tempdate_history order by id desc limit 1");
			rs=pstmt.executeQuery();
			if(rs.next()){
				lastDate=rs.getDate("last_date");	
			}else{
				pstmt = con.prepareStatement("select value last_date from st_lms_property_master where property_code='deployment_date'");
				rs=pstmt.executeQuery();
				if(rs.next()){
					String oldLastDate =rs.getString("last_date");
					SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date oldDate = formatOld.parse(oldLastDate);
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(oldDate);
					cal1.add(Calendar.DAY_OF_MONTH, -1);
					lastDate =new java.sql.Date(cal1.getTimeInMillis());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lastDate;
	}

	/**
	 * Game MAP Without Free Games
	 * @return
	 * @throws LMSException
	 */
	public static HashMap<Integer, String> fetchGameMapWithoutPromo() throws LMSException{
		
		if(gameMapWithoutPromo==null){
			Connection con = null;
			con = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
//				pstmt = con
//						.prepareStatement("select game_id,game_name from st_dg_game_master  left join st_dg_promo_scheme on game_id=promo_game_id where promo_game_id is null  order by display_order");
				pstmt = con
				.prepareStatement("select game_id,game_name from st_dg_game_master order by display_order");
				
				rs = pstmt.executeQuery();
				logger.debug(pstmt);
				gameMapWithoutPromo=new LinkedHashMap<Integer, String>();
				while (rs.next()) {
					gameMapWithoutPromo.put(rs.getInt("game_id"),rs.getString("game_name").toUpperCase());
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					if (con != null ) {
						con.close();
					}
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}
		
		return gameMapWithoutPromo;
	}

	public static ReportStatusBean getReportStatus(String actionName) {
		PriviledgeBean priviledgeBean = priviledgeMap.get(actionName);
		ReportStatusBean reportStatusBean = null;

		if(priviledgeBean != null) {
			reportStatusBean =  reportsMap.get(priviledgeBean.getModule()+"#"+priviledgeBean.getGroupName()+"#"+priviledgeBean.getChannel());
			if(reportStatusBean != null) {
				Calendar calendar = Calendar.getInstance();
				long startTime = reportStatusBean.getStartTime().getTime();
				long endTime = reportStatusBean.getEndTime().getTime();
				long nowTime = 0L;
				try {
					nowTime = new Time(new SimpleDateFormat("HH:mm:ss").parse(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)).getTime()).getTime();
				} catch (ParseException ex) {
					logger.error("Exception: " + ex);
					ex.printStackTrace();
					reportStatusBean = new ReportStatusBean();
					reportStatusBean.setReportStatus("FAILURE");
					return reportStatusBean;
				}

				if(!(nowTime>=startTime && nowTime<=endTime))
					reportStatusBean.setReportStatus("SUCCESS");
				else
					reportStatusBean.setReportStatus("FAILURE");

				return reportStatusBean;
			}
		}

		reportStatusBean = new ReportStatusBean();
		reportStatusBean.setReportStatus("SUCCESS");
		reportStatusBean.setReportingFrom("MAIN_DB");
		return reportStatusBean;
	}

	public static void onStartPriviledgeMap(Connection connection) throws LMSException {
		priviledgeMap = new HashMap<String, PriviledgeBean>();

		Statement stmt = null;
		ResultSet rs = null;
		PriviledgeBean priviledgeBean = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT action_mapping AS priviledge_key, action_id, priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, STATUS, priv_owner, related_to, group_name, is_start, channel, priv_code, hidden, 'MGMT' AS module FROM st_lms_priviledge_rep UNION ALL SELECT action_mapping AS priviledge_key, action_id, priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, STATUS, priv_owner, related_to, group_name, is_start, channel, priv_code, hidden, 'DG' AS module FROM st_dg_priviledge_rep UNION ALL SELECT action_mapping AS priviledge_key, action_id, priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, STATUS, priv_owner, related_to, group_name, is_start, channel, priv_code, hidden, 'OLA' AS module FROM st_ola_priviledge_rep UNION ALL SELECT action_mapping AS priviledge_key, action_id, priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, STATUS, priv_owner, related_to, group_name, is_start, channel, priv_code, hidden, 'SE' AS module FROM st_se_priviledge_rep UNION ALL SELECT action_mapping AS priviledge_key, action_id, priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, STATUS, priv_owner, related_to, group_name, is_start, channel, priv_code, hidden, 'IW' AS module FROM st_iw_priviledge_rep;");
			logger.info(stmt);
			while (rs.next()) {
				priviledgeBean = new PriviledgeBean();

				priviledgeBean.setActionId(rs.getInt("action_id"));
				priviledgeBean.setPrivId(rs.getInt("priv_id"));
				priviledgeBean.setPrivTitle(rs.getString("priv_title"));
				priviledgeBean.setPrivDisplayName(rs.getString("priv_disp_name"));
				priviledgeBean.setActionMapping(rs.getString("action_mapping"));
				priviledgeBean.setParentPrivId(rs.getInt("parent_priv_id"));
				priviledgeBean.setStatus(rs.getString("status"));
				priviledgeBean.setPrivOwner(rs.getString("priv_owner"));
				priviledgeBean.setRelatedTo(rs.getString("related_to"));
				priviledgeBean.setGroupName(rs.getString("group_name"));
				priviledgeBean.setIsStart(rs.getString("is_start"));
				priviledgeBean.setChannel(rs.getString("channel"));
				priviledgeBean.setPrivCode(rs.getInt("priv_code"));
				priviledgeBean.setHidden(rs.getString("hidden"));
				priviledgeBean.setModule(rs.getString("module"));

				priviledgeMap.put(rs.getString("priviledge_key"), priviledgeBean);
			}
		} catch (SQLException se) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void onStartReportMap(Connection connection) throws LMSException {
		reportsMap = new HashMap<String, ReportStatusBean>();

		Statement stmt = null;
		ResultSet rs = null;
		ReportStatusBean reportStatusBean = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT CONCAT(serviceName,'#',report_group_name,'#',interfaceType) report_key, report_id, report_group_name, report_disp_name, reporting_from, report_off_start_time, report_off_end_time, serviceName, interfaceType FROM st_lms_reports_status;");
			logger.info(stmt);
			while (rs.next()) {
				reportStatusBean = new ReportStatusBean();
				reportStatusBean.setReportId(rs.getInt("report_id"));
				reportStatusBean.setReportGroupName(rs.getString("report_group_name"));
				reportStatusBean.setReportDisplayName(rs.getString("report_disp_name"));
				reportStatusBean.setReportingFrom(rs.getString("reporting_from"));
				reportStatusBean.setStartTime(rs.getTime("report_off_start_time"));
				reportStatusBean.setEndTime(rs.getTime("report_off_end_time"));
				reportStatusBean.setServiceName(rs.getString("serviceName"));
				reportStatusBean.setInterfaceType(rs.getString("interfaceType"));

				reportsMap.put(rs.getString("report_key"), reportStatusBean);
			}
		} catch (SQLException se) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void onStartPriviledgeReportMap() {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			onStartPriviledgeMap(connection);
			onStartReportMap(connection);
		} catch (LMSException se) {
			   logger.error("Exception",se);
		} catch (Exception e) {
				logger.error("Exception",e);
		} finally {
			DBConnect.closeCon(connection);
		}
	}
	
	public static Map<Integer,UserDetailsBean> getRetUserDetailsMap(Connection con,String stateCode,String cityCode,String areaCode,String startDate,String endDate ) throws LMSException{
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		 Map<Integer,UserDetailsBean>  retUsrDetailsMap = new  HashMap<Integer,UserDetailsBean> ();
		try{
			StringBuilder sb = new StringBuilder();
			if(stateCode!=null&&!stateCode.equalsIgnoreCase("-1")){
				sb.append(" and sm.state_code='"+stateCode+"' ");
			}
			if(cityCode!=null&&!cityCode.equalsIgnoreCase("-1")){
				sb.append(" and cm.city_code='"+cityCode+"' ");
			}
			if(areaCode!=null&&!areaCode.equalsIgnoreCase("-1")){
				sb.append(" and am.area_code='"+areaCode+"' ");
			}
			if((startDate!=null&&!startDate.equalsIgnoreCase("-1"))&&(endDate!=null&&!endDate.equalsIgnoreCase("-1"))){
				sb.append(" and (um.termination_date is null or (um.termination_date>='"+startDate+"'  or registration_date>='"+endDate+"' ))" ); 
				
			}
			pstmt = con.prepareStatement(" select user_id,area_name,city_name,sm.name state,sm.state_code from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id "+ 
                                         " inner join st_lms_city_master cm on cm.city_name=om.city  inner join st_lms_state_master  sm on sm.state_code=om.state_code  inner join st_lms_area_master  am on  am.area_code=om.area_code where   om.organization_type='RETAILER' "+sb.toString()) ; 
			rs=pstmt.executeQuery();
			while(rs.next()){
				UserDetailsBean usrBean =new UserDetailsBean();
				usrBean.setOrgStateCode(rs.getString("state_code"));
				usrBean.setOrgState(rs.getString("state"));
				usrBean.setOrgCity(rs.getString("city_name"));
				usrBean.setOrgAreaName(rs.getString("area_name"));
				retUsrDetailsMap.put(rs.getInt("user_id"), usrBean);
			}
			
			
			
		}catch (SQLException e) {
			logger.error("SQL Exception ", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			logger.error(" Exception ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
			
		}
		return retUsrDetailsMap;
		
	}

	public static Timestamp getZeroTimeDate(java.util.Date date) {

		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return new Timestamp(calendar.getTime().getTime());
	}

	public static Timestamp getLastTimeDate(java.util.Date date) {

		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return new Timestamp(calendar.getTime().getTime());
	}
	
	public static String getIWGameMapQuery(java.sql.Timestamp fromDate)
			throws LMSException {
		return "select game_id,game_disp_name game_name from st_iw_game_master where game_id not in(select game_id from st_iw_game_master where closing_time <='"
				+ fromDate + "') order by display_order";
	}

	public static Map<Integer, String> getIWGameMap(java.sql.Timestamp fromDate)
			throws LMSException {
		Map<Integer, String> gameMap = new HashMap<Integer, String>();

		String query = getIWGameMapQuery(fromDate);

		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				gameMap.put(rs.getInt("game_id"), rs.getString("game_name"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		
		return gameMap;
	}
	
	public static String getVSGameMapQuery(java.sql.Timestamp fromDate) throws LMSException {
		return "select game_id,game_disp_name game_name from st_vs_game_master where game_id not in(select game_id from st_vs_game_master where closing_time <='"
				+ fromDate + "') order by display_order";
	}

	public static Map<Integer, String> getVSGameMap(java.sql.Timestamp fromDate)
			throws LMSException {
		Map<Integer, String> gameMap = new HashMap<Integer, String>();

		String query = getVSGameMapQuery(fromDate);

		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				gameMap.put(rs.getInt("game_id"), rs.getString("game_name"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return gameMap;
	}
	
	public static List<ServiceBean> fetchGameServiceWise() throws LMSException {
		List<ServiceBean> serviceBeans = new ArrayList<ServiceBean>();
		ServiceBean serviceBean = null;
		List<GameBean> gameBeans = null;
		GameBean gameBean = null;
		Connection con = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();

			stmt = con.createStatement();

			rs = stmt.executeQuery("select service_id, service_code, service_display_name from st_lms_service_master where status = 'ACTIVE';");
			while (rs.next()) {
				if("MGMT".equals(rs.getString("service_code")))
					continue;
				serviceBean = new ServiceBean();
				serviceBean.setServiceId(rs.getInt("service_id"));
				serviceBean.setServiceCode(rs.getString("service_code"));
				serviceBean.setServiceDisplayName(rs.getString("service_display_name"));
				serviceBeans.add(serviceBean);
			}

			for(ServiceBean bean : serviceBeans) {
				if ("IW".equals(bean.getServiceCode())) {
					gameBeans = new ArrayList<GameBean>();
					rs = stmt.executeQuery("select game_id, game_disp_name from st_iw_game_master where game_status = 'SALE_OPEN';");
					while(rs.next()) {
						gameBean = new GameBean();
						gameBean.setGameId(rs.getInt("game_id"));
						gameBean.setGameName(rs.getString("game_disp_name"));
						gameBeans.add(gameBean);
					}
					bean.setGameBeans(gameBeans);
				} else if ("DG".equals(bean.getServiceCode())) {
					gameBeans = new ArrayList<GameBean>();
					rs = stmt.executeQuery("select game_id, game_name from st_dg_game_master where game_status = 'OPEN';");
					while(rs.next()) {
						gameBean = new GameBean();
						gameBean.setGameId(rs.getInt("game_id"));
						gameBean.setGameName(rs.getString("game_name"));
						gameBeans.add(gameBean);
					}
					bean.setGameBeans(gameBeans);
				}
			}
			System.out.println("Service Wise Game Bean is " + serviceBeans);
		} catch (SQLException e) {
			logger.error("SQL Exception ", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(" Exception ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return serviceBeans;
	}
	
	public static boolean checkDateLessThanLastRunDate(Object fromDate, int agentOrgId, String orgType, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat sdk = new SimpleDateFormat("dd-mm-yyyy");
			java.util.Date startDate = (java.util.Date) sdf.parse(fromDate.toString());
			
			String subQuery = " ";
			
			if("RETAILER".equals(orgType))
				subQuery = " and parent_id = " + agentOrgId;
//				subQuery = " and organization_id = " + retOrgId + " and parent_id = " + agentOrgId;

			pstmt = con.prepareStatement("select if(finaldate > ?,'TRUE','FALSE') status from st_rep_org_bal_history where organization_type = '" + orgType + "' " + subQuery + " order by finaldate desc limit 1");
			pstmt.setString(1, fromDate.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if ("TRUE".equals(rs.getString("status"))) {
					return true;
				}
			} else {
				pstmt = con.prepareStatement("select value from st_lms_property_master where property_code='deployment_date'");
				rs = pstmt.executeQuery();
				if (rs.next()) {
					java.util.Date dbDate = (java.util.Date) sdk.parse(rs.getString("value"));
					if (startDate.before(dbDate)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String fetchLastRunDate(Connection con) {
		Statement stmt = null;
		ResultSet rs = null;

		String lastRunDate = null;

		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("select max(finaldate) finaldate from st_rep_org_bal_history;");
			if (rs.next()) {
				if(rs.getString("finaldate")!=null){
					lastRunDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("finaldate")));
				}else {
					lastRunDate = Utility.getPropertyValue("DEPLOYMENT_DATE");
				}
				
			} else {
				lastRunDate = Utility.getPropertyValue("DEPLOYMENT_DATE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRunDate;
	}
	
	public static String fetchLastRunDate(String orgType, int agentId, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;

		String lastRunDate = null;
		String subQuery = " ";

		if ("RETAILER".equals(orgType))
			subQuery = " and parent_id = " + agentId;
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("select ifnull(max(finaldate), 'NA') finaldate from st_rep_org_bal_history where organization_type = '" + orgType + "' " + subQuery + ";");
			if (rs.next() && !"NA".equals(rs.getString("finaldate"))) {
				lastRunDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("finaldate")));
			} else {
				lastRunDate = Utility.getPropertyValue("DEPLOYMENT_DATE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRunDate;
	}
	
	public static java.util.Date fetchLastRunDateifExists(Connection con) {
		Statement stmt = null;
		ResultSet rs = null;

		java.util.Date lastRunDate = null;

		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("select max(finaldate) finaldate from st_rep_org_bal_history;");
			if (rs.next()) {
				lastRunDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("finaldate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRunDate;
	}
	
	public static Map<Integer, Double> fetchClosingBalAgentExcludingCLAndXCl(Connection con, Timestamp timeStamp) {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		
		Map<Integer, Double> agentClosingBalMap = new HashMap<Integer, Double>();
		
		try {
			pStmt = con.prepareStatement("select organization_id, (opening_bal + net_amount_transaction) open_bal from st_rep_org_bal_history where finaldate = ? and organization_type='AGENT'");
			pStmt.setTimestamp(1, timeStamp);
			
			rs = pStmt.executeQuery();
			
			while (rs.next()) {
				agentClosingBalMap.put(rs.getInt("organization_id"), rs.getDouble("open_bal"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentClosingBalMap;
	}
	
	public static void main(String args[]){
			Connection con = DBConnect.getConnection();
			//checkDateLessThanArchiveDate2("2014-11-10 23:59:59.0",con);
	}
}