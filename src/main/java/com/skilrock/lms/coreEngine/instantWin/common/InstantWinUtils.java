package com.skilrock.lms.coreEngine.instantWin.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;

import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.dataMgmt.controllerImpl.SportsLotteryDataControllerImpl;


public class InstantWinUtils {
	static Log logger = LogFactory.getLog(InstantWinUtils.class);

	
	public static Map<Integer,GameMasterBean> gameInfoMap=null; 
	public static Map<Integer,GameTypeMasterBean> gameTypeInfoMap=null; 
	
	
	private static final InstantWinUtils instance;

	static {
		instance = new InstantWinUtils();
		
		new SportsLotteryDataControllerImpl().getSportsLotteryOnStartServerData("WGRL");
		setLmsGameData();
	}

	public String convertJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

	public static void setLmsGameData(){
		Statement stmt=null;
		ResultSet rs=null;
		Connection con=null;
		try {
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			rs=stmt.executeQuery("select game_id,game_type_id,retailer_sale_comm_rate,retailer_pwt_comm_rate,agent_sale_comm_rate,agent_pwt_comm_rate from st_sle_game_type_master where type_status <> 'SALE_CLOSE'");
			while(rs.next()){
				GameTypeMasterBean gameTypeMasterBean=gameTypeInfoMap.get(rs.getInt("game_type_id"));
				if(gameTypeMasterBean != null){
					gameTypeMasterBean.setRetailetSaleComm(rs.getDouble("retailer_sale_comm_rate"));
					gameTypeMasterBean.setAgentSaleComm(rs.getDouble("agent_sale_comm_rate"));
					gameTypeMasterBean.setRetailetPwtComm(rs.getDouble("retailer_pwt_comm_rate"));
					gameTypeMasterBean.setAgentPwtComm(rs.getDouble("agent_pwt_comm_rate"));
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getGameIdFromGameNumber(int gameNumber) {
		Iterator<Map.Entry<Integer, GameMasterBean>> gameNumItr = gameInfoMap
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterBean> gameNumpair = gameNumItr
					.next();
			if(gameNumpair.getValue().getGameStatus().equals("SALE_OPEN")){
			if (gameNumber == gameNumpair.getValue().getGameNo()) {
				return gameNumpair.getValue().getGameId();
			}
			}
		}
		return 0;
	}
	
	public static int getGameIdFromGameName(String gameName) {
		Iterator<Map.Entry<Integer, GameMasterBean>> gameNumItr = gameInfoMap
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterBean> gameNumpair = gameNumItr
					.next();
			
			if (gameName.equalsIgnoreCase(gameNumpair.getValue().getGameDevName())) {
				return gameNumpair.getValue().getGameId();
			}
			
		}
		return 0;
	}
	
	public static int getGameTypeIdFromGameTypeName(String gameTypeName) {
		Iterator<Map.Entry<Integer, GameTypeMasterBean>> gameNumItr = gameTypeInfoMap
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameTypeMasterBean> gameNumpair = gameNumItr
					.next();
			
			if (gameTypeName.equalsIgnoreCase(gameNumpair.getValue().getGameTypeDevName())) {
				return gameNumpair.getValue().getGameTypeId();
			}
			
		}
		return 0;
	}
	
	
	public static Map<Integer, GameMasterBean> getGameMap(Connection connection) throws IWException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, GameMasterBean> gameMasterMap = null;
		GameMasterBean gameMasterBean = null;
		try {
			gameMasterMap = new LinkedHashMap<Integer, GameMasterBean>();
			stmt = connection.createStatement();
			query = "SELECT game_id, game_no, game_dev_name, game_disp_name, closing_time, display_order, game_status FROM st_sle_game_master WHERE game_status='SALE_OPEN' ORDER BY display_order;";
			logger.info("GameMasterQuery - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameMasterBean = new GameMasterBean();
				int gameId = rs.getInt("game_id");
				gameMasterBean.setGameId(gameId);
				gameMasterBean.setGameNo(rs.getInt("game_no"));
				gameMasterBean.setGameDevName(rs.getString("game_dev_name"));
				gameMasterBean.setGameDispName(rs.getString("game_disp_name"));
				gameMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameMasterBean.setGameStatus(rs.getString("game_status"));
				gameMasterBean.setGameTypeMasterList(getGameTypeList(gameId, connection));
				gameMasterMap.put(gameId, gameMasterBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameMasterMap;
	}

	public static List<GameTypeMasterBean> getGameTypeList(int gameId, Connection connection) throws IWException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		List<GameTypeMasterBean> gameTypeMasterList = null;
		GameTypeMasterBean gameTypeMasterBean = null;
		try {
			gameTypeMasterList = new ArrayList<GameTypeMasterBean>();
			stmt = connection.createStatement();
			query = "SELECT game_type_id, game_id, type_dev_name, type_disp_name, vat_amt, gov_comm_rate, retailer_sale_comm_rate, retailer_pwt_comm_rate, agent_sale_comm_rate agent_pwt_comm_rate, closing_time, display_order, type_status FROM st_sle_game_type_master WHERE game_id="+gameId+" AND type_status='SALE_OPEN' ORDER BY game_id, display_order;";
			logger.info("GameMasterQuery - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameTypeMasterBean = new GameTypeMasterBean();
				int gameTypeId = rs.getInt("game_type_id");
				gameTypeMasterBean.setGameTypeId(gameTypeId);
				gameTypeMasterBean.setGameId(gameId);
				gameTypeMasterBean.setGameTypeDevName(rs.getString("type_dev_name"));
				gameTypeMasterBean.setGameTypeDispName(rs.getString("type_disp_name"));
				gameTypeMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameTypeMasterBean.setGameTypeStatus(rs.getString("type_status"));
				gameTypeMasterList.add(gameTypeMasterBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameTypeMasterList;
	}

	public static void main(String[] args) throws Exception {
		Connection connection = DBConnect.getConnection();
		Map<Integer, GameMasterBean> gameMap = getGameMap(connection);
		//Map<Integer, Map<Integer, GameTypeMasterBean>> typeMap = getGameTypeMap(connection);
		System.out.println(gameMap);
		//System.out.println(typeMap);
	}

	public static synchronized InstantWinUtils getInstance() {
		return instance;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}