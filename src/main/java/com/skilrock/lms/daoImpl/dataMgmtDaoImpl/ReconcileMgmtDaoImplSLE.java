package com.skilrock.lms.daoImpl.dataMgmtDaoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TPReconciliationBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;

public class ReconcileMgmtDaoImplSLE {
	private ReconcileMgmtDaoImplSLE() {}

	public enum Single {
		INSTANCE;
		ReconcileMgmtDaoImplSLE instance = new ReconcileMgmtDaoImplSLE();

		public ReconcileMgmtDaoImplSLE getInstance() {
			if (instance == null)
				return new ReconcileMgmtDaoImplSLE();
			else
				return instance;
		}
	}
	
	public void reconcileFailedSaleTxns(List<TPReconciliationBean> tpReconciliationBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPReconciliationBean tpReconciliationBean = new TPReconciliationBean();
		TPReconciliationBean tempBean = null;
		StringBuilder txnIds = new StringBuilder();
		List<String> failedTxnList = new java.util.ArrayList<String>();
		for (TPReconciliationBean tempReconciliationBean : tpReconciliationBeans) {
			txnIds.append(tempReconciliationBean.getEngineTxnId()).append(",");
			failedTxnList.add(tempReconciliationBean.getEngineTxnId());
		}
		if(txnIds.length() > 0)
			txnIds.deleteCharAt(txnIds.length() - 1);
		else 
			return;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT engine_tx_id FROM st_sle_ret_sale WHERE engine_tx_id IN(" + txnIds.toString() + ");");
			while (rs.next()) {
				failedTxnList.remove(rs.getString("engine_tx_id"));
			}

			for(String failedTxnIds : failedTxnList) {
				tpReconciliationBean.setEngineSaleTxnId(failedTxnIds);
				tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, tpReconciliationBean);
				tempBean.setStatus("FAILED");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void fetchfetchDoneCancelTxnsInfo(List<TPReconciliationBean> tpReconciliationBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPReconciliationBean tpReconciliationBean = new TPReconciliationBean();
		TPReconciliationBean tempBean = null;
		StringBuilder txnIds = new StringBuilder();
		for (TPReconciliationBean tempReconciliationBean : tpReconciliationBeans) {
			txnIds.append(tempReconciliationBean.getEngineTxnId()).append(",");
		}
		if(txnIds.length() > 0)
			txnIds.deleteCharAt(txnIds.length() - 1);
		else
			return;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT refund.transaction_id rTxnId, sale.transaction_id sTxnId, sale.engine_tx_id FROM st_sle_ret_sale sale INNER JOIN st_sle_ret_sale_refund refund ON sale.transaction_id = refund.sale_ref_transaction_id WHERE sale.engine_tx_id IN(" + txnIds.toString() + ");");
			while (rs.next()) {
				tpReconciliationBean.setEngineSaleTxnId(rs.getString("engine_tx_id"));
				tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, tpReconciliationBean);
				tempBean.setMerchantTxnId(rs.getString("rTxnId"));
				tempBean.setStatus("FAILED");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void fetchDoneSaleTxnsInfo(String txnIds, List<TPTxRequestBean> tpTxRequestBeans, List<UserInfoBean> userInfoBeans,Map<Long,Long> txnTicketMap, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPTxRequestBean tpTxRequestBean = null;
		UserInfoBean userInfoBean = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select tm.service_code, tm.interface, tm.user_type, txnTable.engine_tx_id, txnTable.game_id, txnTable.game_type_id, txnTable.ticket_nbr, retailer.user_name, retailer.organization_id ret_org_id, retailer.user_id, agent.organization_id agent_org_id from st_lms_transaction_master tm inner join st_sle_ret_sale txnTable on tm.transaction_id = txnTable.transaction_id inner join st_lms_user_master retailer on retailer.organization_id = txnTable.retailer_org_id inner join st_lms_user_master agent on retailer.parent_user_id = agent.user_id where txnTable.engine_tx_id in (" + txnIds.toString() + ") And is_cancel = 'N';");
			while(rs.next()) {
				tpTxRequestBean = new TPTxRequestBean();
				tpTxRequestBean.setServiceCode(rs.getString("service_code"));
				tpTxRequestBean.setInterfaceType(rs.getString("interface"));
				tpTxRequestBean.setGameId(rs.getInt("game_id"));
				tpTxRequestBean.setEngineTxId(rs.getLong("engine_tx_id"));
				tpTxRequestBean.setEngineSaleTxId(rs.getLong("engine_tx_id"));
				tpTxRequestBean.setGameTypeId(rs.getInt("game_type_id"));
				tpTxRequestBean.setTicketNumber(txnTicketMap.get(tpTxRequestBean.getEngineTxId()) + "0");
				tpTxRequestBeans.add(tpTxRequestBean);
				
				userInfoBean = new UserInfoBean();
				userInfoBean.setUserType(rs.getString("user_type"));
				userInfoBean.setUserId(rs.getInt("user_id"));
				userInfoBean.setUserOrgId(rs.getInt("ret_org_id"));
				userInfoBean.setParentOrgId(rs.getInt("agent_org_id"));
				userInfoBeans.add(userInfoBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	
	public void fetchDoneSaleTxnsInfoForRefundTxn(Map<String, String> saleRefundTxnMap,Map<Long,Long> txnTicketMap,String txnIds, List<TPTxRequestBean> tpTxRequestBeans, List<UserInfoBean> userInfoBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPTxRequestBean tpTxRequestBean = null;
		UserInfoBean userInfoBean = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select tm.service_code, tm.interface, tm.user_type, txnTable.engine_tx_id, txnTable.game_id, txnTable.game_type_id, txnTable.ticket_nbr, retailer.user_name, retailer.organization_id ret_org_id, retailer.user_id, agent.organization_id agent_org_id from st_lms_transaction_master tm inner join st_sle_ret_sale txnTable on tm.transaction_id = txnTable.transaction_id inner join st_lms_user_master retailer on retailer.organization_id = txnTable.retailer_org_id inner join st_lms_user_master agent on retailer.parent_user_id = agent.user_id where txnTable.engine_tx_id in (" + txnIds.toString() + ") And is_cancel = 'N';");
			while(rs.next()) {
				tpTxRequestBean = new TPTxRequestBean();
				tpTxRequestBean.setServiceCode(rs.getString("service_code"));
				tpTxRequestBean.setInterfaceType(rs.getString("interface"));
				tpTxRequestBean.setGameId(rs.getInt("game_id"));
				tpTxRequestBean.setEngineTxId(Long.parseLong(saleRefundTxnMap.get(rs.getString("engine_tx_id"))));
				tpTxRequestBean.setEngineSaleTxId(rs.getLong("engine_tx_id"));
				tpTxRequestBean.setGameTypeId(rs.getInt("game_type_id"));
				tpTxRequestBean.setTicketNumber(txnTicketMap.get(tpTxRequestBean.getEngineSaleTxId())+ "0");
				tpTxRequestBeans.add(tpTxRequestBean);
				
				userInfoBean = new UserInfoBean();
				userInfoBean.setUserType(rs.getString("user_type"));
				userInfoBean.setUserId(rs.getInt("user_id"));
				userInfoBean.setUserOrgId(rs.getInt("ret_org_id"));
				userInfoBean.setParentOrgId(rs.getInt("agent_org_id"));
				userInfoBeans.add(userInfoBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void fetchMissingRefundTxnsInfo(List<TPReconciliationBean> tpReconciliationBeans, List<TPTxRequestBean> tpTxRequestBeans, List<UserInfoBean> userInfoBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder txnIds = new StringBuilder();
		List<String> failedTxnList = new ArrayList<String>();
		Map<Long,Long>txnTicketMap=new HashMap<Long, Long>();
		Map<String, String> salerefundTxnMap=new java.util.HashMap<String, String>();
		for (TPReconciliationBean tempReconciliationBean : tpReconciliationBeans) {
			salerefundTxnMap.put(tempReconciliationBean.getEngineSaleTxnId(), tempReconciliationBean.getEngineTxnId());
			txnIds.append(tempReconciliationBean.getEngineSaleTxnId()).append(",");
			failedTxnList.add(tempReconciliationBean.getEngineSaleTxnId());
			txnTicketMap.put(Long.parseLong(tempReconciliationBean.getEngineSaleTxnId()),tempReconciliationBean.getTicktNo());
		}
		if(txnIds.length() > 0)
			txnIds.deleteCharAt(txnIds.length() - 1);
		else
			return;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT refund.transaction_id rTxnId, sale.transaction_id sTxnId, sale.engine_tx_id FROM st_sle_ret_sale sale INNER JOIN st_sle_ret_sale_refund refund ON sale.transaction_id = refund.sale_ref_transaction_id WHERE sale.engine_tx_id IN (" + txnIds.toString() + ");");
			while(rs.next()) {
				failedTxnList.remove(rs.getString("engine_tx_id"));
			}
			
			txnIds.setLength(0);
			for(String txn : failedTxnList) {
				txnIds.append(txn).append(",");
			}
			if(txnIds.length() > 0) {
				txnIds.deleteCharAt(txnIds.length() - 1);
				fetchDoneSaleTxnsInfoForRefundTxn(salerefundTxnMap,txnTicketMap,txnIds.toString(), tpTxRequestBeans, userInfoBeans, con);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void reconcileDoneRefundTxns(List<TPReconciliationBean> tpReconciliationBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPReconciliationBean tpReconciliationBean = new TPReconciliationBean();
		TPReconciliationBean tempBean = null;
		StringBuilder txnIds = new StringBuilder();
		for (TPReconciliationBean tempReconciliationBean : tpReconciliationBeans) {
			txnIds.append(tempReconciliationBean.getEngineSaleTxnId()).append(",");
		}
		if(txnIds.length() > 0)
			txnIds.deleteCharAt(txnIds.length() - 1);
		else 
			return;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT refund.transaction_id rTxnId, sale.transaction_id sTxnId, sale.engine_tx_id FROM st_sle_ret_sale sale INNER JOIN st_sle_ret_sale_refund refund ON sale.transaction_id = refund.sale_ref_transaction_id WHERE sale.engine_tx_id IN (" + txnIds.toString() + ");");
			while (rs.next()) {
				tpReconciliationBean.setEngineSaleTxnId(rs.getString("engine_tx_id"));
				tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, tpReconciliationBean);
				tempBean.setMerchantTxnId(rs.getString("rTxnId"));
				tempBean.setStatus("CANCELLED");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void reconcileDonePwtTxns(List<TPReconciliationBean> tpReconciliationBeans, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		TPReconciliationBean tpReconciliationBean = new TPReconciliationBean();
		TPReconciliationBean tempBean = null;
		StringBuilder txnIds = new StringBuilder();
		for (TPReconciliationBean tempReconciliationBean : tpReconciliationBeans) {
			txnIds.append(tempReconciliationBean.getEngineTxnId()).append(",");
		}
		if(txnIds.length() > 0)
			txnIds.deleteCharAt(txnIds.length() - 1);
		else 
			return;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select engine_transaction_id,claim_at,bo_transaction_id,agent_transaction_id,retailer_transaction_id  FROM st_sle_pwt_inv WHERE engine_transaction_id in ("+ txnIds.toString() +")");
			while (rs.next()) {
				tpReconciliationBean.setEngineSaleTxnId(rs.getString("engine_transaction_id"));
				tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, tpReconciliationBean);
				if("BO".equalsIgnoreCase(rs.getString("claim_at"))){
					tempBean.setMerchantTxnId(rs.getString("bo_transaction_id"));
				}else if ("AGENT".equalsIgnoreCase(rs.getString("claim_at"))){
					tempBean.setMerchantTxnId(rs.getString("agent_transaction_id"));
				}else if ("RETAILER".equalsIgnoreCase(rs.getString("claim_at"))) {
					tempBean.setMerchantTxnId(rs.getString("retailer_transaction_id"));
				}
				tempBean.setStatus("DONE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
}
