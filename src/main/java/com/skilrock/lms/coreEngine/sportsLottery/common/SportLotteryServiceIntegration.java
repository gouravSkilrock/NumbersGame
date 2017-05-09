package com.skilrock.lms.coreEngine.sportsLottery.common;

import java.lang.reflect.Type;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawEventResultBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.EventMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameDataReportBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PayPwtTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGamePlayBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.TeamMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.WinningResultReportBean;

public class SportLotteryServiceIntegration {

	public static List<GameMasterBean> getSportLotteryGameData(String merchantName){
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<GameMasterBean> gameMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("merchantName", merchantName);
			responseObject = SportsUtility.sendCallApi("getSportsLotteryGameData", requestObject, "1");
			
			String responseString=responseObject.get("gameMasterList").toString();
			Type listType = new TypeToken<List<GameMasterBean>>() {}.getType();
			
			gameMasterList=new Gson().fromJson(responseString, listType);
			
			//gameMasterList = (List<GameMasterBean>) responseObject.get("gameMasterList");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gameMasterList;
		
	}
	
	public static List<GameMasterBean> getSportsLotteryOnStartServerData(String merchantName){
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<GameMasterBean> gameMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("merchantName", merchantName);
			responseObject = SportsUtility.sendCallApi("getSportsLotteryOnStartServerData", requestObject, "1");
			
			String responseString=responseObject.get("gameMasterList").toString();
			Type listType = new TypeToken<List<GameMasterBean>>() {}.getType();
			
			gameMasterList=new Gson().fromJson(responseString, listType);
			//gameMasterList = (List<GameMasterBean>) responseObject.get("gameMasterList");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gameMasterList;
		
	}
	
