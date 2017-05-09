package com.skilrock.lms.web.sportsLottery.reportsMgmt.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.daoImpl.SLEAgentReportDaoImpl;

public class SLEAgentReportsControllerImpl {

//	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleCancelPWTMultipleAgent(
//			SLEOrgReportRequestBean requestBean, Connection connection)
//			throws GenericException, LMSException {
//
//		Map<Integer, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;
//
//		responseMap = new HashMap<Integer, SLEOrgReportResponseBean>();
//
//		List<Integer> sleGameIdList = ReportUtility.getActiveSLEGameIdList(
//				requestBean.getToDate(), connection);
//		for (Integer gameId : sleGameIdList) {
//
//			requestBean.setGameId(gameId);
//
//			saleResponseMap = SLEAgentReportDaoImpl
//					.fetchSaleDataGameWiseMultipleAgent(requestBean, connection);
//
//			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : saleResponseMap
//					.entrySet()) {
//				int orgId = entry.getKey();
//				SLEOrgReportResponseBean reportSaleResponseBean = entry
//						.getValue();
//				if (responseMap.containsKey(orgId)) {
//					responseMap.get(orgId).setSaleAmt(
//							reportSaleResponseBean.getSaleAmt());
//				} else {
//					responseMap.put(orgId, reportSaleResponseBean);
//				}
//			}
//
//			cancelReponseMap = SLEAgentReportDaoImpl
//					.fetchCancelDataGameWiseMultipleAgent(requestBean,
//							connection);
//
//			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : cancelReponseMap
//					.entrySet()) {
//				int orgId = entry.getKey();
//				SLEOrgReportResponseBean reportcancelResponseBean = entry
//						.getValue();
//				if (responseMap.containsKey(orgId)) {
//					responseMap.get(orgId).setCancelAmt(
//							reportcancelResponseBean.getCancelAmt());
//				} else {
//					responseMap.put(orgId, reportcancelResponseBean);
//				}
//			}
//
//			pwtReponseMap = SLEAgentReportDaoImpl
//					.fetchPWTDataGameWiseMultipleAgent(requestBean, connection);
//
//			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : pwtReponseMap
//					.entrySet()) {
//				int orgId = entry.getKey();
//				SLEOrgReportResponseBean reportPwtResponseBean = entry
//						.getValue();
//				if (responseMap.containsKey(orgId)) {
//					responseMap.get(orgId).setPwtAmt(
//							reportPwtResponseBean.getPwtAmt());
//				} else {
//					responseMap.put(orgId, reportPwtResponseBean);
//				}
//			}
//
//			dirPlyPwtReponseMap = SLEAgentReportDaoImpl
//					.fetchDirPlyPWTDataGameWiseMultipleAgent(requestBean,
//							connection);
//
//			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
//					.entrySet()) {
//				int orgId = entry.getKey();
//				SLEOrgReportResponseBean reportDirPwtResponseBean = entry
//						.getValue();
//				if (responseMap.containsKey(orgId)) {
//					responseMap.get(orgId).setPwtDirAmt(
//							reportDirPwtResponseBean.getPwtDirAmt());
//				} else {
//					responseMap.put(orgId, reportDirPwtResponseBean);
//				}
//			}
//		}
//		return responseMap;
//	}
	
	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleCancelPWTMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<Integer, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, SLEOrgReportResponseBean>();

//		List<Integer> sleGameIdList = ReportUtility.getActiveSLEGameIdList(
//				requestBean.getToDate(), connection);
//		for (Integer gameId : sleGameIdList) {
//
//			requestBean.setGameId(gameId);

			saleResponseMap = SLEAgentReportDaoImpl
					.fetchSaleDataAllGameMultipleAgent(requestBean, connection);

			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : saleResponseMap
					.entrySet()) {
				int orgId = entry.getKey();
				SLEOrgReportResponseBean reportSaleResponseBean = entry
						.getValue();
				if (responseMap.containsKey(orgId)) {
					responseMap.get(orgId).setSaleAmt(
							reportSaleResponseBean.getSaleAmt());
				} else {
					responseMap.put(orgId, reportSaleResponseBean);
				}
			}

			cancelReponseMap = SLEAgentReportDaoImpl
					.fetchCancelDataAllGameMultipleAgent(requestBean, connection);

			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : cancelReponseMap
					.entrySet()) {
				int orgId = entry.getKey();
				SLEOrgReportResponseBean reportcancelResponseBean = entry
						.getValue();
				if (responseMap.containsKey(orgId)) {
					responseMap.get(orgId).setCancelAmt(
							reportcancelResponseBean.getCancelAmt());
				} else {
					responseMap.put(orgId, reportcancelResponseBean);
				}
			}

			pwtReponseMap = SLEAgentReportDaoImpl
					.fetchPWTDataAllGameMultipleAgent(requestBean, connection);

			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : pwtReponseMap
					.entrySet()) {
				int orgId = entry.getKey();
				SLEOrgReportResponseBean reportPwtResponseBean = entry
						.getValue();
				if (responseMap.containsKey(orgId)) {
					responseMap.get(orgId).setPwtAmt(
							reportPwtResponseBean.getPwtAmt());
				} else {
					responseMap.put(orgId, reportPwtResponseBean);
				}
			}

			dirPlyPwtReponseMap = SLEAgentReportDaoImpl
					.fetchDirPlyPWTDataGameWiseMultipleAgent(requestBean,
							connection);

			for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
					.entrySet()) {
				int orgId = entry.getKey();
				SLEOrgReportResponseBean reportDirPwtResponseBean = entry
						.getValue();
				if (responseMap.containsKey(orgId)) {
					responseMap.get(orgId).setPwtDirAmt(
							reportDirPwtResponseBean.getPwtDirAmt());
				} else {
					responseMap.put(orgId, reportDirPwtResponseBean);
				}
			}
