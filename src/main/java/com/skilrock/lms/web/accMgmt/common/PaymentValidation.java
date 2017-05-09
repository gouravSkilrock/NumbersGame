package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class PaymentValidation {
	static Log logger = LogFactory.getLog(PaymentValidation.class);

	public static boolean isValidateCashAmount(double amount, int orgId) {
		if ((!CommonValidation.isEmpty(orgId))
				&& (!CommonValidation.isEmpty(amount)))
			return true;
		return false;
	}

	public static boolean isValidateDeposit(double amount, int orgId,
			String orgType, String receiptNo, String depositDate) {
		if ((CommonValidation.isValidAmount(amount))
				&& (!CommonValidation.isEmpty(orgId))
				&& (!CommonValidation.isEmpty(amount))
				&& (!CommonValidation.isEmpty(orgType))
				&& (!CommonValidation.isEmpty(receiptNo))
				&& (!CommonValidation.isEmpty(depositDate))
				&& (!CommonValidation.isEmpty(amount)))
			return true;
		return false;
	}

	public static boolean isValidateCreditNote(String agentName, double amount,
			String remarks) {
		if ((CommonValidation.isValidAmount(amount))
				&& (!CommonValidation.isEmpty(amount))
				&& (!CommonValidation.isEmpty(Integer.parseInt(agentName))))
			return true;
		return false;
	}

	public static boolean isValidateDebitNote(int agentId, double amount,
			String remarks) {
		if ((CommonValidation.isValidAmount(amount))
				&& (!CommonValidation.isEmpty(agentId)))
			return true;
		return false;
	}

	public static boolean isValidateRetailer(int agentOrgId, int retailerOrgId,
			Connection con) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select organization_id from st_lms_organization_master where organization_id=? and parent_id=?");
			pstmt.setInt(1, retailerOrgId);
			pstmt.setInt(2, agentOrgId);

			logger.info("Check Retailer Id with parent Id:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				throw new LMSException(
						LMSErrors.INVALIDATE_RETAILER_ERROR_CODE,
						LMSErrors.INVALIDATE_RETAILER_ERROR_MESSAGE);
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}

	}

	// Validate agent
	public static boolean isValidateAgent(int OrgId, int agentOrgId,
			Connection con) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select organization_id from st_lms_organization_master where organization_id=? and parent_id=?");
			pstmt.setInt(1, OrgId);
			pstmt.setInt(2, agentOrgId);

			logger.info("Check Agent Id with parent Id:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				throw new LMSException(LMSErrors.INVALIDATE_AGENT_ERROR_CODE,
						LMSErrors.INVALIDATE_AGENT_ERROR_MESSAGE);
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}

	}
}
