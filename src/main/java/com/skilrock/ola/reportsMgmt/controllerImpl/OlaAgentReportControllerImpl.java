package com.skilrock.ola.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.ola.reportsMgmt.daoImpl.OlaAgentReportDaoImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class OlaAgentReportControllerImpl {
	Logger logger = LoggerFactory.getLogger("OlaAgentReportControllerImpl");

	public static OlaOrgReportResponseBean fetchDepositWithdrawlSinglaAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean=	OlaAgentReportDaoImpl.fetchDepositDataSingleAgent(requestBean, connection);
		OlaOrgReportResponseBean DirectPlayerDepositBean=	OlaAgentReportDaoImpl.fetchDepositDirectPlayerDataSingleAgent(requestBean, connection);

		OlaOrgReportResponseBean withdrawlResponseBean=OlaAgentReportDaoImpl.fetchWithdrawDataSingleAgent(requestBean, connection);
		
		OlaOrgReportResponseBean directPlrWithdrawlResponseBean=OlaAgentReportDaoImpl.fetchWithdrawDirectPlayerDataSingleAgent(requestBean, connection);

		responseBean.setMrpDepositAmt(responseBean.getMrpDepositAmt()+DirectPlayerDepositBean.getMrpDepositAmt());
		responseBean.setNetDepositAmt(responseBean.getNetDepositAmt()+DirectPlayerDepositBean.getNetDepositAmt());
		responseBean.setMrpWithdrawalAmt(withdrawlResponseBean.getMrpWithdrawalAmt()+directPlrWithdrawlResponseBean.getMrpWithdrawalAmt());
		responseBean.setNetWithdrawalAmt(withdrawlResponseBean.getNetWithdrawalAmt()+directPlrWithdrawlResponseBean.getNetWithdrawalAmt());
		return responseBean;
	}
	
	public static Map<Integer, OlaOrgReportResponseBean> fetchDepositWithdrawlMultipleAgent(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<Integer, OlaOrgReportResponseBean> depositResponseMap=	OlaAgentReportDaoImpl.fetchDepositDataMultipleAgent(requestBean, connection);
		
		Map<Integer, OlaOrgReportResponseBean> withdrawlResponseMap=OlaAgentReportDaoImpl.fetchWithdrawDataMultipleAgent(requestBean, connection);
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
		
		Map<Integer, OlaOrgReportResponseBean> DirectPlrDepositResponseMap=	OlaAgentReportDaoImpl.fetchDepositDirectPlayerDataMultipleAgent(requestBean, connection);
		for(Map.Entry<Integer, OlaOrgReportResponseBean> directEntry:DirectPlrDepositResponseMap.entrySet()){
			int orgId=directEntry.getKey();
			OlaOrgReportResponseBean directPlrResponseBean=directEntry.getValue();
			if(depositResponseMap.containsKey(orgId)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(orgId);
				depositResponseBean.setMrpDepositAmt(depositResponseBean.getMrpDepositAmt()+directPlrResponseBean.getMrpDepositAmt());
				depositResponseBean.setNetDepositAmt(depositResponseBean.getNetDepositAmt()+directPlrResponseBean.getNetDepositAmt());
			}else{
				depositResponseMap.put(orgId, directPlrResponseBean);
			}
		}
		
		
		Map<Integer, OlaOrgReportResponseBean> directPlrWithdrawlResponseMap=	OlaAgentReportDaoImpl.fetchWithdrawDirectPlayerDataMultipleAgent(requestBean, connection);
		for(Map.Entry<Integer, OlaOrgReportResponseBean> directEntry:directPlrWithdrawlResponseMap.entrySet()){
			int orgId=directEntry.getKey();
			OlaOrgReportResponseBean directPlrResponseBean=directEntry.getValue();
			if(depositResponseMap.containsKey(orgId)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(orgId);
				depositResponseBean.setMrpWithdrawalAmt(depositResponseBean.getMrpWithdrawalAmt()+directPlrResponseBean.getMrpWithdrawalAmt());
				depositResponseBean.setNetWithdrawalAmt(depositResponseBean.getNetWithdrawalAmt()+directPlrResponseBean.getNetWithdrawalAmt());
			}else{
				depositResponseMap.put(orgId, directPlrResponseBean);
			}
		}
		
		
		return depositResponseMap;
	}
	
	public static Map<String, OlaOrgReportResponseBean> fetchDepositWithdrawlSingleAgentDateWise(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		Map<String, OlaOrgReportResponseBean> depositResponseMap=	OlaAgentReportDaoImpl.fetchDepositDataSingleAgentDateWise(requestBean, connection);
		
		Map<String, OlaOrgReportResponseBean> withdrawlResponseMap=OlaAgentReportDaoImpl.fetchWithdrawDataSingleAgentDateWise(requestBean, connection);
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
		
		Map<String, OlaOrgReportResponseBean> DirectPlrDepositResponseMap=	OlaAgentReportDaoImpl.fetchDepositDirectPlayerDataSingleAgentDateWise(requestBean, connection);
		for(Map.Entry<String, OlaOrgReportResponseBean> directEntry:DirectPlrDepositResponseMap.entrySet()){
			String txnDate=directEntry.getKey();
			OlaOrgReportResponseBean directPlrResponseBean=directEntry.getValue();
			if(depositResponseMap.containsKey(txnDate)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(txnDate);
				depositResponseBean.setMrpDepositAmt(depositResponseBean.getMrpDepositAmt()+directPlrResponseBean.getMrpDepositAmt());
				depositResponseBean.setNetDepositAmt(depositResponseBean.getNetDepositAmt()+directPlrResponseBean.getNetDepositAmt());
			}else{
				depositResponseMap.put(txnDate, directPlrResponseBean);
			}
		}
		
		
		Map<String, OlaOrgReportResponseBean> directPlrWithdrawlResponseMap=	OlaAgentReportDaoImpl.fetchWithdrawDirectPlayerDataSingleAgentDateWise(requestBean, connection);
		for(Map.Entry<String, OlaOrgReportResponseBean> directEntry:directPlrWithdrawlResponseMap.entrySet()){
			String txnDate=directEntry.getKey();
			OlaOrgReportResponseBean directPlrResponseBean=directEntry.getValue();
			if(depositResponseMap.containsKey(txnDate)){
				OlaOrgReportResponseBean depositResponseBean = depositResponseMap.get(txnDate);
				depositResponseBean.setMrpWithdrawalAmt(depositResponseBean.getMrpWithdrawalAmt()+directPlrResponseBean.getMrpWithdrawalAmt());
				depositResponseBean.setNetWithdrawalAmt(depositResponseBean.getNetWithdrawalAmt()+directPlrResponseBean.getNetWithdrawalAmt());
			}else{
				depositResponseMap.put(txnDate, directPlrResponseBean);
			}
		}
		
		
		return depositResponseMap;
	}
	
	
}