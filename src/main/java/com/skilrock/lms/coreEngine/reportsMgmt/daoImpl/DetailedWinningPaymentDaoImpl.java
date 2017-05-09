package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.DetailedPaymentTransactionalBean;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.GoodCauseDataBean;

public class DetailedWinningPaymentDaoImpl {
	final static Log logger = LogFactory.getLog(DetailedWinningPaymentDaoImpl.class);

	private static DetailedWinningPaymentDaoImpl instance;

	private DetailedWinningPaymentDaoImpl() {
	}

	public static DetailedWinningPaymentDaoImpl getInstance() {
		if (instance == null) {
			synchronized (DetailedWinningPaymentDaoImpl.class) {
				if (instance == null) {
					instance = new DetailedWinningPaymentDaoImpl();
				}
			}
		}
		return instance;
	}

	public List<GoodCauseDataBean> fetchGoodCauseData(Timestamp startTime, Timestamp endTime, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<GoodCauseDataBean> reportList = new ArrayList<GoodCauseDataBean>();
		GoodCauseDataBean dataBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();
			String query = "SELECT game_id, game_name, transaction_type, SUM(sale_unapproved_amount) sale_unapproved_amount, SUM(winning_unapproved_amount) winning_unapproved_amount, SUM(sale_approved_amount) sale_approved_amount, SUM(winning_approved_amount) winning_approved_amount, SUM(sale_done_amount) sale_done_amount, SUM(winning_done_amount) winning_done_amount, end_date, service_code FROM (SELECT bt.game_id, game_name, transaction_type, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_unapproved_amount, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_unapproved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_approved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_approved_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_done_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_done_amount, end_date, service_code FROM st_lms_bo_tasks bt INNER JOIN st_dg_game_master gm ON bt.game_id=gm.game_id WHERE end_date>='"+startTime+"' AND end_date<='"+endTime+"' AND service_code='DG' UNION ALL SELECT bt.game_id, type_disp_name game_name, transaction_type, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_unapproved_amount, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_unapproved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_approved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_approved_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_done_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_done_amount, end_date, service_code FROM st_lms_bo_tasks bt INNER JOIN st_sle_game_type_master gm ON bt.game_id=gm.game_type_id WHERE end_date>='"+startTime+"' AND end_date<='"+endTime+"' AND service_code='SLE')aa GROUP BY service_code, game_name;";
			logger.info("fetchGoodCauseData - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				dataBean = new GoodCauseDataBean();
				dataBean.setGameId(rs.getInt("game_id"));
				dataBean.setGameName(rs.getString("game_name"));
				dataBean.setSaleUnapprovedAmount(rs.getDouble("sale_unapproved_amount"));
				dataBean.setWinningUnapprovedAmount(rs.getDouble("winning_unapproved_amount"));
				dataBean.setSaleApprovedAmount(rs.getDouble("sale_approved_amount"));
				dataBean.setWinningApprovedAmount(rs.getDouble("winning_approved_amount"));
				dataBean.setSaleDoneAmount(rs.getDouble("sale_done_amount"));
				dataBean.setWinningDoneAmount(rs.getDouble("winning_done_amount"));
				dataBean.setTransactionDate(dateFormat.format(rs.getTimestamp("end_date")));
				dataBean.setServiceCode(rs.getString("service_code"));
				reportList.add(dataBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return reportList;
	}

	public Set<Integer> fetchRetailers(int agentOrgId,Connection con) {
		Set<Integer>retailerSet=new HashSet<Integer>();		
		String query=null;
		ResultSet rs=null;
		Statement stmt=null;
		try{
			stmt=con.createStatement();
			query="select user_id, organization_id from st_lms_user_master where organization_id in (select organization_id from st_lms_organization_master where parent_id="+agentOrgId+");";
			rs=stmt.executeQuery(query);
			while(rs.next()){
				retailerSet.add(rs.getInt("user_id"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retailerSet;
	}
	public Map<Long,String> fetchRetailersOrgCode(Connection con) {
		Map<Long,String>retailerOrgCodeMap=new HashMap<Long,String>();		
		String query=null;
		ResultSet rs=null;
		Statement stmt=null;
		try{
			stmt=con.createStatement();
			query="select user_id,retOrgCode,org_code as agtOrgCode from (select user_id,org_code as retOrgCode,parent_id from st_lms_organization_master a inner join st_lms_user_master b on a.organization_id=b.organization_id where a.organization_type='RETAILER')c inner join st_lms_organization_master d on c.parent_id=d.organization_id;";
			rs=stmt.executeQuery(query);
			while(rs.next()){
				retailerOrgCodeMap.put(rs.getLong("user_id"), rs.getString("retOrgCode")+"-"+rs.getString("agtOrgCode"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retailerOrgCodeMap;
	}

	public List<DetailedPaymentTransactionalBean> getReportData(String serviceCode, String detailType, Set<Integer> retailerSet, int gameId, Timestamp startTime, Timestamp endTime, int retOrgId, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		List<DetailedPaymentTransactionalBean> reportList = new ArrayList<DetailedPaymentTransactionalBean>();
		String appender = "";
		String query = null;

		DetailedPaymentTransactionalBean dataBean = null;
		try {
			stmt = con.createStatement();
			if ("LMC".equalsIgnoreCase(detailType)) {
				String set = retailerSet.toString().replace("[", "").replace("]", "");
				appender = " and a.retailer_org_id in (" + set + ") ";
			} else if ("RETAILER".equalsIgnoreCase(detailType))
				appender = " and a.retailer_org_id =" + retOrgId + " ";

			if ("DG".equals(serviceCode))
				query = "select * from ((select sum(netAmt)netAmtSale from (select ifnull(sum(net_amt),0) netAmt from st_dg_ret_sale_"
						+ gameId
						+ " a inner join st_lms_retailer_transaction_master b on a.transaction_id =b.transaction_id where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startTime
						+ "' and transaction_date<='"
						+ endTime
						+ "' "
						+ appender
						+ "  union all select -ifnull(sum(net_amt),0)netAmt from st_dg_ret_sale_refund_"
						+ gameId
						+ " a inner join st_lms_retailer_transaction_master b on a.transaction_id =b.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startTime
						+ "' and transaction_date<='"
						+ endTime
						+ "' "
						+ appender
						+ " )ff)a,(select count(b.transaction_id)TxnId,ifnull(sum(pwt_amt+agt_claim_comm+retailer_claim_comm-govt_claim_comm),0) netAmtWin from st_dg_ret_pwt_"
						+ gameId
						+ " a inner join st_lms_retailer_transaction_master  b on a.transaction_id=b.transaction_id where b.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and b.transaction_date>='"
						+ startTime
						+ "' and b.transaction_date<='"
						+ endTime
						+ "' " + appender + " )b)";
			else if ("SLE".equals(serviceCode))
				query = "select * from ((select sum(netAmt)netAmtSale from (select ifnull(sum(retailer_net_amt),0) netAmt from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b on a.transaction_id =b.transaction_id where transaction_type in('SLE_SALE') and a.game_id = "
						+ gameId
						+ " and b.transaction_date>='"
						+ startTime
						+ "' and b.transaction_date<='"
						+ endTime
						+ "' "
						+ appender
						+ "union all select -ifnull(sum(retailer_net_amt),0)netAmt from st_sle_ret_sale_refund a inner join st_lms_retailer_transaction_master b on a.transaction_id =b.transaction_id where transaction_type in('SLE_REFUND_CANCEL') and a.game_id = "
						+ gameId
						+ " and b.transaction_date>='"
						+ startTime
						+ "' and b.transaction_date<='"
						+ endTime
						+ "' "
						+ appender
						+ ")ff)a,(select count(b.transaction_id)TxnId,ifnull(sum(pwt_amt+agt_claim_comm+retailer_claim_comm-govt_claim_comm),0) netAmtWin from st_sle_ret_pwt a inner join st_lms_retailer_transaction_master  b on b.transaction_id=b.transaction_id where b.transaction_type in('SLE_PWT') and a.game_id ="
						+ gameId
						+ " and b.transaction_date>='"
						+ startTime
						+ "' and b.transaction_date<='"
						+ endTime
						+ "' "
						+ appender + " )b)";
			logger.info("fetchDetailedWinningTransactionalData - " + query);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				dataBean = new DetailedPaymentTransactionalBean();
				dataBean.setClaimedPwtAmt(rs.getDouble("netAmtWin"));
				dataBean.setTotalSaleAmt(rs.getDouble("netAmtSale"));
				dataBean.setTotalTransactions(rs.getInt("TxnId"));

				reportList.add(dataBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return reportList;
	}

	public String fetchWinTransDate(String serviceCode, String tktNbr, int gameId, Connection con) {
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		int retTxnId = 0;
		int boTxnId = 0;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			stmt = con.createStatement();
			if("DG".equals(serviceCode))
				rs = stmt.executeQuery("select ifnull(retailer_transaction_id, 0) retailer_transaction_id, ifnull(bo_transaction_id, 0) bo_transaction_id from st_dg_pwt_inv_" + gameId + " where ticket_nbr = " + tktNbr.substring(0, 17));
			else if("SLE".equals(serviceCode))
				rs = stmt.executeQuery("select ifnull(retailer_transaction_id, 0) retailer_transaction_id, ifnull(bo_transaction_id, 0) bo_transaction_id from st_sle_pwt_inv  where ticket_nbr = " + tktNbr.substring(0, 17));

			if (rs.next()) {
				retTxnId = rs.getInt("retailer_transaction_id");
				boTxnId = rs.getInt("bo_transaction_id");
			} else
				return "NA";
			if (retTxnId != 0)
				rs = stmt.executeQuery("select transaction_date from st_lms_retailer_transaction_master where transaction_id=" + retTxnId + ";");
			else if (boTxnId != 0)
				rs = stmt.executeQuery("select transaction_date from st_lms_bo_transaction_master where transaction_id=" + boTxnId + ";");
			if (rs.next()) {
				return dateFormat.format(rs.getTimestamp("transaction_date"));
			} else
				return "NA";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}