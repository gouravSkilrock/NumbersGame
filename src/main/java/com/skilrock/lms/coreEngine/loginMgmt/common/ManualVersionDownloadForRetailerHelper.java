package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class ManualVersionDownloadForRetailerHelper {
	Log logger = LogFactory.getLog(ManualVersionDownloadForRetailerHelper.class);
	
	
	public String fetchAvailableTerminalBuildVersions(String deviceType,String profile,double version) throws Exception{
		
		String versionString = null;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			if (deviceType == null || "".equals(deviceType))
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);

			con=DBConnect.getConnection();
			//pstmt=con.prepareStatement("select device_id,version from st_lms_htpos_version_master where device_id=(select device_id from st_lms_htpos_device_master where device_type=?) and manual_download_status='ACTIVE'");
			pstmt=con.prepareStatement("select device_id,concat (version ,'') version  from st_lms_htpos_download where device_id =(select device_id from st_lms_htpos_device_master where device_type=?) and manual_download_status='ACTIVE' and profile=? and version> ? order by version DESC");
			pstmt.setString(1 ,deviceType);
			pstmt.setString(2 ,profile);
			pstmt.setDouble(3 ,version);
			rs=pstmt.executeQuery();
			if(rs.next()){
				versionString="Builds_Info:"+rs.getString("version").concat("|");
				while(rs.next())
					versionString+=rs.getString("version").concat("|");
			}else
				throw new LMSException(LMSErrors.NO_RECORD_FOUND_ERROR_CODE, LMSErrors.NO_RECORD_FOUND_ERROR_MESSAGE);
			
		}catch (LMSException e) {
			throw e ;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			logger.error("EXCEPTION :- " + e);
			e.printStackTrace();
			throw e;
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return versionString;
	}
}
