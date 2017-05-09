package com.skilrock.lms.admin.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class TerminalMgmtHelper {
	public TreeMap<String, ArrayList<String>> fetchRetList(
			String searchType, String modelId) throws Exception {
		TreeMap<String, ArrayList<String>> retMap = new TreeMap<String, ArrayList<String>>();
		Connection con = DBConnect.getConnection();
		Statement drawStmt = con.createStatement();
		String selRet = null;
		ArrayList<String> retList = null;
		String type = null;
		if ("AGENTWISE".equalsIgnoreCase(searchType)) {
			selRet = "select upper(slom.name) name,upper(parent.name) as searchType,slom.organization_id from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where organization_type='AGENT') parent, st_lms_user_master slum, st_lms_ret_offline_master rom where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id and slom.organization_id = rom.organization_id and slom.organization_status !='TERMINATE' and rom.device_type = '"+modelId+"' order by searchType,name;";
		} else {
			selRet = "select upper(slom.name) name,slom.organization_id,upper(slom.city) searchType from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where organization_type='AGENT') parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id and slom.organization_status !='TERMINATE'  order by searchType,name";
		}
		ResultSet retRs = drawStmt.executeQuery(selRet);
		while (retRs.next()) {
			type = retRs.getString("searchType");
			if (retMap.containsKey(type)) {
				retMap.get(type).add(retRs.getString("name"));
			} else {
				retList = new ArrayList<String>();
				retList.add(retRs.getString("name"));
				retMap.put(type, retList);
			}
		}
		DBConnect.closeCon(con);
		return retMap;
	}
	
	public String saveTerminalDetails(String[] retName, String version) throws Exception{
		Connection con = DBConnect.getConnection();
		con.setAutoCommit(false);
		Statement st = con.createStatement();
		boolean isAllRet = false;
		String query = null;
		StringBuilder tempRet = new StringBuilder("");
		List<String> tempRetNameList = Arrays.asList(retName);
		StringBuilder tempRetIdList = new StringBuilder("");
		if (tempRetNameList.contains("-1")) {
			isAllRet = true;
		}
		if (!isAllRet) {
			for (String element : retName) {
				tempRet.append("'" + element);
				tempRet.append("',");
			}
			tempRet.deleteCharAt(tempRet.length() - 1);
			String selRet = "select organization_id from st_lms_organization_master where name in ("
					+ tempRet + ")";
			ResultSet retRs = st.executeQuery(selRet);
			while (retRs.next()) {
				tempRetIdList.append(retRs.getString("organization_id")+",");
			}
			tempRetIdList.deleteCharAt(tempRetIdList.length() - 1);
		}
		if (isAllRet) {
			query = "update st_lms_ret_offline_master set is_download_available = 'YES', expected_version = '" + version + "'"; 
		} else {
			query = "update st_lms_ret_offline_master set is_download_available = 'YES', expected_version = '" + version + "' where organization_id in (" + tempRetIdList + ")"; 
		}
		st.executeUpdate(query);
		con.commit();
		DBConnect.closeCon(con);
		return "success";
	}
	
	public ArrayList<String> fetchVersionDetails(String modelId) throws SQLException{
		ArrayList<String> versionList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		Statement st = con.createStatement();
		String query = "select vm.version `version` from st_lms_htpos_device_master dm inner join st_lms_htpos_version_master vm on dm.device_id = vm.device_id where dm.device_type = '"+modelId+"' and vm.status = 'ACTIVE';";
		ResultSet rs = st.executeQuery(query);
		String devType = "";
		while(rs.next()){
			versionList.add(rs.getString("version"));
		}
		return versionList;
	}
	public Map<Integer, String> fetchDeviceList() throws SQLException{
		Map<Integer, String> deviceMap = new HashMap<Integer, String>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try{
		con = DBConnect.getConnection();
		st = con.createStatement();
		String query = "select device_id, device_type from st_lms_htpos_device_master;";
		rs = st.executeQuery(query);
		while(rs.next()){
			deviceMap.put(rs.getInt("device_id"), rs.getString("device_type"));
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(st, rs, con);
		}
		return deviceMap;
	}
	public static TreeMap<String,String> fetchTerminalBrand() throws LMSException {

		Connection con = DBConnect.getConnection();
		TreeMap<String, String> brandDetMap = new TreeMap<String, String>();

		try {
			String fetchBrandDetQuery="select brand_id, brand_name from st_lms_inv_brand_master where inv_id =1;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchBrandDetQuery);

			while (rs.next()) {
				brandDetMap.put(rs.getString("brand_id") , rs
						.getString("brand_name"));
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return brandDetMap;
	}
}
