package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.CreditDebitNoteReportBean;
import com.skilrock.lms.common.db.DBConnect;

/**
 * This class acts as Helper class for CR-DR Report
 * 
 * @author umesh
 *
 */

public class CreditDebitNoteReportHelper {
	public List<CreditDebitNoteReportBean> fetchCreditDebitNoteReportData(
			int agtOrgId, Timestamp startDate, Timestamp endDate) {
		ArrayList<CreditDebitNoteReportBean> list = new ArrayList<CreditDebitNoteReportBean>();
		Connection con = DBConnect.getConnection();
		try {
			String appnenQuery = "";
			if (agtOrgId != -1) {
				appnenQuery = "  and agent_org_id=" + agtOrgId + " ";
			}
			String query = "select agent_org_id, name, sum(credit_amt) credit_amt, sum(debit_amt) debit_amt, reason from(select agent_org_id, bcn.reason, sum(bcn.amount) credit_amt,0.0 debit_amt from st_lms_bo_credit_note bcn, st_lms_bo_transaction_master btm where btm.transaction_type='CR_NOTE_CASH' and (btm.transaction_date>=? and btm.transaction_date<=?) and (bcn.transaction_id=btm.transaction_id) "
					+ appnenQuery
					+ " group by agent_org_id,reason union all select agent_org_id, bdn.reason, 0.0 credit_amt,sum(bdn.amount) debit_amt from st_lms_bo_debit_note bdn, st_lms_bo_transaction_master btm where (btm.transaction_type='DR_NOTE_CASH' or btm.transaction_type='DR_NOTE') and (btm.transaction_date>=? and btm.transaction_date<=?) and (bdn.transaction_id=btm.transaction_id) "
					+ appnenQuery
					+ " group by agent_org_id, reason) a, st_lms_organization_master b where a.agent_org_id=b.organization_id group by agent_org_id, reason";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setTimestamp(3, startDate);
			pstmt.setTimestamp(4, endDate);
			System.out.println("fetchCreditDebitNoteReportData query:" + pstmt);

			ResultSet rs = pstmt.executeQuery();

			CreditDebitNoteReportBean bean = new CreditDebitNoteReportBean();
			String reason = null;

			boolean flag = false;

			double crOthersTot = 0.0;
			double crAgainstLooseBooksReturnTot = 0.0;
			double crAgainstFaultyRechargeVouchersTot = 0.0;

			double drOthersTot = 0.0;
			double drAgainstLooseBooksReturnTot = 0.0;
			double drAgainstFaultyRechargeVouchersTot = 0.0;

			while (rs.next()) {
				int orgId = rs.getInt("agent_org_id");
				if (bean.getUserOrgId() != orgId) {
					bean = new CreditDebitNoteReportBean();
					bean.setUserOrgId(orgId);
					bean.setUserOrgName(rs.getString("name"));
					list.add(bean);
				}

				reason = rs.getString("reason");
				if ("OTHERS".equals(reason)) {
					bean.setCrOthers(rs.getDouble("credit_amt"));
					bean.setDrOthers(rs.getDouble("debit_amt"));

					flag = true;

					crOthersTot = crOthersTot + bean.getCrOthers();
					drOthersTot = drOthersTot + bean.getDrOthers();
				} else if ("AGAINST_LOOSE_BOOKS_RETURN".equals(reason)) {
					bean.setCrAgainstLooseBooksReturn(rs
							.getDouble("credit_amt"));
					bean
							.setDrAgainstLooseBooksReturn(rs
									.getDouble("debit_amt"));

					flag = true;

					crAgainstLooseBooksReturnTot = crAgainstLooseBooksReturnTot
							+ bean.getCrAgainstLooseBooksReturn();
					drAgainstLooseBooksReturnTot = drAgainstLooseBooksReturnTot
							+ bean.getDrAgainstLooseBooksReturn();
				} else if ("AGAINST_FAULTY_RECHARGE_VOUCHERS".equals(reason)) {
					bean.setCrAgainstFaultyRechargeVouchers(rs
							.getDouble("credit_amt"));
					bean.setDrAgainstFaultyRechargeVouchers(rs
							.getDouble("debit_amt"));

					flag = true;

					crAgainstFaultyRechargeVouchersTot = crAgainstFaultyRechargeVouchersTot
							+ bean.getCrAgainstFaultyRechargeVouchers();
					drAgainstFaultyRechargeVouchersTot = drAgainstFaultyRechargeVouchersTot
							+ bean.getDrAgainstFaultyRechargeVouchers();
				}
			}

			if (flag) {
				CreditDebitNoteReportBean totBean = new CreditDebitNoteReportBean();

				totBean
						.setCrAgainstFaultyRechargeVouchersTot(crAgainstFaultyRechargeVouchersTot);
				totBean
						.setCrAgainstLooseBooksReturnTot(crAgainstLooseBooksReturnTot);
				totBean.setCrOthersTot(crOthersTot);

				totBean
						.setDrAgainstFaultyRechargeVouchersTot(drAgainstFaultyRechargeVouchersTot);
				totBean
						.setDrAgainstLooseBooksReturnTot(drAgainstLooseBooksReturnTot);
				totBean.setDrOthersTot(drOthersTot);
				
				list.add(totBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return list;
	}
}
