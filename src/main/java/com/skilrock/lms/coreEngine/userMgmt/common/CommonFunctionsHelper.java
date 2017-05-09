package com.skilrock.lms.coreEngine.userMgmt.common;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skilrock.lms.beans.ClmDrawSaleBean;
import com.skilrock.lms.beans.ClmPwtBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.exception.handler.LMSExceptionInterceptor;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class CommonFunctionsHelper {
	static Log logger = LogFactory.getLog(CommonFunctionsHelper.class);
	private static final String GEOCODE_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";
	private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	
	public static String fetchNameOfUser(int userId) {
		String userName = "";
		Connection con = DBConnect.getConnection();
		try {
			String query = "select concat(first_name,' ',last_name) name from st_lms_user_contact_details where user_id = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				userName = rs.getString("name");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return userName;
	}

	public static ArrayList<String> getCityNameList(String countryName) {
		ArrayList<String> cityNameList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			String getCityNameListAll = "SELECT bb.city_name, bb.city_code FROM st_lms_city_master bb, st_lms_country_master aa WHERE aa.country_code = bb.country_code AND aa.name = ? order by bb.city_name";
			PreparedStatement pstmt = con.prepareStatement(getCityNameListAll);
			pstmt.setString(1, countryName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cityNameList.add(rs.getString("city_name").toUpperCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return cityNameList;
	}
	
	public static List<String> getCityCodeAndNameList(String selectState) {
		List<String> cityNameList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select city_code,city_name from st_lms_city_master where state_code='"+selectState+"'");
			while (rs.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(rs.getString("city_name")).append("(").append(rs.getString("city_code").replace(' ', '_')).append(")");
				cityNameList.add(sb.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return cityNameList;
	}
	public static List<String> getCityCodeList(String selectState) {
		List<String> cityCodeList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select city_code,city_name from st_lms_city_master where state_code='"+selectState+"'");
			while (rs.next()) {
				cityCodeList.add(rs.getString("city_code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return cityCodeList;
	}
	public static List<String> getStateList() {
		List<String> statusList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select state_code from st_lms_state_master");
			while (rs.next()) {
				statusList.add(rs.getString("state_code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return statusList;
	}
	public static List getUsersList(int orgId) {
		List<String> userList = new LinkedList<String>();
		Connection connection = DBConnect.getConnection();
		String query = "select user_name from st_lms_user_master where organization_id ="
				+ orgId;
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				userList.add(rs.getString("user_name"));
			}
			DBConnect.closeCon(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	private Connection conn = null;

	private Connection connection;

	private PreparedStatement pstmt;

	private ResultSet result;

	public double fetchCommOfOrganization(int gameId, int orgId,
			String commType, String orgType, Connection con)
			throws SQLException {
		String fetCommAmount = "";
		if ("PWT".equalsIgnoreCase(commType)) {

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.retailer_pwt_comm_rate, b.pwt_comm_variance , "
						+ "(ifnull(b.pwt_comm_variance, 0)+ a.retailer_pwt_comm_rate) 'total_comm' from "
						+ " (select game_id ,retailer_pwt_comm_rate from st_se_game_master where game_id = ?) a "
						+ " left join (select retailer_org_id, pwt_comm_variance, game_id from "
						+ " st_se_agent_retailer_sale_pwt_comm_variance where game_id = ? and  retailer_org_id = ?) b "
						+ "on a.game_id = b.game_id ";
				logger.debug("PWT Commision Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select (a.agent_pwt_comm_rate+ifnull(b.pwt_comm_variance,0)) total_comm from("
						+ "select agent_pwt_comm_rate,game_id from st_se_game_master  where game_id=?)a left join"
						+ "(select pwt_comm_variance,game_id from st_se_bo_agent_sale_pwt_comm_variance "
						+ "where  game_id=? and agent_org_id=?) b on a.game_id=b.game_id";
				logger.debug("PWT Commision Variance.");
			} else {
				logger.debug("ERROR:: Org type is not Defined properly :: "
						+ orgType);
			}

		} else {
			logger.debug("SALE Commision Variance.");
		}
		PreparedStatement fetCommAmountPstmt = con
				.prepareStatement(fetCommAmount);
		fetCommAmountPstmt.setInt(1, gameId);
		fetCommAmountPstmt.setInt(2, gameId);
		fetCommAmountPstmt.setInt(3, orgId);
		ResultSet rs = fetCommAmountPstmt.executeQuery();
		double commAmt = 0.0;
		if (rs.next()) {
			commAmt = rs.getDouble("total_comm");
		}
		logger.debug(" commAmt = " + commAmt + " ,   fetCommAmount = "
				+ fetCommAmountPstmt);
		return commAmt;

	}

	public Map fetchDGPwtDetailsOfAgtOrg(int agtOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		Map clmPwtDetailMap = new TreeMap();
		Map newGameWiseTotAmt = new TreeMap();

		try {
			String fetchClmBalOfOrgQuery = "select  aaa.game_id, bbb.game_nbr, bbb.game_name, ticket_nbr, "
					+ " aaa.draw_id, aaa.agt_claim_comm, aaa.pwt_amt, aaa.pwt_type, aaa.name from ((select "
					+ " b.ticket_nbr, a.pwt_amt, 'Anonymous' as 'pwt_type', a.game_id, a.agt_claim_comm, "
					+ " b.draw_id, 'Anonymous' as name from st_dg_agt_pwt a, st_dg_pwt_inv_? b where "
					+ " a.status = ? and agent_org_id = ? and a.transaction_id = b.agent_transaction_id ) union all "
					+ " (select  cc.ticket_nbr, aa.pwt_amt, 'direct_plr' as 'pwt_type', aa.game_id, "
					+ " agt_claim_comm, cc.draw_id ,concat(bb.first_name, ' ', bb.last_name) 'name' "
					+ " from st_dg_agt_direct_plr_pwt aa, st_lms_player_master bb, st_dg_pwt_inv_? cc where"
					+ " aa.pwt_claim_status = ? and agent_org_id = ? and aa.player_id = bb.player_id and "
					+ " aa.transaction_id = cc.agent_transaction_id ))aaa, st_dg_game_master bbb where "
					+ " aaa.game_id = bbb.game_id order by aaa.game_id, aaa.draw_id";

			logger.debug(fetchClmBalOfOrgQuery);
			PreparedStatement pstmt = con
					.prepareStatement(fetchClmBalOfOrgQuery);

			String fetchDrawGameListQuery = "select game_id, game_nbr, game_name from st_dg_game_master";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchDrawGameListQuery);

			int gameNbr = 0;
			double totalClmBal = 0.0, pwtAmt = 0.0;
			ClmPwtBean pwtBean = null;
			String gameName = "";
			while (rs.next()) {
				gameNbr = rs.getInt("game_nbr");
				gameName = rs.getInt("game_nbr") + "-"
						+ rs.getString("game_name");
				totalClmBal = 0.0;

				pstmt.setInt(1, gameNbr);
				// pstmt.setInt(2, gameNbr);
				pstmt.setString(2, status);
				pstmt.setInt(3, agtOrgId);
				pstmt.setInt(4, gameNbr);
				pstmt.setString(5, status);
				pstmt.setInt(6, agtOrgId);
				logger.debug("Fetch " + status + " Draw game PWT details for "
						+ orgType + " and orgId is " + agtOrgId + " query is "
						+ pstmt);
				ResultSet result = pstmt.executeQuery();

				List<ClmPwtBean> beanList = new ArrayList<ClmPwtBean>();
				while (result.next()) {
					pwtBean = new ClmPwtBean();
					pwtAmt = result.getDouble("pwt_amt");
					totalClmBal = totalClmBal + pwtAmt;
					pwtBean.setTktNbr(result.getString("ticket_nbr"));
					pwtBean.setDrawId(result.getInt("draw_id"));
					// pwtBean.setPanelId(result.getObject("panel_id"));
					pwtBean.setPwtAmt(result.getDouble("pwt_amt"));
					pwtBean.setPwtType(result.getString("pwt_type"));
					// pwtBean.setClaimComm(result.getDouble("claim_comm"));
					pwtBean.setGameId(result.getInt("game_id"));
					pwtBean.setGameName(result.getString("game_name"));
					pwtBean.setGameNbr(result.getInt("game_nbr"));
					pwtBean.setClaimedBy(result.getString("name"));
					pwtBean.setAgtClaimComm(result.getDouble("agt_claim_comm"));

					beanList.add(pwtBean);
				}
				if (!beanList.isEmpty()) {
					clmPwtDetailMap.put(gameName, beanList);
					newGameWiseTotAmt.put(gameName, totalClmBal);
				}
			}

			if (!clmPwtDetailMap.isEmpty()) {
				map.put("DRClmPwtDetailMap", clmPwtDetailMap);
				map.put("newDRGameWiseTotAmt", newGameWiseTotAmt);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public Map fetchDGPwtDetailsOfAgtOrgClm(int agtOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		Map clmPwtDetailMap = new TreeMap();
		Map newGameWiseTotAmt = new TreeMap();

		try {
			String fetchClmBalOfOrgQuery = "select  aaa.transaction_id, aaa.game_id, bbb.game_nbr, "
					+ " bbb.game_name, aaa.draw_id, aaa.agt_claim_comm, aaa.pwt_amt, aaa.pwt_type, "
					+ " DATE(aaa.transaction_date) 'transaction_date' from ((select  a.transaction_id,  "
					+ " a.draw_id, a.pwt_amt, 'Anonymous' as 'pwt_type', a.game_id,  a.agt_claim_comm, "
					+ " transaction_date from st_dg_agt_pwt a, st_lms_agent_transaction_master b where   "
					+ " a.status = ? and a.agent_org_id = ? and game_id =? and a.transaction_id = b.transaction_id)"
					+ " union all (select    aa.transaction_id, aa.draw_id, aa.pwt_amt, 'direct_plr' as 'pwt_type', "
					+ " aa.game_id,  agt_claim_comm, transaction_date from st_dg_agt_direct_plr_pwt aa where "
					+ " aa.pwt_claim_status = ? and  aa.agent_org_id = ? and game_id =?))aaa, st_dg_game_master"
					+ " bbb where  aaa.game_id = bbb.game_id  order by aaa.game_id, aaa.draw_id, "
					+ " aaa.transaction_date";

			logger.debug(fetchClmBalOfOrgQuery);
			PreparedStatement pstmt = con
					.prepareStatement(fetchClmBalOfOrgQuery);

			String fetchDrawGameListQuery = "select game_id, game_nbr, game_name from st_dg_game_master";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchDrawGameListQuery);

			int gameNbr = 0;
			double totalClmBal = 0.0, pwtAmt = 0.0;
			ClmPwtBean pwtBean = null;
			String gameName = "";
			while (rs.next()) {
				gameNbr = rs.getInt("game_nbr");

				gameName = rs.getInt("game_nbr") + "-"
						+ rs.getString("game_name");
				totalClmBal = 0.0;

				pstmt.setString(1, status);
				pstmt.setInt(2, agtOrgId);
				pstmt.setInt(3, rs.getInt("game_id"));
				pstmt.setString(4, status);
				pstmt.setInt(5, agtOrgId);
				pstmt.setInt(6, rs.getInt("game_id"));

				logger.debug("Fetch " + status + " Draw game PWT details for "
						+ orgType + " and orgId is " + agtOrgId + " query is "
						+ pstmt);
				ResultSet result = pstmt.executeQuery();

				List<ClmPwtBean> beanList = new ArrayList<ClmPwtBean>();
				while (result.next()) {
					pwtBean = new ClmPwtBean();
					pwtAmt = result.getDouble("pwt_amt");
					totalClmBal = totalClmBal + pwtAmt;
					// pwtBean.setTktNbr(result.getString("ticket_nbr"));
					pwtBean.setDrawId(result.getInt("draw_id"));
					// pwtBean.setPanelId(result.getObject("panel_id"));
					pwtBean.setPwtAmt(result.getDouble("pwt_amt"));
					pwtBean.setPwtType(result.getString("pwt_type"));
					// pwtBean.setClaimComm(result.getDouble("claim_comm"));
					pwtBean.setGameId(result.getInt("game_id"));
					pwtBean.setGameName(result.getString("game_name"));
					pwtBean.setGameNbr(result.getInt("game_nbr"));
					// pwtBean.setClaimedBy(result.getString("name"));
					pwtBean.setAgtClaimComm(result.getDouble("agt_claim_comm"));
					pwtBean.setTransactionId(result.getLong("transaction_id"));
					pwtBean.setTransDate(result.getDate("transaction_date")
							.toString());

					beanList.add(pwtBean);
				}
				// if(!beanList.isEmpty()) {
				clmPwtDetailMap.put(gameName, beanList);
				newGameWiseTotAmt.put(gameName, totalClmBal);
				// }
			}

			if (!clmPwtDetailMap.isEmpty()) {
				map.put("DRClmPwtDetailMap", clmPwtDetailMap);
				map.put("newDRGameWiseTotAmt", newGameWiseTotAmt);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public Map fetchDGPwtDetailsOfRetOrg(int retOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		Map clmPwtDetailMap = new TreeMap();
		Map newGameWiseTotAmt = new TreeMap();

		try {
			String fetchClmBalOfOrgQuery = "select aaa.game_id, bbb.game_nbr, bbb.game_name, ticket_nbr, "
					+ " aaa.draw_id,  aaa.panel_id, aaa.agt_claim_comm, aaa.pwt_amt, aaa.pwt_type, "
					+ " aaa.retailer_claim_comm 'claim_comm', aaa.name, DATE(aaa.transaction_date) 'transaction_date' from((select "
					+ " b.ticket_nbr, a.pwt_amt, 'Anonymous' as  'pwt_type' , a.game_id, b.panel_id,   b.draw_id,"
					+ " a.retailer_claim_comm , a.agt_claim_comm , 'Anonymous' as name, transaction_date from "
					+ " st_dg_ret_pwt_?  a, st_dg_pwt_inv_? b, st_lms_retailer_transaction_master c where a.status"
					+ " = ? and a.retailer_org_id = ? and   a.transaction_id = b.retailer_transaction_id and "
					+ " a.transaction_id = c.transaction_id) union all (select  cc.ticket_nbr, aa.pwt_amt , 'direct_plr'"
					+ " as  'pwt_type',  aa.game_id, aa.panel_id, cc.draw_id, aa.retailer_claim_comm, "
					+ " aa.agt_claim_comm,   concat(bb.first_name, ' ', bb.last_name) 'name', transaction_date from "
					+ " st_dg_ret_direct_plr_pwt aa,  st_lms_player_master bb, st_dg_pwt_inv_? cc where  "
					+ " aa.pwt_claim_status = ?   and retailer_org_id = ? and aa.player_id = bb.player_id and "
					+ " aa.transaction_id =   cc.retailer_transaction_id))aaa, st_dg_game_master bbb  where "
					+ " aaa.game_id = bbb.game_id order by aaa.game_id, aaa.draw_id, aaa.panel_id, "
					+ " aaa.transaction_date";

			logger.debug(fetchClmBalOfOrgQuery);
			PreparedStatement pstmt = con
					.prepareStatement(fetchClmBalOfOrgQuery);

			String fetchDrawGameListQuery = "select game_id, game_nbr, game_name from st_dg_game_master";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchDrawGameListQuery);

			int gameNbr = 0;
			double totalClmBal = 0.0, pwtAmt = 0.0;
			ClmPwtBean pwtBean = null;
			String gameName = "";
			while (rs.next()) {
				gameNbr = rs.getInt("game_nbr");
				gameName = rs.getInt("game_nbr") + "-"
						+ rs.getString("game_name");
				totalClmBal = 0.0;

				pstmt.setInt(1, gameNbr);
				pstmt.setInt(2, gameNbr);
				pstmt.setString(3, status);
				pstmt.setInt(4, retOrgId);
				pstmt.setInt(5, gameNbr);
				pstmt.setString(6, status);
				pstmt.setInt(7, retOrgId);
				logger.debug("Fetch " + status + " Draw game PWT details for "
						+ orgType + " and orgId is " + retOrgId + " query is "
						+ pstmt);
				ResultSet result = pstmt.executeQuery();

				List<ClmPwtBean> beanList = new ArrayList<ClmPwtBean>();
				while (result.next()) {
					pwtBean = new ClmPwtBean();
					pwtAmt = result.getDouble("pwt_amt");
					totalClmBal = totalClmBal + pwtAmt;
					pwtBean.setTktNbr(result.getString("ticket_nbr"));
					pwtBean.setDrawId(result.getInt("draw_id"));
					pwtBean.setPanelId(result.getObject("panel_id"));
					pwtBean.setPwtAmt(result.getDouble("pwt_amt"));
					pwtBean.setPwtType(result.getString("pwt_type"));
					pwtBean.setClaimComm(result.getDouble("claim_comm"));
					pwtBean.setGameId(result.getInt("game_id"));
					pwtBean.setGameName(result.getString("game_name"));
					pwtBean.setGameNbr(result.getInt("game_nbr"));
					pwtBean.setClaimedBy(result.getString("name"));
					pwtBean.setAgtClaimComm(result.getDouble("agt_claim_comm"));
					pwtBean.setTransDate(result.getDate("transaction_date")
							.toString());

					beanList.add(pwtBean);
				}
				if (!beanList.isEmpty()) {
					clmPwtDetailMap.put(gameName, beanList);
					newGameWiseTotAmt.put(gameName, totalClmBal);
				}
			}

			if (!clmPwtDetailMap.isEmpty()) {
				map.put("DRClmPwtDetailMap", clmPwtDetailMap);
				map.put("newDRGameWiseTotAmt", newGameWiseTotAmt);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public Map fetchDrawSaleDetailsOfOrg(int userOrgId, String orgType,
			String status, Connection con,
			Map<Long, GameMasterLMSBean> retComMap) throws LMSException {
		GameMasterLMSBean commBean = null;
		Map map = new TreeMap();
		ClmDrawSaleBean darwSaleBean = null;
		PreparedStatement pstmtRefund = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtGameNbr = null;
		try {
			String fetchClmBalOfOrgQuery = "";
			String fetchDrawRefundClmBal = "";
			String getGameNbrFromGameMaster = "";

			Map<String, List<ClmDrawSaleBean>> clmDrawSaleDetailMap = new TreeMap();
			Map<String, List<ClmDrawSaleBean>> clmDrawSaleRefundDetailMap = new TreeMap();

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select a.transaction_id ,mrp_amt ,retailer_comm, agent_comm, "
						+ " good_cause_amt, vat_amt,taxable_sale,a.game_id,DATE(transaction_date) 'transaction_date' from st_dg_ret_sale_? a,"
						+ " st_lms_retailer_transaction_master b  where a.retailer_org_id=? and claim_status=? "
						+ " and a.transaction_id = b.transaction_id order by game_id,transaction_date ";

				fetchDrawRefundClmBal = "select a.transaction_id ,b.transaction_type,a.mrp_amt ,a.retailer_comm,"
						+ " a.agent_comm,a.good_cause_amt, a.vat_amt,a.taxable_sale,a.game_id,a.cancellation_charges, DATE(b.transaction_date) 'transaction_date' from "
						+ " st_dg_ret_sale_refund_? a,st_lms_retailer_transaction_master b  where a.retailer_org_id=? "
						+ " and a.claim_status=? and b.transaction_id=a.transaction_id order by a.game_id, "
						+ " b.transaction_type, b.transaction_date ";

				getGameNbrFromGameMaster = "select game_nbr,prize_payout_ratio,vat_amt from st_dg_game_master";
				pstmtGameNbr = con.prepareStatement(getGameNbrFromGameMaster);
				ResultSet resultGameNbr = pstmtGameNbr.executeQuery();
				int gameNbr = 0;
				double ppr = 0.0;
				double vatAmt = 0.0;

				while (resultGameNbr.next()) {
					gameNbr = resultGameNbr.getInt("game_nbr");
					ppr = resultGameNbr.getInt("prize_payout_ratio");
					vatAmt = resultGameNbr.getDouble("vat_amt");
					pstmt = con.prepareStatement(fetchClmBalOfOrgQuery);
					pstmt.setInt(1, gameNbr);
					pstmt.setInt(2, userOrgId);
					pstmt.setString(3, status);
					logger.debug("****** Fetch RETAILER DRAW SALE=== " + pstmt);
					ResultSet result = pstmt.executeQuery();
					List<ClmDrawSaleBean> beanList = new ArrayList<ClmDrawSaleBean>();

					// String key="-1";
					while (result.next()) {
						commBean = retComMap.get(result
								.getLong("transaction_id"));

						darwSaleBean = new ClmDrawSaleBean();
						double mrpAmt = result.getDouble("mrp_amt");
						double retComm = result.getDouble("retailer_comm");
						double agentComm = result.getDouble("agent_comm");
						double goodCauseAmt = result
								.getDouble("good_cause_amt");
						String transDate = result.getDate("transaction_date")
								.toString();
						int game_id = result.getInt("game_id");
						if (mrpAmt != 0) {
							logger.debug(game_id + "=============" + retComm
									+ "====***************" + mrpAmt);
							String keyGen = game_id + ":"
									+ commBean.getRetSaleCommRate() + ":"
									+ commBean.getAgentSaleCommRate() + ":"
									+ commBean.getGovtComm() + ":" + transDate;
							logger.debug(keyGen
									+ "=================***************");

							darwSaleBean.setPricePayRatio(ppr);
							darwSaleBean.setVatAmt(vatAmt);

							darwSaleBean.setMrpAmt(mrpAmt);
							darwSaleBean.setRetComm(retComm);
							darwSaleBean.setAgtComm(agentComm);
							darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
							darwSaleBean.setGameId(game_id);
							darwSaleBean.setTransactinId(result
									.getLong("transaction_id"));
							darwSaleBean.setTransDate(result.getDate(
									"transaction_date").toString());
							darwSaleBean.setGameNbr(gameNbr);
							// modify the below code so as to improve
							// readability . Use map.contains method
							// remove duplicacy in variable declaration
							// modified by yogesh as suggested by reviewer
							// vinnet

							if (clmDrawSaleDetailMap.containsKey(keyGen)) {
								beanList = clmDrawSaleDetailMap.get(keyGen);
								beanList.add(darwSaleBean);
								clmDrawSaleDetailMap.put(keyGen, beanList);
							} else {
								beanList = new ArrayList<ClmDrawSaleBean>();
								beanList.add(darwSaleBean);
								clmDrawSaleDetailMap.put(keyGen, beanList);
							}
						}

					}

					// get data from draw sale refund table
					pstmtRefund = con.prepareStatement(fetchDrawRefundClmBal);
					pstmtRefund.setInt(1, gameNbr);
					pstmtRefund.setInt(2, userOrgId);
					pstmtRefund.setString(3, status);
					logger.debug("****** Fetch RETAILER DRAW REFUND SALE=== "
							+ pstmtRefund);
					ResultSet resultRefund = pstmtRefund.executeQuery();
					List<ClmDrawSaleBean> beanRefundList = new ArrayList<ClmDrawSaleBean>();
					// Map clmDrawSaleRefundDetailMap = new TreeMap();

					// String keyRefund="-1";
					while (resultRefund.next()) {
						commBean = retComMap.get(resultRefund
								.getLong("transaction_id"));
						darwSaleBean = new ClmDrawSaleBean();
						double mrpAmt = resultRefund.getDouble("mrp_amt");
						double retComm = resultRefund
								.getDouble("retailer_comm");
						double agentComm = resultRefund.getDouble("agent_comm");
						double goodCauseAmt = resultRefund
								.getDouble("good_cause_amt");
						int game_id = resultRefund.getInt("game_id");
						double cancellationCharges = resultRefund
								.getDouble("cancellation_charges");
						String refundType = resultRefund
								.getString("transaction_type");
						String transDate = resultRefund.getDate(
								"transaction_date").toString();
						if (mrpAmt != 0) {
							String keyGenRefund = game_id + ":"
									+ commBean.getRetSaleCommRate() + ":"
									+ commBean.getAgentSaleCommRate() + ":"
									+ commBean.getGovtComm() + ":" + refundType
									+ ":" + transDate;

							darwSaleBean.setPricePayRatio(ppr);
							darwSaleBean.setVatAmt(vatAmt);
							darwSaleBean.setMrpAmt(mrpAmt);
							darwSaleBean.setRetComm(retComm);
							darwSaleBean.setAgtComm(agentComm);
							darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
							darwSaleBean.setGameId(game_id);
							darwSaleBean.setTransactinId(resultRefund
									.getLong("transaction_id"));
							darwSaleBean.setTransDate(resultRefund.getDate(
									"transaction_date").toString());
							darwSaleBean.setGameNbr(gameNbr);
							darwSaleBean
									.setCancellationCharges(cancellationCharges);
							// modify same as sale

							if (clmDrawSaleRefundDetailMap
									.containsKey(keyGenRefund)) {
								beanRefundList = clmDrawSaleRefundDetailMap
										.get(keyGenRefund);
								beanRefundList.add(darwSaleBean);
								clmDrawSaleRefundDetailMap.put(keyGenRefund,
										beanRefundList);
							} else {
								beanRefundList = new ArrayList<ClmDrawSaleBean>();
								beanRefundList.add(darwSaleBean);
								clmDrawSaleRefundDetailMap.put(keyGenRefund,
										beanRefundList);
							}
						}

					}
				}
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select a.transaction_id ,mrp_amt ,retailer_comm,agent_comm, "
						+ " govt_comm, vat_amt,taxable_sale,game_id,DATE(b.transaction_date) "
						+ " 'transaction_date' from st_dg_agt_sale a, st_lms_agent_transaction_master b "
						+ " where agent_org_id=? and claim_status=? and a.transaction_id = b.transaction_id"
						+ " order by game_id,transaction_date ";

				fetchDrawRefundClmBal = "select a.transaction_id ,b.transaction_type,a.mrp_amt , "
						+ " a.retailer_comm,a.agent_comm,govt_comm,a.vat_amt, a.taxable_sale,a.game_id,a.cancellation_charges, DATE(b.transaction_date) 'transaction_date'"
						+ " from st_dg_agt_sale_refund a,st_lms_agent_transaction_master b where "
						+ " a.agent_org_id=? and a.claim_status=? and a.transaction_id=b.transaction_id "
						+ " order by a.game_id,b.transaction_type, transaction_date ";

				pstmt = con.prepareStatement(fetchClmBalOfOrgQuery);
				pstmt.setInt(1, userOrgId);
				pstmt.setString(2, status);
				logger.debug("****** Fetch AGENT DRAW SALE=== " + pstmt);
				ResultSet result = pstmt.executeQuery();
				List<ClmDrawSaleBean> beanList = new ArrayList<ClmDrawSaleBean>();
				// String key="-1";
				while (result.next()) {
					commBean = retComMap.get(result.getLong("transaction_id"));
					darwSaleBean = new ClmDrawSaleBean();
					double mrpAmt = result.getDouble("mrp_amt");
					double agentComm = result.getDouble("agent_comm");
					double goodCauseAmt = result.getDouble("govt_comm");
					int game_id = result.getInt("game_id");
					String transDate = result.getDate("transaction_date")
							.toString();
					if (mrpAmt != 0) {
						String keyGen = game_id + ":"
								+ commBean.getAgentSaleCommRate() + ":"
								+ commBean.getGovtComm() + ":" + transDate;

						darwSaleBean.setMrpAmt(mrpAmt);
						darwSaleBean.setAgtComm(agentComm);
						darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
						darwSaleBean.setGameId(game_id);
						darwSaleBean.setTransactinId(result
								.getLong("transaction_id"));
						darwSaleBean.setTransDate(transDate);

						// modify same as retailer

						if (clmDrawSaleDetailMap.containsKey(keyGen)) {
							beanList = clmDrawSaleDetailMap.get(keyGen);
							beanList.add(darwSaleBean);
							clmDrawSaleDetailMap.put(keyGen, beanList);
						} else {
							beanList = new ArrayList<ClmDrawSaleBean>();
							beanList.add(darwSaleBean);
							clmDrawSaleDetailMap.put(keyGen, beanList);
						}
					}
				}

				// get data from draw sale refund table
				pstmtRefund = con.prepareStatement(fetchDrawRefundClmBal);
				pstmtRefund.setInt(1, userOrgId);
				pstmtRefund.setString(2, status);
				logger
						.debug("****** Fetch AGENT DRAW REFUND=== "
								+ pstmtRefund);
				ResultSet resultRefund = pstmtRefund.executeQuery();
				List<ClmDrawSaleBean> beanRefundList = new ArrayList<ClmDrawSaleBean>();

				// String keyRefund="-1";
				while (resultRefund.next()) {
					commBean = retComMap.get(resultRefund
							.getLong("transaction_id"));
					darwSaleBean = new ClmDrawSaleBean();
					double mrpAmt = resultRefund.getDouble("mrp_amt");
					double agentComm = resultRefund.getDouble("agent_comm");
					double goodCauseAmt = resultRefund.getDouble("govt_comm");
					int game_id = resultRefund.getInt("game_id");
					double cancellationCharges = resultRefund
							.getDouble("cancellation_charges");
					String refundType = resultRefund
							.getString("transaction_type");
					String transDate = resultRefund.getDate("transaction_date")
							.toString();
					if (mrpAmt != 0) {
						String keyGenRefund = game_id + ":"
								+ commBean.getAgentSaleCommRate() + ":"
								+ commBean.getGovtComm() + ":" + refundType
								+ ":" + transDate;

						darwSaleBean.setMrpAmt(mrpAmt);
						darwSaleBean.setAgtComm(agentComm);
						darwSaleBean.setAgtGoodCauseAmt(goodCauseAmt);
						darwSaleBean.setGameId(game_id);
						darwSaleBean.setTransactinId(resultRefund
								.getLong("transaction_id"));
						darwSaleBean.setTransDate(transDate);
						darwSaleBean
								.setCancellationCharges(cancellationCharges);
						// modify same as retailer

						if (clmDrawSaleRefundDetailMap
								.containsKey(keyGenRefund)) {
							beanRefundList = clmDrawSaleRefundDetailMap
									.get(keyGenRefund);
							beanRefundList.add(darwSaleBean);
							clmDrawSaleRefundDetailMap.put(keyGenRefund,
									beanRefundList);
						} else {
							beanRefundList = new ArrayList<ClmDrawSaleBean>();
							beanRefundList.add(darwSaleBean);
							clmDrawSaleRefundDetailMap.put(keyGenRefund,
									beanRefundList);
						}

					}
				}

			}

			if (clmDrawSaleDetailMap != null && !clmDrawSaleDetailMap.isEmpty()) {
				map.put("drawSaleMap", clmDrawSaleDetailMap);
			}
			if (clmDrawSaleRefundDetailMap != null
					&& !clmDrawSaleRefundDetailMap.isEmpty()) {
				map.put("drawSaleRefundMap", clmDrawSaleRefundDetailMap);
			}

			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public Map fetchPwtDetailsOfOrg(int retOrgId, String orgType,
			String status, Connection con) throws LMSException {

		Map map = new TreeMap();
		Map clmPwtDetailMap = new TreeMap();
		ClmPwtBean pwtBean = null;
		try {
			String fetchClmBalOfOrgQuery = "";
			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select  aaa.game_id, bbb.game_nbr, bbb.game_name, ticket_nbr, "
						+ " aaa.agt_claim_comm ,  aaa.virn_code, aaa.pwt_amt,  aaa.pwt_type, aaa.claim_comm,"
						+ " aaa.name, aaa.transaction_date from((select  virn_code, ticket_nbr, pwt_amt, "
						+ " 'Anonymous' as  'pwt_type' , a.game_id, claim_comm , agt_claim_comm , 'Anonymous'"
						+ " as name, DATE(b.transaction_date) 'transaction_date' from st_se_retailer_pwt a, "
						+ " st_lms_retailer_transaction_master b where status = ?  and  a.retailer_org_id = ?"
						+ " and a.transaction_id = b.transaction_id)  "
						+ "union "
						+ " (select  aa.virn_code, aa.ticket_nbr, aa.pwt_amt , 'direct_plr' as  'pwt_type',  "
						+ " aa.game_id,  aa.claim_comm, agt_claim_comm , concat(bb.first_name, ' ', bb.last_name)"
						+ " 'name', DATE(transaction_date) 'transaction_date' from st_se_retailer_direct_player_pwt aa,"
						+ " st_lms_player_master bb where  pwt_claim_status = ?  and retailer_org_id = ? and "
						+ " aa.player_id = bb.player_id ))aaa, st_se_game_master bbb  where aaa.game_id = "
						+ " bbb.game_id order by aaa.game_id, aaa.transaction_date";
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetchClmBalOfOrgQuery = "select  aa.game_id, bb.game_nbr, aa.ticket_nbr, "
						+ " bb.game_name, aa.virn_code, aa.pwt_amt, aa.pwt_type, aa.claim_comm, aa.name, "
						+ " aa.transaction_date from((select a.virn_code,  a.pwt_amt,a.ticket_nbr, 'Anonymous' as 'pwt_type'"
						+ " , a.game_id, a.claim_comm, b.name, DATE(c.transaction_date) 'transaction_date' from"
						+ " st_se_agent_pwt a, st_lms_organization_master b, st_lms_agent_transaction_master c"
						+ " where a.retailer_org_id = b.organization_id  and ( a.status = ? "
						+ ("UNCLAIM_BAL".equalsIgnoreCase(status) ? " or a.status ='BULK_BO' "
								: "")
						+ ") and a.agent_org_id = ? and a.transaction_id = c.transaction_id) "
						+ " union "
						+ " ( select virn_code, pwt_amt 'dir_pwt_amt', ticket_nbr,'direct_plr' as 'pwt_type', game_id,"
						+ " claim_comm, concat(b.first_name, ' ', b.last_name) 'name',  DATE(transaction_date)"
						+ " 'transaction_date' from  st_se_agt_direct_player_pwt a, st_lms_player_master b  "
						+ " where a.player_id = b.player_id and ( pwt_claim_status = ? "
						+ ("UNCLAIM_BAL".equalsIgnoreCase(status) ? " or pwt_claim_status ='BULK_BO' "
								: "")
						+ " )and agent_org_id = ?))aa, st_se_game_master bb where aa.game_id = bb.game_id "
						+ " order by aa.game_id, aa.transaction_date";

			}
			logger.debug(fetchClmBalOfOrgQuery);
			PreparedStatement pstmt = con
					.prepareStatement(fetchClmBalOfOrgQuery);
			pstmt.setString(1, status);
			pstmt.setInt(2, retOrgId);
			pstmt.setString(3, status);
			pstmt.setInt(4, retOrgId);
			logger.debug("********Fetch " + status + " PWT details for "
					+ orgType + " and orgId is " + retOrgId + " query is "
					+ pstmt);
			ResultSet result = pstmt.executeQuery();
			List<ClmPwtBean> beanList = new ArrayList<ClmPwtBean>();
			int gameId = -1;
			double totalClmBal = 0.0;
			int count = 0;
			while (result.next()) {
				count = count + 1;
				pwtBean = new ClmPwtBean();

				double pwtAmt = result.getDouble("pwt_amt");
				int game_id = result.getInt("game_id");

				totalClmBal = totalClmBal + pwtAmt;

				pwtBean.setVirnCode(result.getString("virn_code"));
				pwtBean.setTktNbr(result.getString("ticket_nbr"));
				pwtBean.setPwtAmt(result.getDouble("pwt_amt"));
				pwtBean.setPwtType(result.getString("pwt_type"));
				pwtBean.setClaimComm(result.getDouble("claim_comm"));
				pwtBean.setGameId(result.getInt("game_id"));
				pwtBean.setGameName(result.getString("game_name"));
				pwtBean.setGameNbr(result.getInt("game_nbr"));
				pwtBean.setClaimedBy(result.getString("name"));
				if ("RETAILER".equalsIgnoreCase(orgType)) {
					pwtBean.setAgtClaimComm(result.getDouble("agt_claim_comm"));
				}
				pwtBean.setTransDate(result.getDate("transaction_date")
						.toString());

				if (gameId != game_id) {
					if (gameId == -1) {
						beanList.add(pwtBean);
					} else {
						logger.debug("inside else game_id = " + gameId
								+ "and list size = " + beanList.size());
						clmPwtDetailMap.put(gameId, beanList);
						beanList = new ArrayList<ClmPwtBean>();
						gameId = game_id;
						beanList.add(pwtBean);
					}
					gameId = game_id;
				} else {
					beanList.add(pwtBean);
					gameId = game_id;
				}

				// if result set row is last
				if (result.isLast()) {
					logger.debug("inside if when result is last game_id = "
							+ gameId + " and list size = " + beanList.size());
					clmPwtDetailMap.put(gameId, beanList);
				}

			}
			logger.debug("result row size = " + count);
			if (!clmPwtDetailMap.isEmpty()) {
				map.put("clmPwtDetails", clmPwtDetailMap);
				map.put("totalClmBal", totalClmBal);
			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	public OrgPwtLimitBean fetchPwtLimitsOfOrgnization(int organizationId,
			Connection connection) throws SQLException {
		OrgPwtLimitBean bean = null;
		String query = "select aa.organization_id, verification_limit, approval_limit, pay_limit, scrap_limit,min_claim_per_ticket,max_claim_per_ticket, bb.pwt_scrap from st_lms_oranization_limits aa, st_lms_organization_master bb where  aa.organization_id = bb.organization_id and  aa.organization_id = ?";
		pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, organizationId);
		result = pstmt.executeQuery();
		logger.debug("query that fetch limit details = " + pstmt);
		if (result.next()) {
			bean = new OrgPwtLimitBean();
			bean.setOrganizationId(organizationId);
			bean.setVerificationLimit(result.getDouble("verification_limit"));
			bean.setApprovalLimit(result.getDouble("approval_limit"));
			bean.setPayLimit(result.getDouble("pay_limit"));
			bean.setScrapLimit(result.getDouble("scrap_limit"));
			bean.setIsPwtAutoScrap(result.getString("pwt_scrap"));
			bean.setMinClaimPerTicket(result.getDouble("min_claim_per_ticket"));
			bean.setMaxClaimPerTicket(result.getDouble("max_claim_per_ticket"));
		}
		return bean;
	}

	public ArrayList<String> fetchTerminalIds(int userOrgId) {
		ArrayList<String> termId = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		StringBuilder sb = new StringBuilder("");
		try {
			/*
			 * PreparedStatement ps = con .prepareStatement("select
			 * m.model_name, i.serial_no from st_lms_inv_model_master m,
			 * st_lms_inv_status i where i.current_owner_id = ? and m.model_id =
			 * i.inv_model_id and (m.model_id = 1 or m.model_id = 2 or
			 * m.model_id = 20)");
			 */
			PreparedStatement ps = con
					.prepareStatement("select m.model_name, i.serial_no,m.model_id from st_lms_inv_model_master m, st_lms_inv_status i where i.current_owner_id = ? and m.model_id = i.inv_model_id and m.is_req_on_reg='YES' and i.current_owner_type != 'REMOVED'");
			ps.setInt(1, userOrgId);
			ResultSet rs = ps.executeQuery();
			String modelName = "";
			String serialNo = "";

			while (rs.next()) {
				modelName = rs.getString("model_name");
				serialNo = rs.getString("serial_no");
				termId.add(modelName + "-" + serialNo+"-"+rs.getString("model_id"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return termId;
	}

	public ArrayList<String> fetchTerminalSerialNos(int userOrgId,
			String modelName) {
		ArrayList<String> serialNoList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select i.serial_no from st_lms_inv_model_master m, st_lms_inv_status i where i.current_owner_id = ? and m.model_id = i.inv_model_id and m.model_name = ? and  m.is_req_on_reg='YES' order by i.serial_no");
			ps.setInt(1, userOrgId);
			ps.setString(2, modelName);
			ResultSet rs = ps.executeQuery();
			System.out.println("Terminal Serial No Query:" + ps);

			while (rs.next()) {
				serialNoList.add(rs.getString("serial_no"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return serialNoList;
	}

	public ArrayList<String> fetchTerminalModelNames(int userOrgId) {
		ArrayList<String> modelNamesList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("select distinct(model_name) from st_lms_inv_status, st_lms_inv_model_master where current_owner_id = ? and model_id = inv_model_id and is_req_on_reg = 'YES' and inv_id=? ");
			ps.setInt(1, userOrgId);
			ps.setInt(2, 1);// Get Models Of Terminal 
			ResultSet rs = ps.executeQuery();
			System.out.println("Terminal Model Names Query:" + ps);

			while (rs.next()) {
				modelNamesList.add(rs.getString("model_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return modelNamesList;
	}
	public ArrayList<String> fetchInvModelNames(int userOrgId) {
		ArrayList<String> modelNamesList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps =null;
		try {
			ps = con.prepareStatement("select distinct(model_name) from st_lms_inv_status, st_lms_inv_model_master where current_owner_id = ? and model_id = inv_model_id and is_req_on_reg = 'YES' and inv_id!=? ");
			ps.setInt(1, userOrgId);
			ps.setInt(2, 1);// Get Models Of Terminal 
			ResultSet rs = ps.executeQuery();
			logger.debug("Inv Model Names Query:" + ps);

			while (rs.next()) {
				modelNamesList.add(rs.getString("model_name"));
			}
		} catch (Exception e) {
			logger.error("exception",e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, ps);
		}
		return modelNamesList;
	}
	public Map fetchUnCLMPwtDetailsOfOrg(int userOrgId, String orgType,
			String status) throws LMSException {
		Connection connection = DBConnect.getConnection();
		try {

			boolean isDraw = "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsDraw());
			boolean isScratch = "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsScratch());

			logger.debug(" user org id = " + userOrgId + "   , orgType = "
					+ orgType + "   , status = " + status);
			// fetch scratch game PWT details
			Map map = null;
			Map detailMap = null;
			Map newDetailMap = new TreeMap();
			Map newGameWiseTotAmt = new TreeMap();

			if (isScratch) {
				map = fetchPwtDetailsOfOrg(userOrgId, orgType, status,
						connection);
				detailMap = (Map) map.get("clmPwtDetails");
				// create game details map game name wise

				if (detailMap != null && !detailMap.isEmpty()) {
					Set gameIdSet = detailMap.keySet();
					for (Object gameId : gameIdSet) {
						List<ClmPwtBean> clmBeanList = (List<ClmPwtBean>) detailMap
								.get(gameId);
						String gameName = "";
						double pwtAmtSum = 0.0;
						for (ClmPwtBean clmPwtBean : clmBeanList) {
							gameName = clmPwtBean.getGameNbr() + "-"
									+ clmPwtBean.getGameName();
							pwtAmtSum = pwtAmtSum + clmPwtBean.getPwtAmt();
						}
						newDetailMap.put(gameName, clmBeanList);
						newGameWiseTotAmt.put(gameName, pwtAmtSum);
					}
				}
			}

			// fetch Draw game PWT details
			Map drawMap = new TreeMap();
			if (isDraw) {

				if ("AGENT".equalsIgnoreCase(orgType.trim())) {
					drawMap = fetchDGPwtDetailsOfAgtOrg(userOrgId, orgType,
							status, connection);
				} else {
					drawMap = fetchDGPwtDetailsOfRetOrg(userOrgId, orgType,
							status, connection);
				}
			}
			// Map drawDetailMap = (Map)map.get("clmPwtDetails");

			map = new TreeMap();
			map.put("unclmPwtDet", newDetailMap);
			map.put("totalUnclmBal", newGameWiseTotAmt);
			map.put("DRClmPwtDetailMap", drawMap.get("DRClmPwtDetailMap"));
			map.put("newDRGameWiseTotAmt", drawMap.get("newDRGameWiseTotAmt"));

			return map;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}
	}

/*	public String generateReceiptBo(Connection connection, int partyId,
			String partyType, Map<String, List<Integer>> transIdMap)
			throws LMSException {
		try {
			Set<String> dateKeys = transIdMap.keySet();
			StringBuilder result = new StringBuilder("");
			for (String date : dateKeys) {
				List<Integer> transIdList = transIdMap.get(date);

				// get latest generated receipt number
				PreparedStatement autoGenRecptPstmtBO = connection
						.prepareStatement(QueryManager.getBOLatestReceiptNb());
				autoGenRecptPstmtBO.setString(1, "RECEIPT");
				ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

				String autoGeneRecieptNoBO = null;
				if (recieptRsBO.next()) {
					String lastRecieptNoGeneratedBO = recieptRsBO
							.getString("generated_id");
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"RECEIPT", lastRecieptNoGeneratedBO, "BO");
				} else {
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"RECEIPT", null, "BO");
				}

				// for generating receipt********************

				PreparedStatement boReceiptPstmt = connection
						.prepareStatement(QueryManager.insertInReceiptMaster());
				boReceiptPstmt.setString(1, "BO");
				boReceiptPstmt.executeUpdate();
				int boReceiptId = -1;
				ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
				if (boRSet.next()) {
					boReceiptId = boRSet.getInt(1);

					boReceiptPstmt = connection.prepareStatement(QueryManager
							.insertInBOReceipts());
					boReceiptPstmt.setInt(1, boReceiptId);
					boReceiptPstmt.setString(2, "RECEIPT");
					boReceiptPstmt.setInt(3, partyId);
					boReceiptPstmt.setString(4, partyType);
					boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
					boReceiptPstmt.execute();

					PreparedStatement boReceiptMappingPstmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());
					for (Integer transId : transIdList) {
						boReceiptMappingPstmt.setInt(1, boReceiptId);
						boReceiptMappingPstmt.setInt(2, transId);
						boReceiptMappingPstmt.execute();
					}

				}
				result.append(boReceiptId + "-" + autoGeneRecieptNoBO + ";");
			}
			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}*/

	public String generateReceiptBoNew(Connection connection, int partyId,
			String partyType, Map<String, List<Long>> transIdMap)
			throws LMSException {
		try {
			Set<String> dateKeys = transIdMap.keySet();
			StringBuilder result = new StringBuilder("");
			List<Long> transIdList = new ArrayList<Long>();
			//for (String date : dateKeys) {
				//List<Integer> transIdList = transIdMap.get(date);
				for (String date : dateKeys) {
	                //trnIdListInvoice = trnIdMapInvoice.get(date);
					transIdList.addAll(transIdMap.get(date)) ;
					}
				// get latest generated receipt number
				PreparedStatement autoGenRecptPstmtBO = connection
						.prepareStatement(QueryManager.getBOLatestReceiptNb());
				autoGenRecptPstmtBO.setString(1, "RECEIPT");
				ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

				String autoGeneRecieptNoBO = null;
				if (recieptRsBO.next()) {
					String lastRecieptNoGeneratedBO = recieptRsBO
							.getString("generated_id");
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"RECEIPT", lastRecieptNoGeneratedBO, "BO");
				} else {
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"RECEIPT", null, "BO");
				}

				// for generating receipt********************

				PreparedStatement boReceiptPstmt = connection
						.prepareStatement(QueryManager.insertInReceiptMaster());
				boReceiptPstmt.setString(1, "BO");
				boReceiptPstmt.executeUpdate();
				int boReceiptId = -1;
				ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
				if (boRSet.next()) {
					boReceiptId = boRSet.getInt(1);

					boReceiptPstmt = connection.prepareStatement(QueryManager
							.insertInBOReceipts());
					boReceiptPstmt.setInt(1, boReceiptId);
					boReceiptPstmt.setString(2, "RECEIPT");
					boReceiptPstmt.setInt(3, partyId);
					boReceiptPstmt.setString(4, partyType);
					boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
					boReceiptPstmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boReceiptPstmt.execute();

					PreparedStatement boReceiptMappingPstmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());
					for (Long transId : transIdList) {
						boReceiptMappingPstmt.setInt(1, boReceiptId);
						boReceiptMappingPstmt.setLong(2, transId);
						boReceiptMappingPstmt.execute();
					}

				}
				result.append(boReceiptId + "-" + autoGeneRecieptNoBO + ";");
			//}
			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}
	
	public String generateReceiptDGBo(Connection connection, int partyId,
			String partyType, Map<String, List<Long>> transIdMap)
			throws LMSException {
		try {
			Set<String> dateKeys = transIdMap.keySet();
			StringBuilder result = new StringBuilder("");
			for (String date : dateKeys) {
				List<Long> transIdList = transIdMap.get(date);

				// get latest generated receipt number
				// PreparedStatement autoGenRecptPstmtBO =
				// connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
				String query = "SELECT generated_id from st_lms_bo_receipts where receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT' ORDER BY generated_id DESC LIMIT 1";
				PreparedStatement autoGenRecptPstmtBO = connection
						.prepareStatement(query);
				// autoGenRecptPstmtBO.setString(1, "DG_RECEIPT");
				ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

				String autoGeneRecieptNoBO = null;
				if (recieptRsBO.next()) {
					String lastRecieptNoGeneratedBO = recieptRsBO
							.getString("generated_id");
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"DG_RECEIPT", lastRecieptNoGeneratedBO, "BO");
				} else {
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"DG_RECEIPT", null, "BO");
				}

				// for generating receipt********************

				PreparedStatement boReceiptPstmt = connection
						.prepareStatement(QueryManager.insertInReceiptMaster());
				boReceiptPstmt.setString(1, "BO");
				boReceiptPstmt.executeUpdate();
				int boReceiptId = -1;
				ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
				if (boRSet.next()) {
					boReceiptId = boRSet.getInt(1);

					boReceiptPstmt = connection.prepareStatement(QueryManager
							.insertInBOReceipts());
					boReceiptPstmt.setInt(1, boReceiptId);
					boReceiptPstmt.setString(2, "DG_RECEIPT");
					boReceiptPstmt.setInt(3, partyId);
					boReceiptPstmt.setString(4, partyType);
					boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
					boReceiptPstmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boReceiptPstmt.execute();

					PreparedStatement boReceiptMappingPstmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());
					for (Long transId : transIdList) {
						boReceiptMappingPstmt.setInt(1, boReceiptId);
						boReceiptMappingPstmt.setLong(2, transId);
						boReceiptMappingPstmt.execute();
					}

				}

				result.append(boReceiptId + "-" + autoGeneRecieptNoBO + ";");
			}

			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public synchronized String generateReciptForPwt(
			Map<String, List<Long>> transIdMap, Connection connection,
			int userOrgID, int partyId, String partyType, String recType) {
		int agtReceiptId = -1;
		StringBuilder receipts = new StringBuilder("");
		// int boReceiptId=-1;
		PreparedStatement agtReceiptPstmt = null;
		PreparedStatement agtReceiptMappingPstmt = null;
		String receiptType = null;
		try {
			Set<String> transDate = transIdMap.keySet();
			for (String date : transDate) {
				List<Long> transIdList = transIdMap.get(date);
				// for generating receipt********************
				// insert in receipt master
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptPstmt.setString(1, "AGENT");
				agtReceiptPstmt.executeUpdate();
				if ("DRAWGAME".equalsIgnoreCase(recType)) {
					receiptType = "DG_RECEIPT";
				} else {
					receiptType = "RECEIPT";
				}
				ResultSet agtRSet = agtReceiptPstmt.getGeneratedKeys();
				while (agtRSet.next()) {
					agtReceiptId = agtRSet.getInt(1);
					logger.debug("Receipt Id:" + agtReceiptId);
				}

				PreparedStatement autoGenRecptPstmt = null;
				if (receiptType.equalsIgnoreCase("RECEIPT")) {
					autoGenRecptPstmt = connection
							.prepareStatement(QueryManager
									.getAGENTLatestReceiptNb());
					autoGenRecptPstmt.setString(1, receiptType);
					autoGenRecptPstmt.setInt(2, userOrgID);
				} else {
					if (receiptType.equalsIgnoreCase("DG_RECEIPT")) {
						String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT') and agent_org_id="
								+ userOrgID
								+ " ORDER BY generated_id DESC LIMIT 1";
						autoGenRecptPstmt = connection.prepareStatement(query);
					}
				}

				ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNoAgt = GenerateRecieptNo
						.getRecieptNoAgt(receiptType, lastRecieptNoGenerated,
								"AGENT", userOrgID);

				// insert in agent receipts
				// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInAgentReceipts());
				agtReceiptPstmt.setInt(1, agtReceiptId);
				agtReceiptPstmt.setString(2, receiptType);
				agtReceiptPstmt.setInt(3, userOrgID);
				agtReceiptPstmt.setInt(4, partyId);
				agtReceiptPstmt.setString(5, partyType);
				agtReceiptPstmt.setString(6, autoGeneRecieptNoAgt);
				agtReceiptPstmt.setTimestamp(7, Util.getCurrentTimeStamp());
				agtReceiptPstmt.execute();

				// insert agetn receipt trn mapping

				// agtReceiptMappingQuery =
				// QueryManager.getST1AgtReceiptsMappingQuery();
				agtReceiptMappingPstmt = connection
						.prepareStatement(QueryManager
								.insertAgentReceiptTrnMapping());

				for (int i = 0; i < transIdList.size(); i++) {
					agtReceiptMappingPstmt.setInt(1, agtReceiptId);
					agtReceiptMappingPstmt.setLong(2, transIdList.get(i));
					agtReceiptMappingPstmt.execute();
				}
				receipts.append(agtReceiptId + "-" + autoGeneRecieptNoAgt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipts.toString();
	}

	public synchronized String generateReciptForPwtNew(
			Map<String, List<Long>> transIdMap, Connection connection,
			int userOrgID, int partyId, String partyType, String recType) {
		int agtReceiptId = -1;
		StringBuilder receipts = new StringBuilder("");
		// int boReceiptId=-1;
		PreparedStatement agtReceiptPstmt = null;
		PreparedStatement agtReceiptMappingPstmt = null;
		String receiptType = null;
		List<Long> transIdList = new ArrayList<Long>();
		try {
			Set<String> transDate = transIdMap.keySet();
			//for (String date : transDate) {
				//List<Integer> transIdList = transIdMap.get(date);
				for (String date : transDate) {
	                //trnIdListInvoice = trnIdMapInvoice.get(date);
					transIdList.addAll(transIdMap.get(date)) ;
					}
				// for generating receipt********************
				// insert in receipt master
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptPstmt.setString(1, "AGENT");
				agtReceiptPstmt.executeUpdate();
				if ("DRAWGAME".equalsIgnoreCase(recType)) {
					receiptType = "DG_RECEIPT";
				} else {
					receiptType = "RECEIPT";
				}
				ResultSet agtRSet = agtReceiptPstmt.getGeneratedKeys();
				while (agtRSet.next()) {
					agtReceiptId = agtRSet.getInt(1);
					logger.debug("Receipt Id:" + agtReceiptId);
				}

				PreparedStatement autoGenRecptPstmt = null;
				if (receiptType.equalsIgnoreCase("RECEIPT")) {
					autoGenRecptPstmt = connection
							.prepareStatement(QueryManager
									.getAGENTLatestReceiptNb());
					autoGenRecptPstmt.setString(1, receiptType);
					autoGenRecptPstmt.setInt(2, userOrgID);
				} else {
					if (receiptType.equalsIgnoreCase("DG_RECEIPT")) {
						String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT') and agent_org_id="
								+ userOrgID
								+ " ORDER BY generated_id DESC LIMIT 1";
						autoGenRecptPstmt = connection.prepareStatement(query);
					}
				}

				ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNoAgt = GenerateRecieptNo
						.getRecieptNoAgt(receiptType, lastRecieptNoGenerated,
								"AGENT", userOrgID);

				// insert in agent receipts
				// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInAgentReceipts());
				agtReceiptPstmt.setInt(1, agtReceiptId);
				agtReceiptPstmt.setString(2, receiptType);
				agtReceiptPstmt.setInt(3, userOrgID);
				agtReceiptPstmt.setInt(4, partyId);
				agtReceiptPstmt.setString(5, partyType);
				agtReceiptPstmt.setString(6, autoGeneRecieptNoAgt);
				agtReceiptPstmt.setTimestamp(7, Util.getCurrentTimeStamp());
				agtReceiptPstmt.execute();

				// insert agetn receipt trn mapping

				// agtReceiptMappingQuery =
				// QueryManager.getST1AgtReceiptsMappingQuery();
				agtReceiptMappingPstmt = connection
						.prepareStatement(QueryManager
								.insertAgentReceiptTrnMapping());

				for (int i = 0; i < transIdList.size(); i++) {
					agtReceiptMappingPstmt.setInt(1, agtReceiptId);
					agtReceiptMappingPstmt.setLong(2, transIdList.get(i));
					agtReceiptMappingPstmt.execute();
				}
				receipts.append(agtReceiptId + "-" + autoGeneRecieptNoAgt);
			//}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipts.toString();
	}
	public List<OrgBean> getAgentOrgList() {
		List<OrgBean> list = new ArrayList<OrgBean>(0);
		OrgBean Bean = null;
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(QueryManager
					.getOrgQry("AGENT"));
			
			result = pstmt.executeQuery();
			while (result.next()) {
				Bean = new OrgBean();
				Bean.setOrgId(result.getInt(TableConstants.ORG_ID));
				Bean.setOrgName(result.getString("orgCode"));
				list.add(Bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<OrgBean> getActiveAgentList() {
		List<OrgBean> list = new ArrayList<OrgBean>(0);
		OrgBean Bean = null;
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(QueryManager.getActiveInactiveBlockOrgQry("AGENT"));
			result = pstmt.executeQuery();
			while (result.next()) {
				Bean = new OrgBean();
				Bean.setOrgId(result.getInt(TableConstants.ORG_ID));
				Bean.setOrgName(result.getString("orgCode"));
				list.add(Bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(conn);
		}

		return list;
	}

	public String getAgentOrgWithIdNACA() throws LMSException {
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(QueryManager
					.getST_RECEIPT_SEARCH_AGENT_ORGID());
			result = pstmt.executeQuery();
			StringBuilder Orgstrng = new StringBuilder();
			boolean flag = false;
			while (result.next()) {
				Orgstrng.append("Nx*" + result.getString(TableConstants.NAME)
						+ ":");
				Orgstrng.append(result.getInt(TableConstants.ORG_ID) + ":");
				Orgstrng.append(result.getDouble("available_credit"));
				flag = true;
			}
			String nbrFormatStr = "";
			if (flag) {
				nbrFormatStr = Orgstrng.delete(0, 3).toString();
			}
			logger.debug("returned string = " + nbrFormatStr);
			return nbrFormatStr;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<String> getCityNameList(String countryName,
			String stateName) throws LMSException {
		ArrayList<String> cityNameList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			String getCityNameList = "select cc.city_name from st_lms_country_master aa, st_lms_state_master bb, st_lms_city_master cc where cc.country_code = aa.country_code and cc.state_code = bb.state_code and aa.name = ? and bb.name = ?";
			PreparedStatement pstmt = con.prepareStatement(getCityNameList);
			pstmt.setString(1, countryName);
			pstmt.setString(2, stateName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cityNameList.add(rs.getString("city_name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return cityNameList;
	}

	public List<UserInfoBean> getOrgIdNClmAmtList(Connection con)
			throws SQLException {
		List<UserInfoBean> orgList = new ArrayList<UserInfoBean>();
		String getOrgIdNClmListQuery = "select aa.organization_id, aa.organization_type, aa.parent_id, aa.name, "
				+ " aa.claimable_bal, bb.parent_user_id, bb.user_id from st_lms_organization_master aa, st_lms_user_master bb where "
				+ " aa.organization_id = bb.organization_id and bb.isrolehead = 'Y' and "
				+ " (aa.organization_type = 'AGENT' or aa.organization_type = 'RETAILER') "
				+ " and aa.organization_status!='TERMINATE' order by organization_type desc";
		PreparedStatement pstmt = con.prepareStatement(getOrgIdNClmListQuery);
		ResultSet rs = pstmt.executeQuery();
		logger.debug("getOrgIdNClmAmtListQuery is = " + pstmt);
		UserInfoBean userBean = null;
		while (rs.next()) {
			userBean = new UserInfoBean();
			userBean.setUserOrgId(rs.getInt("organization_id"));
			userBean.setUserId(rs.getInt("user_id"));
			userBean.setClaimableBal(rs.getDouble("claimable_bal"));
			userBean.setUserType(rs.getString("organization_type"));
			userBean.setOrgName(rs.getString("name"));
			userBean.setParentOrgId(rs.getInt("parent_id"));
			userBean.setParentUserId(rs.getInt("parent_user_id"));
			orgList.add(userBean);
		}
		return orgList;
	}

	public List<OrgBean> getRetailerOrgList(String agentOrgId) {
		List<OrgBean> list = new ArrayList<OrgBean>(0);
		OrgBean Bean = null;
		try {
			conn = DBConnect.getConnection();
			String query = "select name, organization_id from st_lms_organization_master where organization_type='RETAILER' ";
			if (Integer.parseInt(agentOrgId) != -1) {
				query = query + "and parent_id = " + agentOrgId;
			}
			query = query + " order by name";

			pstmt = conn.prepareStatement(query);
			result = pstmt.executeQuery();
			while (result.next()) {
				Bean = new OrgBean();
				Bean.setOrgId(result.getInt(TableConstants.ORG_ID));
				Bean.setOrgName(result.getString(TableConstants.NAME));
				list.add(Bean);
			}
			logger.debug(query + "\nreceipt search list : " + list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public String getRetailerOrgWithIdNACA(int agtOrgId) throws LMSException {
		try {
			conn = DBConnect.getConnection();
			pstmt = conn
					.prepareStatement("select name,organization_id,available_credit from st_lms_organization_master where organization_type='RETAILER' and parent_id="
							+ agtOrgId + " order by name");
			result = pstmt.executeQuery();
			StringBuilder Orgstrng = new StringBuilder();
			boolean flag = false;
			while (result.next()) {
				Orgstrng.append("Nx*" + result.getString(TableConstants.NAME)
						+ ":");
				Orgstrng.append(result.getInt(TableConstants.ORG_ID) + ":");
				Orgstrng.append(result.getDouble("available_credit"));
				flag = true;
			}
			String nbrFormatStr = "";
			if (flag) {
				nbrFormatStr = Orgstrng.delete(0, 3).toString();
			}
			logger.debug("returned string = " + nbrFormatStr);
			return nbrFormatStr;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<String> getStateNameList(String countryName)
			throws LMSException {
		ArrayList<String> stateNameList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			String getStateNameList = "select bb.name from st_lms_country_master aa, st_lms_state_master bb where aa.country_code = bb.country_code and aa.name =?";
			PreparedStatement pstmt = con.prepareStatement(getStateNameList);
			pstmt.setString(1, countryName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				stateNameList.add(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return stateNameList;
	}
	
	public String getCountryPhnCode(String countryName) throws LMSException {
		String code = "";
		Connection con = DBConnect.getConnection();
		try {
			String phoneCodeQuery = "select country_phone_code from st_lms_country_master where name='"
					+ countryName + "'";
			PreparedStatement pstmt = con.prepareStatement(phoneCodeQuery);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) { 
				code = rs.getString("country_phone_code");
				if(code==null)
					code="";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return code;
	}
	
	public String getCityCode(String cityName) throws LMSException {
		String cityCode=null;
		Connection con = DBConnect.getConnection();
		try {
			String getStateNameList = "select city_phone_code from st_lms_city_master where city_name=?";
			PreparedStatement pstmt = con.prepareStatement(getStateNameList);
			pstmt.setString(1, cityName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cityCode=rs.getString("city_phone_code"); 
				if(cityCode==null) 
				cityCode="";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return cityCode;
	}

	public ClmDrawSaleBean getVatAndPpr(Connection con,
			ClmDrawSaleBean saleBean, int gameId) throws SQLException {
		String getGameNbrFromGameMaster = "select game_nbr,prize_payout_ratio,vat_amt from st_dg_game_master where game_id ="
				+ gameId;
		PreparedStatement pstmtGameNbr = con
				.prepareStatement(getGameNbrFromGameMaster);
		ResultSet resultGameNbr = pstmtGameNbr.executeQuery();
		while (resultGameNbr.next()) {
			saleBean.setPricePayRatio(resultGameNbr
					.getDouble("prize_payout_ratio"));
			saleBean.setVatAmt(resultGameNbr.getDouble("vat_amt"));
		}
		return saleBean;
	}

	public boolean updateBookStatus(int gameId, String bookNbr, Connection con,
			String status) throws SQLException {
		String updateBookStatus = "update st_se_game_inv_status set book_status = ? where game_id = ? and book_nbr=?";
		PreparedStatement updateBookStatusPstmt = con
				.prepareStatement(updateBookStatus);
		updateBookStatusPstmt.setString(1, status);
		updateBookStatusPstmt.setInt(2, gameId);
		updateBookStatusPstmt.setString(3, bookNbr);
		int retBalRow = updateBookStatusPstmt.executeUpdate();
		logger.debug(retBalRow + " row updated,  updateBookStatus = "
				+ updateBookStatusPstmt);
		return retBalRow > 0;

	}

	public String updateClaimableForDrawSaleAgt(Map drawMap, int agtOrgId,
			int agtParentUserId, int agtParentOrgId, Connection con)
			throws LMSException {
		logger.debug("inside agt updation ");
		double agtDebitAmount = 0.0;// total amount of retailer that would be
		// deducted from retailers ACA
		List<Long> trnIdListInvoice = null;
		List<Long> trnIdListCRNote = null;
		Map<String, List<Long>> trnIdMapInvoice = new HashMap<String, List<Long>>();
		Map<String, List<Long>> trnIdMapCRNote = new HashMap<String, List<Long>>();
		String autoGeneRecieptNo = null;
		try {

			Map<String, List<ClmDrawSaleBean>> drawSaleMap = (Map) drawMap
					.get("drawSaleMap");
			Map<String, List<ClmDrawSaleBean>> drawSaleRefundMap = (Map) drawMap
					.get("drawSaleRefundMap");
			List<ClmDrawSaleBean> drawSaleBeanList = null;
			if (drawSaleMap != null) {
				Set keySet = drawSaleMap.keySet();
				for (Object key : keySet) {
					drawSaleBeanList = drawSaleMap.get(key);
					double totalMrpAmt = 0.0;
					double totalAgtComm = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						getVatAndPpr(con, drawSaleBeanList.get(0), Integer
								.parseInt(((String) key).split(":")[0]));
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					String transDate = null;
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();
						totalAgtComm += clmDrawSaleBean.getAgtComm();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();
						transDate = clmDrawSaleBean.getTransDate();
					}

					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					// calculate agt vat , agt taxable sale and agt good cause
					double agtCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodcauseRate = Double.parseDouble(((String) key)
							.split(":")[2]);
					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					/*
					 * double tatalAgtTaxableSale = (((totalMrpAmt * (100 -
					 * (agtCommRate + 50 + goodcauseRate))) / 100) * 100) / (100 +
					 * 16);
					 */
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					logger
							.debug(key + "Test======" + drawSaleBeanList
									+ "======vatAmt**" + vatAmt
									+ "===agt=========" + agtCommRate
									+ "**goodcauseRate**" + goodcauseRate);
					agtDebitAmount += totalAgtNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "BO");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						if (trnIdMapInvoice.containsKey(transDate)) {
							trnIdMapInvoice.get(transDate).add(transId);
						} else {
							trnIdListInvoice = new ArrayList<Long>();
							trnIdListInvoice.add(transId);
							trnIdMapInvoice.put(transDate, trnIdListInvoice);
						}
						// insert in bo transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInBOTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtParentUserId);
						pstmt.setInt(3, agtParentOrgId);
						pstmt.setString(4, "AGENT");
						pstmt.setInt(5, agtOrgId);
						pstmt
								.setTimestamp(
										6,
										GetDate
												.fetchTransDateTimeStampForAdocLedger(transDate));
						pstmt.setString(7, "DG_SALE");
						pstmt.executeUpdate();

						// insert in bo draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_bo_sale(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtOrgId);
						pstmt.setInt(3, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(4, totalMrpAmt);
						pstmt.setDouble(5, totalAgtComm);
						pstmt.setDouble(6, totalAgtNetAmt);
						pstmt.setDouble(7, totalAgtVat);
						pstmt.setDouble(8, tatalAgtTaxableSale);
						pstmt.setDouble(9, totalGoodCauseAmt);
						pstmt.executeUpdate();

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_agt_sale set claim_status=?,bo_ref_transaction_id=? where transaction_id=?");
							pstmt.setString(1, "DONE_CLAIM");
							pstmt.setLong(2, transId);
							pstmt.setLong(3, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate INVOICE at BO End here
				// int invoiceId = -1;
				// String autoGeneRecieptNo = null;
				if (trnIdMapInvoice.size() > 0 && trnIdMapInvoice != null) {

					Set<String> dateKeys = trnIdMapInvoice.keySet();
					for (String date : dateKeys) {
						trnIdListInvoice = trnIdMapInvoice.get(date);

						String query = "SELECT generated_id from st_lms_bo_receipts where receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT' ORDER BY generated_id DESC LIMIT 1";
						// PreparedStatement boReceiptNoGenStmt =
						// connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
						PreparedStatement boReceiptNoGenStmt = con
								.prepareStatement(query);
						// boReceiptNoGenStmt.setString(1, "DG_INVOICE");
						ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
						String lastRecieptNoGenerated = null;

						while (recieptRs.next()) {
							lastRecieptNoGenerated = recieptRs
									.getString("generated_id");
						}
						// String autoGeneRecieptNo = null;
						autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
								"DG_INVOICE", lastRecieptNoGenerated, "BO");

						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInReceiptMaster());
						boReceiptNoGenStmt.setString(1, "BO");
						boReceiptNoGenStmt.executeUpdate();

						ResultSet rs = boReceiptNoGenStmt.getGeneratedKeys();
						int invoiceId = -1;
						while (rs.next()) {
							invoiceId = rs.getInt(1);
						}

						// insert in bo receipts
						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInBOReceipts());
						boReceiptNoGenStmt.setInt(1, invoiceId);
						boReceiptNoGenStmt.setString(2, "DG_INVOICE");
						boReceiptNoGenStmt.setInt(3, agtOrgId);
						boReceiptNoGenStmt.setString(4, "AGENT");
						logger.debug("autoGeneRecieptNo********"
								+ autoGeneRecieptNo);
						boReceiptNoGenStmt.setString(5, autoGeneRecieptNo);
						boReceiptNoGenStmt.setTimestamp(6, Util.getCurrentTimeStamp());
						boReceiptNoGenStmt.execute();

						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertBOReceiptTrnMapping());

						for (int i = 0; i < trnIdListInvoice.size(); i++) {
							boReceiptNoGenStmt.setInt(1, invoiceId);
							boReceiptNoGenStmt.setLong(2, trnIdListInvoice
									.get(i));
							boReceiptNoGenStmt.execute();

						}
					}
				}
			}
			// update for refund sale
			if (drawSaleRefundMap != null) {
				Set keySetRefund = drawSaleRefundMap.keySet();
				for (Object key : keySetRefund) {
					drawSaleBeanList = drawSaleRefundMap.get(key);
					double totalMrpAmt = 0.0;
					double totalAgtComm = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double totalCancellationCharges = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						getVatAndPpr(con, drawSaleBeanList.get(0), Integer
								.parseInt(((String) key).split(":")[0]));
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					String transDate = null;
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();
						totalAgtComm += clmDrawSaleBean.getAgtComm();
						totalCancellationCharges += clmDrawSaleBean
								.getCancellationCharges();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();
						transDate = clmDrawSaleBean.getTransDate();
					}

					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancellationCharges;

					// calculate agt vat , agt taxable sale and agt good cause
					double agtCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodcauseRate = Double.parseDouble(((String) key)
							.split(":")[2]);
					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);
					// double tatalAgtTaxableSale =
					// (((totalMrpAmt*(100-(Double.parseDouble(((String)key).split(":")[1])*100/totalMrpAmt
					// + 50
					// +Double.parseDouble(((String)key).split(":")[2])*100/totalMrpAmt)))/100)*100)/(100+16);
					/*
					 * double tatalAgtTaxableSale = (((totalMrpAmt * (100 -
					 * (agtCommRate + 50 + goodcauseRate))) / 100) * 100) / (100 +
					 * 16);
					 */
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, agtCommRate, ppr, goodcauseRate,
							vatAmt);

					agtDebitAmount -= totalAgtNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "BO");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						if (trnIdMapCRNote.containsKey(transDate)) {
							trnIdMapCRNote.get(transDate).add(transId);
						} else {
							trnIdListCRNote = new ArrayList<Long>();
							trnIdListCRNote.add(transId);
							trnIdMapCRNote.put(transDate, trnIdListCRNote);
						}
						// insert in bo transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInBOTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtParentUserId);
						pstmt.setInt(3, agtParentOrgId);
						pstmt.setString(4, "AGENT");
						pstmt.setInt(5, agtOrgId);
						pstmt
								.setTimestamp(
										6,
										GetDate
												.fetchTransDateTimeStampForAdocLedger(transDate));
						pstmt.setString(7, ((String) key).split(":")[3]);
						pstmt.executeUpdate();

						// insert in bo draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_bo_sale_refund(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, agtOrgId);
						pstmt.setInt(3, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(4, totalMrpAmt);
						pstmt.setDouble(5, totalAgtComm);
						pstmt.setDouble(6, totalAgtNetAmt);
						pstmt.setDouble(7, totalAgtVat);
						pstmt.setDouble(8, tatalAgtTaxableSale);
						pstmt.setDouble(9, totalGoodCauseAmt);
						pstmt.setDouble(10, totalCancellationCharges);
						pstmt.executeUpdate();
						logger.debug("%%%%%insert " + pstmt);

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_agt_sale_refund set claim_status=?,bo_ref_transaction_id=? where transaction_id=?");
							pstmt.setString(1, "DONE_CLAIM");
							pstmt.setLong(2, transId);
							pstmt.setLong(3, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate INVOICE at BO End here
				// int invoiceId = -1;
				// String autoGeneRecieptNo = null;
				if (trnIdMapCRNote.size() > 0 && trnIdMapCRNote != null) {
					Set<String> dateKeys = trnIdMapCRNote.keySet();
					for (String date : dateKeys) {
						trnIdListCRNote = trnIdMapCRNote.get(date);

						PreparedStatement boReceiptNoGenStmt = con
								.prepareStatement("SELECT generated_id from st_lms_bo_receipts where receipt_type like ('CR_NOTE%')  ORDER BY generated_id DESC LIMIT 1");
						// boReceiptNoGenStmt.setString(1, "INVOICE");
						ResultSet recieptRs = boReceiptNoGenStmt.executeQuery();
						String lastRecieptNoGenerated = null;

						while (recieptRs.next()) {
							lastRecieptNoGenerated = recieptRs
									.getString("generated_id");
						}

						// String autoGeneRecieptNo = null;
						autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
								"CR_NOTE", lastRecieptNoGenerated, "BO");

						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInReceiptMaster());
						boReceiptNoGenStmt.setString(1, "BO");
						boReceiptNoGenStmt.executeUpdate();

						ResultSet rs = boReceiptNoGenStmt.getGeneratedKeys();
						int crNoteId = -1;
						while (rs.next()) {
							crNoteId = rs.getInt(1);
						}

						// insert in bo receipts
						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInBOReceipts());
						boReceiptNoGenStmt.setInt(1, crNoteId);
						boReceiptNoGenStmt.setString(2, "CR_NOTE");
						boReceiptNoGenStmt.setInt(3, agtOrgId);
						boReceiptNoGenStmt.setString(4, "AGENT");
						boReceiptNoGenStmt.setString(5, autoGeneRecieptNo);
						boReceiptNoGenStmt.execute();

						boReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertBOReceiptTrnMapping());

						for (int i = 0; i < trnIdListCRNote.size(); i++) {
							boReceiptNoGenStmt.setInt(1, crNoteId);
							boReceiptNoGenStmt
									.setLong(2, trnIdListCRNote.get(i));
							boReceiptNoGenStmt.execute();

						}
					}
				}
			}

			logger.debug("sale Agent Debit Amount " + agtDebitAmount);
			// update organization claimable balance
			
			OrgCreditUpdation.updateOrganizationBalWithValidate(agtDebitAmount, "TRANSACTION", "DRAW_GAME_SALE", agtOrgId,
					0, "AGENT", 0, conn);			
			
			OrgCreditUpdation.updateOrganizationBalWithValidate(agtDebitAmount, "CLAIM_BAL", "DEBIT", agtOrgId,
					0, "AGENT", 0, conn);
			/*OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					"DRAW_GAME_SALE", agtDebitAmount, con);*/
			/*updateOrgBalance("CLAIM_BAL", agtDebitAmount, agtOrgId, "DEBIT",
					con);*/
			return autoGeneRecieptNo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}

	}

	public String updateClaimableForDrawSaleRet(Map drawMap, int retOrgId,
			int retParentUserId, int retParentOrgId, Connection con)
			throws LMSException {
		logger.debug("insideeeeeeeeeeeeeeeeeeeee");
		double retDebitAmount = 0.0;// total amount of retailer that would be
		// deducted from retailers ACA
		List<Long> trnIdListInvoice= null;
		Map<String, List<Long>> trnIdMapInvoice = new HashMap<String, List<Long>>();
		List<Long> trnIdListCRNote = null;
		Map<String, List<Long>> trnIdMapCRNote = new HashMap<String, List<Long>>();
		String autoGeneRecieptNo = null;
		try {

			Map<String, List<ClmDrawSaleBean>> drawSaleMap = (Map) drawMap
					.get("drawSaleMap");
			Map<String, List<ClmDrawSaleBean>> drawSaleRefundMap = (Map) drawMap
					.get("drawSaleRefundMap");
			List<ClmDrawSaleBean> drawSaleBeanList = null;

			if (drawSaleMap != null) {
				Set keySet = drawSaleMap.keySet();
				for (Object key : keySet) {
					drawSaleBeanList = drawSaleMap.get(key);
					double totalMrpAmt = 0.0;
					double totalRetComm = 0.0;
					double totalAgtComm = 0.0;
					double totalretNetAmt = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					String transDate = null;
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
						totalMrpAmt = totalMrpAmt + clmDrawSaleBean.getMrpAmt();
						totalRetComm = totalRetComm
								+ clmDrawSaleBean.getRetComm();
						totalAgtComm = totalAgtComm
								+ clmDrawSaleBean.getAgtComm();
						totalGoodCauseAmt = totalGoodCauseAmt
								+ clmDrawSaleBean.getAgtGoodCauseAmt();
						transDate = clmDrawSaleBean.getTransDate();
					}
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					// calculate agt vat , agt taxable sale and agt good cause
					double retCommRate = Double.parseDouble(((String) key)
							.split(":")[1]);
					double goodCauserate = Double.parseDouble(((String) key)
							.split(":")[3]);
					logger.debug(key + "Test======" + drawSaleBeanList
							+ "======vatAmt**" + vatAmt + "============"
							+ retCommRate);
					double totalAgtVat = CommonMethods.calculateDrawGameVatRet(
							totalMrpAmt, retCommRate, ppr, goodCauserate,
							vatAmt);
					/*
					 * double tatalAgtTaxableSale = (((totalMrpAmt * (100 -
					 * (retCommRate + ppr + goodCauserate))) / 100) * 100) /
					 * (100 + vatAmt);
					 */
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, retCommRate, ppr, goodCauserate,
							vatAmt);

					retDebitAmount = retDebitAmount + totalretNetAmt;

					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "AGENT");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						// trnIdListInvoice.add(transId);

						if (trnIdMapInvoice.containsKey(transDate)) {
							trnIdMapInvoice.get(transDate).add(transId);
						} else {
							trnIdListInvoice = new ArrayList<Long>();
							trnIdListInvoice.add(transId);
							trnIdMapInvoice.put(transDate, trnIdListInvoice);
						}
						// insert in agent transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInAgentTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentUserId);
						pstmt.setInt(3, retParentOrgId);
						pstmt.setString(4, "RETAILER");
						pstmt.setInt(5, retOrgId);
						pstmt.setString(6, "DG_SALE");
						pstmt
								.setTimestamp(
										7,
										GetDate
												.fetchTransDateTimeStampForAdocLedger(transDate));
						pstmt.executeUpdate();

						// insert in agent draw game sale table
						pstmt = con
								.prepareStatement("insert into st_dg_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

						logger.debug(tatalAgtTaxableSale
								+ "Test========================" + totalAgtVat);
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentOrgId);
						pstmt.setInt(3, retOrgId);
						pstmt.setInt(4, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(5, totalMrpAmt);
						pstmt.setDouble(6, totalRetComm);
						pstmt.setDouble(7, totalretNetAmt);
						pstmt.setDouble(8, totalAgtComm);
						pstmt.setDouble(9, totalAgtNetAmt);
						pstmt.setString(10, "CLAIM_BAL");
						pstmt.setDouble(11, totalAgtVat);
						pstmt.setDouble(12, CommonMethods
								.fmtToTwoDecimal(tatalAgtTaxableSale));
						pstmt.setDouble(13, CommonMethods
								.fmtToTwoDecimal(totalGoodCauseAmt));
						logger.debug("Test========================" + pstmt);
						pstmt.executeUpdate();

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_ret_sale_? set claim_status=?,agent_ref_transaction_id=? where transaction_id=?");
							pstmt.setInt(1, clmDrawSaleBean.getGameNbr());
							pstmt.setString(2, "DONE_CLAIM");
							pstmt.setLong(3, transId);
							pstmt.setLong(4, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate receipt here
				// int invoiceId = -1;
				// String autoGeneRecieptNo=null;
				if (trnIdMapInvoice != null && trnIdMapInvoice.size() > 0) {
					Set<String> dateKeys = trnIdMapInvoice.keySet();
					for (String date : dateKeys) {
						trnIdListInvoice = trnIdMapInvoice.get(date);

						String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT') and agent_org_id="
								+ retParentOrgId
								+ " ORDER BY generated_id DESC LIMIT 1";
						// PreparedStatement agtReceiptNoGenStmt =
						// con.prepareStatement(QueryManager.getAGENTLatestReceiptNb());
						PreparedStatement agtReceiptNoGenStmt = con
								.prepareStatement(query);
						// agtReceiptNoGenStmt.setString(1, "DG_INVOICE");
						// agtReceiptNoGenStmt.setInt(2, retParentOrgId);
						ResultSet recieptRs = agtReceiptNoGenStmt
								.executeQuery();
						String lastRecieptNoGenerated = null;
						while (recieptRs.next()) {
							lastRecieptNoGenerated = recieptRs
									.getString("generated_id");
						}

						// String autoGeneRecieptNo=null;
						autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
								"DG_INVOICE", lastRecieptNoGenerated, "AGENT",
								retParentOrgId);

						// insert in receipt master for invoice

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInReceiptMaster());
						agtReceiptNoGenStmt.setString(1, "AGENT");
						agtReceiptNoGenStmt.executeUpdate();

						ResultSet rs = agtReceiptNoGenStmt.getGeneratedKeys();
						int invoiceId = -1;
						while (rs.next()) {
							invoiceId = rs.getInt(1);
						}

						// insert into agent receipt table

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInAgentReceipts());
						agtReceiptNoGenStmt.setInt(1, invoiceId);
						agtReceiptNoGenStmt.setString(2, "DG_INVOICE");
						agtReceiptNoGenStmt.setInt(3, retParentOrgId);
						agtReceiptNoGenStmt.setInt(4, retOrgId);
						agtReceiptNoGenStmt.setString(5, "RETAILER");
						agtReceiptNoGenStmt.setString(6, autoGeneRecieptNo);
						agtReceiptNoGenStmt.setTimestamp(7, Util.getCurrentTimeStamp());
						logger.debug(agtReceiptNoGenStmt);
						agtReceiptNoGenStmt.execute();

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertAgentReceiptTrnMapping());
						for (int i = 0; i < trnIdListInvoice.size(); i++) {
							agtReceiptNoGenStmt.setInt(1, invoiceId);
							agtReceiptNoGenStmt.setLong(2, trnIdListInvoice
									.get(i));
							agtReceiptNoGenStmt.execute();

						}

					}
				}
			}
			// update for refund sale
			if (drawSaleRefundMap != null) {
				Set keySetRefund = drawSaleRefundMap.keySet();
				for (Object key : keySetRefund) {
					drawSaleBeanList = drawSaleRefundMap.get(key);
					double totalMrpAmt = 0.0;
					double totalRetComm = 0.0;
					double totalAgtComm = 0.0;
					double totalretNetAmt = 0.0;
					double totalAgtNetAmt = 0.0;
					double totalGoodCauseAmt = 0.0;
					double totalCancellationCharges = 0.0;
					double ppr = 0.0;
					double vatAmt = 0.0;
					if (!drawSaleBeanList.isEmpty()) {
						ppr = drawSaleBeanList.get(0).getPricePayRatio();
						vatAmt = drawSaleBeanList.get(0).getVatAmt();
					}
					String transDate = null;
					for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {

						// totalMrpAmt=clmDrawSaleBean.getMrpAmt();
						totalMrpAmt += clmDrawSaleBean.getMrpAmt();

						// totalRetComm=clmDrawSaleBean.getRetComm();
						totalRetComm += clmDrawSaleBean.getRetComm();

						totalCancellationCharges += clmDrawSaleBean
								.getCancellationCharges();

						// totalAgtComm = clmDrawSaleBean.getAgtComm();
						totalAgtComm += clmDrawSaleBean.getAgtComm();

						// totalGoodCauseAmt =
						// clmDrawSaleBean.getAgtGoodCauseAmt();
						totalGoodCauseAmt += clmDrawSaleBean
								.getAgtGoodCauseAmt();
						transDate = clmDrawSaleBean.getTransDate();

					}
					totalretNetAmt = totalMrpAmt - totalRetComm
							- totalCancellationCharges;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancellationCharges;

					// calculate agt vat , agt taxable sale and agt good cause

					double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
							totalMrpAmt, Double.parseDouble(((String) key)
									.split(":")[1]), ppr, Double
									.parseDouble(((String) key).split(":")[3]),
							vatAmt);
					/*
					 * double tatalAgtTaxableSale = (((totalMrpAmt * (100 -
					 * (Double .parseDouble(((String) key).split(":")[1]) 100 /
					 * totalMrpAmt + ppr + Double .parseDouble(((String)
					 * key).split(":")[3]) 100 / totalMrpAmt))) / 100) * 100) /
					 * (100 + vatAmt);
					 */
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
							totalMrpAmt, Double.parseDouble(((String) key)
									.split(":")[1]), ppr, Double
									.parseDouble(((String) key).split(":")[3]),
							vatAmt);
					retDebitAmount = retDebitAmount - totalretNetAmt;

					// insert in main transaction master
					// insert in main transaction table
					PreparedStatement pstmt = con.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
					pstmt.setString(1, "AGENT");
					pstmt.executeUpdate();
					ResultSet rsTrns = pstmt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						// trnIdListCRNote.add(transId);
						if (trnIdMapCRNote.containsKey(transDate)) {
							trnIdMapCRNote.get(transDate).add(transId);
						} else {
							trnIdListCRNote = new ArrayList<Long>();
							trnIdListCRNote.add(transId);
							trnIdMapCRNote.put(transDate, trnIdListCRNote);
						}
						// insert in agent transaction master
						pstmt = con.prepareStatement(QueryManager
								.insertInAgentTransactionMaster());
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentUserId);
						pstmt.setInt(3, retParentOrgId);
						pstmt.setString(4, "RETAILER");
						pstmt.setInt(5, retOrgId);
						pstmt.setString(6, ((String) key).split(":")[4]);
						pstmt
								.setTimestamp(
										7,
										GetDate
												.fetchTransDateTimeStampForAdocLedger(transDate));
						pstmt.executeUpdate();

						// insert in agent draw game sale table
						// double retNetAmt=clmDrawSaleBean.getMrpAmt() -
						// clmDrawSaleBean.getRetComm();
						// retDebitAmount+=retNetAmt;
						pstmt = con
								.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pstmt.setLong(1, transId);
						pstmt.setInt(2, retParentOrgId);
						pstmt.setInt(3, retOrgId);
						pstmt.setInt(4, Integer.parseInt(((String) key)
								.split(":")[0]));
						pstmt.setDouble(5, totalMrpAmt);
						pstmt.setDouble(6, totalRetComm);
						pstmt.setDouble(7, totalretNetAmt);
						pstmt.setDouble(8, totalAgtComm);
						pstmt.setDouble(9, totalAgtNetAmt);
						pstmt.setString(10, "CLAIM_BAL");
						pstmt.setDouble(11, totalAgtVat);
						pstmt.setDouble(12, tatalAgtTaxableSale);
						pstmt.setDouble(13, totalGoodCauseAmt);
						pstmt.setDouble(14, totalCancellationCharges);
						pstmt.executeUpdate();
						logger.debug("%%%%%insert " + pstmt);

						// update retailer draw table for claim_done
						for (ClmDrawSaleBean clmDrawSaleBean : drawSaleBeanList) {
							pstmt = con
									.prepareStatement("update st_dg_ret_sale_refund_? set claim_status=?,agent_ref_transaction_id=? where transaction_id=?");
							pstmt.setInt(1, clmDrawSaleBean.getGameNbr());
							pstmt.setString(2, "DONE_CLAIM");
							pstmt.setLong(3, transId);
							pstmt.setLong(4, clmDrawSaleBean.getTransactinId());
							pstmt.executeUpdate();

						}
					}
				}

				// genarate receipt here
				// int credit note = -1;
				// String autoGeneRecieptNo=null;
				if (trnIdMapCRNote.size() > 0 && trnIdMapCRNote != null) {

					Set<String> dateKeys = trnIdMapCRNote.keySet();
					for (String date : dateKeys) {
						trnIdListCRNote = trnIdMapCRNote.get(date);

						PreparedStatement agtReceiptNoGenStmt = con
								.prepareStatement(QueryManager
										.getAgentLatestCRNoteNb());
						agtReceiptNoGenStmt.setString(1, "CR_NOTE_CASH");
						agtReceiptNoGenStmt.setString(2, "CR_NOTE");
						agtReceiptNoGenStmt.setInt(3, retParentOrgId);
						ResultSet recieptRs = agtReceiptNoGenStmt
								.executeQuery();
						String lastRecieptNoGenerated = null;
						while (recieptRs.next()) {
							lastRecieptNoGenerated = recieptRs
									.getString("generated_id");
						}

						// String autoGeneRecieptNo=null;
						autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
								"CR_NOTE", lastRecieptNoGenerated, "AGENT",
								retParentOrgId);

						// insert in receipt master for invoice

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInReceiptMaster());
						agtReceiptNoGenStmt.setString(1, "AGENT");
						agtReceiptNoGenStmt.executeUpdate();

						ResultSet rs = agtReceiptNoGenStmt.getGeneratedKeys();
						int crNoteId = -1;
						while (rs.next()) {
							crNoteId = rs.getInt(1);
						}

						// insert into agent receipt table

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertInAgentReceipts());
						agtReceiptNoGenStmt.setInt(1, crNoteId);
						agtReceiptNoGenStmt.setString(2, "CR_NOTE");
						agtReceiptNoGenStmt.setInt(3, retParentOrgId);
						agtReceiptNoGenStmt.setInt(4, retOrgId);
						agtReceiptNoGenStmt.setString(5, "RETAILER");
						agtReceiptNoGenStmt.setString(6, autoGeneRecieptNo);
						agtReceiptNoGenStmt.execute();

						agtReceiptNoGenStmt = con.prepareStatement(QueryManager
								.insertAgentReceiptTrnMapping());
						for (int i = 0; i < trnIdListCRNote.size(); i++) {
							agtReceiptNoGenStmt.setInt(1, crNoteId);
							agtReceiptNoGenStmt.setLong(2, trnIdListCRNote
									.get(i));
							agtReceiptNoGenStmt.execute();

						}

					}
				}
			}

			logger.debug("&&&&&77777777 " + retDebitAmount);
			// update organization claimable balance
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retDebitAmount, "TRANSACTION", "DRAW_GAME_SALE", retOrgId,
					retParentOrgId, "RETAILER", 0, con);
			if(!isValid)
				throw new LMSException();
			
			/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
					"DRAW_GAME_SALE", retDebitAmount, con);*/
			OrgCreditUpdation.updateOrganizationBalWithValidate(retDebitAmount, "CLAIM_BAL", "DEBIT", retOrgId,
					retParentOrgId, "RETAILER", 0, con);
			/*updateOrgBalance("CLAIM_BAL", retDebitAmount, retOrgId, "DEBIT",
					con);*/
			return autoGeneRecieptNo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}

	}

	public String updateClmableBalOfAgtOrg(int agtUserId, int agtOrgId,
			int parUserId, int parOrgId, Map pwtMap, Connection conn)
			throws SQLException, LMSException {

		String receiptId = null;

		String fetchClmBalOfOrgQuery = "select claimable_bal from st_lms_organization_master where organization_id = ?";
		pstmt = conn.prepareStatement(fetchClmBalOfOrgQuery);
		pstmt.setInt(1, agtOrgId);
		result = pstmt.executeQuery();

		if (result.next()) {
			double amt = result.getDouble("claimable_bal");

			logger.debug("======================");
			logger.debug(pwtMap.get("totalClmBal"));

			double totalPwtAmt = (Double) pwtMap.get("totalClmBal");
			logger.debug("claimable_bal of  AGENT id " + agtOrgId + " is = "
					+ FormatNumber.formatNumberForJSP(amt)
					+ " and total claimable pwt amt is = "
					+ FormatNumber.formatNumberForJSP(totalPwtAmt));

			boolean verifyValue = amt > 0 && amt >= totalPwtAmt;
			verifyValue = true; // need to change
			if (verifyValue) {

				Map pwtDetail = (Map) pwtMap.get("clmPwtDetails");
				Set gameIdSet = pwtDetail.keySet();

				double agtCreditAmt = 0.0;
				List<Long> transList = null;
				Map<String, List<Long>> transMap = new HashMap<String, List<Long>>();
				for (Object gameid : gameIdSet) {
					int gameId = (Integer) gameid;
					/*
					 * List<ClmPwtBean> pwtBeanList = (List<ClmPwtBean>)
					 * pwtDetail .get(gameId);
					 */

					List<ClmPwtBean> pwtBeanListGameIdWise = (List<ClmPwtBean>) pwtDetail
							.get(gameId);
					;

					if (pwtBeanListGameIdWise.isEmpty()) {
						logger.debug("pwt bean list is empty for the game = "
								+ gameId);
						continue;
					}

					// These both for loops are created to group the data
					Set<String> groupKeySet = new TreeSet<String>();

					for (ClmPwtBean clmPwtBean : pwtBeanListGameIdWise) {
						groupKeySet.add(clmPwtBean.getGameId() + "="
								+ clmPwtBean.getTransDate());
					}

					logger.debug(groupKeySet
							+ "Test111111****************keySet");
					List<ClmPwtBean> pwtBeanList = null;
					int drawId = 0;
					String transDate = null, gameIdNTransDateArr[] = null;
					for (String drawIdBean : groupKeySet) {
						gameIdNTransDateArr = drawIdBean.split("=");
						int game_id = Integer.parseInt(gameIdNTransDateArr[0]);
						transDate = gameIdNTransDateArr[1];
						pwtBeanList = new ArrayList<ClmPwtBean>();
						for (ClmPwtBean clmPwtBean : pwtBeanListGameIdWise) {
							if (drawId == clmPwtBean.getDrawId()
									&& transDate != null
									&& transDate.equals(clmPwtBean
											.getTransDate())) {
								pwtBeanList.add(clmPwtBean);
							}
						}

						// insert data into main transaction master
						String LMSMasterQuery = QueryManager
								.insertInLMSTransactionMaster();
						PreparedStatement LMSMasterPstmt = conn
								.prepareStatement(LMSMasterQuery);
						LMSMasterPstmt.setString(1, "BO");
						LMSMasterPstmt.executeUpdate();
						logger
								.debug("insert data into transaction master type  is BO ");
						ResultSet resultSet = LMSMasterPstmt.getGeneratedKeys();

						if (resultSet.next()) {
							long transId = resultSet.getLong(1);
							if (transMap.containsKey(transDate)) {
								transMap.get(transDate).add(transId);
							} else {
								transList = new ArrayList<Long>();
								transList.add(transId);
								transMap.put(transDate, transList);
							}
							// insert into agent transaction master
							String masterQuery = QueryManager
									.insertInBOTransactionMaster();
							PreparedStatement masterPstmt = conn
									.prepareStatement(masterQuery);
							masterPstmt.setLong(1, transId);
							masterPstmt.setInt(2, parUserId);
							masterPstmt.setInt(3, parOrgId);
							masterPstmt.setString(4, "AGENT");
							masterPstmt.setInt(5, agtOrgId);
							masterPstmt
									.setTimestamp(
											6,
											GetDate
													.fetchTransDateTimeStampForAdocLedger(transDate));
							masterPstmt.setString(7, "PWT_AUTO");
							masterPstmt.execute();
							logger
									.debug("insert into agent transaction master = "
											+ masterPstmt);

							// insert into st_se_agent_pwt when comes pwtAmt is
							// in
							// payment and approval limit
							String boPwtinsertQuery = QueryManager
									.getST1PwtBODetailQuery();
							;
							PreparedStatement boPwtInsertPstmt = conn
									.prepareStatement(boPwtinsertQuery);

							// update st_se_agent_pwt in case if AGENT brings
							// unclaimed PWTs to BO
							String agtPwtUpdateQuery = "update st_se_agent_pwt set status=? where game_id = ? and "
									+ " virn_code = ? and agent_org_id = ? and ticket_nbr=?";
							PreparedStatement agtPwtUpdatePstmt = conn
									.prepareStatement(agtPwtUpdateQuery);

							// update st_se_agt_direct_player_pwt table
							String agtPwtUpdateDirectPlr = "update st_se_agt_direct_player_pwt set pwt_claim_status=? "
									+ " where game_id = ? and virn_code = ? and agent_org_id = ? ";
							PreparedStatement agtDirPlrPstmt = conn
									.prepareStatement(agtPwtUpdateDirectPlr);

							double commAmt = 0.0, agtNetAmt = 0.0;
							for (ClmPwtBean clmPwtBean : pwtBeanList) {
								logger.debug("\n\n\n");
								String encodedVirn = clmPwtBean.getVirnCode();
								double pwtAmt = clmPwtBean.getPwtAmt();

								boPwtInsertPstmt.setString(1, encodedVirn);
								boPwtInsertPstmt.setLong(2, transId);
								boPwtInsertPstmt.setInt(3, gameId);
								boPwtInsertPstmt.setInt(4, agtUserId);
								boPwtInsertPstmt.setInt(5, agtOrgId);
								commAmt = pwtAmt * clmPwtBean.getClaimComm()
										* 0.01; // AGENT
								// commission
								// amount
								agtNetAmt = pwtAmt + commAmt;
								agtCreditAmt += agtNetAmt; // Total Actual
								// amount
								// that added in AGENT
								// ACA
								boPwtInsertPstmt.setDouble(6, pwtAmt);
								boPwtInsertPstmt.setDouble(7, commAmt);
								boPwtInsertPstmt.setDouble(8, agtNetAmt);
								boPwtInsertPstmt.execute();
								logger.debug("insert into st_se_agent_pwt = "
										+ boPwtInsertPstmt);

								if (!"direct_plr".equals(clmPwtBean
										.getPwtType())) {
									// update retailer PWT status for claimed
									// PWT as
									// done
									agtPwtUpdatePstmt.setString(1, "DONE_CLM");
									agtPwtUpdatePstmt.setInt(2, gameId);
									agtPwtUpdatePstmt.setString(3, encodedVirn);
									agtPwtUpdatePstmt.setInt(4, agtOrgId);
									agtPwtUpdatePstmt.setString(5, clmPwtBean
											.getTktNbr());
									int row = agtPwtUpdatePstmt.executeUpdate();
									if (row == 1) {
										logger
												.debug("status of VIRN == "
														+ encodedVirn
														+ "   in AGENT PWT table Updated successfully = "
														+ agtPwtUpdatePstmt);
									} else {
										throw new LMSException(
												"row updated for VIRN "
														+ encodedVirn
														+ "  is = " + row
														+ " in AGENT PWT table"
														+ agtPwtUpdatePstmt);
									}
								} else {
									// update retailer Direct Player PWT status
									// for
									// claimed PWT as done
									agtDirPlrPstmt.setString(1, "DONE_CLM");
									agtDirPlrPstmt.setInt(2, gameId);
									agtDirPlrPstmt.setString(3, encodedVirn);
									agtDirPlrPstmt.setInt(4, agtOrgId);
									int row = agtDirPlrPstmt.executeUpdate();
									if (row == 1) {
										logger
												.debug("status of  VIRN == "
														+ encodedVirn
														+ "      in AGENT Direct Player PWT table Updated successfully "
														+ agtDirPlrPstmt);
									} else {
										throw new LMSException(
												"row updated for VIRN "
														+ encodedVirn
														+ "  is = "
														+ row
														+ " in AGENT  Direct Player PWT table");
									}

								}

								// update the ticket_inv_detail table
								// String [] tktArr =
								// clmPwtBean.getTktNbr().split("-");
								// commHelper.updateTicketInvTable(clmPwtBean.getTktNbr(),
								// tktArr[0]+"-"+tktArr[1],
								// Integer.parseInt(tktArr[0]), gameId,
								// "CLAIM_AGT_AUTO", parUserId , parOrgId,
								// conn);

								// update PWT inventory status table
								updateVirnStatus(clmPwtBean.getGameNbr(),
										"CLAIM_AGT_AUTO", gameId, clmPwtBean
												.getVirnCode(), conn,
										MD5Encoder.encode(clmPwtBean
												.getTktNbr()));

							}

						} else {
							throw new LMSException(
									"Transaction Not Generated Successfully ");
						}
					}
				}// for loop of all games finished here

				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agtCreditAmt, "TRANSACTION", "PWT_AUTO", agtOrgId,
						0, "AGENT", 0, conn);
				if(!isValid)
					throw new LMSException();
					
				
				/*// update AGENT ACA and claimable Balance
				OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
						"PWT_AUTO", agtCreditAmt, conn);*/
				// commHelper.updateOrgBalance("CLAIM_BAL", agtCreditAmt,
				// agtOrgId, "DEBIT", conn);
				// changed when draw game sale come picture
				OrgCreditUpdation.updateOrganizationBalWithValidate(agtCreditAmt, "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", agtOrgId,
						0, "AGENT", 0, conn);
				/*updateOrgBalance("CLAIM_BAL", agtCreditAmt, agtOrgId, "CREDIT",
						conn);*/

				// generate receipt for BO
				if (transMap.size() > 0) {
					String receipts = generateReceiptBoNew(conn, agtOrgId,
							"AGENT", transMap);
					String receiptArr[] = receipts.split("-");
					receiptId = receiptArr[1];
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
				} else {
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					// throw new LMSException("NO Transaction Done ");
					return null;
				}

			} else {
				return null;
				// throw new LMSException("Claimable Balance = "+amt+" is Not
				// Greator Then ZERO " +
				// " || Less Then total Pwt Amt = "+totalPwtAmt);
			}
		}

		return receiptId;
	}

	public String updateClmableBalOfOrg(int userId, int userOrgId,
			String userType, int parentOrgId) throws LMSException {
		connection = DBConnect.getConnection();
		String ledgerStatus = null;
		try {
			connection.setAutoCommit(false);
			ledgerStatus = updateClmableBalOfOrg(userId, userOrgId, userType,
					parentOrgId, connection, fetchOrgCommRates(userOrgId,
							userType, parentOrgId, createQryForUpdLedger()));
			connection.commit();
			return ledgerStatus;

		} catch (LMSException e) {
			try {
				logger.debug("exception catch before rollback occured");
				connection.rollback();
				logger.debug("after roolbacked");
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new LMSException(e);
			}
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			try {
				logger.debug("exception catch before rollback occured");
				connection.rollback();
				logger.debug("after roolbacked");
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new LMSException(e);
			}
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}

	public String updateClmableBalOfOrg(int userId, int userOrgId,
			String userType, int parentOrgId, Connection connection,
			Map<Long, GameMasterLMSBean> commMap) throws LMSException {

		try {

			boolean isDraw = "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsDraw());
			boolean isScratch = "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsScratch());

			String getagentUserId = "select user_id, bb.name from st_lms_user_master aa, st_lms_organization_master bb,"
					+ " st_lms_role_master cc where aa.organization_id =bb.organization_id and aa.role_id = cc.role_id"
					+ " and aa.organization_id = ? and isrolehead = 'Y' and cc.tier_id = (select tier_id from st_lms_tier_master where"
					+ " tier_code = ?) and cc.is_master = 'Y' ";
			pstmt = connection.prepareStatement(getagentUserId);
			pstmt.setInt(1, parentOrgId);
			if ("RETAILER".equalsIgnoreCase(userType)) {
				pstmt.setString(2, "AGENT");
			} else if ("AGENT".equalsIgnoreCase(userType)) {
				pstmt.setString(2, "BO");
			}
			ResultSet res = pstmt.executeQuery();

			int parentUserId = -1;
			// String parentName = null;
			if (res.next()) {
				parentUserId = res.getInt("user_id");
				// parentName = res.getString("name");
			} else {
				throw new LMSException(
						"parent User Id not found some internal error has occured");
			}

			// update claimable Amount for Instant Game PWT
			String receiptId = null;
			if (isScratch) {
				logger.debug("Instant game pwt started");
				Map virnPwtMap = fetchPwtDetailsOfOrg(userOrgId, userType,
						"CLAIM_BAL", connection);
				if (virnPwtMap != null && !virnPwtMap.isEmpty()) {
					if ("RETAILER".equalsIgnoreCase(userType)) {
						logger.debug("CLAIMABLE for RETAILER");
						receiptId = updateClmableBalOfRetOrg(userId, userOrgId,
								parentUserId, parentOrgId, virnPwtMap,
								connection);
					} else if ("AGENT".equalsIgnoreCase(userType)) { // claimable
						// for
						// AGENT
						logger.debug("CLAIMABLE for AGENT");
						receiptId = updateClmableBalOfAgtOrg(userId, userOrgId,
								parentUserId, parentOrgId, virnPwtMap,
								connection);
					}
					logger.debug("receipt id is " + receiptId);
				} else {
					receiptId = "AlreadyUpdated";
				}
			} else {
				logger.debug("Scratch Game Module not implemented !!");
			}

			// update claimable Amount for Draw games PWT
			String dgReceiptId = null;
			Map drawSaleMap = null;
			String receiptIdDrawSale = null;

			if (isDraw) {

				logger.debug("Draw PWT game pwt started");
				Map dgVirnPwtMap = null;
				if ("RETAILER".equalsIgnoreCase(userType)) {
					dgVirnPwtMap = fetchDGPwtDetailsOfRetOrg(userOrgId,
							userType, "CLAIM_BAL", connection);
					if (dgVirnPwtMap != null && !dgVirnPwtMap.isEmpty()) {
						logger.debug("CLAIMABLE for RETAILER");
						dgReceiptId = updateDGClmableBalOfRetOrg(userId,
								userOrgId, parentUserId, parentOrgId,
								dgVirnPwtMap, connection);
					} else {
						dgReceiptId = "AlreadyUpdated";
					}
				} else if ("AGENT".equalsIgnoreCase(userType)) { // claimable
					// for AGENT
					dgVirnPwtMap = fetchDGPwtDetailsOfAgtOrgClm(userOrgId,
							userType, "CLAIM_BAL", connection);
					if (dgVirnPwtMap != null && !dgVirnPwtMap.isEmpty()) {
						logger.debug("CLAIMABLE for AGENT");
						dgReceiptId = updateDGClmableBalOfAgtOrg(userId,
								userOrgId, parentUserId, parentOrgId,
								dgVirnPwtMap, connection);
					} else {
						dgReceiptId = "AlreadyUpdated";
					}
				}
				logger.debug("receipt id is " + dgReceiptId);

				// added by yogesh claimable for draw sales
				logger
						.debug("code for draw game sale claimmable by yogesh is started *************************");
				drawSaleMap = fetchDrawSaleDetailsOfOrg(userOrgId, userType,
						"CLAIM_BAL", connection, commMap);
				if (drawSaleMap != null && !drawSaleMap.isEmpty()) {
					if ("RETAILER".equalsIgnoreCase(userType)) {
						logger
								.debug("CLAIMABLE for RETAILER by yogesh ************************");
						receiptIdDrawSale = updateClaimableForDrawSaleRet(
								drawSaleMap, userOrgId, parentUserId,
								parentOrgId, connection);
					} else if ("AGENT".equalsIgnoreCase(userType)) { // claimable
						// for
						// AGENT
						logger
								.debug("CLAIMABLE for AGENT by yogesh************************");
						receiptIdDrawSale = updateClaimableForDrawSaleAgt(
								drawSaleMap, userOrgId, parentUserId,
								parentOrgId, connection);
					}
				} else {
					receiptIdDrawSale = "AlreadyUpdated";
				}

			} else {
				logger.debug("Draw Game Module not implemented !!");
			}

			if (receiptId == null && receiptIdDrawSale == null
					&& dgReceiptId == null) {
				logger
						.debug("Exception throws because of no receipt generated !!");
				throw new LMSException(
						"Exception throws because of no receipt generated !!");
			} else if ("AlreadyUpdated".equalsIgnoreCase(receiptId)
					&& "AlreadyUpdated".equalsIgnoreCase(dgReceiptId)
					&& "AlreadyUpdated".equalsIgnoreCase(receiptIdDrawSale)) {
				return "AlreadyUpdated";
			}

			return "LedgerUpdated";

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	/**
	 * this method is depricated and not used commented on 26 april 2013 by sumit
	 */
/*	public void updateClmableBalOfOrgList() throws LMSException {
		logger
				.debug("============Inside updateClmableBalOfOrgList for Claimable Balance Updation ======== ");
		Connection connection = DBConnect.getConnection();
		String ledgerStatus = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		;
		try {
			connection.setAutoCommit(false);
			String query = "select aa.user_id, aa.organization_id,aa.organization_type, bb.parent_id from 	st_lms_user_master aa, st_lms_organization_master bb where aa.organization_id = bb.organization_id and aa.organization_type!='BO' order by aa.organization_type desc";
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ledgerStatus = updateClmableBalOfOrg(rs.getInt("user_id"), rs
						.getInt("organization_id"), rs
						.getString("organization_type"),
						rs.getInt("parent_id"), connection, fetchOrgCommRates(
								rs.getInt("organization_id"), rs
										.getString("organization_type"), rs
										.getInt("parent_id"),
								createQryForUpdLedger()));
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}
*/
	public String updateClmableBalOfRetOrg(int retUserId, int retOrgId,
			int agentUserId, int agtOrgId, Map pwtMap, Connection connection)
			throws SQLException, LMSException {

		// int receiptId = -1;
		String receiptId = null;

		String fetchClmBalOfOrgQuery = "select claimable_bal from st_lms_organization_master where organization_id = ?";
		pstmt = connection.prepareStatement(fetchClmBalOfOrgQuery);
		pstmt.setInt(1, retOrgId);
		result = pstmt.executeQuery();
		if (result.next()) {
			double amt = result.getDouble("claimable_bal");
			double totalPwtAmt = (Double) pwtMap.get("totalClmBal");
			logger.debug("claimable_bal of  retailer id " + retOrgId + " is = "
					+ amt + " and total claimable pwt amt is = " + totalPwtAmt);

			boolean verifyValue = amt > 0 && amt >= totalPwtAmt;
			verifyValue = true; // need to change
			if (verifyValue) {

				Map<Integer, List<ClmPwtBean>> pwtDetail = (Map<Integer, List<ClmPwtBean>>) pwtMap
						.get("clmPwtDetails");
				Set<Integer> gameIdSet = pwtDetail.keySet();

				double retCreditAmt = 0.0, agtTotalClmBal = 0.0;
				List<Long> transList = null;
				Map<String, List<Long>> transMap = new HashMap<String, List<Long>>();
				for (Integer gameId : gameIdSet) {

					List<ClmPwtBean> pwtBeanListGameIdWise = pwtDetail
							.get(gameId);

					if (pwtBeanListGameIdWise.isEmpty()) {
						logger.debug("pwt bean list is empty for the game = "
								+ gameId);
						continue;
					}

					// These both for loops are created to group the data
					Set<String> groupKeySet = new TreeSet<String>();

					for (ClmPwtBean clmPwtBean : pwtBeanListGameIdWise) {
						groupKeySet.add(clmPwtBean.getGameId() + "="
								+ clmPwtBean.getTransDate());
					}

					logger.debug(groupKeySet
							+ "Test11111111****************keySet");
					List<ClmPwtBean> pwtBeanList = null;
					int drawId = 0;
					String transDate = null, gameIdNTransDateArr[] = null;
					for (String drawIdBean : groupKeySet) {
						gameIdNTransDateArr = drawIdBean.split("=");
						int gameid = Integer.parseInt(gameIdNTransDateArr[0]);
						transDate = gameIdNTransDateArr[1];
						pwtBeanList = new ArrayList<ClmPwtBean>();
						for (ClmPwtBean clmPwtBean : pwtBeanListGameIdWise) {
							if (drawId == clmPwtBean.getDrawId()
									&& transDate != null
									&& transDate.equals(clmPwtBean
											.getTransDate())) {
								pwtBeanList.add(clmPwtBean);
							}
						}

						// fetch the PWT commission for parent organization
						// double agtPwtCommRate =
						// commHelper.fetchCommOfOrganization(gameId, agtOrgId,
						// "PWT","AGENT", connection);
						// List<ClmPwtBean> pwtBeanList = pwtDetail.get(gameId);

						// insert data into main transaction master
						logger
								.debug("insert data into transaction master type  is AGENT");
						String transMasQuery = QueryManager
								.insertInLMSTransactionMaster();
						PreparedStatement transMasQueryPstmt = connection
								.prepareStatement(transMasQuery);
						transMasQueryPstmt.setString(1, "AGENT");
						transMasQueryPstmt.executeUpdate();
						ResultSet rs = transMasQueryPstmt.getGeneratedKeys();

						if (rs.next()) {
							long transId = rs.getLong(1);
							// added By Vaibhav
							if (transMap.containsKey(transDate)) {
								transMap.get(transDate).add(transId);
							} else {
								transList = new ArrayList<Long>();
								transList.add(transId);
								transMap.put(transDate, transList);
							}
							// insert into agent transaction master
							String agtTransMasterQuery = QueryManager
									.insertInAgentTransactionMaster();
							logger.debug("agtTransMasterQuery = "
									+ agtTransMasterQuery);
							PreparedStatement agtTransMasterPstmt = connection
									.prepareStatement(agtTransMasterQuery);
							agtTransMasterPstmt.setLong(1, transId);
							agtTransMasterPstmt.setInt(2, agentUserId);
							agtTransMasterPstmt.setInt(3, agtOrgId);
							agtTransMasterPstmt.setString(4, "RETAILER");
							agtTransMasterPstmt.setInt(5, retOrgId);
							agtTransMasterPstmt.setString(6, "PWT_AUTO");
							agtTransMasterPstmt
									.setTimestamp(
											7,
											GetDate
													.fetchTransDateTimeStampForAdocLedger(transDate));
							agtTransMasterPstmt.executeUpdate();
							logger
									.debug("insert into agent transaction master = "
											+ agtTransMasterPstmt);

							// insert into st_se_agent_pwt when comes pwtAmt is
							// in
							// payment and approval limit
							String agtPwtEntry = "insert into  st_se_agent_pwt ( virn_code, transaction_id ,game_id,agent_user_id,retailer_user_id,retailer_org_id,pwt_amt,comm_amt,net_amt,agent_org_id,status,claim_comm,ticket_nbr ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
							pstmt = connection.prepareStatement(agtPwtEntry);

							// update st_se_retailer_pwt in case if retailer
							// brings
							// unclaimed PWTs to agent
							String retPwtUpdateQuery = "update  st_se_retailer_pwt set status=? where game_id = ? and virn_code = ? and retailer_org_id = ? and ticket_nbr=?";
							PreparedStatement retPwtUpdatePstmt = connection
									.prepareStatement(retPwtUpdateQuery);

							// insert in st_ret_direct_player table in case of
							// player
							String retPwtUpdateDirectPlrQry = "update st_se_retailer_direct_player_pwt set pwt_claim_status=? where game_id = ? and virn_code = ? and retailer_org_id = ? ";
							PreparedStatement retDirPlrPstmt = connection
									.prepareStatement(retPwtUpdateDirectPlrQry);

							double commAmt = 0.0, retNetAmt = 0.0;
							for (ClmPwtBean clmPwtBean : pwtBeanList) {
								logger.debug("\n\n\n");
								String encodedVirn = clmPwtBean.getVirnCode();
								double pwtAmt = clmPwtBean.getPwtAmt();

								pstmt.setString(1, encodedVirn);
								pstmt.setLong(2, transId);
								pstmt.setInt(3, gameId);
								pstmt.setInt(4, agentUserId);
								pstmt.setInt(5, retUserId);
								pstmt.setInt(6, retOrgId);
								commAmt = pwtAmt * clmPwtBean.getClaimComm()
										* 0.01; // retailer
								// commission
								// amount
								retNetAmt = pwtAmt + commAmt;
								retCreditAmt += retNetAmt; // Total Actual
								// amount
								// that added in
								// retailer ACA
								pstmt.setDouble(7, pwtAmt);
								pstmt.setDouble(8, commAmt);
								pstmt.setDouble(9, retNetAmt);
								pstmt.setInt(10, agtOrgId);
								pstmt.setString(11, "CLAIM_BAL");
								// agtTotalClmBal+=(pwtAmt+(pwtAmt*.01*agtPwtCommRate));
								// // Total Actual amount that added in AGENT
								// claimable
								// Commission of agent when PWT claimed at
								// retailer
								pstmt.setDouble(12, clmPwtBean
										.getAgtClaimComm());
								pstmt.setString(13, clmPwtBean.getTktNbr());
								pstmt.executeUpdate();
								logger.debug("insert into st_se_agent_pwt = "
										+ pstmt);

								if (!"direct_plr".equals(clmPwtBean
										.getPwtType())) {
									// update retailer PWT status for claimed
									// PWT as
									// done
									retPwtUpdatePstmt.setString(1, "DONE_CLM");
									retPwtUpdatePstmt.setInt(2, gameId);
									retPwtUpdatePstmt.setString(3, encodedVirn);
									retPwtUpdatePstmt.setInt(4, retOrgId);
									retPwtUpdatePstmt.setString(5, clmPwtBean
											.getTktNbr());
									int row = retPwtUpdatePstmt.executeUpdate();
									if (row == 1) {
										logger
												.debug("VIRN == "
														+ encodedVirn
														+ "      in Retailet PWT table Updated successfully");
									} else {
										throw new LMSException(
												"row updated for VIRN "
														+ encodedVirn
														+ "  is = "
														+ row
														+ " in Retailet PWT table");
									}
								} else {
									// update retailer Direct Player PWT status
									// for
									// claimed PWT as done
									retDirPlrPstmt.setString(1, "DONE_CLM");
									retDirPlrPstmt.setInt(2, gameId);
									retDirPlrPstmt.setString(3, encodedVirn);
									retDirPlrPstmt.setInt(4, retOrgId);
									int row = retDirPlrPstmt.executeUpdate();
									if (row == 1) {
										logger
												.debug("VIRN == "
														+ encodedVirn
														+ "      in Retailet Direct Player PWT table Updated successfully");
									} else {
										throw new LMSException(
												"row updated for VIRN "
														+ encodedVirn
														+ "  is = "
														+ row
														+ " in Retailet  Direct Player PWT table");
									}

								}

								// update the ticket_inv_detail table
								String[] tktArr = clmPwtBean.getTktNbr().split(
										"-");
								updateTicketInvTable(clmPwtBean.getTktNbr(),
										tktArr[0] + "-" + tktArr[1], Integer
												.parseInt(tktArr[0]), gameId,
										"CLAIM_RET_CLM_AUTO", agentUserId,
										agtOrgId, "UPDATE", retOrgId,
										"RETAILER", "WEB", connection);

								// update PWT inventory status table
								updateVirnStatus(Integer.parseInt(tktArr[0]),
										"CLAIM_RET_CLM_AUTO", gameId,
										clmPwtBean.getVirnCode(), connection,
										MD5Encoder.encode(clmPwtBean
												.getTktNbr()));

							}

						} else {
							throw new LMSException(
									"Transaction Not Generated Successfully ");
						}
					}

				}// for loop of all games finished here

				// update retailer ACA and claimable Balance
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retCreditAmt, "TRANSACTION", "PWT_AUTO", retOrgId,
						agtOrgId, "RETAILER", 0, connection);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(retCreditAmt, "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", retOrgId,
						agtOrgId, "RETAILER", 0, connection);
				
				
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
						"PWT_AUTO", retCreditAmt, connection);
				// commHelper.updateOrgBalance("CLAIM_BAL", retCreditAmt,
				// retOrgId, "DEBIT", connection);
				// changed when draw game sale come picture
				updateOrgBalance("CLAIM_BAL", retCreditAmt, retOrgId, "CREDIT",
						connection);*/

				// update AGENT Claimable Balance is not required
				// changed when draw game sale come picture
				// commHelper.updateOrgBalance("CLAIM_BAL", agtTotalClmBal,
				// agtOrgId, "CREDIT", connection);

				// generate receipt on agent end
				if (transMap.size() > 0) {
					String strRec = generateReciptForPwtNew(transMap, connection,
							agtOrgId, retOrgId, "RETAILER", "SCRATCH_GAME");
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					receiptId = strRec.split("-")[1];
				} else {
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					// throw new LMSException("NO Transaction Done ");
					return null;
				}
			} else {
				// throw new LMSException("Claimable Balance = "+amt+" is Not
				// Greator Then ZERO " +
				// " || Less Then total Pwt Amt = "+totalPwtAmt);
				return null;
			}
		}

		return receiptId;
	}

	public String updateDGClmableBalOfAgtOrg(int agtUserId, int agtOrgId,
			int boUserId, int boOrgId, Map pwtMap, Connection connection)
			throws SQLException, LMSException {

		// int receiptId = -1;
		String receiptId = null;

		String fetchClmBalOfOrgQuery = "select claimable_bal from st_lms_organization_master where "
				+ " organization_id = ?";
		PreparedStatement pstmt = connection
				.prepareStatement(fetchClmBalOfOrgQuery);
		pstmt.setInt(1, agtOrgId);
		ResultSet result = pstmt.executeQuery();
		if (result.next()) {
			double orgClmBal = result.getDouble("claimable_bal");
			logger.debug("pwt total claimable_bal == " + orgClmBal);
			boolean verifyValue = true; // need to change

			if (verifyValue) {

				Map<String, List<ClmPwtBean>> pwtDetail = (Map<String, List<ClmPwtBean>>) pwtMap
						.get("DRClmPwtDetailMap");
				Set<String> gameNameSet = pwtDetail.keySet();

				double agtCreditAmt = 0.0;
				// agtTotalClmBal = 0.0;
				int gameNbr = -1, gameId = -1;
				List<Long> transList = null;
				Map<String, List<Long>> transMap = new HashMap<String, List<Long>>();
				for (String gameName : gameNameSet) {

					List<ClmPwtBean> pwtBeanList = pwtDetail.get(gameName);

					if (pwtBeanList.isEmpty()) {
						logger.debug("pwt bean list is empty for the game = "
								+ gameName);
						continue;
					}

					// These both for loops are created to group the data draw
					// id wise and comm wise
					Set<String> drawIdSet = new TreeSet<String>();

					for (ClmPwtBean clmPwtBean : pwtBeanList) {
						drawIdSet.add(clmPwtBean.getDrawId() + "="
								+ clmPwtBean.getTransDate());
					}

					List<ClmPwtBean> drawIdWiseList = null;
					int drawId = 0;
					String transDate = null, drawIdNTransDateArr[] = null;
					for (String drawIdBean : drawIdSet) {
						drawIdNTransDateArr = drawIdBean.split("=");
						drawId = Integer.parseInt(drawIdNTransDateArr[0]);
						transDate = drawIdNTransDateArr[1];
						drawIdWiseList = new ArrayList<ClmPwtBean>();
						for (ClmPwtBean clmPwtBean : pwtBeanList) {
							if (drawId == clmPwtBean.getDrawId()
									&& transDate != null
									&& transDate.equals(clmPwtBean
											.getTransDate())) {
								drawIdWiseList.add(clmPwtBean);
							}
						}

						logger.debug(drawId + " and list size = "
								+ drawIdWiseList.size());
						// drawIdMap.put(drawId, drawIdWiseList);

						// insert data into main transaction master
						logger
								.debug("insert data into transaction master type  is BO");
						String transMasQuery = QueryManager
								.insertInLMSTransactionMaster();
						PreparedStatement transMasQueryPstmt = connection
								.prepareStatement(transMasQuery);
						transMasQueryPstmt.setString(1, "BO");
						transMasQueryPstmt.executeUpdate();
						ResultSet rs = transMasQueryPstmt.getGeneratedKeys();

						if (rs.next()) {
							long transId = rs.getLong(1);

							if (transMap.containsKey(transDate)) {
								transMap.get(transDate).add(transId);
							} else {
								transList = new ArrayList<Long>();
								transList.add(transId);
								transMap.put(transDate, transList);
							}

							// insert into agent transaction master
							String boTransMasterQuery = QueryManager
									.insertInBOTransactionMaster();
							logger.debug("boTransMasterQuery = "
									+ boTransMasterQuery);
							PreparedStatement boTransMasterPstmt = connection
									.prepareStatement(boTransMasterQuery);
							boTransMasterPstmt.setLong(1, transId);
							boTransMasterPstmt.setInt(2, boUserId);
							boTransMasterPstmt.setInt(3, boOrgId);
							boTransMasterPstmt.setString(4, "AGENT");
							boTransMasterPstmt.setInt(5, agtOrgId);
							boTransMasterPstmt
									.setTimestamp(
											6,
											GetDate
													.fetchTransDateTimeStampForAdocLedger(transDate));
							boTransMasterPstmt.setString(7, "DG_PWT_AUTO");

							boTransMasterPstmt.executeUpdate();
							logger.debug("insert into bo transaction master = "
									+ boTransMasterPstmt);

							// update st_agt_pwt in case if retailer brings
							// unclaimed PWTs to bo

							String agtPwtUpdateQuery = "update st_dg_agt_pwt aa, st_dg_pwt_inv_? bb set aa.status = ? ,"
									+ " bb.status = ? , bb.bo_transaction_id ="
									+ transId
									+ " where transaction_id = ? and bb.agent_transaction_id =?";
							PreparedStatement agtPwtUpdatePstmt = connection
									.prepareStatement(agtPwtUpdateQuery);

							// insert in st_agt_direct_player table in case of
							// player

							String agtPwtUpdateDirectPlrQry = "update st_dg_agt_direct_plr_pwt aa, st_dg_pwt_inv_? bb "
									+ " set aa.pwt_claim_status = ? , bb.status = ? , bb.bo_transaction_id ="
									+ transId
									+ " where transaction_id = ?  and bb.agent_transaction_id =?";
							PreparedStatement agtDirPlrPstmt = connection
									.prepareStatement(agtPwtUpdateDirectPlrQry);

							double agtNetAmt = 0.0, netPwt = 0.0, sumPwtAmt = 0.0, agtCommRate = 0.0;
							// agtTotalClmBal = 0.0;
							for (ClmPwtBean clmPwtBean : drawIdWiseList) {

								gameNbr = clmPwtBean.getGameNbr();
								gameId = clmPwtBean.getGameId();
								// sum of PWT Amount
								sumPwtAmt = sumPwtAmt + clmPwtBean.getPwtAmt();
								// net amount with commission
								netPwt = clmPwtBean.getPwtAmt()
										+ clmPwtBean.getAgtClaimComm();
								// AGENT net amount with commission
								agtNetAmt = agtNetAmt + netPwt;
								// Total Actual amount that added in retailer
								// ACA
								agtCreditAmt = agtCreditAmt + netPwt;
								logger
										.debug("netPwt = " + netPwt
												+ "agtNetAmt = " + agtNetAmt
												+ " agtCredit Amount = "
												+ agtCreditAmt);

								// agtCommRate = clmPwtBean.getAgtClaimComm();
								// Total Actual amount that added in AGENT
								// claimable
								// agtTotalClmBal += clmPwtBean.getPwtAmt() +
								// (clmPwtBean.getPwtAmt()*
								// clmPwtBean.getAgtClaimComm()*.01);

								if (!"direct_plr".equals(clmPwtBean
										.getPwtType())) {
									// update AGENT PWT status for claimed PWT
									// as done
									agtPwtUpdatePstmt.setInt(1, clmPwtBean
											.getGameNbr());
									agtPwtUpdatePstmt.setString(2, "DONE_CLM");
									agtPwtUpdatePstmt.setString(3,
											"CLAIM_AGT_CLM_AUTO");
									// agtPwtUpdatePstmt.setInt(4,
									// clmPwtBean.getGameId());
									// agtPwtUpdatePstmt.setInt(5,
									// clmPwtBean.getDrawId());
									// agtPwtUpdatePstmt.setString(6,
									// clmPwtBean.getTktNbr());
									// agtPwtUpdatePstmt.setInt(7, agtOrgId);
									agtPwtUpdatePstmt.setLong(4, clmPwtBean
											.getTransactionId());
									agtPwtUpdatePstmt.setLong(5, clmPwtBean
											.getTransactionId());
									agtPwtUpdatePstmt.executeUpdate();
									logger
											.debug("  in agt Direct PWT table Updated successfully \n"
													+ agtPwtUpdatePstmt);
								} else {
									// update AGENT Direct Player PWT status for
									// claimed PWT as done
									agtDirPlrPstmt.setInt(1, clmPwtBean
											.getGameNbr());
									agtDirPlrPstmt.setString(2, "DONE_CLM");
									agtDirPlrPstmt.setString(3,
											"CLAIM_AGT_CLM_AUTO");
									// agtDirPlrPstmt.setInt(4,
									// clmPwtBean.getGameId());
									// agtDirPlrPstmt.setInt(5,
									// clmPwtBean.getDrawId());
									// agtDirPlrPstmt.setString(6,
									// clmPwtBean.getTktNbr());
									// agtDirPlrPstmt.setInt(7, agtOrgId);
									agtDirPlrPstmt.setLong(4, clmPwtBean
											.getTransactionId());
									agtDirPlrPstmt.setLong(5, clmPwtBean
											.getTransactionId());
									agtDirPlrPstmt.executeUpdate();
									logger
											.debug("  in Retailet PWT table Updated successfully \n"
													+ agtDirPlrPstmt);
								}

							}

							logger.debug("=========agtCreditAmt = "
									+ agtCreditAmt + ", agtNetAmt = "
									+ agtNetAmt);

							// insert into st_bo_pwt when comes pwtAmt is in
							// payment and approval limit
							String agtDGPwtEntry = "insert into st_dg_bo_pwt (bo_user_id, bo_org_id, agent_org_id, "
									+ " draw_id, game_id, transaction_id, pwt_amt, comm_amt, net_amt"
									+ ") values (?, ?, ?, ?, ?, ?, ?, ?,?)";
							pstmt = connection.prepareStatement(agtDGPwtEntry);
							pstmt.setInt(1, boUserId);
							pstmt.setInt(2, boOrgId);
							pstmt.setInt(3, agtOrgId);
							pstmt.setInt(4, drawId);
							pstmt.setInt(5, gameId);
							pstmt.setLong(6, transId);
							pstmt.setDouble(7, sumPwtAmt);
							pstmt.setDouble(8, agtNetAmt - sumPwtAmt); // Total
							// commission
							// amount
							// of
							// AGENT
							pstmt.setDouble(9, agtNetAmt);
							logger.debug("insert into st_bo_pwt = " + pstmt);
							pstmt.executeUpdate();

						} else {
							throw new LMSException(
									"Transaction Not Generated Successfully ");
						}

					} // for loop of all games finished here
				}

				// update retailer ACA and claimable Balance
				logger.debug("org claim bal = " + orgClmBal
						+ " :::: agent credit Amt is = " + agtCreditAmt);
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agtCreditAmt, "TRANSACTION", "PWT_AUTO", agtOrgId,
						0, "AGENT", 0, connection);
				if(!isValid)
					throw new LMSException();
				OrgCreditUpdation.updateOrganizationBalWithValidate(agtCreditAmt, "CLAIM_BAL", "CREDIT", agtOrgId,
						0, "AGENT", 0, connection);
				
				
				/*OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
						"PWT_AUTO", agtCreditAmt, connection);
				// commHelper.updateOrgBalance("CLAIM_BAL", retCreditAmt,
				// retOrgId, "DEBIT", connection);
				// changed when draw game sale come picture
				updateOrgBalance("CLAIM_BAL", agtCreditAmt, agtOrgId, "CREDIT",
						connection);*/

				// update AGENT Claimable Balance is not required
				// changed when draw game sale come picture
				// commHelper.updateOrgBalance("CLAIM_BAL", agtTotalClmBal,
				// agtOrgId, "CREDIT", connection);

				// generate receipt on agent end
				if (transMap.size() > 0) {
					String strRec = generateReceiptDGBo(connection, agtOrgId,
							"AGENT", transMap);
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					receiptId = strRec.split("-")[1];
				} else {
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					// throw new LMSException("NO Transaction Done ");
					return null;
				}

			} else {
				// throw new LMSException("Claimable Balance = "+amt+" is Not
				// Greator Then ZERO " +
				// " || Less Then total Pwt Amt = "+totalPwtAmt);
				return null;
			}
		}

		return receiptId;
	}

	public String updateDGClmableBalOfRetOrg(int retUserId, int retOrgId,
			int agentUserId, int agtOrgId, Map pwtMap, Connection connection)
			throws SQLException, LMSException {

		String receiptId = null;

		String fetchClmBalOfOrgQuery = "select claimable_bal from st_lms_organization_master where organization_id = ?";
		PreparedStatement pstmt = connection
				.prepareStatement(fetchClmBalOfOrgQuery);
		pstmt.setInt(1, retOrgId);
		ResultSet result = pstmt.executeQuery();

		double orgClmBal = 0.0, retCreditAmt = 0.0, agtTotalClmBal = 0.0;
		int gameNbr = -1, gameId = -1;

		if (result.next()) {
			orgClmBal = result.getDouble("claimable_bal");
			logger.debug("total claimable_bal == " + orgClmBal);

			boolean verifyValue = true; // need to change

			if (verifyValue) {

				Map<String, List<ClmPwtBean>> pwtDetail = (Map<String, List<ClmPwtBean>>) pwtMap
						.get("DRClmPwtDetailMap");
				Set<String> gameNameSet = pwtDetail.keySet();
				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				List<Long> transList = null;
				Map<String, List<Long>> transMap = new HashMap<String, List<Long>>();
				for (String gameName : gameNameSet) {

					List<ClmPwtBean> pwtBeanList = pwtDetail.get(gameName);

					if (pwtBeanList.isEmpty()) {
						logger.debug("pwt bean list is empty for the game = "
								+ gameName);
						continue;
					}

					// These both for loops are created to group the data draw
					// id wise
					Set<String> drawIdSet = new TreeSet<String>();

					for (ClmPwtBean clmPwtBean : pwtBeanList) {
						drawIdSet.add(clmPwtBean.getDrawId() + "="
								+ clmPwtBean.getTransDate());
					}

					logger.debug(drawIdSet + "Test****************drawIdSet");
					List<ClmPwtBean> drawIdWiseList = null;
					int drawId = 0;
					String transDate = null, drawIdNTransDateArr[] = null;
					for (String drawIdBean : drawIdSet) {
						drawIdNTransDateArr = drawIdBean.split("=");
						drawId = Integer.parseInt(drawIdNTransDateArr[0]);
						transDate = drawIdNTransDateArr[1];
						drawIdWiseList = new ArrayList<ClmPwtBean>();
						for (ClmPwtBean clmPwtBean : pwtBeanList) {
							if (drawId == clmPwtBean.getDrawId()
									&& transDate != null
									&& transDate.equals(clmPwtBean
											.getTransDate())) {
								drawIdWiseList.add(clmPwtBean);
							}
						}

						// insert data into main transaction master
						logger
								.debug("insert data into transaction master type  is AGENT");
						String transMasQuery = QueryManager
								.insertInLMSTransactionMaster();
						PreparedStatement transMasQueryPstmt = connection
								.prepareStatement(transMasQuery);
						transMasQueryPstmt.setString(1, "AGENT");
						transMasQueryPstmt.executeUpdate();
						ResultSet rs = transMasQueryPstmt.getGeneratedKeys();

						if (rs.next()) {
							long  transId = rs.getLong(1);
							// added By Vaibhav
							if (transMap.containsKey(transDate)) {
								transMap.get(transDate).add(transId);
							} else {
								transList = new ArrayList<Long>();
								transList.add(transId);
								transMap.put(transDate, transList);
							}

							// insert into agent transaction master
							String agtTransMasterQuery = QueryManager
									.insertInAgentTransactionMaster();
							logger.debug("agtTransMasterQuery = "
									+ agtTransMasterQuery);
							PreparedStatement agtTransMasterPstmt = connection
									.prepareStatement(agtTransMasterQuery);
							agtTransMasterPstmt.setLong(1, transId);
							agtTransMasterPstmt.setInt(2, agentUserId);
							agtTransMasterPstmt.setInt(3, agtOrgId);
							agtTransMasterPstmt.setString(4, "RETAILER");
							agtTransMasterPstmt.setInt(5, retOrgId);
							agtTransMasterPstmt.setString(6, "DG_PWT_AUTO");
							agtTransMasterPstmt
									.setTimestamp(
											7,
											GetDate
													.fetchTransDateTimeStampForAdocLedger(transDate));
							agtTransMasterPstmt.executeUpdate();
							logger
									.debug("insert into agent transaction master = "
											+ agtTransMasterPstmt);

							// update st_retailer_pwt in case if retailer brings
							// unclaimed PWTs to agent
							String retPwtUpdateQuery = "update st_dg_ret_pwt_? aa, st_dg_pwt_inv_? bb set aa.status = ? ,"
									+ " bb.status = ? , bb.agent_transaction_id ="
									+ transId
									+ " where transaction_id = bb.retailer_transaction_id "
									+ " and aa.game_id = ? and  bb.draw_id = ?  and bb.ticket_nbr = ? and aa.retailer_org_id =?";
							PreparedStatement retPwtUpdatePstmt = connection
									.prepareStatement(retPwtUpdateQuery);

							// insert in st_ret_direct_player table in case of
							// player
							String retPwtUpdateDirectPlrQry = "update st_dg_ret_direct_plr_pwt aa, st_dg_pwt_inv_? bb "
									+ " set aa.pwt_claim_status = ? , bb.status = ? , bb.agent_transaction_id ="
									+ transId
									+ " where transaction_id = bb.retailer_transaction_id and aa.game_id = ? and bb.draw_id = ? "
									+ " and bb.ticket_nbr = ? and aa.retailer_org_id =?";
							PreparedStatement retDirPlrPstmt = connection
									.prepareStatement(retPwtUpdateDirectPlrQry);

							double retNetAmt = 0.0, netPwtAmt = 0.0, sumPwtAmt = 0.0, agtCommRate = 0.0;
							agtTotalClmBal = 0.0;
							int rowUpdate = 0;
							for (ClmPwtBean clmPwtBean : drawIdWiseList) {

								gameNbr = clmPwtBean.getGameNbr();
								gameId = clmPwtBean.getGameId();
								sumPwtAmt = sumPwtAmt + clmPwtBean.getPwtAmt();
								// retailer Net amount with commission
								// commission amount
								netPwtAmt = clmPwtBean.getPwtAmt()
										+ clmPwtBean.getClaimComm();
								// retailer net PWT amount transaction wise
								retNetAmt = retNetAmt + netPwtAmt;
								// total amount to be claimed at AGENT End
								retCreditAmt = retCreditAmt + netPwtAmt;
								logger.debug("netPwtAmt = " + netPwtAmt
										+ "retNetAmt=" + retNetAmt
										+ " , retCreditAmt = " + retCreditAmt);
								agtCommRate += clmPwtBean.getAgtClaimComm();
								// Total Actual amount that added in AGENT
								// claimable
								// agtTotalClmBal += clmPwtBean.getPwtAmt() +
								// (clmPwtBean.getPwtAmt()*
								// clmPwtBean.getAgtClaimComm()*.01);

								if (!"direct_plr".equals(clmPwtBean
										.getPwtType())) {
									// update retailer PWT status for claimed
									// PWT as done
									retPwtUpdatePstmt.setInt(1, clmPwtBean
											.getGameNbr());
									retPwtUpdatePstmt.setInt(2, clmPwtBean
											.getGameNbr());
									retPwtUpdatePstmt.setString(3, "DONE_CLM");
									retPwtUpdatePstmt.setString(4,
											"CLAIM_RET_CLM_AUTO");
									retPwtUpdatePstmt.setInt(5, clmPwtBean
											.getGameId());
									retPwtUpdatePstmt.setInt(6, clmPwtBean
											.getDrawId());
									retPwtUpdatePstmt.setString(7, clmPwtBean
											.getTktNbr());
									retPwtUpdatePstmt.setInt(8, retOrgId);
									logger.debug(rowUpdate
											+ " : update  in Ret Direct PWT \n"
											+ retPwtUpdatePstmt);
									rowUpdate = retPwtUpdatePstmt
											.executeUpdate();

								} else {
									// update retailer Direct Player PWT status
									// for claimed PWT as done
									retDirPlrPstmt.setInt(1, clmPwtBean
											.getGameNbr());
									retDirPlrPstmt.setString(2, "DONE_CLM");
									retDirPlrPstmt.setString(3,
											"CLAIM_RET_CLM_AUTO");
									retDirPlrPstmt.setInt(4, clmPwtBean
											.getGameId());
									retDirPlrPstmt.setInt(5, clmPwtBean
											.getDrawId());
									retDirPlrPstmt.setString(6, clmPwtBean
											.getTktNbr());
									retDirPlrPstmt.setInt(7, retOrgId);
									rowUpdate = retDirPlrPstmt.executeUpdate();
									logger.debug(rowUpdate
											+ " : update in Ret PWT  \n"
											+ retDirPlrPstmt);
								}

							}

							// insert into st_agent_pwt when comes pwtAmt is in
							// payment and approval limit
							String agtDGPwtEntry = "insert into st_dg_agt_pwt (agent_user_id, agent_org_id, retailer_org_id, "
									+ " draw_id, game_id, transaction_id, pwt_amt, comm_amt, net_amt, agt_claim_comm, "
									+ " status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							pstmt = connection.prepareStatement(agtDGPwtEntry);
							pstmt.setInt(1, agentUserId);
							pstmt.setInt(2, agtOrgId);
							pstmt.setInt(3, retOrgId);
							pstmt.setInt(4, drawId);
							pstmt.setInt(5, gameId);
							pstmt.setLong(6, transId);
							pstmt.setDouble(7, sumPwtAmt);
							pstmt.setDouble(8, retNetAmt - sumPwtAmt); // commission
							// amount
							// of
							// retailer
							pstmt.setDouble(9, retNetAmt);
							pstmt.setDouble(10, agtCommRate);
							pstmt.setString(11, "CLAIM_BAL");
							logger.debug("insert into st_agent_pwt = " + pstmt);
							pstmt.executeUpdate();

						} else {
							throw new LMSException(
									"Transaction Not Generated Successfully ");
						}

					} // for loop of all DrawId's For Outer game Loop finished
					// here
				} // for loop of all games finished here

				logger.debug("org current claim bal = " + orgClmBal
						+ "/n credit amount to be claim_bal is = "
						+ retCreditAmt);
				// update retailer ACA and claimable Balance
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retCreditAmt, "TRANSACTION", "PWT_AUTO", retOrgId,
						agtOrgId, "RETAILER", 0, connection);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(retCreditAmt, "CLAIM_BAL", "CREDIT", retOrgId,
						agtOrgId, "RETAILER", 0, connection);
				
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
						"PWT_AUTO", retCreditAmt, connection);
				// commHelper.updateOrgBalance("CLAIM_BAL", retCreditAmt,
				// retOrgId, "DEBIT", connection);
				// changed when draw game sale come picture
				commHelper.updateOrgBalance("CLAIM_BAL", retCreditAmt,
						retOrgId, "CREDIT", connection);*/

				// update AGENT Claimable Balance is not required
				// changed when draw game sale come picture
				// commHelper.updateOrgBalance("CLAIM_BAL", agtTotalClmBal,
				// agtOrgId, "CREDIT", connection);

				// generate receipt on agent end
				if (transMap.size() > 0) {
					String strRec = generateReciptForPwt(transMap, connection,
							agtOrgId, retOrgId, "RETAILER", "DRAWGAME");
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					receiptId = strRec.split("-")[1];
				} else {
					logger.debug("receipt id is = " + receiptId
							+ "  transList.size() = " + transMap.size()
							+ "  and transaction ids = " + transMap);
					// throw new LMSException("NO Transaction Done ");
					return null;
				}

			} else {
				// throw new LMSException("Claimable Balance = "+amt+" is Not
				// Greator Then ZERO " +
				// " || Less Then total Pwt Amt = "+totalPwtAmt);
				return null;
			}
		}

		return receiptId;
	}

	public boolean updateOrgBalance(String claimType, double amount, int orgId,
			String amtUpdateType, Connection connection) throws SQLException {

		// check whether amount type is debit or credit
		amount = "DEBIT".equals(amtUpdateType) ? -1 * amount : amount;
		logger.debug("claimType " + claimType + " ::amtUpdateType "
				+ amtUpdateType + " amount = " + amount);
		String updateRetBalQuery = null;
		if ("UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?";
		} else if ("CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal+?) "
					+ " , organization_status = if((available_credit-claimable_bal)>0, 'ACTIVE', 'INACTIVE')"
					+ " where organization_id = ?";
			// updateRetBalQuery = "update st_lms_organization_master set
			// claimable_bal = (claimable_bal+?) where organization_id = ?";
		} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		}
		PreparedStatement updateRetBal = connection
				.prepareStatement(updateRetBalQuery);
		updateRetBal.setDouble(1, amount);
		if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBal.setDouble(2, amount);
			updateRetBal.setInt(3, orgId);
		} else {
			updateRetBal.setInt(2, orgId);
		}
		int retBalRow = updateRetBal.executeUpdate();
		logger.debug(retBalRow + " row updated,  updateRetBalQuery = "
				+ updateRetBal);

		return retBalRow > 0;
	}

	public boolean updateOrgForUnClaimedVirn(int orgId, String orgType,
			String enVirnCode, String virnStatus, int gameId, String tableType,
			Connection connection) throws SQLException, LMSException {
		boolean flag = false;
		String fetchVirnDetailQuery = "";
		String updateVirnDetailQuery = "";
		if ("RETAILER".equalsIgnoreCase(orgType)) {

			// update according to the retailer

		} else if ("AGENT".equalsIgnoreCase(orgType)) {
			if ("AGENT".equalsIgnoreCase(tableType)) {
				updateVirnDetailQuery = "update st_se_agent_pwt set status=? where game_id = ? and virn_code = ? and agent_org_id = ?";
			} else if ("PLAYER".equalsIgnoreCase(tableType)) {
				updateVirnDetailQuery = "update st_se_agt_direct_player_pwt set pwt_claim_status=? where game_id = ? and virn_code = ? and agent_org_id = ?";
			} else {
				throw new LMSException(
						"ERROR OCCURED while updating status in AGENT or PLR table for UNCLAIMMABLE balance  for this tableType is "
								+ tableType);
				// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
				// game_id = ? and virn_code = ? and agent_org_id = ? and status
				// =
				// ?";
				// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
				// game_id = ? and virn_code = ? and agent_org_id = ? and status
				// =
				// ?";
			}
		}
		pstmt = connection.prepareStatement(updateVirnDetailQuery);
		pstmt.setString(1, virnStatus);
		pstmt.setInt(2, gameId);
		pstmt.setString(3, enVirnCode);
		pstmt.setInt(4, orgId);
		int isupdate = pstmt.executeUpdate();
		logger.debug(" update  virn for " + orgType + " pwt table pstmt = "
				+ pstmt);
		if (isupdate == 1) {
			flag = true;
		} else {
			throw new LMSException(
					"ERROR OCCURED PWT NOT Found for this status " + virnStatus);
		}
		return flag;
	}

	public int updateTicketInvTable(String tktNo, String bookNo, int gameNbr,
			int gameId, String status, int userId, int userOrgId,
			String updateType, int partyOrgId, String channel,
			String interfaceType, Connection connection) throws SQLException,
			LMSException {

		int tktRow = 0;
		// updated by yogesh now not to execute select query

		if ("update".equalsIgnoreCase(updateType)) { // ticket already exist
			// in st_pwt_tickets_inv
			// table
			String updateTktInvQuery = "update st_se_pwt_tickets_inv_? set status = ?, verify_by_user=?, verify_by_org=?, channel = ?, interface = ?, party_org_id = ?, date = ?  where game_id = ? and ticket_nbr = ?";
			PreparedStatement upTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			upTktInvPstmt.setInt(1, gameNbr);
			upTktInvPstmt.setString(2, status);
			upTktInvPstmt.setInt(3, userId);
			upTktInvPstmt.setInt(4, userOrgId);
			upTktInvPstmt.setString(5, channel);
			upTktInvPstmt.setString(6, interfaceType);
			upTktInvPstmt.setInt(7, partyOrgId);
			upTktInvPstmt.setTimestamp(8, new Timestamp(new java.util.Date()
					.getTime()));
			upTktInvPstmt.setInt(9, gameId);
			upTktInvPstmt.setString(10, tktNo);
			tktRow = upTktInvPstmt.executeUpdate();
			logger.debug("total row = " + tktRow
					+ "   ,update ticket inventory status table = "
					+ upTktInvPstmt);
			if (tktRow < 1) {
				throw new LMSException("ticket Number " + tktNo
						+ " status not updated in st_pwt_tickets_inv table.");
			}
			if (tktRow > 1) {
				LMSExceptionInterceptor.sendMail("Ticket Number " + tktNo
						+ "is duplicate in DATABASE");
				throw new LMSException("Ticket Number " + tktNo
						+ "is duplicate in DATABASE");
			}
		} else if ("insert".equalsIgnoreCase(updateType)) { // if ticket not
			// exist in
			// st_pwt_tickets_inv
			// table
			String updateTktInvQuery = "insert into  st_se_pwt_tickets_inv_? ( ticket_nbr , game_id , book_nbr , status , verify_by_user , verify_by_org, channel, interface, party_org_id, date ) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement inTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			inTktInvPstmt.setInt(1, gameNbr);
			inTktInvPstmt.setString(2, tktNo);
			inTktInvPstmt.setInt(3, gameId);
			inTktInvPstmt.setString(4, bookNo);
			inTktInvPstmt.setString(5, status);
			inTktInvPstmt.setInt(6, userId);
			inTktInvPstmt.setInt(7, userOrgId);
			inTktInvPstmt.setString(8, channel);
			inTktInvPstmt.setString(9, interfaceType);
			inTktInvPstmt.setInt(10, partyOrgId);
			inTktInvPstmt.setTimestamp(11, new Timestamp(new java.util.Date()
					.getTime()));
			tktRow = inTktInvPstmt.executeUpdate();
			logger.debug("total row = " + tktRow
					+ "   ,insert ticket inventory details into table = "
					+ inTktInvPstmt);
			if (tktRow < 1) {
				throw new LMSException("ticket Number " + tktNo
						+ " is not inserted in st_pwt_tickets_inv table.");
			}
		} else if ("delete".equalsIgnoreCase(updateType)) { // in case of ticket
			// temporary
			// cancellation
			String updateTktInvQuery = "delete from st_se_pwt_tickets_inv_? where game_id=? and ticket_nbr=? ";
			PreparedStatement inTktInvPstmt = connection
					.prepareStatement(updateTktInvQuery);
			inTktInvPstmt.setInt(1, gameNbr);
			inTktInvPstmt.setInt(2, gameId);
			inTktInvPstmt.setString(3, tktNo);
			tktRow = inTktInvPstmt.executeUpdate();
			logger.debug("total row = " + tktRow
					+ "   ,delete form ticket inventory  table = "
					+ inTktInvPstmt);
			if (tktRow < 1) {
				throw new LMSException("ticket Number " + tktNo
						+ " is not deleted from st_pwt_tickets_inv table.");
			}
		} else {
			throw new LMSException(
					"Exception occured while updating ticket inventory table updation type is :: "
							+ updateType);
		}

		return tktRow;

	}

	public Boolean updateVirnStatus(int gameNbr, String pwtStatus, int gameId,
			String encVirnCode, Connection con, String encTktNbr)
			throws SQLException, LMSException {
		String updateVirnInvQuery = "update st_se_pwt_inv_? set status = ? where game_id = ? and virn_code = ? and id1=?";
		PreparedStatement invPstmt = con.prepareStatement(updateVirnInvQuery);
		invPstmt.setInt(1, gameNbr);
		invPstmt.setString(2, pwtStatus);
		invPstmt.setInt(3, gameId);
		invPstmt.setString(4, encVirnCode);
		invPstmt.setString(5, encTktNbr);
		int virnRow = invPstmt.executeUpdate();
		logger.debug("total row = " + virnRow
				+ "   ,update ticket inventory status table = " + invPstmt);
		if (virnRow < 1) {
			throw new LMSException(
					"update ticket inventory status table not updated.");
		}
		return virnRow > 0;
	}

	public String verifyOrgForUnClaimedVirn(int orgId, String orgType,
			String enVirnCode, String virnStatus, int gameId,
			Connection connection) throws SQLException {
		String flag = "NONE";
		String fetchVirnDetailQuery = "";
		if ("RETAILER".equalsIgnoreCase(orgType)) {
			fetchVirnDetailQuery = "select virn_code,'RETAILER' as tableType from st_se_retailer_pwt where game_id = ? and virn_code = ? and retailer_org_id = ? and status = ? UNION select virn_code, 'PLAYER' as tableType from st_se_retailer_direct_player_pwt where game_id = ? and virn_code = ? and retailer_org_id = ? and pwt_claim_status = ?";
			// fetchVirnDetailQuery = "select * from st_se_retailer_pwt where
			// game_id = ? and virn_code = ? and retailer_org_id = ? and status
			// = ?";
		} else if ("AGENT".equalsIgnoreCase(orgType)) {

			fetchVirnDetailQuery = "select virn_code,'AGENT' as tableType from st_se_agent_pwt where game_id = ? and virn_code = ? and agent_org_id = ? and status = ? UNION select virn_code, 'PLAYER' as tableType from st_se_agt_direct_player_pwt where game_id = ? and virn_code = ? and agent_org_id = ? and pwt_claim_status=?";
			// fetchVirnDetailQuery = "select * from st_se_agent_pwt where
			// game_id = ? and virn_code = ? and agent_org_id = ? and status =
			// ?";
		}
		pstmt = connection.prepareStatement(fetchVirnDetailQuery);
		pstmt.setInt(1, gameId);
		pstmt.setString(2, enVirnCode);
		pstmt.setInt(3, orgId);
		pstmt.setString(4, virnStatus);
		pstmt.setInt(5, gameId);
		pstmt.setString(6, enVirnCode);
		pstmt.setInt(7, orgId);
		pstmt.setString(8, virnStatus);
		result = pstmt.executeQuery();
		logger.debug(" get detail of virn from " + orgType
				+ " pwt table pstmt = " + pstmt);
		if (result.next()) {
			if ("PLAYER".equalsIgnoreCase(result.getString("tableType"))) {
				flag = "IN_PLR_UNCLM";
			} else {
				flag = "IN_" + orgType + "_UNCLM";
			}
		} else {
			// we require to shoot a mail
			logger.debug("status not matched in " + orgType
					+ " pwt table and st_pwt_inv table for virn = "
					+ enVirnCode);
			// CommonMethods.sendMail("status not matched in "+orgType+" pwt
			// table and st_pwt_inv table for virn = "+enVirnCode);
		}
		return flag;
	}

	public Map<Long, GameMasterLMSBean> fetchOrgCommRates(int orgId,
			String orgType, int parentOrgId, List<String> qryList)
			throws SQLException {
		Map<Long, GameMasterLMSBean> tansIdMap = new HashMap<Long, GameMasterLMSBean>();
		GameMasterLMSBean commBean = null;

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		if ("RETAILER".equalsIgnoreCase(orgType)) {
			pstmt = con.prepareStatement(qryList.get(0).replaceAll("\\?",
					orgId + "")); // Sale Qry
			rs = pstmt.executeQuery();
			long transId;
			while (rs.next()) {
				transId = rs.getLong("transaction_id");

				commBean = new GameMasterLMSBean();
				tansIdMap.put(transId, commBean);
				commBean.setRetSaleCommRate(0.0);
				commBean.setAgentSaleCommRate(0.0);
				commBean.setGovtComm(0.0);

				PreparedStatement comPst = con.prepareStatement(qryList.get(2)); // Comm
				// Qry
				comPst.setInt(1, rs.getInt("game_id"));
				comPst.setInt(2, orgId);
				comPst.setInt(3, parentOrgId);
				comPst.setInt(4, orgId);
				comPst.setInt(5, parentOrgId);
				comPst.setInt(6, rs.getInt("game_id"));
				ResultSet comRs = comPst.executeQuery();
				while (comRs.next()) {
					double retComm = comRs.getDouble("retSaleComm");
					double agtComm = comRs.getDouble("agtSaleComm");
					double govtComm = comRs.getDouble("govtComm");
					if (comRs.getTimestamp("date").getTime() > rs.getTimestamp(
							"transaction_date").getTime()) {
						if (retComm != 0.0) {
							commBean.setRetSaleCommRate(retComm);
						}
						if (agtComm != 0.0) {
							commBean.setAgentSaleCommRate(agtComm);
						}
						if (govtComm != 0.0) {
							commBean.setGovtComm(govtComm);
						}
					}
				}
			}

			pstmt = con.prepareStatement(qryList.get(1).replaceAll("\\?",
					orgId + "")); // Refund Qry
			rs = pstmt.executeQuery();

			while (rs.next()) {
				transId = rs.getLong("transaction_id");

				commBean = new GameMasterLMSBean();
				tansIdMap.put(transId, commBean);
				commBean.setRetSaleCommRate(0.0);
				commBean.setAgentSaleCommRate(0.0);
				commBean.setGovtComm(0.0);

				PreparedStatement comPst = con.prepareStatement(qryList.get(2)); // Comm
				// Qry
				comPst.setInt(1, rs.getInt("game_id"));
				comPst.setInt(2, orgId);
				comPst.setInt(3, parentOrgId);
				comPst.setInt(4, orgId);
				comPst.setInt(5, parentOrgId);
				comPst.setInt(6, rs.getInt("game_id"));
				ResultSet comRs = comPst.executeQuery();
				while (comRs.next()) {
					double retComm = comRs.getDouble("retSaleComm");
					double agtComm = comRs.getDouble("agtSaleComm");
					double govtComm = comRs.getDouble("govtComm");
					if (comRs.getTimestamp("date").getTime() > rs.getTimestamp(
							"transaction_date").getTime()) {
						if (retComm != 0.0) {
							commBean.setRetSaleCommRate(retComm);
						}
						if (agtComm != 0.0) {
							commBean.setAgentSaleCommRate(agtComm);
						}
						if (govtComm != 0.0) {
							commBean.setGovtComm(govtComm);
						}
					}

				}
			}
		} else if ("AGENT".equalsIgnoreCase(orgType)) {

			String saleQryAgent = "select a.transaction_id , game_id,b.transaction_date,agent_org_id from st_dg_agt_sale a, st_lms_agent_transaction_master b  where agent_org_id=? and claim_status='CLAIM_BAL' and a.transaction_id = b.transaction_id";

			String saleRefundQryAgent = "select a.transaction_id , game_id,b.transaction_date,agent_org_id from st_dg_agt_sale_refund a, st_lms_agent_transaction_master b  where agent_org_id=? and claim_status='CLAIM_BAL' and a.transaction_id = b.transaction_id";

			String commQry = "select game_id,agtSaleComm,govtComm,date from (select game_id,sum(agent_sale_comm_rate) agtSaleComm,sum(govt_comm) govtComm,now() 'date' from (select game_id,agent_sale_comm_rate,govt_comm from st_dg_game_master where game_id=? union all select game_id,sale_comm_variance,0 from st_dg_bo_agent_sale_pwt_comm_variance where agent_org_id=?) comm group by game_id union all select gm.game_id,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where agent_org_id=? union all select game_id,0,govt_comm_rate,date_changed from st_dg_game_govt_comm_history where game_id=?) comm order by date desc";

			pstmt = con.prepareStatement(saleQryAgent);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			long transId;
			while (rs.next()) {
				transId = rs.getLong("transaction_id");

				commBean = new GameMasterLMSBean();
				tansIdMap.put(transId, commBean);
				commBean.setAgentSaleCommRate(0.0);
				commBean.setGovtComm(0.0);

				PreparedStatement comPst = con.prepareStatement(commQry);
				comPst.setInt(1, rs.getInt("game_id"));
				comPst.setInt(2, orgId);
				comPst.setInt(3, orgId);
				comPst.setInt(4, rs.getInt("game_id"));
				ResultSet comRs = comPst.executeQuery();
				while (comRs.next()) {
					double agtComm = comRs.getDouble("agtSaleComm");
					double govtComm = comRs.getDouble("govtComm");
					if (comRs.getTimestamp("date").getTime() > rs.getTimestamp(
							"transaction_date").getTime()) {
						if (agtComm != 0.0) {
							commBean.setAgentSaleCommRate(agtComm);
						}
						if (govtComm != 0.0) {
							commBean.setGovtComm(govtComm);
						}
					}

				}
			}

			pstmt = con.prepareStatement(saleRefundQryAgent);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				transId = rs.getLong("transaction_id");

				commBean = new GameMasterLMSBean();
				tansIdMap.put(transId, commBean);
				commBean.setAgentSaleCommRate(0.0);
				commBean.setGovtComm(0.0);

				PreparedStatement comPst = con.prepareStatement(commQry);
				comPst.setInt(1, rs.getInt("game_id"));
				comPst.setInt(2, orgId);
				comPst.setInt(3, orgId);
				comPst.setInt(4, rs.getInt("game_id"));
				ResultSet comRs = comPst.executeQuery();
				while (comRs.next()) {
					double agtComm = comRs.getDouble("agtSaleComm");
					double govtComm = comRs.getDouble("govtComm");
					if (comRs.getTimestamp("date").getTime() > rs.getTimestamp(
							"transaction_date").getTime()) {
						if (agtComm != 0.0) {
							commBean.setAgentSaleCommRate(agtComm);
						}
						if (govtComm != 0.0) {
							commBean.setGovtComm(govtComm);
						}
					}

				}
			}
		}

		return tansIdMap;
	}

	public List<String> createQryForUpdLedger() {
		List<String> qryList = new ArrayList<String>();

		Set<Integer> gameSet = Util.getGameMap().keySet();
		StringBuilder saleQry = new StringBuilder("");
		StringBuilder saleRefundQry = new StringBuilder("");
		
		for (Integer gameId : gameSet) {
			
			saleQry
					.append("select a.transaction_id,a.game_id ,transaction_date,a.retailer_org_id from st_dg_ret_sale_"
							+ gameId
							+ " a, st_lms_retailer_transaction_master b  where claim_status='CLAIM_BAL' and a.transaction_id = b.transaction_id and a.retailer_org_id=? union all ");

			saleRefundQry
					.append("select a.transaction_id,a.game_id ,transaction_date,a.retailer_org_id from st_dg_ret_sale_refund_"
							+ gameId
							+ " a, st_lms_retailer_transaction_master b  where claim_status='CLAIM_BAL' and a.transaction_id = b.transaction_id and a.retailer_org_id=? union all ");
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleRefundQry.delete(saleRefundQry.length() - 10, saleRefundQry
				.length());

		String commQry = "select game_id,retSaleComm,agtSaleComm,govtComm,date from (select game_id,sum(retailer_sale_comm_rate) retSaleComm,sum(agent_sale_comm_rate) agtSaleComm,sum(govt_comm) govtComm,now() 'date' from (select game_id,retailer_sale_comm_rate,agent_sale_comm_rate,govt_comm from st_dg_game_master where game_id=? union all select game_id,sale_comm_variance,0,0 from st_dg_agent_retailer_sale_pwt_comm_variance where retailer_org_id=? union all select game_id,0,sale_comm_variance,0 from st_dg_bo_agent_sale_pwt_comm_variance where agent_org_id=?) comm group by game_id union all select gm.game_id,retailer_sale_comm_rate+sale_comm_variance retComm,0 agtComm,0 govtComm,date_changed from st_dg_agent_retailer_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where retialer_org_id=? union all select gm.game_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where agent_org_id=? union all select game_id,0,0,govt_comm_rate,date_changed from st_dg_game_govt_comm_history where game_id=?) comm order by date desc";
		qryList.add(saleQry.toString());
		qryList.add(saleRefundQry.toString());
		qryList.add(commQry);
		return qryList;

	}
/**
 * 
 * change due to org Code implementation 
 * @param userOrgId
 * @return
 */
	public static List<String> chkStatusAndFetchUsers(int userOrgId) {
		String orgStatus = null;
		
		Connection con = DBConnect.getConnection();
		List<String> userNameList = new ArrayList<String>();

		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT organization_status FROM st_lms_organization_master WHERE organization_id = ?");
			pstmt.setInt(1, userOrgId);
			logger.debug("org status query : " + pstmt + "*********");
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				orgStatus = rs.getString("organization_status");
				
			}
			if ("INACTIVE".equalsIgnoreCase(orgStatus)) {
				pstmt = con
						.prepareStatement("SELECT user_name FROM st_lms_user_master WHERE organization_id = ?");
				pstmt.setInt(1, userOrgId);
				logger.debug("user lists query : " + pstmt + "*********");
				rs = pstmt.executeQuery();

				while (rs.next()) {
					userNameList.add(rs.getString("user_name"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e);
		} finally {
			DBConnect.closeCon(con);
		}
		return userNameList;
	}

	public static boolean isSessionValid(HttpSession session) {
		boolean isCheck = true;
		if (session == null) {
			isCheck = false;
		} else {
			try {
				session.getAttribute("");
			} catch (IllegalStateException ex) {
				System.out.println(ex.getMessage());
				if (ex.getMessage().contains("Session already invalidated")) {
					isCheck = false;
				}
			}
		}
		return isCheck;
	}
	public static ArrayList<String> getTerminalTypeList(boolean type) {
		ArrayList<String> terminalList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select sim_name from st_lms_con_device_master order by id");
			if(false){
			while (rs.next()) {
				terminalList.add(rs.getString("sim_name"));
			}
			} else{
				while (rs.next()) {
					terminalList.add(rs.getString("sim_name").toUpperCase());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return terminalList;
	}
	
	public static ArrayList<String> getTerminalBulidVersionList(String deviceName) {
		ArrayList<String> versionList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select vm.device_id,version from st_lms_htpos_version_master vm inner join st_lms_htpos_device_master dm on vm.device_id=dm.device_id where device_type='"+deviceName+"'");
			while (rs.next()) {
				versionList.add(rs.getString("version"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return versionList;
	}
	public static ArrayList<String> getTerminalList() {
		ArrayList<String> terList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select device_type from st_lms_htpos_device_master");
			while (rs.next()) {
				terList.add(rs.getString("device_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return terList;
	}
	public static List<String> getServiceList(){
		List<String> list = new LinkedList<String>();
		if(LMSFilterDispatcher.getIsDraw().equals("YES")){
			list.add("Draw Game");
			list.add("Draw Game Sale");
			list.add("Draw Game Pwt");
		}
		if(LMSFilterDispatcher.getIsScratch().equals("YES")){
			list.add("Scratch Game");
			list.add("Scratch Game Sale");
			list.add("Scratch Game Pwt");
		}
		if(LMSFilterDispatcher.getIsOLA().equals("YES")){
			list.add("Offline Affiliates");
			list.add("Offline Affiliates Deposit");
			list.add("Offline Affiliates Withdrawal");
		}
		if(LMSFilterDispatcher.getIsCS().equals("YES")){
			list.add("Commercial Service");
			list.add("Commercial Service Sale");
		}if(LMSFilterDispatcher.getIsSLE().equals("YES")){
			list.add("Sports Lottery");
			list.add("Sports Lottery Sale");
			list.add("Sports Lottery Pwt");
		}
		if(LMSFilterDispatcher.getIsIW().equals("YES")){
			list.add("Instant Win");
			list.add("Instant Win Sale");
			list.add("Instant Win Pwt");
		}
		if(LMSFilterDispatcher.getIsVS().equals("YES")){
			list.add("Virtual Sports");
			list.add("Virtual Sports Sale");
			list.add("Virtual Sports Pwt");
		}

		list.add("Login");
		list.add("HeartBeat");
		list.add("Total");
			return list;
		} 
	public static String getLongitudeLatitude(String address) {
		String strLatitude = "0.000000";
		String strLongtitude ="0.000000";
		try {
			StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
			if (StringUtils.isNotBlank(address)) {
				urlBuilder.append("&address=").append(
						URLEncoder.encode(address, "UTF-8"));
			}
			final GetMethod getMethod = new GetMethod(urlBuilder.toString());
			try {
				httpClient.executeMethod(getMethod);
				Reader reader = new InputStreamReader(getMethod
						.getResponseBodyAsStream(), getMethod
						.getResponseCharSet());
				int data = reader.read();
				char[] buffer = new char[1024];
				Writer writer = new StringWriter();
				while ((data = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, data);
				}

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader("<"
						+ writer.toString().trim()));
				Document doc = db.parse(is);
				strLatitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lat/text()");
				strLongtitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lng/text()");
				if(strLatitude == null)
					strLatitude = "0.000000";
				if(strLongtitude == null)
					strLongtitude = "0.000000";
			} finally {
				getMethod.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strLatitude + ":" + strLongtitude;
	}

	private static String getXpathValue(Document doc, String strXpath)
			throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(strXpath);
		String resultData = null;
		Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result4;
		for (int i = 0; i < nodes.getLength(); i++) {
			resultData = nodes.item(i).getNodeValue();
		}
		return resultData;
	}
	public static List<String> getEmailIdsForDrawMgmtNotification(){
		Connection con =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		List<String> emailList = new ArrayList<String>();
		try{
			con =DBConnect.getConnection();
			String query ="select email_id from st_lms_report_email_priviledge_rep a inner join st_lms_report_email_priv_master  b on a.email_pid =b.email_pid " +
							" inner join st_lms_report_email_user_master c on b.user_id=c.ref_user_id  where  priv_title ='DrawMgmtNotification' and a.status='ACTIVE' and b.status='ACTIVE' and c.status='ACTIVE'";
			pstmt = con.prepareStatement(query);
			rs =pstmt.executeQuery();
			while(rs.next()){
				
				emailList.add(rs.getString("email_id"));
			}
			return emailList;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
public static String  getStateListWithCode(String countryCode){

	StringBuilder listWithCode = new StringBuilder();
	PreparedStatement pstmt = null;
	Connection con = DBConnect.getConnection();
	ResultSet rs = null;
	try {
		pstmt = con.prepareStatement("select state_code,name from st_lms_state_master where country_code=? and status=?");
		pstmt.setString(1,countryCode);
		pstmt.setString(2,"ACTIVE");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			listWithCode.append(rs.getString("state_code") + ":"
					+ rs.getString("name")+"|");

		}

	} catch (Exception e) {
		e.printStackTrace();

	} finally {

		try {
			if (con != null) {
				con.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	return listWithCode.toString();

}
public static String  getCityListWithCode(String countryCode,String stateCode){

	StringBuilder listWithCode = new StringBuilder();
	PreparedStatement pstmt = null;
	Connection con = DBConnect.getConnection();
	ResultSet rs = null;
	try {
		pstmt = con.prepareStatement("select city_code,city_name from st_lms_city_master where state_code=? and country_code=? and status=? ");
		pstmt.setString(1,stateCode);
		pstmt.setString(2,countryCode);
		pstmt.setString(3,"ACTIVE");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			listWithCode.append(rs.getString("city_code") + ":"
					+ rs.getString("city_name")+"|");

		}

	} catch (Exception e) {
		e.printStackTrace();

	} finally {

		try {
			if (con != null) {
				con.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	return listWithCode.toString();

}
public static String  getAreaListWithCode(String countryCode,String stateCode,String cityCode){

	StringBuilder listWithCode = new StringBuilder();
	PreparedStatement pstmt = null;
	Connection con = DBConnect.getConnection();
	ResultSet rs = null;
	try {
		//pstmt = con.prepareStatement("select area_code,area_name from st_lms_area_master where city_code=? and state_code=? and country_code=? and status=? ");
		pstmt = con.prepareStatement("select area_code,area_name from st_lms_area_master am,st_lms_city_master ctm,st_lms_state_master sm,st_lms_country_master cm where am.city_code=ctm.city_code and am.state_code=sm.state_code and am.country_code=cm.country_code and ctm.city_name=? and sm.name=? and  cm.name=? and  am.status=?");
		pstmt.setString(1,cityCode);
		pstmt.setString(2,stateCode);
		pstmt.setString(3,countryCode);
		pstmt.setString(4,"ACTIVE");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			listWithCode.append(rs.getString("area_code") + ":"
					+ rs.getString("area_name")+"|");

		}

	} catch (Exception e) {
		e.printStackTrace();

	} finally {

		try {
			if (con != null) {
				con.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	return listWithCode.toString();

}
public static String  getDivisionListWithCode(String countryCode,String stateCode,String cityCode,String areaCode ){

	StringBuilder listWithCode = new StringBuilder();
	PreparedStatement pstmt = null;
	Connection con = DBConnect.getConnection();
	ResultSet rs = null;
	try {
		//pstmt = con.prepareStatement("select area_code,area_name from st_lms_area_master where city_code=? and state_code=? and country_code=? and status=? ");
		pstmt = con.prepareStatement("select division_code,division_name from st_lms_division_master dm,st_lms_area_master am,st_lms_city_master ctm,st_lms_state_master sm,st_lms_country_master cm where dm.area_code=am.area_code and am.city_code=ctm.city_code and am.state_code=sm.state_code and am.country_code=cm.country_code and am.area_code=? and ctm.city_name=? and sm.name=? and  cm.name=? and  dm.status=?");
		pstmt.setString(1,areaCode);
		pstmt.setString(2,cityCode);
		pstmt.setString(3,stateCode);
		pstmt.setString(4,countryCode);
		pstmt.setString(5,"ACTIVE");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			listWithCode.append(rs.getString("division_code") + ":"
					+ rs.getString("division_name")+"|");

		}

	} catch (Exception e) {
		e.printStackTrace();

	} finally {

		try {
			if (con != null) {
				con.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	return listWithCode.toString();

}


}