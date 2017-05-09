package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;

public class RaffleHelper {
	static Log logger = LogFactory.getLog(RaffleHelper.class);
	
	public TreeMap getDrawGameData() {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_GAMEDATA);
		sReq.setServiceData(LMSFilterDispatcher.isMachineEnabled);
		logger.debug("2222222222222222 sd");
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return (TreeMap) sRes.getResponseData();
	}

	public String getSaleTktNo(String promoTktNo) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String saleTktNo = null;
		try {
			pstmt = con.prepareStatement("select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr = ?");
			pstmt.setString(1, promoTktNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				saleTktNo = rs.getString("sale_ticket_nbr");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
		return saleTktNo;
	}
	
	public String swapRaffleResult(String drawResult){
		if(drawResult != null && !"null".equalsIgnoreCase(drawResult) && !"0".equalsIgnoreCase(drawResult)){
			int drawRsltTktLen = drawResult.length();
			String promoTktNo = null;
			String rpcCount = null;
			if (drawRsltTktLen == ConfigurationVariables.tktLenA || drawRsltTktLen == ConfigurationVariables.tktLenB) {
				promoTktNo = drawResult.substring(0, drawRsltTktLen - 2);
				rpcCount = drawResult.substring(drawRsltTktLen - 2, drawRsltTktLen);
				String saleTktNo = getSaleTktNo(promoTktNo);
				if (saleTktNo != null) {
					drawResult = saleTktNo + rpcCount;
				}
			} else {
				drawResult = "Undisclosed";
			}
		} else {
			drawResult = "No Sale";	// No Tickets Sold for This Draw.	
		}
		return drawResult;
	}
}
