package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ChequePaymentBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

public class AgentPaymentChequeHelper {
	static Log logger = LogFactory.getLog(AgentPaymentChequeHelper.class);
	
	//by sumit on 25 june 2014
/*	Map<Integer,String> orgNameResults = new LinkedHashMap<Integer,String>();

	public Map<Integer,String> getOrganizations() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query =" ";
		try {
			 
			connection = DBConnect.getConnection();
	
			String appendOrder =QueryManager.getAppendOrgOrder();
			
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE") ){
				
				
				query ="select org_code orgCode,organization_id from st_lms_organization_master where organization_type=? and organization_status!=?  order by  "+appendOrder;
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
				
				
			query ="select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where organization_type=? and organization_status!=?  order by "+appendOrder;
			
					
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
				
			
				query ="select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where organization_type=? and organization_status!=?  order by  "+appendOrder;
			
				
				
			}else {
							
				query ="select name orgCode,organization_id from st_lms_organization_master where organization_type=? and organization_status!=?  order by  "+appendOrder;
					
				
			}
			statement = connection.prepareStatement(query);
			statement.setString(1,"AGENT");
			statement.setString(2,"TERMINATE");
			
			logger.debug("-----Query----::" + query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				orgNameResults.put(resultSet.getInt("organization_id"), resultSet.getString("orgCode"));
				//orgNameResults.add(resultSet.getString("orgCode"));

			}

			return orgNameResults;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;
	}
*/
	public int getOrgIdFromOrgName(String orgName, Connection conn) {

		int orgId = 0;
		PreparedStatement pstmtOrg;
		String queryGetOrgId = "select organization_id from st_lms_organization_master where name=?";
		try {
			pstmtOrg = conn.prepareStatement(queryGetOrgId);
			pstmtOrg.setString(1, orgName);
			ResultSet idSet = pstmtOrg.executeQuery();
			if (idSet.next()) {
				orgId = idSet.getInt("organization_id");
			}

			return orgId;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgId;
	}

	public void submitChequePaymentTemp(List<ChequePaymentBean> paymetList,
			String boOrgName, int userOrgID, String root_path)
			throws LMSException {

		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement pstmtGetTempId = null;
		PreparedStatement pstmtUpdateTempTable = null;
		PreparedStatement pstmtDupCheque = null;
		ResultSet rs = null;
		String lastTempReceiptId = null;
		int agtOrgId = 0;
		List<String> dupChqNbr = new ArrayList<String>();

		try {
			conn.setAutoCommit(false);
			pstmtGetTempId = conn
					.prepareStatement("select temp_receipt_id from st_lms_bo_cheque_temp_receipt order by temp_receipt_id desc LIMIT 1");
			ResultSet rsTempId = pstmtGetTempId.executeQuery();
			if (rsTempId.next()) {
				lastTempReceiptId = rsTempId.getString(1);
			}

			String autoGenTempReceiptId = null;
			autoGenTempReceiptId = GenerateRecieptNo.getRecieptNo("TCHEQUE",
					lastTempReceiptId, "BO");

			// insert entry intp cheque temporary table

			String updateTempTable = "insert into st_lms_bo_cheque_temp_receipt(temp_receipt_id,cheque_nbr,agent_org_id,cheque_date,cheque_receiving_date,issuing_party_name,drawee_bank,cheque_amt,cheque_status) values(?,?,?,?,?,?,?,?,?)";

			int listSize = paymetList.size();

			// get agt org name and org id

			if (listSize > 0) {
				agtOrgId = Integer.parseInt(paymetList.get(0).getOrgName());//getOrgIdFromOrgName(paymetList.get(0).getOrgName(),conn);
			}
			String duplChqQuery = "select * from st_lms_bo_cheque_temp_receipt where cheque_nbr=? and agent_org_id=? and issuing_party_name=? and drawee_bank=? and cheque_amt=?";

			for (int i = 0; i < listSize; i++) {
				logger
						.debug("das%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				pstmtDupCheque = conn.prepareStatement(duplChqQuery);
				pstmtDupCheque.setString(1, paymetList.get(i).getChequeNo());
				pstmtDupCheque.setInt(2, agtOrgId);
				pstmtDupCheque.setString(3, paymetList.get(i)
						.getIssuePartyname());
				pstmtDupCheque.setString(4, paymetList.get(i).getBankName());
				pstmtDupCheque.setDouble(5, paymetList.get(i).getAmount());

				logger.debug("duplChqQuery:::::::::::::" + pstmtDupCheque);
				rs = pstmtDupCheque.executeQuery();
				if (rs.next()) {
					paymetList.get(i).setChqeueStatus("Not Processed");
				} else {
					// logger.debug("cheque date is :: " +
					// paymetList.get(i).getChequeDate());
					pstmtUpdateTempTable = conn
							.prepareStatement(updateTempTable);
					pstmtUpdateTempTable.setString(1, autoGenTempReceiptId);
					pstmtUpdateTempTable.setString(2, paymetList.get(i)
							.getChequeNo());
					pstmtUpdateTempTable.setInt(3, agtOrgId);
					pstmtUpdateTempTable.setDate(4, paymetList.get(i)
							.getChequeDate());
					pstmtUpdateTempTable.setTimestamp(5,
							new java.sql.Timestamp(new java.util.Date()
									.getTime()));
					pstmtUpdateTempTable.setString(6, paymetList.get(i)
							.getIssuePartyname());
					pstmtUpdateTempTable.setString(7, paymetList.get(i)
							.getBankName());
					pstmtUpdateTempTable.setDouble(8, paymetList.get(i)
							.getAmount());
					pstmtUpdateTempTable.setString(9, "PENDING");
					pstmtUpdateTempTable.executeUpdate();
					paymetList.get(i).setChqeueStatus("Submitted");
				}
			}
			conn.commit();
			if (autoGenTempReceiptId != null) {
				GraphReportHelper graphHelper = new GraphReportHelper();
				graphHelper.createTempChqReceipt(autoGenTempReceiptId,
						boOrgName, userOrgID, root_path);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
					throw new LMSException(se);
				}
			}
		}

	}

	public boolean validateCheque(String bank, String chqnbr)
			throws LMSException {
		StringBuffer st = new StringBuffer();
		st.append(bank).append(chqnbr);
		String bnkChq = st.toString();
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = QueryManager.getST5AgentChequeQuery();
		// String query="select count(*) 'count' from st_lms_agent_sale_chq
		// where drawee_bank='"+bank+"' and chq_nbr="+chqnbr+"";
		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, bank);
			statement.setString(2, chqnbr);
			rs = statement.executeQuery();
			rs.next();
			int count = rs.getInt("count");
			logger.debug("getFetchSize" + count);
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
		return true;
	}

}