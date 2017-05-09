package com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEUtil;
import com.skilrock.lms.coreEngine.sportsLottery.common.daoImpl.CommonMethodsDaoImpl;

public class CommonMethodsControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger("CommonMethodsControllerImpl");

	private static CommonMethodsControllerImpl instance;

	private CommonMethodsControllerImpl(){}

	public static CommonMethodsControllerImpl getInstance() {
		if (instance == null) {
			synchronized (CommonMethodsControllerImpl.class) {
				if (instance == null) {
					instance = new CommonMethodsControllerImpl();
				}
			}
		}

		return instance;
	}

	public Map<Integer, GameMasterBean> getGameMap() {
		Map<Integer, GameMasterBean> gameInfoMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			gameInfoMap = CommonMethodsDaoImpl.getInstance().getGameMap(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return gameInfoMap;
	}

	public Map<Integer, GameTypeMasterBean> getGameTypeMap() {
		Map<Integer, GameTypeMasterBean> gameTypeInfoMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			gameTypeInfoMap = CommonMethodsDaoImpl.getInstance().getGameTypeMap(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return gameTypeInfoMap;
	}

	public Map<Integer, String> getSLEGameMapForAdvMessage() {
		Map<Integer, String> serviceMap = new TreeMap<Integer, String>();
		for(Map.Entry<Integer, GameTypeMasterBean> entry : SLEUtil.gameTypeInfoMap.entrySet()) {
			serviceMap.put(entry.getKey(), entry.getValue().getGameTypeDispName());
		}

		return serviceMap;
	}

	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getSLEAdvMessageMap() {
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			orgMsgDetailMap = CommonMethodsDaoImpl.getInstance().getSLEAdvMessageMap(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return orgMsgDetailMap;
	}

	public Map<String, List<String>> getSLEAdvMessages(int orgId, int gameTypeId) {
		Map<String, List<String>> messageMap = new HashMap<String, List<String>>();
		Set<MessageDetailsBean> messageSet = new HashSet<MessageDetailsBean>();

		if(SLEUtil.advMessageMap.get(orgId)!=null && SLEUtil.advMessageMap.get(orgId).get(gameTypeId)!=null)
			messageSet.addAll(SLEUtil.advMessageMap.get(orgId).get(gameTypeId));
		if(SLEUtil.advMessageMap.get(-1)!=null && SLEUtil.advMessageMap.get(-1).get(gameTypeId)!=null)
			messageSet.addAll(SLEUtil.advMessageMap.get(-1).get(gameTypeId));

		String messageLocation = null;
		for(MessageDetailsBean bean : messageSet) {
			messageLocation = bean.getMessageLocation();
			if(messageMap.containsKey(messageLocation)) {
				messageMap.get(messageLocation).add(bean.getMessageText());
			} else {
				List<String> tempList = new ArrayList<String>();
				tempList.add(bean.getMessageText());
				messageMap.put(messageLocation, tempList);
			}
		}

		return messageMap;
	}
}