package com.skilrock.lms.common.utility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.AgentCollectionReportOverAllBean;
import com.skilrock.lms.beans.BodyText;
import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.EmailReportBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportHelper;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportHelperSP;
import com.skilrock.lms.coreEngine.commercialService.reportMgmt.CSSaleReportIF;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawPwtReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawPwtReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelperSP;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDrawPwtReportHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.IDrawSaleReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.AgentCollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsAgentHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CompleteCollectionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CompleteCollectionReportHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICashChqReportsAgentHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICashChqReportsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICompleteCollectionReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetActivityReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ISaleReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.IScratchPwtReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.IScratchSaleReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.SaleReportsAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.SaleReportsAgentHelperSP;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchPwtReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchPwtReportHelperSP;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchSaleReportHelper;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.ScratchSaleReportHelperSP;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.ReportBeanDrawModule;
import com.skilrock.lms.dge.beans.ReportDrawBean;
import com.skilrock.lms.scheduler.MailBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class SendReportMailerMain {

	static Log logger = LogFactory.getLog(SendReportMailerMain.class);

	public static void main(String args[]) throws SQLException {
		// dailySaleReport();
	}

	private List<CollectionReportBean> agtList = null;
	private Map<Integer, List<CollectionReportBean>> bigMap = new TreeMap<Integer, List<CollectionReportBean>>();
	private Connection con = null;
	private DateBeans dateBeans = null;
	private boolean isDraw = false;
	private boolean isScratch = true;
	private boolean isCS = false;
	private boolean isOLA = false;

	public SendReportMailerMain(DateBeans datebean) {
		this.dateBeans = datebean;

	}

	public SendReportMailerMain() {
	}

	/**
	 * this method added the list of BO recipients into the list
	 * 
	 * @param bean
	 * @throws LMSException
	 */
	private void addBORecipient(MailBean bean, String type) throws LMSException {
		try {
			// PreparedStatement
			// pstmtt=con.prepareStatement(QueryManager.getST_REPORT_MAIL_SCHEDULER_GET_BO_EMAILID());
			PreparedStatement pstmtt = con
					.prepareStatement("select distinct(aa.user_id),ref_user_id, email_id, "
							+ "first_name, last_name from st_lms_report_email_priv_master aa, st_lms_report_email_priviledge_rep bb, "
							+ "st_lms_report_email_user_master cc where cc.user_id = aa.user_id and aa.email_pid = bb.email_pid"
							+ " and cc.organization_type='BO' and  (aa.status='ACTIVE' and bb.status='ACTIVE' and "
							+ "cc.status ='ACTIVE') and bb.priv_title like '%"
							+ type + "%'");
			System.out
					.println("query to get recipient email list == " + pstmtt);
			ResultSet results = pstmtt.executeQuery();
			while (results.next()) {
				bean.to.add(results.getString("email_id").trim());
			}
			System.out.println("back office email list == " + bean.to);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	/**
	 * This method call the helpers ( sale Report Helper ,Cash Chq Report Helper
	 * and PWT Report Helper ) , get the data and created the excel file. mail
	 * 
	 * @param fileName
	 * @throws LMSException
	 * @throws SQLException
	 */
	private void createXLSSheet(String fileName, double boDirPlrPwtForScratch,
			double boDirPlrPwtForDraw, Timestamp startDate, Timestamp endDate)
			throws LMSException, SQLException {
		try {

			ICompleteCollectionReportHelper helper = null;
			IDrawSaleReportHelper drawSalehelper = null;
			IDrawPwtReportHelper drawPwthelper = null;
			IScratchSaleReportHelper scratchSalehelper = null;
			IScratchPwtReportHelper scratchPwthelper = null;
			CSSaleReportIF csSaleHelper = null;
			List<SalePwtReportsBean> drawSaleReportList = null;
			List<SalePwtReportsBean> drawPwtReportList = null;
			List<SalePwtReportsBean> scratchSaleReportList = null;
			List<SalePwtReportsBean> scratchPwtReportList = null;
			List<CSSaleReportBean> csSaleReportList = null;

			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new CompleteCollectionReportHelperSP();
				drawSalehelper = new DrawSaleReportHelperSP();
				drawPwthelper = new DrawPwtReportHelperSP();
				scratchSalehelper = new ScratchSaleReportHelperSP();
				scratchPwthelper = new ScratchPwtReportHelperSP();
				csSaleHelper = new CSSaleReportHelperSP();
			} else {
				helper = new CompleteCollectionReportHelper();
				drawSalehelper = new DrawSaleReportHelper();
				drawPwthelper = new DrawPwtReportHelper();
				scratchSalehelper = new ScratchSaleReportHelper();
				scratchPwthelper = new ScratchPwtReportHelper();
				csSaleHelper = new CSSaleReportHelper();
			}

			boolean isDraw = LMSFilterDispatcher.getIsDraw().equalsIgnoreCase(
					"yes") ? true : false;
			boolean isScratch = LMSFilterDispatcher.getIsScratch()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isCS = LMSFilterDispatcher.getIsCS()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isOLA = LMSFilterDispatcher.getIsOLA().equalsIgnoreCase(
					"yes") ? true : false;
			// for GAME WISE/PRODUCT WISE/WALLET WISE

			if (isDraw) {
				drawSaleReportList = drawSalehelper.drawSaleGameWise(startDate,
						endDate);
				drawPwtReportList = drawPwthelper.drawPwtGameWise(startDate,
						endDate);
			}
			if (isScratch) {
				scratchSaleReportList = scratchSalehelper.scratchSaleGameWise(
						startDate, endDate);
				scratchPwtReportList = scratchPwthelper.scratchPwtGameWise(
						startDate, endDate);
			}
			if (isCS) {
				csSaleReportList = csSaleHelper.CSSaleCategoryWise(startDate,
						endDate);
			}
			if (isOLA) {
				// Code For OLA
			}

			dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
					dateBeans.getLastdate()).getTime()));
			CashChqReportsHelper cashChqHelper = new CashChqReportsHelper(
					dateBeans);
			List<Integer> agtOrgIdList = cashChqHelper.getAgentOrgList();
			List<CashChqReportBean> cashChqList = null;
			OrganizationTerminateReportHelper.getTerminateAgentListForRep(
					new Timestamp(dateBeans.getStartDate().getTime()),
					new Timestamp(dateBeans.getEndDate().getTime()));
			List<Integer> terminateAgentList = OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
			System.out.println("Terminate agent List::" + terminateAgentList);
			agtOrgIdList.removeAll(terminateAgentList);
			if (!agtOrgIdList.isEmpty()) {
				cashChqList = cashChqHelper.getCashChqDetail(agtOrgIdList, -1,
						true);
			}
			dateBeans.setLastdate(new java.sql.Date(dateBeans.getLastdate()
					.getTime()
					- 24 * 60 * 60 * 1000));
			Map<String, CompleteCollectionBean> reportMapAgentWise = helper
					.collectionReport(startDate, endDate, "Agent Wise");

			ReportMailExl exl = new ReportMailExl(fileName, dateBeans);
			exl.CreateXLSFile(reportMapAgentWise, drawSaleReportList,
					drawPwtReportList, scratchSaleReportList,
					scratchPwtReportList, csSaleReportList, cashChqList,
					boDirPlrPwtForDraw, boDirPlrPwtForScratch);

		} catch (WriteException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private int getActiveAgentCount() {
		int agtCount = 0;
		ResultSet rs = null;
		try {
			this.con = DBConnect.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("select count(*) as agt_count from st_lms_organization_master where organization_status = 'ACTIVE' and organization_type = 'AGENT'");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				agtCount = rs.getInt("agt_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return agtCount;
	}

	private int getActiveAgtCount() {
		int AgtCount = 0;
		return AgtCount;
	}

	private int getActiveRetailerCount(int agtOrgId) {
		int retCount = 0;
		ResultSet rs = null;
		try {
			this.con = DBConnect.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("select count(*) as ret_count from st_lms_organization_master where organization_status = 'ACTIVE' and organization_type = 'RETAILER' and parent_id=?");
			pstmt.setInt(1, agtOrgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retCount = rs.getInt("ret_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retCount;
	}

	public void getDailyCollections(String orgType, Timestamp deplDate,
			String Type) {

		Map<String, String> lastRowMap = null;
		Map<Integer, Double> mapForOpenBal = null;
		// get the list of BO details from database using helper
		if ("BO".equalsIgnoreCase(orgType)) {
			logger.debug("start date is this:" + dateBeans.getFirstdate()
					+ "end date is this:" + dateBeans.getLastdate());
			CollectionReportHelper boHelper = new CollectionReportHelper(
					dateBeans);
			mapForOpenBal = boHelper.getAgentOpeningBalance(boHelper
					.getAgentOrgId(), this.isDraw, this.isScratch, this.isCS,
					deplDate);
			this.agtList = boHelper.getBOCollectionDetail(boHelper
					.getAgentOrgId(), this.isDraw, this.isScratch, this.isCS,
					false);
			this.agtList = boHelper.MergeOpenBal(this.agtList, mapForOpenBal);
			// logger.debug(mapForOpenBal);
		}
		// get the list of AGENT details from database using helper
		else {
			try {
				this.con = DBConnect.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("select organization_id, name from st_lms_organization_master where organization_type='AGENT'");
				ResultSet rs = pstmt.executeQuery();
				List<CollectionReportBean> retList = null;
				while (rs.next()) {
					UserInfoBean infoBean = new UserInfoBean();
					infoBean.setOrgName(rs.getString("name"));
					infoBean.setUserOrgId(rs.getInt("organization_id"));
					CollectionReportAgentHelper boHelper = new CollectionReportAgentHelper(
							infoBean, dateBeans);
					mapForOpenBal = boHelper.getRetailerOpeningBalance(boHelper
							.getRetailerOrgId(), this.isDraw, this.isScratch,
							this.isOLA, this.isCS, deplDate);
					retList = boHelper.getAgentCollectionDetail(boHelper
							.getRetailerOrgId(), this.isOLA, this.isCS,
							this.isDraw, this.isScratch, false);
					retList = boHelper.MergeOpenBal(retList, mapForOpenBal);
					bigMap.put(infoBean.getUserOrgId(), retList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private synchronized MailBean mailToAgent(Connection con,
			String reportType, String emailId, int agentUserId, int agentOrgId,
			CollectionReportOverAllBean collectionReportOverAllBean)
			throws LMSException {

		MailBean mail = new MailBean();
		mail.boTextBody = "LMS Report from   "
				+ dateBeans.getFirstdate().toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4) + " TO "
				+ dateBeans.getLastdate().toString().substring(8, 10) + '-'
				+ dateBeans.getLastdate().toString().substring(5, 7) + '-'
				+ dateBeans.getLastdate().toString().substring(0, 4);
		if ("Current Day".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.DAILY_SUBJECT;
			mail.AGENT_FILE_NAME = mail.AGENT_FILE_NAME + "AGENT_"
					+ agentUserId + BodyText.DAILY_FILE;
		} else if ("Current Week".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.WEEKLY_SUBJECT;
			mail.AGENT_FILE_NAME = mail.AGENT_FILE_NAME + "AGENT_"
					+ agentUserId + BodyText.WEEKLY_FILE;
		} else if ("Current Month".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.MONTHLY_SUBJECT;
			mail.AGENT_FILE_NAME = mail.AGENT_FILE_NAME + "AGENT_"
					+ agentUserId + BodyText.MONTHLY_FILE;
		} else {
			mail.subject = "LMS Report From " + this.dateBeans.getFirstdate()
					+ " To " + this.dateBeans.getLastdate();
			mail.AGENT_FILE_NAME = mail.AGENT_FILE_NAME + "AGENT_"
					+ agentUserId + "_" + dateBeans.getLastdate() + "_"
					+ BodyText.DATE_WISE_FILE;
		}
		System.out.println("File Name----------------------------------------"
				+ mail.AGENT_FILE_NAME);

		try {
			UserInfoBean userbean = new UserInfoBean();
			userbean.setUserId(agentUserId);
			userbean.setUserOrgId(agentOrgId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startDate = new Timestamp(sdf.parse(
					sdf.format(dateBeans.getFirstdate())).getTime());
			Timestamp endDate = new Timestamp(sdf.parse(
					sdf.format(dateBeans.getLastdate())).getTime());
			SimpleDateFormat depDateFormat = new SimpleDateFormat(
					(String) ServletActionContext.getServletContext()
							.getAttribute("date_format"));
			Timestamp deplDate = new Timestamp(depDateFormat.parse(
					(String) ServletActionContext.getServletContext()
							.getAttribute("DEPLOYMENT_DATE")).getTime());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.HOUR, 23);
			cal.add(Calendar.MINUTE, 59);
			cal.add(Calendar.SECOND, 59);
			endDate = new Timestamp(cal.getTime().getTime());

			/*
			 * or For End Date endDate = new Timestamp((new
			 * SimpleDateFormat(dateFormat)).parse( end_Date).getTime() + 24 *
			 * 60 * 60 * 1000 - 1000);
			 */

			this.con = DBConnect.getConnection();
			boolean isDraw = LMSFilterDispatcher.getIsDraw().equalsIgnoreCase(
					"yes") ? true : false;
			boolean isScratch = LMSFilterDispatcher.getIsScratch()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isCS = LMSFilterDispatcher.getIsCS()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isOLA = LMSFilterDispatcher.getIsOLA().equalsIgnoreCase(
					"yes") ? true : false;
			Double totalDrawSale = 0.0;
			Double totalScratchSale = 0.0;
			Double totalDrawPwt = 0.0;
			Double totalScratchPwt = 0.0;
			Double netPayment = 0.0;
			Double totalCSSale = 0.0;
			Double totalOLAWithdraw = 0.0;
			Double totalOLADeposit = 0.0;
			Double totalOLANetGamming = 0.0;
			Double totalExpenseForRetailer = 0.0;
			Double totalExpenseAtBo = 0.0;
			double netPaymentsAtBo = 0.0;
			double netDgSaleAtBo = 0.0;
			double netDgPwtAtBo = 0.0;
			double netSeSaleAtBo = 0.0;
			double netSePwtAtBo = 0.0;
			double netCsSaleAtBo = 0.0;
			double netOlaDepAtBo = 0.0;
			double netOlaWithAtBo = 0.0;
			double netOlaNetGameAtBo = 0.0;
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;

			if ("Current Day".equalsIgnoreCase(reportType)) {
				pstmt1 = con
						.prepareStatement("select count(*)as logged_users from st_lms_user_master where organization_type='RETAILER' and parent_user_id = ? and date(last_login_date) =?");
				pstmt1.setInt(1, agentUserId);
				pstmt1.setDate(2, dateBeans.getFirstdate());
				rs1 = pstmt1.executeQuery();
			}

			ICompleteCollectionReportHelper dgScSalePwtRetWiseHelper = null;
			CSSaleReportIF csSaleRetWiseHelper = null;

			IDrawSaleReportHelper drawSaleGameWiseHelperForAgent = null;
			IDrawPwtReportHelper drawPwtGameWiseHelperForAgent = null;

			IScratchSaleReportHelper scratchSaleGameWiseForAgent = null;
			IScratchPwtReportHelper scratchPwtGameWiseForAgent = null;

			ISaleReportsAgentHelper scratchPurchaseDetailsWithBo = null;

			ICashChqReportsAgentHelper agentCashChqHelper = null;

			UserInfoBean infoBean = new UserInfoBean();
			infoBean.setUserId(agentUserId);
			infoBean.setUserOrgId(agentOrgId);

			if (LMSFilterDispatcher.isRepFrmSP) {
				dgScSalePwtRetWiseHelper = new CompleteCollectionReportHelperSP();
				csSaleRetWiseHelper = new CSSaleReportHelperSP();
				scratchSaleGameWiseForAgent = new ScratchSaleReportHelperSP();
				drawSaleGameWiseHelperForAgent = new DrawSaleReportHelperSP();
				drawPwtGameWiseHelperForAgent = new DrawPwtReportHelperSP();
				scratchPwtGameWiseForAgent = new ScratchPwtReportHelperSP();
				scratchPurchaseDetailsWithBo = new SaleReportsAgentHelperSP(
						infoBean, dateBeans);
				dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
						dateBeans.getLastdate()).getTime()));
				agentCashChqHelper = new CashChqReportsAgentHelperSP(infoBean,
						dateBeans);
				dateBeans.setLastdate(new java.sql.Date(dateBeans.getLastdate()
						.getTime()
						- 24 * 60 * 60 * 1000));
			} else {
				dgScSalePwtRetWiseHelper = new CompleteCollectionReportHelper();
				csSaleRetWiseHelper = new CSSaleReportHelper();
				scratchSaleGameWiseForAgent = new ScratchSaleReportHelper();
				drawSaleGameWiseHelperForAgent = new DrawSaleReportHelper();
				drawPwtGameWiseHelperForAgent = new DrawPwtReportHelper();
				scratchPwtGameWiseForAgent = new ScratchPwtReportHelper();
				scratchPurchaseDetailsWithBo = new SaleReportsAgentHelper(
						infoBean, dateBeans);
				dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
						dateBeans.getLastdate()).getTime()));
				agentCashChqHelper = new CashChqReportsAgentHelper(infoBean,
						dateBeans);
				dateBeans.setLastdate(new java.sql.Date(dateBeans.getLastdate()
						.getTime()
						- 24 * 60 * 60 * 1000));
			}
			// FOR Reatiler Wise
			Map<String, CompleteCollectionBean> dgScSalePwtRetWiseMap = dgScSalePwtRetWiseHelper
					.collectionReportForAgent(startDate, endDate, agentOrgId,
							"Retailer Wise");
			List<CSSaleReportBean> csSaleRetWiseList = null;
			List<CSSaleReportBean> csSaleProductWiseForAgentList = null;
			List<SalePwtReportsBean> drawSaleGameWiseListForAgent = null;
			List<SalePwtReportsBean> drawPwtGameWiseListForAgent = null;
			List<SalePwtReportsBean> scratchSaleGameWiseForAgentList = null;
			List<SalePwtReportsBean> scratchPwtGameWiseForAgentList = null;
			List<SaleReportBean> purchWithBOGameWise = null;
			SaleReportBean purchWithBO = null;
			if (isCS) {
				// Retailer Wise
				csSaleRetWiseList = csSaleRetWiseHelper.CSSaleRetailerWise(
						startDate, endDate, agentOrgId);
				// Product Wise
				csSaleProductWiseForAgentList = csSaleRetWiseHelper
						.CSSaleProductWiseAgentWise(startDate, endDate,
								agentOrgId);
			}

			if (isDraw) {
				// Game Wise
				ReportStatusBean reportStatusBean = new ReportStatusBean();
				reportStatusBean.setReportingFrom("MAIN_DB");
				drawSaleGameWiseListForAgent = drawSaleGameWiseHelperForAgent
						.drawSaleGameWiseForAgent(startDate, endDate,
								agentOrgId, reportStatusBean);
				drawPwtGameWiseListForAgent = drawPwtGameWiseHelperForAgent
						.drawPwtGameWiseForAgent(startDate, endDate, agentOrgId);
			}

			if (isScratch) {
				// Game Wise
				scratchSaleGameWiseForAgentList = scratchSaleGameWiseForAgent
						.scratchSaleAgentWiseExpand(startDate, endDate,
								agentOrgId);
				scratchPwtGameWiseForAgentList = scratchPwtGameWiseForAgent
						.scratchPwtAgentWiseExpand(startDate, endDate,
								agentOrgId);
				// purchanse details with BO
				purchWithBO = scratchPurchaseDetailsWithBo
						.getPurchaseDetailWithBo();
				purchWithBOGameWise = scratchPurchaseDetailsWithBo
						.getSaleDetailGameWise(scratchPurchaseDetailsWithBo
								.getGameId());
			}
			dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
					dateBeans.getLastdate()).getTime()));
			List<CashChqReportBean> cashChqWithRetlist = agentCashChqHelper
					.getCashChqDetail();
			CashChqReportBean cashChqWithBoBean = agentCashChqHelper
					.getCashChqDetailWithBO();
			dateBeans.setLastdate(new java.sql.Date(dateBeans.getLastdate()
					.getTime()
					- 24 * 60 * 60 * 1000));
			EmailReportBean emailRepBeanAgt = new EmailReportBean();
			netPaymentsAtBo = collectionReportOverAllBean.getCash()
					+ collectionReportOverAllBean.getCheque()
					+ collectionReportOverAllBean.getCredit()
					+ collectionReportOverAllBean.getBankDep()
					- collectionReportOverAllBean.getDebit()
					- collectionReportOverAllBean.getChequeReturn();

			netDgSaleAtBo = collectionReportOverAllBean.getDgSale()
					- collectionReportOverAllBean.getDgCancel();
			netDgPwtAtBo = collectionReportOverAllBean.getDgPwt()
					+ collectionReportOverAllBean.getDgDirPlyPwt();
			netSeSaleAtBo = collectionReportOverAllBean.getSeSale()
					- collectionReportOverAllBean.getSeCancel();
			netSePwtAtBo = collectionReportOverAllBean.getSePwt()
					+ collectionReportOverAllBean.getSeDirPlyPwt();
			netCsSaleAtBo = collectionReportOverAllBean.getCSSale()
					- collectionReportOverAllBean.getCSCancel();
			netOlaDepAtBo = collectionReportOverAllBean.getDeposit()
					- collectionReportOverAllBean.getDepositRefund();
			netOlaWithAtBo = collectionReportOverAllBean.getWithdrawal()
					- collectionReportOverAllBean.getWithdrawalRefund();
			netOlaNetGameAtBo = collectionReportOverAllBean.getNetGamingComm();

			// START FROM HERE
			totalExpenseAtBo = netDgSaleAtBo - netDgPwtAtBo - netPaymentsAtBo
					+ netSeSaleAtBo - netSePwtAtBo + netCsSaleAtBo
					+ netOlaDepAtBo - netOlaWithAtBo - netOlaNetGameAtBo;
			mail.boTextBody += "<br><br><br><b>Details of Transaction With BO</b>";
			mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";
			if (isDraw) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Draw Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Purchase</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netDgSaleAtBo)
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netDgPwtAtBo)
						+ "</td>"
						+ "</tr>";
			}
			if (isScratch) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Scratch Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Purchase</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netSeSaleAtBo)
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netSePwtAtBo)
						+ "</td>"
						+ "</tr>";
			}
			if (isCS) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Commercial Services</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netCsSaleAtBo)
						+ "</td>"
						+ "</tr>";
			}
			if (isOLA) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>OLA Service</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Total Deposit</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netOlaDepAtBo)
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Total Withdraw</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netOlaWithAtBo)
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Total Net Gaming</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(netOlaNetGameAtBo)
						+ "</td>" + "</tr>";
			}
			mail.boTextBody += "<tr>"
					+ "<td width='200'><B>Net Payment</B></td>"
					+ "<td align='right' width='150'>" + BodyText.INR
					+ FormatNumber.formatNumber(netPaymentsAtBo) + "</td>"
					+ "</tr>" + "<tr>"
					+ "<td width='200'><B>Total Expense</B></td>"
					+ "<td align='right' width='150'>" + BodyText.INR
					+ FormatNumber.formatNumber(totalExpenseAtBo) + "</td>"
					+ "</tr>" + "</table>";

			// Details With Retailer
			AgentCollectionReportOverAllHelper agentHelper = new AgentCollectionReportOverAllHelper();
			Map<String, AgentCollectionReportOverAllBean> resultMapForAgent = agentHelper
					.collectionRetailerWiseWithOpeningBal(agentOrgId, deplDate,
							startDate, endDate);

			logger
					.debug("The size of bean Map is: "
							+ resultMapForAgent.size());

			Set<Map.Entry<String, AgentCollectionReportOverAllBean>> set = resultMapForAgent
					.entrySet();
			Iterator<Entry<String, AgentCollectionReportOverAllBean>> it = set
					.iterator();
			System.out.println(set.size());
			while (it.hasNext()) {
				Map.Entry<String, AgentCollectionReportOverAllBean> entry = (Map.Entry<String, AgentCollectionReportOverAllBean>) it
						.next();
				AgentCollectionReportOverAllBean bean = entry.getValue();
				netPayment += bean.getCash() + bean.getBankDep()
						+ bean.getCheque() - bean.getChequeReturn()
						+ bean.getCredit() - bean.getDebit();
				System.out.println(netPayment);
				System.out.println(entry.getKey());
				if (isDraw) {
					totalDrawSale += bean.getDgSale() - bean.getDgCancel();
					totalDrawPwt += bean.getDgPwt() + bean.getDgDirPlyPwt();
				}
				if (isScratch) {
					totalScratchSale += bean.getSeSale() - bean.getSeCancel();
					totalScratchPwt += bean.getSePwt() + bean.getSeDirPlyPwt();
				}
				if (isCS) {
					totalCSSale += bean.getCSSale() - bean.getCSCancel();
					System.out.println("CS SALE " + totalCSSale);
				}
				if (isOLA) {
					totalOLADeposit += bean.getDeposit()
							- bean.getDepositRefund();
					totalOLAWithdraw += bean.getWithdrawal()
							- bean.getWithdrawalRefund();
					totalOLANetGamming += bean.getNetGamingComm();
				}
			}
			emailRepBeanAgt.setTotalDrawPurchase(totalDrawSale);
			emailRepBeanAgt.setTotalDrawPwt(totalDrawPwt);
			emailRepBeanAgt.setTotalScratchPurchase(totalScratchSale);
			emailRepBeanAgt.setTotalScratchPwt(totalScratchPwt);
			emailRepBeanAgt.setTotalCSsale(totalCSSale);
			emailRepBeanAgt.setNetPayment(netPayment);
			emailRepBeanAgt.setTotalOLADeposit(totalOLADeposit);
			emailRepBeanAgt.setTotalOLAWithdraw(totalOLAWithdraw);
			emailRepBeanAgt.setTotalOLANetGamming(totalOLANetGamming);

			totalExpenseForRetailer = totalDrawSale - totalDrawPwt - netPayment
					+ totalScratchSale - totalScratchPwt + totalCSSale
					+ totalOLADeposit - totalOLAWithdraw - totalOLANetGamming;

			if ("Current Day".equalsIgnoreCase(reportType)) {
				if (rs1.next()) {
					emailRepBeanAgt
							.setLoggedInUsers(rs1.getInt("logged_users"));
				} else {
					emailRepBeanAgt.setLoggedInUsers(0);
				}
			}
			mail.boTextBody += "<br><br><br>Details of Transaction With Retailer";
			mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";
			if (isDraw) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Draw Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalDrawPurchase())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalDrawPwt()) + "</td>" + "</tr>";
			}
			if (isScratch) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Scratch Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalScratchPurchase())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalScratchPwt()) + "</td>" + "</tr>";
			}
			if (isCS) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Commercial Services</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalCSsale()) + "</td>" + "</tr>";

			}
			if (isOLA) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>OLA Service</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Total Deposit</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalOLADeposit())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Total Withdraw</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalScratchPwt())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Total Net Gaming</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getTotalOLANetGamming()) + "</td>" + "</tr>";
			}
			mail.boTextBody += "<tr>"
					+ "<td width='200'><B>Net Received</B></td>"
					+ "<td align='right' width='150'>"
					+ BodyText.INR
					+ FormatNumber
							.formatNumber(emailRepBeanAgt.getNetPayment())
					+ "</td>" + "</tr>" + "<tr>"
					+ "<td width='200'><B>Total Collections</B></td>"
					+ "<td align='right' width='150'>" + BodyText.INR
					+ FormatNumber.formatNumber(totalExpenseForRetailer)
					+ "</td>" + "</tr>";
			if ("Current Day".equalsIgnoreCase(reportType)) {
				mail.boTextBody += "<tr>"
						+ "<td width='200'><B>Active Retailers</B></td>"
						+ "<td align='right' width='150'>"
						+ FormatNumber.formatNumber(this
								.getActiveRetailerCount(agentOrgId))
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'><B>Yesterday Logged in Retailers</B></td>"
						+ "<td align='right' width='150'>"
						+ FormatNumber.formatNumber(emailRepBeanAgt
								.getLoggedInUsers()) + "</td>" + "</tr>";
			}
			mail.boTextBody += "</table>";
			if ("Current Day".equalsIgnoreCase(reportType)) {
				mail.boTextBody += "<br><br><b> Last Day Collections </b>";
				mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";
				mail.boTextBody += "<tr>" + "<th> Retailer Name </th>"
						+ "<th>Total Collections</th>" + "</tr>";
				if (resultMapForAgent != null) {
					for (Map.Entry<String, AgentCollectionReportOverAllBean> temp : resultMapForAgent
							.entrySet()) {
						mail.boTextBody += "<tr>" + "<td>"
								+ temp.getValue().getRetailerName() + "</td>"
								+ "<td>" + temp.getValue().getOpeningBal()
								+ "</td>" + "</tr>";
					}
				}
				mail.boTextBody += "</table>";
			}
			logger.debug("\n" + mail.boTextBody);
			// create the excel
			AgentReportMailExl mailExl = new AgentReportMailExl(
					mail.AGENT_FILE_NAME, dateBeans);
			mailExl.CreateXLSFile(dgScSalePwtRetWiseMap, csSaleRetWiseList,
					drawSaleGameWiseListForAgent, drawPwtGameWiseListForAgent,
					scratchSaleGameWiseForAgentList,
					scratchPwtGameWiseForAgentList,
					csSaleProductWiseForAgentList, purchWithBO,
					purchWithBOGameWise, cashChqWithRetlist, cashChqWithBoBean,
					isDraw, isScratch, isCS, isOLA);

			// add reciepient
			mail.reciepient = emailId;
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public boolean sendMailToAgent(String reportType) throws LMSException,
			ParseException, SQLException {

		boolean isDraw = LMSFilterDispatcher.getIsDraw()
				.equalsIgnoreCase("yes") ? true : false;
		boolean isScratch = LMSFilterDispatcher.getIsScratch()
				.equalsIgnoreCase("yes") ? true : false;
		boolean isCS = LMSFilterDispatcher.getIsCS().equalsIgnoreCase("yes") ? true
				: false;
		boolean isOLA = LMSFilterDispatcher.getIsOLA().equalsIgnoreCase("yes") ? true
				: false;

		Connection con = null;
		String FROM = "", PASSWORD = "", type = "";
		ICollectionReportOverAllHelper helper = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if ("Current Day".equalsIgnoreCase(reportType)) {
			FROM = MailBean.DAILY_MAIL_FROM;
			PASSWORD = MailBean.PASSWORD;
			type = "Daily";
		} else if ("Current Week".equalsIgnoreCase(reportType)) {
			PASSWORD = MailBean.PASSWORD;
			FROM = MailBean.WEEKLY_MAIL_FROM;
			type = "Weekly";
		} else if ("Current Month".equalsIgnoreCase(reportType)) {
			FROM = MailBean.MONTHLY_MAIL_FROM;
			PASSWORD = MailBean.PASSWORD;
			type = "Monthly";
		} else {
			FROM = MailBean.MONTHLY_MAIL_FROM;
			PASSWORD = MailBean.PASSWORD;
			type = "Daily";
		}

		try {
			List<MailBean> mailbeanlist = new ArrayList<MailBean>();
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("select distinct(aa.user_id),ref_user_id, organization_id, email_id, "
							+ "first_name, last_name from st_lms_report_email_priv_master aa, st_lms_report_email_priviledge_rep bb, "
							+ "st_lms_report_email_user_master cc where cc.user_id = aa.user_id and  aa.email_pid = "
							+ "bb.email_pid and cc.organization_type = 'AGENT' and (aa.status='ACTIVE' and bb.status = "
							+ "'ACTIVE' and cc.status ='ACTIVE') and bb.priv_title like '%"
							+ type + "%'");
			ResultSet rs = pstmt.executeQuery();

			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new CollectionReportOverAllHelperSP();
			} else {
				helper = new CollectionReportOverAllHelper();
			}
			Map<String, CollectionReportOverAllBean> resultMap = helper
					.collectionAgentWiseWithOpeningBal(new Timestamp(sdf.parse(
							sdf.format(dateBeans.getFirstdate())).getTime()),
							new Timestamp(sdf.parse(
									sdf.format(dateBeans.getFirstdate()))
									.getTime()), new Timestamp(sdf.parse(
									sdf.format(dateBeans.getLastdate()))
									.getTime()
									+ 24 * 60 * 60 * 1000 - 1000), "ALL", "ALL");

			logger.debug("The size of bean Map is: " + resultMap.size());

			while (rs.next()) {
				MailBean bean = null;
				String emailId = rs.getString("email_id").trim();
				int agentUserId = rs.getInt("ref_user_id");
				int agentOrgId = rs.getInt("organization_id");
				CollectionReportOverAllBean collectionReportOverAllBean = resultMap
						.get(String.valueOf(agentOrgId));
				bean = mailToAgent(con, reportType, emailId, agentUserId,
						agentOrgId, collectionReportOverAllBean);
				if (bean != null) {
					mailbeanlist.add(bean);
				}
			}

			MailSender mailSender = new MailSender(FROM, PASSWORD, mailbeanlist); // sending
			// the
			// mail
			mailSender.setDaemon(true);
			mailSender.start();

		}catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
		return true;
	}

	/**
	 * this method send mail to Back Office regarding the Sale Report, PWT
	 * Report, and Cash Cheque Reports
	 * 
	 * @param reportType
	 *            , String i.e.('Daily','Weekly', 'Monthly')
	 * @return true if no exception occurred.
	 * @throws LMSException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean sendMailToBO(String reportType) throws LMSException,
			SQLException, ParseException {

		MailBean mail = new MailBean();
		String FROM = null;
		String PASSWORD = MailBean.PASSWORD;
		String type = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat depDateFormat = new SimpleDateFormat(
				(String) ServletActionContext.getServletContext().getAttribute(
						"date_format"));
		Timestamp deplDate = new Timestamp(depDateFormat.parse(
				(String) ServletActionContext.getServletContext().getAttribute(
						"DEPLOYMENT_DATE")).getTime());
		Timestamp startDate = new Timestamp(sdf.parse(
				sdf.format(dateBeans.getFirstdate())).getTime());
		Timestamp endDate = new Timestamp(sdf.parse(
				sdf.format(dateBeans.getLastdate())).getTime());
		mail.boTextBody = "LMS Report from   "
				+ dateBeans.getFirstdate().toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4) + " TO "
				+ dateBeans.getLastdate().toString().substring(8, 10) + '-'
				+ dateBeans.getLastdate().toString().substring(5, 7) + '-'
				+ dateBeans.getLastdate().toString().substring(0, 4);
		if ("Current Day".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.DAILY_SUBJECT;
			mail.BO_FILE_NAME = mail.BO_FILE_NAME + "BO_" + BodyText.DAILY_FILE;
			FROM = MailBean.DAILY_MAIL_FROM;
			type = "Daily";
		} else if ("Current Week".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.WEEKLY_SUBJECT;
			mail.BO_FILE_NAME = mail.BO_FILE_NAME + "BO_"
					+ BodyText.WEEKLY_FILE;
			// System.out.println("Weakly condition executed :
			// =========================================== ");
			FROM = MailBean.WEEKLY_MAIL_FROM;
			type = "Weekly";
		} else if ("Current Month".equalsIgnoreCase(reportType)) {
			mail.subject = BodyText.MONTHLY_SUBJECT;
			mail.BO_FILE_NAME = mail.BO_FILE_NAME + "BO_"
					+ BodyText.MONTHLY_FILE;
			FROM = MailBean.MONTHLY_MAIL_FROM;
			type = "Monthly";
		} else if ("Current Month".equalsIgnoreCase(reportType)) {
			// FOR NEW TYPE
			mail.subject = "Performed Draw Details";
			mail.boTextBody = "";
			FROM = MailBean.DAILY_MAIL_FROM;
			type = "Draw Wise";
		} else {
			mail.subject = "LMS Report From " + this.dateBeans.getFirstdate()
					+ " To " + this.dateBeans.getLastdate();
			mail.BO_FILE_NAME = mail.BO_FILE_NAME + "BO_"
					+ dateBeans.getLastdate() + "_" + BodyText.DATE_WISE_FILE;
			FROM = MailBean.MONTHLY_MAIL_FROM;
			type = "Daily";
		}
		System.out.println("File Name----------------------------------------"
				+ mail.BO_FILE_NAME); // created excel sheet file Name

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.HOUR, 23);
		cal.add(Calendar.MINUTE, 59);
		cal.add(Calendar.SECOND, 59);
		endDate = new Timestamp(cal.getTime().getTime());

		IDrawPwtReportHelper boDirPlrPwtHelperForDraw = null;
		IScratchPwtReportHelper boDirPlrPwtHelperForScratch = null;
		if (LMSFilterDispatcher.isRepFrmSP) {
			boDirPlrPwtHelperForDraw = new DrawPwtReportHelperSP();
			boDirPlrPwtHelperForScratch = new ScratchPwtReportHelperSP();
		} else {
			boDirPlrPwtHelperForDraw = new DrawPwtReportHelper();
			boDirPlrPwtHelperForScratch = new ScratchPwtReportHelper();
		}

		// Dir Pwt At BO For Draw
		List<SalePwtReportsBean> reportListForDraw = boDirPlrPwtHelperForDraw
				.drawBODirPlyPwtGameWise(startDate, endDate);

		Iterator<SalePwtReportsBean> boDirPwtIteratorForDraw = reportListForDraw
				.iterator();
		double boDirPlrPwtForDraw = 0.0;
		while (boDirPwtIteratorForDraw.hasNext()) {
			boDirPlrPwtForDraw += boDirPwtIteratorForDraw.next().getPwtMrpAmt();
		}

		// Dir Pwt At BO For Scratch
		List<SalePwtReportsBean> reportListForScratch = null;
		reportListForScratch = boDirPlrPwtHelperForScratch
				.scratchBODirPlyPwtGameWise(startDate, endDate);

		Iterator<SalePwtReportsBean> boDirPwtIteratorForScratch = reportListForScratch
				.iterator();
		double boDirPlrPwtForScratch = 0.0;
		while (boDirPwtIteratorForScratch.hasNext()) {
			boDirPlrPwtForScratch += boDirPwtIteratorForScratch.next()
					.getPwtMrpAmt();
		}

		createXLSSheet(mail.BO_FILE_NAME, boDirPlrPwtForScratch,
				boDirPlrPwtForDraw, startDate, endDate);

		try {
			this.con = DBConnect.getConnection();
			Double totalDrawSale = 0.0;
			Double totalScratchSale = 0.0;
			Double totalDrawPwt = 0.0;
			Double totalScratchPwt = 0.0;
			Double netPayment = 0.0;
			Double totalCSSale = 0.0;
			Double totalOLAWithdraw = 0.0;
			Double totalOLADeposit = 0.0;
			Double totalOLANetGamming = 0.0;

			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			boolean isDraw = LMSFilterDispatcher.getIsDraw().equalsIgnoreCase(
					"yes") ? true : false;
			boolean isScratch = LMSFilterDispatcher.getIsScratch()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isCS = LMSFilterDispatcher.getIsCS()
					.equalsIgnoreCase("yes") ? true : false;
			boolean isOLA = LMSFilterDispatcher.getIsOLA().equalsIgnoreCase(
					"yes") ? true : false;

			if ("Current Day".equalsIgnoreCase(reportType)) {
				pstmt1 = con
						.prepareStatement("select count(*)as logged_users from st_lms_user_master where organization_type='AGENT' and date(last_login_date) =?");
				pstmt1.setDate(1, dateBeans.getFirstdate());
				rs1 = pstmt1.executeQuery();
			}
			ICollectionReportOverAllHelper helper = null;

			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new CollectionReportOverAllHelperSP();
			} else {
				helper = new CollectionReportOverAllHelper();
			}
			EmailReportBean emailRepBean = new EmailReportBean();
			Map<String, CollectionReportOverAllBean> resultMap = helper
					.collectionAgentWiseWithOpeningBal(deplDate, startDate,
							endDate, "ALL", "ALL");

			logger.debug("The size of bean Map is: " + resultMap.size());

			Set<Map.Entry<String, CollectionReportOverAllBean>> set = resultMap
					.entrySet();
			Iterator<Entry<String, CollectionReportOverAllBean>> it = set
					.iterator();
			while (it.hasNext()) {

				Map.Entry<String, CollectionReportOverAllBean> entry = (Map.Entry<String, CollectionReportOverAllBean>) it
						.next();
				CollectionReportOverAllBean bean = entry.getValue();
				netPayment += bean.getCash() + bean.getBankDep()
						+ bean.getCheque() - bean.getChequeReturn()
						+ bean.getCredit() - bean.getDebit();

				if (isDraw) {
					totalDrawSale += bean.getDgSale() - bean.getDgCancel();
					totalDrawPwt += bean.getDgPwt() + bean.getDgDirPlyPwt();
				}
				if (isScratch) {
					totalScratchSale += bean.getSeSale() - bean.getSeCancel();
					totalScratchPwt += bean.getSePwt() + bean.getSeDirPlyPwt();
				}
				if (isCS) {
					totalCSSale += bean.getCSSale() - bean.getCSCancel();

				}
				if (isOLA) {
					totalOLADeposit += bean.getDeposit()
							- bean.getDepositRefund();
					totalOLAWithdraw += bean.getWithdrawal()
							- bean.getWithdrawalRefund();
					totalOLANetGamming += bean.getNetGamingComm();
				}
			}

			emailRepBean.setTotalDrawPurchase(totalDrawSale);
			emailRepBean.setTotalDrawPwt(totalDrawPwt);
			emailRepBean.setTotalScratchPurchase(totalScratchSale);
			emailRepBean.setTotalScratchPwt(totalScratchPwt);
			emailRepBean.setTotalCSsale(totalCSSale);
			emailRepBean.setNetPayment(netPayment);
			emailRepBean.setTotalOLADeposit(totalOLADeposit);
			emailRepBean.setTotalOLAWithdraw(totalOLAWithdraw);
			emailRepBean.setTotalOLANetGamming(totalOLANetGamming);

			Double totalCollection = totalDrawSale - totalDrawPwt
					+ totalScratchSale - totalScratchPwt + totalOLADeposit
					- totalOLAWithdraw - totalOLANetGamming - netPayment;
			if ("Current Day".equalsIgnoreCase(reportType)) {
				if (rs1.next()) {
					emailRepBean.setLoggedInUsers(rs1.getInt("logged_users"));
				} else {
					emailRepBean.setLoggedInUsers(0);
				}
			}

			mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";
			if (isDraw) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Draw Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalDrawPurchase())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalDrawPwt())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Driect Player Pwt For Draw</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(boDirPlrPwtForDraw)
						+ "</td>" + "</tr>";
			}
			if (isScratch) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Scratch Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalScratchPurchase())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Pwt</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalScratchPwt())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Driect Player Pwt For Scratch</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(boDirPlrPwtForScratch)
						+ "</td>" + "</tr>";
			}
			if (isCS) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Commercial Services</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Sale</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalCSsale()) + "</td>" + "</tr>";
			}
			if (isOLA) {
				mail.boTextBody += "<tr><td align='left' width='200'><B>Scratch Game</B></td></tr>"
						+ "<tr>"
						+ "<td width='200'>Net Deposit</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalOLADeposit())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Withdrawal</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalOLAWithdraw())
						+ "</td>"
						+ "</tr>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'>Net Gamming</td>"
						+ "<td align='right' width='150'>"
						+ BodyText.INR
						+ FormatNumber.formatNumber(emailRepBean
								.getTotalOLANetGamming()) + "</td>" + "</tr>";
			}
			mail.boTextBody += "<tr>"
					+ "<td width='200'><B>Net Payment</B></td>"
					+ "<td align='right' width='150'>" + BodyText.INR
					+ FormatNumber.formatNumber(emailRepBean.getNetPayment())
					+ "</td>" + "</tr>" + "<tr>"
					+ "<td width='200'><B>Total Collections</B></td>"
					+ "<td align='right' width='150'>" + BodyText.INR
					+ FormatNumber.formatNumber(totalCollection) + "</td>"
					+ "</tr>";
			if ("Current Day".equalsIgnoreCase(reportType)) {
				mail.boTextBody += "<tr>"
						+ "<td width='200'><B>Active Agents</B></td>"
						+ "<td align='right' width='150'>"
						+ FormatNumber.formatNumber(this.getActiveAgentCount())
						+ "</td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td width='200'><B>Agents logged in yesterday</B></td>"
						+ "<td align='right' width='150'>"
						+ FormatNumber.formatNumber(emailRepBean
								.getLoggedInUsers()) + "</td>" + "</tr>";
			}
			mail.boTextBody += "</table>";

			// Section for table of last day collections from each Agent
			if ("Current Day".equalsIgnoreCase(reportType)) {
				mail.boTextBody += "<br><br><b> Last Day Collections </b>";
				mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";
				mail.boTextBody += "<tr>" + "<th> Agent Name </th>"
						+ "<th>Total Collections</th>";
				if (resultMap != null) {
					for (Map.Entry<String, CollectionReportOverAllBean> temp : resultMap
							.entrySet()) {
						mail.boTextBody += "<tr>" + "<td>"
								+ temp.getValue().getAgentName() + "</td>"
								+ "<td>" + temp.getValue().getOpeningBal()
								+ "</td>" + "</tr>";
					}
				}
				mail.boTextBody += "</table>";
			}
			logger.debug(mail.boTextBody);
			addBORecipient(mail, type); // add the recipient's mail id into list
			MailSender mailSender = new MailSender(FROM, PASSWORD, mail.to,
					mail.subject, mail.boTextBody, mail.BO_FILE_NAME); // sending
			// the
			// mail
			mailSender.setDaemon(true);
			mailSender.start();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

	}

	public MailBean sendDrawReportMail(Timestamp startDate, Timestamp endDate)
			throws LMSException, Exception {
		MailBean mail = new MailBean();
		mail.subject = "Daily Draw Report Of "
				+ startDate.toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4);
		mail.boTextBody = "";
		mail.BO_FILE_NAME = BodyText.DAILY_FILE;
		List<Integer> gameNoList = Util.getGameNumberList();
		DrawGameMgmtHelper dgMgmtHelper = new DrawGameMgmtHelper();
		this.con = new DBConnect().getConnection();
		mail.boTextBody += "<table cellpadding='2' border='2' bordercolor='black' cellspacing='0'>";
		mail.boTextBody += "<tr><th colspan=\"11\">" + mail.subject
				+ "</th></tr>";
		mail.boTextBody += "<tr>";
		mail.boTextBody += "<th>Game</th>";
		mail.boTextBody += "<th>Draw Date-Time</th>";
		mail.boTextBody += "<th>Status</th>";
		mail.boTextBody += "<th>Winning Result</th>";
		mail.boTextBody += "<th>No. OF Tickets</th>";
		mail.boTextBody += "<th>Sale Value</th>";
		mail.boTextBody += "<th>No. of Winning Tickets</th>";
		mail.boTextBody += "<th>Winning Amount</th>";
		mail.boTextBody += "</tr>";
		for (Integer gameNo : gameNoList) {
			DrawDataBean drawBean = new DrawDataBean();
			drawBean.setFromDate(startDate.toString());
			drawBean.setToDate(endDate.toString());
			drawBean.setGameNo(gameNo);
			drawBean.setGameName(Util.getGameDisplayName(gameNo));
			ReportBeanDrawModule repData = dgMgmtHelper.fetchDrawData(drawBean,
					(String) LMSUtility.sc.getAttribute("raffle_ticket_type"));
			List<ReportDrawBean> drawList = repData.getRepGameBean()
					.getRepDrawBean();
			for (ReportDrawBean drwBean : drawList) {
				mail.boTextBody += "<tr>";
				mail.boTextBody += "<td>" + drawBean.getGameName() + "</td>";
				mail.boTextBody += "<td>" + drwBean.getDrawDateTime() + "</td>";
				mail.boTextBody += "<td>" + drwBean.getDrawStatus() + "</td>";
				mail.boTextBody += "<td>"
						+ (drwBean.getWinningResult() == null ? "N.A."
								: drwBean.getWinningResult()) + "</td>";
				mail.boTextBody += "<td>" + drwBean.getTotalNoTickets()
						+ "</td>";
				mail.boTextBody += "<td>" + drwBean.getTotalSaleValue()
						+ "</td>";
				mail.boTextBody += "<td>" + drwBean.getTotalWinningTickets()
						+ "</td>";
				mail.boTextBody += "<td>" + drwBean.getTotalWinningAmount()
						+ "</td>";
				mail.boTextBody += "</tr>";
			}
		}
		mail.boTextBody += "</table>";
		return mail;
	}

	public MailBean sendScratchSalePwtReport(Timestamp startDate,
			Timestamp endDate) throws LMSException, Exception {
		MailBean mail = new MailBean();
		mail.subject = "Daily Scratch Sale Pwt Report Of "
				+ startDate.toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4);
		mail.boTextBody = "";
		mail.BO_FILE_NAME = BodyText.DAILY_FILE;
		this.con = new DBConnect().getConnection();
		IScratchSaleReportHelper scrSale = null;
		IScratchPwtReportHelper scrPwt = null;
		if (LMSFilterDispatcher.isRepFrmSP) {
			scrSale = new ScratchSaleReportHelperSP();
			scrPwt = new ScratchPwtReportHelperSP();
		} else {
			scrSale = new ScratchSaleReportHelper();
			scrPwt = new ScratchPwtReportHelper();

		}
		List<SalePwtReportsBean> scrSaleList = scrSale.scratchSaleGameWise(
				startDate, endDate);
		List<SalePwtReportsBean> scrPwtList = scrPwt.scratchPwtGameWise(
				startDate, endDate);
		Map<Integer, String> gameMap = Util.fetchActiveScratchGameList();
		mail.boTextBody += "<table cellpadding='2' border='2' bordercolor='black' cellspacing='0'>";
		mail.boTextBody += "<tr><th colspan=\"3\">" + mail.subject
				+ "</th></tr>";
		mail.boTextBody += "<tr>";
		mail.boTextBody += "<th>Game</th>";
		mail.boTextBody += "<th>Sale</th>";
		mail.boTextBody += "<th>Pwt</th>";
		mail.boTextBody += "</tr>";
		for (int gameNo : gameMap.keySet()) {
			boolean sFlag = false;
			boolean pFlag = false;
			mail.boTextBody += "<tr>";
			mail.boTextBody += "<td>" + gameMap.get(gameNo) + "</td>";
			if (scrSaleList.size() == 0) {
				mail.boTextBody += "<td>0.00</td>";
				sFlag = true;
			}
			if (scrPwtList.size() == 0) {
				mail.boTextBody += "<td>0.00</td>";
				pFlag = true;
			}
			for (SalePwtReportsBean sBean : scrSaleList) {
				if (sBean.getGameNo() == gameNo) {
					mail.boTextBody += "<td>" + sBean.getSaleMrpAmt() + "</td>";
					sFlag = true;
				}
			}
			for (SalePwtReportsBean pBean : scrPwtList) {
				if (pBean.getGameNo() == gameNo) {
					mail.boTextBody += "<td>" + pBean.getPwtMrpAmt() + "</td>";
					pFlag = true;
				}
			}
			if (!sFlag) {
				mail.boTextBody += "<td>0.00</td>";
			}
			if (!pFlag) {
				mail.boTextBody += "<td>0.00</td>";
			}
			mail.boTextBody += "</tr>";
		}
		mail.boTextBody += "</table>";
		return mail;
	}

	public MailBean sendDrawSalePwtReport(Timestamp startDate, Timestamp endDate)
			throws LMSException, Exception {
		MailBean mail = new MailBean();

		mail.subject = "Daily Draw Sale Pwt Report Of "
				+ startDate.toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4);
		mail.boTextBody = "";
		mail.BO_FILE_NAME = BodyText.DAILY_FILE;
		this.con = DBConnect.getConnection();
		IDrawSaleReportHelper drwSale = null;
		IDrawPwtReportHelper drwPwt = null;

		if (LMSFilterDispatcher.isRepFrmSP) {
			drwSale = new DrawSaleReportHelperSP();
			drwPwt = new DrawPwtReportHelperSP();
		} else {
			drwSale = new DrawSaleReportHelper();
			drwPwt = new DrawPwtReportHelper();
		}
		List<SalePwtReportsBean> drwSaleList = drwSale.drawSaleGameWise(
				startDate, endDate);
		List<SalePwtReportsBean> drwPwtList = drwPwt.drawPwtGameWise(startDate,
				endDate);
		Map<Integer, SalePwtReportsBean> map = new TreeMap<Integer, SalePwtReportsBean>();
		List<Integer> gameNoList = Util.getGameNumberList();
		for (int gno : gameNoList) {
			SalePwtReportsBean tempBean = new SalePwtReportsBean();
			map.put(gno, tempBean);
		}
		for (SalePwtReportsBean sBean : drwSaleList) {
			map.get(sBean.getGameNo()).setSaleMrpAmt(sBean.getSaleMrpAmt());
		}
		for (SalePwtReportsBean pBean : drwPwtList) {
			map.get(pBean.getGameNo()).setPwtMrpAmt(pBean.getPwtMrpAmt());
		}
		mail.boTextBody += "<table cellpadding='2' border='2' bordercolor='black' cellspacing='0'>";
		mail.boTextBody += "<tr><th colspan=\"3\">" + mail.subject
				+ "</th></tr>";
		mail.boTextBody += "<tr>";
		mail.boTextBody += "<th>Game</th>";
		mail.boTextBody += "<th>Sale</th>";
		mail.boTextBody += "<th>Pwt</th>";
		mail.boTextBody += "</tr>";
		for (int gameNo : gameNoList) {
			mail.boTextBody += "<tr>";
			mail.boTextBody += "<td>" + Util.getGameDisplayName(gameNo)
					+ "</td>";
			mail.boTextBody += "<td>" + map.get(gameNo).getSaleMrpAmt()
					+ "</td>";
			mail.boTextBody += "<td>" + map.get(gameNo).getPwtMrpAmt()
					+ "</td>";
			mail.boTextBody += "</tr>";
		}
		mail.boTextBody += "</table>";
		return mail;
	}

	public MailBean sendRetActivityReport(Timestamp startDate, Timestamp endDate)
			throws LMSException, Exception {
		MailBean mail = new MailBean();
		mail.subject = "Retailer Activity Report Of "
				+ startDate.toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4);
		mail.boTextBody = "";
		mail.BO_FILE_NAME = BodyText.DAILY_FILE;
		DateBeans dBean = new DateBeans();
		dBean.setFirstdate(new java.sql.Date(startDate.getTime()));
		dBean.setLastdate(new java.sql.Date(endDate.getTime()));
		RetActivityReportHelper retHelper = new RetActivityReportHelper();
		List<String> histList = retHelper.fetchActRepHistoryForDrawGame(dBean);
		this.con = new DBConnect().getConnection();
		mail.boTextBody += "<table cellpadding='2' border='2' bordercolor='black' cellspacing='0'>";
		mail.boTextBody += "<tr><th colspan=\"4\">" + mail.subject
				+ "</th></tr>";
		mail.boTextBody += "<tr>";
		mail.boTextBody += "<th>Live Retailers</th>";
		mail.boTextBody += "<th>No Sale Retailers</th>";
		mail.boTextBody += "<th>Inactive Retailers</th>";
		mail.boTextBody += "<th>Terminated Retailers</th>";
		mail.boTextBody += "</tr>";
		if (histList.size() > 0) {
			String[] histData = histList.get(0).split(",");
			mail.boTextBody += "<tr>";
			mail.boTextBody += "<td>" + histData[2] + "</td>";
			mail.boTextBody += "<td>" + histData[3] + "</td>";
			mail.boTextBody += "<td>" + histData[4] + "</td>";
			mail.boTextBody += "<td>" + histData[5] + "</td>";
			mail.boTextBody += "</tr>";
		} else {
			mail.boTextBody += "<tr>";
			mail.boTextBody += "<td colspan=\"4\">no records found for tha day</td>";
			mail.boTextBody += "</tr>";
		}
		mail.boTextBody += "</table>";
		return mail;
	}

	public MailBean sendCashCollectionReport() throws LMSException, Exception {
		MailBean mail = new MailBean();
		Calendar cd = Calendar.getInstance();
		cd.setTime(new java.util.Date());
		String dd = cd.get(Calendar.DAY_OF_MONTH) + "";
		String mm = cd.get(Calendar.MONTH) + "";
		String yy = cd.get(Calendar.YEAR) + "";
		if (dd.length() <= 1) {
			dd = "0" + dd;
		}
		if (mm.length() <= 1) {
			mm = "0" + mm;
		}
		mail.subject = "Cash Collection Report Of " + dd + "-" + mm + "-" + yy;
		mail.boTextBody = "";
		mail.BO_FILE_NAME = BodyText.DAILY_FILE;
		DateBeans dBean = new DateBeans();
		dBean.setFirstdate(new java.sql.Date(new Date().getTime()));
		dBean.setLastdate(new java.sql.Date(new Date().getTime() + 24 * 60 * 60
				* 1000));

		ICashChqReportsHelper cashChqHelper = null;

		if (LMSFilterDispatcher.isRepFrmSP) {
			cashChqHelper = new CashChqReportsHelperSP(dBean);
		} else {
			cashChqHelper = new CashChqReportsHelper(dBean);
		}

		List<Integer> agtOrgIdList = cashChqHelper.getAgentOrgList();
		List<CashChqReportBean> cashChqData = cashChqHelper.getCashChqDetail(
				agtOrgIdList, -1, false);
		mail.boTextBody += "<table cellpadding='2' border='2' bordercolor='black' cellspacing='0'>";
		mail.boTextBody += "<tr><th colspan=\"6\">" + mail.subject
				+ "</th></tr>";
		mail.boTextBody += "<tr>";
		mail.boTextBody += "<th>Name</th>";
		mail.boTextBody += "<th>Cash</th>";
		mail.boTextBody += "<th>Cheque</th>";
		mail.boTextBody += "<th>Cheque bounce</th>";
		mail.boTextBody += "<th>Credit</th>";
		mail.boTextBody += "<th>Debit</th>";
		mail.boTextBody += "<th>Bank Deposit</th>";
		mail.boTextBody += "</tr>";
		if (cashChqData.size() > 0) {
			for (CashChqReportBean cbean : cashChqData) {
				mail.boTextBody += "<tr>";
				mail.boTextBody += "<td>" + cbean.getName() + "</td>";
				mail.boTextBody += "<td>" + cbean.getTotalCash() + "</td>";
				mail.boTextBody += "<td>" + cbean.getTotalChq() + "</td>";
				mail.boTextBody += "<td>" + cbean.getCheqBounce() + "</td>";
				mail.boTextBody += "<td>" + cbean.getCredit() + "</td>";
				mail.boTextBody += "<td>" + cbean.getDebit() + "</td>";
				mail.boTextBody += "<td>" + cbean.getBankDeposit() + "</td>";
				mail.boTextBody += "</tr>";
			}
		} else {
			mail.boTextBody = "<tr><td>no records found for tha day<td></tr>";
		}
		mail.boTextBody += "</table>";
		return mail;
	}

	public void allDailyReportsCombined() throws LMSException, Exception {
		SendReportMailerMain srm = new SendReportMailerMain(new DateBeans());
		MailBean jointMail = new MailBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp startDate = new Timestamp(sdf.parse(
				sdf.format(dateBeans.getFirstdate())).getTime());
		Timestamp endDate = new Timestamp(sdf.parse(
				sdf.format(dateBeans.getLastdate())).getTime()
				- 24 * 60 * 60 * 1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.HOUR, 23);
		cal.add(Calendar.MINUTE, 59);
		cal.add(Calendar.SECOND, 59);
		endDate = new Timestamp(cal.getTime().getTime());
		String PASSWORD = MailBean.PASSWORD;
		String FROM = MailBean.DAILY_MAIL_FROM;
		this.con = new DBConnect().getConnection();
		jointMail.subject = "Daily Reports Of "
				+ startDate.toString().substring(8, 10) + '-'
				+ dateBeans.getFirstdate().toString().substring(5, 7) + '-'
				+ dateBeans.getFirstdate().toString().substring(0, 4);
		jointMail.boTextBody += "<table width=\"100%\"><tr><th align=\"center\">"
				+ jointMail.subject + "</th><tr></table>";
		jointMail.boTextBody += srm.sendDrawReportMail(startDate, endDate).boTextBody
				+ "<br/>"
				+ srm.sendDrawSalePwtReport(startDate, endDate).boTextBody
				+ "<br/>"
				+ srm.sendScratchSalePwtReport(startDate, endDate).boTextBody
				+ "<br/>"
				+ srm.sendRetActivityReport(startDate, endDate).boTextBody
				+ "<br/>" + srm.sendCashCollectionReport().boTextBody + "<br/>";
		addBORecipient(jointMail, "Daily");
		MailSender mailSender = new MailSender(FROM, PASSWORD, jointMail.to,
				jointMail.subject, jointMail.boTextBody, "");
		mailSender.setDaemon(true);
		mailSender.start();

	}

	public void dailyReport() throws LMSException {

		TreeMap<Integer, ArrayList<Double>> gameDataMap = new TreeMap<Integer, ArrayList<Double>>();

		Connection con = DBConnect.getConnection();
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		DrawDataBean drawDataBean = new DrawDataBean();

		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<Double> totAmt = null;
		int gameNum = 0;
		List<Integer> gameNumList = Util.getGameNumberList();
		int totalRet = 0;
		double totSale = 0;
		double totPwt = 0;
		int count = 0;

		String date = new java.sql.Date(new Date().getTime()) + "";

		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		drawDataBean.setFromDate(fromDate);
		drawDataBean.setToDate(toDate);
		String trxDate = "transaction_date>'" + fromDate
				+ "' and transaction_date<'" + toDate + "'";
		String trxQry = "select transaction_id from st_lms_retailer_transaction_master where "
				+ trxDate;

		String retQry = "select count(distinct(retailer_org_id)) retCount from st_lms_retailer_transaction_master where transaction_date>'"
				+ fromDate + "' and transaction_date<'" + toDate + "' ";
		StringBuilder retActiveRep = new StringBuilder(
				"<br/><table><tr><td><b>Total Active Retailers of the Day</b></td><td>");
		try {
			pstmt = con.prepareStatement(retQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalRet = rs.getInt("retCount");
			}
			retActiveRep.append(totalRet + "</td></tr></table>");

			String combinedQry = "select if(sale.res is null ,0,sale.res)+if(saleref.res is null ,0,saleref.res) as res from (select sum(mrp_amt) res from st_dg_ret_sale_? where transaction_id in ("
					+ trxQry
					+ ")) sale,(select sum(mrp_amt) res  from st_dg_ret_sale_refund_? where transaction_id in ("
					+ trxQry
					+ ")) saleref union all select if(pwt.res is null ,0,pwt.res)+pwtdir.dirPwt as res from (select sum(pwt_amt) res from st_dg_ret_pwt_? where transaction_id in ("
					+ trxQry
					+ ") ) pwt,( select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_dg_bo_direct_plr_pwt where "
					+ trxDate
					+ " and game_id=?) a,(select sum(pwt_amt) pwt_amt from st_dg_agt_direct_plr_pwt where "
					+ trxDate + "  and game_id=?) b) pwtdir";
			StringBuilder gamewiseRep = new StringBuilder(
					"<table width=\"50%\" border=\"1\" cellpadding=\"2\" cellspacing=\"0\"	bordercolor=\"#CCCCCC\" align=\"center\"><tr align=\"center\" style=\"font-weight:bold\"><td>Game Name</td><td>Sale</td><td>PWT</td></tr>");

			for (int i = 0; i < gameNumList.size(); i++) {
				gameNum = gameNumList.get(i);
				totAmt = new ArrayList<Double>();
				pstmt = con.prepareStatement(combinedQry);
				pstmt.setInt(1, gameNum);
				pstmt.setInt(2, gameNum);
				pstmt.setInt(3, gameNum);
				pstmt.setInt(4, gameNum);
				pstmt.setInt(5, gameNum);
				System.out.println(combinedQry);
				rs = pstmt.executeQuery();
				gamewiseRep.append("<tr  align=\"right\"><td>"
						+ Util.getGameDisplayName(gameNum) + "</td>");
				count = 0;
				while (rs.next()) {
					gamewiseRep.append("<td>" + rs.getDouble("res") + "</td>");
					totAmt.add(rs.getDouble("res"));
					if (count == 0) {
						totSale = totSale + rs.getDouble("res");
					} else {
						totPwt = totPwt + rs.getDouble("res");
					}
				}
				gamewiseRep.append("</tr>");
				gameDataMap.put(gameNum, totAmt);
			}
			gamewiseRep
					.append("<tr align=\"right\" style=\"font-weight:bold\"><td>Total</td><td>"
							+ totSale + "</td><td>" + totPwt + "</td></tr>");
			gamewiseRep.append("</table><br/>");
			StringBuilder gameRep = new StringBuilder(
					"<table border=\"1\" bordercolor=\"black\">");
			gameRep
					.append("<tr><td width=\"80%\" align=\"center\"><b>Draw Report of "
							+ date + "</b></td></tr>");
			System.out.println("***************" + gameNumList);
			gameRep.append("<tr><td>" + gamewiseRep + "</td></tr>");
			for (int i = 0; i < gameNumList.size(); i++) {
				gameRep.append("<tr><td width=\"80%\">");
				gameNum = gameNumList.get(i);
				gameRep
						.append("<table width=\"100%\" border=\"1\" cellpadding=\"2\" cellspacing=\"0\"	bordercolor=\"#CCCCCC\" align=\"center\"><tr align=\"center\" style=\"font-weight:bold\"><td colspan=7>"
								+ Util.getGameDisplayName(gameNum)
								+ "</td></tr>");
				gameRep
						.append("<tr align=\"center\" style=\"font-weight:bold\"><td>Draw Date</td><td>Draw Status</td><td>Total Tickets</td><td>Total Sale</td><td>Total Winning</td><td>Avg. Per Tkt Cost</td><td>Avg. Sale Per Ret</td></tr>");
				drawDataBean.setGameNo(gameNum);
				List<ReportDrawBean> l = helper.fetchDrawData(drawDataBean,
						raffleTktType).getRepGameBean().getRepDrawBean();
				System.out.println("***************" + l);
				for (int j = 0; j < l.size(); j++) {
					ReportDrawBean rdb = l.get(j);
					gameRep
							.append("<tr align=\"right\"><td>"
									+ rdb.getDrawDateTime()
									+ "</td><td>"
									+ rdb.getDrawStatus()
									+ "</td><td>"
									+ rdb.getTotalNoTickets()
									+ "</td><td>"
									+ rdb.getTotalSaleValue()
									+ "</td><td>"
									+ rdb.getTotalWinningAmount()
									+ "</td><td>"
									+ (rdb.getTotalSaleValue() > 0 ? (rdb
											.getTotalSaleValue() / rdb
											.getTotalNoTickets()) : rdb
											.getTotalSaleValue())
									+ "</td><td>"
									+ (totalRet > 0 ? (rdb.getTotalSaleValue() / totalRet)
											: "N.A.") + "</td></tr>");
				}
				gameRep.append("</table></td></tr>");
			}
			gameRep.append("<tr align=\"center\"><td>" + retActiveRep
					+ "</td></tr>");
			gameRep.append("</table><br/>");

			System.out.println(gameRep);
			MailBean mail = new MailBean();
			// SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
			String FROM = null;
			String PASSWORD = MailBean.PASSWORD;
			String type = "";

			mail.subject = BodyText.DAILY_SUBJECT;
			FROM = MailBean.DAILY_MAIL_FROM;
			type = "Daily";

			addBORecipient(mail, type);

			// sending the mail
			MailSender mailSender = new MailSender(FROM, PASSWORD, mail.to,
					mail.subject, gameRep.toString(), mail.BO_FILE_NAME);
			mailSender.setDaemon(true);
			mailSender.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Daily Draw Report Not Send");
		}
	}

	public String sendMailAfterDrawPerform(
			List<ReportDrawBean> mailPerformedDraw) throws LMSException {
		MailBean mail = new MailBean();
		mail.subject = "Draw Details";
		mail.boTextBody = "Details Of Recently Performed Draws";
		String FROM = MailBean.DAILY_MAIL_FROM;
		String PASSWORD = MailBean.PASSWORD;
		String type = "Performed";
		String status = "Mail Sent";
		File fileObj = null;

		Iterator<ReportDrawBean> mailPerformedDrawListIterator = mailPerformedDraw
				.iterator();

		mail.boTextBody += "<br><br><br><b>Details of Performed Draws </b>";
		mail.boTextBody += "<br><br> <table cellpadding='8' border='2' bordercolor='red' cellspacing='0'>";

		int count = 0;
		ReportDrawBean bean = null;
		while (mailPerformedDrawListIterator.hasNext()) {
			bean = mailPerformedDrawListIterator.next();
			count++;

			mail.boTextBody += "<tr><td align='left' width='200'><B>" + count
					+ "</B></td></tr>" + "<tr>"
					+ "<td width='200'>Draw Id</td>"
					+ "<td width='200'>Draw Name</td>"
					+ "<td width='200'>Date Time</td>"
					+ "<td width='200'>Draw Day</td>"
					+ "<td width='200'>Winning Result</td>"
					+ "<td width='200'>Draw Status</td>"
					+ "<td width='200'>Sold Tickets</td>"
					+ "<td width='200'>Sale Amount</td>"
					+ "<td width='200'>Winning Tickets</td>"
					+ "<td width='200'>Winning Amount</td></tr>"
					+ "<tr><td align='right' width='150'>" + bean.getDrawId()
					+ "</td><td align='right' width='150'>"
					+ bean.getDrawName()
					+ "</td><td align='right' width='150'>"
					+ bean.getDrawDateTime()
					+ "</td><td align='right' width='150'>" + bean.getDrawDay()
					+ "</td><td align='right' width='150'>"
					+ bean.getWinningResult()
					+ "</td><td align='right' width='150'>"
					+ bean.getDrawStatus()
					+ "</td><td align='right' width='150'>"
					+ bean.getTotalNoTickets()
					+ "</td><td align='right' width='150'>"
					+ bean.getTotalSaleValue()
					+ "</td><td align='right' width='150'>"
					+ bean.getTotalWinningTickets()
					+ "</td><td align='right' width='150'>"
					+ bean.getTotalWinningAmount() + "</td></tr>";
		}
		
		fileObj = new PPRSheetHelper().preparePPRSheet(mailPerformedDraw, Utility.getPropertyValue("COUNTRY_DEPLOYED") + ".xls");
		
		this.con = DBConnect.getConnection();
		addBORecipient(mail, type);
//		mail.to.add("dushyant.sapra@skilrock.com");
		try {
			MailSender mailSender = new MailSender(FROM, PASSWORD, mail.to,
					mail.subject, mail.boTextBody, fileObj, "PPR_" + Utility.getPropertyValue("COUNTRY_DEPLOYED") + ".xls");
			mailSender.setDaemon(true);
			mailSender.start();
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			status = "Not Sent";
		}
		return status;
	}

	public String sendMailAfterTrackTicketLimitReached(String userOrgName) throws LMSException {
		MailBean mail = new MailBean();
		mail.subject = "Track Ticket Limit Reached";
		mail.boTextBody = "User "+userOrgName+" Reached Maximum Attempt Limit for Tracking Tickets.";
		//String FROM = MailBean.DAILY_MAIL_FROM;
		String FROM = "lms.user@skilrock.com";
		String PASSWORD = MailBean.PASSWORD;
		String type = "TrackTicketLimit";
		String status = null;
		MailSender mailSender = null;
		try {
			status = "Mail Sent";
			con = DBConnect.getConnection();
			addBORecipient(mail, type);
			mailSender = new MailSender(FROM, PASSWORD, mail.to, mail.subject, mail.boTextBody, "");
			mailSender.setDaemon(true);
			mailSender.start();
		} catch (Exception e) {
			e.printStackTrace();
			status = "Not Sent";
		} finally {
			DBConnect.closeCon(con);
		}

		return status;
	}
}