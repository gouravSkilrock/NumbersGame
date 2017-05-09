package com.skilrock.lms.coreEngine.instantPrint.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;

public class CommonMethods {
	static ServletContext sc = LMSUtility.sc;
	static String countryDeployed = (String) sc
			.getAttribute("COUNTRY_DEPLOYED");

	public static double fmtToTwoDecimal(double number) {
		return Math.round((number * 100)) / 100.0;

	}

	public OrgPwtLimitBean fetchPwtLimitsOfOrgnization(int organizationId,
			Connection con) throws SQLException {

		OrgPwtLimitBean bean = null;
		String query = "select aa.organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, bb.pwt_scrap from st_lms_oranization_limits aa, st_lms_organization_master bb where  aa.organization_id = bb.organization_id and  aa.organization_id = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, organizationId);
		ResultSet result = pstmt.executeQuery();
		if (result.next()) {
			bean = new OrgPwtLimitBean();
			bean.setOrganizationId(organizationId);
			bean.setVerificationLimit(result.getDouble("verification_limit"));
			bean.setApprovalLimit(result.getDouble("approval_limit"));
			bean.setPayLimit(result.getDouble("pay_limit"));
			bean.setScrapLimit(result.getDouble("scrap_limit"));
			bean.setIsPwtAutoScrap(result.getString("pwt_scrap"));
		}
		return bean;
	}

	public static double calTaxableSale(double ticketMrp, double saleCommRate,
			double prizePayOutRatio, double govtComm, double vat)
			throws LMSException {
		double taxableSale = 0.0;

		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp * (100 - (prizePayOutRatio + govtComm))
					/ 100 * 100 / (100 + vat);
		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else {
			throw new LMSException();
		}
		return taxableSale;
	}

	public static double calculateVatPlr(double mrpAmt, double plrSaleCommRate,
			double prizePayOutRatio, double govtComm, double vat)
			throws LMSException {
		double vatAmt = 0.0;
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
					* vat * 0.01 / (1 + vat * 0.01);

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// NO VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else {
			throw new LMSException();
		}
		return vatAmt;
	}

	public synchronized static boolean updateOrgBalance(String claimType,
			double amount, int orgId, String amtUpdateType,
			Connection connection) throws SQLException {

		// check whether amount type is debit or credit
		amount = "DEBIT".equals(amtUpdateType) ? -1 * amount : amount;
		System.out.println("claimType " + claimType + " ::amtUpdateType "
				+ amtUpdateType);
		// tem.out.println("claimType " + claimType + " ::amtUpdateType " +
		// amtUpdateType);
		String updateRetBalQuery = null;
		if ("UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?";
		} else if ("CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal+?) "
					+ " , organization_status = if((available_credit-claimable_bal)>0, 'ACTIVE', 'INACTIVE')"
					+ " where organization_id = ?";
			// updateRetBalQuery = "update st_lms_organization_master set
			// claimable_bal = (claimable_bal+?) where organization_id = ?";
		} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		}
		PreparedStatement updateRetBal = connection
				.prepareStatement(updateRetBalQuery);
		updateRetBal.setDouble(1, amount);
		if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBal.setDouble(2, amount);
			updateRetBal.setInt(3, orgId);
		} else {
			updateRetBal.setInt(2, orgId);
		}
		int retBalRow = updateRetBal.executeUpdate();
		System.out.println(retBalRow + " row updated,  updateRetBalQuery = "
				+ updateRetBal);
		// tem.out.println(retBalRow + " row updated, updateRetBalQuery = " +
		// updateRetBal);
		return retBalRow > 0;
	}
}
