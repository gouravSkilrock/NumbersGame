package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CashChqReportForRetHelper {

	private Date endDate = null;
	private int parentOrgId;
	private int retailerOrgId;
	private Date startDate = null;

	public CashChqReportForRetHelper(UserInfoBean userInfoBean,
			DateBeans dateBeans) {
		this.retailerOrgId = userInfoBean.getUserOrgId();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.parentOrgId = userInfoBean.getParentOrgId();
	}

	public CashChqReportBean getCashChqDetail() throws LMSException {
		String query = null;
		CashChqReportBean reportbean = null;
		Connection con = DBConnect.getConnection();
		boolean isArchData = false;
		try {
			isArchData = ReportUtility.isArchData(startDate);
			query = "SELECT IFNULL(SUM(cash_amt), 0.0) AS 'total_cash', IFNULL(SUM(chq), 0.0)AS 'chq_coll', IFNULL(SUM(debit_amt), 0.0) AS 'debit_amt', IFNULL(SUM(credit_amt), 0.0) AS 'credit_amt', IFNULL(SUM(chq_bounce), 0.0) 'chq_bounce', ((IFNULL(SUM(cash_amt), 0)+IFNULL(SUM(chq),0)+IFNULL(SUM(credit_amt), 0))-(IFNULL(SUM(chq_bounce), 0)+IFNULL(SUM(debit_amt),0))) 'net_amount' FROM (SELECT * FROM ((SELECT IFNULL(SUM(cash.amount),0) 'cash_amt' FROM st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm WHERE btm.user_org_id=? AND (btm.transaction_date>=? AND btm.transaction_date< ?) AND cash.retailer_org_id=? AND btm.transaction_type='CASH' AND cash.transaction_id=btm.transaction_id ) aa, (SELECT IFNULL(SUM(chq.cheque_amt),0) 'chq' FROM  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm WHERE chq.transaction_type IN ('CHEQUE','CLOSED') AND btm.user_org_id=? AND (btm.transaction_date>=? AND btm.transaction_date < ?) AND chq.retailer_org_id=?  AND chq.transaction_id=btm.transaction_id)  bb, (SELECT IFNULL(SUM(chq.cheque_amt),0) 'chq_bounce' FROM  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm WHERE chq.transaction_type='CHQ_BOUNCE' AND btm.user_org_id=? AND (btm.transaction_date>=? AND btm.transaction_date< ?) AND chq.retailer_org_id=? AND chq.transaction_id=btm.transaction_id ) cc, (SELECT IFNULL(SUM(bo.amount),0) 'debit_amt'  FROM st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm WHERE btm.transaction_id=bo.transaction_id AND (bo.transaction_type ='DR_NOTE_CASH' OR bo.transaction_type ='DR_NOTE' ) AND btm.user_org_id=? AND bo.retailer_org_id =? AND ( btm.transaction_date>=? AND btm.transaction_date<?))gg, (SELECT IFNULL(SUM(bo.amount),0) 'credit_amt'  FROM st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm WHERE btm.transaction_id=bo.transaction_id AND (bo.transaction_type ='CR_NOTE_CASH' OR bo.transaction_type ='CR_NOTE' ) AND btm.user_org_id=? AND bo.retailer_org_id =? AND ( btm.transaction_date>=? AND btm.transaction_date<?))kk )";
			if (isArchData) {
				query += "UNION ALL (SELECT rep.cash_amt AS 'cash_amt', rep.cheque_amt AS 'chq', rep.cheque_bounce_amt AS 'chq_bounce' , rep.debit_note AS 'debit_amt', rep.credit_note AS 'credit_amt'FROM (SELECT IFNULL(SUM(cash_amt), 0.0) cash_amt, IFNULL(SUM(credit_note), 0.0) credit_note, IFNULL(SUM(debit_note), 0.0) debit_note, IFNULL(SUM(cheque_amt),0.0) cheque_amt, IFNULL(SUM(cheque_bounce_amt), 0.0) cheque_bounce_amt FROM st_rep_agent_payments WHERE parent_id = ? AND finaldate >= ? AND finaldate < ? GROUP BY retailer_org_id)rep)";
			}
			query += ") final";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, parentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			pstmt.setInt(4, retailerOrgId);

			pstmt.setInt(5, parentOrgId);
			pstmt.setDate(6, startDate);
			pstmt.setDate(7, endDate);
			pstmt.setInt(8, retailerOrgId);

			pstmt.setInt(9, parentOrgId);
			pstmt.setDate(10, startDate);
			pstmt.setDate(11, endDate);
			pstmt.setInt(12, retailerOrgId);

			pstmt.setInt(13, parentOrgId);
			pstmt.setInt(14, retailerOrgId);
			pstmt.setDate(15, startDate);
			pstmt.setDate(16, endDate);

			pstmt.setInt(17, parentOrgId);
			pstmt.setInt(18, retailerOrgId);
			pstmt.setDate(19, startDate);
			pstmt.setDate(20, endDate);

			if (isArchData) {
				pstmt.setInt(21, parentOrgId);
				pstmt.setDate(22, startDate);
				pstmt.setDate(23, endDate);
			}

			System.out.println("query--" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				reportbean = new CashChqReportBean();
				reportbean.setTotalCash(FormatNumber.formatNumber(rs
						.getDouble("total_cash")));
				reportbean.setTotalChq(FormatNumber.formatNumber(rs
						.getDouble("chq_coll")));
				reportbean.setCheqBounce(FormatNumber.formatNumber(rs
						.getDouble("chq_bounce")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("net_amount")));
				reportbean.setDebit(FormatNumber.formatNumber(rs
						.getDouble("debit_amt")));
				reportbean.setCredit(FormatNumber.formatNumber(rs
						.getDouble("credit_amt")));
				return reportbean;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return reportbean;
	}

}
