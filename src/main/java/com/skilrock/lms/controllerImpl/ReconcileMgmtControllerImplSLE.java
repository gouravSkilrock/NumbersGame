package com.skilrock.lms.controllerImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.controller.dataMgmtController.ReconcileMgmtController;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.playMgmt.daoImpl.SportsLotteryGamePlayDaoImpl;
import com.skilrock.lms.daoImpl.dataMgmtDaoImpl.ReconcileMgmtDaoImplSLE;
import com.skilrock.lms.rest.services.bean.TPReconciliationBean;
import com.skilrock.lms.rest.services.bean.TPRequestBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;

public class ReconcileMgmtControllerImplSLE implements ReconcileMgmtController {

	private ReconcileMgmtControllerImplSLE(){}
	
	public enum Single {
		INSTANCE;
		ReconcileMgmtControllerImplSLE instance = new ReconcileMgmtControllerImplSLE();

		public ReconcileMgmtControllerImplSLE getInstance() {
			if (instance == null)
				return new ReconcileMgmtControllerImplSLE();
			else
				return instance;
		}
	}
	
	@Override
	public void reconcileSLETransactions(TPRequestBean requestBean) throws SLEException {
		Map<String, List<TPReconciliationBean>> recMap = null;
		List<TPReconciliationBean> tpReconciliationBeans = null;
		TPReconciliationBean reconciliationBean = new TPReconciliationBean();
		TPReconciliationBean tempBean = null;
		StringBuilder txnIds = new StringBuilder();
		Connection con = null;
		List<TPTxRequestBean> tpTxRequestBeans = new ArrayList<TPTxRequestBean>();
		TPTxRequestBean tpTxRequestBean = null;
		List<UserInfoBean> userInfoBeans = new ArrayList<UserInfoBean>();
		
		Iterator<TPTxRequestBean> tpTxIterator = null;
		Iterator<UserInfoBean> userInfoBeanIterator = null;
		Map<Long,Long> txnTicketMap=null;
		
		long cancelTxnId = 0;
		
		ReconcileMgmtDaoImplSLE reconcileMgmtDaoImplSLE =  ReconcileMgmtDaoImplSLE.Single.INSTANCE.getInstance();

		try {
			recMap = new Gson().fromJson(requestBean.getRequestData().toString(), new TypeToken<Map<String, List<TPReconciliationBean>>>() {}.getType());
	
			Set<Entry<String, List<TPReconciliationBean>>> set = recMap.entrySet();
	
			for (Map.Entry<String, List<TPReconciliationBean>> entrySet : set) {
				if ("SALE".equals(entrySet.getKey())) {
					tpTxRequestBeans.clear();
					tpReconciliationBeans = entrySet.getValue();
					if(tpReconciliationBeans.isEmpty())
						continue;
					con = DBConnect.getConnection();
					reconcileMgmtDaoImplSLE.reconcileFailedSaleTxns(tpReconciliationBeans, con);
					reconcileMgmtDaoImplSLE.fetchfetchDoneCancelTxnsInfo(tpReconciliationBeans, con);
					
					txnTicketMap = new HashMap<Long, Long>();
					for (TPReconciliationBean tpReconciliationBean : tpReconciliationBeans) {
						txnIds.append(tpReconciliationBean.getEngineTxnId()).append(",");
						txnTicketMap.put(Long.parseLong(tpReconciliationBean.getEngineTxnId()), tpReconciliationBean.getTicktNo());
					}
					if(txnIds.length() > 0) {
						txnIds.deleteCharAt(txnIds.length() - 1);
						reconcileMgmtDaoImplSLE.fetchDoneSaleTxnsInfo(txnIds.toString(), tpTxRequestBeans, userInfoBeans,txnTicketMap, con);
					}
					
					tpTxIterator = tpTxRequestBeans.iterator();
					userInfoBeanIterator = userInfoBeans.iterator();
					while(tpTxIterator.hasNext() && userInfoBeanIterator.hasNext()) {
						con.setAutoCommit(false);
						tpTxRequestBean = tpTxIterator.next();
						cancelTxnId = SportsLotteryGamePlayDaoImpl.refundPurchaseTicket(tpTxRequestBean, userInfoBeanIterator.next(), con);
						con.commit();
						
						reconciliationBean.setEngineSaleTxnId(String.valueOf(tpTxRequestBean.getEngineTxId()));
						tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, reconciliationBean);
						tempBean.setStatus("FAILED");
						tempBean.setMerchantTxnId(String.valueOf(cancelTxnId));
					}
					DBConnect.closeCon(con);
				} else if ("REFUND".equals(entrySet.getKey())) {
					tpTxRequestBeans.clear();
					tpReconciliationBeans = entrySet.getValue();
					if(tpReconciliationBeans.isEmpty())
						continue;
					con = DBConnect.getConnection();
					reconcileMgmtDaoImplSLE.reconcileDoneRefundTxns(tpReconciliationBeans, con);
					reconcileMgmtDaoImplSLE.fetchMissingRefundTxnsInfo(tpReconciliationBeans, tpTxRequestBeans, userInfoBeans, con);
					tpTxIterator = tpTxRequestBeans.iterator();
					userInfoBeanIterator = userInfoBeans.iterator();
					while(tpTxIterator.hasNext() && userInfoBeanIterator.hasNext()) {
						con.setAutoCommit(false);
						tpTxRequestBean = tpTxIterator.next();
						cancelTxnId = SportsLotteryGamePlayDaoImpl.refundPurchaseTicket(tpTxRequestBean , userInfoBeanIterator.next(), con);
						con.commit();
						
						reconciliationBean.setEngineSaleTxnId(String.valueOf(tpTxRequestBean.getEngineSaleTxId()));
						tempBean = (TPReconciliationBean) CollectionUtils.find(tpReconciliationBeans, reconciliationBean);
						tempBean.setStatus("CANCELLED");
						tempBean.setMerchantTxnId(String.valueOf(cancelTxnId));
					}
					DBConnect.closeCon(con);
				} else if ("PWT".equals(entrySet.getKey())) {
					con = DBConnect.getConnection();
					tpReconciliationBeans = entrySet.getValue();
					if(tpReconciliationBeans.isEmpty())
						continue;
					reconcileMgmtDaoImplSLE.reconcileDonePwtTxns(tpReconciliationBeans, con);
					DBConnect.closeCon(con);
				}
			}
			requestBean.setRequestData(recMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}

}
