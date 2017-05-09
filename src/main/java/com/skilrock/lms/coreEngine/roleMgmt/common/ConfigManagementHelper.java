package com.skilrock.lms.coreEngine.roleMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ConfigBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class ConfigManagementHelper

{
	static Log logger = LogFactory.getLog(ConfigManagementHelper.class);

	public List<ConfigBean> fetchProperty() throws LMSException {

		List<ConfigBean> configList = new ArrayList<ConfigBean>();
		ConfigBean bean = null;
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement configStmt = con.createStatement();
			String fetchProperty = "select * from st_lms_property_master order by property_display_name";
			ResultSet propRS = configStmt.executeQuery(fetchProperty);

			while (propRS.next()) {
				bean = new ConfigBean();
				bean.setCode(propRS.getString("property_code"));
				bean.setName(propRS.getString("property_display_name"));
				bean.setStatus(propRS.getString("status"));
				bean.setType(propRS.getString("value_type"));
				bean.setValue(propRS.getString("value"));
				bean.setEditable(propRS.getString("editable"));
				bean.setDescription(propRS.getString("description"));
				configList.add(bean);
			}

			return configList;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void updateProperty(UserInfoBean userBean, String[] code,
			String[] status, String[] value, String[] description)
			throws LMSException {

		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			String updProperty = "update st_lms_property_master set status=?,value=?,description=? where property_code =? and editable='YES'";
			PreparedStatement pstmt = con.prepareStatement(updProperty);
			String insPropertyHistory = "insert into st_lms_property_master_history select property_code,property_display_name,status,value,'"
					+ new Timestamp(new Date().getTime())
					+ "','"
					+ userBean.getUserId()
					+ "' from st_lms_property_master where property_code=?";
			PreparedStatement pstmtHistory = con
					.prepareStatement(insPropertyHistory);
			logger
					.info(code.length + "-Code***Status-" + status.length
							+ "**Value-" + value.length + "**desc"
							+ description.length);
			for (int i = 0; i < code.length; i++) {
				pstmt.setString(1, status[i]);
				pstmt.setString(2, value[i].trim());
				pstmt.setString(3, description[i]);
				pstmt.setString(4, code[i]);
				
				pstmtHistory.setString(1, code[i]);
				pstmtHistory.executeUpdate();
				pstmt.execute();
			}
			con.commit();
			
			//-- Updating the LMS Servlet Context
			LMSFilterDispatcher.updateProperties();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}