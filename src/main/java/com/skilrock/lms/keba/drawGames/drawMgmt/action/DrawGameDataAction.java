package com.skilrock.lms.keba.drawGames.drawMgmt.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.BetDetailsBean;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.GameLoginDrawBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class DrawGameDataAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public DrawGameDataAction() {
		super(DrawGameDataAction.class);
	}

	@SuppressWarnings("unchecked")
	public void fetchLoginDrawGameData() throws Exception {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		SimpleDateFormat drawDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat drawTimeFormat = new SimpleDateFormat("hh:mm:ss");

		Map<Integer,GameLoginDrawBean> gameLoginDrawBeanmap = null;
		ServiceRequest sReq = new ServiceRequest();
		ServiceResponse sRes = new ServiceResponse();
		GameLoginDrawBean gameLoginDrawBean = null;
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.FETCH_LOGIN_DRAW_DATA);
		sReq.setServiceData(Util.getGameNumberList());
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if(sRes.getIsSuccess()) {
			gameLoginDrawBeanmap = (Map<Integer, GameLoginDrawBean>) sRes.getResponseData();
		}

		List<Integer> gameIdList = Util.getGameNumberList();
		JSONObject finalData = new JSONObject();
		JSONArray gameDataArray = new JSONArray();
		JSONObject gameData = new JSONObject();
		JSONArray betTypeArray = null;
		JSONObject betTypeData = new JSONObject();
		JSONArray drawDataArray = null;
		JSONObject drawData = new JSONObject();
		for(int i=0; i<gameIdList.size(); i++) {
			gameLoginDrawBean = gameLoginDrawBeanmap.get(gameIdList.get(i));
			if(gameLoginDrawBean != null) {
				gameData.put("gameDispName", gameLoginDrawBean.getGameName());
				gameData.put("gameDevName", gameLoginDrawBean.getGameNameDev());
				gameData.put("gameId", gameLoginDrawBean.getGameId());
				gameData.put("jackpotLimit", gameLoginDrawBean.getJackpotLimit());
				gameData.put("freezeTime", gameLoginDrawBean.getFreezeTime());
				gameData.put("ticketExpiry", gameLoginDrawBean.getTicketExpiryPeriod());

				betTypeArray = new JSONArray();
				for(Map.Entry<String, BetDetailsBean> priceMap : gameLoginDrawBean.getPriceMap().entrySet()){
					betTypeData.put("betName", priceMap.getKey());
					betTypeData.put("unitPrice", priceMap.getValue().getUnitPrice());
					betTypeData.put("maxBetAmt", priceMap.getValue().getMaxBetAmtMultiple());
					betTypeArray.add(betTypeData);
				}
				gameData.put("betTypeData", betTypeArray);

				drawDataArray = new JSONArray();
				for(DrawDetailsBean drawDetailsBean : gameLoginDrawBean.getDrawDetailsBeanList()) {
					drawData.put("drawId", drawDetailsBean.getDrawId());
					drawData.put("drawName", (drawDetailsBean.getDrawName()==null)?"":drawDetailsBean.getDrawName());
					drawData.put("drawDate", drawDateFormat.format(drawDetailsBean.getDrawDateTime().getTime()));
					drawData.put("drawTime", drawTimeFormat.format(drawDetailsBean.getDrawDateTime().getTime()));
					drawDataArray.add(drawData);
				}
				gameData.put("drawData", drawDataArray);
				gameDataArray.add(gameData);
			}
		}
		finalData.put("gameData", gameDataArray);
		finalData.put("isSuccess", true);
		finalData.put("errorMsg", "");
		logger.info("Draw Response Data:"+finalData);
		out.print(finalData);
		out.flush();
	}
}