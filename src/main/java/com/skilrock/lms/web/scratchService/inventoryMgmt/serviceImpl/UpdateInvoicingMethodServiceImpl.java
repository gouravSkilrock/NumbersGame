package com.skilrock.lms.web.scratchService.inventoryMgmt.serviceImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.scratchService.inventoryMgmt.daoImpl.UpdateInvoicingMethodDaoImpl;
import com.skilrock.lms.web.scratchService.inventoryMgmt.javaBeans.AgentInvoicingMethodBean;

public class UpdateInvoicingMethodServiceImpl {
	final static Log logger = LogFactory.getLog(UpdateInvoicingMethodServiceImpl.class);

	private static UpdateInvoicingMethodServiceImpl instance;

	private UpdateInvoicingMethodServiceImpl() {
	}

	public static UpdateInvoicingMethodServiceImpl getInstance() {
		if (instance == null) {
			synchronized (UpdateInvoicingMethodServiceImpl.class) {
				if (instance == null) {
					instance = new UpdateInvoicingMethodServiceImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, String> activeGameMap() throws LMSException {
		Connection connection = null;
		Map<Integer, String> gameMap = null;
		try {
			connection = DBConnect.getConnection();
			gameMap = UpdateInvoicingMethodDaoImpl.getInstance().activeGameMap(connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return gameMap;
	}

	public List<AgentInvoicingMethodBean> getAgentInvoicingMethod(int gameId) throws LMSException {
		Connection connection = null;
		List<AgentInvoicingMethodBean> agentDetailList = null;
		try {
			connection = DBConnect.getConnection();
			agentDetailList = UpdateInvoicingMethodDaoImpl.getInstance().getAgentInvoicingMethod(gameId, connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return agentDetailList;
	}

	public Map<Integer,String> getInvoicingMethods() throws LMSException {
		Connection connection = null;
		Map<Integer,String> invoiceMap = null;
		try {
			connection = DBConnect.getConnection();
			invoiceMap = UpdateInvoicingMethodDaoImpl.getInstance().getInvoicingMethods(connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return invoiceMap;
	}

	public Map<Integer,String> getMethodIdMap() throws LMSException {
		Connection connection = null;
		Map<Integer,String> methodIdMap = null;
		try {
			connection = DBConnect.getConnection();
			methodIdMap = UpdateInvoicingMethodDaoImpl.getInstance().getInvoiceMethodIdMap(connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return methodIdMap;
	}

	public void updateInvoicingMethod(int gameId, List<AgentInvoicingMethodBean> agentDetailList, int doneByUserId, String requestIp) throws LMSException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			UpdateInvoicingMethodDaoImpl.getInstance().updateInvoicingMethod(gameId, agentDetailList, doneByUserId, requestIp, connection);
			connection.commit();
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}
}