package com.skilrock.ola.api;


import java.sql.Connection;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationResponseBean;

public class PlayerLotteryIntegration {

	public boolean verifyPlrName(String userName) throws LMSException {
		boolean isSuccess=false;
		// Call Player Mgmt Api  
				String method = "playerUserNameVerificationAction";
				JSONObject params = new JSONObject();
				params.put("userName",userName);
				JSONObject responseObj =Utility.sendCallApi(method, params, "5");
				if(responseObj==null){
					throw new LMSException(LMSErrors.PMS_CONNECTION_ERROR_CODE);
				}else{
					 isSuccess = responseObj.getBoolean("isSuccess");
					
				}		 
				return isSuccess;
	}
	
	public boolean verifyMobileNumber(String mobileNumber) throws LMSException  {
			boolean isSuccess=false;
			// Call Player Mgmt Api  
			String method = "playerMobileNumberVerificationAction";
			JSONObject params = new JSONObject();
	        params.put("mobileNumber",mobileNumber);
	        JSONObject responseObj =Utility.sendCallApi(method, params, "6");
			if(responseObj==null){
				throw new LMSException(LMSErrors.PMS_CONNECTION_ERROR_CODE);
			}else{
				 isSuccess = responseObj.getBoolean("isSuccess");				
			}		 
			return isSuccess;
	}
	
	public static OLADepositResponseBean playerDeposit(OLADepositRequestBean reqBean) throws LMSException{
		JSONObject params = new JSONObject();
		// Call Player Mgmt Api  
		String method = "playerDepositAction";
			  params.put("refTransactionId",reqBean.getTransactionId());
			  params.put("depositMode", "OLA");
			  params.put("refCode", reqBean.getRefCode());
			  params.put("depositAmount",reqBean.getDepositAmt());
	    JSONObject responseObj =Utility.sendCallApi(method, params, "3");
	    OLADepositResponseBean resBean = new OLADepositResponseBean();
	    if(responseObj==null){ 
	    	resBean.setSuccess(false);
	    }else if(responseObj.getBoolean("isSuccess")){
	    	resBean.setSuccess(responseObj.getBoolean("isSuccess"));
	    	resBean.setRefTxnId(String.valueOf(responseObj.getLong("transactioId")));
	    }else{
	    	resBean.setSuccess(false);
	    	int errorCode = responseObj.getInt("errorCode");
			if(errorCode == 511){
				resBean.setReponseCode(LMSErrors.MIN_DEPOSIT_LIMIT_ERROR_CODE);
			}
			if(errorCode == 307){
				resBean.setReponseCode(LMSErrors.PLAYER_BLOCK_DEPOSIT_ERROR_CODE);
			}
	    }	    	
	    return resBean;	
	}
	
