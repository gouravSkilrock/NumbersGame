package com.skilrock.lms.coreEngine.accMgmt.common;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.ManualRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.IWIncentiveDataBean;
import com.skilrock.lms.beans.IWTrainingExpBean;
import com.skilrock.lms.beans.IWTrainingExpDataBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.TrainingExpenseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.service.ServiceDelegateIW;
import com.skilrock.lms.web.drawGames.common.Util;

public class TrainingExpAgentHelper {

	static Log logger = LogFactory.getLog(TrainingExpAgentHelper.class);

	public static void main(String[] args) throws LMSException, SQLException {
		//Connection con=DBConnect.getConnection();
		//new TrainingExpAgentHelper().drawSaleAgentWiseTimeWiseNew(new Timestamp(2013, 11, 25, 14, 00, 00, 00),new Timestamp(2013, 11, 25, 16, 00, 00, 00), 2);
	/*	try {
			//new TrainingExpAgentHelper().submitWeeklyTrngExpForAgents();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	} 

	public Map<String, TrainingExpenseBean> fetchMenuData(String trngExpType, String startDate, String endDate, int serviceId, int gameNo) throws LMSException {
		Connection con = null;
		Map<String, TrainingExpenseBean> map = new LinkedHashMap<String, TrainingExpenseBean>();
		con = DBConnect.getConnection();

		TrainingExpenseBean tempBean = null;

		try {
			String orgCodeQry = " a.name orgCode ";

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode ";

			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode ";

			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode";

			}
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cal.setTimeInMillis(sdf.parse(endDate).getTime());
			cal.add(Calendar.DAY_OF_MONTH, +1);
			String endDate1 = new Timestamp(sdf.parse(new java.sql.Date(cal.getTimeInMillis()).toString()).getTime()).toString().split(" ")[0];
			String searchDate = null;
			if ("weekly".equalsIgnoreCase(trngExpType)) {
				searchDate = endDate1;
			} else {
				searchDate = endDate.split(" ")[0];
			}
			String childDataQry = null;
			if ("daily".equalsIgnoreCase(trngExpType)) {
				childDataQry = "select b.id," + orgCodeQry + ",b.agent_org_id,b.mrp_sale,b.training_exp_per,b.training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,b.incentive_mrp,b.incentive_per,b.incentive_amt,b.credit_note_number,b.incentive_credit_note_number,b.status,b.updated_date from st_lms_organization_master a,st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp b where b.agent_org_id=a.organization_id and date(b.date)= '" + searchDate + "' and service_id = " + serviceId + " and game_id=" + gameNo + " order by " + QueryManager.getAppendOrgOrder();
			} else {
				/*
				 * childDataQry = "select b.id,"+orgCodeQry+
				 * ",b.agent_org_id,b.mrp_sale,b.training_exp_per,b.training_exp_amt,b.credit_note_number,b.status,b.updated_date from st_lms_organization_master a,st_lms_agent_"
				 * + trngExpType.toLowerCase() +
				 * "_training_exp b where b.agent_org_id=a.organization_id and date(b.date)= '"
				 * + searchDate +
				 * "' and game_id="+gameNo+" order by "+QueryManager
				 * .getAppendOrgOrder();
				 */
				childDataQry = "select b.id," + orgCodeQry + ",b.agent_org_id,b.mrp_sale,b.training_exp_per,b.training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,b.incentive_mrp,b.incentive_per,b.incentive_amt,b.credit_note_number,b.incentive_credit_note_number,b.status,b.updated_date from st_lms_organization_master a,st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp b where b.agent_org_id=a.organization_id and date(b.date)= '" + searchDate + "' and service_id = " + serviceId + " and game_id=" + gameNo + " order by " + QueryManager.getAppendOrgOrder();
			}

			PreparedStatement pstmt = con.prepareStatement(childDataQry);
			System.out.println("query fetching training data :" + childDataQry);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tempBean = new TrainingExpenseBean();
				tempBean.setTaskId(rs.getInt("id"));
				tempBean.setOrgId(rs.getInt("agent_org_id"));
				tempBean.setOrgName(rs.getString("orgCode"));
				tempBean.setStatus(rs.getString("status"));
				tempBean.setSaleAmt(rs.getDouble("mrp_sale"));
				tempBean.setTrainingAmt(rs.getDouble("training_exp_amt"));
				tempBean.setTrainingPer(rs.getDouble("training_exp_per"));
				if(rs.getString("credit_note_number") == null && "DONE".equalsIgnoreCase(rs.getString("status"))){
					tempBean.setCrNote("N.A.");
				}else{
					tempBean.setCrNote(rs.getString("credit_note_number"));	
				}
				
				/*
				 * if("daily".equalsIgnoreCase(trngExpType)){
				 * tempBean.setTimeSlottedMrpSale
				 * (rs.getDouble("time_slotted_mrp_sale"));
				 * tempBean.setExtraTrainingPer
				 * (rs.getDouble("extra_training_exp_per"));
				 * tempBean.setExtraTrainingAmt
				 * (rs.getDouble("extra_training_exp_amt")); }
				 */
				tempBean.setTimeSlottedMrpSale(rs.getDouble("time_slotted_mrp_sale"));
				tempBean.setExtraTrainingPer(rs.getDouble("extra_training_exp_per"));
				tempBean.setExtraTrainingAmt(rs.getDouble("extra_training_exp_amt"));
				tempBean.setIncentiveMrp(rs.getDouble("incentive_mrp"));
				tempBean.setIncentivePer(rs.getDouble("incentive_per"));
				tempBean.setIncentiveAmt(rs.getDouble("incentive_amt"));
				if(rs.getString("incentive_credit_note_number") == null && "DONE".equalsIgnoreCase(rs.getString("status"))){
					tempBean.setIncentiveCrNote("N.A.");
				}else{
					tempBean.setIncentiveCrNote(rs.getString("incentive_credit_note_number"));
				}
				
				
//				tempBean.setIncentiveCrNote((rs.getString("incentive_credit_note_number") != null ? rs.getString("incentive_credit_note_number") : ((rs.getString("credit_note_number")) != null ? rs.getString("incentive_credit_note_number") : "N.A.")));
				String tempDate = rs.getString("updated_date");
				if (tempDate != null) {
					tempDate = tempDate.substring(0, tempDate.lastIndexOf('.'));
				}
				tempBean.setUpdateDate(tempDate);
				map.put(rs.getString("agent_org_id"), tempBean);
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		System.out.println(map);
		return map;
	}

	public String updateAgentData(int taskId[], UserInfoBean userBean, String trngExpType, String fromDate, String toDate, int serviceId, int gameNo) throws LMSException {
		StringBuilder trExpResp = new StringBuilder();
		Connection con = DBConnect.getConnection();
		Statement statement = null;
		StringBuilder taskIds = new StringBuilder();
		for (int i = 0; i < taskId.length; i++) {
			taskIds.append(taskId[i] + ",");
		}
		taskIds.deleteCharAt(taskIds.length() - 1);
		Timestamp updTime = new Timestamp(new Date().getTime());
		try {
			// con.setAutoCommit(false);
			statement = con.createStatement();
			String selectQry = "select id,date,agent_org_id,training_exp_amt+extra_training_exp_amt training_exp_amt,incentive_per,incentive_amt,updated_mode from st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp where id in (" + taskIds + ") and service_id = " + serviceId + " and status!='DONE'";
			ResultSet rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				con.setAutoCommit(false);
				if(rs.getDouble("training_exp_amt") != 0.0)
					updateTrngExpAgentDataIW(updTime, fromDate, toDate, rs.getInt("id"), rs.getInt("agent_org_id"), rs.getDouble("training_exp_amt"), userBean, "MANUAL", trngExpType, gameNo, serviceId, con);
				if(rs.getDouble("incentive_amt") > 0.00){
					
					updateAgentDataIWIncentive(updTime, fromDate, toDate, rs.getInt("id"), rs.getInt("agent_org_id"), rs.getDouble("incentive_amt"), userBean, "MANUAL", trngExpType, gameNo, serviceId, con);
				}
				updateApprovalStatus(rs.getInt("id"),con,trngExpType.toLowerCase());
				con.commit();
			}
			selectQry = "select  id,status,credit_note_number,incentive_credit_note_number,updated_date from st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp where id in (" + taskIds + ") and service_id = " + serviceId;
			rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				trExpResp.append(rs.getInt("id") + "," + rs.getString("status") + "," + (rs.getString("credit_note_number") != null ? rs.getString("credit_note_number"):"N.A.") + ","+rs.getString("updated_date").substring(0, rs.getString("updated_date").lastIndexOf('.'))+"," + (rs.getString("incentive_credit_note_number") != null ? rs.getString("incentive_credit_note_number"):"N.A.") + "Nxt");
			}
			// con.commit();
			DBConnect.closeCon(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(trExpResp);
		return trExpResp.toString();
	}

	// method for automatically generating credit note to all agents by amit ..
	public void submitDailyTrainingExpForAgents(ServletContext sc) throws Exception {
		String isdailyTrngExp = (String) sc.getAttribute("IS_DAILY_TRAINING_EXP");
		int serviceId = Util.getServiceIdFormCode("DG");
		if ("YES".equalsIgnoreCase(isdailyTrngExp)) {
			String trngExpMode = (String) sc.getAttribute("DAILY_TRAINING_EXP_MODE");
			Connection con = DBConnect.getConnection();
			try {
				con.setAutoCommit(false);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select game_id from st_dg_game_master where game_status <> 'SALE_CLOSE'");
				while (rs.next()) {
					int gameNo = rs.getInt("game_id");
					Statement statement = null;
					ResultSet resultSet = null;
					PreparedStatement pstmt = null;

					Timestamp startDate = null;
					Timestamp endDate = null;

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -1);// done for rescheduling of training expanses at 12:15 AM
					/*
					 * String currDate = new
					 * java.sql.Date(cal.getTimeInMillis()) .toString();
					 * startDate = new Timestamp((new
					 * SimpleDateFormat("yyyy-MM-dd"))
					 * .parse(currDate).getTime()); endDate = new Timestamp((new
					 * SimpleDateFormat("yyyy-MM-dd")).parse(
					 * currDate).getTime() + 24 * 60 * 60 * 1000 - 1000);
					 */
					// Timestamp currUpdateTime = new Timestamp(new
					// Date().getTime());

					UserInfoBean userBean = new UserInfoBean();

					statement = con.createStatement();
					if ("AUTO".equalsIgnoreCase(trngExpMode)) {
						resultSet = statement.executeQuery("select user_id,organization_id,organization_type from st_lms_user_master where user_name='bomaster'");
						if (resultSet.next()) {
							userBean.setUserOrgId(resultSet.getInt(TableConstants.ORGANIZATION_ID));
							userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
							userBean.setUserType(resultSet.getString(TableConstants.ORG_TYPE));
						}
					}

					Date lastDateDone = new java.sql.Date(cal.getTimeInMillis());
					Calendar calLastDate = Calendar.getInstance();
					Calendar calStratDate = Calendar.getInstance();
					calStratDate.setTime(lastDateDone);
					pstmt = con.prepareStatement("select date(date) date from st_lms_agent_daily_training_exp where service_id = "+serviceId+" and game_id= " + gameNo + " order by date desc limit 1");
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						lastDateDone = resultSet.getDate("date");
						calLastDate.setTime(lastDateDone);
						calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						System.out.println("last daily training exapnses given for " + lastDateDone);
					} else {
						System.out.println("daily training expanses generating first time");
						calLastDate.setTime(lastDateDone);
						// first time destribution of training expenses
					}
					System.out.println("calLastDate" + calLastDate.getTime());
					System.out.println("calStratDate" + calStratDate.getTime());
					System.out.println("diff..." + calLastDate.compareTo(calStratDate));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					while (calLastDate.compareTo(calStratDate) <= 0) {
						Map<Integer, Double> agtTrngExpMap = fetchDailyTrainingExp("daily", sdf.format(calLastDate.getTime()), serviceId, gameNo);
						Map<Integer, Double> agtExtraTrngExpMap = fetchExtraTrainingExp("daily", sdf.format(calLastDate.getTime()), gameNo, serviceId, con);
						List<Integer> agtOrgIdList = new ArrayList<Integer>();
						agtOrgIdList.addAll(agtTrngExpMap.keySet());

						startDate = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime());
						endDate = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + 24 * 60 * 60 * 1000 - 1000);

						Map<Integer, SalePwtReportsBean> reportMap = drawSaleAgentWiseNew(startDate, endDate, gameNo);

						String propertyValue = getPropertyValue(con, sdf.format(calLastDate.getTime()));
						// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

						String[] gameWiseTimingArr = propertyValue.split("~");
						Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
						for (String gameWiseTiming : gameWiseTimingArr) {
							gameSlotTimingMap.put(Integer.parseInt(gameWiseTiming.split("#")[0]), gameWiseTiming.split("#")[1]);
						}

						String slotTiming = gameSlotTimingMap.get(gameNo);
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
						simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
						long startTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
						long endTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

						Timestamp startTimeSale = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + startTimeSlot);
						Timestamp endTimeSale = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + endTimeSlot);

						Map<Integer, SalePwtReportsBean> reportSaleTimeMap = drawSaleAgentWiseTimeWiseNew(startTimeSale, endTimeSale, gameNo);

						System.out.println("total no. of agents " + agtOrgIdList.size());
						if (agtOrgIdList.size() > 0) {
							pstmt = con.prepareStatement("select date from st_lms_agent_daily_training_exp tes where date(tes.date)='" + new java.sql.Date(new Date().getTime()) + "' and service_id = "+serviceId+" and game_id=" + gameNo);
							resultSet = pstmt.executeQuery();
							if (resultSet.next()) {
								throw new LMSException("Daily expanses already given for the day ... ");
							}
							for (int i = 0; i < agtOrgIdList.size(); i++) {
								SalePwtReportsBean tempBean = reportMap.get(agtOrgIdList.get(i));
								SalePwtReportsBean tempBeanExtraTraining = reportSaleTimeMap.get(agtOrgIdList.get(i));

								if (tempBean != null) {
									double agtMrpSale = tempBean.getSaleMrpAmt();
									double trngExpAmt = (agtMrpSale * agtTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);

									double agtExtraMrpSale = tempBeanExtraTraining.getSaleMrpAmt();

									if (agtExtraMrpSale < 0) {
										agtExtraMrpSale = 0.0;
									}

									double trngExtraExpAmt = (agtExtraMrpSale * agtExtraTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);

									double totalTrainingExp = trngExpAmt + trngExtraExpAmt;
									System.out.println(" agent_org_id :" + agtOrgIdList.get(i) + " agtMrpSale:" + agtMrpSale + " for the day:" + startDate + " trngExpAmt:" + trngExpAmt + " @: " + agtTrngExpMap.get(agtOrgIdList.get(i)) + " ExtraagtMrpSale:" + agtExtraMrpSale + " for the day:" + startDate + " ExtratrngExpAmt:" + trngExtraExpAmt + " @: " + agtExtraTrngExpMap.get(agtOrgIdList.get(i)));
									if (totalTrainingExp != 0.0) {
										// entry into daily training expense table

										pstmt = con.prepareStatement(QueryManager.insertAgtDailyTrngExp());
										Calendar tempDate = (Calendar) calLastDate.clone();
										tempDate.set(Calendar.HOUR_OF_DAY, 23);
										tempDate.set(Calendar.MINUTE, 59);
										tempDate.set(Calendar.SECOND, 59);
										Timestamp updateTime = new Timestamp(tempDate.getTimeInMillis());
										pstmt.setTimestamp(1, new Timestamp(calLastDate.getTimeInMillis()));
										pstmt.setInt(2, agtOrgIdList.get(i));
										pstmt.setInt(3, serviceId);
										pstmt.setInt(4, gameNo);
										pstmt.setDouble(5, agtMrpSale);
										pstmt.setDouble(6, agtTrngExpMap.get(agtOrgIdList.get(i)));
										pstmt.setDouble(7, trngExpAmt);
										pstmt.setDouble(8, agtExtraMrpSale);
										pstmt.setDouble(9, agtExtraTrngExpMap.get(agtOrgIdList.get(i)));
										pstmt.setDouble(10, trngExtraExpAmt);
										pstmt.setString(11, "PENDING");
										System.out.println("insertAgtTrngExp>>" + pstmt);
										pstmt.executeUpdate();
										int taskId = 0;
										resultSet = pstmt.getGeneratedKeys();
										if (resultSet.next())
											taskId = resultSet.getInt(1);
										if ("AUTO".equalsIgnoreCase(trngExpMode)) {
											updateTrngExpAgentData(updateTime, updateTime.toString(), updateTime.toString(), taskId, agtOrgIdList.get(i), totalTrainingExp, userBean, trngExpMode, "DAILY", gameNo, serviceId, con);
											updateApprovalStatus(taskId,con,"daily");	
										}
									}
								}
							}
							calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						} else {
							calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						}
					}
				}
				con.commit();
			} catch (SQLException e) {
				logger.info("SQL Exception ", e);
				throw new LMSException("SQL Exception " + e.getMessage());
			} catch (Exception e) {
				logger.info("Exception ", e);
				throw new LMSException("Exception" + e.getMessage());
			} finally {
				DBConnect.closeCon(con);
			}
		}
	}
	