	public static SportsLotteryGamePlayBean getSportsLotteryGamePlay(SportsLotteryGamePlayBean gamePlayBean) throws SLEException{

		JSONObject requestObject = null;
		JSONObject responseObject = null;
		SportsLotteryGamePlayBean gamePlayBeanResponse=null;
		try {
			requestObject = new JSONObject();
			requestObject.put("ticketPurchaseData", gamePlayBean);
			responseObject = SportsUtility.sendCallApi("purchaseTicketAction", requestObject, "1");
			
			if(responseObject.getBoolean("isSuccess")){
				String s=responseObject.get("ticketPurchaseData").toString();
				//	new Gson().fromJson(requestObject.get("ticketPurchaseData").toString(), type);
					Type type1 = new TypeToken<SportsLotteryGamePlayBean>() {}.getType();
					gamePlayBeanResponse=new Gson().fromJson(s, type1);
			}else{
				throw new SLEException(SLEErrors.SLE_EXCEPTION_ERROR_CODE, SLEErrors.SLE_EXCEPTION_ERROR_MESSAGE);
			}
			
			
							
		
		}catch (SLEException e) {
			throw e;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gamePlayBeanResponse;
	}

	public static PwtVerifyTicketBean prizeWinningVerifyTicket(
			String merchantName, long ticketNumber) throws SLEException {


		JSONObject requestObject = null;
		JSONObject responseObject = null;
		PwtVerifyTicketBean pwtVerifyTicketBean=null;
		try {
			requestObject = new JSONObject();
			requestObject.put("merchantName", merchantName);
			requestObject.put("ticketNumber", ticketNumber);

			responseObject = SportsUtility.sendCallApi("prizeWinningVerifyTicket", requestObject, "1");
			if(responseObject.getBoolean("isSuccess")){
			String s=responseObject.get("ticketVerifyData").toString();
			//	new Gson().fromJson(requestObject.get("ticketPurchaseData").toString(), type);
				Type type1 = new TypeToken<PwtVerifyTicketBean>() {}.getType();
				pwtVerifyTicketBean=new Gson().fromJson(s, type1);
			}else{
			if(responseObject.getInt("errorCode") == 104){
				throw new SLEException(responseObject.getInt("errorCode"),responseObject.getString("errorMsg"));
			}else{
				throw new SLEException(SLEErrors.SLE_EXCEPTION_ERROR_CODE, SLEErrors.SLE_EXCEPTION_ERROR_MESSAGE);
			}
				
			}
		
		}catch (SLEException e) {
			throw e;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return pwtVerifyTicketBean;
	
	}

	public static void payPrizeWinningTicket(PayPwtTicketBean payPwtTicketBean) throws SLEException {


		JSONObject requestObject = null;
		JSONObject responseObject = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("payPwtRequest", payPwtTicketBean);
			
			responseObject = SportsUtility.sendCallApi("payPrizeWinningTicket", requestObject, "1");
			if(responseObject.getBoolean("isSuccess")){
			
			}else{

				if(responseObject.getInt("errorCode") == 104){
					throw new SLEException(responseObject.getInt("errorCode"),responseObject.getString("errorMsg"));
				}else{
					throw new SLEException(SLEErrors.SLE_EXCEPTION_ERROR_CODE, SLEErrors.SLE_EXCEPTION_ERROR_MESSAGE);
				}
					
				
			}
		
		}catch (SLEException e) {
			throw e;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
	
	}

	
	public static List<GameDataReportBean> gameDataReportSearch(int gameId, int gameTypeId, String startDate, String endDate, String reportType, String merchantName) {

		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<GameDataReportBean> gameDataReportList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("startDate", startDate);
			requestObject.put("endDate", endDate);
			requestObject.put("reportType", reportType);
			requestObject.put("merchantName", "WGRL");
			responseObject = SportsUtility.sendCallApi("getGameDataReportList", requestObject, "1");

			Type type = new TypeToken<List<GameDataReportBean>>(){}.getType();
			gameDataReportList = new Gson().fromJson(responseObject.get("gameDataReportList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameDataReportList;
	}

	public static List<DrawMasterBean> resultSubmissionDrawData(int gameId, int gameTypeId, String string) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<DrawMasterBean> drawMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("merchantName", "WGRL");
			responseObject = SportsUtility.sendCallApi("sportsLotteryResultSubmissionDrawData", requestObject, "1");
			Type type = new TypeToken<List<DrawMasterBean>>(){}.getType();
			drawMasterList = new Gson().fromJson(responseObject.get("drawMasterList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return drawMasterList;
	}

	public static String resultSubmissionSubmit(DrawEventResultBean drawEventResultBean) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		String status = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("drawEventResultBean", drawEventResultBean);
			responseObject = SportsUtility.sendCallApi("sportsLotteryResultSubmission", requestObject, "2");
			status = responseObject.getString("status");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return status;
	}

	public static List<TeamMasterBean> getTeamMasterData(int gameId) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<TeamMasterBean> teamMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			responseObject = SportsUtility.sendCallApi("getTeamMasterData", requestObject, "2");
			Type type = new TypeToken<List<TeamMasterBean>>(){}.getType();
			teamMasterList = new Gson().fromJson(responseObject.get("teamMasterList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return teamMasterList;
	}

	public static String eventInsertionSubmit(int gameId, String homeTeam, String awayTeam, String[] optionSet, long startTime, long endTime) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		String status = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("homeTeam", homeTeam);
			requestObject.put("awayTeam", awayTeam);

			JSONArray optionArray = new JSONArray();
			for(int i=0; i<optionSet.length; i++) {
				optionArray.add(optionSet[i]);
			}
			requestObject.put("optionSet", optionArray);

			requestObject.put("startTime", startTime);
			requestObject.put("endTime", endTime);
			responseObject = SportsUtility.sendCallApi("eventInsertionSubmit", requestObject, "2");
			status = responseObject.get("status").toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return status;
	}

	public static List<DrawMasterBean> getDrawMappingDrawMasterList(int gameId, int gameTypeId) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<DrawMasterBean> drawMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("merchantName", "WGRL");
			responseObject = SportsUtility.sendCallApi("getDrawMappingDrawMasterList", requestObject, "1");
			Type type = new TypeToken<List<DrawMasterBean>>(){}.getType();
			drawMasterList = new Gson().fromJson(responseObject.get("drawMasterList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return drawMasterList;
	}

	public static List<EventMasterBean> getDrawMappingEventMasterList(int gameId, int gameTypeId, int drawId) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<EventMasterBean> eventMasterList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("drawId", drawId);
			responseObject = SportsUtility.sendCallApi("getDrawMappingEventMasterList", requestObject, "1");
			Type type = new TypeToken<List<EventMasterBean>>(){}.getType();
			eventMasterList = new Gson().fromJson(responseObject.get("eventMasterList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return eventMasterList;
	}

	public static String drawEventMappingSubmit(int gameId, int gameTypeId, int drawId, String[] eventSelected) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		String retValue = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("drawId", drawId);

			JSONArray eventSelectedArray = new JSONArray();
			for(int i=0; i<eventSelected.length; i++) {
				eventSelectedArray.add(eventSelected[i]);
			}
			requestObject.put("eventSelected", eventSelectedArray);

			responseObject = SportsUtility.sendCallApi("drawEventMappingSubmit", requestObject, "1");
			retValue = responseObject.getString("retValue");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return retValue;
	}

	public static List<WinningResultReportBean> winningResultReportSearch(int gameId, int gameTypeId) {
		JSONObject requestObject = null;
		JSONObject responseObject = null;
		List<WinningResultReportBean> winningResultReportList = null;
		try {
			requestObject = new JSONObject();
			requestObject.put("gameId", gameId);
			requestObject.put("gameTypeId", gameTypeId);
			requestObject.put("merchantName", "WGRL");
			responseObject = SportsUtility.sendCallApi("winningResultReportSearch", requestObject, "1");
			Type type = new TypeToken<List<WinningResultReportBean>>(){}.getType();
			winningResultReportList = new Gson().fromJson(responseObject.get("winningResultReportList").toString(), type);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return winningResultReportList;
	}
}