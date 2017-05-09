package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.drawGames.common.Util;

public class reportsMgmtUtility {
	private static Log logger = LogFactory.getLog(reportsMgmtUtility.class);
	public static void getCommonDailyRetActivity() throws ParseException, LMSException, SQLException
	{
		String drawDate = getStartNEndDates();
		String csDate = getStartNEndDatesForCS();
		String slDate=getStartNEndDatesForSL();
		String iwDate=getStartNEndDatesForIW();
		String vsDate = getStartNEndDates("VS");
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		Calendar calStart1 = Calendar.getInstance();
		Calendar calStart2=Calendar.getInstance();
		Calendar calStart3=Calendar.getInstance();
		Calendar calStart4 = Calendar.getInstance();
		SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
		Date darwDate1 = frmt.parse(drawDate);
		Date csDate1 = frmt.parse(csDate);
		Date slDate1=frmt.parse(slDate);
		Date iwDate1=frmt.parse(iwDate);
		Date vsDate1 = frmt.parse(vsDate);
		System.out.println(darwDate1);
		Date endDate = frmt.parse(new java.sql.Date(new Date().getTime()) + "");
		calStart.setTime(darwDate1);
		calStart1.setTime(csDate1);
		calStart2.setTime(slDate1);
		calStart3.setTime(iwDate1);
		calStart4.setTime(vsDate1);
		calEnd.setTime(endDate);
		//calEnd.add(Calendar.DAY_OF_MONTH, 1);
		while (calStart.compareTo(calEnd) < 0)
		{
			java.sql.Date date = new java.sql.Date(calStart.getTime()
					.getTime());
			String date1 = frmt.format(date);
			System.out.println(date1);
			if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw())){
					getDailyRetActivity(date1);
			}
			calStart.add(Calendar.DAY_OF_MONTH, 1);
		}
		while (calStart1.compareTo(calEnd) < 0)
		{
			java.sql.Date secondDate = new java.sql.Date(calStart1.getTime()
					.getTime());
			String date2 = frmt.format(secondDate);
			System.out.println(date2);
			if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsCS()))
			{
			  getDailyRetActivityForCS(date2);
			}
			calStart1.add(Calendar.DAY_OF_MONTH, 1);
		}
		while(calStart2.compareTo(calEnd)<0)
		{
			java.sql.Date date2=new java.sql.Date(calStart2.getTime().getTime());
			String date3=frmt.format(date2);
			if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE()))
			{
			  getDailyRetActivityForSL(date3);
			}
			calStart2.add(Calendar.DAY_OF_MONTH, 1);
		}
		while (calStart3.compareTo(calEnd) < 0) {
			java.sql.Date date2 = new java.sql.Date(calStart3.getTime().getTime());
			String date3 = frmt.format(date2);
			if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW())) {
				getDailyRetActivityForIW(date3);
			}
			calStart3.add(Calendar.DAY_OF_MONTH, 1);
		}

		while (calStart4.compareTo(calEnd) < 0) {
			if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS())) {
				java.sql.Date date = new java.sql.Date(calStart4.getTime().getTime());
				String dateString = frmt.format(date);

				getDailyRetActivityForVS(dateString);
			}
			calStart4.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public static String getStartNEndDates() {
		Connection con = DBConnect.getConnection();
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			con.setAutoCommit(false);
			PreparedStatement pStatement = con
					.prepareStatement("SELECT date FROM st_lms_ret_activity_history ORDER BY date DESC LIMIT 1");
			ResultSet rSet = pStatement.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rSet.next()) {
				startDate = rSet.getDate("date");
			}
			Calendar calStart = Calendar.getInstance();
			java.util.Date currDate = new java.util.Date(calStart
					.getTimeInMillis());
						if(startDate!=null)
						{
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH, 1);
						}
						else
						{
							calStart.setTime(currDate);	
							calStart.add(Calendar.DAY_OF_MONTH, -1);
						}
						sb.append(sdf.format(calStart.getTimeInMillis()));
					con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	public static String getStartNEndDatesForSL()
	{
		Connection con = DBConnect.getConnection();
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			con.setAutoCommit(false);
			PreparedStatement pStatement = con
					.prepareStatement("SELECT date FROM st_sle_ret_activity_history ORDER BY date DESC LIMIT 1");
			ResultSet rSet = pStatement.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rSet.next()) {
				startDate = rSet.getDate("date");
			}
			Calendar calStart = Calendar.getInstance();
			java.util.Date currDate = new java.util.Date(calStart
					.getTimeInMillis());
						if(startDate!=null)
						{
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH, 1);
						}
						else
						{
							calStart.setTime(currDate);	
							calStart.add(Calendar.DAY_OF_MONTH, -1);
						}
						sb.append(sdf.format(calStart.getTimeInMillis()));
					con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String getStartNEndDatesForIW() {
		Connection con = DBConnect.getConnection();
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			con.setAutoCommit(false);
			PreparedStatement pStatement = con.prepareStatement("SELECT date FROM st_iw_ret_activity_history ORDER BY date DESC LIMIT 1");
			ResultSet rSet = pStatement.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rSet.next()) {
				startDate = rSet.getDate("date");
			}
			Calendar calStart = Calendar.getInstance();
			java.util.Date currDate = new java.util.Date(calStart.getTimeInMillis());
			if (startDate != null) {
				calStart.setTime(startDate);
				calStart.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				calStart.setTime(currDate);
				calStart.add(Calendar.DAY_OF_MONTH, -1);
			}
			sb.append(sdf.format(calStart.getTimeInMillis()));
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static String getStartNEndDates(String serviceName) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		SimpleDateFormat dateFormat = null;
		String returnDate = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			String tableName = null;
			if("VS".equals(serviceName))
				tableName = "st_vs_ret_activity_history";
			String query = "SELECT date FROM "+tableName+" ORDER BY date DESC LIMIT 1;";

			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			logger.info("getStartNEndDates - "+query);
			rs = stmt.executeQuery(query);
			Date startDate = null;
			if (rs.next())
				startDate = rs.getDate("date");

			Calendar calendar = Calendar.getInstance();
			Date currentDate = new java.util.Date();
			if (startDate != null) {
				calendar.setTime(startDate);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				calendar.setTime(currentDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}

			returnDate = dateFormat.format(calendar.getTimeInMillis());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(connection);
		}

		logger.info("Return Date - "+returnDate);
		return returnDate.toString();
	}

	public static String getStartNEndDatesForCS() {
		Connection con = DBConnect.getConnection();
		Date startDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			con.setAutoCommit(false);
			PreparedStatement pStatement = con
					.prepareStatement("SELECT date FROM st_cs_ret_activity_history ORDER BY date DESC LIMIT 1");
			ResultSet rSet = pStatement.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rSet.next()) {
				startDate = rSet.getDate("date");
			}
			Calendar calStart = Calendar.getInstance();
			java.util.Date currDate = new java.util.Date(calStart
					.getTimeInMillis());
						if(startDate!=null)
						{
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH, 1);
						}
						else
						{
							calStart.setTime(currDate);	
							calStart.add(Calendar.DAY_OF_MONTH, -1);
						}
						sb.append(sdf.format(calStart.getTimeInMillis()));
					con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	public static void getDailyRetActivity(String date) throws LMSException{
	// TreeMap<Integer, ArrayList<Double>> gameDataMap = new TreeMap<Integer, ArrayList<Double>>();
		 
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<Double> totAmt = null;
		int gameNum = 0;
		//List<Integer> gameNumList = Util.getGameNumberList();
		List<Integer> gameNumList =Util.getLMSGameNumberList();

		int liveRets = 0;
		int noSaleRets = 0;
		int inactiveRets = 0;
		int terminateRets = 0;
		double totalSale = 0.0;
		double totalPwt = 0.0;
		int totTktCnt = 0;
		int totPwtCnt = 0;
		double avgSalePerRet = 0.0;

		//String date = new java.sql.Date(new Date().getTime()) + "";

		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		String trxDate = "transaction_date>'" + fromDate
				+ "' and transaction_date<'" + toDate + "'";
		String retQry = "select count(distinct(retailer_org_id)) retCount from st_lms_retailer_transaction_master where transaction_type not in ('CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET') and "
				+ trxDate;

		String trxQry = "select transaction_id from st_lms_retailer_transaction_master where "
				+ trxDate;
		String saleQry = "select sum(mrp_amt) res from st_dg_ret_sale_? where transaction_id in ("
				+ trxQry + ")";
		String saleRetQry = "select sum(mrp_amt) res  from st_dg_ret_sale_refund_? where transaction_id in ("
				+ trxQry + ")";
		String pwtQry = "select sum(pwt_amt) res from st_dg_ret_pwt_? where transaction_id in ("
				+ trxQry + ")";
		String combinedQry = saleQry + " union all " + saleRetQry
				+ " union all " + pwtQry;

		String dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_dg_bo_direct_plr_pwt where "
				+ trxDate
				+ ") a,(select sum(pwt_amt) pwt_amt from st_dg_agt_direct_plr_pwt where "
				+ trxDate + ") b";
		String retSelQry="select count(distinct(organization_id)) retCount from st_lms_organization_master where organization_type='RETAILER'";
		String inactiveQry=retSelQry+" and organization_status='INACTIVE'";
		String terQry=retSelQry+" and organization_status='TERMINATE'";
		//String nosaleQry=retSelQry+" and organization_status='ACTIVE' and organization_id not in (select distinct(retailer_org_id) from st_lms_retailer_transaction_master where transaction_type not in ('CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET') and "+trxDate+" )";
		String nosaleQry=" SELECT count(distinct(a.organization_id)) retCount from ( (select distinct(organization_id) from st_lms_organization_master where organization_type='RETAILER' and organization_status='ACTIVE')a LEFT JOIN (select distinct(retailer_org_id) retailer_org_id from st_lms_retailer_transaction_master where transaction_type not in ('CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET')"+ 
							" and "+trxDate+" )b on a.organization_id=b.retailer_org_id )WHERE b.retailer_org_id IS NULL";
		String combinedCntQry=retQry+" union all "+inactiveQry+" union all "+terQry+" union all "+nosaleQry;
		double tempRetPwt=0.0;
		try {
		for (int i = 0; i < gameNumList.size(); i++) {
			gameNum = gameNumList.get(i);
			totAmt = new ArrayList<Double>();
			pstmt = con.prepareStatement(combinedQry);
			pstmt.setInt(1, gameNum);
			pstmt.setInt(2, gameNum);
			pstmt.setInt(3, gameNum);
			System.out.println("Q1:"+pstmt);
			rs = pstmt.executeQuery();
				
			
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			totalSale+=(totAmt.get(0)-totAmt.get(1));
			tempRetPwt+=totAmt.get(2);
			//gameDataMap.put(gameNum, totAmt);
		}
		// direct player pwt 
		double dirPlrPwt=0.0;
		pstmt = con.prepareStatement(dirPlrQry);
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			dirPlrPwt = rs.getInt("dirPwt");
		}
		
		// total sale & Pwt
		/*double tempRetPwt=0.0;
		Iterator<Map.Entry<Integer, ArrayList<Double>>> itr = gameDataMap.entrySet().iterator();
		while (itr.hasNext()){
			Map.Entry<Integer, ArrayList<Double>> pair = itr.next();
			ArrayList<Double> list = (ArrayList<Double>)pair.getValue();
			totalSale+=(list.get(0)-list.get(1));
			tempRetPwt+=list.get(2);
			
		}*/
		totalPwt = tempRetPwt+dirPlrPwt;
		
		// Retailer count query
		pstmt=con.prepareStatement(combinedCntQry);
		System.out.println("combined query:"+combinedCntQry);
		rs=pstmt.executeQuery();
		List<Integer> retCntList = new ArrayList<Integer>();
		while(rs.next()){
			retCntList.add(rs.getInt("retCount"));
		}
		
		if(retCntList.size()>0){
			liveRets=retCntList.get(0);
			inactiveRets=retCntList.get(1);
			terminateRets=retCntList.get(2);
			noSaleRets=retCntList.get(3);
		}
		
		
		// calculating avgSalePerRet 
		if(totalSale != 0.0 && liveRets !=0)
			 avgSalePerRet = totalSale/liveRets;
			
		/*
		// total noSale rets
		pstmt = con
				.prepareStatement("select count(distinct(organization_id)) retNSCount from " +
						"st_lms_organization_master where organization_type='RETAILER' and " +
						"organization_status='ACTIVE' and organization_id not " +
						"in (select distinct(retailer_org_id) from st_lms_retailer_transaction_master where "+trxDate+" )");
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			noSaleRets = rs.getInt("retNSCount");
		}
		// total Live Rets
		pstmt = con.prepareStatement(retQry);
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			liveRets = rs.getInt("retCount");
		}
		// total Inactive rets
		pstmt = con
				.prepareStatement("select count(distinct(organization_id)) retInCount from " +
						"st_lms_organization_master where organization_type='RETAILER'" +
						" and organization_status='INACTIVE'");
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			inactiveRets = rs.getInt("retInCount");
		}
		// total terminate rets
		pstmt = con
				.prepareStatement("select count(distinct(organization_id)) retTrCount from " +
						"st_lms_organization_master where organization_type='RETAILER'" +
						" and organization_status='TERMINATE'");
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			terminateRets = rs.getInt("retTrCount");
		}*/
		
		
		
		// total ticket sold 
		pstmt = con.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from " +
				"(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where ("+trxDate+") and " +
						"(transaction_type ='DG_SALE' or transaction_type='DG_SALE_OFFLINE') )sale," +
						"(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where ("+
						trxDate+") and (transaction_type ='DG_REFUND_CANCEL' or transaction_type='DG_REFUND_FAILED'))ref");
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			totTktCnt = rs.getInt("netSaleCnt");
		}
		//total PWT count 
		pstmt = con.prepareStatement("select (ret.retPwt+agt.agtPwt+bo.boPwt) as pwtCnt from " +
				"((select ifnull(count(*),0) retPwt  from  st_lms_retailer_transaction_master where "+trxDate +" and  " +
						"transaction_type='DG_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_dg_agt_direct_plr_pwt where "+
						trxDate + ")agt,(select ifnull(count(*),0) boPwt from st_dg_bo_direct_plr_pwt where "+trxDate+" )bo" );
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			totPwtCnt = rs.getInt("pwtCnt");
		}
			
		// done columns
		System.out.println("liveRets:" + liveRets);
		System.out.println("inactiveRets:" + inactiveRets);
		System.out.println("terminateRets:" + terminateRets);
		System.out.println("noSaleRets:" + noSaleRets);
		System.out.println("totalSale:" + totalSale);
		System.out.println("totalPwt:" + totalPwt);
		System.out.println("avgSalePerRet:" + avgSalePerRet);
		System.out.println("totTktCnt:" + totTktCnt);
		System.out.println("totPwtCnt:" + totPwtCnt);
		String insertvalues = "insert into st_lms_ret_activity_history (date,live_retailers,noSale_retailers,inactive_retailers,terminated_retailers,total_sales,total_pwt,total_tkt_count,total_pwt_count,avg_sale_per_ret) values (?,?,?,?,?,?,?,?,?,?)";
		pstmt=con.prepareStatement(insertvalues);
		pstmt.setString(1,fromDate);
		pstmt.setInt(2, liveRets);
		pstmt.setInt(3, noSaleRets);
		pstmt.setInt(4, inactiveRets);
		pstmt.setInt(5, terminateRets);
		pstmt.setDouble(6, totalSale);
		pstmt.setDouble(7, totalPwt);
		pstmt.setInt(8,totTktCnt);
		pstmt.setInt(9, totPwtCnt);
		pstmt.setDouble(10, avgSalePerRet);
		System.out.println("Q1:"+pstmt);
		pstmt.executeUpdate();
		rs = pstmt.getGeneratedKeys();
		int retActId =0 ;
		if(rs.next()){
			retActId=rs.getInt(1);
		}
		if(retActId>0)
			System.out.println("Data for the day :"+date+" inserted successfully ");
		else
			System.out.println("Data for the day :"+date+"not inserted ");
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

