package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.util.List;
import com.skilrock.lms.beans.SaleReportBean;


public interface ISaleReportsAgentHelper {

	public String getGameDetail(Connection conn, int gameid,
			SaleReportBean bean) ;

	public List<Integer> getGameId() ;

	public SaleReportBean getPurchaseDetailWithBo() ;

	public List<Integer> getRetailerId() ;

	public String getRetailerName(Connection conn, int retailerOrgId) ;

	public List<SaleReportBean> getSaleDetailGameWise(List<Integer> idlist);

	public List<SaleReportBean> getSaleDetailRetailerWise(List<Integer> idlist) ;

}
