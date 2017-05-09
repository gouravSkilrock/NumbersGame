package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;

public class MRPSalesReportLagosHelper {
	Log logger = LogFactory.getLog(MRPSalesReportLagosHelper.class);

	public Map<String, CollectionReportOverAllBean> getGameWiseMRPDetailsLagos(
			int agtOrgId, Timestamp deployDate, Timestamp startDate,
			Timestamp endDate, boolean isDG, boolean isSE, boolean isCS,
			boolean isOLA, boolean isIW) throws LMSException {

		String agtOrgQry = null;

		Connection con = null;
		ResultSet rsRetOrg = null;
		PreparedStatement pstmt = null;

		Map<String, CollectionReportOverAllBean> agtMap = null;
		CollectionReportOverAllBean collBean = null;
		try {
			String orgCodeQry = "  name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "  org_code orgCode  ";


			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			
			if (startDate.after(endDate))
				return null;

			con = DBConnect.getConnection();
			agtMap = new LinkedHashMap<String, CollectionReportOverAllBean>();
			agtOrgQry = "select "+orgCodeQry+",organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by "+QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CollectionReportOverAllBean();
				collBean.setAgentName(rsRetOrg.getString("orgCode"));
				if (isDG) 
					collBean.setDgSale(0.0);
				 agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			collBean = new CollectionReportOverAllBean();
			collBean.setAgentName("Total");
			if (isDG) 
				collBean.setDgSale(0.0);
			agtMap.put("9999999999", collBean);
			getGameWiseMRPDetailsLagos(agtOrgId, startDate, endDate, con, isDG,
					isSE, isCS, isOLA,isIW, agtMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionAgentWiseWithOpeningBal");
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rsRetOrg.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agtMap;
	}

	private static List<String> getAllDates(String strdate, String enddate) {
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
		Date st = null;
		Date ed = null;
		try {
			st = formatterDate.parse(strdate);
			ed = formatterDate.parse(enddate);
		} catch (ParseException e) {
			System.out.println("Parse Exception :" + e);
		}

		Calendar ss = Calendar.getInstance();
		Calendar ee = Calendar.getInstance();

		ss.setTime(st);
		ee.setTime(ed);
		ee.add(Calendar.DATE, 1);
		List<String> days = new ArrayList<String>();
		while (!ss.equals(ee)) {
			days.add(formatterDate.format(ss.getTime()));
			ss.add(Calendar.DATE, 1);
		}

		return days;
	}

	public void getGameWiseMRPDetailsLagos(int agtOrgId , Timestamp startDate,
			Timestamp endDate, Connection con, boolean isDG, boolean isSE,
			boolean isCS, boolean isOLA,
			boolean isIW, Map<String, CollectionReportOverAllBean> agtMap)
			throws LMSException {

		//StringBuilder unionQuery = null;
		//StringBuilder mainQuery = null;
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Statement gameStmt=null;
		CompleteCollectionBean gameBean = null;
		CollectionReportOverAllBean retailerBean = null;
		Statement slotStmt=null;
		Statement stmt=null;
		try {
			if (isDG) {
				// Game Master Query change query by sumit
				//String gameQry = "select game_id,game_name from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where game_status='SALE_CLOSE')";
				String gameQry="SELECT game_id,game_name FROM st_dg_game_master WHERE game_status !='SALE_CLOSE'";
				gameStmt=con.createStatement();
				//PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				//ResultSet rsGame = gamePstmt.executeQuery();
				ResultSet rsGame = gameStmt.executeQuery(gameQry);
				while (rsGame.next()) {
					double totalSale = 0.0;
					double totalSlotSale = 0.0;
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_name");
					String dgSaleQuery = "select sale.organization_id,ifnull(sale,0.0)-ifnull(cancel,0.0) sale from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_"
						+ gameId
						+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id="
						+ agtOrgId
						+ ") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_"
						+ gameId
						+ " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  and om.parent_id="
						+ agtOrgId
						+ ") cancel on sale.organization_id=cancel.organization_id";

					//	By SHOBHIT - Slot Data START
					/*
					Timestamp startSlotDate = startDate;
					StringBuilder queryBuilder = new StringBuilder("");
					String query = "";
					Statement statement = con.createStatement();
					String qry = "SELECT DATE(update_date) updateDate, value FROM st_lms_property_master_history WHERE property_code='gameWiseSaleSlotTime' AND update_date>='"+startSlotDate+"' AND update_date<='"+endDate+"'  GROUP BY DATE(updateDate) UNION SELECT '"+new Timestamp(endDate.getTime()+24*60*60*1000)+"' updateDate, VALUE FROM st_lms_property_master WHERE property_code='gameWiseSaleSlotTime';";
					ResultSet resultSet = statement.executeQuery(qry);
					while(resultSet.next()) {
						String saleSlotTimeString = resultSet.getString("value");
						Timestamp date = resultSet.getTimestamp("updateDate");

						String[] gameWiseTimingArr = saleSlotTimeString.split("~");
						Map<Integer, String> gameSlotTimingMap = new HashMap<Integer, String>();
						for(String gameWiseTiming : gameWiseTimingArr) {
							gameSlotTimingMap.put(Integer.parseInt(gameWiseTiming.split("#")[0]), gameWiseTiming.split("#")[1]);
						}
						String slotTiming = gameSlotTimingMap.get(gameId);
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
						simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
						String startTimeSlot = slotTiming.split("_")[0];
						String endTimeSlot = slotTiming.split("_")[1];
						queryBuilder.append("(DATE(transaction_date)>=DATE('").append(startSlotDate).append("') AND DATE(transaction_date)<DATE('").append(date).append("') AND TIME(transaction_date)>='").append(startTimeSlot).append("' AND TIME(transaction_date)<='").append(endTimeSlot).append("') OR ");
						startSlotDate = date;
					}
					query = queryBuilder.substring(0, queryBuilder.lastIndexOf(" OR "));

					String dgSaleSlotQuery = "SELECT organization_id, IFNULL(mrpAmt,0.00) sale FROM st_lms_organization_master " +
							"oms LEFT JOIN (SELECT retailer_org_id,SUM(mrp_amt) mrpAmt,SUM(net_amt) netAmt FROM st_dg_ret_sale_"+gameId+
							" WHERE transaction_id IN(SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') " +
							"AND "+query+" AND transaction_id NOT IN" +
							"(SELECT ref_transaction_id FROM st_dg_ret_sale_refund_"+gameId+"))GROUP BY retailer_org_id) saleTbl ON saleTbl.retailer_org_id = oms.organization_id " +
							"WHERE oms.parent_id="+agtOrgId+";";
					*/
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					List<String> days = getAllDates(simpleDateFormat.format(startDate.getTime()), simpleDateFormat.format(endDate.getTime()));
					String dgSaleSlotQuery = "SELECT organization_id, IFNULL(mrpAmt,0.00) sale FROM st_lms_organization_master oms LEFT JOIN (SELECT retailer_org_id, SUM(mrpAmt) mrpAmt, SUM(netAmt) netAmt FROM (";
					String saleSlotTimeString = null;
					for(String day : days) {
						saleSlotTimeString = new TrainingExpAgentHelper().getPropertyValue(con, day);
						String[] gameWiseTimingArr = saleSlotTimeString.split("~");
						String timeString = null;
						for(String gameWiseTiming : gameWiseTimingArr) {
							if(gameId == Integer.parseInt(gameWiseTiming.split("#")[0])) {
								timeString = gameWiseTiming.split("#")[1];
								break;
							}
						}
						String startTimeSlot = timeString.split("_")[0];
						String endTimeSlot = timeString.split("_")[1];
						/*
						dgSaleSlotQuery += "SELECT retailer_org_id,mrp_amt mrpAmt, net_amt netAmt FROM st_dg_ret_sale_"+gameId+" "+
								"WHERE transaction_id IN(SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') " +
								"AND DATE(transaction_date)='"+day+"' AND TIME(transaction_date)>='"+startTimeSlot+"' AND TIME(transaction_date)<='"+endTimeSlot+"'" +
								"AND transaction_id NOT IN (select ref_transaction_id from st_dg_ret_sale_refund_"+gameId+" refund inner join  st_lms_retailer_transaction_master rtm on refund.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and date(transaction_date)=date('"+day+"'))) UNION ALL ";
						*/
						
						/*dgSaleSlotQuery += "SELECT rs.retailer_org_id, mrp_amt mrpAmt, net_amt netAmt FROM st_dg_ret_sale_"+gameId+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') " +
								"AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" "+endTimeSlot+"' AND rtm.transaction_id NOT IN (" +
								"SELECT ref_transaction_id FROM st_dg_ret_sale_refund_"+gameId+" refund INNER JOIN st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') " +
								"AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" 23:59:59') UNION ALL ";
					*/
						dgSaleSlotQuery += "SELECT rs.retailer_org_id, mrp_amt mrpAmt, net_amt netAmt FROM st_dg_ret_sale_"+gameId+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id " +
								" left join (SELECT ref_transaction_id FROM st_dg_ret_sale_refund_"+gameId+" refund INNER JOIN st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') " +
										" AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" 23:59:59')ref on ref.ref_transaction_id=rtm.transaction_id  WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" "+endTimeSlot+"' and ref.ref_transaction_id is null  UNION ALL ";
								
								
								/*"SELECT rs.retailer_org_id, mrp_amt mrpAmt, net_amt netAmt FROM st_dg_ret_sale_"+gameId+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') " +
								"AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" "+endTimeSlot+"' AND rtm.transaction_id NOT IN (" +
								"SELECT ref_transaction_id FROM st_dg_ret_sale_refund_"+gameId+" refund INNER JOIN st_lms_retailer_transaction_master rtm ON refund.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_REFUND_CANCEL','DG_REFUND_FAILED') " +
								"AND transaction_date>='"+day+" "+startTimeSlot+"' AND transaction_date<='"+day+" 23:59:59') UNION ALL ";
					*/
					
					
					
					}
					dgSaleSlotQuery = dgSaleSlotQuery.substring(0, dgSaleSlotQuery.lastIndexOf(" UNION ALL "));
					dgSaleSlotQuery += ")aa GROUP BY retailer_org_id)saleTbl ON saleTbl.retailer_org_id = oms.organization_id WHERE oms.parent_id="+agtOrgId+";";
					//	By SHOBHIT - Slot Data END

			/*		if (LMSFilterDispatcher.isRepFrmSP) {
					mainQuery = new StringBuilder(
							"select main.organization_id,sum(main.sale)as sale from (");
					unionQuery = new StringBuilder(
							" union all select dgr.organization_id as organization_id,(sum(ifnull(dgr.sale_mrp,0.0))-sum(ifnull(dgr.ref_sale_mrp,0.0)))  as sale from  st_rep_dg_retailer as dgr  where  dgr.game_id="
									+ gameId
									+ " and dgr.parent_id="
									+ agtOrgId
									+ " and dgr.finaldate>=date('"
									+ startDate
									+ "') and dgr.finaldate<=date('"
									+ endDate
									+ "') group by dgr.organization_id)as main group by main.organization_id ");
					mainQuery.append(dgSaleQuery).append(
							unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				} else {*/
					logger.info("***Draw Game Salse Query***"+dgSaleQuery);
				Statement saleStmt = con.createStatement();
				//}
				

				// Draw Sale Query
				rs = saleStmt.executeQuery(dgSaleQuery);
					while (rs.next()) {
						String retOrgId = rs.getString("organization_id");
						retailerBean = agtMap.get(retOrgId);
						if (retailerBean != null) {
							Map<String, CompleteCollectionBean> gameMap = retailerBean
									.getGameBeanMap();
							if (gameMap == null) {
								gameMap = new HashMap<String, CompleteCollectionBean>();
								retailerBean.setGameBeanMap(gameMap);
							}
							gameBean = gameMap.get(gameName);
							if (gameBean == null) {
								gameBean = new CompleteCollectionBean();
								gameMap.put(gameName, gameBean);
							}
							totalSale += rs.getDouble("sale");
							gameBean.setOrgName(gameName);
							gameBean.setDrawSale(rs.getDouble("sale"));
						}
					}
					slotStmt=con.createStatement();
					//pstmt = con.prepareStatement(dgSaleSlotQuery);
					//rs = pstmt.executeQuery();
					rs=slotStmt.executeQuery(dgSaleSlotQuery);
					while (rs.next()) {
						String retOrgId = rs.getString("organization_id");
						retailerBean = agtMap.get(retOrgId);
						if (retailerBean != null) {
							Map<String, CompleteCollectionBean> gameMap = retailerBean
									.getGameBeanMap();
							gameBean = gameMap.get(gameName);
							totalSlotSale += rs.getDouble("sale");
							gameBean.setDrawSlotSale(rs.getDouble("sale"));
						}
					}

					retailerBean = agtMap.get("9999999999");
					Map<String, CompleteCollectionBean> gameMap = retailerBean
							.getGameBeanMap();
					if (gameMap == null) {
						gameMap = new HashMap<String, CompleteCollectionBean>();
						retailerBean.setGameBeanMap(gameMap);
					}
					gameBean = gameMap.get(gameName);
					if (gameBean == null) {
						gameBean = new CompleteCollectionBean();
						gameMap.put(gameName, gameBean);
					}
					gameBean.setOrgName(gameName);
					gameBean.setDrawSale(totalSale);
					gameBean.setDrawSlotSale(totalSlotSale);
				}
			}
			if (isIW) {
				stmt = con.createStatement();
				String gameQry = "SELECT game_id,game_disp_name FROM st_iw_game_master WHERE game_status !='SALE_CLOSE'";
				gameStmt = con.createStatement();
				// PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				// ResultSet rsGame = gamePstmt.executeQuery();
				ResultSet rsGame = gameStmt.executeQuery(gameQry);
				while (rsGame.next()) {
					double totalSale = 0.0;
					int gameId = rsGame.getInt("game_id");
					String gameName = rsGame.getString("game_disp_name");
					String iwSaleQuery = "select retailer_org_id,sum(mrp_amt) amt from st_iw_ret_sale rs inner join st_lms_organization_master om on om.organization_id=rs.retailer_org_id where is_cancel='N'  and transaction_date>='"+startDate+"' and transaction_date<= '"+endDate+"' and parent_id="+agtOrgId+" and game_id="+gameId+" group by retailer_org_id;";

					rs = stmt.executeQuery(iwSaleQuery);
					while (rs.next()) {
						String retOrgId = rs.getString("retailer_org_id");
						retailerBean = agtMap.get(retOrgId);
						if (retailerBean != null) {
							Map<String, CompleteCollectionBean> gameMap = retailerBean
									.getGameBeanMap();
							if (gameMap == null) {
								gameMap = new HashMap<String, CompleteCollectionBean>();
								retailerBean.setGameBeanMap(gameMap);
							}
							gameBean = gameMap.get(gameName);
							if (gameBean == null) {
								gameBean = new CompleteCollectionBean();
								gameMap.put(gameName, gameBean);
							}
							totalSale += rs.getDouble("amt");
							gameBean.setOrgName(gameName);
							gameBean.setIwSale(rs.getDouble("amt"));
						}
					}
					
					retailerBean = agtMap.get("9999999999");
					Map<String, CompleteCollectionBean> gameMap = retailerBean
							.getGameBeanMap();
					if (gameMap == null) {
						gameMap = new HashMap<String, CompleteCollectionBean>();
						retailerBean.setGameBeanMap(gameMap);
					}
					gameBean = gameMap.get(gameName);
					if (gameBean == null) {
						gameBean = new CompleteCollectionBean();
						gameMap.put(gameName, gameBean);
					}
					gameBean.setOrgName(gameName);
					gameBean.setIwSale(totalSale);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise Expand");
		} finally {
			
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			System.out.println(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
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
		return orgAdd;
	}

	public Map<String, String> allGameMap(Timestamp startDate) throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select 'DG' game_type,game_name from st_dg_game_master where game_nbr not in(select game_nbr from st_dg_game_master where game_status='SALE_CLOSE') union all select  'IW' game_type,game_disp_name from st_iw_game_master where game_status!='SALE_CLOSE'";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("game_name"), rs
						.getString("game_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Game List");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}
}
