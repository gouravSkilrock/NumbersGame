package com.skilrock.lms.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.scheduler.SchedulerCommonFuntionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerActivityHistory implements Job{

	Log logger = LogFactory.getLog(RetailerActivityHistory.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try{
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap( context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Retailer_ActivityHistory_Daily_SCHEDULER").isActive()){
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Retailer_ActivityHistory_Daily_SCHEDULER").getJobId());
						logger.info("Post Sale Commission Scheduler Started");
						updateRetailerActivityHistoryDetails();
						SchedulerCommonFuntionsHelper.updateSchedulerEnd( scheBeanMap.get("Retailer_ActivityHistory_Daily_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Post_Commission_Monthly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Retailer_ActivityHistory_Daily_SCHEDULER").getJobId(), errorMsg);
					}
					
				}
			}
			
		}catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
	}

	
	public static void main(String[] args) throws ParseException, LMSException, SQLException{
		
		
	
	
		
		new RetailerActivityHistory().updateRetailerActivityHistoryDetails();
	}
	
	public void updateRetailerActivityHistoryDetails() throws ParseException,
			LMSException, SQLException {
		SimpleDateFormat frmt = null;
		String drawDate = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();  
			frmt = new SimpleDateFormat("yyyy-MM-dd");
			drawDate = getStartNEndDates(con);
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calStart.setTime(frmt.parse(drawDate));
		//	calEnd.setTime(new java.util.Date());
			calEnd.add(Calendar.DAY_OF_MONTH, -1);
			ServletContext servletContext = LMSUtility.sc;
			String  isGPSActivate =(String)servletContext.getAttribute("GPS_ACTIVATION"); 
			if(isGPSActivate.equalsIgnoreCase("YES")){
				logger.info("update the city code using lan and lon in offline master table ..... ");
				updateAddressThroughLatAndLongitude(con);
			}
			calEnd.set(Calendar.HOUR_OF_DAY, 0);
			calEnd.set(Calendar.MINUTE, 0);                                                    
			calEnd.set(Calendar.SECOND, 0);
			calEnd.set(Calendar.MILLISECOND, 0);
			con.setAutoCommit(false);	
			logger.info("start updating st_lms_new_ret_activity_history table ");
			while ((calStart.getTime()).compareTo(calEnd.getTime())<=0) {       
				Date date = new Date(calStart.getTime().getTime());
				String date1 = frmt.format(date);
				updateDailyRetActivity(date1,con);
				logger	
						.info("start updating st_lms_location_wise_history table");
		
				updateLocationWiseHistoryDetails(date,con);
				logger
						.info("start updating st_lms_daily_connectivity_history table... ");

				updateConnectivityHistoryDetails(date,con);
				logger.info("start updating st_lms_pos_version_history table");

				updateVersionHistoryDetails(date,con);
				
				//this is for new agent history
				logger.info("start updating st_lms_ret_activityHistory_agentwise,st_lms_ret_saleHistory_agentwise table");
				if(date.compareTo(calEnd.getTime())==0){
					logger.info("cuureDate");
					updateRetActivityAgentWise(date, con,true);
				}else{
					logger.info("notcurrdate");
					updateRetActivityAgentWise(date, con,false);
				}
				calStart.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
	}
	
	
	public static Date getZeroTimeDate(Date fecha) {
	    Date res = fecha;
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( fecha );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    res = new Date(calendar.getTime().getTime());

	    return res;
	}

	public void updateAddressThroughLatAndLongitude(Connection con)
			throws SQLException {
		Statement stmt = null;
		int organizationId = 0;
		ResultSet rs = null;
		String address = null;
		String cityName = null;
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select organization_id,lat,lon from st_lms_ret_offline_master where lat!='0.000000'");
			while (rs.next()) {
				organizationId = rs.getInt("organization_id");
				address = convertLatLongToAddress(rs.getString("lat"), rs
						.getString("lon"));
				cityName = address.split(",")[1];
				System.out.println("Address : " + address + " City Name : "
						+ cityName + " of" + " Organization id : "
						+ organizationId);
				pstmt = con
						.prepareStatement("update st_lms_ret_offline_master rm,st_lms_city_master cm set rm.city_code=cm.city_code where cm.city_name like '"
								+ cityName.trim() + "%' and organization_id=?");
				pstmt.setInt(1, organizationId);
				pstmt.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			logger.error(e);
		} finally {
			stmt.close();
			rs.close();
		}
	}

	public String convertLatLongToAddress(String latitude, String longitude) {
		String address = null;
		try {
			URL url = new URL(
					"http://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ latitude + "," + longitude + "&sensor=true");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(true);
			con.setDoOutput(true);
			System.out
					.println("latitude and longitude to address URL : " + url);
			InputStream is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String result = "";
			while ((result = br.readLine()) != null) {
				sb.append(result);
			}
			System.out.println(sb.toString());
			JSONObject obje = new Gson().fromJson(sb.toString(),
					JSONObject.class);
			System.out.println(obje.getString("status"));
			if (((JSONObject) (obje.getJSONArray("results").get(0))) != null)
				address = (String) ((JSONObject) (obje.getJSONArray("results")
						.get(0))).get("formatted_address");
		} catch (Exception e) {
			logger.error(e);
		}
		return address;
	}

	public String getStartNEndDates(Connection con) throws SQLException {
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			Statement stmt = con.createStatement();
			ResultSet rSet = stmt.executeQuery("SELECT date FROM st_lms_new_ret_activity_history ORDER BY date DESC LIMIT 1");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rSet.next()) {
				startDate = rSet.getDate("date");
			}
			Calendar calStart = Calendar.getInstance();
			java.util.Date currDate = new java.util.Date(calStart
					.getTimeInMillis());
			if (startDate != null) {
				calStart.setTime(startDate);
				calStart.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				calStart.setTime(currDate);
				calStart.add(Calendar.DAY_OF_MONTH, -1);
			}
			sb.append(sdf.format(calStart.getTimeInMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info(sb.toString());
		return sb.toString();
	}

	public void updateDailyRetActivity(String date,Connection con)
			throws LMSException, SQLException {
		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		Statement stmt = null;
		ResultSet rs = null;
		String combinedQnry = null;
		PreparedStatement pstmt = null;
		ArrayList<Double> totAmt = null;
		int dgSaleRC = 0, dgPwtRC = 0, slSaleRC=0,slPwtRC=0,iwSaleRC=0, iwPwtRC=0,vsSaleRC=0, vsPwtRC=0, seSaleRC = 0, sePwtRC = 0, olaDepoRC = 0, olaWdrlRC = 0, csSaleRC = 0, loginRC = 0, heartBeatRC = 0, dgRC = 0,slRC=0,iwRC=0,vsRC=0, seRC = 0, olaRC = 0, csRC = 0, totalRC = 0, inActiveRet = 0, terminateRet = 0;
		int gameNum = 0, catNum = 0;
		int walletNumber = 0;
		double dgTotalSale = 0.0, slTotalSale=0.0,iwTotalSale=0.0,vsTotalSale=0.0,dgTotalPwt = 0.0,slTotalPwt=0.0,iwTotalPwt=0.0,vsTotalPwt=0.0, tempRetPwt = 0.0;
		int totTktCnt = 0,slTotTktCnt=0,iwTotTktCnt=0,vsTotTktCnt=0, totPwtCnt = 0,slTotPwtCnt=0, iwTotPwtCnt=0, vsTotPwtCnt=0;
		double avgSalePerRet = 0.0,slAvgSalePerRet=0.0,iwAvgSalePerRet=0.0,vsAvgSalePerRet=0.0, csTotalSale = 0.0, olaDepo = 0.0, olaWdrl = 0.0, seTotalSale = 0.0, seTotalPwt = 0.0;
		try {
			//con.setAutoCommit(false);
			String trxDate = "transaction_date>'" + fromDate+ "' and transaction_date<'" + toDate + "'";
			logger.info("inside updating daily ret activity  table  for trxDate"+trxDate+"having connection "+con);
			List<Integer> gameNumList = Util.getLMSGameNumberList();
			logger.info("inside updating daily ret activity  table  gameNumList"+gameNumList);
			stmt = con.createStatement();
			List<Integer> catNumList = Util.getCategoryNumberList(con);
			logger.info("inside updating daily ret activity  table  catNumList"+catNumList);
			List<Integer> walletNumList = Util.getWalletNumberList(con);
			logger.info("inside updating daily ret activity  table  walletNumList"+walletNumList);
			String query ="select dgSaleRC,dgPwtRC,slSaleRC,slPwtRC,iwSaleRC,iwPwtRC,vsSaleRC,vsPwtRC,seSaleRC,sePwtRC,olaDepoRC,olaWdrlRC,csSaleRC,loginRC,heartBeatRC,dgRC,slRC,iwRC,vsRC,seRC,olaRC,csRC,totalRC,terRetCount,inActiveRetCount  from ((select count(*) dgSaleRC from st_lms_ret_offline_master where dg_last_sale_time>'"
				+ fromDate
				+ "' and dg_last_sale_time<'"
				+ toDate
				+ "') aa inner join (select count(*) dgPwtRC from st_lms_ret_offline_master where dg_last_pwt_time>'"
				+ fromDate
				+ "' and dg_last_pwt_time<'"
				+ toDate
				+ "') bb inner join (select count(*) slSaleRC from st_lms_ret_offline_master where sle_last_sale_time>'"
				+ fromDate
				+ "' and sle_last_sale_time<'"
				+ toDate
				+ "') cc inner join (select count(*) slPwtRC from st_lms_ret_offline_master where sle_last_pwt_time>'"
				+ fromDate
				+ "' and sle_last_pwt_time<'"
				+ toDate
				+ "') dd inner join (select count(*) iwSaleRC from st_lms_ret_offline_master where iw_last_sale_time>'"
				+ fromDate
				+ "' and iw_last_sale_time<'"
				+ toDate
				+ "') it inner join (select count(*) iwPwtRC from st_lms_ret_offline_master where iw_last_pwt_time>'"
				+ fromDate
				+ "' and iw_last_pwt_time<'"
				+ toDate								
				+ "') iw inner join (select count(*) vsSaleRC from st_lms_ret_offline_master where vs_last_sale_time>'" 
				+ fromDate
				+ "' and vs_last_sale_time<'"
				+ toDate 
				+ "') vt inner join (select count(*) vsPwtRC from st_lms_ret_offline_master where vs_last_pwt_time>'" +
				fromDate 
				+ "' and vs_last_pwt_time<'" 
				+ toDate +
				"') vs inner join (select count(*) seSaleRC from st_lms_ret_offline_master where se_last_sale_time>'"
				+ fromDate
				+ "' and se_last_sale_time<'"
				+ toDate
				+ "') ee inner join (select count(*) sePwtRC from st_lms_ret_offline_master where se_last_pwt_time>'"
				+ fromDate
				+ "' and se_last_pwt_time<'"
				+ toDate
				+ "') ff inner join (select count(*) olaDepoRC from st_lms_ret_offline_master where ola_last_deposit_time>'"
				+ fromDate
				+ "' and ola_last_deposit_time<'"
				+ toDate
				+ "') gg inner join (select count(*) olaWdrlRC from st_lms_ret_offline_master where ola_last_withdrawal_time>'"
				+ fromDate
				+ "' and ola_last_withdrawal_time<'"
				+ toDate
				+ "') hh inner join (select count(*) csSaleRC from st_lms_ret_offline_master where cs_last_sale_time>'"
				+ fromDate
				+ "' and cs_last_sale_time<'"
				+ toDate
				+ "') ii inner join (select count(*) loginRC from st_lms_ret_offline_master where last_login_time>'"
				+ fromDate
				+ "' and last_login_time<'"
				+ toDate
				+ "') jj inner join (select count(*) heartBeatRC from st_lms_ret_offline_master where last_HBT_time>'"
				+ fromDate
				+ "' and last_HBT_time<'"
				+ toDate
				+ "') kk inner join (select count(*) dgRC from st_lms_ret_offline_master where (dg_last_sale_time>'"
				+ fromDate
				+ "' and dg_last_sale_time<'"
				+ toDate
				+ "') or (dg_last_pwt_time>'"
				+ fromDate
				+ "' and dg_last_pwt_time<'"
				+ toDate
				+ "')) ll inner join (select count(*) slRC from st_lms_ret_offline_master where (sle_last_sale_time>'"
				+ fromDate
				+ "' and sle_last_sale_time<'"
				+ toDate
				+ "') or (sle_last_pwt_time>'"
				+ fromDate
				+ "' and sle_last_pwt_time<'"
				+ toDate
				+ "'))mm inner join (select count(*) iwRC from st_lms_ret_offline_master where (iw_last_sale_time>'"
				+ fromDate
				+ "' and iw_last_sale_time<'"
				+ toDate
				+ "') or (iw_last_pwt_time>'"
				+ fromDate
				+ "' and iw_last_pwt_time<'"
				+ toDate
				+ "'))im inner join (select count(*) vsRC from st_lms_ret_offline_master where (vs_last_sale_time>'"
				+ fromDate
				+ "' and vs_last_sale_time<'"
				+ toDate
				+ "') or (vs_last_pwt_time>'"
				+ fromDate
				+ "' and vs_last_pwt_time<'"
				+toDate
				+ "'))vm inner join (select count(*) seRC from st_lms_ret_offline_master where (se_last_sale_time>'"
				+ fromDate
				+ "' and se_last_sale_time<'"
				+ toDate
				+ "') or (se_last_pwt_time>'"
				+ fromDate
				+ "' and se_last_pwt_time<'"
				+ toDate
				+ "')) nn inner join (select count(*) olaRC from st_lms_ret_offline_master where (ola_last_deposit_time>'"
				+ fromDate
				+ "' and ola_last_deposit_time<'"
				+ toDate
				+ "') or  (ola_last_withdrawal_time>'"
				+ fromDate
				+ "' and ola_last_withdrawal_time<'"
				+ toDate
				+ "')) oo inner join (select count(*) csRC from st_lms_ret_offline_master where cs_last_sale_time>'"
				+ fromDate
				+ "' and cs_last_sale_time<'"
				+ toDate
				+ "') pp inner join (select count(*) totalRC from st_lms_ret_offline_master where last_HBT_time>'"
				+ fromDate
				+ "' and last_HBT_time<'"
				+ toDate
				+ "') qq inner join (select count(distinct(organization_id)) terRetCount from st_lms_organization_master where organization_type='RETAILER' and organization_status='TERMINATE') rr inner join (select count(distinct(organization_id)) inActiveRetCount from st_lms_organization_master where organization_type='RETAILER' and organization_status='INACTIVE') ss)";
			logger.info("inside updating daily ret activity  table  query1"+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				dgSaleRC = rs.getInt("dgSaleRC");
				dgPwtRC = rs.getInt("dgPwtRC");
				slSaleRC = rs.getInt("slSaleRC");
				slPwtRC = rs.getInt("slPwtRC");
				iwSaleRC = rs.getInt("iwSaleRC");
				iwPwtRC = rs.getInt("iwPwtRC");
				vsSaleRC = rs.getInt("vsSaleRC");
				vsPwtRC = rs.getInt("vsPwtRC");
				seSaleRC = rs.getInt("seSaleRC");
				sePwtRC = rs.getInt("sePwtRC");
				csSaleRC = rs.getInt("csSaleRC");
				olaDepoRC = rs.getInt("olaDepoRC");
				olaWdrlRC = rs.getInt("olaWdrlRC");
				dgRC = rs.getInt("dgRC");
				slRC = rs.getInt("slRC");
				iwRC = rs.getInt("iwRC");
				vsRC = rs.getInt("vsRC");
				seRC = rs.getInt("seRC");
				csRC = rs.getInt("csRC");
				olaRC = rs.getInt("olaRC");
				totalRC = rs.getInt("totalRC");
				heartBeatRC = rs.getInt("heartBeatRC");
				loginRC = rs.getInt("loginRC");
				terminateRet = rs.getInt("terRetCount");
				inActiveRet = rs.getInt("inActiveRetCount");
			}

			// Draw Game
			logger.info("Draw Game Service start : ");
			combinedQnry = "select ifnull(sum(mrp_amt),0.0) res from st_dg_ret_sale_? rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(mrp_amt),0.0) res  from st_dg_ret_sale_refund_? ref inner join st_lms_retailer_transaction_master tm on ref.transaction_id=tm.transaction_id where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(pwt_amt),0.0) res from st_dg_ret_pwt_? pwt inner join st_lms_retailer_transaction_master tm on pwt.transaction_id=tm.transaction_id where transaction_date>'"
					+ fromDate + "' and transaction_date<'" + toDate + "'";

			for (int i = 0; i < gameNumList.size(); i++) {
				gameNum = gameNumList.get(i);
				totAmt = new ArrayList<Double>();
				pstmt = con.prepareStatement(combinedQnry);
				pstmt.setInt(1, gameNum);
				pstmt.setInt(2, gameNum);
				pstmt.setInt(3, gameNum);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					totAmt.add(rs.getDouble("res"));
				}
				dgTotalSale += (totAmt.get(0) - totAmt.get(1));
				tempRetPwt += totAmt.get(2);
			}
			// direct player pwt
			String dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_dg_bo_direct_plr_pwt where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "') a,(select sum(pwt_amt) pwt_amt from st_dg_agt_direct_plr_pwt where transaction_date>'"
					+ fromDate + "' and transaction_date<'" + toDate + "') b";
			double dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}

			dgTotalPwt = tempRetPwt + dirPlrPwt;

			// total ticket sold
			pstmt = con
					.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from "
							+ "(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and "
							+ "(transaction_type ='DG_SALE' or transaction_type='DG_SALE_OFFLINE') )sale,"
							+ "(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and (transaction_type ='DG_REFUND_CANCEL' or transaction_type='DG_REFUND_FAILED'))ref");

			rs = pstmt.executeQuery();
			if (rs.next()) {
				totTktCnt = rs.getInt("netSaleCnt");
			}

			// total PWT count
			pstmt = con
					.prepareStatement("select (ret.retPwt+agt.agtPwt+bo.boPwt) as pwtCnt from "
							+ "((select ifnull(count(*),0) retPwt  from  st_lms_retailer_transaction_master where "
							+ trxDate
							+ " and  "
							+ "transaction_type='DG_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_dg_agt_direct_plr_pwt where "
							+ trxDate
							+ ")agt,(select ifnull(count(*),0) boPwt from st_dg_bo_direct_plr_pwt where "
							+ trxDate + " )bo");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totPwtCnt = rs.getInt("pwtCnt");
			}

			// calculating avgSalePerRet
			if (dgTotalSale != 0.0 && dgSaleRC != 0)
				avgSalePerRet = dgTotalSale / dgSaleRC;
			
			//Sports Lottery
			logger.info("Sports Lottery Service start : ");
			combinedQnry = "select ifnull(sum(mrp_amt),0.0) res from st_sle_ret_sale rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(mrp_amt),0.0) res  from st_sle_ret_sale_refund ref inner join st_lms_retailer_transaction_master tm on ref.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(pwt_amt),0.0) res from st_sle_ret_pwt pwt inner join st_lms_retailer_transaction_master tm on pwt.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate + "' and tm.transaction_date<'" + toDate + "'";
			
            pstmt=con.prepareStatement(combinedQnry);
			rs = pstmt.executeQuery();
           totAmt=new ArrayList<Double>();
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			slTotalSale += (totAmt.get(0) - totAmt.get(1));
			tempRetPwt = 0.0 ;
			tempRetPwt += totAmt.get(2);

			// direct player pwt
			 dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_sle_bo_direct_plr_pwt where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "') a,(select sum(pwt_amt) pwt_amt from st_sle_agent_direct_plr_pwt where transaction_date>'"
					+ fromDate + "' and transaction_date<'" + toDate + "') b";
			 dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}

			slTotalPwt = tempRetPwt + dirPlrPwt;

			// total ticket sold
			pstmt = con
					.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from "
							+ "(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and "
							+ "(transaction_type ='SLE_SALE') )sale,"
							+ "(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and (transaction_type ='SLE_REFUND_CANCEL'))ref");

			rs = pstmt.executeQuery();
			if (rs.next()) {
				slTotTktCnt = rs.getInt("netSaleCnt");
			}

			// total PWT count
			pstmt = con
					.prepareStatement("select (ret.retPwt+agt.agtPwt+bo.boPwt) as pwtCnt from "
							+ "((select ifnull(count(*),0) retPwt  from  st_lms_retailer_transaction_master where "
							+ trxDate
							+ " and  "
							+ "transaction_type='SLE_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_sle_agent_direct_plr_pwt where "
							+ trxDate
							+ ")agt,(select ifnull(count(*),0) boPwt from st_sle_bo_direct_plr_pwt where "
							+ trxDate + " )bo");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				slTotPwtCnt = rs.getInt("pwtCnt");
			}

			// calculating avgSalePerRet
			if (slTotalSale != 0.0 && slSaleRC != 0)
				slAvgSalePerRet = slTotalSale / slSaleRC;
	
			
			// Instant Win Service Start
			logger.info("Instant Win Service start : ");
			combinedQnry = "select ifnull(sum(mrp_amt),0.0) res from st_iw_ret_sale rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(mrp_amt),0.0) res  from st_iw_ret_sale_refund ref inner join st_lms_retailer_transaction_master tm on ref.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(pwt_amt),0.0) res from st_iw_ret_pwt pwt inner join st_lms_retailer_transaction_master tm on pwt.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate + "' and tm.transaction_date<'" + toDate + "'";
			
            pstmt=con.prepareStatement(combinedQnry);
			rs = pstmt.executeQuery();
           totAmt=new ArrayList<Double>();
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			iwTotalSale += (totAmt.get(0) - totAmt.get(1));
			tempRetPwt = 0.0 ;
			tempRetPwt += totAmt.get(2);

			// direct player pwt
			 dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_iw_bo_direct_plr_pwt where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "') a,(select sum(pwt_amt) pwt_amt from st_iw_agent_direct_plr_pwt where transaction_date>'"
					+ fromDate + "' and transaction_date<'" + toDate + "') b";
			 dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}

			iwTotalPwt = tempRetPwt + dirPlrPwt;

			// total ticket sold
			pstmt = con
					.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from "
							+ "(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and "
							+ "(transaction_type ='IW_SALE') )sale,"
							+ "(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and (transaction_type ='IW_REFUND_CANCEL'))ref");

			rs = pstmt.executeQuery();
			if (rs.next()) {
				iwTotTktCnt = rs.getInt("netSaleCnt");
			}

			// total PWT count
			pstmt = con
					.prepareStatement("select (ret.retPwt+agt.agtPwt+bo.boPwt) as pwtCnt from "
							+ "((select ifnull(count(*),0) retPwt  from  st_lms_retailer_transaction_master where "
							+ trxDate
							+ " and  "
							+ "transaction_type='IW_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_iw_agent_direct_plr_pwt where "
							+ trxDate
							+ ")agt,(select ifnull(count(*),0) boPwt from st_iw_bo_direct_plr_pwt where "
							+ trxDate + " )bo");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				iwTotPwtCnt = rs.getInt("pwtCnt");
			}

			// calculating avgSalePerRet
			if (iwTotalSale != 0.0 && iwSaleRC != 0)
				iwAvgSalePerRet = iwTotalSale / iwSaleRC;
	
        	
			// Virtual Sports Service Start
			logger.info("Virtual Sports Service start : ");
			combinedQnry = "select ifnull(sum(mrp_amt),0.0) res from st_vs_ret_sale rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(mrp_amt),0.0) res  from st_vs_ret_sale_refund ref inner join st_lms_retailer_transaction_master tm on ref.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate
					+ "' and tm.transaction_date<'"
					+ toDate
					+ "' union all select ifnull(sum(pwt_amt),0.0) res from st_vs_ret_pwt pwt inner join st_lms_retailer_transaction_master tm on pwt.transaction_id=tm.transaction_id where tm.transaction_date>'"
					+ fromDate + "' and tm.transaction_date<'" + toDate + "'";
			
			pstmt = con.prepareStatement(combinedQnry);
			rs = pstmt.executeQuery();
			totAmt = new ArrayList<Double>();
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			vsTotalSale += (totAmt.get(0) - totAmt.get(1));
			tempRetPwt = 0.0;
			tempRetPwt += totAmt.get(2);

			// direct player pwt
			dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_vs_bo_direct_plr_pwt where transaction_date>'"
					+ fromDate
					+ "' and transaction_date<'"
					+ toDate
					+ "') a,(select sum(pwt_amt) pwt_amt from st_vs_agent_direct_plr_pwt where transaction_date>'"
					+ fromDate + "' and transaction_date<'" + toDate + "') b";
			dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}

			vsTotalPwt = tempRetPwt + dirPlrPwt;

			// total ticket sold
			pstmt = con
					.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from "
							+ "(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and "
							+ "(transaction_type ='VS_SALE') )sale,"
							+ "(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where ("
							+ trxDate
							+ ") and (transaction_type ='VS_REFUND_CANCEL'))ref");

			rs = pstmt.executeQuery();
			if (rs.next()) {
				vsTotTktCnt = rs.getInt("netSaleCnt");
			}

			// total PWT count
			pstmt = con
					.prepareStatement("select (ret.retPwt+agt.agtPwt+bo.boPwt) as pwtCnt from "
							+ "((select ifnull(count(*),0) retPwt  from  st_lms_retailer_transaction_master where "
							+ trxDate
							+ " and  "
							+ "transaction_type='VS_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_vs_agent_direct_plr_pwt where "
							+ trxDate
							+ ")agt,(select ifnull(count(*),0) boPwt from st_vs_bo_direct_plr_pwt where "
							+ trxDate + " )bo");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vsTotPwtCnt = rs.getInt("pwtCnt");
			}

			// calculating avgSalePerRet
			if (vsTotalSale != 0.0 && vsSaleRC != 0)
				vsAvgSalePerRet = vsTotalSale / vsSaleRC;

			logger.info("Scratch Service start");

			// Scratch Pwt

			pstmt = con
					.prepareStatement("select ifnull(sum(pwt_amt),0.0) res from st_se_retailer_pwt rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where transaction_date>'"
							+ fromDate
							+ "' and transaction_date<'"
							+ toDate
							+ "'");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				seTotalPwt = rs.getDouble("res");
			}

			logger.info("Commercial Service start");
			// commercial service
			for (int i = 0; i < catNumList.size(); i++) {
				catNum = catNumList.get(i);
				pstmt = con
						.prepareStatement("select sale,saleRef from((select ifnull(sum(mrp_amt),0.0) sale from st_cs_sale_? rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate
								+ "') sale inner join (select ifnull(sum(mrp_amt),0.0) saleRef from st_cs_refund_? rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate + "')ref)");
				pstmt.setInt(1, catNum);
				pstmt.setInt(2, catNum);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					csTotalSale = csTotalSale
							+ (rs.getDouble("sale") - rs.getDouble("saleRef"));
				}
			}
			logger.info("OLA service start");

			// Off line Affiliates(OLA)

			for (int i = 0; i < walletNumList.size(); i++) {
				walletNumber = walletNumList.get(i);
				totAmt = new ArrayList<Double>();
				pstmt = con
						.prepareStatement("select ifnull(sum(deposit_amt),0.0) res from st_ola_ret_deposit rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where wallet_id=? and transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate
								+ "' union all select ifnull(sum(deposit_amt),0.0) res from st_ola_ret_deposit_refund rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where wallet_id=? and transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate
								+ "' union all select ifnull(sum(withdrawl_amt),0.0) res from st_ola_ret_withdrawl rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where wallet_id=? and transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate
								+ "' union all select ifnull(sum(withdrawl_amt),0.0) res from st_ola_ret_withdrawl_refund rs inner join st_lms_retailer_transaction_master tm on rs.transaction_id=tm.transaction_id where wallet_id=? and transaction_date>'"
								+ fromDate
								+ "' and transaction_date<'"
								+ toDate + "'");
				pstmt.setInt(1, walletNumber);
				pstmt.setInt(2, walletNumber);
				pstmt.setInt(3, walletNumber);
				pstmt.setInt(4, walletNumber);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					totAmt.add(rs.getDouble("res"));
				}
				olaDepo += (totAmt.get(0) - totAmt.get(1));
				olaWdrl += (totAmt.get(2) - totAmt.get(3));
			}
			
			
			logger
					.info("Insert data into st_lms_new_ret_activity_history table...");
			DecimalFormat df = new DecimalFormat("#.##");
			pstmt = con
					.prepareStatement("insert into st_lms_new_ret_activity_history(date,dg_sale_RC, dg_pwt_RC,sl_Sale_RC,sl_Pwt_RC,iw_Sale_RC,iw_Pwt_RC,vs_Sale_RC,vs_Pwt_RC, se_sale_RC, se_pwt_RC, cs_sale_RC, ola_deposit_RC, ola_wd_RC, dg_RC,sl_RC,iw_RC,vs_RC, se_RC, cs_RC, ola_RC, total_RC, heartBeat_RC, login_RC, inactive_retailers, terminated_retailers, dg_total_sales, dg_total_pwt, dg_tkt_count, dg_pwt_count, dg_avg_sale_per_ret,sl_total_sales,sl_total_pwt,sl_tkt_count,sl_pwt_count,sl_avg_sale_per_ret, iw_total_sales,iw_total_pwt,iw_tkt_count,iw_pwt_count,iw_avg_sale_per_ret, vs_total_sales,vs_total_pwt,vs_tkt_count,vs_pwt_count,vs_avg_sale_per_ret,se_total_sales, se_total_pwt, ola_total_deposit, ola_total_wd, cs_total_sale) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, fromDate);
			pstmt.setInt(2, dgSaleRC);
			pstmt.setInt(3, dgPwtRC);
			pstmt.setInt(4, slSaleRC);
			pstmt.setInt(5, slPwtRC);
			pstmt.setInt(6, iwSaleRC);
			pstmt.setInt(7, iwPwtRC);
			pstmt.setInt(8, vsSaleRC);
			pstmt.setInt(9, vsPwtRC);
			pstmt.setInt(10, seSaleRC);
			pstmt.setInt(11, sePwtRC);
			pstmt.setInt(12, csSaleRC);
			pstmt.setInt(13, olaDepoRC);
			pstmt.setInt(14, olaWdrlRC);
			pstmt.setInt(15, dgRC);
			pstmt.setInt(16, slRC);
			pstmt.setInt(17, iwRC);
			pstmt.setInt(18, vsRC);
			pstmt.setInt(19, seRC);
			pstmt.setInt(20, csRC);
			pstmt.setInt(21, olaRC);
			pstmt.setInt(22, totalRC);
			pstmt.setInt(23, heartBeatRC);
			pstmt.setInt(24, loginRC);
			pstmt.setInt(25, inActiveRet);
			pstmt.setInt(26, terminateRet);
			pstmt.setDouble(27, dgTotalSale);
			pstmt.setDouble(28, dgTotalPwt);
			pstmt.setInt(29, totTktCnt);
			pstmt.setInt(30, totPwtCnt);
			pstmt.setDouble(31,Double.parseDouble(df.format(avgSalePerRet)));
			pstmt.setDouble(32, slTotalSale);
			pstmt.setDouble(33, slTotalPwt);
			pstmt.setInt(34, slTotTktCnt);
			pstmt.setInt(35, slTotPwtCnt);
			pstmt.setDouble(36,Double.parseDouble(df.format(slAvgSalePerRet)));
			pstmt.setDouble(37, iwTotalSale);
			pstmt.setDouble(38, iwTotalPwt);
			pstmt.setInt(39, iwTotTktCnt);
			pstmt.setInt(40, iwTotPwtCnt);
			pstmt.setDouble(41,Double.parseDouble(df.format(iwAvgSalePerRet)));
			pstmt.setDouble(42, vsTotalSale);
			pstmt.setDouble(43, vsTotalPwt);
			pstmt.setInt(44, vsTotTktCnt);
			pstmt.setInt(45, vsTotPwtCnt);
			pstmt.setDouble(46, Double.parseDouble(df.format(vsAvgSalePerRet)));
			pstmt.setDouble(47, seTotalSale);
			pstmt.setDouble(48, seTotalPwt);
			pstmt.setDouble(49, olaDepo);
			pstmt.setDouble(50, olaWdrl);
			pstmt.setDouble(51, csTotalSale);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
			logger.error(e);
		} finally {
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				
				pstmt.close();
				
			}
			
		}
	}

	public void updateLocationWiseHistoryDetails(Date date,Connection con)
			throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query=null;
		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		int count = 0;
		try {
			//con.setAutoCommit(false);
			Map<String, RetailersLocationHistoryBean> map = new LinkedHashMap<String, RetailersLocationHistoryBean>();
			;
			RetailersLocationHistoryBean locationBean = null;
			stmt = con.createStatement();
			query="select city_code,count(*) res from st_lms_ret_offline_master where dg_last_sale_time>'"
				+ fromDate
				+ "' and dg_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where dg_last_pwt_time>'"
				+ fromDate
				+ "' and dg_last_pwt_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where sle_last_sale_time>'"
				+ fromDate
				+ "' and sle_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where sle_last_pwt_time>'"
				+ fromDate
				+ "' and sle_last_pwt_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where vs_last_sale_time>'"
				+ fromDate
				+ "' and vs_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where vs_last_pwt_time>'"
				+ fromDate
				+ "' and vs_last_pwt_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where iw_last_sale_time>'"
				+ fromDate
				+ "' and iw_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where iw_last_pwt_time>'"
				+ fromDate
				+ "' and iw_last_pwt_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK'union all select city_code,count(*) res from st_lms_ret_offline_master where se_last_sale_time>'"
				+ fromDate
				+ "' and se_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null  group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where se_last_pwt_time>'"
				+ fromDate
				+ "' and se_last_pwt_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where ola_last_deposit_time>'"
				+ fromDate
				+ "' and ola_last_deposit_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where ola_last_withdrawal_time>'"
				+ fromDate
				+ "' and ola_last_withdrawal_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where cs_last_sale_time>'"
				+ fromDate
				+ "' and cs_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where last_login_time>'"
				+ fromDate
				+ "' and last_login_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where last_HBT_time>'"
				+ fromDate
				+ "' and last_HBT_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null  and (dg_last_sale_time>'"
				+ fromDate
				+ "' and dg_last_sale_time<'"
				+ toDate
				+ "') or (dg_last_pwt_time>'"
				+ fromDate
				+ "' and dg_last_pwt_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK'union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null  and (sle_last_sale_time>'"
				+ fromDate
				+ "' and sle_last_sale_time<'"
				+ toDate
				+ "') or (sle_last_pwt_time>'"
				+ fromDate
				+ "' and sle_last_pwt_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK'union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null  and (vs_last_sale_time>'"
				+ fromDate
				+ "' and vs_last_sale_time<'"
				+ toDate
				+ "') or (vs_last_pwt_time>'"
				+ fromDate
				+ "' and vs_last_pwt_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK'union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null  and (iw_last_sale_time>'"
				+ fromDate
				+ "' and iw_last_sale_time<'"
				+ toDate
				+ "') or (iw_last_pwt_time>'"
				+ fromDate
				+ "' and iw_last_pwt_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null and (se_last_sale_time>'"
				+ fromDate
				+ "' and se_last_sale_time<'"
				+ toDate
				+ "') or (se_last_pwt_time>'"
				+ fromDate
				+ "' and se_last_pwt_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where city_code is not null and (ola_last_deposit_time>'"
				+ fromDate
				+ "' and ola_last_deposit_time<'"
				+ toDate
				+ "') or  (ola_last_withdrawal_time>'"
				+ fromDate
				+ "' and ola_last_withdrawal_time<'"
				+ toDate
				+ "') group by city_code union all select 'GK','GK' union all  select city_code,count(*) res from st_lms_ret_offline_master where cs_last_sale_time>'"
				+ fromDate
				+ "' and cs_last_sale_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code union all select 'GK','GK' union all select city_code,count(*) res from st_lms_ret_offline_master where last_HBT_time>'"
				+ fromDate
				+ "' and last_HBT_time<'"
				+ toDate
				+ "' and city_code is not null group by city_code";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				if(rs.getString("city_code")!=null){
				if (rs.getString("city_code").equals("GK")) {
					count++;
					continue;
				} else {
					locationBean = new RetailersLocationHistoryBean();
					if (!map.containsKey(rs.getString("city_code"))) {
						map.put(rs.getString("city_code"), locationBean);
					}
					switch (count) {
					case 0:
						map.get(rs.getString("city_code")).setDgSaleCount(
								rs.getInt("res"));
						break;
					case 1:
						map.get(rs.getString("city_code")).setDgPwtCount(
								rs.getInt("res"));
						break;
					case 2:
						map.get(rs.getString("city_code")).setSlSaleCount(
								rs.getInt("res"));
						break;
					case 3:
						map.get(rs.getString("city_code")).setSlPwtCount(
								rs.getInt("res"));
						break;
					case 4:
						map.get(rs.getString("city_code")).setVsSaleCount(
								rs.getInt("res"));
						break;
					case 5:
						map.get(rs.getString("city_code")).setVsPwtCount(
								rs.getInt("res"));
						break;
					case 6:
						map.get(rs.getString("city_code")).setIwSaleCount(
								rs.getInt("res"));
						break;
					case 7:
						map.get(rs.getString("city_code")).setIwPwtCount(
								rs.getInt("res"));
						break;
					case 8:
						map.get(rs.getString("city_code")).setSeSaleCount(
								rs.getInt("res"));
						break;
					case 9:
						map.get(rs.getString("city_code")).setSePwtCount(
								rs.getInt("res"));
						break;
					case 10:
						map.get(rs.getString("city_code")).setOlaDepoCount(
								rs.getInt("res"));
						break;
					case 11:
						map.get(rs.getString("city_code")).setOlaWdlCount(
								rs.getInt("res"));
						break;
					case 12:
						map.get(rs.getString("city_code")).setCsSaleCount(
								rs.getInt("res"));
						break;
					case 13:
						map.get(rs.getString("city_code")).setLoginCount(
								rs.getInt("res"));
						break;
					case 14:
						map.get(rs.getString("city_code")).setHeartBeatCount(
								rs.getInt("res"));
						break;
					case 15:
						map.get(rs.getString("city_code")).setDgCount(
								rs.getInt("res"));
						break;
					case 16:

						map.get(rs.getString("city_code")).setSlCount(
								rs.getInt("res"));
						break;
					case 17:

						map.get(rs.getString("city_code")).setVsCount(
								rs.getInt("res"));
						break;

					case 18:

						map.get(rs.getString("city_code")).setIwCount(
								rs.getInt("res"));
						break;
					case 19:
						map.get(rs.getString("city_code")).setSeCount(
								rs.getInt("res"));
						break;
					case 20:
						map.get(rs.getString("city_code")).setOlaCount(
								rs.getInt("res"));
						break;
					case 21:
						map.get(rs.getString("city_code")).setCsCount(
								rs.getInt("res"));
						break;
					case 22:
						map.get(rs.getString("city_code")).setTotalCount(
								rs.getInt("res"));
						break;
					}
				}
			}
			}

			Iterator<Entry<String, RetailersLocationHistoryBean>> itr = map
					.entrySet().iterator();
			while (itr.hasNext()) {
				pstmt = con
						.prepareStatement("insert into st_lms_location_wise_history(date,city_code, dg_sale_RC, dg_pwt_RC, se_sale_RC, se_pwt_RC, cs_sale_RC, ola_deposit_RC, ola_wd_RC, dg_RC, se_RC, cs_RC, ola_RC, total_RC, login_RC, heartbeat_RC,sl_sale_RC,sl_Pwt_RC,sl_RC,iw_sale_RC,iw_pwt_RC,iw_RC,vs_sale_RC,vs_pwt_RC,vs_RC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				Map.Entry<String, RetailersLocationHistoryBean> me = itr.next();
				pstmt.setDate(1, date);
				pstmt.setString(2, me.getKey());
				pstmt.setInt(3, me.getValue().getDgSaleCount());
				pstmt.setInt(4, me.getValue().getDgPwtCount());
				pstmt.setInt(5, me.getValue().getSeSaleCount());
				pstmt.setInt(6, me.getValue().getSePwtCount());
				pstmt.setInt(7, me.getValue().getCsSaleCount());
				pstmt.setInt(8, me.getValue().getOlaDepoCount());
				pstmt.setInt(9, me.getValue().getOlaWdlCount());
				pstmt.setInt(10, me.getValue().getDgCount());
				pstmt.setInt(11, me.getValue().getSeCount());
				pstmt.setInt(12, me.getValue().getCsCount());
				pstmt.setInt(13, me.getValue().getOlaCount());
				pstmt.setInt(14, me.getValue().getTotalCount());
				pstmt.setInt(15, me.getValue().getLoginCount());
				pstmt.setInt(16, me.getValue().getHeartBeatCount());
				pstmt.setInt(17, me.getValue().getSlSaleCount());
				pstmt.setInt(18, me.getValue().getSlPwtCount());
				pstmt.setInt(19, me.getValue().getSlCount());
				pstmt.setInt(20, me.getValue().getIwSaleCount());
				pstmt.setInt(21, me.getValue().getIwPwtCount());
				pstmt.setInt(22, me.getValue().getIwCount());
				pstmt.setInt(23, me.getValue().getVsSaleCount());
				pstmt.setInt(24, me.getValue().getVsPwtCount());
				pstmt.setInt(25, me.getValue().getVsCount());

				pstmt.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
			logger.error(e);
		} finally {
		
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				
				pstmt.close();
				
			}
		}
	}

	public void updateVersionHistoryDetails(Date date,Connection con)
			throws SQLException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		try {
			//con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select device_type,current_version,count(*) retCount from st_lms_ret_offline_master where last_login_time>='"+fromDate+"' and last_login_time<'"+toDate+"' and device_type is not null group by device_type,current_version");
			while (rs.next()) {
				pstmt = con
						.prepareStatement("insert into st_lms_pos_version_history(date,device_type, current_version, ret_count) values (?,?,?,?)");
				pstmt.setDate(1, date);
				pstmt.setString(2, rs.getString("device_type"));
				pstmt.setString(3, rs.getString("current_version"));
				pstmt.setInt(4, rs.getInt("retCount"));
				pstmt.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
			logger.error(e);
		} finally {
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				
				pstmt.close();
				
			}
		}
	}

	public void updateConnectivityHistoryDetails(Date date,Connection con)
			throws SQLException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		try {
			//con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select sim_id,count(distinct(ret_organization_id)) retCount from st_lms_ret_wise_sim_history  where datetime>'"
							+ fromDate
							+ "' and datetime<'"
							+ toDate
							+ "' group by sim_id");
			while (rs.next()) {
				pstmt = con
						.prepareStatement("insert into st_lms_daily_connectivity_history(date, sim_id, ret_count) values (?,?,?)");
				pstmt.setDate(1, date);
				pstmt.setInt(2, rs.getInt("sim_id"));
				pstmt.setInt(3, rs.getInt("retCount"));
				pstmt.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
			logger.error(e);
		} finally {
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				
				pstmt.close();
				
			}
		}
	}

