package com.skilrock.lms.web.instantWin.reportsMgmt.controller;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.daoImpl.IWRetailerReportDaoImpl;

public class IWRetailerReportsControllerImpl {

	public static Map<Integer, IWOrgReportResponseBean> fetchSaleCancelPwtSingleRetailer(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {

		Map<Integer, IWOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = IWRetailerReportDaoImpl.fetchSaleDataSingleRetailer(requestBean, connection);
		cancelResponseMap = IWRetailerReportDaoImpl.fetchCancelDataSingleRetailer(requestBean, connection);
		pwtResponseMap = IWRetailerReportDaoImpl.fetchPWTDataSingleRetailer(requestBean, connection);

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : cancelResponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : pwtResponseMap.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

	public static Map<Integer, IWOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailer(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, IWOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = IWRetailerReportDaoImpl
				.fetchSaleDataMultipleRetailer(requestBean, connection);

		cancelResponseMap = IWRetailerReportDaoImpl
				.fetchCancelDataMultipleRetailer(requestBean, connection);

		pwtResponseMap = IWRetailerReportDaoImpl.fetchPWTDataMultipleRetailer(
				requestBean, connection);
		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

	public static Map<Integer, IWOrgReportResponseBean> fetchSaleCancelPwtMultipleRetailerGameWise(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, IWOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = IWRetailerReportDaoImpl
				.fetchSaleDataMultipleRetailerGameWise(requestBean, connection);

		cancelResponseMap = IWRetailerReportDaoImpl
				.fetchCancelDataMultipleRetailerGameWise(requestBean,
						connection);

		pwtResponseMap = IWRetailerReportDaoImpl
				.fetchPWTDataMultipleRetailerGameWise(requestBean, connection);
		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : cancelResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(orgId, cancelResponseBean);
			}
		}

		for (Map.Entry<Integer, IWOrgReportResponseBean> entry : pwtResponseMap
				.entrySet()) {
			int orgId = entry.getKey();
			IWOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(orgId)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap
						.get(orgId);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(orgId, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}
	
	public static Map<String, IWOrgReportResponseBean> fetchSaleCancelPwtDateWiseSingleRetailerAllGame(
			IWOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, IWOrgReportResponseBean> saleResponseMap, cancelResponseMap, pwtResponseMap;

		saleResponseMap = IWRetailerReportDaoImpl
				.fetchSaleDataDateWiseSingleRetailerAllGame(requestBean, connection);

		cancelResponseMap = IWRetailerReportDaoImpl
				.fetchCancelDataDateWiseSingleRetailerAllGame(requestBean, connection);

		pwtResponseMap = IWRetailerReportDaoImpl.fetchPWTDataDateWiseSingleRetailerAllGame(requestBean, connection);
		
		for (Map.Entry<String, IWOrgReportResponseBean> entry : cancelResponseMap.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean cancelResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean
						.setCancelAmt(cancelResponseBean.getCancelAmt());
			} else {
				saleResponseMap.put(date, cancelResponseBean);
			}
		}

		for (Map.Entry<String, IWOrgReportResponseBean> entry : pwtResponseMap.entrySet()) {
			String date = entry.getKey();
			IWOrgReportResponseBean pwtResponseBean = entry.getValue();
			if (saleResponseMap.containsKey(date)) {
				IWOrgReportResponseBean saleResponseBean = saleResponseMap.get(date);
				saleResponseBean.setPwtAmt(pwtResponseBean.getPwtAmt());
			} else {
				saleResponseMap.put(date, pwtResponseBean);
			}
		}

		return saleResponseMap;
	}

}
