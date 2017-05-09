package com.skilrock.lms.api.reports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.skilrock.lms.api.reports.beans.LmsApiReportConsolidateGameDataBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportDGConsolidateDrawBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportDGLMSSaleBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportDGPMSSaleBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportRequestDataBean;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DGLMSSaleBean;
import com.skilrock.lms.dge.beans.DGPMSSaleBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class LmsReportsServicesApiHelper {

	public LmsApiReportConsolidateGameDataBean getDrawGameReport(LmsApiReportConsolidateGameDataBean lmsApiReportConsolidateGameDataBean,LmsApiReportRequestDataBean lmsApiReportRequestDataBean,String raffleTktType,String refMerchantId){
		DrawGameMgmtHelper helper=new DrawGameMgmtHelper();
		
		lmsApiReportConsolidateGameDataBean.setGameCode(lmsApiReportRequestDataBean.getGameCode());
		lmsApiReportConsolidateGameDataBean.setStartData(lmsApiReportRequestDataBean.getStartDate());
		lmsApiReportConsolidateGameDataBean.setEndData(lmsApiReportRequestDataBean.getEndDate());
		
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setFromDate(lmsApiReportRequestDataBean.getStartDate());
		drawDataBean.setToDate(lmsApiReportRequestDataBean.getEndDate());
		drawDataBean.setGameNo(Util.getGameId(lmsApiReportRequestDataBean.getGameCode()));
		drawDataBean.setAgentOrgId(-1);
		drawDataBean.setDrawName("ALL");
		drawDataBean.setMerchantId(refMerchantId);
		DGConsolidateGameDataBean dgConsolidateGameDataBean=helper.fetchDrawGameDataForLMS(drawDataBean,raffleTktType, "ALL", "ALL");
		List<LmsApiReportDGConsolidateDrawBean> drawDataBeanList=new ArrayList<LmsApiReportDGConsolidateDrawBean>();
		lmsApiReportConsolidateGameDataBean.setIsSuccess(true);
		lmsApiReportConsolidateGameDataBean.setErrorCode("100");
		Iterator<DGConsolidateDrawBean> it =dgConsolidateGameDataBean.getDrawDataBeanList().iterator();
		while(it.hasNext())
		{
			LmsApiReportDGLMSSaleBean lmsApiReportDGLMSSaleBean=new LmsApiReportDGLMSSaleBean();
			LmsApiReportDGPMSSaleBean lmsApiReportDGPMSSaleBean=new LmsApiReportDGPMSSaleBean();
			LmsApiReportDGConsolidateDrawBean lmsApiReportDGConsolidateDrawBean=new LmsApiReportDGConsolidateDrawBean();
			DGConsolidateDrawBean dgConsolidateDrawBean=it.next();
			DGLMSSaleBean dgLMSSaleBean=dgConsolidateDrawBean.getLmsSaleBean();
			DGPMSSaleBean dgPMSSaleBean=dgConsolidateDrawBean.getPmsSaleBean();
			
			lmsApiReportDGConsolidateDrawBean.setDrawDateTime(dgConsolidateDrawBean.getDrawDateTime());
			lmsApiReportDGConsolidateDrawBean.setDrawDay(dgConsolidateDrawBean.getDrawDay());
			lmsApiReportDGConsolidateDrawBean.setDrawFreezeTime(dgConsolidateDrawBean.getDrawFreezeTime());
			lmsApiReportDGConsolidateDrawBean.setDrawEventId(dgConsolidateDrawBean.getDrawEventId());
			lmsApiReportDGConsolidateDrawBean.setDrawName(dgConsolidateDrawBean.getDrawName());
			lmsApiReportDGConsolidateDrawBean.setDrawStatus(dgConsolidateDrawBean.getDrawStatus());
			lmsApiReportDGConsolidateDrawBean.setWinningResult(dgConsolidateDrawBean.getWinningResult());
			
			lmsApiReportDGLMSSaleBean.setClaimedAmount(dgLMSSaleBean.getClaimedAmount());
			lmsApiReportDGLMSSaleBean.setTotalClaimedTickets(dgLMSSaleBean.getTotalClaimedTickets());
			lmsApiReportDGLMSSaleBean.setTotalNoTickets(dgLMSSaleBean.getTotalNoTickets());
			lmsApiReportDGLMSSaleBean.setTotalSaleValue(dgLMSSaleBean.getTotalSaleValue());
			lmsApiReportDGLMSSaleBean.setTotalWinningAmount(dgLMSSaleBean.getTotalWinningAmount());
			lmsApiReportDGLMSSaleBean.setTotalWinningTickets(dgLMSSaleBean.getTotalWinningTickets());
			lmsApiReportDGLMSSaleBean.setTotalUnclaimedTickets(dgLMSSaleBean.getTotalWinningTickets()-dgLMSSaleBean.getTotalClaimedTickets());
			lmsApiReportDGConsolidateDrawBean.setLmsSaleBean(lmsApiReportDGLMSSaleBean);
			
			lmsApiReportDGPMSSaleBean.setClaimedAmount(dgPMSSaleBean.getClaimedAmount());
			lmsApiReportDGPMSSaleBean.setTotalClaimedTickets(dgPMSSaleBean.getTotalClaimedTickets());
			lmsApiReportDGPMSSaleBean.setTotalNoTickets(dgPMSSaleBean.getTotalNoTickets());
			lmsApiReportDGPMSSaleBean.setTotalSaleValue(dgPMSSaleBean.getTotalSaleValue());
			lmsApiReportDGPMSSaleBean.setTotalWinningAmount(dgPMSSaleBean.getTotalWinningAmount());
			lmsApiReportDGPMSSaleBean.setTotalWinningTickets(dgPMSSaleBean.getTotalWinningTickets());
			lmsApiReportDGPMSSaleBean.setTotalUnclaimedTickets(dgPMSSaleBean.getTotalWinningTickets()-dgPMSSaleBean.getTotalClaimedTickets());
			lmsApiReportDGConsolidateDrawBean.setPmsSaleBean(lmsApiReportDGPMSSaleBean);
			
			drawDataBeanList.add(lmsApiReportDGConsolidateDrawBean);
		}
		lmsApiReportConsolidateGameDataBean.setDrawDataBeanList(drawDataBeanList);
		return lmsApiReportConsolidateGameDataBean;
	}
	
	public static void main(String s[]){
		LmsApiReportConsolidateGameDataBean lmsApiReportConsolidateGameDataBean=new LmsApiReportConsolidateGameDataBean();
		
		lmsApiReportConsolidateGameDataBean.setStartData("2013-08-23 00:00:00");
		lmsApiReportConsolidateGameDataBean.setEndData("2013-08-23 23:59:59");
		//lmsApiReportConsolidateGameDataBean.setGameId(2);
		
		//getDrawGameReport(lmsApiReportConsolidateGameDataBean,"NA");
		
	}
}
