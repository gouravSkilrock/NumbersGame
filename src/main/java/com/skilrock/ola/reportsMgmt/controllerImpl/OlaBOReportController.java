package com.skilrock.ola.reportsMgmt.controllerImpl;

import java.sql.Connection;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.ola.reportsMgmt.daoImpl.OlaBoReportDaoImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class OlaBOReportController {
	
	public static OlaOrgReportResponseBean fetchDepositWithdrawlBO(OlaOrgReportRequestBean requestBean, Connection connection) throws GenericException {
		OlaOrgReportResponseBean responseBean=	OlaBoReportDaoImpl.fetchDepositDirectPlayerDataBO(requestBean, connection);		
		OlaOrgReportResponseBean directPlrWithdrawlResponseBean=OlaBoReportDaoImpl.fetchWithdrawDirectPlayerDataBO(requestBean, connection);
		responseBean.setMrpWithdrawalAmt(directPlrWithdrawlResponseBean.getMrpWithdrawalAmt());
		return responseBean;
	}
}