//		}
		return responseMap;
	}

	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleCancelPWTMultipleAgentGameWise(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<Integer, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<Integer, SLEOrgReportResponseBean>();

		saleResponseMap = SLEAgentReportDaoImpl
				.fetchSaleDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = SLEAgentReportDaoImpl
				.fetchCancelDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean reportcancelResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = SLEAgentReportDaoImpl
				.fetchPWTDataGameWiseMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = SLEAgentReportDaoImpl
				.fetchDirPlyPWTDataAllGameMultipleAgent(requestBean, connection);

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean reportDirPwtResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static Map<String, SLEOrgReportResponseBean> fetchSalePWTDayWiseForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, SLEOrgReportResponseBean>();

		saleResponseMap = SLEAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = SLEAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportcancelResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = SLEAgentReportDaoImpl.fetchPWTDataDayWisePerGameForBO(
				requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = SLEAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForBO(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportDirPwtResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static SLEOrgReportResponseBean fetchSaleCancelPWTSingleAgentAllGame(SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		SLEOrgReportResponseBean responseBean = new SLEOrgReportResponseBean();

		SLEAgentReportDaoImpl.fetchSaleDataAllGameSingleAgent(requestBean, responseBean, connection);

		SLEAgentReportDaoImpl.fetchCancelDataAllGameSingleAgent(requestBean, responseBean, connection);
		
		SLEAgentReportDaoImpl.fetchPWTDataAllGameSingleAgent(requestBean, responseBean, connection);
		
		SLEAgentReportDaoImpl.fetchSLEDirectPlyPwtofAgent(requestBean, responseBean, connection);

		return responseBean;
	}
	
	public static Map<String, SLEOrgReportResponseBean> fetchSalePWTDayWiseForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, SLEOrgReportResponseBean>();

		saleResponseMap = SLEAgentReportDaoImpl
				.fetchSaleDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(orgId, reportSaleResponseBean);
			}
		}

		cancelReponseMap = SLEAgentReportDaoImpl
				.fetchCancelDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportcancelResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(orgId, reportcancelResponseBean);
			}
		}

		pwtReponseMap = SLEAgentReportDaoImpl
				.fetchPWTDataDayWisePerGameForAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(orgId, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = SLEAgentReportDaoImpl
				.fetchDirPlyPWTDataDayWisePerGameForAgent(requestBean,
						connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String orgId = entry.getKey();
			SLEOrgReportResponseBean reportDirPwtResponseBean = entry
					.getValue();
			if (responseMap.containsKey(orgId)) {
				responseMap.get(orgId).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(orgId, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}
	
	public static Map<String, SLEOrgReportResponseBean> fetchSalePWTDayWiseAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException, LMSException {

		Map<String, SLEOrgReportResponseBean> responseMap = null, saleResponseMap = null, cancelReponseMap = null, pwtReponseMap = null, dirPlyPwtReponseMap = null;

		responseMap = new HashMap<String, SLEOrgReportResponseBean>();

		saleResponseMap = SLEAgentReportDaoImpl
				.fetchSaleDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : saleResponseMap
				.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean reportSaleResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setSaleAmt(
						reportSaleResponseBean.getSaleAmt());
			} else {
				responseMap.put(date, reportSaleResponseBean);
			}
		}

		cancelReponseMap = SLEAgentReportDaoImpl
				.fetchCancelDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : cancelReponseMap
				.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean reportcancelResponseBean = entry
					.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setCancelAmt(
						reportcancelResponseBean.getCancelAmt());
			} else {
				responseMap.put(date, reportcancelResponseBean);
			}
		}

		pwtReponseMap = SLEAgentReportDaoImpl
				.fetchPWTDataDayWiseAllGameSingleAgent(requestBean, connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : pwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean reportPwtResponseBean = entry.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtAmt(
						reportPwtResponseBean.getPwtAmt());
			} else {
				responseMap.put(date, reportPwtResponseBean);
			}
		}

		dirPlyPwtReponseMap = SLEAgentReportDaoImpl
				.fetchDirectPlyPWTDataDayWiseAllGameSingleAgent(requestBean,connection);

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : dirPlyPwtReponseMap
				.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean reportDirPwtResponseBean = entry
					.getValue();
			if (responseMap.containsKey(date)) {
				responseMap.get(date).setPwtDirAmt(
						reportDirPwtResponseBean.getPwtDirAmt());
			} else {
				responseMap.put(date, reportDirPwtResponseBean);
			}
		}
		return responseMap;
	}

	public static SLEOrgReportResponseBean getDirPWTofAgent(
			SLEOrgReportRequestBean requestBean, Connection con)
			throws GenericException, LMSException {
		SLEOrgReportResponseBean responseBean = new SLEOrgReportResponseBean();
		SLEAgentReportDaoImpl.fetchSLEDirectPlyPwtofAgent(requestBean, responseBean, con);
		return responseBean;
	}
}
