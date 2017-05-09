package com.skilrock.lms.web.sportsLottery.reportsMgmt.controller;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.daoImpl.SLERetailerReportDaoImpl;

public class SLERetailerReportsControllerImpl {

	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailer(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = SLERetailerReportDaoImpl
				.fetchSaleDataMultipleRetailer(requestBean, connection);

		cancelResponseMap = SLERetailerReportDaoImpl
				.fetchCancelDataMultipleRetailer(requestBean, connection);

		pwtResponseMap = SLERetailerReportDaoImpl.fetchPWTDataMultipleRetailer(
				requestBean, connection);
		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailerGameWise(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = SLERetailerReportDaoImpl
				.fetchSaleDataMultipleRetailerGameWise(requestBean, connection);

		cancelResponseMap = SLERetailerReportDaoImpl
				.fetchCancelDataMultipleRetailerGameWise(requestBean,
						connection);

		pwtResponseMap = SLERetailerReportDaoImpl
				.fetchPWTDataMultipleRetailerGameWise(requestBean, connection);
		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, SLEOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			SLEOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}
	
	public static Map<String, SLEOrgReportResponseBean> fetchSaleCancelPwtDateWiseSingleRetailerAllGame(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = SLERetailerReportDaoImpl
				.fetchSaleDataDateWiseSingleRetailerAllGame(requestBean, connection);

		cancelResponseMap = SLERetailerReportDaoImpl
				.fetchCancelDataDateWiseSingleRetailerAllGame(requestBean, connection);

		pwtResponseMap = SLERetailerReportDaoImpl.fetchPWTDataDateWiseSingleRetailerAllGame(requestBean, connection);
		
		for (Map.Entry<String, SLEOrgReportResponseBean> entry : cancelResponseMap.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(date, cancelResponseBean);
			}
		}

		for (Map.Entry<String, SLEOrgReportResponseBean> entry : pwtResponseMap.entrySet()) {
			String date = entry.getKey();
			SLEOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				SLEOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(date, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

}
