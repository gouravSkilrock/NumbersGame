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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.UserBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

/**
 * 
 * This class is a helper used to process the user search.
 * 
 * @author SkilRock Technologies
 * 
 */
public class SearchUserHelper {
	/**
	 * 
	 * This method is used to get Role id for Role name.
	 * 
	 * @param role
	 *            name
	 * @return role id throw LMSException
	 */

	public int getRoleId(String key) {
		int roleId = -1;
		 
		Connection connection = DBConnect.getConnection();
		try {
			Statement statement = connection.createStatement();
			String query1 = QueryManager.getST5RoleQuery()
					+ " where role_name='" + key + "'";
			System.out.println(" query to get role Id" + query1);
			ResultSet rs = statement.executeQuery(query1);

			while (rs.next()) {
				roleId = rs.getInt("role_id");
				// System.out.println("roleid"+roleId);
			}
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
		return roleId;
	}

	/**
	 * This method is used to search user.
	 * 
	 * @param searchMap(user
	 *            name,user role,status)
	 * @return List
	 * @throws LMSException
	 */

	public List<String> getRoleMasterName(int tierId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<String> roleName = new ArrayList<String>();
		try {
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = "select role_name from st_lms_role_master where is_master='Y' and tier_id in (select tm1.tier_id from st_lms_tier_master tm1 inner join st_lms_tier_master tm2 on tm1.parent_tier_id = tm2.tier_id where tm2.tier_id="
					+ tierId
					+ " or (tm1.parent_tier_id in(select tm2.tier_id from st_lms_tier_master tm1 inner join st_lms_tier_master tm2 on tm1.parent_tier_id = tm2.tier_id where tm2.parent_tier_id>="
					+ tierId + ")))";
			System.out.println("-----Query iss ----::" + query);
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				roleName.add(resultSet.getString("role_name"));
			}

			return roleName;
		} catch (SQLException e) {

			throw new LMSException(e);
			// e.printStackTrace();
		}

		finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	private String getWhereClause(Map searchMap,String orgQry) throws SQLException {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();
		// whereClause.append(" where a.role_id=b.role_id and ");
		if (searchMap != null) {
			keySet = searchMap.keySet();
			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;
			int fieldAdded = 1;

			while (itr.hasNext()) {
				key = (String) itr.next();
				if (key.equals(GameContants.PARENT_COMP_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause
									.append(" and c.parent_id in(select organization_id from st_lms_organization_master where  ");
						}
						whereClause.append(orgQry);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%')");
						System.out.println("User name Clause");
						fieldAdded++;
					}
				} else if (key.equals(GameContants.COMP_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause
									.append(" and a.organization_id in(select organization_id from st_lms_organization_master where ");
						}
						whereClause.append(orgQry);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%')");
						System.out.println("User name Clause");
						fieldAdded++;
					}
				} else if (key.equals(GameContants.USER_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}
						whereClause.append(TableConstants.USER_NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%'");
						System.out.println("User name Clause");
						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.USER_STATUS)) {

					strValue = (String) searchMap.get(key);
					System.out.println(strValue);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" and ");
						}

						whereClause.append(TableConstants.STATUS);
						whereClause.append(" = ");
						whereClause.append("'");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						System.out.println("status Clause");
						fieldAdded++;

					}
				}

				else if (key.equals(GameContants.USER_ROLE)) {
					strValue = (String) searchMap.get(key);
					System.out.println(strValue);

					strValue = (String) searchMap.get(key);
					int strRoleId = this.getRoleId(strValue);
					if (strValue != null && !strValue.equals("")
							&& !(strRoleId == -1)) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append("a.role_id");
						whereClause.append(" = ");
						whereClause.append(strRoleId);
						System.out.println("role Clause");
						fieldAdded++;

					}

				}

			}
			if (fieldAdded == 1) {
				whereClause.append(" and 1=1");

			}

		}

		return whereClause.toString();
	}

	public List searchOfflineUser(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			UserBean userBean = null;
			List<UserBean> searchResults = new ArrayList<UserBean>();
			String orgCodeQry = " c.name orgCode,d.name parentorgCode ";
			String orgQry = " name";
		
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " c.org_code orgCode,d.org_code parentorgCode ";
				 orgQry = " org_code ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat( c.org_code,'_',c.name)  orgCode,concat( d.org_code,'_',d.name)  parentorgCode ";
				 orgQry = " concat( org_code,'_',name) ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(c.name,'_',c.org_code)  orgCode,concat(d.name,'_',d.org_code)  parentorgCode ";
				 orgQry = " concat( name,'_',org_code) ";

			}			
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String dynamicWhereClause = getWhereClause(searchMap,orgQry);
			StringBuilder addUserTypeQry = new StringBuilder("");
			if ("ONLINE".equals(searchMap.get("user_type"))) {
				addUserTypeQry.append(" and b.is_offline='NO' ");
			} else if ("OFFLINE".equals(searchMap.get("user_type"))) {
				addUserTypeQry.append(" and b.is_offline='YES' ");
			}

			if (searchMap.get("offline_status") != null) {
				addUserTypeQry.append(" and b.offline_status='"
						+ searchMap.get("offline_status") + "' ");
			}

			String query = "select a.user_id,a.user_name,"+orgCodeQry+",b.offline_status,"
					+ "b.login_status,b.is_offline from st_lms_user_master a,st_lms_ret_offline_master b,"
					+ "st_lms_organization_master c,st_lms_organization_master d where a.user_id=b.user_id"
					+ " and c.organization_id=a.organization_id and c.organization_id=b.organization_id and"
					+ " d.organization_id=c.parent_id"
					+ dynamicWhereClause
					+ addUserTypeQry + " order by user_name";

			System.out.println("-----Query iss ----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				userBean = new UserBean();
				userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				userBean.setUserName(resultSet
						.getString(TableConstants.USER_NAME));
				userBean.setUserOrgName(resultSet
						.getString("orgCode"));
				userBean.setParentOrgName(resultSet.getString("parentorgCode"));
				userBean
						.setOfflineStatus(resultSet.getString("offline_status"));
				userBean.setLoginStatus(resultSet.getString("login_status"));
				userBean.setIsOffline(resultSet.getString("is_offline"));
				searchResults.add(userBean);
			}

			return searchResults;

		} catch (SQLException e) {

			throw new LMSException(e);
			// e.printStackTrace();
		}

		finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public List searchUser(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			UserBean userBean = null;
			List<UserBean> searchResults = new ArrayList<UserBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String orgCodeQry = " c.name orgCode,d.name parentorgCode ";
			String orgQry = " name ";
			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " c.org_code orgCode,d.org_code parentorgCode ";
				orgQry = " org_code  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat( c.org_code,'_',c.name)  orgCode,concat( d.org_code,'_',d.name)  parentorgCode ";
				orgQry = " concat(org_code,'_',name)   ";
				

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(c.name,'_',c.org_code)  orgCode,concat(d.name,'_',d.org_code)  parentorgCode ";
				orgQry = "  concat(name,'_',org_code)  ";
				

			}
			String dynamicWhereClause = getWhereClause(searchMap,orgQry);
			String query = "select "+orgCodeQry+" ,a.user_id,b.role_name,a.user_name,a.status,a.registration_date from st_lms_user_master a,st_lms_role_master b,st_lms_organization_master c,st_lms_organization_master d where b.tier_id in (select tier_id from st_lms_tier_master where (tier_code = 'AGENT' or tier_code = 'RETAILER')) and a.role_id=b.role_id and c.organization_id=a.organization_id and d.organization_id=c.parent_id"
					+ dynamicWhereClause + " order by user_name";

			System.out.println("-----Query iss ----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				// System.out.println("Helper Result Set");
				userBean = new UserBean();
				userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				// System.out.println("User
				// Name"+resultSet.getString(TableConstants.USER_NAME));
				userBean.setUserName(resultSet
						.getString(TableConstants.USER_NAME));
				// userBean.setUserRoleId(resultSet.getInt(TableConstants.ROLE_ID));
				userBean.setUserRoleName(resultSet
						.getString(TableConstants.ROLE_NAME));
				userBean.setUserStatus(resultSet
						.getString(TableConstants.STATUS));
				userBean.setRegisterDate(resultSet
						.getDate(TableConstants.Register_DATE));
				userBean.setUserOrgName(resultSet
						.getString("orgCode"));
				userBean.setParentOrgName(resultSet
						.getString("parentorgCode"));
				searchResults.add(userBean);

				// System.out.println("UserId:" +
				// resultSet.getInt(TableConstants.USER_ID));
				// System.out.println("UserName:" +
				// resultSet.getString(TableConstants.USER_NAME));
				// System.out.println("Role:" +
				// resultSet.getInt(TableConstants.ROLE_ID));
				// System.out.println("Status:" +
				// resultSet.getString(TableConstants.STATUS));
				// System.out.println("Registration Date:" +
				// resultSet.getDate(TableConstants.Register_DATE));
			}

			return searchResults;

		} catch (SQLException e) {

			throw new LMSException(e);
			// e.printStackTrace();
		}

		finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * This method is used to search User Details.
	 * 
	 * @author SkilRock Technologies
	 * @param userId
	 * @return List throw LMSException
	 */
	public List searchUserDetail(int userId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			UserInfoBean userBean = null;
			List<UserInfoBean> searchResults = new ArrayList<UserInfoBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST5UserDetailQuery() + userId;

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				// System.out.println("Helper Result Set");

				userBean = new UserInfoBean();
				userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				userBean.setUserName(resultSet
						.getString(TableConstants.USER_NAME));
				userBean.setRoleName(resultSet
						.getString(TableConstants.ROLE_NAME));
				userBean.setStatus(resultSet.getString(TableConstants.STATUS));
				userBean.setRegDate(resultSet
						.getDate(TableConstants.Register_DATE));
				userBean.setOrgName(resultSet.getString("name"));
				userBean.setUserType(resultSet.getString("organization_type"));

				searchResults.add(userBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}

	}

	public List searchUserRetailer(Map searchMap, int agtOrgId)
			throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			UserBean userBean = null;
			List<UserBean> searchResults = new ArrayList<UserBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String orgCodeQry = " d.name orgCode ";
			String orgQry = " name";
			String appendQry =QueryManager.getAppendOrgOrder();
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " d.org_code orgCode ";
				 orgQry = " org_code ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat( d.org_code,'_',d.name)  orgCode ";
				 orgQry = " concat( org_code,'_',name) ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(d.name,'_',d.org_code) orgCode ";
				 orgQry = " concat( name,'_',org_code) ";

			}
			 
			String dynamicWhereClause = getWhereClause(searchMap,orgQry);
			// String query = QueryManager.getST5UserSearchQuery() +
			// dynamicWhereClause;
			// String query="select
			// a.user_id,b.role_name,a.user_name,a.status,a.registration_date
			// from st_lms_user_master a,st_lms_role_master b where b.role_name
			// in ('RET_MAS') and organization_id in(select organization_id from
			// st_lms_organization_master where parent_id="+agtOrgId+")" +
			// dynamicWhereClause ;
			// this query is edited by yogesh to get organization name
			String query = "select "+orgCodeQry+", a.user_id,b.role_name,a.user_name,a.status,a.registration_date from st_lms_user_master a,st_lms_role_master b,st_lms_organization_master d where b.role_id in (select role_id from st_lms_role_master where tier_id = (select tier_id from st_lms_tier_master where tier_code = 'RETAILER') and is_master = 'Y') and a.organization_id in(select organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") and a.organization_id=d.organization_id "
					+ dynamicWhereClause + " order by "+appendQry;
			System.out
					.println("-----Query iss for retailer user search   ----::"
							+ query);
			// select
			// a.user_id,b.role_name,a.user_name,a.status,a.registration_date
			// from st_lms_user_master a,st_lms_role_master b where b.role_name
			// in ('AGT_MAS','RET_MAS') and a.role_id=b.role_id
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				// System.out.println("Helper Result Set");
				userBean = new UserBean();
				userBean.setUserId(resultSet.getInt(TableConstants.USER_ID));
				System.out.println("User Name"
						+ resultSet.getString(TableConstants.USER_NAME));
				userBean.setUserName(resultSet
						.getString(TableConstants.USER_NAME));
				// userBean.setUserRoleId(resultSet.getInt(TableConstants.ROLE_ID));
				userBean.setUserRoleName(resultSet
						.getString(TableConstants.ROLE_NAME));
				userBean.setUserStatus(resultSet
						.getString(TableConstants.STATUS));
				userBean.setRegisterDate(resultSet
						.getDate(TableConstants.Register_DATE));
				userBean.setUserOrgName(resultSet
						.getString("orgCode"));
				searchResults.add(userBean);

				// System.out.println("UserId:" +
				// resultSet.getInt(TableConstants.USER_ID));
				// System.out.println("UserName:" +
				// resultSet.getString(TableConstants.USER_NAME));
				// System.out.println("Role:" +
				// resultSet.getInt(TableConstants.ROLE_ID));
				// System.out.println("Status:" +
				// resultSet.getString(TableConstants.STATUS));
				// System.out.println("Registration Date:" +
				// resultSet.getDate(TableConstants.Register_DATE));
			}

			return searchResults;

		} catch (SQLException e) {

			throw new LMSException(e);
			// e.printStackTrace();
		}

		finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

}
