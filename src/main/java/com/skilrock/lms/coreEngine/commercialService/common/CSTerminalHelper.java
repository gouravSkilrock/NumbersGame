package com.skilrock.lms.coreEngine.commercialService.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.beans.CSLoginBean;
//import com.skilrock.lms.beans.HTPOSItemBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.ResponsibleGaming;

public class CSTerminalHelper {

	public CSLoginBean getBalanceForTerminal(CSLoginBean loginBean) {
		loginBean.setBalance(CSUtil.fetchUserBalance(loginBean.getUserName()));
		if (Double.compare(loginBean.getBalance(), -1.0) <= 0) {
			loginBean.setStatus("Failure");
			loginBean.setErrorCode(103); // 103: Wrong UserName
			return loginBean;
		} else {
			loginBean.setStatus("Success");
			loginBean.setErrorCode(100);
			return loginBean;
		}
	}

	public CSLoginBean getReprintAuthorization(CSLoginBean loginBean) {
		UserInfoBean userBean = new UserInfoBean();
		Map<String, Integer> idMap = CSUtil.fetchUserOrgId(loginBean
				.getUserName(), 0);
		if (idMap.size() == 0) {
			loginBean.setStatus("Failure");
			loginBean.setErrorCode(103); // 103: Wrong UserName
			return loginBean;
		} else {
			userBean.setUserId(idMap.get("UserId"));
			userBean.setUserOrgId(idMap.get("OrgId"));
		}
		boolean isFraud = ResponsibleGaming.respGaming(userBean,
				"CS_REPRINT_LIMIT", 1 + "");
		if (!isFraud) {
			loginBean.setStatus("SUCCESS");
			loginBean.setErrorCode(100); // 100: No Error
		} else {
			loginBean.setStatus("Failure");
			loginBean.setErrorCode(116); // 116: Reprint limit reached;
		}
		return loginBean;
	}
	
	public Map<Integer, String> fetchDeviceTypes(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select device_id, device_type from st_lms_htpos_device_master");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getInt("device_id"), rs.getString("device_type"));
			}
			return map;
		}catch(Exception ex){
			
		}
		return map;
	}
	
	public Map<Integer, String> fetchDeviceItems(int deviceId){
		Map<Integer, String> map = new HashMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select item_id, item_name from st_lms_htpos_download_details where device_id = ?");
			pstmt.setInt(1, deviceId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getInt("item_id"), rs.getString("item_name"));
			}
			return map;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return map;
	}
	
	public String fetchCurrentVersionOfItem(int itemId){
		Connection con = DBConnect.getConnection();
		String version = "0";
		try{
			PreparedStatement pstmt = con.prepareStatement("select item_id, item_name from st_lms_htpos_download_details where item_id = ?");
			pstmt.setInt(1, itemId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				version = rs.getString("version");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return version;
	}
	
	/*public boolean updateVersion(HTPOSItemBean bean){
		boolean status = false;
		//insert in case item is not in st_lms_htpos_download_details, otherwise update the version of respective item
		
		return status;
	}*/
	
	
}
