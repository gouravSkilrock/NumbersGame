package com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl;

import java.security.MessageDigest;
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
import com.skilrock.lms.beans.VSRegistrationDataBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.virtualSport.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.VSUtil;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl.VirtualSportGamePlayDaoImpl;

public class CommonMethodsControllerImpl extends BaseAction {
	public CommonMethodsControllerImpl() {
		super(CommonMethodsControllerImpl.class.getName());
	}

	/**
	 * 	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger("CommonMethodsControllerImpl");

	private static CommonMethodsControllerImpl instance;


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

	public Map<Integer, String> getVSGameMapForAdvMessage() {
		Map<Integer, String> serviceMap = new TreeMap<Integer, String>();
		for (Map.Entry<Integer, GameMasterBean> entry : VSUtil.gameInfoMap.entrySet()) {
			serviceMap.put(entry.getKey(), entry.getValue().getGameDispName());
		}
		return serviceMap;
	}

	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getVSAdvMessageMap() {
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			orgMsgDetailMap = CommonMethodsDaoImpl.getInstance().getVSAdvMessageMap(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return orgMsgDetailMap;
	}

	public Map<String, List<String>> getVSAdvMessages(int orgId, int gameId) {
		Map<String, List<String>> messageMap = new HashMap<String, List<String>>();
		Set<MessageDetailsBean> messageSet = new HashSet<MessageDetailsBean>();

		if (VSUtil.advMessageMap.get(orgId) != null && VSUtil.advMessageMap.get(orgId).get(gameId) != null)
			messageSet.addAll(VSUtil.advMessageMap.get(orgId).get(gameId));
		if (VSUtil.advMessageMap.get(-1) != null && VSUtil.advMessageMap.get(-1).get(gameId) != null)
			messageSet.addAll(VSUtil.advMessageMap.get(-1).get(gameId));

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
	
	public Map<String, List<String>> getVSAdvMessages(int orgId, int gameId, String activity) {
		Map<String, List<String>> messageMap = new HashMap<String, List<String>>();
		Set<MessageDetailsBean> messageSet = new HashSet<MessageDetailsBean>();

		if (VSUtil.advMessageMap.get(orgId) != null && VSUtil.advMessageMap.get(orgId).get(gameId) != null)
			messageSet.addAll(VSUtil.advMessageMap.get(orgId).get(gameId));
		if (VSUtil.advMessageMap.get(-1) != null && VSUtil.advMessageMap.get(-1).get(gameId) != null)
			messageSet.addAll(VSUtil.advMessageMap.get(-1).get(gameId));

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

	public boolean authenticateRequest(String message, String sign) {
		String signature = null;
		MessageDigest md5;
		String security = null;
		try {
			signature = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_AUTHENTICATION_SIGNATURE");
			security = message + signature;
			md5 = MessageDigest.getInstance("MD5");
			byte[] hashMD5 = md5.digest(security.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < hashMD5.length; ++i) {
				sb.append(Integer.toHexString((hashMD5[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			if (sb.toString().equalsIgnoreCase(sign)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 
	 * @param userName
	 * @return response
	 * @throws VSException
	 * @author Rishi
	 */
	public VSRegistrationDataBean verifyAndFetchVSUser(String userName) throws VSException{
		Connection conn = null;
		VSRegistrationDataBean response = null;
		try{
			logger.info("Username: "+userName);
			conn = DBConnect.getConnection();
			response = CommonMethodsDaoImpl.getInstance().verifyAndFetchVSUser(userName, conn);
		} catch(VSException le){
			throw le;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(conn);
		}
		return response;
	}
	
	/**
	 * 
	 * @param shopId
	 * @return credit
	 * @throws VSException
	 * @author Rishi
	 * @throws LMSException 
	 */
	public double fetchUserCredit(int unitId) throws VSException, LMSException{
		double credit = 0.0;
		Connection conn = null;
		String userName = null;
		try{
			conn = DBConnect.getConnection();
			userName=VirtualSportGamePlayDaoImpl.getInstance().getUserNameFromRetPrinterId(unitId, conn);
			getUserBean(userName);
			credit = CommonMethodsDaoImpl.getInstance().fetchVSUserCredit(unitId, conn);
		} catch(VSException le){
			throw le;
		} catch(LMSException le){
			throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE,VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(conn);
		}
		return credit;
	}
	
	public int isTicketValid(String ticketNumber) throws VSException{
		int gameId = 0;
		Connection conn = null;
		try{
			conn = DBConnect.getConnection();
			gameId = CommonMethodsDaoImpl.getInstance().verifyTktAndFetchGameId(ticketNumber, conn);
		} catch(VSException le){
			throw le;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(conn);
		}
		return gameId;
	}
}
