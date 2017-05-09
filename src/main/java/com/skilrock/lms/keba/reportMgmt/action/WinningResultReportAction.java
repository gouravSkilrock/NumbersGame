package com.skilrock.lms.keba.reportMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawWinningReportBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class WinningResultReportAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	public WinningResultReportAction() {
		super(WinningResultReportAction.class);
	}
	private String requestData;
	
	public void fetchWinningResult() throws Exception {
//		ServletContext sc = ServletActionContext.getServletContext();
//		JSONObject responseObject = new JSONObject();
//		PrintWriter out = null;
//		try {
//			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
//			String userName = (String) requestData.get("userName");
//			response.setContentType("application/json");
//			out = response.getWriter();
//
//		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
//		if (currentUserSessionMap == null) {
//			throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
//			}
//		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
//
//		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
//
//		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
//		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
//		
//		if (!CommonFunctionsHelper.isSessionValid(session)) {
//			throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
//		}
//
//		/*
//		 * TreeMap<Integer, List<List>> gameData = (TreeMap<Integer,
//		 * List<List>>) sc .getAttribute("GAME_DATA");
//		 */
//		TreeMap<Integer, DrawWinningReportBean> gameData = new DrawGameRPOSHelper()
//		.getDrawGameData();
//		
//		Map<Integer, GameMasterLMSBean> gameMap=Util.getGameMap();
//		JSONObject gameMapObj=null;
//		JSONObject drawMapObj=null;
//		JSONArray drawArr=null;
//		JSONArray gameArr=new JSONArray();
//		for(Map.Entry<Integer, GameMasterLMSBean> gameMasterBean: gameMap.entrySet()){
//			DrawWinningReportBean list=gameData.get(gameMasterBean.getValue().getGameId());
//			if(list == null){
//				continue;
//			}
//			List<DrawDetailsBean> drawDetailbeanList=gameData.get(gameMasterBean.getValue().getGameId()).get(3);
//			gameMapObj=new JSONObject();
//			gameMapObj.put("gameName", gameMasterBean.getValue().getGameName());
//			drawArr=new JSONArray();
//			for(int i=0;i<drawDetailbeanList.size();i++){
//				DrawDetailsBean drawDetailbean=drawDetailbeanList.get(i);
//				drawMapObj=new JSONObject(); 
//				drawMapObj.put("drawName", drawDetailbean.getDrawName());
//				drawMapObj.put("drawDateTime",Util.getDateTimeFormat(drawDetailbean.getDrawDateTime()) );
//				if(drawDetailbean.getWinningResult() == null){
//					drawMapObj.put("winningResult", "Result Awaited");
//				}else{
//				drawMapObj.put("winningResult", drawDetailbean.getWinningResult());
//				}
//				drawArr.add(drawMapObj);
//				
//			}
//			gameMapObj.put("drawData", drawArr);
//			gameArr.add(gameMapObj);
//			
//		}
//		responseObject.put("gameData", gameArr);
//		responseObject.put("errorMsg", "");
//		responseObject.put("isSuccess", true);
//		//String winningResult = "Result Awaited";
//		/*for (int i = 0; i < winningResultList.size(); i++) {
//			String[] result = winningResultList.get(i).split("=");
//			Long time = Long.parseLong(result[0]);
//			Timestamp t = new Timestamp(time);
//			if (t.toString().split("\\.")[0].equalsIgnoreCase(drawTime)) {
//				winningResult = result[1];
//				
//				 * if ("RaffleGame".equalsIgnoreCase(Util.getGameName(gameNo)))
//				 * { winningResult = new
//				 * RaffleHelper().swapRaffleResult(winningResult); }
//				 
//			}
//		}*/
//		//logger.debug("Winning Result:" + winningResult + "|");
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//			responseObject.put("errorMsg", "IOException Occured.");
//			responseObject.put("isSuccess", false);
//			return;
//		} catch (LMSException e) {
//			e.printStackTrace();
//			if(e.getErrorCode() == 2013){
//				responseObject.put("errorMsg", e.getErrorMessage());
//			}else{
//			responseObject.put("errorMsg", "LMSException Occured.");
//			}
//			responseObject.put("isSuccess", false);
//			return;
//		}  catch (Exception e) {
//			e.printStackTrace();
//			responseObject.put("errorMsg", "Exception Occured.");
//			responseObject.put("isSuccess", false);
//			return;
//		} finally {
//			if (responseObject.isEmpty()) {
//				responseObject.put("errorMsg", "Compile Time Error.");
//				responseObject.put("isSuccess", false);
//			}
//			logger.info("ZimLottoBonus Sale Response Data : " + responseObject);
//			out.print(responseObject);
//			out.flush();
//			out.close();
//		}
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

}
