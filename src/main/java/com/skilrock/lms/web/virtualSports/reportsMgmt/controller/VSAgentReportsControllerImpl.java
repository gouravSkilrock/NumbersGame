package com.skilrock.lms.web.virtualSports.reportsMgmt.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.virtualSport.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSUtil;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.daoImpl.VSAgentReportDaoImpl;

public class VSAgentReportsControllerImpl {

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleCancelPWTMultipleAgent(VSOrgReportRequestBean requestBean, Connection connection) throws GenericException, LMSException {
		Map<Integer, VSOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, VSOrgReportResponseBean>();

		// List<Integer> vsGameIdList = ReportUtility.getActiveVSGameIdList(
		// requestBean.getToDate(), connection);
		// for (Integer gameId : vsGameIdList) {
		//
		// requestBean.setGameId(gameId);

		saleResponseMap = VSAgentReportDaoImpl.fetchSaleDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : saleResponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}
		cancelReponseMap = VSAgentReportDaoImpl.fetchCancelDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : cancelReponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = VSAgentReportDaoImpl.fetchPWTDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : pwtReponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		Map.Entry<Integer, GameMasterBean> entrySet = VSUtil.gameInfoMap.entrySet().iterator().next();
		requestBean.setGameId(entrySet.getKey());
		dirPlyPwtReponseMap = VSAgentReportDaoImpl.fetchDirPlyPWTDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : dirPlyPwtReponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleCancelPWTMultipleAgentGameWise(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<Integer, VSOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, VSOrgReportResponseBean>();

		saleResponseMap = VSAgentReportDaoImpl
				.fetchSaleDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = VSAgentReportDaoImpl
				.fetchCancelDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = VSAgentReportDaoImpl.fetchPWTDataGameWiseMultipleAgent(
				requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = VSAgentReportDaoImpl
				.fetchDirPlyPWTDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<String, VSOrgReportResponseBean> fetchSalePWTDayWiseForBO(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, VSOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, VSOrgReportResponseBean>();

		saleResponseMap = VSAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = VSAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = VSAgentReportDaoImpl.fetchPWTDataDayWisePerGameForBO(
				requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = VSAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static VSOrgReportResponseBean fetchSaleCancelPWTSingleAgentAllGame(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		VSOrgReportResponseBean responseBean = new VSOrgReportResponseBean();

		VSAgentReportDaoImpl.fetchSaleDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		VSAgentReportDaoImpl.fetchCancelDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		VSAgentReportDaoImpl.fetchPWTDataAllGameSingleAgent(requestBean,
				responseBean, connection);

		VSAgentReportDaoImpl.fetchVSDirectPlyPwtofAgent(requestBean,
				responseBean, connection);

		return responseBean;
	}

	public static Map<String, VSOrgReportResponseBean> fetchSalePWTDayWiseForAgent(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, VSOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, VSOrgReportResponseBean>();

		saleResponseMap = VSAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = VSAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = VSAgentReportDaoImpl
				.fetchPWTDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = VSAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForAgent(requestBean,
						connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			VSOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<String, VSOrgReportResponseBean> fetchSalePWTDayWiseAllGameSingleAgent(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, VSOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, VSOrgReportResponseBean>();

		saleResponseMap = VSAgentReportDaoImpl
				.fetchSaleDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(date, reportSaleResponseBean);
			}
		}

		cancelReponseMap = VSAgentReportDaoImpl
				.fetchCancelDataDayWiseAllGameSingleAgent(requestBean,
						connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean reportcancelResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(date, reportcancelResponseBean);
			}
		}

		pwtReponseMap = VSAgentReportDaoImpl
				.fetchPWTDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(date, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = VSAgentReportDaoImpl
				.fetchDirectPlyPWTDataDayWiseAllGameSingleAgent(requestBean,
						connection);

		for (Map.Entry<String, VSOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean reportDirPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(date, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static VSOrgReportResponseBean getDirPWTofAgent(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException, LMSException {
		VSOrgReportResponseBean responseBean = new VSOrgReportResponseBean();
		VSAgentReportDaoImpl.fetchVSDirectPlyPwtofAgent(requestBean,
				responseBean, con);
		return responseBean;
	}
}
