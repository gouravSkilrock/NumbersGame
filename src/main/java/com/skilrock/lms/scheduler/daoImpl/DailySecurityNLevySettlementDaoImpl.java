package com.skilrock.lms.scheduler.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.web.drawGames.common.Util;

public class DailySecurityNLevySettlementDaoImpl{
	static Log logger=LogFactory.getLog(ResponsibleGaming.class);
	
	private static DailySecurityNLevySettlementDaoImpl classInstance;

	public static synchronized DailySecurityNLevySettlementDaoImpl getInstance() {
		if(classInstance == null)
			classInstance = new DailySecurityNLevySettlementDaoImpl();
		return classInstance;
	}
	
	public void updateCollectedSdofAllRetailer(String startDate,String endDate,Connection con) throws LMSException{
		Statement stmt=null;
		String updateQry=null;
		String gameQuery=null;
		ResultSet rs=null;
		int updatedRow=0;
		try{
			logger.info("INSIDE DailyUpdateLevyNSecuityDeposit_SCHEDULER updateCollectedSdofAllRetailer Method ");
			gameQuery = " select game_id from st_dg_game_master where closing_time > '"+startDate+"' or closing_time is null";
			stmt = con.createStatement();
			int gameId;
			rs = stmt.executeQuery(gameQuery);
			while(rs.next()){
				gameId=rs.getInt("game_id");
				updateQry="UPDATE  st_lms_organization_security_levy_master slm INNER JOIN (SELECT mainSaleTB.organization_id, (saleSd-cancelSd) collectedSd FROM (SELECT organization_id,ifnull(saleSd,0.0) saleSd FROM st_lms_organization_master om LEFT OUTER JOIN (SELECT  s.retailer_org_id  sorgid,sum(ret_sd_amt)  saleSd FROM st_dg_ret_sale_"+gameId+" s INNER JOIN st_lms_retailer_transaction_master t ON s.transaction_id=t.transaction_id  WHERE t.transaction_date>'"+startDate+"' and t.transaction_date <='"+endDate+"' GROUP BY sorgid  ) retSaleTB ON om.organization_id=retSaleTB.sorgid WHERE om.organization_type='RETAILER') mainSaleTB INNER JOIN ( SELECT organization_id,ifnull(cancelSd,0.0) cancelSd FROM st_lms_organization_master om LEFT OUTER JOIN (SELECT s.retailer_org_id  corgid,sum(ret_sd_amt)  cancelSd FROM st_dg_ret_sale_refund_"+gameId+" s INNER JOIN st_lms_retailer_transaction_master t ON s.transaction_id=t.transaction_id  WHERE t.transaction_date>'"+startDate+"' AND t.transaction_date <='"+endDate+"' GROUP BY corgid) retCancelTB  ON om.organization_id=retCancelTB.corgid WHERE om.organization_type='RETAILER') mainCancelTB ON mainSaleTB.organization_id=mainCancelTB.organization_id )mainTB ON slm.organization_id=mainTB.organization_id   SET collected_security_deposit=collected_security_deposit+collectedSd where collectedSd <>0";
				stmt=con.createStatement();
				updatedRow=stmt.executeUpdate(updateQry);
				logger.info(updatedRow+" Records updated in st_lms_organization_security_levy_master ");
			}
		}catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		
	}
	
	public void updateLevyNSdRateInOrgLimit(Connection con) throws LMSException{
		Statement stmt=null;
		String updateQry=null;
		String qry=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		int userId=0;
		int updatedRow=0;
		List<Integer> orgIdList = new ArrayList<Integer>();
		try{
			logger.info("inside DailyUpdateLevyNSecuityDeposit_SCHEDULER updateLevyNSdRateInOrgLimit method");
			qry="SELECT slm.organization_id,slm.expected_security_deposit,slm.collected_security_deposit,security_deposit_rate,levy_cat_type,levy_rate FROM st_lms_organization_security_levy_master slm INNER JOIN st_lms_oranization_limits ol ON slm.organization_id=ol.organization_id WHERE (levy_cat_type='CAT-1' AND levy_rate="+Utility.getPropertyValue("LEVY_CAT-2_PERCENTAGE")+") OR (levy_cat_type='CAT-2' AND levy_rate="+Utility.getPropertyValue("LEVY_CAT-1_PERCENTAGE")+") OR (collected_security_deposit>=expected_security_deposit AND security_deposit_rate="+Utility.getPropertyValue("SECURITY_DEPOSIT_RATE")+")";
			stmt=con.createStatement();
			logger.info("org_id list that need to be updated:"+qry);
			rs=stmt.executeQuery(qry);
			while(rs.next()){
				orgIdList.add(rs.getInt("organization_id"));
			}
			if(orgIdList.size()>0){
				pstmt = con.prepareStatement("INSERT into st_lms_oranization_limits_history(date_changed,change_by_user_id,organization_id,verification_limit,approval_limit,pay_limit,scrap_limit,ola_deposit_limit,ola_withdrawal_limit,max_daily_claim_amt,claim_any_ticket,is_acting_as_bo_for_pwt,levy_rate,security_deposit_rate) SELECT ?,?,organization_id,verification_limit,approval_limit,pay_limit,scrap_limit,ola_deposit_limit,ola_withdrawal_limit,max_daily_claim_amt,claim_any_ticket,is_acting_as_bo_for_pwt,levy_rate,security_deposit_rate FROM st_lms_oranization_limits WHERE FIND_IN_SET(organization_id,?);");
				pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
				pstmt.setInt(2, userId);
				pstmt.setString(3, orgIdList.toString().replace("[", "").replace("]", "").replace(" ", ""));
				logger.info("INSERT Limit history Table:"+pstmt);
				updatedRow=pstmt.executeUpdate();
				logger.info("insert >>"+updatedRow+" << Rows in st_lms_oranization_limits_history table");
				
				updateQry="UPDATE st_lms_oranization_limits ol INNER JOIN   st_lms_organization_security_levy_master slm on ol.organization_id=slm.organization_id set levy_rate=IF(slm.levy_cat_type='CAT-1',"+Utility.getPropertyValue("LEVY_CAT-1_PERCENTAGE")+","+Utility.getPropertyValue("LEVY_CAT-2_PERCENTAGE")+"),security_deposit_rate=IF(slm.collected_security_deposit>=expected_security_deposit,0,"+Utility.getPropertyValue("SECURITY_DEPOSIT_RATE")+")";
				stmt=con.createStatement();
				logger.info("UPDATE Limit  Table:"+updateQry);
				updatedRow=stmt.executeUpdate(updateQry);
				logger.info("UPDATE >>"+updatedRow+" << Rows in st_lms_oranization_limits table");
			}else{
				logger.info("No changes Done By DailyUpdateLevyNSecuityDeposit_SCHEDULER In Limit Table");
			}
			
		}catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt, rs);
			DBConnect.closeStmt(stmt);
		}
		
	}
}