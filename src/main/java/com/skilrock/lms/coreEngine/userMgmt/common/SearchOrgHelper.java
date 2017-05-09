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
package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

/**
 * 
 * This class is a helper used to process the Company search @ BO.
 * @author SkilRock Technologies
 * 
 */
public class SearchOrgHelper {

	public static void main(String[] args) {
		long days = 0, hours = 0;

		Calendar today = Calendar.getInstance();
		today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		Calendar extendedDate = Calendar.getInstance();
		// extendedDate.setTimeInMillis(date.getTime());
		extendedDate.set(2009, 3, 30, 0, 0, 0);

		long timeDiff = extendedDate.getTimeInMillis()
				- today.getTimeInMillis();

		days = timeDiff / (1000 * 60 * 60 * 24);
		hours = timeDiff / (1000 * 60 * 60);

		System.out.println(" dd days : " + days + "  hours = " + hours);
		System.out.println(",  extendedDate = " + extendedDate.getTime()
				+ "     ,today : " + today.getTime());

	}

	private int calculateExtendsCreditLimitUpto(java.sql.Date date) {
		if (date == null) {
			return 0;
		}
		long days = 0, hours = 0;

		Calendar today = Calendar.getInstance();
		today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		Calendar extendedDate = Calendar.getInstance();
		extendedDate.setTimeInMillis(date.getTime());
		extendedDate.set(extendedDate.get(Calendar.YEAR), extendedDate
				.get(Calendar.MONTH), extendedDate.get(Calendar.DAY_OF_MONTH),
				0, 0, 1);

		long timeDiff = extendedDate.getTimeInMillis()
				- today.getTimeInMillis();
		if (timeDiff > 0) {
			days = timeDiff / (1000 * 60 * 60 * 24);
			hours = timeDiff / (1000 * 60 * 60);
		}
		// System.out.println(" dd days : "+days +" hours = "+hours);
		// System.out.println(date +", extendedDate = "+extendedDate.getTime()
		// +" ,today : "+today.getTime());

		return (int) days;
	}

	private java.sql.Date getDate(String date) throws LMSException {
		try {
			DateFormat dateFormat = new SimpleDateFormat();

			Date parsedDate = dateFormat.parse(date);
			return new java.sql.Date(parsedDate.getTime());
		} catch (Exception e) {
			throw new LMSException();
		}

	}

	public int getRoleId(String key) throws LMSException {
		int roleId = 0;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query1 = QueryManager.getST5RoleQuery()
					+ " where role_name='" + key + "'";
			System.out.println(" query to get role Id" + query1);
			rs = statement.executeQuery(query1);
			rs.next();
			roleId = rs.getInt("role_id");

			return roleId;
		} catch (SQLException e) {
			throw new LMSException(e);
		}

		finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}
	}

	private String getWhereClause(Map searchMap,String parentorgQry,String orgQry) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();
		if (searchMap != null) {
			keySet = searchMap.keySet();
			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;
			int fieldAdded = 1;
			while (itr.hasNext()) {
				key = (String) itr.next();

				if (key.equals("PARENT_ORG_NAME")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause
									.append(" and a.parent_id in(select organization_id from st_lms_organization_master where ");
						}

						whereClause.append(parentorgQry);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%')");
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				} else if (key.equals("AGENT_ID")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {
						int orgId = Integer.parseInt(strValue);

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.parent_id");
						whereClause.append("=");
						whereClause.append(orgId);
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				}

				else if (key.equals("ORG_NAME")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						//whereClause.append("a.");
						whereClause.append(orgQry);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%'");
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				}

				else if (key.equals("ORG_STATUS")) {

					strValue = (String) searchMap.get(key);
					System.out.println(strValue);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.");
						whereClause.append(TableConstants.ORGANIZATION_STATUS);
						whereClause.append(" = ");
						whereClause.append("'");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						System.out.println("status Clause");
						fieldAdded++;

					}
				}

				else if (key.equals("ORG_TYPE")) {
					strValue = (String) searchMap.get(key);
					System.out.println(strValue);

					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.");
						whereClause.append(TableConstants.ORGANIZATION_TYPE);
						whereClause.append(" = ");
						whereClause.append("'");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						System.out.println("Org Type");
						fieldAdded++;

					}
				}

			}
			if (fieldAdded == 0) {
				whereClause.append("1=1");

			}

		}
		
		return whereClause.toString();
	}
