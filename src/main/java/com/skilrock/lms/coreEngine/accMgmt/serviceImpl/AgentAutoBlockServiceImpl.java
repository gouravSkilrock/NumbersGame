package com.skilrock.lms.coreEngine.accMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.coreEngine.accMgmt.daoImpl.AgentAutoBlockDaoImpl;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.AgentAutoBlockBean;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CollectionReportOverAllHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.ICollectionReportOverAllHelper;
import com.skilrock.lms.web.accMgmt.common.AgentOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class AgentAutoBlockServiceImpl {
	private static Log logger = LogFactory.getLog(AgentAutoBlockServiceImpl.class);
	private static AgentAutoBlockServiceImpl instance = null;

	private AgentAutoBlockServiceImpl() {
	}

	public static AgentAutoBlockServiceImpl getInstance() {
		if (instance == null) {
			instance = new AgentAutoBlockServiceImpl();
		}
		return instance;
	}

	public void autoBlock() throws LMSException {
		logger.info("--  Agent Auto Block Start  --");

		Connection connection = null;
		Calendar calendar = Calendar.getInstance();
		Calendar todayDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = null;
		Properties weekdaysProperties = null;
		String[] weekdays = new String[]{"", "INCLUDE_SUNDAY", "INCLUDE_MONDAY", "INCLUDE_TUESDAY", "INCLUDE_WEDNESDAY", "INCLUDE_THURSDAY", "INCLUDE_FRIDAY", "INCLUDE_SATURDAY"};
		HistoryBean historyBean = null;
		try {
			connection = DBConnect.getConnection();

			boolean autoBlockActive = "YES".equals(Utility.getPropertyValue("IS_AUTO_BLOCK_ACTIVE")) ? true : false;
			logger.info("Auto Block Active Status - "+autoBlockActive);

			if(autoBlockActive) {
				AgentAutoBlockDaoImpl daoImpl = AgentAutoBlockDaoImpl.getInstance();
				Map<Integer, AgentAutoBlockBean> agentLimitMap = daoImpl.getAgentLimitList(false, connection);

				boolean includeHoliday = "YES".equals(Utility.getPropertyValue("INCLUDE_HOLIDAY")) ? true : false;
				logger.info("Include Holiday Status - "+includeHoliday);
				List<String> holidayList = (includeHoliday) ? daoImpl.getHolidayList(connection) : new ArrayList<String>();
				logger.info("Holiday List - "+holidayList);

				weekdaysProperties = PropertyLoader.loadProperties("RMS/agent_auto_block.properties");

				dateFormat = new SimpleDateFormat(Utility.getPropertyValue("date_format"));
				String dayCheck = (String) weekdaysProperties.get(weekdays[todayDate.get(Calendar.DAY_OF_WEEK)]);
				if(holidayList.contains(dateFormat.format(todayDate.getTime())) || "YES".equals(dayCheck)) {
					logger.info("Today is Holiday at - "+dateFormat.format(todayDate.getTime()));
					return;
				}

				boolean excludeCurrentDaySale = "YES".equals(Utility.getPropertyValue("EXCLUDE_CURRENT_DAY_SALE")) ? true : false;
				logger.info("Exclude Current Day Sale Status - "+excludeCurrentDaySale);

				int maxAutoblockDays = Integer.parseInt(Utility.getPropertyValue("MAXIMUM_AUTOBLOCK_DAYS"));
				logger.info("Maximum AutoBlock Days - "+maxAutoblockDays);
				for(int dayCount=1; dayCount<=maxAutoblockDays; dayCount++) {
					String date = dateFormat.format(calendar.getTime());
					logger.info("Date - "+date);
					dayCheck = (String) weekdaysProperties.get(weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
				    if(holidayList.contains(date) || "YES".equals(dayCheck)) {
				    	logger.info("Date Include Holiday - "+date);
				    	dayCount--;
				    	calendar.add(Calendar.DATE, -1);
				    	continue;
				    }

				    boolean currentSaleExcludeFlag = (excludeCurrentDaySale && (todayDate.getTime().compareTo(calendar.getTime()) == 0)) ? true : false;
				    checkOutStandingLimits(agentLimitMap, date, currentSaleExcludeFlag, connection);

				    Iterator<Integer> iterator = agentLimitMap.keySet().iterator();
					while(iterator.hasNext()) {
				    	int orgId = iterator.next();
				    	AgentAutoBlockBean bean = agentLimitMap.get(orgId);

						if(bean.getBlockAmount() > bean.getClosingBalance()) {
							if("INACTIVE".equals(bean.getOrgStatus())) {
								historyBean = new HistoryBean(orgId, 0, "Closing Balance of Organization is "+bean.getClosingBalance(), "");

								historyBean.setChangeType("ORGANIZATION_STATUS");
								historyBean.setChangeValue(bean.getOrgStatus());
								historyBean.setUpdatedValue("ACTIVE");
								CommonMethods.insertUpdateOrganizationHistory(historyBean, connection);
							}

							iterator.remove();
						} else {
							if(dayCount == bean.getBlockDays()) {
								logger.info("Organization Id - "+orgId+" | Closing Balance - "+bean.getClosingBalance());

								String blockAction = null;
								if("SALE_BLOCK".equals(bean.getBlockAction())) {
									blockAction = "INACTIVE";
								} else if("LOGIN_BLOCK".equals(bean.getBlockAction())) {
									blockAction = "BLOCK";
								}

								if(blockAction != null && !(blockAction.equals(bean.getOrgStatus())) && !(blockAction.equals(bean.getUserStatus()))) {
									historyBean = new HistoryBean(orgId, 0, "Closing Balance of Organization is "+bean.getClosingBalance(), "");

									historyBean.setChangeType("ORGANIZATION_STATUS");
									historyBean.setChangeValue(bean.getOrgStatus());
									historyBean.setUpdatedValue(blockAction);
									CommonMethods.insertUpdateOrganizationHistory(historyBean, connection);
								}
	
								iterator.remove();
							}
						}
					}

					if(agentLimitMap.size() == 0)
						break;

					calendar.add(Calendar.DATE, -1);
				}
			}
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		logger.info("--  Agent Auto Block End  --");
	}

	public void checkOutStandingLimits(Map<Integer, AgentAutoBlockBean> agentLimitMap, String date, boolean currentSaleExcludeFlag, Connection connection) throws LMSException {
		String dateFormat = Utility.getPropertyValue("date_format");
		ICollectionReportOverAllHelper collectionReportHelper = (LMSFilterDispatcher.isRepFrmSP) ? new CollectionReportOverAllHelperSP() : new CollectionReportOverAllHelper();
		try {
			Timestamp startDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(date).getTime());
			Timestamp endDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(date).getTime() + 24 * 60 * 60 * 1000 - 1000);
			Timestamp deployDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(Utility.getPropertyValue("DEPLOYMENT_DATE")).getTime());

			Map<String, CollectionReportOverAllBean> agentMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
			for(Integer orgId : agentLimitMap.keySet()) {
				agentMap.put(String.valueOf(orgId), new CollectionReportOverAllBean());
			}

			java.util.Date lastRunDate = ReportUtility.fetchLastRunDateifExists(connection);

			if (lastRunDate != null && lastRunDate.getTime() >= startDate.getTime() && !currentSaleExcludeFlag) {
				Map<Integer, Double> agtClosingBalMap = ReportUtility.fetchClosingBalAgentExcludingCLAndXCl(connection, startDate);
				for (Map.Entry<Integer, Double> entry : agtClosingBalMap.entrySet()) {
					if(agentLimitMap.containsKey(entry.getKey())) {
                        agentLimitMap.get(entry.getKey()).setClosingBalance(entry.getValue());
                    }
				}
			} else {
				new AgentOpeningBalanceHelper().collectionAgentWiseOpenningBal(deployDate, startDate, connection, agentMap);
				collectionReportHelper.collectionAgentWise(startDate, endDate, connection, agentMap);
	
				for(Map.Entry<String, CollectionReportOverAllBean> entry : agentMap.entrySet()) {
					String orgId = entry.getKey();
					CollectionReportOverAllBean bean = entry.getValue();
	
					if(currentSaleExcludeFlag) {
						bean.setDgSale(0.00);
						bean.setDgCancel(0.00);
						bean.setSeSale(0.00);
						bean.setSeCancel(0.00);
						bean.setCSSale(0.00);
						bean.setCSCancel(0.00);
						bean.setSleSale(0.00);
						bean.setSleCancel(0.00);
						bean.setIwSale(0.00);
						bean.setIwCancel(0.00);
					}
	
					double closingBalance = -(bean.getCash()+bean.getCheque()-bean.getChequeReturn()-bean.getDebit()+bean.getCredit()+bean.getBankDep())
											+bean.getOpeningBal()+bean.getDgSale()-bean.getDgCancel()-bean.getDgPwt()-bean.getDgDirPlyPwt()
											+bean.getSeSale()-bean.getSePwt()-bean.getSeDirPlyPwt()
											+bean.getCSSale()-bean.getCSCancel()
											+bean.getDeposit()-bean.getDepositRefund()-(bean.getWithdrawal()-bean.getWithdrawalRefund())
											-bean.getNetGamingComm()
											+bean.getSleSale()-bean.getSleCancel()-bean.getSlePwt()-bean.getSleDirPlyPwt()
											+bean.getIwSale()-bean.getIwCancel()-bean.getIwPwt()-bean.getIwDirPlyPwt();

					agentLimitMap.get(Integer.parseInt(orgId)).setClosingBalance(closingBalance);
				}
			}
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public Map<Integer, AgentAutoBlockBean> agentAutoBlockReport(ReportStatusBean reportStatusBean) throws LMSException {
		Connection connection = null;
		AgentAutoBlockDaoImpl daoImpl = AgentAutoBlockDaoImpl.getInstance();
		Map<Integer, AgentAutoBlockBean> agentDataMap = null;
		Map<Integer, AgentAutoBlockBean> agentLimitMap = null;
	    AgentAutoBlockBean bean = null;
		Calendar calendar = Calendar.getInstance();
		Calendar todayDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = null;
		Properties weekdaysProperties = null;
		String[] weekdays = new String[]{"", "INCLUDE_SUNDAY", "INCLUDE_MONDAY", "INCLUDE_TUESDAY", "INCLUDE_WEDNESDAY", "INCLUDE_THURSDAY", "INCLUDE_FRIDAY", "INCLUDE_SATURDAY"};
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				connection = DBConnect.getConnection();
			else
				connection = DBConnectReplica.getConnection();

			agentLimitMap = daoImpl.getAgentLimitList(true, connection);
			agentDataMap = new LinkedHashMap<Integer, AgentAutoBlockBean>();

			boolean includeHoliday = "YES".equals(Utility.getPropertyValue("INCLUDE_HOLIDAY")) ? true : false;
			logger.info("Include Holiday Status - "+includeHoliday);
			List<String> holidayList = (includeHoliday) ? daoImpl.getHolidayList(connection) : new ArrayList<String>();
			logger.info("Holiday List - "+holidayList);
			weekdaysProperties =  PropertyLoader.loadProperties("RMS/agent_auto_block.properties");

			boolean excludeCurrentDaySale = "YES".equals(Utility.getPropertyValue("EXCLUDE_CURRENT_DAY_SALE")) ? true : false;
			logger.info("Exclude Current Day Sale Status - "+excludeCurrentDaySale);

			dateFormat = new SimpleDateFormat(Utility.getPropertyValue("date_format"));
			int maxAutoblockDays = Integer.parseInt(Utility.getPropertyValue("MAXIMUM_AUTOBLOCK_DAYS"));
			logger.info("Maximum AutoBlock Days - "+maxAutoblockDays);
			for(int dayCount=1; dayCount<=maxAutoblockDays; dayCount++) {
				String date = dateFormat.format(calendar.getTime());
				logger.info("Date - "+date);
				String dayCheck = (String) weekdaysProperties.get(weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
			    if(holidayList.contains(date) || "YES".equals(dayCheck)) {
			    	logger.info("Date Include Holiday - "+date);
			    	dayCount--;
			    	calendar.add(Calendar.DATE, -1);
			    	continue;
			    }

			    boolean currentSaleExcludeFlag = (excludeCurrentDaySale && (todayDate.getTime().compareTo(calendar.getTime()) == 0)) ? true : false;
			    checkOutStandingLimits(agentLimitMap, date, currentSaleExcludeFlag, connection);

			    if(dayCount == 1) {
			    	agentDataMap.putAll(agentLimitMap);
			    	for(Integer orgId : agentDataMap.keySet()) {
			    		bean = agentDataMap.get(orgId);
			    		bean.setFirstClosingBalance(bean.getClosingBalance());

			    		if(bean.getBlockAmount() > bean.getClosingBalance()) {
			    			bean.setEligibleBlock(false);
			    			if("INACTIVE".equals(bean.getOrgStatus())) {
			    				bean.setEligibleActive(true);
				    		}
			    		} else
			    			bean.setEligibleBlock(true);
			    	}
			    }

			    Iterator<Integer> iterator = agentLimitMap.keySet().iterator();
				while(iterator.hasNext()) {
			    	int orgId = iterator.next();
			    	bean = agentLimitMap.get(orgId);

			    	if(bean.isEligibleBlock()) {
			    		if(bean.getBlockDays() >= dayCount) {
				    		if(bean.getBlockAmount() > bean.getClosingBalance()) {
				    			bean.setEligibleBlock(false);
								bean.setNegitiveFromDays(0);
				    		} else {
				    			bean.setNegitiveFromDays(bean.getNegitiveFromDays()+1);
								logger.info("Organization Id - "+orgId+" | Closing Balance - "+bean.getClosingBalance()+" | Negative From Days - "+bean.getNegitiveFromDays());
				    		}
			    		}
			    	}
				}

				if(agentLimitMap.size() == 0)
					break;

				calendar.add(Calendar.DATE, -1);
			}

			AgentAutoBlockBean reportBean = null;
			AgentAutoBlockBean dataBean = null;
			for(Integer orgId : agentLimitMap.keySet()) {
				reportBean = agentDataMap.get(orgId);
				dataBean = agentLimitMap.get(orgId);
				reportBean.setBlockDays(dataBean.getBlockDays());
				reportBean.setBlockAction(dataBean.getBlockAction());
			}
			agentLimitMap = null;
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return agentDataMap;
	}

	public void updateOrganizationStatus(int orgId, String currentStatus, int doneByUserId, String requestIp) throws LMSException {
		Connection connection = null;
		HistoryBean historyBean = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			String newStatus = "ACTIVE".equals(currentStatus) ? "INACTIVE" : "ACTIVE";
			historyBean = new HistoryBean(orgId, doneByUserId, "Agent Status Change to "+newStatus, requestIp);
			historyBean.setChangeType("ORGANIZATION_STATUS");
			historyBean.setChangeValue(currentStatus);
			historyBean.setUpdatedValue(newStatus);
			CommonMethods.insertUpdateOrganizationHistory(historyBean, connection);
			connection.commit();
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public double getCBForPayment(int agentOrgId, Connection connection) throws LMSException {
		double claimBalance = 0.00;

		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date todayDate = new Date();
			Timestamp startTime = new Timestamp(timeFormat.parse(dateFormat.format(todayDate)+" 00:00:00").getTime());
			Timestamp endTime = new Timestamp(timeFormat.parse(dateFormat.format(todayDate)+" 23:59:59").getTime());

			CollectionReportOverAllBean reportBean = AgentAutoBlockDaoImpl.getInstance().getCBForPayment(agentOrgId, startTime, endTime, connection);

			boolean excludeCurrentDaySale = "YES".equals(Utility.getPropertyValue("EXCLUDE_CURRENT_DAY_SALE")) ? true : false;
			logger.info("Exclude Current Day Sale - "+excludeCurrentDaySale);
			if(excludeCurrentDaySale) {
				reportBean.setDgSale(0.00);
				reportBean.setDgCancel(0.00);
				reportBean.setSeSale(0.00);
				reportBean.setSeCancel(0.00);
				reportBean.setCSSale(0.00);
				reportBean.setCSCancel(0.00);
				reportBean.setSleSale(0.00);
				reportBean.setSleCancel(0.00);
				//reportBean.setIwSale(0.00);
				//reportBean.setIwCancel(0.00);
			} else {
				reportBean.setIwSale(0.00);
				reportBean.setIwCancel(0.00);
			}

			claimBalance = reportBean.getDgSale()
				- reportBean.getDgCancel()
				- reportBean.getDgPwt()
				- reportBean.getDgDirPlyPwt()
				+ reportBean.getSeSale()
				- reportBean.getSeCancel()
				- reportBean.getSePwt()
				- reportBean.getSeDirPlyPwt()
				+ reportBean.getCSSale()
				- reportBean.getCSCancel()
				+ reportBean.getSleSale()
				- reportBean.getSleCancel()
				- reportBean.getSlePwt()
				- reportBean.getSleDirPlyPwt()
				- reportBean.getIwSale()
				+ reportBean.getIwCancel()
				/*- reportBean.getIwPwt()
				- reportBean.getIwDirPlyPwt()*/
				+ reportBean.getDeposit()
				- reportBean.getDepositRefund()
				- reportBean.getWithdrawal()
				- reportBean.getNetGamingComm();
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return claimBalance;
	}
}
