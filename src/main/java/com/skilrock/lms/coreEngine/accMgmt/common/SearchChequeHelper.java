/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

/**
 * This is a helper class having methods to complete Cheque bounce process.
 * 
 * @author Skilrock Technologies
 * 
 */
public class SearchChequeHelper {
	static Log logger = LogFactory.getLog(SearchChequeHelper.class);

	private String getWhereClause(Map searchMap) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();

		if (searchMap != null) {
			keySet = searchMap.keySet();

			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;
			whereClause.append(" and ");
			key = (String) itr.next();
			logger.debug("Key " + key);
			strValue = (String) searchMap.get(key);
			logger.debug("Cheque Number" + strValue);

			whereClause.append(TableConstants.CHEQUE_NUMBER);
			whereClause.append(" = ");
			whereClause.append(strValue);

		}
		return whereClause.toString();
	}

	private String getWhereClauseForRetailer(Map searchMap) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();

		if (searchMap != null) {
			keySet = searchMap.keySet();

			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;

			int fieldAdded = 0;

			whereClause.append(" and ");

			key = (String) itr.next();

			strValue = (String) searchMap.get(key);
			logger.debug("Cheque Number" + strValue);
			if (fieldAdded > 0) {
				whereClause.append(" and ");
			}
			whereClause.append("cheque_nbr");
			whereClause.append(" = ");
			whereClause.append(strValue);
			fieldAdded++;

			if (fieldAdded == 0) {
				whereClause.append("1=1");
			}

		}
		return whereClause.toString();
	}

	/**
	 * This method is used to search cheque which are submitted by agent.
	 * 
	 * @param searchMap
	 * @return List of Cheques
	 * @throws LMSException
	 */
	public List<ChequeBean> searchCheque(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		logger.debug("-----Query----::");
		try {

			ChequeBean chequeBean = null;
			List<ChequeBean> searchResults = new ArrayList<ChequeBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			
			String dynamicWhereClause = getWhereClause(searchMap);
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
			String query = "select a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,"+orgCodeQry+",b.organization_id  from st_lms_bo_sale_chq a, st_lms_organization_master b where a.agent_org_id=b.organization_id and a.transaction_type='CHEQUE' "
					+ dynamicWhereClause;

			logger.debug("-----Query yogesh************----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				chequeBean = new ChequeBean();
				logger.debug("-----Query----::");

				chequeBean.setChequeNumber(resultSet
						.getString(TableConstants.CHEQUE_NUMBER));
				logger.debug("Cheque Date"
						+ resultSet.getDate(TableConstants.CHEQUE_DATE));
				String sd = resultSet.getDate(TableConstants.CHEQUE_DATE)
						.toString();
				Calendar cal = Calendar.getInstance();

				java.sql.Date sD = new java.sql.Date(cal.getTimeInMillis());

				chequeBean.setChequeDate(sd);
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

				logger.debug("Cheque Number"
						+ resultSet.getString(TableConstants.CHEQUE_NUMBER));
				logger.debug("Org Name:"
						+ resultSet.getString("orgCode"));
				logger.debug("Bank"
						+ resultSet.getString(TableConstants.DRAWEE_BANK));

			}
			return searchResults;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
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
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				throw new LMSException(se);
			}
		}

	}

	/**
	 * This method is used to search cheque which submitted by the retailer
	 * 
	 * @param searchMap
	 * @return
	 * @throws LMSException
	 */
	public List searchChequeRetailer(Map searchMap, int agent_id,
			double chequeBounceCharge) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		logger.debug("-----Query----::");
		try {

			ChequeBean chequeBean = null;
			List<ChequeBean> searchResults = new ArrayList<ChequeBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String dynamicWhereClause = getWhereClauseForRetailer(searchMap);
			String query = QueryManager.getST5ChequeSearchRetailerQuery()
					+ "and agent_user_id=" + agent_id + "" + dynamicWhereClause;

			logger.debug("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				chequeBean = new ChequeBean();
				logger.debug("-----Query----::");

				chequeBean.setChequeNumber(resultSet.getString("cheque_nbr"));

				chequeBean.setChequeDate(resultSet.getDate(
						TableConstants.CHEQUE_DATE).toString());
				chequeBean.setIssuePartyname(resultSet
						.getString(TableConstants.ISSUE_PARTY_NAME));
				chequeBean.setBankName(resultSet
						.getString(TableConstants.DRAWEE_BANK));
				chequeBean.setChequeAmount(resultSet
						.getDouble(TableConstants.CHEQUE_AMT));
				chequeBean.setOrgName(resultSet.getString(TableConstants.NAME));
				chequeBean.setTransactionId(resultSet
						.getLong(TableConstants.TRANSACTION_ID));
				// chequeBean.setChequeBounceCharges(chequeBounceCharge);

				searchResults.add(chequeBean);

				logger.debug("Cheque Number"
						+ resultSet.getString("cheque_nbr"));
				logger.debug("Org Name:"
						+ resultSet.getString(TableConstants.NAME));
				logger.debug("Bank"
						+ resultSet.getString(TableConstants.DRAWEE_BANK));

			}
			return searchResults;
		}

		catch (SQLException e) {
			logger.error("Exception: " + e);
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
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				throw new LMSException(se);
			}
		}

	}

}
