package com.skilrock.lms.scheduler.controllerImpl;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.scheduler.SchedulerCommonFuntionsHelper;
import com.skilrock.lms.scheduler.daoImpl.DailySecurityNLevySettlementDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

public class DailySecurityNLevySettlementControllerImpl{
	 static Log logger=LogFactory.getLog(DailySecurityNLevySettlementControllerImpl.class);
	 
	 private static DailySecurityNLevySettlementControllerImpl classInstance;

		public static synchronized DailySecurityNLevySettlementControllerImpl getInstance() {
			if(classInstance == null)
				classInstance = new DailySecurityNLevySettlementControllerImpl();
			return classInstance;
		}
	 
	 public void settleLevynNSecurityDeposit(Map<String,SchedulerDetailsBean> scheBeanMap,String endDate) throws LMSException{
		 String startDate=null;
		 Connection con=null;
		 try{
			
			 con=DBConnect.getConnection();
			 con.setAutoCommit(false);
			 startDate= SchedulerCommonFuntionsHelper.getStartDateForLastSuccessfulScheduler(scheBeanMap.get("DailyUpdateLevyNSecuityDeposit_SCHEDULER").getJobId(),con);
			 DailySecurityNLevySettlementDaoImpl.getInstance().updateCollectedSdofAllRetailer(startDate,endDate,con);
			 DailySecurityNLevySettlementDaoImpl.getInstance().updateLevyNSdRateInOrgLimit(con);
			 con.commit();
		 }catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
		}
		 
		 
	 }
}