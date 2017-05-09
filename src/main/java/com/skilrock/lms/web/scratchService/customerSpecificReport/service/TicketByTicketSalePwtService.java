package com.skilrock.lms.web.scratchService.customerSpecificReport.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt;
import com.skilrock.lms.web.scratchService.customerSpecificReport.dao.TicketByTicketSalePwtDao;

public class TicketByTicketSalePwtService {
	
	private static final String AGENT_WISE = "Agent Wise";
	private static final String RETAILER_WISE = "Retailer Wise";
	private static final String REGIONAL_OFFICE_WISE = "Regional Office Wise";
	private static final int CHECK_FOR_ALL = -1;

	public Map<String, Map<Integer, TicketByTicketSalePwt>> getTicketByTicketSaleNPwt(String reportType, int agentOrgId, Timestamp startDate, Timestamp endDate, int roleId) throws LMSException{
		Connection con = null;
		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")))
			 con = DBConnect.getConnection();
		else
			 con = DBConnectReplica.getConnection();
		
		Map<String, Map<Integer, TicketByTicketSalePwt>> orgWiseTicketByTicketSalePwt = null;
		if(AGENT_WISE.equalsIgnoreCase(reportType.trim())){
			if(agentOrgId == CHECK_FOR_ALL){
				orgWiseTicketByTicketSalePwt = TicketByTicketSalePwtDao.ticketByTicketSaleForAllAgentWise(startDate, endDate, con,roleId);
				TicketByTicketSalePwtDao.getPwtAgentWise(orgWiseTicketByTicketSalePwt, startDate, endDate, con,roleId);

			}else{
				orgWiseTicketByTicketSalePwt = TicketByTicketSalePwtDao.ticketByTicketSaleForSingleAgentWise(agentOrgId, startDate, endDate, con);
				TicketByTicketSalePwtDao.getPwtForAgent(orgWiseTicketByTicketSalePwt, startDate, endDate, con,agentOrgId);

			}
						
		}else if(RETAILER_WISE.equalsIgnoreCase(reportType.trim())){
			if(agentOrgId == CHECK_FOR_ALL){
				orgWiseTicketByTicketSalePwt = TicketByTicketSalePwtDao.ticketByTicketSaleRetailerWiseForAllAgent(startDate, endDate, con, roleId);
				TicketByTicketSalePwtDao.getPwtRetailerWise(orgWiseTicketByTicketSalePwt, startDate, endDate, con,roleId);

			}else{
				orgWiseTicketByTicketSalePwt = TicketByTicketSalePwtDao.ticketByTicketSaleRetailerWiseForSingleAgent(agentOrgId, startDate, endDate, con);
				TicketByTicketSalePwtDao.getPwtForSingleAgentRetailerWise(orgWiseTicketByTicketSalePwt, startDate, endDate, con,agentOrgId);
			}

		}else if (REGIONAL_OFFICE_WISE.endsWith(reportType.trim())){
			
			orgWiseTicketByTicketSalePwt = TicketByTicketSalePwtDao.ticketByTicketSaleForAllAgentRegionalOfficeWise(startDate, endDate, con,roleId);
			TicketByTicketSalePwtDao.getPwtAgentRegionalOfficeWise(orgWiseTicketByTicketSalePwt, startDate, endDate, con);
		}
		return orgWiseTicketByTicketSalePwt;
	}
	
	public Map<Integer, String>  getGameMap() throws LMSException{
		return TicketByTicketSalePwtDao.getGameMap();		
	}

}
