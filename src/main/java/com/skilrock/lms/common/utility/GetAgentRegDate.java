package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class GetAgentRegDate {
	static Log logger = LogFactory.getLog(GetAgentRegDate.class);

	public static Timestamp getAgtRegDate(int agtOrgId) throws LMSException {

		Connection con = null;
		PreparedStatement pstmt = null;
		Timestamp agtregDate = null;
		try {

			 
			con = DBConnect.getConnection();
			pstmt = con
					.prepareStatement("select registration_date from st_lms_user_master where organization_id=? and isrolehead=?");
			pstmt.setInt(1, agtOrgId);
			pstmt.setString(2, "Y");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				agtregDate = rs.getTimestamp("registration_date");
			}
			return agtregDate;

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sqlException" + se);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("sql exception", e);
			}
		}

	}

}