/*	public void submitDailyIWNonWinningTrainingExpForAgents(ServletContext sc, String incDurType) throws LMSException {
		String isdailyTrngExp = (String) sc.getAttribute("IS_"+incDurType+"_TRAINING_EXP");
		int serviceId = Util.getServiceIdFormCode("IW");
		ServiceRequest serviceRequest = null ;
		String response = null ;
		if ("YES".equalsIgnoreCase(isdailyTrngExp)) {
			String trngExpMode = (String) sc.getAttribute(incDurType+"_TRAINING_EXP_MODE");
			Connection con = DBConnect.getConnection();
			try {
				con.setAutoCommit(false);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select game_id from st_iw_game_master where game_status <> 'SALE_CLOSE'");
				while (rs.next()) {
					int gameNo = rs.getInt("game_id");
					Statement statement = null;
					ResultSet resultSet = null;
					PreparedStatement pstmt = null;

					String startDate = null;
					String endDate = null;

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, "DAILY".equalsIgnoreCase(incDurType) ? -1 : -7);// done for rescheduling of training expanses at 12:15 AM

					UserInfoBean userBean = new UserInfoBean();

					statement = con.createStatement();
					if ("AUTO".equalsIgnoreCase(trngExpMode)) {
						resultSet = statement.executeQuery("select user_id,organization_id,organization_type from st_lms_user_master where user_name='bomaster'");
						if (resultSet.next()) {
							userBean.setUserOrgId(resultSet.getInt(TableConstants.ORGANIZATION_ID));
							userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
							userBean.setUserType(resultSet.getString(TableConstants.ORG_TYPE));
						}
					}
					Date lastDateDone = new java.sql.Date(cal.getTimeInMillis());
					Calendar calLastDate = Calendar.getInstance();
					Calendar calStratDate = Calendar.getInstance();
					calStratDate.setTime(lastDateDone);
					Timestamp updateTime = "DAILY".equalsIgnoreCase(incDurType) ? new Timestamp(new java.sql.Date(new Date().getTime()).getTime()) :  new Timestamp((calStratDate.getTime()).getTime()+24 * 7 * 60 * 60 * 1000);
					
					pstmt = con.prepareStatement("select date(date) date from st_lms_agent_"+incDurType.toLowerCase()+"_training_exp tes where date(tes.date)='" + updateTime + "' and  service_id = "+serviceId+" and game_id= " + gameNo + " order by date desc limit 1");
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						lastDateDone = resultSet.getDate("date");
						calLastDate.setTime(lastDateDone);
						calLastDate.add(Calendar.DAY_OF_MONTH, "DAILY".equalsIgnoreCase(incDurType) ? 1 : 7);
						System.out.println("last "+incDurType+" training exapnses given for " + lastDateDone);
					} else {
						System.out.println(incDurType + " training expanses generating first time");
						calLastDate.setTime(lastDateDone);
						// first time destribution of training expenses
					}
					System.out.println("calLastDate" + calLastDate.getTime());
					System.out.println("calStratDate" + calStratDate.getTime());
					System.out.println("diff..." + calLastDate.compareTo(calStratDate));
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					while (calLastDate.compareTo(calStratDate) <= 0) {
						startDate = sdf.format(calLastDate.getTime())+" 00:00:00" ;
						endDate = sdf.format((calStratDate.getTime()).getTime()+("DAILY".equalsIgnoreCase(incDurType) ? 0 : (24 * 7 * 60 * 60 * 1000 - 1000)))+" 23:59:59" ;
						serviceRequest = new ServiceRequest();
						serviceRequest.setServiceName("/retailer/terminal/");
						serviceRequest.setServiceMethod("BORetailerDataCall");
						serviceRequest.setServiceData("?fromDate="+URLEncoder.encode(startDate, "UTF-8")+"&toDate="+URLEncoder.encode(endDate, "UTF-8"));
						//serviceRequest.setServiceData("?fromDate="+URLEncoder.encode("03/01/2016 00:00:00", "UTF-8")+"&toDate="+URLEncoder.encode("04/30/2016 23:59:59", "UTF-8"));
						response = ServiceDelegateIW.getInstance().getResponseStringForTrainingExpense(serviceRequest);
						//response = "{  \"responseCode\": \"0\",  \"dataArray\": [    {      \"winning\": 0,      \"userName\": \"testr1\",      \"sale\": 400    },    {      \"winning\": 4050,      \"userName\": \"TEEJAY006\",      \"sale\": 5670    }  ],  \"responseMsg\": \"Success\"}";
						if (response == null) {
							throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
						}
						IWTrainingExpBean bean = new Gson().fromJson(response, new TypeToken<IWTrainingExpBean>() {}.getType());
						if("success".equalsIgnoreCase(bean.getResponseMsg()) && bean.getDataArray().size() > 0){
								prepareDataRecord(con, bean.getDataArray());
								Map<Integer, Double> agentWiseMap = clubDataAgentWise(con, bean.getDataArray(), gameNo, serviceId, incDurType, startDate, endDate);
								if(agentWiseMap.size() > 0){
										if ("AUTO".equalsIgnoreCase(trngExpMode)) {
											updateAllAgentData(sdf.format(calLastDate.getTime())+" 00:00:00", sdf1.format((calStratDate.getTime()).getTime())+" 23:59:59", incDurType, userBean, serviceId, gameNo, con);
										}
								}
						}
						calLastDate.add(Calendar.DAY_OF_MONTH, 1);
				}
				con.commit();
				}} catch (LMSException e) {
				logger.info("LMS Exception ", e);
				throw e;
			} catch (Exception e) {
				logger.info("Exception ", e);
				throw new LMSException("Exception" + e.getMessage());
			} finally {
				DBConnect.closeCon(con);
			}
		}
	}*/
	