	public static OlaPlayerRegistrationResponseBean newPMSPlayerRegistration(OlaPlayerRegistrationRequestBean playerBean) throws LMSException{
		OlaPlayerRegistrationResponseBean registrationResponseBean=new OlaPlayerRegistrationResponseBean();
		// Call Player Mgmt Api 
		String method = "playerRegistrationAction";
		JSONObject params = new JSONObject();
        params.put("firstName",playerBean.getFirstName()==null?"":playerBean.getFirstName());
        params.put("lastName", playerBean.getLastName()==null?"":playerBean.getLastName());
        if("M".equalsIgnoreCase(playerBean.getGender())){
        	 params.put("gender","MALE");
        }else if("F".equalsIgnoreCase(playerBean.getGender())){
        	 params.put("gender","FEMALE");
        }else{
        	params.put("gender","");
        }
		try{
			if(playerBean.getDateOfBirth()==null || playerBean.getDateOfBirth()==""){
				params.put("dateOfBirth","");
			}else{
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");	
				SimpleDateFormat sf1 = new SimpleDateFormat("dd-MM-yyyy");
				String birthdate=sf1.format(sf.parse(playerBean.getDateOfBirth()));
				params.put("dateOfBirth",birthdate);
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}
        params.put("userName",playerBean.getUsername()==null?"":playerBean.getUsername());
        params.put("emailId",playerBean.getEmail()==null?"":playerBean.getEmail());
        params.put("mobileNum",playerBean.getPhone()==null?"":playerBean.getPhone());
        params.put("address",playerBean.getAddress()==null?"":playerBean.getAddress());
        params.put("city",playerBean.getCity()==null?"":playerBean.getCity());
        params.put("state", playerBean.getState()==null?"":playerBean.getState());
        params.put("country", playerBean.getCountry()==null?"":playerBean.getCountry());
        JSONObject responseObj =Utility.sendCallApi(method, params, "7");
        if(responseObj==null){
        	throw new LMSException(LMSErrors.PMS_REG_PLAYER_FAILED_ERROR_CODE);
        }
        boolean isSuccess=false;
        isSuccess= responseObj.getBoolean("isSuccess");  
       if(isSuccess){
    		playerBean.setAccountId(responseObj.getInt("playerId")+"");
    		playerBean.setPassword(responseObj.getString("password")+"");
    		registrationResponseBean.setSuccess(true);
  	 	}else{
    	   throw new LMSException(LMSErrors.PMS_REG_PLAYER_FAILED_ERROR_CODE);
       }	
		return registrationResponseBean;
	}
	
	public static OlaPTResponseBean checkWithdrawalRequest(OLAWithdrawalRequestBean reqBean ) throws LMSException{
		//call Plr Mgmt API set IMS Transaction id 
		String method ="PlayerWithdrawlVerification";
			JSONObject params = new JSONObject();
			params.put("verificationCode",reqBean.getAuthenticationCode());
			params.put("withdrawlAmount",reqBean.getWithdrawlAmt());
			params.put("transactionId",reqBean.getTxnId());
			params.put("refCode",reqBean.getRefCode());
		JSONObject responseObj =Utility.sendCallApi(method, params, "7");
		OlaPTResponseBean respBean = new OlaPTResponseBean();
		if(responseObj==null){
			throw new LMSException(LMSErrors.PMS_CONNECTION_ERROR_CODE);	
		}
		else{
			boolean isSuccess = responseObj.getBoolean("isSuccess");
			if(isSuccess){
				respBean.setImsWithdrawalTransactionId(Long.parseLong(responseObj.getString("refTransactionId")));
				respBean.setWithdrawalStatus("APPROVED");
				respBean.setSuccess(true);
			}else{
				int errorCode = responseObj.getInt("errorCode");
				if(errorCode == 126){
					throw new LMSException(LMSErrors.INVALID_WITHDRAWL_VERIFICATION_ERROR_CODE);
				}
				if(errorCode == 307){
					throw new LMSException(LMSErrors.PLAYER_BLOCK_WDRWL_ERROR_CODE);
				}
				throw new LMSException(LMSErrors.PMS_WITHDRAWL_DENY_ERROR_CODE);			
			}
		}			
		return respBean;
	}
	
	public OlaPlayerRegistrationRequestBean getPlayerInfo(String refCode) throws LMSException  {
		boolean isSuccess=false;
		// Call Player Mgmt Api  
		String method = "getPlayerInfo";
		JSONObject params = new JSONObject();
        params.put("refCode",refCode);
        JSONObject responseObj =Utility.sendCallApi(method, params, "9");
        OlaPlayerRegistrationRequestBean plrDataBean = null;
		if(responseObj==null){
			throw new LMSException(LMSErrors.PMS_CONNECTION_ERROR_CODE);
		}else{
			 isSuccess = responseObj.getBoolean("isSuccess");
			 if(isSuccess){
				 plrDataBean = new OlaPlayerRegistrationRequestBean();
				 plrDataBean.setSuccess(true);
				 plrDataBean.setAccountId(responseObj.getString("playerId"));
				 plrDataBean.setPhone(responseObj.getString("mobileNum"));
				 plrDataBean.setUsername(responseObj.getString("playerName"));
				 if(responseObj.has("firstName")){
					 plrDataBean.setFirstName(responseObj.getString("firstName"));
				 }
				 if(responseObj.has("lastName")){
					 plrDataBean.setLastName(responseObj.getString("lastName"));
				 }
				 if(responseObj.has("emailId")){
					 plrDataBean.setEmail(responseObj.getString("emailId"));
				 }
				 if(responseObj.has("gender")){
					 plrDataBean.setGender(responseObj.getString("gender"));
				 }
				 plrDataBean.setPlrRegDate(responseObj.getString("registrationDate"));
				 if(responseObj.has("address")){
					 plrDataBean.setAddress(responseObj.getString("address"));
				 }
				 plrDataBean.setCountry(responseObj.getString("country"));
				 if(responseObj.has("state")){
					 plrDataBean.setState(responseObj.getString("state"));
				 }
				 if(responseObj.has("city")){
					 plrDataBean.setCity(responseObj.getString("city"));
				 }
				 return plrDataBean;
			 }else{
				 throw new LMSException (LMSErrors.OLA_REG_ERROR_CODE);
			 }
		}		 
	}
	
}
