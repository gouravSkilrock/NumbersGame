package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

public class GetUserOrganziationDetails {
	static Log logger = LogFactory.getLog(GetUserOrganziationDetails.class);

	public static String getAddress(int orgId) {
		Connection con = DBConnect.getConnection();
		String add = null;
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(QueryManager
					.getST_BO_INVOICE_CUSTOMER_DETAILS());
			pstmt.setInt(1, orgId);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null) {
				if (rs.next()) {

					add = rs.getString("addr_line1")
							+ rs.getString("addr_line2") + ", "
							+ rs.getString("city") + ", "
							+ rs.getString("state") + ", "
							+ rs.getString("country");
					logger.debug("org add =============== " + add);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return add;
	}

}
