package com.skilrock.lms.coreEngine.userMgmt.common;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;


/**
 * 
 * @author Gaurav Ujjwal
 * 
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2005     ABxxxxxx       CR#zzzzzz: blah blah blah... 
 * 28-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry(freezed draws).
 * 02-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry. Method getManualEntryData added.
 * 03-MAY-2010     Arun Tanwar    CR#L0375:Implementation of entering PMEP for ACTIVE draws. Method getManualDeclareData added.
 * </pre>
 */
public class AdvMsgHelper {
	static Log logger = LogFactory.getLog(AdvMsgHelper.class);
	
	public LinkedHashMap<String, Map<String, String>> fetchAdvMessageData(
			String searchType) throws Exception {
		LinkedHashMap<String, Map<String, String>> retMap = new LinkedHashMap<String, Map<String, String>>();
		
		Connection con = DBConnect.getConnection();
		Statement drawStmt = con.createStatement();
		String selRet = null;
		Map<String, String> retList = null;
		String type = null;

		/*
			String orgCodeQry = " slom.name orgCode  ";

		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " slom.org_code orgCode ";


		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(slom.org_code,'_',slom.name)  orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(slom.name,'_',slom.org_code)  orgCode ";
		

		}	
		*/
		


		if ("AGENTWISE".equalsIgnoreCase(searchType)) {
			//selRet = "select "+orgCodeQry+",upper(parent.name) as searchType,slom.organization_id, user_id from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=(SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 )) parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id order by "+QueryManager.getAppendOrgOrder();
			selRet = "SELECT "+QueryManager.getOrgCodeQuery()+", (SELECT UPPER(NAME) FROM st_lms_organization_master bb WHERE bb.organization_id=aa.parent_id) searchType, aa.organization_id, user_id FROM st_lms_organization_master aa INNER JOIN st_lms_user_master cc ON cc.organization_id=aa.organization_id WHERE aa.organization_type='RETAILER'  AND aa.organization_status !='TERMINATE'  and aa.parent_id not in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_status='TERMINATE') ORDER BY "+QueryManager.getAppendOrgOrder();
		} else {
			//selRet = "select "+orgCodeQry+",slom.organization_id, user_id,upper(slom.city) searchType from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=(SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 )) parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id order by "+QueryManager.getAppendOrgOrder();
			selRet = "SELECT "+QueryManager.getOrgCodeQuery()+", aa.organization_id, user_id, UPPER(city) searchType FROM st_lms_organization_master aa INNER JOIN st_lms_user_master cc ON cc.organization_id=aa.organization_id WHERE aa.organization_type='RETAILER' AND aa.organization_status !='TERMINATE'  and aa.parent_id not in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_status='TERMINATE') ORDER BY "+QueryManager.getAppendOrgOrder();
		}
		

		/*	if ("AGENTWISE".equalsIgnoreCase(searchType)) {
			selRet = "SELECT UPPER(slom.name) NAME,UPPER(parent.name) AS searchType,slom.organization_id FROM st_lms_organization_master slom,(SELECT a.NAME,a.organization_id FROM st_lms_organization_master a  INNER JOIN (SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 ) b   ON a. parent_id=b.organization_id) parent, st_lms_user_master slum WHERE slom.parent_id=parent.organization_id AND slom.organization_id = slum.organization_id   ORDER BY searchType,NAME;";
		} else {
			selRet = "SELECT UPPER(slom.name) NAME,slom.organization_id,UPPER(slom.city) searchType FROM st_lms_organization_master slom,(SELECT a.NAME,a.organization_id FROM st_lms_organization_master a  INNER JOIN (SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 ) b   ON a. parent_id=b.organization_id) parent, st_lms_user_master slum WHERE slom.parent_id=parent.organization_id AND slom.organization_id = slum.organization_id   ORDER BY searchType,NAME;";
		}*/
		ResultSet retRs = drawStmt.executeQuery(selRet);
		while (retRs.next()) {
			type = retRs.getString("searchType");
			if (retMap.containsKey(type)) {
				retMap.get(type).put(retRs.getString("user_id"), retRs.getString("orgCode"));
			} else {
				retList = new LinkedHashMap<String, String>();
				retList.put(retRs.getString("user_id"), retRs.getString("orgCode"));
				retMap.put(type, retList);
			}
		}
		DBConnect.closeCon(con);
		return retMap;

	}
	
