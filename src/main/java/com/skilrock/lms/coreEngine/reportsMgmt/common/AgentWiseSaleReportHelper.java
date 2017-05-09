package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.beans.AgentWiseSaleBean;

public class AgentWiseSaleReportHelper {
	static Log logger = LogFactory.getLog(AgentWiseSaleReportHelper.class);

	public void fetchReportData(String startDate,String endDate,Map<Integer,AgentWiseSaleBean> agentWiseSaleCommMap) throws LMSException{
		Connection con=null;
		PreparedStatement pstmt=null;
		Statement stmt=null;
		double beforeLevyComm = 0.0;
		double afterLevyComm = 0.0;
		CallableStatement cstmt = null;
		ResultSet rs=null;
		double agentLevyComm = 0.0;
		ResultSet gameRs=null;
		AgentWiseSaleBean saleBean=null;
		
		try{
			con=DBConnect.getConnection();
			String levyDateChangeQuery="SELECT olm.organization_id,olm.levy_rate,IFNULL(new_levy,0.00) new_levy,IFNULL(old_levy,0.00)old_levy,date_changed FROM st_lms_oranization_limits olm LEFT JOIN (SELECT ol.organization_id, date_changed,ol.levy_rate new_levy ,olh.levy_rate old_levy  FROM st_lms_oranization_limits ol  INNER JOIN st_lms_oranization_limits_history olh ON ol.organization_id=olh.organization_id WHERE  ol.levy_rate <> olh.levy_rate   AND  (date_changed >='"+startDate+"' and date_changed <='"+endDate+"' ) order by organization_id) olb ON olm.organization_id=olb.organization_id INNER JOIN st_lms_organization_master om ON om.organization_id=olm.organization_id WHERE om.organization_type='AGENT'";
	        stmt=con.createStatement();
	        logger.info("Agein Wise Sale levyDateChangeQuery:"+levyDateChangeQuery);
	        rs=stmt.executeQuery(levyDateChangeQuery);
	        while(rs.next()){
	        	saleBean=new AgentWiseSaleBean();
	        	saleBean.setDateChanged(rs.getString("date_changed"));
	        	saleBean.setFixLevyRate(rs.getDouble("levy_rate"));
	        	saleBean.setOldLevyRate(rs.getDouble("old_levy"));
	        	saleBean.setNewLevyRate(rs.getDouble("new_levy"));
	        	agentWiseSaleCommMap.put(rs.getInt("organization_id"), saleBean);
  	        }
	        String gameQuery = " select game_id from st_dg_game_master where closing_time > '"+startDate+"' or closing_time is null";
	        String query="SELECT om.organization_id,CONCAT(om.org_code,'_',om.name) agent_name,IFNULL(SUM(mrp_amt),0.0) mrp_amt,IFNULL(SUM(good_cause_amt),0.0) good_cause_amt FROM st_lms_organization_master om LEFT JOIN (SELECT c.organization_id,c.parent_id,(c.mrp_amt-d.mrp_amt) mrp_amt,(c.good_cause_amt-d.good_cause_amt) good_cause_amt FROM(SELECT org_master.organization_id,parent_id,IFNULL(mrp_amt,0.0) mrp_amt,IFNULL(good_cause_amt,0.0) good_cause_amt FROM st_lms_organization_master org_master LEFT JOIN (SELECT sale.retailer_org_id,SUM(mrp_amt) mrp_amt,sum(good_cause_amt) good_cause_amt FROM st_dg_ret_sale_? sale inner join st_lms_retailer_transaction_master  txn_master ON sale.transaction_id=txn_master.transaction_id WHERE transaction_date>=? and transaction_date<=? GROUP BY sale.retailer_org_id) a ON org_master.organization_id=a.retailer_org_id WHERE organization_type=?) c INNER JOIN (select org_master.organization_id,parent_id,IFNULL(mrp_amt,0.0) mrp_amt,IFNULL(good_cause_amt,0.0) good_cause_amt FROM st_lms_organization_master org_master LEFT JOIN (SELECT refund.retailer_org_id,SUM(mrp_amt) mrp_amt,SUM(good_cause_amt) good_cause_amt FROM st_dg_ret_sale_refund_? refund INNER JOIN st_lms_retailer_transaction_master  txn_master on refund.transaction_id=txn_master.transaction_id WHERE transaction_date>=? and transaction_date<=?GROUP BY refund.retailer_org_id) b ON org_master.organization_id=b.retailer_org_id WHERE organization_type=?) d ON c.organization_id=d.organization_id) cd ON cd.parent_id=om.organization_id WHERE organization_type=? GROUP BY om.organization_id";
	        stmt=con.createStatement();
	        gameRs=stmt.executeQuery(gameQuery);
	        while(gameRs.next()){
	        	int gameId=gameRs.getInt("game_id");
			    pstmt=con.prepareStatement(query);			
				pstmt.setInt(1, gameId);
				pstmt.setString(2,startDate);
	            pstmt.setString(3,endDate);
	            pstmt.setString(4,"RETAILER");
	            pstmt.setInt(5, gameId);
	            pstmt.setString(6,startDate);
	            pstmt.setString(7, endDate);
	            pstmt.setString(8, "RETAILER");
	            pstmt.setString(9,"AGENT");
	            logger.info("getSaleCommAgentWise"+pstmt.toString());
	            rs=pstmt.executeQuery();
	            while(rs.next()){
	            	if(agentWiseSaleCommMap.containsKey(rs.getInt("organization_id"))){
	            		agentWiseSaleCommMap.get(rs.getInt("organization_id")).setMrpAmt(agentWiseSaleCommMap.get(rs.getInt("organization_id")).getMrpAmt()+rs.getDouble("mrp_amt"));
		            	agentWiseSaleCommMap.get(rs.getInt("organization_id")).setGovtComm(agentWiseSaleCommMap.get(rs.getInt("organization_id")).getGovtComm()+rs.getDouble("good_cause_amt"));
		            	agentWiseSaleCommMap.get(rs.getInt("organization_id")).setAgentComm((agentWiseSaleCommMap.get(rs.getInt("organization_id")).getMrpAmt()-agentWiseSaleCommMap.get(rs.getInt("organization_id")).getGovtComm())*.01);
		            	agentWiseSaleCommMap.get(rs.getInt("organization_id")).setAgentName(rs.getString("agent_name"));
	            	}
	         
	            }
	            cstmt = con.prepareCall("call getAgentWiseNetSaleBeforeLevyChange(?,?,?)");
	            cstmt.setTimestamp(1, Timestamp.valueOf(startDate));
	            cstmt.setTimestamp(2, Timestamp.valueOf(endDate));
	            cstmt.setInt(3, gameId);
	            logger.info(cstmt);
	            rs=cstmt.executeQuery();
	            
	            while(rs.next()){
	            	if(agentWiseSaleCommMap.get(rs.getInt("organization_id")).getMrpAmt() > 0.0 ){
	            		 if("NULL".equalsIgnoreCase(rs.getString("date_changed")) || rs.getString("date_changed")==null){
	 	 	            	agentLevyComm = agentWiseSaleCommMap.get(rs.getInt("organization_id")).getAgentComm()*agentWiseSaleCommMap.get(rs.getInt("organization_id")).getFixLevyRate()*.01;
	 	 	            	agentWiseSaleCommMap.get(rs.getInt("organization_id")).setAgentLevyComm(agentLevyComm);
	 	 	            } else{
	 	 	            	//Before
	 	 	            	beforeLevyComm = (rs.getDouble("saleMrp")-rs.getDouble("govtComm"))*.01*agentWiseSaleCommMap.get(rs.getInt("organization_id")).getOldLevyRate()*.01;
	 	 	            	//After
	 	 	            	afterLevyComm = ((agentWiseSaleCommMap.get(rs.getInt("organization_id")).getMrpAmt()-rs.getDouble("saleMrp"))-(agentWiseSaleCommMap.get(rs.getInt("organization_id")).getGovtComm()-rs.getDouble("govtComm")))*.01*agentWiseSaleCommMap.get(rs.getInt("organization_id")).getNewLevyRate()*.01;
	 	 	            	//set levy
	 	 	            	agentWiseSaleCommMap.get(rs.getInt("organization_id")).setAgentLevyComm(agentWiseSaleCommMap.get(rs.getInt("organization_id")).getAgentLevyComm()+beforeLevyComm + afterLevyComm);
	 	 	            }
	            	}
	            }
	           
	            
	        }
		}catch(SQLException se){
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con, pstmt,rs);
			DBConnect.closeConnection(stmt, gameRs);
		}
	}
}
