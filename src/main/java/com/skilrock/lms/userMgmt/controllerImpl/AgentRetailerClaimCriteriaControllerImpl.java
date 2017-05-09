package com.skilrock.lms.userMgmt.controllerImpl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.userMgmt.daoImpl.AgentRetailerClaimCriteriaDaoImpl;
import com.skilrock.lms.userMgmt.javaBeans.UpdateClaimCriteriaBean;
import com.skilrock.lms.userMgmt.javaBeans.UpdatePayoutCenterBean;

public class AgentRetailerClaimCriteriaControllerImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataControllerImpl.class);

	private static AgentRetailerClaimCriteriaControllerImpl instance;

	private AgentRetailerClaimCriteriaControllerImpl() {
	}

	public static AgentRetailerClaimCriteriaControllerImpl getInstance() {
		if (instance == null) {
			synchronized (AgentRetailerClaimCriteriaControllerImpl.class) {
				if (instance == null) {
					instance = new AgentRetailerClaimCriteriaControllerImpl();
				}
			}
		}
		return instance;
	}

	public List<UpdateClaimCriteriaBean> fetchRetailerCriteriaList(int agentOrgId) throws LMSException {
		Connection connection = null;
		List<UpdateClaimCriteriaBean> retCriteriaList = null;
		try {
			connection = DBConnect.getConnection();
			retCriteriaList = AgentRetailerClaimCriteriaDaoImpl.getInstance().fetchRetailerCriteriaList(agentOrgId, connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return retCriteriaList;
	}

	public void retailerClaimCriteriaUpdate(List<UpdateClaimCriteriaBean> retCriteriaList, int doneByUserId, String requestIp) throws LMSException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			AgentRetailerClaimCriteriaDaoImpl.getInstance().retailerClaimCriteriaUpdate(retCriteriaList, doneByUserId, requestIp, connection);
			connection.commit();
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public List<UpdatePayoutCenterBean> fetchOrgPayoutList(int agentOrgId) throws LMSException {
		Connection connection = null;
		List<UpdatePayoutCenterBean> payoutCenterList = null;
		try {
			connection = DBConnect.getConnection();
			payoutCenterList = AgentRetailerClaimCriteriaDaoImpl.getInstance().fetchOrgPayoutList(agentOrgId, connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return payoutCenterList;
	}

	public void payoutCenterUpdate(List<UpdatePayoutCenterBean> payoutCenterList, int doneByUserId) throws LMSException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			AgentRetailerClaimCriteriaDaoImpl.getInstance().payoutCenterUpdate(payoutCenterList, doneByUserId, connection);
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