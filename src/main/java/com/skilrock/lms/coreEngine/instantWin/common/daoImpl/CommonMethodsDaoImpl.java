package com.skilrock.lms.coreEngine.instantWin.common.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.coreEngine.instantWin.beans.GameMasterBean;

public class CommonMethodsDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger("CommonMethodsDaoImpl");

	private static CommonMethodsDaoImpl instance;

	private CommonMethodsDaoImpl() {
	}

	public static CommonMethodsDaoImpl getInstance() {
		if (instance == null) {
			synchronized (CommonMethodsDaoImpl.class) {
				if (instance == null) {
					instance = new CommonMethodsDaoImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, GameMasterBean> getGameMap(Connection connection) {
		Map<Integer, GameMasterBean> gameInfoMap = new TreeMap<Integer, GameMasterBean>();
		GameMasterBean gameBean = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT game_id, game_no, game_dev_name, game_disp_name FROM st_iw_game_master WHERE game_status='SALE_OPEN';";
			logger.info("getGameMap Query - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				gameBean = new GameMasterBean();
				gameBean.setGameId(rs.getInt("game_id"));
				gameBean.setGameNo(rs.getInt("game_no"));
				gameBean.setGameDevName(rs.getString("game_dev_name"));
				gameBean.setGameDispName(rs.getString("game_disp_name"));

				gameInfoMap.put(rs.getInt("game_id"), gameBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gameInfoMap;
	}

	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getIWAdvMessageMap(Connection connection) {
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		List<MessageDetailsBean> tempList = null;
		MessageDetailsBean messageDetailsBean = null;
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT advMap.org_id, advMap.game_id, advMas.msg_id, advMas.date, advMas.creator_user_id, advMas.msg_text, advMas.status, advMas.editable, advMas.msg_for, advMas.msg_location, advMas.activity FROM st_dg_adv_msg_org_mapping advMap INNER JOIN st_dg_adv_msg_master advMas ON advMap.msg_id=advMas.msg_id AND advMas.status='ACTIVE' AND advMas.activity IN ('SALE', 'PWT', 'ALL') AND advMas.msg_for='PLAYER' AND advMap.service_id=(SELECT service_id FROM st_lms_service_master WHERE service_code='IW') ORDER BY game_id,org_id;";
			logger.info("getIWAdvMessageMap Query - " + query);
			rs = stmt.executeQuery(query);
			orgMsgDetailMap = new HashMap<Integer, Map<Integer, List<MessageDetailsBean>>>();
			while (rs.next()) {
				messageDetailsBean = new MessageDetailsBean();
				messageDetailsBean.setMessageId(rs.getInt("msg_id"));
				messageDetailsBean.setDate(rs.getTimestamp("date"));
				messageDetailsBean.setCreatorUserId(rs.getInt("creator_user_id"));
				messageDetailsBean.setMessageText(rs.getString("msg_text"));
				messageDetailsBean.setStatus(rs.getString("status"));
				messageDetailsBean.setEditable(rs.getString("editable"));
				messageDetailsBean.setMessageFor(rs.getString("msg_for"));
				messageDetailsBean.setMessageLocation(rs.getString("msg_location"));
				messageDetailsBean.setActivity(rs.getString("activity"));

				int orgId = rs.getInt("org_id");
				int gameId = rs.getInt("game_id");

				if (orgMsgDetailMap.containsKey(orgId)) {
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = orgMsgDetailMap.get(orgId);
					if (gameMsgDetailMap.containsKey(gameId)) {
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return orgMsgDetailMap;
	}
}