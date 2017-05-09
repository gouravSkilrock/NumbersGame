package com.skilrock.ola.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.Date;
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

public class OlaAgentReportDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger("OlaAgentReportDaoImpl");
	
	public static void main(String[] args) throws Exception {
		OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
		requestBean.setOrgId(3);
		requestBean.setFromDate("2014-08-28 00:00:00");
		requestBean.setToDate("2014-08-29 00:00:00");
		OlaOrgReportResponseBean responseBean = fetchDepositDataSingleAgent(requestBean, DBConnect.getConnection());
		System.out.println(responseBean);
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static OlaOrgReportResponseBean fetchDepositDataSingleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp-ref_deposit_mrp) mrp_dep_amt, SUM(deposit_net-ref_deposit_net_amt) net_dep_amt FROM st_rep_ola_agent WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"' AND organization_id="+requestBean.getOrgId()+walletAppender;

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT parent_id agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, agent_net_amt net_dep_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -agent_net_amt net_refund_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND')main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id WHERE parent_id=").append(requestBean.getOrgId()).append(archiveAppender);
			logger.debug("fetchDepositDataSingleAgent - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			responseBean = new OlaOrgReportResponseBean();
			if (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("agent_org_id"));
				responseBean.setMrpDepositAmt(responseBean.getMrpDepositAmt() + rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(responseBean.getNetDepositAmt() + rs.getDouble("net_dep_amt"));
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
	public static Map<String, OlaOrgReportResponseBean> fetchDepositDataSingleAgentDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();

			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String agentAppender = (requestBean.getOrgId() == 0)?"":" WHERE parent_id="+requestBean.getOrgId();
			String agentArchiveAppender = (requestBean.getOrgId() == 0)?"":" AND organization_id = "+requestBean.getOrgId();
			String archiveAppender = " UNION ALL SELECT organization_id, deposit_mrp-ref_deposit_mrp mrp_dep_amt, deposit_net-ref_deposit_net_amt net_dep_amt, finaldate FROM st_rep_ola_agent Where  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"' "+walletAppender+agentArchiveAppender;

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT parent_id agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt, DATE(transaction_date) transaction_date FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, agent_net_amt net_dep_amt, transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -agent_net_amt net_refund_amt, transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND')main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id").append(agentAppender).append(" GROUP BY DATE(transaction_date)").append(archiveAppender);
			logger.debug("fetchDepositDataSingleRetailer - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			while (rs.next()) {
				responseBean = responseBeanMap.get(rs.getInt("transaction_date"));
				if(responseBean == null) {
					responseBean = new OlaOrgReportResponseBean();
				}
				responseBean.setOrgId(rs.getInt("agent_org_id"));
				responseBean.setMrpDepositAmt(responseBean.getMrpDepositAmt() + rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(responseBean.getNetDepositAmt() + rs.getDouble("net_dep_amt"));
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
	public static OlaOrgReportResponseBean fetchDepositDirectPlayerDataSingleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp-ref_deposit_mrp) mrp_dep_amt, SUM(deposit_net-ref_deposit_net_amt) net_dep_amt FROM st_rep_ola_agent WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"' AND organization_id="+requestBean.getOrgId()+walletAppender;

			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT agent_org_id,deposit_amt mrp_dep_amt, net_amt net_dep_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit dp ON atm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_PLR' AND agent_org_id=").append(requestBean.getOrgId()).append(" UNION ALL SELECT agent_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit_refund dpr ON atm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_REFUND_PLR' AND agent_org_id=").append(requestBean.getOrgId()).append(archiveAppender).append(")main;");
			logger.debug("fetchDepositDirectPlayerDataSingleAgent - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			if (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("agent_org_id"));
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
	public static Map<String, OlaOrgReportResponseBean> fetchDepositDirectPlayerDataSingleAgentDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {			
				String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
				String agentAppender = (requestBean.getOrgId() == 0)?"":" AND agent_org_id="+requestBean.getOrgId();
				String agentArchiveAppender = (requestBean.getOrgId() == 0)?"":" AND organization_id="+requestBean.getOrgId();
				String archiveAppender = " UNION ALL SELECT organization_id, deposit_mrp-ref_deposit_mrp mrp_dep_amt, deposit_net-ref_deposit_net_amt net_dep_amt, finaldate FROM st_rep_ola_agt_dir WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+agentArchiveAppender+walletAppender;
				responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();
				depositQueryBuilder = new StringBuilder();
				depositQueryBuilder.append("SELECT agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt, DATE(transaction_date) transaction_date FROM (SELECT agent_org_id,deposit_amt mrp_dep_amt, net_amt net_dep_amt, transaction_date FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit dp ON atm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_PLR'").append(agentAppender).append(" UNION ALL SELECT agent_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt, transaction_date FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit_refund dpr ON atm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_REFUND_PLR'").append(agentAppender).append(")main GROUP BY DATE(transaction_date)").append(archiveAppender);
				logger.debug("fetchDepositDirectPlayerDataSingleAgentDateWise - "+depositQueryBuilder.toString());

				stmt = connection.createStatement();
				rs = stmt.executeQuery(depositQueryBuilder.toString());
				while (rs.next()) {
					responseBean = new OlaOrgReportResponseBean();
					responseBean.setOrgId(rs.getInt("agent_org_id"));
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
	public static Map<Integer, OlaOrgReportResponseBean> fetchDepositDataMultipleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp-ref_deposit_mrp) mrp_dep_amt, SUM(deposit_net-ref_deposit_net_amt) net_dep_amt FROM st_rep_ola_agent WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+walletAppender+" group by organization_id";
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();
			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("select agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT om.parent_id agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT rtm.retailer_org_id, deposit_amt mrp_dep_amt, agent_net_amt net_dep_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit dp ON rtm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT' UNION ALL SELECT rtm.retailer_org_id, -deposit_amt mrp_refund_amt, -agent_net_amt net_refund_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_deposit_refund dpr ON rtm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_DEPOSIT_REFUND')main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id GROUP BY om.parent_id ").append(archiveAppender).append(" )tmp group by agent_org_id");
			logger.debug("fetchDepositDataMultipleAgent - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpDepositAmt(rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(rs.getDouble("net_dep_amt"));
				responseBeanMap.put(rs.getInt("agent_org_id"), responseBean);
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
	public static Map<Integer, OlaOrgReportResponseBean> fetchDepositDirectPlayerDataMultipleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder depositQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(deposit_mrp-ref_deposit_mrp) mrp_dep_amt, SUM(deposit_net-ref_deposit_net_amt) net_dep_amt FROM st_rep_ola_agt_dir WHERE   date(finaldate)>=date('"+requestBean.getFromDate()+"') AND date(finaldate)<=date('"+requestBean.getToDate()+"')"+walletAppender+" group by organization_id ";
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();
			depositQueryBuilder = new StringBuilder();
			depositQueryBuilder.append("SELECT agent_org_id, IFNULL(SUM(mrp_dep_amt),0.0) mrp_dep_amt, IFNULL(SUM(net_dep_amt),0.00) net_dep_amt FROM (SELECT agent_org_id, deposit_amt mrp_dep_amt, net_amt net_dep_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit dp ON atm.transaction_id=dp.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_PLR' UNION ALL SELECT agent_org_id, -deposit_amt mrp_refund_amt, -net_amt net_refund_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_deposit_refund dpr ON atm.transaction_id=dpr.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_DEPOSIT_REFUND_PLR' ").append(archiveAppender).append(" )main GROUP BY agent_org_id;");
			logger.debug("fetchDepositDirectPlayerDataMultipleAgent - "+depositQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(depositQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpDepositAmt(rs.getDouble("mrp_dep_amt"));
				responseBean.setNetDepositAmt(rs.getDouble("net_dep_amt"));
				responseBeanMap.put(rs.getInt("agent_org_id"), responseBean);
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
	public static OlaOrgReportResponseBean fetchWithdrawDataSingleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " union all SELECT organization_id, SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt FROM st_rep_ola_retailer WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"' AND organization_id="+requestBean.getOrgId()+walletAppender;
			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("SELECT parent_id agent_org_id, IFNULL(SUM(mrp_wid_amt),0.0) mrp_with_amt, IFNULL(SUM(net_wid_amt),0.00) net_with_amt FROM (SELECT rtm.retailer_org_id,withdrawl_amt mrp_wid_amt, agent_net_amt net_wid_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL' ").append(archiveAppender).append(")main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id WHERE parent_id=").append(requestBean.getOrgId());
			logger.debug("fetchWithdrawDataSingleAgent - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			if (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("agent_org_id"));
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
	public static Map<String, OlaOrgReportResponseBean> fetchWithdrawDataSingleAgentDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String agentAppender = (requestBean.getOrgId() == 0)?"":" AND parent_id="+requestBean.getOrgId();
			String agentArchiveAppender = (requestBean.getOrgId() == 0)?"":" AND organization_id="+requestBean.getOrgId();
			String archiveAppender = " union all SELECT organization_id, SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt, finaldate FROM st_rep_ola_agent WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+agentArchiveAppender+walletAppender+" group by finaldate";
			responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();
			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("select agent_org_id, IFNULL(SUM(mrp_with_amt),0.0) mrp_withd_amt, IFNULL(SUM(net_with_amt),0.00) net_withd_amt, DATE(transaction_date) transaction_date FROM ( SELECT parent_id agent_org_id, IFNULL(SUM(mrp_wid_amt),0.0) mrp_with_amt, IFNULL(SUM(net_wid_amt),0.00) net_with_amt, DATE(transaction_date) transaction_date FROM (SELECT rtm.retailer_org_id,withdrawl_amt mrp_wid_amt, agent_net_amt net_wid_amt, transaction_date FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL')main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id").append(agentAppender).append( " GROUP BY DATE(transaction_date) ").append(archiveAppender).append(" )tmp group by transaction_date");;
			logger.debug("fetchWithdrawDataSingleAgentDateWise - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("agent_org_id"));
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_withd_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_withd_amt"));
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
	public static OlaOrgReportResponseBean fetchWithdrawDirectPlayerDataSingleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String agentAppender = (requestBean.getOrgId() == 0)?"":" AND agent_org_id="+requestBean.getOrgId();
			String agentArchAppender = (requestBean.getOrgId() == 0)?"":" AND organization_id="+requestBean.getOrgId();
			String archiveAppender = " UNION ALL SELECT ifNULL(organization_id,0), SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt FROM st_rep_ola_agt_dir WHERE finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+agentArchAppender+walletAppender;
			withdrawQueryBuilder = new StringBuilder();			
			withdrawQueryBuilder.append("SELECT IFNULL(agent_org_id,0) agent_org_id, IFNULL(SUM(withdrawl_amt),0.0) mrp_with_amt, IFNULL(SUM(net_amt),0.0) net_with_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_withdrawl dw ON atm.transaction_id=dw.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("' AND atm.transaction_type='OLA_WITHDRAWL_PLR' ").append(agentAppender).append(archiveAppender);
			logger.debug("fetchWithdrawDirectPlayerDataSingleAgent - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			int orgId = 0;
			double mrpWithAmt = 0.0;
			double netAmt = 0.0;
			responseBean = new OlaOrgReportResponseBean();
			while (rs.next()) {				
				orgId = orgId + rs.getInt("agent_org_id");
				mrpWithAmt = mrpWithAmt + rs.getDouble("mrp_with_amt");
				netAmt = netAmt+ rs.getDouble("net_with_amt");
			}
			responseBean.setOrgId(orgId);
			responseBean.setMrpWithdrawalAmt(mrpWithAmt);
			responseBean.setNetWithdrawalAmt(netAmt);
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
	public static Map<String, OlaOrgReportResponseBean> fetchWithdrawDirectPlayerDataSingleAgentDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String agentAppender = (requestBean.getOrgId() == 0)?"":" AND agent_org_id="+requestBean.getOrgId();
			String agentArchAppender = (requestBean.getOrgId() == 0)?"":" AND organization_id="+requestBean.getOrgId();
			String archiveAppender = "  UNION ALL SELECT IFNULL(organization_id,0) agent_org_id, SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt, finaldate FROM st_rep_ola_agt_dir WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+agentArchAppender+walletAppender+" group by finaldate";
			responseBeanMap = new HashMap<String, OlaOrgReportResponseBean>();
			withdrawQueryBuilder = new StringBuilder();			
			withdrawQueryBuilder.append("SELECT IFNULL(agent_org_id,0) agent_org_id, IFNULL(sum(withdrawl_amt),0.0) mrp_with_amt, IFNULL(SUM(net_amt),0.0) net_with_amt, DATE(transaction_date) transaction_date FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_withdrawl dw ON atm.transaction_id=dw.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_WITHDRAWL_PLR' ").append(agentAppender).append(" GROUP BY DATE(transaction_date) ").append(archiveAppender);
			logger.debug("fetchWithdrawDirectPlayerDataSingleAgentDateWise - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());			
			while (rs.next()) {				
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setOrgId(rs.getInt("agent_org_id"));
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
	public static Map<Integer, OlaOrgReportResponseBean> fetchWithdrawDataMultipleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " UNION ALL SELECT organization_id, SUM(withdrawal_mrp) mrp_with_amt, SUM(withdrawal_net_amt) net_with_amt FROM st_rep_ola_agent WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+walletAppender+" group by organization_id";
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();
			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("SELECT agent_org_id, IFNULL(SUM(mrp_with_amt),0.0) mrp_withd_amt, IFNULL(SUM(net_with_amt),0.00) net_withd_amt FROM ( SELECT parent_id agent_org_id, IFNULL(SUM(mrp_wid_amt),0.0) mrp_with_amt, IFNULL(SUM(net_wid_amt),0.00) net_with_amt FROM (SELECT rtm.retailer_org_id, withdrawl_amt mrp_wid_amt, agent_net_amt net_wid_amt FROM st_lms_retailer_transaction_master rtm INNER JOIN st_ola_ret_withdrawl wd ON rtm.transaction_id=wd.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND rtm.transaction_type='OLA_WITHDRAWL' )main INNER JOIN st_lms_organization_master om ON om.organization_id=main.retailer_org_id GROUP BY parent_id ").append(archiveAppender).append(" )tmp group by agent_org_id");
			logger.debug("fetchWithdrawDataMultipleAgent - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_withd_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_withd_amt"));
				responseBeanMap.put(rs.getInt("agent_org_id"), responseBean);
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
	public static Map<Integer, OlaOrgReportResponseBean> fetchWithdrawDirectPlayerDataMultipleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> responseBeanMap = null;
		OlaOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder withdrawQueryBuilder = null;
		try {
			String walletAppender = (requestBean.getWalletId() == 0)?"":" AND wallet_id="+requestBean.getWalletId();
			String archiveAppender = " union all SELECT organization_id, (withdrawal_mrp) mrp_with_amt, (withdrawal_net_amt) net_with_amt FROM st_rep_ola_agt_dir WHERE  finaldate>='"+requestBean.getFromDate()+"' AND finaldate<='"+requestBean.getToDate()+"'"+walletAppender;
			responseBeanMap = new HashMap<Integer, OlaOrgReportResponseBean>();
			withdrawQueryBuilder = new StringBuilder();
			withdrawQueryBuilder.append("SELECT agent_org_id, IFNULL(SUM(withdrawl_amt),0.0) mrp_with_amt, IFNULL(SUM(net_amt),0.0) net_with_amt FROM st_lms_agent_transaction_master atm INNER JOIN st_ola_agt_direct_plr_withdrawl dw ON atm.transaction_id=dw.transaction_id WHERE transaction_date>='").append(requestBean.getFromDate()).append("' AND transaction_date<='").append(requestBean.getToDate()).append("'").append(walletAppender).append(" AND atm.transaction_type='OLA_WITHDRAWL_PLR' GROUP BY agent_org_id ").append(archiveAppender);
			logger.debug("fetchWithdrawDirectPlayerDataMultipleAgent - "+withdrawQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(withdrawQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new OlaOrgReportResponseBean();
				responseBean.setMrpWithdrawalAmt(rs.getDouble("mrp_with_amt"));
				responseBean.setNetWithdrawalAmt(rs.getDouble("net_with_amt"));
				responseBeanMap.put(rs.getInt("agent_org_id"), responseBean);
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