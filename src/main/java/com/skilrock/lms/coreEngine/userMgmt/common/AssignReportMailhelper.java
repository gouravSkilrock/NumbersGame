package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class AssignReportMailhelper {

	public static void main(String[] args) {

		/*
		 * //Map<String, String> map= new
		 * AssignReportMailhelper().getBOUserList("BO MASTER", "ALL", 349); List
		 * agtList=new AssignReportMailhelper().getAgentList(); //String
		 * list=map.toString(); String list=agtList.toString();
		 * list=list.replace("[", ""); list=list.replace("]", "");
		 * System.out.println("list is ='"+list+"'");
		 */

		String str = "";
		String abc[] = new String[0];
		if (!"".equalsIgnoreCase(str.trim())) {
			abc = str.split(",");
		}
		for (String string : abc) {
			System.out.println(abc.length + " " + string);
		}
		System.out.println("kfjsdfklsdkl");

	}

	public void createEmailPriviledges(String userId, String orgType,
			String email, String[] activePrivIdList, String[] inactivePrivIdList) {
		Connection con = DBConnect.getConnection();
		int newUserId = Integer.parseInt(userId);
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);

			// verify that email id is already registered
			boolean isEmailExist = false;
			pstmt = con
					.prepareStatement("select aa.user_id from  st_lms_report_email_user_master aa where aa.email_id=? and aa.ref_user_id=?");
			pstmt.setString(1, email);
			pstmt.setInt(2, Integer.parseInt(userId));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("email already registered ========== ");
				newUserId = rs.getInt("user_id");
				isEmailExist = true;

				// update the privileges list
				pstmt = con
						.prepareStatement(" update st_lms_report_email_priv_master set status='INACTIVE' where user_id =?");
				pstmt.setInt(1, newUserId);
				int updateRow = pstmt.executeUpdate();
				System.out.println("total row inactive == " + updateRow);

				for (String emailPid : activePrivIdList) {
					pstmt = con
							.prepareStatement("update st_lms_report_email_priv_master set status='ACTIVE' where user_id=? and email_pid=?");
					pstmt.setInt(1, newUserId);
					pstmt.setInt(2, Integer.parseInt(emailPid));
					updateRow = pstmt.executeUpdate();
					System.out.println("total row active == " + updateRow);
				}
			} else {
				// get details from user master
				System.out
						.println(" new user for registration in email ============= ");
				pstmt = con
						.prepareStatement("select  aa.user_id 'ref_user_id',first_name, last_name, email_id, phone_nbr, organization_id, organization_type, status  from st_lms_user_master aa, st_lms_user_contact_details bb where aa.user_id=bb.user_id and aa.user_id=?");
				pstmt.setInt(1, Integer.parseInt(userId));
				rs = pstmt.executeQuery();
				if (rs.next()) {

					String first_name = rs.getString("first_name");
					String last_name = rs.getString("last_name");
					String email_id = rs.getString("email_id");
					String organization_type = rs
							.getString("organization_type");
					String status = rs.getString("status");
					long phone_nbr = rs.getLong("phone_nbr");
					int organization_id = rs.getInt("organization_id");
					int refuserid = rs.getInt("ref_user_id");
					System.out.println("details getted are " + first_name + " "
							+ last_name + " " + email_id + " "
							+ organization_type + " " + status + " "
							+ phone_nbr + " " + organization_id + " "
							+ refuserid);

					// insert details into st_lms_report_email_user_master
					pstmt = con
							.prepareStatement("insert into st_lms_report_email_user_master (ref_user_id, organization_id, organization_type, first_name, last_name, email_id, mob_no, registration_date, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?) ");
					pstmt.setInt(1, refuserid);
					pstmt.setInt(2, organization_id);
					pstmt.setString(3, organization_type);
					pstmt.setString(4, first_name);
					pstmt.setString(5, last_name);
					pstmt.setString(6, email_id);
					pstmt.setLong(7, phone_nbr);
					pstmt.setTimestamp(8, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					pstmt.setString(9, "ACTIVE");
					int updateRow = pstmt.executeUpdate();

					if (updateRow > 0) {
						System.out
								.println("inserttion into st_lms_report_email_user_master table is done");
						rs = pstmt.getGeneratedKeys();
						if (rs.next()) {
							newUserId = rs.getInt(1);
							System.out.println("Auto generated keys are : "
									+ newUserId);
						}

						System.out.println("activePrivIdList : "
								+ activePrivIdList);

						//
						for (String emailPid : inactivePrivIdList) {
							pstmt = con
									.prepareStatement(" insert into st_lms_report_email_priv_master (user_id, email_pid, status) values (?, ?, ?) ");
							pstmt.setInt(1, newUserId);
							pstmt.setInt(2, Integer.parseInt(emailPid));
							pstmt.setString(3, "INACTIVE");
							updateRow = pstmt.executeUpdate();
							System.out
									.println("total row inserted as inactive == "
											+ updateRow);
						}
						for (String emailPid : activePrivIdList) {
							pstmt = con
									.prepareStatement(" insert into st_lms_report_email_priv_master (user_id, email_pid, status) values (?, ?, ?) ");
							pstmt.setInt(1, newUserId);
							pstmt.setInt(2, Integer.parseInt(emailPid));
							pstmt.setString(3, "ACTIVE");
							updateRow = pstmt.executeUpdate();
							System.out.println("total row active == "
									+ updateRow);
						}
					} else {
						System.out
								.println("Problem in inserttion into st_lms_report_email_user_master table ");
						throw new LMSException("");
					}

				}
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
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

	}

	/*
	 * private Map<String, Integer> getLoginUserIdFromMail(Connection con, int
	 * userId) { Map<String, Integer> map=new TreeMap<String, Integer>();
	 * 
	 * PreparedStatement pstmt=null; try {
	 * 
	 * pstmt=con.prepareStatement("select aa.user_id, ref_user_id, cc.user_id
	 * 'email_user_id' from st_lms_user_master aa, st_lms_role_master
	 * bb,st_lms_report_email_user_master cc where aa.role_id = bb.role_id and
	 * bb.role_name = 'BO_MAS' and aa.isrolehead = 'Y' and aa.user_id =
	 * cc.ref_user_id and aa.user_id = "+userId); ResultSet
	 * rs=pstmt.executeQuery();
	 * 
	 * System.out.println("get login user id"); if(rs.next()) {
	 * map.put("user_id", rs.getInt("user_id")); map.put("email_user_id",
	 * rs.getInt("email_user_id")); map.put("ref_user_id",
	 * rs.getInt("ref_user_id")); } rs.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } return map; }
	 */

	public boolean createNewEmailPriviledges(String orgType, String firstName,
			String lastName, long mobile, String email,
			String[] activePrivIdList, String[] inactivePrivIdList) {

		boolean flag = true;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;

		try {
			// verify email
			List<String> list = verifyEmailPriviledgesFromEmail(email);
			if ("NO".equalsIgnoreCase(list.get(0).trim())) {
				throw new LMSException();
			}

			con.setAutoCommit(false);

			// insert details into st_lms_report_email_user_master
			pstmt = con
					.prepareStatement("insert into st_lms_report_email_user_master (organization_id, organization_type, first_name, last_name, email_id, mob_no, registration_date, status) select organization_id, ?, ?, ?, ?, ?, ?, ? from st_lms_organization_master where organization_type=? ");
			pstmt.setString(1, orgType);
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			pstmt.setString(4, email);
			pstmt.setLong(5, mobile);
			pstmt.setTimestamp(6, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt.setString(7, "ACTIVE");
			pstmt.setString(8, orgType);
			int updateRow = pstmt.executeUpdate();

			ResultSet rs = null;
			int newUserId = -1;
			if (updateRow > 0) {
				System.out
						.println("inserttion into st_lms_report_email_user_master table is done");
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					newUserId = rs.getInt(1);
				}

				//
				for (String emailPid : inactivePrivIdList) {
					pstmt = con
							.prepareStatement(" insert into st_lms_report_email_priv_master (user_id, email_pid, status) values (?, ?, ?) ");
					pstmt.setInt(1, newUserId);
					pstmt.setInt(2, Integer.parseInt(emailPid));
					pstmt.setString(3, "INACTIVE");
					updateRow = pstmt.executeUpdate();
					System.out.println("total row inserted as inactive == "
							+ updateRow);
				}
				for (String emailPid : activePrivIdList) {
					pstmt = con
							.prepareStatement(" insert into st_lms_report_email_priv_master (user_id, email_pid, status) values (?, ?, ?) ");
					pstmt.setInt(1, newUserId);
					pstmt.setInt(2, Integer.parseInt(emailPid));
					pstmt.setString(3, "ACTIVE");
					updateRow = pstmt.executeUpdate();
					System.out.println("total row active == " + updateRow);
				}
			}
			con.commit();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
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
		return flag;
	}

	public List<String> getAgentList() {
		List<String> agentList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select name, om.organization_id   from st_lms_organization_master om, st_lms_user_master um where um.organization_id=om.organization_id and  om.organization_type='AGENT' and isrolehead = 'Y'  ");
			ResultSet rs = pstmt.executeQuery();

			System.out.println("getAgentList");
			while (rs.next()) {
				String agentName = rs.getString("name");
				int orgId = rs.getInt("organization_id");
				agentList.add(orgId + "=" + agentName);
			}
			if (agentList.size() > 0) {
				agentList.add(0, "ALL=ALL");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(agentList);
		return agentList;
	}

	public Map<String, String> getAgentUserList(int userId) {
		Map<String, String> map = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		// query update on 10 mar 09
		// String boQuery = select concat(user_name, concat('-', email_id ))
		// 'user_name', um.user_id 'user_id', 'UN_REG_USER' ref_user_id from
		// st_lms_user_master um, st_lms_user_contact_details ucd where
		// um.organization_type='AGENT' and ucd.user_id=um.user_id and
		// um.user_id not in( select ref_user_id from
		// st_lms_report_email_user_master um where ref_user_id is not null)
		// union select concat(first_name, concat(' ',concat(last_name,
		// concat('-', email_id )))) 'user_name', um.user_id 'user_id',
		// 'NEW_USER' ref_user_id from st_lms_report_email_user_master um where
		// um.organization_type='AGENT' and ref_user_id is null union select
		// concat(first_name, concat(' ',concat(last_name, concat('-', email_id
		// )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id from
		// st_lms_report_email_user_master um, st_lms_user_master bb where
		// um.organization_type='AGENT' and ref_user_id is not null and
		// bb.user_id=um.ref_user_id ;
		String boQuery = "select om.name 'org_name', concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id  from st_lms_user_master um, st_lms_user_contact_details ucd, st_lms_organization_master om where om.organization_id = um.organization_id and um.organization_type='AGENT' and ucd.user_id=um.user_id and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null)  union select om.name 'org_name', concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'NEW_USER' ref_user_id from st_lms_report_email_user_master um, st_lms_organization_master om where om.organization_id = um.organization_id and um.organization_type='AGENT' and ref_user_id is null   union   select om.name 'org_name', concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb , st_lms_organization_master om where om.organization_id = um.organization_id and um.organization_type='AGENT' and ref_user_id is not null and bb.user_id=um.ref_user_id ";
		try {
			pstmt = con.prepareStatement(boQuery);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getAgentList");
			while (rs.next()) {
				String agentOrgName = rs.getString("org_name");
				String agentName = rs.getString("user_name");
				String email[] = agentName.split("-");
				String user_id = rs.getString("user_id");
				String ref_user_id = rs.getString("ref_user_id");
				StringBuilder key = new StringBuilder();
				key.append(user_id).append("-").append(email[1]).append("-")
						.append(ref_user_id);

				// System.out.println(key +" "+ agentName);
				// System.out.print((agentName.indexOf(""+userId)==-1));
				// System.out.println(key.indexOf(""+userId)==-1);

				// if(userId!=Integer.parseInt(user_id)) { {
				// map.put(key.toString(), agentName);
				map.put(key.toString(), email[0] + "-" + agentOrgName + "-"
						+ email[1]);
				// }
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(map);
		return map;
	}

	private Map<String, Integer> getBoMasterUserIdFromMail(Connection con) {
		Map<String, Integer> map = new TreeMap<String, Integer>();

		PreparedStatement pstmt = null;
		try {

			pstmt = con
					.prepareStatement("select aa.user_id, cc.ref_user_id, cc.user_id 'email_user_id'  from st_lms_user_master aa, st_lms_role_master bb,st_lms_report_email_user_master cc  where aa.role_id = bb.role_id and bb.role_name = 'BO_MAS' and aa.isrolehead = 'Y' and aa.user_id = cc.ref_user_id");
			ResultSet rs = pstmt.executeQuery();

			System.out.println("get BO master user id");
			if (rs.next()) {
				map.put("user_id", rs.getInt("user_id"));
				map.put("email_user_id", rs.getInt("email_user_id"));
				map.put("ref_user_id", rs.getInt("ref_user_id"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, String> getBOUserList(String userType,
			String isRoleHead, int userId) {

		String query = getQueryForBO(userType, isRoleHead, userId);
		System.out.println(" to get bo detail query is === " + query);
		Map<String, String> map = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			int masterEmailUserId = -1;// , loginUserEmailUserId = -1;
			Map<String, Integer> boMasUserId = getBoMasterUserIdFromMail(con);
			if (!boMasUserId.isEmpty()
					&& boMasUserId.get("email_user_id") != null) {
				masterEmailUserId = boMasUserId.get("email_user_id");
			}

			// Map<String, Integer> loginUserIdMap = getLoginUserIdFromMail(con,
			// userId);
			// if(!loginUserIdMap.isEmpty() &&
			// loginUserIdMap.get("email_user_id") != null)
			// loginUserEmailUserId = loginUserIdMap.get("email_user_id");

			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			System.out.println("get BO user List");
			while (rs.next()) {
				String userName = rs.getString("user_name");
				String email[] = userName.split("-");
				String user_id = rs.getString("user_id");
				String ref_user_id = rs.getString("ref_user_id");
				StringBuilder key = new StringBuilder();
				key.append(user_id).append("-").append(email[1]).append("-")
						.append(ref_user_id);
				System.out.println("key === " + key);
				System.out.println("  codition = "
						+ (Integer.parseInt(user_id) != masterEmailUserId));
				if (Integer.parseInt(user_id) != masterEmailUserId) {
					map.put(key.toString(), userName);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

		System.out.println("map  == " + map);
		return map;
	}

	public List<String> getEmailPriviledges(String userId, String email,
			String orgType) {
		List<String> privlist = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {

			// verify that email id is already registered
			pstmt = con
					.prepareStatement("select aa.user_id from  st_lms_report_email_user_master aa where aa.email_id=? and aa.ref_user_id=?");
			pstmt.setString(1, email);
			pstmt.setInt(2, Integer.parseInt(userId));
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				privlist.add("REG=REG=REG");
				String newUserId = rs.getString("user_id");

				// get details of privileges from email_priv_master
				pstmt = con
						.prepareStatement("select aa.email_pid, bb.priv_title, aa.status from  st_lms_report_email_priviledge_rep  bb, st_lms_report_email_priv_master aa where bb.status = 'ACTIVE' and aa.email_pid=bb.email_pid and aa.user_id=? order by email_pid");
				pstmt.setString(1, newUserId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int emailPid = rs.getInt("email_pid");
					String privTitle = rs.getString("priv_title");
					String status = rs.getString("status");
					privlist.add(emailPid + "=" + privTitle + "=" + status);
				}
				System.out.println("this mail id already registerd ========== "
						+ newUserId);
			} else {
				privlist.add("NEW=NEW=NEW");
				pstmt = con
						.prepareStatement("select bb.email_pid, bb.priv_title from  st_lms_report_email_priviledge_rep bb where bb.status = 'ACTIVE' and priv_owner = '"
								+ orgType + "'");
				rs = pstmt.executeQuery();
				System.out.println("this mail id not  registerd ========== ");

				while (rs.next()) {
					int emailPid = rs.getInt("email_pid");
					String privTitle = rs.getString("priv_title");
					// String status=rs.getString("status");
					privlist.add(emailPid + "=" + privTitle + "=INACTIVE");
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(privlist);
		return privlist;
	}

	public List<String> getEmailPriviledgesFromEmail(String userId, String email) {
		List<String> privlist = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select bb.email_pid, bb.priv_title ,aa.status from  st_lms_report_email_priv_master aa, st_lms_report_email_priviledge_rep bb where aa.email_pid = bb.email_pid and user_id=?");
			pstmt.setInt(1, Integer.parseInt(userId));
			ResultSet rs = pstmt.executeQuery();

			System.out.println("getprivlist");
			while (rs.next()) {
				int emailPid = rs.getInt("email_pid");
				String privTitle = rs.getString("priv_title");
				String status = rs.getString("status");
				privlist.add(emailPid + "=" + privTitle + "=" + status);
			}
			if (privlist.size() > 0) {
				privlist.add(0, "ALL=ALL=ALL");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(privlist);
		return privlist;
	}

	private String getQueryForBO(String userType, String isRoleHead, int userId) {
		String boQuery = "";
		String roleHead = "%";
		if ("ROLEHEAD".equalsIgnoreCase(isRoleHead.trim())) {
			roleHead = "Y";
		} else if ("NONEROLEHEAD".equalsIgnoreCase(isRoleHead.trim())) {
			roleHead = "N";
		}

		if ("E-MAIL".equalsIgnoreCase(userType.trim())) {
			boQuery = "select concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'NEW_USER' ref_user_id from st_lms_report_email_user_master um where um.organization_type='BO' and ref_user_id is null  union  select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where ref_user_id is not null and bb.user_id=um.ref_user_id and um.ref_user_id!="
					+ userId
					+ " and um.organization_type='BO' and bb.isrolehead like '"
					+ roleHead + "'";
		} else {
			if ("ALL".equalsIgnoreCase(userType.trim())) {
				boQuery = "select concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id from st_lms_user_master um, st_lms_user_contact_details ucd where um.organization_type='BO' and ucd.user_id=um.user_id and um.user_id !="
						+ userId
						+ " and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null) and um.isrolehead like '"
						+ roleHead
						+ "' union  select concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'NEW_USER' ref_user_id from st_lms_report_email_user_master um where um.organization_type='BO' and ref_user_id is null  union  select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where um.organization_type='BO' and ref_user_id is not null and bb.user_id=um.ref_user_id and um.ref_user_id !="
						+ userId + " and bb.isrolehead like '" + roleHead + "'";
			} else if ("BO MASTER".equalsIgnoreCase(userType.trim())) {
				roleHead = "N";
				boQuery = "select concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id from st_lms_user_master um, st_lms_user_contact_details ucd where um.organization_type='BO' and ucd.user_id=um.user_id and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null) and um.isrolehead like '"
						+ roleHead
						+ "' and um.role_id=(select role_id from st_lms_role_master where role_name = 'BO_MAS') union select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where um.organization_type='BO' and ref_user_id is not null and bb.user_id=um.ref_user_id and bb.isrolehead like '"
						+ roleHead
						+ "' and bb.role_id=(select role_id from st_lms_role_master where role_name = 'BO_MAS')";
			} else if ("BO ADMIN".equalsIgnoreCase(userType.trim())) {
				boQuery = "select concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id from st_lms_user_master um, st_lms_user_contact_details ucd where um.organization_type='BO' and ucd.user_id=um.user_id and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null) and um.isrolehead like '"
						+ roleHead
						+ "' and um.role_id=(select role_id from st_lms_role_master where role_name = 'BO_ADM') union select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where um.organization_type='BO' and ref_user_id is not null and bb.user_id=um.ref_user_id and bb.isrolehead like '"
						+ roleHead
						+ "' and bb.role_id=(select role_id from st_lms_role_master where role_name = 'BO_ADM')";
			} else if ("BO ACCOUNT".equalsIgnoreCase(userType.trim())) {
				boQuery = "select concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id from st_lms_user_master um, st_lms_user_contact_details ucd where um.organization_type='BO' and ucd.user_id=um.user_id and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null) and um.isrolehead like '"
						+ roleHead
						+ "' and um.role_id=(select role_id from st_lms_role_master where role_name = 'BO_ACT') union select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where um.organization_type='BO' and ref_user_id is not null and bb.user_id=um.ref_user_id and bb.isrolehead like '"
						+ roleHead
						+ "' and bb.role_id=(select role_id from st_lms_role_master where role_name = 'BO_ACT')";
			} else if ("BO INVENTORY".equalsIgnoreCase(userType.trim())) {
				boQuery = "select concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id from st_lms_user_master um, st_lms_user_contact_details ucd where um.organization_type='BO' and ucd.user_id=um.user_id and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null) and um.isrolehead like '"
						+ roleHead
						+ "' and um.role_id=(select role_id from st_lms_role_master where role_name = 'BO_INV') union select  concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id  from st_lms_report_email_user_master um, st_lms_user_master bb where um.organization_type='BO' and ref_user_id is not null and bb.user_id=um.ref_user_id and bb.isrolehead like '"
						+ roleHead
						+ "' and bb.role_id=(select role_id from st_lms_role_master where role_name = 'BO_INV')";
			}
		}
		System.out.println("sql query for bo = " + boQuery);
		return boQuery;
	}

	public Map<String, String> getRetailerUserList(int userId, String ownerId) {
		Map<String, String> map = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String owner = "%";
		if (!"ALL".equalsIgnoreCase(ownerId.trim())) {
			owner = ownerId + "%";
		}
		// query update on 10 mar 09
		// String boQuery = "select concat(user_name, concat('-', email_id ))
		// 'user_name', um.user_id 'user_id', 'UN_REG_USER' ref_user_id from
		// st_lms_user_master um, st_lms_user_contact_details ucd,
		// st_lms_organization_master om where um.organization_type='RETAILER'
		// and ucd.user_id=um.user_id and om.organization_id=um.organization_id
		// and om.parent_id like '"+owner+"' and um.user_id not in( select
		// ref_user_id from st_lms_report_email_user_master um where ref_user_id
		// is not null ) union select concat(first_name, concat('
		// ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id
		// 'user_id', 'NEW_USER' ref_user_id from
		// st_lms_report_email_user_master um where
		// um.organization_type='RETAILER' and ref_user_id is null union select
		// concat(first_name, concat(' ',concat(last_name, concat('-', email_id
		// )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id from
		// st_lms_report_email_user_master um, st_lms_user_master bb,
		// st_lms_organization_master om where um.organization_type='RETAILER'
		// and om.organization_id=um.organization_id and om.parent_id like
		// '"+owner+"' and ref_user_id is not null and bb.user_id=um.ref_user_id
		// ";
		String boQuery = "select om.name 'org_name', concat(user_name, concat('-', email_id )) 'user_name', um.user_id 'user_id', 'UN_REG_USER'  ref_user_id  from st_lms_user_master um, st_lms_user_contact_details ucd, st_lms_organization_master om where um.organization_type='RETAILER'  and ucd.user_id=um.user_id  and om.organization_id=um.organization_id  and om.parent_id like '"
				+ owner
				+ "' and um.user_id not in( select ref_user_id from st_lms_report_email_user_master um where ref_user_id is not null ) union   select om.name 'org_name', concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'NEW_USER' ref_user_id from st_lms_report_email_user_master um, st_lms_organization_master om where om.organization_id = um.organization_id and  um.organization_type='RETAILER' and ref_user_id is null   union   select  om.name 'org_name', concat(first_name, concat(' ',concat(last_name, concat('-', email_id )))) 'user_name', um.user_id 'user_id', 'REG_USER' ref_user_id   from st_lms_report_email_user_master um, st_lms_user_master bb, st_lms_organization_master om  where um.organization_type='RETAILER'  and om.organization_id=um.organization_id and om.parent_id like '"
				+ owner
				+ "' and ref_user_id is not null  and bb.user_id=um.ref_user_id";
		try {
			pstmt = con.prepareStatement(boQuery);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getAgentList");
			while (rs.next()) {
				String retOrgName = rs.getString("org_name");
				String retName = rs.getString("user_name");
				String email[] = retName.split("-");
				String user_id = rs.getString("user_id");
				String ref_user_id = rs.getString("ref_user_id");
				StringBuilder key = new StringBuilder();
				key.append(user_id).append("-").append(email[1]).append("-")
						.append(ref_user_id);

				// System.out.println(key +" "+ agentName);
				// System.out.print((agentName.indexOf(""+userId)==-1));
				// System.out.println(key.indexOf(""+userId)==-1);

				// if(userId!=Integer.parseInt(user_id)) {
				// map.put(key.toString(), agentName);
				map.put(key.toString(), email[0] + "-" + retOrgName + "-"
						+ email[1]);
				// }
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(map);
		return map;
	}

	public void updateEmailPriviledges(String userId,
			String[] activePrivIdList, String[] inactivePrivIdList) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			pstmt = con
					.prepareStatement(" update st_lms_report_email_priv_master set status='INACTIVE' where user_id =?");
			pstmt.setInt(1, Integer.parseInt(userId));
			int updateRow = pstmt.executeUpdate();
			System.out.println("total row inactive == " + updateRow);

			for (String emailPid : activePrivIdList) {
				pstmt = con
						.prepareStatement("update st_lms_report_email_priv_master set status='ACTIVE' where user_id=? and email_pid=?");
				pstmt.setInt(1, Integer.parseInt(userId));
				pstmt.setInt(2, Integer.parseInt(emailPid));
				updateRow = pstmt.executeUpdate();
				System.out.println("total row active == " + updateRow);
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

	}

	public List<String> verifyEmailPriviledgesFromEmail(String email) {
		List<String> privlist = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {

			// verify that email id is already registered
			pstmt = con
					.prepareStatement("select aa.user_id from  st_lms_report_email_user_master aa where aa.email_id=? and aa.ref_user_id is null");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("query is === " + pstmt);
			if (rs.next()) {
				privlist.add("NO");

			} else {
				// privlist.add("");
				pstmt = con
						.prepareStatement("select bb.email_pid, bb.priv_title from  st_lms_report_email_priviledge_rep bb where bb.status = 'ACTIVE' and priv_owner = 'BO'");
				rs = pstmt.executeQuery();
				System.out.println("this mail id not  registerd ========== ");

				while (rs.next()) {
					int emailPid = rs.getInt("email_pid");
					String privTitle = rs.getString("priv_title");
					privlist.add(emailPid + "=" + privTitle + "=INACTIVE");
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		System.out.println(privlist);
		return privlist;
	}

}
