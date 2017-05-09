package com.skilrock.ola.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.ola.reportsMgmt.daoImpl.OlaRetailerReportDaoImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class OlaRetailerReportControllerImpl {
	Logger logger = LoggerFactory.getLogger("OlaRetailerReportControllerImpl");

	public static OlaOrgReportResponseBean fetchDepositWithdrawlSinglaRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean=	OlaRetailerReportDaoImpl.fetchDepositDataSingleRetailer(requestBean, connection);
		
		OlaOrgReportResponseBean withdrawlResponseBean=OlaRetailerReportDaoImpl.fetchWithdrawDataSingleRetailer(requestBean, connection);
		responseBean.setMrpWithdrawalAmt(withdrawlResponseBean.getMrpWithdrawalAmt());
		responseBean.setNetWithdrawalAmt(withdrawlResponseBean.getNetWithdrawalAmt());
		return responseBean;
	}
	
	public static Map<Integer, OlaOrgReportResponseBean> fetchDepositWithdrawlMultipleRetailer(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> depositResponseMap=	OlaRetailerReportDaoImpl.fetchDepositDataMultipleRetailer(requestBean, connection);
		
		Map<Integer, OlaOrgReportResponseBean> withdrawlResponseMap=OlaRetailerReportDaoImpl.fetchWithdrawDataMultipleRetailer(requestBean, connection);
		for(Map.Entry<Integer, OlaOrgReportResponseBean> entry:withdrawlResponseMap.entrySet()){
			int orgId=entry.getKey();
			OlaOrgReportResponseBean withdrawlResponseBean=entry.getValue();
			if(depositResponseMap.containsKey(orgId)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(orgId);
				depositResponseBean.setMrpWithdrawalAmt(withdrawlResponseBean.getMrpWithdrawalAmt());
				depositResponseBean.setNetWithdrawalAmt(withdrawlResponseBean.getNetWithdrawalAmt());
			}else{
				depositResponseMap.put(orgId, withdrawlResponseBean);
			}
		}
		
		return depositResponseMap;
	}
	
	public static Map<String, OlaOrgReportResponseBean> fetchDepositWithdrawlSingleRetailerDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> depositResponseMap=	OlaRetailerReportDaoImpl.fetchDepositDataSingleRetailerDateWise(requestBean, connection);
		
		Map<String, OlaOrgReportResponseBean> withdrawlResponseMap=OlaRetailerReportDaoImpl.fetchWithdrawDataSingleRetailerDateWise(requestBean, connection);
		for(Map.Entry<String, OlaOrgReportResponseBean> entry:withdrawlResponseMap.entrySet()){
			String txnDate=entry.getKey();
			OlaOrgReportResponseBean withdrawlResponseBean=entry.getValue();
			if(depositResponseMap.containsKey(txnDate)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(txnDate);
				depositResponseBean.setMrpWithdrawalAmt(withdrawlResponseBean.getMrpWithdrawalAmt());
				depositResponseBean.setNetWithdrawalAmt(withdrawlResponseBean.getNetWithdrawalAmt());
			}else{
				depositResponseMap.put(txnDate, withdrawlResponseBean);
			}
		}
		
		return depositResponseMap;
	}
	
	
}