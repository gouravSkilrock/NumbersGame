package com.skilrock.lms.ajax;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;


public class AjaxRequestHelper {
	static Log logger = LogFactory.getLog(AjaxRequestHelper.class);

	public static int getOrgUserId(String orgName) {
		int orgUser = 0;
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select organization_id from st_lms_organization_master where name = '"
							+ orgName + "'");
			while (rs.next()) {
				orgUser = rs.getInt("organization_id");
			}
			System.out.println("orgList-------" + orgUser);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgUser;
	}

	public String fetchChildOrgNGamenFmtSale(int agtOrgId, String ownerType,String shiftGameStatus)
			throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuilder nbrFormat = new StringBuilder();
		Statement stmt1 = null;
		PreparedStatement statement = null;
		Connection con = DBConnect.getConnection();

		ResultSet rs = null;
		ResultSet rs2 = null;
		String appender="";
		if(shiftGameStatus==null)
			appender=" and a.game_status='OPEN' ";
		try {
			stmt1 = con.createStatement();
			String detQuery = "select a.game_id,a.game_name,a.game_nbr,b.pack_nbr_digits,b.book_nbr_digits,b.ticket_nbr_digits,b.game_nbr_digits from st_se_game_master a ,st_se_game_ticket_nbr_format b where a.game_id in (select  distinct game_id from st_se_game_inv_status where current_owner = '"
					+ ownerType.trim()
					+ "' and current_owner_id = "
					+ agtOrgId
					+ ") "+appender+" and a.game_id=b.game_id";
			logger.debug(detQuery);
			rs = stmt1.executeQuery(detQuery);
			while (rs.next()) {
				nbrFormat.append(rs.getString("game_name") + ":");
				nbrFormat.append(rs.getString("game_nbr") + ":");
				nbrFormat.append(rs.getInt("pack_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("book_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("ticket_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("game_nbr_digits") + "Nx*");

			}
			String query = null;
			if ("BO".equalsIgnoreCase(ownerType.trim())) {
				if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
					query = "select so.organization_id as orgId, so.org_code as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
				} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
					query = "select so.organization_id as orgId, concat(so.org_code,'_',so.name) as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
				}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
					query = "select so.organization_id as orgId, concat(so.name,'_',so.org_code) as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
				}else {
					query = QueryManager.getST1_AGENT_ORG_ACTIVE();
				}
			} else {
				// query = QueryManager.getST1RetOrgActiveQuery();// +" order by
				// name";
				if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
					query = "select so.organization_id as orgId,sso.org_code as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su where so.organization_type = 'RETAILER' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y'   and so.parent_id = ?  order by name";
				} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
					query = "select so.organization_id as orgId,concat(so.org_code,'_',so.name) as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su where so.organization_type = 'RETAILER' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y'   and so.parent_id = ?  order by name";
				}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
					query = "select so.organization_id as orgId,concat(so.name,'_',so.org_code) as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su where so.organization_type = 'RETAILER' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y'   and so.parent_id = ?  order by name";
				}else {
					query = "select so.organization_id as orgId,so.name as name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su where so.organization_type = 'RETAILER' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y'   and so.parent_id = ?  order by name";
				}
			}
			statement = con.prepareStatement(query);
			statement.setInt(1, agtOrgId);
			rs2 = statement.executeQuery();
			while (rs2.next()) {
				nbrFormat.append(rs2.getString("name") +"|"+ rs2.getString("orgId") + ":");
				nbrFormat.append(FormatNumber.formatNumberForJSP(rs2
						.getString("available_credit"))
						+ ":");
				nbrFormat.append(FormatNumber.formatNumberForJSP(rs2
						.getString("claimable_bal"))
						+ "Rt*");
			}
			stmt1.close();
			con.close();
			logger.debug(nbrFormat.toString()
					+ "*****PWT Ticket And VIRN***********");
			return nbrFormat.toString();

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}
	}

	public Map<Integer, String> fetchOrganizationListMap(String listType,
			UserInfoBean userInfo) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			OrgBean agentOrgBean = null;
			Map<Integer, String> searchResults = new HashMap<Integer, String>();

			// create database connection
			 
			connection = DBConnect.getConnection();

			String query = null;
			if (listType.equalsIgnoreCase("agent")) {
				query = QueryManager.getST1AgtOrgQuery();// +" group by
				// name";
			} else if (listType.equalsIgnoreCase("agentPwt")) {
				query = QueryManager.getST1AgtOrgQueryPwt();// +" group by
				// name";
			}
			statement = connection.prepareStatement(query);
			statement.setInt(1, userInfo.getUserOrgId());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				searchResults.put(resultSet.getInt(TableConstants.SOM_ORG_ID),
						resultSet.getString(TableConstants.SOM_ORG_NAME));
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
			}
		}
	}

	public String fetchRetailerNameList(int userOrgId, String retType)
			throws LMSException {
		StringBuilder retNameList = new StringBuilder();
		Connection con = null;
		String retTypeQuery = "";
		if (!"ALL".equalsIgnoreCase(retType.trim())) {
			retTypeQuery = "and status = '" + retType.trim() + "'";
		}
		try {
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("select organization_id, name, organization_type from  st_lms_organization_master where organization_type='RETAILER' and parent_id = ?"
							+ retTypeQuery + " order by name");
			pstmt.setInt(1, userOrgId);
			ResultSet rs = pstmt.executeQuery();
			logger.debug("retrieve the Retailer Name List query ==== " + pstmt);
			while (rs.next()) {
				int orgId = rs.getInt("organization_id");
				String Name = rs.getString("name");
				retNameList.append("," + orgId + "=" + Name);
			}
			if (retNameList.length() > 0) {
				retNameList.delete(0, 1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug("game name list === " + retNameList);

		return retNameList.toString();
	}

	public String fetchRetailernGameFmt(int agtOrgId) throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuilder nbrFormat = new StringBuilder();
		Statement stmt1 = null;
		PreparedStatement statement = null;
		Connection con = DBConnect.getConnection();

		ResultSet rs = null;
		ResultSet rs2 = null;

		try {
			stmt1 = con.createStatement();
			rs = stmt1
					.executeQuery("select res.game_name,res.game_nbr,res.game_id,sgtnf.pack_nbr_digits,sgtnf.book_nbr_digits,sgtnf.ticket_nbr_digits,sgtnf.game_nbr_digits,sgtnf.game_virn_digits from st_se_game_ticket_nbr_format sgtnf,(select game_name,game_nbr,game_id from st_se_game_master where game_status='OPEN' OR game_status='SALE_CLOSE' OR game_status='SALE_HOLD') res where sgtnf.game_id = res.game_id");
			while (rs.next()) {
				nbrFormat.append(rs.getString("game_name") + ":");
				nbrFormat.append(rs.getString("game_nbr") + ":");
				nbrFormat.append(rs.getInt("pack_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("book_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("ticket_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("game_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("game_virn_digits") + "Nx*");
			}
			// String query = QueryManager.getST1RetOrgQuery();// +" order by
			// name";

			String query = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su where so.organization_type = 'RETAILER' and (so.organization_status='ACTIVE' or so.organization_status='INACTIVE') and so.organization_id = su.organization_id and su.isrolehead = 'Y' and so.parent_id = ?  order by name";
			statement = con.prepareStatement(query);
			statement.setInt(1, agtOrgId);
			rs2 = statement.executeQuery();
			while (rs2.next()) {
				nbrFormat.append(rs2.getInt("organization_id") + ";"
						+ rs2.getString("name") + ":");
			}
			stmt1.close();
			con.close();
			logger.debug(nbrFormat.toString()
					+ "*****PWT Ticket And VIRN***********");
			return nbrFormat.toString();

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}
	}

	public Map fetchRoles() throws LMSException {
		Map roleMap = new TreeMap();

		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {

			stmt = con.createStatement();
			// ResultSet rs = stmt.executeQuery("select role_id,role_name from
			// st_lms_role_master where role_owner='BO'and is_master='N' order
			// by role_name");
			ResultSet rs = stmt
					.executeQuery("select role_id,role_name from st_lms_role_master where tier_id = "
							+ "(select tier_id from st_lms_tier_master where tier_code = 'BO')and is_master='N' order by role_name");
			while (rs.next()) {

				roleMap.put(rs.getInt("role_id"), rs.getString("role_name"));
			}
			DBConnect.closeCon(con);
		} catch (Exception e) {
			throw new LMSException(e);
		}
		return roleMap;
	}

	public List getAgents(String listType, UserInfoBean userInfo)
			throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			OrgBean agentOrgBean = null;
			List searchResults = new ArrayList();

			// create database connection
			 
			connection = DBConnect.getConnection();

			String query = null;
			if (listType.equalsIgnoreCase("agent")) {
				query = CommonMethods.appendRoleAgentMappingQuery(QueryManager.getST1AgtOrgQueryWithoutSort(), "so.organization_id",userInfo.getRoleId());// +" group by
				// name";
			} else if (listType.equalsIgnoreCase("agentPwt")) {
				query = QueryManager.getST1AgtOrgQueryPwt();// +" group by
				// name";
			}
			statement = connection.prepareStatement(query);
			statement.setInt(1, userInfo.getUserOrgId());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				searchResults.add(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
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
			}
		}
	}

	public String getAvlblCreditAmt(UserInfoBean bean) {
		Connection con=null;
		 String avlCredit=null;
		try {
		 con = DBConnect.getConnection();
		  avlCredit = getAvlblCreditAmt(bean, con);
		 
		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return avlCredit;
	}

	
	public String getAvlblCreditAmt(UserInfoBean bean,Connection con) {
		UserInfoBean userBean = bean;
		String avlCredit = "N";
		int userOrgId = userBean.getUserOrgId();
		Double availableCredit = null;// userBean.getAvailableCreditLimit();'
		Double unClmBal = 0.00;// akhil
		Double clmBal = 0.00;
		Double ext_credit_limit = null;
		Double credit_limit = null;
		Date ext_credit_limit_date = null;
		  // /akhil
		//logger.info("asdhjfhfhsjkhjdfhjhk");
		//Calendar cal = Calendar.getInstance();
		long current_date = 0;
		long extended_date = 0;
		int remaining_days = 0;
		
		try {
			Statement stmt = con.createStatement();

			String getACtDetails = "select available_credit, claimable_bal, unclaimable_bal,extended_credit_limit,credit_limit,extends_credit_limit_upto from st_lms_organization_master where organization_type!='BO' and organization_id = "
					+ userOrgId;
			ResultSet result = stmt.executeQuery(getACtDetails);

			if (result.next()) {
				availableCredit = result.getDouble("available_credit");
				unClmBal = result.getDouble("unclaimable_bal");
				clmBal = result.getDouble("claimable_bal");
				ext_credit_limit = result.getDouble("extended_credit_limit");
				credit_limit = result.getDouble("credit_limit");
				ext_credit_limit_date = result
						.getDate("extends_credit_limit_upto");

				// logger.debug("***$$$clm balance"+clmBal);
				//logger.info("date is akhil " + ext_credit_limit_date);

				current_date = new java.sql.Date(new java.util.Date().getTime())
						.getTime();
				if (ext_credit_limit_date != null
						&& !ext_credit_limit_date.toString().equals("")) {

					extended_date = ext_credit_limit_date.getTime();
					logger.info("extended date" + extended_date);
					remaining_days = (int) ((extended_date - current_date) / (1000 * 60 * 60 * 24)) + 1;

					//logger.info("Remaining no of days" + remaining_days);

				}

				avlCredit = FormatNumber.formatNumber(availableCredit) + "="
						+ FormatNumber.formatNumber(clmBal) + "="
						+ FormatNumber.formatNumber(unClmBal) + "="
						+ FormatNumber.formatNumber(availableCredit - clmBal)
						+ "=" + FormatNumber.formatNumber(ext_credit_limit)
						+ "=" + FormatNumber.formatNumber(remaining_days)
						+ "=" + FormatNumber.formatNumber(credit_limit);
			}

			userBean.setAvailableCreditLimit(availableCredit);
			userBean.setClaimableBal(clmBal);
			userBean.setUnclaimableBal(unClmBal);

			//logger.info(avlCredit + "****---------********");
		} catch (Exception e) {
			logger.error("Exception ",e);
			 //e.printStackTrace();
		} 
		return avlCredit;
	}

	public static void getLiveAmt(UserInfoBean userBean,Connection con) {
				
		try {
			Statement stmt = con.createStatement();

			String getACtDetails = "select available_credit,claimable_bal from st_lms_organization_master where organization_id = "
					+ userBean.getUserOrgId();
			ResultSet result = stmt.executeQuery(getACtDetails);

			if (result.next()) {
				userBean.setAvailableCreditLimit(result.getDouble("available_credit"));
				userBean.setClaimableBal(result.getDouble("claimable_bal"));
				}
		
		} catch (Exception e) {
			logger.error("Exception ",e);
		} 
		
	}
	
	public List getCountry() throws LMSException {
		List countryList = new ArrayList();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();

		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			String countryNames = QueryManager.getST3Country();
			rs = stmt1.executeQuery(countryNames);
			while (rs.next()) {
				countryList.add(rs.getString(TableConstants.SOM_ORG_NAME));
			}
			stmt1.close();
			con.close();
			return countryList;

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}

	public List<String> getGameDetailForCommVar(String gameName,
			String agentOrgName) throws LMSException {
		List<String> gameNameList = new ArrayList<String>();
		String[] game = gameName.split("-");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select game_nbr, game_name, game_status from st_se_game_master where (game_status like ? and game_status!='TERMINATE')");
			if ("ALL".equalsIgnoreCase(gameName.trim())) {
				pstmt.setString(1, "%");
			} else {
				pstmt.setString(1, game[0].trim());
			}
			rs = pstmt.executeQuery();
			System.out
					.println("retrieve the game detail for agent comm variance query ==== "
							+ pstmt);
			while (rs.next()) {
				String gameNbr = String.valueOf(rs.getInt("game_nbr"));
				String Name = rs.getString("game_name");
				gameNameList.add(gameNbr + "-" + Name);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug("game name list === " + gameNameList);

		return gameNameList;
	}

	public List getGameList(String game_type) throws LMSException {
		List gameList = new ArrayList();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();

			String gmdetail = QueryManager.getGameDetails() + "'" + game_type
					+ "' and add_inv_status='F'";
			rs = stmt1.executeQuery(gmdetail);
			while (rs.next()) {
				gameList.add(rs.getString(TableConstants.GAME_NAME));
			}
			stmt1.close();
			con.close();
			return gameList;

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}

	public String getGameListing(String serviceCode) {
		String result = "";
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			if ("SE".equalsIgnoreCase(serviceCode)) {
				pstmt = con
						.prepareStatement("select game_id, concat(game_nbr, concat('-',game_name)) 'game_name', govt_comm_rate  from st_se_game_master where game_status !='TERMINATE'");
			} else if ("DG".equalsIgnoreCase(serviceCode)) {
				pstmt = con
						.prepareStatement("select game_id, concat(game_nbr, concat('-',game_name)) 'game_name', govt_comm, govt_comm_pwt from st_dg_game_master where game_status !='TERMINATE' order by display_order");
			} else if ("IW".equalsIgnoreCase(serviceCode)) {
				pstmt = con.prepareStatement("select game_id, concat(game_no, concat('-',game_disp_name)) 'game_name', gov_comm_rate  from st_iw_game_master where game_status !='TERMINATE' order by display_order");
			} else if ("VS".equalsIgnoreCase(serviceCode)) {
				pstmt = con.prepareStatement("select game_id, concat(game_no, concat('-',game_disp_name)) 'game_name', gov_comm_rate, govt_comm_pwt_rate from st_vs_game_master where game_status !='TERMINATE' order by display_order");
			} else if ("SLE".equalsIgnoreCase(serviceCode)) {
				pstmt = con.prepareStatement("SELECT SQL_CACHE game_type_id, CONCAT(game_type_id, CONCAT('-',type_disp_name)) 'game_name', gov_comm_rate, govt_pwt_comm_rate FROM st_sle_game_type_master WHERE type_status!='TERMINATE' ORDER BY display_order;");
			}
			rs = pstmt.executeQuery();
			logger.debug("getgameList");
			while (rs.next()) {
				String gameIdAmt = "";
				if (serviceCode.equalsIgnoreCase("SE")) {
					gameIdAmt = rs.getInt("game_id") + ":"
							+ rs.getDouble("govt_comm_rate");
				} else if (serviceCode.equalsIgnoreCase("DG")) {
					gameIdAmt = rs.getInt("game_id") + ":" + rs.getDouble("govt_comm") + "_" + rs.getDouble("govt_comm_pwt");
				} else if ("IW".equalsIgnoreCase(serviceCode)) {
					gameIdAmt = rs.getInt("game_id") + ":" + rs.getDouble("gov_comm_rate");
				} else if ("VS".equalsIgnoreCase(serviceCode)) {
					gameIdAmt = rs.getInt("game_id") + ":" + rs.getDouble("gov_comm_rate")+ "_" + rs.getDouble("govt_comm_pwt_rate");
				} else if (serviceCode.equalsIgnoreCase("SLE")) {
					gameIdAmt = rs.getInt("game_type_id") + ":" + rs.getDouble("gov_comm_rate") + "_" + rs.getDouble("govt_pwt_comm_rate");
				}
				String gameName = rs.getString("game_name");
				result += gameIdAmt + "||" + gameName + "|:";
				// gameMap.put(gameIdAmt, gameName);
			}
			logger.debug("result1 " + result);
			if (!result.equals("")) {
				result = result.substring(0, result.length() - 2);
			}
			logger.debug("result2 " + result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return result;
	}

	public String getGameNameList(String gameStatus) throws LMSException {

		StringBuilder gameNameList = new StringBuilder();
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("select game_id, game_nbr, game_name, game_status from st_se_game_master where game_status = ?");
			pstmt.setString(1, gameStatus.trim());
			ResultSet rs = pstmt.executeQuery();
			System.out
					.println("retrieve the game detail for agent comm variance query ==== "
							+ pstmt);
			while (rs.next()) {
				String gameNbr = String.valueOf(rs.getInt("game_nbr"));
				String Name = rs.getString("game_name");
				gameNameList.append("," + rs.getInt("game_id") + "=" + gameNbr
						+ "-" + Name);
			}
			if (gameNameList.length() > 0) {
				gameNameList.delete(0, 1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug("game name list === " + gameNameList);

		return gameNameList.toString();
	}

	public List<String> getGameNameListForCommVar(String gameType)
			throws LMSException {
		List<String> gameNameList = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select game_nbr, game_name, game_status from st_se_game_master where (game_status like ? and game_status!='TERMINATE')");
			if ("ALL".equalsIgnoreCase(gameType.trim())) {
				pstmt.setString(1, "%");
			} else {
				pstmt.setString(1, gameType.trim());
			}
			rs = pstmt.executeQuery();
			System.out
					.println("retrieve the game detail for agent comm variance query ==== "
							+ pstmt);
			while (rs.next()) {
				String gameNbr = String.valueOf(rs.getInt("game_nbr"));
				String gameName = rs.getString("game_name");
				gameNameList.add(gameNbr + "-" + gameName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug("game name list === " + gameNameList);

		return gameNameList;
	}

	public String getGameNbrFormat(String game_name) throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuilder nbrFormat = new StringBuilder();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			rs = stmt1
					.executeQuery("select * from st_se_game_ticket_nbr_format where game_id=(select game_id from st_se_game_master where game_name='"
							+ game_name + "' )");
			while (rs.next()) {
				nbrFormat.append(rs.getInt("pack_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("book_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("ticket_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("game_nbr_digits") + ":");
				nbrFormat.append(rs.getInt("game_rank_digits") + ":");
				nbrFormat.append(rs.getInt("game_virn_digits") + ":");
			}
			stmt1.close();
			con.close();

			return nbrFormat.toString();

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}

	public String getGamePrizeList(String game_name) throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuilder przAmt = new StringBuilder();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			rs = stmt1
					.executeQuery("select distinct(prize_amt) from st_se_rank_master where game_id=(select game_id from st_se_game_master where game_name='"
							+ game_name
							+ "' ) and prize_amt <= "
							+ sc.getAttribute("HIGH_PRIZE_AMT")
							+ " order by prize_amt");
			while (rs.next()) {
				przAmt.append(FormatNumber.formatNumberForJSP(rs
						.getDouble("prize_amt"))
						+ ":");
			}
			stmt1.close();
			con.close();
			return przAmt.toString();

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}

	public List getOrganization(UserInfoBean bean) throws LMSException {
		List OrganizationList = new ArrayList();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			String userType = bean.getUserType();
			String roleName = bean.getRoleName();
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode  ";
			

			}
			if (userType.equals("BO")) {
				if (bean.getIsMasterRole().equals("Y")
						&& bean.getIsRoleHeadUser().equals("Y")) {
					logger.debug("role name is BO MAS");
					rs = stmt1
							.executeQuery("select "+orgCodeQry+",organization_type,organization_id  from st_lms_organization_master where organization_type='BO'");
				} else {
					rs = stmt1
							.executeQuery("select "+orgCodeQry+",organization_type,organization_id  from st_lms_organization_master where organization_type='AGENT'");
				}

			}
			if (userType.equals("AGENT")) {
				rs = stmt1
						.executeQuery("select "+orgCodeQry+",organization_type,organization_id  from st_lms_organization_master where organization_type='RETAILER'");

			}
			while (rs.next()) {
				OrganizationList.add(rs.getString("orgCode"));
			}
			stmt1.close();
			con.close();
			return OrganizationList;

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}
/**
 * New Common method to get orgList 
 * @param userOrgId
 * @param orgType
 * @return
 * @throws LMSException
 */
	public String getOrgIdList(int userOrgId, String orgType)
			throws LMSException {
		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		Statement stmt =null;
		ResultSet rs = null;
		String query=null;
		
		try {
		
			
			query =QueryManager.getOrgInfoQry(userOrgId,orgType);
			stmt=con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				orgList.append(rs.getString("orgCode")).append("|").append(rs.getString("organization_id")).append(":"); 
			}
			//logger.info("orgList-------" + orgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList.toString();
	}
	
	public static Map<String, String> getOrgIdMap(int userOrgId, String orgType) throws LMSException {
		Map<String, String> orgMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		Statement stmt =null;
		ResultSet rs = null;
		String query=null;
		try {
				query =QueryManager.getOrgInfoQry(userOrgId,orgType);
				stmt=con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {					
					orgMap.put(rs.getString("organization_id"),rs.getString("orgCode"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				DBConnect.closeConnection(con, stmt, rs);
			}
		return orgMap;
	}

	public String getAllOrgIdList(int userOrgId, String orgType)
			throws LMSException {
		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		Statement stmt =null;
		ResultSet rs = null;
		String query=null;
		
		try {
		
			
			query =QueryManager.getAllOrgInfoQry(userOrgId,orgType);
			stmt=con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				orgList.append(rs.getString("orgCode")).append("|").append(rs.getString("organization_id")).append(":"); 
			}
			//logger.info("orgList-------" + orgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList.toString();
	}
	
	public String getOrgList(int userOrgId, String orgType) throws LMSException {
		String orgList = "";
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			logger
					.debug("Query:--- select name from st_lms_organization_master where organization_type='"
							+ orgType + "' and parent_id='" + userOrgId + "' order by name");
			rs = stmt
					.executeQuery("select name from st_lms_organization_master where organization_type='"
							+ orgType + "' and parent_id='" + userOrgId + "' order by name");
			while (rs.next()) {
				orgList += rs.getString("name") + "|:";
			}

			if (!orgList.equals("")) {
				orgList = orgList.substring(0, orgList.length() - 2);
			}

			logger.debug("orgList-------" + orgList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList;
	}

	public String getUserIdList(int orgId) {
		
		

		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		String query=null;
	try {
			String appendOrder =QueryManager.getAppendOrgOrder();
			
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE") ){
				
				
				query ="select org_code orgCode,organization_id from st_lms_organization_master where parent_id=?  order by   "+appendOrder;
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME") ){
				
				query ="select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where parent_id=?  order by  "+appendOrder;
			
					
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
				
				
				query ="select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where parent_id=?  order by   "+appendOrder;
			
				
				
			}else {
				
				
				query ="select name orgCode,organization_id from st_lms_organization_master where  parent_id=?  order by  "+appendOrder;
				
			
				
			}
	
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,orgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.append(rs.getString("orgCode")).append("|").append(rs.getString("organization_id")).append(":"); 
			}
			logger.info("orgList-------" + orgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
	
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList.toString();
	
		
		
		
	}
	public String getOrgUserIdList(int orgId) {
		String orgUserList = "";
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select user_id, user_name from st_lms_user_master where organization_id = "
							+ orgId + " order by user_name");
			while (rs.next()) {
				orgUserList += rs.getString("user_name") + "|"
						+ rs.getString("user_id") + ":";
			}
			logger.debug("orgList-------" + orgUserList);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgUserList;
	}

	public static ArrayList<Integer> getUserIdList(int orgId , boolean isRetailer) {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Integer> orgUserList = null;
		String searchUserId = null;
		try {
			if(isRetailer){
				searchUserId = "select user_id from st_lms_user_master where organization_id = "+orgId;
			}else{
				searchUserId = "select user_id from st_lms_user_master where parent_user_id = (select user_id from st_lms_user_master where organization_id = "+orgId+")";
			}
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(searchUserId);
			orgUserList = new ArrayList<Integer>();
			while (rs.next()) {
				orgUserList.add(rs.getInt("user_id"));
			}
			logger.debug("orgList-------" + orgUserList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
		if(orgUserList != null)
			orgUserList.trimToSize();
		return orgUserList;
	}

	public String getPacknBookList(String agentOrgId, String gameName) {

		Connection con = DBConnect.getConnection();

		Statement stmt = null;
		Statement stmt1 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;

		StringBuilder packNbook = new StringBuilder();
		try {
			stmt = con.createStatement();
			stmt1 = con.createStatement();

			String packNumQuery = "select pack_nbr from (select nbr_of_books_per_pack from st_se_game_master where game_name='"
					+ gameName
					+ "')res1 ,(select distinct pack_nbr,count(*) no_of_books from st_se_game_inv_status where current_owner_id="
					+ agentOrgId
					+ " and game_id=(select game_id from st_se_game_master where game_name='"
					+ gameName
					+ "') and (book_status!='MISSING' and book_status!='CLAIMED') group by pack_nbr) res2 where res1.nbr_of_books_per_pack = res2.no_of_books";

			String bookNumQuery = "select distinct book_nbr from st_se_game_inv_status where current_owner_id="
					+ agentOrgId
					+ " and game_id=(select game_id from st_se_game_master where game_name='"
					+ gameName
					+ "') and (book_status!='MISSING' and book_status!='CLAIMED')";

			rs = stmt.executeQuery(packNumQuery);

			while (rs.next()) {
				packNbook.append(rs.getString("pack_nbr") + ":");

			}
			packNbook.append("book_nbr");
			rs1 = stmt1.executeQuery(bookNumQuery);

			while (rs1.next()) {
				packNbook.append(rs1.getString("book_nbr") + ":");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return packNbook.toString();
	}

	public String getQrgNameHeadUserId(int userOrgId, String orgType)
			throws LMSException {
		String orgList = "";
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select om.name,om.organization_id,um.user_id from st_lms_organization_master om,st_lms_user_master um where om.organization_type='"
							+ orgType
							+ "' and om.parent_id='"
							+ userOrgId
							+ "' and um.isrolehead='Y' and om.organization_type=um.organization_type and om.organization_id=um.organization_id");
			while (rs.next()) {
				orgList += rs.getString("name") + "|"
						+ rs.getString("organization_id") + "|"
						+ rs.getString("user_id") + ":";
			}
			logger.debug("orgList-------" + orgList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList;
	}

	public List getSupplier() throws LMSException {
		List supplierList = new ArrayList();
		Statement stmt1 = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;

		try {
			stmt1 = con.createStatement();
			rs = stmt1.executeQuery("select name from st_se_supplier_master");
			while (rs.next()) {
				supplierList.add(rs.getString("name"));
			}
			stmt1.close();
			con.close();
			return supplierList;

		}

		catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);

		}

	}

	public static String getOrgNameFromUserId(int userId, String orgType) {
		Connection con = DBConnect.getConnection();
		String orgName = null;
		try {
			String query = null;
			if ("RETAILER".equalsIgnoreCase(orgType)) {
				query = "select om.name from st_lms_organization_master om, st_lms_user_master um where om.organization_id = um.organization_id and um.user_id = ?";
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				query = "select name from st_lms_organization_master where organization_id = (select organization_id from st_lms_user_master where user_id=(select parent_user_id from st_lms_user_master where user_id = ?))";
			}

			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userId);
			logger.debug("***pstmt*****" + pstmt + "****");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				orgName = rs.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgName;
	}

	public String getOrgListForAll(String orgType) throws LMSException
	{
		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;

		try {
			
			
			
			pstmt = con.prepareStatement("select organization_id, "+QueryManager.getOrgCodeQuery()+" from st_lms_organization_master where organization_type=? order by "+QueryManager.getAppendOrgOrder());
			pstmt.setString(1,orgType);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.append(rs.getString("organization_id")).append("|").append(rs.getString("orgCode")).append(":"); 
			}
			logger.info("orgList-------" + orgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList.toString();
	}

	public List<OrganizationBean> getBoAgtList(String orgType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrganizationBean> organizationList = new ArrayList<OrganizationBean>();
		OrganizationBean organizationBean = null;

		String query = "";
		if(orgType.equals("BO")) {
			String orgCodeQry = " user_name orgCode  ";
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE"))
				orgCodeQry = " org_code orgCode ";
			else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME"))
				orgCodeQry = " concat(org_code,'_',user_name)  orgCode ";
			else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE"))
				orgCodeQry = " concat(user_name,'_',org_code)  orgCode ";
			query = "SELECT user_id AS userId, "+orgCodeQry+" FROM st_lms_user_master u INNER JOIN st_lms_organization_master o ON o.organization_id=u.organization_id WHERE u.organization_type='BO' ORDER BY "+QueryManager.getAppendOrgOrder();
		} else {
			//query = "SELECT user_id, user_name FROM st_lms_user_master WHERE organization_type='AGENT' AND isrolehead='y' ORDER BY user_name;";
			query = "SELECT u.user_id as userId, u.user_name as userName, "+QueryManager.getOrgCodeQuery()+" FROM st_lms_user_master u INNER JOIN st_lms_organization_master o ON u.organization_id=o.organization_id WHERE u.organization_type='AGENT' AND u.isrolehead='y' and o.organization_status !='TERMINATE'  ORDER BY "+QueryManager.getAppendOrgOrder();
		}

		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query);
			logger.info("getBoAgtList - " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//orgUserList += rs.getString("userId") + "|" + rs.getString("orgCode") + ":";
				organizationBean = new OrganizationBean();
				organizationBean.setOrgId(rs.getInt("userId"));
				organizationBean.setOrgName(rs.getString("orgCode"));
				organizationList.add(organizationBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return organizationList;
	}

	public static String getCountryListWithCode() {
		StringBuilder listWithCode = new StringBuilder();
		PreparedStatement pstmt = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(QueryManager.getST3Country());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listWithCode.append(rs.getString("country_code") + ":"
						+ rs.getString("name")+"|");

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return listWithCode.toString();
}	
	
	public static String getCityAndStateBuilder(){

		ResultSet rs= null;
 		Connection con= null;
		PreparedStatement pstmt = null;
		TreeMap<String, String> map = null;
		try {
			con =  DBConnect.getConnection();
			pstmt =  con.prepareStatement("select sm.state_code ,city_code, name state_name, city_name from st_lms_state_master sm inner join st_lms_city_master cm on sm.state_code=cm.state_code order by state_code");
			rs =  pstmt.executeQuery();
			map = new TreeMap<String, String>();
			while(rs.next())
			{
				String cityBuilder = "";
					if(map.containsKey(rs.getString("state_name"))){
						cityBuilder += map.get(rs.getString("state_name"))+rs.getString("city_name")+"-"+rs.getString("city_code")+"," ;
						map.put(rs.getString("state_name"), cityBuilder);
					}else{
						map.put(rs.getString("state_name"), rs.getString("city_name")+"-"+rs.getString("city_code")+",");
					}
			}
		} catch (Exception e) {
			logger.error("Exception :" , e);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return map.toString().replace(",,", "|").replace("{","").replace("}", "");
	}
	
	public static String getZoneAndCodeBuilder(String cityName){

		ResultSet rs= null;
 		Connection con= null;
		PreparedStatement pstmt = null;
		TreeMap<String, String> map = null;
		try {
			con =  DBConnect.getConnection();
			pstmt =  con.prepareStatement("select area_name,area_code from st_lms_area_master  where city_code=? order by area_name");
			pstmt.setString(1, cityName);
			rs =  pstmt.executeQuery();
			map = new TreeMap<String, String>();
			while(rs.next())
			{
				String cityBuilder = "";
					if(map.containsKey(rs.getString("area_name"))){
						cityBuilder += map.get(rs.getString("area_name"))+rs.getString("area_code")+"," ;
						map.put(rs.getString("area_name"), cityBuilder);
					}else{
						map.put(rs.getString("area_name"), rs.getString("area_code")+",");
					}
			}
		} catch (Exception e) {
			logger.error("Exception :" , e);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return map.toString().replace(",,", "|").replace("{","").replace("}", "");
	}
	public String getOrgListForArea(String orgType,String state,String  city,String  area) throws LMSException
	{
		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;

		try {
			
			StringBuilder sb = new StringBuilder();
			if(state!=null&&!state.equalsIgnoreCase("-1")){
				sb.append(" and state_code='"+state.trim()+"' ");
			}
			if(city!=null&&!city.equalsIgnoreCase("-1")){
				sb.append(" and city='"+city.trim()+"'");
			}
			if(area!=null&&!area.equalsIgnoreCase("-1")){
				sb.append(" and find_in_set(area_code,'"+area.trim()+"')");
				
			}
			
			pstmt = con.prepareStatement("select organization_id, "+QueryManager.getOrgCodeQuery()+" from st_lms_organization_master where organization_type=? "+sb.toString()+" order by "+QueryManager.getAppendOrgOrder());
			pstmt.setString(1,orgType);
			logger.debug("orgList Query" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgList.append(rs.getString("organization_id")).append("|").append(rs.getString("orgCode")).append(":"); 
			}
			
		} catch (Exception e) {
			logger.error("Exception ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeRs(rs);
			DBConnect.closePstmt(pstmt);
			DBConnect.closeCon(con);
		}
		return orgList.toString();
	}
	
	public static Map<String, String> getUserIdOgrCodeMap(boolean isAll , int agentOrgId) {

		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		Map<String, String> userMap = null;
		try {
			String orgCodeQry = " a.name orgCode  ";
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " a.org_code orgCode ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(a.org_code,'_',a.name)  orgCode ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(a.name,'_',a.org_code)  orgCode ";
			}
			
			con = DBConnect.getConnection();
			userMap = new HashMap<String, String>();
			String query = "select b.user_id user_id ,"
					+ orgCodeQry
					+ " from st_lms_organization_master a, st_lms_user_master b  where a.organization_type='RETAILER' and a.organization_id=b.organization_id "+(!isAll ? "and a.parent_id  = "+agentOrgId:"");
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				userMap.put(rs.getString("user_id"), rs.getString("orgCode"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
		return userMap;
	}
	
	public String getOrgIdListStateAndCityWise(int userOrgId, String orgType, String state, String city) throws LMSException {
		StringBuilder orgList = new StringBuilder();
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;

		try {
			query = QueryManager.getOrgInfoQryStateAndCityWise(userOrgId, orgType, state, city);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				orgList.append(rs.getString("orgCode")).append("|").append(rs.getString("organization_id")).append(":");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgList.toString();
	}


	
	
	public static int getAgentOrgIdByRetailerOrgId(int retId) {
		int orgUser = 0;
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select b.organization_id from st_lms_user_master a inner join st_lms_user_master b on a.parent_user_id = b.user_id where a.organization_id = '" + retId+ "';");
			while (rs.next()) {
				orgUser = rs.getInt("organization_id");
			}
			System.out.println("orgList-------" + orgUser);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgUser;
	}
	
	/**
	 * New Common method to get orgList for Debit Note in safaribet
	 * @param userOrgId
	 * @param orgType
	 * @return
	 * @throws LMSException
	 */
		public String getOrgIdList(int userOrgId, String orgType,int roleId) throws LMSException {
			StringBuilder orgList = new StringBuilder();
			Connection con = DBConnect.getConnection();
			Statement stmt =null;
			ResultSet rs = null;
			String query=null;
			try {
				if("RETAILER".equalsIgnoreCase(orgType)){
					query=QueryManager.getOrgInfoQry(userOrgId,orgType);
				}else{
					query = CommonMethods.appendRoleAgentMappingQuery(QueryManager.getOrgInfoQryWithoutSort(userOrgId,orgType),"organization_id",roleId)+ "order by orgCode ASC ";	
				}
				logger.info("getOrgIdList method with :- :" + query);
				stmt=con.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					orgList.append(rs.getString("orgCode")).append("|").append(rs.getString("organization_id")).append(":"); 
				}
			} catch (Exception e) {
				logger.error(e);
				throw new LMSException(e);
			} finally {
				DBConnect.closeResource(con,stmt,rs);
			}
			return orgList.toString();
		}
	
}