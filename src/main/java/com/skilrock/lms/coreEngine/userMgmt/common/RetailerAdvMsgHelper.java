package com.skilrock.lms.coreEngine.userMgmt.common;


import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.common.db.DBConnect;



public class RetailerAdvMsgHelper {
	static Log logger = LogFactory.getLog(RetailerAdvMsgHelper.class);	
	
	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getAdvMsgDataMap() {

		ResultSet set = null;
		Connection con = null;
		Statement statement=null;
		List<MessageDetailsBean> tempList = null;
		MessageDetailsBean messageDetailsBean = null;
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		
		try {
			con = DBConnect.getConnection();
			statement = con.createStatement();
			set = statement.executeQuery("select advMap.org_id, advMap.game_id, advMas.msg_id, advMas.date, advMas.creator_user_id, advMas.msg_text, advMas.status, advMas.editable, advMas.msg_for, advMas.msg_location, advMas.activity from st_dg_adv_msg_org_mapping advMap inner join st_dg_adv_msg_master advMas on advMap.msg_id = advMas.msg_id and advMas.status = 'ACTIVE' and advMas.activity in('SALE','ALL') and advMas.msg_for = 'PLAYER' and advMap.service_id = (select service_id from st_lms_service_master where service_code='DG') order by game_id,org_id");

			orgMsgDetailMap = new HashMap<Integer, Map<Integer,List<MessageDetailsBean>>>();
			while(set.next())
			{
				messageDetailsBean = new MessageDetailsBean();
				messageDetailsBean.setMessageId(set.getInt("msg_id"));
				messageDetailsBean.setDate(set.getTimestamp("date"));
				messageDetailsBean.setCreatorUserId(set.getInt("creator_user_id"));
				messageDetailsBean.setMessageText(set.getString("msg_text"));
				messageDetailsBean.setStatus(set.getString("status"));
				messageDetailsBean.setEditable(set.getString("editable"));
				messageDetailsBean.setMessageFor(set.getString("msg_for"));
				messageDetailsBean.setMessageLocation(set.getString("msg_location"));
				messageDetailsBean.setActivity(set.getString("activity"));

				int orgId = set.getInt("org_id");
				int gameId = set.getInt("game_id");
				
				if(orgMsgDetailMap.containsKey(orgId)){
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = orgMsgDetailMap.get(orgId);
					if(gameMsgDetailMap.containsKey(gameId)) {
						gameMsgDetailMap.get(gameId).add(messageDetailsBean);
					} else {
						tempList = new ArrayList<MessageDetailsBean>();
						tempList.add(messageDetailsBean);
						gameMsgDetailMap.put(gameId, tempList);
					}
				} else {
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = new HashMap<Integer, List<MessageDetailsBean>>();
					tempList = new ArrayList<MessageDetailsBean>();
					tempList.add(messageDetailsBean);
					gameMsgDetailMap.put(gameId, tempList);
					orgMsgDetailMap.put(orgId, gameMsgDetailMap);
				}
			}
		}
		catch (SQLException e) {
			logger.error("SQL Exception  :- " + e);
		}catch (Exception e) {
			logger.error("General Exception  :- " + e);
		}
		finally {
			DBConnect.closeConnection(con, statement, set);
		}
		return orgMsgDetailMap;
	}

}