/*
 * This method will return the map which will contain the agentOrgId as key and the final incentive to be given as Value.
 * */
	public Map<Integer, IWIncentiveDataBean> clubDataAgentWise(Connection con, List<IWTrainingExpDataBean> dataArray, int gameId, int serviceId, String incDurType, Timestamp startDate, Timestamp endDate) {
		Statement preparedStatement = null;
		Statement fetchTrngExpStatement = null;
		PreparedStatement prepareUpdateStatement = null ;
		PreparedStatement insPreStatement = null ;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		Map<Integer, IWIncentiveDataBean> agentIncentiveMap = null;
		Map<String, IWTrainingExpDataBean> retailerMap = null;
		IWIncentiveDataBean inDataBean = null;
		int agentOrgId = 0;
		double incentivePercentage = 0.00;
		double amountForIncentive = 0.00;
		double calculatedIncentive = 0.00 ;
		
		try {
			agentIncentiveMap = new HashMap<Integer, IWIncentiveDataBean>();
			retailerMap = new HashMap<String, IWTrainingExpDataBean>();
			StringBuilder userList = new StringBuilder() ;
			for (IWTrainingExpDataBean dataBean : dataArray) {
				userList.append("'"+dataBean.getUserName()+"',");
				retailerMap.put(dataBean.getUserName(), dataBean);
			}
			String insIncentivequery = "INSERT INTO st_iw_retailer_"+incDurType+"_incentive_data(start_date,end_date,user_name, sale_amt, winning_amt,incentive_per,incentive_amt) VALUES(?,?,?,?,?,?,?)" ;
			String fetchTrainingExpenseQuery = "select organization_id,value+incentive_exp_var incentive_train_exp from (select organization_id,name,ifnull(incentive_exp_var,0.0) incentive_exp_var from st_lms_organization_master left outer join (select var.agent_org_id,ifnull(hist_var,incentive_exp_var)incentive_exp_var from st_lms_agent_"+incDurType+"_trng_exp_var_mapping var left join (select agent_org_id,incentive_exp_var hist_var, service_id,game_id from(select agent_org_id,incentive_exp_var,updated_date, service_id,game_id from st_lms_agent_"+incDurType+"_trng_exp_var_mapping_history where date(updated_date) > '"+startDate+"' and service_id = "+serviceId+" and game_id = "+gameId+" order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.service_id=hist.service_id and var.game_id=hist.game_id  where var.service_id = "+serviceId+" and var.game_id="+gameId+") map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select incentive_exp_default value from st_lms_agent_default_"+incDurType+"_training_exp where service_id = "+serviceId+" and game_id="+gameId+") dtr;";
			String query = "select um1.user_name,group_concat(um.user_name) ret_user_name, um1.organization_id organization_id from st_lms_user_master um inner join st_lms_user_master um1 on um.parent_user_id = um1.user_id where um.user_name IN ("+userList.substring(0, userList.length()-1)+") group by um1.user_name";
			logger.info("insIncentivequery "+insIncentivequery);
			logger.info("fetchTrainingExpenseQuery "+fetchTrainingExpenseQuery);
			logger.info("query "+query);
			preparedStatement = con.createStatement();
			fetchTrngExpStatement = con.createStatement();
			insPreStatement = con.prepareStatement(insIncentivequery) ;
			resultSet = preparedStatement.executeQuery(query);
			resultSet1 = fetchTrngExpStatement.executeQuery(fetchTrainingExpenseQuery);
			while(resultSet1.next()){
				inDataBean = new IWIncentiveDataBean();
				inDataBean.setIncentivePer( resultSet1.getDouble("incentive_train_exp"));
				agentIncentiveMap.put(resultSet1.getInt("organization_id"),inDataBean);
			}
			logger.info("agentIncentiveMap size "+agentIncentiveMap.size());
			while(resultSet.next()){
				String[] userArray = resultSet.getString("ret_user_name").split(",");
				agentOrgId = resultSet.getInt("organization_id"); 
				for (int i=0; i< userArray.length; i++) {
					if(retailerMap.get(userArray[i]) != null && agentIncentiveMap.containsKey(agentOrgId)){
						incentivePercentage = agentIncentiveMap.get(agentOrgId).getIncentivePer();
						amountForIncentive = retailerMap.get(userArray[i]).getSale() - retailerMap.get(userArray[i]).getWinning();
						calculatedIncentive = amountForIncentive > 0.00 ? (amountForIncentive * incentivePercentage * 0.01) : 0.00 ;
						
						insPreStatement.setTimestamp(1, startDate);
						insPreStatement.setTimestamp(2, endDate);
						insPreStatement.setString(3, userArray[i]);
						insPreStatement.setDouble(4, retailerMap.get(userArray[i]).getSale());
						insPreStatement.setDouble(5, retailerMap.get(userArray[i]).getWinning());
						insPreStatement.setDouble(6, incentivePercentage);
						insPreStatement.setDouble(7, calculatedIncentive);
						insPreStatement.addBatch() ;
						agentIncentiveMap.get(agentOrgId).setIncentiveAmt(agentIncentiveMap.get(agentOrgId).getIncentiveAmt() + calculatedIncentive);
						agentIncentiveMap.get(agentOrgId).setIncentiveMrp(agentIncentiveMap.get(agentOrgId).getIncentiveMrp() + amountForIncentive);
					}
				}
					
			}
			logger.info("insPreStatement "+insPreStatement.getFetchSize());
			insPreStatement.executeBatch() ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(preparedStatement, fetchTrngExpStatement, prepareUpdateStatement, resultSet,insPreStatement) ;
		}
		
		return agentIncentiveMap ;

	}

	public void submitDailyInstantWinTrainingExpForAgents(ServletContext sc) throws Exception {
		int serviceId = Util.getServiceIdFormCode("IW");
			String trngExpMode = (String) sc.getAttribute("DAILY_TRAINING_EXP_MODE");
			Connection con = DBConnect.getConnection();
			String response = null ;
			Map<Integer, SalePwtReportsBean> reportMap = null;
			Map<Integer, SalePwtReportsBean> reportSaleTimeMap = null;
			Map<Integer, Double> agtTrngExpMap = null;
			Map<Integer, Double> agtExtraTrngExpMap = null;
			Map<Integer, IWIncentiveDataBean> agentWiseMap = null;
			try {
				con.setAutoCommit(false);
				Statement stmt = con.createStatement();			
				ResultSet rs = stmt.executeQuery("select game_id from st_iw_game_master where game_status <> 'SALE_CLOSE'");
				while (rs.next()) {
					int gameNo = rs.getInt("game_id");
					Statement statement = null;
					ResultSet resultSet = null;
					PreparedStatement pstmt = null;
					Timestamp startDate = null;
					Timestamp endDate = null;
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -1);// done for rescheduling of training expanses at 12:15 AM
					UserInfoBean userBean = new UserInfoBean();
					statement = con.createStatement();
					if ("AUTO".equalsIgnoreCase(trngExpMode)) {
						resultSet = statement.executeQuery("select user_id,organization_id,organization_type from st_lms_user_master where user_name='bomaster'");
						if (resultSet.next()) {
							userBean.setUserOrgId(resultSet.getInt(TableConstants.ORGANIZATION_ID));
							userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
							userBean.setUserType(resultSet.getString(TableConstants.ORG_TYPE));
						}
					}
					Date lastDateDone = new java.sql.Date(cal.getTimeInMillis());
					Calendar calLastDate = Calendar.getInstance();
					Calendar calStratDate = Calendar.getInstance();
					calStratDate.setTime(lastDateDone);
					pstmt = con.prepareStatement("select date(date) date from st_lms_agent_daily_training_exp where service_id = "+serviceId+" and game_id= " + gameNo + " order by date desc limit 1");
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						lastDateDone = resultSet.getDate("date");
						calLastDate.setTime(lastDateDone);
						calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						System.out.println("last daily training exapnses given for " + lastDateDone);
					} else {
						System.out.println("daily training expanses generating first time");
						calLastDate.setTime(lastDateDone);
					}
					System.out.println("calLastDate" + calLastDate.getTime());
					System.out.println("calStratDate" + calStratDate.getTime());
					System.out.println("diff..." + calLastDate.compareTo(calStratDate));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					while (calLastDate.compareTo(calStratDate) <= 0) {
						startDate = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime());
						endDate = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + 24 * 60 * 60 * 1000 - 1000);
						List<Integer> agtOrgIdList = new ArrayList<Integer>();
						if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_INCENTIVE_EXPENSE_ENABLED"))){
								ServiceRequest serviceRequest = new ServiceRequest();
								serviceRequest.setServiceName("/retailer/terminal/");
								serviceRequest.setServiceMethod("BORetailerDataCall");
								serviceRequest.setServiceData("?fromDate="+URLEncoder.encode(startDate.toString(), "UTF-8")+"&toDate="+URLEncoder.encode(endDate.toString(), "UTF-8"));
								//serviceRequest.setServiceData("?fromDate="+URLEncoder.encode("2016-03-01 00:00:00", "UTF-8")+"&toDate="+URLEncoder.encode("2016-04-30 23:59:59", "UTF-8"));
								response = ServiceDelegateIW.getInstance().getResponseStringForTrainingExpense(serviceRequest);
								//response = "{  \"responseCode\": \"0\",  \"dataArray\": [    {      \"winning\": 20,      \"userName\": \"testr1\",      \"sale\": 400    },    {      \"winning\": 4050,      \"userName\": \"TEEJAY006\",      \"sale\": 5670    }  ],  \"responseMsg\": \"Success\"}";
								if (response == null) {
									throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
								}
								IWTrainingExpBean bean = new Gson().fromJson(response, new TypeToken<IWTrainingExpBean>() {}.getType());
								if("success".equalsIgnoreCase(bean.getResponseMsg()) && bean.getDataArray().size() > 0){
										//prepareDataRecord(con, bean.getDataArray());
										agentWiseMap = clubDataAgentWise(con, bean.getDataArray(), gameNo, serviceId, "daily", startDate, endDate);
										logger.info("agentWiseMap length : "+agentWiseMap.size());
										if(agentWiseMap.size() > 0)
											agtOrgIdList.addAll(agentWiseMap.keySet());
								}
						}
						if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_TRAINING_EXPENSE_ENABLED"))){
							agtTrngExpMap = fetchDailyTrainingExp("daily", sdf.format(calLastDate.getTime()), serviceId, gameNo);
							agtExtraTrngExpMap = fetchExtraTrainingExp("daily", sdf.format(calLastDate.getTime()), gameNo, serviceId, con);
							reportMap = iwSaleAgentWiseNew(startDate, endDate, gameNo);
							String propertyValue = getPropertyValue(con, sdf.format(calLastDate.getTime()));
							// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

							String[] gameWiseTimingArr = propertyValue.split("~");
							Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
							for (String gameWiseTiming : gameWiseTimingArr) {
								gameSlotTimingMap.put(Integer.parseInt(gameWiseTiming.split("#")[0]), gameWiseTiming.split("#")[1]);
							}
							String slotTiming = gameSlotTimingMap.get(gameNo);
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
							simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
							long startTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
							long endTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

							Timestamp startTimeSale = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + startTimeSlot);
							Timestamp endTimeSale = new Timestamp(sdf.parse(sdf.format(calLastDate.getTime())).getTime() + endTimeSlot);

							reportSaleTimeMap = iwSaleAgentWiseTimeWiseNew(startTimeSale, endTimeSale, gameNo);
							if(agtOrgIdList.size() < 1)
								agtOrgIdList.addAll(agtTrngExpMap.keySet());
						}
						if (agtOrgIdList.size() > 0) {
							pstmt = con.prepareStatement("select date from st_lms_agent_daily_training_exp tes where date(tes.date)='" + new java.sql.Date(new Date().getTime()) + "' and service_id = " + serviceId + " and game_id=" + gameNo);
							resultSet = pstmt.executeQuery();
							if (resultSet.next()) {
								throw new LMSException("Daily expanses already given for the day ... ");
							}
							for (int i = 0; i < agtOrgIdList.size(); i++) {
								double agtMrpSale = 0.0;
								double trainingExpPer = 0.0;
								double trngExpAmt = 0.0;
								double agtExtraMrpSale = 0.0;
								double agtExtraTrainingExpPer = 0.0;
								double trngExtraExpAmt = 0.0;
								double totalTrainingExp = 0.0;
								double incentiveAmt = 0.0;
								double incentivePer = 0.0;
								double incentiveMrp = 0.0;
								
								if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_TRAINING_EXPENSE_ENABLED"))){
									SalePwtReportsBean tempBean = reportMap.get(agtOrgIdList.get(i));
									SalePwtReportsBean tempBeanExtraTraining = reportSaleTimeMap.get(agtOrgIdList.get(i));
									if (tempBean != null) {
										agtMrpSale = tempBean.getSaleMrpAmt();
										trngExpAmt = (agtMrpSale * agtTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);
										agtExtraMrpSale = tempBeanExtraTraining.getSaleMrpAmt();
										if (agtExtraMrpSale < 0) {
											agtExtraMrpSale = 0.0;
										}
										trngExtraExpAmt = (agtExtraMrpSale * agtExtraTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);
										totalTrainingExp = trngExpAmt + trngExtraExpAmt;
										trainingExpPer = agtTrngExpMap.get(agtOrgIdList.get(i));
										agtExtraTrainingExpPer = agtExtraTrngExpMap.get(agtOrgIdList.get(i));
									}
								} 
								
								if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_DAILY_IW_INCENTIVE_EXPENSE_ENABLED"))){
									if(agentWiseMap != null && agentWiseMap.containsKey(agtOrgIdList.get(i))){
										incentiveAmt = agentWiseMap.get(agtOrgIdList.get(i)).getIncentiveAmt();
										incentivePer = agentWiseMap.get(agtOrgIdList.get(i)).getIncentivePer();
										incentiveMrp = agentWiseMap.get(agtOrgIdList.get(i)).getIncentiveMrp();
									}
								}
								//System.out.println(" agent_org_id :" + agtOrgIdList.get(i) + " agtMrpSale:" + agtMrpSale + " for the day:" + startDate + " trngExpAmt:" + trngExpAmt + " @: " + agtTrngExpMap.get(agtOrgIdList.get(i)) + " ExtraagtMrpSale:" + agtExtraMrpSale + " for the day:" + startDate + " ExtratrngExpAmt:" + trngExtraExpAmt + " @: " + agtExtraTrngExpMap.get(agtOrgIdList.get(i)));
								if (totalTrainingExp != 0.0 || incentiveAmt > 0.0) {
										// entry into daily training expense table
										pstmt = con.prepareStatement("insert into st_lms_agent_daily_training_exp(date,agent_org_id, service_id,game_id,mrp_sale,training_exp_per,training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,incentive_mrp,incentive_per,incentive_amt,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
										Calendar tempDate = (Calendar) calLastDate.clone();
										tempDate.set(Calendar.HOUR_OF_DAY, 23);
										tempDate.set(Calendar.MINUTE, 59);
										tempDate.set(Calendar.SECOND, 59);
										Timestamp updateTime = new Timestamp(tempDate.getTimeInMillis());
										pstmt.setTimestamp(1, new Timestamp(calLastDate.getTimeInMillis()));
										pstmt.setInt(2, agtOrgIdList.get(i));
										pstmt.setInt(3, serviceId);
										pstmt.setInt(4, gameNo);
										pstmt.setDouble(5, agtMrpSale);
										pstmt.setDouble(6, trainingExpPer);
										pstmt.setDouble(7, trngExpAmt);
										pstmt.setDouble(8, agtExtraMrpSale);
										pstmt.setDouble(9, agtExtraTrainingExpPer);
										pstmt.setDouble(10, trngExtraExpAmt);
										pstmt.setDouble(11, incentiveMrp);
										pstmt.setDouble(12, incentivePer);
										pstmt.setDouble(13, incentiveAmt);
										pstmt.setString(14, "PENDING");
										System.out.println("insertAgtTrngExp>>" + pstmt);
										pstmt.executeUpdate();
										int taskId = 0;
										resultSet = pstmt.getGeneratedKeys();
										if (resultSet.next())
											taskId = resultSet.getInt(1);
										if ("AUTO".equalsIgnoreCase(trngExpMode)) {
											if(totalTrainingExp != 0.0)
												updateTrngExpAgentData(updateTime, updateTime.toString(), updateTime.toString(), taskId, agtOrgIdList.get(i), totalTrainingExp, userBean, trngExpMode, "DAILY", gameNo, serviceId, con);
											if(incentiveAmt > 0.0)
												updateAgentDataIWIncentive(updateTime, updateTime.toString(), updateTime.toString(), taskId, agtOrgIdList.get(i), incentiveAmt, userBean, trngExpMode, "DAILY", gameNo, serviceId, con);
											updateApprovalStatus(taskId,con,"daily");	
										}
									}
							}
							calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						} else {
							calLastDate.add(Calendar.DAY_OF_MONTH, 1);
						}
					}
				}
				con.commit();
			} catch (SQLException e) {
				logger.info("SQL Exception ", e);
				throw new LMSException("SQL Exception " + e.getMessage());
			} catch (Exception e) {
				logger.info("Exception ", e);
				throw new LMSException("Exception" + e.getMessage());
			} finally {
				DBConnect.closeCon(con);
			}
	}
	
	public String getPropertyValue(Connection con,String expTime) throws LMSException{
		Statement stmt=null;
		ResultSet rs=null;
		String propertyValue=null;
		try{
			stmt=con.createStatement();
			rs=stmt.executeQuery("select value from st_lms_property_master_history where property_code='gameWiseSaleSlotTime' and date(update_date)>'"+expTime+"' order by update_date asc limit 1");
			if(rs.next()){
				propertyValue=rs.getString("value");
			}else{
				rs=stmt.executeQuery("select value from st_lms_property_master where property_dev_name='SALE_SLOT_TIME'");
				if(rs.next()){
					propertyValue=rs.getString("value");
				}else{
					throw new LMSException();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
		return propertyValue;
	}
/**
 * @modified  transaction_id type changed to long from int
 *  By Neeraj
 *
 */
	public void updateTrngExpAgentData(Timestamp updateTime, String fromDate, String toDate, int taskId, int agtOrgId, double trngExpAmt, UserInfoBean userBean, String UpdateMode, String trngExpType,int gameNo, int serviceId, Connection con) throws Exception {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		String reason="";
		String transactionType = trngExpAmt > 0.0 ? "CR_NOTE_CASH":"DR_NOTE_CASH";
		double unsignTrngAmt =  trngExpAmt > 0.0 ? trngExpAmt : (-1) * trngExpAmt;
		if ("AUTO".equalsIgnoreCase(UpdateMode)) {
			HttpServletRequest request = new ManualRequest();
			request.setAttribute("code", "MGMT");
			request.setAttribute("interfaceType", "WEB");
			ServletActionContext.setRequest(request);
		}
		String updateQry = null;
		try {
			updateQry = "update st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp set credit_note_number=?, done_by_user_id=?,  updated_date=?, updated_mode=? where service_id = " + serviceId + " and agent_org_id=" + agtOrgId + " and id=" + taskId;	

			/*
			 * String updateBoRecieptGenMapping = QueryManager
			 * .updateST5BOReceiptGenMapping();
			 */
			// insert into LMS transaction master
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			pstmt1 = con.prepareStatement(queryLMSTrans);
			pstmt1.setString(1, "BO");
			pstmt1.executeUpdate();
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			// int transaction_id = -1;
			long transaction_id =-1;
			if (rs1.next()) {
				transaction_id = rs1.getLong(1);
			} else {
				throw new LMSException("Transaction id is not generated ");
			}
			
			String updateBoMaster = QueryManager.insertInBOTransactionMaster();
			pstmt1 = con.prepareStatement(updateBoMaster);
			pstmt1.setLong(1, transaction_id);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setString(4, "AGENT");
			pstmt1.setInt(5, agtOrgId);
			pstmt1.setTimestamp(6, updateTime);
			pstmt1.setString(7,transactionType);
			pstmt1.executeUpdate();
			System.out.println("insertInBOTransactionMaster>>>" + pstmt1);

			String updateCreditNote = "insert into st_lms_bo_credit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)";
			String updateDebitNote = "insert into st_lms_bo_debit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)" ;
			String remarks = null;
			if ("weekly".equalsIgnoreCase(trngExpType)) {
				remarks = "Given as Weekly Training expense for the week from "
						+ fromDate.split(" ")[0] + " to "
						+ toDate.split(" ")[0];
			} else {
				remarks = "Given as Daily Training expense for the day "
						+ fromDate.split(" ")[0];
			}
			if(trngExpAmt > 0.0 ){
				pstmt2 = con.prepareStatement(updateCreditNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="CR_WEEKLY_EXP";
				else
					reason="CR_DAILY_EXP";
			}else{
				pstmt2 = con.prepareStatement(updateDebitNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="DR_WEEKLY_TE_RETURN";
				else
					reason="DR_DAILY_TE_RETURN";
			}
			pstmt2.setLong(1, transaction_id);
			pstmt2.setInt(2, agtOrgId);
			pstmt2.setDouble(3, unsignTrngAmt);
			pstmt2.setString(4, transactionType);
			pstmt2.setString(5, remarks);
			pstmt2.setString(6, reason);
			System.out.println("st_lms_bo_credit/debit_note>>>" + pstmt2);
			pstmt2.executeUpdate();

			String fetchCreditNoteLastEntryQuery = "SELECT * from st_lms_bo_receipts where receipt_type=? or receipt_type=? ORDER BY generated_id DESC LIMIT 1";
			pstmt4 = con.prepareStatement(fetchCreditNoteLastEntryQuery);
			if(trngExpAmt > 0.0){
				pstmt4.setString(1, "CR_NOTE_CASH");
				pstmt4.setString(2, "CR_NOTE");
			}else{
				pstmt4.setString(1, "DR_NOTE_CASH");
				pstmt4.setString(2, "DR_NOTE");
			}
			ResultSet recieptRs = pstmt4.executeQuery();
			String lastRecieptNoGenerated = null;
			if (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}
			// create receipt no.
			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(transactionType, lastRecieptNoGenerated, userBean.getUserType());

			// insert in receipt master table
			pstmt2 = con.prepareStatement(QueryManager.insertInReceiptMaster());
			pstmt2.setString(1, "BO");
			pstmt2.executeUpdate();
			ResultSet rs2 = pstmt2.getGeneratedKeys();
			int id = -1;
			if (rs2.next()) {
				id = rs2.getInt(1);
			} else {
				throw new LMSException("Error in reciept generation");
			}

			// insert into BO receipt table
			pstmt2 = con.prepareStatement(QueryManager.insertInBOReceipts());
			pstmt2.setInt(1, id);
			pstmt2.setString(2, transactionType);
			pstmt2.setInt(3, agtOrgId);
			pstmt2.setString(4, "AGENT");
			pstmt2.setString(5, autoGeneRecieptNo);
			pstmt2.setTimestamp(6, Util.getCurrentTimeStamp());
			System.out.println("insertInBOReceipts>>>" + pstmt2);
			pstmt2.executeUpdate();

			// insert into receipt transaction mapping
			pstmt3 = con.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			pstmt3.setInt(1, id);
			pstmt3.setLong(2, transaction_id);
			pstmt3.executeUpdate();

			// update organization account details
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(unsignTrngAmt, "TRANSACTION", transactionType, agtOrgId,
					0, "AGENT", 0, con);
			if(!isValid)
				throw new LMSException();
			
			
			/*OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					transactionType, unsignTrngAmt, con);*/

			// update training expense table

			pstmt5 = con.prepareStatement(updateQry);
			pstmt5.setString(1, autoGeneRecieptNo);
			pstmt5.setInt(2, userBean.getUserId());
			//pstmt5.setString(3, "DONE");
			pstmt5.setTimestamp(3, updateTime);
			pstmt5.setString(4, UpdateMode);
			System.out.println("updating training Expense table.>>>>" + pstmt5);
			if (pstmt5.executeUpdate() < 1) {
				throw new LMSException("training expense table not updated");
			}
			// following code is commented temp....
			/*
			 * GraphReportHelper graphReportHelper = new GraphReportHelper();
			 * graphReportHelper.createTextReportBO(id, parentOrgName,
			 * userOrgID, (String) session.getAttribute("ROOT_PATH"));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void submitWeeklyTrngExpForAgents()	throws Exception {
		Connection con = null;
		Statement statement = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		int serviceId = Util.getServiceIdFormCode("DG");
		try {
			String isweeklyTrngExp = Utility.getPropertyValue("IS_WEEKLY_TRAINING_EXP");// (String)sc.getAttribute("IS_WEEKLY_TRAINING_EXP");
			if ("YES".equalsIgnoreCase(isweeklyTrngExp)) {
				String trngExpMode = Utility.getPropertyValue("WEEKLY_TRAINING_EXP_MODE");// (String)sc.getAttribute("WEEKLY_TRAINING_EXP_MODE");
				con = DBConnect.getConnection();

				UserInfoBean userBean = new UserInfoBean();
				statement = con.createStatement();
				if ("AUTO".equalsIgnoreCase(trngExpMode)) {
					resultSet = statement.executeQuery("select user_id,organization_id,organization_type from st_lms_user_master where user_name='bomaster'");
					if (resultSet.next()) {
						userBean.setUserOrgId(resultSet.getInt(TableConstants.ORGANIZATION_ID));
						userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
						userBean.setUserType(resultSet.getString(TableConstants.ORG_TYPE));
					}
				}
				DBConnect.closeRs(resultSet);
				con.setAutoCommit(false);
				stmt = con.createStatement();
				rs = stmt.executeQuery("select game_id from st_dg_game_master  where game_status <> 'SALE_CLOSE' ");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				while (rs.next()) {
					int gameNo = rs.getInt("game_id");
					// ResultSet resultSet = null;
					Timestamp weekStartDate = null;
					Timestamp weekEndDate = null;
					Timestamp updateTime = null;
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -7);// done for rescheduling of training expanses at 12:15 AM
					Date lastDateDone = new java.sql.Date(cal.getTimeInMillis());

					Calendar calLastDate = Calendar.getInstance();
					Calendar calStartDate = Calendar.getInstance();
					calStartDate.setTime(lastDateDone);
					pstmt = con.prepareStatement("select date(date) date from st_lms_agent_weekly_training_exp where service_id = "+serviceId+" and game_id= " + gameNo + " order by date desc limit 1");
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						lastDateDone = resultSet.getDate("date");
						calLastDate.setTime(lastDateDone);
						logger.info("last weekly training exapnses given for " + lastDateDone);
					} else {
						logger.info("weekly training expanses generating first time");
						calLastDate.setTime(lastDateDone);
						// first time destribution of training expenses
					}
					logger.info("calLastDate" + calLastDate.getTime() + " calStratDate" + calStartDate.getTime() + " diff..." + calLastDate.compareTo(calStartDate));
					DBConnect.closeRs(resultSet);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();
					// while
					
					while (calLastDate.compareTo(calStartDate) <= 0) {
						Map<Integer, SalePwtReportsBean> totalreportSaleTimeMap = new HashMap<Integer, SalePwtReportsBean>();
						long lastTime = sdf.parse(sdf.format(calLastDate.getTime())).getTime();
						weekStartDate = new Timestamp(lastTime);
						weekEndDate = new Timestamp(lastTime + 24 * 7 * 60 * 60 * 1000 - 1000);
						updateTime = new Timestamp(lastTime + 24 * 7 * 60 * 60 * 1000);
						startDate.setTimeInMillis(lastTime);
						endDate.setTimeInMillis(lastTime + 24 * 7 * 60 * 60 * 1000 - 1000);
						Map<Integer, SalePwtReportsBean> reportMap = drawSaleAgentWiseNew(weekStartDate, weekEndDate, gameNo);
						Map<Integer, Double> agtTrngExpMap = fetchTrainingExp("weekly", sdf.format(weekEndDate.getTime()), gameNo, serviceId);
						Map<Integer, Double> agtExtraTrngExpMap = fetchExtraTrainingExp("weekly", sdf.format(weekEndDate.getTime()), gameNo, serviceId, con);
						List<Integer> agtOrgIdList = new ArrayList<Integer>();
						agtOrgIdList.addAll(agtTrngExpMap.keySet());
						while (startDate.compareTo(endDate) <= 0) {
							String propertyValue = getPropertyValue(con, sdf.format(startDate.getTime()));
							// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

							String[] gameWiseTimingArr = propertyValue.split("~");
							logger.debug("prop value" + propertyValue);
							Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
							for (String gameWiseTiming : gameWiseTimingArr) {
								gameSlotTimingMap.put(Integer.parseInt(gameWiseTiming.split("#")[0]), gameWiseTiming.split("#")[1]);
							}

							String slotTiming = gameSlotTimingMap.get(gameNo);

							long startTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
							long endTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

							Timestamp startTimeSale = new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime() + startTimeSlot);
							Timestamp endTimeSale = new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime() + endTimeSlot);

							Map<Integer, SalePwtReportsBean> reportSaleTimeMap = drawSaleAgentWiseWeeklyTimeWiseNew(startTimeSale, endTimeSale, weekStartDate, weekEndDate, gameNo);

							Iterator itr = reportSaleTimeMap.entrySet().iterator();
							while (itr.hasNext()) {
								Map.Entry<Integer, SalePwtReportsBean> entry = (Map.Entry<Integer, SalePwtReportsBean>) itr.next();

								if (totalreportSaleTimeMap.containsKey(entry.getKey())) {
									SalePwtReportsBean bean = totalreportSaleTimeMap.get(entry.getKey());
									bean.setSaleMrpAmt(bean.getSaleMrpAmt() + reportSaleTimeMap.get(entry.getKey()).getSaleMrpAmt());
									bean.setSaleNetAmt(bean.getSaleNetAmt() + reportSaleTimeMap.get(entry.getKey()).getSaleNetAmt());
								} else {
									totalreportSaleTimeMap.put(entry.getKey(), entry.getValue());
								}
							}
							startDate.add(Calendar.DAY_OF_MONTH, 1);
						}

						logger.debug("total no. of agents " + agtOrgIdList.size());
						if (agtOrgIdList.size() > 0) {
							pstmt = con.prepareStatement("select date from st_lms_agent_weekly_training_exp tes where date(tes.date)=date('" + updateTime + "') and service_id = "+serviceId+" and game_id=" + gameNo);
							resultSet = pstmt.executeQuery();
							if (resultSet.next()) {
								throw new LMSException("Weekly expanses already given for this Week ... ");
							}
							DBConnect.closeRs(resultSet);
							for (int i = 0; i < agtOrgIdList.size(); i++) {
								SalePwtReportsBean tempBean = reportMap.get(agtOrgIdList.get(i));
								SalePwtReportsBean tempBeanExtraTraining = totalreportSaleTimeMap.get(agtOrgIdList.get(i));

								if (tempBean != null) {
									double agtMrpSale = tempBean.getSaleMrpAmt();
									double trngExpAmt = (agtMrpSale * agtTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);
									double agtExtraMrpSale = tempBeanExtraTraining.getSaleMrpAmt();

									if (agtExtraMrpSale < 0) {
										agtExtraMrpSale = 0.0;
									}
									double trngExtraExpAmt = (agtExtraMrpSale * agtExtraTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);

									logger.debug(" agent_org_id :" + agtOrgIdList.get(i) + " agtMrpSale:" + agtMrpSale + " for the day:" + startDate + " trngExpAmt:" + trngExpAmt + " @: " + agtTrngExpMap.get(agtOrgIdList.get(i)) + " ExtraagtMrpSale:" + agtExtraMrpSale + " for the day:" + startDate + " ExtratrngExpAmt:" + trngExtraExpAmt + " @: " + agtExtraTrngExpMap.get(agtOrgIdList.get(i)));

									double totalTrainingExp = trngExpAmt + trngExtraExpAmt;

									if (totalTrainingExp > 0.0) {
										// entry into weekly training expense table
										pstmt = con.prepareStatement(QueryManager.insertAgtWeeklyTrngExp());
										/*
										 * Calendar tempDate = (Calendar)calLastDate.clone();
										 * tempDate.add(Calendar.DAY_OF_MONTH,1);
										 * tempDate.set(Calendar.HOUR_OF_DAY,00);
										 * tempDate.set(Calendar.MINUTE,00);
										 * tempDate.set(Calendar.SECOND,00);
										 */
										// Timestamp updateTime = new
										// Timestamp(tempDate.getTimeInMillis());
										pstmt.setTimestamp(1, updateTime);
										pstmt.setInt(2, agtOrgIdList.get(i));
										pstmt.setInt(3, serviceId);
										pstmt.setInt(4, gameNo);
										pstmt.setDouble(5, agtMrpSale);
										pstmt.setDouble(6, agtTrngExpMap.get(agtOrgIdList.get(i)));
										pstmt.setDouble(7, trngExpAmt);
										pstmt.setDouble(8, agtExtraMrpSale);
										pstmt.setDouble(9, agtExtraTrngExpMap.get(agtOrgIdList.get(i)));
										pstmt.setDouble(10, trngExtraExpAmt);
										pstmt.setString(11, "PENDING");
										logger.info("insertAgtTrngExp>>" + pstmt);
										pstmt.executeUpdate();
										int taskId = 0;
										resultSet = pstmt.getGeneratedKeys();

										if (resultSet.next())
											taskId = resultSet.getInt(1);
										if ("AUTO".equalsIgnoreCase(trngExpMode)) {
											updateTrngExpAgentData(updateTime, new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime()).toString(), new Timestamp(sdf.parse(sdf.format(endDate.getTime())).getTime()).toString(), taskId, agtOrgIdList.get(i), totalTrainingExp, userBean, trngExpMode, "WEEKLY", gameNo, serviceId, con);
											updateApprovalStatus(taskId,con,"weekly");	
										}
									}
								}
							}
							calLastDate.add(Calendar.DAY_OF_MONTH, 7);
						} else {
							calLastDate.add(Calendar.DAY_OF_MONTH, 7);
						}
					}
				}
				con.commit();
			}
		} catch (SQLException e) {
			logger.info("SQL Exception ", e);
			throw new LMSException("SQL Exception " + e.getMessage());
		} catch (Exception e) {
			logger.info("Exception ", e);
			throw new LMSException("Exception" + e.getMessage());
		} finally {
			DBConnect.closeRs(resultSet);
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
			DBConnect.closeStmt(stmt);
			DBConnect.closeStmt(statement);
			DBConnect.closeCon(con);
		}
	}
	
	public void submitWeeklyTrngExpInstantWinForAgents() throws Exception {
		Connection con = null;
		Statement statement = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		String response = null;
		Map<Integer, IWIncentiveDataBean> agentWiseMap = null;
		int serviceId = Util.getServiceIdFormCode("IW");
		Map<Integer, SalePwtReportsBean> reportMap = null;
		Map<Integer, SalePwtReportsBean> totalreportSaleTimeMap = null;
		Map<Integer, Double> agtTrngExpMap = null;
		Map<Integer, Double> agtExtraTrngExpMap = null;
		try {
				String trngExpMode = Utility.getPropertyValue("WEEKLY_TRAINING_EXP_MODE");// (String)sc.getAttribute("WEEKLY_TRAINING_EXP_MODE");
				con = DBConnect.getConnection();
				UserInfoBean userBean = new UserInfoBean();
				statement = con.createStatement();
				
				if ("AUTO".equalsIgnoreCase(trngExpMode)) {
					resultSet = statement.executeQuery("select user_id,organization_id,organization_type from st_lms_user_master where user_name='bomaster'");
					if (resultSet.next()) {
						userBean.setUserOrgId(resultSet.getInt(TableConstants.ORGANIZATION_ID));
						userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
						userBean.setUserType(resultSet.getString(TableConstants.ORG_TYPE));
					}
				}
				DBConnect.closeRs(resultSet);
				con.setAutoCommit(false);
				stmt = con.createStatement();
				rs = stmt.executeQuery("select game_id from st_iw_game_master  where game_status <> 'SALE_CLOSE' ");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
				simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				while (rs.next()) {
					int gameNo = rs.getInt("game_id");
					Timestamp weekStartDate = null;
					Timestamp weekEndDate = null;
					Timestamp updateTime = null;
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -7);// done for rescheduling of training expanses at 12:15 AM
					Date lastDateDone = new java.sql.Date(cal.getTimeInMillis());

					Calendar calLastDate = Calendar.getInstance();
					Calendar calStartDate = Calendar.getInstance();
					calStartDate.setTime(lastDateDone);
					pstmt = con.prepareStatement("select date(date) date from st_lms_agent_weekly_training_exp where service_id = "+serviceId+" and game_id=" + gameNo + " order by date desc limit 1");
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {
						lastDateDone = resultSet.getDate("date");
						calLastDate.setTime(lastDateDone);
						logger.info("last weekly training expenses of IW given for " + lastDateDone);
					} else {
						logger.info("weekly training expanses generating first time for IW");
						calLastDate.setTime(lastDateDone);
						// first time destribution of training expenses
					}
					logger.info("calLastDate" + calLastDate.getTime() + " calStratDate" + calStartDate.getTime() + " diff..." + calLastDate.compareTo(calStartDate));
					DBConnect.closeRs(resultSet);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();
					// while
					while (calLastDate.compareTo(calStartDate) <= 0) {
						long lastTime = sdf.parse(sdf.format(calLastDate.getTime())).getTime();
						updateTime = new Timestamp(lastTime + 24 * 7 * 60 * 60 * 1000);
						weekStartDate = new Timestamp(lastTime);
						weekEndDate = new Timestamp(lastTime + 24 * 7 * 60 * 60 * 1000 - 1000);
						List<Integer> agtOrgIdList = new ArrayList<Integer>();
						
						if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_INCENTIVE_EXPENSE_ENABLED"))){
							ServiceRequest serviceRequest = new ServiceRequest();
							serviceRequest.setServiceName("/retailer/terminal/");
							serviceRequest.setServiceMethod("BORetailerDataCall");
							serviceRequest.setServiceData("?fromDate="+URLEncoder.encode(String.valueOf(weekStartDate), "UTF-8")+"&toDate="+URLEncoder.encode(String.valueOf(weekEndDate), "UTF-8"));
							//serviceRequest.setServiceData("?fromDate="+URLEncoder.encode("2016-03-07 00:00:00", "UTF-8")+"&toDate="+URLEncoder.encode("2016-03-14 23:59:59", "UTF-8"));
							response = ServiceDelegateIW.getInstance().getResponseStringForTrainingExpense(serviceRequest);
							//response = "{  \"responseCode\": \"0\",  \"dataArray\": [    {      \"winning\": 0,      \"userName\": \"testr1\",      \"sale\": 400    },    {      \"winning\": 4050,      \"userName\": \"TEEJAY006\",      \"sale\": 5670    }  ],  \"responseMsg\": \"Success\"}";
							if (response == null) {
								throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
							}
							IWTrainingExpBean bean = new Gson().fromJson(response, new TypeToken<IWTrainingExpBean>() {}.getType());
							if("success".equalsIgnoreCase(bean.getResponseMsg()) && bean.getDataArray().size() > 0){
									//prepareDataRecord(con, bean.getDataArray());
									agentWiseMap = clubDataAgentWise(con, bean.getDataArray(), gameNo, serviceId, "weekly", weekStartDate, weekEndDate);
									if(agentWiseMap.size() > 0)
										agtOrgIdList.addAll(agentWiseMap.keySet());
							}
							
						}
						
						if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_TRAINING_EXPENSE_ENABLED"))){
							totalreportSaleTimeMap = new HashMap<Integer, SalePwtReportsBean>();
							startDate.setTimeInMillis(lastTime);
							endDate.setTimeInMillis(lastTime + 24 * 7 * 60 * 60 * 1000 - 1000);
							reportMap = iwSaleAgentWiseNew(weekStartDate, weekEndDate, gameNo);
							agtTrngExpMap = fetchTrainingExp("weekly", sdf.format(weekEndDate.getTime()), gameNo, serviceId);
							agtExtraTrngExpMap = fetchExtraTrainingExp("weekly", sdf.format(weekEndDate.getTime()), gameNo, serviceId, con);
							if(agtOrgIdList.size() < 1)
								agtOrgIdList.addAll(agtTrngExpMap.keySet());
							
							while (startDate.compareTo(endDate) <= 0) {
								String propertyValue = getPropertyValue(con, sdf.format(startDate.getTime()));
								// 1#14:00:00_16:00:00~2#00:00:00_00:00:00

								String[] gameWiseTimingArr = propertyValue.split("~");
								logger.debug("prop value" + propertyValue);
								Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
								for (String gameWiseTiming : gameWiseTimingArr) {
									gameSlotTimingMap.put(Integer.parseInt(gameWiseTiming.split("#")[0]), gameWiseTiming.split("#")[1]);
								}

								String slotTiming = gameSlotTimingMap.get(gameNo);

								long startTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[0]).getTime();
								long endTimeSlot = simpleDateFormat.parse(slotTiming.split("_")[1]).getTime();

								Timestamp startTimeSale = new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime() + startTimeSlot);
								Timestamp endTimeSale = new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime() + endTimeSlot);

								Map<Integer, SalePwtReportsBean> reportSaleTimeMap = iwSaleAgentWiseWeeklyTimeWiseNew(startTimeSale, endTimeSale, weekStartDate, weekEndDate, gameNo);

								Iterator itr = reportSaleTimeMap.entrySet().iterator();
								while (itr.hasNext()) {
									Map.Entry<Integer, SalePwtReportsBean> entry = (Map.Entry<Integer, SalePwtReportsBean>) itr.next();

									if (totalreportSaleTimeMap.containsKey(entry.getKey())) {
										SalePwtReportsBean bean = totalreportSaleTimeMap.get(entry.getKey());
										bean.setSaleMrpAmt(bean.getSaleMrpAmt() + reportSaleTimeMap.get(entry.getKey()).getSaleMrpAmt());
										bean.setSaleNetAmt(bean.getSaleNetAmt() + reportSaleTimeMap.get(entry.getKey()).getSaleNetAmt());
									} else {
										totalreportSaleTimeMap.put(entry.getKey(), entry.getValue());
									}
								}
								startDate.add(Calendar.DAY_OF_MONTH, 1);
							}

						}
						logger.debug("total no. of agents " + agtOrgIdList.size());
						if (agtOrgIdList.size() > 0) {
							pstmt = con.prepareStatement("select date from st_lms_agent_weekly_training_exp tes where date(tes.date)=date('" + updateTime + "') and service_id = "+serviceId+" and game_id=" + gameNo);
							resultSet = pstmt.executeQuery();
							if (resultSet.next()) {
								throw new LMSException("Weekly expanses already given for this Week ... ");
							}
							DBConnect.closeRs(resultSet);
							for (int i = 0; i < agtOrgIdList.size(); i++) {
								double agtMrpSale = 0.0;
								double trainingExpPer = 0.0;
								double trngExpAmt = 0.0;
								double agtExtraMrpSale = 0.0;
								double agtExtraTrainingExpPer = 0.0;
								double trngExtraExpAmt = 0.0;
								double totalTrainingExp = 0.0;
								double incentiveAmt = 0.0;
								double incentivePer = 0.0;
								double incentiveMrp = 0.0;
								
								if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_TRAINING_EXPENSE_ENABLED"))){
									SalePwtReportsBean tempBean = reportMap.get(agtOrgIdList.get(i));
									SalePwtReportsBean tempBeanExtraTraining = totalreportSaleTimeMap.get(agtOrgIdList.get(i));
									if (tempBean != null) {
										agtMrpSale = tempBean.getSaleMrpAmt();
										trngExpAmt = (agtMrpSale * agtTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);
										agtExtraMrpSale = tempBeanExtraTraining.getSaleMrpAmt();
										if (agtExtraMrpSale < 0) {
											agtExtraMrpSale = 0.0;
										}
										trngExtraExpAmt = (agtExtraMrpSale * agtExtraTrngExpMap.get(agtOrgIdList.get(i)) * 0.01);
										logger.debug(" agent_org_id :" + agtOrgIdList.get(i) + " agtMrpSale:" + agtMrpSale + " for the day:" + startDate + " trngExpAmt:" + trngExpAmt + " @: " + agtTrngExpMap.get(agtOrgIdList.get(i)) + " ExtraagtMrpSale:" + agtExtraMrpSale + " for the day:" + startDate + " ExtratrngExpAmt:" + trngExtraExpAmt + " @: " + agtExtraTrngExpMap.get(agtOrgIdList.get(i)));
										totalTrainingExp = trngExpAmt + trngExtraExpAmt;
										trainingExpPer = agtTrngExpMap.get(agtOrgIdList.get(i));
										agtExtraTrainingExpPer = agtExtraTrngExpMap.get(agtOrgIdList.get(i));
									}
								}
								if("YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WEEKLY_IW_INCENTIVE_EXPENSE_ENABLED"))){
									if(agentWiseMap != null && agentWiseMap.containsKey(agtOrgIdList.get(i))){
										incentiveAmt = agentWiseMap.get(agtOrgIdList.get(i)).getIncentiveAmt();
										incentivePer = agentWiseMap.get(agtOrgIdList.get(i)).getIncentivePer();
										incentiveMrp = agentWiseMap.get(agtOrgIdList.get(i)).getIncentiveMrp();
									}
								}
								if (totalTrainingExp > 0.0 || incentiveAmt > 0.0) {
										pstmt = con.prepareStatement("insert into st_lms_agent_weekly_training_exp (date,agent_org_id, service_id,game_id,mrp_sale,training_exp_per,training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,incentive_mrp,incentive_per,incentive_amt,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
										pstmt.setTimestamp(1, updateTime);
										pstmt.setInt(2, agtOrgIdList.get(i));
										pstmt.setInt(3, serviceId);
										pstmt.setInt(4, gameNo);
										pstmt.setDouble(5, agtMrpSale);
										pstmt.setDouble(6,trainingExpPer);
										pstmt.setDouble(7, trngExpAmt);
										pstmt.setDouble(8, agtExtraMrpSale);
										pstmt.setDouble(9, agtExtraTrainingExpPer);
										pstmt.setDouble(10, trngExtraExpAmt);
										pstmt.setDouble(11, incentiveMrp);
										pstmt.setDouble(12, incentivePer);
										pstmt.setDouble(13, incentiveAmt);
										pstmt.setString(14, "PENDING");
										logger.info("insertAgtTrngExp>>" + pstmt);
										pstmt.executeUpdate();
										int taskId = 0;
										resultSet = pstmt.getGeneratedKeys();

										if (resultSet.next())
											taskId = resultSet.getInt(1);
										
										if ("AUTO".equalsIgnoreCase(trngExpMode)) {
											if(totalTrainingExp > 0.0)
												updateTrngExpAgentData(updateTime, new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime()).toString(), new Timestamp(sdf.parse(sdf.format(endDate.getTime())).getTime()).toString(), taskId, agtOrgIdList.get(i), totalTrainingExp, userBean, trngExpMode, "WEEKLY", gameNo, serviceId, con);
											if(incentiveAmt > 0.0)
												updateAgentDataIWIncentive(updateTime, new Timestamp(sdf.parse(sdf.format(startDate.getTime())).getTime()).toString(), new Timestamp(sdf.parse(sdf.format(endDate.getTime())).getTime()).toString(), taskId, agtOrgIdList.get(i), incentiveAmt, userBean, trngExpMode, "WEEKLY", gameNo, serviceId, con);
											updateApprovalStatus(taskId,con,"weekly");	
										}
									}
							}
							calLastDate.add(Calendar.DAY_OF_MONTH, 7);
						} else {
							calLastDate.add(Calendar.DAY_OF_MONTH, 7);
						}
					}
				}
				con.commit();
		} catch (SQLException e) {
			logger.info("SQL Exception ", e);
			throw new LMSException("SQL Exception " + e.getMessage());
		} catch (Exception e) {
			logger.info("Exception ", e);
			throw new LMSException("Exception" + e.getMessage());
		} finally {
			DBConnect.closeRs(resultSet);
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
			DBConnect.closeStmt(stmt);
			DBConnect.closeStmt(statement);
			DBConnect.closeCon(con);
		}
	}

	private Map<Integer, Double> fetchTrainingExp(String type, String expDate, int gameNo, int serviceId) throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Map<Integer, Double> agtTrngExpMap = new HashMap<Integer, Double>();
		try {
			if ("daily".equalsIgnoreCase(type)) {
				statement = con
						.prepareStatement("select organization_id, value + training_exp_var train_exp from (select organization_id, name, ifnull(training_exp_var, 0.0) training_exp_var from st_lms_organization_master left outer join (select var.agent_org_id, ifnull(hist_var, training_exp_var)training_exp_var from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping var left join (select agent_org_id,training_exp_var hist_var, service_id, game_id from (select agent_org_id,training_exp_var,updated_date, service_id,game_id from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping_history where date(updated_date) > ? and service_id = ? and game_id = ? order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.game_id=hist.game_id  and var.service_id = hist.service_id where var.service_id = ? and var.game_id = ?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"
								+ type.toLowerCase()
								+ "_training_exp where service_id = ? and game_id = ?) dtr;");
			} else if ("weekly".equalsIgnoreCase(type)) {
				statement = con
						.prepareStatement("select organization_id, value + training_exp_var train_exp from (select organization_id, name, ifnull(training_exp_var, 0.0) training_exp_var from st_lms_organization_master left outer join (select var.agent_org_id, ifnull(hist_var, training_exp_var)training_exp_var from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping var left join (select agent_org_id,training_exp_var hist_var, aa.service_id, game_id from (select agent_org_id,training_exp_var,updated_date, service_id, game_id from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping_history where date(updated_date) > ? and service_id = ? and game_id = ? order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.game_id=hist.game_id  where var.service_id  = ? and var.game_id = ?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"
								+ type.toLowerCase()
								+ "_training_exp where service_id = ? and game_id = ?) dtr;");
			} else {
				throw new LMSException("IN Valid Type" + type);
			}
			statement.setString(1, expDate);
			statement.setInt(2, serviceId);
			statement.setInt(3, gameNo);
			statement.setInt(4, serviceId);
			statement.setInt(5, gameNo);
			statement.setInt(6, serviceId);
			statement.setInt(7, gameNo);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				agtTrngExpMap.put(resultSet.getInt("organization_id"), resultSet.getDouble("train_exp"));
			}
		} catch (SQLException e) {
			logger.error("SQL Exception", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception", e);
			// e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return agtTrngExpMap;
	}

	private Map<Integer, Double> fetchDailyTrainingExp(String type, String expDate, int serviceId, int gameNo) {
		Connection con = DBConnect.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Map<Integer, Double> agtTrngExpMap = new HashMap<Integer, Double>();
		try {
			statement = con
					.prepareStatement("select organization_id,value+training_exp_var train_exp from (select organization_id,name,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join (select var.agent_org_id,ifnull(hist_var,training_exp_var)training_exp_var from st_lms_agent_"
							+ type.toLowerCase()
							+ "_trng_exp_var_mapping var left join (select agent_org_id,training_exp_var hist_var, service_id,game_id from(select agent_org_id,training_exp_var,updated_date, service_id,game_id from st_lms_agent_"
							+ type.toLowerCase()
							+ "_trng_exp_var_mapping_history where date(updated_date) > ? and service_id = ? and game_id=? order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.service_id=hist.service_id and var.game_id=hist.game_id  where var.service_id = ? and var.game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"
							+ type.toLowerCase()
							+ "_training_exp where service_id = ? and game_id=?) dtr;");
			statement.setString(1, expDate);
			statement.setInt(2, serviceId);
			statement.setInt(3, gameNo);
			statement.setInt(4, serviceId);
			statement.setInt(5, gameNo);
			statement.setInt(6, serviceId);
			statement.setInt(7, gameNo);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				agtTrngExpMap.put(resultSet.getInt("organization_id"), resultSet.getDouble("train_exp"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtTrngExpMap;
	}
	
	private Map<Integer, Double> fetchExtraTrainingExp(String type, String expDate, int gameNo, int serviceId, Connection con) throws LMSException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Map<Integer, Double> agtExtraTrngExpMap = new HashMap<Integer, Double>();
		try {
			if ("daily".equalsIgnoreCase(type)) {
				statement = con
						.prepareStatement("select organization_id,value + extra_training_exp_var extra_train_exp from (select organization_id,name,ifnull(extra_training_exp_var,0.0) extra_training_exp_var from st_lms_organization_master left outer join (select var.agent_org_id,var.game_id,ifnull(hist_var,extra_training_exp_var)extra_training_exp_var from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping var left join (select agent_org_id,extra_training_exp_var hist_var, service_id,game_id from(select agent_org_id,extra_training_exp_var,updated_date, service_id, game_id from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping_history where date(updated_date) > ? and service_id = ? and game_id = ? order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.service_id=hist.service_id and var.game_id=hist.game_id  where var.service_id = ? and var.game_id = ?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select ifnull(training_exp_extra,0.0) value from st_lms_agent_default_"
								+ type.toLowerCase()
								+ "_training_exp where service_id = ? and game_id = ?) dtr;");
			} else if ("weekly".equalsIgnoreCase(type)) {
				statement = con
						.prepareStatement("select organization_id,value+extra_training_exp_var extra_train_exp from (select organization_id,name,ifnull(extra_training_exp_var,0.0) extra_training_exp_var from st_lms_organization_master left outer join (select var.agent_org_id,var.game_id,ifnull(hist_var,extra_training_exp_var)extra_training_exp_var from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping var left join (select agent_org_id,extra_training_exp_var hist_var, service_id,game_id from(select agent_org_id,extra_training_exp_var,updated_date, service_id,game_id from st_lms_agent_"
								+ type.toLowerCase()
								+ "_trng_exp_var_mapping_history where date(updated_date) > ? and service_id = ? and game_id = ? order by updated_date asc)aa group by agent_org_id) hist on var.agent_org_id=hist.agent_org_id and var.service_id = hist.service_id and var.game_id=hist.game_id  where var.service_id = ? and var.game_id = ?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select ifnull(training_exp_extra,0.0) value from st_lms_agent_default_"
								+ type.toLowerCase()
								+ "_training_exp where service_id = ?  and game_id = ?) dtr;");
			} else {
				throw new LMSException("IN Valid Type" + type);
			}
			statement.setString(1, expDate);
			statement.setInt(2, serviceId);
			statement.setInt(3, gameNo);
			statement.setInt(4, serviceId);
			statement.setInt(5, gameNo);
			statement.setInt(6, serviceId);
			statement.setInt(7, gameNo);

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				agtExtraTrngExpMap.put(resultSet.getInt("organization_id"), resultSet.getDouble("extra_train_exp"));
			}
		} catch (SQLException e) {
			logger.error("SQL Exception", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception", e);
			// e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return agtExtraTrngExpMap;
	}
	
	public void updateAllAgentData(String startDate, String endDate, String trngExpType, UserInfoBean userBean, int serviceId, int gameNo, Connection con) {
		Statement statement = null;
		Timestamp updTime = new Timestamp(new Date().getTime());
		try {
			statement = con.createStatement();
			String selectQry = "select id,date,agent_org_id,training_exp_amt+extra_training_exp_amt training_exp_amt,incentive_per,incentive_amt,updated_mode from st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp where date>='" + startDate + "' and date<='" + endDate + "' and service_id = " + serviceId + " and status!='DONE'";	
			ResultSet rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				if(rs.getDouble("training_exp_amt") != 0.0)
					updateTrngExpAgentDataIW(updTime, startDate, endDate, rs.getInt("id"), rs.getInt("agent_org_id"), rs.getDouble("training_exp_amt"), userBean, "MANUAL", trngExpType, gameNo, serviceId, con);
				if(rs.getDouble("incentive_amt") > 0.00){
					updateAgentDataIWIncentive(updTime, startDate, endDate, rs.getInt("id"), rs.getInt("agent_org_id"), rs.getDouble("incentive_amt"), userBean, "MANUAL", trngExpType, gameNo, serviceId, con);
				}
				updateApprovalStatus(rs.getInt("id"),con,trngExpType.toLowerCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(statement);
		}
	}
	
	public void updateAllAgentData(String startDate, String endDate, String trngExpType, UserInfoBean userBean, int serviceId, int gameNo) {
		Connection con = DBConnect.getConnection();
		try{
			con.setAutoCommit(false);
			updateAllAgentData(startDate, endDate, trngExpType, userBean, serviceId, gameNo, con);
			con.commit();
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(con);
		}
		
	}

	public Map<Integer, SalePwtReportsBean> drawSaleAgentWise(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			String indGameQry = "(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
			String gameQry = "select game_nbr from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_nbr")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id group by parent_id) saleTlb on saleTlb.parent_id=om.organization_id");

			pstmt = con.prepareStatement(saleQry.toString());

			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> drawSaleAgentWiseNew(
			Timestamp startDate, Timestamp endDate,int gameNo) throws SQLException {
		Connection con = DBConnect.getConnection();
		//PreparedStatement pstmt = null;
		Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			String indGameQry = "select netRetSale.organization_id,netRetSale.name,(ifnull(netRetSale.mrpAmt,0.0)-ifnull(netRetSaleRef.mrpAmt,0.0))mrpAmt,(ifnull(netRetSale.netAmt,0.0)-ifnull(netRetSaleRef.netAmt,0.0))netAmt from(select om.organization_id,om.name,ifnull(sale.mrpAmt,0.0) mrpAmt,ifnull(sale.netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale on om.organization_id=sale.retailer_org_id where om.organization_type='RETAILER') netRetSale inner join (select om.organization_id,om.name,ifnull(saleRet.mrpAmtRet,0.0) mrpAmt,ifnull(saleRet.netAmtRet,0.0) netAmt from st_lms_organization_master om left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on om.organization_id=saleRet.retailer_org_id where om.organization_type='RETAILER') netRetSaleRef on netRetSale.organization_id=netRetSaleRef.organization_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			
			
			

			
				saleQry.append(indGameQry.replaceAll("\\*", String.valueOf(gameNo)));
			
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTbl group by organization_id) saleTbl where saleTbl.organization_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id");

			//pstmt = con.prepareStatement(saleQry.toString());
			pstmt = con.createStatement();

			logger.info("----Agent new Qry---" + saleQry.toString());
			//rs = pstmt.executeQuery();
			rs = pstmt.executeQuery(saleQry.toString());
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> iwSaleAgentWiseNew(Timestamp startDate, Timestamp endDate, int gameNo) throws SQLException {
		Connection con = DBConnect.getConnection();
		Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			// String indGameQry = "select netRetSale.organization_id,netRetSale.name,(ifnull(netRetSale.mrpAmt,0.0)-ifnull(netRetSaleRef.mrpAmt,0.0))mrpAmt,(ifnull(netRetSale.netAmt,0.0)-ifnull(netRetSaleRef.netAmt,0.0))netAmt from(select om.organization_id,om.name,ifnull(sale.mrpAmt,0.0) mrpAmt,ifnull(sale.netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale where game_id = "+gameNo+" transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>='"
			// + startDate
			// + "' and transaction_date<='"
			// + endDate
			// + "' )group by retailer_org_id) sale on om.organization_id=sale.retailer_org_id where om.organization_type='RETAILER') netRetSale inner join (select om.organization_id,om.name,ifnull(saleRet.mrpAmtRet,0.0) mrpAmt,ifnull(saleRet.netAmtRet,0.0) netAmt from st_lms_organization_master om left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_iw_ret_sale_refund where game_id = "+gameNo+" transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and transaction_date>='"
			// + startDate
			// + "' and transaction_date<='"
			// + endDate
			// + "' )group by retailer_org_id) saleRet on om.organization_id=saleRet.retailer_org_id where om.organization_type='RETAILER') netRetSaleRef on netRetSale.organization_id=netRetSaleRef.organization_id union all ";

			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(select netRetSale.organization_id,netRetSale.name,(ifnull(netRetSale.mrpAmt,0.0)-ifnull(netRetSaleRef.mrpAmt,0.0))mrpAmt,(ifnull(netRetSale.netAmt,0.0)-ifnull(netRetSaleRef.netAmt,0.0))netAmt from(select om.organization_id,om.name,ifnull(sale.mrpAmt,0.0) mrpAmt,ifnull(sale.netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale where game_id = ")
					.append(gameNo)
					.append(" and transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>='")
					.append(startDate)
					.append("' and transaction_date<='")
					.append(endDate)
					.append("' )group by retailer_org_id) sale on om.organization_id=sale.retailer_org_id where om.organization_type='RETAILER') netRetSale inner join (select om.organization_id,om.name,ifnull(saleRet.mrpAmtRet,0.0) mrpAmt,ifnull(saleRet.netAmtRet,0.0) netAmt from st_lms_organization_master om left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_iw_ret_sale_refund where game_id = ")
					.append(gameNo)
					.append(" and transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and transaction_date>='")
					.append(startDate)
					.append("' and transaction_date<='")
					.append(endDate)
					.append("' )group by retailer_org_id) saleRet on om.organization_id=saleRet.retailer_org_id where om.organization_type='RETAILER') netRetSaleRef on netRetSale.organization_id=netRetSaleRef.organization_id) saleTbl group by organization_id) saleTbl where saleTbl.organization_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id");

			pstmt = con.createStatement();

			logger.info("----Agent new Qry---" + saleQry.toString());
			// rs = pstmt.executeQuery();
			rs = pstmt.executeQuery(saleQry.toString());
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> drawSaleAgentWiseTimeWiseNew(
			Timestamp startDate, Timestamp endDate,int gameNo) throws SQLException, LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		//Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			/*String indGameQry = "select netRetSale.organization_id,netRetSale.name,(ifnull(netRetSale.mrpAmt,0.0)-ifnull(netRetSaleRef.mrpAmt,0.0))mrpAmt,(ifnull(netRetSale.netAmt,0.0)-ifnull(netRetSaleRef.netAmt,0.0))netAmt from(select om.organization_id,om.name,ifnull(sale.mrpAmt,0.0) mrpAmt,ifnull(sale.netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale on om.organization_id=sale.retailer_org_id where om.organization_type='RETAILER') netRetSale inner join (select om.organization_id,om.name,ifnull(saleRet.mrpAmtRet,0.0) mrpAmt,ifnull(saleRet.netAmtRet,0.0) netAmt from st_lms_organization_master om left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on om.organization_id=saleRet.retailer_org_id where om.organization_type='RETAILER') netRetSaleRef on netRetSale.organization_id=netRetSaleRef.organization_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			
			
			

			
				saleQry.append(indGameQry.replaceAll("\\*", String.valueOf(gameNo)));
			
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTbl group by organization_id) saleTbl where saleTbl.organization_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id");
*/
			pstmt = con.prepareStatement("select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_? where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=? and transaction_id not in(select ref_transaction_id from st_dg_ret_sale_refund_? refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and date(transaction_date)=date(?)))group by retailer_org_id) saleTbl where saleTbl.retailer_org_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id where om.organization_type='AGENT'");
			//pstmt = con.createStatement();
			pstmt.setInt(1, gameNo);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);
			pstmt.setInt(4, gameNo);
			pstmt.setTimestamp(5, startDate);
			logger.info("----Agent Time wise Qry---" + pstmt);
			//rs = pstmt.executeQuery();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
			
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> iwSaleAgentWiseTimeWiseNew(Timestamp startDate, Timestamp endDate, int gameNo) throws SQLException, LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		// Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			pstmt = con
					.prepareStatement("select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale where game_id = ? and transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>=? and transaction_date<=? and transaction_id not in(select sale_ref_transaction_id from st_iw_ret_sale_refund refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where refund.game_id = ? and  transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and date(rtm.transaction_date)=date(?)))group by retailer_org_id) saleTbl where saleTbl.retailer_org_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id where om.organization_type='AGENT';");
			// pstmt = con.createStatement();
			pstmt.setInt(1, gameNo);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);
			pstmt.setInt(4, gameNo);
			pstmt.setTimestamp(5, startDate);
			logger.info("----Agent Time wise Qry---" + pstmt);
			// rs = pstmt.executeQuery();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> drawSaleAgentWiseWeeklyTimeWiseNew(Timestamp startDate,Timestamp toDate,
			Timestamp weekStartDate, Timestamp weekEndDate,int gameNo) throws SQLException, LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		//Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			/*String indGameQry = "select netRetSale.organization_id,netRetSale.name,(ifnull(netRetSale.mrpAmt,0.0)-ifnull(netRetSaleRef.mrpAmt,0.0))mrpAmt,(ifnull(netRetSale.netAmt,0.0)-ifnull(netRetSaleRef.netAmt,0.0))netAmt from(select om.organization_id,om.name,ifnull(sale.mrpAmt,0.0) mrpAmt,ifnull(sale.netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) sale on om.organization_id=sale.retailer_org_id where om.organization_type='RETAILER') netRetSale inner join (select om.organization_id,om.name,ifnull(saleRet.mrpAmtRet,0.0) mrpAmt,ifnull(saleRet.netAmtRet,0.0) netAmt from st_lms_organization_master om left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' )group by retailer_org_id) saleRet on om.organization_id=saleRet.retailer_org_id where om.organization_type='RETAILER') netRetSaleRef on netRetSale.organization_id=netRetSaleRef.organization_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(");
			
			
			

			
				saleQry.append(indGameQry.replaceAll("\\*", String.valueOf(gameNo)));
			
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry
					.append(") saleTbl group by organization_id) saleTbl where saleTbl.organization_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id");
*/
			pstmt = con.prepareStatement(" select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_? where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>=? and transaction_date<=? and transaction_id  not in(select ref_transaction_id from st_dg_ret_sale_refund_? refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and date(transaction_date)>=date(?) and date(transaction_date)<=date(?)))group by retailer_org_id) saleTbl where saleTbl.retailer_org_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id where om.organization_type='AGENT'");
			//pstmt = con.createStatement();
			pstmt.setInt(1, gameNo);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, toDate);
			pstmt.setInt(4, gameNo);
			pstmt.setTimestamp(5, weekStartDate);
			pstmt.setTimestamp(6, weekEndDate);
			logger.info("----Agent Time wise Qry---" + pstmt);
			//rs = pstmt.executeQuery();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
			
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public Map<Integer, SalePwtReportsBean> iwSaleAgentWiseWeeklyTimeWiseNew(Timestamp startDate, Timestamp toDate, Timestamp weekStartDate, Timestamp weekEndDate, int gameNo) throws SQLException, LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		// Statement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		Map<Integer, SalePwtReportsBean> reportMap = new HashMap<Integer, SalePwtReportsBean>();
		try {
			pstmt = con
					.prepareStatement("select om.organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om left outer join(select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master oms,(select retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale where game_id = ? and transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>=? and transaction_date<=? and transaction_id  not in(select sale_ref_transaction_id from st_iw_ret_sale_refund refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where refund.game_id = ? and transaction_type in('IW_REFUND_CANCEL','IW_REFUND_FAILED') and date(rtm.transaction_date)>=date(?) and date(rtm.transaction_date)<=date(?)))group by retailer_org_id) saleTbl where saleTbl.retailer_org_id = oms.organization_id group by parent_id)saleTlb on saleTlb.parent_id=om.organization_id where om.organization_type='AGENT';");
			pstmt.setInt(1, gameNo);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, toDate);
			pstmt.setInt(4, gameNo);
			pstmt.setTimestamp(5, weekStartDate);
			pstmt.setTimestamp(6, weekEndDate);
			logger.info("---- IW Agent Time wise Qry---" + pstmt);
			// rs = pstmt.executeQuery();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("name"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportMap.put(rs.getInt("organization_id"), reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			con.close();
		}
		return reportMap;
	}
	
	public void updateTrngExpAgentDataIW(Timestamp updateTime, String fromDate, String toDate, int taskId, int agtOrgId, double trngExpAmt, UserInfoBean userBean, String UpdateMode, String trngExpType,int gameNo, int serviceId, Connection con) throws Exception {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		String reason="";
		String transactionType = trngExpAmt > 0.0 ? "CR_NOTE_CASH":"DR_NOTE_CASH";
		double unsignTrngAmt =  trngExpAmt > 0.0 ? trngExpAmt : (-1) * trngExpAmt;
		if ("AUTO".equalsIgnoreCase(UpdateMode)) {
			HttpServletRequest request = new ManualRequest();
			request.setAttribute("code", "MGMT");
			request.setAttribute("interfaceType", "WEB");
			ServletActionContext.setRequest(request);
		}
		String updateQry = null;
		try {
			updateQry = "update st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp set credit_note_number=?, done_by_user_id=?, updated_date=?, updated_mode=? where service_id = " + serviceId + " and agent_org_id=" + agtOrgId + " and id=" + taskId;	

			/*
			 * String updateBoRecieptGenMapping = QueryManager
			 * .updateST5BOReceiptGenMapping();
			 */
			// insert into LMS transaction master
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			pstmt1 = con.prepareStatement(queryLMSTrans);
			pstmt1.setString(1, "BO");
			pstmt1.executeUpdate();
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			// int transaction_id = -1;
			long transaction_id =-1;
			if (rs1.next()) {
				transaction_id = rs1.getLong(1);
			} else {
				throw new LMSException("Transaction id is not generated ");
			}
			
			String updateBoMaster = QueryManager.insertInBOTransactionMaster();
			pstmt1 = con.prepareStatement(updateBoMaster);
			pstmt1.setLong(1, transaction_id);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setString(4, "AGENT");
			pstmt1.setInt(5, agtOrgId);
			pstmt1.setTimestamp(6, updateTime);
			pstmt1.setString(7,transactionType);
			pstmt1.executeUpdate();
			System.out.println("insertInBOTransactionMaster>>>" + pstmt1);

			String updateCreditNote = "insert into st_lms_bo_credit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)";
			String updateDebitNote = "insert into st_lms_bo_debit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)" ;
			String remarks = null;
			if ("weekly".equalsIgnoreCase(trngExpType)) {
				remarks = "Given as Weekly Training expense for the week from "
						+ fromDate.split(" ")[0] + " to "
						+ toDate.split(" ")[0];
			} else {
				remarks = "Given as Daily Training expense for the day "
						+ fromDate.split(" ")[0];
			}
			if(trngExpAmt > 0.0 ){
				pstmt2 = con.prepareStatement(updateCreditNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="CR_WEEKLY_EXP";
				else
					reason="CR_DAILY_EXP";
			}else{
				pstmt2 = con.prepareStatement(updateDebitNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="DR_WEEKLY_TE_RETURN";
				else
					reason="DR_DAILY_TE_RETURN";
			}
			pstmt2.setLong(1, transaction_id);
			pstmt2.setInt(2, agtOrgId);
			pstmt2.setDouble(3, unsignTrngAmt);
			pstmt2.setString(4, transactionType);
			pstmt2.setString(5, remarks);
			pstmt2.setString(6, reason);
			System.out.println("st_lms_bo_credit/debit_note>>>" + pstmt2);
			pstmt2.executeUpdate();

			String fetchCreditNoteLastEntryQuery = "SELECT * from st_lms_bo_receipts where receipt_type=? or receipt_type=? ORDER BY generated_id DESC LIMIT 1";
			pstmt4 = con.prepareStatement(fetchCreditNoteLastEntryQuery);
			if(trngExpAmt > 0.0){
				pstmt4.setString(1, "CR_NOTE_CASH");
				pstmt4.setString(2, "CR_NOTE");
			}else{
				pstmt4.setString(1, "DR_NOTE_CASH");
				pstmt4.setString(2, "DR_NOTE");
			}
			ResultSet recieptRs = pstmt4.executeQuery();
			String lastRecieptNoGenerated = null;
			if (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}
			// create receipt no.
			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(transactionType, lastRecieptNoGenerated, userBean.getUserType());

			// insert in receipt master table
			pstmt2 = con.prepareStatement(QueryManager.insertInReceiptMaster());
			pstmt2.setString(1, "BO");
			pstmt2.executeUpdate();
			ResultSet rs2 = pstmt2.getGeneratedKeys();
			int id = -1;
			if (rs2.next()) {
				id = rs2.getInt(1);
			} else {
				throw new LMSException("Error in reciept generation");
			}

			// insert into BO receipt table
			pstmt2 = con.prepareStatement(QueryManager.insertInBOReceipts());
			pstmt2.setInt(1, id);
			pstmt2.setString(2, transactionType);
			pstmt2.setInt(3, agtOrgId);
			pstmt2.setString(4, "AGENT");
			pstmt2.setString(5, autoGeneRecieptNo);
			pstmt2.setTimestamp(6, Util.getCurrentTimeStamp());
			System.out.println("insertInBOReceipts>>>" + pstmt2);
			pstmt2.executeUpdate();

			// insert into receipt transaction mapping
			pstmt3 = con.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			pstmt3.setInt(1, id);
			pstmt3.setLong(2, transaction_id);
			pstmt3.executeUpdate();

			// update organization account details
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(unsignTrngAmt, "TRANSACTION", transactionType, agtOrgId,
					0, "AGENT", 0, con);
			if(!isValid)
				throw new LMSException();
			
			
			/*OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					transactionType, unsignTrngAmt, con);*/

			// update training expense table

			pstmt5 = con.prepareStatement(updateQry);
			pstmt5.setString(1, autoGeneRecieptNo);
			pstmt5.setInt(2, userBean.getUserId());
			pstmt5.setTimestamp(3, updateTime);
			pstmt5.setString(4, UpdateMode);
			System.out.println("updating training Expense table.>>>>" + pstmt5);
			if (pstmt5.executeUpdate() < 1) {
				throw new LMSException("training expense table not updated");
			}
			// following code is commented temp....
			/*
			 * GraphReportHelper graphReportHelper = new GraphReportHelper();
			 * graphReportHelper.createTextReportBO(id, parentOrgName,
			 * userOrgID, (String) session.getAttribute("ROOT_PATH"));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * This method is for updating IW Incentive agentwise.
	 * @param updateTime
	 * @param fromDate
	 * @param toDate
	 * @param taskId
	 * @param agtOrgId
	 * @param trngExpAmt
	 * @param userBean
	 * @param UpdateMode
	 * @param trngExpType
	 * @param gameNo
	 * @param serviceId
	 * @param con
	 * @throws Exception
	 * @author Rishi
	 */
	public void updateAgentDataIWIncentive(Timestamp updateTime, String fromDate, String toDate, int taskId, int agtOrgId, double trngExpAmt, UserInfoBean userBean, String UpdateMode, String trngExpType,int gameNo, int serviceId, Connection con) throws Exception {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		String reason="";
		String transactionType = trngExpAmt > 0.0 ? "CR_NOTE_CASH":"DR_NOTE_CASH";
		double unsignTrngAmt =  trngExpAmt > 0.0 ? trngExpAmt : (-1) * trngExpAmt;
		if ("AUTO".equalsIgnoreCase(UpdateMode)) {
			HttpServletRequest request = new ManualRequest();
			request.setAttribute("code", "MGMT");
			request.setAttribute("interfaceType", "WEB");
			ServletActionContext.setRequest(request);
		}
		String updateQry = null;
		try {
			//statement = con.createStatement();
			updateQry = "update st_lms_agent_" + trngExpType.toLowerCase() + "_training_exp set incentive_credit_note_number=?, done_by_user_id=?, updated_date=?, updated_mode=? where service_id = " + serviceId + " and agent_org_id=" + agtOrgId + " and id=" + taskId;

			/*
			 * String updateBoRecieptGenMapping = QueryManager
			 * .updateST5BOReceiptGenMapping();
			 */
			// insert into LMS transaction master
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			pstmt1 = con.prepareStatement(queryLMSTrans);
			pstmt1.setString(1, "BO");
			pstmt1.executeUpdate();
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			// int transaction_id = -1;
			long transaction_id =-1;
			if (rs1.next()) {
				transaction_id = rs1.getLong(1);
			} else {
				throw new LMSException("Transaction id is not generated ");
			}
			
			String updateBoMaster = QueryManager.insertInBOTransactionMaster();
			pstmt1 = con.prepareStatement(updateBoMaster);
			pstmt1.setLong(1, transaction_id);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setString(4, "AGENT");
			pstmt1.setInt(5, agtOrgId);
			pstmt1.setTimestamp(6, updateTime);
			pstmt1.setString(7,transactionType);
			pstmt1.executeUpdate();
			System.out.println("insertInBOTransactionMaster>>>" + pstmt1);

			String updateCreditNote = "insert into st_lms_bo_credit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)";
			String updateDebitNote = "insert into st_lms_bo_debit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason) values(?,?,?,?,?,?)" ;
			String remarks = null;
			if ("weekly".equalsIgnoreCase(trngExpType)) {
				remarks = "Given as Weekly Incentive for the week from "
						+ fromDate.split(" ")[0] + " to "
						+ toDate.split(" ")[0];
			} else {
				remarks = "Given as Daily Incentive for the day "
						+ fromDate.split(" ")[0];
			}
			if(trngExpAmt > 0.0 ){
				pstmt2 = con.prepareStatement(updateCreditNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="CR_WEEKLY_INCENTIVE";
				else
					reason="CR_DAILY_INCENTIVE";
			}else{
				pstmt2 = con.prepareStatement(updateDebitNote);
				if(trngExpType.equalsIgnoreCase("WEEKLY"))
					reason="DR_WEEKLY_TE_RETURN";
				else
					reason="DR_DAILY_TE_RETURN";
			}
			pstmt2.setLong(1, transaction_id);
			pstmt2.setInt(2, agtOrgId);
			pstmt2.setDouble(3, unsignTrngAmt);
			pstmt2.setString(4, transactionType);
			pstmt2.setString(5, remarks);
			pstmt2.setString(6, reason);
			System.out.println("st_lms_bo_credit/debit_note>>>" + pstmt2);
			pstmt2.executeUpdate();

			String fetchCreditNoteLastEntryQuery = "SELECT * from st_lms_bo_receipts where receipt_type=? or receipt_type=? ORDER BY generated_id DESC LIMIT 1";
			pstmt4 = con.prepareStatement(fetchCreditNoteLastEntryQuery);
			if(trngExpAmt > 0.0){
				pstmt4.setString(1, "CR_NOTE_CASH");
				pstmt4.setString(2, "CR_NOTE");
			}else{
				pstmt4.setString(1, "DR_NOTE_CASH");
				pstmt4.setString(2, "DR_NOTE");
			}
			ResultSet recieptRs = pstmt4.executeQuery();
			String lastRecieptNoGenerated = null;
			if (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}
			// create receipt no.
			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(transactionType, lastRecieptNoGenerated, userBean.getUserType());

			// insert in receipt master table
			pstmt2 = con.prepareStatement(QueryManager.insertInReceiptMaster());
			pstmt2.setString(1, "BO");
			pstmt2.executeUpdate();
			ResultSet rs2 = pstmt2.getGeneratedKeys();
			int id = -1;
			if (rs2.next()) {
				id = rs2.getInt(1);
			} else {
				throw new LMSException("Error in reciept generation");
			}

			// insert into BO receipt table
			pstmt2 = con.prepareStatement(QueryManager.insertInBOReceipts());
			pstmt2.setInt(1, id);
			pstmt2.setString(2, transactionType);
			pstmt2.setInt(3, agtOrgId);
			pstmt2.setString(4, "AGENT");
			pstmt2.setString(5, autoGeneRecieptNo);
			pstmt2.setTimestamp(6, Util.getCurrentTimeStamp());
			System.out.println("insertInBOReceipts>>>" + pstmt2);
			pstmt2.executeUpdate();

			// insert into receipt transaction mapping
			pstmt3 = con.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			pstmt3.setInt(1, id);
			pstmt3.setLong(2, transaction_id);
			pstmt3.executeUpdate();

			// update organization account details
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(unsignTrngAmt, "TRANSACTION", transactionType, agtOrgId,
					0, "AGENT", 0, con);
			if(!isValid)
				throw new LMSException();
			
			
			/*OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					transactionType, unsignTrngAmt, con);*/

			// update training expense table

			pstmt5 = con.prepareStatement(updateQry);
			pstmt5.setString(1, autoGeneRecieptNo);
			pstmt5.setInt(2, userBean.getUserId());
			pstmt5.setTimestamp(3, updateTime);
			pstmt5.setString(4, UpdateMode);
			System.out.println("updating training Expense table.>>>>" + pstmt5);
			if (pstmt5.executeUpdate() < 1) {
				throw new LMSException("training expense table not updated");
			}
			// following code is commented temp....
			/*
			 * GraphReportHelper graphReportHelper = new GraphReportHelper();
			 * graphReportHelper.createTextReportBO(id, parentOrgName,
			 * userOrgID, (String) session.getAttribute("ROOT_PATH"));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method is for updating approval status after generating CR Note Number.
	 * @param taskId
	 * @param con
	 * @throws LMSException
	 * @author Rishi
	 */
	public void updateApprovalStatus(int taskId,Connection con, String trngExpType) throws LMSException{
		String query = null;
		PreparedStatement pstmt = null;
		int isUpdated = 0;
		try{
			logger.info("In update Approval");
			query = "Update st_lms_agent_"+trngExpType+"_training_exp SET status = 'DONE' Where id=?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, taskId);
			isUpdated = pstmt.executeUpdate();
			if(isUpdated == 0){
				throw new LMSException();
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			DBConnect.closeResource(pstmt);
		}
	}
	
}
