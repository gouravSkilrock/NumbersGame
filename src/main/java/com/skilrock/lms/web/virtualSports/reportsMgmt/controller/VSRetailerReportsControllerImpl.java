package com.skilrock.lms.web.virtualSports.reportsMgmt.controller;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.daoImpl.VSRetailerReportDaoImpl;

public class VSRetailerReportsControllerImpl {

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleCancelPwtSingleRetailer(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {

		Map<Integer, VSOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = VSRetailerReportDaoImpl.fetchSaleDataSingleRetailer(requestBean, connection);
		cancelResponseMap = VSRetailerReportDaoImpl.fetchCancelDataSingleRetailer(requestBean, connection);
		pwtResponseMap = VSRetailerReportDaoImpl.fetchPWTDataSingleRetailer(requestBean, connection);

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : cancelResponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : pwtResponseMap.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailer(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = VSRetailerReportDaoImpl
				.fetchSaleDataMultipleRetailer(requestBean, connection);

		cancelResponseMap = VSRetailerReportDaoImpl
				.fetchCancelDataMultipleRetailer(requestBean, connection);

		pwtResponseMap = VSRetailerReportDaoImpl.fetchPWTDataMultipleRetailer(
				requestBean, connection);
		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailerGameWise(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = VSRetailerReportDaoImpl
				.fetchSaleDataMultipleRetailerGameWise(requestBean, connection);

		cancelResponseMap = VSRetailerReportDaoImpl
				.fetchCancelDataMultipleRetailerGameWise(requestBean,
						connection);

		pwtResponseMap = VSRetailerReportDaoImpl
				.fetchPWTDataMultipleRetailerGameWise(requestBean, connection);
		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, VSOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			VSOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}
	
	public static Map<String, VSOrgReportResponseBean> fetchSaleCancelPwtDateWiseSingleRetailerAllGame(
			VSOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, VSOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = VSRetailerReportDaoImpl
				.fetchSaleDataDateWiseSingleRetailerAllGame(requestBean, connection);

		cancelResponseMap = VSRetailerReportDaoImpl
				.fetchCancelDataDateWiseSingleRetailerAllGame(requestBean, connection);

		pwtResponseMap = VSRetailerReportDaoImpl.fetchPWTDataDateWiseSingleRetailerAllGame(requestBean, connection);
		
		for (Map.Entry<String, VSOrgReportResponseBean> entry : cancelResponseMap.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(date, cancelResponseBean);
			}
		}

		for (Map.Entry<String, VSOrgReportResponseBean> entry : pwtResponseMap.entrySet()) {
			String date = entry.getKey();
			VSOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				VSOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(date, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

}
