package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.beans.CustomTransactionReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;


public class Transaction_5_90_ReportHelper {
	Log logger = LogFactory.getLog(Transaction_5_90_ReportHelper.class);
	
	
	public void collectionTransactionWise(Timestamp startDate, Timestamp endDate,
			Connection con,	Map<String, CustomTransactionReportBean> retailerMap, int retOrgId,int gameId)
			throws LMSException {
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	try {
			String transactionQuery="select transaction_id,ticket_nbr,transaction_date,transaction_type,amount,ref_transaction_id from(select rtm.transaction_id,ticket_nbr,transaction_date,transaction_type,net_amt as amount,'ref_transaction_id' from st_lms_retailer_transaction_master rtm,st_dg_ret_sale_? srs where rtm.transaction_id=srs.transaction_id  and transaction_date>=? and " 
			+	"transaction_date <= ? and rtm.retailer_org_id=? union " 
			+ "select rtm.transaction_id,ticket_nbr,transaction_date,transaction_type,net_amt as amount,ref_transaction_id from st_lms_retailer_transaction_master rtm,st_dg_ret_sale_refund_? srs where rtm.transaction_id=srs.transaction_id  and transaction_date>=?  and rtm.retailer_org_id=?) transactionTlb order by transaction_date asc, transaction_id ";
			pstmt=con.prepareStatement(transactionQuery);
			pstmt.setInt(1, gameId);
			pstmt.setTimestamp(2, startDate);
			pstmt.setTimestamp(3, endDate);
			pstmt.setInt(4, retOrgId);
			pstmt.setInt(5, gameId);
			pstmt.setTimestamp(6, startDate);
			pstmt.setInt(7, retOrgId);
			logger.debug("transaction Query::"+pstmt);
			rs=pstmt.executeQuery();
			while(rs.next()){
				Timestamp transactionDate=rs.getTimestamp("transaction_date");

				if(!((startDate.before(transactionDate) && endDate.after(transactionDate)) || (retailerMap.containsKey(rs.getString("ref_transaction_id"))))){
					continue;
				}
				String transactionType=rs.getString("transaction_type");
				CustomTransactionReportBean transactionBean=new CustomTransactionReportBean();
				transactionBean.setDate(rs.getString("transaction_date").substring(0,rs.getString("transaction_date").length()-2));
				transactionBean.setRefTransId(rs.getString("ref_transaction_id"));
				transactionBean.setTransactionType(transactionType);
				
				String ticketNo=rs.getString("ticket_nbr");
				if(ticketNo !="0"){
					if(ticketNo.length()==14){
						transactionBean.setTransactionNo(ticketNo.substring(8, 12));
					}else if (ticketNo.length()==17){
						if("NEWTKTFORMAT".equalsIgnoreCase(Util.getTicketNumberFormat(ticketNo+Util.getRpcAppenderForTickets(ticketNo.length())))){
							transactionBean.setTransactionNo(ticketNo.substring(14,17));
						}else{
							transactionBean.setTransactionNo(ticketNo.substring(9,13));
						}
					}else {
						transactionBean.setTransactionNo("----");
					}
					
					
				}else{
					transactionBean.setTransactionNo("----");
				}
				if("DG_SALE".equalsIgnoreCase(transactionType)){
					transactionBean.setStatus("Successful");
					transactionBean.setGameName("Sale");
					transactionBean.setAmount(rs.getDouble("amount"));
				}else if("DG_REFUND_CANCEL".equalsIgnoreCase(transactionType) || "DG_REFUND_FAILED".equalsIgnoreCase(transactionType)){
					transactionBean.setStatus("Successful");
					transactionBean.setGameName("REFUND");
					transactionBean.setAmount(-(rs.getDouble("amount")));
					if(retailerMap.containsKey(rs.getString("ref_transaction_id"))){
					retailerMap.get(rs.getString("ref_transaction_id")).setStatus("Unsuccessful");
					}
				}
				if(startDate.before(transactionDate) && endDate.after(transactionDate)){
					retailerMap.put(rs.getString("transaction_id"),transactionBean);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}
	
	
	
	
	
	

	public Map<String, CustomTransactionReportBean> collectionTransactionWiseWithOpeningBal(Timestamp startDate, Timestamp endDate,int retOrgId,int agentOrgId,int gameId) throws LMSException {
		
		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CustomTransactionReportBean> retailerMap = new LinkedHashMap<String, CustomTransactionReportBean>();
	try {
			
			con = DBConnect.getConnection();
					
			collectionTransactionWise(startDate, endDate, con,retailerMap,retOrgId,gameId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionAgentWiseWithOpeningBal");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retailerMap;
	}


}
