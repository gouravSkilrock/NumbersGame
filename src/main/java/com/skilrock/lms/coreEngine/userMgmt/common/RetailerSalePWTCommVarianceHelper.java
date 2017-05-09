package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.SalePwtCommVarBean;
import com.skilrock.lms.beans.UpdateSalePwtCommVarBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerSalePWTCommVarianceHelper {
	static Log logger = LogFactory.getLog(RetailerSalePWTCommVarianceHelper.class);
	private Connection con;
	public Map<String, Double> maxCommRate = new TreeMap<String, Double>();
	private PreparedStatement pstmt;

	private ResultSet rs;

	public Map<String, Double> getDefaultCommRate(String nbr, String name) {
		double maxSaleVariance = 0.0;
		double maxPwtVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select retailer_sale_comm_rate 'max_sale_rate',  retailer_pwt_comm_rate  'max_pwt_rate',  govt_comm_rate  'max_gov_rate',  prize_payout_ratio  'max_prize_ratio' from st_se_game_master where  game_status!='TERMINATE' and game_nbr="
				+ nbr ;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" game name query === " + pstmt);
			while (rss.next()) {
				double maxSaleCommRate = rss.getDouble("max_sale_rate");
				double maxGovRate = rss.getDouble("max_gov_rate");
				double maxPrizeRatio = rss.getDouble("max_prize_ratio");
				double maxPwtCommRate = rss.getDouble("max_pwt_rate");
				maxSaleVariance = 100 - (maxSaleCommRate + maxGovRate + maxPrizeRatio);
				maxPwtVariance = 100 - (maxPwtCommRate + maxGovRate + maxPrizeRatio);
				// maxPwtVariance=100(maxPwtCommRate+)
				maxCommRate.put("maxSaleVariance", maxSaleVariance);
				maxCommRate.put("maxPwtVariance", maxPwtVariance);
				// maxCommRate.put("maxPwtVariance", value)
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}

	public Map<String, Double> getDefaultCommRateDg(String nbr, String name) {
		double maxSaleVariance = 0.0;
		double maxPwtVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select retailer_sale_comm_rate 'max_sale_rate',  retailer_pwt_comm_rate  'max_pwt_rate',  govt_comm  'max_gov_rate',  prize_payout_ratio  'max_prize_ratio' from st_dg_game_master where  game_status!='TERMINATE' and game_id="
				+ nbr ;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" game name query === DG " + pstmt);
			while (rss.next()) {
				double maxSaleCommRate = rss.getDouble("max_sale_rate");
				double maxGovRate = rss.getDouble("max_gov_rate");
				double maxPrizeRatio = rss.getDouble("max_prize_ratio");
				double maxPwtCommRate = rss.getDouble("max_pwt_rate");
				maxSaleVariance = 100 - (maxSaleCommRate + maxGovRate + maxPrizeRatio);
				maxPwtVariance = 100 - (maxPwtCommRate + maxGovRate + maxPrizeRatio);
				// maxPwtVariance=100(maxPwtCommRate+)
				maxCommRate.put("maxSaleVariance", maxSaleVariance);
				maxCommRate.put("maxPwtVariance", maxPwtVariance);
				// maxCommRate.put("maxPwtVariance", value)
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}
	
	public Map<String, Double> getDefaultCommRateIW(String nbr, String name) {
		double maxSaleVariance = 0.0;
		double maxPwtVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select retailer_sale_comm_rate 'max_sale_rate',  retailer_pwt_comm_rate  'max_pwt_rate', gov_comm_rate 'max_gov_rate',  prize_payout_ratio  'max_prize_ratio' from st_iw_game_master where  game_status!='TERMINATE' and game_id=" + nbr;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" game name query === DG " + pstmt);
			while (rss.next()) {
				double maxSaleCommRate = rss.getDouble("max_sale_rate");
				double maxGovRate = rss.getDouble("max_gov_rate");
				double maxPrizeRatio = rss.getDouble("max_prize_ratio");
				double maxPwtCommRate = rss.getDouble("max_pwt_rate");
				maxSaleVariance = 100 - (maxSaleCommRate + maxGovRate + maxPrizeRatio);
				maxPwtVariance = 100 - (maxPwtCommRate + maxGovRate + maxPrizeRatio);
				// maxPwtVariance=100(maxPwtCommRate+)
				maxCommRate.put("maxSaleVariance", maxSaleVariance);
				maxCommRate.put("maxPwtVariance", maxPwtVariance);
				// maxCommRate.put("maxPwtVariance", value)
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}
	
	public Map<String, Double> getDefaultCommRateVS(String nbr, String name) {
		double maxSaleVariance = 0.0;
		double maxPwtVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select retailer_sale_comm_rate 'max_sale_rate',  retailer_pwt_comm_rate  'max_pwt_rate', gov_comm_rate 'max_gov_rate',  prize_payout_ratio  'max_prize_ratio' from st_vs_game_master where  game_status!='TERMINATE' and game_id=" + nbr;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" game name query === DG " + pstmt);
			while (rss.next()) {
				double maxSaleCommRate = rss.getDouble("max_sale_rate");
				double maxGovRate = rss.getDouble("max_gov_rate");
				double maxPrizeRatio = rss.getDouble("max_prize_ratio");
				double maxPwtCommRate = rss.getDouble("max_pwt_rate");
				maxSaleVariance = 100 - (maxSaleCommRate + maxGovRate + maxPrizeRatio);
				maxPwtVariance = 100 - (maxPwtCommRate + maxGovRate + maxPrizeRatio);
				// maxPwtVariance=100(maxPwtCommRate+)
				maxCommRate.put("maxSaleVariance", maxSaleVariance);
				maxCommRate.put("maxPwtVariance", maxPwtVariance);
				// maxCommRate.put("maxPwtVariance", value)
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}
	
	public Map<String, Double> getDefaultCommRateCS(String nbr, String prodCode, String provider) {
		double maxSaleVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select retailer_comm 'max_sale_rate', good_cause_comm  'max_gov_rate' from st_cs_product_master where  status!='INACTIVE' and product_id="
				+ nbr ;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			logger.debug("product name query for CS === " + pstmt);
			while (rss.next()) {
				double maxSaleCommRate = rss.getDouble("max_sale_rate");
				double maxGovRate = rss.getDouble("max_gov_rate");
				maxSaleVariance = 100 - (maxSaleCommRate + maxGovRate);
				maxCommRate.put("maxSaleVariance", maxSaleVariance);
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}
	
	public Map<String, Double> getDefaultCommRateForOLA(String id, String name){
		double maxWidVariance = 0.0;
		double maxDepVariance = 0.0;
		double maxNetGVariance = 0.0;
		Connection conn = DBConnect.getConnection();
		String sqlQuery = "select ret_withdrawl_comm 'max_wid_rate',  ret_deposit_comm  'max_dep_rate', ret_net_gaming_comm 'max_netg_rate' from st_ola_wallet_master where  wallet_status ='ACTIVE' and wallet_id="
			+ id;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rss = pstmt.executeQuery();
			System.out.println(" wallet name query === OLA " + pstmt);
			while (rss.next()) {
				double maxWidCommRate = rss.getDouble("max_wid_rate");
				double maxDepCommRate = rss.getDouble("max_dep_rate");
				double maxNetGCommRate = rss.getDouble("max_netg_rate");
				maxWidVariance = 100 - (maxWidCommRate + maxDepCommRate + maxNetGCommRate);
				maxDepVariance = 100 - (maxWidCommRate + maxDepCommRate + maxNetGCommRate);
				maxNetGVariance = 100 - (maxWidCommRate + maxDepCommRate + maxNetGCommRate);
				// maxPwtVariance=100(maxPwtCommRate+)
				maxCommRate.put("maxWidVariance", maxWidVariance);
				maxCommRate.put("maxDepVariance", maxDepVariance);
				maxCommRate.put("maxNetGVariance", maxNetGVariance);
				// maxCommRate.put("maxPwtVariance", value)
			}
			if (rss != null) {
				rss.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxCommRate;
	}

	public List<SalePwtCommVarBean> getSalePwtGameDetails(String query,
			Connection conn, String gamestatus) throws SQLException {
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" game name query === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String gamename = rss.getInt("game_nbr") + "-"
					+ rss.getString("game_name");
			bean.setGameName(gamename);

			// retrieve the default Sale commission variance
			double defSaleCommRate = rss.getDouble("retailer_sale_comm_rate");
			bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
			// retrieve the Sale commission variance
			double currentSaleCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentSaleCommVar(FormatNumber
					.formatNumber(currentSaleCommVar));
			// calculate the Sale commission variance
			bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate
					+ currentSaleCommVar));

			double defPwtCommRate = rss.getDouble("retailer_pwt_comm_rate");
			bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
			// retrieve the PWT commission variance
			double currentPwtCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentPwtCommVar(FormatNumber
					.formatNumber(currentPwtCommVar));
			// calculate the PWT commission variance
			bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate
					+ currentPwtCommVar));

			salePwtCommVarList.add(bean);
		}

		return salePwtCommVarList;
	}

	public List<SalePwtCommVarBean> getSalePwtGameDetailsDg(String query,
			Connection conn, String gamestatus) throws SQLException {
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" game name query === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String gamename = rss.getInt("game_id") + "-"
					+ rss.getString("game_name");
			bean.setGameName(gamename);

			// retrieve the default Sale commission variance
			double defSaleCommRate = rss.getDouble("retailer_sale_comm_rate");
			bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
			// retrieve the Sale commission variance
			double currentSaleCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentSaleCommVar(FormatNumber
					.formatNumber(currentSaleCommVar));
			// calculate the Sale commission variance
			bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate
					+ currentSaleCommVar));

			double defPwtCommRate = rss.getDouble("retailer_pwt_comm_rate");
			bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
			// retrieve the PWT commission variance
			double currentPwtCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentPwtCommVar(FormatNumber
					.formatNumber(currentPwtCommVar));
			// calculate the PWT commission variance
			bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate
					+ currentPwtCommVar));

			salePwtCommVarList.add(bean);
		}

		return salePwtCommVarList;
	}

	public List<SalePwtCommVarBean> getSalePwtGameDetailsIW(String query, Connection conn, String gamestatus) throws SQLException {
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" game name query === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String gamename = rss.getInt("game_id") + "-" + rss.getString("game_disp_name");
			bean.setGameName(gamename);

			// retrieve the default Sale commission variance
			double defSaleCommRate = rss.getDouble("retailer_sale_comm_rate");
			bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
			// retrieve the Sale commission variance
			double currentSaleCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
			// calculate the Sale commission variance
			bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate + currentSaleCommVar));

			double defPwtCommRate = rss.getDouble("retailer_pwt_comm_rate");
			bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
			// retrieve the PWT commission variance
			double currentPwtCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
			// calculate the PWT commission variance
			bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate + currentPwtCommVar));

			salePwtCommVarList.add(bean);
		}
		return salePwtCommVarList;
	}
	
	public List<SalePwtCommVarBean> getSalePwtGameDetailsVS(String query, Connection conn, String gamestatus) throws SQLException {
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" game name query === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String gamename = rss.getInt("game_id") + "-" + rss.getString("game_disp_name");
			bean.setGameName(gamename);

			// retrieve the default Sale commission variance
			double defSaleCommRate = rss.getDouble("retailer_sale_comm_rate");
			bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
			// retrieve the Sale commission variance
			double currentSaleCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
			// calculate the Sale commission variance
			bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate + currentSaleCommVar));

			double defPwtCommRate = rss.getDouble("retailer_pwt_comm_rate");
			bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
			// retrieve the PWT commission variance
			double currentPwtCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
			// calculate the PWT commission variance
			bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate + currentPwtCommVar));

			salePwtCommVarList.add(bean);
		}
		return salePwtCommVarList;
	}

	public List<SalePwtCommVarBean> getSaleProdDetailsCS(String query,
			Connection conn, String prodStatus)throws SQLException{
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" products query for comm var === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String prodName = rss.getInt("product_id") + "_"
			+ rss.getString("product_code")+"_"+rss.getString("operator_name")+"_"+rss.getString("denomination");
			bean.setGameName(prodName);

			// retrieve the default Sale commission variance
			double defSaleCommRate = rss.getDouble("retailer_comm");
			bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
			// retrieve the Sale commission variance
			double currentSaleCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentSaleCommVar(FormatNumber
					.formatNumber(currentSaleCommVar));
			// calculate the Sale commission variance
			bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate
					+ currentSaleCommVar));

			salePwtCommVarList.add(bean);
		}
		return salePwtCommVarList;
	}

	public List<SalePwtCommVarBean> salePwtCommVarList(String agentOrgName,
			String gamestatus, String orgTypeCode) {
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();
		
		try {
			int orgId = Integer.parseInt(agentOrgName);
		String sqlQuery = "select * from st_se_agent_retailer_sale_pwt_comm_variance aa, st_se_game_master gm where retailer_org_id="
							+ orgId+" and gm.game_id=aa.game_id ";
		if (!"ALL".equalsIgnoreCase(gamestatus)) {
			sqlQuery = sqlQuery + "and gm.game_status='" + gamestatus + "'";
		}
		System.out.println("agent organization name is ==== " + agentOrgName
				+ "\n RetailerAgentsalePwtCommVar query = " + sqlQuery);

			
			
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			// Point to the last row in resultset.
			rs.last();
			// Get the row position which is also the number of rows in the
			// resultset.
			int rowcount = rs.getRow();
			System.out.println("row count value is ======== " + rowcount);
			rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				System.out.println("inside if row count 11111111111111");
				// get the game name
				int gameId = rs.getInt("game_id");
				if (gameId <= 0) {
					if (gameId == 0) {
						bean.setGameName("Applicable on All Existing Games");
					} else if (gameId == -1) {
						bean
								.setGameName("Applicable on All Existing and New Games");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default Sale commission variance
					bean.setDefSaleCommRate(null);
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(null);

					bean.setDefPwtCommRate(null);
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs
							.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber
							.formatNumber(currentPwtCommVar));
					// calculate the PWT commission variance
					bean.setNetSaleCommRate(null);

					salePwtCommVarList.add(bean);
					System.out.println("inside if row count 2222222222");
				} else {
					System.out
							.println("inside else rowcount 111111111111111111");
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("game_nbr") + "-"
							+ rs.getString("game_name"));

					// retrieve the default Sale commission variance
					double defSaleCommRate = rs
							.getDouble("default_sale_comm_rate");
					bean.setDefSaleCommRate(FormatNumber
							.formatNumber(defSaleCommRate));
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean
							.setNetSaleCommRate(FormatNumber
									.formatNumber(defSaleCommRate
											+ currentSaleCommVar));

					double defPwtCommRate = rs
							.getDouble("default_pwt_comm_rate");
					bean.setDefPwtCommRate(FormatNumber
							.formatNumber(defPwtCommRate));
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs
							.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber
							.formatNumber(currentPwtCommVar));
					// calculate the PWT commission variance
					bean.setNetPwtCommRate(FormatNumber
							.formatNumber(defPwtCommRate + currentPwtCommVar));

					salePwtCommVarList.add(bean);
					System.out.println("inside else row count 2222222222");
				}
			}
			rs.close();
			stmt.close();

			// now get the details of games from st_se_game_master thats
			// commission variance are not set
			String query = "select game_id, game_nbr, game_name, retailer_sale_comm_rate, retailer_pwt_comm_rate, govt_comm_rate, prize_payout_ratio from st_se_game_master where game_id NOT IN (select game_id from st_se_agent_retailer_sale_pwt_comm_variance where retailer_org_id="
							+ orgId + " )";
			if (!"ALL".equalsIgnoreCase(gamestatus)) {
				query = query + "and game_status='" + gamestatus + "'";
			}
			System.out.println("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getSalePwtGameDetails(query,
					con, gamestatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}

		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}

		System.out.println("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;

	}

	public List<SalePwtCommVarBean> salePwtCommVarListForDg(
			String agentOrgName, String gamestatus, String orgTypeCode) {
		System.out.println("salePwtCommVarListForDg called");
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();
	

		try {
			
			int orgId =Integer.parseInt(agentOrgName);
			String sqlQuery = "select * from st_dg_agent_retailer_sale_pwt_comm_variance aa, st_dg_game_master gm where retailer_org_id="
							+ orgId +" and gm.game_id=aa.game_id and gm.game_id not in(select game_id from st_dg_game_master where closing_time < now())";
		/*
		 * if (!("ALL".equalsIgnoreCase(gamestatus))) sqlQuery = sqlQuery + "and
		 * gm.game_status='" + gamestatus + "'";
		 */
		System.out.println("agent organization name is ==== " + agentOrgName
				+ "\n RetailerAgent query = " + sqlQuery);
			
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			// Point to the last row in resultset.
			rs.last();
			// Get the row position which is also the number of rows in the
			// resultset.
			int rowcount = rs.getRow();
			System.out.println("row count value is ======== " + rowcount);
			rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				System.out.println("inside if row count 11111111111111");
				// get the game name
				int gameId = rs.getInt("game_id");
				if (gameId <= 0) {
					if (gameId == 0) {
						bean.setGameName("Applicable on All Existing Games");
					} else if (gameId == -1) {
						bean
								.setGameName("Applicable on All Existing and New Games");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default Sale commission variance
					bean.setDefSaleCommRate(null);
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(null);

					bean.setDefPwtCommRate(null);
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs
							.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber
							.formatNumber(currentPwtCommVar));
					// calculate the PWT commission vasalePwtCommVarListForDgriance
					bean.setNetSaleCommRate(null);

					salePwtCommVarList.add(bean);
					System.out.println("inside if row count 2222222222");
				} else {
					System.out
							.println("inside else rowcount 111111111111111111");
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("game_id") + "-"
							+ rs.getString("game_name"));

					// retrieve the default Sale commission variance
					double defSaleCommRate = rs
							.getDouble("default_sale_comm_rate");
					bean.setDefSaleCommRate(FormatNumber
							.formatNumber(defSaleCommRate));
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean
							.setNetSaleCommRate(FormatNumber
									.formatNumber(defSaleCommRate
											+ currentSaleCommVar));

					double defPwtCommRate = rs
							.getDouble("default_pwt_comm_rate");
					bean.setDefPwtCommRate(FormatNumber
							.formatNumber(defPwtCommRate));
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs
							.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber
							.formatNumber(currentPwtCommVar));
					// calculate the PWT commission variance
					bean.setNetPwtCommRate(FormatNumber
							.formatNumber(defPwtCommRate + currentPwtCommVar));

					salePwtCommVarList.add(bean);
					System.out.println("inside else row count 2222222222");
				}
			}
			rs.close();
			stmt.close();

			// now get the details of games from st_dg_game_master thats
			// commission variance are not set
			String query = "select game_id, game_nbr, game_name, retailer_sale_comm_rate, retailer_pwt_comm_rate, govt_comm, prize_payout_ratio from st_dg_game_master where game_id NOT IN (select game_id from st_dg_agent_retailer_sale_pwt_comm_variance where retailer_org_id="
					+ orgId + " union select game_id from st_dg_game_master where closing_time < now())";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) query = query + "and
			 * game_status='" + gamestatus + "'";
			 */
			System.out.println("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getSalePwtGameDetailsDg(query,
					con, gamestatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}

		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}

		System.out.println("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;

	}
	
	public List<SalePwtCommVarBean> saleCommVarListForCS(
			String agentOrgName, String prodStatus, String orgTypeCode) {

		logger.debug("saleCommVarListForCS called");
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();
		
		try {
			int orgId =Integer.parseInt(agentOrgName);
			
			String sqlQuery = "select * from st_cs_agent_retailer_sale_comm_variance aa, st_cs_product_master pm, st_cs_operator_master com where pm.operator_code = com.operator_code and pm.status = 'ACTIVE' and retailer_org_id="
							+ orgId+"	 and pm.status='ACTIVE' and pm.product_id=aa.product_id and pm.operator_code = com.operator_code ";

			logger.debug("agent organization name is ==== " + agentOrgName
				+ "\n RetailerAgent query = " + sqlQuery);

			
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			// Point to the last row in resultset.
			rs.last();
			// Get the row position which is also the number of rows in the
			// resultset.
			int rowcount = rs.getRow();
			logger.debug("row count value is ======== " + rowcount);
			rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				// get the product name
				int prodNum = rs.getInt("product_id");
				if (prodNum <= 0) {
					if (prodNum == 0) {
						bean.setGameName("Applicable on All Existing Products");
					} else if (prodNum == -1) {
						bean
								.setGameName("Applicable on All Existing and New Games");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default Sale commission variance
					bean.setDefSaleCommRate(null);
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(null);
					salePwtCommVarList.add(bean);
				} else {
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("product_id") + "_"
							+ rs.getString("product_code")+"_"+rs.getString("operator_name")+"_"+rs.getString("denomination"));

					// retrieve the default Sale commission variance
					double defSaleCommRate = rs
							.getDouble("default_sale_comm_rate");
					bean.setDefSaleCommRate(FormatNumber
							.formatNumber(defSaleCommRate));
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs
							.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber
							.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean
							.setNetSaleCommRate(FormatNumber
									.formatNumber(defSaleCommRate
											+ currentSaleCommVar));
					salePwtCommVarList.add(bean);
				}
			}
			rs.close();
			stmt.close();

			// now get the details of products from st_cs_product_master thats
			// commission variance are not set
			String query = "select product_id, product_code,operator_name, denomination, retailer_comm, good_cause_comm, operator_name from st_cs_product_master pm, st_cs_operator_master com where pm.status='ACTIVE' and pm.operator_code = com.operator_code and pm.product_id NOT IN (select product_id from st_cs_agent_retailer_sale_comm_variance where retailer_org_id="
					+orgId + ")";
			logger.debug("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getSaleProdDetailsCS(query,
					con, prodStatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}

		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}
		logger.debug("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;
	}
	
	public List<SalePwtCommVarBean> salePwtCommVarListOLA(String agentOrgName, String walletStatus, String orgTypeCode){
		System.out.println("salePwtCommVarListForOLA called");
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();
		

		try {
			int orgId =Integer.parseInt(agentOrgName);
			
			String sqlQuery = "select * from st_ola_agent_retailer_comm_variance aa, st_ola_wallet_master wm where retailer_org_id="
								+ orgId+" and wm.wallet_id=aa.wallet_id ";
		/*
		 * if (!("ALL".equalsIgnoreCase(gamestatus))) sqlQuery = sqlQuery + "and
		 * gm.game_status='" + gamestatus + "'";
		 */
		logger.debug("agent organization name is ==== " + agentOrgName
				+ "\n query = " + sqlQuery);
			
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			// Point to the last row in resultset.
			//rs.last();
			// Get the row position which is also the number of rows in the
			// resultset.
			//int rowcount = rs.getRow();
			//logger.debug("row count value is ======== " + rowcount);
			//rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				logger.debug("inside if row count 11111111111111");
				// get the game name
				int walletId = rs.getInt("wallet_id");
				if (walletId <= 0) {
					if (walletId == 0) {
						bean.setGameName("Applicable on All Existing Wallets");
					} else if (walletId == -1) {
						bean
								.setGameName("Applicable on All Existing and New Wallets");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default withdrawal commission variance
					bean.setDefWidCommRate(null);
					// retrieve the withdrawal commission variance
					double currentWidCommVar = rs
							.getDouble("withdrawl_comm_variance");
					bean.setCurrentWidCommVar(FormatNumber
							.formatNumber(currentWidCommVar));
					// calculate the withdrawal commission variance
					bean.setNetWidCommRate(null);

					bean.setDefDepCommRate(null);
					// retrieve the Deposit commission variance
					double currentDepCommVar = rs
							.getDouble("deposit_comm_variance");
					bean.setCurrentWidCommVar(FormatNumber
							.formatNumber(currentDepCommVar));
					// calculate the Deposit commission variance
					bean.setNetSaleCommRate(null);
					
					bean.setDefNetGCommRate(null);
					// retrieve the Net Gaming commission variance
					double currentNetGCommVar = rs
							.getDouble("net_gaming_comm_variance");
					bean.setCurrentWidCommVar(FormatNumber
							.formatNumber(currentNetGCommVar));
					// calculate the Net Gaming commission variance
					bean.setNetNetGCommRate(null);

					salePwtCommVarList.add(bean);
					logger.debug("inside if row count 2222222222");
				} else {
					logger.debug("inside else rowcount 111111111111111111");
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("wallet_id") + "-"
							+ rs.getString("wallet_name"));

					// retrieve the default Withdrawal commission variance
					double defWidCommRate = rs
							.getDouble("default_withdrawl_comm_rate");
					bean.setDefWidCommRate(FormatNumber
							.formatNumber(defWidCommRate));
					// retrieve the Withdrawal commission variance
					double currentWidCommVar = rs
							.getDouble("withdrawl_comm_variance");
					bean.setCurrentWidCommVar(FormatNumber
							.formatNumber(currentWidCommVar));
					// calculate the Withdrawal commission variance
					bean
							.setNetWidCommRate(FormatNumber
									.formatNumber(defWidCommRate
											+ currentWidCommVar));

					double defDepCommRate = rs
							.getDouble("default_deposit_comm_rate");
					bean.setDefDepCommRate(FormatNumber
							.formatNumber(defDepCommRate));
					// retrieve the Deposit commission variance
					double currentDepCommVar = rs
							.getDouble("deposit_comm_variance");
					bean.setCurrentDepCommVar(FormatNumber
							.formatNumber(currentDepCommVar));
					// calculate the Deposit commission variance
					bean.setNetDepCommRate(FormatNumber
							.formatNumber(defDepCommRate + currentDepCommVar));
					
					double defNetGCommRate = rs
					.getDouble("default_net_gaming_comm_rate");
					bean.setDefNetGCommRate(FormatNumber
						.formatNumber(defNetGCommRate));
					// retrieve the Net Gaming commission variance
					double currentNetGCommVar = rs
							.getDouble("net_gaming_comm_variance");
					bean.setCurrentNetGCommVar(FormatNumber
							.formatNumber(currentNetGCommVar));
					// calculate the Net Gaming commission variance
					bean.setNetNetGCommRate(FormatNumber
							.formatNumber(defNetGCommRate + currentNetGCommVar));

					salePwtCommVarList.add(bean);
					logger.debug("inside else row count 2222222222");
				}
			}
			rs.close();
			stmt.close();

			// now get the details of games from st_dg_game_master thats
			// commission variance are not set
			String query = "select wallet_id, wallet_name, ret_withdrawl_comm, ret_deposit_comm, ret_net_gaming_comm from st_ola_wallet_master where wallet_id NOT IN (select wallet_id from st_ola_agent_retailer_comm_variance where retailer_org_id="
					+ orgId+ ")";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) query = query + "and
			 * game_status='" + gamestatus + "'";
			 */
			System.out.println("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getWidDepNetgameDetailsOLA(query,
					con, walletStatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}

		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}

		System.out.println("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;

	
	}
	
	public List<SalePwtCommVarBean> getWidDepNetgameDetailsOLA(String query,
			Connection conn, String walletStatus)throws SQLException{

		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		Connection connection = conn;
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rss = pstmt.executeQuery();
		System.out.println(" wallet name query === " + pstmt);
		while (rss.next()) {
			bean = new SalePwtCommVarBean();
			String walletname = rss.getInt("wallet_id") + "-"
					+ rss.getString("wallet_name");
			bean.setGameName(walletname);

			// retrieve the default Withdrawal commission variance
			double defWidCommRate = rss.getDouble("ret_withdrawl_comm");
			bean.setDefWidCommRate(FormatNumber.formatNumber(defWidCommRate));
			// retrieve the Withdrawal commission variance
			double currentWidCommVar = 0.0;// rss.getDouble("sale_comm_variance");
			bean.setCurrentWidCommVar(FormatNumber
					.formatNumber(currentWidCommVar));
			// calculate the Sale commission variance
			bean.setNetWidCommRate(FormatNumber.formatNumber(defWidCommRate
					+ currentWidCommVar));

			double defDepCommRate = rss.getDouble("ret_deposit_comm");
			bean.setDefDepCommRate(FormatNumber.formatNumber(defDepCommRate));
			// retrieve the Deposit commission variance
			double currentDepCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentDepCommVar(FormatNumber
					.formatNumber(currentDepCommVar));
			// calculate the Deposit commission variance
			bean.setNetDepCommRate(FormatNumber.formatNumber(defDepCommRate
					+ currentDepCommVar));
			
			double defNetGCommRate = rss.getDouble("ret_net_gaming_comm");
			bean.setDefNetGCommRate(FormatNumber.formatNumber(defNetGCommRate));
			// retrieve the Net Gaming commission variance
			double currentNetGCommVar = 0.0;// rss.getDouble("pwt_comm_variance");
			bean.setCurrentNetGCommVar(FormatNumber
					.formatNumber(currentNetGCommVar));
			// calculate the Net Gaming commission variance
			bean.setNetNetGCommRate(FormatNumber.formatNumber(defNetGCommRate
					+ currentNetGCommVar));

			salePwtCommVarList.add(bean);
		}

		return salePwtCommVarList;
	
	}
	
	
	public List<SalePwtCommVarBean> salePwtCommVarListForIW(String agentOrgName, String gamestatus, String orgTypeCode) {
		System.out.println("salePwtCommVarListForIW called");
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();

		try {

			int orgId = Integer.parseInt(agentOrgName);
			String sqlQuery = "select * from st_iw_agent_retailer_sale_pwt_comm_variance aa, st_iw_game_master gm where retailer_org_id=" + orgId + " and gm.game_id=aa.game_id and gm.game_id not in(select game_id from st_iw_game_master where closing_time < now())";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) sqlQuery = sqlQuery +
			 * "and gm.game_status='" + gamestatus + "'";
			 */
			System.out.println("agent organization name is ==== " + agentOrgName + "\n RetailerAgent query = " + sqlQuery);

			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			rs.last();
			int rowcount = rs.getRow();
			System.out.println("row count value is ======== " + rowcount);
			rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				System.out.println("inside if row count 11111111111111");
				int gameId = rs.getInt("game_id");
				if (gameId <= 0) {
					if (gameId == 0) {
						bean.setGameName("Applicable on All Existing Games");
					} else if (gameId == -1) {
						bean.setGameName("Applicable on All Existing and New Games");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default Sale commission variance
					bean.setDefSaleCommRate(null);
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(null);

					bean.setDefPwtCommRate(null);
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
					// calculate the PWT commission
					// vasalePwtCommVarListForDgriance
					bean.setNetSaleCommRate(null);

					salePwtCommVarList.add(bean);
					System.out.println("inside if row count 2222222222");
				} else {
					System.out.println("inside else rowcount 111111111111111111");
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("game_id") + "-" + rs.getString("game_disp_name"));

					// retrieve the default Sale commission variance
					double defSaleCommRate = rs.getDouble("default_sale_comm_rate");
					bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate + currentSaleCommVar));

					double defPwtCommRate = rs.getDouble("default_pwt_comm_rate");
					bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
					// calculate the PWT commission variance
					bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate + currentPwtCommVar));

					salePwtCommVarList.add(bean);
					System.out.println("inside else row count 2222222222");
				}
			}
			rs.close();
			stmt.close();

			String query = "select game_id, game_no, game_disp_name, retailer_sale_comm_rate, retailer_pwt_comm_rate, gov_comm_rate, prize_payout_ratio from st_iw_game_master where game_id NOT IN (select game_id from st_iw_agent_retailer_sale_pwt_comm_variance where retailer_org_id=" + orgId + " union select game_id from st_iw_game_master where closing_time < now())";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) query = query + "and
			 * game_status='" + gamestatus + "'";
			 */
			System.out.println("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getSalePwtGameDetailsIW(query, con, gamestatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}
		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}
		System.out.println("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;
	}
	
	
	public List<SalePwtCommVarBean> salePwtCommVarListForVS(String agentOrgName, String gamestatus, String orgTypeCode) {
		System.out.println("salePwtCommVarListForVS called");
		List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
		SalePwtCommVarBean bean = null;
		con = DBConnect.getConnection();

		try {
			int orgId = Integer.parseInt(agentOrgName);
			String sqlQuery = "select * from st_vs_agent_retailer_sale_pwt_comm_variance aa, st_vs_game_master gm where retailer_org_id=" + orgId + " and gm.game_id=aa.game_id and gm.game_id not in(select game_id from st_vs_game_master where closing_time < now())";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) sqlQuery = sqlQuery +
			 * "and gm.game_status='" + gamestatus + "'";
			 */
			System.out.println("agent organization name is ==== " + agentOrgName + "\n RetailerAgent query = " + sqlQuery);

			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sqlQuery);
			rs.last();
			int rowcount = rs.getRow();
			System.out.println("row count value is ======== " + rowcount);
			rs.beforeFirst();
			while (rs.next()) {
				bean = new SalePwtCommVarBean();
				System.out.println("inside if row count 11111111111111");
				int gameId = rs.getInt("game_id");
				if (gameId <= 0) {
					if (gameId == 0) {
						bean.setGameName("Applicable on All Existing Games");
					} else if (gameId == -1) {
						bean.setGameName("Applicable on All Existing and New Games");
					} else {
						bean.setGameName("Errror occured");
					}
					// retrieve the default Sale commission variance
					bean.setDefSaleCommRate(null);
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(null);

					bean.setDefPwtCommRate(null);
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
					// calculate the PWT commission
					// vasalePwtCommVarListForDgriance
					bean.setNetSaleCommRate(null);

					salePwtCommVarList.add(bean);
					System.out.println("inside if row count 2222222222");
				} else {
					System.out.println("inside else rowcount 111111111111111111");
					bean = new SalePwtCommVarBean();
					bean.setGameName(rs.getInt("game_id") + "-" + rs.getString("game_disp_name"));

					// retrieve the default Sale commission variance
					double defSaleCommRate = rs.getDouble("default_sale_comm_rate");
					bean.setDefSaleCommRate(FormatNumber.formatNumber(defSaleCommRate));
					// retrieve the Sale commission variance
					double currentSaleCommVar = rs.getDouble("sale_comm_variance");
					bean.setCurrentSaleCommVar(FormatNumber.formatNumber(currentSaleCommVar));
					// calculate the Sale commission variance
					bean.setNetSaleCommRate(FormatNumber.formatNumber(defSaleCommRate + currentSaleCommVar));

					double defPwtCommRate = rs.getDouble("default_pwt_comm_rate");
					bean.setDefPwtCommRate(FormatNumber.formatNumber(defPwtCommRate));
					// retrieve the PWT commission variance
					double currentPwtCommVar = rs.getDouble("pwt_comm_variance");
					bean.setCurrentPwtCommVar(FormatNumber.formatNumber(currentPwtCommVar));
					// calculate the PWT commission variance
					bean.setNetPwtCommRate(FormatNumber.formatNumber(defPwtCommRate + currentPwtCommVar));

					salePwtCommVarList.add(bean);
					System.out.println("inside else row count 2222222222");
				}
			}
			rs.close();
			stmt.close();

			String query = "select game_id, game_no, game_disp_name, retailer_sale_comm_rate, retailer_pwt_comm_rate, gov_comm_rate, prize_payout_ratio from st_vs_game_master where game_id NOT IN (select game_id from st_vs_agent_retailer_sale_pwt_comm_variance where retailer_org_id=" + orgId + " union select game_id from st_vs_game_master where closing_time < now())";
			/*
			 * if (!("ALL".equalsIgnoreCase(gamestatus))) query = query + "and
			 * game_status='" + gamestatus + "'";
			 */
			System.out.println("agent  query = " + query);
			List<SalePwtCommVarBean> remList = getSalePwtGameDetailsVS(query, con, gamestatus);
			if (remList.size() > 0) {
				salePwtCommVarList.addAll(remList);
			}
		} catch (SQLException e) {
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
				e.printStackTrace();
			}
		}
		System.out.println("name list ===== " + salePwtCommVarList);
		return salePwtCommVarList;
	}

	public String submitUpdatedValue(String agtOrgName, String gname,
			String defSaleCommRate, String saleCommVar, String defPwtCommRate,
			String pwtCommVar, int userOrgId,String countryDep) throws LMSException {

		String game[] = gname.split("-");
		double dSaleCommRate = Double.parseDouble(defSaleCommRate.trim());
		double saleCommVar1 = Double.parseDouble(saleCommVar.trim());
		double dPwtCommRate = Double.parseDouble(defPwtCommRate.trim());
		double pwtCommVar1 = Double.parseDouble(pwtCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtSaleComm = 0.0;
		double agtSaleComm = 0.0;
		double dAgtPwtComm = 0.0;
		double agtPwtComm = 0.0;
		try {
			int orgId = Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn
					.prepareStatement("select agent_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_se_bo_agent_sale_pwt_comm_variance  gm where game_id=(select game_id from st_se_game_master where game_nbr="
							+ game[0]
							+ " ) and agent_org_id=(select parent_id from st_lms_organization_master where organization_id="
							+ orgId + ")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				dAgtSaleComm = rs0.getDouble("default_sale_comm_rate");
				agtSaleComm = rs0.getDouble("sale_comm_variance");
				dAgtPwtComm = rs0.getDouble("default_pwt_comm_rate");
				agtPwtComm = rs0.getDouble("pwt_comm_variance");
			} else {
				PreparedStatement pstmtGameMaster = conn
						.prepareStatement("select agent_sale_comm_rate,agent_pwt_comm_rate from st_se_game_master where game_nbr="
								+ game[0]);
				ResultSet rsGameMaster = pstmtGameMaster.executeQuery();
				System.out.println("query = " + pstmtGameMaster);
				if (rsGameMaster.next()) {
					dAgtSaleComm = rsGameMaster
							.getDouble("agent_sale_comm_rate");
					dAgtPwtComm = rsGameMaster.getDouble("agent_pwt_comm_rate");
				}
			}
			double diffSaleComm = dAgtSaleComm
					+ (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0))
					- (dSaleCommRate + saleCommVar1);
			double diffPwtComm = dAgtPwtComm
					+ (agtPwtComm < 0 ? agtPwtComm : 0.0)
					- (dPwtCommRate + pwtCommVar1);
			if (diffSaleComm >= 0 && diffPwtComm >= 0) {
				PreparedStatement pstmt1 = conn
						.prepareStatement("select retailer_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_se_agent_retailer_sale_pwt_comm_variance where game_id=(select game_id from st_se_game_master where game_nbr="
								+ game[0]
								+ " ) and retailer_org_id="
								+ orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				System.out.println("query = " + pstmt1);
				if (rs1.next()) {
					System.out.println("inside pstmt1---11111111");
					double oldPwtCommVar = rs1.getDouble("pwt_comm_variance");
					double oldSaleCommVar = rs1.getDouble("sale_comm_variance");
					if (oldPwtCommVar == pwtCommVar1
							&& saleCommVar1 == oldSaleCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("game_id"));
					commVarianceBean.setSaleCommVar(oldSaleCommVar);
					commVarianceBean.setDefaultSaleCommRate(rs1
							.getDouble("default_sale_comm_rate"));
					commVarianceBean.setPwtCommVar(oldPwtCommVar);
					commVarianceBean.setDefaultPwtCommRate(rs1
							.getDouble("default_pwt_comm_rate"));
					System.out.println("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();

				if (commVarianceBean != null) {
					System.out.println("555555555555555");
					PreparedStatement pstmt5 = conn
							.prepareStatement("delete from st_se_agent_retailer_sale_pwt_comm_variance  where game_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					System.out.println("query = " + pstmt5);
					System.out
							.println("Delete entry from st_se_agent_retailer_sale_pwt_comm_variance  table updated rows === "
									+ rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null
						&& saleCommVar1 != commVarianceBean.getSaleCommVar()) {
					System.out.println("3333333333333333333");
					PreparedStatement pstmt3 = conn
							.prepareStatement("insert into st_se_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getSaleCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					System.out.println("query = " + pstmt3);
					System.out
							.println("sale comm var history table updated rows === "
									+ rows);
					pstmt3.close();
				}

				if (commVarianceBean != null
						&& pwtCommVar1 != commVarianceBean.getPwtCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn
							.prepareStatement("insert into st_se_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getPwtCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out
							.println("PWT comm var history table updated rows === "
									+ rows);
					pstmt4.close();
				}

				System.out.println(saleCommVar1 + " **saleCommVar1** "
						+ pwtCommVar1);
				System.out.println("saleCommVar1!=0.0 ||  pwtCommVar1!=0.0"
						+ (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0));
				if (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0) {
					System.out.println("66666666666666666666666666");
					PreparedStatement pstmt6 = conn
							.prepareStatement(" select game_id from st_se_game_master where game_nbr=?   ");
				//	pstmt6.setString(1, agtOrgName.trim());
					pstmt6.setInt(1, Integer.parseInt(game[0]));
					//pstmt6.setString(3, game[1]);
					ResultSet rs6 = pstmt6.executeQuery();
					System.out.println("query = " + pstmt6);
					int gameId = 0;
					//int retOrgid = 0;
					if (rs6.next()) {
						gameId = rs6.getInt("game_id");
					///	retOrgid = rs6.getInt("organization_id");
					}
					if (gameId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn
								.prepareStatement("insert into st_se_agent_retailer_sale_pwt_comm_variance  values (? , ? , ? ,?,?, ?,?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, gameId);
						pstmt5.setDouble(4, saleCommVar1);
						pstmt5.setDouble(5, dSaleCommRate);
						pstmt5.setDouble(6, pwtCommVar1);
						pstmt5.setDouble(7, dPwtCommRate);
						int rows = pstmt5.executeUpdate();
						System.out.println("query = " + pstmt5);
						System.out
								.println("insert entries st_se_agent_retailer_sale_pwt_comm_variance  table updated rows === "
										+ rows);
						pstmt5.close();
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("3333333333333333333");
						PreparedStatement pstmt3 = conn
								.prepareStatement("insert into st_se_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt3.setInt(1, userOrgId);
						pstmt3.setInt(2, orgId);
						pstmt3.setInt(3, gameId);
						pstmt3.setDouble(4, 0.0);
						pstmt3.setTimestamp(5, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						int rows = pstmt3.executeUpdate();
						System.out.println("query = " + pstmt3);
						System.out
								.println("sale comm var history table updated rows === "
										+ rows);
						pstmt3.close();
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("44444444444444444444444");
						PreparedStatement pstmt4 = conn
								.prepareStatement("insert into st_se_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt4.setInt(1, userOrgId);
						pstmt4.setInt(2, orgId);
						pstmt4.setInt(3, gameId);
						pstmt4.setDouble(4, 0.0);
						pstmt4.setTimestamp(5, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						int rows = pstmt4.executeUpdate();
						System.out.println("query = " + pstmt4);
						System.out
								.println("PWT comm var history table updated rows === "
										+ rows);
						pstmt4.close();
					}

				}
			} else {
				return "AGENT_DIFF_GREATER;"
						+ (dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0)))
						+ ";"
						+ (dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0));
			}
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rollbacked");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);

		} finally {

			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return "SUCCESS";
	}

	public String submitUpdatedValueDg(String agtOrgName, String gname,
			String defSaleCommRate, String saleCommVar, String defPwtCommRate,
			String pwtCommVar, int userOrgId,String countryDep) throws LMSException {

		String game[] = gname.split("-");
		double dSaleCommRate = Double.parseDouble(defSaleCommRate.trim());
		double saleCommVar1 = Double.parseDouble(saleCommVar.trim());
		double dPwtCommRate = Double.parseDouble(defPwtCommRate.trim());
		double pwtCommVar1 = Double.parseDouble(pwtCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtSaleComm = 0.0;
		double agtSaleComm = 0.0;
		double dAgtPwtComm = 0.0;
		double agtPwtComm = 0.0;
		try {
			int orgId = Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn
					.prepareStatement("select agent_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_dg_bo_agent_sale_pwt_comm_variance  gm where game_id="
							+ game[0]
							+ " and agent_org_id=(select parent_id from st_lms_organization_master where organization_id="
							+ orgId + ")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				dAgtSaleComm = rs0.getDouble("default_sale_comm_rate");
				agtSaleComm = rs0.getDouble("sale_comm_variance");
				dAgtPwtComm = rs0.getDouble("default_pwt_comm_rate");
				agtPwtComm = rs0.getDouble("pwt_comm_variance");
			} else {
				PreparedStatement pstmtGameMaster = conn
						.prepareStatement("select agent_sale_comm_rate,agent_pwt_comm_rate from st_dg_game_master where game_id="
								+ game[0]);
				ResultSet rsGameMaster = pstmtGameMaster.executeQuery();
				System.out.println("query = " + pstmtGameMaster);
				if (rsGameMaster.next()) {
					dAgtSaleComm = rsGameMaster
							.getDouble("agent_sale_comm_rate");
					dAgtPwtComm = rsGameMaster.getDouble("agent_pwt_comm_rate");
				}
			}
			double diffSaleComm = dAgtSaleComm
					+ (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0))
					- (dSaleCommRate + saleCommVar1);
			double diffPwtComm = dAgtPwtComm
					+ (agtPwtComm < 0 ? agtPwtComm : 0.0)
					- (dPwtCommRate + pwtCommVar1);
			if (diffSaleComm >= 0 && diffPwtComm >= 0) {

				PreparedStatement pstmt1 = conn
						.prepareStatement("select retailer_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_dg_agent_retailer_sale_pwt_comm_variance where game_id="
								+ game[0]
								+ " and retailer_org_id="
								+ orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				System.out.println("query = " + pstmt1);
				if (rs1.next()) {
					System.out.println("inside pstmt1---11111111");
					double oldPwtCommVar = rs1.getDouble("pwt_comm_variance");
					double oldSaleCommVar = rs1.getDouble("sale_comm_variance");
					if (oldPwtCommVar == pwtCommVar1
							&& saleCommVar1 == oldSaleCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("game_id"));
					commVarianceBean.setSaleCommVar(oldSaleCommVar);
					commVarianceBean.setDefaultSaleCommRate(rs1
							.getDouble("default_sale_comm_rate"));
					commVarianceBean.setPwtCommVar(oldPwtCommVar);
					commVarianceBean.setDefaultPwtCommRate(rs1
							.getDouble("default_pwt_comm_rate"));
					System.out.println("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();

				if (commVarianceBean != null) {
					System.out.println("555555555555555");
					PreparedStatement pstmt5 = conn
							.prepareStatement("delete from st_dg_agent_retailer_sale_pwt_comm_variance  where game_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					System.out.println("query = " + pstmt5);
					System.out
							.println("Delete entry from st_dg_agent_retailer_sale_pwt_comm_variance  table updated rows === "
									+ rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null
						&& saleCommVar1 != commVarianceBean.getSaleCommVar()) {
					System.out.println("3333333333333333333");
					PreparedStatement pstmt3 = conn
							.prepareStatement("insert into st_dg_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getSaleCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					System.out.println("query = " + pstmt3);
					System.out
							.println("sale comm var history table updated rows === "
									+ rows);
					pstmt3.close();
				}

				if (commVarianceBean != null
						&& pwtCommVar1 != commVarianceBean.getPwtCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn
							.prepareStatement("insert into st_dg_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getPwtCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out
							.println("PWT comm var history table updated rows === "
									+ rows);
					pstmt4.close();
				}

				System.out.println(saleCommVar1 + " **saleCommVar1** "
						+ pwtCommVar1);
				System.out.println("saleCommVar1!=0.0 ||  pwtCommVar1!=0.0"
						+ (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0));
				
				System.out.println("66666666666666666666666666");
			/*	PreparedStatement pstmt6 = conn
						.prepareStatement(" select game_id from st_dg_game_master where game_id=?  ");
				//pstmt6.setString(1, agtOrgName.trim());
				pstmt6.setInt(1, Integer.parseInt(game[0]));
				//pstmt6.setString(3, game[1]);
				ResultSet rs6 = pstmt6.executeQuery();
				System.out.println("query = " + pstmt6);*/
				int gameId = Integer.parseInt(game[0]);
				// int retOrgid = 0;
			/*	if (rs6.next()) {
					gameId = rs6.getInt("game_id");
				//	retOrgid = rs6.getInt("organization_id");
				}	*/			
				
				if (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0) {					
					if (gameId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn
								.prepareStatement("insert into st_dg_agent_retailer_sale_pwt_comm_variance  values (? , ? , ? ,?,?, ?,?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, gameId);
						pstmt5.setDouble(4, saleCommVar1);
						pstmt5.setDouble(5, dSaleCommRate);
						pstmt5.setDouble(6, pwtCommVar1);
						pstmt5.setDouble(7, dPwtCommRate);
						int rows = pstmt5.executeUpdate();
						System.out.println("query = " + pstmt5);
						System.out
								.println("insert entries st_dg_agent_retailer_sale_pwt_comm_variance  table updated rows === "
										+ rows);
						pstmt5.close();
						
					/*	
						//here update the updated comm rate in the context
					//	System.out.println("var in context before: " + Util.getSaleCommVariance(retOrgid, gameId));
						double retUpdatedCommVar = CommonFunctionsHelper.fetchDGCommOfOrganization(gameId, retOrgid, "SALE", "RETAILER", conn);
						Util.updateSaleCommVariance(retOrgid, gameId,retUpdatedCommVar);
						System.out.println("var in context after updation: " + Util.getSaleCommVariance(retOrgid, gameId));
					  //updated successfully
*/						
						
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("3333333333333333333");
						PreparedStatement pstmt3 = conn
								.prepareStatement("insert into st_dg_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt3.setInt(1, userOrgId);
						pstmt3.setInt(2, orgId);
						pstmt3.setInt(3, gameId);
						pstmt3.setDouble(4, 0.0);
						pstmt3.setTimestamp(5, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						int rows = pstmt3.executeUpdate();
						System.out.println("query = " + pstmt3);
						System.out
								.println("sale comm var history table updated rows === "
										+ rows);
						pstmt3.close();
						
						
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("44444444444444444444444");
						PreparedStatement pstmt4 = conn
								.prepareStatement("insert into st_dg_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt4.setInt(1, userOrgId);
						pstmt4.setInt(2, orgId);
						pstmt4.setInt(3, gameId);
						pstmt4.setDouble(4, 0.0);
						pstmt4.setTimestamp(5, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						int rows = pstmt4.executeUpdate();
						System.out.println("query = " + pstmt4);
						System.out
								.println("PWT comm var history table updated rows === "
										+ rows);
						pstmt4.close();
					}

				}				
				
				//here update the updated comm rate in the context
			   //System.out.println("var in context before: " + Util.getSaleCommVariance(retOrgid, gameId));
				double retUpdatedCommVar = CommonFunctionsHelper.fetchDGCommOfOrganization(gameId, orgId, "SALE", "RETAILER", conn);
				Util.updateSaleCommVariance(orgId, gameId,retUpdatedCommVar);
				System.out.println("var in context after updation: " + Util.getSaleCommVariance(orgId, gameId));
			   //updated successfully
				
			} else {
				return "AGENT_DIFF_GREATER;"
						+ (dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0)))
						+ ";"
						+ (dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0));
			}
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rollbacked");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);

		} finally {

			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return "SUCCESS";
	}
	
	
	public String submitUpdatedValueIW(String agtOrgName, String gname, String defSaleCommRate, String saleCommVar, String defPwtCommRate, String pwtCommVar, int userOrgId, String countryDep) throws LMSException {
		String game[] = gname.split("-");
		double dSaleCommRate = Double.parseDouble(defSaleCommRate.trim());
		double saleCommVar1 = Double.parseDouble(saleCommVar.trim());
		double dPwtCommRate = Double.parseDouble(defPwtCommRate.trim());
		double pwtCommVar1 = Double.parseDouble(pwtCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtSaleComm = 0.0;
		double agtSaleComm = 0.0;
		double dAgtPwtComm = 0.0;
		double agtPwtComm = 0.0;
		try {
			int orgId = Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn.prepareStatement("select agent_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_iw_bo_agent_sale_pwt_comm_variance gm where game_id=" + game[0] + " and agent_org_id=(select parent_id from st_lms_organization_master where organization_id=" + orgId + ")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				dAgtSaleComm = rs0.getDouble("default_sale_comm_rate");
				agtSaleComm = rs0.getDouble("sale_comm_variance");
				dAgtPwtComm = rs0.getDouble("default_pwt_comm_rate");
				agtPwtComm = rs0.getDouble("pwt_comm_variance");
			} else {
				PreparedStatement pstmtGameMaster = conn.prepareStatement("select agent_sale_comm_rate,agent_pwt_comm_rate from st_iw_game_master where game_id=" + game[0]);
				ResultSet rsGameMaster = pstmtGameMaster.executeQuery();
				System.out.println("query = " + pstmtGameMaster);
				if (rsGameMaster.next()) {
					dAgtSaleComm = rsGameMaster.getDouble("agent_sale_comm_rate");
					dAgtPwtComm = rsGameMaster.getDouble("agent_pwt_comm_rate");
				}
			}
			double diffSaleComm = dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep) ? agtSaleComm : 0.0)) - (dSaleCommRate + saleCommVar1);
			double diffPwtComm = dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0) - (dPwtCommRate + pwtCommVar1);
			if (diffSaleComm >= 0 && diffPwtComm >= 0) {
				PreparedStatement pstmt1 = conn.prepareStatement("select retailer_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_iw_agent_retailer_sale_pwt_comm_variance where game_id=" + game[0] + " and retailer_org_id=" + orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				System.out.println("query = " + pstmt1);
				if (rs1.next()) {
					System.out.println("inside pstmt1---11111111");
					double oldPwtCommVar = rs1.getDouble("pwt_comm_variance");
					double oldSaleCommVar = rs1.getDouble("sale_comm_variance");
					if (oldPwtCommVar == pwtCommVar1 && saleCommVar1 == oldSaleCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("game_id"));
					commVarianceBean.setSaleCommVar(oldSaleCommVar);
					commVarianceBean.setDefaultSaleCommRate(rs1.getDouble("default_sale_comm_rate"));
					commVarianceBean.setPwtCommVar(oldPwtCommVar);
					commVarianceBean.setDefaultPwtCommRate(rs1.getDouble("default_pwt_comm_rate"));
					System.out.println("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();

				if (commVarianceBean != null) {
					System.out.println("555555555555555");
					PreparedStatement pstmt5 = conn.prepareStatement("delete from st_iw_agent_retailer_sale_pwt_comm_variance  where game_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					System.out.println("query = " + pstmt5);
					System.out.println("Delete entry from st_iw_agent_retailer_sale_pwt_comm_variance  table updated rows === " + rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null && saleCommVar1 != commVarianceBean.getSaleCommVar()) {
					System.out.println("3333333333333333333");
					PreparedStatement pstmt3 = conn.prepareStatement("insert into st_iw_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getSaleCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					System.out.println("query = " + pstmt3);
					System.out.println("sale comm var history table updated rows === " + rows);
					pstmt3.close();
				}

				if (commVarianceBean != null && pwtCommVar1 != commVarianceBean.getPwtCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn.prepareStatement("insert into st_iw_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getPwtCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out.println("PWT comm var history table updated rows === " + rows);
					pstmt4.close();
				}

				System.out.println(saleCommVar1 + " **saleCommVar1** " + pwtCommVar1);
				System.out.println("saleCommVar1!=0.0 ||  pwtCommVar1!=0.0" + (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0));

				System.out.println("66666666666666666666666666");
				/*
				 * PreparedStatement pstmt6 = conn .prepareStatement(
				 * " select game_id from st_dg_game_master where game_id=?  ");
				 * //pstmt6.setString(1, agtOrgName.trim()); pstmt6.setInt(1,
				 * Integer.parseInt(game[0])); //pstmt6.setString(3, game[1]);
				 * ResultSet rs6 = pstmt6.executeQuery();
				 * System.out.println("query = " + pstmt6);
				 */
				int gameId = Integer.parseInt(game[0]);
				// int retOrgid = 0;
				/*
				 * if (rs6.next()) { gameId = rs6.getInt("game_id"); // retOrgid
				 * = rs6.getInt("organization_id"); }
				 */

				if (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0) {
					if (gameId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn.prepareStatement("insert into st_iw_agent_retailer_sale_pwt_comm_variance  values (?, ?, ?, ?, ?, ?,?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, gameId);
						pstmt5.setDouble(4, saleCommVar1);
						pstmt5.setDouble(5, dSaleCommRate);
						pstmt5.setDouble(6, pwtCommVar1);
						pstmt5.setDouble(7, dPwtCommRate);
						int rows = pstmt5.executeUpdate();
						System.out.println("query = " + pstmt5);
						System.out.println("insert entries st_iw_agent_retailer_sale_pwt_comm_variance table updated rows === " + rows);
						pstmt5.close();
						/*
						 * //here update the updated comm rate in the context //
						 * System.out.println("var in context before: " +
						 * Util.getSaleCommVariance(retOrgid, gameId)); double
						 * retUpdatedCommVar =
						 * CommonFunctionsHelper.fetchDGCommOfOrganization
						 * (gameId, retOrgid, "SALE", "RETAILER", conn);
						 * Util.updateSaleCommVariance(retOrgid,
						 * gameId,retUpdatedCommVar);
						 * System.out.println("var in context after updation: "
						 * + Util.getSaleCommVariance(retOrgid, gameId));
						 * //updated successfully
						 */
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("3333333333333333333");
						PreparedStatement pstmt3 = conn.prepareStatement("insert into st_iw_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt3.setInt(1, userOrgId);
						pstmt3.setInt(2, orgId);
						pstmt3.setInt(3, gameId);
						pstmt3.setDouble(4, 0.0);
						pstmt3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
						int rows = pstmt3.executeUpdate();
						System.out.println("query = " + pstmt3);
						System.out.println("sale comm var history table updated rows === " + rows);
						pstmt3.close();
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("44444444444444444444444");
						PreparedStatement pstmt4 = conn.prepareStatement("insert into st_iw_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt4.setInt(1, userOrgId);
						pstmt4.setInt(2, orgId);
						pstmt4.setInt(3, gameId);
						pstmt4.setDouble(4, 0.0);
						pstmt4.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
						int rows = pstmt4.executeUpdate();
						System.out.println("query = " + pstmt4);
						System.out.println("PWT comm var history table updated rows === " + rows);
						pstmt4.close();
					}
				}

				// here update the updated comm rate in the context
				// System.out.println("var in context before: " +
				// Util.getSaleCommVariance(retOrgid, gameId));
				double retUpdatedCommVar = CommonFunctionsHelper.fetchIWCommOfOrganization(gameId, orgId, "SALE", "RETAILER", conn);
				Util.updateSaleCommVarianceIW(orgId, gameId, retUpdatedCommVar);
				System.out.println("var in context after updation: " + Util.getSaleCommVariance(orgId, gameId));
				// updated successfully
			} else {
				return "AGENT_DIFF_GREATER;" + (dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep) ? agtSaleComm : 0.0))) + ";" + (dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0));
			}
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rollbacked");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "SUCCESS";
	}
	
	public String submitUpdatedValueVS(String agtOrgName, String gname, String defSaleCommRate, String saleCommVar, String defPwtCommRate, String pwtCommVar, int userOrgId, String countryDep) throws LMSException {
		String game[] = gname.split("-");
		double dSaleCommRate = Double.parseDouble(defSaleCommRate.trim());
		double saleCommVar1 = Double.parseDouble(saleCommVar.trim());
		double dPwtCommRate = Double.parseDouble(defPwtCommRate.trim());
		double pwtCommVar1 = Double.parseDouble(pwtCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtSaleComm = 0.0;
		double agtSaleComm = 0.0;
		double dAgtPwtComm = 0.0;
		double agtPwtComm = 0.0;
		try {
			int orgId = Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn.prepareStatement("select agent_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_vs_bo_agent_sale_pwt_comm_variance gm where game_id=" + game[0] + " and agent_org_id=(select parent_id from st_lms_organization_master where organization_id=" + orgId + ")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				dAgtSaleComm = rs0.getDouble("default_sale_comm_rate");
				agtSaleComm = rs0.getDouble("sale_comm_variance");
				dAgtPwtComm = rs0.getDouble("default_pwt_comm_rate");
				agtPwtComm = rs0.getDouble("pwt_comm_variance");
			} else {
				PreparedStatement pstmtGameMaster = conn.prepareStatement("select agent_sale_comm_rate,agent_pwt_comm_rate from st_vs_game_master where game_id=" + game[0]);
				ResultSet rsGameMaster = pstmtGameMaster.executeQuery();
				System.out.println("query = " + pstmtGameMaster);
				if (rsGameMaster.next()) {
					dAgtSaleComm = rsGameMaster.getDouble("agent_sale_comm_rate");
					dAgtPwtComm = rsGameMaster.getDouble("agent_pwt_comm_rate");
				}
			}
			double diffSaleComm = dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep) ? agtSaleComm : 0.0)) - (dSaleCommRate + saleCommVar1);
			double diffPwtComm = dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0) - (dPwtCommRate + pwtCommVar1);
			if (diffSaleComm >= 0 && diffPwtComm >= 0) {
				PreparedStatement pstmt1 = conn.prepareStatement("select retailer_org_id, game_id, sale_comm_variance, default_sale_comm_rate, pwt_comm_variance, default_pwt_comm_rate from st_vs_agent_retailer_sale_pwt_comm_variance where game_id=" + game[0] + " and retailer_org_id=" + orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				System.out.println("query = " + pstmt1);
				if (rs1.next()) {
					System.out.println("inside pstmt1---11111111");
					double oldPwtCommVar = rs1.getDouble("pwt_comm_variance");
					double oldSaleCommVar = rs1.getDouble("sale_comm_variance");
					if (oldPwtCommVar == pwtCommVar1 && saleCommVar1 == oldSaleCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("game_id"));
					commVarianceBean.setSaleCommVar(oldSaleCommVar);
					commVarianceBean.setDefaultSaleCommRate(rs1.getDouble("default_sale_comm_rate"));
					commVarianceBean.setPwtCommVar(oldPwtCommVar);
					commVarianceBean.setDefaultPwtCommRate(rs1.getDouble("default_pwt_comm_rate"));
					System.out.println("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();

				if (commVarianceBean != null) {
					System.out.println("555555555555555");
					PreparedStatement pstmt5 = conn.prepareStatement("delete from st_vs_agent_retailer_sale_pwt_comm_variance  where game_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					System.out.println("query = " + pstmt5);
					System.out.println("Delete entry from st_iw_agent_retailer_sale_pwt_comm_variance  table updated rows === " + rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null && saleCommVar1 != commVarianceBean.getSaleCommVar()) {
					System.out.println("3333333333333333333");
					PreparedStatement pstmt3 = conn.prepareStatement("insert into st_vs_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getSaleCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					System.out.println("query = " + pstmt3);
					System.out.println("sale comm var history table updated rows === " + rows);
					pstmt3.close();
				}

				if (commVarianceBean != null && pwtCommVar1 != commVarianceBean.getPwtCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn.prepareStatement("insert into st_vs_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getPwtCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out.println("PWT comm var history table updated rows === " + rows);
					pstmt4.close();
				}

				System.out.println(saleCommVar1 + " **saleCommVar1** " + pwtCommVar1);
				System.out.println("saleCommVar1!=0.0 ||  pwtCommVar1!=0.0" + (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0));

				System.out.println("66666666666666666666666666");
				/*
				 * PreparedStatement pstmt6 = conn .prepareStatement(
				 * " select game_id from st_dg_game_master where game_id=?  ");
				 * //pstmt6.setString(1, agtOrgName.trim()); pstmt6.setInt(1,
				 * Integer.parseInt(game[0])); //pstmt6.setString(3, game[1]);
				 * ResultSet rs6 = pstmt6.executeQuery();
				 * System.out.println("query = " + pstmt6);
				 */
				int gameId = Integer.parseInt(game[0]);
				// int retOrgid = 0;
				/*
				 * if (rs6.next()) { gameId = rs6.getInt("game_id"); // retOrgid
				 * = rs6.getInt("organization_id"); }
				 */

				if (saleCommVar1 != 0.0 || pwtCommVar1 != 0.0) {
					if (gameId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn.prepareStatement("insert into st_vs_agent_retailer_sale_pwt_comm_variance  values (?, ?, ?, ?, ?, ?,?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, gameId);
						pstmt5.setDouble(4, saleCommVar1);
						pstmt5.setDouble(5, dSaleCommRate);
						pstmt5.setDouble(6, pwtCommVar1);
						pstmt5.setDouble(7, dPwtCommRate);
						int rows = pstmt5.executeUpdate();
						System.out.println("query = " + pstmt5);
						System.out.println("insert entries st_vs_agent_retailer_sale_pwt_comm_variance table updated rows === " + rows);
						pstmt5.close();
						/*
						 * //here update the updated comm rate in the context //
						 * System.out.println("var in context before: " +
						 * Util.getSaleCommVariance(retOrgid, gameId)); double
						 * retUpdatedCommVar =
						 * CommonFunctionsHelper.fetchDGCommOfOrganization
						 * (gameId, retOrgid, "SALE", "RETAILER", conn);
						 * Util.updateSaleCommVariance(retOrgid,
						 * gameId,retUpdatedCommVar);
						 * System.out.println("var in context after updation: "
						 * + Util.getSaleCommVariance(retOrgid, gameId));
						 * //updated successfully
						 */
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("3333333333333333333");
						PreparedStatement pstmt3 = conn.prepareStatement("insert into st_vs_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt3.setInt(1, userOrgId);
						pstmt3.setInt(2, orgId);
						pstmt3.setInt(3, gameId);
						pstmt3.setDouble(4, 0.0);
						pstmt3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
						int rows = pstmt3.executeUpdate();
						System.out.println("query = " + pstmt3);
						System.out.println("sale comm var history table updated rows === " + rows);
						pstmt3.close();
					}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						System.out.println("44444444444444444444444");
						PreparedStatement pstmt4 = conn.prepareStatement("insert into st_vs_agent_retailer_pwt_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt4.setInt(1, userOrgId);
						pstmt4.setInt(2, orgId);
						pstmt4.setInt(3, gameId);
						pstmt4.setDouble(4, 0.0);
						pstmt4.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
						int rows = pstmt4.executeUpdate();
						System.out.println("query = " + pstmt4);
						System.out.println("PWT comm var history table updated rows === " + rows);
						pstmt4.close();
					}
				}

				// here update the updated comm rate in the context
				// System.out.println("var in context before: " +
				// Util.getSaleCommVariance(retOrgid, gameId));
				double retUpdatedCommVar = CommonFunctionsHelper.fetchVSCommOfOrganization(gameId, orgId, "SALE", "RETAILER", conn);
				Util.updateSaleCommVarianceVS(orgId, gameId, retUpdatedCommVar);
				System.out.println("var in context after updation: " + Util.getVSSaleCommVariance(orgId, gameId));
				// updated successfully
			} else {
				return "AGENT_DIFF_GREATER;" + (dAgtSaleComm + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep) ? agtSaleComm : 0.0))) + ";" + (dAgtPwtComm + (agtPwtComm < 0 ? agtPwtComm : 0.0));
			}
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rollbacked");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "SUCCESS";
	}
	
	public String submitUpdatedValueCS(String agtOrgName, String gname,
			String defSaleCommRate, String saleCommVar, int userOrgId,String countryDep) throws LMSException {

		String game[] = gname.split("_");
		double dSaleCommRate = Double.parseDouble(defSaleCommRate.trim());
		double saleCommVar1 = Double.parseDouble(saleCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtSaleComm = 0.0;
		double agtSaleComm = 0.0;
		double dAgtPwtComm = 0.0;
		double agtPwtComm = 0.0;
		try {
			int orgId = Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn
					.prepareStatement("select agent_org_id, product_id, sale_comm_variance, default_sale_comm_rate from st_cs_bo_agent_sale_comm_variance  gm where product_id="
							+ game[0]
							+ " and agent_org_id=(select parent_id from st_lms_organization_master where organization_id="
							+ orgId + ")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				dAgtSaleComm = rs0.getDouble("default_sale_comm_rate");
				agtSaleComm = rs0.getDouble("sale_comm_variance");
			} else {
				PreparedStatement pstmtGameMaster = conn
						.prepareStatement("select agent_comm from st_cs_product_master where product_id="
								+ game[0]);
				ResultSet rsGameMaster = pstmtGameMaster.executeQuery();
				System.out.println("query = " + pstmtGameMaster);
				if (rsGameMaster.next()) {
					dAgtSaleComm = rsGameMaster
							.getDouble("agent_comm");
				}
			}
			double diffSaleComm = dAgtSaleComm
					+ (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0))
					- (dSaleCommRate + saleCommVar1);
			if (diffSaleComm >= 0) {

				PreparedStatement pstmt1 = conn
						.prepareStatement("select retailer_org_id, product_id, sale_comm_variance, default_sale_comm_rate from st_cs_agent_retailer_sale_comm_variance where product_id="
								+ game[0]
								+ " and retailer_org_id="
								+ orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				logger.debug("query = " + pstmt1);
				if (rs1.next()) {
					logger.debug("inside pstmt1---11111111");
					double oldSaleCommVar = rs1.getDouble("sale_comm_variance");
					if (saleCommVar1 == oldSaleCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("product_id"));
					commVarianceBean.setSaleCommVar(oldSaleCommVar);
					commVarianceBean.setDefaultSaleCommRate(rs1
							.getDouble("default_sale_comm_rate"));
					logger.debug("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();

				if (commVarianceBean != null) {
					logger.debug("555555555555555");
					PreparedStatement pstmt5 = conn
							.prepareStatement("delete from st_cs_agent_retailer_sale_comm_variance  where product_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					logger.debug("query = " + pstmt5);
					logger.debug("Delete entry from st_dg_agent_retailer_sale_pwt_comm_variance  table updated rows === "
									+ rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null
						&& saleCommVar1 != commVarianceBean.getSaleCommVar()) {
					logger.debug("3333333333333333333");
					PreparedStatement pstmt3 = conn
							.prepareStatement("insert into st_cs_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getSaleCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					logger.debug("query = " + pstmt3);
					logger.debug("sale comm var history table updated rows === "
									+ rows);
					pstmt3.close();
				}

				logger.debug(saleCommVar1 + " **saleCommVar1** ");
				logger.debug("saleCommVar1!=0.0"
						+ (saleCommVar1 != 0.0));
				
				logger.debug("66666666666666666666666666");
				PreparedStatement pstmt6 = conn
						.prepareStatement(" select product_id from st_cs_product_master where product_id=? ");
				//	pstmt6.setString(1, agtOrgName.trim());
				pstmt6.setInt(1, Integer.parseInt(game[0]));
				ResultSet rs6 = pstmt6.executeQuery();
				logger.debug("query = " + pstmt6);
				int gameId = 0;
			//	int retOrgid = 0;
				if (rs6.next()) {
					gameId = rs6.getInt("product_id");
				//	retOrgid = rs6.getInt("organization_id");
				}				
				
				if (saleCommVar1 != 0.0) {					
					if (gameId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn
								.prepareStatement("insert into st_cs_agent_retailer_sale_comm_variance  values (? , ? , ? ,?, ?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, gameId);
						pstmt5.setDouble(4, saleCommVar1);
						pstmt5.setDouble(5, dSaleCommRate);
						int rows = pstmt5.executeUpdate();
						System.out.println("query = " + pstmt5);
						System.out
								.println("insert entries st_cs_agent_retailer_sale_comm_variance  table updated rows === "
										+ rows);
						pstmt5.close();
						}

					if (commVarianceBean == null && gameId > 0 && orgId > 0) {
						logger.debug("3333333333333333333");
						PreparedStatement pstmt3 = conn
								.prepareStatement("insert into st_cs_agent_retailer_sale_comm_variance_history values(?, ?, ?, ?,?)");
						pstmt3.setInt(1, userOrgId);
						pstmt3.setInt(2, orgId);
						pstmt3.setInt(3, gameId);
						pstmt3.setDouble(4, 0.0);
						pstmt3.setTimestamp(5, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						int rows = pstmt3.executeUpdate();
						logger.debug("query = " + pstmt3);
						logger.debug("sale comm var history table updated rows === "
										+ rows);
						pstmt3.close();
					}
				}				
				
				//here update the updated comm rate in the context
			   //System.out.println("var in context before: " + Util.getSaleCommVariance(retOrgid, gameId));
				
			} else {
				return "AGENT_DIFF_GREATER;"
						+ (dAgtSaleComm - dSaleCommRate + (agtSaleComm < 0 ? agtSaleComm : ("kenya".equalsIgnoreCase(countryDep)?agtSaleComm:0.0)));
			}
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rollbacked");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);

		} finally {

			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return "SUCCESS";
		
	}
	
	public String submitUpdatedValueOLA(String agtOrgName, String wname,
			String defWidCommRate, String widCommVar, String defDepCommRate,
			String depCommVar, String defNetGCommRate, 
			String netGCommVar,int userOrgId,String countryDep) throws LMSException{


		String wallet[] = wname.split("-");
		double dWidCommRate = Double.parseDouble(defWidCommRate.trim());
		double widCommVar1 = Double.parseDouble(widCommVar.trim());
		double dDepCommRate = Double.parseDouble(defDepCommRate.trim());
		double depCommVar1 = Double.parseDouble(depCommVar.trim());
		double dNetGCommRate = Double.parseDouble(defNetGCommRate.trim());
		double netGCommVar1 = Double.parseDouble(netGCommVar.trim());
		UpdateSalePwtCommVarBean commVarianceBean = null;
		Connection conn = DBConnect.getConnection();
		boolean flagforZero = false;
		double dAgtWidComm = 0.0;
		double agtWidComm = 0.0;
		double dAgtDepComm = 0.0;
		double agtDepComm = 0.0;
		double dAgtNetGComm = 0.0;
		double agtNetGComm = 0.0;
		try {
			int orgId =Integer.parseInt(agtOrgName);
			conn.setAutoCommit(false);
			PreparedStatement pstmt0 = conn
					.prepareStatement("select agent_org_id, wallet_id, deposit_comm_variance, default_deposit_comm_rate, withdrawl_comm_variance, default_withdrawl_comm_rate, net_gaming_comm_variance, default_net_gaming_comm_rate from st_ola_bo_agent_comm_variance where wallet_id = "
										+ wallet[0]
										+ " and agent_org_id=(select organization_id from st_lms_organization_master where organization_id="
										+ orgId +")");
			ResultSet rs0 = pstmt0.executeQuery();
			System.out.println("query = " + pstmt0);
			if (rs0.next()) {
				System.out.println("inside pstmt1---11111111");
				dAgtDepComm = rs0.getDouble("default_deposit_comm_rate");
				agtDepComm = rs0.getDouble("deposit_comm_variance");
				dAgtWidComm = rs0.getDouble("default_withdrawl_comm_rate");
				agtWidComm = rs0.getDouble("withdrawl_comm_variance");
				dAgtNetGComm = rs0.getDouble("default_net_gaming_comm_rate");
				agtNetGComm = rs0.getDouble("net_gaming_comm_variance");
			}else{
				PreparedStatement pstmtWalletMaster = conn
						.prepareStatement("select agt_withdrawl_comm, agt_deposit_comm, agt_net_gaming_comm from st_ola_wallet_master where wallet_id="
								+ wallet[0]);
				ResultSet rsWalletMaster = pstmtWalletMaster.executeQuery();
				System.out.println("query = " + pstmtWalletMaster);
				if (rsWalletMaster.next()) {
					dAgtWidComm = rsWalletMaster
							.getDouble("agt_withdrawl_comm");
					dAgtDepComm = rsWalletMaster.getDouble("agt_deposit_comm");
					dAgtNetGComm = rsWalletMaster.getDouble("agt_net_gaming_comm");
				}
			}
			
			double diffWidComm = dAgtWidComm
					+ (agtWidComm < 0 ? agtWidComm : 0.0)
					- (dWidCommRate + widCommVar1);
			double diffDepComm = dAgtDepComm
					+ (agtDepComm < 0 ? agtDepComm : 0.0)
					- (dDepCommRate + depCommVar1);
			double diffNetGComm = dAgtNetGComm
				+ (agtNetGComm < 0 ? agtNetGComm : 0.0)
				- (dNetGCommRate + netGCommVar1);
			if (diffWidComm >= 0 && diffDepComm >= 0 && diffNetGComm >= 0) {
				
				PreparedStatement pstmt1 = conn
				.prepareStatement("select retailer_org_id, wallet_id, deposit_comm_variance, default_deposit_comm_rate, withdrawl_comm_variance, default_withdrawl_comm_rate, net_gaming_comm_variance, default_net_gaming_comm_rate from st_ola_agent_retailer_comm_variance where wallet_id=(select wallet_id from st_ola_wallet_master where wallet_id="
						+ wallet[0]
						+ " ) and retailer_org_id="
						+ orgId + "");
				ResultSet rs1 = pstmt1.executeQuery();
				System.out.println("query = " + pstmt1);
				if (rs1.next()) {
					System.out.println("inside pstmt1---11111111");
					double oldWidCommVar = rs1.getDouble("withdrawl_comm_variance");
					double oldDepCommVar = rs1.getDouble("deposit_comm_variance");
					double oldNetGCommVar = rs1.getDouble("net_gaming_comm_variance");
					if (oldWidCommVar == widCommVar1
							&& depCommVar1 == oldDepCommVar 
							&& netGCommVar1 == oldNetGCommVar) {
						return "SAME_VALUES";
					}

					commVarianceBean = new UpdateSalePwtCommVarBean();
					commVarianceBean.setRetOrgId(rs1.getInt("retailer_org_id"));
					commVarianceBean.setGameId(rs1.getInt("wallet_id"));
					commVarianceBean.setWidCommVar(oldWidCommVar);
					commVarianceBean.setDefaultWidCommRate(rs1
							.getDouble("default_withdrawl_comm_rate"));
					commVarianceBean.setDepCommVar(oldDepCommVar);
					commVarianceBean.setDefaultDepCommRate(rs1
							.getDouble("default_deposit_comm_rate"));
					commVarianceBean.setNetGCommVar(oldNetGCommVar);
					commVarianceBean.setDefaultNetGCommRate(rs1
							.getDouble("default_net_gaming_comm_rate"));
					System.out.println("inside pstmt1---22222222");
				}
				pstmt1.close();
				rs1.close();
				
				
				if (commVarianceBean != null) {
					System.out.println("555555555555555");
					PreparedStatement pstmt5 = conn
							.prepareStatement("delete from st_ola_agent_retailer_comm_variance  where wallet_id=? and retailer_org_id=?");
					pstmt5.setInt(1, commVarianceBean.getGameId());
					pstmt5.setInt(2, commVarianceBean.getRetOrgId());
					int rows = pstmt5.executeUpdate();
					System.out.println("query = " + pstmt5);
					System.out
							.println("Delete entry from st_ola_agent_retailer_variance  table updated rows === "
									+ rows);
					pstmt5.close();
					flagforZero = true;
				}

				if (commVarianceBean != null
						&& widCommVar1 != commVarianceBean.getWidCommVar()) {
					System.out.println("3333333333333333333");
					PreparedStatement pstmt3 = conn
							.prepareStatement("insert into st_ola_agent_retailer_withdrawl_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, commVarianceBean.getRetOrgId());
					pstmt3.setInt(3, commVarianceBean.getGameId());
					pstmt3.setDouble(4, commVarianceBean.getWidCommVar());
					pstmt3.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					System.out.println("query = " + pstmt3);
					System.out
							.println("sale comm var history table updated rows === "
									+ rows);
					pstmt3.close();
				}

				if (commVarianceBean != null
						&& depCommVar1 != commVarianceBean.getDepCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn
							.prepareStatement("insert into st_ola_agent_retailer_deposit_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getDepCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out
							.println("Deposit comm var history table updated rows === "
									+ rows);
					pstmt4.close();
				}
			
				if (commVarianceBean != null
						&& netGCommVar1 != commVarianceBean.getNetGCommVar()) {
					System.out.println("44444444444444444444444");
					PreparedStatement pstmt4 = conn
							.prepareStatement("insert into st_ola_agent_retailer_plr_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, commVarianceBean.getRetOrgId());
					pstmt4.setInt(3, commVarianceBean.getGameId());
					pstmt4.setDouble(4, commVarianceBean.getNetGCommVar());
					pstmt4.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					System.out.println("query = " + pstmt4);
					System.out
							.println("Net Gaming comm var history table updated rows === "
									+ rows);
					pstmt4.close();
				}

				logger.debug(" **widCommVar1** " + widCommVar1 + " **depCommVar1** "
						+ depCommVar1 + " **netGCommVar1** "+ netGCommVar1  +" **");
				logger.debug("widCommVar1!=0.0 ||  depCommVar1!=0.0 || netGCommVar != 0.0"
						+ (widCommVar1 != 0.0 || depCommVar1 != 0.0 || netGCommVar1 != 0.0));

				if (widCommVar1 != 0.0 || depCommVar1 != 0.0 || netGCommVar1 != 0.0) {
					System.out.println("66666666666666666666666666");
					PreparedStatement pstmt6 = conn
							.prepareStatement(" select wallet_id from st_ola_wallet_master where wallet_id=? and wallet_name=? ");
					//pstmt6.setString(1, agtOrgName.trim());
					pstmt6.setInt(1, Integer.parseInt(wallet[0]));
					pstmt6.setString(2, wallet[1]);
					ResultSet rs6 = pstmt6.executeQuery();
					System.out.println("query = " + pstmt6);
					int walletId = 0;
					//int retOrgId = 0;
					if (rs6.next()) {
						walletId = rs6.getInt("wallet_id");
					//	retOrgId = rs6.getInt("organization_id");
					}
					if (walletId > 0 && orgId > 0) {
						PreparedStatement pstmt5 = conn
								.prepareStatement("insert into st_ola_agent_retailer_comm_variance  values (?, ? , ? , ? ,?,?, ?,?,?)");
						pstmt5.setInt(1, userOrgId);
						pstmt5.setInt(2, orgId);
						pstmt5.setInt(3, walletId);
						pstmt5.setDouble(4, depCommVar1);
						pstmt5.setDouble(5, dDepCommRate);
						pstmt5.setDouble(6, widCommVar1);
						pstmt5.setDouble(7, dWidCommRate);
						pstmt5.setDouble(8, netGCommVar1);
						pstmt5.setDouble(9, dNetGCommRate);
						int rows = pstmt5.executeUpdate();
						logger.debug("query = " + pstmt5);
						logger.debug("insert entries st_ola_agent_retailer_comm_variance  table updated rows === "
										+ rows);
						pstmt5.close();
				
				}

				if (commVarianceBean == null && walletId > 0 && orgId > 0) {
					logger.debug("3333333333333333333");
					PreparedStatement pstmt3 = conn
							.prepareStatement("insert into st_ola_agent_retailer_withdrawl_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt3.setInt(1, userOrgId);
					pstmt3.setInt(2, orgId);
					pstmt3.setInt(3, walletId);
					pstmt3.setDouble(4, 0.0);
					pstmt3.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt3.executeUpdate();
					logger.debug("query = " + pstmt3);
					logger.debug("withdrawal comm var history table updated rows === "
									+ rows);
					pstmt3.close();
				}

				if (commVarianceBean == null && walletId > 0 && orgId > 0) {
					logger.debug("44444444444444444444444");
					PreparedStatement pstmt4 = conn
							.prepareStatement("insert into st_ola_agent_retailer_deposit_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt4.setInt(1, userOrgId);
					pstmt4.setInt(2, orgId);
					pstmt4.setInt(3, walletId);
					pstmt4.setDouble(4, 0.0);
					pstmt4.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt4.executeUpdate();
					logger.debug("query = " + pstmt4);
					logger.debug("Deposit comm var history table updated rows === "
									+ rows);
					pstmt4.close();
				}
				
				if (commVarianceBean == null && walletId > 0 && orgId > 0) {
					logger.debug("55555555555555555555555");
					PreparedStatement pstmt5 = conn
							.prepareStatement("insert into st_ola_agent_retailer_plr_comm_variance_history values(?, ?, ?, ?, ?)");
					pstmt5.setInt(1, userOrgId);
					pstmt5.setInt(2, orgId);
					pstmt5.setInt(3, walletId);
					pstmt5.setDouble(4, 0.0);
					pstmt5.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					int rows = pstmt5.executeUpdate();
					logger.debug("query = " + pstmt5);
					logger.debug("Net Gaming comm var history table updated rows === "
									+ rows);
					pstmt5.close();
				}

			}
			
		}else{
				return "AGENT_DIFF_GREATER;"
						+ (dAgtWidComm + (agtWidComm < 0 ? agtWidComm : ("kenya".equalsIgnoreCase(countryDep)?agtWidComm:0.0)))
						+ ";"
						+ (dAgtDepComm + (agtDepComm < 0 ? agtDepComm : 0.0)
						+ ";"
						+ (dAgtNetGComm + (agtNetGComm < 0 ? agtNetGComm : 0.0)));
			}
			//here update the updated comm rate in the context
			   //System.out.println("var in context before: " + Util.getSaleCommVariance(retOrgid, gameId));
				//double retUpdatedCommVar = CommonFunctionsHelper.fetchDGCommOfOrganization(gameId, retOrgid, "SALE", "RETAILER", conn);
				//Util.updateSaleCommVariance(retOrgid, gameId,retUpdatedCommVar);
				//System.out.println("var in context after updation: " + Util.getSaleCommVariance(retOrgid, gameId));
			   //updated successfully
			
			conn.commit();
		} catch (SQLException se) {
			try {
				System.out.println("rolled back");
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				// throw new LMSException(e);
			}
			se.printStackTrace();
			throw new LMSException(se);

		} finally {

			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return "SUCCESS";
	
	}

}