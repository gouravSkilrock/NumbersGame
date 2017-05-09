package com.skilrock.lms.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.userMgmt.javaBeans.LmsUserDataBean;


public class LmsUserDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataDaoImpl.class);

	private static LmsUserDataDaoImpl singleInstance;
	private LmsUserDataDaoImpl(){}
	 public static LmsUserDataDaoImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsUserDataDaoImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsUserDataDaoImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	 
	public List<LmsUserDataBean> fetchLmsUserDetails(String userType,
			String cityCode, Connection con) throws SQLException {
		logger
				.info("***** Inside fetchLmsUserDetails method of LmsUserDataDaoImpl class");

		List<LmsUserDataBean> lmsUserList = null;
		LmsUserDataBean lmsUserDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		// String query =
		// "select table_2.first_name, table_2.last_name, table_2.email_id, table_2.phone_nbr, table_2.mobile_nbr, table_1.lat, table_1.lon, table_1.addr_line1, table_1.addr_line2,table_1.city_code from st_lms_user_contact_details as table_2 inner join (select rom.user_id, rom.city_code, rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_ret_offline_master rom inner join st_lms_organization_master om on rom.organization_id=om.organization_id where om.organization_type='RETAILER' and rom.city_code <> '') as table_1 on table_1.user_id = table_2.user_id";
		String query = null;
		pStatement = con.prepareStatement("select city_name from st_lms_city_master where city_code=?");
		pStatement.setString(1, cityCode);

		logger.info("Query is " + pStatement);

		rs = pStatement.executeQuery();
		String cityName=null;
		if(rs.next()){
			cityName=rs.getString("city_name");
		}
		
		
		if ("RETAILER".equals(userType))
			query = "select ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr, "
					+ "rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_organization_master om "
					+ "inner join st_lms_user_master um on um.organization_id = om.organization_id "
					+ "inner join st_lms_ret_offline_master rom on rom.user_id = um.user_id "
					+ "inner join st_lms_user_contact_details ucd on ucd.user_id = rom.user_id "
					+ "where um.organization_type = 'RETAILER' and um.status='ACTIVE' and om.organization_status='ACTIVE' and om.city=?  and (lat !=0 or lon !=0) group by lat,lon order by first_name";
		else
			query = "select * from st_lms_user_contact_details ucd inner join "
					+ "(select rom.user_id, rom.lat, rom.lon from st_lms_ret_offline_master rom inner join "
					+ "st_lms_user_master um on rom.user_id=um.user_id where um.organization_type='RETAILER' "
					+ "and um.status='ACTIVE' and rom.city_code=?) aa on aa.user_id = ucd.user_id";
		
			pStatement = con.prepareStatement(query);
			pStatement.setString(1, cityName);

			logger.info("Query is " + pStatement);

			rs = pStatement.executeQuery();

			lmsUserList = new ArrayList<LmsUserDataBean>();

			while (rs.next()) {
				lmsUserDataBean = new LmsUserDataBean();
				lmsUserDataBean.setFirstName(rs.getString("first_name"));
				lmsUserDataBean.setLastName(rs.getString("last_name"));
				lmsUserDataBean.setEmailId(rs.getString("email_id"));
				lmsUserDataBean.setPhoneNbr(rs.getString("phone_nbr"));
				lmsUserDataBean.setMobileNbr(rs.getString("mobile_nbr"));
				lmsUserDataBean.setLatitude(rs.getString("lat"));
				lmsUserDataBean.setLongitude(rs.getString("lon"));
				lmsUserDataBean.setAddress_1(rs.getString("addr_line1"));
				lmsUserDataBean.setAddress_2(rs.getString("addr_line2"));
				lmsUserList.add(lmsUserDataBean);
			}
			logger.debug("***** Fetched LMS User List is "
					+ lmsUserList.toString());
		
		return lmsUserList;
	}
	
	public List<LmsUserDataBean> fetchNearByLmsUserDetails(String lat,String lng,Connection con) throws SQLException {
		logger.info("***** Inside fetchLmsUserDetails method of LmsUserDataDaoImpl class");

		List<LmsUserDataBean> lmsUserList = null;
		LmsUserDataBean lmsUserDataBean = null;
		Statement statement = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		// String query =
		// "select table_2.first_name, table_2.last_name, table_2.email_id, table_2.phone_nbr, table_2.mobile_nbr, table_1.lat, table_1.lon, table_1.addr_line1, table_1.addr_line2,table_1.city_code from st_lms_user_contact_details as table_2 inner join (select rom.user_id, rom.city_code, rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_ret_offline_master rom inner join st_lms_organization_master om on rom.organization_id=om.organization_id where om.organization_type='RETAILER' and rom.city_code <> '') as table_1 on table_1.user_id = table_2.user_id";
		
		/*if("RETAILER".equals(userType))
		query = "select ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr,rom.lat, rom.lon, om.addr_line1, om.addr_line2, " +
					"(3959 * acos (cos ( radians("+lat+") ) * cos( radians( lat ) )* cos( radians( lon ) - radians("+lng+") ) + sin ( radians("+lat+") ) * sin( radians( lat ) ) ) ) AS distance from st_lms_organization_master om inner join st_lms_user_master um on um.organization_id = om.organization_id inner join st_lms_ret_offline_master rom on rom.user_id = um.user_id inner join st_lms_user_contact_details ucd on ucd.user_id = rom.user_id where um.organization_type = 'RETAILER' OR 'AGENT' OR 'BO' and um.status='ACTIVE' and om.organization_status='ACTIVE'  and (lat !=0 or lon !=0) group by lat,lon order by distance limit 10";
		else
		query = "select * from st_lms_user_contact_details ucd inner join "
				+ "(select rom.user_id, rom.lat, rom.lon from st_lms_ret_offline_master rom inner join "
				+ "st_lms_user_master um on rom.user_id=um.user_id where um.organization_type='RETAILER' "
				+ "and um.status='ACTIVE' and rom.city_code=?) aa on aa.user_id = ucd.user_id";*/
		/**
		 * Services are Hard Code Values .
		 */
	    List<String> boServiceList = new ArrayList<String>();                                            
	    boServiceList.add("Winning claim high prize");
	    List<String> retServiceList = new ArrayList<String>();
	    retServiceList.add("Buy Scratch Cards");
	    retServiceList.add("Buy Draw Games");
	    retServiceList.add("Deposit");
	    retServiceList.add("Buy Sports Lottery");
	    retServiceList.add("Winning claim low prize upto $100 ");
	    String query="select organization_type,first_name, last_name, email_id, phone_nbr, mobile_nbr ,city, sm.name state,cm.name country,lat, lon, addr_line1, addr_line2,distance from(select organization_type,first_name, last_name, email_id, phone_nbr, mobile_nbr ,city,state_code,country_code,lat, lon, addr_line1, addr_line2,distance from (select om.city,om.state_code,om.country_code,um.organization_type,ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr,rom.lat, rom.lon, om.addr_line1, om.addr_line2, (3959 * acos (cos ( radians("+lat+") ) * cos( radians( lat ) )* cos( radians( lon ) - radians("+lng+") ) + sin ( radians("+lat+") ) * sin( radians( lat ) ) ) ) AS distance from st_lms_organization_master om inner join st_lms_user_master um on um.organization_id = om.organization_id inner join st_lms_ret_offline_master rom on rom.user_id = um.user_id inner join st_lms_user_contact_details ucd on ucd.user_id = rom.user_id where um.organization_type = 'RETAILER' and um.status!=('BLOCK' OR 'TERMINATE') and om.organization_status!=('BLOCK' OR 'TERMINATE') and isrolehead='Y' AND (lat !=0 or lon !=0)  order by distance  limit 10) main UNION select   um.organization_type,ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr,om.city,om.state_code,om.country_code,0 as lat,0 as lon, om.addr_line1, om.addr_line2, 0 as distance   from st_lms_organization_master om inner join st_lms_user_master um on um.organization_id = om.organization_id inner join st_lms_user_contact_details ucd on ucd.user_id = um .user_id where um.organization_type = 'BO' and um.status!=('BLOCK' OR 'TERMINATE') and om.organization_status!=('BLOCK' OR 'TERMINATE') and isrolehead='Y' and um.parent_user_id=0) ud left join st_lms_country_master  cm  on cm.country_code=ud.country_code left join st_lms_state_master  sm  on sm.state_code=ud.state_code ";
        statement = con.createStatement();
		logger.info("Query is " + query);
		lmsUserList = new ArrayList<LmsUserDataBean>();
		rs = statement.executeQuery(query);
	   while (rs.next()){
				lmsUserDataBean = new LmsUserDataBean();
				lmsUserDataBean.setFirstName(rs.getString("first_name"));
				lmsUserDataBean.setLastName(rs.getString("last_name"));
				lmsUserDataBean.setEmailId(rs.getString("email_id"));
				lmsUserDataBean.setPhoneNbr(rs.getString("phone_nbr"));
				lmsUserDataBean.setMobileNbr(rs.getString("mobile_nbr"));
				lmsUserDataBean.setAddress_1(rs.getString("addr_line1"));
				lmsUserDataBean.setAddress_2(rs.getString("addr_line2"));
				lmsUserDataBean.setUserType(rs.getString("organization_type"));
			    if ("BO".equals(rs.getString("organization_type"))){
					String	address = sb.append(rs.getString("city")).append(rs.getString("state")).append(rs.getString("country")).toString();
					String latLon = CommonFunctionsHelper.getLongitudeLatitude(address);
					lmsUserDataBean.setLatitude(latLon.split(":")[0]);
					lmsUserDataBean.setLongitude(latLon.split(":")[1]);
					lmsUserDataBean.setServices(boServiceList);
				}else if("RETAILER".equals(rs.getString("organization_type"))){
				    lmsUserDataBean.setLatitude(rs.getString("lat"));
				    lmsUserDataBean.setLongitude(rs.getString("lon"));
				    lmsUserDataBean.setServices(retServiceList);
				}
			    lmsUserList.add(lmsUserDataBean);	
		     }
			
			logger.debug("***** Fetched LMS User List is "+ lmsUserList.toString());

		return lmsUserList;
	}
	public List<LmsUserDataBean> fetchUserDetails(String stateCode,String cityCode, Connection con) throws SQLException {
		logger.debug("***** Inside fetchLmsUserDetails method of LmsUserDataDaoImpl class");
		List<LmsUserDataBean> lmsUserList = null;
		LmsUserDataBean lmsUserDataBean = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		// String query =
		// "select table_2.first_name, table_2.last_name, table_2.email_id, table_2.phone_nbr, table_2.mobile_nbr, table_1.lat, table_1.lon, table_1.addr_line1, table_1.addr_line2,table_1.city_code from st_lms_user_contact_details as table_2 inner join (select rom.user_id, rom.city_code, rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_ret_offline_master rom inner join st_lms_organization_master om on rom.organization_id=om.organization_id where om.organization_type='RETAILER' and rom.city_code <> '') as table_1 on table_1.user_id = table_2.user_id";
		String query = null;
		pStatement = con.prepareStatement("select city_name from st_lms_city_master where city_code=?");
		pStatement.setString(1, cityCode);
    	logger.debug("Query is " + pStatement);
		rs = pStatement.executeQuery();
		String cityName=null;
		if(rs.next()){
			cityName=rs.getString("city_name");
		}
		/**
		 * Services are Hard Code Values .
		 */
		    List<String> boServiceList = new ArrayList<String>();                                         
		    boServiceList.add("Winning claim high prize");
		    List<String> retServiceList = new ArrayList<String>();
		    retServiceList.add("Buy Scratch Cards");
		    retServiceList.add("Buy Draw Games");
		    retServiceList.add("Deposit");
		    retServiceList.add("Buy Sports Lottery");
		    retServiceList.add("Winning claim low prize upto $100");
			query =" select  organization_type,first_name, last_name, email_id, phone_nbr, mobile_nbr,lat, lon, addr_line1, addr_line2,city,sm.name state,cm.name country from(select um.organization_type,ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr,rom.lat, rom.lon, om.addr_line1, om.addr_line2,om.city,om.state_code,om.country_code from st_lms_organization_master om inner join st_lms_user_master um on um.organization_id = om.organization_id inner join st_lms_ret_offline_master rom on rom.user_id = um.user_id inner join st_lms_user_contact_details ucd on ucd.user_id = rom.user_id where um.organization_type = 'RETAILER' and um.status!=('BLOCK' OR 'TERMINATE') and om.organization_status!=('BLOCK' OR 'TERMINATE') and om.city=? and  state_code=?  and (lat !=0 or lon !=0)"+ 
					" UNION select  um.organization_type, ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr,0 as lat,0 as lon, om.addr_line1, om.addr_line2,om.city,om.state_code,om.country_code   from st_lms_organization_master om inner join st_lms_user_master um on um.organization_id = om.organization_id inner join st_lms_user_contact_details ucd on ucd.user_id = um .user_id where um.organization_type = 'BO' and um.status!=('BLOCK' OR 'TERMINATE') and om.organization_status!=('BLOCK' OR 'TERMINATE') and isrolehead='Y' and um.parent_user_id=0)main"+
					" left join st_lms_country_master  cm  on cm.country_code=main.country_code left join st_lms_state_master  sm  on sm.state_code=main.state_code"; 
			pStatement = con.prepareStatement(query);
			pStatement.setString(1, cityName);
			pStatement.setString(2, stateCode);
        	logger.debug("Query is " + pStatement);
			rs = pStatement.executeQuery();
			lmsUserList = new ArrayList<LmsUserDataBean>();
			while (rs.next()) {
				lmsUserDataBean = new LmsUserDataBean();
				lmsUserDataBean.setFirstName(rs.getString("first_name"));
				lmsUserDataBean.setLastName(rs.getString("last_name"));
				lmsUserDataBean.setEmailId(rs.getString("email_id"));
				lmsUserDataBean.setPhoneNbr(rs.getString("phone_nbr"));
				lmsUserDataBean.setMobileNbr(rs.getString("mobile_nbr"));
				lmsUserDataBean.setUserType(rs.getString("organization_type"));
				 if ("BO".equals(rs.getString("organization_type"))){
						String	address = sb.append(rs.getString("city")).append(rs.getString("state")).append(rs.getString("country")).toString();
						String latLon = CommonFunctionsHelper.getLongitudeLatitude(address);
						lmsUserDataBean.setLatitude(latLon.split(":")[0]);
						lmsUserDataBean.setLongitude(latLon.split(":")[1]);
						lmsUserDataBean.setServices(boServiceList);
					}else if("RETAILER".equals(rs.getString("organization_type"))){
					    lmsUserDataBean.setLatitude(rs.getString("lat"));
					    lmsUserDataBean.setLongitude(rs.getString("lon"));
					    lmsUserDataBean.setServices(retServiceList);
					}
				lmsUserDataBean.setAddress_1(rs.getString("addr_line1"));
				lmsUserDataBean.setAddress_2(rs.getString("addr_line2"));
				lmsUserList.add(lmsUserDataBean);
			}
			logger.debug("***** Fetched LMS User List is "	+ lmsUserList.toString());
		
		return lmsUserList;
	}
	
	
}


