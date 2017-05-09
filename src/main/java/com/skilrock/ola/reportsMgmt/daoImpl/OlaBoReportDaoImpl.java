package com.skilrock.ola.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class OlaBoReportDaoImpl {
	
	public static OlaOrgReportResponseBean fetchWithdrawDirectPlayerDataBO(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
				String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
				String archiveAppender = " UNION ALL SELECT organization_id, SUM(withdrawal_mrp) with_amt FROM st_rep_ola_bo_dir WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'  and wallet_id = "+requestBean.getWalletId()+" AND organization_id="+requestBean.getOrgId();
				withdrawQueryBuilder = new StringBuilder();
				withdrawQueryBuilder.append("SELECT bo_org_id, IFNULL(SUM(withdrawl_amt),0.0) with_amt FROM st_lms_bo_transaction_master atm INNER JOIN st_ola_bo_direct_plr_withdrawl dw ON atm.transaction_id=dw.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_WITHDRAWL_PLR' AND bo_org_id=").append(requestBean.getOrgId()).append(archiveAppender);
				stmt = connection.createStatement();
				rs = stmt.executeQuery(withdrawQueryBuilder.toString());
				responseBean = new OlaOrgReportResponseBean();
				int orgId = 0;
				double withAmt = 0.0;
				while(rs.next()) {
					orgId = orgId + rs.getInt("bo_org_id");
					withAmt = withAmt + rs.getDouble("with_amt");
				}
				responseBean.setOrgId(orgId);
				responseBean.setMrpWithdrawalAmt(withAmt);
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBean;
	}
	
	
	public static OlaOrgReportResponseBean fetchDepositDirectPlayerDataBO(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQryBuilder = null;
		try {
				String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
				String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp) dep_amt FROM st_rep_ola_bo_dir WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'  and wallet_id = "+requestBean.getWalletId()+" AND organization_id="+requestBean.getOrgId();
				depositQryBuilder = new StringBuilder();
				depositQryBuilder.append("SELECT bo_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt FROM (SELECT bo_org_id,deposit_amt mrp_dep_amt FROM st_lms_bo_transaction_master atm INNER JOIN st_ola_bo_direct_plr_deposit dp ON atm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_PLR' AND bo_org_id=").append(requestBean.getOrgId()).append(" UNION ALL SELECT bo_org_id, -deposit_amt mrp_refund_amt FROM st_lms_bo_transaction_master atm INNER JOIN st_ola_bo_direct_plr_deposit_refund dpr ON atm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_REFUND_PLR' AND bo_org_id=").append(requestBean.getOrgId()).append(archiveAppender).append(" )main");
				stmt = connection.createStatement();
				rs = stmt.executeQuery(depositQryBuilder.toString());
				responseBean = new OlaOrgReportResponseBean();
				int orgId = 0;
				double depAmt = 0.0;
				while(rs.next()) {					
					orgId = orgId + rs.getInt("bo_org_id");
					depAmt = depAmt + rs.getDouble("mrp_dep_amt");
				}
				responseBean.setOrgId(orgId);
				responseBean.setMrpDepositAmt(depAmt);
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBean;
	}


}
