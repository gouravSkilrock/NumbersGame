package com.skilrock.lms.coreEngine.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsUserDataBean;

public class LmsUserDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataDaoImpl.class);

	public List<LmsUserDataBean> fetchLmsUserDetails(String userType,
			String cityCode, Connection con) throws LMSException {
		logger
				.info("***** Inside fetchLmsUserDetails method of LmsUserDataDaoImpl class");

		List<LmsUserDataBean> lmsUserList = null;
		LmsUserDataBean lmsUserDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		// String query =
		// "select table_2.first_name, table_2.last_name, table_2.email_id, table_2.phone_nbr, table_2.mobile_nbr, table_1.lat, table_1.lon, table_1.addr_line1, table_1.addr_line2,table_1.city_code from st_lms_user_contact_details as table_2 inner join (select rom.user_id, rom.city_code, rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_ret_offline_master rom inner join st_lms_organization_master om on rom.organization_id=om.organization_id where om.organization_type='RETAILER' and rom.city_code <> '') as table_1 on table_1.user_id = table_2.user_id";
		String query = null;

		// query =
		// "select * from st_lms_user_contact_details ucd inner join "
		// +
		// "(select rom.user_id, rom.lat, rom.lon from st_lms_ret_offline_master rom inner join "
		// +
		// "st_lms_user_master um on rom.user_id=um.user_id where um.organization_type='RETAILER' "
		// +
		// "and um.status='ACTIVE' and rom.city_code=?) aa on aa.user_id = ucd.user_id";
		if ("RETAILER".equals(userType))
			query = "select ucd.first_name, ucd.last_name, ucd.email_id, ucd.phone_nbr, ucd.mobile_nbr, "
					+ "rom.lat, rom.lon, om.addr_line1, om.addr_line2 from st_lms_organization_master om "
					+ "inner join st_lms_user_master um on um.organization_id = om.organization_id "
					+ "inner join st_lms_ret_offline_master rom on rom.user_id = um.user_id "
					+ "inner join st_lms_user_contact_details ucd on ucd.user_id = rom.user_id "
					+ "where um.organization_type = 'RETAILER' and rom.city_code=?";
		else
			query = "select * from st_lms_user_contact_details ucd inner join "
					+ "(select rom.user_id, rom.lat, rom.lon from st_lms_ret_offline_master rom inner join "
					+ "st_lms_user_master um on rom.user_id=um.user_id where um.organization_type='RETAILER' "
					+ "and um.status='ACTIVE' and rom.city_code=?) aa on aa.user_id = ucd.user_id";
		try {
			pStatement = con.prepareStatement(query);
			pStatement.setString(1, cityCode);

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
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return lmsUserList;
	}
}