public void updateRetActivityAgentWise(Date date,Connection con,boolean isCurrentDay) throws SQLException{
	PreparedStatement pstmt = null;
	String query =" ";
	ResultSet rs =null;
	try{
		
		// create data table  
		query ="create  temporary table orgData (primary key(organization_id )) as select distinct slom.organization_id,slom.organization_type ,pslom.organization_id parentId,pslom.name,pslom.organization_status,pslom.city from"+ 
				" st_lms_organization_master slom inner join st_lms_organization_master pslom on pslom.organization_id=slom.parent_id inner join (select organization_id from st_lms_user_master where date(registration_date)<=? and isrolehead='Y' AND organization_type='AGENT') slum  on pslom.organization_id=slum.organization_id"+
				" where  slom.organization_type='RETAILER'";
		pstmt = con.prepareStatement(query);
		pstmt.setDate(1,date);
		logger.info(" create data table  "+pstmt);
		pstmt.executeUpdate();
	
		
		// live Retailers 
		query =" insert into st_lms_ret_activityHistory_agentwise(active_Ret,date,agent_id,status)  select sum(if(retailer_org_id is not null,1,0)) live ,'"+date+"' date,parentId,organization_status from (select retailer_org_id from st_lms_retailer_transaction_master rtm inner join st_lms_transaction_master tm on tm.transaction_id=rtm.transaction_id where service_code='DG' and interface='TERMINAL' and date(transaction_date)=?  group by retailer_org_id)slrtm "+
				" right join orgData on orgData.organization_id=slrtm.retailer_org_id  group by parentId order by parentId ";
		pstmt = con.prepareStatement(query);
		pstmt.setDate(1,date);
		logger.info(" live Retailers  "+pstmt);
		pstmt.executeUpdate();
		if(isCurrentDay){
			
			
			// new Login
			query="	create temporary table  newLogin select sum(if(newLogin=1 and current_owner_id is not null,1,0)) newLogin ,parentId from (select if(slrtm.retailer_org_id is null,1,0) newLogin ,orgData.organization_id,parentId from"+ 
					"(select retailer_org_id from st_lms_retailer_transaction_master rtm inner join st_lms_transaction_master tm on tm.transaction_id=rtm.transaction_id where service_code='DG' and interface='TERMINAL' and date(transaction_date)<=?  union select distinct organization_id from st_rep_dg_retailer  where  sale_mrp!=0 and finaldate<=? "+   
					" group by organization_id)slrtm right join orgData on orgData.organization_id=slrtm.retailer_org_id  ) main left join (select current_owner_id from st_lms_inv_status    inner join st_lms_inv_model_master invModel on inv_model_id=model_id  inner join  st_lms_inv_master invMaster on invModel.inv_id=invMaster.inv_id  where invMaster.inv_id=? and invMaster.inv_name=?  and current_owner_type =?  )invStatus "+ 
					"	on  invStatus.current_owner_id=main.organization_id group by parentId order by parentId ";
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			pstmt.setDate(2,date);
			pstmt.setInt(3,1);
			pstmt.setString(4,"Terminal");
			pstmt.setString(5,"RETAILER");
			logger.info(" new Login current day  "+pstmt);
			pstmt.executeUpdate();
		
			query ="update  st_lms_ret_activityHistory_agentwise  retAct ,newLogin  set newLogin_Ret=newLogin where retAct.agent_id=newLogin.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			query="drop table newLogin" ;
			pstmt = con.prepareStatement(query);
			logger.info(" update new Login current day  "+pstmt);
			pstmt.executeUpdate();
			// assigned 
			
			query="create temporary table  assign   select sum(if(current_owner_id is not null,1,0)) assigned,parentId from (select current_owner_id from st_lms_inv_status    inner join st_lms_inv_model_master invModel on inv_model_id=model_id  inner join  st_lms_inv_master invMaster on invModel.inv_id=invMaster.inv_id  where invMaster.inv_id=? and invMaster.inv_name=?  and current_owner_type =? )invStatus"+
					" right  join  orgData on (orgData.organization_id=invStatus.current_owner_id) group by parentId  order by parentId  ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,1);
			pstmt.setString(2,"Terminal");
			pstmt.setString(3,"RETAILER");
			logger.info("assigned current day  "+pstmt);
			pstmt.executeUpdate();
			query ="update  st_lms_ret_activityHistory_agentwise  retAct ,assign  set assigned_total=assigned where retAct.agent_id=assign.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			logger.info("update assigned current day  "+pstmt);
			pstmt.executeUpdate();
			query="drop table assign" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		
			// not assigned 
			query=" create temporary table   notassign select sum(if(current_owner_id is not null,1,0))  notassigned,parentId from (select current_owner_id from st_lms_inv_status  inner join st_lms_inv_model_master invModel on inv_model_id=model_id  inner join  st_lms_inv_master invMaster on invModel.inv_id=invMaster.inv_id  where invMaster.inv_id=?  and invMaster.inv_name=?  and  current_owner_type =? )invStatus "+
					" right  join (select distinct parentId from orgData) orgData on (orgData.parentId=invStatus.current_owner_id) group by parentId  order by parentId";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,1);
			pstmt.setString(2,"Terminal");
			pstmt.setString(3,"AGENT");
			logger.info("not assigned current day  "+pstmt);
			pstmt.executeUpdate();
			query ="update  st_lms_ret_activityHistory_agentwise  retAct , notassign  set notAssigned_total=notassigned where retAct.agent_id=notassign.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			logger.info("update not assigned current day  "+pstmt);
			query="drop table notassign" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			
			query=" select game_id  from   st_dg_game_master where (date(closing_time) > ?  or closing_time is null) " ;
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			logger.info(" gameIds current day  "+pstmt);
			rs=pstmt.executeQuery();
			
			int gameId =0;
			while(rs.next()){
				gameId =rs.getInt("game_id");
				query=" create temporary table   totalsale  select ifnull(sum(mrpAmt),0) sale,parentId,"+gameId+" gameId from ( select (ifnull(mrpAmt,0.0)-ifnull(mrpAmtRef,0.0))mrpAmt,sale.retailer_org_id from"+ 
					" (select game_id,sum(mrp_amt)  mrpAmt,retailer_org_id from st_dg_ret_sale_"+gameId+"   where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE')  "+
					"	and date (transaction_date)=?)  group by retailer_org_id ) sale  left join (select sum(mrp_amt)  mrpAmtRef,retailer_org_id from st_dg_ret_sale_refund_"+gameId+"   where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')"+
					" and date   (transaction_date)=?)  group by retailer_org_id  )saleRet on sale.retailer_org_id=saleRet.retailer_org_id )totalSale right     join orgData on totalSale.retailer_org_id = orgData.organization_id  group by parentId   " ;
				pstmt = con.prepareStatement(query);
				pstmt.setDate(1,date);
				pstmt.setDate(2,date);
				logger.info(" sale  current day  "+pstmt);
				pstmt.executeUpdate();
				query ="insert into   st_lms_ret_saleHistory_agentwise  select task_id,gameId,sale from totalsale, st_lms_ret_activityHistory_agentwise retAct  where retAct.agent_id=totalsale.parentId  and date='"+date+"'";
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
				query="drop table totalsale" ;
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
				
			}

			
			
			
			
		}else {
			
			// temp for retailers id owning terminal for the date 
			
			query =" create temporary table terminalStatus (PRIMARY KEY(current_owner_id )) select count(distinct sts.serial_no) totalTerminal ,invDet.current_owner_id from st_lms_inv_status  sts left join (select det1.serial_no,det1.date,det1.user_org_type,det1.current_owner_type,det1.inv_model_id,det1.current_owner_id  from st_lms_inv_detail det1"+ 
					" inner join (select max(date) date ,serial_no  from st_lms_inv_detail where date(date)<=?  group by serial_no )  det2 on  det1.date =det2.date and det1.serial_no =det2.serial_no )  invDet"+
					" on  invDet.serial_no =sts.serial_no   inner join st_lms_inv_model_master invModel on invDet.inv_model_id=model_id  inner join  st_lms_inv_master invMaster on invModel.inv_id=invMaster.inv_id  where invMaster.inv_id=1 and invMaster.inv_name='Terminal' and invDet.current_owner_type ='RETAILER'   group  by current_owner_id";
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			logger.info("  temp for retailers id owning terminal for   previous day  "+pstmt);
			pstmt.executeUpdate();
			// new logined
			query=" create temporary table  newLogin select sum(if(newLogin=1 and current_owner_id is not null,1,0)) newLogin ,parentId,name,organization_status,city from (select if(slrtm.retailer_org_id is null,1,0) newLogin ,orgData.organization_id,parentId,name,organization_status,city from"+ 
					" (select retailer_org_id from st_lms_retailer_transaction_master rtm inner join st_lms_transaction_master tm on tm.transaction_id=rtm.transaction_id where service_code='DG' and interface='TERMINAL' and date(transaction_date)<=?  union select distinct organization_id from st_rep_dg_retailer  where  sale_mrp!=0 and finaldate<=? "+   
					" group by organization_id)slrtm right join orgData on orgData.organization_id=slrtm.retailer_org_id ) main left join terminalStatus invStatus on  invStatus.current_owner_id=main.organization_id group by parentId order by parentId  " ;	
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			pstmt.setDate(2,date);
			logger.info("  new logined for   previous day  "+pstmt);
			pstmt.executeUpdate();
			query ="update  st_lms_ret_activityHistory_agentwise  retAct ,newLogin  set newLogin_Ret=newLogin where retAct.agent_id=newLogin.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			logger.info("  update   previous day  "+pstmt);
			pstmt.executeUpdate();
			query="drop table newLogin" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			
			
			// assigned 
			query="	create temporary table   assign  select sum(ifnull(totalTerminal,0)) assigned,parentId from terminalStatus right join " +
					" orgData on current_owner_id =organization_id   group  by parentId   order by parentId";
			pstmt = con.prepareStatement(query);
			logger.info(" assigned for   previous day  "+pstmt);
			pstmt.executeUpdate();
			query ="update  st_lms_ret_activityHistory_agentwise  retAct ,assign  set assigned_total=assigned where retAct.agent_id=assign.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			logger.info(" update assigned for   previous day  "+pstmt);
			pstmt.executeUpdate();
			query="drop table assign" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			query="drop table terminalStatus" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			
			
			// not assigned 
			
			query ="create temporary table  notassign select sum(ifnull(assigned,0)) notassigned,parentId from ( SELECT count(distinct sts.serial_no) assigned ,invDet.current_owner_id   from st_lms_inv_status sts "+
					" left join (select det1.serial_no,det1.date,det1.user_org_type,det1.current_owner_type,det1.inv_model_id,det1.current_owner_id  from st_lms_inv_detail det1 "+ 
					"	inner join (select max(date) date,serial_no from st_lms_inv_detail where date(date)<=?  group by serial_no )  det2 on  det1.date =det2.date  and det1.serial_no =det2.serial_no  )  invDet "+
					" on  invDet.serial_no =sts.serial_no inner join st_lms_inv_model_master invModel on invDet.inv_model_id=model_id inner join st_lms_inv_master invMaster on invModel.inv_id=invMaster.inv_id where invMaster.inv_id=? and invMaster.inv_name=?  and invDet.current_owner_type=? and sts.current_owner_type!='REMOVED' group  by current_owner_id  )main "+ 
					"	right join  (select distinct  parentId from orgData) orgData on main.current_owner_id =parentId group  by parentId   order by parentId";
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			pstmt.setInt(2,1);
			pstmt.setString(3,"Terminal");
			pstmt.setString(4,"AGENT");		
			logger.info("  not assigned  for   previous day  "+pstmt);
			pstmt.executeUpdate();
			query ="update  st_lms_ret_activityHistory_agentwise  retAct , notassign  set notAssigned_total=notassigned where retAct.agent_id=notassign.parentId and date='"+date+"'";
			pstmt = con.prepareStatement(query);
			logger.info("  update assigned  for   previous day  "+pstmt);
			pstmt.executeUpdate();
			query="drop table notassign" ;
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			
			query=" select game_id  from   st_dg_game_master where (date(closing_time) > ?  or closing_time is null) " ;
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1,date);
			rs=pstmt.executeQuery();
			
			int gameId =0;
			while(rs.next()){
				gameId =rs.getInt("game_id");
				query=" create temporary table   totalsale  select ifnull(sum(mrpAmt),0) sale,parentId,"+gameId+" gameId from ( select (ifnull(mrpAmt,0.0)-ifnull(mrpAmtRef,0.0))mrpAmt,sale.retailer_org_id from"+ 
					" (select game_id,sum(mrp_amt)  mrpAmt,retailer_org_id from st_dg_ret_sale_"+gameId+"   where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE')  "+
					"	and date (transaction_date)=?)  group by retailer_org_id ) sale  left join (select sum(mrp_amt)  mrpAmtRef,retailer_org_id from st_dg_ret_sale_refund_"+gameId+"   where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')"+
					" and date   (transaction_date)=?)  group by retailer_org_id  )saleRet on sale.retailer_org_id=saleRet.retailer_org_id )totalSale right     join orgData on totalSale.retailer_org_id = orgData.organization_id  group by parentId   " ;
				pstmt = con.prepareStatement(query);
				pstmt.setDate(1,date);
				pstmt.setDate(2,date);
				logger.info("  sale   for   previous day  "+pstmt);
				pstmt.executeUpdate();
				query ="insert into   st_lms_ret_saleHistory_agentwise  select task_id,gameId,sale from totalsale, st_lms_ret_activityHistory_agentwise retAct  where retAct.agent_id=totalsale.parentId  and date='"+date+"'";
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
				query="drop table totalsale" ;
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
				
			}
			
			
			
			
			
			
		}
		query="drop table orgData" ;
		pstmt = con.prepareStatement(query);
		pstmt.executeUpdate();
		logger.info("  agt wise ret history updates successfully  "+pstmt);
		con.commit();
	}catch(Exception e){
		con.rollback();
		e.printStackTrace();
	}finally{
		try{
			
			if(pstmt!=null){
				
				pstmt.close();
				
			}
			if(rs!=null){
				
				rs.close();
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}	

}