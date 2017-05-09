package com.skilrock.lms.admin.common;

import java.sql.Connection;
import com.skilrock.lms.common.db.DBConnect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserIdMappingBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;

public class GenSecurityCodeHelper {
	
	static Log logger = LogFactory.getLog(GenSecurityCodeHelper.class);
	public String checkAndGenerateNewSecurityCode(int userId , int doneByUserId , /*int codeExpiryDays ,*/boolean isAll ,boolean isGenPlaceLMS ,int noOfExpDays ,String requesInitiateTime,String activity){
		
		Connection con = null;
		String status  = "ERROR IN THE SYSTEM !!! ";
		UserIdMappingBean userIdMappingBean = null;
			try {
				userIdMappingBean = new UserIdMappingBean();
				userIdMappingBean.setAll(isAll);
				userIdMappingBean.setUserId(userId);
				userIdMappingBean.setSpecific(!isAll);
				userIdMappingBean.setExpiryDays(noOfExpDays);
				userIdMappingBean.setThirdPartyGeneration(isGenPlaceLMS);
				userIdMappingBean.setActivity(activity);
				userIdMappingBean.setDoneByUserId(doneByUserId);
				//userIdMappingBean.setExpiryDays(codeExpiryDays);
				//userIdMappingBean.setThirdPartyGeneration(false);
				userIdMappingBean.setRequesInitiateTime(requesInitiateTime);
				con = DBConnect.getConnection();
				// CODE TO GENERATE THE ID
				CommonMethods.getUserIdToGenMappindId(userIdMappingBean ,con);
				status = "SUCCESS";
			}catch (LMSException e) {
				status = e.getErrorMessage();
				logger.error(e.getErrorMessage());
			} catch (Exception e) {
				logger.error("EXCEPTION : - " , e);
			}finally{
				DBConnect.closeCon(con);
			}
			return status;
		}
}
