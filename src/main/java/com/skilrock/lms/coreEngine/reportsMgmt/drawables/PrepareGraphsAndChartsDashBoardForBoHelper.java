package com.skilrock.lms.coreEngine.reportsMgmt.drawables;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DrawDataBean;

public class PrepareGraphsAndChartsDashBoardForBoHelper/* implements Runnable */{

	Log logger = LogFactory
			.getLog(PrepareGraphsAndChartsDashBoardForBoHelper.class);

	//private int type;
	private ResultSet rs = null;
	private Statement stmt = null;
	private Connection con = null;
	private PreparedStatement pstmt = null;

	public PrepareGraphsAndChartsDashBoardForBoHelper(/*int type*/) {
		String filePath = System.getProperty("jboss.server.home.dir")+"/deploy"+com.skilrock.lms.web.common.drawables.CommonMethods.path;
		com.skilrock.lms.web.common.drawables.CommonMethods.DeleteFiles(filePath, false, "");
		prepDataForUserGraphForRendering(filePath);
		prepDataForSalesGraphForRendering(filePath);
		prepDataForDrawWiseGraphForRendering(filePath);
		prepDataForRetailerStatusGraphForRendering(filePath);
		prepDataForCshColChartForRendering(filePath);
	}

	private void prepDataForCshColChartForRendering(String filePath) {

		String graphsDates = null;
		String[] dateArray = null;
		boolean isArchTablesReq = false;
		LinkedHashMap<String, String> gameMap = null;
		String fileName = "DASHBOARDCASHCOLLECTIONDRILLDOWN";
		try {
			graphsDates = com.skilrock.lms.web.common.drawables.CommonMethods.perpDateForDashBoardChartsAndGraphs(true, false, -1, 7,false);
			dateArray = graphsDates.split("#");
			isArchTablesReq = com.skilrock.lms.web.common.drawables.CommonMethods.isArchTablesRequired(dateArray[dateArray.length - 1].substring(0, 10), CommonMethods.getLastArchDate());
			gameMap = new LinkedHashMap<String, String>();
			gameMap.put("", com.skilrock.lms.web.common.drawables.CommonMethods.perpareCaptionsForDrawables("Cash Collection Analytics","","Last 7 Days", "Collection (In " +Utility.getPropertyValue("CURRENCY_SYMBOL") + ")"));
			prepareCashCollectionWeekly(dateArray,isArchTablesReq, gameMap);
			com.skilrock.lms.web.common.drawables.CommonMethods.prepareCSVFile(fileName, gameMap, filePath);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepDataForSalesGraphForRendering(String filePath) {
		String graphsDates = null;
		String[] dateArray = null;
		boolean isArchTablesReq = false;
		LinkedHashMap<String, String> gameMap = null;
		String fileName = "DASHBOARDSALESCOLLECTIONCOLUMNSTACKED";
		try {
			graphsDates = com.skilrock.lms.web.common.drawables.CommonMethods.perpDateForDashBoardChartsAndGraphs(true, false, -1, 7,false);
			dateArray = graphsDates.split("#");
			isArchTablesReq = com.skilrock.lms.web.common.drawables.CommonMethods.isArchTablesRequired(dateArray[dateArray.length - 1].substring(0, 10), CommonMethods.getLastArchDate());
			List<String> list = Arrays.asList(dateArray);
			Collections.reverse(list);
			dateArray = (String[]) list.toArray();
			gameMap = new LinkedHashMap<String, String>();
			gameMap.put("", com.skilrock.lms.web.common.drawables.CommonMethods.perpareCaptionsForDrawables("Game Wise Analytics", "","Last 7 Days","Amount (In " +Utility.getPropertyValue("CURRENCY_SYMBOL") + ")"));
			prepareSalesDailyString(dateArray[0],dateArray[dateArray.length - 1].substring(0, 10).concat(" 23:59:59"), isArchTablesReq, dateArray, gameMap);
			com.skilrock.lms.web.common.drawables.CommonMethods.prepareCSVFile(fileName, gameMap, filePath);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepDataForUserGraphForRendering(String filePath) {

		LinkedHashMap<String, String> gameMap = null;
		String fileName = "DASHBOARDUSERPIE";
		try {
			gameMap = new LinkedHashMap<String, String>();
			gameMap.put("", com.skilrock.lms.web.common.drawables.CommonMethods.perpareCaptionsForDrawables("LMS Tier Users Analytics", "", "","Count"));
			prepareUserWiseAnalytics(gameMap);
			com.skilrock.lms.web.common.drawables.CommonMethods.prepareCSVFile(fileName, gameMap, filePath);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepDataForDrawWiseGraphForRendering(String filePath) {
		
		LinkedHashMap<String, String> gameMap = null;
		String fileName = "DASHBOARDDRAWWISECOLLECTIONLINECHART";
		try {
			gameMap = new LinkedHashMap<String, String>();
			gameMap.put("", com.skilrock.lms.web.common.drawables.CommonMethods.perpareCaptionsForDrawables("Draw Wise Analytics", "", "Last 7 Draws","Amount (In " +Utility.getPropertyValue("CURRENCY_SYMBOL") + ")"));
			prepareDrawWiseAnalyticsString(7, gameMap);
			com.skilrock.lms.web.common.drawables.CommonMethods.prepareCSVFile(fileName, gameMap, filePath);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepDataForRetailerStatusGraphForRendering(String filePath) {
		String graphsDates = null;
		String[] dateArray = null;
		LinkedHashMap<String, String> gameMap = null;
		String fileName = "DASHBOARDRETSTATUSCOLUMNSTACKED";
		try {
			graphsDates = com.skilrock.lms.web.common.drawables.CommonMethods.perpDateForDashBoardChartsAndGraphs(true, false, -7, 7,true);
			List<String> list = Arrays.asList(graphsDates.split("#"));
			Collections.reverse(list);
			dateArray = (String[]) list.toArray();
			gameMap = new LinkedHashMap<String, String>();
			gameMap.put("", com.skilrock.lms.web.common.drawables.CommonMethods.perpareCaptionsForDrawables("Retailer Status Analytics","", "Last 7 Saturdays", "Count"));
			prepareRetailerStatusGraphString(dateArray, gameMap);
			com.skilrock.lms.web.common.drawables.CommonMethods.prepareCSVFile(fileName, gameMap, filePath);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepareCashCollectionWeekly(String[] dateArray, boolean isArchTablesReq,LinkedHashMap<String, String> gameMap) {

		
		String endDate = null;
		String startDate = null;
		StringBuilder mainQuery = null;
		StringBuilder cashBuilder = new StringBuilder();
		StringBuilder chequeBuilder = new StringBuilder();
		StringBuilder cheqBounceBuilder = new StringBuilder();
		StringBuilder debitBuilder = new StringBuilder();
		StringBuilder creditBuilder = new StringBuilder();
		StringBuilder bankBuilder = new StringBuilder();
		StringBuilder categoryBuilder = new StringBuilder();
		try {
			mainQuery = new StringBuilder(
					"select  ifnull(sum(cash) ,0.0) cash, ifnull(sum(cheque),0.0) cheque, ifnull(sum(bounce),0.0) bounce, ifnull(sum(debit),0.0) debit, ifnull(sum(credit),0.0) credit,ifnull(sum(bank),0.0) bank ,ifnull(sum(((cash+cheque+credit+bank)-(bounce+debit))),0.0)  netAmount from ( select ifnull(sum(cash.amount),0) cash, 0 cheque, 0 bounce, 0 debit, 0 credit,0 bank from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where ( btm.transaction_date >=? and btm.transaction_date <=?) and cash.transaction_id=btm.transaction_id  union all  select 0 cash, ifnull(sum(chq.cheque_amt),0) cheque, 0 bounce, 0 debit, 0 credit,0 bank from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where  ( btm.transaction_date >=? and btm.transaction_date <=?) and chq.transaction_type IN ('CHEQUE','CLOSED')and chq.transaction_id=btm.transaction_id  union all select 0 cash,0 cheque,ifnull(sum(chq.cheque_amt),0) bounce, 0 debit, 0 credit,0 bank from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and ( btm.transaction_date >=? and btm.transaction_date <=?) and chq.transaction_id=btm.transaction_id  union all select 0 cash , 0 cheque, 0 bounce, ifnull(sum(bo.amount),0) debit, 0 credit,0 bank  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and ( btm.transaction_date >=? and btm.transaction_date <=?)  union all select 0 cash , 0 cheque, 0 bounce, 0 debit,ifnull(sum(bo.amount),0) credit,0 bank  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and ( btm.transaction_date >=? and btm.transaction_date <=?)  union all select 0 cash , 0 cheque, 0 bounce, 0 debit, 0 credit,ifnull(sum(bank.amount),0) bank from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where btm.transaction_id=bank.transaction_id and  ( btm.transaction_date >=? and btm.transaction_date <=?))  bank ");
			con = DBConnect.getConnection();
			if (isArchTablesReq)
				pstmt = con
						.prepareStatement("select sum(cash) cash,sum(cheque) cheque, sum(bounce) bounce, sum(debit) debit, sum(credit) credit,sum(bank) bank ,sum(((cash+cheque+credit+bank)-(bounce+debit)))  netAmount  from ("
								.concat(mainQuery.toString())
								.concat(
										" union all select  ifnull(sum(cash_amt),0.0) cash,  ifnull(sum(cheque_amt),0.0) cheque,  ifnull(sum(cheque_bounce_amt),0.0) bounce,  ifnull(sum(debit_note),0.0) debit,   ifnull(sum(credit_note),0.0) credit,  ifnull(sum(bank_deposit),0.0) bank, ifnull(sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))),0.0)  netAmount from st_rep_bo_payments where finaldate >=? and finaldate <=? ) cash"));
			else
				pstmt = con.prepareStatement(mainQuery.toString());
			
			int count = dateArray.length;
			gameMap.put("",gameMap.get("").replace("7",count+""));
			while (count != 0) {
				count--;
				if (count == 0) {
					startDate = dateArray[count];
					endDate = dateArray[count].substring(0, 11).concat(
							"23:59:59");
				} else {
					startDate = dateArray[count];
					endDate = dateArray[count].substring(0, 11).concat(
							"23:59:59");
				}

				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				pstmt.setString(3, startDate);
				pstmt.setString(4, endDate);
				pstmt.setString(5, startDate);
				pstmt.setString(6, endDate);
				pstmt.setString(7, startDate);
				pstmt.setString(8, endDate);
				pstmt.setString(9, startDate);
				pstmt.setString(10, endDate);
				pstmt.setString(11, startDate);
				pstmt.setString(12, endDate);

				if (isArchTablesReq) {
					pstmt.setString(13, startDate);
					pstmt.setString(14, endDate);
				}

				rs = pstmt.executeQuery();

				
				if (rs.next()) {
					cashBuilder.append(rs.getString("cash")).append(",");
					chequeBuilder.append(rs.getString("cheque")).append(",");
					cheqBounceBuilder.append(rs.getString("bounce")).append(",");
					debitBuilder.append(rs.getString("debit")).append(",");
					creditBuilder.append(rs.getString("credit")).append(",");
					bankBuilder.append(rs.getString("bank")).append(",");
				} else {
					cashBuilder.append(0.0).append(",");
					chequeBuilder.append(0.0).append(",");
					cheqBounceBuilder.append(0.0).append(",");
					debitBuilder.append(0.0).append(",");
					creditBuilder.append(0.0).append(",");
					bankBuilder.append(0.0).append(",");

				}
				
				categoryBuilder.append(com.skilrock.lms.web.common.drawables.CommonMethods.getDateForDrawables(startDate)).append(",");
			}
			
			
			gameMap.put("categories", categoryBuilder.toString());
			gameMap.put("CASH", cashBuilder.toString());
			gameMap.put("CASH", cashBuilder.toString());
			gameMap.put("CHEQUE", chequeBuilder.toString());
			gameMap.put("BOUNCE", cheqBounceBuilder.toString());
			gameMap.put("DEBIT", debitBuilder.toString());
			gameMap.put("CREDIT", creditBuilder.toString());
			gameMap.put("BANK", bankBuilder.toString());
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		

	}


	private void prepareRetailerStatusGraphString(String datesArray[],
			LinkedHashMap<String, String> weeklyMap) {

		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder liveRetBuilder = new StringBuilder();
		StringBuilder noSaleRetBuilder = new StringBuilder();
		StringBuilder inactiveRetBuilder = new StringBuilder();
		StringBuilder terminatedRetBuilder = new StringBuilder();

		int totalActiveRets = 0;
		int totalInActiveRets = 0;
		int totalNoSaleRets = 0;

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery(" select date, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers  from st_lms_ret_activity_history where date in ("
							+ com.skilrock.lms.web.common.drawables.CommonMethods
									.getListFromData(Arrays.asList(datesArray))
									.replace("]", "").replace("[", "")
							+ ") order by date");
			int count = 0;
			while (rs.next()) {
				int LiveRet = 0;
				int NoSaleRet = 0;
				int InactiveRet = 0;
				int TerminatedRet = 0;
				if (datesArray[count].equals(rs.getString("date").replace(".0",
						""))) {
					LiveRet = rs.getInt("live_retailers");
					NoSaleRet = rs.getInt("noSale_retailers");
					InactiveRet = rs.getInt("inactive_retailers");
					TerminatedRet = rs.getInt("terminated_retailers");
				}

				totalActiveRets += LiveRet;
				totalNoSaleRets += NoSaleRet;
				totalInActiveRets += InactiveRet;

				categoryBuilder.append(com.skilrock.lms.web.common.drawables.CommonMethods.getDateForDrawables(datesArray[count])).append(",");
				liveRetBuilder.append(LiveRet).append(",");
				noSaleRetBuilder.append(NoSaleRet).append(",");
				inactiveRetBuilder.append(InactiveRet).append(",");
				terminatedRetBuilder.append(TerminatedRet).append(",");
				count++;
			}
			weeklyMap.put("",weeklyMap.get("").replace("7",count+""));
			weeklyMap.put("categories", categoryBuilder.toString());
			weeklyMap.put("LIVE", liveRetBuilder.toString());
			weeklyMap.put("IDLE", noSaleRetBuilder.toString());
			weeklyMap.put("INACTIVE", inactiveRetBuilder.toString());
			weeklyMap.put("TERMINATED", terminatedRetBuilder.toString());
			weeklyMap.put("SUMMARY", "");
			weeklyMap.put("Avg Live Ret",
					com.skilrock.lms.web.drawGames.common.Util
							.getBalInString(totalActiveRets / count)
							+ ",");
			weeklyMap.put("Avg Idle Ret",
					com.skilrock.lms.web.drawGames.common.Util
							.getBalInString(totalNoSaleRets / count)
							+ ",");
			weeklyMap.put("Avg Inactive Ret",
					com.skilrock.lms.web.drawGames.common.Util
							.getBalInString(totalInActiveRets / count)
							+ ",");

		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}

	}

	private void prepareDrawWiseAnalyticsString(int noOfDaysForReport,
			LinkedHashMap<String, String> gameMap) throws SQLException {

		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		DrawDataBean drawDataBean = null;
		DGConsolidateGameDataBean consolidateBean = null;

		int totalPwt = 0;
		int totalSale = 0;
		int totalClmd = 0;
		double pprAppender = 0.0;
		StringBuilder pwtBuilder = new StringBuilder();
		StringBuilder saleBuilder = new StringBuilder();
		StringBuilder clmdBuilder = new StringBuilder();
		StringBuilder categoryBuilder = new StringBuilder();

		try {
			drawDataBean = new DrawDataBean();
			drawDataBean.setMerchantId("ALL");
			drawDataBean.setDrawId(noOfDaysForReport);
			drawDataBean.setGameDevName(Utility.getPropertyValue("GAME_DEV_NAME_FOR_DRAW_WISE_GRAPH"));
			serReq = new ServiceRequest();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq
					.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_CONSOLIDATE_DATA_FOR_GRAPHS);
			serReq.setServiceData(drawDataBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();

			serResp = new ServiceResponse();
			serResp = delegate.getResponse(serReq);
			consolidateBean = new DGConsolidateGameDataBean();
			if (serResp.getIsSuccess()){
				String respString = serResp.getResponseData().toString();
				Type elementType = new TypeToken<DGConsolidateGameDataBean>(){}.getType();
			    consolidateBean = new Gson().fromJson(respString, elementType);
			
			 List<DGConsolidateDrawBean> drawDataBeanList = consolidateBean.getDrawDataBeanList();
			// Collections.reverse(drawDataBeanList);
			 
			int count = 0;
			for (DGConsolidateDrawBean dgConsolidateDrawBean : drawDataBeanList) {
				if(noOfDaysForReport == count)
					break;
				
				count++;
				double sale = dgConsolidateDrawBean.getLmsSaleBean()
						.getTotalSaleValue();
				double pwt = dgConsolidateDrawBean.getLmsSaleBean()
						.getTotalWinningAmount();
				double clmd = dgConsolidateDrawBean.getLmsSaleBean()
						.getClaimedAmount();

				totalSale += sale;
				totalPwt += pwt;
				totalClmd += clmd;

				categoryBuilder.append(
						com.skilrock.lms.web.common.drawables.CommonMethods
								.getDateForDrawables(dgConsolidateDrawBean
										.getDrawDateTime())).append(",");
				saleBuilder.append(sale).append(",");
				pprAppender = Math.floor((pwt * 100) / sale);
				pwtBuilder.append((Double.isNaN(pprAppender) ? 0.0 : pprAppender)).append(",");
				clmdBuilder.append(clmd).append(",");
			}

			int noOfDays = count;
			
			gameMap.put("",gameMap.get("").replace("7",count+""));
			gameMap.put("categories", categoryBuilder.toString());
			gameMap.put("SALE", saleBuilder.toString());
			gameMap.put("PPR", pwtBuilder.toString());
			gameMap.put("CLAIMED", clmdBuilder.toString());
			gameMap.put("SUMMARY", "");
			gameMap.put("Avg Sales", com.skilrock.lms.web.drawGames.common.Util
					.getBalInString(totalSale / noOfDays)
					+ ",");
			pprAppender = totalSale==0.0 ? 0.0:(totalPwt * 100) / totalSale;
			gameMap.put("Avg PPR (%)", com.skilrock.lms.web.drawGames.common.Util
					.getBalInString((Double.isNaN(pprAppender) ? 0.0 : totalPwt))
					+ "%,");
			gameMap.put("Avg Claimed",
					com.skilrock.lms.web.drawGames.common.Util
							.getBalInString(totalClmd / noOfDays)
							+ ",");

		} }catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		}
	}

	private void prepareSalesDailyString(String startDate, String endDate,
			boolean isArchTablesReq, String[] graphsDates,
			LinkedHashMap<String, String> gameMap) throws SQLException {

		int gameId = 0;
		boolean found = false;
		String groupBy = null;
		String outerQuery = null;
		String unionQuery = null;
		StringBuilder mainQuery = null;
		StringBuilder dataBuilder = new StringBuilder();
		StringBuilder categoryBuilder = new StringBuilder();

		List<String> gameList = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select game_id,game_name from st_dg_game_master where game_status <> 'SALE_CLOSE' ");
			mainQuery = new StringBuilder(
					"select sum(mrp_amt) sale_mrp , game_id , substring(transaction_date ,1,10) transaction_date from (");
			gameList = new ArrayList<String>();
			while (rs.next()) {
				gameId = rs.getInt("game_id");
				gameList.add(rs.getString("game_name"));
				unionQuery = "select status,transaction_id,game_id,ticket_nbr,mrp_amt,retailer_org_id,transaction_date from (select 'cancel 'status, rs.ref_transaction_id transaction_id,rs.game_id,ticket_nbr,mrp_amt,rs.retailer_org_id,rtm.transaction_date from st_dg_ret_sale_refund_"
						+ gameId
						+ " rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' union all select 'sale' status ,rs.transaction_id,rs.game_id,ticket_nbr,mrp_amt,rs.retailer_org_id ,rtm.transaction_date from st_dg_ret_sale_"
						+ gameId
						+ " rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "') a group by transaction_id having count(transaction_id)=1 and status='sale' union all select saleRef.status,ref.transaction_id,saleRef.game_id,ref.ticket_nbr,-ref.mrp_amt mrp_amt,saleRef.retailer_org_id,saleRef.transaction_date from st_dg_ret_sale_refund_"
						+ gameId
						+ " ref inner join (select status,transaction_id,game_id,ticket_nbr,-mrp_amt mrp_amt,retailer_org_id,transaction_date from (select 'cancel 'status, rs.ref_transaction_id transaction_id,rs.game_id,ticket_nbr,mrp_amt,rs.retailer_org_id,rtm.transaction_date from st_dg_ret_sale_refund_"
						+ gameId
						+ " rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' union all select 'sale' status ,rs.transaction_id,rs.game_id,ticket_nbr,mrp_amt,rs.retailer_org_id ,rtm.transaction_date from st_dg_ret_sale_"
						+ gameId
						+ " rs,st_lms_retailer_transaction_master rtm where rs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' ) a group by transaction_id having count(transaction_id)=1 and status='cancel') saleRef on saleRef.transaction_id=ref.ref_transaction_id union all ";
				mainQuery.append(unionQuery);
			}
			groupBy = "a1 inner join st_dg_game_master gm on gm.game_id=a1.game_id group by day(transaction_date),a1.game_id order  by transaction_date DESC";
			outerQuery = " select sum(sale_mrp) sale_mrp, game_name , transaction_date from ( ";
			stmt = null;
			stmt = con.createStatement();

			if (isArchTablesReq)
				rs = stmt
						.executeQuery(outerQuery
								.concat(mainQuery
										.substring(
												0,
												mainQuery
														.lastIndexOf(" union all"))
										.concat(
												") a group by day(transaction_date),game_id")
										.concat(
												" union all select (sum(sale_mrp)-sum(ref_sale_mrp))sale_mrp,game_id,substring(finaldate ,1,10) from st_rep_dg_retailer  where finaldate>='"
														+ startDate
														+ "' and  finaldate<='"
														+ endDate
														+ "'  group by day(finaldate),game_id )"
														+ groupBy)));
			else
				rs = stmt
						.executeQuery(outerQuery
								+ mainQuery
										.substring(
												0,
												mainQuery
														.lastIndexOf("union all"))
										.concat(") ")
										.concat(
												" a1 group by day(transaction_date),game_id  )")
								+ groupBy);

			for (String date : graphsDates) {
				categoryBuilder.append(
						com.skilrock.lms.web.common.drawables.CommonMethods
								.getDateForDrawables(date)).append(",");
			}
			gameMap.put("categories", categoryBuilder.toString());

			for (String gameName : gameList) {
				dataBuilder = new StringBuilder();
				for (String date : graphsDates) {
					String tempDate = date.substring(0, 10);
					while (rs.next()) {
						if (tempDate.equals(rs.getString("transaction_date"))
								& gameName.equals(rs.getString("game_name"))) {
							dataBuilder.append(rs.getDouble("sale_mrp"))
									.append(",");
							found = true;
							break;
						}
					}
					if (!found) {
						dataBuilder.append(0.0).append(",");
					}
					found = false;
					rs.beforeFirst();
				}
				gameMap.put(gameName.toUpperCase(), dataBuilder.toString());
			}
			logger.info(gameMap);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
	}

	private void prepareUserWiseAnalytics(LinkedHashMap<String, String> gameMap) {

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select organization_type ,  count(*) total from st_lms_user_master where organization_type = 'BO'	union all select organization_type , count(*) total from st_lms_user_master where organization_type = 'RETAILER' union all select organization_type, count(*) total from st_lms_user_master where organization_type = 'AGENT'");
			while (rs.next()) {
				gameMap.put(rs.getString("organization_type"), rs
						.getString("total")
						+ ",");
			}
			logger.info(gameMap);
		} catch (Exception e) {
			logger.error("EXCEPTION :- ", e);
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
	}

	

	
	/*@Override
	public void run() {
		String filePath = com.skilrock.lms.web.common.drawables.CommonMethods.path;
		com.skilrock.lms.web.common.drawables.CommonMethods.DeleteFiles(filePath, false, "");
		if (type == 1) {
			prepDataForUserGraphForRendering(filePath);
		}
		if (type == 2) {
			prepDataForSalesGraphForRendering(filePath);
		}
		if (type == 3) {
			prepDataForDrawWiseGraphForRendering(filePath);
		}
		if (type == 4) {
			prepDataForRetailerStatusGraphForRendering(filePath);
		}
		if(type == 5){
			prepDataForCshColChartForRendering(filePath);
		}

	}*/

}