	public String saveAdvMessageData(String orgType, String[] gameNo,
			String[] retName, String message, int creatorUserId,
			String msgLocation, String activity, int serviceId) throws SQLException {
		
		int msgId = 0;
		String result=null;
		boolean isAllRet = false;
		//boolean isAllGame = false;
		Connection con = null;
		ResultSet rsMsgId = null;
		Statement drawStmt = null;
		StringBuilder tempRet = null;
		PreparedStatement pstmt = null;
		List<String> tempRetNameList = null;
		//List<String> tempGameList = null;
		//List<String> tempRetIdList = null;
		try{
		//int serviceId = ((HashMap<String,Integer>)LMSUtility.sc.getAttribute("SERVICES_CODE_ID_MAP")).get(serviceCode);
		if (gameNo == null || retName == null)
			throw new LMSException(LMSErrors.BO_ADD_MESSAGING_ERROR_CODE, LMSErrors.BO_ADD_MESSAGING_ERROR_MESSAGE);
			//return "error";
		
		//tempGameList = Arrays.asList(gameNo);
		//tempRetIdList = new ArrayList<String>();
		/*if (tempGameList.contains("-1")) {
			isAllGame = true;
		}*/
		tempRetNameList = Arrays.asList(retName);
		if (tempRetNameList.contains("-1")) {
			isAllRet = true;
		}
		tempRet = new StringBuilder("");
		if (!isAllRet)
			for (String element : retName) {
				tempRet.append("'" + element);
				tempRet.append("',");
			}
			/*tempRet.deleteCharAt(tempRet.length() - 1);
			String selRet = "select organization_id from st_lms_organization_master where name in ("
					+ tempRet + ")";
			ResultSet retRs = drawStmt.executeQuery(selRet);
			while (retRs.next()) {
				tempRetIdList.add(retRs.getString("organization_id"));
			}*/
		/*String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values('"
				+ new Timestamp(new Date().getTime())
				+ "',"
				+ creatorUserId
				+ ",'"
				+ message
				+ "','ACTIVE','"
				+ orgType
				+ "','"
				+ msgLocation + "','" + activity + "')";*/

		con = DBConnect.getConnection();
		con.setAutoCommit(false);
		String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values(?,?,?,?,?,?,?)";
		pstmt=con.prepareStatement(query);
		pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
		pstmt.setInt(2, creatorUserId);
		pstmt.setString(3, message);
		pstmt.setString(4, "ACTIVE");
		pstmt.setString(5, orgType);
		pstmt.setString(6, msgLocation);
		pstmt.setString(7, activity);
		
		logger.info("***************-" + query);
		pstmt.executeUpdate();
		rsMsgId = pstmt.getGeneratedKeys();
		//drawStmt.execute(query);
		if (rsMsgId.next()) 
			msgId = rsMsgId.getInt(1);
		
		drawStmt = con.createStatement();
		if (isAllRet) 
			for (String element : gameNo) {
				query = "insert into st_dg_adv_msg_org_mapping(msg_id,org_id,service_id,game_id) values("
						+ msgId + ",-1,"+serviceId+"," + element + ")";
				drawStmt.addBatch(query);

			}
			//drawStmt.executeBatch();
		 else 
			for (String element : gameNo) {
				for (int j = 0; j < retName.length; j++) {
					query = "insert into st_dg_adv_msg_org_mapping(msg_id,org_id,service_id,game_id) values("
							+ msgId
							+ ","
							+ "(SELECT organization_id FROM st_lms_user_master WHERE user_id="+retName[j].split("~")[0]+")"
							+ ","+serviceId
							+ ","	
							+ element + ")";
					drawStmt.addBatch(query);
				}
			}
			//drawStmt.executeBatch();
		
		drawStmt.executeBatch();
		con.commit();
		//DBConnect.closeCon(con);
		result="success";
		//		ADD ADVERTISEMENT MESSAGES IN STATIC MAP IN CONTEXT
		Util.advMsgDataMap = new RetailerAdvMsgHelper().getAdvMsgDataMap();
		}catch (LMSException e) {
			logger.error(e.getErrorMessage());
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (SQLException e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (Exception e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}finally{
			DBConnect.closePstmt(pstmt);
			DBConnect.closeConnection(con, drawStmt, rsMsgId);
		}
		return result;
	}
	
	public Map<Integer, Map<String, String>> getAdvMsgForEdit() {
		Map<Integer, Map<String, String>> advMap = new TreeMap<Integer, Map<String, String>>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select msg_id, date, msg_text, status, msg_location, msg_for, activity  from st_dg_adv_msg_master where status = 'ACTIVE' and editable='YES'");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				Map<String, String> tmp = new LinkedHashMap<String, String>();
				tmp.put("Date", Util.getDateTimeFormat(rs.getTimestamp("date")));
				tmp.put("Message Text", rs.getString("msg_text"));
				tmp.put("status", rs.getString("status"));
				tmp.put("location", rs.getString("msg_location"));
				tmp.put("Message For", rs.getString("msg_for"));
				tmp.put("Activity", rs.getString("activity"));
				advMap.put(rs.getInt("msg_id"), tmp);
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
		return advMap;
	}
	
public boolean editAdvMsgStatus(int msgId, int userId, int orgId) {
		
		Connection con = null;
		boolean status = false;
		StringBuilder query=null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			query= new StringBuilder("insert into st_dg_adv_msg_master_history select msg_id, date, creator_user_id,msg_text, msg_for , 'INACTIVE', ? ,?  from st_dg_adv_msg_master where msg_id = ?");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, new java.sql.Timestamp(new java.util.Date().getTime()).toString());
			pstmt.setString(2, userId + "");
			pstmt.setInt(3, msgId);
			logger.debug("instAdvHist:   " + pstmt);
			pstmt.executeUpdate();

			query= new StringBuilder("update st_dg_adv_msg_master set status = 'INACTIVE' where msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, msgId);
			logger.debug("updtAdvMst:   " + pstmt);
			pstmt.executeUpdate();

			query= new StringBuilder("insert into st_dg_adv_msg_org_mapping_history select amm.msg_id, aom.org_id , aom.game_id, activity, ?, ? from st_dg_adv_msg_org_mapping aom, st_dg_adv_msg_master amm where amm.msg_id = aom.msg_id and amm.msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, new java.sql.Timestamp(new java.util.Date().getTime()).toString());
			pstmt.setString(2, userId + "");
			pstmt.setInt(3, msgId);
			logger.debug("instOrgMappingHist:   " + pstmt);
			pstmt.executeUpdate();
			
			query= new StringBuilder("delete from st_dg_adv_msg_org_mapping where msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, msgId);
			logger.debug("delOrgMapMst:   " + pstmt);
			pstmt.executeUpdate();
			
			status = true;
			con.commit();
		} catch (SQLException e) {
			logger.error("SQL Exception  :- " + e);
		} catch (Exception e) {
			logger.error("General Exception  :- " + e);
		} finally {
			DBConnect.closeConnection(con, pstmt);
		}
		return status;
	}


public String saveAdvMessageDataForRetailer(String orgType, String[] agtName,
		String[] retName, String message, int creatorUserId,
		String msgLocation, String activity1) throws SQLException {
	
	int msgId = 0;
	boolean isAllRet = false;
	
	String result = null;
	String status = null;
	String tempRetStr = "";
	String tempAgtStr = "";
	String tempOrgIdStr = "";
	StringBuilder tempOrgStr = null;

	ResultSet rs = null;
	Connection con = null;
	Statement drawStmt = null;
	PreparedStatement pstmt = null;

	List<Integer> orgIdList = null;
	List<String> phoneNoList = null;
	List<Integer> userIdList = null;
	List<String> tempAgtNameList = null;
	List<String> tempRetNameList = null;
	try{

		if (retName == null && agtName == null) 
		throw new LMSException(LMSErrors.BO_ADD_MESSAGING_ERROR_CODE, LMSErrors.BO_ADD_MESSAGING_ERROR_MESSAGE);

		if(agtName != null)
		tempAgtNameList = Arrays.asList(agtName);

		if(tempAgtNameList != null)
		tempAgtStr = tempAgtNameList.toString().replace(", ", "','").replace("[", "'").replace("]", "'");
	
		if(retName != null){
			String temp[] = new String[retName.length];
				for(int i=0; i<retName.length; i++) 
						temp[i] = retName[i].split("~")[0];
				tempRetNameList = Arrays.asList(temp);
		}
		if(tempRetNameList != null)
		tempRetStr = tempRetNameList.toString().replace(", ", "','").replace("[", "'").replace("]", "'");

		if (tempRetNameList != null && tempRetNameList.contains("-1")) 
		isAllRet = true;

		if (!isAllRet) {
			tempOrgStr = new StringBuilder("");
			if(tempAgtStr.length() == 0 && tempRetStr.length() != 0)
				tempOrgStr.append(tempRetStr);
			else if(tempAgtStr.length() != 0 && tempRetStr.length() == 0)
				tempOrgStr.append(tempAgtStr);
			else 
			tempOrgStr.append(tempAgtStr + "," + tempRetStr);
		
		/*String orgIdQry = "select organization_id from st_lms_organization_master where name in (" + tempOrgStr.toString() + ")";
		pstmt = con.prepareStatement(orgIdQry);
		System.out.println("orgIdQry:" + pstmt);
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			orgIdList.add(rs.getInt("organization_id"));
		}*/
		
		//tempOrgIdStr = orgIdList.toString().replace("[", "(").replace("]", ")").replace(" ", "");
		tempOrgIdStr = " and b.organization_id in (" + tempRetStr + ")";
	}
	
	//orgIdList = new ArrayList<Integer>();
	
	String qry = "select a.user_id, b.organization_id, a.phone_nbr from st_lms_user_contact_details a, st_lms_user_master b, st_lms_role_master c where b.isrolehead='Y' and a.user_id=b.user_id and c.is_master = 'Y' and b.role_id=c.role_id" + tempOrgIdStr;
	con = DBConnect.getConnection();
	con.setAutoCommit(false);
	pstmt = con.prepareStatement(qry);
	logger.info("phn no query:" + pstmt);
	rs = pstmt.executeQuery();
	
	orgIdList = new ArrayList<Integer>();
	userIdList = new ArrayList<Integer>();
	phoneNoList = new ArrayList<String>();
	while (rs.next()) {
		userIdList.add(rs.getInt("user_id"));
		orgIdList.add(rs.getInt("organization_id"));
		phoneNoList.add(rs.getString("phone_nbr"));
	}
	/*String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values('"
			+ new Timestamp(new Date().getTime())
			+ "',"
			+ creatorUserId
			+ ",'"
			+ message
			+ "','ACTIVE','"
			+ orgType
			+ "','"
			+ msgLocation + "','" + activity1 + "')";
	*/
	
	String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values(?,?,?,?,?,?,?)";
	pstmt=con.prepareStatement(query);
	pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
	pstmt.setInt(2, creatorUserId);
	pstmt.setString(3, message);
	pstmt.setString(4, "ACTIVE");
	pstmt.setString(5, orgType);
	pstmt.setString(6, msgLocation);
	pstmt.setString(7, activity1);
	logger.info("***************-" + query);
	pstmt.executeUpdate();
	rs = pstmt.getGeneratedKeys();

	if (rs.next()) 
		msgId = rs.getInt(1);
	//--
	//-- insert SMS details
	
	drawStmt=con.createStatement();
	for (int i = 0; i < orgIdList.size(); i++) {
		status = "Sent";
		Timestamp currTime = new Timestamp(new Date().getTime());
		if("Instant".equalsIgnoreCase(activity1)){
			try {
				Util.sendMsgToUsers(phoneNoList.get(i), message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if("Draw Perform".equalsIgnoreCase(activity1)){
			
		} else if ("Scheduled".equalsIgnoreCase(activity1)){
			
		}
		query = "insert into st_dg_adv_sms_details (user_id,org_id,phn_no,msg_id,status,time) values("+userIdList.get(i)+","+orgIdList.get(i)+",'"+phoneNoList.get(i)+"',"+msgId+",'"+status+"','"+currTime+"')";
		drawStmt.addBatch(query);
	}
	
	drawStmt.executeBatch();
	con.commit();
	result="success";

	}catch (LMSException e) {
		logger.error(e.getErrorMessage());
		logger.error("Exception in Adv Mssg " + e);
		result="error";
	}catch (SQLException e) {
		logger.error("Exception in Adv Mssg " + e);
		result="error";
	}catch (Exception e) {
		logger.error("Exception in Adv Mssg " + e);
		result="error";
	}finally{
		DBConnect.closeConnection(con, pstmt, drawStmt, rs);
	}

	return result;

}

}