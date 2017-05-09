package com.skilrock.lms.coreEngine.instantPrint.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skilrock.ipe.Bean.GameLMSBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.beans.GameInfoBean;

/**
 * @author vaibhav
 * 
 */
public class IPEUtility {
	public static Map<Integer, GameLMSBean> gameMap = null;
	public static Map<Integer, Map<Integer, GameInfoBean>> orgWiseGameInfo = null;

	public static Map<Integer, Map<Integer, GameInfoBean>> getOrgWiseGameInfo() {
		return orgWiseGameInfo;
	}

	public static void setOrgWiseGameInfo(
			Map<Integer, Map<Integer, GameInfoBean>> orgWiseGameInfo) {
		IPEUtility.orgWiseGameInfo = orgWiseGameInfo;
	}

	public static GameInfoBean fetchOrgGameComm(int orgId, int gameId) {

		return (orgWiseGameInfo.get(orgId) != null) ? orgWiseGameInfo
				.get(orgId).get(gameId) : orgWiseGameInfo.get(0).get(gameId);
	}

	public static int getGameId(int gameNo) {
		Iterator<Map.Entry<Integer, GameLMSBean>> gameNumItr = gameMap.entrySet()
				.iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameLMSBean> gameNumpair = gameNumItr.next();
			if (gameNo == gameNumpair.getValue().getGameNo()) {
				return gameNumpair.getKey();
			}
		}
		return 0;
	}

	public static String getGameName(int gameNo) {
		return gameMap.get(getGameId(gameNo)).getGameName();
	}

	public static int getGameNo(int gameId) {
		return gameMap.get(gameId).getGameNo();
	}

	public static double getGameTktPrice(int gameNo) {
		return gameMap.get(getGameId(gameNo)).getTicketPrice();
	}

	public static List<GameLMSBean> fetchNewGameMap() {
		List<GameLMSBean> newGameList = new ArrayList<GameLMSBean>();
		Calendar cal = Calendar.getInstance();
		Timestamp startDate = null;
		Iterator<Map.Entry<Integer, GameLMSBean>> gameNumItr = gameMap.entrySet()
				.iterator();

		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameLMSBean> gameNumpair = gameNumItr.next();
			GameLMSBean gameBean = gameNumpair.getValue();
			startDate = gameBean.getStartDate();
			if (startDate != null) {
				if ("NEW".equals(gameBean.getGameStatus())
						&& startDate.getTime() <= cal.getTimeInMillis()) {
					newGameList.add(gameBean);
				}
			}
		}
		return newGameList;
	}

	public static Map<Integer, GameLMSBean> getGameMap() {
		return gameMap;
	}

	public static void setGameMap(Map<Integer, GameLMSBean> gameMap) {
		IPEUtility.gameMap = gameMap;
	}

	public static int fetchGameNoFrmTicket(String ticketNo, int digitInGameNo) {
		int gameNo = Integer.parseInt(ticketNo.substring(0, digitInGameNo));
		if (getGameId(gameNo) != 0) {
			return gameNo;
		}
		return 0;
	}
	
	public static String fetchLastIPETransId(int retOrgId) throws LMSException{
		String lastTransId = "";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = con.prepareStatement("select IPELastSaleTransId from st_lms_last_sale_transaction where organization_id = ?");
			pstmt.setInt(1, retOrgId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				lastTransId = rs.getString("IPELastSaleTransId");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return lastTransId;
	}
	
	public static void updateLastIPETransId(int retOrgId,long newTransId) throws LMSException{
		Connection con = DBConnect.getConnection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("update st_lms_last_sale_transaction set IPELastSaleTransId = "+ newTransId +" where organization_id = "+ retOrgId);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}
}
