package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.PriviledgeModificationDaoImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.PriviledgeModificationMasterBean;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;

public class PriviledgeModificationControllerImpl {
	final static Log logger = LogFactory.getLog(PriviledgeModificationControllerImpl.class);

	private static PriviledgeModificationControllerImpl instance;

	private PriviledgeModificationControllerImpl() {
	}

	public static PriviledgeModificationControllerImpl getInstance() {
		if (instance == null) {
			synchronized (PriviledgeModificationControllerImpl.class) {
				if (instance == null) {
					instance = new PriviledgeModificationControllerImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, String> getBoUsersList() throws LMSException {
		Connection connection = null;
		Map<Integer, String> boUserMap = null;
		try {
			connection = DBConnect.getConnection();
			boUserMap = PriviledgeModificationDaoImpl.getInstance().getBoUsersList(connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return boUserMap;
	}

	public Map<String, String> getServiceMap() throws LMSException {
		Connection connection = null;
		Map<String, String> serviceMap = null;
		try {
			connection = DBConnect.getConnection();
			serviceMap = PriviledgeModificationDaoImpl.getInstance().getServiceMap(connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return serviceMap;
	}

	public PriviledgeModificationMasterBean fetchUserPriviledgeHistoryData(int userId, Timestamp startTime, Timestamp endTime, String serviceCode) throws LMSException {
		Connection connection = null;
		PriviledgeModificationMasterBean masterBean = null;
		try {
			connection = DBConnect.getConnection();

			if("MGMT".equals(serviceCode) || "DG".equals(serviceCode)) {
				masterBean = PriviledgeModificationDaoImpl.getInstance().fetchUserPriviledgeHistoryData(userId, startTime, endTime, serviceCode, connection);
			} else if("SLE".equals(serviceCode)) {
				if(ServicesBean.isSLE()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					masterBean = new PriviledgeModificationMasterBean();
					masterBean.setUserId(userId);
					masterBean.setStartDate(dateFormat.format(startTime));
					masterBean.setEndDate(dateFormat.format(endTime));
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.FETCH_USER_PRIV_HISTORY, masterBean);

					masterBean = (PriviledgeModificationMasterBean) notifySLE.asyncCall(notifySLE);

					PriviledgeModificationDaoImpl.getInstance().getUserBasicData(userId, masterBean, connection);
				}
			}
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return masterBean;
	}
}