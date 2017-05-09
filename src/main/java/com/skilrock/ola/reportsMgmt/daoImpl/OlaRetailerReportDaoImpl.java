package com.skilrock.ola.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class OlaRetailerReportDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger("OlaRetailerReportDaoImpl");
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
		requestBean.setOrgId(3);
		requestBean.setFromDate("2010-01-01 00:00:00");
		requestBean.setToDate("2014-08-18 00:00:00");
		OlaOrgReportResponseBean responseBean = fetchDepositDataSingleRetailer(requestBean, DBConnect.getConnection());
		System.out.println(responseBean);
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static OlaOrgReportResponseBean fetchDepositDataSingleRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp-ref_deposit_mrp) mrp_dep_amt, SUM(deposit_net-ref_deposit_net_amt) net_dep_amt FROM st_rep_ola_retailer WHERE organization_id="+requestBean.getOrgId()+walletAppender+" AND finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'";

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, net_amt net_dep_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE dp.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE dpr.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND'").append(archiveAppender).append(")main;");
			logger.debug("fetchDepositDataSingleRetailer - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			if (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("retailer_org_id"));
				responseBean.setMrpDepositAmt(rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(rs.getDouble("net_dep_amt"));
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBean;
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, OlaOrgReportResponseBean> fetchDepositDataSingleRetailerDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();

			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, deposit_mrp-ref_deposit_mrp mrp_dep_amt, deposit_net-ref_deposit_net_amt net_dep_amt, finaldate FROM st_rep_ola_retailer WHERE organization_id="+requestBean.getOrgId()+walletAppender+" AND finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'";

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt, DATE(transaction_date) transaction_date FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, net_amt net_dep_amt, transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE dp.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt, transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE dpr.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND'").append(archiveAppender).append(")main GROUP BY DATE(transaction_date);");
			logger.debug("fetchDepositDataSingleRetailerDateWise - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("retailer_org_id"));
				responseBean.setMrpDepositAmt(rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(rs.getDouble("net_dep_amt"));
				responseBeanMap.put(rs.getString("transaction_date"), responseBean);
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBeanMap;
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, OlaOrgReportResponseBean> fetchDepositDataMultipleRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();

			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String retailerAppenderArchive = (requestBean.getOrgId() == 0)?"":" AND parent_id="+requestBean.getOrgId();
			String retailerAppender = (requestBean.getOrgId() == 0)?"":" INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id WHERE parent_id="+requestBean.getOrgId();
			String archiveAppender = " UNION ALL SELECT organization_id, deposit_mrp-ref_deposit_mrp mrp_dep_amt, deposit_net-ref_deposit_net_amt net_dep_amt FROM st_rep_ola_retailer WHERE finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"' "+retailerAppenderArchive+" "+walletAppender;

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, net_amt net_dep_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND'").append(archiveAppender).append(")main").append(retailerAppender).append(" GROUP BY retailer_org_id;");
			logger.debug("fetchDepositDataMultipleRetailer - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpDepositAmt(rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(rs.getDouble("net_dep_amt"));
				responseBeanMap.put(rs.getInt("retailer_org_id"), responseBean);
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBeanMap;
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static OlaOrgReportResponseBean fetchWithdrawDataSingleRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt FROM st_rep_ola_retailer WHERE organization_id="+requestBean.getOrgId()+walletAppender+" AND finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'";

			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_with_amt),0.00) mrp_with_amt, IFNULL(SUM(net_with_amt),0.00) net_with_amt FROM (SELECT rtm.retailer_org_id, SUM(withdrawl_amt) mrp_with_amt, SUM(net_amt) net_with_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE wd.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL'").append(archiveAppender).append(")main;");
			logger.debug("fetchWithdrawDataSingleRetailer - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			if (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(requestBean.getOrgId());
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_with_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_with_amt"));
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBean;
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, OlaOrgReportResponseBean> fetchWithdrawDataSingleRetailerDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, withdrawal_mrp mrp_with_amt, withdrawal_net_amt net_with_amt, finaldate FROM st_rep_ola_retailer WHERE organization_id="+requestBean.getOrgId()+walletAppender+" AND finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'";

			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_with_amt),0.00) mrp_with_amt, IFNULL(SUM(net_with_amt),0.00) net_with_amt, transaction_date FROM (SELECT rtm.retailer_org_id, SUM(withdrawl_amt) mrp_with_amt, SUM(net_amt) net_with_amt, DATE(transaction_date) transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE wd.retailer_org_id=").append(requestBean.getOrgId()).append(" AND transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL' GROUP BY DATE(transaction_date)").append(archiveAppender).append(")main GROUP BY DATE(transaction_date);");
			logger.debug("fetchWithdrawDataSingleRetailerDateWise - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("retailer_org_id"));
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_with_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_with_amt"));
				responseBeanMap.put(rs.getString("transaction_date"), responseBean);
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBeanMap;
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, OlaOrgReportResponseBean> fetchWithdrawDataMultipleRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();

			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, withdrawal_mrp, withdrawal_net_amt FROM st_rep_ola_retailer WHERE finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+walletAppender;

			withdrawQueryBuilder = new StringBuilder();
			if(requestBean.getOrgId() == 0) {
				withdrawQueryBuilder.append("SELECT retailer_org_id, IFNULL(SUM(mrp_with_amt),0.00) mrp_with_amt, IFNULL(SUM(net_with_amt),0.00) net_with_amt FROM (SELECT rtm.retailer_org_id, withdrawl_amt mrp_with_amt, net_amt net_with_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL'").append(archiveAppender).append(")main GROUP BY retailer_org_id;");
			} else {
				withdrawQueryBuilder.append("SELECT retailer_org_id, SUM(withdrawl_amt) mrp_with_amt, SUM(net_amt) net_with_amt FROM (SELECT rtm.retailer_org_id, withdrawl_amt, net_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL'").append(archiveAppender).append(" )main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id AND parent_id=").append(requestBean.getOrgId()).append(" GROUP BY retailer_org_id;");
			}
			logger.debug("fetchWithdrawDataMultipleRetailer - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_with_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_with_amt"));
				responseBeanMap.put(rs.getInt("retailer_org_id"), responseBean);
			}
		} catch(SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}

		return responseBeanMap;
	}
}