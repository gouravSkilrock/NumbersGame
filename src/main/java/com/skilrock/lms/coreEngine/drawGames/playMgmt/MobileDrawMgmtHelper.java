package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Timestamp;
import java.util.Date;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class MobileDrawMgmtHelper {
	
	public static String fetchDrawDetails(String gameNameDev){
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		StringBuilder str = new StringBuilder("");
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.FETCH_LIVE_DRAW_DATA);
		DrawDataBean drawDataBean = new DrawDataBean();
		int gameId=Util.getGameId(gameNameDev);
		System.out.println("game no in mobile helper is : "+gameId);
		String frmDate = new Timestamp(new Date().getTime()).toString().split("\\.")[0];
		drawDataBean.setGameNo(gameId);
		drawDataBean.setFromDate(frmDate);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sReq.setServiceData(drawDataBean);
		sRes = delegate.getResponse(sReq);
		if(sRes.getIsSuccess()){
			drawDataBean = (DrawDataBean)sRes.getResponseData();
			str.append("<rmi><?xml version=\"1.0\" encoding=\"UTF-8\"?><gameNames>");
			str.append("<lastDraw><dateTime>"+drawDataBean.getLastDrawtime()+"</dateTime>");
			str.append("<sale>"+drawDataBean.getLastDrawSaleValue()+"</sale><pwt>"+drawDataBean.getLastDrawWinningAmt()+"</pwt></lastDraw>");
			str.append("<currentDraw><dateTime>"+drawDataBean.getDrawTime()+"</dateTime>");
			str.append("<sale>"+drawDataBean.getTotalSaleValue()+"</sale><pwt>"+drawDataBean.getTotalWinningAmount()+"</pwt></currentDraw>");
			str.append("</gameNames></rmi>");
			System.out.println("result sent to mobile by amit: "+str);
			return str.toString();
		}
		System.out.println("no data found returning null....");
		return null;
	}

}
