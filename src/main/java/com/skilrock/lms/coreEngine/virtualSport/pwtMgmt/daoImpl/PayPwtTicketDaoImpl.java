package com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;

public class PayPwtTicketDaoImpl {
	private static PayPwtTicketDaoImpl classInstance = null;
	private static Logger logger = LoggerFactory.getLogger(PayPwtTicketDaoImpl.class);
	
	public static PayPwtTicketDaoImpl getInstance() {
		if (classInstance == null)
			classInstance = new PayPwtTicketDaoImpl();
		return classInstance;
	}
	
	
	
	public static double fetchVSCommOfOrganization(int gameId, int orgId, String commType, String orgType, Connection con) throws VSException {
		double commAmt = 0.0;
		String fetCommAmount = null;
		PreparedStatement fetCommAmountPstmt = null;
		ResultSet rs = null;
		try{
		if ("PWT".equalsIgnoreCase(commType)) {
			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.default_pwt_comm_rate, b.pwt_comm_variance , (ifnull(b.pwt_comm_variance, 0)+ a.default_pwt_comm_rate) 'total_comm' from (select game_id ,retailer_pwt_comm_rate as default_pwt_comm_rate from st_vs_game_master where game_id = ?) a  left join ( select retailer_org_id, pwt_comm_variance, game_id from st_vs_agent_retailer_sale_pwt_comm_variance where game_id = ? and  retailer_org_id = ?) b on a.game_id = b.game_id ";
				logger.debug("PWT Ret Commision Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.default_pwt_comm_rate, b.pwt_comm_variance , (ifnull(b.pwt_comm_variance, 0)+ a.default_pwt_comm_rate) 'total_comm' from (select game_id ,agent_pwt_comm_rate as default_pwt_comm_rate from st_vs_game_master where game_id = ?) a  left join (select agent_org_id, pwt_comm_variance, game_id from st_vs_bo_agent_sale_pwt_comm_variance where game_id = ? and  agent_org_id = ?) b on a.game_id = b.game_id ";
				logger.debug("PWT Agt Commision Variance.");
			} else {
				logger.error("ERROR:: Org type is not Defined properly :: " + orgType);
			}
		} else {
			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.default_sale_comm_rate, b.sale_comm_variance, (ifnull(b.sale_comm_variance, 0) + a.default_sale_comm_rate) 'total_comm' from (select game_id ,retailer_sale_comm_rate as default_sale_comm_rate from st_vs_game_master where game_id = ?) a left join (select retailer_org_id, sale_comm_variance, game_id from st_vs_agent_retailer_sale_pwt_comm_variance where game_id = ? and  retailer_org_id = ?) b on a.game_id = b.game_id ";
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.game_id, a.default_sale_comm_rate, b.sale_comm_variance, (ifnull(b.sale_comm_variance, 0)+ a.default_sale_comm_rate) 'total_comm' from (select game_id ,agent_sale_comm_rate as default_sale_comm_rate from st_vs_game_master where game_id = ?) a  left join ( select agent_org_id, sale_comm_variance, game_id from st_vs_bo_agent_sale_pwt_comm_variance where game_id = ? and  agent_org_id = ?) b on a.game_id = b.game_id ";
			} else {
				logger.error("ERROR:: Org type is not Defined properly :: " + orgType);
				logger.debug("ERROR:: Org type is not Defined properly :: " + orgType);
			}
		}
		fetCommAmountPstmt = con.prepareStatement(fetCommAmount);
		fetCommAmountPstmt.setInt(1, 1);
		fetCommAmountPstmt.setInt(2, 1);
		fetCommAmountPstmt.setInt(3, orgId);
		rs = fetCommAmountPstmt.executeQuery();
		if (rs.next()) {
			commAmt = rs.getDouble("total_comm");
		}
		} catch(SQLException se){
			se.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			logger.debug(" commAmt = " + commAmt + " ,   fetCommAmountPStmt = " + fetCommAmountPstmt);
			DBConnect.closeConnection(fetCommAmountPstmt, rs);
		}
		return commAmt;
	}
	
	
}