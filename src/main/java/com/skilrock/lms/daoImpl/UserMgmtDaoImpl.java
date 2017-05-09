package com.skilrock.lms.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.dao.UserMgmtDao;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

public class UserMgmtDaoImpl implements UserMgmtDao{
	private static Logger loggger = LoggerFactory.getLogger(UserMgmtDaoImpl.class);
	private volatile static UserMgmtDaoImpl userMgmtDaoImpl = null;

	private UserMgmtDaoImpl(){}
	public static UserMgmtDaoImpl getInstance() {
		if (userMgmtDaoImpl == null) {
			synchronized (UserMgmtDaoImpl.class) {
				if (userMgmtDaoImpl == null) {
					loggger.info("getInstance(): First time getInstance invoked!");
					userMgmtDaoImpl = new UserMgmtDaoImpl();
				}
			}
		}
		return userMgmtDaoImpl;
	}

	@Override
	public UserDataBean getUserInfo(String userName) throws SLEException {
		Connection connection = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		UserDataBean userBean = null;
		try {
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			//query = "SELECT first_name, last_name, addr_line1, mobile_nbr, email_id, user_name, um.user_id, um.organization_type, user_session, city, (SELECT NAME FROM st_lms_state_master WHERE state_code=om.state_code) state, (SELECT NAME FROM st_lms_country_master WHERE country_code=om.country_code) country FROM st_lms_user_master um INNER JOIN st_lms_user_contact_details ucd ON um.user_id=ucd.user_id INNER JOIN st_lms_organization_master om ON um.organization_id=om.organization_id WHERE user_name='"+userName+"';";
			query = "SELECT first_name, last_name, name org_name, addr_line1, mobile_nbr, email_id, user_name, aa.user_id, IFNULL(user_mapping_id,0) user_mapping_id, organization_type, user_session, city, state, country FROM (SELECT first_name, last_name, om.name,addr_line1, mobile_nbr, email_id, user_name, um.user_id, um.organization_type, user_session, city, (SELECT NAME FROM st_lms_state_master WHERE state_code=om.state_code) state, (SELECT NAME FROM st_lms_country_master WHERE country_code=om.country_code) country FROM st_lms_user_master um INNER JOIN st_lms_user_contact_details ucd ON um.user_id=ucd.user_id INNER JOIN st_lms_organization_master om ON um.organization_id=om.organization_id WHERE user_name='"+userName+"') aa LEFT JOIN st_lms_user_random_id_mapping urm ON aa.user_id=urm.user_id;";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				userBean = new UserDataBean();
				userBean.setFirstName(rs.getString("first_name"));
				userBean.setLastName(rs.getString("last_name"));
				userBean.setOrgName(rs.getString("org_name"));
				userBean.setAddress(rs.getString("addr_line1"));
				userBean.setMobileNbr(rs.getString("mobile_nbr"));
				userBean.setEmailId(rs.getString("email_id"));
				userBean.setUserName(rs.getString("user_name"));
				userBean.setUserId(rs.getString("user_id"));
				userBean.setUserMappingId(rs.getInt("user_mapping_id"));
				userBean.setUserType(rs.getString("organization_type"));
				userBean.setSessionId(rs.getString("user_session"));
				userBean.setCity(rs.getString("city"));
				userBean.setState(rs.getString("state"));
				userBean.setCountry(rs.getString("country"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return userBean;
	}
	
	public void updateUserLogout(String sessionId){
		Connection connection = null;
		PreparedStatement pstmt = null;
		String query = null;
		try {
			connection = DBConnect.getConnection();
			query = "UPDATE st_lms_user_master SET user_session = NULL where user_session = ? ";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, sessionId);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}