private String getWhereClause(Map searchMap) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();
		if (searchMap != null) {
			keySet = searchMap.keySet();
			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;
			int fieldAdded = 1;
			while (itr.hasNext()) {
				key = (String) itr.next();

				if (key.equals("PARENT_ORG_NAME")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause
									.append(" and a.parent_id in(select organization_id from st_lms_organization_master where ");
						}

						whereClause.append(TableConstants.NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%')");
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				} else if (key.equals("AGENT_ID")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {
						int orgId = Integer.parseInt(strValue);

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.parent_id");
						whereClause.append("=");
						whereClause.append(orgId);
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				}

				else if (key.equals("ORG_NAME")) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.");
						whereClause.append(TableConstants.NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%'");
						System.out.println("Org name Clause");
						fieldAdded++;
					}
				}

				else if (key.equals("ORG_STATUS")) {

					strValue = (String) searchMap.get(key);
					System.out.println(strValue);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.");
						whereClause.append(TableConstants.ORGANIZATION_STATUS);
						whereClause.append(" = ");
						whereClause.append("'");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						System.out.println("status Clause");
						fieldAdded++;

					}
				}

				else if (key.equals("ORG_TYPE")) {
					strValue = (String) searchMap.get(key);
					System.out.println(strValue);

					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append("a.");
						whereClause.append(TableConstants.ORGANIZATION_TYPE);
						whereClause.append(" = ");
						whereClause.append("'");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						System.out.println("Org Type");
						fieldAdded++;

					}
				}

			}
			if (fieldAdded == 0) {
				whereClause.append("1=1");

			}

		}

		return whereClause.toString();
	}

	private String getWhereClause(String orgName, String orgType,
			String orgStatus, String parentCompName, String CrLimitSign,
			String extendCrLimitSign, String avlblCrLimitSign,
			String securityDepositSign, String CrLimit, String extendCrLimit,
			String avlblCrLimit, String securityDeposit, String pwtScrapStatus,String orgQry ,String parentorgQry) {
		StringBuilder whereClause = new StringBuilder();
	
		
		if (orgName != null & !orgName.equals("")) {
			whereClause.append("and " + orgQry+ " like '"
					+ orgName + "%' ");
		
			
		}
		if (orgType != null && !orgType.equals("-1")) {
			whereClause.append("and a." + TableConstants.ORGANIZATION_TYPE
					+ "='" + orgType + "' ");
		}
		if (orgStatus != null && !orgStatus.equals("-1")) {
			whereClause.append("and a." + TableConstants.ORGANIZATION_STATUS
					+ "='" + orgStatus + "' ");
		}
		if (parentCompName != null && !parentCompName.equals("")) {
			whereClause.append("and "+parentorgQry+"  like '"
					+ parentCompName + "%' ");

		}
		if (CrLimit != null && !CrLimit.trim().equals("")) {
			whereClause.append("and a.credit_limit " + CrLimitSign + " "
					+ CrLimit + " ");
		}
		if (extendCrLimit != null && !extendCrLimit.trim().equals("")) {
			whereClause.append("and a.extended_credit_limit "
					+ extendCrLimitSign + " " + extendCrLimit + " ");
		}
		if (avlblCrLimit != null && !avlblCrLimit.trim().equals("")) {
			whereClause.append("and a.available_credit " + avlblCrLimitSign
					+ " " + avlblCrLimit + " ");
		}
		if (securityDeposit != null && !securityDeposit.trim().equals("")) {
			whereClause.append("and a.security_deposit " + securityDepositSign
					+ " " + securityDeposit + " ");
		}
		if (pwtScrapStatus != null && !pwtScrapStatus.trim().equals("")
				& !pwtScrapStatus.equals("-1")) {
			whereClause.append("and a.pwt_scrap = '" + pwtScrapStatus + "'");
		}
		return whereClause.toString();
	}

	/**
	 * This method is used to search Company
	 * 
	 * @BO
	 * @param searchMap(agent
	 *            id,org id)
	 * @return List
	 * @throws LMSException
	 */

	public List searchOrg(String orgName, String orgType, String orgStatus,
			String parentCompName, String CrLimitSign, 
			String extendCrLimitSign, String avlblCrLimitSign,
			String securityDepositSign, String CrLimit, String extendCrLimit,
			String avlblCrLimit, String securityDeposit, String pwtScrapStatus, String reportType, String terminalStatusType)
			throws LMSException {

		Connection connection = null;
		Statement statement = null;
		PreparedStatement pstmt = null;
		ResultSet rsLedger = null;
		ResultSet resultSet = null;
		String colletedSdCol=null;
		String joinCheckAppendQry=null;
		String joinQry=null;
		

		try {
			

			String appendQury =QueryManager.getAppendOrgOrder();
			String orgCodeQry = " a.name orgCode,d.name parentorgCode ";
			String orgQry = " a.name ";
			String parentorgQry = " d.name ";
			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode,d.org_code parentorgCode ";
				orgQry = " a.org_code  ";
				parentorgQry =" d.org_code ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode,concat(d.org_code,'_',d.name)  parentorgCode ";
				orgQry = " concat(a.org_code,'_',a.name)   ";
				parentorgQry =" concat(d.org_code,'_',d.name) ";
				
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode,concat(d.name,'_',d.org_code)  parentorgCode ";
				orgQry = "  concat(a.name,'_',a.org_code)  ";
				parentorgQry ="  concat(d.name,'_',d.org_code  ";

			}
			OrganizationBean orgBean = null;
			List<OrganizationBean> searchResults = new ArrayList<OrganizationBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String dynamicWhereClause = getWhereClause(orgName, orgType,
					orgStatus, parentCompName, CrLimitSign, extendCrLimitSign,
					avlblCrLimitSign, securityDepositSign, CrLimit,
					extendCrLimit, avlblCrLimit, securityDeposit,
					pwtScrapStatus,orgQry,parentorgQry);
			// String query = QueryManager.getST5OrgSearchQuery() +
			// dynamicWhereClause;
		/*	String query = QueryManager.getST5OrgSearchQuery()
					+ dynamicWhereClause + "order by a.name";*/

			String appender = null;
			if("clXclReport".equals(reportType)) {
				if(!dynamicWhereClause.contains("TERMINATE")) {
					appender = "and a.organization_status !='TERMINATE'";
				}
				else{
					appender="";
				}
				
			} else if("searchAgtRetReport".equals(reportType)) {
				appender = "";
			}
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				joinCheckAppendQry="and e.organization_id=a.organization_id ";
				colletedSdCol="e.collected_security_deposit,";
				joinQry=",st_lms_organization_security_levy_master e";
				dynamicWhereClause=dynamicWhereClause.replace("a.security_deposit", "e.collected_security_deposit");
			}
			else{
				joinCheckAppendQry="";
				colletedSdCol="";
				joinQry="";
			}
			String query =" select a.organization_id, a.extends_credit_limit_upto, a.organization_type,a.parent_id, "+orgCodeQry+" ,a.organization_status,a.addr_line1,a.city,a.available_credit,a.credit_limit,a.security_deposit,"+colletedSdCol+"a.extended_credit_limit,a.pwt_scrap,a.claimable_bal,a.unclaimable_bal,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c "+joinQry+" where a.state_code=b.state_code and  a.country_code=c.country_code "+joinCheckAppendQry+"and a.parent_id = d.organization_id "+appender+" and a.organization_type in('AGENT','RETAILER') "+dynamicWhereClause+" order by "+appendQury;
			if("RETAILER".equals(orgType)) {
				if("ALL".equals(terminalStatusType)) {
					query = "SELECT organization_id, extends_credit_limit_upto, organization_type, parent_id, orgCode, parentorgCode, organization_status, addr_line1, city, available_credit, credit_limit, security_deposit, extended_credit_limit, pwt_scrap, claimable_bal, unclaimable_bal, state, country, concat(b.model_name,'-', status.serial_no) serial_no from ("
							+ query
							+ " )main LEFT JOIN st_lms_inv_status status ON main.organization_id = status.current_owner_id inner join st_lms_inv_model_master b on b.model_id = inv_model_id "
							+"UNION ALL SELECT organization_id, extends_credit_limit_upto, organization_type, parent_id, orgCode, parentorgCode, organization_status, addr_line1, city, available_credit, credit_limit, security_deposit, extended_credit_limit, pwt_scrap, claimable_bal, unclaimable_bal, state, country, status.serial_no from ("
						+ query
						+ " )main LEFT JOIN st_lms_inv_status status ON main.organization_id = status.current_owner_id WHERE status.serial_no IS NULL;";

				} else if ("DONT_HAVE".equals(terminalStatusType)) {
					query = "SELECT organization_id, extends_credit_limit_upto, organization_type, parent_id, orgCode, parentorgCode, organization_status, addr_line1, city, available_credit, credit_limit, security_deposit, extended_credit_limit, pwt_scrap, claimable_bal, unclaimable_bal, state, country, status.serial_no from ("
							+ query
							+ " )main LEFT JOIN st_lms_inv_status status ON main.organization_id = status.current_owner_id WHERE status.serial_no IS NULL;";
				} else if ("HAVE".equals(terminalStatusType)) {
					query = "SELECT organization_id, extends_credit_limit_upto, organization_type, parent_id, orgCode, parentorgCode, organization_status, addr_line1, city, available_credit, credit_limit, security_deposit, extended_credit_limit, pwt_scrap, claimable_bal, unclaimable_bal, state, country,  concat(b.model_name,'-', status.serial_no) serial_no from ("
							+ query
							+ " )main INNER JOIN st_lms_inv_status status ON main.organization_id = status.current_owner_id  inner join st_lms_inv_model_master b on b.model_id = inv_model_id;";
				}
			}

			System.out.println("-----Query For org Search----::" + query);

			resultSet = statement.executeQuery(query);

			System.out
					.println("Result Set for Organization Search" + resultSet);
			while (resultSet.next()) {
				// System.out.println("State "+resultSet.getString("state"));
				orgBean = new OrganizationBean();
				// userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				orgBean.setOrgId(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				// userBean.setUserName(resultSet.getString(TableConstants.USER_NAME));
				orgBean.setOrgName(resultSet.getString("orgCode"));
				orgBean.setParentOrgName(resultSet.getString("parentorgCode"));
				orgBean.setParentOrgId(resultSet.getInt("parent_id"));
				// userBean.setUserRoleName(TableConstants.ROLE_NAME);
				orgBean.setUserOrgType(orgType);
				orgBean.setOrgType(resultSet
						.getString(TableConstants.ORGANIZATION_TYPE));
				orgBean.setOrgStatus(resultSet
						.getString(TableConstants.ORGANIZATION_STATUS));
				orgBean.setOrgState(resultSet.getString("state"));
				orgBean.setOrgCity(resultSet.getString("city"));
				orgBean.setOrgCountry((String) resultSet.getString("country"));
				orgBean.setOrgAddr1((String) resultSet.getString("addr_line1"));
				orgBean.setAvailableCredit(resultSet
						.getDouble("available_credit"));
				orgBean.setClaimableBal(resultSet.getDouble("claimable_bal"));
				orgBean.setUnclaimableBal(resultSet
						.getDouble("unclaimable_bal"));
				// changed by arun
				int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(resultSet
						.getDate("extends_credit_limit_upto"));
				orgBean.setExtendsCreditLimitUpto(extendedCreditLimitUpto);
				orgBean.setOrgCreditLimit(resultSet.getDouble("credit_limit"));
				orgBean.setExtendedCredit(resultSet
						.getDouble("extended_credit_limit"));
				orgBean.setPwtScrapStatus(resultSet.getString("pwt_scrap"));
				orgBean.setSecurityDeposit("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))?resultSet
						.getDouble("collected_security_deposit"):resultSet.getDouble("security_deposit"));

				// get the last updated ledger balance of AGENT/RETAILER
				if ("AGENT".equalsIgnoreCase(orgBean.getOrgType())) {
					pstmt = connection
							.prepareStatement("select *  from st_lms_bo_current_balance where agent_org_id = ?");
				} else {
					pstmt = connection
							.prepareStatement("select *  from st_lms_agent_current_balance where account_type = ?");
				}
				pstmt.setInt(1, orgBean.getOrgId());
				rsLedger = pstmt.executeQuery();
				if (rsLedger.next()) {
					orgBean.setLedgerBalance(rsLedger
							.getDouble("current_balance"));
				}

				// commented by arun bcz ledger balance get from
				// 'current_balance' table directly
				// double ledgerBalance = orgBean.getAvailableCredit() -
				// orgBean.getOrgCreditLimit() - orgBean.getExtendedCredit();
				// System.out.println("ledger Balance of
				// =="+orgBean.getOrgName()+"== is =
				// "+orgBean.getLedgerBalance()+" calculated ledger =
				// "+ledgerBalance);
				// orgBean.setOrgCreditLimit(resultSet.getDouble("credit_limit")
				// + resultSet.getDouble("extended_credit_limit"));
				
				if ("RETAILER".equals(orgType)) {
					if (resultSet.getString("serial_no") != null)
						orgBean.setSerialNo(resultSet.getString("serial_no"));
					else
						orgBean.setSerialNo("NA");
				} else
					orgBean.setSerialNo("NA");

				searchResults.add(orgBean);
			}

			return searchResults;

		} catch (SQLException e) {
			throw new LMSException(e);
		}

		finally {

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
				throw new LMSException(se);
			}
		}

	}

	/**
	 * This method is used to search Company
	 * 
	 * @AGENT
	 * @param searchMap
	 * @return List
	 * @throws LMSException
	 */

	public List searchOrgForRetailer(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;

		try {

			OrganizationBean orgBean = null;
			List<OrganizationBean> searchResults = new ArrayList<OrganizationBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String dynamicWhereClause = getWhereClause(searchMap);
			String query = QueryManager.getST5OrgSearchQuery()
					+ dynamicWhereClause + "order by a.name";

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				// System.out.println("Helper Result Set");
				orgBean = new OrganizationBean();
				// userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				orgBean.setOrgId(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				// userBean.setUserName(resultSet.getString(TableConstants.USER_NAME));
				orgBean.setOrgName(resultSet.getString(TableConstants.NAME));
				// userBean.setUserRoleName(TableConstants.ROLE_NAME);
				orgBean.setOrgType(resultSet
						.getString(TableConstants.ORGANIZATION_TYPE));
				orgBean.setOrgStatus(resultSet
						.getString(TableConstants.ORGANIZATION_STATUS));
				orgBean.setOrgState(resultSet.getString("state"));
				orgBean.setOrgCity(resultSet.getString("city"));
				orgBean.setOrgCountry(resultSet.getString("country"));
				orgBean.setOrgAddr1(resultSet.getString("addr_line1"));

				searchResults.add(orgBean);
			}

			return searchResults;

		} catch (SQLException e) {
			throw new LMSException(e);
		}

		finally {

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
				throw new LMSException(se);
			}

		}

	}

	public List<OrganizationBean> searchOrgRetailer(
			Map<String, String> searchMap, int orgId) throws LMSException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			OrganizationBean orgBean = null;
			List<OrganizationBean> searchResults = new ArrayList<OrganizationBean>();

			
	/*		String query = QueryManager.getST5_RET_ORG_SEARCH_FOR_AGENT()
					+ dynamicWhereClause + " order by a.name";*/
			String appendQury =QueryManager.getAppendOrgOrder();
			String orgCodeQry = " a.name orgCode,d.name parentorgCode ";
			String parentOrgQry = " name ";
			String orgQry = " a.name ";

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode,d.org_code parentorgCode ";
				parentOrgQry = "  org_code  ";
				orgQry = "  a.org_code  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode,concat(d.org_code,'_',d.name)  parentorgCode ";
				parentOrgQry = " concat(org_code,'_',name)   ";
				orgQry = " concat(a.org_code,'_',a.name)   ";
				
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode,concat(d.name,'_',d.org_code)  parentorgCode ";
				parentOrgQry = "  concat(name,'_',org_code)  ";
				orgQry = " concat(a.name,'_',a.org_code)   ";
				

			}
			String dynamicWhereClause = getWhereClause(searchMap,parentOrgQry,orgQry);
			String query =" select a.organization_id, a.extends_credit_limit_upto ,a.organization_type,"+orgCodeQry+" ,a.organization_status,a.addr_line1,a.city,a.available_credit,a.credit_limit,a.security_deposit,a.extended_credit_limit,a.pwt_scrap,a.claimable_bal,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and  a.country_code=c.country_code and a.parent_id=d.organization_id and a.organization_type='RETAILER' and a.parent_id=? "+dynamicWhereClause+" order by "+appendQury;
			System.out.println("-----Query----::" + query);

			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, orgId);
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				// System.out.println("Helper Result Set");
				orgBean = new OrganizationBean();
				// userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				orgBean.setOrgId(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				orgBean.setParentOrgName(resultSet.getString("parentorgCode"));
				orgBean.setParentOrgId(orgId);
				// userBean.setUserName(resultSet.getString(TableConstants.USER_NAME));
				orgBean.setOrgName(resultSet.getString("orgCode"));
				// userBean.setUserRoleName(TableConstants.ROLE_NAME);
				orgBean.setOrgType(resultSet
						.getString(TableConstants.ORGANIZATION_TYPE));
				orgBean.setOrgStatus(resultSet
						.getString(TableConstants.ORGANIZATION_STATUS));
				orgBean.setOrgState(resultSet.getString("state"));
				orgBean.setOrgCity(resultSet.getString("city"));
				orgBean.setOrgCountry(resultSet.getString("country"));
				orgBean.setOrgAddr1(resultSet.getString("addr_line1"));
				orgBean.setAvailableCredit(resultSet
						.getDouble("available_credit"));
				// changed by arun
				int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(resultSet
						.getDate("extends_credit_limit_upto"));
				orgBean.setExtendsCreditLimitUpto(extendedCreditLimitUpto);
				orgBean.setOrgCreditLimit(resultSet.getDouble("credit_limit"));
				orgBean.setExtendedCredit(resultSet
						.getDouble("extended_credit_limit"));
				orgBean.setClaimableBal(resultSet.getDouble("claimable_bal"));// added
				// by
				// amit
				// on
				// 21/09/10

				// get the last updated ledger balance of AGENT/RETAILER
				pstmt = connection
						.prepareStatement("select *  from st_lms_agent_current_balance where account_type = ?");
				pstmt.setInt(1, orgBean.getOrgId());
				ResultSet rsLedger = pstmt.executeQuery();
				if (rsLedger.next()) {
					orgBean.setLedgerBalance(rsLedger
							.getDouble("current_balance"));
				}

				double ledgerBalance = orgBean.getAvailableCredit()
						- orgBean.getOrgCreditLimit()
						- orgBean.getExtendedCredit();
				System.out.println("ledger Balance of =="
						+ orgBean.getOrgName() + "==  is = "
						+ orgBean.getLedgerBalance()
						+ "     calculated ledger = " + ledgerBalance);

				// orgBean.setOrgCreditLimit(resultSet.getDouble("credit_limit")
				// + resultSet.getDouble("extended_credit_limit"));
				orgBean.setSecurityDeposit(resultSet
						.getDouble("security_deposit"));
				searchResults.add(orgBean);
			}

			return searchResults;

		} catch (SQLException e) {
			throw new LMSException(e);
		}

		finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null & !connection.isClosed()) {
					connection.close();
				}

			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}
}
