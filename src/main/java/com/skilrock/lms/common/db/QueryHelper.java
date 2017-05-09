/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.beans.SearchOrgBean;
import com.skilrock.lms.beans.SupplierBean;
import com.skilrock.lms.beans.UserBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class QueryHelper {
	static Log logger = LogFactory.getLog(QueryHelper.class);
	private Map orgIdGroup;
	private Map userIdGroup;

	public Map getOrgIdGroup() {
		return orgIdGroup;
	}

	public Map getUserIdGroup() {
		return userIdGroup;
	}

	public List SearchCheque(String chequeNumber, long transactionId,
			double chequeBounceCharges) throws LMSException {
		List<ChequeBean> searchResults = new ArrayList<ChequeBean>();
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		 
		try {
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String orgCodeQry = " b.name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " b.org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(b.org_code,'_',b.name)  orgCode";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(b.name,'_',b.org_code)  orgCode ";
			

			}	
			String query = "select a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,"+orgCodeQry+",b.organization_id  from st_lms_bo_sale_chq a, st_lms_organization_master b  where a.cheque_nbr ="
					+ chequeNumber
					+ " and a.agent_org_id =b.organization_id and a.transaction_id="
					+ transactionId;

			logger.debug("-----Query----::" + query);
			// ResultSet resultSet = statement.executeQuery(query);

			resultSet = statement.executeQuery(query);

			ChequeBean chequeBean = new ChequeBean();
			while (resultSet.next()) {
				chequeBean.setChequeNumber(resultSet
						.getString(TableConstants.CHEQUE_NUMBER));
				chequeBean.setChequeDate(resultSet.getDate(
						TableConstants.CHEQUE_DATE).toString());
				chequeBean.setIssuePartyname(resultSet
						.getString(TableConstants.ISSUE_PARTY_NAME));
				chequeBean.setBankName(resultSet
						.getString(TableConstants.DRAWEE_BANK));
				chequeBean.setChequeAmount(resultSet
						.getDouble(TableConstants.CHEQUE_AMT));
				chequeBean.setOrgName(resultSet.getString("orgCode"));
				chequeBean.setOrgId(resultSet.getInt("organization_id"));
				chequeBean.setTransactionId(resultSet
						.getLong(TableConstants.TRANSACTION_ID));
				// chequeBean.setChequeBounceCharges(chequeBounceCharges);
				searchResults.add(chequeBean);

			}
		}

		catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return searchResults;

	}

	public String checkDrawerAvailablity(int userId)
	{
		Connection con = null;
		String returnType = null;
		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select user_name from st_lms_bo_cash_drawer_status ca inner join st_lms_user_master user on ca.cashier_id=user.user_id where cashier_id="+userId+"");
			if(rs.next())
			{
				returnType = "ACTIVE";
			}
			else
			{
				returnType = "INACTIVE";
			}
			
		} catch (SQLException ex) {
			logger.error("Exception",ex);
		} finally {
			try {
				DBConnect.closeCon(con);
			} catch (Exception e) {
				logger.error("Exception",e);
			}
		}
		return returnType;
	}
	public List<ChequeBean> SearchChequeRetailer(String chequeNumber,
			long transactionId, double chequeBounceCharges) throws LMSException {
		List<ChequeBean> searchResults = new ArrayList<ChequeBean>();
		 
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String orgCodeQry = " b.name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " b.org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(b.org_code,'_',b.name)  orgCode";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(b.name,'_',b.org_code)  orgCode ";
			

			}	
			String query = "select a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,"+orgCodeQry+",b.organization_id from st_lms_agent_sale_chq a, st_lms_organization_master b  where a.cheque_nbr ="
					+ chequeNumber
					+ " and a.retailer_org_id =b.organization_id and a.transaction_id="
					+ transactionId;

			logger.debug("-----Query----::" + query);
			// ResultSet resultSet = statement.executeQuery(query);

			resultSet = statement.executeQuery(query);

			ChequeBean chequeBean = new ChequeBean();
			while (resultSet.next()) {
				chequeBean.setChequeNumber(resultSet.getString("cheque_nbr"));
				chequeBean.setChequeDate(resultSet.getDate(
						TableConstants.CHEQUE_DATE).toString());
				chequeBean.setIssuePartyname(resultSet
						.getString(TableConstants.ISSUE_PARTY_NAME));
				chequeBean.setBankName(resultSet
						.getString(TableConstants.DRAWEE_BANK));
				chequeBean.setChequeAmount(resultSet
						.getDouble(TableConstants.CHEQUE_AMT));
				//chequeBean.setOrgName(resultSet.getString(TableConstants.NAME));
				chequeBean.setOrgName(resultSet.getString("orgCode"));
				chequeBean.setOrgId(resultSet.getInt("organization_id"));
				chequeBean.setTransactionId(resultSet
						.getLong(TableConstants.TRANSACTION_ID));
				// chequeBean.setChequeBounceCharges(chequeBounceCharges);
				searchResults.add(chequeBean);

			}
		}

		catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}
		return searchResults;

	}

	public List searchOrganization() throws LMSException {

		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<SearchOrgBean> searchResults = new ArrayList<SearchOrgBean>();
		List<UserBean> usersearchResults = new ArrayList<UserBean>();
		List orgName = null;
		try {

			SearchOrgBean orgBean = null;
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query = QueryManager.getST5PartyTypeQuery()
					+ " order by name";
			logger.debug("-----Query----::" + query);
			orgName = new ArrayList();
			resultSet = statement.executeQuery(query);
			orgIdGroup = new HashMap();

			while (resultSet.next()) {

				orgBean = new SearchOrgBean();
				orgBean.setOrganization_id(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				orgBean.setName(resultSet.getString(TableConstants.NAME));
				searchMap.put(resultSet.getString(TableConstants.NAME),
						resultSet.getInt(TableConstants.ORGANIZATION_ID));
				orgName.add(resultSet.getString(TableConstants.NAME));
				this.setOrgIdGroup(searchMap);
				// logger.debug("OrgId:" +
				// resultSet.getInt(TableConstants.ORGANIZATION_ID));
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return orgName;

	}

	/**
	 * This method id for finding Organization Names for the Retailer under the
	 * logged in Agent.
	 * 
	 * @return List of Org
	 */
	public List searchOrganizationForRetailer(int agentId) throws LMSException {

		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List orgName = null;
		List<SearchOrgBean> searchResults = new ArrayList<SearchOrgBean>();
		List<UserBean> usersearchResults = new ArrayList<UserBean>();
		try {

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query = QueryManager.getST5PartyTypeForAgentQuery()
					+ " and parent_id=" + agentId + " order by name";
			logger.debug("-----Query1----::" + query);
			orgName = new ArrayList();
			resultSet = statement.executeQuery(query);
			orgIdGroup = new HashMap();

			while (resultSet.next()) {

				// orgBean = new SearchOrgBean();
				// orgBean.setOrganization_id(resultSet.getInt(TableConstants.ORGANIZATION_ID));
				// orgBean.setName(resultSet.getString(TableConstants.NAME));
				// searchMap.put(resultSet.getString(TableConstants.NAME),resultSet.getInt(TableConstants.ORGANIZATION_ID));

				logger.debug(resultSet.getString(TableConstants.NAME));
				orgName.add(resultSet.getString(TableConstants.NAME));
				// this.setOrgIdGroup(searchMap);
				// logger.debug("OrgId:" +
				// resultSet.getInt(TableConstants.ORGANIZATION_ID));
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return orgName;

	}


	public List SearchSupplier() throws LMSException {
		List<SupplierBean> searchResults = new ArrayList<SupplierBean>();
		 
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		List supplierName = new ArrayList();
		try {
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			// String query="select
			// a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,b.name
			// from st_lms_bo_sale_chq a, st_lms_organization_master b where
			// a.cheque_nbr ="+chequeNumber+" and a.agent_org_id
			// =b.organization_id and a.transaction_id="+transactionId;

			String query = QueryManager.getST5SupplierQuery();
			resultSet = statement.executeQuery(query);

			SupplierBean supplierBean = new SupplierBean();
			while (resultSet.next()) {
				// supplierBean.setChequeNumber(resultSet.getInt(TableConstants.CHEQUE_NUMBER));
				// supplierBean.setChequeDate(resultSet.getDate(TableConstants.CHEQUE_DATE));
				// supplierBean.setIssuePartyname(resultSet.getString(TableConstants.ISSUE_PARTY_NAME));
				// supplierBean.setBankName(resultSet.getString(TableConstants.DRAWEE_BANK));
				// supplierBean.setChequeAmount(resultSet.getDouble(TableConstants.CHEQUE_AMT));
				// supplierBean.setOrgName(resultSet.getString(TableConstants.NAME));
				// supplierBean.setTransactionId(resultSet.getInt(TableConstants.TRANSACTION_ID));
				supplierName.add(resultSet.getString("name"));
				// searchResults.add(chequeBean);

			}
		}

		catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}
		return supplierName;

	}

	public List searchUser() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<UserBean> usersearchResults = null;

		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		List userList = new ArrayList();
		try {

			UserBean userBean = null;
			usersearchResults = new ArrayList<UserBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query = QueryManager.getST5UerNameIdGroupQuery();
			logger.debug("-----Query----::" + query);
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				userBean = new UserBean();
				userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				userBean.setUserName(resultSet
						.getString(TableConstants.USER_NAME));
				userList.add(resultSet.getString(TableConstants.USER_NAME));
				searchMap.put(resultSet.getString(TableConstants.USER_NAME),
						resultSet.getInt(TableConstants.USER_ID));
				// searchMap.put(Constants.USER_NAME,resultSet.getString(TableConstants.USER_NAME));
				this.setUserIdGroup(searchMap);

				usersearchResults.add(userBean);

				logger.debug("User Name :"
						+ resultSet.getString(TableConstants.USER_NAME));
				logger.debug("User Id :"
						+ resultSet.getString(TableConstants.USER_ID));
			}

		}
		/*
		 * 
		 * if(searchMap != null){ keySet = searchMap.keySet();
		 * 
		 * Iterator itr = keySet.iterator(); String key = null; String strValue;
		 * 
		 * int fieldAdded = 0;
		 * 
		 * whereClause.append(" where ");
		 * 
		 * while(itr.hasNext()){ key = (String)itr.next();
		 * 
		 * 
		 * if(key.equals(GameContants.GAME_NAME)){ strValue = (String)
		 * searchMap.get(key); logger.debug("Game Name"+strValue); if(strValue!=
		 * null && !strValue.equals("")){ if(fieldAdded > 0){
		 * whereClause.append(" and "); }
		 * 
		 * whereClause.append(TableConstants.GAME_NAME); whereClause.append("
		 * like '"); whereClause.append(strValue.trim()); whereClause.append("%'
		 * ");
		 * 
		 * fieldAdded++; } }
		 * 
		 */

		catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return userList;
	}

	public ArrayList<String> searchOrganizationForAllRetailer() throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> orgName = null;
		try {
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query ="select organization_id,organization_type,name from st_lms_organization_master where organization_type='RETAILER' and organization_status in ('ACTIVE','INACTIVE') order by name";
			logger.debug("-----Query1----::" + query);
			orgName = new ArrayList<String>();
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				orgName.add(resultSet.getString(TableConstants.NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return orgName;

	}
	
	public String getLmcIdOfRetailer(String retName) throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		String lmcName=null;
		//retName=retName+"%";
		try {
			String orgCodeQry = " om1.name orgCode,om.name parentorgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " om1.org_code orgCode,om.org_code parentorgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(om1.org_code,'_',om1.name)  orgCode,concat(om.org_code,'_',om.name)  parentorgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(om1.name,'_',om1.org_code)  orgCode,concat(om.name,'_',om.org_code)  parentorgCode ";
			

			}
			
			
			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("select "+orgCodeQry+",om1.organization_id retOrgId,om1.parent_id agtOrgId from st_lms_organization_master om ,(select organization_id,name,parent_id,org_code from  st_lms_organization_master where org_code=? and organization_type='RETAILER' and organization_status <>'TERMINATE') om1 where om1.parent_id=om.organization_id and om.organization_status <> 'TERMINATE'");
			pstmt.setString(1, retName);
			logger.debug("-----Query1----::" + pstmt);
			resultSet = pstmt.executeQuery();
			int count=0;
			while (resultSet.next()) {
				count++;
				lmcName=resultSet.getString("parentorgCode")+"|"+resultSet.getInt("agtOrgId")+":"+resultSet.getString("orgCode")+"|"+resultSet.getInt("retOrgId");
			}
	
			if(count==0)
				lmcName="Does Not Exist:Does Not Exist";
			else if(count>1)
				lmcName="ERROR:ERROR";
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception ee) {
				logger.error("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return lmcName;

	}
	public Map<String, String> searchBanks() throws LMSException {
		Map<String, String> bankDetailsMap = new LinkedHashMap<String, String>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {			
				connection = DBConnect.getConnection();
				statement = connection.createStatement();
				String query = "select bank_dev_name, bank_disp_name from st_lms_bank_deposit_bank_details where status = 'ACTIVE' order by item_order";
				logger.debug("-----Query----::" + query);
				resultSet = statement.executeQuery(query);
				while (resultSet.next()) {
					bankDetailsMap.put(resultSet.getString("bank_dev_name"), resultSet.getString("bank_disp_name"));
				}
		} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
		} finally {
			DBConnect.closeConnection(connection, statement, resultSet);
		}
		return bankDetailsMap;
	}
	public void setOrgIdGroup(Map orgIdGroup) {
		this.orgIdGroup = orgIdGroup;
	}

	public void setUserIdGroup(Map userIdGroup) {
		this.userIdGroup = userIdGroup;
	}

}