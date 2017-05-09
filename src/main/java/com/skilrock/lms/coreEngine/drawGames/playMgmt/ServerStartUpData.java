package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.beans.OrgDataBean;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEUtil;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.common.VSUtil;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;


public class ServerStartUpData {
	static Log logger = LogFactory.getLog(ServerStartUpData.class);
	public static void onStartGameData(ServletContext servletContext) {
		logger.debug("---- ENTERING INTO onStartGameData ----");
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DATA_MGMT);
		sReq.setServiceMethod(ServiceMethodName.ON_START_GAME_DATA);
	
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		
		sRes = delegate.getResponse(sReq);
		
		String responseString=sRes.getResponseData().toString();
		//Type elementType = new TypeToken<Map<Integer, GameMasterLMSBean>>() {}.getType();
		Type elementType = new TypeToken<Map<Integer, GameMasterLMSBean>>() {}.getType();
		
		
		Map<Integer, GameMasterLMSBean> lmsMap = null;
		Map<Integer, GameMasterLMSBean> dgMap = null;
		
		Connection con = DBConnect.getConnection();
	
	
		if (sRes.getIsSuccess()) {
			try {
				lmsMap = new HashMap<Integer, GameMasterLMSBean>();
				dgMap=new Gson().fromJson(responseString, elementType);
				
				String query2 = "select game_id,game_name_dev,agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,vat_amt,govt_comm,govt_comm_pwt,high_prize_amt,prize_payout_ratio,game_status,bonus_ball_enable from st_dg_game_master order by display_order";
	
				PreparedStatement pstmt2 = con.prepareStatement(query2);
				ResultSet rs2 = pstmt2.executeQuery();
				int gameId = 0;
		    	GameMasterLMSBean gameMasterLMSBean = null;
				while (rs2.next()) {
					gameId = rs2.getInt("game_id");
	
					logger.debug(gameId);
	
					gameMasterLMSBean = dgMap.get(gameId);
	             if(gameMasterLMSBean != null){
					gameMasterLMSBean.setAgentSaleCommRate(rs2
							.getDouble("agent_sale_comm_rate"));
					gameMasterLMSBean.setAgentPwtCommRate(rs2
							.getDouble("agent_pwt_comm_rate"));
					gameMasterLMSBean.setRetSaleCommRate(rs2
							.getDouble("retailer_sale_comm_rate"));
					gameMasterLMSBean.setRetPwtCommRate(rs2
							.getDouble("retailer_pwt_comm_rate"));
					gameMasterLMSBean.setVatAmount(rs2.getDouble("vat_amt"));
					gameMasterLMSBean.setGovtComm(rs2.getDouble("govt_comm"));
					gameMasterLMSBean.setGovtCommPwt(rs2.getDouble("govt_comm_pwt"));
					gameMasterLMSBean.setHighPrizeAmount(rs2
							.getDouble("high_prize_amt"));
					gameMasterLMSBean.setPrizePayoutRatio(rs2
							.getDouble("prize_payout_ratio"));
					gameMasterLMSBean.setGameStatus(rs2
							.getString("game_status"));
					gameMasterLMSBean.setBonusBallEnable(rs2
							.getString("bonus_ball_enable"));
					dgMap.put(gameId, gameMasterLMSBean);
					lmsMap.put(gameId,gameMasterLMSBean);
				}
				}
				// Utility.gameLMSMap=lmsMap;
	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	
			finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		Util.setGameMap(dgMap);
		Util.setLmsGameMap(lmsMap);
		
		logger.debug("**********In onStartGameData***********"
				+ Util.getGameMap());
		
		 getUpdatedData(servletContext);
		
	}
	
	public static void onStartSLEGameData(){
		long startTime = System.currentTimeMillis();
		logger.debug("---- ENTERING INTO onStartSLEGameData ----");
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Map<Integer, GameMasterLMSBean> sleGameMap = null;
			try {
				con = DBConnect.getConnection();				
				stmt = con.createStatement();
				rs = stmt.executeQuery("select game_type_id,type_dev_name,agent_sale_comm_rate,agent_pwt_comm_rate,govt_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,vat_amt,gov_comm_rate,type_status,prize_payout_ratio from st_sle_game_type_master order by display_order");
				sleGameMap = new HashMap<Integer, GameMasterLMSBean>();
				while (rs.next()) {
					GameMasterLMSBean gameMasterLMSBean = new GameMasterLMSBean();
					gameMasterLMSBean.setAgentSaleCommRate(rs.getDouble("agent_sale_comm_rate"));
					gameMasterLMSBean.setAgentPwtCommRate(rs.getDouble("agent_pwt_comm_rate"));
					gameMasterLMSBean.setRetSaleCommRate(rs.getDouble("retailer_sale_comm_rate"));
					gameMasterLMSBean.setRetPwtCommRate(rs.getDouble("retailer_pwt_comm_rate"));
					gameMasterLMSBean.setGovtCommPwt(rs.getDouble("govt_pwt_comm_rate"));
					gameMasterLMSBean.setVatAmount(rs.getDouble("vat_amt"));
					gameMasterLMSBean.setGovtComm(rs.getDouble("gov_comm_rate"));
					gameMasterLMSBean.setGameStatus(rs.getString("type_status"));
					gameMasterLMSBean.setPrizePayoutRatio(rs.getDouble("prize_payout_ratio"));
					sleGameMap.put(rs.getInt("game_type_id"), gameMasterLMSBean);
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}finally {
			}
			Util.setSLEGameMap(sleGameMap);
			long endTime = System.currentTimeMillis();
			logger.info("TIME TAKEN BY onStartSLEGameData " + ((endTime-startTime)/1000)+ " SECONDS");
		}
	public static void getUpdatedData(ServletContext servletContext) {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DATA_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_UPDATED_DATA);

		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
			
		JsonArray updatedDataListArray=(JsonArray) sRes.getResponseData();
		
		  String drawIdTableMapString= updatedDataListArray.get(0).getAsJsonObject().toString();
		  Type drawIdTableMapType = new TypeToken<Map<Integer, Map<Integer, String>>>() {}.getType();
		  Map<Integer, Map<Integer, String>> drawIdTableMap=new Gson().fromJson(drawIdTableMapString, drawIdTableMapType);
			
		  String jackPotMapString= updatedDataListArray.get(3).getAsJsonObject().toString();
		  Type jackPotMapType = new TypeToken<Map<Integer, Double>>() {}.getType();
		  Map<Integer, Double> jackPotMap=new Gson().fromJson(jackPotMapString, jackPotMapType);
		
		  String drawIdDateMapString= updatedDataListArray.get(4).getAsJsonObject().toString();
		  Type drawIdDateMapType = new TypeToken<Map<String, Map<Long, String>>>() {}.getType();
		  Map<String, Map<Long, String>> drawIdDateMap=new Gson().fromJson(drawIdDateMapString, drawIdDateMapType);
		
		  
		  String drawDetailsMapString= updatedDataListArray.get(5).getAsJsonObject().toString();
		  Type drawDetailsMapType = new TypeToken<Map<Integer, Map<Integer, DrawDetailsBean>>>() {}.getType();
		  Map<Integer, Map<Integer, DrawDetailsBean>> drawDetailsMap=new Gson().fromJson(drawDetailsMapString, drawDetailsMapType);
		
		  
		  String drawNameListMapString= updatedDataListArray.get(6).getAsJsonObject().toString();
		  Type drawNameListMapType = new TypeToken<TreeMap<Integer, List<String>>>() {}.getType();
		  TreeMap<Integer, List<String>> drawNameListMap=new Gson().fromJson(drawNameListMapString, drawNameListMapType);
		
		  String freezTimeMapString= updatedDataListArray.get(7).getAsJsonObject().toString();
		  Type freezTimeMapType = new TypeToken<TreeMap<Integer, String>>() {}.getType();
		  TreeMap<Integer, String> freezTimeMap=new Gson().fromJson(freezTimeMapString, freezTimeMapType);
		
		  String gameNameListString= updatedDataListArray.get(8).getAsJsonArray().toString();
		  Type gameNameListType = new TypeToken<List<String>>() {}.getType();
		  List<String> gameNameList=new Gson().fromJson(gameNameListString, gameNameListType);
		
		  String gameDataString= updatedDataListArray.get(1).getAsJsonObject().toString();
		  Type gameDataType = new TypeToken<TreeMap<Integer, List<List>>>() {}.getType();
		  TreeMap<Integer, List<List>> gameData=new Gson().fromJson(gameDataString, gameDataType);
		

		Util.drawIdTableMap = drawIdTableMap;
		Util.gameData = gameData;
		Util.jackPotMap = jackPotMap;
		Util.drawIdDateMap = drawIdDateMap;
		Util.drawDetailsMap = drawDetailsMap;
		Util.drawNameListMap = drawNameListMap;
		
		
		
		servletContext.setAttribute("GAME_DATA",gameData);
		servletContext.setAttribute("drawIdTableMap",drawIdTableMap);
		servletContext.setAttribute("jackPotMap",jackPotMap);
		servletContext.setAttribute("drawIdDateMap",drawIdDateMap);
		servletContext.setAttribute("drawDetailsMap",drawDetailsMap);
		servletContext.setAttribute("FREEZE_TIME_MAP_NEW",freezTimeMap);
		servletContext.setAttribute("gameNameList",gameNameList);
		 
		 
	}
	
	

	public static void onStartOrganizationData() {
		logger.debug("---- ENTERING INTO onStartOrganizationData ----");
		Map<String, GameMasterLMSBean> lmsMap = null;
		Connection con = DBConnect.getConnection();
		try {
			String orgQuery = "select organization_id,organization_type from st_lms_organization_master where organization_type!='BO'";
			PreparedStatement pstmtOrgQry = con.prepareStatement(orgQuery);
			ResultSet rsOrgQry = pstmtOrgQry.executeQuery();
			Map<Integer, Map<Integer, OrgDataBean>> orgDataMap = new HashMap<Integer, Map<Integer, OrgDataBean>>();
			Map<Integer, OrgDataBean> gameDataMap = null;
			while (rsOrgQry.next()) {
				gameDataMap = getOrgGameData(
						rsOrgQry.getInt("organization_id"), con, rsOrgQry
								.getString("organization_type"));
				orgDataMap.put(rsOrgQry.getInt("organization_id"), gameDataMap);
			}
			Util.orgDataMap = orgDataMap;

			Util.promoGameBeanMap=getPromoGameBeanMap(con);
	
			Util.setRespGamingCriteriaStatusMap();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void onStartOrgDataForSLE() {
		long startTime  = System.currentTimeMillis();
		logger.debug(" ---- ENTERING INTO onStartOrganizationData for SLE----");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Map<Integer, Map<Integer, OrgDataBean>> orgDataMap = null;
		try {
			String query = "select game_type_id game_id , organization_id ,sum(agent_pwt_comm_rate) pwt_comm_variance , sum(agent_sale_comm_rate) sale_comm_variance from (select game_type_id ,organization_id , IF(organization_type='AGENT', agent_pwt_comm_rate, retailer_pwt_comm_rate) agent_pwt_comm_rate, IF(organization_type='AGENT', agent_sale_comm_rate, retailer_sale_comm_rate) agent_sale_comm_rate from st_sle_game_type_master  gm , st_lms_organization_master om where  organization_type <> 'BO' union all select  game_id , agent_org_id organization_id , pwt_comm_variance , sale_comm_variance from st_sle_bo_agent_sale_pwt_comm_variance union all select  game_id , retailer_org_id organization_id , pwt_comm_variance , sale_comm_variance from st_sle_agent_retailer_sale_pwt_comm_variance) a group by organization_id , game_type_id order by  pwt_comm_variance DESC";
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			orgDataMap = new ConcurrentHashMap<Integer, Map<Integer, OrgDataBean>>();
			while (rs.next()) {
				int orgId = rs.getInt("organization_id");
				if(orgDataMap.containsKey(orgId)){
					Map<Integer, OrgDataBean> gameDataMap = orgDataMap.get(orgId);
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
				}else{
					Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();;
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
					orgDataMap.put(orgId, gameDataMap);
				}
			}
			
			Util.sleOrgDataMap = orgDataMap;
			long endTime  = System.currentTimeMillis();
			
			logger.info("TIME TAKEN " + ((endTime-startTime)/1000) +" Seconds....");
			logger.info("ORG DATA SIZE... "+orgDataMap.size());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBConnect.closeConnection(con, pstmt , rs);
		}
	}
	
	public static void updateOrgDataForSLE(String orgType , int organizationId , Connection con) throws Exception {
		long startTime  = System.currentTimeMillis();
		logger.debug(" ---- ENTERING INTO onStartOrganizationData for SLE----");
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Map<Integer, OrgDataBean> gameDataMap = null;

		try {
			String query = "select  " +organizationId+" organization_id  ,game_type_id game_id,  retailer_sale_comm_rate , retailer_pwt_comm_rate , agent_sale_comm_rate , agent_pwt_comm_rate from st_sle_game_type_master";
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			gameDataMap = new HashMap<Integer, OrgDataBean>();
			if("RETAILER".equalsIgnoreCase(orgType)){
				while (rs.next()) {
					int gameId = rs.getInt("game_id");
						OrgDataBean orgBean = new OrgDataBean();
						orgBean.setOrganizationId(organizationId);
						orgBean.setPwtCommVar(rs.getDouble("retailer_pwt_comm_rate"));
						orgBean.setSaleCommVar(rs.getDouble("retailer_sale_comm_rate"));
						gameDataMap.put(gameId , orgBean);
				}
			}else if("AGENT".equalsIgnoreCase(orgType)){
				while (rs.next()) {
					int gameId = rs.getInt("game_id");
						OrgDataBean orgBean = new OrgDataBean();
						orgBean.setOrganizationId(organizationId);
						orgBean.setPwtCommVar(rs.getDouble("agent_pwt_comm_rate"));
						orgBean.setSaleCommVar(rs.getDouble("agent_sale_comm_rate"));
						gameDataMap.put(gameId , orgBean);
				}
			}else{
				throw new Exception(); // TEMP
			}
			
			Util.sleOrgDataMap.put(organizationId, gameDataMap);
			long endTime  = System.currentTimeMillis();
			logger.info("TIME TAKEN " + ((endTime-startTime)/1000) +" Seconds....");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			DBConnect.closeConnection(pstmt , rs);
		}

	}
	
	
	
	public static Map<Integer, OrgDataBean> getOrgGameData(int organizationId,
			Connection con, String orgType) throws LMSException {
		Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();
		OrgDataBean orgDataBean = null;
		PreparedStatement pstmtgameQry = null;
		ResultSet rsGameQry = null;
		String gameQuery = null;
		gameQuery = "select game_id from st_dg_game_master order by game_nbr";
		try {
			pstmtgameQry = con.prepareStatement(gameQuery);
			rsGameQry = pstmtgameQry.executeQuery();
			while (rsGameQry.next()) {
				orgDataBean = new OrgDataBean();
				double orgSaleCommVar = CommonFunctionsHelper
						.fetchDGCommOfOrganization(rsGameQry.getInt("game_id"),
								organizationId, "SALE", orgType, con);
				orgDataBean.setOrganizationId(organizationId);
				orgDataBean.setSaleCommVar(orgSaleCommVar);
				gameDataMap.put(rsGameQry.getInt("game_id"), orgDataBean);
			}
			return gameDataMap;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		}
	}

	public static Map<Integer, OrgDataBean> getOrgGameDataIW(int organizationId, Connection con, String orgType) throws LMSException {
		Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();
		OrgDataBean orgDataBean = null;
		PreparedStatement pstmtgameQry = null;
		ResultSet rsGameQry = null;
		String gameQuery = null;
		gameQuery = "select game_id from st_iw_game_master where game_status = 'SALE_OPEN' order by game_no";
		try {
			pstmtgameQry = con.prepareStatement(gameQuery);
			rsGameQry = pstmtgameQry.executeQuery();
			while (rsGameQry.next()) {
				orgDataBean = new OrgDataBean();
				double orgSaleCommVar = CommonFunctionsHelper.fetchIWCommOfOrganization(rsGameQry.getInt("game_id"), organizationId, "SALE", orgType, con);
				orgDataBean.setOrganizationId(organizationId);
				orgDataBean.setSaleCommVar(orgSaleCommVar);
				gameDataMap.put(rsGameQry.getInt("game_id"), orgDataBean);
			}
			return gameDataMap;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		}
	}
	
	public static Map<Integer, OrgDataBean> getOrgGameDataVS(int organizationId, Connection con, String orgType) throws LMSException {
		Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();
		OrgDataBean orgDataBean = null;
		PreparedStatement pstmtgameQry = null;
		ResultSet rsGameQry = null;
		String gameQuery = null;
		gameQuery = "select game_id from st_vs_game_master where game_status = 'SALE_OPEN' order by game_no";
		try {
			pstmtgameQry = con.prepareStatement(gameQuery);
			rsGameQry = pstmtgameQry.executeQuery();
			while (rsGameQry.next()) {
				orgDataBean = new OrgDataBean();
				double orgSaleCommVar = CommonFunctionsHelper.fetchVSCommOfOrganization(rsGameQry.getInt("game_id"), organizationId, "SALE", orgType, con);
				orgDataBean.setOrganizationId(organizationId);
				orgDataBean.setSaleCommVar(orgSaleCommVar);
				gameDataMap.put(rsGameQry.getInt("game_id"), orgDataBean);
			}
			return gameDataMap;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException();
		}
	}

	public static Map<Integer, List<PromoGameBean>> getPromoGameBeanMap(Connection con) throws SQLException {
		
		
		Map<Integer,List<PromoGameBean>> promoGameBeanMap=new HashMap<Integer, List<PromoGameBean>>();
		Statement stmt=con.createStatement();
		ResultSet gameRs=stmt.executeQuery("select game_id,game_name_dev,game_name from st_dg_game_master where game_status !='SALE_CLOSE'");
		while(gameRs.next()){
		PreparedStatement promoPstmt=con.prepareStatement("select scheme_id,sale_ticket_amt,start_time,end_time,promo_game_id,promo_ticket_type,promo_game_type,no_of_free_tickets,no_of_draws,game_name_dev as promo_game_name,game_name promo_display_name from st_dg_promo_scheme ps,st_dg_game_master gm where ps.promo_game_id=gm.game_id and sale_game_id="+gameRs.getShort("game_id")+"  and status='ACTIVE'");
		ResultSet rs=promoPstmt.executeQuery();
		PromoGameBean promoBean=null;
		List<PromoGameBean> promoGameList=new ArrayList<PromoGameBean>();
		String  gameName= gameRs.getString("game_name");
			while(rs.next()){
			
			promoBean = new PromoGameBean();
			promoBean.setSchemeId(rs.getInt("scheme_id"));
			promoBean.setPromoGameNo(rs.getInt("promo_game_id"));
			promoBean.setPromoGametype(rs.getString("promo_game_type"));
			promoBean.setPromoTicketType(rs.getString("promo_ticket_type"));
			promoBean.setPromoGameDisplayName(rs.getString("promo_display_name"));
			promoBean.setSaleStartTime(rs.getString("start_time"));
			promoBean.setSaleEndTime(rs.getString("end_time"));
			promoBean.setSaleTicketAmt(rs.getDouble("sale_ticket_amt"));
			if(rs.getString("promo_game_name") != null){
				promoBean.setPromoGameName(rs.getString("promo_game_name"));
				}
			if(rs.getString("no_of_free_tickets") != null){
				promoBean.setNoOfPromoTickets(rs.getInt("no_of_free_tickets"));
			}
			if(rs.getString("no_of_draws") != null){
				promoBean.setNoOfDraws(rs.getInt("no_of_draws"));
				}
			if(gameName!=null){
				promoBean.setMainGameName(gameName);
			}
			
			promoGameList.add(promoBean);
		}
		promoGameBeanMap.put(gameRs.getInt("game_id"), promoGameList);
		
		}
		return promoGameBeanMap;
	}
	public static void onStartAdvMessageData()
	{
		logger.info("---- ENTERING INTO onStartAdvMessageData ----");

			Util.advMsgDataMap = new ServerStartUpData().getAdvMsgDataMap();
			SLEUtil.advMessageMap = CommonMethodsControllerImpl.getInstance().getSLEAdvMessageMap();
			IWUtil.advMessageMap = com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getIWAdvMessageMap();
			VSUtil.advMessageMap = com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getVSAdvMessageMap();
	}



	public Map<Integer, Map<Integer, List<MessageDetailsBean>>> getAdvMsgDataMap() {

		ResultSet set = null;
		Connection con = null;
		Statement statement=null;
		List<MessageDetailsBean> tempList = null;
		MessageDetailsBean messageDetailsBean = null;
		Map<Integer, Map<Integer, List<MessageDetailsBean>>> orgMsgDetailMap = null;
		
		try {
			con = DBConnect.getConnection();
			statement = con.createStatement();
			set = statement.executeQuery("select advMap.org_id, advMap.game_id, advMas.msg_id, advMas.date, advMas.creator_user_id, advMas.msg_text, advMas.status, advMas.editable, advMas.msg_for, advMas.msg_location, advMas.activity from st_dg_adv_msg_org_mapping advMap inner join st_dg_adv_msg_master advMas on advMap.msg_id = advMas.msg_id and advMas.status = 'ACTIVE' and advMas.activity in('SALE','ALL') and advMas.msg_for = 'PLAYER' and advMap.service_id = (select service_id from st_lms_service_master where service_code='DG') order by game_id,org_id");

			orgMsgDetailMap = new HashMap<Integer, Map<Integer,List<MessageDetailsBean>>>();
			while(set.next())
			{
				messageDetailsBean = new MessageDetailsBean();
				messageDetailsBean.setMessageId(set.getInt("msg_id"));
				messageDetailsBean.setDate(set.getTimestamp("date"));
				messageDetailsBean.setCreatorUserId(set.getInt("creator_user_id"));
				messageDetailsBean.setMessageText(set.getString("msg_text"));
				messageDetailsBean.setStatus(set.getString("status"));
				messageDetailsBean.setEditable(set.getString("editable"));
				messageDetailsBean.setMessageFor(set.getString("msg_for"));
				messageDetailsBean.setMessageLocation(set.getString("msg_location"));
				messageDetailsBean.setActivity(set.getString("activity"));

				int orgId = set.getInt("org_id");
				int gameId = set.getInt("game_id");
				
				if(orgMsgDetailMap.containsKey(orgId)){
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = orgMsgDetailMap.get(orgId);
					if(gameMsgDetailMap.containsKey(gameId)) {
						gameMsgDetailMap.get(gameId).add(messageDetailsBean);
					} else {
						tempList = new ArrayList<MessageDetailsBean>();
						tempList.add(messageDetailsBean);
						gameMsgDetailMap.put(gameId, tempList);
					}
				} else {
					Map<Integer, List<MessageDetailsBean>> gameMsgDetailMap = new HashMap<Integer, List<MessageDetailsBean>>();
					tempList = new ArrayList<MessageDetailsBean>();
					tempList.add(messageDetailsBean);
					gameMsgDetailMap.put(gameId, tempList);
					orgMsgDetailMap.put(orgId, gameMsgDetailMap);
				}
			}
		}
		catch (SQLException e) {
			logger.error("SQL Exception  :- " + e);
		}catch (Exception e) {
			logger.error("General Exception  :- " + e);
		}
		finally {
			DBConnect.closeConnection(con, statement, set);
		}
		return orgMsgDetailMap;
	}
	
	public static void setServicesMaps(Map<String,String> serviceCodeNameMap, Map<String,Integer> serviceCodeIdMap){
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select service_id, service_code, service_display_name from st_lms_service_master where status='ACTIVE' and service_display_name <> 'HOME' AND service_display_name <> 'SCRATCH'");
			System.out.println("Service Query:" + pstmt + "***");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				serviceCodeNameMap.put(rs.getString("service_code"), rs.getString("service_display_name"));
				serviceCodeIdMap.put(rs.getString("service_code"), rs.getInt("service_id"));
			}
		} catch(SQLException se){
			se.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}
	}
	
	public static void onStartIWGameData() {
		long startTime = System.currentTimeMillis();
		logger.debug("---- ENTERING INTO onStartIWGameData ----");
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Map<Integer, GameMasterLMSBean> iwGameMap = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select game_id, game_dev_name, agent_sale_comm_rate, agent_pwt_comm_rate, retailer_sale_comm_rate, retailer_pwt_comm_rate, vat_amt, gov_comm_rate, game_status, prize_payout_ratio from st_iw_game_master order by display_order;");
			iwGameMap = new HashMap<Integer, GameMasterLMSBean>();
			while (rs.next()) {
				GameMasterLMSBean gameMasterLMSBean = new GameMasterLMSBean();
				gameMasterLMSBean.setAgentSaleCommRate(rs.getDouble("agent_sale_comm_rate"));
				gameMasterLMSBean.setAgentPwtCommRate(rs.getDouble("agent_pwt_comm_rate"));
				gameMasterLMSBean.setRetSaleCommRate(rs.getDouble("retailer_sale_comm_rate"));
				gameMasterLMSBean.setRetPwtCommRate(rs.getDouble("retailer_pwt_comm_rate"));
				gameMasterLMSBean.setVatAmount(rs.getDouble("vat_amt"));
				gameMasterLMSBean.setGovtComm(rs.getDouble("gov_comm_rate"));
				gameMasterLMSBean.setGameStatus(rs.getString("game_status"));
				gameMasterLMSBean.setPrizePayoutRatio(rs.getDouble("prize_payout_ratio"));
				iwGameMap.put(rs.getInt("game_id"), gameMasterLMSBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		Util.setIWGameMap(iwGameMap);
		long endTime = System.currentTimeMillis();
		logger.info("TIME TAKEN BY onStartIWGameData " + ((endTime - startTime) / 1000) + " SECONDS");
	}
	
	public static void onStartOrgDataForIW() {
		long startTime = System.currentTimeMillis();
		logger.debug(" ---- ENTERING INTO onStartOrganizationData for IW----");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Map<Integer, Map<Integer, OrgDataBean>> orgDataMap = null;
		try {
			String query = "select game_id , organization_id ,sum(agent_pwt_comm_rate) pwt_comm_variance , sum(agent_sale_comm_rate) sale_comm_variance from (select game_id ,organization_id, IF(organization_type='AGENT', agent_pwt_comm_rate, retailer_pwt_comm_rate) agent_pwt_comm_rate, IF(organization_type='AGENT', agent_sale_comm_rate, retailer_sale_comm_rate) agent_sale_comm_rate from st_iw_game_master  gm , st_lms_organization_master om where  organization_type <> 'BO' union all select  game_id , agent_org_id organization_id, pwt_comm_variance , sale_comm_variance from st_iw_bo_agent_sale_pwt_comm_variance union all select  game_id , retailer_org_id organization_id , pwt_comm_variance, sale_comm_variance from st_iw_agent_retailer_sale_pwt_comm_variance) a group by organization_id , game_id order by  pwt_comm_variance DESC;";
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			orgDataMap = new ConcurrentHashMap<Integer, Map<Integer, OrgDataBean>>();
			while (rs.next()) {
				int orgId = rs.getInt("organization_id");
				if (orgDataMap.containsKey(orgId)) {
					Map<Integer, OrgDataBean> gameDataMap = orgDataMap.get(orgId);
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
				} else {
					Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
					orgDataMap.put(orgId, gameDataMap);
				}
			}
			Util.iwOrgDataMap = orgDataMap;
			long endTime = System.currentTimeMillis();

			logger.info("TIME TAKEN " + ((endTime - startTime) / 1000) + " Seconds....");
			logger.info("IW ORG DATA SIZE... " + orgDataMap.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
	}
	
	public static void onStartVSGameData() {
		long startTime = System.currentTimeMillis();
		logger.debug("---- ENTERING INTO onStartVSGameData ----");
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		Map<Integer, GameMasterLMSBean> vsGameMap = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select game_id, game_dev_name, agent_sale_comm_rate, agent_pwt_comm_rate, retailer_sale_comm_rate, retailer_pwt_comm_rate, vat_amt, gov_comm_rate, game_status, prize_payout_ratio from st_vs_game_master order by display_order;");
			vsGameMap = new HashMap<Integer, GameMasterLMSBean>();
			while (rs.next()) {
				GameMasterLMSBean gameMasterLMSBean = new GameMasterLMSBean();
				gameMasterLMSBean.setAgentSaleCommRate(rs.getDouble("agent_sale_comm_rate"));
				gameMasterLMSBean.setAgentPwtCommRate(rs.getDouble("agent_pwt_comm_rate"));
				gameMasterLMSBean.setRetSaleCommRate(rs.getDouble("retailer_sale_comm_rate"));
				gameMasterLMSBean.setRetPwtCommRate(rs.getDouble("retailer_pwt_comm_rate"));
				gameMasterLMSBean.setVatAmount(rs.getDouble("vat_amt"));
				gameMasterLMSBean.setGovtComm(rs.getDouble("gov_comm_rate"));
				gameMasterLMSBean.setGameStatus(rs.getString("game_status"));
				gameMasterLMSBean.setPrizePayoutRatio(rs.getDouble("prize_payout_ratio"));
				vsGameMap.put(rs.getInt("game_id"), gameMasterLMSBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		Util.setVSGameMap(vsGameMap);
		long endTime = System.currentTimeMillis();
		logger.info("TIME TAKEN BY onStartVSGameData " + ((endTime - startTime) / 1000) + " SECONDS");
	}
	
	public static void onStartOrgDataForVS() {
		long startTime = System.currentTimeMillis();
		logger.debug(" ---- ENTERING INTO onStartOrganizationData for VS----");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Map<Integer, Map<Integer, OrgDataBean>> orgDataMap = null;
		try {
			String query = "select game_id, organization_id, sum(agent_pwt_comm_rate) pwt_comm_variance, sum(agent_sale_comm_rate) sale_comm_variance from (select game_id ,organization_id, IF(organization_type='AGENT', agent_pwt_comm_rate, retailer_pwt_comm_rate) agent_pwt_comm_rate, IF(organization_type='AGENT', agent_sale_comm_rate, retailer_sale_comm_rate) agent_sale_comm_rate from st_vs_game_master gm , st_lms_organization_master om where  organization_type <> 'BO' union all select  game_id , agent_org_id organization_id, pwt_comm_variance , sale_comm_variance from st_vs_bo_agent_sale_pwt_comm_variance union all select  game_id , retailer_org_id organization_id , pwt_comm_variance, sale_comm_variance from st_vs_agent_retailer_sale_pwt_comm_variance) a group by organization_id , game_id order by  pwt_comm_variance DESC;";
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			orgDataMap = new ConcurrentHashMap<Integer, Map<Integer, OrgDataBean>>();
			while (rs.next()) {
				int orgId = rs.getInt("organization_id");
				if (orgDataMap.containsKey(orgId)) {
					Map<Integer, OrgDataBean> gameDataMap = orgDataMap.get(orgId);
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
				} else {
					Map<Integer, OrgDataBean> gameDataMap = new HashMap<Integer, OrgDataBean>();
					OrgDataBean orgBean = new OrgDataBean();
					orgBean.setOrganizationId(orgId);
					orgBean.setPwtCommVar(rs.getDouble("pwt_comm_variance"));
					orgBean.setSaleCommVar(rs.getDouble("sale_comm_variance"));
					gameDataMap.put(rs.getInt("game_id"), orgBean);
					orgDataMap.put(orgId, gameDataMap);
				}
			}
			Util.vsOrgDataMap = orgDataMap;
			long endTime = System.currentTimeMillis();

			logger.info("TIME TAKEN " + ((endTime - startTime) / 1000) + " Seconds....");
			logger.info("VS ORG DATA SIZE... " + orgDataMap.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
	}
	

public static void main(String[] args) {
	
	ServiceResponse sRes = new ServiceResponse();
	ServiceRequest sReq = new ServiceRequest();
	sReq.setServiceName(ServiceName.DATA_MGMT);
	sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_UPDATED_DATA);
	//KenoPurchaseBean kenoBean=new KenoPurchaseBean();
	//kenoBean.setGameDispName("keno");
	//kenoBean.setDrawDateTime(null);
	//sReq.setServiceData(kenoBean);
	IServiceDelegate delegate = ServiceDelegate.getInstance();
	sRes = delegate.getResponse(sReq);
		
	JsonArray updatedDataListArray=(JsonArray) sRes.getResponseData();
	
	String drawIdTableMapString= updatedDataListArray.get(0).getAsJsonObject().toString();
	  Type drawIdTableMapType = new TypeToken<Map<Integer, Map<Integer, String>>>() {}.getType();
	  Map<Integer, Map<Integer, String>> drawIdTableMap=new Gson().fromJson(drawIdTableMapString, drawIdTableMapType);
		
	  String jackPotMapString= updatedDataListArray.get(3).getAsJsonObject().toString();
	  Type jackPotMapType = new TypeToken<Map<Integer, Double>>() {}.getType();
	  Map<Integer, Double> jackPotMap=new Gson().fromJson(jackPotMapString, jackPotMapType);
	
	  String drawIdDateMapString= updatedDataListArray.get(4).getAsJsonObject().toString();
	  Type drawIdDateMapType = new TypeToken<Map<String, Map<Long, String>>>() {}.getType();
	  Map<String, Map<Long, String>> drawIdDateMap=new Gson().fromJson(drawIdDateMapString, drawIdDateMapType);
	
	  
	  String drawDetailsMapString= updatedDataListArray.get(5).getAsJsonObject().toString();
	  Type drawDetailsMapType = new TypeToken<Map<Integer, Map<Integer, DrawDetailsBean>>>() {}.getType();
	  Map<Integer, Map<Integer, DrawDetailsBean>> drawDetailsMap=new Gson().fromJson(drawDetailsMapString, drawDetailsMapType);
	
	  
	  String drawNameListMapString= updatedDataListArray.get(6).getAsJsonObject().toString();
	  Type drawNameListMapType = new TypeToken<TreeMap<Integer, List<String>>>() {}.getType();
	  TreeMap<Integer, List<String>> drawNameListMap=new Gson().fromJson(drawNameListMapString, drawNameListMapType);
	
	  String freezTimeMapString= updatedDataListArray.get(7).getAsJsonObject().toString();
	  Type freezTimeMapType = new TypeToken<Map<Integer, String>>() {}.getType();
	  Map<Integer, String> freezTimeMap=new Gson().fromJson(freezTimeMapString, freezTimeMapType);
	
	  String gameNameListString= updatedDataListArray.get(8).getAsJsonArray().toString();
	  Type gameNameListType = new TypeToken<List<String>>() {}.getType();
	  List<String> gameNameList=new Gson().fromJson(gameNameListString, gameNameListType);
	
	  String gameDataString= updatedDataListArray.get(1).getAsJsonObject().toString();
	  Type gameDataType = new TypeToken<TreeMap<Integer, List<List>>>() {}.getType();
	  TreeMap<Integer, List<List>> gameData=new Gson().fromJson(gameDataString, gameDataType);
	
	 

	Util.drawIdTableMap = drawIdTableMap;
	Util.gameData = gameData;
	Util.jackPotMap = jackPotMap;
	Util.drawIdDateMap = drawIdDateMap;
	Util.drawDetailsMap = drawDetailsMap;
	Util.drawNameListMap = drawNameListMap;
	
	 
	
	 
	 
	
	/*
	ServiceResponse sRes = new ServiceResponse();
	ServiceRequest sReq = new ServiceRequest();
	sReq.setServiceName(ServiceName.DATA_MGMT);
	sReq.setServiceMethod(ServiceMethodName.ON_START_GAME_DATA);
	IServiceDelegate delegate = ServiceDelegate.getInstance();
	
	sRes = delegate.getResponse(sReq);
	
	String responseString=sRes.getResponseData().toString();
	//Type elementType = new TypeToken<Map<Integer, GameMasterLMSBean>>() {}.getType();
	Type elementType = new TypeToken<Map<Integer, GameMasterLMSBean>>() {}.getType();
	
	Map<Integer, GameMasterLMSBean> lmsMap=new Gson().fromJson(responseString, elementType);
	
	
	
	
	
	//Map<Integer, GameMasterLMSBean> lmsMap = null;
	Connection con = DBConnect.getConnection();

	if (sRes.getIsSuccess()) {
		
			lmsMap = (Map<Integer, GameMasterLMSBean>) sRes
					.getResponseData();
			
		
			
			
			System.out.println(lmsMap.get(2));
			for(Entry<Integer, GameMasterLMSBean> entry:lmsMap.entrySet())
				
			System.out.println(entry.getKey()+"   "+entry.getValue().getPriceMap());
			}
			
			
	*/}


	
}