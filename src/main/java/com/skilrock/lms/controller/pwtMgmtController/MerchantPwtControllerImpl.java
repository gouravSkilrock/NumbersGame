package com.skilrock.lms.controller.pwtMgmtController;

import java.sql.Connection;
import java.util.List;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.pwtMgmtDao.pwtMgmtDaoImpl;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;

/**
 * @author Nikhil K. Bansal
 * @category Direct Player Pwt MerchantWise DGE Calling
 */

public class MerchantPwtControllerImpl{
	
	private static final Logger logger = LoggerFactory.getLogger(MerchantPwtControllerImpl.class);

	private MerchantPwtControllerImpl(){}

	private static MerchantPwtControllerImpl classInstance;

	public static synchronized MerchantPwtControllerImpl getInstance() {
		if(classInstance == null)
			classInstance = new MerchantPwtControllerImpl();
		return classInstance;
	}
	public PwtVerifyTicketBean merchantWiseTicketPwtInformation(String ticketNo, UserInfoBean userInfoBean) throws LMSException{
		ServiceRequest sReq = null; 
		PwtVerifyTicketBean responseBean=null;
		Connection connection = null ;
		JSONObject requestObject = new JSONObject();
		try {
			connection = DBConnect.getConnection();
			
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.PWT_MGMT);
			sReq.setServiceMethod(ServiceMethodName.VERIFY_TP_TICKET);
			requestObject.put("ticketNumber", ticketNo);
			sReq.setServiceData(requestObject);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			String responseString = delegate.getResponseString(sReq);
			//String responseString="{\"isSuccess\":true,\"responseData\":{\"responseCode\":0,\"ticketNumber\":\"98033131979305001\",\"merchantName\":\"Asoft\",\"purchaseDateTime\":\"2014-11-15 14:04:29\",\"totalPurchaseAmt\":1.0,\"totalWinAmt\":4.0,\"noOfDraws\":1,\"verifyTicketDrawDataBeanList\":[{\"drawId\":34382,\"drawName\":\"Super Saturday30\",\"drawDateTime\":\"2014-11-15 14:15:00*Super Saturday30\",\"drawResult\":\"3-4-14-74-82\",\"boardTicketBeanList\":[{\"boardId\":1,\"winningAmt\":4.0,\"betAmtMultiple\":0,\"ticketStatus\":\"UNCLM_PWT\"}],\"drawStatus\":\"DRAW_EXPIRED\",\"drawWinAmt\":4.0,\"boardCount\":10}],\"gameName\":\"Lucky Numbers\",\"gameId\":1,\"userName\":\"11228\"}}";
			logger.info("response Date From DGE"+responseString);
			JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
			if(!data.get("isSuccess").getAsBoolean()&&data.get("responseData").getAsJsonObject().get("responseCode").getAsInt()==130){
				throw new LMSException(130,data.get("responseData").getAsJsonObject().get("responseMsg").getAsString());
			}
			if(data.get("isSuccess").getAsBoolean()==false){
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
			responseBean = new Gson().fromJson(data.get("responseData"), PwtVerifyTicketBean.class);
			if(responseBean.getVerifyTicketDrawDataBeanList()==null || responseBean.getVerifyTicketDrawDataBeanList().size()<=0){
				throw new LMSException(LMSErrors.NO_RECORD_FOUND_ERROR_CODE,LMSErrors.NO_RECORD_FOUND_ERROR_MESSAGE);
			}
			
			if("RETAILER".equalsIgnoreCase(userInfoBean.getUserType())){
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),
							connection);
			if(orgPwtLimit.getVerificationLimit() <= responseBean.getTotalWinAmt())
			{
				throw new LMSException(EmbeddedErrors.PWT_OUT_VERIFY_LIMIT_ERROR_CODE,EmbeddedErrors.PWT_OUT_VERIFY_LIMIT);
			}
			}
			
		} catch (LMSException el) {
			throw el;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseBean;
	}
	
	public PwtVerifyTicketBean payDirectPwtProcesscontrol(String verCode,PwtVerifyTicketBean pwtBean,UserInfoBean userInfoBean) throws LMSException{
		JSONObject requestObject = new JSONObject();
		ServiceRequest sReq = null; 
		List<Long> refTransId;
		Connection con=null;
		String recieptNo=null;
		boolean isSuccess=false;
		try {
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			refTransId=pwtMgmtDaoImpl.getInstance().payDirectPwtProcessAtLMS(verCode,pwtBean,userInfoBean,con);
			if(refTransId.size() > 0){
				sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.PWT_MGMT);
				sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_TP_UPDATE);
				requestObject.put("ticketNumber", pwtBean.getTicketNumber());
				requestObject.put("verificationCode", verCode);
//				requestObject.put("merchantCode", pwtBean.getMerchantCode());
				requestObject.put("merchantCode", "LMS"); //This need to change and get merchant-Id from transaction manager at DGE.  
				requestObject.put("refMerchantId", "WGRL");
				requestObject.put("partyId", userInfoBean.getUserOrgId());
				requestObject.put("refTransId", refTransId.toString().replace("[", "").replace("]", "").replace(" ", ""));
				requestObject.put("userId", userInfoBean.getUserId());
				requestObject.put("userType", userInfoBean.getUserType());
				sReq.setServiceData(requestObject);
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				String responseString = delegate.getResponseString(sReq);
				//String responseString="{\"responseCode\":0,\"winningAmt\":45.0,\"purchaseTime\":\"2015-03-12 15:15:52\",\"purchaseAmt\":5.5,\"refTxnId\":\"565\"}";
				JsonObject data = new JsonParser().parse(responseString).getAsJsonObject();
				if (data.get("responseCode").getAsInt()==0){
					isSuccess=pwtMgmtDaoImpl.getInstance().updatePlayerPwtMerchantTransaction(pwtBean,data.get("refTxnId").getAsString(),con);
					con.commit();
					if(!isSuccess){
						throw new LMSException(LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_CODE,LMSErrors.TRANSACTION_NOT_AVAILABLE_ERROR_MESSAGE);
					}
				}else{
					//con.rollback();
					throw new LMSException(data.get("responseCode").getAsInt(),data.get("responseMsg").getAsString());
				}
			} 
		} catch (LMSException el) {
			throw el;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(con);
		}
		return pwtBean;
	}
}