package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.GameWisePWTBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.FormatNumber;

public class DGPwtReportsHelper {

	public static void main(String[] args) {

	}

	private Connection con = null;
	private Date endDate;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private Date startDate;

	public DGPwtReportsHelper(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
	}

	private PwtReportBean getPlayerPwtDetail(Connection conn) {
		PwtReportBean bean = null;
		try {

			PreparedStatement pst = conn
					.prepareStatement("select ifnull(sum(pwt_amt),0) total_pwt_amt from st_dg_bo_direct_plr_pwt where  transaction_date>=? and transaction_date<?");
			pst.setDate(1, startDate);
			pst.setDate(2, endDate);
			ResultSet rss = pst.executeQuery();
			System.out.println("get Player PWT query : == " + pst);
			if (rss.next()) {
				bean = new PwtReportBean();
				bean.setPartyName("Player PWT");
				bean.setPwtAmt(FormatNumber.formatNumber(rss
						.getDouble("total_pwt_amt")));
				if (rss.getDouble("total_pwt_amt") > 0) {
					return bean;
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<PwtReportBean> getPwtDetailAGTWise() {

		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		con = DBConnect.getConnection();

		// used to add a row of total PWT amount Claimed by Players
		if ((reportbean = getPlayerPwtDetail(con)) != null) {
			list.add(reportbean);
		}

		try {

			// pstmt=con.prepareStatement(QueryManager.getST_PWT_REPORT_BO());
			String getPwtDetailsForBo = "select ifnull(sum(pwt_amt),0) total_pwt_amt, agent_org_id, "
					+ " name from st_dg_bo_pwt bpwt, st_lms_organization_master  where bpwt.transaction_id"
					+ " in (select btm.transaction_id from st_lms_bo_transaction_master btm where ( "
					+ " transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO')and ( "
					+ " btm.transaction_date>=? and btm.transaction_date<?)) and organization_id = "
					+ " agent_org_id group by agent_org_id order by name";
			pstmt = con.prepareStatement(getPwtDetailsForBo);
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			rs = pstmt.executeQuery();
			System.out.println(" get pwt details query- ==== -" + pstmt);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				reportbean.setPwtAmt(FormatNumber.formatNumber(rs
						.getDouble("total_pwt_amt")));
				reportbean.setPartyName(rs.getString("name"));
				reportbean.setPartyId(rs.getInt("agent_org_id"));

				if (rs.getDouble("total_pwt_amt") > 0) {
					list.add(reportbean);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * It is called when BO want to get the PWT details 'Game Wise'
	 * 
	 * @param gameStatus
	 * @return List<GameWisePWTBean>
	 */

	public List<GameWisePWTBean> getPwtDetailGameWise(String gameStatus) {

		List<GameWisePWTBean> list = new ArrayList<GameWisePWTBean>();
		Connection conn = DBConnect.getConnection();

		try {
			// get the details of games based on the game status
			Map<Integer, GameBean> gameMap = new TreeMap<Integer, GameBean>();
			Map<Integer, String> totalPwtMap = new TreeMap<Integer, String>();
			String query = "select game_id, game_nbr, game_name from st_dg_game_master ";
			pstmt = conn.prepareStatement(query);
			ResultSet rss = pstmt.executeQuery();
			System.out.println("get Game id query :  " + pstmt);
			GameBean gbean = null;
			while (rss.next()) {
				gbean = new GameBean();
				int id = rss.getInt("game_id");
				String name = rss.getString("game_name");
				int gameNbr = rss.getInt("game_nbr");
				gbean.setGameId(id);
				gbean.setGameName(name);
				gbean.setGameNbr(gameNbr);
				gameMap.put(id, gbean);

				/*
				 * //get the details of total pwt of game wise
				 * query=QueryManager.getST_BO_GAME_WISE_TOTAL_PWT_DETAILS();
				 * PreparedStatement gstmt=conn.prepareStatement(query);
				 * gstmt.setInt(1, gameNbr); gstmt.setInt(2, id); ResultSet
				 * rs=gstmt.executeQuery(); System.out.println("total pwt
				 * details query == "+pstmt); while(rs.next()) { String
				 * playerPwt=FormatNumber.formatNumber(rs.getDouble("TOTAL_PWT"));
				 * int gameId=rs.getInt("game_id"); totalPwtMap.put(gameId,
				 * playerPwt); } //System.out.println(" Total pwt details
				 * "+totalPwtMap);
				 * 
				 * rs.close(); gstmt.close();
				 */

			}
			// System.out.println("game id details : "+gameMap);
			rss.close();
			pstmt.close();

			// get the details of pwt claimed by agent
			Map<Integer, String> agentMap = new TreeMap<Integer, String>();
			query = "select ifnull(sum(pwt_amt),0) total_pwt_amt, game_id from st_dg_bo_pwt bpwt  where bpwt.transaction_id in (select btm.transaction_id from st_lms_bo_transaction_master btm where (  transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO')and (  btm.transaction_date>=? and btm.transaction_date<?))  group by game_id ";
			pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, this.startDate);
			pstmt.setDate(2, this.endDate);

			rss = pstmt.executeQuery();
			// System.out.println("agent pwt details query == "+pstmt);
			while (rss.next()) {
				String agentPwt = FormatNumber.formatNumber(rss
						.getDouble("total_pwt_amt"));
				int gameId = rss.getInt("game_id");
				agentMap.put(gameId, agentPwt);
			}
			// System.out.println(" agent pwt details "+agentMap);
			rss.close();
			pstmt.close();

			// get the details of pwt claimed by Player
			Map<Integer, String> playerMap = new TreeMap<Integer, String>();
			pstmt = null;
			rss = null;
			query = "select ifnull(sum(pwt_amt),0) 'player_pwt', game_id from st_dg_bo_direct_plr_pwt where  transaction_date>=? and transaction_date<? group by game_id";
			pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, this.startDate);
			pstmt.setDate(2, this.endDate);
			rss = pstmt.executeQuery();
			// System.out.println("Player pwt details query == "+pstmt);
			while (rss.next()) {
				String playerPwt = FormatNumber.formatNumber(rss
						.getDouble("player_pwt"));
				int gameId = rss.getInt("game_id");
				playerMap.put(gameId, playerPwt);
			}
			// System.out.println(" Player pwt details "+playerMap);
			rss.close();
			pstmt.close();

			/*
			 * //get the details of pwt claimed by Retailer Map<Integer,
			 * String> retMap= new TreeMap<Integer, String>(); pstmt=null;
			 * rss=null;
			 * query=QueryManager.getST_BO_GAME_WISE_PWT_RET_DETAILS();
			 * pstmt=conn.prepareStatement(query); pstmt.setDate(1,
			 * this.startDate); pstmt.setDate(2, this.endDate);
			 * if(!"ALL".equalsIgnoreCase(gameStatus.trim())) pstmt.setString(3,
			 * gameStatus.trim()+"%"); else pstmt.setString(3, "%");
			 * rss=pstmt.executeQuery(); //System.out.println("ret pwt details
			 * query == "+pstmt); while(rss.next()) { String
			 * retPwt=FormatNumber.formatNumber(rss.getDouble("ret_pwt")); int
			 * gameId=rss.getInt("game_id"); retMap.put(gameId, retPwt); }
			 * //System.out.println(" ret pwt details "+retMap); rss.close();
			 * pstmt.close();
			 */

			Set<Integer> gameIdSet = gameMap.keySet();
			for (Integer id : gameIdSet) {
				// String agentPWT=(agentMap.get(id)!=null)?;
				GameBean gameBean = gameMap.get(id);
				String playerPWT = playerMap.get(id);
				// String retPWT=retMap.get(id);
				String agentPWT = agentMap.get(id);
				String totalPWT = totalPwtMap.get(id);
				if (playerPWT == null) {
					playerPWT = "0.00";
				}
				// if(retPWT==null) retPWT="0.00";
				if (agentPWT == null) {
					agentPWT = "0.00";
				}
				if (totalPWT == null) {
					totalPWT = "0.00";
				}

				// set the bean to be display on the jsp
				GameWisePWTBean bean = new GameWisePWTBean();
				bean.setGameNbr(gameBean.getGameNbr());
				bean.setGameName(gameBean.getGameName());
				bean.setAgentPWT(FormatNumber.formatNumberForJSP(agentPWT));
				bean.setPlayerPWT(FormatNumber.formatNumberForJSP(playerPWT));
				// bean.setRetPWT(FormatNumber.formatNumberForJSP(retPWT));
				bean.setTotalPWT(FormatNumber.formatNumberForJSP(totalPWT));
				double sumpwtAtBo = Double.parseDouble(agentPWT
						.replace(",", ""))
						+ Double.parseDouble(playerPWT.replace(",", ""));
				bean.setSumPwtAtBo(FormatNumber.formatNumberForJSP(sumpwtAtBo));

				list.add(bean);
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

		// System.out.println(list);
		return list;

	}

}
