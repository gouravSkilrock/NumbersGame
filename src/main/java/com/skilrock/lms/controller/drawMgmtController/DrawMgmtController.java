package com.skilrock.lms.controller.drawMgmtController;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.*;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.BoardTicketDataBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;


/**
 * @author Nikhil K. Bansal
 * @category Draw Mgmt Controller
 */

public class DrawMgmtController{
	
	private static final Logger logger = LoggerFactory.getLogger(DrawMgmtController.class);

	private DrawMgmtController(){}

	private static DrawMgmtController classInstance;

	public static synchronized DrawMgmtController getInstance() {
		if(classInstance == null)
			classInstance = new DrawMgmtController();
		return classInstance;
	}
	
	public PwtVerifyTicketBean fetchTPTktDetailsFromDGE(String ticketNumber) throws LMSException{
		ServiceRequest sReq = null; 
		PwtVerifyTicketBean responseBean=null;
		JSONObject requestObject = new JSONObject();
		try {
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.TP_DATA_MGMT);
			sReq.setServiceMethod(ServiceMethodName.TRACK_TP_TICKET_DETAILS);
			requestObject.put("ticketNumber", ticketNumber);
			sReq.setServiceData(requestObject);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			String responseString = delegate.getResponseString(sReq);
			//String responseString = "{\"responseCode\":0,\"responseMsg\":\"SUCCESS\",\"merchantName\":\"Asoft\",\"merchantCode\":\"Asoft\",\"ticketNumber\":\"250000120717171\",\"ticketStatus\":\"CLAIMED\",\"purchaseDateTime\":\"2015-03-12 15:15:52\",\"totalPurchaseAmt\":5.5,\"totalWinAmt\":0.0,\"noOfDraws\":1,\"verifyTicketDrawDataBeanList\":[{\"drawId\":288,\"drawName\":\"NA\",\"drawDateTime\":\"2015-03-12 15:18:00\",\"drawResult\":\"1,2,3,4,5,6,7 \",\"boardTicketBeanList\":[{\"boardId\":1,\"winningAmt\":45.0,\"betAmtMultiple\":3,\"pickedData\":\"1,3,2,12,4,16,18\",\"claimedAt\":\"BO\",\"status\":\"CLAIMED\"}],\"drawStatus\":\"CLAIM ALLOW\",\"drawWinAmt\":45.0,\"drawClaimTime\":\"2015-03-13 13:05:42\",\"boardCount\":0,\"tableName\":\"285\"}],\"gameName\":\"BONUS LOTTO\",\"gameId\":10,\"userId\":2500001,\"userName\":\"manoj\"}";
			logger.info("response Date From DGE"+responseString);
			JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
			if (data.get("responseCode").getAsInt()==0){
				responseBean = new Gson().fromJson(data, PwtVerifyTicketBean.class);
				updateOrgName(responseBean);
			}
			else{
				throw new LMSException(data.get("responseCode").getAsInt(),data.get("responseMsg").getAsString());
			}
		} catch (LMSException el) {
			throw el;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseBean;
	}

	private void updateOrgName(PwtVerifyTicketBean responseBean) {
		
		Connection conn = null ;
		Statement stmt = null ;
		Statement stmt1 = null ;
		ResultSet rs = null ;
		String tableName = null ;
		try{
			conn = DBConnect.getConnection() ;
			String query = "select retailer_transaction_id, bo_transaction_id from st_dg_pwt_inv_"+responseBean.getGameId()+" where ticket_nbr = "+ ("Asoft".equalsIgnoreCase(responseBean.getMerchantCode())?responseBean.getTicketNumber() : responseBean.getTicketNumber()+0)+" ;" ;
			
			String txnId = null ;
			
			String orgCol = null ;
			
			stmt = conn.createStatement() ;
			
			rs = stmt.executeQuery(query);
			
			if(rs.next())
			{
				if(rs.getString("retailer_transaction_id") == null)
				{
					txnId = rs.getString("bo_transaction_id") ;
					orgCol = "bo_org_id";
					tableName = "st_dg_bo_direct_plr_pwt" ;
				}
				else{
					txnId = rs.getString("retailer_transaction_id") ;
					orgCol = "retailer_org_id";
					tableName = "st_dg_ret_pwt_"+responseBean.getGameId() ;
				}
			}
			
			if(orgCol != null && tableName != null){
				String orgId = null ;
				String orgQuery = "select "+orgCol+" from "+tableName +" where transaction_id='"+txnId+"';";
				stmt1 = conn.createStatement();
				rs = stmt1.executeQuery(orgQuery);
				if(rs.next())
					orgId = rs.getString(orgCol) ;
				
				String orgName = Util.getOrgName(Integer.parseInt(orgId));
				
				for(PwtVerifyTicketDrawDataBean drawBean : responseBean.getVerifyTicketDrawDataBeanList())
				{
					for(BoardTicketDataBean tktDataBean : drawBean.getBoardTicketBeanList())
					{
						if("CLAIMED".equalsIgnoreCase(tktDataBean.getTicketStatus()))
						tktDataBean.setClaimedAt(orgName);
					}
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			
		}finally{
			DBConnect.closeStmt(stmt);
			DBConnect.closeStmt(stmt1);
			DBConnect.closeRs(rs);
		}
		
		
	}
}
