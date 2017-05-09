package com.skilrock.lms.coreEngine.instantWin.common.controllerImpl;

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
import com.skilrock.lms.coreEngine.instantWin.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.coreEngine.instantWin.common.daoImpl.CommonMethodsDaoImpl;

public class CommonMethodsControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger("CommonMethodsControllerImpl");

	private static CommonMethodsControllerImpl instance;

	private CommonMethodsControllerImpl() {
	}

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

	public Map<Integer, String> getIWGameMapForAdvMessage() {
		Map<Integer, String> serviceMap = new TreeMap<Integer, String>();
		for (Map.Entry<Integer, GameMasterBean> entry : IWUtil.gameInfoMap.entrySet()) {
			serviceMap.put(entry.getKey(), entry.getValue().getGameDispName());
		}
		return serviceMap;
	}

	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getIWAdvMessageMap() {
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			orgMsgDetailMap = CommonMethodsDaoImpl.getInstance().getIWAdvMessageMap(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return orgMsgDetailMap;
	}

	public Map<String, List<String>> getIWAdvMessages(int orgId, int gameId) {
		Map<String, List<String>> messageMap = new HashMap<String, List<String>>();
		Set<MessageDetailsBean> messageSet = new HashSet<MessageDetailsBean>();

		if (IWUtil.advMessageMap.get(orgId) != null && IWUtil.advMessageMap.get(orgId).get(gameId) != null)
			messageSet.addAll(IWUtil.advMessageMap.get(orgId).get(gameId));
		if (IWUtil.advMessageMap.get(-1) != null && IWUtil.advMessageMap.get(-1).get(gameId) != null)
			messageSet.addAll(IWUtil.advMessageMap.get(-1).get(gameId));

		String messageLocation = null;
		for (MessageDetailsBean bean : messageSet) {
			messageLocation = bean.getMessageLocation();
			if (messageMap.containsKey(messageLocation)) {
				messageMap.get(messageLocation).add(bean.getMessageText());
			} else {
				List<String> tempList = new ArrayList<String>();
				tempList.add(bean.getMessageText());
				messageMap.put(messageLocation, tempList);
			}
		}
		return messageMap;
	}
	
	public Map<String, List<String>> getIWAdvMessages(int orgId, int gameId, String activity) {
		Map<String, List<String>> messageMap = new HashMap<String, List<String>>();
		Set<MessageDetailsBean> messageSet = new HashSet<MessageDetailsBean>();

		if (IWUtil.advMessageMap!=null && IWUtil.advMessageMap.get(orgId) != null && IWUtil.advMessageMap.get(orgId).get(gameId) != null)
			messageSet.addAll(IWUtil.advMessageMap.get(orgId).get(gameId));
		if (IWUtil.advMessageMap!=null && IWUtil.advMessageMap.get(-1) != null && IWUtil.advMessageMap.get(-1).get(gameId) != null)
			messageSet.addAll(IWUtil.advMessageMap.get(-1).get(gameId));

		String messageLocation = null;
		for (MessageDetailsBean bean : messageSet) {
			messageLocation = bean.getMessageLocation();
			if (messageMap.containsKey(messageLocation)) {
				if (activity.equalsIgnoreCase(bean.getActivity()) || "ALL".equalsIgnoreCase(bean.getActivity()))
					messageMap.get(messageLocation).add(bean.getMessageText());
			} else {
				if (activity.equalsIgnoreCase(bean.getActivity()) || "ALL".equalsIgnoreCase(bean.getActivity())) {
					List<String> tempList = new ArrayList<String>();
					tempList.add(bean.getMessageText());
					messageMap.put(messageLocation, tempList);
				}
			}
		}
		return messageMap;
	}
}