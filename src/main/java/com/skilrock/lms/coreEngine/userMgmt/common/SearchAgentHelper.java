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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
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
public class SearchAgentHelper {

	/**
	 * 
	 * @param searchMap
	 * @return
	 */
	public Map<String, OrganizationBean> fetchAgtBalDistributionHelper(
			UserInfoBean userBean) {
		Connection con = null;
		Map<String, OrganizationBean> agtBalDistMap = new TreeMap<String, OrganizationBean>();

		try {
			OrganizationBean orgBean = null;

			String appendOrder = QueryManager.getAppendOrgOrder();
			String orgCodeQry = "  name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "  org_code orgCode  ";


			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			String query = "select organization_id,"+orgCodeQry+",(available_credit-claimable_bal) as available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit from st_lms_organization_master where parent_id=? and organization_status != 'TERMINATE' order by "+appendOrder;
			 
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con.prepareStatement(query);
			System.out.println("Agt Bal Dist Query: " + pstmt);
			pstmt.setInt(1, userBean.getUserOrgId());
			System.out.println("Agt Bal Dist Query: " + pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				orgBean = new OrganizationBean();
				orgBean.setOrgId(rs.getInt("organization_id"));
				orgBean.setOrgName(rs.getString("orgCode"));
				orgBean.setAvailableCredit(rs.getDouble("available_credit"));
				agtBalDistMap.put(rs.getString("orgCode"), orgBean);
			}

			rs.close();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return agtBalDistMap;
	}

	private String getWhereClause(Map searchMap,String orgQry ) {
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

				/*
				 * if(key.equals("AGENT_ID")){ strValue = (String)
				 * searchMap.get(key);
				 * 
				 * if(strValue!= null && !strValue.equals("")){ int
				 * orgId=Integer.parseInt(strValue);
				 * 
				 * if(fieldAdded > 0){ whereClause.append(" and "); }
				 * whereClause.append("a.parent_id"); whereClause.append("=");
				 * whereClause.append(orgId); System.out.println("Org name
				 * Clause"); fieldAdded++; } }
				 * 
				 */

				if (key.equals("ORG_NAME")) {
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

				/*
				 * if(key.equals("AGENT_ID")){ strValue = (String)
				 * searchMap.get(key);
				 * 
				 * if(strValue!= null && !strValue.equals("")){ int
				 * orgId=Integer.parseInt(strValue);
				 * 
				 * if(fieldAdded > 0){ whereClause.append(" and "); }
				 * whereClause.append("a.parent_id"); whereClause.append("=");
				 * whereClause.append(orgId); System.out.println("Org name
				 * Clause"); fieldAdded++; } }
				 * 
				 */

				if (key.equals("ORG_NAME")) {
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

	/**
	 * This method is used to search Company
	 * 
	 * @BO
	 * @param searchMap(agent
	 *            id,org id)
	 * @return List
	 * @throws LMSException
	 */

	public List searchOrg(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			OrganizationBean orgBean = null;
			List<OrganizationBean> searchResults = new ArrayList<OrganizationBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String dynamicWhereClause = getWhereClause(searchMap);
			// String query = QueryManager.getST5OrgSearchQuery() +
			// dynamicWhereClause;
			String query = QueryManager.getST3AgentSearchQuery()
					+ dynamicWhereClause + " order by a.name";

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
				orgBean.setParentOrgName(resultSet.getString("parent_name"));
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
				orgBean.setPwtScrapStatus(resultSet.getString("pwt_scrap"));

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

	public List searchOrgForRetailer(Map searchMap, int agtOrgId)
			throws LMSException {

		Connection connection = null;
		PreparedStatement pstatement = null;
		ResultSet resultSet = null;

		try {

			OrganizationBean orgBean = null;
			List<OrganizationBean> searchResults = new ArrayList<OrganizationBean>();

			 
			connection = DBConnect.getConnection();
		
			String orgCodeQry = " a.name orgCode,d.name parentorgCode ";
			String orgQry = " a.name ";
	
			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode,d.org_code parentorgCode ";
				orgQry = " a.org_code  ";
		

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode,concat(d.org_code,'_',d.name)  parentorgCode ";
				orgQry = " concat(a.org_code,'_',a.name)   ";
			
				
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode,concat(d.name,'_',d.org_code)  parentorgCode ";
				orgQry = "  concat(a.name,'_',a.org_code)  ";
			

			}
			String dynamicWhereClause = getWhereClause(searchMap,orgQry);
			String query = "select a.organization_id, a.extends_credit_limit_upto ,a.organization_type,"+orgCodeQry+",a.organization_status,a.addr_line1,a.city,a.available_credit,a.credit_limit,a.security_deposit,a.extended_credit_limit,a.pwt_scrap,a.claimable_bal,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and  a.country_code=c.country_code and a.parent_id=d.organization_id and a.organization_status!='TERMINATE' and   a.organization_type='RETAILER' and a.parent_id=? "
					+ dynamicWhereClause + " order by "+QueryManager.getAppendOrgOrder();

			System.out.println("-----Query----::" + query);
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1, agtOrgId);
			System.out.println("qqqqqqqq :: " + pstatement);
			resultSet = pstatement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Helper Result Set");
				orgBean = new OrganizationBean();
				// userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				orgBean.setOrgId(resultSet
						.getInt(TableConstants.ORGANIZATION_ID));
				// userBean.setUserName(resultSet.getString(TableConstants.USER_NAME));
				orgBean.setOrgName(resultSet.getString("orgCode"));
				//orgBean.setParentOrgName(resultSet.getString("parent_name"));
				orgBean.setParentOrgName(resultSet.getString("parentorgCode"));
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
				if (pstatement != null) {
					pstatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

}
/*
 * private java.sql.Date getDate (String date)throws LMSException { try{
 * DateFormat dateFormat = new SimpleDateFormat();
 * 
 * 
 * Date parsedDate = dateFormat.parse(date); return new
 * java.sql.Date(parsedDate.getTime()); } catch(Exception e){ throw new
 * LMSException();} }
 * 
 * public int getRoleId(String key)throws LMSException {int roleId=0; Connection
 * connection=null; Statement statement=null; ResultSet rs=null; try{ DBConnect
 *   connection = DBConnect.getConnection();
 * statement = connection.createStatement(); String
 * query1=QueryManager.getST5RoleQuery()+" where role_name='"+key+"'";
 * System.out.println(" query to get role Id"+query1);
 * rs=statement.executeQuery(query1); rs.next(); roleId=rs.getInt("role_id");
 * 
 * return roleId; } catch(SQLException e){ throw new LMSException(e); }
 * 
 * finally {
 * 
 * try { if (rs != null) { rs.close(); } if (statement != null) {
 * statement.close(); } if (connection != null) { connection.close(); } } catch
 * (SQLException se) { throw new LMSException(se); } } } }
 */
