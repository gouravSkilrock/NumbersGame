package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.GovCommBean;
import com.skilrock.lms.beans.TaskBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.web.drawGames.common.Util;

public class CalculateGovCommAmtHelper {

	static Log logger = LogFactory.getLog(CalculateGovCommAmtHelper.class);

	private static final DateFormat sqlDateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final DateFormat utilDateFormatter = new SimpleDateFormat(
			"dd-MM-yyyy");

	public static void main(String[] args) {
		// logger.debug("" && "" );
		// new CalculateGovCommAmtHelper().getLastAproveDateOfGovCommAmt();
	}

	private Connection con;

	private PreparedStatement pstmt;

	double totalGovCommAmt;

	private Date add(Date date, int value) {

		Date newDate = null;
		try {
			java.util.Date utilDate = utilDateFormatter.parse(utilDateFormatter
					.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			newDate = new Date(cal.getTime().getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.debug("Date after addition Date : " + newDate);
		return newDate;
	}

	public void approveGovCommDetails(long[] taskIds, String status) {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			Timestamp updateTime = Util.getCurrentTimeStamp();
			pstmt = connection.prepareStatement("UPDATE st_lms_bo_tasks SET status=?, approve_date=? WHERE task_id=?;");
			for (long taskId : taskIds) {
				pstmt.setString(1, status);
				pstmt.setTimestamp(2, updateTime);
				pstmt.setLong(3, taskId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			connection.commit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}  finally {
			DBConnect.closeCon(connection);
			DBConnect.closePstmt(pstmt);
		}
	}

	// It return date till government commission submitted
	public Map<Integer, GovCommBean> getGovCommAmtDetailToApprove(String endDate) {
		GovCommBean gameBean = null;
		this.totalGovCommAmt = 0;

		Map<Integer, GovCommBean> map = new TreeMap<Integer, GovCommBean>();
		this.con = DBConnect.getConnection();
		try {

			pstmt = con
					.prepareStatement("select game_id, game_nbr, game_name, start_date from st_se_game_master where game_status!='NEW'");
			ResultSet result = pstmt.executeQuery();
			logger.debug("query to get game details from game master = "
					+ pstmt);
			while (result.next()) {
				gameBean = new GovCommBean();
				Date startDate = result.getDate("start_date");
				int gameId = result.getInt("game_id");
				String gameName = result.getString("game_name");
				int gameNbr = result.getInt("game_nbr");
				// logger.debug(gameId+gameName+" start date = "+startDate);

				// check whether this game entries in st_lms_bo_tasks
				pstmt = con
						.prepareStatement("select end_date from st_lms_bo_tasks where transaction_type='GOVT_COMM' and (status='APPROVED' or status='DONE') and service_code = 'SE' and game_id =? order by end_date desc limit 1");
				pstmt.setInt(1, gameId);
				ResultSet rs = pstmt.executeQuery();
				// logger.debug("query to check whether game gov tax is aprove
				// before that = "+pstmt);

				if (rs.next()) {
					startDate = add(new java.sql.Date(sqlDateFormatter.parse(
							rs.getDate("end_date").toString()).getTime()), 1);
					// logger.debug(gameId+gameName+" now changed start date =
					// "+startDate);
				}
				rs.close();

				// set game details into bean
				gameBean.setGameId(gameId);
				gameBean.setGameNbr(gameNbr);
				gameBean.setGameName(gameName);
				gameBean.setStartDate(startDate.toString());
				gameBean.setEndDate(endDate);

				// getting the details of SALE Amount and SALE_RET Amount
				Date edDate = add(new java.sql.Date(sqlDateFormatter.parse(
						endDate).getTime()), 1);
				Date stDate = startDate;
				// logger.debug("st date is "+stDate);
				
				double govtCommRate = 100/Double.parseDouble((String)LMSUtility.sc.getAttribute("GOVT_COMM_RATE"));
				pstmt = con
						.prepareStatement("select aa.sale, bb.sale_ret, (aa.sale - bb.sale_ret - cc.cr_note + dd.dr_note) 'gov_comm_amt' from (( select ifnull(sum(gov_comm_amt), 0) 'sale' from  st_se_bo_agent_transaction bat, st_lms_bo_transaction_master btm where bat.transaction_type='SALE' and bat.game_id=? and bat.transaction_id = btm.transaction_id   and (btm.transaction_date>=? and btm.transaction_date<?) ) aa, ( select ifnull(sum(gov_comm_amt),0) 'sale_ret' from  st_se_bo_agent_transaction bat, st_lms_bo_transaction_master btm where bat.transaction_type='SALE_RET' and bat.game_id=? and bat.transaction_id = btm.transaction_id   and (btm.transaction_date>=? and btm.transaction_date<?)  ) bb, (select ifnull(sum(amount)/?,0) 'cr_note' from st_lms_bo_credit_note bcn, st_lms_bo_transaction_master btm where btm.transaction_type=? AND bcn.reason=? and btm.transaction_id=bcn.transaction_id and (btm.transaction_date>=? and btm.transaction_date<?) and bcn.ref_id=?) cc, (select ifnull(sum(amount)/?,0) 'dr_note' from st_lms_bo_debit_note bdn, st_lms_bo_transaction_master btm where btm.transaction_type=? AND bdn.reason=? and btm.transaction_id=bdn.transaction_id and (btm.transaction_date>=? and btm.transaction_date<?) and bdn.ref_id=?) dd)");
				pstmt.setInt(1, gameId);
				pstmt.setDate(2, stDate);
				pstmt.setDate(3, edDate);
				pstmt.setInt(4, gameId);
				pstmt.setDate(5, stDate);
				pstmt.setDate(6, edDate);
				
				pstmt.setDouble(7, govtCommRate); 
				pstmt.setString(8, "CR_NOTE_CASH");
				pstmt.setString(9, "AGAINST_LOOSE_BOOKS_RETURN");
				pstmt.setDate(10, stDate);
				pstmt.setDate(11, edDate);
				pstmt.setInt(12, gameId);
				
				pstmt.setDouble(13, govtCommRate); 
				pstmt.setString(14, "DR_NOTE_CASH");
				pstmt.setString(15, "AGAINST_LOOSE_BOOKS_RETURN");
				pstmt.setDate(16, stDate);
				pstmt.setDate(17, edDate);
				pstmt.setInt(18, gameId);
				
				rs = pstmt.executeQuery();
				logger.debug("getUnApprovedGovComm ==== query === " + pstmt);

				double sale = 0;
				double saleRet = 0;
				double govComm = 0;

				if (rs.next()) {
					sale = rs.getDouble("sale");
					saleRet = rs.getDouble("sale_ret");
					govComm = rs.getDouble("gov_comm_amt");
				}
				rs.close();

				gameBean.setSaleAmount(sale);
				gameBean.setSaleRetAmount(saleRet);
				gameBean.setGovAmount(FormatNumber.formatNumber(govComm));

				this.totalGovCommAmt = this.totalGovCommAmt + govComm;
				if (Double.parseDouble(gameBean.getGovAmount()) != 0) {
					map.put(gameId, gameBean);
				}

			}

			result.close();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}

		}

		return map;
	}

	/*public Map<Integer, GovCommBean> getGovCommAmtDetailToApproveForDG(Timestamp startDate, Timestamp endDate, String serviceCode, String type) {
		Map<Integer, GovCommBean> map = new TreeMap<Integer, GovCommBean>();
		Map<Integer, Timestamp> startDateMap = new TreeMap<Integer, Timestamp>();
		con = DBConnect.getConnection();
		GovCommBean GovBean = null;
		ResultSet mainRs = null;
		ResultSet mainRsRef = null;
		String queryMain = "select total, game_name, game_nbr as gameNbr,top.game_id gameId from (select sum(govt_comm)as total, game_id from st_dg_bo_sale where transaction_id in (select transaction_id from st_lms_bo_transaction_master where (transaction_date >=? and transaction_date<?) and transaction_type='DG_SALE') and game_id = ? group by game_id )as myn, (select game_nbr, game_id, game_name from st_dg_game_master)as top where myn.game_id = top.game_id";
		String queryMainRef = "select total, game_name, game_nbr as gameNbr,top.game_id gameId from (select sum(govt_comm)as total, game_id from st_dg_bo_sale_refund where transaction_id in (select transaction_id from st_lms_bo_transaction_master where (transaction_date >=? and transaction_date<?) and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')) and game_id = ? group by game_id )as myn, (select game_nbr, game_id, game_name from st_dg_game_master)as top where myn.game_id = top.game_id";
		try {

			pstmt = con
					.prepareStatement("select game_id from st_dg_game_master");
			logger.debug("******Query for game master*******" + pstmt);
			ResultSet st = pstmt.executeQuery();
			Timestamp localStartDate = null;
			while (st.next()) {
				pstmt = con
						.prepareStatement("select max(end_date) as start_date from st_lms_bo_tasks where "+("ALL".equalsIgnoreCase(type) ? "" : ("SALE".equalsIgnoreCase(type) ? "transaction_type='GOVT_COMM' and " : "transaction_type='GOVT_COMM_PWT' and " ))+" (status='APPROVED' or status='DONE') and service_code = ? and game_id = ?");
				pstmt.setString(1, serviceCode);
				pstmt.setInt(2, st.getInt("game_id"));
				logger.debug("******Query for start date game_wise******"
						+ pstmt);
				ResultSet resultSet = pstmt.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getTimestamp("start_date") != null) {
						localStartDate = new Timestamp(add(
								new Date(resultSet.getTimestamp("start_date")
										.getTime()), 1).getTime());
					} else {
						localStartDate = startDate;
					}
				}
				startDateMap.put(st.getInt("game_id"), localStartDate);
			}
			Iterator<Map.Entry<Integer, Timestamp>> itr = startDateMap
					.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, Timestamp> pair = itr.next();
				pstmt = con.prepareStatement(queryMain);
				logger.debug("******startDate for gameid" + pair.getKey()
						+ "is" + pair.getValue());
				pstmt.setTimestamp(1, pair.getValue());
				pstmt.setTimestamp(2, endDate);
				pstmt.setInt(3, pair.getKey());
				logger.debug("*******QueryMain****" + pstmt);
				mainRs = pstmt.executeQuery();

				pstmt = con.prepareStatement(queryMainRef);
				logger.debug("******startDate for gameid" + pair.getKey()
						+ "is" + pair.getValue());
				pstmt.setTimestamp(1, pair.getValue());
				pstmt.setTimestamp(2, endDate);
				pstmt.setInt(3, pair.getKey());
				logger.debug("*******QueryMainRef****" + pstmt);
				mainRsRef = pstmt.executeQuery();

				while (mainRs.next()) {
					GovBean = new GovCommBean();
					if (mainRsRef.next()) {
						GovBean.setGovAmount(mainRs.getDouble("total")
								- mainRsRef.getDouble("total") + "");
						totalGovCommAmt += mainRs.getDouble("total")
								- mainRsRef.getDouble("total");
					} else {
						GovBean.setGovAmount(mainRs.getString("total"));
						totalGovCommAmt += mainRs.getDouble("total");
					}
					GovBean.setGameNbr(mainRs.getInt("gameNbr"));
					GovBean.setGameId(mainRs.getInt("gameId"));
					GovBean.setGameName(mainRs.getString("game_name"));
					GovBean.setStartDate(new Date(pair.getValue().getTime())
							.toString());
					GovBean.setEndDate(new Date(endDate.getTime() - 24 * 60
							* 60 * 1000).toString());
					map.put(mainRs.getInt("gameId"), GovBean);
				}
			}
			// insert the entry for this approval in st_lms_bo_tasks
		DBConnect.closeCon(con);
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}
		return map;
	}*/

	public Map<Integer, GovCommBean> getGovCommAmtDetailToApproveForDG(Timestamp startDate, Timestamp endDate, String serviceCode, String type, Connection connection) {
		ResultSet rs = null;
		ResultSet dateRs = null;
		Map<Integer, Timestamp> startDateMap = new TreeMap<Integer, Timestamp>();
		Map<Integer, GovCommBean> commissionMap = new TreeMap<Integer, GovCommBean>();
		GovCommBean commissionBean = null;
		try {
			pstmt = connection.prepareStatement("SELECT game_id FROM st_dg_game_master;");
			rs = pstmt.executeQuery();
			Timestamp localStartDate = null;
			while (rs.next()) {
				pstmt = connection.prepareStatement("SELECT MAX(end_date) AS start_date FROM st_lms_bo_tasks WHERE "+("ALL".equalsIgnoreCase(type) ? "" : ("SALE".equalsIgnoreCase(type) ? "transaction_type='GOVT_COMM' AND " : "transaction_type='GOVT_COMM_PWT' AND " ))+" service_code=? AND game_id=?;");
				pstmt.setString(1, serviceCode);
				pstmt.setInt(2, rs.getInt("game_id"));
				dateRs = pstmt.executeQuery();
				if (dateRs.next()) {
					if (dateRs.getTimestamp("start_date") != null) {
						localStartDate = new Timestamp(add(new Date(dateRs.getTimestamp("start_date").getTime()), 1).getTime());
					} else {
						localStartDate = startDate;
					}
				}
				startDateMap.put(rs.getInt("game_id"), localStartDate);
			}

			Iterator<Map.Entry<Integer, Timestamp>> itr = startDateMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, Timestamp> pair = itr.next();

				if("ALL".equals(type) || "SALE".equals(type)) {
					//	For Sale
					pstmt = connection.prepareStatement("SELECT total, game_name, game_nbr AS gameNbr, top.game_id gameId FROM (SELECT SUM(good_cause_amt) AS total, rs.game_id FROM st_dg_ret_sale_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_date>=? AND transaction_date<? AND transaction_type='DG_SALE' AND rs.game_id=? GROUP BY rs.game_id)AS main, (SELECT game_nbr, game_id, game_name FROM st_dg_game_master)AS top WHERE main.game_id=top.game_id;");
					pstmt.setInt(1, pair.getKey());
					pstmt.setTimestamp(2, pair.getValue());
					pstmt.setTimestamp(3, endDate);
					pstmt.setInt(4, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = new GovCommBean();
						commissionBean.setGovAmount(rs.getString("total"));
						commissionBean.setGameNbr(rs.getInt("gameNbr"));
						commissionBean.setGameId(rs.getInt("gameId"));
						commissionBean.setGameName(rs.getString("game_name"));
						commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
						commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
						commissionMap.put(rs.getInt("gameId"), commissionBean);
					}

					//	For Sale Refund
					pstmt = connection.prepareStatement("SELECT total, game_name, game_nbr AS gameNbr, top.game_id gameId FROM (SELECT SUM(good_cause_amt) AS total, rs.game_id FROM st_dg_ret_sale_refund_? rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE transaction_date>=? AND transaction_date<? AND transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND rs.game_id=? GROUP BY rs.game_id)AS main, (SELECT game_nbr, game_id, game_name FROM st_dg_game_master)AS top WHERE main.game_id=top.game_id;");
					pstmt.setInt(1, pair.getKey());
					pstmt.setTimestamp(2, pair.getValue());
					pstmt.setTimestamp(3, endDate);
					pstmt.setInt(4, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = commissionMap.get(rs.getInt("gameId"));
						if(commissionBean == null) {
							commissionBean = new GovCommBean();
							commissionBean.setGovAmount(String.valueOf(-rs.getDouble("total")));
							commissionBean.setGameNbr(rs.getInt("gameNbr"));
							commissionBean.setGameId(rs.getInt("gameId"));
							commissionBean.setGameName(rs.getString("game_name"));
							commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
							commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
							commissionMap.put(rs.getInt("gameId"), commissionBean);
						} else {
							commissionBean.setGovAmount(String.valueOf(Double.parseDouble(commissionBean.getGovAmount()) - rs.getDouble("total")));
						}
					}
				}

				if("ALL".equals(type) || "PWT".equals(type)) {
					//	For PWT
					pstmt = connection.prepareStatement("SELECT total, game_name, game_nbr AS gameNbr, top.game_id gameId FROM (SELECT SUM(total) AS total, game_id FROM (SELECT SUM(govt_claim_comm) AS total, rp.game_id FROM st_dg_ret_pwt_? rp INNER JOIN st_lms_retailer_transaction_master rtm ON rp.transaction_id=rtm.transaction_id WHERE transaction_date>=? AND transaction_date<? AND transaction_type IN ('DG_PWT_AUTO','PWT_PLR','PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT') AND rp.game_id=? GROUP BY rp.game_id UNION ALL SELECT SUM(tax_amt) AS total, game_id FROM st_dg_bo_direct_plr_pwt bs INNER JOIN st_lms_bo_transaction_master btm ON bs.transaction_id=btm.transaction_id WHERE btm.transaction_date>=? AND btm.transaction_date<? AND transaction_type IN ('DG_PWT_AUTO','PWT_PLR','PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT') AND game_id=? GROUP BY game_id UNION ALL SELECT SUM(tax_amt) AS total, game_id FROM st_dg_agt_direct_plr_pwt adp INNER JOIN st_lms_agent_transaction_master atm ON adp.transaction_id=atm.transaction_id WHERE atm.transaction_date>=? AND atm.transaction_date<? AND transaction_type IN ('DG_PWT_AUTO','PWT_PLR','PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT') AND game_id=? GROUP BY game_id)aa)AS main, (SELECT game_nbr, game_id, game_name FROM st_dg_game_master)AS top WHERE main.game_id=top.game_id;");
					pstmt.setInt(1, pair.getKey());
					pstmt.setTimestamp(2, pair.getValue());
					pstmt.setTimestamp(3, endDate);
					pstmt.setInt(4, pair.getKey());
					pstmt.setTimestamp(5, pair.getValue());
					pstmt.setTimestamp(6, endDate);
					pstmt.setInt(7, pair.getKey());
					pstmt.setTimestamp(8, pair.getValue());
					pstmt.setTimestamp(9, endDate);
					pstmt.setInt(10, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = commissionMap.get(rs.getInt("gameId"));
						if(commissionBean == null) {
							commissionBean = new GovCommBean();
							commissionBean.setGovAmount("0.00");
							commissionBean.setGameNbr(rs.getInt("gameNbr"));
							commissionBean.setGameId(rs.getInt("gameId"));
							commissionBean.setGameName(rs.getString("game_name"));
							commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
							commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
							commissionMap.put(rs.getInt("gameId"), commissionBean);
						}
						commissionBean.setGovPwtAmount(rs.getDouble("total"));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closeRs(rs);
			DBConnect.closeRs(dateRs);
		}

		return commissionMap;
	}

	public Map<Integer, GovCommBean> getGovCommAmtDetailToApproveForSLE(Timestamp startDate, Timestamp endDate, String serviceCode, String type, Connection connection) {
		ResultSet rs = null;
		ResultSet dateRs = null;
		Map<Integer, Timestamp> startDateMap = new TreeMap<Integer, Timestamp>();
		Map<Integer, GovCommBean> commissionMap = new TreeMap<Integer, GovCommBean>();
		GovCommBean commissionBean = null;
		try {
			pstmt = connection.prepareStatement("SELECT game_id, game_type_id FROM st_sle_game_type_master;");
			rs = pstmt.executeQuery();
			Timestamp localStartDate = null;
			while (rs.next()) {
				pstmt = connection.prepareStatement("SELECT MAX(end_date) AS start_date FROM st_lms_bo_tasks WHERE "+("ALL".equalsIgnoreCase(type) ? "" : ("SALE".equalsIgnoreCase(type) ? "transaction_type='GOVT_COMM' AND " : "transaction_type='GOVT_COMM_PWT' AND " ))+"  service_code=? AND game_id=?;");
				pstmt.setString(1, serviceCode);
				pstmt.setInt(2, rs.getInt("game_type_id"));
				dateRs = pstmt.executeQuery();
				if (dateRs.next()) {
					if (dateRs.getTimestamp("start_date") != null) {
						localStartDate = new Timestamp(add(new Date(dateRs.getTimestamp("start_date").getTime()), 1).getTime());
					} else {
						localStartDate = startDate;
					}
				}
				startDateMap.put(rs.getInt("game_type_id"), localStartDate);
			}

			Iterator<Map.Entry<Integer, Timestamp>> itr = startDateMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, Timestamp> pair = itr.next();

				if("ALL".equals(type) || "SALE".equals(type)) {
					//	For Sale
					pstmt = connection.prepareStatement("SELECT total, type_disp_name, top.game_id AS gameNbr, top.game_type_id gameId FROM (SELECT SUM(good_cause_amt) AS total, game_type_id FROM st_sle_ret_sale rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE rtm.transaction_date>=? AND rtm.transaction_date<? AND transaction_type='SLE_SALE' AND game_type_id=? GROUP BY game_type_id)AS main, (SELECT game_id, game_type_id, type_disp_name FROM st_sle_game_type_master)AS top WHERE main.game_type_id=top.game_type_id;");
					pstmt.setTimestamp(1, pair.getValue());
					pstmt.setTimestamp(2, endDate);
					pstmt.setInt(3, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = new GovCommBean();
						commissionBean.setGovAmount(rs.getString("total"));
						commissionBean.setGameNbr(rs.getInt("gameNbr"));
						commissionBean.setGameId(rs.getInt("gameId"));
						commissionBean.setGameName(rs.getString("type_disp_name"));
						commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
						commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
						commissionMap.put(rs.getInt("gameId"), commissionBean);
					}

					//	For Sale Refund
					pstmt = connection.prepareStatement("SELECT total, type_disp_name, top.game_id AS gameNbr, top.game_type_id gameId FROM (SELECT SUM(good_cause_amt) AS total, game_type_id FROM st_sle_ret_sale_refund rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE rtm.transaction_date>=? AND rtm.transaction_date<? AND transaction_type IN ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') AND game_type_id=? GROUP BY game_type_id)AS main, (SELECT game_id, game_type_id, type_disp_name FROM st_sle_game_type_master)AS top WHERE main.game_type_id=top.game_type_id;");
					pstmt.setTimestamp(1, pair.getValue());
					pstmt.setTimestamp(2, endDate);
					pstmt.setInt(3, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = commissionMap.get(rs.getInt("gameId"));
						if(commissionBean == null) {
							commissionBean = new GovCommBean();
							commissionBean.setGovAmount(String.valueOf(-rs.getDouble("total")));
							commissionBean.setGameNbr(rs.getInt("gameNbr"));
							commissionBean.setGameId(rs.getInt("gameId"));
							commissionBean.setGameName(rs.getString("type_disp_name"));
							commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
							commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
							commissionMap.put(rs.getInt("gameId"), commissionBean);
						} else {
							commissionBean.setGovAmount(String.valueOf(Double.parseDouble(commissionBean.getGovAmount()) - rs.getDouble("total")));
						}
					}
				}

				if("ALL".equals(type) || "PWT".equals(type)) {
					//	For PWT
					pstmt = connection.prepareStatement("SELECT SUM(total) total, type_disp_name, top.game_id AS gameNbr, top.game_type_id gameId FROM (SELECT SUM(govt_claim_comm) AS total, game_type_id FROM st_sle_ret_pwt rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id WHERE rtm.transaction_date>=? AND rtm.transaction_date<? AND transaction_type IN ('SLE_PWT') AND game_type_id=? GROUP BY game_type_id UNION ALL SELECT SUM(tax_amt) AS total, game_type_id FROM st_sle_bo_direct_plr_pwt bs INNER JOIN st_lms_bo_transaction_master btm ON bs.transaction_id=btm.transaction_id WHERE btm.transaction_date>=? AND btm.transaction_date<? AND transaction_type IN ('SLE_PWT_AUTO','SLE_PWT_PLR') AND game_type_id=? GROUP BY game_type_id UNION ALL SELECT SUM(tax_amt) AS total, game_type_id FROM st_sle_agent_direct_plr_pwt adp INNER JOIN st_lms_agent_transaction_master atm ON adp.transaction_id=atm.transaction_id WHERE atm.transaction_date>=? AND atm.transaction_date<? AND transaction_type IN ('SLE_PWT') AND game_type_id=? GROUP BY game_type_id)AS main, (SELECT game_id, game_type_id, type_disp_name FROM st_sle_game_type_master)AS top WHERE main.game_type_id=top.game_type_id;");
					pstmt.setTimestamp(1, pair.getValue());
					pstmt.setTimestamp(2, endDate);
					pstmt.setInt(3, pair.getKey());
					pstmt.setTimestamp(4, pair.getValue());
					pstmt.setTimestamp(5, endDate);
					pstmt.setInt(6, pair.getKey());
					pstmt.setTimestamp(7, pair.getValue());
					pstmt.setTimestamp(8, endDate);
					pstmt.setInt(9, pair.getKey());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						commissionBean = commissionMap.get(rs.getInt("gameId"));
						if(commissionBean == null) {
							commissionBean = new GovCommBean();
							commissionBean.setGovAmount("0.00");
							commissionBean.setGameNbr(rs.getInt("gameNbr"));
							commissionBean.setGameId(rs.getInt("gameId"));
							commissionBean.setGameName(rs.getString("type_disp_name"));
							commissionBean.setStartDate(new Date(pair.getValue().getTime()).toString());
							commissionBean.setEndDate(new Date(endDate.getTime() - 24 * 60 * 60 * 1000).toString());
							commissionMap.put(rs.getInt("gameId"), commissionBean);
						}
						commissionBean.setGovPwtAmount(rs.getDouble("total"));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closeRs(rs);
			DBConnect.closeRs(dateRs);
		}

		return commissionMap;
	}

	public Map<Integer, GovCommBean> getCommissionDetailsToApprove(Timestamp startDate, Timestamp endDate, String serviceCode, String type) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		Map<Integer, GovCommBean> commissionMap = new TreeMap<Integer, GovCommBean>();
		GovCommBean commissionBean = null;
		try {
			String query = null;
			String transactionType = "SALE".equals(type) ? "GOVT_COMM" : "GOVT_COMM_PWT";
			if("DG".equals(serviceCode))
				query = "SELECT task_id, bt.game_id, game_name, start_date, end_date, amount FROM st_lms_bo_tasks bt INNER JOIN st_dg_game_master gm ON bt.game_id=gm.game_id WHERE STATUS='UNAPPROVED' AND end_date>='"+startDate+"' AND end_date<='"+endDate+"' AND service_code='DG' AND transaction_type='"+transactionType+"';";
			else if("SLE".equals(serviceCode))
				query = "SELECT task_id, bt.game_id, type_disp_name game_name, start_date, end_date, amount FROM st_lms_bo_tasks bt INNER JOIN st_sle_game_type_master gm ON bt.game_id=gm.game_type_id WHERE STATUS='UNAPPROVED' AND end_date>='"+startDate+"' AND end_date<='"+endDate+"' AND service_code='SLE' AND transaction_type='"+transactionType+"';";
			logger.info("getCommissionDetailsToApprove - "+query);

			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				commissionBean = new GovCommBean();
				commissionBean.setTaskId(rs.getLong("task_id"));
				commissionBean.setGameId(rs.getInt("game_id"));
				commissionBean.setGameName(rs.getString("game_name"));
				commissionBean.setStartDate(dateFormat.format(rs.getTimestamp("start_date")));
				commissionBean.setEndDate(dateFormat.format(rs.getTimestamp("end_date")));
				commissionBean.setGovAmount(rs.getString("amount"));
				commissionMap.put(rs.getInt("task_id"), commissionBean);

				totalGovCommAmt += rs.getDouble("amount");
			}
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return commissionMap;
	}
	
	public Map<Integer, GovCommBean> getGovCommAmtDetailToApproveForIW(Timestamp startDate, Timestamp endDate, String serviceCode) {
		Map<Integer, GovCommBean> map = new TreeMap<Integer, GovCommBean>();
		Map<Integer, Timestamp> startDateMap = new TreeMap<Integer, Timestamp>();
		con = DBConnect.getConnection();
		GovCommBean GovBean = null;
		ResultSet mainRs = null;
		//ResultSet mainRsRef = null;
		String queryMain = "select total, game_disp_name as game_name, game_no as gameNbr,top.game_id gameId from (select sum(govt_comm)as total, game_id from st_iw_bo_sale where transaction_id in (select transaction_id from st_lms_bo_transaction_master where (transaction_date >=? and transaction_date<?) and transaction_type='DG_SALE') and game_id = ? group by game_id )as myn, (select game_no, game_id, game_disp_name from st_iw_game_master)as top where myn.game_id = top.game_id";
		//String queryMainRef = "select total, game_name, game_nbr as gameNbr,top.game_id gameId from (select sum(govt_comm)as total, game_id from st_dg_bo_sale_refund where transaction_id in (select transaction_id from st_lms_bo_transaction_master where (transaction_date >=? and transaction_date<?) and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')) and game_id = ? group by game_id )as myn, (select game_nbr, game_id, game_name from st_dg_game_master)as top where myn.game_id = top.game_id";
		try {
			pstmt = con
					.prepareStatement("select game_id from st_iw_game_master");
			logger.debug("******Query for game master*******" + pstmt);
			ResultSet st = pstmt.executeQuery();
			Timestamp localStartDate = null;
			while (st.next()) {
				pstmt = con
						.prepareStatement("select max(end_date) as start_date from st_lms_bo_tasks where transaction_type='GOVT_COMM' and (status='APPROVED' or status='DONE') and service_code = ? and game_id = ?");
				pstmt.setString(1, serviceCode);
				pstmt.setInt(2, st.getInt("game_id"));
				logger.debug("******Query for start date game_wise******"
						+ pstmt);
				ResultSet resultSet = pstmt.executeQuery();
				if (resultSet.next()) {
					if (resultSet.getTimestamp("start_date") != null) {
						localStartDate = new Timestamp(add(
								new Date(resultSet.getTimestamp("start_date")
										.getTime()), 1).getTime());
					} else {
						localStartDate = startDate;
					}
				}
				startDateMap.put(st.getInt("game_id"), localStartDate);
			}
			Iterator<Map.Entry<Integer, Timestamp>> itr = startDateMap
					.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<Integer, Timestamp> pair = itr.next();
				pstmt = con.prepareStatement(queryMain);
				logger.debug("******startDate for gameid" + pair.getKey()
						+ "is" + pair.getValue());
				pstmt.setTimestamp(1, pair.getValue());
				pstmt.setTimestamp(2, endDate);
				pstmt.setInt(3, pair.getKey());
				logger.debug("*******QueryMain****" + pstmt);
				mainRs = pstmt.executeQuery();

				/*pstmt = con.prepareStatement(queryMainRef);
				logger.debug("******startDate for gameid" + pair.getKey()
						+ "is" + pair.getValue());
				pstmt.setTimestamp(1, pair.getValue());
				pstmt.setTimestamp(2, endDate);
				pstmt.setInt(3, pair.getKey());
				logger.debug("*******QueryMainRef****" + pstmt);
				mainRsRef = pstmt.executeQuery();
*/
				while (mainRs.next()) {
					GovBean = new GovCommBean();
					
					GovBean.setGovAmount(mainRs.getString("total"));
					totalGovCommAmt += mainRs.getDouble("total");
					
					GovBean.setGameNbr(mainRs.getInt("gameNbr"));
					GovBean.setGameId(mainRs.getInt("gameId"));
					GovBean.setGameName(mainRs.getString("game_name"));
					GovBean.setStartDate(new Date(pair.getValue().getTime())
							.toString());
					GovBean.setEndDate(new Date(endDate.getTime() - 24 * 60
							* 60 * 1000).toString());
					map.put(mainRs.getInt("gameId"), GovBean);
				}
			}
			// insert the entry for this approval in st_lms_bo_tasks
			DBConnect.closeCon(con);
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, String> getServiceList() {
		Map<String, String> serviceNameMap = new TreeMap<String, String>();
		con = DBConnect.getConnection();
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("Select * from st_lms_service_master where service_code <>'MGMT' and status='ACTIVE'");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				serviceNameMap.put(rs.getString("service_code"), rs
						.getString("service_display_name"));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.debug("service Name map  ===== " + serviceNameMap);
		return serviceNameMap;
	}

	public Map<String, String> getcommissionTypeList() {
		Map<String, String> serviceTypeMap = new TreeMap<String, String>();
		try {
				serviceTypeMap.put("GOVT_COMM","GOVT_COMM");
				serviceTypeMap.put("VAT", "VAT");
			}
		 catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}

		logger.debug("service Name map  ===== " + serviceTypeMap);
		return serviceTypeMap;
	}

	public double getTotalGovCommAmt() {
		return totalGovCommAmt;
	}

	public String getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endDate = new Date(cal.getTime().getTime());
		logger.debug("Gov Comm End date 1 == " + endDate);
		return endDate.toString();
	}

	public void setTotalGovCommAmt(double totalGovCommAmt) {
		this.totalGovCommAmt = totalGovCommAmt;
	}

	public List<TaskBean> taskGovtCommSearch(String serviceCode, String type) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<TaskBean> taskList = new ArrayList<TaskBean>();
		TaskBean taskBean = null;
		try {
			String query = null;
			String transactionType= "SALE".equals(type) ? "GOVT_COMM" : "GOVT_COMM_PWT";
			if(serviceCode.equalsIgnoreCase("SE")) {
				query = QueryManager.getST3GovtCommApprovedSE();
			} else if(serviceCode.equalsIgnoreCase("DG")) {
				query = "SELECT task_id, bt.game_id, game_nbr, game_name, start_date, end_date, amount, transaction_type, status FROM st_lms_bo_tasks bt INNER JOIN st_dg_game_master gm ON bt.game_id=gm.game_id WHERE transaction_type='"+transactionType+"' AND bt.service_code='DG' AND bt.status='APPROVED';";
			} else if(serviceCode.equalsIgnoreCase("SLE")) {
				query = "SELECT task_id, bt.game_id, bt.game_id game_nbr, type_disp_name game_name, start_date, end_date, amount, transaction_type, status FROM st_lms_bo_tasks bt INNER JOIN st_sle_game_type_master gm ON bt.game_id=gm.game_type_id WHERE transaction_type='"+transactionType+"' AND bt.service_code='SLE' AND bt.status='APPROVED';";
			}
			logger.info("taskGovtCommSearch - "+query);

			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getInt("task_id"));
				taskBean.setGameId(rs.getInt("game_id"));
				taskBean.setGameNbr(rs.getInt("game_nbr"));
				taskBean.setGameName(rs.getString("game_name"));
				taskBean.setStartDate(dateFormat.format(rs.getTimestamp("start_date")));
				taskBean.setEndDate(dateFormat.format(rs.getTimestamp("end_date")));
				taskBean.setAmount(rs.getDouble("amount"));
				taskBean.setTransactionType(rs.getString("transaction_type"));
				taskBean.setStatus(rs.getString("status"));
				taskList.add(taskBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return taskList;
	}

	public List<TaskBean> taskGovtCommAndVatCommSearch(String serviceCode,String startDate,String endDate,String commissionType) {

		con = DBConnect.getConnection();
		List<TaskBean> GovtCommSearchResults = new ArrayList<TaskBean>();
		try {

			PreparedStatement stmt = con.prepareStatement("");
			TaskBean taskBean = null;
			ResultSet rs = null;
			String queryForGovtCommSearch = null;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = (java.util.Date) dateFormat.parse(endDate);
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String devEndDate = dateFormat.format(cal.getTime());
			if(commissionType.equalsIgnoreCase("GOVT_COMM"))
			{
				if (serviceCode.equalsIgnoreCase("DG")) {
					queryForGovtCommSearch = "select * from st_lms_bo_govt_transaction task,st_dg_game_master game,st_lms_bo_transaction_master trans where task.transaction_type='"+commissionType+"' and  task.service_code='"+serviceCode+"' and trans.transaction_date>='"+startDate+"' and trans.transaction_date<'"+devEndDate+"' and task.game_id=game.game_id and trans.transaction_id=task.transaction_id and trans.transaction_type='"+commissionType+"'";
				} else {
					if (serviceCode.equalsIgnoreCase("SE")) {
						queryForGovtCommSearch = "select * from st_lms_bo_govt_transaction task,st_se_game_master game,st_lms_bo_transaction_master trans where task.transaction_type='"+commissionType+"' and  task.service_code='"+serviceCode+"' and trans.transaction_date>='"+startDate+"' and trans.transaction_date<'"+devEndDate+"' and task.game_id=game.game_id and trans.transaction_id=task.transaction_id and trans.transaction_type='"+commissionType+"'";
					}
				}
			}
			else
			{
					queryForGovtCommSearch = "select * from st_lms_bo_govt_transaction task,st_lms_bo_transaction_master trans where task.transaction_type='"+commissionType+"' and  task.service_code='"+serviceCode+"' and trans.transaction_date>='"+startDate+"' and trans.transaction_date<'"+devEndDate+"' and  trans.transaction_id=task.transaction_id and trans.transaction_type='"+commissionType+"'";
					System.out.println(queryForGovtCommSearch);
			}
			rs = stmt.executeQuery(queryForGovtCommSearch);
			logger.debug("query to search approved Gov Comm  == "
					+ queryForGovtCommSearch);
			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setAmount(rs.getDouble("amount"));
				// taskBean.setMonth(rs.getString(TableConstants.MONTH_TASK));
				taskBean.setTransactionType(rs.getString("transaction_type"));
				taskBean.setGameId(rs.getInt("game_id"));
				taskBean.setStatus("Submitted");
				taskBean.setSubmitDate(rs.getDate("transaction_date").toString());
				if(commissionType.equalsIgnoreCase("VAT"))
				{
				taskBean.setGameNbr(0);
				taskBean.setGameName("N/A");
				}
				else
				{
					taskBean.setGameNbr(rs.getInt("game_nbr"));
					taskBean.setGameName(rs.getString("game_name"));
				}
				taskBean.setStartDate(rs.getDate("start_date").toString());
				taskBean.setEndDate(rs.getDate("end_date").toString());
				GovtCommSearchResults.add(taskBean);
			}
			return GovtCommSearchResults;

		} catch (SQLException e) {
			logger.error("Exception: " + e);

			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}
		return GovtCommSearchResults;
	}
	
	public void insertGovCommDetails(Map<Integer, GovCommBean> approvedMap, String serviceCode, Connection connection) {
		SimpleDateFormat endDateFormat = null;
		try {
			endDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pstmt = connection.prepareStatement("INSERT INTO st_lms_bo_tasks (amount, game_id, transaction_type, status, approve_date, start_date, end_date, service_code) VALUES (?,?,?,?,?,?,?,?);");

			int gameId = 0;
			GovCommBean commissionBean = null;
			for (Map.Entry<Integer, GovCommBean> entry : approvedMap.entrySet()) {
				gameId = entry.getKey();
				commissionBean = entry.getValue();

				pstmt.setDouble(1, Double.parseDouble(commissionBean.getGovAmount()));
				pstmt.setInt(2, gameId);
				pstmt.setString(3, "GOVT_COMM");
				pstmt.setString(4, "UNAPPROVED");
				pstmt.setDate(5, new Date(Util.getCurrentTimeStamp().getTime()));
				pstmt.setDate(6, new java.sql.Date(sqlDateFormatter.parse(commissionBean.getStartDate()).getTime()));
				pstmt.setTimestamp(7, new Timestamp(endDateFormat.parse(commissionBean.getEndDate() + " 23:59:59").getTime()));
				pstmt.setString(8, serviceCode);
				pstmt.addBatch();

				pstmt.clearParameters();

				pstmt.setDouble(1, commissionBean.getGovPwtAmount());
				pstmt.setInt(2, gameId);
				pstmt.setString(3, "GOVT_COMM_PWT");
				pstmt.setString(4, "UNAPPROVED");
				pstmt.setDate(5, new Date(Util.getCurrentTimeStamp().getTime()));
				pstmt.setDate(6, new java.sql.Date(sqlDateFormatter.parse(commissionBean.getStartDate()).getTime()));
				pstmt.setTimestamp(7, new Timestamp(endDateFormat.parse(commissionBean.getEndDate() + " 23:59:59").getTime()));
				pstmt.setString(8, serviceCode);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (Exception e) {
			logger.error("Exception - " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closePstmt(pstmt);
		}
	}
}