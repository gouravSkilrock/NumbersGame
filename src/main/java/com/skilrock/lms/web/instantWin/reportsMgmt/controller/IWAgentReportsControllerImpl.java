package com.skilrock.lms.web.instantWin.reportsMgmt.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantWin.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.daoImpl.IWAgentReportDaoImpl;

public class IWAgentReportsControllerImpl {

	public static Map<Integer, IWOrgReportResponseBean> fetchSaleCancelPWTMultipleAgent(IWOrgReportRequestBean requestBean, Connection connection) throws GenericException, LMSException {
		Map<Integer, IWOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, IWOrgReportResponseBean>();

		// List<Integer> iwGameIdList = ReportUtility.getActiveIWGameIdList(
		// requestBean.getToDate(), connection);
		// for (Integer gameId : iwGameIdList) {
		//
		// requestBean.setGameId(gameId);

		saleResponseMap = IWAgentReportDaoImpl.fetchSaleDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : saleResponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}
		cancelReponseMap = IWAgentReportDaoImpl.fetchCancelDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : cancelReponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = IWAgentReportDaoImpl.fetchPWTDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : pwtReponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		Map.Entry<Integer, GameMasterBean> entrySet = IWUtil.gameInfoMap.entrySet().iterator().next();
		requestBean.setGameId(entrySet.getKey());
		dirPlyPwtReponseMap = IWAgentReportDaoImpl.fetchDirPlyPWTDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : dirPlyPwtReponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<Integer, IWOrgReportResponseBean> fetchSaleCancelPWTMultipleAgentGameWise(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<Integer, IWOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, IWOrgReportResponseBean>();

		saleResponseMap = IWAgentReportDaoImpl
				.fetchSaleDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = IWAgentReportDaoImpl
				.fetchCancelDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = IWAgentReportDaoImpl.fetchPWTDataGameWiseMultipleAgent(
				requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = IWAgentReportDaoImpl
				.fetchDirPlyPWTDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<String, IWOrgReportResponseBean> fetchSalePWTDayWiseForBO(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, IWOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, IWOrgReportResponseBean>();

		saleResponseMap = IWAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = IWAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = IWAgentReportDaoImpl.fetchPWTDataDayWisePerGameForBO(
				requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = IWAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static IWOrgReportResponseBean fetchSaleCancelPWTSingleAgentAllGame(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		IWOrgReportResponseBean responseBean = new IWOrgReportResponseBean();

		IWAgentReportDaoImpl.fetchSaleDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		IWAgentReportDaoImpl.fetchCancelDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		IWAgentReportDaoImpl.fetchPWTDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		IWAgentReportDaoImpl.fetchIWDirectPlyPwtofAgent(requestBean,
				responseBean, connection);

		return responseBean;
	}

	public static Map<String, IWOrgReportResponseBean> fetchSalePWTDayWiseForAgent(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, IWOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, IWOrgReportResponseBean>();

		saleResponseMap = IWAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = IWAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = IWAgentReportDaoImpl
				.fetchPWTDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = IWAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForAgent(requestBean,
						connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			IWOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<String, IWOrgReportResponseBean> fetchSalePWTDayWiseAllGameSingleAgent(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, IWOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, IWOrgReportResponseBean>();

		saleResponseMap = IWAgentReportDaoImpl
				.fetchSaleDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(date, reportSaleResponseBean);
			}
		}

		cancelReponseMap = IWAgentReportDaoImpl
				.fetchCancelDataDayWiseAllGameSingleAgent(requestBean,
						connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(date, reportcancelResponseBean);
			}
		}

		pwtReponseMap = IWAgentReportDaoImpl
				.fetchPWTDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(date, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = IWAgentReportDaoImpl
				.fetchDirectPlyPWTDataDayWiseAllGameSingleAgent(requestBean,
						connection);

		for (Map.Entry<String, IWOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(date, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static IWOrgReportResponseBean getDirPWTofAgent(
			IWOrgReportRequestBean requestBean, Connection con)
			throws GenericException, LMSException {
		IWOrgReportResponseBean responseBean = new IWOrgReportResponseBean();
		IWAgentReportDaoImpl.fetchIWDirectPlyPwtofAgent(requestBean,
				responseBean, con);
		return responseBean;
	}
}
