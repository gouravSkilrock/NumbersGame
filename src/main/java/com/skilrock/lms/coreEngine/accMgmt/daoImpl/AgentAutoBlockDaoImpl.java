package com.skilrock.lms.coreEngine.accMgmt.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.AgentAutoBlockBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportRequestBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.beans.IWOrgReportResponseBean;
import com.skilrock.lms.web.instantWin.reportsMgmt.controller.IWAgentReportsControllerImpl;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.controller.SLEAgentReportsControllerImpl;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.controller.VSAgentReportsControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.OlaAgentReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;

public class AgentAutoBlockDaoImpl {
	private static Log logger = LogFactory.getLog(AgentAutoBlockDaoImpl.class);
	private static AgentAutoBlockDaoImpl instance = null;

	private AgentAutoBlockDaoImpl() {
	}

	public static AgentAutoBlockDaoImpl getInstance() {
		if (instance == null) {
			instance = new AgentAutoBlockDaoImpl();
		}
		return instance;
	}

	public Map<Integer, AgentAutoBlockBean> getAgentLimitList(boolean allowBlock, Connection connection) throws LMSException {
		Map<Integer, AgentAutoBlockBean> agentLimitMap = new LinkedHashMap<Integer, AgentAutoBlockBean>();
		AgentAutoBlockBean agentLimitBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String status = (allowBlock) ? "'ACTIVE','INACTIVE','BLOCK'" : "'ACTIVE','INACTIVE'";

			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE ol.organization_id, user_id, name, om.organization_status, um.status, block_amt, block_days, block_action FROM st_lms_oranization_limits ol INNER JOIN st_lms_organization_master om ON ol.organization_id=om.organization_id INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_type='AGENT' AND om.organization_status IN ("+status+") AND isrolehead='Y' ORDER BY NAME;";
			logger.info("getAgentLimitList Query - "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				agentLimitBean = new AgentAutoBlockBean();
				int orgId = rs.getInt("organization_id");
				agentLimitBean.setOrgId(orgId);
				agentLimitBean.setUserId(rs.getInt("user_id"));
				agentLimitBean.setOrgName(rs.getString("name"));
				agentLimitBean.setOrgStatus(rs.getString("organization_status"));
				agentLimitBean.setUserStatus(rs.getString("status"));
				agentLimitBean.setBlockAmount(rs.getDouble("block_amt"));
				agentLimitBean.setBlockDays(rs.getInt("block_days"));
				agentLimitBean.setBlockAction(rs.getString("block_action"));
				agentLimitMap.put(orgId, agentLimitBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return agentLimitMap;
	}

	public List<String> getHolidayList(Connection connection) throws LMSException {
		SimpleDateFormat dateFormat = null;
		List<String> holidayList = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			dateFormat = new SimpleDateFormat(Utility.getPropertyValue("date_format"));

			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE holiday_date FROM st_lms_holiday_master WHERE status='ACTIVE' ORDER BY holiday_date DESC;";
			logger.info("getHolidayList Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				holidayList.add(dateFormat.format(rs.getTimestamp("holiday_date")));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return holidayList;
	}

	public CollectionReportOverAllBean getCBForPayment(int agentOrgId, Timestamp startDate, Timestamp endDate, Connection connection) throws LMSException {
		CollectionReportOverAllBean reportBean = new CollectionReportOverAllBean();
		Statement stmt = null;
		CallableStatement cstmt = null;
		ResultSet gameRs = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();

			if (ReportUtility.isDG) {
				String gameQuery = "SELECT SQL_CACHE game_id FROM st_dg_game_master;";
				logger.info("gameQuery DG - "+gameQuery);
				gameRs = stmt.executeQuery(gameQuery);
				while(gameRs.next()) {
					int gameId = gameRs.getInt("game_id");
					cstmt = connection.prepareCall("call saleCancelPaymentDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						reportBean.setDgSale(reportBean.getDgSale() + rs.getDouble("sale"));
						reportBean.setDgCancel(reportBean.getDgCancel() + rs.getDouble("cancel"));
					}

					cstmt = connection.prepareCall("call pwtPaymentDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						reportBean.setDgPwt(reportBean.getDgPwt() + rs.getDouble("pwt"));
					}
				}

				cstmt = connection.prepareCall("call DirPlayerpwtPaymentDateWisePerGame(?,?,?)");
				cstmt.setTimestamp(1, startDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, agentOrgId);
				rs = cstmt.executeQuery();
				while (rs.next()) {
					reportBean.setDgDirPlyPwt(reportBean.getDgDirPlyPwt() + rs.getDouble("pwtDir"));
				}
			}

			if (ReportUtility.isSE) {
				String gameQuery = "SELECT SQL_CACHE game_id FROM st_se_game_master;";
				logger.info("gameQuery SE - "+gameQuery);
				gameRs = stmt.executeQuery(gameQuery);
				while(gameRs.next()) {
					int gameId = gameRs.getInt("game_id");
					cstmt = connection.prepareCall("call scratchSaleCancelDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
						while (rs.next()) {
							reportBean.setSeSale(reportBean.getSeSale() + rs.getDouble("sale"));
							reportBean.setSeCancel(reportBean.getSeCancel() + rs.getDouble("cancel"));
						}
					} else if ("TICKET_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
						reportBean.setSeSale(reportBean.getSeSale() + rs.getDouble("sale"));
					}

					cstmt = connection.prepareCall("call scratchPwtDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						reportBean.setSePwt(reportBean.getSePwt() + rs.getDouble("pwt"));
					}

					cstmt = connection.prepareCall("call scratchDirPwtPlayerDateWisePerGame(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						reportBean.setSeDirPlyPwt(reportBean.getSeDirPlyPwt() + rs.getDouble("pwtDir"));
					}
				}
			}

			if (ReportUtility.isCS) {
				String gameQuery = "SELECT SQL_CACHE category_id FROM st_cs_product_category_master;";
				logger.info("gameQuery CS - "+gameQuery);
				gameRs = stmt.executeQuery(gameQuery);
				while(gameRs.next()) {
					int catId = gameRs.getInt("category_id");
					cstmt = connection.prepareCall("call csCollectionDateWisePerCategory(?,?,?,?)");
					cstmt.setTimestamp(1, startDate);
					cstmt.setTimestamp(2, endDate);
					cstmt.setInt(3, catId);
					cstmt.setInt(4, agentOrgId);
					rs = cstmt.executeQuery();
					while (rs.next()) {
						reportBean.setCSSale(reportBean.getCSSale() + rs.getDouble("sale"));
						reportBean.setCSCancel(reportBean.getCSCancel() + rs.getDouble("cancel"));
					}
				}
			}

			if (ReportUtility.isSLE) {
				SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
				requestBean.setOrgId(agentOrgId);
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				Map<String, SLEOrgReportResponseBean> sleResponseBeanMap = SLEAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, connection);

				for (Map.Entry<String, SLEOrgReportResponseBean> entry : sleResponseBeanMap.entrySet()) {
					SLEOrgReportResponseBean sleResponseBean = entry.getValue();
					reportBean.setSleSale(reportBean.getSleSale() + sleResponseBean.getSaleAmt());
					reportBean.setSleCancel(reportBean.getSleCancel() + sleResponseBean.getCancelAmt());
					reportBean.setSlePwt(reportBean.getSlePwt() + sleResponseBean.getPwtAmt());
					reportBean.setSleDirPlyPwt(reportBean.getSleDirPlyPwt() + sleResponseBean.getPwtDirAmt());
				}
			}

			if (ReportUtility.isIW) {
				IWOrgReportRequestBean requestBean = new IWOrgReportRequestBean();
				requestBean.setOrgId(agentOrgId);
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				//Map<String, IWOrgReportResponseBean> iwResponseBeanMap = IWAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, connection);
				IWOrgReportResponseBean iwResponseBean = IWAgentReportsControllerImpl.fetchSaleCancelPWTSingleAgentAllGame(requestBean, connection);

				reportBean.setIwSale(reportBean.getIwSale() + iwResponseBean.getSaleAmt());
				reportBean.setIwCancel(reportBean.getIwCancel() + iwResponseBean.getCancelAmt());
				reportBean.setIwPwt(reportBean.getIwPwt() + iwResponseBean.getPwtAmt());
				reportBean.setIwDirPlyPwt(reportBean.getIwDirPlyPwt() + iwResponseBean.getPwtDirAmt());
			}

			if (ReportUtility.isOLA) {
				OlaOrgReportRequestBean requestBean = new OlaOrgReportRequestBean();
				requestBean.setFromDate(startDate.toString());
				requestBean.setToDate(endDate.toString());
				requestBean.setOrgId(agentOrgId);
				Map<String, OlaOrgReportResponseBean> olaResponseBeanMap = OlaAgentReportControllerImpl.fetchDepositWithdrawlSingleAgentDateWise(requestBean, connection);
				for (Map.Entry<String, OlaOrgReportResponseBean> entry : olaResponseBeanMap.entrySet()) {
					OlaOrgReportResponseBean olaResponseBean = entry.getValue();
					reportBean.setWithdrawal(reportBean.getWithdrawal() + olaResponseBean.getNetWithdrawalAmt());
					reportBean.setDeposit(reportBean.getDeposit() + olaResponseBean.getNetDepositAmt());
				}
			}

			if (ReportUtility.isVS) {
				VSOrgReportRequestBean requestBean = new VSOrgReportRequestBean();
				requestBean.setOrgId(agentOrgId);
				requestBean.setFromDate(startDate);
				requestBean.setToDate(endDate);
				Map<String, VSOrgReportResponseBean> vsResponseBeanMap = VSAgentReportsControllerImpl.fetchSalePWTDayWiseForAgent(requestBean, connection);

				for (Map.Entry<String, VSOrgReportResponseBean> entry : vsResponseBeanMap.entrySet()) {
					VSOrgReportResponseBean vsResponseBean = entry.getValue();
					reportBean.setVsSale(reportBean.getVsSale() + vsResponseBean.getSaleAmt());
					reportBean.setVsCancel(reportBean.getVsCancel() + vsResponseBean.getCancelAmt());
					reportBean.setVsPwt(reportBean.getVsPwt() + vsResponseBean.getPwtAmt());
					reportBean.setVsDirPlyPwt(reportBean.getVsDirPlyPwt() + vsResponseBean.getPwtDirAmt());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeCstmt(cstmt);
			DBConnect.closeRs(gameRs);
			DBConnect.closeRs(rs);
		}

		return reportBean;
	}
}