public static void getDailyRetActivityForSL(String date) throws LMSException,SQLException
{
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<Double> totAmt = null;
		int liveRets = 0;
		int noSaleRets = 0;
		int inactiveRets = 0;
		int terminateRets = 0;
		double totalSale = 0.0;
		double totalPwt = 0.0;
		int totTktCnt = 0;
		int totPwtCnt = 0;
		double avgSalePerRet = 0.0;

		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		String trxDate = "transaction_date>'" + fromDate
				+ "' and transaction_date<'" + toDate + "'";
		String retQry = "select count(distinct(retailer_org_id)) retCount from st_lms_retailer_transaction_master where transaction_type in ('SLE_PWT','SLE_REFUND_CANCEL','SLE_SALE') and "
				+ trxDate;
		String trxQry = "select transaction_id from st_lms_retailer_transaction_master where "
				+ trxDate;
		String saleQry = "select sum(mrp_amt) res from st_sle_ret_sale where transaction_id in ("
				+ trxQry + ")";
		String saleRetQry = "select sum(mrp_amt) res  from st_sle_ret_sale_refund where transaction_id in ("
				+ trxQry + ")";
		String pwtQry = "select sum(pwt_amt) res from st_sle_ret_pwt where transaction_id in ("
				+ trxQry + ")";
		String combinedQry = saleQry + " union all " + saleRetQry
				+ " union all " + pwtQry;
		String dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_sle_bo_direct_plr_pwt where "
				+ trxDate
				+ ") a,(select sum(pwt_amt) pwt_amt from st_sle_agent_direct_plr_pwt where "
				+ trxDate + ") b";
		String retSelQry="select count(distinct(organization_id)) retCount from st_lms_organization_master where organization_type='RETAILER'";
		String inactiveQry=retSelQry+" and organization_status='INACTIVE'";
		String terQry=retSelQry+" and organization_status='TERMINATE'";
		String nosaleQry=" SELECT count(distinct(a.organization_id)) retCount from ( (select distinct(organization_id) from st_lms_organization_master where organization_type='RETAILER' and organization_status='ACTIVE')a LEFT JOIN (select distinct(retailer_org_id) retailer_org_id from st_lms_retailer_transaction_master where transaction_type in ('SLE_PWT','SLE_REFUND_CANCEL','SLE_SALE')"+ 
							" and "+trxDate+" )b on a.organization_id=b.retailer_org_id )WHERE b.retailer_org_id IS NULL";
		String combinedCntQry=retQry+" union all "+inactiveQry+" union all "+terQry+" union all "+nosaleQry;
		double tempRetPwt=0.0;
		try {
			totAmt = new ArrayList<Double>();
			pstmt = con.prepareStatement(combinedQry);
			logger.info("Combined query for sale,sale_refund and pwt:"+pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			totalSale += (totAmt.get(0) - totAmt.get(1));
			tempRetPwt += totAmt.get(2);
			// direct player pwt
			double dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			logger.info("Query for direct player pwt:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}
			// total sale & Pwt
			totalPwt = tempRetPwt + dirPlrPwt;
			// Retailer count query
			pstmt = con.prepareStatement(combinedCntQry);
			logger.info("combined query:"+combinedCntQry);
			rs = pstmt.executeQuery();
			List<Integer> retCntList = new ArrayList<Integer>();
			while (rs.next()) {
				retCntList.add(rs.getInt("retCount"));
			}
			if (retCntList.size() > 0) {
				liveRets = retCntList.get(0);
				inactiveRets = retCntList.get(1);
				terminateRets = retCntList.get(2);
				noSaleRets = retCntList.get(3);
			}
			// calculating avgSalePerRet
			if (totalSale != 0.0 && liveRets != 0)
				avgSalePerRet = totalSale / liveRets;
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
			
			logger.info("Q1:"+pstmt);
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
							+ "transaction_type='SLE_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_sle_agent_direct_plr_pwt where "
							+ trxDate
							+ ")agt,(select ifnull(count(*),0) boPwt from st_sle_bo_direct_plr_pwt where "
							+ trxDate + " )bo");
			logger.info("Q1:"+pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totPwtCnt = rs.getInt("pwtCnt");
			}
			// done columns
			logger.info("liveRets:" + liveRets);
			logger.info("inactiveRets:" + inactiveRets);
			logger.info("terminateRets:" + terminateRets);
			logger.info("noSaleRets:" + noSaleRets);
			logger.info("totalSale:" + totalSale);
			logger.info("totalPwt:" + totalPwt);
			logger.info("avgSalePerRet:" + avgSalePerRet);
			logger.info("totTktCnt:" + totTktCnt);
			logger.info("totPwtCnt:" + totPwtCnt);
			String insertvalues = "insert into st_sle_ret_activity_history (date,live_retailers,noSale_retailers,inactive_retailers,terminated_retailers,total_sales,total_pwt,total_tkt_count,total_pwt_count,avg_sale_per_ret) values (?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(insertvalues);
			pstmt.setString(1, fromDate);
			pstmt.setInt(2, liveRets);
			pstmt.setInt(3, noSaleRets);
			pstmt.setInt(4, inactiveRets);
			pstmt.setInt(5, terminateRets);
			pstmt.setDouble(6, totalSale);
			pstmt.setDouble(7, totalPwt);
			pstmt.setInt(8, totTktCnt);
			pstmt.setInt(9, totPwtCnt);
			pstmt.setDouble(10, avgSalePerRet);
			System.out.println("Q1:" + pstmt);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			int retActId = 0;
			if (rs.next()) {
				retActId = rs.getInt(1);
			}
			if (retActId > 0)
				logger.info("Data for the day :" + date+ " inserted successfully ");
			else
				logger.info("Data for the day :" + date+ "not inserted ");
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

	public static void getDailyRetActivityForIW(String date) throws LMSException, SQLException {
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<Double> totAmt = null;
		int liveRets = 0;
		int noSaleRets = 0;
		int inactiveRets = 0;
		int terminateRets = 0;
		double totalSale = 0.0;
		double totalPwt = 0.0;
		int totTktCnt = 0;
		int totPwtCnt = 0;
		double avgSalePerRet = 0.0;

		String fromDate = date + " 00:00:00";
		String toDate = date + " 23:59:59";
		String trxDate = "transaction_date>'" + fromDate + "' and transaction_date<'" + toDate + "'";
		String retQry = "select count(distinct(retailer_org_id)) retCount from st_lms_retailer_transaction_master where transaction_type in ('IW_PWT','IW_REFUND_CANCEL','IW_SALE') and " + trxDate;
		String trxQry = "select transaction_id from st_lms_retailer_transaction_master where " + trxDate;
		String saleQry = "select sum(mrp_amt) res from st_iw_ret_sale where transaction_id in (" + trxQry + ")";
		String saleRetQry = "select sum(mrp_amt) res  from st_iw_ret_sale_refund where transaction_id in (" + trxQry + ")";
		String pwtQry = "select sum(pwt_amt) res from st_iw_ret_pwt where transaction_id in (" + trxQry + ")";
		String combinedQry = saleQry + " union all " + saleRetQry + " union all " + pwtQry;
		String dirPlrQry = "select if(a.pwt_amt is null ,0,a.pwt_amt)+if(b.pwt_amt is null ,0,b.pwt_amt) dirPwt from (select sum(pwt_amt) pwt_amt from st_iw_bo_direct_plr_pwt where " + trxDate + ") a,(select sum(pwt_amt) pwt_amt from st_iw_agent_direct_plr_pwt where " + trxDate + ") b";
		String retSelQry = "select count(distinct(organization_id)) retCount from st_lms_organization_master where organization_type='RETAILER'";
		String inactiveQry = retSelQry + " and organization_status='INACTIVE'";
		String terQry = retSelQry + " and organization_status='TERMINATE'";
		String nosaleQry = " SELECT count(distinct(a.organization_id)) retCount from ( (select distinct(organization_id) from st_lms_organization_master where organization_type='RETAILER' and organization_status='ACTIVE')a LEFT JOIN (select distinct(retailer_org_id) retailer_org_id from st_lms_retailer_transaction_master where transaction_type in ('IW_PWT','IW_REFUND_CANCEL','IW_SALE')" + " and " + trxDate + " )b on a.organization_id=b.retailer_org_id )WHERE b.retailer_org_id IS NULL";
		String combinedCntQry = retQry + " union all " + inactiveQry + " union all " + terQry + " union all " + nosaleQry;
		double tempRetPwt = 0.0;
		try {
			totAmt = new ArrayList<Double>();
			pstmt = con.prepareStatement(combinedQry);
			logger.info("Combined query for sale, sale_refund and pwt:" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				totAmt.add(rs.getDouble("res"));
			}
			totalSale += (totAmt.get(0) - totAmt.get(1));
			tempRetPwt += totAmt.get(2);
			// direct player pwt
			double dirPlrPwt = 0.0;
			pstmt = con.prepareStatement(dirPlrQry);
			logger.info("Query for direct player pwt:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwt = rs.getInt("dirPwt");
			}
			// total sale & Pwt
			totalPwt = tempRetPwt + dirPlrPwt;
			// Retailer count query
			pstmt = con.prepareStatement(combinedCntQry);
			logger.info("combined query:" + combinedCntQry);
			rs = pstmt.executeQuery();
			List<Integer> retCntList = new ArrayList<Integer>();
			while (rs.next()) {
				retCntList.add(rs.getInt("retCount"));
			}
			if (retCntList.size() > 0) {
				liveRets = retCntList.get(0);
				inactiveRets = retCntList.get(1);
				terminateRets = retCntList.get(2);
				noSaleRets = retCntList.get(3);
			}
			// calculating avgSalePerRet
			if (totalSale != 0.0 && liveRets != 0)
				avgSalePerRet = totalSale / liveRets;
			// total ticket sold
			pstmt = con.prepareStatement("select (sale.saleCnt - ref.refCnt) as netSaleCnt from " + "(select ifnull(count(*),0) saleCnt from st_lms_retailer_transaction_master where (" + trxDate + ") and " + "(transaction_type ='IW_SALE') )sale," + "(select ifnull(count(*),0)refCnt from st_lms_retailer_transaction_master where (" + trxDate + ") and (transaction_type ='IW_REFUND_CANCEL'))ref");
			logger.info("Q1:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totTktCnt = rs.getInt("netSaleCnt");
			}
			// total PWT count
			pstmt = con.prepareStatement("select (ret.retPwt + agt.agtPwt + bo.boPwt) as pwtCnt from " + "((select ifnull(count(*),0) retPwt from st_lms_retailer_transaction_master where " + trxDate + " and  " + "transaction_type='IW_PWT'))ret,(select ifnull(count(*),0) agtPwt from st_iw_agent_direct_plr_pwt where " + trxDate + ")agt,(select ifnull(count(*),0) boPwt from st_iw_bo_direct_plr_pwt where " + trxDate + " )bo");
			logger.info("Q1:" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totPwtCnt = rs.getInt("pwtCnt");
			}
			// done columns
			logger.info("liveRets:" + liveRets);
			logger.info("inactiveRets:" + inactiveRets);
			logger.info("terminateRets:" + terminateRets);
			logger.info("noSaleRets:" + noSaleRets);
			logger.info("totalSale:" + totalSale);
			logger.info("totalPwt:" + totalPwt);
			logger.info("avgSalePerRet:" + avgSalePerRet);
			logger.info("totTktCnt:" + totTktCnt);
			logger.info("totPwtCnt:" + totPwtCnt);
			String insertvalues = "insert into st_iw_ret_activity_history (date,live_retailers,noSale_retailers,inactive_retailers,terminated_retailers,total_sales,total_pwt,total_tkt_count,total_pwt_count,avg_sale_per_ret) values (?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(insertvalues);
			pstmt.setString(1, fromDate);
			pstmt.setInt(2, liveRets);
			pstmt.setInt(3, noSaleRets);
			pstmt.setInt(4, inactiveRets);
			pstmt.setInt(5, terminateRets);
			pstmt.setDouble(6, totalSale);
			pstmt.setDouble(7, totalPwt);
			pstmt.setInt(8, totTktCnt);
			pstmt.setInt(9, totPwtCnt);
			pstmt.setDouble(10, avgSalePerRet);
			System.out.println("Q1:" + pstmt);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			int retActId = 0;
			if (rs.next()) {
				retActId = rs.getInt(1);
			}
			if (retActId > 0)
				logger.info("Data for the day :" + date + " inserted successfully ");
			else
				logger.info("Data for the day :" + date + "not inserted ");
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

	public static void getDailyRetActivityForVS(String date) throws LMSException, SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = DBConnect.getConnection();

			String fromDate = date + " 00:00:00";
			String toDate = date + " 23:59:59";

			List<Double> totalAmount = new ArrayList<Double>();
			pstmt = connection.prepareStatement("SELECT SUM(mrp_amt) res FROM st_vs_ret_sale WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') UNION ALL SELECT SUM(mrp_amt) res FROM st_vs_ret_sale_refund WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') UNION ALL SELECT SUM(pwt_amt) res FROM st_vs_ret_pwt WHERE transaction_id IN (SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "');");
			logger.info("Combined Query for SALE, CANCEL, PWT - " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				totalAmount.add(rs.getDouble("res"));
			}
			double totalSale = 0.0;
			double normalPwt = 0.0;
			totalSale += totalAmount.get(0) - totalAmount.get(1);
			normalPwt += totalAmount.get(2);

			double dirPlayerPwt = 0.0;
			pstmt = connection.prepareStatement("SELECT IF(a.pwt_amt IS NULL, 0, a.pwt_amt) + IF(b.pwt_amt IS NULL, 0, b.pwt_amt) dirPwt FROM (SELECT SUM(pwt_amt) pwt_amt FROM st_vs_bo_direct_plr_pwt WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') a, (SELECT SUM(pwt_amt) pwt_amt FROM st_vs_agent_direct_plr_pwt WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') b;");
			logger.info("Query for Direct Player PWT - " + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlayerPwt = rs.getInt("dirPwt");
			}
			double totalPwt = 0.0;
			totalPwt = normalPwt + dirPlayerPwt;

			List<Integer> retailerCountList = new ArrayList<Integer>();
			pstmt = connection.prepareStatement("SELECT COUNT(DISTINCT(retailer_org_id)) retCount FROM st_lms_retailer_transaction_master WHERE transaction_type IN ('VS_SALE','VS_REFUND_CANCEL','VS_PWT') AND transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "' UNION ALL SELECT COUNT(DISTINCT(organization_id)) retCount FROM st_lms_organization_master WHERE organization_type='RETAILER' AND organization_status='INACTIVE' UNION ALL SELECT COUNT(DISTINCT(organization_id)) retCount FROM st_lms_organization_master WHERE organization_type='RETAILER' AND organization_status='TERMINATE' UNION ALL SELECT COUNT(DISTINCT(a.organization_id)) retCount FROM ((SELECT DISTINCT(organization_id) FROM st_lms_organization_master WHERE organization_type='RETAILER' AND organization_status='ACTIVE') a LEFT JOIN (SELECT DISTINCT(retailer_org_id) retailer_org_id FROM st_lms_retailer_transaction_master WHERE transaction_type IN ('VS_SALE','VS_REFUND_CANCEL','VS_PWT') AND transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "' )b ON a.organization_id=b.retailer_org_id) WHERE b.retailer_org_id IS NULL;");
			logger.info("Combined Query for Count - " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retailerCountList.add(rs.getInt("retCount"));
			}

			int liveRet = 0;
			int noSaleRet = 0;
			int inactiveRet = 0;
			int terminateRet = 0;
			if (retailerCountList.size() > 0) {
				liveRet = retailerCountList.get(0);
				inactiveRet = retailerCountList.get(1);
				terminateRet = retailerCountList.get(2);
				noSaleRet = retailerCountList.get(3);
			}

			double avgSalePerRet = 0.0;
			if (totalSale!=0.0 && liveRet!=0) {
				avgSalePerRet = totalSale / liveRet;
			}

			int totalTicketCount = 0;
			pstmt = connection.prepareStatement("SELECT (sale.saleCnt - ref.refCnt) AS netSaleCnt FROM (SELECT IFNULL(COUNT(*), 0) saleCnt FROM st_lms_retailer_transaction_master WHERE (transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') AND (transaction_type ='VS_SALE'))sale, (SELECT IFNULL(COUNT(*), 0) refCnt FROM st_lms_retailer_transaction_master WHERE (transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') AND (transaction_type ='VS_REFUND_CANCEL')) ref;");
			logger.info("Total Ticket Count Query - " + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalTicketCount = rs.getInt("netSaleCnt");
			}

			int totalPWTCount = 0;
			pstmt = connection.prepareStatement("SELECT (ret.retPwt + agt.agtPwt + bo.boPwt) AS pwtCnt FROM ((SELECT IFNULL(COUNT(*), 0) retPwt FROM st_lms_retailer_transaction_master WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "' AND transaction_type='VS_PWT')) ret, (SELECT IFNULL(COUNT(*), 0) agtPwt FROM st_vs_agent_direct_plr_pwt WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') agt, (SELECT IFNULL(COUNT(*), 0) boPwt FROM st_vs_bo_direct_plr_pwt WHERE transaction_date > '" + fromDate + "' AND transaction_date < '" + toDate + "') bo;");
			logger.info("Total PWT Count Query - " + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalPWTCount = rs.getInt("pwtCnt");
			}

			logger.info("Live Retailers - " + liveRet);
			logger.info("Inactive Retailers - " + inactiveRet);
			logger.info("Terminated Retailers - " + terminateRet);
			logger.info("No Sale Retailers - " + noSaleRet);
			logger.info("Total Sale Retailers - " + totalSale);
			logger.info("Total PWT Retailers - " + totalPwt);
			logger.info("Average Sale Per Retailer - " + avgSalePerRet);
			logger.info("Total Ticket Count - " + totalTicketCount);
			logger.info("Total PWT Count" + totalPWTCount);

			String insertValues = "INSERT INTO st_vs_ret_activity_history (DATE, live_retailers, noSale_retailers, inactive_retailers, terminated_retailers, total_sales, total_pwt, total_tkt_count, total_pwt_count, avg_sale_per_ret) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			pstmt = connection.prepareStatement(insertValues);
			pstmt.setString(1, fromDate);
			pstmt.setInt(2, liveRet);
			pstmt.setInt(3, noSaleRet);
			pstmt.setInt(4, inactiveRet);
			pstmt.setInt(5, terminateRet);
			pstmt.setDouble(6, totalSale);
			pstmt.setDouble(7, totalPwt);
			pstmt.setInt(8, totalTicketCount);
			pstmt.setInt(9, totalPWTCount);
			pstmt.setDouble(10, avgSalePerRet);
			System.out.println("Insert in st_vs_ret_activity_history - " + pstmt);
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			int retActId = 0;
			if (rs.next())
				retActId = rs.getInt(1);

			if (retActId > 0)
				logger.info("Data for Day - " + date + " Inserted Successfully.");
			else
				logger.info("Data for Day - " + date + " Not Inserted.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("SQL Exception - " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Exception - " + e.getMessage());
		} finally {
			DBConnect.closeResource(connection, pstmt, rs);
		}
	}

public static void getDailyRetActivityForCS(String date) throws LMSException, SQLException{
	TreeMap<Integer, ArrayList<Double>> gameDataMap = new TreeMap<Integer, ArrayList<Double>>();
	 
	Connection con = DBConnect.getConnection();
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	ArrayList<Double> totAmt = null;
	int catNum = 0;
	List<Integer> categoryList = Util.getCategoryNumberList(con);

	int liveRets = 0;
	int noSaleRets = 0;
	int inactiveRets = 0;
	int terminateRets = 0;
	double totalSale = 0.0;
	double avgSalePerRet = 0.0;

	//String date = new java.sql.Date(new Date().getTime()) + "";

	String fromDate = date + " 00:00:00";
	String toDate = date + " 23:59:59";
	String trxDate = "transaction_date>'" + fromDate
			+ "' and transaction_date<'" + toDate + "'";
	String retQry = "select count(distinct(retailer_org_id)) retCount from st_lms_retailer_transaction_master where transaction_type in ('CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET') and "
			+ trxDate;

	String trxQry = "select transaction_id from st_lms_retailer_transaction_master where "
			+ trxDate;
	String saleQry = "select sum(mrp_amt) res from st_cs_sale_? where transaction_id in ("
			+ trxQry + ")";
	String saleRetQry = "select sum(mrp_amt) res  from st_cs_refund_? where transaction_id in ("
			+ trxQry + ")";
	String combinedQry = saleQry + " union all " + saleRetQry;

	String retSelQry="select count(distinct(organization_id)) from st_lms_organization_master where organization_type='RETAILER'";
	String inactiveQry=retSelQry+" and organization_status='INACTIVE'";
	String terQry=retSelQry+" and organization_status='TERMINATE'";
	String nosaleQry=retSelQry+" and organization_status='ACTIVE' and organization_id not in (select distinct(retailer_org_id) from st_lms_retailer_transaction_master where transaction_type in ('CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET') and "+trxDate+" )";
	String combinedCntQry=retQry+" union all "+inactiveQry+" union all "+terQry+" union all "+nosaleQry;
	
	try {
	for (int i = 0; i < categoryList.size(); i++) {
		catNum = categoryList.get(i);
		totAmt = new ArrayList<Double>();
		pstmt = con.prepareStatement(combinedQry);
		pstmt.setInt(1, catNum);
		pstmt.setInt(2, catNum);
		System.out.println("Q1:"+pstmt);
		rs = pstmt.executeQuery();
		
		
		while (rs.next()) {
			totAmt.add(rs.getDouble("res"));
		}
		gameDataMap.put(catNum, totAmt);
	}

	// total sale & Pwt
	
	Iterator<Map.Entry<Integer, ArrayList<Double>>> itr = gameDataMap.entrySet().iterator();
	while (itr.hasNext()){
		Map.Entry<Integer, ArrayList<Double>> pair = itr.next();
		ArrayList<Double> list = (ArrayList<Double>)pair.getValue();
		totalSale+=(list.get(0)-list.get(1));
	}
	// Retailer count query
	pstmt=con.prepareStatement(combinedCntQry);
	System.out.println("combined query:"+combinedCntQry);
	rs=pstmt.executeQuery();
	List<Integer> retCntList = new ArrayList<Integer>();
	while(rs.next()){
		retCntList.add(rs.getInt("retCount"));
	}
	
	if(retCntList.size()>0){
		liveRets=retCntList.get(0);
		inactiveRets=retCntList.get(1);
		terminateRets=retCntList.get(2);
		noSaleRets=retCntList.get(3);
	}
	// calculating avgSalePerRet 
	if(totalSale != 0.0 && liveRets !=0)
		 avgSalePerRet = totalSale/liveRets;
		
	
	
	
	// done columns
	System.out.println("liveRets:" + liveRets);
	System.out.println("inactiveRets:" + inactiveRets);
	System.out.println("terminateRets:" + terminateRets);
	System.out.println("noSaleRets:" + noSaleRets);
	System.out.println("totalSale:" + totalSale);
	System.out.println("avgSalePerRet:" + avgSalePerRet);
	String insertvalues = "insert into st_cs_ret_activity_history (date,live_retailers,noSale_retailers,inactive_retailers,terminated_retailers,total_sales,avg_sale_per_ret) values (?,?,?,?,?,?,?)";
	pstmt=con.prepareStatement(insertvalues);
	pstmt.setString(1,fromDate);
	pstmt.setInt(2, liveRets);
	pstmt.setInt(3, noSaleRets);
	pstmt.setInt(4, inactiveRets);
	pstmt.setInt(5, terminateRets);
	pstmt.setDouble(6, totalSale);
	pstmt.setDouble(7, avgSalePerRet);
	System.out.println("Q1:"+pstmt);
	pstmt.executeUpdate();
	rs = pstmt.getGeneratedKeys();
	int retActId =0 ;
	if(rs.next()){
		retActId=rs.getInt(1);
	}
	if(retActId>0)
		System.out.println("Data for the day :"+date+" inserted successfully ");
	else
		System.out.println("Data for the day :"+date+"not inserted ");
	}catch (SQLException e) {
		logger.info("SQL Exception ",e);
		throw new LMSException("SQL Exception "+e.getMessage());
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException("Exception"+e.getMessage());
	} finally {
		DBConnect.closeCon(con);
	}
}

}
