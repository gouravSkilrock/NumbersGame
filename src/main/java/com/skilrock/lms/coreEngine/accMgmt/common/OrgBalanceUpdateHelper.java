package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.AgentCollectionReportOverAllBean;
import com.skilrock.lms.beans.OrganizationBalanceData;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.accMgmt.common.AgentOpeningBalanceHelper;
import com.skilrock.lms.web.accMgmt.common.RetailerOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class OrgBalanceUpdateHelper {
	private static final Logger logger = LoggerFactory.getLogger(OrgBalanceUpdateHelper.class);

	public void inserOpeningBalForAgentAndRetailer() throws LMSException {
		Connection con = null;
		Calendar from  = Calendar.getInstance();

		Calendar yesterday  = Calendar.getInstance();
		Calendar to = Calendar.getInstance();

		Calendar tmpFrom = Calendar.getInstance();
//		Calendar tmpTo = Calendar.getInstance();

		int diff = 0, prop = 0;
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		try {
			con = DBConnect.getConnection();
			String lastRunDate = ReportUtility.fetchLastRunDate(con);
			logger.info("Last Run Date is {}", lastRunDate);
			if (lastRunDate != null) {
				from.setTimeInMillis(new SimpleDateFormat("dd-MM-yyyy").parse(lastRunDate).getTime());
				diff = (int) TimeUnit.DAYS.convert(yesterday.getTimeInMillis() - from.getTimeInMillis(), TimeUnit.MILLISECONDS);

				prop = Integer.parseInt(Utility.getPropertyValue("OPENING_BAL_SCHEDULER_RUN_DAY_COUNT"));

				Integer[] durationArray = new Integer[(diff / prop) + (diff % prop == 0 ? 0 : 1)];

				if (diff <= prop) {
					durationArray[0] = diff;
				} else {
					int count = diff / prop;
					int iLoop = 0;
					for (; iLoop < count; iLoop++) {
						durationArray[iLoop] = prop;
					}
					
					if((diff % prop) > 0) {
						durationArray[iLoop] = diff % prop; 
					}
				}

				logger.info("Duration to run Opening Bal Scheduler {}", Arrays.toString(durationArray));

				from.add(Calendar.DAY_OF_MONTH, 1);
				for (Integer iValue : durationArray) {
					if (iValue != null && iValue != 0) {
						to.setTimeInMillis(from.getTimeInMillis());
						to.add(Calendar.DAY_OF_MONTH, iValue);

						tmpFrom.setTime(from.getTime());

						con.setAutoCommit(false);
						logger.info("Checking CL and XCL Scheduler Last Run Date");
						to.add(Calendar.DAY_OF_MONTH, -1);
						if (!checkCLXCLSchedulerLastRunDate(to, con)) {
							logger.info("Run CL and XCl Scheduler First");
							throw new LMSException("Please Run the CL/XCL Scheduler First.");
						}
						to.add(Calendar.DAY_OF_MONTH, 1);
						logger.info("Starting Org Balance Update For Agent");
						insertOrgBalance(tmpFrom, to, con);
						logger.info("Org Balance Update For Agent Completed");

						tmpFrom.setTime(from.getTime());

						logger.info("Starting Org Balance Update For Reailers");
						insertRetailerOrgBalance(tmpFrom, to, con);
						logger.info("Org Balance Update For Retailer Completed");
						con.commit();
						from.setTime(to.getTime());
					}
				}
			} else {
				logger.info("Last Run Fetch Error, Returning");
				return;
			}
		} catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public boolean checkCLXCLSchedulerLastRunDate(Calendar compTo, Connection con) {
		boolean isRun = false;

		Statement stmt = null;
		ResultSet rs = null;
		Date clXclLastRunDate = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select date_format(last_success_time, '%d-%m-%Y') date_time from st_lms_scheduler_master where dev_name = 'Auto_Quartz_SCHEDULER';");

			while (rs.next()) {
				clXclLastRunDate = new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("date_time"));
			}
			logger.info("CL And XCl Scheduler Last Run Date is {}", clXclLastRunDate);

			if (clXclLastRunDate.getTime() >= compTo.getTimeInMillis()) {
				isRun = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isRun;
	}

	private void setDefaultValueForRetailerOrgBalacnceBean(Map<String, AgentCollectionReportOverAllBean> agtMap) {
		AgentCollectionReportOverAllBean collBean = null;
		for (Map.Entry<String, AgentCollectionReportOverAllBean> entrySet : agtMap.entrySet()) {
			collBean = entrySet.getValue();
			collBean.setOpeningBal(0.0);
			collBean.setLiveOpeningBal(0.0);
			collBean.setClLimit(0.0);
			collBean.setXclLimit(0.0);
			collBean.setNetTxnAmt(0.0);
			collBean.setCash(0.0);
			collBean.setCheque(0.0);
			collBean.setChequeReturn(0.0);
			collBean.setCredit(0.0);
			collBean.setDebit(0.0);
			collBean.setBankDep(0.0);
			if (ReportUtility.isDG) {
				collBean.setDgSale(0.0);
				collBean.setDgPwt(0.0);
				collBean.setDgCancel(0.0);
				collBean.setDgDirPlyPwt(0.0);
			}
			if (ReportUtility.isSE) {
				collBean.setSeSale(0.0);
				collBean.setSePwt(0.0);
				collBean.setSeDirPlyPwt(0.0);
			}
			if (ReportUtility.isCS) {
				collBean.setCSSale(0.0);
				collBean.setCSCancel(0.0);
			}
			if (ReportUtility.isOLA) {
				collBean.setDeposit(0.0);
				collBean.setDepositRefund(0.0);
				collBean.setWithdrawal(0.0);
				collBean.setWithdrawalRefund(0.0);
				collBean.setNetGamingComm(0.0);
			}
		}
	}

	//Change to Agent
	private void setDefaultValueForOrgBalacnceBean(Map<String, OrganizationBalanceData> agtMap) {
		OrganizationBalanceData orgBalanceDataBean = null;
		for (Map.Entry<String, OrganizationBalanceData> entrySet : agtMap.entrySet()) {
			orgBalanceDataBean = entrySet.getValue();
			orgBalanceDataBean.setOpeningBal(0.0);
			orgBalanceDataBean.setLiveOpeningBal(0.0);
			orgBalanceDataBean.setClLimit(0.0);
			orgBalanceDataBean.setXclLimit(0.0);
			orgBalanceDataBean.setNetTxnAmt(0.0);
			orgBalanceDataBean.setCash(0.0);
			orgBalanceDataBean.setCheque(0.0);
			orgBalanceDataBean.setChequeReturn(0.0);
			orgBalanceDataBean.setCredit(0.0);
			orgBalanceDataBean.setDebit(0.0);
			orgBalanceDataBean.setBankDep(0.0);
			if (ReportUtility.isDG) {
				orgBalanceDataBean.setDgSale(0.0);
				orgBalanceDataBean.setDgPwt(0.0);
				orgBalanceDataBean.setDgCancel(0.0);
				orgBalanceDataBean.setDgDirPlyPwt(0.0);
			}
			if (ReportUtility.isSE) {
				orgBalanceDataBean.setSeSale(0.0);
				orgBalanceDataBean.setSePwt(0.0);
				orgBalanceDataBean.setSeDirPlyPwt(0.0);
			}
			if (ReportUtility.isCS) {
				orgBalanceDataBean.setCSSale(0.0);
				orgBalanceDataBean.setCSCancel(0.0);
			}
			if (ReportUtility.isOLA) {
				orgBalanceDataBean.setDeposit(0.0);
				orgBalanceDataBean.setDepositRefund(0.0);
				orgBalanceDataBean.setWithdrawal(0.0);
				orgBalanceDataBean.setWithdrawalRefund(0.0);
				orgBalanceDataBean.setNetGamingComm(0.0);
			}
		}
	}

	public void insertRetailerOrgBalance(Calendar from, Calendar to, Connection con) throws LMSException {
		Statement stmt = null;
		Statement stmtRetailer = null;
		ResultSet rs = null;
		ResultSet rsRetailer = null;

		Map<String, AgentCollectionReportOverAllBean> agtMap = null;
		AgentCollectionReportOverAllBean collBean = null;
		
		Calendar tmpFrom = Calendar.getInstance();
		Calendar tmpTo = Calendar.getInstance();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select organization_id from st_lms_organization_master where organization_type = 'AGENT'");
			stmtRetailer = con.createStatement();
			while (rs.next()) {
				rsRetailer = stmtRetailer.executeQuery("select name, organization_id from st_lms_organization_master where parent_id=" + rs.getInt("organization_id") + " order by name");
				agtMap = new LinkedHashMap<String, AgentCollectionReportOverAllBean>();
				while (rsRetailer.next()) {
					collBean = new AgentCollectionReportOverAllBean();
					collBean.setRetailerName(rsRetailer.getString("name"));
					collBean.setRetailerOrgId(rsRetailer.getInt("organization_id"));
					collBean.setOpeningBal(0.0);
					collBean.setCash(0.0);
					collBean.setCheque(0.0);
					collBean.setChequeReturn(0.0);
					collBean.setCredit(0.0);
					collBean.setDebit(0.0);
					collBean.setBankDep(0.0);
					if (ReportUtility.isDG) {
						collBean.setDgSale(0.0);
						collBean.setDgPwt(0.0);
						collBean.setDgCancel(0.0);
						collBean.setDgDirPlyPwt(0.0);
					}
					if (ReportUtility.isSE) {
						collBean.setSeSale(0.0);
						collBean.setSePwt(0.0);
						collBean.setSeDirPlyPwt(0.0);
					}
					if (ReportUtility.isCS) {
						collBean.setCSSale(0.0);
						collBean.setCSCancel(0.0);
					}
					if (ReportUtility.isOLA) {
						collBean.setDeposit(0.0);
						collBean.setDepositRefund(0.0);
						collBean.setWithdrawal(0.0);
						collBean.setWithdrawalRefund(0.0);
						collBean.setNetGamingComm(0.0);
					}
					agtMap.put(rsRetailer.getString("organization_id"), collBean);
				}
				tmpFrom.setTime(from.getTime());
				tmpTo.setTime(to.getTime());
				if (agtMap.size() > 0)
					insertRetailerOpeningBalance(tmpFrom, tmpTo, rs.getInt("organization_id"), agtMap, con);
				agtMap = null;
			}
		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void insertRetailerOpeningBalance(Calendar from, Calendar to, int agentOrgId, Map<String, AgentCollectionReportOverAllBean> agtMap, Connection con) throws LMSException {
		AgentCollectionReportOverAllBean organizationBalanceData = null;
		PreparedStatement pStmt = null;

		RetailerOpeningBalanceHelper helper = new RetailerOpeningBalanceHelper();
		try {
			pStmt = con.prepareStatement("insert into st_rep_org_bal_history(organization_id, organization_type, parent_id, finaldate, opening_bal, opening_bal_cl_inc, net_amount_transaction, cl, xcl) values(?, ?, ?, ?, ?, ?, ?, ?, ?);");
			for (from.getTime(); from.before(to); from.add(Calendar.DAY_OF_MONTH, 1)) {
				Calendar temp = Calendar.getInstance();
				temp.setTime(from.getTime());
				temp.add(Calendar.HOUR_OF_DAY, 23);
				temp.add(Calendar.MINUTE, 59);
				temp.add(Calendar.SECOND, 59);

				helper.collectionRetailerWiseOpenBalData(agentOrgId, new Timestamp(from.getTimeInMillis()), new Timestamp(temp.getTimeInMillis()), con, agtMap, 0);
				logger.info("Starting Opening Balance Update for Retailers of agentId {} For Date {}", agentOrgId, new Timestamp(from.getTimeInMillis()));
				for (Map.Entry<String, AgentCollectionReportOverAllBean> entrySet : agtMap.entrySet()) {
					organizationBalanceData = entrySet.getValue();
					pStmt.setInt(1, Integer.parseInt(entrySet.getKey()));
					pStmt.setString(2, "RETAILER");
					pStmt.setInt(3, agentOrgId);
					pStmt.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(from.getTime()));
					pStmt.setDouble(5, organizationBalanceData.getOpeningBal());
					pStmt.setDouble(6, organizationBalanceData.getLiveOpeningBal());
					pStmt.setDouble(7, organizationBalanceData.getNetTxnAmt());
					pStmt.setDouble(8, organizationBalanceData.getClLimit());
					pStmt.setDouble(9, organizationBalanceData.getXclLimit());
					pStmt.executeUpdate();
					pStmt.clearParameters();
				}
				setDefaultValueForRetailerOrgBalacnceBean(agtMap);
			}
		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void insertOrgBalance(Calendar from, Calendar to, Connection con) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;

		Map<String, OrganizationBalanceData> agtMap = null;
		OrganizationBalanceData orgBalanceDataBean = null;

		try {
			stmt = con.createStatement();

			agtMap = new HashMap<String, OrganizationBalanceData>();
			rs = stmt.executeQuery("select name orgCode, organization_id, parent_id from st_lms_organization_master where organization_type = 'AGENT';");
			while (rs.next()) {
				orgBalanceDataBean = new OrganizationBalanceData();
				orgBalanceDataBean.setAgentName(rs.getString("orgCode"));
				orgBalanceDataBean.setOrgId(rs.getInt("organization_id"));
				orgBalanceDataBean.setParentorgId(rs.getInt("parent_id"));
				orgBalanceDataBean.setOpeningBal(0.0);
				orgBalanceDataBean.setNetTxnAmt(0.0);
				orgBalanceDataBean.setCash(0.0);
				orgBalanceDataBean.setCheque(0.0);
				orgBalanceDataBean.setChequeReturn(0.0);
				orgBalanceDataBean.setCredit(0.0);
				orgBalanceDataBean.setDebit(0.0);
				orgBalanceDataBean.setBankDep(0.0);
				if (ReportUtility.isDG) {
					orgBalanceDataBean.setDgSale(0.0);
					orgBalanceDataBean.setDgPwt(0.0);
					orgBalanceDataBean.setDgCancel(0.0);
					orgBalanceDataBean.setDgDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					orgBalanceDataBean.setSeSale(0.0);
					orgBalanceDataBean.setSePwt(0.0);
					orgBalanceDataBean.setSeDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					orgBalanceDataBean.setCSSale(0.0);
					orgBalanceDataBean.setCSCancel(0.0);
				}
				if (ReportUtility.isOLA) {
					orgBalanceDataBean.setDeposit(0.0);
					orgBalanceDataBean.setDepositRefund(0.0);
					orgBalanceDataBean.setWithdrawal(0.0);
					orgBalanceDataBean.setWithdrawalRefund(0.0);
					orgBalanceDataBean.setNetGamingComm(0.0);
				}
				agtMap.put(rs.getString("organization_id"), orgBalanceDataBean);
			}
			insertOpeningBalance(from, to, agtMap, con);
		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void insertOpeningBalance(Calendar from, Calendar to, Map<String, OrganizationBalanceData> agtMap, Connection con) throws LMSException {
		OrganizationBalanceData organizationBalanceData = null;
		PreparedStatement pStmt = null;

		AgentOpeningBalanceHelper helper = new AgentOpeningBalanceHelper();
		try {
			pStmt = con.prepareStatement("insert into st_rep_org_bal_history(organization_id, organization_type, parent_id, finaldate, opening_bal, opening_bal_cl_inc, net_amount_transaction, cl, xcl) values(?, ?, ?, ?, ?, ?, ?, ?, ?);");
			for (from.getTime(); from.before(to); from.add(Calendar.DAY_OF_MONTH, 1)) {
				Calendar temp = Calendar.getInstance();
//				temp.setTime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sdf.format(lastInsertedDate.getTime()) + " 23:59:59"));
				temp.setTime(from.getTime());
				temp.add(Calendar.HOUR_OF_DAY, 23);
				temp.add(Calendar.MINUTE, 59);
				temp.add(Calendar.SECOND, 59);

				helper.collectionAgentWiseBalanceData(new Timestamp(from.getTimeInMillis()), new Timestamp(temp.getTimeInMillis()), con, agtMap);
				logger.info("Starting Agent Opening Balance For Date {}", new Timestamp(from.getTimeInMillis()));
				for (Map.Entry<String, OrganizationBalanceData> entrySet : agtMap.entrySet()) {
					organizationBalanceData = entrySet.getValue();
					pStmt.setInt(1, Integer.parseInt(entrySet.getKey()));
					pStmt.setString(2, "AGENT");
					pStmt.setInt(3, organizationBalanceData.getParentorgId());
					pStmt.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(from.getTime()));
					pStmt.setDouble(5, organizationBalanceData.getOpeningBal());
					pStmt.setDouble(6, organizationBalanceData.getLiveOpeningBal());
					pStmt.setDouble(7, organizationBalanceData.getNetTxnAmt());
					pStmt.setDouble(8, organizationBalanceData.getClLimit());
					pStmt.setDouble(9, organizationBalanceData.getXclLimit());
					pStmt.executeUpdate();
					pStmt.clearParameters();
				}
				setDefaultValueForOrgBalacnceBean(agtMap);
			}
		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
}
