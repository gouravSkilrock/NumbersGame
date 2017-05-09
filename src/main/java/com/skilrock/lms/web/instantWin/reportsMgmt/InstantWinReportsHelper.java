package com.skilrock.lms.web.instantWin.reportsMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.IWUserIncentiveBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;


/**
 * 
 * This is helper class for Instant Win Reports.
 * @author Mukesh
 *
 */
public class InstantWinReportsHelper {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private static Log log = LogFactory.getLog(InstantWinReportsHelper.class);  
    /**
     * This method incentive data for all agents.
     * @param agentMap
     * @param fromDate
     * @param toDate
     * @param reportType
     * @throws LMSException
     * @author Rishi
     */
    public void getAgentData(HashMap<Integer,IWUserIncentiveBean> agentMap, String fromDate, String toDate, String reportType) throws LMSException{
    	log.info("In getAgentData()");
    	con = DBConnect.getConnection();
    	String query = null;
    	IWUserIncentiveBean iwUserIncentiveBean =  null;
    	try{
   			query = "SELECT om.organization_id,name, IFNULL(incentiveAmt,0.0) incentiveAmt,IFNULL(saleAmt,0.0) saleAmt,IFNULL(winAmt,0.0) winAmt FROM st_lms_organization_master om LEFT JOIN (select organization_id,incentiveAmt,saleAmt,winAmt from st_lms_user_master um1 inner join (SELECT um.parent_user_id,IFNULL(SUM(incentive_amt),0.0) incentiveAmt,IFNULL(SUM(sale_amt),0.0) saleAmt,IFNULL(SUM(winning_amt),0.0) winAmt From st_iw_retailer_"+reportType.toLowerCase()+"_incentive_data  rdid INNER JOIN st_lms_user_master um ON rdid.user_name=um.user_name Where start_date >='"+fromDate+" 00:00:00'and end_date <= '"+toDate+" 23:59:59' GROUP BY um.parent_user_id )  re on re.parent_user_id=um1.user_id ) mainTB ON om.organization_id=mainTB.organization_id where organization_type='AGENT' and organization_status <> 'TERMINATE' order by name";
   			log.info("Query to fetch agent data : "+query);
    		ps = con.prepareStatement(query);
    		rs = ps.executeQuery();
    		while(rs.next()){
    			iwUserIncentiveBean = new IWUserIncentiveBean();
    			iwUserIncentiveBean.setOrganizationId(rs.getInt("organization_id"));
    			iwUserIncentiveBean.setOrganizationName(rs.getString("name"));
    			iwUserIncentiveBean.setSale(rs.getDouble("saleAmt"));
    			iwUserIncentiveBean.setWinning(rs.getDouble("winAmt"));
    			iwUserIncentiveBean.setIncentiveAmount(rs.getDouble("incentiveAmt"));
    			agentMap.put(rs.getInt("organization_id"), iwUserIncentiveBean);
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
    	}finally{
    		DBConnect.closeResource(con,ps,rs);
    	}
    }
    
    /**
     * This method fetches incentive data for all retailers.
     * @param retailerMap
     * @param fromDate
     * @param toDate
     * @param parentOrgId
     * @param reportType
     * @param orgName
     * @throws LMSException
     * @author Rishi
     */
    public void getRetailerData(HashMap<Integer,IWUserIncentiveBean> retailerMap, String fromDate, String toDate, int parentOrgId, String reportType, String orgName) throws LMSException{
    	log.info("In getRetailerData()");
    	con = DBConnect.getConnection();
    	String query = null;
    	IWUserIncentiveBean iwUserIncentiveBean = null;
    	try{
    		query = "SELECT om.organization_id,name, IFNULL(incentiveAmt,0.0)incentiveAmt,IFNULL(saleAmt,0.0)saleAmt,IFNULL(winAmt,0.0)winAmt FROM st_lms_organization_master om LEFT JOIN (Select rdid.user_name,um.organization_id,ifnull(sum(sale_amt),0.0) saleAmt,ifnull(sum(winning_amt),0.0) winAmt,ifnull(sum(incentive_amt),0.0) incentiveAmt from st_lms_user_master um INNER JOIN st_iw_retailer_daily_incentive_data rdid ON um.user_name = rdid.user_name Where start_date >='"+fromDate+" 00:00:00'and end_date <= '"+toDate+" 23:59:59'  Group by rdid.user_name) mainTB ON om.organization_id=mainTB.organization_id AND om.organization_type='RETAILER'  Where om.parent_id ="+parentOrgId+" order by name";
    		log.info("Query to fetch retailer data : "+query);
    		ps = con.prepareStatement(query);
    		rs = ps.executeQuery();
    		while(rs.next()){
    			iwUserIncentiveBean = new IWUserIncentiveBean();
    			iwUserIncentiveBean.setOrganizationId(rs.getInt("organization_id"));
    			iwUserIncentiveBean.setOrganizationName(rs.getString("name"));
    			iwUserIncentiveBean.setSale(rs.getDouble("saleAmt"));
    			iwUserIncentiveBean.setWinning(rs.getDouble("winAmt"));
    			iwUserIncentiveBean.setIncentiveAmount(rs.getDouble("incentiveAmt"));
    			iwUserIncentiveBean.setParentOrgName(orgName);
    			retailerMap.put(rs.getInt("organization_id"), iwUserIncentiveBean);
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
    	}finally{
    		DBConnect.closeResource(con,ps,rs);
    	}	
    }
   
}
